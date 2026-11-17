# German dictionary candidate review

The five I would show, best first, are **nötig, Nachricht, fällen, perfekt, Trainer**. Use **nötig unchanged** and the **corrected JSON below** for the other four.

This review covers all 20 candidates. “Do not use” means exclude the candidate from the demonstration set, not reject the German headword as invalid. Each “use with corrections” verdict is followed by a complete replacement entry in the original object shape.

All original definitions and examples pass a whitespace-based word-count check: definitions 6–15 words, examples 6–14. That does not guarantee compliance with the one-sentence or phrase-style rules. In particular, the *sterben* hunger example contains two sentences.

Sense inventories are editorial analyses, not unique facts: monolingual dictionaries sometimes number the same uses differently. I distinguish clear inaccuracies from the compact grouping recommended for this house style. CEFR values are estimates, not claims of certification; no original level is demonstrably wrong by more than one step. The ranking and sense ordering are editorial judgments, not corpus frequency measurements. Definitions and newly written examples below are original wording; links identify the dictionary evidence used for factual checks.

Two specification issues should be resolved before these rules are scaled: the allowed plural classes omit umlaut-only plurals such as *Schaden → Schäden*, and “phonemic IPA” is being used for a house transcription that also prescribes surface features such as vocalized r. I follow the given transcription conventions without treating their notation as a universal IPA standard. Clarify separately whether predictable weak-verb forms must be listed: the brief both requests preterites/participles and says regular forms are generated later. Here I retain them, following the explicit verb-form requirement and all supplied candidates.

| # | Candidate | Verdict |
|---|---|---|
| 1 | Gespräch | **use with corrections** |
| 2 | Nachricht | **use with corrections** |
| 3 | Richter | **use with corrections** |
| 4 | Hoffnung | **use with corrections** |
| 5 | sterben | **do not use** |
| 6 | fällen | **use with corrections** |
| 7 | verkaufen | **do not use** |
| 8 | regeln | **use with corrections** |
| 9 | interessant | **use with corrections** |
| 10 | nötig | **use as it is** |
| 11 | schaden | **do not use** |
| 12 | perfekt | **use with corrections** |
| 13 | Tor | **use with corrections** |
| 14 | irgendwo | **use with corrections** |
| 15 | Trainer | **use with corrections** |
| 16 | Kreis | **do not use** |
| 17 | Auge | **do not use** |
| 18 | Bier | **do not use** |
| 19 | stets | **use as it is** |
| 20 | möglichst | **use with corrections** |

## 1. Gespräch

**use with corrections** — The telephone-call sense is missing, while the negotiation sense is an avoidable split in this compact entry.

**Facts and senses:** Add the normal genitive variant *Gespräches*. PONS separates ordinary conversation from a telephone call; its general conversation sense can cover negotiations. The original negotiation meaning is real, not an invented fact, but I would merge it here rather than make the nature of the participants a new sense. Add the missing call meaning instead.

**Style:** Both original definitions and examples satisfy the word limits. The new call example makes the telephone context visible without using a proper name. No major CEFR error.

