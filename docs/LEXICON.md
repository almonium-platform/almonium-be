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
| 4 | v4, v4.1, v4.2, v4.3, v4.4, v4.5, v4.6 | November: the core only, no glosses or cues; labels on separate axes, frames, spellings by variety, multiword and contraction lexemes, typed relations | `entry.schema.v4.json`; 31,000 entries in `out/*-v4/` |

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
- A cloud session writes entries too, with the same writer, into its own clone. The owner's
  machine pushes `main` without pulling, so a cloud session never pushes there: it pushes a branch
  `claude/*`, and every run of the sync merges those branches before it pushes. A word written on
  both sides keeps the machine's entry.
- Writers are started and paused from the book processor's Operations page, Entries tab.
- Backend: `learning/lexicon`. It reads schema 3 and refuses schema 4 (it requires glosses, cues and
  a register on every sense), so the 31,000 entries cannot be shown yet.

## How a word becomes an entry

Today, for schema 4:

1. **List.** `headwords/<name>-<lang>.json`, most frequent first.
2. **Gate** (`lemma_gate.py`, free). Wiktionary's verdict on the spelling, from an index built once
   by `lemma_index.py`: an inflected form or a bare abbreviation is not written.
3. **Write** (`claude_code_writer.py`, Opus at high effort, prompt v4.6). A word with a schema-2
   entry is mended instead (`mend_v4.py`): the old entry is converted by code and the model returns
   only a patch.
4. **Audit** (`check_v4.py`, free). Every fault a program can see, sorted into what is fixed for
   free, what needs a paid patch of one field, and what a rule must settle.
5. **Ingest** (`ingest_v4.py`, free, rerunnable). Ids for lexemes and senses that survive a rewrite,
   leaked lexemes dropped, a word written under two spellings kept once with an alias, a headword
   rejected as an inflected form or a spelling pointed at the entry that holds its word, the
   sounds put in the form the dictionary prints (a syllable dot before every stress mark inside a
   word, an Italian double consonant written twice, the *ll* of Spain as /ʝ/), and *être* or
   *essere* given to every French or Italian pronominal sense.
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
2. A model of another family reviews them strictly (GPT did all six languages). Its answer is kept
   as it came in `gold/answers/<lang>.md`.
3. `gold_candidates.py --take gold/answers/<lang>.md --lang <lang> --pick a,b,c,d,e` takes the
   corrected entries named (or the candidate, where the verdict was "use as it is"), puts in the
   syllable dots by ingest's rule, and stops on any that fails the schema or a check. The result
   is `gold/<lang>.json`. Where a taken entry breaks a rule the prompt got after the review, the
   correction is ours and stands apart from the answer, in `gold/own/<lang>.json` (two so far:
   notes the French and the Italian reviewer wrote in their languages).
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

## Prompt v4.5

French has its examples since v4.5: *hôpital* (noun with an irregular plural), *continuer* (verb with
*à* and *de*), *normal* (adjective, a frame with the subjunctive), *historique* (adjective and
noun), *couple* (masculine and feminine lexemes, a familiar sense mainly of Canada). Of twenty
candidates: three passed, twelve corrected, five refused.

Half of what the French review found was v4.4's subject already (a definition narrower than the
word: an *avion* carries passengers, a *port* is on the coast, a *couple* is married). What is new:

| Found | Rule in v4.5, or fix |
|---|---|
| *s'expliquer* written under a verb that "takes avoir": three in five French pronominal senses lacked *être* | A French or Italian pronominal sense says `auxiliary: be`. For what is written, ingest sets it by rule, free: 73 of 119 French and 16 of 78 Italian pronominal senses |
| *permettre* derived from *mettre*, *chanson* from *chanter*; *port* derived from *porter* in all its senses, the harbour too | `derived_from` is for a word built inside the language, not for a common ancestor; a relation may list the `senses` it holds for (new in the schema) |
| *ben*, *cool*, *voilà* defined each in its own way ("mot de l'oral qui...", "exprime...") | A word with no meaning to paraphrase is defined by what it is used for, opening as the language's learner's dictionaries do ("used to ...", "sert à ...") |
| "Cool ! On part demain." as one example | An interjection stands inside its sentence, after a comma |
| *normal* marked predicative in a sense that also stands before a noun; *couple* marked singular only | Position and number only for what is never used the other way |
| `standard_alternative` on a sense with no normative label | Only with nonstandard or proscribed |

Tried on twenty French words against v4.4, and on twenty-two words of the other five languages:
every entry valid. French senses went from 64 to 74 and frames from 61 to 105, no pronominal
sense lacked its auxiliary (two did), and the price moved from $0.043 to $0.045 an entry.
Definitions outside six to fifteen words went from one to four, which the audit counts.

## Prompt v4.6: one prompt, every language with its examples

