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

Subscribe it to:

- `subscription.created`
- `subscription.updated`
- `subscription.canceled`
- `transaction.payment_failed`

Copy that destination's endpoint secret into `PADDLE_WEBHOOK_SECRET`. The
backend validates `Paddle-Signature` against the exact raw body, rejects stale
timestamps, and logs event IDs transactionally for idempotency.

## Rollout note

The Paddle cutover removes the unused legacy provider columns and event log.
It assumes there are no existing paid customers or subscriptions to migrate.
