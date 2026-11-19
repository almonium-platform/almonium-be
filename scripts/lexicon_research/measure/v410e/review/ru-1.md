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
- said: /vɐ.ɐp.ˈɕːe/ вообще́ (ru-RU), /vɐ.ˈɕːe/ вообще́ (ru-RU, casual)
  1. [A2] совсем, совершенно, нисколько
     - example: После его объяснений я вообще ничего не понимаю.
     - collocations: вообще ничего; вообще никогда; вообще не понимать; вообще никто
     - labels: formality informal
     - grammar: note usually with a negation
  2. [B1] в целом, без учёта частностей и отдельных случаев
     - example: Вообще он человек добрый, просто сегодня устал.
     - collocations: вообще говоря; и вообще; а вообще
  3. [B1] всегда, как правило, при любых условиях
     - example: Я вообще встаю рано, даже в выходные дни.

### 2. есть

**Lexeme 1: есть**, VERB
- grammar: aspect Imp; valency Both; irregular True
- said: /ˈjesʲtʲ/ есть (ru-RU)
- forms: ем [Tense=Pres, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind]; ешь [Tense=Pres, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind]; ест [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind]; едим [Tense=Pres, Person=1, Number=Plur, VerbForm=Fin, Mood=Ind] еди́м; едите [Tense=Pres, Person=2, Number=Plur, VerbForm=Fin, Mood=Ind] еди́те; едят [Tense=Pres, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] едя́т; ел [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind]; ела [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] е́ла; ешь [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin]
  1. [A1] принимать пищу, жевать и глотать её
     - example: Мы обычно едим суп на обед всей семьёй.
     - frames: есть что; есть чем
     - collocations: хотеть есть; есть с аппетитом; есть ложкой; есть руками; есть досыта; есть мясо
  2. [B2] вызывать жжение или постепенно разрушать что-либо своим действием
     - example: От костра шёл дым, который сильно ел глаза.
     - frames: есть что
     - collocations: дым ест глаза; ржавчина ест железо
     - grammar: valency Tr
- relations: aspect_partner съесть (VERB)
**Lexeme 2: есть**, VERB
- grammar: invariable True; note present tense of быть; one form for every person and number
- said: /ˈjesʲtʲ/ есть (ru-RU)
  1. [A1] иметься в наличии, существовать или находиться где-либо
     - example: У меня есть старший брат и две сестры.
     - frames: у кого есть кто/что; где есть кто/что
  2. [B2] употребляется для связи подлежащего со сказуемым в значении «является»
     - example: Это и есть та самая книга, о которой я говорил.
     - collocations: это и есть; закон есть закон
     - grammar: note usually left out; used for emphasis or in bookish style
**Lexeme 3: есть**, INTJ
- said: /ˈjesʲtʲ/ есть (ru-RU)
  1. [B2] ответ на приказ, означающий, что он понят и будет выполнен
     - example: «Есть, товарищ командир!» — ответил солдат и побежал выполнять приказ.
     - labels: domain military
  2. [B1] возглас радости, когда что-либо удалось или получилось
     - example: Есть, наконец-то я решил эту трудную задачу!
     - labels: formality informal

### 3. тут

**Lexeme 1: тут**, ADV
- said: /ˈtut/ тут (ru-RU)
  1. [A1] в этом месте, рядом с говорящим
     - example: Подожди меня тут, я вернусь через пять минут.
     - collocations: тут и там; вот тут; тут как тут; тут недалеко
     - opposites: там
  2. [B1] в этот момент, сразу после того, что произошло
     - example: Мы уже собирались уходить, и тут зазвонил телефон.
     - collocations: и тут; тут же

### 4. даже

**Lexeme 1: даже**, PART
- said: /ˈda.ʐɨ/ да́же (ru-RU)
  1. [A2] используется, чтобы подчеркнуть неожиданность или крайнюю степень сказанного
     - example: Он так устал, что даже не снял ботинки.
     - collocations: даже не; и даже; или даже

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
- grammar: note with a subject other than the main clause's, the verb after it takes the past-tense form
- said: /ˈʂto.bɨ/ что́бы (ru-RU, чтобы), /ˈʂtop/ чтоб (ru-RU, чтоб)
- spellings: чтобы; чтоб
  1. [A2] употребляется, чтобы указать цель действия
     - example: Я встаю рано, чтобы успеть на первый автобус.
     - frames: чтобы + инф.; для того чтобы + инф.; с тем чтобы + инф.
  2. [B1] вводит то, чего желают, требуют или о чём просят
     - example: Мама попросила, чтобы мы не шумели после десяти.
**Lexeme 2: чтобы**, PART
- said: /ˈʂto.bɨ/ что́бы (ru-RU, чтобы), /ˈʂtop/ чтоб (ru-RU, чтоб)
- spellings: чтобы; чтоб
  1. [B2] употребляется для выражения пожелания, требования или предостережения
     - example: Чтобы к ужину все были дома, понятно?
     - labels: formality informal

### 7. к

**Lexeme 1: к**, ADP
- grammar: note governs the dative; ко before some consonant clusters: ко мне, ко всем
- said: /ˈk/ к (ru-RU)
- spellings: к; ко
  1. [A1] употребляется для указания, в сторону кого или чего направлено движение
     - example: Завтра утром я обязательно пойду к зубному врачу.
     - frames: к кому/чему
     - collocations: лицом к лицу; прийти к выводу
  2. [A2] употребляется для указания срока, не позже которого что-либо происходит
     - example: Постарайся закончить свой доклад к понедельнику.
     - frames: к чему
     - collocations: к утру; к вечеру; к тому времени
  3. [B1] употребляется для указания, на кого или что направлено чувство или отношение
     - example: У моего брата с детства большой интерес к музыке.
     - frames: к кому/чему
  4. [B1] употребляется для указания, с чем что-либо соединяется или для какого случая предназначено
     - example: Мы купили к чаю свежий шоколадный торт.
     - frames: к чему
     - collocations: к чаю; к празднику

### 8. они

**Lexeme 1: они**, PRON
- said: /ɐ.ˈnʲi/ они́ (ru-RU)
- forms: их [Case=Gen,Acc, Number=Plur, Person=3]; им [Case=Dat, Number=Plur, Person=3]; ими [Case=Ins, Number=Plur, Person=3] и́ми; них [Case=Gen,Acc,Loc, Number=Plur, Person=3] (after prepositions: у них, про них, о них); ним [Case=Dat, Number=Plur, Person=3] (after prepositions: к ним); ними [Case=Ins, Number=Plur, Person=3] ни́ми (after prepositions: с ними)
  1. [A1] употребляется для указания на нескольких людей или предметов, о которых идёт речь
     - example: Мои соседи очень добрые, они всегда помогают нам.
  2. [C1] употребляется вместо «он» или «она» о ком-то одном, с подчёркнутым почтением или иронией
     - example: Барин ещё отдыхают, они велели никого не принимать.
     - labels: time dated
     - grammar: note the verb stands in the plural

