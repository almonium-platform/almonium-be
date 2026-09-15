# CI/CD Architecture Overview

Review date: 2026-07-25. This document explains how the Almonium backend moves
from a Git commit to the running production or staging service. It covers both
this repository and the adjacent `almonium-infra` repository. It is intended as
an onboarding map, an interview-ready explanation, and an operational outline;
the workflow and infrastructure files remain the executable source of truth.

## Executive summary

Almonium uses a deliberately small, self-managed delivery platform:

- GitHub Actions is the CI/CD control plane.
- Maven Wrapper on Java 25 verifies and packages the Spring Boot application.
- Docker Buildx creates one `linux/arm64` image and pushes it to GitHub
  Container Registry (GHCR), tagged with the exact checked-out Git commit SHA.
- The pushed image digest is recorded and is the immutable reference passed to
  deployment and promotion.
- The backend workflow checks out `almonium-infra` and runs its Ansible backend
  playbook over SSH.
- Ansible renders an environment-specific Compose file, pulls the exact image
  digest, starts the next blue/green slot, and polls Spring Boot Actuator.
- Ansible atomically updates Traefik's dynamic file-provider route after the
  new slot is healthy. Once an external HTTPS check confirms that exact slot,
  Ansible retires the previous slot.
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
                   resolve SHA / existing digest
                         |              |
                    build needed?       | redeploy
                         v              |
                  ./mvnw -B verify      |
                         v              |
                   layered ARM64 image  |
                         v              |
        tag: ghcr.io/.../almonium-be:<SHA>
      deploy: ghcr.io/.../almonium-be@sha256:<digest>
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
                   explicit Traefik route + TLS
                                 |
                    retire previous healthy slot
