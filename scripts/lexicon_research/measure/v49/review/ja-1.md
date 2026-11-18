# Japanese: entries to check (1 of 1)

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
- **collocation**: a collocation that is not said that way in Japanese; or one that does not belong
  among the ten a learner is shown for the sense, because anyone would build it from the meanings
  or because it is rare; or a list that lacks the commonest partner of the sense while it has
  rarer ones. Quote the collocation in "what". A frame with the wrong preposition or case is a
  fact, not this.

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
  (heavy rain, in the lead). A sense holds at most five frames and ten collocations, so a missing one
  is not a fault.
- a word is said as a reading in hiragana, not in IPA. "accent" after it is the pitch accent of Tokyo as
  dictionaries number it: the mora after which the pitch drops, 0 when it does not drop. A reading without
  one is not a fault: the writer gives it only when sure. A wrong one is a fact.
- a frame is a pattern with 〜 for an open place and its particle written (〜に行く). A use after the て form
  with a meaning of its own (〜ている, 〜てくる) is numbered as a sense on purpose, and so is a particle's or
  a formal noun's use in grammar (〜ことがある): do not report these as split.
- suru true means the noun makes a verb with する. A noun has no countability. A definition is one
  predicate or one noun phrase of 10 to 35 characters.
- where the headword is a word, its use as a suffix, a prefix or an auxiliary (目 in 二回目, そう in
  降りそう) is a sense or a lexeme of it on purpose; a prefix or suffix that is no word (〜的) has no entry.

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

### 1. 生える

**Lexeme 1: 生える**, VERB
- grammar: conjugation ichidan; valency Intr
- said:  はえる, accent 2 (ja-JP)
- spellings: 生える (ja-JP); はえる (ja-JP)
  1. [B1] 植物や毛、歯などが表に出て伸びてくる
     - example: しばらく留守にしていたら、庭に雑草がたくさん生えてきた。
     - frames: 〜が生える; 〜に〜が生える
     - collocations: カビが生える; ひげが生える; 歯が生える; 毛が生える; 草が生える; 雑草が生える; 根が生える
- relations: confusable 生やす (VERB): transitive partner: 生やす takes を (ひげを生やす), 生える takes が (ひげが生える)

### 2. エントリー

**Lexeme 1: エントリー**, NOUN
- said:  えんとりー (ja-JP)
- spellings: エントリー (ja-JP); エントリ (ja-JP)
  1. [B1] 競技や催しなどへの参加を申し込むこと
     - example: 来月のマラソン大会にエントリーしました。
     - frames: 〜にエントリーする
     - collocations: エントリーを受け付ける; エントリーを締め切る; 大会にエントリーする; エントリー料
     - grammar: suru True
  2. [B2] 就職活動で、企業に応募の意思を伝えること
     - example: 興味のある会社には早めにエントリーしておこう。
     - frames: 〜にエントリーする
     - collocations: 企業にエントリーする
     - grammar: suru True; note Used in job hunting; often the first step before applying formally.
  3. [B2] 一覧や記録に登録された一つ一つの項目
     - example: このブログには古いエントリーがたくさん残っている。
     - collocations: ブログのエントリー
     - grammar: note エントリ is common in computing.

### 3. 課税

**Lexeme 1: 課税**, NOUN
- grammar: suru True
- said:  かぜい, accent 0 (ja-JP)
- parts: 課 (課) + 税 (税)
  1. [B2] 国や自治体が人や物に税金を割り当てること
     - example: 政府は来年から甘い飲み物に課税する方針だ。
     - frames: 〜に課税する; 〜を課税する
     - collocations: 課税対象; 課税所得; 課税を強化する; 課税を逃れる; 二重課税; 重い課税

### 4. よろしい

