# Russian: entries to check (1 of 1)

These are entries of a learner's dictionary of Russian, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of Russian.

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

Do not report: the order of senses, a CEFR level off by one step, how syllables are divided, a
definition you would have worded differently when the given one is true, or anything about style.

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

### 1. вообще

**Lexeme 1: вообще**, ADV
- said: /vɐ.ɐp.ˈɕːe/ вообще́ (ru-RU)
  1. [A2] совсем, совершенно, нисколько
     - example: Я вообще не понимаю, о чём ты сейчас говоришь.
     - collocations: вообще не; вообще ничего; вообще никогда; вообще никто
     - grammar: note usually with a negation
  2. [B1] всегда, при любых обстоятельствах, как правило
     - example: Моя сестра вообще очень спокойный и добрый человек.
  3. [B2] в целом, не касаясь отдельных случаев и подробностей
     - example: Лектор говорил не о нашем городе, а об экологии вообще.
     - grammar: note often after the noun it refers to
  4. [B1] используется, чтобы подвести итог сказанному или перейти к другой мысли
     - example: А вообще, давай лучше встретимся завтра после работы.
     - collocations: а вообще; вообще говоря
     - labels: formality informal
     - grammar: note used as a parenthetical word, set off by commas

### 2. есть

**Lexeme 1: есть**, VERB
- grammar: aspect Imp; valency Both; irregular True; note irregular conjugation: ем, ешь, ест, едим, едите, едят
- said: /ˈjesʲtʲ/ есть (ru-RU)
- forms: ем [Tense=Pres, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind]; ешь [Tense=Pres, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind]; едят [Tense=Pres, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] едя́т; ел [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind]; ела [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] е́ла; ешь [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin]
  1. [A1] принимать пищу, жевать и глотать её
     - example: По утрам я обычно ем кашу с фруктами.
     - frames: есть что; есть чем
     - collocations: хотеть есть; есть с аппетитом; есть ложкой; есть руками; есть досыта; есть мясо
  2. [B2] постепенно разрушать что-либо или вызывать неприятное жжение
     - example: Едкий дым от костра ест глаза.
     - frames: есть что
     - collocations: дым ест глаза; ржавчина ест металл
     - grammar: valency Tr
- relations: aspect_partner съесть (VERB)
**Lexeme 2: есть**, INTJ
- said: /ˈjesʲtʲ/ есть (ru-RU)
  1. [B2] употребляется как ответ на приказ в значении «слушаюсь»
     - example: Матрос коротко ответил: «Есть!» — и побежал выполнять приказ.
     - collocations: есть, товарищ командир
     - labels: domain military
  2. [B1] употребляется для выражения радости, когда что-то удалось
     - example: Есть, наконец-то мы забили этот гол!
     - labels: formality informal

### 3. тут

**Lexeme 1: тут**, ADV
- said: /ˈtut/ тут (ru-RU)
  1. [A1] в этом месте, недалеко от говорящего
     - example: Подожди меня тут, я скоро вернусь.
     - collocations: вот тут; тут и там; тут рядом; тут как тут
     - opposites: там
  2. [A2] в этот момент, сразу после того, что произошло
     - example: Мы уже собирались уходить, и тут зазвонил телефон.
     - collocations: тут же; и тут
  3. [B1] в этом деле, в данной ситуации
     - example: Боюсь, тут уже ничего нельзя изменить.
     - collocations: при чём тут; тут ничего не поделаешь

### 4. даже

**Lexeme 1: даже**, PART
- said: /ˈda.ʐɨ/ да́же (ru-RU)
  1. [A2] употребляется, чтобы выделить что-либо неожиданное, крайнее или особенно важное
     - example: Он так устал, что даже не снял ботинки.
     - collocations: даже если; даже не; и даже; или даже; даже больше

### 5. возле

**Lexeme 1: возле**, ADP
- said: /ˈvoz.lʲɪ/ во́зле (ru-RU)
  1. [A2] на небольшом расстоянии от кого-либо или чего-либо
     - example: Давай встретимся завтра возле входа в метро.
     - frames: возле кого/чего
**Lexeme 2: возле**, ADV
- said: /ˈvoz.lʲɪ/ во́зле (ru-RU)
  1. [B2] на небольшом расстоянии от указанного места
     - example: Я сел на скамейку, а пёс устроился возле.

### 6. чтобы

**Lexeme 1: чтобы**, SCONJ
- said: /ˈʂto.bɨ/ что́бы (ru-RU, чтобы), /ˈʂtop/ чтоб (ru-RU, чтоб)
- spellings: чтобы; чтоб
  1. [A2] используется, чтобы присоединить часть предложения, которая называет цель действия
     - example: Я встал пораньше, чтобы успеть на первый автобус.
     - frames: чтобы + инф.; чтобы ...; для того чтобы + инф.; с тем чтобы + инф.
     - grammar: note with an infinitive when the subject is the same, otherwise with a verb in the past tense form
  2. [B1] используется, чтобы присоединить придаточное после слов желания, просьбы, необходимости или сомнения
     - example: Мама хочет, чтобы мы приехали к ней на выходные.
     - frames: хотеть, чтобы ...; просить кого, чтобы ...; нужно, чтобы ...; не думать, чтобы ...
     - grammar: note the verb after it is in the past tense form
  3. [B2] используется, чтобы присоединить часть предложения, которая называет возможное следствие
     - example: Сегодня слишком холодно, чтобы купаться в реке.
     - frames: слишком какой, чтобы + инф.; достаточно, чтобы + инф.
- relations: confusable что (PRON): чтобы is one word, but the pronoun что with the particle бы is written apart: что бы ты ни делал
**Lexeme 2: чтобы**, PART
- said: /ˈʂto.bɨ/ что́бы (ru-RU, чтобы), /ˈʂtop/ чтоб (ru-RU, чтоб)
- spellings: чтобы; чтоб
  1. [B2] используется для выражения настойчивого пожелания, требования или предостережения
     - example: Чтобы завтра к восьми часам все были на месте!
     - labels: formality informal
     - grammar: note the verb after it is in the past tense form

### 7. к

**Lexeme 1: к**, ADP
- grammar: note governs the dative; ко before some consonant clusters: ко мне, ко всем, ко второму
- said: /ˈk/ к (ru-RU, к), /ˈko/ ко (ru-RU, ко)
- spellings: к; ко
  1. [A1] указывает на направление движения в сторону кого-либо или чего-либо
     - example: Мальчик подошёл к окну и открыл его настежь.
     - frames: к кому/чему
     - collocations: лицом к лицу; ближе к делу
  2. [A2] указывает на время, не позже которого что-то происходит
     - example: Эту работу нужно закончить к пятнице.
     - frames: к чему
     - collocations: к вечеру; к утру; к концу года
  3. [A2] указывает на того, на кого направлены чувство, отношение или действие
     - example: Я всегда чувствовал большое уважение к своим учителям.
     - frames: к кому/чему
     - collocations: обратиться к врачу; любовь к родине; интерес к музыке
  4. [B1] указывает на то, что добавляется или для чего что-либо предназначено
     - example: Бабушка испекла для нас вкусный пирог к чаю.
     - frames: к чему
     - collocations: подарок к празднику; к тому же

### 8. они

**Lexeme 1: они**, PRON
- grammar: note third person plural; after a preposition the forms begin with н-: у них, к ним, с ними
- said: /ɐ.ˈnʲi/ они́ (ru-RU)
- forms: их [Case=Gen,Acc, Number=Plur, Person=3]; них [Case=Gen,Acc,Loc, Number=Plur, Person=3] (after a preposition: у них, на них, о них); им [Case=Dat, Number=Plur, Person=3]; ним [Case=Dat, Number=Plur, Person=3] (after a preposition: к ним); ими [Case=Ins, Number=Plur, Person=3] и́ми; ними [Case=Ins, Number=Plur, Person=3] ни́ми (after a preposition: с ними)
  1. [A1] используется для указания на людей или предметы, о которых уже шла речь
     - example: Друзья позвонили вчера, и завтра они приедут к нам в гости.

