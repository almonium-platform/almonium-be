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
  1. [A2] полностью, ни в какой мере и ни при каких условиях
     - example: Я вообще ничего не понял из его долгого объяснения.
     - collocations: вообще не; вообще ничего; вообще никогда; вообще никто
     - grammar: note usually with a negative
  2. [B1] в целом, не касаясь отдельных случаев и подробностей
     - example: Мы говорили не только о нашем городе, а о жизни вообще.
     - collocations: вообще говоря; и вообще
  3. [B1] обычно, как правило, в большинстве случаев
     - example: Вообще я встаю рано, но сегодня почему-то проспал.
  4. [B2] используется в разговоре для усиления оценки или чувства
     - example: Этот новый фильм вообще классный, обязательно посмотри его!
     - labels: formality informal

### 2. есть

**Lexeme 1: есть**, VERB
- grammar: aspect Imp; valency Both; irregular True
- said: /ˈjesʲtʲ/ есть (ru-RU)
- forms: ем [Tense=Pres, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind]; ешь [Tense=Pres, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind]; ест [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind]; едим [Tense=Pres, Person=1, Number=Plur, VerbForm=Fin, Mood=Ind] еди́м; едите [Tense=Pres, Person=2, Number=Plur, VerbForm=Fin, Mood=Ind] еди́те; едят [Tense=Pres, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] едя́т; ел [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind]; ела [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] е́ла; ешь [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin]
  1. [A1] принимать пищу, жуя и глотая что-либо
     - example: По воскресеньям мы всегда едим суп всей семьёй.
     - frames: есть что; есть чем
     - collocations: хотеть есть; есть с аппетитом; есть досыта; есть руками; есть ложкой; есть мясо
  2. [B2] вызывать жжение и боль в глазах или на коже
     - example: Отойди от костра, а то дым ест глаза.
     - frames: есть что
     - collocations: дым ест глаза; мыло ест глаза
     - grammar: valency Tr
  3. [C1] постоянно мучить кого-либо упрёками и придирками
     - example: Начальник ест нас поедом за каждую мелкую ошибку.
     - frames: есть кого
     - collocations: есть поедом
     - labels: formality informal, figurative True
     - grammar: valency Tr
- relations: aspect_partner съесть (VERB)
**Lexeme 2: есть**, VERB
- grammar: aspect Imp; valency Intr; invariable True; position Pred; note present of быть; one form for every person and number
- said: /ˈjesʲtʲ/ есть (ru-RU)
  1. [A1] имеется, существует, находится в наличии
     - example: У меня есть старший брат и две младшие сестры.
     - frames: у кого есть кто/что; где есть кто/что
     - collocations: есть время; есть возможность; есть вопрос; так и есть
  2. [B1] употребляется в значении «является, представляет собой»
     - example: Вот это и есть тот самый дом, где я вырос.
     - collocations: это и есть; как есть
     - grammar: note usually with и: это и есть; otherwise bookish
**Lexeme 3: есть**, INTJ
- said: /ˈjesʲtʲ/ есть (ru-RU)
  1. [B2] ответ на приказ: понял и выполню
     - example: Есть, товарищ командир, всё будет сделано немедленно!
     - labels: domain military
  2. [B2] восклицание радости, когда удалось добиться желаемого
     - example: Есть, наконец-то я сдал этот трудный экзамен!
     - labels: formality informal

### 3. тут

**Lexeme 1: тут**, ADV
- said: /ˈtut/ тут (ru-RU)
  1. [A1] в этом месте, там, где находится говорящий
     - example: Я живу тут уже пять лет и знаю всех соседей.
     - collocations: вот тут; тут и там; тут рядом; тут недалеко
     - opposites: там
  2. [B1] в этот момент, сразу после того, что произошло
     - example: Мы уже собирались уходить, и тут зазвонил телефон.
     - collocations: и тут; тут же; вдруг тут
  3. [B1] в этом деле, в данном случае или положении
     - example: Тут без помощи хорошего специалиста не обойтись.
     - collocations: тут ничего не поделаешь; при чём тут
**Lexeme 2: тут**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈtut/ тут (ru-RU)
- forms: тута [Case=Gen, Number=Sing] ту́та; туты [Case=Nom, Number=Plur] ту́ты; тутов [Case=Gen, Number=Plur] ту́тов
  1. [C2] южное дерево со сладкими съедобными ягодами, похожими на ежевику
     - example: Во дворе старого дома растёт высокий тут с тёмными ягодами.

### 4. даже

**Lexeme 1: даже**, PART
- said: /ˈda.ʐɨ/ да́же (ru-RU)
  1. [A2] используется, чтобы выделить что-либо неожиданное, крайнее или особенно важное
     - example: Эту задачу легко решит даже маленький ребёнок.
     - collocations: даже не; и даже; даже если; даже когда

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
  1. [A2] употребляется для присоединения части предложения, которая называет цель действия
     - example: Я встал пораньше, чтобы успеть на первый автобус.
     - frames: чтобы + инф.; для того чтобы + инф.; чтобы ...
     - grammar: note with an infinitive when the subject is the same, otherwise with a past-tense verb
  2. [A2] употребляется для присоединения части предложения, которая называет желаемое или требуемое действие
     - example: Мама попросила, чтобы мы вернулись домой до темноты.
     - frames: хотеть, чтобы ...; просить, чтобы ...; нужно, чтобы ...
     - grammar: note the verb after it is in the past tense
  3. [B2] употребляется для присоединения части предложения о сомнительной или невозможной ситуации
     - example: Не помню, чтобы мы с ним когда-нибудь встречались.
     - frames: не может быть, чтобы ...; не думать, чтобы ...
     - grammar: note usually after a negated verb or a word of doubt
**Lexeme 2: чтобы**, PART
- said: /ˈʂto.bɨ/ что́бы (ru-RU, чтобы), /ˈʂtop/ чтоб (ru-RU, чтоб)
- spellings: чтобы; чтоб
  1. [B2] употребляется для выражения требования, приказа или сильного желания
     - example: Чтобы к вечеру вся посуда была вымыта!
     - frames: чтобы ...
     - labels: formality informal
     - grammar: note at the start of the sentence, with a past-tense verb

### 7. к

**Lexeme 1: к**, ADP
- grammar: note governs the dative; ко before мне and before some consonant clusters (ко всем, ко дню)
- said: /ˈk/ к (ru-RU, к: before a vowel or a voiceless consonant), /ˈɡ/ к (ru-RU, к: before a voiced consonant), /kɐ/ ко (ru-RU, ко)
- spellings: к; ко
  1. [A1] указывает, в сторону кого или чего направлено движение
     - example: Вечером я обязательно зайду к бабушке за ключами.
     - frames: к кому/чему
     - collocations: идти к врачу; подойти к окну; прислониться к стене; лицом к лицу; ехать к друзьям
     - opposites: от
  2. [A2] указывает на время, не позже которого что-либо происходит
     - example: Постарайся закончить этот отчёт к пятнице.
     - frames: к чему
     - collocations: к вечеру; к утру; к концу недели; к сроку; к началу
  3. [B1] указывает на цель или назначение чего-либо
     - example: Мы долго и серьёзно готовились к этому экзамену.
     - frames: к чему
     - collocations: готовиться к экзамену; подарок к празднику; прийти к выводу; призыв к действию
  4. [B1] указывает, на кого или что направлено чувство или отношение
     - example: У нашего сына очень рано появился интерес к музыке.
     - frames: к кому/чему
     - collocations: любовь к родине; интерес к музыке; уважение к старшим; отношение к делу; привыкнуть к чему-либо
  5. [B1] указывает, к чему что-либо добавляется или чему принадлежит
     - example: Прибавь к этой сумме ещё сто рублей.
     - frames: к чему
     - collocations: к тому же; ключ к двери; относиться к группе
  6. [A2] указывает на чувство или последствие, которое вызывает событие
     - example: К сожалению, сегодня я не смогу прийти на встречу.
     - frames: к чему
     - collocations: к сожалению; к счастью; к несчастью; к удивлению

