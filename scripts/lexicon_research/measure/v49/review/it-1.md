# Italian: entries to check (1 of 1)

These are entries of a learner's dictionary of Italian, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of Italian.

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

### 1. armadio

**Lexeme 1: armadio**, NOUN
- grammar: gender Masc; countability Count
- said: /ar.ˈma.djo/ armàdio (it-IT)
  1. [A1] mobile alto e chiuso da ante, dove si tengono vestiti o altri oggetti
     - example: Metti i maglioni puliti nell'armadio della camera da letto.
     - collocations: armadio a muro; armadio a due ante; anta dell'armadio; riordinare l'armadio; avere uno scheletro nell'armadio
  2. [C1] persona molto grossa e robusta
     - example: Il nuovo buttafuori del locale è un vero armadio.
     - labels: formality informal, figurative True

### 2. infelice

**Lexeme 1: infelice**, ADJ
- grammar: comparable True
- said: /in.fe.ˈli.tʃe/ infelìce (it-IT)
  1. [B1] che prova tristezza e non è contento della propria vita
     - example: Dopo il divorzio si sentiva sola e infelice.
     - collocations: rendere infelice; sentirsi infelice; profondamente infelice; matrimonio infelice; infanzia infelice
     - opposites: felice
  2. [B2] poco adatto alla situazione o riuscito male, con cattivo effetto
     - example: Ha fatto una battuta infelice durante la riunione.
     - collocations: battuta infelice; scelta infelice; frase infelice; momento infelice; posizione infelice
     - opposites: felice
**Lexeme 2: infelice**, NOUN
- grammar: gender Com; countability Count
- said: /in.fe.ˈli.tʃe/ infelìce (it-IT)
  1. [B2] persona che soffre molto o che ha avuto molta sfortuna
     - example: Quel povero infelice non aveva nemmeno una casa.
     - collocations: povero infelice
- relations: derived_from infelice (ADJ)

### 3. oppure

**Lexeme 1: oppure**, CONJ
- said: /op.ˈpu.re/ oppùre (it-IT)
- parts: o (o) + ppure (pure)
  1. [A1] usato per presentare un'altra possibilità tra cui scegliere
     - example: Stasera possiamo andare al cinema, oppure restare a casa.
  2. [B1] usato per indicare che cosa succede se una cosa non avviene
     - example: Sbrigati, oppure perderemo l'ultimo autobus della sera.

### 4. quattro

**Lexeme 1: quattro**, NUM
- grammar: invariable True
- said: /ˈkwat.tro/ quàttro (it-IT)
  1. [A1] numero intero che viene dopo il tre e prima del cinque
     - example: Nella nostra famiglia siamo in quattro: i genitori e due figli.
     - collocations: farsi in quattro; a quattr'occhi; in quattro e quattr'otto; a quattro zampe
  2. [B1] numero piccolo e non preciso di persone o cose
     - example: Dopo cena usciamo a fare quattro passi in centro.
     - collocations: fare quattro passi; fare quattro chiacchiere; quattro soldi; quattro gatti
     - labels: formality informal, figurative True
**Lexeme 2: quattro**, NOUN
- grammar: gender Masc; countability Count; invariable True
- said: /ˈkwat.tro/ quàttro (it-IT)
  1. [A2] la cifra o il segno che rappresenta il numero tre più uno
     - example: Sul biglietto c'era scritto un quattro quasi illeggibile.
  2. [B1] voto scolastico che indica un risultato gravemente insufficiente
     - example: Se prendo un altro quattro in latino, i miei si arrabbiano.
     - collocations: prendere un quattro
- relations: derived_from quattro (NUM)

### 5. rottura

**Lexeme 1: rottura**, NOUN
- grammar: gender Fem; countability Count
- said: /rot.ˈtu.ra/ rottùra (it-IT)
  1. [B1] danno di una cosa o di una parte del corpo che si spezza o non funziona più
     - example: La rottura di un tubo ha allagato tutta la cantina.
     - frames: rottura di qcs
     - collocations: rottura del legamento; rottura del menisco; in caso di rottura; rottura di un tubo
  2. [B2] fine improvvisa di un rapporto, di un accordo o di un legame
     - example: Dopo la rottura con il suo ragazzo, è tornata a vivere dai genitori.
     - frames: rottura con qcn/qcs; rottura tra qcn e qcn
     - collocations: rottura delle trattative; rottura con il passato; arrivare alla rottura; punto di rottura; rottura definitiva
  3. [B2] cosa o persona noiosa e molto fastidiosa
     - example: Che rottura dover rifare tutto il lavoro da capo!
     - collocations: che rottura; rottura di scatole
     - labels: formality informal
- relations: verbal_noun_of rompere (VERB)

### 6. ascoltare

**Lexeme 1: ascoltare**, VERB
- grammar: conjugation 1; irregular False; auxiliary have; valency Both
- said: /as.kol.ˈta.re/ ascoltàre (it-IT)
  1. [A1] fare attenzione a suoni, parole o musica per sentirli bene
     - example: La sera mi piace ascoltare la musica alla radio.
     - frames: ascoltare qcn/qcs
     - collocations: ascoltare la musica; ascoltare la radio; stare ad ascoltare; ascoltare attentamente; ascoltare in silenzio; ascoltare con attenzione
     - grammar: valency Both
  2. [A2] seguire i consigli, gli ordini o le richieste di qualcuno
     - example: Se mi avessi ascoltato, adesso non saresti in questa situazione.
     - frames: ascoltare qcn/qcs
     - collocations: ascoltare i consigli; ascoltare la propria coscienza; ascoltare il proprio cuore
     - grammar: valency Tr
- relations: confusable sentire (VERB): sentire is to hear without trying; ascoltare means paying attention

### 7. football

**Lexeme 1: football**, NOUN
- grammar: gender Masc; number Sing; countability Mass; invariable True
- said: /ˈfut.bol/ (it-IT)
  1. [B2] sport tra due squadre di undici giocatori che cercano di segnare con un pallone
     - example: Il football italiano ha vinto molti trofei importanti in Europa.
     - collocations: giocare a football; partita di football; squadra di football; appassionato di football
  2. [B2] sport americano con una palla ovale, giocato da squadre con caschi e protezioni
     - example: Mio cugino gioca a football americano in una squadra universitaria.
     - collocations: football americano; giocare a football
     - grammar: note usually in the phrase «football americano»

### 8. professionale