### 9. фильм

**Lexeme 1: фильм**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈfʲilʲm/ фильм (ru-RU)
- forms: фильма [Case=Gen, Number=Sing] фи́льма; фильмы [Case=Nom, Number=Plur] фи́льмы; фильмов [Case=Gen, Number=Plur] фи́льмов
  1. [A1] произведение, снятое на камеру для показа в кинотеатре или по телевизору
     - example: Сегодня вечером мы идём смотреть новый фильм о космосе.
     - frames: фильм о ком/о чём; фильм про кого/что
     - collocations: снять фильм; смотреть фильм; художественный фильм; документальный фильм; фильм ужасов; фильм вышел на экраны; показывать фильм; мультипликационный фильм; сниматься в фильме

### 10. сторона

**Lexeme 1: сторона**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 1
- said: /stə.rɐ.ˈna/ сторона́ (ru-RU)
- forms: стороны [Case=Gen, Number=Sing] стороны́; стороны [Case=Nom, Number=Plur] сто́роны; сторон [Case=Gen, Number=Plur] сторо́н; сторону [Case=Acc, Number=Sing] сто́рону; сторонам [Case=Dat, Number=Plur] сторона́м
  1. [A2] направление или место относительно кого-либо или чего-либо
     - example: Мальчик внимательно посмотрел по сторонам и перешёл улицу.
     - frames: в сторону кого/чего; со стороны кого/чего
     - collocations: смотреть по сторонам; отойти в сторону; в разные стороны; в другую сторону; по обе стороны; с левой стороны; с правой стороны
  2. [B1] одна из поверхностей или боковых частей предмета
     - example: Напишите свой ответ на обратной стороне листа.
     - frames: сторона чего
     - collocations: обратная сторона; лицевая сторона; солнечная сторона; сторона улицы
  3. [B1] участник спора, договора, переговоров или борьбы
     - example: После долгих переговоров обе стороны наконец подписали договор.
     - frames: на стороне кого; сторона чего
     - collocations: встать на сторону; перейти на сторону; принять сторону; занять сторону; обе стороны; стороны договорились
  4. [B1] отдельная черта или особенность чего-либо
     - example: У этого плана есть и положительные, и отрицательные стороны.
     - frames: сторона чего
     - collocations: с одной стороны; с другой стороны; сильные стороны; слабые стороны; положительная сторона; оборотная сторона медали
  5. [B2] линия родства по отцу или по матери
     - example: Со стороны матери у меня много родственников в деревне.
     - frames: со стороны кого
  6. [C1] край, местность или страна
     - example: Он часто вспоминал родную сторону, живя в большом городе.
     - collocations: родная сторона; чужая сторона
     - labels: style poetic

### 11. против

**Lexeme 1: против**, ADP
- said: /ˈpro.tʲɪf/ про́тив (ru-RU)
  1. [A2] выражая борьбу, несогласие или защиту от кого-либо или чего-либо
     - example: Жители района выступили против строительства нового завода.
     - frames: против кого/чего; против того, что ...; против того, чтобы ...
     - collocations: бороться против; выступать против; голосовать против; иметь что-нибудь против; средство против; лекарство против кашля; преступление против человечности
     - opposites: за
  2. [B1] не так, как требует или хочет кто-либо или что-либо
     - example: Иногда приходится делать что-то против собственного желания.
     - frames: против чего
     - collocations: против воли; против правил; против закона; против желания
  3. [B1] в направлении, обратном движению чего-либо
     - example: Плыть против течения было очень трудно даже опытному пловцу.
     - frames: против чего
     - collocations: против ветра; против течения; против часовой стрелки; против света
  4. [B2] на месте, обращённом лицом к кому-либо или чему-либо
     - example: Мы сели против окна и долго смотрели на море.
     - frames: против кого/чего
     - collocations: сидеть против; стоять против
  5. [C1] по сравнению с чем-либо, в сравнении
     - example: Цены выросли на десять процентов против прошлого года.
     - frames: против чего
     - labels: formality formal
**Lexeme 2: против**, ADV
- grammar: position Pred, mainly
- said: /ˈpro.tʲɪf/ про́тив (ru-RU)
  1. [A2] не соглашаясь, возражая или подавая голос несогласия
     - example: Если никто не против, мы начнём собрание прямо сейчас.
     - frames: против + инф.
     - collocations: я не против; кто против; голосовать против; ничего не иметь против; быть против
     - opposites: за

### 12. язык

**Lexeme 1: язык**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /jɪ.ˈzɨk/ язы́к (ru-RU)
- forms: языка [Case=Gen, Number=Sing] языка́; языки [Case=Nom, Number=Plur] языки́; языков [Case=Gen, Number=Plur] языко́в
  1. [A1] система слов и правил, которой люди пользуются для общения
     - example: Моя сестра свободно говорит на трёх иностранных языках.
     - collocations: родной язык; иностранный язык; говорить на языке; изучать язык; владеть языком; государственный язык; перевести с языка; найти общий язык
  2. [A2] подвижный орган во рту, которым чувствуют вкус и говорят
     - example: Врач попросил меня открыть рот и показать язык.
     - collocations: показать язык; прикусить язык; обжечь язык; высунуть язык
  3. [B2] манера и средства выражения мыслей в речи или тексте
     - example: Эта книга написана простым и понятным языком.
     - collocations: простым языком; литературный язык; разговорный язык; живой язык
  4. [B1] система знаков, с помощью которой передают сведения
     - example: Мой брат пишет программы на нескольких языках программирования.
     - collocations: язык жестов; язык программирования; язык тела
  5. [C1] что-либо вытянутое и подвижное, по форме похожее на орган во рту
     - example: Из разбитого окна вырывались яркие языки пламени.
     - collocations: языки пламени; язык колокола
**Lexeme 2: язык**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; declension 2
- said: /jɪ.ˈzɨk/ язы́к (ru-RU)
- forms: языка [Case=Gen,Acc, Number=Sing] языка́; языки [Case=Nom, Number=Plur] языки́; языков [Case=Gen,Acc, Number=Plur] языко́в
  1. [C1] пленный, которого захватили, чтобы получить от него сведения
     - example: Разведчики ночью взяли языка у самой линии фронта.
     - collocations: взять языка; добыть языка
     - labels: domain military, formality informal

### 13. теперь

**Lexeme 1: теперь**, ADV
- said: /tʲɪ.ˈpʲerʲ/ тепе́рь (ru-RU)
  1. [A1] в настоящее время, в отличие от того, что было раньше
     - example: Раньше я жил в деревне, а теперь живу в большом городе.
     - collocations: только теперь; теперь уже
  2. [A2] после того, что уже произошло или было сказано
     - example: Первый вопрос мы обсудили, теперь перейдём ко второму.
     - collocations: а теперь

### 14. страна

**Lexeme 1: страна**, NOUN
- grammar: gender Fem; animacy Inan; countability Count; declension 1
- said: /strɐ.ˈna/ страна́ (ru-RU)
- forms: страны [Case=Gen, Number=Sing] страны́; страны [Case=Nom, Number=Plur] стра́ны; стран [Case=Gen, Number=Plur]
  1. [A1] государство и территория, которую оно занимает в своих границах
     - example: Летом мы хотим проехать на машине через всю страну.
     - collocations: родная страна; развитая страна; развивающаяся страна; зарубежная страна; по всей стране; покинуть страну; въехать в страну; страна происхождения
  2. [B1] местность, край с особой природой, климатом или жизнью
     - example: Каждую осень эти птицы улетают в тёплые страны.
     - collocations: тёплые страны; жаркие страны; далёкие страны; северные страны; страна чудес

### 15. жизнь