### 8. они

**Lexeme 1: они**, PRON
- grammar: note third person plural; after a preposition the oblique forms take н-: у них, к ним, с ними
- said: /ɐ.ˈnʲi/ они́ (ru-RU)
- forms: их [Case=Gen,Acc, Person=3, Number=Plur]; им [Case=Dat, Person=3, Number=Plur]; ими [Case=Ins, Person=3, Number=Plur] и́ми; них [Case=Gen,Acc,Loc, Person=3, Number=Plur] (after a preposition: у них, про них, о них); ним [Case=Dat, Person=3, Number=Plur] (after a preposition: к ним); ними [Case=Ins, Person=3, Number=Plur] ни́ми (after a preposition: с ними)
  1. [A1] используется, чтобы указать на людей или предметы, о которых уже говорилось
     - example: Мои друзья живут далеко, но они часто мне звонят.
  2. [C2] используется вместо он или она, чтобы почтительно сказать об одном человеке
     - example: Барыня просили передать, что они сегодня не принимают.
     - labels: time dated, formality informal
     - grammar: note the verb stands in the plural

### 9. фильм

**Lexeme 1: фильм**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈfʲilʲm/ фильм (ru-RU)
- forms: фильма [Case=Gen, Number=Sing] фи́льма; фильмы [Case=Nom, Number=Plur] фи́льмы; фильмов [Case=Gen, Number=Plur] фи́льмов
  1. [A1] произведение из снятых камерой сцен, которое показывают на экране
     - example: Давай в субботу сходим в кино на новый фильм.
     - frames: фильм о ком/чём; фильм про кого/что
     - collocations: снимать фильм; смотреть фильм; художественный фильм; документальный фильм; фильм ужасов; вышел фильм; режиссёр фильма; главная роль в фильме

### 10. против

**Lexeme 1: против**, ADP
- said: /ˈpro.tʲif/ про́тив (ru-RU)
  1. [A2] в борьбе, споре или несогласии с кем-либо или чем-либо
     - example: Жители города выступили против строительства нового завода.
     - frames: против кого/чего
     - collocations: выступать против; бороться против; голосовать против; иметь что-то против; средство против; лекарство против; играть против
     - opposites: за
  2. [B1] не считаясь с чьим-либо желанием, правилом или законом
     - example: Он поступил в этот институт против воли родителей.
     - frames: против чего
     - collocations: против воли; против желания; против правил; против закона
  3. [B1] навстречу движению чего-либо, в обратную сторону
     - example: Нам пришлось долго идти против сильного ветра.
     - frames: против чего
     - collocations: против течения; против ветра; против часовой стрелки; против шерсти
  4. [B2] прямо перед кем-либо или чем-либо, лицом к нему
     - example: Против нашего дома недавно открыли маленькое кафе.
     - frames: против кого/чего
  5. [C1] в сравнении с кем-либо или чем-либо
     - example: В этом году урожай вырос вдвое против прошлого.
     - frames: против кого/чего
**Lexeme 2: против**, ADV
- grammar: position Pred
- said: /ˈpro.tʲif/ про́тив (ru-RU)
  1. [A2] употребляется, чтобы выразить несогласие или неодобрение
     - example: Если ты хочешь поехать с нами, я не против.
     - collocations: быть против; не против; ничего не иметь против; кто против

### 11. язык

**Lexeme 1: язык**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /jɪ.ˈzɨk/ язы́к (ru-RU)
- forms: языка [Case=Gen, Number=Sing] языка́; языки [Case=Nom, Number=Plur] языки́; языков [Case=Gen, Number=Plur] языко́в
  1. [A1] система слов, знаков или жестов, которой люди пользуются для общения
     - example: Мой брат свободно говорит на трёх иностранных языках.
     - frames: говорить на каком языке; перевести с какого языка на какой
     - collocations: родной язык; иностранный язык; говорить на языке; изучать язык; владеть языком; государственный язык; язык жестов; найти общий язык
  2. [A2] подвижный орган во рту, которым чувствуют вкус и произносят звуки
     - example: Малыш обжёг язык горячим чаем и заплакал.
     - collocations: показать язык; прикусить язык; обжечь язык; высунуть язык
  3. [B2] манера выражать мысли словами, свойственная человеку или тексту
     - example: Эта книга написана простым и живым языком.
     - collocations: простым языком; живой язык; сухой язык; язык писателя
  4. [C1] узкая вытянутая полоса пламени или чего-либо похожего на неё
     - example: Из окон горящего дома вырывались яркие языки пламени.
     - collocations: языки пламени; язык огня
     - grammar: number Plur, mainly
  5. [C1] металлический стержень внутри колокола, который ударяет по его стенкам
     - example: Звонарь раскачал тяжёлый язык большого колокола.
**Lexeme 2: язык**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; declension 2
- said: /jɪ.ˈzɨk/ язы́к (ru-RU)
- forms: языка [Case=Gen, Number=Sing] языка́; языки [Case=Nom, Number=Plur] языки́; языков [Case=Gen, Number=Plur] языко́в
  1. [C2] пленный, которого захватили, чтобы получить сведения о противнике
     - example: Ночью разведчики ушли за линию фронта, чтобы взять языка.
     - collocations: взять языка; добыть языка
     - labels: formality slang, domain military

### 12. теперь

**Lexeme 1: теперь**, ADV
- said: /tʲɪ.ˈpʲerʲ/ тепе́рь (ru-RU)
  1. [A1] в настоящее время, в отличие от того, что было раньше
     - example: Раньше я жил в деревне, а теперь живу в большом городе.
     - collocations: только теперь; теперь уже; а теперь
  2. [B1] используется, чтобы перейти к следующему действию или теме
     - example: А теперь давайте послушаем, что скажет наш гость.
     - collocations: а теперь

### 13. страна

**Lexeme 1: страна**, NOUN
- grammar: gender Fem; animacy Inan; countability Count; declension 1
- said: /strɐ.ˈna/ страна́ (ru-RU)
- forms: страны [Case=Gen, Number=Sing] страны́; страны [Case=Nom, Number=Plur] стра́ны; стран [Case=Gen, Number=Plur]
  1. [A1] государство с его территорией, границами и населением
     - example: Летом мы хотим поехать в какую-нибудь тёплую страну.
     - collocations: родная страна; чужая страна; зарубежная страна; развивающаяся страна; по всей стране; уехать из страны; граница страны; страна происхождения
  2. [B2] большая местность с особой природой или особыми условиями
     - example: Далеко на севере лежит суровая страна льдов и снегов.
     - frames: страна чего
  3. [B1] все люди, которые живут в одном государстве
     - example: Вся страна с волнением следила за полётом космонавтов.
     - collocations: вся страна
     - labels: figurative True

### 14. жизнь

