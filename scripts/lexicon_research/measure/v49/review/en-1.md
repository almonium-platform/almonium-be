# English: entries to check (1 of 1)

These are entries of a learner's dictionary of English, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of English.

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
  sense, or an example is not natural English.
- **collocation**: a collocation that is not said that way in English; or one that does not belong
  among the ten a learner is shown for the sense, because anyone would build it from the meanings
  or because it is rare; or a list that lacks the commonest partner of the sense while it has
  rarer ones. Quote the collocation in "what". A frame with the wrong preposition or case is a
  fact, not this.

Do not report: the order of senses, a CEFR level off by one step, how syllables are divided, a
definition you would have worded differently when the given one is true, or anything about style.

These are this dictionary's own definitions, not faults:

- valency Refl stands for every use with a reflexive pronoun, whatever a grammar of English calls it
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

### 1. misnomer

**Lexeme 1: misnomer**, NOUN
- grammar: countability Count
- said: /ˌmɪs.ˈnoʊ.mɚ/ (en-US), /ˌmɪs.ˈnəʊ.mə/ (en-GB)
  1. [C2] a name or word that describes something wrongly or unsuitably
     - example: Calling it a quick lunch is a misnomer; it lasted three hours.
     - frames: misnomer for sth
     - collocations: something of a misnomer; a bit of a misnomer; a complete misnomer

### 2. tock

**Lexeme 1: tock**, NOUN
- grammar: countability Count
- said: /ˈtɑːk/ (en-US), /ˈtɒk/ (en-GB)
  1. [C1] a short, hollow sound made by a clock, following each tick
     - example: In the silent room, every tick and tock seemed very loud.
     - collocations: tick and tock

### 3. debauchery

**Lexeme 1: debauchery**, NOUN
- grammar: countability Mass, mainly
- said: /dɪ.ˈbɔː.tʃɚ.i/ (en-US), /dɪ.ˈbɑː.tʃɚ.i/ (en-US), /dɪ.ˈbɔː.tʃə.ɹi/ (en-GB)
  1. [C2] behaviour with too much drinking, sex or other physical pleasure, seen as immoral
     - example: The young prince wasted his fortune on years of debauchery.
     - collocations: a life of debauchery; a night of debauchery; drunken debauchery; sink into debauchery
     - labels: attitude disapproving
- relations: derived_from debauch (VERB)

### 4. phase

**Lexeme 1: phase**, NOUN
- grammar: countability Count
- said: /ˈfeɪz/ (en-US), /ˈfeɪz/ (en-GB)
  1. [B2] one of the stages in a process of change or development
     - example: The first phase of the project will finish next spring.
     - frames: phase of sth
     - collocations: go through a phase; just a phase; enter a phase; in phases; first phase; initial phase; final phase; next phase
  2. [C1] the shape of the bright part of the moon at a particular time
     - example: A full moon is the brightest phase you can see.
     - collocations: phases of the moon
**Lexeme 2: phase**, VERB
- grammar: irregular False
- said: /ˈfeɪz/ (en-US), /ˈfeɪz/ (en-GB)
  1. [C1] to plan or carry out a change gradually, in several stages
     - example: The new rules will be phased over the next three years.
     - labels: formality formal
     - grammar: valency Tr; note usually passive
- relations: derived_from phase (NOUN); confusable faze (VERB): sounds the same; "not phased" is often written for "not fazed"

### 5. homologous

**Lexeme 1: homologous**, ADJ
- grammar: comparable False, mainly
- said: /hoʊ.ˈmɑː.lə.ɡəs/ (en-US), /hə.ˈmɑː.lə.ɡəs/ (en-US), /hə.ˈmɒl.ə.ɡəs/ (en-GB), /hɒ.ˈmɒl.ə.ɡəs/ (en-GB)
  1. [C2] similar in structure because they come from a shared ancestor
     - example: A human arm and a bat's wing are homologous structures.
     - frames: homologous to sth; homologous with sth
     - collocations: homologous structures; homologous organs; homologous genes
     - labels: domain biology
  2. [C2] forming a matching pair, one from each parent, with the same genes
     - example: During cell division, homologous chromosomes line up next to each other.
     - collocations: homologous chromosomes; homologous pair
     - labels: domain biology
     - grammar: position Attr
  3. [C2] having the same position, structure or purpose as something else
     - example: The two offices are homologous, though they have different names.
     - frames: homologous with sth; homologous to sth
     - labels: formality formal
- relations: confusable homogeneous (ADJ): similar spelling; homogeneous means made of parts that are all the same; confusable analogous (ADJ): in biology analogous structures do a similar job but do not share an ancestor

### 6. sexy

**Lexeme 1: sexy**, ADJ
- said: /ˈsɛk.si/ (en-US), /ˈsek.si/ (en-GB)
  1. [B1] physically attractive in a way that makes other people desire you
     - example: She wore a sexy red dress to the party last night.
     - collocations: look sexy; feel sexy; sexy dress; sexy voice; sexy underwear
     - labels: formality informal
  2. [B2] exciting and fashionable, so that many people are interested in it
     - example: The company wants a sexy new product to attract young buyers.
     - collocations: not very sexy; sexy topic; sexy product
     - labels: formality informal
- relations: derived_from sex (NOUN)

### 7. thereon