### 9. фильм

**Lexeme 1: фильм**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈfʲilʲm/ фильм (ru-RU)
- forms: фильма [Case=Gen, Number=Sing] фи́льма; фильмы [Case=Nom, Number=Plur] фи́льмы; фильмов [Case=Gen, Number=Plur] фи́льмов
  1. [A1] произведение, снятое на камеру, которое смотрят на экране
     - example: Давай вечером посмотрим какой-нибудь смешной фильм дома.
     - frames: фильм о ком/о чём; фильм про кого/что
     - collocations: снять фильм; смотреть фильм; художественный фильм; документальный фильм; фильм ужасов; фильм вышел в прокат; сниматься в фильме; главная роль в фильме

### 10. против

**Lexeme 1: против**, ADP
- said: /ˈpro.tʲif/ про́тив (ru-RU)
  1. [A2] выражая борьбу, несогласие или защиту от кого-либо или чего-либо
     - example: Большинство жителей выступили против строительства нового завода.
     - frames: против кого/чего
     - collocations: выступать против; голосовать против; бороться против; ничего не иметь против; лекарство против; играть против; преступление против
     - opposites: за
  2. [B1] не так, как кто-либо хочет или как требует правило
     - example: Его отправили учиться за границу против его воли.
     - frames: против чего
     - collocations: против воли; против правил; против желания; против закона
  3. [B1] в направлении, обратном движению чего-либо
     - example: Идти против сильного ветра было очень тяжело.
     - frames: против чего
     - collocations: против ветра; плыть против течения; против часовой стрелки
  4. [B2] на другой стороне, лицом к кому-либо или чему-либо
     - example: Он сел против меня и молча открыл газету.
     - frames: против кого/чего
  5. [C1] по сравнению с кем-либо или чем-либо
     - example: В этом году урожай вырос вдвое против прошлого.
     - frames: против кого/чего
     - collocations: десять против одного
**Lexeme 2: против**, ADV
- grammar: position Pred
- said: /ˈpro.tʲif/ про́тив (ru-RU)
  1. [A2] не согласен, возражает
     - example: Если никто не против, я открою окно.
     - collocations: быть против; я не против; кто против

### 11. язык

**Lexeme 1: язык**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /jɪ.ˈzɨk/ язы́к (ru-RU)
- forms: языка [Case=Gen, Number=Sing] языка́; языки [Case=Nom, Number=Plur] языки́; языков [Case=Gen, Number=Plur] языко́в
  1. [A1] система слов и правил, которой люди пользуются для общения
     - example: Моя сестра свободно говорит на трёх иностранных языках.
     - collocations: говорить на языке; родной язык; иностранный язык; выучить язык; владеть языком; переводить с языка на язык; государственный язык; язык программирования; язык жестов
  2. [A2] подвижный орган во рту, которым чувствуют вкус и произносят звуки
     - example: Осторожно, горячий чай легко обжигает язык.
     - collocations: показать язык; высунуть язык; прикусить язык; держать язык за зубами; острый язык
  3. [B2] особый способ выражать мысли, свойственный человеку, книге или группе людей
     - example: Этот автор пишет простым и очень живым языком.
     - collocations: найти общий язык; говорить на одном языке; простым языком; сухой язык
  4. [C1] что-либо узкое и вытянутое, по форме похожее на орган во рту
     - example: Яркие языки пламени поднимались высоко над крышей сарая.
     - collocations: языки пламени; язык колокола
     - labels: figurative True
**Lexeme 2: язык**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; declension 2
- said: /jɪ.ˈzɨk/ язы́к (ru-RU)
- forms: языка [Case=Gen, Number=Sing] языка́; языки [Case=Nom, Number=Plur] языки́; языков [Case=Gen, Number=Plur] языко́в
  1. [C1] пленный, которого захватывают, чтобы получить от него сведения
     - example: Ночью разведчики должны взять языка в соседней деревне.
     - collocations: взять языка; добыть языка
     - labels: formality slang, domain military

### 12. теперь

**Lexeme 1: теперь**, ADV
- said: /tʲɪ.ˈpʲerʲ/ тепе́рь (ru-RU)
  1. [A1] в настоящее время, в отличие от прошлого
     - example: Раньше мы жили в деревне, а теперь живём в большом городе.
     - collocations: только теперь; теперь уже; до сих пор и теперь
  2. [A2] после того, что было сделано или сказано; дальше
     - example: Мы закончили с первым вопросом, а теперь перейдём ко второму.
     - collocations: а теперь

### 13. страна

**Lexeme 1: страна**, NOUN
- grammar: gender Fem; animacy Inan; countability Count; declension 1
- said: /strɐ.ˈna/ страна́ (ru-RU)
- forms: страны [Case=Gen, Number=Sing] страны́; страны [Case=Nom, Number=Plur] стра́ны; стран [Case=Gen, Number=Plur]
  1. [A1] государство вместе с его территорией, границами и населением
     - example: Этим летом мы хотим впервые поехать в другую страну.
     - collocations: по всей стране; родная страна; зарубежные страны; развивающиеся страны; покинуть страну; жить в стране; страна происхождения
  2. [B1] большая местность или часть земли, обычно далёкая
     - example: Осенью птицы улетают на юг, в тёплые страны.
     - collocations: тёплые страны; дальние страны; заморские страны

### 14. жизнь

**Lexeme 1: жизнь**, NOUN
- grammar: gender Fem; animacy Inan; countability Both; declension 3
- said: /ˈʐɨzʲnʲ/ жизнь (ru-RU)
- forms: жизни [Case=Gen, Number=Sing] жи́зни; жизни [Case=Nom, Number=Plur] жи́зни; жизней [Case=Gen, Number=Plur] жи́зней
  1. [A2] особая форма существования, присущая людям, животным и растениям
     - example: Врачи сделали всё возможное, чтобы спасти жизнь ребёнку.
     - collocations: спасти жизнь; рисковать жизнью; отдать жизнь; лишить жизни; борьба за жизнь; жизнь на Земле
     - opposites: смерть
  2. [A2] время от рождения человека до его смерти
     - example: Мой дед всю жизнь проработал на одном заводе.
     - collocations: всю жизнь; за всю жизнь; на всю жизнь; первый раз в жизни; никогда в жизни; долгая жизнь; короткая жизнь
  3. [A2] условия, быт и занятия человека или общества в какой-либо области
     - example: Студенческая жизнь в большом городе полна новых впечатлений.
     - collocations: образ жизни; личная жизнь; семейная жизнь; общественная жизнь; политическая жизнь; уровень жизни; вести жизнь; начать новую жизнь
  4. [B1] реальная действительность, то, что происходит на самом деле
     - example: В жизни всё оказалось гораздо сложнее, чем в книгах.
     - collocations: воплотить в жизнь; проводить в жизнь; в реальной жизни
  5. [B2] активное движение, бодрость и энергия в чём-либо
     - example: Утром на рынке уже вовсю кипела жизнь.
     - collocations: кипит жизнь; вдохнуть жизнь; полный жизни

