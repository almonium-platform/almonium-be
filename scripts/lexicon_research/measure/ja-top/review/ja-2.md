# Japanese: entries to check (2 of 2)

These are entries of a learner's dictionary of Japanese, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of Japanese.

## The faults

- **fact**: something the entry states is false. A gender, a plural or another form, a part of
  speech, an auxiliary, a transcription that gives the wrong sound (not a matter of notation), a
  definition that gives a meaning the word does not have.
- **claim**: a restriction stated too strongly or wrongly. A region ("only" where the sense is used
  elsewhere too), a position (only before a noun), a number or countability, a label (formal,
  dated, offensive) the sense does not deserve.
- **missing-core**: the entry lacks one of the two or three commonest meanings or parts of speech of
  the word, one a learner at B1 or B2 meets in ordinary text.
- **missing-minor**: it lacks a meaning that a learner's dictionary lists but that is rarer than
  those the entry has. Not technical, dialectal or obsolete meanings: those are left out on purpose.
- **split**: two numbered senses are one meaning, or a mere construction or an idiom is numbered as
  a sense.
- **wording**: a definition is narrower or wider than its sense, an example does not show its
  sense, or an example is not natural Japanese.

Do not report: the order of senses, a CEFR level off by one step, how syllables are divided, a
definition you would have worded differently when the given one is true, or anything about style.

These are this dictionary's own definitions, not faults:

- valency Refl stands for every use with a reflexive pronoun, whatever a grammar of Japanese calls it
  (reflexive, reciprocal, pronominal); Tr means the verb takes a direct object, so a verb whose
  object needs a preposition or the dative is Intr.
- a grammar value followed by "mainly" holds for most uses, not all ("position Attr, mainly"
  is "usually before a noun"); without it the value is claimed without exception.
- a headword that is also an inflected form of another word (given, ladies), an abbreviation or a
  name has only the lexemes it is the lemma of. The reader reaches the other word by a link that
  is not shown here.
- a frame is the word with what it governs, every open place marked (depend on sth); a collocation
  is the word with a particular word it usually goes with, or a set phrase, with no open place
  (heavy rain, in the lead). At most three of each, so a missing one is not a fault.
- a word is said as a reading in hiragana, not in IPA. "accent" after it is the pitch accent of Tokyo as
  dictionaries number it: the mora after which the pitch drops, 0 when it does not drop. A reading without
  one is not a fault: the writer gives it only when sure. A wrong one is a fact.
- a frame is a pattern with 〜 for an open place and its particle written (〜に行く). A use after the て form
  with a meaning of its own (〜ている, 〜てくる) is numbered as a sense on purpose, and so is a particle's or
  a formal noun's use in grammar (〜ことがある): do not report these as split.
- suru true means the noun makes a verb with する. A noun has no countability. A definition is one
  predicate or one noun phrase of 10 to 35 characters.

## The answer

