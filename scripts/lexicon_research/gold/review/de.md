# Example entries for German: review

These are entries of a learner's dictionary of German, written by a model. The best four or five will be
shown to the writing model as examples of the house style, so an error left in one is copied into
thousands of entries. Review each candidate strictly.

For each candidate, check:

1. **Facts.** Is anything wrong: a meaning, a gender, a plural or another form, a transcription, a frame,
   a label, a region, a level that is off by more than one step?
2. **The sense list.** Would a monolingual learner's dictionary of German number exactly these senses?
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

A lexeme is one word with one paradigm. Split the headword into separate lexemes when the pronunciation differs (English "lead" /liːd/ to guide and "lead" /lɛd/ the metal), when the part of speech differs ("run" the verb and "run" the noun), when a noun's gender differs (German "der See" lake and "die See" sea), or when the inflection differs (English "hang", past "hung" for a picture and "hanged" for a person). Meanings with the same pronunciation, part of speech, gender and inflection are senses of one lexeme (German "Schloss" castle and lock). Capital letters do not separate entries: a lexeme whose lemma differs from the headword only in capitalisation belongs here too (German "morgen" tomorrow and "Morgen" morning).

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

German spelling was reformed in 1996: daß, muß and Schiffahrt are pre-reform spellings of dass, muss and Schifffahrt. Swiss German writes ss for ß (Strasse, de-CH). Give "spellings" whenever the lemma is written more than one way: every current spelling with the varieties that use it ("colour" en-GB, en-AU, en-IN and "color" en-US), and a spelling only older books use with "pre_reform": true. The other spellings are never written as entries of their own, so list them all; a lemma written one way has no "spellings".

When the request has a line "Covered:", the headword is already listed as a spelling of the lexemes named there, and they are written. Do not write them again: return only lexemes of this spelling that are other words (English "tire", to make someone tired, when "tire" is covered as a spelling of the noun "tyre"), and if there are none, reject with "spelling of" and the lemma. The varieties of German are only these: de-DE, de-AT, de-CH.

# Pronunciation

Phonemic IPA for each of these varieties and no other: de-DE; between slashes, with a primary stress mark ˈ on every word including monosyllables, and a dot between syllables. When a variety says the word in more than one way, give the most common first; a reading after the first may carry a "note": less_common, older or newer (said mainly by older or younger speakers), careful or casual, regional (in part of the variety only), disputed (considered wrong by some). Give a note only when you are sure of it. Use only these symbols:
de-DE: consonants p b t d k ɡ pf ts tʃ dʒ f v s z ʃ ʒ ç x h m n ŋ l ʁ j; vowels iː ɪ yː ʏ eː ɛ ɛː øː œ aː a oː ɔ uː ʊ ə ɐ aɪ aʊ ɔʏ ãː õː ɛ̃ː œ̃ː; marks ˈ ˌ . ː. Standard German. Written r is ʁ before a vowel and ɐ after a long vowel or in -er. Ich-Laut is ç, Ach-Laut is x. Syllabic consonants are written with ə (ən, əl), not with a syllabic mark.

# Grammar and forms

