# Development environment

## Actual topology

Routine backend development uses local source code against the existing
staging PostgreSQL and RabbitMQ services on the Almonium server. A separately
installed local database is not the normal workflow.

The neighboring `almonium-infra` repository is the source of truth for
deployed topology and credentials:

- `ansible/vars/apps/almonium/vars.yaml` defines the production and staging
  database names/users and RabbitMQ users/vhosts;
- encrypted `vault.*.yaml` files hold passwords and provider secrets;
- `services/postgres/docker-compose.yaml` exposes PgBouncer only on server
  loopback port `6432`;
- the RabbitMQ role exposes AMQP only on server loopback port `5672`;
- application deployment is performed by
  `ansible/playbook-deploy-almonium-be.yaml`.

Production and staging are isolated by database credentials and RabbitMQ
vhosts, but they share one server. Use staging for routine development.
Connecting a local process to production should be an explicit,
incident/debugging-only decision.

## First-time setup

Requirements:

- JDK 21;
- Docker with Docker Compose for Testcontainers and an optional local RabbitMQ;
- SSH access configured under the `oci` host alias;
- development credentials for the external providers initialized at startup.

Copy the tracked template and fill its placeholders:

```bash
cp .env.template .env
```

`.env` and other `.env*` working files are ignored. Never commit their secrets.
The `dev` Spring profile imports `.env` and keeps application email delivery in
dry-run mode.

## Connect to staging services

The infra repository binds PgBouncer and RabbitMQ to server loopback rather
than exposing them to the Internet. Open both tunnels before starting the
backend:

```bash
ssh -N \
  -L 6432:127.0.0.1:6432 \
  -L 5672:127.0.0.1:5672 \
  oci
```

The staging defaults in `.env.template` then connect to:

| Service | Local endpoint | Staging identity |
| --- | --- | --- |
| PostgreSQL via PgBouncer | `127.0.0.1:6432` | database/user `almonium_staging` |
| RabbitMQ | `127.0.0.1:5672` | user `almonium_staging`, vhost `/almonium_staging` |

Passwords remain in the appropriate encrypted infra vault and must be copied
into the untracked `.env` through the normal secret-handling workflow.

Starting a local application can run pending Liquibase changes against the
selected database. Review migrations and confirm `DB_NAME`,
`DB_USERNAME`, and `RABBITMQ_VHOST` before startup. In particular, do not
switch those values to production merely to obtain realistic data.

## Start and verify the backend

Start the application after the tunnels are established:

```bash
./mvnw spring-boot:run
```

With the template defaults, the API is available at
`http://localhost:8080/api/v1`.

The full verification suite uses isolated Testcontainers PostgreSQL rather
than either deployed database:

```bash
./mvnw verify
```

Docker must be available to the Maven process. The application-context test
also expects RabbitMQ on `localhost:5672`; either keep the staging tunnel open
or start the disposable local broker:

```bash
docker compose -f docker-compose.local.yaml up -d rabbitmq
```

Do not run the local broker and the RabbitMQ SSH tunnel on port `5672` at the
same time. Stop the local broker with:

```bash
docker compose -f docker-compose.local.yaml down
```

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
| Stripe | `STRIPE_KEY`, `STRIPE_WEBHOOK_SECRET` | Stripe API and webhook verification. |
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

- PostgreSQL or RabbitMQ connection refused: confirm the SSH process is still
  running and the local ports are listening.
- Authentication failure: confirm the staging database user/password or
  RabbitMQ user/password/vhost combination from the infra configuration.
- Google credential decoding/parsing failure: Base64-encode the complete
  service-account JSON as one line.
- Testcontainers cannot find Docker: verify `docker info` succeeds for the same
  user running Maven.
- Port already allocated: stop the conflicting tunnel/local container or use
  different local tunnel ports and update `.env`.
- PostgreSQL reports that `varchar` cannot be cast to `cefr_level`: after
  Liquibase creates the enum, add the cast once in the selected schema:

  ```sql
  CREATE CAST (varchar AS public.cefr_level) WITH INOUT AS IMPLICIT;
  ```
