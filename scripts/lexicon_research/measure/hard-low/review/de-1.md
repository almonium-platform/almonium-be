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
- grammar: gender Fem; plural_class -s; countability Count
- said: /ˈbɛnt/ (de-DE)
- forms: Band [Case=Gen, Number=Sing] /ˈbɛnt/; Bands [Case=Nom, Number=Plur] /ˈbɛnts/
  1. [A2] kleinere Gruppe von Personen, die gemeinsam moderne Musik macht
     - example: Am Samstagabend spielt eine bekannte Band auf dem Stadtfest.
     - collocations: eine Band gründen; eine bekannte Band; in einer Band spielen
**Lexeme 2: Band**, NOUN
- grammar: gender Neut; plural_class -¨er; countability Count
- said: /ˈbant/ (de-DE)
- forms: Bandes [Case=Gen, Number=Sing] /ˈban.dəs/; Bands [Case=Gen, Number=Sing] /ˈbants/; Bänder [Case=Nom, Number=Plur] /ˈbɛn.dɐ/
  1. [B1] langer, schmaler Streifen aus Stoff, Papier oder einem anderen Material
     - example: Sie hat das Geschenk mit einem roten Band aus Seide geschmückt.
     - collocations: ein schmales Band; ein rotes Band; ein Band durchschneiden; am laufenden Band
  2. [B2] fester Gewebestrang im Körper, der bewegliche Knochen an einem Gelenk zusammenhält
     - example: Beim Fußballspielen hat er sich leider ein Band im Knie gerissen.
     - collocations: ein Band überdehnen; ein gerissenes Band
  3. [B2] beschichteter Streifen aus Kunststoff, auf dem Töne oder Bilder gespeichert werden
     - example: Das ganze Gespräch wurde damals auf Band aufgenommen und später abgetippt.
     - collocations: etwas auf Band aufnehmen; das Band läuft
- relations: derived_from binden (VERB)
**Lexeme 3: Band**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈbant/ (de-DE)
- forms: Bandes [Case=Gen, Number=Sing] /ˈban.dəs/; Bands [Case=Gen, Number=Sing] /ˈbants/; Bände [Case=Nom, Number=Plur] /ˈbɛn.də/
  1. [B1] einzelnes Buch, das Teil eines größeren mehrteiligen Werkes oder einer Reihe ist
     - example: Der dritte Band dieser spannenden Reihe erscheint erst im nächsten Herbst.
     - collocations: ein dicker Band; ein schmaler Band; in mehreren Bänden erscheinen; Bände sprechen
- relations: derived_from binden (VERB)
**Lexeme 4: Band**, NOUN
- grammar: gender Neut; plural_class -e; countability Count; number Plur, mainly
- said: /ˈbant/ (de-DE)
- forms: Bandes [Case=Gen, Number=Sing] /ˈban.dəs/; Bands [Case=Gen, Number=Sing] /ˈbants/; Bande [Case=Nom, Number=Plur] /ˈban.də/
  1. [C1] enge innere Beziehung oder emotionale Verbundenheit zwischen Menschen
     - example: Zwischen den beiden Geschwistern besteht seit ihrer Kindheit ein enges Band.
     - collocations: ein enges Band; freundschaftliche Bande; familiäre Bande; Bande knüpfen
     - labels: style elevated, figurative True
- relations: derived_from binden (VERB); confusable Bande (NOUN): plural Bande looks like die Bande 'gang'

### 2. See

#### Entry A

**Lexeme 1: See**, NOUN
- grammar: gender Masc; plural_class -n; countability Count
- said: /ˈzeː/ (de-DE)
- forms: Sees [Case=Gen, Number=Sing] /ˈzeːs/; Seen [Case=Nom, Number=Plur] /ˈzeː.ən/
  1. [A1] größeres stehendes Gewässer auf dem Festland, das ringsum von Land umgeben ist
     - example: Am Wochenende fahren wir oft mit dem Fahrrad um den nahen See.
     - collocations: am See liegen; im See schwimmen; an den See fahren; ein künstlicher See; ein zugefrorener See
