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

Do not report: the order of senses, a CEFR level off by one step, how syllables are divided, a
definition you would have worded differently when the given one is true, or anything about style.

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

### 1. soap

**Lexeme 1: soap**, NOUN
- grammar: countability Both
- said: /ˈsoʊp/ (en-US), /ˈsəʊp/ (en-GB)
  1. [A1] a substance you use with water to wash your body or things
     - example: Wash your hands with soap and warm water before eating.
     - frames: a bar of soap
     - grammar: countability Mass
  2. [B2] a television or radio series about the daily lives of a group of characters
     - example: My grandmother never misses an episode of her favourite soap.
     - labels: formality informal
     - grammar: countability Count
**Lexeme 2: soap**, VERB
- grammar: valency Tr
- said: /ˈsoʊp/ (en-US), /ˈsəʊp/ (en-GB)
  1. [C1] to rub someone or something with a substance used for washing
     - example: He soaped his hands carefully and rinsed them under the tap.
     - frames: soap sth; soap yourself

### 2. obliterate

**Lexeme 1: obliterate**, VERB
- grammar: valency Tr; irregular False
- said: /ə.ˈblɪ.tə.ɹeɪt/ (en-US), /ə.ˈblɪ.tə.ɹeɪt/ (en-GB)
  1. [C1] to destroy something completely so that nothing is left
     - example: A single bomb could obliterate an entire city in seconds.
  2. [C2] to remove all signs of something or cover it so it cannot be seen
     - example: Fresh snow had obliterated the footprints on the path.
     - frames: obliterate sth from sb's memory
  3. [C1] to defeat a person or team very easily and completely
     - example: They obliterated the visiting side in the final.
     - labels: formality informal

### 3. north

**Lexeme 1: north**, NOUN
- grammar: countability Mass; number Sing
- said: /ˈnɔːɹθ/ (en-US), /ˈnɔːθ/ (en-GB)
  1. [A2] the direction on your left when you face the rising sun
     - example: The compass needle always points to the north.
     - frames: the north
     - opposites: south
  2. [A2] the part of a country or area that lies in this direction
     - example: My grandparents still live in a small village in the north.
     - frames: the north of sth
     - opposites: south
**Lexeme 2: north**, ADJ
- grammar: position Attr; comparable False
- said: /ˈnɔːɹθ/ (en-US), /ˈnɔːθ/ (en-GB)
  1. [A2] in or facing the part that lies towards the north
     - example: Our bedroom window is on the north side of the house.
     - opposites: south
  2. [B1] of a wind, blowing from the north
     - example: A cold north wind blew across the fields all night.
     - opposites: south
**Lexeme 3: north**, ADV
- said: /ˈnɔːɹθ/ (en-US), /ˈnɔːθ/ (en-GB)
  1. [A2] towards or in the direction of the north
     - example: We drove north for three hours without stopping.
     - frames: north of sth
     - opposites: south

### 4. master

**Lexeme 1: master**, NOUN
- grammar: countability Count
- said: /ˈmæs.tɚ/ (en-US), /ˈmɑːs.tə/ (en-GB)
  1. [B2] a man who has control over servants, workers or an animal
     - example: The dog waited by the door for its master to return.
     - opposites: servant
  2. [B2] a person who is extremely skilled at a particular activity
     - example: He was a true master of the short story.
     - frames: a master of sth
  3. [B1] a university degree taken after a first degree
     - example: After graduating, she did a master's in economics.
     - frames: a master's (degree) in sth
  4. [C1] an original recording or document from which copies are made
     - example: All the copies were made from a single master.
  5. [C1] a male teacher, especially at a private school
     - example: Our history master was strict but very fair.
     - labels: time dated
     - regions: en-GB
**Lexeme 2: master**, VERB
- grammar: valency Tr
- said: /ˈmæs.tɚ/ (en-US), /ˈmɑːs.tə/ (en-GB)
  1. [B2] to learn how to do or use something very well
     - example: It takes years to master a foreign language.
  2. [C1] to manage to control a strong feeling or a difficult situation
     - example: She finally mastered her fear of flying.
**Lexeme 3: master**, ADJ
- grammar: position Attr; comparable False
- said: /ˈmæs.tɚ/ (en-US), /ˈmɑːs.tə/ (en-GB)
  1. [B2] main or most important, or controlling all others of its kind
     - example: The master bedroom has its own bathroom.
  2. [C1] having great skill in a trade after long training
     - example: My grandfather worked as a master carpenter all his life.

### 5. tour

**Lexeme 1: tour**, NOUN
- grammar: countability Count
- said: /ˈtʊɹ/ (en-US), /ˈtʊə/ (en-GB), /ˈtɔː/ (en-GB)
  1. [A2] a journey for pleasure during which you visit several different places
     - example: We're planning a cycling tour of the French countryside.
     - frames: a tour of/around sth
  2. [A2] a short visit around a building, city or site to look at it
     - example: A guide will take you on a tour of the castle.
     - frames: a guided tour; a tour of sth
  3. [B1] a series of shows or games given in different places by a band, team or performer
     - example: The band is on tour in Asia until March.
     - frames: on tour; a world tour
  4. [C1] a period of time a soldier or official spends working in another place
     - example: He returned home after two tours of duty overseas.
     - frames: a tour of duty
     - labels: domain military
**Lexeme 2: tour**, VERB
- grammar: valency Both
- said: /ˈtʊɹ/ (en-US), /ˈtʊə/ (en-GB), /ˈtɔː/ (en-GB)
  1. [B1] to travel around a place for pleasure, visiting different parts of it
     - example: After graduating, they toured Scotland in an old van.
     - grammar: valency Tr
  2. [B2] to travel to different places giving performances or playing games
     - example: The orchestra will tour the United States next spring.
     - grammar: valency Both

### 6. rushed

**Lexeme 1: rushed**, ADJ
- grammar: comparable True
- said: /ˈɹʌʃt/ (en-US), /ˈɹʌʃt/ (en-GB)
  1. [B2] done too quickly, without enough time or care
     - example: The ending of the film felt rushed and hard to follow.
     - opposites: unhurried
  2. [B2] having too much to do in too little time
     - example: Sorry I can't talk now, I'm really rushed this morning.
     - grammar: position Pred
- relations: derived_from rush (VERB)

### 7. algorithm

**Lexeme 1: algorithm**, NOUN
- grammar: countability Count
- said: /ˈæl.ɡə.ɹɪ.ðəm/ (en-US), /ˈæl.ɡə.ɹɪ.ðəm/ (en-GB)
  1. [B2] a set of rules or steps for solving a problem, especially by a computer
     - example: Search engines use a complex algorithm to rank web pages.
     - frames: an algorithm for sth; an algorithm for doing sth

### 8. lucy

**Lexeme 1: Lucy**, PROPN
- said: /ˈluː.siː/ (en-US), /ˈluː.siː/ (en-GB)
  1. [A1] a first name given to girls and women in English-speaking countries
     - example: My best friend at school was a girl called Lucy.

### 9. windward

**Lexeme 1: windward**, ADJ
- grammar: comparable False
- said: /ˈwɪnd.wɚd/ (en-US), /ˈwɪnd.wəd/ (en-GB)
  1. [C1] on or facing the side from which the wind is blowing
     - example: Most of the rain falls on the windward slopes of the mountains.
     - frames: the windward side of sth
     - opposites: leeward
- relations: derived_from wind (NOUN)
**Lexeme 2: windward**, NOUN
- grammar: countability Mass; number Sing
- said: /ˈwɪnd.wɚd/ (en-US), /ˈwɪnd.wəd/ (en-GB)
  1. [C2] the side or direction from which the wind is blowing
     - example: The captain ordered the crew to steer to windward.
     - frames: to windward; to windward of sth
     - labels: domain nautical
     - opposites: leeward
- relations: derived_from wind (NOUN)

### 10. immunotherapy

**Lexeme 1: immunotherapy**, NOUN
- grammar: countability Both
- said: /ˌɪm.jə.noʊ.ˈθɛɹ.ə.pi/ (en-US), /ˌɪm.jʊ.nəʊ.ˈθe.ɹə.pi/ (en-GB)
  1. [C1] medical treatment that helps the body's own natural defences fight a disease
     - example: Doctors are now using immunotherapy to treat some kinds of cancer.
     - labels: domain medicine

### 11. abs

**Lexeme 1: abs**, NOUN
- grammar: number Plur; countability Count
- said: /ˈæbz/ (en-US), /ˈæbz/ (en-GB)
  1. [B2] the muscles across the front of the stomach
     - example: Doing sit-ups every morning will help strengthen your abs.
     - labels: formality informal
- relations: derived_from abdominal (NOUN)

### 12. mib

**Lexeme 1: mib**, NOUN
- grammar: countability Count
- said: /ˈmɪb/ (en-US), /ˈmɪb/ (en-GB)
  1. [C2] a small glass ball used in a children's game, especially as a target
     - example: The boys knelt in the dirt, trying to knock the mibs out of the ring.
     - labels: rare True, time dated
     - regions: en-US only

### 13. mating

**Lexeme 1: mating**, NOUN
- grammar: countability Both
- said: /ˈmeɪ.tɪŋ/ (en-US), /ˈmeɪ.tɪŋ/ (en-GB)
  1. [C1] the joining of a male and female animal to produce young
     - example: For many birds, the mating season begins in early spring.
     - frames: mating season; mating call
- relations: verbal_noun_of mate (VERB)

### 14. benchmark

**Lexeme 1: benchmark**, NOUN
- grammar: countability Count
- said: /ˈbɛntʃ.mɑːɹk/ (en-US), /ˈbentʃ.mɑːk/ (en-GB)
  1. [C1] a standard or level used to judge the quality of other things
     - example: Her first novel set a benchmark for young writers.
     - frames: a benchmark for sth; set a benchmark
  2. [C2] a standard test used to measure how fast or well a computer works
     - example: The new laptop scored highly on every benchmark we ran.
     - labels: domain computing
