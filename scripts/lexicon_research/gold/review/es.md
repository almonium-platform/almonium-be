# Example entries for Spanish: review

These are entries of a learner's dictionary of Spanish, written by a model. The best four or five will be
shown to the writing model as examples of the house style, so an error left in one is copied into
thousands of entries. Review each candidate strictly.

For each candidate, check:

1. **Facts.** Is anything wrong: a meaning, a gender, a plural or another form, a transcription, a frame,
   a label, a region, a level that is off by more than one step?
2. **The sense list.** Would a monolingual learner's dictionary of Spanish number exactly these senses?
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

The 2010 orthography dropped some accents (solo, este, guion): sólo as an adverb, guión and fué are pre-reform spellings. Give "spellings" whenever the lemma is written more than one way: every current spelling with the varieties that use it ("colour" en-GB, en-AU, en-IN and "color" en-US), and a spelling only older books use with "pre_reform": true. A variant chosen by the next sound is a spelling of the same lexeme too (y and e, o and u). The other spellings are never written as entries of their own, so list them all; a lemma written one way has no "spellings".

When the request has a line "Covered:", the headword is already listed as a spelling of the lexemes named there, and they are written. Do not write them again: return only lexemes of this spelling that are other words (English "tire", to make someone tired, when "tire" is covered as a spelling of the noun "tyre"), and if there are none, reject with "spelling of" and the lemma. The varieties of Spanish are only these: es-ES, es-MX, es-AR, es-CO.

# Pronunciation

Phonemic IPA for each of these varieties and no other: es-ES, es-MX; between slashes, with a primary stress mark ˈ on every word including monosyllables, and a dot between syllables. When a variety says the word in more than one way, give the most common first; a reading after the first may carry a "note": less_common, older or newer (said mainly by older or younger speakers), careful or casual, regional (in part of the variety only), disputed (considered wrong by some). Give a note only when you are sure of it. Use only these symbols:
es-ES: consonants p b t d k ɡ f θ s x tʃ ʝ l ʎ m n ɲ ɾ r j w; vowels a e i o u; marks ˈ ˌ . ː. Castilian Spanish with distinción (c/z before e, i and z are θ) and ll as ʎ. Voiced stops are written b d ɡ in every position; the approximant allophones are not marked. Stress falls where the written accent rules put it.
es-MX: consonants p b t d k ɡ f s x tʃ ʝ l m n ɲ ɾ r j w; vowels a e i o u; marks ˈ ˌ . ː. Mexican Spanish with seseo (c/z before e, i and z are s) and yeísmo (ll and y are ʝ). Voiced stops are written b d ɡ in every position.

# Grammar and forms

