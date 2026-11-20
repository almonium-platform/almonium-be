# English: entries to check (1 of 1)

These are entries of a learner's dictionary of English, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of English.

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
- a grammar value (countability, valency, position, number) says how the word is ordinarily used, as
  a learner's dictionary of English prints it: "Mass" is "uncountable" in that sense, although kinds
  of the thing can be counted. Followed by "mainly", the other use is common too. Report a value
  only when a learner's dictionary would not print it for this word or sense.
- position Attr means the adjective is used joined to a noun and not after a verb such as "be",
  wherever English puts it (before the noun in English, mostly after it in French); Pred means only
  after such a verb. Neither says on which side of the noun the adjective stands.
- a lexeme is one word with one way of saying and inflecting it. Two homonyms that are said and
  inflected alike are senses of one lexeme here, although dictionaries of English number them apart.
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

### 1. square

**Lexeme 1: square**, NOUN
- grammar: countability Count
- said: /ˈskwɛɹ/ (en-US), /ˈskweə/ (en-GB)
  1. [A2] a flat shape with four equal straight sides and four right angles
     - example: Cut the paper into squares of the same size.
     - collocations: draw a square
  2. [A2] an open public area in a town, usually with buildings around it
     - example: There is a big market in the main square every Saturday.
     - collocations: town square; main square; market square; in the square; village square
  3. [B1] a flat piece of something with four equal sides
     - example: Would you like a square of chocolate with your coffee?
     - frames: a square of sth
  4. [B1] one of the equal spaces on a board for a game or in a grid
     - example: Move your piece forward three squares.
  5. [B2] the number you get when you multiply a number by itself
     - example: The square of five is twenty-five.
     - frames: the square of sth
     - labels: domain mathematics
  6. [C1] a person who is old-fashioned, boring and follows the rules
     - example: My parents think anyone who doesn't dance is a square.
     - labels: formality informal, time dated
**Lexeme 2: square**, ADJ
- grammar: comparable True
- said: /ˈskwɛɹ/ (en-US), /ˈskweə/ (en-GB)
  1. [A2] having four equal straight sides and four right angles
     - example: We bought a small square table for the kitchen.
     - grammar: comparable False, mainly
  2. [B1] used after a number and before a unit of length to name a unit of area
     - example: Their new flat is only fifty square metres.
     - collocations: square metre; square mile; square foot; square kilometre
     - grammar: position Attr; comparable False
  3. [B2] broad and solid, with straight lines and corners rather than curves
     - example: He had a strong, square jaw and dark eyes.
     - collocations: square jaw; square shoulders; square chin
  4. [B2] having the same score, or owing nothing to each other
     - example: You paid for lunch, so now we're square.
     - collocations: all square
     - labels: formality informal
     - grammar: position Pred
  5. [C1] honest and fair in dealing with other people
     - example: I want you to be square with me about the money.
     - frames: square with sb
     - collocations: a square deal; fair and square
  6. [C1] old-fashioned and boring, not following new fashions
     - example: Her friends thought the party was really square.
     - labels: formality informal, time dated
**Lexeme 3: square**, ADV
- said: /ˈskwɛɹ/ (en-US), /ˈskweə/ (en-GB)
  1. [C1] directly and firmly, not at an angle or to one side
     - example: The ball hit him square on the nose.
     - collocations: square in the face; square in the eye; square on the jaw
**Lexeme 4: square**, VERB
- grammar: irregular False
- said: /ˈskwɛɹ/ (en-US), /ˈskweə/ (en-GB)
  1. [C1] to agree with something or make two things agree
     - example: His story doesn't square with what the witnesses said.
     - frames: square with sth; square sth with sth
     - grammar: valency Both
  2. [B2] to multiply a number by itself
     - example: If you square three, you get nine.
     - frames: square sth
     - labels: domain mathematics
     - grammar: valency Tr
  3. [C1] to straighten your shoulders or body, ready to face something
     - example: She squared her shoulders and walked into the meeting.
     - collocations: square your shoulders
     - grammar: valency Tr
  4. [C1] to get someone's agreement or settle something with them
     - example: I'll need to square it with my boss first.
     - frames: square sth with sb
     - labels: formality informal
     - grammar: valency Tr
  5. [C1] to make the score in a game or series level
     - example: A late goal squared the match at two each.
     - collocations: square the match; square the series
     - labels: domain sport
     - regions: en-GB mainly
     - grammar: valency Tr

### 2. reaction

**Lexeme 1: reaction**, NOUN
- grammar: countability Count
- said: /ɹi.ˈæk.ʃən/ (en-US), /ɹi.ˈæk.ʃən/ (en-GB)
  1. [B1] what you feel, say or do because of something that has happened
     - example: My first reaction was to laugh at the whole idea.
     - frames: reaction to sth; reaction from sb
     - collocations: first reaction; initial reaction; gut reaction; mixed reaction; immediate reaction; provoke a reaction; get a reaction; public reaction
  2. [B2] a bad effect on the body caused by something you eat, touch or take
     - example: Some patients have a serious reaction to this medicine.
     - frames: reaction to sth
     - collocations: allergic reaction; have a reaction; severe reaction; skin reaction
  3. [B2] the ability to move quickly when something suddenly happens
     - example: Good drivers need quick reactions on busy roads.
     - collocations: quick reactions; fast reactions; slow reactions
     - grammar: number Plur
  4. [B2] a process in which substances act on each other and form new substances
     - example: Heat speeds up the reaction between the two liquids.
     - frames: reaction between sth and sth
     - collocations: chemical reaction; chain reaction; nuclear reaction; cause a reaction
     - labels: domain chemistry
  5. [C1] a change in ideas or behaviour that opposes what was usual before
     - example: Her simple style was a reaction against the fashions of the time.
     - frames: reaction against sth
- relations: derived_from react (VERB)

### 3. mad

**Lexeme 1: mad**, ADJ
- grammar: comparable True
- said: /ˈmæd/ (en-US), /ˈmæd/ (en-GB)
  1. [A2] very angry
     - example: My dad gets mad at me when I come home late.
     - frames: mad at sb; mad about sth; mad with sb
     - collocations: get mad; really mad; hopping mad; mad as hell
     - labels: formality informal
     - regions: en-US mainly
     - grammar: position Pred, mainly; note mad with sb is mainly British
  2. [B1] having a mind that does not work normally; mentally ill
     - example: In the story, the old king slowly goes mad.
     - frames: drive sb mad
     - collocations: go mad; stark raving mad; mad scientist
     - labels: time dated, attitude offensive
     - regions: en-GB mainly
     - opposites: sane
  3. [B1] very foolish and not sensible, often in a dangerous way
     - example: You must be mad to go swimming in this weather.
     - frames: mad to do sth
     - collocations: a mad idea; completely mad; absolutely mad
     - labels: formality informal
     - regions: en-GB mainly
  4. [B1] liking someone or something very much
     - example: Both of my sons are mad about football.
     - frames: mad about sb/sth; mad on sth
     - collocations: mad keen
     - labels: formality informal
     - regions: en-GB mainly
     - grammar: position Pred
  5. [B2] very fast, busy or wild, often because of excitement or fear
     - example: There was a mad rush for the doors when the fire started.
     - collocations: a mad rush; a mad dash; a mad scramble; like mad
     - grammar: position Attr, mainly
  6. [C1] suffering from a disease that makes an animal violent and dangerous
     - example: The farmer had to shoot a mad dog that attacked his sheep.
     - collocations: mad dog; mad cow disease
     - grammar: position Attr; note used of animals

### 4. sad

**Lexeme 1: sad**, ADJ
- grammar: comparable True
- said: /ˈsæd/ (en-US), /ˈsæd/ (en-GB)
  1. [A1] unhappy, often because something bad has happened
     - example: She felt sad when her best friend moved away.
     - frames: sad about sth; sad to do sth; sad that ...
     - collocations: feel sad; look sad; deeply sad; a sad face; sad eyes
     - opposites: happy
  2. [A2] making people feel unhappy
     - example: It's sad that so many shops on our street have closed.
     - frames: it is sad that ...; it is sad to do sth
     - collocations: sad news; a sad story; a sad day; a sad song; sad to say; sad but true
  3. [C1] bad or unacceptable in a way that deserves criticism
     - example: Empty classrooms and broken windows are a sad sight.
     - collocations: a sad state of affairs; a sad reflection
     - grammar: position Attr
  4. [B2] boring, unfashionable and a little embarrassing
     - example: My brother thinks it's sad to stay home on Friday nights.
     - labels: formality informal, attitude disapproving
     - regions: en-GB mainly

