# TTS and IPA: what the engines actually obey

Recorded 2026-10-02. This is the evidence base for pronunciation audio. It is a
research record, not an implementation: nothing here is wired into the
application yet. `VARIETY_AND_TTS.md` describes what is built; the plan for the
lexical core that consumes these findings lives in the owner's "Linguistic core:
plan" document.

## The rule this establishes

One IPA string per pronunciation is both shown to the learner and sent to the
engine. That only removes drift when the engine obeys the IPA, and **an engine
accepting a request proves nothing**. Every route below was confirmed by the
owner's ear, and for deterministic voices by a byte comparison as well.

A route is a triple: engine, language variety, control mode. It is trusted only
after a listening test on that triple.

## Decision for the launch languages

| Variety | Engine and voice | Control | Verified |
|---|---|---|---|
| de-DE | Chirp 3 HD, Charon (male) | IPA via `customPronunciations` | Real pair `umfahren`, both stresses |
| en-US | Chirp 3 HD, Charon | IPA | Real pair `record` noun and verb |
| en-GB | Chirp 3 HD, Charon | IPA | Made-up word; clearer than en-US on the last syllable |
| fr-FR | Chirp 3 HD, Charon | IPA, sounds only | Real pair `fils` with and without /s/ |
| es-ES | Chirp 3 HD, Charon | IPA | Real pair `público` / `publicó` |
| it-IT | Chirp 3 HD, Charon | IPA | Real pairs `principi`, `subito`; `ancora` subtle |
| uk-UA | WaveNet-B (female, robotic) | IPA via SSML `<phoneme>`, or a stress mark in the text | Six real pairs, both controls sound the same |

Gemini TTS is rejected as an IPA route. Voice flavour (Charon, Orus, Puck) was
judged immaterial; Charon stays.

## How to drive each engine

Chirp 3 HD, `text:synthesize`:

```json
{
  "input": {
    "text": "umfahren",
    "customPronunciations": {"pronunciations": [
      {"phrase": "umfahren", "phoneticEncoding": "PHONETIC_ENCODING_IPA", "pronunciation": "ʊmˈfaːʁən"}
    ]}
  },
  "voice": {"languageCode": "de-DE", "name": "de-DE-Chirp3-HD-Charon"},
  "audioConfig": {"audioEncoding": "MP3"}
}
```

Older voices (WaveNet, Neural2, Standard):

```json
{
  "input": {"ssml": "<speak><phoneme alphabet=\"ipa\" ph=\"zaˈmɔk\">замок</phoneme></speak>"},
  "voice": {"languageCode": "uk-UA", "name": "uk-UA-Wavenet-B"},
  "audioConfig": {"audioEncoding": "MP3"}
}
```

Ukrainian WaveNet also obeys a combining acute (U+0301) placed after the
stressed vowel in plain text: `{"text": "замо́к"}`. This works inside a sentence
too, where the unmarked sentence is pronounced wrongly.

## Rules learned

1. **Accepted is not obeyed.** Gemini TTS returns 200 for the same
   `customPronunciations` field and ignores it. Older voices return 200 for any
   SSML and silently speak the text when they cannot use the IPA.
2. **The engine wants phonemes from its own per-language list, not dictionary
   IPA.** Chirp rejects an unknown symbol with HTTP 400, which is a free and
   loud validation step. Rejections seen and the fix:

   | Variety | Rejected | Accepted |
   |---|---|---|
   | en-GB | /r/ | /ɹ/ |
   | it-IT | /ŋ/, long vowel `oː` | /n/, plain vowels |
   | es-ES | /β/ | /b/ |
   | uk-UA (Gemini) | /ɑ/ | /a/ |
   | pl-PL | plain `t n s` | dental `t̪ n̪ s̪` |
   | de-DE | plain `i` in an open syllable set | `iː` |
   | ko-KR | `g`, `s`, `o`, `e` | `k`, `sʰ`, `ʌ`, `ɛ` |
   | hi-IN | `i u a` | `ɪ ʊ ə` with dental `t̪` |

   Google publishes the lists for 22 locales at
   <https://docs.cloud.google.com/text-to-speech/docs/phonemes>. Each variety
   needs a small normalizer from stored IPA to that list.
3. **Older voices fall back silently.** One symbol outside the voice's list and
   the whole `<phoneme>` is dropped. Google publishes no list for most of these
   languages, so a working symbol set was found by trial (table below).
4. **Byte test: compare IPA against IPA, never IPA against text.** For a
   deterministic voice, send two different IPA strings with the same text. If
   the audio is byte-identical, both fell back and the IPA was ignored. Comparing
   an IPA clip with a plain-text clip gives false positives, because the fallback
   rendering of text inside a tag can differ from the same text sent plainly.
   Scored IPA against IPA, the test matched the owner's ear on all 31 older
   voices.
5. **Determinism differs by family.** Standard voices return identical bytes for
   identical requests. WaveNet and Neural2 return one of two or three renderings,
   so sample each input several times and compare the sets of hashes. Chirp never
   repeats itself, so the byte test does not apply; it rejects loudly instead.
6. **Some languages have no stress to mark.** For French, Indonesian, Korean,
   Tamil, Kannada and Malayalam, Chirp rejects any request containing a stress
   mark and accepts the same sounds without it.
