# Russian: entries to check (1 of 1)

These are entries of a learner's dictionary of Russian, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of Russian.

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
  sense, or an example is not natural Russian.
- **collocation**: a collocation that is not said that way in Russian; or one that does not belong
  among the ten a learner is shown for the sense, because anyone would build it from the meanings
  or because it is rare; or a list that lacks the commonest partner of the sense while it has
  rarer ones. Quote the collocation in "what". A frame with the wrong preposition or case is a
  fact, not this.

Do not report: the order of senses, a CEFR level off by one step, how syllables are divided, a
definition you would have worded differently when the given one is true, or anything about style.

These are this dictionary's own definitions, not faults:

- valency Refl stands for every use with a reflexive pronoun, whatever a grammar of Russian calls it
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

### 1. нож

**Lexeme 1: нож**, NOUN
- grammar: gender Masc; animacy Inan; countability Count
- said: /ˈnoʂ/ нож (ru-RU)
- forms: ножа [Case=Gen, Number=Sing] ножа́; ножи [Case=Nom, Number=Plur] ножи́; ножей [Case=Gen, Number=Plur] ноже́й
  1. [A1] инструмент с острым лезвием и ручкой для резания
     - example: Передай мне, пожалуйста, нож, я порежу хлеб.
  2. [C1] острая режущая часть машины или прибора
     - example: Нож у мясорубки совсем затупился, его надо наточить.

### 2. давать

**Lexeme 1: давать**, VERB
- grammar: aspect Imp; valency Tr; irregular True; note Loses -ва- in the present tense: даю, даёшь, but keeps it in the imperative давай.
- said: /də.ˈvatʲ/ дава́ть (ru-RU)
- forms: даю [Person=1, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] даю́; даёшь [Person=2, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] даёшь; дают [Person=3, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] даю́т; давал [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin] дава́л; давала [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin] дава́ла; давай [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] дава́й
  1. [A1] вручать кому-то вещь, чтобы он мог её взять или иметь
     - example: Бабушка всегда даёт внукам конфеты после обеда.
     - frames: давать кому что
     - opposites: брать
  2. [B1] позволять, разрешать кому-то что-то делать
     - example: Соседская собака не даёт мне спать по ночам.
     - frames: давать кому + инф.
  3. [B2] приносить, производить как результат
     - example: Эти занятия уже дают хорошие результаты.
  4. [B2] проводить для публики или гостей выступление, урок или приём
     - example: Каждую субботу оркестр даёт концерты в этом зале.
- relations: aspect_partner дать (VERB)

### 3. петь

**Lexeme 1: петь**, VERB
- grammar: aspect Imp; valency Both; irregular True; note present stem по-: пою, поёшь
- said: /ˈpʲetʲ/ петь (ru-RU)
- forms: пою [Tense=Pres, Person=1, Number=Sing] пою́; поёшь [Tense=Pres, Person=2, Number=Sing] поёшь; поют [Tense=Pres, Person=3, Number=Plur] пою́т; пел [Tense=Past, Gender=Masc, Number=Sing]; пела [Tense=Past, Gender=Fem, Number=Sing] пе́ла; пой [Mood=Imp, Person=2, Number=Sing]
  1. [A1] исполнять голосом музыку, издавать голосом музыкальные звуки
     - example: Моя сестра прекрасно поёт и играет на гитаре.
     - frames: петь что; петь кому
  2. [B1] издавать звонкие мелодичные звуки — о птицах и насекомых
     - example: Весной в нашем саду с самого утра поют соловьи.
     - grammar: valency Intr
- relations: aspect_partner спеть (VERB)

### 4. вид

**Lexeme 1: вид**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈvʲit/ вид (ru-RU)
- forms: вида [Case=Gen, Number=Sing] ви́да; виды [Case=Nom, Number=Plur] ви́ды; видов [Case=Gen, Number=Plur] ви́дов; виду [Case=Loc, Number=Sing] виду́ (after в and на in set phrases: на виду́, в виду́; but в ви́де); виду [Case=Par, Number=Sing] ви́ду (in set phrases: не подава́ть ви́ду, из ви́ду)
  1. [A2] внешний облик человека или предмета
     - example: После долгой болезни у неё был очень усталый вид.
     - collocations: делать вид; иметь вид; не подавать виду; внешний вид; усталый вид; с серьёзным видом; на вид
  2. [A2] отдельный тип среди предметов или явлений, разновидность
     - example: Плавание — мой самый любимый вид спорта.
     - frames: вид чего
     - collocations: вид спорта; вид транспорта; вид деятельности; все виды
  3. [A2] пейзаж или пространство, которое открывается перед глазами
     - example: Из окна нашей гостиницы открывается прекрасный вид на море.
     - frames: вид на что; вид из чего
     - collocations: открывается вид; красивый вид; вид из окна; номер с видом на море
  4. [B1] форма или состояние, в котором кто-либо или что-либо находится
     - example: Новый шкаф нам привезли в разобранном виде.
     - collocations: в готовом виде; в чистом виде; в пьяном виде; в письменном виде; в электронном виде
  5. [B2] пространство, которое человек может охватить взглядом
     - example: Лодка быстро скрылась из виду за большим островом.
     - collocations: потерять из виду; скрыться из виду; на виду; на виду у всех
  6. [B2] группа сходных живых организмов, способных давать общее потомство
     - example: Многие виды животных исчезают из-за деятельности человека.
     - collocations: редкий вид; исчезающий вид; вымирающий вид; виды растений
     - labels: domain biology
  7. [B1] категория глагола, которая показывает законченность или незаконченность действия
     - example: В русском языке почти каждый глагол имеет свой вид.
     - collocations: совершенный вид; несовершенный вид
     - labels: domain grammar
  8. [C1] планы и расчёты на кого-либо или что-либо в будущем
     - example: У отца были большие виды на старшего сына.
     - frames: виды на кого/что
     - collocations: иметь виды
     - grammar: number Plur

### 5. брак

**Lexeme 1: брак**, NOUN
- grammar: gender Masc; animacy Inan; declension 2
- said: /ˈbrak/ брак (ru-RU)
- forms: брака [Case=Gen, Number=Sing] бра́ка; браки [Case=Nom, Number=Plur] бра́ки; браков [Case=Gen, Number=Plur] бра́ков
  1. [B1] официально признанный семейный союз двух людей
     - example: Они прожили в браке почти сорок лет и вырастили троих детей.
     - frames: брак с кем; брак между кем
     - collocations: вступить в брак; заключить брак; состоять в браке; расторгнуть брак; гражданский брак; счастливый брак; второй брак; родиться в браке
     - grammar: countability Count
  2. [B2] плохо сделанные вещи или изъяны в изготовленной продукции
     - example: На заводе ввели строгий контроль, чтобы не выпускать брак.
     - collocations: заводской брак; выпускать брак; допустить брак; брак в работе; отправить в брак
     - grammar: countability Mass; number Sing

### 6. мяч

**Lexeme 1: мяч**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈmʲatɕ/ мяч (ru-RU)
- forms: мяча [Case=Gen, Number=Sing] мяча́; мячи [Case=Nom, Number=Plur] мячи́; мячей [Case=Gen, Number=Plur] мяче́й
  1. [A1] круглый упругий предмет, который бросают, ловят или бьют в игре
     - example: Дети весь вечер играли во дворе в мяч.
     - collocations: играть в мяч; забить мяч; бить по мячу; бросать мяч; ловить мяч; отбить мяч; передать мяч; футбольный мяч; теннисный мяч; кожаный мяч

### 7. лейтенант

