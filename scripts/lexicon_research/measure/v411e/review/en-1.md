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
     - example: Draw a square and then divide it into four triangles.
     - collocations: draw a square
  2. [A2] an open public area in a town, with buildings all around it
     - example: Our hotel is on the main square, opposite the old church.
     - collocations: town square; main square; market square; village square
  3. [B1] a piece of something with four straight sides of about equal length
     - example: Would you like a square of chocolate with your coffee?
     - frames: a square of sth
  4. [B1] one of the spaces on a board used for playing a game
     - example: You throw the dice and move forward that many squares.
  5. [B2] the number you get when you multiply a number by itself
     - example: Nine is the square of three.
     - frames: the square of sth
     - labels: domain mathematics
  6. [C2] a person who is boring because they follow old-fashioned ideas
     - example: Don't be such a square, come to the party with us.
     - labels: formality informal, time dated, attitude disapproving
**Lexeme 2: square**, ADJ
- said: /ˈskwɛɹ/ (en-US), /ˈskweə/ (en-GB)
  1. [A2] having four straight equal sides and four right angles
     - example: We bought a small square table for the kitchen.
     - collocations: square table; square box; square window
  2. [B1] used before a unit of length to make a unit of area
     - example: The flat has about sixty square metres of floor space.
     - collocations: square metre; square kilometre; square mile; square foot
     - grammar: position Attr; comparable False; note after a measurement it gives the length of each side: three metres square
  3. [B2] having straight edges and sharp corners rather than curves
     - example: He had a square jaw and very short dark hair.
     - collocations: square jaw; square shoulders; square corner
  4. [B2] not owing anything to each other, or having equal scores
     - example: Here's the money I borrowed, so now we're square.
     - frames: square with sb
     - collocations: all square
     - labels: formality informal
     - grammar: position Pred
  5. [C1] honest and fair
     - example: All the workers want is a square deal from the company.
     - collocations: a square deal; fair and square
  6. [C1] large, healthy and filling enough to satisfy you
     - example: Make sure the children get three square meals a day.
     - collocations: a square meal; three square meals a day
     - grammar: position Attr
  7. [C2] boring because following old-fashioned ideas and ways
     - example: My parents are far too square to like this kind of music.
     - labels: formality informal, time dated, attitude disapproving
**Lexeme 3: square**, ADV
- said: /ˈskwɛɹ/ (en-US), /ˈskweə/ (en-GB)
  1. [C1] directly and exactly, without going to one side
     - example: The ball hit him square on the nose.
     - collocations: square in the face; square in the eye; square on the jaw
     - labels: formality informal
**Lexeme 4: square**, VERB
- grammar: irregular False
- said: /ˈskwɛɹ/ (en-US), /ˈskweə/ (en-GB)
  1. [B2] to multiply a number by itself
     - example: If you square five, you get twenty-five.
     - frames: square sth
     - labels: domain mathematics
     - grammar: valency Tr
  2. [C1] to agree with something else, or to make two things agree
     - example: Her story doesn't square with what the witnesses said.
     - frames: square with sth; square sth with sth
     - grammar: valency Both
  3. [C1] to pull your shoulders back and up, ready to face something difficult
     - example: She squared her shoulders and walked into the meeting.
     - collocations: square your shoulders
     - grammar: valency Tr
  4. [C1] to make the scores in a game or series equal
     - example: A late goal squared the match at two each.
     - collocations: square the match; square the series
     - regions: en-GB mainly
     - grammar: valency Tr
  5. [C2] to get someone's permission or agreement for something
     - example: I'll square it with my manager before I take the day off.
     - frames: square sth with sb
     - labels: formality informal
     - grammar: valency Tr

### 2. reaction

**Lexeme 1: reaction**, NOUN
- said: /ɹi.ˈæk.ʃən/ (en-US), /ɹi.ˈæk.ʃən/ (en-GB)
  1. [B1] what you feel, say or do because of something that has happened
     - example: My first reaction to the news was complete shock.
     - frames: reaction to sth; reaction from sb
     - collocations: initial reaction; mixed reaction; gut reaction; knee-jerk reaction; immediate reaction; positive reaction; provoke a reaction; get a reaction
     - grammar: countability Count
  2. [B2] the ability to move quickly when something suddenly happens
     - example: Good drivers need quick reactions on busy roads.
     - collocations: quick reactions; fast reactions; slow reactions; reaction time
     - grammar: countability Count; number Plur
  3. [B2] a bad effect on the body caused by a drug, food or substance
     - example: Some people have a serious reaction to nuts.
     - frames: reaction to sth
     - collocations: allergic reaction; severe reaction; bad reaction; have a reaction
     - grammar: countability Count
  4. [B2] a process in which substances change into other substances when they meet
     - example: Heat speeds up the reaction between the two liquids.
     - collocations: chemical reaction; chain reaction; nuclear reaction
     - labels: domain chemistry
     - grammar: countability Count
  5. [C1] a change in ideas or behaviour to the opposite of what was before
     - example: Her simple style was a reaction against the fashions of her time.
     - frames: reaction against sth
     - grammar: countability Count
  6. [C2] strong opposition to social or political change
     - example: The reformers saw themselves as fighting the forces of reaction.
     - collocations: forces of reaction
     - labels: domain politics
     - grammar: countability Mass
- relations: derived_from react (VERB)

### 3. mad

**Lexeme 1: mad**, ADJ
- grammar: comparable True
- said: /ˈmæd/ (en-US), /ˈmæd/ (en-GB)
  1. [A2] very angry or annoyed
     - example: My sister is still mad at me for breaking her phone.
     - frames: mad at sb; mad with sb; mad about sth
     - collocations: get mad; hopping mad; really mad; so mad
     - labels: formality informal
     - regions: en-US mainly
     - grammar: position Pred, mainly; note mad at sb is usual in American English, mad with sb in British English
  2. [B1] very silly or unwise, not thinking about the danger or result
     - example: You'd be mad to turn down a job offer like that.
     - frames: mad to do sth
     - collocations: completely mad; a mad idea; must be mad
     - labels: formality informal
     - regions: en-GB mainly
  3. [B1] liking someone or something very much
     - example: Both of my boys are mad about football.
     - frames: mad about sb/sth; mad on sth; mad for sth
     - collocations: mad keen; football mad
     - labels: formality informal
     - grammar: position Pred
  4. [B2] done quickly and wildly, without control or order
     - example: There was a mad rush for the doors when the shop opened.
     - collocations: a mad rush; a mad dash; a mad scramble; like mad
     - grammar: position Attr, mainly
  5. [B2] having a serious mental illness
     - example: In the story, the old king slowly goes mad.
     - frames: drive sb mad
     - collocations: go mad; stark raving mad
     - labels: time dated, attitude offensive
     - regions: en-GB mainly
  6. [B2] very strange, surprising or hard to believe
     - example: It's mad how quickly the town has changed.
     - collocations: that's mad
     - labels: formality informal
     - regions: en-GB mainly, en-IE mainly
**Lexeme 2: mad**, ADV
- said: /ˈmæd/ (en-US), /ˈmæd/ (en-GB)
  1. [C1] to a very great degree; extremely
     - example: That new pizza place down the road is mad good.
     - labels: formality slang
     - grammar: note used before an adjective

### 4. sad

