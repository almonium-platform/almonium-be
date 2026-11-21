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
| 3 | v4oct, v5, v6 | October: schema 2 plus regions by country or UN area with an extent, domains, pronunciation notes and, from v5, the Wiktionary senses each sense was written from (`from`) | `entry.schema.json`; the backend read this one until 18 November 2026; about 730 drafts under `out/grounded/` |
| 4 | v4, v4.1, v4.2, v4.3, v4.4, v4.5, v4.6, v4.7, v4.8, v4.9 | November: the core only, no glosses or cues; labels on separate axes, frames and collocations, spellings by variety, multiword and contraction lexemes, typed relations | `entry.schema.v4.json`; 31,000 entries in `out/*-v4/`; the backend reads this one, as ingest cleans it |

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
- The owner's machine is not the only writer: a cloud session writes with the same writer into its
  own clone, and another machine or account may. Every run of the sync first takes in what the
  data repository holds, its own branch and every branch `claude/*`, then pushes; a word written
  on both sides keeps the machine's entry. A cloud session pushes a branch `claude/*`, which is
  safe whatever version of the sync a machine runs.
- Writers are started and paused from the book processor's Operations page, Entries tab.
- `docs/LEXICON_TOUR.md`: the fewest real entries that between them show everything the shape can
  say (`tour.py` picks them), for checking by hand what the app shows a reader.
- Another machine has the code and the entries after a pull and a sync, and none of the tables
  the checks read: `wiktionary/` is built, not kept. `lemma_index.py --langs en,de,fr,es,it,pt,ru`
  builds the index of the English edition (the gate, the witness and the evidence block read it),
  and `open_sources.py --from FOLDER --fetch` downloads the other sources (1.6 GB) and builds
  theirs, the own editions' meanings among them. Writing without evidence needs only the first.
- The reader of every sample is GPT Astra 6.2 at extra-high effort in the ChatGPT app, one file a
  chat (the owner, 19 November 2026); the answer files do not say so themselves. The app searches
  the web as it reads: one file had it look at 169 sites, the Oxford, Cambridge and Collins
  learner's dictionaries first. So the reader that was measured reads with the dictionaries
  open, and every count in this document is that reader's. The review loop's reader seat for the
  Codex command line is to be given the same model and effort, and searches by default
  (`:search`, "The review loop").
- Backend: `learning/lexicon`. It reads schema 4 as ingest publishes it and nothing older; see "The
  backend reads schema 4". Its copy of the schema is derived (`published_schema.py`), and is derived
  again whenever the writers' schema gains a key, as it did with `context` on 19 November.

## How a word becomes an entry

Today, for schema 4:

1. **List.** `headwords/<name>-<lang>.json`, most frequent first.
2. **Gate** (`lemma_gate.py`, free). Wiktionary's verdict on the spelling, from an index built once
   by `lemma_index.py`: an inflected form or a bare abbreviation is not written.
3. **Write** (`claude_code_writer.py`, Opus at high effort, prompt v4.10). A word whose only entry
   is of the shape before schema 4 is written afresh too, from `rewrite-<lang>.json` with
   `--rewrite-older`: the owner's decision of 18 November 2026, when a mend (`mend_v4.py`: the old
   entry converted by code, the model returning only a patch) had come to cost nine tenths of a new
   entry and kept the older, narrower definitions. The older entry stays where it is.
4. **Audit** (`check_v4.py`, free). Every fault a program can see, sorted into what is fixed for
   free, what needs a paid patch of one field, and what a rule must settle.
5. **Ingest** (`ingest_v4.py`, free, rerunnable). Ids for lexemes and senses that survive a rewrite,
   leaked lexemes dropped, a word written under two spellings kept once with an alias, a headword
   rejected as an inflected form or a spelling pointed at the entry that holds its word, the
   sounds put in the form the dictionary prints (a syllable dot before every stress mark inside a
   word, an Italian double consonant written twice, the *ll* of Spain as /ʝ/), and *être* or
   *essere* given to every French or Italian pronominal sense.
6. **Witness** (`witness.py`, free). The gender, the plural, the stressed syllable and the sounds
   an entry states, held against what Wiktionary states for the word. See "Witnesses".
7. **Second look** (`settle_v4.py`, a small paid call for an entry the witness lists). A model is
   shown the word, what the entry has and what every source has, and changes the fact or keeps it
   with a reason; a change stands only if the sources then agree. See "The second look".
8. **Review loop** (`review_v4.py`; a reading of every entry by a second model family, and a small
   paid call for the one entry in four it finds a fault in). The writer answers each finding,
   a third model rules where the two disagree, and what is upheld is a patch. See "The review
   loop". Built and tried on 19 November 2026; not yet run over the dictionary.
9. **Measure** (`measure.py`). A random sample read by a second model family against a human
   dictionary, counted by kind of fault. See "Measuring the entries".
10. **Publish** (`publish_entries.py`, free). The clean entries of `out/lexicon-s4/` go to the backend,
    which checks each against the published schema and takes it; see "The backend reads schema 4".

Steps 3, 7 and 8 are models, 7 only for the entries a program has named. Steps 2, 4, 5 and 6 are programs: they cost nothing, give the same answer
every time, and can be run again over every entry whenever a rule is added. A fault found in a
review becomes a rule there when a program can decide it (a stress on the wrong syllable of a
Spanish word), a sentence of the next prompt when only a writer can (which phrases are frames),
and a line in the review file when the reviewer was wrong (what Attr means).

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

## Prompt v4.7: "mainly"

The first measurement (below, "Measuring the entries") found the entries' weak place: a grammar
tag that claims more than is true. *Soap* uncountable, though one says *two soaps*; *rushed* only
after a verb, though *a rushed commuter*; *bloggen* intransitive, though one blogs a text.

A learner's dictionary does not choose between "only" and nothing: it prints "usually before
noun" beside "only before noun". So does the shape now, with the word it already uses for where
a sense is spoken. A grammar, of the word or of one sense, may list under `mainly` the keys whose
value holds for most uses and not all:

    "grammar": {"position": "Attr", "mainly": ["position"]}      usually before a noun
    "grammar": {"countability": "Mass", "mainly": ["countability"]}    uncountable, except for kinds of it

A key not named holds without exception. The keys are countability, valency, position, number and
comparable. There is one step, not three: "sometimes" is not a restriction at all, and is said by
Both or by leaving the tag out; "never" is the other value said without `mainly`.

v4.7 also asks for these five tags as a learner's dictionary of the language prints them and to
leave out the one the writer is not sure of; gives pt-PT its vowels before the stress as "usually
ɨ, ɐ, u, but give each word what European dictionaries give it", where the old text stated the
reduction as a rule; and lists the weak vowels of English and German that the writers already
used.

Tried on forty-one words the measurement had flagged, written again under v4.7: all valid, and
twenty of them use `mainly`. Of thirty-one claims too strong, twenty-four no longer stand: hedged
(*Wald* countable, mainly; *telescopic* before a noun, mainly), dropped (*burnt* not comparable)
or widened to Both. Seven stand as they were. Of the seven pt-PT vowels three are right now, one
of them the prompt's own example, and four are unchanged (*inclusive*, *otomano*, *travesti*,
*envolvente*): what a writer does not know, a rule does not teach it, and those wait for a check
against a source.

The entries written before v4.7 keep their claims. Which of them are too strong only a pass over
them can say; what ingest can do without one it does (a value of the whole word that one sense
contradicts goes to the senses it is true of).

## Prompt v4.8: frames and collocations

Until v4.7 a sense had one list, `frames`, and a count of 36,300 of them (below, "Frames") found
two things in it. From v4.8 a sense has two lists (of at most three each then; five frames and ten
collocations from v4.9):

    "frames": ["decision on sth", "decision to do sth"]          how the word is built into a sentence
    "collocations": ["make a decision", "a tough decision"]      which words it is said with

- **A frame** is grammar: the preposition, the case or the kind of complement the word takes, with
  every open place marked. It always has one, and each language writes it one way, which the
  prompt now states for every language (the table under "Frames").
- **A collocation** is vocabulary: a particular word the sense goes with that a learner could not
  guess and would get wrong from another language (*make* a decision and not *do* one, *принять
  решение*, *薬を飲む*: medicine is drunk), or a set phrase the sense lives in (*in the lead*). It is
  written as it is said and has no open place.

The two are apart because they answer different questions, and neither follows from the other. A
frame filled in is a collocation only when the filling is the thing to learn (*pay sth* teaches
nothing by *pay a bill*, and something by *pay attention*). A word with no frame at all has
collocations (*rain*: *heavy rain*, *in the rain*; *sleep*: *sleep soundly*), and a word can have a
frame and no partner worth teaching (*dislodge sth from sth*).

What changed with it:

- The prompt's own example has *in the lead* and *take the lead* as collocations, and the reviewed
  examples of every language are shown with their frames sorted the same way (`gold/v4.8/`,
  twenty-nine senses re-sorted by hand, nothing added; Italian's and Portuguese's open places put
  in the table's spelling). The files of v4.7 stay as they are, since entries were written with them.
- An idiom a learner needs goes among the collocations, where the prompt used to send it to the
  frames.
- The notes that told Russian and Japanese how to write a frame give way to the one paragraph
  every language now has.
- **Ingest sorts what is already written**, for free (`sort_frames`): a frame with no open place
  moves to the sense's collocations, up to the ten a sense holds. Of the 36,300 frames counted,
  about 12,100 move, a third (English 33%, German 32%, French 40%, Spanish 35%, Italian 36%,
  Portuguese 33%, Russian 26%). What it reads as an open place is generous on purpose, the old
  abbreviations and full words too, so that a pattern is never taken for a pairing; one word alone
  (*suicidarse*, *勉強する*) is the form of a pattern and stays. The audit names both faults among
  those ingest puts right.

Tried on forty-one words that had no entry (`headwords/v48-check-*`, $4.77): all valid, and not one
frame without an open place or in another spelling, in any of the eight languages. Of 113 senses
69 have frames and 81 have collocations, 1.5 a sense: *одержать победу*, *сделать вывод*, *fall
into disrepair*, *offer your condolences*, *Appetit bekommen*, *約束を守る*. *Disrepair*, *rutina*,
*fortuna* and *薬* have collocations and no frame. The weak place is the pairing anyone would build
(*зависеть от обстоятельств*, *partager sur les réseaux sociaux*), which the prompt asks to leave
out and a few entries have all the same. The entries are no longer than before.

A test holds the three tables together: every frame the prompt quotes as a model is read by ingest
as open and left as spelled, and every collocation it quotes would be moved if written as a frame.

**The mender had to follow.** A mend returns a patch, and the patch of mend-v2 could give a sense
frames and nothing else: under the rules of v4.8 a mended word would have had no collocations at
all, where v4.7 had let it put them among the frames. mend-v3 is mend-v2 with both lists in its
instructions and in what a patch may set. Nine older words mended with it (`mend-v3-check`): all
valid, frames in the table's spelling, and collocations such as *ask for forgiveness*, *sich
schwarz ärgern*, *infliger une punition*.

**What sorting cannot give.** Ingest files what the writers wrote where it belongs; it does not
write what they were never asked for. After sorting, of the 59,355 senses of the 31,914 entries
written before v4.8, 29% have a frame and 15% a collocation, 0.2 collocations a sense. The fifty
entries written under v4.8 have a frame on 60% of their senses and a collocation on 71%, 1.5 a
sense. The trial words were chosen for having both, so the gap is smaller than that, but it is
real: an entry of before is right and thinner. The 48,600 older words still to mend get both lists
as they are mended. For the entries already in schema 4, a pass that returns only the two lists is
the way, most frequent first: see Pending.

## Prompt v4.9: a frame a program reads, and the word's common company

Two things the owner asked of v4.8 on the day it was made.

**A frame has to be usable by a program**, to find the word's use in a reader's text. A frame stays
a string a learner reads, and the string is formal: a headword, the words fixed around it, and
open places written with the marks of the table under "Frames". `ingest_v4.frame_parts` reads it,
and the clean entry carries the result beside the frames, as `patterns`:

    "decision on sth"       [{"head": "decision"}, {"word": "on"}, {"slot": ["thing"]}]
    "afford to do sth"      [{"head": "afford"}, {"slot": ["verb"], "form": "to-inf"}]
    "warten auf + Akk"      [{"head": "warten"}, {"word": "auf"}, {"slot": ["any"], "case": ["Acc"]}]
    "платить кому за что"   [{"head": "платить"}, {"slot": ["person"], "case": ["Dat"]}, {"word": "за"},
                             {"slot": ["thing"], "case": ["Acc", "Nom"]}]
    "il est normal que + subj"   [... {"word": "que"}, {"slot": ["clause"], "mood": "subj"}]

A slot says what stands there (person, thing, verb with its form, clause with its mood, adjective,
place, direction) and, in German, Russian and Ukrainian, the case. The model is not asked for any
of this: it writes the string, and code reads it, so the two cannot disagree. Three rules make the
string readable:

- **One frame, one pattern.** *decision on/about sth* is two frames. A slash stands only between
  two open places of one position (*sb/sth*, *кого/что*); no brackets. v4.9 asks for it and lets a
  sense have five frames; ingest splits what was written before (`split_frames`, 2,100 strings),
  frames and collocations alike.
- **An open place is never a word.** *etwas* and *qualcosa* for a place cannot be told from the
  words; the abbreviation can.
- **The headword is in it**, in a form that begins like the lemma, one of its spellings or, for
  Japanese, its reading.

Of the 24,400 frames written before v4.8, after ingest's sorting, 93.4% read: English 96.5%,
Portuguese 96.7%, Spanish 95.6%, Russian 93.9%, French 93.7%, Japanese 87.9%, German and Italian
86%, where one frame in ten has the open place as a full word. All 137 frames written under v4.8
read, and 90 of the 92 of the v4.9 trial. A frame that does not read has no `patterns` entry and
is named by the audit ("frame a program cannot read", with why). Finding a frame in a sentence
still needs what the book processor has, lemmas and a parse, because words move (*Ich warte seit
einer Stunde auf dich*); the parts say what to look for.

**Collocations are the word's common company, up to ten.** v4.8 allowed three and asked for the
ones a learner could not guess. v4.9 asks for the common ones, as many as are really common, those
a learner would get wrong first: the order is the priority, and a client that shows three shows
the three that matter. A word that goes with nearly every word of its kind (*big*, *good*, *very*,
*to have*) is still not one. The prompt's own example shows it on *lead* (*take the lead*, *in the
lead*, *hold the lead*, *lose the lead*, *a narrow lead*, *a comfortable lead*).

v4.9 also says that a grammar note states what is usual as usual, and a rule without exception
only where there is none: of the sixteen claims the Japanese count found, eight were notes that
said "follows" or "cannot" where "usually" was true.

Tried on thirty-two words that had no entry (`headwords/v49-check-*`, $4.54): all valid. A sense
has 4.1 collocations, a first sense 6.0, where v4.8 gave 1.5: *faire grève*, *être en grève*,
*préavis de grève*; *adquirir un hábito*, *colgar los hábitos*; *тяжёлый характер*, *не сойтись
характерами*; *пройти проверку*, *провести проверку*; *影響を与える*, *影響を受ける*, *影響を及ぼす*.
No frame holds a choice of words. One entry of the thirty-two (*finestra*) wrote two collocations
as frames, which ingest moves. The entries are 5% longer.

## The backend reads schema 4