### 5. file

**Lexeme 1: file**, NOUN
- grammar: countability Count
- said: /ˈfaɪl/ (en-US), /ˈfaɪl/ (en-GB)
  1. [A2] a set of data stored on a computer under its own name
     - example: Remember to save the file before you close the program.
     - collocations: save a file; open a file; delete a file; attach a file; download a file; file size
  2. [B1] a folder or box for keeping papers, or the papers kept in it about one subject
     - example: The doctor keeps a file on every patient she treats.
     - frames: file on sb/sth
     - collocations: on file; keep a file; personal file; case file; confidential file
  3. [B2] a metal tool with a rough surface, used for making things smooth or shaping them
     - example: He used a small file to smooth the edge of the key.
     - collocations: nail file
  4. [C1] a line of people or animals standing or moving one behind another
     - example: The children walked in single file along the narrow path.
     - collocations: in single file; walk single file
     - grammar: note usually in the phrase single file
**Lexeme 2: file**, VERB
- grammar: valency Both; irregular False
- said: /ˈfaɪl/ (en-US), /ˈfaɪl/ (en-GB)
  1. [B2] to officially give a document, claim or request to a court or authority
     - example: She decided to file a complaint against her landlord.
     - frames: file sth; file for sth; file sth against sb
     - collocations: file a complaint; file a lawsuit; file for divorce; file for bankruptcy; file charges; file a claim; file a tax return
     - grammar: valency Tr, mainly
  2. [B1] to put papers or documents in their proper place so they are kept in order
     - example: Please file these invoices under the client's name.
     - frames: file sth; file sth under sth
     - grammar: valency Tr
  3. [C1] to walk in a line, one person behind another
     - example: The audience slowly filed out of the theatre after the show.
     - frames: file into sth; file out of sth; file past sb/sth
     - grammar: valency Intr
  4. [B2] to make something smooth or change its shape by rubbing it with a rough metal tool
     - example: I need to file my nails before the party tonight.
     - frames: file sth
     - collocations: file your nails
     - grammar: valency Tr
  5. [C1] to send a report or story to a newspaper or news organization
     - example: Our correspondent filed this report from the capital last night.
     - frames: file sth
     - collocations: file a report; file a story
     - grammar: valency Tr

### 6. northern

**Lexeme 1: northern**, ADJ
- grammar: position Attr; comparable False
- said: /ˈnɔːɹ.ðɚn/ (en-US), /ˈnɔː.ðən/ (en-GB)
  1. [A2] in, towards or coming from the north of a place
     - example: Snow is expected across northern parts of the country tonight.
     - collocations: northern hemisphere; northern border; northern edge; northern half; northern lights; northern wind
     - opposites: southern
  2. [B2] typical of the people and way of life in the north of a country
     - example: He still speaks with a strong northern accent after years abroad.
     - collocations: northern accent; northern humour
     - grammar: note often written with a capital N
     - opposites: southern
- relations: adjective_of north (NOUN)

### 7. vehicle

**Lexeme 1: vehicle**, NOUN
- grammar: countability Count
- said: /ˈviː.ə.kəl/ (en-US), /ˈviː.ɪ.kəl/ (en-US), /ˈviː.ə.kəl/ (en-GB), /ˈvɪə.kəl/ (en-GB)
  1. [B1] a machine with wheels or an engine for carrying people or goods
     - example: Please do not leave your vehicle parked in front of the gate.
     - collocations: motor vehicle; electric vehicle; commercial vehicle; emergency vehicle; stolen vehicle; drive a vehicle; park a vehicle
  2. [C1] something used as a way of expressing ideas or reaching a goal
     - example: For many young artists, social media is a vehicle for their work.
     - frames: vehicle for sth; vehicle for doing sth
     - collocations: a vehicle for change; investment vehicle
     - labels: formality formal

### 8. smart

**Lexeme 1: smart**, ADJ
- grammar: comparable True
- said: /ˈsmɑːɹt/ (en-US), /ˈsmɑːt/ (en-GB)
- forms: smarter [Degree=Cmp] /ˈsmɑːɹ.tɚ/ /ˈsmɑː.tə/; smartest [Degree=Sup] /ˈsmɑːɹ.tɪst/ /ˈsmɑː.tɪst/
  1. [A2] able to think quickly and understand things well
     - example: My little sister is smart enough to solve the puzzle alone.
     - frames: smart to do sth; smart of sb to do sth; smart enough to do sth
     - collocations: a smart move; a smart choice; a smart decision; a smart kid; street smart
     - regions: en-021 mainly
     - opposites: stupid
  2. [A2] clean, tidy and dressed in good clothes
     - example: You look very smart in that new jacket.
     - collocations: look smart; smart casual; a smart suit; smart clothes; smart appearance
     - regions: en-GB mainly
     - opposites: scruffy
  3. [B1] fashionable and expensive, used by rich people
     - example: They had dinner at a smart restaurant near the river.
     - collocations: a smart restaurant; a smart hotel; a smart area; a smart address
     - regions: en-GB mainly
  4. [B1] controlled by a computer and able to connect to other devices or the internet
     - example: Our new smart TV lets us watch films from the internet.
     - collocations: smart card; smart home; smart meter; smart TV; smart speaker; smart technology
     - grammar: position Attr
  5. [C1] quick and done with force or energy
     - example: The soldiers set off at a smart pace up the hill.
     - collocations: at a smart pace; a smart blow
     - grammar: position Attr
  6. [B2] clever in a rude way that shows a lack of respect
     - example: Don't get smart with me, or you'll stay after class.
     - frames: get smart with sb
     - collocations: a smart remark; a smart answer; smart mouth
     - labels: formality informal, attitude disapproving
**Lexeme 2: smart**, VERB
- grammar: valency Intr; irregular False
- said: /ˈsmɑːɹt/ (en-US), /ˈsmɑːt/ (en-GB)
  1. [C1] to feel a sharp, burning pain
     - example: The thick smoke made everyone's eyes smart.
     - collocations: eyes smart
  2. [C1] to feel upset and offended because of something that happened
     - example: The team is still smarting from last week's heavy defeat.
     - frames: smart from sth; smart over sth; smart at sth
     - collocations: still smarting
     - grammar: note usually in the progressive form

### 9. influence

**Lexeme 1: influence**, NOUN
- grammar: countability Both
- said: /ˈɪn.flu.əns/ (en-US), /ɪn.ˈflu.əns/ (en-US, disputed), /ˈɪn.flu.əns/ (en-GB)
  1. [B1] the power to change what someone does or thinks, or how something develops
     - example: Parents have a strong influence on their children's eating habits.
     - frames: influence on sb/sth; influence over sb/sth
     - collocations: have an influence on; under the influence of; exert influence; use your influence; political influence; strong influence; considerable influence; sphere of influence
     - grammar: countability Mass, mainly
  2. [B2] a person or thing that changes the way someone behaves, thinks or develops
     - example: My grandmother was the biggest influence on my life.
     - frames: influence on sb/sth
     - collocations: a bad influence; a good influence; a major influence; an early influence
     - grammar: countability Count
**Lexeme 2: influence**, VERB
- grammar: valency Tr; irregular False
- said: /ˈɪn.flu.əns/ (en-US), /ɪn.ˈflu.əns/ (en-US, disputed), /ˈɪn.flu.əns/ (en-GB)
  1. [B1] to have an effect on what someone does or thinks, or how something develops
     - example: The weather can influence how people feel during the day.
     - frames: influence sb/sth; influence sb to do sth; be influenced by sb/sth
     - collocations: strongly influence; heavily influenced; greatly influence; influence a decision; influence the outcome; influence public opinion
