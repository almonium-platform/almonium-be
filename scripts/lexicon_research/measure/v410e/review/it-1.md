# Italian: entries to check (1 of 1)

These are entries of a learner's dictionary of Italian, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of Italian.

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
  sense, or an example is not natural Italian.
- **collocation**: a collocation that is not said that way in Italian; or one that does not belong
  among the ten a learner is shown for the sense, because anyone would build it from the meanings
  or because it is rare; or a list that lacks the commonest partner of the sense while it has
  rarer ones. Quote the collocation in "what". A frame with the wrong preposition or case is a
  fact, not this.

Do not report: the order of senses, a CEFR level off by one step, how syllables are divided, a
definition you would have worded differently when the given one is true, or anything about style.

These are this dictionary's own definitions, not faults:

- valency Refl stands for every use with a reflexive pronoun, whatever a grammar of Italian calls it
  (reflexive, reciprocal, pronominal); Tr means the verb takes a direct object, so a verb whose
  object needs a preposition or the dative is Intr.
- a grammar value (countability, valency, position, number) says how the word is ordinarily used, as
  a learner's dictionary of Italian prints it: "Mass" is "uncountable" in that sense, although kinds
  of the thing can be counted. Followed by "mainly", the other use is common too. Report a value
  only when a learner's dictionary would not print it for this word or sense.
- position Attr means the adjective is used joined to a noun and not after a verb such as "be",
  wherever Italian puts it (before the noun in English, mostly after it in French); Pred means only
  after such a verb. Neither says on which side of the noun the adjective stands.
- a lexeme is one word with one way of saying and inflecting it. Two homonyms that are said and
  inflected alike are senses of one lexeme here, although dictionaries of Italian number them apart.
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

### 1. presunto

**Lexeme 1: presunto**, ADJ
- grammar: comparable False; position Attr, mainly; note usually before the noun
- said: /pre.ˈzun.to/ presùnto (it-IT), /pre.ˈsun.to/ presùnto (it-IT)
  1. [B2] che si crede o si dice tale, ma senza prove sicure
     - example: La polizia ha fermato ieri sera il presunto autore della rapina.
     - collocations: presunto colpevole; presunto autore; presunta vittima; presunto responsabile; data presunta del parto
- relations: derived_from presumere (VERB)

### 2. cominciare

**Lexeme 1: cominciare**, VERB
- grammar: conjugation 1; irregular False; valency Both; auxiliary both
- said: /ko.min.ˈtʃa.re/ cominciàre (it-IT)
  1. [A1] fare la prima parte di un’attività o mettersi a fare qualcosa
     - example: Domani comincio a studiare per l’esame di storia.
     - frames: cominciare qcs; cominciare a + inf; cominciare da qcs; cominciare con qcs
     - collocations: cominciare da capo; cominciare da zero; per cominciare; cominciare un lavoro
     - grammar: valency Both; auxiliary have
     - opposites: finire
  2. [A1] avere inizio in un certo momento o luogo
     - example: Lo spettacolo comincia alle nove, quindi non fate tardi.
     - frames: qcs comincia; cominciare con qcs
     - collocations: cominciare bene; cominciare male
     - grammar: valency Intr; auxiliary be
     - opposites: finire

### 3. esclusivamente

**Lexeme 1: esclusivamente**, ADV
- said: /e.sklu.zi.va.ˈmen.te/ esclusivaménte (it-IT)
  1. [B1] soltanto, senza comprendere nessun’altra persona, cosa o possibilità
     - example: Questo parcheggio è riservato esclusivamente ai residenti del quartiere.
     - collocations: riservato esclusivamente; dedicarsi esclusivamente; dipendere esclusivamente da
- relations: adverb_of esclusivo (ADJ)

### 4. evidente

**Lexeme 1: evidente**, ADJ
- grammar: comparable True
- said: /e.vi.ˈdɛn.te/ evidènte (it-IT)
  1. [B1] che si vede o si capisce con chiarezza, senza alcun dubbio
     - example: Era evidente che nessuno aveva voglia di uscire con quella pioggia.
     - frames: è evidente che + ind
     - collocations: segni evidenti; prova evidente; differenza evidente; evidente imbarazzo; in modo evidente

### 5. fila

**Lexeme 1: fila**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈfi.la/ fila (it-IT)
  1. [A2] serie di persone o cose messe una dietro l’altra o una accanto all’altra
     - example: A teatro preferisco sedermi nelle prime file, vicino al palco.
     - frames: una fila di qcs/qcn
     - collocations: di fila; in fila indiana; in prima fila; una fila di alberi; fila di posti
  2. [A2] gruppo di persone che aspettano una dopo l’altra il proprio turno
     - example: Stamattina alla posta abbiamo fatto la fila per un’ora.
     - collocations: fare la fila; mettersi in fila; saltare la fila; una lunga fila; in fila
  3. [C1] insieme delle persone che fanno parte di un esercito, un partito o un gruppo
     - example: Dopo le elezioni molti giovani sono entrati nelle file del partito.
     - frames: nelle file di qcs
     - collocations: serrare le file; ingrossare le file; militare nelle file
     - labels: figurative True
     - grammar: number Plur

### 6. top

**Lexeme 1: top**, NOUN
- grammar: gender Masc; invariable True
- said: /ˈtɔp/ top (it-IT)
  1. [B1] indumento femminile leggero che copre il busto, spesso senza maniche
     - example: Con questo caldo metto un top e una gonna di cotone.
     - collocations: top di pizzo; top senza spalline; top sportivo
     - grammar: countability Count
  2. [B2] il livello più alto, la cosa o la condizione migliore
     - example: Per una vacanza al mare, quest’isola è davvero il top.
     - collocations: essere il top; il top della gamma; al top della forma; il top del top
     - labels: formality informal
     - grammar: number Sing; countability Mass

### 7. decidere