**Lexeme 1: жизнь**, NOUN
- grammar: gender Fem; animacy Inan; declension 3
- said: /ˈʐɨzʲnʲ/ жизнь (ru-RU)
- forms: жизни [Case=Gen, Number=Sing] жи́зни; жизни [Case=Nom, Number=Plur] жи́зни; жизней [Case=Gen, Number=Plur] жи́зней
  1. [A2] состояние людей, животных и растений, пока они растут, дышат и не умерли
     - example: Учёные ищут признаки жизни на других планетах.
     - collocations: спасти жизнь; рисковать жизнью; отдать жизнь; лишить жизни; угроза жизни; борьба за жизнь; жизнь на Земле
     - grammar: countability Both
     - opposites: смерть
  2. [A2] время от рождения до смерти человека или другого существа
     - example: Бабушка всю жизнь прожила в одной маленькой деревне.
     - collocations: всю жизнь; на всю жизнь; за всю жизнь; первый раз в жизни; продолжительность жизни; прожить долгую жизнь
     - grammar: countability Count
  3. [A2] то, как человек проводит свои дни: его дела, заботы и события
     - example: После переезда в город наша жизнь сильно изменилась.
     - collocations: личная жизнь; образ жизни; семейная жизнь; уровень жизни; начать новую жизнь; условия жизни; повседневная жизнь
     - grammar: countability Mass
  4. [B1] деятельность общества или его части в какой-либо области
     - example: Он активно участвует в общественной жизни нашего района.
     - frames: жизнь чего
     - collocations: общественная жизнь; политическая жизнь; культурная жизнь; экономическая жизнь
     - grammar: countability Mass
  5. [B1] реальная действительность, в отличие от мечты, теории или вымысла
     - example: В жизни всё оказалось гораздо сложнее, чем в книгах.
     - collocations: в реальной жизни; воплотить в жизнь; провести в жизнь; такова жизнь
     - grammar: countability Mass
  6. [B2] движение, бодрость и энергия, которые чувствуются в ком-либо или где-либо
     - example: Старый парк весной снова наполняется жизнью и детскими голосами.
     - collocations: полный жизни; вдохнуть жизнь; жизнь кипит; жизнь бьёт ключом
     - grammar: countability Mass
  7. [B2] то, что для человека дороже всего и даёт ему радость
     - example: Музыка для меня — это вся жизнь.
     - labels: figurative True
     - grammar: countability Mass
- relations: derived_from жить (VERB)

### 15. быть

**Lexeme 1: быть**, VERB
- grammar: aspect Imp; valency Intr; irregular True; note No aspect partner; present forms are mostly omitted except есть.
- said: /ˈbɨtʲ/ быть (ru-RU)
- forms: буду [Tense=Fut, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] бу́ду; будешь [Tense=Fut, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] бу́дешь; будут [Tense=Fut, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] бу́дут; есть [Tense=Pres, VerbForm=Fin, Mood=Ind] (the only common present form, for any person; often left out); суть [Tense=Pres, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] (bookish and rare); был [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] (with не the stress moves: не́ был); была [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] была́; было [Tense=Past, Gender=Neut, Number=Sing, VerbForm=Fin, Mood=Ind] бы́ло; были [Tense=Past, Number=Plur, VerbForm=Fin, Mood=Ind] бы́ли; будь [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin]
  1. [A1] используется для связи подлежащего с тем, что о нём сообщается
     - example: Когда я был маленьким, я хотел стать лётчиком.
     - frames: быть кем/чем; быть каким
     - collocations: быть готовым; быть уверенным; быть правым
     - grammar: note In the present the verb is usually left out: он врач.
  2. [A1] находиться или присутствовать где-либо в какое-либо время
     - example: Весь вчерашний вечер мы были дома и смотрели кино.
     - frames: быть где; быть у кого
     - collocations: быть дома; быть на работе; быть в гостях; быть в отпуске
  3. [A1] существовать, иметься на свете или в каком-либо месте
     - example: Когда-то на этом месте был старый тенистый парк.
     - collocations: жил-был
  4. [A1] иметься у кого-либо в собственности или в распоряжении
     - example: У моей сестры есть большая белая собака.
     - frames: быть у кого
  5. [A2] происходить, случаться в какое-либо время
     - example: Завтра будет сильный дождь, так что возьми зонт.
     - collocations: будь что будет; что было, то было
  6. [A1] используется с инфинитивом, чтобы выразить действие, которое произойдёт позже
     - example: Завтра я буду работать дома весь день.
     - frames: быть + инф.
     - grammar: note Only the future forms, with an imperfective infinitive.
  7. [B1] используется с краткими причастиями для выражения страдательного залога
     - example: Этот дом был построен в начале прошлого века.
     - labels: domain grammar
     - grammar: note In the present the verb is left out: дом построен.
- relations: confusable бить (VERB): бить, to beat, differs only in и for ы

### 16. мир

**Lexeme 1: мир**, NOUN
- grammar: gender Masc; animacy Inan; declension 2
- said: /ˈmʲir/ мир (ru-RU)
- forms: мира [Case=Gen, Number=Sing] ми́ра; миры [Case=Nom, Number=Plur] миры́; миров [Case=Gen, Number=Plur] миро́в; миру [Case=Loc, Number=Sing] миру́ (only in в миру́ and на миру́; elsewhere в ми́ре)
  1. [A1] Земля со всеми странами и людьми, которые на ней живут
     - example: Эта книга переведена почти на все языки мира.
     - collocations: весь мир; во всём мире; чемпионат мира; лучший в мире; объехать весь мир; увидеть мир
     - grammar: number Sing; countability Count
  2. [A2] жизнь без войны, вражды и ссор; согласие и спокойствие
     - example: Соседи наконец перестали ссориться, и в доме наступил мир.
     - frames: мир между кем; мир с кем
     - collocations: жить в мире; мир во всём мире; борьба за мир; мир и покой; сохранить мир; мир в семье
     - grammar: number Sing; countability Mass
     - opposites: война
  3. [B1] отдельная область жизни, круг людей или явлений с общими чертами
     - example: С детства её привлекал загадочный мир театра.
     - frames: мир чего
     - collocations: внутренний мир; животный мир; растительный мир; мир искусства; мир моды; мир детства
     - grammar: countability Count
  4. [B2] вся вселенная со всем, что в ней существует
     - example: Древние люди по-разному представляли себе устройство мира.
     - collocations: сотворение мира; картина мира; иные миры; параллельный мир; устройство мира
     - grammar: countability Count
  5. [B2] соглашение между воюющими сторонами о прекращении войны
     - example: После долгой войны две страны наконец заключили мир.
     - frames: мир с кем
     - collocations: заключить мир; подписать мир; просить мира
     - grammar: countability Count
  6. [C2] жизнь людей, не связанная с церковью и монастырём
     - example: Старый монах рассказывал, как его звали в миру.
     - collocations: в миру; уйти из мира
     - labels: domain religion
     - grammar: number Sing
  7. [C2] крестьянская община в старой России и все её члены
     - example: Старую церковь крестьяне восстанавливали всем миром.
     - collocations: всем миром; на миру; сельский мир
     - labels: time historical
     - grammar: number Sing

### 17. центр

