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
     - example: Follow me and I'll lead you to the exit.
     - frames: lead sb to sth; lead sb into sth
     - collocations: lead the way
     - grammar: valency Tr
  2. [B1] to be in charge of a group of people or an organization
     - example: She has led the research team for ten years.
     - frames: lead sth
     - collocations: lead a team; lead a group; lead a discussion; lead a campaign
     - grammar: valency Tr
  3. [B1] to go in a direction or reach a particular place
     - example: Which of these narrow stone paths leads down to the beach?
     - frames: lead to sth; lead into sth
     - grammar: valency Intr
  4. [B1] to cause something to happen or cause someone to do something
     - example: Years of poor diet can lead to serious health problems.
     - frames: lead to sth; lead sb to do sth
     - grammar: valency Both
  5. [B2] to be winning in a race, game or competition
     - example: We were leading by two goals at half-time.
     - frames: lead by sth; lead sth
     - collocations: lead the race; lead the field
     - grammar: valency Both
     - opposites: trail
  6. [B2] to have a particular kind of life
     - example: They moved to the countryside to lead a quiet life.
     - frames: lead sth
     - collocations: lead a quiet life; lead a normal life; lead a busy life; lead a double life
     - grammar: valency Tr
**Lexeme 2: lead**, NOUN
- grammar: countability Count
- said: /ˈliːd/ (en-US), /ˈliːd/ (en-GB)
  1. [B1] the first position in a race, game or competition
     - example: Our runner took the lead on the final lap.
     - collocations: take the lead; in the lead; hold the lead; lose the lead; extend the lead; a narrow lead; a comfortable lead; follow someone's lead
     - grammar: number Sing, mainly
  2. [B2] the main part in a play, film or show, or the person playing it
     - example: Who is playing the female lead in their new musical?
     - collocations: play the lead; the male lead; the female lead; take the lead
  3. [B2] a piece of information that may help solve a crime or find something
     - example: Detectives are following up several promising new leads in the case.
     - collocations: follow a lead; follow up a lead; a promising lead
  4. [B2] a long strap or chain fastened to a dog's collar
     - example: Please keep your dog on a lead near the road.
     - collocations: on a lead; off the lead
     - regions: en-GB mainly, en-009 mainly
  5. [B2] a wire covered in plastic that connects an electrical piece of equipment
     - example: Have you seen the power lead for my old laptop?
     - collocations: an extension lead; a power lead
     - regions: en-GB mainly, en-009 mainly
- relations: derived_from lead (VERB)
**Lexeme 3: lead**, ADJ
- grammar: position Attr; comparable False
- said: /ˈliːd/ (en-US), /ˈliːd/ (en-GB)
  1. [B2] most important, or in the first position among several people or things
     - example: She became the lead singer of a popular rock band.
     - collocations: lead singer; lead role; lead author; lead guitar
- relations: derived_from lead (NOUN)
**Lexeme 4: lead**, NOUN
- said: /ˈlɛd/ (en-US), /ˈled/ (en-GB)
  1. [B2] a soft heavy grey metal, once used for water pipes and paint
     - example: Old houses sometimes still have water pipes made of lead.
     - collocations: lead pipe; lead poisoning; lead paint; unleaded petrol
     - grammar: countability Mass
  2. [B2] the thin dark grey stick inside a pencil that makes marks
     - example: Press gently on the paper or the pencil lead will snap.
     - collocations: pencil lead
     - grammar: countability Both
- relations: confusable lead (VERB): sounds like led, the past form of the verb lead

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