**Lexeme 1: жизнь**, NOUN
- grammar: gender Fem; animacy Inan; declension 3
- said: /ˈʐɨzʲnʲ/ жизнь (ru-RU)
- forms: жизни [Case=Gen, Number=Sing] жи́зни; жизни [Case=Nom, Number=Plur] жи́зни; жизней [Case=Gen, Number=Plur] жи́зней
  1. [A2] существование человека или другого организма, пока он не умер
     - example: Врачи сделали всё, чтобы спасти жизнь этого ребёнка.
     - frames: жизнь кого
     - collocations: спасти жизнь; рисковать жизнью; отдать жизнь; лишить жизни; борьба за жизнь; вопрос жизни и смерти; жизнь на Земле
     - grammar: countability Both
     - opposites: смерть
  2. [A1] время от рождения до смерти человека
     - example: Бабушка прожила всю жизнь в этой маленькой деревне.
     - collocations: всю жизнь; на всю жизнь; первый раз в жизни; никогда в жизни; долгая жизнь; прожить жизнь
     - grammar: countability Mass
  3. [A2] условия, быт и занятия, из которых состоят дни человека
     - example: После переезда наша жизнь стала намного спокойнее.
     - frames: жизнь кого
     - collocations: личная жизнь; семейная жизнь; образ жизни; уровень жизни; повседневная жизнь; как жизнь; наладить жизнь
     - grammar: countability Mass
  4. [B1] деятельность общества или людей в какой-либо области
     - example: Студенты активно участвуют в общественной жизни университета.
     - frames: жизнь чего
     - collocations: общественная жизнь; политическая жизнь; культурная жизнь; участвовать в жизни
     - grammar: countability Mass
  5. [B1] реальная действительность в отличие от мечты или вымысла
     - example: В жизни всё оказалось совсем не так, как в кино.
     - collocations: воплотить в жизнь; проводить в жизнь; такова жизнь; в реальной жизни
     - grammar: countability Mass
  6. [B2] бодрость, энергия, яркое проявление движения и деятельности
     - example: Вечером улицы старого города полны жизни и музыки.
     - collocations: полный жизни; вдохнуть жизнь
     - grammar: countability Mass
- relations: derived_from жить (VERB)

### 16. лучше

**Lexeme 1: лучше**, ADV
- said: /lutɕ.ˈʂɨ/ лучше́ (ru-RU)
  1. [A2] предпочтительнее, разумнее, правильнее в данном случае
     - example: Тебе лучше остаться дома и как следует отдохнуть.
     - frames: кому лучше + инф.; лучше + инф., чем + инф.
     - collocations: лучше всего; лучше бы; тем лучше; уж лучше; лучше не надо
     - grammar: note not the comparative of хороший and хорошо, which are forms of those words
- relations: derived_from хорошо (ADV)

### 17. быть

**Lexeme 1: быть**, VERB
- grammar: aspect Imp; valency Intr; irregular True; note negated past is stressed on не: не́ был, не́ было, не́ были, but не была́
- said: /ˈbɨtʲ/ быть (ru-RU)
- forms: есть [Tense=Pres, VerbForm=Fin, Mood=Ind] (the only present form, used for every person; usually left out); буду [Tense=Fut, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] бу́ду; будешь [Tense=Fut, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] бу́дешь; будут [Tense=Fut, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] бу́дут; был [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind]; была [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] была́; было [Tense=Past, Gender=Neut, Number=Sing, VerbForm=Fin, Mood=Ind] бы́ло; были [Tense=Past, Number=Plur, VerbForm=Fin, Mood=Ind] бы́ли; будь [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin]; будучи [VerbForm=Conv, Tense=Pres] бу́дучи
  1. [A1] существовать или иметься в наличии
     - example: У моей сестры есть маленькая белая собака.
     - frames: быть у кого
     - collocations: быть в наличии
  2. [A1] находиться или присутствовать в каком-либо месте
     - example: Я весь день буду дома, заходи в любое время.
     - frames: быть где; быть у кого
     - collocations: быть в гостях; быть на месте
  3. [A2] происходить, случаться в какое-то время
     - example: Вчера вечером в нашем районе был сильный дождь.
  4. [A1] используется, чтобы связать предмет с его названием или признаком
     - example: Мой дедушка всю жизнь был сельским учителем.
     - frames: быть кем/чем; быть каким
     - grammar: note present form usually left out; the noun after it is mostly in the instrumental
  5. [A1] используется с инфинитивом, чтобы говорить о том, что произойдёт потом
     - example: Летом мы будем жить у бабушки в деревне.
     - frames: быть + инф.
     - grammar: note only in the future forms, with an imperfective infinitive

### 18. мир

**Lexeme 1: мир**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈmʲir/ мир (ru-RU)
- forms: мира [Case=Gen, Number=Sing] ми́ра; миры [Case=Nom, Number=Plur] миры́; миров [Case=Gen, Number=Plur] миро́в
  1. [A1] Земля со всеми странами и людьми, которые на ней живут
     - example: Мой брат мечтает объехать весь мир на велосипеде.
     - collocations: во всём мире; по всему миру; весь мир; чемпионат мира; страны мира; карта мира; рекорд мира
     - grammar: number Sing
  2. [B1] Вселенная и всё, что в ней существует, или отдельная её часть
     - example: Учёные давно спорят о том, как возник наш мир.
     - collocations: сотворение мира; другие миры; иные миры
  3. [B1] область жизни или круг людей, объединённых общим занятием или признаком
     - example: Она с детства мечтала попасть в мир большого театра.
     - frames: мир чего; мир кого
     - collocations: внутренний мир; животный мир; растительный мир; мир искусства; мир моды; преступный мир
**Lexeme 2: мир**, NOUN
- grammar: gender Masc; animacy Inan; countability Mass; number Sing; declension 2
- said: /ˈmʲir/ мир (ru-RU)
- forms: мира [Case=Gen, Number=Sing] ми́ра
  1. [A2] жизнь без войны, спокойные отношения между странами и народами
     - example: После долгой войны в стране наконец наступил мир.
     - collocations: борьба за мир; сохранить мир; установить мир; нарушить мир; прочный мир; мир во всём мире
     - opposites: война
  2. [B1] дружные, спокойные отношения между людьми без ссор и вражды
     - example: В нашей семье всегда царили мир и согласие.
     - frames: мир между кем; мир с кем
     - collocations: мир и согласие; жить в мире; мир в семье; мир и покой
  3. [B2] соглашение воюющих сторон о прекращении войны
     - example: После трёх лет боёв стороны наконец подписали мир.
     - frames: мир с кем
     - collocations: заключить мир; подписать мир; сепаратный мир

### 19. центр

**Lexeme 1: центр**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈtsentr/ центр (ru-RU)
- forms: центра [Case=Gen, Number=Sing] це́нтра; центры [Case=Nom, Number=Plur] це́нтры; центров [Case=Gen, Number=Plur] це́нтров
  1. [A1] главная, самая оживлённая часть города
     - example: Мы живём далеко от центра и ездим туда на метро.
     - frames: центр чего
     - collocations: центр города; в центре города; ехать в центр; исторический центр; в самом центре
  2. [A2] средняя точка или часть чего-либо, одинаково далёкая от краёв
     - example: Поставьте вазу с цветами в самый центр стола.
     - frames: центр чего
     - collocations: в центре внимания; центр тяжести; центр круга; оказаться в центре событий
  3. [A2] учреждение или здание, где занимаются определённой работой или оказывают услуги
     - example: Рядом с нашим домом скоро откроют новый торговый центр.
     - frames: центр чего; центр по чему
     - collocations: торговый центр; медицинский центр; учебный центр; научный центр; центр занятости; деловой центр
  4. [B1] место, где сосредоточена какая-либо деятельность, жизнь или власть
     - example: Этот небольшой город когда-то был важным центром торговли.
     - frames: центр чего
     - collocations: промышленный центр; культурный центр; административный центр; областной центр; крупный центр; финансовый центр
  5. [C1] политические силы с умеренными взглядами, стоящие между левыми и правыми
     - example: На выборах партии центра получили почти половину голосов.
     - collocations: левый центр; правый центр; партии центра
     - labels: domain politics
     - grammar: number Sing

### 20. обеспечить

