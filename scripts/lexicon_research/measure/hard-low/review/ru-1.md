# Russian: two entries for each word (10 words)

Each word below has two entries of a learner's dictionary of Russian, A and B, written by two different writers
under the same rules. You are not told which writer wrote which, and the sides change from word to word. Some of
these words are hard on purpose: two words under one spelling, forms that are not words of their own, senses of
one country only, rude words.

For each word say which entry serves a learner better, and name the faults of each. A writer may have refused a
word as no dictionary word of its own ("refused: inflected form of ..."); that is right for an inflected form, an
abbreviation or a name, and wrong for a word a dictionary of Russian lists.

## The faults

- **fact**: something the entry states is false. A gender, a plural or another form, a part of
  speech, an auxiliary, a transcription that gives the wrong sound (not a matter of notation), a
  definition that gives a meaning the word does not have.
- **claim**: a restriction stated too strongly or wrongly. A region ("only" where the sense is used
  elsewhere too), a label (formal, dated, offensive) the sense does not deserve, a grammar value
  that is wrong for the word's ordinary use. A grammar value is not a claim because an exception
  to it exists: see below what it states.
- **missing-core**: the entry lacks one of the two or three commonest meanings or parts of speech of
  the word, one a learner at B1 or B2 meets in ordinary text.
- **missing-minor**: it lacks a meaning that a learner's dictionary lists but that is rarer than
  those the entry has. Not technical, dialectal or obsolete meanings: those are left out on purpose.
- **split**: two numbered senses are one meaning, or a mere construction or an idiom is numbered as
  a sense.
- **wording**: a definition is narrower or wider than its sense, an example does not show its
  sense, or an example is not natural Russian.
- **collocation**: a collocation that is not said that way in Russian; or one that does not belong
  among the ten a learner is shown for the sense, because anyone would build it from the meanings
  or because it is rare; or a list that lacks the commonest partner of the sense while it has
  rarer ones. Quote the collocation in "what". A frame with the wrong preposition or case is a
  fact, not this.

These are this dictionary's own definitions, not faults:

- valency Refl stands for every use with a reflexive pronoun, whatever a grammar of Russian calls it
  (reflexive, reciprocal, pronominal); Tr means the verb takes a direct object, so a verb whose
  object needs a preposition or the dative is Intr.
- a grammar value (countability, valency, position, number) says how the word is ordinarily used, as
  a learner's dictionary of Russian prints it: "Mass" is "uncountable" in that sense, although kinds
  of the thing can be counted. Followed by "mainly", the other use is common too. Report a value
  only when a learner's dictionary would not print it for this word or sense.
- position Attr means the adjective is used joined to a noun and not after a verb such as "be",
  wherever Russian puts it (before the noun in English, mostly after it in French); Pred means only
  after such a verb. Neither says on which side of the noun the adjective stands.
- a lexeme is one word with one way of saying and inflecting it. Two homonyms that are said and
  inflected alike are senses of one lexeme here, although dictionaries of Russian number them apart.
- a headword that is also an inflected form of another word (given, ladies), an abbreviation or a
  name has only the lexemes it is the lemma of. The reader reaches the other word by a link that
  is not shown here.
- a frame is the word with what it governs, every open place marked (depend on sth); a collocation
  is the word with a particular word it usually goes with, or a set phrase, with no open place
  (heavy rain, in the lead). A sense holds at most five frames and ten collocations, so a missing one
  is not a fault.

## The answer

