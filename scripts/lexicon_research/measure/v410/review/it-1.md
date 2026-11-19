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
- grammar: comparable False; position Attr, mainly; note past participle of presumere; usually before the noun
- said: /pre.ˈzun.to/ presùnto (it-IT)
  1. [B2] ritenuto vero o tale in base a indizi, ma senza prova certa
     - example: Il presunto autore del furto è stato fermato vicino alla stazione.
     - collocations: presunto colpevole; presunto assassino; morte presunta; data presunta del parto; presunta vittima
- relations: derived_from presumere (VERB)

### 2. cominciare

**Lexeme 1: cominciare**, VERB
- grammar: conjugation 1; irregular False; valency Both; auxiliary both; note the i is dropped before e or i: comincerò, cominci
- said: /ko.min.ˈtʃa.re/ cominciàre (it-IT)
  1. [A1] fare i primi passi di un'attività o mettersi a fare qualcosa
     - example: Domani comincio a studiare per l'esame di storia.
     - frames: cominciare qcs; cominciare a + inf; cominciare con qcs; cominciare da qcs
     - collocations: cominciare da capo; cominciare da zero; per cominciare; cominciare bene; cominciare male; cominciare un lavoro
     - grammar: valency Both; auxiliary have
     - opposites: finire
  2. [A1] prendere avvio in un certo momento o in un certo punto
     - example: Il film comincia alle nove, quindi sbrighiamoci.
     - frames: qcs comincia; cominciare con qcs; cominciare da qcs
     - collocations: a cominciare da
     - grammar: valency Intr; auxiliary be
     - opposites: finire

### 3. esclusivamente

**Lexeme 1: esclusivamente**, ADV
- said: /e.sklu.zi.va.ˈmen.te/ esclusivaménte (it-IT)
  1. [B2] soltanto, senza che altre persone o altre cose siano comprese
     - example: Questo parcheggio è riservato esclusivamente ai residenti del quartiere.
     - collocations: quasi esclusivamente; riservato esclusivamente
- relations: adverb_of esclusivo (ADJ)

### 4. evidente

**Lexeme 1: evidente**, ADJ
- grammar: comparable True
- said: /e.vi.ˈdɛn.te/ evidènte (it-IT)
  1. [B1] che si vede o si nota con facilità
     - example: Sul muro della cucina ci sono segni evidenti di umidità.
     - collocations: segni evidenti; in modo evidente; rendere evidente; tracce evidenti
  2. [B1] così chiaro da non avere bisogno di prove o spiegazioni
     - example: Mi sembra evidente che nessuno ha letto le istruzioni.
     - frames: è evidente che + ind
     - collocations: prova evidente; motivi evidenti; per ragioni evidenti

### 5. fila

**Lexeme 1: fila**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈfi.la/ fìla (it-IT)
  1. [A2] serie di persone o cose disposte in linea una dopo l’altra
     - example: Per vedere meglio lo spettacolo ci siamo seduti in prima fila.
     - frames: una fila di qcn/qcs
     - collocations: di fila; in fila indiana; prima fila; in fila per due; una fila di alberi
  2. [A2] insieme di persone che aspettano il proprio turno una dietro l’altra
     - example: Alla posta c’era una fila lunghissima anche stamattina.
     - collocations: fare la fila; mettersi in fila; stare in fila; saltare la fila; una lunga fila
  3. [C1] insieme delle persone che fanno parte di un esercito, un partito o un gruppo
     - example: Negli ultimi anni molti giovani sono entrati nelle file del partito.
     - frames: le file di qcs
     - collocations: serrare le file; ingrossare le file
     - labels: figurative True
     - grammar: number Plur
- relations: confusable filo (NOUN): filo has a figurative plural le fila (tirare le fila), while the plural of fila is file

### 6. top

**Lexeme 1: top**, NOUN
- grammar: gender Masc; invariable True
- said: /ˈtɔp/ top (it-IT)
  1. [B1] maglia femminile leggera che copre il busto, spesso corta e senza maniche
     - example: D’estate mi piace mettere un top bianco con i jeans.
     - collocations: top di pizzo; top senza spalline
     - grammar: countability Count
  2. [B2] il livello più alto di qualità, successo o importanza
     - example: Per chi ama il mare, quest’isola è davvero il top.
     - frames: il top di qcs
     - collocations: essere il top; il top della gamma; il top della carriera; al top
     - labels: formality informal
     - grammar: number Sing; countability Mass
  3. [C1] piano superiore di un mobile da cucina, su cui si lavora
     - example: Abbiamo scelto un top in marmo per la cucina nuova.
     - collocations: top in marmo; top della cucina
     - grammar: countability Count
**Lexeme 2: top**, ADJ
- grammar: invariable True
- said: /ˈtɔp/ top (it-IT)
  1. [B2] di altissimo livello o qualità, tra i migliori
     - example: Questo ristorante offre un servizio top a prezzi onesti.
     - labels: formality informal

### 7. decidere

**Lexeme 1: decidere**, VERB
- grammar: conjugation 2; irregular True
- said: /de.ˈtʃi.de.re/ decìdere (it-IT)
- forms: decido [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] decìdo; decidi [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] decìdi; decide [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] decìde; decidiamo [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] decidiàmo; decidono [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] decìdono; decisi [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] decìsi; decise [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] decìse; deciderò [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] deciderò; decida [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] decìda; deciso [Tense=Past, VerbForm=Part] decìso
  1. [A1] scegliere, dopo averci pensato, che cosa fare o come stanno le cose
     - example: Abbiamo deciso di passare le vacanze in montagna quest'anno.
     - frames: decidere di + inf; decidere qcs; decidere che + ind; decidere su qcs
     - collocations: decidere all'unanimità; decidere il da farsi; decidere in fretta; decidere da solo
     - grammar: auxiliary have; valency Tr
  2. [B2] determinare il risultato di una gara, di una vicenda o di una questione
     - example: Un gol nel secondo tempo ha deciso la partita.
     - collocations: decidere la partita; decidere le sorti; decidere una causa; decidere una controversia
     - grammar: auxiliary have; valency Tr
  3. [B1] arrivare finalmente a una scelta dopo aver esitato a lungo
     - example: Alla fine mi sono deciso a cambiare lavoro.
     - frames: decidersi a + inf
     - collocations: decidersi una buona volta
     - grammar: valency Refl; reflexive True; auxiliary be; note as decidersi; the pronoun does not mean oneself

### 8. negozio

