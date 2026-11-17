# Spanish dictionary example review

Reviewed against the supplied brief on November 17, 2026.

**Recommended five, best first: lunes; estudiar; enorme; corrected responsable; corrected genial.** The first three work as supplied. The last two must be replaced by the complete JSON below. Together they cover a plain noun, verb frames and valency, an adjective, multiple parts of speech, common noun gender, and an informal label. A regional example is optional under the brief; corrected café is available if regional metadata is a separate training priority.

The verdicts concern suitability as house-style exemplars. “Do not use” does not mean that the word should be excluded from the dictionary; it means that the candidate needs a structural or policy-level rebuild before it is safe as a demonstration.

The RAE’s *Diccionario del estudiante* supplies a concrete learner-oriented baseline, supplemented by the DPD, DLE, ASALE and FundéuRAE for disputed details. It is not a CEFR dictionary. Sense grouping and CEFR estimates below are editorial judgments, not claims that every monolingual dictionary must have identical numbering. I have not invented omissions or extra senses where none are needed, and have not measured corpus frequencies.

All 20 original entries have definitions of 6–15 words and examples of 6–14 words, counted as whitespace-separated words. Passing the length test does not establish correct meaning, POS, example structure, or regional coverage. The corrected entries were checked against the same limits and parsed as JSON. They retain the original object/lexeme/sense structure.

## Policy issues to fix before scaling

