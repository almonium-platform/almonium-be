# Example entries for French: review

These are entries of a learner's dictionary of French, written by a model. The best four or five will be
shown to the writing model as examples of the house style, so an error left in one is copied into
thousands of entries. Review each candidate strictly.

For each candidate, check:

1. **Facts.** Is anything wrong: a meaning, a gender, a plural or another form, a transcription, a frame,
   a label, a region, a level that is off by more than one step?
2. **The sense list.** Would a monolingual learner's dictionary of French number exactly these senses?
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

Since the 1990 rectifications some words have two accepted spellings (événement and évènement, nénuphar and nénufar): give both, the traditional one first. Give "spellings" whenever the lemma is written more than one way: every current spelling with the varieties that use it ("colour" en-GB, en-AU, en-IN and "color" en-US), and a spelling only older books use with "pre_reform": true. A variant chosen by the next sound is a spelling of the same lexeme too (je and j', le and l', de and d', que and qu'). The other spellings are never written as entries of their own, so list them all; a lemma written one way has no "spellings".

When the request has a line "Covered:", the headword is already listed as a spelling of the lexemes named there, and they are written. Do not write them again: return only lexemes of this spelling that are other words (English "tire", to make someone tired, when "tire" is covered as a spelling of the noun "tyre"), and if there are none, reject with "spelling of" and the lemma. The varieties of French are only these: fr-FR, fr-CA, fr-BE, fr-CH.

# Pronunciation

Phonemic IPA for each of these varieties and no other: fr-FR; between slashes, with a primary stress mark ˈ on every word including monosyllables, and a dot between syllables. When a variety says the word in more than one way, give the most common first; a reading after the first may carry a "note": less_common, older or newer (said mainly by older or younger speakers), careful or casual, regional (in part of the variety only), disputed (considered wrong by some). Give a note only when you are sure of it. Use only these symbols:
fr-FR: consonants p b t d k ɡ f v s z ʃ ʒ m n ɲ ŋ l ʁ j w ɥ; vowels i e ɛ a ɑ ɔ o u y ø œ ə ɛ̃ ɑ̃ ɔ̃ œ̃; marks ˈ ˌ . ː. Standard French of France. Stress is not phonemic; put the primary stress mark on the last syllable that is not a schwa. Final schwa is dropped unless it is pronounced in ordinary speech. Liaison consonants are not written on the headword.

# Grammar and forms