**Lexeme 1: よろしい**, ADJ
- grammar: conjugation i; note A politer, more formal equivalent of いい／よい. Usually written in kana.
- said:  よろしい (ja-JP)
- spellings: よろしい (ja-JP); 宜しい (ja-JP)
  1. [A2] 好ましく、差し支えがない
     - example: この書類はこちらに置いてもよろしいですか。
     - frames: 〜てもよろしい; 〜でよろしい; 〜ばよろしい
     - collocations: よろしいでしょうか; どちらでもよろしい; 都合がよろしい
     - labels: formality formal, politeness polite
  2. [B1] 相手の申し出や言い分を認め、許すときに使う
     - example: よろしい、今回だけは特別に許可しよう。
     - grammar: note Used on its own as a reply, usually by someone in authority.

### 5. リボン

**Lexeme 1: リボン**, NOUN
- grammar: classifier 本
- said:  りぼん, accent 1 (ja-JP)
  1. [A2] 飾りに使う、布などでできた細長い帯
     - example: 誕生日のプレゼントに赤いリボンをかけてもらった。
     - collocations: リボンを結ぶ; リボンをかける; リボンをほどく; リボンをつける; 髪にリボンを結ぶ
  2. [C1] 印字に使う、インクをしみこませた細長い帯
     - example: 文字が薄くなってきたので、プリンターのリボンを交換した。
     - collocations: リボンを交換する

### 6. 土下座

**Lexeme 1: 土下座**, NOUN
- grammar: suru True
- said:  どげざ (ja-JP)
- parts: 土 (土) + 下座 (下座)
  1. [B2] 床や地面にひざと手をつき、深く頭を下げてわびたり頼んだりすること
     - example: 彼は床に手をついて土下座し、何度も謝った。
     - frames: 〜に土下座する; 〜の前で土下座する
     - collocations: 土下座して謝る; 土下座して頼む

### 7. 家族

**Lexeme 1: 家族**, NOUN
- grammar: note Members are counted with 人; whole households with 世帯.
- said:  かぞく, accent 1 (ja-JP)
  1. [A1] 結婚や血のつながりによって結ばれた人々の集まり
     - example: 週末はいつも家族と一緒に夕食を食べます。
     - frames: 〜の家族; 〜と家族になる
     - collocations: 家族を養う; 家族を持つ; 家族と過ごす; 家族旅行; 家族連れ; 家族ぐるみの付き合い; 家族思い

### 8. 守る

**Lexeme 1: 守る**, VERB
- grammar: conjugation godan; valency Tr
- said:  まもる, accent 2 (ja-JP)
- spellings: 守る (ja-JP); まもる (ja-JP); 護る (ja-JP)
  1. [A2] 害や危険が及ばないように防ぐ
     - example: 親には子どもを危険から守る責任がある。
     - frames: 〜を守る; 〜を〜から守る
     - collocations: 身を守る; 命を守る; 自然を守る; 国を守る; 家族を守る; 環境を守る; ゴールを守る
  2. [A2] 決まりや約束にそむかないようにする
     - example: 約束の時間はきちんと守ってください。
     - frames: 〜を守る
     - collocations: 約束を守る; ルールを守る; 時間を守る; 法律を守る; 秘密を守る; 規則を守る; 言いつけを守る
     - opposites: 破る
  3. [B2] ある状態が変わらないように保ち続ける
     - example: 彼は会議の間ずっと沈黙を守っていた。
     - frames: 〜を守る
     - collocations: 沈黙を守る; 伝統を守る; 中立を守る; 首位を守る; 味を守る

### 9. 授業

**Lexeme 1: 授業**, NOUN
- grammar: suru True; note Class periods are usually counted with 時間 or 時限, and with コマ at university.
- said:  じゅぎょう, accent 1 (ja-JP)
- parts: 授 (授) + 業 (業)
  1. [A1] 学校などで、先生が知識や技術を教えること
     - example: 明日の授業では、漢字の書き方を練習します。
     - frames: 〜の授業; 〜に授業する
     - collocations: 授業を受ける; 授業に出る; 授業をサボる; 授業についていく; 授業を休む; 授業が始まる; 授業が終わる; 授業を行う

### 10. 忠告

**Lexeme 1: 忠告**, NOUN
- grammar: suru True
- said:  ちゅうこく, accent 0 (ja-JP)
  1. [B1] 相手のためを思って、よくない点を改めるよう言うこと
     - example: 医者の忠告を聞いて、たばこをやめることにした。
     - frames: 〜に忠告する; 〜ように忠告する; 〜の忠告
     - collocations: 忠告に従う; 忠告を聞く; 忠告を無視する; 忠告を受ける; 忠告を守る; 忠告を聞き入れる; 友人の忠告

