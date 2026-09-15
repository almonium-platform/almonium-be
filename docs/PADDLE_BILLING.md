# Paddle Billing setup

Almonium uses Paddle Billing for recurring Premium subscriptions. The backend
creates a Paddle customer and transaction, the browser opens the returned
transaction link through Paddle.js, and signed webhooks remain the source of
truth for entitlement changes.

## Catalog and pricing

Create one standard product named `Almonium Premium` in both Paddle sandbox and
live, and three recurring USD prices against it:

| Application key | Base amount | Billing cycle | Visibility |
| --- | ---: | --- | --- |
| `premium_monthly` | $12 | monthly | public |
| `premium_annual` | $120 | annual | public |
| `founder_annual` | $80 | annual | application-controlled |

There is no founder monthly price. The founder tier is annual-only; the
reasoning is below, and the retirement of the monthly founder price is listed
under "Decided, not yet implemented".

### Regional bands

The base amount is the United States price. It is not a global price. Each
public price carries country overrides — `unit_price_overrides` on the one
price object for that billing cycle, not a separate price per region — so the
`pri_` IDs and the backend configuration stay exactly as they are:

| Band | Monthly | Annual |
| --- | ---: | ---: |
| United States and anywhere unlisted | $12 | $120 |
| European Union and United Kingdom | $14 | $140 |
| Ukraine, CEE, LATAM, SEA | $6–7 | $60–70 |

The EU band is not opportunistic. Prices are tax-inclusive, so $14 in a 19% VAT
country nets $11.76 — the US $12, after the VAT the higher sticker absorbs. The
discount band is priced to what those markets bear rather than to what serving
them costs, which is the point: the marginal cost of a reader there is the same
as anywhere, and the alternative to $6 is not $12, it is nothing.

Note that a Ukrainian living in Germany bills in the EU band. The wedge audience
splits across both, and no configuration can fix that — Paddle bands on the
buyer's location, not their nationality.

The founder price is a single global $80 with no country overrides. It is a
customer-development budget rather than a revenue line, and banding it would add
per-region complexity to a twenty-slot promise that will never be material.

### Tax

Set the account tax setting to **prices include tax**, and leave every price on
**account default** so there is one lever rather than four. Displayed prices are
final: the number on the page is the number charged, in every country.

Do not use "automatic based on location". It adds tax on top of the price in the
United States, which would make the displayed amount wrong in the market the
base price is built for, and it would double-count the VAT that the EU band
already absorbs.

What that costs, per subscription, at Paddle's standard 5% + $0.50:

| Buyer | Charged | Tax | Paddle fee | Net |
| --- | ---: | ---: | ---: | ---: |
| US monthly, no nexus | $12.00 | $0.00 | $1.10 | $10.90 |
| EU monthly, 19% VAT | $14.00 | $2.24 | $1.20 | $10.56 |
| Founder annual, US | $80.00 | $0.00 | $4.50 | $75.50 |
| Founder annual, 19% VAT | $80.00 | $12.77 | $4.50 | $62.73 |

### The founder tier is annual-only

At $8 a month the fixed $0.50 is 6.25% of the charge, so the deepest discount is
also where the fee structure hurts most: a German founder nets $5.82 a month.
The fix is the shape, not the price. One $80 annual charge nets $62.73 in the
same country, today, in a single transaction.

Twelve monthly charges would net $69.84 — but only from someone who stays a full
year, and most will not. The comparison is not two payment schedules for one
person. Prepaying a year selects a more committed cohort, removes eleven monthly
cancel decisions, saves eleven fixed fees, and puts the cash in the account now.

Twenty founders is roughly $1,250 up front. It was never going to matter
financially. It buys twenty people who will answer your emails, which is what
the tier is actually for.

### Lead with annual

Annual is the default on the pricing page. Monthly is the option someone has to
look for. This follows from the same arithmetic — eleven fewer fixed fees and
materially lower churn, because an annual subscriber never faces a monthly
cancel decision — and it applies to the public tier, not only to founders.

### The application's local copy of prices

The application never reads amounts from Paddle. It stores its own copy in the
`plan` table — `price` for the public amount, `founder_price` for the discounted
one — and serves both to the client through `/public/plans`. Only the `pri_` IDs
are configured, so an amount that disagrees with the Paddle catalog is not
detected anywhere: the page advertises the local number and Paddle charges its
own.

Those columns hold the **US base amounts**. They cannot express regional bands,
and they are not a substitute for asking Paddle what this visitor pays. Treat
them as the pre-load fallback and the record of the base price. Verify all
amounts by hand when creating a catalog, and update the `plan` rows through
Liquibase whenever a base price changes.

### Decided, not yet implemented

Both items below are settled product decisions that the code does not yet
reflect. Until they land, the deployed behavior is the old behavior.