### 15. быть

**Lexeme 1: быть**, VERB
- grammar: aspect Imp; valency Intr; irregular True; note Present is usually left out, except есть; negated past is stressed on не: не́ был, не́ было.
- said: /ˈbɨtʲ/ быть (ru-RU)
- forms: есть [Tense=Pres, Person=1,2,3, Number=Sing,Plur, VerbForm=Fin, Mood=Ind] (the only present form in use, for every person); буду [Tense=Fut, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] бу́ду; будешь [Tense=Fut, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] бу́дешь; будут [Tense=Fut, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] бу́дут; был [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind]; была [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] была́; было [Tense=Past, Gender=Neut, Number=Sing, VerbForm=Fin, Mood=Ind] бы́ло; были [Tense=Past, Number=Plur, VerbForm=Fin, Mood=Ind] бы́ли; будь [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin]
  1. [A1] служит для связи предмета с его признаком, названием или состоянием
     - example: Мой дедушка был врачом в маленькой сельской больнице.
     - frames: быть кем/чем; быть каким
     - collocations: быть правым; быть готовым; быть в состоянии; быть в курсе
  2. [A1] существовать, иметься в действительности
     - example: В нашем маленьком городе есть очень красивый старый парк.
     - frames: где есть кто/что
  3. [A1] находиться в чьём-либо владении или распоряжении
     - example: У моей младшей сестры есть две пушистые кошки.
     - frames: у кого есть кто/что
  4. [A1] находиться, присутствовать в каком-либо месте
     - example: Вчера вечером я был дома и смотрел старый фильм.
     - frames: быть где; быть у кого
     - collocations: быть дома; быть в гостях; быть на работе
  5. [A2] происходить, случаться в какое-либо время
     - example: Завтра в школе будет родительское собрание.
  6. [A1] служит для образования будущего времени глаголов несовершенного вида
     - example: Летом я буду работать в деревне у бабушки.
     - frames: быть + инф.
     - grammar: note only in the future forms, with an imperfective infinitive

### 16. мир

**Lexeme 1: мир**, NOUN
- grammar: gender Masc; animacy Inan; declension 2
- said: /ˈmʲir/ мир (ru-RU)
- forms: мира [Case=Gen, Number=Sing] ми́ра; миры [Case=Nom, Number=Plur] миры́; миров [Case=Gen, Number=Plur] миро́в; миру [Case=Loc, Number=Sing] миру́ (only in в миру́, outside a monastery; otherwise в ми́ре)
  1. [A1] Земля и все люди, которые на ней живут
     - example: Эту книгу перевели почти на все языки мира.
     - collocations: весь мир; во всём мире; объехать весь мир; чемпионат мира; чемпион мира; карта мира; страны мира
     - grammar: number Sing
  2. [A2] отсутствие войны, вражды и ссор между людьми или странами
     - example: Все люди на земле хотят жить в мире и покое.
     - frames: мир между кем
     - collocations: жить в мире; борьба за мир; мир во всём мире; мир и покой; сохранить мир; мир в семье
     - grammar: number Sing
     - opposites: война
  3. [B1] отдельная область жизни, природы или общества со своими особенностями
     - example: Она с детства мечтала попасть в мир моды.
     - frames: мир чего
     - collocations: внутренний мир; животный мир; растительный мир; мир искусства; научный мир; мир детства
     - grammar: countability Count
  4. [B2] вся вселенная, а также отдельная её часть или планета
     - example: Писатели-фантасты часто рассказывают о далёких мирах и их жителях.
     - collocations: иные миры; сотворение мира
     - grammar: countability Count
  5. [B2] соглашение между странами о том, чтобы закончить войну
     - example: После долгих переговоров две страны наконец заключили мир.
     - collocations: заключить мир; подписать мир; просить мира
     - grammar: number Sing
  6. [C1] обычная жизнь людей вне монастыря и церкви
     - example: До пострига монах носил в миру совсем другое имя.
     - collocations: в миру; уйти из мира
     - labels: domain religion
     - grammar: number Sing

### 17. центр

**Lexeme 1: центр**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈtsentr/ центр (ru-RU)
- forms: центра [Case=Gen, Number=Sing] це́нтра; центры [Case=Nom, Number=Plur] це́нтры; центров [Case=Gen, Number=Plur] це́нтров
  1. [A2] точка или место в самой середине чего-либо
     - example: Поставь вазу с цветами в самый центр стола.
     - frames: центр чего
     - collocations: в самом центре; центр круга; центр комнаты; центр тяжести
     - opposites: край
  2. [A1] главная, обычно самая оживлённая часть города
     - example: Мы живём далеко от центра и ездим туда на метро.
     - collocations: в центре города; ехать в центр; исторический центр; жить в центре
     - grammar: number Sing
     - opposites: окраина
  3. [B1] место, где сосредоточена важная деятельность или внимание
     - example: Новая ученица сразу оказалась в центре внимания всего класса.
     - frames: центр чего
     - collocations: центр внимания; быть в центре внимания; культурный центр; промышленный центр; в центре событий; научный центр
  4. [A2] организация или здание, где оказывают определённые услуги или ведут работу
     - example: В выходные мы ходили за покупками в новый торговый центр.
     - collocations: торговый центр; медицинский центр; учебный центр; спортивный центр; деловой центр; центр занятости

### 18. обеспечить

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

### 19. кроме

**Lexeme 1: кроме**, ADP
- said: /ˈkro.mʲɪ/ кро́ме (ru-RU)
  1. [A2] за исключением кого-либо или чего-либо
     - example: В пустой комнате не было никого, кроме старого кота.
     - frames: кроме кого/чего; кроме как + инф.
  2. [B1] сверх кого-либо или чего-либо, в дополнение к ним
     - example: Кроме английского, она свободно говорит ещё на двух языках.
     - frames: кроме кого/чего
     - collocations: кроме того

### 20. чемпионат

**Lexeme 1: чемпионат**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /tɕɪm.pʲi.ɐ.ˈnat/ чемпиона́т (ru-RU)
- forms: чемпионата [Case=Gen, Number=Sing] чемпиона́та; чемпионаты [Case=Nom, Number=Plur] чемпиона́ты; чемпионатов [Case=Gen, Number=Plur] чемпиона́тов
  1. [B1] соревнование за звание лучшего участника или лучшей команды
     - example: В следующем году наша страна примет чемпионат мира по футболу.
     - frames: чемпионат по чему; чемпионат чего по чему