Since 18 November 2026. What is published is the **clean entry** ingest derives, not the entry a
writer returned: the backend needs what ingest adds. `published_schema.py` derives the schema of a
clean entry from `entry.schema.v4.json` (ids, `homograph`, `patterns`, `sense_ids` on a form,
`aliases`, `dropped`, `retired`, `source`, and ingest's counts) and writes the backend's copy,
`src/main/resources/lexicon/entry.schema.json`; a test fails when that copy is stale, so after a
change to the writers' schema run it once. The backend refuses anything else, an entry of schema 2
or 3 included: there is one shape.

    scripts/.venv/bin/python scripts/lexicon_research/ingest_v4.py
    scripts/.venv/bin/python scripts/lexicon_research/publish_entries.py [--langs en,de] [--words lead,sign]

What the backend does with an entry (`LexiconPublicationService`):

- **A sense is keyed by the id ingest gives it.** Ingest owns sense identity: the id survives a
  rewrite while the definition stays mostly the same words, which ingest decides against the clean
  entry of the last run. The backend's own matcher is gone. A card and a report name a sense by
  that key; the history of a key (`/ops`) shows every version that had it and the version that
  dropped it. The ids live in `out/lexicon-s4/`, which the data repository does not track: a
  machine that ingests from nothing gives a sense the id of its definition, so a definition
  rewritten since the last publish gets a new key there. Publish from the machine that ingests.
- **A lexeme** lands on the row of its language, lemma, part of speech and homograph number, so its
  pronunciations stay where they are and the clips recorded for them stay attached.
- **Labels** are stored as written, each axis by itself; the single register of schema 3 is not
  filled any more. Frames, their parts (`patterns`), collocations, antonyms, the spellings of a
  lexeme by variety, the parts of a multiword, a form's varieties and senses, a reading with its
  notation and the Japanese accent are all kept and all served by
  `GET /public/lexicon/{language}/entries`.
- **A region** written as a bare variety (`en-GB`) is stored as only there.
- **A spelling the entry answers for** goes to `lexicon.entry_alias`: the entry's `aliases` (a form
  or a spelling that was asked for as a headword and refused) and the other spellings of its
  lexemes. A reader's word that is no headword, lemma or listed form is looked up there, so
  *colour* shows *color* and *signed* shows *sign*.
- **A variety**, wherever an entry names one (a transcription, a spelling, a form, grammar that
  differs by variety), must be one the product's catalogue lists for the language.
- **No glosses and no cues.** The sheet shows the definition and the example in the word's own
  language and no translation; the tables for glosses and cues stay, empty, for their passes.

Not done with it, in the order it will be felt:

1. **A translation line on the sheet.** A learner who cannot read the definition yet sees no
   translation for a lexicon word. Either the provider's translation beside the entry or the gloss
   pass.
2. **Regular forms.** Schema 4 lists only the forms a learner could not guess, so *walked* finds
   nothing unless it was asked for as a headword. The lookup needs a lemmatiser's answer, or the
   book processor's lemma with the tap.
3. **Shown by both clients since 18 November**, from the Discover sheet's response (each sense its
   labels, regions, grammar, frames, collocations and antonyms, and `lexemes` with the grammar,
   spellings, forms and relations of each word): the web sheet and the mobile lookup sheet. The
   reader's word card, on the web and in the app, shows the definition where there is no
   translation and keeps it as the card's meaning. Each client has an `entry-shape.ts` that turns
   the entry's keys into words; a new label or grammar value is named in both. `patterns`, the
   parts of a multiword and a reading without a transcription are served by the entries endpoint
   and shown nowhere yet.
4. **The entries published before** (schema 2, with glosses) stay in a database until their
   headword is published again or the owner clears the lexicon. The columns only schema 3 filled
   (`sense.register`, `usage`, `domain`, `lexeme.pattern`, `regions`, `lexeme_form.regions`) were
   dropped the same day by a change set of their own, so those older entries no longer have a
   register or a field. A version of the backend from before schema 4 cannot read the lexicon
   once that change set has run.
5. A reading with no transcription (Japanese) is stored and served by the entries endpoint, and the
   Discover sheet has no place for it yet.
## Prompt v4.10: what a value claims

Written on 18 November 2026 from the second sample's eight reviews. Three of its changes are to the
writer and one is to the reviewer; v4.9's text is as it was (pinned).

- **A grammar value says how the word is ordinarily used.** v4.7 to v4.9 said a value "holds
  without exception", with `mainly` for everything else. No learner's dictionary means that by
  "uncountable" or "transitive", and a reviewer who can think of one exception then reports a
  claim: one new entry in six had one. From v4.10 a value is what such a dictionary prints,
  `mainly` is kept for a word whose other use a learner meets in ordinary text (*soap, two soaps*;
  an adjective often used after a verb too), and the review file tells the reviewer the same: a
  value is a claim only when a learner's dictionary would not print it for the word.
- **Attr is "joined to a noun", on whichever side the language puts it.** The schema value never
  meant "before a noun"; the prompt and the review file glossed it so, and eight French and Italian
  adjectives that follow their noun were reported for it.
- **A phrase that is not complete without what follows it is a frame**: *in favour of sth*, never
  *in favour of* among the collocations (28 of the collocations reported in the sample). A
  collocation stands under the sense whose meaning it has, and an idiom no sense explains is left
  out (17). The word with only an article and a preposition of place or time is not one (*no
  fogão*).
- **Not changed: one word, one lexeme.** A reviewer of Russian asked for *брак* (marriage) and
  *брак* (faulty goods) as two lexemes, as dictionaries number them. A lexeme here is one way of
  saying and inflecting a word, which is what the forms, the sounds and the reader's saved word
  hang on; the two meanings are two senses. The review file now says so.

The trial: 39 words in eight languages chosen for what changed (adjectives that stand only with a
noun, nouns for stuff, nouns that live in one phrase: *sheer*, *luggage*, *behalf*, *hiesig*,
*zugunsten*, *insu*, *ropa*, *prol*, *бывший*, *単なる*), $4.05, and some $0.50 more for four
German calls whose answers were lost when the run stopped on a list row without its `set`; the
writer now reads its whole list before the first call. All 39 are valid; 45 frames, one
a program cannot read (*à + déterminant possessif + insu*); 3.9 collocations a sense. Every
adjective that stands only with a noun has Attr, on the sense where only one sense does (*sheer*
luck, not a *sheer* cliff); every noun for stuff is Mass, with `mainly` on the three whose plural
is common (*roba*, *mobília*, *bagagem*) and without it on the rest; and the phrases that need a
complement are frames (*on behalf of sb*, *aux dépens de qqn*, *all'insaputa di qcn*, *em prol de
algo*). Whether reviewers now report fewer claims is for the next sample to say: the trial shows
the writer follows the text, not that the text is right.

## Prompt v4.11: when a way of saying the word is the one said

Written on 19 November 2026, at the owner's word. A second transcription could say how common it is
(`note`: less_common, older, regional and four more) and nothing about when it is said. So *the*
before a vowel, French *six* before a consonant and *bath* in the north of England stood beside the
first transcription without a word, the condition sometimes hidden in a grammar note; and a weak
form was left out (*and*, *of*, *to* had only /ˈænd/, /ˈʌv/, /ˈtuː/) or given a stress mark it does
not have (/ˈðə/, /ˈkən/).

- **The shape**: a pronunciation may carry `context`, one clause in English of at most 80
  characters, like the `note` a form already has. It may stand on any reading, the first included,
  and beside a `note`: the note says how common the reading is, the context when it is the one
  said. An entry without it is as valid as before.
- **The prompt**: v4.11 is v4.10 with that clause and nothing else moved (a test holds the two
  against each other). The context names what follows the word, which use of the word it is, or the
  part of a variety where a regional reading is said. A word said weakly in running speech gives
  its full form first, as it is said alone, and then the weak form with the context "weak form,
  usual in running speech" and no stress mark: the first transcription stays the one a recorded
  clip says and the one the witness compares.
- **The trial**: 25 words chosen for it and against it, $3.17 (most of it the first call of each
  language, which pays for the prompt), in `out/review-context-v411/`. All 25 are valid, lose
  nothing in ingest and raise nothing with the witness.

| word | what v4.11 wrote |
|---|---|
| *the* | /ˈðiː/, then /ðə/ "weak form before a consonant sound, usual in running speech" and /ði/ "… before a vowel sound …" |
| *and*, *of*, *can*, *to*, *a* | the full form, then /ənd/ /ən/, /əv/, /kən/, /tə/ /tu/, /ə/ /ən/ as weak forms |
| *bath* | en-GB /ˈbɑːθ/, then /ˈbæθ/ regional, "north of England" |
| *six* | /ˈsis/ "alone, at the end of a phrase, or as an ordinal", /ˈsi/ "before a noun beginning with a consonant sound", /ˈsiz/ "… a vowel sound" |
| *neuf*, *plus* | /ˈnœv/ "before ans and heures"; /ˈply/ "in negation, and before an adjective or adverb", /ˈplys/ "meaning more, after a verb or at the end of a phrase" |
| *Chemie*, *König*, *casa* | /ke.ˈmiː/ regional "southern Germany"; /ˈkøː.nɪk/ regional "south of Germany"; /ˈka.za/ regional "northern Italy" |
| *на*, *что* | /nɐ/ and /ʂtə/ as weak forms: the writer carried the rule to Russian's unstressed little words |
| *either*, *schedule*, *дождь* | a note only (less_common, newer, older): nothing decides, and no context was invented |
| *table*, *water*, *maison*, *Haus*, *стол*, *zio* | one way of saying it, no context |

Entries written before v4.11 are not written again for this. The words it matters for are few and
known: the weak forms of English are a closed list of some forty words, and a patch of that one
field is the second kind of change in "What a later change costs".

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

### The first count

All six languages, 600 entries, 701 findings. Entries in a hundred with the fault:

| | wrong fact | claim too strong | core sense missing | no fault at all |
|---|---|---|---|---|
| Aim | at most 2 | at most 5 | at most 10 | |
| All 600 | 5.0 | 11.2 | 9.2 | 29 |
| The 360 held (all written under v4) | 5.3 | 14.2 | 13.6 | 20 |
| The 240 new (v4.6, rarer words) | 4.6 | 6.7 | 2.5 | 43 |

Two of the three aims are missed. Where, by a reading of each finding:

- **Claims too strong (67 entries)** sit almost wholly in four tags of grammar: countability (15
  findings), valency (15), position (14), comparability (5), with number (6), labels (7) and
  regions (3) after them. "Countable" where a mass use exists, "only before a noun" where the word
  also follows a verb, "transitive" where a rarer intransitive use exists. Under v4.6 position
  nearly went (2 findings among the 240), valency did not (7).
- **Wrong facts (30 entries)**: seven are the vowel before the stress in the Portuguese of
  Portugal (*normal*, *otomano*, *inclusive*), where our own convention states as a rule ("unstressed
  o is u") what has many exceptions. Nine are how a pronominal or prepositional verb is classed,
  where the reviewer follows the national grammars and we chose one value (Refl) and one
  definition (transitive means a direct object); it was not told so. Fifteen are errors plain and
  simple: a meaning the word does not have (*veineux* for *veinard*), a stress (*vendita*), two
  verbs taken for one (*impostare*), an auxiliary, a glide.
- **Core sense missing (55 entries)**: thirty-four truly lack a sense or a part of speech. Thirteen
  lack a fixed phrase a learner needs at that word (*sans doute*, *por favor*, *darse cuenta*, *de
  nouveau*), which by our rule is an entry of its own, and those entries are not written. Nine
  are a headword that is also a form, an abbreviation or a name (*given*, *mm*, *Mike*), which is
  not a lexeme by design: the reader reaches *give* through the alias, which the reviewer was not
  shown.

So the meanings and the forms hold, and three things do not: grammar tags that say "only" or
"never", the sound of unstressed vowels in pt-PT, and the fixed phrases.

The reviewer marked 94 findings in a hundred as misleading, so that mark tells nothing apart, and
it found something in 71 entries of a hundred, most often a minor sense missing (34) or a
definition narrower than the word (37). Those two are what any dictionary is reproached with by
another, and they have no aim.

What followed at once, free: ingest moves a grammar value stated for the whole word to the senses
it is true of when one sense states another (776 values in 710 entries), so the word no longer
claims it. What it asks of the next prompt version is under "Pending".

### The second sample, v49

Made on 18 November 2026 for prompt v4.9, and for the mender that will write most of what remains:

- **new**: forty words with no entry, in each of eight languages, written under v4.9 ($16.47). All
  valid; four of the 320 were refused as no lemma.
- **mended**: fifteen older words in each of the six languages that have older entries, mended
  under v4.9 with mend-v3 (ninety in the sample; $5.30 for the 98 words it took, of which two the
  mender asked to have written afresh and six have no lexeme to show).
- **have**: twenty Russian entries of the trial, which this method had never counted.

425 entries in eight files, `measure/v49/review/<lang>-1.md`, the parts mixed. The six faults are as
before, so the count can be held against the first one, and there is a seventh counted apart,
**collocation**: one that is not said, one that does not belong among a sense's ten (anyone would
build it, or it is rare), or a list that lacks the commonest partner. The reviewer quotes it, so
that the answers also say whether the seventh to ninth of a list are worth their place.

Before any reviewer: a sense has 3.4 collocations in the new entries and 3.7 in the mended; a
program reads 444 of the 452 new frames and 156 of the 157 mended.

**The count** (all eight files, 426 entries, 18 November 2026), entries in a hundred with the fault:

| | fact | claim | missing core | wording | collocation |
|---|---:|---:|---:|---:|---:|
| the bar | 2 | 5 | 10 | | |
| new, v4.9 (316) | 8 | 16 | 7 | 38 | 16 |
| mended, v4.9 with mend-v3 (90) | 8 | 14 | 10 | 58 | 21 |
| Russian of the trial, v4.6 (20) | 5 | 10 | 15 | 35 | 0 |

| new entries | en | de | fr | es | it | pt | ru | ja |
|---|---:|---:|---:|---:|---:|---:|---:|---:|
| fact | 2 | 5 | 2 | 15 | 5 | 11 | 18 | 5 |
| claim | 15 | 18 | 20 | 15 | 20 | 18 | 15 | 8 |
| missing core | 0 | 8 | 12 | 0 | 5 | 5 | 12 | 12 |
| collocation | 2 | 13 | 12 | 15 | 22 | 13 | 32 | 12 |

The missing-core bar is met; facts and claims are not, as in the first count. The first count's new
entries stood at 5, 7 and 2, but they were rare words of one or two senses, and these are common
ones outside English; the two counts are not one scale, and each language is forty entries.

- **Facts.** About a third are a sound: Portuguese vowels before the stress again (four), the
  Spanish tap written as a trill (*carrera*, *representación*), Russian stress (*креди́т* and
  *кре́дит* the wrong way round, *осо́бо*, and *попро́бовать* transcribed with the stress on another
  syllable than its own stressed spelling). That last kind needs no source, and the audit now
  finds it ("transcription stressed on another syllable than the reading"): four of the 391
  Russian pronunciations, each with the spelling right and the transcription wrong. Another third
  is how a word is divided: two homonyms written as one (*брак*, *заставить*, *ポリス*), a valency
  the reviewer counts otherwise (*dormir la siesta*). The rest are a frame that is wrong (*sotto
  di qcn*, *plus que aucun*) or a relation.
- **Claims** are still the grammar tags: of 71 findings twenty are countability or number, fourteen
  position, seven valency, ten a label, eight a note. `mainly` is used and does not carry it: a
  reviewer finds the exception to any tag stated without it (*a selection of fine cottons*). Eight
  of the fourteen position findings are ours and not the entries': French and Italian adjectives
  tagged Attr, which the writers mean as "with a noun, not after a verb" and the prompt and the
  review file both gloss as "before a noun" (*loi martiale*). Position has to be defined by what
  the adjective is joined to and not by where it stands; the review file says so now, the prompt
  from its next version.
- **Collocations.** 3,538 were shown and 2.6% were named: 28 findings an open place left in, mostly
  a phrase that ends in its preposition (*prendre la direction de*, *sotto la guida di*); 19 a
  pairing anyone would build (*molto chic*, *no fogão*, *ехать быстро*); 17 one that belongs to
  another sense or means what the definition does not say (*отправить на тот свет*, *vider les
  lieux*). By its place in the list a collocation is named 2.9, 1.2, 2.6, 4.4, 2.8 and 2.1 times
  in a hundred from the first to the sixth, and 2.7 from the seventh on: the tail is no worse than
  the head, and ten stays. Ingest now moves a collocation with an unmistakable open place to the
  frames (`sort_frames`).
- **The mender** is level with the writer on facts and claims and behind on wording, 58 against
  38: the definitions it keeps from the older entry are found too narrow more often. It is hardly
  cheaper any more. The calls of this sample cost $0.056 a mend (90 calls) against $0.052 a new
  entry (320 calls), but that comparison is unfair to the mender: a batch of fifteen words on four
  workers pays to write the prompt cache in one call in five (17 of 90, $0.13 each), a batch of
  forty in one in ten (32 of 320). Among the calls that found the cache written, a mend costs
  $0.038 and a new entry $0.041: eight percent less, where mend-v1 cost half ($0.023). Two things
  ate the difference. The mender reads the same twenty thousand tokens of rules and the old entry
  on top; and since mend-v3 it writes the collocations of every sense, so its answer grew from
  some 730 tokens to 880 against a new entry's 1,300. What it still gives is the older entry's
  glosses and cues, kept beside the entry. The owner decided the same day that this is not worth
  the weaker definitions: the older words are written afresh ("Healthy lists").