**Lexeme 1: лейтенант**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; declension 2
- said: /lʲɪj.tʲɪ.ˈnant/ лейтена́нт (ru-RU)
- forms: лейтенанта [Case=Gen, Number=Sing] лейтена́нта; лейтенанты [Case=Nom, Number=Plur] лейтена́нты; лейтенантов [Case=Gen, Number=Plur] лейтена́нтов
  1. [B1] младшее офицерское звание в армии или полиции, а также человек с этим званием
     - example: Молодой лейтенант быстро построил солдат во дворе казармы.
     - collocations: старший лейтенант; младший лейтенант; лейтенант полиции; звание лейтенанта; получить звание лейтенанта

### 8. введение

**Lexeme 1: введение**, NOUN
- grammar: gender Neut; animacy Inan; declension 2
- said: /vʲːɪ.ˈdʲe.nʲɪ.jə/ введе́ние (ru-RU)
- forms: введения [Case=Gen, Number=Sing] введе́ния; введения [Case=Nom, Number=Plur] введе́ния; введений [Case=Gen, Number=Plur] введе́ний
  1. [B2] начало применения или действия чего-либо нового
     - example: Введение новых правил вызвало много споров среди жителей города.
     - frames: введение чего
     - collocations: введение в действие; введение в эксплуатацию; введение санкций; введение войск; введение запрета; поэтапное введение
     - grammar: countability Mass
     - opposites: отмена
  2. [B1] вступительная часть текста перед основным содержанием
     - example: Прочитай сначала введение, там объясняется цель всей книги.
     - frames: введение к чему
     - collocations: во введении; написать введение; краткое введение
     - grammar: countability Count
     - opposites: заключение
  3. [B2] краткое изложение основ науки или предмета для начинающих
     - example: На первом курсе студенты слушают введение в языкознание.
     - frames: введение во что
     - collocations: введение в специальность
     - grammar: countability Count
- relations: verbal_noun_of ввести (VERB)

### 9. суд

**Lexeme 1: суд**, NOUN
- grammar: gender Masc; animacy Inan; countability Count
- said: /ˈsut/ суд (ru-RU)
- forms: суда [Case=Gen, Number=Sing] суда́; суды [Case=Nom, Number=Plur] суды́; судов [Case=Gen, Number=Plur] судо́в
  1. [B1] государственный орган, который рассматривает дела и выносит решения по закону
     - example: Соседи решили подать в суд, чтобы вернуть свои деньги.
     - frames: подать в суд на кого
     - collocations: через суд
  2. [B2] заседание, на котором рассматривают дело и выносят приговор или решение
     - example: Суд над бывшим министром продлится несколько месяцев.
     - frames: суд над кем
     - collocations: на суде
  3. [C1] оценка или мнение, которые дают чьим-то поступкам или работе
     - example: Молодой художник представил свои новые картины на суд зрителей.
     - frames: на суд кого
     - grammar: number Sing
- relations: confusable судно (NOUN): its plural суда́ (ships) looks and sounds like суда́, the genitive of суд

### 10. боевой

**Lexeme 1: боевой**, ADJ
- said: /bə.jɪ.ˈvoj/ боево́й (ru-RU)
  1. [B1] предназначенный для сражения или связанный с ведением войны
     - example: Солдаты получили боевые патроны и отправились на задание.
     - collocations: боевые действия; боевая готовность; боевой самолёт; боевые патроны; боевое оружие; боевой опыт; боевой товарищ; боевые искусства; боевое задание
     - grammar: position Attr; comparable False
  2. [B2] смелый, решительный и энергичный, всегда готовый действовать
     - example: У моей бабушки до сих пор очень боевой характер.
     - collocations: боевой характер; боевое настроение; боевая девчонка
     - labels: formality informal
- relations: adjective_of бой (NOUN)

### 11. инженер

**Lexeme 1: инженер**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; note also used of women, with masculine agreement
- said: /ɪn.ʐɨ.ˈnʲer/ инжене́р (ru-RU)
- forms: инженера [Case=Gen, Number=Sing] инжене́ра; инженеры [Case=Nom, Number=Plur] инжене́ры; инженеров [Case=Gen, Number=Plur] инжене́ров
  1. [A2] специалист с высшим образованием в области техники
     - example: Мой брат работает инженером на большом заводе.
     - collocations: работать инженером

### 12. попытка

**Lexeme 1: попытка**, NOUN
- grammar: gender Fem; animacy Inan; countability Count; declension 1
- said: /pɐ.ˈpɨt.kə/ попы́тка (ru-RU)
- forms: попытки [Case=Gen, Number=Sing] попы́тки; попытки [Case=Nom, Number=Plur] попы́тки; попыток [Case=Gen, Number=Plur] попы́ток
  1. [B1] действие, которым стараются чего-либо достичь, без уверенности в успехе
     - example: Его попытка открыть дверь чужим ключом оказалась безуспешной.
     - frames: попытка + инф.; попытка чего
     - collocations: сделать попытку; предпринять попытку; с первой попытки; неудачная попытка; безуспешная попытка; отчаянная попытка; последняя попытка; попытка не пытка
  2. [B2] одно из нескольких выполнений упражнения на спортивном соревновании
     - example: У каждого прыгуна в финале будет ещё три попытки.
     - collocations: лучшая попытка; зачётная попытка; использовать попытку
- relations: derived_from попытаться (VERB)

### 13. итак

**Lexeme 1: итак**, CONJ
- grammar: invariable True; note set off by a comma, usually at the start of a sentence
- said: /ɪ.ˈtak/ ита́к (ru-RU)
  1. [B1] употребляется, чтобы сделать вывод из того, что сказано
     - example: Итак, мы решили, что план нужно немного изменить.
  2. [B1] употребляется в начале речи или при переходе к новой теме
     - example: Итак, начнём сегодняшний урок с повторения новых слов.

### 14. пора

**Lexeme 1: пора**, ADV
- grammar: position Pred
- said: /pɐ.ˈra/ пора́ (ru-RU)
  1. [A2] наступило время, когда нужно или следует что-либо сделать
     - example: Уже совсем поздно, нам пора ехать домой.
     - frames: кому пора + инф.; пора + инф.; кому пора куда; пора на что
     - collocations: давно пора; мне пора; пора спать; пора домой
- relations: derived_from пора (NOUN): the noun пора́, time
**Lexeme 2: пора**, NOUN
- grammar: gender Fem; animacy Inan; countability Count; declension 1
- said: /pɐ.ˈra/ пора́ (ru-RU)
- forms: поры [Case=Gen, Number=Sing] поры́; пору [Case=Acc, Number=Sing] по́ру; поры [Case=Nom, Number=Plur] по́ры; пор [Case=Gen, Number=Plur]; порам [Case=Dat, Number=Plur] пора́м
  1. [B1] период времени, когда что-либо происходит или бывает
     - example: В ту пору мы жили в маленьком доме у реки.
     - collocations: в ту пору; в эту пору; пора года; весенняя пора; осенняя пора; золотая пора; до поры до времени
- relations: confusable пора (NOUN): по́ра, a pore, differs only in stress
**Lexeme 3: пора**, NOUN
- grammar: gender Fem; animacy Inan; countability Count; declension 1
- said: /ˈpo.rə/ по́ра (ru-RU)
- forms: поры [Case=Gen, Number=Sing] по́ры; поры [Case=Nom, Number=Plur] по́ры; пор [Case=Gen, Number=Plur]
  1. [C1] очень маленькое отверстие на поверхности кожи, листа или материала
     - example: Этот крем хорошо очищает поры и делает кожу гладкой.
     - collocations: расширенные поры; забитые поры; очищать поры

### 15. председатель