**Lexeme 1: negozio**, NOUN
- grammar: gender Masc; countability Count
- said: /ne.ˈɡɔt.tsjo/ negòzio (it-IT)
  1. [A1] locale aperto al pubblico dove si vendono merci o si offrono servizi
     - example: Sotto casa mia hanno aperto un negozio di biciclette usate.
     - frames: negozio di qcs
     - collocations: andare per negozi; fare il giro dei negozi; aprire un negozio; gestire un negozio; negozio di abbigliamento; negozio di alimentari; negozio online; vetrina del negozio
  2. [C2] atto con cui una persona vuole produrre determinati effetti previsti dalla legge
     - example: Il contratto è il più comune negozio giuridico del diritto privato.
     - collocations: negozio giuridico
     - labels: domain law

### 9. impresa

**Lexeme 1: impresa**, NOUN
- grammar: gender Masc; countability Count
- said: /im.ˈpre.za/ imprésa (it-IT)
  1. [B1] organizzazione economica che produce beni o servizi per guadagnare
     - example: Mio padre lavora da vent’anni in una piccola impresa edile.
     - collocations: piccole e medie imprese; impresa edile; impresa di pulizie; impresa di costruzioni; impresa familiare; avviare un’impresa; fare impresa
  2. [B2] azione difficile e importante che richiede coraggio e grande impegno
     - example: Attraversare l’oceano in barca a vela fu una vera impresa.
     - frames: è un’impresa + inf
     - collocations: compiere un’impresa; impresa eroica; impresa disperata; impresa titanica; tentare l’impresa

### 10. industriale

**Lexeme 1: industriale**, ADJ
- grammar: comparable False
- said: /in.du.ˈstrja.le/ industriàle (it-IT)
  1. [B1] delle fabbriche e della produzione di beni in grande quantità con macchine
     - example: Ai margini della città sorge una grande zona industriale.
     - collocations: zona industriale; rivoluzione industriale; produzione industriale; prodotto industriale; area industriale; città industriale; paese industriale; design industriale
  2. [B2] molto grande per quantità, quasi esagerato
     - example: Per la festa abbiamo preparato una quantità industriale di panini.
     - collocations: quantità industriale; in quantità industriali
     - labels: formality informal, figurative True
     - grammar: position Attr
- relations: adjective_of industria (NOUN)
**Lexeme 2: industriale**, NOUN
- grammar: gender Com; countability Count
- said: /in.du.ˈstrja.le/ industriàle (it-IT)
  1. [B2] persona che possiede o dirige un'impresa che produce beni
     - example: Suo nonno era un ricco industriale del settore tessile.
     - collocations: grande industriale; ricco industriale; associazione degli industriali
- relations: derived_from industriale (ADJ)

### 11. influenza

**Lexeme 1: influenza**, NOUN
- grammar: gender Fem; countability Both
- said: /in.flu.ˈɛn.tsa/ influènza (it-IT)
  1. [B1] azione che una persona o una cosa esercita su altri, cambiandone idee o comportamenti
     - example: I genitori hanno ancora una grande influenza sulle scelte dei figli.
     - frames: influenza su qcn/qcs; influenza di qcn/qcs
     - collocations: esercitare un'influenza; subire l'influenza; avere influenza; sotto l'influenza di; sfera d'influenza; influenza negativa; forte influenza
  2. [A2] malattia contagiosa causata da un virus, con febbre, tosse e dolori
     - example: Mio fratello è a letto con l'influenza da lunedì.
     - collocations: avere l'influenza; prendere l'influenza; a letto con l'influenza; influenza stagionale; influenza intestinale; vaccino contro l'influenza
     - grammar: countability Count

### 12. istruzione

**Lexeme 1: istruzione**, NOUN
- grammar: gender Fem; countability Both
- said: /is.trut.ˈtsjo.ne/ istruzióne (it-IT)
  1. [B1] insegnamento e cultura che si ricevono attraverso la scuola e lo studio
     - example: Tutti i bambini hanno diritto a un'istruzione gratuita e di qualità.
     - collocations: istruzione obbligatoria; istruzione superiore; istruzione universitaria; ministero dell'istruzione; livello di istruzione; grado di istruzione; diritto all'istruzione; ricevere un'istruzione
     - grammar: countability Mass
  2. [A2] indicazione su come si fa qualcosa o su come comportarsi
     - example: Prima di montare il mobile, leggi con attenzione le istruzioni.
     - frames: istruzioni su qcs; istruzioni per + inf
     - collocations: istruzioni per l'uso; libretto di istruzioni; seguire le istruzioni; leggere le istruzioni; dare istruzioni; impartire istruzioni; attenersi alle istruzioni
     - grammar: countability Count; number Plur, mainly
  3. [C1] comando elementare che un programma fa eseguire al computer
     - example: Il processore esegue milioni di istruzioni ogni secondo.
     - labels: domain computing
     - grammar: countability Count
  4. [C2] fase del processo in cui si raccolgono e si esaminano le prove
     - example: Il giudice ha chiuso l'istruzione e fissato la data del dibattimento.
     - collocations: istruzione probatoria
     - labels: domain law
     - grammar: countability Mass
- relations: verbal_noun_of istruire (VERB)

### 13. ricco

**Lexeme 1: ricco**, ADJ
- grammar: comparable True
- said: /ˈrik.ko/ rìcco (it-IT)
  1. [A1] che possiede molto denaro o molti beni
     - example: Sogno di diventare ricco e di comprare una casa al mare.
     - collocations: diventare ricco; ricco sfondato; famiglia ricca; paese ricco; nascere ricco
     - opposites: povero
  2. [B1] che contiene o possiede qualcosa in grande quantità
     - example: Questa frutta è ricca di vitamine e fa bene alla salute.
     - frames: ricco di qcs
     - collocations: ricco di vitamine; ricco di storia; ricco di significato; ricco di idee; terreno ricco
     - opposites: povero
  3. [B2] abbondante, generoso, più grande o più pieno del solito
     - example: In quell'albergo servono una colazione ricca e varia.
     - collocations: una ricca mancia; un ricco bottino; una ricca colazione; un ricco premio
  4. [C1] pieno di lusso, decorato con materiali di grande valore
     - example: Le sale del palazzo hanno ricchi arredi e soffitti dipinti.
     - collocations: ricchi arredi; ricche vesti
     - labels: style literary
**Lexeme 2: ricco**, NOUN
- grammar: gender Masc; countability Count; number Plur, mainly; note feminine ricca
- said: /ˈrik.ko/ rìcco (it-IT)
  1. [B1] persona che possiede molto denaro o molti beni
     - example: Le nuove tasse colpiranno soprattutto i ricchi del paese.
     - collocations: i ricchi e i poveri; nuovo ricco
     - opposites: povero
- relations: derived_from ricco (ADJ)

### 14. tradizionale