### 21. главное

**Lexeme 1: главное**, NOUN
- grammar: gender Neut; animacy Inan; number Sing; declension adjectival
- said: /ˈɡlav.nə.jə/ гла́вное (ru-RU)
- forms: главного [Case=Gen, Number=Sing] гла́вного
  1. [A2] самое важное, основное в чём-либо
     - example: Я не помню подробностей, но главное понял сразу.
     - frames: главное в чём
     - collocations: самое главное; понять главное; говорить о главном; не упустить главное
- relations: derived_from главный (ADJ)
**Lexeme 2: главное**, ADV
- said: /ˈɡlav.nə.jə/ гла́вное (ru-RU)
  1. [B1] употребляется, чтобы выделить самое важное в высказывании
     - example: Отдыхай, гуляй, а главное, хорошо высыпайся перед экзаменом.
     - collocations: а главное; и главное
     - grammar: note parenthetical, set off by commas
- relations: derived_from главный (ADJ)

### 22. на

**Lexeme 1: на**, ADP
- grammar: note Takes the prepositional for place, the accusative for direction; stressed in some phrases: на́ пол, на́ гору
- said: /ˈna/ на (ru-RU)
  1. [A1] используется, чтобы указать поверхность, где кто-либо или что-либо находится
     - example: Твои ключи лежат на столе рядом с телефоном.
     - frames: на чём
  2. [A1] используется, чтобы указать место, событие или занятие, где кто-либо находится
     - example: Мама сейчас на работе и вернётся только вечером.
     - frames: на чём
     - collocations: на работе; на улице; на почте; на вокзале; на уроке; на концерте; на севере
     - grammar: note with certain nouns instead of в
  3. [A1] используется, чтобы указать поверхность, куда направлено движение
     - example: Поставь, пожалуйста, чашку на верхнюю полку.
     - frames: на что
  4. [A1] используется, чтобы указать место или событие, куда кто-либо направляется
     - example: Завтра утром мы поедем на вокзал встречать бабушку.
     - frames: на что
     - collocations: идти на работу; пойти на концерт; поехать на море; выйти на улицу
     - grammar: note with certain nouns instead of в
  5. [A2] используется, чтобы указать лицо или предмет, к которому обращены действие или чувство
     - example: Не сердись на меня, я не хотел тебя обидеть.
     - frames: смотреть на кого/что; сердиться на кого; надеяться на кого/что; отвечать на что
  6. [A2] используется, чтобы указать время, к которому относится действие
     - example: Я записался к врачу на пятницу.
     - frames: на что; на чём
     - collocations: на этой неделе; на следующий день; на Новый год; на рассвете
  7. [A1] используется, чтобы указать вид транспорта, которым пользуются в пути
     - example: Каждый день я езжу в университет на автобусе.
     - frames: на чём
     - collocations: ехать на поезде; лететь на самолёте; кататься на велосипеде; ехать на машине
  8. [A1] используется, чтобы указать язык, которым пользуются в речи или письме
     - example: Мой друг свободно говорит на трёх языках.
     - frames: на чём
     - collocations: говорить на русском языке; написать на английском
  9. [A2] используется, чтобы указать цель или назначение чего-либо
     - example: Эти деньги мы откладываем на новую машину.
     - frames: на что
     - collocations: пригласить на ужин; на память; на всякий случай
  10. [A2] используется, чтобы указать срок, который продлится результат действия
     - example: Брат уехал к друзьям на две недели.
     - frames: на что
  11. [B1] используется, чтобы указать величину разницы при сравнении
     - example: Моя сестра на три года старше меня.
     - frames: на что
  12. [B1] используется, чтобы указать средство, с помощью которого что-либо работает или делается
     - example: Эта машина работает на электричестве, а не на бензине.
     - frames: на чём
     - collocations: играть на гитаре; жарить на масле
  13. [B1] используется, чтобы указать число, которое служит делителем или множителем
     - example: Если десять разделить на два, получится пять.
     - frames: делить что на что; умножить что на что
- relations: confusable в (ADP): both mean in or at with places and events; which one a noun takes must be learned
**Lexeme 2: на**, INTJ
- said: /ˈna/ на (ru-RU)
- forms: нате [Number=Plur] на́те (said to several people)
  1. [A2] используется, когда протягивают кому-либо что-либо и предлагают взять
     - example: На, возьми мой зонтик, а то промокнешь.
     - labels: formality informal

### 23. любой

**Lexeme 1: любой**, DET
- grammar: declension adjectival
- said: /lʲu.ˈboj/ любо́й (ru-RU)
- forms: любая [Case=Nom, Gender=Fem, Number=Sing] люба́я; любое [Case=Nom, Gender=Neut, Number=Sing] любо́е; любые [Case=Nom, Number=Plur] любы́е; любого [Case=Gen, Gender=Masc,Neut, Number=Sing] любо́го
  1. [A2] какой угодно, всё равно какой из многих
     - example: Ты можешь взять с этой полки любую книгу.
     - collocations: в любом случае; любой ценой; в любое время; на любой вкус; в любую погоду; любыми средствами
**Lexeme 2: любой**, PRON
- grammar: gender Masc; animacy Anim; declension adjectival
- said: /lʲu.ˈboj/ любо́й (ru-RU)
- forms: любого [Case=Gen, Number=Sing] любо́го
  1. [B1] всякий человек, кто угодно
     - example: Любой на твоём месте поступил бы точно так же.
     - frames: любой из кого

### 24. этот

**Lexeme 1: этот**, DET
- grammar: note agrees with its noun in gender, number and case
- said: /ˈe.tət/ э́тот (ru-RU)
- forms: эта [Case=Nom, Gender=Fem, Number=Sing] э́та; это [Case=Nom, Gender=Neut, Number=Sing] э́то; эти [Case=Nom, Number=Plur] э́ти; этого [Case=Gen, Gender=Masc,Neut, Number=Sing] э́того
  1. [A1] указывает на предмет или человека рядом с говорящим
     - example: Этот стул совсем сломан, лучше возьми другой.
     - collocations: в этом году; на этой неделе; в этот раз; в этот момент
     - opposites: тот
  2. [A1] указывает на то, о чём только что говорили или скоро скажут
     - example: Мы долго обсуждали этот вопрос на собрании.
     - collocations: в этом случае; по этой причине; этот самый
**Lexeme 2: этот**, PRON
- grammar: note changes for gender, number and case like the determiner
- said: /ˈe.tət/ э́тот (ru-RU)
- forms: эта [Case=Nom, Gender=Fem, Number=Sing] э́та; это [Case=Nom, Gender=Neut, Number=Sing] э́то; эти [Case=Nom, Number=Plur] э́ти; этого [Case=Gen, Gender=Masc,Neut, Number=Sing] э́того
  1. [A2] предмет или человек рядом с говорящим или уже упомянутый, без названия
     - example: Из всех курток мне больше всего нравится вот эта.
  2. [B1] человек, которого говорящий не хочет называть по имени
     - example: Опять этот звонил и просил у тебя денег.
     - labels: formality informal, attitude disapproving