**Lexeme 2: benchmark**, VERB
- grammar: valency Tr
- said: /ˈbɛntʃ.mɑːɹk/ (en-US), /ˈbentʃ.mɑːk/ (en-GB)
  1. [C2] to measure or compare something against a fixed standard
     - example: We benchmark our prices against those of our main competitors.
     - frames: benchmark sth against sth

### 15. inaccuracy

**Lexeme 1: inaccuracy**, NOUN
- grammar: countability Both
- said: /ɪn.ˈæk.jɚ.ə.si/ (en-US), /ɪn.ˈæk.jə.ɹə.si/ (en-GB)
  1. [C1] a mistake in a piece of information, a report or a calculation
     - example: The article contains several inaccuracies about the town's history.
     - frames: an inaccuracy in sth
     - grammar: countability Count
  2. [C1] lack of correctness or exactness
     - example: Critics complained about the inaccuracy of the weather forecasts.
     - frames: the inaccuracy of sth
     - grammar: countability Mass
     - opposites: accuracy
- relations: abstract_noun_of inaccurate (ADJ)

### 16. toward

**Lexeme 1: toward**, ADP
- said: /ˈtɔːɹd/ (en-US), /tə.ˈwɔːɹd/ (en-US), /tə.ˈwɔːd/ (en-GB), /ˈtɔːd/ (en-GB)
- spellings: toward (en-US); towards (en-GB, en-AU, en-IN)
  1. [A2] in the direction of someone or something
     - example: The children ran toward the beach as soon as they arrived.
  2. [B1] in relation to someone or something, especially about feelings or behaviour
     - example: Her attitude toward her job has changed a lot recently.
     - frames: attitude toward sb/sth; feel sth toward sb
  3. [B1] near or a little before a particular time
     - example: It usually gets busier toward the end of the week.
     - frames: toward the end of sth
  4. [B2] as a help in paying for or achieving something
     - example: We're saving a little each month toward a new car.
     - frames: put money toward sth; a step toward sth

### 17. rep

**Lexeme 1: rep**, NOUN
- grammar: countability Count
- said: /ˈɹɛp/ (en-US), /ˈɹep/ (en-GB)
  1. [B2] a person who speaks or acts for a company or group
     - example: A sales rep will call you tomorrow to arrange a visit.
     - frames: sales rep; union rep
     - labels: formality informal
  2. [B2] one single movement of an exercise, done many times
     - example: Do three sets of ten reps with each arm.
     - labels: formality informal, domain sport
  3. [C1] the opinion that people generally have of someone
     - example: That kind of behaviour could ruin your rep at school.
     - labels: formality slang
- relations: derived_from representative (NOUN); derived_from repetition (NOUN); derived_from reputation (NOUN)

### 18. closed

**Lexeme 1: closed**, ADJ
- said: /ˈkloʊzd/ (en-US), /ˈkləʊzd/ (en-GB)
  1. [A1] not open, so that nothing can go in or out
     - example: Keep your eyes closed until I tell you to look.
     - opposites: open
  2. [A2] not open for people to visit, shop or do business
     - example: Most of the shops here are closed on Sundays.
     - frames: closed to sb
     - grammar: position Pred
     - opposites: open
  3. [B2] only for a particular group of people, not for everyone
     - example: The talks took place in a closed meeting.
     - grammar: position Attr
     - opposites: open
  4. [C1] not willing to accept new ideas or influences from outside
     - example: He has a closed mind when it comes to politics.
     - labels: figurative True
     - opposites: open
- relations: derived_from close (VERB)

### 19. carpet

**Lexeme 1: carpet**, NOUN
- grammar: countability Both
- said: /ˈkɑːɹ.pət/ (en-US), /ˈkɑː.pɪt/ (en-GB)
  1. [A2] a thick soft covering for a floor, often made of wool
     - example: We spilled coffee on the new carpet in the living room.
  2. [C1] a thick layer of something covering the ground
     - example: In autumn the path is hidden under a carpet of leaves.
     - frames: a carpet of sth
     - labels: figurative True
     - grammar: countability Count
**Lexeme 2: carpet**, VERB
- grammar: valency Tr
- said: /ˈkɑːɹ.pət/ (en-US), /ˈkɑː.pɪt/ (en-GB)
  1. [C1] to cover a floor with thick soft material
     - example: They're going to carpet the stairs next month.
  2. [C1] to cover the ground thickly with something
     - example: Each spring the woods are carpeted with bluebells.
     - frames: be carpeted with sth
     - labels: figurative True
- relations: derived_from carpet (NOUN)

### 20. scoundrel

**Lexeme 1: scoundrel**, NOUN
- grammar: countability Count
- said: /ˈskaʊn.dɹəl/ (en-US), /ˈskaʊn.dɹəl/ (en-GB)
  1. [C1] a man who treats other people badly, especially by being dishonest
     - example: That scoundrel took all her savings and disappeared.
     - labels: time dated, attitude disapproving

### 21. crony

**Lexeme 1: crony**, NOUN
- grammar: countability Count
- said: /ˈkɹoʊ.ni/ (en-US), /ˈkɹəʊ.ni/ (en-GB)
  1. [C1] a close friend or supporter, especially one given unfair help by someone powerful
     - example: The governor gave all the best jobs to his old cronies.
     - frames: sb's cronies
     - labels: formality informal, attitude disapproving
     - grammar: note usually plural

### 22. identically

**Lexeme 1: identically**, ADV
- said: /aɪ.ˈdɛn.tɪ.kli/ (en-US), /ɪ.ˈdɛn.tɪ.kli/ (en-US, less_common), /aɪ.ˈden.tɪ.kli/ (en-GB), /ɪ.ˈden.tɪ.kli/ (en-GB, less_common)
  1. [C1] in exactly the same way, with no differences at all
     - example: The two little girls were dressed identically for the party.
- relations: adverb_of identical (ADJ)

### 23. multidimensional

**Lexeme 1: multidimensional**, ADJ
- grammar: comparable True
- said: /ˌmʌl.ti.dɪ.ˈmɛn.ʃə.nəl/ (en-US), /ˌmʌl.taɪ.daɪ.ˈmɛn.ʃə.nəl/ (en-US), /ˌmʌl.ti.daɪ.ˈmen.ʃə.nəl/ (en-GB), /ˌmʌl.ti.dɪ.ˈmen.ʃə.nəl/ (en-GB)
  1. [C1] having many different parts, sides or aspects
     - example: Poverty is a multidimensional problem with no simple solution.
     - opposites: one-dimensional
  2. [C2] having more than three, or many, measurable directions
     - example: The data are stored in a multidimensional array.
     - labels: domain mathematics
     - grammar: comparable False
- relations: derived_from dimensional (ADJ)

### 24. portico

**Lexeme 1: portico**, NOUN
- grammar: countability Count
- said: /ˈpɔːɹ.tɪ.koʊ/ (en-US), /ˈpɔː.tɪ.kəʊ/ (en-GB)
- forms: porticoes [Number=Plur] /ˈpɔːɹ.tɪ.koʊz/ /ˈpɔː.tɪ.kəʊz/; porticos [Number=Plur] /ˈpɔːɹ.tɪ.koʊz/ /ˈpɔː.tɪ.kəʊz/
  1. [C2] a covered entrance to a building, with a roof held up by columns
     - example: Visitors waited under the portico until the rain stopped.

### 25. double

**Lexeme 1: double**, ADJ
- grammar: comparable False; position Attr
- said: /ˈdʌb.əl/ (en-US), /ˈdʌb.əl/ (en-GB)
  1. [A2] twice the usual size, amount or strength
     - example: I need a double espresso before the meeting starts.
     - opposites: single
  2. [A2] made for or used by two people
     - example: Would you prefer a double room or two singles?
     - opposites: single
  3. [B1] having two parts or things that are the same
     - example: Go through the double doors at the end of the corridor.
**Lexeme 2: double**, VERB
- grammar: valency Both
- said: /ˈdʌb.əl/ (en-US), /ˈdʌb.əl/ (en-GB)
  1. [B1] to become, or make something, twice as big or as many
     - example: House prices in the area have doubled in ten years.
     - opposites: halve
  2. [B2] to have a second use or job as well as the main one
     - example: In our small flat, the sofa doubles as a bed.
     - frames: double as sth
     - grammar: valency Intr
**Lexeme 3: double**, NOUN
- grammar: countability Count
- said: /ˈdʌb.əl/ (en-US), /ˈdʌb.əl/ (en-GB)
  1. [B2] a person who looks exactly like someone else
     - example: That actor is the double of my old maths teacher.
     - frames: sb's double; the double of sb
  2. [B2] a glass of a strong alcoholic drink with twice the usual amount
     - example: He ordered another whisky and asked to make it a double.
  3. [B1] a hotel room with one large bed for two people
     - example: We booked a double for three nights by the sea.
**Lexeme 4: double**, ADV
- said: /ˈdʌb.əl/ (en-US), /ˈdʌb.əl/ (en-GB)
  1. [B2] at twice the usual amount or rate
     - example: Workers get paid double if they work on holidays.
     - frames: pay/charge double
  2. [B2] so that you see two of everything, or something is in two layers
     - example: After hitting my head, I was seeing double for an hour.
     - frames: see double; fold sth double
**Lexeme 5: double**, DET
- said: /ˈdʌb.əl/ (en-US), /ˈdʌb.əl/ (en-GB)
  1. [B2] twice as much or as many as something
     - example: Her new job pays almost double her old salary.
     - frames: double the + noun

### 26. sit

