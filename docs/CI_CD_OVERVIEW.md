# CI/CD Architecture Overview

Review date: 2026-07-18. This document explains how the Almonium backend moves
from a Git commit to the running production or staging service. It covers both
this repository and the adjacent `almonium-infra` repository. It is intended as
an onboarding map, an interview-ready explanation, and an operational outline;
the workflow and infrastructure files remain the executable source of truth.

## Executive summary

Almonium uses a deliberately small, self-managed delivery platform:

- GitHub Actions is the CI/CD control plane.
- Maven Wrapper on Java 21 verifies and packages the Spring Boot application.
- Docker Buildx creates one `linux/arm64` image and pushes it to GitHub
  Container Registry (GHCR), tagged with a Git commit SHA.
- The backend workflow checks out `almonium-infra` and runs its Ansible backend
  playbook over SSH.
- Ansible renders an environment-specific Compose file, pulls the exact image
  tag, starts the next blue/green slot, and polls Spring Boot Actuator.
- Traefik discovers the container through Docker labels and provides HTTPS with
  Porkbun DNS-01 certificates. Once the new slot is healthy, Ansible retires
  the previous slot.
- Production and staging share one Oracle Cloud host and the platform-level
  PostgreSQL/PgBouncer, RabbitMQ, Traefik, and Docker networks, while using
  separate application containers, databases/users, RabbitMQ vhosts/users,
  secrets, hostnames, and Spring profiles.

This is closer to a lightweight, GitHub-driven platform than to a collection
of shell scripts, but it is not Kubernetes or a multi-node highly available
system. The design optimizes for understandable operations and low cost on one
ARM server.

## The 60-second version

```text
developer pushes develop                         operator dispatches prod
          |                                                |
          v                                                v
 staging-pipeline.yaml                              prod-pipeline.yaml
          +----------------------+-------------------------+
                                 |
                                 v
                 reusable-build-and-deploy.yaml
                    resolve SHA / existing tag
                         |              |
                    build needed?       | redeploy
                         v              |
                  ./mvnw -B verify      |
                         v              |
                   layered ARM64 image  |
                         v              |
             ghcr.io/.../almonium-be:<SHA>
                                 |
                                 v
                    reusable-deploy.yaml
                  checkout almonium-infra
                  prepare Ansible vault IDs
                                 |
                          SSH + Ansible
                                 v
              Oracle Cloud host / Docker Compose
             render next slot -> pull -> start -> health
                                 |
                   Traefik route + TLS discovery
                                 |
                    retire previous healthy slot
```

The artifact is the contract between CI and CD. Build and deployment are
separable: a known image tag can be deployed again without rebuilding source.

## Repository responsibilities

| Repository | Owns |
| --- | --- |
| `almonium-be` | Java/Kotlin source, tests, Maven build, Dockerfile, application profiles, pipeline entry points, image publication, and invocation of backend deployment |
| `almonium-infra` | Host inventory, bootstrap, Ansible playbooks and roles, environment topology, encrypted configuration, Compose templates, shared data/broker stacks, Traefik, TLS, and server synchronization |
| GitHub configuration | Actions secrets, environment-scoped secrets/protection rules, repository/package permissions, branch protection, and workflow approvals; these settings are not fully visible in Git |
| Oracle Cloud host | Docker runtime, deployment slot files, shared networks, persistent data, and the `/home/almonium/infra` operational checkout |

The boundary is intentional: application CI decides **which backend image** to
deploy, while the infrastructure repository decides **how and where it runs**.

## Pipeline entry points

### Normal environment pipelines

| Workflow | Trigger | Source/environment | Result |
| --- | --- | --- | --- |
| `staging-pipeline.yaml` | Push to `develop`, or manual dispatch | `develop` / `staging` | Builds a new SHA image unless an existing tag is supplied, then deploys staging |
| `prod-pipeline.yaml` | Manual dispatch only | `main` / `prod` | Builds and deploys, or redeploys a supplied existing image tag |

Production is deliberately not deployed automatically on a push. Promotion is
an operator decision, and the optional `image_tag` supports deploying an
already-built artifact. In practice, the safest promotion model is to deploy
the same verified SHA tag to production rather than rebuild equivalent source.

### Utility and reusable workflows

| Workflow | Purpose |
| --- | --- |
| `reusable-build-and-deploy.yaml` | Resolves whether to build or reuse an image, then composes the reusable build and deploy workflows |
| `reusable-build.yaml` | Verifies source, builds the container, and publishes it to GHCR |
| `reusable-deploy.yaml` | Selects GitHub environment, checks out infra, prepares vault credentials, and runs Ansible |
| `build-only.yaml` | Manually publishes an image without deployment |
| `deploy-only.yaml` | Manually deploys a supplied image tag to staging or production |