- relations: derived_from influence (NOUN)

### 10. trial

**Lexeme 1: trial**, NOUN
- said: /ˈtɹaɪ.əl/ (en-US), /ˈtɹaɪl/ (en-US, casual), /ˈtɹaɪ.əl/ (en-GB)
  1. [B1] a formal process in a court where evidence is heard and a case is decided
     - example: He went on trial for fraud last spring.
     - frames: trial for sth; bring sb to trial
     - collocations: on trial; stand trial; face trial; go to trial; a fair trial; await trial; trial by jury; a murder trial
     - grammar: countability Both
  2. [B2] a test of how good, safe or useful someone or something is
     - example: The new drug is still undergoing clinical trials.
     - frames: trial of sth
     - collocations: trial and error; clinical trial; on trial; free trial; trial period; carry out a trial; run a trial; on a trial basis
     - grammar: countability Both, mainly
  3. [C1] a competition in which players try to win a place on a team
     - example: She ran well at the national trials and made the team.
     - collocations: Olympic trials
     - grammar: countability Count; number Plur, mainly
  4. [C1] a person or thing that causes you a lot of trouble or worry
     - example: Getting the children ready each morning is a real trial.
     - frames: a trial to sb
     - collocations: trials and tribulations
     - grammar: countability Count
- relations: confusable trail (NOUN): trail, a path or a line of marks, has almost the same letters
**Lexeme 2: trial**, VERB
- grammar: valency Tr; irregular False
- said: /ˈtɹaɪ.əl/ (en-US), /ˈtɹaɪ.əl/ (en-GB)
- forms: trialled [Tense=Past, VerbForm=Fin,Part] /ˈtɹaɪ.əld/ /ˈtɹaɪ.əld/ (en-GB, en-AU, en-IN); trialed [Tense=Past, VerbForm=Fin,Part] /ˈtɹaɪ.əld/ /ˈtɹaɪ.əld/ (en-US); trialling [Tense=Pres, VerbForm=Part] /ˈtɹaɪ.ə.lɪŋ/ /ˈtɹaɪ.ə.lɪŋ/ (en-GB, en-AU, en-IN); trialing [Tense=Pres, VerbForm=Part] /ˈtɹaɪ.ə.lɪŋ/ /ˈtɹaɪ.ə.lɪŋ/ (en-US)
  1. [C1] to test a new product, method or system to see how well it works
     - example: Several schools are trialling the new reading programme this year.
     - frames: trial sth
     - collocations: trial a scheme; trial a new system
     - regions: en-GB mainly
- relations: derived_from trial (NOUN)

### 11. slightly

**Lexeme 1: slightly**, ADV
- said: /ˈslaɪt.li/ (en-US), /ˈslaɪt.li/ (en-GB)
  1. [A2] a little; to a small degree
     - example: I'm feeling slightly better than I did yesterday.
     - collocations: slightly different; slightly better; slightly higher; slightly more; only slightly; ever so slightly; change slightly; slightly worried
  2. [C1] in a way that is thin and small, with little weight
     - example: She was slightly built but surprisingly strong.
     - collocations: slightly built
     - grammar: note almost always used with built
- relations: adverb_of slight (ADJ)

### 12. foot

**Lexeme 1: foot**, NOUN
- grammar: countability Count
- said: /ˈfʊt/ (en-US), /ˈfʊt/ (en-GB)
- forms: feet [Number=Plur] /ˈfiːt/ /ˈfiːt/; foot [Number=Plur] /ˈfʊt/ /ˈfʊt/ (informal, in measurements before a number or adjective: six foot tall)
  1. [A1] the part at the end of the leg of a person or animal, used for standing and walking
     - example: My feet hurt after walking around the city all day.
     - collocations: on foot; on your feet; get to your feet; set foot in; bare feet; sore feet; stamp your feet; left foot
  2. [A2] a unit for measuring length, equal to twelve inches or about 30 centimetres
     - example: The wall around the garden is about six feet high.
     - collocations: six feet tall; square foot; cubic foot
  3. [B1] the lowest part or bottom of something
     - example: Leave your shoes at the foot of the stairs, please.
     - frames: the foot of sth
     - collocations: the foot of the stairs; the foot of the hill; the foot of the mountain; the foot of the page
     - grammar: number Sing
     - opposites: top
  4. [B2] the end of a bed opposite the end where you rest your head
     - example: The cat always sleeps at the foot of my bed.
     - collocations: the foot of the bed
     - grammar: number Sing
     - opposites: head
**Lexeme 2: foot**, VERB
- grammar: valency Tr; irregular False
- said: /ˈfʊt/ (en-US), /ˈfʊt/ (en-GB)
  1. [C1] to pay for something, often something costly or that others should pay
     - example: Taxpayers will have to foot the bill for the new stadium.
     - frames: foot sth
     - collocations: foot the bill
     - labels: formality informal
- relations: derived_from foot (NOUN)

### 13. traditional

**Lexeme 1: traditional**, ADJ
- grammar: comparable True
- said: /tɹə.ˈdɪʃ.ə.nəl/ (en-US), /tɹə.ˈdɪʃ.nəl/ (en-US), /tɹə.ˈdɪʃ.ə.nəl/ (en-GB), /tɹə.ˈdɪʃ.nəl/ (en-GB)
  1. [A2] being part of the customs and beliefs passed down in a group over many years
     - example: On holidays the whole village wears traditional dress.
     - collocations: traditional dress; traditional costume; traditional music; traditional food; traditional medicine; traditional way
     - grammar: position Attr, mainly; comparable False
  2. [B1] following older ideas and methods rather than new or different ones
     - example: My parents have fairly traditional views about marriage.
     - collocations: traditional values; traditional methods; traditional approach; traditional views; fairly traditional; very traditional
- relations: adjective_of tradition (NOUN)

### 14. estate

**Lexeme 1: estate**, NOUN
- grammar: countability Count
- said: /ɪ.ˈsteɪt/ (en-US), /ɪ.ˈsteɪt/ (en-GB)
  1. [B2] a large area of land owned by one person, family or company, often with a large house
     - example: His family has owned the estate for over three hundred years.
     - collocations: country estate; family estate; tea estate; wine estate; on the estate
  2. [B2] an area where many houses or factories have been built together as one plan
     - example: They grew up on a big estate on the edge of town.
     - collocations: housing estate; council estate; industrial estate; on an estate; live on an estate
     - regions: en-GB mainly
  3. [C1] all the money and property that someone owns, especially when they die
     - example: Her whole estate was left to her two grandchildren.
     - frames: sb's estate
     - collocations: estate tax; settle an estate; executor of the estate; leave an estate
  4. [B2] a car with a long body and a door at the back for luggage
     - example: We need an estate now that we have three kids and a dog.
     - regions: en-GB mainly
     - grammar: note short for estate car
  5. [C2] one of the social classes that shared political power in some European countries
     - example: In old France, the clergy formed the first estate of the realm.
     - collocations: the three estates; the Fourth Estate
     - labels: time historical

### 15. sport

**Lexeme 1: sport**, NOUN
- said: /ˈspɔːɹt/ (en-US), /ˈspɔːt/ (en-GB)
  1. [A1] a physical activity or game in which people compete following fixed rules
     - example: Football is the most popular sport in many countries.
     - collocations: play a sport; do sport; team sport; contact sport; spectator sport; winter sports; water sports; extreme sport
     - grammar: countability Both; note uncountable sport for the activity in general is mainly British; American English says sports
  2. [B2] a person who stays cheerful when they lose, are teased or are asked for help
     - example: Thanks for helping us move, you're a real sport.
     - collocations: a good sport; be a sport
     - labels: formality informal
     - grammar: countability Count
     - opposites: spoilsport
  3. [C1] pleasure or fun that people get from an activity, especially from hunting animals
     - example: Some people still shoot birds just for sport.
     - collocations: for sport; hunt for sport
     - grammar: countability Mass
  4. [C1] a friendly way of speaking to a man or a boy
     - example: How are you going today, sport?
     - labels: formality informal
     - regions: en-AU mainly
     - grammar: countability Count