**Lexeme 1: sad**, ADJ
- grammar: comparable True
- said: /ˈsæd/ (en-US), /ˈsæd/ (en-GB)
  1. [A1] unhappy, often because something bad has happened
     - example: She felt sad when her best friend moved away.
     - frames: sad about sth; sad to do sth; sad that ...
     - collocations: feel sad; look sad; deeply sad; sad face; sad eyes
     - opposites: happy
  2. [A2] making people feel unhappy
     - example: It was a sad film, and half the audience cried.
     - frames: it is sad that ...; it is sad to do sth
     - collocations: sad news; sad story; sad song; sad day; sad ending; sad occasion
  3. [B2] bad or disappointing in a way that makes you feel sorry or ashamed
     - example: The old theatre is in a sad state after years of neglect.
     - collocations: a sad state of affairs; the sad truth; sad fact; sad to say; sad but true
     - grammar: position Attr, mainly
  4. [B2] boring, unfashionable or embarrassing, so that people laugh at you
     - example: My brother thinks anyone who still collects stamps is a bit sad.
     - labels: formality informal, attitude disapproving
     - regions: en-GB mainly

### 5. file

**Lexeme 1: file**, NOUN
- grammar: countability Count
- said: /ˈfaɪl/ (en-US), /ˈfaɪl/ (en-GB)
  1. [A2] a document or set of data stored on a computer under its own name
     - example: Remember to save the file before you close the program.
     - collocations: open a file; save a file; delete a file; attach a file; download a file; send a file; large file
  2. [B1] a box or folder for keeping papers together, or the papers kept in it
     - example: The police kept a file on him for many years.
     - frames: file on sb/sth
     - collocations: on file; keep a file; open a file; case file; personnel file; secret file
  3. [B2] a metal tool with a rough surface, used for making things smooth or shaping them
     - example: He smoothed the rough edge of the metal with a file.
     - collocations: nail file
  4. [C1] a line of people or things, one behind the other
     - example: The children walked in single file across the narrow bridge.
     - collocations: in single file; single file
**Lexeme 2: file**, VERB
- grammar: irregular False
- said: /ˈfaɪl/ (en-US), /ˈfaɪl/ (en-GB)
  1. [B2] to officially send or give a document or legal claim to an authority
     - example: Several customers have filed complaints about the service.
     - frames: file sth; file sth against sb; file sth with sb; file for sth
     - collocations: file a complaint; file a lawsuit; file for divorce; file for bankruptcy; file charges; file a claim; file a tax return
     - grammar: valency Both
  2. [B2] to put a document in a particular place with others, in order
     - example: I file all my bills in a drawer by the desk.
     - frames: file sth; file sth under sth
     - grammar: valency Tr
  3. [C1] to walk in a line, one person after another
     - example: After the concert, people filed slowly out of the hall.
     - frames: file into sth; file out of sth; file past sth
     - grammar: valency Intr
  4. [B2] to make something smooth, shape it or cut it with a rough metal tool
     - example: She sat on the sofa, filing her nails and watching television.
     - frames: file sth
     - collocations: file your nails
     - grammar: valency Tr
  5. [C1] to send a news story or report to a newspaper or broadcaster
     - example: Our reporter filed this story from the border last night.
     - frames: file sth
     - collocations: file a story; file a report
     - grammar: valency Tr

### 6. northern

**Lexeme 1: northern**, ADJ
- grammar: position Attr
- said: /ˈnɔːɹ.ðɚn/ (en-US), /ˈnɔː.ðən/ (en-GB)
  1. [A2] situated in, coming from or facing the direction opposite to south
     - example: Snow is common in the northern part of the country.
     - collocations: northern hemisphere; northern lights; northern border; northern coast; northern edge; northern Europe
     - opposites: southern
  2. [B1] typical of the people or way of life in the part of a country away from the south
     - example: After twenty years abroad, he still has a strong northern accent.
     - collocations: northern accent; northern town
     - grammar: note in British English often written Northern, meaning the North of England
     - opposites: southern
- relations: adjective_of north (NOUN)

### 7. vehicle

**Lexeme 1: vehicle**, NOUN
- grammar: countability Count
- said: /ˈviː.ə.kəl/ (en-US), /ˈviː.hɪ.kəl/ (en-US, regional), /ˈviː.ə.kəl/ (en-GB), /ˈvɪə.kəl/ (en-GB)
  1. [B1] a machine with wheels or an engine that carries people or goods
     - example: Please do not leave your vehicle parked outside the gate.
     - collocations: motor vehicle; electric vehicle; emergency vehicle; commercial vehicle; drive a vehicle; park a vehicle; stolen vehicle
  2. [C1] a way of expressing ideas or feelings, or of achieving something
     - example: The new film is a perfect vehicle for her comic talent.
     - frames: vehicle for sth
     - collocations: vehicle for change; ideal vehicle; perfect vehicle
     - labels: figurative True

### 8. smart

**Lexeme 1: smart**, ADJ
- grammar: comparable True
- said: /ˈsmɑːɹt/ (en-US), /ˈsmɑːt/ (en-GB)
  1. [A2] quick to understand and learn, and good at thinking
     - example: She's a smart kid who always asks good questions.
     - collocations: smart move; smart choice; smart decision; smart idea; smart enough
     - opposites: stupid
  2. [B1] clean, tidy and dressed in good clothes
     - example: You look very smart in that new suit.
     - collocations: look smart; smart clothes; smart suit; smart casual
     - regions: en-GB mainly
     - opposites: scruffy
  3. [B2] fashionable and expensive, and used by rich people
     - example: They booked a table at a smart restaurant in town.
     - collocations: smart restaurant; smart hotel; smart area; smart address
     - regions: en-GB mainly
  4. [B1] controlled by a computer and able to connect to the internet
     - example: Our new smart TV lets us watch films online.
     - collocations: smart card; smart TV; smart home; smart meter; smart watch
     - grammar: position Attr; comparable False
  5. [B2] answering in a clever but rude way that shows no respect
     - example: Don't get smart with me, young man!
     - frames: get smart with sb
     - collocations: get smart; smart remark; smart answer
     - labels: formality informal, attitude disapproving
  6. [C1] quick and with a lot of force or energy
     - example: We set off at a smart pace towards the hills.
     - collocations: smart pace; smart blow; smart salute
     - grammar: position Attr
**Lexeme 2: smart**, VERB
- grammar: valency Intr; irregular False
- said: /ˈsmɑːɹt/ (en-US), /ˈsmɑːt/ (en-GB)
  1. [C1] to feel or cause a sharp stinging pain
     - example: My eyes were smarting from the smoke of the fire.
     - frames: smart from sth
     - grammar: note often in the progressive
  2. [C1] to feel upset and hurt because of something unfair or embarrassing
     - example: The team is still smarting from last week's heavy defeat.
     - frames: smart from sth; smart over sth
     - grammar: note usually in the progressive
**Lexeme 3: smart**, NOUN
- grammar: note usually singular, with the
- said: /ˈsmɑːɹt/ (en-US), /ˈsmɑːt/ (en-GB)
  1. [C2] a sharp stinging pain in the body or the mind
     - example: He could still feel the smart of her harsh words.
     - frames: the smart of sth
     - labels: style literary
- relations: derived_from smart (VERB)

### 9. influence

**Lexeme 1: influence**, NOUN
- grammar: countability Both
- said: /ˈɪn.flu.əns/ (en-US), /ɪn.ˈflu.əns/ (en-US, disputed), /ˈɪn.flu.əns/ (en-GB)
  1. [B1] the effect that someone or something has on how people think, behave or develop
     - example: Television has a huge influence on the way children speak.
     - frames: influence on sb/sth; influence of sb/sth; under the influence of sb/sth
     - collocations: have an influence on; strong influence; great influence; cultural influence; growing influence; come under the influence of
  2. [B2] the power to make people or things behave or change in a way you want
     - example: Big companies often use their influence to change the law.
     - frames: influence over sb/sth; influence with sb
     - collocations: use your influence; exert influence; political influence; sphere of influence; a person of influence; gain influence; lose influence
     - grammar: countability Mass
  3. [B2] a person or thing that changes how someone thinks or behaves
     - example: My grandmother was the biggest influence in my early life.
     - frames: influence on sb
     - collocations: a good influence; a bad influence; a major influence; an early influence
     - grammar: countability Count
