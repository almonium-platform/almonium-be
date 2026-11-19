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
  1. [A2] a flat shape with four equal sides and four right angles, or a piece shaped like this
     - example: Cut the cake into squares before you serve it.
     - collocations: cut into squares; a square of chocolate; a square of cloth; draw a square
  2. [A2] an open area in a town with buildings all around it
     - example: The market is held in the main square every Saturday.
     - collocations: main square; town square; market square; village square; central square
     - grammar: note often part of a place name
  3. [B1] one of the spaces on a board used for playing a game
     - example: Move your piece forward three squares.
     - collocations: black square; white square; back to square one
  4. [B2] the number you get when you multiply a number by itself
     - example: Nine is the square of three.
     - frames: the square of sth
     - collocations: perfect square
     - labels: domain mathematics
  5. [C2] a person who is boring and follows old-fashioned ideas
     - example: My friends thought I was a square because I never went out.
     - labels: formality informal, time dated
**Lexeme 2: square**, ADJ
- grammar: comparable True
- said: /ˈskwɛɹ/ (en-US), /ˈskweə/ (en-GB)
  1. [A2] having four straight equal sides and four right angles
     - example: We bought a small square table for the kitchen.
     - collocations: square table; square box; square window
  2. [B2] forming a right angle, or broad with straight edges and corners
     - example: He has a square jaw and very short hair.
     - collocations: square jaw; square shoulders; square corners
  3. [B1] used before a unit of length to make a unit measuring area
     - example: The flat has about eighty square metres of floor space.
     - collocations: square metre; square kilometre; square mile; square foot
     - grammar: position Attr, mainly; comparable False; note after a measurement it gives the length of each side: two metres square
  4. [C2] fair and honest in the way people are treated
     - example: All we want is a square deal from the company.
     - collocations: a square deal
     - labels: formality informal
  5. [C1] no longer owing money, or having the same score in a game
     - example: Here's the money I borrowed, so now we're square.
     - frames: square with sb
     - collocations: all square; call it square
     - labels: formality informal
     - grammar: position Pred; comparable False
  6. [C2] boring and following old-fashioned ideas
     - example: Don't be so square, everyone dresses like this now.
     - labels: formality informal, time dated
**Lexeme 3: square**, VERB
- grammar: irregular False
- said: /ˈskwɛɹ/ (en-US), /ˈskweə/ (en-GB)
  1. [B2] to multiply a number by itself
     - example: If you square four, you get sixteen.
     - frames: square sth
     - labels: domain mathematics
     - grammar: valency Tr
  2. [C1] to agree with something, or to make two things agree
     - example: His story doesn't square with what the witnesses saw.
     - frames: square with sth; square sth with sth
     - grammar: valency Both
  3. [C1] to make the scores in a game or a series equal
     - example: A late goal squared the match at two each.
     - frames: square sth
     - collocations: square the match; square the series
     - regions: en-GB mainly
     - grammar: valency Tr
  4. [C1] to pull your shoulders back and hold them straight, ready to face something
     - example: She squared her shoulders and walked into the meeting.
     - collocations: square your shoulders
     - grammar: valency Tr
**Lexeme 4: square**, ADV
- grammar: comparable False
- said: /ˈskwɛɹ/ (en-US), /ˈskweə/ (en-GB)
  1. [C2] directly and firmly, not at an angle or to one side
     - example: The ball hit him square in the face.
     - collocations: square in the face; square on the jaw; fair and square
     - labels: formality informal

### 2. reaction

**Lexeme 1: reaction**, NOUN
- said: /ɹi.ˈæk.ʃən/ (en-US), /ɹi.ˈæk.ʃən/ (en-GB)
  1. [B1] what you feel, say or do because of something that has happened
     - example: My first reaction was to laugh, but then I felt sad.
     - frames: reaction to sth; reaction from sb
     - collocations: first reaction; initial reaction; immediate reaction; mixed reaction; gut reaction; knee-jerk reaction; provoke a reaction; get a reaction
     - grammar: countability Count
  2. [B2] an illness or bad effect on the body caused by a food, drug or substance
     - example: Some patients have a bad reaction to this medicine.
     - frames: reaction to sth
     - collocations: allergic reaction; severe reaction; have a reaction; suffer a reaction
     - grammar: countability Count
  3. [B2] the ability to move quickly when something suddenly happens
     - example: Racing drivers need very fast reactions.
     - collocations: quick reactions; fast reactions; slow reactions; reaction time
     - grammar: countability Count; number Plur
  4. [B2] a process in which substances change when they are mixed or combined
     - example: Heat speeds up the reaction between the two liquids.
     - frames: reaction between sth and sth; reaction with sth
     - collocations: chemical reaction; nuclear reaction; chain reaction; cause a reaction
     - labels: domain chemistry
     - grammar: countability Count
  5. [C1] a change in opinion or behaviour towards the opposite of what came before
     - example: The new fashion was a reaction against the strict styles of the past.
     - frames: reaction against sth
     - grammar: countability Both
- relations: verbal_noun_of react (VERB)

### 3. winner

**Lexeme 1: winner**, NOUN
- grammar: countability Mass
- said: /ˈwɪn.ɚ/ (en-US), /ˈwɪn.ə/ (en-GB)
  1. [A2] a person, team or animal that comes first in a competition, race or game
     - example: The winners will be announced at the end of the evening.
     - frames: winner of sth
     - collocations: declare the winner; announce the winner; pick a winner; prize winner; overall winner; clear winner; worthy winner; outright winner; Nobel Prize winner
     - opposites: loser
  2. [B2] something or someone that is successful or likely to be successful
     - example: Your new recipe is a real winner with the kids.
     - frames: winner with sb
     - collocations: a real winner; be onto a winner; be on to a winner
     - labels: formality informal
  3. [B2] a goal or point that makes a team or player win a game
     - example: He scored the winner in the last minute of the match.
     - collocations: score the winner; late winner; last-minute winner
     - regions: en-GB mainly
- relations: agent_noun_of win (VERB)

### 4. mad

**Lexeme 1: mad**, ADJ
- grammar: comparable True
- said: /ˈmæd/ (en-US), /ˈmæd/ (en-GB)
  1. [A2] very angry
     - example: My parents will be mad at me for coming home late.
     - frames: mad at sb; mad about sth; mad with sb
     - collocations: get mad; hopping mad; go mad; boiling mad
     - labels: formality informal
     - regions: en-US mainly
     - grammar: position Pred, mainly; note mad with sb is mainly British
  2. [B1] very silly, not sensible, or likely to be dangerous
     - example: You'd be mad to turn down a job like that.
     - frames: mad to do sth
     - collocations: a mad idea; must be mad; completely mad
     - labels: formality informal
     - regions: en-GB mainly
  3. [B1] having a serious mental illness
     - example: In the novel, the old king slowly goes mad.
     - collocations: go mad; stark raving mad; a mad scientist
     - labels: time dated
     - opposites: sane
  4. [B1] liking someone or something very much and very enthusiastic about them
     - example: Her little brother is absolutely mad about dinosaurs.
     - frames: mad about sb/sth; mad on sth
     - collocations: football-mad; mad keen
     - labels: formality informal
     - regions: en-GB mainly
     - grammar: position Pred
  5. [B2] wild, hurried and not controlled
     - example: There was a mad rush for the last few tickets.
     - collocations: a mad rush; a mad dash; like mad; mad with grief
**Lexeme 2: mad**, ADV
- said: /ˈmæd/ (en-US), /ˈmæd/ (en-GB)
  1. [C1] very or extremely
     - example: That concert was mad expensive, but it was worth it.
     - labels: formality slang
     - regions: en-US mainly

### 5. sad

**Lexeme 1: sad**, ADJ
- grammar: comparable True
- said: /ˈsæd/ (en-US), /ˈsæd/ (en-GB)
  1. [A1] unhappy or sorry because of something that has happened
     - example: I felt really sad when my best friend moved away.
     - frames: sad about sth; sad to do sth; sad that ...
     - collocations: feel sad; look sad; deeply sad; sad to see; sad to hear
     - opposites: happy
  2. [A2] making people feel unhappy or sorry
     - example: It's a sad film, so bring some tissues.
     - collocations: sad news; sad story; sad day; sad song; sad occasion
  3. [B2] very bad and deserving disapproval or regret
     - example: Our schools are in a sad state after years of cuts.
     - collocations: a sad state of affairs; the sad truth; a sad fact; sad to say
     - grammar: position Attr, mainly
  4. [B2] boring, unfashionable or deserving to be laughed at
     - example: Staying in to watch quiz shows on Saturday night is so sad.
     - labels: formality informal, attitude disapproving
     - regions: en-GB mainly

### 6. file

**Lexeme 1: file**, NOUN
- grammar: countability Count
- said: /ˈfaɪl/ (en-US), /ˈfaɪl/ (en-GB)
  1. [A2] a collection of information stored together under one name on a computer
     - example: Don't forget to save the file before you close the program.
     - collocations: open a file; save a file; delete a file; attach a file; download a file; copy a file; file size
  2. [B1] a folder or box for keeping loose papers together and in order
     - example: All the receipts are kept in a blue file on my desk.
     - collocations: box file; ring file
  3. [B2] a collection of documents and information about a particular person or subject
     - example: The police have kept a file on him for years.
     - frames: file on sb/sth
     - collocations: on file; keep a file; open a file; close a file; police file; medical file
  4. [C1] a metal tool with a rough surface for making things smooth or shaping them
     - example: She used a small file to smooth the edges of the key.
     - collocations: nail file
  5. [C1] a line of people or things, one behind the other
     - example: The children walked to the bus in single file.
     - collocations: in single file; single file
