# Development environment

## Actual topology

Routine backend development runs source code on the developer machine with the
`local` Spring profile. PostgreSQL and RabbitMQ run in Docker Compose and keep
their state in named local volumes. Liquibase is enabled and applies changes
only to the local PostgreSQL database.

The neighboring `almonium-infra` repository is the source of truth for
deployed staging and production topology and credentials:

- `ansible/vars/apps/almonium/vars.yaml` defines the production and staging
  database names/users and RabbitMQ users/vhosts;
- encrypted `vault.*.yaml` files hold passwords and provider secrets;
- `services/postgres/docker-compose.yaml` exposes PgBouncer only on server
  loopback port `6432`;
- the RabbitMQ role exposes AMQP only on server loopback port `5672`;
- application deployment is performed by
  `ansible/playbook-deploy-almonium-be.yaml`.

Production and staging are isolated by database credentials and RabbitMQ
vhosts, but they share one server. Their migrations are applied by the
reviewed deployment artifact. Connecting a local process to either deployed
database should be an explicit incident/debugging decision, not the normal
development workflow.

## First-time setup

Requirements:

- JDK 25;
- Docker with Docker Compose for PostgreSQL, RabbitMQ, and Testcontainers;
- development credentials for the external providers initialized at startup.

The Docker CLI must be connected to a running daemon before any Compose or
Testcontainers command will work. Starting a package-managed Docker service is
not enough if your shell is pointed at a different Docker context or socket.
Check `docker info` first; if it fails, fix the Docker daemon/context before
starting local services.

Copy the tracked template and fill its placeholders:

```bash
cp .env.template .env
```

`.env` and other `.env*` working files are ignored. Never commit their secrets.
The base Spring configuration imports `.env`; the template selects the `local`
profile and keeps application email delivery in dry-run mode.

If `.env` predates the local database workflow, change `SPRING_PROFILE` to
`local`. The local profile pins its database and broker connection to the
matching Compose services, so stale staging connection values in an older
`.env` cannot redirect local Liquibase.

## Start local infrastructure

Start PostgreSQL and RabbitMQ together with:

```bash
docker compose -f docker-compose.local.yaml up -d --wait postgres rabbitmq
```

If this fails with a socket error such as
`Cannot connect to the Docker daemon at unix:///home/ok/.docker/desktop/docker.sock`,
the CLI is pointing at Docker Desktop while the daemon is not running. Start
the matching Docker runtime for your current context, or switch the CLI back to
a working context, then rerun the Compose command.

| Service | Local endpoint | Local identity |
| --- | --- | --- |
| PostgreSQL | `127.0.0.1:5432` | database `almonium_local`, user `almonium` |
| RabbitMQ | `127.0.0.1:5672` | user `almonium`, vhost `/almonium_local` |

The credentials are intentionally local-only and match `.env.template`.
PostgreSQL data and RabbitMQ state survive container recreation in named
volumes.

## Profiles and Liquibase contexts

`application.yaml` uses `SPRING_PROFILE` for both the active Spring profile and
the Liquibase context. This is an intentional naming convention:

| Spring profile | Liquibase context | Database |
| --- | --- | --- |
| `local` | `local` | Docker `almonium_local` |
| `staging` | `staging` | Deployed `almonium_staging` |
| `prod` | `prod` | Deployed `almonium_prod` |
| `test` | `test` | Testcontainers PostgreSQL |

Local IDE run configurations must therefore set `SPRING_PROFILE=local` (or
otherwise activate the `local` profile while keeping `SPRING_PROFILE`
consistent). There is no `dev` alias.

Contexts filter environment-specific data changesets; they do not define the
database connection. Connection settings remain profile-specific. Changesets
without a context run in every environment. When adding an environment, use
the same name for its Spring profile and Liquibase context rather than adding a
second mapping in an environment-specific configuration file.

## Start and verify the backend

Start the application after both containers report healthy:

```bash
./mvnw spring-boot:run
```

With the template defaults, the API is available at
`http://localhost:8080/api/v1`. Liquibase applies pending changes to
`almonium_local` during startup.

Stop the local services without deleting their data:

```bash
docker compose -f docker-compose.local.yaml down
```

To rebuild both disposable services from empty local volumes:

```bash
docker compose -f docker-compose.local.yaml down --volumes
docker compose -f docker-compose.local.yaml up -d --wait postgres rabbitmq
```

The `--volumes` command permanently deletes only the local Compose database
and broker state.

## Migration policy

- Develop and test new changesets with the `local` profile and local
  PostgreSQL.
- Once a schema patch is committed, treat its file, ID, author, and contents as
  immutable. Corrections go into a new patch. Context-only seed configuration
  can be extended deliberately when a new environment reuses the same data.
