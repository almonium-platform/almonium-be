# Japanese: entries to check (1 of 2)

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

### 1. くらい

**Lexeme 1: くらい**, PART
- grammar: note くらい and ぐらい are interchangeable in most uses.
- said:  くらい (ja-JP, くらい),  ぐらい (ja-JP, ぐらい),  くらい (ja-JP, 位)
- spellings: くらい (ja-JP); ぐらい (ja-JP); 位 (ja-JP)
  1. [A2] 数量や時間がおおよそであることを表すのに使う
     - example: 駅まで歩いて十分くらいかかります。
     - frames: 〜くらい; 〜ぐらい
  2. [B1] 動作や状態の程度を、例を挙げて示すのに使う
     - example: 涙が出るくらい笑ってしまった。
     - frames: 〜くらい〜; 〜くらい〜はない
  3. [B1] 軽い程度のものとして取り上げるのに使う
     - example: 自分の部屋くらい自分で片付けなさい。
     - frames: 〜くらい(は); 〜くらいなら
**Lexeme 2: 暗い**, ADJ
- grammar: conjugation i
- said:  くらい, accent 0 (ja-JP)
- spellings: 暗い (ja-JP); くらい (ja-JP)
  1. [A1] 光が少なくて、物がよく見えない
     - example: 冬は五時になると、外はもう暗い。
     - opposites: 明るい
  2. [B1] 希望が感じられず、気持ちや雰囲気が沈んでいる
     - example: 暗いニュースばかり続いて、気が重くなる。
     - labels: figurative True
     - opposites: 明るい
  3. [B1] 色が黒っぽく、くすんでいる
     - example: このコートは暗い緑色で落ち着いて見える。
     - opposites: 明るい
  4. [B2] ある物事についてよく知らない
     - example: 引っ越したばかりで、この辺りの地理に暗い。
     - frames: 〜に暗い
     - opposites: 明るい

### 2. 時間

**Lexeme 1: 時間**, NOUN
- said:  じかん, accent 0 (ja-JP)
- parts: 時 (時) + 間 (間)
  1. [A1] 物事をするのにかかる、または使える長さ
     - example: 忙しくて、ゆっくり本を読む時間がない。
     - frames: 〜する時間
     - collocations: 時間がかかる; 時間がある
  2. [A1] 一日を二十四に分けた長さの単位
     - example: 駅から空港まではバスで二時間かかる。
     - frames: 数＋時間
     - grammar: note Used after a number as a counter of hours.
  3. [A2] 予定として決めた、一日の中のある一点
     - example: 約束の時間に遅れないよう、早めに家を出た。
     - frames: 〜の時間
     - collocations: 時間に遅れる; 時間を守る
  4. [A2] 学校などで、ある事のために区切ったひとまとまり
     - example: 次は理科の時間だから、教室を移動しよう。
     - frames: 〜の時間
  5. [B1] 過去から未来へと絶えず流れていくもの
     - example: どんなに願っても、流れていく時間は止められない。
     - collocations: 時間が経つ; 時間が過ぎる

### 3. 最近

**Lexeme 1: 最近**, NOUN
- grammar: note Often used as an adverb without a particle: 最近、忙しい.
- said:  さいきん, accent 0 (ja-JP)
  1. [A2] 今より少し前から今までの間
     - example: 最近、駅の前に新しいパン屋ができた。
     - frames: 最近の〜; 最近は〜

### 4. わ

**Lexeme 1: わ**, PART
- said:  わ (ja-JP)
  1. [A2] 文の終わりにつけて、軽い主張や感動を表す
     - example: あら、もうこんな時間になっていたわ。
     - frames: 〜わ; 〜わよ; 〜わね
     - labels: formality informal
     - grammar: note With rising pitch chiefly used by women; with falling pitch used by all, especially in western Japan.
  2. [B2] 同じような事柄を重ねて、驚きやあきれた気持ちを表す
     - example: 雨は降るわ風は吹くわで、ひどい一日だった。
     - frames: 〜わ〜わ; 〜わ〜わで
     - labels: formality informal

### 5. 関係

**Lexeme 1: 関係**, NOUN
- said:  かんけい, accent 0 (ja-JP)
- spellings: 関係 (ja-JP); 關係 [older books only]
  1. [A2] 二つ以上の物事の間にあるつながりや影響
     - example: 睡眠と健康の関係について調べている。
     - frames: 〜と〜の関係; 〜と関係がある; 〜の関係で
     - grammar: suru True; note 〜の関係で means 'because of, due to' (時間の関係で).
  2. [B1] 人と人との間のつながりや間柄
     - example: 隣の家族とはずっといい関係を保っている。
     - frames: 〜との関係; 〜と〜の関係
  3. [B1] ある物事に手を出したり、つながりを持ったりすること
     - example: 彼はその事件には関係していないと言っている。
     - frames: 〜に関係する; 〜には関係ない
     - grammar: suru True
  4. [B2] 仕事や学問などのある分野や方面
     - example: 姉は出版関係の会社で働いています。
     - frames: 〜関係
     - grammar: note Used directly after a noun naming a field.
  5. [C1] 男女の間の性的なつながり
     - example: 二人は以前から関係があったらしい。
     - frames: 〜と関係を持つ
     - labels: attitude euphemistic
     - grammar: suru True

### 6. 最後

**Lexeme 1: 最後**, NOUN
- said:  さいご, accent 1 (ja-JP)
  1. [A2] 順番や時間の流れの中で、いちばん終わり
     - example: 映画の最後の場面で思わず泣いてしまった。
     - frames: 最後の〜
     - collocations: 最後に; 最後まで
     - opposites: 最初
  2. [B2] 一度そうなったら、もうどうにもならないこと
     - example: あの人は話し始めたら最後、二時間は止まらない。
     - frames: 〜たら最後; 〜が最後
- relations: confusable 最期 (NOUN): same reading さいご; 最期 means the moment of death

### 7. 使う