**Lexeme 2: influence**, VERB
- grammar: valency Tr; irregular False
- said: /ˈɪn.flu.əns/ (en-US), /ɪn.ˈflu.əns/ (en-US, disputed), /ˈɪn.flu.əns/ (en-GB)
  1. [B1] to have an effect on what someone does or thinks or on how something develops
     - example: Don't let other people influence your decision about the job.
     - frames: influence sb/sth; influence sb to do sth; be influenced by sb/sth
     - collocations: strongly influence; heavily influenced; greatly influence; influence a decision; influence the outcome; directly influence
- relations: derived_from influence (NOUN)

### 10. trial

**Lexeme 1: trial**, NOUN
- grammar: countability Both
- said: /ˈtɹaɪ.əl/ (en-US), /ˈtɹaɪ.əl/ (en-GB)
  1. [B1] a formal process in a court of law where evidence is examined to decide a case
     - example: The two men will stand trial for robbery next month.
     - frames: trial for sth
     - collocations: stand trial; go on trial; on trial; face trial; a fair trial; trial by jury; murder trial; await trial
     - grammar: countability Both
  2. [B2] a test of how well, safely or usefully someone or something works, often over a period
     - example: The new drug is now being tested in clinical trials.
     - frames: trial of sth
     - collocations: clinical trial; on trial; trial and error; free trial; trial period; trial run; carry out a trial
     - grammar: countability Both
  3. [C1] a competition or set of tests held to choose the best players or animals for a team or event
     - example: She was picked for the national squad after the spring trials.
     - labels: domain sport
     - regions: en-GB mainly
     - grammar: countability Count; number Plur, mainly
  4. [C1] a person or situation that causes you difficulty or annoyance
     - example: My little brother can be a real trial at times.
     - frames: trial to sb
     - collocations: trials and tribulations
     - grammar: countability Count
- relations: confusable trail (NOUN): trail, a path or a track left behind, has the same letters in a different order
**Lexeme 2: trial**, VERB
- grammar: valency Tr; irregular False
- said: /ˈtɹaɪ.əl/ (en-US), /ˈtɹaɪ.əl/ (en-GB)
- forms: trialled [Tense=Past, VerbForm=Fin,Part] /ˈtɹaɪ.əld/ /ˈtɹaɪ.əld/ (en-GB, en-AU, en-IN); trialed [Tense=Past, VerbForm=Fin,Part] /ˈtɹaɪ.əld/ /ˈtɹaɪ.əld/ (en-US); trialling [Tense=Pres, VerbForm=Part] /ˈtɹaɪ.ə.lɪŋ/ /ˈtɹaɪ.ə.lɪŋ/ (en-GB, en-AU, en-IN); trialing [Tense=Pres, VerbForm=Part] /ˈtɹaɪ.ə.lɪŋ/ /ˈtɹaɪ.ə.lɪŋ/ (en-US)
  1. [C1] to test a new product, method or player over a period to see how well they work
     - example: Several schools are trialling a shorter school day this year.
     - frames: trial sth
     - collocations: trial a scheme; trial a system
     - regions: en-GB mainly

### 11. slightly

**Lexeme 1: slightly**, ADV
- said: /ˈslaɪt.li/ (en-US), /ˈslaɪt.li/ (en-GB)
  1. [B1] a little; to a small amount or degree
     - example: Prices have gone up slightly since last month.
     - collocations: slightly different; only slightly; slightly better; slightly more; slightly less; slightly higher; slightly worried
  2. [C1] with a thin, light and not very strong body
     - example: He was slightly built but surprisingly strong.
     - collocations: slightly built
     - grammar: note almost always used before built
- relations: adverb_of slight (ADJ)

### 12. foot

**Lexeme 1: foot**, NOUN
- grammar: countability Count; irregular True
- said: /ˈfʊt/ (en-US), /ˈfʊt/ (en-GB)
- forms: feet [Number=Plur] /ˈfiːt/ /ˈfiːt/; foot [Number=Plur] /ˈfʊt/ /ˈfʊt/ (informal, in measurements: six foot tall)
  1. [A1] the part of the body at the end of the leg that you stand and walk on
     - example: My feet hurt after walking around the city all day.
     - collocations: on foot; bare feet; set foot in; on your feet; get to your feet; rise to your feet; stamp your feet; sore feet; left foot; right foot
  2. [A2] a unit for measuring length, equal to twelve inches or about 30 centimetres
     - example: The wall is about six feet high and very old.
     - collocations: square foot; cubic foot; a foot long
  3. [B1] the lowest part or bottom of something
     - example: There is a small village at the foot of the mountain.
     - frames: the foot of sth; at the foot of sth
     - collocations: the foot of the stairs; the foot of the hill; the foot of the page; at the foot of the mountain
     - grammar: number Sing
  4. [B2] the end of a bed or table opposite the end where the head is
     - example: The cat likes to sleep at the foot of my bed.
     - frames: the foot of sth; at the foot of sth
     - collocations: the foot of the bed; the foot of the table
     - grammar: number Sing
     - opposites: head
**Lexeme 2: foot**, VERB
- grammar: valency Tr; irregular False
- said: /ˈfʊt/ (en-US), /ˈfʊt/ (en-GB)
  1. [C1] to pay for something, especially something that costs a lot
     - example: Taxpayers will have to foot the bill for the new stadium.
     - frames: foot sth
     - collocations: foot the bill
     - labels: formality informal

### 13. traditional

**Lexeme 1: traditional**, ADJ
- grammar: comparable True
- said: /tɹə.ˈdɪʃ.ə.nəl/ (en-US), /tɹə.ˈdɪʃ.nəl/ (en-US), /tɹə.ˈdɪʃ.ə.nəl/ (en-GB), /tɹə.ˈdɪʃ.nəl/ (en-GB)
  1. [A2] belonging to customs or beliefs that a group has kept for a very long time
     - example: At the wedding, many guests wore traditional dress from the region.
     - collocations: traditional dress; traditional costume; traditional food; traditional music; traditional dish; traditional medicine; traditional festival
     - grammar: position Attr, mainly
  2. [B1] following older, accepted ways of doing things rather than new ones
     - example: My grandparents are quite traditional about how holidays should be spent.
     - frames: traditional about sth
     - collocations: traditional values; traditional methods; traditional view; traditional way; traditional family; fairly traditional
- relations: adjective_of tradition (NOUN)

### 14. estate

**Lexeme 1: estate**, NOUN
- grammar: countability Count
- said: /ɪ.ˈsteɪt/ (en-US), /ə.ˈsteɪt/ (en-US), /ɪ.ˈsteɪt/ (en-GB)
  1. [B2] a large area of land in the country owned by one person, family or company
     - example: The old house stands in the middle of a huge estate with its own forest.
     - collocations: country estate; family estate; tea estate; on the estate; large estate
  2. [C1] all the money and property that a person owns, especially at their death
     - example: Her whole estate was left to a charity for homeless children.
     - collocations: leave an estate; settle an estate; estate tax; inherit an estate
  3. [B2] an area where many houses or buildings were planned and built together
     - example: They grew up on a small estate on the edge of town.
     - collocations: housing estate; council estate; industrial estate; on an estate
     - regions: en-GB mainly
     - grammar: note used with on: live on an estate
  4. [B2] a car with a long body and a door at the back for loading things
     - example: We need an estate to fit the dog and all our luggage.
     - regions: en-GB mainly
  5. [C2] one of the classes of people who shared political power in a country in the past
     - example: In medieval times the clergy formed the first estate of the kingdom.
     - collocations: the fourth estate; the three estates
     - labels: time historical

