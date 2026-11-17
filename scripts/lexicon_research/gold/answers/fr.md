# French dictionary candidate review

Reviewed against the instructions in `fr.md`, with targeted checks against monolingual dictionaries. “Do not use” means unsuitable as a house-style exemplar in this selection; it does not mean the headword should be rejected from the dictionary.

The review distinguishes demonstrable errors from editorial choices. No dictionary supplies one uniquely correct sense numbering for every word. CEFR values and frequency order are editorial estimates, not corpus measurements. Adjacent-level differences alone are not treated as errors.

All original definitions have 6–15 words and all original examples have 6–14 words when whitespace-separated items containing letters or digits are counted; elided and hyphenated forms count as one and isolated punctuation does not count. Passing the length check does not establish accuracy or naturalness. Relative clauses beginning with “qui” count as adjectival defining phrases; a finite explanatory clause such as “exprime…” does not. The requested final-syllable stress marks are retained even though French lexical stress is not phonemic.

Regular feminine adjective forms are omitted under the instruction to list only forms the generator cannot supply. Pronominal verb uses remain under their base verbs, as in the supplied candidates, with auxiliary information corrected. Different etymologies alone do not create separate lexemes under this project’s rules.

There is no need to invent a missing common sense or an oversplit sense for an entry whose compact inventory is already adequate. Rare poetic and technical extensions are not automatically omissions.


| # | Candidate | Verdict |
|---|---|---|
| 1 | hiver | use as it is |
| 2 | hôpital | use as it is |
| 3 | importance | use as it is |
| 4 | avion | use with corrections |
| 5 | envoyer | do not use |
| 6 | expliquer | use with corrections |
| 7 | permettre | use with corrections |
| 8 | continuer | use with corrections |
| 9 | normal | use with corrections |
| 10 | économique | use with corrections |
| 11 | cool | do not use |
| 12 | historique | use with corrections |
| 13 | chien | do not use |
| 14 | port | use with corrections |
| 15 | couple | use with corrections |
| 16 | ben | do not use |
| 17 | chanson | use with corrections |
| 18 | seigneur | do not use |
| 19 | généralement | use with corrections |
| 20 | probablement | use with corrections |


## 1. hiver

**use as it is** — The seasonal sense and the clearly labelled literary sense are sound, with suitable IPA, frames and examples.

Facts: masculine, countable and mute h are correct; /i.ˈvɛʁ/ follows the requested stress convention. Both definitions and examples meet the length limits.

Sense list: the literary old-age metaphor is established, not an invented English transfer. No common everyday sense is missing. “Cold winter weather” can remain a shade of the season sense; the poetic use meaning a year of someone’s age need not be added to this compact learner entry. The specific “l’hiver de la vie” frame is appropriately attached to its figurative sense.