**Lexeme 1: 使う**, VERB
- grammar: conjugation godan; valency Tr; note 遣う is also written for spending and for 気をつかう.
- said:  つかう, accent 0 (ja-JP)
- spellings: 使う (ja-JP); つかう (ja-JP); 遣う (ja-JP)
  1. [A1] 物や方法を目的のために役立てる
     - example: 辞書を使って、知らない言葉の意味を調べる。
     - frames: 〜を使う; 〜を使って〜する
  2. [A2] お金や時間、力などを費やす
     - example: 今月は本だけで一万円も使ってしまった。
     - frames: 〜に〜を使う
  3. [B1] 心や頭をあれこれと働かせる
     - example: 客に気を使いすぎて、すっかり疲れた。
     - collocations: 気を使う; 頭を使う
  4. [B1] 人を雇ったり働かせたりする
     - example: 彼は小さな工房で三人の職人を使っている。
     - collocations: 人を使う

### 8. 顔

**Lexeme 1: 顔**, NOUN
- said:  かお, accent 0 (ja-JP)
- spellings: 顔 (ja-JP); かお (ja-JP); 顏 [older books only]
  1. [A1] 頭の前の面で、目や鼻や口がある部分
     - example: 朝起きたら、まず冷たい水で顔を洗う。
     - frames: 〜を洗う
  2. [A2] 気持ちが表に出た目や口などのようす
     - example: 試験の結果を聞いて、弟はうれしそうな顔をした。
     - frames: 〜な顔をする
  3. [B2] 人々の前で保たれる名誉や立場
     - example: 私の失敗で、紹介してくれた先輩の顔をつぶしてしまった。
     - collocations: 顔をつぶす; 顔が立つ
     - labels: figurative True
  4. [B2] 広く人に知られていること、またそれによる力
     - example: 父はこの町の商店街ではかなり顔がきく。
     - collocations: 顔が広い; 顔がきく
     - labels: figurative True
  5. [B2] 団体や場所を代表する人や物
     - example: この駅は、町の新しい顔として親しまれている。
     - frames: 〜の顔
     - labels: figurative True

### 9. 男

**Lexeme 1: 男**, NOUN
- grammar: classifier 人
- said:  おとこ, accent 3 (ja-JP)
- spellings: 男 (ja-JP); おとこ (ja-JP); オトコ (ja-JP)
  1. [A1] 人の性別の一つで、女ではないほうの人
     - example: 公園で若い男が犬を連れて歩いていた。
     - opposites: 女
  2. [B2] 一人前の大人としての体面や値打ち
     - example: 約束をきちんと守って、彼は男を上げた。
     - collocations: 男を上げる; 男が立つ; 男がすたる
  3. [B2] 女性から見た恋人や愛人
     - example: 姉には最近、男ができたらしい。
     - collocations: 男ができる
     - labels: formality informal

### 10. あれ

**Lexeme 1: あれ**, PRON
- said:  あれ, accent 0 (ja-JP)
  1. [A1] 話し手からも聞き手からも遠くにある物を指す言葉
     - example: あれが私たちの泊まるホテルです。
  2. [A2] 話し手と聞き手がともに知っている物事や過去の時を指す言葉
     - example: あれから十年たったが、今でもよく覚えている。
     - collocations: あれから; あれ以来
  3. [B1] 名前がすぐに出ないものや言いにくいことを代わりに指す言葉
     - example: 今日はちょっとあれなので、また今度にしよう。
     - collocations: ちょっとあれで; あれだ
     - labels: formality informal
**Lexeme 2: あれ**, INTJ
- said:  あれ (ja-JP)
  1. [A2] 驚いたり不思議に思ったりしたときに出す言葉
     - example: あれ、ここに置いたはずの鍵がない。
     - labels: formality informal
     - grammar: note Often said as あれっ or あれれ.

### 11. 私

**Lexeme 1: 私**, PRON
- grammar: note わたくし is the more formal reading, used in speeches and to superiors.
- said:  わたし, accent 0 (ja-JP),  わたくし, accent 0 (ja-JP, careful)
- spellings: 私 (ja-JP); わたし (ja-JP); わたくし (ja-JP)
  1. [A1] 話し手や書き手が自分自身を指す言葉
     - example: 私は毎朝七時に家を出ます。
**Lexeme 2: 私**, NOUN
- said:  わたくし, accent 0 (ja-JP)
- spellings: 私 (ja-JP); わたくし (ja-JP)
  1. [C1] 社会や組織と関係のない、個人だけの事柄
     - example: 仕事には私の感情を持ち込まないようにしている。
     - labels: style literary
     - opposites: 公

### 12. から

**Lexeme 1: から**, ADP
- said:  から (ja-JP)
  1. [A1] 動作や時間の始まりとなる所や時を示す
     - example: 駅から家まで歩いて十分ほどかかります。
     - frames: 〜から; 〜から〜まで
  2. [A1] 動作の相手や、物や情報の出どころを示す
     - example: 先週、昔の友人から手紙が届いた。
     - frames: 〜からもらう; 〜から聞く
  3. [A2] 通り抜けていく場所を示す
     - example: 開いた窓から冷たい風が入ってくる。
     - frames: 〜から入る; 〜から出る
  4. [A2] ある動作が済んだあとに次のことが起こる意を示す
     - example: 手を洗ってから食事にしましょう。
     - frames: 〜てから
     - grammar: note Follows the て form of a verb.
  5. [B1] 物を作るときの原料を示す
     - example: このチーズは山羊の乳から作られている。
     - frames: 〜から作る; 〜からできる
  6. [B1] 原因やきっかけ、判断のよりどころを示す
     - example: 小さな不注意から大きな事故が起きた。
     - frames: 〜から見ると; 〜から判断する
  7. [B2] 数量がそれ以上であることを示す
     - example: 会場には千人からの人が集まった。
     - frames: 〜からの〜; 〜からする
**Lexeme 2: から**, PART
- said:  から (ja-JP)
  1. [A1] 前の事柄が後の事柄の理由であることを示す
     - example: 疲れたから、今日は早く寝ます。
     - frames: 〜から、〜; 〜のは〜からだ
  2. [B1] 相手に念を押したり、強い決意を示したりする
     - example: 今度遅れたら、もう待たないからね。
     - frames: 〜からね
     - labels: formality informal
     - grammar: note Used at the end of a sentence, often followed by ね or な.

### 13. 方