**Lexeme 1: центр**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈtsentr/ центр (ru-RU)
- forms: центра [Case=Gen, Number=Sing] це́нтра; центры [Case=Nom, Number=Plur] це́нтры; центров [Case=Gen, Number=Plur] це́нтров
  1. [A2] точка или часть посередине чего-либо, на равном расстоянии от краёв
     - example: Поставь вазу с цветами в самый центр стола.
     - frames: центр чего; в центре чего
     - collocations: центр круга; центр тяжести; центр поля; в самом центре
  2. [A1] главная средняя часть города с основными улицами и учреждениями
     - example: Мы живём далеко от центра и ездим туда на метро.
     - frames: центр чего
     - collocations: центр города; исторический центр; жить в центре; ехать в центр
     - opposites: окраина
  3. [B1] главное место, где сосредоточена какая-либо деятельность или чьё-либо внимание
     - example: Этот небольшой город когда-то был крупным центром торговли.
     - frames: центр чего
     - collocations: быть в центре внимания; оказаться в центре событий; промышленный центр; культурный центр; административный центр; крупный центр
  4. [B1] организация или здание, где занимаются определённой работой или оказывают услуги
     - example: После травмы он долго лечился в реабилитационном центре.
     - frames: центр чего; центр по чему
     - collocations: торговый центр; медицинский центр; научный центр; учебный центр; деловой центр; центр занятости
  5. [B2] высшая власть страны в отношениях с её областями и местными властями
     - example: Местные власти ждут из центра решения о новом бюджете.
     - collocations: федеральный центр; центр и регионы
     - grammar: number Sing
  6. [C1] политические силы, занимающие положение между левыми и правыми партиями
     - example: На выборах партии центра получили больше голосов, чем обычно.
     - collocations: партии центра; левый центр; правый центр
     - labels: domain politics
     - grammar: number Sing

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
  1. [A2] не включая кого-либо или что-либо в число остальных
     - example: В комнате никого не было, кроме старой серой кошки.
     - frames: кроме кого/чего
  2. [B1] в дополнение к кому-либо или чему-либо названному
     - example: Кроме английского, она хорошо знает французский и немецкий.
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
  1. [A2] самое важное, основное в каком-либо деле или явлении
     - example: В этой работе главное — внимательность и терпение.
     - frames: главное в чём
     - collocations: самое главное; понять главное; говорить о главном; не упустить главное
- relations: derived_from главный (ADJ)
**Lexeme 2: главное**, ADV
- grammar: note parenthetical, set off by commas; often а главное, и главное
- said: /ˈɡlav.nə.jə/ гла́вное (ru-RU)
  1. [B1] используется, чтобы выделить самое важное в высказывании
     - example: Не переживай за экзамен, главное, хорошо выспись накануне.
     - collocations: а главное; и главное
     - labels: formality informal
- relations: derived_from главный (ADJ)

### 22. на

**Lexeme 1: на**, ADP
- grammar: note Takes the accusative or the prepositional case; itself stressed in на́ пол, на́ ночь.
- said: /ˈna/ на (ru-RU: alone, or stressed before some nouns (на́ пол)), /nɐ/ на (ru-RU: weak form, usual in running speech, just before the stress), /nə/ на (ru-RU: weak form, two or more syllables before the stress)
  1. [A1] указывает поверхность, где что-либо находится или куда что-либо кладут
     - example: Поставь чашку на стол, а книгу оставь на диване.
     - frames: на что; на чём
  2. [A1] указывает место, событие или занятие, где кто-либо находится или куда идёт
     - example: Утром папа уехал на работу, а мама пока на рынке.
     - frames: на что; на чём
     - collocations: на работе; на улице; на кухне; на вокзале; на концерте; на севере
  3. [A2] указывает человека или предмет, к которому направлены действие или чувство
     - example: Почему ты обижаешься на меня из-за такой мелочи?
     - frames: на кого/что
  4. [A2] указывает время, когда что-либо происходит
     - example: Мы поедем к бабушке на Новый год всей семьёй.
     - frames: на что; на чём
     - collocations: на следующий день; на этой неделе; на Новый год; на днях
  5. [A1] указывает транспорт, инструмент или другое средство, с помощью которого действуют
     - example: Сестра каждый день ездит в университет на метро.
     - frames: на чём
     - collocations: ехать на поезде; играть на гитаре; говорить на русском языке; жарить на масле
  6. [A2] указывает цель или назначение чего-либо
     - example: Эти деньги я откладываю на новый велосипед.
     - frames: на что
     - collocations: на память; на ужин; на всякий случай
  7. [A2] указывает срок, в течение которого будет длиться результат действия
     - example: Мы сняли домик у озера на две недели.
     - frames: на что
  8. [B1] указывает величину разницы при сравнении или изменении
     - example: Мой брат старше меня на три года.
     - frames: на что
  9. [B1] указывает части или число при делении, а также число при умножении
     - example: Разрежь пирог на восемь кусков, чтобы всем хватило.
     - frames: на что
  10. [C1] указывает настроение или состояние, в котором кто-либо находится
     - example: Сегодня я весь день на нервах из-за экзамена.
     - frames: на чём
     - collocations: на нервах; на позитиве
     - labels: formality slang
**Lexeme 2: на**, INTJ
- said: /ˈna/ на (ru-RU)
- forms: нате [Number=Plur] на́те (said to several people or politely)
  1. [A2] употребляется, когда что-либо дают или протягивают кому-либо
     - example: На, возьми мой зонт, а то промокнешь по дороге.
     - labels: formality informal, politeness familiar

### 23. любой

**Lexeme 1: любой**, DET
- grammar: declension adjectival
- said: /lʲu.ˈboj/ любо́й (ru-RU)
  1. [A2] какой угодно, неважно какой из всех возможных
     - example: Звоните мне в любое время, я всегда готов помочь.
     - collocations: в любом случае; любой ценой; в любое время; любыми средствами; при любой погоде; в любой момент
**Lexeme 2: любой**, PRON
- grammar: declension adjectival; note declined like an adjective; mostly in the masculine singular
- said: /lʲu.ˈboj/ любо́й (ru-RU)
  1. [B1] кто угодно, каждый человек без исключения
     - example: Любой из нас мог бы оказаться на его месте.
     - frames: любой из кого

### 24. этот

**Lexeme 1: этот**, DET
- grammar: note declines like a pronoun: этого, этому, этим, об этом
- said: /ˈe.tət/ э́тот (ru-RU)
- forms: эта [Gender=Fem, Number=Sing, Case=Nom] э́та; это [Gender=Neut, Number=Sing, Case=Nom] э́то; эти [Number=Plur, Case=Nom] э́ти; этого [Gender=Masc,Neut, Number=Sing, Case=Gen] э́того; этой [Gender=Fem, Number=Sing, Case=Gen,Dat,Ins,Loc] э́той
  1. [A1] указывает на предмет или лицо рядом с говорящим или только что названное
     - example: Этот дом построил мой дед сорок лет назад.
     - collocations: вот этот; этот самый; в этом случае; по этому поводу; на этот счёт
     - opposites: тот
  2. [A2] указывает на нынешний или ближайший отрезок времени
     - example: В этом году мы поедем отдыхать на море.
     - collocations: в этом году; на этой неделе; в этом месяце; в этот раз; в этот момент
**Lexeme 2: этот**, PRON
- grammar: note declines like a pronoun: этого, этому, этим, об этом
- said: /ˈe.tət/ э́тот (ru-RU)
- forms: эта [Gender=Fem, Number=Sing, Case=Nom] э́та; это [Gender=Neut, Number=Sing, Case=Nom] э́то; эти [Number=Plur, Case=Nom] э́ти; этого [Gender=Masc,Neut, Number=Sing, Case=Gen] э́того; этой [Gender=Fem, Number=Sing, Case=Gen,Dat,Ins,Loc] э́той
  1. [A2] заменяет название предмета или лица, находящегося рядом или уже упомянутого
     - example: Возьми другой зонт, этот совсем сломан.
     - opposites: тот
  2. [B2] о человеке, которого не хотят называть по имени
     - example: И зачем ты пригласила этих на наш праздник?
     - labels: formality informal, attitude disapproving

### 25. второй

