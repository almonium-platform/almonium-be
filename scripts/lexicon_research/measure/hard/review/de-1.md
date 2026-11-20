# German: two entries for each word (11 words)

Each word below has two entries of a learner's dictionary of German, A and B, written by two different writers
under the same rules. You are not told which writer wrote which, and the sides change from word to word. Some of
these words are hard on purpose: two words under one spelling, forms that are not words of their own, senses of
one country only, rude words.

For each word say which entry serves a learner better, and name the faults of each. A writer may have refused a
word as no dictionary word of its own ("refused: inflected form of ..."); that is right for an inflected form, an
abbreviation or a name, and wrong for a word a dictionary of German lists.

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
  sense, or an example is not natural German.
- **collocation**: a collocation that is not said that way in German; or one that does not belong
  among the ten a learner is shown for the sense, because anyone would build it from the meanings
  or because it is rare; or a list that lacks the commonest partner of the sense while it has
  rarer ones. Quote the collocation in "what". A frame with the wrong preposition or case is a
  fact, not this.

These are this dictionary's own definitions, not faults:

- valency Refl stands for every use with a reflexive pronoun, whatever a grammar of German calls it
  (reflexive, reciprocal, pronominal); Tr means the verb takes a direct object, so a verb whose
  object needs a preposition or the dative is Intr.
- a grammar value (countability, valency, position, number) says how the word is ordinarily used, as
  a learner's dictionary of German prints it: "Mass" is "uncountable" in that sense, although kinds
  of the thing can be counted. Followed by "mainly", the other use is common too. Report a value
  only when a learner's dictionary would not print it for this word or sense.
- position Attr means the adjective is used joined to a noun and not after a verb such as "be",
  wherever German puts it (before the noun in English, mostly after it in French); Pred means only
  after such a verb. Neither says on which side of the noun the adjective stands.
- a lexeme is one word with one way of saying and inflecting it. Two homonyms that are said and
  inflected alike are senses of one lexeme here, although dictionaries of German number them apart.
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

### 1. Band

#### Entry A

**Lexeme 1: Band**, NOUN
- grammar: gender Neut; plural_class -¨er; countability Count
- said: /ˈbant/ (de-DE)
- forms: Bandes [Case=Gen, Number=Sing] /ˈban.dəs/; Bänder [Case=Nom, Number=Plur] /ˈbɛn.dɐ/
  1. [B1] schmaler, langer Streifen aus Stoff oder Kunststoff zum Binden oder Schmücken
     - example: Sie bindet ein rotes Band um das kleine Geschenk.
     - collocations: ein Band binden; das Band durchschneiden; ein Band im Haar tragen
  2. [B2] Anlage mit einem laufenden Streifen, die Dinge in der Fabrik weiterbefördert
     - example: In der Fabrik steht er jeden Tag acht Stunden am Band.
     - collocations: am Band arbeiten; am laufenden Band; das Band läuft
  3. [B2] Streifen, auf dem Töne, Bilder oder Daten gespeichert werden
     - example: Der Anrufbeantworter hat das ganze Gespräch auf Band aufgenommen.
     - collocations: auf Band aufnehmen; auf Band sprechen
  4. [C1] festes Gewebe im Körper, das Knochen in einem Gelenk verbindet
     - example: Beim Fußballspielen hat er sich ein Band im Knie gerissen.
     - collocations: sich ein Band reißen; sich die Bänder dehnen
     - labels: domain anatomy
**Lexeme 2: Band**, NOUN
- grammar: gender Neut; plural_class -e; countability Count; number Plur, mainly
- said: /ˈbant/ (de-DE)
- forms: Bandes [Case=Gen, Number=Sing] /ˈban.dəs/; Bande [Case=Nom, Number=Plur] /ˈban.də/
  1. [C1] enge Beziehung, die Menschen innerlich miteinander verbindet
     - example: Zwischen den beiden Familien bestehen seit vielen Jahren enge Bande.
     - collocations: zarte Bande knüpfen; familiäre Bande; die Bande der Freundschaft; enge Bande
     - labels: style elevated
- relations: confusable Bande (NOUN): the plural Bande looks like die Bande, a gang
**Lexeme 3: Band**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈbant/ (de-DE)
- forms: Bandes [Case=Gen, Number=Sing] /ˈban.dəs/; Bände [Case=Nom, Number=Plur] /ˈbɛn.də/
  1. [B2] einzelnes Buch, das zu einer Reihe oder einem größeren Werk gehört
     - example: Die Bibliothek besitzt alle zwölf Bände dieses alten Lexikons.
     - collocations: Bände sprechen; der erste Band; in drei Bänden erscheinen
**Lexeme 4: Band**, NOUN
- grammar: gender Fem; plural_class -s; countability Count
- said: /ˈbɛnt/ (de-DE)
- forms: Band [Case=Gen, Number=Sing] /ˈbɛnt/; Bands [Case=Nom, Number=Plur] /ˈbɛnts/
  1. [A2] Gruppe von Musikern, die zusammen vor allem Pop, Rock oder Jazz spielen
     - example: Mein Bruder spielt seit zwei Jahren Gitarre in einer Band.
     - collocations: in einer Band spielen; eine Band gründen; die Band tritt auf; eine Band live sehen

