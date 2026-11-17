# Italian learner’s dictionary review

Across the 18 candidates, **2 can be used as submitted**, **13 need corrections**, and **3 should be left out of the model examples**. The five recommended entries are ranked at the end.

Every submitted definition meets the 6–15-word limit, and every submitted example meets the 6–14-word limit. Elisions such as *un’attività* count as one word. The reply under *addirittura* passes the length check but contains three sentences. CEFR levels and sense order are editorial estimates.

## Verdicts

| No. | Headword | Verdict |
|---:|---|---|
| 1 | [collaborazione](#1-collaborazione) | use with corrections |
| 2 | [denaro](#2-denaro) | do not use |
| 3 | [fenomeno](#3-fenomeno) | use with corrections |
| 4 | [museo](#4-museo) | use with corrections |
| 5 | [comprare](#5-comprare) | use with corrections |
| 6 | [mantenere](#6-mantenere) | use with corrections |
| 7 | [iniziare](#7-iniziare) | use with corrections |
| 8 | [continuare](#8-continuare) | use with corrections |
| 9 | [recente](#9-recente) | use with corrections |
| 10 | [strano](#10-strano) | use with corrections |
| 11 | [europeo](#11-europeo) | do not use |
| 12 | [minimo](#12-minimo) | use with corrections |
| 13 | [bellezza](#13-bellezza) | do not use |
| 14 | [addirittura](#14-addirittura) | use with corrections |
| 15 | [malattia](#15-malattia) | use with corrections |
| 16 | [pressione](#16-pressione) | use with corrections |
| 17 | [immediatamente](#17-immediatamente) | use as it is |
| 18 | [attualmente](#18-attualmente) | use as it is |

The corrected JSON follows the Italian candidate structure. Where the brief requires spelling variants, its examples supply no nested spelling object. This review uses `form` and `varieties`, with `pre_reform` for an older written variant as specified in the brief.

## 1. collaborazione

**use with corrections** — The freelance sense turns on working as an outside contributor. The draft also misses cooperation with an occupying enemy.

A long-running arrangement with a newspaper can be a *collaborazione*. The example already shows this. Keep assistance and working together under the first sense. The enemy sense can describe a present-day occupation, so it needs no historical label. The revised IPA keeps the long /lː/ together with its following vowel and retains the doubled stop in /tˈtsj/. [De Mauro](https://dizionario.internazionale.it/parola/collaborazione) · [Treccani](https://www.treccani.it/vocabolario/collaborazione/).

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
          "ipa": "/kolːa.bo.ratˈtsjo.ne/",
          "reading": "collaborazióne"
        }
      ],
      "senses": [
        {
          "definition": "lavoro fatto insieme ad altre persone per raggiungere uno scopo comune",
          "example": "Grazie alla collaborazione di tutti, finiremo il progetto in tempo.",
          "cefr": "B1",
          "frames": [
            "collaborazione con qualcuno",
            "collaborazione a un progetto",
            "in collaborazione con qualcuno"
          ]
        },
        {
          "definition": "attività svolta per un’azienda o un giornale come professionista esterno",
          "example": "Da anni ha una collaborazione con un quotidiano locale.",
          "cefr": "B2",
          "frames": [
            "collaborazione con un giornale",
            "collaborazione a una rivista"
          ],
          "grammar": {
            "countability": "Count"
          }
        },
        {
          "definition": "aiuto dato da alcuni abitanti alle forze che occupano il loro paese",
          "example": "Dopo la guerra fu accusato di collaborazione con il nemico.",
          "cefr": "C1",
          "frames": [
            "collaborazione con il nemico"
          ]
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

## 2. denaro

**do not use** — The core gloss excludes money held in an account, and the entry misses the card suit *denari* and the variant *danaro*.

The ordinary sense needs a gloss such as *soldi che si possono spendere, ricevere o conservare*. Cash and bank balances belong together. In *il sette di denari*, the plural names a suit and needs a plural restriction on that sense. The countable historical coin merits its time label, although the metal and value varied. Hosiery labels such as *quaranta denari* are a practical argument for also mentioning the textile unit. The spellings and their historical variants make this a poor choice for the simple noun slot. [De Mauro](https://dizionario.internazionale.it/parola/denaro) · [De Mauro](https://dizionario.internazionale.it/parola/danaro) · [Treccani](https://www.treccani.it/vocabolario/denaro/).

## 3. fenomeno

**use with corrections** — Give observable events, socially significant developments, and the informal figurative use examples that distinguish all three.

The draft’s climate example can illustrate either observation or wider social significance. An *arcobaleno* and a *fenomeno di massa* make the distinction visible. The figurative use can praise a person or a thing. An ironic *sei proprio un fenomeno!* uses the same evaluation with reversed intent. The philosophical use falls outside this learner selection. [De Mauro](https://dizionario.internazionale.it/parola/fenomeno).

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
          "definition": "fatto che si manifesta e può essere osservato o studiato",
          "example": "L’arcobaleno è un fenomeno che si vede spesso dopo un temporale.",
          "cefr": "B1"
        },
        {
          "definition": "fatto o tendenza che assume particolare importanza nella società",
          "example": "Il lavoro da casa è ormai un fenomeno di massa.",
          "cefr": "B2"
        },
        {
          "definition": "persona o cosa che stupisce per le sue qualità eccezionali",
          "example": "Mia sorella è un fenomeno con i numeri.",
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

## 4. museo

**use with corrections** — The gloss should cover the collection as well as the place where visitors see it, including a museum in the open air.

Keep one ordinary noun sense. Italian dictionaries group these closely related readings, and a separate number for an art, science, or outdoor museum would teach the model to split by subject. The original gender, open stressed *è*, voiced /z/, level, and example work. *Casa museo* illustrates an appositional use that learners can understand through the noun. [De Mauro](https://dizionario.internazionale.it/parola/museo) · [Treccani](https://www.treccani.it/vocabolario/museo/).

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
          "definition": "raccolta di opere o oggetti di valore culturale, o il luogo dove si espongono",
          "example": "Domenica andiamo al museo a vedere una mostra di pittura.",
          "cefr": "A1"
        }
      ]
    }
  ]
}
```

## 5. comprare

**use with corrections** — Add the variant *comperare*, show a seller in the example, and cover buying someone’s silence or vote.

The ordinary pattern takes a direct object. *Da qualcuno* identifies the seller, while *a qualcuno* commonly identifies the beneficiary. A dative pronoun can depend on context. The second sense covers both a person who is bribed and the cooperation secured. *Vendere* is an opposite only for the literal transaction. [De Mauro](https://dizionario.internazionale.it/parola/comprare) · [De Mauro](https://dizionario.internazionale.it/parola/comperare) · [Treccani](https://www.treccani.it/vocabolario/comprare/).

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
        },
        {
          "variety": "it-IT",
          "ipa": "/kom.peˈra.re/",
          "reading": "comperàre"
        }
      ],
      "senses": [
        {
          "definition": "ottenere una cosa pagando una somma di denaro",
          "example": "Domani compro una bicicletta usata da un collega.",
          "cefr": "A1",
          "frames": [
            "comprare qualcosa",
            "comprare qualcosa da qualcuno",
            "comprare qualcosa a qualcuno"
          ],
          "antonyms": [
            {
              "lemma": "vendere",
              "pos": "VERB"
            }
          ]
        },
        {
          "definition": "ottenere con denaro o favori il sostegno, il silenzio o la complicità di qualcuno",
          "example": "Con quei regali cercava di comprare il silenzio dei testimoni.",
          "cefr": "B2",
          "frames": [
            "comprare qualcuno",
            "comprare il silenzio di qualcuno",
            "comprare il voto di qualcuno"
          ],
          "labels": {
            "figurative": true
          }
        }
      ],
      "spellings": [
        {
          "form": "comprare",
          "varieties": [
            "it-IT"
          ]
        },
        {
          "form": "comperare",
          "varieties": [
            "it-IT"
          ]
        }
      ]
    }
  ]
}
```

## 6. mantenere

**use with corrections** — Keep the irregular forms, add the object-plus-adjective construction, and distinguish paying a thing’s running costs from keeping it in good condition.

Compare *mantiene calda la zuppa*, *mantiene due auto* and *mantiene il giardino*. A promise requires a different gloss again. *Mantenersi con uno stipendio* is a pronominal construction with *essere* in compound tenses. Replace it in this transitive entry with *farsi mantenere da qualcuno*. The ten requested form slots are present. In particular, *mantèngo* and *manténni* have different stressed vowels. The shared subjunctive form is marked for all three singular persons, and *manteniamo* for both moods. Omit the uncertain direct derivation from modern *tenere*. De Mauro traces the verb to Latin *manu tenere*. [De Mauro](https://dizionario.internazionale.it/parola/mantenere) · [Treccani on the *tenere* paradigm](https://www.treccani.it/vocabolario/tenere/).

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
            "Number": "Sing",
            "VerbForm": "Fin"
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
            "Number": "Sing",
            "VerbForm": "Fin"
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
            "Number": "Sing",
            "VerbForm": "Fin"
          },
          "ipa": {
            "it-IT": "/manˈtjɛ.ne/"
          },
          "reading": "mantiène"
        },
        {
          "form": "manteniamo",
          "features": {
            "Mood": "Ind,Sub",
            "Tense": "Pres",
            "Person": "1",
            "Number": "Plur",
            "VerbForm": "Fin"
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
            "Number": "Plur",
            "VerbForm": "Fin"
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
            "Number": "Sing",
            "VerbForm": "Fin"
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
            "Number": "Sing",
            "VerbForm": "Fin"
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
            "Number": "Sing",
            "VerbForm": "Fin"
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
            "Person": "1,2,3",
            "Number": "Sing",
            "VerbForm": "Fin"
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
          "definition": "fare in modo che qualcuno o qualcosa resti in una certa condizione",
          "example": "Questo coperchio mantiene calda la zuppa fino all’ora di cena.",
          "cefr": "B1",
          "frames": [
            "mantenere la calma",
            "mantenere qualcuno o qualcosa + aggettivo"
          ]
        },
        {
          "definition": "fare davvero ciò che si è promesso a qualcuno",
          "example": "Non preoccuparti, manterrò la promessa che ti ho fatto.",
          "cefr": "B1",
          "frames": [
            "mantenere una promessa",
            "mantenere la parola",
            "mantenere un impegno"
          ]
        },
        {
          "definition": "dare a qualcuno il necessario per vivere",
          "example": "Con un solo stipendio mantiene moglie e tre figli.",
          "cefr": "B1",
          "frames": [
            "mantenere qualcuno",
            "farsi mantenere da qualcuno"
          ]
        },
        {
          "definition": "sostenere nel tempo le spese necessarie per qualcosa",
          "example": "Ormai ci costa troppo mantenere due auto in famiglia.",
          "cefr": "B2",
          "frames": [
            "mantenere una casa",
            "mantenere un’auto"
          ]
        },
        {
          "definition": "prendersi cura di qualcosa perché resti in buono stato",
          "example": "Per mantenere il giardino servono tempo e attrezzi adatti.",
          "cefr": "B2",
          "frames": [
            "mantenere qualcosa in buono stato"
          ]
        }
      ]
    }
  ]
}
```

## 7. iniziare

**use with corrections** — The rule “essere when intransitive” predicts the wrong auxiliary for *ho iniziato a lavorare*.

Keep starting an activity with a noun object and beginning to do it with *a* + infinitive under one agentive meaning. Give the event-subject construction its own example with *essere*. A person who introduces a child to photography need perform no ceremony, so introducing someone to a skill and admitting them through a rite merit separate glosses. Both use *iniziare qualcuno a qualcosa*. [De Mauro](https://dizionario.internazionale.it/parola/iniziare) · [Treccani](https://www.treccani.it/vocabolario/iniziare/).

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
        "auxiliary": "both"
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
          "definition": "compiere i primi passi di un’attività o mettersi a fare qualcosa",
          "example": "Dopo cena ho iniziato a leggere il tuo libro.",
          "cefr": "A1",
          "frames": [
            "iniziare qualcosa",
            "iniziare a + infinito"
          ],
          "grammar": {
            "valency": "Both",
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
          "definition": "prendere avvio in un certo momento o luogo",
          "example": "La lezione è iniziata mentre cercavo un posto libero.",
          "cefr": "A2",
          "frames": [
            "qualcosa inizia",
            "iniziare con qualcosa"
          ],
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
          "definition": "guidare qualcuno nei primi passi di una nuova attività",
          "example": "È stata mia zia a iniziarmi alla fotografia.",
          "cefr": "C1",
          "frames": [
            "iniziare qualcuno a qualcosa"
          ],
          "grammar": {
            "valency": "Tr",
            "auxiliary": "have"
          }
        },
        {
          "definition": "ammettere qualcuno in un gruppo attraverso un rito",
          "example": "Gli anziani iniziavano i giovani ai misteri della loro comunità.",
          "cefr": "C1",
          "frames": [
            "iniziare qualcuno ai misteri di un culto"
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

## 8. continuare

**use with corrections** — Keep the three readings, clarify the constructions, and retain the useful present-tense stress note in one clause.

The original infinitive has the documented hiatus in *nu-à*. The assisted construction is visible in *ha continuato a correre*, while the duration example gives *la pioggia è continuata*. Resuming yesterday’s work fits the first sense. The extension of a road is independently useful and has a spatial complement. The revised frames also allow *continuare negli studi*. [De Mauro](https://dizionario.internazionale.it/parola/continuare) · [Treccani](https://www.treccani.it/vocabolario/continuare/).

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
        "note": "accento sulla i in contìnuo e contìnuano"
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
          "definition": "portare avanti un’attività già cominciata, anche dopo una pausa",
          "example": "Se continui a correre così, domani sarai stanchissimo.",
          "cefr": "A2",
          "frames": [
            "continuare qualcosa",
            "continuare a + infinito",
            "continuare in qualcosa"
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
          "definition": "durare nel tempo oltre un certo momento",
          "example": "La pioggia è continuata per tutta la notte.",
          "cefr": "B1",
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
          "definition": "estendersi ancora nello spazio, andare oltre un certo punto",
          "example": "Oltre il ponte la strada continua fino al mare.",
          "cefr": "B1",
          "grammar": {
            "valency": "Intr"
          },
          "frames": [
            "continuare oltre qualcosa",
            "continuare fino a un luogo"
          ]
        }
      ]
    }
  ]
}
```

## 9. recente

**use with corrections** — The modern adjective is sound. Add the attested older spelling *ricente* and give *di recente* a frame.

The open *è* in *recènte* and the opposite *remoto* are supported. Creation, birth and occurrence share one temporal meaning. *Di recente* means “recently” and belongs among the frames here. The chronologically later phase of an archaeological period is a specialized application. The shorter definition keeps the original example. [De Mauro](https://dizionario.internazionale.it/parola/recente) · [Treccani](https://www.treccani.it/vocabolario/recente/).

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
          "definition": "avvenuto, nato o fatto da poco tempo",
          "example": "Secondo uno studio recente, i giovani leggono sempre meno giornali.",
          "cefr": "B1",
          "antonyms": [
            {
              "lemma": "remoto",
              "pos": "ADJ"
            }
          ],
          "frames": [
            "di recente"
          ]
        }
      ],
      "spellings": [
        {
          "form": "recente",
          "varieties": [
            "it-IT"
          ]
        },
        {
          "form": "ricente",
          "varieties": [
            "it-IT"
          ],
          "pre_reform": true
        }
      ]
    }
  ]
}
```

## 10. strano

**use with corrections** — The draft misses eccentric behavior and the established masculine noun in *lo strano è che…*.

An unusual event and an eccentric person warrant separate adjective examples. The masculine singular noun names the puzzling feature of a situation. Keep *sentirsi strano* within ordinary adjective use. Its temporary physical or emotional reading does not require a third adjective entry here. The English-based comparison with *stranger* does little to explain an Italian construction and is optional. [De Mauro](https://dizionario.internazionale.it/parola/strano) · [Treccani](https://www.treccani.it/vocabolario/strano/).

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
            "sembrare strano a qualcuno",
            "sentirsi strano"
          ],
          "antonyms": [
            {
              "lemma": "normale",
              "pos": "ADJ"
            }
          ]
        },
        {
          "definition": "che pensa o si comporta in modo insolito",
          "example": "I vicini lo trovano strano perché esce soltanto di notte.",
          "cefr": "B1"
        }
      ]
    },
    {
      "lemma": "strano",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {
        "gender": "Masc",
        "number": "Sing"
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
          "definition": "aspetto di una situazione che sorprende o sembra difficile da spiegare",
          "example": "Lo strano è che nessuno ha sentito il campanello.",
          "cefr": "B2",
          "frames": [
            "lo strano è che + frase",
            "lo strano di qualcosa"
          ]
        }
      ]
    }
  ]
}
```

## 11. europeo

**do not use** — The sports gloss excludes individual athletes, the *Plur* tag overstates the restriction, and the EU example uses a proper name.

The continent and its political union have different scopes. A budget measure or a directive would illustrate the institutional reading without *Parlamento europeo*. The geography gloss relies on a list of countries despite the rule against examples inside definitions. For the person noun, *un europeo / un’europea* changes ending, so *Com* would also be wrong. In sport, dictionaries record *gli Europei*, while the Italian football federation also uses *vincere l’Europeo*. Individual sports such as swimming belong in the definition. A rewrite should also account for *le europee* in the electoral sense under the project’s treatment of ellipsis. This is an unnecessarily complex model for the two-part-of-speech slot. [De Mauro](https://dizionario.internazionale.it/parola/europeo) · [Treccani](https://www.treccani.it/vocabolario/europeo/) · [FIGC singular attestation](https://www.figc.it/it/nazionali/news/chiesa-e-barella-doppio-obiettivo-battere-grecia-e-bosnia-e-poi-vincere-leuropeo-ikouese8) · [Olivetti on *le Europee*](https://www.dizionario-italiano.it/dizionario-italiano.php?lemma=EUROPEO100).

## 12. minimo

**use with corrections** — Use an actual lower limit for the first adjective sense and add the everyday noun for an engine’s idle speed.

An age limit can be reached exactly. *Non avere la minima idea* instead illustrates the emphatic sense “even the slightest.” Keep that expression under the second adjective sense. For the noun, compare a minimum of eight participants with a motor that *non tiene il minimo*. *Come minimo* stays a frame for a lower bound. [De Mauro](https://dizionario.internazionale.it/parola/minimo) · [Treccani](https://www.treccani.it/vocabolario/minimo/).

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
          "definition": "più piccolo o più basso tra quelli possibili, richiesti o consentiti",
          "example": "L’età minima per partecipare al corso è di sedici anni.",
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
          "cefr": "B2",
          "frames": [
            "non avere la minima idea di qualcosa"
          ]
        }
      ]
    },
    {
      "lemma": "minimo",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {
        "gender": "Masc",
        "countability": "Both"
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
          "example": "Per aprire il corso serve un minimo di otto iscritti.",
          "cefr": "B1",
          "frames": [
            "un minimo di qualcosa",
            "come minimo",
            "ridurre qualcosa al minimo"
          ],
          "antonyms": [
            {
              "lemma": "massimo",
              "pos": "NOUN"
            }
          ],
          "grammar": {
            "countability": "Count"
          }
        },
        {
          "definition": "regime di un motore acceso quando non si preme l’acceleratore",
          "example": "Al semaforo il motore non tiene il minimo e si spegne.",
          "cefr": "B2",
          "frames": [
            "girare al minimo",
            "tenere il minimo"
          ],
          "grammar": {
            "countability": "Mass"
          }
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

## 13. bellezza

**do not use** — The emphatic “quantity” reading occurs in *la bellezza di*, and the pronunciation spells out the doubled *l* against the house convention.

Preserve the mass/count distinction between a quality and a person or thing admired. *Le bellezze artistiche* is a natural plural use, while *il tuo giardino è una bellezza* would demonstrate the singular count meaning more directly. Make room for beauty perceived in music or in a generous act. Treat *la bellezza di + quantità* as a fixed emphatic construction and retain *che bellezza!* as a frame. The shape of the house IPA would be /beˈlːettsa/, with a long lateral and a doubled affricate stop. [De Mauro](https://dizionario.internazionale.it/parola/bellezza) · [Treccani](https://www.treccani.it/vocabolario/bellezza/).

## 14. addirittura

**use with corrections** — Group the surprised reply with the emphatic adverb and remove the obsolete “directly” reading from the modern learner senses.

The submitted reply consists of three sentences. Its conversational force can be shown with the frame *addirittura!* and one ordinary example for the underlying adverb. The stronger reading in *addirittura assurdo* fits the same broad emphasis. De Mauro marks the literal directional reading obsolete, and Treccani records the old spelling *a dirittura*. The latter is retained as spelling information. [De Mauro](https://dizionario.internazionale.it/parola/addirittura) · [Treccani](https://www.treccani.it/vocabolario/addirittura/).

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
          "ipa": "/adːi.riˈtːu.ra/",
          "reading": "addirittùra"
        }
      ],
      "senses": [
        {
          "definition": "perfino o davvero, con sorpresa per qualcosa che supera le aspettative",
          "example": "Per aiutarmi ha addirittura rinunciato al suo giorno libero.",
          "cefr": "B1",
          "frames": [
            "addirittura!"
          ]
        }
      ],
      "spellings": [
        {
          "form": "addirittura",
          "varieties": [
            "it-IT"
          ]
        },
        {
          "form": "a dirittura",
          "varieties": [
            "it-IT"
          ],
          "pre_reform": true
        }
      ]
    }
  ]
}
```

## 15. malattia

**use with corrections** — Extend the literal meaning to plants and give sick leave its own example alongside the figurative readings.

The first definition can cover human, animal and plant health. Sick leave has a distinct workplace meaning, as in *essere in malattia*. The football example clearly illustrates an obsession. A separate social reading explains *una malattia delle istituzioni*. The “social ill” metaphor does not carry an informal label. [De Mauro](https://dizionario.internazionale.it/parola/malattia) · [Treccani](https://www.treccani.it/vocabolario/malattia/).

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
          "definition": "disturbo che danneggia la salute di una persona, un animale o una pianta",
          "example": "Questa malattia colpisce soprattutto le foglie più giovani.",
          "cefr": "A2",
          "frames": [
            "avere una malattia",
            "prendere una malattia",
            "una malattia del cuore"
          ],
          "antonyms": [
            {
              "lemma": "salute",
              "pos": "NOUN"
            }
          ]
        },
        {
          "definition": "periodo di assenza dal lavoro per motivi di salute",
          "example": "La mia collega è in malattia fino a venerdì.",
          "cefr": "B1",
          "frames": [
            "essere in malattia",
            "mettersi in malattia",
            "giorni di malattia"
          ],
          "grammar": {
            "countability": "Mass"
          }
        },
        {
          "definition": "passione così forte da diventare quasi un’ossessione",
          "example": "Per lui il calcio è una vera malattia.",
          "cefr": "B2",
          "labels": {
            "figurative": true
          }
        },
        {
          "definition": "problema grave che indebolisce una società o un sistema",
          "example": "La corruzione è una malattia che indebolisce le istituzioni.",
          "cefr": "C1",
          "labels": {
            "figurative": true
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

## 16. pressione

**use with corrections** — Distinguish an ordinary push, force per unit area, blood pressure, influence on a decision, and the strain someone feels.

A push can come from an object as well as from a finger. The measured physical quantity applies to solids as well as fluids and depends on area. Weather expressions fit that measurement. Everyday *misurarsi la pressione* conventionally refers to blood pressure. The new example for social influence shows who is expected to act. The idiom *essere sotto pressione* belongs with experienced strain. [De Mauro](https://dizionario.internazionale.it/parola/pressione) · [Treccani](https://www.treccani.it/vocabolario/pressione/).

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
          "definition": "spinta esercitata su qualcosa con la mano, uno strumento o un peso",
          "example": "Basta una leggera pressione sul tasto per accendere la radio.",
          "cefr": "B1",
          "frames": [
            "fare pressione su qualcosa",
            "esercitare una pressione su qualcosa"
          ],
          "grammar": {
            "countability": "Both"
          }
        },
        {
          "definition": "forza del sangue contro le pareti delle arterie",
          "example": "Mio nonno si misura la pressione ogni mattina dopo colazione.",
          "cefr": "B1",
          "frames": [
            "avere la pressione alta o bassa",
            "misurarsi la pressione"
          ],
          "grammar": {
            "countability": "Mass"
          }
        },
        {
          "definition": "insistenza o influenza usata per spingere qualcuno ad agire",
          "example": "Hanno fatto pressione sul direttore perché cambiasse la sua decisione.",
          "cefr": "B2",
          "frames": [
            "fare pressione su qualcuno",
            "esercitare pressioni su qualcuno",
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
            "essere sotto pressione",
            "mettere qualcuno sotto pressione"
          ],
          "grammar": {
            "countability": "Mass"
          },
          "labels": {
            "figurative": true
          }
        },
        {
          "definition": "forza esercitata perpendicolarmente su una superficie, divisa per la sua area",
          "example": "Se gonfi ancora la ruota, la pressione dell’aria aumenta.",
          "cefr": "B2",
          "grammar": {
            "countability": "Mass"
          },
          "frames": [
            "pressione atmosferica",
            "alta pressione",
            "bassa pressione"
          ],
          "labels": {
            "domain": "physics"
          }
        }
      ]
    }
  ]
}
```

## 17. immediatamente

**use as it is** — The alarm example shows urgency, and the pharmacy example shows spatial adjacency. Both match the definitions and frames.

The second sense also works for an ordered list or one moment following another. These are applications of the same “nothing in between” meaning. The marked reading, IPA and relation to *immediato* fit the brief. [De Mauro](https://dizionario.internazionale.it/parola/immediatamente).

## 18. attualmente

**use as it is** — One present-time sense, a natural employment example, and the derivation from *attuale* make a complete everyday entry.

The hiatus in *tu-al-* is documented. The gloss works for a current period as well as a particular moment. English *actually* provides no additional Italian sense here. A specialist philosophical meaning can be left out under the brief. [De Mauro](https://dizionario.internazionale.it/parola/attualmente) · [Olivetti pronunciation](https://www.dizionario-italiano.it/dizionario-italiano.php?parola=attualmente).

## Five entries to show the writing model, best first

Use the corrected JSON blocks above for all five.

| Rank | Entry | Coverage | What the example teaches |
|---:|---|---|---|
| 1 | [museo](#4-museo) | Plain noun | A single gloss covers both the collection and the place a learner visits. |
| 2 | [iniziare](#7-iniziare) | Verb with frames | The contrast between *ho iniziato a leggere* and *la lezione è iniziata* shows how the construction determines the auxiliary. |
| 3 | [recente](#9-recente) | Adjective | One temporal sense covers several kinds of noun. *Di recente* shows where to put a fixed expression. |
| 4 | [minimo](#12-minimo) | Two parts of speech | Separate adjective and noun lexemes show a threshold, a very small degree, and the engine sense without duplicating entries. |
| 5 | [fenomeno](#3-fenomeno) | Labelled sense | The everyday compliment *un fenomeno con i numeri* motivates both the informal and figurative labels. |