### 15. sport

**Lexeme 1: sport**, NOUN
- grammar: countability Both
- said: /ˈspɔːɹt/ (en-US), /ˈspɔːt/ (en-GB)
  1. [A1] a game or activity needing physical effort and skill, played by rules
     - example: Football is the most popular sport in many countries.
     - collocations: play a sport; do sport; team sport; winter sports; contact sport; spectator sport; extreme sports; professional sport
     - grammar: countability Both; note British English often uses uncountable sport where American English says sports
  2. [B2] a person who stays cheerful and fair when losing or being teased
     - example: Thanks for being such a good sport about the joke.
     - collocations: a good sport; be a sport; a bad sport
     - labels: formality informal
     - grammar: countability Count; note usually with good or bad
  3. [C1] pleasure or fun, especially from hunting or fishing animals
     - example: In the past, wealthy men shot birds purely for sport.
     - collocations: for sport
     - grammar: countability Mass
  4. [C1] used as a friendly way of speaking to a man or boy
     - example: Cheer up, sport, it's not the end of the world.
     - labels: formality informal, time dated
     - regions: en-AU mainly
     - grammar: countability Count
**Lexeme 2: sport**, VERB
- grammar: valency Tr; irregular False
- said: /ˈspɔːɹt/ (en-US), /ˈspɔːt/ (en-GB)
  1. [C1] to wear or have something in a way that people notice
     - example: He turned up at the party sporting a bright red tie.
     - frames: sport sth
     - collocations: sport a beard; sport a tattoo; sport a moustache

### 16. dear

**Lexeme 1: dear**, ADJ
- grammar: comparable True
- said: /ˈdɪɹ/ (en-US), /ˈdɪə/ (en-GB)
  1. [A1] used before a name or title to begin a letter or email
     - example: I started my letter with Dear Sir or Madam, as usual.
     - collocations: Dear Sir; Dear Madam; Dear Sir or Madam
     - grammar: position Attr; comparable False
  2. [B1] loved very much, or very important and precious to someone
     - example: She has been my dearest friend since we were at school.
     - frames: dear to sb; hold sth dear
     - collocations: dear friend; near and dear; dear to my heart; my dear fellow
  3. [B2] costing a lot of money
     - example: Fruit is getting dearer every week at the market.
     - regions: en-GB mainly, en-IE mainly
     - opposites: cheap
  4. [C1] sweet and pleasant to look at
     - example: They rented a dear little cottage by the sea.
     - labels: formality informal, time dated
     - grammar: position Attr, mainly
- relations: confusable deer (NOUN): sounds the same as deer, the wild animal
**Lexeme 2: dear**, NOUN
- grammar: countability Count
- said: /ˈdɪɹ/ (en-US), /ˈdɪə/ (en-GB)
  1. [A2] used to speak to someone you love, or kindly to anyone
     - example: Would you like another cup of tea, dear?
     - collocations: my dear
     - labels: formality informal
  2. [B2] a kind and lovable person
     - example: Your grandmother is such a dear to help us like this.
     - collocations: be a dear
     - labels: formality informal
**Lexeme 3: dear**, ADV
- said: /ˈdɪɹ/ (en-US), /ˈdɪə/ (en-GB)
  1. [C1] at a high cost in money, suffering or loss
     - example: That single mistake cost the team dear in the final.
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
     - example: My grandfather is ninety and still very healthy.
     - collocations: perfectly healthy; fit and healthy; stay healthy; keep healthy; healthy skin; healthy hair; healthy appetite
     - opposites: unhealthy
  2. [A2] good for your body and helping you to stay well
     - example: Try to eat a healthy breakfast before school.
     - collocations: healthy diet; healthy food; healthy eating; healthy lifestyle; healthy habits
     - opposites: unhealthy
  3. [B2] successful and in a good condition
     - example: A healthy economy needs both strong exports and spending.
     - collocations: healthy economy; healthy relationship; healthy competition; financially healthy
     - labels: figurative True
     - opposites: unhealthy
  4. [C1] fairly large in amount or size
     - example: The shop made a healthy profit over the holidays.
     - collocations: healthy profit; healthy margin; healthy lead
     - grammar: position Attr
  5. [C1] normal and sensible, showing good judgment
     - example: It's good to have a healthy respect for the sea.
     - collocations: healthy respect; healthy scepticism; healthy interest
     - grammar: position Attr, mainly
- relations: derived_from health (NOUN)

### 18. driver

**Lexeme 1: driver**, NOUN
- grammar: countability Count
- said: /ˈdɹaɪ.vɚ/ (en-US), /ˈdɹaɪ.və/ (en-GB)
  1. [A1] a person who controls a car, bus, train or other vehicle
     - example: My uncle has worked as a taxi driver for twenty years.
     - collocations: taxi driver; bus driver; truck driver; learner driver; racing driver; drunk driver; careful driver; the driver's seat
  2. [B2] a program that lets a computer work with a particular piece of equipment
     - example: You need to install the latest driver for your new printer.
     - collocations: install a driver; update a driver; printer driver; device driver
     - labels: domain computing
  3. [C1] one of the main things that cause something to happen or grow
     - example: Tourism is a key driver of the local economy.
     - frames: driver of sth; driver for sth
     - collocations: key driver; main driver; major driver; economic driver
  4. [C1] a golf club with a large head, used for hitting the ball very far
     - example: He used his driver to hit the ball down the fairway.
     - labels: domain sport
- relations: agent_noun_of drive (VERB)

### 19. cent

**Lexeme 1: cent**, NOUN
- grammar: countability Count
- said: /ˈsɛnt/ (en-US), /ˈsent/ (en-GB)
  1. [A1] a small unit of money, one hundredth of a dollar, euro or similar, or a coin worth this
     - example: This pen costs ninety-nine cents at the corner shop.
     - collocations: every cent; not a cent; a few cents; cents on the dollar
- relations: confusable scent (NOUN): sounds the same as scent, a pleasant smell, and sent, the past of send

### 20. dress

**Lexeme 1: dress**, NOUN
- said: /ˈdɹɛs/ (en-US), /ˈdɹes/ (en-GB)
  1. [A1] a piece of clothing for a woman or girl that covers the body and hangs down over the legs
     - example: She bought a long blue dress for the wedding.
     - collocations: wear a dress; put on a dress; wedding dress; summer dress; evening dress; long dress; short dress
     - grammar: countability Count
  2. [B2] clothes of a particular type, worn for an occasion or by a group
     - example: Guests are asked to wear formal dress to the dinner.
     - collocations: formal dress; national dress; traditional dress; casual dress; evening dress; dress code
     - grammar: countability Mass
  3. [B1] any piece of clothing, or a set of clothes worn together
     - example: All pupils must wear the school dress on Mondays.
     - collocations: school dress
     - regions: en-IN only
     - grammar: countability Count