7. **Chirp refuses IPA outright for 27 locales** with the message "custom
   pronunciations are not supported for locale". Pause tags are refused for
   uk-UA as well. Chinese requires `PHONETIC_ENCODING_PINYIN` and Japanese a
   kana encoding instead of IPA.
8. **Chirp cannot be steered for Ukrainian by any trick.** It reads a combining
   acute as an extra "s" sound. Apostrophe, hyphen, capital vowel, plus sign,
   doubled vowel and Latin spelling moved the stress on one or two words and not
   on others, or produced two glued words.
9. **Context does not fix stress on Chirp.** "Дверний замок зламався" came out as
   за́мок on three voices.
10. **Gemini reads prompt instructions aloud**, so prompting for a stress is not
    usable. With a plain-text accent mark it got roughly seven of eleven
    Ukrainian clips right.

## Full matrix

All tests used the enabled routes in `src/main/resources/tts-voices.json`: 61
varieties, 51 of them Chirp. The other 85 of 146 varieties have no Google voice.

### Chirp 3 HD, IPA obeyed (21 varieties)

Test: text `pigutan`, IPA with the stress on each syllable in turn, plus a probe
whose IPA spells a different word (`somakel`). Heard by the owner: the probe
speaks the IPA's word in all 21, and stress lands where asked in the 14 that
have stress.

| Variety | Control | IPA that was accepted (stress on 1st) | Probe IPA |
|---|---|---|---|
| en-US | sounds and stress | `ˈpiːɡuːtæn` | `soʊˈmækɛl` |
| en-GB | sounds and stress | `ˈpiːɡuːtæn` | `səʊˈmækɛl` |
| en-AU | sounds and stress | `ˈpiːɡuːtæn` | `səʊˈmækɛl` |
| de-DE | sounds and stress | `ˈpiːgutan` | `soˈmakɛl` |
| nl-NL, nl-BE | sounds and stress | `ˈpigutɑn` | `soːˈmɑkɛl` |
| es-ES | sounds and stress | `ˈpigutan` | `soˈmakel` |
| pt-BR | sounds and stress | `ˈpigutan` | `soˈmakel` |
| it-IT | sounds and stress | `ˈpiɡutan` | `soˈmakel` |
| pl-PL | sounds and stress | `ˈpigut̪an̪` | `s̪ɔˈmakɛl` |
| ru-RU | sounds and stress | `ˈpigutan` | `soˈmakel` |
| tr | sounds and stress | `ˈpigutan` | `soˈmakel` |
| ar (ar-XA) | sounds and stress | `ˈbikutan` | `suˈmakil` |
| hi | sounds and stress | `ˈpɪgʊt̪ən` | `soːˈməkeːl` |
| fr-FR, fr-CA | sounds only | `pigutan` | `somakel` |
| id | sounds only | `pigutan` | `somakel` |
| ta | sounds only | `pigutan` | `somakel` |
| ko-KR | sounds only | `pikutan` | `sʰʌmakɛl` |
| kn, ml | sounds only | `piːguːt̪aːn` | `soːmaːkeːl` |

nl-BE is on Google's exclusion list and was accepted anyway.

### Chirp 3 HD, not usable with IPA (30 varieties)

- Refused for the locale (24): bn, bg, hr, cs, da, et, fi, el, gu, he, hu, lv,
  lt, no, pa, ro, sr, sk, sl, sw, sv, th, uk-UA, vi.
- Needs another notation (2): zh-CN (pinyin), ja-JP (kana).
- Generated syllables rejected, needs a hand-built attempt (4): en-IN, mr, te, ur.

### Older Google voices with the SSML phoneme tag (31 voices)

| Result by ear | Variety | Voice | Symbol set that works |
|---|---|---|---|
| Sounds and stress | uk-UA | uk-UA-Wavenet-B | `a i u` with `s m k l`; also `ɔ ɛ` |
| Sounds and stress | da | da-DK-Wavenet-G | plain `i u a o e`; stress needs the stressed vowel lengthened, for example `piˈguːtan` |
| Sounds and stress, very clear | fil, tl | fil-ph-Neural2-D (one shared voice) | `m n` with `a i` |
| Sounds and stress, slight | af | af-ZA-Standard-A | `a i u` with `s m k l` |
| Sounds only | ro | ro-RO-Wavenet-B | plain `i u a o e` |
| Sounds only | pt-PT | pt-PT-Wavenet-F | plain `i u a o e`; a lengthened vowel breaks the word |
| Sounds only | ca | ca-ES-Standard-B | plain `i u a o e` |
| Sounds only | bg | bg-BG-Standard-B | `t n m p` with `u i a` |
| Sounds only | cs | cs-CZ-Wavenet-B | `ɔ a ɛ` with `s m k l` |
| Sounds only | hu | hu-HU-Wavenet-B | `a i u` with `s m k l` |
| Sounds only | sk | sk-SK-Wavenet-B | `a i u` with `s m k l` |
| Sounds only; a stress mark makes the voice drop the IPA | bn, lv, pa, sr, sv, is, ms, no | WaveNet or Standard | the voice's own list, no stress mark; heard on round 3 |
| Sounds, heard on round 3; stress mark accepted by bytes, not yet judged by ear | fi | fi-FI-Wavenet-B | the voice's own list, `ɑ` not `a` |
| Heard as not following, despite a recovered list | he, lt | WaveNet, Standard | the recovered lists contain odd symbols and are probably noisy |
| Unresolved | et, gl, gu | WaveNet or Standard | byte results implausible |
| IPA not accepted at all | el, th, vi, zh-TW, eu | WaveNet, Neural2 or Standard | no symbol pair ever differs from the fallback |