**Lexeme 1: sit**, VERB
- grammar: irregular True; valency Both
- said: /ˈsɪt/ (en-US), /ˈsɪt/ (en-GB)
- forms: sat [Tense=Past, VerbForm=Fin,Part] /ˈsæt/ /ˈsæt/
  1. [A1] to rest your weight on your bottom, with your back upright
     - example: Come and sit beside me on the sofa.
     - frames: sit on/in/at sth; sit down
     - grammar: valency Intr
     - opposites: stand
  2. [B2] to stay in one place for a long time without being used
     - example: That old bike just sits in the garage all winter.
     - grammar: valency Intr
  3. [C1] to be a member of a committee, a court or a parliament
     - example: I have sat on the school board for six years.
     - frames: sit on sth
     - grammar: valency Intr
  4. [B2] to take a written test at school or university
     - example: Most students sit their final exams in June.
     - frames: sit an exam; sit for sth
     - regions: en-GB, en-AU, en-IN
     - grammar: valency Tr
  5. [C1] to hold an official meeting, as a court or parliament does
     - example: The court sits again next week to hear the case.
     - grammar: valency Intr

### 27. smashing

**Lexeme 1: smashing**, ADJ
- said: /ˈsmæʃ.ɪŋ/ (en-US), /ˈsmæʃ.ɪŋ/ (en-GB)
  1. [B2] very good, enjoyable or attractive; excellent
     - example: We had a smashing time at the seaside last weekend.
     - labels: formality informal, time dated
     - regions: en-GB
     - opposites: awful
- relations: derived_from smash (VERB)

### 28. given

**Lexeme 1: given**, ADJ
- grammar: comparable False
- said: /ˈɡɪv.ən/ (en-US), /ˈɡɪv.ən/ (en-GB)
  1. [B2] already decided, named or agreed, often one particular case
     - example: Only a few people can use the room at any given time.
     - grammar: position Attr
  2. [C1] often doing something or likely to behave in a certain way
     - example: My uncle is given to long speeches after dinner.
     - frames: be given to sth; be given to doing sth
     - labels: formality formal
     - grammar: position Pred
- relations: derived_from give (VERB)
**Lexeme 2: given**, ADP
- said: /ˈɡɪv.ən/ (en-US), /ˈɡɪv.ən/ (en-GB)
  1. [B2] when you think about or take into account a particular fact
     - example: Given the bad weather, the match went surprisingly well.
     - frames: given sth; given that
- relations: derived_from give (VERB)
**Lexeme 3: given**, NOUN
- grammar: countability Count
- said: /ˈɡɪv.ən/ (en-US), /ˈɡɪv.ən/ (en-GB)
  1. [C1] a fact that everyone accepts as true or certain
     - example: In this job, working late is pretty much a given.
     - frames: take sth as a given
- relations: derived_from give (VERB)

### 29. mesothelioma

**Lexeme 1: mesothelioma**, NOUN
- grammar: countability Both
- said: /ˌmɛ.zə.ˌθiː.li.ˈoʊ.mə/ (en-US), /ˌmɛ.zoʊ.ˌθiː.li.ˈoʊ.mə/ (en-US), /ˌmiː.səʊ.ˌθiː.li.ˈəʊ.mə/ (en-GB), /ˌme.zəʊ.ˌθiː.li.ˈəʊ.mə/ (en-GB)
  1. [C2] a cancer of the thin layer of tissue that covers the lungs and other organs
     - example: Years of working with asbestos left him with mesothelioma.
     - labels: domain medicine

### 30. barely

**Lexeme 1: barely**, ADV
- grammar: comparable False
- said: /ˈbɛɹ.li/ (en-US), /ˈbeə.li/ (en-GB)
  1. [B2] only just; almost not, or with great difficulty
     - example: I could barely hear him over the noise of the music.
  2. [C1] only a very short time before something else happened
     - example: We had barely sat down when the phone rang.
     - frames: barely ... when
- relations: adverb_of bare (ADJ)

### 31. modelling

**Lexeme 1: modelling**, NOUN
- grammar: countability Mass
- said: /ˈmɑː.də.lɪŋ/ (en-US), /ˈmɒ.də.lɪŋ/ (en-GB)
- spellings: modelling (en-GB, en-AU, en-IN); modeling (en-US)
  1. [B2] the job of wearing clothes for photographs or fashion shows
     - example: She started modelling at sixteen and now works in Paris.
  2. [C1] making a simple description of a system, often on a computer, to predict results
     - example: Climate modelling suggests that summers here will become much hotter.
  3. [B2] making small copies or shapes of things from clay, wood or similar materials
     - example: Modelling with clay is a popular activity for young children.
- relations: verbal_noun_of model (VERB)

### 32. mm

**Lexeme 1: mm**, INTJ
- said: /ˈm/ (en-US), /ˈm/ (en-GB)
  1. [A2] a sound made to show you are listening, agreeing, or thinking
     - example: Mm, I see what you mean about the price.
     - labels: formality informal
  2. [A2] a sound made to show that something tastes or smells good
     - example: Mm, this soup smells absolutely wonderful!
     - labels: formality informal

### 33. tomb

**Lexeme 1: tomb**, NOUN
- grammar: countability Count
- said: /ˈtuːm/ (en-US), /ˈtuːm/ (en-GB)
  1. [B1] a large grave, often a stone building, where a dead person is buried
     - example: Archaeologists discovered the tomb of an ancient king near the river.
- relations: confusable womb (NOUN): similar spelling and the same vowel sound, but a very different meaning

### 34. right

**Lexeme 1: right**, ADJ
- said: /ˈɹaɪt/ (en-US), /ˈɹaɪt/ (en-GB)
  1. [A1] correct or true, without any mistakes
     - example: Is that the right answer to the third question?
     - opposites: wrong
  2. [A1] on the side of the body that is east when facing north
     - example: She writes with her right hand.
     - grammar: position Attr
     - opposites: left
  3. [A2] most suitable or best for a particular person or situation
     - example: I think he's the right person for the job.
     - frames: right for sb/sth
  4. [B1] morally good, fair or acceptable
     - example: It isn't right to blame her for your mistake.
     - frames: it is right to do sth
     - opposites: wrong
- relations: confusable write (VERB): sounds exactly the same as right; confusable rite (NOUN): sounds exactly the same as right
**Lexeme 2: right**, ADV
- said: /ˈɹaɪt/ (en-US), /ˈɹaɪt/ (en-GB)
  1. [A1] towards or on the side of the body that is east when facing north
     - example: Turn right at the traffic lights.
     - opposites: left
  2. [A2] exactly in a particular place or position
     - example: The bus stop is right outside our house.
     - frames: right in/behind/outside sth
  3. [A2] immediately, without any delay
     - example: Wait here, I'll be right back.
     - frames: right now; right away
  4. [B1] in the correct way, without making a mistake
     - example: Did you spell my name right on the form?
     - opposites: wrong
  5. [B1] completely, all the way
     - example: We stayed right to the end of the concert.
     - frames: right to/through sth
**Lexeme 3: right**, NOUN
- grammar: countability Both
- said: /ˈɹaɪt/ (en-US), /ˈɹaɪt/ (en-GB)
  1. [B1] a moral or legal claim to have or do something
     - example: Every citizen has the right to vote.
     - frames: the right to do sth; have a right to sth
     - grammar: countability Count
  2. [A1] the side or direction that is east when facing north
     - example: Our school is the second building on your right.
     - frames: on the right; to the right
     - grammar: number Sing
     - opposites: left
  3. [B1] behaviour that is morally good or fair
     - example: Young children must learn the difference between right and wrong.
     - grammar: countability Mass
     - opposites: wrong
  4. [B2] political parties or people with conservative ideas
     - example: Support for the right has grown in recent years.
     - labels: domain politics
     - grammar: number Sing
     - opposites: left
**Lexeme 4: right**, VERB
- grammar: valency Tr
- said: /ˈɹaɪt/ (en-US), /ˈɹaɪt/ (en-GB)
  1. [C1] to correct an unfair situation or a bad thing that was done
     - example: The new law tries to right old injustices.
     - frames: right a wrong
  2. [C1] to bring something back to its normal upright position
     - example: After the wave passed, the small boat righted itself.
**Lexeme 5: right**, INTJ
- said: /ˈɹaɪt/ (en-US), /ˈɹaɪt/ (en-GB)
  1. [A2] used to get attention before starting something new
     - example: Right, let's begin today's lesson.
     - labels: formality informal
  2. [A2] used to show that you agree or understand
     - example: "Meet me at eight." "Right, see you then."
     - labels: formality informal

### 35. slang

**Lexeme 1: slang**, NOUN
- grammar: countability Mass
- said: /ˈslæŋ/ (en-US), /ˈslæŋ/ (en-GB)
  1. [B2] very informal words and phrases, often used by a particular group of people
     - example: Teenagers often use slang that their parents don't understand.
**Lexeme 2: slang**, VERB
- grammar: valency Tr
- said: /ˈslæŋ/ (en-US), /ˈslæŋ/ (en-GB)
  1. [C2] to shout angry, rude words at someone during an argument
     - example: The two drivers got out and started slanging each other.
     - labels: formality informal
     - regions: en-GB, en-AU

### 36. creepy

**Lexeme 1: creepy**, ADJ
- grammar: comparable True
- said: /ˈkɹiː.pi/ (en-US), /ˈkɹiː.pi/ (en-GB)
  1. [B1] strange and slightly frightening in a way that makes you nervous
     - example: That old house at the end of the street looks really creepy.
     - labels: formality informal
  2. [B2] of a person or behaviour: unpleasant and making you feel uncomfortable or unsafe
     - example: There's something creepy about the way he keeps staring at us.
     - labels: formality informal, attitude disapproving
- relations: derived_from creep (VERB)

### 37. yup