### 25. второй

**Lexeme 1: второй**, ADJ
- grammar: comparable False; note ordinal numeral; declines like an adjective with stressed ending
- said: /ftɐ.ˈroj/ второ́й (ru-RU)
  1. [A1] идущий по порядку сразу после первого
     - example: Наша новая квартира находится на втором этаже старого дома.
     - frames: второй по чему
     - collocations: во второй раз; второй этаж; второе место; вторая половина; второй класс; второй по величине; из вторых рук
  2. [B1] менее важный, не главный по значению
     - example: Для него работа всегда стояла на втором месте после семьи.
     - collocations: на втором месте; второй план; второй сорт; на вторых ролях; играть вторую скрипку
  3. [B1] ставший таким же близким или важным, как родной
     - example: Этот небольшой город давно стал для меня второй родиной.
     - collocations: вторая родина; второй дом; вторая мама; вторая натура
     - labels: figurative True

### 26. игра

**Lexeme 1: игра**, NOUN
- grammar: gender Fem; animacy Inan; declension 1
- said: /i.ˈɡra/ игра́ (ru-RU)
- forms: игры [Case=Gen, Number=Sing] игры́; игры [Case=Nom, Number=Plur] и́гры; игр [Case=Gen, Number=Plur]
  1. [A1] занятие для развлечения или отдыха, обычно по определённым правилам
     - example: Дети придумали новую игру и весь вечер играли в неё.
     - frames: игра во что
     - collocations: компьютерная игра; настольная игра; правила игры; азартные игры; ролевая игра; детская игра
     - grammar: countability Count
  2. [A2] отдельная встреча соперников в спортивном соревновании
     - example: Наша команда уверенно выиграла вчерашнюю игру со счётом три — один.
     - frames: игра с кем
     - collocations: выиграть игру; проиграть игру; Олимпийские игры; домашняя игра
     - grammar: countability Count
  3. [B1] исполнение роли на сцене или музыки на инструменте
     - example: Зрители долго хлопали, восхищаясь игрой молодого актёра.
     - frames: игра на чём
     - collocations: игра актёров; игра на гитаре; игра на пианино
     - grammar: countability Mass; number Sing
  4. [B2] хитрые действия, которыми кто-либо скрыто добивается своей цели
     - example: Я сразу понял, что он ведёт со мной нечестную игру.
     - frames: игра с кем
     - collocations: вести двойную игру; опасная игра; политическая игра
     - labels: figurative True
     - grammar: countability Count
  5. [C1] быстрая смена света, красок или оттенков
     - example: Мы долго любовались игрой света на поверхности воды.
     - collocations: игра света; игра красок; игра воображения
     - grammar: countability Count; number Sing
- relations: derived_from играть (VERB)

### 27. город

**Lexeme 1: город**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈɡo.rət/ го́род (ru-RU)
- forms: города [Case=Gen, Number=Sing] го́рода; города [Case=Nom, Number=Plur] города́; городов [Case=Gen, Number=Plur] городо́в
  1. [A1] большой населённый пункт с улицами, домами, заводами и магазинами
     - example: Я родился в маленьком городе на берегу реки.
     - collocations: родной город; крупный город; центр города; жить в городе; уехать за город; жить за городом; столичный город; провинциальный город
  2. [B2] центральная часть населённого пункта в отличие от окраин
     - example: Мы живём на окраине, а в город ездим только по выходным.
     - collocations: поехать в город
     - labels: formality informal

### 28. без

**Lexeme 1: без**, ADP
- said: /ˈbʲes/ без (ru-RU)
- spellings: без; безо
  1. [A1] при отсутствии кого-либо или чего-либо, не имея их
     - example: Я обычно пью кофе без сахара и без молока.
     - frames: без кого/чего
     - collocations: без сомнения; без труда; без проблем; без исключения; без остановки; без конца; без помощи; без предупреждения
     - opposites: с
  2. [A2] меньше указанного часа на столько-то минут
     - example: Поезд отправляется ровно без пятнадцати девять утра.
     - frames: без чего
     - collocations: без четверти; без пяти; без малого

### 29. огромный

**Lexeme 1: огромный**, ADJ
- said: /ɐ.ˈɡrom.nɨj/ огро́мный (ru-RU)
  1. [A2] очень большой по размеру, количеству или силе
     - example: За нашим домом начинается огромный густой лес.
     - opposites: крошечный

### 30. подарок

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

### 31. управление

**Lexeme 1: управление**, NOUN
- grammar: gender Neut; animacy Inan; declension 2
- said: /u.prɐ.ˈvlʲe.nʲɪ.jə/ управле́ние (ru-RU)
- forms: управления [Case=Gen, Number=Sing] управле́ния; управления [Case=Nom, Number=Plur] управле́ния; управлений [Case=Gen, Number=Plur] управле́ний
  1. [B1] руководство работой людей, организации, страны или хозяйства
     - example: Отец много лет занимается управлением крупной строительной компанией.
     - frames: управление чем; под управлением кого
     - collocations: органы управления; государственное управление; система управления; управление государством; управление персоналом; взять на себя управление
     - grammar: countability Mass
  2. [B1] действия, с помощью которых ведут машину или заставляют работать механизм
     - example: На скользкой дороге водитель потерял управление и съехал в кювет.
     - frames: управление чем
     - collocations: потерять управление; не справиться с управлением; пульт управления; панель управления; дистанционное управление; управление автомобилем
     - grammar: countability Mass
  3. [B2] учреждение или отдел, который руководит какой-либо областью работы
     - example: Моя сестра уже пять лет работает в городском управлении образования.
     - frames: управление чего; управление по чему
     - collocations: главное управление; начальник управления; управление внутренних дел
     - grammar: countability Count
- relations: verbal_noun_of управлять (VERB)

### 32. следующий

**Lexeme 1: следующий**, ADJ
- grammar: comparable False
- said: /ˈslʲe.du.ju.ɕːij/ сле́дующий (ru-RU)
  1. [A1] идущий сразу после этого по порядку или по времени
     - example: На следующей остановке нам с тобой нужно выходить.
     - collocations: в следующий раз; на следующий день; в следующем году; на следующей неделе; следующая остановка; следующим утром
     - opposites: предыдущий
  2. [B1] такой, о котором будет сказано или который будет перечислен дальше
     - example: Задание нужно выполнить следующим образом: сначала прочитайте текст.
     - collocations: следующим образом; заключаться в следующем; сказать следующее
     - grammar: note the neuter следующее is often used as a noun: сказать следующее