**Lexeme 1: tradizionale**, ADJ
- said: /tra.dit.tsjo.ˈna.le/ tradizionàle (it-IT)
  1. [B1] tramandato da una generazione all’altra secondo usi antichi e consolidati
     - example: Per le feste preparo sempre i dolci tradizionali del mio paese.
     - collocations: cucina tradizionale; piatto tradizionale; ricetta tradizionale; costume tradizionale; musica tradizionale; festa tradizionale
  2. [B1] conforme ai modi abituali e diffusi, non nuovo né alternativo
     - example: Molti preferiscono ancora la scuola tradizionale ai corsi online.
     - collocations: medicina tradizionale; metodi tradizionali; famiglia tradizionale; in modo tradizionale
     - grammar: comparable True
     - opposites: innovativo
- relations: adjective_of tradizione (NOUN)

### 15. live

**Lexeme 1: live**, ADJ
- grammar: invariable True; comparable False; note English loanword; usually placed after the noun
- said: /ˈlajv/ live (it-IT)
  1. [B1] eseguito davanti a un pubblico e non registrato in studio
     - example: Il venerdì sera questo locale offre sempre musica live.
     - collocations: musica live; concerto live; album live; esibizione live
  2. [B2] trasmesso nello stesso momento in cui avviene, senza registrazione
     - example: Seguiremo la conferenza in streaming live sul sito della scuola.
     - collocations: diretta live; streaming live; video live
**Lexeme 2: live**, ADV
- said: /ˈlajv/ live (it-IT)
  1. [B1] davanti a un pubblico oppure in diretta, nel momento in cui avviene
     - example: Questa canzone suona molto meglio live che su disco.
     - collocations: suonare live; cantare live
**Lexeme 3: live**, NOUN
- grammar: gender Masc; countability Count; invariable True
- said: /ˈlajv/ live (it-IT)
  1. [B2] concerto davanti al pubblico, o disco registrato durante un concerto
     - example: Il loro ultimo live è uscito anche in vinile.
- relations: derived_from live (ADJ)

### 16. elemento

**Lexeme 1: elemento**, NOUN
- grammar: gender Masc; countability Count
- said: /e.le.ˈmen.to/ eleménto (it-IT)
  1. [B1] ciascuna delle parti che insieme formano un tutto
     - example: La nostra cucina è fatta di elementi che si montano facilmente.
     - frames: elemento di qcs
     - collocations: elemento fondamentale; elemento essenziale; elemento chiave; l'elemento sorpresa; elemento di disturbo
  2. [B2] persona considerata per il ruolo che ha in un gruppo
     - example: In ufficio tutti lo considerano un ottimo elemento.
     - collocations: un ottimo elemento; un bell'elemento; elemento di spicco
  3. [B2] dato o fatto utile per capire o giudicare qualcosa
     - example: Il giudice non aveva elementi sufficienti per decidere.
     - collocations: elementi di prova; elementi sufficienti; nuovi elementi; avere elementi
     - grammar: number Plur, mainly
  4. [B2] sostanza che non si può dividere in sostanze più semplici
     - example: L’ossigeno è l’elemento più abbondante nella crosta terrestre.
     - collocations: elemento chimico; tavola periodica degli elementi
     - labels: domain chemistry
  5. [C1] nozioni di base di una disciplina o di un’arte
     - example: Il corso offre i primi elementi di grammatica latina.
     - frames: elementi di qcs
     - grammar: number Plur
  6. [C1] ciascuna delle forze o sostanze naturali che formano il mondo
     - example: Durante la tempesta la barca rimase in balia degli elementi.
     - collocations: i quattro elementi; la furia degli elementi; essere nel proprio elemento

### 17. blocco

**Lexeme 1: blocco**, NOUN
- grammar: gender Masc
- said: /ˈblɔk.ko/ blòcco (it-IT)
  1. [B1] pezzo grande e compatto di un materiale solido
     - example: Lo scultore ha ricavato la statua da un unico blocco di marmo.
     - frames: un blocco di qcs
     - collocations: blocco di marmo; blocco di ghiaccio; blocco di cemento; blocco di pietra
     - grammar: countability Count
  2. [B1] arresto o interruzione di un movimento, di un funzionamento o di un'attività
     - example: Il blocco del traffico durerà fino a domenica sera.
     - frames: blocco di qcs
     - collocations: blocco del traffico; blocco dei prezzi; blocco degli affitti; blocco cardiaco; blocco delle assunzioni; blocco del sistema
     - grammar: countability Both
     - opposites: sblocco
  3. [B2] sbarramento che impedisce il passaggio di persone o mezzi
     - example: Stanotte la polizia ci ha fermati a un posto di blocco.
     - collocations: posto di blocco; blocco stradale; blocco navale; forzare il blocco; togliere il blocco
     - grammar: countability Count
  4. [A2] insieme di fogli uniti da una parte e facili da staccare, per scrivere
     - example: Prendi un blocco e scrivi il numero di telefono.
     - collocations: blocco per appunti; blocco da disegno; blocco di carta
     - grammar: countability Count
  5. [B2] incapacità improvvisa di agire, parlare o ricordare per motivi psicologici
     - example: Davanti al foglio bianco ho avuto un blocco totale.
     - collocations: blocco psicologico; blocco dello scrittore; avere un blocco; superare un blocco
     - grammar: countability Count
  6. [B2] insieme di elementi considerati come un tutto unico
     - example: Abbiamo venduto i vecchi mobili in blocco a un rigattiere.
     - collocations: in blocco; comprare in blocco; un blocco di azioni
     - grammar: countability Count
  7. [B2] alleanza di stati, partiti o gruppi con interessi comuni
     - example: Durante la guerra fredda il mondo era diviso in due blocchi.
     - collocations: blocco occidentale; blocco sovietico; blocco orientale
     - grammar: countability Count
- relations: verbal_noun_of bloccare (VERB)

### 18. agenzia

**Lexeme 1: agenzia**, NOUN
- grammar: gender Fem; countability Count
- said: /a.dʒen.ˈtsi.a/ agenzìa (it-IT)
  1. [A2] impresa che offre servizi o tratta affari per conto dei clienti
     - example: Abbiamo prenotato il viaggio in un'agenzia vicino alla stazione.
     - collocations: agenzia di viaggi; agenzia immobiliare; agenzia pubblicitaria; agenzia di stampa; agenzia matrimoniale; agenzia per il lavoro; rivolgersi a un'agenzia
  2. [B2] ufficio locale di una banca o di una società con sede principale altrove
     - example: Per aprire il conto devi passare dalla nostra agenzia in centro.
     - collocations: agenzia bancaria; agenzia di assicurazioni
  3. [B2] ente pubblico che svolge compiti tecnici o amministrativi in un settore
     - example: L'agenzia spaziale europea prepara una nuova missione verso Marte.
     - collocations: agenzia spaziale; Agenzia delle Entrate
     - labels: style official