#### Entry B

**Lexeme 1: Band**, NOUN
- grammar: gender Neut; plural_class -¨er; countability Count
- said: /ˈbant/ (de-DE)
- forms: Bandes [Case=Gen, Number=Sing] /ˈban.dəs/; Bands [Case=Gen, Number=Sing] /ˈbants/; Bänder [Case=Nom, Number=Plur] /ˈbɛn.dɐ/
  1. [B1] langer, schmaler Streifen aus Stoff, Papier oder einem anderen biegsamen Material
     - example: Sie hat ein rotes Band um das kleine Geschenk gewickelt.
     - collocations: ein rotes Band; ein Band durchschneiden
  2. [B2] bewegliche Anlage in einer Fabrik, die Teile von Arbeitsplatz zu Arbeitsplatz transportiert
     - example: In der Fabrik rollen täglich hunderte neue Autos vom Band.
     - collocations: am laufenden Band; vom Band laufen
  3. [B2] Streifen mit einer Schicht zur Aufnahme von Ton oder Bild
     - example: Das ganze Gespräch wurde damals heimlich auf Band aufgenommen.
     - collocations: etwas auf Band aufnehmen; auf Band sprechen
  4. [C1] fester Strang aus Gewebe im Körper, der Knochen oder Gelenke zusammenhält
     - example: Beim Fußballspielen hat er sich ein Band im Knie gerissen.
     - collocations: sich ein Band dehnen; sich ein Band reißen
- relations: derived_from binden (VERB)
**Lexeme 2: Band**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈbant/ (de-DE)
- forms: Bandes [Case=Gen, Number=Sing] /ˈban.dəs/; Bands [Case=Gen, Number=Sing] /ˈbants/; Bände [Case=Nom, Number=Plur] /ˈbɛn.də/
  1. [B1] einzelnes gebundenes Buch, besonders als Teil eines größeren mehrteiligen Werkes
     - example: Der dritte Band dieser spannenden Reihe erscheint erst im Herbst.
     - collocations: der erste Band; in mehreren Bänden erscheinen; Bände sprechen
- relations: derived_from binden (VERB)
**Lexeme 3: Band**, NOUN
- grammar: gender Fem; plural_class -s; countability Count
- said: /ˈbɛnt/ (de-DE), /ˈbant/ (de-DE, older)
- forms: Band [Case=Gen, Number=Sing] /ˈbɛnt/; Bands [Case=Nom, Number=Plur] /ˈbɛnts/
  1. [A2] Gruppe von Musikern, die gemeinsam moderne Musik wie Rock, Pop oder Jazz spielt
     - example: Unsere Band spielt am Samstagabend in einer kleinen Kneipe.
     - collocations: eine Band gründen; in einer Band spielen; eine bekannte Band
**Lexeme 4: Band**, NOUN
- grammar: gender Neut; plural_class -e; countability Count; number Plur, mainly
- said: /ˈbant/ (de-DE)
- forms: Bandes [Case=Gen, Number=Sing] /ˈban.dəs/; Bands [Case=Gen, Number=Sing] /ˈbants/; Bande [Case=Nom, Number=Plur] /ˈban.də/
  1. [C1] enge innere Beziehung oder Verbundenheit zwischen einzelnen Menschen oder ganzen Gruppen
     - example: Durch den langen Streit sind alle familiären Bande endgültig zerrissen.
     - collocations: familiäre Bande; Bande der Freundschaft; enge Bande knüpfen
     - labels: style elevated, figurative True
- relations: derived_from binden (VERB)

### 2. See

#### Entry A

**Lexeme 1: See**, NOUN
- grammar: gender Masc; plural_class -n; countability Count; declension mixed
- said: /ˈzeː/ (de-DE)
- forms: Sees [Case=Gen, Number=Sing] /ˈzeːs/; Seen [Case=Nom, Number=Plur] /ˈzeː.ən/
  1. [A1] größeres stehendes Gewässer auf dem Festland, das ringsum von Land umgeben ist
     - example: Im Sommer schwimmen wir fast jeden Abend in dem kühlen See.
     - collocations: im See baden; im See schwimmen; an den See fahren; ein tiefer See; ein klarer See
**Lexeme 2: See**, NOUN
- grammar: gender Fem; number Sing; countability Mass
- said: /ˈzeː/ (de-DE)
- forms: See [Case=Gen, Number=Sing] /ˈzeː/
  1. [A2] große zusammenhängende Fläche aus Salzwasser, die das Festland umgibt
     - example: Bei starkem Sturm fahren die kleinen Fischerboote nicht auf die offene See hinaus.
     - collocations: in See stechen; auf hoher See; die offene See; zur See fahren; an die See fahren; raue See; ruhige See

#### Entry B

**Lexeme 1: See**, NOUN
- grammar: gender Masc; plural_class -n; countability Count
- said: /ˈzeː/ (de-DE)
- forms: Sees [Case=Gen, Number=Sing] /ˈzeːs/; Seen [Case=Nom, Number=Plur] /ˈzeː.ən/
  1. [A1] größeres stehendes Gewässer, das ganz von Land umgeben ist
     - example: Im Sommer schwimmen wir fast jeden Tag im See.
     - collocations: im See baden; im See schwimmen; ein zugefrorener See; ein künstlicher See; ein tiefer See; auf dem See rudern