**Lexeme 2: dress**, VERB
- grammar: valency Both; irregular False
- said: /ˈdɹɛs/ (en-US), /ˈdɹes/ (en-GB)
  1. [A2] to put clothes on yourself or on someone else
     - example: I usually wash and dress before I have breakfast.
     - frames: dress sb
     - collocations: get dressed; dress quickly
     - grammar: valency Both; note get dressed is more usual for putting on your own clothes
     - opposites: undress
  2. [B1] to wear clothes of a particular kind or style
     - example: He always dresses in black when he goes on stage.
     - frames: dress in sth; dress as sb/sth; dress for sth; dress + adv
     - collocations: dress well; dress smartly; dress casually; dress for dinner; dress for the occasion
     - grammar: valency Intr
  3. [C1] to clean, treat and cover a wound
     - example: The nurse cleaned and dressed the cut on my knee.
     - frames: dress sth
     - collocations: dress a wound
     - grammar: valency Tr
  4. [C1] to prepare food for cooking or eating, especially by adding a sauce
     - example: Dress the salad just before you serve it.
     - frames: dress sth
     - collocations: dress a salad; dress a chicken
     - grammar: valency Tr
  5. [C2] to decorate or arrange something so that it looks attractive
     - example: The staff were busy dressing the shop windows for the holidays.
     - frames: dress sth
     - collocations: dress a window; dress the set
     - grammar: valency Tr

### 21. begin

**Lexeme 1: begin**, VERB
- grammar: irregular True; valency Both
- said: /bɪ.ˈɡɪn/ (en-US), /bə.ˈɡɪn/ (en-US), /bɪ.ˈɡɪn/ (en-GB)
- forms: began [Tense=Past, VerbForm=Fin] /bɪ.ˈɡæn/ /bɪ.ˈɡæn/; begun [Tense=Past, VerbForm=Part] /bɪ.ˈɡʌn/ /bɪ.ˈɡʌn/
  1. [A1] to start doing something or to do the first part of it
     - example: I began to feel nervous as the exam got closer.
     - frames: begin sth; begin to do sth; begin doing sth; begin by doing sth; begin with sth
     - collocations: begin work; begin a career; begin a journey; begin again; begin immediately; to begin with
     - grammar: valency Tr, mainly
     - opposites: finish
  2. [A2] to start to happen or to exist
     - example: The concert begins at eight, so don't be late.
     - frames: begin with sth; begin at sth
     - collocations: the story begins; the season begins; the meeting begins; life begins
     - grammar: valency Intr
     - opposites: end

### 22. assistant

**Lexeme 1: assistant**, NOUN
- grammar: countability Count
- said: /ə.ˈsɪs.tənt/ (en-US), /ə.ˈsɪs.tənt/ (en-GB)
  1. [A2] a person whose job is to help another person with their work
     - example: My assistant will send you the details by email.
     - frames: assistant to sb
     - collocations: personal assistant; research assistant; teaching assistant; administrative assistant; lab assistant; dental assistant
  2. [A2] a person whose job is to serve customers in a shop
     - example: An assistant helped me find a jacket in my size.
     - collocations: shop assistant; sales assistant
     - regions: en-GB mainly
  3. [B2] a computer program that answers questions and does simple tasks for its user
     - example: I asked the voice assistant on my phone to set an alarm.
     - collocations: virtual assistant; voice assistant; digital assistant; AI assistant
- relations: agent_noun_of assist (VERB)
**Lexeme 2: assistant**, ADJ
- grammar: position Attr; comparable False
- said: /ə.ˈsɪs.tənt/ (en-US), /ə.ˈsɪs.tənt/ (en-GB)
  1. [B1] having a job just below a senior person and helping them in it
     - example: She was promoted to assistant manager last spring.
     - collocations: assistant manager; assistant director; assistant professor; assistant coach; assistant editor

### 23. wood

**Lexeme 1: wood**, NOUN
- grammar: countability Both
- said: /ˈwʊd/ (en-US), /ˈwʊd/ (en-GB)
  1. [A2] the hard material that the trunk and branches of a tree are made of
     - example: Grandpa made this little table out of old wood.
     - collocations: made of wood; a piece of wood; chop wood; solid wood; a wood fire; carved wood; dry wood
     - grammar: countability Mass, mainly; note counted when kinds are meant: soft woods such as pine
  2. [A2] an area of land covered with trees, smaller than a forest
     - example: Let's take the dog for a walk in the woods.
     - collocations: in the woods; a walk in the woods; through the woods; deep in the woods; dense woods
     - grammar: countability Count; note often plural, the woods, for one such area
  3. [C1] a golf club with a long handle and a large rounded head, used for long shots
     - example: She took out her three wood for the long shot.
     - labels: domain sport
     - grammar: countability Count
- relations: confusable would (AUX): sounds the same as would, the modal verb

### 24. luggage

**Lexeme 1: luggage**, NOUN
- grammar: countability Mass; note one item is "a piece of luggage", not "a luggage"
- said: /ˈlʌɡ.ɪdʒ/ (en-US), /ˈlʌɡ.ɪdʒ/ (en-GB)
  1. [A2] the bags and cases that you carry your things in when you travel
     - example: Please don't leave your luggage unattended anywhere in the station.
     - collocations: piece of luggage; hand luggage; carry-on luggage; check in luggage; lost luggage; excess luggage; luggage allowance; pack luggage; collect luggage

### 25. brand

**Lexeme 1: brand**, NOUN
- grammar: countability Count
- said: /ˈbɹænd/ (en-US), /ˈbɹænd/ (en-GB)
  1. [A2] a type of product made by one company and sold under its own name
     - example: Which brand of coffee do you usually buy?
     - frames: brand of sth
     - collocations: brand name; leading brand; well-known brand; brand loyalty; own brand; designer brand; switch brands
  2. [B2] the name, image and reputation by which a company or person is known to the public
     - example: The company spent millions trying to build a stronger brand.
     - collocations: build a brand; brand image; brand identity; personal brand; protect the brand; damage the brand
  3. [C1] a particular kind or style of something that one person or group has
     - example: Not everyone enjoys his strange brand of humour.
     - frames: sb's brand of sth; brand of sth
  4. [C2] a mark burned into an animal's skin with hot metal to show who owns it
     - example: Each cow carried the farm's brand on its side.
**Lexeme 2: brand**, VERB
- grammar: valency Tr; irregular False
- said: /ˈbɹænd/ (en-US), /ˈbɹænd/ (en-GB)
  1. [B2] to describe someone or something as bad, often unfairly
     - example: The newspapers branded him a liar after the scandal.
     - frames: brand sb sth; brand sb as sth; brand sb + adj
     - collocations: branded a traitor; branded a failure; branded a criminal
     - labels: attitude disapproving
  2. [C1] to mark an animal's skin with hot metal to show who owns it
     - example: In spring the ranchers brand the young cattle.
     - frames: brand sth
  3. [C1] to give a product a company's name and image so people recognize it
     - example: The shop sells cheap clothes that are branded as luxury goods.
     - frames: brand sth; brand sth as sth
     - grammar: note often in the passive or as the adjective branded
  4. [C2] to fix something firmly in someone's memory so it cannot be forgotten
     - example: That terrible night is branded on my memory forever.
     - frames: be branded on sth; be branded into sth
     - labels: style literary, figurative True

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
  1. [A2] a website where a person or group regularly writes about their opinions, experiences or interests
     - example: My sister writes a travel blog about her trips abroad.
     - frames: blog about sth
     - collocations: write a blog; start a blog; read a blog; post on a blog; travel blog; food blog; blog post
  2. [B1] a single piece of writing published on such a website
     - example: Did you see her latest blog on healthy cooking?
     - grammar: note blog post is more usual