### 11. 触る

**Lexeme 1: 触る**, VERB
- grammar: conjugation godan; valency Both
- said:  さわる, accent 0 (ja-JP)
- spellings: 触る (ja-JP); さわる (ja-JP)
  1. [A2] 手などを物や人に軽くつける
     - example: 熱いから、鍋には触らないでね。
     - frames: 〜に触る; 〜を触る
     - collocations: 手で触る; 髪を触る; そっと触る; 指で触る
     - grammar: valency Both; note Both に and を are common; を stresses handling the thing.
  2. [B2] 面倒な物事にかかわりを持つ
     - example: その話題にはあまり触らないほうがいい。
     - frames: 〜に触る
     - collocations: 触らぬ神に祟りなし
     - labels: figurative True
     - grammar: valency Intr; note Mostly in the negative.
- relations: confusable 触れる (VERB): similar meaning; 触れる is lighter or more formal and also means to mention; confusable 障る (VERB): same reading さわる; 障る means to harm or offend (気に障る)

### 12. 寝る

**Lexeme 1: 寝る**, VERB
- grammar: conjugation ichidan; valency Intr
- said:  ねる, accent 0 (ja-JP)
- spellings: 寝る (ja-JP); ねる (ja-JP); 寢る [older books only]
  1. [A1] 体を休めるために床に入り、眠る
     - example: 明日は早いので、今夜は十時に寝ます。
     - collocations: 早く寝る; 遅く寝る; ぐっすり寝る; 寝る前に; 寝る時間; 昼まで寝る
     - opposites: 起きる
  2. [A2] 体を横にした姿勢になる
     - example: ソファに寝たままテレビを見ている。
     - frames: 〜に寝る
     - collocations: 横になって寝る; 仰向けに寝る; うつぶせに寝る; 横向きに寝る; 寝転がる
  3. [B1] 病気で床について休んでいる
     - example: 風邪をひいて、三日間ずっと寝ていた。
     - frames: 〜で寝ている
     - collocations: 風邪で寝る; 熱で寝る
     - grammar: note Usually in the form 寝ている.
  4. [B2] 相手と性的な関係を持つ
     - example: 彼は彼女と寝たことを友人に隠していた。
     - frames: 〜と寝る
     - labels: attitude euphemistic, formality informal
  5. [C1] 品物やお金が使われずにそのままになっている
     - example: 倉庫に売れない商品がたくさん寝ている。
     - collocations: 金が寝ている
     - labels: figurative True
     - grammar: note Usually in the form 寝ている.
- relations: confusable 眠る (VERB): 寝る can mean going to bed or lying down without being asleep; 眠る means only being asleep

### 13. 裁判

**Lexeme 1: 裁判**, NOUN
- grammar: suru True; note The noun is far more common than 裁判する.
- said:  さいばん, accent 1 (ja-JP)
  1. [B1] 法律に従って争いごとや罪の有無を判断する手続き
     - example: 事故の責任をめぐって、会社を相手に裁判を起こした。
     - frames: 〜を裁判にかける; 〜を裁判で争う; 〜を相手に裁判を起こす
     - collocations: 裁判を起こす; 裁判にかける; 裁判で争う; 裁判に勝つ; 裁判に負ける; 裁判を受ける; 裁判が始まる; 公正な裁判

### 14. 天然

**Lexeme 1: 天然**, NOUN
- said:  てんねん, accent 0 (ja-JP)
  1. [B1] 人の手が加わっていない、自然のままのもの
     - example: この店では天然の鮎を炭火で焼いて出している。
     - frames: 天然の〜
     - collocations: 天然資源; 天然ガス; 天然素材; 天然の魚; 天然記念物
     - grammar: note Often used before another noun with の or as the first part of a compound.
     - opposites: 人工
  2. [B2] 本人は気づかずに、どこかずれた言動をする人
     - example: 彼女はちょっと天然なところがあって、みんなに好かれている。
     - frames: 天然な〜
     - collocations: 天然なところがある
     - labels: formality slang
     - grammar: note Short for 天然ボケ. Often used like a na-adjective; usually affectionate.