**Lexeme 1: председатель**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; declension 2; note also used of a woman, usually with masculine agreement
- said: /prʲɪ.tsʲɪ.ˈda.tʲɪlʲ/ председа́тель (ru-RU)
- forms: председателя [Case=Gen, Number=Sing] председа́теля; председатели [Case=Nom, Number=Plur] председа́тели; председателей [Case=Gen, Number=Plur] председа́телей
  1. [B1] руководитель организации, комитета, совета или другого коллектива
     - example: Председатель комиссии подписал все документы ещё утром.
     - frames: председатель чего
     - collocations: председатель правительства; председатель совета директоров; председатель суда; председатель комитета; избрать председателем; заместитель председателя; председатель колхоза
  2. [B2] человек, который ведёт собрание или заседание
     - example: Председатель собрания предоставил слово следующему выступающему.
     - frames: председатель чего
     - collocations: председатель собрания; председатель заседания; председатель жюри

### 16. научный

**Lexeme 1: научный**, ADJ
- said: /nɐ.ˈutɕ.nɨj/ нау́чный (ru-RU)
  1. [B1] посвящённый исследованиям и знаниям, которые получают учёные
     - example: Она сейчас пишет научную статью о климате северных морей.
     - grammar: comparable False
  2. [B2] основанный на точных фактах, проверке и строгих методах исследования
     - example: Этот вопрос требует серьёзного научного подхода, а не догадок.
     - grammar: comparable True
     - opposites: ненаучный
- relations: adjective_of наука (NOUN)

### 17. захватить

**Lexeme 1: захватить**, VERB
- grammar: aspect Perf; valency Tr
- said: /zə.xvɐ.ˈtʲitʲ/ захвати́ть (ru-RU)
- forms: захвачу [Person=1, Number=Sing, Tense=Fut, VerbForm=Fin] захвачу́; захватишь [Person=2, Number=Sing, Tense=Fut, VerbForm=Fin] захва́тишь; захватят [Person=3, Number=Plur, Tense=Fut, VerbForm=Fin] захва́тят; захватил [Tense=Past, Gender=Masc, Number=Sing] захвати́л; захватила [Tense=Past, Gender=Fem, Number=Sing] захвати́ла; захвати [Mood=Imp, Person=2, Number=Sing] захвати́
  1. [B1] силой взять в своё владение или под свою власть
     - example: Армия захватила город после долгой и тяжёлой осады.
     - frames: захватить что
  2. [A2] взять с собой, отправляясь куда-нибудь
     - example: Не забудь захватить зонтик, на улице идёт дождь.
     - frames: захватить что с собой
  3. [B2] сильно увлечь, полностью занять чьё-то внимание или чувства
     - example: Этот роман захватил меня с самой первой страницы.
     - frames: захватить кого
     - labels: figurative True
  4. [B2] успеть застать кого-то или что-то, пока не поздно
     - example: Если поторопимся, ещё захватим последний поезд.
     - frames: захватить кого/что
     - labels: formality informal
- relations: aspect_partner захватывать (VERB)

### 18. корреспондент

**Lexeme 1: корреспондент**, NOUN
- grammar: gender Masc; animacy Anim; countability Count
- said: /kə.rʲɪ.spɐn.ˈdʲent/ корреспонде́нт (ru-RU)
- forms: корреспондента [Case=Gen, Number=Sing] корреспонде́нта; корреспонденты [Case=Nom, Number=Plur] корреспонде́нты; корреспондентов [Case=Gen, Number=Plur] корреспонде́нтов
  1. [B1] журналист, сообщающий новости с места событий для газеты, радио или телевидения
     - example: Наш корреспондент уже три года работает в столице.
     - frames: корреспондент чего (газеты, агентства); корреспондент в чём (в стране, в городе)
  2. [C1] человек, с которым кто-либо ведёт переписку
     - example: Писатель всегда лично отвечал каждому своему корреспонденту.
     - labels: formality formal

### 19. почему

**Lexeme 1: почему**, ADV
- said: /pə.tɕɪ.ˈmu/ почему́ (ru-RU)
  1. [A1] используется в вопросе, чтобы узнать причину чего-либо
     - example: Почему ты вчера не пришёл на занятия?
     - collocations: почему бы не; почему же; почему так
  2. [A2] используется, чтобы присоединить придаточное предложение о причине
     - example: Я так и не понял, почему он уехал так рано.
     - frames: почему ...
     - collocations: вот почему; не знаю почему; не понимаю почему

### 20. способ

**Lexeme 1: способ**, NOUN
- grammar: gender Masc; animacy Inan; countability Count
- said: /ˈspo.səp/ спо́соб (ru-RU)
- forms: способа [Case=Gen, Number=Sing] спо́соба; способы [Case=Nom, Number=Plur] спо́собы; способов [Case=Gen, Number=Plur] спо́собов
  1. [A2] приём или порядок действий, с помощью которого чего-то достигают
     - example: Мы обязательно найдём простой способ решить эту задачу.
     - frames: способ + инф.; способ чего; каким способом

### 21. уверенно

**Lexeme 1: уверенно**, ADV
- grammar: comparable True
- said: /u.ˈvʲe.rʲɪn.nə/ уве́ренно (ru-RU)
  1. [B1] спокойно и твёрдо, не сомневаясь в своих силах или словах
     - example: Девочка уверенно вышла к доске и начала отвечать.
     - opposites: неуверенно
  2. [B2] умело и точно, без ошибок и колебаний
     - example: Через месяц ты уже будешь уверенно водить машину.
     - opposites: неуверенно
- relations: adverb_of уверенный (ADJ)

### 22. особо

**Lexeme 1: особо**, ADV
- said: /ˈo.sə.bə/ о́собо (ru-RU)
  1. [B1] в большей степени, чем обычно или чем другие
     - example: Хочу особо поблагодарить всех, кто помогал нам с переездом.
     - collocations: особо отметить; особо подчеркнуть; особо важный; особо опасный; не особо
  2. [C1] отдельно, в стороне от остальных
     - example: Этот вопрос мы обсудим особо на следующем собрании.
     - collocations: держаться особо; стоять особо
- relations: adverb_of особый (ADJ)

### 23. банк

**Lexeme 1: банк**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈbank/ банк (ru-RU)
- forms: банка [Case=Gen, Number=Sing] ба́нка; банки [Case=Nom, Number=Plur] ба́нки; банков [Case=Gen, Number=Plur] ба́нков
  1. [A1] организация, которая хранит деньги, даёт кредиты и делает платежи
     - example: Завтра я пойду в банк и открою новый счёт.
     - collocations: положить деньги в банк; счёт в банке; взять кредит в банке; работать в банке; центральный банк; коммерческий банк; ограбить банк
  2. [B2] собрание данных или материалов, хранимых для дальнейшего использования
     - example: Все сведения о пациентах хранятся в общем банке данных.
     - collocations: банк данных; банк крови; банк идей
  3. [C1] общая сумма денег, на которую играют в азартной игре
     - example: После последней раздачи он неожиданно сорвал весь банк.
     - collocations: сорвать банк; держать банк
- relations: confusable банка (NOUN): the genitive singular банка is spelled like банка, a jar, but stressed the same; context tells them apart

### 24. значение

**Lexeme 1: значение**, NOUN
- grammar: gender Neut; animacy Inan; countability Both
- said: /znɐ.ˈtɕe.nʲɪ.jə/ значе́ние (ru-RU)
- forms: значения [Case=Gen, Number=Sing] значе́ния; значения [Case=Nom, Number=Plur] значе́ния; значений [Case=Gen, Number=Plur] значе́ний
  1. [A2] то, что выражает или обозначает слово, знак или жест
     - example: Посмотри в словаре значение этого незнакомого слова.
     - frames: значение чего
     - grammar: countability Count
  2. [B1] важность, роль чего-либо для кого-либо или чего-либо
     - example: Эта встреча имеет большое значение для нашей компании.
     - frames: придавать значение чему
     - collocations: иметь значение
     - grammar: countability Mass
  3. [B2] число или величина, которую получают при измерении или расчёте
     - example: Найдите значение выражения, если икс равен двум.
     - frames: значение чего
     - labels: domain mathematics
     - grammar: countability Count
