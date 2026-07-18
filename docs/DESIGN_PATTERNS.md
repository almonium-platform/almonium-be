# Design Patterns in Almonium

This is an interview-memory aid and a refactoring guide, not a demand that every
class fit a Gang of Four label. Patterns are most valuable here when they name
a recurring pressure and make future changes safer.

## Patterns already present

### Template Method

The email composer hierarchy is the clearest textbook example. A base composer
owns the common algorithm while subclasses supply message-specific pieces.

Interview framing: "The workflow is stable, but selected steps vary. Template
Method keeps ordering and shared invariants in one place." Its tradeoff is
inheritance coupling; if the steps begin varying independently, compose
collaborators instead.

### Strategy

Token generator implementations encapsulate interchangeable token-generation
behavior behind a common contract. OAuth provider information handling also
selects provider-specific behavior at runtime.

Interview framing: "Strategy replaces a growing provider/type conditional with
an interface whose implementations can be tested and selected independently."

### Factory

`PrincipalFactory` and `OAuth2UserInfoFactory` centralize construction/selection
when the concrete result depends on authentication input.

The old `UserFactory` was correctly renamed to `UserRegistrationService`. It
does not merely construct an object; it orchestrates validation, persistence,
defaults, and domain side effects. Naming orchestration as a factory hides its
transactional/application-service responsibility.

### Adapter

External dictionary/translation response types are converted to internal DTOs,
and MapStruct mappers adapt persistence/domain shapes to API shapes. The n-gram
integration is another adapter-like boundary.

The important property is not the suffix `Adapter`; it is that provider schema,
transport errors, and terminology stop at the boundary.

### Facade

`RelationshipActionsFacade` presents a small use-case-oriented surface over
several relationship operations. Authentication services similarly provide a
cohesive entry point over repositories, token services, events, and providers.

### Observer / publish-subscribe and transactional outbox

Spring application events decouple producers from listeners. Spring Modulith's
persisted event publication plus RabbitMQ externalization gives the project an
outbox-style delivery mechanism: commit domain state and publication intent in
one transaction, then deliver asynchronously.

This is event-driven choreography, not automatically a Saga. A Saga coordinates
a long-running business transaction across participants and defines
compensating actions. Do not claim Saga unless those semantics exist.

### Builder, Repository, Mapper, Proxy, and Chain of Responsibility

- Builders appear in DTO/entity construction where many optional values would
  make constructors unreadable.
- Spring Data repositories isolate persistence access.
- MapStruct mappers isolate structural conversion.
- Spring uses proxies for transactions, security, async work, and AOP logging.
- Spring Security's filter chain is a framework-level Chain of Responsibility:
  each filter may handle, enrich, reject, or pass the request onward.

These are valid practical examples even though some are supplied by the
framework rather than hand-written.

## Registry: more than "just a map"

A registry is often implemented with a map, but the pattern is the ownership
and lookup policy around that data structure. A useful registry:

- discovers implementations through dependency injection;
- indexes them by a stable key or declared capability;
- rejects duplicate keys at startup;
- owns missing-provider behavior;
- exposes lookup without leaking construction/lifecycle details.

Registry and Strategy commonly work together:

```text
caller -> registry.select("YANDEX") -> TranslationStrategy
                                           |
                                           -> translate(request)
```

Strategy answers "how can these behaviors be interchanged?" Registry answers
"which implementations exist, and how do I find the requested one?" A plain
local map of unrelated data is not meaningfully the Registry pattern.

Registry is an architectural/enterprise pattern rather than one of the original
GoF 23. That does not make it less useful or less discussable in an interview.

## Finite-state machine versus the State pattern

These concepts are related but are not synonyms.

A finite-state machine (FSM) is a behavioral model:

```text
(current state, event, guard) -> next state + effects
```

It requires a finite set of states/events and explicit valid transitions. It can
be implemented with a switch, a transition table, functions, or state objects.

The GoF State pattern is one object-oriented implementation technique. A
context delegates state-dependent operations to an object representing its
current state. State objects may cause the context to transition:

```java
interface RelationshipState {
    void accept(RelationshipContext context, Actor actor);
    void block(RelationshipContext context, Actor actor);
}

final class PendingState implements RelationshipState { /* ... */ }
final class FriendsState implements RelationshipState { /* ... */ }
final class BlockedState implements RelationshipState { /* ... */ }
```

