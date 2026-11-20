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
- a grammar value followed by "mainly" holds for most uses, not all ("position Attr, mainly"
  is "usually before a noun"); without it the value is claimed without exception.
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

### 1. Bürgerkrieg

**Lexeme 1: Bürgerkrieg**, NOUN
- grammar: gender Masc; plural_class -e; countability Count
- said: /ˈbʏʁ.ɡɐ.kʁiːk/ (de-DE)
- parts: Bürger (Bürger) + krieg (Krieg)
- forms: Bürgerkriegs [Case=Gen, Number=Sing] /ˈbʏʁ.ɡɐ.kʁiːks/; Bürgerkrieges [Case=Gen, Number=Sing] /ˈbʏʁ.ɡɐ.kʁiː.ɡəs/; Bürgerkriege [Case=Nom, Number=Plur] /ˈbʏʁ.ɡɐ.kʁiː.ɡə/
  1. [B2] bewaffneter Kampf zwischen verschiedenen Gruppen der Bevölkerung eines Landes
     - example: Viele Familien sind vor dem Bürgerkrieg ins Nachbarland geflohen.
     - collocations: ein Bürgerkrieg bricht aus; vor dem Bürgerkrieg fliehen; im Bürgerkrieg; ein blutiger Bürgerkrieg; der Bürgerkrieg tobt; einen Bürgerkrieg beenden

### 2. Dieb

**Lexeme 1: Dieb**, NOUN
- grammar: gender Masc; plural_class -e; countability Count
- said: /ˈdiːp/ (de-DE)
- forms: Diebes [Case=Gen, Number=Sing] /ˈdiː.bəs/; Diebs [Case=Gen, Number=Sing] /ˈdiːps/; Diebe [Case=Nom, Number=Plur] /ˈdiː.bə/
  1. [B1] Person, die anderen heimlich etwas wegnimmt, das ihr nicht gehört
     - example: Gestern Nacht haben Diebe bei unseren Nachbarn zwei Fahrräder gestohlen.
     - collocations: einen Dieb fassen; einen Dieb auf frischer Tat ertappen; Haltet den Dieb!; einen Dieb festnehmen

### 3. Stadt

**Lexeme 1: Stadt**, NOUN
- grammar: gender Fem; plural_class -¨e; countability Count
- said: /ˈʃtat/ (de-DE)
- forms: Stadt [Case=Gen, Number=Sing] /ˈʃtat/; Städte [Case=Nom, Number=Plur] /ˈʃtɛː.tə/
  1. [A1] größerer Ort mit vielen Häusern, Straßen und Einwohnern und eigener Verwaltung
     - example: Wir wohnen seit zehn Jahren in einer kleinen Stadt am Rhein.
     - collocations: in der Stadt wohnen; in die Stadt fahren; in die Stadt gehen; mitten in der Stadt; eine Stadt besichtigen; eine große Stadt; Stadt und Land
  2. [B1] Verwaltung eines größeren Ortes mit ihren Ämtern und Behörden
     - example: Die Stadt will im nächsten Jahr eine neue Schule bauen.
     - collocations: bei der Stadt arbeiten; bei der Stadt angestellt sein
     - grammar: number Sing

### 4. Maul

**Lexeme 1: Maul**, NOUN
- grammar: gender Neut; plural_class -¨er; countability Count
- said: /ˈmaʊl/ (de-DE)
- forms: Maules [Case=Gen, Number=Sing] /ˈmaʊ.ləs/; Mauls [Case=Gen, Number=Sing] /ˈmaʊls/; Mäuler [Case=Nom, Number=Plur] /ˈmɔʏ.lɐ/
  1. [B1] Öffnung am Kopf eines Tieres, durch die es Nahrung aufnimmt
     - example: Der Hund trägt stolz einen alten Schuh im Maul durch den Garten.
     - collocations: im Maul tragen; das Maul öffnen; das Maul aufsperren
  2. [B2] Mund eines Menschen, besonders beim Reden
     - example: Wenn du nichts Nettes sagen kannst, halt einfach dein Maul.
     - collocations: das Maul halten; Halt's Maul; ein großes Maul haben; das Maul aufreißen; sich das Maul zerreißen; jmdm. das Maul stopfen; hungrige Mäuler stopfen
     - labels: formality informal, attitude vulgar

### 5. weder

**Lexeme 1: weder**, CONJ
- grammar: invariable True; note always followed by noch: weder ... noch
- said: /ˈveː.dɐ/ (de-DE)
  1. [B1] verwendet zusammen mit „noch“, um zu sagen, dass beides nicht zutrifft
     - example: Ich habe heute weder Zeit noch Lust auf einen langen Spaziergang.
     - frames: weder etw. noch etw.; weder jmd. noch jmd.
     - collocations: weder ja noch nein; weder hier noch dort; weder Fisch noch Fleisch

### 6. Betrieb

**Lexeme 1: Betrieb**, NOUN
- grammar: gender Masc; plural_class -e
- said: /bə.ˈtʁiːp/ (de-DE)
- forms: Betriebs [Case=Gen, Number=Sing] /bə.ˈtʁiːps/; Betriebes [Case=Gen, Number=Sing] /bə.ˈtʁiː.bəs/; Betriebe [Case=Nom, Number=Plur] /bə.ˈtʁiː.bə/
  1. [A2] Unternehmen, in dem Menschen Waren herstellen oder Dienstleistungen anbieten
     - example: Mein Onkel arbeitet seit zwanzig Jahren im selben Betrieb.
     - collocations: einen Betrieb leiten; einen Betrieb führen; einen Betrieb gründen; im Betrieb arbeiten; ein landwirtschaftlicher Betrieb; ein mittelständischer Betrieb; ein kleiner Betrieb
     - grammar: countability Count
  2. [B1] das Arbeiten oder Laufen einer Maschine, Anlage oder Einrichtung
     - example: Die neue Bahnstrecke wird im nächsten Frühjahr in Betrieb genommen.
     - frames: etw. in Betrieb nehmen; etw. außer Betrieb setzen
     - collocations: in Betrieb sein; außer Betrieb sein; in Betrieb nehmen; außer Betrieb setzen; den Betrieb aufnehmen; den Betrieb einstellen
     - grammar: countability Mass; number Sing
  3. [B2] reges Leben und Kommen und Gehen vieler Menschen an einem Ort
     - example: Vor Weihnachten herrscht in den Geschäften immer viel Betrieb.
     - collocations: viel Betrieb; es herrscht Betrieb; reger Betrieb; wenig Betrieb
     - grammar: countability Mass; number Sing
- relations: derived_from betreiben (VERB)

### 7. schwören

**Lexeme 1: schwören**, VERB
- grammar: irregular True; conjugation strong; auxiliary have; valency Both
- said: /ˈʃvøː.ʁən/ (de-DE)
- forms: schwor [Tense=Past, Person=3, Number=Sing] /ˈʃvoːɐ/; geschworen [VerbForm=Part, Tense=Past] /ɡə.ˈʃvoː.ʁən/
  1. [B1] sehr ernst sagen, dass etwas ganz sicher wahr ist
     - example: Ich schwöre dir, ich habe dein Geld nicht genommen.
     - frames: jmdm. etw. schwören; schwören, dass ...
     - collocations: Stein und Bein schwören; ich könnte schwören; hoch und heilig schwören
  2. [B2] einen Eid leisten; feierlich und offiziell etwas versprechen
     - example: Der Zeuge musste vor Gericht schwören, die Wahrheit zu sagen.
     - frames: etw. schwören; schwören, etw. zu tun; jmdm. etw. schwören
     - collocations: einen Eid schwören; Treue schwören; auf die Bibel schwören; auf die Verfassung schwören; vor Gericht schwören; einen Meineid schwören; Rache schwören
  3. [B2] sich ganz fest vornehmen, etwas zu tun oder nicht zu tun
     - example: Nach dem Unfall hat sie sich geschworen, nie wieder Auto zu fahren.
     - frames: sich etw. schwören; sich schwören, etw. zu tun
     - grammar: reflexive True; note with sich in the dative
  4. [C1] fest glauben, dass etwas Bestimmtes sehr gut hilft oder wirkt
     - example: Meine Oma schwört bei Erkältung auf heißen Tee mit Honig.
     - frames: schwören auf + Akk
     - labels: formality informal
     - grammar: valency Intr

### 8. pädagogik