**Lexeme 2: file**, VERB
- grammar: valency Both; irregular False
- said: /ˈfaɪl/ (en-US), /ˈfaɪl/ (en-GB)
  1. [B2] to officially give a document or claim to a court or other authority
     - example: Several customers have filed complaints against the company.
     - frames: file sth; file for sth; file sth against sb
     - collocations: file a complaint; file a lawsuit; file for divorce; file for bankruptcy; file a claim; file charges; file a tax return
     - grammar: valency Tr, mainly
  2. [B2] to put a document with others in a particular place so it can be found easily
     - example: I file all my bills in a drawer by date.
     - frames: file sth; file sth under sth
     - collocations: file documents; file papers; file alphabetically
     - grammar: valency Tr
  3. [C1] to walk in a line, one behind another
     - example: After the meeting, everyone filed slowly out of the hall.
     - frames: file into sth; file out of sth; file past sth
     - grammar: valency Intr
  4. [C1] to make something smooth or change its shape with a metal tool
     - example: She sat on the sofa filing her nails.
     - frames: file sth
     - collocations: file your nails
     - grammar: valency Tr
  5. [C1] to send a news report or story to a newspaper or broadcaster
     - example: Our reporter filed the story just before midnight.
     - frames: file sth
     - collocations: file a report; file a story
     - grammar: valency Tr

### 7. lucky

**Lexeme 1: lucky**, ADJ
- grammar: comparable True
- said: /lʌk.ˈi/ (en-US), /lʌk.ˈi/ (en-GB)
  1. [A2] having good things happen to you by chance
     - example: You were lucky to find a seat on such a busy train.
     - frames: lucky to do sth; lucky that ...; lucky with sth
     - collocations: count yourself lucky; get lucky; lucky enough; third time lucky; extremely lucky
     - opposites: unlucky
  2. [B1] happening by chance and having a good result
     - example: It was only a lucky guess, but I got the answer right.
     - frames: it is lucky that ...
     - collocations: lucky guess; lucky escape; lucky break; lucky shot; lucky coincidence
     - opposites: unlucky
  3. [B1] believed to bring good fortune or success
     - example: My grandfather always wore his lucky hat to football matches.
     - collocations: lucky charm; lucky number; lucky day; lucky shirt
     - grammar: position Attr
     - opposites: unlucky
- relations: adjective_of luck (NOUN)

### 8. northern

**Lexeme 1: northern**, ADJ
- grammar: position Attr; comparable False
- said: /ˈnɔːɹ.ðɚn/ (en-US), /ˈnɔː.ðən/ (en-GB)
  1. [A2] in the north part of a place, or facing the north
     - example: Snow often falls early in the northern part of the country.
     - collocations: northern hemisphere; northern Europe; northern border; northern edge; northern lights
     - opposites: southern
  2. [B1] coming from or typical of the north of a country or the world
     - example: He still speaks with a strong northern accent.
     - collocations: northern accent; northern town; northern cities
     - opposites: southern
- relations: derived_from north (NOUN)

### 9. vehicle

**Lexeme 1: vehicle**, NOUN
- grammar: countability Count
- said: /ˈviː.ə.kəl/ (en-US), /ˈviː.hɪ.kəl/ (en-US, less_common), /ˈviː.ə.kəl/ (en-GB), /ˈviː.ɪ.kəl/ (en-GB)
  1. [B1] a machine that carries people or goods from place to place, usually on roads
     - example: Please do not leave your vehicle in front of the gate.
     - collocations: motor vehicle; commercial vehicle; electric vehicle; military vehicle; drive a vehicle; park a vehicle; emergency vehicle; vehicle registration
  2. [C1] a means of expressing ideas or of achieving a particular aim
     - example: For him, the newspaper was a vehicle for his political views.
     - frames: vehicle for sth; vehicle for doing sth
     - collocations: vehicle for change; a vehicle of expression
  3. [C2] a film, play or song made to show the skills of a particular performer
     - example: The musical was written as a vehicle for its young star.
     - frames: vehicle for sb
     - collocations: star vehicle

### 10. smart

**Lexeme 1: smart**, ADJ
- grammar: comparable True
- said: /ˈsmɑːɹt/ (en-US), /ˈsmɑːt/ (en-GB)
  1. [A2] intelligent, or showing good judgement and quick thinking
     - example: Hiring a good lawyer early was a really smart move.
     - collocations: smart move; smart kid; smart idea; smart choice; street smart
     - regions: en-021 mainly
     - opposites: stupid
  2. [A2] clean, tidy and dressed in good clothes; looking neat and attractive
     - example: You look very smart in that new suit.
     - collocations: look smart; smart suit; smart clothes; smart casual
     - regions: en-GB mainly
     - opposites: scruffy
  3. [B1] fashionable and expensive, and used by rich people
     - example: They had dinner at a smart restaurant near the river.
     - collocations: smart restaurant; smart hotel; smart area; smart set
     - regions: en-GB mainly
  4. [B1] controlled by a computer and able to act in an intelligent way
     - example: Our new smart meter shows how much electricity we use.
     - collocations: smart card; smart meter; smart home; smart TV; smart device; smart watch
     - grammar: position Attr; comparable False
  5. [B2] rude to someone by answering them in a clever way
     - example: Don't get smart with me, young man!
     - frames: get smart with sb
     - collocations: smart remark; smart answer
     - labels: formality informal, attitude disapproving
  6. [C1] quick and done with energy or force
     - example: We set off at a smart pace towards the hills.
     - collocations: at a smart pace; a smart blow
     - grammar: position Attr
**Lexeme 2: smart**, VERB
- grammar: valency Intr; irregular False
- said: /ˈsmɑːɹt/ (en-US), /ˈsmɑːt/ (en-GB)
  1. [C1] to feel or cause a sharp stinging pain
     - example: The smoke made my eyes smart.
     - frames: smart from sth
     - collocations: eyes smart
  2. [C1] to feel upset or offended because of something that happened
     - example: The team is still smarting from last week's heavy defeat.
     - frames: smart from sth; smart over sth; smart at sth
     - labels: figurative True
     - grammar: note usually in the progressive

### 11. balance

**Lexeme 1: balance**, NOUN
- grammar: countability Both
- said: /bæl.ˈəns/ (en-US), /bæl.ˈəns/ (en-GB)
  1. [B1] the ability to stay steady and upright without falling
     - example: She slipped on the ice and lost her balance.
     - collocations: lose your balance; keep your balance; sense of balance; off balance; regain your balance
     - grammar: countability Mass
  2. [B2] a state in which different things have the right amount or importance
     - example: It's hard to find a good balance between work and family.
     - frames: balance between sth and sth; balance of sth
     - collocations: strike a balance; the right balance; work-life balance; balance of power; a healthy balance; upset the balance
     - grammar: note usually singular
     - opposites: imbalance
  3. [B1] the amount of money in a bank account at a particular time
     - example: I check my balance online before I go shopping.
     - collocations: bank balance; account balance; check your balance; a negative balance
     - labels: domain finance
     - grammar: countability Count
  4. [B2] the amount of money still owed after part has been paid
     - example: You pay a deposit now and the balance next month.
     - collocations: pay the balance; the outstanding balance; the remaining balance
     - grammar: number Sing
  5. [C1] an instrument for weighing things, with two dishes hanging from a bar
     - example: The chemist placed the powder carefully on the balance.
     - grammar: countability Count
**Lexeme 2: balance**, VERB
- grammar: valency Both; irregular False
- said: /ˈbæl.əns/ (en-US), /ˈbæl.əns/ (en-GB)
  1. [B1] to stay or keep something steady without it falling
     - example: The waiter balanced three plates on one arm.
     - frames: balance on sth; balance sth on sth
     - collocations: balance on one leg; balance carefully; balance precariously
  2. [B2] to give two or more things equal or fair importance
     - example: Many parents struggle to balance their jobs and their children.
     - frames: balance sth with sth; balance sth against sth; balance sth and sth
     - collocations: balance the needs; balance the interests; carefully balanced
     - grammar: valency Tr
  3. [C1] to make the money spent equal to the money received
     - example: The government has promised to balance the budget by next year.
     - collocations: balance the budget; balance the books; balance the accounts
     - labels: domain finance
     - grammar: valency Tr, mainly

### 12. influence

**Lexeme 1: influence**, NOUN
- grammar: countability Both
- said: /ˈɪn.flu.əns/ (en-US), /ˈɪn.flu.əns/ (en-GB)
  1. [B1] the power to change how someone thinks or behaves, or how something develops
     - example: Parents still have a lot of influence over their teenage children.
     - frames: influence on sb/sth; influence over sb/sth; under the influence of sb/sth
     - collocations: have an influence on; exert influence; use your influence; strong influence; political influence; under the influence; sphere of influence; come under the influence of
     - grammar: countability Mass, mainly
  2. [B2] a person or thing that changes how someone thinks, behaves or develops
     - example: My grandmother was a major influence on my choice of career.
     - frames: influence on sb/sth
     - collocations: a good influence; a bad influence; a major influence; a positive influence; an early influence
     - grammar: countability Count