**Lexeme 2: sport**, VERB
- grammar: valency Tr; irregular False
- said: /ˈspɔːɹt/ (en-US), /ˈspɔːt/ (en-GB)
  1. [C1] to wear or have something in a way that people notice
     - example: My uncle was sporting a bright red tie at the wedding.
     - frames: sport sth
     - collocations: sport a beard; sport a moustache; sport a tattoo

### 16. dear

**Lexeme 1: dear**, ADJ
- grammar: comparable True
- said: /ˈdɪɹ/ (en-US), /ˈdɪə/ (en-GB)
  1. [A1] used before a name or title at the start of a letter or email
     - example: She began her email with 'Dear Professor' and a short apology.
     - collocations: Dear Sir or Madam; Dear Sir
     - grammar: position Attr
  2. [B1] loved very much, or very important to someone
     - example: He has been a dear friend of mine for thirty years.
     - frames: dear to sb; hold sth dear
     - collocations: dear friend; near and dear; dear to my heart
  3. [B2] costing a lot of money
     - example: Fresh fruit is very dear at this time of year.
     - regions: en-GB mainly
     - grammar: position Pred, mainly
     - opposites: cheap
- relations: confusable deer (NOUN): sounds the same as deer, the wild animal
**Lexeme 2: dear**, NOUN
- grammar: countability Count
- said: /ˈdɪɹ/ (en-US), /ˈdɪə/ (en-GB)
  1. [A2] used to speak to someone you love, or in a friendly way to anyone
     - example: Come and sit down, dear, dinner is almost ready.
     - collocations: my dear
     - labels: politeness familiar
  2. [B2] a kind and lovable person
     - example: Be a dear and pass me my glasses.
     - collocations: be a dear; such a dear
     - labels: formality informal
**Lexeme 3: dear**, ADV
- said: /ˈdɪɹ/ (en-US), /ˈdɪə/ (en-GB)
  1. [C1] at a high cost, with serious loss or suffering
     - example: That one careless mistake cost the team dear in the final.
     - frames: cost sb dear
     - collocations: cost dear; pay dear
**Lexeme 4: dear**, INTJ
- said: /ˈdɪɹ/ (en-US), /ˈdɪə/ (en-GB)
  1. [A2] used to show surprise, worry, sadness or mild annoyance
     - example: Oh dear, I think I've left my keys at home.
     - collocations: oh dear; dear me; dear oh dear

### 17. healthy

**Lexeme 1: healthy**, ADJ
- grammar: comparable True
- said: /ˈhɛl.θi/ (en-US), /ˈhel.θi/ (en-GB)
  1. [A1] physically strong and well, and not ill
     - example: Regular exercise will help you stay fit and healthy.
     - collocations: stay healthy; keep healthy; healthy teeth; healthy skin; perfectly healthy; fit and healthy; a healthy baby
     - opposites: unhealthy
  2. [A2] good for your body and helping you stay well
     - example: I'm trying to eat a healthier diet with more vegetables.
     - collocations: healthy food; a healthy diet; a healthy lifestyle; healthy eating; a healthy meal; healthy habits
     - opposites: unhealthy
  3. [B2] working well and successful, without problems
     - example: A healthy economy creates plenty of new jobs.
     - collocations: a healthy economy; a healthy relationship; financially healthy; a healthy democracy
     - labels: figurative True
     - opposites: unhealthy
  4. [C1] normal and sensible, showing good judgement
     - example: Sailors have a healthy respect for the power of the sea.
     - collocations: a healthy respect; healthy scepticism; a healthy attitude; healthy competition
     - grammar: position Attr, mainly
     - opposites: unhealthy
  5. [B2] fairly large in amount or size
     - example: The company made a healthy profit in its first year.
     - collocations: a healthy profit; a healthy appetite; a healthy margin; a healthy lead
     - grammar: position Attr
- relations: derived_from health (NOUN)

### 18. driver

**Lexeme 1: driver**, NOUN
- grammar: countability Count
- said: /ˈdɹaɪ.vɚ/ (en-US), /ˈdɹaɪ.və/ (en-GB)
  1. [A1] a person who controls a car, bus, train or other vehicle
     - example: Our bus driver waited for everyone to sit down.
     - collocations: taxi driver; bus driver; truck driver; lorry driver; learner driver; drunk driver; careful driver; racing driver; driver's licence; driver's seat
  2. [B2] a program that lets a computer control a piece of equipment
     - example: You need to install the latest printer driver first.
     - collocations: install a driver; update a driver; device driver; printer driver
     - labels: domain computing
  3. [C1] something that strongly influences a process or causes it to happen
     - example: Tourism is the main driver of the island's economy.
     - frames: driver of sth; driver for sth
     - collocations: key driver; main driver; major driver; driver of growth
  4. [C1] a golf club with a large head, used for hitting the ball very far
     - example: He took out his driver and hit the ball straight down the middle.
     - labels: domain sport
- relations: agent_noun_of drive (VERB)

### 19. cent

**Lexeme 1: cent**, NOUN
- grammar: countability Count
- said: /ˈsɛnt/ (en-US), /ˈsent/ (en-GB)
  1. [A1] a small unit of money, or a coin, worth one hundredth of a dollar, euro, etc.
     - example: These apples cost fifty cents each at the market.
     - collocations: a ten-cent coin; cents on the dollar
  2. [B1] a very small amount of money
     - example: I didn't have a cent left after paying the rent.
     - collocations: not a cent; every cent; worth every cent; not spend a cent
     - labels: formality informal
     - grammar: number Sing; note usually in negative sentences or with every
- relations: confusable scent (NOUN): sounds the same as scent, a pleasant smell; confusable sent (VERB): sounds the same as sent, the past of send

### 20. dress

**Lexeme 1: dress**, NOUN
- said: /ˈdɹɛs/ (en-US), /ˈdɹes/ (en-GB)
  1. [A1] a piece of clothing for a woman or girl that covers the body and part of the legs
     - example: She bought a long red dress for her sister's wedding.
     - collocations: wear a dress; put on a dress; wedding dress; summer dress; long dress; short dress
     - grammar: countability Count
  2. [B1] clothes of a particular kind, or for a particular occasion
     - example: Guests are expected to come in formal dress.
     - collocations: formal dress; casual dress; national dress; traditional dress; evening dress
     - grammar: countability Mass
  3. [B2] any piece of clothing, or a set of clothes worn together
     - example: My uncle gave me a new dress for the festival.
     - regions: en-IN mainly
     - grammar: countability Count
**Lexeme 2: dress**, VERB
- grammar: valency Both; irregular False
- said: /ˈdɹɛs/ (en-US), /ˈdɹes/ (en-GB)
  1. [A2] to put clothes on yourself or on someone else
     - example: I usually dress quickly and have breakfast on the train.
     - frames: dress sb
     - collocations: get dressed; dress quickly; dress yourself
     - grammar: valency Both
     - opposites: undress
  2. [B1] to wear clothes of a particular kind, style or colour
     - example: He always dresses smartly when he meets new clients.
     - frames: dress in sth; dress for sth; dress like sb
     - collocations: dress well; dress smartly; dress casually; dress for dinner; dress in black
     - grammar: valency Intr
  3. [C1] to clean and cover a wound to protect it
     - example: The nurse dressed the cut on my arm before I left.
     - frames: dress sth
     - collocations: dress a wound
     - grammar: valency Tr
  4. [C1] to put a sauce on food or prepare it for cooking
     - example: Dress the salad with oil and lemon just before serving.
     - frames: dress sth with sth
     - collocations: dress a salad
     - grammar: valency Tr

### 21. begin