Czech, Hungarian and Slovak have fixed first-syllable stress, so a voice that
will not stress another syllable is behaving correctly for the language. No
older voice exists for hr, sl, sw.

### Count

| State | Voices |
|---|---|
| IPA with stress, verified by ear | 18: 14 on Chirp, 4 older |
| IPA sounds, language has no stress to mark | 7 on Chirp |
| IPA sounds only, stress not audible or not accepted | 16 older |
| Voice exists, IPA not followed, refused or unresolved | 13 |
| Needs another notation or hand-built sounds | 6 |

That is 41 voices where the IPA shown is what gets spoken.

## Sound lists discovered by bytes

Google publishes no phoneme table for most languages the older voices cover,
because the phoneme tag is not officially supported there. The lists can be
recovered anyway. An older voice that cannot use the IPA speaks the text inside
the tag, and that fallback audio is the same whatever the IPA said. So a symbol
is accepted exactly when a string containing it produces audio that is not the
fallback. `scripts/tts_research/discover_inventory.py` samples the fallback with
impossible IPA, finds one working consonant and vowel, then tests about 130
symbols one at a time inside that frame.

The recovered lists look like the languages: Ukrainian has the palatalized
series and /ɦ/, Hungarian /c ɟ ɲ/ and front rounded vowels, Norwegian and
Swedish the retroflexes, Icelandic the aspirated stops, Finnish /ɑ æ y ø ʋ/.