**Lexeme 1: Pädagogik**, NOUN
- grammar: gender Fem; plural_class -en; countability Mass, mainly; note plural rare
- said: /pɛ.da.ˈɡoː.ɡɪk/ (de-DE)
- forms: Pädagogik [Case=Gen, Number=Sing] /pɛ.da.ˈɡoː.ɡɪk/; Pädagogiken [Case=Nom, Number=Plur] /pɛ.da.ˈɡoː.ɡɪ.kən/
  1. [B2] Wissenschaft, die sich mit Erziehung und Unterricht von Menschen beschäftigt
     - example: Sie studiert Pädagogik, weil sie später Lehrerin werden möchte.
     - collocations: Pädagogik studieren; Professor für Pädagogik; Lehrstuhl für Pädagogik
  2. [C1] bestimmte Art und Weise, wie man Kinder erzieht oder unterrichtet
     - example: Die Schule ist für ihre moderne Pädagogik im ganzen Land bekannt.
     - collocations: moderne Pädagogik; eine neue Pädagogik

### 9. Chemiker

**Lexeme 1: Chemiker**, NOUN
- grammar: gender Masc; plural_class -
- said: /ˈçeː.mi.kɐ/ (de-DE)
- forms: Chemikers [Case=Gen, Number=Sing] /ˈçeː.mi.kɐs/; Chemiker [Case=Nom, Number=Plur] /ˈçeː.mi.kɐ/
  1. [B1] Person, die beruflich Stoffe und ihre Reaktionen wissenschaftlich untersucht
     - example: Mein Bruder arbeitet als Chemiker in einem großen Labor.
     - collocations: als Chemiker arbeiten; studierter Chemiker
- relations: derived_from Chemie (NOUN)

### 10. Flut

**Lexeme 1: Flut**, NOUN
- grammar: gender Fem; plural_class -en
- said: /ˈfluːt/ (de-DE)
- forms: Flut [Case=Gen, Number=Sing] /ˈfluːt/; Fluten [Case=Nom, Number=Plur] /ˈfluː.tən/
  1. [B1] regelmäßiges Steigen des Meeres, bei dem das Wasser zur Küste kommt
     - example: Bei Flut kann man hier nicht mehr am Strand spazieren gehen.
     - collocations: bei Flut; die Flut kommt; Ebbe und Flut; die Flut steigt
     - grammar: number Sing, mainly
     - opposites: Ebbe
  2. [C1] große Menge Wasser, die sich mit viel Kraft bewegt
     - example: Der Schwimmer verschwand plötzlich in den dunklen Fluten des Flusses.
     - collocations: in den Fluten versinken; die tosenden Fluten
     - labels: style elevated
     - grammar: number Plur, mainly
  3. [B2] sehr große Menge von Dingen, die auf einmal kommen
     - example: Nach der Sendung erreichte uns eine Flut von Anrufen.
     - frames: eine Flut von + Dat
     - collocations: eine Flut von Briefen; eine Flut von Informationen; eine Flut von Beschwerden
     - labels: figurative True
     - grammar: countability Count

### 11. anstatt

**Lexeme 1: anstatt**, ADP
- grammar: note usually with the genitive; in speech often with the dative
- said: /an.ˈʃtat/ (de-DE)
  1. [B1] an der Stelle einer anderen Person oder Sache
     - example: Anstatt eines Briefes hat sie mir nur eine kurze Nachricht geschickt.
     - frames: anstatt + Gen; anstatt + Dat
**Lexeme 2: anstatt**, SCONJ
- said: /an.ˈʃtat/ (de-DE)
  1. [B1] verwendet, um zu sagen, dass etwas Erwartetes nicht geschieht, sondern etwas anderes
     - example: Anstatt zu lernen, sitzt mein Bruder den ganzen Tag vor dem Fernseher.
     - frames: anstatt etw. zu tun; anstatt dass ...

### 12. Trennung

**Lexeme 1: Trennung**, NOUN
- grammar: gender Fem; plural_class -en
- said: /ˈtʁɛ.nʊŋ/ (de-DE)
- forms: Trennung [Case=Gen, Number=Sing] /ˈtʁɛ.nʊŋ/; Trennungen [Case=Nom, Number=Plur] /ˈtʁɛ.nʊ.ŋən/
  1. [B1] das Ende einer Liebesbeziehung oder Ehe zwischen zwei Menschen
     - example: Nach der Trennung ist er in eine kleinere Wohnung gezogen.
     - frames: Trennung von + Dat
     - collocations: in Trennung leben; die Trennung verarbeiten; eine schmerzhafte Trennung; nach der Trennung; sich für eine Trennung entscheiden
  2. [B2] der Vorgang, bei dem man Dinge voneinander löst oder auseinanderhält
     - example: Die Trennung von Kirche und Staat ist im Gesetz festgelegt.
     - frames: Trennung von + Dat; Trennung zwischen + Dat
     - collocations: strikte Trennung; räumliche Trennung; Trennung von Kirche und Staat; Trennung von Beruf und Privatleben
  3. [B2] die Zeit, in der man von geliebten Menschen weit weg ist
     - example: Die lange Trennung von seiner Familie fiel ihm sehr schwer.
     - frames: Trennung von + Dat
     - collocations: eine lange Trennung; die Trennung ertragen
- relations: verbal_noun_of trennen (VERB)

### 13. Sessel

**Lexeme 1: Sessel**, NOUN
- grammar: gender Masc; plural_class -; countability Count
- said: /ˈzɛ.səl/ (de-DE)
- forms: Sessels [Case=Gen, Number=Sing] /ˈzɛ.səls/; Sessel [Case=Nom, Number=Plur] /ˈzɛ.səl/
  1. [A2] bequemer, gepolsterter Sitz für eine Person mit Lehnen für die Arme
     - example: Nach der Arbeit setzt sich mein Vater gern in seinen Sessel.
     - collocations: im Sessel sitzen; sich in einen Sessel setzen; sich in den Sessel fallen lassen; im Sessel versinken; ein bequemer Sessel; ein weicher Sessel
  2. [B1] einfaches Möbelstück mit Lehne zum Sitzen für eine Person
     - example: Stell bitte noch einen Sessel an den Küchentisch.
     - collocations: auf dem Sessel sitzen; einen Sessel holen
     - regions: de-AT only

### 14. singen

**Lexeme 1: singen**, VERB
- grammar: irregular True; separable False; auxiliary have; conjugation strong; valency Both
- said: /ˈzɪ.ŋən/ (de-DE)
- forms: sang [Tense=Past, Person=3, Number=Sing, Mood=Ind, VerbForm=Fin] /ˈzaŋ/; gesungen [VerbForm=Part, Tense=Past] /ɡə.ˈzʊ.ŋən/
  1. [A1] mit der Stimme Musik machen, besonders Lieder oder Melodien
     - example: Die Kinder singen jeden Morgen ein Lied in der Schule.
     - frames: etw. singen; jmdm. etw. singen
     - collocations: ein Lied singen; im Chor singen; falsch singen; laut singen; zur Gitarre singen; jmdn. in den Schlaf singen; vom Blatt singen
  2. [C1] bei der Polizei Geheimnisse verraten und andere belasten
     - example: Nach drei Stunden Verhör hat der Komplize endlich gesungen.
     - labels: formality slang
     - grammar: valency Intr
- relations: confusable sinken (VERB): similar sound and forms (sang/sank, gesungen/gesunken), different meaning

### 15. Bundeswehr

**Lexeme 1: Bundeswehr**, NOUN
- grammar: gender Fem; number Sing; countability Mass
- said: /ˈbʊn.dəs.ˌveːɐ/ (de-DE)
- parts: Bundes (Bund) + wehr (Wehr)
- forms: Bundeswehr [Case=Gen, Number=Sing] /ˈbʊn.dəs.ˌveːɐ/
  1. [B1] die Streitkräfte Deutschlands, gegründet im Jahr 1955
     - example: Nach der Schule ging mein Bruder für zwei Jahre zur Bundeswehr.
     - collocations: zur Bundeswehr gehen; bei der Bundeswehr sein; bei der Bundeswehr dienen; Soldat der Bundeswehr; Einsatz der Bundeswehr

### 16. Doku