- relations: derived_from agente (NOUN)

### 19. utente

**Lexeme 1: utente**, NOUN
- grammar: gender Com; countability Count
- said: /u.ˈtɛn.te/ utènte (it-IT)
  1. [B1] persona che usa un servizio o un bene, spesso pubblico
     - example: Gli utenti del treno protestano per i continui ritardi.
     - frames: utente di qcs
     - collocations: utente della strada; utente finale; utenti del servizio; servizio agli utenti
  2. [B1] persona che usa un computer, un programma o un sito internet
     - example: Inserisci il nome utente e la password per entrare.
     - frames: utente di qcs
     - collocations: nome utente; utente registrato; account utente; profilo utente; utente anonimo

### 20. museo

**Lexeme 1: museo**, NOUN
- grammar: gender Masc; countability Count
- said: /mu.ˈzɛ.o/ musèo (it-IT)
  1. [A1] raccolta di opere o oggetti di valore culturale, o il luogo dove si espongono
     - example: Domenica andiamo al museo a vedere una mostra di pittura.

### 21. pertanto

**Lexeme 1: pertanto**, CONJ
- grammar: invariable True
- said: /per.ˈtan.to/ pertànto (it-IT)
  1. [B2] usato per introdurre la conseguenza di quanto è stato detto prima
     - example: Il treno è in forte ritardo, pertanto arriveremo dopo le dieci.
     - labels: formality formal

### 22. roba

**Lexeme 1: roba**, NOUN
- grammar: gender Masc; countability Mass, mainly
- said: /ˈrɔ.ba/ ròba (it-IT)
  1. [A2] insieme di oggetti, vestiti o beni di vario tipo, spesso di una persona
     - example: Prima di partire metti tutta la tua roba nella valigia.
     - collocations: roba da mangiare; roba vecchia; roba di valore; roba usata; mettere via la roba; roba di qualità
  2. [B1] cosa, fatto o faccenda qualsiasi, spesso giudicata con stupore o disprezzo
     - example: Pagare dieci euro per un caffè è roba da matti!
     - frames: roba da + inf; roba da qcn
     - collocations: roba da matti; roba da pazzi; roba da non credere; che roba; roba da ridere
     - labels: formality informal
  3. [C1] sostanza stupefacente che si compra e si usa illegalmente
     - example: La polizia ha trovato la roba nascosta in cantina.
     - collocations: farsi di roba; spacciare roba
     - labels: formality slang

### 23. recente

**Lexeme 1: recente**, ADJ
- grammar: comparable True
- said: /re.ˈtʃɛn.te/ recènte (it-IT)
- spellings: recente (it-IT); ricente (it-IT) [older books only]
  1. [B1] avvenuto, nato o fatto da poco tempo
     - example: Secondo uno studio recente, i giovani leggono sempre meno giornali.
     - collocations: di recente
     - opposites: remoto

### 24. omicidio

**Lexeme 1: omicidio**, NOUN
- grammar: gender Masc; countability Count
- said: /o.mi.ˈtʃi.djo/ omicìdio (it-IT)
  1. [B2] uccisione di una persona da parte di un'altra, voluta o non voluta
     - example: La polizia sta ancora indagando sull'omicidio avvenuto ieri sera in centro.
     - collocations: commettere un omicidio; tentato omicidio; omicidio colposo; omicidio volontario; omicidio premeditato; omicidio stradale; accusare di omicidio; indagare su un omicidio

### 25. garantire

**Lexeme 1: garantire**, VERB
- grammar: conjugation 3; irregular False; valency Tr; auxiliary have; note present tense with -isc-: garantisco, garantiscono
- said: /ɡa.ran.ti.ˈre/ garantirè (it-IT)
  1. [B1] fare in modo che qualcosa ci sia o avvenga con certezza
     - example: La legge garantisce a tutti il diritto all’istruzione.
     - frames: garantire qcs a qcn
     - collocations: garantire la sicurezza; garantire un diritto; garantire la qualità; garantire il funzionamento; garantire la continuità
  2. [B1] affermare con sicurezza che qualcosa è vero o che succederà
     - example: Ti garantisco che domani il lavoro sarà finito.
     - frames: garantire che + ind; garantire di + inf; garantire qcs a qcn
     - collocations: te lo garantisco
  3. [C1] impegnarsi a rispondere per una persona o per un suo debito
     - example: Se la banca chiede un garante, posso garantire io per te.
     - frames: garantire per qcn; garantire qcs
     - collocations: garantire un prestito

### 26. insaputa

**Lexeme 1: insaputa**, NOUN
- grammar: gender Fem; number Sing; countability Mass; note used only in all'insaputa di qcn and with a possessive: a mia, a tua, a sua insaputa
- said: /in.sa.ˈpu.ta/ insapùta (it-IT)
  1. [B2] condizione di chi non è informato di qualcosa che lo riguarda
     - example: Hanno organizzato la festa a mia insaputa, e sono rimasto senza parole.
     - frames: all'insaputa di qcn; a insaputa di qcn
     - collocations: a mia insaputa; a sua insaputa; a tua insaputa; all'insaputa di tutti

### 27. primavera

**Lexeme 1: primavera**, NOUN
- grammar: gender Fem; countability Count
- said: /pri.ma.ˈvɛ.ra/ primavèra (it-IT)
  1. [A1] stagione dell'anno che viene dopo l'inverno e prima dell'estate
     - example: In primavera i giardini della città si riempiono di fiori.
     - collocations: primavera inoltrata; arriva la primavera; pulizie di primavera; all'inizio della primavera
  2. [C1] anni di età di una persona
     - example: Mio nonno ha appena compiuto novanta primavere ed è ancora in forma.
     - labels: style literary, attitude humorous
     - grammar: number Plur; note used with a number: avere ottanta primavere
  3. [C1] il primo periodo, il più fresco e vivace, della vita o di qualcosa
     - example: Quegli anni furono la primavera della sua carriera di pittore.
     - frames: la primavera di qcs
     - collocations: la primavera della vita
     - labels: figurative True, style literary
  4. [B2] squadra di giovani giocatori di una società di calcio
     - example: Il ragazzo gioca nella Primavera e sogna la prima squadra.
     - labels: domain sport
     - grammar: countability Count; note usually written with a capital letter: la Primavera

### 28. vento