**Lexeme 1: begin**, VERB
- grammar: valency Both; irregular True
- said: /bɪ.ˈɡɪn/ (en-US), /bɪ.ˈɡɪn/ (en-GB)
- forms: began [Tense=Past, VerbForm=Fin] /bɪ.ˈɡæn/ /bɪ.ˈɡæn/; begun [Tense=Past, VerbForm=Part] /bɪ.ˈɡʌn/ /bɪ.ˈɡʌn/; beginning [Tense=Pres, VerbForm=Part] /bɪ.ˈɡɪn.ɪŋ/ /bɪ.ˈɡɪn.ɪŋ/
  1. [A2] to start doing something or do the first part of it
     - example: Please begin the test when you hear the bell.
     - frames: begin sth; begin to do sth; begin doing sth; begin with sth; begin by doing sth
     - collocations: begin work; begin again; begin a career; begin a journey; begin at the beginning
     - grammar: valency Tr, mainly
     - opposites: finish
  2. [A2] to start to happen or to exist
     - example: The film begins at eight, so don't be late.
     - frames: begin at sth; begin with sth
     - collocations: school begins; the meeting begins; the story begins
     - grammar: valency Intr
     - opposites: end
  3. [B2] to start speaking by saying certain words
     - example: "Well," she began slowly, "it's a long story."
     - grammar: valency Tr; note usually with direct speech

### 22. assistant

**Lexeme 1: assistant**, NOUN
- grammar: countability Count
- said: /ə.ˈsɪs.tənt/ (en-US), /ə.ˈsɪs.tənt/ (en-GB)
  1. [A2] a person whose job is to help another person with their work
     - example: My assistant will send you the documents this afternoon.
     - frames: assistant to sb
     - collocations: personal assistant; research assistant; teaching assistant; laboratory assistant; administrative assistant
  2. [A2] a person whose job is to serve customers in a shop
     - example: Ask one of the assistants if they have this shirt in blue.
     - collocations: shop assistant; sales assistant
     - regions: en-GB mainly
  3. [B1] a computer program that answers questions and does tasks for its user
     - example: I asked the voice assistant on my phone to set an alarm.
     - collocations: virtual assistant; voice assistant; digital assistant; AI assistant
- relations: agent_noun_of assist (VERB); confusable assistance (NOUN): assistance is the help given, not the person who gives it
**Lexeme 2: assistant**, ADJ
- grammar: position Attr; comparable False
- said: /ə.ˈsɪs.tənt/ (en-US), /ə.ˈsɪs.tənt/ (en-GB)
  1. [B1] having a job just below the person in charge and helping them
     - example: She was promoted to assistant manager after only a year.
     - collocations: assistant manager; assistant director; assistant professor; assistant coach; assistant editor
     - grammar: note used before a job title
- relations: derived_from assist (VERB)

### 23. wood

**Lexeme 1: wood**, NOUN
- grammar: countability Both
- said: /ˈwʊd/ (en-US), /ˈwʊd/ (en-GB)
  1. [A2] the hard material that the trunk and branches of trees are made of
     - example: The old table is made of solid wood.
     - collocations: made of wood; a piece of wood; chop wood; burn wood; solid wood; dark wood; carved wood
     - grammar: countability Mass, mainly; note counted when kinds are meant: woods such as oak and pine
  2. [A2] an area of land covered with trees, smaller than a forest
     - example: The children love playing in the woods behind our house.
     - collocations: in the woods; walk in the woods; through the woods; deep in the woods
     - grammar: countability Count; note often plural, the woods, with the same meaning
  3. [C2] a golf club with a large rounded head, used for hitting the ball a long way
     - example: He took out a wood for his first shot.
     - labels: domain sport
     - grammar: countability Count
- relations: confusable would (AUX): sounds the same as would, the modal verb

### 24. luggage

**Lexeme 1: luggage**, NOUN
- grammar: countability Mass; note not used with a or in the plural; count with piece of luggage
- said: /ˈlʌ.ɡɪdʒ/ (en-US), /ˈlʌ.ɡɪdʒ/ (en-GB)
  1. [A2] the bags and cases that you carry your things in when you travel
     - example: Please don't leave your luggage unattended in the waiting area.
     - collocations: a piece of luggage; hand luggage; carry-on luggage; lost luggage; excess luggage; check in luggage; pack your luggage; luggage rack; luggage allowance

### 25. brand

**Lexeme 1: brand**, NOUN
- grammar: countability Count
- said: /ˈbɹænd/ (en-US), /ˈbɹænd/ (en-GB)
  1. [A2] a type of product made by one company and sold under its own name
     - example: Which brand of coffee do you usually buy?
     - frames: brand of sth
     - collocations: brand name; brand loyalty; a leading brand; a well-known brand; own brand; a luxury brand; switch brands
  2. [B2] the image and reputation that a company or person shows to the public
     - example: The scandal has seriously damaged the company's brand.
     - collocations: build a brand; personal brand; brand image; brand identity; protect the brand
  3. [C1] a particular kind or style of something, especially a way of thinking or behaving
     - example: Not everyone enjoys his strange brand of humour.
     - frames: brand of sth
     - collocations: a brand of humour; a brand of politics
  4. [C1] a mark burned into an animal's skin to show who owns it
     - example: Each cow on the ranch carries the owner's brand.
- relations: confusable make (NOUN): make is used mainly for the company that produces cars and machines
**Lexeme 2: brand**, VERB
- grammar: valency Tr; irregular False
- said: /ˈbɹænd/ (en-US), /ˈbɹænd/ (en-GB)
  1. [C1] to describe someone or something publicly as bad, often unfairly
     - example: Critics branded the plan a waste of public money.
     - frames: brand sb/sth as sth; brand sb/sth sth; brand sb/sth + adj
     - collocations: branded a traitor; branded a liar
  2. [C1] to give a product a name, logo and image that people will recognize
     - example: The drinks are branded to appeal to young buyers.
     - frames: brand sth
     - labels: domain economics
  3. [C2] to mark an animal by burning its skin with a hot iron
     - example: Ranchers still brand their cattle every spring.
     - frames: brand sth

### 26. afford

**Lexeme 1: afford**, VERB
- grammar: valency Tr; irregular False
- said: /ə.ˈfɔːɹd/ (en-US), /ə.ˈfɔːd/ (en-GB)
  1. [B1] to have enough money or time to buy or do something
     - example: We can't afford a new car this year.
     - frames: can afford sth; can afford to do sth
  2. [B2] to be able to do something without causing problems for yourself
     - example: You can't afford to miss any more classes before the exam.
     - frames: can't afford to do sth; can't afford sth
  3. [C1] to provide someone with something useful or pleasant
     - example: The top floor affords a wonderful view of the sea.
     - frames: afford sth; afford sb sth
     - labels: formality formal

### 27. blog

**Lexeme 1: blog**, NOUN
- grammar: countability Count
- said: /ˈblɑːɡ/ (en-US), /ˈblɔːɡ/ (en-US), /ˈblɒɡ/ (en-GB)
  1. [B1] a website where a person or group regularly writes about their opinions or experiences
     - example: She started a travel blog when she moved abroad.
     - collocations: write a blog; start a blog; read a blog; travel blog; food blog; on a blog; blog post
  2. [B1] a single piece of writing published on such a website
     - example: I wrote a blog about my first week at the new job.
     - frames: blog about sth; blog on sth
     - labels: formality informal
- relations: derived_from weblog (NOUN)
**Lexeme 2: blog**, VERB
- grammar: valency Intr, mainly; irregular False
- said: /ˈblɑːɡ/ (en-US), /ˈblɔːɡ/ (en-US), /ˈblɒɡ/ (en-GB)
  1. [B2] to write and publish pieces on a website about your opinions or experiences
     - example: He has been blogging about local politics for years.
     - frames: blog about sth
     - collocations: blog regularly
- relations: derived_from blog (NOUN)

### 28. weather

**Lexeme 1: weather**, NOUN
- grammar: countability Mass; note not used with a: "what nice weather", not "a nice weather"
- said: /ˈwɛð.ɚ/ (en-US), /ˈweð.ə/ (en-GB)
  1. [A1] how hot, cold, wet, windy or sunny it is outside at a particular time
     - example: If the weather stays fine, let's have lunch in the garden.
     - collocations: bad weather; the weather forecast; weather permitting; in all weathers; hot weather; cold weather; weather conditions