**Lexeme 1: 方**, NOUN
- grammar: note In the comparing and grammatical uses it is very often written in kana.
- said:  ほう, accent 1 (ja-JP)
- spellings: 方 (ja-JP); ほう (ja-JP)
  1. [A2] 比べたり分けたりしたもののうちの一つの側
     - example: こちらの方が少し安いので、これにします。
     - frames: 〜より〜の方が; 〜たほうがいい; 〜ないほうがいい
  2. [A2] 人や物がある、または進んでいく向き
     - example: 駅の方へまっすぐ歩いていくと公園があります。
     - frames: 〜の方へ; 〜の方に
  3. [B1] 比べてみると、どちらかといえば入る部類
     - example: 私は朝早く起きるのは得意なほうだ。
     - frames: 〜ほうだ
  4. [B1] ある事柄にかかわる方面や分野
     - example: 最近、仕事の方はうまくいっていますか。
     - frames: 〜の方は
**Lexeme 2: 方**, NOUN
- said:  かた, accent 2 (ja-JP)
  1. [A2] 相手や話題にしている人を敬って言う言葉
     - example: あちらの方がこの店の店長さんです。
     - labels: politeness honorific

### 14. 目

**Lexeme 1: 目**, NOUN
- grammar: note 眼 is used mainly for the eye and for the power of seeing.
- said:  め, accent 1 (ja-JP)
- spellings: 目 (ja-JP); め (ja-JP); 眼 (ja-JP)
  1. [A1] 顔にあって、物を見る働きをする器官
     - example: 目を閉じて、ゆっくり深呼吸してください。
  2. [A2] 物の形や色を見ることのできる力
     - example: 祖母は年をとって目が悪くなった。
     - collocations: 目がいい; 目が悪い
  3. [B1] 見ている人の視線や見方
     - example: 周りの人の目が気になって、うまく話せない。
     - frames: 〜に目を向ける
     - collocations: 人の目が気になる
  4. [B2] 物事のよしあしを見分ける力
     - example: 彼女は人を見る目が確かだ。
     - frames: 〜を見る目がある
  5. [B1] 身の上に起こる、ありがたくない体験
     - example: 雨の中で道に迷い、ひどい目にあった。
     - frames: 〜目にあう; 〜目を見る
  6. [B2] 網や布などのすき間や、並んだ筋や点
     - example: この網は目が細かいので小さな魚も逃げない。
     - collocations: 目が細かい; 目が粗い

### 15. へ

**Lexeme 1: へ**, ADP
- grammar: note Written へ but pronounced え as a particle.
- said:  え (ja-JP)
  1. [A1] 動作が向かう方向や行き先を示す
     - example: 夏休みには家族で海へ行くつもりです。
     - frames: 〜へ行く; 〜へ向かう; 〜への〜
  2. [A2] 動作や気持ちを向ける相手を示す
     - example: 遠くに住む祖母へ毎月手紙を書いている。
     - frames: 〜へ送る; 〜への〜
- relations: confusable に (ADP): Both mark a destination; へ stresses the direction, に the point reached.

### 16. 方法

**Lexeme 1: 方法**, NOUN
- said:  ほうほう, accent 0 (ja-JP)
- parts: 方 (方) + 法 (法)
  1. [A2] 目的を果たすための手順や手だて
     - example: この問題を解決する方法をみんなで考えよう。
     - frames: 〜する方法; 〜の方法

### 17. や

**Lexeme 1: や**, ADP
- said:  や (ja-JP)
  1. [A2] いくつかの物事を例として並べて挙げるのに使う
     - example: 机の上に本やノートが置いてある。
     - frames: AやB; AやBなど
     - grammar: note Joins nouns; unlike と, implies the list is not complete.
**Lexeme 2: や**, PART
- said:  や (ja-JP)
  1. [B1] あきらめの気持ちを込めて、軽く文を終えるのに使う
     - example: 雨が降ってきたし、散歩はもういいや。
     - frames: 〜でいいや
     - collocations: もういいや
     - labels: formality informal
     - grammar: note Sentence-final; often said to oneself.
  2. [B2] 相手を誘う気持ちを強めて文を終えるのに使う
     - example: せっかくだから、みんなで一緒に行こうや。
     - frames: 〜(よ)うや
     - labels: formality informal
     - grammar: note After the volitional form; used mostly by men, especially in western Japan.
  3. [C1] ある動作のすぐ後に次のことが起こることを表す
     - example: 玄関の戸を開けるや、猫が外へ飛び出した。
     - frames: 〜するや; 〜や否や
     - labels: style literary
     - grammar: note After the dictionary form of a verb.
**Lexeme 3: や**, INTJ
- said:  や (ja-JP)
  1. [B2] 驚いたときや急に気づいたときに出す声
     - example: や、こんなところで会うとは思わなかった。
**Lexeme 4: や**, AUX
- said:  や (ja-JP)
- forms: やった [Tense=Past] やった
  1. [B2] 物事をはっきりと言い切るのに使う
     - example: 今日はほんまにええ天気や。
     - labels: formality informal
     - grammar: note Kansai and western Japan dialect; corresponds to standard だ.

### 18. 楽しい

**Lexeme 1: 楽しい**, ADJ
- grammar: conjugation i
- said:  たのしい, accent 3 (ja-JP)
- spellings: 楽しい (ja-JP); たのしい (ja-JP)
  1. [A1] 満ち足りて、明るく愉快な気持ちだ
     - example: 友達と話していると、時間を忘れるほど楽しい。
     - frames: 〜が楽しい; 〜のが楽しい; 楽しく〜
     - opposites: つまらない

### 19. 気

**Lexeme 1: 気**, NOUN
- said:  き, accent 0 (ja-JP)
- spellings: 気 (ja-JP); 氣 [older books only]
  1. [A2] 物事に対する心の動きや生まれつきの性質
     - example: 彼は気が短くて、すぐに怒り出してしまう。
     - frames: 〜と気が合う
     - collocations: 気が強い; 気が弱い
  2. [A2] 物事に向ける注意や心配り
     - example: 細かいことを気にしすぎると疲れてしまうよ。
     - frames: 〜に気をつける; 〜を気にする; 〜に気を配る
  3. [B1] 何かをしようとする心の向きや意志
     - example: 今日はどうしても勉強する気が起きない。
     - frames: 〜する気がある; 〜する気になる
  4. [C1] その場にただよう、目に見えない感じ
     - example: 夜明けの山には澄んだ気が満ちている。
     - labels: style literary
  5. [C1] 体や自然の中を流れるとされる生命の力
     - example: ゆっくり呼吸をして、体に気をめぐらせましょう。