The reusable workflows avoid duplicating staging and production mechanics.
`build-only` and `deploy-only` are useful for recovery, promotion, and testing
the two halves independently.

## CI: source to immutable application artifact

The build job runs on a GitHub-hosted Ubuntu runner:

1. Check out application source.
2. Install Temurin JDK 21 and enable the Maven dependency cache.
3. Run `./mvnw -B verify`.
4. Configure Docker Buildx.
5. Authenticate to GHCR with the workflow's short-lived `GITHUB_TOKEN`.
6. Build for `linux/arm64`, use the GitHub Actions layer cache, and push
   `ghcr.io/almonium-platform/almonium-be:<image_tag>`.

`verify` is the quality gate. It includes compilation, test execution, Spring
Boot packaging, and the Spotless check bound to Maven's `validate` phase. The
Maven Wrapper makes the local and CI Maven entry point the same. Integration
tests use Testcontainers, so their successful execution depends on Docker being
available on the runner.

The Dockerfile expects the JAR to exist before the container build. Its first
stage extracts Spring Boot layers from `target/*.jar`; its runtime stage copies
dependencies, loader, snapshot dependencies, and application classes into a
Temurin 21 JRE image. This gives Docker reusable layers without compiling the
project inside Docker.

Only ARM64 is published because the current Oracle host is ARM-based. Supporting
different hosts would require a multi-platform image such as
`linux/amd64,linux/arm64` or separate platform tags.

## CD: artifact to a running slot

The deployment workflow runs as a GitHub job bound to the requested `staging`
or `prod` environment. It:

1. Checks out the private `almonium-infra` repository with a dedicated SSH
   checkout key.
2. Writes the all/shared/environment Ansible vault passwords to mode-restricted
   temporary files on the runner.
3. Selects the `almonium_prod` or `almonium_staging` vault ID.
4. Runs `playbook-deploy-almonium-be.yaml`, limited to the `almonium` inventory
   host, using the cloud SSH key.
5. Passes only deployment coordinates as extra variables: environment, image
   tag, GHCR actor, and a package-read token.

The playbook loads three configuration layers:

- unencrypted application topology from `vars/apps/almonium/vars.yaml`;
- encrypted secrets shared by both environments from `vault.shared.yaml`;
- encrypted production or staging secrets from the matching environment vault.

Schema files beside the vaults document required secret keys without exposing
values. GitHub stores the keys needed to reach and decrypt the infrastructure;
Ansible Vault stores the application/runtime secrets committed in encrypted
form.

### Slot rollout

Production and staging each alternate between a `blue` and `green` container.
The host keeps a small `.next_color_<environment>` file that tells the next run
which slot to replace. Each environment/slot has its own rendered Compose
directory under `/home/almonium/deploy_slots` and a loopback-only health port:

| Environment | Blue check | Green check | Public host |
| --- | ---: | ---: | --- |
| staging | `127.0.0.1:9978` | `127.0.0.1:9979` | `staging.api.almonium.com` |
| production | `127.0.0.1:9988` | `127.0.0.1:9989` | `api.almonium.com` |

For the target slot, the role ensures Docker/Compose are available, renders
the Compose file, retries the GHCR pull, recreates the application container,
and polls `/api/v1/actuator/health` every three seconds for up to roughly three
minutes. The endpoint is publicly permitted by Spring Security, but the slot
check itself uses a host-loopback port.

If Actuator reports `UP`, the playbook attempts to stop/remove the previous
slot and advances the color file. If the check fails, Ansible fails before
those two steps, so the previous container remains. A rollback is a manual
redeployment of the previous known-good image tag; there is no automatic
post-cutover rollback controller. The current cleanup command has an
implementation defect described below, so removal is intended but not fully
enforced.

This is blue/green-inspired rather than a fully atomic load-balancer switch.
Both slots can temporarily advertise equivalent Traefik router rules while the
new one is checked, and traffic selection is driven by Docker discovery and
container health rather than an explicit router pointer.

## Runtime topology

```text
Internet
   |
   v
Traefik :80/:443
  - redirects HTTP to HTTPS
  - obtains/renews certificates with Porkbun DNS-01
  - discovers opted-in Docker containers
   |
   +--> almonium_prod_blue OR almonium_prod_green
   |        Spring profile: prod
   |        hostname: api.almonium.com
   |
   +--> almonium_staging_blue OR almonium_staging_green
            Spring profile: staging
            hostname: staging.api.almonium.com

Application containers attach to:
  proxy-net  -> Traefik
  db-net     -> PgBouncer :6432 -> PostgreSQL
  broker-net -> RabbitMQ :5672
```

Traefik terminates TLS and forwards to the application's internal port 8080.
Only the two health ports bind to host loopback. The Traefik dashboard,
RabbitMQ management UI, and RabbitMQ's direct AMQP troubleshooting port are
also loopback-only and are intended to be reached through SSH tunnels.

