# Example entries for Portuguese: review

These are entries of a learner's dictionary of Portuguese, written by a model. The best four or five will be
shown to the writing model as examples of the house style, so an error left in one is copied into
thousands of entries. Review each candidate strictly.

For each candidate, check:

1. **Facts.** Is anything wrong: a meaning, a gender, a plural or another form, a transcription, a frame,
   a label, a region, a level that is off by more than one step?
2. **The sense list.** Would a monolingual learner's dictionary of Portuguese number exactly these senses?
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

The 1990 Orthographic Agreement removed silent consonants and some accents: acção, óptimo, idéia, vôo and freqüente are pre-reform spellings. Where Brazil and Portugal still write a word differently (econômico pt-BR and económico pt-PT; fato pt-BR and facto pt-PT for "fact"), give both spellings with their varieties. Give "spellings" whenever the lemma is written more than one way: every current spelling with the varieties that use it ("colour" en-GB, en-AU, en-IN and "color" en-US), and a spelling only older books use with "pre_reform": true. The other spellings are never written as entries of their own, so list them all; a lemma written one way has no "spellings".

When the request has a line "Covered:", the headword is already listed as a spelling of the lexemes named there, and they are written. Do not write them again: return only lexemes of this spelling that are other words (English "tire", to make someone tired, when "tire" is covered as a spelling of the noun "tyre"), and if there are none, reject with "spelling of" and the lemma. The varieties of Portuguese are only these: pt-BR, pt-PT.

# Pronunciation

Phonemic IPA for each of these varieties and no other: pt-BR, pt-PT; between slashes, with a primary stress mark ˈ on every word including monosyllables, and a dot between syllables. When a variety says the word in more than one way, give the most common first; a reading after the first may carry a "note": less_common, older or newer (said mainly by older or younger speakers), careful or casual, regional (in part of the variety only), disputed (considered wrong by some). Give a note only when you are sure of it. Use only these symbols:
pt-BR: consonants p b t d k ɡ f v s z ʃ ʒ tʃ dʒ m n ɲ l ʎ ɾ ʁ j w; vowels i e ɛ a ɐ ɔ o u ĩ ẽ ɐ̃ õ ũ; marks ˈ ˌ . ː. Brazilian Portuguese, São Paulo standard. t and d before i are tʃ and dʒ. Written r at word start and rr are ʁ; single r between vowels is ɾ. Final unstressed e and o are i and u. Nasal vowels carry the tilde. Final l is w.
pt-PT: consonants p b t d k ɡ f v s z ʃ ʒ m n ɲ l ʎ ɾ ʁ j w; vowels i e ɛ a ɐ ɔ o u ɨ ĩ ẽ ɐ̃ õ ũ; marks ˈ ˌ . ː. European Portuguese, Lisbon standard. Unstressed e is reduced to ɨ and unstressed a to ɐ; unstressed o is u. Written r at word start and rr are ʁ; single r between vowels is ɾ. Syllable-final s is ʃ. Nasal vowels carry the tilde.

# Grammar and forms