Grammar: only what the language needs, and only these keys, with these values: gender (Masc, Fem, Neut; Com for a noun that is masculine for a man and feminine for a woman without changing its form: el/la estudiante, колега), number (Sing or Plur, only for a noun used in one number), countability (Count, Mass, Both), valency (Tr, Intr, Both, Refl, Impers), irregular (true/false), conjugation (1, 2, 3 by the infinitive ending in the language's own order; German: strong, weak, mixed), auxiliary (have, be, both), separable and separable_prefix, reflexive, declension (1-4, weak, strong, mixed, adjectival), plural_class (German: -e, -¨e, -en, -n, -er, -¨er, -s, -nen, or - for no change), aspect (Imp, Perf), animacy (Anim, Inan), invariable, position (Attr, Pred), comparable, and note for anything else, in one short clause. Where one variety differs from the rest, put only the difference under "by_variety" (German "E-Mail": gender Fem, by_variety de-AT and de-CH: gender Neut).

Forms: Nouns: the genitive singular and the nominative plural, with gender and plural_class in grammar. Verbs: the third person singular present when its vowel changes (fährt), the third person singular preterite and the past participle, with auxiliary in grammar; a separable verb's forms are written as a sentence shows them (fängt an, fing an, angefangen). Adjectives: the comparative and superlative when they take an umlaut or are irregular (alt, älter, am ältesten). The regular forms are generated later from the grammar, so list only what a learner has to be told. Features use Universal Dependencies names and values only (Number=Plur, Case=Gen, Tense=Past, Person=3, VerbForm=Part, Mood=Sub); a form that fills several cells joins the values with a comma (VerbForm=Fin,Part). A form used only in some varieties lists them ("learnt" en-GB). A form that belongs to only some senses lists their numbers, counting from 1 ("brethren" for the religious sense of "brother"). A listed form carries its own ipa.

# Senses

Senses in descending frequency of use. Only senses a reader meets in books and ordinary text; no technical, dialectal or obsolete senses unless the headword is itself rare. Two senses are separate only when a monolingual learner's dictionary of German would number them separately. Most words have one or two senses; give more only for a word that is truly polysemous. Do not split shades of one meaning, and do not make senses out of subject areas or facts about the thing ("potato" as a plant, a food and a crop is one sense). Include a common figurative sense ("a climate of fear"). Give only senses the German word has: a meaning that only its English equivalent has does not belong here.

Definition. In German, 6 to 15 words, plain words a learner knows, a phrase not a sentence, in the style of a learner's dictionary. The headword and words of its family must not appear in it. No example inside it, and no formula such as "the act of", "the state of being", "relating to" or "characteristic of". A verb is defined as a verb, a noun as a noun.

Example. One sentence in German of 6 to 14 words, natural, no proper names, that shows this sense and its frame if it has one. Vary the examples: not always the past tense, not always a third-person subject, not always starting with an article.

Frames. The constructions a learner needs to use the sense, printed as a learner's dictionary prints them: the word with its preposition, case or complement ("warten auf + Akk", "helfen + Dat", "depend on sth", "дякувати кому", "insist on doing sth"). At most three; leave out when the sense has none.

Level. Your estimate of the CEFR level of the sense, A1 to C2.

Labels. How the sense is used. Leave out every label that has its default:
- formality: formal | informal | slang (default neutral)
- attitude: any of derogatory, offensive, vulgar, humorous, ironic, euphemistic, endearing, disapproving
- time: dated (old-fashioned but still understood) | archaic (met only in old texts) | historical (the ordinary word for a thing of the past) (default current)
- style: any of literary, poetic, elevated, official
- normativity: nonstandard (common in speech but not standard) | proscribed (rejected by the language's normative dictionaries; give the standard word in "standard_alternative") (default standard)
- politeness: familiar (said to friends, family and children: du, tu, tú) | polite (the respectful form to a stranger or a superior: Sie, vous, usted, Lei), only for a word or sense that is one of the two
- domain: one of agriculture, anatomy, architecture, art, astronomy, aviation, biology, botany, chemistry, computing, cooking, economics, education, electricity, finance, fishing, geography, geology, grammar, history, hunting, law, linguistics, literature, mathematics, medicine, military, music, mythology, nautical, philosophy, photography, physics, politics, psychology, religion, sport, technology, theatre, transport, zoology, only for a specialist sense
- figurative: true for a figurative sense
- rare: true for a sense few speakers use
Regions. A sense used only or mainly in part of the German-speaking world lists where, each with "variety" and "extent". A variety is the language with a country (de-DE, de-AT, de-CH). The extent is "only" when the sense is not used elsewhere and "mainly" when it is used everywhere but chiefly there ("lead", a dog's strap: en-GB only). Leave regions out for what the whole language uses, and never put a region in a definition. When a word is neutral in one variety and offensive or vulgar in another, those are separate senses, each with its regions and labels.

Antonyms. For a sense with a clear opposite, the opposite's lemma and part of speech. No synonyms.

The headword may come in lower case (fallschirm): write each lemma as German writes it (Fallschirm).

# Relations

Relations point from this lexeme to the word it is made from or is confused with, each as type, lemma and part of speech; the reverse links are filled in later, so the base word does not list its derivatives. Types: diminutive_of (хлопчик of хлопець), augmentative_of, endearing_of (сонечко of сонце), feminine_of (actress of actor), agent_noun_of (teacher of teach), verbal_noun_of (читання of читати), abstract_noun_of (kindness of kind), adjective_of (urban of city), adverb_of (quickly of quick), derived_from (any other derivation), aspect_partner (зробити of робити), and confusable (a different word learners mix it up with, with "why" in English). Give only relations you are sure of.

Write the same way for every word, so that neighbouring entries read as one dictionary. Give nothing you are unsure of; an empty field is better than a guess.

## Candidates

### 1. Gespräch (noun)

```json
{
 "language": "de",
 "headword": "Gespräch",
 "lexemes": [
  {
   "lemma": "Gespräch",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Neut",
    "plural_class": "-e",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ɡə.ˈʃpʁɛːç/"
    }
   ],
   "forms": [
    {
     "form": "Gesprächs",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ɡə.ˈʃpʁɛːçs/"
     }
    },
    {
     "form": "Gespräche",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "ipa": {
      "de-DE": "/ɡə.ˈʃpʁɛː.çə/"
     }
    }
   ],
   "senses": [
    {
     "definition": "Austausch von Gedanken, bei dem zwei oder mehr Personen miteinander reden",
     "example": "Nach dem Essen hatten wir ein langes Gespräch über Politik.",
     "cefr": "A2",
     "frames": [
      "ein Gespräch mit + Dat führen",
      "ein Gespräch über + Akk"
     ]
    },
    {
     "definition": "offizielles Treffen, bei dem Vertreter über ein bestimmtes Thema verhandeln",
     "example": "Morgen werden die Gespräche zwischen beiden Regierungen fortgesetzt.",
     "cefr": "B1",
     "frames": [
      "Gespräche mit + Dat"
     ]
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "sprechen",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 2. Nachricht (noun)

```json
{
 "language": "de",
 "headword": "Nachricht",
 "lexemes": [
  {
   "lemma": "Nachricht",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Fem",
    "plural_class": "-en",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈnaːx.ʁɪçt/"
    }
   ],
   "forms": [
    {
     "form": "Nachricht",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ˈnaːx.ʁɪçt/"
     }
    },
    {
     "form": "Nachrichten",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "ipa": {
      "de-DE": "/ˈnaːx.ʁɪç.tən/"
     }
    }
   ],
   "senses": [
    {
     "definition": "kurze Mitteilung oder Information, die man jemandem schickt oder gibt",
     "example": "Hast du meine Nachricht von gestern schon gelesen?",
     "cefr": "A1",
     "frames": [
      "jmdm. eine Nachricht schicken",
      "Nachricht von + Dat",
      "Nachricht über + Akk"
     ]
    },
    {
     "definition": "Sendung im Radio oder Fernsehen über die wichtigsten aktuellen Ereignisse",
     "example": "Um acht Uhr sehen wir fast immer die Nachrichten.",
     "cefr": "A2",
     "grammar": {
      "number": "Plur"
     }
    }
   ]
  }
 ]
}
```

### 3. Richter (noun)

```json
{
 "language": "de",
 "headword": "Richter",
 "lexemes": [
  {
   "lemma": "Richter",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "plural_class": "-",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈʁɪç.tɐ/"
    }
   ],
   "forms": [
    {
     "form": "Richters",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ˈʁɪç.tɐs/"
     }
    },
    {
     "form": "Richter",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "ipa": {
      "de-DE": "/ˈʁɪç.tɐ/"
     }
    }
   ],
   "senses": [
    {
     "definition": "Person, die in einem Prozess Urteile spricht und über Schuld entscheidet",
     "example": "Morgen muss sie vor dem Richter als Zeugin aussagen.",
     "cefr": "B1"
    }
   ],
   "relations": [
    {
     "type": "agent_noun_of",
     "lemma": "richten",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 4. Hoffnung (noun)

```json
{
 "language": "de",
 "headword": "Hoffnung",
 "lexemes": [
  {
   "lemma": "Hoffnung",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Fem",
    "plural_class": "-en",
    "countability": "Both"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈhɔf.nʊŋ/"
    }
   ],
   "forms": [
    {
     "form": "Hoffnung",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ˈhɔf.nʊŋ/"
     }
    },
    {
     "form": "Hoffnungen",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "ipa": {
      "de-DE": "/ˈhɔf.nʊ.ŋən/"
     }
    }
   ],
   "senses": [
    {
     "definition": "Gefühl, dass etwas Gutes, das man sich wünscht, geschehen wird",
     "example": "Wir haben die Hoffnung auf besseres Wetter noch nicht aufgegeben.",
     "cefr": "A2",
     "frames": [
      "Hoffnung auf + Akk",
      "die Hoffnung aufgeben",
      "sich Hoffnungen machen"
     ],
     "antonyms": [
      {
       "lemma": "Verzweiflung",
       "pos": "NOUN"
      }
     ]
    },
    {
     "definition": "Person oder Sache, von der man Erfolg oder Rettung erwartet",
     "example": "Der junge Spieler gilt als größte Hoffnung des Vereins.",
     "cefr": "B2",
     "grammar": {
      "countability": "Count"
     }
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "hoffen",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 5. sterben (verb with frames)

```json
{
 "language": "de",
 "headword": "sterben",
 "lexemes": [
  {
   "lemma": "sterben",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "strong",
    "irregular": true,
    "auxiliary": "be",
    "valency": "Intr"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈʃtɛʁ.bən/"
    }
   ],
   "forms": [
    {
     "form": "stirbt",
     "features": {
      "Person": "3",
      "Number": "Sing",
      "Tense": "Pres",
      "Mood": "Ind",
      "VerbForm": "Fin"
     },
     "ipa": {
      "de-DE": "/ˈʃtɪʁpt/"
     }
    },
    {
     "form": "starb",
     "features": {
      "Person": "3",
      "Number": "Sing",
      "Tense": "Past",
      "Mood": "Ind",
      "VerbForm": "Fin"
     },
     "ipa": {
      "de-DE": "/ˈʃtaʁp/"
     }
    },
    {
     "form": "gestorben",
     "features": {
      "Tense": "Past",
      "VerbForm": "Part"
     },
     "ipa": {
      "de-DE": "/ɡə.ˈʃtɔʁ.bən/"
     }
    }
   ],
   "senses": [
    {
     "definition": "aufhören zu leben, zum Beispiel durch Krankheit, Alter oder Unfall",
     "example": "Mein Großvater ist letztes Jahr an Krebs gestorben.",
     "cefr": "A2",
     "frames": [
      "sterben an + Dat",
      "sterben für + Akk"
     ]
    },
    {
     "definition": "langsam verschwinden und am Ende nicht mehr existieren",
     "example": "Ohne junge Familien stirbt das kleine Dorf langsam.",
     "cefr": "B2",
     "labels": {
      "figurative": true
     }
    },
    {
     "definition": "ein Gefühl wie Angst, Hunger oder Langeweile sehr stark spüren",
     "example": "Wann gibt es endlich Essen? Ich sterbe vor Hunger!",
     "cefr": "B1",
     "frames": [
      "vor etwas (Dat) sterben"
     ],
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

### 6. fällen (verb with frames)

```json
{
 "language": "de",
 "headword": "fällen",
 "lexemes": [
  {
   "lemma": "fällen",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "weak",
    "auxiliary": "have",
    "valency": "Tr"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈfɛ.lən/"
    }
   ],
   "forms": [
    {
     "form": "fällte",
     "features": {
      "Tense": "Past",
      "Person": "3",
      "Number": "Sing",
      "VerbForm": "Fin",
      "Mood": "Ind"
     },
     "ipa": {
      "de-DE": "/ˈfɛl.tə/"
     }
    },
    {
     "form": "gefällt",
     "features": {
      "Tense": "Past",
      "VerbForm": "Part"
     },
     "ipa": {
      "de-DE": "/ɡəˈfɛlt/"
     }
    }
   ],
   "senses": [
    {
     "definition": "einen Baum unten durchschneiden, sodass er zu Boden stürzt",
     "example": "Im Winter werden im Wald viele alte Bäume gefällt.",
     "cefr": "B2"
    },
    {
     "definition": "eine Entscheidung oder ein Urteil treffen und offiziell bekannt geben",
     "example": "Bis Freitag müssen wir endlich eine Entscheidung fällen.",
     "cefr": "B2",
     "frames": [
      "eine Entscheidung fällen",
      "ein Urteil fällen"
     ]
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "fallen",
     "pos": "VERB"
    },
    {
     "type": "confusable",
     "lemma": "gefallen",
     "pos": "VERB",
     "why": "the participle gefällt looks like gefällt 'pleases', present tense of gefallen"
    }
   ]
  }
 ]
}
```

### 7. verkaufen (verb with frames)

```json
{
 "language": "de",
 "headword": "verkaufen",
 "lexemes": [
  {
   "lemma": "verkaufen",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "weak",
    "auxiliary": "have",
    "valency": "Tr",
    "irregular": false
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/fɛɐˈkaʊ.fən/"
    }
   ],
   "forms": [
    {
     "form": "verkaufte",
     "features": {
      "Person": "3",
      "Number": "Sing",
      "Tense": "Past",
      "VerbForm": "Fin"
     },
     "ipa": {
      "de-DE": "/fɛɐˈkaʊf.tə/"
     }
    },
    {
     "form": "verkauft",
     "features": {
      "Tense": "Past",
      "VerbForm": "Part"
     },
     "ipa": {
      "de-DE": "/fɛɐˈkaʊft/"
     }
    }
   ],
   "senses": [
    {
     "definition": "eine Sache einem anderen gegen Geld geben",
     "example": "Wir verkaufen unser altes Auto an einen Nachbarn.",
     "cefr": "A1",
     "frames": [
      "jemandem etwas verkaufen",
      "etwas an jemanden verkaufen"
     ],
     "antonyms": [
      {
       "lemma": "kaufen",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "eine Idee oder Sache anderen geschickt und überzeugend darstellen",
     "example": "Die Regierung versucht, die Steuererhöhung als große Reform zu verkaufen.",
     "cefr": "B2",
     "frames": [
      "etwas als etwas verkaufen"
     ],
     "labels": {
      "figurative": true
     }
    },
    {
     "definition": "von Waren: von vielen Kunden gern genommen werden",
     "example": "Dieses Buch verkauft sich überraschend gut.",
     "cefr": "B1",
     "frames": [
      "sich gut/schlecht verkaufen"
     ],
     "grammar": {
      "valency": "Refl",
      "reflexive": true
     }
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "kaufen",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 8. regeln (verb with frames)

```json
{
 "language": "de",
 "headword": "regeln",
 "lexemes": [
  {
   "lemma": "regeln",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "weak",
    "auxiliary": "have",
    "valency": "Tr",
    "irregular": false
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈʁeː.ɡəln/"
    }
   ],
   "forms": [
    {
     "form": "regelte",
     "features": {
      "Person": "3",
      "Number": "Sing",
      "Tense": "Past",
      "VerbForm": "Fin"
     },
     "ipa": {
      "de-DE": "/ˈʁeː.ɡəl.tə/"
     }
    },
    {
     "form": "geregelt",
     "features": {
      "Tense": "Past",
      "VerbForm": "Part"
     },
     "ipa": {
      "de-DE": "/ɡə.ˈʁeː.ɡəlt/"
     }
    }
   ],
   "senses": [
    {
     "definition": "eine Angelegenheit klären und in Ordnung bringen",
     "example": "Keine Sorge, ich kläre das und regle es morgen mit dem Vermieter.",
     "cefr": "B1",
     "frames": [
      "etw. regeln",
      "etw. mit jmdm. regeln"
     ]
    },
    {
     "definition": "durch Vorschriften oder Absprachen genau festlegen",
     "example": "Ein neues Gesetz regelt den Schutz persönlicher Daten im Internet.",
     "cefr": "B2"
    },
    {
     "definition": "Temperatur, Menge oder Verkehr steuern, damit alles gleichmäßig läuft",
     "example": "An der Kreuzung regelt heute ein Polizist den Verkehr.",
     "cefr": "B2"
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "Regel",
     "pos": "NOUN"
    }
   ]
  }
 ]
}
```

### 9. interessant (adjective)

```json
{
 "language": "de",
 "headword": "interessant",
 "lexemes": [
  {
   "lemma": "interessant",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": true
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ɪn.tə.ʁɛ.ˈsant/"
    }
   ],
   "senses": [
    {
     "definition": "so, dass man gern mehr darüber wissen oder hören möchte",
     "example": "Gestern habe ich einen sehr interessanten Film über Wale gesehen.",
     "cefr": "A1",
     "antonyms": [
      {
       "lemma": "langweilig",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "für jemanden günstig, weil es Vorteile oder Gewinn bringt",
     "example": "Für junge Familien ist dieses Angebot finanziell besonders interessant.",
     "cefr": "B2",
     "frames": [
      "interessant für + Akk"
     ]
    }
   ]
  }
 ]
}
```

### 10. nötig (adjective)

```json
{
 "language": "de",
 "headword": "nötig",
 "lexemes": [
  {
   "lemma": "nötig",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": true
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈnøː.tɪç/"
    }
   ],
   "senses": [
    {
     "definition": "unbedingt gebraucht, damit etwas gelingt oder funktioniert",
     "example": "Für diese Reise ist ein gültiger Reisepass unbedingt nötig.",
     "cefr": "A2",
     "frames": [
      "etw. ist nötig für + Akk",
      "es ist nötig, etw. zu tun",
      "etw. nötig haben"
     ],
     "antonyms": [
      {
       "lemma": "unnötig",
       "pos": "ADJ"
      }
     ]
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "Not",
     "pos": "NOUN"
    }
   ]
  }
 ]
}
```

### 11. schaden (two parts of speech)

```json
{
 "language": "de",
 "headword": "schaden",
 "lexemes": [
  {
   "lemma": "schaden",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "weak",
    "auxiliary": "have",
    "valency": "Intr"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈʃaː.dən/"
    }
   ],
   "forms": [
    {
     "form": "schadete",
     "features": {
      "Tense": "Past",
      "VerbForm": "Fin",
      "Person": "3",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ˈʃaː.də.tə/"
     }
    },
    {
     "form": "geschadet",
     "features": {
      "Tense": "Past",
      "VerbForm": "Part"
     },
     "ipa": {
      "de-DE": "/ɡəˈʃaː.dət/"
     }
    }
   ],
   "senses": [
    {
     "definition": "für jemanden oder etwas schlecht sein, einen Nachteil bringen",
     "example": "Zu viel Zucker schadet auf Dauer deinen Zähnen.",
     "cefr": "B1",
     "frames": [
      "jdm./etw. schaden",
      "schaden + Dat"
     ],
     "antonyms": [
      {
       "lemma": "nützen",
       "pos": "VERB"
      }
     ]
    }
   ]
  },
  {
   "lemma": "Schaden",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "plural_class": "-¨",
    "countability": "Both"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈʃaː.dən/"
    }
   ],
   "forms": [
    {
     "form": "Schadens",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ˈʃaː.dəns/"
     }
    },
    {
     "form": "Schäden",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "ipa": {
      "de-DE": "/ˈʃɛː.dən/"
     }
    }
   ],
   "senses": [
    {
     "definition": "Veränderung, durch die eine Sache kaputt oder weniger wert ist",
     "example": "Der Sturm hat an vielen Dächern großen Schaden angerichtet.",
     "cefr": "B1",
     "frames": [
      "Schaden anrichten",
      "Schaden an + Dat"
     ]
    },
    {
     "definition": "Nachteil oder Verlust, den jemand durch etwas hat",
     "example": "Diese Entscheidung soll nicht zu Ihrem Schaden sein.",
     "cefr": "B2",
     "frames": [
      "zu jds. Schaden",
      "Schaden erleiden"
     ],
     "grammar": {
      "countability": "Mass"
     },
     "antonyms": [
      {
       "lemma": "Nutzen",
       "pos": "NOUN"
      }
     ]
    }
   ]
  }
 ]
}
```

### 12. perfekt (two parts of speech)

```json
{
 "language": "de",
 "headword": "perfekt",
 "lexemes": [
  {
   "lemma": "perfekt",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {},
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/pɛʁˈfɛkt/"
    }
   ],
   "senses": [
    {
     "definition": "ohne jeden Fehler, so gut, wie es nur möglich ist",
     "example": "Nach vielen Jahren im Ausland spricht sie perfektes Englisch.",
     "cefr": "A2",
     "antonyms": [
      {
       "lemma": "fehlerhaft",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "endgültig vereinbart und abgeschlossen, besonders bei Verträgen oder Geschäften",
     "example": "Nach langen Verhandlungen ist der Wechsel des Spielers jetzt perfekt.",
     "cefr": "B2",
     "grammar": {
      "position": "Pred"
     },
     "labels": {
      "formality": "informal"
     }
    }
   ]
  },
  {
   "lemma": "Perfekt",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Neut",
    "plural_class": "-e",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈpɛʁ.fɛkt/"
    },
    {
     "variety": "de-DE",
     "ipa": "/pɛʁˈfɛkt/"
    }
   ],
   "forms": [
    {
     "form": "Perfekts",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ˈpɛʁ.fɛkts/"
     }
    },
    {
     "form": "Perfekte",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "ipa": {
      "de-DE": "/ˈpɛʁ.fɛk.tə/"
     }
    }
   ],
   "senses": [
    {
     "definition": "Zeitform des Verbs, gebildet mit „haben“ oder „sein“ und Partizip",
     "example": "Im Gespräch benutzen die meisten Leute eher das Perfekt als das Präteritum.",
     "cefr": "A2",
     "labels": {
      "domain": "grammar"
     }
    }
   ]
  }
 ]
}
```

### 13. Tor (labelled sense)

```json
{
 "language": "de",
 "headword": "Tor",
 "lexemes": [
  {
   "lemma": "Tor",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Neut",
    "plural_class": "-e",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈtoːɐ/"
    }
   ],
   "forms": [
    {
     "form": "Tores",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ˈtoː.ʁəs/"
     }
    },
    {
     "form": "Tors",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ˈtoːɐs/"
     }
    },
    {
     "form": "Tore",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "ipa": {
      "de-DE": "/ˈtoː.ʁə/"
     }
    }
   ],
   "senses": [
    {
     "definition": "große Tür in einer Mauer, einem Zaun oder Gebäude",
     "example": "Am Abend wird das Tor zum Hof immer abgeschlossen.",
     "cefr": "A2"
    },
    {
     "definition": "Treffer, bei dem der Ball ins Ziel geht und zählt",
     "example": "In der zweiten Halbzeit schoss sie gleich zwei Tore.",
     "cefr": "A2",
     "frames": [
      "ein Tor schießen"
     ],
     "labels": {
      "domain": "sport"
     }
    },
    {
     "definition": "Gestell mit Netz, in das man beim Fußball den Ball schießen muss",
     "example": "Der Ball prallte an den Pfosten und flog dann hinein ins Tor.",
     "cefr": "A2",
     "labels": {
      "domain": "sport"
     }
    },
    {
     "definition": "Zugang oder Weg in einen neuen, größeren Bereich",
     "example": "Für viele Auswanderer war der Hafen das Tor zur Welt.",
     "cefr": "B2",
     "frames": [
      "das Tor zu + Dat"
     ],
     "labels": {
      "figurative": true
     }
    }
   ]
  },
  {
   "lemma": "Tor",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "declension": "weak",
    "plural_class": "-en",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈtoːɐ/"
    }
   ],
   "forms": [
    {
     "form": "Toren",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ˈtoː.ʁən/"
     }
    },
    {
     "form": "Toren",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "ipa": {
      "de-DE": "/ˈtoː.ʁən/"
     }
    }
   ],
   "senses": [
    {
     "definition": "dummer, naiver Mensch, der nicht klug handelt",
     "example": "Ich war ein Tor, ihm so blind zu vertrauen.",
     "cefr": "C1",
     "labels": {
      "style": [
       "literary"
      ],
      "time": "dated"
     }
    }
   ],
   "relations": [
    {
     "type": "confusable",
     "lemma": "Tor",
     "pos": "NOUN",
     "why": "same spelling as das Tor (gate, goal); der Tor means a fool"
    }
   ]
  }
 ]
}
```

### 14. irgendwo (labelled sense)

```json
{
 "language": "de",
 "headword": "irgendwo",
 "lexemes": [
  {
   "lemma": "irgendwo",
   "pos": "ADV",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈɪʁ.ɡənt.voː/"
    }
   ],
   "senses": [
    {
     "definition": "an einem Ort, den man nicht kennt oder nicht nennen will",
     "example": "Ich habe meinen Schlüssel irgendwo in der Wohnung verloren.",
     "cefr": "A2",
     "antonyms": [
      {
       "lemma": "nirgendwo",
       "pos": "ADV"
      }
     ]
    },
    {
     "definition": "in gewisser Hinsicht, auf eine schwer zu erklärende Weise",
     "example": "Irgendwo hast du ja recht mit deiner Kritik.",
     "cefr": "B2",
     "labels": {
      "formality": "informal",
      "figurative": true
     }
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "wo",
     "pos": "ADV"
    }
   ]
  }
 ]
}
```

### 15. Trainer (regional sense)

```json
{
 "language": "de",
 "headword": "Trainer",
 "lexemes": [
  {
   "lemma": "Trainer",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "plural_class": "-",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈtʁɛː.nɐ/"
    },
    {
     "variety": "de-DE",
     "ipa": "/ˈtʁeː.nɐ/"
    }
   ],
   "forms": [
    {
     "form": "Trainers",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ˈtʁɛː.nɐs/"
     }
    },
    {
     "form": "Trainer",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "ipa": {
      "de-DE": "/ˈtʁɛː.nɐ/"
     }
    }
   ],
   "senses": [
    {
     "definition": "Person, die Sportler oder eine Mannschaft anleitet und auf Wettkämpfe vorbereitet",
     "example": "Nach drei Niederlagen in Folge musste der Trainer gehen.",
     "cefr": "A2"
    },
    {
     "definition": "bequemer Anzug aus Jacke und Hose, den man beim Sport trägt",
     "example": "Am Sonntag sitze ich gern im Trainer auf dem Sofa.",
     "cefr": "B2",
     "regions": [
      {
       "variety": "de-AT",
       "extent": "only"
      },
      {
       "variety": "de-CH",
       "extent": "only"
      }
     ]
    }
   ],
   "relations": [
    {
     "type": "agent_noun_of",
     "lemma": "trainieren",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 16. Kreis (regional sense)

```json
{
 "language": "de",
 "headword": "Kreis",
 "lexemes": [
  {
   "lemma": "Kreis",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "plural_class": "-e",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈkʁaɪs/"
    }
   ],
   "forms": [
    {
     "form": "Kreises",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ˈkʁaɪ.zəs/"
     }
    },
    {
     "form": "Kreise",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "ipa": {
      "de-DE": "/ˈkʁaɪ.zə/"
     }
    }
   ],
   "senses": [
    {
     "definition": "runde, geschlossene Linie, deren Punkte alle gleich weit von der Mitte entfernt sind",
     "example": "Zum Spielen setzen sich die Kinder im Kreis auf den Boden.",
     "cefr": "A2",
     "frames": [
      "im Kreis"
     ]
    },
    {
     "definition": "Gruppe von Menschen, die sich kennen oder etwas gemeinsam haben",
     "example": "Wir haben den Geburtstag nur im kleinen Kreis gefeiert.",
     "cefr": "B1",
     "frames": [
      "im Kreis + Gen",
      "in ... Kreisen"
     ]
    },
    {
     "definition": "Gebiet der Verwaltung, zu dem mehrere Städte und Gemeinden gehören",
     "example": "Der Kreis plant eine neue Busverbindung zwischen den Dörfern.",
     "cefr": "B2",
     "regions": [
      {
       "variety": "de-DE",
       "extent": "only"
      },
      {
       "variety": "de-CH",
       "extent": "only"
      }
     ]
    }
   ]
  }
 ]
}
```

### 17. Auge (figurative sense)

```json
{
 "language": "de",
 "headword": "Auge",
 "lexemes": [
  {
   "lemma": "Auge",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Neut",
    "plural_class": "-n",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈaʊ.ɡə/"
    }
   ],
   "forms": [
    {
     "form": "Auges",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ˈaʊ.ɡəs/"
     }
    },
    {
     "form": "Augen",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "ipa": {
      "de-DE": "/ˈaʊ.ɡən/"
     }
    }
   ],
   "senses": [
    {
     "definition": "Organ im Gesicht, mit dem Menschen und Tiere sehen",
     "example": "Mach bitte die Augen zu, ich habe eine Überraschung.",
     "cefr": "A1"
    },
    {
     "definition": "Fähigkeit, etwas richtig zu sehen und zu beurteilen",
     "example": "Sie hat ein gutes Auge für schöne Farben.",
     "cefr": "B2",
     "frames": [
      "ein Auge für etw. haben"
     ],
     "labels": {
      "figurative": true
     }
    },
    {
     "definition": "Punkt auf einer Seite eines Würfels, der eine Zahl zeigt",
     "example": "Wer die meisten Augen würfelt, darf anfangen.",
     "cefr": "B2"
    }
   ]
  }
 ]
}
```

### 18. Bier (figurative sense)

```json
{
 "language": "de",
 "headword": "Bier",
 "lexemes": [
  {
   "lemma": "Bier",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Neut",
    "plural_class": "-e",
    "countability": "Both",
    "note": "after numbers often unchanged: zwei Bier; plural Biere means kinds of beer"
   },
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈbiːɐ/"
    }
   ],
   "forms": [
    {
     "form": "Bieres",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ˈbiː.ʁəs/"
     }
    },
    {
     "form": "Biers",
     "features": {
      "Case": "Gen",
      "Number": "Sing"
     },
     "ipa": {
      "de-DE": "/ˈbiːɐs/"
     }
    },
    {
     "form": "Biere",
     "features": {
      "Case": "Nom",
      "Number": "Plur"
     },
     "ipa": {
      "de-DE": "/ˈbiː.ʁə/"
     }
    }
   ],
   "senses": [
    {
     "definition": "alkoholisches Getränk aus Malz, Hopfen, Hefe und Wasser",
     "example": "In dieser Region wird seit Jahrhunderten Bier gebraut.",
     "cefr": "A1",
     "grammar": {
      "countability": "Mass"
     }
    },
    {
     "definition": "ein Glas oder eine Flasche von diesem Getränk",
     "example": "Bringen Sie uns bitte noch zwei Bier!",
     "cefr": "A1",
     "grammar": {
      "countability": "Count"
     }
    },
    {
     "definition": "Angelegenheit, um die sich jemand selbst kümmern muss",
     "example": "Wie er sein Geld ausgibt, ist nicht dein Bier.",
     "cefr": "B2",
     "frames": [
      "nicht jemandes Bier sein",
      "das ist dein Bier"
     ],
     "labels": {
      "formality": "informal",
      "figurative": true
     },
     "grammar": {
      "countability": "Mass"
     }
    }
   ]
  }
 ]
}
```

### 19. stets (adverb)

```json
{
 "language": "de",
 "headword": "stets",
 "lexemes": [
  {
   "lemma": "stets",
   "pos": "ADV",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈʃteːts/"
    }
   ],
   "senses": [
    {
     "definition": "immer, zu jeder Zeit und ohne Ausnahme",
     "example": "Unsere Nachbarin war stets freundlich und hilfsbereit zu allen.",
     "cefr": "B2",
     "antonyms": [
      {
       "lemma": "nie",
       "pos": "ADV"
      }
     ]
    }
   ]
  }
 ]
}
```

### 20. möglichst (adverb)

```json
{
 "language": "de",
 "headword": "möglichst",
 "lexemes": [
  {
   "lemma": "möglichst",
   "pos": "ADV",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "de-DE",
     "ipa": "/ˈmøːk.lɪçst/"
    }
   ],
   "senses": [
    {
     "definition": "vor Adjektiven: so sehr oder so viel, wie es nur geht",
     "example": "Bitte antworten Sie uns möglichst schnell auf diese Anfrage.",
     "cefr": "B1",
     "frames": [
      "möglichst + Adj"
     ]
    },
    {
     "definition": "wenn es sich irgendwie machen lässt; wenn es geht",
     "example": "Wir wollen möglichst noch heute Abend zu Hause sein.",
     "cefr": "B2"
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "möglich",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```