Source: [TLFi, hiver](https://www.cnrtl.fr/definition/hiver?referer=www.cuteslator.com).

## 2. hôpital

**use as it is** — A reliable plain noun entry with the correct gender, pronunciation and irregular plural.

Facts: masculine hôpital and plural hôpitaux, with /ɔ.pi.ˈtal/ and /ɔ.pi.ˈto/, are correct. The initial h is mute; omission of h_aspire is not an error. The definition is accurate and the example is natural.

Sense list: one ordinary sense is enough. The institution, its building and its medical services do not need separate numbers. No common sense is missing. “Être hospitalisé à l’hôpital” is redundant but grammatical; shortening that frame would be an optional editorial preference, not a reason to fail the entry.

## 3. importance

**use as it is** — The distinction between significance and magnitude is useful, and the entry meets the stated formal rules.

Facts: gender, mass use, IPA and the abstract-noun relation are acceptable. Both examples clearly identify their senses; no CEFR estimate is more than one step out.

Sense list: significance/value and size/extent deserve separate treatment. Social standing or influence can fit the first sense rather than becoming a third sense. No common core sense is missing. Definitions are within 6–15 words and are noun phrases.

## 4. avion

**use with corrections** — The original definition wrongly makes carrying passengers or goods a defining property of every aircraft of this type.

Facts: gender, IPA, level and frames are correct. Military, experimental and aerobatic aircraft are still avions even when transporting passengers or freight is not their purpose.

Sense list: retain one sense; do not divide aircraft by purpose. No separate common figurative sense is required. The replacement identifies the motor and fixed wings, and keeps the natural original example.

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
          "definition": "appareil à moteur qui vole grâce à des ailes fixes",
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

## 5. envoyer

**do not use** — The core conjugation is sound, but the sense inventory and constructions are too incomplete for a verb exemplar.

Facts: group 1 is correct even though the future stem is irregular; the eight requested paradigm cells and their IPA are substantially correct. The two identical indicative envoie records should be joined with Person="1,3" under the house rule. The grammar note contains two clauses. “Avec force” is unnecessarily restrictive: a gentle pass can also be expressed with envoyer. The destination frame needs chez, among other possibilities, and the throwing sense needs a construction such as envoyer qqch à qqn / vers qqch.

Sense list: emitting heat, light or signals is missing. Common informal pronominal uses, including consuming a drink and taking on an unpleasant task, also require decisions about inclusion and être. Dispatching someone on a mission and referring someone to a professional can be grouped under a broader directing/sending sense; they should not automatically become separate numbers. The existing examples are natural and within the limits, but the entry needs a broader redesign before use as a model.

Source: [Larousse, envoyer](https://www.larousse.fr/dictionnaires/francais/envoyer/30174).

## 6. expliquer

**use with corrections** — Add the missing pronominal uses and make auxiliary selection explicit.

Facts: regular first-group conjugation and IPA are correct. Avoir alone cannot describe an entry that includes elle s’est expliquée. The corrected entry uses both at lexeme level, with sense-level choices. “Expliquer que + indicative” is appropriate for reporting a proposition; it is not a blanket rule for explanatory questions such as comment expliquer qu’il soit parti.

Sense list: the original misses making oneself understood, understanding/accounting for something oneself, and the construction s’expliquer par. The last is kept with the causal sense rather than numbered merely because its syntax differs. Explaining a text, a mechanism or one’s reasoning remains one broad communicative sense. Giving an account of one’s conduct is distinguished from merely expressing oneself clearly. The correction keeps idiomatic pronominal constructions under expliquer, as the supplied candidates do.

Source: [Usito, expliquer](https://usito.usherbrooke.ca/d%C3%A9finitions/expliquer).

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
        "auxiliary": "both",
        "note": "être uniquement à la forme pronominale"
      },
      "pronunciations": [
        {
          "variety": "fr-FR",
          "ipa": "/ɛks.pli.ˈke/"
        }
      ],
      "senses": [
        {
          "definition": "faire comprendre quelque chose à quelqu’un en donnant des détails",
          "example": "Peux-tu m’expliquer comment fonctionne cette machine ?",
          "cefr": "A2",
          "frames": [
            "expliquer qqch à qqn",
            "expliquer à qqn que + ind",
            "expliquer à qqn comment / pourquoi"
          ],
          "grammar": {
            "valency": "Tr",
            "auxiliary": "have"
          }
        },
        {
          "definition": "être la cause ou la raison de quelque chose",
          "example": "Le mauvais temps explique en partie le retard du train.",
          "cefr": "B1",
          "frames": [
            "expliquer qqch",
            "s’expliquer par qqch"
          ],
          "grammar": {
            "auxiliary": "both",
            "note": "être uniquement dans s’expliquer par"
          }
        },
        {
          "definition": "exprimer ses idées de manière à se faire comprendre",
          "example": "Je me suis mal expliqué, alors je vais recommencer.",
          "cefr": "B1",
          "frames": [
            "s’expliquer"
          ],
          "grammar": {
            "valency": "Refl",
            "reflexive": true,
            "auxiliary": "be"
          }
        },
        {
          "definition": "donner les raisons de sa conduite pour se justifier",
          "example": "Explique-toi sur ton absence avant de demander une nouvelle autorisation.",
          "cefr": "B1",
          "frames": [
            "s’expliquer sur qqch",
            "s’expliquer avec qqn"
          ],
          "grammar": {
            "valency": "Refl",
            "reflexive": true,
            "auxiliary": "be"
          }
        },
        {
          "definition": "comprendre la raison ou la cause de quelque chose",
          "example": "Je ne m’explique pas son silence depuis notre dernière rencontre.",
          "cefr": "B2",
          "frames": [
            "s’expliquer qqch"
          ],
          "grammar": {
            "valency": "Refl",
            "reflexive": true,
            "auxiliary": "be"
          }
        }
      ]
    }
  ]
}
```

## 7. permettre

**use with corrections** — Supply the missing “afford” use, the subjunctive frame and the pronominal auxiliary.

Facts: forms and IPA are correct. Se permettre uses être in compound tenses. The original third definition is too narrow for its courteous letter-opening example; the replacement also covers self-authorized indulgence and taking a liberty. Permettre que takes the subjunctive. The unqualified derived_from mettre relation is removed: the recorded etymology is a Latin borrowing influenced by mettre, not straightforward French prefixation.

Sense list: permission, making possible, taking a liberty and being able to afford something are useful distinctions. “Je ne peux pas me permettre cette voiture” cannot be reduced to audacity. The polite formula is kept as a use of taking a liberty rather than inflated into another numbered sense. Four senses suffice here.

Sources: [Larousse, permettre](https://www.larousse.fr/dictionnaires/francais/permettre/59689); [Académie, permettre](https://www.dictionnaire-academie.fr/article/A9P1630).

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
        "auxiliary": "both",
        "valency": "Tr",
        "note": "être uniquement à la forme pronominale"
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
          "definition": "rendre possible une action ou donner les moyens de la réaliser",
          "example": "Ce nouveau train permet d'aller plus vite à la mer.",
          "cefr": "B1",
          "frames": [
            "permettre qqch",
            "permettre de + inf",
            "permettre à qqn de + inf"
          ],
          "grammar": {
            "valency": "Tr",
            "auxiliary": "have"
          }
        },
        {
          "definition": "autoriser quelqu’un à faire quelque chose ou accepter une action",
          "example": "Mes parents ne me permettent pas de sortir le soir.",
          "cefr": "A2",
          "frames": [
            "permettre qqch à qqn",
            "permettre à qqn de + inf",
            "permettre que + subj"
          ],
          "antonyms": [
            {
              "lemma": "interdire",
              "pos": "VERB"
            }
          ],
          "grammar": {
            "valency": "Tr",
            "auxiliary": "have"
          }
        },
        {
          "definition": "prendre la liberté de faire quelque chose ou de s’accorder un plaisir",
          "example": "Je me permets de vous écrire au sujet de votre annonce.",
          "cefr": "B1",
          "frames": [
            "se permettre qqch",
            "se permettre de + inf"
          ],
          "grammar": {
            "valency": "Refl",
            "reflexive": true,
            "auxiliary": "be"
          }
        },
        {
          "definition": "avoir assez de moyens ou de liberté pour faire quelque chose",
          "example": "Avec ce salaire, je ne peux pas me permettre cette voiture.",
          "cefr": "B1",
          "frames": [
            "se permettre qqch",
            "se permettre de + inf"
          ],
          "grammar": {
            "valency": "Refl",
            "reflexive": true,
            "auxiliary": "be"
          }
        }
      ]
    }
  ]
}
```