### 15. ぜひ

**Lexeme 1: ぜひ**, ADV
- said:  ぜひ, accent 1 (ja-JP)
- spellings: ぜひ (ja-JP); 是非 (ja-JP)
  1. [A2] 強い願いや心からの勧めを表すのに使う
     - example: 近くに来たら、ぜひうちにも寄ってください。
     - frames: ぜひ〜てください; ぜひ〜たい
     - collocations: ぜひお願いします; ぜひ一度; ぜひお越しください; ぜひ参加したい
     - grammar: note Usually with a request, a wish (〜たい) or a suggestion; not with a negative command.
- relations: derived_from 是非 (NOUN)

### 16. 主義

**Lexeme 1: 主義**, NOUN
- said:  しゅぎ, accent 1 (ja-JP)
  1. [B1] 社会や政治などについてのまとまった考え方や立場
     - example: この国では戦後、民主主義が少しずつ根づいていった。
     - frames: 名詞＋主義
     - collocations: 主義主張; 主義を掲げる; 資本主義; 民主主義; 社会主義
     - grammar: note Very often used as a suffix after a noun (資本主義, 個人主義).
  2. [B2] 自分がいつも守っている行動や考え方の決まり
     - example: 私は人からお金を借りない主義です。
     - frames: 〜主義だ; 〜という主義
     - collocations: 主義を貫く; 主義を曲げる; 主義に反する; 主義を守る

### 17. 灰色

**Lexeme 1: 灰色**, NOUN
- said:  はいいろ, accent 0 (ja-JP)
- spellings: 灰色 (ja-JP); はいいろ (ja-JP)
- parts: 灰 (灰) + 色 (色)
  1. [A2] 白と黒をまぜたような、くすんだ色
     - example: 朝から灰色の雲が空一面に広がっている。
     - frames: 灰色の〜
     - collocations: 灰色の空; 灰色の雲; 灰色の髪; 灰色のスーツ
  2. [B2] 楽しみや希望がなく、暗く沈んだ状態
     - example: 友達もいなくて、灰色の毎日を送っていた。
     - frames: 灰色の〜
     - collocations: 灰色の人生; 灰色の青春; 灰色の日々
     - labels: figurative True
  3. [C1] 疑わしいが、罪があるとははっきりしない状態
     - example: 調査は終わったが、彼の立場は灰色のままだ。
     - collocations: 灰色の決着
     - labels: figurative True

### 18. 責任

**Lexeme 1: 責任**, NOUN
- said:  せきにん, accent 0 (ja-JP)
  1. [B1] 立場の上で当然しなければならない務め
     - example: 子どもを守るのは私たち大人の責任です。
     - frames: 〜の責任; 〜に対する責任
     - collocations: 責任を果たす; 責任を持つ; 責任が重い; 責任ある立場; 重い責任
  2. [B1] 自分のしたことの悪い結果を引き受ける義務
     - example: 事故の責任を取って、社長が辞任した。
     - frames: 〜の責任; 〜に責任がある
     - collocations: 責任を取る; 責任を負う; 責任を問う; 責任を感じる; 責任を押し付ける; 責任を逃れる

### 19. 感染

**Lexeme 1: 感染**, NOUN
- grammar: suru True; valency Intr
- said:  かんせん, accent 0 (ja-JP)
  1. [B1] 病原体が体の中に入り込んで増えること
     - example: 手洗いをすれば、風邪の感染をある程度防げる。
     - frames: 〜に感染する; 〜が感染する; 〜から感染する
     - collocations: 感染を防ぐ; 感染が広がる; 感染経路; 感染拡大; 院内感染; 二次感染; 感染者; 感染症
  2. [B2] コンピューターに害のあるプログラムが入り込むこと
     - example: パソコンがウイルスに感染して、データが消えた。
     - frames: 〜に感染する
     - collocations: ウイルスに感染する; 感染を防ぐ
     - labels: domain computing
  3. [C1] 周りの影響を受けて、考えや行動が似てくること
     - example: 悪い仲間の考え方に感染しないよう気をつけなさい。
     - frames: 〜に感染する
     - labels: figurative True