There is one prompt. Its version is the same for all six languages, and a writer of any language
gets the same rules. What differs by language is only what has to: the five or six example entries,
which are in the language; the list of its sounds; and a line or two of its own grammar (the German
plural classes, the French *h aspiré*, which auxiliary a pronominal verb takes). The versions v4.3
to v4.6 came one after another only because the six reviews did; from v4.6 every language sees its
own examples and no review is outstanding.

Italian and Portuguese have their examples since v4.6:

- Italian: *museo*, *iniziare* (avere in one sense, essere in another), *recente*, *minimo*
  (adjective and noun), *fenomeno* (an informal figurative sense). Of eighteen candidates: two
  passed, thirteen corrected, three refused.
- Portuguese: *computador*, *escolher*, *global*, *policial* (adjective and two nouns, one mainly
  of Brazil), *ciência* (a formal sense, spellings of before 1911). Of twenty: two passed, fourteen
  corrected, four refused.

These two reviews found little that was new, which is the sign the rules are settling:

| Found | Rule in v4.6, or fix |
|---|---|
| Italian double consonants written three ways (/ˈfatːo/, /ˈfatto/, /ˈfatː.o/), because a long mark left no place for the syllable dot | Written twice with the boundary between, /ˈfat.to/, /ˈpit.tsa/. Ingest rewrites what is written by rule, free: 706 of 3,400 Italian transcriptions |
| "essere when intransitive" is wrong for *ho iniziato a lavorare* | A verb with both auxiliaries says `both`, and each sense gives its own |
| *lidar com* called transitive in Portuguese, *schaden* + dative intransitive in German | Transitive means a direct object, in every language; the frame shows the preposition or case |
| Three reviewers joined persons as the prompt asks (*mantenga*: 1,2,3) and the schema refused it | A schema bug: Person could not be joined, every other feature could. Fixed |
| *francês* defined without the word *França* ("of the country whose capital is Paris") | The country's name may stand in the definition of its people and language |
| A national stereotype in an example | No stereotype of a people or a group |
| es-ES wrote *ll* as /ʎ/, the old norm; nearly all of Spain says /ʝ/, as Mexico does | es-ES writes /ʝ/. Ingest rewrites the 47 readings written the old way |

Tried on twenty Italian and twenty Portuguese words against v4.5, and thirteen words of the other
four languages: every entry valid; no Italian double written the old way (nine were); frames went
from 55 to 73 in Italian and from 48 to 61 in Portuguese; the price stayed at $0.043 an entry in
Italian and went from $0.046 to $0.051 in Portuguese.

## What the six reviews say about the entries

118 entries written under v4, all faultless to the audit and all of plain common words, were read
strictly: 13 passed, 80 were corrected, 25 refused as examples. Sorted by their main fault, as a
rough reading of the reviewer's reasons:

- **The hard facts held.** No wrong gender, no wrong plural and no wrong part of speech in 118
  entries; one wrong verb form (*muerto* given as a participle of *matar*); two transcriptions
  corrected in substance, both Portuguese, the other sound corrections being notation (the dot,
  the Italian doubles); and the auxiliary incomplete in several French and Italian verbs, which
  v4.5 and ingest now settle.
- About 30 made a claim that is too strong: a sense said to be used "only" in one country, an
  adjective said to stand only before a noun, a noun said to have no plural, a verb said to be only
  transitive. v4.4 to v4.6 each have a rule against these.
- About 45 lacked a sense or a part of speech the reviewer would number. The reviewer says itself
  that no two dictionaries number alike, and a good share of these are rare (*hotel* as a villa,
  the idle speed of an engine).
- The rest were wording: a definition narrower than the word.

The shape took all of it with five optional fields added and nothing changed: every entry written
before is still valid. What the reviews moved was the wording of rules, and what a rule could
settle was applied to the written entries by ingest at no cost.

These numbers are of v4's entries. Nothing yet measures v4.6 on a sample drawn at random: that is
the next step under "Pending", item 3.

## Measuring the entries

A review that corrects entries always finds something to correct, so it cannot say when the entries
are good enough. `measure.py` asks a narrower question: for each entry of a sample drawn at random,
which of six kinds of fault does it have? A wrong fact, a claim stated too strongly, a missing
core sense, a missing minor sense, a sense split in two, wording. The reviewer does not correct and
does not rank; each finding says whether a learner would be misled. The answer is a rate, and the
same question can be asked after any change.

What is aimed at, written down before the first count: of a hundred entries, at most two with a
wrong fact, five with a claim too strong, ten lacking a core sense.

The sample `v46`, a hundred entries a language, mixed so the reviewer cannot tell the parts apart:

- sixty that the dictionary already holds, as ingest would publish them, across the frequencies
  above Zipf 3.5: what a reader meets most. Nearly all were written under v4 to v4.2.