| Voice | Consonants | Vowels | Stress mark | Length mark |
|---|---|---|---|---|
| uk-UA-Wavenet-B | p b t d k g ɡ m n f v s z ʃ ʒ x ɦ ts dz tʃ dʒ ʧ ʤ ʦ l r j rʲ lʲ nʲ tʲ dʲ sʲ | a ɛ i ɪ ɔ u ai au aɪ ɔɪ | accepted, moves | no |
| da-DK-Wavenet-G | p b t d k g ɡ m n ŋ f v s h θ ð ɕ ts tɕ ʦ l ɹ ʁ j w sʲ | a ɑ e ɛ ə i y ø œ o ɔ u ɒ aː eː iː oː uː ɛː ɔː ɑː yː øː ai au ei ou oi | accepted, moves | yes |
| fil-ph-Neural2-D | p b t d k g ɡ ʔ m n ŋ ɲ f v s z ʃ ʒ h θ ð ts dz tʃ dʒ ʧ ʤ ʦ l ɾ j w | a ɛ ə i o ʊ ʌ ai au ei ou oi aʊ oʊ əʊ | accepted, moves | no |
| af-ZA-Standard-A | p b t d k g ɡ m n ŋ f v s z ʃ ʒ x h θ ð ts dz tʃ dʒ ʧ ʤ ʦ l r ɹ j w | a æ ɐ ɛ ə i ɪ y œ ɔ u ʊ ʌ ɒ iː uː ɔː ɑː øː æː ai au aɪ aʊ eɪ ɔɪ əʊ | accepted, moves | no |
| bg-BG-Standard-B | p b t d k g ɡ m n f v s z ʃ ʒ x ts dz tʃ dʒ ʧ ʤ ʦ ɫ ʎ r j rʲ nʲ tʲ dʲ sʲ | a ɛ i o u ɤ ai au ou oi | accepted, moves | no |
| cs-CZ-Wavenet-B | p b t d k g ɡ ʔ m n ŋ ɲ f v s z ʃ ʒ x ɣ ɦ θ ç ʑ ts dz tʃ dʒ ʧ ʤ ʦ l r ɹ j w c ɟ ɽ pʰ z̪ lʲ sʲ | a æ ɛ ə ɪ ɔ u ʌ ɤ aː iː uː ɛː ɔː au ei aɪ ɔɪ | accepted, moves | yes |
| hu-HU-Wavenet-B | p b t d k g ɡ m n ɲ f v s z ʃ ʒ h ts dz tʃ dʒ ʧ ʤ ʦ l r j c ɟ | a ɛ ə i y ø o u ɒ aː eː iː oː uː yː øː ai au ou oi | accepted, moves | yes |
| sk-SK-Wavenet-B | p b t d k g ɡ m n ɲ f v s z ʃ ʒ x ɦ θ ts dz tʃ dʒ ʧ ʤ ʦ l ʎ r ɹ j w c ɟ | a ɛ ə i ɔ u aː iː uː ɛː ɔː ai au | accepted, moves | yes |
| ro-RO-Wavenet-B | p b t d k g ɡ m n f v s z ʃ ʒ h ts dz tʃ dʒ ʧ ʤ ʦ l r j w | a e ə i ɨ o u ai au ei ou oi | accepted, moves | no |
| fi-FI-Wavenet-B | p b t d k g ɡ ʔ m n ŋ f s ʃ ʒ h ts tʃ dʒ ʧ ʤ ʦ l r j ʋ t̪ | ɑ æ e ə i y ø o u eː iː oː uː ɑː yː øː æː ei ou oi | accepted, moves | yes |
| he-IL-Wavenet-B | p b t d k g ɡ ʔ m n ŋ f v s z ʃ ʒ x h ɦ θ ð ts dz tʃ dʒ ʧ ʤ ʦ l ɹ ʁ ʀ j w ɖ tʰ dʰ t̪ rʲ tʲ dʲ | ɐ i y u eː ẽ au ou aɪ | accepted, moves | no |
| nb-NO-Wavenet-G | p b t d k g ɡ m n ŋ ɳ f v s z ʃ x h ð ç ʂ ts dz tʃ dʒ tʂ ʧ ʤ ʦ l ɾ j ʈ ɖ ɽ | ɑ æ ɛ ə ɪ ʏ œ ɔ ʊ eː iː oː uː ɑː yː øː æː ɔɪ əʊ | **drops the IPA** | yes |
| sv-SE-Wavenet-C | p b t d k g ɡ m n ŋ ɳ f v s h θ ð ʂ ɕ ts tɕ tʂ ʦ l r ɹ w ʈ ɖ | a æ ɛ i ɪ y ʏ ø œ ɔ ʊ eː iː oː uː ɛː ɔː ɑː yː øː ai aɪ aʊ ɔɪ | **drops the IPA** | yes |
| is-IS-Standard-B | p t k m n ŋ ɲ f v s x ɣ h θ ð ç ts ʦ l r j c pʰ tʰ kʰ | a ɛ i ɪ ʏ œ ɔ u aː iː uː ɛː ɔː ai au ei ou aɪ aʊ ɔɪ | **drops the IPA** | yes |
| lv-LV-Standard-B | p b t d k g ɡ m n ɲ f v s z ʃ ʒ x ts dz tʃ dʒ ʧ ʤ ʦ l ʎ r j c ɟ | a æ ɛ ə i ɔ u aː iː uː ɛː æː ai au | **drops the IPA** | yes |
| lt-LT-Standard-B | p b t d k g ɡ m n ŋ ɲ f s z ʃ ʒ x ɣ θ ʐ ts dz tʃ dʒ tɕ ʧ ʤ ʦ l ɫ ɾ j ʋ c ɖ ɸ bʰ t̪ z̪ rʲ lʲ nʲ sʲ | æ ɐ e ɛ i ɪ o ɔ ʊ ʌ ɤ aː eː iː oː uː ɑː øː æː ɔ̃ ẽ ai au ei oʊ ɔɪ əʊ | **drops the IPA** | yes |
| sr-RS-Standard-B | p b t d k g ɡ m n ɲ f v s z ʃ ʒ x ʂ ʐ ts dz tʃ dʒ tɕ dʑ tʂ dʐ ʧ ʤ ʦ l ʎ r j ʋ | a e ə i o u aː eː iː oː uː ai au ei ou oi | **drops the IPA** | yes |
| ms-MY-Wavenet-B | p b t d k g ɡ ʔ m n ŋ ɲ f v s z ʃ ʒ x ɣ h θ ð ç ts dz tʃ dʒ ʧ ʤ ʦ l ʎ r j w ʋ s̪ sʲ | a æ ɐ e ɛ ə i ɪ œ o u ɛ̃ ɑ̃ ai au ei ou oi aɪ aʊ ɔɪ | **drops the IPA** | no |
| bn-IN-Wavenet-B | p b k g ɡ m n ŋ f s z ʃ h tʃ dʒ ʧ ʤ l r ʈ ɖ ɽ pʰ kʰ bʰ gʰ t̪ d̪ | a æ e ə i o ɔ u ã ɔ̃ õ ẽ ai au ei ou oi | **drops the IPA** | no |
| pa-IN-Wavenet-B | p b k g ɡ m ŋ ɳ f s z ʃ x ɣ h tʃ dʒ ʧ ʤ l ɾ j ʋ ʈ ɖ ɽ pʰ kʰ t̪ d̪ n̪ | a e ɛ ə i ɪ o ɔ u ʊ ã ɛ̃ ɔ̃ õ ẽ ai au ei ou oi aɪ aʊ eɪ oʊ ɔɪ əʊ | **drops the IPA** | no |

What this corrected: the ten voices marked "IPA not followed" on the earlier
sheets were never given a fair clip. Every clip the owner heard for them carried
a stress mark, and for these voices **the stress mark itself is an unknown
symbol that makes the voice drop the whole IPA**. Without the mark their sounds
follow the IPA: the owner heard Bengali, Icelandic, Latvian, Malay, Norwegian,
Punjabi, Serbian and Swedish speak the probe word on `older-round3/index.html`,
and Finnish too, which had been written off because the probe used /a/ where
Finnish has /ɑ/. Hebrew and Lithuanian still said the text; their recovered
lists include unlikely symbols, so the discovery is probably noisy for them.

Notes on the rest:

- pt-PT-Wavenet-F and ca-ES-Standard-B accepted every one of the 130 symbols.
  They do not validate, so their list cannot be recovered this way.
- et-EE-Standard-A, gl-ES-Standard-B and gu-IN-Wavenet-B returned small,
  implausible sets. Treat them as unresolved.
- cmn-TW, el-GR, eu-ES, th-TH and vi-VN accepted nothing: no consonant and vowel
  pair ever differed from the fallback. Chinese, Thai and Vietnamese are tonal
  and likely need another notation.

Build consequence: before sending IPA to an older voice, check every symbol
against that voice's list and strip the stress mark where it is not accepted.
After synthesis, run the IPA-against-IPA byte check as the safety net.