- relations: derived_from следовать (VERB)

### 33. очень

**Lexeme 1: очень**, ADV
- said: /ˈo.tɕɪnʲ/ о́чень (ru-RU)
  1. [A1] в большой степени, гораздо сильнее обычного
     - example: Сегодня на улице очень холодно, так что надень шапку.
     - collocations: очень приятно; очень хотеть; очень нравиться; очень любить; очень много; очень хорошо; очень жаль
  2. [A2] хуже, чем хотелось бы, посредственно, так себе
     - example: Честно говоря, этот новый фильм мне показался не очень.
     - collocations: не очень; чувствовать себя не очень
     - labels: formality informal
     - grammar: note only with не, usually after a verb or as the predicate

### 34. все

**Lexeme 1: все**, PRON
- grammar: number Plur
- said: /ˈfsʲe/ все (ru-RU)
- forms: всех [Case=Gen,Acc,Loc, Number=Plur]; всем [Case=Dat, Number=Plur]; всеми [Case=Ins, Number=Plur] все́ми
  1. [A1] каждый человек из какой-либо группы, без исключения
     - example: Все уже пришли, так что можно начинать собрание.
     - collocations: все вместе; все без исключения; все до одного; лучше всех; больше всех
     - opposites: никто
**Lexeme 2: всё**, PRON
- grammar: gender Neut; number Sing
- said: /ˈfsʲo/ всё (ru-RU)
- spellings: всё; все
- forms: всего [Case=Gen, Number=Sing, Gender=Neut] всего́; всему [Case=Dat, Number=Sing, Gender=Neut] всему́; всем [Case=Ins, Number=Sing, Gender=Neut]; всём [Case=Loc, Number=Sing, Gender=Neut]
  1. [A1] любая вещь или любое дело без исключения
     - example: Не волнуйся, я всё понял с первого раза.
     - collocations: всё в порядке; больше всего; прежде всего; всё на свете; всё вместе
     - opposites: ничто
  2. [B1] самое главное и самое дорогое в жизни
     - example: Эта работа для него — всё, без неё он себя не представляет.
     - frames: всё для кого
     - labels: figurative True
**Lexeme 3: всё**, ADV
- said: /ˈfsʲo/ всё (ru-RU)
- spellings: всё; все
  1. [B1] постоянно, в течение долгого времени
     - example: Старик всё сидит у окна и смотрит на дорогу.
     - collocations: всё время
  2. [B1] до сих пор, до настоящего момента
     - example: Дождь всё не кончается, хотя уже наступил вечер.
     - collocations: всё ещё
  3. [B1] постепенно, с каждым разом в большей степени
     - example: С каждым днём на улице становится всё холоднее.
     - collocations: всё больше; всё лучше; всё чаще
     - grammar: note before a comparative
**Lexeme 4: всё**, INTJ
- said: /ˈfsʲo/ всё (ru-RU)
- spellings: всё; все
  1. [A2] употребляется, чтобы сказать, что что-то закончилось или решено окончательно
     - example: Всё, хватит спорить, мы уезжаем завтра утром.
     - labels: formality informal

### 35. русский

**Lexeme 1: русский**, ADJ
- said: /ˈrus.kʲij/ ру́сский (ru-RU)
  1. [A1] принадлежащий России или её основному народу, их языку и культуре
     - example: Вечером мы попробовали блюда русской кухни в маленьком ресторане.
     - collocations: русский язык; русская литература; русская кухня; русский народ; русская культура; русская душа; русская зима
**Lexeme 2: русский**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; declension adjectival; note declines like an adjective; a woman is русская
- said: /ˈrus.kʲij/ ру́сский (ru-RU)
- forms: русского [Case=Gen, Number=Sing] ру́сского; русские [Case=Nom, Number=Plur] ру́сские; русских [Case=Gen, Number=Plur] ру́сских
  1. [A1] человек, принадлежащий к основному народу России
     - example: Среди туристов в этом отеле всегда много русских.
- relations: derived_from русский (ADJ)
**Lexeme 3: русский**, NOUN
- grammar: gender Masc; animacy Inan; number Sing; declension adjectival; note short for русский язык, mostly in speech
- said: /ˈrus.kʲij/ ру́сский (ru-RU)
- forms: русского [Case=Gen, Number=Sing] ру́сского
  1. [A1] язык, на котором говорят в России и многих других странах
     - example: Моя подруга считает, что русский совсем не трудный.
     - collocations: учить русский; знать русский; урок русского; учитель русского; экзамен по русскому
- relations: derived_from русский (ADJ)

### 36. конец

**Lexeme 1: конец**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /kɐ.ˈnʲets/ коне́ц (ru-RU)
- forms: конца [Case=Gen, Number=Sing] конца́; концы [Case=Nom, Number=Plur] концы́; концов [Case=Gen, Number=Plur] концо́в
  1. [A1] последняя часть или момент чего-либо, после которого оно прекращается
     - example: В конце фильма все герои снова встречаются в старом доме.
     - frames: конец чего
     - collocations: в конце концов; подойти к концу; положить конец; довести до конца; до конца; под конец; без конца; конец света
     - opposites: начало
  2. [A2] место, где предмет или пространство заканчивается
     - example: Наш дом стоит в самом конце длинной улицы.
     - frames: конец чего
     - collocations: на другом конце; из конца в конец; конец верёвки; острый конец
     - opposites: начало
  3. [B1] путь от одного места до другого в одну сторону
     - example: Дорога до работы занимает у меня час в один конец.
     - collocations: в один конец; в оба конца; билет в один конец
  4. [B2] гибель, смерть или полная неудача
     - example: Если начальник узнает об этой ошибке, нам конец.
     - frames: конец кому
     - collocations: пришёл конец
     - labels: formality informal
     - grammar: note usually as a predicate with the dative

### 37. за