**Lexeme 1: vento**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈvɛn.to/ vènto (it-IT)
  1. [A2] movimento dell'aria nell'atmosfera, più o meno forte, da una zona all'altra
     - example: Oggi tira un vento così forte che è difficile camminare.
     - collocations: tira vento; c'è vento; vento forte; raffica di vento; colpo di vento; vento freddo; soffia il vento; parlare al vento
  2. [B2] corrente di idee o tendenza che si diffonde con forza in una società
     - example: Nel paese soffia un vento di rinnovamento che nessuno può fermare.
     - frames: un vento di qcs
     - collocations: vento di cambiamento; vento di rivolta; il vento è cambiato
     - labels: figurative True

### 29. facoltà

**Lexeme 1: facoltà**, NOUN
- grammar: gender Fem; countability Count; invariable True
- said: /fa.kol.ˈta/ facoltà (it-IT)
  1. [B1] ciascuna delle parti dell'università dove si insegna un gruppo di materie
     - example: Quest'anno mio fratello si è iscritto alla facoltà di medicina.
     - frames: facoltà di qcs
     - collocations: iscriversi a una facoltà; preside di facoltà; facoltà di medicina; facoltà di lettere; facoltà di giurisprudenza; consiglio di facoltà
  2. [B2] potere o diritto di fare qualcosa, riconosciuto a una persona
     - example: L'imputato ha la facoltà di non rispondere alle domande.
     - frames: facoltà di + inf
     - collocations: avere facoltà di; avvalersi della facoltà di non rispondere; concedere la facoltà; lasciare facoltà
  3. [B2] capacità naturale della mente o del corpo di compiere certe funzioni
     - example: Nonostante l'età, la nonna conserva tutte le sue facoltà mentali.
     - collocations: facoltà mentali; facoltà intellettive; nel pieno delle proprie facoltà; perdere le facoltà

### 30. ritardo

**Lexeme 1: ritardo**, NOUN
- grammar: gender Masc; countability Both
- said: /ri.ˈtar.do/ ritàrdo (it-IT)
  1. [A2] tempo che passa oltre il momento previsto o stabilito per qualcosa
     - example: Scusate, sono in ritardo perché c’era molto traffico.
     - frames: in ritardo con qcs; in ritardo su qcs
     - collocations: essere in ritardo; arrivare in ritardo; in ritardo; con ritardo; scusa il ritardo; accumulare ritardo; minuti di ritardo; fare ritardo; senza ritardo
     - opposites: anticipo
  2. [B2] sviluppo più lento rispetto al normale o a quello degli altri
     - example: Il paese ha accumulato un forte ritardo nella ricerca scientifica.
     - frames: ritardo in qcs; ritardo rispetto a qcn/qcs
     - collocations: colmare il ritardo; ritardo tecnologico; ritardo nello sviluppo; ritardo mentale
     - grammar: countability Count
- relations: derived_from ritardare (VERB)

### 31. lago

**Lexeme 1: lago**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈla.ɡo/ làgo (it-IT)
  1. [A2] grande massa d’acqua raccolta in una conca del terreno e circondata da terra
     - example: D’estate passiamo sempre qualche settimana in una casetta sul lago.
     - collocations: in riva al lago; lago artificiale; lago alpino; lago vulcanico; andare al lago; fare il bagno nel lago; sponda del lago
  2. [B2] grande quantità di liquido sparso su una superficie
     - example: Dopo la partita i giocatori erano in un lago di sudore.
     - frames: un lago di qcs
     - collocations: un lago di sangue; in un lago di sudore; un lago di lacrime
     - labels: figurative True

### 32. nemico

**Lexeme 1: nemico**, NOUN
- grammar: gender Masc; countability Count; note feminine nemica for a woman
- said: /ne.ˈmi.ko/ nemìco (it-IT)
- forms: nemici [Number=Plur, Gender=Masc] nemìci; nemica [Number=Sing, Gender=Fem] nemìca; nemiche [Number=Plur, Gender=Fem] nemìche
  1. [A2] persona che prova odio verso qualcuno e cerca di fargli del male
     - example: Con il suo carattere difficile si è fatto molti nemici.
     - collocations: farsi dei nemici; nemico giurato; acerrimo nemico; nemico mortale; nemico pubblico
     - opposites: amico
  2. [B1] esercito o paese contro cui si combatte in una guerra
     - example: Durante la notte i soldati hanno respinto l’attacco del nemico.
     - collocations: passare al nemico; attaccare il nemico; sconfiggere il nemico
     - grammar: number Sing, mainly; note usually il nemico, meaning the whole enemy side
  3. [B2] chi si oppone a qualcosa, o cosa che la danneggia
     - example: La fretta è spesso il peggior nemico di un buon lavoro.
     - frames: nemico di qcs
     - collocations: il peggior nemico
     - labels: figurative True
**Lexeme 2: nemico**, ADJ
- said: /ne.ˈmi.ko/ nemìco (it-IT)
- forms: nemici [Number=Plur, Gender=Masc] nemìci
  1. [B1] che è ostile a qualcuno o in guerra con lui
     - example: Gli aerei hanno sorvolato il territorio nemico senza essere visti.
     - frames: nemico di qcn
     - collocations: esercito nemico; fuoco nemico; linee nemiche; territorio nemico; paese nemico
     - opposites: amico
  2. [C1] contrario, sfavorevole o dannoso per qualcuno o qualcosa
     - example: Da quel giorno la sorte gli fu sempre nemica.
     - frames: nemico di qcs
     - collocations: sorte nemica
     - labels: style literary
     - opposites: favorevole

### 33. iniziare

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

### 34. clima

**Lexeme 1: clima**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈkli.ma/ clìma (it-IT)
- forms: climi [Number=Plur] clìmi
  1. [B1] insieme delle condizioni del tempo tipiche di una zona durante l’anno
     - example: Dopo la riunione, in ufficio si respirava un clima di tensione.
     - collocations: clima mite; clima temperato; clima rigido; clima secco; clima umido; clima tropicale; cambiamento del clima
  2. [B2] atmosfera generale che caratterizza un ambiente, un gruppo o un periodo
     - example: In questa regione il clima resta mite anche d’inverno.
     - frames: clima di qcs
     - collocations: clima di tensione; clima di fiducia; clima disteso; clima sereno; clima politico; respirare un clima
     - labels: figurative True
     - grammar: number Sing, mainly

### 35. professore

**Lexeme 1: professore**, NOUN
- grammar: gender Masc; countability Mass; note feminine professoressa
- said: /pro.fes.ˈso.re/ professóre (it-IT)
  1. [A1] persona che insegna una materia nella scuola media, superiore o all’università
     - example: Il professore di matematica ci ha dato molti compiti per domani.
     - frames: professore di qcs
     - collocations: professore universitario; professore di ruolo; professore ordinario; professore associato; professore emerito; professore supplente
  2. [B2] persona che parla con l’aria di chi sa tutto e vuole spiegarlo agli altri
     - example: Smettila di fare il professore, nessuno ti ha chiesto un parere!
     - collocations: fare il professore
     - labels: formality informal, attitude ironic, figurative True

