# Paddle Billing setup

Almonium uses Paddle Billing for recurring Premium subscriptions. The backend
creates a Paddle customer and transaction, the browser opens the returned
transaction link through Paddle.js, and signed webhooks remain the source of
truth for entitlement changes.

## Catalog

Create one standard product named `Almonium Premium` in both Paddle sandbox and
live. Create four recurring USD prices against that product:

| Application key | Amount | Billing cycle | Visibility |
| --- | ---: | --- | --- |
| `premium_monthly` | $12 | monthly | public |
| `premium_annual` | $120 | annual | public |
| `founder_monthly` | $8 | monthly | application-controlled |
| `founder_annual` | $80 | annual | application-controlled |

The application never reads amounts from Paddle. It stores its own copy in the
`plan` table — `price` for the public amount, `founder_price` for the
discounted one — and serves both to the client through `/public/plans`. Only
the `pri_` IDs are configured, so an amount that disagrees with the Paddle
catalog is not detected anywhere: the page advertises the local number and
Paddle charges its own. Verify all four amounts by hand when creating a
catalog, and update the `plan` rows through Liquibase whenever a Paddle price
changes.

Paddle price IDs start with `pri_`. Paddle Billing does not have Stripe's
`lookup_key` field. The names in this table are Almonium configuration keys;
putting the same value in each Paddle price's `custom_data.lookup_key` is useful
for dashboard readability but is not required by the application.

Sandbox and live catalogs are separate. Copy each environment's four `pri_`
IDs into that environment's backend configuration. The founder prices are
never returned as public plan records. The backend selects one only after it
durably reserves a founder slot, and rejects a stale founder checkout request
instead of silently charging the regular price.

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

## Checkout and webhooks

Set the Paddle default payment link to a page that loads Paddle.js:

- sandbox/staging: `https://staging.almonium.com/payment/checkout`
- live: `https://almonium.com/payment/checkout`
- local sandbox when needed: `http://localhost:9999/payment/checkout`

The live domains must be approved in Paddle before checkout works. The backend
uses Paddle's account default payment link when creating a transaction, so set
the correct default link separately in each Paddle environment.

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