**Lexeme 2: influence**, VERB
- grammar: valency Tr; irregular False
- said: /ˈɪn.flu.əns/ (en-US), /ˈɪn.flu.əns/ (en-GB)
  1. [B1] to change how someone thinks or behaves, or how something develops
     - example: The weather can strongly influence how people feel each day.
     - frames: influence sb/sth; influence sb to do sth
     - collocations: strongly influence; heavily influence; greatly influence; influence a decision; influence the outcome; be influenced by

### 13. trial

**Lexeme 1: trial**, NOUN
- grammar: countability Both
- said: /ˈtɹaɪ.əl/ (en-US), /ˈtɹaɪl/ (en-US, casual), /ˈtɹaɪ.əl/ (en-GB)
  1. [B1] a formal process in a court where a judge or jury decides a case
     - example: The two men will go on trial next month for robbery.
     - frames: trial for sth; bring sb to trial
     - collocations: stand trial; go on trial; on trial; fair trial; trial by jury; murder trial; face trial
     - grammar: countability Both
  2. [B2] a test of how good, safe or useful something or someone is
     - example: The new drug is still being tested in clinical trials.
     - frames: trial of sth
     - collocations: clinical trial; trial and error; trial period; trial run; on a trial basis; on trial; free trial; carry out a trial
     - grammar: countability Count, mainly
  3. [C1] a competition in which people are tested to choose who joins a team
     - example: She ran her best time ever at the national trials.
     - collocations: Olympic trials; have a trial
     - labels: domain sport
     - grammar: countability Count; note often plural
  4. [C1] a person or thing that is annoying or difficult to deal with
     - example: My little brother can be a real trial at times.
     - frames: trial to sb
     - collocations: trials and tribulations
     - grammar: countability Count
- relations: confusable trail (NOUN): trail, a path or a track, has nearly the same letters
**Lexeme 2: trial**, VERB
- grammar: valency Tr; irregular False
- said: /ˈtɹaɪ.əl/ (en-US), /ˈtɹaɪ.əl/ (en-GB)
- forms: trialled [Tense=Past, VerbForm=Fin,Part] /ˈtɹaɪ.əld/ /ˈtɹaɪ.əld/ (en-GB, en-AU, en-IN); trialed [Tense=Past, VerbForm=Fin,Part] /ˈtɹaɪ.əld/ /ˈtɹaɪ.əld/ (en-US); trialling [Tense=Pres, VerbForm=Part] /ˈtɹaɪ.ə.lɪŋ/ /ˈtɹaɪ.ə.lɪŋ/ (en-GB, en-AU, en-IN); trialing [Tense=Pres, VerbForm=Part] /ˈtɹaɪ.ə.lɪŋ/ /ˈtɹaɪ.ə.lɪŋ/ (en-US)
  1. [C1] to test something new to find out how well it works
     - example: Several schools are trialling a shorter working week.
     - frames: trial sth
     - collocations: trial a scheme; trial a new system
     - regions: en-GB mainly
- relations: derived_from trial (NOUN)

### 14. slightly

**Lexeme 1: slightly**, ADV
- said: /ˈslaɪt.li/ (en-US), /ˈslaɪt.li/ (en-GB)
  1. [B1] a little; not very much
     - example: The new version is slightly cheaper than the old one.
     - collocations: slightly different; only slightly; slightly better; slightly more; slightly less; slightly higher; ever so slightly; slightly worried
  2. [C1] in a way that is thin and small in body
     - example: He was slightly built, with narrow shoulders and thin arms.
     - collocations: slightly built
     - grammar: note usually in slightly built
- relations: adverb_of slight (ADJ)

### 15. foot

**Lexeme 1: foot**, NOUN
- grammar: countability Count; irregular True
- said: /ˈfʊt/ (en-US), /ˈfʊt/ (en-GB)
- forms: feet [Number=Plur] /ˈfiːt/ /ˈfiːt/; foot [Number=Plur] /ˈfʊt/ /ˈfʊt/ (informal, mostly in heights: six foot two)
  1. [A1] the part of the body at the end of the leg, on which you stand
     - example: My feet hurt after walking around the city all day.
     - collocations: on foot; set foot in; get to your feet; on your feet; stamp your foot; bare feet; sore feet; from head to foot
  2. [A2] a unit for measuring length, equal to twelve inches or about 30 centimetres
     - example: The fence around the garden is about six feet high.
     - collocations: square foot; cubic foot; six feet tall
     - grammar: note often shortened to ft
  3. [B1] the lowest part or bottom end of something
     - example: Leave your shoes at the foot of the stairs, please.
     - frames: at the foot of sth
     - collocations: at the foot of the stairs; at the foot of the hill; at the foot of the page; the foot of the bed
     - grammar: number Sing
**Lexeme 2: foot**, VERB
- grammar: valency Tr; irregular False
- said: /ˈfʊt/ (en-US), /ˈfʊt/ (en-GB)
  1. [C1] to pay for something, usually something expensive
     - example: Taxpayers will have to foot the bill for the new stadium.
     - collocations: foot the bill
     - labels: formality informal
     - grammar: note almost always in foot the bill

### 16. traditional

**Lexeme 1: traditional**, ADJ
- grammar: comparable True
- said: /tɹə.ˈdɪʃ.ən.əl/ (en-US), /tɹə.ˈdɪʃ.nəl/ (en-US), /tɹə.ˈdɪʃ.ən.əl/ (en-GB), /tɹə.ˈdɪʃ.nəl/ (en-GB)
  1. [A2] following customs or ways of doing things that have existed for a long time
     - example: We ate traditional food at a small village restaurant.
     - collocations: traditional dress; traditional costume; traditional music; traditional medicine; traditional methods; traditional way; traditional values
  2. [B2] preferring older ideas and ways of behaving and not liking change
     - example: My grandparents are rather traditional about marriage and family life.
     - frames: traditional about sth
     - collocations: traditional views; traditional attitudes
- relations: adjective_of tradition (NOUN)

### 17. score

**Lexeme 1: score**, NOUN
- grammar: countability Mass
- said: /ˈskɔːɹ/ (en-US), /ˈskɔː/ (en-GB)
  1. [A2] the number of points or goals each player or team has in a game
     - example: The final score was three goals to one.
     - collocations: keep score; the final score; the half-time score; level the score; the score is tied
  2. [B1] the number of marks or points someone gets in a test or exam
     - example: Her test scores have improved a lot this term.
     - frames: score on sth; score in sth
     - collocations: a high score; a low score; a perfect score; test score; get a score
  3. [C1] the written music for a piece, or the music written for a film or play
     - example: The film's score was written by a famous composer.
     - collocations: film score; musical score; write the score
     - labels: domain music
  4. [C1] a very large number of people or things
     - example: Scores of people were waiting outside the stadium.
     - frames: scores of sb/sth
     - grammar: number Plur
  5. [C2] a group or set of twenty
     - example: The old man had lived for four score years.
     - labels: time dated, style literary
     - grammar: note plural is usually score after a number: three score
**Lexeme 2: score**, VERB
- grammar: valency Both; irregular False
- said: /ˈskɔːɹ/ (en-US), /ˈskɔː/ (en-GB)
  1. [A2] to win a point or goal in a game or sport
     - example: He scored twice in the second half of the match.
     - frames: score sth; score against sb
     - collocations: score a goal; score points; score a try; score a run; score a winner
  2. [B1] to get a particular number of marks or points in a test
     - example: Most students scored above eighty per cent on the exam.
     - frames: score sth; score sth on sth; score + adv
     - collocations: score highly; score well; score poorly; score low
  3. [B2] to achieve a success or gain an advantage
     - example: The new album has scored a huge hit with teenagers.
     - frames: score sth; score points off sb
     - collocations: score a victory; score a success; score a hit
     - grammar: valency Tr
  4. [B2] to manage to get something you want, often by luck
     - example: I scored two free tickets to tonight's concert.
     - frames: score sth
     - labels: formality informal
     - grammar: valency Tr
  5. [C1] to cut a line or lines into the surface of something
     - example: Score the bread dough lightly with a sharp knife.
     - frames: score sth
     - grammar: valency Tr
  6. [C2] to write music for a film, play or group of instruments
     - example: She was asked to score a new documentary about the ocean.
     - frames: score sth; score sth for sth
     - labels: domain music
     - grammar: valency Tr

### 18. estate