- relations: derived_from значить (VERB)

### 25. мама

**Lexeme 1: мама**, NOUN
- grammar: gender Fem; animacy Anim; countability Count
- said: /ˈma.mə/ ма́ма (ru-RU)
- forms: мамы [Case=Gen, Number=Sing] ма́мы; мамы [Case=Nom, Number=Plur] ма́мы; мам [Case=Gen, Number=Plur]
  1. [A1] женщина по отношению к своим детям; мать
     - example: Моя мама работает врачом в детской больнице.
     - opposites: папа

### 26. решение

**Lexeme 1: решение**, NOUN
- grammar: gender Neut; animacy Inan; countability Count; declension 2
- said: /rʲɪ.ˈʂe.nʲɪ.jə/ реше́ние (ru-RU)
- forms: решения [Case=Gen, Number=Sing] реше́ния; решения [Case=Nom, Number=Plur] реше́ния; решений [Case=Gen, Number=Plur] реше́ний
  1. [A2] выбор того, как поступить, сделанный после размышления
     - example: После долгих споров мы приняли решение переехать в другой город.
     - frames: решение + инф.; решение о чём
     - collocations: принять решение; прийти к решению; изменить решение; окончательное решение; правильное решение; трудное решение; твёрдое решение
  2. [B2] официальный вывод суда или органа власти, обязательный для исполнения
     - example: Адвокат собирается обжаловать решение суда в высшей инстанции.
     - frames: решение о чём; решение по чему
     - collocations: решение суда; вынести решение; обжаловать решение; отменить решение; выполнять решение
  3. [B1] способ справиться с трудностью или найти ответ на задачу
     - example: Учёные пока не нашли решения этой сложной проблемы.
     - frames: решение чего
     - collocations: найти решение; решение проблемы; решение задачи; решение вопроса; простое решение; техническое решение
- relations: verbal_noun_of решить (VERB)

### 27. попробовать

**Lexeme 1: попробовать**, VERB
- grammar: aspect Perf; valency Tr; conjugation 1
- said: /pə.prɐ.ˈbo.vətʲ/ попро́бовать (ru-RU)
- forms: попробую [Tense=Fut, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] попро́бую; попробуешь [Tense=Fut, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] попро́буешь; попробуют [Tense=Fut, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] попро́буют; попробовал [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] попро́бовал; попробовала [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] попро́бовала; попробуй [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] попро́буй
  1. [A2] сделать попытку совершить какое-либо действие, не зная результата
     - example: Завтра я попробую поговорить с директором ещё раз.
     - frames: попробовать + инф.
     - collocations: попробовать свои силы; попробовать ещё раз; попробовать снова
  2. [A2] взять немного еды или питья, чтобы узнать вкус
     - example: Попробуй этот суп, мне кажется, он слишком солёный.
     - frames: попробовать что; попробовать чего
     - collocations: попробовать на вкус; дать попробовать
  3. [B1] использовать что-либо, чтобы проверить его качество или действие
     - example: Врач посоветовал мне попробовать новое лекарство от кашля.
     - frames: попробовать что
     - collocations: попробовать на прочность
- relations: aspect_partner пробовать (VERB)

### 28. автор

**Lexeme 1: автор**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; note also used of a woman; the past-tense verb may then be feminine (автор написала)
- said: /ˈaf.tər/ а́втор (ru-RU)
- forms: автора [Case=Gen, Number=Sing] а́втора; авторы [Case=Nom, Number=Plur] а́вторы; авторов [Case=Gen, Number=Plur] а́второв
  1. [A2] человек, который написал книгу, статью или создал произведение искусства
     - example: Автор этого романа живёт в небольшом городе у моря.
     - frames: автор чего
  2. [B1] человек, который придумал или предложил идею, план, проект
     - example: Автор проекта подробно рассказал о своём плане на собрании.
     - frames: автор чего

### 29. особый

**Lexeme 1: особый**, ADJ
- grammar: position Attr, mainly
- said: /ɐ.ˈso.bɨj/ осо́бый (ru-RU)
  1. [B1] не такой, как другие, отличающийся своими свойствами
     - example: У каждого старого города есть свой особый характер.
     - collocations: особый случай; особое мнение; особый статус; особый вкус; особая роль
     - opposites: обычный
  2. [B1] больший или сильнее, чем обычно
     - example: Врачи уделяют особое внимание детям с этой болезнью.
     - collocations: особое внимание; особое значение; особый интерес; с особым удовольствием; особая благодарность
  3. [B2] отдельный, предназначенный только для определённой цели или людей
     - example: Для гостей в здании есть особый вход со двора.
     - collocations: особый вход; особая комната; особый режим

### 30. мастер

**Lexeme 1: мастер**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; declension 2; note also used of women, usually with masculine agreement
- said: /ˈma.stʲɪr/ ма́стер (ru-RU)
- forms: мастера [Case=Gen, Number=Sing] ма́стера; мастера [Case=Nom, Number=Plur] мастера́; мастеров [Case=Gen, Number=Plur] мастеро́в
  1. [A2] работник, профессионально выполняющий ремонт или другую работу руками
     - example: Завтра придёт мастер и починит нашу стиральную машину.
     - frames: мастер по чему
     - collocations: вызвать мастера; мастер по ремонту; опытный мастер
  2. [B1] человек, достигший высокого умения в каком-либо деле
     - example: Мой дедушка был настоящим мастером своего дела.
     - frames: мастер чего; мастер + инф.; мастер на что
     - collocations: мастер своего дела; мастер на все руки; мастер спорта; настоящий мастер; великий мастер
  3. [B2] руководитель группы рабочих на участке завода или стройки
     - example: Каждое утро мастер цеха проверяет, все ли пришли на смену.
     - collocations: мастер цеха; старший мастер

### 31. менять

**Lexeme 1: менять**, VERB
- grammar: aspect Imp; valency Tr
- said: /mʲɪ.ˈnʲatʲ/ меня́ть (ru-RU)
- forms: меняю [Person=1, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] меня́ю; меняешь [Person=2, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] меня́ешь; меняют [Person=3, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] меня́ют; менял [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin] меня́л; меняла [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin] меня́ла; меняй [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] меня́й
  1. [A2] делать другим, не таким, каким было раньше
     - example: Мы не будем откладывать поездку и не станем менять наши планы.
     - frames: менять что
  2. [A2] убирать что-то и ставить на его место другое
     - example: Каждые полгода я меняю масло в машине.
     - frames: менять что
  3. [B1] отдавать одну вещь и получать за неё другую
     - example: В этом банке можно менять доллары на евро.
     - frames: менять что на что
- relations: aspect_partner поменять (VERB)

### 32. кредит

**Lexeme 1: кредит**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈkrʲe.dʲit/ кре́дит (ru-RU)
- forms: кредита [Case=Gen, Number=Sing] кре́дита; кредиты [Case=Nom, Number=Plur] кре́диты; кредитов [Case=Gen, Number=Plur] кре́дитов
  1. [B1] деньги или товары, которые дают в долг на определённый срок
     - example: Чтобы купить машину, им пришлось взять кредит в банке.
     - frames: кредит на что; кредит под что; кредит кому
     - collocations: взять кредит; купить в кредит; погасить кредит; выдать кредит; получить кредит; ипотечный кредит; потребительский кредит; кредит под проценты; кредит доверия
