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
- Backend: `learning/lexicon`. It reads schema 4 as ingest publishes it and nothing older; see "The
  backend reads schema 4".

## How a word becomes an entry

Today, for schema 4:

1. **List.** `headwords/<name>-<lang>.json`, most frequent first.
2. **Gate** (`lemma_gate.py`, free). Wiktionary's verdict on the spelling, from an index built once
   by `lemma_index.py`: an inflected form or a bare abbreviation is not written.
3. **Write** (`claude_code_writer.py`, Opus at high effort, prompt v4.9). A word with a schema-2
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
6. **Publish** (`publish_entries.py`, free). The clean entries of `out/lexicon-s4/` go to the backend,
   which checks each against the published schema and takes it; see "The backend reads schema 4".

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
3. **The clients show none of the new fields** (labels, frames, collocations, spellings): the
   Discover sheet's contract is unchanged.
4. **The entries published before** (schema 2, with glosses) stay in a database until their
   headword is published again or the owner clears the lexicon. The columns only schema 3 filled
   (`sense.register`, `usage`, `domain`, `lexeme.pattern`, `regions`, `lexeme_form.regions`) were
   dropped the same day by a change set of their own, so those older entries no longer have a
   register or a field. A version of the backend from before schema 4 cannot read the lexicon
   once that change set has run.
5. A reading with no transcription (Japanese) is stored and served by the entries endpoint, and the
   Discover sheet has no place for it yet.

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

Its list is `headwords/core-ru.json`: 12,596 words at Zipf 3 or above that Wiktionary knows as
words and nobody has written, most frequent first, made by `core_list.py`, which gives the French
list of the same name back almost to the word. Three things in it are Russian's own:

- **ё.** Print has *ее* and *елка*, Wiktionary files those as spellings of *её* and *ёлка*, and a
  frequency list counts them apart. The word with ё is queued, with the frequency of both; the
  spelling with е is not, because the entry lists it.
- **Letters and abbreviations.** *г*, *см*, *км* are frequent and Wiktionary has them as words. A
  word without a vowel is left out, and a single letter unless it is one of the one-letter words
  (*а*, *в*, *и*, *к*, *о*, *с*, *у*, *я*).
- **Words that are forms as well** are in the list (1,822 of them), where the other five lists
  left them out. In Russian they are the first words a learner needs: *как*, *это*, *для*, *уже*,
  *хорошо*, each of them also filed as a form of something (*для* of the verb *длить*). See below.

```bash
scripts/.venv/bin/python scripts/lexicon_research/claude_code_writer.py --model claude-opus-5-5 --effort high --langs ru --headwords core --workers 4 --limit 1000
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
*der*, *das*, *es*, *sich*; *la*, *des*, *une*, *dans*, *mais*, *été*; *la*, *los*, *para*, *como*,
*pero*; *mi*, *gli*, *fatto*; *a*, *os*, *para*, *como*. None of them is in any list or has an entry.
`core_list.py --forms-only` writes them as `headwords/core-forms-<lang>.json`, most frequent first:

| | words at Zipf 3 or above | at 4.5 or above |
|---|---:|---:|
| de | 1,978 | 271 |
| fr | 1,330 | 180 |
| es | 1,639 | 244 |
| it | 2,901 | 364 |
| pt | 1,955 | 303 |

Going by English, about half of each list will be written and half refused, and the top of each is
where the words are. German's has a weakness of its own: a noun that looks like a verb form (*Habe*,
*Muss*, *Hast*) takes that form's frequency, because the frequency list knows no capitals.

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
- Prompt v4.9 is the writers' prompt since 18 November 2026 (v4.8 for a few hours before it);
  `LEXICON_PROMPT_VERSION=v4.7` goes back to one list.
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
- A common-word list holds the words that are inflected forms as well for Russian, and for the other
  five they are a second list, `core-forms-<lang>`: the writer decides which of them are words.
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

1. ~~The backend reads schema 4~~ Done on 18 November 2026; what it left open is listed under "The
   backend reads schema 4", the translation line and the regular forms first.
2. **The citing pass**, and the evidence block in the writer for new words (v4.1 rules with v6's
   evidence and inflected-form rules).
3. **Measure v4.9**: forty new words a language again, which is the cheap half, and the same
   files for a batch of mended entries. The review file now states what is ours by definition (one
   value for every pronominal use; transitive means a direct object; what `mainly` means; a form
   reaches its word by an alias), so that none of it is counted as a fault. Then, if the claims
   of the older entries are still worth it, a pass over them that asks only whether a tag should
   be `mainly`.
4. **Lists**: fixed phrases first, which the measurement found to be the gap a learner feels (one
   entry in fifty lacks a phrase like *sans doute* at its word), with ingest linking each phrase to
   the words in it; the commonest words of de, fr, es, it and pt, which their core lists left out
   because each is a form of something as well (*der*, *la*, *para*): `core-forms-<lang>` holds
   them and is not yet run; the book-word list, which needs an export from the book processor.
5. **Japanese**: the next few hundred words under v4.9 and a second count, on words of every
   band this time; the entries of v4.7 whose headword is also a suffix or an auxiliary, written
   again (`--rewrite-older`); an accent table that tells a word in kana from its homophones. Its
   list of thousands needs a reading that the top 250 got by hand. Korean and Chinese have no
   settings yet.
6. **Collocations a reviewer has read**, then **the two lists for the entries of before v4.8.**
   The split is by rule and by eighty trial entries; whether the collocations chosen are the ones a
   learner's dictionary would choose, and whether ten is too many, is a question for the next
   measurement, whose review file shows them. If they hold, a pass over the 31,900 schema-4 entries of before that returns only a
   sense's frames and collocations, as a patch like the mender's and keyed by the entry it read:
   some $0.02 an entry by the mender's prices, so about $640 for all of them and $100 for the
   5,000 most frequent. Not built. The clients show neither list yet.
7. **One set of checks**: `entry_checks.py` and `entry_repair.py` (schema 3) and `check_v4.py` and
   `ingest_v4.py` (schema 4) overlap.
8. ~~One owner of sense identity~~ Done with item 1: ingest owns it, the backend takes the id. What
   is left is that the ids are only on the machine that ingests (`out/lexicon-s4/` is not in the
   data repository).
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