- Pushes to `develop` deploy the `staging` profile; that deployment applies
  reviewed migrations to `almonium_staging`.
- Production applies the same committed migration chain through the manual
  `main` deployment.
- Do not run uncommitted migrations against staging or production. A local
  process connecting to either deployed database must explicitly disable
  Liquibase.

The full verification suite uses isolated Testcontainers PostgreSQL rather
than either deployed database:

```bash
./mvnw verify
```

Docker must be available to the Maven process. The application-context test
also expects RabbitMQ on `localhost:5672`, so keep the local Compose broker
running during `verify`.

Spotless, SpotBugs/FindSecBugs, and ArchUnit are part of `verify`. Apply
intentional formatting with `./mvnw spotless:apply`.

To build the same application image shape used by deployment:

```bash
./mvnw package
docker build -t almonium-be:local .
```

## Environment-variable catalogue

`.env.template` is the canonical developer template.

| Group | Variables | Purpose |
| --- | --- | --- |
| Application | `SPRING_PROFILE`, `LOCAL_PORT`, `CONTEXT_PATH`, `DEBUG_PORT` | Spring profile, HTTP context, and local/debug ports. |
| PostgreSQL | `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_SCHEMA`, `DB_USERNAME`, `DB_PASSWORD` | JDBC connection and Liquibase/Hibernate schema. |
| RabbitMQ | `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_HOST_PORT`, `RABBITMQ_USER`, `RABBITMQ_PASS`, `RABBITMQ_VHOST` | Broker connection; `RABBITMQ_HOST_PORT` is used only by local Compose. |
| Google/Firebase | `GOOGLE_PROJECT_ID`, `FIREBASE_STORAGE_BUCKET`, `GOOGLE_SERVICE_ACCOUNT_KEY_BASE64` | Firebase Auth/storage and Google Translation/Text-to-Speech. |
| Stream | `STREAM_KEY`, `STREAM_SECRET` | Stream Chat server integration. |
| Paddle Billing | `PADDLE_ENVIRONMENT`, `PADDLE_CLIENT_TOKEN`, `PADDLE_API_KEY`, `PADDLE_WEBHOOK_SECRET`, `PADDLE_PRICE_PREMIUM_MONTHLY`, `PADDLE_PRICE_PREMIUM_ANNUAL`, `PADDLE_PRICE_FOUNDER_MONTHLY`, `PADDLE_PRICE_FOUNDER_ANNUAL` | Paddle API, Paddle.js, webhook verification, and environment-specific catalog price IDs. |
| Email/dictionaries | `ZEPTO_API_KEY`, `RAPID_API_KEY`, `WORDNIK_KEY`, `YANDEX_KEY` | Email delivery and dictionary providers. |

Google/Firebase clients are initialized eagerly, so
`GOOGLE_SERVICE_ACCOUNT_KEY_BASE64` must contain a valid Base64-encoded service
account JSON even when the current change does not exercise those integrations.
Other provider values are validated as nonblank during startup.

The infra deployment template may still contain environment variables retained
from removed custom-auth or AI integrations. They are not part of the backend
developer template unless current application configuration consumes them.

## Environment ownership

- Change backend configuration keys and validation in this repository.
- Change database tenants, RabbitMQ vhosts, server topology, deployed
  environment values, or encrypted secrets in `../almonium-infra`.
- Keep `.env.template` aligned with variables consumed by
  `src/main/resources/application*.yaml`.
- Validate an infrastructure change using the instructions in the infra
  repository's `AGENTS.md`; do not copy decrypted vault contents into this
  repository.

## Common failures

- PostgreSQL or RabbitMQ connection refused: run the combined Compose startup
  command and inspect `docker compose -f docker-compose.local.yaml ps`.
- Authentication failure: confirm the containers were created from the current
  Compose file; the local profile and Compose use the same local-only
  credentials.
- Google credential decoding/parsing failure: Base64-encode the complete
  service-account JSON as one line.
- Testcontainers cannot find Docker: verify `docker info` succeeds for the same
  user running Maven.
- `docker compose` cannot connect to the daemon: verify the active Docker
  context and socket match the runtime you started; starting `snap docker`
  alone does not help if the CLI still targets Docker Desktop.
- Port already allocated: stop the process using local port `5432` or `5672`
  before starting the Compose services.
- PostgreSQL reports that `varchar` cannot be cast to `cefr_level`: after
  Liquibase creates the enum, add the cast once in the selected schema:

  ```sql
  CREATE CAST (varchar AS public.cefr_level) WITH INOUT AS IMPLICIT;
  ```