**Lexeme 1: обеспечить**, VERB
- grammar: aspect Perf; valency Tr; conjugation 2
- said: /ɐ.bʲɪ.ˈspʲe.tɕitʲ/ обеспе́чить (ru-RU)
- forms: обеспечу [Tense=Fut, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] обеспе́чу; обеспечишь [Tense=Fut, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] обеспе́чишь; обеспечат [Tense=Fut, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] обеспе́чат; обеспечил [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] обеспе́чил; обеспечила [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] обеспе́чила; обеспечь [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] обеспе́чь
  1. [B2] сделать так, чтобы нужный результат был достигнут
     - example: Организаторы должны обеспечить безопасность всех участников концерта.
     - frames: обеспечить что; обеспечить кому что
  2. [B1] снабдить кого-либо или что-либо тем, что требуется
     - example: Служба доставки обеспечит жителей деревни питьевой водой.
     - frames: обеспечить кого/что чем
  3. [B2] дать кому-либо достаточно средств для жизни
     - example: Новая работа позволит ему обеспечить всю семью.
     - frames: обеспечить кого
- relations: aspect_partner обеспечивать (VERB)

### 21. кроме

**Lexeme 1: кроме**, ADP
- said: /ˈkro.mʲɪ/ кро́ме (ru-RU)
  1. [A2] за исключением кого-либо или чего-либо
     - example: Магазин работает каждый день, кроме воскресенья.
     - frames: кроме кого/чего
  2. [B1] сверх кого-либо или чего-либо, в дополнение к ним
     - example: Кроме английского, она свободно говорит по-французски и по-немецки.
     - frames: кроме кого/чего
     - collocations: кроме того

### 22. чемпионат

**Lexeme 1: чемпионат**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /tɕɪm.pʲi.ɐ.ˈnat/ чемпиона́т (ru-RU)
- forms: чемпионата [Case=Gen, Number=Sing] чемпиона́та; чемпионаты [Case=Nom, Number=Plur] чемпиона́ты; чемпионатов [Case=Gen, Number=Plur] чемпиона́тов
  1. [B1] соревнование за звание лучшего участника или лучшей команды
     - example: В следующем году наша страна примет чемпионат мира по футболу.
     - frames: чемпионат по чему; чемпионат чего по чему

### 23. главное

**Lexeme 1: главное**, NOUN
- grammar: gender Neut; animacy Inan; number Sing; declension adjectival
- said: /ˈɡlav.nə.jə/ гла́вное (ru-RU)
- forms: главного [Case=Gen, Number=Sing] гла́вного
  1. [B1] самое важное, основное в чём-либо
     - example: В этой работе главное — точность и внимание к деталям.
     - frames: главное в чём
     - collocations: самое главное; понять главное; говорить о главном; не забыть о главном
- relations: derived_from главный (ADJ)
**Lexeme 2: главное**, ADV
- grammar: note parenthetical, set off by commas
- said: /ˈɡlav.nə.jə/ гла́вное (ru-RU)
  1. [B1] употребляется, чтобы выделить самое важное в высказывании
     - example: Не волнуйся, главное, приходи вовремя и не забудь документы.
     - frames: главное, чтобы ...; главное, что ...
     - collocations: а главное; и главное; самое главное
- relations: derived_from главный (ADJ)

### 24. на

**Lexeme 1: на**, ADP
- said: /ˈna/ на (ru-RU)
  1. [A1] употребляется при указании поверхности, где находится кто-либо или что-либо
     - example: Твой телефон лежит на кухонном столе рядом с хлебом.
     - frames: на ком/чём
  2. [A1] употребляется при указании поверхности или места, куда направлено движение
     - example: Положи, пожалуйста, ключи на полку у двери.
     - frames: на кого/что
     - collocations: идти на работу; сесть на поезд; пойти на концерт
  3. [A1] употребляется при указании места, события или занятия, где кто-либо присутствует
     - example: Завтра я буду на работе до шести часов вечера.
     - frames: на чём
     - collocations: на улице; на работе; на уроке; на вокзале; на свежем воздухе
  4. [A1] употребляется при указании средства, с помощью которого что-либо делают
     - example: Каждый день мы ездим в школу на автобусе.
     - frames: на чём
     - collocations: ехать на поезде; играть на гитаре; говорить на русском языке
  5. [A2] употребляется при указании срока, который предполагается для чего-либо
     - example: Летом мы уезжаем к бабушке в деревню на целый месяц.
     - frames: на что
     - collocations: на время; на минуту; на всю жизнь
  6. [A2] употребляется при указании дня или времени, когда что-либо происходит
     - example: Мы договорились встретиться на следующий день после экзамена.
     - frames: на что; на чём
     - collocations: на следующий день; на этой неделе; на Новый год; на днях
  7. [A2] употребляется при указании цели или назначения чего-либо
     - example: Родители дали мне деньги на новые учебники.
     - frames: на что
     - collocations: на память; на завтрак; на всякий случай
  8. [B1] употребляется при указании величины разницы при сравнении
     - example: Мой брат старше меня на три года.
     - frames: на сколько
**Lexeme 2: на**, PART
- said: /ˈna/ на (ru-RU)
- forms: нате [Number=Plur] на́те (said to several people)
  1. [B1] употребляется, когда что-либо протягивают собеседнику, предлагая взять
     - example: На, возьми мой зонтик, а то промокнешь под дождём.
     - labels: formality informal, politeness familiar

### 25. несколько

**Lexeme 1: несколько**, NUM
- grammar: note in the nominative and accusative takes the genitive plural: несколько книг
- said: /nʲe.ˈskəlʲ.kə/ неско́лько (ru-RU)
- forms: нескольких [Case=Gen,Loc] не́скольких; нескольким [Case=Dat] не́скольким; несколькими [Case=Ins] не́сколькими
  1. [A1] небольшое неопределённое количество, обычно больше двух
     - example: Я подожду тебя ещё несколько минут у входа.
     - frames: несколько кого/чего
     - collocations: несколько раз; через несколько минут; несколько лет назад; в нескольких словах; в нескольких шагах
**Lexeme 2: несколько**, ADV
- said: /ˈnʲe.skəlʲ.kə/ не́сколько (ru-RU)
  1. [B2] в небольшой степени, до известной меры
     - example: Его неожиданный ответ несколько удивил всех присутствующих.
     - collocations: несколько иначе; несколько удивлён; несколько смущён

### 26. любой

**Lexeme 1: любой**, DET
- grammar: declension adjectival
- said: /lʲu.ˈboj/ любо́й (ru-RU)
  1. [A2] какой угодно из всех возможных, всякий без исключения
     - example: Вы можете выбрать для своей комнаты любой цвет.
     - collocations: в любом случае; в любое время; в любой момент; любой ценой; любыми средствами; на любой вкус
**Lexeme 2: любой**, PRON
- grammar: declension adjectival; note declines like an adjective; masculine for a person in general, feminine любая for a woman
- said: /lʲu.ˈboj/ любо́й (ru-RU)
  1. [B1] всякий человек, кто угодно
     - example: Такую простую задачу решит любой из вас.
     - frames: любой из кого

### 27. этот

**Lexeme 1: этот**, DET
- said: /ˈe.tət/ э́тот (ru-RU)
- forms: эта [Case=Nom, Gender=Fem, Number=Sing] э́та; это [Case=Nom, Gender=Neut, Number=Sing] э́то; эти [Case=Nom, Number=Plur] э́ти; этого [Case=Gen, Gender=Masc,Neut, Number=Sing] э́того; этих [Case=Gen, Number=Plur] э́тих
  1. [A1] указывает на предмет или лицо, которые находятся рядом или перед глазами
     - example: Возьми этот стул и поставь его поближе к окну.
     - opposites: тот
  2. [A2] указывает на то, о чём только что говорили или сейчас скажут
     - example: Он прочитал письмо, но об этом письме никому не сказал.
     - collocations: в этом случае; по этому поводу; в этой связи; при этом
  3. [A1] о времени: нынешний, текущий, в котором находится говорящий
     - example: В этом году лето у нас было очень жарким.
     - collocations: в этом году; на этой неделе; в этот раз; в этот момент; в этом месяце; этой зимой
  4. [B2] указывает на человека, о котором говорят с пренебрежением
     - example: А этот опять пришёл к нам без всякого приглашения.
     - labels: formality informal, attitude disapproving
     - grammar: note used without a noun, in place of one