**Lexeme 2: кредит**, NOUN
- grammar: gender Masc; animacy Inan; countability Mass; declension 2; number Sing
- said: /krʲɪ.ˈdʲit/ креди́т (ru-RU)
- forms: кредита [Case=Gen, Number=Sing] креди́та
  1. [C2] правая сторона бухгалтерского счёта
     - example: Бухгалтер записал эту сумму в кредит счёта.
     - collocations: дебет и кредит
     - labels: domain finance
     - opposites: дебет

### 33. пока

**Lexeme 1: пока**, ADV
- said: /pɐ.ˈka/ пока́ (ru-RU)
  1. [A2] в настоящее время, до какого-то момента в будущем
     - example: Я пока не знаю, куда поеду летом.
     - collocations: пока что; пока ещё; пока всё
  2. [B1] тем временем, до того как произойдёт что-то другое
     - example: Обед будет через час, а ты пока отдохни.
**Lexeme 2: пока**, SCONJ
- said: /pɐ.ˈka/ пока́ (ru-RU)
  1. [A2] используется, чтобы сказать, что действия происходят в одно время
     - example: Пока я готовлю ужин, ты можешь накрыть на стол.
  2. [B1] используется, чтобы назвать момент, до которого длится действие
     - example: Подожди здесь, пока я не вернусь из магазина.
     - collocations: пока не
     - grammar: note usually with не in the clause
**Lexeme 3: пока**, INTJ
- said: /pɐ.ˈka/ пока́ (ru-RU)
  1. [A1] используется при прощании с близкими или знакомыми людьми
     - example: Ну, пока, завтра увидимся в школе!
     - collocations: ну пока; пока-пока
     - labels: formality informal, politeness familiar

### 34. отправить

**Lexeme 1: отправить**, VERB
- grammar: aspect Perf; valency Tr; conjugation 2
- said: /ɐt.ˈpra.vʲitʲ/ отпра́вить (ru-RU)
- forms: отправлю [Tense=Fut, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] отпра́влю; отправишь [Tense=Fut, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] отпра́вишь; отправят [Tense=Fut, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] отпра́вят; отправил [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] отпра́вил; отправила [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] отпра́вила; отправь [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] отпра́вь
  1. [A2] послать что-либо кому-либо или в какое-либо место
     - example: Я отправлю тебе фотографии сразу после возвращения домой.
     - frames: отправить что кому; отправить что куда
     - collocations: отправить письмо; отправить посылку; отправить сообщение; отправить по почте; отправить по электронной почте
  2. [B1] заставить или попросить кого-либо пойти или поехать куда-либо
     - example: Родители отправили сына на лето к бабушке в деревню.
     - frames: отправить кого куда; отправить кого + инф.; отправить кого за кем/чем
     - collocations: отправить в командировку; отправить в отпуск; отправить на пенсию; отправить в тюрьму; отправить на тот свет
- relations: aspect_partner отправлять (VERB)

### 35. впоследствии

**Lexeme 1: впоследствии**, ADV
- said: /fpɐ.ˈslʲet.stvʲi.i/ впосле́дствии (ru-RU)
  1. [B2] в более позднее время, через какое-то время
     - example: Эта случайная встреча впоследствии изменила всю мою жизнь.
     - labels: style literary

### 36. принять

**Lexeme 1: принять**, VERB
- grammar: aspect Perf; valency Tr; conjugation 1; irregular True
- said: /prʲi.ˈnʲatʲ/ приня́ть (ru-RU)
- forms: приму [Tense=Fut, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] приму́; примешь [Tense=Fut, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] при́мешь; примут [Tense=Fut, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] при́мут; принял [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] при́нял; приняла [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] приняла́; приняло [Tense=Past, Gender=Neut, Number=Sing, VerbForm=Fin, Mood=Ind] при́няло; приняли [Tense=Past, Number=Plur, VerbForm=Fin, Mood=Ind] при́няли; прими [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] прими́
  1. [B1] получить то, что дают, приносят или присылают
     - example: Она с благодарностью приняла подарок от коллег.
     - frames: принять что от кого
     - collocations: принять подарок; принять поздравления; принять заказ; принять звонок
  2. [B1] согласиться с чем-либо, признать что-либо подходящим
     - example: К сожалению, мы не можем принять ваши условия.
     - frames: принять что
     - collocations: принять предложение; принять приглашение; принять извинения; принять вызов; принять как должное
     - opposites: отвергнуть
  3. [B1] включить кого-либо в состав группы, организации или учебного заведения
     - example: Его приняли в университет с первой попытки.
     - frames: принять кого во что; принять кого на что
     - collocations: принять на работу; принять в члены
  4. [B1] встретить пришедших гостей или посетителей
     - example: Мы будем рады принять вас у себя в субботу.
     - frames: принять кого
     - collocations: принять гостей; тепло принять; радушно принять
  5. [B2] официально утвердить решение, закон или документ
     - example: Парламент принял новый закон об образовании.
     - frames: принять что
     - collocations: принять решение; принять закон; принять резолюцию; принять единогласно
  6. [A2] выпить лекарство или воспользоваться душем, ванной
     - example: Перед сном тебе нужно принять две таблетки.
     - frames: принять что
     - collocations: принять лекарство; принять таблетку; принять душ; принять ванну
  7. [B2] ошибочно посчитать кем-либо или чем-либо другим
     - example: В темноте я принял старый куст за человека.
     - frames: принять кого/что за кого/что
  8. [B1] начать или совершить то действие, которое названо существительным
     - example: Все студенты нашей группы приняли участие в конференции.
     - collocations: принять участие; принять меры; принять во внимание; принять форму; принять вид
- relations: aspect_partner принимать (VERB)

### 37. текст

**Lexeme 1: текст**, NOUN
- grammar: gender Masc; animacy Inan; countability Count, mainly; declension 2
- said: /ˈtʲekst/ текст (ru-RU)
- forms: текста [Case=Gen, Number=Sing] те́кста; тексты [Case=Nom, Number=Plur] те́ксты; текстов [Case=Gen, Number=Plur] те́кстов
  1. [A1] написанные или напечатанные слова, которые составляют одно целое
     - example: Прочитайте текст и ответьте на вопросы после него.
     - frames: текст чего
     - collocations: полный текст; исходный текст; набрать текст; перевести текст; написать текст; прочитать текст; текст договора
  2. [A2] слова песни или другого музыкального произведения
     - example: Он сам написал и музыку, и текст этой песни.
     - frames: текст к чему; текст чего
     - collocations: текст песни; автор текста; выучить текст

### 38. нечто

**Lexeme 1: нечто**, PRON
- grammar: invariable True; note used only as nominative or accusative; an adjective after it takes the neuter form (нечто новое)
- said: /ˈnʲeʂ.tə/ не́что (ru-RU)
  1. [B1] что-то неопределённое, точно не названное или не известное
     - example: В темноте я вдруг заметил нечто похожее на огромную птицу.
     - frames: нечто вроде чего
     - collocations: нечто среднее; нечто подобное; нечто большее; нечто особенное; нечто странное; нечто вроде
  2. [B2] что-то необыкновенное, вызывающее восхищение или сильное удивление
     - example: Вчерашний концерт был просто нечто, до сих пор под впечатлением!
     - collocations: это нечто
     - labels: formality informal
     - grammar: position Pred, mainly; note mostly in это было нечто

### 39. двадцать

**Lexeme 1: двадцать**, NUM
- grammar: note declines like a 3rd-declension noun, with the stress on the ending in the oblique cases
- said: /ˈdva.tsətʲ/ два́дцать (ru-RU)
- forms: двадцати [Case=Gen,Dat,Loc] двадцати́; двадцатью [Case=Ins] двадцатью́
  1. [A1] число и количество, на единицу больше девятнадцати
     - example: В нашем классе учатся двадцать человек.
     - frames: двадцать кого/чего
     - collocations: двадцать лет; двадцать минут