- relations: confusable 伝染 (NOUN): 伝染 is a disease or mood passing from one to another; 感染 is the catching of it by the one infected.

### 20. 作り出す

**Lexeme 1: 作り出す**, VERB
- grammar: conjugation godan; valency Tr
- said:  つくりだす, accent 4 (ja-JP)
- spellings: 作り出す (ja-JP); つくりだす (ja-JP); 作りだす (ja-JP); 創り出す (ja-JP); 造り出す (ja-JP)
- parts: 作り (作る) + 出す (出す)
  1. [B1] 今までになかった物事や状態を新たに生じさせる
     - example: この町に新しい仕事を作り出すことが課題だ。
     - frames: 〜を作り出す
     - collocations: 価値を作り出す; 雰囲気を作り出す; 雇用を作り出す; 流行を作り出す; 状況を作り出す
  2. [B2] 原料などに手を加えて製品をこしらえる
     - example: この工場では、一日に千台もの車を作り出している。
     - frames: 〜を作り出す; 〜から〜を作り出す
     - collocations: 製品を作り出す

### 21. やら

**Lexeme 1: やら**, PART
- said:  やら (ja-JP)
  1. [B1] 似た物事をいくつか例として並べて挙げるのに使う
     - example: 引っ越しの前は、掃除やら荷造りやらで忙しい。
     - frames: 〜やら〜やら
     - collocations: 泣くやら笑うやら
     - grammar: note Usually repeated after each item. Often suggests a busy or confused mix of things.
  2. [B2] はっきりしない、わからないという気持ちを表すのに使う
     - example: 鍵をどこに置いたのやら、まったく思い出せない。
     - frames: 疑問詞＋やら; 〜のやら
     - collocations: いつの間にやら; どこへやら; 誰やら

### 22. 登場

**Lexeme 1: 登場**, NOUN
- grammar: suru True
- said:  とうじょう, accent 0 (ja-JP)
  1. [B1] 舞台や物語、場面などに人物が出てくること
     - example: 主人公の父親は物語の最後にもう一度登場する。
     - frames: 〜が登場する; 〜に登場する
     - collocations: 舞台に登場する; 登場人物; 颯爽と登場する; 再び登場する
     - opposites: 退場
  2. [B1] 新しい人や物が世の中に現れること
     - example: 駅前に新しいタイプのコンビニが登場した。
     - frames: 〜が登場する; 〜に登場する
     - collocations: 新製品が登場する; 市場に登場する; 新たに登場する; 華々しく登場する

### 23. 交流

**Lexeme 1: 交流**, NOUN
- said:  こうりゅう, accent 0 (ja-JP)
  1. [B1] 異なる人や地域の間で行き来し、互いに触れ合うこと
     - example: 留学生との交流を通じて、多くのことを学んだ。
     - frames: 〜と交流する; 〜との交流; 〜の間の交流
     - collocations: 交流を深める; 交流を図る; 交流が盛んだ; 国際交流; 文化交流; 交流会
     - grammar: suru True
  2. [B2] 一定の時間ごとに流れる向きが変わる電流
     - example: 日本の家庭には交流の電気が送られている。
     - labels: domain electricity
     - opposites: 直流

### 24. すね

**Lexeme 1: すね**, NOUN
- said:  すね (ja-JP)
- spellings: すね (ja-JP); 脛 (ja-JP); 臑 (ja-JP); スネ (ja-JP)
  1. [B1] 足の、ひざから足首までの部分
     - example: 机の脚にすねをぶつけて、思わず声が出た。
     - collocations: 親のすねをかじる; すねをかじる; すねに傷を持つ; すねをぶつける; すねを蹴る; 向こうずね

### 25. 盛り上がり