1. **Spelling versus pronunciation.** “Split whenever pronunciation differs” conflicts with allowing several pronunciations per lexeme and treating spelling variants as one entry. Distinguish pronunciation variants from homographs, and allow variant-specific sense restrictions. Período/periodo exposes this directly: menstruation belongs to the unaccented, diphthongal variant. [DPD](https://www.rae.es/dpd/per%C3%ADodo).
2. **Spanish orthography.** Do not classify every adverbial sólo as obsolete/pre-reform: current guidance permits the accent when the writer perceives ambiguity. Fué became obsolete with the rules effective in 1959, not in the 2010 change. [FundéuRAE](https://www.fundeu.es/recomendacion/solo-sin-tilde/); [Instituto Cervantes](https://cvc.cervantes.es/lengua/alhabla/museo_horrores/museo_028.htm).
3. **Pronunciation coverage.** /ʎ/ can be an explicitly chosen non-yeísta model, but should not be presented as the single general modern pronunciation of Spain. Yeísmo is widespread and standard. Stress placement also cannot be derived from written accent rules alone: -mente adverbs preserve two stress sites. Neither .ˈ nor .ˌ in the supplied IPA is itself an error; a stress mark can serve as the syllable boundary, so forms without a preceding dot are also acceptable. Normalize that display convention globally if desired. [DPD, yeísmo](https://www.rae.es/dpd/ye%C3%ADsmo); [DPD, tilde](https://www.rae.es/dpd/tilde).
4. **Regions versus model varieties.** The four supported spelling/morphology varieties do not delimit where Spanish senses exist. The later Regions rule expressly admits country and area codes such as es-419. Use that richer vocabulary for sense distribution and provide a way to scope regional frames without inventing new senses.
5. **Forms.** The full eleven-cell requirement for irregular verbs conflicts with “list only what is irregular.” Here the explicit cell list takes precedence. The format also needs construction-specific restrictions for electo and a clear policy for syncretic cells: combining features independently must not accidentally license nonexistent mood/tense/person combinations. [DPD, elegir](https://www.rae.es/dpd/elegir).
6. **Pronominal grammar.** Refl does not distinguish true reflexives, accidental/inchoative pronominals, reciprocal uses, and emphatic datives. Use the existing grammar.note for such distinctions or extend the schema before making matar a teaching example.
7. **Scope and sense counts.** The requested labelled/regional coverage is compatible with ordinary regional words; it is not permission to import every specialized or narrow local dictionary meaning. Nor does “no shades” mean merging established core meanings. Keep explicit decisions about learner scope separate from factual claims.

## Verdicts

| # | Candidate | Verdict | Main reason |
|---|---|---|---|
| 1 | gobernador | **use with corrections** | The institutional definition is too general, and the adjective lexeme is missing. |
| 2 | hotel | **use with corrections** | The accommodation sense works, but a documented house/chalet sense is absent. |
| 3 | período | **do not use** | The menstruation sense is attached to the wrong pronunciation, and the variant policy cannot represent it safely. |
| 4 | lunes | **use as it is** | A clear, accurate single-sense noun with useful singular/plural usage. |
| 5 | compartir | **use with corrections** | The first sense merges two core meanings while the later senses partly repeat them. |
| 6 | participar | **use with corrections** | Add receiving a share and clarify the constructions and their regional use. |
| 7 | elegir | **use with corrections** | The listed forms are correct, but the American passive electo and useful complement patterns are missing. |
| 8 | estudiar | **use as it is** | The three senses and their transitive/intransitive behavior are accurate and well illustrated. |
| 9 | enorme | **use as it is** | One appropriately broad adjective sense covers size, amount, and intensity. |
| 10 | genial | **use with corrections** | The common adverb is acknowledged but incorrectly hidden inside adjective metadata. |
| 11 | directo | **do not use** | A false grammar definition, missing ordinary uses, and a missing adverb make this a poor template. |
| 12 | responsable | **use with corrections** | Responsibility does not necessarily imply wrongdoing, a bad result, or payment. |
| 13 | concepto | **use with corrections** | The payment example narrows a broader “aspect/heading” sense and does not justify an official label. |
| 14 | matar | **do not use** | The entry combines distinct pronominal meanings, omits common uses, and misattributes muerto. |
| 15 | café | **use with corrections** | The color geography is too narrow, the noun color is missing, and the material definition is incomplete. |
| 16 | recién | **do not use** | Construction differences are presented as meanings, and the American distribution is incorrectly restricted. |
| 17 | ojo | **use with corrections** | Add caution as a noun, broaden the aperture meaning, and mark abstract uses as mass nouns. |
| 18 | debajo | **use with corrections** | The core senses work, but the contrast with abajo should acknowledge normal American usage. |
| 19 | exactamente | **use with corrections** | The confirmation is a discourse use of the same adverb, and its dialogue breaks the one-sentence rule. |
| 20 | principalmente | **use with corrections** | The definition’s “por ... razón” wording is narrower than the general meaning “chiefly.” |

## Candidate-by-candidate review

### 1. gobernador

**use with corrections** — The institutional definition is too general, and the adjective lexeme is missing.

**Facts:** Masculine noun gender, the feminine note, pronunciations, and the political use are sound. The institutional title is not a general name for every important public-institution director.

**Senses:** Keep the territorial and institutional noun senses. Add the adjective meaning “ruling/in charge”; the noun-only heading does not override the instruction to return all eligible parts of speech. No existing noun sense is merely a redundant shade.

**Style:** Both original definitions and examples meet the length limits. The correction narrows the institutional title and adds a short adjective definition without using the headword family. B1/C1 are defensible estimates.

Source: [RAE, gobernador](https://temporalweb.asale.org/diccionario-estudiante/gobernador).

Complete corrected entry:

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
          "definition": "persona que ocupa el cargo superior de ciertos bancos u otras instituciones públicas",
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
    },
    {
      "lemma": "gobernador",
      "pos": "ADJ",
      "kind": "word",
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
          "definition": "que dirige un territorio, una comunidad o una institución",
          "example": "La reina gobernadora firmó el decreto aquella mañana.",
          "cefr": "C1"
        }
      ],
      "relations": [
        {
          "type": "adjective_of",
          "lemma": "gobernar",
          "pos": "VERB"
        }
      ]
    }
  ]
}
```

### 2. hotel

**use with corrections** — The accommodation sense works, but a documented house/chalet sense is absent.

**Facts:** Gender, pronunciation, regular plural, and A1 for paid accommodation are correct. “Establecimiento” identifies the business more accurately than “edificio.”

**Senses:** The student dictionary also numbers a private-house/chalet meaning, often encountered in the diminutive. It is not just a type of commercial accommodation. Include it for the stated books-and-ordinary-text remit; do not invent separate senses for the hotel building and business. The added C1 estimate concerns this less frequent meaning.

**Style:** The original lengths and example are good. The added example makes the residential use explicit. No unsupported region or dated label has been added.

Source: [RAE, hotel](https://temporalweb.asale.org/diccionario-estudiante/hotel).

Complete corrected entry:

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
          "definition": "establecimiento que ofrece habitaciones y otros servicios a cambio de dinero",
          "example": "¿Puedes recomendarme un hotel barato cerca de la estación?",
          "cefr": "A1"
        },
        {
          "definition": "casa para una familia, generalmente rodeada de un jardín",
          "example": "Aquel pequeño hotel era una vivienda familiar, sin huéspedes.",
          "cefr": "C1"
        }
      ]
    }
  ]
}
```