**Lexeme 1: Doku**, NOUN
- grammar: gender Fem; plural_class -s
- said: /ˈdoː.ku/ (de-DE)
- forms: Doku [Case=Gen, Number=Sing] /ˈdoː.ku/; Dokus [Case=Nom, Number=Plur] /ˈdoː.kus/
  1. [B1] Film oder Sendung, die über wirkliche Ereignisse, Menschen oder Themen berichtet
     - example: Gestern Abend habe ich eine spannende Doku über Wale gesehen.
     - frames: Doku über + Akk
     - collocations: eine Doku sehen; eine Doku drehen; eine Doku zeigen
     - labels: formality informal
     - grammar: countability Count
  2. [B2] Texte, die erklären, wie ein Programm oder Gerät aufgebaut ist und benutzt wird
     - example: Bevor du fragst, lies bitte zuerst die Doku.
     - collocations: die Doku lesen; in der Doku nachschauen
     - labels: formality informal
     - grammar: countability Mass, mainly
- relations: derived_from Dokumentation (NOUN)

### 17. Teilnahme

**Lexeme 1: Teilnahme**, NOUN
- grammar: gender Fem; plural_class -n; countability Mass, mainly
- said: /ˈtaɪl.naː.mə/ (de-DE)
- forms: Teilnahme [Case=Gen, Number=Sing] /ˈtaɪl.naː.mə/; Teilnahmen [Case=Nom, Number=Plur] /ˈtaɪl.naː.mən/
  1. [B1] das Mitmachen bei einer Aktivität oder Veranstaltung zusammen mit anderen
     - example: Die Teilnahme am Kurs ist für alle Mitglieder kostenlos.
     - frames: Teilnahme an + Dat
     - collocations: die Teilnahme ist freiwillig; die Teilnahme ist kostenlos; seine Teilnahme bestätigen; seine Teilnahme absagen; regelmäßige Teilnahme; aktive Teilnahme
  2. [C1] Mitgefühl mit einem Menschen, der Leid oder einen Verlust erlebt hat
     - example: Wir möchten Ihnen zum Tod Ihres Vaters unsere herzliche Teilnahme aussprechen.
     - collocations: seine Teilnahme aussprechen; herzliche Teilnahme
     - labels: style elevated
     - grammar: countability Mass
- relations: verbal_noun_of teilnehmen (VERB)

### 18. Kompetenz

**Lexeme 1: Kompetenz**, NOUN
- grammar: gender Fem; plural_class -en
- said: /kɔm.pe.ˈtɛnts/ (de-DE)
- forms: Kompetenz [Case=Gen, Number=Sing] /kɔm.pe.ˈtɛnts/; Kompetenzen [Case=Nom, Number=Plur] /kɔm.pe.ˈtɛn.tsən/
  1. [B2] Wissen und Fähigkeiten, die man braucht, um etwas gut zu tun
     - example: Ihre fachliche Kompetenz hat das ganze Team sehr beeindruckt.
     - frames: Kompetenz in + Dat
     - collocations: fachliche Kompetenz; soziale Kompetenz; Kompetenz beweisen; Kompetenzen erwerben; Kompetenz besitzen; interkulturelle Kompetenz
     - opposites: Inkompetenz
  2. [C1] das Recht und die Pflicht, über bestimmte Dinge zu entscheiden
     - example: Diese Frage liegt nicht in der Kompetenz der Stadtverwaltung.
     - frames: Kompetenz für + Akk
     - collocations: in jmds. Kompetenz fallen; seine Kompetenzen überschreiten; Kompetenzen übertragen; Kompetenzen abgeben
     - labels: formality formal

### 19. hinter

**Lexeme 1: hinter**, ADP
- grammar: note with the dative for a place, with the accusative for a direction
- said: /ˈhɪn.tɐ/ (de-DE)
  1. [A1] bezeichnet eine Lage oder Richtung auf der Rückseite von etwas
     - example: Unser Fahrrad steht meistens hinter dem Haus neben der Garage.
     - frames: hinter + Dat; hinter + Akk
     - collocations: hinter verschlossenen Türen; die Tür hinter sich schließen; hinter jmds. Rücken; hinter der Tür
     - opposites: vor
  2. [B1] bezeichnet einen späteren Platz in einer Reihenfolge oder Rangfolge
     - example: Unsere Mannschaft liegt nur zwei Punkte hinter dem Tabellenführer.
     - frames: hinter + Dat
     - collocations: hinter den Erwartungen zurückbleiben; hinter dem Zeitplan liegen
     - opposites: vor
  3. [B1] bezeichnet etwas, das zeitlich schon vorbei und überstanden ist
     - example: Zum Glück habe ich die schwierigste Prüfung jetzt hinter mir.
     - frames: etw. hinter sich haben; etw. hinter sich bringen
     - collocations: das Schlimmste hinter sich haben
     - opposites: vor
  4. [B2] bezeichnet jemanden oder etwas als verborgenen Grund oder Urheber
     - example: Die Polizei vermutet, dass eine Bande hinter den Einbrüchen steckt.
     - frames: jmd. steckt hinter + Dat

### 20. grüßen

**Lexeme 1: grüßen**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False
- said: /ˈɡʁyː.sən/ (de-DE)
- spellings: grüßen (de-DE, de-AT); grüssen (de-CH)
- forms: grüßte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈɡʁyːs.tə/; gegrüßt [Tense=Past, VerbForm=Part] /ɡə.ˈɡʁyːst/
  1. [A2] bei einer Begegnung jemandem mit Worten oder einer Geste Höflichkeit zeigen
     - example: Unser Nachbar grüßt uns jeden Morgen freundlich über den Zaun.
     - collocations: freundlich grüßen; höflich grüßen; Grüß Gott; Grüß dich
  2. [B1] jemandem durch eine andere Person oder einen Brief freundliche Wünsche senden
     - example: Grüß bitte deine Eltern ganz herzlich von mir!
     - frames: jmdn. grüßen von + Dat; jmdn. grüßen lassen
     - collocations: herzlich grüßen; schön grüßen lassen

### 21. verlegen

**Lexeme 1: verlegen**, ADJ
- grammar: comparable True
- said: /fɛɐ.ˈleː.ɡən/ (de-DE)
  1. [B1] unsicher und etwas beschämt in einer unangenehmen sozialen Situation
     - example: Nach dem unerwarteten Lob lächelte sie verlegen.
     - collocations: verlegen lächeln; verlegen werden; verlegen schweigen; verlegen zu Boden blicken
  2. [C1] ohne etwas, das man in einer bestimmten Situation braucht
     - example: Er ist um eine Ausrede nie verlegen.
     - frames: um etw. verlegen sein
     - collocations: um keine Antwort verlegen sein; um eine Ausrede nie verlegen sein
     - grammar: position Pred; note usually negated
**Lexeme 2: verlegen**, VERB
- grammar: separable False; conjugation weak; auxiliary have
- said: /fɛɐ.ˈleː.ɡən/ (de-DE)
- forms: verlegte [VerbForm=Fin, Mood=Ind, Tense=Past, Person=3, Number=Sing] /fɛɐ.ˈleːk.tə/; verlegt [VerbForm=Part, Tense=Past] /fɛɐ.ˈleːkt/
  1. [B1] den Zeitpunkt einer geplanten Sache auf einen anderen Termin verschieben
     - example: Wir müssen die Besprechung auf nächsten Montag verlegen.
     - frames: etw. auf + Akk verlegen
     - collocations: einen Termin verlegen; ein Spiel verlegen; eine Sitzung verlegen
     - grammar: valency Tr
  2. [B1] etwas oder jemanden an einen anderen Ort bringen
     - example: Die Firma will ihren Sitz ins Ausland verlegen.
     - frames: etw. nach + Dat verlegen; jmdn. in + Akk verlegen; etw. in + Akk verlegen
     - collocations: den Wohnsitz verlegen; den Firmensitz verlegen; einen Patienten verlegen; Truppen verlegen
     - grammar: valency Tr
  3. [B1] etwas irgendwo ablegen und später nicht mehr finden können
     - example: Ich habe meine Brille schon wieder verlegt.
     - collocations: den Schlüssel verlegen; die Brille verlegen
     - grammar: valency Tr
  4. [B2] Leitungen oder Bodenmaterial an ihren vorgesehenen Stellen fest anbringen
     - example: Die Arbeiter verlegen neue Rohre unter der Straße.
     - collocations: Kabel verlegen; Rohre verlegen; Leitungen verlegen; Fliesen verlegen; Parkett verlegen; Teppichboden verlegen
     - grammar: valency Tr
  5. [B2] Bücher oder andere Texte für den Verkauf herstellen und herausgeben
     - example: Das Unternehmen verlegt vor allem Bücher für Kinder.
     - collocations: Bücher verlegen; Zeitschriften verlegen
     - grammar: valency Tr
  6. [C1] anfangen, ein anderes Mittel oder eine andere Methode zu nutzen
     - example: Als Drohungen nichts halfen, verlegte er sich aufs Bitten.
     - frames: sich auf + Akk verlegen
     - grammar: valency Refl; note the reflexive pronoun has no meaning of its own
