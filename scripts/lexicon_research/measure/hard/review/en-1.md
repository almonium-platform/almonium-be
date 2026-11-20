# English: two entries for each word (10 words)

Each word below has two entries of a learner's dictionary of English, A and B, written by two different writers
under the same rules. You are not told which writer wrote which, and the sides change from word to word. Some of
these words are hard on purpose: two words under one spelling, forms that are not words of their own, senses of
one country only, rude words.

For each word say which entry serves a learner better, and name the faults of each. A writer may have refused a
word as no dictionary word of its own ("refused: inflected form of ..."); that is right for an inflected form, an
abbreviation or a name, and wrong for a word a dictionary of English lists.

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

One line of JSON for every word, in the order given, inside a single ```json block:

```json
{"id": 1, "better": "A", "why": "B lacks the metal, the commoner of the two words spelt lead", "A": [], "B": [{"fault": "missing-core", "where": "lexemes", "what": "the metal is missing"}]}
```

"better" is "A", "B" or "same": "same" only when you could not say which you would rather give a learner. "why" is
one sentence. "A" and "B" list each entry's faults as above, empty when it has none.

## The words

### 1. lead

#### Entry A

**Lexeme 1: lead**, VERB
- grammar: irregular True; valency Both
- said: /ˈliːd/ (en-US), /ˈliːd/ (en-GB)
- forms: led [Tense=Past, VerbForm=Fin,Part] /ˈlɛd/ /ˈled/
  1. [A2] to show someone the way by going in front of them
     - example: Follow me closely and I will lead you to the exit.
     - frames: lead sb to sth; lead sb into sth
     - collocations: lead the way
     - grammar: valency Tr
  2. [B1] to go in a particular direction or reach a particular place
     - example: Does anyone know where this narrow forest path leads?
     - frames: lead to sth; lead into sth; lead + adv
     - grammar: valency Intr
  3. [B1] to cause something to happen or make someone do something
     - example: Years of poor diet can lead to serious health problems.
     - frames: lead to sth; lead sb to do sth
     - collocations: one thing leads to another
     - grammar: valency Both
  4. [B1] to be in charge of a group, an activity or an organization
     - example: She has led the medical research team for six years.
     - frames: lead sth
     - collocations: lead a team; lead a campaign; lead a discussion; lead the country
     - grammar: valency Tr
  5. [B2] to be ahead of others in a race, game or competition
     - example: The home team were leading by two goals at half-time.
     - frames: lead sth; lead by sth
     - collocations: lead the race; lead the field
     - grammar: valency Both
     - opposites: trail
  6. [B2] to have or experience a particular kind of life
     - example: My grandparents lead a quiet life in a small village.
     - frames: lead sth
     - collocations: lead a normal life; lead a busy life; lead a quiet life; lead a double life
     - grammar: valency Tr
**Lexeme 2: lead**, NOUN
- grammar: countability Count
- said: /ˈliːd/ (en-US), /ˈliːd/ (en-GB)
  1. [B1] the first position in a race, game or competition, or the distance ahead
     - example: Our runner took the lead in the final lap.
     - frames: lead over sb/sth
     - collocations: take the lead; in the lead; hold the lead; lose the lead; a narrow lead; a comfortable lead
  2. [B2] an example or first step that other people can copy or follow
     - example: Other companies are expected to follow their lead on prices.
     - frames: follow sb's lead
     - collocations: take the lead; give a lead
  3. [B2] the main part in a play or film, or the actor who plays it
     - example: He was offered the romantic lead in the new musical.
     - collocations: play the lead; the female lead; the male lead; the romantic lead
  4. [B2] a piece of information that may help solve a crime or problem
     - example: Detectives are following up several promising leads after the robbery.
     - collocations: a promising lead; follow up a lead
  5. [B2] a long strap, rope or chain fastened to a dog's collar
     - example: Please keep your dog on a lead near the road.
     - collocations: on a lead; off the lead
     - regions: en-GB only, en-AU only
  6. [B2] a covered wire that connects electrical equipment to a power supply or another device
     - example: Check that the power lead is plugged into the wall.
     - collocations: an extension lead; a power lead
     - regions: en-GB only, en-AU only
**Lexeme 3: lead**, ADJ
- grammar: position Attr; comparable False
- said: /ˈliːd/ (en-US), /ˈliːd/ (en-GB)
  1. [B2] having the first, main or most important part in a group or performance
     - example: The band had to cancel because the lead singer was ill.
     - collocations: lead singer; lead role; lead guitar; lead author
**Lexeme 4: lead**, NOUN
- said: /ˈlɛd/ (en-US), /ˈled/ (en-GB)
  1. [B2] a soft heavy grey metal, used in batteries and once used for water pipes
     - example: Many older houses still have water pipes made of lead.
     - collocations: lead pipe; lead paint; lead poisoning
     - grammar: countability Mass
  2. [B2] the thin black or grey stick inside a pencil that makes marks
     - example: Press gently on the paper or the pencil lead will break.
     - collocations: pencil lead
     - grammar: countability Both
- relations: confusable lead (VERB): spelled like the verb lead, and sounds like its past form led

#### Entry B

**Lexeme 1: lead**, VERB
- grammar: irregular True; valency Both
- said: /ˈliːd/ (en-US), /ˈliːd/ (en-GB)
- forms: led [Tense=Past, VerbForm=Fin,Part] /ˈlɛd/ /ˈled/
  1. [A2] to show someone the way by going in front of them
     - example: Follow me and I'll lead you to the exit.
     - frames: lead sb to sth; lead sb into sth; lead sb through sth
     - collocations: lead the way
     - grammar: valency Tr
  2. [B1] to cause something to happen or cause someone to do something
     - example: Eating too much sugar can lead to serious health problems.
     - frames: lead to sth; lead sb to do sth
     - collocations: lead to problems; lead to an increase; inevitably lead to; eventually lead to; lead to the conclusion
     - grammar: valency Both
  3. [B1] to be in charge of a group of people, an activity or an organization
     - example: She has led the research team for ten years.
     - collocations: lead a team; lead a campaign; lead the country; lead an attack; lead a discussion
     - grammar: valency Tr
  4. [B1] to go towards a particular place or connect with it
     - example: This narrow path leads down to the beach.
     - frames: lead to sth; lead into sth; lead from sth
     - grammar: valency Intr; note said of a road, path, door or passage
  5. [B2] to live a particular kind of life
     - example: After the accident he still leads a perfectly normal life.
     - collocations: lead a normal life; lead a quiet life; lead a busy life; lead a double life
     - grammar: valency Tr
  6. [B2] to be ahead of others in a race, competition or activity
     - example: We were leading by two goals at half-time.
     - frames: lead by sth
     - collocations: lead the race; lead the league; lead the world; lead the field
     - grammar: valency Both
     - opposites: trail
**Lexeme 2: lead**, NOUN
- grammar: countability Count
- said: /ˈliːd/ (en-US), /ˈliːd/ (en-GB)
  1. [B2] a position ahead of others in a race or competition, or the distance ahead
     - example: Our team now has a lead of three points.
     - frames: lead over sb
     - collocations: take the lead; in the lead; hold the lead; lose the lead; extend the lead; a narrow lead; a comfortable lead
  2. [B2] an action or example that other people can copy
     - example: Other companies quickly followed their lead and cut prices.
     - frames: follow sb's lead
     - collocations: take the lead; give a lead
     - grammar: number Sing
  3. [B2] the main part in a play, film or show, or the performer who has it
     - example: She got the lead in the school musical this year.
     - collocations: play the lead; the lead role; lead singer; lead actor
  4. [C1] a piece of information that may help to discover the truth
     - example: Police are following up several new leads in the case.
     - collocations: follow up a lead; a promising lead; a new lead
  5. [B2] a long strap or chain fixed to a dog's collar
     - example: Please keep your dog on a lead near the road.
     - collocations: on a lead; off the lead
     - regions: en-GB only, en-AU only
  6. [B2] a wire that connects a piece of electrical equipment to the power supply
     - example: I can't charge my laptop because I forgot the lead.
     - collocations: extension lead; plug in the lead
     - regions: en-GB mainly, en-AU mainly
- relations: derived_from lead (VERB)
**Lexeme 3: lead**, NOUN
- said: /ˈlɛd/ (en-US), /ˈled/ (en-GB)
  1. [B2] a soft heavy grey metal, once used for water pipes
     - example: Old houses sometimes still have pipes made of lead.
     - collocations: lead pipe; lead poisoning; lead paint
     - grammar: countability Mass
  2. [B2] the thin grey part inside a pencil that makes marks
     - example: The lead in my pencil keeps breaking.
     - collocations: pencil lead
     - grammar: countability Both
- relations: confusable lead (VERB): sounds like led, the past of the verb lead

### 2. tear

#### Entry A

**Lexeme 1: tear**, NOUN
- grammar: countability Count
- said: /ˈtɪɹ/ (en-US), /ˈtɪə/ (en-GB)
  1. [A2] a drop of salty liquid that comes from your eye when you cry
     - example: Tears ran down her face as she read the letter.
     - frames: bring tears to sb's eyes
     - collocations: in tears; burst into tears; shed tears; fight back tears; close to tears; tears of joy; be moved to tears
     - grammar: number Plur, mainly
- relations: confusable tear (VERB): spelled the same as the verb tear /teə/, to pull apart
**Lexeme 2: tear**, VERB
- grammar: valency Both; irregular True
- said: /ˈtɛɹ/ (en-US), /ˈteə/ (en-GB)
- forms: tore [Tense=Past, VerbForm=Fin] /ˈtɔːɹ/ /ˈtɔː/; torn [Tense=Past, VerbForm=Part] /ˈtɔːɹn/ /ˈtɔːn/
  1. [B1] to pull paper or cloth apart, or make a hole in it, by force
     - example: Be careful not to tear the pages when you turn them.
     - frames: tear sth; tear sth in half; tear sth on sth; tear sth to pieces; tear sth open
     - collocations: tear a hole; tear a muscle; tear easily
  2. [B2] to remove something from somewhere by pulling it roughly
     - example: The wind tore the roof off the old shed.
     - frames: tear sth off sth; tear sth from sth; tear sth out of sth
     - grammar: valency Tr
  3. [C1] to move somewhere very fast, often in a careless way
     - example: Two boys came tearing down the street on bikes.
     - frames: tear + adv; tear along sth; tear around sth
     - labels: formality informal
     - grammar: valency Intr
  4. [C1] to badly divide a country or group of people through conflict
     - example: The country has been torn apart by years of civil war.
     - frames: be torn by sth; tear sth apart
     - collocations: torn apart
     - labels: figurative True
     - grammar: valency Tr; note usually passive
  5. [C1] to make someone unable to choose between two wishes or loyalties
     - example: I was torn between staying at home and travelling abroad.
     - frames: be torn between sth and sth
     - labels: figurative True
     - grammar: valency Tr; note only in the passive
**Lexeme 3: tear**, NOUN
- grammar: countability Count
- said: /ˈtɛɹ/ (en-US), /ˈteə/ (en-GB)
  1. [B2] a hole or split in something where it has been pulled apart
     - example: There's a small tear in the sleeve of my jacket.
     - frames: tear in sth
     - collocations: wear and tear; a muscle tear
**Lexeme 4: tear**, VERB
- grammar: valency Intr; irregular False
- said: /ˈtɪɹ/ (en-US), /ˈtɪə/ (en-GB)
  1. [C1] of the eyes, to fill with drops of liquid
     - example: My eyes always tear when I cut onions.
     - collocations: eyes tear

#### Entry B

**Lexeme 1: tear**, NOUN
- grammar: countability Count
- said: /ˈtɪɹ/ (en-US), /ˈtɪə/ (en-GB)
  1. [B1] a drop of salty liquid that flows from the eye
     - example: A single tear rolled slowly down his cheek as he read the letter.
     - collocations: in tears; burst into tears; shed a tear; fight back tears; wipe away a tear; close to tears; tears of joy
**Lexeme 2: tear**, VERB
- grammar: irregular True; valency Both
- said: /ˈtɛɹ/ (en-US), /ˈteə/ (en-GB)
- forms: tore [Tense=Past, VerbForm=Fin] /ˈtɔːɹ/ /ˈtɔː/; torn [Tense=Past, VerbForm=Part] /ˈtɔːɹn/ /ˈtɔːn/
  1. [B1] to damage something by pulling it apart, or to become damaged in this way
     - example: Be careful not to tear your coat on that sharp wire fence.
     - frames: tear sth; tear sth into sth
     - collocations: tear a muscle; tear a ligament; tear easily
  2. [B2] to pull someone or something roughly away from where they are
     - example: Strong winds had torn several branches off the old apple trees.
     - frames: tear sb/sth from sth; tear sth off sth; tear sth out of sth
     - grammar: valency Tr
  3. [C1] to move somewhere very quickly and in a wild or careless way
     - example: Two motorcycles tore past the bus stop at high speed.
     - frames: tear past sb/sth; tear down sth; tear along sth
     - grammar: valency Intr; note always followed by an adverb or preposition of movement
**Lexeme 3: tear**, NOUN
- grammar: countability Count
- said: /ˈtɛɹ/ (en-US), /ˈteə/ (en-GB)
  1. [B2] a hole or split in cloth, paper or another material caused by pulling
     - example: You can hardly see the tear in the curtain once it is mended.
     - collocations: mend a tear; repair a tear; a muscle tear

### 3. wind

#### Entry A

**Lexeme 1: wind**, NOUN
- grammar: countability Both
- said: /ˈwɪnd/ (en-US), /ˈwɪnd/ (en-GB)
  1. [A1] air that moves naturally across the surface of the earth
     - example: A cold wind was blowing from the north all afternoon.
     - collocations: strong wind; high wind; light wind; fierce wind; gale-force wind; the wind blows; the wind drops; against the wind
  2. [B2] breath that you need in order to do physical exercise
     - example: Sit down for a minute until you get your wind back.
     - collocations: second wind
     - grammar: countability Mass
  3. [B2] gas in the stomach or bowels that causes discomfort
     - example: Babies often cry after feeding when they have trapped wind.
     - collocations: break wind; trapped wind; suffer from wind
     - regions: en-GB mainly
     - grammar: countability Mass
**Lexeme 2: wind**, VERB
- grammar: irregular True; valency Both
- said: /ˈwaɪnd/ (en-US), /ˈwaɪnd/ (en-GB)
- forms: wound [Tense=Past, VerbForm=Fin,Part] /ˈwaʊnd/ /ˈwaʊnd/
  1. [B2] to follow a curving course with many turns and bends
     - example: A narrow path winds through the woods to the beach.
     - frames: wind through sth; wind along sth; wind around sth
     - collocations: wind its way
     - grammar: valency Intr
  2. [B2] to wrap something long and thin several times around another object
     - example: She wound the warm wool scarf tightly around her neck.
     - frames: wind sth around sth; wind sth round sth; wind sth into sth
     - grammar: valency Tr
     - opposites: unwind
  3. [B2] to turn a key or handle to make a device operate
     - example: Don't forget to wind the bedroom clock before going to sleep.
     - frames: wind sth
     - collocations: wind a clock; wind a watch
     - grammar: valency Tr
- relations: confusable wound (NOUN): its past form wound is spelled the same as wound, an injury
**Lexeme 3: wind**, VERB
- grammar: irregular False; valency Tr; note usually passive
- said: /ˈwɪnd/ (en-US), /ˈwɪnd/ (en-GB)
  1. [C1] to make someone briefly unable to breathe easily, especially after a blow
     - example: I was completely winded after falling hard on my back.
     - frames: wind sb
- relations: derived_from wind (NOUN)

#### Entry B

**Lexeme 1: wind**, NOUN
- grammar: countability Both
- said: /ˈwɪnd/ (en-US), /ˈwɪnd/ (en-GB)
  1. [A2] air that moves quickly across the ground, especially outdoors
     - example: A cold wind was blowing from the north all day.
     - collocations: strong wind; high winds; the wind blows; gust of wind; the wind picks up; the wind drops; against the wind; light wind; cold wind
  2. [C1] air or gas in the stomach or bowels that causes discomfort
     - example: The baby is crying because she has wind.
     - collocations: have wind; break wind
     - regions: en-GB mainly
     - grammar: countability Mass
  3. [C1] the breath you need to run or do hard exercise
     - example: Give me a minute to get my wind back.
     - collocations: get your wind back; second wind
     - grammar: countability Mass
- relations: confusable wind (VERB): the verb wind /waɪnd/, to turn or twist something, is spelled the same
**Lexeme 2: wind**, VERB
- grammar: valency Tr; irregular False
- said: /ˈwɪnd/ (en-US), /ˈwɪnd/ (en-GB)
  1. [C1] to make someone unable to breathe for a short time, usually by a hit
     - example: He was winded by a kick in the stomach.
     - frames: wind sb
     - grammar: note often passive: be winded
  2. [C2] to help a baby let air out of its stomach after feeding
     - example: You should wind the baby after each feed.
     - frames: wind sb
     - regions: en-GB mainly
- relations: derived_from wind (NOUN)
**Lexeme 3: wind**, VERB
- grammar: valency Both; irregular True
- said: /ˈwaɪnd/ (en-US), /ˈwaɪnd/ (en-GB)
- forms: wound [Tense=Past, VerbForm=Fin,Part] /ˈwaʊnd/ /ˈwaʊnd/
  1. [B2] to follow a course with many turns and bends
     - example: A narrow path winds through the forest to the lake.
     - frames: wind through sth; wind along sth; wind up sth; wind down sth
     - collocations: wind its way; river winds; road winds
     - grammar: valency Intr
  2. [B2] to wrap something long around itself or another thing many times
     - example: She wound the wool into a neat ball.
     - frames: wind sth around sth; wind sth round sth; wind sth into sth
     - grammar: valency Tr
  3. [B2] to make a clock or machine work by turning a key or handle
     - example: My grandfather winds his old watch every morning.
     - frames: wind sth
     - collocations: wind a clock; wind a watch
     - grammar: valency Tr
- relations: confusable wound (NOUN): the past form wound /waʊnd/ is spelled like wound /wuːnd/, an injury

### 4. left

#### Entry A

**Lexeme 1: left**, ADJ
- grammar: comparable False
- said: /ˈlɛft/ (en-US), /ˈleft/ (en-GB)
  1. [A1] on or of the side of the body where the heart is
     - example: She broke her left arm when she fell off her bike.
     - collocations: left hand; left side; left foot; left eye; left lane
     - grammar: position Attr
     - opposites: right
  2. [A2] still there or still available after the rest has gone
     - example: There's only a little milk left in the fridge.
     - frames: sth left; have sth left
     - collocations: nothing left; there is no time left; be left over
     - grammar: position Pred; note after a verb or after the noun it describes
**Lexeme 2: left**, ADV
- said: /ˈlɛft/ (en-US), /ˈleft/ (en-GB)
  1. [A1] towards the side of the body where the heart is
     - example: Turn left at the traffic lights and park behind the bank.
     - collocations: turn left; go left; look left; bear left
     - opposites: right
**Lexeme 3: left**, NOUN
- grammar: countability Count
- said: /ˈlɛft/ (en-US), /ˈleft/ (en-GB)
  1. [A1] the side or direction that is on your left hand
     - example: In this photo, my grandmother is the one on the left.
     - collocations: on the left; to the left; on your left; keep to the left; from left to right
     - grammar: number Sing; note usually the left
     - opposites: right
  2. [B2] political parties and people who support socialist ideas and social equality
     - example: The new law was strongly criticized by the left.
     - collocations: the far left; the political left; on the left; the centre left
     - grammar: number Sing; note the left, often written with a capital L
     - opposites: right
  3. [B1] a turn towards the left side when driving or walking
     - example: Take the next left and the station is straight ahead.
     - collocations: take a left; make a left; the next left; the first left
     - labels: formality informal

#### Entry B

**Lexeme 1: left**, ADJ
- grammar: comparable False
- said: /ˈlɛft/ (en-US), /ˈleft/ (en-GB)
  1. [A1] on the side of your body towards the west when you face north
     - example: He writes with his left hand and kicks with both feet.
     - collocations: left hand; left side; left turn; left eye; left arm; left leg
     - grammar: position Attr
     - opposites: right
  2. [A2] still present after the rest has gone or been used
     - example: Hurry up, because we only have ten minutes left.
     - collocations: nothing left; nobody left; time left; money left
     - grammar: note never before a noun
- relations: derived_from leave (VERB), for sense 2 only
**Lexeme 2: left**, ADV
- grammar: comparable False
- said: /ˈlɛft/ (en-US), /ˈleft/ (en-GB)
  1. [A1] towards or on the side opposite to the right
     - example: Turn left at the traffic lights and walk past the bank.
     - collocations: turn left; look left; go left; keep left; bear left
     - opposites: right
**Lexeme 3: left**, NOUN
- grammar: number Sing
- said: /ˈlɛft/ (en-US), /ˈleft/ (en-GB)
  1. [A1] the side or direction opposite to the right
     - example: Our house is the third building on your left.
     - collocations: on the left; to the left; from the left; on your left
     - opposites: right
  2. [B2] people and political groups supporting socialism or greater social equality
     - example: Many voters on the left opposed the tax cuts.
     - collocations: on the left; the political left; the far left
     - grammar: note usually with the, sometimes capitalized
     - opposites: right

### 5. news

#### Entry A

**Lexeme 1: news**, NOUN
- grammar: countability Mass; note takes a singular verb; one item is "a piece of news", not "a news"
- said: /ˈnuːz/ (en-US), /ˈnjuːz/ (en-US, less_common), /ˈnjuːz/ (en-GB)
  1. [A1] new information about something that has happened recently
     - example: I've got some good news: we're having a baby!
     - frames: news of sth; news about sth; news that ...; be news to sb
     - collocations: good news; bad news; a piece of news; break the news; hear the news; have news; great news; news travels fast
  2. [A2] reports of recent events in newspapers, on television or online
     - example: The story was in the news for weeks.
     - collocations: in the news; make the news; hit the news; news story; news report; local news; international news; latest news; breaking news; fake news
  3. [A1] a regular television or radio programme that reports recent events
     - example: Let's watch the news before we go to bed.
     - collocations: watch the news; on the news; the evening news; the six o'clock news; listen to the news
     - grammar: note used with "the"

#### Entry B

**Lexeme 1: news**, NOUN
- grammar: countability Mass; note takes a singular verb
- said: /ˈnuːz/ (en-US), /ˈnjuːz/ (en-GB)
  1. [A1] reports or information about things that have happened recently
     - example: Have you heard any news about your job application yet?
     - frames: news about sb/sth; news of sb/sth; news on sb/sth; news that ...
     - collocations: a piece of news; a bit of news; breaking news; latest news; local news; bad news; hear the news; break the news; in the news
  2. [A2] a regular television or radio broadcast reporting recent events
     - example: We usually watch the evening news together after dinner.
     - collocations: watch the news; on the news; the evening news; read the news
     - grammar: number Sing; note used with the

### 6. pants

#### Entry A

**Lexeme 1: pants**, NOUN
- grammar: number Plur; note takes a plural verb; counted with "a pair of"
- said: /ˈpænts/ (en-US), /ˈpænts/ (en-GB)
  1. [A1] a piece of clothing covering the body from the waist to the ankles, with a part for each leg
     - example: I need to buy some new pants for work.
     - collocations: a pair of pants; wear pants; put on pants; take off pants; long pants; sweat pants
     - regions: en-021 mainly, en-AU mainly
  2. [A2] a piece of underwear covering the bottom and the area between the legs
     - example: Don't forget to pack clean pants and socks for the trip.
     - collocations: a pair of pants; clean pants
     - regions: en-GB mainly
  3. [C2] something that is silly or untrue
     - example: Honestly, everything he said about the project was a load of pants.
     - collocations: a load of pants
     - labels: formality slang
     - regions: en-GB only
     - grammar: countability Mass; note usually in "a load of pants"
**Lexeme 2: pants**, ADJ
- grammar: position Pred, mainly; invariable True
- said: /ˈpænts/ (en-US), /ˈpænts/ (en-GB)
  1. [C2] very bad or of very low quality
     - example: The weather has been absolutely pants all week.
     - collocations: absolutely pants; really pants
     - labels: formality slang
     - regions: en-GB only

#### Entry B

**Lexeme 1: pants**, NOUN
- grammar: number Plur
- said: /ˈpænts/ (en-US), /ˈpænts/ (en-GB)
  1. [A1] outer clothing that covers each leg separately from the waist to the ankles
     - example: I need to buy a pair of warm pants before winter starts.
     - collocations: a pair of pants; dress pants; cargo pants; pants pocket
     - regions: en-021 mainly, en-009 mainly
  2. [A2] a piece of underwear worn on the lower part of the body
     - example: Remember to pack enough socks and pants for the whole week.
     - collocations: a pair of pants
     - regions: en-GB mainly
**Lexeme 2: pants**, ADJ
- grammar: comparable False; position Pred, mainly
- said: /ˈpænts/ (en-US), /ˈpænts/ (en-GB)
  1. [C1] of very poor quality, or not enjoyable
     - example: To be honest, we thought the second half was completely pants.
     - labels: formality informal
     - regions: en-GB only

### 7. biscuit

#### Entry A

**Lexeme 1: biscuit**, NOUN
- grammar: countability Count
- said: /ˈbɪs.kɪt/ (en-US), /ˈbɪs.kɪt/ (en-GB)
  1. [A2] a small flat dry cake, usually sweet, baked until it is crisp
     - example: Would you like a cup of tea and a biscuit?
     - collocations: a packet of biscuits; chocolate biscuit; dunk a biscuit; biscuit tin; dog biscuit; cheese and biscuits
     - regions: en-GB mainly, en-AU mainly, en-IN mainly
  2. [B2] a small soft round bread, often eaten warm with butter or gravy
     - example: My grandmother bakes fresh biscuits for breakfast every Sunday.
     - collocations: biscuits and gravy; buttermilk biscuits
     - regions: en-US mainly

#### Entry B

**Lexeme 1: biscuit**, NOUN
- grammar: countability Count
- said: /ˈbɪs.kɪt/ (en-US), /ˈbɪs.kɪt/ (en-GB)
  1. [A2] a small, flat baked food that is crisp and usually sweet
     - example: Would you like a chocolate biscuit with your cup of tea?
     - collocations: chocolate biscuit; sweet biscuit; a packet of biscuits; biscuit tin; dog biscuit
     - regions: en-GB mainly, en-009 mainly
  2. [B1] a small, soft, round bread roll that is usually eaten warm
     - example: They served hot chicken soup with fresh buttermilk biscuits on the side.
     - collocations: biscuits and gravy; buttermilk biscuit
     - regions: en-021 mainly

### 8. bloody

#### Entry A

**Lexeme 1: bloody**, ADJ
- grammar: comparable True
- said: /ˈblʌd.i/ (en-US), /ˈblʌd.i/ (en-GB)
  1. [B1] covered in or containing the red liquid that flows inside the body
     - example: He came home from the game with a bloody knee.
     - collocations: a bloody nose; bloody hands; bloody clothes
  2. [B2] involving a lot of violence, serious injury and killing
     - example: Thousands of soldiers died in that bloody battle.
     - collocations: a bloody battle; a bloody war; a bloody conflict
  3. [B2] used to express anger or to add strong emphasis
     - example: Where did I put my bloody keys this morning?
     - collocations: a bloody idiot; a bloody nuisance; a bloody shame; a bloody mess
     - labels: formality informal, attitude offensive
     - regions: en-GB mainly, en-IE mainly, en-009 mainly
     - grammar: position Attr; comparable False
- relations: derived_from blood (NOUN)
**Lexeme 2: bloody**, ADV
- grammar: comparable False
- said: /ˈblʌd.i/ (en-US), /ˈblʌd.i/ (en-GB)
  1. [B2] used to add strong emphasis, especially when annoyed or angry
     - example: It is bloody freezing out here without a coat.
     - frames: bloody + adj; bloody + adv
     - collocations: bloody well; bloody good; not bloody likely
     - labels: formality informal, attitude offensive
     - regions: en-GB mainly, en-IE mainly, en-009 mainly
- relations: derived_from bloody (ADJ)
**Lexeme 3: bloody**, VERB
- grammar: valency Tr; irregular False
- said: /ˈblʌd.i/ (en-US), /ˈblʌd.i/ (en-GB)
  1. [C1] to cover or stain something with the red liquid from a wound
     - example: His face was badly bloodied during the street fight.
     - frames: bloody sth
- relations: derived_from bloody (ADJ)

#### Entry B

**Lexeme 1: bloody**, ADJ
- grammar: comparable True
- said: /ˈblʌd.i/ (en-US), /ˈblʌd.i/ (en-GB)
  1. [B2] covered in blood or losing blood
     - example: He came home from the match with a bloody nose.
     - collocations: bloody nose; bloody hands; bloody bandage
  2. [B2] involving a lot of violence, with many people hurt or killed
     - example: The country is still recovering from a long and bloody civil war.
     - collocations: bloody battle; bloody war; bloody conflict; bloody revolution
     - opposites: bloodless
  3. [B2] used to add force to what you say, especially when angry
     - example: I can't find my bloody keys anywhere!
     - collocations: bloody idiot; bloody hell; bloody nuisance
     - labels: formality informal
     - regions: en-GB mainly, en-AU mainly
     - grammar: position Attr; comparable False; note a mild swear word that some people find rude
- relations: adjective_of blood (NOUN)
**Lexeme 2: bloody**, ADV
- said: /ˈblʌd.i/ (en-US), /ˈblʌd.i/ (en-GB)
  1. [B2] used to make what you say stronger, especially when angry
     - example: That was a bloody good film, wasn't it?
     - frames: bloody + adj; bloody + adv
     - collocations: bloody well; bloody good; bloody awful
     - labels: formality informal
     - regions: en-GB mainly, en-AU mainly
     - grammar: note a mild swear word that some people find rude
- relations: derived_from bloody (ADJ)
**Lexeme 3: bloody**, VERB
- grammar: valency Tr; irregular False
- said: /ˈblʌd.i/ (en-US), /ˈblʌd.i/ (en-GB)
  1. [C1] to cover something with blood, usually by injuring it
     - example: A stray punch bloodied his lip during the fight.
     - frames: bloody sth; bloody sb's nose
     - collocations: bloodied but unbowed
- relations: derived_from bloody (ADJ)

### 9. the

#### Entry A

**Lexeme 1: the**, DET
- said: /ˈðiː/ (en-US: alone or stressed), /ðə/ (en-US: weak form, usual in running speech, before a consonant sound), /ði/ (en-US: weak form, usual in running speech, before a vowel sound), /ˈðiː/ (en-GB: alone or stressed), /ðə/ (en-GB: weak form, usual in running speech, before a consonant sound), /ði/ (en-GB: weak form, usual in running speech, before a vowel sound)
  1. [A1] used before a noun to show which particular person or thing you mean
     - example: Could you pass me the salt, please?
     - grammar: note also used before superlatives and some names, for example of rivers and seas
  2. [A1] used before a noun when there is only one of something
     - example: The sun rises much earlier in summer.
     - collocations: the sun; the world; the sky; the moon
  3. [B1] used before a singular noun to talk about a whole type of thing
     - example: The computer has changed the way people work.
  4. [B1] used before an adjective to mean all people of that kind
     - example: The government has promised more help for the poor.
     - frames: the + adj
     - collocations: the rich; the poor; the young; the elderly
  5. [B2] used to say that someone or something is the best or most famous of its kind
     - example: This is the place to eat fresh fish in town.
     - labels: formality informal
     - grammar: note always stressed and said /ðiː/
  6. [B2] used to show the amount that a price or rate is measured in
     - example: Most of the workers here are paid by the hour.
     - collocations: by the hour; by the day; to the gallon
**Lexeme 2: the**, ADV
- said: /ˈðiː/ (en-US: alone or stressed), /ðə/ (en-US: weak form, usual in running speech, before a consonant sound), /ði/ (en-US: weak form, usual in running speech, before a vowel sound), /ˈðiː/ (en-GB: alone or stressed), /ðə/ (en-GB: weak form, usual in running speech, before a consonant sound), /ði/ (en-GB: weak form, usual in running speech, before a vowel sound)
  1. [B1] used before two comparatives to show that one thing changes with another
     - example: The more you practise, the easier it gets.
     - frames: the + comparative, the + comparative
     - collocations: the sooner the better; the more the merrier
  2. [C1] used before a comparative to mean by that much or for that reason
     - example: She felt all the better for her long holiday.
     - frames: the + comparative
     - collocations: all the better; none the wiser; none the worse; so much the better

#### Entry B

**Lexeme 1: the**, DET
- said: /ˈðiː/ (en-US: alone or under stress), /ðə/ (en-US: weak form, usual in running speech before a consonant sound), /ði/ (en-US: weak form, usual in running speech before a vowel sound), /ˈðiː/ (en-GB: alone or under stress), /ðə/ (en-GB: weak form, usual in running speech before a consonant sound), /ði/ (en-GB: weak form, usual in running speech before a vowel sound)
  1. [A1] used to refer to a particular, known or unique person or thing
     - example: Please close the door when you leave the room.
  2. [B1] used to refer to all people or things of a certain type or group
     - example: My sister has played the violin since she was eight.
     - grammar: note used with a singular noun, or with an adjective for a group of people
  3. [B2] used to show that someone or something is best or most famous
     - example: Right now, that little cafe is the place to meet friends.
     - grammar: note pronounced /ˈðiː/ and stressed in speech
**Lexeme 2: the**, ADV
- said: /ˈðiː/ (en-US: alone or under stress), /ðə/ (en-US: weak form, usual in running speech before a consonant sound), /ði/ (en-US: weak form, usual in running speech before a vowel sound), /ˈðiː/ (en-GB: alone or under stress), /ðə/ (en-GB: weak form, usual in running speech before a consonant sound), /ði/ (en-GB: weak form, usual in running speech before a vowel sound)
  1. [B1] used to show that one amount or quality changes as another changes
     - example: The longer you wait to reply, the harder it becomes.
     - collocations: the sooner the better; the more the merrier
     - grammar: note used before each of two comparative adjectives or adverbs
  2. [B2] used to emphasize how much more or less a quality applies
     - example: We felt all the better after a long walk outside.
     - collocations: all the better; none the wiser; so much the better
     - grammar: note used before a comparative adjective or adverb

### 10. used

#### Entry A

**Lexeme 1: used**, ADJ
- grammar: comparable False; position Attr, mainly
- said: /ˈjuːzd/ (en-US), /ˈjuːzd/ (en-GB)
  1. [B1] owned by someone else before, and not new when bought
     - example: We're looking for a cheap used car for our son.
     - collocations: used car; used cars; used clothes; used books
     - opposites: new
  2. [B2] already put to use once, and so dirty or not fit to use again
     - example: Please throw used tissues in the bin straight away.
     - collocations: used needles; used tissues; used batteries; used tea bags
     - opposites: unused
- relations: derived_from use (VERB)
**Lexeme 2: used**, ADJ
- grammar: position Pred
- said: /ˈjuːst/ (en-US), /ˈjuːst/ (en-GB)
  1. [B1] familiar with something because you have done or met it often
     - example: I'm not used to getting up so early in the morning.
     - frames: used to sth; used to doing sth; get used to sth; get used to doing sth
     - collocations: get used to it; become used to; be used to
     - opposites: unused
- relations: confusable used to (AUX): used to do sth speaks of past habits; be used to doing sth means be familiar with it

#### Entry B

**Lexeme 1: used**, ADJ
- grammar: position Pred; comparable True
- said: /ˈjuːst/ (en-US), /ˈjuːst/ (en-GB)
  1. [B1] familiar with something because you have often done or experienced it
     - example: I grew up near a railway, so I'm used to the noise.
     - frames: used to sb/sth; used to doing sth; get used to sb/sth; get used to doing sth
     - opposites: unused
- relations: derived_from use (VERB)
**Lexeme 2: used**, ADJ
- grammar: position Attr, mainly; comparable False
- said: /ˈjuːzd/ (en-US), /ˈjuːzd/ (en-GB)
  1. [A2] not new and having belonged to someone else before
     - example: She bought a reliable used car instead of a new one.
     - collocations: used car; used book; used clothing; used furniture; buy used
     - opposites: new
- relations: derived_from use (VERB)