### 3. período

**do not use** — The menstruation sense is attached to the wrong pronunciation, and the variant policy cannot represent it safely.

**Facts:** For a time span, both **período** /peˈɾi.o.do/ and **periodo** /peˈɾjo.do/ are accepted. The DPD specifically assigns menstruation to the latter, unaccented form. The entry supplies only the former pronunciation while permitting both spellings for both senses. Menstruation is a recurring physiological process/bleeding, not simply a quantity of blood.

**Senses:** Time span and menstruation are genuinely different. Technical period meanings need not be imported into this common entry. There is no redundant numbered sense, but the second does not belong to the accentuated variant as presented.

**Style:** Lengths pass. The menstruation example should use “periodo.” Repair the spelling/pronunciation/sense linkage in the schema before using this candidate as a model; adding an unlinked second IPA is insufficient.

Source: [RAE–ASALE, período/periodo](https://www.rae.es/dpd/per%C3%ADodo).

### 4. lunes

**use as it is** — A clear, accurate single-sense noun with useful singular/plural usage.

**Facts:** Masculine count noun, invariant plural, both pronunciations, and A1 are correct. It is not a plural-only noun.

**Senses:** No common independent meaning is missing; “el lunes” and habitual “los lunes” are grammatical uses, not separate senses.

**Style:** The definition is 13 words and the example 8. Both are natural. The invariant plural is useful explicit information for the generator even though it follows the general rule for unstressed words ending in -s. The note and form duplicate that information harmlessly.

### 5. compartir

**use with corrections** — The first sense merges two core meanings while the later senses partly repeat them.

**Facts:** Regular third-conjugation transitive verb; pronunciation and frame are correct.

**Senses:** Distinguish having/using something in common from giving others a share or access. Agreement in opinions belongs to the former; sharing a link is a contemporary application of the latter. The original first sense combines both, then makes narrower applications into additional numbered senses. No additional common core meaning needs inventing.

**Style:** Original lengths pass. The online example is natural, but its “en el grupo” did not illustrate the supplied “con alguien” frame; the replacement does. Keep examples of opinions and distributing food as additional usage material if the format later permits them.

Source: [RAE, compartir](https://temporalweb.asale.org/diccionario-estudiante/compartir).

Complete corrected entry:

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
          "definition": "tener o usar algo junto con una o más personas",
          "example": "Durante la carrera compartí piso con dos estudiantes de medicina.",
          "cefr": "A2",
          "frames": [
            "compartir algo con alguien"
          ]
        },
        {
          "definition": "dar a otras personas parte de algo o acceso a ello",
          "example": "¿Puedes compartir el enlace con el grupo de la clase?",
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

### 6. participar

**use with corrections** — Add receiving a share and clarify the constructions and their regional use.

**Facts:** The transitive/intransitive distinction is correct. Taking part normally uses “en”; American Spanish also permits “de.” Communicating a message is transitive and also permits a que-clause. C1 is sufficient for that formal use; C2 was not an error of more than one level.

**Senses:** Receiving a share, as in profits, is missing and is separately numbered in the student dictionary. Retain sharing an opinion/quality and notifying somebody. The specialized corporate-ownership use can be omitted under the brief’s technical-sense exclusion. None of the retained four is merely a subject-area duplicate.

**Style:** All original lengths pass. The revised second abstract definition removes the appended synonym “compartir”; formal labels apply to sharing qualities/opinions in this construction and to notification, not to participation generally.

Sources: [RAE, participar](https://www.rae.es/diccionario-estudiante/participar); [RAE–ASALE, construction guidance](https://www.rae.es/dpd/participar).

Complete corrected entry:

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
            "valency": "Intr",
            "note": "participar de algo is also used in American Spanish"
          }
        },
        {
          "definition": "recibir una parte de algo que se reparte entre varias personas",
          "example": "Todos los trabajadores participan de los beneficios de la empresa.",
          "cefr": "B2",
          "frames": [
            "participar de algo",
            "participar en algo"
          ],
          "grammar": {
            "valency": "Intr"
          }
        },
        {
          "definition": "tener una opinión, un sentimiento o una cualidad en común con otros",
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
          "cefr": "C1",
          "frames": [
            "participar algo a alguien",
            "participar a alguien que + oración"
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

### 7. elegir

**use with corrections** — The listed forms are correct, but the American passive electo and useful complement patterns are missing.

**Facts:** All eleven supplied forms are correctly spelled. Add **electo**, restricted to the election sense and American passive constructions; **elegido** remains the general participle. The three American variety codes are the American varieties supported by this specification, not a claim that other American countries lack the use. Perfective aspect makes the preterite forms unambiguous.

**Senses:** Choosing and electing to office are defensible separate senses. Choosing to do something is a complement pattern, not a third meaning. No common extra sense is missing.

**Style:** Replace “tomar ... porque se prefiere”: choice need not imply preference. Clarify personal a, infinitive complements, and the optional como/para constructions. Keep the explicitly requested paradigm cells despite the contradictory general “only irregular forms” instruction; see the policy notes.

Source: [RAE–ASALE, elegir](https://www.rae.es/dpd/elegir).

Complete corrected entry:

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
        "note": "e→i stem change; g→j before a and o",
        "by_variety": {
          "es-MX": {
            "note": "electo also occurs in passive constructions in sense 2"
          },
          "es-AR": {
            "note": "electo also occurs in passive constructions in sense 2"
          },
          "es-CO": {
            "note": "electo also occurs in passive constructions in sense 2"
          }
        }
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
            "VerbForm": "Fin",
            "Aspect": "Perf"
          }
        },
        {
          "form": "eligió",
          "features": {
            "Mood": "Ind",
            "Tense": "Past",
            "Person": "3",
            "Number": "Sing",
            "VerbForm": "Fin",
            "Aspect": "Perf"
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
        },
        {
          "form": "electo",
          "features": {
            "Tense": "Past",
            "VerbForm": "Part"
          },
          "varieties": [
            "es-MX",
            "es-AR",
            "es-CO"
          ],
          "senses": [
            2
          ]
        }
      ],
      "senses": [
        {
          "definition": "escoger una persona o cosa entre varias posibilidades",
          "example": "Puedes elegir el color que más te guste.",
          "cefr": "A2",
          "frames": [
            "elegir algo o a alguien",
            "elegir entre varias opciones",
            "elegir + infinitivo"
          ]
        },
        {
          "definition": "nombrar por votación a una persona para un cargo",
          "example": "Los vecinos eligieron a mi hermana como alcaldesa.",
          "cefr": "B1",
          "frames": [
            "elegir a alguien (como) + cargo",
            "elegir a alguien para un cargo"
          ]
        }
      ]
    }
  ]
}
```

### 8. estudiar

**use as it is** — The three senses and their transitive/intransitive behavior are accurate and well illustrated.

**Facts:** Regular first-conjugation verb; /es.tuˈdjaɾ/ is correct. The sense-level valencies match the examples.

**Senses:** Learning material, receiving formal instruction, and examining a proposal are established distinctions. No common independent sense is missing, and these are not simply three subject areas. Optional “me estudio el tema” is an aspectual/pragmatic construction, not a new meaning.

**Style:** All definitions and examples pass the limits, use appropriate infinitives, and show the intended meanings. A1/A1/B1 are reasonable estimates. The third sense could also display “estudiar algo,” but omitting that predictable frame is not a material defect.

Source: [RAE, estudiar](https://temporalweb.asale.org/diccionario-estudiante/estudiar).

### 9. enorme

**use as it is** — One appropriately broad adjective sense covers size, amount, and intensity.

**Facts:** Pronunciation, common-gender adjective behavior, and regular plural are correct. “Diminuto” is a clear opposite for physical size, although not for every collocation.

**Senses:** No common extra sense is required. An enormous park, sum, and joy should not automatically become three senses. The student dictionary likewise treats the emphatic extensions together.

**Style:** The definition is 12 words and the example 11; both work. The note spelling out regular “enormes” is unnecessary but not false; a global policy may omit such notes consistently. A2 is appropriate.

Source: [RAE, enorme](https://temporalweb.asale.org/diccionario-estudiante/enorme).

### 10. genial

**use with corrections** — The common adverb is acknowledged but incorrectly hidden inside adjective metadata.

**Facts:** The pronunciations are correct; the dot before the stress mark is not a phonological error. “Pasarlo genial” is adverbial and needs a separate ADV lexeme under the given rules.

**Senses:** Keep informal “excellent” and exceptional talent/brilliance under ADJ, then add informal “very well” under ADV. Applying brilliance to a person or an idea need not create two senses under the brief’s no-shades policy. No additional common independent meaning is needed.

**Style:** All original lengths pass. The revised talent definition explicitly covers possessing and displaying talent. The new adverb has an adverbial definition and an example of its own frame. The informal labels should remain.

Source: [RAE, genial](https://temporalweb.asale.org/diccionario-estudiante/genial).

Complete corrected entry:

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
        "note": "same form for masculine and feminine"
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
          "definition": "que tiene o demuestra un talento o una inteligencia extraordinarios",
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
    },
    {
      "lemma": "genial",
      "pos": "ADV",
      "kind": "word",
      "grammar": {
        "invariable": true
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
          "definition": "muy bien o de una manera muy agradable",
          "example": "Lo pasamos genial durante las vacaciones de verano.",
          "cefr": "A2",
          "labels": {
            "formality": "informal"
          },
          "frames": [
            "pasarlo genial",
            "sentar genial a alguien"
          ]
        }
      ]
    }
  ]
}
```

