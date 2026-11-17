# Portuguese learner’s dictionary review

Of the 20 candidates, 2 can be used as supplied, 14 need corrections, and 4 are unsuitable as house-style examples. The ranked teaching set follows candidate 20.

The JSON uses the requested São Paulo and Lisbon conventions. In the verb entries, **Tr** includes a selected prepositional complement, following the Brazilian dictionary treatment of *lidar com* and *acreditar em*. **Intr** covers uses without an object. CEFR levels and the ranking are editorial estimates.

## 1. computador

**use as it is** — One count noun, one everyday meaning, and a home-working example make this the cleanest baseline. The historical human calculator falls outside the ordinary learner inventory.

[Infopédia](https://www.infopedia.pt/dicionarios/lingua-portuguesa/computador)

```json
{
  "language": "pt",
  "headword": "computador",
  "lexemes": [
    {
      "lemma": "computador",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {"gender": "Masc", "countability": "Count"},
      "pronunciations": [
        {"variety": "pt-BR", "ipa": "/kõ.pu.ta.ˈdoɾ/"},
        {"variety": "pt-PT", "ipa": "/kõ.pu.tɐ.ˈdoɾ/"}
      ],
      "senses": [
        {
          "definition": "máquina que guarda e processa dados e executa programas",
          "example": "Preciso de um computador novo para trabalhar em casa.",
          "cefr": "A1"
        }
      ],
      "relations": [{"type": "derived_from", "lemma": "computar", "pos": "VERB"}]
    }
  ]
}
```

## 2. episódio

**use with corrections** — Broaden the installment sense to serialized works and distinguish a secondary passage in a narrative from a real-life incident. An incident can be routine. The supplied Brazilian /dʒju/ is defensible.

[Infopédia](https://www.infopedia.pt/dicionarios/lingua-portuguesa/epis%C3%B3dio) · [Priberam](https://dicionario.priberam.org/epis%C3%B3dio) · [Portal da Língua Portuguesa](https://www.portaldalinguaportuguesa.org/index.php?action=lemma&lemma=22716) · [Study of nineteenth-century title pages](https://romatrepress.uniroma3.it/wp-content/uploads/2024/08/Ebook-O-ROMANCE-HISTORICO-EM-LINGUA-PORTUGUESA-1.pdf)

```json
{
  "language": "pt",
  "headword": "episódio",
  "lexemes": [
    {
      "lemma": "episódio",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {"gender": "Masc", "countability": "Count"},
      "pronunciations": [
        {"variety": "pt-BR", "ipa": "/e.pi.ˈzɔ.dʒju/"},
        {"variety": "pt-PT", "ipa": "/i.pi.ˈzɔ.dju/"},
        {"variety": "pt-PT", "ipa": "/e.pi.ˈzɔ.dju/"}
      ],
      "senses": [
        {
          "definition": "cada uma das partes de uma história ou programa apresentado em série",
          "example": "Hoje à noite vamos ver o último episódio da série.",
          "cefr": "A2"
        },
        {
          "definition": "acontecimento particular na vida de alguém ou no decorrer de uma situação",
          "example": "Aquele episódio desagradável na escola nunca foi esquecido pelos alunos.",
          "cefr": "B2"
        },
        {
          "definition": "passagem de uma narrativa que desenvolve uma ação secundária",
          "example": "O episódio da tempestade interrompe a viagem narrada no romance.",
          "cefr": "B2"
        }
      ],
      "spellings": [
        {"spelling": "episódio", "varieties": ["pt-BR", "pt-PT"]},
        {"spelling": "episodio", "varieties": ["pt-BR", "pt-PT"], "pre_reform": true}
      ]
    }
  ]
}
```

## 3. prefeito

**do not use** — The Brazilian office heads a município. The second gloss blurs a modern departmental representative with a Roman official, and the imperial example cannot illustrate both. Ecclesiastical uses also occur in the news.

The head of a Brazilian municipality, the French state representative, and the ancient Roman office need different treatment. Only the Roman office calls for a historical label. A Portuguese report can use prefeito when discussing Brazil, so a regional tag must describe the local title precisely.

[Priberam](https://dicionario.priberam.org/prefeito) · [Brazilian electoral court](https://www.tse.jus.br/comunicacao/noticias/2024/Junho/tire-suas-duvidas-sobre-as-funcoes-do-prefeito-e-do-vereador)

## 4. redor

**use with corrections** — The singular is chiefly learned through ao redor and em redor. Dictionaries also record nearby territory, often in the plural redores. A blanket Count tag hides that contrast. The São Paulo reference supports a final tap.

[Priberam](https://dicionario.priberam.org/redor) · [Portal da Língua Portuguesa](https://www.portaldalinguaportuguesa.org/index.php?action=lemma&lemma=50724)

```json
{
  "language": "pt",
  "headword": "redor",
  "lexemes": [
    {
      "lemma": "redor",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {"gender": "Masc", "note": "usually in ao redor (de) or em redor (de)"},
      "pronunciations": [{"variety": "pt-BR", "ipa": "/ʁe.ˈdɔɾ/"}, {"variety": "pt-PT", "ipa": "/ʁɨ.ˈdɔɾ/"}],
      "senses": [
        {
          "definition": "espaço que se estende em volta de alguém ou de alguma coisa",
          "example": "Encontramos várias pegadas ao redor da fogueira apagada.",
          "cefr": "A2",
          "frames": ["ao redor de algo ou alguém", "em redor de algo ou alguém", "ao seu redor"]
        },
        {
          "definition": "zona próxima de uma cidade ou de outro lugar",
          "example": "Nos redores da vila ainda há caminhos de terra.",
          "cefr": "C1",
          "grammar": {"countability": "Count", "note": "usually plural"}
        }
      ]
    }
  ]
}
```

## 5. escolher

**use with corrections** — The direct object, choice between alternatives, and following infinitive belong together. Allow absolute use in the lexeme grammar, keep sorting by quality distinct, and correct the syllable boundaries and initial vowels in the IPA.

Priberam calls the conjugation regular. The brief also requests forms for a verb with a changing stem vowel, so the nine specified cells are retained. Compare escolho and escolha with /o/ against escolhes, escolhe and escolhem with /ɔ/.

[Priberam](https://dicionario.priberam.org/escolher) · [Portal da Língua Portuguesa](https://www.portaldalinguaportuguesa.org/index.php?action=lemma&lemma=62433) · [Verb morphology, University of São Paulo](https://teses.usp.br/teses/disponiveis/8/8142/tde-04102007-143455/publico/TESE_MARIA_ELIZABETH_LEUBA_SALUM.pdf)

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
        "valency": "Both",
        "irregular": false,
        "note": "stressed o alternates between /o/ in escolho and /ɔ/ in escolhe"
      },
      "pronunciations": [
        {"variety": "pt-BR", "ipa": "/is.ko.ˈʎeɾ/"},
        {"variety": "pt-PT", "ipa": "/iʃ.ku.ˈʎeɾ/"},
        {"variety": "pt-BR", "ipa": "/is.ko.ˈʎe/", "note": "casual"},
        {"variety": "pt-PT", "ipa": "/ʃku.ˈʎeɾ/"}
      ],
      "forms": [
        {
          "form": "escolho",
          "features": {"Mood": "Ind", "Tense": "Pres", "Person": "1", "Number": "Sing", "VerbForm": "Fin"},
          "ipa": {"pt-BR": "/is.ˈko.ʎu/", "pt-PT": "/iʃ.ˈko.ʎu/"}
        },
        {
          "form": "escolhes",
          "features": {"Mood": "Ind", "Tense": "Pres", "Person": "2", "Number": "Sing", "VerbForm": "Fin"},
          "ipa": {"pt-BR": "/is.ˈkɔ.ʎis/", "pt-PT": "/iʃ.ˈkɔ.ʎɨʃ/"}
        },
        {
          "form": "escolhe",
          "features": {"Mood": "Ind", "Tense": "Pres", "Person": "3", "Number": "Sing", "VerbForm": "Fin"},
          "ipa": {"pt-BR": "/is.ˈkɔ.ʎi/", "pt-PT": "/iʃ.ˈkɔ.ʎɨ/"}
        },
        {
          "form": "escolhemos",
          "features": {"Mood": "Ind", "Tense": "Pres", "Person": "1", "Number": "Plur", "VerbForm": "Fin"},
          "ipa": {"pt-BR": "/is.ko.ˈʎe.mus/", "pt-PT": "/iʃ.ku.ˈʎe.muʃ/"}
        },
        {
          "form": "escolhem",
          "features": {"Mood": "Ind", "Tense": "Pres", "Person": "3", "Number": "Plur", "VerbForm": "Fin"},
          "ipa": {"pt-BR": "/is.ˈkɔ.ʎẽj/", "pt-PT": "/iʃ.ˈkɔ.ʎɐ̃j/"}
        },
        {
          "form": "escolhi",
          "features": {"Mood": "Ind", "Tense": "Past", "Person": "1", "Number": "Sing", "VerbForm": "Fin"},
          "ipa": {"pt-BR": "/is.ko.ˈʎi/", "pt-PT": "/iʃ.ku.ˈʎi/"}
        },
        {
          "form": "escolheu",
          "features": {"Mood": "Ind", "Tense": "Past", "Person": "3", "Number": "Sing", "VerbForm": "Fin"},
          "ipa": {"pt-BR": "/is.ko.ˈʎew/", "pt-PT": "/iʃ.ku.ˈʎew/"}
        },
        {
          "form": "escolha",
          "features": {"Mood": "Sub", "Tense": "Pres", "Person": "1", "Number": "Sing", "VerbForm": "Fin"},
          "ipa": {"pt-BR": "/is.ˈko.ʎɐ/", "pt-PT": "/iʃ.ˈko.ʎɐ/"}
        },
        {
          "form": "escolhido",
          "features": {"VerbForm": "Part", "Tense": "Past"},
          "ipa": {"pt-BR": "/is.ko.ˈʎi.du/", "pt-PT": "/iʃ.ku.ˈʎi.du/"}
        }
      ],
      "senses": [
        {
          "definition": "decidir qual das possibilidades se quer ou o que fazer",
          "example": "Ainda não consegui escolher entre os dois vestidos.",
          "cefr": "A1",
          "frames": ["escolher algo ou alguém", "escolher entre duas opções", "escolher + infinitivo"]
        },
        {
          "definition": "separar o que se pode aproveitar do que se deve retirar",
          "example": "Ajuda a escolher o feijão e tira as pedrinhas.",
          "cefr": "B2",
          "frames": ["escolher algo"]
        }
      ]
    }
  ]
}
```

## 6. lidar

**use with corrections** — Distinguish handling pressure from dealing regularly with people or things. Both select com. Working hard also occurs without a complement. Bullfighting is a specialist use recorded in Brazil, so the Portugal-only tag misleads.

[Aulete](https://www.aulete.com.br/lidar) · [Infopédia](https://www.infopedia.pt/dicionarios/lingua-portuguesa/lidar)

```json
{
  "language": "pt",
  "headword": "lidar",
  "lexemes": [
    {
      "lemma": "lidar",
      "pos": "VERB",
      "kind": "word",
      "grammar": {"conjugation": "1", "irregular": false, "valency": "Both"},
      "pronunciations": [
        {"variety": "pt-BR", "ipa": "/li.ˈdaɾ/"},
        {"variety": "pt-PT", "ipa": "/li.ˈdaɾ/"},
        {"variety": "pt-BR", "ipa": "/li.ˈda/", "note": "casual"}
      ],
      "senses": [
        {
          "definition": "encontrar uma maneira de enfrentar uma situação difícil",
          "example": "Ainda não sei como lidar com tanta pressão no trabalho.",
          "cefr": "B1",
          "frames": ["lidar com algo"],
          "grammar": {"valency": "Tr"}
        },
        {
          "definition": "tratar com pessoas ou trabalhar regularmente com alguma coisa",
          "example": "Quem lida com o público precisa de muita paciência.",
          "cefr": "B2",
          "frames": ["lidar com alguém", "lidar com algo"],
          "grammar": {"valency": "Tr"}
        },
        {
          "definition": "dedicar grande esforço a um trabalho ou tarefa",
          "example": "Mesmo no inverno, lidavam de sol a sol nos campos.",
          "cefr": "C1",
          "grammar": {"valency": "Intr"}
        }
      ]
    }
  ]
}
```

## 7. trazer

**do not use** — The entry misses carrying something on one’s person and wearing something. Its direction rule overlooks a narrative point of reference, the content gloss gives examples, and the forms duplicate trouxe while adding two unrequested tenses.

A learner needs to understand Trazia a chave no bolso and Trazia um casaco escuro. The two supplied forms trarei and traria are genuine. The house specification excludes them from the forms list. Both preterite cells for trouxe fit one record with Person set to "1,3".

[Infopédia](https://www.infopedia.pt/dicionarios/lingua-portuguesa/trazer)

## 8. acreditar

**use with corrections** — Show the contrast between acreditar que and acreditar em, and separate trusting someone from accepting a claim. Official authorization to represent someone and recognition of an institution’s competence need clearer examples.

[Aulete](https://www.aulete.com.br/acreditar) · [Michaelis](https://michaelis.uol.com.br/moderno-portugues/busca/portugues-brasileiro/acreditar/) · [Inmetro](https://www.gov.br/inmetro/pt-br/acesso-a-informacao/perguntas-frequentes/acreditacao/existe-obrigatoriedade-de-contratacao-de-servico-em-laboratorio-acreditado) · [Brazilian National Library, 1911 issue](https://hemeroteca-pdf.bn.gov.br/259063/per259063_1911_00030.pdf)

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
        "valency": "Both",
        "note": "also used with no expressed complement"
      },
      "pronunciations": [
        {"variety": "pt-BR", "ipa": "/a.kɾe.dʒi.ˈtaɾ/"},
        {"variety": "pt-PT", "ipa": "/ɐ.kɾɨ.di.ˈtaɾ/"},
        {"variety": "pt-BR", "ipa": "/a.kɾe.dʒi.ˈta/", "note": "casual"}
      ],
      "senses": [
        {
          "definition": "aceitar algo como verdadeiro, real ou provável",
          "example": "Custa acreditar que ninguém ouviu o alarme tocar.",
          "cefr": "A2",
          "frames": ["acreditar que + oração", "acreditar em algo ou alguém"],
          "antonyms": [{"lemma": "duvidar", "pos": "VERB"}]
        },
        {
          "definition": "ter confiança em alguém ou na possibilidade de algo acontecer",
          "example": "A treinadora acredita em mim, mesmo quando erro.",
          "cefr": "B1",
          "frames": ["acreditar em alguém ou em algo"]
        },
        {
          "definition": "dar a alguém autorização oficial para representar outra pessoa ou instituição",
          "example": "O governo acreditou a diplomata como representante junto da organização.",
          "cefr": "C1",
          "frames": ["acreditar alguém como representante", "acreditar alguém junto de uma instituição"],
          "labels": {"style": ["official"]}
        },
        {
          "definition": "reconhecer oficialmente que uma instituição tem competência para determinada atividade",
          "example": "O organismo acreditou dois laboratórios para realizar esses ensaios.",
          "cefr": "C1",
          "frames": ["acreditar uma instituição para + infinitivo"],
          "labels": {"style": ["official"]}
        }
      ],
      "spellings": [
        {"spelling": "acreditar", "varieties": ["pt-BR", "pt-PT"]},
        {"spelling": "accreditar", "varieties": ["pt-BR", "pt-PT"], "pre_reform": true}
      ]
    }
  ]
}
```

## 9. breve

**use with corrections** — Include the independent adverb meaning “soon” and illustrate it without em. The adjective covers short duration and limited extent, including concise speech or writing. The musical and ecclesiastical nouns are specialist vocabulary.

[Priberam](https://dicionario.priberam.org/breve) · [Infopédia](https://www.infopedia.pt/dicionarios/lingua-portuguesa/breve)

```json
{
  "language": "pt",
  "headword": "breve",
  "lexemes": [
    {
      "lemma": "breve",
      "pos": "ADJ",
      "kind": "word",
      "grammar": {"comparable": true, "note": "same form for masculine and feminine"},
      "pronunciations": [{"variety": "pt-BR", "ipa": "/ˈbɾɛ.vi/"}, {"variety": "pt-PT", "ipa": "/ˈbɾɛ.vɨ/"}],
      "senses": [
        {
          "definition": "que dura pouco tempo ou acaba depressa",
          "example": "Fizemos uma breve pausa antes de continuar a reunião.",
          "cefr": "B1",
          "antonyms": [{"lemma": "longo", "pos": "ADJ"}]
        },
        {
          "definition": "de pouca extensão ou expresso em poucas palavras",
          "example": "Pedimos um breve resumo das decisões da reunião.",
          "cefr": "B2",
          "antonyms": [{"lemma": "extenso", "pos": "ADJ"}]
        }
      ]
    },
    {
      "lemma": "breve",
      "pos": "ADV",
      "kind": "word",
      "pronunciations": [{"variety": "pt-BR", "ipa": "/ˈbɾɛ.vi/"}, {"variety": "pt-PT", "ipa": "/ˈbɾɛ.vɨ/"}],
      "senses": [
        {
          "definition": "dentro de pouco tempo a partir de agora ou de então",
          "example": "Tenha paciência, pois breve teremos boas notícias.",
          "cefr": "B2"
        }
      ]
    }
  ]
}
```

## 10. global

**use with corrections** — The worldwide and overall readings deserve separate senses. Restore the IPA boundary before the stressed syllable and allow worldwide reach without requiring every country to participate. Globais follows the regular -al pattern.

[Infopédia](https://www.infopedia.pt/dicionarios/lingua-portuguesa/global)

```json
{
  "language": "pt",
  "headword": "global",
  "lexemes": [
    {
      "lemma": "global",
      "pos": "ADJ",
      "kind": "word",
      "grammar": {"note": "same form for masculine and feminine"},
      "pronunciations": [{"variety": "pt-BR", "ipa": "/ɡlo.ˈbaw/"}, {"variety": "pt-PT", "ipa": "/ɡlu.ˈbal/"}],
      "senses": [
        {
          "definition": "que tem alcance mundial ou envolve países de vários continentes",
          "example": "O aquecimento global preocupa cientistas de todos os continentes.",
          "cefr": "B1",
          "antonyms": [{"lemma": "local", "pos": "ADJ"}]
        },
        {
          "definition": "considerado no seu conjunto, sem separar as partes",
          "example": "Qual é o custo global da obra, com todos os materiais?",
          "cefr": "B2",
          "antonyms": [{"lemma": "parcial", "pos": "ADJ"}]
        }
      ],
      "relations": [{"type": "adjective_of", "lemma": "globo", "pos": "NOUN"}]
    }
  ]
}
```

## 11. francês

**use with corrections** — Keep the language and the person as mass and count senses of one masculine noun. Regular -ês inflections need no form records. Replace the national dining stereotype and keep the related name França out of the definitions.

[Aulete](https://www.aulete.com.br/franc%C3%AAs) · [Portal da Língua Portuguesa](https://www.portaldalinguaportuguesa.org/index.php?action=lemma&lemma=42281) · [Historical lexicon, Universidade Estadual de Londrina](https://www.uel.br/lhispb/frances%C2%B9/)

```json
{
  "language": "pt",
  "headword": "francês",
  "lexemes": [
    {
      "lemma": "francês",
      "pos": "ADJ",
      "kind": "word",
      "grammar": {"note": "feminine and plural follow the regular -ês pattern"},
      "pronunciations": [{"variety": "pt-BR", "ipa": "/fɾɐ̃.ˈses/"}, {"variety": "pt-PT", "ipa": "/fɾɐ̃.ˈseʃ/"}],
      "senses": [
        {
          "definition": "do país cuja capital é Paris, da sua população ou da sua língua",
          "example": "Comprei um queijo francês muito bom na feira de ontem.",
          "cefr": "A1"
        }
      ],
      "relations": [{"type": "adjective_of", "lemma": "França", "pos": "PROPN"}],
      "spellings": [
        {"spelling": "francês", "varieties": ["pt-BR", "pt-PT"]},
        {"spelling": "francez", "varieties": ["pt-BR", "pt-PT"], "pre_reform": true}
      ]
    },
    {
      "lemma": "francês",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {"gender": "Masc", "countability": "Both"},
      "pronunciations": [{"variety": "pt-BR", "ipa": "/fɾɐ̃.ˈses/"}, {"variety": "pt-PT", "ipa": "/fɾɐ̃.ˈseʃ/"}],
      "senses": [
        {
          "definition": "língua românica oficial do país cuja capital é Paris",
          "example": "Ela fala francês muito bem desde criança.",
          "cefr": "A1",
          "grammar": {"countability": "Mass"}
        },
        {
          "definition": "homem nascido no país cuja capital é Paris ou com essa nacionalidade",
          "example": "Um francês pediu ajuda para encontrar a estação.",
          "cefr": "A2",
          "grammar": {"countability": "Count"}
        }
      ],
      "spellings": [
        {"spelling": "francês", "varieties": ["pt-BR", "pt-PT"]},
        {"spelling": "francez", "varieties": ["pt-BR", "pt-PT"], "pre_reform": true}
      ]
    }
  ]
}
```

## 12. policial

**use with corrections** — Add masculine um policial for a crime novel or film alongside common-gender o/a policial for an officer. Use a geographic preference for the officer sense, remove diz-se de, and supply the glide in the reference pronunciations.

[Infopédia](https://www.infopedia.pt/dicionarios/lingua-portuguesa/policial) · [Priberam](https://dicionario.priberam.org/policial) · [Michaelis](https://michaelis.uol.com.br/palavra/RQxQa/policial/)

```json
{
  "language": "pt",
  "headword": "policial",
  "lexemes": [
    {
      "lemma": "policial",
      "pos": "ADJ",
      "kind": "word",
      "grammar": {"comparable": false},
      "pronunciations": [{"variety": "pt-BR", "ipa": "/po.li.ˈsjaw/"}, {"variety": "pt-PT", "ipa": "/pu.li.ˈsjal/"}],
      "senses": [
        {
          "definition": "das forças que mantêm a ordem pública e combatem o crime",
          "example": "Houve uma operação policial no bairro durante a noite.",
          "cefr": "B1"
        },
        {
          "definition": "que conta ou desenvolve uma história de crime e investigação",
          "example": "Adoro ler romances policiais antes de dormir.",
          "cefr": "B1"
        }
      ],
      "relations": [{"type": "adjective_of", "lemma": "polícia", "pos": "NOUN"}]
    },
    {
      "lemma": "policial",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {"gender": "Com", "countability": "Count"},
      "pronunciations": [{"variety": "pt-BR", "ipa": "/po.li.ˈsjaw/"}, {"variety": "pt-PT", "ipa": "/pu.li.ˈsjal/"}],
      "senses": [
        {
          "definition": "agente encarregado de manter a ordem pública e combater o crime",
          "example": "Uma policial nos ajudou a encontrar a estação.",
          "cefr": "A2",
          "regions": [{"variety": "pt-BR", "extent": "mainly"}]
        }
      ],
      "relations": [
        {"type": "derived_from", "lemma": "polícia", "pos": "NOUN"},
        {
          "type": "confusable",
          "lemma": "polícia",
          "pos": "NOUN",
          "why": "in Portugal a police officer is usually called 'um polícia', while Brazil says 'um policial'"
        }
      ]
    },
    {
      "lemma": "policial",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {"gender": "Masc", "countability": "Count"},
      "pronunciations": [{"variety": "pt-BR", "ipa": "/po.li.ˈsjaw/"}, {"variety": "pt-PT", "ipa": "/pu.li.ˈsjal/"}],
      "senses": [
        {
          "definition": "obra de ficção que acompanha um crime e a sua investigação",
          "example": "Li um policial e só descobri o culpado na última página.",
          "cefr": "B2"
        }
      ]
    }
  ]
}
```

## 13. ciência

**use with corrections** — Keep the distinction between knowledge, a countable discipline, and formal awareness. Move the discipline illustration out of the definition, show the count reading directly, and restore the syllable boundary in the Brazilian IPA.

[Aulete](https://www.aulete.com.br/ci%C3%AAncia) · [Portal da Língua Portuguesa](https://www.portaldalinguaportuguesa.org/index.php?action=lemma&lemma=13430) · [Brazilian Academy of Letters](https://www.academia.org.br/nossa-lingua/formulario-ortografico) · [Portuguese edition, 1913](https://pt.wikisource.org/wiki/P%C3%A1gina%3ASebasti%C3%A3o_Rodolfo_Dalgado_-_Influ%C3%AAncia_do_Vocabul%C3%A1rio_Portugu%C3%AAs_em_L%C3%ADnguas_Asi%C3%A1ticas_%281913%29.pdf/13)

```json
{
  "language": "pt",
  "headword": "ciência",
  "lexemes": [
    {
      "lemma": "ciência",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {"gender": "Fem", "countability": "Both"},
      "pronunciations": [{"variety": "pt-BR", "ipa": "/si.ˈẽ.sjɐ/"}, {"variety": "pt-PT", "ipa": "/ˈsjẽ.sjɐ/"}],
      "senses": [
        {
          "definition": "conhecimento construído por meio de observação, estudo e investigação",
          "example": "A ciência ainda não explica todos os mistérios do universo.",
          "cefr": "B1",
          "grammar": {"countability": "Mass"}
        },
        {
          "definition": "área de estudo que investiga certas questões com métodos próprios",
          "example": "A astronomia é a ciência que estuda os astros.",
          "cefr": "B1",
          "grammar": {"countability": "Count"}
        },
        {
          "definition": "conhecimento ou informação que alguém tem sobre um assunto",
          "example": "Você tem ciência dos riscos deste tratamento?",
          "cefr": "C1",
          "frames": ["ter ciência de algo", "tomar ciência de algo", "dar ciência de algo a alguém"],
          "grammar": {"countability": "Mass"},
          "labels": {"formality": "formal"}
        }
      ],
      "spellings": [
        {"spelling": "ciência", "varieties": ["pt-BR", "pt-PT"]},
        {"spelling": "sciencia", "varieties": ["pt-BR", "pt-PT"], "pre_reform": true},
        {"spelling": "sciência", "varieties": ["pt-PT"], "pre_reform": true}
      ]
    }
  ]
}
```

## 14. pronto

**do not use** — The position and comparison restrictions overgeneralize. The learner can meet refeições prontas, mais pronto para agir, and pronto a ajudar. The Portugal-only discourse label is too strong, and the Brazilian telephone response is missing.

The fixed locutions de pronto and a pronto do not supply an independent adverb example. Dictionaries do separately record adverbial pronto. The short informal interjection Pronto? for answering the telephone is a clearer regional teaching case than the broad “agree, accept or change the subject” gloss.

[Priberam](https://dicionario.priberam.org/pronto) · [Infopédia](https://www.infopedia.pt/dicionarios/lingua-portuguesa/pronto)

## 15. vice

**use with corrections** — The sports example describes a runner-up, while ficou com o vice names the placing. Give the person or team and the masculine title separate grammatical treatment, with a Brazilian preference for the sports clipping.

[Infopédia](https://www.infopedia.pt/dicionarios/lingua-portuguesa/vice) · [Brazilian study of clipping](https://www2.uesb.br/ppg/profletras/banco/t2/Luciano_Ferreira.pdf) · [Brazilian sports usage](https://ge.globo.com/futebol/times/vasco/noticia/2025/12/21/vice-do-vasco-na-copa-do-brasil-rende-zoeiras-na-internet-veja-memes.ghtml)

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
        "note": "short form of titles such as vice-presidente and vice-campeão"
      },
      "pronunciations": [{"variety": "pt-BR", "ipa": "/ˈvi.si/"}, {"variety": "pt-PT", "ipa": "/ˈvi.sɨ/"}],
      "senses": [
        {
          "definition": "pessoa que ocupa o cargo imediatamente abaixo do chefe e pode substituí-lo",
          "example": "Na ausência da presidente, a vice conduziu a reunião.",
          "cefr": "B1",
          "labels": {"formality": "informal"}
        },
        {
          "definition": "atleta ou equipe que termina uma competição em segundo lugar",
          "example": "A vice chegou apenas dois segundos depois da campeã.",
          "cefr": "B2",
          "regions": [{"variety": "pt-BR", "extent": "mainly"}],
          "labels": {"domain": "sport", "formality": "informal"}
        }
      ]
    },
    {
      "lemma": "vice",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {"gender": "Masc", "countability": "Count", "note": "short form of vice-campeonato"},
      "pronunciations": [{"variety": "pt-BR", "ipa": "/ˈvi.si/"}, {"variety": "pt-PT", "ipa": "/ˈvi.sɨ/"}],
      "senses": [
        {
          "definition": "segunda colocação alcançada por um participante numa competição",
          "example": "O time perdeu a final e ficou com o vice.",
          "cefr": "B2",
          "regions": [{"variety": "pt-BR", "extent": "mainly"}],
          "labels": {"domain": "sport", "formality": "informal"}
        }
      ]
    }
  ]
}
```

## 16. casal

**use with corrections** — A casal de filhos can be a boy and a girl. A rural holding and a hamlet are also distinct referents. Both rural meanings appear in Brazilian lexicography, and the Portugal-only label overstates the evidence.

[Aulete](https://www.aulete.com.br/casal) · [Infopédia](https://www.infopedia.pt/dicionarios/lingua-portuguesa/casal) · [GLOBAL learner dictionary](https://dictionary.cambridge.org/pt/dicionario/portugues-ingles/casal)

```json
{
  "language": "pt",
  "headword": "casal",
  "lexemes": [
    {
      "lemma": "casal",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {"gender": "Masc", "countability": "Count"},
      "pronunciations": [{"variety": "pt-BR", "ipa": "/ka.ˈzaw/"}, {"variety": "pt-PT", "ipa": "/kɐ.ˈzal/"}],
      "senses": [
        {
          "definition": "duas pessoas que mantêm uma relação amorosa entre si",
          "example": "O casal do andar de cima acabou de chegar.",
          "cefr": "A2"
        },
        {
          "definition": "um macho e uma fêmea da mesma espécie de animal",
          "example": "Vamos comprar um casal de periquitos para as crianças.",
          "cefr": "B1",
          "frames": ["casal de + nome de animal"]
        },
        {
          "definition": "duas pessoas de sexos diferentes consideradas em conjunto",
          "example": "Minha irmã tem um casal de filhos, uma menina e um menino.",
          "cefr": "B1",
          "frames": ["casal de filhos", "casal de irmãos"]
        },
        {
          "definition": "pequeno povoado rural formado por poucas habitações",
          "example": "A minha avó nasceu num casal perto da serra.",
          "cefr": "C1",
          "regions": [{"variety": "pt-PT", "extent": "mainly"}]
        },
        {
          "definition": "pequena propriedade no campo com terreno para cultivo",
          "example": "Os avós cultivavam trigo no casal que tinham herdado.",
          "cefr": "C1",
          "regions": [{"variety": "pt-PT", "extent": "mainly"}]
        }
      ]
    }
  ]
}
```

## 17. chave

**do not use** — The three noun senses are sound, but ordinary text also uses chave for an access code and an answer key, and as an adjective in elemento chave. The current entry would teach the model to miss an entire part of speech.

Fechou a porta com a chave, consulte a chave das respostas, and introduza a chave de acesso require different interpretations. The figurative idea of a solution belongs in the learner entry alongside these concrete uses.

[Priberam](https://dicionario.priberam.org/chave) · [Infopédia](https://www.infopedia.pt/dicionarios/lingua-portuguesa/chave)

## 18. doença

**use with corrections** — The noun allows both por doença and uma doença, and an illness can cause no noticeable suffering. Retain the social metaphor, add the ordinary obsession sense, remove the vague frame, and omit the unsupported derivation from doente.

[Priberam](https://dicionario.priberam.org/doen%C3%A7a) · [Infopédia](https://www.infopedia.pt/dicionarios/lingua-portuguesa/doen%C3%A7a) · [Portal da Língua Portuguesa](https://www.portaldalinguaportuguesa.org/index.php?action=lemma&lemma=58627)

```json
{
  "language": "pt",
  "headword": "doença",
  "lexemes": [
    {
      "lemma": "doença",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {"gender": "Fem", "countability": "Both"},
      "pronunciations": [{"variety": "pt-BR", "ipa": "/do.ˈẽ.sɐ/"}, {"variety": "pt-PT", "ipa": "/ˈdwẽ.sɐ/"}],
      "senses": [
        {
          "definition": "alteração que prejudica a saúde de um ser vivo",
          "example": "A minha avó tem uma doença grave no coração.",
          "cefr": "A2"
        },
        {
          "definition": "mal que prejudica profundamente uma sociedade ou um grupo",
          "example": "Para muitos, a corrupção é a grande doença do país.",
          "cefr": "B2",
          "labels": {"figurative": true}
        },
        {
          "definition": "interesse tão forte por algo que se torna excessivo",
          "example": "Para ele, o futebol já virou uma doença.",
          "cefr": "B2",
          "labels": {"figurative": true}
        }
      ]
    }
  ]
}
```

## 19. normalmente

**use as it is** — The alarm-clock example establishes habitual frequency, while the traffic example establishes ordinary functioning. The two numbered senses, their examples, and the relation to normal work as supplied.

[Infopédia](https://www.infopedia.pt/dicionarios/lingua-portuguesa/normalmente)

## 20. rapidamente

**use with corrections** — Falar rapidamente can suggest a short treatment in context, but the adverb itself promises no particular level of detail. Keep one speed or short-time sense and restore the IPA boundary before -mente.

Dicio numbers a superficial or cursory reading separately. Infopédia groups the word under speed. The supplied sentence supports either interpretation, so a single sense is the safer teaching example. Older books also use ràpidamente.

[Infopédia](https://www.infopedia.pt/dicionarios/lingua-portuguesa/rapidamente) · [Ciberdúvidas on ràpidamente](https://ciberduvidas.iscte-iul.pt/consultorio/perguntas/adverbios-de-modo/2243) · [Dicio](https://www.dicio.com.br/rapidamente/)

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
        {"variety": "pt-BR", "ipa": "/ˌʁa.pi.da.ˈmẽ.tʃi/"},
        {"variety": "pt-PT", "ipa": "/ˌʁa.pi.dɐ.ˈmẽ.tɨ/"}
      ],
      "senses": [
        {
          "definition": "com grande velocidade, sem demorar muito tempo",
          "example": "A notícia espalhou-se rapidamente por toda a cidade.",
          "cefr": "A2",
          "antonyms": [{"lemma": "lentamente", "pos": "ADV"}]
        }
      ],
      "relations": [{"type": "adverb_of", "lemma": "rápido", "pos": "ADJ"}],
      "spellings": [
        {"spelling": "rapidamente", "varieties": ["pt-BR", "pt-PT"]},
        {"spelling": "ràpidamente", "varieties": ["pt-BR", "pt-PT"], "pre_reform": true}
      ]
    }
  ]
}
```

## The five entries to show the writing model

| Rank | Headword | Version | Teaching purpose |
|---:|---|---|---|
| 1 | **computador** | As supplied | **Plain noun.** One count noun and a concrete sentence give the model a baseline for stopping after one sense. |
| 2 | **escolher** | Corrected JSON above | **Verb with frames.** The same everyday choice appears with an object, entre or an infinitive. Sorting beans earns a separate sense. |
| 3 | **global** | Corrected JSON above | **Adjective.** Aquecimento global and custo global show two readings with different opposites. |
| 4 | **policial** | Corrected JSON above | **Two parts of speech and regional usage.** Ajudou-nos uma policial identifies a person. Li um policial identifies a work of fiction. |
| 5 | **ciência** | Corrected JSON above | **Labelled sense.** A ciência, uma ciência and ter ciência de show how countability and a formal label attach to individual senses. |