Grammar: only what the language needs, and only these keys, with these values: gender (Masc, Fem, Neut; Com for a noun that is masculine for a man and feminine for a woman without changing its form: el/la estudiante, колега), number (Sing or Plur, only for a noun used in one number), countability (Count, Mass, Both), valency (Tr, Intr, Both, Refl, Impers), irregular (true/false), conjugation (1, 2, 3 by the infinitive ending in the language's own order; German: strong, weak, mixed), auxiliary (have, be, both), separable and separable_prefix, reflexive, declension (1-4, weak, strong, mixed, adjectival), plural_class (German: -e, -¨e, -en, -n, -er, -¨er, -s, -nen, or - for no change), aspect (Imp, Perf), animacy (Anim, Inan), invariable, position (Attr, Pred), comparable, and note for anything else, in one short clause. Where one variety differs from the rest, put only the difference under "by_variety" (German "E-Mail": gender Fem, by_variety de-AT and de-CH: gender Neut).

Forms: Nouns: the plural when irregular (pão, pães; cidadão, cidadãos). Adjectives: the feminine and plural when irregular. Verbs: nothing beyond grammar for a regular verb; for an irregular or stem-changing verb the present indicative 1sg, 2sg, 3sg, 1pl and 3pl, the pretérito perfeito 1sg and 3sg, the present subjunctive 1sg and every past participle (aceitado, aceito). The regular forms are generated later from the grammar, so list only what a learner has to be told. Features use Universal Dependencies names and values only (Number=Plur, Case=Gen, Tense=Past, Person=3, VerbForm=Part, Mood=Sub); a form that fills several cells joins the values with a comma (VerbForm=Fin,Part). A form used only in some varieties lists them ("learnt" en-GB). A form that belongs to only some senses lists their numbers, counting from 1 ("brethren" for the religious sense of "brother"). A listed form carries its own ipa for the same varieties.

# Senses

Senses in descending frequency of use. Only senses a reader meets in books and ordinary text; no technical, dialectal or obsolete senses unless the headword is itself rare. Two senses are separate only when a monolingual learner's dictionary of Portuguese would number them separately. Most words have one or two senses; give more only for a word that is truly polysemous. Do not split shades of one meaning, and do not make senses out of subject areas or facts about the thing ("potato" as a plant, a food and a crop is one sense). Include a common figurative sense ("a climate of fear"). Give only senses the Portuguese word has: a meaning that only its English equivalent has does not belong here.

Definition. In Portuguese, 6 to 15 words, plain words a learner knows, a phrase not a sentence, in the style of a learner's dictionary. The headword and words of its family must not appear in it. No example inside it, and no formula such as "the act of", "the state of being", "relating to" or "characteristic of". A verb is defined as a verb, a noun as a noun.

Example. One sentence in Portuguese of 6 to 14 words, natural, no proper names, that shows this sense and its frame if it has one. Vary the examples: not always the past tense, not always a third-person subject, not always starting with an article.

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
Regions. A sense used only or mainly in part of the Portuguese-speaking world lists where, each with "variety" and "extent". A variety is the language with a country (pt-BR, pt-PT, pt-AO) or with a UN area (pt-002 Africa). The extent is "only" when the sense is not used elsewhere and "mainly" when it is used everywhere but chiefly there ("lead", a dog's strap: en-GB only). Leave regions out for what the whole language uses, and never put a region in a definition. When a word is neutral in one variety and offensive or vulgar in another, those are separate senses, each with its regions and labels.

Antonyms. For a sense with a clear opposite, the opposite's lemma and part of speech. No synonyms.

# Relations

Relations point from this lexeme to the word it is made from or is confused with, each as type, lemma and part of speech; the reverse links are filled in later, so the base word does not list its derivatives. Types: diminutive_of (хлопчик of хлопець), augmentative_of, endearing_of (сонечко of сонце), feminine_of (actress of actor), agent_noun_of (teacher of teach), verbal_noun_of (читання of читати), abstract_noun_of (kindness of kind), adjective_of (urban of city), adverb_of (quickly of quick), derived_from (any other derivation), aspect_partner (зробити of робити), and confusable (a different word learners mix it up with, with "why" in English). Give only relations you are sure of.

Write the same way for every word, so that neighbouring entries read as one dictionary. Give nothing you are unsure of; an empty field is better than a guess.

## Candidates

### 1. computador (noun)

```json
{
 "language": "pt",
 "headword": "computador",
 "lexemes": [
  {
   "lemma": "computador",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/kõ.pu.ta.ˈdoɾ/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/kõ.pu.tɐ.ˈdoɾ/"
    }
   ],
   "senses": [
    {
     "definition": "máquina que guarda e processa dados e executa programas",
     "example": "Preciso de um computador novo para trabalhar em casa.",
     "cefr": "A1"
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "computar",
     "pos": "VERB"
    }
   ]
  }
 ]
}
```

### 2. episódio (noun)

```json
{
 "language": "pt",
 "headword": "episódio",
 "lexemes": [
  {
   "lemma": "episódio",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/e.pi.ˈzɔ.dʒju/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/i.pi.ˈzɔ.dju/"
    }
   ],
   "senses": [
    {
     "definition": "cada uma das partes de uma série de televisão, rádio ou podcast",
     "example": "Hoje à noite vamos ver o último episódio da série.",
     "cefr": "A2"
    },
    {
     "definition": "acontecimento isolado, muitas vezes marcante, dentro de uma história ou situação maior",
     "example": "Aquele episódio desagradável na escola nunca foi esquecido pelos alunos.",
     "cefr": "B2"
    }
   ]
  }
 ]
}
```

### 3. prefeito (noun)

```json
{
 "language": "pt",
 "headword": "prefeito",
 "lexemes": [
  {
   "lemma": "prefeito",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/pɾe.ˈfej.tu/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/pɾɨ.ˈfɐj.tu/"
    }
   ],
   "senses": [
    {
     "definition": "pessoa eleita que governa uma cidade ou um município",
     "example": "O prefeito prometeu consertar as ruas do bairro até dezembro.",
     "cefr": "A2",
     "regions": [
      {
       "variety": "pt-BR",
       "extent": "only"
      }
     ]
    },
    {
     "definition": "alto funcionário que governa uma região em nome do poder central",
     "example": "O imperador nomeou um novo prefeito para governar a província.",
     "cefr": "C1"
    }
   ]
  }
 ]
}
```

### 4. redor (noun)

```json
{
 "language": "pt",
 "headword": "redor",
 "lexemes": [
  {
   "lemma": "redor",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count",
    "note": "used mostly in the locutions ao redor (de) and em redor (de)"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/ʁe.ˈdɔʁ/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/ʁɨ.ˈdɔɾ/"
    }
   ],
   "senses": [
    {
     "definition": "espaço ou área que fica em volta de uma coisa ou pessoa",
     "example": "Sentamos todos ao redor da fogueira para ouvir as histórias.",
     "cefr": "B1",
     "frames": [
      "ao redor de",
      "em redor de"
     ]
    }
   ]
  }
 ]
}
```

### 5. escolher (verb with frames)

```json
{
 "language": "pt",
 "headword": "escolher",
 "lexemes": [
  {
   "lemma": "escolher",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "2",
    "valency": "Tr",
    "irregular": false,
    "note": "spelling regular; stressed stem o is open ɔ in escolhes, escolhe, escolhem"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/es.koˈʎeɾ/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/ɨʃ.kuˈʎeɾ/"
    }
   ],
   "forms": [
    {
     "form": "escolho",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/esˈko.ʎu/",
      "pt-PT": "/ɨʃˈko.ʎu/"
     }
    },
    {
     "form": "escolhes",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "2",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/esˈkɔ.ʎis/",
      "pt-PT": "/ɨʃˈkɔ.ʎɨʃ/"
     }
    },
    {
     "form": "escolhe",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "3",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/esˈkɔ.ʎi/",
      "pt-PT": "/ɨʃˈkɔ.ʎɨ/"
     }
    },
    {
     "form": "escolhemos",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Plur",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/es.koˈʎe.mus/",
      "pt-PT": "/ɨʃ.kuˈʎe.muʃ/"
     }
    },
    {
     "form": "escolhem",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "3",
      "Number": "Plur",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/esˈkɔ.ʎẽj/",
      "pt-PT": "/ɨʃˈkɔ.ʎɐ̃j/"
     }
    },
    {
     "form": "escolhi",
     "features": {
      "Mood": "Ind",
      "Tense": "Past",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/es.koˈʎi/",
      "pt-PT": "/ɨʃ.kuˈʎi/"
     }
    },
    {
     "form": "escolheu",
     "features": {
      "Mood": "Ind",
      "Tense": "Past",
      "Person": "3",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/es.koˈʎew/",
      "pt-PT": "/ɨʃ.kuˈʎew/"
     }
    },
    {
     "form": "escolha",
     "features": {
      "Mood": "Sub",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/esˈko.ʎɐ/",
      "pt-PT": "/ɨʃˈko.ʎɐ/"
     }
    },
    {
     "form": "escolhido",
     "features": {
      "VerbForm": "Part",
      "Tense": "Past"
     },
     "ipa": {
      "pt-BR": "/es.koˈʎi.du/",
      "pt-PT": "/ɨʃ.kuˈʎi.du/"
     }
    }
   ],
   "senses": [
    {
     "definition": "decidir qual, entre várias coisas ou pessoas, se prefere",
     "example": "Podes escolher entre o vestido azul e o vermelho.",
     "cefr": "A1",
     "frames": [
      "escolher algo/alguém",
      "escolher entre A e B",
      "escolher + infinitivo"
     ]
    },
    {
     "definition": "separar o que é bom do que é mau ou impróprio",
     "example": "Antes de cozinhar, é preciso escolher bem o feijão.",
     "cefr": "B2"
    }
   ]
  }
 ]
}
```

### 6. lidar (verb with frames)

```json
{
 "language": "pt",
 "headword": "lidar",
 "lexemes": [
  {
   "lemma": "lidar",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "1",
    "irregular": false,
    "valency": "Both"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/li.ˈdaʁ/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/li.ˈdaɾ/"
    }
   ],
   "senses": [
    {
     "definition": "enfrentar e tentar resolver uma situação ou um problema difícil",
     "example": "Ainda não sei como lidar com tanta pressão no trabalho.",
     "cefr": "B1",
     "frames": [
      "lidar com algo"
     ],
     "grammar": {
      "valency": "Intr"
     }
    },
    {
     "definition": "ter contato ou trabalhar regularmente com pessoas ou coisas",
     "example": "Quem lida com o público precisa de muita paciência.",
     "cefr": "B2",
     "frames": [
      "lidar com alguém",
      "lidar com algo"
     ],
     "grammar": {
      "valency": "Intr"
     }
    },
    {
     "definition": "enfrentar um touro na arena, fazendo-o investir contra a capa",
     "example": "O cavaleiro lidou dois touros na corrida de domingo.",
     "cefr": "C2",
     "regions": [
      {
       "variety": "pt-PT",
       "extent": "only"
      }
     ],
     "grammar": {
      "valency": "Tr"
     }
    }
   ]
  }
 ]
}
```

### 7. trazer (verb with frames)

```json
{
 "language": "pt",
 "headword": "trazer",
 "lexemes": [
  {
   "lemma": "trazer",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "2",
    "irregular": true,
    "valency": "Tr"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/tɾa.ˈzeɾ/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/tɾɐ.ˈzeɾ/"
    }
   ],
   "forms": [
    {
     "form": "trago",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/ˈtɾa.ɡu/",
      "pt-PT": "/ˈtɾa.ɡu/"
     }
    },
    {
     "form": "trazes",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "2",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/ˈtɾa.zis/",
      "pt-PT": "/ˈtɾa.zɨʃ/"
     }
    },
    {
     "form": "traz",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "3",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/ˈtɾas/",
      "pt-PT": "/ˈtɾaʃ/"
     }
    },
    {
     "form": "trazemos",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Plur",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/tɾa.ˈze.mus/",
      "pt-PT": "/tɾɐ.ˈze.muʃ/"
     }
    },
    {
     "form": "trazem",
     "features": {
      "Mood": "Ind",
      "Tense": "Pres",
      "Person": "3",
      "Number": "Plur",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/ˈtɾa.zẽj/",
      "pt-PT": "/ˈtɾa.zɐ̃j/"
     }
    },
    {
     "form": "trouxe",
     "features": {
      "Mood": "Ind",
      "Tense": "Past",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/ˈtɾo.si/",
      "pt-PT": "/ˈtɾo.sɨ/"
     }
    },
    {
     "form": "trouxe",
     "features": {
      "Mood": "Ind",
      "Tense": "Past",
      "Person": "3",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/ˈtɾo.si/",
      "pt-PT": "/ˈtɾo.sɨ/"
     }
    },
    {
     "form": "traga",
     "features": {
      "Mood": "Sub",
      "Tense": "Pres",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/ˈtɾa.ɡɐ/",
      "pt-PT": "/ˈtɾa.ɡɐ/"
     }
    },
    {
     "form": "trarei",
     "features": {
      "Mood": "Ind",
      "Tense": "Fut",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/tɾa.ˈɾej/",
      "pt-PT": "/tɾɐ.ˈɾɐj/"
     }
    },
    {
     "form": "traria",
     "features": {
      "Mood": "Cnd",
      "Person": "1",
      "Number": "Sing",
      "VerbForm": "Fin"
     },
     "ipa": {
      "pt-BR": "/tɾa.ˈɾi.ɐ/",
      "pt-PT": "/tɾɐ.ˈɾi.ɐ/"
     }
    },
    {
     "form": "trazido",
     "features": {
      "VerbForm": "Part",
      "Tense": "Past"
     },
     "ipa": {
      "pt-BR": "/tɾa.ˈzi.du/",
      "pt-PT": "/tɾɐ.ˈzi.du/"
     }
    }
   ],
   "senses": [
    {
     "definition": "levar alguém ou alguma coisa até o lugar onde está quem fala",
     "example": "Pode trazer um copo de água para mim, por favor?",
     "cefr": "A1",
     "frames": [
      "trazer algo a/para alguém",
      "trazer algo de algum lugar"
     ],
     "antonyms": [
      {
       "lemma": "levar",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "ser a causa de algo; ter algo como resultado",
     "example": "Essas mudanças vão trazer muitos benefícios para a cidade.",
     "cefr": "B1",
     "frames": [
      "trazer algo a/para alguém"
     ]
    },
    {
     "definition": "ter ou apresentar algum conteúdo, como notícia ou informação",
     "example": "O jornal de hoje traz uma entrevista com o novo ministro.",
     "cefr": "B1"
    }
   ],
   "relations": [
    {
     "type": "confusable",
     "lemma": "levar",
     "pos": "VERB",
     "why": "trazer is movement towards the speaker, levar is movement away from the speaker"
    }
   ]
  }
 ]
}
```

### 8. acreditar (verb with frames)

```json
{
 "language": "pt",
 "headword": "acreditar",
 "lexemes": [
  {
   "lemma": "acreditar",
   "pos": "VERB",
   "kind": "word",
   "grammar": {
    "conjugation": "1",
    "irregular": false,
    "valency": "Both"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/a.kɾe.dʒi.ˈtaʁ/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/ɐ.kɾɨ.di.ˈtaɾ/"
    }
   ],
   "senses": [
    {
     "definition": "considerar que algo é verdadeiro ou que alguém diz a verdade",
     "example": "Ainda não acredito que passamos no exame!",
     "cefr": "A2",
     "frames": [
      "acreditar em algo/alguém",
      "acreditar que + oração"
     ],
     "grammar": {
      "valency": "Both"
     },
     "antonyms": [
      {
       "lemma": "duvidar",
       "pos": "VERB"
      }
     ]
    },
    {
     "definition": "ter fé ou confiança em alguém, numa ideia ou em Deus",
     "example": "Você precisa acreditar em si mesmo para vencer.",
     "cefr": "B1",
     "frames": [
      "acreditar em alguém/algo"
     ],
     "grammar": {
      "valency": "Intr"
     }
    },
    {
     "definition": "reconhecer oficialmente alguém ou algo como autorizado ou válido",
     "example": "O governo acreditou cem jornalistas para cobrir a conferência.",
     "cefr": "C1",
     "grammar": {
      "valency": "Tr"
     },
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

### 9. breve (adjective)

```json
{
 "language": "pt",
 "headword": "breve",
 "lexemes": [
  {
   "lemma": "breve",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": true,
    "note": "same form for masculine and feminine; plural breves"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/ˈbɾɛ.vi/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/ˈbɾɛ.vɨ/"
    }
   ],
   "senses": [
    {
     "definition": "que dura pouco tempo ou acaba depressa",
     "example": "Fizemos uma breve pausa antes de continuar a reunião.",
     "cefr": "B1",
     "antonyms": [
      {
       "lemma": "longo",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "dito ou escrito com poucas palavras, sem detalhes desnecessários",
     "example": "Seja breve, por favor, porque temos pouco tempo.",
     "cefr": "B2",
     "antonyms": [
      {
       "lemma": "extenso",
       "pos": "ADJ"
      }
     ]
    }
   ]
  }
 ]
}
```

### 10. global (adjective)

```json
{
 "language": "pt",
 "headword": "global",
 "lexemes": [
  {
   "lemma": "global",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "note": "same form for masculine and feminine; plural globais"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/ɡloˈbaw/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/ɡluˈbal/"
    }
   ],
   "senses": [
    {
     "definition": "que abrange o mundo inteiro ou todos os países",
     "example": "O aquecimento global preocupa cientistas de todos os continentes.",
     "cefr": "B1",
     "antonyms": [
      {
       "lemma": "local",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "considerado no seu conjunto, sem separar as partes",
     "example": "Qual é o custo global da obra, com todos os materiais?",
     "cefr": "B2",
     "antonyms": [
      {
       "lemma": "parcial",
       "pos": "ADJ"
      }
     ]
    }
   ],
   "relations": [
    {
     "type": "adjective_of",
     "lemma": "globo",
     "pos": "NOUN"
    }
   ]
  }
 ]
}
```

### 11. francês (two parts of speech)

```json
{
 "language": "pt",
 "headword": "francês",
 "lexemes": [
  {
   "lemma": "francês",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "note": "feminine francesa; the accent is dropped in the inflected forms"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/fɾɐ̃.ˈses/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/fɾɐ̃.ˈseʃ/"
    }
   ],
   "forms": [
    {
     "form": "francesa",
     "features": {
      "Gender": "Fem",
      "Number": "Sing"
     },
     "ipa": {
      "pt-BR": "/fɾɐ̃.ˈse.zɐ/",
      "pt-PT": "/fɾɐ̃.ˈse.zɐ/"
     }
    },
    {
     "form": "franceses",
     "features": {
      "Gender": "Masc",
      "Number": "Plur"
     },
     "ipa": {
      "pt-BR": "/fɾɐ̃.ˈse.zis/",
      "pt-PT": "/fɾɐ̃.ˈse.zɨʃ/"
     }
    },
    {
     "form": "francesas",
     "features": {
      "Gender": "Fem",
      "Number": "Plur"
     },
     "ipa": {
      "pt-BR": "/fɾɐ̃.ˈse.zɐs/",
      "pt-PT": "/fɾɐ̃.ˈse.zɐʃ/"
     }
    }
   ],
   "senses": [
    {
     "definition": "que é da França, vem desse país ou de lá é típico",
     "example": "Comprei um queijo francês muito bom na feira de ontem.",
     "cefr": "A1"
    }
   ],
   "relations": [
    {
     "type": "adjective_of",
     "lemma": "França",
     "pos": "PROPN"
    }
   ]
  },
  {
   "lemma": "francês",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Both"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/fɾɐ̃.ˈses/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/fɾɐ̃.ˈseʃ/"
    }
   ],
   "forms": [
    {
     "form": "franceses",
     "features": {
      "Number": "Plur"
     },
     "ipa": {
      "pt-BR": "/fɾɐ̃.ˈse.zis/",
      "pt-PT": "/fɾɐ̃.ˈse.zɨʃ/"
     },
     "senses": [
      2
     ]
    }
   ],
   "senses": [
    {
     "definition": "língua falada na França, na Bélgica, no Canadá e noutros países",
     "example": "Ela fala francês muito bem desde criança.",
     "cefr": "A1",
     "grammar": {
      "countability": "Mass"
     }
    },
    {
     "definition": "homem que nasceu na França ou tem essa nacionalidade",
     "example": "Os franceses costumam jantar mais tarde do que nós.",
     "cefr": "A2",
     "grammar": {
      "countability": "Count"
     }
    }
   ]
  }
 ]
}
```

### 12. policial (two parts of speech)

```json
{
 "language": "pt",
 "headword": "policial",
 "lexemes": [
  {
   "lemma": "policial",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": false
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/po.li.siˈaw/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/pu.li.siˈal/"
    }
   ],
   "senses": [
    {
     "definition": "das forças que mantêm a ordem pública e combatem o crime",
     "example": "Houve uma operação policial no bairro durante a noite.",
     "cefr": "B1"
    },
    {
     "definition": "diz-se de livro ou filme sobre crimes e a sua investigação",
     "example": "Adoro ler romances policiais antes de dormir.",
     "cefr": "B1"
    }
   ],
   "relations": [
    {
     "type": "adjective_of",
     "lemma": "polícia",
     "pos": "NOUN"
    }
   ]
  },
  {
   "lemma": "policial",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Com",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/po.li.siˈaw/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/pu.li.siˈal/"
    }
   ],
   "senses": [
    {
     "definition": "pessoa que trabalha nas forças que mantêm a ordem pública",
     "example": "Pergunte ao policial da esquina onde fica a estação.",
     "cefr": "A2",
     "regions": [
      {
       "variety": "pt-BR",
       "extent": "only"
      }
     ]
    }
   ],
   "relations": [
    {
     "type": "derived_from",
     "lemma": "polícia",
     "pos": "NOUN"
    },
    {
     "type": "confusable",
     "lemma": "polícia",
     "pos": "NOUN",
     "why": "in Portugal a police officer is usually called 'um polícia', while Brazil says 'um policial'"
    }
   ]
  }
 ]
}
```

### 13. ciência (labelled sense)

```json
{
 "language": "pt",
 "headword": "ciência",
 "lexemes": [
  {
   "lemma": "ciência",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Fem",
    "countability": "Both"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/siˈẽ.sjɐ/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/ˈsjẽ.sjɐ/"
    }
   ],
   "senses": [
    {
     "definition": "conjunto de conhecimentos obtidos pela observação, experiência e estudo rigoroso do mundo",
     "example": "A ciência ainda não explica todos os mistérios do universo.",
     "cefr": "B1",
     "grammar": {
      "countability": "Mass"
     }
    },
    {
     "definition": "cada área de estudo com objeto e métodos próprios, como a física",
     "example": "Na universidade, ela quer estudar ciências sociais ou economia.",
     "cefr": "B1",
     "grammar": {
      "countability": "Count"
     }
    },
    {
     "definition": "conhecimento ou informação que alguém tem sobre um assunto",
     "example": "Você tem ciência dos riscos deste tratamento?",
     "cefr": "C1",
     "frames": [
      "ter ciência de algo",
      "tomar ciência de algo",
      "dar ciência a alguém de algo"
     ],
     "grammar": {
      "countability": "Mass"
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

### 14. pronto (labelled sense)

```json
{
 "language": "pt",
 "headword": "pronto",
 "lexemes": [
  {
   "lemma": "pronto",
   "pos": "ADJ",
   "kind": "word",
   "grammar": {
    "comparable": false
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/ˈpɾõ.tu/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/ˈpɾõ.tu/"
    }
   ],
   "senses": [
    {
     "definition": "preparado e em condições de fazer algo ou de ser usado",
     "example": "Estou pronto para sair, só falta calçar os sapatos.",
     "cefr": "A1",
     "frames": [
      "estar pronto para + inf",
      "pronto para algo"
     ],
     "grammar": {
      "position": "Pred"
     }
    },
    {
     "definition": "acabado, terminado, com todo o trabalho já feito",
     "example": "O jantar já está pronto, venham para a mesa!",
     "cefr": "A1",
     "grammar": {
      "position": "Pred"
     },
     "antonyms": [
      {
       "lemma": "inacabado",
       "pos": "ADJ"
      }
     ]
    },
    {
     "definition": "que acontece ou se faz sem demora; rápido, imediato",
     "example": "Agradecemos a sua pronta resposta ao nosso pedido.",
     "cefr": "B2",
     "labels": {
      "formality": "formal"
     },
     "grammar": {
      "position": "Attr"
     }
    }
   ]
  },
  {
   "lemma": "pronto",
   "pos": "INTJ",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/ˈpɾõ.tu/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/ˈpɾõ.tu/"
    }
   ],
   "senses": [
    {
     "definition": "palavra que indica que algo terminou ou ficou resolvido",
     "example": "Pronto, já arrumei a cozinha toda.",
     "cefr": "A2",
     "labels": {
      "formality": "informal"
     }
    },
    {
     "definition": "palavra usada na conversa para concordar, aceitar ou mudar de assunto",
     "example": "Pronto, está bem, amanhã falamos sobre isso.",
     "cefr": "B1",
     "labels": {
      "formality": "informal"
     },
     "regions": [
      {
       "variety": "pt-PT",
       "extent": "only"
      }
     ]
    }
   ]
  }
 ]
}
```

### 15. vice (regional sense)

```json
{
 "language": "pt",
 "headword": "vice",
 "lexemes": [
  {
   "lemma": "vice",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Com",
    "countability": "Count",
    "note": "clipping of vice-presidente, vice-governador, vice-campeão etc."
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/ˈvi.si/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/ˈvi.sɨ/"
    }
   ],
   "senses": [
    {
     "definition": "pessoa que substitui o presidente, governador ou outro chefe quando necessário",
     "example": "Durante a viagem do presidente, quem governa o país é a vice.",
     "cefr": "B1",
     "labels": {
      "formality": "informal"
     }
    },
    {
     "definition": "segundo lugar num campeonato, ou a equipa que fica nessa posição",
     "example": "Depois de três finais perdidas, o clube voltou a ser vice.",
     "cefr": "B2",
     "regions": [
      {
       "variety": "pt-BR",
       "extent": "only"
      }
     ],
     "labels": {
      "domain": "sport",
      "formality": "informal"
     }
    }
   ]
  }
 ]
}
```

### 16. casal (regional sense)

```json
{
 "language": "pt",
 "headword": "casal",
 "lexemes": [
  {
   "lemma": "casal",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Masc",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/ka.ˈzaw/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/kɐ.ˈzal/"
    }
   ],
   "senses": [
    {
     "definition": "duas pessoas unidas por casamento ou por uma relação amorosa",
     "example": "Conhecemos um casal de italianos muito simpático durante as férias.",
     "cefr": "A2",
     "frames": [
      "casal de + subst."
     ]
    },
    {
     "definition": "um macho e uma fêmea da mesma espécie de animal",
     "example": "Vamos comprar um casal de periquitos para as crianças.",
     "cefr": "B1",
     "frames": [
      "casal de + subst."
     ]
    },
    {
     "definition": "pequeno povoado no campo, com poucas casas e terras",
     "example": "A minha avó nasceu num casal perto da serra.",
     "cefr": "C1",
     "regions": [
      {
       "variety": "pt-PT",
       "extent": "only"
      }
     ]
    }
   ]
  }
 ]
}
```

### 17. chave (figurative sense)

```json
{
 "language": "pt",
 "headword": "chave",
 "lexemes": [
  {
   "lemma": "chave",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Fem",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/ˈʃa.vi/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/ˈʃa.vɨ/"
    }
   ],
   "senses": [
    {
     "definition": "peça de metal usada para abrir e fechar fechaduras",
     "example": "Esqueci a chave do carro em cima da mesa da cozinha.",
     "cefr": "A1"
    },
    {
     "definition": "aquilo que permite entender ou resolver algo; elemento essencial",
     "example": "Para mim, a paciência é a chave para aprender uma língua.",
     "cefr": "B1",
     "frames": [
      "a chave de/para algo"
     ],
     "labels": {
      "figurative": true
     }
    },
    {
     "definition": "ferramenta usada para apertar ou soltar parafusos e porcas",
     "example": "Preciso de uma chave maior para soltar esta porca.",
     "cefr": "B2"
    }
   ]
  }
 ]
}
```

### 18. doença (figurative sense)

```json
{
 "language": "pt",
 "headword": "doença",
 "lexemes": [
  {
   "lemma": "doença",
   "pos": "NOUN",
   "kind": "word",
   "grammar": {
    "gender": "Fem",
    "countability": "Count"
   },
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/doˈẽ.sɐ/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/duˈẽ.sɐ/"
    }
   ],
   "senses": [
    {
     "definition": "alteração da saúde do corpo ou da mente que causa sofrimento",
     "example": "A minha avó tem uma doença grave no coração.",
     "cefr": "A2",
     "frames": [
      "doença de algo"
     ],
     "antonyms": [
      {
       "lemma": "saúde",
       "pos": "NOUN"
      }
     ]
    },
    {
     "definition": "problema grave e difícil de resolver numa sociedade ou num grupo",
     "example": "Para muitos, a corrupção é a grande doença do país.",
     "cefr": "B2",
     "labels": {
      "figurative": true
     }
    }
   ],
   "relations": [
    {
     "type": "abstract_noun_of",
     "lemma": "doente",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```

### 19. normalmente (adverb)

```json
{
 "language": "pt",
 "headword": "normalmente",
 "lexemes": [
  {
   "lemma": "normalmente",
   "pos": "ADV",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/noɾ.ˌmaw.ˈmẽ.tʃi/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/nɔɾ.ˌmal.ˈmẽ.tɨ/"
    }
   ],
   "senses": [
    {
     "definition": "na maior parte das vezes; de costume, em geral",
     "example": "Normalmente acordo às sete, mas hoje dormi até mais tarde.",
     "cefr": "A2",
     "antonyms": [
      {
       "lemma": "raramente",
       "pos": "ADV"
      }
     ]
    },
    {
     "definition": "de maneira comum, sem problemas nem alterações",
     "example": "Depois do acidente, o trânsito voltou a circular normalmente na avenida.",
     "cefr": "B1",
     "antonyms": [
      {
       "lemma": "anormalmente",
       "pos": "ADV"
      }
     ]
    }
   ],
   "relations": [
    {
     "type": "adverb_of",
     "lemma": "normal",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```

### 20. rapidamente (adverb)

```json
{
 "language": "pt",
 "headword": "rapidamente",
 "lexemes": [
  {
   "lemma": "rapidamente",
   "pos": "ADV",
   "kind": "word",
   "pronunciations": [
    {
     "variety": "pt-BR",
     "ipa": "/ˌʁa.pi.daˈmẽ.tʃi/"
    },
    {
     "variety": "pt-PT",
     "ipa": "/ˌʁa.pi.dɐˈmẽ.tɨ/"
    }
   ],
   "senses": [
    {
     "definition": "com grande velocidade, sem demorar muito tempo",
     "example": "A notícia espalhou-se rapidamente por toda a cidade.",
     "cefr": "A2",
     "antonyms": [
      {
       "lemma": "lentamente",
       "pos": "ADV"
      }
     ]
    },
    {
     "definition": "durante pouco tempo, de forma breve e sem entrar em detalhes",
     "example": "Antes de começarmos, quero falar rapidamente sobre o horário.",
     "cefr": "B1"
    }
   ],
   "relations": [
    {
     "type": "adverb_of",
     "lemma": "rápido",
     "pos": "ADJ"
    }
   ]
  }
 ]
}
```