**Lexeme 1: yup**, INTJ
- said: /ˈjʌp/ (en-US), /ˈjʌp/ (en-GB)
  1. [A2] used in conversation to say yes or agree
     - example: "Are you ready to go?" "Yup, just let me grab my coat."
     - labels: formality informal
     - opposites: nope
- relations: derived_from yes (INTJ)

### 38. froth

**Lexeme 1: froth**, NOUN
- grammar: countability Mass
- said: /ˈfɹɔːθ/ (en-US), /ˈfɹɑːθ/ (en-US), /ˈfɹɒθ/ (en-GB)
  1. [B2] a mass of small bubbles on top of a liquid
     - example: I love the thick froth on top of my coffee.
     - frames: the froth on sth
  2. [C2] ideas, talk or entertainment that seem attractive but have no real value
     - example: Most of the speech was just froth with no real plans.
     - labels: figurative True, attitude disapproving
**Lexeme 2: froth**, VERB
- grammar: valency Both; irregular False
- said: /ˈfɹɔːθ/ (en-US), /ˈfɹɑːθ/ (en-US), /ˈfɹɒθ/ (en-GB)
  1. [B2] to produce or be covered with a mass of small bubbles
     - example: Waves frothed around the rocks during the storm.
     - frames: froth at the mouth
     - grammar: valency Intr
  2. [C1] to make a liquid, especially milk, full of small bubbles
     - example: You can froth the milk with a small whisk.
     - frames: froth sth
     - grammar: valency Tr

### 39. piece

**Lexeme 1: piece**, NOUN
- grammar: countability Count
- said: /ˈpiːs/ (en-US), /ˈpiːs/ (en-GB)
  1. [A1] a part of something that has been cut, broken or divided
     - example: Would you like another piece of cake?
     - frames: a piece of sth; in pieces
  2. [A2] a single item or amount of a particular kind of thing
     - example: She gave me a useful piece of advice about the interview.
     - frames: a piece of sth
  3. [B1] a single work of art, music or writing
     - example: The orchestra played a short piece by a young composer.
     - frames: a piece of music/writing
  4. [B1] one of the small figures or objects used in a board game
     - example: Put all the chess pieces back in the box, please.
  5. [B2] a coin of a particular value
     - example: He found a fifty-cent piece under the sofa.
- relations: confusable peace (NOUN): sounds exactly the same as peace, a time without war
**Lexeme 2: piece**, VERB
- grammar: valency Tr
- said: /ˈpiːs/ (en-US), /ˈpiːs/ (en-GB)
  1. [C1] to form a complete story or object from separate parts or facts
     - example: Police are still trying to piece together what happened that night.
     - frames: piece sth together
- relations: derived_from piece (NOUN)

### 40. fulcrum

**Lexeme 1: fulcrum**, NOUN
- grammar: countability Count
- said: /ˈfʊl.kɹəm/ (en-US), /ˈfʌl.kɹəm/ (en-US), /ˈfʊl.kɹəm/ (en-GB), /ˈfʌl.kɹəm/ (en-GB)
- forms: fulcrums [Number=Plur] /ˈfʊl.kɹəmz/ /ˈfʊl.kɹəmz/; fulcra [Number=Plur] /ˈfʊl.kɹə/ /ˈfʊl.kɹə/ (less common, mainly in technical writing)
  1. [C1] the point on which a lever rests or turns when it moves something
     - example: Place the stick over a stone so it acts as a fulcrum.
  2. [C2] the most important part on which an activity or situation depends
     - example: Trust is the fulcrum on which every good partnership rests.
     - frames: the fulcrum of sth
     - labels: figurative True, formality formal

### 41. manner

**Lexeme 1: manner**, NOUN
- grammar: countability Count
- said: /ˈmæn.ɚ/ (en-US), /ˈmæn.ə/ (en-GB)
  1. [B1] the way in which something is done or happens
     - example: Complaints should be dealt with in a calm and professional manner.
     - frames: in a … manner; the manner in which
  2. [B2] the way a person usually behaves or speaks towards other people
     - example: Her friendly manner made everyone feel welcome at once.
  3. [A2] polite ways of behaving that are accepted as correct in society
     - example: It's bad manners to talk with your mouth full.
     - frames: good/bad manners; have no manners
     - grammar: number Plur
  4. [C1] a kind or type of person or thing
     - example: The shop sells all manner of strange old objects.
     - frames: all manner of sth
     - labels: formality formal
- relations: confusable manor (NOUN): sounds the same but means a large country house

### 42. understanding

**Lexeme 1: understanding**, NOUN
- grammar: countability Both
- said: /ˌʌn.dɚ.ˈstæn.dɪŋ/ (en-US), /ˌʌn.də.ˈstæn.dɪŋ/ (en-GB)
  1. [B1] knowledge of how something works or what something means
     - example: You need a basic understanding of grammar to pass this course.
     - frames: an understanding of sth
     - grammar: countability Mass
  2. [B2] sympathy and patience shown towards other people's feelings or problems
     - example: Thank you for your understanding while the lift is being repaired.
     - grammar: countability Mass
  3. [B2] the way a person thinks a fact or situation should be interpreted
     - example: My understanding is that the meeting has been moved to Friday.
     - frames: sb's understanding is that ...
     - grammar: countability Mass
  4. [C1] an informal agreement between people, often not written down
     - example: After a long talk, the two neighbours came to an understanding.
     - frames: come to an understanding; reach an understanding with sb
     - grammar: countability Count
- relations: verbal_noun_of understand (VERB)
**Lexeme 2: understanding**, ADJ
- grammar: comparable True
- said: /ˌʌn.dɚ.ˈstæn.dɪŋ/ (en-US), /ˌʌn.də.ˈstæn.dɪŋ/ (en-GB)
  1. [B1] kind and patient towards other people and their problems
     - example: My boss was very understanding when I was ill last month.
     - frames: understanding about sth
- relations: derived_from understand (VERB)

### 43. ary

**Lexeme 1: ary**, DET
- grammar: note usually before a singular noun or in ary one
- said: /ˈɛɹ.i/ (en-US), /ˈeə.ɹi/ (en-GB)
  1. [C2] used to mean any, or even one, of something
     - example: If you see ary snake by the creek, come and tell me.
     - frames: ary one; ary a + noun
     - labels: normativity nonstandard, formality informal, rare True
     - regions: en-US mainly
     - standard word: any
- relations: confusable nary (DET): nary means not one, the opposite of ary

### 44. tory

**Lexeme 1: Tory**, NOUN
- grammar: countability Count
- said: /ˈtɔː.ɹi/ (en-US), /ˈtɔː.ɹi/ (en-GB)
  1. [B2] a member or supporter of the British Conservative Party
     - example: Many lifelong Tories voted against the party this time.
     - labels: domain politics
  2. [C1] an American colonist who supported Britain during the American Revolution
     - example: After the war, thousands of Tories fled north to Canada.
     - labels: time historical
**Lexeme 2: Tory**, ADJ
- grammar: comparable False; position Attr
- said: /ˈtɔː.ɹi/ (en-US), /ˈtɔː.ɹi/ (en-GB)
  1. [B2] belonging to or supporting the British Conservative Party
     - example: The new Tory government promised lower taxes for families.
     - labels: domain politics

### 45. flirtation

**Lexeme 1: flirtation**, NOUN
- grammar: countability Both
- said: /flɝ.ˈteɪ.ʃən/ (en-US), /flɜː.ˈteɪ.ʃən/ (en-GB)
  1. [C1] behaviour that playfully shows romantic or sexual interest in someone, without serious intentions
     - example: Their harmless flirtation at the office party surprised everyone.
     - frames: flirtation with sb; flirtation between A and B
  2. [C1] a short period of being interested in an idea or activity
     - example: His brief flirtation with politics ended after one election.
     - frames: flirtation with sth
     - labels: figurative True
     - grammar: countability Count
- relations: verbal_noun_of flirt (VERB)

### 46. where

**Lexeme 1: where**, ADV
- said: /ˈwɛɹ/ (en-US), /ˈweə/ (en-GB)
  1. [A1] in, at or to what place; used to ask questions
     - example: Where do you keep the spare keys?
     - frames: where to; where from
  2. [A2] used after a noun naming a place, meaning at, in or to which
     - example: This is the café where we first met.
  3. [B1] in what situation or at what point in a process
     - example: I'm not sure where this argument is going.
     - labels: figurative True
- relations: confusable wear (VERB): sounds the same; confusable be (AUX): looks like were, the past form of be
**Lexeme 2: where**, SCONJ
- said: /ˈwɛɹ/ (en-US), /ˈweə/ (en-GB)
  1. [A2] in, at or to the place that
     - example: Put the box where everyone can see it.
  2. [B2] in cases or situations in which
     - example: Where possible, try to travel by public transport.
     - frames: where possible; where necessary

### 47. appointment

**Lexeme 1: appointment**, NOUN
- grammar: countability Count
- said: /ə.ˈpɔɪnt.mənt/ (en-US), /ə.ˈpɔɪnt.mənt/ (en-GB)
  1. [A2] an arrangement to see someone at a particular time, often for a service
     - example: I've got a dentist's appointment at three this afternoon.
     - frames: appointment with sb; make an appointment; by appointment
  2. [B2] the choosing of someone for an official job or position; also that job
     - example: Her appointment as head teacher was announced on Monday.
     - frames: appointment of sb as sth; appointment to sth
- relations: derived_from appoint (VERB)

### 48. guesswork

**Lexeme 1: guesswork**, NOUN
- grammar: countability Mass
- said: /ˈɡɛs.wɝk/ (en-US), /ˈɡes.wɜːk/ (en-GB)
- parts: guess (guess) + work (work)
  1. [B2] trying to find an answer without having enough facts or information
     - example: Without reliable data, our sales forecast is mostly guesswork.
     - frames: take the guesswork out of sth; pure guesswork

### 49. stretching