**Lexeme 1: thereon**, ADV
- said: /ðɛɹ.ˈɑːn/ (en-US), /ðɛɹ.ˈɔːn/ (en-US), /ðeə.ˈɹɒn/ (en-GB)
- parts: there (there) + on (on)
  1. [C2] on the thing or subject that has just been mentioned
     - example: The owner must insure the land and any buildings thereon.
     - collocations: interest thereon; comments thereon
     - labels: formality formal
  2. [C2] immediately after that, or because of that
     - example: The guard read the order and thereon opened the gates.
     - labels: time archaic

### 8. watertight

**Lexeme 1: watertight**, ADJ
- said: /ˈwɑː.tɚ.taɪt/ (en-US), /ˈwɔː.tɚ.taɪt/ (en-US), /ˈwɔː.tə.taɪt/ (en-GB)
- parts: water (water) + tight (tight)
  1. [B2] closed or made so that no liquid can get in or out
     - example: Keep your phone in a watertight bag on the boat.
     - collocations: watertight container; watertight seal; watertight compartment; watertight door
  2. [C1] having no faults or weak points that anyone could question
     - example: Her lawyer believes the case against the company is watertight.
     - collocations: watertight alibi; watertight case; watertight argument; watertight contract; watertight guarantee
     - labels: figurative True

### 9. tennis

**Lexeme 1: tennis**, NOUN
- grammar: countability Mass
- said: /ˈtɛ.nɪs/ (en-US), /ˈte.nɪs/ (en-GB)
  1. [A1] a sport in which players hit a ball over a net using rackets
     - example: We play tennis every Saturday morning at the local club.
     - collocations: play tennis; a game of tennis; tennis court; tennis player; tennis match; tennis racket; tennis ball; tennis club

### 10. ligature

**Lexeme 1: ligature**, NOUN
- grammar: countability Count
- said: /ˈlɪɡ.ə.tʃɚ/ (en-US), /ˈlɪɡ.ə.tʃʊɹ/ (en-US, less_common), /ˈlɪɡ.ə.tʃə/ (en-GB), /ˈlɪɡ.ə.tʃʊə/ (en-GB, less_common)
  1. [C2] a thread, cord or wire tied tightly around something
     - example: The surgeon tied a ligature around the bleeding artery.
     - collocations: ligature marks; tie a ligature
  2. [C2] a single printed or written character made by joining two or more letters
     - example: Many older books print the letters a and e as a ligature.

### 11. invertebrate

**Lexeme 1: invertebrate**, NOUN
- grammar: countability Count
- said: /ɪn.ˈvɝ.tə.bɹət/ (en-US), /ɪn.ˈvɝ.tə.bɹeɪt/ (en-US), /ɪn.ˈvɜː.tɪ.bɹət/ (en-GB), /ɪn.ˈvɜː.tɪ.bɹeɪt/ (en-GB)
  1. [C1] an animal that has no backbone
     - example: Worms, snails and insects are all invertebrates found in gardens.
     - collocations: marine invertebrates; aquatic invertebrates; small invertebrates
     - labels: domain biology
     - opposites: vertebrate
- relations: derived_from vertebrate (NOUN)
**Lexeme 2: invertebrate**, ADJ
- grammar: comparable False; position Attr, mainly
- said: /ɪn.ˈvɝ.tə.bɹət/ (en-US), /ɪn.ˈvɝ.tə.bɹeɪt/ (en-US), /ɪn.ˈvɜː.tɪ.bɹət/ (en-GB), /ɪn.ˈvɜː.tɪ.bɹeɪt/ (en-GB)
  1. [C1] having no backbone
     - example: The pond is full of tiny invertebrate creatures.
     - collocations: invertebrate animals; invertebrate species
     - labels: domain biology
     - opposites: vertebrate
- relations: derived_from vertebrate (ADJ)

### 12. showman

**Lexeme 1: showman**, NOUN
- grammar: countability Count; irregular True
- said: /ˈʃoʊ.mən/ (en-US), /ˈʃəʊ.mən/ (en-GB)
- parts: show (show) + man (man)
- forms: showmen [Number=Plur] /ˈʃoʊ.mən/ /ˈʃəʊ.mən/
  1. [C1] a person who is good at entertaining people and getting their attention
     - example: Our new coach is a natural showman who loves the cameras.
     - collocations: a natural showman; a born showman; a great showman; a consummate showman
  2. [C2] a person whose job is organizing or presenting public entertainment
     - example: The travelling showmen set up their rides in the field.
     - collocations: travelling showman; circus showman

### 13. maleficent

**Lexeme 1: maleficent**, ADJ
- said: /mə.ˈlɛf.ɪ.sənt/ (en-US), /mə.ˈlef.ɪ.sənt/ (en-GB)
  1. [C2] causing harm or doing evil, often on purpose
     - example: Old stories tell of a maleficent spirit that haunts the forest.
     - collocations: maleficent spirit; maleficent influence; maleficent power
     - labels: style literary, formality formal
     - opposites: beneficent
- relations: confusable malevolent (ADJ): malevolent stresses wishing harm; maleficent stresses actually causing it

### 14. safely

**Lexeme 1: safely**, ADV
- said: /ˈseɪf.li/ (en-US), /ˈseɪf.li/ (en-GB)
  1. [A2] without being hurt, damaged, lost or put in danger
     - example: The plane landed safely despite the strong winds.
     - collocations: arrive safely; land safely; get home safely; safely back; keep sth safely
  2. [B1] in a careful way that avoids danger or risk
     - example: Please drive safely because the roads are icy tonight.
     - collocations: drive safely; handle safely; use safely; dispose of safely
  3. [B2] with no real chance of being wrong
     - example: I think we can safely say the worst is over.
     - collocations: safely say; safely assume; safely ignore; safely predict
- relations: adverb_of safe (ADJ)