## Azure, first pass (not yet heard)

A free-tier Azure Speech resource (`almonium-speech-research`, North Europe, in
resource group `almonium-speech`) was created on 2026-10-02. Its key is in the
backend `.env` as `AZURE_SPEECH_KEY` with `AZURE_SPEECH_REGION=northeurope`.
Azure lists 833 voices in 154 locales and has a male neural voice for every one
of the 60 locales the app enables, including uk-UA (Ostap), hr-HR, sl-SI and
sw-KE, which Google cannot serve with a steerable male voice or at all.

`scripts/tts_research/azure_sheet.py` ran the same test through the SSML
phoneme tag on one male neural voice per locale. What is established so far:

- Azure returned 200 for all 60, with a stress mark and dotted syllables, and
  also for impossible IPA. Contrary to its documentation it did not answer 400,
  so acceptance means nothing here either.
- Azure neural voices are not deterministic: the same request gave three
  different byte results. The byte test does not apply.
- A machine transcription of the probe clip, good only for which word was said,
  guesses that 45 of 60 speak the IPA's word. Among them are Greek, Hebrew,
  Thai, Vietnamese, Croatian, Finnish, Swedish, Norwegian, Czech, Hungarian and
  the male Ukrainian voice. It guesses that Basque, Estonian, Filipino,
  Galician, Icelandic, Latvian, Lithuanian and Swahili read the text, and is
  unclear on seven.

Heard by the owner on `azure-all/index.html`, which is sorted by the machine
guess:

- All 45 in the first group speak the IPA's word in the probe and `pigutan` in
  the word clips. The machine transcription was right about every one, and
  right about all eight it marked as reading the text.
- Stress was noted as clearly right for Italian and Danish. It has not been
  judged row by row for the rest.
- Three voices follow the IPA but garble some clips: ur-IN-Salman drops the
  /t/, sv-SE-Mattias mangles the stressed clips, ru-RU-Dmitry clips the word.
  The made-up syllables may sit badly in those sound systems; real words need
  testing before these routes are trusted.
- Of the seven the machine could not call: sl-SI-Rok is close to the probe word
  and counts as following; gu-IN-Niranjan says something like "sojakel", so it
  probably follows with a wrong consonant; af, bn, kn, ml and sr do not follow.

Voices heard to follow IPA on Azure: en-US, en-GB, en-AU, en-IN, es-ES, pt-BR,
pt-PT, fr-FR, fr-CA, de-DE, nl-NL, nl-BE, zh-CN, zh-TW, ar-EG, bg-BG, ca-ES,
hr-HR, cs-CZ, da-DK, fi-FI, el-GR, he-IL, hi-IN, hu-HU, id-ID, it-IT, ja-JP,
ko-KR, ms-MY, mr-IN, nb-NO, pl-PL, pa-IN, ro-RO, ru-RU, sk-SK, sv-SE, ta-IN,
te-IN, th-TH, tr-TR, uk-UA, ur-IN, vi-VN, sl-SI, and gu-IN doubtfully. Not
following: eu-ES, et-EE, fil-PH, gl-ES, is-IS, lv-LV, lt-LT, sw-KE, af-ZA,
bn-IN, kn-IN, ml-IN, sr-RS.

Follow-up probes with seven different words each (`azure-gujarati`,
`azure-more`): Gujarati, Urdu and Swedish all follow the IPA. They sound odd to
the owner's ear on made-up words, but the IPA is clearly what drives them. A
multilingual Azure voice (`en-US-AndrewMultilingualNeural` with a `<lang>` tag)
was tried for Lithuanian, Estonian, Basque, Galician and Swahili and said the
text every time.

Amazon Polly was checked and not tested: its 41 locales include none of those
five, nor Ukrainian, Greek, Hebrew, Thai, Vietnamese, Hungarian, Urdu or
Gujarati, so it adds no coverage.

Across both providers, every enabled variety except Basque, Estonian, Galician,
Lithuanian and Swahili now has at least one voice heard to speak the IPA it is
given. Those five get plain audio only. Swahili is the one large language among
them. Afrikaans, Bengali, Filipino, Icelandic, Latvian and Serbian are covered
by an older Google voice, Kannada and Malayalam by Chirp.

## Azure for languages with no Google voice (not yet heard)

After the language list grew to 328, Azure's catalogue was matched against it:
95 languages have an Azure voice, and 41 of those have no Google voice in the
app. `scripts/tts_research/azure_new_languages.py` ran the probe on one male
voice for each (38 distinct voices). By machine transcription only:

- Probably follows IPA (12): Assamese, Odia, Cantonese, Swiss Standard German,
  and the Arabic country voices for Egypt, Jordan, Iraq, Libya, Morocco,
  Tunisia, Saudi Arabia and Yemen.
- Probably reads the text (19): Albanian, Armenian, Azerbaijani, Bosnian,
  Irish, Javanese, Kazakh, Khmer, Macedonian, Maltese, Mongolian, Persian,
  Sinhala, Somali, Sundanese, Uzbek, Welsh, Wu Chinese, Zulu.
- Unclear (7): Algerian Arabic, Amharic, Georgian, Lao, Burmese, Nepali, Pashto.