**Lexeme 1: второй**, ADJ
- grammar: comparable False; note ordinal numeral, declined like an adjective
- said: /ftɐ.ˈroj/ второ́й (ru-RU)
  1. [A1] идущий по счёту или порядку сразу после первого
     - example: Мы живём на втором этаже старого кирпичного дома.
     - collocations: второй этаж; второе место; во второй раз; вторая половина; второй класс; второй тайм
  2. [B1] ставший таким же близким и важным, как главный или настоящий
     - example: Этот маленький город давно стал для меня второй родиной.
     - collocations: вторая родина; второй дом; второе дыхание; вторая молодость
  3. [B2] менее важный или худший по сравнению с главным
     - example: Личные дела ему пришлось отодвинуть на второй план.
     - collocations: на второй план; второй сорт; люди второго сорта; второстепенная роль
     - labels: figurative True

### 26. игра

**Lexeme 1: игра**, NOUN
- grammar: gender Fem; animacy Inan; countability Count; declension 1
- said: /i.ˈɡra/ игра́ (ru-RU)
- forms: игры [Case=Gen, Number=Sing] игры́; игры [Case=Nom, Number=Plur] и́гры; игр [Case=Gen, Number=Plur]
  1. [A1] занятие для развлечения или отдыха, обычно по определённым правилам
     - example: По вечерам мы любим собираться всей семьёй за настольной игрой.
     - frames: игра в что; игра с кем/чем
     - collocations: компьютерная игра; настольная игра; детские игры; правила игры; спортивная игра; сыграть в игру; ролевая игра
  2. [A2] одна встреча соперников в спортивном соревновании
     - example: Наша команда проиграла вчерашнюю игру со счётом ноль — два.
     - frames: игра с кем
     - collocations: выиграть игру; проиграть игру; домашняя игра; Олимпийские игры; игра закончилась
  3. [B1] то, как кто-либо исполняет роль, музыку или действует в соревновании
     - example: Зрители долго аплодировали блестящей игре молодого пианиста.
     - frames: игра на чём; игра кого
     - collocations: игра актёров; игра на гитаре; игра на фортепиано; игра на скрипке; уверенная игра
  4. [B2] хитрые скрытые действия ради собственной выгоды
     - example: Он слишком поздно понял, что партнёр ведёт с ним нечестную игру.
     - frames: игра с кем/чем
     - collocations: вести игру; двойная игра; опасная игра; политическая игра; игра с огнём
     - labels: figurative True
  5. [C1] быстрая смена света, красок или выражения лица
     - example: Мы долго смотрели на игру света в каплях воды.
     - frames: игра чего
     - collocations: игра света; игра теней; игра красок; игра воображения
- relations: verbal_noun_of играть (VERB)

### 27. город

**Lexeme 1: город**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /ˈɡo.rət/ го́род (ru-RU)
- forms: города [Case=Gen, Number=Sing] го́рода; города [Case=Nom, Number=Plur] города́; городов [Case=Gen, Number=Plur] городо́в
  1. [A1] крупный населённый пункт, жители которого обычно не заняты сельским хозяйством
     - example: Мы живём в небольшом городе на берегу широкой реки.
     - collocations: родной город; большой город; центр города; мэр города; гулять по городу; жить в городе
  2. [A2] жизнь и места крупных поселений в отличие от деревни
     - example: После школы многие молодые люди переезжают из деревни в город.
     - collocations: уехать за город; жить за городом; переехать в город
     - grammar: number Sing
     - opposites: деревня
  3. [B1] центральная часть большого населённого пункта в отличие от окраин
     - example: В субботу мы поедем в город за покупками.
     - labels: formality informal
     - grammar: number Sing
  4. [B2] все люди, которые живут в одном населённом пункте
     - example: Об этой необычной свадьбе потом долго говорил весь город.
     - collocations: весь город
     - labels: figurative True
     - grammar: number Sing

### 28. без

**Lexeme 1: без**, ADP
- said: /ˈbʲes/ без (ru-RU: said alone or under stress), /bʲɪz/ без (ru-RU: weak form, before a voiced consonant or a vowel), /bʲɪs/ без (ru-RU: weak form, before a voiceless consonant)
- spellings: без; безо
  1. [A1] указывает, что кого-либо или чего-либо нет или не будет
     - example: Я всегда пью кофе без сахара и без молока.
     - frames: без кого/чего
     - collocations: без сомнения; без труда; без проблем; без исключения; без конца; без помощи; без остановки
     - grammar: note безо before some consonant clusters: безо всего, безо льда
  2. [A2] указывает, какой части не хватает до целого числа или часа
     - example: Поезд отправляется без четверти восемь, так что не опаздывай.
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
- grammar: gender Neut; animacy Inan; countability Both; declension 2
- said: /u.prɐ.ˈvlʲe.nʲɪ.jə/ управле́ние (ru-RU)
- forms: управления [Case=Gen, Number=Sing] управле́ния; управления [Case=Nom, Number=Plur] управле́ния; управлений [Case=Gen, Number=Plur] управле́ний
  1. [B1] руководство деятельностью организации, страны или группы людей
     - example: Отец решил передать управление фирмой старшему сыну.
     - frames: управление чем; под управлением кого/чего
     - collocations: государственное управление; органы управления; система управления; передать в управление; местное управление; оркестр под управлением дирижёра
     - grammar: countability Mass
  2. [B1] действия, которыми направляют движение или работу машины
     - example: На мокрой дороге водитель потерял управление и съехал в кювет.
     - frames: управление чем
     - collocations: потерять управление; не справиться с управлением; пульт управления; панель управления; дистанционное управление; управление автомобилем
     - grammar: countability Mass
  3. [B2] учреждение или его крупный отдел, ведающий какой-либо областью работы
     - example: Завтра мне нужно отнести документы в районное управление образования.
     - frames: управление чего; управление по чему
     - collocations: главное управление; управление полиции; начальник управления
     - grammar: countability Count
  4. [C1] связь слов, при которой главное слово требует от зависимого определённого падежа
     - example: На уроке мы изучали управление глаголов и предлогов.
     - labels: domain linguistics
     - grammar: countability Mass
- relations: verbal_noun_of управлять (VERB)

### 32. следующий

**Lexeme 1: следующий**, ADJ
- grammar: comparable False
- said: /ˈslʲe.du.ju.ɕːɪj/ сле́дующий (ru-RU), /ˈslʲe.du.ɕːɪj/ сле́дующий (ru-RU, casual)
  1. [A2] идущий сразу за кем-либо или чем-либо по порядку или во времени
     - example: Нам нужно выходить на следующей остановке.
     - collocations: в следующий раз; на следующий день; следующий год; следующая неделя; следующая остановка; кто следующий
     - opposites: предыдущий
  2. [B1] такой, о котором будет сказано сразу после этого
     - example: Для поступления вам понадобятся следующие документы.
     - collocations: следующим образом; дело в следующем
- relations: derived_from следовать (VERB)

### 33. очень

**Lexeme 1: очень**, ADV
- said: /ˈo.tɕɪnʲ/ о́чень (ru-RU)
  1. [A1] в большой степени, намного сильнее или больше обычного
     - example: Сегодня на улице очень холодно, поэтому надень тёплую куртку.
     - collocations: очень приятно; очень жаль; не очень; очень много; очень хорошо; очень любить; очень нравиться; очень хотеть; очень рад
     - grammar: note usually stands before an adjective, an adverb or a verb

### 34. все