Drawing it showed three things. The mender's queue names every word with an older entry, also the
ones a writer has since written afresh, which the mender then passes over for nothing; the draw
leaves them out. A draw made again after the first words are written must keep them, or their
price is lost: words already written or mended for a measurement come first, and a word a draw
should not have had is named in `measure/<name>/skip.json` and gives its place to the next. And
the lists the words come from hold what is no word to write: in `core-de` a large share of the
7,000 capitalised words are names of people and towns, for which the frequency list has no capital
to tell them by; `core-pt` has 360 past forms Wiktionary files as lemmas (*fizeram*, *tiveram*) and
`core-it` 557 infinitives with a pronoun (*farlo*, *dirgli*). The last two cost a refusal each and
become aliases; the names become entries nobody asked for.

### The evidence trial, v4.10e

Does a writer do better when the request carries what Wiktionary states about the word? October
asked this of schema 3 and found that evidence gave checkable facts and not better definitions
(`grounded_pipeline_results.txt`). On 19 November it was asked of schema 4, on the 280 words of the
third sample, so that every word exists twice: as written under v4.10, and written again under
v4.10e, which is v4.10 with one more section saying how to use the block at the end of the request
(a check on oneself, never a model: a common sense or part of speech left out is a reason to think
again, a rare one still stays out; a gender, plural or stress that differs is usually right; the
notation is never copied; every definition is the writer's own). The block is October's
(`wiktionary.py`): each word Wiktionary has under the spelling, with gender, plural, forms and
transcriptions, and its senses in short English glosses; 170 to 310 characters for most languages,
1,250 for English. The entries are in the trial folder `out/review-evidence-v410e/` and are not
the dictionary's.

What a program can say of the two, same words:

|  | v4.10 | v4.10e, with evidence |
|---|---|---|
| list price an entry | $0.053 | $0.057 |
| lexemes, senses an entry | 1.30, 3.45 | 1.29, 3.43 |
| collocations a sense | 3.5 | 3.4 |
| sounds no source has | 6 of 523 | 1 of 523 |
| gender, stress no source has | 0, 0 | 0, 0 |
| refused, invalid | 0, 0 | 0, 0 |

- **Facts: better.** The three faults both a reader and the witness had named are gone
  (*successivo*, *управление*, *бывший*); the one that is left is the same in both arms and is the
  Portuguese edition's notation.
- **Size: the same.** The fear was that a list of every sense Wiktionary has would swell the
  entries; it did not, by one sense in a hundred entries either way.
- **Parts of speech: eleven entries differ**, both ways: *comunale* gains its noun and *этот* its
  pronoun, which GPT had asked for; *за* loses its noun and *top* its adjective.
- **Core senses GPT found missing in the first arm, fourteen**: by my reading the second arm has
  four or five of them (*certainement* as certainty, *moteur* in *moteur de recherche*, *orgulho*
  as self-respect, *Fernsehen* as the medium), lacks nine, and dropped a sense elsewhere in two.
- **Copying: none measurable.** Of the English definitions, 9 in 198 share five words in a row
  with a gloss they were shown, against 7 in 187 written without seeing any.

What only a reader can say is whether senses and wording are better, and the reader that counted
the first arm counted the second: `measure/v410e/review/` holds the same forty words a language
from the trial folder, with the five checked entries, and `measure.py --name v410e --tally --pair
v410` sets the two counts side by side entry by entry.

**The count, 19 November: the evidence did not make the entries better.** Entries in a hundred
with the fault, the same 280 words and the same reader (GPT):

| fault | v4.10 | v4.10e | gone in, new in |
|---|---|---|---|
| fact | 4.3 | 4.3 | 11, 11 |
| claim | 16.1 | 15.4 | 22, 20 |
| missing-core | 4.6 | 9.6 | 7, 21 |
| missing-minor | 46.8 | 54.3 | 22, 43 |
| split | 2.9 | 2.1 | 8, 6 |
| wording | 38.6 | 43.6 | 41, 55 |
| collocation | 17.9 | 20.7 | 26, 34 |

Two things are in those numbers, and the comparison now prints both.

- **The reader moved.** The 35 entries checked by hand are the same text in both samples. GPT
  found something in 11 of them the first time and in 18 the second (wording 4 then 9,
  missing-minor 5 then 7). A rise of a tenth in wording or in minor senses is therefore the reader,
  not the writing. This is what the two-reader count had already said: one reading by one model is
  a noisy instrument, and a difference between two samples means something only beside the same
  difference on unchanged entries.
- **The writer followed the size of what it was shown.** Read one by one, of the 21 entries with a
  new missing core sense, six had that sense in the first arm and lost it (*begin* as where
  something starts, *euer* as a pronoun, *considerado* as respected, *top* as an adjective,
  *atitude* as posture, *тут* as "in this matter") and two more lost part of it; the other thirteen
  lack it in both arms and are the reader. Of the seven that went, five are senses the second
  arm has: three stood in the block (*certainement*, *moteur*, *на*) and two did not (*orgulho*,
  *центр*), which is what any second writing brings. In every one of the six losses the
  block the writer saw did not have the sense: English Wiktionary gives *considerado* as
  "considerate, thoughtful" and *top* in Italian as a garment, and the writer wrote what it was
  shown.

| senses the block held | entries | senses an entry, first then second | fewer in, more in |
|---|---|---|---|
| one | 80 | 2.39, 2.29 | 14, 7 |
| two | 66 | 2.89, 2.79 | 15, 9 |
| three or four | 57 | 3.56, 3.35 | 19, 8 |
| five to nine | 41 | 4.17, 4.29 | 8, 11 |
| ten or more | 35 | 6.00, 6.43 | 6, 15 |

So the block pulls an entry toward its own size, whatever the prompt says about it being a check
and not a model. English, whose block is the long one, grew (more senses in 15 entries, fewer in
5) and has no fact and one missing core sense in forty; the other six languages, whose blocks are
English Wiktionary's short record of a foreign word, shrank (fewer in 57, more in 35). The flat
average of the program's table hid the two movements.

What stays true: the sounds are better with the block (the three wrong sounds a reader named in
the first arm are gone, and none came). What follows:

1. **v4.10e is not the writers' prompt.** For six languages of seven it trades senses for sounds,
   and the witness with a second look already mends sounds for less.
2. **A block of facts alone** (gender, plural, forms, transcriptions, no senses) keeps what was
   gained and cannot pull the senses. Not tried.
3. **Senses from a record that is as full as the entry should be**: each language's own
   Wiktionary. Tried the same day, below.

### The second evidence trial, v4.11e: both editions

The first trial showed every language the English Wiktionary's record. The owner had taken it to
be each language's own edition, which until then was loaded as a witness only, and asked for
that. `open_sources.py` now keeps the meanings each edition numbers
(`wiktionary/<lang>.native.senses.tsv`: 83,000 for Italian to 543,000 for French), and from
v4.11e a writer given evidence is shown two blocks: the English edition's record as before (its
facts are what mended the sounds), and under it "Meanings in the German Wiktionary", the own
edition's parts of speech and definitions in its own words. English is its own edition and has
one block.

How full the editions are, for the 40 sample words a language:

| | senses, v4.10 entry | English edition | own edition | block thinner than the entry: English, own, the fuller |
|---|---|---|---|---|
| de | 2.77 | 2.65 | 3.52 | 16, 10, 8 |
| fr | 3.62 | 2.48 | 7.40 | 28, 1, 1 |
| es | 2.98 | 2.85 | 5.00 | 20, 4, 2 |
| it | 2.95 | 2.85 | 3.08 | 16, 11, 7 |
| pt | 3.25 | 3.05 | 3.85 | 18, 14, 9 |
| ru | 3.92 | 3.58 | 6.38 | 20, 6, 4 |

The own edition is the fuller one in every language, by far in French, Spanish and Russian and
by little in Italian and Portuguese, where it still lacks *top* as an adjective and gives
*atitude* one line. So v4.11e also says what v4.10e did not: a short block is an editor who
stopped, and no sense in everyday use is left out because the evidence lacks it. v4.11e is v4.11
with that section and nothing else (pinned); the 280 words are in `out/review-evidence-v411e/`,
$18.96.

What a program can say of the three arms, same 280 words:

| | v4.10 | v4.10e, English block | v4.11e, both blocks |
|---|---|---|---|
| list price an entry | $0.053 | $0.057 | $0.068 |
| senses an entry | 3.45 | 3.43 | 3.73 |
| entries with fewer senses than v4.10, with more | | 62, 50 | 24, 81 |
| sounds no source has | 6 of 523 | 1 of 523 | 1 of 531 |
| gender, stress no source has | 0, 0 | 0, 0 | 0, 0 |
| definitions with five words in a row from the own edition | 8 of 967 (unseen) | | 16 of 1,044 |

- **The pull is gone.** Every language has more senses than without evidence, and three entries
  gain for one that loses. Five of the six senses the first trial lost are back (*euer* as a
  pronoun, *considerado* as respected, *top* as an adjective, *atitude* as posture, *тут* as "in
  this matter"), three of them against blocks that still lack them. *begin* lost its third sense
  again: English has the same single block as before.
- **What was added is mostly not rare.** Of the 77 senses more than v4.10, 68 are B1 to C1; C2
  went from 19 to 23. Some are what the rules keep out (*тут* the mulberry tree, *smart* a
  stinging pain), and no fault a reader is asked for counts those.
- **Copying: eight definitions more in a thousand** share five words in a row with a definition
  they were shown, against the eight that do by chance. Small, and to be looked at if the prompt
  is adopted.
- **Facts: as in the first trial.**

Whether the senses and the wording are better is the reader's to say:
`measure/v411e/review/` holds the 45 entries a language for GPT, the five checked ones the same
text as in both earlier rounds, and `measure.py --name v411e --tally --pair v410` sets the count
beside the first arm's with the reader's drift.

**The reader's count of the three arms** (GPT Astra 6.2 with the web open, the same 280 words;
entries in a hundred with the fault):

| fault | v4.10, no evidence | v4.10e, English block | v4.11e, both blocks |
|---|---|---|---|
| fact | 4.3 | 4.3 | 6.1 |
| claim | 16.1 | 15.4 | 15.7 |
| missing-core | 4.6 | 9.6 | 6.8 |
| missing-minor | 46.8 | 54.3 | 45.7 |
| split | 2.9 | 2.1 | 2.1 |
| wording | 38.6 | 43.6 | 47.5 |
| collocation | 17.9 | 20.7 | 18.6 |
| the same 35 checked entries: a finding in | 11 | 18 | 15 |
| of those, missing-minor and wording | 5, 4 | 7, 9 | 8, 6 |

- **The own edition mended what the English block broke**: core senses missing 9.6 to 6.8, rarer
  ones 54.3 to 45.7.
- **Against no evidence, the reader sees no gain.** Rarer senses missing are level, with a reader
  that found them in eight unchanged entries where it had found five; wording and core senses
  are nominally worse, and none of it is outside the reader's own drift.
- **Core senses, read one by one.** Thirteen entries have a new missing core sense: three lost a
  sense the first writing had (*begin* as where something starts, *aimer* as to wish, *elemento*
  as a fact to judge by), four have it worded more narrowly (*préparer* no longer "to be eaten",
  *tratar* no longer "of someone"), six lack it in both writings and are the reader. Seven went
  the other way. None of the three losses is the block's doing this time: the French Wiktionary
  has *aimer* at length.
- **Facts, read one by one.** Sixteen new, eleven gone. The three wrong sounds went, as in the
  first trial. Two of the new ones are the evidence: a lexeme Wiktionary has and the rules do not
  (*tranquilo* as an interjection, *есть* "there is" as a verb of its own). Three are v4.11's
  `context`, not the evidence: on the Russian prepositions *к*, *без* and *над* the writer stated
  when each sound is said and stated it too widely (/ɡ/ "before a voiced consonant", which *к
  маме* is not; *над* with /t/ where *над нами* has /d/). The rest are the frames and spellings
  every arm has.

**What the three arms say together.**

1. **A second writing is another draw.** Each time the 280 words were written again, some entries
   gained a common sense and about as many lost one, whatever the request carried. The evidence
   moved the average (3.45, 3.43, 3.73 senses) and not that. So an entry is not made better by
   writing it again, with or without evidence: it is made better by a patch that adds what is
   missing and touches nothing else, which is what the review loop does.
2. **One reading by one reader cannot tell arms this close apart.** The reader found a third more
   in the same unchanged entries from one round to the next. Planted faults and unchanged entries
   are what make its numbers usable at all.
3. **Evidence is not adopted for the writer** (recommended; the owner's to confirm). What it
   surely buys is the sounds, which the witness and a second look mend for $0.03 on the entries
   they list; what it surely costs is $0.015 an entry, $600 over the 40,000 words to come, the
   tables on every machine that writes, and the lexemes it copies from Wiktionary. v4.11e and its
   tables stay, for a language or a list where a count shows the writer short of senses.
4. **The `context` clause wants watching on words said as one with the next.** Three faults in
   six Russian prepositions that carry one. They are a closed list in every language and the
   review loop's reader named all three; if the next sample has them too, the clause is narrowed
   in a v4.12 to what a dictionary prints for the word and not the rule behind it.

### Measuring the reviewer

A count is only as good as its reader, and the second sample showed the reader is wrong too: eight
"claims" were an adjective after its noun reported for a tag that never said "before". Three
models are in play, not two: Opus writes, and GPT Astra and Claude Fable can each read. That is
enough to measure a reader, and none of it needs a person who knows the language. `measure.py`
does it from the third sample on (`--written`, `--planted`, `--gold`):

- **Planted faults.** Entries of the same pool with one fault put in by code, not marked: a gender
  flipped, the stress moved a syllable (in the transcription and in the stressed spelling), Mass
  for Count, the examples of two senses changed over. Only a fault beyond dispute is planted: a
  gender or a stress where Wiktionary has the entry's own and no other, Mass on a noun it gives a
  plural, always in the word's first lexeme. What was planted is in `planted-<lang>.json`; the
  tally says how many of each kind the reader named, by the kind of fault and the place.
- **Entries known to be right.** The example entries a person has checked (`gold/`) go in
  unmarked. What a reader reports in them is its false alarms, printed finding by finding.
- **The witness first.** Every drawn entry is held against Wiktionary before it is shown, and what
  differs is kept in the sample file, unseen. The tally says in how many of those entries the
  reader reported a fact.
- **Two readers on the same part.** A second reader's answers go under `answers-2/`, and the tally
  says, fault by fault, in how many entries both named it out of those either did.

A planted or a checked entry is not counted in the rates or against the bar: it is the reader
being measured, not the dictionary.

### The third sample, v410

Drawn on 19 November from the 1,863 entries written under v4.10 (the first night and the trial),
nothing bought: forty entries a language as written, six to eight with a planted fault, five
checked by hand, in one file of some fifty entries a language (`measure/v410/review/`). Seven
languages; Japanese has four entries of v4.10 and waits for its list.

|    | as written | planted | checked by hand | Wiktionary differs in |
|----|-----------|---------|-----------------|----------------------|
| en | 40 | 8: stress 3, Mass 3, examples 2 | 5 | 0 |
| de | 40 | 8: gender 2, stress 2, Mass 2, examples 2 | 5 | 1 |
| fr | 40 | 6: gender 2, Mass 2, examples 2 | 5 | 2 |
| es | 40 | 8: two of each | 5 | 0 |
| it | 40 | 8: two of each | 5 | 1 |
| pt | 40 | 8: two of each | 5 | 1 |
| ru | 40 | 6: gender 2, stress 2, examples 2 | 5 | 2 |

French has no stress to move and English no gender; Russian nouns have no plural in the facts
table, so no Mass was planted there. The words are the commonest of each language (the top of the
rewrite lists and of `healthy-ru`), so this sample says how v4.10 writes common words and nothing
yet of rare ones. Answers go to `measure/v410/answers/<lang>-1.md`, a second reader's to
`answers-2/`, and `measure.py --name v410 --tally` counts.

**The second reader, first.** Claude Fable read all seven files blind on 19 November: one fresh
reader a language, given its review file and nothing else, no search, not told that anything was
planted. Its answers are `answers-2/`. Counted by themselves:

- Of the 280 entries as written, 88% without a finding. In a hundred: fact 1.8, claim 1.1,
  core sense missing 0.4, all three within the bar; wording 2, collocation 6, a rarer sense
  missing 1. By language the facts are two entries in Russian and one each in French, Spanish and
  Italian.
- **Every planted fault found, 52 of 52**: 12 genders, 13 stresses, 13 nouns made uncountable, 14
  pairs of examples changed over. So a stated gender, stress or countability that is plainly wrong
  does not get past this reader. It says less than it seems: these are faults a reader sees by
  looking, and a fault that takes knowing (an open vowel, a devoiced consonant) is the harder kind.
- **The harder kind: three of seven.** In the seven entries Wiktionary contradicts, the reader
  reported a fact in three, and they are the three real ones, each named for the same sound the
  witness named: Italian *successivo* written /suk.tʃes/, Russian *управление* with /ə/ before the
  stress, *бывший* with /v/ before /ʂ/. The four it passed are the witness's doubtful ones (a
  loanword said two ways, a stress mark in French). Reader and witness, neither seeing the other,
  agree on what is wrong: that is the strongest evidence this page has.