- relations: confusable whether (SCONJ): sounds the same as whether, the word used to introduce a choice
**Lexeme 2: weather**, VERB
- grammar: irregular False
- said: /ˈwɛð.ɚ/ (en-US), /ˈweð.ə/ (en-GB)
  1. [C1] to come safely through a difficult period or situation
     - example: The small company managed to weather the economic crisis.
     - frames: weather sth
     - collocations: weather the storm; weather the crisis; weather the recession
     - grammar: valency Tr
  2. [C1] to change in colour or shape, or make something do so, because of sun, wind and rain over time
     - example: The stone walls have weathered to a soft grey colour.
     - collocations: weather well; weather badly
     - grammar: valency Both

### 29. worry

**Lexeme 1: worry**, VERB
- grammar: valency Both; irregular False
- said: /ˈwɝ.i/ (en-US), /ˈwʌɹ.i/ (en-US), /ˈwʌɹ.i/ (en-GB)
  1. [A2] to keep thinking about problems or bad things that might happen
     - example: My parents always worry when I come home late.
     - frames: worry about sb/sth; worry over sth; worry that ...
     - collocations: don't worry; not to worry; nothing to worry about; worry too much; worry constantly; worry needlessly
     - grammar: valency Intr
  2. [B1] to make someone feel anxious or uneasy
     - example: It worries me that she hasn't called for a week.
     - frames: worry sb; it worries sb that ...
     - collocations: worry yourself sick
     - grammar: valency Tr
**Lexeme 2: worry**, NOUN
- grammar: countability Both
- said: /ˈwɝ.i/ (en-US), /ˈwʌɹ.i/ (en-US), /ˈwʌɹ.i/ (en-GB)
  1. [B1] an unhappy feeling of being anxious about what might happen
     - example: Years of worry had left deep lines on his face.
     - frames: worry about sth; worry over sth
     - collocations: sick with worry; a source of worry; cause worry; cause for worry; free from worry
     - grammar: countability Mass
  2. [B1] a problem or thing that makes you feel anxious
     - example: Our biggest worry is finding somewhere affordable to live.
     - frames: worry that ...; worry for sb
     - collocations: money worries; a major worry; main worry; no worries; the least of your worries
     - grammar: countability Count

### 30. profile

**Lexeme 1: profile**, NOUN
- grammar: countability Count
- said: /ˈpɹoʊ.faɪl/ (en-US), /ˈpɹəʊ.faɪl/ (en-GB)
  1. [B2] a description that gives the main facts about a person, group or thing
     - example: The newspaper published a short profile of the new mayor.
     - frames: profile of sb/sth
     - collocations: write a profile; customer profile; job profile; risk profile
  2. [B1] the page on a website or app where a user gives information about themselves
     - example: Don't forget to add a photo to your profile.
     - collocations: profile picture; update your profile; create a profile; online profile; dating profile
  3. [B2] the shape of a person's face seen from the side
     - example: The coin shows the queen's head in profile.
     - collocations: in profile; side profile
  4. [C1] the amount of public attention that someone or something gets
     - example: The festival has helped raise the town's profile abroad.
     - collocations: keep a low profile; high profile; raise the profile; public profile; a higher profile
     - grammar: note usually singular
  5. [C1] the outline of something seen against a background
     - example: We could see the dark profile of the mountains at sunset.
**Lexeme 2: profile**, VERB
- grammar: valency Tr; irregular False
- said: /ˈpɹoʊ.faɪl/ (en-US), /ˈpɹəʊ.faɪl/ (en-GB)
  1. [C1] to describe someone or something in an article, programme or report
     - example: A local magazine is going to profile our small bakery.
     - frames: profile sb/sth
  2. [C1] to treat people as suspects because of the group they seem to belong to
     - example: Many young men say they are profiled by police at stations.
     - frames: profile sb
     - collocations: racially profile
     - labels: attitude disapproving

### 31. totally

**Lexeme 1: totally**, ADV
- said: /ˈtoʊ.tə.li/ (en-US), /ˈtəʊ.tə.li/ (en-GB)
  1. [B1] completely, to the fullest degree
     - example: The old factory was totally destroyed by the fire.
     - collocations: totally different; totally agree; totally destroyed; totally wrong; totally unexpected; totally free; totally unacceptable
     - opposites: partially
  2. [B1] very or extremely
     - example: Their new song is totally amazing, you have to hear it.
     - collocations: totally awesome; totally cool
     - labels: formality informal
  3. [B2] used to say that you are sure about something or strongly agree
     - example: You should totally come to the beach with us tomorrow.
     - labels: formality informal
     - grammar: note also used alone as a reply to show agreement
- relations: adverb_of total (ADJ)

### 32. apart

**Lexeme 1: apart**, ADV
- said: /ə.ˈpɑːɹt/ (en-US), /ə.ˈpɑːt/ (en-GB)
  1. [B1] separated from each other by a distance or a period of time
     - example: The two villages are only about five miles apart.
     - collocations: far apart; wide apart; miles apart; years apart; worlds apart; spaced apart
     - grammar: comparable True
  2. [B1] not together with another person or thing; separately
     - example: My parents have been living apart for a year now.
     - collocations: live apart; grow apart; keep apart; spend time apart; pull apart; stand apart; set apart
     - opposites: together
  3. [B2] into separate pieces or parts
     - example: This cheap chair is already starting to come apart.
     - collocations: fall apart; come apart; take apart; tear apart; rip apart; blow apart
  4. [C1] used to say that something is not being considered
     - example: Joking apart, I really think you should take the job.
     - frames: sth apart
     - collocations: joking apart; jokes apart
     - grammar: note always after the noun it refers to
**Lexeme 2: apart**, ADJ
- grammar: position Attr; comparable False; note always after the noun
- said: /ə.ˈpɑːɹt/ (en-US), /ə.ˈpɑːt/ (en-GB)
  1. [C1] very different from others of the same kind
     - example: Good mountain guides really are a breed apart.
     - collocations: a breed apart; a world apart; a race apart; a thing apart

### 33. aware

**Lexeme 1: aware**, ADJ
- grammar: comparable True
- said: /ə.ˈwɛɹ/ (en-US), /ə.ˈweə/ (en-GB)
  1. [B1] knowing that something exists, is true or is happening
     - example: Were you aware that the shop closes early today?
     - frames: aware of sth; aware that ...
     - grammar: position Pred
     - opposites: unaware
  2. [B2] knowing a lot about a subject and caring about it
     - example: Young people today are very environmentally aware.
     - collocations: politically aware; socially aware; environmentally aware
     - grammar: note usually modified by an adverb

### 34. fat

**Lexeme 1: fat**, ADJ
- grammar: comparable True
- said: /ˈfæt/ (en-US), /ˈfæt/ (en-GB)
  1. [A1] having a lot of extra flesh on the body and weighing too much
     - example: Our cat got fat because everyone kept feeding him.
     - collocations: get fat; grow fat; fat belly
     - grammar: note often rude when said directly of a person
     - opposites: thin
  2. [B1] thick, wide or round, and often full
     - example: She pulled a fat book from the top shelf.
     - collocations: fat wallet; fat envelope; fat book; fat cigar
     - opposites: thin
  3. [B2] worth a large amount of money
     - example: He earns a fat salary at the bank.
     - collocations: fat salary; fat profit; fat cheque; fat fee
     - labels: formality informal
**Lexeme 2: fat**, NOUN
- said: /ˈfæt/ (en-US), /ˈfæt/ (en-GB)
  1. [B1] the soft white substance under the skin that stores energy
     - example: Regular exercise helps you burn fat and build muscle.
     - collocations: body fat; burn fat; lose fat; layer of fat; belly fat
     - grammar: countability Mass
  2. [B1] an oily substance found in food or used for cooking
     - example: Try to eat less food that is high in fat.
     - collocations: high in fat; low in fat; saturated fat; animal fat; vegetable fat; cut down on fat; fry in fat
     - grammar: countability Both
  3. [C1] the parts of an organization or a plan that waste money
     - example: The new manager promised to cut the fat from the budget.
     - collocations: trim the fat; cut the fat
     - labels: figurative True
     - grammar: countability Mass

### 35. realize