### 15. duffel

**Lexeme 1: duffel**, NOUN
- grammar: countability Both
- said: /ˈdʌf.əl/ (en-US), /ˈdʌf.əl/ (en-GB)
- spellings: duffel; duffle
  1. [B2] a large tube-shaped bag of strong cloth for carrying clothes
     - example: He threw his duffel into the back of the truck.
     - collocations: duffel bag; pack a duffel
     - regions: en-021 mainly
     - grammar: countability Count; note often in the phrase duffel bag
  2. [C2] a thick, heavy woollen cloth with a soft rough surface
     - example: My grandfather still wears his old coat made of duffel.
     - collocations: duffel coat
     - grammar: countability Mass

### 16. chaff

**Lexeme 1: chaff**, NOUN
- grammar: countability Mass
- said: /ˈtʃæf/ (en-US), /ˈtʃɑːf/ (en-GB), /ˈtʃæf/ (en-GB)
  1. [C1] the dry outer covering of grain seeds, removed before the grain is used
     - example: The wind carried the chaff away from the threshing floor.
     - collocations: separate the chaff
  2. [C2] things or people of no value, mixed with those that are useful
     - example: Good editors quickly separate the wheat from the chaff.
     - collocations: separate the wheat from the chaff; sort the wheat from the chaff
     - labels: figurative True
**Lexeme 2: chaff**, VERB
- grammar: valency Tr; irregular False
- said: /ˈtʃæf/ (en-US), /ˈtʃɑːf/ (en-GB), /ˈtʃæf/ (en-GB)
  1. [C2] to tease someone in a friendly, joking way
     - example: His brothers chaffed him about his new haircut.
     - frames: chaff sb about sth
     - labels: time dated
     - regions: en-GB mainly

### 17. jute

**Lexeme 1: jute**, NOUN
- grammar: countability Mass
- said: /ˈdʒuːt/ (en-US), /ˈdʒuːt/ (en-GB)
  1. [C1] a rough strong fibre from a tropical plant, used to make sacks and rope
     - example: The coffee beans arrived in heavy sacks made of jute.
     - collocations: jute bag; jute sack; jute rope; jute fibre; jute mill
**Lexeme 2: Jute**, NOUN
- grammar: countability Count; number Plur, mainly
- said: /ˈdʒuːt/ (en-US), /ˈdʒuːt/ (en-GB)
  1. [C2] a member of a Germanic people who settled in southern England long ago
     - example: Many Jutes settled in Kent after the Romans left Britain.
     - labels: time historical, domain history

### 18. dim

**Lexeme 1: dim**, ADJ
- said: /ˈdɪm/ (en-US), /ˈdɪm/ (en-GB)
  1. [B1] fairly dark or not giving much light, so that you cannot see well
     - example: We could hardly read the menu in the dim light.
     - collocations: dim light; a dim room; a dim corner; grow dim
     - opposites: bright
  2. [B2] not clear in your mind because it happened a long time ago
     - example: I have only a dim memory of my first school.
     - collocations: a dim memory; a dim recollection; a dim awareness; the dim and distant past
  3. [C1] difficult to see clearly because it is far away or dark
     - example: A dim figure was standing at the end of the road.
     - collocations: a dim figure; a dim outline; a dim shape
  4. [B2] not very clever and slow to understand things
     - example: He's a bit dim, but he's very kind to everyone.
     - labels: formality informal
  5. [C1] not likely to be good or successful in the future
     - example: The team's chances of winning the cup now look dim.
     - collocations: dim prospects; look dim
**Lexeme 2: dim**, VERB
- said: /ˈdɪm/ (en-US), /ˈdɪm/ (en-GB)
  1. [B2] to make a light less bright
     - example: Could you dim the lights before the film starts?
     - collocations: dim the lights; dim the headlights
     - grammar: valency Tr
  2. [B2] to become less bright, so that there is less light
     - example: The lights in the theatre dimmed and the audience went quiet.
     - collocations: the lights dim
     - grammar: valency Intr
  3. [C1] to become weaker or less strong, used about feelings or hopes
     - example: Her hopes of finding a job dimmed as the months passed.
     - collocations: hopes dim; enthusiasm dims
     - grammar: valency Intr
- relations: derived_from dim (ADJ)

### 19. riverbank

**Lexeme 1: riverbank**, NOUN
- grammar: countability Count
- said: /ˈɹɪv.ɚ.bæŋk/ (en-US), /ˈɹɪv.ə.bæŋk/ (en-GB)
- spellings: riverbank; river bank
- parts: river (river) + bank (bank)
  1. [B1] the land along either side of a large natural stream of water
     - example: We sat on the riverbank and watched the boats go by.
     - collocations: on the riverbank; along the riverbank; grassy riverbank; muddy riverbank; steep riverbank

### 20. teenaged

**Lexeme 1: teenaged**, ADJ
- grammar: comparable False; position Attr, mainly
- said: /ˈtiːn.eɪdʒd/ (en-US), /ˈtiːn.eɪdʒd/ (en-GB)
  1. [B2] between thirteen and nineteen years old
     - example: Our neighbours have two teenaged daughters who play the drums.
     - collocations: teenaged son; teenaged daughter; teenaged children; teenaged boy; teenaged girl

### 21. placate

**Lexeme 1: placate**, VERB
- grammar: valency Tr; irregular False
- said: /ˈpleɪ.keɪt/ (en-US), /ˈplæk.eɪt/ (en-US, less_common), /plə.ˈkeɪt/ (en-GB), /pleɪ.ˈkeɪt/ (en-GB)
  1. [C1] to make someone less angry or upset, often by giving them something they want
     - example: The manager offered a refund to placate the angry customers.
     - frames: placate sb
     - collocations: try to placate; attempt to placate; placate critics; placate investors
     - labels: formality formal