Heard by the owner on `azure-new/index.html`: the machine guess held. All
twelve in the first group speak the IPA's word. Of the unclear seven, Algerian
Arabic follows and Amharic, Georgian, Lao, Burmese, Nepali and Pashto do not.
The nineteen guessed as reading the text do read the text. So Azure adds
thirteen steerable voices here and twenty-five that give plain audio only.
The three stress clips sounded the same to the owner in this batch, so these
voices follow the sounds and not the stress mark. Bytes cannot confirm that:
five identical Azure requests returned four or five different byte results
for every voice sampled, so no byte comparison is possible on Azure.

Caveats on the matching: Azure's Arabic voices are tagged by country and mostly
read Standard Arabic with a regional accent, so they are a loose fit for the
colloquial languages; `de-CH` is Swiss Standard German, not Alemannic; three
Yemeni varieties share one voice. The sheet is `azure-new/index.html`. The
remaining 233 languages have no voice from either provider.

## ElevenLabs

A free-plan key (`ELEVENLABS_API_KEY` in `.env`, restricted to text to speech,
voices, models and account read) was added on 2026-10-02. The model tested is
`eleven_v4` with the stock voice Eric; it takes IPA inline between slashes, so
the whole text of a clip is the IPA and there is no spelling to fall back on.
`scripts/tts_research/elevenlabs_sheet.py` produced two pages in `elevenlabs/`:

- `index.html`: a made-up word with the stress on each syllable and a second
  made-up word, in 18 languages, plus real homograph pairs (umfahren, record,
  read, замок, principi, público and publicó, fils). A machine transcription
  heard something close to the intended sounds in all 18, including Lithuanian,
  Estonian, Basque, Galician and Swahili, the five that neither Google nor
  Azure can steer.
- `blind.html`: twenty real German and English words as plain text, ElevenLabs
  beside Chirp 3 HD, unlabelled and in random order; the key is
  `blind-key.json`.

Heard by the owner: the stress lands where asked on the made-up word in all
languages, and the real pairs record, read, umfahren and principi are right,
record strikingly so. Three clips went wrong, and none of them failed loudly:

- French `/fis/` was read out as the characters, "slash, f, i, s, slash". An
  IPA string made only of plain letters is apparently not recognised as IPA.
- Spanish `/publiˈko/` came out garbled.
- Ukrainian `/zaˈmɔk/` had even stress on both syllables.

So ElevenLabs follows stress better than any other engine tested, but when it
fails it speaks nonsense or the markup itself, with a 200 response. Every clip
from it would need a check. Variations of the three are in `elevenlabs/retry.html`.

The retry showed the failures are systematic, not random, and avoidable:

- `/fis/` failed again the same way; `/ˈfis/` and `/ˈfil/` with a stress mark
  both work. An IPA string needs a non-letter symbol to be read as IPA, so a
  stress mark goes on every word, monosyllables included. The older
  `eleven_v3` reads `/fis/` correctly without it.
- `/publiˈko/` was wrong again the same way; `/pu.bli.ˈko/` with syllable dots
  works. Syllable boundaries are written out.
- `/zɑˈmɔːk/` with a long stressed vowel is clearly stressed where
  `/zaˈmɔk/` was not.

Rule for ElevenLabs input: always a stress mark, always syllable dots, and a
length mark on the stressed vowel where the stress is otherwise weak.

Side by side with Chirp 3 HD on twenty real German and English words, as plain
text, the owner preferred ElevenLabs on all twenty: more like a person, not
robotic. The test was not truly blind, since he recognised the voice after a
few words, but the preference was unanimous. I had expected the difference to
be hard to hear on single words; it is not.

ElevenLabs is not deterministic: the same request returned different bytes
twice, with and without a fixed seed. Its documentation says IPA results "can
still vary by voice and phrase". The whole run used 1,074 of the plan's 10,000
characters. Nothing here is verified by ear yet, stress least of all.

### Tricky real words from IPA alone (heard 2026-10-02): fails outside English

`scripts/tts_research/elevenlabs_tricky.py` spoke 73 real words in seven
languages from hand-written IPA only (stress mark and syllable dots on every
word, `language_code` sent and accepted), plus one plain-text sentence per
language. Output is `elevenlabs/tricky.html`. The owner's verdict:

| Language | Verdict |
|---|---|
| English | All right except `choir` `/ˈkwaɪ.ɚ/`, which ends in a clear t ("quiet"). |
| German | Sounds like an English speaker. `übersetzen` has the same stress in both readings, `modern` is MOdern in both, and /ç/ comes out as "sh". |
| French | `plus` `/ˈply/` is said as "play". |
| Spanish | `termino` `/teɾ.ˈmi.no/` is stressed on the first syllable, as the machine listener also wrote. |
| Ukrainian | Unusable: a foreign learner's accent throughout. `/ukr.za.lʲiz.ˈnɪ.tsʲa/` had "slash" read aloud. |
| Polish | Unusable in the same way; not close to real Polish (the owner speaks it). |

The plain-text sentences were good in every language, as I read the owner's
remark; that reading should be confirmed.

What this changes:

- The stock voice is an English one. Given plain text it takes on the
  language; given IPA alone it has no spelling to tell it which language it is
  in, and `language_code` does not make up for that. The IPA is read with
  English habits.
- The made-up-word test overstated ElevenLabs. Moving the stress on `pigutan`
  showed only that stress marks are followed in an English-like word. It did
  not show the sounds of each language, and real stress pairs in German and
  Spanish failed here.