**Lexeme 1: realize**, VERB
- grammar: valency Tr; irregular False
- said: /ˈɹiː.ə.ˌlaɪz/ (en-US), /ˈɹɪə.laɪz/ (en-GB)
- spellings: realize (en-US, en-GB); realise (en-GB, en-AU, en-IN)
  1. [A2] to understand or become aware of a fact or situation
     - example: I didn't realize how late it was until the lights went off.
     - frames: realize that ...; realize sth; realize how ...; realize what ...
     - collocations: suddenly realize; fully realize; come to realize; begin to realize; without realizing it
  2. [B2] to make something that you planned or hoped for actually happen
     - example: She finally realized her dream of opening a small bakery.
     - frames: realize sth
     - collocations: realize a dream; realize an ambition; realize your potential; fears are realized
     - labels: formality formal
  3. [C1] to get money by selling something, or to make a profit in this way
     - example: The company realized a large profit from the sale of its land.
     - frames: realize sth
     - collocations: realize a profit; realize assets; realize a gain
     - labels: formality formal, domain finance
- relations: derived_from real (ADJ)

### 36. mouth

**Lexeme 1: mouth**, NOUN
- said: /ˈmaʊθ/ (en-US), /ˈmaʊθ/ (en-GB)
- forms: mouths [Number=Plur] /ˈmaʊðz/ /ˈmaʊðz/
  1. [A1] the opening in the face used for eating and speaking
     - example: Please don't talk with your mouth full.
     - collocations: open your mouth; shut your mouth; keep your mouth shut; with your mouth full; a big mouth; a mouth to feed; by word of mouth; dry mouth
     - grammar: countability Count
  2. [B2] the place where a river flows into the sea or a lake
     - example: The town grew up at the mouth of the river.
     - frames: the mouth of sth
     - collocations: river mouth; at the mouth of
     - grammar: countability Count
  3. [B2] the opening or entrance of a cave, bottle, tunnel or similar hollow thing
     - example: Bats flew out from the mouth of the cave at dusk.
     - frames: the mouth of sth
     - grammar: countability Count
  4. [C1] rude or boastful talk
     - example: That's enough of your mouth, young man!
     - collocations: all mouth; watch your mouth
     - labels: formality informal
     - grammar: countability Mass
**Lexeme 2: mouth**, VERB
- grammar: valency Tr; irregular False
- said: /ˈmaʊð/ (en-US), /ˈmaʊð/ (en-GB)
  1. [C1] to move your lips as if saying words, but without making any sound
     - example: She mouthed a silent thank you across the crowded room.
     - frames: mouth sth; mouth sth to sb
     - collocations: mouth the words
  2. [C2] to say things you do not really believe or understand
     - example: Politicians often mouth the same old promises before every election.
     - frames: mouth sth
     - collocations: mouth platitudes; mouth slogans
     - labels: attitude disapproving
- relations: derived_from mouth (NOUN)

### 37. institute

**Lexeme 1: institute**, NOUN
- grammar: countability Count
- said: /ˈɪn.stə.tuːt/ (en-US), /ˈɪn.stɪ.tuːt/ (en-US), /ˈɪn.stɪ.tʃuːt/ (en-GB), /ˈɪn.stɪ.tjuːt/ (en-GB)
  1. [B2] an organization for specialist study, professional work or research, or its buildings
     - example: She works at a research institute for marine biology.
- relations: confusable institution (NOUN): institution is a wider word for any large organization or long-established custom
**Lexeme 2: institute**, VERB
- grammar: valency Tr; irregular False
- said: /ˈɪn.stə.tuːt/ (en-US), /ˈɪn.stɪ.tuːt/ (en-US), /ˈɪn.stɪ.tʃuːt/ (en-GB), /ˈɪn.stɪ.tjuːt/ (en-GB)
  1. [C1] to officially start or introduce a rule, system or legal action
     - example: The company will institute a new policy on working from home.
     - frames: institute sth
     - collocations: institute proceedings
     - labels: formality formal

### 38. fuel

**Lexeme 1: fuel**, NOUN
- grammar: countability Mass, mainly
- said: /ˈfjuː.əl/ (en-US), /ˈfjuːl/ (en-US), /ˈfjuː.əl/ (en-GB), /ˈfjuːl/ (en-GB)
  1. [B1] a substance that is burned or used up to produce heat or power
     - example: Our car is almost out of fuel, so let's stop here.
     - collocations: run out of fuel; fossil fuels; fuel consumption; fuel prices; fuel tank; fuel efficiency; solid fuel; nuclear fuel
  2. [C1] something that makes a feeling, argument or situation stronger or more intense
     - example: His angry reply only gave more fuel to the rumours.
     - frames: fuel for sth
     - collocations: add fuel to the fire; add fuel to the flames
     - labels: figurative True
     - grammar: countability Mass
**Lexeme 2: fuel**, VERB
- grammar: valency Tr; irregular False
- said: /ˈfjuː.əl/ (en-US), /ˈfjuːl/ (en-US), /ˈfjuː.əl/ (en-GB), /ˈfjuːl/ (en-GB)
- forms: fuelled [Tense=Past, VerbForm=Fin,Part] /ˈfjuː.əld/ /ˈfjuː.əld/ (en-GB, en-AU, en-IN); fueled [Tense=Past, VerbForm=Fin,Part] /ˈfjuː.əld/ /ˈfjuː.əld/ (en-US); fuelling [Tense=Pres, VerbForm=Part] /ˈfjuː.ə.lɪŋ/ /ˈfjuː.ə.lɪŋ/ (en-GB, en-AU, en-IN); fueling [Tense=Pres, VerbForm=Part] /ˈfjuː.ə.lɪŋ/ /ˈfjuː.ə.lɪŋ/ (en-US)
  1. [B2] to make a feeling, belief or process stronger or make it grow
     - example: Rising prices are fuelling anger among ordinary families.
     - frames: fuel sth
     - collocations: fuel speculation; fuel fears; fuel inflation; fuel growth; fuel demand; fuel anger; fuel a debate
  2. [B2] to supply a vehicle or machine with something that gives it power
     - example: The power station is fuelled by natural gas.
     - frames: fuel sth
     - grammar: note often passive: be fuelled by sth

### 39. lie

**Lexeme 1: lie**, VERB
- grammar: valency Intr; irregular True
- said: /ˈlaɪ/ (en-US), /ˈlaɪ/ (en-GB)
- forms: lay [Tense=Past, VerbForm=Fin] /ˈleɪ/ /ˈleɪ/; lain [Tense=Past, VerbForm=Part] /ˈleɪn/ /ˈleɪn/; lying [Tense=Pres, VerbForm=Part] /ˈlaɪ.ɪŋ/ /ˈlaɪ.ɪŋ/
  1. [A2] to be in a flat position resting on a surface
     - example: The cat was lying in the sun by the window.
     - frames: lie on sth; lie in sth; lie + adj
     - collocations: lie awake; lie still; lie flat; lie in bed; lie on your back; lie face down
  2. [B1] to be in a particular place or position
     - example: The village lies in a deep valley between two hills.
     - collocations: lie ahead; lie to the north; lie off the coast
  3. [B2] to stay in a particular state or condition for some time
     - example: The old factory has lain empty for over twenty years.
     - frames: lie + adj
     - collocations: lie empty; lie idle; lie dormant; lie hidden; lie in wait; lie low
  4. [B2] to be found in something or caused by it
     - example: I think the real problem lies in the design of the system.
     - frames: lie in sth; lie in doing sth
     - collocations: the problem lies; the answer lies; the difficulty lies
  5. [C1] to be the responsibility or decision of a particular person
     - example: In the end, the final decision lies with the committee.
     - frames: lie with sb
     - collocations: responsibility lies with; blame lies with
     - labels: formality formal
- relations: confusable lay (VERB): lay takes an object (lay something down), and lay is also the past tense of lie
**Lexeme 2: lie**, VERB
- grammar: valency Intr; irregular False
- said: /ˈlaɪ/ (en-US), /ˈlaɪ/ (en-GB)
- forms: lying [Tense=Pres, VerbForm=Part] /ˈlaɪ.ɪŋ/ /ˈlaɪ.ɪŋ/
  1. [A2] to say or write something you know is not true
     - example: He admitted that he had lied to the police.
     - frames: lie to sb; lie about sth
     - collocations: lie about your age; lie under oath; lie through your teeth
  2. [B2] to give a false idea of what is real or true
     - example: Be careful, because statistics can lie as easily as people.
     - collocations: the camera never lies; numbers don't lie