### 20. 誰

**Lexeme 1: 誰**, PRON
- said:  だれ, accent 1 (ja-JP),  たれ (ja-JP, older)
- spellings: 誰 (ja-JP); だれ (ja-JP)
  1. [A1] 名前や正体がわからない人を指して問う言葉
     - example: あそこで手を振っている人は誰ですか。
     - frames: 〜は誰ですか; 誰が〜; 誰の〜
     - grammar: note どなた is the polite equivalent. The reading たれ is found in older and literary texts.

### 21. 開催

**Lexeme 1: 開催**, NOUN
- grammar: suru True
- said:  かいさい, accent 0 (ja-JP)
  1. [B1] 人が集まる会や行事を計画して行うこと
     - example: 来年の大会はこの町で開催される予定だ。
     - frames: 〜を開催する; 〜で開催される

### 22. 人間

**Lexeme 1: 人間**, NOUN
- grammar: classifier 人
- said:  にんげん, accent 0 (ja-JP)
- spellings: 人間 (ja-JP); にんげん (ja-JP)
- parts: 人 (人) + 間 (間)
  1. [A2] 言葉や道具を使い、社会を作って生きる動物
     - example: 犬は人間よりずっと鼻がいいそうだ。
  2. [B2] 性格や品位など、その者としての中身
     - example: 彼は若いのに人間ができていて、誰にでも親切だ。
     - collocations: 人間ができている; 人間が小さい; 人間が大きい

### 23. 子

**Lexeme 1: 子**, NOUN
- grammar: classifier 人; note Animal young are counted with 匹 or 頭.
- said:  こ, accent 0 (ja-JP)
  1. [A2] 親から生まれた人、または親が育てる人
     - example: うちの子は今年で五歳になります。
     - opposites: 親
  2. [A2] まだ大人になっていない、年の小さい人
     - example: 公園で近所の子たちが元気に遊んでいる。
     - collocations: 男の子; 女の子; この子
  3. [B1] 動物の、親から生まれたもの
     - example: うちの猫が四匹の子を産んだ。
     - collocations: 子を産む
     - opposites: 親
  4. [B1] 親しみをこめて若い人、特に若い女性を指す言葉
     - example: 隣の部署に入った新しい子はとても明るい。
     - labels: formality informal
**Lexeme 2: 子**, NOUN
- said:  ね (ja-JP)
  1. [C1] 十二支の一番目で、ねずみに当たるもの
     - example: 昔は夜中の十二時ごろを子の刻と呼んだ。
     - collocations: 子の刻; 子年

### 24. そう

**Lexeme 1: そう**, ADV
- said:  そう, accent 1 (ja-JP)
  1. [A1] 相手が言ったことや話に出たことのように
     - example: 私もそう思いますが、もう少し考えてみます。
     - frames: そうする
     - collocations: そう思う; そうだ
  2. [B1] 考えていたほどの程度には
     - example: この問題はそう難しくないから、すぐ解けるよ。
     - frames: そう〜ない
     - grammar: note Used with a negative that follows.
**Lexeme 2: そう**, INTJ
- said:  そう, accent 1 (ja-JP)
  1. [A1] 相手の言葉を聞いて、同意したり受け止めたりするときに言う語
     - example: そう、その調子で最後まで頑張って。
     - grammar: note With rising intonation (そう？) it shows mild doubt or surprise.
  2. [B1] ふと思い出したときや話を切り出すときに言う語
     - example: そうそう、明日の会議は十時からだよ。
     - collocations: そうそう

### 25. 円

**Lexeme 1: 円**, NOUN
- said:  えん, accent 1 (ja-JP)
- spellings: 円 (ja-JP); 圓 [older books only]
  1. [B1] 一つの点から同じ距離にある点を結んだ丸い形
     - example: コンパスを使って、紙に大きな円を描いた。
     - collocations: 円を描く; 円になる
  2. [B1] 日本で使われている通貨
     - example: 円が安くなると、海外旅行の費用が上がる。
     - collocations: 円が高くなる; 円に替える
**Lexeme 2: 円**, NOUN
- grammar: note Counter for amounts of Japanese money; follows a number.
- said:  えん (ja-JP)
- spellings: 円 (ja-JP); 圓 [older books only]
- forms: 四円 [NumType=Card] よえん (four yen)
  1. [A1] 日本のお金の額を数える単位
     - example: このパンは一つ百五十円で売っています。
     - frames: 〜円

### 26. 少し

**Lexeme 1: 少し**, ADV
- said:  すこし, accent 2 (ja-JP)
- spellings: 少し (ja-JP); すこし (ja-JP)
  1. [A1] 数や量、程度がわずかであるようす
     - example: 今日は昨日より少し寒いですね。
     - collocations: 少しずつ; 少しだけ
  2. [A1] 時間や距離がわずかであるようす
     - example: すぐに戻りますから、少し待っていてください。
**Lexeme 2: 少し**, NOUN
- said:  すこし, accent 2 (ja-JP)
- spellings: 少し (ja-JP); すこし (ja-JP)
  1. [A2] ほんのわずかな数や量のもの
     - example: ほんの少しの水でも、この花は元気になる。
     - frames: 少しの〜
     - collocations: ほんの少し

### 27. 月

**Lexeme 1: 月**, NOUN
- said:  つき, accent 2 (ja-JP)
- spellings: 月 (ja-JP); つき (ja-JP)
  1. [A1] 地球の周りを回り、夜空に明るく見える天体
     - example: 今夜は雲がなく、丸い月がよく見える。
  2. [A2] 一年を十二に分けた、その一つの期間
     - example: 月に一度は実家の両親に電話をかけます。
     - frames: 月に〜; 〜か月
     - grammar: note Counted with か月 (三か月, three months).
**Lexeme 2: 月**, NOUN
- grammar: note Counter used after a number to name a month of the year.
- said:  がつ (ja-JP)
- forms: 四月 [NumType=Ord] しがつ (the fourth month; not よんがつ); 七月 [NumType=Ord] しちがつ (the seventh month; not ななかつ); 九月 [NumType=Ord] くがつ (the ninth month; not きゅうがつ)
  1. [A1] 数の後につけて、一年のうち何番目の時期かを表す語
     - example: 私の誕生日は三月の終わりです。
     - frames: 〜月