- forty words that had no entry, written under v4.6 for this and kept as entries. They are rarer
  words (Zipf 2.7 to 3.4), because every commoner word is either written or waits for the mender.
  All 240 are valid, $8.42 at list price; of their 464 senses the audit finds nine definitions
  and one example out of length, and nothing else.

So the two parts answer two questions, how good is what there is and how good is what v4.6 writes,
and are not a comparison of like with like: the new words are rarer and have fewer senses.

The files for the reviewer are `measure/v46/review/<lang>-1.md`. Its answers go, as they come, to
`measure/v46/answers/<lang>-1.md`, and `measure.py --name v46 --tally` counts them.

The mender's entries, some 40,000 older ones still to be mended, are in neither part: they do not
exist yet. They are the third thing to measure, with the same files, once a batch is mended.

## Russian, on trial

Russian has its settings (`prompting.PILOT_LANGUAGES`): it can be written by naming it, no list is
written for it by default, and nothing is published. Almost all of it was there from Ukrainian: a
stress reading on every pronunciation and form, the cases, the aspect partner as a relation.
Russian's own:

- The lemma is written with ё where the word has it, and the spelling with е is listed beside it,
  because most print has е (*ребёнок*, *ребенок*). Spellings of before 1918 are not listed: a rule
  turns them into today's.
- The transcription shows what the spelling hides: reduced vowels (*молоко* /mə.lɐ.ˈko/), soft
  consonants, devoicing (*хлеб* /ˈxlʲep/), letters not said or said as others (*солнце*, *что*,
  *его*).
- A verb in -ся is a lemma of its own; a word used only as a predicate (*нельзя*, *рад*) says so.
- A definition may have four words, Russian having no articles.

A pilot of fifty words chosen to be hard, under v4.6 with no examples: all valid, no sound outside
the list, and all 212 stress readings agree with Wiktionary. *Замок* and *мука* came back as two
lexemes each by stress; *человек* with *люди* and with *человек* after numbers; *хлеб* with *хле́бы*
for loaves and *хлеба́* for crops; *лес* with *в лесу́*; *ложить* proscribed, with *класть*;
*звонить* with a note on the stress people get wrong. $2.67, five cents an entry.

Next for Russian, the same path as the six: a few hundred common words written, twenty candidates
picked from them, a review, examples, then its place among the target languages.

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

Japanese, Korean and Chinese are the three with the most learners outside the six, and the book
processor can lemmatize all three. What stands between them and a pilot is that three things the
writers share assume an alphabet written with spaces:

1. The pronunciation section of the prompt asks for IPA. These need a reading instead: pinyin with
   tone marks, kana, a romanization. The schema has it; the prompt has one paragraph to exchange.
2. The limits on a definition and an example count words. Without spaces they must count
   characters, in the prompt and in the audit.
3. The gate asks Wiktionary whether a spelling is an inflected form. Chinese does not inflect;
   Japanese and Korean do, but a reader taps a run of characters that the book processor, not the
   gate, must first cut into words.

And each has its own decisions: which script is the headword and which a spelling (simplified and
traditional; kanji and kana), the classes of verbs and adjectives, counters and measure words,
levels of politeness. A pilot of forty words a language, as for Russian, is the way to find what
else there is.

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
- Prompt v4.6 is the writers' prompt since 17 November 2026; `LEXICON_PROMPT_VERSION=v4.5` goes back.
- es-ES writes *ll* as /ʝ/, as nearly all of Spain says it; the separate /ʎ/ is not marked.
- Glosses are not bought for now. The sheet has the definition in the word's own language. A reader
  who cannot read that yet needs a translation line; today the lookup asks a translation provider
  only when the lexicon has nothing, so showing it beside a lexicon entry is part of the backend work.
- The audit accepts the weak vowels of *happy* and *situation* in English and the short close
  vowels of unstressed German syllables (*marginal*, *voran*), which the writers use of their own
  accord and rightly (`"also"` in the inventory files). That took the entries with a sound outside
  the list from 1,766 to 39 in English and from 342 to 4 in German. The prompts up to v4.6 do not
  list them, because a prompt entries were written with does not change; the next version will.
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
3. **Read the measurement.** The sample `v46` is drawn and written and its six review files are
   ready; they wait for a reviewer's answers, then `measure.py --name v46 --tally`. After it, the
   same for a batch of mended entries.
4. **Lists**: function words for de, fr, es, it, pt (English has them); the book-word list, which
   needs an export from the book processor; phrases.
5. **Russian examples**, by the path above; then a pilot of Japanese, the first language without
   an alphabet (see "Languages outside Europe").
6. **One set of checks**: `entry_checks.py` and `entry_repair.py` (schema 3) and `check_v4.py` and
   `ingest_v4.py` (schema 4) overlap.
7. **One owner of sense identity**: the backend keeps the key; it should take the id an ingested
   entry already carries.
8. The lookup normaliser (apostrophes, soft hyphens); 95 entries that fail the schema, every one
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