**Lexeme 1: stretching**, NOUN
- grammar: countability Mass
- said: /ˈstɹɛtʃ.ɪŋ/ (en-US), /ˈstɹetʃ.ɪŋ/ (en-GB)
  1. [B1] exercises that make your muscles longer and your body loose
     - example: Ten minutes of stretching before a run helps prevent injuries.
- relations: verbal_noun_of stretch (VERB)

### 50. mike

**Lexeme 1: mike**, NOUN
- grammar: countability Count
- said: /ˈmaɪk/ (en-US), /ˈmaɪk/ (en-GB)
- spellings: mike; mic
  1. [B2] a device you speak or sing into to make your voice louder or record it
     - example: Can everyone at the back hear me, or is this mike off?
     - labels: formality informal
- relations: derived_from microphone (NOUN)
**Lexeme 2: mike**, VERB
- grammar: valency Tr
- said: /ˈmaɪk/ (en-US), /ˈmaɪk/ (en-GB)
  1. [C2] to fit a person or place with a microphone
     - example: The sound engineer will mike you up before the interview starts.
     - frames: mike sb up
     - labels: formality informal
- relations: derived_from mike (NOUN)

### 51. ladies

**Lexeme 1: ladies**, NOUN
- grammar: number Sing; note usually 'the ladies'; takes a singular verb; also written ladies'
- said: /ˈleɪ.diːz/ (en-US), /ˈleɪ.diːz/ (en-GB)
  1. [B1] a toilet for women in a public building such as a restaurant
     - example: Excuse me, could you tell me where the ladies is?
     - regions: en-GB, en-AU
     - opposites: gents
- relations: derived_from lady (NOUN)

### 52. luke

**Lexeme 1: Luke**, PROPN
- said: /ˈluːk/ (en-US), /ˈluːk/ (en-GB)
  1. [A2] a common first name for a boy or man
     - example: Their youngest son is called Luke, after his grandfather.
  2. [C1] the third book of the New Testament, telling the life of Jesus
     - example: The story of the good Samaritan is told only in Luke.
     - labels: domain religion

### 53. procreation

**Lexeme 1: procreation**, NOUN
- grammar: countability Mass
- said: /ˌpɹoʊ.kɹi.ˈeɪ.ʃən/ (en-US), /ˌpɹəʊ.kɹi.ˈeɪ.ʃən/ (en-GB)
  1. [C2] the process by which people or animals produce babies or young
     - example: Some religions teach that marriage exists mainly for procreation.
     - labels: formality formal
- relations: verbal_noun_of procreate (VERB)

### 54. straight

**Lexeme 1: straight**, ADJ
- grammar: comparable True
- said: /ˈstɹeɪt/ (en-US), /ˈstɹeɪt/ (en-GB)
  1. [A2] going in one direction without bending or curving
     - example: Use a ruler to draw a straight line across the page.
     - opposites: crooked
  2. [A2] of hair, without curls or waves
     - example: She has long straight hair and brown eyes.
     - opposites: curly
  3. [B2] honest and open, saying clearly what you think
     - example: I just want a straight answer to a simple question.
     - frames: be straight with sb
  4. [B2] coming one after another without a break
     - example: The team has now won five straight games.
     - grammar: position Attr
  5. [B2] sexually attracted only to people of the opposite sex
     - example: Most of his friends from school are straight.
     - labels: formality informal
     - opposites: gay
- relations: confusable strait (NOUN): sounds the same; a strait is a narrow stretch of sea
**Lexeme 2: straight**, ADV
- said: /ˈstɹeɪt/ (en-US), /ˈstɹeɪt/ (en-GB)
  1. [A2] in a line that does not bend or turn
     - example: Keep going straight ahead until you reach the bridge.
  2. [A2] directly and immediately, without stopping or doing anything else first
     - example: After work, I'm going straight home to bed.
     - frames: straight to sth
  3. [B2] one after another, without a break
     - example: It rained for four days straight last week.
**Lexeme 3: straight**, NOUN
- grammar: countability Count
- said: /ˈstɹeɪt/ (en-US), /ˈstɹeɪt/ (en-GB)
  1. [C1] a part of a road or racetrack that does not curve
     - example: The horses were neck and neck as they entered the final straight.
     - regions: en-GB, en-AU
  2. [C1] a person who is attracted only to people of the opposite sex
     - example: The club welcomes gays and straights alike.
     - labels: formality informal

### 55. clear

**Lexeme 1: clear**, ADJ
- grammar: comparable True
- said: /ˈklɪɹ/ (en-US), /ˈklɪə/ (en-GB)
  1. [A2] easy to understand and not causing any confusion
     - example: Her instructions were short and very clear.
     - opposites: unclear
  2. [B1] obvious, so that nobody can have any doubt about it
     - example: It's clear that he doesn't want to come with us.
     - frames: it is clear that
  3. [B1] easy to see through, like glass or clean water
     - example: You can see the fish in the clear water.
     - opposites: opaque
  4. [B1] without clouds, mist or rain
     - example: On a clear night you can see thousands of stars.
     - opposites: cloudy
  5. [B1] sharp and easy to see or hear
     - example: The picture on this old television isn't very clear.
  6. [B1] not blocked or covered, so that you can pass freely
     - example: Make sure the road is clear before you cross.
  7. [B2] sure that you understand something correctly
     - example: Are you clear about what you have to do?
     - frames: be clear about sth
     - grammar: position Pred
**Lexeme 2: clear**, VERB
- grammar: valency Both
- said: /ˈklɪɹ/ (en-US), /ˈklɪə/ (en-GB)
  1. [B1] to remove things from a place so that it is empty or tidy
     - example: Could you help me clear the table after dinner?
     - frames: clear sth; clear sth from/off sth
     - grammar: valency Tr
  2. [B2] to become brighter as clouds or fog go away
     - example: The fog should clear by late morning.
     - frames: clear (up)
     - grammar: valency Intr
  3. [B2] to prove or decide that someone did nothing wrong
     - example: The court cleared him of all the charges.
     - frames: clear sb of sth
     - grammar: valency Tr
  4. [C1] to give official permission for something to happen
     - example: The new drug has not yet been cleared for sale.
     - frames: clear sth for sth; clear sth with sb
     - grammar: valency Tr
  5. [C1] to jump or go over something without touching it
     - example: Her horse cleared the last fence with ease.
     - grammar: valency Tr
**Lexeme 3: clear**, ADV
- said: /ˈklɪɹ/ (en-US), /ˈklɪə/ (en-GB)
  1. [B2] away from something, so as not to touch it
     - example: Please stand clear of the closing doors.
     - frames: stand clear of sth; keep clear of sth

### 56. opportunist

**Lexeme 1: opportunist**, NOUN
- grammar: countability Count
- said: /ˌɑː.pɚ.ˈtuː.nɪst/ (en-US), /ˌɒ.pə.ˈtjuː.nɪst/ (en-GB), /ˌɒ.pə.ˈtʃuː.nɪst/ (en-GB)
  1. [C1] a person who uses every chance to get an advantage, without caring about what is right
     - example: Some called him a political opportunist who changed sides for power.
     - labels: attitude disapproving
**Lexeme 2: opportunist**, ADJ
- grammar: comparable False; position Attr
- said: /ˌɑː.pɚ.ˈtuː.nɪst/ (en-US), /ˌɒ.pə.ˈtjuː.nɪst/ (en-GB), /ˌɒ.pə.ˈtʃuː.nɪst/ (en-GB)
  1. [C1] taking advantage of a chance when it comes, without planning ahead
     - example: Most burglaries in this area are carried out by opportunist thieves.

### 57. persecute

**Lexeme 1: persecute**, VERB
- grammar: valency Tr; irregular False
- said: /ˈpɝ.sɪ.kjuːt/ (en-US), /ˈpɝ.sə.kjuːt/ (en-US), /ˈpɜː.sɪ.kjuːt/ (en-GB)
  1. [C1] to treat people cruelly and unfairly, especially because of their race, religion or beliefs
     - example: For centuries the group was persecuted for its religious beliefs.
     - frames: persecute sb for sth
  2. [C2] to annoy or trouble someone again and again
     - example: Reporters kept persecuting the family with phone calls and visits.
     - frames: persecute sb with sth
- relations: confusable prosecute (VERB): looks and sounds similar; prosecute means to accuse someone officially in a court of law

### 58. whosoever

**Lexeme 1: whosoever**, PRON
- said: /ˌhuː.soʊ.ˈɛv.ɚ/ (en-US), /ˌhuː.səʊ.ˈev.ə/ (en-GB)
  1. [C2] any person at all who does something, no matter who
     - example: Whosoever breaks this law shall be punished without mercy.
     - labels: time archaic, formality formal

### 59. ford

**Lexeme 1: ford**, NOUN
- grammar: countability Count
- said: /ˈfɔːɹd/ (en-US), /ˈfɔːd/ (en-GB)
  1. [C1] a shallow place in a river where people can walk or drive across
     - example: We crossed the stream at a ford near the old mill.
**Lexeme 2: ford**, VERB
- grammar: valency Tr
- said: /ˈfɔːɹd/ (en-US), /ˈfɔːd/ (en-GB)
  1. [C2] to cross a river by walking or driving through shallow water
     - example: The soldiers had to ford the river before dawn.

### 60. typhus

**Lexeme 1: typhus**, NOUN
- grammar: countability Mass
- said: /ˈtaɪ.fəs/ (en-US), /ˈtaɪ.fəs/ (en-GB)
  1. [C2] a serious infectious disease spread by lice or fleas, causing fever and a red rash
     - example: Thousands of soldiers died of typhus during the long winter.
     - frames: die of typhus
- relations: confusable typhoid (NOUN): typhoid is a different fever caused by dirty food or water

### 61. modulate