## 8. continuer

**use with corrections** — Keep the compact sense division, but fix the first sense’s valency and the second sense’s mismatched example.

Facts: regular first-group grammar and /kɔ̃.ti.ˈnɥe/ are correct; both continuer à and continuer de are standard. The activity sense also occurs without an object, so it is not exclusively transitive.

Sense list: “Continuez tout droit” expresses continuing an activity or movement with an agent; it is a poor example for impersonal duration or spatial extension. A rain example now illustrates the second sense. Resuming after a pause is explicitly included in the first definition. Duration and spatial extension are defensible shades of one “have a continuation” sense, as in Usito; they do not have to become separate entries. No further common sense is required at this resolution.

Source: [Usito, continuer](https://usito.usherbrooke.ca/d%C3%A9finitions/continuer).

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
          "definition": "poursuivre une activité déjà commencée, sans s’arrêter ou après une pause",
          "example": "Malgré la pluie, nous avons continué à marcher jusqu'au village.",
          "cefr": "A2",
          "frames": [
            "continuer qqch",
            "continuer à + inf",
            "continuer de + inf"
          ],
          "grammar": {
            "valency": "Both"
          },
          "antonyms": [
            {
              "lemma": "arrêter",
              "pos": "VERB"
            }
          ]
        },
        {
          "definition": "se prolonger dans le temps ou dans l’espace",
          "example": "La pluie continue depuis ce matin sans aucune interruption.",
          "cefr": "A2",
          "grammar": {
            "valency": "Intr"
          }
        }
      ]
    }
  ]
}
```

## 9. normal

**use with corrections** — Remove the false predicative-only restriction and supply the ordinary functioning/health sense.

Facts: normaux and its IPA are correct. Normale and normales are regular and should be generated, not listed under the irregular-only forms rule. The “understandable/acceptable” sense is also attributive: une réaction normale; position=Pred is therefore false. The relation to norme is encoded as adjective_of rather than as a direct derivation.

Sense list: usual, understandable/acceptable, and functioning/developing without a problem are a defensible learner grouping. Expectations of fairness can remain under acceptable rather than creating another small shade. The missing functioning sense is illustrated with a machine, avoiding an unexplained judgement about people. The separate noun normale is not the lemma normal and does not belong here.

Source: [Larousse, normal](https://www.larousse.fr/dictionnaires/francais/normal/54992).

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
          "form": "normaux",
          "features": {
            "Gender": "Masc",
            "Number": "Plur"
          },
          "ipa": {
            "fr-FR": "/nɔʁ.ˈmo/"
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
          ]
        },
        {
          "definition": "qui fonctionne ou se développe sans problème particulier",
          "example": "Depuis sa réparation, le moteur retrouve un fonctionnement normal.",
          "cefr": "B1",
          "antonyms": [
            {
              "lemma": "anormal",
              "pos": "ADJ"
            }
          ]
        }
      ],
      "relations": [
        {
          "type": "adjective_of",
          "lemma": "norme",
          "pos": "NOUN"
        }
      ]
    }
  ]
}
```