One line of JSON for every word, in the order given, inside a single ```json block:

```json
{"id": 1, "better": "A", "why": "B lacks the metal, the commoner of the two words spelt lead", "A": [], "B": [{"fault": "missing-core", "where": "lexemes", "what": "the metal is missing"}]}
```

"better" is "A", "B" or "same": "same" only when you could not say which you would rather give a learner. "why" is
one sentence. "A" and "B" list each entry's faults as above, empty when it has none.

## The words

### 1. замок

#### Entry A

**Lexeme 1: замок**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈza.mək/ за́мок (ru-RU)
- forms: замка [Case=Gen, Number=Sing] за́мка; замки [Case=Nom, Number=Plur] за́мки; замков [Case=Gen, Number=Plur] за́мков
  1. [A2] старинное укреплённое жилище знатного владельца со стенами и башнями
     - example: На вершине холма стоит старинный замок с высокими башнями.
     - collocations: средневековый замок; старинный замок; рыцарский замок; песочный замок; развалины замка
**Lexeme 2: замок**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /zɐ.ˈmok/ замо́к (ru-RU)
- forms: замка [Case=Gen, Number=Sing] замка́; замки [Case=Nom, Number=Plur] замки́; замков [Case=Gen, Number=Plur] замко́в
  1. [A2] устройство, которое запирают и открывают ключом или кодом
     - example: Не забудь закрыть дверь на замок, когда будешь уходить.
     - collocations: закрыть на замок; под замком; висячий замок; кодовый замок; ключ от замка; повесить замок; сломать замок; врезать замок
  2. [B2] приспособление, которое скрепляет концы цепочки, браслета или застёжки
     - example: У браслета сломался замок, и он упал в траву.
     - collocations: застегнуть замок

#### Entry B

**Lexeme 1: замок**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /zɐ.ˈmok/ замо́к (ru-RU)
- forms: замка [Case=Gen, Number=Sing] замка́; замки [Case=Nom, Number=Plur] замки́; замков [Case=Gen, Number=Plur] замко́в
  1. [A2] устройство для запирания чего-либо с помощью ключа или кода
     - example: Не забудь закрыть входную дверь на замок перед уходом из дома.
     - collocations: закрыть на замок; запереть на замок; дверной замок; навесной замок; кодовый замок; взломать замок
  2. [B1] подвижная застёжка для соединения концов украшения или краёв одежды и сумок
     - example: На моей зимней куртке опять заедает замок, придётся нести её в ремонт.
     - collocations: застегнуть замок; расстегнуть замок
**Lexeme 2: замок**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈza.mək/ за́мок (ru-RU)
- forms: замка [Case=Gen, Number=Sing] за́мка; замки [Case=Nom, Number=Plur] за́мки; замков [Case=Gen, Number=Plur] за́мков
  1. [B1] большое укреплённое здание, которое раньше служило жилищем и крепостью правителя или знатного человека
     - example: Во время поездки мы осмотрели старинный каменный замок на высоком берегу реки.
     - collocations: старинный замок; средневековый замок; рыцарский замок; королевский замок

### 2. мука

#### Entry A

**Lexeme 1: мука**, NOUN
- grammar: gender Fem; animacy Inan; number Sing, mainly; countability Mass; declension 1
- said: /mu.ˈka/ мука́ (ru-RU)
- forms: муки [Case=Gen, Number=Sing] муки́
  1. [A2] мелкий порошок из перемолотого зерна для выпечки хлеба и других продуктов
     - example: Перед приготовлением теста хозяйка просеяла пшеничную муку через мелкое сито.
     - collocations: пшеничная мука; ржаная мука; кукурузная мука; просеять муку; мешок муки
**Lexeme 2: мука**, NOUN
- grammar: gender Fem; animacy Inan; countability Both; declension 1
- said: /ˈmu.kə/ му́ка (ru-RU)
- forms: муки [Case=Gen, Number=Sing] му́ки; муки [Case=Nom, Number=Plur] му́ки; мук [Case=Gen, Number=Plur]
  1. [B2] сильная физическая боль или тяжёлое душевное страдание
     - example: После сложной операции больной ещё несколько дней терпел страшные муки.
     - collocations: терпеть муки; адские муки; душевные муки; муки совести; муки творчества; хождение по мукам
  2. [B2] занятие или дело, которое требует огромного труда и терпения
     - example: Собирать этот шкаф без понятной инструкции оказалось для нас сплошной мукой.
     - frames: мука + инф.; мука с кем/чем
     - collocations: сплошная мука; настоящая мука
     - labels: formality informal
     - grammar: number Sing; countability Mass

#### Entry B

**Lexeme 1: мука**, NOUN
- grammar: gender Fem; animacy Inan; countability Mass; number Sing; declension 1
- said: /mu.ˈka/ мука́ (ru-RU)
- forms: муки [Case=Gen, Number=Sing] муки́; муку [Case=Acc, Number=Sing] муку́
  1. [A2] мелкий порошок из размолотых зёрен, из которого пекут хлеб
     - example: Для этого пирога вам понадобится два стакана муки.
     - collocations: пшеничная мука; ржаная мука; кукурузная мука; мука высшего сорта; просеять муку; мешок муки
- relations: confusable мука (NOUN): spelt the same as му́ка, torment, which has the stress on the first syllable
**Lexeme 2: мука**, NOUN
- grammar: gender Fem; animacy Inan; declension 1
- said: /ˈmu.kə/ му́ка (ru-RU)
- forms: муки [Case=Gen, Number=Sing] му́ки; муки [Case=Nom, Number=Plur] му́ки; мук [Case=Gen, Number=Plur]
  1. [B2] сильное физическое или душевное страдание
     - example: Каждый шаг по горной тропе давался ему с мукой.
     - collocations: муки совести; муки творчества; адские муки; терпеть муки; одна мука; не жизнь, а мука; хождение по мукам
- relations: confusable мука (NOUN): spelt the same as мука́, flour, which has the stress on the last syllable

### 3. печь

#### Entry A

**Lexeme 1: печь**, NOUN
- grammar: gender Fem; animacy Inan; countability Count; declension 3
- said: /ˈpʲetɕ/ печь (ru-RU)
- forms: печи [Case=Gen, Number=Sing] пе́чи; печи [Case=Loc, Number=Sing] печи́ (after в and на); печи [Case=Nom, Number=Plur] пе́чи; печей [Case=Gen, Number=Plur] пече́й
  1. [A2] устройство для отопления помещения или для приготовления горячей пищи
     - example: Бабушка рано утром затопила в старом деревенском доме большую печь.
     - collocations: русская печь; микроволновая печь; затопить печь; топить печь
  2. [B2] промышленное сооружение с высокой температурой для плавки или обжига материалов
     - example: Рабочие завода внимательно следили за температурой металла в плавильной печи.
     - collocations: доменная печь; мартеновская печь; плавильная печь
**Lexeme 2: печь**, VERB
- grammar: aspect Imp; conjugation 1; irregular True; valency Both
- said: /ˈpʲetɕ/ печь (ru-RU)
- forms: пеку [Tense=Pres, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] пеку́; печёшь [Tense=Pres, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] печёшь; пекут [Tense=Pres, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] пеку́т; пёк [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] пёк; пекла [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] пекла́; пекло [Tense=Past, Gender=Neut, Number=Sing, VerbForm=Fin, Mood=Ind] пекло́; пекли [Tense=Past, Number=Plur, VerbForm=Fin, Mood=Ind] пекли́; пеки [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] пеки́
  1. [A2] готовить пищу сухим жаром на огне или в духовке
     - example: По выходным мы всей семьёй печём на завтрак тонкие блины.
     - frames: печь что
     - collocations: печь хлеб; печь пироги; печь блины; печь торт
     - grammar: valency Tr, mainly
  2. [B2] сильно греть, обдавать лучами или сухим жаром
     - example: В полдень летнее солнце беспощадно пекло нам открытые плечи.
     - frames: печь кого/что
- relations: aspect_partner испечь (VERB), for sense 1 only

#### Entry B

**Lexeme 1: печь**, NOUN
- grammar: gender Fem; animacy Inan; countability Count; declension 3
- said: /ˈpʲetɕ/ печь (ru-RU)
- forms: печи [Case=Gen, Number=Sing] пе́чи; печи [Case=Nom, Number=Plur] пе́чи; печей [Case=Gen, Number=Plur] пече́й; печи [Case=Loc, Number=Sing] печи́ (after в and на: в печи́, but о пе́чи)
  1. [B1] устройство из кирпича или металла для отопления дома и приготовления еды
     - example: Зимой мы каждое утро топим печь сухими дровами.
     - collocations: топить печь; растопить печь; русская печь; лежать на печи
  2. [B1] устройство для нагрева или обработки чего-либо высокой температурой
     - example: Разогрей суп в микроволновой печи, он совсем остыл.
     - collocations: микроволновая печь; доменная печь; плавильная печь
**Lexeme 2: печь**, VERB
- grammar: aspect Imp; valency Both; conjugation 1
- said: /ˈpʲetɕ/ печь (ru-RU)
- forms: пеку [Tense=Pres, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] пеку́; печёшь [Tense=Pres, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] печёшь; пекут [Tense=Pres, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] пеку́т; пёк [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind]; пекла [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] пекла́; пеки [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] пеки́
  1. [A2] готовить еду сухим жаром в духовке или на углях
     - example: По выходным бабушка всегда печёт нам пироги с капустой.
     - frames: печь что; печь кому что
     - collocations: печь пироги; печь хлеб; печь блины; печь торт; печь картошку
     - grammar: valency Tr
  2. [B2] сильно жечь своими горячими лучами
     - example: В полдень солнце печёт так, что невозможно выйти на улицу.
     - collocations: солнце печёт
     - grammar: valency Intr; note said of the sun; often impersonal: печёт
- relations: aspect_partner испечь (VERB), for sense 1 only

### 4. три

#### Entry A

**Lexeme 1: три**, NUM
- said: /ˈtrʲi/ три (ru-RU)
- forms: трёх [Case=Gen,Loc]; трёх [Case=Acc, Animacy=Anim]; трём [Case=Dat]; тремя [Case=Ins] тремя́
  1. [A1] число и количество, равное сумме двух и одного
     - example: Мы провели в этом уютном городе целых три дня.
**Lexeme 2: три**, NOUN
- grammar: gender Neut; animacy Inan; invariable True
- said: /ˈtrʲi/ три (ru-RU)
  1. [A2] школьная оценка, означающая удовлетворительное знание учебного предмета
     - example: За последнюю контрольную работу по физике он получил три.
     - collocations: получить три; сдать на три
     - labels: formality informal

#### Entry B

**Lexeme 1: три**, NUM
- grammar: note In the nominative and inanimate accusative the noun after it is genitive singular: три книги.
- said: /ˈtrʲi/ три (ru-RU)
- forms: трёх [Case=Gen,Loc]; трём [Case=Dat]; тремя [Case=Ins] тремя́
  1. [A1] число, на единицу большее двух
     - example: Мы прожили в этом маленьком городе три года.
     - frames: три кого/чего
     - collocations: три раза; в три часа; три четверти; раз, два, три

### 5. кофе

#### Entry A

**Lexeme 1: кофе**, NOUN
- grammar: gender Masc; animacy Inan; countability Mass; invariable True; note also neuter in informal speech
- said: /ˈko.fʲɪ/ ко́фе (ru-RU), /ˈko.fə/ ко́фе (ru-RU, older)
  1. [A1] бодрящий напиток тёмного цвета из обжаренных и молотых зёрен южного растения, а также сами зёрна и порошок из них
     - example: По утрам я обычно пью крепкий чёрный кофе без сахара.
     - collocations: варить кофе; чёрный кофе; крепкий кофе; растворимый кофе; молотый кофе; кофе в зёрнах; кофе с молоком; чашка кофе

#### Entry B

**Lexeme 1: кофе**, NOUN
- grammar: gender Masc; animacy Inan; countability Mass; invariable True; note neuter agreement (вкусное кофе) is accepted in casual speech
- said: /ˈko.fʲɪ/ ко́фе (ru-RU)
  1. [A1] тёмный горячий напиток из обжаренных молотых зёрен тропического дерева
     - example: По утрам я обычно пью крепкий кофе без сахара.
     - collocations: чёрный кофе; кофе с молоком; крепкий кофе; варить кофе; заварить кофе; пить кофе; чашка кофе; сварить кофе
  2. [A2] обжаренные зёрна тропического дерева или порошок из них
     - example: Не забудь купить в магазине пачку молотого кофе.
     - collocations: молотый кофе; кофе в зёрнах; растворимый кофе; молоть кофе; пачка кофе

### 6. человек

#### Entry A

**Lexeme 1: человек**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; declension 2; irregular True
- said: /tɕɪ.lɐ.ˈvʲek/ челове́к (ru-RU)
- forms: человека [Case=Gen, Number=Sing] челове́ка; люди [Case=Nom, Number=Plur] лю́ди; людей [Case=Gen, Number=Plur] люде́й; людям [Case=Dat, Number=Plur] лю́дям; людьми [Case=Ins, Number=Plur] людьми́; людях [Case=Loc, Number=Plur] лю́дях; человек [Case=Gen, Number=Plur] челове́к (after numbers and words of quantity)
  1. [A1] живое существо, которое умеет думать, говорить и создавать что-либо
     - example: В комнате уже ждали пять незнакомых мне человек.
     - collocations: молодой человек; пожилой человек; взрослый человек; родной человек; близкий человек; посторонний человек; живой человек; права человека
  2. [B1] тот, кто обладает определёнными чертами характера, взглядами или положением
     - example: Мой дедушка всю жизнь оставался человеком чести и слова.
     - collocations: человек дела; человек слова; деловой человек; публичный человек; свой человек

#### Entry B

**Lexeme 1: человек**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; declension 2; irregular True; note plural is formed from another stem: люди
- said: /tɕɪ.lɐ.ˈvʲek/ челове́к (ru-RU)
- forms: человека [Case=Gen, Number=Sing] челове́ка; люди [Case=Nom, Number=Plur] лю́ди; людей [Case=Gen, Number=Plur] люде́й; человек [Case=Gen, Number=Plur] челове́к (after numbers and сколько, несколько: пять человек); людьми [Case=Ins, Number=Plur] людьми́
  1. [A1] живое существо, обладающее разумом и речью
     - example: В соседней комнате сидел незнакомый человек в сером пальто.
     - collocations: молодой человек; пожилой человек; простой человек; права человека; каждый человек
  2. [A2] личность с определёнными чертами характера и поведения
     - example: Мой дед был честным и очень добрым человеком.
     - collocations: настоящий человек; близкий человек; свой человек; человек слова; стать человеком
  3. [C2] слуга в богатом доме или работник трактира
     - example: Эй, человек, подай-ка нам ещё горячего чаю!
     - labels: time archaic

### 7. год

#### Entry A

**Lexeme 1: год**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈɡot/ год (ru-RU)
- forms: года [Case=Gen, Number=Sing] го́да; году [Case=Loc, Number=Sing] году́ (after в: в этом году́, but о го́де); годы [Case=Nom, Number=Plur] го́ды; года [Case=Nom, Number=Plur] года́ (colloquial, mostly of age: года́ не те); лет [Case=Gen, Number=Plur] (after numbers and words of quantity: пять лет, много лет); годов [Case=Gen, Number=Plur] годо́в (of decades and periods: 90-х годо́в); годам [Case=Dat, Number=Plur] года́м
  1. [A1] промежуток времени в двенадцать месяцев
     - example: Мы живём в этом городе уже почти три года.
     - collocations: в этом году; в прошлом году; в следующем году; Новый год; круглый год; из года в год; год за годом; високосный год; через год; каждый год
  2. [A2] установленный период учёбы или работы от начала до конца
     - example: Учебный год в наших школах начинается первого сентября.
     - collocations: учебный год; финансовый год; отчётный год
  3. [B1] период времени в жизни человека или в истории
     - example: В годы войны мой дед работал на большом заводе.
     - frames: в годы чего
     - collocations: девяностые годы; школьные годы; студенческие годы; лучшие годы; с годами
     - grammar: number Plur
  4. [B2] возраст человека, обычно уже немолодой
     - example: В твои годы я уже работал и жил отдельно.
     - collocations: в годах; не по годам; годы не те
     - grammar: number Plur

#### Entry B

**Lexeme 1: год**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2; irregular True
- said: /ˈɡot/ год (ru-RU)
- forms: года [Case=Gen, Number=Sing] го́да; году [Case=Loc, Number=Sing] году́ (after в and на); годы [Case=Nom, Number=Plur] го́ды; года [Case=Nom, Number=Plur] года́ (informal); годов [Case=Gen, Number=Plur] годо́в; лет [Case=Gen, Number=Plur] (used with numbers and words of quantity)
  1. [A1] промежуток времени в двенадцать месяцев, за который Земля делает оборот вокруг Солнца
     - example: В следующем году наша семья планирует переехать в другой город.
     - collocations: каждый год; прошлый год; следующий год; круглый год; учебный год; год рождения; раз в год; из года в год
  2. [B1] период из нескольких лет, объединённых общей чертой или временем жизни человека
     - example: В студенческие годы мы часто ездили на море всей группой.
     - collocations: молодые годы; детские годы; школьные годы; последние годы; долгие годы
     - grammar: number Plur
  3. [A2] число лет, прожитых человеком, или его возраст в определённую пору жизни
     - example: Моей младшей сестре недавно исполнилось двадцать лет, но выглядит она моложе.
     - collocations: в мои годы; не по годам; в годах
     - grammar: number Plur, mainly

### 8. нравиться

#### Entry A

**Lexeme 1: нравиться**, VERB
- grammar: aspect Imp; valency Intr; conjugation 2; reflexive True
- said: /ˈnra.vʲi.tsːə/ нра́виться (ru-RU)
- forms: нравлюсь [Tense=Pres, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] нра́влюсь; нравишься [Tense=Pres, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] нра́вишься; нравятся [Tense=Pres, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] нра́вятся; нравился [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] нра́вился; нравилась [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] нра́вилась; нравься [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] нра́вься
  1. [A1] вызывать у кого-либо приятное чувство, симпатию или одобрение
     - example: Мне очень нравится гулять по осеннему парку ранним утром.
     - frames: нравиться кому; кому нравится + инф.; кому нравится, что ...
     - collocations: очень нравиться; совсем не нравиться; безумно нравиться; сразу нравиться
- relations: aspect_partner понравиться (VERB)

#### Entry B

**Lexeme 1: нравиться**, VERB
- grammar: aspect Imp; valency Intr; conjugation 2
- said: /ˈnra.vʲɪ.tsə/ нра́виться (ru-RU)
- forms: нравлюсь [Tense=Pres, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] нра́влюсь; нравишься [Tense=Pres, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] нра́вишься; нравятся [Tense=Pres, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] нра́вятся; нравился [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] нра́вился; нравилась [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] нра́вилась; нравься [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin]
  1. [A1] вызывать у кого-либо приятное чувство, симпатию, быть по вкусу
     - example: Мне очень нравится гулять по вечерам в старом парке.
     - frames: нравиться кому; кому нравится + инф.
     - collocations: очень нравиться; больше всего нравиться; не нравиться
     - grammar: note the person who likes is in the dative; the thing liked is the subject
- relations: aspect_partner понравиться (VERB)

### 9. блин

#### Entry A

**Lexeme 1: блин**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈblʲin/ блин (ru-RU)
- forms: блина [Case=Gen, Number=Sing] блина́; блины [Case=Nom, Number=Plur] блины́; блинов [Case=Gen, Number=Plur] блино́в
  1. [A2] тонкая круглая лепёшка из жидкого теста, испечённая на сковороде
     - example: Бабушка напекла к завтраку целую тарелку горячих блинов со сметаной.
     - frames: блин с чем
     - collocations: печь блины; блины со сметаной; блины с творогом; блины с икрой; первый блин комом
**Lexeme 2: блин**, INTJ
- said: /ˈblʲin/ блин (ru-RU)
  1. [B1] выражает досаду, разочарование, удивление или раздражение по какому-либо поводу
     - example: Я, блин, опять забыл ключи от квартиры на рабочем столе!
     - labels: formality informal, attitude euphemistic

#### Entry B

**Lexeme 1: блин**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈblʲin/ блин (ru-RU)
- forms: блина [Case=Gen, Number=Sing] блина́; блины [Case=Nom, Number=Plur] блины́; блинов [Case=Gen, Number=Plur] блино́в
  1. [A2] тонкая круглая лепёшка из жидкого теста, поджаренная на сковороде
     - example: По воскресеньям бабушка всегда печёт нам блины на завтрак.
     - frames: блин с чем
     - collocations: печь блины; блины со сметаной; блины с икрой; первый блин комом
  2. [C1] плоский металлический диск, который надевают на гриф штанги
     - example: Он повесил на штангу ещё два тяжёлых блина.
     - labels: formality informal
**Lexeme 2: блин**, INTJ
- said: /ˈblʲin/ блин (ru-RU)
  1. [B1] используется для выражения досады, раздражения или удивления
     - example: Блин, я опять забыл ключи от квартиры дома!
     - labels: formality informal, attitude euphemistic

### 10. нельзя

#### Entry A

**Lexeme 1: нельзя**, ADV
- grammar: position Pred; invariable True; note predicative; the person is in the dative, the action an infinitive
- said: /nʲɪlʲ.ˈzʲa/ нельзя́ (ru-RU)
  1. [A1] не разрешается, не позволено делать что-либо
     - example: Детям нельзя смотреть этот фильм без родителей.
     - frames: нельзя + инф.; кому нельзя + инф.; нельзя ли + инф.
     - collocations: здесь нельзя курить; так нельзя; нельзя ли потише
     - opposites: можно
  2. [A2] невозможно, нет возможности сделать что-либо
     - example: Этот старый замок нельзя открыть без ключа.
     - frames: нельзя + инф.; кому нельзя + инф.; нельзя не + инф.
     - collocations: как нельзя лучше; нельзя не признать; нельзя сказать
     - opposites: можно

#### Entry B

**Lexeme 1: нельзя**, ADV
- grammar: invariable True; position Pred
- said: /nʲɪlʲ.ˈzʲa/ нельзя́ (ru-RU)
  1. [A1] запрещено правилами, законом, обычаем или чьим-либо решением
     - example: В этом зале нельзя фотографировать со вспышкой.
     - frames: нельзя + инф.; кому нельзя + инф.
     - collocations: категорически нельзя; строго нельзя; ни в коем случае нельзя
     - opposites: можно
  2. [A1] нет возможности сделать что-либо из-за обстоятельств
     - example: Из-за сильного тумана сегодня нельзя вылететь вовремя.
     - frames: нельзя + инф.; кому нельзя + инф.
     - collocations: никак нельзя; больше нельзя; нельзя сказать; нельзя не согласиться
     - opposites: можно
  3. [B2] до предельной степени, дальше которой уже невозможно
     - example: Дела у нашей команды обстоят как нельзя лучше.
     - labels: informal informal