### 40. интервью

**Lexeme 1: интервью**, NOUN
- grammar: gender Neut; animacy Inan; countability Count; invariable True
- said: /in.tɨr.ˈvʲju/ интервью́ (ru-RU)
  1. [B1] беседа журналиста с человеком, которую потом публикуют или показывают
     - example: Знаменитый актёр редко даёт интервью после выхода нового фильма.
     - frames: интервью с кем; интервью кому; интервью о чём
     - collocations: дать интервью; взять интервью; брать интервью; давать интервью; эксклюзивное интервью; опубликовать интервью; телевизионное интервью

### 41. найти

**Lexeme 1: найти**, VERB
- grammar: aspect Perf; valency Both; irregular True
- said: /nɐj.ˈtʲi/ найти́ (ru-RU)
- forms: найду [Person=1, Number=Sing, Tense=Fut, Mood=Ind, VerbForm=Fin] найду́; найдёшь [Person=2, Number=Sing, Tense=Fut, Mood=Ind, VerbForm=Fin] найдёшь; найдут [Person=3, Number=Plur, Tense=Fut, Mood=Ind, VerbForm=Fin] найду́т; нашёл [Gender=Masc, Number=Sing, Tense=Past, VerbForm=Fin] нашёл; нашла [Gender=Fem, Number=Sing, Tense=Past, VerbForm=Fin] нашла́; найди [Person=2, Number=Sing, Mood=Imp, VerbForm=Fin] найди́
  1. [A1] обнаружить кого-то или что-то, после поисков или случайно
     - example: Я наконец нашёл свои ключи под диваном.
     - frames: найти кого/что; найти где
     - grammar: valency Tr
     - opposites: потерять
  2. [B2] прийти к выводу, что кто-то или что-то таково
     - example: Врач нашёл его состояние вполне удовлетворительным.
     - frames: найти кого/что каким; найти, что ...
     - labels: style literary
     - grammar: valency Tr
  3. [B2] неожиданно охватить кого-то, о чувстве или настроении
     - example: Не понимаю, что на меня вчера нашло.
     - frames: найти на кого
     - labels: formality informal
     - grammar: valency Intr; note usually with что as subject: что на тебя нашло
- relations: aspect_partner находить (VERB)

### 42. мать

**Lexeme 1: мать**, NOUN
- grammar: gender Fem; animacy Anim; countability Count; declension 3; irregular True; note stem матер- in all forms except the nominative and accusative singular
- said: /ˈmatʲ/ мать (ru-RU)
- forms: матери [Case=Gen, Number=Sing] ма́тери; матери [Case=Nom, Number=Plur] ма́тери; матерей [Case=Gen, Number=Plur] матере́й; матерью [Case=Ins, Number=Sing] ма́терью; матерям [Case=Dat, Number=Plur] матеря́м
  1. [A1] женщина по отношению к детям, которых она родила или вырастила
     - example: Мать каждый вечер читала детям сказки перед сном.
  2. [C1] обращение к монахине или её звание в монастыре
     - example: Мать игуменья вышла встречать гостей к воротам монастыря.
     - labels: domain religion
  3. [B2] простое дружеское обращение к знакомой женщине, часто к жене
     - example: Ну что, мать, пойдём домой, уже совсем поздно.
     - labels: formality informal, politeness familiar

### 43. вполне

**Lexeme 1: вполне**, ADV
- said: /fpɐl.ˈnʲe/ вполне́ (ru-RU)
  1. [B1] целиком, в полной мере, без всяких оговорок
     - example: Я вполне согласен с вашим мнением по этому вопросу.
     - collocations: вполне согласен; вполне понятно; вполне доволен; вполне заслуживать; вполне понимать
  2. [B1] в достаточной степени, так, как нужно
     - example: Для начинающего художника эта картина вполне хороша.
     - collocations: вполне возможно; вполне может быть; вполне достаточно; вполне прилично; вполне нормальный
- relations: derived_from полный (ADJ)

### 44. ехать

**Lexeme 1: ехать**, VERB
- grammar: aspect Imp; valency Intr; conjugation 1; irregular True; note unidirectional verb of motion: one trip in one direction
- said: /ˈje.xətʲ/ е́хать (ru-RU)
- forms: еду [Tense=Pres, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] е́ду; едешь [Tense=Pres, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] е́дешь; едут [Tense=Pres, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] е́дут; ехал [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] е́хал; ехала [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] е́хала; поезжай [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] поезжа́й (the standard imperative); езжай [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] езжа́й (colloquial)
  1. [A1] передвигаться на машине, поезде, лошади или другом транспорте
     - example: Каждое утро я еду на работу на метро.
     - frames: ехать на чём; ехать куда; ехать откуда
     - collocations: ехать на машине; ехать на поезде; ехать на метро; ехать на автобусе; ехать на велосипеде; ехать верхом; ехать домой; ехать на работу
  2. [A2] двигаться по дороге или по рельсам (о транспорте)
     - example: Смотри, по мосту медленно едет длинный товарный поезд.
     - collocations: машина едет; поезд едет; ехать быстро; ехать медленно
  3. [A1] отправляться в поездку куда-либо, покидая своё место
     - example: Летом мы едем к бабушке в деревню.
     - frames: ехать куда; ехать к кому; ехать + инф.
     - collocations: ехать в отпуск; ехать в командировку; ехать за границу; ехать на юг
- relations: aspect_partner поехать (VERB); confusable ездить (VERB): ездить is the multidirectional verb: trips there and back, repeated or habitual travel; ехать is one trip in one direction

### 45. любовь

**Lexeme 1: любовь**, NOUN
- grammar: gender Fem; animacy Inan; declension 3; number Sing, mainly
- said: /lʲu.ˈbofʲ/ любо́вь (ru-RU)
- forms: любви [Case=Gen, Number=Sing] любви́; любовью [Case=Ins, Number=Sing] любо́вью; любви [Case=Nom, Number=Plur] любви́ (rare, mostly in poetry); любвей [Case=Gen, Number=Plur] любве́й (rare, mostly in poetry)
  1. [A2] сильное чувство нежности и влечения к другому человеку
     - example: Они встретились в юности, и это была любовь с первого взгляда.
     - frames: любовь к кому
     - collocations: любовь с первого взгляда; признаться в любви; объясниться в любви; заниматься любовью; первая любовь; взаимная любовь; безответная любовь; несчастная любовь
     - opposites: ненависть
  2. [B1] глубокая сердечная привязанность к кому-либо или чему-либо близкому
     - example: Материнская любовь помогала ему в самые трудные годы.
     - frames: любовь к кому/чему
     - collocations: материнская любовь; любовь к родине; любовь к детям; любовь к ближнему
     - opposites: ненависть
  3. [B1] сильный постоянный интерес к какому-либо занятию или предмету
     - example: Любовь к чтению появилась у меня ещё в детстве.
     - frames: любовь к чему
     - collocations: любовь к музыке; любовь к чтению; любовь к природе
  4. [B2] человек, к которому испытывают сильное нежное чувство
     - example: Через много лет он случайно встретил свою первую любовь.
     - collocations: первая любовь
**Lexeme 2: Любовь**, PROPN
- grammar: gender Fem; animacy Anim; declension 3
- said: /lʲu.ˈbofʲ/ Любо́вь (ru-RU)
- forms: Любови [Case=Gen, Number=Sing] Любо́ви; Любовью [Case=Ins, Number=Sing] Любо́вью
  1. [B1] русское женское личное имя
     - example: Мою бабушку звали Любовь, но все называли её иначе.
     - grammar: note keeps the vowel о in all cases, unlike the common noun