### 11. directo

**do not use** — A false grammar definition, missing ordinary uses, and a missing adverb make this a poor template.

**Facts:** A Spanish direct object can take personal **a**: “Vi a mi hermana.” It is therefore false to define it through absence of a preposition. The definition also describes a noun phrase while the lexeme is ADJ. “Complemento directo” belongs to its own multiword entry under the brief. Pronunciation and the boxing noun itself are sound.

**Senses:** Missing are the adverb in “ir directo a casa” and the noun for a live broadcast/stream; live performance is also established. Conversely, the boxing sense is specialized, conflicting with the brief’s exclusion of specialist senses under common words. The first adjective definition also bundles straightness and a journey without intermediate stops; learner dictionaries can distinguish them.

**Style:** Lengths pass, but “en gramática” and “en boxeo” are usage explanations inside definitions. This needs a structural rebuild, not a small exemplar correction.

Sources: [RAE, directo](https://temporalweb.asale.org/diccionario-estudiante/directo); [DLE, broadcast and performance nouns](https://dle.rae.es/directo).

### 12. responsable

**use with corrections** — Responsibility does not necessarily imply wrongdoing, a bad result, or payment.

**Facts:** The noun correctly has common gender, **el/la responsable**. The causal adjective need not refer to an accountable person and certainly does not entail paying. Its revised example uses an inanimate cause.

**Senses:** Retain obligation/accountability, conscientious behavior, and causation as useful distinctions. The noun for a person answerable for an event is too narrowly limited to criminals in the original; broaden it. The managerial noun is useful separately. Being responsible for a good result is covered by causation, not a fourth adjective meaning.

**Style:** Original lengths and naturalness are good. The replacements remove false restrictions without adding an evaluative label. B1/B2 estimates are acceptable. Dictionary treatments vary in how they group accountability and causation; there is no uniquely mandatory count.

Source: [RAE, responsable](https://temporalweb.asale.org/diccionario-estudiante/responsable).

Complete corrected entry:

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
          "definition": "que causa un hecho o influye de manera decisiva en él",
          "example": "La falta de lluvia es responsable de esta mala cosecha.",
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
          "definition": "persona que tiene a su cargo una actividad, un grupo o un área",
          "example": "Hable usted con la responsable de ventas, por favor.",
          "cefr": "B1",
          "frames": [
            "el/la responsable de algo"
          ]
        },
        {
          "definition": "persona que debe responder de un hecho o de sus consecuencias",
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

### 13. concepto

**use with corrections** — The payment example narrows a broader “aspect/heading” sense and does not justify an official label.

**Facts:** Gender, pronunciation, and the first two meanings are sound. The third meaning is not restricted to money.

**Senses:** Keep concept/idea, opinion, and an aspect or heading considered. A line item on an invoice is an application of the third; do not add a separate financial sense. “En concepto de” can remain a useful frame, but should not supply the entire sense definition. No extra common core meaning is needed after broadening it.

**Style:** Original lengths pass. The replacement example demonstrates the noun itself. Remove “official”: an ordinary invoice or general aspect is not inherently official language. B2 is a reasonable estimate for the broadened sense.

Source: [RAE, concepto](https://temporalweb.asale.org/diccionario-estudiante/concepto).

Complete corrected entry:

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
          "definition": "aspecto concreto que se considera al valorar o describir algo",
          "example": "La factura incluye varios conceptos que no entiendo bien.",
          "cefr": "B2",
          "frames": [
            "en concepto de algo",
            "por este concepto"
          ]
        }
      ]
    }
  ]
}
```

### 14. matar

**do not use** — The entry combines distinct pronominal meanings, omits common uses, and misattributes muerto.

**Facts:** Killing applies to plants and other living organisms too. The historical/literary passive “ser muerto” belongs to a transitive use of **morir**; it should not teach **muerto** as a participle in the regular **matar** paradigm. Accidental “matarse” is not literal reflexive self-killing, even though the schema offers only an imperfect Refl shorthand.

**Senses:** Accidental death and intentionally killing oneself should not be glossed as one undifferentiated meaning. Ending or reducing something, as in hunger, is absent; figurative suffering/annoyance can be grouped. “Matar el tiempo” is important, but the brief’s phrase policy calls for an appropriate frame or separate multiword entry rather than indiscriminate sense inflation. Intensive effort is legitimate and must remain distinct from death.

**Style:** All lengths pass; the hunting example is grammatical but overlong for the simple core meaning. A careful rebuild is necessary before this can model pronominal grammar.

Sources: [RAE, matar](https://temporalweb.asale.org/diccionario-estudiante/matar); [FundéuRAE, ser muerto](https://www.fundeu.es/consulta/muerto-asesinado/).

### 15. café

**use with corrections** — The color geography is too narrow, the noun color is missing, and the material definition is incomplete.

**Facts:** Coffee need not be hot. Ground coffee is included, not just whole seeds. The color is widespread in America, not exclusive to Mexico and Colombia; use **es-419, mainly**, following the DLE. Both “ojos café” and “ojos cafés” are accepted. The original **Europa** violates the no-proper-names example rule.

**Senses:** Retain beverage, plant/material, establishment, and add the color noun. A served cup is the beverage’s count use, not an extra sense. The plant and seeds are grouped in keeping with the brief’s plant/crop policy. Do not split the adjective into “a color” versus “an object of that color.” The regional reprimand use is excluded under the brief’s narrow common-vocabulary remit.

**Style:** All original lengths pass. Add only the invariant adjective plural to forms; regular cafés is generated. The revised definitions avoid café/cafeto and the new examples contain no proper names.

Sources: [DLE, café](https://dle.rae.es/caf%C3%A9); [ASALE, American distribution](https://www.asale.org/damer/caf%C3%A9); [FundéuRAE, color plurals](https://www.fundeu.es/recomendacion/dia-internacional-del-cafe-claves-de-redaccion/).

Complete corrected entry:

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
          "definition": "bebida oscura preparada con agua y semillas tostadas y molidas de un arbusto tropical",
          "example": "¿Quieres tomar un café conmigo esta tarde?",
          "cefr": "A1",
          "frames": [
            "café con leche",
            "café solo"
          ]
        },
        {
          "definition": "arbusto tropical y sus semillas, que se tuestan y muelen para preparar una bebida",
          "example": "En esta tienda venden café en grano y molido.",
          "cefr": "A2"
        },
        {
          "definition": "local donde se sirven bebidas y comidas ligeras",
          "example": "Nos vemos en el café de la esquina a las cinco.",
          "cefr": "A2",
          "grammar": {
            "countability": "Count"
          }
        },
        {
          "definition": "color oscuro entre el amarillo y el rojo, parecido al de muchas maderas",
          "example": "Prefiero el café oscuro para pintar esta puerta.",
          "cefr": "A2",
          "regions": [
            {
              "variety": "es-419",
              "extent": "mainly"
            }
          ],
          "grammar": {
            "countability": "Both"
          }
        }
      ]
    },
    {
      "lemma": "café",
      "pos": "ADJ",
      "kind": "word",
      "grammar": {
        "note": "same form for both genders; plural café or cafés"
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
              "variety": "es-419",
              "extent": "mainly"
            }
          ]
        }
      ],
      "forms": [
        {
          "form": "café",
          "features": {
            "Number": "Plur"
          }
        }
      ]
    }
  ]
}
```