- relations: derived_from weblog (NOUN): a shortened form of weblog
**Lexeme 2: blog**, VERB
- grammar: valency Intr, mainly; irregular False
- said: /ˈblɑːɡ/ (en-US), /ˈblɔːɡ/ (en-US), /ˈblɒɡ/ (en-GB)
  1. [B1] to write and publish pieces of writing on a website regularly
     - example: He has been blogging about local politics for five years.
     - frames: blog about sth
     - collocations: blog regularly
- relations: derived_from blog (NOUN)

### 28. weather

**Lexeme 1: weather**, NOUN
- grammar: countability Mass
- said: /ˈwɛð.ɚ/ (en-US), /ˈweð.ə/ (en-GB)
  1. [A1] the conditions in the air at a particular time and place, such as heat, wind or rain
     - example: If the weather is nice tomorrow, let's have a picnic.
     - collocations: weather forecast; weather permitting; in all weathers; bad weather; good weather; hot weather; cold weather; wet weather; the weather changes
- relations: confusable whether (SCONJ): sounds the same as whether, used to talk about a choice between possibilities
**Lexeme 2: weather**, VERB
- grammar: irregular False
- said: /ˈwɛð.ɚ/ (en-US), /ˈweð.ə/ (en-GB)
  1. [C1] to come through a difficult period or situation without serious harm
     - example: Many small shops are struggling to weather the economic crisis.
     - frames: weather sth
     - collocations: weather the storm; weather the crisis; weather a recession
     - grammar: valency Tr
  2. [C1] to change in colour or shape, or make something change, after long exposure to wind, rain and sun
     - example: Over the years the wooden fence has weathered to a soft grey.
     - collocations: weather well; weather badly
     - grammar: valency Both

### 29. worry

**Lexeme 1: worry**, VERB
- grammar: valency Both; irregular False
- said: /ˈwɝ.i/ (en-US), /ˈwʌɹ.i/ (en-US), /ˈwʌɹ.i/ (en-GB)
  1. [A2] to keep thinking about problems or bad things that might happen and feel nervous
     - example: Don't worry, the train will wait for us.
     - frames: worry about sb/sth; worry over sth; worry that ...
     - collocations: don't worry; worry too much; nothing to worry about; worry constantly; worry yourself sick; stop worrying
     - grammar: valency Intr
  2. [B1] to make someone feel nervous and unhappy about what might happen
     - example: It worries me that he hasn't called since Monday.
     - frames: worry sb; it worries sb that ...
     - collocations: deeply worry
     - grammar: valency Tr
  3. [C2] of a dog, to chase and attack an animal, biting and shaking it
     - example: Farmers may shoot dogs that worry their sheep.
     - frames: worry sth
     - collocations: worry sheep; worry livestock
     - grammar: valency Tr
**Lexeme 2: worry**, NOUN
- grammar: countability Both
- said: /ˈwɝ.i/ (en-US), /ˈwʌɹ.i/ (en-US), /ˈwʌɹ.i/ (en-GB)
  1. [B1] the nervous, unhappy feeling of thinking about problems or possible trouble
     - example: Her face was lined with worry after weeks without news.
     - frames: worry about sth; worry over sth
     - collocations: cause for worry; a source of worry; sick with worry; cause worry
     - grammar: countability Mass
  2. [B1] a problem or person that makes you feel nervous and unhappy
     - example: Money is my biggest worry at the moment.
     - frames: worry about sth; worry for sb; worry that ...
     - collocations: money worries; main worry; the least of my worries; no worries; have worries; a major worry
     - grammar: countability Count

### 30. profile

**Lexeme 1: profile**, NOUN
- grammar: countability Count
- said: /ˈpɹoʊ.faɪl/ (en-US), /ˈpɹəʊ.faɪl/ (en-GB)
  1. [B2] a short description of a person or organization, giving the main facts
     - example: The newspaper published a profile of the new mayor.
     - frames: profile of sb/sth
     - collocations: write a profile; detailed profile; company profile; brief profile
  2. [B1] the personal information a person puts about themselves on a website or app
     - example: I finally changed my profile picture after five years.
     - collocations: profile picture; update your profile; create a profile; user profile; online profile; social media profile
  3. [C1] the amount of attention someone or something gets from the public
     - example: The campaign helped to raise the profile of the charity.
     - collocations: raise the profile; keep a low profile; high profile; low profile; public profile
     - grammar: note usually singular
  4. [B2] the shape of a person's face when you look at it from the side
     - example: The photo shows her in profile, looking out of the window.
     - collocations: in profile
  5. [C1] the outline of something seen against a background
     - example: The dark profile of the mountains stood out against the evening sky.
**Lexeme 2: profile**, VERB
- grammar: valency Tr; irregular False
- said: /ˈpɹoʊ.faɪl/ (en-US), /ˈpɹəʊ.faɪl/ (en-GB)
  1. [C1] to write or broadcast a description of a person or organization
     - example: Each week the programme profiles a young local artist.
     - frames: profile sb/sth
  2. [C1] to treat someone as likely to do wrong because of their race or appearance
     - example: Many travellers feel they are being profiled at airport security.
     - frames: profile sb
     - labels: attitude disapproving

### 31. totally

**Lexeme 1: totally**, ADV
- grammar: comparable False
- said: /ˈtoʊ.tə.li/ (en-US), /ˈtəʊ.tə.li/ (en-GB)
  1. [B1] in every way or to the greatest possible degree
     - example: I totally forgot about our meeting this morning.
     - collocations: totally different; totally agree; totally forget; totally unacceptable; totally free; totally new; totally destroyed
     - opposites: partly
  2. [B2] very or extremely
     - example: That concert last night was totally amazing.
     - collocations: totally awesome; totally cool
     - labels: formality informal
     - grammar: note usually before an adjective
  3. [B2] used to say strongly that something is certainly true or that you agree
     - example: You should totally come with us to the beach tomorrow.
     - labels: formality informal
     - grammar: note also used alone as a reply
- relations: adverb_of total (ADJ)

### 32. apart

**Lexeme 1: apart**, ADV
- said: /ə.ˈpɑːɹt/ (en-US), /ə.ˈpɑːt/ (en-GB)
  1. [B1] separated from each other by a distance or a period of time
     - example: Our two houses are only a few hundred metres apart.
     - collocations: far apart; wide apart; miles apart; years apart; further apart; worlds apart; feet apart
     - grammar: comparable True; note usually after a measure of distance or time
  2. [B1] not together with another person or thing; separately
     - example: My parents have been living apart for over a year.
     - frames: apart from sb/sth
     - collocations: live apart; grow apart; drift apart; keep apart; spend time apart; stand apart; set apart
     - opposites: together
  3. [B1] into separate pieces or parts
     - example: The old chair simply fell apart when I sat on it.
     - collocations: fall apart; come apart; take apart; tear apart; pull apart; break apart; rip apart
  4. [C1] used after a noun to say that it is not being included or considered
     - example: Joking apart, I think we have a real problem here.
     - collocations: joking apart; jokes apart; that apart
     - grammar: note always after the noun it refers to
**Lexeme 2: apart**, ADJ
- grammar: position Attr; comparable False; note always after the noun
- said: /ə.ˈpɑːɹt/ (en-US), /ə.ˈpɑːt/ (en-GB)
  1. [C1] very different from all others of the same kind
     - example: As a singer, she was always a class apart.
     - collocations: a race apart; a breed apart; a class apart; a world apart

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
- forms: fatter [Degree=Cmp] /ˈfæt.ɚ/ /ˈfæt.ə/; fattest [Degree=Sup] /ˈfæt.ɪst/ /ˈfæt.ɪst/
  1. [A1] having a large, round body with a lot of extra flesh
     - example: Our cat got fat because everyone kept feeding it.
     - collocations: get fat; grow fat; become fat
     - grammar: note often considered rude when said about a person
     - opposites: thin
  2. [B1] thick or wide, and often very full
     - example: She handed me a fat envelope full of old letters.
     - collocations: a fat book; a fat envelope; a fat wallet; a fat cigar
     - opposites: thin
  3. [B2] large in amount, usually of money
     - example: The company made a fat profit from the deal.
     - collocations: a fat profit; a fat salary; a fat fee; a fat bonus
     - labels: formality informal
     - grammar: position Attr