## 10. économique

**use with corrections** — Broaden the economic definition, clarify low resource consumption and include the established noun use.

Facts: IPA is correct. Economic activity is not restricted to a country: households, firms and international systems also qualify. The first definition is rewritten without the “lié à” formula. The inexpensive sense includes low fuel, energy or material use; the new example demonstrates this directly.

Sense list: the two adjective senses are distinct. Purchase price and running costs are shades of the inexpensive/resource-saving sense, not separate numbered senses. The noun l’économique, especially in contrast with le social or le politique, is missing and is included as a masculine mass lexeme. It is not necessary to add the more specialist name of an academic science.

Sources: [Larousse, économique](https://www.larousse.fr/dictionnaires/francais/%C3%A9conomique/27632); [Académie, économique](https://www.dictionnaire-academie.fr/article/A9E0289).

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
          "definition": "qui met en jeu la production, les échanges et la consommation de richesses",
          "example": "Notre pays traverse une grave crise économique depuis deux ans.",
          "cefr": "B1"
        },
        {
          "definition": "qui coûte peu ou consomme peu d’argent, d’énergie ou de matières",
          "example": "Cette petite voiture est économique, car elle consomme peu de carburant.",
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
    },
    {
      "lemma": "économique",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {
        "gender": "Masc",
        "countability": "Mass",
        "number": "Sing"
      },
      "pronunciations": [
        {
          "variety": "fr-FR",
          "ipa": "/e.kɔ.nɔ.ˈmik/"
        }
      ],
      "senses": [
        {
          "definition": "ensemble des activités de production, d’échange et de consommation des richesses",
          "example": "Dans ce débat, l’économique prend le dessus sur le social.",
          "cefr": "C1"
        }
      ],
      "relations": [
        {
          "type": "derived_from",
          "lemma": "économique",
          "pos": "ADJ"
        }
      ]
    }
  ]
}
```

## 11. cool

**do not use** — The adjective merges different meanings, and the proposed second part of speech is a less reliable model than historique.

Facts: /ˈkul/ and the informal label are sound. Cool is traditionally invariable, while the rectified spelling permits plural cools; presenting both without explaining the spelling convention is incomplete for a model of morphology. This is a plural-form issue, not a second lemma. Calmness and tolerance are different: a calm person can be strict, so strict is not an opposite of the whole second sense.

Sense list: pleasant, relaxed and tolerant should be distinguished more carefully. The interjection is a genuine possible use, not an invented French meaning, but dictionaries differ in whether they give it its own lexical category or treat it as exclamatory adjective use. The jazz sense is specialist and need not be added under this brief.

Style: “exprime la satisfaction…” is a finite explanatory clause, not the required phrase. “Cool ! On part…” is two sentences, contrary to the one-sentence rule. Quebec normative treatment also differs from unmarked informal use in France. These interacting decisions make this a poor house-style exemplar.

Sources: [Larousse, cool](https://www.larousse.fr/dictionnaires/francais/Cool/19049); [Usito, cool](https://usito.usherbrooke.ca/d%C3%A9finitions/cool); [Cambridge French–English, cool](https://dictionary.cambridge.org/fr/dictionnaire/francais-anglais/cool).

## 12. historique

**use with corrections** — Preserve the two parts of speech, but repair the adjective coverage and replace the awkward medical example.

Facts: IPA, noun gender and the two lexical categories are correct. “L’historique de vos maladies” is understandable but an awkward model; ordinary medical phrasing is often vos antécédents médicaux. The replacement illustrates faire l’historique de with a project.

Sense list: the original first adjective sense mixes a connection with past events and factual reality. The corrected inventory also covers unprecedented record levels and a founding/longstanding participant. These are current ordinary-text uses, not technical extras. The general noun account and a stored digital history can remain separate: the distinction is an established list/function meaning, not simply “history in another subject area.” The digital noun comes first as an editorial frequency estimate, not a measured corpus result.

Sources: [Le Robert, historique](https://dictionnaire.lerobert.com/definition/historique); [Larousse, historique](https://www.larousse.fr/dictionnaires/francais/historique/40092).

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
          "definition": "qui présente ou étudie les événements et les sociétés du passé",
          "example": "Nous préparons une exposition historique sur la vie du village.",
          "cefr": "B1"
        },
        {
          "definition": "assez marquant pour rester longtemps dans la mémoire collective",
          "example": "Cette victoire historique marque un tournant pour toute l’équipe.",
          "cefr": "B1"
        },
        {
          "definition": "dont la réalité est établie, contrairement aux récits inventés",
          "example": "Ce personnage est historique, même si ses aventures sont inventées.",
          "cefr": "B2",
          "antonyms": [
            {
              "lemma": "imaginaire",
              "pos": "ADJ"
            }
          ]
        },
        {
          "definition": "jamais atteint auparavant, au point de marquer les esprits",
          "example": "Le chômage atteint un niveau historique dans cette région.",
          "cefr": "B2"
        },
        {
          "definition": "présent depuis les débuts d’une organisation ou d’une activité",
          "example": "Le dirigeant historique du mouvement annonce enfin son départ.",
          "cefr": "B2"
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
          "definition": "liste enregistrée des pages visitées ou des actions faites sur un appareil",
          "example": "Pense à effacer l'historique de ton navigateur sur cet ordinateur.",
          "cefr": "B1",
          "labels": {
            "domain": "computing"
          }
        },
        {
          "definition": "récit, dans l'ordre du temps, des faits passés concernant une chose",
          "example": "Pouvez-vous faire un bref historique du projet pour nos nouveaux collègues ?",
          "cefr": "B2",
          "frames": [
            "faire l’historique de qqch"
          ]
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

## 13. chien

**do not use** — The entry omits a common insulting use and an adjective lexeme while defining the animal too narrowly.

Facts: gender and /ˈʃjɛ̃/ are correct. Guarding a house is not a defining property of all dogs. The count/mass distinction for the animal and avoir du chien is useful. Chienne as a noun is a separate lemma under these rules; it is not a missing feminine noun form of this lexeme.

Sense list: the insulting designation of a person is a more pressing omission than additional zoological detail. The informal adjective chien, meaning harsh, mean or stingy, also belongs under the headword. Having charm is an established figurative use and can remain attached to avoir du chien; do not turn every chien idiom into another noun sense. A firearm component is attested but specialist, so its omission alone does not fail the entry.

Style: the examples meet the length rules. The beauty-versus-charm contrast in the second is traditional but makes a less useful neutral model than a fresh example. Reject this candidate as an exemplar rather than patching only its animal definition.

Source: [Larousse, chien](https://www.larousse.fr/dictionnaires/francais/chien/15295).

## 14. port

**use with corrections** — Include inland ports, define wearing accurately and remove a derivation that wrongly covers unrelated senses.

Facts: “sur la côte” excludes river and lake ports. The wearing sense is not simply use: someone can use an object without wearing or carrying it. Gender, IPA and the sense-specific mass overrides are otherwise sound.

Sense list: the five original core senses are defensible; a literary safe-refuge sense is added. A port city can remain a metonymic shade of the harbour sense; plant habit and network-port numbers need not expand this general learner entry. A computer socket is common enough in ordinary instructions to retain with its domain label.

The harbour meaning and the carrying/wearing meanings have different histories. Keep one lexeme because your schema groups equal pronunciation, gender and paradigm, but remove the lexeme-wide derived_from porter link: the schema has no way to restrict it to the relevant senses. The existing porc confusable is valid.

Sources: [Usito, port 1](https://usito.usherbrooke.ca/d%C3%A9finitions/port_1); [Usito, port 2](https://usito.usherbrooke.ca/d%C3%A9finitions/port_2).

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
          "definition": "lieu aménagé au bord de l’eau où les bateaux viennent s’abriter",
          "example": "Chaque matin, les bateaux de pêche rentrent au port.",
          "cefr": "A2",
          "frames": [
            "au port",
            "arriver à bon port"
          ]
        },
        {
          "definition": "présence sur soi d’un vêtement, d’un équipement ou d’un autre objet",
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
        },
        {
          "definition": "lieu où trouver la sécurité après des épreuves ou des dangers",
          "example": "Après tant d’épreuves, cette maison devient enfin son port.",
          "cefr": "C1",
          "labels": {
            "figurative": true,
            "style": [
              "literary"
            ]
          }
        }
      ],
      "relations": [
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

## 15. couple

**use with corrections** — Correct the relationship definition, widen the paired sense and soften the Canadian-only claim.

Facts: separate masculine and feminine lexemes are correct. Romantic partners need not be married or cohabit, and continued mutual affection is not a definitional condition. The feminine noun is not categorically singular-only; remove number=Sing and describe its usual construction instead. A colloquial regional use is not automatically proscribed, so remove standard_alternative.

Sense list: the original misses associated non-romantic partners and things. The animal pair is grouped with this broader paired sense rather than numbered solely because its referents are animals. The feminine quantitative use remains separate because the gender differs. Two or a few are closely related quantity shades, not additional senses.

Region: Usito marks une couple de as familiar Quebec/Canadian usage and notes that it is old-fashioned in France. “Only” is therefore too strong; “mainly” is the conservative representation here. It is not a claim that the use is equally common across all other varieties. Specialist mechanics and nautical senses are outside this compact general entry.

Source: [Usito, couple](https://usito.usherbrooke.ca/d%C3%A9finitions/couple); especially [une couple de](https://usito.usherbrooke.ca/d%C3%A9finitions/couple/lexies).

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
          "definition": "deux personnes unies par une relation amoureuse ou par le mariage",
          "example": "Nos voisins forment un couple très uni depuis trente ans.",
          "cefr": "A2",
          "frames": [
            "être en couple",
            "vivre en couple",
            "former un couple"
          ]
        },
        {
          "definition": "ensemble de deux personnes, animaux ou choses associés",
          "example": "Les danseurs se répartissent par couples avant le début du cours.",
          "cefr": "B1",
          "frames": [
            "un couple de + nom pluriel",
            "par couples"
          ]
        }
      ],
      "relations": [
        {
          "type": "confusable",
          "lemma": "paire",
          "pos": "NOUN",
          "why": "paire often denotes two matching or complementary items; couple emphasizes an association, especially a romantic relationship"
        }
      ]
    },
    {
      "lemma": "couple",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {
        "gender": "Fem",
        "countability": "Count",
        "note": "surtout dans une couple de + nom pluriel"
      },
      "pronunciations": [
        {
          "variety": "fr-FR",
          "ipa": "/ˈkupl/"
        }
      ],
      "senses": [
        {
          "definition": "groupe de deux ou de quelques personnes ou choses",
          "example": "Attends-moi ici, je reviens dans une couple de minutes.",
          "cefr": "B2",
          "frames": [
            "une couple de + nom pluriel"
          ],
          "labels": {
            "formality": "informal"
          },
          "regions": [
            {
              "variety": "fr-CA",
              "extent": "mainly"
            }
          ]
        }
      ]
    }
  ]
}
```

