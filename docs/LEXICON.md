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
| 4 | v4, v4.1, v4.2, v4.3, v4.4 | November: the core only, no glosses or cues; labels on separate axes, frames, spellings by variety, multiword and contraction lexemes, typed relations | `entry.schema.v4.json`; 31,000 entries in `out/*-v4/` |

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
3. **Write** (`claude_code_writer.py`, Opus at high effort, prompt v4.4). A word with a schema-2
   entry is mended instead (`mend_v4.py`): the old entry is converted by code and the model returns
   only a patch.
4. **Audit** (`check_v4.py`, free). Every fault a program can see, sorted into what is fixed for
   free, what needs a paid patch of one field, and what a rule must settle.
5. **Ingest** (`ingest_v4.py`, free, rerunnable). Ids for lexemes and senses that survive a rewrite,
   leaked lexemes dropped, a word written under two spellings kept once with an alias, a headword
   rejected as an inflected form or a spelling pointed at the entry that holds its word, and the
   syllable dot put before every stress mark inside a word.
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

Since 17 November the schema holds October's regions, pronunciation notes and `from`, and prompt v4.2
writes the first two; `from` waits for the citing pass.

## Prompt v4.2

v4.1 plus five rules, tried on 36 trap words (all valid, each behaving as described):

- **A headword that is also an inflected form** returns only what it is the lemma of, October's v6
  rule: *saw* gives the tool and to saw, *left* the adjective, adverb and noun; *glasses* and
  *Eltern* are plural lexemes; *arms* and *esposas* are rejected naming the lemma whose entry holds
  the plural sense; *interested* is an adjective, *signed* a reject naming *sign*.
- **Regions** as October wrote them: a country or UN area with an extent (*boot* of a car: en-GB and
  en-AU only; to *table*: mainly en-GB in one sense, mainly en-US in the opposite one).
- **Pronunciation notes** on a second reading (less common, older, regional).
- **Parts of a compound written as one word**: *Arbeitszimmer* is Arbeits (Arbeit) + zimmer (Zimmer),
  *toothbrush* is tooth + brush. Phrases and contractions already listed theirs.
- **Politeness**: familiar (du, tu, tú) and polite (Sie, vous, usted, Lei).

Entries written under v4 and v4.1 lack the compound parts and the politeness label; both are small
passes over the words concerned, not rewrites.

## Prompt v4.3 and the example entries

A writer is shown reviewed entries of its own language, the one change the literature measured as
raising rule-keeping (78% to 95%). They are our own entries, not a published dictionary's: those
belong to their publishers and do not have our shape.

How a language gets them:

1. `gold_candidates.py` picks twenty faultless entries of plain common words into
   `gold/candidates/<lang>.json` and writes `gold/review/<lang>.md` for a reviewer.
2. A model of another family reviews them strictly (GPT did English, German and Spanish). Its answer
   is kept as it came in `gold/answers/<lang>.md`.
3. `gold_candidates.py --take gold/answers/<lang>.md --lang <lang> --pick a,b,c,d,e` takes the
   corrected entries named (or the candidate, where the verdict was "use as it is"), puts in the
   syllable dots by ingest's rule, and stops on any that fails the schema or a check. The result
   is `gold/<lang>.json`.
4. The language is added to `prompting.GOLD_SINCE` with the prompt version that first shows them.

A prompt version renders the same text for a language for ever, because its hash is in every entry
written with it (`tests/test_gold.py` pins them). So a language whose examples arrive later gets
them in the next version, and a gold file in use is not edited.

English has its five since v4.3: *lake* (plain noun), *afford* (verb with frames), *aware*
(adjective used after a verb), *institute* (two parts of speech, a formal sense), *hall* (a regional
sense). Of the twenty candidates the reviewer passed one as it was, corrected sixteen and refused
three. What it found in most of them became rules of v4.3:

- **The syllable dot before a stress mark** (/ɪn.ˈstɪ.tuːt/, not /ɪnˈstɪ.tuːt/). A quarter of the
  transcriptions written so far lack it (12,971 of 52,398). Ingest now adds it by rule, free, and
  the audit counts it.
- **Grammar that holds for one sense only** goes on the sense, with a note where a code does not
  say it. *Position: attributive* had been put on whole adjectives.
- **No list of instances in a definition** ("such as...").
- **A fixed phrase that is an entry of its own is not a frame.**
- **Antonyms only where the two are true opposites**: not *hill* and *valley*, *write* and *read*.

