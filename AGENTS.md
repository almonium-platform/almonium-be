# Almonium Backend Agent Guide

This repository is a Java/Kotlin Spring Boot modular monolith for the Almonium
language-learning product. Read `docs/PROJECT_AUDIT.md` for a system map and
known risks, and `docs/DESIGN_PATTERNS.md` for the project's pattern catalogue.

## Working agreement

- Preserve pre-existing worktree changes. Never stage or rewrite changes that
  belong to the user unless the task explicitly includes them.
- When commits are requested, make separate logical commits. Stage every file
  deliberately, including new files, with explicit paths; do not use
  `git add .`. Review `git diff --cached` before each commit.
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

The repository was reviewed on 2026-07-18 with these known baseline issues:

- `mvnw` has line-ending problems on Linux.
- Production compilation works with the system Maven installation.
- The `List`/`Set` test-fixture mismatches found during the audit were repaired;
  test compilation is expected to pass.
- Spotless is configured to apply changes during the build, so avoid allowing
  it to rewrite unrelated user files during focused work.

For a focused production compile, use:

```bash
mvn clean compile -DskipTests -Dspotless.apply.skip=true
```

Run the narrowest relevant test set during refactors. Once the test baseline
and wrapper are repaired, the canonical check should become:

```bash
./mvnw verify
```

Before reporting completion, include the exact checks run and any failures that
are demonstrably pre-existing.
