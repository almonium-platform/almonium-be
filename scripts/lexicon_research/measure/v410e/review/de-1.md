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
- grammar: gender Fem; plural_class -en
- said: /ˈaɪn.fyː.ʁʊŋ/ (de-DE)
- forms: Einführung [Case=Gen, Number=Sing] /ˈaɪn.fyː.ʁʊŋ/; Einführungen [Case=Nom, Number=Plur] /ˈaɪn.fyː.ʁʊ.ŋən/
  1. [B1] erstmalige Verwendung oder Geltung von etwas Neuem in einem Bereich
     - example: Seit der Einführung der neuen Software arbeiten wir viel schneller.
     - frames: die Einführung von + Dat
     - collocations: die Einführung beschließen; eine schrittweise Einführung; die Einführung einer Steuer; die Einführung eines neuen Produkts
     - grammar: countability Mass, mainly
  2. [B1] erste Erklärung der Grundlagen eines Fachs oder Themas, auch als Kurs oder Buch
     - example: Am ersten Tag bekommen alle neuen Mitarbeiter eine kurze Einführung.
     - frames: Einführung in + Akk
     - collocations: eine kurze Einführung; eine Einführung geben; zur Einführung
     - grammar: countability Count
- relations: verbal_noun_of einführen (VERB)

### 2. fernsehen

