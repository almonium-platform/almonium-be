# German: entries to check (1 of 1)

These are entries of a learner's dictionary of German, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of German.

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
  sense, or an example is not natural German.

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

### 1. gebräuchlich

**Lexeme 1: gebräuchlich**, ADJ
- grammar: comparable True
- said: /ɡə.ˈbʁɔʏç.lɪç/ (de-DE)
  1. [B2] von vielen Menschen oft verwendet und allgemein üblich
     - example: Dieser Ausdruck ist heute in der Umgangssprache kaum noch gebräuchlich.
     - frames: etw. ist gebräuchlich; allgemein gebräuchlich
     - opposites: ungebräuchlich
- relations: derived_from Gebrauch (NOUN)

### 2. handschriftlich

**Lexeme 1: handschriftlich**, ADJ
- grammar: comparable False
- said: /ˈhant.ʃʁɪft.lɪç/ (de-DE)
  1. [B1] selbst mit einem Stift geschrieben, nicht gedruckt oder getippt
     - example: Bitte füllen Sie das Formular handschriftlich und gut lesbar aus.
- relations: derived_from Handschrift (NOUN)

### 3. deswegen

**Lexeme 1: deswegen**, ADV
- grammar: invariable True
- said: /ˈdɛs.veː.ɡən/ (de-DE)
  1. [A2] aus diesem Grund; als Folge des vorher Gesagten
     - example: Ich war gestern krank und bin deswegen nicht zur Arbeit gegangen.
- relations: derived_from wegen (ADP)

### 4. Verbleib

**Lexeme 1: Verbleib**, NOUN
- grammar: gender Masc; number Sing; countability Mass
- said: /fɛɐ.ˈblaɪp/ (de-DE)
- forms: Verbleibs [Case=Gen, Number=Sing] /fɛɐ.ˈblaɪps/; Verbleibes [Case=Gen, Number=Sing] /fɛɐ.ˈblaɪ.bəs/
  1. [B2] Ort, an dem sich jemand oder etwas gerade befindet
     - example: Über den Verbleib des vermissten Mädchens ist bis heute nichts bekannt.
     - frames: der Verbleib + Gen
  2. [C1] Tatsache, dass jemand weiterhin an einem Ort, in einem Amt oder einer Gruppe ist
     - example: Viele Abgeordnete fordern seinen Verbleib im Amt bis zur nächsten Wahl.
     - frames: Verbleib in + Dat
- relations: derived_from verbleiben (VERB)

### 5. unerfahren

**Lexeme 1: unerfahren**, ADJ
- grammar: comparable True
- said: /ˈʊn.ɛɐ.faː.ʁən/ (de-DE)
  1. [B2] ohne das Wissen und Können, das man durch längere Praxis gewinnt
     - example: Als junger, unerfahrener Lehrer hatte er anfangs große Probleme.
     - frames: unerfahren in + Dat; unerfahren sein
     - opposites: erfahren
- relations: derived_from erfahren (ADJ)

### 6. ansprechbar

**Lexeme 1: ansprechbar**, ADJ
- said: /ˈan.ʃpʁɛç.baːɐ/ (de-DE)
  1. [B2] bereit und erreichbar, um mit anderen über etwas zu reden
     - example: Bei Problemen ist die Lehrerin jederzeit für die Eltern ansprechbar.
     - frames: für jmdn. ansprechbar sein; ansprechbar sein für + Akk
  2. [B2] bei Bewusstsein und fähig, auf Worte anderer zu reagieren
     - example: Nach dem Unfall war der Fahrer zunächst nicht ansprechbar.
     - grammar: position Pred
- relations: derived_from ansprechen (VERB)

### 7. Bürger

**Lexeme 1: Bürger**, NOUN
- grammar: gender Masc; plural_class -; countability Count
- said: /ˈbʏʁ.ɡɐ/ (de-DE)
- forms: Bürgers [Case=Gen, Number=Sing] /ˈbʏʁ.ɡɐs/; Bürger [Case=Nom, Number=Plur] /ˈbʏʁ.ɡɐ/
  1. [B1] Person, die zu einem Staat gehört und dort Rechte und Pflichten hat
     - example: Alle Bürger dürfen ab achtzehn Jahren wählen gehen.
  2. [B1] Person, die in einer bestimmten Stadt oder Gemeinde wohnt
     - example: Die Stadt lädt ihre Bürger zu einer Versammlung im Rathaus ein.
     - frames: Bürger + Gen
  3. [C1] Angehöriger der Mittelschicht, besonders im Unterschied zu Adel und Arbeitern
     - example: Im neunzehnten Jahrhundert wurden die reichen Bürger immer mächtiger.
     - labels: domain history

### 8. naturwissenschaftlich

**Lexeme 1: naturwissenschaftlich**, ADJ
- grammar: comparable False
- said: /na.ˈtuːɐ.vɪ.sən.ʃaft.lɪç/ (de-DE)
  1. [B2] zu den Fächern gehörend, die mit Experimenten die belebte und unbelebte Welt erforschen
     - example: Nach dem Abitur möchte mein Bruder ein naturwissenschaftliches Fach studieren.
- relations: adjective_of Naturwissenschaft (NOUN)

### 9. selbstständigkeit

**Lexeme 1: Selbstständigkeit**, NOUN
- grammar: gender Fem; countability Mass; number Sing
- said: /ˈzɛlpst.ʃtɛn.dɪç.kaɪt/ (de-DE)
- spellings: Selbstständigkeit; Selbständigkeit
- forms: Selbstständigkeit [Case=Gen, Number=Sing] /ˈzɛlpst.ʃtɛn.dɪç.kaɪt/
  1. [B2] Fähigkeit, ohne Hilfe anderer zu handeln und zu entscheiden
     - example: Eltern sollten die Selbstständigkeit ihrer Kinder schon früh fördern.
     - opposites: Abhängigkeit
  2. [B2] Arbeit im eigenen Betrieb oder auf eigene Rechnung, ohne Arbeitgeber
     - example: Nach zehn Jahren im Büro wagte sie den Schritt in die Selbstständigkeit.
     - frames: in die Selbstständigkeit gehen
- relations: abstract_noun_of selbstständig (ADJ)

### 10. Bauer

**Lexeme 1: Bauer**, NOUN
- grammar: gender Masc; declension weak; plural_class -n; countability Count; note Dativ und Akkusativ Singular: dem/den Bauern
- said: /ˈbaʊ.ɐ/ (de-DE)
- forms: Bauern [Case=Gen, Number=Sing] /ˈbaʊ.ɐn/; Bauern [Case=Nom, Number=Plur] /ˈbaʊ.ɐn/
  1. [A2] jemand, der beruflich Felder bestellt und Tiere hält
     - example: Viele Bauern verkaufen ihre Milch direkt an die Kunden.
  2. [B2] kleinste und schwächste Figur im Schachspiel
     - example: Mit dem Bauern darfst du nur nach vorne ziehen.
  3. [C1] grober, ungebildeter Mensch ohne gute Manieren
     - example: Benimm dich beim Essen nicht wie ein Bauer!
     - labels: formality informal, attitude derogatory
**Lexeme 2: Bauer**, NOUN
- grammar: gender Neut; plural_class -; countability Count; note selten auch der Bauer
- said: /ˈbaʊ.ɐ/ (de-DE)
- forms: Bauers [Case=Gen, Number=Sing] /ˈbaʊ.ɐs/; Bauer [Case=Nom, Number=Plur] /ˈbaʊ.ɐ/
  1. [C2] kleiner Käfig, in dem man Vögel hält
     - example: Im Bauer am Fenster sang ein kleiner gelber Kanarienvogel.
     - labels: rare True

### 11. Grenze

**Lexeme 1: Grenze**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /ˈɡʁɛn.tsə/ (de-DE)
- forms: Grenze [Case=Gen, Number=Sing] /ˈɡʁɛn.tsə/; Grenzen [Case=Nom, Number=Plur] /ˈɡʁɛn.tsən/
  1. [A2] Linie, die zwei Staaten, Gebiete oder Grundstücke voneinander trennt
     - example: An der Grenze mussten alle Reisenden ihre Pässe zeigen.
     - frames: die Grenze zu + Dat; an der Grenze; über die Grenze
  2. [B1] Punkt oder Maß, über das man nicht hinausgehen kann oder darf
     - example: Auch meine Geduld hat irgendwann ihre Grenzen.
     - frames: an seine Grenzen stoßen; keine Grenzen kennen

### 12. leisten

**Lexeme 1: leisten**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr
- said: /ˈlaɪs.tən/ (de-DE)
- forms: leistete [Tense=Past, Person=3, Number=Sing, Mood=Ind, VerbForm=Fin] /ˈlaɪs.tə.tə/; geleistet [Tense=Past, VerbForm=Part] /ɡə.ˈlaɪs.tət/
  1. [B1] durch Arbeit oder Anstrengung ein gutes Ergebnis erreichen
     - example: Ihr habt in diesem Jahr wirklich sehr viel geleistet.
     - frames: etw leisten
  2. [B1] genug Geld haben, um etwas zu kaufen oder zu bezahlen
     - example: Eine so große Wohnung können wir uns nicht leisten.
     - frames: sich (Dat) etw leisten; sich (Dat) etw leisten können
     - grammar: reflexive True
  3. [B2] zusammen mit einem Nomen ausdrücken, dass man etwas tut oder gibt
     - example: Bei einem Unfall muss jeder sofort Erste Hilfe leisten.
     - frames: Hilfe leisten; Widerstand leisten; einen Beitrag leisten zu + Dat
  4. [B2] etwas tun, was eigentlich nicht erlaubt oder nicht klug ist
     - example: In dieser Prüfung darfst du dir keinen Fehler leisten.
     - frames: sich (Dat) etw leisten
     - grammar: reflexive True
**Lexeme 2: Leisten**, NOUN
- grammar: gender Masc; plural_class -; countability Count
- said: /ˈlaɪs.tən/ (de-DE)
- forms: Leistens [Case=Gen, Number=Sing] /ˈlaɪs.təns/; Leisten [Case=Nom, Number=Plur] /ˈlaɪs.tən/
  1. [C2] Form aus Holz oder Metall wie ein Fuß, über der man Schuhe herstellt
     - example: Der Schuhmacher spannt das Leder vorsichtig über den Leisten.
- relations: confusable Leiste (NOUN): Leisten is also the plural of die Leiste (strip of wood; groin)

### 13. verwendbar

**Lexeme 1: verwendbar**, ADJ
- said: /fɛɐ.ˈvɛnt.baːɐ/ (de-DE)
  1. [B2] so beschaffen, dass man es für einen Zweck gebrauchen kann
     - example: Diese alten Kartons sind für den Umzug noch gut verwendbar.
     - frames: verwendbar für + Akk; als etw. verwendbar sein
     - opposites: unbrauchbar
- relations: derived_from verwenden (VERB)

### 14. triggern

**Lexeme 1: triggern**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False
- said: /ˈtʁɪ.ɡɐn/ (de-DE)
- forms: triggerte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈtʁɪ.ɡɐ.tə/; getriggert [Tense=Past, VerbForm=Part] /ɡə.ˈtʁɪ.ɡɐt/
  1. [C1] bei jemandem plötzlich schmerzhafte Erinnerungen oder starke negative Gefühle auslösen
     - example: Bestimmte Geräusche können Menschen mit schlimmen Erlebnissen stark triggern.
     - labels: domain psychology
  2. [B2] jemanden absichtlich oder unabsichtlich reizen, sodass er sich sehr aufregt
     - example: Solche Kommentare im Netz triggern mich jedes Mal total.
     - labels: formality informal
  3. [C2] einen technischen Vorgang durch ein Signal in Gang setzen
     - example: Ein kurzer Impuls triggert die Kamera, sobald sich etwas bewegt.
     - labels: domain technology