Grammar: only what the language needs, and only these keys, with these values: gender (Masc, Fem, Neut; Com for a noun that is masculine for a man and feminine for a woman without changing its form: el/la estudiante, колега), number (Sing or Plur, only for a noun used in one number), countability (Count, Mass, Both), valency (Tr, Intr, Both, Refl, Impers), irregular (true/false), conjugation (1, 2, 3 by the infinitive ending in the language's own order; German: strong, weak, mixed), auxiliary (have, be, both), separable and separable_prefix, reflexive, declension (1-4, weak, strong, mixed, adjectival), plural_class (German: -e, -¨e, -en, -n, -er, -¨er, -s, -nen, or - for no change), aspect (Imp, Perf), animacy (Anim, Inan), invariable, position (Attr, Pred), comparable, h_aspire (true for a word whose initial h blocks elision and liaison: le héros, les héros), and note for anything else, in one short clause. Where one variety differs from the rest, put only the difference under "by_variety" (German "E-Mail": gender Fem, by_variety de-AT and de-CH: gender Neut).

Forms: Nouns: the plural when irregular (œil, yeux; travail, travaux). Adjectives: the feminine singular and the plurals when irregular (beau, belle, beaux, and bel before a vowel). Verbs: nothing beyond grammar for a regular verb of the first or second group; for any other verb the present indicative 1sg, 3sg, 1pl and 3pl, the past participle, the passé simple 3sg, the future 1sg and the present subjunctive 1sg, with auxiliary in grammar. The regular forms are generated later from the grammar, so list only what a learner has to be told. Features use Universal Dependencies names and values only (Number=Plur, Case=Gen, Tense=Past, Person=3, VerbForm=Part, Mood=Sub); a form that fills several cells joins the values with a comma (VerbForm=Fin,Part). A form used only in some varieties lists them ("learnt" en-GB). A form that belongs to only some senses lists their numbers, counting from 1 ("brethren" for the religious sense of "brother"). A listed form carries its own ipa.

# Senses

Senses in descending frequency of use. Only senses a reader meets in books and ordinary text; no technical, dialectal or obsolete senses unless the headword is itself rare. Two senses are separate only when a monolingual learner's dictionary of French would number them separately. Most words have one or two senses; give more only for a word that is truly polysemous. Do not split shades of one meaning, and do not make senses out of subject areas or facts about the thing ("potato" as a plant, a food and a crop is one sense). Include a common figurative sense ("a climate of fear"). Give only senses the French word has: a meaning that only its English equivalent has does not belong here.

Definition. In French, 6 to 15 words, plain words a learner knows, a phrase not a sentence, in the style of a learner's dictionary. The headword and words of its family must not appear in it. No example inside it, and no formula such as "the act of", "the state of being", "relating to" or "characteristic of". A verb is defined as a verb, a noun as a noun.

Example. One sentence in French of 6 to 14 words, natural, no proper names, that shows this sense and its frame if it has one. Vary the examples: not always the past tense, not always a third-person subject, not always starting with an article.

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
Regions. A sense used only or mainly in part of the French-speaking world lists where, each with "variety" and "extent". A variety is the language with a country (fr-FR, fr-CA, fr-BE, fr-CH) or with a UN area (fr-002 Africa, fr-021 Northern America). The extent is "only" when the sense is not used elsewhere and "mainly" when it is used everywhere but chiefly there ("lead", a dog's strap: en-GB only). Leave regions out for what the whole language uses, and never put a region in a definition. When a word is neutral in one variety and offensive or vulgar in another, those are separate senses, each with its regions and labels.

Antonyms. For a sense with a clear opposite, the opposite's lemma and part of speech. No synonyms.

# Relations

Relations point from this lexeme to the word it is made from or is confused with, each as type, lemma and part of speech; the reverse links are filled in later, so the base word does not list its derivatives. Types: diminutive_of (хлопчик of хлопець), augmentative_of, endearing_of (сонечко of сонце), feminine_of (actress of actor), agent_noun_of (teacher of teach), verbal_noun_of (читання of читати), abstract_noun_of (kindness of kind), adjective_of (urban of city), adverb_of (quickly of quick), derived_from (any other derivation), aspect_partner (зробити of робити), and confusable (a different word learners mix it up with, with "why" in English). Give only relations you are sure of.

Write the same way for every word, so that neighbouring entries read as one dictionary. Give nothing you are unsure of; an empty field is better than a guess.

## Candidates

### 1. hiver (noun)

```json
{
 "language": "fr",
 "headword": "hiver",
 "lexemes": [
  {
   "lemma": "hiver",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count",
    "h_aspire": false
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/i.ˈvɛʁ/"
    }
   ],
   "senses": [
    {
     "definition": "saison la plus froide de l'année, entre l'automne et le printemps",
     "example": "En hiver, nous partons souvent skier à la montagne.",
     "cefr": "A1",
     "frames": [
      "en hiver",
      "l'hiver dernier"
     ],
     "antonyms": [
      {
       "lemma": "été",
       "pos": "NOUN"
      }
     ]
    },
    {
     "definition": "dernière période de la vie d'une personne, la vieillesse",
     "example": "Arrivé à l'hiver de sa vie, il écrivait enfin ses souvenirs.",
     "cefr": "C1",
     "frames": [
      "l'hiver de la vie"
     ],
     "labels": {
      "figurative": true,
      "style": [
       "literary"
      ]
     }
    }
   ]
  }
 ]
}
```

### 2. hôpital (noun)

```json
{
 "language": "fr",
 "headword": "hôpital",
 "lexemes": [
  {
   "lemma": "hôpital",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/ɔ.pi.ˈtal/"
    }
   ],
   "forms": [
    {
     "form": "hôpitaux",
     "features": {
      "Number": "Plur"
     },
     "ipa": {
      "fr-FR": "/ɔ.pi.ˈto/"
     }
    }
   ],
   "senses": [
    {
     "definition": "établissement où des médecins soignent les malades et les blessés",
     "example": "Après l'accident, on l'a transporté d'urgence à l'hôpital le plus proche.",
     "cefr": "A1",
     "frames": [
      "à l'hôpital",
      "être hospitalisé à l'hôpital"
     ]
    }
   ]
  }
 ]
}
```

### 3. importance (noun)

```json
{
 "language": "fr",
 "headword": "importance",
 "lexemes": [
  {
   "lemma": "importance",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Fem",
    "countability": "Mass"
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/ɛ̃.pɔʁ.ˈtɑ̃s/"
    }
   ],
   "senses": [
    {
     "definition": "grande valeur ou intérêt qu'on accorde à une chose ou une personne",
     "example": "Tu attaches trop d'importance à l'avis des autres.",
     "cefr": "B1",
     "frames": [
      "attacher de l'importance à qqch",
      "avoir de l'importance",
      "sans importance"
     ]
    },
    {
     "definition": "grandeur, quantité ou étendue de quelque chose",
     "example": "L'importance des dégâts a surpris les habitants du village.",
     "cefr": "B2"
    }
   ],
   "relations": [
    {
     "type": "abstract_noun_of",
     "lemma": "important",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```

### 4. avion (noun)

```json
{
 "language": "fr",
 "headword": "avion",
 "lexemes": [
  {
   "lemma": "avion",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/a.ˈvjɔ̃/"
    }
   ],
   "senses": [
    {
     "definition": "appareil volant à moteur et à ailes qui transporte des passagers ou des marchandises",
     "example": "Nous prendrons l'avion demain matin pour rendre visite à ma sœur.",
     "cefr": "A1",
     "frames": [
      "prendre l'avion",
      "voyager en avion",
      "par avion"
     ]
    }
   ]
  }
 ]
}
```

### 5. envoyer (verb with frames)

```json
{
 "language": "fr",
 "headword": "envoyer",
 "lexemes": [
  {
   "lemma": "envoyer",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "1",
    "irregular": true,
    "auxiliary": "have",
    "valency": "Tr",
    "note": "future and conditional stem enverr-; y becomes i before a silent e"
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/ɑ̃.vwa.ˈje/"
    }
   ],
   "forms": [
    {
     "form": "envoie",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "fr-FR": "/ɑ̃.ˈvwa/"
     }
    },
    {
     "form": "envoie",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "3",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "fr-FR": "/ɑ̃.ˈvwa/"
     }
    },
    {
     "form": "envoyons",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Plur",
      "VerbForm": "Fin"
     },
     "ipa": {
      "fr-FR": "/ɑ̃.vwa.ˈjɔ̃/"
     }
    },
    {
     "form": "envoient",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "3",
      "Number": "Plur",
      "VerbForm": "Fin"
     },
     "ipa": {
      "fr-FR": "/ɑ̃.ˈvwa/"
     }
    },
    {
     "form": "envoyé",
     "features": {
      "Tense": "Past",
      "VerbForm": "Part"
     },
     "ipa": {
      "fr-FR": "/ɑ̃.vwa.ˈje/"
     }
    },
    {
     "form": "envoya",
     "features": {
      "Mood": "Ind",
      "Tense": "Past",
      "Person": "3",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "fr-FR": "/ɑ̃.vwa.ˈja/"
     }
    },
    {
     "form": "enverrai",
     "features": {
      "Mood": "Ind",
      "Tense": "Fut",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "fr-FR": "/ɑ̃.vɛ.ˈʁe/"
     }
    },
    {
     "form": "envoie",
     "features": {
      "Mood": "Sub",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "fr-FR": "/ɑ̃.ˈvwa/"
     }
    }
   ],
   "senses": [
    {
     "definition": "faire partir une lettre, un message ou un objet vers quelqu'un",
     "example": "Tu peux m'envoyer les photos par courriel ce soir ?",
     "cefr": "A1",
     "frames": [
      "envoyer qqch à qqn"
     ],
     "antonyms": [
      {
       "lemma": "recevoir",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "faire aller une personne quelque part, souvent pour une mission",
     "example": "Ma mère m'a envoyé chercher du pain à la boulangerie.",
     "cefr": "B1",
     "frames": [
      "envoyer qqn + inf",
      "envoyer qqn à/en + lieu"
     ]
    },
    {
     "definition": "lancer avec force un objet dans une direction",
     "example": "Le gardien envoie le ballon loin devant lui.",
     "cefr": "B1"
    }
   ]
  }
 ]
}
```

### 6. expliquer (verb with frames)

```json
{
 "language": "fr",
 "headword": "expliquer",
 "lexemes": [
  {
   "lemma": "expliquer",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "1",
    "valency": "Tr",
    "auxiliary": "have"
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/ɛks.pli.ˈke/"
    }
   ],
   "senses": [
    {
     "definition": "faire comprendre quelque chose à quelqu'un en donnant des détails",
     "example": "Peux-tu m'expliquer comment fonctionne cette machine ?",
     "cefr": "A2",
     "frames": [
      "expliquer qqch à qqn",
      "expliquer à qqn que + ind",
      "expliquer comment / pourquoi"
     ],
     "grammar": {
      "valency": "Tr"
     }
    },
    {
     "definition": "être la cause ou la raison de quelque chose",
     "example": "Le mauvais temps explique en partie le retard du train.",
     "cefr": "B1",
     "grammar": {
      "valency": "Tr"
     }
    },
    {
     "definition": "donner les raisons de sa conduite pour se justifier",
     "example": "Laisse-moi m'expliquer avant de te mettre en colère.",
     "cefr": "B1",
     "frames": [
      "s'expliquer sur qqch",
      "s'expliquer avec qqn"
     ],
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

### 7. permettre (verb with frames)

```json
{
 "language": "fr",
 "headword": "permettre",
 "lexemes": [
  {
   "lemma": "permettre",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "3",
    "irregular": true,
    "auxiliary": "have",
    "valency": "Tr"
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/pɛʁ.ˈmɛtʁ/"
    }
   ],
   "forms": [
    {
     "form": "permets",
     "features": {
      "Person": "1",
      "Number": "Sing",
      "Tense": "Pres",
      "Mood": "Ind",
      "VerbForm": "Fin"
     },
     "ipa": {
      "fr-FR": "/pɛʁ.ˈmɛ/"
     }
    },
    {
     "form": "permet",
     "features": {
      "Person": "3",
      "Number": "Sing",
      "Tense": "Pres",
      "Mood": "Ind",
      "VerbForm": "Fin"
     },
     "ipa": {
      "fr-FR": "/pɛʁ.ˈmɛ/"
     }
    },
    {
     "form": "permettons",
     "features": {
      "Person": "1",
      "Number": "Plur",
      "Tense": "Pres",
      "Mood": "Ind",
      "VerbForm": "Fin"
     },
     "ipa": {
      "fr-FR": "/pɛʁ.mɛ.ˈtɔ̃/"
     }
    },
    {
     "form": "permettent",
     "features": {
      "Person": "3",
      "Number": "Plur",
      "Tense": "Pres",
      "Mood": "Ind",
      "VerbForm": "Fin"
     },
     "ipa": {
      "fr-FR": "/pɛʁ.ˈmɛt/"
     }
    },
    {
     "form": "permis",
     "features": {
      "VerbForm": "Part",
      "Tense": "Past"
     },
     "ipa": {
      "fr-FR": "/pɛʁ.ˈmi/"
     }
    },
    {
     "form": "permit",
     "features": {
      "Person": "3",
      "Number": "Sing",
      "Tense": "Past",
      "Mood": "Ind",
      "VerbForm": "Fin"
     },
     "ipa": {
      "fr-FR": "/pɛʁ.ˈmi/"
     }
    },
    {
     "form": "permettrai",
     "features": {
      "Person": "1",
      "Number": "Sing",
      "Tense": "Fut",
      "Mood": "Ind",
      "VerbForm": "Fin"
     },
     "ipa": {
      "fr-FR": "/pɛʁ.mɛ.ˈtʁe/"
     }
    },
    {
     "form": "permette",
     "features": {
      "Person": "1",
      "Number": "Sing",
      "Tense": "Pres",
      "Mood": "Sub",
      "VerbForm": "Fin"
     },
     "ipa": {
      "fr-FR": "/pɛʁ.ˈmɛt/"
     }
    }
   ],
   "senses": [
    {
     "definition": "rendre une chose possible, donner le moyen de faire",
     "example": "Ce nouveau train permet d'aller plus vite à la mer.",
     "cefr": "B1",
     "frames": [
      "permettre qqch",
      "permettre de + inf"
     ]
    },
    {
     "definition": "donner à quelqu'un le droit ou l'accord de faire quelque chose",
     "example": "Mes parents ne me permettent pas de sortir le soir.",
     "cefr": "A2",
     "frames": [
      "permettre qqch à qqn",
      "permettre à qqn de + inf"
     ],
     "antonyms": [
      {
       "lemma": "interdire",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "oser faire quelque chose sans en demander l'accord, s'accorder une liberté",
     "example": "Je me permets de vous écrire au sujet de votre annonce.",
     "cefr": "B1",
     "frames": [
      "se permettre qqch",
      "se permettre de + inf"
     ],
     "grammar": {
      "reflexive": true
     }
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "mettre",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 8. continuer (verb with frames)

```json
{
 "language": "fr",
 "headword": "continuer",
 "lexemes": [
  {
   "lemma": "continuer",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "1",
    "valency": "Both",
    "auxiliary": "have"
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/kɔ̃.ti.ˈnɥe/"
    }
   ],
   "senses": [
    {
     "definition": "ne pas arrêter de faire quelque chose, poursuivre ce qu'on a commencé",
     "example": "Malgré la pluie, nous avons continué à marcher jusqu'au village.",
     "cefr": "A2",
     "frames": [
      "continuer qqch",
      "continuer à + inf",
      "continuer de + inf"
     ],
     "grammar": {
      "valency": "Tr"
     },
     "antonyms": [
      {
       "lemma": "arrêter",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "ne pas s'interrompre, durer encore ou aller plus loin dans l'espace",
     "example": "Continuez tout droit, la gare est au bout de la rue.",
     "cefr": "A2",
     "grammar": {
      "valency": "Intr"
     },
     "antonyms": [
      {
       "lemma": "cesser",
       "pos": "VERB"
      }
     ]
    }
   ]
  }
 ]
}
```

### 9. normal (adjective)

```json
{
 "language": "fr",
 "headword": "normal",
 "lexemes": [
  {
   "lemma": "normal",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": true
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/nɔʁ.ˈmal/"
    }
   ],
   "forms": [
    {
     "form": "normale",
     "features": {
      "Gender": "Fem",
      "Number": "Sing"
     },
     "ipa": {
      "fr-FR": "/nɔʁ.ˈmal/"
     }
    },
    {
     "form": "normaux",
     "features": {
      "Gender": "Masc",
      "Number": "Plur"
     },
     "ipa": {
      "fr-FR": "/nɔʁ.ˈmo/"
     }
    },
    {
     "form": "normales",
     "features": {
      "Gender": "Fem",
      "Number": "Plur"
     },
     "ipa": {
      "fr-FR": "/nɔʁ.ˈmal/"
     }
    }
   ],
   "senses": [
    {
     "definition": "conforme à ce qui arrive le plus souvent, sans rien de particulier",
     "example": "Aujourd'hui, les températures restent normales pour la saison.",
     "cefr": "A2",
     "antonyms": [
      {
       "lemma": "anormal",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "qui ne surprend pas, que l'on peut facilement comprendre ou accepter",
     "example": "Il est normal que tu sois inquiet avant l'examen.",
     "cefr": "A2",
     "frames": [
      "il est normal que + subj",
      "c'est normal de + inf"
     ],
     "grammar": {
      "position": "Pred"
     }
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "norme",
     "pos": "NOUN"
    }
   ]
  }
 ]
}
```

### 10. économique (adjective)

```json
{
 "language": "fr",
 "headword": "économique",
 "lexemes": [
  {
   "lemma": "économique",
   "pos": "ADJ",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/e.kɔ.nɔ.ˈmik/"
    }
   ],
   "senses": [
    {
     "definition": "lié à la production, aux échanges et aux richesses d'un pays",
     "example": "Notre pays traverse une grave crise économique depuis deux ans.",
     "cefr": "B1"
    },
    {
     "definition": "qui coûte peu ou permet de dépenser moins d'argent",
     "example": "Prendre le train est souvent plus économique que la voiture.",
     "cefr": "B1",
     "antonyms": [
      {
       "lemma": "coûteux",
       "pos": "ADJ"
      }
     ]
    }
   ],
   "relations": [
    {
     "type": "adjective_of",
     "lemma": "économie",
     "pos": "NOUN"
    }
   ]
  }
 ]
}
```

### 11. cool (two parts of speech)

```json
{
 "language": "fr",
 "headword": "cool",
 "lexemes": [
  {
   "lemma": "cool",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": true,
    "note": "Le féminin est identique ; au pluriel on écrit « cool » ou « cools »."
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/ˈkul/"
    }
   ],
   "senses": [
    {
     "definition": "très agréable, plaisant, qui plaît beaucoup",
     "example": "Ton nouvel appartement est vraiment cool, surtout la terrasse.",
     "cefr": "A2",
     "labels": {
      "formality": "informal"
     }
    },
    {
     "definition": "calme et détendu, qui ne s'énerve pas et n'est pas sévère",
     "example": "Mes parents sont plutôt cool avec les sorties du samedi soir.",
     "cefr": "B1",
     "labels": {
      "formality": "informal"
     },
     "antonyms": [
      {
       "lemma": "strict",
       "pos": "ADJ"
      }
     ]
    }
   ]
  },
  {
   "lemma": "cool",
   "pos": "INTJ",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/ˈkul/"
    }
   ],
   "senses": [
    {
     "definition": "exprime la satisfaction, la joie ou l'accord",
     "example": "Cool ! On part en vacances dès demain matin.",
     "cefr": "A2",
     "labels": {
      "formality": "informal"
     }
    }
   ]
  }
 ]
}
```

### 12. historique (two parts of speech)

```json
{
 "language": "fr",
 "headword": "historique",
 "lexemes": [
  {
   "lemma": "historique",
   "pos": "ADJ",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/is.tɔ.ˈʁik/"
    }
   ],
   "senses": [
    {
     "definition": "qui appartient au passé réel des peuples et des sociétés",
     "example": "Ce film raconte des faits historiques survenus pendant la guerre.",
     "cefr": "A2"
    },
    {
     "definition": "si important qu'on s'en souviendra longtemps",
     "example": "C'est une victoire historique pour notre petite équipe de football.",
     "cefr": "B1"
    }
   ],
   "relations": [
    {
     "type": "adjective_of",
     "lemma": "histoire",
     "pos": "NOUN"
    }
   ]
  },
  {
   "lemma": "historique",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/is.tɔ.ˈʁik/"
    }
   ],
   "senses": [
    {
     "definition": "récit, dans l'ordre du temps, des faits passés concernant une chose",
     "example": "Le médecin demande toujours l'historique complet de vos maladies.",
     "cefr": "B2"
    },
    {
     "definition": "liste enregistrée des pages visitées ou des actions faites sur un appareil",
     "example": "Pense à effacer l'historique de ton navigateur sur cet ordinateur.",
     "cefr": "B1",
     "labels": {
      "domain": "computing"
     }
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "historique",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```

### 13. chien (labelled sense)

```json
{
 "language": "fr",
 "headword": "chien",
 "lexemes": [
  {
   "lemma": "chien",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/ˈʃjɛ̃/"
    }
   ],
   "senses": [
    {
     "definition": "animal domestique à quatre pattes qui aboie et garde la maison",
     "example": "Nous promenons notre chien deux fois par jour dans le parc.",
     "cefr": "A1"
    },
    {
     "definition": "charme particulier et séduisant d'une personne, surtout d'une femme",
     "example": "Sans être vraiment belle, elle a beaucoup de chien.",
     "cefr": "C1",
     "frames": [
      "avoir du chien"
     ],
     "grammar": {
      "countability": "Mass"
     },
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

### 14. port (labelled sense)

```json
{
 "language": "fr",
 "headword": "port",
 "lexemes": [
  {
   "lemma": "port",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/ˈpɔʁ/"
    }
   ],
   "senses": [
    {
     "definition": "endroit sur la côte où les bateaux s'abritent, chargent et déchargent",
     "example": "Chaque matin, les bateaux de pêche rentrent au port.",
     "cefr": "A2",
     "frames": [
      "au port",
      "arriver à bon port"
     ]
    },
    {
     "definition": "utilisation d'un vêtement, d'un équipement ou d'un objet gardé sur soi",
     "example": "Dans cette voiture, le port de la ceinture est obligatoire pour tous.",
     "cefr": "B2",
     "frames": [
      "le port de qqch"
     ],
     "grammar": {
      "countability": "Mass"
     }
    },
    {
     "definition": "somme payée pour envoyer une lettre ou un colis",
     "example": "Les frais de port sont offerts dès cinquante euros d'achat.",
     "cefr": "B2",
     "frames": [
      "frais de port"
     ],
     "grammar": {
      "countability": "Mass"
     }
    },
    {
     "definition": "prise d'un ordinateur où l'on branche un câble ou un appareil",
     "example": "Branchez la clé sur un port libre de votre ordinateur.",
     "cefr": "B2",
     "labels": {
      "domain": "computing"
     }
    },
    {
     "definition": "manière de tenir son corps et sa tête, allure d'une personne",
     "example": "Elle traversait la salle avec un port de reine.",
     "cefr": "C1",
     "labels": {
      "style": [
       "literary"
      ]
     },
     "grammar": {
      "countability": "Mass"
     }
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "porter",
     "pos": "VERB"
    },
    {
     "type": "confusable",
     "lemma": "porc",
     "pos": "NOUN",
     "why": "pronounced exactly the same, /pɔʁ/, but means pig or pork"
    }
   ]
  }
 ]
}
```

### 15. couple (regional sense)

```json
{
 "language": "fr",
 "headword": "couple",
 "lexemes": [
  {
   "lemma": "couple",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/ˈkupl/"
    }
   ],
   "senses": [
    {
     "definition": "deux personnes qui vivent ensemble ou sont mariées et s'aiment",
     "example": "Nos voisins forment un couple très uni depuis trente ans.",
     "cefr": "A2",
     "frames": [
      "vivre en couple",
      "un couple de + nom"
     ]
    },
    {
     "definition": "un mâle et une femelle d'une même espèce animale réunis",
     "example": "Un couple de cygnes a fait son nid près de l'étang.",
     "cefr": "B2",
     "frames": [
      "un couple de + nom"
     ]
    }
   ],
   "relations": [
    {
     "type": "confusable",
     "lemma": "paire",
     "pos": "NOUN",
     "why": "une paire is two matching things; un couple is mostly two people or animals linked together"
    }
   ]
  },
  {
   "lemma": "couple",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Fem",
    "number": "Sing"
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/ˈkupl/"
    }
   ],
   "senses": [
    {
     "definition": "petit nombre de choses ou de personnes, environ deux ou trois",
     "example": "Attends-moi ici, je reviens dans une couple de minutes.",
     "cefr": "B2",
     "frames": [
      "une couple de + nom"
     ],
     "labels": {
      "formality": "informal"
     },
     "regions": [
      {
       "variety": "fr-CA",
       "extent": "only"
      }
     ],
     "standard_alternative": "quelques"
    }
   ]
  }
 ]
}
```

### 16. ben (regional sense)

```json
{
 "language": "fr",
 "headword": "ben",
 "lexemes": [
  {
   "lemma": "ben",
   "pos": "INTJ",
   "kind": "word",
   "grammar": {
    "invariable": true
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/ˈbɛ̃/"
    }
   ],
   "senses": [
    {
     "definition": "mot de l'oral qui souligne qu'une réponse est évidente",
     "example": "Ben oui, je viens ce soir, tu le sais bien !",
     "cefr": "A2",
     "frames": [
      "ben oui",
      "ben non",
      "eh ben"
     ],
     "labels": {
      "formality": "informal"
     }
    },
    {
     "definition": "mot de l'oral qui marque une hésitation ou un étonnement",
     "example": "Ben… je ne sais pas trop quoi te répondre.",
     "cefr": "A2",
     "frames": [
      "ben quoi ?"
     ],
     "labels": {
      "formality": "informal"
     }
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "bien",
     "pos": "ADV"
    }
   ]
  },
  {
   "lemma": "ben",
   "pos": "ADV",
   "kind": "word",
   "grammar": {
    "invariable": true
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/ˈbɛ̃/"
    }
   ],
   "senses": [
    {
     "definition": "à un haut degré, beaucoup, vraiment",
     "example": "Il fait ben froid ce matin, mets ta tuque.",
     "cefr": "B2",
     "regions": [
      {
       "variety": "fr-CA",
       "extent": "only"
      }
     ],
     "labels": {
      "formality": "informal"
     },
     "standard_alternative": "bien"
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "bien",
     "pos": "ADV"
    }
   ]
  }
 ]
}
```

### 17. chanson (figurative sense)

```json
{
 "language": "fr",
 "headword": "chanson",
 "lexemes": [
  {
   "lemma": "chanson",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Fem",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/ʃɑ̃.ˈsɔ̃/"
    }
   ],
   "senses": [
    {
     "definition": "court texte en vers mis en musique, qu'on interprète avec la voix",
     "example": "Ma grand-mère connaît par cœur toutes les vieilles chansons populaires.",
     "cefr": "A1"
    },
    {
     "definition": "propos qu'on répète sans cesse et qui lassent",
     "example": "Avec lui, c'est toujours la même chanson : il n'a jamais le temps.",
     "cefr": "B2",
     "frames": [
      "c'est toujours la même chanson",
      "c'est une autre chanson"
     ],
     "labels": {
      "figurative": true,
      "formality": "informal"
     }
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "chanter",
     "pos": "VERB"
    },
    {
     "type": "confusable",
     "lemma": "chant",
     "pos": "NOUN",
     "why": "chant is singing in general or a hymn/birdsong; chanson is a song with words"
    }
   ]
  }
 ]
}
```

### 18. seigneur (figurative sense)

```json
{
 "language": "fr",
 "headword": "seigneur",
 "lexemes": [
  {
   "lemma": "seigneur",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/sɛ.ˈɲœʁ/"
    }
   ],
   "senses": [
    {
     "definition": "noble du Moyen Âge qui possédait une terre et commandait ses habitants",
     "example": "Au Moyen Âge, les paysans travaillaient les terres du seigneur.",
     "cefr": "B1",
     "labels": {
      "time": "historical"
     },
     "antonyms": [
      {
       "lemma": "vassal",
       "pos": "NOUN"
      }
     ]
    },
    {
     "definition": "nom que les chrétiens et les juifs donnent à Dieu",
     "example": "Prions le Seigneur pour tous ceux qui souffrent aujourd'hui.",
     "cefr": "B1",
     "labels": {
      "domain": "religion"
     }
    },
    {
     "definition": "homme puissant ou riche qui se conduit avec noblesse et générosité",
     "example": "Comme toujours, il a payé tout le repas en grand seigneur.",
     "cefr": "C1",
     "frames": [
      "en grand seigneur",
      "faire le grand seigneur"
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

### 19. généralement (adverb)

```json
{
 "language": "fr",
 "headword": "généralement",
 "lexemes": [
  {
   "lemma": "généralement",
   "pos": "ADV",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/ʒe.ne.ʁal.ˈmɑ̃/"
    }
   ],
   "senses": [
    {
     "definition": "dans la plupart des cas, le plus souvent, presque toujours",
     "example": "Le dimanche, je me lève généralement vers neuf heures.",
     "cefr": "A2",
     "antonyms": [
      {
       "lemma": "rarement",
       "pos": "ADV"
      }
     ]
    },
    {
     "definition": "par la plupart des gens, de façon large et commune",
     "example": "C'est une idée généralement admise par les spécialistes du domaine.",
     "cefr": "B2",
     "frames": [
      "généralement admis",
      "généralement reconnu"
     ]
    }
   ],
   "relations": [
    {
     "type": "adverb_of",
     "lemma": "général",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```

### 20. probablement (adverb)

```json
{
 "language": "fr",
 "headword": "probablement",
 "lexemes": [
  {
   "lemma": "probablement",
   "pos": "ADV",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "fr-FR",
     "ipa": "/pʁɔ.ba.blə.ˈmɑ̃/"
    }
   ],
   "senses": [
    {
     "definition": "selon toute vraisemblance, avec de fortes chances d'être vrai",
     "example": "Il pleuvra probablement demain, alors prends ton parapluie.",
     "cefr": "A2"
    }
   ],
   "relations": [
    {
     "type": "adverb_of",
     "lemma": "probable",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```