**Lexeme 2: fat**, NOUN
- grammar: countability Mass
- said: /ˈfæt/ (en-US), /ˈfæt/ (en-GB)
  1. [A2] an oily substance from animals or plants, eaten in food or used for cooking
     - example: Try to eat fewer snacks that are high in fat.
     - collocations: high in fat; low in fat; saturated fat; animal fat; vegetable fat; fry in fat
     - grammar: countability Mass, mainly
  2. [B1] the soft substance under the skin that stores energy in the body
     - example: Regular exercise is a good way to burn fat.
     - collocations: body fat; burn fat; lose fat; a layer of fat
     - grammar: countability Mass
  3. [C1] the parts of an organization or plan that waste money or are not needed
     - example: The new manager promised to cut the fat from the budget.
     - collocations: trim the fat; cut the fat
     - labels: figurative True

### 35. realize

**Lexeme 1: realize**, VERB
- grammar: valency Tr; irregular False
- said: /ˈɹiː.ə.laɪz/ (en-US), /ˈɹɪə.laɪz/ (en-GB), /ˈɹiː.ə.laɪz/ (en-GB, less_common)
- spellings: realize (en-US, en-GB); realise (en-GB, en-AU, en-IN)
  1. [A2] to begin to know or understand a fact you did not notice before
     - example: I didn't realize how late it was until the lights went out.
     - frames: realize that ...; realize sth
     - collocations: suddenly realize; soon realize; fully realize; come to realize; begin to realize; realize your mistake
     - grammar: valency Tr, mainly; note often followed by how, what or why and a clause
  2. [B2] to make something you hoped for or planned actually happen
     - example: She finally realized her dream of opening a small bakery.
     - frames: realize sth
     - collocations: realize a dream; realize an ambition; realize your potential; realize a goal; fully realized
     - grammar: note often passive
  3. [C2] to get an amount of money by selling something you own
     - example: The sale of the old factory realized a large profit.
     - frames: realize sth
     - collocations: realize a profit; realize assets
     - labels: domain finance, formality formal

### 36. mouth

**Lexeme 1: mouth**, NOUN
- grammar: countability Count
- said: /ˈmaʊθ/ (en-US), /ˈmaʊθ/ (en-GB)
- forms: mouths [Number=Plur] /ˈmaʊðz/ /ˈmaʊðz/
  1. [A1] the opening in the face used for eating and speaking
     - example: Please don't talk with your mouth full.
     - collocations: open your mouth; shut your mouth; close your mouth; keep your mouth shut; wipe your mouth; with your mouth full; a big mouth; mouths to feed
  2. [B2] the place where a river flows into the sea or a lake
     - example: The town grew up at the mouth of the river.
     - frames: mouth of sth
     - collocations: river mouth; at the mouth of the river
  3. [B2] the opening or entrance of a cave, bottle, tunnel or similar space
     - example: We waited at the mouth of the cave until the rain stopped.
     - frames: mouth of sth
     - collocations: cave mouth
**Lexeme 2: mouth**, VERB
- grammar: valency Tr; irregular False
- said: /ˈmaʊð/ (en-US), /ˈmaʊð/ (en-GB)
  1. [B2] to move your lips as if saying words, but without making a sound
     - example: She mouthed a quick thank you from across the room.
     - frames: mouth sth; mouth sth to sb
     - collocations: mouth the words
  2. [C1] to say things you do not really believe or mean
     - example: Politicians often mouth fine words about helping the poor.
     - frames: mouth sth
     - collocations: mouth platitudes; mouth slogans
     - labels: attitude disapproving

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
  1. [B1] a material that is burned or used up to produce heat or power
     - example: The plane had to land early because it was running out of fuel.
     - collocations: run out of fuel; fossil fuels; fuel prices; fuel tank; fuel consumption; save fuel; fuel bill; nuclear fuel
  2. [C1] something that makes a feeling, argument or activity stronger or more intense
     - example: His angry reply only gave more fuel to the rumours.
     - frames: fuel for sth
     - collocations: add fuel to the fire; add fuel to the flames; provide fuel for
     - labels: figurative True
     - grammar: countability Mass
**Lexeme 2: fuel**, VERB
- grammar: valency Tr; irregular False
- said: /ˈfjuː.əl/ (en-US), /ˈfjuːl/ (en-US), /ˈfjuː.əl/ (en-GB), /ˈfjuːl/ (en-GB)
- forms: fuelled [Tense=Past, VerbForm=Fin,Part] /ˈfjuː.əld/ /ˈfjuː.əld/ (en-GB, en-AU, en-IN); fueled [Tense=Past, VerbForm=Fin,Part] /ˈfjuː.əld/ /ˈfjuː.əld/ (en-US); fuelling [Tense=Pres, VerbForm=Part] /ˈfjuː.ə.lɪŋ/ /ˈfjuː.ə.lɪŋ/ (en-GB, en-AU, en-IN); fueling [Tense=Pres, VerbForm=Part] /ˈfjuː.ə.lɪŋ/ /ˈfjuː.ə.lɪŋ/ (en-US)
  1. [C1] to make a feeling, situation or process stronger, bigger or worse
     - example: Rising food prices are fuelling anger across the country.
     - frames: fuel sth
     - collocations: fuel speculation; fuel fears; fuel growth; fuel inflation; fuel demand; fuel anger; fuel rumours; fuel the debate
  2. [B2] to supply a vehicle or machine with a material that it burns for power
     - example: The ships are fuelled before they leave the harbour.
     - frames: fuel sth; be fuelled by sth
     - collocations: fuelled by gas; fuelled by coal
- relations: derived_from fuel (NOUN)

### 39. lie

**Lexeme 1: lie**, VERB
- grammar: valency Intr; irregular True
- said: /ˈlaɪ/ (en-US), /ˈlaɪ/ (en-GB)
- forms: lay [Tense=Past, VerbForm=Fin] /ˈleɪ/ /ˈleɪ/; lain [Tense=Past, VerbForm=Part] /ˈleɪn/ /ˈleɪn/; lying [Tense=Pres, VerbForm=Part] /ˈlaɪ.ɪŋ/ /ˈlaɪ.ɪŋ/
  1. [A2] to be in a flat position on a surface, or to put your body in that position
     - example: I like to lie on the grass and watch the clouds.
     - frames: lie on sth; lie in sth
     - collocations: lie in bed; lie awake; lie still; lie flat; lie on your back; lie in the sun
  2. [B1] to be in a particular place or position
     - example: The village lies in a narrow valley between two hills.
     - frames: lie in sth; lie between sth
     - collocations: lie ahead; lie to the north; lie beyond; lie off the coast
  3. [B2] to be or stay in a particular condition for some time
     - example: The old factory has lain empty for years.
     - frames: lie + adj
     - collocations: lie empty; lie idle; lie dormant; lie open; lie in ruins; lie unused
  4. [B2] to exist or be found in something, or to be someone's responsibility
     - example: The real problem lies in the way the system was designed.
     - frames: lie in sth; lie with sb/sth
     - collocations: the problem lies in; the answer lies in; the fault lies with; responsibility lies with