**Lexeme 1: estate**, NOUN
- grammar: countability Count
- said: /ɪ.ˈsteɪt/ (en-US), /ə.ˈsteɪt/ (en-US), /ɪ.ˈsteɪt/ (en-GB)
  1. [B2] a large area of land, usually in the countryside, owned by one person, family or company
     - example: The family has owned the estate and its big house for centuries.
     - collocations: country estate; family estate; tea estate; coffee estate; on the estate; inherit an estate
  2. [C1] all the money and property that a person owns, especially when they die
     - example: Her estate was divided equally between her three children.
     - collocations: leave an estate; estate tax; settle an estate; the deceased's estate
     - labels: domain law
     - grammar: number Sing, mainly
  3. [B2] an area of land with many houses or factories built on it together
     - example: We grew up on a big housing estate outside the city.
     - collocations: housing estate; council estate; industrial estate; on an estate
     - regions: en-GB mainly, en-AU mainly
  4. [B2] a car with a long body and a door at the back for luggage
     - example: They need an estate to fit the dogs and all the camping gear.
     - collocations: estate car
     - regions: en-GB only
  5. [C2] one of the social classes that had political power in a country in the past
     - example: The clergy formed the first estate of the realm.
     - collocations: the fourth estate; the three estates; estates of the realm
     - labels: time historical

### 19. sport

**Lexeme 1: sport**, NOUN
- grammar: countability Both
- said: /ˈspɔːɹt/ (en-US), /ˈspɔːt/ (en-GB)
  1. [A1] a game or physical activity needing effort and skill, played by rules
     - example: Football is the most popular sport in many countries.
     - collocations: do sport; play a sport; team sport; contact sport; spectator sport; winter sports; extreme sports; professional sport; competitive sport
     - grammar: note uncountable sport for all sports together is mainly British; American English says sports
  2. [B2] a person who stays cheerful when losing or when others joke about them
     - example: Thanks for helping us move, you're a real sport.
     - collocations: a good sport; be a sport
     - labels: formality informal
     - grammar: countability Count
  3. [C1] fun or pleasure, especially from hunting animals or laughing at others
     - example: In those days, rich men shot birds purely for sport.
     - collocations: for sport
     - grammar: countability Mass
**Lexeme 2: sport**, VERB
- grammar: valency Tr; irregular False
- said: /ˈspɔːɹt/ (en-US), /ˈspɔːt/ (en-GB)
  1. [C1] to wear or have something in a way that people notice
     - example: My uncle is sporting a long grey beard these days.
     - frames: sport sth
     - collocations: sport a beard; sport a moustache; sport a tattoo; sport a new look
     - grammar: note often in the progressive

### 20. dear

**Lexeme 1: dear**, ADJ
- grammar: comparable True
- said: /ˈdɪɹ/ (en-US), /ˈdɪə/ (en-GB)
  1. [A1] used before a name or title at the start of a letter or email
     - example: Dear Sir or Madam, I am writing to apply for the job.
     - collocations: Dear Sir or Madam; Dear Sir
     - grammar: position Attr; comparable False
  2. [B1] loved very much, or very important to someone
     - example: He has been a dear friend of mine for years.
     - frames: dear to sb
     - collocations: a dear friend; dear to my heart; hold dear; nearest and dearest
  3. [B2] costing a lot of money
     - example: Fresh fruit is very dear at this time of year.
     - regions: en-GB mainly
     - opposites: cheap
  4. [C1] sweet and pleasant to look at
     - example: They lived in a dear little cottage by the river.
     - labels: time dated
     - grammar: position Attr
- relations: confusable deer (NOUN): sounds the same as deer, the wild animal with antlers
**Lexeme 2: dear**, NOUN
- grammar: countability Count
- said: /ˈdɪɹ/ (en-US), /ˈdɪə/ (en-GB)
  1. [A2] used to speak in a friendly way to someone you love or like
     - example: Come and sit down, dear, dinner is almost ready.
     - collocations: my dear
     - grammar: note used as a form of address, without an article
  2. [B2] a kind and lovable person
     - example: Be a dear and pass me that book.
     - collocations: be a dear; a real dear
     - labels: formality informal
- relations: derived_from dear (ADJ)
**Lexeme 3: dear**, INTJ
- said: /ˈdɪɹ/ (en-US), /ˈdɪə/ (en-GB)
  1. [A2] used to show surprise, worry, sadness or annoyance
     - example: Oh dear, I think I've left my keys at home.
     - collocations: oh dear; dear me; dear oh dear
**Lexeme 4: dear**, ADV
- said: /ˈdɪɹ/ (en-US), /ˈdɪə/ (en-GB)
  1. [C1] at a high cost, or with great loss or suffering
     - example: That one careless mistake cost the team dear.
     - collocations: cost dear; pay dear
     - regions: en-GB mainly

### 21. healthy

**Lexeme 1: healthy**, ADJ
- grammar: comparable True
- said: /ˈhɛl.θi/ (en-US), /ˈhel.θi/ (en-GB)
  1. [A1] physically strong and well, and not ill
     - example: Regular exercise helps you stay healthy as you get older.
     - collocations: stay healthy; keep healthy; perfectly healthy; fit and healthy; a healthy baby; healthy skin; healthy teeth
     - opposites: unhealthy
  2. [A2] good for your body and helping you to stay well
     - example: I'm trying to eat a healthier diet with more vegetables.
     - collocations: healthy eating; a healthy diet; healthy food; a healthy lifestyle; healthy habits
     - opposites: unhealthy
  3. [B2] sensible and normal, and showing good judgment
     - example: She has a healthy attitude towards money and saving.
     - collocations: a healthy attitude; a healthy respect; healthy scepticism; a healthy relationship; healthy competition
     - grammar: position Attr, mainly
     - opposites: unhealthy
  4. [B2] successful and in good condition
     - example: The local economy is in a much healthier state now.
     - collocations: a healthy economy; financially healthy
     - labels: figurative True
     - opposites: unhealthy
  5. [C1] fairly large in amount or size
     - example: The shop made a healthy profit over the holidays.
     - collocations: a healthy profit; a healthy appetite; a healthy majority; a healthy lead
     - grammar: position Attr
- relations: adjective_of health (NOUN)

### 22. bag

**Lexeme 1: bag**, NOUN
- grammar: countability Mass
- said: /ˈbæɡ/ (en-US), /ˈbæɡ/ (en-GB)
  1. [A1] a container made of paper, plastic, cloth or leather, used for carrying things
     - example: Don't forget to pack your bags before the taxi arrives.
     - frames: bag of sth
     - collocations: pack your bags; plastic bag; paper bag; shopping bag; carry a bag; school bag; overnight bag
  2. [B2] dark areas of loose skin under a person's eyes
     - example: After a week of late nights, I've got huge bags under my eyes.
     - collocations: bags under your eyes
     - grammar: number Plur
  3. [C1] a large amount or number of something
     - example: Don't worry, there's bags of time before the film starts.
     - frames: bags of sth
     - labels: formality informal
     - regions: en-GB mainly
     - grammar: number Plur
  4. [C2] an unpleasant or unattractive older woman
     - example: The neighbours called her a miserable old bag behind her back.
     - collocations: old bag
     - labels: formality informal, attitude offensive
  5. [C2] an activity or subject that someone likes or is good at
     - example: Team sports were never really my bag.
     - collocations: not my bag
     - labels: formality informal, time dated
     - grammar: note usually in the negative, with a possessive
**Lexeme 2: bag**, VERB
- grammar: valency Tr; irregular False
- said: /ˈbæɡ/ (en-US), /ˈbæɡ/ (en-GB)
  1. [B2] to put things into a bag
     - example: The assistant bagged our groceries while we paid.
     - frames: bag sth; bag sth up
  2. [C1] to manage to get something, often before other people can
     - example: Get there early and bag us some good seats.
     - frames: bag sth; bag sb sth
     - collocations: bag a seat; bag a goal; bag a bargain
     - labels: formality informal
  3. [C2] to catch or kill an animal when hunting
     - example: The hunters bagged three ducks before lunch.
     - frames: bag sth
     - labels: domain hunting

### 23. driver

**Lexeme 1: driver**, NOUN
- grammar: countability Count
- said: /ˈdɹaɪ.vɚ/ (en-US), /ˈdɹaɪ.və/ (en-GB)
  1. [A1] a person who controls a car, bus, train or other vehicle
     - example: My uncle has been a taxi driver for twenty years.
     - collocations: taxi driver; bus driver; truck driver; lorry driver; learner driver; drunk driver; racing driver; careful driver; driver's seat; driver's license
  2. [C1] something that strongly causes a process or change to happen
     - example: Cheap energy was a key driver of the country's growth.
     - frames: driver of sth; driver for sth
     - collocations: key driver; main driver; major driver; driver of growth; driver of change
  3. [B2] a program that lets a computer control a connected device
     - example: You may need to install a new driver for the printer.
     - collocations: install a driver; update a driver; printer driver; device driver
     - labels: domain computing
  4. [C1] a golf club with a long handle, used for hitting the ball far
     - example: He took out his driver and hit the ball down the fairway.
     - labels: domain sport
- relations: agent_noun_of drive (VERB); derived_from drive (VERB)

### 24. cent

**Lexeme 1: cent**, NOUN
- grammar: countability Count
- said: /ˈsɛnt/ (en-US), /ˈsent/ (en-GB)
  1. [A1] a coin or unit of money worth one hundredth of a dollar or euro
     - example: This pen costs ninety-nine cents at the corner shop.
     - collocations: every cent; not a cent; save every cent; cents on the dollar; a few cents
- relations: confusable scent (NOUN): sounds the same as scent, a pleasant smell