**Lexeme 1: tear**, VERB
- grammar: irregular True; valency Both
- said: /ˈtɛɹ/ (en-US), /ˈteə/ (en-GB)
- forms: tore [Tense=Past, VerbForm=Fin] /ˈtɔːɹ/ /ˈtɔː/; torn [Tense=Past, VerbForm=Part] /ˈtɔːɹn/ /ˈtɔːn/
  1. [B1] to pull paper or cloth apart into pieces, or come apart when pulled
     - example: Be careful not to tear your jacket on the fence.
     - frames: tear sth; tear sth in half; tear sth into sth
     - collocations: tear in half; tear to pieces; tear to shreds; tear open; tear a page
  2. [B2] to injure a muscle or other body part by stretching it too far
     - example: She tore a muscle in her leg during the race.
     - frames: tear sth
     - collocations: tear a muscle; tear a ligament; tear a tendon
     - grammar: valency Tr
  3. [B2] to pull something away from a place quickly and with force
     - example: He tore the old poster off the bedroom wall.
     - frames: tear sth from sth; tear sth off sth; tear sth out of sth; tear yourself away
     - collocations: tear your eyes away; tear yourself away
     - grammar: valency Tr
  4. [C1] to move somewhere very fast in an uncontrolled or excited way
     - example: Two children tore down the stairs into the garden.
     - frames: tear + adv
     - collocations: tear down the road; tear along; tear past
     - grammar: valency Intr
**Lexeme 2: tear**, NOUN
- grammar: countability Count
- said: /ˈtɪɹ/ (en-US), /ˈtɪə/ (en-GB)
  1. [B1] a drop of clear salty liquid that comes from your eye when crying
     - example: A single tear rolled slowly down his pale cheek.
     - collocations: burst into tears; in tears; shed a tear; wipe away a tear; fight back tears; hold back tears; close to tears; move to tears
**Lexeme 3: tear**, NOUN
- grammar: countability Count
- said: /ˈtɛɹ/ (en-US), /ˈteə/ (en-GB)
  1. [B2] a hole or split where paper, cloth or skin has been pulled apart
     - example: There is a small tear in the left sleeve.
     - frames: tear in sth
     - collocations: wear and tear; mend a tear; a small tear
- relations: derived_from tear (VERB)
**Lexeme 4: tear**, VERB
- grammar: irregular False; valency Intr
- said: /ˈtɪɹ/ (en-US), /ˈtɪə/ (en-GB)
  1. [C1] to fill with drops of clear salty liquid from crying or irritation
     - example: My eyes always tear in the cold winter wind.
     - grammar: note usually said of the eyes
- relations: derived_from tear (NOUN)

### 3. wind

#### Entry A

**Lexeme 1: wind**, NOUN
- grammar: countability Both
- said: /ˈwɪnd/ (en-US), /ˈwɪnd/ (en-GB)
  1. [A1] air that moves quickly as a result of natural forces
     - example: Strong winds blew several trees down during the night.
     - collocations: strong wind; high wind; light wind; cold wind; gust of wind; wind blows; into the wind; against the wind
  2. [B2] breath needed for doing physical exercise or playing an instrument
     - example: The heavy blow knocked the wind out of him.
     - collocations: get your wind back; knock the wind out of
     - grammar: countability Mass
  3. [C1] gas in the stomach or bowels that causes discomfort
     - example: Warm water can help when the baby has wind.
     - collocations: break wind; pass wind; suffer from wind
     - regions: en-GB mainly
     - grammar: countability Mass
  4. [C1] musical instruments played by blowing air through them, or their players
     - example: The piece is scored for strings, percussion and wind.
     - collocations: wind instrument; wind band; wind section
     - labels: domain music
     - grammar: countability Mass
**Lexeme 2: wind**, VERB
- grammar: irregular True; valency Both
- said: /ˈwaɪnd/ (en-US), /ˈwaɪnd/ (en-GB)
- forms: wound [Tense=Past, VerbForm=Fin,Part] /ˈwaʊnd/ /ˈwaʊnd/
  1. [B2] to have many bends and turns along its course
     - example: A narrow path winds up the side of the mountain.
     - frames: wind through sth; wind along sth; wind around sth
     - collocations: wind its way
     - grammar: valency Intr
  2. [B2] to wrap or twist something long several times around something else
     - example: She wound a thick woollen scarf around her neck.
     - frames: wind sth around sth; wind sth into sth
     - collocations: wind into a ball
     - grammar: valency Tr
     - opposites: unwind
  3. [B2] to make a clock or machine work by turning a key or handle
     - example: My grandfather still winds his pocket watch every evening.
     - frames: wind sth
     - collocations: wind a watch; wind a clock
     - grammar: valency Tr
  4. [B2] to turn a handle or wheel so that something moves
     - example: Could you wind the car window down a little?
     - frames: wind sth + adv
     - collocations: wind down the window; wind up the window
     - grammar: valency Tr