- relations: confusable lay (VERB): lay takes an object (lay something down) and its past is laid; lay is also the past of this verb
**Lexeme 2: lie**, VERB
- grammar: valency Intr; irregular False
- said: /ˈlaɪ/ (en-US), /ˈlaɪ/ (en-GB)
- forms: lied [Tense=Past, VerbForm=Fin,Part] /ˈlaɪd/ /ˈlaɪd/; lying [Tense=Pres, VerbForm=Part] /ˈlaɪ.ɪŋ/ /ˈlaɪ.ɪŋ/
  1. [A2] to say something you know is not true in order to deceive
     - example: He lied to his parents about where he had been.
     - frames: lie to sb; lie about sth
     - collocations: lie through your teeth; lie under oath; lie convincingly; blatantly lie
  2. [B2] to give a false idea of what is real or true
     - example: Photos can lie, so don't believe everything you see online.
     - collocations: the camera never lies; numbers don't lie
     - labels: figurative True
- relations: confusable lie (VERB): the verb meaning to rest flat has the past forms lay and lain, not lied
**Lexeme 3: lie**, NOUN
- grammar: countability Count
- said: /ˈlaɪ/ (en-US), /ˈlaɪ/ (en-GB)
  1. [A2] something you say that you know is not true, in order to deceive
     - example: Telling one small lie often leads to telling many more.
     - collocations: tell a lie; a white lie; a pack of lies; a big lie; a downright lie; live a lie; give the lie to
     - opposites: truth
  2. [C2] the way or position in which something rests, especially a golf ball
     - example: After a poor shot, her ball had a difficult lie in the long grass.
     - collocations: the lie of the land
     - grammar: countability Count; note the lie of the land is mainly British

### 40. murder

**Lexeme 1: murder**, NOUN
- grammar: countability Both
- said: /ˈmɝ.dɚ/ (en-US), /ˈmɜː.də/ (en-GB)
  1. [B1] the crime of killing a person deliberately and illegally
     - example: Police are still searching for the weapon used in the murder.
     - frames: murder of sb
     - collocations: commit murder; attempted murder; charged with murder; accused of murder; murder investigation; murder case; murder trial; murder weapon; mass murder; get away with murder
  2. [B2] something very difficult, tiring or unpleasant to deal with
     - example: Driving through the city centre at rush hour is murder.
     - frames: murder on sth
     - labels: formality informal
     - grammar: countability Mass; note usually after "be"
**Lexeme 2: murder**, VERB
- grammar: valency Tr; irregular False
- said: /ˈmɝ.dɚ/ (en-US), /ˈmɜː.də/ (en-GB)
  1. [B1] to kill a person deliberately and illegally
     - example: He was arrested for murdering his business partner.
     - frames: murder sb
     - collocations: brutally murdered; murder in cold blood
  2. [C1] to defeat someone completely in a game or competition
     - example: Our team got murdered in the final last weekend.
     - frames: murder sb
     - labels: formality informal, figurative True
  3. [C1] to spoil a song, piece of music or language by doing it very badly
     - example: The band murdered every song they tried to play.
     - frames: murder sth
     - labels: formality informal, figurative True
  4. [C1] to eat or drink something with great eagerness
     - example: I could murder a cup of tea right now.
     - frames: could murder sth
     - labels: formality informal, figurative True
     - regions: en-GB mainly

### 41. status

**Lexeme 1: status**, NOUN
- grammar: countability Both
- said: /ˈsteɪ.təs/ (en-US), /ˈstæ.təs/ (en-US), /ˈsteɪ.təs/ (en-GB)
  1. [B2] a person's social or professional position compared with other people
     - example: Nurses deserve a higher status in our society.
     - frames: status as sth
     - collocations: social status; high status; low status; equal status; raise the status
  2. [B2] the official position of a person, group or place under the law
     - example: Please state your marital status on the form.
     - frames: status as sth
     - collocations: legal status; marital status; refugee status; official status; grant status; immigration status
  3. [B2] the respect and importance that come with a high position
     - example: For many people, an expensive car is a sign of status.
     - collocations: status symbol; celebrity status; cult status; gain status; achieve status
     - grammar: countability Mass
  4. [B2] the situation or progress of something at a particular time
     - example: You can check the status of your order online.
     - frames: status of sth
     - collocations: current status; check the status; status report; status update
     - grammar: countability Count, mainly; note usually singular
  5. [B2] a short message on a social media profile saying what you are doing or feeling
     - example: She changed her status to say she was on holiday.
     - collocations: update your status; post a status; status update
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
  1. [A2] a journey in a plane, or a plane making a planned journey
     - example: Our flight to the capital was delayed by two hours.
     - frames: flight to sth; flight from sth
     - collocations: book a flight; catch a flight; miss a flight; direct flight; connecting flight; long-haul flight; domestic flight; flight attendant
     - grammar: countability Count
  2. [B1] the act of moving through the air, or the ability to do this
     - example: The young birds are not yet strong enough for flight.
     - collocations: in flight; take flight; the history of flight
     - grammar: countability Mass, mainly
  3. [B1] a set of stairs going from one floor or level to the next
     - example: She climbed three flights of stairs to reach her flat.
     - frames: flight of sth
     - collocations: a flight of stairs; a flight of steps
     - grammar: countability Count
  4. [C1] the act of running away from danger or a difficult situation
     - example: Their flight from the war took them across three borders.
     - frames: flight from sth
     - collocations: take flight; in flight
     - grammar: countability Mass
  5. [C1] an idea or plan that is imaginative but not realistic
     - example: Sailing around the world was just another flight of fancy.
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
- grammar: irregular False
- said: /ˈɡeɪn/ (en-US), /ˈɡeɪn/ (en-GB)
  1. [B1] to get something useful or wanted, often slowly or through effort
     - example: She gained a lot of experience working abroad.
     - frames: gain sth; gain sth from sth
     - collocations: gain experience; gain access; gain control; gain confidence; gain popularity; gain support; gain independence; gain an advantage; gain ground
     - grammar: valency Tr
     - opposites: lose
  2. [B1] to get more of something measurable, so that it increases
     - example: I gained three kilos over the holidays.
     - frames: gain sth
     - collocations: gain weight; gain speed; gain momentum; gain height; gain in value
     - grammar: valency Both
     - opposites: lose
  3. [B2] to get an advantage or profit from a situation or action
     - example: Nobody really gains from a long war.
     - frames: gain from sth; gain by doing sth
     - grammar: valency Intr
  4. [C1] to come closer to someone or something you are chasing
     - example: The other boat was gaining on us fast.
     - frames: gain on sb/sth
     - grammar: valency Intr
  5. [C2] to reach a place, especially after a lot of effort
     - example: After hours of climbing, they finally gained the summit.
     - frames: gain sth
     - labels: style literary
     - grammar: valency Tr
**Lexeme 2: gain**, NOUN
- grammar: countability Both
- said: /ˈɡeɪn/ (en-US), /ˈɡeɪn/ (en-GB)
  1. [B2] an increase in amount, size, value or level
     - example: Rapid weight gain can be a sign of illness.
     - frames: gain in sth
     - collocations: weight gain; make gains; a significant gain; a net gain; gains in productivity
     - grammar: countability Count
     - opposites: loss
  2. [B2] an advantage or profit, especially money, that you get from something
     - example: He only helped them for his own personal gain.
     - collocations: personal gain; financial gain; for gain; capital gains; ill-gotten gains; short-term gain
     - grammar: countability Both
     - opposites: loss
