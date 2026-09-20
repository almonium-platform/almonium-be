# Variety and pronunciation foundation

A learner's variety is a language preference, independent of the UI locale and
independent of whether we currently offer audio for it. Onboarding and Settings
store it per target language. Changing it does not rewrite saved words or text.
Discover, regional senses, spelling adaptation and examples are not implemented
by this foundation.

## Choosing voices

Edit `src/main/resources/tts-voices.json`. It is a reviewed, non-secret catalogue
bundled into the application image. No environment variables, vault entries or
infra configuration are needed. Commit the file and deploy through the normal
pipeline; changes take effect when the new application starts.

Example:

```json
{
  "variety": "en-GB",
  "provider": "GOOGLE",
  "enabled": true,
  "languageCode": "en-GB",
  "voiceId": "en-GB-Chirp3-HD-Charon"
}
```

`voiceId` is the exact provider identifier, not a gender hint. The initial enabled
entries use the male Charon voice where available and male WaveNet voices for
Portugal and Taiwan as provisional defaults, not a quality ranking.
Substitute another supported male voice ID after listening. Confirm the ID and
language code in Google's [voice catalogue](https://docs.cloud.google.com/text-to-speech/docs/list-voices-and-types)
or ListVoices API. Local startup checks
structure, duplicates and variety/code consistency; it does not make paid synthesis
calls or claim to validate a voice against the live vendor catalogue.

A missing entry or `enabled: false` means no audio for that variety. The existing
authenticated audio endpoint returns 422 with the normal `{success, message}`
error body. There is deliberately no fallback to a different regional variety.
The preference remains selectable even while its voice is unavailable. Enabling
an entry requires a non-empty named voice matching its provider language code.
Mandarin's `zh-CN` / `zh-TW` identities map to Google's `cmn-CN` / `cmn-TW` codes.
Only the Google adapter is implemented. Adding an Azure voice ID does not enable
Azure: that requires a provider adapter and separately managed credentials.

Existing Google credentials are reused. Voice IDs and this file are not secrets.
No cache, pre-warming, storage bucket, pronunciation paywall or new playback UI
is introduced here. Those belong to the next TTS delivery slice.

## Variety identity and rollout

`LanguageVariety` owns supported tags and defaults. Every language resolves a
non-null variety on API responses. A language with no regional distinction yet
uses its bare BCP-47 language tag; common supported single choices have explicit
regional tags such as `it-IT`, `uk-UA` and `pl-PL`. Clients only show a selector
for languages with multiple choices. Client labels are kept in their respective
language-varieties catalogues; update both when adding choices.

The existing nullable varchar column remains additive and rollout-compatible.
An omitted choice is resolved on read. Single-choice defaults also remain null
in storage, including when submitted explicitly, so the prior slot's enum can
read them. This is rollout compatibility, not a data backfill. No data is dropped.
Multi-choice values already known to the prior release keep their existing enum
storage names. Introducing further persisted enum values requires accounting for
what the preceding application slot can read.

Tests cover defaults for every language, tag serialization, cross-language input
rejection, named provider requests, invalid voice configuration and unavailable
routes. Voice quality still requires listening; unit tests cannot certify it.

## Delivered and verified

- Backend foundation: `9a33dd89`.
- Web copy and accessible selector: `1e6c837`.
- Mobile onboarding, Settings and DTO wiring: `036c9c0`.

`./mvnw -B spotless:apply verify` passed for the backend. The web focused
Karma run passed 19 specs and `npm run lint` passed. Mobile `tsc --noEmit`,
ESLint (one existing generated `.expo` warning), Vitest (143 tests), and
`expo export --platform web` passed. The web E2E and mobile picker checks used
HTTP/component fixtures. No check called Google synthesis, so voice existence
and quality are still listening and vendor-catalogue checks.

The enabled routes are:

`en-US-Chirp3-HD-Charon`, `en-GB-Chirp3-HD-Charon`,
`en-AU-Chirp3-HD-Charon`, `en-IN-Chirp3-HD-Charon`,
`es-ES-Chirp3-HD-Charon`, `pt-BR-Chirp3-HD-Charon`, `pt-PT-Wavenet-F`,
`fr-FR-Chirp3-HD-Charon`, `fr-CA-Chirp3-HD-Charon`,
`de-DE-Chirp3-HD-Charon`, `nl-NL-Chirp3-HD-Charon`,
`nl-BE-Chirp3-HD-Charon`, `cmn-CN-Chirp3-HD-Charon`, `cmn-TW-Wavenet-B`,
`it-IT-Chirp3-HD-Charon`, `ja-JP-Chirp3-HD-Charon`,
`ko-KR-Chirp3-HD-Charon`, `pl-PL-Chirp3-HD-Charon`,
`ru-RU-Chirp3-HD-Charon`, and `uk-UA-Chirp3-HD-Charon`.

## Owner decisions

- Listen to the enabled voices and retain or replace each curated ID.
- Decide whether any current single-choice language should expose a regional
  tag instead of its bare language tag.
- Decide whether mobile should keep the selector in both onboarding and
  Settings, or make Settings the only mobile editing surface.