**Lexeme 1: professionale**, ADJ
- grammar: note one form for masculine and feminine
- said: /pro.fes.sjo.ˈna.le/ professionàle (it-IT)
  1. [B1] che riguarda il lavoro che una persona fa per vivere
     - example: Ha molta esperienza professionale nel campo della vendita.
     - collocations: formazione professionale; esperienza professionale; istituto professionale; segreto professionale; ordine professionale; carriera professionale; vita professionale; aggiornamento professionale
     - grammar: comparable False; position Attr
  2. [B1] fatto con serietà e competenza, come farebbe un vero esperto
     - example: Il nuovo cameriere si è comportato in modo molto professionale.
     - collocations: in modo professionale; comportamento professionale; atteggiamento professionale; lavoro professionale
     - grammar: comparable True
  3. [B2] pensato per chi usa un oggetto per lavoro, non per hobby
     - example: Per il matrimonio hanno usato una macchina fotografica professionale.
     - collocations: uso professionale; attrezzatura professionale; macchina fotografica professionale
     - grammar: comparable False; position Attr, mainly
- relations: adjective_of professione (NOUN)

### 9. polemica

**Lexeme 1: polemica**, NOUN
- grammar: gender Fem; countability Both
- said: /po.ˈlɛ.mi.ka/ polèmica (it-IT)
  1. [B2] discussione accesa e pubblica tra persone con opinioni opposte
     - example: La decisione del governo ha provocato molte polemiche sui giornali.
     - frames: polemica su qcs; polemica con qcn; polemica tra qcn
     - collocations: suscitare polemiche; scatenare una polemica; sollevare una polemica; alimentare la polemica; accesa polemica; in polemica con; al centro delle polemiche
     - grammar: countability Count
  2. [B2] tendenza a criticare e contestare tutto, anche senza un vero motivo
     - example: Non voglio fare polemica, ma il servizio era davvero lento.
     - collocations: fare polemica; senza polemica; per polemica; spirito di polemica
     - grammar: countability Mass
- relations: derived_from polemico (ADJ)

### 10. busto

**Lexeme 1: busto**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈbu.sto/ bùsto (it-IT)
  1. [B1] parte superiore del corpo umano, dal collo alla vita
     - example: Durante l’esercizio tieni il busto dritto e le gambe ferme.
     - collocations: piegare il busto; ruotare il busto; a mezzo busto; busto eretto
  2. [B2] scultura che rappresenta testa, collo e parte alta del petto
     - example: Nella piazza c’è un busto di marmo dedicato al fondatore della città.
     - collocations: busto di marmo; busto di bronzo
  3. [C1] indumento rigido che stringe o sostiene il tronco
     - example: Dopo l’operazione alla schiena dovrà portare il busto per due mesi.
     - collocations: busto ortopedico; portare il busto

### 11. amichevole

**Lexeme 1: amichevole**, ADJ
- grammar: comparable True, mainly
- said: /a.mi.ˈke.vo.le/ amichévole (it-IT)
  1. [B1] che mostra cordialità, simpatia e disponibilità verso gli altri
     - example: Il nuovo vicino mi ha salutato con un sorriso amichevole.
     - frames: amichevole con qcn
     - collocations: in via amichevole; accordo amichevole; tono amichevole; rapporti amichevoli; consiglio amichevole; atmosfera amichevole
     - opposites: ostile
  2. [B2] di incontro sportivo che non conta per un campionato o un torneo
     - example: Stasera la nazionale gioca una partita amichevole contro una squadra straniera.
     - collocations: partita amichevole; incontro amichevole
     - grammar: position Attr, mainly; comparable False
     - opposites: ufficiale
- relations: adjective_of amico (NOUN)
**Lexeme 2: amichevole**, NOUN
- grammar: gender Fem; countability Count
- said: /a.mi.ˈke.vo.le/ amichévole (it-IT)
  1. [B2] incontro sportivo che non vale per la classifica di un torneo
     - example: Prima del campionato la squadra giocherà tre amichevoli all’estero.
     - collocations: giocare un'amichevole; amichevole internazionale; amichevole precampionato
- relations: derived_from amichevole (ADJ)

### 12. contadino

**Lexeme 1: contadino**, NOUN
- grammar: gender Masc; countability Count; note feminine contadina
- said: /kon.ta.ˈdi.no/ contadìno (it-IT)
  1. [A2] persona che lavora la terra e la coltiva, per sé o per altri
     - example: Mio nonno faceva il contadino e coltivava grano e viti.
     - collocations: fare il contadino; figlio di contadini; piccolo contadino; contadini poveri
  2. [B2] persona dai modi rozzi e poco educati
     - example: Non mangiare con le mani, sembri proprio un contadino!
     - labels: formality informal, attitude derogatory, figurative True
- relations: derived_from contado (NOUN)
**Lexeme 2: contadino**, ADJ
- grammar: comparable False
- said: /kon.ta.ˈdi.no/ contadìno (it-IT)
  1. [B1] proprio di chi lavora la terra e della vita di campagna
     - example: La cultura contadina ha lasciato molte tradizioni nei nostri paesi.
     - collocations: civiltà contadina; mondo contadino; cultura contadina; famiglia contadina; origini contadine

### 13. spada

**Lexeme 1: spada**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈspa.da/ spàda (it-IT)
  1. [B1] arma formata da una lunga lama di metallo con un'impugnatura
     - example: Il cavaliere estrasse la spada e si preparò a combattere.
     - collocations: sguainare la spada; impugnare la spada; colpo di spada; incrociare le spade; passare a fil di spada; spada di Damocle
  2. [B2] uno dei quattro semi delle carte da gioco italiane
     - example: Mi mancava solo l'asso di spade per vincere la partita.
     - collocations: asso di spade; re di spade
     - grammar: number Plur

### 14. apposito

**Lexeme 1: apposito**, ADJ
- grammar: comparable False; note often placed before the noun
- said: /ap.ˈpɔ.zi.to/ appòsito (it-IT)
  1. [B2] fatto, scelto o destinato per uno scopo preciso
     - example: Getta le bottiglie di plastica negli appositi contenitori.
     - collocations: l'apposito modulo; l'apposito spazio; apposita commissione; negli appositi contenitori

### 15. qualunque

**Lexeme 1: qualunque**, DET
- grammar: invariable True; note used only with singular nouns
- said: /kwa.ˈlun.kwe/ qualùnque (it-IT)
  1. [A2] indica una persona o una cosa, non importa quale, tra tutte quelle possibili
     - example: Puoi chiamarmi a qualunque ora, anche nel cuore della notte.
     - frames: qualunque + nome; qualunque + nome + che + cong
     - collocations: qualunque cosa; a qualunque costo; in qualunque momento; in qualunque caso; a qualunque ora; in qualunque modo
     - grammar: note usually before the noun; with che + subjunctive it means whatever, whichever
  2. [B1] comune, uguale a tanti altri, senza niente di speciale
     - example: Non voglio un regalo qualunque, cerco qualcosa di davvero speciale.
     - frames: un + nome + qualunque
     - collocations: un uomo qualunque; un giorno qualunque; una persona qualunque
     - grammar: note after the noun, usually with the indefinite article