### 16. recién

**do not use** — Construction differences are presented as meanings, and the American distribution is incorrectly restricted.

**Facts:** The listed IPA forms and derivation from reciente are sound. The recent-past use with a finite verb extends across much of America. “Only now/not until” also occurs outside Argentina. Before participial material, recién can accompany some adjectives expressing a completed action, such as limpio or despierto, not just morphological participles.

**Senses:** Original senses 1 and 2 share the same temporal meaning; they differ in construction and distribution. Merge those semantically while retaining the regional grammar. The “only then/not until” meaning is distinct. A further American temporal use meaning “as soon as” is missing and requires a conscious POS/construction policy.

**Style:** Lengths and examples are good, but “usado delante de un participio” is grammar placed in the definition. Fix the policy for regional frames first; splitting one meaning merely to attach regional labels would train the wrong sense-granularity rule.

Source: [RAE–ASALE, recién](https://www.rae.es/dpd/reci%C3%A9n).

### 17. ojo

**use with corrections** — Add caution as a noun, broaden the aperture meaning, and mark abstract uses as mass nouns.

**Facts:** The organ is not universally located in a “face,” and the listed countability cannot cover “mucho ojo/buen ojo” without overrides. The interjection is a defensible separate POS under this schema; the DLE recognizes it, although some learner treatments describe it as an exclamative noun use.

**Senses:** Missing are caution/attention and the familiar opening of a bridge. The aperture meaning extends beyond needles to locks and scissors. Do not create separate senses for each tool or for eye color versus the organ. Keep discernment distinct from caution. Numerous technical or fixed-expression meanings remain outside scope.

**Style:** Original lengths pass. The revised interjection example now illustrates “ojo con.” The added abstract-noun sense and sense-specific countability prevent a misleading all-Count template. Examples remain short and natural.

Sources: [RAE, ojo](https://temporalweb.asale.org/diccionario-estudiante/ojo); [DLE, ojo](https://dle.rae.es/ojo).

Complete corrected entry:

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
        "countability": "Both"
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
          "definition": "órgano con el que las personas y muchos animales perciben luz, formas y colores",
          "example": "Mi hermana tiene los ojos verdes, como nuestra abuela.",
          "cefr": "A1",
          "grammar": {
            "countability": "Count"
          }
        },
        {
          "definition": "atención o cuidado para evitar errores, daños o peligros",
          "example": "Ten mucho ojo al cruzar esta calle tan transitada.",
          "cefr": "B1",
          "frames": [
            "tener ojo con algo",
            "tener ojo al + infinitivo",
            "andar con ojo"
          ],
          "grammar": {
            "countability": "Mass"
          },
          "labels": {
            "figurative": true
          }
        },
        {
          "definition": "capacidad especial para notar, juzgar o elegir bien algo",
          "example": "Tienes muy buen ojo para elegir regalos.",
          "cefr": "B2",
          "frames": [
            "tener (buen) ojo para algo",
            "tener (buen) ojo para + infinitivo"
          ],
          "labels": {
            "figurative": true
          },
          "grammar": {
            "countability": "Mass"
          }
        },
        {
          "definition": "agujero de ciertos objetos por donde se introduce o pasa algo",
          "example": "No consigo pasar el hilo por el ojo de la aguja.",
          "cefr": "B2",
          "grammar": {
            "countability": "Count"
          }
        },
        {
          "definition": "espacio abierto bajo un puente entre dos de sus apoyos",
          "example": "El agua pasa con fuerza por los ojos del puente.",
          "cefr": "B2",
          "grammar": {
            "countability": "Count"
          }
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
          "example": "¡Ojo con el suelo mojado de la cocina!",
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

### 18. debajo

**use with corrections** — The core senses work, but the contrast with abajo should acknowledge normal American usage.

**Facts:** Pronunciation, antonym, and the figurative frame are correct. “Abajo de” is normal in American Spanish; a binary “debajo takes de, abajo stands alone” explanation is misleading.

**Senses:** Spatial/layered location and lower level/quantity form a workable compact division. The student dictionary separately numbers the covered-by use, but it is already represented here and can remain grouped under the brief’s compact-sense policy. There is no missing common core sense. “Por debajo de cero” need not become an additional noun-like meaning.

**Style:** All original lengths and examples pass. Tighten “lugar inferior” to positional wording and revise the English confusable note; preserve the natural examples and frames.

Sources: [RAE, debajo](https://www.rae.es/diccionario-estudiante/debajo); [RAE–ASALE, abajo](https://www.rae.es/dpd/abajo).

Complete corrected entry:

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
          "definition": "en una posición inferior respecto de algo, o cubierto por ello",
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
          "why": "abajo means down or in a lower place; debajo relates a position to something above or covering it. abajo de is also normal in American Spanish."
        }
      ]
    }
  ]
}
```

### 19. exactamente

**use with corrections** — The confirmation is a discourse use of the same adverb, and its dialogue breaks the one-sentence rule.

**Facts:** Precision and confirmation are both real uses. The two stress locations in an -mente adverb are legitimate; the secondary/primary notation is a permissible house convention.

**Senses:** The student dictionary treats confirmation as a use of the precision adverb. Follow that compact analysis, with a usage note rather than a second numbered sense defined as a “respuesta.” No common independent meaning needs adding.

**Style:** Word counts pass, but the original question-plus-answer is not one example sentence. Retain the natural first example. The relation to exacto is correct, and no related word appears in the retained definition.

Sources: [RAE, exactamente](https://temporalweb.asale.org/diccionario-estudiante/exactamente); [RAE–ASALE, -mente stress](https://www.rae.es/dpd/tilde).

Complete corrected entry:

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
        }
      ],
      "relations": [
        {
          "type": "adverb_of",
          "lemma": "exacto",
          "pos": "ADJ"
        }
      ],
      "grammar": {
        "invariable": true,
        "note": "also used alone to confirm a statement"
      }
    }
  ]
}
```