### 28. 作品

**Lexeme 1: 作品**, NOUN
- grammar: classifier 点; note Films and novels are also counted with 本 or 作; short stories and poems with 編.
- said:  さくひん, accent 0 (ja-JP)
  1. [A2] 芸術などの活動によって生み出されたもの
     - example: この美術館には有名な画家の作品が数多く展示されている。

### 29. どう

**Lexeme 1: どう**, ADV
- said:  どう, accent 1 (ja-JP)
  1. [A1] 方法ややり方を尋ねるのに使う語
     - example: この漢字はどう読むのか教えてください。
     - frames: どう〜か; どうする
  2. [A1] 物事の様子や相手の考えを尋ねるのに使う語
     - example: 新しい仕事はどうですか。もう慣れましたか。
     - frames: 〜はどうですか; 〜をどう思う
  3. [A2] 相手に物事を勧めるのに使う語
     - example: 疲れているなら、少し休んだらどう？
     - frames: 〜はどう; 〜たらどう
     - grammar: note Said at the end of a suggestion; どうですか or いかがですか is more polite.
  4. [B1] どんな方法でも結果は変わらないことを表す語
     - example: どう頑張っても、締め切りには間に合わない。
     - frames: どう〜ても

### 30. 何

**Lexeme 1: 何**, PRON
- grammar: note Read なん before だ, で, の, a counter or a d/t/n sound (何人, 何ですか); otherwise なに.
- said:  なに, accent 1 (ja-JP),  なん, accent 1 (ja-JP)
- spellings: 何 (ja-JP); なに (ja-JP)
  1. [A1] わからない物事について問うときに使う言葉
     - example: 週末は何をする予定ですか。
     - frames: 何を〜; 何の〜; 何＋助数詞（何人・何時）
  2. [B2] 名前をはっきり言わずに物事を指す言葉
     - example: ちょっと、そこの何を取ってくれないか。
     - labels: formality informal
     - grammar: note Used when the speaker cannot recall or avoids naming the thing; read なに.
**Lexeme 2: 何**, INTJ
- said:  なに, accent 1 (ja-JP)
- spellings: 何 (ja-JP); なに (ja-JP)
  1. [B1] 驚いたり聞き返したりするときに発する言葉
     - example: なに、もう帰ってしまったのか。
     - labels: formality informal
  2. [B2] 大したことではないと相手の心配を打ち消す言葉
     - example: なに、これくらいの傷ならすぐ治るさ。
     - labels: formality informal

### 31. かかる

**Lexeme 1: かかる**, VERB
- grammar: conjugation godan; valency Intr; note Often written in kana. 係る is also read かかわる with another meaning.
- said:  かかる, accent 2 (ja-JP)
- spellings: かかる (ja-JP); 掛かる (ja-JP); 懸かる (ja-JP); 架かる (ja-JP); 罹る (ja-JP); 係る (ja-JP)
  1. [A2] 時間や費用などが必要となる
     - example: 駅から会社まで歩いて二十分かかる。
     - frames: 〜に（時間・お金）がかかる
  2. [B1] 錠や装置などが働く状態になる
     - example: 寒い朝はなかなかエンジンがかからない。
  3. [B1] 電話が通じたり、相手から来たりする
     - example: 夜遅くに友人から電話がかかってきた。
     - frames: 〜から電話がかかってくる
  4. [B1] 負担や力、迷惑などが他に加わる
     - example: 重い荷物を持つと腰に負担がかかる。
     - frames: 〜に〜がかかる
  5. [A2] 物が高い所からつるされて下がる
     - example: 壁に古い時計がかかっている。
     - frames: 〜に〜がかかっている
  6. [B2] 物事の結果がそれ次第で決まる
     - example: 合格できるかどうかは、これからの努力にかかっている。
     - frames: 〜にかかっている
  7. [B1] 病気にとりつかれて患う
     - example: 子どもの頃、重いはしかにかかったことがある。
     - frames: 〜にかかる
  8. [B1] 医者に体を診察してもらう
     - example: 熱が下がらないので、近くの医者にかかった。
     - collocations: 医者にかかる
  9. [B1] 水などの液体や粉が上から浴びせられる
     - example: 車が通って、服に泥水がかかった。
     - frames: 〜に〜がかかる
  10. [B2] 雲や霧などが一面をおおう
     - example: 朝から山の頂上に雲がかかっている。
  11. [B1] 橋などが川の両側に渡される
     - example: この川には古い石の橋がかかっている。
     - frames: 〜に橋がかかる
  12. [B2] 仕事などの物事に取り組み始める
     - example: 朝食をすませたら、すぐ仕事にかかろう。
     - frames: 〜にかかる
  13. [B2] わなや網などにとらえられる
     - example: 仕掛けた網に大きな魚がかかった。
     - frames: 〜にかかる
  14. [B2] 相手に向かって攻めていく
     - example: 何人でもいいから、まとめてかかってこい。
     - frames: 〜にかかっていく
     - collocations: かかってくる
  15. [B2] 今にもそうなりそうになる
     - example: 溺れかかった子どもを近くの人が助けた。
     - frames: 動詞連用形＋かかる
     - grammar: note Attached to the stem (連用形) of another verb.
  16. [C1] 言葉が意味の上で後の語に続いて関係する
     - example: この副詞は文末の動詞にかかっている。
     - frames: 〜にかかる
     - labels: domain grammar
- relations: confusable 掛ける (VERB): transitive partner: someone hangs or spends something (かける); it hangs or is needed (かかる)
**Lexeme 2: かかる**, ADJ
- grammar: position Attr; invariable True
- said:  かかる (ja-JP)
- spellings: かかる (ja-JP); 斯かる (ja-JP)
  1. [C1] 前に述べた内容を指して、このような
     - example: かかる事態を招いたことを深くおわびします。
     - frames: かかる〜
     - labels: formality formal, style literary

### 32. まま

**Lexeme 1: まま**, NOUN
- grammar: note A formal noun: always follows a modifier (a verb, この/その, or a noun with の). The kanji 儘 is rare today.
- said:  まま (ja-JP)
- spellings: まま (ja-JP); 儘 (ja-JP)
  1. [B1] 前と同じ状態が変わらずに続いていること
     - example: 窓を開けたまま寝てしまい、風邪をひいた。
     - frames: 〜たまま; 〜ないまま; 〜のまま
  2. [B2] 人の思いや成り行きに逆らわず従うこと
     - example: 足の向くままに、知らない町を歩いてみたい。
     - frames: 〜るままに; 〜のままに