### 28. цель

**Lexeme 1: цель**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 3
- said: /ˈtselʲ/ цель (ru-RU)
- forms: цели [Case=Gen, Number=Sing] це́ли; цели [Case=Nom, Number=Plur] це́ли; целей [Case=Gen, Number=Plur] це́лей
  1. [A2] то, к чему человек стремится и чего хочет достичь
     - example: Главная цель нашей поездки — увидеть старую часть города.
     - frames: цель чего; с целью чего; с целью + инф.; в целях чего
     - collocations: достичь цели; добиться цели; поставить цель; ставить перед собой цель; преследовать цель; иметь целью; главная цель; конечная цель; цель жизни
  2. [B1] предмет или место, в которое стреляют или которое нужно поразить
     - example: Стрелок волновался и дважды не попал в цель.
     - collocations: попасть в цель; поразить цель; бить в цель; промахнуться мимо цели; движущаяся цель

### 29. второй

**Lexeme 1: второй**, ADJ
- grammar: comparable False; note ordinal numeral, declined like an adjective
- said: /ftɐ.ˈroj/ второ́й (ru-RU)
  1. [A1] следующий по порядку сразу после первого
     - example: Наша квартира находится на втором этаже этого старого дома.
     - collocations: второй раз; второй этаж; второе место; вторая половина; второй класс; второй язык
  2. [B2] менее важный или менее хороший, чем лучший
     - example: После рождения детей работа отошла для неё на второй план.
     - collocations: отойти на второй план; второй сорт; на вторых ролях; второй план
  3. [B1] ставший близким и родным, как настоящий, или подобный кому-либо известному
     - example: Этот северный город давно стал для меня второй родиной.
     - collocations: вторая родина; второй дом; вторая мама; второе дыхание
     - labels: figurative True

### 30. игра

**Lexeme 1: игра**, NOUN
- grammar: gender Fem; animacy Inan; countability Count; declension 1
- said: /i.ˈɡra/ игра́ (ru-RU)
- forms: игры [Case=Gen, Number=Sing] игры́; игры [Case=Nom, Number=Plur] и́гры; игр [Case=Gen, Number=Plur]
  1. [A1] занятие по определённым правилам, которое служит для развлечения или отдыха
     - example: По вечерам мы всей семьёй садимся за новую настольную игру.
     - frames: игра во что
     - collocations: настольная игра; компьютерная игра; азартные игры; правила игры; детская игра; игра в прятки; сыграть в игру
  2. [A2] состязание команд или игроков в каком-либо виде спорта
     - example: Вчерашняя игра закончилась уверенной победой хозяев поля.
     - frames: игра с кем; игра против кого
     - collocations: выиграть игру; проиграть игру; Олимпийские игры; домашняя игра; игра закончилась вничью; решающая игра
  3. [B1] исполнение музыки на инструменте или роли на сцене, в кино
     - example: Зрители долго аплодировали прекрасной игре молодой актрисы.
     - frames: игра на чём; игра кого
     - collocations: игра на гитаре; игра на фортепиано; игра актёров; актёрская игра
  4. [B2] хитрые, часто скрытые действия ради достижения своей цели
     - example: Он слишком поздно понял, что вокруг него ведётся нечестная игра.
     - collocations: вести двойную игру; политическая игра; грязная игра; раскрыть чью-то игру
     - labels: figurative True
  5. [C1] постоянная смена и переливы света, красок или звуков
     - example: На закате мы любовались игрой света на воде.
     - frames: игра чего
     - collocations: игра света и тени; игра красок
     - labels: figurative True
- relations: derived_from играть (VERB)

### 31. город

**Lexeme 1: город**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈɡo.rət/ го́род (ru-RU)
- forms: города [Case=Gen, Number=Sing] го́рода; города [Case=Nom, Number=Plur] города́; городов [Case=Gen, Number=Plur] городо́в
  1. [A1] большой населённый пункт с улицами, домами, магазинами и предприятиями
     - example: Мы живём в небольшом, но очень зелёном городе на реке.
     - collocations: родной город; крупный город; центр города; столичный город; провинциальный город; город-миллионник; весь город; город-герой
  2. [A2] жизнь и место в отличие от сельской местности
     - example: Летом мы любим уезжать за город, подальше от шума.
     - collocations: уехать за город; жить за городом; загородный дом; переехать в город; выехать за город
     - grammar: number Sing; note в выражении за город ударение часто переходит на предлог: за́ город

### 32. без

**Lexeme 1: без**, ADP
- grammar: note безо before some consonant clusters: безо всего, безо льда
- said: /ˈbʲes/ без (ru-RU)
- spellings: без; безо
  1. [A1] указывает на то, что кого-либо или чего-либо нет
     - example: Я всегда пью кофе без сахара и без молока.
     - frames: без кого/чего
     - collocations: без сомнения; без труда; без проблем; без исключения; без разрешения; без помощи; без конца
     - opposites: с
  2. [A2] указывает, сколько минут не хватает до целого часа
     - example: Давай встретимся у кинотеатра без четверти семь.
     - frames: без чего
     - collocations: без четверти; без пяти минут

### 33. огромный

**Lexeme 1: огромный**, ADJ
- said: /ɐ.ˈɡrom.nɨj/ огро́мный (ru-RU)
  1. [A2] очень большой по размеру, количеству или силе
     - example: За нашим домом начинается огромный густой лес.
     - opposites: крошечный

### 34. подарок

**Lexeme 1: подарок**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /pɐ.ˈda.rək/ пода́рок (ru-RU)
- forms: подарка [Case=Gen, Number=Sing] пода́рка; подарки [Case=Nom, Number=Plur] пода́рки; подарков [Case=Gen, Number=Plur] пода́рков
  1. [A1] то, что дают кому-либо в знак внимания, не требуя ничего взамен
     - example: Мы купили маме красивый подарок на день рождения.
     - frames: подарок кому; подарок от кого
     - collocations: получить в подарок
  2. [B2] то, что приносит особую радость или оказывается большой удачей
     - example: Этот тёплый солнечный день стал для нас настоящим подарком.
     - frames: подарок для кого
     - labels: figurative True
- relations: derived_from подарить (VERB)

### 35. управление

**Lexeme 1: управление**, NOUN
- grammar: gender Neut; animacy Inan; countability Both; declension 2
- said: /u.prəv.ˈlʲe.nʲɪ.jə/ управле́ние (ru-RU)
- forms: управления [Case=Gen, Number=Sing] управле́ния; управления [Case=Nom, Number=Plur] управле́ния; управлений [Case=Gen, Number=Plur] управле́ний
  1. [B1] руководство деятельностью людей, организации или страны
     - example: Управление большой компанией требует опыта и терпения.
     - frames: управление кем/чем; под управлением кого
     - collocations: управление государством; система управления; органы управления; государственное управление; управление проектами; управление персоналом
     - grammar: countability Mass
  2. [B1] действия, которыми направляют движение машины или работу механизма
     - example: На скользкой дороге водитель не справился с управлением.
     - frames: управление чем
     - collocations: не справиться с управлением; потерять управление; управление автомобилем; взять управление на себя
     - grammar: countability Mass
  3. [B2] учреждение или крупный отдел, ведающий отдельной областью работы
     - example: Мой брат работает в городском управлении образования.
     - frames: управление по чему
     - collocations: главное управление; начальник управления; управление полиции; налоговое управление
     - grammar: countability Count
  4. [B2] устройства, с помощью которых направляют движение машины или работу прибора
     - example: У этой старой машины очень тугое рулевое управление.
     - collocations: рулевое управление; пульт управления; панель управления; дистанционное управление
     - grammar: countability Mass
- relations: verbal_noun_of управлять (VERB)

### 36. жить