**Lexeme 1: все**, PRON
- grammar: note plural only; declines like the plural of весь
- said: /ˈfsʲe/ все (ru-RU)
- forms: всех [Case=Gen,Acc,Loc, Number=Plur]; всем [Case=Dat, Number=Plur]; всеми [Case=Ins, Number=Plur] все́ми
  1. [A1] каждый человек из какой-то группы, люди без исключения
     - example: Все уже собрались в зале, так что можно начинать.
     - collocations: все вместе; все до одного; лучше всех; больше всех; раньше всех; на глазах у всех
     - opposites: никто
- relations: derived_from весь (DET)
**Lexeme 2: всё**, PRON
- grammar: note declines like the neuter singular of весь
- said: /ˈfsʲo/ всё (ru-RU)
- spellings: всё; все
- forms: всего [Case=Gen, Number=Sing, Gender=Neut] всего́; всему [Case=Dat, Number=Sing, Gender=Neut] всему́; всем [Case=Ins, Number=Sing, Gender=Neut]; всём [Case=Loc, Number=Sing, Gender=Neut]
  1. [A1] любые вещи, дела и обстоятельства без исключения
     - example: Не волнуйся, я тебе потом всё подробно объясню.
     - collocations: всё в порядке; прежде всего; больше всего; лучше всего; всё на свете; всё понятно
     - opposites: ничто
- relations: derived_from весь (DET)
**Lexeme 3: всё**, ADV
- said: /ˈfsʲo/ всё (ru-RU)
- spellings: всё; все
  1. [B1] постоянно, долгое время без перерыва
     - example: Он всё сидит у окна и молчит.
     - collocations: всё время
  2. [A2] до сих пор, по-прежнему
     - example: Уже полночь, а сын всё не возвращается домой.
     - collocations: всё ещё
  3. [B1] постепенно и непрерывно, с каждым разом сильнее
     - example: С каждым днём на улице становится всё теплее.
     - collocations: всё больше; всё больше и больше; всё лучше; всё чаще; всё меньше
     - grammar: note with a comparative
**Lexeme 4: всё**, INTJ
- said: /ˈfsʲo/ всё (ru-RU)
- spellings: всё; все
  1. [A2] используется, чтобы сказать, что что-то закончено или прекращено
     - example: Всё, на сегодня работа закончена, идём домой.
     - collocations: всё, хватит
     - labels: formality informal

### 35. русский

**Lexeme 1: русский**, ADJ
- grammar: comparable False
- said: /ˈrus.kʲɪj/ ру́сский (ru-RU)
  1. [A1] связанный с народом, основным населением России, его языком и культурой или с Россией
     - example: По вечерам я с удовольствием читаю русскую классическую литературу.
     - collocations: русский язык; русская литература; русская кухня; русская культура; русская душа; русская зима; русская народная сказка
**Lexeme 2: русский**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; declension adjectival
- said: /ˈrus.kʲɪj/ ру́сский (ru-RU)
- forms: русского [Case=Gen, Number=Sing] ру́сского; русские [Case=Nom, Number=Plur] ру́сские; русских [Case=Gen, Number=Plur] ру́сских
  1. [A2] человек, принадлежащий к народу, основному населению России
     - example: Мой дед был русским, а бабушка родилась в Грузии.
  2. [B1] житель или гражданин России, кто бы он ни был по происхождению
     - example: Летом на этом пляже отдыхает очень много русских.
     - labels: formality informal
- relations: derived_from русский (ADJ)
**Lexeme 3: русский**, NOUN
- grammar: gender Masc; animacy Inan; number Sing; declension adjectival
- said: /ˈrus.kʲɪj/ ру́сский (ru-RU)
- forms: русского [Case=Gen, Number=Sing] ру́сского
  1. [A2] язык, на котором говорит народ России; школьный предмет по этому языку
     - example: Завтра у нас будет контрольная работа по русскому.
     - collocations: урок русского; учитель русского; экзамен по русскому; учить русский; знать русский
     - labels: formality informal
- relations: derived_from русский (ADJ)

### 36. конец

**Lexeme 1: конец**, NOUN
- grammar: gender Masc; animacy Inan; countability Count; declension 2
- said: /kɐ.ˈnʲets/ коне́ц (ru-RU)
- forms: конца [Case=Gen, Number=Sing] конца́; концы [Case=Nom, Number=Plur] концы́; концов [Case=Gen, Number=Plur] концо́в
  1. [A2] последняя точка или крайняя часть предмета, места, пути
     - example: Наш дом стоит в самом конце длинной тихой улицы.
     - frames: конец чего
     - collocations: на другом конце; из конца в конец; на том конце провода; острый конец; конец верёвки
  2. [A1] последний момент или заключительная часть события, времени, рассказа
     - example: К концу лета мы обычно возвращаемся в город.
     - frames: конец чего
     - collocations: в конце концов; положить конец; подойти к концу; довести до конца; до конца; без конца; под конец; конец света; счастливый конец
     - opposites: начало
  3. [B1] смерть, гибель или полная неудача
     - example: Если начальник об этом узнает, нам всем конец.
     - frames: конец кому/чему
     - collocations: пришёл конец
     - labels: formality informal
  4. [B1] путь от одного места до другого в одну сторону
     - example: Дорога в один конец занимает у меня почти час.
     - collocations: в один конец; в оба конца; билет в один конец

### 37. за

**Lexeme 1: за**, ADP
- said: /ˈza/ за (ru-RU), /zɐ/ за (ru-RU: weak form, usual in running speech)
  1. [A1] позади кого-либо или чего-либо, по другую сторону чего-либо
     - example: Наша дача находится за рекой, недалеко от леса.
     - frames: за кем/чем; за кого/что
     - collocations: за городом; уехать за город; за границей; уехать за границу; за углом; за окном; за дверью
     - grammar: note with the instrumental for where, with the accusative for where to
     - opposites: перед
  2. [A2] около предмета, которым пользуются, когда занимаются каким-либо делом
     - example: Вечером вся семья собирается за большим столом на кухне.
     - frames: за чем; за что
     - collocations: сидеть за столом; сесть за стол; за рулём; сесть за руль; за компьютером
     - grammar: note with the instrumental for where, with the accusative for where to
  3. [A1] указывает на причину действия или чувства
     - example: Большое спасибо тебе за подарок и тёплые слова.
     - frames: за что
     - collocations: спасибо за внимание; извините за опоздание; наказать за ошибку
  4. [A2] указывает на цену или на то, что дают взамен
     - example: Я купил этот старый велосипед всего за тысячу рублей.
     - frames: за что
     - collocations: платить за квартиру; за деньги; за бесценок; за свой счёт
  5. [A2] указывает на срок, в течение которого что-либо происходит или сделано
     - example: Эту книгу можно легко прочитать за один вечер.
     - frames: за что
     - collocations: за последние годы; за это время; за всю жизнь; за один день
  6. [B1] указывает, насколько раньше события что-либо происходит
     - example: Приходите на вокзал за полчаса до отправления поезда.
     - frames: за что до чего
  7. [A2] указывает на того или то, что идут получить, принести или позвать
     - example: Сходи, пожалуйста, в магазин за молоком и хлебом.
     - frames: за кем/чем
     - collocations: пойти за хлебом; сходить за водой; послать за врачом; зайти за другом
  8. [A2] указывает на того или то, что движется или находится впереди
     - example: Собака бежала за мальчиком до самой школы.
     - frames: за кем/чем
     - collocations: один за другим; день за днём; год за годом; раз за разом; идти за толпой
  9. [B1] указывает на того или то, что наблюдают или о ком заботятся
     - example: Бабушка присматривает за внуками, пока родители на работе.
     - frames: за кем/чем
     - collocations: следить за детьми; ухаживать за больным; следить за собой; наблюдать за птицами
  10. [B1] указывает на того или то, что поддерживают или защищают
     - example: Большинство жителей проголосовало за строительство нового парка.
     - frames: за кого/что
     - collocations: голосовать за кандидата; бороться за свободу; выступать за мир; болеть за команду; выпить за здоровье
     - grammar: note also used alone after a subject to mean being in favour: я за
     - opposites: против
  11. [B1] указывает на часть, которую берут, держат или трогают
     - example: Мама крепко взяла сына за руку на переходе.
     - frames: за что
     - collocations: взять за руку; держаться за поручень; схватить за рукав
  12. [B1] указывает на того, к кому относится чувство или ответственность
     - example: Я очень рад за тебя и твою новую работу.
     - frames: за кого/что
     - collocations: переживать за детей; бояться за жизнь; отвечать за свои слова; стыдно за себя; рад за друга
  13. [B2] указывает на того, вместо кого или от имени кого действуют
     - example: Сегодня мне пришлось работать за двоих коллег.
     - frames: за кого
     - collocations: работать за двоих; расписаться за получателя
  14. [B2] указывает, кем или чем кого-либо или что-либо считают
     - example: В темноте я принял незнакомца за своего брата.
     - frames: за кого/что
     - collocations: принять за шутку; считать за честь
  15. [B2] указывает на возраст, время или предел, который уже перейдён
     - example: Нашему соседу уже за шестьдесят, но он бегает каждое утро.
     - frames: за что
     - collocations: далеко за полночь; за пределы города
  16. [B2] указывает на расстояние, на котором что-либо находится или происходит
     - example: Гром был слышен за несколько километров от деревни.
     - frames: за что от кого/чего