### 16. indirettamente

**Lexeme 1: indirettamente**, ADV
- grammar: invariable True
- said: /in.di.ret.ta.ˈmen.te/ indirettaménte (it-IT)
  1. [B2] attraverso un intermediario o altri passaggi, non in modo immediato
     - example: La crisi economica ha colpito indirettamente anche molte piccole famiglie.
     - collocations: coinvolto indirettamente; indirettamente collegato; influenzare indirettamente; indirettamente responsabile
     - opposites: direttamente
- relations: adverb_of indiretto (ADJ)

### 17. creatura

**Lexeme 1: creatura**, NOUN
- grammar: gender Fem; countability Count
- said: /kre.a.ˈtu.ra/ creatùra (it-IT)
  1. [B1] ogni essere che vive, uomo o animale
     - example: Nel mare profondo vivono creature che nessuno ha mai visto.
     - collocations: creatura vivente; creature marine; creatura di Dio; creatura misteriosa
  2. [B2] persona, spesso un bambino piccolo, vista con tenerezza o compassione
     - example: Quella povera creatura piangeva da ore senza fermarsi.
     - collocations: povera creatura; creatura indifesa
     - labels: attitude endearing
  3. [C1] opera o progetto ideato e seguito con cura da una persona
     - example: Il festival è la sua creatura e lo segue in ogni dettaglio.
     - labels: figurative True

### 18. uovo

**Lexeme 1: uovo**, NOUN
- grammar: gender Masc; countability Count; note the plural uova is feminine
- said: /ˈwɔ.vo/ uòvo (it-IT)
- forms: uova [Number=Plur, Gender=Fem] /ˈwɔ.va/ uòva
  1. [A1] corpo tondeggiante con guscio deposto da uccelli e altri animali, spesso usato come cibo
     - example: Per fare questa torta servono tre uova e un po' di zucchero.
     - collocations: uovo sodo; uovo alla coque; uovo al tegamino; uova strapazzate; sbattere le uova; rompere un uovo; deporre le uova; tuorlo d'uovo; guscio d'uovo; uovo di Pasqua

### 19. offendere

**Lexeme 1: offendere**, VERB
- grammar: conjugation 2; irregular True
- said: /of.ˈfɛn.de.re/ offèndere (it-IT)
- forms: offendo [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] offèndo; offendi [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] offèndi; offende [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] offènde; offendiamo [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] offendiàmo; offendono [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] offèndono; offesi [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] offési; offese [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] offése; offenderò [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] offenderò; offenda [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] offènda; offeso [Tense=Past, VerbForm=Part, Gender=Masc, Number=Sing] offéso
  1. [B1] ferire qualcuno nei sentimenti o nella dignità con parole o comportamenti
     - example: Scusa, non volevo offenderti con quella battuta sul tuo lavoro.
     - frames: offendere qcn
     - collocations: offendere profondamente; offendere gravemente; sentirsi offeso; offendere la memoria di qcn
     - grammar: valency Tr; auxiliary have
  2. [B1] sentirsi ferito o risentito per parole o comportamenti altrui
     - example: Mio fratello si offende per ogni piccola critica.
     - frames: offendersi per qcs; offendersi con qcn
     - collocations: offendersi facilmente; offendersi a morte
     - grammar: valency Refl; reflexive True; auxiliary be
  3. [C1] andare contro un principio, una regola o un senso comune
     - example: Certe pubblicità offendono il buon gusto e l’intelligenza del pubblico.
     - frames: offendere qcs
     - collocations: offendere il pudore; offendere il buon gusto; offendere l’intelligenza; offendere la vista; offendere la morale
     - grammar: valency Tr; auxiliary have
  4. [C2] danneggiare fisicamente una parte del corpo
     - example: Per fortuna il colpo non ha offeso organi vitali.
     - labels: domain medicine
     - grammar: valency Tr; auxiliary have

### 20. chic

**Lexeme 1: chic**, ADJ
- grammar: invariable True; comparable True; note loanword from French
- said: /ˈʃik/ chic (it-IT)
  1. [B1] elegante e raffinato in modo disinvolto e alla moda
     - example: Per la cena di stasera ho scelto un ristorante piuttosto chic.
     - collocations: molto chic; un locale chic; un abito chic; una signora chic
     - opposites: pacchiano
**Lexeme 2: chic**, NOUN
- grammar: gender Masc; countability Mass; invariable True
- said: /ˈʃik/ chic (it-IT)
  1. [B2] eleganza raffinata e disinvolta nel vestire o nei modi
     - example: Quel cappellino dà un tocco di chic a tutto il vestito.
     - collocations: fare chic; avere dello chic; un tocco di chic
- relations: derived_from chic (ADJ)

### 21. istituto

**Lexeme 1: istituto**, NOUN
- grammar: gender Masc; countability Count
- said: /i.sti.ˈtu.to/ istitùto (it-IT)
  1. [B1] ente pubblico o privato che svolge attività di studio, cura, assistenza o servizio
     - example: Lavora da anni in un istituto di ricerca sul clima.
     - collocations: istituto di ricerca; istituto di credito; istituto bancario; istituto di bellezza; istituto di statistica; fondare un istituto
  2. [A2] scuola, soprattutto di livello secondario superiore
     - example: Mio fratello frequenta un istituto tecnico vicino alla stazione.
     - collocations: istituto tecnico; istituto professionale; istituto alberghiero; istituto d'arte; istituto comprensivo; preside dell'istituto
  3. [C1] insieme di norme che regolano un rapporto o una realtà della vita sociale
     - example: Nel corso dei secoli l'istituto del matrimonio è molto cambiato.
     - frames: l'istituto di qcs
     - collocations: istituto giuridico
     - labels: domain law

### 22. nozze

**Lexeme 1: nozze**, NOUN
- grammar: gender Fem; number Plur
- said: /ˈnɔt.tse/ nòzze (it-IT)
  1. [B1] celebrazione del matrimonio, con il rito e la festa che lo accompagnano
     - example: Le nozze si terranno a giugno in una piccola chiesa di campagna.
     - collocations: viaggio di nozze; convolare a nozze; in seconde nozze; nozze d'oro; nozze d'argento; pranzo di nozze; lista di nozze; celebrare le nozze; andare a nozze

### 23. ossia