**Lexeme 1: 盛り上がり**, NOUN
- said:  もりあがり, accent 0 (ja-JP)
  1. [B1] 人々の気持ちや場の雰囲気、物事の勢いの高まり
     - example: 決勝戦が近づくにつれて、町の盛り上がりも増してきた。
     - collocations: 盛り上がりに欠ける; 盛り上がりを見せる; 盛り上がりを欠く; 大きな盛り上がり; 最高の盛り上がり; 一番の盛り上がり
  2. [B2] 物の表面が周りより高くふくらんだ部分
     - example: 木の根が伸びて、地面に盛り上がりができている。
- relations: verbal_noun_of 盛り上がる (VERB)

### 26. 事態

**Lexeme 1: 事態**, NOUN
- said:  じたい, accent 1 (ja-JP)
  1. [B2] 物事のなりゆきと、そのときの状態
     - example: 対応が遅れたため、事態はさらに悪化した。
     - collocations: 事態を収拾する; 事態を招く; 事態を重く見る; 事態が悪化する; 最悪の事態; 不測の事態; 緊急事態; 非常事態
     - grammar: note Usually said of an unfavourable or serious situation.

### 27. 貨幣

**Lexeme 1: 貨幣**, NOUN
- said:  かへい, accent 1 (ja-JP)
  1. [B2] 物の売り買いの仲立ちとなり、価値をはかるもの
     - example: 貨幣が生まれる前、人々は物と物を直接交換していた。
     - collocations: 貨幣を発行する; 貨幣を鋳造する; 貨幣価値; 貨幣経済; 貨幣制度
     - grammar: note In law, 貨幣 refers to coins only, as opposed to 紙幣.

### 28. ポリス

**Lexeme 1: ポリス**, NOUN
- said:  ぽりす, accent 1 (ja-JP)
  1. [B1] 町の安全を守る組織や、その仕事をする人
     - example: ハロウィンにはポリスの衣装を着る子どもが多い。
     - grammar: note Less common than 警察 or 警察官; often in loanword compounds.
  2. [C1] 古代ギリシャで、都市を中心につくられた国
     - example: 古代ギリシャには数多くのポリスが存在した。
     - collocations: 都市国家ポリス
     - labels: domain history

### 29. 外側

**Lexeme 1: 外側**, NOUN
- said:  そとがわ, accent 0 (ja-JP),  がいそく (ja-JP, less_common)
- parts: 外 (外) + 側 (側)
  1. [A2] 物や囲いの、中心から遠く表に面した部分
     - example: 箱の外側に名前を書いておいてください。
     - collocations: 外側の車線; 外側を歩く; 外側に開く
     - grammar: note がいそく is used mainly in anatomy and technical terms.
     - opposites: 内側
  2. [B2] 組織や物事に直接かかわらない立場
     - example: 会社の外側から見ると、問題がよくわかる。
     - collocations: 外側から見る
     - labels: figurative True
     - opposites: 内側

### 30. 安心

**Lexeme 1: 安心**, NOUN
- grammar: suru True; valency Intr
- said:  あんしん, accent 0 (ja-JP)
  1. [A2] 心配や不安がなく、落ち着いた気持ち
     - example: 母の元気な声を聞いて、やっと安心した。
     - frames: 〜に安心する; 〜て安心する; 安心して〜
     - collocations: 安心する; ほっと安心する; 安心させる; 安心して眠る; 安心して暮らす; 安心を得る; 安心感
     - opposites: 心配, 不安
**Lexeme 2: 安心**, ADJ
- grammar: conjugation na
- said:  あんしん, accent 0 (ja-JP)
  1. [B1] 危ないことや心配がなく、信じて任せられる
     - example: 駅から近くて明るい道なので、夜でも安心だ。
     - frames: 〜なら安心だ; 安心な〜
     - collocations: 安心な場所; 安心な食品
     - opposites: 不安

### 31. 含む