Liquibase runs with the active Spring profile/context when the application
starts. Database changes therefore ride with the application artifact, while
database server provisioning, users, PgBouncer, persistence, and backups are
owned by the infrastructure repository. This makes backward-compatible schema
changes especially important during a two-slot rollout.

## Environment and secret flow

The rendered Compose template is the final configuration junction:

```text
plain infra vars --------------------+
shared encrypted Ansible vault ------+--> Compose environment --> Spring Boot
prod/staging encrypted vault --------+
image tag from backend workflow -----+--> Compose image
Traefik labels from template --------+--> public routing and TLS
```

Plain vars hold non-secret topology such as hostnames, Spring profiles,
database names/users, RabbitMQ vhosts/users, Firebase project IDs, and service
names. Vaults hold passwords, the Google service-account credential,
third-party API keys, mail credentials, and service-account material.

The application receives configuration through environment variables and
activates `application-prod.yaml` or `application-staging.yaml`. Environment
separation is logical, not physical: a secret or database mistake can still
affect services on the same host, so vault review and least privilege matter.

## Infrastructure delivery around the application

The adjacent repository has its own GitHub Actions lifecycle:

- every push to `main` synchronizes the desired infra checkout to
  `/home/almonium/infra`;
- path-filtered workflows deploy PostgreSQL/PgBouncer and RabbitMQ through
  Ansible when their definitions change;
- a path-filtered workflow updates and applies Traefik over SSH;
- other path-filtered workflows manage personal/static stacks;
- application backend/frontend playbooks are normally invoked from the
  respective application repositories with a concrete image tag.

The server is bootstrapped once with Ansible. Bootstrap installs Docker,
Compose, Git, creates the `almonium` user and state directories, and creates
the external `proxy-net`, `db-net`, and `broker-net` networks. Normal
deployments then converge the smaller application or shared-stack scope.

PostgreSQL data and backups are outside disposable application containers. The
infra repository configures daily custom-format tenant dumps plus encrypted
restic snapshots to Backblaze B2. This is operational resilience, not part of
the backend deploy transaction.

## Routine operating playbook

### Deploy a normal staging change

1. Merge or push the intended commit to `develop`.
2. Watch `Deploy to Staging`: Maven verification must pass, then the SHA image
   must publish, then Ansible must report the target slot healthy.
3. Confirm the deployed SHA and perform behavior-level smoke tests against the
   staging hostname.

### Promote to production

1. Ensure the intended commit is on `main` and has passed the relevant checks.
2. Prefer a known, already-tested SHA image tag when provenance is confirmed.
3. Manually dispatch `Deploy to Production` with that tag.
4. Confirm Actuator health and run a user-facing smoke test.

### Roll back or redeploy

1. Identify the last known-good GHCR SHA tag.
2. Run `Deploy Only`, or the environment pipeline with `image_tag` populated.
3. The normal slot and health-check process deploys that artifact; no source
   revert or rebuild is required.

### Change runtime infrastructure or secrets

- Change topology, Compose templates, roles, or playbooks in `almonium-infra`.
- Change encrypted values with the correct existing Ansible vault ID; update
  the matching schema when the secret shape changes.
- Let the relevant infra workflow apply shared-stack changes, or dispatch it
  manually. Backend template/playbook changes take effect on the next backend
  deployment unless applied explicitly.

## What is automated, and what remains manual

Automated:

- formatting gate, compilation, tests, and JAR packaging;
- ARM64 container build, cache, authentication, and GHCR publication;
- environment config rendering and secret selection;
- image pull, slot recreation, Actuator polling, and old-slot retirement;
- HTTPS routing/certificate management and selected shared-stack convergence;
- repeatable deployment of an existing artifact.

Manual or external to the repository:

- production approval/dispatch and behavior-level smoke testing;
- rollback decision and previous-tag selection;
- GitHub branch/environment/package policy administration;
- first host bootstrap, DNS/cloud provisioning, vault password custody, and
  disaster recovery;
- database restore testing and application-aware migration rollback;
- monitoring/alerting beyond the deployment-time health request.

## Current risks and improvement order

These are important distinctions between the intended design and the guarantees
currently enforced by code.

### P0: make build success a hard deployment prerequisite

The orchestrator's deploy job uses `if: always()`. That is necessary when the
build is intentionally skipped for an existing image, but it also attempts CD
after a failed build. For a new SHA the image pull will normally fail, yet the
pipeline should express the invariant directly: deploy only when resolution
succeeded and either the build succeeded or `build_needed == false`.

### P0: bind image tags to the exact checked-out source