1. **Localized price display.** The pricing page shows one number to every
   visitor, so a German sees $12 and is charged $14 at the Paddle overlay. The
   fix is `Paddle.PricePreview()` in the client, which returns the visitor's
   localized formatted price for a set of price IDs and needs only the
   client-side token the backend already serves — no API key scope, no backend
   call. Regional bands should not be switched on in the live catalog before
   this lands, or every EU visitor meets a higher number at checkout than the
   page promised.
2. **Founder monthly retirement.** `PADDLE_PRICE_FOUNDER_MONTHLY` is still
   `@NotBlank` in `PaddleProperties`, `PaddlePriceCatalog.priceIdFor` still
   resolves a founder monthly price, and the `plan` row for PREMIUM/MONTHLY
   still carries `founder_price` 8.00. Retiring the tier means dropping the
   configuration key across this repository and the infra vaults, rejecting a
   founder reservation on a monthly checkout, and nulling that column.

Paddle price IDs start with `pri_`. Paddle Billing does not have Stripe's
`lookup_key` field. The names in the table above are Almonium configuration
keys; putting the same value in each Paddle price's `custom_data.lookup_key` is
useful for dashboard readability but is not required by the application.

Sandbox and live catalogs are separate. Copy each environment's `pri_` IDs into
that environment's backend configuration. The founder price is never returned as
a public plan record. The backend selects it only after it durably reserves a
founder slot, and rejects a stale founder checkout request instead of silently
charging the regular price.

## Credentials and environment variables

Create a server-side API key with customer, transaction, customer-portal, and
subscription read/write permissions. Create a separate client-side token for
Paddle.js. The client-side token is designed to be public; it is served by the
backend so sandbox and live builds use the deployed backend's matching token.

Configure:

```text
PADDLE_ENVIRONMENT=SANDBOX|LIVE
PADDLE_CLIENT_TOKEN=...
PADDLE_API_KEY=...
PADDLE_WEBHOOK_SECRET=...
PADDLE_PRICE_PREMIUM_MONTHLY=pri_...
PADDLE_PRICE_PREMIUM_ANNUAL=pri_...
PADDLE_PRICE_FOUNDER_MONTHLY=pri_...
PADDLE_PRICE_FOUNDER_ANNUAL=pri_...
```

Local development should use only sandbox values. Staging is `SANDBOX`; prod
is `LIVE`. Deployment values live in the corresponding encrypted infra vault.

### API key scopes

The backend calls six endpoints. Grant exactly the scopes they need and nothing
else:

| Scope | Read | Write | Used by |
| --- | :--: | :--: | --- |
| Customers | yes | yes | `POST /customers`, and `GET /customers?email=` on the already-exists reconcile path |
| Transactions | yes | yes | `POST /transactions` for checkout; `GET /transactions?subscription_id=` to find the payment a guarantee refund targets |
| Customer portal sessions | — | yes | `POST /customers/{id}/portal-sessions` |
| Subscriptions | yes | yes | `GET /subscriptions/{id}` for reconciliation, `POST /subscriptions/{id}/cancel`, `PATCH /subscriptions/{id}` and its `/preview` for cadence changes |
| Adjustments | — | yes | `POST /adjustments`, to refund an annual payment when a member switches to monthly inside the guarantee |

Products, prices, discounts, reports, and notification settings stay off.
Notification destinations are configured in the dashboard, not through the API.
Add prices read only when the amount-verification gap above is closed.

Paddle grants read with write on a resource, so the read column above describes
what the code depends on rather than a box to tick separately.

Adjustments write is the one that has to be granted deliberately, and its
failure is invisible until a member takes the guarantee path: the key is
accepted, checkout keeps working, and the refund comes back `forbidden` after
they have already been switched to monthly and charged. Grant it in every
environment, in the same change as the deploy that carries the flow.

### API key expiry

The prod key is created without an expiry and marked rotatable, and is rotated
on a calendar reminder or immediately on suspicion.

A short expiry does not improve the control that matters here. The key exists
only in the encrypted prod vault and can be revoked from the dashboard in one
click, which is total and immediate. What a quarterly expiry does add is a
scheduled outage: rotation means editing the vault, redeploying both slots, and
re-verifying checkout, and the failure mode when it is missed is that new
subscriptions, cancellations, and portal sessions start failing while webhooks
keep flowing — so existing subscribers look healthy and the first signal is a
user who could not pay. Nothing alerts on `PaddleIntegrationException` today.

Marking the key rotatable costs nothing and makes a deliberate rotation an
overlap rather than a hard cutover against a redeploy.

## Checkout and webhooks

Set the Paddle default payment link to a page that loads Paddle.js:

- sandbox/staging: `https://staging.almonium.com/payment/checkout`
- live: `https://almonium.com/payment/checkout`
- local sandbox when needed: `http://localhost:9999/payment/checkout`

The live domains must be approved in Paddle before checkout works. The backend
uses Paddle's account default payment link when creating a transaction, so set
the correct default link separately in each Paddle environment.