**Lexeme 1: fernsehen**, VERB
- grammar: conjugation strong; auxiliary have; valency Intr; irregular True; separable True; separable_prefix fern
- said: /ˈfɛʁn.ˌzeː.ən/ (de-DE)
- parts: fern (fern) + sehen (sehen)
- forms: sieht fern [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈziːt ˈfɛʁn/; sah fern [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈzaː ˈfɛʁn/; ferngesehen [Tense=Past, VerbForm=Part] /ˈfɛʁn.ɡə.ˌzeː.ən/
  1. [A1] Sendungen und Filme anschauen, die ein Sender auf den Bildschirm bringt
     - example: Abends sehen wir oft zusammen fern, bevor die Kinder schlafen gehen.
     - collocations: viel fernsehen; stundenlang fernsehen; abends fernsehen
- relations: derived_from sehen (VERB)
**Lexeme 2: Fernsehen**, NOUN
- grammar: gender Neut; number Sing; countability Mass
- said: /ˈfɛʁn.ˌzeː.ən/ (de-DE)
- forms: Fernsehens [Case=Gen, Number=Sing] /ˈfɛʁn.ˌzeː.əns/
  1. [A1] Medium und Einrichtung, die Sendungen mit Bild und Ton verbreiten
     - example: Das Spiel wird heute Abend live im Fernsehen übertragen.
     - collocations: im Fernsehen kommen; im Fernsehen übertragen; im Fernsehen zeigen; beim Fernsehen arbeiten; das öffentlich-rechtliche Fernsehen; das private Fernsehen
- relations: derived_from fernsehen (VERB)

### 3. Industrie

**Lexeme 1: Industrie**, NOUN
- grammar: gender Fem; plural_class -n; countability Both
- said: /ɪn.dʊs.ˈtʁiː/ (de-DE)
- forms: Industrie [Case=Gen, Number=Sing] /ɪn.dʊs.ˈtʁiː/; Industrien [Case=Nom, Number=Plur] /ɪn.dʊs.ˈtʁiː.ən/
  1. [B1] Teil der Wirtschaft, der in Fabriken Waren in großen Mengen herstellt
     - example: In dieser Gegend arbeiten fast alle Menschen in der Industrie.
     - collocations: in der Industrie arbeiten; Industrie und Handel; die heimische Industrie; Industrie ansiedeln; die Industrie fördern
     - grammar: countability Mass
  2. [B2] alle Betriebe, die eine bestimmte Art von Waren herstellen
     - example: Die chemische Industrie will im nächsten Jahr deutlich mehr investieren.
     - collocations: die chemische Industrie; die pharmazeutische Industrie; die verarbeitende Industrie; die Industrie eines Landes
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
  1. [B1] Kanzler und Minister, die zusammen einen aus Ländern bestehenden Staat führen
     - example: Die Bundesregierung will im nächsten Jahr mehr Geld für Schulen ausgeben.
     - collocations: die Bundesregierung bilden; Sprecher der Bundesregierung; die Bundesregierung kritisieren; die Bundesregierung fordert; die neue Bundesregierung

### 6. Ehemalige

**Lexeme 1: Ehemalige**, NOUN
- grammar: gender Com; declension adjectival; countability Count; note declined like an adjective: der/die Ehemalige, ein Ehemaliger, eine Ehemalige
- said: /ˈeː.ə.maː.lɪ.ɡə/ (de-DE)
- forms: Ehemaligen [Case=Gen, Number=Sing] /ˈeː.ə.maː.lɪ.ɡən/; Ehemaligen [Case=Nom, Number=Plur] /ˈeː.ə.maː.lɪ.ɡən/ (after the definite article: die Ehemaligen); Ehemalige [Case=Nom, Number=Plur] /ˈeː.ə.maː.lɪ.ɡə/ (without an article: viele Ehemalige); Ehemaliger [Case=Nom, Number=Sing, Gender=Masc] /ˈeː.ə.maː.lɪ.ɡɐ/ (for a man after ein or without an article)
  1. [B2] Person, die früher Mitglied einer Gruppe, Schule oder Firma war
     - example: Zum Jubiläum der Schule kommen auch viele Ehemalige aus dem ganzen Land.
     - collocations: ein Treffen der Ehemaligen
     - grammar: number Plur, mainly
  2. [B2] Person, mit der man früher eine Liebesbeziehung oder Ehe hatte
     - example: Gestern habe ich im Supermarkt zufällig meine Ehemalige getroffen.
     - labels: formality informal
- relations: derived_from ehemalig (ADJ)

### 7. müssen

**Lexeme 1: müssen**, VERB
- grammar: irregular True; auxiliary have; note modal verb; with an infinitive the perfect uses müssen, not gemusst (hat kommen müssen)
- said: /ˈmʏ.sən/ (de-DE)
- forms: muss [Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈmʊs/; musste [Tense=Past, Person=1,3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈmʊs.tə/; gemusst [Tense=Past, VerbForm=Part] /ɡə.ˈmʊst/ (only without a dependent infinitive); müsste [Tense=Past, Person=1,3, Number=Sing, VerbForm=Fin, Mood=Sub] /ˈmʏs.tə/
  1. [A1] keine andere Wahl haben, als etwas zu tun, weil es Pflicht oder notwendig ist
     - example: Morgen muss ich schon um sechs Uhr aufstehen.
     - frames: etw. tun müssen
     - collocations: arbeiten müssen; warten müssen; nach Hause müssen; zum Arzt müssen
     - grammar: note nicht müssen means 'need not', not 'must not' (that is nicht dürfen)
  2. [B1] nach allem, was man weiß, mit großer Sicherheit so sein
     - example: Das Licht brennt, also muss sie schon zu Hause sein.
     - frames: etw. muss so sein
  3. [B1] dringend zur Toilette gehen wollen
     - example: Halt bitte kurz an, ich muss mal ganz dringend.
     - collocations: mal müssen
     - labels: formality informal, attitude euphemistic
     - grammar: valency Intr

### 8. euer

**Lexeme 1: euer**, DET
- grammar: note declined like ein; the e before r is usually dropped when an ending follows (eure, euren)
- said: /ˈɔʏ.ɐ/ (de-DE)
- forms: eure [Case=Nom,Acc, Gender=Fem, Number=Sing] /ˈɔʏ.ʁə/; eure [Case=Nom,Acc, Number=Plur] /ˈɔʏ.ʁə/; euren [Case=Acc, Gender=Masc, Number=Sing] /ˈɔʏ.ʁən/; euren [Case=Dat, Number=Plur] /ˈɔʏ.ʁən/; eurem [Case=Dat, Gender=Masc,Neut, Number=Sing] /ˈɔʏ.ʁəm/; eurer [Case=Gen,Dat, Gender=Fem, Number=Sing] /ˈɔʏ.ʁɐ/; eurer [Case=Gen, Number=Plur] /ˈɔʏ.ʁɐ/; eures [Case=Gen, Gender=Masc,Neut, Number=Sing] /ˈɔʏ.ʁəs/
  1. [A1] zu mehreren angesprochenen Personen gehörend, die man duzt
     - example: Vergesst bitte eure Jacken nicht, draußen ist es sehr kalt.
  2. [C1] verwendet in der höflichen Anrede vor dem Titel einer hohen Person
     - example: Euer Ehren, mein Mandant wusste nichts von diesem Vertrag.
     - collocations: Eure Majestät; Euer Ehren; Eure Exzellenz; Eure Hoheit
     - labels: formality formal, style elevated
     - grammar: note written with a capital letter in this use

### 9. Gang

**Lexeme 1: Gang**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈɡaŋ/ (de-DE)
- forms: Ganges [Case=Gen, Number=Sing] /ˈɡa.ŋəs/; Gangs [Case=Gen, Number=Sing] /ˈɡaŋs/; Gänge [Case=Nom, Number=Plur] /ˈɡɛ.ŋə/
  1. [A2] langer, schmaler Weg in einem Gebäude zwischen Zimmern, Sitzreihen oder Regalen
     - example: Die Toiletten findet ihr am Ende des Gangs auf der rechten Seite.
     - collocations: ein langer Gang; ein schmaler Gang; den Gang entlanggehen; einen Platz am Gang
  2. [B1] eine von mehreren Stufen im Getriebe eines Fahrzeugs, die die Geschwindigkeit bestimmen
     - example: Am steilen Berg solltest du besser in einen kleineren Gang schalten.
     - collocations: einen Gang einlegen; in den nächsten Gang schalten; einen Gang zurückschalten; den ersten Gang einlegen; im dritten Gang fahren; einen Gang höher schalten
  3. [B1] eines von mehreren Gerichten, die bei einem Essen nacheinander serviert werden
     - example: Als zweiten Gang gab es eine kräftige Suppe mit frischem Brot.
     - collocations: ein Menü mit drei Gängen; der erste Gang; einen Gang servieren
  4. [B2] Ablauf, in dem etwas geschieht oder sich weiterentwickelt
     - example: Die Arbeiten an der neuen Brücke sind bereits in vollem Gang.
     - collocations: in Gang bringen; in Gang kommen; in Gang setzen; in vollem Gang sein; der Gang der Dinge; seinen Gang gehen
  5. [B2] Art, wie jemand beim Laufen die Beine und den Körper bewegt
     - example: Schon an seinem müden Gang merkt man, wie anstrengend der Tag war.
     - collocations: ein aufrechter Gang; ein unsicherer Gang; ein federnder Gang
  6. [B2] Weg zu einem bestimmten Ort, um dort etwas zu erledigen
     - example: Morgen früh muss ich noch schnell einen Gang zur Bank machen.
     - frames: Gang zu + Dat
     - collocations: einen Gang machen; der Gang zum Arzt; ein schwerer Gang
- relations: derived_from gehen (VERB)
**Lexeme 2: Gang**, NOUN
- grammar: gender Fem; plural_class -s; countability Count
- said: /ˈɡɛŋ/ (de-DE)
- forms: Gang [Case=Gen, Number=Sing] /ˈɡɛŋ/; Gangs [Case=Nom, Number=Plur] /ˈɡɛŋs/
  1. [B2] Gruppe von Leuten, die zusammenhalten und gemeinsam Straftaten begehen
     - example: In diesem Viertel kämpfen zwei Gangs seit Jahren um die Macht.
     - collocations: eine kriminelle Gang; Mitglied einer Gang; einer Gang angehören

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
- grammar: invariable True; position Pred; comparable False; note also used as an adverb: sie spielt spitze
- said: /ˈʃpɪ.tsə/ (de-DE)
  1. [B1] außerordentlich gut und alle Erwartungen übertreffend
     - example: Das Konzert gestern Abend war wirklich spitze!
     - frames: etw. ist spitze; jmd. ist spitze
     - collocations: einfach spitze; echt spitze
     - labels: formality informal
- relations: derived_from Spitze (NOUN); confusable spitz (ADJ): spitze is also an inflected form of spitz 'pointed' (eine spitze Nadel)
**Lexeme 2: Spitze**, NOUN
- grammar: gender Fem; plural_class -n
- said: /ˈʃpɪ.tsə/ (de-DE)
- forms: Spitze [Case=Gen, Number=Sing] /ˈʃpɪ.tsə/; Spitzen [Case=Nom, Number=Plur] /ˈʃpɪ.tsən/
  1. [B1] dünnes, scharfes Ende, in dem ein Gegenstand nach vorn oder oben ausläuft
     - example: Die Spitze meines Bleistifts ist schon wieder abgebrochen.
     - collocations: die Spitze des Eisbergs; die Spitze des Turms
     - grammar: countability Count
  2. [B1] erster, vorderster Platz in einer Reihe, Rangfolge oder einem Wettbewerb
     - example: Nach dem dritten Spieltag liegt unsere Mannschaft an der Spitze der Tabelle.
     - collocations: an der Spitze liegen; an der Spitze stehen; sich an die Spitze setzen; die Spitze übernehmen
     - grammar: countability Count; number Sing
  3. [B2] die Personen, die eine Organisation oder einen Staat leiten
     - example: Die Spitze der Partei trifft sich morgen zu einem Krisengespräch.
     - collocations: die politische Spitze
     - grammar: countability Count
  4. [B2] feines, durchbrochenes Gewebe mit Mustern, oft zur Verzierung von Kleidung
     - example: Das Hochzeitskleid ihrer Großmutter war mit weißer Spitze besetzt.
     - grammar: countability Mass, mainly
  5. [C1] kleine, boshafte Bemerkung, mit der man jemanden ärgern oder treffen will
     - example: Ihre Spitze gegen den Chef hat im Raum niemand überhört.
     - frames: eine Spitze gegen + Akk
     - grammar: countability Count
- relations: derived_from spitz (ADJ)

### 13. Twitter

**Lexeme 1: Twitter**, PROPN
- grammar: gender Neut; number Sing; note usually without article; renamed X in 2023, but the old name is still common
- said: /ˈtvɪ.tɐ/ (de-DE)
  1. [B1] sozialer Onlinedienst, in dem man kurze Beiträge öffentlich schreibt und liest
     - example: Viele Politiker teilen ihre Meinung inzwischen fast täglich auf Twitter.
     - frames: jmdm. auf Twitter folgen
     - collocations: auf Twitter posten; bei Twitter angemeldet sein

### 14. plus

**Lexeme 1: plus**, CONJ
- grammar: invariable True
- said: /ˈplʊs/ (de-DE)
  1. [A1] verwendet, um beim Rechnen eine Zahl zu einer anderen zu addieren
     - example: Drei plus vier ist sieben, das weiß schon jedes Kind.
     - labels: domain mathematics
     - opposites: minus
**Lexeme 2: plus**, ADP
- grammar: invariable True; note a noun without an article after it usually stays uninflected
- said: /ˈplʊs/ (de-DE)
  1. [B1] verwendet, um zu sagen, dass ein Betrag oder eine Sache noch dazukommt
     - example: Die Wohnung kostet achthundert Euro plus Nebenkosten im Monat.
     - frames: plus + Gen; plus + Dat
     - opposites: minus
**Lexeme 3: plus**, ADV
- grammar: invariable True
- said: /ˈplʊs/ (de-DE)
  1. [A2] über dem Nullpunkt, mit einem positiven Wert
     - example: In der Nacht sinken die Temperaturen auf plus zwei Grad.
     - collocations: plus fünf Grad
     - opposites: minus
  2. [B1] etwas besser als die genannte Schulnote
     - example: Für meinen Aufsatz habe ich diesmal eine Zwei plus bekommen.
     - labels: domain education
     - opposites: minus
**Lexeme 4: Plus**, NOUN
- grammar: gender Neut; plural_class -; countability Count; note the plural is rare
- said: /ˈplʊs/ (de-DE)
- forms: Plus [Case=Gen, Number=Sing] /ˈplʊs/; Plus [Case=Nom, Number=Plur] /ˈplʊs/
  1. [B2] Betrag, um den eine Summe größer ist als vorher oder als erwartet
     - example: Am Ende des Jahres hat die Firma ein kleines Plus gemacht.
     - frames: ein Plus von + Dat
     - collocations: im Plus sein; ein Plus machen; ins Plus kommen; ein deutliches Plus
     - opposites: Minus
  2. [B2] positive Eigenschaft, die einer Person oder Sache einen Vorteil bringt
     - example: Gute Englischkenntnisse sind bei dieser Stelle ein echtes Plus.
     - frames: ein Plus für + Akk
     - collocations: ein großes Plus; ein echtes Plus
     - opposites: Minus

### 15. runter

**Lexeme 1: runter**, ADV
- said: /ˈʁʊn.tɐ/ (de-DE)
  1. [B1] von einer höheren Stelle an eine tiefere Stelle, nach unten
     - example: Komm bitte sofort vom Dach runter, das ist viel zu gefährlich!
     - frames: runter von + Dat
     - collocations: die Treppe runter; den Berg runter; die Straße runter; Runter mit dir!
     - labels: formality informal
     - opposites: rauf
- relations: derived_from herunter (ADV); derived_from hinunter (ADV)

### 16. Computer

**Lexeme 1: Computer**, NOUN
- grammar: gender Masc; plural_class -; countability Count
- said: /kɔm.ˈpjuː.tɐ/ (de-DE)
- forms: Computers [Case=Gen, Number=Sing] /kɔm.ˈpjuː.tɐs/; Computer [Case=Nom, Number=Plur] /kɔm.ˈpjuː.tɐ/
  1. [A1] elektronisches Gerät, das Daten speichert und nach Programmen verarbeitet
     - example: Abends sitzt mein Bruder oft stundenlang am Computer.
     - collocations: am Computer arbeiten; am Computer sitzen; den Computer einschalten; den Computer ausschalten; den Computer hochfahren; den Computer herunterfahren; ein Programm auf dem Computer installieren; der Computer stürzt ab

### 17. meistens

**Lexeme 1: meistens**, ADV
- said: /ˈmaɪs.təns/ (de-DE)
  1. [A2] fast immer, in der Mehrzahl der Fälle, aber nicht jedes Mal
     - example: Am Wochenende stehe ich meistens erst gegen zehn Uhr auf.
- relations: derived_from meist (ADV)

### 18. jung

**Lexeme 1: jung**, ADJ
- grammar: comparable True
- said: /ˈjʊŋ/ (de-DE)
- forms: jünger [Degree=Cmp] /ˈjʏ.ŋɐ/; am jüngsten [Degree=Sup] /am ˈjʏŋ.stən/
  1. [A1] noch nicht alt, erst seit relativ kurzer Zeit am Leben
     - example: Als junges Mädchen wollte meine Oma unbedingt Ärztin werden.
     - frames: zu jung für + Akk; jung genug, um etw. zu tun
     - collocations: junge Leute; ein junger Mann; eine junge Frau; jung und alt; jung aussehen; jung bleiben; jung sterben; von jung auf
     - opposites: alt
  2. [B1] erst vor kurzer Zeit entstanden oder gegründet
     - example: Diese junge Firma hat schon über hundert Mitarbeiter.
     - collocations: ein junges Unternehmen; eine junge Demokratie; junger Wein
     - opposites: alt

### 19. empfehlen

**Lexeme 1: empfehlen**, VERB
- grammar: conjugation strong; auxiliary have; irregular True
- said: /ɛm.ˈpfeː.lən/ (de-DE)
- forms: empfiehlt [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ɛm.ˈpfiːlt/; empfahl [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ɛm.ˈpfaːl/; empfohlen [Tense=Past, VerbForm=Part] /ɛm.ˈpfoː.lən/; empfähle [Tense=Past, Person=1,3, Number=Sing, VerbForm=Fin, Mood=Sub] /ɛm.ˈpfɛː.lə/
  1. [A2] jemandem raten, etwas zu tun oder zu wählen, weil es gut ist
     - example: Der Arzt hat mir empfohlen, jeden Tag mehr Wasser zu trinken.
     - frames: jmdm. etw. empfehlen; jmdm. jmdn. empfehlen; jmdm. empfehlen, etw. zu tun
     - collocations: wärmstens empfehlen; dringend empfehlen; nur empfehlen können; sehr zu empfehlen sein; ein Restaurant empfehlen; ein Buch empfehlen
     - grammar: valency Tr
  2. [B2] in einer bestimmten Situation klug, sinnvoll und ratsam sein
     - example: Im Sommer empfiehlt es sich, die Unterkunft früh zu buchen.
     - frames: es empfiehlt sich, etw. zu tun
     - grammar: valency Refl; note mostly impersonal: es empfiehlt sich

### 20. Material

**Lexeme 1: Material**, NOUN
- grammar: gender Neut; note plural Materialien
- said: /ma.te.ˈʁjaːl/ (de-DE)
- forms: Materials [Case=Gen, Number=Sing] /ma.te.ˈʁjaːls/; Materialien [Case=Nom, Number=Plur] /ma.te.ˈʁjaː.ljən/
  1. [B1] Stoff, aus dem man etwas herstellt oder baut
     - example: Diese Jacke ist aus einem sehr leichten, wasserdichten Material.
     - frames: Material für + Akk
     - collocations: aus festem Material; natürliches Material; das Material prüfen; hochwertiges Material; Material verarbeiten
     - grammar: countability Mass, mainly
  2. [B1] Dinge und Hilfsmittel, die man für eine bestimmte Arbeit braucht
     - example: Bringt bitte morgen euer Material für den Kunstunterricht mit.
     - collocations: Material besorgen; Materialien für den Unterricht
     - grammar: countability Mass, mainly
  3. [B2] gesammelte Informationen, Texte oder Bilder, mit denen man arbeitet
     - example: Für meine Abschlussarbeit habe ich schon viel Material gesammelt.
     - frames: Material über + Akk; Material zu + Dat
     - collocations: Material sammeln; Material auswerten; umfangreiches Material; belastendes Material
     - grammar: countability Mass
- relations: confusable Materie (NOUN): similar form; Materie means matter or subject matter, not a material things are made of

### 21. Baum

**Lexeme 1: Baum**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈbaʊm/ (de-DE)
- forms: Baumes [Case=Gen, Number=Sing] /ˈbaʊ.məs/; Baums [Case=Gen, Number=Sing] /ˈbaʊms/; Bäume [Case=Nom, Number=Plur] /ˈbɔʏ.mə/
  1. [A1] große Pflanze mit einem Stamm aus Holz, Ästen und Blättern oder Nadeln
     - example: Hinter unserem Haus steht ein alter Baum mit vielen Äpfeln.
     - collocations: einen Baum pflanzen; einen Baum fällen; auf einen Baum klettern; im Schatten eines Baumes; ein hoher Baum; ein alter Baum
  2. [B1] zu Weihnachten geschmückte Tanne oder Fichte in der Wohnung
     - example: Morgen stellen wir im Wohnzimmer endlich den Baum auf.
     - collocations: den Baum schmücken; den Baum aufstellen; unter dem Baum liegen
     - labels: formality informal

### 22. Tätigkeit

**Lexeme 1: Tätigkeit**, NOUN
- grammar: gender Fem; plural_class -en
- said: /ˈtɛː.tɪç.ˌkaɪt/ (de-DE), /ˈteː.tɪç.ˌkaɪt/ (de-DE)
- forms: Tätigkeit [Case=Gen, Number=Sing] /ˈtɛː.tɪç.ˌkaɪt/; Tätigkeiten [Case=Nom, Number=Plur] /ˈtɛː.tɪç.ˌkaɪ.tən/
  1. [B1] Arbeit oder Beschäftigung, mit der sich jemand regelmäßig befasst, oft im Beruf
     - example: Nach dem Studium suchte sie eine Tätigkeit im sozialen Bereich.
     - frames: Tätigkeit als + Nom
     - collocations: eine Tätigkeit ausüben; eine Tätigkeit aufnehmen; berufliche Tätigkeit; ehrenamtliche Tätigkeit; selbstständige Tätigkeit; eine Tätigkeit beenden
     - grammar: countability Count
  2. [C1] das Arbeiten und Funktionieren einer Maschine, eines Organs oder einer Einrichtung
     - example: Die Ärzte beobachten die Tätigkeit des Herzens während der Operation genau.
     - collocations: in Tätigkeit treten; in Tätigkeit setzen; außer Tätigkeit setzen; in Tätigkeit sein
     - grammar: countability Mass; number Sing
- relations: derived_from tätig (ADJ)

### 23. Farbe

**Lexeme 1: Farbe**, NOUN
- grammar: gender Fem; plural_class -n
- said: /ˈfaʁ.bə/ (de-DE)
- forms: Farbe [Case=Gen, Number=Sing] /ˈfaʁ.bə/; Farben [Case=Nom, Number=Plur] /ˈfaʁ.bən/
  1. [A1] das, was man an einer Sache sieht, wenn Licht auf sie fällt, etwa rot oder blau
     - example: Welche Farbe soll das neue Sofa im Wohnzimmer haben?
     - collocations: eine helle Farbe; eine dunkle Farbe; eine kräftige Farbe; in Farbe; Farbe bekommen; die Farbe wechseln
     - grammar: countability Count
  2. [A2] Stoff, mit dem man etwas anstreicht, bemalt oder färbt
     - example: Vorsicht, an der Tür ist die Farbe noch nicht trocken!
     - collocations: frische Farbe; Farbe auftragen; die Farbe blättert ab; mit Farbe streichen
     - grammar: countability Mass, mainly
  3. [B2] eine der vier Gruppen, in die die Karten eines Kartenspiels eingeteilt sind
     - example: Wer keine Karte dieser Farbe hat, darf eine andere spielen.
     - collocations: Farbe bekennen
     - grammar: countability Count
  4. [B2] bestimmte Farben als Zeichen eines Landes, Vereins oder einer Gruppe
     - example: Seit fünf Jahren spielt sie für die Farben unseres Vereins.
     - collocations: die Farben vertreten
     - grammar: countability Count; number Plur

### 24. Ecke

**Lexeme 1: Ecke**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /ˈɛ.kə/ (de-DE)
- forms: Ecke [Case=Gen, Number=Sing] /ˈɛ.kə/; Ecken [Case=Nom, Number=Plur] /ˈɛ.kən/
  1. [A1] Stelle, an der zwei oder mehr Kanten, Seiten oder Wände zusammentreffen
     - example: Stell die große Pflanze doch in die Ecke neben dem Fenster.
     - collocations: in der Ecke stehen; in die Ecke stellen; sich an einer Ecke stoßen; abgerundete Ecken; in die Ecke drängen; an allen Ecken und Enden
  2. [A2] Stelle, an der zwei Straßen zusammentreffen oder eine Straße abbiegt
     - example: Der kleine Bäcker an der Ecke hat jeden Morgen frische Brötchen.
     - collocations: an der Ecke; um die Ecke biegen; gleich um die Ecke; um die Ecke wohnen
  3. [B1] nicht genau begrenzte Gegend, Teil eines Landes oder einer Stadt
     - example: In unserer Ecke gibt es leider kaum noch Geschäfte.
     - collocations: aus dieser Ecke kommen; in dieser Ecke wohnen
     - labels: formality informal
  4. [B2] Schuss vom Rand des Spielfelds, nachdem die Abwehr den Ball ins Toraus spielte
     - example: Kurz vor dem Ende bekam unsere Mannschaft noch eine Ecke.
     - collocations: eine Ecke treten; eine Ecke ausführen; eine Ecke bekommen; eine Ecke herausholen

### 25. Mama

**Lexeme 1: Mama**, NOUN
- grammar: gender Fem; plural_class -s; countability Count
- said: /ˈma.ma/ (de-DE), /ma.ˈmaː/ (de-DE, older)
- forms: Mama [Case=Gen, Number=Sing] /ˈma.ma/; Mamas [Case=Gen, Number=Sing] /ˈma.mas/ (without article, before a noun: Mamas Auto); Mamas [Case=Nom, Number=Plur] /ˈma.mas/
  1. [A1] vertraute, liebevolle Bezeichnung für die eigene oder eine andere Mutter
     - example: Kannst du mich heute nach der Schule abholen, Mama?
     - collocations: Mama und Papa; werdende Mama; frischgebackene Mama
     - labels: formality informal

### 26. ungefähr

**Lexeme 1: ungefähr**, ADV
- said: /ˈʊn.ɡə.fɛːɐ/ (de-DE), /ʊn.ɡə.ˈfɛːɐ/ (de-DE, less_common), /ˈʊn.ɡə.feːɐ/ (de-DE, regional)
  1. [A2] nicht ganz genau, aber nicht weit von der richtigen Zahl oder Angabe entfernt
     - example: Die Fahrt zum Flughafen dauert ungefähr eine halbe Stunde.
     - collocations: so ungefähr; ungefähr gleich; ungefähr so; ungefähr wissen
     - opposites: genau
**Lexeme 2: ungefähr**, ADJ
- grammar: comparable False; position Attr, mainly
- said: /ˈʊn.ɡə.fɛːɐ/ (de-DE), /ʊn.ɡə.ˈfɛːɐ/ (de-DE, less_common), /ˈʊn.ɡə.feːɐ/ (de-DE, regional)
  1. [B1] nicht ganz genau, aber im Großen und Ganzen zutreffend
     - example: Ich habe nur eine ungefähre Vorstellung davon, was die Reparatur kostet.
     - collocations: eine ungefähre Vorstellung; eine ungefähre Ahnung; ungefähre Angaben; die ungefähre Richtung
     - opposites: genau

### 27. vorne

**Lexeme 1: vorne**, ADV
- said: /ˈfɔʁ.nə/ (de-DE)
- spellings: vorne; vorn
  1. [A1] an der Seite oder am Ende, das nach der Richtung zeigt, in die man schaut
     - example: Im Kino sitze ich am liebsten ganz vorne.
     - frames: vorne in + Dat; vorne an + Dat; vorne auf + Dat
     - collocations: nach vorne; von vorne; ganz vorne; weiter vorne; vorne sitzen; da vorne
     - opposites: hinten
  2. [A2] am Anfang eines Textes, einer Reihe oder eines Ablaufs
     - example: Nach dem Fehler mussten wir noch einmal von vorne anfangen.
     - collocations: von vorne anfangen; von vorne beginnen; vorne im Buch; wieder von vorne
     - opposites: hinten
  3. [B1] an der Spitze, vor den anderen in einem Wettkampf oder Vergleich
     - example: Nach der ersten Runde liegt unsere Mannschaft klar vorne.
     - collocations: vorne liegen; ganz vorne sein; vorne mitspielen; vorne dabei sein
     - labels: figurative True

### 28. Brücke

**Lexeme 1: Brücke**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /ˈbʁʏ.kə/ (de-DE)
- forms: Brücke [Case=Gen, Number=Sing] /ˈbʁʏ.kə/; Brücken [Case=Nom, Number=Plur] /ˈbʁʏ.kən/
  1. [A2] Bauwerk, das über ein Hindernis führt, damit man es überqueren kann
     - example: Hinter dem Dorf führt eine alte Brücke aus Stein über den Fluss.
     - frames: Brücke über + Akk
     - collocations: über eine Brücke fahren; über die Brücke gehen; eine Brücke überqueren; eine Brücke bauen; eine Brücke sprengen; eine Brücke stürzt ein
  2. [B2] etwas, das Menschen, Gruppen oder Bereiche einander näherbringt
     - example: Gemeinsame Musik kann eine Brücke zwischen fremden Kulturen sein.
     - frames: Brücke zwischen + Dat; Brücke zu + Dat
     - collocations: eine Brücke schlagen; Brücken bauen; alle Brücken hinter sich abbrechen
     - labels: figurative True
  3. [C1] fester künstlicher Zahn, der an den Zähnen daneben befestigt wird
     - example: Nächste Woche bekomme ich beim Zahnarzt endlich meine neue Brücke.
     - collocations: eine Brücke einsetzen
  4. [C1] erhöhter Raum auf einem Schiff, von dem aus man es steuert
     - example: Der Kapitän blieb während des Sturms die ganze Nacht auf der Brücke.
     - labels: domain nautical

### 29. Armee

**Lexeme 1: Armee**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /aʁ.ˈmeː/ (de-DE)
- forms: Armee [Case=Gen, Number=Sing] /aʁ.ˈmeː/; Armeen [Case=Nom, Number=Plur] /aʁ.ˈmeː.ən/
  1. [B1] alle Soldaten und militärischen Einheiten, die ein Staat hat
     - example: Mein Bruder war nach der Schule ein Jahr bei der Armee.
     - collocations: bei der Armee sein; zur Armee gehen; in die Armee eintreten; die Armee einsetzen; eine Armee aufbauen
  2. [B2] großer Verband von Soldaten, der aus mehreren Divisionen besteht
     - example: Die feindliche Armee stand schon wenige Kilometer vor der Stadt.
     - collocations: eine Armee führen; eine Armee aufstellen; die feindliche Armee
  3. [C1] sehr große Zahl von Menschen, die zusammen auftreten
     - example: Vor dem Konzert wartete eine ganze Armee von Fans am Eingang.
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
- grammar: gender Fem; plural_class -en
- said: /bə.ˈhand.lʊŋ/ (de-DE)
- forms: Behandlung [Case=Gen, Number=Sing] /bə.ˈhand.lʊŋ/; Behandlungen [Case=Nom, Number=Plur] /bə.ˈhand.lʊ.ŋən/
  1. [B1] Maßnahmen eines Arztes oder Therapeuten, um eine Krankheit oder Verletzung zu heilen
     - example: Nach dem Unfall war sie mehrere Wochen in ärztlicher Behandlung.
     - frames: in Behandlung bei + Dat; Behandlung gegen + Akk
     - collocations: in ärztlicher Behandlung sein; sich in Behandlung begeben; eine Behandlung abbrechen; ambulante Behandlung; stationäre Behandlung; medizinische Behandlung
     - grammar: countability Count
  2. [B2] Art und Weise, wie man mit einer Person oder Sache umgeht
     - example: Viele Mitarbeiter beschweren sich über die ungerechte Behandlung durch ihren Chef.
     - frames: Behandlung durch + Akk
     - collocations: schlechte Behandlung; ungerechte Behandlung; bevorzugte Behandlung; faire Behandlung
     - grammar: countability Mass
  3. [C1] ausführliche Beschäftigung mit einem Thema in Text, Unterricht oder Gespräch
     - example: Die Behandlung dieser Frage verschieben wir auf die nächste Sitzung.
     - collocations: die Behandlung eines Themas
     - grammar: countability Mass
- relations: verbal_noun_of behandeln (VERB)

### 32. bloß

**Lexeme 1: bloß**, ADJ
- grammar: comparable False
- said: /ˈbloːs/ (de-DE)
- spellings: bloß (de-DE, de-AT); bloss (de-CH)
  1. [B2] nicht von Kleidung, Schuhen oder einer anderen Hülle bedeckt
     - example: Im Sommer laufen die Kinder gern mit bloßen Füßen über die Wiese.
     - collocations: mit bloßen Händen; mit bloßem Auge; mit bloßen Füßen; auf der bloßen Haut; mit bloßem Oberkörper
     - grammar: position Attr, mainly
  2. [B2] nichts weiter als das Genannte, ohne etwas Zusätzliches
     - example: Schon der bloße Gedanke an die Prüfung macht mich nervös.
     - collocations: der bloße Gedanke; ein bloßer Zufall; die bloße Vorstellung; die bloße Tatsache; eine bloße Vermutung
     - grammar: position Attr
**Lexeme 2: bloß**, ADV
- said: /ˈbloːs/ (de-DE)
- spellings: bloß (de-DE, de-AT); bloss (de-CH)
  1. [B1] verwendet, um etwas auf das Genannte zu beschränken und nicht mehr
     - example: Ich wollte bloß kurz fragen, ob du morgen Zeit hast.
     - collocations: nicht bloß; bloß noch; bloß ein bisschen
**Lexeme 3: bloß**, PART
- said: /ˈbloːs/ (de-DE)
- spellings: bloß (de-DE, de-AT); bloss (de-CH)
  1. [B1] verwendet, um eine Frage, eine Warnung oder einen Wunsch zu verstärken
     - example: Fass den heißen Topf bloß nicht mit der Hand an!
     - collocations: bloß nicht; was ist bloß los; wenn ich bloß wüsste
     - labels: formality informal
     - grammar: note unstressed in questions, stressed in warnings and wishes

### 33. davor

**Lexeme 1: davor**, ADV
- said: /da.ˈfoːɐ/ (de-DE)
- parts: da (da) + vor (vor)
  1. [B1] verwendet, um mit „vor“ auf eine genannte oder folgende Sache zu verweisen
     - example: Viele Kinder fürchten sich vor Hunden, aber mein Sohn hat keine Angst davor.
     - frames: Angst davor haben, dass ...; jmdn. davor warnen, etw. zu tun; sich davor fürchten, dass ...; jmdn. davor schützen, dass ...
     - collocations: Angst davor haben; davor warnen; sich davor schützen; sich davor fürchten; sich davor drücken
  2. [A2] zu einer früheren Zeit als das gerade genannte Ereignis
     - example: Wir essen um sieben, aber davor gehe ich noch schnell einkaufen.
     - frames: kurz davor sein, etw. zu tun
     - collocations: kurz davor; am Tag davor; die Woche davor; kurz davor sein
     - opposites: danach
  3. [A2] an der vorderen Seite des gerade genannten Ortes oder Gegenstands
     - example: Am Ende der Straße steht eine alte Kirche, und davor liegt ein Platz.
     - collocations: direkt davor
     - opposites: dahinter
- relations: confusable bevor (SCONJ): both mean 'before', but bevor introduces a clause while davor is an adverb standing alone

### 34. sparen

**Lexeme 1: sparen**, VERB
- grammar: conjugation weak; auxiliary have; valency Both; irregular False
- said: /ˈʃpaː.ʁən/ (de-DE)
- forms: sparte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈʃpaːɐ.tə/; gespart [Tense=Past, VerbForm=Part] /ɡə.ˈʃpaːɐt/
  1. [A2] Geld nicht ausgeben, sondern für später zurücklegen
     - example: Seit Monaten sparen wir für einen neuen Kühlschrank.
     - frames: etw. sparen; sparen für + Akk; sparen auf + Akk
     - collocations: Geld sparen; fürs Alter sparen; eisern sparen; auf ein Auto sparen
     - grammar: valency Both
  2. [A2] weniger von etwas verbrauchen, als man sonst verbrauchen würde
     - example: Mit den neuen Fenstern sparen wir im Winter viel Energie.
     - frames: etw. sparen; sparen an + Dat; sparen mit + Dat
     - collocations: Zeit sparen; Energie sparen; Strom sparen; Kosten sparen; Platz sparen; am falschen Ende sparen
     - grammar: valency Tr, mainly
     - opposites: verschwenden
  3. [B2] etwas nicht tun oder sagen, weil es unnötig oder nutzlos ist
     - example: Deine Ausreden kannst du dir sparen, ich glaube dir sowieso nicht.
     - frames: sich etw. sparen; sich etw. sparen können
     - collocations: sich die Mühe sparen; sich den Weg sparen
     - grammar: valency Tr; note with a dative reflexive pronoun, often with können

### 35. Gast

**Lexeme 1: Gast**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈɡast/ (de-DE)
- forms: Gastes [Case=Gen, Number=Sing] /ˈɡas.təs/; Gasts [Case=Gen, Number=Sing] /ˈɡasts/; Gäste [Case=Nom, Number=Plur] /ˈɡɛs.tə/
  1. [A1] Person, die jemand eingeladen hat oder die jemanden besucht
     - example: Am Samstag erwarten wir zwanzig Gäste zu unserer Hochzeit.
     - frames: bei jmdm. zu Gast sein; jmdn. zu Gast haben
     - collocations: zu Gast sein; Gäste einladen; Gäste empfangen; Gäste bewirten; ein gern gesehener Gast; ein ungebetener Gast
  2. [A2] Person, die in einem Lokal oder Hotel isst, trinkt oder übernachtet
     - example: In der Hochsaison ist das Hotel fast nur mit ausländischen Gästen belegt.
     - collocations: Gäste bedienen; ein zahlender Gast; die Gäste des Hotels
  3. [B2] Mannschaft, die nicht im eigenen Stadion oder auf eigenem Platz spielt
     - example: Kurz vor Schluss gelang den Gästen noch der Ausgleich.
     - collocations: die Gäste gehen in Führung
     - grammar: number Plur, mainly

### 36. Leitung

**Lexeme 1: Leitung**, NOUN
- grammar: gender Fem; plural_class -en
- said: /ˈlaɪ.tʊŋ/ (de-DE)
- forms: Leitung [Case=Gen, Number=Sing] /ˈlaɪ.tʊŋ/; Leitungen [Case=Nom, Number=Plur] /ˈlaɪ.tʊ.ŋən/
  1. [B1] Aufgabe und Verantwortung, eine Gruppe, Firma oder Veranstaltung zu führen
     - example: Das Konzert findet unter der Leitung eines jungen Dirigenten statt.
     - frames: unter der Leitung von + Dat; die Leitung + Gen
     - collocations: die Leitung übernehmen; die Leitung haben; die Leitung abgeben; die Leitung innehaben
     - grammar: number Sing; countability Mass
  2. [B2] Gruppe von Personen, die eine Firma oder Einrichtung führt
     - example: Die Leitung der Schule hat neue Regeln für die Pausen beschlossen.
     - collocations: sich an die Leitung wenden
     - grammar: countability Count; number Sing, mainly
  3. [B1] Rohr oder Kabel, durch das Flüssigkeiten, Gase oder Strom fließen
     - example: Im Keller ist eine Leitung geplatzt, und jetzt steht dort Wasser.
     - collocations: eine Leitung verlegen; eine Leitung legen; eine Leitung reparieren; eine Leitung platzt
     - grammar: countability Count
  4. [B1] technische Verbindung, über die man mit jemandem telefoniert
     - example: Bleiben Sie bitte in der Leitung, ich verbinde Sie gleich.
     - collocations: in der Leitung bleiben; die Leitung ist besetzt; die Leitung ist tot; in der Leitung sein
     - grammar: countability Count
- relations: verbal_noun_of leiten (VERB)

### 37. quellen

**Lexeme 1: quellen**, VERB
- grammar: conjugation strong; auxiliary be; valency Intr; irregular True
- said: /ˈkvɛ.lən/ (de-DE)
- forms: quillt [Tense=Pres, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈkvɪlt/; quoll [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈkvɔl/; gequollen [Tense=Past, VerbForm=Part] /ɡə.ˈkvɔ.lən/
  1. [C1] in großer Menge aus einer Öffnung herausfließen oder herausdringen
     - example: Aus dem kaputten Rohr quillt seit Stunden schmutziges Wasser.
     - frames: aus + Dat quellen
     - collocations: Rauch quillt; Wasser quillt aus dem Boden; Tränen quellen aus den Augen
  2. [C1] durch Aufnahme von Wasser weicher und größer werden
     - example: Die getrockneten Bohnen müssen über Nacht im Wasser quellen.
     - collocations: quellen lassen; Reis quillt
- relations: confusable Quelle (NOUN): its plural Quellen differs from the verb only by the capital letter

### 38. Verantwortung

**Lexeme 1: Verantwortung**, NOUN
- grammar: gender Fem; plural_class -en; countability Mass
- said: /fɛɐ.ˈant.vɔʁ.tʊŋ/ (de-DE)
- forms: Verantwortung [Case=Gen, Number=Sing] /fɛɐ.ˈant.vɔʁ.tʊŋ/; Verantwortungen [Case=Nom, Number=Plur] /fɛɐ.ˈant.vɔʁ.tʊ.ŋən/
  1. [B1] Pflicht, dafür zu sorgen, dass mit einer Person oder Sache alles gut geht
     - example: Als Lehrerin trägt sie die Verantwortung für dreißig kleine Kinder.
     - frames: Verantwortung für + Akk; Verantwortung gegenüber + Dat
     - collocations: Verantwortung tragen; Verantwortung übernehmen; große Verantwortung; Verantwortung abgeben; auf eigene Verantwortung; in der Verantwortung stehen
  2. [B2] Pflicht, für die Folgen eines Fehlers oder Schadens einzustehen
     - example: Nach dem Unfall will niemand die Verantwortung für den Schaden übernehmen.
     - frames: jmdn. zur Verantwortung ziehen; die Verantwortung für + Akk
     - collocations: zur Verantwortung ziehen; die Verantwortung übernehmen; die Verantwortung zurückweisen; die Verantwortung abschieben; die volle Verantwortung
- relations: verbal_noun_of verantworten (VERB)

### 39. sicherlich

**Lexeme 1: sicherlich**, ADV
- said: /ˈzɪ.çɐ.lɪç/ (de-DE)
  1. [B1] drückt aus, dass man etwas für sehr wahrscheinlich oder gewiss hält
     - example: Bei diesem Wetter kommt der Bus sicherlich wieder zu spät.
     - collocations: sicherlich nicht; ganz sicherlich
- relations: derived_from sicher (ADJ)

### 40. Anspruch

**Lexeme 1: Anspruch**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈan.ʃpʁʊx/ (de-DE)
- forms: Anspruchs [Case=Gen, Number=Sing] /ˈan.ʃpʁʊxs/; Anspruches [Case=Gen, Number=Sing] /ˈan.ʃpʁʊ.xəs/; Ansprüche [Case=Nom, Number=Plur] /ˈan.ʃpʁʏ.çə/
  1. [B1] Recht, etwas Bestimmtes zu fordern oder zu bekommen
     - example: Nach einem Jahr im Betrieb hast du Anspruch auf vier Wochen Urlaub.
     - frames: Anspruch auf + Akk; Anspruch erheben auf + Akk
     - collocations: Anspruch haben; einen Anspruch geltend machen; Anspruch erheben; einen Anspruch verlieren; einen rechtlichen Anspruch; Hilfe in Anspruch nehmen
  2. [B2] Erwartung, wie gut jemand oder etwas sein soll
     - example: Meine Eltern haben sehr hohe Ansprüche an die Schule ihrer Kinder.
     - frames: Ansprüche an + Akk
     - collocations: hohe Ansprüche stellen; hohe Ansprüche haben; den Ansprüchen genügen; den Ansprüchen gerecht werden; Ansprüche an sich selbst
     - grammar: note often in the plural

### 41. General

**Lexeme 1: General**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count; note plural Generäle or Generale; both are standard
- said: /ɡe.nə.ˈʁaːl/ (de-DE)
- forms: Generals [Case=Gen, Number=Sing] /ɡe.nə.ˈʁaːls/; Generäle [Case=Nom, Number=Plur] /ɡe.nə.ˈʁɛː.lə/; Generale [Case=Nom, Number=Plur] /ɡe.nə.ˈʁaː.lə/
  1. [B1] Offizier mit einem der höchsten Dienstgrade in einer Armee
     - example: Nach dreißig Jahren beim Militär wurde er schließlich zum General befördert.
     - collocations: zum General befördert werden; zum General ernennen; ein pensionierter General; ein hoher General
  2. [C2] oberster Leiter eines katholischen Ordens
     - example: Der neue General des Ordens wird von den Mitgliedern gewählt.
     - labels: domain religion
- relations: confusable generell (ADJ): similar spelling; generell means 'in general', not an officer

### 42. Tour

**Lexeme 1: Tour**, NOUN
- grammar: gender Fem; plural_class -en; countability Count
- said: /ˈtuːɐ/ (de-DE)
- forms: Tour [Case=Gen, Number=Sing] /ˈtuːɐ/; Touren [Case=Nom, Number=Plur] /ˈtuː.ʁən/
  1. [A2] Ausflug oder Reise, bei der man unterwegs ist und mehrere Orte besucht
     - example: Am Wochenende machen wir eine lange Tour mit dem Fahrrad.
     - frames: Tour durch + Akk; Tour nach + Dat
     - collocations: eine Tour machen; eine Tour unternehmen; eine Tour planen; eine geführte Tour
  2. [B1] Reihe von Auftritten eines Künstlers oder einer Gruppe an verschiedenen Orten
     - example: Die Band geht im Frühjahr wieder auf Tour durch Europa.
     - frames: Tour durch + Akk
     - collocations: auf Tour gehen; auf Tour sein; eine Tour absagen
  3. [C1] Art und Weise, mit der jemand etwas zu erreichen versucht
     - example: Mit dieser Tour kommst du bei mir nicht weiter.
     - collocations: auf die sanfte Tour; auf die krumme Tour; auf die Tour; die Tour zieht nicht
     - labels: formality informal
  4. [C1] einzelne Drehung einer Welle oder eines Motors um sich selbst
     - example: Nach einigen Minuten kam der alte Motor endlich auf Touren.
     - collocations: auf Touren kommen; auf vollen Touren laufen; auf Touren bringen
     - grammar: number Plur, mainly; note mostly in phrases with auf Touren, also figuratively

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
  1. [A1] deutlich spürbare Bewegung der Luft im Freien
     - example: Heute weht ein kalter Wind vom Meer her.
     - collocations: der Wind weht; der Wind bläst; starker Wind; eisiger Wind; der Wind dreht sich; der Wind legt sich; gegen den Wind; bei Wind und Wetter

### 45. Forschung

**Lexeme 1: Forschung**, NOUN
- grammar: gender Fem; plural_class -en; countability Mass, mainly
- said: /ˈfɔʁ.ʃʊŋ/ (de-DE)
- forms: Forschung [Case=Gen, Number=Sing] /ˈfɔʁ.ʃʊŋ/; Forschungen [Case=Nom, Number=Plur] /ˈfɔʁ.ʃʊ.ŋən/
  1. [B1] genaue wissenschaftliche Arbeit, mit der man neue Erkenntnisse gewinnen will
     - example: Für ihre Forschung über seltene Pflanzen reist sie oft in die Berge.
     - frames: Forschung über + Akk; Forschung zu + Dat; Forschung an + Dat
     - collocations: Forschung betreiben; Forschung fördern; Forschung und Entwicklung; Forschung und Lehre; medizinische Forschung; Geld für die Forschung
  2. [B2] alle Wissenschaftler und Einrichtungen, die auf einem Gebiet wissenschaftlich arbeiten
     - example: Die Forschung hat inzwischen gezeigt, dass Schlaf sehr wichtig ist.
     - collocations: in der Forschung arbeiten; die moderne Forschung; nach dem heutigen Stand der Forschung
     - grammar: number Sing
- relations: verbal_noun_of forschen (VERB)