### 36. riuscire

**Lexeme 1: riuscire**, VERB
- grammar: conjugation 3; irregular True; valency Intr; auxiliary be; note conjugated like uscire: riesco, riesci, but riusciamo
- said: /ri.uʃ.ˈʃi.re/ riuscìre (it-IT)
- forms: riesco [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] rièsco; riesci [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] rièsci; riesce [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] rièsce; riusciamo [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] riusciàmo; riescono [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] rièscono; riuscii [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] riuscìi; riuscì [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] riuscì; riuscirò [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] riuscirò; riesca [Mood=Sub, Tense=Pres, Person=1,2,3, Number=Sing, VerbForm=Fin] rièsca; riuscito [VerbForm=Part, Tense=Past, Gender=Masc, Number=Sing] riuscìto
  1. [A1] essere capace di fare qualcosa, anche superando delle difficoltà
     - example: Con questo rumore non riesco proprio a dormire.
     - frames: riuscire a + inf
     - collocations: riuscirci; non riuscire a crederci; riuscire a fatica; riuscire a malapena
     - grammar: valency Intr; auxiliary be
  2. [B1] avere un certo risultato, buono o cattivo
     - example: La torta è riuscita benissimo anche senza burro.
     - collocations: riuscire bene; riuscire male; riuscire alla perfezione
     - grammar: valency Intr; auxiliary be
  3. [B2] ottenere successo in un'attività o in un'impresa
     - example: Spero che i miei figli riescano nella vita.
     - frames: riuscire in qcs
     - collocations: riuscire nella vita; riuscire nell'intento; riuscire nell'impresa
     - grammar: valency Intr; auxiliary be
     - opposites: fallire
  4. [B2] apparire o essere in un certo modo per qualcuno
     - example: Mi riesce difficile credere a quello che racconti.
     - frames: riuscire + agg a qcn
     - collocations: riuscire simpatico; riuscire antipatico; riuscire difficile
     - grammar: valency Intr; auxiliary be
  5. [C1] andare di nuovo fuori da un luogo
     - example: Appena rientrato, è riuscito di casa per comprare il pane.
     - grammar: valency Intr; auxiliary be
- relations: derived_from uscire (VERB)

### 37. aumentare

**Lexeme 1: aumentare**, VERB
- grammar: conjugation 1; irregular False; valency Both; auxiliary both
- said: /au.men.ˈta.re/ aumentàre (it-IT)
  1. [A2] rendere più grande per quantità, numero, misura o intensità
     - example: Il governo vuole aumentare le tasse sulle sigarette dal prossimo anno.
     - frames: aumentare qcs; aumentare qcs di qcs
     - collocations: aumentare i prezzi; aumentare lo stipendio; aumentare la velocità; aumentare il volume; aumentare le vendite
     - grammar: valency Tr; auxiliary have
     - opposites: diminuire
  2. [A2] diventare più grande per quantità, numero, misura o intensità
     - example: Negli ultimi mesi il prezzo della benzina è aumentato molto.
     - frames: qcs aumenta; aumentare di qcs
     - collocations: aumentare di peso; aumentare di valore; aumentare notevolmente; aumentare costantemente
     - grammar: valency Intr; auxiliary be
     - opposites: diminuire

### 38. fenomeno

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

### 39. potente

**Lexeme 1: potente**, ADJ
- grammar: comparable True
- said: /po.ˈtɛn.te/ potènte (it-IT)
  1. [B1] che ha molta autorità, influenza o ricchezza e può imporsi sugli altri
     - example: Per questo lavoro serve un computer molto potente.
     - collocations: un paese potente; una famiglia potente; un uomo potente; amici potenti
  2. [B1] che ha grande forza, energia o efficacia
     - example: Nel romanzo una famiglia potente controlla tutta la città.
     - collocations: motore potente; farmaco potente; veleno potente; voce potente; un computer potente
     - opposites: debole
**Lexeme 2: potente**, NOUN
- grammar: gender Com; countability Count; number Plur, mainly
- said: /po.ˈtɛn.te/ potènte (it-IT)
  1. [B2] persona che ha molta autorità e influenza nella società
     - example: Spesso le leggi proteggono i potenti più dei cittadini comuni.
     - collocations: i potenti della terra
- relations: derived_from potente (ADJ)

### 40. minimo

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

### 41. dominio

**Lexeme 1: dominio**, NOUN
- grammar: gender Masc
- said: /do.ˈmi.njo/ domìnio (it-IT)
  1. [B2] potere di comandare su persone, popoli o territori
     - example: Per secoli la regione rimase sotto il dominio di potenze straniere.
     - frames: dominio su qcn/qcs; dominio di qcn
     - collocations: sotto il dominio di; esercitare il dominio; dominio straniero; dominio coloniale
  2. [B2] capacità di controllare bene sé stessi, una situazione o una materia
     - example: Anche nei momenti più difficili non perde mai il dominio di sé.
     - frames: dominio di qcs
     - collocations: dominio di sé; perdere il dominio di sé; avere il dominio della situazione
     - grammar: countability Mass
  3. [C1] territorio che si trova sotto il potere di uno stato o di un sovrano
     - example: La repubblica estese i suoi domini lungo tutta la costa.
     - grammar: countability Count; number Plur, mainly
  4. [C1] campo o settore di attività, di studio o di conoscenza
     - example: Questa questione esce dal dominio della medicina.
     - collocations: di dominio pubblico
  5. [B2] parte di un indirizzo internet che identifica un sito
     - example: Abbiamo registrato un nuovo dominio per il sito del negozio.
     - collocations: nome di dominio; registrare un dominio
     - labels: domain computing
     - grammar: countability Count

### 42. fisico

**Lexeme 1: fisico**, ADJ
- grammar: comparable False, mainly
- said: /ˈfi.zi.ko/ fìsico (it-IT)
- forms: fisici [Gender=Masc, Number=Plur] /ˈfi.zi.tʃi/ fìsici
  1. [A2] proprio del corpo, e non della mente o dello spirito
     - example: Fare un po' di attività fisica ogni giorno migliora l'umore.
     - collocations: attività fisica; aspetto fisico; dolore fisico; forma fisica; contatto fisico; sforzo fisico; esercizio fisico
     - opposites: mentale
  2. [B2] della natura e della materia, o della scienza che le studia
     - example: Nessuno può sfuggire alle leggi fisiche che regolano l'universo.
     - collocations: leggi fisiche; fenomeno fisico; geografia fisica; mondo fisico
  3. [B2] concreto e materiale, che esiste davvero nello spazio e non è virtuale
     - example: Oggi molti preferiscono comprare online piuttosto che in un negozio fisico.
     - collocations: negozio fisico; presenza fisica; persona fisica; supporto fisico
     - opposites: virtuale
