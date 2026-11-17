# English dictionary candidates — strict review

Reviewed against the supplied house rules, with Cambridge, Oxford, Collins and other primary dictionary sources used for disputed distinctions. This is an editorial recommendation about examples for the writing model, not a claim that rejected headwords are invalid English.

**Result:** 1 use as it is; 16 use with corrections; 3 do not use.

## How the rules were applied

Every original definition is 6–15 words and every original example is 6–14 words under a whitespace count, treating contractions and hyphenated compounds as single words. There are no original length violations. The problems are coverage, semantics, grammar, pronunciation formatting and a few other style constraints.

Learner dictionaries do not always use identical sense divisions. A defensible, attested split is sufficient; missing senses and unnecessary splits are not invented just to fill a review checklist. The corrected sense order is an editorial estimate of ordinary use, not a measured frequency ranking. CEFR values are estimates: a one-step difference from a reference is not automatically an error.

Pronunciations are normalized to the requested symbol inventory and monosyllable-stress convention. Where references differ, ordinary alternatives are retained without unsupported older/newer or regional notes. The ordering of institute and hall alternatives follows a reference-dictionary presentation choice; the available evidence does not establish population-wide frequency rankings.

The brief requires compound components but does not supply their exact JSON sub-schema. For airport, this review uses components with text, lemma and inflection: "fixed". Map that last key to the actual production schema if it has another name; no such schema was supplied. All other corrected entries retain the original entry/lexeme/sense structure.

## Recommended five, best first

| Rank | Headword | Version | Coverage |
| --- | --- | --- | --- |
| 1 | lake | unchanged | Plain countable noun with one clear everyday sense. |
| 2 | afford | corrected | Three useful verb readings, complement frames and a formal sense. |
| 3 | aware | corrected | Adjective complements and a position restriction limited to one sense. |
| 4 | institute | corrected | Noun and verb lexemes, pronunciation alternatives and formal usage. |
| 5 | hall | corrected | Real polysemy and careful regional tendencies without false exclusivity. |

Only use the corrected JSON below for entries marked corrected. If only four examples are wanted, omit hall; afford and institute already supply clearly labelled formal senses.

## Verdicts at a glance

| # | Candidate | Verdict | Reason |
| --- | --- | --- | --- |
| 1 | hill | use with corrections | The two senses are defensible, but valley is not a dependable lexical antonym. |
| 2 | lake | use as it is | A clear ordinary noun with a complete everyday sense and a natural example. |
| 3 | owner | use with corrections | The definition should include organizations as well as individual people. |
| 4 | airport | use with corrections | The entry omits the compound components explicitly required by the brief. |
| 5 | consider | use with corrections | Transitive-only grammar misses intransitive use, and the feelings/needs sense is absent. |
| 6 | allow | use with corrections | The formal accept/admit sense and the allow doing sth construction are missing. |
| 7 | afford | use with corrections | The first sense omits time, and the formal example needs the afford sth frame. |
| 8 | write | do not use | It needs substantial sense reconstruction and incorrectly teaches read as a clear antonym. |
| 9 | aware | use with corrections | The sense split is valid, but syllable dots and the no-proper-names rule need attention. |
| 10 | eastern | use with corrections | The Asian definition includes unnecessary country examples; position and capitalization need sense-level treatment. |
| 11 | adult | use with corrections | The adjective is not restricted to attributive use, and the current informal verb is missing. |
| 12 | institute | use with corrections | The noun is too narrowly educational, and common pronunciation alternatives are omitted. |
| 13 | senate | use with corrections | The political definition wrongly requires election; the university definition overstates its powers. |
| 14 | mid | do not use | The example confuses the free adjective with mid-, and the slang position restriction is false. |
| 15 | hall | use with corrections | The entrance and corridor readings are conflated, the country-house sense is missing, and UK-only is too strong. |
| 16 | johnson | do not use | The meaning is real, but humorous and exclusively US are overconfident labels for an exemplar. |
| 17 | criminal | use with corrections | Separate illegal conduct from the legal-system adjective, and remove the blanket attributive restriction. |
| 18 | royal | use with corrections | The informal intensifier meaning in a royal pain is missing. |
| 19 | perhaps | use with corrections | The approximate-quantity use and a common British pronunciation are missing; definitely is not a clear opposite. |
| 20 | instead | use with corrections | The IPA lacks syllable boundaries, and instead of belongs to a separate multiword preposition entry. |

## Individual reviews and complete corrected entries

