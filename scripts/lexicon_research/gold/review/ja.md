# Example entries for Japanese: review

These are entries of a learner's dictionary of Japanese, written by a model. The best four or five will be
shown to the writing model as examples of the house style, so an error left in one is copied into
thousands of entries. Review each candidate strictly.

For each candidate, check:

1. **Facts.** Is anything wrong: a meaning, a gender, a plural or another form, a transcription, a frame,
   a label, a region, a level that is off by more than one step?
2. **The sense list.** Would a monolingual learner's dictionary of Japanese number exactly these senses?
   Name a common sense that is missing, and a sense that is only a shade of another.
3. **Style.** Does every definition keep the rules below (10 to 35 characters, plain words, a phrase and not a
   sentence, without the headword, without formulas such as "the act of")? Is every example natural,
   and does it show its own sense?

Answer for each candidate with:

- a verdict: **use as it is**, **use with corrections**, or **do not use**, and one line saying why;
- for "use with corrections", the whole corrected entry as JSON, in the same shape.

Then name the four or five you would show, best first. Between them they should cover a plain noun, a
verb with its frames, an adjective, a headword with two parts of speech, and a labelled or regional sense.

## The rules the entries were written to

# Lexemes

A lexeme is one word with one paradigm. Split the headword into separate lexemes when the pronunciation differs (English "lead" /liːd/ to guide and "lead" /lɛd/ the metal), when the part of speech differs ("run" the verb and "run" the noun), when a noun's gender differs (German "der See" lake and "die See" sea), or when the inflection differs (English "hang", past "hung" for a picture and "hanged" for a person). Meanings with the same pronunciation, part of speech, gender and inflection are senses of one lexeme (German "Schloss" castle and lock), and one word that is said in two ways (English "either") is one lexeme with two readings. Give every part of speech the headword has in ordinary use: an adjective also used as an adverb or as a noun has a lexeme for each (Spanish "genial", "responsable"), not a note on the adjective. Capital letters do not separate entries: a lexeme whose lemma differs from the headword only in capitalisation belongs here too (German "morgen" tomorrow and "Morgen" morning).

Return only lexemes whose lemma is the headword itself. A phrase, phrasal verb or idiom that contains the headword ("hash out", "make a hash of" under "hash") is an entry of its own and does not belong here; an idiom a learner needs is shown under the frames of the sense it uses.

kind is "word" for a single word and "multiword" for a fixed expression of several words ("give up", "mother-in-law", "pomme de terre", "брати участь"). A multiword lexeme lists its components in order, each with its text, the lemma it is a form of, and how it inflects: "head" carries the inflection (mothers-in-law, gave up), "agrees" inflects to agree with the head (Ukrainian "залізна дорога", "залізної дороги"), "fixed" never changes. Set "separable": true when other words can come between the components ("give it up").

A compound written as one word stays kind "word" and lists its components too, when each is a word of the language: German "Fallschirm" is fallen + Schirm, "Arbeitszimmer" is Arbeit + Zimmer, English "toothbrush" is tooth + brush. Each component has its text as it stands in the compound, with any linking letters ("Arbeits"), and its lemma, and is "fixed". A word built with a prefix or a suffix ("unhappy", "kindness") is not a compound and has no components.

kind is "contraction" for two words written as one (German "zum" is zu + dem, French "au" is à + le, Italian "della" is di + la, English "can't" is can + not). A contraction is an entry, never a reject: it takes the part of speech of its first component, lists its components in order with their lemmas, each "fixed", and has one sense that says what it stands for and when it is used. A word that is both a contraction and a word of its own (French "du", the partitive article and de + le) gets a lexeme for each.

A headword that is also an inflected form of another word returns only the lexemes it is itself the lemma of; the inflection is never a lexeme. English "saw" gives the tool and the verb to saw, not the past of see; "found" gives to found, not the past of find; "left" gives the adjective, the adverb and the noun, not the past of leave. A form with a meaning its lemma lacks follows the learner's dictionaries of the language:
- a form they list as a headword of its own is a lexeme, with "number": "Plur" for a plural (English "glasses" for the eyes, "scissors", German "Eltern");
- a form they list under its lemma is a sense of the lemma's lexeme, with "grammar": {"number": "Plur"} on that sense (English "arms", weapons, under "arm"; Spanish "esposas", handcuffs, under "esposa"), and the form itself is rejected naming the lemma;
- a participle they list as an adjective is an adjective lexeme of its own ("interested", "tired", German "bekannt"); a participle that only inflects its verb ("signed", "departing") is rejected naming the verb.
When nothing is left, reject with "inflected form of" and the lemma.

If the headword is not a lemma of the language at all (an abbreviation, a word of another language, an inflected form, a misspelling, or a spelling only older books use for a word written differently today), return an empty lexemes list and a one-line "reject" in English saying why, naming the lemma if there is one ("inflected form of festlich", "pre-reform spelling of dass"). An old word is not a reject: a word that readers of older books still meet is a lemma, written with its time label (dated or archaic).

# Spellings

Write the lemma as Japanese dictionaries head it: in kanji with its okurigana where the word is normally written so (食べる, 高い), in kana where it normally is (する, これ, テレビ). The headword may arrive in kana or in kanji. List another way of writing the word as a spelling only when print uses it too, each with its "script": Jpan for kanji with or without kana, Hira, Kana (綺麗 Jpan and きれい Hira; コンピューター and コンピュータ, both Kana). A character of before the 1946 reform (國 for 国) is a spelling with "pre_reform": true. Give "spellings" whenever the lemma is written more than one way: every current spelling with the varieties that use it ("colour" en-GB, en-AU, en-IN and "color" en-US), and a spelling only older books use with "pre_reform": true. The other spellings are never written as entries of their own, so list them all; a lemma written one way has no "spellings". When two spellings are said differently (Spanish "período" and "periodo"), each reading names its "spelling", and a sense that only some of the spellings have lists them under "spellings".

When the request has a line "Covered:", the headword is already listed as a spelling of the lexemes named there, and they are written. Do not write them again: return only lexemes of this spelling that are other words (English "tire", to make someone tired, when "tire" is covered as a spelling of the noun "tyre"), and if there are none, reject with "spelling of" and the lemma. For spellings and forms the varieties of Japanese are only these: ja-JP; where a sense is used is said under Regions, which reach further.

# Pronunciation