**Lexeme 1: modulate**, VERB
- grammar: valency Both; irregular False
- said: /ˈmɑː.dʒə.leɪt/ (en-US), /ˈmɒ.dʒə.leɪt/ (en-GB), /ˈmɒ.djʊ.leɪt/ (en-GB)
  1. [C1] to change or control the amount, strength or effect of something
     - example: This drug helps to modulate the body's response to stress.
     - frames: modulate sth
     - labels: formality formal
     - grammar: valency Tr
  2. [C2] to change the tone, pitch or loudness of your voice
     - example: Good speakers modulate their voices to keep the audience interested.
     - frames: modulate your voice
     - grammar: valency Tr
  3. [C2] to vary the frequency or strength of a radio wave to carry a signal
     - example: The transmitter modulates the carrier wave with the sound signal.
     - labels: domain physics
     - grammar: valency Tr
  4. [C2] to move from one key to another within a piece of music
     - example: In the final verse, the song modulates from C to D.
     - frames: modulate from sth to sth
     - labels: domain music
     - grammar: valency Intr
- relations: confusable moderate (VERB): moderate means to make something less extreme, not to adjust or vary it

### 62. committee

**Lexeme 1: committee**, NOUN
- grammar: countability Count; note collective noun; in en-GB the verb may be singular or plural
- said: /kə.ˈmɪ.tiː/ (en-US), /kə.ˈmɪ.tiː/ (en-GB)
  1. [B1] a small group of people chosen to discuss, plan or decide things for a larger group
     - example: She has sat on the planning committee for three years.
     - frames: on a committee; sit on a committee
- relations: derived_from commit (VERB)

### 63. mahi

**Lexeme 1: mahi**, NOUN
- grammar: countability Both
- said: /ˈmɑː.hiː/ (en-US), /ˈmɑː.hiː/ (en-GB)
  1. [C1] a large brightly coloured sea fish, or its flesh eaten as food
     - example: We ordered grilled mahi with rice and black beans.
     - labels: formality informal
     - regions: en-US mainly
  2. [C2] work, especially hard work done for a group or community
     - example: Everyone put in a lot of mahi to finish the new hall.
     - labels: formality informal
     - regions: en-NZ only
     - grammar: countability Mass
- relations: derived_from mahi-mahi (NOUN)

### 64. shrew

**Lexeme 1: shrew**, NOUN
- grammar: countability Count
- said: /ˈʃɹuː/ (en-US), /ˈʃɹuː/ (en-GB)
  1. [C1] a very small animal like a mouse with a long pointed nose
     - example: A tiny shrew ran across the path and into the grass.
  2. [C2] a woman who is unpleasant, bad-tempered and often argues
     - example: In the old story, everyone in town calls her a shrew.
     - labels: time dated, attitude derogatory
- relations: confusable shrewd (ADJ): shrewd is an adjective meaning clever at judging situations, not a noun

### 65. hand

**Lexeme 1: hand**, NOUN
- grammar: countability Count
- said: /ˈhænd/ (en-US), /ˈhænd/ (en-GB)
  1. [A1] the part at the end of the arm, with fingers and a thumb
     - example: Wash your hands before you sit down to eat.
     - frames: by hand; hold hands
  2. [B1] help with doing a job or task
     - example: Could you give me a hand with these heavy boxes?
     - frames: give sb a hand; need a hand with sth
     - grammar: countability Count
  3. [B1] one of the thin moving pointers on a clock or watch
     - example: The minute hand had stopped just before twelve.
  4. [B2] a person who does physical work on a farm, ship or in a factory
     - example: They hired two extra hands to bring in the harvest.
  5. [B2] the set of playing cards one player holds in a game
     - example: I had a terrible hand, so I gave up early.
  6. [B2] a part or role in making something happen
     - example: Police believe she had a hand in the robbery.
     - frames: have a hand in sth
  7. [B2] a round of clapping to show approval of someone
     - example: Let's give our speakers a big hand, everyone!
     - frames: give sb a big hand
     - labels: formality informal
**Lexeme 2: hand**, VERB
- grammar: valency Tr
- said: /ˈhænd/ (en-US), /ˈhænd/ (en-GB)
  1. [A2] to give something to someone by passing it to them
     - example: Can you hand me that towel, please?
     - frames: hand sb sth; hand sth to sb
- relations: derived_from hand (NOUN)

### 66. apostrophe