- The input rules (stress mark, dots) do not prevent markup being read aloud:
  it happened again on a long word that followed both.
- The machine listener earned its place: it flagged `choir`, `termino`,
  `plus`, Chemie and the Polish words, all confirmed by ear. It missed the
  German stress pairs, as expected, since it cannot judge stress.
- The IPA was hand-written, so single wrong clips could be the transcription.
  The pattern across German, Ukrainian and Polish is too broad for that.

That conclusion held only for the English voice. The run used 1,178 characters.

### Native voices (heard 2026-10-02): the earlier failure was the voice

Using Eric for every language was my mistake. Library voices need a paid plan
over the API (the free plan answers 402); the account is now on Starter. The
same words were rerun with a voice native to each language: Otto (de), Nicolas
(fr), David Martin (es-ES), MarcoTrox (it), Evgeniy Shevchenko (uk), Adam
(pl). Output is `elevenlabs-native/tricky.html`. The owner's verdict:

- English and German from IPA: perfect.
- The major European languages overall: very good.
- Italian `ancora` came out anCOra for both IPA strings.
- Ukrainian `замок` came out ЗАмок for both IPA strings, and the vowel written
  /ɪ/ was said as Russian "и". Parts of it sounded Russian.

`scripts/tts_research/elevenlabs_variants.py` then sent the doubtful words
several ways side by side (`elevenlabs-native/variants.html`):

| Word | Bare spelling | Spelling with stress mark | IPA |
|---|---|---|---|
| риба | good | bad: `ри́ба` is said "рііба" | /ɪ/ good, /ɨ/ good |
| мити | good | not named as good | /ɨ/ good, /ɪ/ not |
| криниця | best | fine | /ɨ/ good, /ɪ/ bad |
| паляниця | best | bad | /ɨ/ passable, the soft /lʲ/ slightly off |
| Укрзалізниця | fine | fine | not named as good |
| замок, заМОК | | `замо́к` works | failed earlier |
| мука, муКА | | `мука́` works | |
| дорога, доРОга | bad: Russian г | `доро́га` fixes it | |
| дорога, дороГА | | `дорога́`: Russian pronunciation | |
| ancora, ANcora | | `àncora` is the only one that works | three IPA variants all fail |
| principi, PRINcipi | | `prìncipi` works | says "printipi" |
| principi, prinCIpi | | `princìpi` works | works |

What this establishes for ElevenLabs with native voices:

- The best input differs by language. English and German take IPA well.
  Ukrainian and Italian are better from the spelling, with the stress written
  as a mark in the text (acute U+0301 for Ukrainian, a grave or acute accent on
  the vowel for Italian). There the IPA stress mark loses to the word's more
  common reading and the sounds come out less native.
- For Ukrainian IPA, /ɨ/ is closer to "и" than the standard /ɪ/, which the
  engine reads as a plain i. Bare spelling is still better than either.
- A stress mark on Ukrainian "и" spoils the vowel: it comes out as "і". The
  stress mark works on other vowels. For a stressed "и" the IPA is, oddly,
  the better input. The owner suspects the same Russian influence.
- The voice can slip into Russian on a word the two languages share: bare
  `дорога` and `дорога́` both did, with `language_code` set to `uk`. The stress
  mark fixed one and not the other.
- That slip belongs to the voice. Ten words shared with Russian, each with a
  stress mark, on three Ukrainian voices (`elevenlabs-native/uk-voices.html`,
  `scripts/tts_research/elevenlabs_uk_voices.py`): Alex Nekrasov was the clear
  winner with nine of ten right, and Evgeniy Shevchenko, used until then,
  sounds Russian. Alex Nekrasov is the Ukrainian voice from here on. A voice
  being listed as Ukrainian says nothing about how Ukrainian it sounds.
- The tenth word, `кни́га`, came out "кнііга" on all three voices. So the
  stress mark on "и" breaks the vowel in the model, not in one voice.
- The comparison rerun with Alex Nekrasov (`elevenlabs-alex/variants.html`):

  | Word | Bare | Stress mark | IPA /ɪ/ | IPA /ɨ/ |
  |---|---|---|---|---|
  | риба | good | good | good | good (`/ˈre.ba/` says "реба") |
  | син | good | | good but sounds English | good |
  | мити | not named | bad: и becomes і | not named | very good |
  | криниця | not named | not named | bad | not named |
  | паляниця | good | good | not named | not named |
  | книга | good | bad: и becomes і | good | good |
  | Укрзалізниця | not named | not named | bad | |
  | замок, заМОК | | `замо́к` works | | |

  Bare spelling was never wrong. /ɨ/ was never wrong; /ɪ/ is unreliable. The
  stress mark on "и" breaks the vowel in some words (книга, мити) and not in
  others (риба, паляниця). Long words from IPA remain a risk. The мука and
  дорога rows were not reported.
- Proposed Ukrainian input for ElevenLabs, not yet tested as a whole: the
  spelling with a stress mark, except when the stressed vowel is "и", where
  the word goes as IPA with /ɨ/.
- So "send the IPA we show" does not hold for every route. Where the spelling
  plus a stress mark is the input, the sounds come from the engine's own
  reading of the spelling, and the IPA we display has to agree with that
  rather than drive it. A route's control mode is then one of: IPA, spelling
  with stress mark, bare spelling.