### 22. annular

**Lexeme 1: annular**, ADJ
- grammar: comparable False; position Attr, mainly
- said: /ˈæn.jə.lɚ/ (en-US), /ˈæn.jə.lə/ (en-GB), /ˈæn.jʊ.lə/ (en-GB)
  1. [C2] shaped like a ring or forming a ring
     - example: Next year an annular eclipse will be visible from our town.
     - collocations: annular eclipse; annular ring
     - labels: formality formal

### 23. shanti

**Lexeme 1: shanti**, NOUN
- grammar: countability Mass
- said: /ˈʃɑːn.ti/ (en-US), /ˈʃɑːn.ti/ (en-GB), /ˈʃæn.ti/ (en-GB, less_common)
- spellings: shanti; shantih
  1. [C2] deep peace of mind, in Hindu and Buddhist teaching
     - example: The yoga teacher ended each class by chanting shanti three times.
     - collocations: om shanti
     - labels: domain religion
     - grammar: note often chanted three times at the end of a prayer
- relations: confusable shanty (NOUN): shanty, a small rough hut or a sailors' song, sounds similar

### 24. activity

**Lexeme 1: activity**, NOUN
- grammar: countability Both
- said: /æk.ˈtɪv.ə.ti/ (en-US), /æk.ˈtɪv.ə.ti/ (en-GB)
  1. [A2] something you do for fun, interest or exercise, often with others
     - example: The hotel offers outdoor activities such as hiking and swimming.
     - collocations: take part in activities; outdoor activities; leisure activities; after-school activities; physical activity; social activities
     - grammar: countability Count; note often plural
  2. [B2] the things that a person or group does to achieve a particular aim
     - example: Police are investigating criminal activity in the area.
     - collocations: criminal activity; economic activity; business activities; terrorist activity; political activity; brain activity
     - grammar: countability Both; note uncountable or plural
  3. [B1] a situation in which a lot of things are happening or people are moving
     - example: There was a lot of activity in the kitchen before dinner.
     - collocations: a flurry of activity; a hive of activity; a burst of activity; a lot of activity
     - grammar: countability Mass
- relations: abstract_noun_of active (ADJ)

### 25. huckleberry

**Lexeme 1: huckleberry**, NOUN
- grammar: countability Count
- said: /ˈhʌk.əl.bɛɹ.i/ (en-US), /ˈhʌk.əl.bəɹ.i/ (en-GB), /ˈhʌk.əl.beɹ.i/ (en-GB)
- forms: huckleberries [Number=Plur] /ˈhʌk.əl.bɛɹ.iz/ /ˈhʌk.əl.bəɹ.iz/
  1. [C2] a small round dark blue or black berry, or the North American bush it grows on
     - example: We picked wild huckleberries on the mountain slopes in August.
     - collocations: huckleberry pie; huckleberry jam; wild huckleberries; pick huckleberries

### 26. wye

**Lexeme 1: wye**, NOUN
- grammar: countability Count
- said: /ˈwaɪ/ (en-US), /ˈwaɪ/ (en-GB)
  1. [C2] the name of the letter Y
     - example: The word for that colour starts with a wye.
     - labels: rare True
  2. [C2] a joint, pipe or set of railway tracks shaped like the letter Y
     - example: The plumber fitted a wye where the two drains meet.
     - labels: domain technology

### 27. ornamentation

**Lexeme 1: ornamentation**, NOUN
- grammar: countability Mass
- said: /ˌɔːɹ.nə.mɛn.ˈteɪ.ʃən/ (en-US), /ˌɔː.nə.men.ˈteɪ.ʃən/ (en-GB)
  1. [C1] decorations added to a building or object to make it more beautiful
     - example: The old church is famous for its rich stone ornamentation.
     - collocations: elaborate ornamentation; rich ornamentation; floral ornamentation; without ornamentation; little ornamentation
  2. [C2] extra notes added to a tune to make it more interesting
     - example: Singers of that period often added their own ornamentation to the melody.
     - collocations: vocal ornamentation
     - labels: domain music
- relations: derived_from ornament (VERB)

### 28. deva

**Lexeme 1: deva**, NOUN
- grammar: countability Count
- said: /ˈdeɪ.və/ (en-US), /ˈdeɪ.və/ (en-GB)
  1. [C2] a god or divine being in Hinduism and Buddhism
     - example: The temple walls are carved with images of devas and demons.
     - labels: domain religion

### 29. artillery

**Lexeme 1: artillery**, NOUN
- grammar: countability Mass
- said: /ɑːɹ.ˈtɪ.lɚ.i/ (en-US), /ɑː.ˈtɪ.lə.ɹi/ (en-GB)
  1. [C1] large heavy guns that fire shells over long distances
     - example: The town was shelled by heavy artillery throughout the night.
     - collocations: heavy artillery; artillery fire; artillery shell; artillery barrage; artillery piece
  2. [C1] the part of an army that uses large heavy guns
     - example: My grandfather served in the artillery during the war.
     - collocations: serve in the artillery; artillery regiment; artillery unit
     - grammar: note usually with the

### 30. baboon

**Lexeme 1: baboon**, NOUN
- grammar: countability Count
- said: /bæ.ˈbuːn/ (en-US), /bə.ˈbuːn/ (en-US), /bə.ˈbuːn/ (en-GB)
  1. [C1] a large monkey with a long face like a dog's, living mainly in Africa
     - example: A troop of baboons was sitting on the rocks near the road.
     - collocations: a troop of baboons