**Lexeme 1: 含む**, VERB
- grammar: conjugation godan; valency Tr
- said:  ふくむ, accent 2 (ja-JP)
- spellings: 含む (ja-JP); ふくむ (ja-JP)
  1. [B1] 中に成分や一部として持っている
     - example: この野菜はビタミンCを多く含んでいる。
     - frames: 〜を含む; 〜を含めた〜
     - collocations: 水分を含む; 税を含む; 送料を含む; 多く含む; 栄養を含む; 成分を含む
  2. [B2] 物を飲み込まずに口の中に入れておく
     - example: うがいをするときは、水を口に含んでから上を向く。
     - frames: 〜を口に含む
     - collocations: 口に含む; 水を口に含む
  3. [B2] 気持ちや意味をそれとなく内に持って表す
     - example: 彼女は少し怒りを含んだ声で返事をした。
     - frames: 〜を含んだ〜
     - collocations: 怒りを含む; 笑みを含む; 憂いを含む; 意味を含む; 皮肉を含む
     - labels: figurative True
  4. [C1] 事情などをよく心に留めておく
     - example: その点をよく含んでおいてください。
     - frames: 〜を含んでおく
     - grammar: note Usually in the form 含んでおく.
- relations: confusable 含める (VERB): 含める is to include something on purpose; 含む is to contain it

### 32. 容易

**Lexeme 1: 容易**, ADJ
- grammar: conjugation na
- said:  ようい, accent 0 (ja-JP)
  1. [B2] 手間がかからず、難しくない
     - example: この問題を短い期間で解決するのは容易ではない。
     - frames: 〜のは容易だ; 〜は容易だ; 容易に〜
     - collocations: 容易ではない; 容易に想像できる; 容易ならぬ事態; 容易に手に入る
     - labels: formality formal
     - opposites: 困難

### 33. プライバシー

**Lexeme 1: プライバシー**, NOUN
- said:  ぷらいばしー (ja-JP)
  1. [B1] 他人に知られたり干渉されたりしない個人の生活や秘密
     - example: ネットに他人の写真を載せるときは、プライバシーに気をつけよう。
     - collocations: プライバシーの侵害; プライバシーを侵害する; プライバシーを守る; プライバシーに配慮する; プライバシーを尊重する; プライバシーに関わる; プライバシーを保護する

### 34. 与える

**Lexeme 1: 与える**, VERB
- grammar: conjugation ichidan; valency Tr
- said:  あたえる (ja-JP)
- spellings: 与える (ja-JP); あたえる (ja-JP); 與える (ja-JP) [older books only]
  1. [B1] 自分の物を相手に渡して、その人のものにする
     - example: 毎朝、庭に来る鳥にえさを与えている。
     - frames: 〜に〜を与える
     - collocations: えさを与える; 水を与える; 肥料を与える; お金を与える
     - grammar: note Usually said of giving to someone of lower status, a child or an animal.
  2. [B1] 相手が使えるように、役目や権利などを割り当てる
     - example: 失敗した部下にも、もう一度機会を与えるべきだ。
     - frames: 〜に〜を与える
     - collocations: 機会を与える; チャンスを与える; 許可を与える; 権利を与える; 賞を与える; 仕事を与える; 課題を与える; ヒントを与える
  3. [B1] 相手や物事に作用して、ある結果や感じを生じさせる
     - example: 今回の台風は農作物に大きな被害を与えた。
     - frames: 〜に〜を与える
     - collocations: 影響を与える; 被害を与える; 損害を与える; 印象を与える; ショックを与える; 刺激を与える; 不安を与える; 勇気を与える; ダメージを与える

### 35. シューズ

**Lexeme 1: シューズ**, NOUN
- grammar: classifier 足; note Often the second part of compounds: ランニングシューズ, バスケットシューズ.
- said:  しゅーず (ja-JP)
  1. [B1] 運動や外出のときなどに足に履く洋風の靴
     - example: 週末に走るため、軽くて丈夫なシューズを買った。
     - collocations: シューズを履く; シューズを脱ぐ; シューズのひも; シューズを選ぶ

### 36. 業者

**Lexeme 1: 業者**, NOUN
- said:  ぎょうしゃ, accent 1 (ja-JP)
  1. [B1] 商売や事業を営んでいる人や会社
     - example: 引っ越しの見積もりを三つの業者に頼んだ。
     - frames: 〜業者
     - collocations: 業者に頼む; 業者に依頼する; 業者を選ぶ; 引っ越し業者; 清掃業者; 出入りの業者; 悪徳業者; 専門の業者