```

The artifact is the contract between CI and CD. Build and deployment are
separable: a recorded image digest can be deployed again without rebuilding
source. The commit tag makes the image discoverable; the digest makes the
deployment immutable even if a registry tag is later moved.

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
| `staging-pipeline.yaml` | Push to `develop`, or manual dispatch | `develop` / `staging` | Builds an exact-SHA image unless an existing digest is supplied, then deploys staging by digest |
| `prod-pipeline.yaml` | Manual dispatch only | `main` / `prod` | Builds and deploys, or promotes/redeploys a supplied existing image digest |

Production is deliberately not deployed automatically on a push. Promotion is
an operator decision, and the optional `image_digest` deploys an already-built
artifact. Production promotion uses the exact digest tested in staging rather
than resolving a mutable tag or rebuilding equivalent source.

### Utility and reusable workflows

| Workflow | Purpose |
| --- | --- |
| `reusable-build-and-deploy.yaml` | Resolves one commit SHA or accepts an existing digest, then composes the reusable build and deploy workflows |
| `reusable-build.yaml` | Checks out the resolved SHA, verifies source, publishes the SHA-tagged container, and returns its digest |
| `reusable-deploy.yaml` | Selects GitHub environment, checks out infra, prepares vault credentials, and runs Ansible |
| `build-only.yaml` | Manually publishes an image without deployment |
| `deploy-only.yaml` | Manually deploys a supplied image digest to staging or production |

The reusable workflows avoid duplicating staging and production mechanics.
`build-only` and `deploy-only` are useful for recovery, promotion, and testing
the two halves independently.

## CI: source to immutable application artifact

The resolver and build jobs run on GitHub-hosted Ubuntu runners:

1. Check out the requested source branch and resolve `git rev-parse HEAD` to one
   full 40-character commit SHA.
2. Pass that SHA to the reusable build, check out exactly that revision, and
   verify that the resulting `HEAD` equals the requested SHA.
3. Install Temurin JDK 25, enable the Maven dependency cache, and run
   `./mvnw -B verify`.
4. Configure Docker Buildx and authenticate to GHCR with the workflow's
   short-lived `GITHUB_TOKEN`.
5. Build for `linux/arm64`, use the GitHub Actions layer cache, and push
   `ghcr.io/almonium-platform/almonium-be:<commit_sha>`.
6. Capture Buildx's registry digest, attach BuildKit SBOM and max-mode
   provenance attestations to the image index, record the SHA/tag/digest in the
   workflow summary, scan the ARM64 image by digest, and return the digest to
   deployment. Trivy is explicitly configured for ARM64 because GitHub-hosted
   build runners are AMD64 while the published artifact targets ARM64.

`verify` is the quality gate. It includes compilation, test execution, Spring
Boot packaging, and the Spotless check bound to Maven's `validate` phase. The
Maven Wrapper makes the local and CI Maven entry point the same. Integration
tests use Testcontainers, so their successful execution depends on Docker being
available on the runner.

The Dockerfile expects the JAR to exist before the container build. Its first
stage extracts Spring Boot layers from `target/*.jar`; its runtime stage copies
dependencies, loader, snapshot dependencies, and application classes into a
Temurin 25 JRE image. This gives Docker reusable layers without compiling the
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
   host, using the cloud SSH key and a reviewed `CLOUD_KNOWN_HOSTS` entry.
5. Passes only deployment coordinates as extra variables: environment, image
   digest, GHCR actor, and a package-read token.

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

Deploy jobs share an environment-specific concurrency group, so overlapping
runs queue instead of racing on slot state.

| Environment | Blue check | Green check | Public host |
| --- | ---: | ---: | --- |
| staging | `127.0.0.1:9978` | `127.0.0.1:9979` | `staging.api.almonium.com` |
| production | `127.0.0.1:9988` | `127.0.0.1:9989` | `api.almonium.com` |

For the target slot, the role validates the digest, ensures Docker/Compose are
available, renders the Compose file with
`ghcr.io/almonium-platform/almonium-be@sha256:<digest>`, retries that immutable
GHCR pull, recreates the application container, and polls
`/api/v1/actuator/health` every three seconds for up to roughly three minutes.
The endpoint is publicly permitted by Spring Security, but the slot check
itself uses a host-loopback port. The Compose health check uses the `curl`
binary installed in the application runtime image.

If Actuator reports `UP`, Ansible atomically renders one environment router in
Traefik's watched dynamic directory, pointing it at the target container. A
response-header middleware identifies the selected slot. An HTTPS request from
the GitHub runner must receive a valid certificate, report application status
`UP`, and return the expected slot header. If verification fails and an old
slot exists, the rescue path restores its route and fails the deployment. Only
after a successful routed check does Ansible idempotently remove the old
container and advance the color file.

A later rollback is a manual redeployment of the previous known-good image
digest; the route switch itself has an automatic failure rollback.

## Runtime topology

```text
Internet
   |
   v
Traefik :80/:443
  - redirects HTTP to HTTPS
  - obtains/renews certificates with Porkbun DNS-01
  - watches the explicit Almonium backend route
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

Liquibase derives its context from the same `SPRING_PROFILE` value that selects
the runtime Spring profile. The supported matching names are `local`,
`staging`, `prod`, and `test`. Database changes therefore ride with the
application artifact, while
database server provisioning, users, PgBouncer, persistence, and backups are
owned by the infrastructure repository. This makes backward-compatible schema
changes especially important during a two-slot rollout.

## Environment and secret flow

The rendered Compose template is the final configuration junction:

```text
plain infra vars --------------------+
shared encrypted Ansible vault ------+--> Compose environment --> Spring Boot
prod/staging encrypted vault --------+
image digest from backend workflow --+--> immutable Compose image
Ansible route template --------------+--> explicit public routing and TLS
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
  respective application repositories with a concrete image reference.

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
3. Record the built digest from the workflow summary, confirm that digest was
   deployed, and perform behavior-level smoke tests against the staging
   hostname.

### Promote to production

1. Ensure the intended commit is on `main` and has passed the relevant checks.
2. Copy the exact digest of the image already tested in staging.
3. Manually dispatch `Deploy to Production` with that digest; leave the input
   empty only when intentionally building the current `main` commit.
4. Confirm Actuator health and run a user-facing smoke test.

### Roll back or redeploy

1. Identify the last known-good GHCR image digest from its build/deployment
   record.
2. Run `Deploy Only`, or the environment pipeline with `image_digest`
   populated.
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

- pull-request Maven verification without deployment;
- formatting gate, compilation, tests, and JAR packaging;
- exact-commit checkout, ARM64 container build, digest recording, attached
  SBOM/provenance, cache, authentication, and GHCR publication;
- environment config rendering and secret selection;
- serialized image deployment, slot recreation, Actuator polling, explicit
  route switch, routed HTTPS verification, failure rollback, and old-slot
  retirement;
- HTTPS routing/certificate management and selected shared-stack convergence;
- repeatable deployment of an existing artifact.

Manual or external to the repository:

- production approval/dispatch and behavior-level smoke testing;
- rollback decision and previous-digest selection;
- GitHub branch/environment/package policy administration;
- first host bootstrap, DNS/cloud provisioning, vault password custody, and
  disaster recovery;
- database restore testing and application-aware migration rollback;
- monitoring/alerting beyond the deployment-time health request.

## Current risks and improvement order

These are important distinctions between the intended design and the guarantees
currently enforced by code.

### P0: ~~make build success a hard deployment prerequisite~~ Remediated

The deploy job now runs only when image resolution succeeds and the build
either succeeds or is intentionally skipped for an existing image. A failed
verification/build no longer reaches deployment.

### P0: ~~bind image tags to the exact checked-out source~~ Remediated

The resolver now derives a full SHA from the requested branch's checked-out
`HEAD`; the reusable build checks out and verifies that exact SHA, tags the
image with it, records Buildx's pushed digest, and passes the digest to Ansible.
Existing-artifact deployment accepts only a `sha256:` digest. Compose therefore
pulls `repository@digest`, closing both the source/tag mismatch and mutable-tag
promotion gaps.

### P1: ~~serialize deployments per environment~~ Remediated

The reusable deployment job queues runs in an environment-specific concurrency
group and never cancels an in-progress rollout.

### P1: ~~make traffic cutover and health semantics explicit~~ Remediated

The runtime image contains the command used by its Compose health check.
Ansible verifies the target on loopback, atomically switches a single dynamic
Traefik router, and then verifies HTTPS, certificate validity, application
health, and exact slot identity from the runner. A failed routed check restores
the old route when possible.

### P1: ~~fix previous-slot cleanup~~ Remediated

The playbook now removes the previous container with the idempotent
`community.docker.docker_container` module after routed verification succeeds.

### P1: ~~add pull-request CI and stronger release evidence~~ Remediated

A non-deploying pull-request workflow runs the Maven gate. Published images
retain revision labels and digest promotion, are scanned by digest, and now
carry BuildKit SBOM and max-mode provenance attestations.

### P1: ~~harden automation trust boundaries~~ Substantially remediated

All backend and infra workflow actions are pinned to reviewed commit SHAs.
Ansible host-key checking is enabled and workflows consume a managed
`CLOUD_KNOWN_HOSTS`; the direct Traefik SSH workflow consumes a managed
`CLOUD_HOST_FINGERPRINT`. Repository administrators must populate and rotate
those values through a trusted channel. Key/password rotation and further token
lifetime reduction remain operational work.

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
> resolves one exact Git commit, verifies the Spring Boot service with the
> Maven Wrapper, packages a layered ARM64 image, and publishes a commit-tagged
> artifact to GHCR. It records and deploys the pushed digest. The backend then
> invokes a versioned Ansible playbook from a separate infrastructure repo.
> Ansible selects environment-specific vaults, renders Compose, deploys the next
> blue/green slot on an Oracle ARM host, waits for Actuator, atomically switches
> Traefik's route, verifies that exact slot over HTTPS, and only then removes
> the old slot. The
> same digest can be promoted or rolled back without rebuilding, while
> PostgreSQL, RabbitMQ, networks, TLS, and backups have independent infra
> lifecycles.

The follow-up engineering discussion is equally important: this setup is
reproducible and understandable, but single-host recovery, secret rotation,
monitoring, and resource isolation remain meaningful operational concerns.

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