Japanese is not given in IPA. Each pronunciation has "variety": "ja-JP", a "reading" in hiragana and "notation": "kana" (食べる: たべる; 東京: とうきょう), also for a word written in kana, whose reading repeats it in hiragana (テレビ: てれび). When a word is read in more than one way with the same meaning, give the most common first (明日: あした, あす); a reading after the first may carry a "note": less_common, older, newer, careful (the formal reading), casual, regional or disputed. Give a note only when you are sure of it. A reading that changes the meaning is another lexeme, not a second reading (辛い: からい, hot to the taste, and つらい, hard to bear).

# Grammar and forms

Grammar: only what the language needs, and only these keys, with these values: gender (Masc, Fem, Neut; Com for a noun that is masculine for a man and feminine for a woman without changing its form: el/la estudiante, колега), number (Sing or Plur, only for a noun used in one number), countability (Count, Mass, Both), valency (Tr for a verb with a direct object, Intr for one without, also when its object takes a preposition or the dative: German "helfen", Portuguese "lidar com"; Both, Refl, Impers), irregular (true/false), conjugation (1, 2, 3 by the infinitive ending in the language's own order; German: strong, weak, mixed), auxiliary (have, be; both for a verb that takes one in some senses and the other in others, each sense then giving its own: Italian "ho iniziato a leggere", "la lezione è iniziata"), separable and separable_prefix, reflexive, declension (1-4, weak, strong, mixed, adjectival), plural_class (German: -e, -¨e, -en, -n, -er, -¨er, -s, -nen, -¨ for an umlaut alone (Vater, Väter), or - for no change), aspect (Imp, Perf), animacy (Anim, Inan), invariable, position (Attr, Pred), comparable, mainly, and note for anything else, in one short clause in English. Where one variety differs from the rest, put only the difference under "by_variety" (German "E-Mail": gender Fem, by_variety de-AT and de-CH: gender Neut). What holds for one sense only goes in that sense's own "grammar", not the lexeme's: an adjective that stands only before a noun in one of its senses has position Attr on that sense. A sense's grammar may carry a short note too.

A value of countability, valency, position, number or comparable says what holds without exception. Give it as a learner's dictionary of Japanese prints it, and leave out the one you are not sure of. Where it holds for most uses but not all, name the key under "mainly" as well: an adjective that usually stands before a noun but can follow a verb has "position": "Attr", "mainly": ["position"]; a noun that is uncountable except when kinds of it are meant (soap, two soaps) has "countability": "Mass", "mainly": ["countability"]; a noun nearly always met in the plural has "number": "Plur", "mainly": ["number"]. A verb with a rarer use of the other kind has valency Both, or Intr with "mainly": ["valency"]. comparable false is only for an adjective that cannot be compared at all (wooden, daily); one that is seldom compared has it with "mainly": ["comparable"].

Forms: Verbs: nothing for a regular godan or ichidan verb, whose class gives every form; for an irregular verb or form the polite non-past (Polite=Form), the て form (VerbForm=Conv) and the plain negative (Polarity=Neg): する, 来る, 行く (行って), ある (ない), いらっしゃる (いらっしゃいます). Adjectives: nothing, except where the class does not give the form (いい: よく, よかった). List the forms this names even where they are regular, and beyond them only a form a learner could not build by rule; every other form is generated later from the grammar. Features use Universal Dependencies names and values only (Number=Plur, Case=Gen, Tense=Past, Person=3, VerbForm=Part, Mood=Sub); a form that fills several cells joins the values of one feature with a comma (VerbForm=Fin,Part; Person=1,3), and is listed once for each when two features differ at once. A form used only in one construction says so in a short "note" (Spanish "electo": only in the passive). A form used only in some varieties lists them ("learnt" en-GB). A form that belongs to only some senses lists their numbers, counting from 1 ("brethren" for the religious sense of "brother"). Each listed form carries its own reading in hiragana with "notation": "kana", and no ipa.

# Senses

Senses in descending frequency of use. Only senses a reader meets in books and ordinary text; no technical, dialectal or obsolete senses unless the headword is itself rare. Two senses are separate only when a monolingual learner's dictionary of Japanese would number them separately. Most words have one or two senses; give more only for a word that is truly polysemous. Do not split shades of one meaning, and do not make senses out of subject areas or facts about the thing ("potato" as a plant, a food and a crop is one sense). A use that every word of its kind has is not a sense (a drink as one glass of it), and neither is a meaning the word has only inside one idiom. A difference of construction alone is not a sense either: it is a frame, and a construction used in part of the language only is said in the sense's grammar note (Spanish "participar de algo" beside "participar en algo"). A reflexive or impersonal use with a meaning of its own is a sense, with its valency in that sense's grammar and, when the pronoun does not mean "oneself", a note saying what it does (each other, by accident). Include a common figurative sense ("a climate of fear"). Give only senses the Japanese word has: a meaning that only its English equivalent has does not belong here.

Definition. In Japanese, 10 to 35 characters, plain words a learner knows, a phrase not a sentence, in the style of a learner's dictionary. The headword and words of its family must not appear in it, except the name of the country in the definition of its people and language ("French": of France). It holds for every use of the sense and says no more than the word means: do not narrow it to the typical case (a judge decides disputes of every kind, not only guilt; a message need not be short). No example inside it, no list of instances ("such as China and Japan"), no synonym added at the end, and no formula such as "the act of", "the state of being", "relating to" or "characteristic of". A verb is defined as a verb, a noun as a noun, an adjective by an adjective phrase and not by a clause ("so that ..."). A word with no meaning to paraphrase (an interjection, a particle, a conjunction) is defined by what it is used for, opening as the learner's dictionaries of Japanese open such a definition (English "used to ..."). A remark on grammar, position or field ("before an adjective", "at the start of a sentence", "in boxing") is not part of a definition: it is a frame, a note in the sense's grammar, or a domain label.

Example. One sentence in Japanese of 12 to 40 characters, natural, no proper names, no stereotype of a people or a group, never a question with its answer, that shows this sense and its frame if it has one. An interjection stands inside its sentence, set off by a comma, not as a sentence of its own. Vary the examples: not always the past tense, not always a third-person subject, not always starting with an article.

Frames. The constructions a learner needs to use the sense, printed as a learner's dictionary prints them: the word with its preposition, case or complement ("warten auf + Akk", "helfen + Dat", "depend on sth", "дякувати кому", "insist on doing sth"). At most three; leave out when the sense has none. A fixed phrase that is an entry of its own ("instead of" under "instead") is not a frame.

Level. Your estimate of the CEFR level of the sense, A1 to C2.

Labels. How the sense is used. Leave out every label that has its default:
- formality: formal | informal | slang (default neutral)
- attitude: any of derogatory, offensive, vulgar, humorous, ironic, euphemistic, endearing, disapproving
- time: dated (old-fashioned but still understood) | archaic (met only in old texts) | historical (the ordinary word for a thing of the past) (default current)
- style: any of literary, poetic, elevated, official
- normativity: nonstandard (common in speech but not standard) | proscribed (rejected by the language's normative dictionaries) (default standard); with either, give the standard word in "standard_alternative", which no other sense has
- politeness: familiar (said to friends, family and children: du, tu, tú) | polite (the respectful form to a stranger or a superior: Sie, vous, usted, Lei), only for a word or sense that is one of the two
- domain: one of agriculture, anatomy, architecture, art, astronomy, aviation, biology, botany, chemistry, computing, cooking, economics, education, electricity, finance, fishing, geography, geology, grammar, history, hunting, law, linguistics, literature, mathematics, medicine, military, music, mythology, nautical, philosophy, photography, physics, politics, psychology, religion, sport, technology, theatre, transport, zoology, only for a specialist sense, not for an everyday word of the field (a goal in football has none)
- figurative: true for a figurative sense
- rare: true for a sense few speakers use
Regions. A sense used only or mainly in part of the Japanese-speaking world lists where, each with "variety" and "extent". A variety is the language with a country (ja-JP). The extent is "only" when the sense is not used elsewhere and "mainly" when it is used everywhere but chiefly there ("lead", a dog's strap: en-GB only). Give "only" when you are sure the rest of the language does not use the sense, otherwise "mainly", and name the area rather than two or three of its countries when the sense is used across it. A region says where the word is used, not where the thing is found: the name of an office or a district that one country has carries no region. Leave regions out for what the whole language uses, and never put a region in a definition. When a word is neutral in one variety and offensive or vulgar in another, those are separate senses, each with its regions and labels.

Antonyms. For a sense with a true opposite, the opposite's lemma and part of speech (allow and forbid, aware and unaware). A word that only contrasts with the sense is not its opposite (hill and valley, write and read, perhaps and definitely): leave it out. No synonyms.

Definitions and examples are in standard Japanese: the definition in the plain style of a dictionary, the example a natural sentence in the plain or the polite style, both with kanji as a newspaper uses them. A verb gives its class in "conjugation": godan, ichidan, or irregular (する, 来る), and its valency; a transitive verb and its intransitive partner (開ける, 開く) name each other as a relation of type confusable, with why. An adjective gives "conjugation": i or na (高い: i; 静か: na). A noun counted with a counter of its own gives it in "classifier" (鉛筆: 本; 犬: 匹). A noun that makes a verb with する (勉強) shows that in a frame (勉強する). Frames show the particles a sense takes (〜が好き, 〜に行く, 〜を食べる). A particle (は, が, を, に, ね) is an entry: its part of speech is ADP for a case particle and PART otherwise, and its definition says what it is used for. An honorific or a humble word (いらっしゃる, 参る) has the politeness label honorific or humble, a word of the polite style (です, ございます) polite, a word used only with friends and family (俺) familiar. A word or sense of one region of Japan (おおきに) names the region in the sense's grammar note, Japan being one variety.

# Relations

Relations point from this lexeme to the word it is made from or is confused with, each as type, lemma and part of speech; the reverse links are filled in later, so the base word does not list its derivatives. Types: diminutive_of (хлопчик of хлопець), augmentative_of, endearing_of (сонечко of сонце), feminine_of (actress of actor), agent_noun_of (teacher of teach), verbal_noun_of (читання of читати), abstract_noun_of (kindness of kind), adjective_of (urban of city), adverb_of (quickly of quick), derived_from (any other derivation), aspect_partner (зробити of робити), and confusable (a different word learners mix it up with, with "why" in English). A relation names another word, never a lexeme of this headword with the same part of speech; one that holds for only some of the senses lists their numbers under "senses". derived_from is for a word built from another inside the language, by a prefix, a suffix or a change of part of speech, not for two words with a common ancestor (French "permettre" is not derived from "mettre"). Give only relations you are sure of.

Write the same way for every word, so that neighbouring entries read as one dictionary. Give nothing you are unsure of; an empty field is better than a guess.

## Candidates

### 1. 食べる (pilot)

```json
{
 "language": "ja",
 "headword": "食べる",
 "lexemes": [
  {
   "lemma": "食べる",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "ichidan"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "たべる",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "物を口に入れ、かんでのみこむ",
     "example": "毎朝パンと卵を食べてから出かけます。",
     "cefr": "A1",
     "frames": [
      "〜を食べる"
     ],
     "grammar": {
      "valency": "Tr"
     }
    },
    {
     "definition": "収入を得て暮らしを立てる",
     "example": "絵を描くだけで食べていくのは難しい。",
     "cefr": "B2",
     "frames": [
      "〜で食べていく"
     ],
     "grammar": {
      "valency": "Intr",
      "note": "usually in the form 食べていく"
     }
    }
   ]
  }
 ]
}
```

### 2. 行く (pilot)

```json
{
 "language": "ja",
 "headword": "行く",
 "lexemes": [
  {
   "lemma": "行く",
   "pos": "VERB",
   "kind": "word",
   "spellings": [
    {
     "text": "行く",
     "script": "Jpan"
    },
    {
     "text": "いく",
     "script": "Hira"
    }
   ],
   "grammar": {
    "conjugation": "godan",
    "valency": "Intr",
    "irregular": true,
    "note": "te form 行って and past 行った, not 行いて/行いた"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "いく",
     "notation": "kana"
    },
    {
     "variety": "ja-JP",
     "reading": "ゆく",
     "notation": "kana",
     "note": "less_common"
    }
   ],
   "forms": [
    {
     "form": "行きます",
     "features": {
      "Polite": "Form"
     },
     "reading": "いきます",
     "notation": "kana"
    },
    {
     "form": "行って",
     "features": {
      "VerbForm": "Conv"
     },
     "reading": "いって",
     "notation": "kana"
    },
    {
     "form": "行かない",
     "features": {
      "Polarity": "Neg"
     },
     "reading": "いかない",
     "notation": "kana"
    },
    {
     "form": "行った",
     "features": {
      "Tense": "Past"
     },
     "reading": "いった",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "今いる所から、ほかの場所へ移動する",
     "example": "週末は家族と海に行くつもりです。",
     "cefr": "A1",
     "frames": [
      "〜に行く",
      "〜へ行く",
      "〜に〜を買いに行く"
     ],
     "antonyms": [
      {
       "lemma": "来る",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "物事が進んで、ある結果や状態になる",
     "example": "新しい仕事はうまくいっています。",
     "cefr": "B1",
     "frames": [
      "うまくいく"
     ],
     "spellings": [
      "いく"
     ]
    },
    {
     "definition": "動作や変化がこれから先へ続く様子を表す",
     "example": "これからも日本語の勉強を続けていきたい。",
     "cefr": "B1",
     "frames": [
      "〜ていく"
     ],
     "spellings": [
      "いく"
     ],
     "grammar": {
      "note": "auxiliary use after the te form of a verb"
     }
    }
   ]
  }
 ]
}
```

### 3. する (pilot)

```json
{
 "language": "ja",
 "headword": "する",
 "lexemes": [
  {
   "lemma": "する",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "irregular",
    "irregular": true,
    "valency": "Both"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "する",
     "notation": "kana"
    }
   ],
   "forms": [
    {
     "form": "します",
     "features": {
      "Polite": "Form"
     },
     "reading": "します",
     "notation": "kana"
    },
    {
     "form": "して",
     "features": {
      "VerbForm": "Conv"
     },
     "reading": "して",
     "notation": "kana"
    },
    {
     "form": "しない",
     "features": {
      "Polarity": "Neg"
     },
     "reading": "しない",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "何かの動作や行為を行う",
     "example": "毎晩、夕食の後で宿題をします。",
     "cefr": "A1",
     "frames": [
      "〜をする",
      "（名詞）＋する（勉強する）"
     ],
     "grammar": {
      "valency": "Tr"
     }
    },
    {
     "definition": "物事をある状態に変える",
     "example": "部屋を片付けて、きれいにしよう。",
     "cefr": "A2",
     "frames": [
      "〜を〜にする",
      "〜を〜くする"
     ],
     "grammar": {
      "valency": "Tr"
     }
    },
    {
     "definition": "いくつかの中から選んで決める",
     "example": "私は飲み物はコーヒーにします。",
     "cefr": "A2",
     "frames": [
      "〜にする"
     ],
     "grammar": {
      "valency": "Intr"
     }
    },
    {
     "definition": "音やにおい、味などが感じられる",
     "example": "台所からいいにおいがしてくる。",
     "cefr": "B1",
     "frames": [
      "〜がする"
     ],
     "grammar": {
      "valency": "Intr"
     }
    },
    {
     "definition": "アクセサリーなどを身につける",
     "example": "父は毎朝ネクタイをして出かける。",
     "cefr": "A2",
     "frames": [
      "〜をする"
     ],
     "grammar": {
      "valency": "Tr"
     }
    },
    {
     "definition": "ある色や形、様子を持っている",
     "example": "その子は大きな青い目をしていた。",
     "cefr": "B1",
     "frames": [
      "〜をしている"
     ],
     "grammar": {
      "valency": "Tr",
      "note": "usually in the form 〜をしている"
     }
    },
    {
     "definition": "値段がそれだけかかる",
     "example": "このかばんは五万円もしたそうだ。",
     "cefr": "B2",
     "frames": [
      "（金額）＋する"
     ],
     "grammar": {
      "valency": "Intr"
     }
    }
   ]
  }
 ]
}
```

### 4. 来る (pilot)

```json
{
 "language": "ja",
 "headword": "来る",
 "lexemes": [
  {
   "lemma": "来る",
   "pos": "VERB",
   "kind": "word",
   "spellings": [
    {
     "text": "来る",
     "script": "Jpan"
    },
    {
     "text": "くる",
     "script": "Hira"
    },
    {
     "text": "來る",
     "script": "Jpan",
     "pre_reform": true
    }
   ],
   "grammar": {
    "conjugation": "irregular",
    "irregular": true,
    "valency": "Intr",
    "note": "カ行変格活用 (kuru verb): stem changes between く, き and こ"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "くる",
     "notation": "kana"
    }
   ],
   "forms": [
    {
     "form": "来ます",
     "features": {
      "Polite": "Form"
     },
     "reading": "きます",
     "notation": "kana"
    },
    {
     "form": "来て",
     "features": {
      "VerbForm": "Conv"
     },
     "reading": "きて",
     "notation": "kana"
    },
    {
     "form": "来ない",
     "features": {
      "Polarity": "Neg"
     },
     "reading": "こない",
     "notation": "kana"
    },
    {
     "form": "来た",
     "features": {
      "Tense": "Past"
     },
     "reading": "きた",
     "notation": "kana"
    },
    {
     "form": "来い",
     "features": {
      "Mood": "Imp"
     },
     "reading": "こい",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "ほかの所から、話し手のいる方へ近づいて移る",
     "example": "明日の午後、友達が家に遊びに来ます。",
     "cefr": "A1",
     "frames": [
      "〜に来る",
      "〜へ来る",
      "〜しに来る"
     ],
     "antonyms": [
      {
       "lemma": "行く",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "ある時期や出来事が、やがて訪れる",
     "example": "長い冬が終わって、ようやく春が来た。",
     "cefr": "A2",
     "frames": [
      "〜が来る"
     ]
    },
    {
     "definition": "ある物事が原因や出どころとなって起こる",
     "example": "この疲れは、きっと寝不足から来ているのだろう。",
     "cefr": "B2",
     "frames": [
      "〜から来る"
     ]
    },
    {
     "definition": "動作や変化がこちらへ向かったり、今まで続いたりすることを表す",
     "example": "夕方になって、だんだん寒くなってきた。",
     "cefr": "B1",
     "frames": [
      "〜てくる"
     ],
     "grammar": {
      "note": "auxiliary use after the て form, usually written in hiragana"
     }
    }
   ]
  },
  {
   "lemma": "来る",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "invariable": true,
    "position": "Attr",
    "note": "連体詞: used only before a noun, mostly before a date"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "きたる",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "このあと近いうちに訪れる、次の",
     "example": "来る十日に、市民ホールで総会を開きます。",
     "cefr": "C1",
     "labels": {
      "formality": "formal"
     },
     "antonyms": [
      {
       "lemma": "去る",
       "pos": "ADJ"
      }
     ]
    }
   ]
  }
 ]
}
```

### 5. 開く (pilot)

```json
{
 "language": "ja",
 "headword": "開く",
 "lexemes": [
  {
   "lemma": "開く",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "godan",
    "valency": "Intr"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "あく",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "閉まっていたものが、閉まっていない状態になる",
     "example": "風で窓が急に開いて、びっくりした。",
     "cefr": "A2",
     "frames": [
      "〜が開く"
     ],
     "antonyms": [
      {
       "lemma": "閉まる",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "店などがその日の営業を始める",
     "example": "駅前のスーパーは朝九時に開きます。",
     "cefr": "A2",
     "frames": [
      "〜が開く"
     ],
     "antonyms": [
      {
       "lemma": "閉まる",
       "pos": "VERB"
      }
     ]
    }
   ],
   "relations": [
    {
     "type": "confusable",
     "lemma": "開ける",
     "pos": "VERB",
     "why": "transitive partner: 開ける takes an object (ドアを開ける), 開く (あく) does not (ドアが開く)"
    }
   ]
  },
  {
   "lemma": "開く",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "godan",
    "valency": "Both"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "ひらく",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "閉じているものを広げて、中が見えるようにする",
     "example": "教科書の三十ページを開いてください。",
     "cefr": "A2",
     "frames": [
      "〜を開く"
     ],
     "grammar": {
      "valency": "Tr"
     },
     "antonyms": [
      {
       "lemma": "閉じる",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "会議や式などを計画して行う",
     "example": "来月、新しい社員のために歓迎会を開きます。",
     "cefr": "B1",
     "frames": [
      "〜を開く"
     ],
     "grammar": {
      "valency": "Tr"
     }
    },
    {
     "definition": "店や事業などを新しく始める",
     "example": "父は駅前に小さなパン屋を開いた。",
     "cefr": "B1",
     "frames": [
      "〜を開く"
     ],
     "grammar": {
      "valency": "Tr"
     }
    },
    {
     "definition": "閉じていたものが広がる",
     "example": "暖かくなって、桜の花が開き始めた。",
     "cefr": "B2",
     "frames": [
      "〜が開く"
     ],
     "grammar": {
      "valency": "Intr"
     }
    },
    {
     "definition": "二つのものの差や間が大きくなる",
     "example": "二位のチームとの差がどんどん開いている。",
     "cefr": "B2",
     "frames": [
      "〜との差が開く"
     ],
     "grammar": {
      "valency": "Intr"
     }
    }
   ]
  }
 ]
}
```

### 6. 辛い (pilot)

```json
{
 "language": "ja",
 "headword": "辛い",
 "lexemes": [
  {
   "lemma": "辛い",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "conjugation": "i"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "からい",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "舌を刺すような強い刺激のある味だ",
     "example": "このカレーは辛すぎて、水が手放せない。",
     "cefr": "A2"
    },
    {
     "definition": "塩の味が強く、しょっぱい感じがする",
     "example": "このみそ汁は少し辛いから、お湯を足そう。",
     "cefr": "B1"
    },
    {
     "definition": "評価や扱い方が厳しい",
     "example": "あの先生は採点が辛いことで有名だ。",
     "cefr": "B2",
     "frames": [
      "〜が辛い"
     ],
     "labels": {
      "figurative": true
     },
     "antonyms": [
      {
       "lemma": "甘い",
       "pos": "ADJ"
      }
     ]
    }
   ],
   "relations": [
    {
     "type": "confusable",
     "lemma": "辛い",
     "pos": "ADJ",
     "why": "same kanji spelling as つらい, hard to bear"
    }
   ]
  },
  {
   "lemma": "辛い",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "conjugation": "i"
   },
   "spellings": [
    {
     "text": "辛い",
     "script": "Jpan"
    },
    {
     "text": "つらい",
     "script": "Hira"
    }
   ],
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "つらい",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "体や心が苦しくて、我慢するのが難しい",
     "example": "冬の朝、早く起きるのはつらい。",
     "cefr": "B1",
     "frames": [
      "〜のがつらい",
      "〜がつらい"
     ]
    }
   ],
   "relations": [
    {
     "type": "confusable",
     "lemma": "辛い",
     "pos": "ADJ",
     "why": "same kanji spelling as からい, spicy"
    }
   ]
  }
 ]
}
```

### 7. 今日 (pilot)

```json
{
 "language": "ja",
 "headword": "今日",
 "lexemes": [
  {
   "lemma": "今日",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "note": "often used as an adverb without a particle (今日行く)"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "きょう",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "話している人がいま過ごしているこの日",
     "example": "今日は朝からずっと雨が降っています。",
     "cefr": "A1",
     "frames": [
      "今日は",
      "今日の〜",
      "今日中に"
     ]
    }
   ]
  },
  {
   "lemma": "今日",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "note": "often used as an adverb or in 今日では"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "こんにち",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "昔と比べたときの、現在の時代や世の中",
     "example": "今日の社会では、携帯電話が欠かせない道具になった。",
     "cefr": "B2",
     "frames": [
      "今日では",
      "今日の〜"
     ],
     "labels": {
      "formality": "formal"
     }
    }
   ]
  }
 ]
}
```

### 8. 明日 (pilot)

```json
{
 "language": "ja",
 "headword": "明日",
 "lexemes": [
  {
   "lemma": "明日",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count",
    "note": "Also used without a particle as an adverb of time."
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "あした",
     "notation": "kana"
    },
    {
     "variety": "ja-JP",
     "reading": "あす",
     "notation": "kana",
     "note": "careful"
    },
    {
     "variety": "ja-JP",
     "reading": "みょうにち",
     "notation": "kana",
     "note": "careful"
    }
   ],
   "senses": [
    {
     "definition": "今日が終わったあとの次の日",
     "example": "明日は朝早く起きなければならない。",
     "cefr": "A1",
     "antonyms": [
      {
       "lemma": "昨日",
       "pos": "NOUN"
      }
     ]
    },
    {
     "definition": "これから先にやって来る近い将来",
     "example": "子どもたちは明日の社会を担う大切な存在だ。",
     "cefr": "B2",
     "labels": {
      "figurative": true
     },
     "frames": [
      "明日の〜"
     ]
    }
   ]
  }
 ]
}
```

### 9. 高い (pilot)

```json
{
 "language": "ja",
 "headword": "高い",
 "lexemes": [
  {
   "lemma": "高い",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "conjugation": "i"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "たかい",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "下から上までの長さが大きい。上の方にある",
     "example": "このビルは町でいちばん高い建物です。",
     "cefr": "A1",
     "frames": [
      "〜が高い",
      "背が高い"
     ],
     "antonyms": [
      {
       "lemma": "低い",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "値段が普通より多くかかる",
     "example": "駅前のレストランは少し高いけど、おいしいよ。",
     "cefr": "A1",
     "frames": [
      "〜が高い"
     ],
     "antonyms": [
      {
       "lemma": "安い",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "程度や地位、能力などが普通より上である",
     "example": "今日は気温が高いので、水をたくさん飲みましょう。",
     "cefr": "A2",
     "frames": [
      "〜が高い"
     ],
     "antonyms": [
      {
       "lemma": "低い",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "音や声の調子が上の方にある",
     "example": "彼女は高い声で楽しそうに歌っていた。",
     "cefr": "B1",
     "antonyms": [
      {
       "lemma": "低い",
       "pos": "ADJ"
      }
     ]
    }
   ]
  }
 ]
}
```

### 10. いい (pilot)

```json
{
 "language": "ja",
 "headword": "いい",
 "lexemes": [
  {
   "lemma": "いい",
   "pos": "ADJ",
   "kind": "word",
   "spellings": [
    {
     "text": "いい",
     "script": "Hira"
    },
    {
     "text": "良い",
     "script": "Jpan"
    }
   ],
   "grammar": {
    "conjugation": "i",
    "irregular": true,
    "note": "Colloquial form of よい; every form except いい itself is built on よ- (よく, よかった)."
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "いい",
     "notation": "kana"
    }
   ],
   "forms": [
    {
     "form": "よく",
     "features": {},
     "note": "adverbial form",
     "reading": "よく",
     "notation": "kana"
    },
    {
     "form": "よかった",
     "features": {
      "Tense": "Past"
     },
     "reading": "よかった",
     "notation": "kana"
    },
    {
     "form": "よくない",
     "features": {
      "Polarity": "Neg"
     },
     "reading": "よくない",
     "notation": "kana"
    },
    {
     "form": "よくて",
     "features": {
      "VerbForm": "Conv"
     },
     "reading": "よくて",
     "notation": "kana"
    },
    {
     "form": "よければ",
     "features": {
      "Mood": "Cnd"
     },
     "reading": "よければ",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "質や程度などがすぐれていて好ましい",
     "example": "このレストランは料理も雰囲気もいい。",
     "cefr": "A1",
     "frames": [
      "〜がいい"
     ],
     "antonyms": [
      {
       "lemma": "悪い",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "差し支えがなく、それで十分だ",
     "example": "疲れたら、少し休んでもいいですよ。",
     "cefr": "A2",
     "frames": [
      "〜てもいい",
      "〜ばいい",
      "〜でいい"
     ]
    },
    {
     "definition": "目的や状況にちょうど合っている",
     "example": "今日は洗濯にちょうどいい天気だ。",
     "cefr": "A2",
     "frames": [
      "〜にいい"
     ]
    },
    {
     "definition": "性格がやさしく、親切だ",
     "example": "隣の人はとてもいい人で、よく野菜をくれる。",
     "cefr": "A2"
    }
   ]
  }
 ]
}
```

### 11. 静か (pilot)

```json
{
 "language": "ja",
 "headword": "静か",
 "lexemes": [
  {
   "lemma": "静か",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "conjugation": "na"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "しずか",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "物音や声が聞こえず、ひっそりしている",
     "example": "図書館の中では静かにしてください。",
     "cefr": "A1",
     "frames": [
      "静かにする",
      "静かな〜"
     ],
     "antonyms": [
      {
       "lemma": "うるさい",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "動きが少なく、穏やかで落ち着いている",
     "example": "今朝は風もなく、海がとても静かだ。",
     "cefr": "B1"
    },
    {
     "definition": "性格や態度がおとなしく、落ち着いている",
     "example": "兄は静かな人だが、意見ははっきり言う。",
     "cefr": "B1"
    }
   ]
  }
 ]
}
```

### 12. きれい (pilot)

```json
{
 "language": "ja",
 "headword": "きれい",
 "lexemes": [
  {
   "lemma": "きれい",
   "pos": "ADJ",
   "kind": "word",
   "spellings": [
    {
     "text": "きれい",
     "script": "Hira",
     "varieties": [
      "ja-JP"
     ]
    },
    {
     "text": "綺麗",
     "script": "Jpan",
     "varieties": [
      "ja-JP"
     ]
    },
    {
     "text": "奇麗",
     "script": "Jpan",
     "varieties": [
      "ja-JP"
     ]
    }
   ],
   "grammar": {
    "conjugation": "na"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "きれい",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "見た目や声などが美しく、感じがよい",
     "example": "窓から見える夕日がとてもきれいだ。",
     "cefr": "A1",
     "antonyms": [
      {
       "lemma": "醜い",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "汚れやごみがなく、清潔である",
     "example": "料理の前には、手をきれいに洗いましょう。",
     "cefr": "A1",
     "antonyms": [
      {
       "lemma": "汚い",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "あとに何も残らないほど、すっかり",
     "example": "借りたお金のことをきれいに忘れていた。",
     "cefr": "B2",
     "frames": [
      "きれいに〜する"
     ],
     "grammar": {
      "note": "used adverbially in the form きれいに"
     }
    }
   ]
  }
 ]
}
```

### 13. 好き (pilot)

```json
{
 "language": "ja",
 "headword": "好き",
 "lexemes": [
  {
   "lemma": "好き",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "conjugation": "na"
   },
   "spellings": [
    {
     "text": "好き",
     "script": "Jpan"
    },
    {
     "text": "すき",
     "script": "Hira"
    }
   ],
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "すき",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "心がひかれて、気に入っている様子",
     "example": "私は子どもの頃から絵を描くのが好きです。",
     "cefr": "A1",
     "frames": [
      "〜が好き",
      "〜のが好き",
      "好きな〜"
     ],
     "antonyms": [
      {
       "lemma": "嫌い",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "自分の思うとおりにふるまう様子",
     "example": "時間はたっぷりあるから、好きにしていいよ。",
     "cefr": "A2",
     "frames": [
      "好きにする",
      "好きなように〜"
     ]
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "好く",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 14. 本 (pilot)

```json
{
 "language": "ja",
 "headword": "本",
 "lexemes": [
  {
   "lemma": "本",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count",
    "classifier": "冊"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "ほん",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "文字や絵を印刷した紙をとじて一冊にしたもの",
     "example": "寝る前に少し本を読むのが好きです。",
     "cefr": "A1",
     "frames": [
      "本を読む"
     ]
    }
   ]
  },
  {
   "lemma": "本",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "note": "counter used after a number; read ぽん or ぼん after some numbers: 一本 いっぽん, 三本 さんぼん"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "ほん",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "細長い物や映画、電話などを数えるのに使う語",
     "example": "冷蔵庫にビールが三本入っています。",
     "cefr": "A1",
     "frames": [
      "〜本"
     ]
    }
   ]
  },
  {
   "lemma": "本",
   "pos": "DET",
   "kind": "word",
   "grammar": {
    "position": "Attr",
    "note": "prefix placed directly before a noun"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "ほん",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "いま話題にしている、自分たちが関わるこの",
     "example": "本大会には全国から多くの選手が参加した。",
     "cefr": "B2",
     "frames": [
      "本〜"
     ],
     "labels": {
      "formality": "formal"
     }
    }
   ]
  }
 ]
}
```

### 15. 鉛筆 (pilot)

```json
{
 "language": "ja",
 "headword": "鉛筆",
 "lexemes": [
  {
   "lemma": "鉛筆",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count",
    "classifier": "本"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "えんぴつ",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "細長い木の中に芯を入れた、字や絵をかく道具",
     "example": "テストには鉛筆と消しゴムを持ってきてください。",
     "cefr": "A1",
     "frames": [
      "鉛筆で書く",
      "鉛筆を削る"
     ]
    }
   ]
  }
 ]
}
```

### 16. は (pilot)

```json
{
 "language": "ja",
 "headword": "は",
 "lexemes": [
  {
   "lemma": "は",
   "pos": "PART",
   "kind": "word",
   "grammar": {
    "invariable": true,
    "note": "Written は but always pronounced わ as a particle."
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "わ",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "文の話題となる事柄を示すのに使う",
     "example": "私の趣味は写真を撮ることです。",
     "cefr": "A1",
     "frames": [
      "AはBだ",
      "〜は"
     ]
    },
    {
     "definition": "他のものと比べて対比や区別を示すのに使う",
     "example": "肉は食べますが、魚は食べません。",
     "cefr": "A2",
     "frames": [
      "Aは〜が、Bは〜",
      "〜ではない"
     ]
    }
   ]
  }
 ]
}
```

### 17. を (pilot)

```json
{
 "language": "ja",
 "headword": "を",
 "lexemes": [
  {
   "lemma": "を",
   "pos": "ADP",
   "kind": "word",
   "grammar": {
    "invariable": true,
    "note": "case particle placed after a noun; pronounced like お"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "お",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "動作が直接向かう対象であることの印として使う",
     "example": "毎朝コーヒーを一杯飲んでから出かけます。",
     "cefr": "A1",
     "frames": [
      "〜を食べる",
      "〜を見る"
     ]
    },
    {
     "definition": "移動する時に通っていく場所の印として使う",
     "example": "子どもたちが公園の中を元気に走っている。",
     "cefr": "A2",
     "frames": [
      "〜を歩く",
      "〜を通る",
      "〜を渡る"
     ]
    },
    {
     "definition": "離れていく場所や出発点の印として使う",
     "example": "来年の春に大学を卒業する予定です。",
     "cefr": "A2",
     "frames": [
      "〜を出る",
      "〜を降りる"
     ]
    }
   ]
  }
 ]
}
```

### 18. いらっしゃる (pilot)

```json
{
 "language": "ja",
 "headword": "いらっしゃる",
 "lexemes": [
  {
   "lemma": "いらっしゃる",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "godan",
    "irregular": true,
    "valency": "Intr",
    "note": "ます form, imperative and て form are irregular"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "いらっしゃる",
     "notation": "kana"
    }
   ],
   "forms": [
    {
     "form": "いらっしゃいます",
     "features": {
      "Polite": "Form"
     },
     "reading": "いらっしゃいます",
     "notation": "kana"
    },
    {
     "form": "いらっしゃって",
     "features": {
      "VerbForm": "Conv"
     },
     "reading": "いらっしゃって",
     "notation": "kana"
    },
    {
     "form": "いらっしゃらない",
     "features": {
      "Polarity": "Neg"
     },
     "reading": "いらっしゃらない",
     "notation": "kana"
    },
    {
     "form": "いらっしゃい",
     "features": {
      "Mood": "Imp"
     },
     "reading": "いらっしゃい",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "敬うべき人が来る、または行く",
     "example": "先生は明日の午後、こちらへいらっしゃいます。",
     "cefr": "B1",
     "frames": [
      "〜にいらっしゃる",
      "〜へいらっしゃる"
     ],
     "labels": {
      "politeness": "honorific"
     }
    },
    {
     "definition": "敬うべき人がある場所にいる",
     "example": "部長は今日は一日中会社にいらっしゃるそうです。",
     "cefr": "B1",
     "frames": [
      "〜にいらっしゃる",
      "〜ていらっしゃる"
     ],
     "labels": {
      "politeness": "honorific"
     }
    }
   ]
  }
 ]
}
```

### 19. 参る (pilot)

```json
{
 "language": "ja",
 "headword": "参る",
 "lexemes": [
  {
   "lemma": "参る",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "godan",
    "valency": "Intr"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "まいる",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "「行く」「来る」をへりくだって丁寧に言う語",
     "example": "明日の午後、私がそちらへ参ります。",
     "cefr": "A2",
     "frames": [
      "〜へ参る",
      "〜に参る",
      "〜て参る"
     ],
     "labels": {
      "politeness": "humble"
     }
    },
    {
     "definition": "どうしてよいか分からず、ひどく困る",
     "example": "毎日こう暑くては、本当に参ってしまう。",
     "cefr": "B2",
     "frames": [
      "〜に参る"
     ],
     "labels": {
      "formality": "informal"
     }
    },
    {
     "definition": "相手の強さに負けたと認める",
     "example": "こんなに強いとは思わなかった、参ったよ。",
     "cefr": "B2",
     "grammar": {
      "note": "usually in the past form 参った"
     },
     "labels": {
      "formality": "informal"
     }
    },
    {
     "definition": "神仏や墓の前に行って拝む",
     "example": "毎年お盆には家族で先祖の墓に参る。",
     "cefr": "B2",
     "frames": [
      "〜に参る"
     ]
    }
   ]
  }
 ]
}
```

### 20. です (pilot)

```json
{
 "language": "ja",
 "headword": "です",
 "lexemes": [
  {
   "lemma": "です",
   "pos": "AUX",
   "kind": "word",
   "grammar": {
    "conjugation": "irregular",
    "irregular": true,
    "note": "polite counterpart of the copula だ"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "です",
     "notation": "kana"
    }
   ],
   "forms": [
    {
     "form": "でした",
     "features": {
      "Tense": "Past",
      "Polite": "Form"
     },
     "reading": "でした",
     "notation": "kana"
    },
    {
     "form": "でして",
     "features": {
      "VerbForm": "Conv",
      "Polite": "Form"
     },
     "reading": "でして",
     "notation": "kana"
    },
    {
     "form": "ではありません",
     "features": {
      "Polarity": "Neg",
      "Polite": "Form"
     },
     "reading": "ではありません",
     "notation": "kana"
    },
    {
     "form": "じゃありません",
     "features": {
      "Polarity": "Neg",
      "Polite": "Form"
     },
     "reading": "じゃありません",
     "notation": "kana",
     "note": "casual form of ではありません"
    },
    {
     "form": "でしょう",
     "features": {
      "Polite": "Form"
     },
     "reading": "でしょう",
     "notation": "kana",
     "note": "presumptive form: expresses a guess"
    }
   ],
   "senses": [
    {
     "definition": "名詞などについて、丁寧に断定する意味を表す",
     "example": "私の兄は駅の近くの病院の医者です。",
     "cefr": "A1",
     "frames": [
      "〜は〜です"
     ],
     "labels": {
      "politeness": "polite"
     }
    },
    {
     "definition": "形容詞などのあとについて、言い方を丁寧にする",
     "example": "今日は朝からとても暑いですね。",
     "cefr": "A1",
     "frames": [
      "形容詞＋です"
     ],
     "labels": {
      "politeness": "polite"
     }
    }
   ],
   "relations": [
    {
     "type": "confusable",
     "lemma": "ます",
     "pos": "AUX",
     "why": "both are polite endings; ます follows verbs, です follows nouns and adjectives"
    }
   ]
  }
 ]
}
```

### 21. 俺 (pilot)

```json
{
 "language": "ja",
 "headword": "俺",
 "lexemes": [
  {
   "lemma": "俺",
   "pos": "PRON",
   "kind": "word",
   "spellings": [
    {
     "text": "俺",
     "script": "Jpan"
    },
    {
     "text": "おれ",
     "script": "Hira"
    }
   ],
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "おれ",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "男性がくだけた場面で自分を指して言う語",
     "example": "俺はもう少しここで待っているよ。",
     "cefr": "A2",
     "labels": {
      "formality": "informal",
      "politeness": "familiar"
     },
     "grammar": {
      "note": "used mostly by men, to equals or those below"
     }
    }
   ]
  }
 ]
}
```

### 22. コンピューター (pilot)

```json
{
 "language": "ja",
 "headword": "コンピューター",
 "lexemes": [
  {
   "lemma": "コンピューター",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count",
    "classifier": "台"
   },
   "spellings": [
    {
     "text": "コンピューター",
     "script": "Kana",
     "varieties": [
      "ja-JP"
     ]
    },
    {
     "text": "コンピュータ",
     "script": "Kana",
     "varieties": [
      "ja-JP"
     ]
    }
   ],
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "こんぴゅーたー",
     "notation": "kana",
     "spelling": "コンピューター"
    },
    {
     "variety": "ja-JP",
     "reading": "こんぴゅーた",
     "notation": "kana",
     "spelling": "コンピュータ"
    }
   ],
   "senses": [
    {
     "definition": "計算や情報の処理を自動で速く行う電子の機械",
     "example": "このコンピューターは古くて、動きがとても遅い。",
     "cefr": "A1"
    }
   ]
  }
 ]
}
```

### 23. 勉強 (pilot)

```json
{
 "language": "ja",
 "headword": "勉強",
 "lexemes": [
  {
   "lemma": "勉強",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Mass"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "べんきょう",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "知識や技術を身につけるために努力すること",
     "example": "毎晩二時間、日本語を勉強しています。",
     "cefr": "A1",
     "frames": [
      "〜を勉強する",
      "勉強する"
     ]
    },
    {
     "definition": "これから役に立つ経験や教訓",
     "example": "失敗したけれど、いい勉強になりました。",
     "cefr": "B1",
     "frames": [
      "勉強になる"
     ]
    },
    {
     "definition": "商人が品物の値段を安くすること",
     "example": "たくさん買ってくれたら、少し勉強しますよ。",
     "cefr": "B2",
     "frames": [
      "勉強する"
     ],
     "labels": {
      "formality": "informal"
     }
    }
   ]
  }
 ]
}
```

### 24. 気をつける (pilot)

```json
{
 "language": "ja",
 "headword": "気をつける",
 "lexemes": [
  {
   "lemma": "気をつける",
   "pos": "VERB",
   "kind": "multiword",
   "components": [
    {
     "text": "気",
     "lemma": "気",
     "inflects": "fixed"
    },
    {
     "text": "を",
     "lemma": "を",
     "inflects": "fixed"
    },
    {
     "text": "つける",
     "lemma": "付ける",
     "inflects": "head"
    }
   ],
   "spellings": [
    {
     "text": "気をつける",
     "script": "Jpan"
    },
    {
     "text": "気を付ける",
     "script": "Jpan"
    }
   ],
   "grammar": {
    "conjugation": "ichidan",
    "valency": "Intr"
   },
   "pronunciations": [
    {
     "variety": "ja-JP",
     "reading": "きをつける",
     "notation": "kana"
    }
   ],
   "senses": [
    {
     "definition": "危ないことや失敗が起きないよう注意を払う",
     "example": "道が凍っているので、転ばないように気をつけてください。",
     "cefr": "A2",
     "frames": [
      "〜に気をつける",
      "〜ように気をつける"
     ]
    }
   ],
   "relations": [
    {
     "type": "confusable",
     "lemma": "気がつく",
     "pos": "VERB",
     "why": "気がつく means to notice, 気をつける means to be careful; they differ only in the particle and the verb"
    }
   ]
  }
 ]
}
```

## Questions about the shape

Japanese is new to this dictionary. After the candidates, answer each of these in a few lines, from what the entries show:

1. A pronunciation is a reading in hiragana, with no pitch accent and no romanization. Is that enough for a learner's
   dictionary of Japanese, and if not, what should be added and in what notation?
2. The lemma is written as dictionaries head it (kanji with okurigana, or kana); another way of writing it is listed
   only when print uses it, with its script. Are the lemmas and the listed spellings the right ones?
3. A verb is godan, ichidan or irregular; an adjective i or na; a transitive verb and its intransitive partner name each
   other. Does a learner's dictionary of Japanese mark anything else (a noun that takes する, a の-adjective)?
4. A noun's counter is given as its classifier. Are the counters right, and is one counter enough?
5. Politeness is one of honorific, humble, polite, familiar. Are these the right four, and rightly given here?
6. The level is a CEFR estimate. Would a JLPT level serve a learner better?
7. Limits are counted in characters: a definition 10 to 35, an example 12 to 40. Are the definitions written as a
   Japanese learner's dictionary writes them?