### 37. 必ず

**Lexeme 1: 必ず**, ADV
- said:  かならず, accent 0 (ja-JP)
- spellings: 必ず (ja-JP); かならず (ja-JP)
  1. [A2] どんなことがあっても間違いなく
     - example: 明日の会議には必ず出席します。
     - collocations: 必ず守る; 必ず来る; 必ず連絡する; 必ず成功する
  2. [B1] どの場合にも例外なく、いつも
     - example: 父は夕食の後に必ず散歩に出かける。
     - frames: 〜と必ず
     - collocations: 必ずと言っていいほど

### 38. 再開

**Lexeme 1: 再開**, NOUN
- grammar: suru True; note 再開する is used both transitively (〜を) and intransitively (〜が).
- said:  さいかい, accent 0 (ja-JP)
  1. [B1] 止まっていた物事をもう一度始めること
     - example: 雨がやんだので、試合は三十分後に再開された。
     - frames: 〜を再開する; 〜が再開する
     - collocations: 運転を再開する; 営業を再開する; 試合を再開する; 交渉を再開する; 授業を再開する; 運転再開; 営業再開
     - opposites: 中断
- relations: confusable 再会 (NOUN): same reading さいかい; 再会 means meeting someone again

### 39. 深い

**Lexeme 1: 深い**, ADJ
- grammar: conjugation i
- said:  ふかい, accent 2 (ja-JP)
- spellings: 深い (ja-JP); ふかい (ja-JP)
  1. [A2] 表面や入り口から底や奥までの距離が長い
     - example: この川は思ったより深いので、気をつけてください。
     - frames: 〜が深い
     - collocations: 深い海; 深い森; 深い穴; 深い川; 深い傷; 深く掘る
     - opposites: 浅い
  2. [B1] 気持ちや状態の程度が大きい
     - example: 祖母の死は家族に深い悲しみを残した。
     - collocations: 深い悲しみ; 深い眠り; 深い愛情; 深い感動; 深く感謝する; 深く反省する
     - opposites: 浅い
  3. [B1] 考えや内容が物事の奥まで行き届いている
     - example: 先生の短い言葉には深い意味が込められていた。
     - frames: 〜が深い
     - collocations: 奥が深い; 深い意味; 深い考え; 深い知識; 考えが深い; 深く考える
     - opposites: 浅い
  4. [B1] 人や物事のつながりが強い
     - example: 環境問題は私たちの生活と深い関係がある。
     - frames: 〜と関係が深い; 〜とのつながりが深い
     - collocations: 関係が深い; 深い仲; 縁が深い; 深いつながり; 深く関わる
     - opposites: 浅い
  5. [B2] 霧や草木、色などが濃い
     - example: 朝から深い霧が町をすっぽりと覆っている。
     - collocations: 深い霧; 深い緑; 草深い

### 40. チャージ

**Lexeme 1: チャージ**, NOUN
- said:  ちゃーじ, accent 1 (ja-JP)
  1. [A2] 前払い式のカードなどに使える金額を入れ足すこと
     - example: 駅の券売機で交通系ICカードに千円チャージした。
     - frames: 〜に〜をチャージする; 〜にチャージする
     - collocations: ICカードにチャージする; 現金でチャージする; 残高をチャージする
     - grammar: suru True
  2. [B1] 飲食店などで席や設備に対して取る料金
     - example: この店は一人五百円のテーブルチャージがかかる。
     - collocations: チャージがかかる; チャージを取る
  3. [B1] 体力や気力を補って元気を取り戻すこと
     - example: 週末はゆっくり休んでエネルギーをチャージしたい。
     - frames: 〜をチャージする
     - collocations: エネルギーをチャージする; パワーをチャージする
     - labels: figurative True, formality informal
     - grammar: suru True
  4. [C1] 競技で相手の選手に体をぶつけていくこと
     - example: 相手選手への強いチャージが反則と判定された。
     - frames: 〜にチャージする
     - labels: domain sport
     - grammar: suru True