**Lexeme 2: fisico**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈfi.zi.ko/ fìsico (it-IT)
- forms: fisici [Number=Plur] /ˈfi.zi.tʃi/ fìsici
  1. [B1] il corpo di una persona, con il suo aspetto e la sua forza
     - example: Dopo mesi di nuoto ha un fisico davvero atletico.
     - frames: avere il fisico per qcs
     - collocations: avere un bel fisico; fisico atletico; fisico asciutto; mantenere il fisico; curare il fisico; fisico robusto
- relations: derived_from fisico (ADJ)
**Lexeme 3: fisico**, NOUN
- grammar: gender Masc; countability Count; note feminine fisica
- said: /ˈfi.zi.ko/ fìsico (it-IT)
- forms: fisici [Number=Plur] /ˈfi.zi.tʃi/ fìsici
  1. [B1] studioso o esperto della scienza della materia e dell'energia
     - example: Mio cugino lavora come fisico in un laboratorio di ricerca.
     - collocations: fisico nucleare; fisico teorico; fisico sperimentale

### 43. ristorante

**Lexeme 1: ristorante**, NOUN
- grammar: gender Masc; countability Count
- said: /ri.sto.ˈran.te/ ristorànte (it-IT)
  1. [A1] locale pubblico dove si ordinano e si consumano pasti a pagamento
     - example: Stasera ceniamo in un piccolo ristorante vicino al porto.
     - collocations: andare al ristorante; mangiare al ristorante; cenare al ristorante; prenotare al ristorante; ristorante di pesce; ristorante stellato; ristorante tipico; ristorante self-service

### 44. condividere

**Lexeme 1: condividere**, VERB
- grammar: conjugation 2; irregular True; valency Tr; auxiliary have; note conjugated like dividere
- said: /kon.di.vi.ˈde.re/ condividère (it-IT)
- forms: condivido [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] condivìdo; condividi [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] condivìdi; condivide [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] condivìde; condividiamo [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] condividiàmo; condividono [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] condivìdono; condivisi [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] condivìsi; condivise [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] condivìse; condividerò [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] condividerò; condivida [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] condivìda; condiviso [VerbForm=Part, Tense=Past] condivìso
  1. [B1] avere, usare o vivere qualcosa insieme ad altre persone
     - example: Durante l’università condividevo un appartamento con due amici.
     - frames: condividere qcs con qcn
     - collocations: condividere un appartamento; condividere la stanza; condividere le spese; condividere gioie e dolori; condividere un’esperienza; condividere la responsabilità
  2. [B1] essere d’accordo con un’idea, un’opinione o una scelta di altri
     - example: Non condivido affatto la decisione presa dal consiglio.
     - collocations: condividere un’opinione; condividere un punto di vista; condividere pienamente; condividere le preoccupazioni
  3. [B1] rendere visibile o disponibile ad altri un contenuto in rete
     - example: Ho appena condiviso il link nel gruppo della classe.
     - frames: condividere qcs con qcn; condividere qcs su qcs
     - collocations: condividere un post; condividere un link; condividere sui social; condividere un file; condividere lo schermo
- relations: derived_from dividere (VERB)

### 45. comunale

**Lexeme 1: comunale**, ADJ
- grammar: comparable False
- said: /ko.mu.ˈna.le/ comunàle (it-IT)
  1. [B1] che appartiene all'amministrazione di una città o di un paese o la riguarda
     - example: Questa estate la piscina comunale resterà aperta anche la sera.
     - collocations: consiglio comunale; consigliere comunale; giunta comunale; elezioni comunali; palazzo comunale; biblioteca comunale; piscina comunale; polizia comunale
  2. [C1] dell'epoca medievale in cui molte città italiane si governavano da sole
     - example: Nell'età comunale molte città costruirono grandi palazzi pubblici.
     - collocations: età comunale; epoca comunale
     - labels: domain history
- relations: adjective_of comune (NOUN)

### 46. biglietto

**Lexeme 1: biglietto**, NOUN
- grammar: gender Masc; countability Count
- said: /biʎ.ˈʎet.to/ bigliétto (it-IT)
  1. [A1] cartoncino o foglio che permette di viaggiare o di entrare in un luogo pagando
     - example: Abbiamo già comprato due biglietti per il concerto di sabato.
     - frames: biglietto per qcs; biglietto per + luogo
     - collocations: fare il biglietto; timbrare il biglietto; obliterare il biglietto; biglietto di andata e ritorno; biglietto di sola andata; biglietto ridotto; biglietto omaggio; biglietto della lotteria; comprare il biglietto
  2. [A2] piccolo foglio o cartoncino su cui si scrive un breve messaggio
     - example: Se esco prima, ti lascio un biglietto sul tavolo.
     - collocations: biglietto d'auguri; biglietto da visita; lasciare un biglietto; scrivere un biglietto
  3. [B1] pezzo di carta che vale come denaro, stampato da una banca
     - example: Alla cassa mi hanno dato il resto in biglietti da dieci euro.
     - frames: biglietto da + numero
     - collocations: biglietto di banca

### 47. divertente

**Lexeme 1: divertente**, ADJ
- grammar: comparable True
- said: /di.ver.ˈtɛn.te/ divertènte (it-IT)
  1. [A1] che fa ridere o fa passare il tempo in modo piacevole
     - example: Giocare a carte con i nonni è sempre divertente.
     - frames: è divertente + inf; trovare divertente qcs
     - collocations: una serata divertente; un film divertente; una persona divertente; una storia divertente
     - opposites: noioso
- relations: derived_from divertire (VERB)

### 48. turno

**Lexeme 1: turno**, NOUN
- grammar: gender Masc; countability Mass
- said: /ˈtur.no/ tùrno (it-IT)
  1. [A2] momento in cui tocca a una persona fare qualcosa, secondo un ordine stabilito
     - example: Adesso tocca a te: è il tuo turno di tirare il dado.
     - frames: il turno di qcn; turno di + inf
     - collocations: a turno; fare a turno; aspettare il proprio turno; saltare il turno; è il mio turno; di turno
  2. [B1] periodo di lavoro o di servizio che si alterna con quello di altre persone
     - example: Questa settimana mio padre fa il turno di notte in ospedale.
     - collocations: turno di notte; lavorare a turni; fare il turno; essere di turno; turno di lavoro; cambio turno; farmacia di turno; medico di turno
  3. [B2] ciascuna delle fasi di una gara, di un torneo o di una votazione
     - example: La nostra squadra è stata eliminata al primo turno del torneo.
     - collocations: primo turno; secondo turno; passare il turno; turno di ballottaggio; turno eliminatorio