Tried against v4.2 on the same twenty English words: both wrote twenty valid entries; missing dots
went from 4 of 53 transcriptions to 0 of 57, the weak antonyms went, senses rose from 50 to 56
(the reviewer's commonest complaint was a missing sense), and the price rose 13%, from $0.0286 to
$0.0323 an entry, for the longer instructions and the extra senses.

## Prompt v4.4

German and Spanish have their examples since v4.4:

- German: *nötig* (adjective with frames), *Nachricht* (noun with a plural-only sense), *fällen*
  (verb with frames and a confusable), *perfekt* (adjective and noun, stressed differently),
  *Trainer* (a sense of Austria and Switzerland). Of twenty candidates: two passed, twelve
  corrected, six refused.
- Spanish: *lunes*, *estudiar*, *enorme*, *responsable* (adjective and noun of common gender),
  *genial* (adjective and adverb, informal), *café* (noun and adjective, a colour sense mainly of
  Latin America). Of twenty: three passed, thirteen corrected, four refused.

Both reviews also named places where the rules contradicted themselves or said too little. What
v4.4 changes, for every language:

| Found | Rule in v4.4 |
|---|---|
| The German plural classes had no umlaut alone (*Vater*, *Väter*) | `-¨` is listed; the writers already used it |
| "List only irregular forms" against "a German verb lists its preterite and participle" | The forms the language's rule names are listed even when regular; beyond them only what a learner could not build |
| A definition narrowed to the typical case: a *Richter* decides guilt, a *Nachricht* is short, *responsable* means at fault (the commonest fault in both languages) | A definition holds for every use of the sense and says no more than the word means |
| A remark on grammar or field inside a definition (*vor Adjektiven:*, *en boxeo*) | It is a frame, a note in the sense's grammar, or a domain label |
| A construction made into a sense (*recién* + participle and *recién* + verb) | A difference of construction is a frame; one used in part of the language is a grammar note |
| A serving of a drink as a sense; a meaning found in one idiom as a sense | Neither is a sense |
| An adverb hidden in a note on the adjective (*genial*) | Every part of speech in ordinary use has its lexeme |
| *sich verkaufen*, *caerse*, *matarse*: one "reflexive" for four different things | A reflexive use with its own meaning is a sense, with a note when the pronoun does not mean "oneself" |
| A region for where the thing exists (*Kreis*); "only" where "mainly" was true; two countries named for a use of all Latin America | A region says where the word is used; "only" only when sure; the area rather than a few of its countries |
| A domain on an everyday word (*Tor* in football) | Domain is for a specialist sense only |
| *período* and *periodo* are said differently and one sense belongs to one of them | A reading may name its `spelling`, a sense its `spellings` (new in the schema) |
| *electo* is used only in one construction | A form may carry a `note` (new in the schema) |
| A form that is two cells at once, joined so that it named cells that do not exist | Values of one feature are joined; otherwise the form is listed twice |
| *sólo* called pre-reform; *fué* dated to 2010 | *sólo* and *éste* are spellings still allowed; *fué* went in 1959 |
| "Phonemic IPA" for what is a dictionary's broad transcription with set conventions | Called that |
| *das Sterben* written beside *sterben* | A German infinitive used as a noun is a lexeme only with a meaning of its own (*das Essen*) |
| Notes in two languages | Notes are in English, as "why" and "reject" are |

Tried on twenty words each of Spanish, German and English against v4.3, and fifteen more on the
final text: every entry valid, no transcription without its dot.

| | Spanish v4.3 | v4.4 | German v4.3 | v4.4 | English v4.3 | v4.4 |
|---|---|---|---|---|---|---|
| Senses | 75 | 82 | 64 | 58 | 56 | 53 |
| Frames | 49 | 94 | 51 | 81 | 63 | 59 |
| Domain labels | 3 | 2 | 5 | 1 | 0 | 0 |
| Price an entry, prompt cached | $0.045 | $0.052 | $0.044 | $0.045 | $0.032 | $0.031 |

The examples roughly double the frames in the two languages that had none to look at. Senses
moved both ways: Spanish gained the adverb of *directo* and the uses of *caer*, German lost the
serving of *Bier*, the noun *Sterben* and the region of *Kreis*, which the review asked for, and
also the figurative *Richter*, which it did not. Twenty words do not settle more than that.

Two things the Spanish review raised are left as they are: es-ES writes *ll* as /ʎ/, the
traditional model most printed dictionaries show, though most of Spain says /ʝ/ (a one-line rule
in ingest either way, when decided); and *Refl* stays one value, with the note saying what kind.

French, Italian and Portuguese write with v4.4's rules and no examples until their reviews are in;
their examples will be v4.5.

## Words that are both a form and a word

Wiktionary files 16,765 English spellings both as a form of another word and as a word of its own
(*signed*, *railings*, *encouraged*). The gate lets them through and the writer decides: of 4,419 it
has seen, it wrote 52% and rejected 48%. No rule predicts that well enough to skip the call: the best
one from Wiktionary's senses and WordNet's lemmas is right 83% of the time and would lose entries
such as *encouraged* and *shoplifting*. So the call stays, a reject costs about $0.013, and ingest
turns it into an alias of the lemma's entry, which is what the reader who taps the form needs.

A reject's answer is one line, about 35 tokens. Its price is the call: 42% is the model's thinking
before it answers (some 240 tokens), 31% is reading the cached instructions, twice, because the CLI
takes two turns when it is given a schema, and 28% is what the CLI writes to the cache on every
call. Dropping the schema flag was tried on 16 words: one turn, but the model thought more, the
price did not fall, and one entry in 16 broke the schema. The lists still to write hold about 3,500
more such rejects, some $47; not worth a batching step of their own.

## Languages outside Europe

Schema 4 can hold them; the writers cannot write them until a language has a profile (symbols, form
rules, examples) and the checks count characters where a language has no spaces.

- A spelling may name its script (`script`, ISO 15924): simplified and traditional Chinese, kanji
  and kana, Cyrillic and Latin Serbian. Wikidata and OntoLex tell scripts apart the same way, by the
  script part of the language tag (`zh-Hans`, `ja-Hira`, `sr-Latn`).
- A pronunciation is IPA or a `reading` or both; `notation` says what the reading is (pinyin, kana,
  a romanization, the vowels a script leaves out). Left out, it is the spelling with its stress marked.
- `grammar.classifier` for the measure word of a noun; every case Universal Dependencies names;
  `labels.politeness` also has honorific and humble.
- For Arabic and Hebrew: `grammar.root`, the consonants a word is built on; a `vocalized` reading for
  the vowels the script leaves out; the dual, the jussive and the construct state among the form
  features. For Swahili and its relatives: `grammar.noun_class`. Forms may carry `Polite`.

What the shape cannot do for such a language is find the word a reader tapped. The book processor
has no Arabic, Hebrew, Hindi or Persian in its registry and no lemmatizer for them; Russian, Polish,
Japanese, Korean and Chinese are there. The larger market is the other way round in any case: people
who speak those languages and learn English need the interface and the short translations in their
language, and a translation is plain text in any script.
- Still to add when the first such language comes: its level scale (HSK, JLPT) beside CEFR, and its
  verb classes.

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
- Prompt v4.4 is the writers' prompt since 17 November 2026; `LEXICON_PROMPT_VERSION=v4.3` goes back.
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
3. **Judge v4.4** on the calibration set and in blind pairs. The tools exist; v4 was never judged.
4. **Lists**: function words for de, fr, es, it, pt (English has them); the book-word list, which
   needs an export from the book processor; phrases.
5. **IPA symbol lists**: about a tenth of English and German entries use a sound the lists lack
   (the last vowel of *fully*, German short unstressed vowels). Extend the lists or map by rule.
6. **Example entries for fr, it, pt.** English, German and Spanish have theirs. The review files are
   written (`gold/review/<lang>.md`); each language needs its reviewer's answer, then `--take` and
   the next prompt version.
7. **One set of checks**: `entry_checks.py` and `entry_repair.py` (schema 3) and `check_v4.py` and
   `ingest_v4.py` (schema 4) overlap.
8. **One owner of sense identity**: the backend keeps the key; it should take the id an ingested
   entry already carries.
9. The lookup normaliser (apostrophes, soft hyphens); 95 entries that fail the schema, every one
   from the Codex writer.

## Numbers

- `docs/LEXICON_PLAN_USAGE.md`: what the plans buy, cost per entry by model, the mend pilot.
- `scripts/lexicon_research/grounded_pipeline_results.txt`: October's evidence pipeline, its blind
  comparisons and Wiktionary's coverage of our headwords.
- `scripts/lexicon_research/review_pilot_results.txt`: the whole-entry review pilot.
- Audit of 17 November 2026 (`check_v4.py` over the data repository): 31,068 schema-4 entries in the
  six languages, $825 at list price; 95 (0.3%) need a paid patch, 248 are put right by ingest for
  free, 2,094 have IPA to settle. Senses per lexeme: en 1.63, de 1.89, fr 1.88, es 1.89, it 2.01,
  pt 2.04. English rejects: about 3,500 of 16,760 calls, about $46.