### 33. はず

**Lexeme 1: はず**, NOUN
- grammar: note A formal noun: always follows a modifying clause, or a noun with の.
- said:  はず, accent 0 (ja-JP)
- spellings: はず (ja-JP); 筈 (ja-JP)
  1. [B1] 当然そうなると考えられる見込みや道理
     - example: 鍵はかばんに入れたはずなのに、見つからない。
     - frames: 〜はずだ; 〜はずがない; 〜はずはない
  2. [B1] 予定や約束でそうなることになっていること
     - example: 会議は十時から始まるはずだったが、一時間遅れた。
     - frames: 〜はずだった; 〜はずになっている

### 34. 悪い

**Lexeme 1: 悪い**, ADJ
- grammar: conjugation i
- said:  わるい, accent 2 (ja-JP)
- spellings: 悪い (ja-JP); わるい (ja-JP)
  1. [A1] 質や状態が劣っていて好ましくない
     - example: 今日は天気が悪いので、洗濯物は部屋に干そう。
     - frames: 〜が悪い
     - opposites: 良い
  2. [A2] 道徳や決まりに反していて正しくない
     - example: 人の物を黙って使うのは悪いことだ。
     - frames: 〜のは悪い
     - opposites: 良い
  3. [A2] 体や心の調子が普通でなく、すぐれない
     - example: 朝から気分が悪くて、何も食べられない。
     - collocations: 気分が悪い; 具合が悪い
  4. [B1] 健康や物事に害を与える性質がある
     - example: 毎晩の夜更かしは体に悪いよ。
     - frames: 〜に悪い
  5. [B1] 相手に迷惑をかけて申し訳ない
     - example: 悪いけど、ちょっと荷物を持ってくれる？
     - frames: 〜に悪い
     - collocations: 悪いけど
     - labels: formality informal

### 35. なら

**Lexeme 1: なら**, PART
- grammar: note Historically the conditional form of the copula だ; follows nouns and plain forms of verbs and adjectives.
- said:  なら (ja-JP)
  1. [A2] 仮の事柄や相手の言ったことを受けて、条件として示すのに使う
     - example: 駅まで行くなら、この手紙も出してきてくれる？
     - frames: 〜なら; 〜のなら
  2. [B1] ある物事を話題として取り上げ、それについて述べるのに使う
     - example: 料理なら、母より父のほうがずっと上手だ。
     - frames: 名詞＋なら
- relations: derived_from だ (AUX)
**Lexeme 2: なら**, CONJ
- said:  なら (ja-JP)
  1. [B1] 相手の話を受けて、そういうことであればと次を続けるのに使う
     - example: 忙しいの？なら、明日もう一度来るよ。
     - labels: formality informal
     - grammar: note Stands at the start of a sentence.

### 36. 言葉

**Lexeme 1: 言葉**, NOUN
- said:  ことば, accent 3 (ja-JP)
- spellings: 言葉 (ja-JP); ことば (ja-JP); コトバ (ja-JP)
  1. [A2] 人が話したり書いたりして表した内容
     - example: 先生の温かい言葉に、ずいぶん励まされました。
     - frames: 〜の言葉; 〜に言葉をかける
  2. [A1] 考えや気持ちを伝えるために人が使う音声や文字のしくみ
     - example: 外国の言葉を覚えるのは思ったより楽しい。
  3. [A2] 意味をもつ一つ一つの語
     - example: わからない言葉は辞書で調べるようにしている。

### 37. 漫画

**Lexeme 1: 漫画**, NOUN
- grammar: classifier 冊; note A book is counted with 冊; a single work also with 作 or 本, and panels with コマ.
- said:  まんが, accent 0 (ja-JP)
- spellings: 漫画 (ja-JP); まんが (ja-JP); マンガ (ja-JP)
  1. [A1] 絵とせりふを組み合わせ、こまを続けて話を描いたもの
     - example: 電車の中で漫画を読んでいる人が多い。
  2. [B2] 人や世の中の出来事をおもしろく誇張して描いた絵
     - example: 新聞に政治家を皮肉った漫画が載っていた。

### 38. ない

**Lexeme 1: ない**, ADJ
- grammar: conjugation i; note Usually written in kana. The polite equivalent is ありません.
- said:  ない, accent 1 (ja-JP)
- spellings: ない (ja-JP); 無い (ja-JP)
  1. [A1] 物や事がそこに存在せず、欠けている
     - example: 冷蔵庫の中に牛乳がもうない。
     - frames: 〜がない; 〜に〜がない
     - opposites: ある
  2. [A1] 必要なものを持たず、欠いている
     - example: 今週は忙しくて、本を読む時間がない。
     - frames: 〜がない; 〜には〜がない
     - opposites: ある
  3. [A1] 形容詞や「だ」の連用形について、打ち消しを表す
     - example: この部屋は思ったほど広くない。
     - frames: 〜くない; 〜ではない; 〜じゃない
     - grammar: note Used after the く form of i-adjectives and after では or じゃ.
**Lexeme 2: ない**, AUX
- grammar: conjugation i; note Follows the negative stem of a verb (食べない, 書かない, しない, 来ない); ある takes the adjective ない instead.
- said:  ない (ja-JP)
- forms: なくて [VerbForm=Conv] なくて (links clauses, often giving a reason); ないで [VerbForm=Conv] ないで ('without doing'; also in requests (〜ないでください))
  1. [A1] 動詞などの未然形について、打ち消しを表す
     - example: 弟は野菜をほとんど食べない。
     - frames: 〜ない; 〜ないでください
  2. [A2] 上がり調子の疑問の形で、誘いや勧めを表す
     - example: 今度の休みに、一緒に映画を見に行かない？
     - frames: 〜ない？; 〜ないか
     - labels: formality informal

### 39. 普通

