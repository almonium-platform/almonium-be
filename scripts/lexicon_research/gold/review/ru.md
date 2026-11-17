# Example entries for Russian: review

These are entries of a learner's dictionary of Russian, written by a model. The best four or five will be
shown to the writing model as examples of the house style, so an error left in one is copied into
thousands of entries. Review each candidate strictly.

For each candidate, check:

1. **Facts.** Is anything wrong: a meaning, a gender, a plural or another form, a transcription, a frame,
   a label, a region, a level that is off by more than one step?
2. **The sense list.** Would a monolingual learner's dictionary of Russian number exactly these senses?
   Name a common sense that is missing, and a sense that is only a shade of another.
3. **Style.** Does every definition keep the rules below (6 to 15 words, plain words, a phrase and not a
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

Most Russian print writes ё as е. Write the lemma with ё where the word has it (ёлка, ребёнок, ещё) and list the spelling with е beside it, without varieties; the headword may arrive in either. The spelling of before 1918 (міръ, хлѣбъ) is turned into today's by a rule and is not listed. Give "spellings" whenever the lemma is written more than one way: every current spelling with the varieties that use it ("colour" en-GB, en-AU, en-IN and "color" en-US), and a spelling only older books use with "pre_reform": true. A variant chosen by the neighbouring sounds is a spelling of the same lexeme too (в and во, с and со, к and ко, о, об and обо, же and ж): list them without varieties. The other spellings are never written as entries of their own, so list them all; a lemma written one way has no "spellings". When two spellings are said differently (Spanish "período" and "periodo"), each reading names its "spelling", and a sense that only some of the spellings have lists them under "spellings".

When the request has a line "Covered:", the headword is already listed as a spelling of the lexemes named there, and they are written. Do not write them again: return only lexemes of this spelling that are other words (English "tire", to make someone tired, when "tire" is covered as a spelling of the noun "tyre"), and if there are none, reject with "spelling of" and the lemma. For spellings and forms the varieties of Russian are only these: ru-RU; where a sense is used is said under Regions, which reach further.

# Pronunciation

Broad IPA, as the dictionaries of the language print it and with the conventions given below, for each of these varieties and no other: ru-RU; between slashes, with a primary stress mark ˈ on every word including monosyllables, and a dot at every syllable boundary, also before a stress mark inside the word (/ə.ˈlaʊ/, not /əˈlaʊ/). When a variety says the word in more than one way, give the most common first; a reading after the first may carry a "note": less_common, older or newer (said mainly by older or younger speakers), careful or casual, regional (in part of the variety only), disputed (considered wrong by some). Give a note only when you are sure of it. Use only these symbols:
ru-RU: consonants p b t d k ɡ f v s z ʂ ʐ ɕ x ts tɕ m n l r j; vowels a e i o u ɨ ɐ ə ɪ; marks ˈ ˌ . ː ʲ. Standard Russian as spoken in Moscow. Palatalization is written with ʲ after the consonant (нет is /ˈnʲet/). Unstressed vowels are written reduced: о and а are ɐ in the syllable before the stress and at the start of a word and ə elsewhere (молоко is /mə.lɐ.ˈko/); е and я after a soft consonant are ɪ (река is /rʲɪ.ˈka/); unstressed у and и keep u and i. ы is ɨ. A voiced consonant is written voiceless at the end of a word and before a voiceless one (хлеб is /ˈxlʲep/, водка is /ˈvot.kə/). ш and ж are ʂ and ʐ, щ is ɕː, ч is tɕ, ц is ts, hard л is l. A letter that is not said is not written, and a letter said as another is written as it is said (солнце is /ˈson.tsə/, что is /ˈʂto/, его is /jɪ.ˈvo/).
Russian does not write stress, so each pronunciation also carries a reading: the word with an acute accent after its stressed vowel (рука́, за́мок, замо́к). The letter ё is always stressed and takes no accent (ёлка, ребёнок). A word of one syllable has no mark.

# Grammar and forms

Grammar: only what the language needs, and only these keys, with these values: gender (Masc, Fem, Neut; Com for a noun that is masculine for a man and feminine for a woman without changing its form: el/la estudiante, колега), number (Sing or Plur, only for a noun used in one number), countability (Count, Mass, Both), valency (Tr for a verb with a direct object, Intr for one without, also when its object takes a preposition or the dative: German "helfen", Portuguese "lidar com"; Both, Refl, Impers), irregular (true/false), conjugation (1, 2, 3 by the infinitive ending in the language's own order; German: strong, weak, mixed), auxiliary (have, be; both for a verb that takes one in some senses and the other in others, each sense then giving its own: Italian "ho iniziato a leggere", "la lezione è iniziata"), separable and separable_prefix, reflexive, declension (1-4, weak, strong, mixed, adjectival), plural_class (German: -e, -¨e, -en, -n, -er, -¨er, -s, -nen, -¨ for an umlaut alone (Vater, Väter), or - for no change), aspect (Imp, Perf), animacy (Anim, Inan), invariable, position (Attr, Pred), comparable, mainly, and note for anything else, in one short clause in English. Where one variety differs from the rest, put only the difference under "by_variety" (German "E-Mail": gender Fem, by_variety de-AT and de-CH: gender Neut). What holds for one sense only goes in that sense's own "grammar", not the lexeme's: an adjective that stands only before a noun in one of its senses has position Attr on that sense. A sense's grammar may carry a short note too.

A value of countability, valency, position, number or comparable says what holds without exception. Give it as a learner's dictionary of Russian prints it, and leave out the one you are not sure of. Where it holds for most uses but not all, name the key under "mainly" as well: an adjective that usually stands before a noun but can follow a verb has "position": "Attr", "mainly": ["position"]; a noun that is uncountable except when kinds of it are meant (soap, two soaps) has "countability": "Mass", "mainly": ["countability"]; a noun nearly always met in the plural has "number": "Plur", "mainly": ["number"]. A verb with a rarer use of the other kind has valency Both, or Intr with "mainly": ["valency"]. comparable false is only for an adjective that cannot be compared at all (wooden, daily); one that is seldom compared has it with "mainly": ["comparable"].

Forms: Nouns: the genitive singular, the nominative plural and the genitive plural, and any other form whose stress or stem a learner would not expect from these (ру́ку; в лесу́, Case=Loc; ча́ю, Case=Par), and the form used after numbers when it is not the genitive plural a learner would build, with Number=Count (пять челове́к, два часа́, пять лет). Verbs: the present or future 1sg, 2sg and 3pl, the past masculine and feminine, and the imperative 2sg. Adjectives: the short forms when the stress moves in them (мо́лод, молода́, мо́лодо) and the comparative when irregular (хороший, лучше). List the forms this names even where they are regular, and beyond them only a form a learner could not build by rule; every other form is generated later from the grammar. Features use Universal Dependencies names and values only (Number=Plur, Case=Gen, Tense=Past, Person=3, VerbForm=Part, Mood=Sub); a form that fills several cells joins the values of one feature with a comma (VerbForm=Fin,Part; Person=1,3), and is listed once for each when two features differ at once. A form used only in one construction says so in a short "note" (Spanish "electo": only in the passive). A form used only in some varieties lists them ("learnt" en-GB). A form that belongs to only some senses lists their numbers, counting from 1 ("brethren" for the religious sense of "brother"). Each listed form of two or more syllables carries its own reading (руки́, ру́ку), and no ipa.

# Senses

Senses in descending frequency of use. Only senses a reader meets in books and ordinary text; no technical, dialectal or obsolete senses unless the headword is itself rare. Two senses are separate only when a monolingual learner's dictionary of Russian would number them separately. Most words have one or two senses; give more only for a word that is truly polysemous. Do not split shades of one meaning, and do not make senses out of subject areas or facts about the thing ("potato" as a plant, a food and a crop is one sense). A use that every word of its kind has is not a sense (a drink as one glass of it), and neither is a meaning the word has only inside one idiom. A difference of construction alone is not a sense either: it is a frame, and a construction used in part of the language only is said in the sense's grammar note (Spanish "participar de algo" beside "participar en algo"). A reflexive or impersonal use with a meaning of its own is a sense, with its valency in that sense's grammar and, when the pronoun does not mean "oneself", a note saying what it does (each other, by accident). Include a common figurative sense ("a climate of fear"). Give only senses the Russian word has: a meaning that only its English equivalent has does not belong here.

Definition. In Russian, 6 to 15 words, plain words a learner knows, a phrase not a sentence, in the style of a learner's dictionary. The headword and words of its family must not appear in it, except the name of the country in the definition of its people and language ("French": of France). It holds for every use of the sense and says no more than the word means: do not narrow it to the typical case (a judge decides disputes of every kind, not only guilt; a message need not be short). No example inside it, no list of instances ("such as China and Japan"), no synonym added at the end, and no formula such as "the act of", "the state of being", "relating to" or "characteristic of". A verb is defined as a verb, a noun as a noun, an adjective by an adjective phrase and not by a clause ("so that ..."). A word with no meaning to paraphrase (an interjection, a particle, a conjunction) is defined by what it is used for, opening as the learner's dictionaries of Russian open such a definition (English "used to ..."). A remark on grammar, position or field ("before an adjective", "at the start of a sentence", "in boxing") is not part of a definition: it is a frame, a note in the sense's grammar, or a domain label.

Example. One sentence in Russian of 6 to 14 words, natural, no proper names, no stereotype of a people or a group, never a question with its answer, that shows this sense and its frame if it has one. An interjection stands inside its sentence, set off by a comma, not as a sentence of its own. Vary the examples: not always the past tense, not always a third-person subject, not always starting with an article.

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
Regions. A sense used only or mainly in part of the Russian-speaking world lists where, each with "variety" and "extent". A variety is the language with a country (ru-RU, ru-BY, ru-KZ). The extent is "only" when the sense is not used elsewhere and "mainly" when it is used everywhere but chiefly there ("lead", a dog's strap: en-GB only). Give "only" when you are sure the rest of the language does not use the sense, otherwise "mainly", and name the area rather than two or three of its countries when the sense is used across it. A region says where the word is used, not where the thing is found: the name of an office or a district that one country has carries no region. Leave regions out for what the whole language uses, and never put a region in a definition. When a word is neutral in one variety and offensive or vulgar in another, those are separate senses, each with its regions and labels.

Antonyms. For a sense with a true opposite, the opposite's lemma and part of speech (allow and forbid, aware and unaware). A word that only contrasts with the sense is not its opposite (hill and valley, write and read, perhaps and definitely): leave it out. No synonyms.

A verb lists its aspect partner as a relation (делать and сделать). A verb in -ся is a lemma of its own, written under its own spelling, not a sense of the verb without it (учиться is not under учить). A word used only as a predicate (нельзя, надо, рад) has position Pred. Russian has no articles, so a definition may have as few as four words.

# Relations

Relations point from this lexeme to the word it is made from or is confused with, each as type, lemma and part of speech; the reverse links are filled in later, so the base word does not list its derivatives. Types: diminutive_of (хлопчик of хлопець), augmentative_of, endearing_of (сонечко of сонце), feminine_of (actress of actor), agent_noun_of (teacher of teach), verbal_noun_of (читання of читати), abstract_noun_of (kindness of kind), adjective_of (urban of city), adverb_of (quickly of quick), derived_from (any other derivation), aspect_partner (зробити of робити), and confusable (a different word learners mix it up with, with "why" in English). A relation names another word, never a lexeme of this headword with the same part of speech; one that holds for only some of the senses lists their numbers under "senses". derived_from is for a word built from another inside the language, by a prefix, a suffix or a change of part of speech, not for two words with a common ancestor (French "permettre" is not derived from "mettre"). Give only relations you are sure of.

Write the same way for every word, so that neighbouring entries read as one dictionary. Give nothing you are unsure of; an empty field is better than a guess.

## Candidates

### 1. стоимость (noun)

```json
{
 "language": "ru",
 "headword": "стоимость",
 "lexemes": [
  {
   "lemma": "стоимость",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Fem",
    "animacy": "Inan",
    "countability": "Both"
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/ˈsto.ɪ.məsʲtʲ/",
     "reading": "сто́имость"
    }
   ],
   "forms": [
    {
     "form": "стоимости",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "reading": "сто́имости"
    },
    {
     "form": "стоимости",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "reading": "сто́имости"
    },
    {
     "form": "стоимостей",
     "features": {
      "Case": "Gen",
      "Number": "Plur"
     },
     "reading": "сто́имостей"
    }
   ],
   "senses": [
    {
     "definition": "сумма денег, которую нужно заплатить за что-либо",
     "example": "Стоимость билета зависит от дня недели и времени поездки.",
     "cefr": "B1",
     "frames": [
      "стоимость чего"
     ]
    },
    {
     "definition": "ценность товара, выраженная в затраченном на него общественном труде",
     "example": "В экономике стоимость товара не всегда совпадает с его ценой.",
     "cefr": "C1",
     "labels": {
      "domain": "economics"
     }
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "стоить",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 2. мальчик (noun)

```json
{
 "language": "ru",
 "headword": "мальчик",
 "lexemes": [
  {
   "lemma": "мальчик",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "animacy": "Anim",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/ˈmalʲ.tɕɪk/",
     "reading": "ма́льчик"
    }
   ],
   "forms": [
    {
     "form": "мальчика",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "reading": "ма́льчика"
    },
    {
     "form": "мальчики",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "reading": "ма́льчики"
    },
    {
     "form": "мальчиков",
     "features": {
      "Case": "Gen",
      "Number": "Plur"
     },
     "reading": "ма́льчиков"
    }
   ],
   "senses": [
    {
     "definition": "ребёнок или подросток мужского пола",
     "example": "Во дворе маленький мальчик учится кататься на велосипеде.",
     "cefr": "A1",
     "antonyms": [
      {
       "lemma": "девочка",
       "pos": "NOUN"
      }
     ]
    },
    {
     "definition": "подросток, который служит в лавке или работает на посылках",
     "example": "Хозяин послал мальчика из лавки отнести покупку на дом.",
     "cefr": "C1",
     "labels": {
      "time": "dated"
     }
    }
   ]
  }
 ]
}
```

### 3. чемпионат (noun)

```json
{
 "language": "ru",
 "headword": "чемпионат",
 "lexemes": [
  {
   "lemma": "чемпионат",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "animacy": "Inan",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/tɕɪm.pʲɪ.ɐ.ˈnat/",
     "reading": "чемпиона́т"
    }
   ],
   "forms": [
    {
     "form": "чемпионата",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "reading": "чемпиона́та"
    },
    {
     "form": "чемпионаты",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "reading": "чемпиона́ты"
    },
    {
     "form": "чемпионатов",
     "features": {
      "Case": "Gen",
      "Number": "Plur"
     },
     "reading": "чемпиона́тов"
    }
   ],
   "senses": [
    {
     "definition": "спортивное соревнование, в котором определяют лучшего спортсмена или лучшую команду",
     "example": "В следующем году наша страна примет чемпионат мира по футболу.",
     "cefr": "B1",
     "frames": [
      "чемпионат по чему",
      "чемпионат мира"
     ]
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "чемпион",
     "pos": "NOUN"
    }
   ]
  }
 ]
}
```

### 4. папа (noun)

```json
{
 "language": "ru",
 "headword": "папа",
 "lexemes": [
  {
   "lemma": "папа",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "animacy": "Anim",
    "countability": "Count",
    "note": "masculine noun declined like a feminine noun in -а"
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/ˈpa.pə/",
     "reading": "па́па"
    }
   ],
   "forms": [
    {
     "form": "папы",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "reading": "па́пы"
    },
    {
     "form": "папы",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "reading": "па́пы"
    },
    {
     "form": "пап",
     "features": {
      "Case": "Gen",
      "Number": "Plur"
     }
    }
   ],
   "senses": [
    {
     "definition": "отец, обычно в речи детей или при обращении к нему",
     "example": "Мой папа каждое утро отвозит нас в школу на машине.",
     "cefr": "A1",
     "antonyms": [
      {
       "lemma": "мама",
       "pos": "NOUN"
      }
     ]
    },
    {
     "definition": "глава католической церкви, живущий в Ватикане",
     "example": "Римский папа обратился к верующим с площади перед собором.",
     "cefr": "B2",
     "frames": [
      "Римский папа"
     ]
    }
   ]
  }
 ]
}
```

### 5. установить (verb with frames)

```json
{
 "language": "ru",
 "headword": "установить",
 "lexemes": [
  {
   "lemma": "установить",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "aspect": "Perf",
    "valency": "Tr"
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/u.stə.nɐ.ˈvʲitʲ/",
     "reading": "установи́ть"
    }
   ],
   "forms": [
    {
     "form": "установлю",
     "features": {
      "Person": "1",
      "Number": "Sing",
      "Tense": "Fut",
      "Mood": "Ind",
      "VerbForm": "Fin"
     },
     "reading": "установлю́"
    },
    {
     "form": "установишь",
     "features": {
      "Person": "2",
      "Number": "Sing",
      "Tense": "Fut",
      "Mood": "Ind",
      "VerbForm": "Fin"
     },
     "reading": "устано́вишь"
    },
    {
     "form": "установят",
     "features": {
      "Person": "3",
      "Number": "Plur",
      "Tense": "Fut",
      "Mood": "Ind",
      "VerbForm": "Fin"
     },
     "reading": "устано́вят"
    },
    {
     "form": "установил",
     "features": {
      "Tense": "Past",
      "Gender": "Masc",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "reading": "установи́л"
    },
    {
     "form": "установила",
     "features": {
      "Tense": "Past",
      "Gender": "Fem",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "reading": "установи́ла"
    },
    {
     "form": "установи",
     "features": {
      "Mood": "Imp",
      "Person": "2",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "reading": "установи́"
    }
   ],
   "senses": [
    {
     "definition": "поставить и закрепить на нужном месте, подготовить к работе",
     "example": "Мастер завтра установит новую стиральную машину на кухне.",
     "cefr": "B1",
     "frames": [
      "установить что где"
     ]
    },
    {
     "definition": "ввести, создать то, что начинает существовать и действовать",
     "example": "Наши компании установили деловые связи несколько лет назад.",
     "cefr": "B2",
     "frames": [
      "установить порядок, связи, рекорд"
     ]
    },
    {
     "definition": "точно выяснить, узнать после проверки или исследования",
     "example": "Эксперты пока не смогли установить причину пожара.",
     "cefr": "B2",
     "frames": [
      "установить что",
      "установить, что…"
     ]
    }
   ],
   "relations": [
    {
     "type": "aspect_partner",
     "lemma": "устанавливать",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 6. искать (verb with frames)

```json
{
 "language": "ru",
 "headword": "искать",
 "lexemes": [
  {
   "lemma": "искать",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "aspect": "Imp",
    "valency": "Tr",
    "note": "ск alternates with щ in the present tense and imperative"
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/ɪs.ˈkatʲ/",
     "reading": "иска́ть"
    }
   ],
   "forms": [
    {
     "form": "ищу",
     "features": {
      "Tense": "Pres",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "reading": "ищу́"
    },
    {
     "form": "ищешь",
     "features": {
      "Tense": "Pres",
      "Person": "2",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "reading": "и́щешь"
    },
    {
     "form": "ищут",
     "features": {
      "Tense": "Pres",
      "Person": "3",
      "Number": "Plur",
      "VerbForm": "Fin"
     },
     "reading": "и́щут"
    },
    {
     "form": "искал",
     "features": {
      "Tense": "Past",
      "Gender": "Masc",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "reading": "иска́л"
    },
    {
     "form": "искала",
     "features": {
      "Tense": "Past",
      "Gender": "Fem",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "reading": "иска́ла"
    },
    {
     "form": "ищи",
     "features": {
      "Mood": "Imp",
      "Person": "2",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "reading": "ищи́"
    }
   ],
   "senses": [
    {
     "definition": "стараться найти кого-то или что-то потерянное или нужное",
     "example": "Я уже целый час ищу свои ключи по всей квартире.",
     "cefr": "A1",
     "frames": [
      "искать кого/что"
     ]
    },
    {
     "definition": "стараться получить или добиться чего-то нужного",
     "example": "В трудную минуту многие ищут поддержки у близких друзей.",
     "cefr": "B1",
     "frames": [
      "искать чего",
      "искать чего у кого"
     ]
    }
   ]
  }
 ]
}
```

### 7. платить (verb with frames)

```json
{
 "language": "ru",
 "headword": "платить",
 "lexemes": [
  {
   "lemma": "платить",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "aspect": "Imp",
    "valency": "Both",
    "note": "т changes to ч in the 1st person singular (плачу)"
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/plɐ.ˈtʲitʲ/",
     "reading": "плати́ть"
    }
   ],
   "forms": [
    {
     "form": "плачу",
     "reading": "плачу́",
     "features": {
      "Person": "1",
      "Number": "Sing",
      "Tense": "Pres",
      "Mood": "Ind",
      "VerbForm": "Fin"
     }
    },
    {
     "form": "платишь",
     "reading": "пла́тишь",
     "features": {
      "Person": "2",
      "Number": "Sing",
      "Tense": "Pres",
      "Mood": "Ind",
      "VerbForm": "Fin"
     }
    },
    {
     "form": "платят",
     "reading": "пла́тят",
     "features": {
      "Person": "3",
      "Number": "Plur",
      "Tense": "Pres",
      "Mood": "Ind",
      "VerbForm": "Fin"
     }
    },
    {
     "form": "платил",
     "reading": "плати́л",
     "features": {
      "Tense": "Past",
      "Gender": "Masc",
      "Number": "Sing",
      "VerbForm": "Fin"
     }
    },
    {
     "form": "платила",
     "reading": "плати́ла",
     "features": {
      "Tense": "Past",
      "Gender": "Fem",
      "Number": "Sing",
      "VerbForm": "Fin"
     }
    },
    {
     "form": "плати",
     "reading": "плати́",
     "features": {
      "Mood": "Imp",
      "Person": "2",
      "Number": "Sing",
      "VerbForm": "Fin"
     }
    }
   ],
   "senses": [
    {
     "definition": "отдавать деньги за товар, услугу или работу",
     "example": "Мы платим за квартиру в начале каждого месяца.",
     "cefr": "A1",
     "frames": [
      "платить за что",
      "платить кому",
      "платить что (деньги, налоги)"
     ],
     "grammar": {
      "valency": "Both"
     }
    },
    {
     "definition": "отвечать кому-либо на поступок или отношение своим поступком",
     "example": "Она всегда платила друзьям преданностью за их доброту.",
     "cefr": "B2",
     "frames": [
      "платить кому чем за что"
     ],
     "labels": {
      "figurative": true
     },
     "grammar": {
      "valency": "Tr"
     }
    }
   ],
   "relations": [
    {
     "type": "aspect_partner",
     "lemma": "заплатить",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 8. обеспечить (verb with frames)

```json
{
 "language": "ru",
 "headword": "обеспечить",
 "lexemes": [
  {
   "lemma": "обеспечить",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "aspect": "Perf",
    "valency": "Tr"
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/ɐ.bʲɪ.ˈspʲe.tɕitʲ/",
     "reading": "обеспе́чить"
    }
   ],
   "forms": [
    {
     "form": "обеспечу",
     "features": {
      "Tense": "Fut",
      "Person": "1",
      "Number": "Sing"
     },
     "reading": "обеспе́чу"
    },
    {
     "form": "обеспечишь",
     "features": {
      "Tense": "Fut",
      "Person": "2",
      "Number": "Sing"
     },
     "reading": "обеспе́чишь"
    },
    {
     "form": "обеспечат",
     "features": {
      "Tense": "Fut",
      "Person": "3",
      "Number": "Plur"
     },
     "reading": "обеспе́чат"
    },
    {
     "form": "обеспечил",
     "features": {
      "Tense": "Past",
      "Gender": "Masc",
      "Number": "Sing"
     },
     "reading": "обеспе́чил"
    },
    {
     "form": "обеспечила",
     "features": {
      "Tense": "Past",
      "Gender": "Fem",
      "Number": "Sing"
     },
     "reading": "обеспе́чила"
    },
    {
     "form": "обеспечь",
     "features": {
      "Mood": "Imp",
      "Person": "2",
      "Number": "Sing"
     },
     "reading": "обеспе́чь"
    }
   ],
   "senses": [
    {
     "definition": "создать условия, при которых что-то нужное точно будет",
     "example": "Организаторы должны обеспечить безопасность всех участников концерта.",
     "cefr": "B1",
     "frames": [
      "обеспечить что"
     ]
    },
    {
     "definition": "дать кому-то всё необходимое в нужном количестве",
     "example": "Государство обязано обеспечить эти семьи нормальным жильём.",
     "cefr": "B2",
     "frames": [
      "обеспечить кого чем"
     ]
    }
   ],
   "relations": [
    {
     "type": "aspect_partner",
     "lemma": "обеспечивать",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 9. морской (adjective)

```json
{
 "language": "ru",
 "headword": "морской",
 "lexemes": [
  {
   "lemma": "морской",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": false,
    "note": "no short forms"
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/mɐr.ˈskoj/",
     "reading": "морско́й"
    }
   ],
   "senses": [
    {
     "definition": "находящийся в море или у моря, живущий в море или взятый из моря",
     "example": "Летом мы любим купаться в тёплой морской воде.",
     "cefr": "A2"
    },
    {
     "definition": "предназначенный для плавания по морю или служащий во флоте",
     "example": "Мой брат хочет стать морским офицером после училища.",
     "cefr": "B1"
    }
   ],
   "relations": [
    {
     "type": "adjective_of",
     "lemma": "море",
     "pos": "NOUN"
    }
   ]
  }
 ]
}
```

### 10. огромный (adjective)

```json
{
 "language": "ru",
 "headword": "огромный",
 "lexemes": [
  {
   "lemma": "огромный",
   "pos": "ADJ",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/ɐ.ˈɡrom.nɨj/",
     "reading": "огро́мный"
    }
   ],
   "senses": [
    {
     "definition": "очень большой по размеру, количеству или силе",
     "example": "За нашим домом начинается огромный густой лес.",
     "cefr": "A2",
     "antonyms": [
      {
       "lemma": "крошечный",
       "pos": "ADJ"
      }
     ]
    }
   ]
  }
 ]
}
```

### 11. возле (two parts of speech)

```json
{
 "language": "ru",
 "headword": "возле",
 "lexemes": [
  {
   "lemma": "возле",
   "pos": "ADP",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/ˈvoz.lʲɪ/",
     "reading": "во́зле"
    }
   ],
   "senses": [
    {
     "definition": "на небольшом расстоянии от кого-либо или чего-либо, рядом с ним",
     "example": "Давай встретимся завтра возле входа в метро.",
     "cefr": "A2",
     "frames": [
      "возле кого/чего"
     ]
    }
   ]
  },
  {
   "lemma": "возле",
   "pos": "ADV",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/ˈvoz.lʲɪ/",
     "reading": "во́зле"
    }
   ],
   "senses": [
    {
     "definition": "рядом, близко, на небольшом расстоянии",
     "example": "Мать сидела возле и тихо гладила сына по голове.",
     "cefr": "B2"
    }
   ]
  }
 ]
}
```

### 12. соответственно (two parts of speech)

```json
{
 "language": "ru",
 "headword": "соответственно",
 "lexemes": [
  {
   "lemma": "соответственно",
   "pos": "ADV",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/sə.ɐt.ˈvʲets.tvʲɪn.nə/",
     "reading": "соотве́тственно"
    }
   ],
   "senses": [
    {
     "definition": "так, как подходит к чему-либо или требуется обстоятельствами",
     "example": "Работы стало больше, и платить нам должны соответственно.",
     "cefr": "B2"
    },
    {
     "definition": "используется, чтобы ввести вывод или следствие из сказанного",
     "example": "Поезд опаздывает, соответственно, мы приедем только поздно вечером.",
     "cefr": "B1",
     "grammar": {
      "note": "used as a parenthetical word, set off by commas"
     }
    },
    {
     "definition": "в том же порядке, в каком названо раньше",
     "example": "Брату и сестре сейчас десять и двенадцать лет соответственно.",
     "cefr": "B2",
     "grammar": {
      "note": "usually at the end of the clause"
     }
    }
   ],
   "relations": [
    {
     "type": "adverb_of",
     "lemma": "соответственный",
     "pos": "ADJ"
    }
   ]
  },
  {
   "lemma": "соответственно",
   "pos": "ADP",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/sə.ɐt.ˈvʲets.tvʲɪn.nə/",
     "reading": "соотве́тственно"
    }
   ],
   "senses": [
    {
     "definition": "так, как требует или указывает что-либо",
     "example": "Все работы будут выполнены соответственно требованиям заказчика.",
     "cefr": "C1",
     "frames": [
      "соответственно чему"
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

### 13. вода (labelled sense)

```json
{
 "language": "ru",
 "headword": "вода",
 "lexemes": [
  {
   "lemma": "вода",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Fem",
    "animacy": "Inan",
    "countability": "Mass"
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/vɐ.ˈda/",
     "reading": "вода́"
    }
   ],
   "forms": [
    {
     "form": "воды",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "reading": "воды́"
    },
    {
     "form": "воду",
     "features": {
      "Case": "Acc",
      "Number": "Sing"
     },
     "reading": "во́ду"
    },
    {
     "form": "воды",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "reading": "во́ды"
    },
    {
     "form": "вод",
     "features": {
      "Case": "Gen",
      "Number": "Plur"
     }
    }
   ],
   "senses": [
    {
     "definition": "прозрачная жидкость без цвета, вкуса и запаха, нужная для жизни",
     "example": "Налей мне, пожалуйста, стакан холодной воды.",
     "cefr": "A1"
    },
    {
     "definition": "моря, реки или озёра определённого места или государства",
     "example": "Корабль вошёл в территориальные воды соседней страны.",
     "cefr": "B2",
     "grammar": {
      "number": "Plur"
     }
    },
    {
     "definition": "лишние слова без важного содержания в тексте или речи",
     "example": "В твоём докладе слишком много воды, сократи его.",
     "cefr": "B2",
     "labels": {
      "formality": "informal",
      "figurative": true
     }
    }
   ]
  }
 ]
}
```

### 14. солнце (labelled sense)

```json
{
 "language": "ru",
 "headword": "солнце",
 "lexemes": [
  {
   "lemma": "солнце",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Neut",
    "animacy": "Inan"
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/ˈson.tsə/",
     "reading": "со́лнце"
    }
   ],
   "forms": [
    {
     "form": "солнца",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "reading": "со́лнца"
    },
    {
     "form": "солнца",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "reading": "со́лнца"
    },
    {
     "form": "солнц",
     "features": {
      "Case": "Gen",
      "Number": "Plur"
     }
    }
   ],
   "senses": [
    {
     "definition": "звезда, вокруг которой движется Земля и которая даёт свет и тепло",
     "example": "Утром солнце встаёт на востоке, а вечером садится на западе.",
     "cefr": "A1",
     "grammar": {
      "countability": "Count"
     }
    },
    {
     "definition": "яркий свет и тепло, которые идут с неба днём",
     "example": "Не сиди долго на солнце, а то обгоришь.",
     "cefr": "A2",
     "frames": [
      "на солнце",
      "под солнцем"
     ],
     "grammar": {
      "countability": "Mass"
     }
    },
    {
     "definition": "ласковое обращение к очень дорогому, любимому человеку",
     "example": "Солнце моё, не грусти, всё обязательно будет хорошо.",
     "cefr": "B1",
     "frames": [
      "солнце моё"
     ],
     "labels": {
      "attitude": [
       "endearing"
      ],
      "formality": "informal",
      "figurative": true
     },
     "grammar": {
      "countability": "Count"
     }
    }
   ]
  }
 ]
}
```

### 15. удар (figurative sense)

```json
{
 "language": "ru",
 "headword": "удар",
 "lexemes": [
  {
   "lemma": "удар",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "animacy": "Inan",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/u.ˈdar/",
     "reading": "уда́р",
     "notation": "stress"
    }
   ],
   "forms": [
    {
     "form": "удара",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "reading": "уда́ра"
    },
    {
     "form": "удары",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "reading": "уда́ры"
    },
    {
     "form": "ударов",
     "features": {
      "Case": "Gen",
      "Number": "Plur"
     },
     "reading": "уда́ров"
    }
   ],
   "senses": [
    {
     "definition": "резкое сильное прикосновение рукой, ногой или предметом",
     "example": "Одним точным ударом по мячу он забил гол.",
     "cefr": "B1",
     "frames": [
      "удар по чему",
      "удар чем",
      "нанести удар кому"
     ]
    },
    {
     "definition": "тяжёлое неожиданное несчастье, сильное потрясение",
     "example": "Потеря работы стала для всей семьи настоящим ударом.",
     "cefr": "B2",
     "frames": [
      "удар для кого"
     ],
     "labels": {
      "figurative": true
     }
    },
    {
     "definition": "быстрое нападение на противника, атака",
     "example": "Ночью авиация нанесла удар по складам противника.",
     "cefr": "B2",
     "frames": [
      "удар по чему",
      "нанести удар"
     ]
    },
    {
     "definition": "короткий резкий звук от столкновения или толчка",
     "example": "Где-то вдали раздался глухой удар грома.",
     "cefr": "B2",
     "frames": [
      "удар чего"
     ]
    },
    {
     "definition": "внезапное кровоизлияние в мозг, инсульт",
     "example": "Старика хватил удар, и он долго лежал в больнице.",
     "cefr": "C1",
     "labels": {
      "formality": "informal"
     }
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "ударить",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 16. подарок (figurative sense)

```json
{
 "language": "ru",
 "headword": "подарок",
 "lexemes": [
  {
   "lemma": "подарок",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "animacy": "Inan",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/pɐ.ˈda.rək/",
     "reading": "пода́рок"
    }
   ],
   "forms": [
    {
     "form": "подарка",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "reading": "пода́рка"
    },
    {
     "form": "подарки",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "reading": "пода́рки"
    },
    {
     "form": "подарков",
     "features": {
      "Case": "Gen",
      "Number": "Plur"
     },
     "reading": "пода́рков"
    }
   ],
   "senses": [
    {
     "definition": "вещь, которую дают кому-то бесплатно, чтобы сделать приятное",
     "example": "Мы купили маме красивый подарок на день рождения.",
     "cefr": "A1",
     "frames": [
      "подарок кому",
      "подарок на что",
      "в подарок"
     ]
    },
    {
     "definition": "что-то приятное и неожиданное, неожиданная удача",
     "example": "Этот тёплый солнечный день стал для нас настоящим подарком.",
     "cefr": "B2",
     "labels": {
      "figurative": true
     },
     "frames": [
      "подарок судьбы"
     ]
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "подарить",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 17. дважды (adverb)

```json
{
 "language": "ru",
 "headword": "дважды",
 "lexemes": [
  {
   "lemma": "дважды",
   "pos": "ADV",
   "kind": "word",
   "grammar": {
    "invariable": true
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/ˈdvaʐ.dɨ/",
     "reading": "два́жды"
    }
   ],
   "senses": [
    {
     "definition": "один раз и потом ещё один раз",
     "example": "Я дважды звонил тебе сегодня, но никто не ответил.",
     "cefr": "A2",
     "frames": [
      "дважды два — четыре"
     ]
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "два",
     "pos": "NUM"
    }
   ]
  }
 ]
}
```

### 18. насколько (adverb)

```json
{
 "language": "ru",
 "headword": "насколько",
 "lexemes": [
  {
   "lemma": "насколько",
   "pos": "ADV",
   "kind": "word",
   "grammar": {
    "invariable": true
   },
   "pronunciations": [
    {
     "variety": "ru-RU",
     "ipa": "/nɐ.ˈskolʲ.kə/",
     "reading": "наско́лько"
    }
   ],
   "senses": [
    {
     "definition": "в какой мере, до какой степени",
     "example": "Я не совсем понимаю, насколько серьёзна эта проблема.",
     "cefr": "B1",
     "frames": [
      "насколько + прил./нареч."
     ]
    },
    {
     "definition": "используется, чтобы ограничить сказанное тем, что говорящий знает или помнит",
     "example": "Насколько я знаю, магазин сегодня работает до девяти.",
     "cefr": "B1",
     "frames": [
      "насколько я знаю",
      "насколько я помню",
      "насколько..., настолько..."
     ]
    }
   ]
  }
 ]
}
```

## Questions about the shape

Russian is new to this dictionary. After the candidates, answer each of these in a few lines, from what the entries show:

1. The transcription shows reduced vowels, soft consonants and devoicing (молоко /mə.lɐ.ˈko/, хлеб /ˈxlʲep/). Is that
   the right depth for a learner, and is it applied without mistakes?
2. The lemma is written with ё and the spelling with е is listed beside it. Right?
3. A noun lists its genitive singular, nominative plural and genitive plural; a verb its 1sg, 2sg and 3pl, past
   masculine and feminine and imperative. Is anything a learner needs missing, or anything listed needlessly?
4. A form used after numbers (два часа́) has Number=Count, the second locative (в лесу́) Case=Loc, the partitive
   (ча́ю) Case=Par. Are these the cases a learner's dictionary of Russian marks?