### 25. dress

**Lexeme 1: dress**, NOUN
- said: /ˈdɹɛs/ (en-US), /ˈdɹes/ (en-GB)
  1. [A1] a piece of clothing for a woman or girl that covers the body and part of the legs
     - example: She bought a long blue dress for the party.
     - collocations: wear a dress; put on a dress; wedding dress; summer dress; long dress; short dress
     - grammar: countability Count
  2. [B2] clothes of a particular kind, worn for a special occasion or by a particular group
     - example: Guests are asked to come in formal dress.
     - collocations: evening dress; formal dress; national dress; traditional dress; dress code; casual dress
     - grammar: countability Mass
**Lexeme 2: dress**, VERB
- grammar: valency Both; irregular False
- said: /ˈdɹɛs/ (en-US), /ˈdɹes/ (en-GB)
  1. [A2] to put clothes on yourself or on someone else
     - example: I usually dress the children while my wife makes breakfast.
     - frames: dress sb
     - collocations: get dressed; dress quickly; fully dressed
     - grammar: valency Both
     - opposites: undress
  2. [B1] to wear clothes of a particular kind or style
     - example: You should dress smartly for the interview tomorrow.
     - frames: dress + adv; dress in sth; dress as sb; dress for sth
     - collocations: dress well; dress casually; dress smartly; be dressed in black
     - grammar: valency Intr, mainly; note often in the passive form be dressed in sth
  3. [C1] to clean an injury and put a bandage on it
     - example: The nurse cleaned and dressed the cut on his arm.
     - frames: dress sth
     - collocations: dress a wound
     - grammar: valency Tr
  4. [C1] to put a sauce or oil on a salad or other food
     - example: Dress the salad just before you serve it.
     - frames: dress sth with sth
     - collocations: dress a salad
     - grammar: valency Tr

### 26. begin

**Lexeme 1: begin**, VERB
- grammar: valency Both; irregular True
- said: /bɪ.ˈɡɪn/ (en-US), /bɪ.ˈɡɪn/ (en-GB)
- forms: began [Tense=Past, VerbForm=Fin] /bɪ.ˈɡæn/ /bɪ.ˈɡæn/; begun [Tense=Past, VerbForm=Part] /bɪ.ˈɡʌn/ /bɪ.ˈɡʌn/
  1. [A1] to start doing something or start working on something
     - example: Please turn over your papers and begin writing now.
     - frames: begin sth; begin to do sth; begin doing sth; begin by doing sth; begin with sth
     - collocations: begin work; begin again; begin a career; begin a journey; to begin with
     - grammar: valency Tr, mainly
     - opposites: finish
  2. [A1] to start to happen or exist
     - example: The concert begins at eight, so we should leave soon.
     - frames: begin at sth; begin in sth
     - collocations: begin soon; the story begins; begin promptly
     - grammar: valency Intr
     - opposites: end
  3. [A2] to have something as the first part, or start from a particular place
     - example: Every sentence in this game must begin with a different letter.
     - frames: begin with sth; begin at sth
     - grammar: valency Intr
     - opposites: end

### 27. assistant

**Lexeme 1: assistant**, NOUN
- grammar: countability Count
- said: /ə.ˈsɪs.tənt/ (en-US), /ə.ˈsɪs.tənt/ (en-GB)
  1. [A2] a person whose job is to help another person in their work
     - example: My assistant will email you the details tomorrow morning.
     - frames: assistant to sb
     - collocations: personal assistant; research assistant; teaching assistant; administrative assistant; laboratory assistant
  2. [B1] a person whose job is to serve customers in a shop
     - example: Ask one of the assistants if they have it in blue.
     - collocations: shop assistant; sales assistant
     - regions: en-GB mainly
  3. [B2] a computer program that answers questions and helps with everyday tasks
     - example: I asked the voice assistant to set an alarm for seven.
     - collocations: virtual assistant; voice assistant; digital assistant; AI assistant
- relations: agent_noun_of assist (VERB); confusable assistance (NOUN): assistance is the help given, not the person who gives it
**Lexeme 2: assistant**, ADJ
- grammar: position Attr; comparable False
- said: /ə.ˈsɪs.tənt/ (en-US), /ə.ˈsɪs.tənt/ (en-GB)
  1. [B1] having a job just below the person in charge
     - example: She was promoted to assistant manager after only a year.
     - collocations: assistant manager; assistant director; assistant professor; assistant coach; assistant editor
     - grammar: note used in job titles
- relations: derived_from assist (VERB)

### 28. wood

**Lexeme 1: wood**, NOUN
- said: /ˈwʊd/ (en-US), /ˈwʊd/ (en-GB)
  1. [A2] the hard material that the trunk and branches of a tree are made of
     - example: Our kitchen table is made of old wood from a farm.
     - collocations: made of wood; a piece of wood; chop wood; solid wood; gather wood; dark wood
     - grammar: countability Mass, mainly; note counted when kinds of it are meant: hard and soft woods
  2. [A2] an area of land covered with trees, smaller than a forest
     - example: The children love walking their dog in the woods.
     - collocations: walk in the woods; deep in the woods; through the woods
     - grammar: countability Count; note often in the plural with the same meaning: the woods
  3. [C1] a golf club with a large rounded head, used for hitting the ball far
     - example: He took out a wood for the long first shot.
     - labels: domain sport
     - grammar: countability Count
- relations: confusable would (AUX): sounds the same as would

### 29. roll

**Lexeme 1: roll**, VERB
- grammar: valency Both; irregular False
- said: /ˈɹoʊl/ (en-US), /ˈɹəʊl/ (en-GB)
  1. [B1] to move along a surface by turning over and over, or make something do this
     - example: He rolled onto his back and looked up at the stars.
     - frames: roll down sth; roll across sth; roll sth
     - collocations: roll downhill; roll away; roll a ball
  2. [B1] to turn your body over so that you lie in a different position
     - example: The ball rolled slowly across the floor and under the sofa.
     - frames: roll onto sth; roll sb over
     - collocations: roll over; roll onto your side; roll out of bed
     - grammar: valency Intr, mainly
  3. [B1] to move along smoothly on wheels, or make something do this
     - example: The car started to roll backwards down the slope.
     - frames: roll into sth; roll sth
     - collocations: roll forward; roll backwards; roll to a stop
  4. [B2] to move or flow smoothly and steadily in one direction
     - example: Tears were rolling down her cheeks as she read the letter.
     - frames: roll down sth; roll into sth
     - collocations: roll in; tears roll down; waves roll in
     - grammar: valency Intr
  5. [B2] to turn something over and over to make it into a ball or tube shape
     - example: Can you help me roll the carpet before the movers arrive?
     - frames: roll sth into sth; roll sth
     - collocations: roll up; roll a cigarette; roll up your sleeves; roll into a ball
     - grammar: valency Tr
  6. [B2] to make something flat by pushing a round object over it
     - example: Roll the dough until it is about half a centimetre thick.
     - frames: roll sth
     - collocations: roll out; roll out the dough; roll the pastry
     - grammar: valency Tr
  7. [B2] to throw dice onto a surface in a game
     - example: It's your turn, so roll the dice and move your piece.
     - frames: roll sth
     - collocations: roll the dice; roll a six; roll a double
     - grammar: valency Tr
  8. [B2] to move your eyes upwards and around to show you are annoyed or bored
     - example: My sister always rolls her eyes when I tell that joke.
     - collocations: roll your eyes
     - grammar: valency Tr
  9. [C1] to make a long, deep, continuous sound
     - example: Thunder rolled in the distance as we hurried home.
     - collocations: thunder rolls; drums roll
     - grammar: valency Intr
**Lexeme 2: roll**, NOUN
- grammar: countability Count
- said: /ˈɹoʊl/ (en-US), /ˈɹəʊl/ (en-GB)
  1. [A2] a long piece of paper, cloth, film or other material turned around itself into a tube
     - example: We need to buy another roll of toilet paper.
     - frames: roll of sth
     - collocations: a roll of toilet paper; a roll of film; a roll of tape; a roll of carpet; a roll of banknotes
  2. [A2] a small round loaf of bread for one person
     - example: She had a cheese roll and an apple for lunch.
     - collocations: bread roll; bacon roll; a warm roll; a crusty roll
  3. [C1] an official list of names of people belonging to a group
     - example: The teacher calls the roll at the start of every class.
     - collocations: call the roll; electoral roll; on the roll; roll of honour
  4. [C1] a single throw of dice in a game
     - example: Everything depends on the next roll of the dice.
     - collocations: a roll of the dice
  5. [C1] a long, deep, continuous sound
     - example: A low roll of thunder came from the hills.
     - frames: roll of sth
     - collocations: a roll of thunder; drum roll
  6. [B2] a movement in which a body turns over completely on the ground
     - example: The children practised forward rolls on the gym mat.
     - collocations: forward roll; backward roll; do a roll
- relations: confusable role (NOUN): sounds the same as role, the part someone plays

### 30. luggage