- relations: confusable wound (VERB): the past form wound is spelled the same as wound /wuːnd/, to injure
**Lexeme 3: wind**, VERB
- grammar: irregular False; valency Tr
- said: /ˈwɪnd/ (en-US), /ˈwɪnd/ (en-GB)
  1. [C1] to make someone temporarily unable to breathe easily, especially by hitting them
     - example: She was badly winded when she fell off her bicycle.
     - frames: wind sb
     - grammar: note usually in the passive
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
- said: /ˈlɛft/ (en-US), /ˈleft/ (en-GB)
  1. [A1] on or towards the side of your body where your heart is
     - example: She writes with her left hand and plays guitar with her right.
     - collocations: left hand; left side; left turn; left eye; left leg
     - grammar: comparable False; position Attr
     - opposites: right
  2. [A2] still remaining after the rest has gone or been used
     - example: Is there any coffee left in the pot for me?
     - frames: left to do sth; left of sth
     - collocations: time left; nothing left; little left; any left; left over
     - grammar: comparable False; position Pred; note also used straight after a noun or pronoun
  3. [B2] supporting greater social equality and public control of wealth
     - example: His views have grown more left since he started teaching.
     - frames: left of sb/sth
     - grammar: comparable True
     - opposites: right
- relations: derived_from leave (VERB), for sense 2 only
**Lexeme 2: left**, ADV
- grammar: comparable False
- said: /ˈlɛft/ (en-US), /ˈleft/ (en-GB)
  1. [A1] towards or onto the side where your heart is
     - example: Turn left at the traffic lights and walk past the bank.
     - collocations: turn left; look left; keep left; bear left
     - opposites: right
**Lexeme 3: left**, NOUN
- said: /ˈlɛft/ (en-US), /ˈleft/ (en-GB)
  1. [A1] the side or direction where your heart is when facing forward
     - example: You will see the post office on your left.
     - collocations: on the left; on your left; to the left; from the left
     - grammar: number Sing
     - opposites: right
  2. [B1] a turn towards the side where your heart is
     - example: Take the second left after the bridge to reach the station.
     - collocations: take a left; make a left
     - grammar: countability Count
     - opposites: right
  3. [B2] political groups and people favouring greater social equality and public ownership
     - example: The proposal drew strong support from the political left.
     - collocations: the political left; on the left; the far left
     - grammar: number Sing; note usually with the; sometimes capitalized
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
  1. [A1] information or reports about recent events or things not known before
     - example: We are still waiting for news about our application.
     - frames: news about sb/sth; news of sb/sth; news that ...
     - collocations: a piece of news; break the news; in the news; breaking news; the latest news; fake news; local news
  2. [A2] a regular television or radio broadcast that reports recent events
     - example: I heard it on the news while driving home.
     - collocations: on the news; watch the news; the evening news
     - grammar: note usually with the

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
  1. [A1] an outer piece of clothing covering the body from the waist to the ankles
     - example: He wore a white shirt and dark blue pants to the interview.
     - collocations: a pair of pants; put on pants; take off pants; dress pants; cargo pants; sweat pants
     - regions: en-021 mainly, en-AU mainly
  2. [A2] a piece of underwear covering the body from the waist to the top of the legs
     - example: Remember to pack clean socks and pants for the weekend trip.
     - collocations: a pair of pants; clean pants
     - regions: en-GB mainly
**Lexeme 2: pants**, ADJ
- grammar: comparable False; position Pred
- said: /ˈpænts/ (en-US), /ˈpænts/ (en-GB)
  1. [C2] of very poor quality, or not enjoyable at all
     - example: That film we watched last night was completely pants.
     - labels: formality slang
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
  1. [A2] a small flat dry sweet cake baked until crisp
     - example: Would you like a chocolate biscuit with your cup of tea?
     - collocations: chocolate biscuit; packet of biscuits; biscuit tin; tea and biscuits; take the biscuit
     - regions: en-GB mainly, en-009 mainly
  2. [B1] a small soft round bread roll eaten warm with meals
     - example: She served hot biscuits with butter alongside the fried chicken.
     - collocations: buttermilk biscuit; biscuits and gravy
     - regions: en-021 mainly

### 8. bloody

