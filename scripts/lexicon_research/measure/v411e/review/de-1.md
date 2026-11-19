# German: entries to check (1 of 1)

These are entries of a learner's dictionary of German, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of German.

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

Do not report: the order of senses, a CEFR level off by one step, how syllables are divided, a
definition you would have worded differently when the given one is true, or anything about style.

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

### 1. Einführung

**Lexeme 1: Einführung**, NOUN
- grammar: gender Fem; plural_class -en; countability Count
- said: /ˈaɪn.fyː.ʁʊŋ/ (de-DE)
- forms: Einführung [Case=Gen, Number=Sing] /ˈaɪn.fyː.ʁʊŋ/; Einführungen [Case=Nom, Number=Plur] /ˈaɪn.fyː.ʁʊ.ŋən/
  1. [B1] Beginn der Nutzung von etwas Neuem, das es vorher nicht gab
     - example: Seit der Einführung der neuen Software arbeiten wir viel schneller.
     - frames: Einführung von + Dat
     - collocations: seit der Einführung; die Einführung einer Steuer; die Einführung neuer Technologien; die Einführung eines Produkts; die Einführung planen
  2. [B1] erste Erklärungen, die jemanden mit einem neuen Gebiet vertraut machen
     - example: Am ersten Arbeitstag bekommst du eine kurze Einführung in das System.
     - frames: Einführung in + Akk; jmdm. eine Einführung geben
     - collocations: eine kurze Einführung; eine Einführung geben; eine Einführung erhalten
  3. [B2] Buch, Kurs oder Vortrag, der Anfängern die Grundlagen eines Fachs erklärt
     - example: Diese Einführung in die Statistik ist auch für Laien gut verständlich.
     - frames: Einführung in + Akk
     - collocations: eine Einführung schreiben; eine verständliche Einführung
- relations: verbal_noun_of einführen (VERB)

### 2. fernsehen