**Lexeme 1: жить**, VERB
- grammar: aspect Imp; valency Intr; conjugation 1
- said: /ˈʐɨtʲ/ жить (ru-RU)
- forms: живу [Tense=Pres, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] живу́; живёшь [Tense=Pres, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] живёшь; живут [Tense=Pres, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] живу́т; жил [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind]; жила [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] жила́; жило [Tense=Past, Gender=Neut, Number=Sing, VerbForm=Fin, Mood=Ind] жи́ло; жили [Tense=Past, Number=Plur, VerbForm=Fin, Mood=Ind] жи́ли; живи [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] живи́
  1. [A1] постоянно находиться в каком-либо месте, иметь там свой дом
     - example: Мой прадед жил больше ста лет и всё помнил.
     - frames: жить где; жить у кого; жить с кем
     - collocations: жить за границей; жить вместе; жить по соседству; жить в общежитии; жить в деревне
  2. [A2] существовать на свете, пока не наступила смерть
     - example: Мы живём в маленькой квартире недалеко от центра города.
     - collocations: долго жить; жить на свете; жить до старости
  3. [B1] проводить свои дни каким-либо образом, в каких-либо условиях
     - example: После переезда за город они стали жить гораздо спокойнее.
     - collocations: жить в мире; жить дружно; жить в согласии; жить счастливо; жить бедно; жить в достатке
  4. [B1] получать средства на еду и другие нужды из какого-либо источника
     - example: Сейчас семья живёт на одну небольшую зарплату отца.
     - frames: жить на что; жить за счёт кого/чего; жить чем
     - collocations: жить на зарплату; жить на пенсию; жить за счёт родителей
  5. [B2] полностью отдавать чему-либо свои мысли, чувства и силы
     - example: Она живёт музыкой и занимается на скрипке каждый день.
     - frames: жить кем/чем
     - collocations: жить надеждой; жить мечтой; жить воспоминаниями; жить работой

### 37. следующий

**Lexeme 1: следующий**, ADJ
- grammar: comparable False
- said: /ˈslʲe.du.ju.ɕːɪj/ сле́дующий (ru-RU)
  1. [A1] идущий или находящийся сразу после кого-либо или чего-либо
     - example: Мы обязательно поговорим об этом в следующий раз.
     - collocations: в следующий раз; на следующий день; в следующем году; на следующей неделе; следующая остановка; следующий по очереди
     - opposites: предыдущий
  2. [B1] такой, о котором будет сказано или который приведён ниже
     - example: Упражнение нужно выполнять следующим образом: медленно и без рывков.
     - collocations: следующим образом; сказать следующее; заключаться в следующем
     - grammar: note often used as a neuter noun: следующее
- relations: derived_from следовать (VERB)

### 38. решить

**Lexeme 1: решить**, VERB
- grammar: aspect Perf; valency Tr; conjugation 2
- said: /rʲɪ.ˈʂɨtʲ/ реши́ть (ru-RU)
- forms: решу [Tense=Fut, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] решу́; решишь [Tense=Fut, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] реши́шь; решат [Tense=Fut, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] реша́т; решил [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] реши́л; решила [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] реши́ла; реши [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] реши́
  1. [A2] подумав, сделать выбор, как поступить
     - example: Я никак не могу решить эту задачу по математике.
     - frames: решить + инф.; решить, что ...
     - collocations: твёрдо решить; окончательно решить; решить для себя
  2. [A2] найти ответ или способ справиться с трудностью
     - example: Этим летом мы решили поехать к морю всей семьёй.
     - frames: решить что
     - collocations: решить задачу; решить проблему; решить вопрос; решить уравнение; решить спор; решить кроссворд
  3. [B2] определить, каким будет результат или дальнейшее развитие чего-либо
     - example: Один удачный удар в конце игры решил исход матча.
     - frames: решить что
     - collocations: решить судьбу; решить исход; решить дело
- relations: aspect_partner решать (VERB)

### 39. очень

**Lexeme 1: очень**, ADV
- grammar: invariable True
- said: /ˈo.tɕɪnʲ/ о́чень (ru-RU)
  1. [A1] в большой степени, значительно сильнее обычного
     - example: Мне очень понравился фильм, который мы вчера смотрели.
     - collocations: не очень; очень приятно; очень много; очень хорошо; очень любить; очень нужно; очень жаль
     - grammar: note not used with comparatives, which take гораздо or намного

### 40. все

**Lexeme 1: все**, PRON
- grammar: number Plur
- said: /ˈfsʲe/ все (ru-RU)
- forms: всех [Case=Gen,Acc, Number=Plur]; всем [Case=Dat, Number=Plur]; всеми [Case=Ins, Number=Plur] все́ми; всех [Case=Loc, Number=Plur]
  1. [A1] каждый человек из какой-либо группы, люди без исключения
     - example: Вечером все собрались за большим столом на кухне.
     - collocations: все вместе; все без исключения; все до одного; все остальные
     - opposites: никто
- relations: derived_from весь (DET)
**Lexeme 2: всё**, PRON
- said: /ˈfsʲo/ всё (ru-RU)
- spellings: всё; все
- forms: всего [Case=Gen, Number=Sing] всего́; всему [Case=Dat, Number=Sing] всему́; всем [Case=Ins, Number=Sing]; всём [Case=Loc, Number=Sing]
  1. [A1] любые предметы, дела и обстоятельства без исключения
     - example: Я расскажу тебе всё, что знаю об этом деле.
     - frames: всё, что ...
     - collocations: всё в порядке; прежде всего; больше всего; лучше всего; спасибо за всё; всё на свете
     - opposites: ничто
  2. [A2] говорится, когда что-то закончилось или больше ничего не будет
     - example: Всё, хватит спорить, давайте лучше пойдём обедать.
     - collocations: вот и всё; и всё
     - labels: formality informal
     - grammar: position Pred; note usually a sentence of its own
- relations: derived_from весь (DET)
**Lexeme 3: всё**, ADV
- said: /ˈfsʲo/ всё (ru-RU)
- spellings: всё; все
  1. [A2] постоянно, в течение долгого времени
     - example: Бабушка всё сидит у окна и смотрит на улицу.
  2. [A2] до сих пор, до настоящего момента
     - example: Уже полночь, а сына всё нет дома.
     - collocations: всё ещё
**Lexeme 4: всё**, PART
- said: /ˈfsʲo/ всё (ru-RU)
- spellings: всё; все
  1. [B1] используется, чтобы показать постепенное усиление признака или действия
     - example: С каждым днём на улице становится всё холоднее.
     - collocations: всё больше; всё чаще; всё лучше и лучше; всё меньше
     - grammar: note before a comparative
  2. [B1] используется, чтобы подчеркнуть единственную причину чего-либо
     - example: Мы опоздали на поезд, и всё из-за этой пробки.
     - frames: всё из-за кого/чего
     - labels: formality informal

### 41. русский

**Lexeme 1: русский**, ADJ
- grammar: comparable False
- said: /ˈrus.kʲij/ ру́сский (ru-RU)
  1. [A1] принадлежащий восточнославянскому народу России, его культуре или языку
     - example: Мы долго обсуждали русскую литературу девятнадцатого века.
     - collocations: русский язык; русская литература; русская кухня; русский народ; русская баня; русская печь
**Lexeme 2: русский**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; declension adjectival
- said: /ˈrus.kʲij/ ру́сский (ru-RU)
- forms: русского [Case=Gen, Number=Sing] ру́сского; русские [Case=Nom, Number=Plur] ру́сские; русских [Case=Gen, Number=Plur] ру́сских
  1. [A1] человек из восточнославянского народа, основного населения России
     - example: В этом городе живут и русские, и татары, и украинцы.
     - collocations: русские люди; по национальности русский
**Lexeme 3: русский**, NOUN
- grammar: gender Masc; animacy Inan; number Sing; declension adjectival
- said: /ˈrus.kʲij/ ру́сский (ru-RU)
- forms: русского [Case=Gen, Number=Sing] ру́сского
  1. [A1] язык восточнославянского народа, государственный язык России
     - example: Я учу русский уже три года и много читаю.
     - collocations: учить русский; знать русский; переводить с русского; сдать русский
     - labels: formality informal