- It also found two the witness cannot: French *disposition* with an open /ɔ/, which the witness
  lets pass on purpose, and the secondary stress of Spanish *seguramente*.
- **In the 35 entries checked by hand, findings in four**: *hall* ("mainly Britain" too strong, a
  collocation missing), German *Trainer* (the tracksuit is Swiss, not Austrian), French *hôpital*
  (*être hospitalisé à l'hôpital* says it twice), *historique* (a definition too narrow). At least
  two of these are right, so the checked entries are not faultless and the reader's false alarms
  are below one entry in ten.

**GPT's count**, all seven files, 19 November (`answers/`). Of the 280 entries as written, 22%
without a finding. In a hundred:

|    | fact | claim | core sense missing | rarer sense missing | wording | collocation |
|----|------|-------|--------------------|---------------------|---------|-------------|
| all | 4.3 | 16.1 | 4.6 | 47 | 39 | 18 |
| en | 0 | 15 | 0 | 42 | 35 | 22 |
| de | 8 | 10 | 8 | 52 | 38 | 12 |
| fr | 0 | 10 | 10 | 48 | 42 | 30 |
| es | 8 | 22 | 2 | 52 | 28 | 22 |
| it | 2 | 15 | 0 | 40 | 45 | 10 |
| pt | 8 | 12 | 2 | 38 | 48 | 8 |
| ru | 5 | 28 | 10 | 55 | 35 | 20 |

Against the bar: core senses met (4.6 for 10), facts not (4.3 for 2), claims not (16.1 for 5).
Beside the second sample's 8, 16 and 7, facts and core senses are lower and claims have not moved,
on commoner words and a newer prompt; which of the two did it this count cannot say.

- **The reader measured.** GPT named all 52 planted faults too. In the 35 entries checked by
  hand it reported something in 11, never a fact, once a claim: four rarer senses missing, three
  wordings, one collocation. That is its floor: about a third of the entries a person has
  corrected still draw a "rarer sense missing" or a "wording" from it, so those two columns
  measure the reader's appetite as much as the dictionary. Its facts and claims are not of that
  kind: none and one in 35.
- **What the facts are.** Twelve entries. Three are sounds (*successivo*, *бывший*, and the
  secondary stress of *seguramente*), two of which the witness had; the other nine are in fields
  nothing checks: two capitals in a collocation (*Miércoles de Ceniza*), two relations with a
  wrong note, two frames, the gender of a loanword, a part of speech, a spelling note.
- **What the claims are.** 55 findings: countability 12, valency 9, other grammar values 11,
  labels 11 ("literary" on a sense that is ordinary), notes 5, regions 3. Three in five are still
  a grammar value to which GPT knows another use (*beneficio* "also has a regular mass use"),
  which v4.10's wording and the review file both say is not a claim. The reader does not take the
  instruction, or the writer gives the value too easily; telling the two apart needs the value
  held against a learner's dictionary, word by word, and that is a count of fifty, not a prompt.
- **The two readers hardly ever name the same thing.** Of the entries either named for a fault,
  both named it in 3 of 14 for facts, 3 of 45 for claims, none of 14 for a core sense, 2 of 132
  for a rarer sense, 5 of 109 for wording, 14 of 53 for collocations. Where they do agree on a
  fact it is one the witness has as well. So a finding one reader makes alone is a suggestion,
  and the dependable part of a count is small: what two readers or a reader and a source both say.

One caution outweighs the rest. Fable is of the writer's family: the two may share what they do
not know, and a reader that agrees with the writer is not thereby right. These rates are a second
reader's, to be set beside GPT's when they come, not in place of them. And the words are the
commonest of each language, where the second sample's were rarer: the fall from 8 facts and 16
claims in a hundred cannot be given to v4.10 by this count.

### What a later change costs

Of the 34,034 schema-4 files, 32,651 were written under v4, 588 under v4.6, 258 under v4.7, 50
under v4.8, 448 under v4.9 and 39 under v4.10. None was written again because the prompt moved
on, and none needs to be, because a change is one of four kinds and only the last is a rewrite:

1. **A rule**: how a frame's open place is spelt, a stress mark in a form, a frame without an open
   place. Ingest applies it to every entry ever written, for nothing, in minutes.
2. **A fact wrong in some entries**: a stress, a sound, a gender. The witness lists them and a
   patch of that one field costs some $0.02 an entry, for the listed entries only.
3. **A field that did not exist**: the collocations, for the 33,500 entries of before v4.8. One
   pass that returns that field, the same $0.02.
4. **The senses and definitions themselves judged wrong**: a rewrite, $0.041. No count has asked
   for this across the board: core senses missing are within the bar.

So an entry of v4.10 is not at risk from a v4.11; it is at risk from a change of shape. The shape
has not changed since v4, but it has never been read by the backend either, and that reading is
where a missing or awkward field will show. Two things follow. The prompt changes from here only
when a kind of fault is over the bar in two samples running and neither a rule nor a patch can
mend it. And the large runs (39,902 older words, the healthy lists) wait for the third sample and
are better started after the backend shows schema-4 entries in the app.

## Witnesses

A third of the facts the second sample's reviewers reported are sounds, genders and stresses: things
no writer knows without a source, and things the owner cannot check in a language they do not
read. A reference states them for nearly every word, so they are held against it by a program.

`lemma_index.py` copies three more things out of each lemma of the Wiktionary extract into
`wiktionary/<lang>.facts.tsv`: its genders, its transcriptions with the place Wiktionary names for
each, and its plurals. A transcription Wiktionary sets apart from the standard, by a note
("Southern US", "weak form") or a tag such as *dialectal*, is marked with a `?`. `witness.py`
compares every written entry, as ingest would publish it, and prints for each kind of fact how many
could be compared, how many agree, and the ones that do not (`--leads` writes them to a file).

The comparison is as lenient as the fact allows:

- A **gender** or a **plural** agrees when it is among Wiktionary's.
- A **stress** agrees when it falls on the same syllable, counted from the start or from the end,
  in any of Wiktionary's transcriptions.
- **Sounds** are compared after both sides are brought to one spelling, because the two write one
  sound in different ways. What that takes is in four tables of `witness.py`, every line read off
  words that were right on both sides: marks finer than our symbol lists are dropped (`FINER`), a
  consonant that is a syllable becomes schwa and the consonant, a sound in brackets is said or not,
  symbols are replaced (`SAME_SOUND`: Wiktionary's /ɛ/ of Britain is our /e/, its narrow Russian
  [æ ɵ ʉ ʊ] are our /a o u u/), a sound that depends on its place is settled (`BY_PLACE`: German
  *r* after a vowel is /ɐ/ whoever writes it so, a French *o* outside the last syllable is open or
  closed by turns), and pairs either of which a dictionary prints are no difference (`EITHER`: an
  English weak vowel, Italian *s* between vowels). A variety of ours is held against the
  transcriptions named for its place; one Wiktionary sets apart, or names for no place beside
  others it does name, can confirm what we wrote and cannot contradict it. The script prints which
  pairs of sounds differ most often, which is how a habit of notation (hundreds of one pair) is
  told from a fault (a pair seen twice).

All 33,751 written entries, 19 November 2026, held against every source loaded for the language
("Sources and witnesses"). Each cell is compared, agreeing, differing:

|    | gender | plural | stressed syllable | sounds | entries to read |
|----|--------|--------|-------------------|--------|-----------------|
| en | - | 422, 95.5%, 19 | 24,149, 98.9%, 269 | 29,501, 98.5%, 453 | 161 of 17,109 |
| de | 1,852, 99.6%, 7 | 1,574, 99.5%, 8 | 2,838, 99.2%, 23 | 3,263, 98.5%, 49 | 35 of 3,281 |
| fr | 2,514, 99.9%, 2 | 41, 95.1%, 2 | - | 4,337, 99.8%, 9 | 13 of 3,729 |
| es | 1,846, 99.7%, 5 | 41, 100%, 0 | 7,098, 99.6%, 25 | 7,216, 99.1%, 65 | 44 of 3,192 |
| it | 1,720, 99.7%, 6 | 61, 100%, 0 | 3,106, 99.6%, 12 | 3,164, 98.3%, 54 | 58 of 2,904 |
| pt | 1,661, 99.8%, 4 | 129, 99.2%, 1 | 5,766, 99.9%, 4 | 5,866, 96.4%, 209 | 179 of 2,946 |
| ru | 286, 100%, 0 | 253, 100%, 0 | 508, 98.4%, 8 | 704, 96.7%, 23 | 21 of 590 |

**What each language's own Wiktionary changed.** Until 19 November the witness read only the
English Wiktionary's entries for each language. The German Wiktionary, the Wiktionnaire and the
others describe their own language more fully, and kaikki.org publishes each as the same kind of
extract. With them, entries to read went from 559 to 511, and the list is a better one:

- **French, 46 to 13.** The Wiktionnaire has the gender the English edition lacked (9 genders
  that "differed" were the English edition's gaps: *bureaucratisation*, *erre*) and the second
  way of saying a word (*cutter*, *artefact*). French stress is no longer compared: French
  stresses the end of a phrase, not a syllable of a word.
- **German, 48 to 35**, and they are other entries. 53 leads went, most a second accepted
  stress or a short vowel the German edition writes as we do (*Appetit*, *Artillerie*); 35 came,
  among them real ones no source had shown: *Abteilungsleiter* and *Allgemeinheit* stressed on the
  first syllable where German says *Abtéilung*, *Allgeméinheit*.
- **Russian**: 253 plurals could be compared where one could before, and all agree.
- **Italian**: the edition divides a word into syllables and marks the stressed one (*cà-sa*),
  which is read as its stress: 77 more stresses compared, four more that differ.
- **Spanish and Portuguese: nothing gained.** The Wikcionario confirms what was known. The
  Portuguese edition has a transcription for one word in five, in a notation of its own, so
  Portugal's vowels, the weakest thing in the dictionary, still stand on one witness.

English is held against CMUdict and Britfone as well, and Russian against OpenRussian: 3,600
more English stresses and 4,200 more transcriptions could be compared, agreement rose, and the
stresses that differ rose with it, most of them a compound stressed on either part (*uplifting*,
*citywide*).

A word the entry gives two ways (German *unmöglich*, stressed on either syllable, as Duden prints
it) is one fact, right when either way is Wiktionary's: counting each way by itself had reported
the second stress of thirty German words whose entries were complete and correct. German writes
no stress in its spelling, so an entry states it only in the transcription, by the mark before
the stressed syllable.

How much of what differs is a fault was counted by hand on a random draw of the sounds that
differ, 20 to 40 a language:

- **Russian, all 26 looked at, nearly all faults**, most against the prompt's own rule: /ə/
  for /ɐ/ in the syllable before the stress (*давать* /də.ˈvatʲ/, ten of them), *бывший* with /v/
  before /ʂ/, *тема* with a hard /t/, *никогда* a syllable short, *нечто* with /ʂ/. Two were the
  witness's own fault and are gone: *в* and *с* carry a second transcription for their second
  spelling (*во*, *со*), which is now held against that spelling and not against the first.
- **Portuguese, about three in four**: Portugal's unstressed vowels (*normal* /nuɾ.ˈmal/ for
  /nɔɾˈmal/, *contração* /tɾɐ/ for /tɾa/, *omissão*, *xerife*), which is what the Portuguese
  reviewer found one entry in nine for. The rest are loanwords said two ways.
- **Spanish, nearly all**: the tap and the trill (*carrera* /ka.ˈre.ra/, 22 of the 58), *brócoli*
  with /u/, *corrosión* with /θ/.
- **Italian, about three in five**: open and closed *e* and *o* (*antenna* with /ɛ/), and the
  words stressed on the wrong syllable.
- **French, about one in four** after the habits of notation are taken out: *talisman* ending
  /man/, *hégélien* with /ʒ/.
- **German, one in five, all small**: a short *i* or *u* outside the stress written /ɪ/ or /ʊ/
  (*Minute*, *Limousine*, *positiv*), against the prompt's own rule. The rest is Wiktionary's.
- **English, two or three in forty.** *fathom* /ˈfæð.ɚm/ is a fault; *sentient*, *apparatus*,
  *bipartisan*, *lutz*, *titular* are each said two ways by the dictionaries, of which we wrote
  one and Wiktionary the other. So the English sounds agree as far as this witness can tell, and
  an English or German entry is not listed as worth reading for a sound alone
  (`ACCEPTED_VARIANTS`). A better witness for English is a pronouncing dictionary that lists every
  accepted way (CMUdict for the United States).

**Stress on the wrong syllable**, which no review had named, is the cleanest list: Spanish
*cadena* /ˈka.de.na/, *modelo*, *palabra*, *correo*, *vocablo*; Italian *vendita* /ven.ˈdi.ta/,
*testimone*, *fenice*, *colera*; German *Nominativ*, *Positiv*; English *intently*. About half of
the English and German lines are a word with two accepted stresses of which Wiktionary has the
other (*wahrscheinlich*, *archetypal*); the Spanish and Italian ones are faults nearly to a line.
**Gender**: 34 nouns in 8,469, some of them Wiktionary's gap and not ours (*mar* is said both
ways, *der Weise* is a word of its own).

Two limits. A disagreement is a lead: the lists were read by sample, not line by line. And nothing
is copied: Wiktionary's text is under a share-alike licence, so it is asked and never written into
an entry.

What follows from it, none of it built: a paid patch of the one field for the 511 entries, with
both transcriptions shown to the model (some $15); the Spanish sounds written by a rule from the
spelling in place of a model, since Spanish is regular (Italian is not: where its stress falls and
whether an *e* or *o* is open is not in the spelling, which is exactly where the writers failed);
the Russian rule for the syllable before the stress checked by ingest from the stressed spelling;
and the same witness run on every batch before it is counted, so a wrong stress never reaches a
reviewer.

## The second look

The witness lists entries; until 19 November nobody and nothing read the list. `settle_v4.py` does:
for each listed entry one small call (Opus at medium effort, about $0.03) that shows the model the
word, what the entry has and what each source has, and asks for each fact "change", with the value
in the dictionary's own notation, or "keep", with the reason. Code then decides. A change is set on
a copy of the entry as published and held against the sources again, and stands only if they now
agree. The written entry is not edited: the decisions are files under `out/review-settle/`, and
ingest sets a confirmed one on the published copy while the entry still has the value the decision
was about. A question is paid for once, by its text.

**The trial: 30 entries, 40 facts, $0.84.** 21 facts changed, every one confirmed by the sources;
19 kept.

- Changed, and right as far as I can judge: Spanish *palabra* back on its second syllable,
  *cableado* given its missing syllable, the tap in *representación* and *turbulencia*; Italian
  *moglie* with a closed o; Russian *помочь* and *напротив* with /ɐ/ before the stress; German
  *derzeit* stressed on the first syllable; six Portuguese vowels (*efeito*, *solene*,
  *edificação*).
