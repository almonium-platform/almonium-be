# Access matrix: who can do what

**Last checked against the code:** 2026-11-28

This is the one place that answers "may a signed-out visitor, a Free member, or
a Premium member do X?" Every row says what the backend **enforces today**.
Where a decision or the pricing page says something different, the row says
so; the full list of disagreements is under [Open gaps](#open-gaps).

The reasoning behind the split (what should be free, what should be paid, and
why) stays in [ALMONIUM_FREEMIUM.md](ALMONIUM_FREEMIUM.md). Prices and checkout
live in [PADDLE_BILLING.md](PADDLE_BILLING.md). When you change a gate or a
limit, update this file in the same commit.

## The four kinds of visitor

| Who | How the backend knows |
|---|---|
| **Signed out** | No session cookie and no Firebase bearer token. Only `/public/**` answers (`WebSecurityConfig.PUBLIC_URL_PATTERNS`); every other route returns 401. |
| **Free** | Signed in, entitlement `FREE`. This is the default when there is no active subscription and no grant. |
| **Premium** | Entitlement `PREMIUM`: a paid monthly or yearly plan, a founding-member place (20 places, $80/year), or an operator grant. |
| **Unlimited** | Entitlement `UNLIMITED`: the `INSIDER` plan or an operator grant, for internal and QA accounts. The code counts it as Premium everywhere (`isPremium` means "not FREE"), and it has fewer limit rows. |

`EffectiveAccessService` is the only correct way to ask about a person's
entitlement: an operator grant overrides the plan, including downwards.

Legend: ✅ allowed · ❌ refused by the backend · 🔢 allowed up to a limit
(numbers in the next section) · 👁 view only.

## Matrix

### Books and reading

| Action | Signed out | Free | Premium | Notes |
|---|---|---|---|---|
| Browse the shelf, catalogue search, book pages, covers | ✅ | ✅ | ✅ | `/public/books`, `/public/catalog`, `/public/covers` |
| Read a book's full text, chapters, chapter vocabulary | ✅ | ✅ | ✅ | No plan check. Reading is subject only to the reader's country (from IP, answers 451); see [COPYRIGHT_AND_CATALOGUE.md](COPYRIGHT_AND_CATALOGUE.md) |
| Parallel text and companion editions | ✅ | ✅ | ✅ | |
| Level-adapted editions (B1/B2/C1) | ✅ | ✅ | ✅ | **Gap:** FREEMIUM says Free previews one chapter, and the pricing page sells "every book at your level" as Premium. Nothing gates it. |
| Open a catalogue work that is not on the shelf yet (`POST /books/live`) | ❌ | 🔢 | 🔢 | 5 per reader per day on every plan (`app.books.live.daily-limit`) |
| Reading progress, certificates of completion | ❌ | ✅ | ✅ | Anyone can view a certificate at `/public/certificates/...` |
| Request a book translation | ❌ | 🔢 | 🔢 | Premium requests are counted first in the ops queue |
| Suggest a book for the library | ❌ | ✅ | ✅ | Premium suggestions are counted first in the ops queue |
| Import a private book | ❌ | ❌ | 🔢 | Free's limit is 0. **Gap:** the pricing page says "2 a month"; the backend allows 10 on the shelf at a time, with no monthly reset. |
| Report a rights problem with a book | ✅ | ✅ | ✅ | A signed-out visitor must leave an email; 5 per address per hour |

### Dictionary (Discover / lexicon)

| Action | Signed out | Free | Premium | Notes |
|---|---|---|---|---|
| Look up a word, word frequency | ✅ | ✅ | ✅ | `/public/discover`. A signed-in member's active and fluent languages steer the result when the language asked for is missing. A miss falls through to an external provider for everyone, with no limit. |
| Dictionary entries, suggestions, pronunciation clips | ✅ | ✅ | ✅ | `/public/lexicon` |
| Translate, word audio (`/lang/...`) | ❌ | ✅ | ✅ | No plan check. **Gap:** the pricing page sells "audio in every variety" as Premium. |
| Report a dictionary entry, compare entries | ❌ | ✅ | ✅ | |

### Learning

| Action | Signed out | Free | Premium | Notes |
|---|---|---|---|---|
| Save words and cards | ❌ | ✅ | ✅ | **Gap:** Free's limit of 100 saved words is decided and shown on the membership page, but the backend does not enforce it (it has no limit key for it; see `almonium-fe/src/app/models/plan-offer.ts`). |
| Review sessions, learning stats | ❌ | ✅ | ✅ | |
| Target languages on the account | ❌ | 🔢 | 🔢 | How many may exist at all, the same on every plan |
| Target languages active at once | ❌ | 🔢 | 🔢 | When a member downgrades, extra languages are switched off, never deleted |
| Fluent (bridge) languages | ❌ | 🔢 | ✅ | |
| Chat with Almo | ❌ | ❌ | 🔢 | Refused for Free (`AlmoChatService`). Premium has a silent ceiling of 60 replies per rolling day. |
| Play Higher or Lower | ✅ | ✅ | ✅ | Words from `/public/play/higher-lower/{language}/words`, no limit. A signed-in member's best run per language is kept (`/play/higher-lower/bests`); a guest's run is not. Nothing touches the deck or review. |

### Sharing and social

| Action | Signed out | Free | Premium | Notes |
|---|---|---|---|---|
| View a shared card or deck | 👁 | ✅ | ✅ | `/public/shares` |
| Add words from a shared card or deck | ❌ | ✅ | ✅ | Free is held to its one active language |
| Share a card or deck | ❌ | ✅ | ✅ | Every card and deck has a public link (`publicId`, `shareId`) on every plan. **Gap:** the pricing page sells "share word packs with friends" as Premium. FREEMIUM says Free may receive packs but not create them. Nothing gates it. |
| View public profiles | 👁 | ✅ | ✅ | `/public/profiles` |
| Friends, chat with friends, notifications | ❌ | ✅ | ✅ | Premium only shows as a badge (avatar, chat, share card) |

### Account and billing

| Action | Signed out | Free | Premium | Notes |
|---|---|---|---|---|
| Sign up, verify email, reset password | ✅ | — | — | `/public/auth`, rate-limited per address |
| See plans, prices, founding places left | ✅ | ✅ | ✅ | `/public/plans`, `/public/billing`, `/public/founding-members` |
| Subscribe, manage or cancel a subscription | ❌ | ✅ | ✅ | Paddle |
| Send feedback | ✅ | ✅ | ✅ | Rate-limited |
| Operations console (`/ops/**`) | ❌ | ❌ | ❌ | Admin role only, whatever the plan |

## Limits

These are the `plan_limit` rows, read from a fully migrated database on
2026-11-28. A missing row means no limit.

| `PlanFeature` | Free | Premium | Unlimited | Enforced in |
|---|---|---|---|---|
| `MAX_TARGET_LANGS` (on the account) | 10 | 10 | 10 | `LearnerService` |
| `MAX_ACTIVE_LANGS` (at once) | 1 | 3 | no limit | `ActiveLanguageService` |
| `MAX_FLUENT_LANGS` | 1 | no limit | no limit | `UserService` |
| `MAX_BOOK_IMPORTS_ON_SHELF` | 0 | 10 | 10 | `UserBookImportService` |
| `MAX_TRANSLATION_REQUESTS_PER_MONTH` | 1 | 3 | 3 | `TranslationOrderService` |

Limits that do not depend on the plan:

| Limit | Value | Where |
|---|---|---|
| Almo replies | 60 per rolling day (Premium and Unlimited only) | `app.almo.daily-message-ceiling` |
| Live books | 5 per reader per day | `app.books.live.daily-limit` |
| Rights reports from signed-out visitors | 5 per email address per hour | `learning.rights` |

## Open gaps

Each of these needs either a backend gate or a change of copy. Keep this list
current: delete an item when it is resolved.

1. **Saved-word cap.** Free's 100 is decided and shown on the membership page,
   but nothing enforces it. FREEMIUM calls it "the load-bearing limit".
2. **Level-adapted editions.** Sold as Premium and planned as "preview one
   chapter" for Free, but open to everyone, including signed-out visitors.
3. **Audio.** Sold as Premium ("audio in every variety", "narrated
   audiobooks"), but word audio is open to every signed-in member and
   pronunciation clips are public.
4. **Creating share links.** Sold as Premium, but open to Free.
5. **Book import allowance.** The pricing page says "2 a month"; the backend
   allows 10 on the shelf at a time.
6. **Active languages for Premium.** The backend allows 3. The pricing page
   follows the backend, but the shared card and shared deck pages say "Member
   covers five" (`shared-card.component.html`, `shared-deck.component.html` in
   `almonium-fe`).
7. **"Unlimited new entries in all 328 languages", "10% off printed
   editions".** Nothing in the backend distinguishes either by plan.
8. **AI dessert stories (Free 3 a week).** Listed in FREEMIUM, not built.
9. **Cross-device sync as a Premium feature.** Listed in FREEMIUM. Every
   account syncs, because all state lives on the server.
10. **Discover provider calls.** A lookup that misses the dictionary calls an
    external provider for anyone, signed out or not, with no limit.
    `RATE_LIMIT_POLICY.md` is a proposal; most of it is not built.
11. **No single limits table for yes/no features.** Numeric limits live in
    `plan_limit`, but on/off gates (Almo today, anything added for the gaps
    above) are `isPremium` checks in each service. FREEMIUM's Enforcement
    section asks for one place.

## Where the truth lives

- Signed-out access: `src/main/java/com/almonium/config/security/WebSecurityConfig.java`
  (`/public/**` is open, everything else needs a signed-in user).
- Entitlement: `subscription/service/EffectiveAccessService.java`.
- Numeric limits: the `plan_limit` table (Liquibase patches under
  `db/changelog/changes/patches`), read through `PlanValidationService`.
- On/off Premium gates: search for `effectiveAccessService.isPremium`.
- Copy shown to users: `almonium-fe/src/app/models/plan-offer.ts`
  (`paidTierFeatures`) and the matching mobile screens.
