# A tour of the shape in real entries

43 written entries that between them show all 106 things an entry can say, picked by
`scripts/lexicon_research/tour.py` from what the writers wrote: the fewest words, a common English one first.
Type each into the app and check that the reader is shown what the right-hand column says.

The entries are shown as ingest publishes them: a pattern with a choice of words split in two, a pattern without
an open place among the collocations, and each pattern also read into its parts. Entries written before prompt
v4.8 have few collocations; the rows that show them are from v4.8 and v4.9.

The entries are schema 4. Until the backend reads schema 4 (docs/LEXICON.md, "Pending", item 1) the app cannot
show them, so today this list is what that work has to make visible, not a test it can pass.

| # | Word | Language | What the reader should see |
|---|---|---|---|
| 1 | нести | Russian | **a figurative sense**: быть причиной чего-либо для других людей<br>**a form with its own stress**: несу́<br>**a link that belongs to some senses only**: aspect_partner понести: senses [3, 4]<br>**a link to the word it comes from or is mixed up with: a word it is confused with**: носить (носить is carrying in many directions, habitually, or wearing; нести is carrying in one direction now)<br>**a link to the word it comes from or is mixed up with: an aspect partner**: понести<br>**a note on one sense's grammar**: only in the third person singular, with no subject<br>**a note on the word's grammar**: verb of motion in one direction; its many-direction pair is носить<br>**a pattern read into its parts for a program**: нести что кому = head: нести | slot: thing, case Acc/Nom | slot: person, case Dat<br>**a pattern whose open place has a case**: нести что кому = head: нести | slot: thing, case Acc/Nom | slot: person, case Dat<br>**a sense marked disapproving**: говорить глупости, ерунду<br>**a sense marked informal**: говорить глупости, ерунду<br>**a sense with company and no pattern**: нести: нести потери; нести убытки<br>**an irregular form**: несу (Tense=Pres, Person=1, Number=Sing, VerbForm=Fin)<br>**an open place for a person or a thing**: от кого/чего несёт чем = word: от | slot: person/thing, case Gen | head: несёт | slot: thing, case Ins<br>**the patterns a sense is used in**: нести что кому; нести что куда<br>**the stress marked on the word**: нести́<br>**the words a sense usually goes with**: нести службу |
| 2 | lower | English | **a link to the word it comes from or is mixed up with: a derived word**: low<br>**a pattern that opens a verb**: lower yourself to do sth = head: lower | slot: reflexive | slot: verb, form to-inf<br>**a rare sense**: of the sky, to look dark and threatening; of a person, to frown<br>**a reflexive sense of a verb**: to behave in a way that makes people respect you less<br>**a sense marked literary**: of the sky, to look dark and threatening; of a person, to frown<br>**a spelling for each variety**: lower (en-US, en-GB, en-AU, en-IN); lour (en-GB)<br>**a word or sense used only joined to a noun, or only after a verb like be**: Attr: placed below another part or thing of the same kind<br>**an opposite**: raise<br>**one spelling, two parts of speech**: VERB and ADJ<br>**said differently in two varieties**: en-US /ˈloʊ.ɚ/, en-GB /ˈləʊ.ə/<br>**two words of one spelling, said differently**: /ˈloʊ.ɚ/ and /ˈlaʊ.ɚ/ |
| 3 | Kraft | German | **a German plural with an umlaut**: -¨e<br>**a capital letter makes it another word**: Kraft and kraft<br>**a sense marked formal**: aufgrund von, durch die Macht oder das Recht von<br>**a sense marked official**: aufgrund von, durch die Macht oder das Recht von<br>**a sense of a special field**: physics: Ursache, die einen Körper bewegt oder seine Form verändert<br>**a sense the word has only in the plural**: Gruppen von Menschen mit gemeinsamen politischen oder gesellschaftlichen Zielen<br>**a word that never changes its form**: kraft<br>**countable in one sense, uncountable in another**: Kraft |
| 4 | dick | English | **a sense marked dated**: a detective, especially one who works privately for clients<br>**a sense marked derogatory**: a man who behaves in a rude, unkind or stupid way<br>**a sense marked slang**: a man's penis<br>**a sense marked vulgar**: a man's penis<br>**a sense used only in one part of the language**: en-US: nothing at all, used after a negative verb |
| 5 | вид | Russian | **a form that belongs to one sense**: виду<br>**a form with a note on when it is used**: виду: after в and на in set phrases: на виду́, в виду́; but в ви́де<br>**a partitive**: ви́ду<br>**a second locative**: виду́<br>**a sense with six collocations or more, the less guessable first**: делать вид; иметь вид; не подавать виду; внешний вид; усталый вид; с серьёзным видом; на вид |
| 6 | nazionale | Italian | **a link to the word it comes from or is mixed up with: an adjective of a noun**: nazione<br>**a noun of either gender**: nazionale<br>**one spelling, two genders, two words**: Fem and Com<br>**the stress and the open or close vowel marked on the word**: nazionàle |
| 7 | give up | English | **a grammar value that holds mainly, not always**: valency Intr: to stop trying to do something because it is too difficult<br>**a phrase other words can come between**: give up<br>**a phrase with its parts**: give (head) + up (fixed) |
| 8 | lay | English | **a sense marked archaic**: a short poem or song, especially one telling an old story<br>**a sense marked nonstandard**: to be or put yourself in a flat position to rest<br>**the standard word given for a proscribed one**: lie |
| 9 | rapariga | Portuguese | **a link to the word it comes from or is mixed up with: a feminine**: rapaz<br>**a sense marked offensive**: mulher que se prostitui ou que é amante de um homem casado<br>**a sense used only in one part of the language, a whole area**: pt-002: pessoa jovem do sexo feminino, entre a infância e a idade adulta |
| 10 | private | English | **a sense marked euphemistic**: the sexual organs of a person's body<br>**a sense marked humorous**: the sexual organs of a person's body |
| 11 | learn | English | **a form used in one variety**: learnt (en-GB, en-AU, en-IN)<br>**a pattern that opens a clause**: learn that = head: learn | word: that | slot: clause |
| 12 | mid | English | **a sense marked poetic**: surrounded by or among other things or people<br>**two spellings of one word, used everywhere**: mid, 'mid |
| 13 | fallen | English | **a noun used only in the plural**: fallen<br>**a sense marked elevated**: killed while fighting in a war |
| 14 | пожаловать | Russian | **a sense marked historical**: дать кому-нибудь в награду, обычно от лица правителя<br>**a sense marked ironic**: прийти или приехать куда-нибудь, обычно в гости |
| 15 | aussehen | German | **a link to the word it comes from or is mixed up with: a verbal noun**: aussehen<br>**a separable verb**: aussehen (aus) |
| 16 | atualmente | Portuguese | **a link to the word it comes from or is mixed up with: an adverb of an adjective**: atual<br>**a spelling only older books use**: actualmente |
| 17 | is | English | **a headword that is an inflected form: the reader is sent to its word**: inflected form of be (third person singular present) |
| 18 | don't | English | **a contraction with the words it stands for**: do + not |
| 19 | baby | English | **a sense marked endearing**: a loving way of speaking to a husband, wife or lover |
| 20 | de | English | **a headword that is no word (an abbreviation, a name): nothing invented**: not an English word: a French, Spanish, Portuguese or Dutch preposition seen only in foreign names and phrases (de Gaulle, de facto); also t |
| 21 | security | English | **a link to the word it comes from or is mixed up with: an abstract noun**: secure |
| 22 | player | English | **a link to the word it comes from or is mixed up with: an agent noun**: play |
| 23 | cotton | English | **a sense used mainly in one part of the language**: en-GB: thin thread used for sewing by hand or machine |
| 24 | larry | English | **a link to the word it comes from or is mixed up with: a diminutive**: Lawrence |
| 25 | в | Russian | **a pronunciation that belongs to one spelling**: /ˈv/ for в |
| 26 | ты | Russian | **a sense marked familiar**: используется при обращении к одному близкому или знакомому человеку |
| 27 | вы | Russian | **a sense marked polite**: употребляется при вежливом обращении к одному человеку |
| 28 | сделать | Russian | **a second pronunciation, with a note on who says it**: /ˈzdʲe.lətʲ/ (newer) |
| 29 | час | Russian | **a form used after numbers**: часа |
| 30 | stehen | German | **grammar that differs in one variety**: {"de-AT": {"auxiliary": "be"}, "de-CH": {"auxiliary": "be"}} |
| 31 | bekommen | German | **a verb with both auxiliaries**: bekommen |
| 32 | draußen | German | **a headword that is another word's spelling: the reader is sent to that word**: spelling of draußen (ADV), already covered; no other lexeme has this spelling |
| 33 | Flughafen | German | **a compound with its parts**: Flug + Hafen |
| 34 | haut | French | **a French h that blocks elision**: haut |
| 35 | cierre | Spanish | **a sense used mainly in one part of the language, a whole area**: es-419: tira con dos filas de dientes que une las partes de una prenda |
| 36 | granny | English | **a link to the word it comes from or is mixed up with: an endearing form**: grandmother |
| 37 | hone | English | **a sense marked proscribed**: to move towards a target or give attention to one thing |
| 38 | portone | Italian | **a link to the word it comes from or is mixed up with: an augmentative**: porta |
| 39 | 見る | Japanese | **a Japanese word given as a reading in kana, not a transcription**: 見る: みる<br>**a spelling that belongs to one sense**: 見る, みる: 目で物の形や色、様子をとらえる<br>**a use of the word as a suffix, a prefix or an auxiliary**: 見る: 観る, 看る and 診る are used for particular senses; the auxiliary 〜てみる is usually written in kana.<br>**one word written in two scripts**: 見る (kanji); みる (hiragana); 観る (kanji); 看る (kanji); 診る (kanji)<br>**the class of a Japanese verb or adjective**: 見る: ichidan<br>**the pitch accent of a Japanese word**: みる 1 |
| 40 | 電話 | Japanese | **a noun that makes a verb with する**: 電話<br>**the counter a Japanese noun is counted with**: 電話: 台 |
| 41 | なる | Japanese | **a sense marked honorific**: 動詞と組み、相手の行為を敬う言い方を作る |
| 42 | 何 | Japanese | **two readings of one Japanese word, one meaning**: なに, なん |
| 43 | 拙者 | Japanese | **a sense marked humble**: 昔の武士などが自分をへりくだって指す言葉 |