- relations: derived_from Trigger (NOUN)

### 15. passieren

**Lexeme 1: passieren**, VERB
- grammar: conjugation weak; auxiliary both; valency Both
- said: /pa.ˈsiː.ʁən/ (de-DE)
- forms: passierte [Tense=Past, Person=3, Number=Sing, Mood=Ind, VerbForm=Fin] /pa.ˈsiːɐ.tə/; passiert [Tense=Past, VerbForm=Part] /pa.ˈsiːɐt/
  1. [A2] sich ereignen, oft plötzlich und ohne dass man es geplant hat
     - example: Was ist gestern Abend auf der Straße passiert?
     - grammar: auxiliary be; valency Intr
  2. [A2] jemanden als etwas Unangenehmes treffen, ohne dass er es will
     - example: Keine Angst, dir kann hier nichts passieren.
     - frames: passieren + Dat
     - grammar: auxiliary be; valency Intr
  3. [B2] durch einen Ort oder an einer Stelle vorbei weiter gehen oder fahren
     - example: Wir haben die Grenze ohne jede Kontrolle passiert.
     - frames: passieren + Akk
     - grammar: auxiliary have; valency Tr
  4. [C1] weiche Lebensmittel durch ein Sieb drücken, damit sie fein werden
     - example: Zum Schluss passierst du die Suppe durch ein feines Sieb.
     - frames: etw durch ein Sieb passieren
     - labels: domain cooking
     - grammar: auxiliary have; valency Tr

### 16. marginal

**Lexeme 1: marginal**, ADJ
- said: /maʁ.ɡi.ˈnaːl/ (de-DE)
  1. [C1] nur sehr gering und deshalb kaum von Bedeutung
     - example: Zwischen den beiden Angeboten gibt es nur marginale Unterschiede.
     - frames: eine marginale Rolle spielen
  2. [C2] am Rand einer Gruppe oder Gesellschaft stehend und wenig beachtet
     - example: Lange Zeit blieb diese Bewegung eine marginale Erscheinung in der Politik.

### 17. durchfahren