**Lexeme 1: ossia**, CONJ
- said: /os.ˈsi.a/ ossìa (it-IT)
- parts: o (o) + sia (essere)
  1. [B2] usato per introdurre una spiegazione o un modo più chiaro di dire la stessa cosa
     - example: Partiamo fra tre giorni, ossia giovedì mattina presto.
     - labels: formality formal
  2. [C1] usato per correggere o precisare meglio quanto si è appena detto
     - example: Ti telefono stasera, ossia domani, perché stasera esco tardi.

### 24. canone

**Lexeme 1: canone**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈka.no.ne/ cànone (it-IT)
  1. [B1] somma che si paga a scadenze fisse per usare un bene o un servizio
     - example: Ogni mese verso il canone d'affitto al proprietario dell'appartamento.
     - collocations: pagare il canone; canone d'affitto; canone mensile; canone annuo; canone televisivo; equo canone; canone di locazione
  2. [B2] regola o criterio da seguire per giudicare o fare qualcosa
     - example: Quel palazzo rispetta tutti i canoni dell'architettura classica.
     - collocations: secondo i canoni; canoni estetici; rispettare i canoni; canoni di bellezza
     - grammar: note often in the plural
  3. [C1] insieme di testi riconosciuti come autentici, sacri o esemplari
     - example: Il canone biblico comprende libri scritti in epoche molto diverse.
     - collocations: canone biblico; canone letterario; canone occidentale
  4. [C1] composizione in cui più voci ripetono la stessa melodia entrando una dopo l'altra
     - example: I bambini hanno cantato un canone a tre voci alla festa.
     - collocations: cantare un canone; canone a due voci
     - labels: domain music

### 25. avvio

**Lexeme 1: avvio**, NOUN
- grammar: gender Masc; countability Count
- said: /av.ˈvi.o/ avvìo (it-IT)
  1. [B1] il momento in cui un'attività o un processo comincia
     - example: L'avvio dei lavori è previsto per la prossima primavera.
     - frames: avvio di qcs
     - collocations: dare avvio a; prendere avvio; in fase di avvio; avvio dei lavori; buon avvio; avvio lento
  2. [B1] messa in funzione di un motore, una macchina o un programma
     - example: Dopo l'avvio, il computer impiega qualche minuto a funzionare bene.
     - frames: avvio di qcs
     - collocations: avvio del computer; pulsante di avvio; avvio automatico; menu di avvio
- relations: verbal_noun_of avviare (VERB)

### 26. federazione

**Lexeme 1: federazione**, NOUN
- grammar: gender Fem; countability Count
- said: /fe.de.rat.ˈtsjo.ne/ federazióne (it-IT)
  1. [B2] unione di più stati con un governo centrale comune ma una certa autonomia
     - example: Dopo la guerra, le piccole repubbliche decisero di unirsi in una federazione.
     - frames: federazione di qcs
     - collocations: federazione di stati; entrare nella federazione; uscire dalla federazione
  2. [B2] associazione che riunisce più società, gruppi o organizzazioni dello stesso tipo
     - example: Ogni squadra del campionato deve essere iscritta alla federazione nazionale.
     - collocations: federazione sportiva; federazione sindacale; presidente della federazione; iscriversi alla federazione; federazione calcistica

### 27. suggerimento

**Lexeme 1: suggerimento**, NOUN
- grammar: gender Masc; countability Count
- said: /sud.dʒe.ri.ˈmen.to/ suggeriménto (it-IT)
  1. [B1] idea o consiglio che si dà a qualcuno su cosa fare
     - example: Grazie per il tuo suggerimento, lo seguirò senz'altro.
     - frames: suggerimento su qcs; suggerimento per qcs
     - collocations: su suggerimento di; dare un suggerimento; seguire un suggerimento; accogliere un suggerimento; chiedere un suggerimento; un utile suggerimento; accettare un suggerimento
  2. [B1] piccolo aiuto o indizio per trovare una risposta o ricordare qualcosa
     - example: Non so la risposta, mi dai almeno un suggerimento?
     - collocations: dare un suggerimento; chiedere un suggerimento; un piccolo suggerimento
- relations: verbal_noun_of suggerire (VERB); confusable suggestione (NOUN): looks like English 'suggestion', but means emotional influence or fascination

### 28. percezione

**Lexeme 1: percezione**, NOUN
- grammar: gender Fem; countability Both
- said: /per.tʃet.ˈtsjo.ne/ percezióne (it-IT)
  1. [B2] processo con cui la mente riconosce ciò che arriva dai sensi
     - example: Il buio rende più difficile la percezione delle distanze.
     - frames: percezione di qcs
     - collocations: percezione visiva; percezione sensoriale; percezione del dolore; percezione dei colori
  2. [B2] modo in cui qualcuno intende, valuta o avverte una realtà
     - example: Secondo i sondaggi, la percezione della sicurezza in città è peggiorata.
     - frames: percezione di qcs; la percezione che + ind
     - collocations: percezione del rischio; percezione comune; avere la percezione; percezione distorta; percezione diffusa

### 29. tranne

**Lexeme 1: tranne**, ADP
- grammar: invariable True
- said: /ˈtran.ne/ trànne (it-IT)
  1. [A2] usato per escludere qualcuno o qualcosa da ciò che si dice
     - example: Il negozio resta aperto tutti i giorni tranne la domenica.
     - frames: tranne qcn/qcs
     - collocations: tutti tranne uno; tutto tranne; nessuno tranne; sempre tranne

### 30. stanotte

**Lexeme 1: stanotte**, ADV
- said: /sta.ˈnɔt.te/ stanòtte (it-IT)
  1. [A2] nelle ore buie di oggi, dopo questa sera o adesso
     - example: Stanotte dormo da mia nonna perché i miei genitori sono via.
  2. [A2] nelle ore buie appena passate, prima di questa mattina
     - example: Stanotte ho sognato di volare sopra il mare.

### 31. umidità

**Lexeme 1: umidità**, NOUN
- grammar: gender Fem; countability Mass; invariable True
- said: /u.mi.di.ˈta/ umidità (it-IT)
  1. [B1] quantità di vapore acqueo presente nell'aria
     - example: D'estate in città l'umidità rende il caldo difficile da sopportare.
     - collocations: tasso di umidità; umidità relativa; umidità elevata; alta umidità
  2. [B1] presenza di acqua in muri, oggetti o luoghi chiusi
     - example: Le pareti della cantina sono piene di macchie di umidità.
     - collocations: macchie di umidità; prendere umidità; temere l'umidità; umidità di risalita
- relations: abstract_noun_of umido (ADJ)

### 32. armonia

