# The lexicon

One page on what the dictionary is, what its parts are called, where they live, what is decided and
what is still to do. Written on 17 November 2026, when the October and November lines of work were
merged into `develop`. Measurements live in the files named under "Numbers"; this page points at
them and does not repeat them.

## What it is

A learner's dictionary the product owns: one entry per headword, written by a model once and served
to every reader. Entries are written for the languages a learner studies: **en, de, fr, es, it, pt**.
Ukrainian is a reader's language only: glosses may be written in it, entries are not.

An entry is everything one spelling stands for. It holds lexemes (one word with one paradigm: a
different sound, part of speech, gender or inflection is a different lexeme), and each lexeme holds
senses.

## Names

Three shapes exist. Their numbers are the schema versions, and a prompt writes exactly one shape.

| Schema | Written by prompts | What it is | Where |
|---|---|---|---|
| 2 | v1, v2, v3 | The first dictionary: glosses and review cues inside every sense, one register label | 49,000 entries in `out/cc-claude-opus-5-5/` |
| 3 | v4oct, v5, v6 | October: schema 2 plus regions by country or UN area with an extent, domains, pronunciation notes and, from v5, the Wiktionary senses each sense was written from (`from`) | `entry.schema.json`; the backend reads this one today; about 730 drafts under `out/grounded/` |
| 4 | v4, v4.1 | November: the core only, no glosses or cues; labels on separate axes, frames, spellings by variety, multiword and contraction lexemes, typed relations | `entry.schema.v4.json`; 31,000 entries in `out/*-v4/` |

Two traps, both from the weeks the lines were apart:

- Entries written from 11 to 17 November say `"schema_version": "3"` in their meta and mean schema 4.
  Nothing on disk was changed; `prompting.schema_of_meta` reads them right.
- `prompts/entry_writer/v4.md` is November's prompt. October's prompt of that name is `v4oct.md`.

A writer directory ending in `-v4` holds schema 4 whatever prompt wrote it; the prompt and the hash
of its exact text are in each entry's `.meta.json`. A prompt change never starts a new directory.

## Where everything is

- Code: `scripts/lexicon_research/` on `develop`. A push to `develop` deploys the backend.
- Entries: the private repository `almonium-lexicon-data`, whose work tree is `out/` itself
  (`lexicon_data_sync.py`, every three hours). The written text is paid for and never edited.
- Writers are started and paused from the book processor's Operations page, Entries tab.
- Backend: `learning/lexicon`. It reads schema 3 and refuses schema 4 (it requires glosses, cues and
  a register on every sense), so the 31,000 entries cannot be shown yet.

## How a word becomes an entry

Today, for schema 4:

1. **List.** `headwords/<name>-<lang>.json`, most frequent first.
2. **Gate** (`lemma_gate.py`, free). Wiktionary's verdict on the spelling, from an index built once
   by `lemma_index.py`: an inflected form or a bare abbreviation is not written.
3. **Write** (`claude_code_writer.py`, Opus at high effort, prompt v4.1). A word with a schema-2
   entry is mended instead (`mend_v4.py`): the old entry is converted by code and the model returns
   only a patch.
4. **Audit** (`check_v4.py`, free). Every fault a program can see, sorted into what is fixed for
   free, what needs a paid patch of one field, and what a rule must settle.
5. **Ingest** (`ingest_v4.py`, free, rerunnable). Ids for lexemes and senses that survive a rewrite,
   leaked lexemes dropped, a word written under two spellings kept once with an alias.
6. **Publish.** Not possible yet, see "Pending".

October's line built the other half, for schema 3 (`entry_pipeline.py`): Wiktionary evidence in the
request, each sense citing the Wiktionary senses it covers, then repair, checks, facts filled and
settled against the evidence by code, and a verdict (ready, disputed, held). It also has the tools
to judge writers: a calibration set with known facts and a blind-pair judge.

## One shape: what schema 4 takes from each line

Decided 17 November 2026: schema 4 stays, and takes the best of both.

| From November (already in schema 4) | From October (to add) |
|---|---|
| No glosses or cues in the core; they are passes of their own | Regions by country or UN area with an extent (only, mainly), one way to say where anything is used |
| Labels on separate axes: formality, attitude, time, style, normativity, domain | `from`: the Wiktionary senses a sense covers |
| Frames per sense | Pronunciation notes (less common, older, newer) |
| Spellings by variety on one lexeme, so *color* and *colour* are one word | Facts checked and filled against Wiktionary by code |
| Multiword and contraction lexemes with their parts | A sense key kept across rewrites in the backend, every published version archived |
| Typed relations, antonyms per sense | Homophones computed by the backend |