Grammar: only what the language needs, and only these keys, with these values: gender (Masc, Fem, Neut; Com for a noun that is masculine for a man and feminine for a woman without changing its form: el/la estudiante, колега), number (Sing or Plur, only for a noun used in one number), countability (Count, Mass, Both), valency (Tr, Intr, Both, Refl, Impers), irregular (true/false), conjugation (1, 2, 3 by the infinitive ending in the language's own order; German: strong, weak, mixed), auxiliary (have, be, both), separable and separable_prefix, reflexive, declension (1-4, weak, strong, mixed, adjectival), plural_class (German: -e, -¨e, -en, -n, -er, -¨er, -s, -nen, or - for no change), aspect (Imp, Perf), animacy (Anim, Inan), invariable, position (Attr, Pred), comparable, and note for anything else, in one short clause. Where one variety differs from the rest, put only the difference under "by_variety" (German "E-Mail": gender Fem, by_variety de-AT and de-CH: gender Neut).

Forms: Nouns: the plural when irregular (régimen, regímenes; carácter, caracteres). Adjectives: the feminine and plural when irregular. Verbs: nothing beyond grammar for a regular verb; for an irregular or stem-changing verb the present indicative 1sg, 2sg, 3sg, 1pl and 3pl, the pretérito indefinido 1sg and 3sg, the future 1sg, the present subjunctive 1sg, the past participle and the gerund. The regular forms are generated later from the grammar, so list only what a learner has to be told. Features use Universal Dependencies names and values only (Number=Plur, Case=Gen, Tense=Past, Person=3, VerbForm=Part, Mood=Sub); a form that fills several cells joins the values with a comma (VerbForm=Fin,Part). A form used only in some varieties lists them ("learnt" en-GB). A form that belongs to only some senses lists their numbers, counting from 1 ("brethren" for the religious sense of "brother"). Listed forms carry no ipa: it follows from the spelling.

# Senses

Senses in descending frequency of use. Only senses a reader meets in books and ordinary text; no technical, dialectal or obsolete senses unless the headword is itself rare. Two senses are separate only when a monolingual learner's dictionary of Spanish would number them separately. Most words have one or two senses; give more only for a word that is truly polysemous. Do not split shades of one meaning, and do not make senses out of subject areas or facts about the thing ("potato" as a plant, a food and a crop is one sense). Include a common figurative sense ("a climate of fear"). Give only senses the Spanish word has: a meaning that only its English equivalent has does not belong here.

Definition. In Spanish, 6 to 15 words, plain words a learner knows, a phrase not a sentence, in the style of a learner's dictionary. The headword and words of its family must not appear in it. No example inside it, and no formula such as "the act of", "the state of being", "relating to" or "characteristic of". A verb is defined as a verb, a noun as a noun.

Example. One sentence in Spanish of 6 to 14 words, natural, no proper names, that shows this sense and its frame if it has one. Vary the examples: not always the past tense, not always a third-person subject, not always starting with an article.

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
Regions. A sense used only or mainly in part of the Spanish-speaking world lists where, each with "variety" and "extent". A variety is the language with a country (es-ES, es-MX, es-AR, es-CU) or with a UN area (es-419 Latin America, es-029 the Caribbean, es-005 South America, es-013 Central America). The extent is "only" when the sense is not used elsewhere and "mainly" when it is used everywhere but chiefly there ("lead", a dog's strap: en-GB only). Leave regions out for what the whole language uses, and never put a region in a definition. When a word is neutral in one variety and offensive or vulgar in another, those are separate senses, each with its regions and labels.

Antonyms. For a sense with a clear opposite, the opposite's lemma and part of speech. No synonyms.

# Relations

Relations point from this lexeme to the word it is made from or is confused with, each as type, lemma and part of speech; the reverse links are filled in later, so the base word does not list its derivatives. Types: diminutive_of (хлопчик of хлопець), augmentative_of, endearing_of (сонечко of сонце), feminine_of (actress of actor), agent_noun_of (teacher of teach), verbal_noun_of (читання of читати), abstract_noun_of (kindness of kind), adjective_of (urban of city), adverb_of (quickly of quick), derived_from (any other derivation), aspect_partner (зробити of робити), and confusable (a different word learners mix it up with, with "why" in English). Give only relations you are sure of.

Write the same way for every word, so that neighbouring entries read as one dictionary. Give nothing you are unsure of; an empty field is better than a guess.

## Candidates

### 1. gobernador (noun)

```json
{
 "language": "es",
 "headword": "gobernador",
 "lexemes": [
  {
   "lemma": "gobernador",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count",
    "note": "feminine: gobernadora"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/ɡo.beɾ.naˈdoɾ/"
    },
    {
     "variety": "es-MX",
     "ipa": "/ɡo.beɾ.naˈdoɾ/"
    }
   ],
   "senses": [
    {
     "definition": "persona que tiene el mando político de un estado, provincia o región",
     "example": "El nuevo gobernador prometió mejorar las carreteras del estado.",
     "cefr": "B1",
     "frames": [
      "gobernador de + lugar"
     ]
    },
    {
     "definition": "persona que dirige un banco central u otra institución pública importante",
     "example": "Mañana hablará el gobernador del banco central sobre la inflación.",
     "cefr": "C1",
     "frames": [
      "gobernador de + institución"
     ]
    }
   ],
   "relations": [
    {
     "type": "agent_noun_of",
     "lemma": "gobernar",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 2. hotel (noun)

```json
{
 "language": "es",
 "headword": "hotel",
 "lexemes": [
  {
   "lemma": "hotel",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/oˈtel/"
    },
    {
     "variety": "es-MX",
     "ipa": "/oˈtel/"
    }
   ],
   "senses": [
    {
     "definition": "edificio donde los viajeros pagan por dormir y a veces comer",
     "example": "¿Puedes recomendarme un hotel barato cerca de la estación?",
     "cefr": "A1"
    }
   ]
  }
 ]
}
```

### 3. período (noun)

```json
{
 "language": "es",
 "headword": "período",
 "lexemes": [
  {
   "lemma": "período",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "spellings": [
    {
     "text": "período"
    },
    {
     "text": "periodo"
    }
   ],
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/peˈɾi.o.do/"
    },
    {
     "variety": "es-MX",
     "ipa": "/peˈɾi.o.do/"
    }
   ],
   "senses": [
    {
     "definition": "espacio de tiempo que dura algo o que hay entre dos momentos",
     "example": "Durante ese período vivíamos en una casa cerca del mar.",
     "cefr": "B1",
     "frames": [
      "período de + sust."
     ]
    },
    {
     "definition": "sangre que la mujer expulsa cada mes por la vagina",
     "example": "Cuando tengo el período me duele bastante la tripa.",
     "cefr": "B1"
    }
   ]
  }
 ]
}
```

### 4. lunes (noun)

```json
{
 "language": "es",
 "headword": "lunes",
 "lexemes": [
  {
   "lemma": "lunes",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count",
    "note": "same form in the plural: el lunes, los lunes"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/ˈlu.nes/"
    },
    {
     "variety": "es-MX",
     "ipa": "/ˈlu.nes/"
    }
   ],
   "forms": [
    {
     "form": "lunes",
     "features": {
      "Number": "Plur"
     }
    }
   ],
   "senses": [
    {
     "definition": "día de la semana que va después del domingo y antes del martes",
     "example": "Los lunes siempre me cuesta mucho levantarme temprano.",
     "cefr": "A1",
     "frames": [
      "el lunes",
      "los lunes"
     ]
    }
   ]
  }
 ]
}
```

### 5. compartir (verb with frames)

```json
{
 "language": "es",
 "headword": "compartir",
 "lexemes": [
  {
   "lemma": "compartir",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "3",
    "irregular": false,
    "valency": "Tr"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/kom.paɾˈtiɾ/"
    },
    {
     "variety": "es-MX",
     "ipa": "/kom.paɾˈtiɾ/"
    }
   ],
   "senses": [
    {
     "definition": "usar o tener algo junto con otras personas, o darles una parte",
     "example": "Durante la carrera compartí piso con dos estudiantes de medicina.",
     "cefr": "A2",
     "frames": [
      "compartir algo con alguien"
     ]
    },
    {
     "definition": "tener la misma opinión, idea o sentimiento que otra persona",
     "example": "No comparto tu punto de vista sobre este asunto.",
     "cefr": "B1"
    },
    {
     "definition": "dar a conocer a otros información, fotos o experiencias, sobre todo en internet",
     "example": "¿Puedes compartir el enlace en el grupo de la clase?",
     "cefr": "A2",
     "frames": [
      "compartir algo con alguien"
     ]
    }
   ]
  }
 ]
}
```

### 6. participar (verb with frames)

```json
{
 "language": "es",
 "headword": "participar",
 "lexemes": [
  {
   "lemma": "participar",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "1",
    "irregular": false,
    "valency": "Both"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/paɾ.ti.θi.ˈpaɾ/"
    },
    {
     "variety": "es-MX",
     "ipa": "/paɾ.ti.si.ˈpaɾ/"
    }
   ],
   "senses": [
    {
     "definition": "intervenir con otras personas en una actividad, un juego o un acontecimiento",
     "example": "¿Quieres participar en el concurso de fotografía del colegio?",
     "cefr": "A2",
     "frames": [
      "participar en algo"
     ],
     "grammar": {
      "valency": "Intr"
     }
    },
    {
     "definition": "tener algo en común con otra persona o cosa; compartir",
     "example": "Ambos partidos participan de las mismas ideas sobre la economía.",
     "cefr": "C1",
     "frames": [
      "participar de algo"
     ],
     "grammar": {
      "valency": "Intr"
     },
     "labels": {
      "formality": "formal"
     }
    },
    {
     "definition": "comunicar o hacer saber una noticia a alguien",
     "example": "Nos complace participarles el nacimiento de nuestra primera hija.",
     "cefr": "C2",
     "frames": [
      "participar algo a alguien"
     ],
     "grammar": {
      "valency": "Tr"
     },
     "labels": {
      "formality": "formal"
     }
    }
   ]
  }
 ]
}
```

### 7. elegir (verb with frames)

```json
{
 "language": "es",
 "headword": "elegir",
 "lexemes": [
  {
   "lemma": "elegir",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "3",
    "irregular": true,
    "valency": "Tr",
    "note": "e→i stem change; g→j before a and o"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/e.leˈxiɾ/"
    },
    {
     "variety": "es-MX",
     "ipa": "/e.leˈxiɾ/"
    }
   ],
   "forms": [
    {
     "form": "elijo",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     }
    },
    {
     "form": "eliges",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "2",
      "Number": "Sing",
      "VerbForm": "Fin"
     }
    },
    {
     "form": "elige",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "3",
      "Number": "Sing",
      "VerbForm": "Fin"
     }
    },
    {
     "form": "elegimos",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Plur",
      "VerbForm": "Fin"
     }
    },
    {
     "form": "eligen",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "3",
      "Number": "Plur",
      "VerbForm": "Fin"
     }
    },
    {
     "form": "elegí",
     "features": {
      "Mood": "Ind",
      "Tense": "Past",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     }
    },
    {
     "form": "eligió",
     "features": {
      "Mood": "Ind",
      "Tense": "Past",
      "Person": "3",
      "Number": "Sing",
      "VerbForm": "Fin"
     }
    },
    {
     "form": "elegiré",
     "features": {
      "Mood": "Ind",
      "Tense": "Fut",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     }
    },
    {
     "form": "elija",
     "features": {
      "Mood": "Sub",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     }
    },
    {
     "form": "elegido",
     "features": {
      "Tense": "Past",
      "VerbForm": "Part"
     }
    },
    {
     "form": "eligiendo",
     "features": {
      "VerbForm": "Ger"
     }
    }
   ],
   "senses": [
    {
     "definition": "tomar una cosa o persona entre varias porque se prefiere",
     "example": "Puedes elegir el color que más te guste.",
     "cefr": "A2",
     "frames": [
      "elegir algo",
      "elegir entre varias cosas"
     ]
    },
    {
     "definition": "nombrar por votación a una persona para un cargo",
     "example": "Cada cuatro años los vecinos eligen a su alcalde.",
     "cefr": "B1",
     "frames": [
      "elegir a alguien",
      "elegir a alguien + cargo"
     ]
    }
   ]
  }
 ]
}
```

### 8. estudiar (verb with frames)

```json
{
 "language": "es",
 "headword": "estudiar",
 "lexemes": [
  {
   "lemma": "estudiar",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "1",
    "irregular": false,
    "valency": "Both"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/es.tuˈdjaɾ/"
    },
    {
     "variety": "es-MX",
     "ipa": "/es.tuˈdjaɾ/"
    }
   ],
   "senses": [
    {
     "definition": "esforzarse por aprender algo leyendo, practicando o memorizando",
     "example": "Esta noche tengo que estudiar para el examen de historia.",
     "cefr": "A1",
     "frames": [
      "estudiar algo",
      "estudiar para algo"
     ],
     "grammar": {
      "valency": "Both"
     }
    },
    {
     "definition": "seguir los cursos de una carrera o materia en un centro de enseñanza",
     "example": "Mi hermana estudia medicina en una universidad pública.",
     "cefr": "A1",
     "frames": [
      "estudiar algo",
      "estudiar en un lugar"
     ],
     "grammar": {
      "valency": "Both"
     }
    },
    {
     "definition": "examinar algo con atención para entenderlo o tomar una decisión",
     "example": "Vamos a estudiar tu propuesta con calma antes de responder.",
     "cefr": "B1",
     "grammar": {
      "valency": "Tr"
     }
    }
   ]
  }
 ]
}
```

### 9. enorme (adjective)

```json
{
 "language": "es",
 "headword": "enorme",
 "lexemes": [
  {
   "lemma": "enorme",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "note": "same form for masculine and feminine; plural enormes"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/eˈnoɾ.me/"
    },
    {
     "variety": "es-MX",
     "ipa": "/eˈnoɾ.me/"
    }
   ],
   "senses": [
    {
     "definition": "muy grande en tamaño, cantidad o intensidad, mucho más de lo normal",
     "example": "Vivimos al lado de un parque enorme lleno de árboles.",
     "cefr": "A2",
     "antonyms": [
      {
       "lemma": "diminuto",
       "pos": "ADJ"
      }
     ]
    }
   ]
  }
 ]
}
```

### 10. genial (adjective)

```json
{
 "language": "es",
 "headword": "genial",
 "lexemes": [
  {
   "lemma": "genial",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": true,
    "note": "Same form for both genders; also used as an adverb in speech: pasarlo genial."
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/xe.ˈnjal/"
    },
    {
     "variety": "es-MX",
     "ipa": "/xe.ˈnjal/"
    }
   ],
   "senses": [
    {
     "definition": "muy bueno, estupendo, que gusta mucho",
     "example": "Tu plan para el fin de semana me parece genial.",
     "cefr": "A2",
     "labels": {
      "formality": "informal"
     },
     "antonyms": [
      {
       "lemma": "horrible",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "que muestra un talento o una inteligencia extraordinarios",
     "example": "Fue un escritor genial que cambió la novela moderna.",
     "cefr": "B2"
    }
   ],
   "relations": [
    {
     "type": "adjective_of",
     "lemma": "genio",
     "pos": "NOUN"
    }
   ]
  }
 ]
}
```

### 11. directo (two parts of speech)

```json
{
 "language": "es",
 "headword": "directo",
 "lexemes": [
  {
   "lemma": "directo",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": true
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/diˈɾek.to/"
    },
    {
     "variety": "es-MX",
     "ipa": "/diˈɾek.to/"
    }
   ],
   "senses": [
    {
     "definition": "que va de un lugar a otro en línea recta o sin paradas",
     "example": "¿Hay algún tren directo de aquí a la capital?",
     "cefr": "A2"
    },
    {
     "definition": "que ocurre o se hace sin nada ni nadie en medio",
     "example": "Prefiero tratar con el dueño de forma directa.",
     "cefr": "B1",
     "antonyms": [
      {
       "lemma": "indirecto",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "que dice lo que piensa con claridad y sin rodeos",
     "example": "Mi jefa es muy directa cuando algo no le gusta.",
     "cefr": "B1"
    },
    {
     "definition": "en gramática, complemento que recibe la acción del verbo sin preposición",
     "example": "En esta oración, el libro es el complemento directo.",
     "cefr": "B2",
     "labels": {
      "domain": "grammar"
     },
     "grammar": {
      "position": "Attr"
     },
     "antonyms": [
      {
       "lemma": "indirecto",
       "pos": "ADJ"
      }
     ]
    }
   ]
  },
  {
   "lemma": "directo",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/diˈɾek.to/"
    },
    {
     "variety": "es-MX",
     "ipa": "/diˈɾek.to/"
    }
   ],
   "senses": [
    {
     "definition": "en boxeo, golpe que se da estirando el brazo hacia delante",
     "example": "Lo tumbó con un directo a la mandíbula.",
     "cefr": "C1",
     "labels": {
      "domain": "sport"
     }
    }
   ]
  }
 ]
}
```

### 12. responsable (two parts of speech)

```json
{
 "language": "es",
 "headword": "responsable",
 "lexemes": [
  {
   "lemma": "responsable",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": true
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/res.ponˈsa.ble/"
    },
    {
     "variety": "es-MX",
     "ipa": "/res.ponˈsa.ble/"
    }
   ],
   "senses": [
    {
     "definition": "que tiene la obligación de cuidar algo o dar cuenta de ello",
     "example": "Cada alumno es responsable de su propio material escolar.",
     "cefr": "B1",
     "frames": [
      "responsable de algo",
      "responsable de hacer algo"
     ]
    },
    {
     "definition": "que cumple sus obligaciones y actúa con cuidado y sensatez",
     "example": "Mi hermana mayor siempre ha sido muy responsable con el dinero.",
     "cefr": "B1",
     "antonyms": [
      {
       "lemma": "irresponsable",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "que es la causa de algo, generalmente malo, y debe pagar por ello",
     "example": "Creen que el conductor fue responsable del accidente.",
     "cefr": "B2",
     "frames": [
      "responsable de algo"
     ]
    }
   ]
  },
  {
   "lemma": "responsable",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Com",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/res.ponˈsa.ble/"
    },
    {
     "variety": "es-MX",
     "ipa": "/res.ponˈsa.ble/"
    }
   ],
   "senses": [
    {
     "definition": "persona que dirige un grupo, un área o una actividad",
     "example": "Hable usted con la responsable de ventas, por favor.",
     "cefr": "B1",
     "frames": [
      "el/la responsable de algo"
     ]
    },
    {
     "definition": "persona que ha causado un daño o cometido un delito",
     "example": "Los responsables del robo fueron detenidos ayer por la tarde.",
     "cefr": "B2",
     "frames": [
      "el/la responsable de algo"
     ]
    }
   ]
  }
 ]
}
```

### 13. concepto (labelled sense)

```json
{
 "language": "es",
 "headword": "concepto",
 "lexemes": [
  {
   "lemma": "concepto",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/kon.ˈθep.to/"
    },
    {
     "variety": "es-MX",
     "ipa": "/kon.ˈsep.to/"
    }
   ],
   "senses": [
    {
     "definition": "idea o representación mental que alguien se forma de algo",
     "example": "Es difícil explicar el concepto de libertad a un niño.",
     "cefr": "B1",
     "frames": [
      "el concepto de algo"
     ]
    },
    {
     "definition": "opinión o juicio que se tiene sobre una persona o cosa",
     "example": "Tengo muy buen concepto de tu hermano desde siempre.",
     "cefr": "B2",
     "frames": [
      "tener buen/mal concepto de alguien"
     ]
    },
    {
     "definition": "motivo o partida por la que se paga o se cobra dinero",
     "example": "Cada mes pagamos cincuenta euros en concepto de mantenimiento.",
     "cefr": "C1",
     "frames": [
      "en concepto de algo"
     ],
     "labels": {
      "style": [
       "official"
      ]
     }
    }
   ]
  }
 ]
}
```

### 14. matar (labelled sense)

```json
{
 "language": "es",
 "headword": "matar",
 "lexemes": [
  {
   "lemma": "matar",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "1",
    "irregular": false,
    "valency": "Tr",
    "note": "literary passive may use the participle muerto (fue muerto)"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/ma.ˈtaɾ/"
    },
    {
     "variety": "es-MX",
     "ipa": "/ma.ˈtaɾ/"
    }
   ],
   "senses": [
    {
     "definition": "quitar la vida a una persona o a un animal",
     "example": "Los cazadores no deben disparar a los ciervos más jóvenes ni matarlos.",
     "cefr": "A2",
     "frames": [
      "matar a alguien"
     ],
     "grammar": {
      "valency": "Tr"
     }
    },
    {
     "definition": "perder la vida en un accidente, o quitarse uno mismo la vida",
     "example": "Casi te matas al bajar la escalera tan rápido.",
     "cefr": "B1",
     "grammar": {
      "valency": "Refl",
      "reflexive": true
     }
    },
    {
     "definition": "causar a alguien mucho dolor, cansancio o molestia",
     "example": "Este calor de agosto me mata todas las tardes.",
     "cefr": "B1",
     "labels": {
      "formality": "informal",
      "figurative": true
     },
     "grammar": {
      "valency": "Tr"
     }
    },
    {
     "definition": "esforzarse o trabajar muchísimo para conseguir algo",
     "example": "Me mato a estudiar y siempre suspendo química.",
     "cefr": "B2",
     "frames": [
      "matarse a + inf.",
      "matarse por algo"
     ],
     "labels": {
      "formality": "informal",
      "figurative": true
     },
     "grammar": {
      "valency": "Refl",
      "reflexive": true
     }
    }
   ]
  }
 ]
}
```

### 15. café (regional sense)

```json
{
 "language": "es",
 "headword": "café",
 "lexemes": [
  {
   "lemma": "café",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Both"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/ka.ˈfe/"
    },
    {
     "variety": "es-MX",
     "ipa": "/ka.ˈfe/"
    }
   ],
   "senses": [
    {
     "definition": "bebida oscura y caliente hecha con granos tostados y molidos",
     "example": "¿Quieres tomar un café conmigo esta tarde?",
     "cefr": "A1",
     "frames": [
      "café con leche",
      "café solo"
     ]
    },
    {
     "definition": "semillas de un arbusto tropical que se tuestan y se muelen",
     "example": "Este país exporta mucho café a Europa cada año.",
     "cefr": "A2",
     "grammar": {
      "countability": "Mass"
     }
    },
    {
     "definition": "local donde se sirven bebidas y comidas ligeras",
     "example": "Nos vemos en el café de la esquina a las cinco.",
     "cefr": "A2",
     "grammar": {
      "countability": "Count"
     }
    }
   ]
  },
  {
   "lemma": "café",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "note": "plural café or cafés"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/ka.ˈfe/"
    },
    {
     "variety": "es-MX",
     "ipa": "/ka.ˈfe/"
    }
   ],
   "senses": [
    {
     "definition": "de color oscuro parecido al de la tierra o la madera",
     "example": "Mi hermana tiene los ojos café y el pelo negro.",
     "cefr": "A2",
     "regions": [
      {
       "variety": "es-MX",
       "extent": "only"
      },
      {
       "variety": "es-CO",
       "extent": "only"
      }
     ]
    }
   ]
  }
 ]
}
```

### 16. recién (regional sense)

```json
{
 "language": "es",
 "headword": "recién",
 "lexemes": [
  {
   "lemma": "recién",
   "pos": "ADV",
   "kind": "word",
   "grammar": {
    "invariable": true
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/reˈθjen/"
    },
    {
     "variety": "es-MX",
     "ipa": "/reˈsjen/"
    }
   ],
   "senses": [
    {
     "definition": "hace muy poco tiempo, usado delante de un participio",
     "example": "Cuidado con la pared, que está recién pintada.",
     "cefr": "B1",
     "frames": [
      "recién + participio"
     ]
    },
    {
     "definition": "hace un momento, justo antes de ahora",
     "example": "Recién llegué a casa y ya me estás llamando.",
     "cefr": "B1",
     "regions": [
      {
       "variety": "es-MX",
       "extent": "only"
      },
      {
       "variety": "es-AR",
       "extent": "only"
      },
      {
       "variety": "es-CO",
       "extent": "only"
      }
     ]
    },
    {
     "definition": "solamente a partir de un momento determinado, no antes",
     "example": "Recién mañana vamos a saber los resultados del examen.",
     "cefr": "B2",
     "regions": [
      {
       "variety": "es-AR",
       "extent": "only"
      }
     ]
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "reciente",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```

### 17. ojo (figurative sense)

```json
{
 "language": "es",
 "headword": "ojo",
 "lexemes": [
  {
   "lemma": "ojo",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/ˈo.xo/"
    },
    {
     "variety": "es-MX",
     "ipa": "/ˈo.xo/"
    }
   ],
   "senses": [
    {
     "definition": "órgano de la cara con el que las personas y los animales ven",
     "example": "Mi hermana tiene los ojos verdes, como nuestra abuela.",
     "cefr": "A1"
    },
    {
     "definition": "capacidad especial para notar, juzgar o elegir bien algo",
     "example": "Tienes muy buen ojo para elegir regalos.",
     "cefr": "B2",
     "frames": [
      "tener (buen) ojo para algo"
     ],
     "labels": {
      "figurative": true
     }
    },
    {
     "definition": "agujero pequeño de una aguja por donde pasa el hilo",
     "example": "No consigo pasar el hilo por el ojo de la aguja.",
     "cefr": "B2"
    }
   ]
  },
  {
   "lemma": "ojo",
   "pos": "INTJ",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/ˈo.xo/"
    },
    {
     "variety": "es-MX",
     "ipa": "/ˈo.xo/"
    }
   ],
   "senses": [
    {
     "definition": "expresión para avisar de un peligro o pedir atención",
     "example": "¡Ojo, que el suelo está mojado y resbala!",
     "cefr": "A2",
     "frames": [
      "¡ojo con algo!"
     ]
    }
   ]
  }
 ]
}
```

### 18. debajo (figurative sense)

```json
{
 "language": "es",
 "headword": "debajo",
 "lexemes": [
  {
   "lemma": "debajo",
   "pos": "ADV",
   "kind": "word",
   "grammar": {
    "invariable": true
   },
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/de.ˈba.xo/"
    },
    {
     "variety": "es-MX",
     "ipa": "/de.ˈba.xo/"
    }
   ],
   "senses": [
    {
     "definition": "en un lugar inferior a otra cosa, a menudo cubierto por ella",
     "example": "El gato duerme debajo de la cama todas las tardes.",
     "cefr": "A1",
     "frames": [
      "debajo de algo"
     ],
     "antonyms": [
      {
       "lemma": "encima",
       "pos": "ADV"
      }
     ]
    },
    {
     "definition": "en un nivel, cantidad o categoría menor que otro",
     "example": "Esta noche las temperaturas estarán por debajo de cero.",
     "cefr": "B1",
     "frames": [
      "por debajo de algo"
     ],
     "labels": {
      "figurative": true
     },
     "antonyms": [
      {
       "lemma": "encima",
       "pos": "ADV"
      }
     ]
    }
   ],
   "relations": [
    {
     "type": "confusable",
     "lemma": "abajo",
     "pos": "ADV",
     "why": "both mean 'below'; debajo usually takes de + noun (under something), abajo is used alone or for direction (downstairs, down)"
    }
   ]
  }
 ]
}
```

### 19. exactamente (adverb)

```json
{
 "language": "es",
 "headword": "exactamente",
 "lexemes": [
  {
   "lemma": "exactamente",
   "pos": "ADV",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/ek.ˌsak.ta.ˈmen.te/"
    },
    {
     "variety": "es-MX",
     "ipa": "/ek.ˌsak.ta.ˈmen.te/"
    }
   ],
   "senses": [
    {
     "definition": "de manera precisa, sin ninguna diferencia ni error",
     "example": "No sé exactamente cuántas personas vendrán a la fiesta.",
     "cefr": "A2",
     "antonyms": [
      {
       "lemma": "aproximadamente",
       "pos": "ADV"
      }
     ]
    },
    {
     "definition": "respuesta que muestra acuerdo total con lo que otro acaba de decir",
     "example": "—¿Entonces el problema era la batería? —Exactamente, eso era.",
     "cefr": "B1"
    }
   ],
   "relations": [
    {
     "type": "adverb_of",
     "lemma": "exacto",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```

### 20. principalmente (adverb)

```json
{
 "language": "es",
 "headword": "principalmente",
 "lexemes": [
  {
   "lemma": "principalmente",
   "pos": "ADV",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "es-ES",
     "ipa": "/pɾin.θi.ˌpal.ˈmen.te/"
    },
    {
     "variety": "es-MX",
     "ipa": "/pɾin.si.ˌpal.ˈmen.te/"
    }
   ],
   "senses": [
    {
     "definition": "sobre todo, más que por cualquier otra cosa o razón",
     "example": "Este pueblo vive principalmente del turismo y de la pesca.",
     "cefr": "B1"
    }
   ],
   "relations": [
    {
     "type": "adverb_of",
     "lemma": "principal",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```