- relations: derived_from legen (VERB)

### 22. Realität

**Lexeme 1: Realität**, NOUN
- grammar: gender Fem; plural_class -en
- said: /ʁe.a.li.ˈtɛːt/ (de-DE)
- forms: Realität [Case=Gen, Number=Sing] /ʁe.a.li.ˈtɛːt/; Realitäten [Case=Nom, Number=Plur] /ʁe.a.li.ˈtɛː.tən/
  1. [B1] die Welt und die Dinge, so wie sie wirklich sind
     - example: Ihre Träume hatten wenig mit der Realität zu tun.
     - collocations: in der Realität; der Realität ins Auge sehen; die Realität verdrängen; Realität werden; an der Realität vorbei; die harte Realität; virtuelle Realität; die Realität sieht anders aus
     - grammar: countability Mass
     - opposites: Fiktion
  2. [C1] eine Tatsache oder gegebene Lage, mit der man rechnen muss
     - example: Wir müssen uns endlich den politischen Realitäten stellen.
     - collocations: sich den Realitäten stellen; die politischen Realitäten; die wirtschaftlichen Realitäten
     - grammar: countability Count; number Plur, mainly
  3. [C2] Grundstücke und Häuser, die man kaufen oder verkaufen kann
     - example: Das kleine Büro vermittelt Realitäten in der ganzen Region.
     - collocations: Realitäten vermitteln
     - regions: de-AT only
     - grammar: countability Count; number Plur

### 23. zwischen

**Lexeme 1: zwischen**, ADP
- grammar: note with the dative for a place, with the accusative for a direction
- said: /ˈtsvɪ.ʃən/ (de-DE)
  1. [A1] in der Mitte von zwei oder mehr Personen, Dingen oder Orten
     - example: Die Apotheke liegt genau zwischen der Bank und dem Supermarkt.
     - frames: zwischen + Dat; zwischen + Akk
     - collocations: zwischen den Zeilen lesen; mitten zwischen
  2. [A2] nach einem bestimmten Zeitpunkt und vor einem anderen
     - example: Zwischen Weihnachten und Neujahr ist unser Büro geschlossen.
     - frames: zwischen + Dat
     - collocations: zwischen den Jahren
  3. [B1] drückt eine Beziehung, einen Unterschied oder eine Wahl unter Personen oder Dingen aus
     - example: Du musst dich zwischen den beiden Angeboten entscheiden.
     - frames: zwischen + Dat
     - collocations: der Unterschied zwischen; die Wahl zwischen; wählen zwischen; unterscheiden zwischen; zwischen uns
  4. [A2] irgendwo im Bereich von einer Zahl oder Menge bis zu einer anderen
     - example: Zu dem Fest kommen wahrscheinlich zwischen zwanzig und dreißig Gäste.
     - grammar: note usually before numbers joined by und

### 24. aha

**Lexeme 1: aha**, INTJ
- said: /a.ˈhaː/ (de-DE), /a.ˈha/ (de-DE)
  1. [A2] verwendet, um zu zeigen, dass man etwas plötzlich verstanden hat
     - example: Aha, jetzt verstehe ich endlich, wie die Maschine funktioniert.
  2. [B1] verwendet, um zu zeigen, dass sich eine Vermutung gerade bestätigt hat
     - example: Aha, ich wusste doch, dass du die Schokolade genommen hast!

### 25. Hauptbahnhof

**Lexeme 1: Hauptbahnhof**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈhaʊpt.baːn.hoːf/ (de-DE)
- parts: Haupt (Haupt) + bahnhof (Bahnhof)
- forms: Hauptbahnhofs [Case=Gen, Number=Sing] /ˈhaʊpt.baːn.hoːfs/; Hauptbahnhofes [Case=Gen, Number=Sing] /ˈhaʊpt.baːn.hoː.fəs/; Hauptbahnhöfe [Case=Nom, Number=Plur] /ˈhaʊpt.baːn.høː.fə/
  1. [A2] größte und wichtigste Station für Züge in einer Stadt
     - example: Vom Hauptbahnhof fährt alle zehn Minuten ein Bus ins Zentrum.
     - collocations: am Hauptbahnhof umsteigen; am Hauptbahnhof ankommen; zum Hauptbahnhof fahren; vor dem Hauptbahnhof

### 26. Gleis

**Lexeme 1: Gleis**, NOUN
- grammar: gender Neut; plural_class -e; countability Count
- said: /ˈɡlaɪs/ (de-DE)
- forms: Gleises [Case=Gen, Number=Sing] /ˈɡlaɪ.zəs/; Gleise [Case=Nom, Number=Plur] /ˈɡlaɪ.zə/
  1. [A2] zwei parallele Schienen, auf denen Züge oder Straßenbahnen fahren
     - example: Unser Zug nach Hamburg fährt heute von Gleis sieben ab.
     - collocations: auf Gleis drei; die Gleise überqueren; Gleise verlegen; ein totes Gleis; aus dem Gleis springen

### 27. Spaß

**Lexeme 1: Spaß**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Both
- said: /ˈʃpaːs/ (de-DE)
- spellings: Spaß (de-DE, de-AT); Spass (de-CH)
- forms: Spaßes [Case=Gen, Number=Sing] /ˈʃpaː.səs/; Späße [Case=Nom, Number=Plur] /ˈʃpɛː.sə/
  1. [A1] Gefühl der Freude bei einer Tätigkeit, die man gern macht
     - example: Das neue Spiel macht uns allen wirklich großen Spaß.
     - frames: jmdm. Spaß machen; Spaß haben an + Dat; es macht jmdm. Spaß, etw. zu tun
     - collocations: Spaß machen; Spaß haben; viel Spaß; großen Spaß; Viel Spaß!; zum Spaß; den Spaß verderben; der Spaß an der Arbeit
     - grammar: countability Mass; number Sing
  2. [A2] Äußerung oder Handlung, die nicht ernst gemeint ist und andere lachen lassen soll
     - example: Sei bitte nicht böse, das war doch nur Spaß.
     - frames: Spaß machen mit + Dat
     - collocations: aus Spaß; nur Spaß; Spaß beiseite; keinen Spaß verstehen; Späße machen; Spaß machen
     - grammar: countability Both
     - opposites: Ernst

### 28. Foto

**Lexeme 1: Foto**, NOUN
- grammar: gender Neut; plural_class -s; countability Count; note in Switzerland also feminine: die Foto
- said: /ˈfoː.to/ (de-DE)
- spellings: Foto; Photo [older books only]
- forms: Fotos [Case=Gen, Number=Sing] /ˈfoː.tos/; Fotos [Case=Nom, Number=Plur] /ˈfoː.tos/
  1. [A1] Bild, das man mit einer Kamera oder einem Handy aufnimmt
     - example: Kannst du bitte ein Foto von uns vor dem Brunnen machen?
     - frames: Foto von + Dat
     - collocations: ein Foto machen; Fotos schießen; auf dem Foto; ein Foto aufnehmen; ein Foto hochladen; ein Foto posten; ein unscharfes Foto
- relations: derived_from Fotografie (NOUN)
**Lexeme 2: Foto**, NOUN
- grammar: gender Masc; plural_class -s; countability Count
- said: /ˈfoː.to/ (de-DE)
- forms: Fotos [Case=Gen, Number=Sing] /ˈfoː.tos/; Fotos [Case=Nom, Number=Plur] /ˈfoː.tos/
  1. [C1] Gerät, mit dem man Bilder aufnimmt
     - example: Mein Großvater hat seinen alten Foto immer noch im Schrank.
     - labels: formality informal, time dated, rare True
- relations: derived_from Fotoapparat (NOUN)

### 29. Schande