### 46. мимо

**Lexeme 1: мимо**, ADP
- said: /ˈmʲi.mə/ ми́мо (ru-RU)
  1. [A2] двигаясь около кого-либо или чего-либо и не останавливаясь
     - example: Каждое утро я хожу на работу мимо старой церкви.
     - frames: мимо кого/чего
     - collocations: пропустить мимо ушей; мимо цели; пройти мимо; проехать мимо
**Lexeme 2: мимо**, ADV
- said: /ˈmʲi.mə/ ми́мо (ru-RU)
  1. [A2] около кого-либо или чего-либо, не останавливаясь
     - example: Автобус проехал мимо, даже не притормозив у остановки.
     - collocations: проходить мимо; проехать мимо; пролететь мимо; пробежать мимо
  2. [B1] не попадая в цель
     - example: Он выстрелил дважды, но оба раза мимо.
     - collocations: стрелять мимо; бить мимо

### 47. полностью

**Lexeme 1: полностью**, ADV
- said: /ˈpol.nəsʲ.tʲju/ по́лностью (ru-RU)
  1. [B1] целиком, во всём объёме, без всякого остатка
     - example: Я полностью согласен с тем, что ты сейчас сказал.
     - collocations: полностью согласен; полностью уверен; полностью готов; полностью разделять мнение; полностью восстановиться; полностью изменить; полностью погасить долг
     - opposites: частично

### 48. статус

**Lexeme 1: статус**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈsta.tus/ ста́тус (ru-RU)
- forms: статуса [Case=Gen, Number=Sing] ста́туса; статусы [Case=Nom, Number=Plur] ста́тусы; статусов [Case=Gen, Number=Plur] ста́тусов
  1. [B2] положение человека, организации или территории, определённое законом или правилами
     - example: После долгих проверок семья наконец получила статус беженцев.
     - frames: статус кого/чего
     - collocations: правовой статус; получить статус; присвоить статус; лишить статуса; особый статус; официальный статус
  2. [B2] положение человека в обществе, его вес и уважение среди других
     - example: Для многих дорогая машина — прежде всего показатель высокого статуса.
     - collocations: социальный статус; высокий статус; повысить статус; подчеркнуть статус
  3. [B1] состояние, в котором что-либо находится в данный момент
     - example: Статус вашего заказа можно проверить в личном кабинете.
     - frames: статус чего
     - collocations: статус заказа; статус заявки; изменить статус

### 49. дать

**Lexeme 1: дать**, VERB
- grammar: aspect Perf; valency Tr; irregular True
- said: /ˈdatʲ/ дать (ru-RU)
- forms: дам [Person=1, Number=Sing, Tense=Fut, VerbForm=Fin]; дашь [Person=2, Number=Sing, Tense=Fut, VerbForm=Fin]; дадут [Person=3, Number=Plur, Tense=Fut, VerbForm=Fin] даду́т; дал [Gender=Masc, Number=Sing, Tense=Past, VerbForm=Fin]; дала [Gender=Fem, Number=Sing, Tense=Past, VerbForm=Fin] дала́; дай [Mood=Imp, Number=Sing, Person=2, VerbForm=Fin]
  1. [A1] вручить кому-то вещь, чтобы он мог её взять или иметь
     - example: Дай мне, пожалуйста, ручку, я забыл свою дома.
     - frames: дать кому что
  2. [A2] позволить кому-то что-то сделать, не помешать
     - example: Мама не дала нам досмотреть фильм до конца.
     - frames: дать кому + инф.
  3. [A2] предоставить кому-то что-либо, обеспечить чем-либо
     - example: Начальник обещал дать мне отпуск в августе.
     - frames: дать кому что
  4. [B1] принести как результат, произвести
     - example: Новый метод лечения дал хорошие результаты.
  5. [B1] совершить действие, которое названо существительным
     - example: Оркестр даст концерт в субботу вечером.
     - grammar: note with nouns of action: дать концерт, дать ответ, дать сигнал
  6. [B2] ударить кого-то
     - example: Ещё одно слово, и я дам тебе по шее!
     - frames: дать кому по чему
     - labels: formality informal
- relations: aspect_partner давать (VERB)

### 50. куда

**Lexeme 1: куда**, ADV
- said: /ku.ˈda/ куда́ (ru-RU)
  1. [A1] в какое место, в каком направлении
     - example: Скажи, куда ты так спешишь в такую рань?
     - collocations: куда деваться; куда идти
  2. [A2] в то место, в которое направлено движение
     - example: Это тот самый город, куда мы ездили прошлым летом.
     - collocations: куда угодно; куда попало; куда глаза глядят
     - grammar: note introduces a relative clause
  3. [B1] в какое-нибудь место, точно не названное
     - example: Если куда поедешь на выходных, обязательно предупреди меня.
     - labels: formality informal
  4. [B2] для чего, с какой целью
     - example: Куда тебе столько обуви, если шкаф уже полон?
     - frames: куда кому что
     - labels: formality informal
**Lexeme 2: куда**, PART
- said: /ku.ˈda/ куда́ (ru-RU)
  1. [B1] используется, чтобы сделать сравнение намного сильнее
     - example: В новой квартире нам куда просторнее, чем в старой.
     - collocations: куда лучше; куда больше; куда проще
     - labels: formality informal
     - grammar: note before a comparative
  2. [B2] используется, чтобы выразить сомнение в чьих-либо возможностях или отрицание
     - example: Он мечтает пробежать марафон, но куда ему с больным коленом.
     - frames: куда кому + инф.
     - collocations: куда там; куда уж; куда тебе
     - labels: formality informal

### 51. храм

**Lexeme 1: храм**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈxram/ храм (ru-RU)
- forms: храма [Case=Gen, Number=Sing] хра́ма; храмы [Case=Nom, Number=Plur] хра́мы; храмов [Case=Gen, Number=Plur] хра́мов
  1. [A2] здание, где верующие молятся и проводят богослужения
     - example: Каждое воскресенье бабушка ходит в старый храм на площади.
     - frames: храм чего
     - collocations: ходить в храм; построить храм; освятить храм; православный храм; древний храм; буддийский храм; служба в храме
     - labels: domain religion
  2. [C1] место, где с особым уважением служат чему-то высокому
     - example: Для молодого учёного университет был настоящим храмом науки.
     - frames: храм чего
     - collocations: храм науки; храм искусства
     - labels: figurative True, style elevated

### 52. ощущение

**Lexeme 1: ощущение**, NOUN
- grammar: gender Neut; animacy Inan; countability Count; declension 2
- said: /ɐ.ɕːu.ˈɕːe.nʲi.jə/ ощуще́ние (ru-RU)
- forms: ощущения [Case=Gen, Number=Sing] ощуще́ния; ощущения [Case=Nom, Number=Plur] ощуще́ния; ощущений [Case=Gen, Number=Plur] ощуще́ний
  1. [B1] то, что человек чувствует телом, когда что-то действует на него
     - example: После долгой прогулки в ногах появилось неприятное ощущение тяжести.
     - frames: ощущение чего
     - collocations: болевые ощущения; ощущение боли; ощущение холода; приятное ощущение; неприятное ощущение; испытывать ощущение
  2. [B1] внутреннее чувство или впечатление, которое возникает у человека
     - example: У меня есть ощущение, что мы здесь уже были.
     - frames: ощущение чего; ощущение, что ...; ощущение от чего
     - collocations: острые ощущения; создаётся ощущение; возникает ощущение; странное ощущение; ощущение счастья; ощущение свободы
- relations: verbal_noun_of ощущать (VERB)

### 53. заставить