**Lexeme 1: armonia**, NOUN
- grammar: gender Fem
- said: /ar.mo.ˈni.a/ armonìa (it-IT)
  1. [B1] accordo e intesa tra persone o tra cose, senza contrasti
     - example: I miei vicini vivono in perfetta armonia da trent'anni.
     - frames: in armonia con qcn/qcs; armonia tra qcn/qcs
     - collocations: vivere in armonia; in perfetta armonia; essere in armonia con la natura; rompere l'armonia; armonia familiare
     - grammar: countability Mass, mainly
     - opposites: discordia
  2. [B2] equilibrio e giusta proporzione tra le parti di un insieme
     - example: Questa facciata colpisce per l'armonia delle sue forme.
     - collocations: armonia delle forme; armonia dei colori
     - grammar: countability Mass, mainly
     - opposites: disarmonia
  3. [C1] combinazione di suoni prodotti insieme e studio delle regole che la governano
     - example: Al conservatorio seguo un corso di armonia e composizione.
     - collocations: lezioni di armonia; trattato di armonia
     - labels: domain music
     - grammar: countability Both

### 33. crudele

**Lexeme 1: crudele**, ADJ
- grammar: comparable True
- said: /kru.ˈdɛ.le/ crudèle (it-IT)
  1. [B1] che fa soffrire gli altri senza pietà, a volte provandone piacere
     - example: Non essere crudele con il tuo cane, ha solo paura.
     - frames: crudele con qcn; crudele verso qcn
     - collocations: un tiranno crudele; un gesto crudele; uno scherzo crudele
  2. [B2] che provoca grande dolore o sofferenza
     - example: Perdere il lavoro a quell'età è stato un colpo crudele.
     - collocations: destino crudele; sorte crudele; morte crudele; ironia crudele; una verità crudele

### 34. pellicola

**Lexeme 1: pellicola**, NOUN
- grammar: gender Fem; countability Count
- said: /pel.ˈli.ko.la/ pellìcola (it-IT)
  1. [B1] strato molto sottile che copre una superficie o che si forma sopra qualcosa
     - example: Copri la ciotola con la pellicola prima di metterla in frigo.
     - collocations: pellicola trasparente; pellicola alimentare; pellicola protettiva; una sottile pellicola
  2. [B2] nastro sensibile alla luce su cui si registrano fotografie o immagini di un film
     - example: Mio nonno scattava ancora le foto su pellicola in bianco e nero.
     - collocations: pellicola fotografica; sviluppare la pellicola; girare in pellicola; pellicola in bianco e nero
  3. [B2] opera cinematografica, storia raccontata per immagini in movimento
     - example: Questa pellicola ha vinto tre premi al festival dell’anno scorso.
     - collocations: girare una pellicola
     - labels: style literary

### 35. nostro

**Lexeme 1: nostro**, DET
- grammar: note usually with the article; without it before a singular kinship noun (nostro padre)
- said: /ˈnɔs.tro/ nòstro (it-IT)
  1. [A1] che appartiene a noi o che ha un rapporto con noi
     - example: La nostra casa si trova a dieci minuti dalla stazione.
     - collocations: a nostro avviso; per conto nostro; da parte nostra; casa nostra; il nostro paese; nostro padre
**Lexeme 2: nostro**, PRON
- grammar: note used with the article: il nostro, la nostra, i nostri, le nostre
- said: /ˈnɔs.tro/ nòstro (it-IT)
  1. [A2] la persona o la cosa che appartiene a noi
     - example: La vostra macchina è molto più grande della nostra.
     - collocations: dire la nostra; essere dalla nostra
  2. [B1] le persone della famiglia, del gruppo o della parte a cui apparteniamo
     - example: Finalmente sono arrivati i nostri a darci una mano.
     - grammar: number Plur; note in the masculine plural, i nostri
  3. [C1] l'autore o il personaggio di cui si sta parlando
     - example: In questo romanzo il Nostro racconta la vita di provincia.
     - labels: style literary
     - grammar: note in the masculine singular, often with a capital: il Nostro

### 36. bosco

**Lexeme 1: bosco**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈbɔs.ko/ bòsco (it-IT)
- forms: boschi [Number=Plur] bòschi
  1. [A2] terreno ampio coperto di alberi, arbusti e piante selvatiche
     - example: Ogni domenica facciamo una lunga passeggiata nel bosco dietro casa.
     - collocations: frutti di bosco; fragole di bosco; bosco fitto; passeggiata nel bosco; perdersi nel bosco; bosco di castagni; funghi di bosco
  2. [C1] grande quantità di cose fitte e disordinate
     - example: Sul tavolo c'era un bosco di fogli e di cartelle.
     - frames: un bosco di qcs
     - labels: figurative True, style literary

### 37. tuta

**Lexeme 1: tuta**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈtu.ta/ tùta (it-IT)
  1. [A2] abito comodo di pantaloni e giacca, usato per fare sport o stare in casa
     - example: La domenica resto in tuta tutto il giorno sul divano.
     - collocations: tuta da ginnastica; tuta sportiva; stare in tuta; in tuta
  2. [B1] indumento intero che copre tutto il corpo, usato per lavorare o per protezione
     - example: Prima di entrare in officina, gli operai indossano la tuta.
     - collocations: tuta da lavoro; tuta blu; tuta spaziale; tuta da sub; tuta mimetica; indossare la tuta; tuta protettiva

### 38. mestiere

**Lexeme 1: mestiere**, NOUN
- grammar: gender Masc; countability Both
- said: /mes.ˈtjɛ.re/ mestière (it-IT)
  1. [A2] attività di lavoro, spesso pratica o manuale, che si fa per vivere
     - example: Mio nonno ha imparato il mestiere di falegname da ragazzo.
     - frames: fare il mestiere di qcs
     - collocations: fare un mestiere; imparare un mestiere; cambiare mestiere; di mestiere; i ferri del mestiere; i trucchi del mestiere; il mestiere più antico del mondo
     - grammar: countability Count
  2. [C1] abilità pratica che si acquista con una lunga esperienza
     - example: È un attore giovane, ma ha già molto mestiere.
     - collocations: avere mestiere; con mestiere
     - grammar: countability Mass

### 39. sotto