This removes large conditionals when each state has substantial, distinct
behavior. It also creates many classes and requires mapping a persisted status
enum to a runtime state object. Relationship actions additionally depend on
whether the actor is requester/requestee/blocker, so class-per-status State can
be more ceremony than clarity.

For Almonium, a small explicit/table-driven FSM plus an orchestration service is
the better first implementation. Keep transitions and guards pure and
testable; let the service perform persistence, notification, and chat effects.
Adopt full State objects only if behavior per state continues to grow.

FSMs are a strong fit when valid actions depend on lifecycle state: orders,
payments, subscriptions, moderation, onboarding, protocols, documents, and
friendships. They are less useful when transitions are unconstrained CRUD or
when there are effectively infinite states.

### Relationship example

The relationship domain is already an implicit FSM. Its states include
`PENDING`, `FRIENDS`, one-sided block variants, and mutual blocking. Events
include request, accept, reject, cancel, unfriend, block, and unblock. Guards
include actor role and current blocker.

A transition matrix makes omissions visible:

| Current state | Event / actor | Result |
| --- | --- | --- |
| `PENDING` | accept / recipient | `FRIENDS` |
| `PENDING` | cancel / requester | relationship removed or terminal cancellation |
| `PENDING` | reject / recipient | relationship removed or terminal rejection |
| `FRIENDS` | unfriend / either | relationship removed or `UNFRIENDED` |
| unblocked state | block / either | appropriate one-sided blocked state |
| one-sided block | block / other user | mutual block |
| mutual block | unblock / either | other user's one-sided block remains |
| one-sided block | unblock / blocker | relationship removed/unfriended |

The persisted model and README must agree on whether cancellation/rejection/
unfriending delete the row or retain terminal states. Pick one lifecycle model
deliberately and test the full state/event/actor matrix.

## Good next pattern refactors

### Translation: Strategy + Adapter + Registry + fallback chain

Provider-specific clients should adapt external responses to one internal
translation result. Strategies expose provider behavior; a registry discovers
them by provider key; an ordered language-pair configuration selects candidates;
and a small fallback chain tries the next eligible provider on a defined
recoverable failure.

Do not catch every exception and silently fall back. Authentication errors,
invalid configuration, and malformed internal requests should fail loudly;
timeouts, quota exhaustion, or provider unavailability may be eligible for a
controlled fallback.

### Stripe webhooks: Command handlers + registry

The webhook entry point should verify signatures and idempotency, then dispatch
each event to a small `StripeEventHandler`. Each handler represents a command
such as subscription updated or invoice paid. A registry maps event type to
handler and rejects duplicates. This shortens the central switch and gives each
event an isolated unit-test seam.

### HTTP clients: composition over inheritance

Provider clients currently inherit common HTTP mechanics even where they do not
use them. An injected HTTP executor/configuration component makes timeouts,
serialization, headers, metrics, and error mapping reusable without asserting
an "is-a" relationship. Provider clients then remain adapters with only their
own API knowledge.

Composition is not a GoF pattern by itself; it is a design principle used by
Strategy, Decorator, Bridge, and many other patterns.

### Authorization: Policy / Specification

Object-level permissions deserve a named policy, e.g. "may this principal edit
this card?" A policy service is easy to audit and test. Specifications are
useful if rules must also compose into database queries. This is security work,
not merely an interview refactor, and should be handled in its own reviewed
session.

## Patterns not worth forcing here

Avoid adding Singleton, Abstract Factory, Visitor, CQRS, Event Sourcing, a
generic base service, or a hand-built Mediator without a concrete pressure.
Spring already manages component lifecycles; CRUD does not become cleaner when
wrapped in generic abstractions; and event sourcing/CQRS would multiply the
operational model of a product that is currently well served by a relational
modular monolith.

## Interview checklist

For any pattern in this repository, explain:

1. The concrete problem and change pressure.
2. The simpler design that existed before it.
3. The roles in the pattern and the actual project classes.
4. The benefit demonstrated by a test or easier extension.
5. The cost: indirection, extra types, runtime selection, or inheritance.
6. The condition under which you would remove or replace it.

That tradeoff discussion is stronger than listing pattern names. A good summary
for this codebase is: use patterns to make provider selection, lifecycle rules,
and event dispatch explicit; keep ordinary domain code ordinary.