### 20. principalmente

**use with corrections** — The definition’s “por ... razón” wording is narrower than the general meaning “chiefly.”

**Facts:** Pronunciation, relation to principal, and B1 are appropriate. The extra stress in the -mente form is not an error.

**Senses:** One sense is enough. Dominance in amount, importance, place, or cause does not require separate senses. No common meaning is absent after broadening the definition.

**Style:** The original lengths and example pass. Replace only the definition so that it also fits uses such as living mainly in cities, without suggesting that principalmente must introduce a reason.

Source: [RAE, principalmente](https://www.rae.es/diccionario-estudiante/principalmente).

Complete corrected entry:

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
          "definition": "en mayor medida o con más importancia que lo demás",
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

## Final demonstration set, best first

| Rank | Entry | Version to show | What it teaches |
|---|---|---|---|
| 1 | lunes | Original | Plain noun, one sense, invariant plural, singular/habitual frames |
| 2 | estudiar | Original | Verb definitions, useful frames, three established senses, valency |
| 3 | enorme | Original | One adjective sense spanning size, quantity, and intensity |
| 4 | responsable | Corrected | Separate adjective/noun lexemes, common noun gender, semantic precision |
| 5 | genial | Corrected | Separate adjective/adverb lexemes and properly scoped informal labels |

If limited to four, omit responsable: genial still supplies the required two parts of speech. Do not show the original versions of entries marked “use with corrections.”