A sandbox account has one default link and it is shared, so a developer running
locally is sent to staging by it. `PADDLE_CHECKOUT_URL` overrides the link for
one transaction and leaves the account default alone; blank, which is the
default, keeps the old behaviour exactly.

It is not a free-form URL. Paddle enforces its approved-domains list against a
URL stated on a transaction, but **not** against the account default link — so
the default may point somewhere the override cannot, and setting the override to
that same address is rejected with
`transaction_checkout_url_domain_is_not_approved`. Add the domain under Checkout
settings, approved domains, before setting the variable.

Create a notification destination using API version 1:

```text
https://<api-host>/api/v1/public/webhooks/paddle
```

Enable exactly these events on the notification destination:

| Dashboard event | Required behavior |
| --- | --- |
| `subscription.created` | Records the Paddle subscription, grants Premium, confirms a founding-member reservation, and sends the welcome email. |
| `subscription.updated` | Authoritatively synchronizes status, scheduled changes, billing dates, and monthly/annual plan changes. It covers renewal-period updates, payment recovery, cancellation scheduling/reversal, pause/resume, and terminal cancellation. |
| `subscription.canceled` | Provides a dedicated terminal-cancellation signal. This overlaps the canceled `subscription.updated` payload intentionally; event ordering and state idempotency prevent duplicate effects. |
| `transaction.completed` | Sends the renewal email only when `origin=subscription_recurring`, after Paddle has successfully collected and completed the renewal payment. Initial checkout and other transaction origins are ignored by this handler. |
| `transaction.payment_failed` | Sends the payment-recovery email when the failed transaction belongs to a subscription. Paddle may emit this for each failed attempt. |

Enable `adjustment.updated` alongside them. A card refund is created as
`pending_approval` and Paddle may reject it afterwards; without this, a
guarantee refund that fails after the member has already been switched to
monthly and charged is silent on both sides. The handler reports it rather than
reversing anything — unwinding a subscription from a webhook turns one bad
refund into two bad states, so it is settled by hand.

Dedicated `subscription.activated`, `subscription.past_due`,
`subscription.paused`, and `subscription.resumed` events are not required. The
complete subscription payload delivered by `subscription.updated` is the
authoritative input for those states. Do not replace `subscription.updated`
with only the dedicated events.

Copy that destination's endpoint secret into `PADDLE_WEBHOOK_SECRET`. The
backend validates `Paddle-Signature` against the exact raw body, rejects stale
timestamps, and logs event IDs transactionally for idempotency.

## Lifecycle and email behavior

Paddle delivers webhooks at least once and may deliver them out of order. The
backend therefore applies two separate guards:

1. `paddle_event_log.event_id` deduplicates delivery of the same event.
2. `plan_subscription.latest_paddle_event_occurred_at` rejects an older
   lifecycle snapshot after a newer one. A pessimistic row lock serializes
   concurrent updates for the same subscription.

`subscription.created` never changes the lifecycle status of an already-known
subscription. A full `subscription.updated` payload with `status=active`, a
previous local status of `ACTIVE_TILL_CYCLE_END`, and no `cancel` scheduled
change means the scheduled cancellation was removed. That transition is
`REACTIVATED`, not `RENEWED`.

| Paddle state or event | Local result | Email event |
| --- | --- | --- |
| New subscription | Premium `ACTIVE` | `CREATED` |
| Active with `scheduled_change.action=cancel` | `ACTIVE_TILL_CYCLE_END`; Premium remains available | `CANCELED` once per transition |
| Scheduled cancellation removed | Premium `ACTIVE` | `REACTIVATED` |
| Successful recurring `transaction.completed` | No lifecycle mutation | `RENEWED` |
| `past_due` | Premium remains available during Paddle recovery | `PAYMENT_FAILED` comes from each failed payment attempt |
| `paused` | Paid subscription `PAUSED`; the local free plan becomes active | None |
| Active after pause | Paid subscription `ACTIVE`; the local free plan becomes inactive | Renewal payment may produce `RENEWED` |
| `canceled` | Paid subscription `CANCELED`; the local free plan becomes active | `ENDED` once |
| Price changed between configured monthly/annual prices | Local Premium plan is updated | None |

A daily 03:15 UTC reconciliation reads every locally tracked,
nonterminal Paddle subscription through the API and applies the same state
machine. This repairs missed or exhausted webhook deliveries for known
subscriptions. It cannot discover a subscription whose `subscription.created`
event never created a local Paddle subscription ID; investigate repeated
creation-webhook failures from the notification delivery log.

The supported catalog invariant is one recurring item per subscription. The
integration reads `items[0]` and rejects unknown price IDs. Refund/adjustment
notifications, one-off subscription charges, invoice-specific workflows, and
multiple recurring items are not currently product flows and have no handlers.

## Rollout note

The Paddle cutover removes the unused legacy provider columns and event log.
It assumes there are no existing paid customers or subscriptions to migrate.