### 1. hill

**use with corrections** — The two senses are defensible, but valley is not a dependable lexical antonym.

The road-slope sense is independently recorded in learner dictionaries; it is not an invented extra sense. No common in-scope sense is missing. Remove the antonym rather than teach a contrast between two different landforms as a clear opposite. Definitions and examples pass the length and style checks.

Sources: [Cambridge: hill](https://dictionary.cambridge.org/dictionary/english/hill).

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

### 2. lake

**use as it is** — A clear ordinary noun with a complete everyday sense and a natural example.

No common in-scope sense is missing or unnecessarily split. The specialist pigment meaning does not belong in this ordinary learner entry. Countability and IPA are correct. The two locative phrases are ordinary usage aids rather than obligatory complements, but they are acceptable under the stated broad frame convention. The A1 estimate is within the permitted tolerance.

### 3. owner

**use with corrections** — The definition should include organizations as well as individual people.

An organization can own a building or business; this belongs in the existing sense, not a new one. No other common sense or redundant split needs to be added. The relation to own, pronunciation, frame and example are sound.

Sources: [Cambridge: owner](https://dictionary.cambridge.org/dictionary/english/owner).

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
          "definition": "a person or organization that something legally belongs to",
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

### 4. airport

**use with corrections** — The entry omits the compound components explicitly required by the brief.

Add air + port, both fixed components, while retaining kind word. The one ordinary sense is sufficient; no separate plant, activity or transport-subtype sense is warranted. The definition, example, level and pronunciations are acceptable. The schema convention for component inflection is explained at the beginning of this review.

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
      ],
      "components": [
        {
          "text": "air",
          "lemma": "air",
          "inflection": "fixed"
        },
        {
          "text": "port",
          "lemma": "port",
          "inflection": "fixed"
        }
      ]
    }
  ]
}
```

### 5. consider

**use with corrections** — Transitive-only grammar misses intransitive use, and the feelings/needs sense is absent.

An absolute use such as “I need time to consider” requires Both. Add caring about or respecting other people and their needs; it is a recognized learner-dictionary distinction. Thinking before deciding and taking a fact into account may overlap, but separating them is also established practice, so they need not be merged. Explicitly show the adjective complement. B2 for opinion and C1 for taking facts into account are calibration choices, not claims that the original adjacent levels violate the brief.

Sources: [Cambridge: consider](https://dictionary.cambridge.org/dictionary/english/consider).

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
        "valency": "Both",
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
            "consider doing sth",
            "consider how/what ..."
          ]
        },
        {
          "definition": "to have a particular opinion about someone or something",
          "example": "Many people consider him the best player of his generation.",
          "cefr": "B2",
          "frames": [
            "consider sb/sth (to be) sth",
            "consider sb/sth (to be) + adjective",
            "consider that ..."
          ],
          "grammar": {
            "valency": "Tr"
          }
        },
        {
          "definition": "to think about a particular fact when judging or planning something",
          "example": "You also have to consider the cost of getting there.",
          "cefr": "C1",
          "frames": [
            "consider sth",
            "consider how/what ..."
          ],
          "grammar": {
            "valency": "Tr"
          }
        },
        {
          "definition": "to take other people and their feelings or needs into account",
          "example": "Please consider your neighbours when playing music late at night.",
          "cefr": "C1",
          "frames": [
            "consider sb",
            "consider sb's feelings/needs"
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

### 6. allow

**use with corrections** — The formal accept/admit sense and the allow doing sth construction are missing.

Permission, enablement and setting aside resources are defensible distinct senses. Broaden the third definition to include space. Add formal acceptance or admission, including allow that. Do not add allow for or allow of as senses: the brief treats phrasal entries separately. The replacement permission example illustrates an explicitly listed frame. The original A2 estimate is only one step below Cambridge B1 and is not a decisive error.

Sources: [Oxford: allow](https://www.oxfordlearnersdictionaries.com/definition/english/allow).

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
          "example": "My landlord doesn't allow me to keep pets.",
          "cefr": "A2",
          "frames": [
            "allow sth/doing sth",
            "allow sb to do sth",
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
          "definition": "to make something possible or enable someone to do something",
          "example": "The new bridge allows people to cross the river safely.",
          "cefr": "B1",
          "frames": [
            "allow sb/sth to do sth",
            "allow sth"
          ]
        },
        {
          "definition": "to keep enough time, money or space available for something",
          "example": "You should allow about two hours for the journey.",
          "cefr": "B2",
          "frames": [
            "allow sth for sth",
            "allow sb sth"
          ]
        },
        {
          "definition": "to accept something as true, correct or reasonable",
          "example": "Even our critics allow that the plan has advantages.",
          "cefr": "C1",
          "frames": [
            "allow that ...",
            "allow sth",
            "allow sb sth"
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

### 7. afford

**use with corrections** — The first sense omits time, and the formal example needs the afford sth frame.

Money and time belong in the same resource-availability sense. The adverse-consequences meaning and formal provision meaning are useful separate readings; no extra fourth meaning is needed. Add syllable dots to both IPA strings. Replace the illustrative “such as” list in the formal definition with a general phrase, and include both one-object and two-object frames.

Sources: [Cambridge: afford](https://dictionary.cambridge.org/dictionary/english/afford).

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
          "ipa": "/ə.ˈfɔːɹd/"
        },
        {
          "variety": "en-GB",
          "ipa": "/ə.ˈfɔːd/"
        }
      ],
      "senses": [
        {
          "definition": "to have enough money or time to buy or do something",
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
          "definition": "to provide someone with something useful or pleasant",
          "example": "The top floor affords a wonderful view of the sea.",
          "cefr": "C1",
          "frames": [
            "afford sth",
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

### 8. write

**do not use** — It needs substantial sense reconstruction and incorrectly teaches read as a clear antonym.

Keep the irregular forms: wrote and written are correct, including their features and house-style IPA. However, the composition definition makes music and programs seem to be text; learner dictionaries also distinguish stating information in writing, completing documents such as prescriptions, and a pen functioning. Those are ordinary uses, not merely separate subject areas. The first three senses themselves are legitimate. The computing data-storage sense can be excluded by the technical-sense rule. Read is an associated/complementary activity, not a reliable lexical opposite. This is a poor compact exemplar until rebuilt. No corrected JSON is supplied for a do not use verdict.

Sources: [Oxford: write](https://www.oxfordlearnersdictionaries.com/definition/english/write).

### 9. aware

**use with corrections** — The sense split is valid, but syllable dots and the no-proper-names rule need attention.

Add the missing IPA boundaries and replace Sundays in the example. The informed-and-concerned meaning is separately numbered by Oxford, not merely an unnecessary shade of knowing. Pred belongs only on the first sense: environmentally aware consumers is possible. The second sense benefits from its adverb-modifier note. No common additional sense is missing.

Sources: [Oxford: aware](https://www.oxfordlearnersdictionaries.com/definition/english/aware).

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
          "ipa": "/ə.ˈwɛɹ/"
        },
        {
          "variety": "en-GB",
          "ipa": "/ə.ˈweə/"
        }
      ],
      "senses": [
        {
          "definition": "knowing that something exists, is true or is happening",
          "example": "Were you aware that the shop closes early today?",
          "cefr": "B1",
          "frames": [
            "aware of sth",
            "aware that ..."
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
            "politically/socially/environmentally aware"
          ],
          "grammar": {
            "note": "usually modified by an adverb"
          }
        }
      ]
    }
  ]
}
```