### 42. конец

**Lexeme 1: конец**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /kɐ.ˈnʲets/ коне́ц (ru-RU)
- forms: конца [Case=Gen, Number=Sing] конца́; концы [Case=Nom, Number=Plur] концы́; концов [Case=Gen, Number=Plur] концо́в
  1. [A2] последний момент или последняя часть события или периода
     - example: Отчёт нужно сдать до конца этой недели.
     - frames: конец чего
     - collocations: в конце концов; без конца; подходить к концу; положить конец; до конца; в конце года; счастливый конец; конец света
     - opposites: начало
  2. [A2] крайняя точка или часть предмета, дороги или пространства
     - example: Наш дом стоит в самом конце этой длинной улицы.
     - frames: конец чего
     - collocations: на другом конце; из конца в конец; конец верёвки; на том конце провода; в конце коридора
     - opposites: начало
  3. [B1] гибель, смерть или полное поражение
     - example: Если начальник узнает правду, нам точно конец.
     - frames: конец кому
     - collocations: пришёл конец
     - labels: formality informal
  4. [B1] путь от одного места до другого
     - example: Дорога до деревни в один конец занимает три часа.
     - collocations: в один конец; в оба конца; билет в один конец

### 43. за

**Lexeme 1: за**, ADP
- grammar: note usually unstressed; takes the stress in some set phrases: за́ город, за́ руку, за́ ночь
- said: /ˈza/ за (ru-RU)
  1. [A1] указывает на то, что даётся в обмен или как плата
     - example: Я отдал за эти билеты в кино почти все деньги.
     - frames: за что
     - collocations: платить за; купить за; за деньги; за бесценок; за свой счёт
  2. [A1] указывает на место по другую сторону от кого-либо или чего-либо
     - example: Машина стоит за домом, рядом со старым гаражом.
     - frames: за кем/чем; за кого/что
     - collocations: за городом; за город; за границей; за границу; за окном; за углом; за рубежом
  3. [A2] указывает на того, кто или что движется впереди, следом
     - example: Иди за мной, я покажу тебе короткую дорогу.
     - frames: за кем/чем
     - collocations: идти за; один за другим; день за днём; следовать за
  4. [A2] указывает на то, что хотят получить, принести или привести
     - example: Мама послала сына в магазин за хлебом и молоком.
     - frames: за кем/чем
     - collocations: сходить за; обратиться за помощью; прийти за; послать за
  5. [A2] указывает на срок, в течение которого что-либо происходит или делается
     - example: За последние пять лет наш город сильно изменился.
     - frames: за что
     - collocations: за последние годы; за один день; за всю жизнь; за это время
  6. [B1] указывает на того или то, кого поддерживают или защищают
     - example: Большинство жителей проголосовали за строительство нового парка.
     - frames: за кого/что
     - collocations: голосовать за; бороться за; выступать за; болеть за
     - opposites: против
  7. [A1] указывает на причину, по которой благодарят, хвалят или наказывают
     - example: Спасибо вам большое за помощь и поддержку.
     - frames: за что
     - collocations: спасибо за; благодарить за; наказать за; извиниться за; любить за
  8. [A2] указывает на предмет или занятие, около которого или во время которого находятся
     - example: Вечером вся семья собралась за большим столом.
     - frames: за чем
     - collocations: сидеть за столом; сесть за стол; за рулём; за компьютером; за обедом; за работой
  9. [B1] указывает на расстояние или время, отделяющее одно от другого
     - example: Приходите на вокзал за полчаса до отправления поезда.
     - frames: за что до чего; за что от чего
     - collocations: за час до
  10. [B1] указывает на часть, которую держат или за которую берут
     - example: Девочка крепко держала отца за руку.
     - frames: кого/что за что
     - collocations: взять за руку; держать за руку; схватить за
  11. [B2] указывает на количество больше названного, обычно о возрасте или времени
     - example: Нашему соседу уже за шестьдесят, но он бегает каждое утро.
     - frames: за что
     - collocations: за полночь; далеко за
  12. [B2] вместо кого-либо или от имени другого человека
     - example: Сегодня я поработаю за тебя, а ты отдохни.
     - frames: за кого
     - collocations: работать за двоих; есть за двоих
**Lexeme 2: за**, ADV
- grammar: position Pred
- said: /ˈza/ за (ru-RU)
  1. [B1] согласен с предложением, поддерживает его
     - example: Если все хотят поехать на море, то я только за.
     - collocations: я за; кто за; только за; голосовать за
     - opposites: против
**Lexeme 3: за**, NOUN
- grammar: gender Neut; animacy Inan; invariable True; countability Count
- said: /ˈza/ за (ru-RU)
  1. [B2] довод в пользу чего-либо
     - example: Перед покупкой квартиры стоит взвесить все за и против.
     - collocations: взвесить все за и против; за и против
     - opposites: против

### 44. над

**Lexeme 1: над**, ADP
- grammar: note надо before some consonant clusters: надо мной, надо всем
- said: /ˈnat/ над (ru-RU)
- spellings: над; надо
  1. [A1] выше кого-либо или чего-либо, в пространстве сверху
     - example: Над нашим городом весь день висели тяжёлые серые тучи.
     - frames: над кем/чем
     - collocations: над головой; над уровнем моря; над землёй
     - opposites: под
  2. [A2] указывает на предмет, на который направлены действие, мысль или чувство
     - example: Я уже третий месяц работаю над новой книгой.
     - frames: работать над чем; думать над чем; смеяться над кем/чем; власть над кем/чем; победа над кем/чем

### 45. время

**Lexeme 1: время**, NOUN
- grammar: gender Neut; animacy Inan; note irregular noun in -мя: времени, временем; plural времена, времён
- said: /ˈvrʲe.mʲə/ вре́мя (ru-RU)
- forms: времени [Case=Gen, Number=Sing] вре́мени; временем [Case=Ins, Number=Sing] вре́менем; времена [Case=Nom, Number=Plur] времена́; времён [Case=Gen, Number=Plur]
  1. [A1] продолжительность, в течение которой что-то происходит или можно что-то делать
     - example: У меня сейчас совсем нет времени на долгие разговоры.
     - frames: время на что; время для чего; время + инф.
     - collocations: свободное время; тратить время; терять время; проводить время; нет времени; экономить время; некоторое время; долгое время
     - grammar: countability Mass; number Sing
  2. [A1] определённый момент или час, когда что-то происходит
     - example: Встретимся у кинотеатра в обычное время, около семи.
     - frames: время чего
     - collocations: сколько времени; в это время; в то же время; местное время; назначить время; время обеда
     - grammar: number Sing, mainly
  3. [A2] период в истории или в жизни человека с общими чертами
     - example: В те времена люди писали друг другу длинные письма.
     - collocations: в наше время; в те времена; время года; старые времена; военное время; трудные времена
     - grammar: countability Count
  4. [B1] непрерывное движение от прошлого к будущему, в котором всё изменяется
     - example: Время лечит, и со временем боль обязательно пройдёт.
     - collocations: время идёт; время летит; со временем; пространство и время
     - grammar: countability Mass; number Sing
  5. [B1] форма глагола, показывающая, когда происходит действие
     - example: В этом упражнении нужно поставить глаголы в прошедшее время.
     - collocations: прошедшее время; настоящее время; будущее время
     - labels: domain grammar
     - grammar: countability Count

### 46. до

**Lexeme 1: до**, ADP
- said: /ˈdo/ до (ru-RU)
  1. [A1] указывает на место, где кончается движение или расстояние
     - example: От нашего дома до школы можно дойти за десять минут.
     - frames: до чего; от чего до чего; до кого
     - collocations: с головы до ног; дойти до угла; доехать до центра
  2. [A1] указывает на время, когда кончается действие или состояние
     - example: Магазин работает с девяти утра до восьми вечера.
     - frames: до чего; с чего до чего
     - collocations: до сих пор; до завтра; до вечера; до утра; до конца
  3. [A2] указывает на событие, раньше которого что-то происходит
     - example: Обязательно вымой руки до обеда, а не после.
     - frames: до чего; за что до чего; задолго до чего
     - collocations: до войны; до нашей эры
     - opposites: после
  4. [B1] указывает на предел, степень или количество, которого что-то достигает
     - example: Летом температура здесь поднимается до сорока градусов.
     - frames: до чего
     - collocations: до слёз; до смерти; промокнуть до нитки; до последней капли
  5. [B1] нет времени, сил или желания заниматься кем-либо или чем-либо
     - example: Сейчас мне не до разговоров, я очень спешу.
     - frames: кому не до кого/чего
     - collocations: не до шуток; не до смеха; не до того
     - grammar: note only with не, as a predicate