**Lexeme 1: за**, ADP
- grammar: note Takes the accusative or the instrumental; stress may move onto за: за́ город, за́ руку, за́ ногу
- said: /ˈza/ за (ru-RU)
  1. [A1] позади кого-либо или чего-либо или по другую сторону от него
     - example: Солнце медленно опустилось за лес, и стало прохладно.
     - frames: за кого/что; за кем/чем
     - collocations: за границей; уехать за город; жить за городом; за углом; за окном; за спиной
  2. [A2] по причине чего-либо, что вызывает благодарность, похвалу или наказание
     - example: Спасибо тебе за помощь и за терпение.
     - frames: за что; за кого
     - collocations: благодарить за помощь; извините за беспокойство; наказать за опоздание; любить за доброту
  3. [B1] в поддержку кого-либо или чего-либо
     - example: Большинство жителей проголосовали за строительство нового парка.
     - frames: за кого/что
     - collocations: болеть за команду; голосовать за кандидата; бороться за свободу; выступать за мир
     - opposites: против
  4. [A2] в течение указанного срока, нужного для какого-либо действия
     - example: Я прочитал эту толстую книгу всего за два дня.
     - frames: за что
     - collocations: за последнее время; за всю жизнь; за один день
  5. [A1] следом, вслед за кем-либо или чем-либо движущимся
     - example: Иди за мной, я покажу тебе дорогу.
     - frames: за кем/чем
     - collocations: день за днём; один за другим; друг за другом; идти следом за
  6. [A2] чтобы взять, принести или позвать кого-либо или что-либо
     - example: Мама послала сына в магазин за хлебом.
     - frames: за кем/чем
     - collocations: сходить за хлебом; зайти за другом; послать за врачом; стоять в очереди за билетами
  7. [A2] в обмен на какую-либо плату или ценность
     - example: Он продал свой старый велосипед за пять тысяч рублей.
     - frames: за что
     - collocations: купить за бесценок; работать за деньги; за свой счёт
  8. [B1] указывает на часть, за которую берут, держат или тянут
     - example: Девочка крепко держала отца за руку.
     - frames: за кого/что
     - collocations: взять за руку; держаться за поручень; схватить за рукав
  9. [B1] во время какого-либо занятия
     - example: Мы долго говорили о жизни за ужином.
     - frames: за чем
     - collocations: за обедом; за работой; сидеть за столом; сидеть за рулём; за чашкой чая
  10. [B1] вместо кого-либо, заменяя его
     - example: Сегодня я поработаю за тебя, а ты отдохни.
     - frames: за кого
     - collocations: работать за двоих; есть за троих
  11. [B1] больше какого-либо числа или позже какого-либо времени
     - example: Нашему соседу уже за шестьдесят, но он каждый день бегает.
     - frames: за что
     - collocations: далеко за полночь; засидеться за полночь
  12. [B1] раньше какого-либо события на указанный срок
     - example: Приходите за полчаса до начала спектакля.
     - frames: за что до чего
  13. [B2] на указанном расстоянии от чего-либо
     - example: Огни деревни были видны за несколько километров.
     - frames: за что от чего
**Lexeme 2: за**, ADV
- grammar: position Pred
- said: /ˈza/ за (ru-RU)
  1. [B1] согласен с предложением, поддерживает его
     - example: Если все за, то завтра утром едем на озеро.
     - opposites: против

### 38. над

**Lexeme 1: над**, ADP
- grammar: note надо is used before some consonant clusters: надо мной, надо всем
- said: /ˈnat/ над (ru-RU, над), /ˈna.də/ на́до (ru-RU, надо)
- spellings: над; надо
  1. [A1] в месте, которое находится выше кого-либо или чего-либо
     - example: Над нашим городом весь день висели тёмные тучи.
     - frames: над кем/чем
     - collocations: над головой; над землёй; над уровнем моря; над столом
     - opposites: под
  2. [A2] употребляется, чтобы указать предмет, на который направлены действие или мысль
     - example: Мы уже второй месяц работаем над этим трудным проектом.
     - frames: работать над чем; думать над чем; смеяться над кем/чем; задуматься над чем; шутить над кем/чем
  3. [B1] употребляется, чтобы указать того, кем управляют или кого побеждают
     - example: Наша команда одержала уверенную победу над сильным соперником.
     - frames: власть над кем/чем; победа над кем/чем; контроль над кем/чем; превосходство над кем/чем
     - collocations: одержать победу над; взять верх над; потерять контроль над

### 39. время

**Lexeme 1: время**, NOUN
- grammar: gender Neut; animacy Inan; irregular True; note declines like имя: времени, временем; plural времена, времён
- said: /ˈvrʲe.mʲə/ вре́мя (ru-RU)
- forms: времени [Case=Gen, Number=Sing] вре́мени; временем [Case=Ins, Number=Sing] вре́менем; времена [Case=Nom, Number=Plur] времена́; времён [Case=Gen, Number=Plur] времён
  1. [A2] непрерывная смена событий от прошлого к будущему
     - example: Время летит незаметно, когда занимаешься любимым делом.
     - collocations: время идёт; время летит; со временем; время покажет; время лечит
     - grammar: countability Mass, mainly
  2. [A1] промежуток, в течение которого что-либо происходит или можно что-либо делать
     - example: У меня совсем нет времени на спорт.
     - frames: время на что; время для чего; время + инф.
     - collocations: свободное время; проводить время; тратить время; терять время; долгое время; некоторое время; всё время; за это время
     - grammar: countability Mass, mainly
  3. [A1] момент, когда что-либо происходит, или час суток
     - example: Позвони мне в любое время, я ещё не сплю.
     - frames: время чего
     - collocations: сколько времени; в любое время; в это время; местное время; точное время; в то же время
     - grammar: countability Mass, mainly
  4. [B1] период истории или жизни со своими особенностями
     - example: В те времена люди путешествовали гораздо медленнее.
     - frames: во времена кого/чего
     - collocations: в наше время; в те времена; во все времена; военное время; старые времена; трудные времена
     - grammar: countability Both
  5. [B1] подходящий или нужный момент для какого-либо действия
     - example: Уже поздно, детям давно время спать.
     - frames: время + инф.; время чего
     - collocations: самое время; пришло время; не время
     - grammar: countability Mass, mainly; note often used as a predicate with an infinitive
  6. [B1] грамматическая форма глагола, указывающая, когда происходит действие
     - example: В этом предложении глагол стоит в прошедшем времени.
     - collocations: настоящее время; прошедшее время; будущее время
     - labels: domain grammar
     - grammar: countability Count

### 40. до

**Lexeme 1: до**, ADP
- said: /ˈdo/ до (ru-RU)
  1. [A1] используется, чтобы указать место, где кончается движение или расстояние
     - example: Мы доехали на автобусе до самого центра города.
     - frames: до кого/чего; от чего до чего
     - collocations: от начала до конца; дойти до дома; довести до двери
  2. [A1] используется, чтобы указать момент, когда кончается действие или состояние
     - example: Магазин на углу работает до десяти часов вечера.
     - frames: до чего; с чего до чего
     - collocations: до вечера; до утра; до сих пор; до конца жизни; до завтра
  3. [A2] используется, чтобы указать время раньше какого-либо события
     - example: Постарайся закончить эту работу до обеда, пожалуйста.
     - frames: до чего; за что до чего
     - collocations: до войны; до обеда; до нашей эры; задолго до; незадолго до
     - opposites: после
  4. [B1] используется, чтобы указать предел степени или количества
     - example: Этот концертный зал вмещает до пятисот зрителей.
     - frames: до чего
     - collocations: до слёз; промокнуть до нитки; до глубины души; до смерти