**Lexeme 1: decidere**, VERB
- grammar: conjugation 2; irregular True; valency Both
- said: /de.ˈtʃi.de.re/ decìdere (it-IT)
- forms: decido [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /de.ˈtʃi.do/ decìdo; decidi [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /de.ˈtʃi.di/ decìdi; decide [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /de.ˈtʃi.de/ decìde; decidiamo [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /de.tʃi.ˈdja.mo/ decidiàmo; decidono [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /de.ˈtʃi.do.no/ decìdono; decisi [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /de.ˈtʃi.zi/ decìsi; decise [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /de.ˈtʃi.ze/ decìse; deciderò [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] /de.tʃi.de.ˈrɔ/ deciderò; decida [Mood=Sub, Tense=Pres, Person=1,2,3, Number=Sing, VerbForm=Fin] /de.ˈtʃi.da/ decìda; deciso [VerbForm=Part, Tense=Past] /de.ˈtʃi.zo/ decìso
  1. [A2] scegliere, dopo averci pensato, che cosa fare o quale soluzione prendere
     - example: Quest’anno abbiamo deciso di passare le vacanze al mare.
     - frames: decidere di + inf; decidere qcs; decidere che + ind; decidere su qcs
     - collocations: decidere all’ultimo momento; decidere da solo; decidere in fretta; decidere insieme
     - grammar: valency Both; auxiliary have
  2. [B2] dare un giudizio che chiude una questione o una lite
     - example: Sarà il tribunale a decidere la questione dell’eredità.
     - frames: decidere qcs
     - collocations: decidere una causa; decidere una controversia; decidere una questione
     - grammar: valency Tr; auxiliary have
  3. [B2] avere un peso determinante sul risultato o sulla sorte di qualcosa
     - example: Un rigore all’ultimo minuto ha deciso la partita.
     - frames: qcs decide qcs; decidere di qcs
     - collocations: decidere la partita; decidere le sorti; decidere il risultato
     - grammar: valency Both; auxiliary have
  4. [B1] arrivare finalmente a una scelta dopo dubbi o esitazioni
     - example: Dopo mesi di dubbi, mi sono deciso a cambiare lavoro.
     - frames: decidersi a + inf
     - collocations: decidersi una buona volta; non sapersi decidere
     - grammar: valency Refl; reflexive True; auxiliary be; note used as decidersi

### 8. negozio

**Lexeme 1: negozio**, NOUN
- grammar: gender Masc; countability Count
- said: /ne.ˈɡɔt.tsjo/ negòzio (it-IT)
  1. [A1] locale dove si vendono merci al pubblico
     - example: Sotto casa mia hanno aperto un nuovo negozio di scarpe.
     - frames: negozio di qcs
     - collocations: negozio di alimentari; andare per negozi; fare il giro dei negozi; aprire un negozio; gestire un negozio; negozio online; commesso di negozio
  2. [C2] atto con cui una persona esprime la volontà di produrre effetti voluti dalla legge
     - example: Il testamento è un negozio che produce effetti dopo la morte.
     - collocations: negozio giuridico
     - labels: domain law

### 9. industriale

**Lexeme 1: industriale**, ADJ
- grammar: comparable False
- said: /in.dus.ˈtrja.le/ industriàle (it-IT)
  1. [B1] della produzione di merci in grande quantità, con macchine e fabbriche
     - example: Dietro la stazione si trova una vecchia zona industriale abbandonata.
     - collocations: zona industriale; rivoluzione industriale; produzione industriale; settore industriale; area industriale; prodotto industriale; design industriale; paese industriale
- relations: adjective_of industria (NOUN)
**Lexeme 2: industriale**, NOUN
- grammar: gender Com; countability Count
- said: /in.dus.ˈtrja.le/ industriàle (it-IT)
  1. [B2] persona che possiede o dirige una grande impresa che produce merci
     - example: Mio nonno era un industriale del tessile molto conosciuto in città.
     - collocations: grande industriale; ricco industriale; associazione degli industriali
- relations: derived_from industriale (ADJ)

### 10. influenza

**Lexeme 1: influenza**, NOUN
- grammar: gender Fem
- said: /in.flu.ˈɛn.tsa/ influènza (it-IT)
  1. [B1] potere di agire sulle idee, le scelte o il comportamento di persone o cose
     - example: Quel professore ha avuto una grande influenza sulle mie scelte.
     - frames: influenza su qcn/qcs; sotto l'influenza di qcn/qcs
     - collocations: esercitare un'influenza; avere influenza; subire l'influenza; zona d'influenza; influenza negativa; forte influenza
     - grammar: countability Both
  2. [A2] malattia contagiosa causata da un virus, che porta febbre e dolori
     - example: Domani resto a casa perché ho l'influenza da tre giorni.
     - collocations: avere l'influenza; prendere l'influenza; a letto con l'influenza; vaccino contro l'influenza; influenza stagionale; influenza intestinale
     - grammar: countability Count

### 11. istruzione

**Lexeme 1: istruzione**, NOUN
- grammar: gender Fem; countability Both
- said: /is.trut.ˈtsjo.ne/ istruzióne (it-IT)
  1. [B1] insegnamento e formazione che una persona riceve, soprattutto a scuola
     - example: In molti paesi l'istruzione è gratuita fino ai diciotto anni.
     - collocations: diritto all'istruzione; istruzione obbligatoria; istruzione superiore; istruzione primaria; pubblica istruzione; livello di istruzione; ricevere un'istruzione
     - grammar: countability Mass
  2. [A2] indicazione su come fare qualcosa o su come comportarsi
     - example: Prima di montare il mobile, leggi bene tutte le istruzioni.
     - frames: istruzioni per qcs; istruzioni su qcs
     - collocations: istruzioni per l'uso; libretto di istruzioni; seguire le istruzioni; dare istruzioni; leggere le istruzioni; ricevere istruzioni
     - grammar: countability Count; number Plur, mainly
  3. [C1] comando che dice a un computer quale operazione eseguire
     - example: Il programma esegue ogni istruzione nell'ordine in cui è scritta.
     - labels: domain computing
     - grammar: countability Count
- relations: verbal_noun_of istruire (VERB)

### 12. ricco

**Lexeme 1: ricco**, ADJ
- grammar: comparable True
- said: /ˈrik.ko/ rìcco (it-IT)
  1. [A1] che possiede molto denaro o molti beni di valore
     - example: Sogno di diventare ricco per girare il mondo senza pensieri.
     - collocations: ricco sfondato; diventare ricco; famiglia ricca; paese ricco
     - opposites: povero
  2. [B1] che contiene o possiede qualcosa in grande quantità
     - example: Questa regione è ricca di boschi, fiumi e piccoli laghi.
     - frames: ricco di qcs
     - collocations: ricco di vitamine; ricco di storia; terreno ricco
     - opposites: povero
  3. [B2] abbondante per quantità o notevole per valore
     - example: Ogni mattina in albergo servivano una ricca colazione.
     - collocations: ricca colazione; ricco bottino; ricco programma; ricco premio
     - grammar: position Attr, mainly
**Lexeme 2: ricco**, NOUN
- grammar: gender Masc; countability Count; note mostly in the plural, i ricchi; feminine ricca for a woman
- said: /ˈrik.ko/ rìcco (it-IT)
- forms: ricca [Gender=Fem, Number=Sing] rìcca
  1. [A2] persona che possiede molto denaro o molti beni
     - example: Le tasse sui ricchi sono un tema molto discusso.
     - opposites: povero
- relations: derived_from ricco (ADJ)

### 13. tradizionale

**Lexeme 1: tradizionale**, ADJ
- grammar: comparable True
- said: /tra.dit.tsjo.ˈna.le/ tradizionàle (it-IT)
  1. [B1] trasmesso da una generazione all'altra e legato alle usanze di un popolo
     - example: A Natale mia nonna prepara sempre i dolci tradizionali del paese.
     - collocations: cucina tradizionale; piatto tradizionale; costume tradizionale; festa tradizionale; musica tradizionale; ricetta tradizionale
  2. [B2] che segue modelli e abitudini già affermati, senza cercare novità
     - example: Preferisco imparare le lingue con un metodo più tradizionale.
     - collocations: metodo tradizionale; in modo tradizionale; famiglia tradizionale; medicina tradizionale; scuola tradizionale
     - opposites: innovativo
- relations: adjective_of tradizione (NOUN)

### 14. live

**Lexeme 1: live**, ADJ
- grammar: invariable True; comparable False; position Attr, mainly; note English loanword; usually placed after the noun
- said: /ˈlajv/ live (it-IT)
  1. [B1] eseguito, trasmesso o registrato davanti al pubblico nel momento in cui avviene
     - example: Stasera in piazza c'è un concerto live di musica jazz.
     - collocations: concerto live; musica live; esibizione live; versione live; album live
**Lexeme 2: live**, ADV
- said: /ˈlajv/ live (it-IT)
  1. [B2] davanti al pubblico, nel momento stesso dell'esecuzione
     - example: Questa band suona molto meglio live che in studio.
     - collocations: suonare live; cantare live
**Lexeme 3: live**, NOUN
- grammar: gender Masc; countability Count; invariable True
- said: /ˈlajv/ live (it-IT)
  1. [B2] esibizione eseguita davanti al pubblico, o il disco che la registra
     - example: Il loro ultimo live è stato registrato in un piccolo teatro.

### 15. elemento

**Lexeme 1: elemento**, NOUN
- grammar: gender Masc; countability Count
- said: /e.le.ˈmen.to/ eleménto (it-IT)
  1. [B1] ciascuna delle parti che formano un insieme o una struttura
     - example: Ogni elemento del mobile è numerato per facilitare il montaggio.
     - frames: elemento di qcs
     - collocations: elemento fondamentale; elemento essenziale; elemento chiave; elemento decorativo
  2. [B2] fatto o informazione che serve a capire o a giudicare qualcosa
     - example: Per ora non abbiamo elementi sufficienti per prendere una decisione.
     - collocations: elementi di prova; elementi a disposizione; elementi sufficienti; elemento di novità
  3. [B2] persona considerata per il ruolo o il valore che ha in un gruppo
     - example: In ufficio tutti la considerano un elemento prezioso.
     - collocations: un ottimo elemento; un buon elemento; elemento di spicco
  4. [B2] sostanza semplice formata da atomi di un solo tipo
     - example: L'ossigeno è l'elemento più diffuso nella crosta terrestre.
     - collocations: elemento chimico; tavola periodica degli elementi
     - labels: domain chemistry
  5. [C1] nozioni di base di una scienza o di una disciplina
     - example: Il corso serale offre i primi elementi di economia.
     - frames: elementi di qcs
     - grammar: number Plur
  6. [C1] ciascuna delle forze naturali che gli antichi ponevano alla base del mondo
     - example: Il piccolo villaggio ha resistito per secoli alla furia degli elementi.
     - collocations: la furia degli elementi; i quattro elementi; essere nel proprio elemento

### 16. blocco

**Lexeme 1: blocco**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈblɔk.ko/ blòcco (it-IT)
- forms: blocchi [Number=Plur] blòcchi
  1. [B1] pezzo grande e compatto di un materiale solido
     - example: Lo scultore ha ricavato la statua da un unico blocco di marmo.
     - frames: blocco di qcs
     - collocations: blocco di marmo; blocco di ghiaccio; blocco di cemento; blocco di pietra
  2. [B1] arresto improvviso del movimento o del funzionamento di qualcosa
     - example: Un incidente in autostrada ha causato il blocco del traffico per ore.
     - frames: blocco di qcs
     - collocations: blocco del traffico; andare in blocco; blocco psicologico; blocco dello scrittore; blocco del motore
  3. [B2] sbarramento che impedisce il passaggio di persone, mezzi o merci
     - example: La polizia controlla tutte le auto a un posto di blocco.
     - collocations: posto di blocco; blocco stradale; blocco navale; forzare il blocco; togliere il blocco
  4. [B2] provvedimento che vieta per un certo tempo aumenti o cambiamenti
     - example: Il governo ha deciso il blocco degli affitti per un anno.
     - frames: blocco di qcs
     - collocations: blocco dei prezzi; blocco degli stipendi; blocco delle assunzioni; blocco degli affitti
  5. [B1] insieme di fogli uniti da un lato che si possono staccare
     - example: Tengo sempre un blocco vicino al telefono per prendere appunti.
     - collocations: blocco per appunti; blocco da disegno; blocco note
  6. [B2] insieme di cose o di elementi considerati come un tutto unico
     - example: Abbiamo venduto in blocco tutti i mobili della vecchia casa.
     - collocations: in blocco; blocco di case
  7. [C1] gruppo di partiti o di stati uniti da interessi comuni
     - example: Dopo la guerra l’Europa era divisa in due blocchi contrapposti.
     - collocations: blocco sovietico; blocco occidentale
- relations: derived_from bloccare (VERB)

### 17. agenzia

**Lexeme 1: agenzia**, NOUN
- grammar: gender Fem; countability Count
- said: /a.dʒen.ˈtsi.a/ agenzìa (it-IT)
  1. [A2] impresa che offre un servizio o fa affari per conto dei clienti
     - example: Abbiamo prenotato il volo e l’albergo in un’agenzia sotto casa.
     - collocations: agenzia di viaggi; agenzia immobiliare; agenzia di stampa; agenzia pubblicitaria; agenzia matrimoniale; agenzia per il lavoro; notizia d’agenzia
  2. [B1] ufficio locale di una banca o di una società di assicurazioni
     - example: Per aprire il conto devi andare nell’agenzia più vicina.
     - collocations: agenzia bancaria; agenzia di banca
  3. [B2] ente pubblico che svolge per lo Stato compiti precisi
     - example: Il nuovo satellite è stato lanciato dall’agenzia spaziale europea.
     - collocations: agenzia spaziale; agenzia delle entrate; agenzia governativa

### 18. utente

**Lexeme 1: utente**, NOUN
- grammar: gender Com; countability Count
- said: /u.ˈtɛn.te/ utènte (it-IT)
  1. [B1] persona che usa un servizio, spesso pubblico, o un bene messo a disposizione
     - example: Gli utenti della linea ferroviaria protestano per i continui ritardi.
     - frames: utente di qcs
     - collocations: utente finale; utenti della strada; utente telefonico; assistenza agli utenti; servizio agli utenti
  2. [B1] persona che usa un computer, un programma o un servizio in rete
     - example: Per accedere al sito ogni utente deve scegliere una password.
     - collocations: nome utente; utente registrato; account utente; registrarsi come utente; utente anonimo

### 19. museo

**Lexeme 1: museo**, NOUN
- grammar: gender Masc; countability Count
- said: /mu.ˈzɛ.o/ musèo (it-IT)
  1. [A1] raccolta di opere o oggetti di valore culturale, o il luogo dove si espongono
     - example: Domenica andiamo al museo a vedere una mostra di pittura.

### 20. pertanto

**Lexeme 1: pertanto**, CONJ
- said: /per.ˈtan.to/ pertànto (it-IT)
- parts: per (per) + tanto (tanto)
  1. [B2] usato per introdurre la conseguenza o la conclusione di quanto detto prima
     - example: Il negozio resterà chiuso per lavori, pertanto vi preghiamo di tornare lunedì.

### 21. recente

**Lexeme 1: recente**, ADJ
- grammar: comparable True
- said: /re.ˈtʃɛn.te/ recènte (it-IT)
- spellings: recente (it-IT); ricente (it-IT) [older books only]
  1. [B1] avvenuto, nato o fatto da poco tempo
     - example: Secondo uno studio recente, i giovani leggono sempre meno giornali.
     - collocations: di recente
     - opposites: remoto

### 22. omicidio

**Lexeme 1: omicidio**, NOUN
- grammar: gender Masc; countability Count
- said: /o.mi.ˈtʃi.djo/ omicìdio (it-IT)
  1. [B2] uccisione di una persona compiuta da un'altra persona
     - example: La polizia sta ancora indagando sull'omicidio avvenuto in centro.
     - frames: omicidio di qcn
     - collocations: commettere un omicidio; tentato omicidio; omicidio colposo; omicidio volontario; omicidio premeditato; omicidio stradale; accusare di omicidio; indagare su un omicidio

### 23. insaputa

**Lexeme 1: insaputa**, NOUN
- grammar: gender Fem; number Sing; note used only in all'insaputa di and with a possessive: a mia insaputa, a sua insaputa
- said: /in.sa.ˈpu.ta/ insapùta (it-IT)
  1. [B2] condizione di chi non è informato di ciò che accade
     - example: Hanno organizzato la festa all'insaputa di nostra madre.
     - frames: all'insaputa di qcn
     - collocations: a mia insaputa; a sua insaputa; a loro insaputa; a tua insaputa

### 24. primavera

**Lexeme 1: primavera**, NOUN
- grammar: gender Fem; countability Count
- said: /pri.ma.ˈvɛ.ra/ primavèra (it-IT), /pri.ma.ˈve.ra/ primavéra (it-IT, regional)
  1. [A1] stagione dell’anno tra l’inverno e l’estate, quando la natura rifiorisce
     - example: Quest’anno la primavera è arrivata presto e gli alberi sono già fioriti.
     - collocations: primavera inoltrata; inizio della primavera; arriva la primavera; pulizie di primavera; equinozio di primavera
  2. [C1] periodo iniziale o di rinascita, pieno di energia e di speranza
     - example: Quei pochi anni furono la primavera della sua carriera artistica.
     - frames: la primavera di qcs
     - collocations: la primavera della vita
     - labels: figurative True, style literary
  3. [B2] anni di vita di una persona, quando si indica la sua età
     - example: Mio nonno ha compiuto novanta primavere e cammina ancora molto.
     - collocations: avere molte primavere
     - labels: attitude humorous
     - grammar: number Plur
  4. [C1] squadra giovanile di una società di calcio professionistica
     - example: Il nuovo attaccante è cresciuto nella Primavera della società.
     - labels: domain sport
     - regions: it-IT mainly
     - grammar: note usually written with a capital letter

### 25. vento

**Lexeme 1: vento**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈvɛn.to/ vènto (it-IT)
  1. [A1] aria che si sposta nell'atmosfera, con forza e velocità diverse
     - example: Stanotte tira un vento così forte che le finestre tremano.
     - collocations: tira vento; c'è vento; colpo di vento; raffica di vento; vento forte; vento freddo; controvento; mulino a vento; in balia del vento
  2. [B2] tendenza o clima nuovo che si diffonde in un ambiente o in una società
     - example: Dopo le elezioni si respira un vento di cambiamento nel paese.
     - frames: vento di qcs
     - collocations: vento di cambiamento; vento di rinnovamento; il vento è cambiato
     - labels: figurative True
     - grammar: countability Count

### 26. facoltà

**Lexeme 1: facoltà**, NOUN
- grammar: gender Fem; countability Count; invariable True
- said: /fa.kol.ˈta/ facoltà (it-IT)
  1. [B1] ciascuna delle grandi sezioni di un'università dedicate a un campo di studi
     - example: Mio fratello si è iscritto alla facoltà di medicina quest'anno.
     - frames: facoltà di qcs
     - collocations: iscriversi a una facoltà; facoltà di lettere; facoltà di medicina; preside di facoltà; consiglio di facoltà
  2. [B2] capacità naturale del corpo o della mente di fare qualcosa
     - example: Nonostante l'età, il nonno conserva intatte le sue facoltà mentali.
     - collocations: facoltà mentali; nel pieno possesso delle proprie facoltà; facoltà intellettive; perdere le facoltà
  3. [B2] diritto o potere di fare qualcosa, dato dalla legge o da un'autorità
     - example: L'imputato ha deciso di avvalersi della facoltà di non rispondere.
     - frames: facoltà di + inf
     - collocations: avvalersi della facoltà di non rispondere; dare facoltà; lasciare facoltà

### 27. ritardo

**Lexeme 1: ritardo**, NOUN
- grammar: gender Masc; countability Count
- said: /ri.ˈtar.do/ ritàrdo (it-IT)
  1. [A2] tempo che passa oltre il momento previsto o stabilito per qualcosa
     - example: Scusate il ritardo, c'era molto traffico in centro.
     - collocations: essere in ritardo; arrivare in ritardo; in ritardo; scusate il ritardo; accumulare un ritardo; avere un'ora di ritardo; senza ritardo; forte ritardo
     - opposites: anticipo
  2. [B2] situazione di chi è più indietro degli altri in un progresso
     - example: Il nostro paese deve recuperare il ritardo nelle infrastrutture digitali.
     - frames: ritardo rispetto a qcn/qcs; ritardo in qcs
     - collocations: colmare il ritardo; recuperare il ritardo; ritardo tecnologico
     - labels: figurative True
  3. [C1] sviluppo fisico o mentale più lento del normale
     - example: I medici hanno notato un lieve ritardo nello sviluppo del linguaggio.
     - frames: ritardo in qcs
     - collocations: ritardo mentale; ritardo nella crescita; ritardo cognitivo
     - labels: domain medicine
- relations: verbal_noun_of ritardare (VERB)

### 28. lago

**Lexeme 1: lago**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈla.ɡo/ làgo (it-IT)
  1. [A1] grande massa d’acqua ferma circondata da terra
     - example: D’estate andiamo spesso a fare il bagno al lago.
     - collocations: riva del lago; sponda del lago; lago artificiale; lago alpino; lago di montagna; giro del lago
  2. [B2] grande quantità di liquido sparso per terra
     - example: Dopo il temporale, il cortile era diventato un lago.
     - frames: un lago di qcs
     - collocations: un lago di sangue; un lago di lacrime; in un lago di sudore
     - labels: figurative True

### 29. nemico

**Lexeme 1: nemico**, NOUN
- grammar: gender Masc; countability Count; note feminine nemica, for a woman
- said: /ne.ˈmi.ko/ nemìco (it-IT)
- forms: nemici [Gender=Masc, Number=Plur] /ne.ˈmi.tʃi/ nemìci; nemica [Gender=Fem, Number=Sing] /ne.ˈmi.ka/ nemìca; nemiche [Gender=Fem, Number=Plur] /ne.ˈmi.ke/ nemìche
  1. [A2] persona che odia qualcuno o cerca di fargli del male
     - example: Con il suo carattere difficile si è fatto molti nemici.
     - collocations: farsi dei nemici; nemico giurato; acerrimo nemico; nemico mortale; nemico pubblico
     - opposites: amico
  2. [B1] l’esercito o il paese contro cui si combatte in una guerra
     - example: All’alba il nemico ha attaccato le nostre posizioni sulla collina.
     - collocations: passare al nemico; attaccare il nemico; respingere il nemico
     - grammar: number Sing, mainly; note usually il nemico, for the whole opposing side
  3. [B2] persona o cosa che si oppone a qualcosa o le fa danno
     - example: La fretta è spesso la peggiore nemica di un buon lavoro.
     - frames: nemico di qcs
     - collocations: il peggior nemico
     - labels: figurative True
**Lexeme 2: nemico**, ADJ
- said: /ne.ˈmi.ko/ nemìco (it-IT)
- forms: nemica [Gender=Fem, Number=Sing] /ne.ˈmi.ka/ nemìca; nemici [Gender=Masc, Number=Plur] /ne.ˈmi.tʃi/ nemìci; nemiche [Gender=Fem, Number=Plur] /ne.ˈmi.ke/ nemìche
  1. [B1] che appartiene alla parte contro cui si combatte in guerra
     - example: Durante la notte gli aerei nemici hanno bombardato il ponte.
     - collocations: esercito nemico; truppe nemiche; territorio nemico; linee nemiche; fuoco nemico
     - grammar: position Attr; comparable False
  2. [B2] ostile o contrario a qualcuno o a qualcosa
     - example: Da quella lite i due fratelli sono rimasti nemici per anni.
     - frames: nemico di qcn/qcs
     - opposites: amico

### 30. iniziare

**Lexeme 1: iniziare**, VERB
- grammar: conjugation 1; irregular False; valency Both; auxiliary both
- said: /i.nit.ˈtsja.re/ iniziàre (it-IT)
  1. [A1] compiere i primi passi di un’attività o mettersi a fare qualcosa
     - example: Dopo cena ho iniziato a leggere il tuo libro.
     - frames: iniziare qcs; iniziare a + inf
     - grammar: valency Both; auxiliary have
     - opposites: finire
  2. [A2] prendere avvio in un certo momento o luogo
     - example: La lezione è iniziata mentre cercavo un posto libero.
     - frames: qcs inizia; iniziare con qcs
     - grammar: valency Intr; auxiliary be
     - opposites: finire
  3. [C1] guidare qualcuno nei primi passi di una nuova attività
     - example: È stata mia zia a iniziarmi alla fotografia.
     - frames: iniziare qcn a qcs
     - grammar: valency Tr; auxiliary have
  4. [C1] ammettere qualcuno in un gruppo attraverso un rito
     - example: Gli anziani iniziavano i giovani ai misteri della loro comunità.
     - frames: iniziare qcn a qcs
     - collocations: iniziare ai misteri di un culto
     - grammar: valency Tr; auxiliary have

### 31. riuscire

**Lexeme 1: riuscire**, VERB
- grammar: conjugation 3; irregular True; valency Intr; auxiliary be
- said: /ri.uʃ.ˈʃi.re/ riuscìre (it-IT)
- forms: riesco [Mood=Ind, Tense=Pres, Person=1, Number=Sing] rièsco; riesci [Mood=Ind, Tense=Pres, Person=2, Number=Sing] rièsci; riesce [Mood=Ind, Tense=Pres, Person=3, Number=Sing] rièsce; riusciamo [Mood=Ind, Tense=Pres, Person=1, Number=Plur] riusciàmo; riescono [Mood=Ind, Tense=Pres, Person=3, Number=Plur] rièscono; riuscii [Mood=Ind, Tense=Past, Person=1, Number=Sing] riuscìi; riuscì [Mood=Ind, Tense=Past, Person=3, Number=Sing] riuscì; riuscirò [Mood=Ind, Tense=Fut, Person=1, Number=Sing] riuscirò; riesca [Mood=Sub, Tense=Pres, Person=1, Number=Sing] rièsca; riuscito [VerbForm=Part, Tense=Past] riuscìto
  1. [A2] essere capace di fare qualcosa, spesso superando una difficoltà
     - example: Con questo rumore non riesco a concentrarmi sul lavoro.
     - frames: riuscire a + inf; riuscire in qcs
     - collocations: non riuscire a dormire; riuscire nell'intento; riuscire a malapena
  2. [B1] avere un certo risultato, spesso buono, alla fine
     - example: La festa di ieri sera è riuscita proprio bene.
     - collocations: riuscire bene; riuscire male; ben riuscito
     - opposites: fallire
  3. [B2] ottenere buoni risultati in un'attività o in un campo
     - example: Mio figlio riesce molto bene in matematica e in fisica.
     - frames: riuscire in qcs
     - collocations: riuscire nella vita; riuscire negli studi
  4. [B2] apparire o risultare in un certo modo a qualcuno
     - example: Mi riesce difficile credere a quello che dici.
     - frames: riuscire + agg a qcn
     - collocations: riuscire simpatico; riuscire antipatico; riuscire difficile
- relations: derived_from uscire (VERB)

### 32. aumentare

**Lexeme 1: aumentare**, VERB
- grammar: conjugation 1; irregular False; valency Both; auxiliary both
- said: /aw.men.ˈta.re/ aumentàre (it-IT)
  1. [A2] rendere più grande per quantità, numero, misura o intensità
     - example: Il governo vuole aumentare le tasse sulle sigarette dal prossimo anno.
     - frames: aumentare qcs; aumentare qcs di qcs
     - collocations: aumentare i prezzi; aumentare lo stipendio; aumentare la velocità; aumentare il volume; aumentare le vendite; aumentare il rischio
     - grammar: valency Tr; auxiliary have
     - opposites: diminuire
  2. [A2] diventare più grande per quantità, numero, valore o intensità
     - example: In estate i prezzi degli alberghi al mare aumentano parecchio.
     - frames: qcs aumenta; aumentare di qcs
     - collocations: aumentare di peso; aumentare di prezzo; aumentare di valore; aumentare notevolmente; aumentare progressivamente
     - grammar: valency Intr; auxiliary be
     - opposites: diminuire

### 33. fenomeno

**Lexeme 1: fenomeno**, NOUN
- grammar: gender Masc; countability Count
- said: /fe.ˈnɔ.me.no/ fenòmeno (it-IT)
  1. [B1] fatto che si manifesta e può essere osservato o studiato
     - example: L’arcobaleno è un fenomeno che si vede spesso dopo un temporale.
  2. [B2] fatto o tendenza che assume particolare importanza nella società
     - example: Il lavoro da casa è ormai un fenomeno di massa.
  3. [B2] persona o cosa che stupisce per le sue qualità eccezionali
     - example: Mia sorella è un fenomeno con i numeri.
     - labels: formality informal, figurative True

### 34. minimo

**Lexeme 1: minimo**, ADJ
- grammar: comparable False; note superlative of piccolo
- said: /ˈmi.ni.mo/ mìnimo (it-IT)
  1. [B1] più piccolo o più basso tra quelli possibili, richiesti o consentiti
     - example: L’età minima per partecipare al corso è di sedici anni.
     - opposites: massimo
  2. [B2] molto piccolo, di poca importanza o quasi nullo
     - example: Tra le due versioni ci sono soltanto differenze minime.
     - collocations: non avere la minima idea
**Lexeme 2: minimo**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈmi.ni.mo/ mìnimo (it-IT)
  1. [B1] la quantità o il grado più basso possibile o richiesto
     - example: Per aprire il corso serve un minimo di otto iscritti.
     - frames: un minimo di qcs
     - collocations: come minimo; ridurre al minimo
     - grammar: countability Count
     - opposites: massimo
  2. [B2] regime di un motore acceso quando non si preme l’acceleratore
     - example: Al semaforo il motore non tiene il minimo e si spegne.
     - collocations: girare al minimo; tenere il minimo
     - grammar: countability Mass
- relations: derived_from minimo (ADJ)

### 35. dominio

**Lexeme 1: dominio**, NOUN
- grammar: gender Masc
- said: /do.ˈmi.njo/ domìnio (it-IT)
  1. [B2] potere e autorità esercitati su un popolo, un territorio o una persona
     - example: Per secoli la regione rimase sotto il dominio straniero.
     - frames: dominio su qcn/qcs; il dominio di qcn
     - collocations: sotto il dominio di; esercitare il dominio; liberarsi dal dominio; dominio coloniale
     - grammar: countability Mass
  2. [B2] capacità di controllare bene una cosa, una situazione o se stessi
     - example: Ammiro il suo perfetto dominio della lingua tedesca.
     - frames: dominio di qcs
     - collocations: dominio di sé; perdere il dominio di sé; avere il dominio della situazione; dominio del pallone
     - grammar: countability Mass
  3. [C1] territorio che si trova sotto il potere di uno Stato o di un sovrano
     - example: Il re visitava ogni anno i suoi domini oltre il mare.
     - labels: time historical
     - grammar: countability Count; number Plur, mainly
  4. [C1] campo di attività o di conoscenza, o sfera a cui qualcosa appartiene
     - example: Questi problemi escono dal dominio della semplice medicina.
     - collocations: di dominio pubblico
     - grammar: countability Count
  5. [B2] parte di un indirizzo internet che identifica un sito o una rete
     - example: Abbiamo registrato un nuovo dominio per il sito del negozio.
     - collocations: nome di dominio; registrare un dominio; acquistare un dominio
     - labels: domain computing
     - grammar: countability Count

### 36. fisico

**Lexeme 1: fisico**, ADJ
- grammar: comparable False
- said: /ˈfi.zi.ko/ fìsico (it-IT)
  1. [A2] proprio del corpo, distinto dalla mente e dallo spirito
     - example: Fare un po’ di attività fisica ogni giorno aiuta a dormire meglio.
     - collocations: attività fisica; dolore fisico; aspetto fisico; contatto fisico; forma fisica; sforzo fisico; educazione fisica
     - opposites: psichico
  2. [B2] della materia e della natura, e delle leggi che le governano
     - example: Il passaggio dell’acqua da liquido a vapore è un fenomeno fisico.
     - collocations: geografia fisica; leggi fisiche; fenomeno fisico; mondo fisico
**Lexeme 2: fisico**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈfi.zi.ko/ fìsico (it-IT)
  1. [B1] insieme delle forme e della forza del corpo di una persona
     - example: Per fare questo lavoro serve un fisico robusto e resistente.
     - collocations: avere un bel fisico; fisico atletico; fisico asciutto; mantenere il fisico; non avere più il fisico
  2. [B1] scienziato che studia la materia, l’energia e le leggi della natura
     - example: Da bambino sognavo di diventare un fisico e studiare le stelle.
     - collocations: fisico nucleare; fisico teorico
     - grammar: note a woman is una fisica
- relations: derived_from fisica (NOUN)

### 37. ristorante

**Lexeme 1: ristorante**, NOUN
- grammar: gender Masc; countability Count
- said: /ri.sto.ˈran.te/ ristorànte (it-IT)
  1. [A1] locale pubblico dove si mangiano pasti preparati e serviti a pagamento
     - example: Stasera ceniamo in un piccolo ristorante vicino al mare.
     - collocations: andare al ristorante; mangiare al ristorante; prenotare al ristorante; ristorante di pesce; ristorante stellato; ristorante tipico

### 38. comunale

**Lexeme 1: comunale**, ADJ
- grammar: comparable False
- said: /ko.mu.ˈna.le/ comunàle (it-IT)
  1. [B1] che appartiene all’amministrazione di una città o di un paese, o ne dipende
     - example: Ogni sabato studio nella biblioteca comunale con i miei compagni.
     - collocations: consiglio comunale; consigliere comunale; giunta comunale; elezioni comunali; palazzo comunale; biblioteca comunale; piscina comunale; polizia comunale
  2. [C1] delle città italiane che nel Medioevo si governavano da sole
     - example: Nell’età comunale molte città italiane diventarono ricche e potenti.
     - collocations: età comunale; Italia comunale
     - labels: domain history
- relations: adjective_of comune (NOUN)
**Lexeme 2: comunale**, NOUN
- grammar: gender Fem; countability Count
- said: /ko.mu.ˈna.le/ comunàle (it-IT)
  1. [B2] elezioni per scegliere il sindaco e il consiglio di una città
     - example: Alle prossime comunali voteranno molti giovani per la prima volta.
     - collocations: vincere le comunali; andare alle comunali
     - grammar: number Plur
- relations: derived_from comunale (ADJ)

### 39. biglietto

**Lexeme 1: biglietto**, NOUN
- grammar: gender Masc; countability Count
- said: /biʎ.ˈʎet.to/ bigliétto (it-IT)
  1. [A1] tagliando che permette di viaggiare o di entrare in un luogo o a uno spettacolo
     - example: Ricordati di timbrare il biglietto prima di salire sul treno.
     - frames: biglietto per qcs; biglietto per + luogo
     - collocations: fare il biglietto; timbrare il biglietto; obliterare il biglietto; biglietto di andata e ritorno; biglietto di sola andata; comprare il biglietto; biglietto ridotto; biglietto della lotteria
  2. [A2] breve messaggio scritto su un piccolo foglio o su un cartoncino
     - example: Ti ho lasciato un biglietto sul tavolo della cucina.
     - collocations: biglietto d'auguri; biglietto da visita; lasciare un biglietto; scrivere un biglietto
  3. [B1] foglio di carta stampato dalla banca centrale e usato come denaro
     - example: Alla cassa ha pagato con un biglietto da cinquanta euro.
     - collocations: biglietto di banca; biglietto da cento euro; biglietto falso

### 40. divertente

**Lexeme 1: divertente**, ADJ
- grammar: comparable True
- said: /di.ver.ˈtɛn.te/ divertènte (it-IT)
  1. [A1] che fa ridere o fa passare il tempo in modo piacevole
     - example: È divertente giocare a carte con i nonni la domenica.
     - frames: è divertente + inf; trovare qcs divertente
     - collocations: trovare divertente; molto divertente; una serata divertente; un film divertente; una storia divertente; una persona divertente
     - opposites: noioso
- relations: derived_from divertire (VERB)

### 41. successivo

**Lexeme 1: successivo**, ADJ
- grammar: comparable False
- said: /sut.tʃes.ˈsi.vo/ successìvo (it-IT)
  1. [B1] che viene subito dopo, nel tempo o in un ordine
     - example: Il giorno successivo siamo partiti presto per la montagna.
     - frames: successivo a qcs
     - collocations: il giorno successivo; l'anno successivo; nei giorni successivi; la fase successiva; in un momento successivo
     - opposites: precedente
  2. [B2] che si presentano uno dopo l'altro, in momenti diversi
     - example: Il progetto è stato modificato in tre fasi successive.
     - collocations: in tempi successivi; per approssimazioni successive
     - grammar: note usually plural

### 42. spiegare

**Lexeme 1: spiegare**, VERB
- grammar: conjugation 1; irregular False; note adds h before e and i: spieghi, spieghiamo
- said: /spje.ˈɡa.re/ spiegàre (it-IT)
  1. [A2] rendere chiaro e comprensibile qualcosa a qualcuno con le parole
     - example: Il professore ci spiega la regola con molti esempi pratici.
     - frames: spiegare qcs a qcn; spiegare a qcn che + ind; spiegare perché + ind
     - collocations: spiegare la lezione; spiegare il motivo; spiegare chiaramente; spiegare nei dettagli; spiegare a parole
     - grammar: valency Tr; auxiliary have
  2. [B2] essere la ragione o la causa di un fatto
     - example: La stanchezza spiega il suo cattivo umore di stamattina.
     - frames: qcs spiega qcs
     - collocations: questo spiega tutto
     - grammar: valency Tr; auxiliary have
  3. [C1] aprire e distendere del tutto una cosa che era chiusa o raccolta
     - example: Il gabbiano spiegò le ali e si alzò sopra il mare.
     - collocations: spiegare le vele; spiegare le ali; spiegare una tovaglia
     - grammar: valency Tr; auxiliary have
     - opposites: piegare
  4. [B1] esprimersi in modo che gli altri capiscano bene
     - example: Scusa, forse prima non mi sono spiegato bene.
     - collocations: non so se mi spiego; spiegarsi meglio
     - grammar: valency Refl; reflexive True; auxiliary be
  5. [B2] riuscire a capire la causa o il senso di un fatto
     - example: Ancora non mi spiego come abbia potuto dimenticare le chiavi.
     - frames: spiegarsi qcs; spiegarsi perché + ind; spiegarsi come + cong
     - grammar: valency Refl; reflexive True; auxiliary be; note the pronoun means to oneself; mostly in negative sentences

### 43. morale

**Lexeme 1: morale**, ADJ
- said: /mo.ˈra.le/ moràle (it-IT)
  1. [B1] legato ai principi del bene e del male che guidano le azioni umane
     - example: Ogni giorno i medici affrontano scelte morali molto difficili.
     - collocations: valori morali; dovere morale; obbligo morale; giudizio morale; senso morale; questione morale; principi morali
     - grammar: comparable False
  2. [B2] conforme ai principi dell'onestà e del bene
     - example: Non mi sembra un comportamento molto morale da parte tua.
     - grammar: comparable True
     - opposites: immorale
  3. [B2] che tocca lo spirito e i sentimenti, non i beni materiali
     - example: In quei mesi gli amici mi hanno dato un grande sostegno morale.
     - collocations: sostegno morale; appoggio morale; danno morale; vittoria morale; vincitore morale
     - grammar: comparable False
     - opposites: materiale
**Lexeme 2: morale**, NOUN
- grammar: gender Fem
- said: /mo.ˈra.le/ moràle (it-IT)
  1. [B2] insieme dei principi su ciò che è giusto o sbagliato fare
     - example: Per la morale di quell'epoca, certi abiti erano scandalosi.
     - collocations: morale comune; morale corrente; contrario alla morale; morale cattolica
     - grammar: countability Mass, mainly
  2. [B1] insegnamento che si ricava da un racconto o da un fatto
     - example: La morale della favola è che non bisogna fidarsi di tutti.
     - frames: la morale di qcs
     - collocations: la morale della favola
     - grammar: countability Count
- relations: derived_from morale (ADJ)
**Lexeme 3: morale**, NOUN
- grammar: gender Masc; countability Mass
- said: /mo.ˈra.le/ moràle (it-IT)
  1. [B1] stato d'animo, fiducia ed energia di una persona o di un gruppo
     - example: Dopo la sconfitta, il morale della squadra era a terra.
     - collocations: giù di morale; morale alto; morale basso; morale a terra; tirare su il morale; risollevare il morale; sollevare il morale
- relations: derived_from morale (ADJ)

### 44. montagna

**Lexeme 1: montagna**, NOUN
- grammar: gender Fem; countability Count
- said: /mon.ˈtaɲ.ɲa/ montàgna (it-IT)
  1. [A1] grande rilievo del terreno che si alza molto sopra la zona intorno
     - example: Dalla finestra dell’albergo si vede una montagna coperta di neve.
     - collocations: scalare una montagna; catena di montagne; cima della montagna; ai piedi della montagna; montagna alta
  2. [A2] zona con molti rilievi alti, in contrasto con il mare o la pianura
     - example: Quest’estate preferisco andare in montagna invece che al mare.
     - collocations: andare in montagna; in montagna; vacanze in montagna; casa in montagna; aria di montagna
     - grammar: number Sing; note usually with the article or in "in montagna"
  3. [B1] grande quantità di cose ammucchiate o da fare
     - example: Sulla scrivania ho una montagna di documenti da firmare.
     - frames: una montagna di qcs
     - collocations: una montagna di soldi; una montagna di lavoro
     - labels: figurative True

### 45. senato

**Lexeme 1: senato**, NOUN
- grammar: gender Masc; countability Count
- said: /se.ˈna.to/ senàto (it-IT)
  1. [B1] una delle due assemblee che formano il parlamento in alcuni stati
     - example: Adesso la proposta di legge passa all'esame del Senato.
     - collocations: presidente del Senato; approvare al Senato; aula del Senato; voto del Senato; elezioni del Senato
     - grammar: note often written with a capital letter
  2. [B2] nell'antica Roma, assemblea di cittadini autorevoli che guidava lo stato
     - example: Il senato romano decideva spesso la guerra e la pace.
     - labels: time historical