**Lexeme 1: sotto**, ADP
- said: /ˈsot.to/ sótto (it-IT)
  1. [A1] indica una posizione più bassa di qualcosa, o coperta da essa
     - example: Il gatto dorme sempre sotto il tavolo della cucina.
     - frames: sotto qcs; sotto a qcs; sotto di qcn
     - collocations: sotto la pioggia; sotto il sole; sotto terra; sotto il braccio; sotto i piedi
     - opposites: sopra
  2. [B1] indica un valore, un numero o un livello inferiore a un altro
     - example: Stanotte la temperatura scenderà sotto lo zero anche in città.
     - frames: sotto qcs
     - collocations: sotto zero; sotto la media; sotto il livello del mare
     - opposites: sopra
  3. [B2] indica dipendenza dall'autorità, dal controllo o dalla guida di qualcuno
     - example: Per molti secoli la regione è rimasta sotto un governo straniero.
     - frames: sotto qcn/qcs
     - collocations: sotto la guida di; sotto il controllo di; tenere sotto controllo; sotto la protezione di; sotto il regno di
  4. [B2] indica la condizione, l'effetto o la forma in cui qualcosa avviene
     - example: Lavoro sotto pressione da settimane e sono molto stanca.
     - frames: sotto qcs
     - collocations: sotto pressione; sotto l'effetto di; sotto forma di; sotto giuramento; sotto falso nome; sotto chiave
  5. [B2] indica un momento vicino, di solito poco prima di un evento
     - example: Sotto Natale i negozi del centro sono sempre pieni di gente.
     - frames: sotto qcs
     - collocations: sotto Natale; sotto gli esami; sotto le feste
**Lexeme 2: sotto**, ADV
- said: /ˈsot.to/ sótto (it-IT)
  1. [A2] in basso, in una posizione o a un piano più basso
     - example: Abito al primo piano e sotto c'è un piccolo bar.
     - collocations: di sotto; al piano di sotto; qui sotto; lì sotto; più sotto; sotto sotto
     - opposites: sopra
  2. [B2] in svantaggio nel punteggio di una gara o di una partita
     - example: Alla fine del primo tempo eravamo sotto di due gol.
     - frames: essere sotto di qcs
     - collocations: andare sotto; essere sotto

### 40. personalmente

**Lexeme 1: personalmente**, ADV
- grammar: invariable True
- said: /per.so.nal.ˈmen.te/ personalménte (it-IT)
  1. [B1] in modo diretto, da soli, senza mandare altri al proprio posto
     - example: Il direttore mi ha telefonato personalmente per farmi gli auguri.
     - collocations: conoscere personalmente; occuparsi personalmente; intervenire personalmente; ringraziare personalmente; consegnare personalmente
  2. [B1] secondo il proprio parere, per quanto riguarda chi parla
     - example: Personalmente, preferisco passare le vacanze in montagna che al mare.
     - grammar: note usually at the start of the sentence, often followed by a comma
- relations: adverb_of personale (ADJ)

### 41. lino

**Lexeme 1: lino**, NOUN
- grammar: gender Masc; countability Mass, mainly
- said: /ˈli.no/ lìno (it-IT)
  1. [B1] tessuto fresco e resistente fatto con le fibre di una pianta
     - example: D'estate preferisco indossare camicie di lino perché sono fresche.
     - collocations: camicia di lino; di puro lino; lenzuola di lino; tovaglia di lino; pantaloni di lino
     - grammar: countability Mass
  2. [B2] pianta con piccoli fiori azzurri, coltivata per le fibre e i semi
     - example: Nei campi intorno al paese un tempo si coltivava il lino.
     - collocations: semi di lino; olio di lino; coltivare il lino
     - labels: domain botany

### 42. per

**Lexeme 1: per**, ADP
- said: /ˈper/ per (it-IT)
  1. [A1] indica lo scopo di un’azione o la persona a cui qualcosa è destinato
     - example: Ho comprato questi fiori per mia madre.
     - frames: per qcn/qcs; per + inf
     - collocations: troppo stanco per uscire; fatto apposta per
  2. [A1] indica la meta verso cui ci si muove o si parte
     - example: Domani mattina partiamo per la montagna con gli amici.
     - frames: per + luogo
     - collocations: partire per; il treno per; in partenza per
  3. [A1] indica il tempo che dura un’azione o una situazione
     - example: Ho vissuto in quella casa per dieci anni.
     - frames: per + tempo
     - collocations: per sempre; per ora; per un po’; per tutta la vita
  4. [A2] indica il luogo attraverso cui si passa o in cui ci si muove
     - example: Mi piace passeggiare la sera per le vie del centro.
     - frames: per + luogo
     - collocations: per strada; cadere per terra; passare per; in giro per
  5. [A2] indica il mezzo o il modo con cui si fa qualcosa
     - example: Ti mando i documenti per posta elettronica entro stasera.
     - collocations: per telefono; per posta; per iscritto; per esempio; per scherzo
  6. [A2] indica la persona secondo il cui giudizio vale quanto si dice
     - example: Per me questo film è davvero troppo lungo.
     - frames: per qcn
     - collocations: per me; per quanto mi riguarda
  7. [A2] indica una moltiplicazione o una divisione in parti uguali
     - example: Tre moltiplicato quattro fa dodici, ma bisogna ricordarselo.
     - collocations: moltiplicare per; per cento; uno per volta; giorno per giorno
  8. [B1] indica la causa o il motivo di qualcosa
     - example: Non è uscito di casa per paura del temporale.
     - frames: per qcs; per + inf
     - collocations: per colpa di; per caso; per fortuna; per questo; per motivi di salute
  9. [B1] indica il prezzo o ciò che si dà in cambio
     - example: Ha venduto la sua vecchia bicicletta per cinquanta euro.
     - frames: per + prezzo

### 43. turistico

**Lexeme 1: turistico**, ADJ
- said: /tu.ˈri.sti.ko/ turìstico (it-IT)
  1. [A2] che riguarda i viaggi fatti per piacere e le persone che li fanno
     - example: Abbiamo prenotato una settimana in un villaggio turistico al mare.
     - collocations: villaggio turistico; guida turistica; agenzia turistica; località turistica; visto turistico; classe turistica; menu turistico; offerta turistica; flussi turistici
     - grammar: comparable False; position Attr, mainly
  2. [B1] frequentato da molte persone in vacanza o pensato soprattutto per loro
     - example: Questa spiaggia è diventata troppo turistica per i miei gusti.
     - collocations: molto turistico; zona turistica
     - grammar: comparable True
- relations: adjective_of turismo (NOUN)

### 44. emergere