Tag resolution uses the workflow-context `github.sha`, while reusable build
jobs perform their own default checkout. Checking out `source_branch` in an
earlier job does not change `github.sha` or the later job's checkout. This can
mislabel images when a manual workflow is launched from a different ref.
`build-only` has the same risk: it reads a selected branch SHA in one job, then
the reusable build checks out its default ref in another job.

Resolve an explicit commit SHA, pass it into the build workflow, check out that
exact SHA there, and tag the resulting image with the same SHA. Record the
digest as deployment output. This is the core provenance invariant.

### P1: serialize deployments per environment

No workflow defines a concurrency group. Two staging or production runs can
race on the next-color file, the same slot directory/container, and removal of
the previous slot. Add an environment-specific concurrency group and decide
whether a newer run should cancel or queue behind an active deployment.

### P1: make traffic cutover and health semantics explicit

The new and old containers temporarily expose equivalent host rules with equal
priority. Use an explicit active-router/service switch, or document and test
Traefik's behavior for simultaneous routers and unhealthy containers. The
Compose health check invokes `curl`, but the application Dockerfile does not
install it; use a health-check mechanism guaranteed to exist in the runtime
image. Keep the independent Ansible poll, and add a post-route HTTPS smoke test
before declaring success.

### P1: fix previous-slot cleanup

The playbook passes `docker stop ... && docker rm ...` to
`ansible.builtin.command`. That module does not interpret shell operators, and
the task also suppresses failure. The valid container can be stopped while the
`&& docker rm` tokens are treated as extra `docker stop` arguments, leaving a
stopped container instead of removing it. Replace this with idempotent
`community.docker` tasks (preferred), or two explicit command tasks with
checked results.

### P1: add pull-request CI and stronger release evidence

There is no repository-defined `pull_request` workflow; the main automatic
backend workflow is a staging deployment after a push to `develop`. Add a
non-deploying PR workflow that runs the Maven gate. Consider image digest
promotion, OCI labels, SBOM/provenance attestations, vulnerability scanning,
and signed images. SHA-shaped tags are a good convention but tags remain
mutable registry references.

### P1: harden automation trust boundaries

Third-party actions are version-tag pinned rather than commit-SHA pinned, and
Ansible disables SSH host-key checking. Pin actions to reviewed commits, enable
host verification with a managed `known_hosts`, minimize token lifetime and
scope, avoid exposing tokens in process arguments, and periodically rotate the
infra checkout key, cloud key, and vault passwords.

### P2: reduce single-host and shared-service blast radius

Production, staging, edge, database, broker, and other projects share one
machine. Docker networks and per-environment credentials provide useful logical
isolation, but host failure, resource exhaustion, Docker daemon compromise, or
an unsafe infrastructure change can affect everything. This may be the right
cost tradeoff now; document recovery objectives, test restores, add resource
limits and monitoring, and move critical components only when reliability
requirements justify the operational cost.

## Interview framing

A concise way to explain the design is:

> I split application delivery from infrastructure ownership. GitHub Actions
> verifies the Spring Boot service with the Maven Wrapper, packages a layered
> ARM64 image, and publishes a commit-tagged artifact to GHCR. The backend then
> invokes a versioned Ansible playbook from a separate infrastructure repo.
> Ansible selects environment-specific vaults, renders Compose, deploys the next
> blue/green slot on an Oracle ARM host, waits for Actuator, and only then
> removes the old slot. Traefik discovers the container and handles HTTPS. The
> same artifact can be promoted or rolled back without rebuilding, while
> PostgreSQL, RabbitMQ, networks, TLS, and backups have independent infra
> lifecycles.

The follow-up engineering discussion is equally important: this setup is
reproducible and understandable, but current work should tighten source/image
provenance, deployment gating and concurrency, explicit traffic switching,
supply-chain pinning, and single-host recovery.

## Source map

Backend repository:

- `.github/workflows/staging-pipeline.yaml`
- `.github/workflows/prod-pipeline.yaml`
- `.github/workflows/reusable-build-and-deploy.yaml`
- `.github/workflows/reusable-build.yaml`
- `.github/workflows/reusable-deploy.yaml`
- `.github/workflows/build-only.yaml`
- `.github/workflows/deploy-only.yaml`
- `Dockerfile`
- `pom.xml`
- `src/main/resources/application.yaml`
- `src/main/resources/application-{staging,prod}.yaml`

Infrastructure repository:

- `README.md`
- `.github/workflows/*.yaml`
- `ansible/playbook-deploy-almonium-be.yaml`
- `ansible/roles/deploy_almonium_be_slot/`
- `ansible/vars/apps/almonium/`
- `ansible/group_vars/all/vars.yaml`
- `ansible/playbook-bootstrap-host.yaml`
- `ansible/roles/bootstrap_host/`
- `traefik/docker-compose.yaml`