**Lexeme 2: See**, NOUN
- grammar: gender Fem; number Sing; countability Mass
- said: /ˈzeː/ (de-DE)
- forms: See [Case=Gen, Number=Sing] /ˈzeː/
  1. [B1] große zusammenhängende Fläche aus Salzwasser zwischen den Kontinenten, besonders als Gebiet der Schifffahrt
     - example: Wegen des starken Sturms konnten die Fischer heute nicht auf die offene See hinausfahren.
     - collocations: die offene See; auf hoher See; in See stechen; zur See fahren; an die See fahren; raue See; ruhige See

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
- forms: umfährt [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ʊm.ˈfɛːɐt/; umfuhr [Tense=Past, Person=1,3, Number=Sing, VerbForm=Fin, Mood=Ind] /ʊm.ˈfuːɐ/; umfahren [Tense=Past, VerbForm=Part] /ʊm.ˈfaː.ʁən/
  1. [B1] in einem Bogen um ein Hindernis oder ein Gebiet herum bewegen
     - example: Wegen der großen Baustelle müssen wir das Zentrum der Stadt weiträumig umfahren.
     - frames: etw. umfahren
     - collocations: einen Stau umfahren; ein Hindernis umfahren; eine Baustelle umfahren; weiträumig umfahren
- relations: derived_from fahren (VERB)
**Lexeme 2: umfahren**, VERB
- grammar: conjugation strong; auxiliary both; valency Both; irregular True; separable True; separable_prefix um
- said: /ˈʊm.ˌfaː.ʁən/ (de-DE)
- forms: fährt um [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈfɛːɐt ˈʊm/; fuhr um [Tense=Past, Person=1,3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈfuːɐ ˈʊm/; umgefahren [Tense=Past, VerbForm=Part] /ˈʊm.ɡə.ˌfaː.ʁən/
  1. [B1] mit einem Fahrzeug gegen jemanden oder etwas stoßen, sodass diese Person oder Sache umfällt
     - example: Beim Rückwärtsfahren hat der Lastwagen gestern fast das neue Straßenschild umgefahren.
     - frames: jmdn./etw. umfahren
     - collocations: einen Fußgänger umfahren; ein Schild umfahren; einen Pfosten umfahren
     - grammar: valency Tr; auxiliary have; by_variety {"de-AT": {"auxiliary": "both"}, "de-CH": {"auxiliary": "both"}}
  2. [B2] nicht den direkten Weg nehmen, sondern eine längere Strecke zurücklegen
     - example: Weil die Brücke gesperrt war, sind wir leider eine halbe Stunde umgefahren.
     - labels: formality informal
     - grammar: valency Intr; auxiliary be
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
  1. [A2] dem heutigen Stand der Technik, der Wissenschaft oder des Lebens entsprechend
     - example: In der Fabrik arbeiten wir seit diesem Jahr mit sehr modernen Maschinen.
     - collocations: moderne Technik; moderne Medizin; moderne Kunst; ein modernes Gebäude; modern ausgestattet
     - opposites: veraltet
  2. [A2] dem aktuellen Geschmack entsprechend und bei vielen Menschen gerade beliebt
     - example: Kurze Jacken mit breiten Schultern sind in diesem Herbst wieder modern.
     - collocations: moderne Kleidung; ein moderner Schnitt; wieder modern sein
     - opposites: altmodisch, unmodern
  3. [B1] offen für neue Ideen und frei von überholten Vorstellungen
     - example: Ihre Eltern denken sehr modern und unterstützen ihre eigenen Pläne.
     - collocations: modern denken; eine moderne Einstellung
     - opposites: konservativ
**Lexeme 2: modern**, VERB
- grammar: conjugation weak; auxiliary have; valency Intr; irregular False
- said: /ˈmoː.dɐn/ (de-DE)
- forms: moderte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈmoː.dɐ.tə/; gemodert [Tense=Past, VerbForm=Part] /ɡə.ˈmoː.dɐt/
  1. [C1] durch Feuchtigkeit und fehlende Luft langsam faulen und sich zersetzen
     - example: Unter den dunklen Bäumen modert das nasse Laub vom letzten Herbst.
     - labels: style elevated
- relations: derived_from Moder (NOUN); confusable modern (ADJ): spelled the same as modern /mo.ˈdɛʁn/ 'modern', but stressed on the first syllable

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
  1. [A1] kleinste selbstständige Einheit der Sprache mit einer eigenen Bedeutung
     - example: Schlag bitte die unbekannten Wörter im Text sofort nach.
     - collocations: ein englisches Wort; ein seltenes Wort; Wörter lernen; ein Wort nachschlagen; ein Wort falsch schreiben
**Lexeme 2: Wort**, NOUN
- grammar: gender Neut; plural_class -e
- said: /ˈvɔʁt/ (de-DE)
- forms: Wortes [Case=Gen, Number=Sing] /ˈvɔʁ.təs/; Worts [Case=Gen, Number=Sing] /ˈvɔʁts/; Worte [Case=Nom, Number=Plur] /ˈvɔʁ.tə/
  1. [A2] zusammenhängende Äußerung, mit der jemand Gedanken oder Gefühle ausdrückt
     - example: Ich danke Ihnen herzlich für Ihre freundlichen und offenen Worte.
     - frames: ein paar Worte über + Akk; ein Wort mit jmdm. reden
     - collocations: freundliche Worte; offene Worte; in eigenen Worten; mit anderen Worten; keine Worte finden; kein Wort sagen
     - grammar: countability Count
  2. [B1] Gelegenheit oder Erlaubnis, in einer Runde oder Diskussion zu sprechen
     - example: In der Versammlung bat eine Kollegin sofort ums Wort.
     - frames: jmdm. das Wort erteilen; jmdm. ins Wort fallen
     - collocations: sich zu Wort melden; zu Wort kommen; das Wort ergreifen; ums Wort bitten; das letzte Wort haben
     - grammar: number Sing; countability Mass
  3. [B2] feste und verbindliche Zusage, auf die sich jemand verlassen kann
     - example: Du kannst mir vertrauen, ich gebe dir mein Wort.
     - frames: jmdm. sein Wort geben; jmdn. beim Wort nehmen
     - collocations: sein Wort halten; sein Wort brechen; zu seinem Wort stehen; ein Mann von Wort
     - grammar: number Sing; countability Mass

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
  1. [A1] Unternehmen, das Geld für Kunden aufbewahrt, verleiht und überweist
     - example: Morgen eröffne ich bei einer anderen Bank ein neues Konto.
     - collocations: Geld auf die Bank bringen; ein Konto bei einer Bank haben
**Lexeme 2: Bank**, NOUN
- grammar: gender Fem; plural_class -¨e; countability Count
- said: /ˈbaŋk/ (de-DE)
- forms: Bank [Case=Gen, Number=Sing] /ˈbaŋk/; Bänke [Case=Nom, Number=Plur] /ˈbɛŋ.kə/
  1. [A2] langes Möbelstück aus Holz, Metall oder Stein zum Sitzen für mehrere Personen
     - example: Im Park sitzen zwei ältere Leute zusammen auf einer Bank.
     - collocations: auf einer Bank sitzen; etwas auf die lange Bank schieben
  2. [B2] Sitzplatz am Rand des Spielfelds für den Trainer und die Ersatzspieler
     - example: Nach seiner Verletzung sitzt der Stürmer heute nur auf der Bank.
     - collocations: auf der Bank sitzen

### 7. E-Mail

#### Entry A

**Lexeme 1: E-Mail**, NOUN
- grammar: gender Fem; plural_class -s; countability Both; by_variety {"de-AT": {"gender": "Neut"}, "de-CH": {"gender": "Neut"}}
- said: /ˈiː.meːl/ (de-DE), /ˈiː.mɛɪl/ (de-DE)
- forms: E-Mail [Case=Gen, Number=Sing] /ˈiː.meːl/ (de-DE); E-Mails [Case=Gen, Number=Sing] /ˈiː.meːls/ (de-AT, de-CH); E-Mails [Case=Nom, Number=Plur] /ˈiː.meːls/
  1. [A1] Nachricht, die elektronisch über ein Computernetz von einem Konto zu einem anderen geschickt wird
     - example: Bitte schicken Sie mir die Unterlagen bis morgen Abend als E-Mail.
     - frames: E-Mail an + Akk; E-Mail von + Dat
     - collocations: eine E-Mail schreiben; eine E-Mail schicken; eine E-Mail beantworten; eine E-Mail weiterleiten; seine E-Mails abrufen; per E-Mail
     - grammar: countability Count
  2. [A2] System zum elektronischen Versenden und Empfangen von Nachrichten über das Internet
     - example: Bei Fragen erreichen Sie unseren Kundendienst am schnellsten per E-Mail.
     - collocations: per E-Mail; Kontakt per E-Mail
     - grammar: countability Mass; number Sing

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
- grammar: gender Masc; plural_class -s; countability Both; by_variety {"de-AT": {"gender": "Neut", "plural_class": "-"}, "de-CH": {"gender": "Neut"}}
- said: /ˈjoː.ɡʊʁt/ (de-DE)
- spellings: Joghurt (de-DE, de-AT, de-CH); Jogurt (de-DE, de-AT, de-CH)
- forms: Joghurts [Case=Gen, Number=Sing] /ˈjoː.ɡʊʁts/; Joghurt [Case=Gen, Number=Sing] /ˈjoː.ɡʊʁt/; Joghurts [Case=Nom, Number=Plur] /ˈjoː.ɡʊʁts/; Joghurt [Case=Nom, Number=Plur] /ˈjoː.ɡʊʁt/ (de-AT)
  1. [A1] cremiges, leicht saures Milchprodukt, das durch besondere Bakterien fest wird
     - example: Zum Frühstück esse ich gern frisches Obst mit kaltem Joghurt.
     - collocations: griechischer Joghurt; naturreiner Joghurt; ein Becher Joghurt
**Lexeme 2: Joghurt**, NOUN
- grammar: gender Neut; plural_class -s; countability Both; by_variety {"de-AT": {"plural_class": "-"}}
- said: /ˈjoː.ɡʊʁt/ (de-DE)
- spellings: Joghurt (de-DE, de-AT, de-CH); Jogurt (de-DE, de-AT, de-CH)
- forms: Joghurts [Case=Gen, Number=Sing] /ˈjoː.ɡʊʁts/; Joghurt [Case=Gen, Number=Sing] /ˈjoː.ɡʊʁt/; Joghurts [Case=Nom, Number=Plur] /ˈjoː.ɡʊʁts/; Joghurt [Case=Nom, Number=Plur] /ˈjoː.ɡʊʁt/ (de-AT)
  1. [A1] cremiges, leicht saures Milchprodukt, das durch besondere Bakterien fest wird
     - example: Hast du das Joghurt schon wieder aus dem Kühlschrank genommen?
     - collocations: griechisches Joghurt; ein Becher Joghurt
**Lexeme 3: Joghurt**, NOUN
- grammar: gender Fem; plural_class -s; countability Both; by_variety {"de-AT": {"plural_class": "-"}}
- said: /ˈjoː.ɡʊʁt/ (de-DE)
- spellings: Joghurt (de-DE, de-AT, de-CH); Jogurt (de-DE, de-AT, de-CH)
- forms: Joghurt [Case=Gen, Number=Sing] /ˈjoː.ɡʊʁt/; Joghurts [Case=Nom, Number=Plur] /ˈjoː.ɡʊʁts/; Joghurt [Case=Nom, Number=Plur] /ˈjoː.ɡʊʁt/ (de-AT)
  1. [A1] cremiges, leicht saures Milchprodukt, das durch besondere Bakterien fest wird
     - example: Stell bitte die geöffnete Joghurt sofort zurück in den Kühlschrank.
     - collocations: ein Becher Joghurt
     - regions: de-DE mainly, de-AT mainly

### 9. erinnern

#### Entry A

**Lexeme 1: erinnern**, VERB
- grammar: conjugation weak; auxiliary have; irregular False; separable False
- said: /ɛɐ.ˈɪ.nɐn/ (de-DE)
- forms: erinnerte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ɛɐ.ˈɪ.nɐ.tə/; erinnert [Tense=Past, VerbForm=Part] /ɛɐ.ˈɪ.nɐt/
  1. [A2] etwas Vergangenes oder Gelerntes noch im Gedächtnis haben und wieder daran denken
     - example: Kannst du dich noch an unseren ersten Urlaub am Meer erinnern?
     - frames: sich erinnern an + Akk; sich erinnern, dass ...; jmds./etw. erinnern
     - collocations: sich gut erinnern; sich kaum erinnern; sich dunkel erinnern; sich genau erinnern
     - grammar: valency Refl, mainly; reflexive True; note genitive object without sich is formal; transitive etw. erinnern is informal
     - opposites: vergessen
  2. [A2] bewirken, dass jemand etwas Bestimmtes nicht vergisst oder wieder daran denkt
     - example: Bitte erinnere mich morgen früh daran, die wichtigen Unterlagen mitzunehmen.
     - frames: jmdn. erinnern an + Akk; jmdn. daran erinnern, etw. zu tun; jmdn. daran erinnern, dass ...
     - grammar: valency Tr
  3. [B1] durch Aussehen oder Art den Gedanken an jemanden oder etwas wecken
     - example: Mit seinen großen dunklen Augen erinnert der Junge stark an seinen Großvater.
     - frames: etw. erinnert an + Akk; etw. erinnert jmdn. an + Akk; jmd. erinnert an + Akk; jmd. erinnert jmdn. an + Akk
     - collocations: stark erinnern
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
  1. [B1] sehr gut, großartig oder aufregend, sodass man davon begeistert ist
     - example: Das Konzert gestern Abend im Park war wirklich total geil.
     - collocations: echt geil; total geil; voll geil
     - labels: formality informal
  2. [B2] stark sexuell erregt und mit großem Verlangen nach körperlicher Nähe
     - example: Er machte auf der Feier ein paar ziemlich geile Bemerkungen.
     - frames: geil sein auf + Akk
     - labels: formality informal
  3. [C1] übermäßig begierig nach etwas Bestimmtem wie Geld, Erfolg oder Aufmerksamkeit
     - example: Manche Medien sind leider nur noch geil auf schnelle Sensationen.
     - frames: geil sein auf + Akk
     - labels: formality informal, attitude disapproving, figurative True
     - grammar: position Pred

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
  1. [A2] drückt aus, dass der Hörer etwas eigentlich schon weiß oder zustimmen wird
     - example: Du weißt doch genau, dass wir heute pünktlich losfahren müssen.
     - collocations: du weißt doch; ich habe es doch gesagt
  2. [A1] drückt in einer Antwort Widerspruch gegen eine verneinte Aussage oder Frage aus
     - example: Du hast heute bestimmt keine Zeit, oder? — Doch, eine Stunde habe ich.
     - grammar: note always stressed
  3. [A2] macht eine Bitte, einen Vorschlag oder eine Aufforderung freundlicher oder dringlicher
     - example: Setz dich doch erst einmal hin und trink eine Tasse Tee.
     - collocations: komm doch mal her; sag doch was
  4. [B1] drückt in Ausrufen Überraschung, Ärger oder Erleichterung über einen Sachverhalt aus
     - example: Das gibt es doch gar nicht, der Zug fährt schon wieder ab!
     - collocations: das gibt es doch nicht; das darf doch nicht wahr sein
**Lexeme 2: doch**, ADV
- said: /ˈdɔx/ (de-DE)
  1. [A2] entgegen einer früheren Annahme, Absicht oder Erwartung am Ende trotzdem
     - example: Wir wollten zu Hause bleiben, sind dann aber doch ins Kino gegangen.
     - collocations: nun doch; dann doch; vielleicht doch; oder doch nicht
     - grammar: note always stressed
**Lexeme 3: doch**, CONJ
- said: /ˈdɔx/ (de-DE)
  1. [B1] verbindet zwei Aussagen oder Satzteile und drückt einen Gegensatz zu dem Vorhergehenden aus
     - example: Sie hat lange nach dem Schlüssel gesucht, doch sie fand ihn nicht.

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