### 31. scratchy

**Lexeme 1: scratchy**, ADJ
- grammar: comparable True
- said: /ˈskɹætʃ.i/ (en-US), /ˈskɹætʃ.i/ (en-GB)
  1. [B2] rough and unpleasant when it touches your skin
     - example: This wool jumper is far too scratchy to wear all day.
     - collocations: scratchy wool; scratchy jumper; scratchy sweater; scratchy blanket; scratchy beard
     - opposites: soft
  2. [C1] sounding rough, with harsh or crackling noises
     - example: We listened to a scratchy old recording of her grandfather singing.
     - collocations: scratchy voice; scratchy recording; scratchy record; scratchy sound
  3. [C1] feeling slightly sore and dry, especially in the throat
     - example: I woke up with a scratchy throat and a headache.
     - collocations: scratchy throat; scratchy eyes
- relations: derived_from scratch (VERB)

### 32. snowmobile

**Lexeme 1: snowmobile**, NOUN
- grammar: countability Count
- said: /ˈsnoʊ.moʊ.ˌbiːl/ (en-US), /ˈsnoʊ.mə.ˌbiːl/ (en-US), /ˈsnəʊ.məʊ.ˌbiːl/ (en-GB), /ˈsnəʊ.mə.ˌbiːl/ (en-GB)
- parts: snow (snow) + mobile (mobile)
  1. [B2] a small motor vehicle with skis at the front for travelling over snow
     - example: In winter the only way to reach the village is by snowmobile.
     - collocations: ride a snowmobile; by snowmobile; on a snowmobile; drive a snowmobile; snowmobile trail
**Lexeme 2: snowmobile**, VERB
- grammar: valency Intr; irregular False
- said: /ˈsnoʊ.moʊ.ˌbiːl/ (en-US), /ˈsnoʊ.mə.ˌbiːl/ (en-US), /ˈsnəʊ.məʊ.ˌbiːl/ (en-GB), /ˈsnəʊ.mə.ˌbiːl/ (en-GB)
  1. [C1] to travel over snow by riding a small motor vehicle with skis
     - example: Every weekend they snowmobile across the frozen lake to their cabin.
     - collocations: go snowmobiling
     - regions: en-021 mainly
- relations: derived_from snowmobile (NOUN)

### 33. tangible

**Lexeme 1: tangible**, ADJ
- said: /ˈtæn.dʒə.bəl/ (en-US), /ˈtæn.dʒə.bəl/ (en-GB)
  1. [C1] clear and real enough to be easily seen, shown or measured
     - example: The new policy has brought tangible benefits to local families.
     - collocations: tangible benefits; tangible results; tangible evidence; tangible progress; tangible proof
     - opposites: intangible
  2. [C2] existing as a physical object that you can touch
     - example: The company's tangible assets include buildings, vehicles and machines.
     - collocations: tangible assets; tangible property
     - labels: formality formal
     - opposites: intangible

### 34. unedited

**Lexeme 1: unedited**, ADJ
- grammar: comparable False
- said: /ʌn.ˈɛd.ɪ.tɪd/ (en-US), /ʌn.ˈɛd.ə.təd/ (en-US), /ʌn.ˈed.ɪ.tɪd/ (en-GB)
  1. [C1] in its first form, without being corrected, shortened or changed by anyone
     - example: The unedited version of the film runs for nearly four hours.
     - collocations: unedited footage; unedited version; unedited transcript; unedited manuscript; unedited video
- relations: derived_from edit (VERB)

### 35. decontamination

**Lexeme 1: decontamination**, NOUN
- grammar: countability Mass, mainly
- said: /ˌdiː.kən.ˌtæm.ə.ˈneɪ.ʃən/ (en-US), /ˌdiː.kən.ˌtæm.ɪ.ˈneɪ.ʃən/ (en-GB)
  1. [C1] the removal of dangerous substances, germs or radiation from people, places or things
     - example: Workers wore protective suits during the decontamination of the old factory.
     - frames: decontamination of sth
     - collocations: undergo decontamination; decontamination process; decontamination procedures; decontamination unit; decontamination zone
- relations: verbal_noun_of decontaminate (VERB)

### 36. conductor

**Lexeme 1: conductor**, NOUN
- grammar: countability Count
- said: /kən.ˈdʌk.tɚ/ (en-US), /kən.ˈdʌk.tə/ (en-GB)
  1. [B1] someone who stands in front of musicians and directs their performance
     - example: The conductor raised her baton and the orchestra began to play.
     - collocations: orchestra conductor; guest conductor; principal conductor; chief conductor
  2. [B1] someone who checks or sells tickets on a train or bus
     - example: The conductor checked our tickets just after the train left.
     - collocations: bus conductor; train conductor
  3. [B2] a material or object that lets electricity or heat pass through it
     - example: Copper is a good conductor, so it is used in electrical wires.
     - frames: conductor of sth
     - collocations: good conductor; poor conductor; conductor of electricity; conductor of heat
     - labels: domain physics
- relations: agent_noun_of conduct (VERB)

### 37. cotton

