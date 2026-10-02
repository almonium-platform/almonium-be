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
  "voiceId": "en-GB-Chirp3-HD-Charon",
  "gender": "MALE"
}
```

`voiceId` is the exact provider identifier, not a gender hint. Enabled entries prefer male Charon, then male Neural2/WaveNet/Standard voices.
Existing selected voices are retained. These are provisional defaults, not a
quality ranking. Afrikaans, Basque, Catalan, Galician and Icelandic use the only
available gender (female); the API and configuration expose this explicitly.
Substitute another supported male voice ID after listening. Confirm the ID and
language code in Google's [voice catalogue](https://docs.cloud.google.com/text-to-speech/docs/list-voices-and-types)
or ListVoices API. Local startup checks
structure, duplicates and variety/code consistency; it does not make paid synthesis
calls or claim to validate a voice against the live vendor catalogue.

Every variety must have an explicit entry; missing entries fail startup.
`enabled: false` means no audio for that variety. The existing
authenticated audio endpoint returns 422 with the normal `{success, message}`
error body. There is deliberately no fallback to a different regional variety.
The preference remains selectable even while its voice is unavailable. Enabling
an entry requires a non-empty named voice matching its provider language code.
Mandarin's `zh-CN` / `zh-TW` identities map to Google's `cmn-CN` / `cmn-TW` codes.
Norwegian `no` maps to Bokmål `nb-NO`; Tagalog `tl` maps to `fil-PH`.
These explicit provider aliases do not permit arbitrary regional substitutions.
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

## Complete coverage and API (2026-10-01)

All 132 languages / 146 varieties now have explicit configuration entries.
61 routes across 54 language identities are enabled; 85 varieties have no
matching voice in the current Google catalogue.
Voice IDs, language codes and genders were checked against Google's published
[voice inventory](https://docs.cloud.google.com/text-to-speech/docs/list-voices-and-types).
Swahili (`sw-KE`) is additionally listed in the newer
[Chirp model documentation](https://docs.cloud.google.com/text-to-speech/docs/chirp3-hd),
which specifies the locale/model/voice naming convention.
This is catalogue verification, not live synthesis or listening validation.
Unavailable regional varieties are never mapped to a different region.

Authenticated endpoints, under the existing `/api/v1` prefix:

- `GET /lang/voices`: all 146 entries, including unavailable ones.
- `GET /lang/voices/{language}`: entries for an enum code such as `DE` or `HI`;
  unknown codes return 400. Single-choice languages return one entry.
- Existing `GET /lang/words/{text}/audio/{lang}` synthesizes MP3 with the
  learner's selected variety, or the language default; unavailable routes return 422.

Catalogue rows contain `language`, `variety` (BCP-47), `defaultVariety`,
`available`, `unavailableReason`, `provider`, `languageCode`, `voiceId`, and `gender`.
Unavailable rows use `NO_ENABLED_VOICE` and null provider/voice metadata.
Availability means an enabled configuration, not a provider health check.
Listing makes no Google calls. Both client API adapters expose `getVoices(language?)`;
no Discover UI changes are included.

Verification for this expansion: `./mvnw -B verify` passed (524 tests, 3 skipped,
SpotBugs clean). After adding the Swahili route, the catalogue, controller and
Google voice-selection tests were rerun with `spotless:apply test` and passed.
Web `npm run test:ci -- --include=src/app/services/language-api.service.spec.ts`
and `npm run lint` passed. Mobile `npm run check` passed (147 tests).
Controller tests use standalone MockMvc; client requests use mocked HTTP/fetch
and verify cookie/bearer behavior. No live synthesis or deployed endpoint was tested.

Enabled routes (the JSON file is authoritative):

| Variety | Voice ID | Gender |
| --- | --- | --- |
| `en-US` | `en-US-Chirp3-HD-Charon` | MALE |
| `en-GB` | `en-GB-Chirp3-HD-Charon` | MALE |
| `en-AU` | `en-AU-Chirp3-HD-Charon` | MALE |
| `en-IN` | `en-IN-Chirp3-HD-Charon` | MALE |
| `es-ES` | `es-ES-Chirp3-HD-Charon` | MALE |
| `pt-BR` | `pt-BR-Chirp3-HD-Charon` | MALE |
| `pt-PT` | `pt-PT-Wavenet-F` | MALE |
| `fr-FR` | `fr-FR-Chirp3-HD-Charon` | MALE |
| `fr-CA` | `fr-CA-Chirp3-HD-Charon` | MALE |
| `de-DE` | `de-DE-Chirp3-HD-Charon` | MALE |
| `nl-NL` | `nl-NL-Chirp3-HD-Charon` | MALE |
| `nl-BE` | `nl-BE-Chirp3-HD-Charon` | MALE |
| `zh-CN` | `cmn-CN-Chirp3-HD-Charon` | MALE |
| `zh-TW` | `cmn-TW-Wavenet-B` | MALE |
| `af` | `af-ZA-Standard-A` | FEMALE |
| `ar` | `ar-XA-Chirp3-HD-Charon` | MALE |
| `eu` | `eu-ES-Standard-B` | FEMALE |
| `bn` | `bn-IN-Chirp3-HD-Charon` | MALE |
| `bg` | `bg-BG-Chirp3-HD-Charon` | MALE |
| `ca` | `ca-ES-Standard-B` | FEMALE |
| `hr` | `hr-HR-Chirp3-HD-Charon` | MALE |
| `cs` | `cs-CZ-Chirp3-HD-Charon` | MALE |
| `da` | `da-DK-Chirp3-HD-Charon` | MALE |
| `et` | `et-EE-Chirp3-HD-Charon` | MALE |
| `fil` | `fil-ph-Neural2-D` | MALE |
| `fi` | `fi-FI-Chirp3-HD-Charon` | MALE |
| `gl` | `gl-ES-Standard-B` | FEMALE |
| `el` | `el-GR-Chirp3-HD-Charon` | MALE |
| `gu` | `gu-IN-Chirp3-HD-Charon` | MALE |
| `he` | `he-IL-Chirp3-HD-Charon` | MALE |
| `hi` | `hi-IN-Chirp3-HD-Charon` | MALE |
| `hu` | `hu-HU-Chirp3-HD-Charon` | MALE |
| `is` | `is-IS-Standard-B` | FEMALE |
| `id` | `id-ID-Chirp3-HD-Charon` | MALE |
| `it-IT` | `it-IT-Chirp3-HD-Charon` | MALE |
| `ja-JP` | `ja-JP-Chirp3-HD-Charon` | MALE |
| `kn` | `kn-IN-Chirp3-HD-Charon` | MALE |
| `ko-KR` | `ko-KR-Chirp3-HD-Charon` | MALE |
| `lv` | `lv-LV-Chirp3-HD-Charon` | MALE |
| `lt` | `lt-LT-Chirp3-HD-Charon` | MALE |
| `ms` | `ms-MY-Wavenet-B` | MALE |
| `ml` | `ml-IN-Chirp3-HD-Charon` | MALE |
| `mr` | `mr-IN-Chirp3-HD-Charon` | MALE |
| `no` | `nb-NO-Chirp3-HD-Charon` | MALE |
| `pl-PL` | `pl-PL-Chirp3-HD-Charon` | MALE |
| `pa` | `pa-IN-Chirp3-HD-Charon` | MALE |
| `ro` | `ro-RO-Chirp3-HD-Charon` | MALE |
| `ru-RU` | `ru-RU-Chirp3-HD-Charon` | MALE |
| `sr` | `sr-RS-Chirp3-HD-Charon` | MALE |
| `sk` | `sk-SK-Chirp3-HD-Charon` | MALE |
| `sl` | `sl-SI-Chirp3-HD-Charon` | MALE |
| `sw` | `sw-KE-Chirp3-HD-Charon` | MALE |
| `sv` | `sv-SE-Chirp3-HD-Charon` | MALE |
| `tl` | `fil-ph-Neural2-D` | MALE |
| `ta` | `ta-IN-Chirp3-HD-Charon` | MALE |
| `te` | `te-IN-Chirp3-HD-Charon` | MALE |
| `th` | `th-TH-Chirp3-HD-Charon` | MALE |
| `tr` | `tr-TR-Chirp3-HD-Charon` | MALE |
| `uk-UA` | `uk-UA-Chirp3-HD-Charon` | MALE |
| `ur` | `ur-IN-Chirp3-HD-Charon` | MALE |
| `vi` | `vi-VN-Chirp3-HD-Charon` | MALE |

## IPA direction (planned, not implemented)

Tested on 2026-10-02: which engines obey IPA, per language, is recorded in
[`TTS_IPA_FINDINGS.md`](TTS_IPA_FINDINGS.md). It supersedes the expectations in
this section wherever they differ; in particular Ukrainian needs WaveNet, and
the catalogue's current plain Chirp routes carry no pronunciation control yet.

Google's [synthesis response](https://docs.cloud.google.com/text-to-speech/docs/reference/rest/v1/text/synthesize)
contains audio, not its inferred IPA. Independently generating IPA and synthesizing
bare text cannot ensure that the learner reads and hears the same pronunciation.

Use a versioned pronunciation record identified by lexical form, variety and
pronunciation variant (including sense/part-of-speech where needed). Store IPA,
source/license and source revision. Dictionary-derived IPA is preferred; any
grapheme-to-phoneme fallback must be marked inferred. Preserve multiple valid
pronunciations. A multiword lexical expression needs its own pronunciation;
joining individual word transcriptions does not reliably capture phrase stress.

The displayed IPA and synthesis phoneme controls should come from the same
record. [Chirp custom pronunciation controls](https://docs.cloud.google.com/text-to-speech/docs/chirp3-hd#custom_pronunciations)
accept IPA, but remain preview and exclude many locales, including Ukrainian.
The same page documents preview SSML phoneme support for synchronous synthesis;
verify actual voice/language behavior before claiming enforcement. If phoneme
control cannot be validated, use a matched, licensed dictionary recording or mark
audio as unverified against the IPA. Do not claim global zero drift.

A future audio artifact must reference pronunciation revision, voice ID and
generation settings. Keep the reviewed bytes to avoid silent vendor regeneration
changes. Broad dictionary IPA is a learning target, not a narrow transcription of
every speaker detail. This slice adds neither IPA lookup nor audio storage.

## Owner decisions

- Listen to the enabled voices and retain or replace each curated ID.
- Decide whether any current single-choice language should expose a regional
  tag instead of its bare language tag.
- Decide whether mobile should keep the selector in both onboarding and
  Settings, or make Settings the only mobile editing surface.