- Kept, and rightly: *macro* and *série* in Portugal (the final vowel is as the entry has it and
  Wiktionary's is odd), German *einmal* (both stresses), Spanish *zeta* as a masculine noun for a
  patrol car, Brazilian *balneário*.
- **Kept, and wrong: Russian *тема* with a hard т**, defended as "traditional" against three
  sources that have the soft one. The model is of the writer's family and shares its mistakes. So a
  kept fact that two sources or more contradict stays on the witness's list; only one that a
  single source contradicted is closed.
- One change looked needless and was not: French *caméraman* from the plural *cameramen* to
  *caméramans*. Asked a second time, with leave to add a plural beside the entry's, the model
  changed it again, and its reason holds: *cameramen* without the accent is the plural of the
  spelling *cameraman*, not of the headword. A plural can now be added as well as replaced
  ("add"), since the shape holds several: 98 nouns have two, each with its varieties (*centres*,
  *centers*), its senses (*Wörter*, *Worte*) or a note where it has one.
- **Three questions were bought twice ($0.09)**, by a fault of the script's: the sentence that
  lists the differing sounds was built from a set in whatever order a run had, so the same
  question had another text and another identity. The comparison is in a fixed order now, and a
  question is known again by its facts, values and sources, not by that sentence. One of the three
  got the other answer the second time (Brazilian *admirável* with or without the added vowel):
  a second look is not steady on a borderline, and the later answer is the one that counts.

After it the witness lists 494 entries, from 511. The rest of the list is some 480 entries and
$13. What it cannot do is what the witness cannot see: a fault in a field no source states.

## The review loop

The owner's design of 19 November 2026, after the book processor's: writing, a review by another
model, the writer's answer to the review, and a third model where the two do not agree.
`review_v4.py` is that, for one entry at a time.

1. **Read.** A reader of another model family (the Codex plan's, by default `codex:gpt-6-astra`)
   is shown ten entries a call exactly as `measure.py` shows a sample, with the same instructions
   and the same seven faults. An answer GPT gave in a chat counts as a reading: `--sample v410
   --answers v410` takes the findings filed under `measure/v410/answers/`.
2. **Reply.** The writer's model, under the prompt the entry was written by, is shown the entry's
   JSON and the findings. It answers each: *accept*, with the patch that mends it, or *reject*,
   with a reason a third reader can check. Only the faults the bar counts and the split go to the
   writer by default (fact, claim, missing-core, split; `--faults all` for the rest): a reader
   finds a rarer sense missing or a wording too narrow in half the entries it reads, entries
   checked by hand among them.
3. **Rule.** A third model (Gemini 4 Carbon since 20 November, Claude Fable in the first trial) is shown every finding the writer rejected,
   the writer's reason, the entry as a reader sees it and the review file's own definitions, and
   upholds or dismisses each. It is told to dismiss what it could not show from a dictionary.
4. **Mend.** The writer writes the patch for what the third model upheld.
5. **Settle**, no call. Each patch is tried on the entry. It is refused when it cannot be applied,
   breaks the shape (`entry.schema.v4.json`), or moves a fact the sources agreed with to one they
   contradict (`witness.py`). What is left is the outcome, `<headword>.<entry>.outcome.json`.

A patch is a list of operations on the entry's JSON (`entry_patch.py`): replace, add or remove at a
JSON Pointer, every path into the entry as it was before the patch. The written entry is never
edited. Ingest applies an outcome from `out/review-loop/` to the published copy, finding by
finding, only for the written text it was made for and only where each place still holds what the
patch saw; a sense a patch adds gets its id and its frames read like any other, and the entry says
how many faults were mended (`faults_mended`). A trial writes into another folder, which ingest
does not read.

**What is bought, and once.** Every paid answer is one file, named by the entry's content, the
step, and a hash of the question and the model it was put to. A run that is stopped, or that meets
a plan's wall, is started again and buys nothing twice; the wall is a stop of the run, never an
answer. An entry written again is another entry. The other side of that rule cost $1.07 on the
first day: the writer was first shown a field ingest derives (`patterns`), one reply copied it
into a new sense, the field was taken out of what the writer sees, and 23 questions were then
different questions. `--dry` says what the next step would ask before anything is asked.

**The trial**, on GPT's findings for the third sample (`out/review-loop-v410/`): four planted
entries, four hand-checked ones and four ordinary ones a language, 84 entries, of which 32 had a
fault that is looped.

| fault | findings | accepted by the writer | dismissed by the third | mended | patch refused |
|---|---|---|---|---|---|
| fact | 20 | 19 | 1 | 18 | 1 |
| claim | 17 | 16 | 1 | 15 | 1 |
| missing-core | 4 | 3 | 1 | 3 | 0 |

- **Planted faults: 22 of 22 put right** (8 genders, 9 stresses, 5 countabilities; one stress
  with a second correction the reviewer also asked for, *лучше* /tʂː/). The six planted pairs of
  swapped examples are a fault of wording and were not looped.
- **Ordinary findings: the writer took 16 of 19 and argued 3**, and the third model sided with the
  writer on all three, with a reason each time that a dictionary bears out: *beim Fernsehen
  einschlafen* is the verb's infinitive used as a noun, not a sense of *das Fernsehen*;
  *football-mad* before a noun does not make *mad* an attributive adjective; the relation of
  *canto* to *cantar* already names the senses it holds for. What it took reads right: *reaction*
  Count to Both in two senses, *seguramente* with its secondary stress on *gu*, *есть* transitive
  and *съесть* its partner in the eating sense only, *даже* given its conjunction, *province*
  uncountable in *en province*.
- **Two patches refused, both rightly**: a gender written into a sense's grammar, where the shape
  has none, and a grammar note longer than its 120 characters. Their findings stay on the list.
- **Hand-checked entries: none changed.** GPT had no fact, claim or missing core sense in the 28.

**Does the writer agree to anything?** The trial above cannot say: nearly every finding it was
shown was true. So 16 right entries were each given one false finding, the value a planted fault
would have put there asserted as the truth (*Frieden* is feminine; *bag* is uncountable; *schützen*
is stressed on its ending), in six languages. `review_v4.py --decoys v410` builds them. The writer
rejected 16 of 16 and the third model dismissed 16 of 16; no entry changed
(`out/review-loop-decoys/`). These are blunt falsehoods, the kind a dictionary settles at once. A
false finding that sounds like a judgement (a sense said to be missing that is rare, a label said
to be undeserved) is the harder test and has not been made.

**What it costs.** The trial: $6.14 on the Claude plan, the repeat above included. A reply is some
$0.03 once a language's prompt is in the cache and $0.20 for the first call of a language; a
ruling is $0.06 to $0.09. By GPT's rates a quarter of the entries have a fault that is looped and
about one finding in six is argued, so a thousand entries are some 250 replies and 40 rulings:
about $12 on the Claude plan, against $53 to write them, and a hundred calls of ten entries on the
Codex plan for the reading. The writer and the third model share one Claude meter.

**The third seat, measured alone** (20 November 2026). The owner wanted Gemini to rule, so that
the three seats are three families: Anthropic writes, OpenAI reads, Google rules. Gemini's own
chat needs the dearest subscription for its best model, and a ruling is a small call, so the seat
is paid by the call. Two routes are written: `openrouter:<vendor/model>:<effort>`, with the
OpenRouter key the bake-off already used (`OPENROUTER_API_KEY`), and `gemini:<model>:<effort>`,
the Gemini API with a key from AI Studio (prepaid, ten dollars to start; not tried, there is no
key). OpenRouter lists, for a million tokens in and out: Gemini 4 Neon $3 and $15, Carbon and
Argon $2 and $10, Hydrogen $1.50 and $7.50; input read from a cache is a twentieth of the price.

`review_v4.py --judge-test v410` puts a third model to a test of its own. From each planted
fault it makes two cases: the entry with the fault and a finding that names it, and the entry as
written with a finding that asserts the fault. Both carry a rejection from a "writer" in the
same words ("Dictionaries of German give Temperatur as a masculine noun; the entry is right as it
stands"), so only knowing the language tells them apart. Three of each kind a language, 42
rulings:

| third model | true findings upheld | false findings dismissed | a ruling |
|---|---|---|---|
| Claude Fable | 21 of 21 | 21 of 21 | $0.062 |
| Gemini 4 Neon | 21 of 21 | 21 of 21 | $0.011 |
| Gemini 4 Hydrogen | 21 of 21 | 21 of 21 | $0.005 |
| Gemini 4 Carbon, medium effort (14 rulings) | 7 of 7 | 7 of 7 | $0.005 |

Neon was tried first for being the newest and dearest of the four, without its description having
been read: OpenRouter calls it a fast model for coding, Carbon the family's frontier model for
reasoning and Argon, a month older, the flagship reasoning model. Carbon is the one the owner
meant and the one in the seat, at medium effort: at high effort it thought for 25,000 tokens over
one small entry (below). It dismissed the two real disputes the others dismissed and the 16 false
findings, for half a cent to a cent a ruling.

- **Fable does not simply side with the writer**, which its 19 rulings for the writer in the
  trial had left open: it overruled the writer all 21 times the writer was wrong.
- **The test cannot rank the three.** These are facts a dictionary settles at once. On the three
  real disputes of the trial a ruling cost Neon $0.08, as much as Fable: a hard case is thought
  about longer.
- **On the real disputes the two disagreed once, and the fault was the review file's.** Both
  dismissed *Fernsehen* and *mad*. On *canto* (the reader: its relation to *cantar* holds for
  singing, not for the corner) the writer answered that the entry already limits the relation to
  senses 3 to 5, which is true; but `measure.show` printed a relation without its senses, so
  neither the reader nor the third model could see it. Fable dismissed the finding; Gemini upheld
  it, rightly on what it was shown. A relation is now shown with the senses it holds for, and the
  reader's finding would not have been made.
- Gemini dismissed the 16 false findings of the decoy test as Fable had.

The lighter model is not taken for its price: a thousand entries have some 40 rulings, so the
whole difference is cents. OpenRouter holds back the price of a model's longest answer for every
request in flight, and refused four at once with a dollar in the account; the seat now asks for
at most 16,000 tokens. An empty account is a wall like a plan's: the run stops and loses nothing.

**The seats go through the book processor's gateway** (21 November 2026). The loop used to call the
plans' command lines itself, so its calls passed no monthly cap, ignored an account the owner had
paused on the dashboard and left nothing in the spending pages. `review_v4.ask` now hands a plan's
seat (`claude:`, `codex:`, `agy:`) to the book processor's `plan-manage ask`, one JSON request in
and one answer out, which routes it like any step there, checks the provider's cap, picks the open
account and records the call. A hold there (a cap, a pause, a plan's limit) is a wall here: the
run stops and loses nothing. The seat's name is unchanged, so no answer already bought is asked
again. Three things still go to the command line directly, because the gateway holds every answer
to its schema and knows three efforts: a trial writer that keeps entries that do not fit
(`seat_writer.py`), an extra-high effort, and a Claude seat that searches the web.
`LEXICON_DIRECT=1` turns the gateway off; the tests set it.

The third seat is now `agy:gemini-4-carbon:medium`, on Google's plan, and no longer OpenRouter
paid by the call. It is the same model at the same effort; its route is part of the seat's name,
so a ruling bought through OpenRouter is asked once more on the plan when its entry comes round.

**Google's command line starts one at a time.** It reads its login from the system keyring as it
starts. Twice (20 November, four calls at once; 21 November, two rulings at once beside three
other calls) calls that started in the same moment made the keyring answer late, the command line
gave up on it for an hour and every call after was refused as signed out. Five calls ran side by
side for an hour without trouble when they had started apart. So only the start is taken in turn:
both the gateway's provider and `review_v4.agy_run` hold one lock file for the first five seconds
of a call, across every process on the machine.

**Carbon as a writer, a first look.** `seat_writer.py` writes entries with any model in a seat
into a trial folder, under the writers' prompt and held to the schema by the provider. Six
entries, to see that it works and what it costs (`out/review-writer-gemini-4-carbon*/`):

| effort | entries | thinking, tokens | seconds | an entry |
|---|---|---|---|---|
| low | *reaction*, *top*, *тут* | 0 | 15 to 20 | $0.035 |
| medium | *top*, *тут* | 1,000 to 2,700 | 33 to 66 | $0.045 to $0.061 |
| high | *top* | 25,400 | 320 | $0.29 |

All six fit the schema and the witness contradicts none. At low and medium nearly all of the price
is the prompt, 15,000 tokens read at full price each time: through OpenRouter nothing was cached,
and cached it would be one to three cents an entry against the $0.053 of Opus. At high effort the one entry
is as full as the best of Opus's three (*top* with its three nouns and two adjective senses); at
low *top* has no adjective and *тут* gains the mulberry tree, and at medium *тут* is one lexeme.
Six entries say that the seat works, not how good the writer is; and these six were written
held to the schema by the provider, which the hard cases below showed to be what made them thin. A model that writes cannot also rule: with
Carbon writing, the third seat goes back to another family.

**Hard cases, and what they said of four writers** (20 November 2026). The owner asked that
money not be spent on 280 more ordinary words: a writer should be tried where writers differ.
`hard_cases.py` holds 84 words, twelve a language, chosen before any writer saw them because an
entry for them is easy to get wrong, and for 80 of them a key that a program checks: a homograph
has its two lexemes, said or gendered differently (*lead*, *Band*, *ancora*, *замок*); an
inflected form is refused or has only the lexemes it is the lemma of (*went*, *été*, *vino*,
*стали*); a noun has its odd gender or both its plurals (*E-Mail*, *Wort*, *uovo*, *год*); a verb
is impersonal or takes both auxiliaries (*falloir*, *monter*); a sense of one country says so
(*pants*, *septante*, *coger*); a rude word is labelled (*con*, *bloody*); a weak form has its
context (*the*). Four small words have no key and are for a reader.

| writer, all under v4.11 | words wholly right | senses written | seconds, median | cost |
|---|---|---|---|---|
| Opus 5.5, high effort | 79 of 80 | 296 | 20 | $6.78 (plan) |
| Fable 5.2, high effort | 78 of 80 | 320 | 25 | $14.91 (plan) |
| Gemini 4 Carbon, medium, schema in the request's words | 77 of 80 | 272 | 46 | $5.09 |
| Gemini 4 Carbon, medium, held to the schema by the provider | 65 of 80 | 217 | 43 | $4.81 |

- **Held to the schema by the provider, Carbon wrote stubs**: *Bank* without the bench, *cura*
  without the priest, *Wort* with one sense and no gender. That was the setup, not the model.
  With the schema given in words and the answer checked here (the writer's default since), the same
  model at the same effort is level with Opus on the key, and all 84 entries fit the schema. The
  control was run only because the stubs looked too poor for a frontier model; the first verdict
  would have been wrong.
- **High effort is not the way**: on the 15 words it had missed, Carbon at high effort (strict)
  mended six, at $0.22 to $0.39 and five to eight minutes an entry, with two entries outside the
  schema and one answer lost at the token ceiling.
- **Argon and Neon** were tried only on those 15 words, strict: 12 and 11 of 16. Words chosen by
  one model's failures cannot rank another, and strict was the fault, so this says nothing yet.
- **The key cannot rank the top three.** 79, 78 and 77 are one or two words apart, and the one
  word all three miss is the key's strictness or a real gap in all (*fato* as "fact" is Brazil's
  spelling and none says so). What separates them, if anything does, is the senses and the
  wording, and that is a reader's work: `measure/hard/review/` holds each word's two entries, by
  Opus and by Carbon, side by side and unnamed, for GPT to choose between (`hard_cases.py --pairs`,
  `--tally`).
- **The reader's blind choice: a tie.** GPT (Astra 6.2, the web open) read 76 words, each with
  the two entries side by side and unnamed, the sides shuffled: Opus the better in 38, Carbon in
  37, the same in one. It leans to whichever entry it reads first (44 of 75); with the two orders
  weighted alike Opus is the better in 52%. By language: English 9 to 1 for Opus, Russian 7 to 3
  and French 8 to 3 for Carbon, German, Spanish and Italian 6 to 5 for Opus, Portuguese 6 to 5
  for Carbon; ten or twelve words a language carry the English result at most. The faults it
  named differ by writer more than the choice does: in Opus's entries 37 claims, 60 wordings and
  35 collocations, in Carbon's 28, 45 and 14; rarer senses missing 62 in Opus's and 79 in
  Carbon's. Opus writes more and is faulted for what it writes; Carbon writes less and is faulted
  for what it leaves out.
- **So Opus is not the only writer.** On words chosen to be hard, under a prompt tuned on Opus, a
  model of another family at medium effort is its equal by a key and by a blind reader of a third
  family. What that buys is a second meter to write on and a second family to turn to, not better
  entries. An entry Carbon writes is not ruled on by Carbon.
- **Whose words these are.** They were chosen and keyed by a Claude model, and the prompt was
  tuned over eleven versions on what Opus wrote. The key once favoured Opus's way of writing a
  fact (a region as a country where Carbon wrote the UN area) and was corrected. A set chosen by
  the reader's family or by Gemini's, with its own key, is the check on that.
- Sol and Astra wrote entries in October and early November (some 700 and 5,200, Astra 679 in the
  present shape), under older prompts and judged by blind pairs then; neither has met this set.
  `seat_writer.py --seat codex:<model>:<effort> --headwords hard --out review-hard-<name>`
  on a machine with the Codex plan, then `hard_cases.py --score`, is the whole test.

**Google's plan, first readings** (20 November 2026, AI Ultra at $200 a month, the 20x tier). The
plan is called through the Antigravity command line, `agy -p`, which is the seat
`agy:<model>:<effort>` here and the provider `antigravity` in the book processor (its meter is on
the dashboard with the other plans'). `agy models` lists Gemini 4 Carbon, Argon, Neon and
Hydrogen at low, medium and high, and on a meter of their own Claude Opus 5.5 and Sonnet 5.5;
`agy -p /usage` prints the week and the five-hour session left, and costs nothing.

| Carbon on the plan, 24 hard words (English, German) | right by the key | senses | seconds, median | thinking a word | list price |
|---|---|---|---|---|---|
| medium | 24 of 24 | 88 | 180 | 14,000 tokens | $4.61 |
| low | 23 of 23 | 95 | 16 | none | $1.31 |
| (the API's medium, the same words) | | 89 | 50 | 1,000 to 3,000 | $1.46 |
| (Opus, the same words) | | 98 | 21 | | $0.49 |

- **The plan's medium is not the API's medium.** It thinks five times as long and is nine times
  slower than Opus. The plan's **low** thinks not at all, is as fast as Opus, as right by the
  key and as full. Low is the setting to try as a writer; whether its wording holds needs the
  blind reader, on all 84 words.
- **Every call carries some 9,000 tokens of the CLI's own instructions**, and almost none of the
  input is read from a cache: an entry is 22,000 input tokens where the API's was 15,000.
- **The meter, read over 276 entries** ($22.4 at list price, every run of the day): the week
  went from 100% to 96% left, the five-hour session from 99% to 90%. So a week holds some $560
  of list price and **one five-hour session some two fifths of the week**. A week of the plan
  costs $46: about twelve list-price dollars for one, the Codex plan's figure, where the Claude
  plan gives twenty. At low effort that is some 9,000 entries a week. (The first 24 entries
  alone had read three dollars for one: the meter moves in whole points and late.)
- **All 84 hard words at low effort**, by the key, with Opus beside them:

| writer | words wholly right | senses | seconds, median | outside the schema |
|---|---|---|---|---|
| Opus 5.5, high | 79 of 80 | 296 | 20 | 0 |
| Carbon, low | 77 of 80 | 287 | 12 | 4 |
| Argon, low | 77 of 80 | 266 | 13 | 2 |
| Neon, low | 78 of 80 | 261 | 25 | 0 |

  The three are level by the key and with each other; Carbon writes the most. What falls
  outside the schema is a key in the wrong place (`relations` inside a sense, `by_variety`
  inside a sense's grammar), which ingest could move or a second call mend. Carbon at low is the
  one to put before the reader: `measure/hard-low/review/` has its entries beside Opus's,
  unnamed (`hard_cases.py --name hard-low --tally` counts the answers).
- **The blind reader on Carbon at low: Opus is the better.** GPT Astra 6.2 at extra-high effort,
  this time through the Codex command line with the web open (`codex:gpt-6.2-astra:xhigh:search`;
  seven files, seven to nine minutes each), read the 76 pairs: Opus the better in 47, Carbon at
  low in 29, none the same; with the two orders weighted alike Opus in 63%. Opus leads in every
  language but Russian (5 to 5) and most in German and Italian, and takes every rude word and
  every word said two ways; homographs are 8 to 8. Carbon at low has more facts wrong (19 to 13),
  more core senses missing (13 to 8) and more collocations faulted (47 to 35). At medium through
  the API the same model had been level with Opus, 37 to 38: the thinking is what the entries
  need, and low is not a writer of the dictionary. The plan's medium (fourteen thousand tokens
  of thinking, three minutes an entry) is the setting still to read.
- **The blind reader on Carbon at medium on the plan: near Opus, not past it** (21 November).
  All 84 words written (three minutes an entry on average, the longest eight; 77 of 80 by the
  key, as at low: the key does not tell the efforts apart). The same reader read the 76 pairs
  (`measure/hard-medium/`): Opus the better in 42, Carbon at medium in 34; with the two orders
  weighted alike Carbon in 44%. A gap that size in 76 words is within chance, as the 38 to 37
  through the API was. The two are different writers: Carbon takes word forms (7 to 3),
  homographs (10 to 6), particles (4 to 1) and Russian and Italian; Opus takes nouns (13 to 5),
  verbs (7 to 1), rude words (6 to 1) and English and German. The reader names fewer faults in
  Carbon's entries of every kind (facts 11 to 16, claims 17 to 48, wording 48 to 78) but one:
  core senses missing, 15 to 9. Carbon writes the shorter, safer entry and leaves more out.
  Opus stays the dictionary's writer; Carbon at medium on the plan is a second writer that
  could carry a language when the Claude plan is short, at three times the seconds an entry.
- **The login is fragile under load.** With four calls at once the system keyring answered later
  than the CLI's five seconds; the CLI turned to a token file for an hour, the token there
  expired, and every call after was refused as not signed in. Both seats now treat that as a
  stop and not as the word's failure. Two calls at once until it is understood.
- The CLI takes no system prompt, no instruction to use no tools, and the request as one
  command-line argument (120,000 bytes at most; the writers' prompt is 35,000).

**Who may look things up.** The readings of the trial came from the ChatGPT app, which searches
the web; the writer and the third model answered from memory, and when they name a dictionary
("Cambridge marks it [after verb]") they are recalling it. A seat ending in `:search` may search:
for the Codex command line that is `web_search="live"`, and the instruction to answer from its
own knowledge, which the seat carried on the first day, is replaced by one to look the words up;
for the Claude command line it is the web search tool. The reader's seat has it by default,
because a reader from memory is not the reader that found 52 planted faults of 52. The third
model can be given it (`--judge claude:claude-fable-5-2:high:search`): tried on one finding, it
looked the word up in four learner's dictionaries and ruled as it had from memory, for $0.48
against $0.08. With some 40 rulings in a thousand entries that is $20 more a thousand, and worth
it only if a count shows the third model ruling wrongly from memory; on the blunt test above
none did. A seat with search is another seat: its answers
are filed apart from the same seat's without.

**What it does not do.** It mends what a second model sees, and the two readers of the third
sample seldom named the same fault: a second reading finds other things. (Part of that gap is not
the model: GPT read with the web open and Fable from memory.) It does not make the
reader steadier: the same reader found something in 11 and then in 18 of the same 35 entries
("The evidence trial"). And nothing has yet been run through the Codex command line: the seat is
written after `codex_writer.py` and tested with a stand-in, and its first real batch should be
ten entries with `--steps read`.

## Russian

Russian has been a target language since 18 November 2026, after the trial this section records.
Almost all of its settings were there from Ukrainian: a stress reading on every pronunciation and
form, the cases, the aspect partner as a relation. Russian's own:

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

A form used only after numbers is stored as a form with `Number=Count`, the name Universal
Dependencies gives it (*пять челове́к*, *два часа́*, *пять лет*), beside the plural a learner would
build (*люди*, *людей*). The pilot's *человек* was written before that rule and says it in the
form's note instead.

Written since, to show what no entry had shown: *ты* (familiar) and *вы* (polite), *час* with *часа́*
as its form after numbers (`Number=Count`), *год* with *лет*. One of them wrote the stress into the
forms themselves (*го́да*); ingest now moves such a mark to the reading.

The 250 plain common words of `headwords/top-ru-ru.json` are written (on the owner's machine,
under v4.6, $8.80): all valid, three rightly refused (*вряд*, *следующее*, *дано*). Of 1,073 stress
readings six disagree with Wiktionary, and at least two of those are wrong outright
(*рассто́яние* for *расстоя́ние*, *о́тчасти* for *отча́сти*): the witness finds what no reviewer
would be asked to.

Eighteen candidates went to a reviewer: one passed, twelve were corrected, five refused. Russian is
shown *огромный*, *чемпионат*, *обеспечить* (three senses told apart by what the verb governs),
*возле* (preposition and adverb), *подарок* (a figurative sense). What the review settled:

- **The sounds stay as they are**, broad Moscow IPA beside the stressed spelling, with two rules
  the candidates had applied unevenly now spelt out: unstressed и is always /i/ and never /ɪ/, and
  two unstressed vowels side by side before the stress are both /ɐ/ (*соответственно*).
- **Three kinds of form after a number, not one.** `Number=Count` is for a form of its own after
  два, три, четыре (*два часа́* beside *о́коло ча́са*); *два стола́* is the ordinary genitive and has
  none. *Пять челове́к* and *пять лет* are genitive plurals used after numbers, listed beside the
  ordinary one with a note. Each such form says in its note when it is used, the second locative
  too (*лесу́*: after в and на), because Universal Dependencies gives the ordinary prepositional
  case the same tag.
- **Also listed**: the short form of address (*пап*, `Case=Voc`), the accusative whose stress
  moves (*во́ду*), a past neuter or plural whose stress could not be guessed.
- A noun gives its declension and a verb its conjugation as school grammar numbers them.
- **A frame is what the word governs**, written with the question words (*искать кого/что*,
  *чемпионат по чему*), not one collocation filled in (*чемпионат мира*).

Eight words written again under these rules and the examples came back as asked: *шаг* with
*шага́* after two to four and *на каждом шагу́*, *стол* with no count form, *папа* with *пап*.

With that Russian had what a target language has, and on 18 November 2026 it became one: it is in
`TARGET_LANGUAGES`, so the writers, the audit and the status take it by default, and its v4.7
prompt is pinned like the others'. The 304 entries of the trial saw three earlier texts, under the
names v4.6 and v4.7; each carries the hash of the one it saw, which tells a trial entry from a
later one.

Its list is `headwords/healthy-ru.json`, 15,570 words, made as every language's is (see "Healthy
lists"). Three things in it are Russian's own:

- **ё.** Print has *ее* and *елка*, Wiktionary files those as spellings of *её* and *ёлка*, and a
  frequency list counts them apart. The word with ё is queued, with the frequency of both; the
  spelling with е is not, because the entry lists it. A form printed without its ё (*идет*) is a
  form of its word like any other.
- **Letters and abbreviations.** *г*, *см*, *км* are frequent and Wiktionary has them as words. A
  word without a vowel is left out, and a single letter unless it is one of the one-letter words
  (*а*, *в*, *и*, *к*, *о*, *с*, *у*, *я*).
- **Words that are forms as well** are in the list. In Russian they are the first words a learner
  needs: *как*, *это*, *для*, *уже*, *хорошо*, each of them also filed as a form of something
  (*для* of the verb *длить*).

```bash
scripts/.venv/bin/python scripts/lexicon_research/claude_code_writer.py --model claude-opus-5-5 --effort high --langs ru --headwords healthy --workers 4 --limit 1000
```

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

The lists `core-de`, `core-fr`, `core-es`, `core-it` and `core-pt` left every such word out, and
with them the commonest words of each language, which are nearly all a form of something as well:
*der*, *das*, *es*, *sich*; *la*, *des*, *une*, *dans*; *los*, *para*, *como*; *mi*, *gli*. Outside
English the writers refuse such a word once in fifty, so the healthy lists hold them (next section).

## Healthy lists

A refusal is not free. Over the 32,500 words the Opus writer was given up to 18 November 2026 an
entry cost $0.0294 and a refusal $0.0137, half an entry, and the queues held a good deal that could
only be refused or was not worth an entry. `lemma_index.py` now keeps more of what Wiktionary says
of a spelling, and the lists are made from it.

What the index learned (it is rebuilt from the same extracts, `lemma_index.py`, about 9 GB read):

- **Names.** A sense from a proper-noun entry is an N when its gloss says a person, a place below a
  country or a firm (*Renate*, *Leverkusen*, *Google*), and stays a word when it is a month, a
  feast, a language or a country (*October*, *Christmas*, *Germany*). A spelling whose first sense
  is such a name is marked too (*David* the name before *a David*, an underdog).
- **Forms that Wiktionary gives no entry.** The inflection table of every lemma is read: 1.2
  million more forms, most of them Russian, Italian and Spanish (*российской*, *fertiggestellten*).
- **Forms it describes in words only**: Italian *possiamo* ("first-person plural present of
  potere"), *farlo* ("compound of the infinitive fare with lo"), Portuguese *fizeram*. 16,000
  Italian and 5,900 Portuguese spellings had passed for lemmas.
- **The parts of speech** of a spelling's own senses, and whether every one of them is marked old,
  rare or slang (French *robert*, frequent as a name).

The gate uses the first three for every writer and, new, for the mender: a headword that is only
such a name is stopped unless the writer is run with `--names`, and a word a frequency list wrote
in lower case is found under its capital (*american*, and *россии*, a form of *Россия*).

How often each kind was refused, of the 30,400 words of the six languages:

| what the gate says of the word | given | refused |
|---|---:|---:|
| a word of its own | 21,093 | 3% (English 6%, the others under 1%) |
| a word that is an inflected form as well | 6,708 | English 46%, the others 2 to 6% |
| not in Wiktionary | 1,021 | 67% |
| an alternative spelling only | 889 | 51% |
| only old, rare or slang senses, beside a name or a form | 312 | 62% |
| a proper name | 247 | 61% |
| an inflected form (now known) | 149 | 72% |

`core_list.py --healthy` writes two lists a language from every unwritten word of every list, every
spelling at Zipf 3 or more and the base words of the forms among them, which get their forms'
frequency (so *быть* and *мочь* stand where *был* and *может* did):

| | `healthy-<lang>` | `doubtful-<lang>` | names dropped |
|---|---:|---:|---:|
| en | 28,509 | 9,165 | 2,573 |
| de | 10,680 | 8,309 | 2,912 |
| fr | 10,802 | 3,840 | 857 |
| es | 7,384 | 4,093 | 982 |
| it | 9,760 | 5,680 | 1,004 |
| pt | 7,880 | 5,542 | 913 |
| ru | 15,570 | 5,335 | 1,566 |

- **healthy**: a word of its own that is not only a name, a spelling or a piece of a word. Outside
  English also the word that is a form as well, which is where the commonest words are (*der*,
  *la*, *para*, *как*, *это*); the five core lists had left those out. In English such a word is
  kept only if it is met more often than its base word (*data*), has a verb of its own (*saw*,
  *felt*) or two parts of speech (*dancing*): refused 16%, 0 to 7% and 26 to 30%, where the rest,
  one adjective or noun beside the form (*viewed*, *fines*), was refused 55 to 67%.
- **doubtful**: what was refused a quarter of the time or more and may still be a word, each row
  with its reason. Not to be run as it is; German's holds compounds Wiktionary lacks, of which the
  writers wrote half.
- **dropped**: names, forms (the gate would stop them, and their base word is queued instead),
  letters, abbreviations, pieces of words.

A German noun whose lower-case spelling is only a form of a commoner verb (*Habe*, *Muss*, *Hast*)
stays, at Zipf 3.5 instead of the form's frequency; *Tag* keeps its own. A word is written under
Wiktionary's spelling: German nouns with their capital, *American* with its.

Of the 108,500 unwritten words the old lists held, 80% are fit; 9% are forms, 5% spellings, 3%
unknown to Wiktionary, 2% names. Leaving out what the old gate would have let through is some
13,000 calls. Of the mender's 48,500 older words the gate now stops 5,600 before a patch is paid
for: 4,300 forms and 1,200 names that the older writers had been given.

`healthy-<lang>` replaces `core-<lang>`, `core-forms-<lang>` and what is unwritten of `v4-new`,
`night*` and the English deep lists. Japanese has no such list: its frequency list needs a reading
first (see "Japanese, on trial").

```bash
scripts/.venv/bin/python scripts/lexicon_research/lemma_index.py          # once, to have the new index
scripts/.venv/bin/python scripts/lexicon_research/claude_code_writer.py --model claude-opus-5-5 --effort high --headwords healthy --workers 4 --limit 2000
```

### The older words, to be written again

`core_list.py --rewrite` lists the words whose only entry is of an older shape, through the same
gate as a new word and whatever their frequency (they were chosen once): `rewrite-<lang>.json`,
most frequent first, and `rewrite-doubtful-<lang>.json`. An older entry for an inflected form sends
its base word. Of some 50,000 older headwords, 5,060 have a schema-4 entry already, 1,650 are names,
forms or abbreviations a writer would refuse, 3,460 are doubtful, and 39,902 are to be written:

|    | to write again | doubtful |
|----|----------------|----------|
| en | 11,579 | 913 |
| de | 7,082 | 750 |
| fr | 8,395 | 1,071 |
| es | 6,049 | 334 |
| it | 3,666 | 186 |
| pt | 3,131 | 206 |

At $0.041 an entry that is about $1,640. They are run with `claude_code_writer.py --headwords
rewrite --rewrite-older`; without the flag the writer still leaves a word with an older entry
alone, so that no list spends this by accident.

### The first night, 18 November

One window of the plan, at the owner's word, in a cloud session: the six rewrite lists and
`healthy-ru`, most frequent first, two workers a language, 2,700 entries an hour. It ran fifty
minutes and ended itself when the plan refused every call ("You've hit your 5-hour limit"), with
nothing lost: the writer now takes `--stop-file`, finishes the calls in flight and makes no other.

|    | written | refused | list price |
|----|---------|---------|------------|
| en | 263 | 0 | $13.08 |
| de | 251 | 0 | $13.23 |
| fr | 267 | 0 | $13.42 |
| es | 296 | 4 | $13.63 |
| it | 271 | 0 | $13.27 |
| pt | 250 | 0 | $13.18 |
| ru | 226 | 6 | $12.42 |

1,824 entries and 10 refusals for $92.22, every entry valid; $0.050 an entry, more than the
$0.041 of a running batch because these are the most frequent words (3.5 senses an entry) and
fourteen calls opened the prompt cache twice. A refusal in 180 is what a healthy list was made
for. 3.5 collocations a sense, 0.7 frames.

Held against Wiktionary by themselves, the night's entries differ less than the 31,917 before
them: in sounds 1.6% against 4.0% in Portuguese, 1.0% against 6.1% in Russian, 0.6% against 1.9%
in Italian; in stress none of 704 in Spanish against 0.4%. That is not the prompt, which says
nothing new about sounds: these are the commonest words of each language, whose sounds a writer
knows best. German stress differs more, 13 of 242 against 2.5%, every one a word with two accepted
stresses of which Wiktionary has the other (*unmöglich*, *selbstverständlich*, *davor*).

Left to write again: some 38,300 words. The run is `night/run.sh` of that session in outline:
one writer a language with `--stop-file`, a commit and a push of the data branch every ten
minutes, and an end at the wall, at five hours, or at 1,500 words a language. The usage meter the
writer pauses on (`/usage`) shows nothing in a cloud session, so there a run goes to the wall.

## Frames

A frame is what grammar calls government, or valency: what a word needs around it before it can be
used, a preposition, a case, an infinitive, a clause. *Depend* needs *on* and a thing, *warten*
needs *auf* and an accusative, *платить* a dative and *за* with an accusative. A learner's
dictionary prints it as the word with its open places, and every language's dictionaries have
their own way of writing an open place. Ours:

| | a person | a thing | other open places |
|---|---|---|---|
| en | sb | sth | sb's, do sth, doing sth, that ... |
| de | jmd. jmdn. jmdm. jmds. | etw. | + Akk, + Dat, + Gen |
| fr | qqn | qqch | inf |
| es | alguien | algo | inf |
| it | qcn | qcs | inf |
| pt | alguém | algo | inf |
| ru, uk | кто, кого, кому, кем, ком | что, чего, чему, чем, чём | куда, где, инф. |
| ja | 〜 | 〜 | the particle is written: 〜に, 〜を, 〜が |

The Russian and Ukrainian question words do more than the others' abbreviations: *кого* against
*кому* says the case, where English *sb* says nothing and German has to add *+ Akk* or decline the
abbreviation (*jmdn.*, *jmdm.*).

A frame is a string, a few to a sense, and until v4.8 the prompt had only ever said how to write one
by example: five examples up to v4.7, a sentence each in the notes for Russian and Japanese,
nothing for French, Spanish, Italian or Portuguese. The 36,300 frames of the schema-4 entries
(18 November 2026) show what that gave:

| | frames | "something" as | "somebody" as | no open place |
|---|---:|---|---|---:|
| en | 13,107 | sth 100% | sb 91%, sb's 9% | 35% |
| de | 3,513 | etw. 71%, etwas 19%, etw 9% | dative: jmdm. 36%, jdm 30%, jemandem 25%, jdm. 9% | 32% |
| fr | 5,082 | qqch 95%, quelque chose 3%, qch 2% | qqn 95%, quelqu'un 4% | 41% |
| es | 3,787 | algo 100% | alguien 98%, algn 2% | 36% |
| it | 3,874 | qcs 56%, qc 27%, qualcosa 13%, qlco 5% | qcn 69%, qualcuno 14%, qn 13%, qlcu 4% | 37% |
| pt | 3,257 | algo 100% | alguém 100% | 32% |
| uk | 2,749 | question words | question words | 23% |
| ru | 757 | question words | question words | 27% |
| ja | 177 | 〜 | 〜 | 21% |

Two things were loose, and both are settled.

**How an open place is spelled.** Four languages had settled on one spelling by themselves; German
and Italian took the abbreviations of three or four dictionaries. Ingest rewrites the abbreviations
to the table's (`settle_frames`, free, 9.6% of German frames and 13.3% of Italian ones), with
German on Duden's, which the reviewed German examples already use, and from v4.8 the prompt states
the table. A full word (*etwas*, *qualcosa*) in an entry of before is left as it is: it can be a
word of the phrase (*etwas dagegen haben*), and no rule tells that from an open place.

**What counts as a frame.** A third of the frames had no open place at all. They were collocations
(*have an idea*, *eine Geldstrafe zahlen*), fixed phrases (*as soon as possible*, *in the lead*) and
names made of two words (*site archéologique*, *tienda de campaña*), and the prompt had asked for
them. From v4.8 they are the sense's collocations, in new entries by the prompt and in the old ones
by ingest: see "Prompt v4.8".

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

### Japanese, on trial

Japanese has its settings, a pilot of 48 words (`headwords/pilot-ja-ja.json`) and 250 common
words (`headwords/top-ja-ja.json`), all written. What was decided for it before the pilot:

- The pronunciation is a reading in hiragana with `notation: kana`, and no IPA; the pronunciation
  section of the prompt is exchanged whole for a language named in `prompting.PRONUNCIATION_V4`.
  Pitch accent is left out.
- Limits count characters (`prompting.LIMITS_V4`, `check_v4.CHARACTERS`): a definition 10 to 35,
  an example 12 to 40.
- The lemma is written as dictionaries head it, kanji with okurigana or kana; another way of
  writing it is a spelling with its script (Jpan, Hira, Kana) only when print uses it; a character
  of before 1946 is a pre-reform spelling.
- A reading that changes the meaning makes another lexeme (*辛い*: からい, つらい); readings with one
  meaning are two pronunciations (*明日*).
- `conjugation` takes godan, ichidan, irregular for a verb and i, na for an adjective (new in the
  schema); a transitive verb and its intransitive partner name each other; a noun's counter is its
  `classifier`; honorific, humble, polite and familiar are politeness labels; a particle is an entry.
- The pilot was written with `--no-gate`. Since 18 November 2026 Japanese has a gate like the
  others (`lemma_index.py --langs ja`), in which a word in kana that Wiktionary only sends on to
  its spelling with kanji counts as a spelling. A reader taps a run of characters that the book
  processor must first cut into words, and its lemma is what reaches the dictionary.

The pilot is written (48 words, $2.13): all valid, and the audit finds nothing. Every one of 71
pronunciations is a kana reading and every one of 52 spellings names its script. *辛い* came back
as two words, からい and つらい, *開く* as あく and ひらく with *開ける* as its transitive partner,
*今日* as きょう and こんにち; *は* is read わ; *国* lists *國* as the form of before the reform;
nouns carry their counters (*鉛筆* 本, *犬* 匹, *本* 冊) and *本* is a counter of its own;
*いらっしゃる* is honorific, *参る* humble, *俺* familiar; *おおきに* names Kansai in a note.
Nothing in the shape had to give.

A reviewer read twenty-four of the entries and answered seven questions about the decisions.
Sixteen entries could be used after correction, eight needed writing afresh, none as it stood:
the shape held and the content did not yet. The basic verbs lacked senses (*行く*, *する*, *来る*
without their uses after the て form), some definitions said two things, a prefix had been made a
determiner. Japanese is shown *鉛筆*, *好き* (adjective and noun), *食べる*, *辛い* (two words by
reading, one sense of the west of Japan), *俺*. What was decided:

| Asked | Decided |
|---|---|
| Is a kana reading enough? | It stays, and beside it the pitch accent of Tokyo as the number dictionaries print: the mora after which the pitch drops, 0 for none (*箸* 1, *橋* 2, *端* 0). `accent` on a pronunciation, new in the schema, given only when the writer is sure. Romanization is not stored: code makes it from the kana. |
| Which spellings? | As written: dictionary forms with their script, kana spellings that print uses, old character forms flagged. A reading is tied to its lexeme or sense where kanji are shared. |
| Which grammar? | The five classes stay. Added: `suru` for a noun that makes a verb with する, on the word or on the senses it holds for; a use after the て form with a meaning of its own is a sense; a word that only stands before a noun (*大きな*, *来る* read きたる) is an adjective, attributive and invariable; a prefix or suffix is not a lexeme; the に form of an adjective is not an adverb, but *好き* taking を is a noun; Japanese nouns take no countability. |
| Are the counters right? | Yes. The usual one is the `classifier`, another unit's goes in the note, and a counter is a lexeme whose forms give the readings that change with the number (*一本* いっぽん, *三本* さんぼん). |
| The four politeness labels? | They stay, on the sense. The finer division of the humble into two kinds is left to a note. |
| JLPT beside CEFR? | Not added. There is no official list by level since 2010, and a level guessed for every sense would be a claim without a source; it can come from a list by code if wanted. |
| Limits in characters? | They stay, as a check and not as a style: a definition is one predicate or one noun phrase, never two sentences. Punctuation counts. |

Eleven words written again under these rules and the examples: all valid; nine readings in ten
carry an accent (*橋* 2, *する* 0, *開く* 0 as あく and 2 as ひらく), *大きな* and *来る* read
きたる came back as attributive adjectives, *本* as a noun and a counter with no determiner.

**The 250 common words** (18 November 2026, $13.09, five cents an entry): all valid, none refused,
338 lexemes and 861 senses, and the audit finds seven things ingest puts right and nothing else.
The list was read out of a frequency list that is not one of words: wordfreq's Japanese is what
MeCab cut the text into, so `core_list.py` counts each piece under its dictionary form (*なっ*
under *なる*), keeps what Wiktionary has as a word, and writes a word spelled both ways once
(*こと*, not also *事*). What came out still had suffixes (*者*, *性*, *的*) and pieces MeCab did
not know near the top, which no rule tells from words; the 250 are the top of it after a reading.
A list of thousands needs that reading done by a model or by the writer's own refusals.

**The pitch accent has a witness.** Wiktionary's extract does not carry the Tokyo accent, but
Kanjium's table does, for 124,000 words (`check_v4.accent_witness` says where to get it; it is not
committed and nothing is copied from it). Of the 415 readings of the 298 entries, 253 carry an
accent, the table knows 249 of those, and all 249 agree: 194 words written with kanji against
their own row, 55 written in kana against every word read so, which is the weaker test. The 162
without one are the pilot's 71, written before the rule, and 91 the writer was not sure of.

`measure/ja-top/review/ja-1.md` and `ja-2.md` hold a hundred of the 250, drawn at random, for a
reader of another model family to count the six kinds of fault in, as was done for the six
languages. The review file says what is Japanese's own, and shows the accent.

**The count** (`measure/ja-top/answers/`, 18 November 2026). Of a hundred entries fourteen are
clean; four have a fact wrong, sixteen a claim too strong, twenty-one lack a core meaning: all
three over the bar (2, 5, 10), and Japanese stays on trial. The hundred are the commonest words of
the language, the ones with most meanings, where the six languages were measured on four bands of
frequency, so the two counts are not one scale. What is behind them:

- **Missing core, 21.** Seven are a suffix, prefix or auxiliary use of the headword (*目* in
  *二回目*, *方* in *読み方*, *人* in *日本人*, *上* in *法律上*, *前* in *前首相*, *そう* in *降りそう*,
  *よう* in *始めよう*), which the rules had left out on the first review's advice that an affix is
  not a lexeme. Six are another word or a fixed combination (*鳴る* under *なる*, *遭う* under *会う*,
  *誰か*, *何か*, *かもしれない*). Eight are meanings an entry should have had (*顔* as looks,
  *悪い* as at fault, *の* in *のだ*).
- **Claims, 16.** Eight are grammar notes that state a rule where a tendency is true (*まま* "after
  a modifier", though *ままならない*); two are old characters called pre-reform that are still
  written in a corner of the language (*氣*, *圓*); two are labels.
- **Facts, 4.** The accent of adverbial *そう*, which the witness let through because a word in kana
  is held to every word read so; *〜か月* listed under the reading つき; a part of speech; a valency.

v4.9 answers the first two. Where the headword is a word, its use as a suffix, a prefix or an
auxiliary is part of its entry: a sense when the reading is the same, a lexeme of its own when it
is read differently or is another part of speech, with a frame that shows what it attaches to.
Six of the words written again under v4.9, in scratch: *目* has "数＋回＋目", *方* has かた with
"動詞の連用形＋方", *人* has じん and にん, *上* has じょう, and *そう* and *よう* have their auxiliaries.
And a note says what is usual as usual. The 250 entries of v4.7 keep what they have; the words
among them that lack such a use are a list to write again.

### Running a trial language

On a machine with the Claude CLI logged in to the account that is to pay:

```bash
git pull origin develop
# the gate of a language is built once: 950 MB to read for Russian, 370 MB for Japanese, a few minutes each
scripts/.venv/bin/python scripts/lexicon_research/lemma_index.py --langs ru,ja
scripts/.venv/bin/python scripts/lexicon_research/claude_code_writer.py --model claude-opus-5-5 --effort high --langs ja --headwords top-ja --workers 4
scripts/.venv/bin/python scripts/lexicon_research/lexicon_data_sync.py
```

The sync pushes the entries to the data repository, where any session reads them. A language named
with `--langs` is written whether it is on trial or not; only the default leaves the trial ones out.

## The pass over finished entries

Designed in November, before the witness existed. Where each step stands on 19 November:

1. **Cite** (paid, small): for each sense, the Wiktionary senses it covers. *Not built for schema
   4.* The evidence trial shows the writer the same senses without asking it to cite them.
2. **Facts from the sources, by code**: gender, plural, forms and IPA compared, a patch call only
   where they disagree. *Built, another way*: `witness.py` compares without citations and
   `settle_v4.py` is the patch call. Still to do from the design: regions with their extent, the
   forms other than the plural, and a count of common Wiktionary senses an entry lacks.
3. **Inside a language**: reverse relations by code, an antonym resolved to the sense it opposes,
   synonyms from Wiktionary's per-sense lists when both words have entries. *Not built.* The
   entries carry the material: 18,786 relations (two in five are "derived from") and 9,719
   antonyms, each naming a lemma and its part of speech. Today 42% of the relations and 40% of the
   antonyms name a word that has an entry; the rest wait for their word to be written.
4. **Across the languages** (paid, small): Wiktionary glosses a German word with linked English
   words, so the candidates are free; a call picks the sense in the English entry. A translation is
   a link where the other entry exists and plain text where it does not. *Not built.* WordNet and
   the Open Multilingual Wordnet, which link senses across languages by concept, are not loaded.
5. **Reader-language glosses**: deferred. Old ones are kept under `out/carry/`.

What runs when. The gate runs before a word is written. Audit, ingest, witness and second look
run afterwards over every entry there is, as often as wanted, and judge each entry by itself.
Nothing yet looks across entries except ingest's search for a word written under two spellings:
whether a relation's target exists, whether an antonym is named back by its opposite, whether two
languages' entries are each other's translation are checks of step 3 and 4, and wait for them.

## Decisions

- Opus at high effort writes. Low effort lost a blind comparison to the old entries 9:17; Haiku
  lost to Opus 5:19 and wrote English definitions into other languages; Fable was level with Opus
  at more than twice the price.
- Words to write: the words of the catalogue's books plus the most frequent of each language, not a
  million up front. English goes down to Zipf 1.5 (about 65,000 words).
- An old or dated word is an entry with a time label; an old spelling is an alias.
- Prompt v4.10 is the writers' prompt since 18 November 2026 (v4.8 and v4.9 for a few hours each
  before it); `LEXICON_PROMPT_VERSION=v4.7` goes back to one list. A grammar value states how the
  word is ordinarily used, as a learner's dictionary prints it.
- Prompt v4.11 is the writers' prompt since 19 November 2026: v4.10 with `context` on a way of saying
  the word (the owner, 19 November). Entries of v4.10 stay as they are.
- The writer is not shown English Wiktionary's senses: it follows their number, and for every
  language but English they are fewer than the entry should have ("The evidence trial"). Shown
  the language's own edition as well it no longer shrinks, and a reader still counts no gain
  ("The second evidence trial"): evidence in the request is not the writers' default.
- The third model of the review loop is Gemini 4 Carbon at medium effort, paid by the call through
  OpenRouter (the owner, 20 November 2026): three families in three seats, and the rulings off the plan the
  writer works on.
- A written entry is reviewed by a second model family; the writer answers, and a third model rules
  where the two disagree (the owner, 19 November 2026). A change reaches the published copy when
  two models of different families call it a fault, or the third has ruled, and never against the
  sources on a fact they state. Wording and rarer senses are not looped until a count asks for it.
- A word whose only entry is of an older shape is written afresh, not mended (the owner, 18
  November 2026). The older entry is kept.
- A word said in more than one way in a variety has a transcription for each, the commonest first,
  and the stressed spelling for each where the language has one. A later one may carry a note:
  less_common, older, newer, careful, casual, regional, disputed. So German *unmöglich* and
  *notwendig* carry both stresses, English *data* both vowels, Portuguese *casar* its casual
  /ka.ˈza/. 570 English lexemes have two, 77 German, 73 French, 52 Portuguese, 13 Italian, 5
  Russian, 4 Spanish. German and English mark stress only in the transcription. What a second
  way can say of itself is little: its variety (en-US, pt-PT), the spelling it belongs to, and one
  note from seven. Of some 2,000 second ways 95 carry a note (34 of them Portuguese "casual"); the
  rest say only, by their place, that they are the less common. There is no "mainly" or "only" on
  a transcription as there is on a sense's region, no place inside a variety (the north of Italy,
  the south of England), and no comment in words. A word said differently in one of its meanings
  is two lexemes (*lead*), which is the only case settled by rule.
- A stressed spelling is the word's own spelling with one mark on a vowel. In Russian and Ukrainian
  the mark is the combining acute U+0301 after the Cyrillic letter (there is no ready-made
  character); in Italian it is the accented letter itself, grave or acute (è and é, ò and ó say
  which vowel). The audit checks it (`check_v4.py`): of 12,171 written, Russian has none wrong;
  Ukrainian had a Latin í and "ov" typed into two words and three with a letter changed, which
  ingest now rebuilds from the spelling with the mark on the same letter (`own_reading`); Italian
  has seven that respell a loanword (*bordò* for *bordeaux*) and one typing error (*vière* for
  *viene*), left for an eye.
- Japanese readings are held against the Japanese Wiktionary (62,000 spellings with their reading,
  `ja.native.readings.tsv`) and the accent table's rows: of 352 entries one reading is in neither,
  *大丈夫* read だいじょうふ, a rare reading that exists. Its pitch accents are not taken: of 9,500
  it has, 760 are Tokyo's and the rest Kyoto's and Osaka's. So every target language is held
  against its own Wiktionary (for English that is the edition the index is built from), and
  Japanese for its readings.
- Two meanings that are said and inflected alike are senses of one lexeme in every language
  (*bank*, *Schloss*, *avocat*, *брак*), although dictionaries number some of them as homonyms. A
  lexeme is split by sound, part of speech, gender or inflection (*lead*, *der/die See*, *Bank* with
  *Bänke* and *Banken*, *за́мок* and *замо́к*): what a reader hears and what the forms hang on.
  Splitting by origin would ask a writer for etymology, and get it wrong.
- es-ES writes *ll* as /ʝ/, as nearly all of Spain says it; the separate /ʎ/ is not marked.
- Glosses are not bought for now. The sheet has the definition in the word's own language. A reader
  who cannot read that yet needs a translation line; today the lookup asks a translation provider
  only when the lexicon has nothing, so showing it beside a lexicon entry is part of the backend work.
- The audit accepts the weak vowels of *happy* and *situation* in English and the short close
  vowels of unstressed German syllables (*marginal*, *voran*), which the writers use of their own
  accord and rightly (`"also"` in the inventory files). That took the entries with a sound outside
  the list from 1,766 to 39 in English and from 342 to 4 in German. The prompts up to v4.6 do not
  list them, because a prompt entries were written with does not change; the next version will.
- Russian is a target language since 18 November 2026. Japanese is on trial; Korean and Chinese have
  no settings.
- A sense has frames (patterns, each with an open place) and collocations (the words it is said
  with, no open place), at most five and ten. One frame is one pattern, and the clean entry carries
  it read into parts (`patterns`). An open place has one spelling per language (the
  table under Frames); German follows Duden (*jmdm.*, *etw.*). Ingest sorts and respells what was
  written before.
- A refusal costs half an entry, so a list holds only the kinds of word the writers mostly write:
  `healthy-<lang>`, from the index and the refusals counted so far. The gate stops a headword that
  is only the name of a person, a place or a firm, for the writers and the mender alike.
- Work happens on `develop` and is pushed at once.

## Sources and witnesses

A writer is a model and states things from memory. Everything else here is something outside the
writer that its work is held against. Nothing from a source is copied into an entry: a source
decides what is written, or says where an entry may be wrong.

**In use, by a program, on every entry:**

| Source | What it is | What it is asked | Where | Licence |
|---|---|---|---|---|
| Wiktionary, through kaikki.org's extracts | The English Wiktionary's entries for each language, 9 GB, read once into tables (`lemma_index.py`) | **Before a write:** is the spelling a word of its own, an inflected form, a name, another spelling, an abbreviation (the gate and the healthy lists). **After:** the gender, the plural, the stressed syllable and the sounds of every entry (`witness.py`); the stressed spelling of Russian and Ukrainian words (`check_v4.py`) | `lemma_gate.py`, `core_list.py`, `witness.py`, `check_v4.py` | CC BY-SA |
| Each language's own Wiktionary, through kaikki.org | The German Wiktionary for German, the Wiktionnaire for French, and the Spanish, Italian, Portuguese and Russian editions: 1.5 GB, read into `<lang>.native.facts.tsv` by `open_sources.py` | The gender, the plural, the stressed syllable and the sounds of an entry, beside the English edition's; the stress of every Russian form | `witness.py`, `check_v4.py` | CC BY-SA |
| CMUdict | 126,000 words of American English, every accepted way of saying each, with stress | The stressed syllable and the sounds of an English entry, for the United States (`open_sources.py` brings it into the witness's tables) | `witness.py` | BSD-style |
| Britfone | 15,000 words of British English in IPA with stress | The same for Britain | `witness.py` | MIT |
| OpenRussian | 60,000 Russian words with their stress in every form, and the gender of nouns | The gender of a Russian noun; the stressed spelling of a word and of its forms, beside Wiktionary's | `witness.py`, `check_v4.py` | CC BY-SA |
| Kanjium's accent table | The Tokyo pitch accent of 124,000 Japanese words | Is the accent an entry gives one the table has for that reading | `check_v4.accent_witness` | CC BY-SA |
| wordfreq | How often a spelling is met, in forty languages | Which words are worth an entry and in what order; the bands a sample is drawn from | `core_list.py`, `measure.py` | Data CC BY-SA |
| MeCab with its dictionary | A reader of Japanese text into words | Which dictionary word a spelling of the frequency list is a form of | `core_list.japanese` | BSD and its dictionary's terms |
| The symbol lists | Our own: the IPA symbols each variety may use, after its dictionaries (`inventories/`) | Is every symbol of a transcription one the variety has | `check_v4.py` | ours |
| The schema | Ours (`entry.schema.v4.json`) | Is the entry of the agreed shape; the writer's CLI refuses an answer that is not | the writers, `check_v4.py` | ours |

**In use, by a reader, on a sample:**

| Source | What it is | What it is asked |
|---|---|---|
| A reader of another model family (GPT Astra), and Claude Fable as a second | Models that did not write the entry | Which of seven kinds of fault each entry of a random sample has (`measure.py`). They are told to report only what a dictionary of the language would show, and are themselves measured by planted faults and checked entries |
| The example entries (`gold/`) | Five entries a language that a person has read and corrected | They are shown to the writer as the model of an entry, and to a reader, unmarked, to count its false alarms |
| The owner | Reads English, Ukrainian and Russian | Decisions, and the entries of those three languages |

**What October built for the schema-3 writers** (`score.py`, `wiktionary.py`, `entry_pipeline.py`),
and where each stands on 19 November:

- *CMUdict for English stress and sounds*: connected, above, with Britfone for Britain. It
  confirmed more than it found: of thirty English sounds that neither it nor Wiktionary has, one
  was a fault.
- *WikiPron for the sounds of English, German and Ukrainian*: nothing to connect. WikiPron is
  Wiktionary's transcriptions scraped into lists, and the witness reads the same transcriptions
  from kaikki's extract with the place and the standing of each.
- *spaCy, lemmatizing a form back to its lemma*: not connected. The index already knows from
  Wiktionary's inflection tables which word a form belongs to, which is surer than a tagger's
  guess; a check of an entry's forms against it is the piece to build.
- *Wiktionary's senses inside the writer's request, each written sense citing the ones it covers*:
  not connected. It changes what the writer is asked, so it is a prompt version with a trial and
  a count of its own, and the one change left that could lower facts and missing senses at the
  source and not after it. A first step that needs no model: the parts of speech Wiktionary files
  a word under, held against the lexemes an entry has.

**Not used yet:** Wikidata's lexemes (CC0: spellings and forms), Princeton WordNet and the Open
Multilingual Wordnet (synonyms, antonyms and sense links across languages; much finer than a
learner needs, 18 senses of *bank*), the catalogue's own public-domain books (examples and which
words readers meet). Duden, Le Robert, the RAE's dictionary, Treccani and the others are named to
readers as what to judge by and give the conventions the prompt follows (German *jmdm.*, *etw.*);
none is read by a program, and none may be copied.

**The dictionaries with a name, and what a program may read of them** (looked up on 19 November
2026; terms change, and each is to be read again before anything is built on it):

| Dictionary | What is offered to a program |
|---|---|
| Duden | A paid API (from 175 euros a month for 1,000 requests a day) for spelling, grammar and synonyms. No entries: no gender, plural, sounds or definitions |
| Le Robert | None. Its terms forbid building a database from the product; the libraries that exist scrape the site |
| RAE (DLE) | No official API was found; what exists is unofficial and reads the site |
| De Mauro, Treccani | None, and no open licence |
| Merriam-Webster | An official API with a learner's dictionary: free for non-commercial use up to 1,000 requests a day, by agreement for a product |
| Oxford, Cambridge | APIs by licence or application; Oxford has a learner's dictionary API |

So the named dictionaries cannot be loaded, with the possible exception of a licence for English.
They are reached in two other ways. A reader with a browser consults them a word at a time, as
GPT did with De Mauro for Italian: the review file can ask every reader to name the dictionary it
judged by. And for each language there are open sources made by specialists, which a program may
load whole:

| | Loaded | To load next |
|---|---|---|
| en | Wiktionary, CMUdict, Britfone | - |
| ru | Wiktionary, the Russian Wiktionary, OpenRussian | - |
| de | Wiktionary, the German Wiktionary | DWDS, which has an API for sounds and for collocations, its terms still to be read |
| fr | Wiktionary, the Wiktionnaire | Lexique 3, 140,000 words with sounds and gender, which the cloud session could not reach |
| es | Wiktionary, the Wikcionario | Nothing found; its sounds follow from its spelling and could be a rule |
| it | Wiktionary, the Wikizionario | No open pronouncing dictionary was found |
| pt | Wiktionary, the Wikcionário | No open pronouncing dictionary was found, which is where the entries are weakest |

The editions hold more than the witness reads so far: every inflected form of a word (a check of
an entry's forms, in place of October's tagger), its senses in the language itself (evidence for
a writer, or for a core sense that is missing), and the parts of speech it is filed under. For the
collocations the Leipzig Corpora Collection offers counts of which words stand together in each
language; its licence differs from corpus to corpus and is to be read first.

**What each can and cannot say.** Wiktionary is edited by volunteers and is uneven: it has the
gender of nearly every noun and a transcription for most common words, often one where a word is
said two ways, sometimes a dialect's beside the standard. So its word is a lead, never a verdict,
and the checks are as lenient as the fact allows ("Witnesses"). wordfreq counts spellings, not
words: an inflected form's count is given to its base word by the index. The readers are models:
they agree with each other on facts more than on wording, and they miss things, which is what the
planted faults measure.

**What has no witness.** The definitions, the choice and order of senses, the examples, the CEFR
levels, the labels and regions, the frames and the collocations are held against nothing but a
reader's judgement on a sample. That is most of an entry. The cheapest next witnesses: each
language's own Wiktionary; a corpus count for the collocations (is the pair really frequent); and
Wiktionary's parts of speech and senses for a core meaning that is missing.

Whether reusing Wiktionary's facts in bulk carries its share-alike terms is a legal question this
page does not settle.

## Pending, in order

1. ~~The backend reads schema 4~~ Done on 18 November 2026; what it left open is listed under "The
   backend reads schema 4", the translation line and the regular forms first.
2. **The citing pass**, and the evidence block in the writer for new words (v4.1 rules with v6's
   evidence and inflected-form rules).
3. **The second look over the whole list**: some 480 entries, $13 ("The second look"). Then
   the Spanish tap and trill and the Spanish stress as rules of ingest, by the spelling, and the
   Russian vowel before the stress checked from the stressed spelling.
   **Evidence, the second try**: counted ("The second evidence trial"). The reader sees no gain
   over writing without evidence, so the recommendation is to write with v4.11 as it is and spend
   on the review loop; the owner confirms. One decision of the owner's is still open: whether the
   bar for facts stays at two.
   **The review loop over the dictionary**: first ten entries through the Codex seat
   (`review_v4.py --langs en --take 10 --steps read`), then the 1,824 entries of v4.10, some $22
   on the Claude plan, and a sample of the outcomes read by the owner in the three languages they
   read.
4. **The older words**: 39,902 to write again, after the third sample has shown that v4.10 holds.
5. **Lists**: the healthy lists are to be run in place of the older queues. Fixed phrases are still
   the gap a learner feels (one entry in fifty lacks a phrase like *sans doute* at its word), with
   ingest linking each phrase to the words in it; the book-word list needs an export from the book
   processor, and will want `--names` for the places of a book. A list for Japanese needs a reading
   of its frequency list.
6. **Japanese**: the next few hundred words under v4.9 and a second count, on words of every
   band this time; the entries of v4.7 whose headword is also a suffix or an auxiliary, written
   again (`--rewrite-older`); an accent table that tells a word in kana from its homophones. Its
   list of thousands needs a reading that the top 250 got by hand. Korean and Chinese have no
   settings yet.
7. **Collocations a reviewer has read**, then **the two lists for the entries of before v4.8.**
   The split is by rule and by eighty trial entries; whether the collocations chosen are the ones a
   learner's dictionary would choose, and whether ten is too many, is a question for the next
   measurement, whose review file shows them. If they hold, a pass over the 31,900 schema-4 entries of before that returns only a
   sense's frames and collocations, as a patch like the mender's and keyed by the entry it read:
   some $0.02 an entry by the mender's prices, so about $640 for all of them and $100 for the
   5,000 most frequent. Not built. The clients show neither list yet.
8. **One set of checks**: `entry_checks.py` and `entry_repair.py` (schema 3) and `check_v4.py` and
   `ingest_v4.py` (schema 4) overlap.
9. ~~One owner of sense identity~~ Done with item 1: ingest owns it, the backend takes the id. What
   is left is that the ids are only on the machine that ingests (`out/lexicon-s4/` is not in the
   data repository).
10. The lookup normaliser (apostrophes, soft hyphens); 95 entries that fail the schema, every one
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
