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
  1. [B1] Beginn der Nutzung oder Geltung von etwas Neuem in einem Bereich
     - example: Die Einführung der neuen Software hat in der Firma mehrere Monate gedauert.
     - frames: Einführung von + Dat
     - collocations: die schrittweise Einführung; die Einführung beschließen; die Einführung planen; seit der Einführung
     - opposites: Abschaffung
  2. [B1] erste Erklärung der Grundlagen eines Fachs oder einer Tätigkeit, auch als Buch oder Kurs
     - example: Am ersten Tag bekommen alle neuen Mitarbeiter eine kurze Einführung.
     - frames: Einführung in + Akk
     - collocations: eine kurze Einführung; eine Einführung geben; eine Einführung bekommen; eine Einführung in das Thema
- relations: verbal_noun_of einführen (VERB)

### 2. fernsehen

**Lexeme 1: fernsehen**, VERB
- grammar: conjugation strong; irregular True; auxiliary have; valency Intr; separable True; separable_prefix fern
- said: /ˈfɛʁn.zeː.ən/ (de-DE)
- parts: fern (fern) + sehen (sehen)
- forms: sieht fern [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈziːt ˈfɛʁn/; sah fern [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈzaː ˈfɛʁn/; ferngesehen [Tense=Past, VerbForm=Part] /ˈfɛʁn.ɡə.zeː.ən/
  1. [A1] Sendungen mit Bild und Ton auf einem Bildschirm anschauen
     - example: Abends sehen wir oft noch eine Stunde zusammen fern.
     - collocations: stundenlang fernsehen; abends fernsehen; zu viel fernsehen
- relations: derived_from sehen (VERB)
**Lexeme 2: Fernsehen**, NOUN
- grammar: gender Neut; number Sing; countability Mass
- said: /ˈfɛʁn.zeː.ən/ (de-DE)
- forms: Fernsehens [Case=Gen, Number=Sing] /ˈfɛʁn.zeː.əns/
  1. [A1] Einrichtung, die Sendungen mit Bild und Ton ausstrahlt, und ihr Programm
     - example: Das Spiel wird heute Abend live im Fernsehen übertragen.
     - collocations: im Fernsehen kommen; im Fernsehen übertragen; im Fernsehen auftreten; beim Fernsehen arbeiten; das öffentlich-rechtliche Fernsehen; das private Fernsehen
  2. [B1] Gerät mit Bildschirm, mit dem man Sendungen empfängt und anschaut
     - example: Mach bitte das Fernsehen leiser, ich muss telefonieren.
     - collocations: das Fernsehen anmachen; das Fernsehen ausmachen
     - labels: formality informal
- relations: verbal_noun_of fernsehen (VERB); confusable Fernseher (NOUN): Fernseher is the usual word for the TV set; Fernsehen is mainly the medium

### 3. Industrie

**Lexeme 1: Industrie**, NOUN
- grammar: gender Fem; plural_class -n; countability Both
- said: /ɪn.dʊs.ˈtʁiː/ (de-DE)
- forms: Industrie [Case=Gen, Number=Sing] /ɪn.dʊs.ˈtʁiː/; Industrien [Case=Nom, Number=Plur] /ɪn.dʊs.ˈtʁiː.ən/
  1. [B1] Wirtschaftsbereich, in dem Waren mit Maschinen in großen Mengen hergestellt werden
     - example: Viele Menschen in dieser Region arbeiten seit Jahrzehnten in der Industrie.
     - collocations: in der Industrie arbeiten; Industrie und Handel; die heimische Industrie; Industrie ansiedeln; Arbeitsplätze in der Industrie
     - grammar: countability Mass
  2. [B2] einzelner Bereich der Wirtschaft, der eine bestimmte Art von Waren herstellt
     - example: Die chemische Industrie braucht sehr viel Energie und Wasser.
     - collocations: die chemische Industrie; die pharmazeutische Industrie; eine Industrie aufbauen; eine neue Industrie
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
  1. [B1] Gruppe aus Kanzler und Ministern, die einen Staat aus mehreren Ländern leitet
     - example: Die Bundesregierung will im nächsten Jahr mehr Geld für Schulen ausgeben.
     - collocations: die Bundesregierung bilden; Sprecher der Bundesregierung; die neue Bundesregierung; die Bundesregierung beschließt; die Bundesregierung kritisieren

### 6. Methode

**Lexeme 1: Methode**, NOUN
- grammar: gender Fem; plural_class -n; countability Mass
- said: /me.ˈtoː.də/ (de-DE)
- forms: Methode [Case=Gen, Number=Sing] /me.ˈtoː.də/; Methoden [Case=Nom, Number=Plur] /me.ˈtoː.dən/
  1. [B1] planmäßiges Verfahren nach bestimmten Regeln, um ein Ziel oder Ergebnis zu erreichen
     - example: Mit dieser neuen Methode lernen die Kinder schneller lesen.
     - frames: eine Methode, etw. zu tun; Methode für + Akk; Methode zu + Dat
     - collocations: eine Methode anwenden; eine Methode entwickeln; nach einer Methode vorgehen; eine wissenschaftliche Methode; eine bewährte Methode; eine einfache Methode
  2. [B2] Art und Weise, wie jemand vorgeht oder handelt, um etwas zu erreichen
     - example: Ich finde die Methoden unseres neuen Chefs ziemlich fragwürdig.
     - collocations: fragwürdige Methoden; brutale Methoden; Methode haben
     - grammar: note often in the plural

### 7. Ehemalige

**Lexeme 1: Ehemalige**, NOUN
- grammar: gender Com; declension adjectival; countability Count; note declined like an adjective: der Ehemalige, ein Ehemaliger, die Ehemalige
- said: /ˈeː.ə.maː.lɪ.ɡə/ (de-DE)
- forms: Ehemaligen [Case=Gen, Number=Sing] /ˈeː.ə.maː.lɪ.ɡən/; Ehemaligen [Case=Nom, Number=Plur] /ˈeː.ə.maː.lɪ.ɡən/
  1. [B2] Person, die früher zu einer Schule, Gruppe oder Organisation gehörte
     - example: Zum Jubiläum der Schule kommen viele Ehemalige aus ganz Deutschland.
     - collocations: ein Treffen der Ehemaligen
     - grammar: number Plur, mainly
  2. [C1] frühere Partnerin oder früherer Partner in einer Ehe oder Liebesbeziehung
     - example: Mit meiner Ehemaligen verstehe ich mich inzwischen wieder ganz gut.
     - labels: formality informal
- relations: derived_from ehemalig (ADJ)

### 8. müssen

**Lexeme 1: müssen**, AUX
- grammar: irregular True; auxiliary have; note modal verb, followed by an infinitive without zu
- said: /ˈmʏ.sən/ (de-DE)
- forms: muss [Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈmʊs/; musst [Tense=Pres, Person=2, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈmʊst/; musste [Tense=Past, Person=1,3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈmʊs.tə/; müsste [Tense=Past, Person=1,3, Number=Sing, VerbForm=Fin, Mood=Sub] /ˈmʏs.tə/; gemusst [Tense=Past, VerbForm=Part] /ɡə.ˈmʊst/ (only when no infinitive depends on it); müssen [Tense=Past, VerbForm=Part] /ˈmʏ.sən/ (in the perfect after an infinitive: hat arbeiten müssen)
  1. [A1] durch eine Pflicht, einen Zwang oder eine Notwendigkeit etwas zu tun haben
     - example: Morgen muss ich schon um sechs Uhr aufstehen.
     - frames: etw. tun müssen
     - grammar: note nicht müssen means 'need not'; also without infinitive before a direction: ich muss nach Hause
  2. [B1] wegen eines starken Gefühls etwas tun, ohne es verhindern zu können
     - example: Als ich das alte Foto sah, musste ich laut lachen.
     - frames: etw. tun müssen
  3. [B1] nach allem, was man weiß, sehr wahrscheinlich so sein
     - example: Das Licht brennt noch, sie muss also schon zu Hause sein.
     - frames: etw. sein müssen
  4. [A2] dringend zur Toilette gehen wollen
     - example: Halt bitte kurz an, ich muss mal ganz dringend.
     - collocations: mal müssen
     - labels: formality informal
     - grammar: valency Intr; note used without infinitive, usually with mal
- relations: confusable dürfen (AUX): English 'must not' is nicht dürfen; nicht müssen means 'need not'

### 9. Aufmerksamkeit

**Lexeme 1: Aufmerksamkeit**, NOUN
- grammar: gender Fem; plural_class -en
- said: /ˈaʊf.mɛʁk.zaːm.kaɪt/ (de-DE)
- forms: Aufmerksamkeit [Case=Gen, Number=Sing] /ˈaʊf.mɛʁk.zaːm.kaɪt/; Aufmerksamkeiten [Case=Nom, Number=Plur] /ˈaʊf.mɛʁk.zaːm.kaɪ.tən/
  1. [B1] Interesse und Konzentration, mit denen man etwas genau beobachtet oder hört
     - example: Bring doch der Gastgeberin eine kleine Aufmerksamkeit mit.
     - frames: jmdm./etw. Aufmerksamkeit schenken; Aufmerksamkeit lenken auf + Akk; Aufmerksamkeit für + Akk
     - collocations: Aufmerksamkeit erregen; Aufmerksamkeit schenken; Aufmerksamkeit auf sich ziehen; die Aufmerksamkeit wecken; volle Aufmerksamkeit; große Aufmerksamkeit; Aufmerksamkeit verdienen
     - grammar: countability Mass
     - opposites: Unaufmerksamkeit
  2. [B2] kleines Geschenk, mit dem man jemandem eine Freude macht
     - example: Vielen Dank für Ihre Aufmerksamkeit während des langen Vortrags.
     - collocations: eine kleine Aufmerksamkeit
     - grammar: countability Count
- relations: abstract_noun_of aufmerksam (ADJ)

### 10. euer

**Lexeme 1: euer**, DET
- grammar: note the e before r usually drops when an ending is added: eure, euren, eurem
- said: /ˈɔʏ.ɐ/ (de-DE)
- forms: eure [Case=Nom,Acc, Gender=Fem, Number=Sing] /ˈɔʏ.ʁə/; eure [Case=Nom,Acc, Number=Plur] /ˈɔʏ.ʁə/; euren [Case=Acc, Gender=Masc, Number=Sing] /ˈɔʏ.ʁən/; eurem [Case=Dat, Gender=Masc,Neut, Number=Sing] /ˈɔʏ.ʁəm/; eurer [Case=Gen,Dat, Gender=Fem, Number=Sing] /ˈɔʏ.ʁɐ/; eures [Case=Gen, Gender=Masc,Neut, Number=Sing] /ˈɔʏ.ʁəs/
  1. [A1] verwendet, um zu zeigen, dass etwas zu mehreren angesprochenen Personen gehört
     - example: Ich habe euren Hund heute Morgen im Park gesehen.
  2. [C1] verwendet in der höflichen Anrede an Herrscher und hochgestellte Personen
     - example: Euer Majestät, die Gäste warten bereits im großen Saal.
     - collocations: Euer Majestät; Euer Ehren; Euer Gnaden; Euer Hoheit
     - labels: time historical, style elevated
     - grammar: note written with a capital: Euer
- relations: derived_from ihr (PRON)
**Lexeme 2: euer**, PRON
- grammar: note used without a noun; takes strong endings: eurer, eure, eures
- said: /ˈɔʏ.ɐ/ (de-DE)
- forms: eurer [Case=Nom, Gender=Masc, Number=Sing] /ˈɔʏ.ʁɐ/; eure [Case=Nom,Acc, Gender=Fem, Number=Sing] /ˈɔʏ.ʁə/; eures [Case=Nom,Acc, Gender=Neut, Number=Sing] /ˈɔʏ.ʁəs/
  1. [A2] verwendet statt eines Nomens für etwas, das mehreren angesprochenen Personen gehört
     - example: Unser Auto ist kaputt, deshalb nehmen wir morgen eures.
- relations: derived_from ihr (PRON)

### 11. Gang

**Lexeme 1: Gang**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈɡaŋ/ (de-DE)
- forms: Gangs [Case=Gen, Number=Sing] /ˈɡaŋs/; Ganges [Case=Gen, Number=Sing] /ˈɡa.ŋəs/; Gänge [Case=Nom, Number=Plur] /ˈɡɛ.ŋə/
  1. [A2] langer, schmaler Raum oder Weg, der zu Zimmern führt oder zwischen Reihen liegt
     - example: Die Toiletten findest du am Ende des Gangs auf der linken Seite.
     - collocations: den Gang entlang; einen Platz am Gang; auf dem Gang warten; ein langer Gang; ein schmaler Gang
  2. [B1] Stufe im Getriebe eines Fahrzeugs, die bestimmt, wie schnell es fahren kann
     - example: Am steilen Berg solltest du besser in den zweiten Gang schalten.
     - collocations: einen Gang einlegen; in den ersten Gang schalten; einen Gang zurückschalten; den Gang wechseln; im ersten Gang fahren
  3. [B2] Ablauf, in dem etwas geschieht oder sich entwickelt
     - example: Die Vorbereitungen für das große Fest sind bereits in vollem Gange.
     - frames: etw. in Gang bringen; etw. in Gang setzen
     - collocations: in Gang kommen; im Gange sein; in vollem Gange sein; der Gang der Dinge; seinen Gang gehen
     - grammar: number Sing
  4. [B1] einzelner Teil eines Essens, der nach einem anderen serviert wird
     - example: Als zweiten Gang servieren wir heute Fisch mit Kartoffeln.
     - collocations: ein Menü mit drei Gängen; der erste Gang; einen Gang servieren
  5. [B2] Art und Weise, wie sich jemand beim Gehen bewegt
     - example: An ihrem leichten, schnellen Gang erkennt man sie schon von Weitem.
     - collocations: ein aufrechter Gang; ein unsicherer Gang; ein schleppender Gang
     - grammar: number Sing
  6. [B2] Weg, den man zu einem bestimmten Ort macht, meist um etwas zu erledigen
     - example: Morgen muss ich noch ein paar Gänge in der Stadt erledigen.
     - frames: Gang zu + Dat
     - collocations: Gänge erledigen; einen Gang machen; ein schwerer Gang; der Gang zum Arzt
- relations: derived_from gehen (VERB)
**Lexeme 2: Gang**, NOUN
- grammar: gender Fem; plural_class -s; countability Count
- said: /ˈɡɛŋ/ (de-DE)
- forms: Gang [Case=Gen, Number=Sing] /ˈɡɛŋ/; Gangs [Case=Nom, Number=Plur] /ˈɡɛŋs/
  1. [B2] Gruppe von meist jungen Leuten, die zusammen Straftaten begehen
     - example: In der Nacht hat eine Gang mehrere Autos in unserer Straße aufgebrochen.
     - collocations: eine kriminelle Gang; Mitglied einer Gang; eine Gang anführen
  2. [B2] Gruppe von Freunden, die viel Zeit miteinander verbringen
     - example: Am Wochenende treffe ich mich mit meiner Gang im Park.
     - labels: formality informal

### 12. fällen

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

### 13. nötig

**Lexeme 1: nötig**, ADJ
- grammar: comparable True
- said: /ˈnøː.tɪç/ (de-DE)
  1. [A2] unbedingt gebraucht, damit etwas gelingt oder funktioniert
     - example: Für diese Reise ist ein gültiger Reisepass unbedingt nötig.
     - frames: etw. ist nötig für + Akk; es ist nötig, etw. zu tun; etw. nötig haben
     - opposites: unnötig
- relations: derived_from Not (NOUN)

### 14. siegen

**Lexeme 1: siegen**, VERB
- grammar: conjugation weak; auxiliary have; valency Intr; irregular False
- said: /ˈziː.ɡən/ (de-DE)
- forms: siegte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈziːk.tə/; gesiegt [Tense=Past, VerbForm=Part] /ɡə.ˈziːkt/
  1. [B1] in einem Kampf, Spiel oder Wettbewerb gewinnen und der Beste sein
     - example: Am Ende siegte bei ihr die Neugier über die Angst.
     - frames: siegen über + Akk; siegen gegen + Akk
     - collocations: knapp siegen; deutlich siegen; klar siegen; nach Punkten siegen; im Finale siegen
     - opposites: verlieren
  2. [B2] sich am Ende als stärker erweisen und durchsetzen
     - example: Am Samstag hat unsere Mannschaft knapp mit zwei zu eins gesiegt.
     - frames: siegen über + Akk
     - collocations: die Vernunft siegt; die Neugier siegt; die Liebe siegt
     - labels: figurative True
- relations: derived_from Sieg (NOUN)

### 15. spitze

**Lexeme 1: Spitze**, NOUN
- grammar: gender Fem; plural_class -n
- said: /ˈʃpɪt.sə/ (de-DE)
- forms: Spitze [Case=Gen, Number=Sing] /ˈʃpɪt.sə/; Spitzen [Case=Nom, Number=Plur] /ˈʃpɪt.sən/
  1. [B1] schmales, scharfes Ende, in dem ein Gegenstand dünn zuläuft
     - example: Pass auf, die Spitze der Nadel ist sehr scharf.
     - collocations: eine scharfe Spitze; die Spitze abbrechen
     - grammar: countability Count
  2. [B1] oberster, schmal zulaufender Teil eines hohen Gegenstands
     - example: Von der Spitze des Turms sieht man die ganze Stadt.
     - collocations: die Spitze des Eisbergs
     - grammar: countability Count
  3. [B1] vorderste oder beste Stelle in einer Reihe, Gruppe oder Rangfolge
     - example: Nach dem dritten Spiel liegt unsere Mannschaft an der Spitze.
     - collocations: an der Spitze liegen; die Spitze übernehmen; sich an die Spitze setzen; an der Spitze stehen
     - grammar: countability Count
  4. [B2] Personen, die eine Organisation oder Gruppe führen und leiten
     - example: Die Spitze der Partei trifft sich morgen zu einer Sitzung.
     - frames: an der Spitze + Gen
     - collocations: die politische Spitze; an der Spitze stehen
     - grammar: countability Count
  5. [B2] feines, durchbrochenes Gewebe aus Fäden mit zarten Mustern
     - example: Ihr Hochzeitskleid war mit weißer Spitze verziert.
     - collocations: Spitze klöppeln
     - grammar: countability Mass, mainly
  6. [C1] kurze, boshafte Bemerkung, mit der man jemanden versteckt kritisiert
     - example: In seiner Rede konnte er sich eine Spitze gegen den Chef nicht verkneifen.
     - frames: eine Spitze gegen + Akk
     - collocations: sich eine Spitze nicht verkneifen können
     - grammar: countability Count
- relations: derived_from spitz (ADJ)
**Lexeme 2: spitze**, ADJ
- grammar: invariable True; comparable False; position Pred; note also used as an adverb (sie hat spitze gespielt); also written Spitze
- said: /ˈʃpɪt.sə/ (de-DE)
  1. [B1] sehr gut, großartig und kaum zu übertreffen
     - example: Das Konzert gestern Abend war einfach spitze!
     - collocations: einfach spitze
     - labels: formality informal
- relations: derived_from Spitze (NOUN)

### 16. Twitter

**Lexeme 1: Twitter**, PROPN
- grammar: gender Neut; number Sing; note usually without an article; renamed X in 2023, but the old name is still common
- said: /ˈtvɪ.tɐ/ (de-DE)
- forms: Twitters [Case=Gen, Number=Sing] /ˈtvɪ.tɐs/
  1. [B1] Internetdienst, in dem Nutzer kurze Texte öffentlich verbreiten und lesen
     - example: Viele Politiker äußern sich inzwischen fast täglich auf Twitter.
     - collocations: auf Twitter schreiben; bei Twitter angemeldet sein; auf Twitter folgen

### 17. plus

**Lexeme 1: plus**, CONJ
- grammar: invariable True
- said: /ˈplʊs/ (de-DE)
  1. [A1] verwendet, um auszudrücken, dass eine Zahl zu einer anderen addiert wird
     - example: Drei plus vier ergibt sieben, das lernen Kinder schon früh.
     - collocations: plus rechnen
     - labels: domain mathematics
     - opposites: minus
**Lexeme 2: plus**, ADP
- grammar: invariable True; note usually before a noun without article, which then shows no case
- said: /ˈplʊs/ (de-DE)
  1. [B1] mit einem zusätzlichen Betrag oder Teil, der noch dazukommt
     - example: Das Zimmer kostet achtzig Euro pro Nacht plus Frühstück.
     - frames: plus + Gen; plus + Dat
     - collocations: plus Mehrwertsteuer; plus Versandkosten; plus Porto
     - opposites: minus
**Lexeme 3: plus**, ADV
- said: /ˈplʊs/ (de-DE)
  1. [A2] über dem Nullpunkt, mit einem positiven Wert
     - example: In der Nacht sinken die Temperaturen auf plus zwei Grad.
     - collocations: plus fünf Grad
     - opposites: minus
**Lexeme 4: Plus**, NOUN
- grammar: gender Neut; plural_class -; countability Count; number Sing, mainly
- said: /ˈplʊs/ (de-DE)
- forms: Plus [Case=Gen, Number=Sing] /ˈplʊs/; Plus [Case=Nom, Number=Plur] /ˈplʊs/
  1. [B1] Betrag, um den Einnahmen oder Werte höher sind als vorher oder als die Ausgaben
     - example: Im letzten Jahr hat die Firma ein deutliches Plus gemacht.
     - frames: ein Plus von + Dat
     - collocations: ein Plus machen; im Plus sein; ins Plus kommen; ein deutliches Plus; ein Plus erwirtschaften
     - opposites: Minus
  2. [B2] Eigenschaft oder Umstand, der jemandem nützt oder etwas besser macht
     - example: Deine Erfahrung im Ausland ist für diese Stelle ein großes Plus.
     - frames: ein Plus für + Akk
     - collocations: ein großes Plus; ein echtes Plus
     - opposites: Minus

### 18. Temperatur

**Lexeme 1: Temperatur**, NOUN
- grammar: gender Masc; plural_class -en
- said: /tɛm.pə.ʁa.ˈtuːɐ/ (de-DE)
- forms: Temperatur [Case=Gen, Number=Sing] /tɛm.pə.ʁa.ˈtuːɐ/; Temperaturen [Case=Nom, Number=Plur] /tɛm.pə.ʁa.ˈtuː.ʁən/
  1. [A2] messbarer Grad, wie warm oder kalt etwas ist
     - example: Morgen sollen die Temperaturen im Süden auf über dreißig Grad steigen.
     - collocations: die Temperatur messen; die Temperatur steigt; die Temperatur sinkt; die Temperatur fällt; hohe Temperaturen; niedrige Temperaturen; Temperaturen unter null; bei Temperaturen von
     - grammar: countability Count
  2. [B1] etwas erhöhte Wärme des Körpers bei einer Krankheit
     - example: Seit gestern Abend hat unser Sohn leider ein bisschen Temperatur.
     - collocations: Temperatur haben; erhöhte Temperatur
     - grammar: countability Mass; number Sing; note usually without an article, after haben

### 19. runter

**Lexeme 1: runter**, ADV
- said: /ˈʁʊn.tɐ/ (de-DE)
  1. [A2] von oben nach unten, zum Sprecher hin oder vom Sprecher weg
     - example: Morgens rennen die Kinder immer laut die Treppe runter.
     - frames: etw. runter; runter von + Dat; runter mit + Dat
     - collocations: die Treppe runter; den Berg runter; die Straße runter
     - labels: formality informal
     - opposites: rauf

### 20. Frieden

**Lexeme 1: Frieden**, NOUN
- grammar: gender Fem; plural_class -; countability Mass; declension mixed; note plural rare; older or elevated nominative form Friede
- said: /ˈfʁiː.dən/ (de-DE)
- forms: Friedens [Case=Gen, Number=Sing] /ˈfʁiː.dəns/; Frieden [Case=Nom, Number=Plur] /ˈfʁiː.dən/
  1. [A2] Zustand, in dem zwischen Ländern oder Gruppen kein Krieg herrscht
     - example: Nach dem langen Krieg wünschen sich die Menschen endlich Frieden.
     - frames: Frieden mit + Dat; Frieden zwischen + Dat
     - collocations: Frieden schließen; in Frieden leben; den Frieden sichern; den Frieden bewahren; für den Frieden kämpfen; dauerhafter Frieden; Krieg und Frieden
     - opposites: Krieg
  2. [B1] gutes Verhältnis zwischen Menschen, ohne Streit und Ärger
     - example: Um des lieben Friedens willen habe ich nichts mehr gesagt.
     - frames: mit jmdm. Frieden schließen
     - collocations: Frieden stiften; um des lieben Friedens willen; der häusliche Frieden
  3. [B1] Ruhe, bei der man von niemandem gestört wird
     - example: Lass mich bitte in Frieden, ich muss mich konzentrieren.
     - frames: jmdn. in Frieden lassen
     - collocations: inneren Frieden finden; in Frieden ruhen

### 21. Computer

**Lexeme 1: Computer**, NOUN
- grammar: gender Masc; plural_class -; countability Count
- said: /kɔm.ˈpjuː.tɐ/ (de-DE)
- forms: Computers [Case=Gen, Number=Sing] /kɔm.ˈpjuː.tɐs/; Computer [Case=Nom, Number=Plur] /kɔm.ˈpjuː.tɐ/
  1. [A1] elektronisches Gerät, das Daten speichert, verarbeitet und Programme ausführt
     - example: Meine Schwester arbeitet den ganzen Tag am Computer im Büro.
     - collocations: am Computer arbeiten; am Computer sitzen; den Computer hochfahren; den Computer herunterfahren; den Computer einschalten; den Computer ausschalten; auf dem Computer speichern; der Computer stürzt ab

### 22. meistens

**Lexeme 1: meistens**, ADV
- said: /ˈmaɪs.təns/ (de-DE)
  1. [A2] fast immer, in der großen Mehrzahl der Fälle
     - example: Am Wochenende schlafe ich meistens bis neun Uhr.
- relations: derived_from meist (ADV)

### 23. jung

**Lexeme 1: jung**, ADJ
- grammar: comparable True
- said: /ˈjʊŋ/ (de-DE)
- forms: jünger [Degree=Cmp] /ˈjʏ.ŋɐ/; am jüngsten [Degree=Sup] /am.ˈjʏŋ.stən/
  1. [A1] erst wenige Jahre oder kurze Zeit am Leben
     - example: Meine Schwester ist drei Jahre jünger als ich.
     - collocations: junge Leute; in jungen Jahren; von jung auf; die junge Generation; eine junge Familie; jung sterben
     - opposites: alt
  2. [B1] erst seit kurzer Zeit vorhanden oder bestehend
     - example: Das junge Unternehmen sucht dringend neue Mitarbeiter für sein Büro.
     - collocations: ein junges Unternehmen; junger Wein; junges Gemüse; eine junge Demokratie
     - grammar: position Attr, mainly
     - opposites: alt
  3. [B1] frisch und voller Energie, wie man es in frühen Lebensjahren ist
     - example: Trotz ihrer achtzig Jahre fühlt sich meine Oma noch ganz jung.
     - collocations: sich jung fühlen; jung bleiben; jung aussehen; jung geblieben
     - opposites: alt

### 24. empfehlen

**Lexeme 1: empfehlen**, VERB
- grammar: conjugation strong; auxiliary have; irregular True
- said: /ɛm.ˈpfeː.lən/ (de-DE)
- forms: empfiehlt [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ɛm.ˈpfiːlt/; empfahl [Tense=Past, Person=1,3, Number=Sing, VerbForm=Fin, Mood=Ind] /ɛm.ˈpfaːl/; empfohlen [Tense=Past, VerbForm=Part] /ɛm.ˈpfoː.lən/
  1. [A2] jemandem sagen, dass eine Person oder Sache gut oder geeignet ist
     - example: Kannst du mir ein gutes Restaurant in der Nähe empfehlen?
     - frames: jmdm. etw. empfehlen; jmdm. jmdn. empfehlen; jmdm. empfehlen, etw. zu tun
     - collocations: wärmstens empfehlen; dringend empfehlen; sehr zu empfehlen sein; ein Buch empfehlen; einen Arzt empfehlen; ein Restaurant empfehlen
     - grammar: valency Tr
  2. [B2] in einer bestimmten Situation ratsam und vernünftig sein
     - example: Bei diesem Wetter empfiehlt es sich, eine warme Jacke mitzunehmen.
     - frames: es empfiehlt sich, etw. zu tun; etw. empfiehlt sich
     - grammar: valency Refl; note usually impersonal with es; sich does not mean oneself
  3. [C2] sich höflich verabschieden und einen Ort verlassen
     - example: Nach dem Essen empfahl sich der alte Herr mit einer Verbeugung.
     - labels: time dated
     - grammar: valency Refl

### 25. schützen

**Lexeme 1: schützen**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False
- said: /ʃʏ.ˈtsən/ (de-DE)
- forms: schützte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈʃʏts.tə/; geschützt [Tense=Past, VerbForm=Part] /ɡə.ˈʃʏtst/
  1. [A2] dafür sorgen, dass jemandem oder etwas nichts Schlimmes passiert
     - example: Eine warme Mütze schützt dich im Winter vor der Kälte.
     - frames: jmdn./etw. schützen vor + Dat; jmdn./etw. schützen gegen + Akk; sich schützen vor + Dat
     - collocations: die Umwelt schützen; sich vor Regen schützen; die Augen schützen; Daten schützen; wirksam schützen; die Natur schützen
  2. [B2] durch ein Gesetz oder eine Regel offiziell vor Schaden oder fremder Nutzung bewahren
     - example: Viele seltene Pflanzen in diesem Tal sind gesetzlich geschützt.
     - collocations: gesetzlich geschützt; urheberrechtlich geschützt; eine Marke schützen; unter Denkmalschutz
- relations: confusable schätzen (VERB): differs only in the vowel ü/ä and sounds similar

### 26. Material

**Lexeme 1: Material**, NOUN
- grammar: gender Neut; countability Both; note plural Materialien
- said: /ma.te.ʁi.ˈaːl/ (de-DE)
- forms: Materials [Case=Gen, Number=Sing] /ma.te.ʁi.ˈaːls/; Materialien [Case=Nom, Number=Plur] /ma.te.ʁi.ˈaː.li.ən/
  1. [B1] Stoff, aus dem etwas besteht oder hergestellt wird
     - example: Diese Jacke ist aus einem leichten, wasserdichten Material.
     - frames: Material für + Akk
     - collocations: aus festem Material; natürliche Materialien; hochwertiges Material; Material verarbeiten; das Material prüfen
  2. [B1] Dinge und Hilfsmittel, die man für eine bestimmte Arbeit braucht
     - example: Für den Kurs bringt jeder sein eigenes Material mit.
     - collocations: Material besorgen; Material zur Verfügung stellen
  3. [B2] Unterlagen, Texte, Bilder oder Daten, die man für eine Arbeit sammelt oder nutzt
     - example: Für meine Hausarbeit habe ich schon viel Material gesammelt.
     - frames: Material über + Akk; Material zu + Dat
     - collocations: Material sammeln; Material auswerten; umfangreiches Material; belastendes Material
     - grammar: countability Mass, mainly
- relations: confusable Materie (NOUN): Materie means matter or a subject area, not the substance something is made of

### 27. Mittwoch

**Lexeme 1: Mittwoch**, NOUN
- grammar: gender Masc; plural_class -e; countability Count
- said: /mɪt.ˈvɔx/ (de-DE)
- forms: Mittwochs [Case=Gen, Number=Sing] /ˈmɪt.vɔxs/; Mittwoche [Case=Nom, Number=Plur] /ˈmɪt.vɔ.xə/
  1. [A1] Tag zwischen Dienstag und Donnerstag
     - example: Jeden Mittwoch gehe ich nach der Arbeit zum Schwimmen.
     - collocations: jeden Mittwoch; nächsten Mittwoch; letzten Mittwoch; diesen Mittwoch

### 28. Baum

**Lexeme 1: Baum**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈbaʊm/ (de-DE)
- forms: Baumes [Case=Gen, Number=Sing] /ˈbaʊ.məs/; Baums [Case=Gen, Number=Sing] /ˈbaʊms/; Bäume [Case=Nom, Number=Plur] /ˈbɔʏ.mə/
  1. [A1] große Pflanze mit einem Stamm aus Holz, Ästen und Blättern oder Nadeln
     - example: Hinter unserem Haus wächst ein alter Baum mit einer großen Krone.
     - collocations: einen Baum pflanzen; einen Baum fällen; auf einen Baum klettern; ein Baum wächst; ein Baum blüht; im Schatten eines Baumes
  2. [B1] geschmückter Nadelbaum, der zu Weihnachten im Zimmer steht
     - example: Am Nachmittag schmücken die Kinder gemeinsam den Baum.
     - collocations: den Baum schmücken; unter dem Baum liegen
     - grammar: note usually with the definite article

### 29. Tätigkeit

**Lexeme 1: Tätigkeit**, NOUN
- grammar: gender Fem; plural_class -en
- said: /ˈtɛː.tɪç.kaɪt/ (de-DE)
- forms: Tätigkeit [Case=Gen, Number=Sing] /ˈtɛː.tɪç.kaɪt/; Tätigkeiten [Case=Nom, Number=Plur] /ˈtɛː.tɪç.kaɪ.tən/
  1. [B1] Arbeit oder Beschäftigung, die jemand regelmäßig oder beruflich ausübt
     - example: Seine neue Tätigkeit in der Firma macht ihm viel Spaß.
     - frames: Tätigkeit als + Nom
     - collocations: eine Tätigkeit ausüben; berufliche Tätigkeit; ehrenamtliche Tätigkeit; eine Tätigkeit aufnehmen; selbstständige Tätigkeit; eine Tätigkeit beenden
     - grammar: countability Count
  2. [C1] Zustand, in dem eine Maschine oder ein Organ arbeitet und funktioniert
     - example: Nach der Reparatur ist die alte Pumpe wieder in Tätigkeit.
     - collocations: in Tätigkeit sein; in Tätigkeit setzen; außer Tätigkeit setzen
     - grammar: countability Mass; number Sing
- relations: abstract_noun_of tätig (ADJ)

### 30. Farbe

**Lexeme 1: Farbe**, NOUN
- grammar: gender Fem; plural_class -n
- said: /ˈfaʁ.bə/ (de-DE)
- forms: Farbe [Case=Gen, Number=Sing] /ˈfaʁ.bə/; Farben [Case=Nom, Number=Plur] /ˈfaʁ.bən/
  1. [A1] Eindruck, den das Licht einer Oberfläche im Auge hervorruft
     - example: Welche Farbe soll das neue Sofa im Wohnzimmer haben?
     - collocations: eine helle Farbe; eine dunkle Farbe; eine kräftige Farbe; in Farbe; die Farbe wechseln; Farbe bekommen
     - grammar: countability Count
  2. [A2] Stoff, mit dem man etwas anstreicht oder bemalt
     - example: Vorsicht, die Farbe an der Tür ist noch nicht trocken!
     - collocations: Farbe auftragen; frische Farbe; die Farbe blättert ab; die Farbe trocknet
     - grammar: countability Mass, mainly
  3. [B2] eine der vier Gruppen gleicher Zeichen in einem Kartenspiel
     - example: Wer keine Karte dieser Farbe hat, darf einen Trumpf spielen.
     - collocations: Farbe bedienen
     - grammar: countability Count

### 31. Ecke

**Lexeme 1: Ecke**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /ˈɛ.kə/ (de-DE)
- forms: Ecke [Case=Gen, Number=Sing] /ˈɛ.kə/; Ecken [Case=Nom, Number=Plur] /ˈɛ.kən/
  1. [A2] Stelle, an der zwei oder mehr Seiten, Kanten oder Wände zusammentreffen
     - example: Stell die große Lampe doch bitte in die Ecke neben dem Sofa.
     - frames: in der Ecke + Gen
     - collocations: in der Ecke stehen; in die Ecke stellen; sich an einer Ecke stoßen; eine scharfe Ecke; in der hintersten Ecke
  2. [A2] Stelle, an der zwei Straßen oder Häuserreihen aufeinandertreffen
     - example: Gleich um die Ecke gibt es eine kleine Bäckerei.
     - collocations: um die Ecke; gleich um die Ecke; an der Ecke; um die Ecke biegen; an der nächsten Ecke
  3. [B1] Gegend oder Teil eines Landes oder einer Stadt
     - example: Aus welcher Ecke von Deutschland kommst du eigentlich?
     - collocations: aus allen Ecken; eine schöne Ecke; in dieser Ecke
     - labels: formality informal
  4. [B1] Schuss aus dem Winkel des Spielfelds, wenn der Gegner den Ball ins Toraus spielt
     - example: Nach der dritten Ecke fiel endlich das erste Tor.
     - collocations: eine Ecke treten; eine Ecke ausführen; eine Ecke geben; eine Ecke verursachen

### 32. Mama

**Lexeme 1: Mama**, NOUN
- grammar: gender Fem; plural_class -s; countability Count
- said: /ˈma.ma/ (de-DE), /ma.ˈmaː/ (de-DE, older)
- forms: Mama [Case=Gen, Number=Sing] /ˈma.ma/; Mamas [Case=Nom, Number=Plur] /ˈma.mas/
  1. [A1] Mutter, vor allem als Anrede oder Bezeichnung in der Familie
     - example: Meine Mama holt mich heute nach der Schule ab.
     - labels: formality informal

### 33. ungefähr

**Lexeme 1: ungefähr**, ADV
- said: /ˈʊn.ɡə.fɛːɐ/ (de-DE), /ʊn.ɡə.ˈfɛːɐ/ (de-DE)
  1. [A2] nicht ganz genau, aber ziemlich nah an der genannten Zahl oder Angabe
     - example: Bis zum Bahnhof sind es ungefähr zehn Minuten zu Fuß.
     - collocations: so ungefähr; ungefähr gleich; ungefähr wissen; um ungefähr
     - opposites: genau
**Lexeme 2: ungefähr**, ADJ
- grammar: position Attr; comparable False
- said: /ˈʊn.ɡə.fɛːɐ/ (de-DE), /ʊn.ɡə.ˈfɛːɐ/ (de-DE)
  1. [B1] nicht ganz genau, aber dem Richtigen ziemlich nahe
     - example: Kannst du mir eine ungefähre Vorstellung von den Kosten geben?
     - collocations: eine ungefähre Vorstellung; eine ungefähre Angabe; die ungefähre Zahl; die ungefähre Richtung
     - opposites: genau

### 34. vorne

**Lexeme 1: vorne**, ADV
- said: /ˈfɔʁ.nə/ (de-DE, vorne), /ˈfɔʁn/ (de-DE, vorn)
- spellings: vorne; vorn
  1. [A1] auf der Seite, die dem Betrachter oder der Richtung der Bewegung zugewandt ist
     - example: Im Kino sitze ich am liebsten ganz vorne.
     - collocations: nach vorne; von vorne; ganz vorne; da vorne; vorne sitzen
     - opposites: hinten
  2. [B1] am Anfang einer Reihe oder Folge, an erster Stelle
     - example: Nach dem Fehler mussten wir leider wieder ganz von vorne anfangen.
     - collocations: von vorne anfangen; vorne liegen; ganz vorne
     - opposites: hinten

### 35. Brücke

**Lexeme 1: Brücke**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /ˈbʁʏ.kə/ (de-DE)
- forms: Brücke [Case=Gen, Number=Sing] /ˈbʁʏ.kə/; Brücken [Case=Nom, Number=Plur] /ˈbʁʏ.kən/
  1. [A1] Bauwerk, auf dem man über einen Fluss, eine Straße oder ein Tal kommt
     - example: Hinter der alten Brücke beginnt die Altstadt mit ihren engen Gassen.
     - frames: Brücke über + Akk
     - collocations: eine Brücke überqueren; über die Brücke gehen; über die Brücke fahren; eine Brücke bauen; die Brücke führt über den Fluss; eine Brücke sperren
  2. [B2] etwas, das Menschen, Gruppen oder Dinge miteinander verbindet
     - example: Musik kann eine Brücke zwischen ganz verschiedenen Kulturen sein.
     - frames: Brücke zwischen + Dat; Brücke zu + Dat; jmdm. eine goldene Brücke bauen
     - collocations: eine Brücke schlagen; Brücken bauen; alle Brücken hinter sich abbrechen
     - labels: figurative True
  3. [B2] künstlicher Zahnersatz, der an den Zähnen daneben befestigt wird
     - example: Mein Zahnarzt will mir nächste Woche eine neue Brücke einsetzen.
     - collocations: eine Brücke einsetzen
  4. [C1] erhöhter Raum auf einem Schiff, von dem aus es gesteuert wird
     - example: Der Kapitän stand die ganze Nacht auf der Brücke.
     - collocations: auf der Brücke stehen
     - labels: domain nautical

### 36. Armee

**Lexeme 1: Armee**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /aʁ.ˈmeː/ (de-DE)
- forms: Armee [Case=Gen, Number=Sing] /aʁ.ˈmeː/; Armeen [Case=Nom, Number=Plur] /aʁ.ˈmeː.ən/
  1. [B1] alle Soldaten und militärischen Truppen, die ein Staat hat
     - example: Mein Bruder hat nach der Schule zwei Jahre in der Armee gedient.
     - collocations: in der Armee dienen; zur Armee gehen; eine Armee aufstellen; die Armee einsetzen; eine stehende Armee; eine reguläre Armee
  2. [C1] großer militärischer Verband, der aus mehreren Divisionen besteht
     - example: Im Winter wurde die ganze Armee von feindlichen Truppen eingeschlossen.
     - labels: domain military
  3. [B2] sehr große Zahl von Menschen oder Dingen
     - example: Für das Stadtfest stand eine ganze Armee von Helfern bereit.
     - frames: eine Armee von + Dat
     - labels: figurative True

### 37. Trainer

**Lexeme 1: Trainer**, NOUN
- grammar: gender Masc; plural_class -; countability Count
- said: /ˈtʁɛː.nɐ/ (de-DE), /ˈtʁeː.nɐ/ (de-DE)
- forms: Trainers [Case=Gen, Number=Sing] /ˈtʁɛː.nɐs/; Trainer [Case=Nom, Number=Plur] /ˈtʁɛː.nɐ/
  1. [A2] Person, die Menschen oder Tiere durch regelmäßige Übungen ausbildet
     - example: Unser Trainer zeigt uns heute Übungen für den Rücken.
  2. [B2] bequemer Anzug aus Jacke und Hose, den man beim Sport trägt
     - example: Am Sonntag sitze ich gern im Trainer auf dem Sofa.
     - regions: de-AT only, de-CH only

### 38. Garten

**Lexeme 1: Garten**, NOUN
- grammar: gender Masc; plural_class -¨; countability Mass
- said: /ˈɡaʁ.tən/ (de-DE)
- forms: Gartens [Case=Gen, Number=Sing] /ˈɡaʁ.təns/; Gärten [Case=Nom, Number=Plur] /ˈɡɛʁ.tən/
  1. [A1] abgegrenztes Stück Land, meist am Haus, auf dem man Pflanzen anbaut oder sich erholt
     - example: Im Sommer essen wir abends oft draußen im Garten.
     - collocations: einen Garten anlegen; den Garten pflegen; im Garten arbeiten; den Garten umgraben; botanischer Garten; zoologischer Garten

### 39. Behandlung

**Lexeme 1: Behandlung**, NOUN
- grammar: gender Fem; plural_class -en
- said: /bə.ˈhand.lʊŋ/ (de-DE)
- forms: Behandlung [Case=Gen, Number=Sing] /bə.ˈhand.lʊŋ/; Behandlungen [Case=Nom, Number=Plur] /bə.ˈhand.lʊ.ŋən/
  1. [B1] Maßnahmen eines Arztes oder Therapeuten, um eine Krankheit oder Verletzung zu heilen
     - example: Nach drei Wochen Behandlung geht es meinem Knie endlich besser.
     - frames: Behandlung bei + Dat; Behandlung gegen + Akk
     - collocations: in Behandlung sein; sich in Behandlung begeben; ärztliche Behandlung; eine Behandlung beginnen; eine Behandlung abbrechen; stationäre Behandlung; ambulante Behandlung
     - grammar: countability Count
  2. [B2] Art und Weise, wie man mit einem Menschen oder Tier umgeht
     - example: Viele Mitarbeiter beschweren sich über die unfreundliche Behandlung durch den Chef.
     - collocations: eine faire Behandlung; eine schlechte Behandlung; eine bevorzugte Behandlung; Gleichbehandlung fordern
     - grammar: countability Mass
  3. [C1] ausführliche Beschäftigung mit einem Thema oder einer Frage, etwa im Unterricht
     - example: Die Behandlung dieses Themas dauert im Unterricht mehrere Wochen.
     - collocations: die Behandlung eines Themas; die Behandlung einer Frage
     - grammar: countability Mass
- relations: verbal_noun_of behandeln (VERB)

### 40. bloß

**Lexeme 1: bloß**, ADJ
- grammar: comparable False
- said: /ˈbloːs/ (de-DE)
- spellings: bloß (de-DE, de-AT); bloss (de-CH)
  1. [B2] nicht von Kleidung oder etwas anderem bedeckt
     - example: Die Kinder liefen mit bloßen Füßen über den warmen Sand.
     - collocations: mit bloßem Auge; mit bloßen Händen; mit bloßen Füßen; auf der bloßen Haut; mit bloßem Oberkörper
     - grammar: position Attr, mainly
  2. [B2] allein schon das Genannte, ohne dass etwas anderes dazukommt
     - example: Schon der bloße Gedanke an die Prüfung macht mich nervös.
     - collocations: der bloße Gedanke; der bloße Anblick; eine bloße Vermutung; ein bloßer Zufall
     - grammar: position Attr
**Lexeme 2: bloß**, ADV
- said: /ˈbloːs/ (de-DE)
- spellings: bloß (de-DE, de-AT); bloss (de-CH)
  1. [A2] nicht mehr als das Genannte und nichts darüber hinaus
     - example: Ich habe bloß noch zehn Euro im Portemonnaie.
     - collocations: nicht bloß; bloß noch; bloß so
     - labels: formality informal
**Lexeme 3: bloß**, PART
- said: /ˈbloːs/ (de-DE)
- spellings: bloß (de-DE, de-AT); bloss (de-CH)
  1. [B1] verwendet, um eine Warnung oder Aufforderung dringender zu machen
     - example: Fass den heißen Topf bloß nicht ohne Handschuhe an!
     - collocations: bloß nicht
     - grammar: note in imperative sentences, often with nicht
  2. [B1] verwendet in Fragen, um Sorge, Ratlosigkeit oder Ärger auszudrücken
     - example: Wo habe ich bloß wieder meine Brille hingelegt?
     - grammar: note in questions with a question word
  3. [B2] verwendet in Wunschsätzen, um einen starken Wunsch auszudrücken
     - example: Wenn der Regen doch bloß endlich aufhören würde!
     - grammar: note in wishes, usually with Konjunktiv II and often with doch

### 41. davor

**Lexeme 1: davor**, ADV
- said: /da.ˈfoːɐ/ (de-DE), /ˈdaː.foːɐ/ (de-DE)
- parts: da (da) + vor (vor)
  1. [A2] vor dem genannten Zeitpunkt oder Ereignis
     - example: Am Abend war die Prüfung, aber kurz davor hatte ich noch gelernt.
     - frames: kurz davor sein, etw. zu tun
     - collocations: kurz davor; am Tag davor; die Woche davor; lange davor; kurz davor sein
     - opposites: danach
  2. [B1] verwendet für eine genannte oder folgende Sache, die man fürchtet, vermeidet oder abwehrt
     - example: Viele Kinder haben Angst davor, allein im Dunkeln zu schlafen.
     - frames: davor, etw. zu tun; davor, dass ...
     - collocations: Angst davor haben; davor warnen; sich davor fürchten; davor schützen; sich davor drücken
  3. [A2] an der vorderen Seite der genannten Sache oder des genannten Ortes
     - example: Das Haus ist ziemlich alt, aber davor wächst ein schöner Baum.
     - collocations: direkt davor; davor stehen
     - opposites: dahinter

### 42. sparen

**Lexeme 1: sparen**, VERB
- grammar: conjugation weak; auxiliary have; valency Both; irregular False
- said: /ˈʃpaː.ʁən/ (de-DE)
- forms: sparte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈʃpaːɐ.tə/; gespart [Tense=Past, VerbForm=Part] /ɡə.ˈʃpaːɐt/
  1. [A2] Geld nicht ausgeben, sondern für später zurücklegen
     - example: Seit einem Jahr sparen wir jeden Monat für eine neue Küche.
     - frames: für + Akk sparen; auf + Akk sparen; etw. sparen
     - collocations: Geld sparen; eisern sparen; fleißig sparen; auf ein Auto sparen
     - grammar: valency Intr, mainly
  2. [A2] weniger von etwas verbrauchen oder ausgeben, als man könnte
     - example: Mit dem neuen Kühlschrank sparen wir eine Menge Strom.
     - frames: etw. sparen; an + Dat sparen; bei + Dat sparen
     - collocations: Zeit sparen; Strom sparen; Energie sparen; Kosten sparen; Platz sparen; am falschen Ende sparen
     - grammar: valency Tr, mainly
     - opposites: verschwenden
  3. [B2] etwas Unnötiges oder Unangenehmes nicht tun oder nicht sagen
     - example: Deine guten Ratschläge kannst du dir wirklich sparen.
     - frames: sich etw. sparen; jmdm. etw. sparen
     - collocations: sich die Mühe sparen; sich den Kommentar sparen; sich den Weg sparen
     - labels: formality informal
     - grammar: valency Tr; note usually with a dative reflexive pronoun: sich etw. sparen

### 43. Gast

**Lexeme 1: Gast**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈɡast/ (de-DE)
- forms: Gastes [Case=Gen, Number=Sing] /ˈɡas.təs/; Gasts [Case=Gen, Number=Sing] /ˈɡasts/; Gäste [Case=Nom, Number=Plur] /ˈɡɛs.tə/
  1. [A1] Person, die jemand eingeladen hat oder die jemanden für kurze Zeit besucht
     - example: Am Samstag erwarten wir ungefähr zwanzig Gäste zu unserer Feier.
     - frames: bei jmdm. zu Gast sein; jmdn. zu Gast haben
     - collocations: Gäste einladen; Gäste empfangen; Gäste bewirten; zu Gast sein; ein ungebetener Gast; ein gern gesehener Gast; Gäste haben
     - opposites: Gastgeber
  2. [A2] Person, die in einem Restaurant oder Hotel gegen Bezahlung isst oder wohnt
     - example: Der Kellner bringt den Gästen am Fenstertisch gerade die Speisekarte.
     - collocations: Gäste bedienen; ein zahlender Gast
  3. [B2] Mannschaft, die nicht auf dem eigenen Platz spielt
     - example: Kurz vor der Pause gingen die Gäste überraschend mit einem Tor in Führung.
     - labels: domain sport
     - grammar: number Plur, mainly; note usually with the definite article: die Gäste
     - opposites: Gastgeber

### 44. Leitung

**Lexeme 1: Leitung**, NOUN
- grammar: gender Fem; plural_class -en
- said: /ˈlaɪ.tʊŋ/ (de-DE)
- forms: Leitung [Case=Gen, Number=Sing] /ˈlaɪ.tʊŋ/; Leitungen [Case=Nom, Number=Plur] /ˈlaɪ.tʊŋ.ən/
  1. [B1] Verantwortung dafür, eine Gruppe, Firma oder Veranstaltung zu führen
     - example: Seit Januar hat sie die Leitung der ganzen Abteilung übernommen.
     - frames: unter der Leitung von + Dat
     - collocations: die Leitung übernehmen; die Leitung haben; die Leitung abgeben; mit der Leitung beauftragen
     - grammar: countability Mass; number Sing
  2. [B1] Personen, die eine Firma, Schule oder Organisation führen
     - example: Die Leitung der Schule hat neue Regeln für die Pausen beschlossen.
     - collocations: die Leitung des Hauses; sich an die Leitung wenden
     - grammar: countability Count
  3. [B1] Rohr oder Kabel, in dem Wasser, Gas oder Strom transportiert wird
     - example: Im alten Haus müssen alle Leitungen neu verlegt werden.
     - collocations: eine Leitung verlegen; eine Leitung reparieren; eine Leitung ist undicht
     - grammar: countability Count
  4. [B1] Verbindung zwischen zwei Anschlüssen beim Telefonieren
     - example: Ich versuche seit einer Stunde anzurufen, aber die Leitung ist immer besetzt.
     - collocations: die Leitung ist besetzt; in der Leitung sein; in der Leitung bleiben; die Leitung ist tot
     - grammar: countability Count
- relations: verbal_noun_of leiten (VERB)

### 45. quellen

**Lexeme 1: quellen**, VERB
- grammar: conjugation strong; auxiliary be; valency Intr; irregular True
- said: /ˈkvɛ.lən/ (de-DE)
- forms: quillt [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈkvɪlt/; quoll [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈkvɔl/; gequollen [Tense=Past, VerbForm=Part] /ɡə.ˈkvɔ.lən/
  1. [C1] in großer Menge aus einer Öffnung nach außen dringen oder fließen
     - example: Aus dem alten Schornstein quillt schon seit Stunden dicker schwarzer Rauch.
     - frames: aus + Dat quellen
     - collocations: Rauch quillt; Blut quillt aus der Wunde; Wasser quillt aus dem Boden; Tränen quellen aus den Augen
  2. [C1] durch das Aufnehmen von Wasser größer und weicher werden
     - example: Die Bohnen müssen vor dem Kochen über Nacht in Wasser quellen.
     - collocations: quellen lassen; Reis quillt
- relations: confusable Quelle (NOUN): the infinitive is spelled like Quellen, the plural of the noun Quelle
**Lexeme 2: quellen**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False
- said: /ˈkvɛ.lən/ (de-DE)
- forms: quellte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈkvɛl.tə/; gequellt [Tense=Past, VerbForm=Part] /ɡə.ˈkvɛlt/
  1. [C2] etwas in Wasser legen, damit es weich wird und sich vergrößert
     - example: Vor dem Kochen quellt man die getrockneten Erbsen in kaltem Wasser.
     - labels: rare True

### 46. Verantwortung

**Lexeme 1: Verantwortung**, NOUN
- grammar: gender Fem; plural_class -en; countability Mass, mainly
- said: /fɛɐ.ˈant.vɔʁ.tʊŋ/ (de-DE)
- forms: Verantwortung [Case=Gen, Number=Sing] /fɛɐ.ˈant.vɔʁ.tʊŋ/; Verantwortungen [Case=Nom, Number=Plur] /fɛɐ.ˈant.vɔʁ.tʊ.ŋən/
  1. [B1] Pflicht, für eine Person oder Aufgabe zu sorgen und Entscheidungen zu treffen
     - example: Als Abteilungsleiterin trägt sie die Verantwortung für zwanzig Mitarbeiter.
     - frames: Verantwortung für + Akk
     - collocations: Verantwortung tragen; Verantwortung übernehmen; große Verantwortung; auf eigene Verantwortung; Verantwortung abgeben; in der Verantwortung stehen
  2. [B2] Pflicht, für die Folgen einer Handlung oder eines Fehlers einzustehen
     - example: Niemand will die Verantwortung für den Unfall auf der Baustelle übernehmen.
     - frames: jmdn. zur Verantwortung ziehen; Verantwortung für + Akk
     - collocations: zur Verantwortung ziehen; die volle Verantwortung übernehmen; jede Verantwortung ablehnen; die Verantwortung zuschieben
- relations: verbal_noun_of verantworten (VERB)

### 47. sicherlich

**Lexeme 1: sicherlich**, ADV
- said: /ˈzɪ.çɐ.lɪç/ (de-DE)
  1. [B1] mit großer Wahrscheinlichkeit, nach fester Überzeugung des Sprechers
     - example: Du hast sicherlich schon gehört, dass unsere Firma bald umzieht.
     - collocations: sicherlich richtig; sicherlich nicht
- relations: derived_from sicher (ADJ)

### 48. Anspruch

**Lexeme 1: Anspruch**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈan.ʃpʁʊx/ (de-DE)
- forms: Anspruchs [Case=Gen, Number=Sing] /ˈan.ʃpʁʊxs/; Anspruches [Case=Gen, Number=Sing] /ˈan.ʃpʁʊ.xəs/; Ansprüche [Case=Nom, Number=Plur] /ˈan.ʃpʁʏ.çə/
  1. [B1] Erwartung, was eine Person oder Sache bieten oder leisten soll
     - example: Meine Eltern stellen ziemlich hohe Ansprüche an meine Noten.
     - frames: Anspruch an + Akk
     - collocations: hohe Ansprüche stellen; den Ansprüchen genügen; Ansprüche erfüllen; den Anspruch erheben; viel Zeit in Anspruch nehmen; Hilfe in Anspruch nehmen
  2. [B1] Recht, etwas Bestimmtes zu bekommen oder zu fordern
     - example: Jeder Arbeitnehmer hat Anspruch auf bezahlten Urlaub.
     - frames: Anspruch auf + Akk
     - collocations: Anspruch haben; Anspruch erheben; einen Anspruch geltend machen; einen Anspruch verlieren; rechtlicher Anspruch
- relations: derived_from ansprechen (VERB)

### 49. General

**Lexeme 1: General**, NOUN
- grammar: gender Masc; plural_class -e; countability Count; note plural also Generäle
- said: /ɡe.nə.ˈʁaːl/ (de-DE)
- forms: Generals [Case=Gen, Number=Sing] /ɡe.nə.ˈʁaːls/; Generale [Case=Nom, Number=Plur] /ɡe.nə.ˈʁaː.lə/; Generäle [Case=Nom, Number=Plur] /ɡe.nə.ˈʁɛː.lə/
  1. [B1] Offizier, der einen der höchsten Ränge in einer Armee hat
     - example: Morgen besucht ein General aus dem Ministerium unsere Kaserne.
     - collocations: zum General befördert werden; kommandierender General; ein pensionierter General; ein hoher General

### 50. Tour

**Lexeme 1: Tour**, NOUN
- grammar: gender Fem; plural_class -en; countability Count
- said: /ˈtuːɐ/ (de-DE)
- forms: Tour [Case=Gen, Number=Sing] /ˈtuːɐ/; Touren [Case=Nom, Number=Plur] /ˈtuː.ʁən/
  1. [A2] Fahrt oder Wanderung, bei der man eine bestimmte Strecke zurücklegt
     - example: Am Wochenende machen wir eine Tour mit dem Fahrrad zum See.
     - frames: Tour durch + Akk; Tour nach + Dat
     - collocations: eine Tour machen; eine Tour unternehmen; eine Tour planen; eine lange Tour
  2. [B1] Reise eines Künstlers oder einer Band mit Auftritten in vielen Städten
     - example: Nächstes Jahr geht die Band wieder auf große Tour.
     - collocations: auf Tour gehen; auf Tour sein
  3. [C1] geschickte, oft unehrliche Art, mit der jemand etwas erreichen will
     - example: Mit dieser Tour kommst du bei mir nicht weiter.
     - collocations: auf die krumme Tour; auf die sanfte Tour; die Tour zieht nicht
     - labels: formality informal
  4. [C1] eine volle Drehung eines Motors oder einer Maschine
     - example: Der alte Motor braucht im Winter lange, bis er auf Touren kommt.
     - collocations: auf Touren kommen; auf vollen Touren laufen; auf Touren bringen
     - grammar: number Plur, mainly

### 51. perfekt

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

### 52. Wind

**Lexeme 1: Wind**, NOUN
- grammar: gender Masc; plural_class -e; countability Count, mainly
- said: /ˈvɪnt/ (de-DE)
- forms: Windes [Case=Gen, Number=Sing] /ˈvɪn.dəs/; Winds [Case=Gen, Number=Sing] /ˈvɪnts/; Winde [Case=Nom, Number=Plur] /ˈvɪn.də/
  1. [A2] deutlich spürbare Bewegung der Luft im Freien
     - example: Heute weht ein kalter Wind von Norden über die Felder.
     - collocations: der Wind weht; der Wind bläst; der Wind dreht sich; der Wind legt sich; gegen den Wind; starker Wind; kalter Wind; bei Wind und Wetter; frischer Wind

### 53. Forschung

**Lexeme 1: Forschung**, NOUN
- grammar: gender Fem; plural_class -en
- said: /ˈfɔʁ.ʃʊŋ/ (de-DE)
- forms: Forschung [Case=Gen, Number=Sing] /ˈfɔʁ.ʃʊŋ/; Forschungen [Case=Nom, Number=Plur] /ˈfɔʁ.ʃʊ.ŋən/
  1. [B1] Arbeit mit wissenschaftlichen Methoden, um neues Wissen zu gewinnen
     - example: Für die Forschung an neuen Medikamenten braucht man viel Geld.
     - frames: Forschung über + Akk; Forschung zu + Dat; Forschung an + Dat
     - collocations: Forschung betreiben; Forschungen anstellen; in der Forschung arbeiten; Forschung fördern; medizinische Forschung; Forschung und Lehre; Forschung und Entwicklung
     - grammar: countability Mass, mainly
  2. [B2] alle Wissenschaftler und Einrichtungen, die auf einem Gebiet wissenschaftlich arbeiten
     - example: Heute ist sich die Forschung in dieser Frage weitgehend einig.
     - collocations: nach dem heutigen Stand der Forschung; die neuere Forschung; die Forschung ist sich einig
     - grammar: number Sing
- relations: verbal_noun_of forschen (VERB)