**Lexeme 1: emergere**, VERB
- grammar: conjugation 2; irregular True; auxiliary be; valency Intr
- said: /e.ˈmɛr.dʒe.re/ emèrgere (it-IT)
- forms: emergo [VerbForm=Fin, Mood=Ind, Tense=Pres, Person=1, Number=Sing] /e.ˈmɛr.ɡo/ emèrgo; emergi [VerbForm=Fin, Mood=Ind, Tense=Pres, Person=2, Number=Sing] /e.ˈmɛr.dʒi/ emèrgi; emerge [VerbForm=Fin, Mood=Ind, Tense=Pres, Person=3, Number=Sing] /e.ˈmɛr.dʒe/ emèrge; emergiamo [VerbForm=Fin, Mood=Ind, Tense=Pres, Person=1, Number=Plur] /e.mer.ˈdʒa.mo/ emergiàmo; emergono [VerbForm=Fin, Mood=Ind, Tense=Pres, Person=3, Number=Plur] /e.ˈmɛr.ɡo.no/ emèrgono; emersi [VerbForm=Fin, Mood=Ind, Tense=Past, Person=1, Number=Sing] /e.ˈmɛr.si/ emèrsi; emerse [VerbForm=Fin, Mood=Ind, Tense=Past, Person=3, Number=Sing] /e.ˈmɛr.se/ emèrse; emergerò [VerbForm=Fin, Mood=Ind, Tense=Fut, Person=1, Number=Sing] /e.mer.dʒe.ˈrɔ/ emergerò; emerga [VerbForm=Fin, Mood=Sub, Tense=Pres, Person=1, Number=Sing] /e.ˈmɛr.ɡa/ emèrga; emerso [VerbForm=Part, Tense=Past, Gender=Masc, Number=Sing] /e.ˈmɛr.so/ emèrso
  1. [B2] diventare noto o chiaro, detto di un fatto o di un problema
     - example: Dalle indagini è emerso un dettaglio molto importante.
     - frames: emergere da qcs; emerge che + ind
     - collocations: emergere la verità; emergere dalle indagini; emergere un problema; far emergere
  2. [B2] uscire da un liquido e salire fino alla superficie
     - example: Il sottomarino è emerso lentamente in mezzo al mare.
     - frames: emergere da qcs
     - collocations: emergere dall'acqua; emergere in superficie
  3. [C1] distinguersi dagli altri per qualità o capacità migliori
     - example: Tra tutti gli studenti, lei emerge per la sua intelligenza.
     - frames: emergere tra qcn; emergere per qcs
     - collocations: emergere nel proprio campo

### 45. maestro

**Lexeme 1: maestro**, NOUN
- grammar: gender Masc; countability Count; note feminine maestra, plural maestre
- said: /ma.ˈɛs.tro/ maèstro (it-IT)
  1. [A1] persona che insegna ai bambini nella scuola elementare
     - example: Il maestro ha spiegato le tabelline a tutta la classe.
     - collocations: maestro elementare; maestro di scuola; maestro di sostegno
  2. [A2] persona che insegna un'arte, uno sport o una disciplina
     - example: Ogni sabato prendo lezioni di pianoforte da un vecchio maestro.
     - frames: maestro di qcs
     - collocations: maestro di sci; maestro di musica; maestro di nuoto; maestro di cerimonie; maestro di vita
  3. [B1] persona molto brava ed esperta in un'attività o in un'arte
     - example: Quel pittore è considerato un maestro del Rinascimento italiano.
     - frames: maestro di qcs
     - collocations: colpo da maestro; da maestro; grande maestro; antichi maestri
  4. [B2] direttore d'orchestra; titolo di rispetto per musicisti e compositori
     - example: Il pubblico ha applaudito il maestro alla fine del concerto.
     - collocations: maestro d'orchestra; maestro del coro; maestro concertatore
     - labels: domain music
**Lexeme 2: maestro**, ADJ
- grammar: position Attr; note placed after the noun
- said: /ma.ˈɛs.tro/ maèstro (it-IT)
  1. [B2] più importante o più grande rispetto agli altri dello stesso tipo
     - example: Per arrivare al paese bisogna seguire la strada maestra.
     - collocations: strada maestra; via maestra; muro maestro; albero maestro
  2. [C1] fatto con grande abilità, come da un vero esperto
     - example: Con una mossa maestra ha vinto la partita a scacchi.
     - collocations: mossa maestra; colpo maestro

### 46. menzione

**Lexeme 1: menzione**, NOUN
- grammar: gender Fem; countability Count
- said: /men.ˈtsjo.ne/ menzióne (it-IT)
  1. [B2] breve riferimento a qualcuno o qualcosa in un discorso o in uno scritto
     - example: Nel suo discorso non ha fatto alcuna menzione del problema.
     - frames: menzione di qcn/qcs
     - collocations: fare menzione di; degno di menzione; una breve menzione; non fare menzione
  2. [B2] riconoscimento pubblico dato in un concorso a chi merita, oltre ai premi principali
     - example: Il nostro cortometraggio ha ricevuto una menzione speciale dalla giuria.
     - collocations: menzione speciale; menzione d'onore; ricevere una menzione; ottenere una menzione

### 47. autrice

**Lexeme 1: autrice**, NOUN
- grammar: gender Fem; countability Count
- said: /au.ˈtri.tʃe/ autrìce (it-IT)
  1. [B1] donna che ha scritto un libro o creato un'opera artistica o scientifica
     - example: Stasera in libreria l'autrice presenta il suo nuovo romanzo.
     - frames: autrice di qcs
     - collocations: autrice esordiente; giovane autrice; autrice di romanzi; autrice di canzoni; autrice di successo
  2. [B2] donna che ha compiuto un'azione o causato un fatto
     - example: La polizia cerca ancora l'autrice della rapina in gioielleria.
     - frames: autrice di qcs
     - collocations: autrice del furto; autrice del gesto; autrice del delitto
- relations: feminine_of autore (NOUN)

### 48. ritornare

**Lexeme 1: ritornare**, VERB
- grammar: conjugation 1; irregular False; auxiliary be; valency Intr
- said: /ri.tor.ˈna.re/ ritornàre (it-IT)
  1. [A2] andare o venire di nuovo nel luogo da cui si era partiti
     - example: Dopo tre anni all'estero, mio fratello è ritornato a casa.
     - frames: ritornare a + luogo; ritornare in + luogo; ritornare da + luogo
     - collocations: ritornare a casa; ritornare indietro; ritornare in patria; ritornare in sé
  2. [B1] riprendere un argomento, un'attività o una situazione lasciati prima
     - example: Vorrei ritornare su quello che abbiamo detto ieri sera.
     - frames: ritornare su qcs; ritornare a qcs; ritornare a + inf
     - collocations: ritornare sull'argomento; ritornare sui propri passi; ritornare alla normalità
  3. [B1] esserci o farsi sentire di nuovo dopo un periodo di assenza
     - example: Con l'inizio di novembre è ritornato il freddo in città.
     - collocations: ritornare in mente; ritornare di moda
- relations: derived_from tornare (VERB)

### 49. costume

