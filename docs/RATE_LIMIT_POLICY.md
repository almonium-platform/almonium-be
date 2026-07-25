• The current code has the sharpest gap exactly where the audit suggests: /lang/translate, bulk translation, TTS, and /public/discover accept unbounded strings; /auth/session only
  checks nonblank token input. I’m folding input caps, concurrency limits, and provider quotas into the recommendation—request throttling alone would still allow costly abuse.

◦ Searching the web

• Searched the web for site:rfc-editor.org RFC RateLimit HTTP fields 2025 429 Retry-After ...

• Explored
  └ Read TranslationEngine.java, FrequencyService.java, CardService.java, RelationshipService.java, RelationshipActionsFacade.java, LangController.java, DiscoverController.java,
         FirebaseSessionController.java, application.yaml, route.yaml.j2
    Search auth/session|public/discover|/lang/translate|translations/.*/bulk|/audio/|cards/.*/search|subscriptions/portal|books/.*/orders|cards/suggestions in almonium-fe

────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────

• Yes—this codebase would benefit from reusable rate-limiting infrastructure, but I would not implement it purely as an AOP aspect or rely exclusively on Redis.

  My recommended design is layered:

  1. Traefik applies coarse per-IP protection before requests reach Spring.
  2. Spring applies feature-specific per-user and per-IP policies using an annotation plus HandlerInterceptor.
  3. Expensive provider operations enforce weighted quotas, concurrency limits, input bounds, and provider-side spending caps.
  4. Start with bounded in-memory buckets because production currently routes to one active application slot; introduce Redis/Valkey when multiple slots receive traffic
     concurrently or quotas must survive deployments.

  ## What needs protection most

  ### Highest priority

   Endpoint/operation                                 Risk                                                            Primary limits
  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
   POST /auth/session                                 Firebase verification, session creation, provisioning/          Per-IP before token verification; per-user/UID after
                                                      account creation                                                verification
  ─────────────────────────────────────────────────  ──────────────────────────────────────────────────────────────  ───────────────────────────────────────────────────────────────
   /lang/translate/**                                 Potentially several external dictionary/translation             Per-user, per-IP, provider-global, input characters
                                                      providers because fallback providers are tried
  ─────────────────────────────────────────────────  ──────────────────────────────────────────────────────────────  ───────────────────────────────────────────────────────────────
   POST /lang/translations/{lang}/bulk                Paid Google Translation, charged by characters                  Requests plus weighted character quota
  ─────────────────────────────────────────────────  ──────────────────────────────────────────────────────────────  ───────────────────────────────────────────────────────────────
   /lang/words/{text}/audio/{lang}                    Paid Google TTS and generated binary output                     Requests, input bytes, daily character quota, concurrency
  ─────────────────────────────────────────────────  ──────────────────────────────────────────────────────────────  ───────────────────────────────────────────────────────────────
   POST /books/{bookId}/language/{language}/orders    Potentially large asynchronous translation workload             Very low per-user creation rate plus one active order per
                                                                                                                      book/language
  ─────────────────────────────────────────────────  ──────────────────────────────────────────────────────────────  ───────────────────────────────────────────────────────────────
   Stripe portal/checkout creation                    External sessions can be repeatedly created                     Per-user and idempotency/cooldown

  The translation and audio routes currently take unbounded raw strings in path variables or the request body in src/main/java/com/almonium/analyzer/analyzer/controller/
  LangController.java:44. The session DTO only checks that the ID token is nonblank before the Firebase call in src/main/java/com/almonium/auth/firebase/controller/
  FirebaseSessionController.java:33.

  ### Next priority

  - GET /public/discover/freq/...: public and calls the external ngrams API; its text parameter is currently unbounded in src/main/java/com/almonium/analyzer/analyzer/controller/
    open/DiscoverController.java:26.

  - Card and relationship searches: limit request rate, query length, and result count. Card search uses %term% and currently returns the complete match list.
  - Friendship requests and card suggestions: protect against notification/social spam. Rate limiting alone is insufficient; add per-recipient cooldowns.
  - Avatar deletion: calls Firebase Storage.
  - Account deletion: calls Firebase and Stream-related cleanup, although recent-login checks already reduce exposure.
  - Public profile lookups: enumeration/scraping limits and bounded output.
  - FCM registration: low priority, but tokens/device records should have per-user cardinality limits.

  Do not put a tight limiter on Stripe webhooks. Protect those with signature verification, request-body bounds, idempotency, a generous edge ceiling, and monitoring; Stripe
  legitimately retries deliveries.

  ## Redis: yes eventually, but not immediately required

  The deployed Traefik route currently points to exactly one active slot in /media/ok/Space/Code/mine/almonium/almonium-infra/ansible/roles/deploy_almonium_be_slot/templates/
  route.yaml.j2:14. Consequently, a bounded in-memory application limiter is consistent for normal traffic today. Its drawbacks are:

  - counters reset on deployment;
  - it will become inaccurate if blue and green receive traffic simultaneously;
  - long-duration quotas cannot be trusted;
  - careless per-IP bucket creation can exhaust application memory.

  I would therefore define a storage abstraction immediately:

  public interface RateLimitStore {
      RateLimitDecision consume(
              AbusePolicy policy,
              RateLimitSubject subject,
              long cost);
  }

  Implementations:

  - LocalRateLimitStore: Bucket4j plus a bounded Caffeine cache with maximum size and expiration.
  - RedisRateLimitStore: Bucket4j’s Redis/Lettuce integration when distributed enforcement is needed.

  Bucket4j is a good fit: it implements token buckets and officially supports distributed Redis/Lettuce, Redis alternatives, and even PostgreSQL backends. I would use the core
  library directly rather than couple the application to a third-party Spring Boot starter, because your feature policies and composite keys are domain-specific. Bucket4j documents
  its distributed backends here (https://github.com/bucket4j/bucket4j).

  I would not use PostgreSQL for every rate-limit check. The database is exactly what should remain available during abusive traffic, and turning every request into one or more
  database writes increases contention.

  For truly financial limits—daily translation characters, monthly audio usage, subscription entitlements—use a durable usage ledger or existing plan-limit model in PostgreSQL.
  Redis token buckets are traffic controls, not billing records.

  ## Annotation-based infrastructure

  An annotation is useful, but a Spring MVC HandlerInterceptor is a better enforcement mechanism than a controller AOP aspect:

  @Target({ElementType.METHOD, ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface RateLimited {
      AbusePolicy value();
  }

  Example:

  @RateLimited(AbusePolicy.TEXT_TRANSLATION)
  @PostMapping("/translate")
  public TranslationResponse translate(
          @Auth User user,
          @Valid @RequestBody TranslationRequest request) {
      // ...
  }

  Suggested components:

  infra/abuse/
  ├── annotation/RateLimited.java
  ├── model/AbusePolicy.java
  ├── model/RateLimitSubject.java
  ├── model/RateLimitDecision.java
  ├── service/RateLimitService.java
  ├── service/ProviderUsageGuard.java
  ├── web/RateLimitInterceptor.java
  ├── web/ClientAddressResolver.java
  ├── exception/RateLimitExceededException.java
  └── store/
      ├── RateLimitStore.java
      ├── LocalRateLimitStore.java
      └── RedisRateLimitStore.java

  The interceptor should:

  1. Find @RateLimited on the handler method/class.
  2. Resolve the authenticated user from the security context.
  3. Resolve the client IP through a trusted proxy-aware component.
  4. Consume all applicable buckets:
      - per IP;
      - per user;
      - optionally provider-global.

  5. Reject if any bucket fails.
  6. Return 429 with Retry-After and a stable error code.

  Keep numeric limits out of annotations. The annotation should select a named policy; configuration supplies the actual numbers. This makes production tuning possible without
  editing every controller.

  Pure AOP is less suitable because it does not protect traffic before handler invocation, complicates HTTP response headers, and encourages fragile expressions such as “charge the
  length of argument 2.” AOP may still be useful around service/provider operations, but weighted provider consumption is clearer as an explicit call:

  providerUsageGuard.consume(
          ProviderCapability.GOOGLE_TRANSLATION_CHARACTERS,
          user.getId(),
          request.text().codePointCount(0, request.text().length()));

  ## Starting policies

  These are starting values to validate against real traffic, not universal constants:

   Policy                               Suggested initial limit
  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
   General edge protection              300 requests/minute/IP, burst 60
  ───────────────────────────────────  ─────────────────────────────────────────────────────────────────────
   Session creation                     10/minute/IP, burst 3; optionally 50/hour/IP
  ───────────────────────────────────  ─────────────────────────────────────────────────────────────────────
   Public discovery                     30/minute/IP, input ≤ 64 characters
  ───────────────────────────────────  ─────────────────────────────────────────────────────────────────────
   Single translation                   20/minute/user and 60/minute/IP, input ≤ 256 code points
  ───────────────────────────────────  ─────────────────────────────────────────────────────────────────────
   Bulk translation                     3/minute/user, input ≤ 5,000 code points
  ───────────────────────────────────  ─────────────────────────────────────────────────────────────────────
   TTS                                  10/minute/user, product input ≤ 500 UTF-8 bytes
  ───────────────────────────────────  ─────────────────────────────────────────────────────────────────────
   Translation orders                   2/hour/user and one active order per book/language
  ───────────────────────────────────  ─────────────────────────────────────────────────────────────────────
   Stripe portal/checkout               5 per 10 minutes/user
  ───────────────────────────────────  ─────────────────────────────────────────────────────────────────────
   User/card search                     60/minute/user, minimum query length 2, maximum 64, result limit 20
  ───────────────────────────────────  ─────────────────────────────────────────────────────────────────────
   Friendship/card suggestions          20/hour/user plus target-specific cooldown
  ───────────────────────────────────  ─────────────────────────────────────────────────────────────────────
   Avatar external-storage mutations    20/hour/user

  Google recommends keeping synchronous translation requests around 5,000 code points even though some APIs permit larger requests, and translation cost is character-based. Google
  Translation quotas (https://docs.cloud.google.com/translate/quotas) document both facts. Google TTS has a hard 5,000-byte request-content limit, but the product’s “word audio”
  endpoint should use a much smaller business limit. Google TTS quotas (https://docs.cloud.google.com/text-to-speech/quotas) document the provider maximum.

  ## Input and resource bounds

  Rate limiting must be accompanied by:

  - Replace text-bearing GET path variables with POST DTOs where text may contain spaces, Unicode, or substantial content.
  - Add @Size or custom code-point/UTF-8-byte validators to every provider-bound string.
  - Add @Size(max = 16384) or another measured safe cap to idToken.
  - Add a global request-body ceiling at Traefik/server level, with explicit exceptions for endpoints such as Stripe webhooks if necessary.
  - Add pagination or SQL-level limits to searches and list endpoints.
  - Bound generated audio response size.
  - Normalize text before calculating quota cost so equivalent requests are charged consistently.
  - Cache safe deterministic results such as normalized translations, frequency lookups, and TTS by content hash.
  - Add external-client connect/read deadlines.
  - Add provider-specific concurrency bulkheads. A rate bucket allowing ten requests does not prevent all ten from executing simultaneously.
  - Lower Google project quotas to a budget-safe ceiling; application limits should not be the last financial defense.

  OWASP explicitly recommends combining rate controls with total request-size limits, bounded input-driven work, timeouts, and load/concurrency limits. OWASP DoS guidance
  (https://cheatsheetseries.owasp.org/cheatsheets/Denial_of_Service_Cheat_Sheet.html) and REST security guidance
  (https://cheatsheetseries.owasp.org/cheatsheets/REST_Security_Cheat_Sheet.html) cover these controls.

  ## IP address safety

  The application already enables forwarded-header processing in src/main/resources/application.yaml:5. Never simply read the first X-Forwarded-For value: clients can spoof it
  unless Traefik strips incoming forwarding headers and the application only trusts known proxy hops. Spring’s documentation explicitly warns that forwarded headers must be
  sanitized at the trust boundary. Spring ForwardedHeaderFilter documentation
  (https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/filter/ForwardedHeaderFilter.html).

  Traefik’s open-source rate-limit middleware uses a token bucket and supports IP source selection, so it is appropriate for coarse pre-application protection. Traefik RateLimit
  documentation (https://doc.traefik.io/traefik/v3.3/reference/routing-configuration/http/middlewares/ratelimit/).

  ## Failure and observability rules

  - Return 429, a stable error code such as RATE_LIMIT_EXCEEDED, and Retry-After. RFC 6585 recommends explaining the limit and permits Retry-After. RFC 6585
    (https://www.rfc-editor.org/rfc/rfc6585.html).

  - If Redis is unavailable, return 503, not 429.
  - High-cost provider endpoints should normally fail closed when their limiter store is unavailable.
  - Ordinary low-cost endpoints can use a small local emergency limiter or fail open, depending on availability requirements.
  - Record metrics by policy, outcome, and key type.
  - Do not put raw user IDs or IPs into metric labels.
  - Sample logs and use a keyed hash for IP correlation rather than retaining raw IP indefinitely.
  - Alert on rejection spikes, provider cost/character usage, provider 429s, concurrency saturation, and Redis failures.
  - Have Angular honor Retry-After, avoid automatic rapid retries, and show a useful cooldown message.

  ## Suggested rollout

  1. Add input/result bounds first.
  2. Add Traefik per-IP limits for session creation, public discovery, and a generous global ceiling.
  3. Add the annotation, interceptor, local bounded Bucket4j store, 429 contract, metrics, and tests.
  4. Apply policies to translation, TTS, session, orders, Stripe sessions, searches, and social writes.
  5. Add weighted character quotas and provider concurrency limits.
  6. Observe for at least a week and adjust limits from actual percentiles.
  7. Add Redis/Valkey when enabling concurrent slots, or sooner if deployment-persistent rate state is required.
  8. Add durable daily/monthly usage accounting for paid-plan and cost enforcement.

  This gives you easy future usage—usually one annotation per endpoint—without hiding the important cost-based controls behind overly magical AOP.