**Lexeme 1: luggage**, NOUN
- grammar: countability Mass
- said: /ˈlʌɡ.ɪdʒ/ (en-US), /ˈlʌɡ.ɪdʒ/ (en-GB)
  1. [A2] the bags and cases that you carry your things in when travelling
     - example: Please don't leave your luggage unattended in the station.
     - collocations: a piece of luggage; hand luggage; carry-on luggage; lost luggage; excess luggage; check in luggage; collect your luggage; luggage rack; luggage allowance
     - grammar: note not used in the plural; count it with "piece of"

### 31. brand

**Lexeme 1: brand**, NOUN
- grammar: countability Count
- said: /ˈbɹænd/ (en-US), /ˈbɹænd/ (en-GB)
  1. [B1] a type of product made by one company and sold under its name
     - example: Which brand of coffee do you usually buy?
     - frames: brand of sth
     - collocations: brand name; brand loyalty; brand image; own brand; leading brand; build a brand; global brand; major brand
  2. [C1] a particular kind or style of something, often typical of one person
     - example: He has his own strange brand of humour.
     - frames: brand of sth
     - collocations: brand of humour; brand of politics
  3. [C2] a mark burned onto an animal's skin to show who owns it
     - example: Each cow carried the ranch's brand on its side.
**Lexeme 2: brand**, VERB
- grammar: valency Tr; irregular False
- said: /ˈbɹænd/ (en-US), /ˈbɹænd/ (en-GB)
  1. [C1] to describe someone or something as bad or shameful, often unfairly
     - example: Critics quickly branded the new law a complete failure.
     - frames: brand sb/sth as sth; brand sb/sth sth; brand sb/sth as + adj
     - grammar: note often passive: be branded a liar
  2. [C2] to mark an animal by burning its skin with a hot iron
     - example: Ranchers still brand their cattle every spring.
     - frames: brand sth
  3. [C1] to give a product a name and image that people will recognise
     - example: The company decided to brand its water as a luxury product.
     - frames: brand sth; brand sth as sth

### 32. afford

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

### 33. blog

**Lexeme 1: blog**, NOUN
- grammar: countability Count
- said: /ˈblɑːɡ/ (en-US), /ˈblɔːɡ/ (en-US), /ˈblɒɡ/ (en-GB)
  1. [B1] a website where a person or group regularly writes about their experiences, opinions or interests
     - example: She writes a popular blog about cooking on a small budget.
     - frames: blog about sth; blog on sth
     - collocations: write a blog; start a blog; read a blog; run a blog; blog post; travel blog; food blog
- relations: derived_from weblog (NOUN)
**Lexeme 2: blog**, VERB
- grammar: valency Intr, mainly; irregular False
- said: /ˈblɑːɡ/ (en-US), /ˈblɔːɡ/ (en-US), /ˈblɒɡ/ (en-GB)
  1. [B2] to write regularly on a website about your experiences, opinions or interests
     - example: I've been blogging about my garden for almost five years.
     - frames: blog about sth; blog sth
     - collocations: blog regularly
- relations: derived_from blog (NOUN)

### 34. weather

**Lexeme 1: weather**, NOUN
- grammar: countability Mass
- said: /ˈwɛð.ɚ/ (en-US), /ˈweð.ə/ (en-GB)
  1. [A1] how hot, cold, wet, windy or sunny it is outside at a particular time
     - example: If the weather stays nice, we'll have lunch in the garden.
     - collocations: weather forecast; weather permitting; bad weather; good weather; cold weather; hot weather; wet weather; in all weathers; the weather turns
     - grammar: note in all weathers is mainly British
- relations: confusable whether (SCONJ): sounds the same as whether, used to talk about a choice between possibilities
**Lexeme 2: weather**, VERB
- grammar: valency Both; irregular False
- said: /ˈwɛð.ɚ/ (en-US), /ˈweð.ə/ (en-GB)
  1. [C1] to get through a difficult period or situation without serious harm
     - example: The small business managed to weather the economic crisis.
     - frames: weather sth
     - collocations: weather the storm; weather the crisis; weather the recession
     - grammar: valency Tr
  2. [C1] to change in colour or shape after a long time in the sun, wind and rain
     - example: The old wooden fence has weathered to a soft silver grey.
     - collocations: weather well
     - grammar: valency Both
- relations: derived_from weather (NOUN); confusable whether (SCONJ): sounds the same as whether, used to talk about a choice between possibilities

### 35. worry

**Lexeme 1: worry**, VERB
- grammar: valency Both; irregular False
- said: /ˈwɝ.i/ (en-US), /ˈwʌ.ɹi/ (en-GB)
  1. [A2] to keep thinking about problems or bad things that might happen and feel anxious
     - example: Don't worry about the test, you've studied hard for it.
     - frames: worry about sb/sth; worry over sth; worry that ...
     - collocations: don't worry; stop worrying; worry too much; worry constantly; nothing to worry about
     - grammar: valency Intr
  2. [B1] to make someone feel anxious or upset
     - example: It worries me that she hasn't called since Monday.
     - frames: worry sb; it worries sb that ...
     - collocations: worry yourself sick; deeply worry
     - grammar: valency Tr
  3. [C2] of a dog, to chase and attack animals, biting and shaking them
     - example: Farmers may shoot dogs that worry their sheep.
     - frames: worry sth
     - collocations: worry sheep
     - grammar: valency Tr
**Lexeme 2: worry**, NOUN
- grammar: countability Both
- said: /ˈwɝ.i/ (en-US), /ˈwʌ.ɹi/ (en-GB)
  1. [B1] an anxious feeling about problems or about what might happen
     - example: Her face was lined with worry after weeks without news.
     - frames: worry about sth; worry over sth
     - collocations: cause for worry; a source of worry; sick with worry; free from worry
     - grammar: countability Mass
  2. [B1] a problem or situation that makes you feel anxious
     - example: My biggest worry is finding somewhere affordable to live.
     - frames: worry about sth; worry that ...
     - collocations: money worries; main worry; biggest worry; health worries; a constant worry; no worries
     - grammar: countability Count

### 36. authority

**Lexeme 1: authority**, NOUN
- grammar: countability Both
- said: /ə.ˈθɔːɹ.ə.ti/ (en-US), /ə.ˈθɑːɹ.ə.ti/ (en-US), /ɔː.ˈθɒɹ.ə.ti/ (en-GB), /ə.ˈθɒɹ.ə.ti/ (en-GB)
  1. [B2] the power to give orders and make people obey them
     - example: The authorities have closed the road after the flood.
     - frames: authority over sb/sth
     - collocations: in authority; a position of authority; exercise authority; undermine authority; challenge authority; have authority over
     - grammar: countability Mass
  2. [B2] an official organization or group with power to make decisions in an area
     - example: Teachers need enough authority to keep order in class.
     - collocations: the authorities; local authority; health authority; tax authorities; port authority; inform the authorities
     - grammar: countability Count; note often plural: the authorities
  3. [C1] official permission or the right to do something
     - example: Only the manager has the authority to sign these contracts.
     - frames: authority to do sth; authority for sth; give sb authority
     - collocations: without authority; on whose authority
     - grammar: countability Mass
  4. [C1] a person with special knowledge of a subject whose opinion is respected
     - example: She is a leading authority on medieval history.
     - frames: authority on sth
     - collocations: a leading authority; a world authority
     - grammar: countability Count
  5. [C1] the quality of making people respect and believe you through knowledge or confidence
     - example: He spoke with such authority that nobody questioned him.
     - collocations: speak with authority; an air of authority; moral authority
     - grammar: countability Mass

### 37. profile

**Lexeme 1: profile**, NOUN
- grammar: countability Count
- said: /ˈpɹoʊ.faɪl/ (en-US), /ˈpɹəʊ.faɪl/ (en-GB)
  1. [B2] a description of a person, group or organization giving useful information about them
     - example: The magazine published a short profile of the new director.
     - frames: profile of sb/sth
     - collocations: publish a profile; write a profile; customer profile; job profile; risk profile
  2. [B1] a set of personal details and pictures that someone puts on a website
     - example: I finally updated my profile with a recent photo.
     - collocations: online profile; profile picture; update your profile; create a profile; social media profile; dating profile
  3. [C1] the amount of public attention that someone or something receives
     - example: The campaign helped to raise the profile of the charity.
     - collocations: raise the profile; high profile; low profile; keep a low profile; public profile
     - grammar: countability Count; note usually singular
  4. [C1] the outline of a person's face when seen from the side
     - example: The coin shows the queen's head in profile.
     - collocations: in profile; side profile
**Lexeme 2: profile**, VERB
- grammar: valency Tr; irregular False
- said: /ˈpɹoʊ.faɪl/ (en-US), /ˈpɹəʊ.faɪl/ (en-GB)
  1. [C1] to give a description of a person, group or organization in a newspaper or programme
     - example: Each week the show profiles a different local business.
     - frames: profile sb/sth
  2. [C2] to judge what people are likely to do from information about their characteristics
     - example: Police officers must not profile drivers because of their appearance.
     - frames: profile sb
     - collocations: racial profiling

### 38. totally