**Lexeme 1: costume**, NOUN
- grammar: gender Masc; countability Count
- said: /kos.ˈtu.me/ costùme (it-IT)
  1. [A2] indumento che si indossa per nuotare o prendere il sole
     - example: Non dimenticare il costume, domani andiamo tutti al mare.
     - collocations: costume da bagno; costume intero; mettersi il costume; costume a due pezzi
  2. [B1] abito tipico di un'epoca o di un luogo, o usato per travestirsi o recitare
     - example: Per la festa di carnevale mio figlio vuole un costume da pirata.
     - frames: costume da qcs
     - collocations: costume di carnevale; costume tradizionale; costume teatrale; in costume; film in costume; festa in costume
  3. [B2] abitudine o modo di vivere tipico di un popolo o di un'epoca
     - example: Il libro descrive gli usi e i costumi delle antiche popolazioni alpine.
     - collocations: usi e costumi; per costume; di costume; rivista di costume
  4. [C1] comportamento morale di una persona o di una società
     - example: Lo scrittore critica duramente la corruzione dei costumi del suo tempo.
     - collocations: buon costume; di facili costumi; corruzione dei costumi; comune senso del pudore e del buon costume
     - grammar: note often in the plural

### 50. chiarezza

**Lexeme 1: chiarezza**, NOUN
- grammar: gender Fem; countability Mass
- said: /kja.ˈret.tsa/ chiarézza (it-IT)
  1. [B1] qualità di ciò che si capisce facilmente, senza dubbi o confusione
     - example: Il professore spiega sempre con grande chiarezza anche gli argomenti difficili.
     - frames: fare chiarezza su qcs
     - collocations: fare chiarezza; con chiarezza; per chiarezza; in tutta chiarezza; chiarezza espositiva; massima chiarezza; mancanza di chiarezza
  2. [B2] qualità di ciò che è luminoso o lascia passare la luce
     - example: La chiarezza dell'acqua permetteva di vedere i pesci sul fondo.
     - collocations: chiarezza dell'acqua; chiarezza del cielo
- relations: abstract_noun_of chiaro (ADJ)

### 51. artiglieria

**Lexeme 1: artiglieria**, NOUN
- grammar: gender Fem; countability Mass, mainly
- said: /ar.tiʎ.ʎe.ˈri.a/ artiglierìa (it-IT)
  1. [B2] insieme delle armi da fuoco pesanti che lanciano proiettili a grande distanza
     - example: Per tutta la notte si è sentito il rumore dell'artiglieria nemica.
     - collocations: fuoco di artiglieria; pezzo di artiglieria; colpo di artiglieria; artiglieria pesante; artiglieria contraerea; bombardamento di artiglieria
  2. [C1] parte dell'esercito formata dai soldati che usano le armi da fuoco pesanti
     - example: Mio nonno ha fatto il servizio militare in artiglieria.
     - collocations: reggimento di artiglieria; ufficiale di artiglieria; servire in artiglieria
     - labels: domain military

### 52. borsa

**Lexeme 1: borsa**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈbor.sa/ bórsa (it-IT)
  1. [A1] contenitore di pelle, stoffa o altro materiale, con manici, per portare oggetti
     - example: Metti il portafoglio nella borsa, così non lo perdi.
     - collocations: borsa della spesa; borsa a tracolla; borsa di pelle; borsa da viaggio; borsa dell'acqua calda; borsa frigo
  2. [B1] mercato dove si comprano e si vendono azioni, titoli e altri valori
     - example: Oggi la Borsa ha chiuso in forte ribasso dopo le notizie.
     - collocations: quotato in borsa; giocare in borsa; entrare in borsa; crollo della borsa; Borsa valori
     - grammar: note often written with a capital letter
  3. [B1] somma di denaro data a uno studente o a un ricercatore per i suoi studi
     - example: Grazie a una borsa ha potuto studiare un anno all'estero.
     - collocations: borsa di studio; vincere una borsa; borsa di ricerca; borsa di dottorato
  4. [B2] gonfiore della pelle sotto gli occhi, dovuto a stanchezza o età
     - example: Dopo tre notti senza dormire ho delle borse enormi.
     - collocations: avere le borse sotto gli occhi
     - grammar: number Plur, mainly

### 53. inutilmente

**Lexeme 1: inutilmente**, ADV
- grammar: invariable True
- said: /i.nu.til.ˈmen.te/ inutilménte (it-IT)
  1. [B1] senza ottenere il risultato che si voleva
     - example: Ho cercato inutilmente le chiavi per tutta la mattina.
     - collocations: cercare inutilmente; tentare inutilmente; aspettare inutilmente; provare inutilmente
  2. [B1] senza che ce ne fosse bisogno o un motivo
     - example: Ti sei preoccupato inutilmente, alla fine è andato tutto bene.
     - collocations: preoccuparsi inutilmente; spendere inutilmente; complicare inutilmente; rischiare inutilmente
- relations: adverb_of inutile (ADJ)

### 54. argentino

**Lexeme 1: argentino**, ADJ
- said: /ar.dʒen.ˈti.no/ argentìno (it-IT)
  1. [A2] del paese sudamericano che ha come capitale Buenos Aires
     - example: Il tango argentino è famoso in tutto il mondo.
     - collocations: tango argentino; carne argentina; pampa argentina
     - grammar: comparable False
  2. [C1] di suono chiaro, limpido e acuto, come quello di un campanello
     - example: Dalla stanza accanto arrivava la sua risata argentina.
     - collocations: risata argentina; voce argentina
     - labels: style literary
- relations: adjective_of Argentina (PROPN); derived_from argento (NOUN)
**Lexeme 2: argentino**, NOUN
- grammar: gender Masc; countability Count; note feminine argentina, plural argentine
- said: /ar.dʒen.ˈti.no/ argentìno (it-IT)
  1. [A2] persona nata o che vive nel paese sudamericano con capitale Buenos Aires
     - example: Al nostro tavolo sedeva un argentino molto simpatico e chiacchierone.
- relations: derived_from Argentina (PROPN)

### 55. contea

**Lexeme 1: contea**, NOUN
- grammar: gender Fem; countability Count
- said: /kon.ˈtɛ.a/ contèa (it-IT)
  1. [B2] divisione amministrativa di alcuni paesi, soprattutto Gran Bretagna e Stati Uniti
     - example: Abitano in un piccolo villaggio di una contea inglese.
     - collocations: capoluogo di contea; sceriffo della contea
  2. [C1] territorio governato da un conte nel Medioevo
     - example: Nel Duecento la contea passò sotto il controllo del vescovo.
     - labels: time historical
- relations: derived_from conte (NOUN)