#### Entry A

**Lexeme 1: bloody**, ADJ
- grammar: comparable True
- said: /ˈblʌd.i/ (en-US), /ˈblʌd.i/ (en-GB)
  1. [B1] covered with or full of red liquid from a wound
     - example: He came home from the park with a bloody nose.
     - collocations: bloody nose; bloody hands; bloody clothes
  2. [B2] used to express anger, annoyance or strong feeling about something
     - example: Where did I put my bloody keys this morning?
     - collocations: bloody hell; bloody fool
     - labels: formality informal, attitude offensive
     - regions: en-GB mainly, en-009 mainly
     - grammar: position Attr; comparable False
  3. [B2] involving a lot of fighting, killing and serious injury
     - example: Thousands of soldiers died in the bloody battle near the river.
     - collocations: bloody battle; bloody war; bloody conflict
- relations: adjective_of blood (NOUN), for sense 1, 3 only; derived_from blood (NOUN), for sense 2 only
**Lexeme 2: bloody**, ADV
- grammar: comparable False
- said: /ˈblʌd.i/ (en-US), /ˈblʌd.i/ (en-GB)
  1. [B2] used to emphasize a statement or show anger or annoyance
     - example: It was bloody freezing waiting at the station last night.
     - collocations: bloody well; bloody awful; bloody good; not bloody likely
     - labels: formality informal, attitude offensive
     - regions: en-GB mainly, en-009 mainly
- relations: derived_from bloody (ADJ)
**Lexeme 3: bloody**, VERB
- grammar: valency Tr; irregular False
- said: /ˈblʌd.i/ (en-US), /ˈblʌd.i/ (en-GB)
  1. [C2] to hit someone hard enough to make a wound bleed
     - example: The heavy fall on the ice bloodied both his knees.
     - frames: bloody sth
     - collocations: bloody someone's nose
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
- said: /ˈðiː/ (en-US: under stress or said alone), /ðə/ (en-US: weak form, usual in running speech, before a consonant sound), /ði/ (en-US: weak form, usual in running speech, before a vowel sound), /ˈðiː/ (en-GB: under stress or said alone), /ðə/ (en-GB: weak form, usual in running speech, before a consonant sound), /ði/ (en-GB: weak form, usual in running speech, before a vowel sound)
  1. [A1] used to refer to a particular person or thing that is known or unique
     - example: Please close the window before you leave the room.
  2. [A2] used to refer to a whole type of animal, machine or musical instrument
     - example: My sister learned to play the violin at school.
     - grammar: note with a singular countable noun
  3. [B1] used to refer to all people of a particular group or condition
     - example: New laws will help protect the elderly and poor.
     - frames: the + adj
     - grammar: note takes a plural verb
**Lexeme 2: the**, ADV
- said: /ˈðiː/ (en-US: under stress or said alone), /ðə/ (en-US: weak form, usual in running speech, before a consonant sound), /ði/ (en-US: weak form, usual in running speech, before a vowel sound), /ˈðiː/ (en-GB: under stress or said alone), /ðə/ (en-GB: weak form, usual in running speech, before a consonant sound), /ði/ (en-GB: weak form, usual in running speech, before a vowel sound)
  1. [B1] used to show that one amount or quality changes together with another
     - example: The sooner we leave, the earlier we can get home.
     - collocations: the sooner the better; the more the merrier
     - grammar: note used before each of two comparatives
  2. [B2] used to show that someone or something is more so as a result
     - example: You will feel all the better after some fresh air.
     - collocations: all the better; all the more; none the worse; so much the better
     - grammar: note used before a comparative

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
  1. [A2] familiar with something because you have often seen or done it
     - example: You will soon get used to waking up early.
     - frames: used to sth; used to doing sth
     - collocations: be used to; get used to; become used to; grow used to
     - opposites: unused
- relations: derived_from use (VERB)
**Lexeme 2: used**, ADJ
- grammar: position Attr; comparable False
- said: /ˈjuːzd/ (en-US), /ˈjuːzd/ (en-GB)
  1. [A2] owned by someone else before and not new
     - example: She bought a reliable used car from a local dealer.
     - collocations: used car; used books; used clothes
     - opposites: new, unused
- relations: derived_from use (VERB)