**Lexeme 2: до**, NOUN
- grammar: gender Neut; animacy Inan; countability Count; invariable True
- said: /ˈdo/ до (ru-RU)
  1. [B2] первая ступень музыкального звукоряда, нота
     - example: Эта мелодия начинается с ноты до первой октавы.
     - collocations: до мажор; до минор; нота до
     - labels: domain music

### 47. хорошо

**Lexeme 1: хорошо**, ADV
- said: /xə.rɐ.ˈʂo/ хорошо́ (ru-RU)
- forms: лучше [Degree=Cmp] лу́чше
  1. [A1] так, как нужно, умело и без недостатков
     - example: Моя сестра очень хорошо говорит по-английски и по-немецки.
     - frames: хорошо относиться к кому/чему
     - collocations: хорошо знать; хорошо выглядеть; хорошо учиться; хорошо относиться; хорошо спать; хорошо говорить; хорошо разбираться
     - opposites: плохо
  2. [A1] о приятном, удобном или радостном состоянии, положении
     - example: Летом на даче очень хорошо и спокойно.
     - frames: кому хорошо; хорошо + инф.; хорошо, что ...
     - collocations: как хорошо; хорошо, что
     - grammar: position Pred; note used as the predicate of an impersonal sentence, often with the dative
     - opposites: плохо
  3. [C1] заметно больше, чем указанное число или возраст
     - example: Нашему соседу, кажется, уже хорошо за шестьдесят.
     - frames: хорошо за что
     - labels: formality informal
- relations: adverb_of хороший (ADJ)
**Lexeme 2: хорошо**, PART
- said: /xə.rɐ.ˈʂo/ хорошо́ (ru-RU)
  1. [A1] используется для выражения согласия или разрешения
     - example: Хорошо, я позвоню тебе вечером после работы.
     - collocations: ну хорошо; ладно, хорошо
**Lexeme 3: хорошо**, NOUN
- grammar: gender Neut; animacy Inan; invariable True
- said: /xə.rɐ.ˈʂo/ хорошо́ (ru-RU)
  1. [B1] школьная оценка «четыре» по пятибалльной шкале
     - example: За последнюю контрольную по физике я получил хорошо.
     - collocations: получить хорошо; сдать на хорошо; поставить хорошо

### 48. прийти

**Lexeme 1: прийти**, VERB
- grammar: aspect Perf; valency Intr; conjugation 1; irregular True
- said: /prʲij.ˈtʲi/ прийти́ (ru-RU)
- spellings: прийти; притти [older books only]
- forms: приду [Tense=Fut, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] приду́; придёшь [Tense=Fut, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] придёшь; придут [Tense=Fut, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] приду́т; пришёл [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] пришёл; пришла [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] пришла́; пришли [Tense=Past, Number=Plur, VerbForm=Fin, Mood=Ind] пришли́; приди [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] приди́
  1. [A1] идя или едучи, добраться до какого-либо места
     - example: Завтра я приду к тебе в гости после работы.
     - frames: прийти куда; прийти к кому; прийти откуда; прийти + инф.
     - collocations: прийти домой; прийти в гости; прийти на работу; прийти вовремя; прийти пешком; прийти первым
     - opposites: уйти
  2. [A2] быть доставленным или прибыть к месту назначения
     - example: Сегодня утром мне пришло письмо от старого друга.
     - frames: прийти кому; прийти на что
     - collocations: пришло письмо; пришло сообщение; поезд пришёл; пришла посылка
  3. [B1] наступить, настать в свой срок
     - example: Наконец пришла весна, и снег начал быстро таять.
     - collocations: пришла весна; пришло время; пришёл конец
  4. [B1] появиться в сознании, возникнуть в мыслях
     - example: Вдруг мне пришла в голову отличная идея.
     - frames: прийти кому в голову
     - collocations: прийти в голову; прийти на ум
  5. [B2] достичь какого-либо результата или состояния
     - example: После долгого спора мы пришли к общему решению.
     - frames: прийти к чему; прийти во что
     - collocations: прийти к выводу; прийти к соглашению; прийти к власти; прийти в себя; прийти в восторг; прийти в ярость; прийти в негодность
- relations: aspect_partner приходить (VERB); derived_from идти (VERB)

### 49. право

**Lexeme 1: право**, NOUN
- grammar: gender Neut; animacy Inan; declension 2
- said: /ˈpra.və/ пра́во (ru-RU)
- forms: права [Case=Gen, Number=Sing] пра́ва; права [Case=Nom, Number=Plur] права́; прав [Case=Gen, Number=Plur]
  1. [A2] закреплённая законом или обычаем возможность что-либо делать или получать
     - example: Каждый гражданин имеет право на бесплатное образование.
     - frames: право на что; право + инф.
     - collocations: иметь право; права человека; лишить права; получить право; по праву; право голоса; нарушать права; защищать права
     - grammar: countability Count
  2. [B1] система законов и правил, принятых в обществе, а также наука о ней
     - example: Мой старший брат изучает право в университете.
     - collocations: гражданское право; уголовное право; международное право; изучать право; нормы права
     - grammar: countability Mass; number Sing
  3. [B1] документ, разрешающий управлять автомобилем или другим транспортом
     - example: Мне наконец выдали права, и теперь я могу водить машину.
     - collocations: водительские права; получить права; сдать на права; лишить прав
     - labels: formality informal
     - grammar: countability Count; number Plur
**Lexeme 2: право**, PART
- said: /ˈpra.və/ пра́во (ru-RU)
  1. [C1] используется, чтобы подчеркнуть искренность или правдивость сказанного
     - example: Я, право, не знаю, что тебе ответить на это.
     - labels: formality informal

### 50. советский

**Lexeme 1: советский**, ADJ
- grammar: comparable False
- said: /sɐ.ˈvʲet.skʲij/ сове́тский (ru-RU)
  1. [B1] существовавший в СССР или принадлежащий этой стране
     - example: Мой дедушка всю жизнь проработал инженером на советском заводе.
     - collocations: советское время; советские времена; советский период; советская эпоха; советский народ; советский человек; советская армия; бывший советский
     - labels: time historical
  2. [B2] связанный с выборными органами власти рабочих и крестьян после 1917 года
     - example: После революции в городе установили новую советскую власть.
     - collocations: советская власть; советский строй
     - labels: time historical
- relations: derived_from совет (NOUN)

### 51. бывший

**Lexeme 1: бывший**, ADJ
- grammar: position Attr; comparable False
- said: /ˈbɨv.ʂɨj/ бы́вший (ru-RU)
  1. [A2] который был кем-либо или чем-либо раньше, но теперь уже не является
     - example: Вчера на улице я случайно встретил своего бывшего учителя.
     - collocations: бывший муж; бывшая жена; бывший президент; бывший СССР; бывший коллега; бывший одноклассник
     - opposites: нынешний
- relations: derived_from быть (VERB)
**Lexeme 2: бывший**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; declension adjectival
- said: /ˈbɨv.ʂɨj/ бы́вший (ru-RU)
- forms: бывшего [Case=Gen, Number=Sing] бы́вшего; бывшие [Case=Nom, Number=Plur] бы́вшие; бывших [Case=Gen, Number=Plur] бы́вших
  1. [B1] мужчина, с которым раньше были в браке или в любовных отношениях
     - example: Она до сих пор иногда переписывается со своим бывшим.
     - labels: formality informal
- relations: derived_from бывший (ADJ)