**Lexeme 2: за**, NOUN
- grammar: gender Neut; animacy Inan; invariable True; note usually in the phrase за и против
- said: /ˈza/ за (ru-RU)
  1. [C1] довод в пользу какого-либо решения или мнения
     - example: Прежде чем решать, нужно взвесить все за и против.
     - collocations: взвесить все за и против
     - opposites: против

### 38. над

**Lexeme 1: над**, ADP
- grammar: note надо before мной and some consonant clusters: надо мной, надо всем
- said: /ˈnat/ над (ru-RU), /nət/ над (ru-RU: weak form, usual in running speech)
- spellings: над; надо
  1. [A1] в месте, которое находится выше кого-либо или чего-либо
     - example: Над нашим городом весь день висели тяжёлые серые тучи.
     - frames: над кем/чем
     - collocations: над головой; над уровнем моря
  2. [B1] указывает на предмет, на который направлено действие или чувство
     - example: Мы уже второй месяц работаем над этим проектом.
     - frames: работать над чем; смеяться над кем/чем; думать над чем; победа над кем/чем
  3. [B2] указывает на того, кем управляют или кого превосходят
     - example: У отца не было никакой власти над взрослым сыном.
     - frames: власть над кем/чем; контроль над кем/чем; превосходство над кем/чем

### 39. время

**Lexeme 1: время**, NOUN
- grammar: gender Neut; animacy Inan; countability Both; note declines like имя, with the stem времен- outside the nominative and accusative singular
- said: /ˈvrʲe.mʲə/ вре́мя (ru-RU)
- forms: времени [Case=Gen, Number=Sing] вре́мени; временем [Case=Ins, Number=Sing] вре́менем; времена [Case=Nom, Number=Plur] времена́; времён [Case=Gen, Number=Plur] времён
  1. [A1] непрерывная смена событий, которую измеряют секундами, часами, годами
     - example: Время летит незаметно, когда занимаешься любимым делом.
     - collocations: время идёт; время летит; со временем; время покажет; время лечит
     - grammar: countability Mass; number Sing
  2. [A2] промежуток, в течение которого что-либо длится или происходит
     - example: Мы долгое время жили в маленьком городе у моря.
     - collocations: долгое время; некоторое время; в последнее время; за короткое время; всё время; время от времени
     - grammar: countability Mass; number Sing
  3. [A1] определённый момент, который показывают часы или календарь
     - example: Мы заранее договорились о времени и месте встречи.
     - frames: время чего
     - collocations: точное время; местное время; московское время; назначить время; в то время; в своё время
     - grammar: countability Mass; number Sing
  4. [A1] часы или дни, не занятые обязательными делами и доступные для чего-либо
     - example: По вечерам у меня совсем нет времени на спорт.
     - frames: время на что; время для чего; время + инф.
     - collocations: свободное время; нет времени; тратить время; терять время; найти время; проводить время; экономить время; занимать время
     - grammar: countability Mass; number Sing
  5. [B1] период в истории общества или в жизни человека
     - example: В те времена люди путешествовали гораздо медленнее, чем сейчас.
     - frames: во времена кого/чего
     - collocations: в наше время; в те времена; трудные времена; советское время; с давних времён
     - grammar: countability Count
  6. [A2] определённая часть суток или года
     - example: Осень всегда была моим любимым временем года.
     - collocations: время года; время суток; ночное время; летнее время
     - grammar: countability Count
  7. [B1] подходящий момент, когда следует что-либо делать
     - example: Уже совсем поздно, время возвращаться домой.
     - frames: время + инф.; время для чего
     - collocations: самое время; не время; всему своё время
     - grammar: countability Mass; number Sing; note often used as a predicate with an infinitive
  8. [B1] форма глагола, показывающая, когда происходит действие
     - example: В этом предложении глагол стоит в прошедшем времени.
     - collocations: прошедшее время; настоящее время; будущее время
     - labels: domain grammar
     - grammar: countability Count

### 40. до

**Lexeme 1: до**, ADP
- said: /ˈdo/ до (ru-RU), /dɐ/ до (ru-RU: weak form, usual in running speech)
  1. [A1] указывает на место, где кончается движение или расстояние
     - example: Мы дошли пешком до самой реки за полчаса.
     - frames: до кого/чего; от чего до чего
  2. [A1] указывает на время, когда действие или состояние прекращается
     - example: Магазин сегодня работает до девяти часов вечера.
     - frames: до чего; с чего до чего
     - collocations: до утра; до вечера; до завтра; до конца
  3. [A2] раньше какого-либо момента или события
     - example: Нам нужно закончить ремонт до начала зимы.
     - frames: до чего; за что до чего
     - collocations: до войны; до нашей эры
     - opposites: после
  4. [B1] указывает на предел количества, меры или степени
     - example: Этот зал вмещает до пятисот зрителей одновременно.
     - frames: до чего
     - collocations: промокнуть до нитки; до слёз; устать до смерти; до глубины души
  5. [B2] указывает на то, к чему относится интерес, желание или забота
     - example: Ему совсем нет дела до чужих проблем.
     - frames: до кого/чего
**Lexeme 2: до**, NOUN
- grammar: gender Neut; animacy Inan; countability Count; invariable True
- said: /ˈdo/ до (ru-RU)
  1. [B1] первый звук музыкальной гаммы, а также нота этого звука
     - example: Девочка медленно сыграла на пианино до первой октавы.
     - collocations: до мажор; до минор
     - labels: domain music

### 41. хорошо

