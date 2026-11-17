# Example entries for English: review

These are entries of a learner's dictionary of English, written by a model. The best four or five will be
shown to the writing model as examples of the house style, so an error left in one is copied into
thousands of entries. Review each candidate strictly.

For each candidate, check:

1. **Facts.** Is anything wrong: a meaning, a gender, a plural or another form, a transcription, a frame,
   a label, a region, a level that is off by more than one step?
2. **The sense list.** Would a monolingual learner's dictionary of English number exactly these senses?
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

Give "spellings" whenever the lemma is written more than one way: every current spelling with the varieties that use it ("colour" en-GB, en-AU, en-IN and "color" en-US), and a spelling only older books use with "pre_reform": true. A variant chosen by the next sound is a spelling of the same lexeme too (a and an). The other spellings are never written as entries of their own, so list them all; a lemma written one way has no "spellings".

When the request has a line "Covered:", the headword is already listed as a spelling of the lexemes named there, and they are written. Do not write them again: return only lexemes of this spelling that are other words (English "tire", to make someone tired, when "tire" is covered as a spelling of the noun "tyre"), and if there are none, reject with "spelling of" and the lemma. The varieties of English are only these: en-US, en-GB, en-AU, en-IN.

# Pronunciation

Phonemic IPA for each of these varieties and no other: en-US, en-GB; between slashes, with a primary stress mark ˈ on every word including monosyllables, and a dot between syllables. When a variety says the word in more than one way, give the most common first; a reading after the first may carry a "note": less_common, older or newer (said mainly by older or younger speakers), careful or casual, regional (in part of the variety only), disputed (considered wrong by some). Give a note only when you are sure of it. Use only these symbols:
en-US: consonants p b t d k ɡ tʃ dʒ f v θ ð s z ʃ ʒ h m n ŋ l ɹ j w; vowels iː ɪ ɛ æ ɑː ɔː ʊ uː ʌ ə ɚ ɝ eɪ aɪ ɔɪ oʊ aʊ; marks ˈ ˌ . ː. Rhotic. Written r is ɹ. Unstressed er is ɚ, stressed is ɝ. The goat vowel is oʊ.
en-GB: consonants p b t d k ɡ tʃ dʒ f v θ ð s z ʃ ʒ h m n ŋ l ɹ j w; vowels iː ɪ e æ ɑː ɒ ɔː ʊ uː ʌ ɜː ə eɪ aɪ ɔɪ əʊ aʊ ɪə eə ʊə; marks ˈ ˌ . ː. Non-rhotic. Written r is ɹ only before a vowel. The goat vowel is əʊ, the nurse vowel ɜː.

# Grammar and forms