### 10. eastern

**use with corrections** — The Asian definition includes unnecessary country examples; position and capitalization need sense-level treatment.

The geographical definition, including facing the direction, is defensible: Oxford explicitly includes facing east. Retain it. The Asian cultural sense is separately recognized; replace the illustrative China/Japan list with a general definition and add its capitalization note. Put Attr on the geographical sense instead of forcing it onto every cultural use. No extra sense is needed for every part of the world; former Eastern-bloc uses are contextual geographical/political extensions here. Asia names the referent, so it is not a prohibited regional-usage label inside a definition.

Sources: [Cambridge Learner: eastern](https://dictionary.cambridge.org/dictionary/learner-english/eastern-eastern); [Oxford: eastern](https://www.oxfordlearnersdictionaries.com/definition/english/eastern).

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
        "comparable": false
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
          ],
          "grammar": {
            "position": "Attr"
          }
        },
        {
          "definition": "connected with the countries and cultures of Asia",
          "example": "Many people in the city now practise Eastern forms of meditation.",
          "cefr": "B1",
          "antonyms": [
            {
              "lemma": "western",
              "pos": "ADJ"
            }
          ],
          "grammar": {
            "note": "usually written Eastern"
          }
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

### 11. adult

**use with corrections** — The adjective is not restricted to attributive use, and the current informal verb is missing.

The human and nonhuman noun senses are a defensible learner-dictionary split; do not merge them simply because both involve maturity. Maturity is more accurate than an absolute full-size threshold. The adjective can be predicative in fully adult. Broaden the sexual-content sense so it does not require explicit depiction of sexual acts. Add syllable dots to the unstressed-first pronunciation. Contemporary dictionaries also record the informal verb about handling grown-up responsibilities, so a complete current entry has three parts of speech. The two stress patterns are pronunciation alternatives of the same lexeme, not different meanings or extra lexemes.

Sources: [Oxford: adult noun](https://www.oxfordlearnersdictionaries.com/definition/english/adult_1); [Cambridge: adult adjective](https://dictionary.cambridge.org/dictionary/english/adult); [Dictionary.com: adult verb](https://www.dictionary.com/browse/adult).

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
          "ipa": "/ə.ˈdʌlt/"
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
          "ipa": "/ə.ˈdʌlt/"
        }
      ],
      "senses": [
        {
          "definition": "a person who is fully grown or legally treated as mature",
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
          "definition": "an animal or plant that has reached maturity",
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
          "ipa": "/ə.ˈdʌlt/"
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
          "ipa": "/ə.ˈdʌlt/"
        }
      ],
      "senses": [
        {
          "definition": "fully developed, or involving people who are no longer children",
          "example": "She spent most of her adult life working abroad.",
          "cefr": "B1"
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
          "definition": "intended only for grown-ups because of sexual content",
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
    },
    {
      "lemma": "adult",
      "pos": "VERB",
      "kind": "word",
      "grammar": {
        "valency": "Intr",
        "irregular": false
      },
      "pronunciations": [
        {
          "variety": "en-US",
          "ipa": "/ə.ˈdʌlt/"
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
          "ipa": "/ə.ˈdʌlt/"
        }
      ],
      "senses": [
        {
          "definition": "to handle everyday responsibilities usually expected of a grown-up person",
          "example": "I paid all my bills, so I've adulted enough today.",
          "cefr": "C1",
          "labels": {
            "formality": "informal"
          }
        }
      ]
    }
  ]
}
```

### 12. institute

**use with corrections** — The noun is too narrowly educational, and common pronunciation alternatives are omitted.

Professional bodies also qualify; broaden the noun within its existing sense. Organization and its buildings need not become separate numbered senses. The verb definition, formal label and frames are good. Retain the original Oxford-style pronunciations as alternatives and add Cambridge’s US schwa and GB affricated pronunciations. The B2 noun level is an optional calibration from B1, not a greater-than-one-step correction. The confusable relation is pedagogically useful and can stay.

Sources: [Oxford: institute](https://www.oxfordlearnersdictionaries.com/definition/english/institute_1); [Cambridge: institute pronunciation](https://dictionary.cambridge.org/pronunciation/english/institute).

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
          "ipa": "/ˈɪn.stə.tuːt/"
        },
        {
          "variety": "en-US",
          "ipa": "/ˈɪn.stɪ.tuːt/"
        },
        {
          "variety": "en-GB",
          "ipa": "/ˈɪn.stɪ.tʃuːt/"
        },
        {
          "variety": "en-GB",
          "ipa": "/ˈɪn.stɪ.tjuːt/"
        }
      ],
      "senses": [
        {
          "definition": "an organization for specialist study, professional work or research, or its buildings",
          "example": "She works at a research institute for marine biology.",
          "cefr": "B2"
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
        "valency": "Tr",
        "irregular": false
      },
      "pronunciations": [
        {
          "variety": "en-US",
          "ipa": "/ˈɪn.stə.tuːt/"
        },
        {
          "variety": "en-US",
          "ipa": "/ˈɪn.stɪ.tuːt/"
        },
        {
          "variety": "en-GB",
          "ipa": "/ˈɪn.stɪ.tʃuːt/"
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

### 13. senate

**use with corrections** — The political definition wrongly requires election; the university definition overstates its powers.

Not all senators are elected: Canada supplies a straightforward counterexample. Define the legislative role without requiring a particular selection method or a universally smaller membership. Keep legislative, university and ancient-Roman councils separate; none is merely a subject-area split of an identical institution. Do not add a separate sense for every country or state. University senates can be countable or uncountable in usage, and their academic responsibilities do not imply complete control of a university. Keep historical on the Roman sense; remove politics as an unnecessary specialist-domain label for the ordinary news meaning. Use lowercase senate in the generic example to avoid a named institution.

Sources: [Canada: appointed Senate](https://www.canada.ca/en/campaign/independent-advisory-board-for-senate-appointments/about-the-senate.html); [Oxford: senate](https://www.oxfordlearnersdictionaries.com/definition/english/senate).

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
          "definition": "the upper of two groups of lawmakers in some national or state governments",
          "example": "The senate approved the bill after a long debate.",
          "cefr": "B2"
        },
        {
          "definition": "a university council responsible for academic rules and decisions",
          "example": "Our university senate voted to change the exam rules.",
          "cefr": "C1",
          "grammar": {
            "countability": "Both"
          }
        },
        {
          "definition": "a governing council in ancient Rome",
          "example": "The emperor often ignored the advice of the senate.",
          "cefr": "C1",
          "labels": {
            "time": "historical"
          }
        }
      ]
    }
  ]
}
```