**Lexeme 2: See**, NOUN
- grammar: gender Fem; number Sing; countability Mass
- said: /ˈzeː/ (de-DE)
- forms: See [Case=Gen, Number=Sing] /ˈzeː/
  1. [B1] großes Gewässer aus salzigem Wasser, das weite Teile der Erde bedeckt
     - example: Mein Großvater ist viele Jahre lang zur See gefahren.
     - collocations: auf See; auf hoher See; zur See fahren; an die See fahren; in See stechen; raue See; ruhige See

### 3. umfahren

#### Entry A

**Lexeme 1: umfahren**, VERB
- grammar: conjugation strong; auxiliary have; valency Tr; irregular True; separable False
- said: /ʊm.ˈfaː.ʁən/ (de-DE)
- forms: umfährt [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ʊm.ˈfɛːɐt/; umfuhr [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ʊm.ˈfuːɐ/; umfahren [Tense=Past, VerbForm=Part] /ʊm.ˈfaː.ʁən/
  1. [B2] sich mit einem Verkehrsmittel in einem Bogen um etwas herum bewegen
     - example: Wegen der Sperrung umfährt der Bus heute die gesamte Innenstadt.
     - frames: etw. umfahren
     - collocations: einen Stau umfahren; eine Baustelle umfahren; ein Hindernis umfahren; weiträumig umfahren; ein Kap umfahren
- relations: derived_from fahren (VERB)
**Lexeme 2: umfahren**, VERB
- grammar: conjugation strong; auxiliary have; valency Tr; irregular True; separable True; separable_prefix um
- said: /ˈʊm.faː.ʁən/ (de-DE)
- forms: fährt um [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈfɛːɐt ˈʊm/; fuhr um [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈfuːɐ ˈʊm/; umgefahren [Tense=Past, VerbForm=Part] /ˈʊm.ɡə.faː.ʁən/
  1. [B2] mit einem Verkehrsmittel so gegen eine Person oder Sache stoßen, dass sie umfällt
     - example: Beim Einparken fuhr der Lastwagen aus Versehen ein altes Straßenschild um.
     - frames: jmdn./etw. umfahren
     - collocations: ein Schild umfahren; einen Pfosten umfahren; einen Fußgänger umfahren; einen Radfahrer umfahren
- relations: derived_from fahren (VERB)

#### Entry B

**Lexeme 1: umfahren**, VERB
- grammar: conjugation strong; irregular True; auxiliary have; valency Tr; separable True; separable_prefix um; note stressed on um- and separable; compare inseparable umfáhren, to go around
- said: /ˈʊm.faː.ʁən/ (de-DE)
- forms: fährt um [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈfɛːɐt ˈʊm/; fuhr um [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈfuːɐ ˈʊm/; umgefahren [Tense=Past, VerbForm=Part] /ˈʊm.ɡə.faː.ʁən/
  1. [B2] mit einem Auto, Rad oder Ähnlichem jemanden oder etwas anstoßen und umwerfen
     - example: Pass auf, sonst fährst du noch das Schild um!
     - frames: jmdn./etw. umfahren
     - collocations: einen Fußgänger umfahren; ein Verkehrsschild umfahren
- relations: derived_from fahren (VERB)
**Lexeme 2: umfahren**, VERB
- grammar: conjugation strong; irregular True; auxiliary have; valency Tr; separable False; note stressed on -fah- and not separable; compare separable úmfahren, to knock down
- said: /ʊm.ˈfaː.ʁən/ (de-DE)
- forms: umfährt [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ʊm.ˈfɛːɐt/; umfuhr [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ʊm.ˈfuːɐ/; umfahren [Tense=Past, VerbForm=Part] /ʊm.ˈfaː.ʁən/
  1. [B2] sich mit einem Auto, Schiff oder Ähnlichem im Bogen um etwas herum bewegen
     - example: Wegen der Baustelle sollten Autofahrer die Innenstadt heute weiträumig umfahren.
     - frames: etw. umfahren
     - collocations: einen Stau umfahren; ein Hindernis umfahren; weiträumig umfahren; die Stadt umfahren
- relations: derived_from fahren (VERB)

### 4. modern

#### Entry A

**Lexeme 1: modern**, ADJ
- grammar: comparable True
- said: /mo.ˈdɛʁn/ (de-DE)
  1. [A2] der heutigen Zeit und dem neuesten Stand der Entwicklung entsprechend
     - example: Das Krankenhaus wurde im letzten Jahr mit moderner Technik ausgestattet.
     - collocations: moderne Technik; moderne Kunst; moderne Medizin; modern eingerichtet
     - opposites: veraltet, altmodisch
  2. [A2] dem aktuellen Geschmack entsprechend und bei vielen Menschen gerade beliebt
     - example: Weite Hosen und kräftige Farben sind in diesem Sommer wieder modern.
     - opposites: unmodern, altmodisch
**Lexeme 2: modern**, VERB
- grammar: conjugation weak; auxiliary have; valency Intr; irregular False
- said: /ˈmoː.dɐn/ (de-DE)
- forms: moderte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈmoː.dɐ.tə/; gemodert [Tense=Past, VerbForm=Part] /ɡə.ˈmoː.dɐt/
  1. [C1] durch Feuchtigkeit langsam faulen und sich allmählich zersetzen
     - example: Unter dem dichten Teppich aus nassem Laub moderte ein alter Baumstamm.
- relations: derived_from Moder (NOUN); confusable modern (ADJ): spelled the same as the adjective modern, which is stressed on the second syllable

#### Entry B

**Lexeme 1: modern**, ADJ
- grammar: comparable True
- said: /mo.ˈdɛʁn/ (de-DE)
  1. [A2] dem neuesten Stand der Technik oder der Entwicklung entsprechend
     - example: Unsere Schule hat endlich moderne Computer für alle Klassen bekommen.
     - collocations: moderne Technik; modern eingerichtet; eine moderne Wohnung; moderne Methoden; die moderne Medizin
     - opposites: veraltet
  2. [A2] so, wie es gerade die Mode oder der Geschmack der Zeit verlangt
     - example: Meine Großmutter kleidet sich überraschend modern und jugendlich.
     - collocations: modern gekleidet; sich modern kleiden; moderne Frisur
     - opposites: altmodisch
  3. [B1] zur Gegenwart oder zur neueren Zeit gehörend, nicht zur Vergangenheit
     - example: Im zweiten Stock zeigt das Museum vor allem moderne Kunst.
     - collocations: moderne Kunst; moderne Literatur; moderne Musik; die moderne Gesellschaft; moderne Architektur
     - grammar: position Attr, mainly
- relations: confusable modern (VERB): same spelling, but the verb is stressed on the first syllable /ˈmoː.dɐn/ and means to rot
**Lexeme 2: modern**, VERB
- grammar: conjugation weak; auxiliary have; valency Intr; irregular False
- said: /ˈmoː.dɐn/ (de-DE)
- forms: moderte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈmoː.dɐ.tə/; gemodert [Tense=Past, VerbForm=Part] /ɡə.ˈmoː.dɐt/
  1. [C1] durch Feuchtigkeit langsam faulen und dabei muffig riechen
     - example: Im feuchten Keller modert das alte Holz langsam vor sich hin.
     - collocations: vor sich hin modern
     - labels: style literary
- relations: derived_from Moder (NOUN); confusable modern (ADJ): same spelling, but the adjective is stressed on the second syllable /mo.ˈdɛʁn/ and means up to date

### 5. Wort

#### Entry A

**Lexeme 1: Wort**, NOUN
- grammar: gender Neut; plural_class -¨er; countability Count; note plural Wörter for single words, Worte for utterances and statements
- said: /ˈvɔʁt/ (de-DE)
- forms: Wortes [Case=Gen, Number=Sing] /ˈvɔʁ.təs/; Worts [Case=Gen, Number=Sing] /ˈvɔʁts/; Wörter [Case=Nom, Number=Plur] /ˈvœʁ.tɐ/; Worte [Case=Nom, Number=Plur] /ˈvɔʁ.tə/
  1. [A1] kleinste selbstständige Einheit der Sprache mit eigener Bedeutung
     - example: Ich schlage jedes unbekannte Wort sofort im Wörterbuch nach.
     - frames: Wort für + Akk
     - collocations: ein Wort nachschlagen; ein Wort buchstabieren; ein Wort aussprechen; Wort für Wort
  2. [B1] Äußerung, mit der jemand etwas mündlich oder schriftlich ausdrückt
     - example: Mit seinen freundlichen Worten hat er mich sehr getröstet.
     - collocations: mit anderen Worten; kein Wort sagen; ein paar Worte sagen; in Worte fassen; ein offenes Wort; kein Wort verstehen
  3. [B1] feste Zusage, dass man etwas ganz sicher tun wird
     - example: Ich gebe dir mein Wort, dass ich morgen pünktlich komme.
     - frames: jmdm. sein Wort geben; jmdn. beim Wort nehmen
     - collocations: sein Wort geben; sein Wort halten; sein Wort brechen; Wort halten
     - grammar: number Sing
  4. [B2] Recht, in einer Versammlung oder Diskussion zu sprechen
     - example: Nach dem Bürgermeister hat jetzt die Lehrerin das Wort.
     - frames: jmdm. das Wort erteilen; jmdm. ins Wort fallen
     - collocations: das Wort ergreifen; das Wort haben; zu Wort kommen; ums Wort bitten; sich zu Wort melden
     - grammar: number Sing

#### Entry B

**Lexeme 1: Wort**, NOUN
- grammar: gender Neut; plural_class -¨er; countability Count
- said: /ˈvɔʁt/ (de-DE)
- forms: Wortes [Case=Gen, Number=Sing] /ˈvɔʁ.təs/; Worts [Case=Gen, Number=Sing] /ˈvɔʁts/; Wörter [Case=Nom, Number=Plur] /ˈvœʁ.tɐ/
  1. [A1] kleinste selbstständige Einheit der Sprache, die eine eigene Bedeutung hat
     - example: Beim Lesen schlage ich unbekannte Wörter sofort im Lexikon nach.
     - collocations: ein Wort nachschlagen; ein Wort aussprechen; ein Wort buchstabieren; ein zusammengesetztes Wort
**Lexeme 2: Wort**, NOUN
- grammar: gender Neut; plural_class -e; countability Count
- said: /ˈvɔʁt/ (de-DE)
- forms: Wortes [Case=Gen, Number=Sing] /ˈvɔʁ.təs/; Worts [Case=Gen, Number=Sing] /ˈvɔʁts/; Worte [Case=Nom, Number=Plur] /ˈvɔʁ.tə/
  1. [B1] mündliche oder schriftliche Äußerung, mit der jemand einen Gedanken oder ein Gefühl ausdrückt
     - example: Zum Abschied fand die Direktorin sehr herzliche Worte für das gesamte Team.
     - frames: Worte für + Akk
     - collocations: in Worte fassen; mit anderen Worten; ein paar Worte sagen; ein paar Worte wechseln; klare Worte finden; offene Worte
  2. [B2] Recht oder Gelegenheit, in einem Gespräch oder einer Versammlung vor anderen zu sprechen
     - example: Bitte lassen Sie Ihren Kollegen ausreden und fallen Sie ihm nicht ins Wort.
     - frames: jmdm. ins Wort fallen; jmdm. das Wort erteilen
     - collocations: zu Wort kommen; sich zu Wort melden; das Wort ergreifen; das Wort haben
     - grammar: number Sing
  3. [B2] feste und verbindliche Zusage, auf die sich eine andere Person verlassen kann
     - example: Du kannst mir vertrauen, denn ich habe dir mein Wort darauf gegeben.
     - frames: jmdm. sein Wort geben
     - collocations: sein Wort halten; sein Wort brechen; sein Wort geben
     - grammar: number Sing

### 6. Bank

#### Entry A

**Lexeme 1: Bank**, NOUN
- grammar: gender Fem; plural_class -¨e; countability Count
- said: /ˈbaŋk/ (de-DE)
- forms: Bank [Case=Gen, Number=Sing] /ˈbaŋk/; Bänke [Case=Nom, Number=Plur] /ˈbɛŋ.kə/
  1. [A2] lange, schmale Sitzgelegenheit, auf der mehrere Personen nebeneinander sitzen können
     - example: Nach dem Spaziergang setzen wir uns auf eine Bank im Park.
     - frames: etw. auf die lange Bank schieben
     - collocations: sich auf eine Bank setzen; auf einer Bank sitzen; eine Bank im Park
  2. [B2] Platz am Rand des Spielfelds, an dem die Ersatzspieler sitzen
     - example: Der junge Stürmer saß das ganze Spiel über nur auf der Bank.
     - collocations: auf der Bank sitzen; von der Bank kommen; auf der Bank Platz nehmen
     - grammar: number Sing, mainly
**Lexeme 2: Bank**, NOUN
- grammar: gender Fem; plural_class -en; countability Count
- said: /ˈbaŋk/ (de-DE)
- forms: Bank [Case=Gen, Number=Sing] /ˈbaŋk/; Banken [Case=Nom, Number=Plur] /ˈbaŋ.kən/
  1. [A1] Unternehmen, das Geld verwahrt, verleiht und Zahlungen abwickelt; auch sein Gebäude
     - example: Morgen muss ich zur Bank, um ein neues Konto zu eröffnen.
     - collocations: zur Bank gehen; Geld auf der Bank haben; ein Konto bei der Bank eröffnen; einen Kredit bei der Bank aufnehmen; bei einer Bank arbeiten; Geld von der Bank abheben

#### Entry B

**Lexeme 1: Bank**, NOUN
- grammar: gender Fem; plural_class -en; countability Count
- said: /ˈbaŋk/ (de-DE)
- forms: Bank [Case=Gen, Number=Sing] /ˈbaŋk/; Banken [Case=Nom, Number=Plur] /ˈbaŋ.kən/
  1. [A1] Unternehmen, das Geld verwaltet, Kredite vergibt und Zahlungen für Kunden erledigt
     - example: Ich muss noch schnell zur Bank gehen und etwas Bargeld abheben.
     - collocations: Geld auf die Bank bringen; ein Konto bei einer Bank eröffnen; zur Bank gehen; eine Bank überfallen
**Lexeme 2: Bank**, NOUN
- grammar: gender Fem; plural_class -¨e; countability Count
- said: /ˈbaŋk/ (de-DE)
- forms: Bank [Case=Gen, Number=Sing] /ˈbaŋk/; Bänke [Case=Nom, Number=Plur] /ˈbɛŋ.kə/
  1. [A2] langes Möbelstück zum Sitzen für mehrere Personen, oft im Freien
     - example: Im Park saßen zwei ältere Leute zusammen auf einer sonnigen Bank.
     - collocations: auf einer Bank sitzen; sich auf eine Bank setzen
  2. [B2] Sitzplatz am Spielfeldrand für die Auswechselspieler einer Mannschaft während eines Spiels
     - example: Wegen seiner leichten Verletzung saß der Stürmer heute nur auf der Bank.
     - collocations: auf der Bank sitzen; auf der Bank bleiben

### 7. E-Mail

#### Entry A

**Lexeme 1: E-Mail**, NOUN
- grammar: gender Fem; plural_class -s; countability Both; by_variety {"de-AT": {"gender": "Neut"}, "de-CH": {"gender": "Neut"}}
- said: /ˈiː.meːl/ (de-DE)
- forms: E-Mail [Case=Gen, Number=Sing] /ˈiː.meːl/; E-Mails [Case=Gen, Number=Sing] /ˈiː.meːls/ (de-AT, de-CH); E-Mails [Case=Nom, Number=Plur] /ˈiː.meːls/
  1. [A1] Nachricht, die elektronisch über das Internet verschickt und empfangen wird
     - example: Hast du die E-Mail von unserer Chefin zum neuen Projekt schon gelesen?
     - frames: E-Mail an + Akk; E-Mail von + Dat
     - collocations: eine E-Mail schreiben; eine E-Mail schicken; eine E-Mail senden; eine E-Mail bekommen; eine E-Mail beantworten; eine E-Mail weiterleiten
     - grammar: countability Count
  2. [A2] System zum elektronischen Austausch von Nachrichten und Dateien über das Internet
     - example: Bei Rückfragen erreichen Sie unser Büro am schnellsten per E-Mail.
     - collocations: per E-Mail
     - grammar: countability Mass; number Sing
- relations: confusable Email (NOUN): Email without a hyphen means 'enamel' in standard German

#### Entry B

**Lexeme 1: E-Mail**, NOUN
- grammar: gender Fem; plural_class -s; by_variety {"de-AT": {"gender": "Neut"}, "de-CH": {"gender": "Neut"}}
- said: /ˈiː.meːl/ (de-DE)
- forms: E-Mail [Case=Gen, Number=Sing] /ˈiː.meːl/; E-Mails [Case=Nom, Number=Plur] /ˈiː.meːls/
  1. [A1] Brief oder Mitteilung, die man über das Internet an jemanden schickt
     - example: Bitte schick mir die Unterlagen morgen früh als E-Mail.
     - frames: E-Mail an + Akk; E-Mail von + Dat
     - collocations: eine E-Mail schreiben; eine E-Mail schicken; eine E-Mail bekommen; eine E-Mail beantworten; eine E-Mail weiterleiten; eine E-Mail löschen; eine E-Mail lesen
     - grammar: countability Count
  2. [A2] System, mit dem man Briefe und Mitteilungen über das Internet verschickt
     - example: Die meisten Kunden erreichen wir heute am schnellsten per E-Mail.
     - collocations: per E-Mail; über E-Mail
     - grammar: countability Mass; number Sing
- relations: confusable Email (NOUN): Email without the hyphen is enamel, a glassy coating, said /e.ˈmaːj/

### 8. Joghurt

#### Entry A

**Lexeme 1: Joghurt**, NOUN
- grammar: gender Masc; plural_class -s; countability Mass, mainly; note also neuter in Germany; in Austria also feminine; by_variety {"de-AT": {"gender": "Neut"}, "de-CH": {"gender": "Neut"}}
- said: /ˈjoː.ɡʊʁt/ (de-DE)
- forms: Joghurts [Case=Gen, Number=Sing] /ˈjoː.ɡʊʁts/; Joghurts [Case=Nom, Number=Plur] /ˈjoː.ɡʊʁts/
  1. [A1] dickflüssiges, leicht saures Lebensmittel aus Milch, die mit Bakterien behandelt wurde
     - example: Zum Frühstück esse ich gern Joghurt mit frischem Obst.
     - collocations: ein Becher Joghurt; griechischer Joghurt; fettarmer Joghurt; Joghurt mit Früchten; Joghurt essen

#### Entry B

**Lexeme 1: Joghurt**, NOUN
- grammar: gender Masc; plural_class -s; countability Both
- said: /ˈjoː.ɡʊʁt/ (de-DE), /ˈjɔ.ɡʊʁt/ (de-DE)
- spellings: Joghurt (de-DE, de-AT, de-CH); Jogurt (de-DE, de-AT, de-CH)
- forms: Joghurts [Case=Gen, Number=Sing] /ˈjoː.ɡʊʁts/; Joghurt [Case=Gen, Number=Sing] /ˈjoː.ɡʊʁt/; Joghurts [Case=Nom, Number=Plur] /ˈjoː.ɡʊʁts/
  1. [A1] dickflüssige, leicht säuerliche Speise aus Milch, die durch Bakterien entsteht
     - example: Zum Frühstück esse ich gern einen Becher Joghurt mit frischen Beeren.
     - collocations: ein Becher Joghurt; griechischer Joghurt; fettarmer Joghurt; cremiger Joghurt; Joghurt mit Früchten
**Lexeme 2: Joghurt**, NOUN
- grammar: gender Neut; plural_class -s; countability Both
- said: /ˈjoː.ɡʊʁt/ (de-DE), /ˈjɔ.ɡʊʁt/ (de-DE)
- spellings: Joghurt (de-DE, de-AT, de-CH); Jogurt (de-DE, de-AT, de-CH)
- forms: Joghurts [Case=Gen, Number=Sing] /ˈjoː.ɡʊʁts/; Joghurt [Case=Gen, Number=Sing] /ˈjoː.ɡʊʁt/; Joghurts [Case=Nom, Number=Plur] /ˈjoː.ɡʊʁts/
  1. [A1] dickflüssige, leicht säuerliche Speise aus Milch, die durch Bakterien entsteht
     - example: Hast du heute Morgen schon das Joghurt aus dem Kühlschrank gegessen?
     - collocations: ein Becher Joghurt; griechisches Joghurt; fettarmes Joghurt; cremiges Joghurt; Joghurt mit Früchten
**Lexeme 3: Joghurt**, NOUN
- grammar: gender Fem; plural_class -s; countability Both; by_variety {"de-AT": {"plural_class": "-"}}
- said: /ˈjoː.ɡʊʁt/ (de-DE), /ˈjɔ.ɡʊʁt/ (de-DE)
- spellings: Joghurt (de-DE, de-AT, de-CH); Jogurt (de-DE, de-AT, de-CH)
- forms: Joghurt [Case=Gen, Number=Sing] /ˈjoː.ɡʊʁt/; Joghurts [Case=Nom, Number=Plur] /ˈjoː.ɡʊʁts/; Joghurt [Case=Nom, Number=Plur] /ˈjoː.ɡʊʁt/ (de-AT)
  1. [A1] dickflüssige, leicht säuerliche Speise aus Milch, die durch Bakterien entsteht
     - example: Kannst du mir bitte die Joghurt aus dem oberen Fach geben?
     - collocations: ein Becher Joghurt; griechische Joghurt; fettarme Joghurt; cremige Joghurt; Joghurt mit Früchten
     - regions: de-AT mainly, de-DE mainly

### 9. erinnern

#### Entry A

**Lexeme 1: erinnern**, VERB
- grammar: conjugation weak; auxiliary have; irregular False; separable False
- said: /ɛɐ.ˈɪ.nɐn/ (de-DE)
- forms: erinnerte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ɛɐ.ˈɪ.nɐ.tə/; erinnert [Tense=Past, VerbForm=Part] /ɛɐ.ˈɪ.nɐt/
  1. [A2] etwas Vergangenes oder Erlebtes noch im Gedächtnis haben und wieder daran denken
     - example: Kannst du dich noch an unseren ersten gemeinsamen Urlaub erinnern?
     - frames: sich erinnern an + Akk; sich erinnern, dass ...; sich daran erinnern, dass ...
     - collocations: sich gut erinnern; sich genau erinnern; sich kaum erinnern; sich dunkel erinnern
     - grammar: valency Refl
     - opposites: vergessen
  2. [A2] dafür sorgen, dass jemand etwas Bestimmtes nicht vergisst
     - example: Erinnere mich bitte morgen früh noch einmal an den wichtigen Termin.
     - frames: jmdn. erinnern an + Akk; jmdn. daran erinnern, etw. zu tun; jmdn. daran erinnern, dass ...
     - grammar: valency Tr
  3. [B1] durch Ähnlichkeit oder als Zeichen Gedanken an jemanden oder etwas wecken
     - example: Seine ruhige Stimme hat uns sofort an seinen Großvater erinnert.
     - frames: jmdn. erinnern an + Akk; erinnern an + Akk
     - grammar: valency Both

#### Entry B

**Lexeme 1: erinnern**, VERB
- grammar: conjugation weak; auxiliary have; valency Both; irregular False
- said: /ɛɐ.ˈɪ.nɐn/ (de-DE)
- forms: erinnerte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ɛɐ.ˈɪ.nɐ.tə/; erinnert [Tense=Past, VerbForm=Part] /ɛɐ.ˈɪ.nɐt/
  1. [A2] etwas Vergangenes im Gedächtnis haben oder wieder daran denken
     - example: An seinen Namen kann ich mich leider nicht mehr erinnern.
     - frames: sich erinnern an + Akk; sich erinnern, dass ...; sich erinnern, etw. getan zu haben
     - collocations: sich gut erinnern; sich genau erinnern; sich dunkel erinnern; sich gern erinnern; wenn ich mich recht erinnere; sich nicht mehr erinnern können
     - grammar: valency Refl; reflexive True
     - opposites: vergessen
  2. [B1] dafür sorgen, dass jemand an etwas denkt und es nicht vergisst
     - example: Kannst du mich morgen bitte an den Termin beim Arzt erinnern?
     - frames: jmdn. erinnern an + Akk; jmdn. daran erinnern, etw. zu tun; jmdn. daran erinnern, dass ...
     - collocations: an einen Termin erinnern; an eine Frist erinnern; rechtzeitig erinnern
     - grammar: valency Tr
  3. [B1] jemanden an eine Person oder Sache denken lassen, oft durch Ähnlichkeit
     - example: Dieser Geruch erinnert mich immer an die Ferien bei meiner Oma.
     - frames: an jmdn./etw. erinnern; jmdn. an jmdn./etw. erinnern
     - collocations: stark erinnern; entfernt erinnern; ein Denkmal erinnert
     - grammar: valency Both

### 10. geil

#### Entry A

**Lexeme 1: geil**, ADJ
- grammar: comparable True
- said: /ˈɡaɪl/ (de-DE)
  1. [B1] besonders gut, beeindruckend und für große Begeisterung sorgend
     - example: Das Konzert gestern Abend im Park war wirklich total geil.
     - frames: etw. geil finden
     - collocations: echt geil; total geil; richtig geil
     - labels: formality informal
  2. [B2] stark sexuell erregt oder voller Verlangen nach sexueller Befriedigung
     - example: Deine tiefe Stimme am Telefon macht mich jedes Mal richtig geil.
     - frames: jmd. ist geil auf + Akk; jmdn. geil machen
     - labels: formality informal, attitude vulgar
  3. [B2] sehr begierig darauf, eine bestimmte Sache zu bekommen oder zu erleben
     - example: In dieser Branche sind leider viele Leute nur geil auf schnelles Geld.
     - frames: jmd. ist geil auf + Akk
     - labels: formality informal, attitude disapproving, figurative True

#### Entry B

**Lexeme 1: geil**, ADJ
- grammar: comparable True
- said: /ˈɡaɪl/ (de-DE)
  1. [B1] außergewöhnlich gut und für jemanden sehr schön und begeisternd
     - example: Das Konzert gestern Abend war echt geil, die Band war super.
     - frames: etw. geil finden
     - collocations: voll geil; echt geil; total geil; einfach geil
     - labels: formality slang
     - opposites: scheiße
  2. [B2] sexuell erregt und mit starkem Verlangen nach körperlicher Liebe
     - example: Nach dem langen Kuss waren beide ziemlich geil aufeinander.
     - frames: geil auf + Akk; jmdn. geil machen
     - collocations: geil werden
     - labels: formality informal, attitude vulgar
  3. [C1] mit großem, oft übertriebenem Verlangen nach einer bestimmten Sache
     - example: Er ist leider nur geil auf Geld und schnelle Autos.
     - frames: geil auf + Akk
     - labels: formality informal, attitude disapproving

### 11. doch

#### Entry A

**Lexeme 1: doch**, PART
- said: /ˈdɔx/ (de-DE)
  1. [A1] drückt als Antwort aus, dass eine verneinte Frage oder Aussage nicht stimmt
     - example: Du hast heute bestimmt keine Zeit, oder doch?
     - grammar: note always stressed; often stands alone as an answer
  2. [A2] drückt aus, dass der Sprecher Zustimmung erwartet oder an etwas Bekanntes erinnert
     - example: Wir haben das Thema doch gestern schon ausführlich besprochen.
     - grammar: note always unstressed
  3. [A2] macht eine Bitte, einen Vorschlag oder eine Aufforderung freundlicher oder dringlicher
     - example: Komm doch heute Abend noch kurz bei uns vorbei!
     - grammar: note always unstressed
  4. [B1] drückt Überraschung, Ärger oder Erleichterung in einem Ausruf oder Wunsch aus
     - example: Hätte ich doch bloß früher auf deinen Rat gehört!
     - grammar: note always unstressed
**Lexeme 2: doch**, ADV
- said: /ˈdɔx/ (de-DE)
  1. [A2] entgegen einer früheren Annahme, Erwartung oder Absicht am Ende trotzdem
     - example: Nach langem Überlegen haben wir uns doch für die Wohnung entschieden.
     - collocations: doch noch; also doch; oder doch; wohl doch
**Lexeme 3: doch**, CONJ
- said: /ˈdɔx/ (de-DE)
  1. [B1] verbindet zwei Aussagen und drückt dabei einen Gegensatz oder eine Einschränkung aus
     - example: Der Zug hatte große Verspätung, doch wir erreichten den Anschluss.

#### Entry B

**Lexeme 1: doch**, CONJ
- grammar: invariable True
- said: /ˈdɔx/ (de-DE)
  1. [B1] verbindet zwei Aussagen und drückt einen Gegensatz zum Vorherigen aus
     - example: Wir wollten heute grillen, doch dann fing es an zu regnen.
     - collocations: doch dann
**Lexeme 2: doch**, ADV
- grammar: invariable True
- said: /ˈdɔx/ (de-DE)
  1. [A2] entgegen der Erwartung oder trotz allem, was dagegen gesprochen hat
     - example: Am Ende haben wir den letzten Zug doch noch erreicht.
     - collocations: doch noch; also doch; dann doch
     - grammar: note usually stressed in this sense
  2. [A1] verwendet, um einer verneinten Frage oder Aussage zu widersprechen
     - example: Doch, ich habe die Hausaufgaben gestern schon gemacht.
     - collocations: oh doch; aber doch
     - grammar: note as an answer, alone or at the start of the reply
**Lexeme 3: doch**, PART
- grammar: invariable True
- said: /ˈdɔx/ (de-DE)
  1. [A2] verwendet, um eine Bitte oder Aufforderung freundlicher oder dringender zu machen
     - example: Komm doch morgen Abend einfach mal bei uns vorbei!
     - collocations: komm doch; setz dich doch
     - grammar: note unstressed, mostly in imperative sentences
  2. [B1] verwendet, um auf etwas Bekanntes hinzuweisen oder einen leichten Vorwurf auszudrücken
     - example: Das habe ich dir gestern schon ganz genau erklärt, weißt du doch.
     - grammar: note unstressed, inside the sentence
  3. [B2] verwendet, um einen Wunsch zu verstärken, der sich wahrscheinlich nicht erfüllt
     - example: Wenn ich doch nur mehr Zeit für meine Familie hätte!
     - collocations: wenn doch nur; wäre doch
     - grammar: note with the subjunctive, often together with nur