**Lexeme 1: totally**, ADV
- grammar: comparable False
- said: /ˈtoʊ.təl.i/ (en-US), /ˈtəʊ.təl.i/ (en-GB)
  1. [B1] in every way or to the greatest possible degree
     - example: I totally forgot about our meeting this morning.
     - collocations: totally different; totally agree; totally wrong; totally unexpected; totally destroyed; totally new; totally unacceptable
  2. [B2] used to make a statement stronger or to show that you fully agree
     - example: That concert was totally amazing, you should have come!
     - collocations: totally awesome
     - labels: formality informal
     - grammar: note also used alone as a reply: "Totally!"
- relations: adverb_of total (ADJ)

### 39. apart

**Lexeme 1: apart**, ADV
- said: /ə.ˈpɑːɹt/ (en-US), /ə.ˈpɑːt/ (en-GB)
  1. [B1] separated from each other by a particular distance or time
     - example: The two villages are only about five miles apart.
     - collocations: far apart; miles apart; years apart; wide apart; spaced apart; born two years apart
  2. [B1] not together, in separate places or as separate people
     - example: My parents have been living apart for over a year.
     - frames: apart from sb
     - collocations: live apart; grow apart; drift apart; spend time apart; keep apart; be torn apart
     - opposites: together
  3. [B1] into separate pieces or parts
     - example: Be careful, that old book is falling apart.
     - collocations: fall apart; come apart; take apart; pull apart; tear apart; break apart; rip apart
  4. [B2] away from or different from other people or things
     - example: Her calm voice sets her apart from the other singers.
     - frames: apart from sb/sth
     - collocations: set apart; stand apart; a world apart; a breed apart; worlds apart
  5. [C1] used to show that someone or something is not being included or considered
     - example: Joking apart, we really need to find a solution soon.
     - collocations: joking apart; jokes apart
     - grammar: note comes after the noun it refers to

### 40. aware

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

### 41. fat

**Lexeme 1: fat**, ADJ
- grammar: comparable True
- said: /ˈfæt/ (en-US), /ˈfæt/ (en-GB)
  1. [A1] having a lot of flesh on the body and weighing too much
     - example: Our cat got fat because we fed her too often.
     - collocations: get fat; grow fat; fat and lazy
     - grammar: note often considered rude when said of a person
     - opposites: thin
  2. [B2] thick, wide or full compared with others of its kind
     - example: He was reading a fat book about the history of trains.
     - collocations: fat book; fat cigar; fat envelope
     - opposites: thin
  3. [C1] large in amount, especially of money
     - example: Bankers there earn fat salaries and even fatter bonuses.
     - collocations: fat cheque; fat salary; fat profit; fat fee
     - labels: formality informal
**Lexeme 2: fat**, NOUN
- grammar: countability Mass, mainly
- said: /ˈfæt/ (en-US), /ˈfæt/ (en-GB)
  1. [B1] the soft white or yellow substance under the skin of people and animals
     - example: Regular exercise helps you burn fat and build muscle.
     - collocations: body fat; burn fat; lose fat; layer of fat
     - grammar: countability Mass
  2. [B1] an oily solid or liquid from animals or plants, used in cooking or found in food
     - example: Try to eat less food that is high in fat.
     - collocations: low in fat; high in fat; animal fat; vegetable fat; saturated fat; fry in fat

### 42. realize

**Lexeme 1: realize**, VERB
- grammar: valency Tr; irregular False
- said: /ˈɹiː.ə.laɪz/ (en-US), /ˈɹɪə.laɪz/ (en-GB), /ˈɹiː.ə.laɪz/ (en-GB)
- spellings: realize (en-US, en-GB); realise (en-GB, en-AU, en-IN)
  1. [B1] to start to know or understand that something is true
     - example: I suddenly realized that I had left my keys at home.
     - frames: realize that ...; realize sth; realize how ...; realize what ...
     - collocations: suddenly realize; soon realize; finally realize; fully realize; come to realize; quickly realize
  2. [B2] to achieve something that you have hoped or planned for
     - example: She worked hard for years to realize her dream of flying.
     - frames: realize sth
     - collocations: realize a dream; realize an ambition; realize your potential; realize a goal
  3. [C1] to happen in the way that someone was afraid of
     - example: Our worst fears were realized when the bridge finally collapsed.
     - collocations: fears are realized; worst fears realized
     - grammar: note usually passive, with fears as the subject
  4. [C2] to be sold for a particular amount of money
     - example: The old paintings realized more than a million pounds at auction.
     - frames: realize sth
     - labels: formality formal, domain finance
- relations: derived_from real (ADJ)

### 43. mouth

**Lexeme 1: mouth**, NOUN
- grammar: countability Count
- said: /ˈmaʊθ/ (en-US), /ˈmaʊθ/ (en-GB)
- forms: mouths [Number=Plur] /ˈmaʊðz/ /ˈmaʊðz/
  1. [A1] the opening in the face used for eating, speaking and breathing
     - example: Please don't talk with your mouth full.
     - collocations: open your mouth; shut your mouth; keep your mouth shut; with your mouth full; wipe your mouth; dry mouth
  2. [B2] the opening or entrance of a cave, container or other hollow thing
     - example: A cold wind blew from the mouth of the cave.
     - frames: the mouth of sth
     - collocations: the mouth of a cave; the mouth of a bottle
  3. [B2] the place where a river flows into the sea
     - example: The town grew up at the mouth of the river.
     - frames: the mouth of sth
     - collocations: at the mouth of the river; river mouth
**Lexeme 2: mouth**, VERB
- grammar: valency Tr; irregular False
- said: /ˈmaʊð/ (en-US), /ˈmaʊð/ (en-GB)
  1. [B2] to move your lips as if saying words, but without making any sound
     - example: She mouthed a quick thank-you across the crowded room.
     - frames: mouth sth; mouth sth to sb
     - collocations: mouth the words
  2. [C1] to say things you do not really believe or understand
     - example: Too many leaders just mouth slogans about change.
     - frames: mouth sth
     - collocations: mouth platitudes; mouth slogans
     - labels: attitude disapproving
- relations: derived_from mouth (NOUN)

### 44. institute

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

### 45. fuel

**Lexeme 1: fuel**, NOUN
- grammar: countability Mass, mainly
- said: /ˈfjuː.əl/ (en-US), /ˈfjuːl/ (en-US), /ˈfjuː.əl/ (en-GB), /ˈfjʊəl/ (en-GB)
  1. [B1] a material that is burned or used to produce heat or power
     - example: Our old car uses far too much fuel on long trips.
     - collocations: run out of fuel; fossil fuels; fuel consumption; fuel tank; fuel prices; fuel efficiency; nuclear fuel; save fuel; fuel bills
  2. [C1] something that makes a feeling, argument or situation stronger or worse
     - example: His angry reply only gave fuel to the rumours.
     - frames: fuel for sth
     - collocations: add fuel to the fire; add fuel to the flames
     - labels: figurative True
**Lexeme 2: fuel**, VERB
- grammar: valency Tr; irregular False; note l is doubled before -ed and -ing in British spelling
- said: /ˈfjuː.əl/ (en-US), /ˈfjuːl/ (en-US), /ˈfjuː.əl/ (en-GB), /ˈfjʊəl/ (en-GB)
- forms: fuelled [Tense=Past, VerbForm=Fin,Part] /ˈfjuː.əld/ (en-GB, en-AU, en-IN); fueled [Tense=Past, VerbForm=Fin,Part] /ˈfjuː.əld/ (en-US); fuelling [Tense=Pres, VerbForm=Part] /ˈfjuː.ə.lɪŋ/ (en-GB, en-AU, en-IN); fueling [Tense=Pres, VerbForm=Part] /ˈfjuː.ə.lɪŋ/ (en-US)
  1. [B2] to make a feeling, belief or process stronger or increase faster
     - example: Rising prices are fuelling anger among ordinary families.
     - frames: fuel sth
     - collocations: fuel speculation; fuel fears; fuel inflation; fuel growth; fuel demand; fuel a debate
  2. [B2] to supply a vehicle or machine with material that it burns for power
     - example: These power stations are fuelled by coal from local mines.
     - frames: fuel sth; be fuelled by sth
- relations: derived_from fuel (NOUN)

### 46. apply

**Lexeme 1: apply**, VERB
- grammar: valency Both; irregular False
- said: /ˈə.plaɪ/ (en-US), /ˈə.plaɪ/ (en-GB)
  1. [A2] to make a formal, usually written, request for something
     - example: She has applied for a job at the local hospital.
     - frames: apply for sth; apply to sb/sth; apply to do sth
     - collocations: apply for a job; apply for a visa; apply for a loan; apply for a place; apply online; apply in writing
     - grammar: valency Intr
  2. [B1] to be relevant to someone or something and have an effect on them
     - example: These rules apply to all students, including part-time ones.
     - frames: apply to sb/sth
     - collocations: the same applies; conditions apply; rules apply; terms and conditions apply
     - grammar: valency Intr
  3. [B2] to use a method, rule or knowledge in a particular situation
     - example: We can apply this method to other problems as well.
     - frames: apply sth to sth; apply sth
     - collocations: apply the law; apply a rule; apply a principle; apply knowledge; apply a technique; strictly apply
     - grammar: valency Tr
  4. [B2] to put or spread a substance on a surface
     - example: Apply the cream to your skin twice a day.
     - frames: apply sth to sth; apply sth
     - collocations: apply a thin layer; apply evenly; apply sunscreen; apply makeup; apply paint
     - grammar: valency Tr
  5. [C1] to press or push on something with force
     - example: Apply firm pressure to the wound to stop the bleeding.
     - frames: apply sth to sth
     - collocations: apply pressure; apply force; apply the brakes
     - grammar: valency Tr
  6. [C1] to work hard and give all your attention to something
     - example: If you apply yourself, you could pass every exam this year.
     - frames: apply oneself to sth; apply oneself to doing sth
     - grammar: valency Refl; note used with a reflexive pronoun: apply yourself