All of it is reached by code or by small passes. Nothing written is written again.

## The pass over finished entries

One pass brings the written entries up to October's standard and links them:

1. **Cite** (paid, small): for each sense, the Wiktionary senses it covers. A few ids per sense.
2. **From the citations, by code**: regions with their extent; gender, plural, forms and IPA
   compared with Wiktionary, a patch call only where they disagree; a count of common Wiktionary
   senses the entry lacks, which is the audit of sense lists nobody has run.
3. **Inside a language**: reverse relations by code, an antonym resolved to the sense it opposes,
   synonyms from Wiktionary's per-sense lists when both words have entries.
4. **Across the six languages** (paid, small): Wiktionary glosses a German word with linked English
   words, so the candidates are free; a call picks the sense in the English entry. A translation is
   a link where the other entry exists and plain text where it does not.
5. **Reader-language glosses**: deferred. Old ones are kept under `out/carry/`.

## Decisions

- Opus at high effort writes. Low effort lost a blind comparison to the old entries 9:17; Haiku
  lost to Opus 5:19 and wrote English definitions into other languages; Fable was level with Opus
  at more than twice the price.
- Words to write: the words of the catalogue's books plus the most frequent of each language, not a
  million up front. English goes down to Zipf 1.5 (about 65,000 words).
- An old or dated word is an entry with a time label; an old spelling is an alias.
- Glosses are not bought for now. The sheet has the definition in the word's own language. A reader
  who cannot read that yet needs a translation line; today the lookup asks a translation provider
  only when the lexicon has nothing, so showing it beside a lexicon entry is part of the backend work.
- Work happens on `develop` and is pushed at once.

## Sources

| Source | Licence | Used for |
|---|---|---|
| Wiktionary through kaikki.org | CC BY-SA | The gate's index; evidence and fact checks in October's writer; the citing pass. Facts are used, wording is never copied |
| Wikidata lexemes | CC0 | Not yet: spellings and forms where it has them |
| Princeton WordNet | Its own permissive licence | Not yet: synonyms, antonyms and concept ids for English, as a witness. Much finer than a learner needs: 18 senses of *bank* |
| Open Multilingual Wordnet | Varies by language | Not yet: sense links across languages through shared concept ids |
| wordfreq | Data CC BY-SA | Ranking the lists |
| CMUdict | Permissive | English stress, as a witness |
| The catalogue's own public-domain books | Free | Examples and word demand, not yet |

Whether reusing Wiktionary's facts in bulk carries its share-alike terms is a legal question this
page does not settle.

## Pending, in order

1. **The backend reads schema 4**, so entries can be published and seen. The largest piece.
2. **The citing pass**, and the evidence block in the writer for new words (v4.1 rules with v6's
   evidence and inflected-form rules).
3. **Judge v4.1** on the calibration set and in blind pairs. The tools exist; v4 was never judged.
4. **Lists**: function words for de, fr, es, it, pt (English has them); the book-word list, which
   needs an export from the book processor; phrases.
5. **IPA symbol lists**: about a tenth of English and German entries use a sound the lists lack
   (the last vowel of *fully*, German short unstressed vowels). Extend the lists or map by rule.
6. **Words that are both a form and a lemma** (*signed*, *railings*): a fifth of the deep English
   list ended as paid rejects. Sort them in batches before writing.
7. **One set of checks**: `entry_checks.py` and `entry_repair.py` (schema 3) and `check_v4.py` and
   `ingest_v4.py` (schema 4) overlap.
8. **One owner of sense identity**: the backend keeps the key; it should take the id an ingested
   entry already carries.
9. The lookup normaliser (apostrophes, soft hyphens); 95 entries that fail the schema.

## Numbers

- `docs/LEXICON_PLAN_USAGE.md`: what the plans buy, cost per entry by model, the mend pilot.
- `scripts/lexicon_research/grounded_pipeline_results.txt`: October's evidence pipeline, its blind
  comparisons and Wiktionary's coverage of our headwords.
- `scripts/lexicon_research/review_pilot_results.txt`: the whole-entry review pilot.
- Audit of 17 November 2026 (`check_v4.py` over the data repository): 31,068 schema-4 entries in the
  six languages, $825 at list price; 95 (0.3%) need a paid patch, 248 are put right by ingest for
  free, 2,094 have IPA to settle. Senses per lexeme: en 1.63, de 1.89, fr 1.88, es 1.89, it 2.01,
  pt 2.04. English rejects: about 3,500 of 16,760 calls, about $46.