**Lexeme 1: fernsehen**, VERB
- grammar: conjugation strong; auxiliary have; valency Intr; irregular True; separable True; separable_prefix fern
- said: /ˈfɛʁn.ˌzeː.ən/ (de-DE)
- parts: fern (fern) + sehen (sehen)
- forms: sieht fern [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈziːt ˈfɛʁn/; sah fern [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈzaː ˈfɛʁn/; ferngesehen [Tense=Past, VerbForm=Part] /ˈfɛʁn.ɡə.ˌzeː.ən/
  1. [A1] Sendungen eines Senders auf einem Bildschirm anschauen
     - example: Nach dem Abendessen sehen wir meistens noch eine Stunde fern.
     - collocations: viel fernsehen; stundenlang fernsehen; abends fernsehen; zu viel fernsehen
**Lexeme 2: Fernsehen**, NOUN
- grammar: gender Neut; number Sing
- said: /ˈfɛʁn.ˌzeː.ən/ (de-DE)
- forms: Fernsehens [Case=Gen, Number=Sing] /ˈfɛʁn.ˌzeː.əns/
  1. [A1] Medium, das Bilder und Ton von Sendungen an viele Bildschirme überträgt
     - example: Heute Abend zeigt das Fernsehen ein wichtiges Fußballspiel live.
     - collocations: im Fernsehen kommen; im Fernsehen zeigen; im Fernsehen übertragen; im Fernsehen auftreten; Fernsehen schauen
  2. [B1] Einrichtung oder Unternehmen, das Programme für Bildschirme produziert und sendet
     - example: Mein Onkel arbeitet seit vielen Jahren als Kameramann beim Fernsehen.
     - collocations: beim Fernsehen arbeiten; öffentlich-rechtliches Fernsehen; privates Fernsehen
- relations: derived_from fernsehen (VERB)

### 3. Industrie

**Lexeme 1: Industrie**, NOUN
- grammar: gender Fem; plural_class -n; countability Both
- said: /ɪn.dʊs.ˈtʁiː/ (de-DE)
- forms: Industrie [Case=Gen, Number=Sing] /ɪn.dʊs.ˈtʁiː/; Industrien [Case=Nom, Number=Plur] /ɪn.dʊs.ˈtʁiː.ən/
  1. [B1] Bereich der Wirtschaft, in dem Waren mit Maschinen in Fabriken hergestellt werden
     - example: In dieser Region arbeiten viele Menschen in der Industrie.
     - collocations: in der Industrie arbeiten; die heimische Industrie; die Industrie fördern; Industrie ansiedeln
     - grammar: countability Mass
  2. [B2] alle Betriebe, die eine bestimmte Art von Waren in großen Mengen herstellen
     - example: Die chemische Industrie braucht sehr viel Energie und Wasser.
     - collocations: die chemische Industrie; die pharmazeutische Industrie; die verarbeitende Industrie
     - grammar: countability Count

### 4. Nachricht

**Lexeme 1: Nachricht**, NOUN
- grammar: gender Fem; plural_class -en; countability Count
- said: /ˈnaːx.ʁɪçt/ (de-DE)
- forms: Nachricht [Case=Gen, Number=Sing] /ˈnaːx.ʁɪçt/; Nachrichten [Case=Nom, Number=Plur] /ˈnaːx.ʁɪç.tən/
  1. [A1] Mitteilung, die jemandem Informationen über eine Person oder Sache gibt
     - example: Ich habe dir gestern eine Nachricht über den Termin geschickt.
     - frames: Nachricht von + Dat; Nachricht über + Akk
     - collocations: eine Nachricht schicken
  2. [A2] Sendung mit Berichten über die wichtigsten aktuellen Ereignisse
     - example: Um acht Uhr sehen wir fast immer die Nachrichten.
     - grammar: number Plur

### 5. Bundesregierung

**Lexeme 1: Bundesregierung**, NOUN
- grammar: gender Fem; plural_class -en; countability Count
- said: /ˈbʊn.dəs.ʁe.ˌɡiː.ʁʊŋ/ (de-DE)
- parts: Bundes (Bund) + regierung (Regierung)
- forms: Bundesregierung [Case=Gen, Number=Sing] /ˈbʊn.dəs.ʁe.ˌɡiː.ʁʊŋ/; Bundesregierungen [Case=Nom, Number=Plur] /ˈbʊn.dəs.ʁe.ˌɡiː.ʁʊ.ŋən/
  1. [B1] oberste politische Leitung eines Staates, der aus mehreren Ländern besteht
     - example: Die Bundesregierung will im nächsten Jahr mehr Geld für Schulen ausgeben.
     - collocations: die Bundesregierung bilden; Sprecher der Bundesregierung; Mitglied der Bundesregierung; Kritik an der Bundesregierung

### 6. Ehemalige

**Lexeme 1: Ehemalige**, NOUN
- grammar: gender Fem; declension adjectival; countability Count; note declined like an adjective: die Ehemalige, eine Ehemalige
- said: /ˈeː.ə.maː.lɪ.ɡə/ (de-DE)
- forms: Ehemaligen [Case=Gen, Number=Sing] /ˈeː.ə.maː.lɪ.ɡən/ (after an article: der Ehemaligen); Ehemaligen [Case=Nom, Number=Plur] /ˈeː.ə.maː.lɪ.ɡən/ (with the article: die Ehemaligen; without it: Ehemalige)
  1. [B2] Frau, die früher zu einer Schule, Firma oder Gruppe gehört hat
     - example: Als Ehemalige wird sie jedes Jahr zum Schulfest eingeladen.
     - collocations: Treffen der Ehemaligen
  2. [B2] frühere Ehefrau oder Partnerin einer Person
     - example: Auf der Party hat er zufällig seine Ehemalige wiedergesehen.
     - collocations: seine Ehemalige
     - labels: formality informal
- relations: feminine_of Ehemaliger (NOUN); derived_from ehemalig (ADJ)

### 7. müssen

**Lexeme 1: müssen**, VERB
- grammar: irregular True; auxiliary have; note modal verb; after another infinitive the participle is müssen, not gemusst (hat gehen müssen)
- said: /ˈmʏ.sən/ (de-DE)
- forms: muss [Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈmʊs/; musste [Tense=Past, Person=1,3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈmʊs.tə/; gemusst [Tense=Past, VerbForm=Part] /ɡə.ˈmʊst/ (only without another infinitive); müssen [Tense=Past, VerbForm=Part] /ˈmʏ.sən/ (only after another infinitive (hat gehen müssen)); müsste [Tense=Past, Person=1,3, Number=Sing, VerbForm=Fin, Mood=Sub] /ˈmʏs.tə/
  1. [A1] durch eine Pflicht, eine Regel oder die Umstände gezwungen sein, etwas zu tun
     - example: Morgen muss ich schon um sechs Uhr aufstehen.
     - frames: etw. tun müssen
     - collocations: nicht müssen; unbedingt müssen; leider müssen; eigentlich müssen; arbeiten müssen
  2. [A2] aus einem bestimmten Grund an einen anderen Ort gehen oder fahren sollen
     - example: Wir müssen jetzt leider schnell zum Bahnhof.
     - frames: müssen zu + Dat; müssen in + Akk; müssen nach + Dat
     - collocations: nach Hause müssen; zum Arzt müssen; in die Schule müssen
     - grammar: note used without an infinitive, with a direction; participle gemusst
  3. [B1] dringend zur Toilette gehen wollen
     - example: Halt bitte kurz an, ich muss mal ganz dringend.
     - collocations: mal müssen
     - labels: formality informal, attitude euphemistic
     - grammar: note used without an object, usually with mal
  4. [B1] verwendet, um auszudrücken, dass man etwas für sehr wahrscheinlich hält
     - example: Im Haus brennt Licht, also muss jemand da sein.
     - frames: etw. getan haben müssen
     - collocations: muss wohl; müsste eigentlich
     - grammar: note often in the subjunctive müsste for a weaker guess
- relations: confusable dürfen (VERB): nicht müssen means 'not have to'; English 'must not' is nicht dürfen

### 8. euer

**Lexeme 1: euer**, DET
- grammar: note before an ending the e of -er is usually dropped: eure, euren, eurem, eures
- said: /ˈɔʏ.ɐ/ (de-DE)
- forms: eure [Case=Nom,Acc, Gender=Fem, Number=Sing] /ˈɔʏ.ʁə/; eure [Case=Nom,Acc, Number=Plur] /ˈɔʏ.ʁə/
  1. [A1] drückt aus, dass etwas zu mehreren vertraut angesprochenen Personen gehört
     - example: Vergesst bitte eure Jacken nicht, draußen ist es sehr kalt.
     - labels: politeness familiar
  2. [C1] in festen Formeln zur ehrenden Anrede einer hochgestellten Person gebraucht
     - example: Euer Ehren, mein Mandant hat dazu nichts mehr zu sagen.
     - collocations: Euer Ehren; Eure Majestät; Euer Hochwürden
     - labels: formality formal, style elevated, politeness polite
     - grammar: note written with a capital: Euer, Eure
**Lexeme 2: euer**, PRON
- grammar: note stands alone without a noun; the e of -er is usually dropped: eurer, eure, eures
- said: /ˈɔʏ.ɐ/ (de-DE)
- forms: eurer [Case=Nom, Gender=Masc, Number=Sing] /ˈɔʏ.ʁɐ/; eures [Case=Nom,Acc, Gender=Neut, Number=Sing] /ˈɔʏ.ʁəs/
  1. [A2] steht für etwas, das mehreren vertraut angesprochenen Personen gehört
     - example: Unser Garten ist ziemlich klein, eurer ist viel größer.
     - labels: politeness familiar

### 9. Gang

**Lexeme 1: Gang**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈɡaŋ/ (de-DE)
- forms: Ganges [Case=Gen, Number=Sing] /ˈɡa.ŋəs/; Gangs [Case=Gen, Number=Sing] /ˈɡaŋs/; Gänge [Case=Nom, Number=Plur] /ˈɡɛ.ŋə/
  1. [A2] langer, schmaler Raum oder Weg in einem Gebäude, durch den man läuft
     - example: Die Toiletten findest du am Ende des Gangs auf der linken Seite.
     - collocations: ein langer Gang; ein dunkler Gang; einen Gang entlanglaufen; ein Platz am Gang
  2. [B2] Ablauf oder Verlauf, in dem etwas geschieht, sich entwickelt oder funktioniert
     - example: Die Vorbereitungen für das Fest sind schon in vollem Gange.
     - collocations: in Gang bringen; in Gang kommen; in Gang setzen; in Gang halten; im Gange sein; in vollem Gange sein; seinen Gang gehen; der Gang der Dinge
     - grammar: number Sing
  3. [B1] eine der Stufen, in die man ein Getriebe schalten kann
     - example: Am Berg solltest du lieber in einen kleineren Gang schalten.
     - collocations: einen Gang einlegen; in den ersten Gang schalten; den Gang wechseln; einen Gang zurückschalten; einen Gang höher schalten
  4. [B1] die Art, wie ein Mensch oder ein Tier läuft
     - example: Schon an seinem schweren Gang merkt man, wie müde er ist.
     - collocations: ein aufrechter Gang; ein unsicherer Gang; ein schleppender Gang
     - grammar: number Sing
  5. [B1] einzelnes Gericht einer Mahlzeit, das nacheinander mit anderen serviert wird
     - example: Als zweiten Gang gibt es heute Fisch mit Kartoffeln.
     - collocations: ein Menü mit drei Gängen; der erste Gang; der nächste Gang
  6. [B2] Weg zu einem Ort, um dort etwas zu erledigen
     - example: Der Gang zum Amt fällt mir jedes Mal schwer.
     - frames: Gang zu + Dat
     - collocations: ein schwerer Gang; einen Gang erledigen; einen Gang machen
- relations: derived_from gehen (VERB)
**Lexeme 2: Gang**, NOUN
- grammar: gender Fem; plural_class -s; countability Count
- said: /ˈɡɛŋ/ (de-DE)
- forms: Gang [Case=Gen, Number=Sing] /ˈɡɛŋ/; Gangs [Case=Nom, Number=Plur] /ˈɡɛŋs/
  1. [B2] Gruppe von meist jungen Menschen, die gemeinsam Straftaten begehen
     - example: In diesem Viertel kämpfen mehrere Gangs um die Kontrolle.
     - collocations: eine kriminelle Gang; einer Gang angehören; Mitglied einer Gang

### 10. fällen

**Lexeme 1: fällen**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False
- said: /ˈfɛ.lən/ (de-DE)
- forms: fällte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈfɛl.tə/; gefällt [Tense=Past, VerbForm=Part] /ɡə.ˈfɛlt/
  1. [B2] einen Baum am Stamm absägen oder mit einer Axt umschlagen
     - example: Im Winter werden im Wald viele alte Bäume gefällt.
     - collocations: einen Baum fällen
  2. [B2] nach einer Prüfung oder Überlegung eine verbindliche Entscheidung treffen
     - example: Bis Freitag müssen wir endlich eine Entscheidung fällen.
     - collocations: eine Entscheidung fällen; ein Urteil fällen
- relations: derived_from fallen (VERB); confusable gefallen (VERB): the participle gefällt looks like gefällt 'pleases', present tense of gefallen

### 11. nötig

**Lexeme 1: nötig**, ADJ
- grammar: comparable True
- said: /ˈnøː.tɪç/ (de-DE)
  1. [A2] unbedingt gebraucht, damit etwas gelingt oder funktioniert
     - example: Für diese Reise ist ein gültiger Reisepass unbedingt nötig.
     - frames: etw. ist nötig für + Akk; es ist nötig, etw. zu tun; etw. nötig haben
     - opposites: unnötig
- relations: derived_from Not (NOUN)

### 12. spitze

**Lexeme 1: spitze**, ADJ
- grammar: invariable True; comparable False; position Pred, mainly
- said: /ˈʃpɪt.sə/ (de-DE)
  1. [A2] ganz hervorragend und begeisternd, besser als man erwartet
     - example: Das Konzert gestern Abend war wirklich spitze!
     - collocations: einfach spitze; echt spitze
     - labels: formality informal
     - grammar: note also written with a capital: Das ist Spitze!
- relations: derived_from Spitze (NOUN)
**Lexeme 2: Spitze**, NOUN
- grammar: gender Fem; plural_class -n
- said: /ˈʃpɪt.sə/ (de-DE)
- forms: Spitze [Case=Gen, Number=Sing] /ˈʃpɪt.sə/; Spitzen [Case=Nom, Number=Plur] /ˈʃpɪt.sən/
  1. [B1] dünnes, scharfes Ende eines Gegenstands, das in einem Punkt ausläuft
     - example: Pass auf, die Spitze von diesem Messer ist sehr scharf.
     - collocations: eine scharfe Spitze; die Spitze abbrechen
     - grammar: countability Count
  2. [B1] oberster, schmal zulaufender Teil eines hohen Gegenstands oder Berges
     - example: Von der Spitze des Turms sieht man die ganze Stadt.
     - collocations: die Spitze des Eisbergs
     - grammar: countability Count
  3. [B1] vorderste oder führende Position in einer Gruppe, Reihe oder Wertung
     - example: Nach dem dritten Sieg liegt unsere Mannschaft wieder an der Spitze.
     - collocations: an der Spitze liegen; an der Spitze stehen; sich an die Spitze setzen; die Spitze übernehmen
     - grammar: countability Count
  4. [B2] Personen, die eine Organisation, Partei oder Firma leiten
     - example: Die Spitze der Partei trifft sich morgen zu einer Krisensitzung.
     - collocations: die politische Spitze
     - grammar: countability Count
  5. [B2] feines, durchbrochenes Gewebe mit Mustern, oft als Schmuck an Kleidung
     - example: Sie trägt heute ein schwarzes Kleid mit Spitze am Kragen.
     - collocations: weiße Spitze; Spitze klöppeln
     - grammar: countability Mass, mainly
  6. [C1] kleine, versteckt böse Bemerkung, die jemanden treffen soll
     - example: In seiner Rede konnte er sich eine Spitze gegen den Chef nicht verkneifen.
     - frames: Spitze gegen + Akk
     - grammar: countability Count
- relations: derived_from spitz (ADJ)

### 13. Twitter

**Lexeme 1: Twitter**, PROPN
- grammar: number Sing; note usually used without an article; since 2023 officially called X
- said: /ˈtvɪ.tɐ/ (de-DE)
  1. [B1] soziales Netzwerk im Internet für kurze Beiträge, die alle lesen können
     - example: Viele Journalisten verbreiten ihre Artikel zuerst über Twitter.
     - collocations: auf Twitter posten; bei Twitter angemeldet sein; auf Twitter folgen

### 14. plus

**Lexeme 1: plus**, CONJ
- grammar: invariable True
- said: /ˈplʊs/ (de-DE)
  1. [A1] drückt aus, dass eine Zahl zu einer anderen addiert wird
     - example: Fünf plus drei ist acht, das weiß doch jedes Kind.
     - labels: domain mathematics
     - opposites: minus
**Lexeme 2: plus**, ADP
- grammar: invariable True; note the noun after it usually stands without article and is not inflected
- said: /ˈplʊs/ (de-DE)
  1. [B1] drückt aus, dass etwas zu einem Betrag noch hinzukommt
     - example: Das Zimmer kostet achtzig Euro pro Nacht plus Frühstück.
     - frames: plus + Gen; plus + Dat
     - opposites: minus
**Lexeme 3: plus**, ADV
- said: /ˈplʊs/ (de-DE)
  1. [A2] über dem Nullpunkt, größer als null
     - example: Heute Nacht sinkt die Temperatur nur auf plus zwei Grad.
     - opposites: minus
  2. [B1] etwas besser als die genannte Schulnote
     - example: Für meinen Aufsatz habe ich diesmal eine Zwei plus bekommen.
     - labels: domain education
     - grammar: note placed after the grade
     - opposites: minus
**Lexeme 4: Plus**, NOUN
- grammar: gender Neut; plural_class -; countability Count
- said: /ˈplʊs/ (de-DE)
- forms: Plus [Case=Gen, Number=Sing] /ˈplʊs/; Plus [Case=Nom, Number=Plur] /ˈplʊs/
  1. [B1] Betrag, um den Einnahmen oder Werte höher sind als vorher oder als die Ausgaben
     - example: Im letzten Jahr machte die Firma ein Plus von zehn Prozent.
     - frames: ein Plus von + Dat
     - collocations: im Plus sein; ein Plus machen; ins Plus kommen; ein deutliches Plus
     - opposites: Minus
  2. [B2] Eigenschaft oder Umstand, der jemandem nützt und ihn besser macht
     - example: Deine Erfahrung im Ausland ist bei dieser Bewerbung ein großes Plus.
     - collocations: ein großes Plus; ein klares Plus
     - opposites: Nachteil

### 15. runter

**Lexeme 1: runter**, ADV
- said: /ˈʁʊn.tɐ/ (de-DE)
  1. [B1] von oben nach unten, an einen tiefer liegenden Ort
     - example: Komm sofort vom Dach runter, das ist viel zu gefährlich!
     - frames: von + Dat runter
     - collocations: die Treppe runter; den Berg runter; runter damit
     - labels: formality informal
     - opposites: rauf
- relations: derived_from herunter (ADV)

### 16. Computer

**Lexeme 1: Computer**, NOUN
- grammar: gender Masc; plural_class -; countability Count
- said: /kɔm.ˈpjuː.tɐ/ (de-DE)
- forms: Computers [Case=Gen, Number=Sing] /kɔm.ˈpjuː.tɐs/; Computer [Case=Nom, Number=Plur] /kɔm.ˈpjuː.tɐ/
  1. [A1] elektronisches Gerät, mit dem man Daten speichert, bearbeitet und Programme nutzt
     - example: Ohne meinen Computer kann ich von zu Hause nicht arbeiten.
     - collocations: am Computer arbeiten; am Computer sitzen; den Computer einschalten; den Computer ausschalten; den Computer hochfahren; den Computer herunterfahren; auf dem Computer speichern

### 17. meistens

**Lexeme 1: meistens**, ADV
- said: /ˈmaɪs.təns/ (de-DE)
  1. [A1] in den allermeisten Fällen, fast immer
     - example: Morgens trinke ich meistens nur einen Kaffee und esse nichts.
- relations: derived_from meist (ADV)

### 18. jung

**Lexeme 1: jung**, ADJ
- grammar: comparable True
- said: /ˈjʊŋ/ (de-DE)
- forms: jünger [Degree=Cmp] /ˈjʏ.ŋɐ/; am jüngsten [Degree=Sup] /am ˈjʏŋs.tən/
  1. [A1] noch nicht lange auf der Welt, in einem frühen Abschnitt des Lebens
     - example: Meine Eltern haben sehr jung geheiratet und früh Kinder bekommen.
     - collocations: junge Leute; ein junger Mann; eine junge Frau; jung sterben; jung heiraten; von jung auf
     - opposites: alt
  2. [B1] erst seit kurzer Zeit vorhanden oder bestehend
     - example: Das junge Unternehmen hat schon über fünfzig Mitarbeiter.
     - collocations: ein junges Unternehmen; eine junge Demokratie; junger Wein; junges Gemüse
     - grammar: position Attr, mainly
     - opposites: alt
  3. [B1] in Aussehen, Denken oder Verhalten frisch und nicht alt wirkend
     - example: Durch den vielen Sport fühlt sich meine Oma noch richtig jung.
     - collocations: sich jung fühlen; jung bleiben; jung aussehen; jung geblieben
  4. [C1] zeitlich nahe an der Gegenwart, vor kurzer Zeit geschehen
     - example: Die jüngsten Ereignisse haben viele Menschen im Land überrascht.
     - collocations: in jüngster Zeit; die jüngsten Ereignisse; die jüngste Vergangenheit
     - grammar: position Attr; note usually in the superlative: die jüngsten Ereignisse, in jüngster Zeit

### 19. empfehlen

**Lexeme 1: empfehlen**, VERB
- grammar: conjugation strong; irregular True; auxiliary have
- said: /ɛm.ˈpfeː.lən/ (de-DE)
- forms: empfiehlt [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ɛm.ˈpfiːlt/; empfahl [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ɛm.ˈpfaːl/; empfohlen [Tense=Past, VerbForm=Part] /ɛm.ˈpfoː.lən/
  1. [A2] jemandem sagen, dass eine Person oder Sache gut und passend für ihn ist
     - example: Kannst du mir ein gutes Restaurant in der Nähe empfehlen?
     - frames: jmdm. etw. empfehlen; jmdm. jmdn. empfehlen; jmdm. empfehlen, etw. zu tun; jmdn. als etw. empfehlen
     - collocations: wärmstens empfehlen; dringend empfehlen; sehr zu empfehlen sein; nur empfehlen können; ein Buch empfehlen; einen Arzt empfehlen
     - grammar: valency Tr
  2. [B2] ratsam und von Vorteil sein, sodass man es tun sollte
     - example: Bei diesem Wetter empfiehlt es sich, einen Schirm mitzunehmen.
     - frames: es empfiehlt sich, etw. zu tun
     - grammar: valency Impers; reflexive True; note usually es empfiehlt sich, with a zu-infinitive or a dass-clause
  3. [C2] sich höflich verabschieden und dann weggehen
     - example: Nach dem Essen empfahl sich der Gast mit einer kleinen Verbeugung.
     - labels: time dated, formality formal
     - grammar: valency Refl; reflexive True

### 20. Material

**Lexeme 1: Material**, NOUN
- grammar: gender Neut; countability Mass, mainly; note plural Materialien, mostly for kinds of material or sets of work materials
- said: /ma.te.ˈʁjaːl/ (de-DE)
- forms: Materials [Case=Gen, Number=Sing] /ma.te.ˈʁjaːls/; Materialien [Case=Nom, Number=Plur] /ma.te.ˈʁjaː.ljən/
  1. [B1] Stoff, aus dem etwas hergestellt oder gebaut wird
     - example: Diese Jacke ist aus einem leichten, wasserdichten Material.
     - frames: Material für + Akk
     - collocations: aus welchem Material; hochwertiges Material; natürliches Material; synthetisches Material; Material verarbeiten; Material sparen
  2. [B1] Dinge, Informationen oder Unterlagen, die man für eine bestimmte Arbeit braucht
     - example: Für mein Referat muss ich noch viel Material sammeln.
     - frames: Material über + Akk; Material zu + Dat
     - collocations: Material sammeln; Material auswerten; Material zusammenstellen; umfangreiches Material; belastendes Material

### 21. Baum

**Lexeme 1: Baum**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈbaʊm/ (de-DE)
- forms: Baumes [Case=Gen, Number=Sing] /ˈbaʊ.məs/; Baums [Case=Gen, Number=Sing] /ˈbaʊms/; Bäume [Case=Nom, Number=Plur] /ˈbɔʏ.mə/
  1. [A1] große Pflanze mit einem Stamm aus Holz, mit Ästen und Blättern oder Nadeln
     - example: Hinter unserem Haus steht ein alter Baum mit vielen Äpfeln.
     - collocations: einen Baum fällen; einen Baum pflanzen; auf einen Baum klettern; ein hoher Baum; im Schatten eines Baumes; der Baum blüht
  2. [B1] geschmückte Tanne oder Fichte, die zu Weihnachten in der Wohnung steht
     - example: Morgen stellen wir zusammen den Baum im Wohnzimmer auf.
     - collocations: den Baum schmücken; den Baum aufstellen; unter dem Baum liegen
     - labels: formality informal
     - grammar: number Sing, mainly

### 22. Tätigkeit

**Lexeme 1: Tätigkeit**, NOUN
- grammar: gender Fem; plural_class -en
- said: /ˈtɛː.tɪç.kaɪt/ (de-DE), /ˈteː.tɪç.kaɪt/ (de-DE)
- forms: Tätigkeit [Case=Gen, Number=Sing] /ˈtɛː.tɪç.kaɪt/; Tätigkeiten [Case=Nom, Number=Plur] /ˈtɛː.tɪç.kaɪ.tən/
  1. [B1] etwas, das jemand tut oder womit er sich beschäftigt
     - example: Lesen gehört am Wochenende zu meinen liebsten Tätigkeiten.
     - collocations: körperliche Tätigkeit; eine sinnvolle Tätigkeit; eine Tätigkeit verrichten
     - grammar: countability Count
  2. [B1] berufliche Arbeit, mit der jemand sein Geld verdient
     - example: Nach dem Studium sucht sie eine Tätigkeit im Ausland.
     - frames: Tätigkeit als + Nom
     - collocations: eine Tätigkeit ausüben; eine Tätigkeit aufnehmen; berufliche Tätigkeit; ehrenamtliche Tätigkeit; selbstständige Tätigkeit
     - grammar: countability Count
  3. [C1] Zustand, in dem etwas arbeitet, funktioniert oder in Betrieb ist
     - example: Nach langer Ruhe ist der alte Vulkan wieder in Tätigkeit.
     - collocations: in Tätigkeit sein; in Tätigkeit treten; außer Tätigkeit setzen
     - grammar: countability Mass; number Sing
- relations: abstract_noun_of tätig (ADJ)

### 23. Farbe

**Lexeme 1: Farbe**, NOUN
- grammar: gender Fem; plural_class -n
- said: /ˈfaʁ.bə/ (de-DE)
- forms: Farbe [Case=Gen, Number=Sing] /ˈfaʁ.bə/; Farben [Case=Nom, Number=Plur] /ˈfaʁ.bən/
  1. [A1] Eindruck wie Rot, Blau oder Grün, den Licht auf einer Oberfläche beim Sehen erzeugt
     - example: Welche Farbe soll das neue Sofa im Wohnzimmer haben?
     - collocations: eine helle Farbe; eine dunkle Farbe; eine kräftige Farbe; die Farbe wechseln; in Farbe; die Farbe verlieren
     - grammar: countability Count
  2. [A2] Stoff, mit dem man Wände, Bilder, Stoffe oder Haare anstreicht oder bemalt
     - example: Vorsicht, die Farbe an der Tür ist noch nicht trocken!
     - collocations: Farbe auftragen; die Farbe blättert ab; die Farbe ist trocken; ein Eimer Farbe
     - grammar: countability Mass, mainly
  3. [B2] gesunder, frischer Ton der Haut im Gesicht
     - example: Nach dem Urlaub am Meer hast du richtig Farbe bekommen.
     - collocations: Farbe bekommen; eine gesunde Farbe
     - grammar: countability Mass; number Sing
  4. [B2] Gruppe von Spielkarten, die alle dasselbe Zeichen tragen
     - example: Wer keine Karte dieser Farbe hat, darf einen Trumpf spielen.
     - collocations: Farbe bedienen
     - grammar: countability Count
  5. [C1] Kennzeichen einer Mannschaft, eines Vereins oder eines Landes, das jeder erkennt
     - example: Seit zehn Jahren spielt sie für die Farben unseres Vereins.
     - labels: figurative True
     - grammar: countability Count; number Plur, mainly

### 24. Ecke

**Lexeme 1: Ecke**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /ˈɛ.kə/ (de-DE)
- forms: Ecke [Case=Gen, Number=Sing] /ˈɛ.kə/; Ecken [Case=Nom, Number=Plur] /ˈɛ.kən/
  1. [A2] Stelle, an der zwei Seiten, Kanten oder Wände zusammentreffen
     - example: Der alte Schrank steht schon seit Jahren dort in der Ecke.
     - collocations: in der Ecke stehen; in die Ecke drängen; sich an der Ecke stoßen; an allen Ecken und Enden
  2. [A2] Stelle, an der zwei Straßen zusammentreffen
     - example: Die Bäckerei findest du gleich um die Ecke neben der Apotheke.
     - collocations: um die Ecke biegen; gleich um die Ecke; an der Ecke warten
  3. [B1] Teil eines Ortes, einer Stadt oder eines Landes
     - example: Wir wohnen in einer sehr ruhigen Ecke der Stadt.
     - collocations: eine ruhige Ecke; aus allen Ecken; in dieser Ecke
     - labels: formality informal
  4. [B2] kleines, meist spitz geformtes Stück, das von etwas abgeteilt ist
     - example: Zum Frühstück esse ich gern eine Ecke Käse mit Brot.
     - collocations: eine Ecke Käse
     - labels: formality informal
  5. [B2] Stoß vom Rand des Spielfelds neben dem Tor nach einem Ball über die Torlinie
     - example: Nach der Ecke köpfte der Verteidiger den Ball ins eigene Tor.
     - collocations: eine Ecke treten; eine Ecke ausführen; eine Ecke geben
  6. [C1] politische oder gesellschaftliche Richtung, zu der eine Gruppe von Menschen gehört
     - example: Solche Parolen kommen meistens aus der rechten Ecke.
     - collocations: aus der rechten Ecke; aus der linken Ecke; in die rechte Ecke stellen
     - labels: figurative True, attitude disapproving

### 25. Mama

**Lexeme 1: Mama**, NOUN
- grammar: gender Fem; plural_class -s; countability Count; note without an article the genitive is Mamas (Mamas Auto)
- said: /ˈma.ma/ (de-DE), /ma.ˈmaː/ (de-DE, older)
- forms: Mama [Case=Gen, Number=Sing] /ˈma.ma/; Mamas [Case=Nom, Number=Plur] /ˈma.mas/
  1. [A1] weiblicher Elternteil eines Kindes, als vertraute Anrede oder Bezeichnung
     - example: Meine Mama holt mich heute nach der Schule ab.
     - collocations: frischgebackene Mama; werdende Mama; alleinerziehende Mama
     - labels: formality informal
     - opposites: Papa

### 26. ungefähr

**Lexeme 1: ungefähr**, ADV
- said: /ˈʊn.ɡə.fɛːɐ/ (de-DE), /ʊn.ɡə.ˈfɛːɐ/ (de-DE, less_common), /ˈʊn.ɡə.feːɐ/ (de-DE, less_common)
  1. [A2] nicht ganz genau, aber so, dass es etwa stimmt
     - example: Die Fahrt in die Stadt dauert ungefähr zwanzig Minuten.
     - collocations: so ungefähr; ungefähr wissen; ungefähr gleich
     - opposites: genau
**Lexeme 2: ungefähr**, ADJ
- grammar: comparable False; position Attr, mainly
- said: /ˈʊn.ɡə.fɛːɐ/ (de-DE), /ʊn.ɡə.ˈfɛːɐ/ (de-DE, less_common), /ˈʊn.ɡə.feːɐ/ (de-DE, less_common)
  1. [B1] nicht ganz genau, nur im Großen und Ganzen zutreffend
     - example: Er hat mir eine ungefähre Beschreibung des Weges gegeben.
     - collocations: eine ungefähre Vorstellung; ungefähre Angaben; eine ungefähre Zahl; eine ungefähre Ahnung
     - opposites: genau

### 27. vorne

**Lexeme 1: vorne**, ADV
- said: /ˈfɔʁ.nə/ (de-DE, vorne), /ˈfɔʁn/ (de-DE, vorn)
- spellings: vorne; vorn
  1. [A1] an der Seite oder Stelle, die zum Gesicht oder in Fahrtrichtung zeigt
     - example: Im Bus sitze ich am liebsten ganz vorne beim Fahrer.
     - collocations: ganz vorne; nach vorne; von vorne; von vorne anfangen; vorne sitzen; weiter vorne
     - opposites: hinten
  2. [B1] in einem Wettbewerb an erster Stelle oder unter den Besten
     - example: Nach der ersten Halbzeit liegt unsere Mannschaft klar vorne.
     - collocations: vorne liegen; die Nase vorne haben; vorne mit dabei sein
     - opposites: hinten

### 28. Brücke

**Lexeme 1: Brücke**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /ˈbʁʏ.kə/ (de-DE)
- forms: Brücke [Case=Gen, Number=Sing] /ˈbʁʏ.kə/; Brücken [Case=Nom, Number=Plur] /ˈbʁʏ.kən/
  1. [A2] Bauwerk, auf dem Menschen oder Fahrzeuge ein Hindernis überqueren können
     - example: Hinter dem Dorf führt eine alte Brücke aus Stein über den Fluss.
     - frames: Brücke über + Akk
     - collocations: eine Brücke bauen; über eine Brücke fahren; über die Brücke gehen; die Brücke überqueren; eine Brücke sprengen; die Brücke führt über den Fluss
  2. [B2] etwas, das getrennte Menschen, Gruppen oder Dinge miteinander verbindet
     - example: Gemeinsame Musik kann eine Brücke zwischen fremden Kulturen sein.
     - frames: Brücke zwischen + Dat; Brücke zu + Dat
     - collocations: eine Brücke schlagen; Brücken bauen; alle Brücken hinter sich abbrechen
     - labels: figurative True
  3. [B2] fest eingesetzter Zahnersatz, der eine Lücke zwischen den Zähnen schließt
     - example: Nächste Woche bekomme ich beim Zahnarzt endlich meine neue Brücke.
     - collocations: eine Brücke einsetzen
  4. [C1] erhöhter Raum auf einem Schiff, von dem aus es gesteuert wird
     - example: Während des Sturms blieb der Kapitän die ganze Nacht auf der Brücke.
     - collocations: auf der Brücke
     - labels: domain nautical
  5. [C1] freier Tag zwischen einem Feiertag und dem Wochenende
     - example: Viele Angestellte nehmen am Freitag nach Auffahrt eine Brücke.
     - regions: de-CH only

### 29. Armee

**Lexeme 1: Armee**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /aʁ.ˈmeː/ (de-DE)
- forms: Armee [Case=Gen, Number=Sing] /aʁ.ˈmeː/; Armeen [Case=Nom, Number=Plur] /aʁ.ˈmeː.ən/
  1. [B1] alle bewaffneten Truppen, mit denen ein Staat sich verteidigt oder Krieg führt
     - example: Nach der Schule will mein Bruder für zwei Jahre zur Armee gehen.
     - collocations: zur Armee gehen; in der Armee dienen; bei der Armee sein; in die Armee eintreten; eine Armee aufstellen; die Armee verlassen; eine stehende Armee
  2. [C1] großer militärischer Verband aus mehreren Divisionen oder Korps
     - example: Im harten Winter wurde die ganze Armee vor der Stadt eingeschlossen.
     - collocations: eine Armee führen; eine Armee einschließen
     - labels: domain military
  3. [C1] sehr große Zahl von Menschen oder Tieren, die gemeinsam dasselbe tun
     - example: Nach dem Hochwasser räumt eine Armee von Helfern die Straßen auf.
     - frames: eine Armee von + Dat
     - labels: figurative True

### 30. Trainer

**Lexeme 1: Trainer**, NOUN
- grammar: gender Masc; plural_class -; countability Count
- said: /ˈtʁɛː.nɐ/ (de-DE), /ˈtʁeː.nɐ/ (de-DE)
- forms: Trainers [Case=Gen, Number=Sing] /ˈtʁɛː.nɐs/; Trainer [Case=Nom, Number=Plur] /ˈtʁɛː.nɐ/
  1. [A2] Person, die Menschen oder Tiere durch regelmäßige Übungen ausbildet
     - example: Unser Trainer zeigt uns heute Übungen für den Rücken.
  2. [B2] bequemer Anzug aus Jacke und Hose, den man beim Sport trägt
     - example: Am Sonntag sitze ich gern im Trainer auf dem Sofa.
     - regions: de-AT only, de-CH only

### 31. Behandlung

**Lexeme 1: Behandlung**, NOUN
- grammar: gender Fem; plural_class -en; countability Count
- said: /bə.ˈhand.lʊŋ/ (de-DE)
- forms: Behandlung [Case=Gen, Number=Sing] /bə.ˈhand.lʊŋ/; Behandlungen [Case=Nom, Number=Plur] /bə.ˈhand.lʊ.ŋən/
  1. [B1] Maßnahmen, mit denen ein Arzt oder Therapeut eine Krankheit oder Verletzung heilen will
     - example: Nach drei Wochen Behandlung kann sie endlich wieder ohne Schmerzen laufen.
     - frames: Behandlung von + Dat; Behandlung bei + Dat
     - collocations: in Behandlung sein; sich in Behandlung begeben; ärztliche Behandlung; medizinische Behandlung; ambulante Behandlung; stationäre Behandlung; eine Behandlung abbrechen
  2. [B2] die Art, wie man sich gegenüber jemandem verhält oder mit etwas umgeht
     - example: Die Gefangenen beschwerten sich über die schlechte Behandlung durch die Wärter.
     - frames: Behandlung durch + Akk
     - collocations: unmenschliche Behandlung; bevorzugte Behandlung; faire Behandlung; gleiche Behandlung
  3. [B2] ausführliches Besprechen oder Darstellen eines Themas, etwa in einem Text oder Unterricht
     - example: Für die Behandlung dieses Themas brauchen wir im Unterricht fast zwei Wochen.
  4. [B2] Bearbeitung eines Materials oder einer Oberfläche mit einem bestimmten Mittel
     - example: Nach der Behandlung mit Öl sieht der alte Tisch wieder schön aus.
     - frames: Behandlung mit + Dat
- relations: verbal_noun_of behandeln (VERB)

### 32. bloß

**Lexeme 1: bloß**, ADJ
- said: /ˈbloːs/ (de-DE)
- spellings: bloß (de-DE, de-AT); bloss (de-CH)
  1. [B2] nicht von Kleidung, einer Hülle oder einem Schutz bedeckt
     - example: Im Sommer läuft sie gern mit bloßen Füßen durch den Garten.
     - collocations: mit bloßem Auge; mit bloßen Händen; mit bloßen Füßen; mit bloßem Oberkörper
     - grammar: position Attr, mainly
  2. [B2] nichts anderes und nichts weiter als das Genannte
     - example: Schon der bloße Gedanke an die Prüfung macht mich nervös.
     - collocations: der bloße Gedanke; bloßer Zufall; eine bloße Vermutung; der bloße Anblick
     - grammar: position Attr; comparable False
**Lexeme 2: bloß**, ADV
- said: /ˈbloːs/ (de-DE)
- spellings: bloß (de-DE, de-AT); bloss (de-CH)
  1. [B1] gibt an, dass nichts anderes oder nicht mehr dazukommt
     - example: Ich wollte dich bloß kurz fragen, wie es dir geht.
     - frames: nicht bloß etw., sondern auch etw.
**Lexeme 3: bloß**, PART
- said: /ˈbloːs/ (de-DE)
- spellings: bloß (de-DE, de-AT); bloss (de-CH)
  1. [B1] verwendet, um in einer Frage Ratlosigkeit, Sorge oder Ärger auszudrücken
     - example: Wo habe ich bloß meinen Schlüssel hingelegt?
     - grammar: note in questions with a question word
  2. [B1] verwendet, um eine Aufforderung oder Warnung dringender zu machen
     - example: Sei bloß vorsichtig mit dem Messer, es ist sehr scharf!
     - collocations: bloß nicht
     - grammar: note in commands, often together with nicht
  3. [B2] verwendet, um einen Wunsch besonders dringend auszudrücken
     - example: Wenn ich bloß mehr Zeit für meine Familie hätte!
     - grammar: note in wishes with the subjunctive, usually with wenn

### 33. davor

**Lexeme 1: davor**, ADV
- said: /da.ˈfoːɐ/ (de-DE), /ˈdaː.foːɐ/ (de-DE: stressed, pointing to something)
- parts: da (da) + vor (vor)
  1. [A2] zeitlich früher als das gerade genannte Ereignis
     - example: Morgen schreiben wir die Prüfung, aber davor will ich noch einmal üben.
     - collocations: kurz davor; lange davor; am Tag davor; in der Woche davor
     - opposites: danach
  2. [B1] auf der Seite des gerade Genannten, die einem Betrachter zugewandt ist
     - example: Das Haus ist ziemlich alt, davor steht ein großer Kastanienbaum.
     - collocations: direkt davor; davor stehen
     - opposites: dahinter
  3. [B1] verwendet, um eine genannte oder folgende Sache als Ergänzung eines Verbs oder Nomens aufzunehmen
     - example: Viele Kinder haben Angst davor, allein im Dunkeln zu schlafen.
     - frames: davor, etw. zu tun; davor, dass ...; jmdn. davor schützen
     - collocations: Angst davor haben; sich davor fürchten; davor warnen; sich davor drücken; davor bewahren

### 34. sparen

**Lexeme 1: sparen**, VERB
- grammar: conjugation weak; auxiliary have; valency Both; irregular False
- said: /ˈʃpaː.ʁən/ (de-DE)
- forms: sparte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈʃpaːɐ.tə/; gespart [Tense=Past, VerbForm=Part] /ɡə.ˈʃpaːɐt/
  1. [A2] Geld nicht ausgeben, sondern für später zur Seite legen
     - example: Seit dem Frühjahr spare ich jeden Monat für den Urlaub.
     - frames: sparen für + Akk; sparen auf + Akk; etw. sparen
     - collocations: Geld sparen; fürs Alter sparen; eisern sparen; auf ein Auto sparen
  2. [A2] weniger Geld, Material, Energie oder Zeit verbrauchen als sonst
     - example: Mit den neuen Fenstern sparen wir im Winter viel Heizkosten.
     - frames: etw. sparen; sparen an + Dat; sparen bei + Dat
     - collocations: Energie sparen; Strom sparen; Zeit sparen; Kosten sparen; Platz sparen; am falschen Ende sparen
     - opposites: verschwenden
  3. [B2] etwas Unnötiges oder Unangenehmes lieber nicht tun oder jemandem nicht zumuten
     - example: Deine klugen Kommentare kannst du dir in Zukunft wirklich sparen.
     - frames: sich etw. sparen; jmdm. etw. sparen
     - collocations: sich die Mühe sparen; sich den Weg sparen
     - labels: formality informal
     - grammar: valency Tr; note usually with a dative reflexive pronoun, often with können

### 35. Gast

**Lexeme 1: Gast**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈɡast/ (de-DE)
- forms: Gastes [Case=Gen, Number=Sing] /ˈɡas.təs/; Gasts [Case=Gen, Number=Sing] /ˈɡasts/; Gäste [Case=Nom, Number=Plur] /ˈɡɛs.tə/
  1. [A1] Person, die man zu sich eingeladen hat oder die zu Besuch kommt
     - example: Am Samstag erwarten wir zum Abendessen sechs Gäste.
     - frames: bei jmdm. zu Gast sein
     - collocations: Gäste einladen; Gäste empfangen; zu Gast sein; Gäste bewirten; ein gern gesehener Gast; ein ungebetener Gast; Gäste haben
     - opposites: Gastgeber
  2. [A2] Person, die in einem Hotel oder Lokal übernachtet oder isst und dafür bezahlt
     - example: In der Hochsaison ist das Hotel fast nur mit ausländischen Gästen belegt.
     - collocations: Gäste bedienen; einen Gast begrüßen
  3. [B2] Mannschaft, die nicht auf dem eigenen Platz, sondern beim Gegner spielt
     - example: Nach der Pause gingen die Gäste überraschend mit zwei Toren in Führung.
     - collocations: die Gäste gewinnen
     - labels: domain sport
     - grammar: number Plur, mainly

### 36. Leitung

**Lexeme 1: Leitung**, NOUN
- grammar: gender Fem; plural_class -en
- said: /ˈlaɪ.tʊŋ/ (de-DE)
- forms: Leitung [Case=Gen, Number=Sing] /ˈlaɪ.tʊŋ/; Leitungen [Case=Nom, Number=Plur] /ˈlaɪ.tʊ.ŋən/
  1. [B1] Verantwortung dafür, eine Gruppe, Firma oder Veranstaltung zu führen
     - example: Unter ihrer Leitung hat sich die Firma sehr gut entwickelt.
     - frames: unter jmds. Leitung; unter der Leitung von + Dat
     - collocations: die Leitung übernehmen; die Leitung haben; die Leitung abgeben; unter neuer Leitung
     - grammar: countability Mass
  2. [B1] Personen, die gemeinsam für die Führung einer Firma oder Einrichtung verantwortlich sind
     - example: Die Leitung der Schule hat alle Eltern zu einem Treffen eingeladen.
     - collocations: sich an die Leitung wenden
     - grammar: countability Count
  3. [B1] Rohr oder System von Rohren, durch das Wasser, Gas oder Öl fließt
     - example: Im Winter ist bei uns eine alte Leitung im Keller geplatzt.
     - collocations: eine Leitung verlegen; die Leitung ist undicht; die Leitung ist verstopft
     - grammar: countability Count
  4. [B1] Draht oder Kabel, durch das elektrischer Strom fließt
     - example: Fass die Leitung nicht an, sie steht vielleicht noch unter Strom!
     - collocations: eine Leitung verlegen; die Leitung steht unter Strom
     - grammar: countability Count
  5. [B1] Verbindung über ein Telefonnetz, über die man mit jemandem spricht
     - example: Bleiben Sie bitte in der Leitung, ich verbinde Sie gleich.
     - collocations: in der Leitung bleiben; die Leitung ist besetzt; die Leitung ist tot; in der Leitung sein
     - grammar: countability Count
- relations: verbal_noun_of leiten (VERB)

### 37. quellen

**Lexeme 1: quellen**, VERB
- grammar: conjugation strong; auxiliary be; valency Intr; irregular True
- said: /ˈkvɛ.lən/ (de-DE)
- forms: quillt [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈkvɪlt/; quoll [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈkvɔl/; gequollen [Tense=Past, VerbForm=Part] /ɡə.ˈkvɔ.lən/
  1. [C1] in großer Menge aus einer Öffnung nach außen dringen
     - example: Aus dem Fenster im zweiten Stock quoll dichter schwarzer Rauch.
     - frames: aus + Dat quellen
     - collocations: aus dem Boden quellen; aus der Wunde quellen
  2. [C1] durch Aufnahme von Wasser oder Gas weich und größer werden
     - example: Der Reis quillt beim Kochen auf das Doppelte.
     - collocations: in Wasser quellen; quellen lassen
- relations: confusable quälen (VERB): sounds almost the same; quälen means to torment
**Lexeme 2: quellen**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False
- said: /ˈkvɛ.lən/ (de-DE)
- forms: quellte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈkvɛl.tə/; gequellt [Tense=Past, VerbForm=Part] /ɡə.ˈkvɛlt/
  1. [C2] etwas in Wasser legen, damit es weich wird und größer
     - example: Die getrockneten Bohnen werden vor dem Kochen über Nacht gequellt.
     - labels: rare True

### 38. Verantwortung

**Lexeme 1: Verantwortung**, NOUN
- grammar: gender Fem; plural_class -en; countability Mass
- said: /fɛɐ.ˈant.vɔʁ.tʊŋ/ (de-DE)
- forms: Verantwortung [Case=Gen, Number=Sing] /fɛɐ.ˈant.vɔʁ.tʊŋ/; Verantwortungen [Case=Nom, Number=Plur] /fɛɐ.ˈant.vɔʁ.tʊ.ŋən/
  1. [B1] Pflicht, für eine Person oder Aufgabe zu sorgen und richtig zu entscheiden
     - example: Als Leiterin des Teams trägst du jetzt die Verantwortung für zehn Leute.
     - frames: Verantwortung für + Akk; Verantwortung gegenüber + Dat
     - collocations: Verantwortung tragen; auf eigene Verantwortung; große Verantwortung; Verantwortung abgeben; soziale Verantwortung
  2. [B2] Pflicht, für die Folgen einer Handlung einzustehen und die Schuld dafür zu tragen
     - example: Nach dem schweren Unfall wollte niemand die Verantwortung übernehmen.
     - frames: jmdn. zur Verantwortung ziehen; die Verantwortung für + Akk übernehmen
     - collocations: die Verantwortung übernehmen; die Verantwortung ablehnen; die Verantwortung von sich weisen
     - grammar: number Sing
- relations: verbal_noun_of verantworten (VERB)

### 39. sicherlich

**Lexeme 1: sicherlich**, ADV
- said: /ˈzɪ.çɐ.lɪç/ (de-DE)
  1. [B1] mit großer Wahrscheinlichkeit, fast ohne jeden Zweifel
     - example: Das ist sicherlich richtig, aber es löst unser Problem nicht.
     - collocations: sicherlich nicht
     - grammar: note often concedes a point that a following clause with aber limits
- relations: derived_from sicher (ADJ)

### 40. Anspruch

**Lexeme 1: Anspruch**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈan.ʃpʁʊx/ (de-DE)
- forms: Anspruchs [Case=Gen, Number=Sing] /ˈan.ʃpʁʊxs/; Anspruches [Case=Gen, Number=Sing] /ˈan.ʃpʁʊ.xəs/; Ansprüche [Case=Nom, Number=Plur] /ˈan.ʃpʁʏ.çə/
  1. [B1] Recht, etwas zu bekommen oder von jemandem zu verlangen
     - example: Nach einem Jahr im Betrieb haben Sie Anspruch auf vollen Urlaub.
     - frames: Anspruch auf + Akk; Anspruch haben auf + Akk; Anspruch erheben auf + Akk; etw. in Anspruch nehmen
     - collocations: Anspruch erheben; einen Anspruch geltend machen; Anspruch haben; Hilfe in Anspruch nehmen; den Anspruch verlieren; ein gesetzlicher Anspruch
  2. [B2] Erwartung, die man an die Qualität oder Leistung von jemandem oder etwas hat
     - example: Meine Eltern stellen sehr hohe Ansprüche an meine Noten.
     - frames: Ansprüche stellen an + Akk; Anspruch an + Akk
     - collocations: hohe Ansprüche; Ansprüche stellen; den Ansprüchen gerecht werden; den Ansprüchen genügen; geringe Ansprüche
     - grammar: note often in the plural

### 41. General

**Lexeme 1: General**, NOUN
- grammar: gender Masc; plural_class -e; countability Count; note plural also Generäle
- said: /ɡe.nə.ˈʁaːl/ (de-DE), /ɡɛ.nə.ˈʁaːl/ (de-DE, less_common)
- forms: Generals [Case=Gen, Number=Sing] /ɡe.nə.ˈʁaːls/; Generale [Case=Nom, Number=Plur] /ɡe.nə.ˈʁaː.lə/; Generäle [Case=Nom, Number=Plur] /ɡe.nə.ˈʁɛː.lə/
  1. [B1] Offizier mit einem der höchsten Dienstgrade in einer Armee
     - example: Nach dreißig Jahren beim Militär wurde er schließlich zum General befördert.
     - collocations: zum General befördert werden; kommandierender General; ein General außer Dienst; ein hochrangiger General
  2. [C2] oberster Leiter einer Gemeinschaft von Ordensleuten in der katholischen Kirche
     - example: Der neue General des Ordens wird von den Brüdern in Rom gewählt.
     - labels: domain religion

### 42. Tour

**Lexeme 1: Tour**, NOUN
- grammar: gender Fem; plural_class -en; countability Count
- said: /ˈtuːɐ/ (de-DE)
- forms: Tour [Case=Gen, Number=Sing] /ˈtuːɐ/; Touren [Case=Nom, Number=Plur] /ˈtuː.ʁən/
  1. [A2] Fahrt oder Wanderung zu verschiedenen Orten, meist mit Rückkehr zum Ausgangspunkt
     - example: Am Wochenende machen wir eine lange Tour mit dem Fahrrad.
     - frames: Tour durch + Akk; Tour nach + Dat
     - collocations: eine Tour machen; eine Tour planen; eine Tour unternehmen
  2. [B1] Reihe von Konzerten oder Auftritten an verschiedenen Orten nacheinander
     - example: Die Band geht im Frühling mit ihrem neuen Album auf Tour.
     - collocations: auf Tour gehen; auf Tour sein
  3. [B2] bestimmte Art, wie jemand vorgeht, um etwas zu erreichen
     - example: Mit dieser Tour kommst du bei mir nicht weiter.
     - collocations: auf die sanfte Tour; auf die krumme Tour; die alte Tour; auf die Tour
     - labels: formality informal, attitude disapproving
  4. [C1] Umdrehungen eines Motors in einer bestimmten Zeit
     - example: Bei hohen Touren wird der Motor ziemlich laut.
     - collocations: auf Touren kommen; auf vollen Touren laufen
     - labels: formality informal
     - grammar: number Plur

### 43. perfekt

**Lexeme 1: perfekt**, ADJ
- said: /pɛʁ.ˈfɛkt/ (de-DE)
  1. [A2] ohne Fehler und in jeder Hinsicht so gut wie möglich
     - example: Nach mehreren Versuchen ist ihr Kuchen diesmal wirklich perfekt geworden.
     - opposites: fehlerhaft
  2. [B2] endgültig vereinbart und abgeschlossen, sodass nichts mehr geklärt werden muss
     - example: Nach langen Verhandlungen ist der Wechsel des Spielers jetzt perfekt.
     - frames: etw. ist perfekt; etw. perfekt machen
     - grammar: position Pred
**Lexeme 2: Perfekt**, NOUN
- grammar: gender Neut; plural_class -e; countability Count
- said: /ˈpɛʁ.fɛkt/ (de-DE)
- forms: Perfekts [Case=Gen, Number=Sing] /ˈpɛʁ.fɛkts/; Perfekte [Case=Nom, Number=Plur] /ˈpɛʁ.fɛk.tə/
  1. [A2] Zeitform, meist mit Hilfsverb im Präsens und Partizip II für vergangene Ereignisse
     - example: Im Gespräch benutzen die meisten Leute eher das Perfekt als das Präteritum.
     - labels: domain grammar

### 44. Wind

**Lexeme 1: Wind**, NOUN
- grammar: gender Masc; plural_class -e; countability Count
- said: /ˈvɪnt/ (de-DE)
- forms: Windes [Case=Gen, Number=Sing] /ˈvɪn.dəs/; Winds [Case=Gen, Number=Sing] /ˈvɪnts/; Winde [Case=Nom, Number=Plur] /ˈvɪn.də/
  1. [A1] Luft, die sich draußen spürbar in eine Richtung bewegt
     - example: Heute weht ein kalter Wind von Osten über das Land.
     - collocations: der Wind weht; der Wind dreht sich; starker Wind; gegen den Wind; mit dem Wind; der Wind lässt nach; Wind und Wetter; frischer Wind
  2. [C1] Luft aus dem Darm, die durch den After nach außen geht
     - example: Nach dem Essen mit Bohnen hatte er ständig Winde.
     - collocations: einen Wind lassen
     - labels: attitude euphemistic

### 45. Forschung

**Lexeme 1: Forschung**, NOUN
- grammar: gender Fem; plural_class -en; countability Mass, mainly
- said: /ˈfɔʁ.ʃʊŋ/ (de-DE)
- forms: Forschung [Case=Gen, Number=Sing] /ˈfɔʁ.ʃʊŋ/; Forschungen [Case=Nom, Number=Plur] /ˈfɔʁ.ʃʊ.ŋən/
  1. [B1] systematische wissenschaftliche Arbeit, um neue Erkenntnisse zu gewinnen
     - example: Für die Forschung an neuen Medikamenten braucht man sehr viel Geld.
     - frames: Forschung über + Akk; Forschung zu + Dat; Forschung an + Dat
     - collocations: Forschung betreiben; Forschung fördern; Forschung und Lehre; Forschung und Entwicklung; medizinische Forschung; neuere Forschungen
  2. [B2] alle Wissenschaftler und Einrichtungen, die auf einem Gebiet nach neuem Wissen suchen
     - example: In dieser Frage ist sich die Forschung bis heute nicht einig.
     - collocations: der Stand der Forschung; die moderne Forschung
     - grammar: number Sing
- relations: verbal_noun_of forschen (VERB)
