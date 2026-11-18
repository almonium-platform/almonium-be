# Japanese learner’s dictionary · pilot review

**24 candidates reviewed.** Sixteen can be used after correction. Eight need a fresh entry before they can teach the house style. None should be copied verbatim.

## Five examples to show the writing model

Use the corrected JSON for these five, in this order.

| Rank | Headword | Coverage | What the example teaches |
|---:|---|---|---|
| 1 | **鉛筆** (#15) | Plain noun | The example counts three sharpened pencils with 本. One concrete definition covers the everyday object. |
| 2 | **好き** (#13) | Two parts of speech | 絵を描くのが好きです shows the adjective. 自分の好きを仕事にしたい gives the noun its own を-marked position. |
| 3 | **食べる** (#1) | Verb and frames | 〜を食べる and 〜で食べていく tie a change in meaning to a change in complement. |
| 4 | **辛い** (#6) | i-adjective and two readings | からい and つらい share a spelling. The salt sense illustrates regional usage, and the grading sense has its own figurative label. |
| 5 | **俺** (#21) | Labelled usage | The familiar pronoun has one ordinary sense. Its register, spelling variants and speaker note each convey a different piece of information. |

## Candidate verdicts

The verdict applies to the submitted entry as a model. CEFR levels in the replacements are editorial estimates. The entries follow the supplied JSON shape.

| No. | Headword | Verdict | Main reason |
|---:|---|---|---|
| 1 | 食べる | **use with corrections** | The two senses and examples work. The livelihood sense needs to include savings or other means of support, and its で frame also works with plain 食べる. |
| 2 | 行く | **do not use** | The forms are right for いく, but the three senses omit reaching a point and physical movement away in 〜ていく. |
| 3 | する | **do not use** | The three supplied irregular forms are sound, but an ordinary elapsed-time sense is missing and the selection sense is incorrectly restricted to intransitive use. |
| 4 | 来る | **do not use** | The くる／きたる distinction is valuable, but the final くる sense conflates directional motion, continuity and the onset of change. |
| 5 | 開く | **do not use** | Both readings and the あく／開ける pairing are sound, but the definitions fail on ordinary examples like 傘を開く and 道を開く. |
| 6 | 辛い | **use with corrections** | Keep the two i-adjectives. Add dry-tasting alcohol and harsh treatment, and remove the two relations that point back to the same headword and POS. |
| 7 | 今日 | **use with corrections** | The two readings have different ranges, but こんにち can mean the present day as well as the present era, and 去年の今日 is missing. |
| 8 | 明日 | **use with corrections** | The tomorrow/future distinction works once the readings are scoped. みょうにち belongs to the literal day, and あす is especially natural for the future. |
| 9 | 高い | **use with corrections** | The first gloss contains two sentences, and the pitch gloss does little to explain what distinguishes one sound from another. |
| 10 | いい | **do not use** | The よ- forms are valuable, but the reading inventory and the treatment of permission, sufficiency, advice and refusal are unsuitable for imitation. |
| 11 | 静か | **use with corrections** | Quietness does not require absolute silence, and 静かに動き出す can describe gentle motion even when something is moving. |
| 12 | きれい | **use with corrections** | The original has useful spelling variants, but it omits orderly arrangement and figurative integrity, and the last definition ends with a redundant synonym. |
| 13 | 好き | **use with corrections** | Add the noun illustrated by 自分の好きを仕事にしたい and distinguish personal choice from the teasing 君も好きだね use. |
| 14 | 本 | **do not use** | 本 combines a book noun, a counter and a bound prefix. The DET tag hides that distinction, and paper and printing are too narrow for the book sense. |
| 15 | 鉛筆 | **use with corrections** | Keep the one everyday noun sense and 本. Add attested spellings, remove routine collocations from frames, and show the counter in the example. |
| 16 | は | **use with corrections** | Keep the topic and contrast examples. Add minimum quantity and emphasis, and remove 〜ではない as a stand-alone cue for contrast. |
| 17 | を | **use with corrections** | Object, path and departure are valid. Add the learner-relevant duration, causee and circumstance uses, and make the departure example match its frame. |
| 18 | いらっしゃる | **use with corrections** | いらっしゃって is regular. The exceptional ます stem and imperative need the note, and the subsidiary uses need their own definitions and examples. |
| 19 | 参る | **do not use** | The motion label needs the 丁重語 distinction, while the heat example fits physical exhaustion better than bewilderment. |
| 20 | です | **do not use** | The two core uses deserve to stay, but the form list mixes inflections with multiword negative equivalents and underspecifies でしょう. |
| 21 | 俺 | **use with corrections** | Keep the familiar label. Add オレ and describe the association with male speakers as a tendency. |
| 22 | コンピューター | **use with corrections** | Keep both katakana spellings and 台. Replace 電子の機械 and remove the claim that rapid processing defines a computer. |
| 23 | 勉強 | **use with corrections** | The three senses have dictionary support. Limit する to the appropriate senses and describe the bargaining context more precisely than “informal.” |
| 24 | 気をつける | **use with corrections** | Keep the fixed 気＋を and the inflecting つける. Broaden the definition beyond preventing danger or failure and explain the external に complement. |

## Candidate notes and complete replacements

### 1. 食べる

**use with corrections** — The two senses and examples work. The livelihood sense needs to include savings or other means of support, and its で frame also works with plain 食べる.

Keep valency at sense level under the brief’s object-based convention. In the livelihood use, the で phrase identifies what supports the person. An omitted object in パンを食べる would be a different reason to see no overt を phrase. [デジタル大辞泉 · 食べる](https://kotobank.jp/word/食べる-562605) [NINJAL · IPAL verb frames](https://www2.ninjal.ac.jp/dictionaries/IPALBV/mibook.html)

```json
{
  "language": "ja",
  "headword": "食べる",
  "lexemes": [
    {
      "lemma": "食べる",
      "pos": "VERB",
      "kind": "word",
      "grammar": {
        "conjugation": "ichidan",
        "note": "食べる is the usual kanji spelling. 喰べる is an uncommon literary spelling."
      },
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "たべる",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "ものを口に入れ、かむなどして飲み込む",
          "example": "毎朝パンと卵を食べてから出かけます。",
          "cefr": "A1",
          "frames": [
            "〜を食べる"
          ],
          "grammar": {
            "valency": "Tr"
          }
        },
        {
          "definition": "生活に必要なものをまかなって暮らす",
          "example": "絵の仕事だけでなんとか食べている。",
          "cefr": "B2",
          "frames": [
            "〜で食べる",
            "〜で食べていく"
          ],
          "grammar": {
            "valency": "Intr",
            "note": "The source of support takes で."
          },
          "labels": {
            "figurative": true
          },
          "spellings": [
            "食べる",
            "たべる"
          ]
        }
      ],
      "spellings": [
        {
          "text": "食べる",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "たべる",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "喰べる",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        }
      ]
    }
  ]
}
```

### 2. 行く

**do not use** — The forms are right for いく, but the three senses omit reaching a point and physical movement away in 〜ていく.

A sum reaching a given amount and 年のいった人 illustrate the range beyond travel. In 子どもが走っていった, 〜ていく marks motion away from the viewpoint. うまく行く occurs in kanji as well. Link いく and ゆく to compatible spellings, including ゆく, and account for inflection before generating from a single reading. For purposes of motion, use a productive frame such as 動詞のます形の語幹＋に行く. [デジタル大辞泉 · 行く](https://kotobank.jp/word/行く-431393)

### 3. する

**do not use** — The three supplied irregular forms are sound, but an ordinary elapsed-time sense is missing and the selection sense is incorrectly restricted to intransitive use.

In 飲み物をお茶にする, the choice can take an explicit を object. A few minutes passing in しばらくすると雨がやんだ is a distinct meaning. 〜ようとする also needs coverage. The double は in the coffee example is possible in a contrastive context but clumsy in isolation. Price is a routine shopping use, so B2 is unnecessarily high. Distinguish the independent verb from verbal nouns and productive constructions. Account for the exceptional imperatives しろ and せよ if the generator lacks them. [NINJAL · 基本語辞書](https://repository.ninjal.ac.jp/record/2000336/files/kihongojisho_s_200902.pdf)

### 4. 来る

**do not use** — The くる／きたる distinction is valuable, but the final くる sense conflates directional motion, continuity and the onset of change.

寒くなってきた can tell us that a change has started. 聞こえてきた covers a perception entering awareness. Neither is explained by continuity alone. The destination of 来る can be an adopted viewpoint. Keep 来る（きたる） as a formal 連体詞 with 来たる as an attested alternative, and account separately for verbal きたる in 時来たる. A 連体詞 needs an explicit POS mapping before this can model two parts of speech. [デジタル大辞泉 · 来る](https://kotobank.jp/word/来る-474513)

### 5. 開く

**do not use** — Both readings and the あく／開ける pairing are sound, but the definitions fail on ordinary examples like 傘を開く and 道を開く.

A closed umbrella can be spread without revealing an interior. For あく, distinguish a barrier opening from an opening being made, as in 壁に穴が開く. For ひらく, include opening up a path or field alongside holding an event and opening a business. 空く in 席が空く belongs to a different spelling and sense. Keep transitivity on each ひらく sense. [漢字ペディア · 開く](https://www.kanjipedia.jp/kotoba/0000811000)

### 6. 辛い

**use with corrections** — Keep the two i-adjectives. Add dry-tasting alcohol and harsh treatment, and remove the two relations that point back to the same headword and POS.

The scripts and sense restrictions matter here. 鹹い is associated with salt, while 辛い covers several meanings. The salt example explicitly names the cause, the usage note identifies its regional distribution, and つらく当たる shows who receives the harsh treatment. [からい](https://kokugo.jitenon.jp/word/p9250) [漢字ペディア · つらい](https://www.kanjipedia.jp/kotoba/0003597100) [Shogakukan · regional use of からい for saltiness](https://kotobanomado.jp/column/6683/)

```json
{
  "language": "ja",
  "headword": "辛い",
  "lexemes": [
    {
      "lemma": "辛い",
      "pos": "ADJ",
      "kind": "word",
      "grammar": {
        "conjugation": "i",
        "note": "鹹い is uncommon and denotes saltiness."
      },
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "からい",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "口に入れると舌に強い刺激がある",
          "example": "このカレーは辛すぎて、水が手放せない。",
          "cefr": "A2",
          "spellings": [
            "辛い",
            "からい"
          ]
        },
        {
          "definition": "口に入れたときの塩味が強い",
          "example": "塩を入れすぎて、スープが辛くなった。",
          "cefr": "B1",
          "grammar": {
            "note": "Common from the Tōkai region through western Japan, including Kansai."
          }
        },
        {
          "definition": "人への評価や点のつけ方が厳しい",
          "example": "あの先生は採点が辛いことで有名だ。",
          "cefr": "B2",
          "frames": [
            "点が辛い",
            "採点が辛い"
          ],
          "labels": {
            "figurative": true
          },
          "antonyms": [
            {
              "lemma": "甘い",
              "pos": "ADJ"
            }
          ],
          "spellings": [
            "辛い",
            "からい"
          ]
        },
        {
          "definition": "酒の味に甘みが少なく、すっきりしている",
          "example": "この酒は思ったよりも辛く、甘みはほとんどない。",
          "cefr": "B2",
          "spellings": [
            "辛い",
            "からい"
          ],
          "antonyms": [
            {
              "lemma": "甘い",
              "pos": "ADJ"
            }
          ]
        }
      ],
      "spellings": [
        {
          "text": "辛い",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "からい",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "鹹い",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        }
      ]
    },
    {
      "lemma": "辛い",
      "pos": "ADJ",
      "kind": "word",
      "grammar": {
        "conjugation": "i"
      },
      "spellings": [
        {
          "text": "辛い",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "つらい",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        }
      ],
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "つらい",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "体や心が苦しくて耐えにくい",
          "example": "冬の朝、早く起きるのはつらい。",
          "cefr": "B1",
          "frames": [
            "〜のがつらい",
            "〜がつらい"
          ]
        },
        {
          "definition": "人への接し方に思いやりがない",
          "example": "失敗した仲間につらく当たるのはよそう。",
          "cefr": "B2",
          "frames": [
            "人につらく当たる"
          ]
        }
      ]
    }
  ]
}
```

### 7. 今日

**use with corrections** — The two readings have different ranges, but こんにち can mean the present day as well as the present era, and 去年の今日 is missing.

A bare time noun can modify a predicate directly. 今日中に contains 中 and needs no separate sense here. The uncommon dictionary reading こんじつ is secondary. In a reading exercise, display the target reading of 今日 above the example, especially in the formal calendar-day use. [デジタル大辞泉 · 今日](https://kotobank.jp/word/今日-477559)

```json
{
  "language": "ja",
  "headword": "今日",
  "lexemes": [
    {
      "lemma": "今日",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {
        "note": "A time noun that can modify the predicate without a particle."
      },
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "きょう",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "話している時点でのこの一日",
          "example": "今日は朝からずっと雨が降っています。",
          "cefr": "A1"
        },
        {
          "definition": "別の年や月で今と同じ日付に当たる日",
          "example": "去年の今日、この町に引っ越してきた。",
          "cefr": "A2",
          "frames": [
            "去年の今日",
            "来年の今日"
          ]
        }
      ],
      "spellings": [
        {
          "text": "今日",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "きょう",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "けふ",
          "script": "Hira",
          "pre_reform": true
        }
      ]
    },
    {
      "lemma": "今日",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {
        "note": "こんにち is characteristic of more formal speech and writing."
      },
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "こんにち",
          "notation": "kana",
          "spelling": "今日"
        },
        {
          "variety": "ja-JP",
          "notation": "kana",
          "reading": "こんじつ",
          "note": "less_common",
          "spelling": "今日"
        },
        {
          "variety": "ja-JP",
          "reading": "こんにち",
          "notation": "kana",
          "spelling": "こんにち"
        }
      ],
      "senses": [
        {
          "definition": "今の人々が暮らしている時代",
          "example": "今日の社会では、携帯電話が欠かせない道具になった。",
          "cefr": "B2",
          "frames": [
            "今日では",
            "今日の〜"
          ],
          "labels": {
            "formality": "formal"
          }
        },
        {
          "definition": "過去からつながる今の時点",
          "example": "周りの人に支えられて、今日に至りました。",
          "cefr": "B2",
          "frames": [
            "今日に至る",
            "今日まで"
          ],
          "labels": {
            "formality": "formal"
          }
        },
        {
          "definition": "話している時に当たるこの一日",
          "example": "今日をもって今期の営業を終了いたします。",
          "cefr": "C1",
          "frames": [
            "今日をもって"
          ],
          "labels": {
            "formality": "formal"
          },
          "grammar": {
            "note": "The reading こんにち is formal here. The spelling can also be read きょう."
          }
        }
      ],
      "spellings": [
        {
          "text": "今日",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "こんにち",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        }
      ]
    }
  ]
}
```

### 8. 明日

**use with corrections** — The tomorrow/future distinction works once the readings are scoped. みょうにち belongs to the literal day, and あす is especially natural for the future.

The single label “careful” obscures the difference between a regular broadcast reading and especially formal speech. Remove the Count claim and the loose antonym 昨日. The figurative example spells あす in kana so that the learner can hear the intended reading. [明日 · readings and senses](https://ja.wiktionary.org/wiki/明日)

```json
{
  "language": "ja",
  "headword": "明日",
  "lexemes": [
    {
      "lemma": "明日",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {
        "note": "A time noun that can modify the predicate without a particle."
      },
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "あした",
          "notation": "kana",
          "spelling": "明日"
        },
        {
          "variety": "ja-JP",
          "reading": "あす",
          "notation": "kana",
          "spelling": "明日"
        },
        {
          "variety": "ja-JP",
          "reading": "みょうにち",
          "notation": "kana",
          "note": "careful",
          "spelling": "明日"
        },
        {
          "variety": "ja-JP",
          "reading": "あした",
          "notation": "kana",
          "spelling": "あした"
        },
        {
          "variety": "ja-JP",
          "reading": "あす",
          "notation": "kana",
          "spelling": "あす"
        },
        {
          "variety": "ja-JP",
          "reading": "みょうにち",
          "notation": "kana",
          "note": "careful",
          "spelling": "みょうにち"
        }
      ],
      "senses": [
        {
          "definition": "今過ごしている日のすぐ次の日",
          "example": "明日は朝早く起きなければならない。",
          "cefr": "A1",
          "grammar": {
            "note": "あした is usual in conversation, あす is common in announcements, and みょうにち is especially formal."
          }
        },
        {
          "definition": "これから人々が迎える時代や生活",
          "example": "子どもたちがこの町のあすをつくる。",
          "cefr": "B2",
          "labels": {
            "figurative": true
          },
          "frames": [
            "明日の〜"
          ],
          "spellings": [
            "明日",
            "あす",
            "あした"
          ],
          "grammar": {
            "note": "Usually read あす, also あした."
          }
        }
      ],
      "spellings": [
        {
          "text": "明日",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "あした",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "あす",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "みょうにち",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        }
      ]
    }
  ]
}
```

### 9. 高い

**use with corrections** — The first gloss contains two sentences, and the pitch gloss does little to explain what distinguishes one sound from another.

A tall tree and a shelf mounted high illustrate size and position separately. A high price pairs with 安い. Rank, degree and pitch pair with 低い. Keep loudness apart from pitch, and leave idioms such as お目が高い outside the numbered literal senses. [デジタル大辞泉 · 高い](https://kotobank.jp/word/高い-559081)

```json
{
  "language": "ja",
  "headword": "高い",
  "lexemes": [
    {
      "lemma": "高い",
      "pos": "ADJ",
      "kind": "word",
      "grammar": {
        "conjugation": "i"
      },
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "たかい",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "下から上までの長さや突き出た部分が大きい",
          "example": "庭には屋根より高い木がある。",
          "cefr": "A1",
          "frames": [
            "背が高い",
            "鼻が高い"
          ],
          "antonyms": [
            {
              "lemma": "低い",
              "pos": "ADJ"
            }
          ]
        },
        {
          "definition": "基準となる所からずっと上にある",
          "example": "棚が高いので踏み台を使った。",
          "cefr": "A1",
          "frames": [
            "〜より高い",
            "高い所に〜"
          ],
          "antonyms": [
            {
              "lemma": "低い",
              "pos": "ADJ"
            }
          ]
        },
        {
          "definition": "買うのに必要なお金が多い",
          "example": "この店は野菜は安いが、果物は高い。",
          "cefr": "A1",
          "frames": [
            "値段が高い"
          ],
          "antonyms": [
            {
              "lemma": "安い",
              "pos": "ADJ"
            }
          ]
        },
        {
          "definition": "比べたときに数値や程度が上にある",
          "example": "今日は気温が高く、上着はいらない。",
          "cefr": "A2",
          "frames": [
            "〜が高い"
          ],
          "antonyms": [
            {
              "lemma": "低い",
              "pos": "ADJ"
            }
          ]
        },
        {
          "definition": "人の立場や評価が上にある",
          "example": "この店は料理の評判が高い。",
          "cefr": "B1",
          "frames": [
            "地位が高い",
            "評判が高い"
          ],
          "antonyms": [
            {
              "lemma": "低い",
              "pos": "ADJ"
            }
          ]
        },
        {
          "definition": "音階で上の方に位置する",
          "example": "同じ音量でも、こっちの笛の方が音が高い。",
          "cefr": "A2",
          "frames": [
            "音が高い",
            "声が高い"
          ],
          "antonyms": [
            {
              "lemma": "低い",
              "pos": "ADJ"
            }
          ]
        },
        {
          "definition": "においが強く、よく感じられる",
          "example": "香りの高いお茶をゆっくり味わった。",
          "cefr": "B2",
          "frames": [
            "香りが高い"
          ]
        },
        {
          "definition": "声が大きく、遠くまでよく響く",
          "example": "声が高いよ、隣の部屋まで聞こえてしまう。",
          "cefr": "B2",
          "frames": [
            "声が高くなる"
          ],
          "grammar": {
            "note": "Less common in this sense than 声が大きい."
          }
        }
      ],
      "spellings": [
        {
          "text": "高い",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "たかい",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        }
      ]
    }
  ]
}
```

### 10. いい

**do not use** — The よ- forms are valuable, but the reading inventory and the treatment of permission, sufficiency, advice and refusal are unsuitable for imitation.

Supply よい and its relationship to 良い. The same written 良い can be read いい. In 休んでもいい the speaker allows an action, while これでいい accepts something as sufficient. In もういいです the speaker can decline more, and 〜ばいい can give advice. The よく record has an empty features object, so its adverbial cell cannot be recovered from features alone. [NINJAL · 良いの発音](https://kotoba.ninjal.ac.jp/qa/yokuaru/qa-238/)

### 11. 静か

**use with corrections** — Quietness does not require absolute silence, and 静かに動き出す can describe gentle motion even when something is moving.

Retain the distinctions between surroundings, calm conditions and composure. The fourth example makes manner visible. Ordinary 静かに is the regular に form of a な-adjective. It needs no extra adverb lexeme. [漢字ペディア · 静か](https://www.kanjipedia.jp/kotoba/0003925600)

```json
{
  "language": "ja",
  "headword": "静か",
  "lexemes": [
    {
      "lemma": "静か",
      "pos": "ADJ",
      "kind": "word",
      "grammar": {
        "conjugation": "na",
        "note": "静か is the usual kanji spelling. 閑か is uncommon."
      },
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "しずか",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "大きな音や騒がしい声がない",
          "example": "窓を閉めると、部屋の中は静かになった。",
          "cefr": "A1",
          "frames": [
            "静かにする",
            "静かな〜"
          ],
          "antonyms": [
            {
              "lemma": "うるさい",
              "pos": "ADJ"
            }
          ]
        },
        {
          "definition": "激しい動きや変化がなく穏やかだ",
          "example": "今朝は風もなく、海がとても静かだ。",
          "cefr": "B1"
        },
        {
          "definition": "気持ちや態度が穏やかで落ち着いている",
          "example": "兄は静かな人だが、意見ははっきり言う。",
          "cefr": "B1"
        },
        {
          "definition": "動きに勢いがなく、ゆっくりしている",
          "example": "船は波も立てず静かに動き出した。",
          "cefr": "B1",
          "frames": [
            "静かに〜"
          ]
        }
      ],
      "spellings": [
        {
          "text": "静か",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "しずか",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "閑か",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "靜か",
          "script": "Jpan",
          "pre_reform": true
        }
      ]
    }
  ]
}
```

### 12. きれい

**use with corrections** — The original has useful spelling variants, but it omits orderly arrangement and figurative integrity, and the last definition ends with a redundant synonym.

A desk can be clean with its contents still scattered. A well-organized set of tools illustrates the independent arrangement sense. The completion reading is tied to きれいに. The adverbial form can stay with its adjective lexeme under Japanese dictionary practice. [デジタル大辞泉 · 綺麗](https://kotobank.jp/word/綺麗-480543)

```json
{
  "language": "ja",
  "headword": "きれい",
  "lexemes": [
    {
      "lemma": "きれい",
      "pos": "ADJ",
      "kind": "word",
      "spellings": [
        {
          "text": "きれい",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "綺麗",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "奇麗",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "キレイ",
          "script": "Kana",
          "varieties": [
            "ja-JP"
          ]
        }
      ],
      "grammar": {
        "conjugation": "na"
      },
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "きれい",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "目で見たり耳で聞いたりして美しい",
          "example": "窓から見える夕日がとてもきれいだ。",
          "cefr": "A1"
        },
        {
          "definition": "目立つ汚れがなく清潔だ",
          "example": "料理の前には、手をきれいに洗いましょう。",
          "cefr": "A1",
          "antonyms": [
            {
              "lemma": "汚い",
              "pos": "ADJ"
            }
          ]
        },
        {
          "definition": "乱れたところがなく整っている",
          "example": "箱の中に道具がきれいに並んでいる。",
          "cefr": "A2"
        },
        {
          "definition": "残ったものが何一つない",
          "example": "おなかがすいて、おかずをきれいに平らげた。",
          "cefr": "B2",
          "frames": [
            "きれいに〜"
          ],
          "grammar": {
            "note": "Used in the adverbial form きれいに."
          }
        },
        {
          "definition": "不正やうしろめたいところがない",
          "example": "どの候補者にもきれいな選挙を求めたい。",
          "cefr": "B2",
          "labels": {
            "figurative": true
          }
        }
      ]
    }
  ]
}
```

### 13. 好き

**use with corrections** — Add the noun illustrated by 自分の好きを仕事にしたい and distinguish personal choice from the teasing 君も好きだね use.

The が in 絵を描くのが好き marks what the speaker likes. In 好きを仕事にする, 好き itself takes を. That gives the writing model a concrete test for adding a noun lexeme. 好きにする expresses personal choice. 君も好きだね can comment on someone’s unusual enthusiasm. Romantic liking is covered by the ordinary attraction sense. [デジタル大辞泉 and 日本国語大辞典 · 好き](https://kotobank.jp/word/好き-540943)

```json
{
  "language": "ja",
  "headword": "好き",
  "lexemes": [
    {
      "lemma": "好き",
      "pos": "ADJ",
      "kind": "word",
      "grammar": {
        "conjugation": "na"
      },
      "spellings": [
        {
          "text": "好き",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "すき",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        }
      ],
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "すき",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "人や物事に心をひかれている",
          "example": "私は子どもの頃から絵を描くのが好きです。",
          "cefr": "A1",
          "frames": [
            "〜が好き",
            "〜のが好き",
            "好きな〜"
          ],
          "antonyms": [
            {
              "lemma": "嫌い",
              "pos": "ADJ"
            }
          ]
        },
        {
          "definition": "自分の思いどおりで自由だ",
          "example": "時間はたっぷりあるから、好きにしていいよ。",
          "cefr": "A2",
          "frames": [
            "好きにする",
            "好きなように〜"
          ]
        },
        {
          "definition": "人があまりしたがらないことにまで熱心だ",
          "example": "また自転車で山を越えるなんて、君も好きだね。",
          "cefr": "B2",
          "frames": [
            "〜も好きだね"
          ],
          "grammar": {
            "note": "Often said with affectionate surprise or gentle irony."
          }
        }
      ],
      "relations": [
        {
          "type": "derived_from",
          "lemma": "好く",
          "pos": "VERB"
        }
      ]
    },
    {
      "lemma": "好き",
      "kind": "word",
      "spellings": [
        {
          "text": "好き",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "すき",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        }
      ],
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "すき",
          "notation": "kana"
        }
      ],
      "relations": [
        {
          "type": "derived_from",
          "lemma": "好く",
          "pos": "VERB"
        }
      ],
      "pos": "NOUN",
      "senses": [
        {
          "definition": "人や物事にひかれる気持ちやその対象",
          "example": "自分の好きを仕事にしたい。",
          "cefr": "B1"
        }
      ]
    }
  ]
}
```

### 14. 本

**do not use** — 本 combines a book noun, a counter and a bound prefix. The DET tag hides that distinction, and paper and printing are too narrow for the book sense.

A script can also be a 本. Account for the もと noun or an explicit spelling link to its canonical entry. Keep 冊 as the counter for books and distinguish it from 本 itself as a counter, including 一本・二本・三本. Productive 本〜 needs a bound-prefix policy. Its use before a noun alone cannot establish a determiner lexeme. [日本国語大辞典 · 本](https://kotobank.jp/word/本-631932)

### 15. 鉛筆

**use with corrections** — Keep the one everyday noun sense and 本. Add attested spellings, remove routine collocations from frames, and show the counter in the example.

The compound components follow the brief’s spelling-based rule for words whose characters also represent independent nouns. The definition identifies the core, its covering and how the tool is used. The mixed えん筆 occurs in school materials. [Tombow · えんぴつの製造工程](https://www.tombow.com/sp/kids/pencil/factory.html) [School writing-supplies guide · えん筆](https://www.kawamotokita-e.ed.jp/?action=common_download_main&upload_id=36943)

```json
{
  "language": "ja",
  "headword": "鉛筆",
  "lexemes": [
    {
      "lemma": "鉛筆",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {
        "countability": "Count",
        "classifier": "本"
      },
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "えんぴつ",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "木などで芯を包み、先を削って字や絵をかく道具",
          "example": "筆箱には削った鉛筆が三本入っている。",
          "cefr": "A1"
        }
      ],
      "spellings": [
        {
          "text": "鉛筆",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "えんぴつ",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "エンピツ",
          "script": "Kana",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "えん筆",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        }
      ],
      "components": [
        {
          "text": "鉛",
          "lemma": "鉛",
          "inflects": "fixed"
        },
        {
          "text": "筆",
          "lemma": "筆",
          "inflects": "fixed"
        }
      ]
    }
  ]
}
```

### 16. は

**use with corrections** — Keep the topic and contrast examples. Add minimum quantity and emphasis, and remove 〜ではない as a stand-alone cue for contrast.

A two-clause contrast makes the function audible. In 十分はかかる, は places a lower bound on the estimate. The example with 読んではみた shows a narrower emphasis on what was actually tried. The PART tag and spoken わ fit the house convention. [日本国語大辞典 · は](https://kotobank.jp/word/は-597777)

```json
{
  "language": "ja",
  "headword": "は",
  "lexemes": [
    {
      "lemma": "は",
      "pos": "PART",
      "kind": "word",
      "grammar": {
        "invariable": true,
        "note": "Written は but always pronounced わ as a particle."
      },
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "わ",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "何について話すのかを示す",
          "example": "私の趣味は写真を撮ることです。",
          "cefr": "A1",
          "frames": [
            "〜は〜",
            "〜には〜"
          ]
        },
        {
          "definition": "比べるものを取り上げて違いを示す",
          "example": "肉は食べますが、魚は食べません。",
          "cefr": "A2",
          "frames": [
            "〜は〜が、〜は〜"
          ]
        },
        {
          "definition": "少なくともその量に達するという目安を示す",
          "example": "ここから駅まで歩いて十分はかかります。",
          "cefr": "B1",
          "frames": [
            "数量＋は"
          ]
        },
        {
          "definition": "ある事柄を特に取り上げて強める",
          "example": "読んではみたものの、意味が分からない。",
          "cefr": "B1",
          "frames": [
            "〜てはみる",
            "〜てはいる",
            "〜くはない"
          ]
        }
      ]
    }
  ]
}
```

### 17. を

**use with corrections** — Object, path and departure are valid. Add the learner-relevant duration, causee and circumstance uses, and make the departure example match its frame.

Compare 電車を降りる, 夏休みを過ごす, 生徒を帰らせる and 大雨の中を迎えに来る. The last example is particularly helpful because 大雨 is neither the object being fetched nor a place left behind. The particle is written を and normally pronounced お. [NINJAL · 期間 and 状況 with を](https://bibdb.ninjal.ac.jp/bunken/ja/article/112022002575) [Agency for Cultural Affairs · modern kana usage](https://www.bunka.go.jp/kokugo_nihongo/sisaku/joho/joho/kijun/naikaku/gendaikana/honbun_dai2.html)

```json
{
  "language": "ja",
  "headword": "を",
  "lexemes": [
    {
      "lemma": "を",
      "pos": "ADP",
      "kind": "word",
      "grammar": {
        "invariable": true,
        "note": "case particle placed after a noun; pronounced like お"
      },
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "お",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "動作が直接及ぶ相手やものにつく",
          "example": "毎朝コーヒーを一杯飲んでから出かけます。",
          "cefr": "A1",
          "frames": [
            "〜を食べる",
            "〜を見る"
          ]
        },
        {
          "definition": "移動するときに通る場所につく",
          "example": "子どもたちが公園の中を元気に走っている。",
          "cefr": "A2",
          "frames": [
            "〜を歩く",
            "〜を通る",
            "〜を渡る"
          ]
        },
        {
          "definition": "移動するときに離れる場所につく",
          "example": "電車を降りるとすぐに海が見えた。",
          "cefr": "A2",
          "frames": [
            "〜を出る",
            "〜を降りる"
          ]
        },
        {
          "definition": "ある動作が続く期間につく",
          "example": "短い夏休みを家族と海辺で過ごした。",
          "cefr": "B1",
          "frames": [
            "〜を過ごす"
          ]
        },
        {
          "definition": "何かするようにさせる相手につく",
          "example": "先生は疲れた生徒を先に帰らせた。",
          "cefr": "B1",
          "frames": [
            "人を〜せる",
            "人を〜させる"
          ]
        },
        {
          "definition": "動作が行われるときの状況につく",
          "example": "大雨の中を迎えに来てくれた。",
          "cefr": "B2",
          "frames": [
            "〜の中を"
          ]
        }
      ]
    }
  ]
}
```

### 18. いらっしゃる

**use with corrections** — いらっしゃって is regular. The exceptional ます stem and imperative need the note, and the subsidiary uses need their own definitions and examples.

Direction, presence, an ongoing action and a respectful predicate each admit a different paraphrase. The speaker shows respect for the person spoken of. A speaker can use an honorific verb in a sentence without the polite ます ending. The greeting いらっしゃいませ does not create another verb sense. [デジタル大辞泉 · いらっしゃる](https://kotobank.jp/word/いらつしやる-3207277)

```json
{
  "language": "ja",
  "headword": "いらっしゃる",
  "lexemes": [
    {
      "lemma": "いらっしゃる",
      "pos": "VERB",
      "kind": "word",
      "grammar": {
        "conjugation": "godan",
        "irregular": true,
        "valency": "Intr",
        "note": "The stem before ます and the imperative end in い. The て-form follows the regular godan rule."
      },
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "いらっしゃる",
          "notation": "kana"
        }
      ],
      "forms": [
        {
          "form": "いらっしゃいます",
          "features": {
            "Polite": "Form"
          },
          "reading": "いらっしゃいます",
          "notation": "kana"
        },
        {
          "form": "いらっしゃって",
          "features": {
            "VerbForm": "Conv"
          },
          "reading": "いらっしゃって",
          "notation": "kana"
        },
        {
          "form": "いらっしゃらない",
          "features": {
            "Polarity": "Neg"
          },
          "reading": "いらっしゃらない",
          "notation": "kana"
        },
        {
          "form": "いらっしゃい",
          "features": {
            "Mood": "Imp"
          },
          "reading": "いらっしゃい",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "ある場所にいる人を敬って言う",
          "example": "先生は今も職員室にいらっしゃいます。",
          "cefr": "B1",
          "frames": [
            "〜にいらっしゃる"
          ],
          "labels": {
            "politeness": "honorific"
          }
        },
        {
          "definition": "こちらに来る人を敬って言う",
          "example": "先生がわざわざこちらへいらっしゃった。",
          "cefr": "B1",
          "frames": [
            "〜にいらっしゃる",
            "〜へいらっしゃる"
          ],
          "labels": {
            "politeness": "honorific"
          }
        },
        {
          "definition": "別の所へ行く人を敬って言う",
          "example": "先生は来週、海外へいらっしゃるそうです。",
          "cefr": "B1",
          "frames": [
            "〜にいらっしゃる",
            "〜へいらっしゃる"
          ],
          "labels": {
            "politeness": "honorific"
          }
        },
        {
          "definition": "その人の動作やその結果が続く様子を敬って言う",
          "example": "先生は今、奥で本を読んでいらっしゃいます。",
          "cefr": "B2",
          "frames": [
            "〜ていらっしゃる",
            "〜でいらっしゃる"
          ],
          "labels": {
            "politeness": "honorific"
          },
          "grammar": {
            "note": "Subsidiary verb corresponding to 〜ている, including voiced 〜でいる."
          }
        },
        {
          "definition": "その人がどんな人かを敬って言う",
          "example": "こちらの方は以前の校長先生でいらっしゃいます。",
          "cefr": "B2",
          "frames": [
            "名詞＋でいらっしゃる",
            "な形容詞の語幹＋でいらっしゃる",
            "〜くていらっしゃる"
          ],
          "labels": {
            "politeness": "honorific"
          },
          "grammar": {
            "note": "Subsidiary use corresponding to である or an adjective predicate."
          }
        }
      ]
    }
  ]
}
```

### 19. 参る

**do not use** — The motion label needs the 丁重語 distinction, while the heat example fits physical exhaustion better than bewilderment.

Keep worship, admitting defeat and being at a loss distinct where the evidence supports them. Add being worn down, as in 暑さで体が参る, and being captivated, as in 笑顔に参った. Separate actual movement from the subsidiary in 寒くなってまいりました. The temple or grave sense does not automatically inherit the motion sense’s humble label. [デジタル大辞泉 · 参る](https://kotobank.jp/word/参る-633122) [Agency for Cultural Affairs · 敬語の指針](https://www.bunka.go.jp/seisaku/bunkashingikai/kokugo/hokoku/pdf/93927001_09.pdf)

### 20. です

**do not use** — The two core uses deserve to stay, but the form list mixes inflections with multiword negative equivalents and underspecifies でしょう.

The copular noun or な-adjective construction and the polite ending after an i-adjective are distinct. 今日も暑かったです is a useful test of the second. ではありません and じゃありません are useful negative counterparts in a teaching paradigm, built with ある and ます. In this schema put such constructions in frames or a paradigm note. Polite=Form alone cannot distinguish でしょう from です. [デジタル大辞泉 · です](https://kotobank.jp/word/です-575878)

### 21. 俺

**use with corrections** — Keep the familiar label. Add オレ and describe the association with male speakers as a tendency.

The present-day standard-language sense needs one definition. The example already works. The register and speaker note together explain why a learner might hear this among friends yet choose 私 when introducing themselves at work. [NINJAL · 俺, おれ, オレ](https://kotoba.ninjal.ac.jp/mado/40/40-01/)

```json
{
  "language": "ja",
  "headword": "俺",
  "lexemes": [
    {
      "lemma": "俺",
      "pos": "PRON",
      "kind": "word",
      "spellings": [
        {
          "text": "俺",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "おれ",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "オレ",
          "script": "Kana",
          "varieties": [
            "ja-JP"
          ]
        }
      ],
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "おれ",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "くだけた会話で話し手自身を指す言葉",
          "example": "俺はもう少しここで待っているよ。",
          "cefr": "A2",
          "labels": {
            "formality": "informal",
            "politeness": "familiar"
          },
          "grammar": {
            "note": "Used mostly by men. Choice depends on the relationship and setting."
          }
        }
      ]
    }
  ]
}
```

### 22. コンピューター

**use with corrections** — Keep both katakana spellings and 台. Replace 電子の機械 and remove the claim that rapid processing defines a computer.

The previous example called the machine slow just after defining it by speed. The new example shows a familiar use. The two spelling-linked readings are citation readings. The absence of a final written ー need not force a short final vowel in speech. [NINJAL · loanword vowel length](https://repository.ninjal.ac.jp/record/1856/files/kk_nkss_016.pdf)

```json
{
  "language": "ja",
  "headword": "コンピューター",
  "lexemes": [
    {
      "lemma": "コンピューター",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {
        "countability": "Count",
        "classifier": "台",
        "note": "The shorter written form can also be pronounced with a long final vowel."
      },
      "spellings": [
        {
          "text": "コンピューター",
          "script": "Kana",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "コンピュータ",
          "script": "Kana",
          "varieties": [
            "ja-JP"
          ]
        }
      ],
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "こんぴゅーたー",
          "notation": "kana",
          "spelling": "コンピューター"
        },
        {
          "variety": "ja-JP",
          "reading": "こんぴゅーた",
          "notation": "kana",
          "spelling": "コンピュータ"
        }
      ],
      "senses": [
        {
          "definition": "指示に従って情報を自動で処理する機械",
          "example": "新しいコンピューターに写真を保存した。",
          "cefr": "A1"
        }
      ]
    }
  ]
}
```

### 23. 勉強

**use with corrections** — The three senses have dictionary support. Limit する to the appropriate senses and describe the bargaining context more precisely than “informal.”

The learning gained from experience in いい勉強になった deserves its own sense. The seller’s 勉強しますよ offers a discount. Neither requires a claim about grammatical mass nouns, and each can be taught without adding a separate verb headed 勉強. [漢字ペディア · 勉強](https://www.kanjipedia.jp/kotoba/0006290400)

```json
{
  "language": "ja",
  "headword": "勉強",
  "lexemes": [
    {
      "lemma": "勉強",
      "pos": "NOUN",
      "kind": "word",
      "grammar": {
        "note": "A verbal noun. する is licensed in the study and price-reduction senses."
      },
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "べんきょう",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "知識や技術を身につけるための学習",
          "example": "毎晩二時間、日本語を勉強しています。",
          "cefr": "A1",
          "frames": [
            "勉強する",
            "〜を勉強する",
            "〜の勉強をする"
          ]
        },
        {
          "definition": "これから役に立つ経験や教訓",
          "example": "失敗したけれど、いい勉強になりました。",
          "cefr": "B1",
          "frames": [
            "勉強になる"
          ]
        },
        {
          "definition": "売る側が客に応じて行う値引き",
          "example": "たくさん買ってくれたら、少し勉強しますよ。",
          "cefr": "B2",
          "frames": [
            "勉強する"
          ],
          "grammar": {
            "note": "Seller’s expression in bargaining or offering a better price."
          }
        }
      ],
      "spellings": [
        {
          "text": "勉強",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "べんきょう",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "勉強",
          "script": "Jpan",
          "pre_reform": true
        }
      ]
    }
  ]
}
```

### 24. 気をつける

**use with corrections** — Keep the fixed 気＋を and the inflecting つける. Broaden the definition beyond preventing danger or failure and explain the external に complement.

Paying attention to pronunciation can be ordinary practice. The reading きをつける preserves the conventional dictionary kana, while the note gives the spoken particle. 気がつく is a useful comparison. Historical reviving or signaling senses have little value in this everyday entry. [デジタル大辞泉 · 気を付ける](https://kotobank.jp/word/気を付ける-471909) [Sōseki letter · 氣をつける and 氣を付ける](https://upload.wikimedia.org/wikipedia/commons/0/01/NDL1127320_%E6%BC%B1%E7%9F%B3%E3%83%BB%E5%AF%85%E5%BD%A6%E3%83%BB%E4%B8%89%E9%87%8D%E5%90%89_part1.pdf)

```json
{
  "language": "ja",
  "headword": "気をつける",
  "lexemes": [
    {
      "lemma": "気をつける",
      "pos": "VERB",
      "kind": "multiword",
      "components": [
        {
          "text": "気",
          "lemma": "気",
          "inflects": "fixed"
        },
        {
          "text": "を",
          "lemma": "を",
          "inflects": "fixed"
        },
        {
          "text": "つける",
          "lemma": "付ける",
          "inflects": "head"
        }
      ],
      "spellings": [
        {
          "text": "気をつける",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "気を付ける",
          "script": "Jpan",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "きをつける",
          "script": "Hira",
          "varieties": [
            "ja-JP"
          ]
        },
        {
          "text": "氣を付ける",
          "script": "Jpan",
          "pre_reform": true
        },
        {
          "text": "氣をつける",
          "script": "Jpan",
          "pre_reform": true
        }
      ],
      "grammar": {
        "conjugation": "ichidan",
        "valency": "Intr",
        "note": "The particle を is pronounced お. The whole expression takes a target with に. 気 is the fixed object of 付ける."
      },
      "pronunciations": [
        {
          "variety": "ja-JP",
          "reading": "きをつける",
          "notation": "kana"
        }
      ],
      "senses": [
        {
          "definition": "大事な点を意識して注意を払う",
          "example": "道が凍っているので、転ばないように気をつけてください。",
          "cefr": "A2",
          "frames": [
            "〜に気をつける",
            "〜ように気をつける"
          ]
        }
      ],
      "relations": [
        {
          "type": "confusable",
          "lemma": "気がつく",
          "pos": "VERB",
          "why": "気をつける means to pay attention to something. 気がつく means to notice something."
        }
      ]
    }
  ]
}
```

## Seven decisions about the Japanese format

### 1. What pronunciation should the learner see?

Keep the hiragana reading searchable. Add an optional, sourced Tokyo pitch-accent pattern for each reading. A compact downstep number works well. **0** means no lexical downstep, and **n** means the drop follows mora n. The Tokyo patterns for 箸・橋・端 are はし [1], はし [2] and はし [0]. A learner can hear the difference between [2] and [0] when が follows the word. Count ん, っ and the second mora of a long vowel separately. Show a pitch trace or audio where available, allow accepted variants, and store an unknown accent as missing. Romanization can be a beginner display option. Conventional yomi and a sound-oriented kana rendering also need distinguishable display roles, as きをつける and its spoken particle illustrate. [Sanseido · 日本語アクセント入門](https://dictionary.sanseido-publ.co.jp/wordpress/wp-content/themes/dictionaryandbeyond/pdf/ssd36531.pdf) [University of Tokyo · OJAD](https://www.gavo.t.u-tokyo.ac.jp/ojad/) [Agency for Cultural Affairs · modern kana usage](https://www.bunka.go.jp/kokugo_nihongo/sisaku/joho/joho/kijun/naikaku/gendaikana/honbun_dai2.html)

### 2. Which spellings belong?

Keep dictionary citation forms and script tags. きれい・綺麗・奇麗 are legitimate, and オレ and えんぴつ are established print choices. Retain both コンピューター and コンピュータ. Record 來る or 氣を付ける with the historical flag. Reserve that flag for historical orthography. The uncommon spelling current 鹹い belongs specifically with the salty sense. For shared kanji, bind a reading to its lexeme or sense: 辛い has two adjective lexemes, and figurative 明日 generally reads あす. A pronunciation alone is insufficient evidence to enter a hiragana spelling. Keep furigana, spoken particle values and independent spellings distinct in the interface. [デジタル大辞泉 · 綺麗](https://kotobank.jp/word/綺麗-480543) [NINJAL · 俺, おれ, オレ](https://kotoba.ninjal.ac.jp/mado/40/40-01/) [NINJAL · loanword vowel length](https://repository.ninjal.ac.jp/record/1856/files/kk_nkss_016.pdf) [Sōseki letter · 氣をつける and 氣を付ける](https://upload.wikimedia.org/wikipedia/commons/0/01/NDL1127320_%E6%BC%B1%E7%9F%B3%E3%83%BB%E5%AF%85%E5%BD%A6%E3%83%BB%E4%B8%89%E9%87%8D%E5%90%89_part1.pdf)

### 3. Which grammar distinctions should be visible?

Keep godan, ichidan, irregular, i and na. Record the exceptional form on the appropriate verb: 行く is godan with 行って and 行った. Mark sense-specific valency where it changes and link a true partner with its reading and contrasting particles, as in 窓が開く（あく）／窓を開ける（あける）. Add a searchable する capability for verbal nouns, with frames on the senses that license it. Record の as an attributive pattern where warranted, as in 永遠の命. An example such as 緑の葉 also illustrates ordinary noun modification. Distinguish these patterns from な and from ordinary の possession. Allow a 連体詞 subtype for invariable adnominals such as 来る（きたる）. A regular に form alone is insufficient evidence for a new adverb, whereas independent 好きを… justifies a noun. For Japanese nouns, omit Count/Mass when the label adds no dependable usage guidance. [デジタル大辞泉 · 来る](https://kotobank.jp/word/来る-474513) [デジタル大辞泉 and 日本国語大辞典 · 好き](https://kotobank.jp/word/好き-540943) [漢字ペディア · 勉強](https://www.kanjipedia.jp/kotoba/0006290400)

### 4. Are the counters right, and can a noun have more than one?

Yes for the intended units: 鉛筆一本 is one pencil, 本一冊 is one volume, and コンピューター一台 is one machine. Keep the primary counter easy to find. Allow additional counters with a meaning or counting unit when needed. For example, 一冊の本 counts one volume, while 一部 can count a copy of a publication and 一巻 can identify a volume in a series. 一本（いっぽん）, 二本（にほん） and 三本（さんぼん） belong to the counter’s own number-dependent pronunciation record. A package or box of pencils is a different unit of sale. [日本国語大辞典 · 本](https://kotobank.jp/word/本-631932) [小学館 · units for counting](https://kotobank.jp/word/名数ものの数え方の例-1614580)

### 5. Do the four politeness labels work?

Keep the four learner-facing values with a neutral default. いらっしゃる honors its subject. です expresses politeness toward the listener. 参る in a formal movement announcement is 謙譲語II／丁重語, which conveys deference to the listener even without a respected destination. 俺 is familiar and informal, and its gender association belongs in a usage note. For a finer layer, the Agency for Cultural Affairs distinguishes 尊敬語, 謙譲語I, 謙譲語II, 丁寧語 and 美化語. Familiarity concerns the social setting. Store these distinctions per sense, so that 暑さに参る does not acquire a humble label. [Agency for Cultural Affairs · 敬語の指針](https://www.bunka.go.jp/seisaku/bunkashingikai/kokugo/hokoku/pdf/93927001_09.pdf) [デジタル大辞泉 · いらっしゃる](https://kotobank.jp/word/いらつしやる-3207277) [NINJAL · 俺, おれ, オレ](https://kotoba.ninjal.ac.jp/mado/40/40-01/)

### 6. Would JLPT improve the level field?

Keep an editorial CEFR estimate per sense for cross-language comparison. An optional, explicitly estimated N5–N1 field helps learners preparing for the JLPT. Give its source or grading method and leave it empty when evidence is weak. The JLPT publishes no current exhaustive vocabulary syllabus by level. Its CEFR reference indicators apply to qualifying test scores in the skills assessed. They do not establish a conversion for individual words or senses. A beginner can know literal 明日 well before recognizing its figurative use. [Official JLPT FAQ](https://www.jlpt.jp/e/faq/index.html)

### 7. Do the length limits produce good definitions?

All submitted definitions and examples pass the stated character limits when punctuation counts as one character. Some still fail as definitions. 高い’s first gloss contains two statements, and 閉まっていない状態になる gives the learner little help visualizing what opens. Keep the limits as an editing check. Use one dictionary-style predicate for a verb or adjective, a noun phrase for a noun, and a short use description for a particle. Count each kana, kanji, long-vowel mark and punctuation mark once. Use examples to reveal a distinction the definition leaves abstract, such as an honest election versus a tidy arrangement under きれい.