## 16. ben

**do not use** — The exclusive Canadian region and the over-split discourse functions would teach unreliable annotation habits.

Facts: /ˈbɛ̃/ is suitable for the listed France pronunciation. The adverb is not exclusively Canadian: Le Robert also records a rural French adverb corresponding to bien. Tuque suits the Canadian example, but a locally characteristic example cannot establish exclusivity of the headword. The unattached standard_alternative field is not warranted by the existing informal-only label.

Sense list: certainty, hesitation and surprise are overlapping discourse functions that can be handled in one interjection sense with suitable frames. The wider adverbial value of bien is not exhausted by the degree gloss “beaucoup, vraiment.” Ben quoi ? often marks a challenge or reaction, so it is not a precise frame for the hesitation example. The rare botanical noun is not required by the ordinary-text scope.

Style: the word counts pass, but repeated “mot de l’oral qui…” definitions are metalinguistic templates rather than strong examples of direct defining style. The entry needs a stable policy for speech variants, discourse particles and regional marking before it should teach the writing model.

Source: [Le Robert, ben](https://dictionnaire.lerobert.com/definition/ben); [Larousse, ben](https://www.larousse.fr/dictionnaires/francais/ben/8713).

## 17. chanson

**use with corrections** — Define the musical work rather than only its text, and remove the frame that expresses a different meaning.

Facts: gender, IPA and levels are acceptable. A chanson is not merely a short written text; the definition should include the musical composition. The revised wording avoids chanter and chant, which belong to the headword’s family. The uncertain direct derived_from chanter link is dropped; shared family membership is not by itself proof of direct derivation.

Sense list: the literal composition and familiar repetitive talk make a defensible compact pair, also found in Larousse’s school dictionary. Words and tune are aspects of the same work, not mandatory separate senses here. Poetic birdsong is a less central extension and need not be added. C’est une autre chanson means a different matter or difficulty, not tiresomely repeated remarks, so it is removed from that sense’s frames; connaître la chanson fits. The confusable note is corrected because chant can also denote a vocal work with words.

Sources: [Larousse, chanson](https://www.larousse.fr/dictionnaires/francais/chanson/14627); [Larousse collège, chanson](https://www.larousse.fr/dictionnaires/college/chanson/7276).

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
          "definition": "composition musicale dont les paroles sont interprétées avec la voix",
          "example": "Ma grand-mère connaît par cœur toutes les vieilles chansons populaires.",
          "cefr": "A1"
        },
        {
          "definition": "propos qu'on répète sans cesse et qui lassent",
          "example": "Avec lui, c'est toujours la même chanson : il n'a jamais le temps.",
          "cefr": "B2",
          "frames": [
            "c’est toujours la même chanson",
            "connaître la chanson"
          ],
          "labels": {
            "figurative": true,
            "formality": "informal"
          }
        }
      ],
      "relations": [
        {
          "type": "confusable",
          "lemma": "chant",
          "pos": "NOUN",
          "why": "chant can mean singing, birdsong or a vocal composition; chanson usually denotes a song with words, often organized into verses and a refrain"
        }
      ]
    }
  ]
}
```

## 18. seigneur

**do not use** — Historical overrestriction, an unsuitable antonym and incomplete figurative coverage make this a poor exemplar.

Facts: masculine gender and /sɛ.ˈɲœʁ/ are correct. Seigneur is not confined to the Middle Ages; the word also applies under the Ancien Régime. Vassal is a relational counterpart, not a clear opposite: a person could be someone’s vassal and someone else’s lord. Remove that antonym.

Sense list: a dominant or leading person in a field is missing. Wealth and actual generosity are not necessary for en grand seigneur, which can emphasize lavish or ostentatious behaviour. The religious sense should explicitly cover the title for God or Christ. Capitalized Seigneur belongs under the same headword by the supplied capitalization rule, not in a separate entry. Noble bearing and lavish spending within grand seigneur should not automatically become independent noun senses.

Style: “nom que…” is a naming formula rather than a strong noun definition. The definition’s proper name is not itself prohibited—the no-proper-names rule applies to examples—but the example containing Moyen Âge is at least an avoidable edge case for that rule. The main reasons for rejection are semantic and structural.

Sources: [Larousse, seigneur](https://www.larousse.fr/dictionnaires/francais/seigneur/71844); [Larousse, vassal](https://www.larousse.fr/dictionnaires/francais/vassal/81183).

## 19. généralement

**use with corrections** — Add the broad-overview meaning and avoid defining ordinary frequency as “almost always.”

Facts: /ʒe.ne.ʁal.ˈmɑ̃/ and the relation to général are correct. Presque toujours is stronger than the core claim and should not be taught as equivalent in every context. The original examples and word counts are otherwise sound.

Sense list: the missing use means considering a matter broadly without its details. The original “by most people” use can be grouped with “in most cases” in a concise learner treatment; this is an editorial choice supported by the Académie and Usito, not a claim that dictionaries universally merge them—Larousse separates them. The correction gives two useful broader senses rather than preserving two overlapping majority readings and omitting the other meaning.

Sources: [Académie, généralement](https://www.dictionnaire-academie.fr/article/A9G0476); [Larousse, généralement](https://www.larousse.fr/dictionnaires/francais/g%C3%A9n%C3%A9ralement/36518).

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
          "definition": "dans la plupart des cas ou pour la plupart des personnes",
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
          "definition": "en considérant l’ensemble, sans examiner chaque cas particulier",
          "example": "Ce problème est traité trop généralement dans votre rapport.",
          "cefr": "B2"
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

## 20. probablement

**use with corrections** — Keep the single sense and pronunciation, but replace the unnecessarily difficult defining expression.

Facts: /pʁɔ.ba.blə.ˈmɑ̃/ is a standard recorded pronunciation; its internal schwa should not be removed merely because some unstressed schwas disappear in speech. The relation to probable is correct, and A2 is within an acceptable estimate.

Sense list: one epistemic-probability sense is enough. An isolated reply and a sentence-modifying use are syntactic uses, not separate meanings. No common sense is missing. “Selon toute vraisemblance” is accurate but harder than the plain wording requested; the replacement preserves uncertainty without equating the word with certainty. The original example is retained.

Source: [TLFi, probablement](https://www.cnrtl.fr/definition/probablement).

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
          "definition": "avec de bonnes raisons de croire que quelque chose est vrai ou arrivera",
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

## The five to show the writing model, best first

Use the complete corrected JSON above wherever a correction is specified; never feed the original defective version alongside it.

| Rank | Entry | Version | What it teaches |
|---|---|---|---|
| 1 | hôpital | Original | Plain concrete noun; gender; irregular plural with its own IPA |
| 2 | continuer | Corrected | Regular verb; à/de infinitive frames; transitive and intransitive use |
| 3 | normal | Corrected | Adjective; irregular masculine plural; a useful subjunctive frame |
| 4 | historique | Corrected | One headword with adjective and noun lexemes; genuine polysemy; a computing label |
| 5 | couple | Corrected | Gender-based lexeme separation; a familiar regional sense; cautious geographical extent |

This set covers every requested category. Hiver is the strongest unchanged alternative if a literary label is preferred to a regional example. Permettre is the alternative to continuer if the writing model specifically needs an irregular-verb paradigm and pronominal auxiliary selection; its greater complexity is why it is not the default verb example.

The corrected JSON blocks are complete entries in the original shape. They do not include review metadata or source citations inside the dictionary data.
