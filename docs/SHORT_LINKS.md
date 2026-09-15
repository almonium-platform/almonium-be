# Short links

Almonium owns the `go.almonium.com` domain in Short.io. It is the stable,
readable public-link layer for selected assets and public destinations. The
backend does not call the Short.io API: it builds links from a fixed domain and
an alias, while the redirect targets are managed in the Short.io dashboard.

## Current usage

Nothing in the backend builds a short link any more.

Chat channel artwork used to go through `/logo`, `/logo-en`, `/logo-de`,
`/logo-es`, `/logo-fr`, `/logo-it` and `/saved-messages`. It is now served from
the web client's own domain (`{web-domain}/chat/logo-de.png`, the same way
`{web-domain}/email/wordmark-white.png` already worked), and each file lives in
`almonium-fe/public/chat/`. Stream only ever stores that URL and hands it back
to a client, so the redirect bought two external dependencies, a Firebase
download token that would silently kill the artwork if rotated, and readability
that no human was ever going to see. Saved Messages carries no image at all now
- the client draws its own emblem.

The aliases still exist in Short.io and still resolve; no code depends on them.
Delete them when you are satisfied the new URLs are live.

The guidance below stands for the next link that genuinely wants to be public,
readable, and redirectable.

## Good uses

Use `go.almonium.com` when the destination is public, stable, and benefits from
a readable or shareable URL:

- Stream Chat images and other third-party integration fields that require a
  public URL;
- public brand assets such as `/logo`, `/favicon`, `/og-image`, and app badges;
- stable public marketing or product destinations such as `/ios`, `/android`,
  `/extension`, `/pricing`, or `/docs`;
- campaign, QR-code, and partner links where Short.io click analytics and
  replaceable redirect targets are useful;
- stable public content aliases that redirect to an Almonium frontend route,
  such as `/book/{slug}` when the public URL contract is intentionally owned by
  Short.io.

Prefer a frontend route or a backend redirect for links that need application
authorization, dynamic ownership checks, or application analytics tied to a
logged-in user. Short.io should not become the source of truth for those
permissions.

## Do not use it for

- private files, user-specific files, or temporary downloads;
- upload URLs, signed URLs, session tokens, password-reset links, or invitation
  secrets;
- every user's avatar or every generated file as a separate Short.io link;
- links whose access must be revoked immediately by Almonium;
- API endpoints or internal service-to-service traffic.

For those cases, keep the object private and authorize access through the
backend or a short-lived signed URL. User avatars are pictures the clients
ship, not stored files.

## Naming and operations

- Use lowercase, short, semantic aliases; treat them as permanent public API.
- Prefer one stable alias per public concept rather than aliases containing
  Firebase object IDs or download tokens.
- Keep redirect targets limited to public assets or public application routes.
- Record every production alias in this document when it becomes part of
  application behavior.
- Update the Short.io redirect first when replacing an asset, then verify the
  redirect in staging/production. Avoid changing the alias merely because the
  underlying Firebase object changed.
- If a target was accidentally private or a download token was exposed, revoke
  the Firebase download token and replace the Short.io target; changing the
  alias alone does not invalidate an already shared target URL.

Short.io is therefore a presentation and redirect layer, not a storage,
authorization, or secret-management layer.