**Lexeme 1: 普通**, ADJ
- grammar: conjugation na
- said:  ふつう, accent 0 (ja-JP)
- spellings: 普通 (ja-JP); ふつう (ja-JP)
  1. [A2] ほかと変わったところがなく、よくある
     - example: 私はごく普通の家庭で育ちました。
     - frames: 普通の〜; 〜は普通だ
     - opposites: 特別
  2. [B2] 特別ではないが、十分に満足できる
     - example: このパン、安いのに普通においしいね。
     - frames: 普通に〜
     - labels: formality informal
     - grammar: note Used in the に form before a verb or adjective; common in casual speech of younger people.
**Lexeme 2: 普通**, ADV
- said:  ふつう, accent 0 (ja-JP)
- spellings: 普通 (ja-JP); ふつう (ja-JP)
  1. [A2] 特別な事情がない限り、たいていの場合
     - example: 普通、この店は朝十時に開きます。
     - frames: 普通は〜
**Lexeme 3: 普通**, NOUN
- said:  ふつう, accent 0 (ja-JP)
- spellings: 普通 (ja-JP); ふつう (ja-JP)
  1. [B1] ほかと変わらない、ありふれた様子
     - example: 派手な暮らしより、普通が一番だと思う。
     - collocations: 普通と違う; 普通が一番
  2. [B1] 駅ごとにすべて止まる列車
     - example: 急行は止まらないので、普通に乗り換えた。
     - labels: domain transport
     - grammar: note Short for 普通列車; counted with 本.
     - opposites: 急行

### 40. よう

**Lexeme 1: よう**, NOUN
- grammar: note Formal noun; never stands alone. With だ it inflects like a na-adjective: ような, ように.
- said:  よう (ja-JP)
- spellings: よう (ja-JP); 様 (ja-JP)
  1. [A2] ほかのものに似ていること、またはたとえを表す
     - example: 彼女の笑顔はまるで花のようだ。
     - frames: 〜のようだ; まるで〜ようだ; 〜のように
  2. [A2] はっきりしないが、そう判断されることを表す
     - example: 外は雨が降っているようですね。
     - frames: 〜ようだ
  3. [A2] ある状態に変わることや、変えることを表す
     - example: 毎日練習して、泳げるようになった。
     - frames: 〜ようになる; 〜ようにする
  4. [B1] ある目的に向けて何かをすることを表す
     - example: 忘れないように、手帳に書いておこう。
     - frames: 〜ように; 〜ないように
  5. [A2] 具体的な例を挙げて示すことを表す
     - example: りんごのような甘い果物が好きです。
     - frames: 〜のような〜; 〜のように
  6. [B1] 願いや、人への指示・注意の内容を表す
     - example: 明日は遅れないようにと先生に言われた。
     - frames: 〜ように言う; 〜ますように
  7. [B2] 物事をするやり方や方法
     - example: あまりに美しくて、言いようがない景色だった。
     - frames: 〜ようがない; 〜ようによっては
     - grammar: note Follows the masu stem of a verb.
**Lexeme 2: よう**, INTJ
- said:  よう (ja-JP)
  1. [B1] 親しい相手に気軽に呼びかけるときに使う言葉
     - example: よう、久しぶりだな。元気だったか。
     - labels: formality informal, politeness familiar
     - grammar: note Used mostly by men.

### 41. まあ

**Lexeme 1: まあ**, ADV
- said:  まあ, accent 1 (ja-JP)
- spellings: まあ (ja-JP); まぁ (ja-JP)
  1. [B1] 十分ではないが、一応よいと認める気持ちを表す
     - example: この点数なら、まあ合格と言えるだろう。
     - frames: まあまあ〜
     - collocations: まあいい
  2. [B1] 相手をなだめたり、軽く何かをすすめたりするときに使う
     - example: まあ、そう怒らずに話を聞いてください。
     - grammar: note Usually at the start of what is said.
  3. [B1] はっきり言うのをためらう気持ちを表す
     - example: まあ、いろいろ事情があったんですよ。
     - labels: formality informal
**Lexeme 2: まあ**, INTJ
- said:  まあ, accent 1 (ja-JP)
- spellings: まあ (ja-JP); まぁ (ja-JP)
  1. [A2] 驚いたり感心したりしたときに思わず出す声
     - example: まあ、なんてきれいな花でしょう。
     - grammar: note Used mostly by women.

### 42. 長い

**Lexeme 1: 長い**, ADJ
- grammar: conjugation i; note 永い is used for long, lasting spans of time, as in 永い眠り.
- said:  ながい, accent 2 (ja-JP)
- spellings: 長い (ja-JP); ながい (ja-JP); 永い (ja-JP)
  1. [A1] 物の端から端までの距離が大きい
     - example: 彼女は長い髪を後ろで一つに結んでいる。
     - opposites: 短い
  2. [A1] 始まりから終わりまでの時間が多くかかる
     - example: 長い冬が終わって、ようやく春が来た。
     - frames: 〜が長い
     - collocations: 長い間
     - opposites: 短い

### 43. よく

**Lexeme 1: よく**, ADV
- grammar: note Usually written in kana. 良く is also common; 善く mostly in よくも.
- said:  よく, accent 1 (ja-JP)
- spellings: よく (ja-JP); 良く (ja-JP); 善く (ja-JP)
  1. [A1] 細かいところまで注意して、十分に
     - example: 説明書をよく読んでから使ってください。
  2. [A2] 同じことが何度も繰り返されるさま
     - example: 子どもの頃は、この公園でよく遊んだ。
  3. [B1] やり方がすぐれていて、上手に
     - example: この絵は細かいところまでよく描けている。
  4. [B1] 難しいことをしたのに感心する気持ちを表す
     - example: こんな雨の中、よく来てくれたね。
     - frames: よく〜ね; よく〜たものだ
  5. [B2] あきれて相手を責める気持ちを表す
     - example: よくもそんなひどいことが言えるね。
     - frames: よくも〜
     - grammar: note Usually in the form よくも.
- relations: adverb_of 良い (ADJ)

### 44. なる

