# Short links

Almonium owns the `go.almonium.com` domain in Short.io. It is the stable,
readable public-link layer for selected assets and public destinations. The
backend does not call the Short.io API: it builds links from a fixed domain and
an alias, while the redirect targets are managed in the Short.io dashboard.

## Current usage

`StreamChatService` supplies Short.io links to Stream Chat for images that need
to be reachable by a third-party service:

| Alias | Current purpose | Redirect target |
| --- | --- | --- |
| `/logo` | Default Almonium channel image | Firebase Storage object `avatars/channels/logo.png` |
| `/logo-en`, `/logo-de`, `/logo-es`, `/logo-fr`, `/logo-it` | Language-channel images | Public Firebase Storage channel-logo assets |
| `/saved-messages` | Image for each user's self-chat channel | A stable public asset |

The aliases are currently generated in
`src/main/java/com/almonium/infra/chat/service/StreamChatService.java`. Keep
these aliases stable: existing Stream Chat channel records and caches may
retain the URLs.

For example, `https://go.almonium.com/logo` is the readable public alias for
the `avatars/channels/logo.png` object. The tokenized Firebase target is not
copied here because it is a bearer-style download URL.

Do not commit Firebase download tokens or full tokenized Firebase URLs to this
repository. A Short.io redirect hides an unwieldy URL, but it does not make the
target private. Anyone who obtains the short URL can follow it, and a Firebase
download URL contains a bearer-style download token.

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
backend, Firebase Storage Rules, or a short-lived signed URL. User avatars are
currently stored under Firebase Storage `avatars/users/...`; they should remain
normal Firebase URLs unless a specific public integration needs a stable alias.

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