**Lexeme 1: хорошо**, ADV
- grammar: comparable True
- said: /xə.rɐ.ˈʂo/ хорошо́ (ru-RU)
- forms: лучше [Degree=Cmp] лу́чше
  1. [A1] так, как нужно; умело, правильно, без ошибок
     - example: Моя сестра хорошо говорит по-французски и по-немецки.
     - collocations: хорошо знать; хорошо говорить; хорошо учиться; хорошо выглядеть; хорошо себя чувствовать; хорошо спать; хорошо относиться
     - opposites: плохо
  2. [A2] о приятном, спокойном состоянии, которое кто-либо испытывает
     - example: Мне здесь так хорошо, что не хочется уезжать.
     - frames: кому хорошо; хорошо + инф.
     - collocations: на душе хорошо
     - grammar: note used as a predicate in impersonal sentences
     - opposites: плохо
  3. [A2] об удачном, полезном или своевременном положении дел
     - example: Хорошо, что ты взял зонт: скоро начнётся дождь.
     - frames: хорошо, что ...; хорошо, если ...
     - grammar: note used as a predicate in impersonal sentences
     - opposites: плохо
  4. [B2] значительно, намного больше указанного
     - example: Ему уже хорошо за пятьдесят, но выглядит он молодо.
     - collocations: хорошо за полночь
     - labels: formality informal
- relations: adverb_of хороший (ADJ)
**Lexeme 2: хорошо**, PART
- said: /xə.rɐ.ˈʂo/ хорошо́ (ru-RU)
  1. [A1] используется, чтобы выразить согласие с собеседником
     - example: Хорошо, я позвоню тебе вечером после работы.
     - collocations: ну хорошо
  2. [C1] используется, чтобы выразить угрозу или предупреждение
     - example: Хорошо же, ты ещё пожалеешь об этих словах!
     - collocations: хорошо же
     - labels: formality informal
**Lexeme 3: хорошо**, NOUN
- grammar: gender Neut; animacy Inan; countability Count; invariable True
- said: /xə.rɐ.ˈʂo/ хорошо́ (ru-RU)
  1. [B1] оценка «четыре» по пятибалльной системе в школе или вузе
     - example: Экзамен по истории я в итоге сдал на хорошо.
     - collocations: сдать на хорошо; получить хорошо
     - labels: domain education

### 42. прийти

**Lexeme 1: прийти**, VERB
- grammar: aspect Perf; valency Intr; conjugation 1; irregular True
- said: /prʲɪj.ˈtʲi/ прийти́ (ru-RU)
- spellings: прийти; притти [older books only]
- forms: приду [Tense=Fut, Person=1, Number=Sing, VerbForm=Fin, Mood=Ind] приду́; придёшь [Tense=Fut, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] придёшь; придут [Tense=Fut, Person=3, Number=Plur, VerbForm=Fin, Mood=Ind] приду́т; пришёл [Tense=Past, Gender=Masc, Number=Sing, VerbForm=Fin, Mood=Ind] пришёл; пришла [Tense=Past, Gender=Fem, Number=Sing, VerbForm=Fin, Mood=Ind] пришла́; приди [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] приди́
  1. [A1] идя пешком или двигаясь, оказаться в каком-либо месте
     - example: Завтра вечером я обязательно приду к тебе в гости.
     - frames: прийти куда; прийти к кому; прийти откуда; прийти + инф.
     - collocations: прийти домой; прийти в гости; прийти на работу; прийти вовремя; прийти пешком; прийти на помощь
     - opposites: уйти
  2. [A2] быть доставленным получателю или прибыть к месту назначения
     - example: Вчера мне пришло длинное письмо от старого друга.
     - frames: прийти кому
     - collocations: пришло письмо; пришло сообщение; поезд пришёл
  3. [B1] начаться, наступить в свой срок
     - example: Наконец пришла весна, и снег на полях быстро растаял.
     - collocations: пришла весна; пришло время; пришла пора; пришла беда
  4. [B1] возникнуть в сознании, появиться в мыслях
     - example: Неожиданно ей пришла в голову отличная идея.
     - frames: прийти кому в голову
     - collocations: прийти в голову; прийти на ум
  5. [B2] в результате чего-либо оказаться в новом состоянии или положении
     - example: После долгого спора стороны всё-таки пришли к соглашению.
     - frames: прийти к чему; прийти во что
     - collocations: прийти к выводу; прийти к соглашению; прийти в себя; прийти в ужас; прийти в восторг; прийти к власти; прийти в негодность
- relations: aspect_partner приходить (VERB); derived_from идти (VERB)

### 43. право

**Lexeme 1: право**, NOUN
- grammar: gender Neut; animacy Inan; declension 2
- said: /ˈpra.və/ пра́во (ru-RU)
- forms: права [Case=Gen, Number=Sing] пра́ва; права [Case=Nom, Number=Plur] права́; прав [Case=Gen, Number=Plur]
  1. [A2] законная или моральная возможность делать что-либо или получать что-либо
     - example: Каждый гражданин имеет право на бесплатное образование.
     - frames: право на что; право + инф.; право кого на что
     - collocations: иметь право; права человека; право голоса; получить право; лишить права; дать право; по праву; защищать права; равные права
     - grammar: countability Count
  2. [B2] совокупность законов и норм, которые регулируют жизнь общества
     - example: Моя сестра изучает международное право в университете.
     - collocations: международное право; гражданское право; уголовное право; нормы права; изучать право
     - grammar: countability Mass; number Sing
  3. [B1] документ, который даёт возможность водить автомобиль
     - example: Через месяц я наконец получу права и куплю машину.
     - collocations: водительские права; получить права; сдавать на права; лишить прав
     - labels: formality informal
     - grammar: countability Count; number Plur; note in this sense only in the plural: права
**Lexeme 2: право**, ADV
- said: /ˈpra.və/ пра́во (ru-RU)
  1. [C1] используется, чтобы подчеркнуть искренность сказанного
     - example: Я, право, не знаю, что вам на это ответить.
     - labels: formality informal
     - grammar: note used as a parenthetical word, set off by commas

### 44. советский

**Lexeme 1: советский**, ADJ
- said: /sɐ.ˈvʲets.kʲij/ сове́тский (ru-RU)
  1. [B1] бывший в СССР или принадлежавший этому государству
     - example: Мой дедушка всю жизнь собирал старые советские монеты.
     - collocations: Советский Союз; советская власть; советское время; советские времена; советский период; советский народ; советская армия
     - labels: time historical
     - grammar: comparable False
  2. [C1] похожий по духу, привычкам или взглядам на жизнь в СССР
     - example: У нашего директора до сих пор очень советский стиль руководства.
     - collocations: советский человек; советский менталитет
     - labels: formality informal
     - grammar: comparable True
- relations: derived_from совет (NOUN)

### 45. бывший

**Lexeme 1: бывший**, ADJ
- grammar: position Attr; comparable False
- said: /ˈbɨf.ʂɨj/ бы́вший (ru-RU)
  1. [A2] раньше занимавший какую-либо должность или имевший какое-либо положение
     - example: Мой бывший начальник теперь работает в другом городе.
     - collocations: бывший муж; бывшая жена; бывший президент; бывший сотрудник; бывший одноклассник; бывший друг
  2. [B1] существовавший раньше, но теперь не существующий или ставший другим
     - example: На месте бывшего завода скоро откроют большой парк.
     - collocations: бывшая столица; бывшие колонии
- relations: derived_from быть (VERB)
**Lexeme 2: бывший**, NOUN
- grammar: gender Masc; animacy Anim; countability Count; declension adjectival
- said: /ˈbɨf.ʂɨj/ бы́вший (ru-RU)
- forms: бывшего [Case=Gen, Number=Sing] бы́вшего; бывшие [Case=Nom, Number=Plur] бы́вшие; бывших [Case=Gen, Number=Plur] бы́вших
  1. [B1] мужчина, с которым у кого-либо раньше были любовные отношения
     - example: Вчера в магазине я случайно встретила своего бывшего.
     - collocations: вернуться к бывшему; расстаться с бывшим
     - labels: formality informal
- relations: derived_from бывший (ADJ)