**Lexeme 1: заставить**, VERB
- grammar: aspect Perf; valency Tr; conjugation 2
- said: /zɐ.ˈsta.vʲitʲ/ заста́вить (ru-RU)
- forms: заставлю [Tense=Fut, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] заста́влю; заставишь [Tense=Fut, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] заста́вишь; заставят [Tense=Fut, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] заста́вят; заставил [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] заста́вил; заставила [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] заста́вила; заставь [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] заста́вь
  1. [B1] добиться, чтобы кто-либо сделал что-либо, часто против его желания
     - example: Плохая погода заставила нас вернуться домой раньше времени.
     - frames: заставить кого + инф.
     - collocations: заставить себя; заставить ждать; заставить задуматься; заставить замолчать; силой заставить
  2. [B2] поставить много вещей, заняв ими всё свободное место
     - example: Соседи заставили весь коридор старыми шкафами и коробками.
     - frames: заставить что чем
- relations: aspect_partner заставлять (VERB)

### 54. вблизи

**Lexeme 1: вблизи**, ADP
- said: /vblʲi.ˈzʲi/ вблизи́ (ru-RU)
  1. [B1] на небольшом расстоянии от кого-либо или чего-либо
     - example: Летом мы снимаем маленький домик вблизи моря.
     - frames: вблизи кого/чего; вблизи от кого/чего
     - collocations: вблизи границы; вблизи города
**Lexeme 2: вблизи**, ADV
- said: /vblʲi.ˈzʲi/ вблизи́ (ru-RU)
  1. [B1] на небольшом расстоянии, недалеко
     - example: Вблизи не было видно ни одного дома.
     - opposites: вдали
  2. [B2] с небольшого расстояния
     - example: Вблизи эта картина выглядит совсем иначе.
     - collocations: рассмотреть вблизи; увидеть вблизи
     - opposites: издали

### 55. повышение

**Lexeme 1: повышение**, NOUN
- grammar: gender Neut; animacy Inan; declension 2
- said: /pə.vɨ.ˈʂe.nʲɪ.jə/ повыше́ние (ru-RU)
- forms: повышения [Case=Gen, Number=Sing] повыше́ния; повышения [Case=Nom, Number=Plur] повыше́ния; повышений [Case=Gen, Number=Plur] повыше́ний
  1. [B1] увеличение уровня, размера, количества или силы чего-либо
     - example: Резкое повышение цен на продукты беспокоит многих жителей города.
     - frames: повышение чего; повышение чего на сколько
     - collocations: повышение цен; повышение зарплаты; повышение температуры; повышение квалификации; повышение качества; повышение уровня жизни; резкое повышение
     - grammar: countability Mass, mainly
     - opposites: понижение
  2. [B1] перевод работника на более важную должность или в старшее звание
     - example: Через год упорной работы она наконец получила повышение.
     - collocations: получить повышение; повышение по службе; повышение в должности; дать повышение
     - grammar: countability Count
     - opposites: понижение
- relations: verbal_noun_of повысить (VERB)

### 56. вызвать

**Lexeme 1: вызвать**, VERB
- grammar: aspect Perf; valency Tr; conjugation 1
- said: /ˈvɨ.zvətʲ/ вы́звать (ru-RU)
- forms: вызову [Tense=Fut, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] вы́зову; вызовешь [Tense=Fut, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] вы́зовешь; вызовут [Tense=Fut, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] вы́зовут; вызвал [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] вы́звал; вызвала [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] вы́звала; вызови [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] вы́зови
  1. [A2] попросить или потребовать, чтобы кто-либо пришёл или приехал
     - example: Соседке стало плохо, и мы сразу вызвали скорую помощь.
     - frames: вызвать кого/что; вызвать кого куда
     - collocations: вызвать врача; вызвать скорую помощь; вызвать такси; вызвать полицию; вызвать к доске; вызвать в суд
  2. [B1] стать причиной чего-либо, привести к чему-либо
     - example: Новый закон вызвал горячие споры в обществе.
     - frames: вызвать что; вызвать что у кого
     - collocations: вызвать интерес; вызвать подозрение; вызвать споры; вызвать смех; вызвать недовольство; вызвать сомнения; вызвать удивление
  3. [B2] предложить кому-либо вступить в борьбу или состязание
     - example: Обиженный офицер вызвал соперника на дуэль.
     - frames: вызвать кого на что
     - collocations: вызвать на дуэль; вызвать на бой; вызвать на соревнование
- relations: aspect_partner вызывать (VERB); derived_from звать (VERB)

### 57. дневник

**Lexeme 1: дневник**, NOUN
- grammar: gender Masc; animacy Inan; countability Count
- said: /dnʲɪv.ˈnʲik/ дневни́к (ru-RU)
- forms: дневника [Case=Gen, Number=Sing] дневника́; дневники [Case=Nom, Number=Plur] дневники́; дневников [Case=Gen, Number=Plur] дневнико́в
  1. [A2] тетрадь, куда человек регулярно записывает события своей жизни и мысли
     - example: Я веду дневник с двенадцати лет и пишу туда каждый вечер.
     - collocations: вести дневник
  2. [A2] тетрадь школьника для записи домашних заданий и оценок
     - example: Учительница поставила мне в дневник пятёрку за диктант.
- relations: derived_from день (NOUN)

### 58. скорость

**Lexeme 1: скорость**, NOUN
- grammar: gender Fem; animacy Inan; declension 3
- said: /ˈsko.rəsʲtʲ/ ско́рость (ru-RU)
- forms: скорости [Case=Gen, Number=Sing] ско́рости; скорости [Case=Nom, Number=Plur] ско́рости; скоростей [Case=Gen, Number=Plur] скоросте́й
  1. [A2] быстрота, с которой что-либо движется или происходит
     - example: На этой дороге машины едут с очень большой скоростью.
     - frames: скорость чего
     - collocations: на большой скорости; с огромной скоростью; набрать скорость; развить скорость; сбавить скорость; превысить скорость; скорость света; максимальная скорость; высокая скорость
     - grammar: countability Mass, mainly
  2. [B2] одна из передач в механизме автомобиля, велосипеда или станка
     - example: На подъёме водитель переключил машину на вторую скорость.
     - collocations: переключить скорость; коробка скоростей; на первой скорости; велосипед со скоростями
     - grammar: countability Count
- relations: abstract_noun_of скорый (ADJ)

### 59. вдоль

**Lexeme 1: вдоль**, ADP
- said: /ˈvdolʲ/ вдоль (ru-RU)
  1. [B1] по направлению длины чего-либо, по краю или рядом с ним
     - example: Мы долго гуляли вдоль реки и говорили о жизни.
     - frames: вдоль чего
     - opposites: поперёк
**Lexeme 2: вдоль**, ADV
- said: /ˈvdolʲ/ вдоль (ru-RU)
  1. [B2] в направлении длины, а не ширины
     - example: Сначала разрежьте огурец вдоль, а потом на мелкие кусочки.
     - opposites: поперёк

### 60. обед

**Lexeme 1: обед**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ɐ.ˈbʲet/ обе́д (ru-RU)
- forms: обеда [Case=Gen, Number=Sing] обе́да; обеды [Case=Nom, Number=Plur] обе́ды; обедов [Case=Gen, Number=Plur] обе́дов
  1. [A1] еда днём, обычно самая сытная за день
     - example: Сегодня на обед у нас суп и котлеты с картошкой.
     - collocations: готовить обед; на обед; пригласить на обед; обед из трёх блюд; комплексный обед; званый обед; сытный обед
  2. [A2] время дневной еды, перерыв в работе в середине дня
     - example: Магазин закрывается на обед с часу до двух.
     - collocations: после обеда; до обеда; в обед; перерыв на обед; уйти на обед; закрыто на обед