### 47. lie

**Lexeme 1: lie**, VERB
- grammar: valency Intr; irregular True
- said: /ˈlaɪ/ (en-US), /ˈlaɪ/ (en-GB)
- forms: lay [Tense=Past, VerbForm=Fin] /ˈleɪ/ /ˈleɪ/; lain [Tense=Past, VerbForm=Part] /ˈleɪn/ /ˈleɪn/; lying [Tense=Pres, VerbForm=Part] /ˈlaɪ.ɪŋ/ /ˈlaɪ.ɪŋ/
  1. [A2] to be or put yourself in a flat position on a surface, not standing or sitting
     - example: I like to lie on the grass and watch the clouds.
     - frames: lie on sth; lie in sth
     - collocations: lie down; lie in bed; lie awake; lie still; lie flat; lie on your back; lie on your side
  2. [B1] of a thing, to be flat on a surface and stay there
     - example: Her clothes were lying all over the bedroom floor.
     - frames: lie on sth
     - collocations: lie open; lie around
  3. [B2] to stay in a particular state or condition
     - example: The house has lain empty for years.
     - frames: lie + adj
     - collocations: lie empty; lie hidden; lie dormant; lie idle; lie in ruins; lie unused
  4. [B2] to be in a particular place or position
     - example: The village lies at the foot of a mountain.
     - frames: lie in sth; lie on sth
     - collocations: lie to the north; lie on the coast; lie between
  5. [B2] to exist or be found in something
     - example: The real problem lies in the way we train our staff.
     - frames: lie in sth; lie with sb
     - collocations: the problem lies; the answer lies; the responsibility lies with; the difference lies
  6. [B2] to be going to happen or to be waiting for someone in the future
     - example: Nobody knows what challenges lie ahead of us.
     - collocations: lie ahead; lie in store
- relations: confusable lay (VERB): lay means to put something down and takes an object; lay is also the past tense of lie
**Lexeme 2: lie**, VERB
- grammar: valency Intr; irregular False
- said: /ˈlaɪ/ (en-US), /ˈlaɪ/ (en-GB)
- forms: lied [Tense=Past, VerbForm=Fin,Part] /ˈlaɪd/ /ˈlaɪd/; lying [Tense=Pres, VerbForm=Part] /ˈlaɪ.ɪŋ/ /ˈlaɪ.ɪŋ/
  1. [B1] to say or write something that you know is not true
     - example: He lied to his parents about where he had been.
     - frames: lie to sb; lie about sth
     - collocations: lie through your teeth; lie under oath; lie blatantly; lie outright
- relations: derived_from lie (NOUN)
**Lexeme 3: lie**, NOUN
- grammar: countability Count
- said: /ˈlaɪ/ (en-US), /ˈlaɪ/ (en-GB)
  1. [B1] something you say or write that you know is not true
     - example: Don't believe him, everything he told you was a lie.
     - frames: lie about sth
     - collocations: tell a lie; a white lie; a pack of lies; a blatant lie; an outright lie; a complete lie; live a lie; give the lie to
     - opposites: truth

### 48. murder

**Lexeme 1: murder**, NOUN
- grammar: countability Both
- said: /ˈmɝ.dɚ/ (en-US), /ˈmɜː.də/ (en-GB)
  1. [B1] the crime of killing a person deliberately
     - example: The police are still looking for the weapon used in the murder.
     - collocations: commit murder; attempted murder; be charged with murder; be accused of murder; murder trial; murder case; murder victim; murder weapon; mass murder
  2. [C1] something that is very difficult or unpleasant to do or bear
     - example: It's murder trying to find a parking space in town on Saturdays.
     - labels: formality informal
     - grammar: countability Mass; note usually after be
**Lexeme 2: murder**, VERB
- grammar: valency Tr; irregular False
- said: /ˈmɝ.dɚ/ (en-US), /ˈmɜː.də/ (en-GB)
  1. [B1] to kill a person deliberately and illegally
     - example: He was accused of murdering his business partner for money.
     - frames: murder sb
     - collocations: brutally murdered; murdered in cold blood
  2. [C1] to perform or use something so badly that it is spoiled
     - example: The band murdered one of my favourite songs last night.
     - frames: murder sth
     - labels: formality informal
  3. [C1] to defeat someone completely in a game or competition
     - example: We got murdered in the final, losing six goals to nil.
     - frames: murder sb
     - labels: formality informal

### 49. status

**Lexeme 1: status**, NOUN
- grammar: countability Both
- said: /ˈsteɪ.təs/ (en-US), /ˈstæ.təs/ (en-US), /ˈsteɪ.təs/ (en-GB)
  1. [B2] the official or legal position of a person, group or country
     - example: Please state your marital status on the form.
     - frames: status as sth; the status of sb/sth
     - collocations: marital status; legal status; refugee status; immigration status; grant status; official status; employment status
     - grammar: countability Mass, mainly
  2. [B2] the level of respect and importance a person or thing has compared with others
     - example: Doctors still enjoy high status in most communities.
     - collocations: social status; high status; low status; status symbol; celebrity status; achieve status; enjoy status; gain status
     - grammar: countability Mass
  3. [B2] the situation of something at a particular time, especially during a process
     - example: You can check the status of your order online.
     - frames: the status of sth
     - collocations: check the status; current status; status report; health status; status update
     - grammar: countability Mass, mainly; number Sing, mainly
  4. [B1] a short message on a social media page saying what someone is doing or thinking
     - example: She changed her status to say she was on holiday.
     - collocations: post a status; update your status; status update
     - grammar: countability Count

### 50. lake

**Lexeme 1: lake**, NOUN
- grammar: countability Count
- said: /ˈleɪk/ (en-US), /ˈleɪk/ (en-GB)
  1. [A1] a large area of water with land all around it
     - example: We went swimming in the lake every summer morning.
     - collocations: on the lake; in the lake

### 51. flight

**Lexeme 1: flight**, NOUN
- said: /ˈflaɪt/ (en-US), /ˈflaɪt/ (en-GB)
  1. [A2] a journey in a plane, or a plane making a planned journey
     - example: Our flight to the coast was delayed by two hours.
     - frames: flight to sth; flight from sth
     - collocations: catch a flight; book a flight; miss a flight; on a flight; direct flight; connecting flight; long-haul flight; domestic flight; international flight; return flight
     - grammar: countability Count
  2. [B1] movement through the air by something with wings, an engine or force
     - example: The camera follows the eagle's flight across the valley.
     - collocations: in flight; take flight; the flight of birds; space flight
     - grammar: countability Mass, mainly
  3. [B2] a set of steps going from one floor or level to the next
     - example: You have to climb three flights of stairs to reach the flat.
     - collocations: a flight of stairs; a flight of steps
     - grammar: countability Count
  4. [C1] running away from danger or from a difficult situation
     - example: Thousands of families joined the flight from the burning city.
     - frames: flight from sth; put sb to flight
     - collocations: take flight; in flight
     - grammar: countability Mass

### 52. hall

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

### 53. gain

**Lexeme 1: gain**, VERB
- grammar: irregular False
- said: /ˈɡeɪn/ (en-US), /ˈɡeɪn/ (en-GB)
  1. [B1] to get something useful or wanted, often slowly or through effort
     - example: You'll gain a lot of experience working in a busy hotel.
     - frames: gain sth
     - collocations: gain experience; gain access; gain control; gain confidence; gain support; gain entry; gain a reputation; gain ground; gain independence; gain popularity
     - grammar: valency Tr
     - opposites: lose
  2. [B1] to get more of a quality, such as weight or speed
     - example: The train slowly gained speed as it left the station.
     - frames: gain sth; gain in sth
     - collocations: gain weight; gain speed; gain height; gain momentum; gain in popularity; gain in value
     - grammar: valency Tr, mainly
     - opposites: lose
  3. [B2] to get an advantage or benefit from something
     - example: Who really gains from these changes to the tax system?
     - frames: gain from sth; gain by doing sth
     - collocations: stand to gain; nothing to gain; have much to gain
     - grammar: valency Intr
     - opposites: lose
**Lexeme 2: gain**, NOUN
- said: /ˈɡeɪn/ (en-US), /ˈɡeɪn/ (en-GB)
  1. [B2] an increase in amount, size, weight or value
     - example: The company reported a small gain in profits this year.
     - frames: gain in sth
     - collocations: weight gain; make gains; significant gain; net gain; a sharp gain
     - grammar: countability Count
     - opposites: loss
  2. [B2] an advantage or profit that you get from something
     - example: He only helped them for his own personal gain.
     - collocations: personal gain; financial gain; for gain; ill-gotten gains; short-term gain; political gain
     - grammar: countability Both
     - opposites: loss
