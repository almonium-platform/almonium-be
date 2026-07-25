# Almonium Backend Agent Guide

This repository is a Java/Kotlin Spring Boot modular monolith for the Almonium
language-learning product. Read `docs/PROJECT_AUDIT.md` for a system map and
known risks, `docs/AUTH_ARCHITECTURE_AUDIT.md` before authentication work, and
`docs/DESIGN_PATTERNS.md` for the project's pattern catalogue.

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
- Keep Java and Kotlin on the same JVM target. Kotlin currently lives under
  `src/main/java`; preserve that layout until it is migrated deliberately.
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