**Lexeme 1: Schande**, NOUN
- grammar: gender Fem; number Sing
- said: /ˈʃan.də/ (de-DE)
- forms: Schande [Case=Gen, Number=Sing] /ˈʃan.də/
  1. [B2] großer Verlust an Ehre und Ansehen bei anderen Menschen
     - example: Mit seinem Verhalten hat er Schande über die ganze Familie gebracht.
     - frames: Schande über jmdn. bringen; jmdm. Schande machen
     - collocations: Schande bringen; Schande machen; zu meiner Schande; in Schande leben; Schmach und Schande; Schande über dich
     - grammar: countability Mass
     - opposites: Ehre
  2. [B1] etwas, worüber man sich empört, weil es sehr schlimm oder unanständig ist
     - example: Es ist doch eine Schande, wie viel Essen hier weggeworfen wird.
     - frames: es ist eine Schande, dass ...; es ist eine Schande, wie ...; etw. ist eine Schande für + Akk
     - collocations: eine wahre Schande; eine echte Schande; eine Schande sein
     - grammar: countability Count; note usually in "es ist eine Schande"

### 30. knoten

**Lexeme 1: Knoten**, NOUN
- grammar: gender Masc; plural_class -; countability Count
- said: /ˈknoː.tən/ (de-DE)
- forms: Knotens [Case=Gen, Number=Sing] /ˈknoː.təns/; Knoten [Case=Nom, Number=Plur] /ˈknoː.tən/
  1. [B1] feste Stelle, an der eine Schnur oder ein Faden verschlungen ist
     - example: Mit einem doppelten Knoten hält das Seil viel besser.
     - frames: einen Knoten in etw. machen
     - collocations: einen Knoten machen; einen Knoten binden; einen Knoten lösen; einen Knoten aufmachen; ein fester Knoten; der Knoten ist geplatzt
  2. [B2] harte, rundliche Verdickung im Gewebe des Körpers
     - example: Beim Abtasten hat die Ärztin einen kleinen Knoten gefunden.
     - collocations: einen Knoten ertasten; ein Knoten in der Brust
  3. [B2] Frisur, bei der langes Haar am Kopf zusammengedreht und festgesteckt wird
     - example: Für die Arbeit bindet sie ihre langen Haare zu einem Knoten.
     - collocations: die Haare zu einem Knoten binden
  4. [C1] Maßeinheit für die Geschwindigkeit von Schiffen, eine Seemeile pro Stunde
     - example: Bei ruhiger See fährt das Schiff mit etwa zwölf Knoten.
     - labels: domain nautical
