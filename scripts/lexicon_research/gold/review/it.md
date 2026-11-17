# Example entries for Italian: review

These are entries of a learner's dictionary of Italian, written by a model. The best four or five will be
shown to the writing model as examples of the house style, so an error left in one is copied into
thousands of entries. Review each candidate strictly.

For each candidate, check:

1. **Facts.** Is anything wrong: a meaning, a gender, a plural or another form, a transcription, a frame,
   a label, a region, a level that is off by more than one step?
2. **The sense list.** Would a monolingual learner's dictionary of Italian number exactly these senses?
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

Give "spellings" whenever the lemma is written more than one way: every current spelling with the varieties that use it ("colour" en-GB, en-AU, en-IN and "color" en-US), and a spelling only older books use with "pre_reform": true. A variant chosen by the next sound is a spelling of the same lexeme too (e and ed, a and ad, lo and l'). The other spellings are never written as entries of their own, so list them all; a lemma written one way has no "spellings".

When the request has a line "Covered:", the headword is already listed as a spelling of the lexemes named there, and they are written. Do not write them again: return only lexemes of this spelling that are other words (English "tire", to make someone tired, when "tire" is covered as a spelling of the noun "tyre"), and if there are none, reject with "spelling of" and the lemma. The varieties of Italian are only these: it-IT.

# Pronunciation

Phonemic IPA for each of these varieties and no other: it-IT; between slashes, with a primary stress mark ˈ on every word including monosyllables, and a dot between syllables. When a variety says the word in more than one way, give the most common first; a reading after the first may carry a "note": less_common, older or newer (said mainly by older or younger speakers), careful or casual, regional (in part of the variety only), disputed (considered wrong by some). Give a note only when you are sure of it. Use only these symbols:
it-IT: consonants p b t d k ɡ f v s z ʃ ts dz tʃ dʒ m n ɲ l ʎ r j w; vowels a e ɛ i o ɔ u; marks ˈ ˌ . ː. Standard Italian. Geminates carry ː on the consonant (fatto is /ˈfatːo/); a geminate affricate doubles its stop instead (oggi is /ˈɔddʒi/, pizza is /ˈpittsa/), and ʃ ʎ ɲ between vowels are written long (pesce is /ˈpeʃːe/). Open and close e and o are distinguished (ɛ/e, ɔ/o) in stressed syllables and are e, o when unstressed. Intervocalic s is z where the standard has it.
Italian does not write stress inside a word, so each pronunciation also carries a reading: the word with an accent on its stressed vowel as Italian dictionaries print it, grave or acute on e and o for an open or a close vowel (àncora, ancóra, pèsca, pésca, telèfono). A word of one syllable has no mark.

# Grammar and forms

Grammar: only what the language needs, and only these keys, with these values: gender (Masc, Fem, Neut; Com for a noun that is masculine for a man and feminine for a woman without changing its form: el/la estudiante, колега), number (Sing or Plur, only for a noun used in one number), countability (Count, Mass, Both), valency (Tr, Intr, Both, Refl, Impers), irregular (true/false), conjugation (1, 2, 3 by the infinitive ending in the language's own order; German: strong, weak, mixed), auxiliary (have, be, both), separable and separable_prefix, reflexive, declension (1-4, weak, strong, mixed, adjectival), plural_class (German: -e, -¨e, -en, -n, -er, -¨er, -s, -nen, or - for no change), aspect (Imp, Perf), animacy (Anim, Inan), invariable, position (Attr, Pred), comparable, and note for anything else, in one short clause. Where one variety differs from the rest, put only the difference under "by_variety" (German "E-Mail": gender Fem, by_variety de-AT and de-CH: gender Neut).

Forms: Nouns: the plural when irregular, including a plural that changes gender (il braccio, le braccia; l'uovo, le uova). Adjectives: the forms when irregular. Verbs: nothing beyond grammar for a regular verb; for an irregular verb the present indicative 1sg, 2sg, 3sg, 1pl and 3pl, the passato remoto 1sg and 3sg, the future 1sg, the present subjunctive 1sg and the past participle, with auxiliary in grammar. The regular forms are generated later from the grammar, so list only what a learner has to be told. Features use Universal Dependencies names and values only (Number=Plur, Case=Gen, Tense=Past, Person=3, VerbForm=Part, Mood=Sub); a form that fills several cells joins the values with a comma (VerbForm=Fin,Part). A form used only in some varieties lists them ("learnt" en-GB). A form that belongs to only some senses lists their numbers, counting from 1 ("brethren" for the religious sense of "brother"). A listed form of two or more syllables carries its own reading (pàrlano).

# Senses

Senses in descending frequency of use. Only senses a reader meets in books and ordinary text; no technical, dialectal or obsolete senses unless the headword is itself rare. Two senses are separate only when a monolingual learner's dictionary of Italian would number them separately. Most words have one or two senses; give more only for a word that is truly polysemous. Do not split shades of one meaning, and do not make senses out of subject areas or facts about the thing ("potato" as a plant, a food and a crop is one sense). Include a common figurative sense ("a climate of fear"). Give only senses the Italian word has: a meaning that only its English equivalent has does not belong here.

Definition. In Italian, 6 to 15 words, plain words a learner knows, a phrase not a sentence, in the style of a learner's dictionary. The headword and words of its family must not appear in it. No example inside it, and no formula such as "the act of", "the state of being", "relating to" or "characteristic of". A verb is defined as a verb, a noun as a noun.

Example. One sentence in Italian of 6 to 14 words, natural, no proper names, that shows this sense and its frame if it has one. Vary the examples: not always the past tense, not always a third-person subject, not always starting with an article.

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
Regions. A sense used only or mainly in part of the Italian-speaking world lists where, each with "variety" and "extent". A variety is the language with a country (it-IT, it-CH). The extent is "only" when the sense is not used elsewhere and "mainly" when it is used everywhere but chiefly there ("lead", a dog's strap: en-GB only). Leave regions out for what the whole language uses, and never put a region in a definition. When a word is neutral in one variety and offensive or vulgar in another, those are separate senses, each with its regions and labels.

Antonyms. For a sense with a clear opposite, the opposite's lemma and part of speech. No synonyms.

# Relations

Relations point from this lexeme to the word it is made from or is confused with, each as type, lemma and part of speech; the reverse links are filled in later, so the base word does not list its derivatives. Types: diminutive_of (хлопчик of хлопець), augmentative_of, endearing_of (сонечко of сонце), feminine_of (actress of actor), agent_noun_of (teacher of teach), verbal_noun_of (читання of читати), abstract_noun_of (kindness of kind), adjective_of (urban of city), adverb_of (quickly of quick), derived_from (any other derivation), aspect_partner (зробити of робити), and confusable (a different word learners mix it up with, with "why" in English). Give only relations you are sure of.

Write the same way for every word, so that neighbouring entries read as one dictionary. Give nothing you are unsure of; an empty field is better than a guess.

## Candidates

### 1. collaborazione (noun)

```json
{
 "language": "it",
 "headword": "collaborazione",
 "lexemes": [
  {
   "lemma": "collaborazione",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Fem",
    "countability": "Both"
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/kolː.a.bo.ratˈtsjo.ne/",
     "reading": "collaborazióne"
    }
   ],
   "senses": [
    {
     "definition": "lavoro fatto insieme ad altre persone per raggiungere uno scopo comune",
     "example": "Grazie alla collaborazione di tutti, finiremo il progetto in tempo.",
     "cefr": "B1",
     "frames": [
      "collaborazione con qcn",
      "in collaborazione con qcn/qcs"
     ]
    },
    {
     "definition": "rapporto di lavoro non fisso con un'azienda, un giornale o un ente",
     "example": "Da anni ha una collaborazione con un quotidiano locale.",
     "cefr": "B2",
     "frames": [
      "collaborazione a/con qcs"
     ],
     "grammar": {
      "countability": "Count"
     }
    }
   ],
   "relations": [
    {
     "type": "verbal_noun_of",
     "lemma": "collaborare",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 2. denaro (noun)

```json
{
 "language": "it",
 "headword": "denaro",
 "lexemes": [
  {
   "lemma": "denaro",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Both"
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/de.ˈna.ro/",
     "reading": "denàro"
    }
   ],
   "senses": [
    {
     "definition": "monete e banconote che si usano per comprare e pagare",
     "example": "Non abbiamo abbastanza denaro per comprare una casa nuova.",
     "cefr": "A2",
     "grammar": {
      "countability": "Mass"
     }
    },
    {
     "definition": "antica moneta d'argento usata dai Romani e nel Medioevo",
     "example": "Nel museo sono esposti alcuni denari d'argento dell'epoca imperiale.",
     "cefr": "C1",
     "grammar": {
      "countability": "Count"
     },
     "labels": {
      "time": "historical"
     }
    }
   ]
  }
 ]
}
```

### 3. fenomeno (noun)

```json
{
 "language": "it",
 "headword": "fenomeno",
 "lexemes": [
  {
   "lemma": "fenomeno",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/feˈnɔ.me.no/",
     "reading": "fenòmeno"
    }
   ],
   "senses": [
    {
     "definition": "fatto o evento che si può osservare in natura o nella società",
     "example": "Il riscaldamento globale è un fenomeno che preoccupa molti scienziati.",
     "cefr": "B1"
    },
    {
     "definition": "persona con capacità straordinarie, che stupisce tutti",
     "example": "Segna sempre, in campo quel ragazzo è davvero un fenomeno!",
     "cefr": "B2",
     "labels": {
      "formality": "informal"
     }
    }
   ]
  }
 ]
}
```

### 4. museo (noun)

```json
{
 "language": "it",
 "headword": "museo",
 "lexemes": [
  {
   "lemma": "museo",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/muˈzɛ.o/",
     "reading": "musèo"
    }
   ],
   "senses": [
    {
     "definition": "edificio dove si conservano e si mostrano al pubblico opere d'arte o oggetti importanti",
     "example": "Domenica andiamo al museo a vedere una mostra di pittura.",
     "cefr": "A1"
    }
   ]
  }
 ]
}
```

### 5. comprare (verb with frames)

```json
{
 "language": "it",
 "headword": "comprare",
 "lexemes": [
  {
   "lemma": "comprare",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "1",
    "auxiliary": "have",
    "valency": "Tr",
    "irregular": false
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/komˈpra.re/",
     "reading": "compràre"
    }
   ],
   "senses": [
    {
     "definition": "ottenere una cosa pagando una somma di denaro",
     "example": "Domani compriamo il pane e la frutta al mercato.",
     "cefr": "A1",
     "frames": [
      "comprare qualcosa a qualcuno",
      "comprare qualcosa da qualcuno"
     ],
     "antonyms": [
      {
       "lemma": "vendere",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "dare denaro o favori a una persona per farla agire in modo disonesto",
     "example": "Hanno cercato di comprare il giudice con molti soldi.",
     "cefr": "B2",
     "frames": [
      "comprare qualcuno"
     ],
     "labels": {
      "figurative": true
     }
    }
   ]
  }
 ]
}
```

### 6. mantenere (verb with frames)

```json
{
 "language": "it",
 "headword": "mantenere",
 "lexemes": [
  {
   "lemma": "mantenere",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "2",
    "irregular": true,
    "auxiliary": "have",
    "valency": "Tr"
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/man.teˈne.re/",
     "reading": "mantenére"
    }
   ],
   "forms": [
    {
     "form": "mantengo",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Sing"
     },
     "ipa": {
      "it-IT": "/manˈtɛn.ɡo/"
     },
     "reading": "mantèngo"
    },
    {
     "form": "mantieni",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "2",
      "Number": "Sing"
     },
     "ipa": {
      "it-IT": "/manˈtjɛ.ni/"
     },
     "reading": "mantièni"
    },
    {
     "form": "mantiene",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "3",
      "Number": "Sing"
     },
     "ipa": {
      "it-IT": "/manˈtjɛ.ne/"
     },
     "reading": "mantiène"
    },
    {
     "form": "manteniamo",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Plur"
     },
     "ipa": {
      "it-IT": "/man.teˈnja.mo/"
     },
     "reading": "manteniàmo"
    },
    {
     "form": "mantengono",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "3",
      "Number": "Plur"
     },
     "ipa": {
      "it-IT": "/manˈtɛn.ɡo.no/"
     },
     "reading": "mantèngono"
    },
    {
     "form": "mantenni",
     "features": {
      "Mood": "Ind",
      "Tense": "Past",
      "Person": "1",
      "Number": "Sing"
     },
     "ipa": {
      "it-IT": "/manˈtenːi/"
     },
     "reading": "manténni"
    },
    {
     "form": "mantenne",
     "features": {
      "Mood": "Ind",
      "Tense": "Past",
      "Person": "3",
      "Number": "Sing"
     },
     "ipa": {
      "it-IT": "/manˈtenːe/"
     },
     "reading": "manténne"
    },
    {
     "form": "manterrò",
     "features": {
      "Mood": "Ind",
      "Tense": "Fut",
      "Person": "1",
      "Number": "Sing"
     },
     "ipa": {
      "it-IT": "/man.teˈrːɔ/"
     },
     "reading": "manterrò"
    },
    {
     "form": "mantenga",
     "features": {
      "Mood": "Sub",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Sing"
     },
     "ipa": {
      "it-IT": "/manˈtɛn.ɡa/"
     },
     "reading": "mantènga"
    },
    {
     "form": "mantenuto",
     "features": {
      "VerbForm": "Part",
      "Tense": "Past",
      "Gender": "Masc",
      "Number": "Sing"
     },
     "ipa": {
      "it-IT": "/man.teˈnu.to/"
     },
     "reading": "mantenùto"
    }
   ],
   "senses": [
    {
     "definition": "conservare qualcosa nello stesso stato o nella stessa condizione nel tempo",
     "example": "Cerca di mantenere la calma anche nei momenti difficili.",
     "cefr": "B1"
    },
    {
     "definition": "dare a qualcuno il denaro e le cose necessarie per vivere",
     "example": "Con un solo stipendio mantiene moglie e tre figli.",
     "cefr": "B1",
     "frames": [
      "mantenere qualcuno",
      "mantenersi con qualcosa"
     ]
    },
    {
     "definition": "fare davvero ciò che si è promesso, rispettare un impegno",
     "example": "Non preoccuparti, manterrò la promessa che ti ho fatto.",
     "cefr": "B1",
     "frames": [
      "mantenere la parola",
      "mantenere una promessa"
     ]
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "tenere",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 7. iniziare (verb with frames)

```json
{
 "language": "it",
 "headword": "iniziare",
 "lexemes": [
  {
   "lemma": "iniziare",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "1",
    "irregular": false,
    "valency": "Both",
    "auxiliary": "both",
    "note": "avere when transitive, essere when intransitive"
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/i.nitˈtsja.re/",
     "reading": "iniziàre"
    }
   ],
   "senses": [
    {
     "definition": "cominciare a fare qualcosa, dare avvio a un'attività",
     "example": "Domani inizio a lavorare in un nuovo ufficio.",
     "cefr": "A1",
     "frames": [
      "iniziare qcs",
      "iniziare a + inf"
     ],
     "grammar": {
      "valency": "Tr",
      "auxiliary": "have"
     },
     "antonyms": [
      {
       "lemma": "finire",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "avere principio, cominciare in un certo momento o luogo",
     "example": "A che ora inizia la lezione di inglese?",
     "cefr": "A2",
     "grammar": {
      "valency": "Intr",
      "auxiliary": "be"
     },
     "antonyms": [
      {
       "lemma": "finire",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "introdurre qualcuno alla conoscenza o alla pratica di un'arte, di un culto",
     "example": "Fu il nonno a iniziarlo alla pesca da bambino.",
     "cefr": "C1",
     "frames": [
      "iniziare qcn a qcs"
     ],
     "grammar": {
      "valency": "Tr",
      "auxiliary": "have"
     }
    }
   ]
  }
 ]
}
```

### 8. continuare (verb with frames)

```json
{
 "language": "it",
 "headword": "continuare",
 "lexemes": [
  {
   "lemma": "continuare",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "1",
    "valency": "Both",
    "auxiliary": "both",
    "irregular": false,
    "note": "present stressed on -ti-: contìnuo, contìnui, contìnua, contìnuano; avere when transitive"
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/kon.ti.nuˈa.re/",
     "reading": "continuàre"
    }
   ],
   "senses": [
    {
     "definition": "andare avanti a fare qualcosa senza fermarsi o dopo una pausa",
     "example": "Se continui a correre così, domani sarai stanchissimo.",
     "cefr": "A2",
     "frames": [
      "continuare qcs",
      "continuare a + inf"
     ],
     "grammar": {
      "valency": "Both"
     },
     "antonyms": [
      {
       "lemma": "smettere",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "durare ancora nel tempo, non finire",
     "example": "La pioggia è continuata per tutta la notte.",
     "cefr": "B1",
     "grammar": {
      "valency": "Intr"
     },
     "antonyms": [
      {
       "lemma": "finire",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "estendersi ancora nello spazio, andare oltre un certo punto",
     "example": "Oltre il ponte la strada continua fino al mare.",
     "cefr": "B1",
     "grammar": {
      "valency": "Intr"
     }
    }
   ]
  }
 ]
}
```

### 9. recente (adjective)

```json
{
 "language": "it",
 "headword": "recente",
 "lexemes": [
  {
   "lemma": "recente",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": true
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/re.ˈtʃɛn.te/",
     "reading": "recènte"
    }
   ],
   "senses": [
    {
     "definition": "che è avvenuto, nato o stato fatto da poco tempo",
     "example": "Secondo uno studio recente, i giovani leggono sempre meno giornali.",
     "cefr": "B1",
     "antonyms": [
      {
       "lemma": "remoto",
       "pos": "ADJ"
      }
     ]
    }
   ]
  }
 ]
}
```

### 10. strano (adjective)

```json
{
 "language": "it",
 "headword": "strano",
 "lexemes": [
  {
   "lemma": "strano",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": true
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/ˈstra.no/",
     "reading": "stràno"
    }
   ],
   "senses": [
    {
     "definition": "diverso dal solito, che sorprende o non si capisce facilmente",
     "example": "Mi sembra strano che non abbia ancora risposto al messaggio.",
     "cefr": "A2",
     "frames": [
      "è strano che + congiuntivo",
      "sembrare strano a qualcuno"
     ],
     "antonyms": [
      {
       "lemma": "normale",
       "pos": "ADJ"
      }
     ]
    }
   ],
   "relations": [
    {
     "type": "confusable",
     "lemma": "straniero",
     "pos": "ADJ",
     "why": "looks similar and recalls English 'stranger', but straniero means foreign, strano means strange"
    }
   ]
  }
 ]
}
```

### 11. europeo (two parts of speech)

```json
{
 "language": "it",
 "headword": "europeo",
 "lexemes": [
  {
   "lemma": "europeo",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": false
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/eu.roˈpɛ.o/",
     "reading": "europèo"
    }
   ],
   "senses": [
    {
     "definition": "del continente che comprende Italia, Francia, Germania e molti altri paesi",
     "example": "Molti studenti europei passano un anno in un'università straniera.",
     "cefr": "A2"
    },
    {
     "definition": "che riguarda l'Unione politica ed economica di molti stati del continente",
     "example": "Domenica si vota per eleggere il nuovo Parlamento europeo.",
     "cefr": "B1"
    }
   ],
   "relations": [
    {
     "type": "adjective_of",
     "lemma": "Europa",
     "pos": "PROPN"
    }
   ]
  },
  {
   "lemma": "europeo",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/eu.roˈpɛ.o/",
     "reading": "europèo"
    }
   ],
   "forms": [
    {
     "form": "europea",
     "features": {
      "Gender": "Fem",
      "Number": "Sing"
     },
     "reading": "europèa"
    }
   ],
   "senses": [
    {
     "definition": "persona nata o che vive in un paese del continente di Italia e Francia",
     "example": "Per tanti europei l'estate significa vacanze al mare.",
     "cefr": "B1"
    },
    {
     "definition": "campionato sportivo tra le squadre nazionali dei paesi del continente",
     "example": "Quest'estate guarderemo gli Europei di calcio con gli amici.",
     "cefr": "B1",
     "grammar": {
      "number": "Plur"
     },
     "labels": {
      "domain": "sport"
     }
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "europeo",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```

### 12. minimo (two parts of speech)

```json
{
 "language": "it",
 "headword": "minimo",
 "lexemes": [
  {
   "lemma": "minimo",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": false,
    "note": "superlativo di piccolo"
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/ˈmi.ni.mo/",
     "reading": "mìnimo"
    }
   ],
   "senses": [
    {
     "definition": "il più piccolo o il più basso possibile o consentito",
     "example": "Non ho la minima idea di dove abbia messo le chiavi.",
     "cefr": "B1",
     "antonyms": [
      {
       "lemma": "massimo",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "molto piccolo, di poca importanza o quasi nullo",
     "example": "Tra le due versioni ci sono soltanto differenze minime.",
     "cefr": "B2"
    }
   ]
  },
  {
   "lemma": "minimo",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/ˈmi.ni.mo/",
     "reading": "mìnimo"
    }
   ],
   "senses": [
    {
     "definition": "la quantità o il grado più basso possibile o richiesto",
     "example": "Questa settimana ho dormito il minimo indispensabile.",
     "cefr": "B1",
     "frames": [
      "al minimo",
      "come minimo",
      "un minimo di + sost."
     ],
     "antonyms": [
      {
       "lemma": "massimo",
       "pos": "NOUN"
      }
     ]
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "minimo",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```

### 13. bellezza (labelled sense)

```json
{
 "language": "it",
 "headword": "bellezza",
 "lexemes": [
  {
   "lemma": "bellezza",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Fem",
    "countability": "Both"
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/bel.ˈlet.tsa/",
     "reading": "bellézza"
    }
   ],
   "senses": [
    {
     "definition": "qualità di ciò che è piacevole da guardare o che colpisce l'animo",
     "example": "Tutti restano colpiti dalla bellezza di questo paesaggio di montagna.",
     "cefr": "A2",
     "grammar": {
      "countability": "Mass"
     },
     "frames": [
      "che bellezza!"
     ],
     "antonyms": [
      {
       "lemma": "bruttezza",
       "pos": "NOUN"
      }
     ]
    },
    {
     "definition": "persona o cosa che piace molto per il suo aspetto",
     "example": "Le bellezze artistiche della città attirano turisti da tutto il mondo.",
     "cefr": "B1",
     "grammar": {
      "countability": "Count"
     }
    },
    {
     "definition": "quantità grande, detta per sottolineare quanto è alta",
     "example": "Ho aspettato in fila la bellezza di tre ore!",
     "cefr": "B2",
     "frames": [
      "la bellezza di + quantità"
     ],
     "labels": {
      "formality": "informal"
     }
    }
   ],
   "relations": [
    {
     "type": "abstract_noun_of",
     "lemma": "bello",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```

### 14. addirittura (labelled sense)

```json
{
 "language": "it",
 "headword": "addirittura",
 "lexemes": [
  {
   "lemma": "addirittura",
   "pos": "ADV",
   "kind": "word",
   "grammar": {
    "invariable": true
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/a.dːi.riˈtːu.ra/",
     "reading": "addirittùra"
    }
   ],
   "senses": [
    {
     "definition": "perfino, per indicare che qualcosa va oltre quanto ci si aspetta",
     "example": "Quel giorno faceva così freddo che è addirittura nevicato in città.",
     "cefr": "B1"
    },
    {
     "definition": "esclamazione di stupore davanti a qualcosa che sembra esagerato",
     "example": "Hai aspettato tre ore? Addirittura! Potevi chiamarmi prima.",
     "cefr": "B1",
     "labels": {
      "formality": "informal"
     }
    },
    {
     "definition": "direttamente, senza passaggi intermedi né esitazioni",
     "example": "Invece di scrivere all'ufficio, rivolgiti addirittura al direttore.",
     "cefr": "B2"
    }
   ]
  }
 ]
}
```

### 15. malattia (figurative sense)

```json
{
 "language": "it",
 "headword": "malattia",
 "lexemes": [
  {
   "lemma": "malattia",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Fem",
    "countability": "Both"
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/ma.laˈtːi.a/",
     "reading": "malattìa"
    }
   ],
   "senses": [
    {
     "definition": "alterazione della salute del corpo o della mente",
     "example": "Mio nonno ha una malattia al cuore da molti anni.",
     "cefr": "A2",
     "antonyms": [
      {
       "lemma": "salute",
       "pos": "NOUN"
      }
     ]
    },
    {
     "definition": "passione molto forte, quasi un'ossessione, per qualcosa",
     "example": "Per lui il calcio è una vera malattia.",
     "cefr": "B2",
     "labels": {
      "figurative": true,
      "formality": "informal"
     }
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "malato",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```

### 16. pressione (figurative sense)

```json
{
 "language": "it",
 "headword": "pressione",
 "lexemes": [
  {
   "lemma": "pressione",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Fem",
    "countability": "Both"
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/preˈsːjo.ne/",
     "reading": "pressióne"
    }
   ],
   "senses": [
    {
     "definition": "forza che un gas o un liquido esercita su una superficie",
     "example": "La pressione dell'aria diminuisce quando si sale in montagna.",
     "cefr": "B1",
     "grammar": {
      "countability": "Mass"
     }
    },
    {
     "definition": "forza del sangue contro le pareti delle arterie",
     "example": "Mio nonno si misura la pressione ogni mattina dopo colazione.",
     "cefr": "B1",
     "frames": [
      "pressione alta/bassa",
      "misurare la pressione"
     ],
     "grammar": {
      "countability": "Mass"
     }
    },
    {
     "definition": "spinta leggera o forte fatta con la mano o il dito su qualcosa",
     "example": "Basta una leggera pressione sul tasto per accendere la radio.",
     "cefr": "B2",
     "frames": [
      "pressione su qc"
     ],
     "grammar": {
      "countability": "Count"
     }
    },
    {
     "definition": "insistenza o influenza usata per spingere qualcuno ad agire",
     "example": "Molti genitori fanno pressione sui figli per avere voti migliori.",
     "cefr": "B2",
     "frames": [
      "fare pressione su qn",
      "subire pressioni"
     ],
     "labels": {
      "figurative": true
     }
    },
    {
     "definition": "condizione di forte tensione dovuta a impegni o responsabilità",
     "example": "In questo periodo al lavoro sono sempre sotto pressione.",
     "cefr": "B2",
     "frames": [
      "essere sotto pressione"
     ],
     "grammar": {
      "countability": "Mass"
     },
     "labels": {
      "figurative": true
     }
    }
   ]
  }
 ]
}
```

### 17. immediatamente (adverb)

```json
{
 "language": "it",
 "headword": "immediatamente",
 "lexemes": [
  {
   "lemma": "immediatamente",
   "pos": "ADV",
   "kind": "word",
   "grammar": {
    "invariable": true
   },
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/imːe.dja.taˈmen.te/",
     "reading": "immediataménte"
    }
   ],
   "senses": [
    {
     "definition": "subito, senza aspettare e senza perdere tempo",
     "example": "Se senti questo allarme, esci immediatamente dall'edificio.",
     "cefr": "B1"
    },
    {
     "definition": "direttamente, senza niente in mezzo nello spazio o nel tempo",
     "example": "La farmacia si trova immediatamente dopo la piazza, sulla sinistra.",
     "cefr": "B2",
     "frames": [
      "immediatamente dopo/prima",
      "immediatamente sopra/sotto"
     ]
    }
   ],
   "relations": [
    {
     "type": "adverb_of",
     "lemma": "immediato",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```

### 18. attualmente (adverb)

```json
{
 "language": "it",
 "headword": "attualmente",
 "lexemes": [
  {
   "lemma": "attualmente",
   "pos": "ADV",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "it-IT",
     "ipa": "/atːu.al.ˈmen.te/",
     "reading": "attualménte"
    }
   ],
   "senses": [
    {
     "definition": "nel periodo presente, in questo momento, oggi",
     "example": "Attualmente lavoro in una piccola azienda vicino a casa.",
     "cefr": "B1"
    }
   ],
   "relations": [
    {
     "type": "adverb_of",
     "lemma": "attuale",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```