### 49. successivo

**Lexeme 1: successivo**, ADJ
- grammar: comparable False
- said: /suk.tʃes.ˈsi.vo/ successìvo (it-IT)
  1. [B1] che viene dopo un altro, nel tempo o in un ordine
     - example: Nel periodo successivo alla guerra molte famiglie emigrarono all’estero.
     - frames: successivo a qcs
     - collocations: il giorno successivo; l’anno successivo; la settimana successiva; in un momento successivo; la fase successiva; la pagina successiva
     - opposites: precedente

### 50. spiegare

**Lexeme 1: spiegare**, VERB
- grammar: conjugation 1; irregular False; note adds h before e and i: spieghi, spieghiamo
- said: /spje.ˈɡa.re/ spiegàre (it-IT)
  1. [A2] far capire qualcosa a qualcuno rendendolo chiaro con parole o esempi
     - example: La maestra spiega la lezione ai bambini con molta pazienza.
     - frames: spiegare qcs a qcn; spiegare a qcn che + ind; spiegare come + ind; spiegare perché + ind
     - collocations: spiegare il motivo; spiegare la lezione; spiegare le ragioni; spiegare nei dettagli; spiegare chiaramente
     - grammar: valency Tr; auxiliary have
  2. [B1] esprimersi in modo chiaro e farsi capire dagli altri
     - example: Scusa, forse prima non mi sono spiegato bene.
     - collocations: spiegarsi meglio; spiegarsi male
     - grammar: valency Refl; reflexive True; auxiliary be
  3. [B2] riuscire a capire la causa o il motivo di qualcosa
     - example: Ancora non mi spiego come abbia potuto perdere le chiavi.
     - frames: spiegarsi qcs; spiegarsi come + cong; spiegarsi perché + cong
     - grammar: valency Refl; reflexive True; auxiliary be; note the pronoun means 'to oneself'; mostly negative or in questions
  4. [C1] aprire e stendere qualcosa che era chiuso o avvolto
     - example: Appena usciti dal porto, i marinai spiegarono le vele.
     - collocations: spiegare le vele; spiegare le ali
     - labels: style literary
     - grammar: valency Tr; auxiliary have
     - opposites: piegare

### 51. morale

**Lexeme 1: morale**, ADJ
- said: /mo.ˈra.le/ moràle (it-IT)
  1. [B1] legato alla distinzione tra bene e male nel comportamento umano
     - example: Hai l’obbligo morale di aiutare tuo fratello in questo momento.
     - collocations: valori morali; obbligo morale; dovere morale; giudizio morale; principi morali; questione morale
     - grammar: comparable False
  2. [B2] onesto e rispettoso dei principi del bene e del giusto
     - example: Era un uomo profondamente morale, rispettato da tutto il paese.
     - grammar: comparable True
     - opposites: immorale
  3. [B2] legato alla mente e ai sentimenti, non alle cose materiali
     - example: Nei mesi difficili gli amici mi hanno dato un grande sostegno morale.
     - collocations: sostegno morale; danno morale; vincitore morale; appoggio morale
     - grammar: comparable False
     - opposites: materiale
**Lexeme 2: morale**, NOUN
- grammar: gender Fem
- said: /mo.ˈra.le/ moràle (it-IT)
  1. [B2] insieme dei principi sul bene e sul male che guidano il comportamento
     - example: Ogni società ha una sua morale, che cambia con il tempo.
     - collocations: morale comune; morale cristiana; senza morale
     - grammar: countability Mass
  2. [B1] insegnamento che si ricava da un racconto o da un fatto
     - example: La morale della storia è che bisogna sempre dire la verità.
     - frames: la morale di qcs
     - collocations: la morale della favola
     - grammar: countability Count
  3. [B2] discorso con cui si rimprovera qualcuno dicendogli come comportarsi
     - example: Smettila di farmi la morale ogni volta che torno tardi!
     - frames: fare la morale a qcn
     - collocations: fare la morale
     - labels: formality informal
     - grammar: countability Count; number Sing
- relations: derived_from morale (ADJ)
**Lexeme 3: morale**, NOUN
- grammar: gender Masc; countability Mass; number Sing
- said: /mo.ˈra.le/ moràle (it-IT)
  1. [B1] stato d’animo di una persona o di un gruppo, tra fiducia e scoraggiamento
     - example: Dopo la vittoria di ieri il morale della squadra è altissimo.
     - collocations: giù di morale; tirare su il morale; alzare il morale; avere il morale a terra; morale alto; morale basso; risollevare il morale
- relations: derived_from morale (ADJ)

### 52. montagna

**Lexeme 1: montagna**, NOUN
- grammar: gender Fem
- said: /mon.ˈtaɲ.ɲa/ montàgna (it-IT)
  1. [A1] grande rilievo naturale del terreno, molto più alto di una collina
     - example: Dalla finestra dell’albergo si vedono le montagne coperte di neve.
     - collocations: scalare una montagna; in cima alla montagna; ai piedi della montagna; catena di montagne; montagna innevata
     - grammar: countability Count
  2. [A1] zona con molti rilievi alti, spesso meta di vacanze
     - example: Ogni estate passiamo due settimane in montagna con i nonni.
     - collocations: andare in montagna; vacanza in montagna; aria di montagna; rifugio di montagna; scarponi da montagna
     - grammar: number Sing; countability Mass
  3. [B1] grande quantità di cose ammucchiate o da fare
     - example: Stasera devo ancora correggere una montagna di compiti.
     - frames: una montagna di qcs
     - collocations: una montagna di soldi; una montagna di lavoro
     - labels: figurative True
     - grammar: countability Count

### 53. senato

**Lexeme 1: senato**, NOUN
- grammar: gender Masc; countability Count; note usually written Senato for the Italian chamber
- said: /se.ˈna.to/ senàto (it-IT)
  1. [B1] una delle due camere del parlamento, che vota le leggi insieme all'altra
     - example: La riforma sarà discussa al Senato la prossima settimana.
     - collocations: presidente del Senato; eletto al Senato; maggioranza al Senato; approvare al Senato; seduta del Senato; voto del Senato
  2. [B2] nell'antica Roma, assemblea dei cittadini più importanti che guidava lo stato
     - example: Il console doveva presentare ogni proposta davanti al senato.
     - collocations: senato romano
     - labels: time historical