**Lexeme 2: до**, NOUN
- grammar: gender Neut; animacy Inan; countability Count; invariable True
- said: /ˈdo/ до (ru-RU)
  1. [B2] название первой ступени музыкальной гаммы
     - example: Певица легко взяла верхнее до в конце арии.
     - collocations: до мажор; до минор; верхнее до
     - labels: domain music

### 41. хорошо

**Lexeme 1: хорошо**, ADV
- said: /xə.rɐ.ˈʂo/ хорошо́ (ru-RU)
- forms: лучше [Degree=Cmp] лу́чше
  1. [A1] так, как нужно, умело и без недостатков
     - example: Моя сестра хорошо говорит по-английски и по-немецки.
     - collocations: хорошо себя чувствовать; хорошо выглядеть; хорошо знать; хорошо учиться; хорошо спать; хорошо говорить; очень хорошо
     - opposites: плохо
  2. [A1] о том, что приятно, удобно или полезно кому-либо
     - example: Летом в деревне очень хорошо и спокойно.
     - frames: кому хорошо; хорошо + инф.; хорошо, что ...
     - collocations: мне хорошо; как хорошо; хорошо, что
     - grammar: note used as a predicate: мне хорошо, на улице хорошо
     - opposites: плохо
- relations: adverb_of хороший (ADJ)
**Lexeme 2: хорошо**, PART
- said: /xə.rɐ.ˈʂo/ хорошо́ (ru-RU)
  1. [A1] используется, чтобы выразить согласие или разрешение
     - example: Хорошо, я позвоню тебе сразу после работы.
     - collocations: ну хорошо
**Lexeme 3: хорошо**, NOUN
- grammar: gender Neut; animacy Inan; countability Count; invariable True
- said: /xə.rɐ.ˈʂo/ хорошо́ (ru-RU)
  1. [B1] оценка «четыре» по пятибалльной системе
     - example: На экзамене по истории брат получил хорошо.
     - collocations: получить хорошо; сдать на хорошо
     - labels: domain education

### 42. прийти

**Lexeme 1: прийти**, VERB
- grammar: aspect Perf; valency Intr; conjugation 1; irregular True
- said: /prʲɪj.ˈtʲi/ прийти́ (ru-RU)
- spellings: прийти; придти [older books only]
- forms: приду [Tense=Fut, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] приду́; придёшь [Tense=Fut, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] придёшь; придут [Tense=Fut, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] приду́т; пришёл [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] пришёл; пришла [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] пришла́; приди [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] приди́
  1. [A1] добраться до какого-либо места и оказаться там
     - example: Завтра я приду к тебе после работы.
     - frames: прийти куда; прийти к кому; прийти откуда; прийти + инф.
     - collocations: прийти домой; прийти в гости; прийти вовремя; прийти на помощь; прийти первым
     - opposites: уйти
  2. [A2] прибыть по назначению или быть доставленным получателю
     - example: Сегодня утром мне пришло письмо от старого друга.
     - frames: прийти кому
     - collocations: пришло письмо; пришло сообщение; поезд пришёл
  3. [B1] наступить, начаться в свой срок
     - example: Наконец пришла весна, и снег быстро растаял.
     - collocations: пришла весна; пришло время; пришла беда
  4. [B1] достичь какого-либо решения или результата после размышлений
     - example: Учёные пришли к выводу, что опыт нужно повторить.
     - frames: прийти к чему
     - collocations: прийти к выводу; прийти к соглашению; прийти к власти
  5. [B2] оказаться в каком-либо состоянии
     - example: Отец придёт в ярость, когда узнает об этом.
     - frames: прийти во что
     - collocations: прийти в себя; прийти в ярость; прийти в восторг; прийти в негодность
  6. [B1] неожиданно появиться в мыслях
     - example: Вчера вечером мне пришла в голову отличная идея.
     - frames: прийти кому в голову
     - collocations: прийти в голову; прийти на ум
- relations: aspect_partner приходить (VERB)

### 43. право

**Lexeme 1: право**, NOUN
- grammar: gender Neut; animacy Inan; declension 2
- said: /ˈpra.və/ пра́во (ru-RU)
- forms: права [Case=Gen, Number=Sing] пра́ва; права [Case=Nom, Number=Plur] права́; прав [Case=Gen, Number=Plur]
  1. [A2] возможность что-либо делать или получать, которую дают закон или правила
     - example: Каждый гражданин имеет право на бесплатное образование.
     - frames: право на что; право + инф.; право кого/чего
     - collocations: иметь право; права человека; право голоса; лишить права; нарушать права; защищать права; получить право; по праву
     - grammar: countability Count
  2. [B2] система законов, действующих в государстве, и наука о ней
     - example: Моя старшая сестра изучает международное право в университете.
     - collocations: международное право; гражданское право; уголовное право; изучать право; нормы права
     - grammar: countability Mass; number Sing
  3. [B1] документ, который разрешает человеку водить машину
     - example: Сын наконец сдал экзамен и получил водительские права.
     - collocations: водительские права; получить права; сдать на права; лишить прав
     - labels: formality informal
     - grammar: countability Count; number Plur; note in this sense only in the plural, права́
**Lexeme 2: право**, PART
- said: /ˈpra.və/ пра́во (ru-RU)
  1. [C1] используется, чтобы подчеркнуть искренность сказанного
     - example: Я, право, не знаю, что вам ответить на это.
     - labels: formality informal

### 44. советский

**Lexeme 1: советский**, ADJ
- grammar: comparable False
- said: /sɐ.ˈvʲet.skʲɪj/ сове́тский (ru-RU)
  1. [B1] принадлежащий СССР, созданный в СССР или существовавший в нём
     - example: Мой дедушка всю жизнь проработал инженером на советском заводе.
     - collocations: Советский Союз; советская власть; советское время; советские времена; советский человек; бывший советский; советская эпоха
- relations: derived_from совет (NOUN)

### 45. бывший

**Lexeme 1: бывший**, ADJ
- grammar: position Attr; comparable False
- said: /ˈbɨf.ʂɨj/ бы́вший (ru-RU)
  1. [A2] который был кем-либо или чем-либо раньше, но теперь уже не является
     - example: На вечер встречи пришли почти все бывшие ученики нашего класса.
     - collocations: бывший муж; бывшая жена; бывший президент; бывший сотрудник; бывший Советский Союз; бывший чемпион
     - opposites: нынешний
- relations: derived_from быть (VERB)
**Lexeme 2: бывший**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; declension adjectival
- said: /ˈbɨf.ʂɨj/ бы́вший (ru-RU)
- forms: бывшего [Case=Gen, Number=Sing] бы́вшего; бывшие [Case=Nom, Number=Plur] бы́вшие; бывших [Case=Gen, Number=Plur] бы́вших
  1. [B1] мужчина, с которым раньше были в браке или любовных отношениях
     - example: Вчера в магазине я случайно встретила своего бывшего.
     - labels: formality informal
- relations: derived_from бывший (ADJ)