**Lexeme 2: knoten**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False
- said: /ˈknoː.tən/ (de-DE)
- forms: knotete [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈknoː.tə.tə/; geknotet [Tense=Past, VerbForm=Part] /ɡə.ˈknoː.tət/
  1. [B2] die Enden von Schnüren oder Tüchern fest ineinander schlingen und so befestigen
     - example: Knote dir die Schnürsenkel doppelt, sonst gehen sie wieder auf.
     - frames: etw. knoten; etw. um + Akk knoten; etw. an + Akk knoten
     - collocations: die Schnürsenkel knoten; die Krawatte knoten; ein Tuch um den Hals knoten
- relations: derived_from Knoten (NOUN)

### 31. Buße

**Lexeme 1: Buße**, NOUN
- grammar: gender Fem; plural_class -n
- said: /ˈbuː.sə/ (de-DE)
- spellings: Buße (de-DE, de-AT); Busse (de-CH)
- forms: Buße [Case=Gen, Number=Sing] /ˈbuː.sə/; Bußen [Case=Nom, Number=Plur] /ˈbuː.sən/
  1. [C1] Reue über eine Sünde und Handlungen, mit denen man sie wiedergutmachen will
     - example: Nach dem Streit wollte er für sein Verhalten Buße tun.
     - frames: Buße tun für + Akk
     - collocations: Buße tun; Buße leisten; zur Buße; Reue und Buße
     - labels: domain religion
     - grammar: countability Mass
  2. [B2] Geldbetrag, den man als Strafe für ein kleineres Vergehen zahlen muss
     - example: Wer hier falsch parkt, muss eine Buße von hundert Franken zahlen.
     - collocations: eine Buße zahlen; eine Buße bekommen; mit einer Buße belegen
     - regions: de-CH mainly
     - grammar: countability Count

### 32. auf

**Lexeme 1: auf**, ADP
- grammar: note with the dative for a place, with the accusative for a direction or goal
- said: /ˈaʊf/ (de-DE)
  1. [A1] gibt an, dass sich etwas oben an der Fläche von etwas befindet
     - example: Deine Schlüssel liegen doch dort drüben auf dem Küchentisch.
     - frames: auf + Dat
     - collocations: auf dem Tisch; auf dem Boden; auf der Straße; auf dem Sofa
     - grammar: note with the dative
  2. [A1] gibt die Richtung einer Bewegung nach oben an eine Fläche an
     - example: Leg die Bücher bitte einfach auf den Schreibtisch.
     - frames: auf + Akk
     - collocations: auf den Tisch legen; auf einen Berg steigen; auf die Straße gehen
     - grammar: note with the accusative
  3. [A2] gibt an, dass jemand bei einer Veranstaltung oder in einer Einrichtung ist oder dorthin geht
     - example: Am Samstag gehen wir zusammen auf eine Hochzeit.
     - frames: auf + Dat; auf + Akk
     - collocations: auf der Post; auf einer Party; auf dem Land; auf die Toilette gehen; auf dem Markt
     - grammar: note dative for where someone is, accusative for where someone goes
  4. [A1] gibt die Art und Weise oder die Sprache an, in der etwas geschieht
     - example: Kannst du mir das bitte noch einmal auf Deutsch erklären?
     - frames: auf + Akk
     - collocations: auf Deutsch; auf diese Weise; auf jeden Fall; auf keinen Fall
  5. [B1] gibt einen Zeitpunkt oder eine Zeitdauer an
     - example: Der Termin beim Arzt wurde auf nächsten Montag verschoben.
     - frames: auf + Akk
     - collocations: auf Dauer; von heute auf morgen; auf Wiedersehen; auf bald
  6. [B2] gibt den Grund oder Anlass an, aus dem etwas geschieht
     - example: Auf Wunsch schicken wir Ihnen gern weitere Informationen.
     - frames: auf + Akk
     - collocations: auf Wunsch; auf Anfrage; auf Einladung; auf Befehl
  7. [A2] verwendet, um bestimmte Verben, Adjektive und Nomen mit ihrer Ergänzung zu verbinden
     - example: Wir freuen uns schon sehr auf die Ferien am Meer.
     - frames: auf + Akk; auf + Dat
     - collocations: warten auf; sich freuen auf; stolz auf; Lust auf; achten auf; bestehen auf
**Lexeme 2: auf**, ADV
- said: /ˈaʊf/ (de-DE)
  1. [A2] geöffnet, nicht geschlossen
     - example: Die Haustür war schon auf, als wir nach Hause kamen.
     - frames: etw. ist auf
     - labels: formality informal
     - grammar: position Pred
     - opposites: zu
  2. [B1] nicht mehr im Bett, wach
     - example: Um sechs Uhr morgens bin ich meistens schon auf.
     - frames: jmd. ist auf
     - collocations: früh auf sein; lange auf sein
     - labels: formality informal
     - grammar: position Pred
  3. [B2] verwendet, um jemanden anzutreiben, sofort loszugehen oder zu beginnen
     - example: Auf, Leute, wir müssen los, der Zug wartet nicht!
     - collocations: auf geht's

### 33. Skala

**Lexeme 1: Skala**, NOUN
- grammar: gender Fem; plural_class -en; countability Count; note plural Skalen replaces the final -a; Skalas also occurs
- said: /ˈskaː.la/ (de-DE)
- forms: Skala [Case=Gen, Number=Sing] /ˈskaː.la/; Skalen [Case=Nom, Number=Plur] /ˈskaː.lən/; Skalas [Case=Nom, Number=Plur] /ˈskaː.las/ (less common plural)
  1. [B2] geordnete Reihe von Stufen oder Werten, nach der man etwas einordnet oder bewertet
     - example: Bewerten Sie unseren Service bitte auf einer Skala von eins bis zehn.
     - frames: auf einer Skala von + Dat bis + Akk
     - collocations: auf einer Skala von eins bis zehn; am oberen Ende der Skala; am unteren Ende der Skala; die ganze Skala
  2. [B2] Reihe von Strichen und Zahlen an einem Messgerät, an der man Werte abliest
     - example: Lies den Wert bitte genau auf der Skala des Thermometers ab.
     - collocations: auf der Skala ablesen
  3. [C1] Folge von Tönen, die in einer festen Ordnung stufenweise steigen oder fallen
     - example: Zum Aufwärmen spielt die Pianistin jeden Morgen mehrere Skalen.
     - labels: domain music

### 34. dass

**Lexeme 1: dass**, SCONJ
- said: /ˈdas/ (de-DE)
- spellings: dass; daß [older books only]
  1. [A2] leitet einen Nebensatz ein, der eine Aussage, einen Gedanken oder eine Tatsache enthält
     - example: Ich hoffe sehr, dass wir uns bald wiedersehen.
     - frames: etw. sagen, dass ...; es ist etw., dass ...; dass ..., ist etw.
     - collocations: ohne dass; anstatt dass; außer dass; nur dass; dafür, dass; darauf, dass
  2. [B1] leitet einen Nebensatz ein, der die Folge des Geschehens im Hauptsatz nennt
     - example: Draußen war es so kalt, dass der See zufror.
     - frames: so ..., dass ...; zu ..., als dass ...
     - collocations: so dass; derart, dass
  3. [B2] leitet einen allein stehenden Satz ein, der einen Wunsch, eine Warnung oder Ärger ausdrückt
     - example: Dass du mir ja pünktlich nach Hause kommst!
     - labels: formality informal
     - grammar: note in a main clause on its own, with the verb at the end
- relations: confusable das (DET): sounds the same as the article and relative pronoun das, which is spelled with one s

### 35. firmen

**Lexeme 1: firmen**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False
- said: /ˈfɪʁ.mən/ (de-DE)
- forms: firmte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈfɪʁm.tə/; gefirmt [Tense=Past, VerbForm=Part] /ɡə.ˈfɪʁmt/
  1. [C1] einem Getauften in der katholischen Kirche durch Salbung und Gebet den Heiligen Geist spenden
     - example: Im Mai wird unsere Tochter vom Bischof in der Kirche gefirmt.
     - frames: jmdn. firmen
     - collocations: gefirmt werden
     - labels: domain religion
- relations: confusable Firma (NOUN): its plural Firmen 'companies' differs from the verb only in the capital letter

### 36. draußen

Not written as an entry: spelling of draußen (ADV), already covered; no other lexeme has this spelling

### 37. Flughafen

**Lexeme 1: Flughafen**, NOUN
- grammar: gender Masc; plural_class -¨; countability Count
- said: /ˈfluːk.haː.fən/ (de-DE)
- parts: Flug (Flug) + hafen (Hafen)
- forms: Flughafens [Case=Gen, Number=Sing] /ˈfluːk.haː.fəns/; Flughäfen [Case=Nom, Number=Plur] /ˈfluːk.hɛː.fən/
  1. [A1] großes Gelände mit Pisten und Gebäuden, wo Flugzeuge starten und landen
     - example: Wir müssen spätestens zwei Stunden vor dem Abflug am Flughafen sein.
     - collocations: am Flughafen; zum Flughafen fahren; vom Flughafen abholen; internationaler Flughafen; auf dem Flughafen landen

### 38. Landtag

**Lexeme 1: Landtag**, NOUN
- grammar: gender Masc; plural_class -e; countability Count
- said: /ˈlant.taːk/ (de-DE)
- parts: Land (Land) + tag (Tag)
- forms: Landtags [Case=Gen, Number=Sing] /ˈlant.taːks/; Landtage [Case=Nom, Number=Plur] /ˈlant.taː.ɡə/
  1. [B2] gewählte Volksvertretung eines Teilstaats, die dort die Gesetze beschließt
     - example: Nach der Wahl zieht die neue Partei zum ersten Mal in den Landtag ein.
     - collocations: in den Landtag einziehen; in den Landtag gewählt werden; im Landtag sitzen; den Landtag wählen; den Landtag auflösen; der Landtag beschließt
  2. [C1] Versammlung der Stände eines Fürstentums in früheren Jahrhunderten
     - example: Früher rief der Fürst den Landtag zusammen, wenn er neue Steuern brauchte.
     - collocations: den Landtag einberufen
     - labels: time historical

### 39. kalt

**Lexeme 1: kalt**, ADJ
- said: /ˈkalt/ (de-DE)
- forms: kälter [Degree=Cmp] /ˈkɛl.tɐ/; am kältesten [Degree=Sup] /ˈam ˈkɛl.təs.tən/
  1. [A1] mit einer niedrigen Temperatur, nicht warm oder heiß
     - example: Im Winter ist das Wasser im See sehr kalt.
     - frames: jmdm. ist kalt
     - collocations: mir ist kalt; kalt werden; kaltes Wasser; kaltes Wetter; kalte Hände; kalt duschen; kalt servieren; kalte Getränke
     - opposites: warm
  2. [B1] ohne Gefühl und Freundlichkeit gegenüber anderen Menschen
     - example: Sie sah mich mit einem kalten Blick an.
     - collocations: kalt lassen; ein kalter Blick; kalt lächeln; kalt und abweisend
     - labels: figurative True
     - opposites: herzlich
  3. [B2] ohne die Kosten für Heizung und warmes Wasser gerechnet
     - example: Die Wohnung kostet achthundert Euro kalt im Monat.
     - collocations: kalt kosten
     - grammar: note only of rent, usually after the amount
     - opposites: warm

### 40. Jacke

**Lexeme 1: Jacke**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /ˈja.kə/ (de-DE)
- forms: Jacke [Case=Gen, Number=Sing] /ˈja.kə/; Jacken [Case=Nom, Number=Plur] /ˈja.kən/
  1. [A1] Kleidungsstück mit Ärmeln für den Oberkörper, das man vorn öffnen kann
     - example: Zieh dir lieber eine warme Jacke an, draußen ist es kalt.
     - collocations: eine Jacke anziehen; die Jacke ausziehen; die Jacke zumachen; die Jacke aufhängen; eine warme Jacke; Das ist Jacke wie Hose

### 41. Tierschutz

**Lexeme 1: Tierschutz**, NOUN
- grammar: gender Masc; number Sing; countability Mass
- said: /ˈtiːɐ.ˌʃʊts/ (de-DE)
- parts: Tier (Tier) + schutz (Schutz)
- forms: Tierschutzes [Case=Gen, Number=Sing] /ˈtiːɐ.ˌʃʊ.tsəs/
  1. [B1] Bemühungen und Gesetze gegen Quälerei und schlechte Behandlung von Lebewesen, die keine Menschen sind
     - example: Viele junge Leute engagieren sich heute aktiv für den Tierschutz.
     - collocations: sich für den Tierschutz einsetzen; sich für den Tierschutz engagieren; aktiver Tierschutz; ein Verstoß gegen den Tierschutz; Tier- und Umweltschutz

### 42. Kompromiss

**Lexeme 1: Kompromiss**, NOUN
- grammar: gender Masc; plural_class -e; countability Count
- said: /kɔm.pʁo.ˈmɪs/ (de-DE)
- spellings: Kompromiss (de-DE, de-AT, de-CH); Kompromiß [older books only]
- forms: Kompromisses [Case=Gen, Number=Sing] /kɔm.pʁo.ˈmɪ.səs/; Kompromisse [Case=Nom, Number=Plur] /kɔm.pʁo.ˈmɪ.sə/
  1. [B1] Einigung, bei der jede Seite auf einen Teil ihrer Forderungen verzichtet
     - example: Nach stundenlanger Diskussion haben beide Parteien endlich einen Kompromiss gefunden.
     - frames: Kompromiss zwischen + Dat; Kompromiss mit + Dat
     - collocations: einen Kompromiss eingehen; einen Kompromiss schließen; sich auf einen Kompromiss einigen; einen Kompromiss finden; Kompromisse machen; zu einem Kompromiss bereit sein; ein fauler Kompromiss; einen Kompromiss aushandeln

### 43. Einsicht

**Lexeme 1: Einsicht**, NOUN
- grammar: gender Fem; plural_class -en
- said: /ˈaɪn.zɪçt/ (de-DE)
- forms: Einsicht [Case=Gen, Number=Sing] /ˈaɪn.zɪçt/; Einsichten [Case=Nom, Number=Plur] /ˈaɪn.zɪç.tən/
  1. [B2] Erkenntnis, durch die man einen Zusammenhang oder eine Sache besser versteht
     - example: Aus diesem langen Gespräch habe ich viele neue Einsichten gewonnen.
     - frames: Einsicht in + Akk; die Einsicht, dass ...
     - collocations: neue Einsichten gewinnen; zu der Einsicht gelangen; eine wichtige Einsicht; tiefe Einsichten
     - grammar: countability Count
  2. [B2] Bereitschaft, einen eigenen Fehler oder eine Notwendigkeit zu erkennen und anzunehmen
     - example: Endlich zeigt er Einsicht und entschuldigt sich bei den Nachbarn.
     - collocations: Einsicht zeigen; zur Einsicht kommen; an die Einsicht appellieren; keine Einsicht haben
     - grammar: countability Mass; number Sing
  3. [C1] Möglichkeit, Akten oder Unterlagen anzusehen und zu prüfen
     - example: Als Patient haben Sie das Recht auf Einsicht in Ihre Krankenakte.
     - frames: Einsicht in + Akk; Einsicht nehmen in + Akk
     - collocations: Einsicht nehmen; Einsicht gewähren; Einsicht verlangen; Einsicht beantragen
     - labels: style official
     - grammar: countability Mass; number Sing
- relations: derived_from einsehen (VERB)

### 44. Laut

**Lexeme 1: Laut**, NOUN
- grammar: gender Masc; plural_class -e; countability Count
- said: /ˈlaʊt/ (de-DE)
- forms: Lautes [Case=Gen, Number=Sing] /ˈlaʊ.təs/; Lauts [Case=Gen, Number=Sing] /ˈlaʊts/; Laute [Case=Nom, Number=Plur] /ˈlaʊ.tə/
  1. [B1] kurzer Ton, den man hören kann, besonders von Mensch oder Tier
     - example: Vor Schreck brachte das kleine Kind keinen Laut heraus.
     - collocations: keinen Laut von sich geben; einen Laut ausstoßen; kein Laut war zu hören
  2. [B2] kleinste Einheit der gesprochenen Sprache, die man mit dem Mund bildet
     - example: Den Laut am Ende von „ich“ finden viele Lernende anfangs schwer.
     - labels: domain linguistics
**Lexeme 2: laut**, ADJ
- grammar: comparable True
- said: /ˈlaʊt/ (de-DE)
  1. [A1] kräftig klingend und deshalb gut und weit zu hören
     - example: Die Musik bei den Nachbarn war gestern Abend sehr laut.
     - collocations: laut sprechen; laut lachen; laut schreien; laute Musik; laut und deutlich; laut vorlesen; laut denken
     - opposites: leise
  2. [A2] voller Lärm und deshalb unruhig und störend
     - example: Unsere Wohnung liegt leider an einer sehr lauten Straße.
     - collocations: eine laute Straße; laute Nachbarn
     - opposites: ruhig
  3. [C1] öffentlich ausgesprochen und von vielen bemerkt
     - example: Nach dem Unfall wurden Forderungen nach strengeren Kontrollen laut.
     - frames: etw. wird laut
     - collocations: Kritik wird laut; Zweifel werden laut; Forderungen werden laut
     - grammar: position Pred
- relations: confusable lauter (ADJ): the comparative lauter looks like lauter 'pure' or 'nothing but'
**Lexeme 3: laut**, ADP
- grammar: note takes the genitive or dative; a noun without article after it usually stays uninflected
- said: /ˈlaʊt/ (de-DE)
  1. [B1] verwendet, um die Quelle einer Information oder Regel anzugeben
     - example: Laut Wetterbericht wird es morgen den ganzen Tag regnen.
     - frames: laut + Gen; laut + Dat
     - collocations: laut Gesetz; laut Vertrag; laut Angaben; laut Polizei; laut Bericht

### 45. stecken

**Lexeme 1: stecken**, VERB
- grammar: separable False; conjugation weak; auxiliary have; valency Both; note intransitive: in southern Germany, Austria and Switzerland also with sein
- said: /ˈʃtɛ.kən/ (de-DE)
- forms: steckte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /ˈʃtɛk.tə/; stak [Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /ˈʃtaːk/ (elevated, beside steckte, only in the intransitive senses); gesteckt [VerbForm=Part, Tense=Past] /ɡə.ˈʃtɛkt/
  1. [A2] etwas in eine Öffnung oder zwischen andere Dinge schieben
     - example: Er steckte den Schlüssel ins Schloss und öffnete die Tür.
     - frames: etw. in + Akk stecken; etw. zwischen + Akk stecken; etw. an + Akk stecken
     - collocations: den Schlüssel ins Schloss stecken; die Hände in die Taschen stecken; in den Briefkasten stecken; ins Gefängnis stecken; in die Tasche stecken
     - grammar: valency Tr
  2. [B1] sich in einer Öffnung oder in etwas anderem befinden
     - example: Der Schlüssel steckt noch von innen in der Tür.
     - frames: in + Dat stecken
     - collocations: im Schloss stecken
     - grammar: valency Intr
  3. [B2] sich in einer schwierigen Lage befinden, aus der man schwer herauskommt
     - example: Die Firma steckt seit Monaten in großen finanziellen Schwierigkeiten.
     - frames: in + Dat stecken
     - collocations: in Schwierigkeiten stecken; in der Krise stecken; in den Kinderschuhen stecken; im Stau stecken; mitten in der Arbeit stecken
     - labels: figurative True
     - grammar: valency Intr
  4. [B2] heimlich der Urheber oder die wahre Ursache von etwas sein
     - example: Die Polizei weiß noch nicht, wer hinter dem Überfall steckt.
     - frames: hinter + Dat stecken
     - labels: formality informal
     - grammar: valency Intr
  5. [B1] Geld oder Zeit für eine Sache ausgeben, um etwas zu erreichen
     - example: Sie hat ihr ganzes Erspartes in das alte Haus gesteckt.
     - frames: etw. in + Akk stecken
     - collocations: viel Geld stecken; viel Arbeit stecken; viel Mühe stecken; viel Zeit stecken
     - labels: formality informal
     - grammar: valency Tr
  6. [B1] an einem unbekannten Ort sein, sich irgendwo aufhalten
     - example: Ich weiß nicht, wo meine Brille schon wieder steckt.
     - labels: formality informal
     - grammar: valency Intr
  7. [B2] als Fähigkeit oder Eigenschaft in jemandem oder etwas vorhanden sein
     - example: In diesem Jungen steckt eine Menge Talent.
     - frames: in + Dat stecken
     - grammar: valency Intr
- relations: confusable stechen (VERB): similar sound; stechen means to prick or sting

### 46. betten

**Lexeme 1: betten**, VERB
- grammar: conjugation weak; auxiliary have; valency Tr; irregular False
- said: /ˈbɛ.tən/ (de-DE)
- forms: bettete [Tense=Past, Person=3, Number=Sing, VerbForm=Fin, Mood=Ind] /ˈbɛ.tə.tə/; gebettet [Tense=Past, VerbForm=Part] /ɡə.ˈbɛ.tət/
  1. [C1] jemanden vorsichtig und bequem irgendwo hinlegen, damit er liegen kann
     - example: Die Sanitäter betten den Verletzten vorsichtig auf eine Trage.
     - frames: jmdn. auf + Akk betten; jmdn. in + Akk betten; sich betten
     - collocations: weich betten; den Kopf betten; zur letzten Ruhe betten
  2. [C1] etwas fest in ein Material oder eine Umgebung legen, sodass es davon umgeben ist
     - example: Die neuen Leitungen werden in eine Schicht aus Sand gebettet.
     - frames: etw. in + Akk betten
     - collocations: in Sand betten
- relations: derived_from Bett (NOUN)

### 47. Höhle

**Lexeme 1: Höhle**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /ˈhøː.lə/ (de-DE)
- forms: Höhle [Case=Gen, Number=Sing] /ˈhøː.lə/; Höhlen [Case=Nom, Number=Plur] /ˈhøː.lən/
  1. [B1] natürlicher großer leerer Raum im Felsen oder unter der Erde
     - example: In dieser Höhle haben Forscher sehr alte Wandmalereien entdeckt.
     - collocations: eine Höhle erforschen; eine Höhle betreten; in einer Höhle leben; eine dunkle Höhle; eine tiefe Höhle
  2. [B2] geschützter Ort, an dem ein wildes Tier wohnt und sich versteckt
     - example: Der Bär schläft den ganzen Winter in seiner Höhle.
     - collocations: sich in die Höhle des Löwen wagen; in die Höhle des Löwen gehen
  3. [C1] dunkle, enge und ungemütliche Wohnung oder Unterkunft
     - example: In dieser feuchten Höhle wollen wir nicht länger wohnen.
     - labels: attitude derogatory, formality informal
- relations: derived_from hohl (ADJ); confusable Hölle (NOUN): Hölle 'hell' differs only by a short ö and double l

### 48. Vermieter

**Lexeme 1: Vermieter**, NOUN
- grammar: gender Masc; plural_class -; countability Count
- said: /fɛɐ.ˈmiː.tɐ/ (de-DE)
- forms: Vermieters [Case=Gen, Number=Sing] /fɛɐ.ˈmiː.tɐs/; Vermieter [Case=Nom, Number=Plur] /fɛɐ.ˈmiː.tɐ/
  1. [B1] Person oder Firma, die anderen etwas gegen regelmäßige Bezahlung zur Nutzung überlässt
     - example: Unser Vermieter will die Heizung im Sommer endlich reparieren lassen.
     - collocations: privater Vermieter; den Vermieter informieren; dem Vermieter kündigen; sich beim Vermieter beschweren; der Vermieter kündigt
     - opposites: Mieter
- relations: agent_noun_of vermieten (VERB)

### 49. passen

**Lexeme 1: passen**, VERB
- grammar: separable False; irregular False; conjugation weak; auxiliary have; valency Both, mainly
- said: /ˈpa.sən/ (de-DE)
- forms: passte [Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /ˈpas.tə/; gepasst [VerbForm=Part, Tense=Past] /ɡə.ˈpast/
  1. [A1] die richtige Größe und Form für jemanden oder etwas haben
     - example: Die neue Hose passt mir leider nicht mehr.
     - frames: jmdm. passen; passen in + Akk
     - collocations: wie angegossen passen; genau passen; nicht mehr passen
     - grammar: valency Intr
  2. [A2] gut mit etwas anderem zusammen aussehen oder zusammengehören
     - example: Die blaue Krawatte passt gut zu deinem grauen Anzug.
     - frames: passen zu + Dat
     - collocations: gut zusammen passen; farblich passen; nicht ins Bild passen
     - grammar: valency Intr
  3. [A2] für jemanden günstig oder angenehm sein, besonders was die Zeit betrifft
     - example: Passt es dir, wenn wir uns morgen um zehn treffen?
     - frames: jmdm. passen; es passt jmdm., wenn ...
     - collocations: zeitlich passen; das passt mir nicht; passt schon
     - grammar: valency Intr
  4. [B2] im Spiel aussetzen oder zugeben, dass man keine Antwort weiß
     - example: Bei dieser schwierigen Frage muss ich leider passen.
     - frames: passen bei + Dat
     - grammar: valency Intr
  5. [B2] im Sport den Ball einem Spieler der eigenen Mannschaft zuspielen
     - example: Der Stürmer passte den Ball schnell zu seinem Mitspieler.
     - frames: etw. passen; passen zu + Dat
     - collocations: den Ball passen; quer passen
     - grammar: valency Both
- relations: confusable passieren (VERB): both look like English 'pass'; passieren means to happen or to go past

### 50. schlau

**Lexeme 1: schlau**, ADJ
- grammar: comparable True
- said: /ˈʃlaʊ/ (de-DE)
  1. [A2] mit gutem Verstand, der Dinge schnell versteht und gut lernt
     - example: Mein kleiner Bruder ist sehr schlau und rechnet schneller als ich.
     - frames: aus jmdm./etw. nicht schlau werden
     - collocations: sich schlau machen; ein schlauer Kopf; ein schlaues Kind; ein schlaues Buch
     - opposites: dumm
  2. [B1] geschickt und listig darin, die eigenen Ziele zu erreichen
     - example: Mit einem schlauen Trick lenkt sie die Wächter am Tor ab.
     - collocations: ein schlauer Fuchs; ein schlauer Trick; ein schlauer Plan; es schlau anstellen

### 51. Innenminister

**Lexeme 1: Innenminister**, NOUN
- grammar: gender Masc; plural_class -
- said: /ˈɪ.nən.mi.ˌnɪs.tɐ/ (de-DE)
- parts: Innen (innen) + minister (Minister)
- forms: Innenministers [Case=Gen, Number=Sing] /ˈɪ.nən.mi.ˌnɪs.tɐs/; Innenminister [Case=Nom, Number=Plur] /ˈɪ.nən.mi.ˌnɪs.tɐ/
  1. [B2] Politiker in der Regierung, der für Polizei und innere Sicherheit zuständig ist
     - example: Der Innenminister kündigte strengere Kontrollen an den Grenzen an.
     - collocations: zum Innenminister ernennen; als Innenminister zurücktreten; der frühere Innenminister; der Innenminister des Landes

### 52. Rucksack

**Lexeme 1: Rucksack**, NOUN
- grammar: gender Masc; plural_class -¨e; countability Count
- said: /ˈʁʊk.zak/ (de-DE)
- forms: Rucksacks [Case=Gen, Number=Sing] /ˈʁʊk.zaks/; Rucksackes [Case=Gen, Number=Sing] /ˈʁʊk.za.kəs/; Rucksäcke [Case=Nom, Number=Plur] /ˈʁʊk.zɛ.kə/
  1. [A2] Tasche mit zwei Riemen, die man auf dem Rücken trägt
     - example: Für die Wanderung packe ich Wasser und Brot in meinen Rucksack.
     - collocations: den Rucksack packen; einen Rucksack tragen; den Rucksack aufsetzen; den Rucksack absetzen; mit dem Rucksack reisen

### 53. öfters

**Lexeme 1: öfters**, ADV
- said: /ˈœf.tɐs/ (de-DE)
  1. [B1] mehrmals und in unregelmäßigen Abständen, nicht nur ein einziges Mal
     - example: Wir treffen uns öfters nach der Arbeit in diesem kleinen Café.
     - collocations: schon öfters; öfters mal
     - regions: de-AT mainly, de-CH mainly
     - opposites: nie
- relations: derived_from oft (ADV); confusable öfter (ADV): öfter is also the comparative of oft (more often), while öfters only means 'now and then'

### 54. Straßenbahn

**Lexeme 1: Straßenbahn**, NOUN
- grammar: gender Fem; plural_class -en; countability Count
- said: /ˈʃtʁaː.sən.ˌbaːn/ (de-DE)
- spellings: Straßenbahn (de-DE, de-AT); Strassenbahn (de-CH)
- parts: Straßen (Straße) + bahn (Bahn)
- forms: Straßenbahn [Case=Gen, Number=Sing] /ˈʃtʁaː.sən.ˌbaːn/; Straßenbahnen [Case=Nom, Number=Plur] /ˈʃtʁaː.sən.ˌbaː.nən/
  1. [A2] elektrisches Fahrzeug, das auf Schienen durch die Stadt fährt und Fahrgäste befördert
     - example: Zur Arbeit fahre ich jeden Morgen mit der Straßenbahn.
     - collocations: mit der Straßenbahn fahren; die Straßenbahn nehmen; in die Straßenbahn einsteigen; aus der Straßenbahn aussteigen; die Straßenbahn verpassen

### 55. Webseite

**Lexeme 1: Webseite**, NOUN
- grammar: gender Fem; plural_class -n; countability Count
- said: /ˈvɛp.zaɪ.tə/ (de-DE)
- parts: Web (Web) + seite (Seite)
- forms: Webseite [Case=Gen, Number=Sing] /ˈvɛp.zaɪ.tə/; Webseiten [Case=Nom, Number=Plur] /ˈvɛp.zaɪ.tən/
  1. [A2] einzelne Seite im Internet, die man mit einem Browser aufrufen kann
     - example: Auf dieser Webseite stehen alle Öffnungszeiten der Bibliothek.
     - collocations: eine Webseite aufrufen; eine Webseite besuchen; auf einer Webseite stehen; eine Webseite laden; auf einer Webseite
  2. [A2] alle Seiten im Internet, die zu einer Firma, Person oder Organisation gehören
     - example: Unser Verein hat endlich eine eigene Webseite mit Fotos und Terminen.
     - collocations: eine Webseite erstellen; eine eigene Webseite; eine Webseite betreiben; die offizielle Webseite
- relations: confusable Website (NOUN): Website is strictly the whole site, Webseite a single page, but many speakers use them interchangeably