**Lexeme 1: なる**, VERB
- grammar: conjugation godan; valency Intr; note Usually written in kana; 成る is common in the sense 'consist of'.
- said:  なる, accent 1 (ja-JP)
- spellings: なる (ja-JP); 成る (ja-JP)
  1. [A1] 前とは違う状態や性質、立場に変わる
     - example: 大きくなったら、医者になりたいです。
     - frames: 〜になる; 〜くなる; 〜ようになる
  2. [A1] ある時期や時刻、数量に達する
     - example: 四月になると、桜が一斉に咲き始める。
     - frames: 〜になる; 合計で〜になる
  3. [B2] いくつかの部分が集まって全体ができている
     - example: この本は全部で五つの章から成る。
     - frames: 〜から成る; 〜より成る
     - labels: formality formal
  4. [A2] 動詞と組み、相手の行為を敬う言い方を作る
     - example: 先生はもうお帰りになりました。
     - frames: お〜になる; ご〜になる
     - labels: politeness honorific
     - grammar: note Used with the stem of a verb: お読みになる, ご覧になる.
**Lexeme 2: なる**, VERB
- grammar: conjugation godan; valency Intr
- said:  なる (ja-JP)
- spellings: なる (ja-JP); 生る (ja-JP)
  1. [B1] 木や草に果実が実ってつく
     - example: 庭の柿の木に今年もたくさん実がなった。
     - frames: 〜に実がなる

### 45. 無理

**Lexeme 1: 無理**, ADJ
- grammar: conjugation na
- said:  むり, accent 1 (ja-JP)
- spellings: 無理 (ja-JP); むり (ja-JP); ムリ (ja-JP)
  1. [A2] 行うことや実現することができない
     - example: この量の仕事を一日で終えるのは無理だ。
     - frames: 〜のは無理だ; 〜には無理だ
     - opposites: 可能
  2. [B1] 筋道や道理に合わない
     - example: そんな無理な要求には応じられません。
     - frames: 無理な〜
  3. [B1] 相手の気持ちや事情を押し切って強引だ
     - example: 嫌がる子どもに無理に食べさせてはいけない。
     - frames: 無理に〜する
**Lexeme 2: 無理**, NOUN
- said:  むり, accent 1 (ja-JP)
- spellings: 無理 (ja-JP); むり (ja-JP); ムリ (ja-JP)
  1. [A2] 体や力の限度を超えて物事をすること
     - example: 熱があるなら、無理をしないで休んでください。
     - frames: 無理する
     - collocations: 無理をする
     - grammar: suru True
  2. [B2] 筋道が通らず、うまくいかない点
     - example: その説明には少し無理があると思う。
     - frames: 〜に無理がある

### 46. 子供

**Lexeme 1: 子供**, NOUN
- grammar: classifier 人; note 子ども is also common, especially in education and the press.
- said:  こども, accent 0 (ja-JP)
- spellings: 子供 (ja-JP); 子ども (ja-JP); こども (ja-JP)
  1. [A1] 年が若く、まだ大人になっていない人
     - example: 公園では子供たちが元気に遊んでいる。
     - opposites: 大人
  2. [A1] ある親から生まれた人
     - example: うちの子供は来年から小学校に通います。
     - opposites: 親
  3. [B1] 考え方やふるまいが幼く、未熟な人
     - example: そんなことで怒るなんて、彼もまだ子供だね。
     - labels: figurative True
     - opposites: 大人

### 47. かなり

**Lexeme 1: かなり**, ADV
- grammar: note Normally written in kana; 可成り is an uncommon ateji spelling.
- said:  かなり, accent 1 (ja-JP)
- spellings: かなり (ja-JP); 可成り (ja-JP)
  1. [A2] 程度が普通よりも相当に上回っているさま
     - example: 駅から家まではかなり遠いので、バスを使っています。
**Lexeme 2: かなり**, ADJ
- grammar: conjugation na; position Attr, mainly; note Before a noun usually かなりの; かなりな also occurs.
- said:  かなり, accent 1 (ja-JP)
- spellings: かなり (ja-JP); 可成り (ja-JP)
  1. [B1] 数や量、程度が普通よりも相当に大きい
     - example: この仕事を終えるには、かなりの時間がかかりそうだ。
     - frames: かなりの〜

### 48. 呼ぶ

**Lexeme 1: 呼ぶ**, VERB
- grammar: conjugation godan; valency Tr
- said:  よぶ, accent 0 (ja-JP)
- spellings: 呼ぶ (ja-JP); よぶ (ja-JP)
  1. [A1] 声をかけて相手の注意をこちらに向けさせる
     - example: 後ろから名前を呼ばれて、思わず振り返った。
     - frames: 〜を呼ぶ
     - collocations: 名前を呼ぶ
  2. [A2] 来るように言って人や車などを来させる
     - example: 具合が悪そうなので、すぐに救急車を呼んだ。
     - frames: 〜を呼ぶ
  3. [A2] 客として家や集まりに来てもらう
     - example: 週末に友達を家に呼んで、一緒に料理を作ります。
     - frames: 〜を〜に呼ぶ
  4. [B1] ある名前をつけて言い表す
     - example: この町の人たちは、あの山を「白い山」と呼んでいる。
     - frames: 〜を〜と呼ぶ
  5. [B2] 人の関心や物事を引き起こしたり集めたりする
     - example: 新しく出た小説が大きな話題を呼んでいる。
     - frames: 〜を呼ぶ
     - labels: figurative True

### 49. すぐ

**Lexeme 1: すぐ**, ADV
- said:  すぐ, accent 1 (ja-JP)
- spellings: すぐ (ja-JP); 直ぐ (ja-JP)
  1. [A1] 時間をおかないで、その時にただちに
     - example: 家に着いたら、すぐ電話してください。
     - frames: すぐ〜する; 〜とすぐ
  2. [A2] 距離がほとんど離れていないようす
     - example: 駅はこの道をまっすぐ行けばすぐそこです。
  3. [B1] わずかなことで簡単にそうなるようす
     - example: うちの弟は、ちょっとしたことですぐ泣く。

### 50. 書く

**Lexeme 1: 書く**, VERB
- grammar: conjugation godan; valency Tr
- said:  かく, accent 1 (ja-JP)
- spellings: 書く (ja-JP); かく (ja-JP)
  1. [A1] 文字や記号をペンなどで紙などにしるす
     - example: ここに名前と住所を書いてください。
     - frames: 〜を書く; 〜に〜を書く
  2. [A2] 文章を作って、考えや出来事を表す
     - example: 週末に祖母へ長い手紙を書くつもりだ。
     - frames: 〜を書く; 〜について書く; 〜に書いてある
- relations: confusable 描く (VERB): Also read かく; 描く is used for drawing pictures, 書く for writing letters and texts.
