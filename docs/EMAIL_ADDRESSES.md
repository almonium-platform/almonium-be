# Almonium email addresses

Every address on `almonium.com` resolves to one real mailbox. `tech@` is that
mailbox; the other names are alias addresses that deliver into it. Adding a
name costs nothing and changes no infrastructure, so use the address that
matches the audience rather than reusing `support@` everywhere.

`noreply@almonium.com` is separate: it is the sending identity the application
uses through Zepto Mail, not a mailbox anyone reads. Never print it as a
contact address.

## Addresses

| Address | Purpose |
| --- | --- |
| `tech@almonium.com` | The real mailbox. Domain, DNS, certificate, and registrar correspondence. |
| `billing@almonium.com` | Paddle, invoices, refunds, tax and payment questions. |
| `dmarc@almonium.com` | Destination for DMARC aggregate reports (`rua`). Machine traffic only. |
| `hello@almonium.com` | General and press enquiries; the friendly public address. |
| `privacy@almonium.com` | Privacy policy, data-subject requests, deletion and export requests. |
| `security@almonium.com` | Vulnerability reports and responsible disclosure. |
| `social@almonium.com` | Social accounts, community platforms, and partnerships. |
| `support@almonium.com` | User-facing product support. The default contact in the product. |
| `noreply@almonium.com` | Outbound sender for transactional mail. Not monitored. |

## Where they appear today

- `support@almonium.com` — the OpenAPI contact in
  `src/main/java/com/almonium/config/OpenAPIConfig.java`, and the "Report" link
  in the frontend's shared-page footer
  (`almonium-fe/src/app/sections/shared-link/shared-footer/shared-footer.component.ts`).
- `tech@almonium.com` — the ACME account email for the Porkbun certificate
  resolver in `almonium-infra/traefik/docker-compose.yaml`.
- `noreply@almonium.com` — `app.email.from-address` in
  `src/main/resources/application.yaml`.
- `dmarc@almonium.com` — referenced from the domain's DMARC DNS record, not
  from any repository.

`billing@`, `hello@`, `privacy@`, `security@`, and `social@` are provisioned
but not yet referenced anywhere. They are the addresses to use when the terms,
privacy policy, pricing page, and landing page get written.

## Rules

- Put the address in configuration or a template, never hard-coded in a service
  or component that has nothing to do with presentation.
- Keep `noreply@` for sending only; every message it sends should name a real
  address for replies.
- Record any new alias in this table when it starts being used, and say what it
  is for. An alias nobody documents becomes an address nobody reads.