**Lexeme 1: cotton**, NOUN
- grammar: countability Mass
- said: /ˈkɑː.tən/ (en-US), /ˈkɒ.tən/ (en-GB)
  1. [A2] soft light cloth made from the white fibres of a plant
     - example: In summer I prefer to wear shirts made of cotton.
     - collocations: made of cotton; pure cotton; 100% cotton; organic cotton; cotton shirt; cotton fabric
  2. [B1] a plant grown in warm countries for the soft white fibres around its seeds
     - example: Farmers in the south grew cotton on huge fields.
     - collocations: grow cotton; pick cotton; cotton field; cotton plantation; cotton crop
  3. [B2] thin thread used for sewing by hand or machine
     - example: She needed a reel of black cotton to fix the button.
     - collocations: a reel of cotton; needle and cotton
     - regions: en-GB mainly

### 38. impersonate

**Lexeme 1: impersonate**, VERB
- grammar: valency Tr; irregular False
- said: /ɪm.ˈpɝ.sə.neɪt/ (en-US), /ɪm.ˈpɜː.sə.neɪt/ (en-GB)
  1. [C1] to pretend to be another person in order to deceive people
     - example: He was arrested for impersonating a police officer at the airport.
     - frames: impersonate sb
     - collocations: impersonate a police officer; impersonate an officer
  2. [C1] to copy the voice and manner of a famous person to entertain people
     - example: My brother can impersonate almost any politician you can name.
     - frames: impersonate sb

### 39. produce

**Lexeme 1: produce**, VERB
- grammar: irregular False; valency Tr
- said: /pɹə.ˈduːs/ (en-US), /pɹə.ˈdjuːs/ (en-GB)
  1. [B1] to make or grow something, especially in large amounts to sell
     - example: The factory produces over two thousand cars every week.
     - collocations: produce goods; produce food; produce crops; produce electricity; produce oil
     - grammar: valency Tr
  2. [B2] to cause a particular result, effect or reaction
     - example: The new medicine produced some unpleasant side effects in patients.
     - collocations: produce results; produce an effect; produce a reaction; produce a change
     - grammar: valency Tr
  3. [B2] to take something out and show it so people can see it
     - example: He produced his ticket when the inspector asked to see it.
     - frames: produce sth from sth
     - collocations: produce evidence; produce proof; produce a ticket
     - grammar: valency Tr
  4. [B2] to organize and pay for the making of a film, play or record
     - example: She has produced three successful films for a major studio.
     - collocations: produce a film; produce an album; produce a play; produce a show
     - grammar: valency Tr
**Lexeme 2: produce**, NOUN
- grammar: countability Mass
- said: /ˈpɹoʊ.duːs/ (en-US), /ˈpɹɑː.duːs/ (en-US), /ˈpɹɒ.djuːs/ (en-GB)
  1. [B2] food that is grown on farms, especially fruit and vegetables
     - example: The market sells fresh local produce every Saturday morning.
     - collocations: fresh produce; local produce; farm produce; dairy produce; organic produce
- relations: derived_from produce (VERB)

### 40. celibate

**Lexeme 1: celibate**, ADJ
- said: /ˈsɛl.ə.bət/ (en-US), /ˈsel.ɪ.bət/ (en-GB), /ˈsel.ə.bət/ (en-GB)
  1. [C1] choosing not to have sex, often for religious reasons
     - example: He remained celibate for most of his adult life.
     - collocations: remain celibate; stay celibate; celibate priest; celibate life
  2. [C2] not married, especially because of a religious promise
     - example: Monks in that order must live a celibate life.
     - collocations: celibate clergy
     - labels: formality formal
**Lexeme 2: celibate**, NOUN
- grammar: countability Count
- said: /ˈsɛl.ə.bət/ (en-US), /ˈsel.ɪ.bət/ (en-GB), /ˈsel.ə.bət/ (en-GB)
  1. [C2] a person who does not have sex or marry, often for religious reasons
     - example: Not every celibate in the community had taken religious vows.
     - labels: formality formal
- relations: derived_from celibate (ADJ)

### 41. peerless

**Lexeme 1: peerless**, ADJ
- grammar: comparable False
- said: /ˈpɪɹ.ləs/ (en-US), /ˈpɪə.ləs/ (en-GB)
  1. [C2] so good that nobody or nothing else is as good
     - example: Her peerless voice made every concert feel like a special event.
     - collocations: peerless performance; peerless beauty
     - labels: style literary
- relations: derived_from peer (NOUN)

### 42. abolitionist

**Lexeme 1: abolitionist**, NOUN
- grammar: countability Count
- said: /ˌæb.ə.ˈlɪʃ.ə.nɪst/ (en-US), /ˌæb.ə.ˈlɪʃ.ə.nɪst/ (en-GB)
  1. [C1] a person who wants a law, system or practice officially ended, especially slavery
     - example: Many abolitionists risked their lives helping people escape slavery.
     - collocations: leading abolitionist; death penalty abolitionist; slavery abolitionist
- relations: derived_from abolition (NOUN)
**Lexeme 2: abolitionist**, ADJ
- grammar: position Attr; comparable False
- said: /ˌæb.ə.ˈlɪʃ.ə.nɪst/ (en-US), /ˌæb.ə.ˈlɪʃ.ə.nɪst/ (en-GB)
  1. [C1] supporting the official ending of a law, system or practice, especially slavery
     - example: She wrote for an abolitionist newspaper in the 1850s.
     - collocations: abolitionist movement; abolitionist cause
- relations: derived_from abolition (NOUN)

### 43. shush

**Lexeme 1: shush**, INTJ
- said: /ˈʃʌʃ/ (en-US), /ˈʃʊʃ/ (en-US), /ˈʃʊʃ/ (en-GB), /ˈʃʌʃ/ (en-GB)
  1. [B1] used to tell someone to be quiet or stop talking
     - example: Shush, you'll wake the baby if you keep shouting.
     - labels: formality informal