Source: [PONS, Gespräch](https://de.pons.com/%C3%BCbersetzung/deutsche-rechtschreibung/Gespr%C3%A4ch).

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
          "form": "Gespräches",
          "features": {
            "Case": "Gen",
            "Number": "Sing"
          },
          "ipa": {
            "de-DE": "/ɡə.ˈʃpʁɛː.çəs/"
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
          "definition": "Verbindung über das Telefon, bei der Menschen miteinander reden",
          "example": "Am Handy kann ich das zweite Gespräch gerade nicht annehmen.",
          "cefr": "A2",
          "frames": [
            "ein Gespräch annehmen",
            "ein Gespräch unterbrechen"
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

## 2. Nachricht

**use with corrections** — Keep the two senses, but remove the unnecessary restriction to short messages and broaden the broadcast definition.

**Facts and senses:** The message/information sense is not necessarily short. It includes news about a person or event, not just a short message sent to someone. Two senses are sufficient: individual information/message and plural-only news programming. Individual news reports belong under the first sense; a third sense is unnecessary. The second definition should not exclude an online news broadcast.

**Style:** Forms, IPA, frames, examples, and plural-only marking are sound. The revised message example directly illustrates the recipient and topic frames. A1 for a simple message and A2 for news programming are reasonable estimates.

Source: [Duden, Nachricht](https://www.duden.de/rechtschreibung/Nachricht).

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
          "definition": "Mitteilung, die jemandem Informationen über eine Person oder Sache gibt",
          "example": "Ich habe dir gestern eine Nachricht über den Termin geschickt.",
          "cefr": "A1",
          "frames": [
            "jmdm. eine Nachricht schicken",
            "Nachricht von + Dat",
            "Nachricht über + Akk"
          ]
        },
        {
          "definition": "Sendung mit Berichten über die wichtigsten aktuellen Ereignisse",
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

## 3. Richter

**use with corrections** — The definition is too focused on guilt, and the ordinary figurative judging-person use is missing.

**Facts and senses:** Judges also decide civil disputes; deciding guilt is not a necessary part of the role. PONS separately records the elevated figurative person who judges others. Add that use with its construction and labels. There is no redundant original sense. The biblical book name is outside this ordinary learner entry.

**Style:** The original example, inflections, pronunciation, and agent-noun relation are fine. The revised legal definition stays simple without implying that every judge conducts criminal proceedings. B1 is suitable for the main sense; C1 is a reasonable estimate for the elevated extension.

Sources: [Duden, Richter](https://www.duden.de/rechtschreibung/Richter); [PONS, Richter](https://de.pons.com/%C3%BCbersetzung/deutsche-rechtschreibung/Richter).

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
          "definition": "Person, die vor Gericht über Streitfälle oder Straftaten entscheidet",
          "example": "Morgen muss sie vor dem Richter als Zeugin aussagen.",
          "cefr": "B1"
        },
        {
          "definition": "Person, die das Verhalten anderer bewertet und darüber urteilt",
          "example": "Mach dich bitte nicht zum Richter über unser Leben.",
          "cefr": "C1",
          "frames": [
            "sich zum Richter über + Akk machen"
          ],
          "labels": {
            "figurative": true,
            "style": [
              "elevated"
            ]
          }
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

## 4. Hoffnung

**use with corrections** — The two senses work, but the person-or-thing sense needs a figurative label and the frames can be more informative.

**Facts and senses:** Keep positive expectation and the person or thing on which expectations rest. Do not add separate senses for feeling hopeful and having a particular hope; these fit the first definition, with countability already set to Both. Mark the second sense figurative, as the house rules require. No major sense is missing at this scope.

**Style:** The original definitions are acceptable. I make the first slightly broader and show the common *dass* complement, while retaining *auf + Akk* and the useful reflexive construction. No factual error is established in the original plural pronunciation; the syllable dots should not be confused with orthographic hyphenation.

Sources: [PONS, Hoffnung](https://en.pons.com/translate/dictionary-of-german-spelling/Hoffnung); [Duden, Hoffnung](https://www.duden.de/rechtschreibung/Hoffnung).

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
          "definition": "positive Erwartung, dass etwas Gutes geschieht oder eine schwierige Lage besser wird",
          "example": "Wir haben die Hoffnung auf besseres Wetter noch nicht aufgegeben.",
          "cefr": "A2",
          "frames": [
            "Hoffnung auf + Akk",
            "die Hoffnung, dass …",
            "sich Hoffnungen auf + Akk machen"
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
          },
          "labels": {
            "figurative": true
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

## 5. sterben

**do not use** — The definition/example rules are broken, and the treatment of figurative constructions and the lexicalized noun needs reconsideration.

**Facts:** The strong forms, *sein* auxiliary, and intransitive analysis are correct. In particular, the short vowel in *starb* /ˈʃtaʁp/ is not an error. *Für jemanden/etwas sterben* is valid. The cognate-accusative frame *einen … Tod sterben* is useful and absent; it does not by itself require reclassifying the verb as ordinarily transitive.

**Senses:** Dictionaries differ in how they group the figurative and hyperbolic uses. Do not teach “slow disappearance” and every *vor + Dat* exaggeration as automatically separate meanings. PONS also gives the lexicalized noun *das Sterben*; under the file’s capitalization rule, that deserves consideration in the same headword response. A complete exemplar needs a deliberate policy here.

**Style:** *Zum Beispiel* introduces examples inside the first definition, which is explicitly prohibited. The hunger example contains two sentences, although its total word count passes. “A feeling like hunger” is also an imprecise classification. These are sufficient reasons to exclude this candidate from the demonstration set.

Sources: [Duden, sterben](https://www.duden.de/rechtschreibung/sterben); [PONS, sterben](https://de.pons.com/%C3%BCbersetzung/deutsche-rechtschreibung/sterben); [PONS, starb](https://de.pons.com/%C3%BCbersetzung/deutsche-rechtschreibung/starb).

## 6. fällen

**use with corrections** — Repair the participle’s IPA formatting and remove the claim that every decision must be officially announced.

**Facts and senses:** The two common senses are appropriate. A tree may be cut down with an axe as well as a saw. More importantly, *eine Entscheidung fällen* need not include public announcement; the original adds an entailment the word does not reliably have. Retain one decision/judgment sense rather than splitting the two collocations. The chemistry and military meanings are outside this general learner scope.

**Style:** Add the syllable boundary in /ɡə.ˈfɛlt/ and a useful tree-object frame. The weak paradigm is correct: *fällt* does not undergo a present-tense vowel change relative to *fällen*, so no extra irregular present form is needed. The warning about confusion with *gefallen* is useful. I do not impose an elevated label on ordinary *eine Entscheidung fällen*, where dictionaries differ.

Sources: [Duden, fällen](https://www.duden.de/rechtschreibung/faellen); [PONS, fallen/fällen](https://de.pons.com/%C3%BCbersetzung/deutsche-rechtschreibung/fallen).

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
        "valency": "Tr",
        "irregular": false
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
            "de-DE": "/ɡə.ˈfɛlt/"
          }
        }
      ],
      "senses": [
        {
          "definition": "einen Baum am Stamm absägen oder mit einer Axt umschlagen",
          "example": "Im Winter werden im Wald viele alte Bäume gefällt.",
          "cefr": "B2",
          "frames": [
            "einen Baum fällen"
          ]
        },
        {
          "definition": "nach einer Prüfung oder Überlegung eine verbindliche Entscheidung treffen",
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

## 7. verkaufen

**do not use** — The middle construction is defined as successful selling, and common self-presentation is missing.

**Facts:** *Sich verkaufen* can mean selling badly or not selling, so “von vielen Kunden gern genommen werden” contradicts the supplied *gut/schlecht* frame. The infinitive and both listed forms also omit a required syllable dot before the stressed syllable.

**Senses:** The persuasive-presentation sense should cover presenting oneself well, as in a job interview. This is a missing application of the same sense, not a reason to create another POS or separate lexeme. A full review must also decide how to handle figurative selling oneself for advantage and the prostitution use; PONS records the latter. Regional *sich verkaufen* meaning making a bad purchase should stay excluded under the dialect policy.

**Style:** *Von Waren:* is a usage heading inside the definition and the resulting wording is not a clean verb definition. The six-word example passes the length rule. Rebuilding this entry is possible, but it is a poor choice for a small, reliable demonstration set.

Source: [PONS, verkaufen](https://de.pons.com/%C3%BCbersetzung/deutsche-rechtschreibung/verkaufen).

## 8. regeln

**use with corrections** — Merge overlapping organizing/legal uses, distinguish adjusting a setting, and add the missing reflexive construction.

**Facts and senses:** *Die Sache regelt sich* is an important omission. The original divides settling an affair and prescribing its organization too sharply, while mixing traffic organization with adjusting temperature. The revision groups organizing/settling/prescribing together, keeps adjustment of a quantity or setting, and adds the reflexive use with a sense-level grammar override. That grouping follows Duden’s distinctions while making the construction difference explicit for learners.

**Style:** “Damit alles gleichmäßig läuft” incorrectly makes uniform operation a necessary result. The revised examples identify their own senses more clearly. The original first example is grammatical but repeats *klären* and *regeln* without adding useful context. Inflections and the schwa-based IPA are sound.

Sources: [Duden, regeln](https://www.duden.de/rechtschreibung/regeln); [PONS, regeln](https://de.pons.com/%C3%BCbersetzung/deutsche-rechtschreibung/regeln).

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
            "VerbForm": "Fin",
            "Mood": "Ind"
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
          "definition": "eine Angelegenheit ordnen oder ihren Ablauf verbindlich festlegen",
          "example": "Den Ablauf regeln wir morgen gemeinsam mit der neuen Kollegin.",
          "cefr": "B1",
          "frames": [
            "etw. regeln",
            "etw. mit jmdm. regeln",
            "etw. durch + Akk regeln"
          ]
        },
        {
          "definition": "die Stärke oder Menge von etwas auf einen gewünschten Wert einstellen",
          "example": "Mit diesem Knopf regelst du die Temperatur im ganzen Zimmer.",
          "cefr": "B2",
          "frames": [
            "etw. regeln"
          ]
        },
        {
          "definition": "ohne weiteres Eingreifen in Ordnung kommen oder sich klären",
          "example": "Manchmal regeln sich solche Probleme mit der Zeit von selbst.",
          "cefr": "B1",
          "frames": [
            "etw. regelt sich",
            "sich nach + Dat regeln"
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
          "lemma": "Regel",
          "pos": "NOUN"
        }
      ]
    }
  ]
}
```

## 9. interessant

**use with corrections** — Use a direct adjective phrase and include the common reduced pronunciation.

**Facts and senses:** The attention-provoking and advantageous senses are established; keep both. Duden also records the reduced pronunciation without the schwa, which is added here without an unsupported frequency or register note. No further everyday sense needs to be split out; revealing, attractive, and worth learning about are contextual shades of the first.

**Style:** The original first definition is a dependent “so, dass …” explanation rather than a clean adjective-headed phrase. Replace it with an adjective phrase and show that *für + Akk* also belongs to this sense. The second example and definition work; the revised first example now demonstrates the added frame. The commercial association of the second sense does not make every occurrence specialist terminology.

Source: [Duden, interessant](https://www.duden.de/rechtschreibung/interessant).

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
        },
        {
          "variety": "de-DE",
          "ipa": "/ɪn.tʁɛ.ˈsant/"
        }
      ],
      "senses": [
        {
          "definition": "spannend und geeignet, die Aufmerksamkeit einer Person zu wecken",
          "example": "Dieser Film über Wale ist besonders interessant für unsere Kinder.",
          "cefr": "A1",
          "antonyms": [
            {
              "lemma": "langweilig",
              "pos": "ADJ"
            }
          ],
          "frames": [
            "interessant für + Akk"
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

## 10. nötig

**use as it is** — A clean adjective entry with natural wording, useful frames, and no material error.

**Facts and senses:** Gender and noun forms are irrelevant; the adjective is comparable, and /ˈnøː.tɪç/ is an appropriate de-DE reference pronunciation. The single necessity sense covers the common uses. Being required for an action and being needed by a person are not separate senses. No important missing common meaning is evident.

**Style:** The seven-word definition and nine-word example meet the limits. The example instantiates *nötig für + Akk*. The infinitival and *nötig haben* frames are useful. The antonym and relation to *Not* are sound. No unsupported register label should be added.

Source: [PONS, nötig](https://de.pons.com/%C3%BCbersetzung/deutsche-rechtschreibung/n%C3%B6tig).

## 11. schaden

**do not use** — The noun’s required plural class cannot be expressed with the supplied enum, and its senses are too narrow.

**Facts:** *Schaden → Schäden* is umlaut without an added ending. The supplied value `-¨` describes that pattern but is absent from the permitted list. Replacing it with `-` would falsely say “no change”; replacing it with `-¨e` would also be wrong. This is a schema defect, not something to fix by inventing a permitted value. Add an umlaut-only class upstream before using this candidate.

**Senses:** Damage to health or a person is missing from the “Sache kaputt” definition. Financial damage also occurs countably in the plural, so the blanket Mass restriction on the broad loss sense is misleading. The general detriment and concrete damage uses require more careful boundaries.

**Style:** /ɡəˈʃaː.dət/ lacks the requested dot. The verb’s two frames duplicate one another. Its Intr label is defensible: a dative complement does not make *schaden* an accusative-transitive verb. The lowercase verb and capitalized noun are correctly separate lexemes.

Source: [Duden, Schaden](https://www.duden.de/rechtschreibung/Schaden).

## 12. perfekt

**use with corrections** — Correct the pronunciation policy and register labeling, and make the grammatical definition more precise.

**Facts:** Use final stress for the adjective and the securely documented initial stress for the noun. I remove the noun’s additional final-stress reading because the consulted primary dictionary does not support it; this is a conservative inclusion decision, not a claim that no speaker uses it. Add the adjective’s missing syllable dot. The concluded/settled sense does not require an informal label. *Perfekt machen* is a useful frame.

**Senses:** Keep the two adjective senses. For this compact learner entry, the tense and an occurrence of its verb form can remain one noun sense; Duden and PONS split them in their fuller accounts. That distinction is not a missing ordinary meaning comparable to omitting a phone call under *Gespräch*.

**Style:** Specify the present-tense auxiliary and Partizip II, with “meist” allowing exceptional constructions. Replace the language-name example with an uncomplicated cake example. Remove the empty grammar object rather than guess a blanket comparability policy. No separate adverb lexeme is necessary for ordinary adverbial use of the German adjective.

Sources: [PONS, perfekt/Perfekt](https://de.pons.com/%C3%BCbersetzung/deutsch-englisch/perfekt); [Duden, perfekt](https://www.duden.de/rechtschreibung/perfekt); [Duden, Perfekt](https://www.duden.de/rechtschreibung/Perfekt).

```json
{
  "language": "de",
  "headword": "perfekt",
  "lexemes": [
    {
      "lemma": "perfekt",
      "pos": "ADJ",
      "kind": "word",
      "pronunciations": [
        {
          "variety": "de-DE",
          "ipa": "/pɛʁ.ˈfɛkt/"
        }
      ],
      "senses": [
        {
          "definition": "ohne Fehler und in jeder Hinsicht so gut wie möglich",
          "example": "Nach mehreren Versuchen ist ihr Kuchen diesmal wirklich perfekt geworden.",
          "cefr": "A2",
          "antonyms": [
            {
              "lemma": "fehlerhaft",
              "pos": "ADJ"
            }
          ]
        },
        {
          "definition": "endgültig vereinbart und abgeschlossen, sodass nichts mehr geklärt werden muss",
          "example": "Nach langen Verhandlungen ist der Wechsel des Spielers jetzt perfekt.",
          "cefr": "B2",
          "grammar": {
            "position": "Pred"
          },
          "frames": [
            "etw. ist perfekt",
            "etw. perfekt machen"
          ]
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
          "definition": "Zeitform, meist mit Hilfsverb im Präsens und Partizip II für vergangene Ereignisse",
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

## 13. Tor

**use with corrections** — The gender split is right, but the sports definitions are too narrow and the domain labels exceed the house policy.

**Facts and senses:** Keep the two noun lexemes and their different declensions. A physical goal is not confined to football or necessarily defined by a net; scoring also includes a puck in ice hockey. Broaden the two definitions. The figurative access sense is a reasonable compact learner extension, although it can also be grouped with the literal gate. The slalom use is a specialist omission allowed by this brief.

**Style and labels:** Scoring a goal and a goal frame are everyday vocabulary. Under this file’s unusually strict “domain only for a specialist sense” policy, remove `sport`; this is not a claim that conventional dictionary domain labels are wrong. Keep the literary/dated fool sense. Remove the optional self-referential confusable relation: lemma plus NOUN does not distinguish the target homonym here. The structural gender split already teaches the contrast.

Source: [PONS, Tor](https://de.pons.com/%C3%BCbersetzung/deutsch-fran%C3%A7ais/Tor).

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
          "definition": "Treffer, bei dem Ball oder Scheibe das Ziel regelgerecht erreichen",
          "example": "In der zweiten Halbzeit schoss sie gleich zwei Tore.",
          "cefr": "A2",
          "frames": [
            "ein Tor schießen"
          ]
        },
        {
          "definition": "durch Pfosten begrenztes Ziel, in das Ball oder Scheibe gespielt werden",
          "example": "Der Ball prallt vom Pfosten ab und rollt ins Tor.",
          "cefr": "A2"
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
      ]
    }
  ]
}
```

## 14. irgendwo

**use with corrections** — The two senses and label are sound, but the definitions unnecessarily restrict indefiniteness and the figurative use.

**Facts and senses:** The location need not be unknown or deliberately concealed; it may simply be arbitrary or unspecified. The colloquial abstract sense need not be difficult to explain. Broaden both definitions while keeping the two-sense analysis, the informal/figurative label, and the existing examples. No additional common sense is missing, and no new sense is needed for an indifferent choice of location.

**Style:** IPA, antonym, and the relation to *wo* are acceptable. Both original examples show their intended senses and meet the limits. The changes improve semantic coverage rather than repair grammar.

Source: [Duden, irgendwo](https://www.duden.de/rechtschreibung/irgendwo).

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
          "definition": "an einem unbekannten, beliebigen oder nicht näher genannten Ort",
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
          "definition": "in einer bestimmten Hinsicht oder auf eine unbestimmte Weise",
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

## 15. Trainer

**use with corrections** — Keep the documented Austrian/Swiss clothing sense, but do not define every trainer as preparing athletes for competitions.

**Facts and senses:** Both listed pronunciations, the masculine gender, and the unchanged plural are supported. The personal sense needs to allow ordinary fitness instruction and non-sports instruction; Duden also includes training animals. These applications can be covered by one broad agent definition rather than split merely by subject area. PONS gives professional instruction a separate sense, so this grouping is a conscious house-style choice.

**Regions:** The Austrian tag is not an error: Duden explicitly gives both Austrian and Swiss usage for the tracksuit sense, while PONS lists Swiss. Retain both tags as membership in a regional set, not a claim of universal use throughout both countries.

**Style:** The apparel definition and example already work. The new first example avoids making competition essential. I omit the optional agent-noun relation from this demonstration: the loanword’s relationship to German *trainieren* is useful semantically, but the example need not imply a particular derivational analysis.

Sources: [Duden, Trainer](https://www.duden.de/rechtschreibung/Trainer); [PONS, Trainer](https://de.pons.com/%C3%BCbersetzung/deutsche-rechtschreibung/Trainer).

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
          "definition": "Person, die Menschen oder Tiere durch regelmäßige Übungen ausbildet",
          "example": "Unser Trainer zeigt uns heute Übungen für den Rücken.",
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
      ]
    }
  ]
}
```

## 16. Kreis

**do not use** — The entry underdistinguishes common senses and treats administrative geography as proof of lexical exclusivity.

**Facts and senses:** The first definition gives a geometrical line, but the example illustrates a circular arrangement. PONS distinguishes these; movement in a circular path is also ordinary. The social-group sense includes plural-only *Kreise*, which deserves its own restriction or a clearly broader policy. The supplied “people who know one another” grouping does not reliably cover *aus gut unterrichteten Kreisen*.

**Regions:** Having administrative units with this name in particular countries is not sufficient evidence that speakers elsewhere do not use the word in that meaning. The two `only` tags require a lexical-variety source. The definition is also more specific than the general “administrative district” meaning warrants.

**Style:** Inflections, IPA, word counts, and the examples themselves are acceptable. The difficulty is the meaning/example alignment and sense inventory, not a spelling or grammar mistake. This is not a clean regional teaching example.

Sources: [PONS, Kreis](https://de.pons.com/%C3%BCbersetzung/deutsche-rechtschreibung/Kreis); [Duden, Kreis](https://www.duden.de/rechtschreibung/Kreis).

## 17. Auge

**do not use** — Several familiar extended meanings are absent, and the supplied figurative use does not establish a complete sense inventory.

**Facts and senses:** The body-part meaning and inflections are correct. A die’s individual dot contributes one point; saying that the dot “shows a number” is less precise. Familiar omissions include the growth point on a potato, a fat droplet on soup, and the calm center of a storm. Scoring values in card games extend beyond dots on dice. These occur in ordinary household, game, and news contexts, not only technical dictionaries.

**Sense boundaries:** *Ein Auge für etwas haben* is a useful construction, but whether it receives its own perception/judgment sense is an editorial choice; Duden groups it with the organ entry. Do not expand every eye idiom into a new numbered meaning.

**Style:** Existing examples and word counts are fine. The entry requires a broader reconstruction and an explicit idiom policy before it can model the intended house style.

Source: [Duden, Auge](https://www.duden.de/rechtschreibung/Auge).

## 18. Bier

**do not use** — It promotes a predictable serving use and a fixed idiom into independent senses despite the brief’s compact sense policy.

**Facts and senses:** The beverage, gender, pronunciation, and genitives are sound. The drink and a serving can share one sense with Both countability; Duden and PONS do not make the glass/bottle use a separate numbered meaning here. “Das ist nicht mein Bier” is an idiom, not evidence that *Bier* freely means an affair or responsibility. Retain it as a construction under the brief’s idiom policy rather than use this candidate to teach that generalization.

**Grammar and style:** The note contains two clauses despite the one-short-clause rule. Duden explicitly gives *Biere* for types and *zwei Bier* for servings, so that basic distinction should not be called an error. The beverage definition can allow alcohol-free beer without inventing a separate sense. The examples are natural and within limits. The problems are the organization and what a model would infer from it.

Sources: [Duden, Bier](https://www.duden.de/rechtschreibung/Bier); [PONS, Bier](https://de.pons.com/%C3%BCbersetzung/deutsche-rechtschreibung/Bier).

## 19. stets

**use as it is** — A concise, accurate adverb entry with no necessary correction.

**Facts and senses:** One general always/at-all-times sense is sufficient. The long vowel in /ˈʃteːts/ is correct. No ordinary additional meaning is missing and there is no redundant subdivision.

**Style:** Definition, example, and antonym are natural and comply with the limits. B2 is a reasonable estimate; the brief does not justify treating an adjacent-level preference as a factual error. Although the word is frequent in written or formal contexts, the consulted Duden entry does not require a register label. Do not invent a hard formal or archaic restriction.

Source: [Duden, stets](https://www.duden.de/rechtschreibung/stets).

## 20. möglichst

**use with corrections** — Remove grammatical commentary from the definition and avoid restricting the modifier to adjectives.

**Facts and senses:** Keep the maximized-degree and if-feasible uses. They are a reasonable learner distinction, not merely two paraphrases of one use. The degree use also modifies adverbs and quantity expressions, so *vor Adjektiven* is too restrictive. Duden additionally illustrates modification of a verb phrase; the frames shown here are useful patterns, not an exhaustive syntactic inventory.

**Style:** Move the construction information into frames. Both revised definitions avoid words of the *möglich* family, and the examples remain natural and compliant. The second definition is a dependent conditional expression, not a standalone declarative sentence; that is a normal adverbial definition. IPA and the derivational relation are retained.

Sources: [Duden, möglichst](https://www.duden.de/rechtschreibung/moeglichst); [PONS, möglichst](https://de.pons.com/%C3%BCbersetzung/deutsch-englisch/m%C3%B6glichst).

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
          "definition": "so sehr oder in so hohem Maß wie machbar",
          "example": "Bitte antworten Sie uns möglichst schnell auf diese Anfrage.",
          "cefr": "B1",
          "frames": [
            "möglichst + Adj/Adv",
            "möglichst viel/wenig"
          ]
        },
        {
          "definition": "wenn es unter den gegebenen Umständen geht",
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

## Final demonstration set, best first

| Rank | Headword | Coverage |
|---|---|---|
| 1 | **nötig** | Adjective: compact definition, antonym, and three useful frames; unchanged. |
| 2 | **Nachricht** | Plain noun: gender, forms, and a plural-only sense; corrected version. |
| 3 | **fällen** | Verb with frames: literal and decision uses, weak paradigm, and a useful confusable; corrected version. |
| 4 | **perfekt** | Two parts of speech: adjective and noun, including their stress contrast and a grammar label; corrected version. |
| 5 | **Trainer** | Regional sense: a documented Austrian/Swiss use alongside the ordinary agent noun; corrected version. |

Feed only the five entry objects to the writing model. Do not include this review’s criticism as entry content. **Stets** is the cleanest spare but adds less coverage than the selected five. **Schaden** can become an attractive two-POS example after the plural-class schema and sense inventory are repaired.

Validation: all JSON blocks parse; corrected definitions and examples meet the stated word limits; corrected examples contain one sentence; frame lists contain no more than three items; accepted entries use permitted grammar keys and enumerated plural classes. A mechanical check cannot certify semantic sense boundaries, so those judgments are explained above.