Not yet heard with native voices: French, Spanish and Polish word by word
(covered only by "very good" overall), and whether a stress mark in the
spelling also beats IPA for Spanish. The three runs used 919 and 409 characters.

## Checking audio by machine

Tried against clips the owner had labelled by ear. None replaces a listener for
stress.

| Method | Score | Verdict |
|---|---|---|
| Audio-capable model as listener (Gemini 2.5 Pro) | 11 of 19 | Answers from what it knows about the word; in a sentence it answers by meaning. Useful only for which word was said |
| Find syllable peaks, pick the stronger | 1 of 3 | Mis-segments |
| Relative loudness position between the two clips of a pair | 8 of 9 calibration, 6 of 9 against the ear | A hint at best |
| Forced alignment (torchaudio MMS_FA) plus length, loudness, pitch per syllable | 34 of 46 absolute, 16 of 19 pairwise | Not reliable enough to gate |
| Aligner confidence as a garble detector | no separation | Finds the expected word inside any audio |
| IPA-against-IPA byte test | 31 of 31 older voices | Reliable for "was the IPA used at all", on deterministic voices only |

What does hold: an engine either obeys a control for a language or it does not,
across every pair heard. So stress is verified once per route by ear. The owner
can hear stress position in languages he does not speak; sound quality still
needs a speaker.

## Text-side tools tried

- `espeak-ng` (through `espeakng-loader` and `phonemizer`): rule-based IPA with
  stress for 100+ languages. Returns one pronunciation per spelling and silently
  picks one for homographs (`record`, `замок`, and the rarer word for French
  `fils`). Writes the stress mark before the vowel, not before the syllable, so
  comparison needs normalizing.
- CMUdict (`cmudict` package): lists both pronunciations with stress digits for
  `record`, `read`, `present`, `object`. A witness for English homographs.
- `ukrainian-word-stress` (MIT, 2.9M forms): flags ambiguous words; not run here.

## Reproducing

Everything runs from the backend repository root against the `almonium-dev`
Google project, using `GOOGLE_SERVICE_ACCOUNT_KEY_BASE64` from `.env`.

Cloud setup done on 2026-10-01 and 2026-10-02, by the owner:

- `texttospeech.googleapis.com` enabled.
- `aiplatform.googleapis.com` enabled and `roles/aiplatform.user` granted to the
  service account. Only Gemini TTS and the audio-model listener need this; both
  were rejected, so the role can be removed.

Environment:

```bash
python3 -m venv temp/tts-audition-venv
temp/tts-audition-venv/bin/pip install -r scripts/tts-audition-requirements.txt
# only for the alignment experiments:
temp/tts-audition-venv/bin/pip install --index-url https://download.pytorch.org/whl/cpu torch==2.5.1 torchaudio==2.5.1
temp/tts-audition-venv/bin/pip install praat-parselmouth numpy espeakng-loader phonemizer cmudict
```

Scripts, all in `scripts/tts_research/`, each taking an output folder:

| Script | What it produces |
|---|---|
| `chirp_sheet.py` | Listening sheet for every enabled Chirp voice: three stress clips, a probe, plain text; picks symbols per locale and falls back until Google accepts |
| `older_voices_sheet.py` | The same sheet for WaveNet, Neural2 and Standard voices through the phoneme tag |
| `byte_test.py` | Samples each older voice and compares hash sets |
| `byte_test2.py` | Retries six symbol sets per voice and tests stress with the set that works |
| `discover_inventory.py` | Recovers each older voice's accepted consonants, vowels, stress and length marks from bytes; writes `older-all/inventory.json` |
| `stress_eval.py` | Forced alignment and per-syllable measures against ear labels |

`scripts/tts_audition.py` is the earlier three-voice audition generator.

Clips and result files are outside the repository, in
`~/Downloads/almonium-voice-audition-2026-10-01/`: `ipa-tier1` (real homograph
pairs), `italian`, `chirp-all`, `older-all`, `older-round2`, `older-stress`,
`uk-wavenet`, `uk-wavenet-ipa`, `uk-male`, `uk-apostrophe`, `stress`,
`calibration`. Each has an `index.html`; `chirp-all/results.json`,
`older-all/results.json`, `older-all/byte-test.json` and
`older-all/byte-test-2.json` hold the machine-readable results.

## Limits

- One listener. Stress is judged reliably; vowel and consonant quality in
  languages the owner does not speak is not.
- Most languages were tested on one made-up word. Only German, English, French,
  Spanish, Italian and Ukrainian were tested on real homograph pairs.
- Chirp custom pronunciations are a preview feature, and the older voices'
  support for unlisted languages is undocumented. Generated clips must be stored
  and kept, never regenerated on the fly.
- One provider. Azure, Amazon Polly and others are untested.

## Next

1. Wire the decision: add a control mode per route in `tts-voices.json`, switch
   uk-UA to WaveNet, add the per-variety IPA normalizers.
2. Hand-build IPA for en-IN, mr, te, ur; test pinyin for zh-CN and kana for ja-JP.
3. Run the same sheet against a second provider for the 22 varieties Google
   cannot steer. Azure publishes IPA phone sets for 39 locales and rejects
   unknown phones with HTTP 400.
4. Widen each verified language from one made-up word to a set of real
   homograph pairs before audio ships.
