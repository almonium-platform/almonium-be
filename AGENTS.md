# Almonium Backend Agent Guide

This repository is a Java/Kotlin Spring Boot modular monolith for the Almonium
language-learning product. Read `docs/PROJECT_AUDIT.md` for a system map and
known risks, `docs/AUTH_ARCHITECTURE_AUDIT.md` before authentication work, and
`docs/DESIGN_PATTERNS.md` for the project's pattern catalogue.

## Repository ecosystem and operations

This checkout is one part of a coordinated product workspace. Neighboring
repositories are available at:

- `../almonium-fe`: the Angular client. Treat API DTO, route, error-contract,
  cookie/CSRF/CORS, Firebase-authentication, and user-flow changes as
  cross-client work: inspect and update the frontend when the requested change
  affects it, and verify the flow end to end.
- `../almonium-infra`: the source of truth for deployed topology, Ansible,
  Compose templates, Traefik, PostgreSQL/PgBouncer, RabbitMQ, encrypted runtime
  configuration, and deployment playbooks. Read its `AGENTS.md` before making
  changes there and commit coordinated changes separately in each repository.
- `../almonium-mobile`: the Expo SDK 54 React Native client for iOS and
  Android. It consumes this API with Firebase ID-token bearer authentication,
  while the Angular browser client uses the Secure, HttpOnly session-cookie
  flow. Treat API DTO, error-contract, authorization, and user-flow changes as
  cross-client work, and preserve the distinct browser-cookie and native-bearer
  security boundaries.

Read `docs/LOCAL_DEVELOPMENT.md` before changing environment configuration,
database/broker connectivity, or local startup behavior. Read
`docs/CI_CD_OVERVIEW.md` before changing Docker, GitHub Actions, deployment, or
runtime infrastructure behavior. The workflow/Ansible files remain the
executable source of truth when documentation differs.

- Routine local development uses the `local` Spring profile with PostgreSQL
  and RabbitMQ from `docker-compose.local.yaml`. Local startup runs Liquibase
  only against the disposable local database. Staging migrations run through
  the reviewed deployment artifact; do not point a local process at staging or
  production without an explicit incident or debugging decision.
- CI builds a Java 21 `linux/arm64` image tagged by Git SHA, then invokes the
  infra repository's Ansible deployment. Pushes to `develop` deploy staging;
  production deployment from `main` is a manual operator action. Do not treat
  SSH access as permission to bypass this path or perform an ad-hoc production
  deployment.
- Deployment uses two application slots on one Oracle Cloud ARM host. Keep
  Liquibase changes backward-compatible across old and new application
  versions; use additive/expand-contract migrations rather than assuming an
  immediate, single-container schema cutover.
- Never copy decrypted Ansible vault material, production credentials, or
  GitHub Actions secrets into this repository, logs, commits, or tickets.

## Working agreement

- Preserve pre-existing worktree changes. Never stage or rewrite changes that
  belong to the user unless the task explicitly includes them.
- After completing a requested implementation and relevant verification, stage
  only the files changed for that task and create focused logical commits.
  Stage every file deliberately, including new files, with explicit paths; do
  not use `git add .`. Review `git diff --cached` before each commit.
- Leave changes uncommitted only when the user explicitly asks for that, or
  when the work is blocked or awaiting a material user decision. Do not amend
  an existing commit unless explicitly asked; report every new commit hash in
  the handoff.
- Keep refactors separate from behavior changes and security fixes. Add or
  adjust tests for every behavior change.
- Prefer small, domain-named components and composition. Do not introduce a
  design pattern solely to make the code look patterned.
- Keep controllers thin, transactions in application services, persistence in
  repositories, external API details behind adapters, and mapping in mappers.
- Treat authorization as an invariant: loading an entity by ID is not enough;
  verify that the authenticated user owns or may access it.
- Update Liquibase rather than editing an already-applied database change set.
- Treat changes to `.env` variables as cross-repository changes: propagate the
  corresponding configuration updates to the `almonium-infra` repository and
  keep the application and infrastructure definitions in sync.
- For a configuration-key change, reconcile the application binding and
  validation, `.env.template`, the deployed Compose/Ansible variable mapping,
  the applicable vault schema and encrypted values, and any frontend contract
  that consumes the setting. Keep secret values in the appropriate infra vault;
  do not add them to backend templates.
- Keep Java and Kotlin on the same JVM target. Kotlin currently lives under
  `src/main/java`; preserve that layout until it is migrated deliberately.
- Keep Spring profile and Liquibase context names aligned: `local`, `staging`,
  `prod`, and `test`. Both are selected from `SPRING_PROFILE`; do not duplicate
  that mapping across profile files.
- Do not add runtime aliases or fallback behavior to accommodate stale personal
  IDE or shell configuration unless the user explicitly requests compatibility.
  State the required local configuration change instead.
- Update the relevant document when an architectural convention or known
  baseline changes.

## Verification

The repository was reviewed on 2026-07-18 with this build baseline:

- The Maven wrapper is the canonical Maven entry point on every platform.
- Java and Kotlin compile for JVM 21.
- Test compilation passes. Integration tests use Testcontainers and therefore
  require access to a working Docker daemon.
- Spotless checks formatting during the build; run `spotless:apply` explicitly
  when formatting changes are intended.

For a focused production compile, use:

```bash
./mvnw clean compile -DskipTests
```

Run the narrowest relevant test set during refactors. The canonical check is:

```bash
./mvnw verify
```

Before reporting completion, include the exact checks run and any failures that
are demonstrably pre-existing.