**Lexeme 1: durchfahren**, VERB
- grammar: conjugation strong; auxiliary be; valency Intr; separable True; separable_prefix durch
- said: /ˈdʊʁç.faː.ʁən/ (de-DE)
- forms: fährt durch [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈfɛːɐt ˈdʊʁç/; fuhr durch [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈfuːɐ ˈdʊʁç/; durchgefahren [Tense=Past, VerbForm=Part] /ˈdʊʁç.ɡə.faː.ʁən/
  1. [B1] ohne Halt oder Pause mit einem Verkehrsmittel bis zum Ziel weiterreisen
     - example: Wir fahren heute Nacht durch und sind morgen früh am Meer.
     - frames: bis + Ort durchfahren
  2. [B1] sich mit einem Verkehrsmittel durch eine Öffnung oder einen Raum hindurch bewegen
     - example: Die Brücke ist zu niedrig, hier können große Lastwagen nicht durchfahren.
     - frames: durch etw. durchfahren
- relations: derived_from fahren (VERB)
**Lexeme 2: durchfahren**, VERB
- grammar: conjugation strong; auxiliary have; valency Tr; separable False
- said: /dʊʁç.ˈfaː.ʁən/ (de-DE)
- forms: durchfährt [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /dʊʁç.ˈfɛːɐt/; durchfuhr [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /dʊʁç.ˈfuːɐ/; durchfahren [Tense=Past, VerbForm=Part] /dʊʁç.ˈfaː.ʁən/
  1. [B2] ein Gebiet mit einem Verkehrsmittel von einem Ende zum anderen durchqueren
     - example: Auf unserer Reise haben wir ganz Skandinavien mit dem Zug durchfahren.
  2. [C1] plötzlich und heftig im Körper oder in den Gedanken einer Person spürbar werden
     - example: Bei dem lauten Knall durchfuhr mich ein heftiger Schreck.
     - frames: etw. durchfährt jmdn.
     - labels: figurative True
- relations: derived_from fahren (VERB)

### 18. Autobahn

**Lexeme 1: Autobahn**, NOUN
- grammar: gender Fem; plural_class -en; countability Count
- said: /ˈaʊ.toː.baːn/ (de-DE)
- forms: Autobahn [Case=Gen, Number=Sing] /ˈaʊ.toː.baːn/; Autobahnen [Case=Nom, Number=Plur] /ˈaʊ.toː.baː.nən/
  1. [A2] breite Straße mit getrennten Fahrspuren für schnelle Fahrzeuge, ohne Kreuzungen
     - example: Am Wochenende gibt es auf der Autobahn oft lange Staus.
     - frames: auf der Autobahn

### 19. einparken

**Lexeme 1: einparken**, VERB
- grammar: conjugation weak; auxiliary have; valency Both; irregular False; separable True; separable_prefix ein
- said: /ˈaɪn.paʁ.kən/ (de-DE)
- forms: parkte ein [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈpaʁk.tə ˈaɪn/; eingeparkt [Tense=Past, VerbForm=Part] /ˈaɪn.ɡə.paʁkt/
  1. [B1] ein Fahrzeug in eine freie Lücke fahren und dort abstellen
     - example: Rückwärts einparken fällt mir in engen Straßen immer noch schwer.
     - frames: (ein Auto) einparken; rückwärts einparken; in eine Lücke einparken
     - grammar: valency Both
     - opposites: ausparken
  2. [C1] ein anderes Fahrzeug durch zu dichtes Parken so einschließen, dass es nicht wegfahren kann
     - example: Jemand hat meinen Wagen eingeparkt, und jetzt komme ich nicht weg.
     - frames: jmdn./etw. einparken
     - grammar: valency Tr
- relations: derived_from parken (VERB)

### 20. Hotel

**Lexeme 1: Hotel**, NOUN
- grammar: gender Neut; plural_class -s; countability Count
- said: /ho.ˈtɛl/ (de-DE)
- forms: Hotels [Case=Gen, Number=Sing] /ho.ˈtɛls/; Hotels [Case=Nom, Number=Plur] /ho.ˈtɛls/
  1. [A1] Haus, in dem Gäste gegen Bezahlung übernachten und oft auch essen
     - example: Heute übernachten wir in einem kleinen Hotel direkt am See.
     - frames: im Hotel übernachten; in einem Hotel wohnen

### 21. abdrücken

**Lexeme 1: abdrücken**, VERB
- grammar: conjugation weak; auxiliary have; valency Both; irregular False; separable True; separable_prefix ab
- said: /ˈap.dʁʏ.kən/ (de-DE)
- forms: drückte ab [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈdʁʏk.tə ˈap/; abgedrückt [Tense=Past, VerbForm=Part] /ˈap.ɡə.dʁʏkt/
  1. [B2] mit dem Finger den Abzug einer Waffe ziehen und so schießen
     - example: Der Jäger zielte lange, dann drückte er plötzlich ab.
     - grammar: valency Intr
  2. [C1] etwas so fest zusammenpressen, dass nichts mehr hindurchfließen kann
     - example: Der enge Gürtel drückt mir fast die Luft ab.
     - frames: jmdm. die Luft abdrücken; eine Ader abdrücken
     - grammar: valency Tr
  3. [C1] eine Summe Geld bezahlen, meist ungern
     - example: Für die Reparatur musste ich fast tausend Euro abdrücken.
     - labels: formality informal
     - grammar: valency Tr
  4. [C1] sich mit Kraft von einer Fläche wegstoßen
     - example: Beim Sprung drückst du dich mit beiden Füßen kräftig vom Boden ab.
     - frames: sich von + Dat abdrücken
     - grammar: valency Refl
- relations: derived_from drücken (VERB); confusable abdrucken (VERB): differs only by the umlaut; abdrucken means to print or publish a text

### 22. anlaufen

**Lexeme 1: anlaufen**, VERB
- grammar: conjugation strong; irregular True; auxiliary both; separable True; separable_prefix an; valency Both
- said: /ˈan.laʊ.fən/ (de-DE)
- forms: läuft an [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈlɔʏft ˈan/; lief an [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈliːf ˈan/; angelaufen [Tense=Past, VerbForm=Part] /ˈan.ɡə.laʊ.fən/
  1. [B2] in Gang kommen und zu arbeiten oder stattzufinden beginnen
     - example: Die Produktion des neuen Modells läuft nächsten Monat an.
     - frames: etw. läuft an; im Kino anlaufen
     - grammar: valency Intr; auxiliary be
  2. [C1] sich mit der Zeit immer mehr ansammeln, vor allem Kosten oder Schulden
     - example: Durch die Verzögerung sind bereits hohe Kosten angelaufen.
     - grammar: valency Intr; auxiliary be
  3. [B2] durch Feuchtigkeit oder Luft eine trübe Schicht auf der Oberfläche bekommen
     - example: Wenn ich aus der Kälte komme, läuft meine Brille sofort an.
     - grammar: valency Intr; auxiliary be
  4. [B2] plötzlich eine bestimmte Farbe bekommen, besonders im Gesicht
     - example: Vor Wut ist er im Gesicht ganz rot angelaufen.
     - frames: rot/blau anlaufen
     - grammar: valency Intr; auxiliary be
  5. [C1] mit einem Schiff zu einem Ort fahren, um dort anzulegen
     - example: Auf seiner Reise läuft das Kreuzfahrtschiff fünf Häfen an.
     - frames: einen Hafen anlaufen
     - labels: domain nautical
     - grammar: valency Tr; auxiliary have
- relations: derived_from laufen (VERB)

### 23. eindrucksvoll

**Lexeme 1: eindrucksvoll**, ADJ
- grammar: comparable True
- said: /ˈaɪn.dʁʊks.fɔl/ (de-DE)
  1. [B2] so groß, schön oder gut, dass man es sehr bewundert
     - example: Von oben hatten wir einen eindrucksvollen Blick über die ganze Stadt.
- relations: derived_from Eindruck (NOUN)

### 24. wichtig

**Lexeme 1: wichtig**, ADJ
- grammar: comparable True
- said: /ˈvɪç.tɪç/ (de-DE)
  1. [A1] von großer Bedeutung, so dass man es beachten oder ernst nehmen muss
     - example: Für mich ist die Familie wichtiger als jeder Job.
     - frames: wichtig für + Akk; jemandem wichtig sein
     - opposites: unwichtig
  2. [B2] sich selbst für sehr bedeutend haltend und das anderen zeigend
     - example: Mach dich doch nicht so wichtig mit deinem neuen Auto!
     - frames: sich wichtig machen; sich wichtig nehmen
     - labels: attitude disapproving, formality informal

### 25. verdächtig

**Lexeme 1: verdächtig**, ADJ
- grammar: comparable True
- said: /fɛɐ.ˈdɛç.tɪç/ (de-DE)
  1. [B1] so, dass man denkt, etwas sei nicht in Ordnung oder gefährlich
     - example: Bitte melden Sie verdächtige Gepäckstücke sofort dem Personal am Bahnhof.
     - opposites: unverdächtig
  2. [B2] von anderen für schuldig an einer Straftat gehalten
     - example: Zwei junge Männer sind des Diebstahls verdächtig und wurden festgenommen.
     - frames: einer Sache (Gen) verdächtig sein; dringend verdächtig
     - grammar: position Pred
- relations: derived_from Verdacht (NOUN); confusable verdächtigen (VERB): the verb 'to suspect someone', not the adjective 'suspicious/suspected'

### 26. würgen

**Lexeme 1: würgen**, VERB
- grammar: conjugation weak; auxiliary have; valency Both; irregular False
- said: /ˈvʏʁ.ɡən/ (de-DE)
- forms: würgte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈvʏʁk.tə/; gewürgt [Tense=Past, VerbForm=Part] /ɡə.ˈvʏʁkt/
  1. [B2] jemandem den Hals so fest zudrücken, dass er keine Luft bekommt
     - example: Der Täter hatte sein Opfer am Hals gepackt und gewürgt.
     - frames: jmdn. würgen; jmdn. am Hals würgen
     - grammar: valency Tr
  2. [B2] den Drang haben, sich zu übergeben, oder nur mit Mühe schlucken
     - example: Bei dem Geruch musste ich plötzlich heftig würgen.
     - frames: an etw. (Dat) würgen
     - grammar: valency Intr
  3. [C1] sich mit einer schwierigen Arbeit lange und mühsam abplagen
     - example: Wir haben stundenlang an dieser Matheaufgabe gewürgt.
     - frames: an etw. (Dat) würgen
     - labels: formality informal, figurative True
     - grammar: valency Intr

### 27. kostspielig

**Lexeme 1: kostspielig**, ADJ
- grammar: comparable True
- said: /ˈkɔst.ʃpiː.lɪç/ (de-DE)
  1. [B2] sehr teuer und mit hohen Ausgaben verbunden
     - example: Die Reparatur des alten Daches wird für uns ziemlich kostspielig.
     - opposites: billig

### 28. Erzeugung

**Lexeme 1: Erzeugung**, NOUN
- grammar: gender Fem; plural_class -en; countability Both
- said: /ɛɐ.ˈtsɔʏ.ɡʊŋ/ (de-DE)
- forms: Erzeugung [Case=Gen, Number=Sing] /ɛɐ.ˈtsɔʏ.ɡʊŋ/; Erzeugungen [Case=Nom, Number=Plur] /ɛɐ.ˈtsɔʏ.ɡʊ.ŋən/
  1. [B2] Herstellung von Waren, Lebensmitteln oder Energie, meist in großer Menge
     - example: Die Erzeugung von Strom aus Wind wird jedes Jahr billiger.
     - frames: die Erzeugung von + Dat
     - grammar: countability Mass
  2. [C1] Hervorrufen einer Wirkung, eines Gefühls oder eines physikalischen Zustands
     - example: Zur Erzeugung von Spannung braucht dieser Film keine laute Musik.
     - frames: die Erzeugung von + Dat
- relations: verbal_noun_of erzeugen (VERB)

### 29. Broker

**Lexeme 1: Broker**, NOUN
- grammar: gender Masc; plural_class -; countability Count
- said: /ˈbʁoː.kɐ/ (de-DE)
- forms: Brokers [Case=Gen, Number=Sing] /ˈbʁoː.kɐs/; Broker [Case=Nom, Number=Plur] /ˈbʁoː.kɐ/
  1. [C1] Person oder Firma, die im Auftrag anderer mit Wertpapieren handelt
     - example: Über einen Broker kann man heute auch online Aktien kaufen.
     - labels: domain finance

### 30. Liebe

**Lexeme 1: Liebe**, NOUN
- grammar: gender Fem; plural_class -n; countability Both
- said: /ˈliː.bə/ (de-DE)
- forms: Liebe [Case=Gen, Number=Sing] /ˈliː.bə/; Lieben [Case=Nom, Number=Plur] /ˈliː.bən/
  1. [A2] starkes Gefühl der Zuneigung zu einem anderen Menschen
     - example: Nach zwanzig Jahren Ehe ist ihre Liebe immer noch groß.
     - frames: Liebe zu + Dat
     - grammar: countability Mass
     - opposites: Hass
  2. [B1] große Freude an einer Sache oder Tätigkeit
     - example: Seine Liebe zur Musik begann schon in der Kindheit.
     - frames: Liebe zu + Dat
     - grammar: countability Mass
  3. [B1] Person, für die man starke Gefühle der Zuneigung hat
     - example: Du warst immer meine große Liebe, das weißt du.
     - grammar: countability Count

### 31. Räumung

**Lexeme 1: Räumung**, NOUN
- grammar: gender Fem; plural_class -en; countability Count
- said: /ˈʁɔʏ.mʊŋ/ (de-DE)
- forms: Räumung [Case=Gen, Number=Sing] /ˈʁɔʏ.mʊŋ/; Räumungen [Case=Nom, Number=Plur] /ˈʁɔʏ.mʊ.ŋən/
  1. [B2] erzwungenes Verlassen eines Gebäudes, einer Wohnung oder eines Ortes
     - example: Nach der Bombendrohung ordnete die Polizei die Räumung des Bahnhofs an.
  2. [C1] Entfernen von Dingen, die an einer Stelle stören, etwa Schnee oder Minen
     - example: Die Räumung der Straßen vom Schnee dauerte die ganze Nacht.
- relations: verbal_noun_of räumen (VERB)

### 32. Spirit

**Lexeme 1: Spirit**, NOUN
- grammar: gender Masc; plural_class -s; countability Mass
- said: /ˈspɪ.ʁɪt/ (de-DE)
- forms: Spirits [Case=Gen, Number=Sing] /ˈspɪ.ʁɪts/; Spirits [Case=Nom, Number=Plur] /ˈspɪ.ʁɪts/
  1. [C1] besondere Haltung und Stimmung, die eine Gruppe oder eine Sache prägt
     - example: In unserem Verein herrscht ein ganz besonderer Spirit.

### 33. applaudieren

**Lexeme 1: applaudieren**, VERB
- grammar: conjugation weak; auxiliary have; valency Intr; irregular False
- said: /a.plaʊ.ˈdiː.ʁən/ (de-DE)
- forms: applaudierte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /a.plaʊ.ˈdiːɐ.tə/; applaudiert [Tense=Past, VerbForm=Part] /a.plaʊ.ˈdiːɐt/
  1. [B2] mit den Händen klatschen, um Begeisterung oder Zustimmung zu zeigen
     - example: Nach dem Konzert applaudierte das Publikum mehrere Minuten lang.
     - frames: jmdm. applaudieren

### 34. Höhe

**Lexeme 1: Höhe**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /ˈhøː.ə/ (de-DE)
- forms: Höhe [Case=Gen, Number=Sing] /ˈhøː.ə/; Höhen [Case=Nom, Number=Plur] /ˈhøː.ən/
  1. [B1] Ausdehnung eines Gegenstands oder Körpers von unten nach oben
     - example: Der neue Schrank hat eine Höhe von zwei Metern.
     - frames: eine Höhe von + Dat
  2. [B1] Abstand eines Punktes über dem Boden oder dem Meeresspiegel
     - example: Jetzt fliegen wir in einer Höhe von zehn Kilometern.
     - frames: in großer Höhe
     - opposites: Tiefe
  3. [B1] Größe eines Betrags, eines Werts oder einer Menge
     - example: Sie musste eine Strafe in Höhe von hundert Euro zahlen.
     - frames: in Höhe von + Dat
  4. [B2] Stelle im Gelände, die über der Umgebung liegt; Hügel
     - example: Von den Höhen rund um die Stadt sieht man bis zum See.
  5. [C1] der beste oder stärkste Punkt einer Entwicklung
     - example: Auf der Höhe ihres Erfolgs zog sich die Sängerin zurück.
     - frames: auf der Höhe + Gen
     - labels: figurative True
- relations: abstract_noun_of hoch (ADJ)

### 35. weit

**Lexeme 1: weit**, ADJ
- grammar: comparable True
- said: /ˈvaɪt/ (de-DE)
  1. [A1] in großer Entfernung, über eine lange Strecke
     - example: Wohnst du weit von hier, oder können wir zu Fuß gehen?
     - frames: weit von + Dat; weit weg
     - opposites: nah
  2. [A2] locker und groß geschnitten, nicht eng am Körper
     - example: Im Sommer trage ich gern Hosen, die schön weit sind.
     - opposites: eng
  3. [B1] sehr groß und ausgedehnt, mit viel Fläche oder Raum
     - example: Vom Gipfel aus blickten wir über das weite Land.
  4. [B1] in einer Arbeit oder Entwicklung schon fortgeschritten
     - example: Wie weit bist du eigentlich mit deiner Hausarbeit?
     - frames: mit + Dat weit sein
  5. [B1] um sehr viel, in hohem Maß, meist vor einem Komparativ
     - example: Das neue Modell ist weit besser als das alte.
     - frames: weit + Komparativ

### 36. birken

**Lexeme 1: birken**, ADJ
- grammar: comparable False; note almost always used before a noun
- said: /ˈbɪʁ.kən/ (de-DE)
  1. [C1] aus dem hellen Holz eines Laubbaums mit weißer Rinde gemacht
     - example: In der Küche steht noch immer ein alter birkener Schrank.
- relations: derived_from Birke (NOUN)

### 37. einfliessen

**Lexeme 1: einfließen**, VERB
- grammar: conjugation strong; irregular True; auxiliary be; valency Intr; separable True; separable_prefix ein
- said: /ˈaɪn.fliː.sən/ (de-DE)
- spellings: einfließen (de-DE, de-AT); einfliessen (de-CH)
- forms: floss ein [Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /ˈflɔs ˈaɪn/; eingeflossen [Tense=Past, VerbForm=Part] /ˈaɪn.ɡə.flɔ.sən/
  1. [B2] als Teil in etwas aufgenommen werden und es mitbestimmen
     - example: Die Wünsche der Eltern sollen in die Planung der Schule einfließen.
     - frames: in etw. (Akk) einfließen; etw. einfließen lassen
     - labels: figurative True
  2. [B2] von Wasser oder Luft: in einen Raum oder Behälter strömen
     - example: Durch das offene Fenster fließt kalte Luft ins Zimmer ein.
     - frames: in etw. (Akk) einfließen
- relations: derived_from fließen (VERB)

### 38. Anfrage

**Lexeme 1: Anfrage**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /ˈan.fʁaː.ɡə/ (de-DE)
- forms: Anfrage [Case=Gen, Number=Sing] /ˈan.fʁaː.ɡə/; Anfragen [Case=Nom, Number=Plur] /ˈan.fʁaː.ɡən/
  1. [B1] Bitte um eine Auskunft, ein Angebot oder eine Information
     - example: Vielen Dank für Ihre Anfrage, wir melden uns morgen.
     - frames: eine Anfrage an + Akk; Anfrage zu + Dat; auf Anfrage
  2. [C1] offizielle schriftliche Bitte von Abgeordneten an die Regierung um Auskunft
     - example: Die Opposition hat im Bundestag eine kleine Anfrage gestellt.
     - frames: eine Anfrage stellen
     - labels: domain politics
- relations: verbal_noun_of anfragen (VERB)

### 39. vielleicht

**Lexeme 1: vielleicht**, ADV
- grammar: invariable True
- said: /fi.ˈlaɪçt/ (de-DE)
  1. [A1] möglicherweise; es kann sein, aber es ist nicht sicher
     - example: Vielleicht kommt sie morgen doch noch zu uns.
  2. [B1] ungefähr, etwa; bei Zahlen und Mengen, die man schätzt
     - example: Im Saal saßen vielleicht fünfzig Leute, nicht mehr.
**Lexeme 2: vielleicht**, PART
- grammar: invariable True; note Modalpartikel, unbetont, steht im Mittelfeld des Satzes
- said: /fi.ˈlaɪçt/ (de-DE)
  1. [B1] verstärkt einen Ausruf und drückt Staunen, Freude oder Ärger aus
     - example: Du hast mich vielleicht erschreckt!
     - labels: formality informal
  2. [B2] in Fragen, auf die man ein Nein erwartet; drückt oft einen Vorwurf aus
     - example: Ist das vielleicht meine Schuld, dass der Zug Verspätung hat?

### 40. Gesetz

**Lexeme 1: Gesetz**, NOUN
- grammar: gender Neut; plural_class -e; countability Count
- said: /ɡə.ˈzɛts/ (de-DE)
- forms: Gesetzes [Case=Gen, Number=Sing] /ɡə.ˈzɛt.səs/; Gesetze [Case=Nom, Number=Plur] /ɡə.ˈzɛt.sə/
  1. [B1] vom Parlament beschlossene Regel, an die sich alle halten müssen
     - example: Das neue Gesetz tritt im nächsten Jahr in Kraft.
     - frames: gegen das Gesetz verstoßen; nach dem Gesetz
     - labels: domain law
  2. [B2] feste Regel, nach der etwas in der Natur oder im Leben immer abläuft
     - example: Nach den Gesetzen der Physik fällt jeder Stein nach unten.
     - frames: das Gesetz + Gen

### 41. behandeln

**Lexeme 1: behandeln**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False
- said: /bə.ˈhan.dəln/ (de-DE)
- forms: behandelte [Person=3, Number=Sing, Tense=Past, VerbForm=Fin] /bə.ˈhan.dəl.tə/; behandelt [Tense=Past, VerbForm=Part] /bə.ˈhan.dəlt/
  1. [B1] sich einer Person oder Sache gegenüber auf bestimmte Art verhalten
     - example: Du solltest deine Kollegen mit mehr Respekt behandeln.
     - frames: jdn./etw. + Adj. behandeln; jdn. wie etw. behandeln
  2. [B1] als Arzt versuchen, einen Kranken oder eine Krankheit zu heilen
     - example: Welcher Arzt hat Sie nach dem Unfall behandelt?
     - frames: jdn. wegen etw. behandeln; etw. mit etw. behandeln
  3. [B2] über ein Thema genau sprechen oder schreiben
     - example: Im nächsten Kapitel behandeln wir die Geschichte der Stadt.
  4. [B2] einen Stoff oder Gegenstand mit einem Mittel bearbeiten
     - example: Vor dem Streichen wird das Holz mit Öl behandelt.
     - frames: etw. mit etw. behandeln
- relations: derived_from handeln (VERB)

### 42. wald

**Lexeme 1: Wald**, NOUN
- grammar: gender Masc; plural_class -¨er; countability Count
- said: /ˈvalt/ (de-DE)
- forms: Waldes [Case=Gen, Number=Sing] /ˈval.dəs/; Wälder [Case=Nom, Number=Plur] /ˈvɛl.dɐ/
  1. [A2] großes Gebiet, auf dem viele Bäume dicht beieinander wachsen
     - example: Am Sonntag gehen wir gern im Wald spazieren.
  2. [C1] große Menge von dicht nebeneinander aufragenden Dingen
     - example: Im alten Hafen sah man einen Wald von Masten.
     - frames: ein Wald von + Dat
     - labels: figurative True

### 43. schuld

**Lexeme 1: Schuld**, NOUN
- grammar: gender Fem; plural_class -en; countability Both; note im Sinn von Geld meist im Plural: Schulden
- said: /ˈʃʊlt/ (de-DE)
- forms: Schuld [Case=Gen, Number=Sing] /ˈʃʊlt/; Schulden [Case=Nom, Number=Plur] /ˈʃʊl.dən/
  1. [B1] Verantwortung für einen Fehler, einen Schaden oder ein Unglück
     - example: Die Polizei gibt dem Fahrer die Schuld an dem Unfall.
     - frames: die Schuld an + Dat; jemandem die Schuld geben
     - grammar: countability Mass; number Sing
  2. [B2] falsches Handeln gegen Gesetz oder Moral, für das man verantwortlich ist
     - example: Das Gericht konnte seine Schuld nicht beweisen.
     - grammar: countability Mass
     - opposites: Unschuld
  3. [B1] Geld, das man jemandem noch zurückzahlen muss
     - example: Wir haben leider noch hohe Schulden bei der Bank.
     - frames: Schulden bei + Dat; Schulden machen
     - grammar: countability Count
**Lexeme 2: schuld**, ADJ
- grammar: position Pred; invariable True; comparable False
- said: /ˈʃʊlt/ (de-DE)
  1. [B1] verantwortlich für etwas Schlechtes, das passiert ist
     - example: Wer ist eigentlich schuld an diesem Chaos in der Küche?
     - frames: an etwas (Dat) schuld sein
- relations: derived_from Schuld (NOUN)

### 44. Aufsichtsrat

**Lexeme 1: Aufsichtsrat**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈaʊf.zɪçts.ʁaːt/ (de-DE)
- forms: Aufsichtsrats [Case=Gen, Number=Sing] /ˈaʊf.zɪçts.ʁaːts/; Aufsichtsrates [Case=Gen, Number=Sing] /ˈaʊf.zɪçts.ʁaː.təs/; Aufsichtsräte [Case=Nom, Number=Plur] /ˈaʊf.zɪçts.ʁɛː.tə/
  1. [C1] Gremium, das die Leitung einer Aktiengesellschaft kontrolliert und berät
     - example: Im Aufsichtsrat sitzen auch mehrere Vertreter der Arbeitnehmer.
     - labels: domain economics
  2. [C1] Person, die Mitglied in diesem kontrollierenden Gremium eines Unternehmens ist
     - example: Mein Onkel ist Aufsichtsrat in zwei großen Firmen.
     - labels: domain economics
- relations: derived_from Aufsicht (NOUN); confusable Vorstand (NOUN): the Vorstand runs the company day to day; the Aufsichtsrat supervises the Vorstand

### 45. raus

**Lexeme 1: raus**, ADV
- grammar: invariable True; note umgangssprachliche Kurzform von heraus und hinaus
- said: /ˈʁaʊs/ (de-DE)
  1. [A2] von innen nach außen, aus einem Raum oder Ort ins Freie
     - example: Raus mit euch, das Wetter ist doch herrlich!
     - frames: raus aus + Dat; Raus hier!
     - labels: formality informal
     - opposites: rein
  2. [B1] nicht mehr dabei, aus einem Spiel oder einer Sache ausgeschieden
     - example: Nach der zweiten Runde war unsere Mannschaft leider schon raus.
     - frames: raus sein; aus etwas raus sein
     - labels: formality informal, figurative True
- relations: derived_from heraus (ADV)

### 46. verstreichen

**Lexeme 1: verstreichen**, VERB
- grammar: conjugation strong; auxiliary both; valency Both
- said: /fɛɐ.ˈʃtʁaɪ.çən/ (de-DE)
- forms: verstrich [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /fɛɐ.ˈʃtʁɪç/; verstrichen [Tense=Past, VerbForm=Part] /fɛɐ.ˈʃtʁɪ.çən/
  1. [B2] vorbeigehen und zu Ende gehen, besonders von Zeit oder einer Frist
     - example: Die Frist für die Anmeldung ist leider schon verstrichen.
     - frames: etw. verstreichen lassen
     - grammar: valency Intr; auxiliary be
  2. [C1] eine weiche Masse gleichmäßig auf einer Fläche verteilen
     - example: Verstreich den Kleber dünn auf beiden Seiten des Papiers.
     - grammar: valency Tr; auxiliary have
- relations: derived_from streichen (VERB)

### 47. ergründen

**Lexeme 1: ergründen**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False; separable False
- said: /ɛɐ.ˈɡʁʏn.dən/ (de-DE)
- forms: ergründete [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ɛɐ.ˈɡʁʏn.də.tə/; ergründet [Tense=Past, VerbForm=Part] /ɛɐ.ˈɡʁʏn.dət/
  1. [C1] durch genaues Untersuchen etwas Unbekanntes oder Verborgenes herausfinden
     - example: Die Forscher versuchen seit Jahren, die Ursache der Krankheit zu ergründen.
     - frames: die Ursache von etw. ergründen; ergründen, warum ...
- relations: derived_from Grund (NOUN)

### 48. würdigen

**Lexeme 1: würdigen**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr
- said: /ˈvʏʁ.dɪ.ɡən/ (de-DE)
- forms: würdigte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /ˈvʏʁ.dɪç.tə/; gewürdigt [Tense=Past, VerbForm=Part] /ɡə.ˈvʏʁ.dɪçt/
  1. [B2] den Wert einer Person oder Leistung anerkennen und öffentlich loben
     - example: In seiner Rede würdigte der Bürgermeister die Arbeit der vielen Helfer.
     - frames: jmdn./etw. würdigen
  2. [C1] jemanden für wert halten, etwas Bestimmtes zu bekommen
     - example: Als ich hereinkam, hat sie mich keines Blickes gewürdigt.
     - frames: jmdn. einer Sache (Gen) würdigen
     - labels: style elevated
- relations: derived_from würdig (ADJ)

### 49. unkontrolliert

**Lexeme 1: unkontrolliert**, ADJ
- said: /ˈʊn.kɔn.tʁɔ.liːɐt/ (de-DE)
  1. [B2] ohne Aufsicht oder Prüfung durch eine zuständige Person oder Stelle
     - example: Jahrelang sind hier Abfälle unkontrolliert in den Boden gelangt.
     - opposites: kontrolliert
  2. [B2] so heftig oder schnell, dass man es nicht mehr steuern oder beherrschen kann
     - example: Vor Kälte zitterten seine Hände ganz unkontrolliert.
     - opposites: kontrolliert
- relations: derived_from kontrollieren (VERB)

### 50. unzumutbar

**Lexeme 1: unzumutbar**, ADJ
- said: /ˈʊn.tsuː.muːt.baːɐ/ (de-DE)
  1. [C1] so schlecht oder schwer, dass man es von niemandem erwarten oder verlangen kann
     - example: Die hygienischen Zustände in der alten Unterkunft sind wirklich unzumutbar.
     - frames: etw. ist unzumutbar für + Akk; es ist jmdm. unzumutbar, etw. zu tun
     - opposites: zumutbar
- relations: derived_from zumutbar (ADJ)

### 51. Kultur

**Lexeme 1: Kultur**, NOUN
- grammar: gender Fem; plural_class -en; countability Both
- said: /kʊl.ˈtuːɐ/ (de-DE)
- forms: Kultur [Case=Gen, Number=Sing] /kʊl.ˈtuːɐ/; Kulturen [Case=Nom, Number=Plur] /kʊl.ˈtuː.ʁən/
  1. [A2] Kunst, Musik, Theater, Literatur und andere geistige Leistungen der Menschen
     - example: In dieser Stadt gibt es viel Kultur: Museen, Theater und Konzerte.
     - grammar: countability Mass
  2. [B1] Lebensweise, Sitten und Werte eines Volkes oder einer Gruppe
     - example: Wer im Ausland lebt, lernt eine fremde Kultur besser kennen.
     - grammar: countability Count
  3. [C1] im Labor gezüchtete Bakterien, Zellen oder Pilze
     - example: Für den Test legt das Labor eine Kultur aus Bakterien an.
     - labels: domain biology
     - grammar: countability Count

### 52. Gehirn

**Lexeme 1: Gehirn**, NOUN
- grammar: gender Neut; plural_class -e; countability Count
- said: /ɡə.ˈhɪʁn/ (de-DE)
- forms: Gehirns [Case=Gen, Number=Sing] /ɡə.ˈhɪʁns/; Gehirne [Case=Nom, Number=Plur] /ɡə.ˈhɪʁ.nə/
  1. [B1] Organ im Kopf, mit dem Menschen und Tiere denken und fühlen
     - example: Das menschliche Gehirn besteht aus Milliarden von Nervenzellen.
  2. [B2] Fähigkeit, klar zu denken und Dinge zu verstehen
     - example: Benutz doch mal dein Gehirn, bevor du etwas sagst!
     - labels: formality informal
- relations: derived_from Hirn (NOUN)

### 53. scrollen

**Lexeme 1: scrollen**, VERB
- grammar: conjugation weak; auxiliary have; valency Both; irregular False
- said: /ˈskʁoː.lən/ (de-DE)
- forms: scrollte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈskʁoːl.tə/; gescrollt [Tense=Past, VerbForm=Part] /ɡə.ˈskʁoːlt/
  1. [B1] den sichtbaren Inhalt auf einem Bildschirm nach oben, unten oder zur Seite verschieben
     - example: Abends scrolle ich oft noch lange durch die Nachrichten auf meinem Handy.
     - frames: nach unten/oben scrollen; durch etw. scrollen

### 54. ähnlich

**Lexeme 1: ähnlich**, ADJ
- grammar: comparable True
- said: /ˈɛːn.lɪç/ (de-DE)
  1. [A2] in vielen Merkmalen fast gleich, aber nicht ganz gleich
     - example: Deine Tasche ist meiner sehr ähnlich, nur etwas größer.
     - frames: jdm/etw (Dat) ähnlich sein; ähnlich wie
     - opposites: unähnlich
  2. [B2] typisch für eine Person, so wie man es von ihr erwartet
     - example: Dass du schon wieder zu spät kommst, sieht dir ähnlich!
     - frames: jdm ähnlich sehen
     - labels: formality informal, figurative True

### 55. ethnisch

**Lexeme 1: ethnisch**, ADJ
- grammar: comparable False
- said: /ˈɛt.nɪʃ/ (de-DE)
  1. [B2] die gemeinsame Herkunft, Sprache und Kultur einer Volksgruppe betreffend
     - example: In dieser Region leben mehrere ethnische Minderheiten friedlich zusammen.
     - frames: ethnische Minderheit; ethnische Herkunft
- relations: derived_from Ethnie (NOUN)

### 56. bezahlbar

**Lexeme 1: bezahlbar**, ADJ
- grammar: comparable True
- said: /bə.ˈtsaːl.baːɐ/ (de-DE)
  1. [B1] nicht zu teuer, mit einem Preis, den sich normale Menschen leisten können
     - example: In der Großstadt ist es schwer, eine bezahlbare Wohnung zu finden.
     - frames: etw. ist bezahlbar für + Akk
     - opposites: unbezahlbar
- relations: derived_from bezahlen (VERB)

### 57. nehmen

**Lexeme 1: nehmen**, VERB
- grammar: conjugation strong; irregular True; auxiliary have; valency Tr
- said: /ˈneː.mən/ (de-DE)
- forms: nimmt [Person=3, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /ˈnɪmt/; nahm [Person=3, Number=Sing, Tense=Past, Mood=Ind, VerbForm=Fin] /ˈnaːm/; genommen [Tense=Past, VerbForm=Part] /ɡə.ˈnɔ.mən/
  1. [A1] etwas mit der Hand greifen und festhalten
     - example: Sie nimmt ein Buch aus dem Regal und setzt sich.
     - opposites: geben
  2. [A1] sich für etwas entscheiden, besonders beim Kaufen oder Bestellen
     - example: Ich nehme die Suppe und ein Glas Wasser, bitte.
  3. [A1] ein Verkehrsmittel benutzen, um irgendwohin zu fahren
     - example: Heute nehmen wir den Zug, weil die Autobahn voll ist.
  4. [A2] ein Medikament oder eine Droge schlucken oder in den Körper bringen
     - example: Nehmen Sie die Tabletten dreimal täglich nach dem Essen.
  5. [B1] dafür sorgen, dass jemand etwas nicht mehr hat
     - example: Die Angst hat ihm den ganzen Mut genommen.
     - frames: jdm etw. nehmen
  6. [B1] etwas auf eine bestimmte Weise verstehen und darauf reagieren
     - example: Du solltest seine Kritik nicht so persönlich nehmen.
     - frames: etw. ernst nehmen; etw. persönlich nehmen; etw. leicht nehmen

### 58. Anteil

**Lexeme 1: Anteil**, NOUN
- grammar: gender Masc; plural_class -e; declension strong; countability Count
- said: /ˈan.taɪl/ (de-DE)
- forms: Anteils [Case=Gen, Number=Sing] /ˈan.taɪls/; Anteile [Case=Nom, Number=Plur] /ˈan.taɪ.lə/
  1. [B1] Menge von etwas im Verhältnis zum Ganzen, oft in Prozent genannt
     - example: Der Anteil älterer Menschen an der Bevölkerung steigt seit Jahren.
     - frames: Anteil an + Dat
  2. [B1] das, was jemandem von einem Ganzen gehört oder zusteht
     - example: Jeder von uns bekommt seinen Anteil am Gewinn.
     - frames: Anteil an + Dat; Anteil haben an + Dat
  3. [B2] Stück vom Kapital einer Firma, das jemand besitzt
     - example: Im Frühjahr will sie ihre Anteile an der Firma verkaufen.
     - frames: Anteile an + Dat
  4. [B2] Mitgefühl oder Interesse für das, was andere Menschen erleben
     - example: Wir nehmen herzlichen Anteil an eurem schweren Verlust.
     - frames: Anteil nehmen an + Dat
     - grammar: countability Mass; number Sing
- relations: derived_from Teil (NOUN)

### 59. wodurch

**Lexeme 1: wodurch**, ADV
- grammar: invariable True; note Pronominaladverb; fragend oder als Relativadverb
- said: /vo.ˈdʊʁç/ (de-DE)
  1. [B1] fragt nach dem Mittel, Weg oder der Ursache von etwas
     - example: Wodurch ist das Feuer in der Küche entstanden?
  2. [B2] leitet einen Nebensatz ein, der eine Folge des eben Gesagten nennt
     - example: Der Bus kam zu spät, wodurch wir den Zug verpassten.

### 60. zink

**Lexeme 1: Zink**, NOUN
- grammar: gender Neut; countability Mass; number Sing
- said: /ˈtsɪŋk/ (de-DE)
- forms: Zinks [Case=Gen, Number=Sing] /ˈtsɪŋks/
  1. [B2] bläulich weißes Metall, das oft gegen Rost schützt
     - example: Das alte Dach ist mit Zink gedeckt und glänzt grau.

### 61. Mutter

**Lexeme 1: Mutter**, NOUN
- grammar: gender Fem; plural_class -¨; countability Count
- said: /ˈmʊ.tɐ/ (de-DE)
- forms: Mutter [Case=Gen, Number=Sing] /ˈmʊ.tɐ/; Mütter [Case=Nom, Number=Plur] /ˈmʏ.tɐ/
  1. [A1] Frau, die ein Kind geboren hat oder für ein Kind sorgt
     - example: Meine Mutter ruft mich jeden Sonntag am Abend an.
     - opposites: Vater
**Lexeme 2: Mutter**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /ˈmʊ.tɐ/ (de-DE)
- forms: Mutter [Case=Gen, Number=Sing] /ˈmʊ.tɐ/; Muttern [Case=Nom, Number=Plur] /ˈmʊ.tɐn/
  1. [B2] kleines Teil aus Metall mit Gewinde, das man auf eine Schraube dreht
     - example: Zieh die Muttern am Rad bitte noch einmal fest an.
     - labels: domain technology

### 62. ausdrücklich

**Lexeme 1: ausdrücklich**, ADJ
- said: /ˈaʊs.dʁʏk.lɪç/ (de-DE), /aʊs.ˈdʁʏk.lɪç/ (de-DE)
  1. [B2] klar und deutlich gesagt, nicht nur angedeutet oder gemeint
     - example: Der Arzt hat mir ausdrücklich verboten, wieder Sport zu treiben.
     - opposites: stillschweigend

### 63. Juni

**Lexeme 1: Juni**, NOUN
- grammar: gender Masc; plural_class -s; countability Count
- said: /ˈjuː.ni/ (de-DE)
- forms: Juni [Case=Gen, Number=Sing] /ˈjuː.ni/; Junis [Case=Gen, Number=Sing] /ˈjuː.nis/; Junis [Case=Nom, Number=Plur] /ˈjuː.nis/
  1. [A1] der sechste Monat des Jahres, zwischen Mai und Juli
     - example: Im Juni sind die Tage am längsten und die Nächte kurz.
     - frames: im Juni
- relations: confusable Juli (NOUN): the two month names sound very alike; speakers sometimes say Juno and Julei to tell them apart

### 64. stehen

**Lexeme 1: stehen**, VERB
- grammar: conjugation strong; irregular True; auxiliary have; valency Intr; by_variety {"de-AT": {"auxiliary": "be"}, "de-CH": {"auxiliary": "be"}}; note also with sein in southern Germany
- said: /ˈʃteː.ən/ (de-DE)
- forms: stand [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈʃtant/; gestanden [Tense=Past, VerbForm=Part] /ɡə.ˈʃtan.dən/
  1. [A1] aufrecht auf den Füßen sein, ohne zu sitzen oder zu liegen
     - example: Im vollen Bus mussten wir die ganze Fahrt über stehen.
     - opposites: sitzen, liegen
  2. [A1] sich aufrecht an einem bestimmten Platz befinden
     - example: Die Milch steht ganz unten im Kühlschrank.
     - frames: irgendwo stehen
  3. [A2] in einem Text geschrieben oder gedruckt sein
     - example: Was steht heute in der Zeitung?
     - frames: in + Dat stehen
  4. [A2] zu einer Person passen und sie gut aussehen lassen
     - example: Die neue Brille steht dir wirklich gut.
     - frames: jdm (gut) stehen
  5. [B1] nicht mehr in Bewegung oder in Betrieb sein
     - example: Seit dem Unfall geht auf der Autobahn nichts mehr, der Verkehr steht.
  6. [B1] ein bestimmtes Ergebnis während eines Spiels haben
     - example: Nach der ersten Halbzeit steht es zwei zu eins.
     - frames: es steht ... zu ...
     - labels: domain sport
     - grammar: valency Impers
  7. [B1] sich in einer bestimmten Lage oder Situation befinden
     - example: Wie steht es eigentlich um dein neues Projekt?
     - frames: es steht gut/schlecht um + Akk; wie steht es mit + Dat?
     - grammar: valency Impers
  8. [B1] jemanden oder etwas sehr mögen oder attraktiv finden
     - example: Ich stehe nicht so auf laute Musik.
     - frames: auf jdn/etw stehen
     - labels: formality informal
  9. [B2] jemanden unterstützen oder etwas Gesagtes nicht zurücknehmen
     - example: Egal, was passiert, ich stehe immer zu dir.
     - frames: zu jdm/etw stehen
- relations: confusable stellen (VERB): stehen describes being in an upright position; stellen is the action of putting something upright

### 65. vormachen

**Lexeme 1: vormachen**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False; separable True; separable_prefix vor
- said: /ˈfoːɐ.ma.xən/ (de-DE)
- forms: machte vor [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈmax.tə ˈfoːɐ/; vorgemacht [Tense=Past, VerbForm=Part] /ˈfoːɐ.ɡə.maxt/
  1. [B1] jemandem zeigen, wie man etwas macht, damit er es nachmachen kann
     - example: Kannst du mir bitte noch einmal vormachen, wie der Knoten geht?
     - frames: jmdm. etw. vormachen
  2. [B2] jemanden täuschen und ihn etwas Falsches glauben lassen
     - example: Mach dir doch nichts vor, er kommt bestimmt nicht zurück.
     - frames: jmdm. etwas vormachen; sich (Dat) etwas vormachen; jmdm. nichts vormachen können
     - labels: formality informal
- relations: derived_from machen (VERB)

### 66. Zimmer

**Lexeme 1: Zimmer**, NOUN
- grammar: gender Neut; plural_class -; countability Count
- said: /ˈtsɪ.mɐ/ (de-DE)
- forms: Zimmers [Case=Gen, Number=Sing] /ˈtsɪ.mɐs/; Zimmer [Case=Nom, Number=Plur] /ˈtsɪ.mɐ/
  1. [A1] Raum mit Wänden, Tür und Fenster in einer Wohnung oder einem Haus
     - example: Unsere neue Wohnung hat drei Zimmer und einen großen Balkon.
  2. [A1] Raum in einem Hotel oder einer Pension, in dem Gäste gegen Bezahlung übernachten
     - example: Haben Sie noch ein Zimmer für zwei Nächte frei?
     - frames: ein Zimmer buchen/reservieren

### 67. erfrieren

**Lexeme 1: erfrieren**, VERB
- grammar: conjugation strong; auxiliary be; valency Intr; irregular True
- said: /ɛɐ.ˈfʁiː.ʁən/ (de-DE)
- forms: erfror [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ɛɐ.ˈfʁoːɐ/; erfroren [Tense=Past, VerbForm=Part] /ɛɐ.ˈfʁoː.ʁən/
  1. [B2] wegen großer Kälte sterben oder als Pflanze kaputtgehen
     - example: Ohne warme Kleidung wäre er in dieser Nacht fast erfroren.
  2. [C1] durch große Kälte so stark verletzt werden, dass das Gewebe abstirbt
     - example: Beim Aufstieg sind dem Bergsteiger drei Zehen erfroren.
     - frames: jmdm. erfriert etw.; sich (Dat) etw. erfrieren
     - grammar: note said of body parts; also transitive with a dative reflexive: sich die Finger erfrieren
- relations: derived_from frieren (VERB)

### 68. rotieren

**Lexeme 1: rotieren**, VERB
- grammar: conjugation weak; auxiliary have; valency Both; irregular False
- said: /ʁo.ˈtiː.ʁən/ (de-DE)
- forms: rotierte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ʁo.ˈtiːɐ.tə/; rotiert [Tense=Past, VerbForm=Part] /ʁo.ˈtiːɐt/
  1. [B2] sich im Kreis um eine Achse oder einen Punkt bewegen
     - example: Die Erde rotiert in etwa vierundzwanzig Stunden einmal um sich selbst.
     - frames: um + Akk rotieren
     - grammar: valency Intr
  2. [C1] sehr hektisch und aufgeregt sein, weil man viel zu tun hat
     - example: Vor der Prüfung rotiert sie seit Tagen und schläft kaum noch.
     - labels: formality informal, figurative True
     - grammar: valency Intr
  3. [C1] Aufgaben, Plätze oder Spieler regelmäßig in einer festen Reihenfolge wechseln
     - example: Bei uns rotieren die Aufgaben im Team jede Woche.
     - grammar: valency Both

### 69. überblicken

**Lexeme 1: überblicken**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False; separable False
- said: /ˌyː.bɐ.ˈblɪ.kən/ (de-DE)
- forms: überblickte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˌyː.bɐ.ˈblɪk.tə/; überblickt [Tense=Past, VerbForm=Part] /ˌyː.bɐ.ˈblɪkt/
  1. [B2] von einem hohen Punkt aus eine große Fläche ganz sehen können
     - example: Von der Terrasse aus überblickt man das ganze Tal.
  2. [C1] eine Lage oder Sache mit allen Zusammenhängen verstehen und richtig einschätzen
     - example: Die Folgen dieser Entscheidung kann im Moment noch niemand überblicken.
     - frames: etw. überblicken können
     - labels: figurative True
- relations: derived_from blicken (VERB)

### 70. ansiedeln

**Lexeme 1: ansiedeln**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False; separable True; separable_prefix an
- said: /ˈan.ziː.dəln/ (de-DE)
- forms: siedelte an [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈziː.dəl.tə ˈan/; angesiedelt [Tense=Past, VerbForm=Part] /ˈan.ɡə.ziː.dəlt/
  1. [C1] dafür sorgen, dass Menschen, Firmen oder Tiere an einem Ort heimisch werden
     - example: Die Stadt möchte mehr Technologiefirmen im neuen Gewerbegebiet ansiedeln.
     - frames: jmdn./etw. in + Dat ansiedeln
     - grammar: valency Tr
  2. [C1] sich an einem Ort niederlassen, um dort dauerhaft zu bleiben
     - example: In den letzten Jahren haben sich viele junge Familien hier angesiedelt.
     - frames: sich in + Dat ansiedeln
     - grammar: valency Refl
  3. [C1] etwas einer bestimmten Zeit, einem Ort oder einem Bereich zuordnen
     - example: Die Handlung des Films ist in einer kleinen Stadt am Meer angesiedelt.
     - frames: etw. ist in/bei + Dat angesiedelt
     - grammar: valency Tr; note mostly in the passive: angesiedelt sein
- relations: derived_from siedeln (VERB)

### 71. Gegenwart

**Lexeme 1: Gegenwart**, NOUN
- grammar: gender Fem; number Sing; countability Mass
- said: /ˈɡeː.ɡən.vaʁt/ (de-DE)
- forms: Gegenwart [Case=Gen, Number=Sing] /ˈɡeː.ɡən.vaʁt/
  1. [B1] die Zeit, in der wir jetzt leben
     - example: Viele Menschen interessieren sich mehr für die Gegenwart als für die Geschichte.
     - opposites: Vergangenheit
  2. [B2] Anwesenheit einer Person am selben Ort wie andere
     - example: In seiner Gegenwart fühle ich mich immer etwas unsicher.
     - frames: in jemandes Gegenwart; in Gegenwart + Gen
     - opposites: Abwesenheit
  3. [B1] Zeitform des Verbs für Handlungen, die jetzt geschehen
     - example: Setz bitte alle Verben in diesem Satz in die Gegenwart.
     - labels: domain grammar
     - opposites: Vergangenheit

### 72. gleich

**Lexeme 1: gleich**, ADJ
- grammar: comparable False
- said: /ˈɡlaɪç/ (de-DE)
  1. [A1] in allen Merkmalen übereinstimmend, nicht anders als etwas anderes
     - example: Meine Schwester und ich haben zufällig die gleichen Schuhe gekauft.
     - opposites: verschieden
  2. [A2] in Wert, Größe, Menge oder Rang nicht verschieden
     - example: Frauen und Männer sollen für ihre Arbeit gleichen Lohn bekommen.
     - opposites: ungleich
  3. [B1] ohne Bedeutung für jemanden, so dass es ihn nicht interessiert
     - example: Ob wir heute oder morgen fahren, ist mir völlig gleich.
     - frames: jmdm. gleich sein
     - grammar: position Pred
- relations: confusable derselbe (DET): der gleiche means 'of the same kind', derselbe means 'the very same one'; learners mix them up
**Lexeme 2: gleich**, ADV
- said: /ˈɡlaɪç/ (de-DE)
  1. [A1] sofort oder sehr bald, in wenigen Minuten
     - example: Warte bitte kurz, ich bin gleich wieder da.
  2. [A2] räumlich ganz nah, direkt neben oder hinter etwas
     - example: Die Bäckerei ist gleich um die Ecke.
  3. [B2] betont, dass es überraschend viele sind, auf einmal
     - example: Sie hat sich in der Bibliothek gleich fünf Bücher ausgeliehen.
**Lexeme 3: gleich**, PART
- said: /ˈɡlaɪç/ (de-DE)
  1. [B2] in Fragen: zeigt, dass man sich an etwas zu erinnern versucht
     - example: Wie hieß noch gleich der Film, den wir letzte Woche gesehen haben?
     - labels: formality informal
**Lexeme 4: gleich**, ADP
- said: /ˈɡlaɪç/ (de-DE)
  1. [C1] so wie, in der Art von
     - example: Leicht wie ein Blatt, gleich einem Vogel schwebte der Drachen über den Feldern.
     - frames: gleich + Dat
     - labels: style literary

### 73. umdenken

**Lexeme 1: umdenken**, VERB
- grammar: conjugation mixed; irregular True; auxiliary have; separable True; separable_prefix um; valency Intr
- said: /ˈʊm.dɛŋ.kən/ (de-DE)
- forms: dachte um [Person=3, Number=Sing, Tense=Past, Mood=Ind, VerbForm=Fin] /ˈdax.tə ˈʊm/; umgedacht [Tense=Past, VerbForm=Part] /ˈʊm.ɡə.daxt/
  1. [B2] die eigene Einstellung grundlegend ändern und Dinge neu betrachten
     - example: Beim Klimaschutz müssen wir alle dringend umdenken.
- relations: derived_from denken (VERB)

### 74. gewöhnungsbedürftig

**Lexeme 1: gewöhnungsbedürftig**, ADJ
- grammar: comparable True
- said: /ɡə.ˈvøː.nʊŋs.bə.ˌdʏʁf.tɪç/ (de-DE)
- parts: Gewöhnungs (Gewöhnung) + bedürftig (bedürftig)
  1. [C1] am Anfang ungewohnt und fremd, sodass man es erst mit der Zeit mag
     - example: Der Geschmack dieses Tees ist anfangs etwas gewöhnungsbedürftig.

### 75. Fett

**Lexeme 1: Fett**, NOUN
- grammar: gender Neut; plural_class -e; countability Both
- said: /ˈfɛt/ (de-DE)
- forms: Fettes [Case=Gen, Number=Sing] /ˈfɛ.təs/; Fetts [Case=Gen, Number=Sing] /ˈfɛts/; Fette [Case=Nom, Number=Plur] /ˈfɛ.tə/
  1. [A2] weicher Stoff aus Pflanzen oder Tieren, zum Kochen und Braten
     - example: Für dieses Rezept brauchst du nur wenig Fett in der Pfanne.
  2. [B1] weiches Gewebe unter der Haut, in dem der Körper Energie speichert
     - example: Durch regelmäßigen Sport verliert man langsam Fett am Bauch.
     - grammar: countability Mass
- relations: derived_from fett (ADJ)
**Lexeme 2: fett**, ADJ
- grammar: comparable True
- said: /ˈfɛt/ (de-DE)
  1. [B1] von Speisen: mit einem hohen Anteil an Öl, Butter oder Speck
     - example: Fettes Essen am Abend liegt mir schwer im Magen.
     - opposites: mager
  2. [B1] von Menschen oder Tieren: sehr dick und schwer
     - example: Der Hund des Nachbarn ist in letzter Zeit richtig fett geworden.
     - labels: attitude derogatory
     - opposites: dünn
  3. [B2] von Schrift: mit dicken, kräftigen Linien gedruckt
     - example: Die wichtigsten Wörter sind im Text fett gedruckt.
  4. [C1] sehr gut, toll und beeindruckend
     - example: Boah, dein neues Auto ist echt fett!
     - labels: formality slang

### 76. fechten

**Lexeme 1: fechten**, VERB
- grammar: conjugation strong; auxiliary have; valency Intr; irregular True
- said: /ˈfɛç.tən/ (de-DE)
- forms: ficht [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈfɪçt/; focht [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈfɔxt/; gefochten [Tense=Past, VerbForm=Part] /ɡə.ˈfɔx.tən/
  1. [B2] mit einer langen, schmalen Klinge nach festen Regeln gegen jemanden kämpfen
     - example: Seit ihrer Kindheit ficht sie zweimal pro Woche im Verein.
     - frames: mit jmdm. fechten; gegen jmdn. fechten
  2. [C1] sich mit großem Einsatz und Mut für oder gegen etwas einsetzen
     - example: Ein Leben lang hat er für die Rechte der Arbeiter gefochten.
     - frames: für etw. fechten; gegen etw. fechten
     - labels: style elevated, figurative True

### 77. Osten

**Lexeme 1: Osten**, NOUN
- grammar: gender Masc; number Sing; note als Himmelsrichtung meist ohne Artikel: nach Osten, aus Osten
- said: /ˈɔs.tən/ (de-DE)
- forms: Ostens [Case=Gen, Number=Sing] /ˈɔs.təns/
  1. [A2] Himmelsrichtung, in der morgens die Sonne aufgeht
     - example: Die Sonne geht im Osten auf und im Westen unter.
     - frames: nach Osten; aus/von Osten; im Osten
     - opposites: Westen
  2. [B1] Teil eines Landes oder einer Stadt, der zum Sonnenaufgang hin liegt
     - example: Meine Großeltern wohnen im Osten des Landes auf einem Bauernhof.
     - frames: im Osten + Gen; im Osten von + Dat
     - opposites: Westen
  3. [B2] Gesamtheit der Länder, die von Westeuropa aus in Richtung Asien liegen
     - example: Nach dem Krieg wuchs das Misstrauen zwischen dem Osten und dem Westen.
     - opposites: Westen

### 78. entstehen

**Lexeme 1: entstehen**, VERB
- grammar: conjugation strong; irregular True; auxiliary be; valency Intr
- said: /ɛnt.ˈʃteː.ən/ (de-DE)
- forms: entstand [Person=3, Number=Sing, Tense=Past, Mood=Ind, VerbForm=Fin] /ɛnt.ˈʃtant/; entstanden [VerbForm=Part, Tense=Past] /ɛnt.ˈʃtan.dən/
  1. [B1] neu zu sein beginnen oder sich nach und nach bilden
     - example: Am Stadtrand entsteht gerade ein neues Wohnviertel mit vielen Grünflächen.
     - frames: entstehen aus + Dat
     - opposites: vergehen
  2. [B2] sich als meist unerwünschte Folge aus einem Ereignis ergeben
     - example: Durch den Wasserschaden sind uns leider hohe Kosten entstanden.
     - frames: jdm entstehen; entstehen durch + Akk
- relations: derived_from stehen (VERB)

### 79. gestern

**Lexeme 1: gestern**, ADV
- said: /ˈɡɛs.tɐn/ (de-DE)
  1. [A1] am Tag direkt vor dem heutigen Tag
     - example: Gestern war ich mit meiner Schwester im Kino.
     - opposites: morgen
  2. [B2] in einer früheren Zeit, die schon vorbei ist
     - example: Mit den Ideen von gestern lösen wir die Probleme nicht.
     - frames: von gestern
     - labels: figurative True
**Lexeme 2: Gestern**, NOUN
- grammar: gender Neut; number Sing; countability Mass
- said: /ˈɡɛs.tɐn/ (de-DE)
  1. [C1] die Zeit, die schon vorbei ist; die Vergangenheit
     - example: Viele Menschen hängen zu sehr am Gestern.
     - labels: style elevated
- relations: derived_from gestern (ADV)

### 80. weste

**Lexeme 1: Weste**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /ˈvɛs.tə/ (de-DE)
- forms: Weste [Case=Gen, Number=Sing] /ˈvɛs.tə/; Westen [Case=Nom, Number=Plur] /ˈvɛs.tən/
  1. [B1] Kleidungsstück ohne Ärmel, das man über Hemd oder Bluse trägt
     - example: Zum Anzug trägt er heute eine graue Weste mit Knöpfen.
  2. [C1] guter Ruf eines Menschen, der nichts Unrechtes getan hat
     - example: Bisher hatte der Minister eine weiße Weste, doch jetzt gibt es Vorwürfe.
     - frames: eine weiße/saubere Weste haben
     - labels: figurative True

### 81. vergleichen

**Lexeme 1: vergleichen**, VERB
- grammar: conjugation strong; irregular True; auxiliary have; valency Tr
- said: /fɛɐ.ˈɡlaɪ.çən/ (de-DE)
- forms: verglich [Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /fɛɐ.ˈɡlɪç/; verglichen [Tense=Past, VerbForm=Part] /fɛɐ.ˈɡlɪ.çən/
  1. [A2] prüfen, worin Personen oder Dinge übereinstimmen und sich unterscheiden
     - example: Vor dem Kauf solltest du die Preise in mehreren Geschäften vergleichen.
     - frames: etw. mit etw. (Dat) vergleichen; sich mit jmdm. vergleichen
  2. [B1] sagen, dass eine Person oder Sache einer anderen sehr ähnlich ist
     - example: Kritiker vergleichen seine Bilder oft mit den Werken alter Meister.
     - frames: jmdn./etw. mit jmdm./etw. (Dat) vergleichen

### 82. flattern

**Lexeme 1: flattern**, VERB
- grammar: conjugation weak; auxiliary both; valency Intr; irregular False
- said: /ˈfla.tɐn/ (de-DE)
- forms: flatterte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈfla.tɐ.tə/; geflattert [Tense=Past, VerbForm=Part] /ɡə.ˈfla.tɐt/
  1. [B2] mit schnellen, unruhigen Bewegungen durch die Luft fliegen
     - example: Ein bunter Schmetterling ist über die Wiese geflattert.
     - frames: irgendwohin flattern
     - grammar: auxiliary be; note with haben when the movement stays in one place
  2. [B2] sich im Wind schnell und lose hin und her bewegen
     - example: Auf dem Dach des Rathauses flattert eine große Fahne im Wind.
     - frames: im Wind flattern
     - grammar: auxiliary have
  3. [C1] unruhig, schnell und unregelmäßig schlagen oder zucken
     - example: Vor der Prüfung flattert mein Herz jedes Mal vor Aufregung.
     - grammar: auxiliary have

### 83. vorangehen

**Lexeme 1: vorangehen**, VERB
- grammar: conjugation strong; auxiliary be; valency Intr; irregular True; separable True; separable_prefix voran
- said: /fo.ˈʁan.ɡeː.ən/ (de-DE)
- forms: ging voran [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈɡɪŋ fo.ˈʁan/; vorangegangen [Tense=Past, VerbForm=Part] /fo.ˈʁan.ɡə.ɡa.ŋən/
  1. [B1] sich in Richtung auf ein Ziel weiterentwickeln und Fortschritte machen
     - example: Mit dem neuen Team geht das Projekt endlich gut voran.
     - frames: es geht voran; mit etw. geht es voran
  2. [C1] zeitlich früher als etwas anderes geschehen
     - example: Der Entscheidung gingen lange und schwierige Diskussionen voran.
     - frames: etw. geht + Dat voran
  3. [B2] vor einer Person oder Gruppe gehen, die einem folgt
     - example: Geh du bitte voran, du kennst den Weg besser.
     - frames: jmdm. vorangehen; mit gutem Beispiel vorangehen
- relations: derived_from gehen (VERB)

### 84. vorgehen

**Lexeme 1: vorgehen**, VERB
- grammar: conjugation strong; irregular True; auxiliary be; separable True; separable_prefix vor; valency Intr
- said: /ˈfoːɐ.ɡeː.ən/ (de-DE)
- forms: ging vor [Tense=Past, VerbForm=Fin, Person=3, Number=Sing] /ˈɡɪŋ ˈfoːɐ/; vorgegangen [Tense=Past, VerbForm=Part] /ˈfoːɐ.ɡə.ɡa.ŋən/
  1. [B2] auf eine bestimmte Art handeln, um ein Ziel zu erreichen
     - example: Die Regierung will künftig härter gegen Steuerbetrug vorgehen.
     - frames: gegen + Akk vorgehen; irgendwie vorgehen
  2. [B1] geschehen, passieren, oft ohne dass man es genau versteht
     - example: Was in ihm vorging, konnte niemand ahnen.
     - frames: in jdm vorgehen
  3. [B1] von einer Uhr: mehr Zeit zeigen, als es wirklich ist
     - example: Meine Uhr geht seit gestern fünf Minuten vor.
     - opposites: nachgehen
  4. [B1] früher als die anderen an einen Ort aufbrechen, vorausmarschieren
     - example: Geh schon mal vor, ich komme gleich nach!
  5. [B2] wichtiger sein als etwas anderes und deshalb zuerst kommen
     - example: Sicherheit geht immer vor, auch wenn es länger dauert.
**Lexeme 2: Vorgehen**, NOUN
- grammar: gender Neut; number Sing; countability Mass
- said: /ˈfoːɐ.ɡeː.ən/ (de-DE)
- forms: Vorgehens [Case=Gen, Number=Sing] /ˈfoːɐ.ɡeː.əns/
  1. [B2] Art und Weise, wie jemand handelt, um etwas zu erreichen
     - example: Das harte Vorgehen der Polizei wurde scharf kritisiert.
- relations: verbal_noun_of vorgehen (VERB)

### 85. allgemein

**Lexeme 1: allgemein**, ADJ
- grammar: comparable True
- said: /ˈal.ɡə.maɪn/ (de-DE), /al.ɡə.ˈmaɪn/ (de-DE)
  1. [B1] die meisten Menschen betreffend oder bei ihnen verbreitet
     - example: Es ist allgemein bekannt, dass Rauchen der Gesundheit schadet.
  2. [B1] nicht auf Einzelheiten oder einen bestimmten Fall bezogen
     - example: Der Vortrag blieb leider sehr allgemein und nannte keine Zahlen.
     - opposites: speziell

### 86. bestellen

**Lexeme 1: bestellen**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; separable False
- said: /bə.ˈʃtɛ.lən/ (de-DE)
- forms: bestellte [Person=3, Number=Sing, Tense=Past, VerbForm=Fin] /bə.ˈʃtɛl.tə/; bestellt [Tense=Past, VerbForm=Part] /bə.ˈʃtɛlt/
  1. [A1] sagen oder schreiben, dass man eine Ware oder ein Essen haben möchte
     - example: Heute Abend bestellen wir eine Pizza beim Italiener um die Ecke.
     - frames: etw. bestellen; etw. bei jmdm. bestellen
  2. [A2] einen Tisch, ein Zimmer oder Plätze im Voraus reservieren lassen
     - example: Für Samstag haben wir einen Tisch für vier Personen bestellt.
  3. [B2] jemanden auffordern, zu einer bestimmten Zeit an einen Ort zu kommen
     - example: Der Chef hat mich für morgen früh in sein Büro bestellt.
     - frames: jmdn. zu sich bestellen; jmdn. irgendwohin bestellen
  4. [B1] eine Nachricht oder Grüße an eine andere Person weitergeben
     - example: Bestell deiner Mutter bitte schöne Grüße von mir!
     - frames: jmdm. etw. bestellen
  5. [C1] jemanden offiziell für ein Amt oder eine Aufgabe bestimmen
     - example: Das Gericht hat für das Kind einen Vormund bestellt.
     - frames: jmdn. zu etw. bestellen
     - labels: style official
  6. [C1] ein Feld bearbeiten, damit man dort etwas anbauen kann
     - example: Im Frühling bestellen die Bauern ihre Felder mit Weizen.
     - labels: domain agriculture
- relations: derived_from stellen (VERB)

### 87. irgendwie

**Lexeme 1: irgendwie**, ADV
- grammar: invariable True
- said: /ˈɪʁ.ɡənt.viː/ (de-DE)
  1. [A2] auf eine Art und Weise, die man nicht kennt oder nicht nennt
     - example: Wir schaffen das schon, irgendwie finden wir eine Lösung.
  2. [B1] in gewisser Weise, ohne dass man genau sagen kann, warum
     - example: Ich finde den neuen Kollegen irgendwie komisch.
     - labels: formality informal
- relations: derived_from irgend (ADV)

### 88. vorsichtig

**Lexeme 1: vorsichtig**, ADJ
- grammar: comparable True
- said: /ˈfoːɐ.zɪç.tɪç/ (de-DE)
  1. [A2] so handelnd, dass man Gefahren, Schäden und Fehler vermeidet
     - example: Fahr bitte vorsichtig, die Straßen sind heute sehr glatt.
     - frames: vorsichtig mit + Dat sein
     - opposites: unvorsichtig
  2. [B2] eher niedrig angesetzt, damit man nicht übertreibt
     - example: Nach vorsichtigen Schätzungen kostet der Umbau zwei Millionen Euro.
- relations: adjective_of Vorsicht (NOUN)

### 89. stilvoll

**Lexeme 1: stilvoll**, ADJ
- grammar: comparable True
- said: /ˈʃtiːl.fɔl/ (de-DE)
- parts: Stil (Stil) + voll (voll)
  1. [B2] mit gutem Geschmack und elegant gestaltet, gekleidet oder gemacht
     - example: Das kleine Hotel am See ist sehr stilvoll eingerichtet.
     - opposites: stillos

### 90. vorn

**Lexeme 1: vorn**, ADV
- said: /ˈfɔʁn/ (de-DE)
  1. [A2] auf der Seite oder in dem Teil, der in Blickrichtung zuerst kommt
     - example: Setz dich bitte vorn ins Auto, hinten ist kein Platz mehr.
     - frames: nach vorn; da vorn
     - opposites: hinten
  2. [B1] noch einmal ganz vom Anfang an, ganz neu
     - example: Die Datei ist weg, jetzt muss ich wieder von vorn anfangen.
     - frames: von vorn (anfangen)

### 91. anhängig

**Lexeme 1: anhängig**, ADJ
- grammar: comparable False
- said: /ˈan.hɛ.ŋɪç/ (de-DE)
  1. [C1] bei einem Gericht oder einer Behörde eingereicht und noch nicht entschieden
     - example: Gegen den früheren Bürgermeister ist derzeit ein Verfahren wegen Bestechung anhängig.
     - frames: etw. ist bei + Dat anhängig; etw. anhängig machen
     - labels: domain law
- relations: derived_from anhängen (VERB); confusable abhängig (ADJ): differs by only one letter; abhängig means 'dependent', anhängig means 'pending in court'

### 92. vollbringen

**Lexeme 1: vollbringen**, VERB
- grammar: conjugation mixed; auxiliary have; valency Tr; irregular True; separable False
- said: /fɔl.ˈbʁɪ.ŋən/ (de-DE)
- forms: vollbrachte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /fɔl.ˈbʁax.tə/; vollbracht [Tense=Past, VerbForm=Part] /fɔl.ˈbʁaxt/
  1. [C1] etwas Schwieriges oder Besonderes mit Erfolg tun und zu Ende führen
     - example: Mit dieser Rettung haben die Feuerwehrleute eine große Leistung vollbracht.
     - frames: eine Leistung vollbringen; ein Wunder vollbringen; eine Tat vollbringen
     - labels: style elevated

### 93. auslesen

**Lexeme 1: auslesen**, VERB
- grammar: conjugation strong; auxiliary have; valency Tr; separable True; separable_prefix aus
- said: /ˈaʊs.leː.zən/ (de-DE)
- forms: liest aus [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈliːst ˈaʊs/; las aus [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈlaːs ˈaʊs/; ausgelesen [Tense=Past, VerbForm=Part] /ˈaʊs.ɡə.leː.zən/
  1. [B2] gespeicherte Daten aus einem Gerät oder Speicher holen und verfügbar machen
     - example: Das Programm liest die Messwerte des Sensors automatisch jede Minute aus.
     - frames: etw. aus + Dat auslesen
     - labels: domain computing
  2. [B1] mit einem Buch oder Text bis zur letzten Seite fertig werden
     - example: Ich habe den dicken Roman an einem einzigen Wochenende ausgelesen.
     - frames: ein Buch auslesen
  3. [C1] aus einer Menge bestimmte Stücke heraussuchen und von den übrigen trennen
     - example: Vor dem Kochen sollte man die schlechten Erbsen sorgfältig auslesen.
- relations: derived_from lesen (VERB)

### 94. dunkel

**Lexeme 1: dunkel**, ADJ
- grammar: comparable True; note loses e before an ending: dunkler, ein dunkles Zimmer
- said: /ˈdʊŋ.kəl/ (de-DE)
- forms: dunkler [Degree=Cmp] /ˈdʊŋ.klɐ/
  1. [A1] ohne Licht oder mit sehr wenig Licht
     - example: Im Winter wird es hier schon um fünf Uhr dunkel.
     - opposites: hell
  2. [A2] von einer Farbe, die fast schwarz oder nicht hell ist
     - example: Zur Hochzeit trägst du am besten einen dunklen Anzug.
     - opposites: hell
  3. [B2] von einem Ton oder einer Stimme: tief und voll klingend
     - example: Der Sänger hat eine schöne, warme und dunkle Stimme.
     - opposites: hell
  4. [B2] nicht klar und deutlich, schwer zu verstehen oder zu erkennen
     - example: Ich habe nur eine dunkle Erinnerung an meine Kindheit.
     - labels: figurative True
  5. [B2] nicht ehrlich und vermutlich nicht legal, verdächtig
     - example: Mit seinen dunklen Geschäften hat er viel Geld verdient.
     - labels: figurative True
**Lexeme 2: Dunkel**, NOUN
- grammar: gender Neut; number Sing; countability Mass
- said: /ˈdʊŋ.kəl/ (de-DE)
- forms: Dunkels [Case=Gen, Number=Sing] /ˈdʊŋ.kəls/
  1. [C1] Umgebung ohne Licht, in der man kaum etwas sehen kann
     - example: Plötzlich trat eine Gestalt aus dem Dunkel des Waldes.
     - labels: style literary
- relations: derived_from dunkel (ADJ)

### 95. bloggen

**Lexeme 1: bloggen**, VERB
- grammar: conjugation weak; auxiliary have; valency Intr; irregular False
- said: /ˈblɔ.ɡən/ (de-DE)
- forms: bloggte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈblɔk.tə/; gebloggt [Tense=Past, VerbForm=Part] /ɡə.ˈblɔkt/
  1. [B2] regelmäßig eigene Texte auf einer persönlichen Seite im Internet veröffentlichen
     - example: Seit zwei Jahren bloggt meine Schwester über vegetarisches Kochen.
     - frames: über + Akk bloggen
- relations: derived_from Blog (NOUN)

### 96. unterschlagen

**Lexeme 1: unterschlagen**, VERB
- grammar: conjugation strong; auxiliary have; valency Tr; irregular True; separable False
- said: /ʊn.tɐ.ˈʃlaː.ɡən/ (de-DE)
- forms: unterschlägt [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ʊn.tɐ.ˈʃlɛːkt/; unterschlug [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ʊn.tɐ.ˈʃluːk/; unterschlagen [Tense=Past, VerbForm=Part] /ʊn.tɐ.ˈʃlaː.ɡən/
  1. [C1] Geld oder Dinge, die einem anvertraut wurden, heimlich und unrechtmäßig für sich behalten
     - example: Der Kassierer soll über Jahre hohe Summen aus der Vereinskasse unterschlagen haben.
     - frames: Geld unterschlagen
  2. [C1] etwas Wichtiges absichtlich nicht mitteilen oder nicht erwähnen
     - example: In seinem Bericht unterschlägt er die wichtigsten Fakten einfach.
     - frames: jmdm. etw. unterschlagen
- relations: derived_from schlagen (VERB)
**Lexeme 2: unterschlagen**, VERB
- grammar: conjugation strong; auxiliary have; valency Tr; irregular True; separable True; separable_prefix unter
- said: /ˈʊn.tɐ.ʃlaː.ɡən/ (de-DE)
- forms: schlägt unter [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈʃlɛːkt ˈʊn.tɐ/; schlug unter [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈʃluːk ˈʊn.tɐ/; untergeschlagen [Tense=Past, VerbForm=Part] /ˈʊn.tɐ.ɡə.ʃlaː.ɡən/
  1. [C1] die Beine oder Arme kreuzen oder im Sitzen unter den Körper legen
     - example: Auf dem Teppich saß sie mit untergeschlagenen Beinen und las.
     - frames: die Beine unterschlagen
- relations: derived_from schlagen (VERB)

### 97. glasklar

**Lexeme 1: glasklar**, ADJ
- grammar: comparable False
- said: /ˈɡlaːs.ˈklaːɐ/ (de-DE)
- parts: Glas (Glas) + klar (klar)
  1. [B2] völlig eindeutig und leicht zu verstehen, ohne jeden Zweifel
     - example: Die Regeln für die Prüfung sind eigentlich glasklar formuliert.
     - labels: figurative True
  2. [B2] völlig durchsichtig und rein, ohne jede Trübung
     - example: In diesem See ist das Wasser so glasklar, dass man den Grund sieht.
     - opposites: trüb

### 98. verdecken

**Lexeme 1: verdecken**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False
- said: /fɛɐ.ˈdɛ.kən/ (de-DE)
- forms: verdeckte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /fɛɐ.ˈdɛk.tə/; verdeckt [Tense=Past, VerbForm=Part] /fɛɐ.ˈdɛkt/
  1. [B2] vor oder über etwas sein, sodass man es nicht mehr sehen kann
     - example: Dicke Wolken verdecken heute fast den ganzen Himmel.
     - frames: jmdm. die Sicht verdecken; etw. mit + Dat verdecken
  2. [C1] etwas vor anderen geheim halten, damit es niemand bemerkt
     - example: Mit seinem Lächeln wollte er nur seine Unsicherheit verdecken.
     - labels: figurative True
     - opposites: aufdecken
- relations: derived_from decken (VERB)

### 99. natürlich

**Lexeme 1: natürlich**, ADJ
- grammar: comparable True
- said: /na.ˈtyːɐ.lɪç/ (de-DE)
  1. [B1] so, wie es in der Umwelt vorkommt; nicht von Menschen hergestellt
     - example: Dieser Saft enthält nur natürliche Farbstoffe und keinen Zucker.
     - opposites: künstlich
  2. [B2] im Verhalten echt und locker, ohne sich zu verstellen
     - example: Auf dem Foto wirkst du ganz natürlich und entspannt.
     - opposites: gekünstelt
  3. [B2] normal und leicht zu verstehen, so wie man es erwarten kann
     - example: Es ist doch ganz natürlich, vor einer Prüfung nervös zu sein.
     - grammar: position Pred
     - opposites: unnatürlich
- relations: adjective_of Natur (NOUN)
**Lexeme 2: natürlich**, ADV
- said: /na.ˈtyːɐ.lɪç/ (de-DE)
  1. [A2] so wie man es erwartet; ganz sicher, ohne Zweifel
     - example: Kommst du heute Abend mit? – Natürlich komme ich mit!
  2. [B1] zugebend, vor einem Einwand: zwar, allerdings
     - example: Die Wohnung ist natürlich teuer, aber die Lage ist perfekt.
- relations: adverb_of natürlich (ADJ)

### 100. Neugier

**Lexeme 1: Neugier**, NOUN
- grammar: gender Fem; number Sing; countability Mass
- said: /ˈnɔʏ.ɡiːɐ/ (de-DE)
- forms: Neugier [Case=Gen, Number=Sing] /ˈnɔʏ.ɡiːɐ/
  1. [B1] starker Wunsch, etwas zu erfahren oder Unbekanntes kennenzulernen
     - example: Ihre Neugier auf die fremde Stadt wurde jeden Tag größer.
     - frames: Neugier auf + Akk; aus Neugier