**Lexeme 2: shush**, VERB
- grammar: valency Tr, mainly; irregular False
- said: /ˈʃʌʃ/ (en-US), /ˈʃʊʃ/ (en-US), /ˈʃʊʃ/ (en-GB), /ˈʃʌʃ/ (en-GB)
  1. [C1] to tell someone to be quiet, often by making a soft sound
     - example: The librarian shushed the students giggling in the corner.
     - frames: shush sb
     - labels: formality informal
- relations: derived_from shush (INTJ)

### 44. bandana

**Lexeme 1: bandana**, NOUN
- grammar: countability Count
- said: /bæn.ˈdæn.ə/ (en-US), /bæn.ˈdæn.ə/ (en-GB)
- spellings: bandana; bandanna
  1. [B2] a square of brightly coloured cloth worn on the head or around the neck
     - example: He tied a red bandana around his neck before the hike.
     - collocations: wear a bandana; tie a bandana

### 45. copy

**Lexeme 1: copy**, NOUN
- grammar: countability Both
- said: /ˈkɑː.pi/ (en-US), /ˈkɒ.pi/ (en-GB)
  1. [A2] something made to look exactly like something else
     - example: Please make a copy of this letter for my files.
     - frames: copy of sth
     - collocations: make a copy; keep a copy; an exact copy; a hard copy; a cheap copy; a backup copy
     - grammar: countability Count
  2. [B1] one single book, newspaper or record of many that were made
     - example: I bought a second-hand copy of the novel yesterday.
     - frames: copy of sth
     - collocations: a signed copy; a free copy; a spare copy; sell a million copies
     - grammar: countability Count
  3. [C1] written text prepared for a newspaper, magazine or advertisement
     - example: She writes advertising copy for a large fashion company.
     - collocations: advertising copy; write copy
     - grammar: countability Mass
**Lexeme 2: copy**, VERB
- grammar: valency Both
- said: /ˈkɑː.pi/ (en-US), /ˈkɒ.pi/ (en-GB)
  1. [A2] to make something that is exactly the same as something else
     - example: Can you copy these files onto the new computer?
     - frames: copy sth to sth; copy sth onto sth; copy sth from sth
     - collocations: copy and paste; copy a file; illegally copy
     - grammar: valency Tr
  2. [A2] to write something exactly as it is written somewhere else
     - example: The students copied the new words from the board.
     - frames: copy sth from sth; copy sth into sth
     - collocations: copy sth carefully; copy the words
     - grammar: valency Tr
  3. [B1] to do the same thing as someone else, or behave like them
     - example: Little children often copy what their parents do.
     - collocations: copy sb's style; copy sb's example; widely copied
     - grammar: valency Tr
  4. [B1] to cheat by looking at someone else's work and using their answers
     - example: The teacher caught him copying from the boy next to him.
     - frames: copy from sb; copy off sb
     - collocations: copy sb's homework
     - grammar: valency Intr, mainly

### 46. gynecologist

**Lexeme 1: gynecologist**, NOUN
- grammar: countability Count
- said: /ˌɡaɪ.nə.ˈkɑː.lə.dʒɪst/ (en-US), /ˌɡaɪ.nə.ˈkɒ.lə.dʒɪst/ (en-GB)
- spellings: gynecologist (en-US); gynaecologist (en-GB, en-AU, en-IN)
  1. [C1] a doctor who treats health problems of women's sex organs
     - example: You should see a gynecologist for a check-up every year.
     - collocations: see a gynecologist; visit a gynecologist
- relations: derived_from gynecology (NOUN)

### 47. encouragement

**Lexeme 1: encouragement**, NOUN
- grammar: countability Both
- said: /ɪn.ˈkɝ.ɪdʒ.mənt/ (en-US), /ɪn.ˈkʌ.ɹɪdʒ.mənt/ (en-GB)
  1. [B2] words or actions that give someone confidence or hope to do something
     - example: Her teacher's encouragement helped her finish the difficult course.
     - frames: encouragement to do sth; encouragement from sb
     - collocations: words of encouragement; a word of encouragement; give encouragement; offer encouragement; need encouragement; constant encouragement; with the encouragement of
     - grammar: countability Mass, mainly
     - opposites: discouragement
  2. [C1] action that helps something develop or become more likely to happen
     - example: The government's encouragement of foreign investment created many new jobs.
     - frames: encouragement of sth
     - grammar: countability Mass
     - opposites: discouragement
- relations: verbal_noun_of encourage (VERB)

### 48. riverfront

**Lexeme 1: riverfront**, NOUN
- grammar: countability Count
- said: /ˈɹɪv.ɚ.ˌfɹʌnt/ (en-US), /ˈɹɪv.ə.ˌfɹʌnt/ (en-GB)
- parts: river (river) + front (front)
  1. [C1] the land or area along the edge of a river, especially in a town
     - example: Families like to walk along the riverfront on summer evenings.
     - collocations: along the riverfront; on the riverfront; riverfront park; riverfront development; riverfront property
     - grammar: note often used before another noun

### 49. bloodborne

**Lexeme 1: bloodborne**, ADJ
- grammar: comparable False; position Attr, mainly
- said: /ˈblʌd.bɔːɹn/ (en-US), /ˈblʌd.bɔːn/ (en-GB)
- spellings: bloodborne (en-US); blood-borne (en-GB, en-AU, en-IN)
- parts: blood (blood) + borne (bear)
  1. [C1] carried in the red liquid of the body, or spread through contact with it
     - example: Nurses are trained to protect themselves from bloodborne infections.
     - collocations: bloodborne pathogens; bloodborne virus; bloodborne disease; bloodborne infection
     - labels: domain medicine