Grammar: only what the language needs, and only these keys, with these values: gender (Masc, Fem, Neut; Com for a noun that is masculine for a man and feminine for a woman without changing its form: el/la estudiante, колега), number (Sing or Plur, only for a noun used in one number), countability (Count, Mass, Both), valency (Tr, Intr, Both, Refl, Impers), irregular (true/false), conjugation (1, 2, 3 by the infinitive ending in the language's own order; German: strong, weak, mixed), auxiliary (have, be, both), separable and separable_prefix, reflexive, declension (1-4, weak, strong, mixed, adjectival), plural_class (German: -e, -¨e, -en, -n, -er, -¨er, -s, -nen, or - for no change), aspect (Imp, Perf), animacy (Anim, Inan), invariable, position (Attr, Pred), comparable, and note for anything else, in one short clause. Where one variety differs from the rest, put only the difference under "by_variety" (German "E-Mail": gender Fem, by_variety de-AT and de-CH: gender Neut).

Forms: Verbs: the past tense and the past participle when irregular. Nouns: the plural when irregular (mouse, mice; sheep, sheep). Adjectives and adverbs: the comparative and superlative when irregular (good, better, best). A form spelled differently in different varieties is listed in each spelling with its varieties (travelled en-GB, traveled en-US). The regular forms are generated later from the grammar, so list only what a learner has to be told. Features use Universal Dependencies names and values only (Number=Plur, Case=Gen, Tense=Past, Person=3, VerbForm=Part, Mood=Sub); a form that fills several cells joins the values with a comma (VerbForm=Fin,Part). A form used only in some varieties lists them ("learnt" en-GB). A form that belongs to only some senses lists their numbers, counting from 1 ("brethren" for the religious sense of "brother"). A listed form carries its own ipa for the same varieties.

# Senses

Senses in descending frequency of use. Only senses a reader meets in books and ordinary text; no technical, dialectal or obsolete senses unless the headword is itself rare. Two senses are separate only when a monolingual learner's dictionary of English would number them separately. Most words have one or two senses; give more only for a word that is truly polysemous. Do not split shades of one meaning, and do not make senses out of subject areas or facts about the thing ("potato" as a plant, a food and a crop is one sense). Include a common figurative sense ("a climate of fear"). Give only senses the English word has: a meaning that only its English equivalent has does not belong here.

Definition. In English, 6 to 15 words, plain words a learner knows, a phrase not a sentence, in the style of a learner's dictionary. The headword and words of its family must not appear in it. No example inside it, and no formula such as "the act of", "the state of being", "relating to" or "characteristic of". A verb is defined as a verb, a noun as a noun.

Example. One sentence in English of 6 to 14 words, natural, no proper names, that shows this sense and its frame if it has one. Vary the examples: not always the past tense, not always a third-person subject, not always starting with an article.

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
Regions. A sense used only or mainly in part of the English-speaking world lists where, each with "variety" and "extent". A variety is the language with a country (en-GB, en-US, en-AU, en-IE) or with a UN area (en-021 Northern America, en-029 the Caribbean, en-002 Africa, en-142 Asia, en-009 Oceania). The extent is "only" when the sense is not used elsewhere and "mainly" when it is used everywhere but chiefly there ("lead", a dog's strap: en-GB only). Leave regions out for what the whole language uses, and never put a region in a definition. When a word is neutral in one variety and offensive or vulgar in another, those are separate senses, each with its regions and labels.

Antonyms. For a sense with a clear opposite, the opposite's lemma and part of speech. No synonyms.

# Relations

Relations point from this lexeme to the word it is made from or is confused with, each as type, lemma and part of speech; the reverse links are filled in later, so the base word does not list its derivatives. Types: diminutive_of (хлопчик of хлопець), augmentative_of, endearing_of (сонечко of сонце), feminine_of (actress of actor), agent_noun_of (teacher of teach), verbal_noun_of (читання of читати), abstract_noun_of (kindness of kind), adjective_of (urban of city), adverb_of (quickly of quick), derived_from (any other derivation), aspect_partner (зробити of робити), and confusable (a different word learners mix it up with, with "why" in English). Give only relations you are sure of.

Write the same way for every word, so that neighbouring entries read as one dictionary. Give nothing you are unsure of; an empty field is better than a guess.

## Candidates

### 1. hill (noun)

```json
{
 "language": "en",
 "headword": "hill",
 "lexemes": [
  {
   "lemma": "hill",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈhɪl/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈhɪl/"
    }
   ],
   "senses": [
    {
     "definition": "an area of land that is higher than the land around it",
     "example": "We climbed to the top of the hill to watch the sunset.",
     "cefr": "A2",
     "frames": [
      "on a hill",
      "up/down a hill"
     ],
     "antonyms": [
      {
       "lemma": "valley",
       "pos": "NOUN"
      }
     ]
    },
    {
     "definition": "a part of a road or path that slopes up or down",
     "example": "Be careful, there's a steep hill just after the bridge.",
     "cefr": "B1"
    }
   ]
  }
 ]
}
```

### 2. lake (noun)

```json
{
 "language": "en",
 "headword": "lake",
 "lexemes": [
  {
   "lemma": "lake",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈleɪk/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈleɪk/"
    }
   ],
   "senses": [
    {
     "definition": "a large area of water with land all around it",
     "example": "We went swimming in the lake every summer morning.",
     "cefr": "A1",
     "frames": [
      "on the lake",
      "in the lake"
     ]
    }
   ]
  }
 ]
}
```

### 3. owner (noun)

```json
{
 "language": "en",
 "headword": "owner",
 "lexemes": [
  {
   "lemma": "owner",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈoʊ.nɚ/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈəʊ.nə/"
    }
   ],
   "senses": [
    {
     "definition": "a person who has something that legally belongs to them",
     "example": "Are you the owner of this car?",
     "cefr": "A2",
     "frames": [
      "the owner of sth"
     ]
    }
   ],
   "relations": [
    {
     "type": "agent_noun_of",
     "lemma": "own",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 4. airport (noun)

```json
{
 "language": "en",
 "headword": "airport",
 "lexemes": [
  {
   "lemma": "airport",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈɛɹ.pɔːɹt/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈeə.pɔːt/"
    }
   ],
   "senses": [
    {
     "definition": "a place where planes take off and land, with buildings for passengers",
     "example": "We need to be at the airport two hours before our flight.",
     "cefr": "A1",
     "frames": [
      "at the airport"
     ]
    }
   ]
  }
 ]
}
```

### 5. consider (verb with frames)

```json
{
 "language": "en",
 "headword": "consider",
 "lexemes": [
  {
   "lemma": "consider",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "valency": "Tr",
    "irregular": false
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/kən.ˈsɪd.ɚ/"
    },
    {
     "variety": "en-GB",
     "ipa": "/kən.ˈsɪd.ə/"
    }
   ],
   "senses": [
    {
     "definition": "to think carefully about something, especially before making a decision",
     "example": "We're considering moving to a smaller flat next year.",
     "cefr": "B1",
     "frames": [
      "consider sth",
      "consider doing sth"
     ]
    },
    {
     "definition": "to have a particular opinion about someone or something",
     "example": "Many people consider him the best player of his generation.",
     "cefr": "B1",
     "frames": [
      "consider sb/sth (to be) sth",
      "consider that ..."
     ]
    },
    {
     "definition": "to think about a particular fact when judging or planning something",
     "example": "You also have to consider the cost of getting there.",
     "cefr": "B2",
     "frames": [
      "consider sth",
      "consider how/what ..."
     ]
    }
   ]
  }
 ]
}
```

### 6. allow (verb with frames)

```json
{
 "language": "en",
 "headword": "allow",
 "lexemes": [
  {
   "lemma": "allow",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "valency": "Tr",
    "irregular": false
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ə.ˈlaʊ/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ə.ˈlaʊ/"
    }
   ],
   "senses": [
    {
     "definition": "to let someone do something or let something happen",
     "example": "Are dogs allowed in the restaurant?",
     "cefr": "A2",
     "frames": [
      "allow sb to do sth",
      "allow sth",
      "be allowed to do sth"
     ],
     "antonyms": [
      {
       "lemma": "forbid",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "to make it possible for someone to do something",
     "example": "The new bridge allows people to cross the river safely.",
     "cefr": "B1",
     "frames": [
      "allow sb to do sth",
      "allow sth"
     ]
    },
    {
     "definition": "to plan to use a certain amount of time or money for something",
     "example": "You should allow about two hours for the journey.",
     "cefr": "B2",
     "frames": [
      "allow sth for sth"
     ]
    }
   ]
  }
 ]
}
```

### 7. afford (verb with frames)

```json
{
 "language": "en",
 "headword": "afford",
 "lexemes": [
  {
   "lemma": "afford",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "valency": "Tr",
    "irregular": false
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/əˈfɔːɹd/"
    },
    {
     "variety": "en-GB",
     "ipa": "/əˈfɔːd/"
    }
   ],
   "senses": [
    {
     "definition": "to have enough money to pay for something",
     "example": "We can't afford a new car this year.",
     "cefr": "B1",
     "frames": [
      "can afford sth",
      "can afford to do sth"
     ]
    },
    {
     "definition": "to be able to do something without causing problems for yourself",
     "example": "You can't afford to miss any more classes before the exam.",
     "cefr": "B2",
     "frames": [
      "can't afford to do sth",
      "can't afford sth"
     ]
    },
    {
     "definition": "to give or provide something such as a chance or a view",
     "example": "The top floor affords a wonderful view of the sea.",
     "cefr": "C1",
     "frames": [
      "afford sb sth"
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

### 8. write (verb with frames)

```json
{
 "language": "en",
 "headword": "write",
 "lexemes": [
  {
   "lemma": "write",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "irregular": true,
    "valency": "Both"
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈɹaɪt/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈɹaɪt/"
    }
   ],
   "forms": [
    {
     "form": "wrote",
     "features": {
      "Tense": "Past",
      "VerbForm": "Fin"
     },
     "ipa": {
      "en-US": "/ˈɹoʊt/",
      "en-GB": "/ˈɹəʊt/"
     }
    },
    {
     "form": "written",
     "features": {
      "Tense": "Past",
      "VerbForm": "Part"
     },
     "ipa": {
      "en-US": "/ˈɹɪt.ən/",
      "en-GB": "/ˈɹɪt.ən/"
     }
    }
   ],
   "senses": [
    {
     "definition": "to make letters or words on paper or a screen with a pen, keyboard, etc.",
     "example": "Please write your name at the top of the page.",
     "cefr": "A1",
     "grammar": {
      "valency": "Both"
     },
     "antonyms": [
      {
       "lemma": "read",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "to produce a book, article, song or other text for people to read or hear",
     "example": "He has written three novels and dozens of short stories.",
     "cefr": "A2",
     "frames": [
      "write about sth",
      "write for sth"
     ],
     "grammar": {
      "valency": "Both"
     }
    },
    {
     "definition": "to send a letter, email or message to someone",
     "example": "I'll write to you as soon as we arrive.",
     "cefr": "A2",
     "frames": [
      "write to sb",
      "write sb sth"
     ],
     "grammar": {
      "valency": "Both"
     }
    }
   ],
   "relations": [
    {
     "type": "confusable",
     "lemma": "right",
     "pos": "ADJ",
     "why": "sounds exactly the same as right (and rite)"
    }
   ]
  }
 ]
}
```

### 9. aware (adjective)

```json
{
 "language": "en",
 "headword": "aware",
 "lexemes": [
  {
   "lemma": "aware",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": true
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/əˈwɛɹ/"
    },
    {
     "variety": "en-GB",
     "ipa": "/əˈweə/"
    }
   ],
   "senses": [
    {
     "definition": "knowing that something exists, is true or is happening",
     "example": "Were you aware that the shop closes early on Sundays?",
     "cefr": "B1",
     "frames": [
      "aware of sth",
      "aware that…"
     ],
     "grammar": {
      "position": "Pred"
     },
     "antonyms": [
      {
       "lemma": "unaware",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "knowing a lot about a subject and caring about it",
     "example": "Young people today are very environmentally aware.",
     "cefr": "B2",
     "frames": [
      "politically/socially aware"
     ]
    }
   ]
  }
 ]
}
```

### 10. eastern (adjective)

```json
{
 "language": "en",
 "headword": "eastern",
 "lexemes": [
  {
   "lemma": "eastern",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": false,
    "position": "Attr"
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈiː.stɚn/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈiː.stən/"
    }
   ],
   "senses": [
    {
     "definition": "in, from or facing the part of a place where the sun rises",
     "example": "Heavy snow is expected across the eastern parts of the country.",
     "cefr": "A2",
     "antonyms": [
      {
       "lemma": "western",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "from or connected with the countries of Asia, especially China and Japan",
     "example": "Many people in the city now practise Eastern forms of meditation.",
     "cefr": "B1",
     "antonyms": [
      {
       "lemma": "western",
       "pos": "ADJ"
      }
     ]
    }
   ],
   "relations": [
    {
     "type": "adjective_of",
     "lemma": "east",
     "pos": "NOUN"
    }
   ]
  }
 ]
}
```

### 11. adult (two parts of speech)

```json
{
 "language": "en",
 "headword": "adult",
 "lexemes": [
  {
   "lemma": "adult",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/əˈdʌlt/"
    },
    {
     "variety": "en-US",
     "ipa": "/ˈæd.ʌlt/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈæd.ʌlt/"
    },
    {
     "variety": "en-GB",
     "ipa": "/əˈdʌlt/"
    }
   ],
   "senses": [
    {
     "definition": "a person who is fully grown and no longer a child",
     "example": "Tickets cost ten dollars for adults and five for children.",
     "cefr": "A2",
     "antonyms": [
      {
       "lemma": "child",
       "pos": "NOUN"
      }
     ]
    },
    {
     "definition": "an animal or plant that has reached its full size",
     "example": "Young birds look quite different from the adults.",
     "cefr": "B2"
    }
   ]
  },
  {
   "lemma": "adult",
   "pos": "ADJ",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/əˈdʌlt/"
    },
    {
     "variety": "en-US",
     "ipa": "/ˈæd.ʌlt/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈæd.ʌlt/"
    },
    {
     "variety": "en-GB",
     "ipa": "/əˈdʌlt/"
    }
   ],
   "senses": [
    {
     "definition": "fully grown, or for people who are fully grown",
     "example": "She spent most of her adult life working abroad.",
     "cefr": "B1",
     "grammar": {
      "position": "Attr"
     }
    },
    {
     "definition": "behaving in a sensible, calm and grown-up way",
     "example": "Let's try to discuss this in an adult manner.",
     "cefr": "B2",
     "antonyms": [
      {
       "lemma": "childish",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "showing sex openly and meant only for grown-ups",
     "example": "The channel shows adult films late at night.",
     "cefr": "B2",
     "grammar": {
      "position": "Attr"
     },
     "labels": {
      "attitude": [
       "euphemistic"
      ]
     }
    }
   ]
  }
 ]
}
```

### 12. institute (two parts of speech)

```json
{
 "language": "en",
 "headword": "institute",
 "lexemes": [
  {
   "lemma": "institute",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈɪn.stɪ.tuːt/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈɪn.stɪ.tjuːt/"
    }
   ],
   "senses": [
    {
     "definition": "an organization or building for research, study or teaching in one subject",
     "example": "She works at a research institute for marine biology.",
     "cefr": "B1"
    }
   ],
   "relations": [
    {
     "type": "confusable",
     "lemma": "institution",
     "pos": "NOUN",
     "why": "institution is a wider word for any large organization or long-established custom"
    }
   ]
  },
  {
   "lemma": "institute",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "valency": "Tr"
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈɪn.stɪ.tuːt/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈɪn.stɪ.tjuːt/"
    }
   ],
   "senses": [
    {
     "definition": "to officially start or introduce a rule, system or legal action",
     "example": "The company will institute a new policy on working from home.",
     "cefr": "C1",
     "frames": [
      "institute sth",
      "institute proceedings against sb"
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

### 13. senate (labelled sense)

```json
{
 "language": "en",
 "headword": "senate",
 "lexemes": [
  {
   "lemma": "senate",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈsɛn.ət/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈsen.ət/"
    }
   ],
   "senses": [
    {
     "definition": "the smaller of the two groups of elected politicians that make laws in some countries",
     "example": "The bill must now be approved by the Senate.",
     "cefr": "B2",
     "labels": {
      "domain": "politics"
     }
    },
    {
     "definition": "the most important governing council in ancient Rome",
     "example": "The emperor often ignored the advice of the senate.",
     "cefr": "C1",
     "labels": {
      "time": "historical"
     }
    },
    {
     "definition": "the group of people who control a university and its teaching",
     "example": "Our university senate voted to change the exam rules.",
     "cefr": "C1"
    }
   ]
  }
 ]
}
```

### 14. mid (labelled sense)

```json
{
 "language": "en",
 "headword": "mid",
 "lexemes": [
  {
   "lemma": "mid",
   "pos": "ADJ",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈmɪd/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈmɪd/"
    }
   ],
   "senses": [
    {
     "definition": "in the central part of a period of time, place or activity",
     "example": "We usually go on holiday in mid August.",
     "cefr": "B1",
     "grammar": {
      "position": "Attr"
     }
    },
    {
     "definition": "of only average or poor quality; not very good",
     "example": "Honestly, I thought the new album was kind of mid.",
     "cefr": "C1",
     "grammar": {
      "position": "Pred"
     },
     "labels": {
      "formality": "slang"
     }
    }
   ]
  },
  {
   "lemma": "mid",
   "pos": "ADP",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈmɪd/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈmɪd/"
    }
   ],
   "spellings": [
    {
     "text": "mid"
    },
    {
     "text": "'mid"
    }
   ],
   "senses": [
    {
     "definition": "surrounded by or among other things or people",
     "example": "The old chapel still stood mid the ruins of the town.",
     "cefr": "C2",
     "labels": {
      "style": [
       "literary",
       "poetic"
      ]
     }
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "amid",
     "pos": "ADP"
    }
   ]
  }
 ]
}
```

### 15. hall (regional sense)

```json
{
 "language": "en",
 "headword": "hall",
 "lexemes": [
  {
   "lemma": "hall",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈhɔːl/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈhɔːl/"
    }
   ],
   "senses": [
    {
     "definition": "a large room or building used for meetings, concerts or other public events",
     "example": "Hundreds of people filled the concert hall last night.",
     "cefr": "A2"
    },
    {
     "definition": "the area just inside the front door of a house, or a passage between rooms",
     "example": "You can leave your wet boots in the hall.",
     "cefr": "A2"
    },
    {
     "definition": "a building where university students live during their studies",
     "example": "Most first-year students live in halls near the campus.",
     "cefr": "B2",
     "frames": [
      "hall of residence",
      "live in halls"
     ],
     "regions": [
      {
       "variety": "en-GB",
       "extent": "only"
      }
     ]
    }
   ],
   "relations": [
    {
     "type": "confusable",
     "lemma": "haul",
     "pos": "VERB",
     "why": "sounds the same as haul, to pull something heavy"
    }
   ]
  }
 ]
}
```

### 16. johnson (regional sense)

```json
{
 "language": "en",
 "headword": "johnson",
 "lexemes": [
  {
   "lemma": "johnson",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈdʒɑːn.sən/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈdʒɒn.sən/"
    }
   ],
   "senses": [
    {
     "definition": "the male sexual organ; a man's penis",
     "example": "He joked that the icy lake water had shrunk his johnson.",
     "cefr": "C2",
     "labels": {
      "formality": "slang",
      "attitude": [
       "vulgar",
       "humorous"
      ]
     },
     "regions": [
      {
       "variety": "en-US",
       "extent": "only"
      }
     ]
    }
   ]
  }
 ]
}
```

### 17. criminal (figurative sense)

```json
{
 "language": "en",
 "headword": "criminal",
 "lexemes": [
  {
   "lemma": "criminal",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈkɹɪm.ə.nəl/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈkɹɪm.ɪ.nəl/"
    }
   ],
   "senses": [
    {
     "definition": "a person who has broken the law, especially in a serious way",
     "example": "Police finally caught the criminals who robbed the bank.",
     "cefr": "B1"
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "crime",
     "pos": "NOUN"
    }
   ]
  },
  {
   "lemma": "criminal",
   "pos": "ADJ",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈkɹɪm.ə.nəl/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈkɹɪm.ɪ.nəl/"
    }
   ],
   "senses": [
    {
     "definition": "involving illegal acts, or dealing with the law that punishes them",
     "example": "Do you have a criminal record of any kind?",
     "cefr": "B1",
     "grammar": {
      "position": "Attr"
     }
    },
    {
     "definition": "morally wrong or very foolish, and deserving strong disapproval",
     "example": "It's criminal to throw away so much good food.",
     "cefr": "B2",
     "frames": [
      "it is criminal to do sth"
     ],
     "labels": {
      "figurative": true
     }
    }
   ],
   "relations": [
    {
     "type": "adjective_of",
     "lemma": "crime",
     "pos": "NOUN"
    }
   ]
  }
 ]
}
```

### 18. royal (figurative sense)

```json
{
 "language": "en",
 "headword": "royal",
 "lexemes": [
  {
   "lemma": "royal",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": false
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈɹɔɪ.əl/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈɹɔɪ.əl/"
    }
   ],
   "senses": [
    {
     "definition": "belonging to or connected with a king or queen and their family",
     "example": "Tourists gather outside the palace to watch the royal wedding.",
     "cefr": "B1"
    },
    {
     "definition": "very fine and impressive, as if meant for a king or queen",
     "example": "The whole village gave us a royal welcome when we arrived.",
     "cefr": "C1",
     "labels": {
      "figurative": true
     }
    }
   ]
  },
  {
   "lemma": "royal",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ˈɹɔɪ.əl/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ˈɹɔɪ.əl/"
    }
   ],
   "senses": [
    {
     "definition": "a member of the family of a king or queen",
     "example": "Crowds waited for hours to see the royals arrive.",
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

### 19. perhaps (adverb)

```json
{
 "language": "en",
 "headword": "perhaps",
 "lexemes": [
  {
   "lemma": "perhaps",
   "pos": "ADV",
   "kind": "word",
   "grammar": {
    "comparable": false
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/pɚˈhæps/"
    },
    {
     "variety": "en-GB",
     "ipa": "/pəˈhæps/"
    }
   ],
   "senses": [
    {
     "definition": "used to say that something is possible but not certain",
     "example": "Perhaps the train is late because of the snow.",
     "cefr": "A2",
     "antonyms": [
      {
       "lemma": "definitely",
       "pos": "ADV"
      }
     ]
    },
    {
     "definition": "used to make a request, offer or suggestion sound more polite",
     "example": "Perhaps you could send me the details by email?",
     "cefr": "B1"
    }
   ]
  }
 ]
}
```

### 20. instead (adverb)

```json
{
 "language": "en",
 "headword": "instead",
 "lexemes": [
  {
   "lemma": "instead",
   "pos": "ADV",
   "kind": "word",
   "grammar": {
    "comparable": false
   },
   "pronunciations": [
    {
     "variety": "en-US",
     "ipa": "/ɪnˈstɛd/"
    },
    {
     "variety": "en-GB",
     "ipa": "/ɪnˈsted/"
    }
   ],
   "senses": [
    {
     "definition": "in the place of someone or something else; as a different choice",
     "example": "There was no rice left, so we had pasta instead.",
     "cefr": "A2",
     "frames": [
      "instead of sth",
      "instead of doing sth"
     ]
    }
   ]
  }
 ]
}
```