One line of JSON for every entry, in the order given, inside a single ```json block. An entry
without a fault has an empty list:

```json
{"id": 1, "findings": []}
{"id": 2, "findings": [{"fault": "claim", "where": "sense 2 regions", "what": "also used in Austria, so not 'only' Germany", "misleads": true}]}
```

"where" names the place (lexeme and sense by number, and the field). "what" is one sentence.
"misleads" is true when a learner would come away believing something false or would not find the
meaning in front of them, false when the fault is a blemish.

## The entries

### 51. 人

**Lexeme 1: 人**, NOUN
- grammar: classifier 人; note 名 is the polite counter.
- said:  ひと, accent 0 (ja-JP)
- spellings: 人 (ja-JP); ひと (ja-JP)
  1. [A1] 言葉を使い、社会をつくって暮らす生き物
     - example: 人は一人では生きていけないものだ。
  2. [A1] ある一人の者や、ある者たち
     - example: 駅の前で知らない人に道を聞かれた。
  3. [A2] 自分ではない、ほかの者
     - example: 人の話は最後までちゃんと聞きなさい。
     - frames: 人の〜
  4. [B1] その者が生まれつき持っている性質
     - example: 彼は人がいいから、頼まれると断れない。
     - collocations: 人がいい; 人が悪い
**Lexeme 2: 人**, NOUN
- grammar: note Counter for people; follows a numeral.
- said:  にん (ja-JP)
- forms: 一人 [NumType=Card] ひとり (one person); 二人 [NumType=Card] ふたり (two people); 四人 [NumType=Card] よにん (four people)
  1. [A1] 何名いるかを数えるのに使う言葉
     - example: このクラスには学生が三十人いる。
     - frames: 〜人

### 52. 女性

**Lexeme 1: 女性**, NOUN
- grammar: classifier 人; note More neutral and polite than 女; usual in news and formal writing.
- said:  じょせい, accent 0 (ja-JP)
- parts: 女 (女) + 性 (性)
  1. [A2] 男と対になる性の人、特に成人した人
     - example: この会社では多くの女性が管理職として働いている。
     - opposites: 男性

### 53. もらう

**Lexeme 1: もらう**, VERB
- grammar: conjugation godan; valency Tr; note Usually written in kana; 貰 is not a jōyō kanji. Humble equivalent: いただく.
- said:  もらう, accent 0 (ja-JP)
- spellings: もらう (ja-JP); 貰う (ja-JP)
  1. [A1] 人から差し出された物を受け取って自分のものにする
     - example: 誕生日に祖母から手紙と本をもらった。
     - frames: 〜に〜をもらう; 〜から〜をもらう
  2. [A2] 他人の行為で自分が恩恵を受けることを表す
     - example: 友達に駅まで車で送ってもらった。
     - frames: 〜に〜てもらう; 〜てもらいたい
     - grammar: note Used after the て form of a verb; the person who acts takes に.
  3. [B2] ほかの人から病気をうつされる
     - example: 弟から風邪をもらって、三日も寝込んだ。
     - labels: figurative True
  4. [B2] 人を家族として自分の家に迎え入れる
     - example: 子どものいない夫婦が養子をもらった。
     - frames: 〜を嫁にもらう; 〜を養子にもらう
  5. [B2] 勝負の勝ちを自分のものと決めてしまう
     - example: あと一点入れれば、この試合はもらったも同然だ。
     - labels: formality informal
     - grammar: note Usually in the past form: この勝負はもらった.
- relations: confusable くれる (VERB): both describe receiving, but くれる takes the giver as subject and もらう the receiver

### 54. 現在

**Lexeme 1: 現在**, NOUN
- grammar: note Also used without a particle as an adverb: 現在、〜している.
- said:  げんざい, accent 1 (ja-JP)
  1. [A2] 過ぎた時とこれから来る時の間にある、今のこの時
     - example: 現在、この町には約三千人が住んでいる。
     - frames: 現在の〜
     - collocations: 現在では
  2. [B2] ある日時を基準にしたときの、その時点
     - example: 四月一日現在の人口は約五万人です。
     - frames: 〜現在; 〜現在で
     - labels: formality formal
     - grammar: note Follows a date or time, often in reports and statistics.

### 55. いつも

**Lexeme 1: いつも**, ADV
- said:  いつも, accent 1 (ja-JP)
- spellings: いつも (ja-JP); 何時も (ja-JP)
  1. [A1] どんな時でも変わらずに
     - example: 父はいつも朝六時に起きて散歩をする。
**Lexeme 2: いつも**, NOUN
- said:  いつも, accent 1 (ja-JP)
- spellings: いつも (ja-JP); 何時も (ja-JP)
  1. [A2] 特別でない、ふだんどおりの状態や物事
     - example: 今日は駅までの道がいつもより混んでいる。
     - frames: いつもの〜; いつもより〜
     - collocations: いつもと違う

### 56. やつ

**Lexeme 1: やつ**, NOUN
- grammar: note 奴 is common in print but やつ is not an official (jōyō) reading, so newspapers write it in kana.
- said:  やつ, accent 1 (ja-JP)
- spellings: やつ (ja-JP); 奴 (ja-JP); ヤツ (ja-JP)
  1. [B1] 人を軽んじたり親しみをこめたりして指す言い方
     - example: 彼は口は悪いが、本当はいいやつだ。
     - frames: 〜なやつ
     - labels: formality informal
     - grammar: note Rude or affectionate depending on the context and the words before it.
  2. [B1] 物や事柄をくだけて指す言い方
     - example: その赤いやつを一つください。
     - labels: formality informal
**Lexeme 2: やつ**, PRON
- said:  やつ, accent 1 (ja-JP)
- spellings: やつ (ja-JP); 奴 (ja-JP); ヤツ (ja-JP)
  1. [B2] その場にいない第三者をぞんざいに指す言葉
     - example: やつがまた遅刻したら、もう待たないぞ。
     - labels: formality informal, attitude derogatory

### 57. 聞く

**Lexeme 1: 聞く**, VERB
- grammar: conjugation godan; valency Tr; note 聴く is used for attentive listening (music, lectures); 訊く is an optional spelling for asking.
- said:  きく, accent 0 (ja-JP)
- spellings: 聞く (ja-JP); きく (ja-JP); 聴く (ja-JP); 訊く (ja-JP)
  1. [A1] 音や声を耳で受け止めて感じ取る
     - example: 毎朝ラジオでニュースを聞いています。
     - frames: 〜を聞く
  2. [A1] 知りたいことを人にたずねる
     - example: 分からないことがあったら、遠慮なく私に聞いてください。
     - frames: 〜に〜を聞く
  3. [A2] 人から話やうわさを伝えられて知る
     - example: 彼女が来月引っ越すと友人から聞いた。
     - frames: 〜から聞く; 〜と聞く
  4. [B1] 人の言うことや頼みを受け入れて従う
     - example: この子は親の言うことを全然聞かない。
     - frames: 〜の言うことを聞く
     - collocations: 頼みを聞く
- relations: confusable 聞こえる (VERB): intransitive partner: 聞こえる means a sound reaches one's ears without effort, 聞く is to listen or hear actively; confusable 効く (VERB): same reading きく, but means to be effective

### 58. 以上

**Lexeme 1: 以上**, NOUN
- said:  いじょう, accent 1 (ja-JP)
  1. [A2] 数や程度がそれと同じか、それより多いこと
     - example: 二十歳以上の人は、この用紙に記入してください。
     - frames: 〜以上; 〜以上の〜
     - collocations: それ以上
     - opposites: 以下
  2. [B1] それより前に述べたこと
     - example: 以上の理由から、計画を延期することにしました。
     - frames: 以上の〜
     - opposites: 以下
  3. [B2] 前の事柄を理由として、当然あとのようになると述べるのに使う語
     - example: 約束した以上、最後まで責任を持つつもりだ。
     - frames: 〜た以上(は); 〜する以上(は)
  4. [B1] 話や文書の終わりに置き、これで終わりだと示すのに使う語
     - example: 報告は以上です。ご清聴ありがとうございました。
     - collocations: 以上です

### 59. 彼女

**Lexeme 1: 彼女**, PRON
- said:  かのじょ, accent 1 (ja-JP)
- spellings: 彼女 (ja-JP); かのじょ (ja-JP)
- parts: 彼 (彼) + 女 (女)
  1. [A1] 話し手と聞き手以外の女性を指す言葉
     - example: 彼女は毎朝駅まで歩いて通っている。
     - grammar: note Often avoided when speaking of a superior or someone present.
**Lexeme 2: 彼女**, NOUN
- grammar: classifier 人
- said:  かのじょ, accent 1 (ja-JP)
- spellings: 彼女 (ja-JP); カノジョ (ja-JP)
  1. [A2] 恋人として付き合っている女性
     - example: 兄に最近彼女ができたらしい。
     - frames: 〜の彼女
     - collocations: 彼女ができる
     - labels: formality informal
- relations: derived_from 彼女 (PRON)

### 60. 会う

**Lexeme 1: 会う**, VERB
- grammar: conjugation godan; valency Intr; note 逢う is a literary spelling, used especially for meeting someone dear.
- said:  あう, accent 1 (ja-JP)
- spellings: 会う (ja-JP); あう (ja-JP); 逢う (ja-JP)
  1. [A1] 人と同じ場所に来て、顔を合わせる
     - example: 週末に駅前で友達と会う約束をしている。
     - frames: 〜に会う; 〜と会う
     - opposites: 別れる
- relations: confusable 合う (VERB): same reading あう; 合う means to fit or match

### 61. 夜

**Lexeme 1: 夜**, NOUN
- grammar: note Read よ mainly in set phrases and literary style (夜が明ける, 夜も更ける).
- said:  よる, accent 1 (ja-JP),  よ (ja-JP, less_common)
- spellings: 夜 (ja-JP); よる (ja-JP)
  1. [A1] 日が沈んでから次の朝までの暗い間
     - example: 夜になると、この通りは急に静かになる。
     - frames: 夜に〜
     - collocations: 夜が明ける; 夜が更ける
     - opposites: 昼

### 62. いえ

**Lexeme 1: いえ**, INTJ
- said:  いえ (ja-JP)
  1. [A2] 相手の言うことを打ち消すときに使う言葉
     - example: いえ、私はまだ何も聞いていません。
     - grammar: note A shorter, lighter form of いいえ. Often doubled (いえいえ) to modestly refuse thanks or praise.
     - opposites: はい
- relations: derived_from いいえ (INTJ)

### 63. 生きる

**Lexeme 1: 生きる**, VERB
- grammar: conjugation ichidan; valency Intr
- said:  いきる, accent 2 (ja-JP)
- spellings: 生きる (ja-JP); いきる (ja-JP); 活きる (ja-JP)
  1. [A2] 命を持ち、死なずに存在している
     - example: 祖父は九十歳まで元気に生きた。
     - opposites: 死ぬ
  2. [B1] ある目的や立場のもとで暮らしていく
     - example: これからは画家として生きていくつもりだ。
     - frames: 〜として生きる; 〜に生きる; 〜で生きていく
  3. [B2] 持っている力や価値が十分に発揮される
     - example: 留学の経験が今の仕事に生きている。
     - frames: 〜が〜に生きる
     - grammar: note Often in the form 生きている.
  4. [C1] 表現に勢いがあり、本物のように感じられる
     - example: この小説は登場人物の会話がよく生きている。
     - labels: figurative True
     - grammar: note Usually in the form 生きている.
- relations: confusable 生かす (VERB): 生きる is intransitive; 生かす is its transitive partner, to keep alive or make good use of

### 64. 以外

**Lexeme 1: 以外**, NOUN
- said:  いがい, accent 1 (ja-JP)
  1. [A2] そのものを除いた、ほかのもの
     - example: 日曜日以外は毎日店を開けています。
     - frames: 〜以外; 〜以外の〜; 〜以外に(は)〜ない
     - grammar: note Follows a noun or a verb in the plain form; usually used as a suffix-like noun.
- relations: confusable 意外 (ADJ): same reading いがい; means unexpected

### 65. そんな

**Lexeme 1: そんな**, ADJ
- grammar: position Attr; invariable True; note Points to what the listener said or has, or to something just mentioned.
- said:  そんな, accent 0 (ja-JP)
  1. [A2] 相手の言ったことや話に出たことと同じような
     - example: そんな話は今まで一度も聞いたことがない。
     - frames: そんな〜
     - collocations: そんなことはない
**Lexeme 2: そんな**, INTJ
- said:  そんな, accent 0 (ja-JP)
  1. [B1] 思いがけないことに驚き、信じられない気持ちを表す
     - example: そんな、今になって中止だなんてひどいよ。
     - labels: formality informal
     - grammar: note Often followed by a trailing phrase such as まさか or ひどい.

### 66. その

**Lexeme 1: その**, DET
- grammar: position Attr; invariable True
- said:  その, accent 0 (ja-JP)
- spellings: その (ja-JP); 其の (ja-JP)
  1. [A1] 聞き手の近くにある物や人を指して言う語
     - example: その箱をこちらに渡してください。
     - frames: その〜
  2. [A2] 前に話に出た物事を指して言う語
     - example: 駅前に新しい店ができたが、その店はいつも混んでいる。
     - frames: その〜
- relations: confusable それ (PRON): それ stands on its own; その is used only before a noun
**Lexeme 2: その**, INTJ
- said:  その (ja-JP)
- spellings: その (ja-JP); 其の (ja-JP)
  1. [B1] 言葉に詰まったときに間をつなぐのに使う語
     - example: その、実は少しお願いしたいことがあるんです。
     - labels: formality informal

### 67. 可能

**Lexeme 1: 可能**, ADJ
- grammar: conjugation na
- said:  かのう, accent 0 (ja-JP)
  1. [B1] そうすることができる見込みがある
     - example: この会議室は五十人まで利用が可能です。
     - frames: 〜が可能だ; 〜ことが可能だ
     - collocations: 可能な限り
     - opposites: 不可能

### 68. こんな

**Lexeme 1: こんな**, ADJ
- grammar: position Attr, mainly; note Stands before a noun without な; こんなだ is less common. Its adverbial form is こんなに.
- said:  こんな, accent 0 (ja-JP)
  1. [A2] 話し手の近くの物事や、いま話している事と同じような
     - example: こんな寒い日は、家でゆっくり休みたい。
     - frames: こんな〜
     - collocations: こんなこと

### 69. 新しい

**Lexeme 1: 新しい**, ADJ
- grammar: conjugation i
- said:  あたらしい, accent 4 (ja-JP)
- spellings: 新しい (ja-JP); あたらしい (ja-JP)
  1. [A1] 作られたり始まったりしてから、あまり時間がたっていない
     - example: 駅前に新しいパン屋ができたそうです。
     - opposites: 古い
  2. [A2] 今までになく、初めて現れたものである
     - example: これまでにない新しいやり方を試してみよう。
  3. [B1] 食べ物がとれたり作られたりしてから間もない
     - example: この店の魚はいつも新しくておいしい。
     - opposites: 古い

### 70. 存在

**Lexeme 1: 存在**, NOUN
- said:  そんざい, accent 0 (ja-JP)
  1. [B1] 人や物が実際にそこにあること
     - example: この村には今も古い習慣が存在している。
     - frames: 〜が存在する; 〜に存在する
     - grammar: suru True; valency Intr
  2. [B2] 周りにとって大きな意味や役割を持つ人や物
     - example: 彼女はこのチームにとって欠かせない存在だ。
     - frames: 〜にとって...な存在; 〜的な存在

### 71. 心

**Lexeme 1: 心**, NOUN
- said:  こころ (ja-JP)
- spellings: 心 (ja-JP); こころ (ja-JP); ココロ (ja-JP)
  1. [A2] 人が考えたり感じたりするもとになるはたらき
     - example: 友達の優しい言葉に、心が温かくなった。
  2. [B1] 相手のことを大切に思いやる気持ち
     - example: 母は心を込めて手紙を書いていた。
     - frames: 〜に心を込める; 心のこもった〜
  3. [B2] 物事の奥にある大切な意味や精神
     - example: 茶道の心を学ぶため、毎週教室に通っています。
     - frames: 〜の心
**Lexeme 2: 心**, NOUN
- grammar: note In this reading usually written 芯 today.
- said:  しん (ja-JP)
  1. [C1] 物のまん中にある大切な部分
     - example: 外は寒くて、体の心まで冷えてしまった。
     - frames: 〜の心まで

### 72. ああ

**Lexeme 1: ああ**, INTJ
- grammar: note 嗚呼 is a literary spelling, used only for the exclamation of feeling.
- said:  ああ, accent 1 (ja-JP)
- spellings: ああ (ja-JP); 嗚呼 (ja-JP)
  1. [A2] 驚いたり感心したり嘆いたりしたときに思わず出す声
     - example: ああ、今日は本当に疲れた。
     - frames: ああ、〜
  2. [A2] 相手の言葉に軽く応じて、承知したことを表す返事
     - example: ああ、いいよ。あとで持っていくね。
     - labels: formality informal
     - grammar: note Said to equals or those below; はい or ええ is used in polite speech.
**Lexeme 2: ああ**, ADV
- said:  ああ, accent 0 (ja-JP)
  1. [A2] 話し手からも聞き手からも離れた物事のようすを指して言う語
     - example: ああなってしまっては、もう手遅れだ。
     - frames: ああする
     - collocations: ああなる; ああだ

### 73. 思う

**Lexeme 1: 思う**, VERB
- grammar: conjugation godan; valency Tr; note 想う is used mainly for longing for or caring about someone.
- said:  おもう, accent 2 (ja-JP)
- spellings: 思う (ja-JP); おもう (ja-JP); 想う (ja-JP)
  1. [A1] 物事について判断や意見を心の中に持つ
     - example: この映画は前の作品よりおもしろいと思う。
     - frames: 〜と思う; 〜をどう思う
  2. [A2] これから何かをするつもりでいる
     - example: 来年は海外に留学しようと思っています。
     - frames: 〜（よ）うと思う
  3. [B1] ある気持ちや感じを心にいだく
     - example: 皆さんにお会いできて、うれしく思います。
     - frames: 〜を〜く思う; 〜に思う
  4. [A2] 前もってこうなるだろうと予想する
     - example: 駅までは思ったより時間がかかった。
     - frames: 思ったより〜; 〜と思ったら
  5. [B2] 人や物事を大切に心にかけて慕う
     - example: 遠く離れて暮らす家族のことを毎日思う。
     - frames: 〜を思う; 〜のことを思う
- relations: confusable 考える (VERB): both translate as 'think'; 考える is deliberate reasoning, 思う an opinion, feeling or intention

### 74. あげる

**Lexeme 1: 上げる**, VERB
- grammar: conjugation ichidan; valency Tr; note Kanji by sense: 上げる raise, 挙げる cite or hold, 揚げる fry or fly; giving is usually written in kana.
- said:  あげる, accent 0 (ja-JP)
- spellings: 上げる (ja-JP); あげる (ja-JP); 挙げる (ja-JP); 揚げる (ja-JP)
  1. [A1] 自分の物を他人に与えて、その人の物にする
     - example: 妹の誕生日に、手作りのケーキをあげるつもりだ。
     - frames: 〜に〜をあげる
     - grammar: note Used toward equals; さしあげる is more polite toward a superior.
  2. [A2] ほかの人のために何かをする
     - example: 道に迷った人を駅まで案内してあげた。
     - frames: 〜てあげる; 〜に〜てあげる
  3. [A2] 物を低い所から高い所へ移す
     - example: 質問がある人は手を上げてください。
     - frames: 〜を上げる
     - opposites: 下げる
  4. [B1] 数量や程度を今までより高くする
     - example: 来月から店の商品の値段を上げるそうだ。
     - frames: 〜を上げる
     - opposites: 下げる
  5. [A2] 熱い油に入れて食べ物を調理する
     - example: 今夜は家で天ぷらを揚げよう。
     - frames: 〜を揚げる
  6. [B1] 例や名前などをはっきり示す
     - example: 具体的な例を挙げて説明してください。
     - frames: 〜を挙げる
  7. [B2] 努力して成果や利益を得る
     - example: 新しい方法を取り入れて、大きな成果を上げた。
  8. [B2] 儀式などを正式に行う
     - example: 二人は来年の春に式を挙げる予定です。
     - frames: 〜を挙げる
- relations: confusable 上がる (VERB): intransitive partner: 上がる means to go up, 上げる to raise something

### 75. ずっと

**Lexeme 1: ずっと**, ADV
- said:  ずっと, accent 0 (ja-JP)
  1. [A1] 長い間、途切れることなく続けて
     - example: 昨日は朝から晩までずっと雨が降っていた。
  2. [A2] ほかと比べて、程度の差がとても大きく
     - example: 電車で行くほうがバスよりずっと早いですよ。
     - frames: 〜よりずっと〜; ずっと〜ほうが〜
  3. [B1] 時間や距離が大きく離れて
     - example: ずっと昔、この辺りは一面の海だったそうだ。
     - collocations: ずっと前; ずっと先; ずっと昔

### 76. 十

**Lexeme 1: 十**, NUM
- grammar: note 拾 is used for this number on cheques and legal documents so it cannot be altered.
- said:  じゅう, accent 1 (ja-JP)
- spellings: 十 (ja-JP); 拾 (ja-JP)
  1. [A1] 数の名前で、九より一つ多い数
     - example: このクラスには生徒が十人います。
     - frames: 十＋助数詞
**Lexeme 2: 十**, NUM
- said:  とお, accent 1 (ja-JP)
- spellings: 十 (ja-JP); とお (ja-JP)
  1. [A1] 物を一つずつ数えるときの、九つの次の数
     - example: 箱の中にみかんが十入っている。
  2. [A2] 年齢で、九つの次の年
     - example: 娘はこの春で十になります。

### 77. 上

**Lexeme 1: 上**, NOUN
- said:  うえ, accent 0 (ja-JP)
- spellings: 上 (ja-JP); うえ (ja-JP)
  1. [A1] ある物や場所より高いところ
     - example: 山の上に白い雲が浮かんでいる。
     - frames: 〜の上
     - opposites: 下
  2. [A1] 物の表面や、そこに接したところ
     - example: 机の上に鍵を置いておいたよ。
     - frames: 〜の上に
  3. [A2] 年齢や地位、能力などが他より高いほう
     - example: 兄は私より三つ上で、大学生です。
     - frames: 〜より上
     - opposites: 下
  4. [B2] ある物事に関する点や方面
     - example: 仕事の上での悩みを先輩に相談した。
     - frames: 〜の上で; 〜の上では
  5. [B2] ある事を済ませ、それをふまえた段階
     - example: よく考えた上で、お返事します。
     - frames: 〜た上で
     - grammar: note Often written in kana (うえで).
  6. [B1] ある事に、さらに別の事が加わること
     - example: この店は値段が安い上に、味もいい。
     - frames: 〜上に
**Lexeme 2: 上**, NOUN
- said:  じょう (ja-JP)
  1. [B2] 段階や順序を分けたうちの、最も上のもの
     - example: 上・中・下の三巻のうち、まだ上しか読んでいない。
     - opposites: 下
**Lexeme 3: 上**, NOUN
- said:  かみ (ja-JP)
  1. [C1] 川の水が流れてくるもとのほう
     - example: 小舟は川の上へとゆっくり進んでいった。
     - labels: style literary
     - opposites: 下

### 78. みんな

**Lexeme 1: みんな**, NOUN
- grammar: note A more colloquial form of みな. The kanji 皆 is more often read みな.
- said:  みんな (ja-JP)
- spellings: みんな (ja-JP); 皆 (ja-JP)
  1. [A1] その場やある集まりにいるすべての人
     - example: 週末はクラスのみんなで海へ行く予定です。
     - frames: みんなで〜する; みんなの〜; 〜のみんな
  2. [B1] ある範囲の物事のすべて
     - example: 知っていることはこれでみんなです。
**Lexeme 2: みんな**, ADV
- grammar: note A more colloquial form of みな.
- said:  みんな (ja-JP)
- spellings: みんな (ja-JP); 皆 (ja-JP)
  1. [A2] ある範囲のものが一つ残らず
     - example: 冷蔵庫にあった物は、弟がみんな食べてしまった。

### 79. 実際

**Lexeme 1: 実際**, NOUN
- said:  じっさい, accent 0 (ja-JP)
- spellings: 実際 (ja-JP); 實際 [older books only]
- parts: 実 (実) + 際 (際)
  1. [B1] 想像や理論ではない、物事の本当のありさま
     - example: 計画は立派だが、実際はなかなかうまくいかない。
     - frames: 実際に〜; 実際の〜; 実際は〜
**Lexeme 2: 実際**, ADV
- said:  じっさい, accent 0 (ja-JP)
- spellings: 実際 (ja-JP); 實際 [older books only]
- parts: 実 (実) + 際 (際)
  1. [B1] 事実そのとおりだと強く認める気持ちを表す
     - example: 実際、あの日の雨はひどかった。
- relations: derived_from 実際 (NOUN)

### 80. 色

**Lexeme 1: 色**, NOUN
- grammar: classifier 色; note Counted with 色 read しょく (三色 さんしょく). The Buddhist reading しき is a separate technical term.
- said:  いろ, accent 2 (ja-JP)
- spellings: 色 (ja-JP); いろ (ja-JP)
  1. [A1] 光の当たり方によって目に感じられる物の見え方
     - example: この花は朝と夕方で色が少し違って見える。
     - frames: 〜色をしている
  2. [B2] 顔や様子に表れる気持ちや気配
     - example: 長い会議の後で、みんなの顔に疲れの色が見えた。
     - frames: 〜の色が見える; 〜の色を隠せない
  3. [B2] そのものらしい趣や特徴
     - example: この祭りには土地の色がよく表れている。
     - frames: 〜の色が出る; 〜の色が濃い
     - labels: figurative True
  4. [C1] 男女の恋愛や情事にかかわること
     - example: この小説には色と欲に迷う人々が描かれている。
     - labels: style literary

### 81. 飲む

**Lexeme 1: 飲む**, VERB
- grammar: conjugation godan; note 呑む is often used for swallowing whole, accepting demands and being overwhelmed.
- said:  のむ, accent 1 (ja-JP)
- spellings: 飲む (ja-JP); のむ (ja-JP); 呑む (ja-JP)
  1. [A1] 液体などを口から体の中へ送り込む
     - example: 朝は必ずコーヒーを一杯飲みます。
     - frames: 〜を飲む
     - grammar: valency Tr
  2. [A2] 酒類を口にし、楽しんだり酔ったりする
     - example: 仕事の後、同僚と駅前の店で飲んだ。
     - frames: 〜と飲む
     - collocations: 飲みに行く
     - grammar: valency Both
  3. [B2] 相手の要求や条件をしかたなく受け入れる
     - example: 会社は組合の要求をのむしかなかった。
     - frames: 〜をのむ
     - grammar: valency Tr
  4. [B2] 勢いで包み込み、すっかり取り込んでしまう
     - example: 初めての大舞台の雰囲気に飲まれてしまった。
     - frames: 〜に飲まれる
     - grammar: valency Tr; note Mostly used in the passive.

### 82. 場合

**Lexeme 1: 場合**, NOUN
- said:  ばあい, accent 0 (ja-JP)
- spellings: 場合 (ja-JP); ばあい (ja-JP)
  1. [A2] ある状態や事態になったとき
     - example: 雨が降った場合、試合は中止になります。
     - frames: 〜場合(は); 〜の場合
  2. [B1] その時のありさまや事情
     - example: 今は冗談を言っている場合ではない。
     - frames: 〜場合ではない
     - collocations: 時と場合による

### 83. 前

**Lexeme 1: 前**, NOUN
- said:  まえ, accent 1 (ja-JP)
- spellings: 前 (ja-JP); まえ (ja-JP)
  1. [A1] 顔や体が向いている方向や、物の正面の側
     - example: 駅の改札口で友だちと待ち合わせて、店の前まで歩いた。
     - frames: 〜の前; 〜の前で
     - collocations: 前に進む
     - opposites: 後ろ
  2. [A1] ある時やできごとより早い時
     - example: 寝る前には必ず歯をみがきなさい。
     - frames: 〜前に; 〜の前に; 〜分前
     - opposites: 後
  3. [A2] 今より以前の、過ぎ去ったある時
     - example: この店には前にも一度来たことがある。
     - frames: 前の〜
     - collocations: 前に
  4. [A2] 順番や並びで先のほう
     - example: 前の人に続いて、順に中へ入ってください。
     - frames: 前の〜

### 84. 絶対

**Lexeme 1: 絶対**, NOUN
- said:  ぜったい, accent 0 (ja-JP)
  1. [B1] 何にも制限されず、必ず従うべきであること
     - example: この会社では、社長の命令は絶対だ。
     - frames: 〜は絶対だ; 絶対の〜
  2. [B2] ほかと比べたり対立したりするものがないこと
     - example: 世の中に絶対の正しさなどないと思う。
     - opposites: 相対
**Lexeme 2: 絶対**, ADV
- said:  ぜったい, accent 0 (ja-JP)
  1. [A2] どんなことがあっても間違いなく
     - example: この映画は絶対おもしろいから、ぜひ見てほしい。
     - frames: 絶対に〜; 絶対〜
     - grammar: note Used with or without に; without に it sounds more casual.
  2. [A2] 打ち消しの言葉と使い、強く否定する
     - example: 明日の約束は絶対に忘れないでね。
     - frames: 絶対に〜ない; 絶対〜ない

### 85. 終わる

**Lexeme 1: 終わる**, VERB
- grammar: conjugation godan; valency Intr, mainly; note 〜を終わる is also heard, but 終える is the usual transitive verb.
- said:  おわる, accent 0 (ja-JP)
- spellings: 終わる (ja-JP); 終る (ja-JP); おわる (ja-JP)
  1. [A1] 続いていたことが、そこでおしまいになる
     - example: 授業は三時に終わるので、そのあと会おう。
     - frames: 〜が終わる; 〜に終わる
     - opposites: 始まる
  2. [A2] ある動作を最後まですっかり済ませる
     - example: 借りた本はもう全部読み終わりました。
     - frames: 〜（動詞の連用形）終わる
     - grammar: note Follows the continuative (masu-stem) form of a verb, as in 読み終わる.
  3. [B2] 人や物事が力を失い、だめになる
     - example: あのチームはもう終わったと言われている。
     - labels: formality informal, figurative True
     - grammar: note Mostly in the past form 終わった.
- relations: confusable 終える (VERB): 終える is the transitive partner (〜を終える); 終わる is mainly intransitive (〜が終わる).

### 86. 三

**Lexeme 1: 三**, NUM
- grammar: note Joined to counters, it can voice the counter's first sound (三本 さんぼん, 三階 さんがい). 参 is used in formal documents.
- said:  さん, accent 0 (ja-JP)
- spellings: 三 (ja-JP); 参 (ja-JP)
  1. [A1] 数の名で、二に一を足した数
     - example: 三に三をかけると九になります。
     - frames: 三＋助数詞

### 87. すごい

**Lexeme 1: すごい**, ADJ
- grammar: conjugation i; note In casual speech すごい often stands before an adjective in place of すごく (すごい寒い); this is nonstandard.
- said:  すごい, accent 2 (ja-JP)
- spellings: すごい (ja-JP); 凄い (ja-JP); スゴイ (ja-JP)
  1. [A2] 程度が普通をはるかに超えている
     - example: 駅前はすごい人で、なかなか前に進めなかった。
     - frames: すごく〜
  2. [A2] とてもすぐれていて、驚くほどだ
     - example: 一人でこの小屋を建てたなんて、本当にすごいね。
     - frames: 〜はすごい
  3. [C1] ぞっとするほど恐ろしい
     - example: 彼女はすごい形相で部屋に飛び込んできた。
     - labels: style literary

### 88. 理由

**Lexeme 1: 理由**, NOUN
- grammar: classifier つ
- said:  りゆう, accent 0 (ja-JP)
- parts: 理 (理) + 由 (由)
  1. [A2] 物事がそうなった、またはそう考えるもとになる事情
     - example: 遅刻した理由を先生にきちんと説明した。
     - frames: 〜の理由; 〜という理由で; 〜を理由に
  2. [B2] 本当のわけを隠して表向きに言いたてること
     - example: 彼は何かと理由をつけて会議を休む。
     - collocations: 理由をつける

### 89. 死ぬ

**Lexeme 1: 死ぬ**, VERB
- grammar: conjugation godan; valency Intr
- said:  しぬ, accent 0 (ja-JP)
- spellings: 死ぬ (ja-JP); しぬ (ja-JP)
  1. [A2] 生き物の命がなくなる
     - example: 祖父は九十歳のとき、病気で静かに死んだ。
     - frames: 〜で死ぬ
     - opposites: 生きる
  2. [B2] 本来の働きや生き生きした様子が失われる
     - example: せっかくの才能が、今の職場では死んでいる。
     - labels: figurative True
     - opposites: 生きる

### 90. 問題

**Lexeme 1: 問題**, NOUN
- grammar: classifier 問; note 問 counts test questions; issues are counted with つ.
- said:  もんだい, accent 0 (ja-JP)
- parts: 問 (問) + 題 (題)
  1. [A1] 答えを出させるために出す問い
     - example: 今日の試験の問題は思ったより易しかった。
  2. [A2] 考えて解決しなければならない事柄
     - example: 環境の問題について、クラスで話し合います。
     - frames: 〜の問題; 〜という問題
  3. [B1] 困った点や面倒な出来事
     - example: この計画には、費用の面で大きな問題がある。
     - frames: 〜に問題がある
     - collocations: 問題になる; 問題を起こす

### 91. 強い

**Lexeme 1: 強い**, ADJ
- grammar: conjugation i
- said:  つよい, accent 2 (ja-JP)
- spellings: 強い (ja-JP); つよい (ja-JP)
  1. [A2] 力や勢い、程度が大きくてはげしい
     - example: 今日は風が強いので、傘がさしにくい。
     - frames: 〜が強い
     - opposites: 弱い
  2. [A2] 争いや試合で相手を負かす力がある
     - example: うちの学校の野球部は県内でいちばん強い。
     - opposites: 弱い
  3. [B1] 外からの力や影響に耐えて、傷みにくい
     - example: この植物は寒さに強く、冬でも枯れない。
     - frames: 〜に強い
     - opposites: 弱い
  4. [B1] 心がしっかりしていて、困難にくじけない
     - example: 彼女は気持ちが強いから、きっと立ち直れる。
     - collocations: 気が強い; 意志が強い
     - opposites: 弱い
  5. [B1] ある分野が得意で、よく知っている
     - example: 兄は機械に強いので、パソコンの設定を頼んだ。
     - frames: 〜に強い
     - opposites: 弱い

### 92. 持つ

**Lexeme 1: 持つ**, VERB
- grammar: conjugation godan; valency Both; note In the sense 'to last' it is often written in kana (もつ).
- said:  もつ, accent 1 (ja-JP)
- spellings: 持つ (ja-JP); もつ (ja-JP)
  1. [A1] 物を手に取ったり、手で支えたりする
     - example: 重そうな荷物ですね。一つ持ちましょうか。
     - frames: 〜を持つ
     - grammar: valency Tr
  2. [A1] 自分のものとして所有している
     - example: 姉は車を二台持っているが、ほとんど乗らない。
     - frames: 〜を持っている
     - grammar: valency Tr
  3. [A2] 物を身につけて運んだり携えたりする
     - example: 雨が降りそうだから、傘を持って出かけよう。
     - frames: 〜を持っていく; 〜を持ってくる
     - grammar: valency Tr
  4. [B1] ある気持ちや考えを心にいだく
     - example: 子どもたちは虫や植物に強い興味を持っている。
     - frames: 〜を持つ; 〜に〜を持つ
     - grammar: valency Tr
  5. [B1] 役目や費用を自分のこととして引き受ける
     - example: 今日の食事代は私が持つから、気にしないで。
     - frames: 〜を持つ
     - grammar: valency Tr
  6. [B1] その状態が長く保たれる
     - example: この暑さでは、買った魚は夕方までもたないだろう。
     - frames: 〜がもつ; 〜までもつ
     - grammar: valency Intr
  7. [B2] 会合や人との関係を設ける
     - example: 来週、地域の住民と話し合いの場を持つ予定だ。
     - frames: 〜を持つ; 〜と〜を持つ
     - grammar: valency Tr

### 93. 確認

**Lexeme 1: 確認**, NOUN
- grammar: suru True
- said:  かくにん, accent 0 (ja-JP)
  1. [A2] 事実かどうか、まちがいがないかをはっきりさせること
     - example: 出かける前に、窓が閉まっているかもう一度確認した。
     - frames: 〜を確認する; 〜か(どうか)確認する

### 94. 欲しい

**Lexeme 1: 欲しい**, ADJ
- grammar: conjugation i
- said:  ほしい, accent 2 (ja-JP)
- spellings: 欲しい (ja-JP); ほしい (ja-JP)
  1. [A1] 自分のものにしたいと強く思う
     - example: 新しい自転車がずっと欲しかったんです。
     - frames: 〜が欲しい
     - grammar: note States the speaker's own wish; for a third person, 欲しがる or 欲しいそうだ is used.
  2. [A2] ほかの人にそうしてもらいたいと望む
     - example: 子どもたちには自由に育ってほしいと思う。
     - frames: 〜てほしい; 人に〜てほしい; 〜ないでほしい
     - grammar: note Follows the te form; usually written in hiragana in this use.

### 95. やる

**Lexeme 1: やる**, VERB
- grammar: conjugation godan; valency Both; note Usually written in kana; 遣る is the kanji spelling.
- said:  やる, accent 0 (ja-JP)
- spellings: やる (ja-JP); 遣る (ja-JP)
  1. [A1] 何かの行為や作業を行う
     - example: 日曜日は午前中に宿題をやることにしている。
     - frames: 〜をやる
     - labels: formality informal
     - grammar: valency Tr; note A more casual word than する.
  2. [A2] 目下の人や動物・植物に物を与える
     - example: 毎朝、庭の花に水をやるのが私の仕事だ。
     - frames: 〜に〜をやる
     - grammar: valency Tr; note Toward people it sounds rough; あげる is politer.
  3. [B1] 目下の人のために何かをしてあげる
     - example: 弟が困っていたので、数学を教えてやった。
     - frames: 〜てやる
     - grammar: valency Tr; note After the て form of a verb.
  4. [B2] 決意や相手への反発を込めて、その動作をする
     - example: こんな会社、明日にでも辞めてやる。
     - frames: 〜てやる
     - labels: formality informal
     - grammar: note After the て form of a verb.
  5. [B1] 店などを開いて商売を営む
     - example: 祖父は駅前で小さな喫茶店をやっている。
     - frames: 〜をやっている
     - grammar: valency Tr
  6. [B2] 人を用事などのためにほかの所へ行かせる
     - example: 母は子どもを近所の店へ使いにやった。
     - frames: 〜を〜にやる
     - grammar: valency Tr
  7. [B2] 目や手などをある方向や場所へ向ける
     - example: 窓の外に目をやると、雪が降り始めていた。
     - frames: 〜に目をやる
     - grammar: valency Tr
  8. [B1] 周囲と関わりながら暮らしや仕事を続けていく
     - example: 新しい職場の人たちとうまくやれそうだ。
     - frames: 〜とうまくやる; 〜でやっていく
     - grammar: valency Intr
  9. [B2] 酒を飲んだり物を食べたりする
     - example: 仕事の後で、同僚と軽く一杯やろう。
     - collocations: 一杯やる
     - labels: formality informal
     - grammar: valency Tr
- relations: confusable あげる (VERB): both mean to give; やる is used toward inferiors, animals and plants, あげる is politer; confusable する (VERB): both mean to do; やる is more casual and cannot replace する in suru verbs like 勉強する

### 96. 意味

**Lexeme 1: 意味**, NOUN
- said:  いみ, accent 1 (ja-JP)
  1. [A2] 言葉や記号などが表している内容
     - example: この言葉の意味がわからないので、辞書で調べた。
     - frames: 〜の意味; 〜を意味する
     - grammar: suru True
  2. [B1] 言葉や行いの裏にある意図や理由
     - example: 彼女が急に黙った意味が、やっとわかった。
     - collocations: どういう意味
  3. [B1] 物事が持っている価値や大切さ
     - example: 今さら謝っても、もう意味がないよ。
     - collocations: 意味がある; 意味がない

### 97. か

**Lexeme 1: か**, PART
- said:  か (ja-JP)
  1. [A1] 文の終わりにつけて、質問の意を表す
     - example: 明日の会議には出席されますか。
     - frames: 〜ですか; 〜ますか; 〜ませんか
     - grammar: note With 〜ませんか it makes an invitation or a polite request.
  2. [A2] 二つ以上のものの中から一つを選ぶ意を表す
     - example: 週末は映画を見るか、本を読むつもりだ。
     - frames: 〜か〜; 〜か〜か
  3. [A2] 疑問の内容を文の一部として示す
     - example: 彼が来るかどうか、まだ分からない。
     - frames: 〜かどうか; 疑問詞＋〜か
  4. [A2] 疑問の言葉の後について、はっきりしない意を表す
     - example: 誰かが窓を開けたままにしたようだ。
     - frames: 疑問詞＋か
  5. [B1] 新たに気づいたことや驚きを表す
     - example: なんだ、もう全部終わったのか。
     - frames: 〜のか
     - collocations: そうか
     - labels: formality informal

### 98. 場所

**Lexeme 1: 場所**, NOUN
- said:  ばしょ, accent 0 (ja-JP)
- parts: 場 (場) + 所 (所)
  1. [A1] 人や物があったり、何かが行われたりするところ
     - example: 待ち合わせの場所は駅の前に決めた。
  2. [B1] 人や物が占める空間や広さ
     - example: この机は大きすぎて、部屋の場所を取る。
     - collocations: 場所を取る; 場所がある
  3. [C1] 大相撲で、決まった期間に開かれる興行
     - example: 九州での場所は毎年十一月に開かれる。
     - labels: domain sport

### 99. 続ける

**Lexeme 1: 続ける**, VERB
- grammar: conjugation ichidan; valency Tr
- said:  つづける, accent 0 (ja-JP)
- spellings: 続ける (ja-JP); つづける (ja-JP); 續ける [older books only]
  1. [A2] 物事を途中でやめずにそのまま行う
     - example: 彼女は仕事を続けながら大学に通っている。
     - frames: 〜を続ける
     - opposites: やめる
  2. [B1] ある動作をやめずにずっとする
     - example: 子どもたちは一時間も歩き続けた。
     - frames: 〜し続ける
     - grammar: note Attached to the masu-stem of another verb.
- relations: confusable 続く (VERB): intransitive partner: 〜が続く beside 〜を続ける

### 100. の

**Lexeme 1: の**, ADP
- said:  の (ja-JP)
  1. [A1] 名詞と名詞をつなぎ、持ち主や所属、性質などを示す
     - example: これは姉の自転車で、毎日学校に乗っていきます。
     - frames: AのB
  2. [B1] 名詞を修飾する節で主語を示す
     - example: 母の作ったカレーが一番おいしい。
     - frames: Aの〜するB
     - grammar: note Replaces が inside a clause that modifies a noun.
  3. [B1] 前と後ろが同じ人や物を指すことを表す
     - example: 医者である兄に相談しようと思い、医者の兄に電話した。
     - frames: AのB
**Lexeme 2: の**, PART
- said:  の (ja-JP)
  1. [A2] 動詞や形容詞などについて、名詞と同じ働きをさせる
     - example: 毎朝公園を走るのが好きです。
     - frames: 〜のが好き; 〜のを見る; 〜のは〜だ
  2. [A2] 話に出た物や人を、名前を言わずに指す
     - example: もっと大きいのはありませんか。
     - frames: 形容詞＋の; 名詞＋の
  3. [A2] 理由や事情を説明したり、やわらかく言い切ったりする
     - example: ごめんね、今日はちょっと頭が痛いの。
     - frames: 〜の。
     - labels: formality informal
     - grammar: note Comes at the end of a sentence; between friends it is used mostly by women and children.
  4. [A2] 上げ調子で言い、相手に説明を求めて尋ねる
     - example: どうしてそんなに急いでいるの？
     - frames: 〜の？
     - labels: formality informal
  5. [B1] 子どもや目下の人に、やわらかく命令する
     - example: もう遅いから、早く寝るの。
     - frames: 動詞の辞書形＋の。
     - labels: formality informal