**Lexeme 1: apostrophe**, NOUN
- grammar: countability Count
- said: /ə.ˈpɑː.stɹə.fi/ (en-US), /ə.ˈpɒ.stɹə.fi/ (en-GB)
  1. [B1] the mark (') that shows missing letters or that something belongs to someone
     - example: Don't forget the apostrophe in "the teacher's desk".
  2. [C2] words in a speech or poem spoken directly to an absent person or a thing
     - example: The poem ends with an apostrophe to the dead king.
     - frames: an apostrophe to sb/sth
     - labels: domain literature

### 67. mummy

**Lexeme 1: mummy**, NOUN
- grammar: countability Count
- said: /ˈmʌ.mi/ (en-US), /ˈmʌ.mi/ (en-GB)
  1. [B1] a dead body kept from decay with oils and wrapped in cloth, especially in ancient Egypt
     - example: The museum displays an Egyptian mummy inside a painted wooden coffin.
  2. [A2] a word for mother, used by children or when talking to them
     - example: Mummy, can I have another biscuit before bed?
     - labels: formality informal
     - regions: en-GB, en-AU, en-IN

### 68. link

**Lexeme 1: link**, NOUN
- grammar: countability Count
- said: /ˈlɪŋk/ (en-US), /ˈlɪŋk/ (en-GB)
  1. [B1] a connection between two things, people or situations
     - example: Scientists have found a clear link between smoking and cancer.
     - frames: a link between sth and sth; a link with sth
  2. [A2] a word or picture on a web page that takes you to another page
     - example: Click on the link below to download the form.
     - labels: domain computing
  3. [B2] a road, railway or service that connects two places
     - example: The new rail link cuts the journey to the airport in half.
  4. [B2] one of the rings that are joined together to make a chain
     - example: One broken link was enough to snap the whole chain.
**Lexeme 2: link**, VERB
- grammar: valency Tr
- said: /ˈlɪŋk/ (en-US), /ˈlɪŋk/ (en-GB)
  1. [B1] to show or say that two things are connected
     - example: Many studies have linked poor diet to heart disease.
     - frames: link sth to/with sth; be linked to sth
  2. [B1] to join two things, places or machines together
     - example: A long bridge links the island to the mainland.
     - frames: link sth to/with sth; link A and B
  3. [B2] to make a connection from one web page or file to another
     - example: You can link your blog to all your social media accounts.
     - frames: link to sth
     - labels: domain computing
     - grammar: valency Both

### 69. rather

**Lexeme 1: rather**, ADV
- grammar: comparable False
- said: /ˈɹæ.ðɚ/ (en-US), /ˈɹɑː.ðə/ (en-GB)
  1. [B1] to some degree; fairly or quite
     - example: It's rather cold today, so bring a warm coat.
  2. [B1] used to say which thing someone prefers or chooses instead of another
     - example: I'd rather walk than wait for the bus.
     - frames: would rather do sth; rather than sth; rather than do sth
  3. [B2] used to correct something you have just said or make it more exact
     - example: He lives in the city, or rather just outside it.
     - frames: or rather
  4. [C1] used to introduce an idea that is opposite to the one before
     - example: The problem isn't money; it is rather a lack of time.
     - frames: not sth but rather sth

### 70. telescopic

**Lexeme 1: telescopic**, ADJ
- grammar: comparable False
- said: /ˌtɛ.lə.ˈskɑː.pɪk/ (en-US), /ˌte.lɪ.ˈskɒ.pɪk/ (en-GB)
  1. [C1] made of parts that slide inside each other to become longer or shorter
     - example: I always carry a small telescopic umbrella in my bag.
     - grammar: position Attr
  2. [C1] making distant things look larger and closer, by using lenses
     - example: The hunter looked through the telescopic sight on his rifle.
     - frames: telescopic lens; telescopic sight
     - grammar: position Attr
- relations: derived_from telescope (NOUN)

### 71. gifted

**Lexeme 1: gifted**, ADJ
- grammar: comparable True
- said: /ˈɡɪf.tɪd/ (en-US), /ˈɡɪf.tɪd/ (en-GB)
  1. [B2] having a natural ability to do something very well
     - example: She is a gifted musician who also writes her own songs.
     - frames: gifted at/in sth
  2. [C1] of a child, much more intelligent than most others of the same age
     - example: Our school runs special classes for gifted children.
     - grammar: position Attr
- relations: derived_from gift (NOUN)

### 72. reed

**Lexeme 1: reed**, NOUN
- grammar: countability Count
- said: /ˈɹiːd/ (en-US), /ˈɹiːd/ (en-GB)
  1. [B2] a tall plant like grass with a hollow stem that grows near water
     - example: Ducks were hiding among the reeds at the edge of the lake.
  2. [C1] a thin strip in some wind instruments that shakes to make a sound
     - example: Clarinet players often have to replace a cracked reed.
     - labels: domain music
- relations: confusable read (VERB): sounds the same as read in the present tense

### 73. rasa

**Lexeme 1: rasa**, NOUN
- grammar: countability Count
- said: /ˈɹʌ.sə/ (en-US), /ˈɹɑː.sə/ (en-US), /ˈɹʌ.sə/ (en-GB), /ˈɹɑː.sə/ (en-GB)
  1. [C2] the emotional feeling or mood that a work of art creates in its audience
     - example: Each dance in the performance aims to awaken a different rasa.
     - labels: domain art, rare True
     - grammar: note a term of classical Indian aesthetics

### 74. enhanced

**Lexeme 1: enhanced**, ADJ
- grammar: position Attr
- said: /ɪn.ˈhænst/ (en-US), /ɪn.ˈhɑːnst/ (en-GB)
  1. [C1] made better, stronger or more effective than before
     - example: Newer models come with enhanced security and a longer battery life.
- relations: derived_from enhance (VERB)

### 75. gulf

**Lexeme 1: gulf**, NOUN
- grammar: countability Count
- said: /ˈɡʌlf/ (en-US), /ˈɡʌlf/ (en-GB)
  1. [B2] a large area of sea that is partly surrounded by land
     - example: Warm water from the gulf often brings storms to this coast.
  2. [C1] a big difference in opinions, beliefs or way of life between groups
     - example: There is a widening gulf between rich and poor in many cities.
     - frames: a gulf between A and B
     - labels: figurative True

### 76. paralegal

**Lexeme 1: paralegal**, NOUN
- grammar: countability Count
- said: /ˌpɛɹ.ə.ˈliː.ɡəl/ (en-US), /ˌpæɹ.ə.ˈliː.ɡəl/ (en-US), /ˌpæɹ.ə.ˈliː.ɡəl/ (en-GB)
  1. [C1] a person trained to help lawyers with their work who is not a qualified lawyer
     - example: She worked as a paralegal while studying for her law degree.
     - frames: work as a paralegal
     - regions: en-021 mainly
- relations: derived_from legal (ADJ)
**Lexeme 2: paralegal**, ADJ
- grammar: position Attr; comparable False
- said: /ˌpɛɹ.ə.ˈliː.ɡəl/ (en-US), /ˌpæɹ.ə.ˈliː.ɡəl/ (en-US), /ˌpæɹ.ə.ˈliː.ɡəl/ (en-GB)
  1. [C1] doing work that helps lawyers, without being a qualified lawyer
     - example: The firm is hiring more paralegal staff this year.
     - regions: en-021 mainly
- relations: derived_from legal (ADJ)

### 77. then

**Lexeme 1: then**, ADV
- grammar: comparable False
- said: /ˈðɛn/ (en-US), /ˈðen/ (en-GB)
  1. [A1] after that; next in order or in time
     - example: Fry the onions for a minute, then add the garlic.
  2. [A2] at that time in the past or in the future
     - example: We were living in a tiny flat by the sea back then.
     - frames: by then; since then; until then
  3. [A2] as a result of that; in that case
     - example: If you're feeling tired, then you should go to bed early.
     - frames: if ..., then ...
- relations: confusable than (SCONJ): sounds similar; than is used in comparisons, then is about time or order
**Lexeme 2: then**, ADJ
- grammar: position Attr; comparable False
- said: /ˈðɛn/ (en-US), /ˈðen/ (en-GB)
  1. [C1] holding a particular job or position at that time
     - example: The then president refused to sign the new law.
     - labels: formality formal

### 78. prehistory

**Lexeme 1: prehistory**, NOUN
- grammar: countability Both
- said: /ˌpɹiː.ˈhɪs.tə.ɹi/ (en-US), /ˌpɹiː.ˈhɪs.tɹi/ (en-US), /ˌpɹiː.ˈhɪs.tə.ɹi/ (en-GB), /ˌpɹiː.ˈhɪs.tɹi/ (en-GB)
  1. [C1] the long period of human life before people began to write things down
     - example: Stone tools tell us a great deal about human prehistory.
     - grammar: countability Mass
  2. [C2] the early events and developments that came before something and led to it
     - example: Her book traces the prehistory of the modern computer.
     - frames: the prehistory of sth
     - grammar: countability Count
- relations: derived_from history (NOUN)

### 79. printed

**Lexeme 1: printed**, ADJ
- grammar: comparable False
- said: /ˈpɹɪn.tɪd/ (en-US), /ˈpɹɪn.tɪd/ (en-GB)
  1. [B1] produced by a machine that puts words or pictures on paper
     - example: Please bring a printed copy of your ticket to the airport.
     - opposites: handwritten
  2. [B2] of cloth, decorated with a pattern pressed onto its surface
     - example: She wore a bright printed dress with small yellow flowers.
     - opposites: plain
- relations: derived_from print (VERB)

### 80. sworn

**Lexeme 1: sworn**, ADJ
- grammar: position Attr; comparable False
- said: /ˈswɔːɹn/ (en-US), /ˈswɔːn/ (en-GB)
  1. [C1] made by someone who has officially promised to tell the truth
     - example: The witness gave a sworn statement to the police.
     - frames: sworn statement; sworn testimony
  2. [C1] having a strong hatred for someone that will not change
     - example: Those two families have been sworn enemies for generations.
     - frames: sworn enemy
- relations: derived_from swear (VERB)

### 81. candidly

**Lexeme 1: candidly**, ADV
- grammar: comparable True
- said: /ˈkæn.dɪd.li/ (en-US), /ˈkæn.dɪd.li/ (en-GB)
  1. [C1] in an honest and open way, saying what you really think
     - example: She spoke candidly about her struggles during her first year abroad.
     - frames: speak/talk candidly about sth; candidly, ...
- relations: adverb_of candid (ADJ)

### 82. boudoir

**Lexeme 1: boudoir**, NOUN
- grammar: countability Count
- said: /ˈbuː.dwɑːɹ/ (en-US), /ˈbuː.dwɑː/ (en-GB)
  1. [C2] a woman's private bedroom or small room for dressing and resting
     - example: Her grandmother's boudoir still smelled of powder and old perfume.
     - labels: time dated

### 83. countrywide

**Lexeme 1: countrywide**, ADJ
- grammar: comparable False
- said: /ˌkʌn.tɹi.ˈwaɪd/ (en-US), /ˌkʌn.tɹi.ˈwaɪd/ (en-GB)
  1. [C1] happening or existing in all parts of a country
     - example: The government announced a countrywide ban on smoking in restaurants.
- relations: derived_from country (NOUN)
**Lexeme 2: countrywide**, ADV
- grammar: comparable False
- said: /ˌkʌn.tɹi.ˈwaɪd/ (en-US), /ˌkʌn.tɹi.ˈwaɪd/ (en-GB)
  1. [C1] in or across all parts of a country
     - example: The new rules will apply countrywide from next spring.
- relations: derived_from country (NOUN)

### 84. setting

**Lexeme 1: setting**, NOUN
- grammar: countability Count
- said: /ˈsɛ.tɪŋ/ (en-US), /ˈse.tɪŋ/ (en-GB)
  1. [B1] the place and conditions in which something is or happens
     - example: The old castle was a perfect setting for the wedding.
     - frames: a setting for sth; in a ... setting
  2. [B1] the place and time in which the events of a story happen
     - example: Have you noticed that the novel's setting changes in every chapter?
  3. [B1] a position of the controls on a machine or device
     - example: Turn the heater to its lowest setting before you go to bed.
     - frames: on a ... setting
  4. [C1] the metal part of a ring that holds a jewel in place
     - example: My grandmother's ring has a diamond in a gold setting.
- relations: derived_from set (VERB)

### 85. show

**Lexeme 1: show**, VERB
- grammar: irregular True; valency Both
- said: /ˈʃoʊ/ (en-US), /ˈʃəʊ/ (en-GB)
- forms: showed [Tense=Past, VerbForm=Fin] /ˈʃoʊd/ /ˈʃəʊd/; shown [Tense=Past, VerbForm=Part] /ˈʃoʊn/ /ˈʃəʊn/
  1. [A1] to let someone see something, or to hold it so they can see it
     - example: Can you show me the photos from your holiday?
     - frames: show sb sth; show sth to sb
     - grammar: valency Tr
     - opposites: hide
  2. [B1] to make it clear that something is true; to prove
     - example: The results show that the new drug really works.
     - frames: show that; show sth to be sth
     - grammar: valency Tr
  3. [A2] to teach someone how to do something by doing it yourself
     - example: Let me show you how to use this machine.
     - frames: show sb how to do sth
     - grammar: valency Tr
  4. [B1] to take someone to a place or lead them around it
     - example: A guide showed us around the old castle.
     - frames: show sb around sth; show sb to sth
     - grammar: valency Tr
  5. [B1] to let your feelings or qualities be seen by others
     - example: He never shows his anger, even when he's upset.
     - grammar: valency Tr
  6. [B2] to be easy to see or notice
     - example: Don't worry, the small stain on your shirt doesn't show.
     - grammar: valency Intr
  7. [B1] to put a film or programme on at a cinema or on television
     - example: They're showing an old western on TV tonight.
     - grammar: valency Both
**Lexeme 2: show**, NOUN
- grammar: countability Count
- said: /ˈʃoʊ/ (en-US), /ˈʃəʊ/ (en-GB)
  1. [A1] a programme on television or radio
     - example: My favourite cooking show starts in five minutes.
  2. [A2] a performance of music, dancing or comedy in a theatre
     - example: We saw a fantastic show in the city last night.
  3. [B1] a public event where people look at things on display
     - example: Every summer the village holds a flower show.
     - frames: on show
  4. [B2] an action meant to make people believe something that may not be true
     - example: Their friendly smiles were just for show.
     - frames: a show of sth; for show
     - grammar: countability Both

### 86. nostril

**Lexeme 1: nostril**, NOUN
- grammar: countability Count
- said: /ˈnɑː.stɹəl/ (en-US), /ˈnɒs.tɹəl/ (en-GB), /ˈnɒs.tɹɪl/ (en-GB)
  1. [B2] one of the two openings at the end of the nose
     - example: The horse's nostrils grew wide as it smelled the smoke.

### 87. philosopher

**Lexeme 1: philosopher**, NOUN
- grammar: countability Count
- said: /fə.ˈlɑː.sə.fɚ/ (en-US), /fə.ˈlɒs.ə.fə/ (en-GB)
  1. [B2] a person who studies or writes about the meaning of life, truth and knowledge
     - example: Ancient Greek thinkers such as Plato are still read by every philosopher today.
  2. [C1] a person who thinks deeply about life and calmly accepts its problems
     - example: You're quite a philosopher about losing your job, aren't you?
- relations: derived_from philosophy (NOUN)

### 88. rona

**Lexeme 1: rona**, NOUN
- grammar: countability Mass; number Sing; note usually with the; also written Rona
- said: /ˈɹoʊ.nə/ (en-US), /ˈɹəʊ.nə/ (en-GB)
  1. [C1] the disease COVID-19, or the virus that causes it
     - example: Half my office is off work with the rona this week.
     - frames: the rona; get/have the rona
     - labels: formality slang, attitude humorous
- relations: derived_from coronavirus (NOUN)

### 89. sleep

**Lexeme 1: sleep**, VERB
- grammar: irregular True; valency Both
- said: /ˈsliːp/ (en-US), /ˈsliːp/ (en-GB)
- forms: slept [Tense=Past, VerbForm=Fin,Part] /ˈslɛpt/ /ˈslept/
  1. [A1] to rest with your eyes closed and your mind and body not active
     - example: I usually sleep for about seven hours a night.
     - grammar: valency Intr
     - opposites: wake
  2. [C1] to have enough beds for a particular number of people
     - example: This little cottage sleeps up to six people.
     - grammar: valency Tr
**Lexeme 2: sleep**, NOUN
- grammar: countability Both
- said: /ˈsliːp/ (en-US), /ˈsliːp/ (en-GB)
  1. [A2] the natural state of rest when your eyes are closed and your mind is not conscious
     - example: Most teenagers don't get enough sleep during the week.
     - frames: go to sleep; get some sleep
     - grammar: countability Mass
  2. [B1] a period of time spent resting with your eyes closed
     - example: You look tired; why don't you have a short sleep?
     - frames: have a sleep
     - grammar: countability Count

### 90. pewter

**Lexeme 1: pewter**, NOUN
- grammar: countability Mass
- said: /ˈpjuː.tɚ/ (en-US), /ˈpjuː.tə/ (en-GB)
  1. [C1] a dull grey metal made by mixing tin with other metals
     - example: My grandmother kept a row of pewter mugs above the fireplace.
  2. [C2] plates, cups and other objects made of this grey metal
     - example: Old pewter was polished and displayed on the kitchen shelves.

### 91. shooting

**Lexeme 1: shooting**, NOUN
- grammar: countability Both
- said: /ˈʃuː.tɪŋ/ (en-US), /ˈʃuː.tɪŋ/ (en-GB)
  1. [B2] an attack in which someone is hurt or killed with a gun
     - example: Police are still investigating last night's shooting outside the bar.
     - grammar: countability Count
  2. [B2] the sport or activity of firing guns at animals, birds or targets
     - example: My uncle goes shooting in the hills every autumn.
     - grammar: countability Mass
  3. [C1] the work of recording a film or taking photographs with a camera
     - example: Shooting begins next month in a small village by the sea.
     - grammar: countability Mass
- relations: verbal_noun_of shoot (VERB)

### 92. twitter

**Lexeme 1: twitter**, VERB
- grammar: valency Intr
- said: /ˈtwɪ.tɚ/ (en-US), /ˈtwɪ.tə/ (en-GB)
  1. [C1] of a bird, to make a series of short, high sounds
     - example: Small birds were twittering in the hedge outside my window.
  2. [C2] to talk quickly in a high voice about unimportant things
     - example: She twittered on about her holiday plans for an hour.
     - frames: twitter on about sth
     - labels: attitude disapproving
**Lexeme 2: twitter**, NOUN
- grammar: countability Count
- said: /ˈtwɪ.tɚ/ (en-US), /ˈtwɪ.tə/ (en-GB)
  1. [C1] a series of short, high sounds made by birds
     - example: We woke to the twitter of sparrows in the garden.
**Lexeme 3: Twitter**, PROPN
- said: /ˈtwɪ.tɚ/ (en-US), /ˈtwɪ.tə/ (en-GB)
  1. [B1] an online service for posting short public messages, renamed X in 2023
     - example: He shared the news on Twitter before telling his family.

### 93. demonstrably

**Lexeme 1: demonstrably**, ADV
- said: /dɪ.ˈmɑːn.stɹə.bli/ (en-US), /ˈdɛm.ən.stɹə.bli/ (en-US), /dɪ.ˈmɒn.stɹə.bli/ (en-GB), /ˈdem.ən.stɹə.bli/ (en-GB)
  1. [C1] in a way that can be clearly shown or proved to be true
     - example: The new treatment is demonstrably safer than the old one.
     - frames: demonstrably + adj; demonstrably false/untrue
     - labels: formality formal
- relations: adverb_of demonstrable (ADJ)

### 94. egotistical

**Lexeme 1: egotistical**, ADJ
- grammar: comparable True
- said: /ˌiː.ɡə.ˈtɪs.tɪ.kəl/ (en-US), /ˌɛ.ɡə.ˈtɪs.tɪ.kəl/ (en-US), /ˌiː.ɡə.ˈtɪs.tɪ.kəl/ (en-GB), /ˌe.ɡə.ˈtɪs.tɪ.kəl/ (en-GB)
  1. [C1] thinking you are more important than other people and talking about yourself too much
     - example: Her egotistical boss never lets anyone else take credit for anything.
     - labels: attitude disapproving
     - opposites: modest
- relations: derived_from egotist (NOUN); confusable egoistic (ADJ): egoistic means caring only about your own interests, not about seeming important

### 95. attention

**Lexeme 1: attention**, NOUN
- grammar: countability Mass
- said: /ə.ˈtɛn.ʃən/ (en-US), /ə.ˈten.ʃən/ (en-GB)
  1. [A2] careful watching, listening or thinking about something or someone
     - example: Please pay attention to what your teacher is saying.
     - frames: pay attention to sth/sb
  2. [B1] interest or notice that people give to someone or something
     - example: The strange noise outside attracted the attention of the neighbours.
     - frames: attract/draw attention to sth; the centre of attention
  3. [B2] practical care, help or treatment given to someone or something
     - example: After the accident, two passengers needed urgent medical attention.
  4. [C1] a position in which soldiers stand very straight with their feet together
     - example: The guards stood to attention as the general walked past.
     - frames: stand to attention; stand at attention
     - labels: domain military
- relations: derived_from attend (VERB)
**Lexeme 2: attention**, INTJ
- said: /ə.ˈtɛn.ʃən/ (en-US), /ə.ˈten.ʃən/ (en-GB)
  1. [A2] said to ask people to listen to an announcement
     - example: Attention, please: the train to the airport is delayed.

### 96. fishman

**Lexeme 1: fishman**, NOUN
- grammar: countability Count
- said: /ˈfɪʃ.mən/ (en-US), /ˈfɪʃ.mən/ (en-GB)
- parts: fish (fish) + man (man)
- forms: fishmen [Number=Plur] /ˈfɪʃ.mən/ /ˈfɪʃ.mən/
  1. [C2] a man who catches fish as his job
     - example: The old fishman mended his nets beside the harbour wall.
     - labels: time archaic, rare True
  2. [C2] an imaginary creature that is partly a human and partly a fish
     - example: In the story, fishmen rise from the sea at night.
     - labels: domain mythology

### 97. burnt

**Lexeme 1: burnt**, ADJ
- grammar: comparable False; note also the past tense and past participle of burn, mainly en-GB
- said: /ˈbɝnt/ (en-US), /ˈbɜːnt/ (en-GB)
  1. [B1] damaged, blackened or hurt by fire or great heat
     - example: Nobody wanted to eat the burnt toast this morning.
- relations: derived_from burn (VERB)

### 98. claret

**Lexeme 1: claret**, NOUN
- grammar: countability Both
- said: /ˈklɛɹ.ət/ (en-US), /ˈklæɹ.ət/ (en-US), /ˈklæɹ.ət/ (en-GB)
  1. [C1] a red wine from the Bordeaux area of France
     - example: My grandfather always drank a glass of claret with dinner.
     - regions: en-GB mainly
  2. [C2] a dark purplish-red colour
     - example: The football club's shirts are claret and blue.
     - grammar: countability Mass
**Lexeme 2: claret**, ADJ
- grammar: comparable False
- said: /ˈklɛɹ.ət/ (en-US), /ˈklæɹ.ət/ (en-US), /ˈklæɹ.ət/ (en-GB)
  1. [C2] dark purplish red in colour
     - example: She chose heavy claret curtains for the dining room.

### 99. winding

**Lexeme 1: winding**, ADJ
- grammar: comparable True
- said: /ˈwaɪn.dɪŋ/ (en-US), /ˈwaɪn.dɪŋ/ (en-GB)
  1. [B2] having many curves and turns, not going in a straight line
     - example: We drove slowly up the narrow, winding road to the village.
     - opposites: straight
- relations: derived_from wind (VERB); confusable wind (NOUN): winding here rhymes with finding, not with the vowel of wind, moving air
**Lexeme 2: winding**, NOUN
- grammar: countability Count
- said: /ˈwaɪn.dɪŋ/ (en-US), /ˈwaɪn.dɪŋ/ (en-GB)
  1. [C1] a bend or turn in a road, path or river, usually plural
     - example: From the hill you can follow every winding of the river.
- relations: derived_from wind (VERB)

### 100. rapporteur

**Lexeme 1: rapporteur**, NOUN
- grammar: countability Count
- said: /ˌɹæ.pɔːɹ.ˈtɝ/ (en-US), /ˌɹæ.pɔː.ˈtɜː/ (en-GB)
  1. [C2] a person chosen by an official body to investigate a subject and report on it
     - example: The special rapporteur will present her findings to the committee next month.
     - frames: special rapporteur on sth; rapporteur for sth
     - labels: formality formal, domain politics