**Lexeme 3: lie**, NOUN
- grammar: countability Count
- said: /ˈlaɪ/ (en-US), /ˈlaɪ/ (en-GB)
  1. [A2] something you say or write that you know is not true
     - example: Everything she told us about her past was a lie.
     - frames: lie about sth
     - collocations: tell a lie; tell lies; a white lie; a pack of lies; a complete lie; a barefaced lie; live a lie
     - opposites: truth

### 40. murder

**Lexeme 1: murder**, NOUN
- grammar: countability Both
- said: /ˈmɝ.dɚ/ (en-US), /ˈmɜː.də/ (en-GB)
  1. [B1] the crime of killing a person deliberately and illegally
     - example: Police are treating the man's death as murder.
     - frames: murder of sb
     - collocations: commit murder; attempted murder; charged with murder; accused of murder; convicted of murder; murder case; murder weapon; murder investigation; mass murder; cold-blooded murder
  2. [C1] something very difficult, tiring or unpleasant to deal with
     - example: Driving through the city centre at rush hour is murder.
     - frames: murder on sth
     - labels: formality informal
     - grammar: countability Mass; note usually after be
**Lexeme 2: murder**, VERB
- grammar: valency Tr; irregular False
- said: /ˈmɝ.dɚ/ (en-US), /ˈmɜː.də/ (en-GB)
  1. [B1] to kill a person deliberately and illegally
     - example: The victim was murdered in her own home.
     - frames: murder sb
     - collocations: brutally murdered; murdered in cold blood
  2. [C1] to defeat someone very easily and completely
     - example: Our team got murdered in the final last weekend.
     - frames: murder sb
     - labels: formality informal
  3. [C1] to spoil a song, piece of music or language by doing it very badly
     - example: He murdered every song he tried at the karaoke night.
     - frames: murder sth
     - labels: formality informal
  4. [C1] to want very much to eat or drink something
     - example: I could murder a cup of tea right now.
     - frames: could murder sth
     - labels: formality informal
     - regions: en-GB mainly

### 41. status

**Lexeme 1: status**, NOUN
- grammar: countability Both
- said: /ˈsteɪ.təs/ (en-US), /ˈstæ.təs/ (en-US), /ˈsteɪ.təs/ (en-GB)
  1. [B2] the social or professional position of someone compared with other people
     - example: Teachers here enjoy a fairly high status in society.
     - collocations: social status; high status; low status; equal status; economic status
     - grammar: countability Mass, mainly
  2. [B2] the respect and admiration that come with an important position
     - example: For many people, an expensive watch is a sign of status.
     - collocations: status symbol; gain status; achieve status; celebrity status; cult status
     - grammar: countability Mass
  3. [C1] the official position of a person, group or country in law
     - example: She applied for refugee status after arriving in the country.
     - collocations: legal status; marital status; refugee status; official status; grant status; charitable status
     - grammar: countability Mass, mainly
  4. [B2] the situation of something at a particular time, especially during a process
     - example: Could you give me an update on the status of the project?
     - frames: status of sth
     - collocations: status report; check the status; current status; status quo
     - grammar: countability Mass, mainly
  5. [B1] a short message on a social media profile telling others what you are doing
     - example: He changes his status every time something happens at work.
     - collocations: status update; update your status; post a status
     - grammar: countability Count

### 42. lake

**Lexeme 1: lake**, NOUN
- grammar: countability Count
- said: /ˈleɪk/ (en-US), /ˈleɪk/ (en-GB)
  1. [A1] a large area of water with land all around it
     - example: We went swimming in the lake every summer morning.
     - collocations: on the lake; in the lake

### 43. flight

**Lexeme 1: flight**, NOUN
- said: /ˈflaɪt/ (en-US), /ˈflaɪt/ (en-GB)
  1. [A2] a journey in a plane, or a plane making a regular journey
     - example: Our flight to the coast was delayed by two hours.
     - frames: flight to sth; flight from sth
     - collocations: book a flight; catch a flight; miss a flight; on a flight; direct flight; connecting flight; long-haul flight; flight attendant
     - grammar: countability Count
  2. [B1] movement through the air, or the ability to move through the air
     - example: Young birds learn the skills of flight within a few weeks.
     - collocations: in flight; flight path
     - grammar: countability Mass, mainly
  3. [B2] a set of stairs between one floor or landing and the next
     - example: Her flat is three flights up, and there is no lift.
     - collocations: flight of stairs; flight of steps
     - grammar: countability Count
  4. [C1] escape from a dangerous or unpleasant place or situation
     - example: Thousands of families joined the flight from the burning city.
     - frames: flight from sth
     - collocations: take flight; in full flight
     - grammar: countability Mass
  5. [C1] an imaginative idea or thought that is not realistic
     - example: His plan to sail around the world was just a flight of fancy.
     - collocations: flight of fancy; flight of imagination
     - labels: figurative True
     - grammar: countability Count

### 44. hall

**Lexeme 1: hall**, NOUN
- grammar: countability Count
- said: /ˈhɑːl/ (en-US), /ˈhɔːl/ (en-US), /ˈhɔːl/ (en-GB)
  1. [A2] the space inside a building entrance that leads to other rooms
     - example: You can leave your wet boots in the hall.
     - regions: en-GB mainly
  2. [A2] a passage inside a building with rooms opening onto it
     - example: Our classroom is at the far end of the hall.
     - regions: en-021 mainly
  3. [A2] a large room or building used for public events or shared activities
     - example: Hundreds of people filled the concert hall last night.
  4. [B2] a building where university or college students live
     - example: Most first-year students live in halls near the campus.
     - collocations: in hall; in halls
     - grammar: note bare hall(s) is especially British
  5. [C1] a large house in the countryside
     - example: They live in an old hall surrounded by fields.
     - regions: en-GB mainly
     - grammar: note often part of a house name
- relations: confusable haul (VERB): sounds the same as haul, to pull something heavy

### 45. gain

**Lexeme 1: gain**, VERB
- grammar: valency Both; irregular False
- said: /ˈɡeɪn/ (en-US), /ˈɡeɪn/ (en-GB)
  1. [B1] to get something useful or wanted, often slowly or through effort
     - example: You can gain a lot of experience by working abroad.
     - frames: gain sth; gain sth from sth
     - collocations: gain experience; gain access; gain control; gain confidence; gain support; gain ground; gain popularity; gain an advantage; gain insight
     - grammar: valency Tr
     - opposites: lose
  2. [B1] to get more of something, such as weight, speed or value
     - example: The car slowly gained speed as it went down the hill.
     - frames: gain sth; gain in sth
     - collocations: gain weight; gain speed; gain momentum; gain height; gain in value; gain in popularity
     - grammar: valency Tr, mainly
     - opposites: lose
  3. [B2] to get an advantage or a profit from something
     - example: Who really gains from these changes to the tax system?
     - frames: gain from sth; gain by doing sth
     - collocations: stand to gain; have nothing to gain; have everything to gain
     - grammar: valency Intr
  4. [C1] to get closer to someone or something you are chasing
     - example: The second runner was gaining on the leader with every step.
     - frames: gain on sb/sth
     - grammar: valency Intr
**Lexeme 2: gain**, NOUN
- grammar: countability Both
- said: /ˈɡeɪn/ (en-US), /ˈɡeɪn/ (en-GB)
  1. [B2] an increase in amount, size, value or level
     - example: Regular exercise can help prevent weight gain in later life.
     - frames: gain in sth
     - collocations: weight gain; make gains; a significant gain; a net gain; a small gain
     - grammar: countability Count
     - opposites: loss
  2. [B2] an advantage or profit that you get from something
     - example: He was accused of using his position for personal gain.
     - collocations: financial gain; personal gain; for gain; capital gains; ill-gotten gains; no pain, no gain
     - grammar: countability Mass, mainly; note the plural gains often means money made from an investment
     - opposites: loss
- relations: derived_from gain (VERB)