### 14. mid

**do not use** — The example confuses the free adjective with mid-, and the slang position restriction is false.

Mid August contains a month name and illustrates a construction normally written mid-August, which is a poor free-adjective exemplar. The free central adjective, slang adjective and old-fashioned literary preposition are all real; do not reject the headword or pretend they are one sense. Slang mid is also attributive, as in a mid album, so Pred is wrong. The preposition needs a time label as well as literary style. The spelling records omit the varieties required by the brief. A phonetic mid-vowel sense is specialist and may legitimately be omitted. These interacting issues make this a poor teaching example despite the legitimate senses.

Sources: [Cambridge: mid](https://dictionary.cambridge.org/dictionary/english/mid).

### 15. hall

**use with corrections** — The entrance and corridor readings are conflated, the country-house sense is missing, and UK-only is too strong.

Separate entrance space and corridor, as Cambridge and other learner dictionaries do. Keep the public-room sense and add the country-house meaning found in ordinary prose. Student accommodation is not exclusively British: bare in hall(s) is especially British, but the residence meaning also occurs in North American English. Attach the regional information to that construction through a note rather than excluding the whole sense elsewhere. Remove hall of residence as a supposed frame: it is a separate multiword noun under the entry policy. Include the common US merged-vowel pronunciation as well as the original pronunciation. Regional mainly labels express tendencies, not claims that speakers elsewhere never use these senses.

Sources: [Cambridge: hall](https://dictionary.cambridge.org/dictionary/english/hall); [Oxford: hall](https://www.oxfordlearnersdictionaries.com/definition/english/hall); [Collins: hall](https://www.collinsdictionary.com/dictionary/english/hall).

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
          "ipa": "/ˈhɑːl/"
        },
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
          "definition": "the space inside a building entrance that leads to other rooms",
          "example": "You can leave your wet boots in the hall.",
          "cefr": "A2",
          "regions": [
            {
              "variety": "en-GB",
              "extent": "mainly"
            }
          ]
        },
        {
          "definition": "a passage inside a building with rooms opening onto it",
          "example": "Our classroom is at the far end of the hall.",
          "cefr": "A2",
          "regions": [
            {
              "variety": "en-021",
              "extent": "mainly"
            }
          ]
        },
        {
          "definition": "a large room or building used for public events or shared activities",
          "example": "Hundreds of people filled the concert hall last night.",
          "cefr": "A2"
        },
        {
          "definition": "a building where university or college students live",
          "example": "Most first-year students live in halls near the campus.",
          "cefr": "B2",
          "frames": [
            "in hall(s)"
          ],
          "grammar": {
            "note": "bare hall(s) is especially British"
          }
        },
        {
          "definition": "a large house in the countryside",
          "example": "They live in an old hall surrounded by fields.",
          "cefr": "C1",
          "grammar": {
            "note": "often part of a house name"
          },
          "regions": [
            {
              "variety": "en-GB",
              "extent": "mainly"
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

### 16. johnson

**do not use** — The meaning is real, but humorous and exclusively US are overconfident labels for an exemplar.

The vulgar slang noun is attested and is not rejected merely because it concerns anatomy. The example is natural, and its definition and example satisfy the length rules. Humor is contextual rather than required by the meaning; the joke example does not prove a lexical humorous label. A US source documenting a word also does not prove geographical exclusivity. No common additional lowercase sense or redundant sense split is evident. A more securely labelled entry is preferable for teaching the model how labels work.

Sources: [American Heritage: johnson](https://www.ahdictionary.com/word/search.html?q=johnson).

### 17. criminal

**use with corrections** — Separate illegal conduct from the legal-system adjective, and remove the blanket attributive restriction.

Criminal acts and criminal courts are not interchangeable meanings: the courts need not be breaking the law. Oxford numbers them separately. The illegal-conduct adjective can be predicative, while the legal-system sense is attributive. Retain the independent figurative disapproval sense and the ordinary noun. No further everyday sense needs adding, and the formal/legal topic of some collocations does not require labelling the entire adjective lexeme as specialist law.

Sources: [Oxford: criminal adjective](https://www.oxfordlearnersdictionaries.com/definition/english/criminal_2).

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
          "definition": "involving actions that are against the law",
          "example": "What they did was criminal, even without any violence.",
          "cefr": "B1",
          "grammar": {
            "note": "usually before a noun"
          }
        },
        {
          "definition": "connected with laws, courts or proceedings concerning illegal acts",
          "example": "She works as a criminal lawyer in the city.",
          "cefr": "B2",
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

### 18. royal

**use with corrections** — The informal intensifier meaning in a royal pain is missing.

The impressive-welcome sense is a recognized extension and should remain. Add the negative intensifier as a separate sense, with its informal label, attributive grammar and mainly US regional marking. Royal patronage and official organizations can remain covered by the broad connection-to-monarchy sense; the naming convention does not require another sense here. The original non-comparable label is defensible for ordinary dictionary treatment and is retained. The noun exists in the singular, so often plural is appropriate but number Plur would not be. Technical sail, paper and coin senses are excluded by scope.

Sources: [Cambridge: royal](https://dictionary.cambridge.org/dictionary/english/royal); [Collins: royal distinctions](https://api.collinsdictionary.com/dictionary/english-thesaurus/royal).

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
        },
        {
          "definition": "very great or extreme, especially in an unpleasant way",
          "example": "Sorting out those mistakes has been a royal pain.",
          "cefr": "C1",
          "grammar": {
            "position": "Attr"
          },
          "labels": {
            "formality": "informal",
            "figurative": true
          },
          "regions": [
            {
              "variety": "en-US",
              "extent": "mainly"
            }
          ]
        }
      ]
    },
    {
      "lemma": "royal",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {
        "countability": "Count",
        "note": "often plural"
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

### 19. perhaps

**use with corrections** — The approximate-quantity use and a common British pronunciation are missing; definitely is not a clear opposite.

Add the approximate-number use, which Cambridge distinguishes. Keep the politeness use: it is an established pragmatic function in learner dictionaries. Reluctant agreement can remain a contextual use of uncertainty in this compact entry rather than an obligatory fourth sense. Definitely expresses a different degree of commitment, not a clean logical antonym of perhaps. Add syllable dots and the attested British casual /præps/ pronunciation in the required symbol system. Used to is not one of the banned definition formulas, and these definitions remain phrases.

Sources: [Cambridge: perhaps](https://dictionary.cambridge.org/dictionary/english/perhaps?topic=polite-expressions); [Oxford: perhaps](https://www.oxfordlearnersdictionaries.com/definition/english/perhaps).

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
          "ipa": "/pɚ.ˈhæps/"
        },
        {
          "variety": "en-GB",
          "ipa": "/pə.ˈhæps/"
        },
        {
          "variety": "en-GB",
          "ipa": "/ˈpɹæps/",
          "note": "casual"
        }
      ],
      "senses": [
        {
          "definition": "used to say that something is possible but not certain",
          "example": "Perhaps the train is late because of the snow.",
          "cefr": "A2"
        },
        {
          "definition": "used to give a rough number, amount or measurement",
          "example": "The walk takes perhaps twenty minutes at a steady pace.",
          "cefr": "B1"
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

### 20. instead

**use with corrections** — The IPA lacks syllable boundaries, and instead of belongs to a separate multiword preposition entry.

Keep the single replacement sense and its natural example. The of-constructions are not additional senses of the standalone adverb, and their separate prepositional entry should own those frames under the supplied policy. Add the missing IPA dots. No additional common sense, irregular form or opposite needs inventing.

Sources: [Cambridge: instead of](https://dictionary.cambridge.org/dictionary/english/instead-of).

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
          "ipa": "/ɪn.ˈstɛd/"
        },
        {
          "variety": "en-GB",
          "ipa": "/ɪn.ˈsted/"
        }
      ],
      "senses": [
        {
          "definition": "in the place of someone or something else; as a different choice",
          "example": "There was no rice left, so we had pasta instead.",
          "cefr": "A2"
        }
      ]
    }
  ]
}
```