### 50. parietal

**Lexeme 1: parietal**, ADJ
- grammar: comparable False; position Attr
- said: /pə.ˈɹaɪ.ə.təl/ (en-US), /pə.ˈɹaɪ.ə.təl/ (en-GB), /pə.ˈɹaɪ.ɪ.təl/ (en-GB)
  1. [C2] in or forming the top and sides of the skull or the brain
     - example: The scan showed damage to the left parietal lobe.
     - collocations: parietal lobe; parietal bone; parietal cortex
     - labels: domain anatomy
  2. [C2] forming or lining the wall of a body space or organ
     - example: The parietal layer lines the inside of the chest wall.
     - collocations: parietal pleura; parietal cells
     - labels: domain anatomy
**Lexeme 2: parietal**, NOUN
- grammar: countability Count
- said: /pə.ˈɹaɪ.ə.təl/ (en-US), /pə.ˈɹaɪ.ə.təl/ (en-GB), /pə.ˈɹaɪ.ɪ.təl/ (en-GB)
  1. [C2] either of the two bones forming the top and sides of the skull
     - example: The fall left a small crack in the right parietal.
     - labels: domain anatomy

### 51. controversially

**Lexeme 1: controversially**, ADV
- said: /ˌkɑːn.tɹə.ˈvɝ.ʃə.li/ (en-US), /ˌkɒn.tɹə.ˈvɜː.ʃə.li/ (en-GB)
  1. [C1] in a way that causes a lot of public disagreement or argument
     - example: The referee controversially awarded a penalty in the final minute.
     - collocations: most controversially; rather controversially
     - grammar: note often at the start of a sentence, commenting on the whole statement
- relations: adverb_of controversial (ADJ)

### 52. classification

**Lexeme 1: classification**, NOUN
- grammar: countability Both
- said: /ˌklæs.ə.fə.ˈkeɪ.ʃən/ (en-US), /ˌklæs.ɪ.fɪ.ˈkeɪ.ʃən/ (en-GB)
  1. [B2] the process of putting things into groups according to their type
     - example: The classification of plants takes years of careful study.
     - frames: classification of sth; classification into sth
     - collocations: classification system; system of classification; scientific classification
     - grammar: countability Mass
  2. [C1] a group or category that something belongs to within a system
     - example: This film has an adult classification, so children cannot watch it.
     - frames: classification as sth
     - collocations: age classification; job classification; security classification
     - grammar: countability Count
- relations: verbal_noun_of classify (VERB)

### 53. patrol

**Lexeme 1: patrol**, VERB
- grammar: irregular False
- said: /pə.ˈtɹoʊl/ (en-US), /pə.ˈtɹəʊl/ (en-GB)
- forms: patrolled [Tense=Past, VerbForm=Fin,Part] /pə.ˈtɹoʊld/ /pə.ˈtɹəʊld/; patrolling [VerbForm=Part, Tense=Pres] /pə.ˈtɹoʊ.lɪŋ/ /pə.ˈtɹəʊ.lɪŋ/
  1. [B2] to go around an area regularly to check that it is safe
     - example: Armed guards patrol the border day and night.
     - collocations: patrol the streets; patrol the border; patrol the area; regularly patrol; heavily patrolled
     - grammar: valency Both
- relations: confusable petrol (NOUN): similar sound and spelling, but means fuel for cars
**Lexeme 2: patrol**, NOUN
- grammar: countability Both
- said: /pə.ˈtɹoʊl/ (en-US), /pə.ˈtɹəʊl/ (en-GB)
  1. [B2] regular movement around an area to check that it is safe
     - example: Two police officers were on patrol near the station.
     - collocations: on patrol; go on patrol; foot patrol; carry out a patrol; night patrol
  2. [B2] a group of soldiers, police or vehicles sent to guard an area
     - example: A small army patrol was attacked in the mountains.
     - collocations: police patrol; army patrol; border patrol; send out a patrol
     - grammar: countability Count
- relations: confusable petrol (NOUN): similar sound and spelling, but means fuel for cars

### 54. droplet

**Lexeme 1: droplet**, NOUN
- grammar: countability Count
- said: /ˈdɹɑːp.lət/ (en-US), /ˈdɹɑːp.lɪt/ (en-US), /ˈdɹɒp.lət/ (en-GB), /ˈdɹɒp.lɪt/ (en-GB)
  1. [C1] a very small round amount of liquid
     - example: Tiny droplets of water clung to the spider's web.
     - frames: droplet of sth
     - collocations: water droplets; tiny droplets; fine droplets; droplets of sweat; airborne droplets
- relations: diminutive_of drop (NOUN)

### 55. energize

**Lexeme 1: energize**, VERB
- grammar: valency Tr; irregular False
- said: /ˈɛn.ɚ.dʒaɪz/ (en-US), /ˈen.ə.dʒaɪz/ (en-GB)
- spellings: energize (en-US, en-GB); energise (en-GB, en-AU, en-IN)
  1. [B2] to give someone more strength, enthusiasm or desire to act
     - example: A short walk in the fresh air always energizes me.
     - frames: energize sb
     - collocations: feel energized; energize voters; energize the team
  2. [C2] to supply a machine or electrical circuit with power
     - example: Never touch the wires while the circuit is energized.
     - frames: energize sth
     - labels: domain electricity
- relations: derived_from energy (NOUN)
