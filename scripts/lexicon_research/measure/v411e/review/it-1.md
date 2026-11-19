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
- said: /pre.ˈzun.to/ presùnto (it-IT)
  1. [B2] ritenuto vero o probabile, ma senza prove certe
     - example: La polizia ha arrestato ieri sera il presunto autore del furto.
     - collocations: presunto colpevole; presunto innocente; presunto autore; presunto responsabile; data presunta
- relations: derived_from presumere (VERB)

### 2. cominciare

**Lexeme 1: cominciare**, VERB
- grammar: conjugation 1; irregular False; valency Both; auxiliary both
- said: /ko.min.ˈtʃa.re/ cominciàre (it-IT)
  1. [A1] mettersi a fare qualcosa o fare la prima parte di un’attività
     - example: Domani comincio a studiare per l’esame di storia.
     - frames: cominciare qcs; cominciare a + inf; cominciare da qcs; cominciare con qcs
     - collocations: cominciare da capo; cominciare da zero; per cominciare; cominciare la scuola; cominciare un lavoro
     - grammar: valency Both; auxiliary have
     - opposites: finire
  2. [A1] prendere avvio in un certo momento o in un certo punto
     - example: Lo spettacolo comincia alle nove, quindi non fate tardi.
     - frames: qcs comincia; cominciare con qcs; cominciare da qcs
     - collocations: a cominciare da
     - grammar: valency Intr; auxiliary be
     - opposites: finire

### 3. esclusivamente

**Lexeme 1: esclusivamente**, ADV
- said: /es.klu.zi.va.ˈmen.te/ esclusivaménte (it-IT)
  1. [B1] soltanto, senza comprendere altre persone o altre cose
     - example: Questo parcheggio è riservato esclusivamente ai residenti del quartiere.
     - collocations: quasi esclusivamente; riservato esclusivamente; dedicarsi esclusivamente
- relations: adverb_of esclusivo (ADJ)

### 4. evidente

**Lexeme 1: evidente**, ADJ
- grammar: comparable True
- said: /e.vi.ˈdɛn.te/ evidènte (it-IT)
  1. [B1] chiaro alla mente, che si capisce subito e non ha bisogno di prove
     - example: Mi sembra evidente che oggi non hai voglia di uscire.
     - frames: è evidente che + ind; evidente a qcn
     - collocations: evidente a tutti; prova evidente; contraddizione evidente; rendere evidente; errore evidente
  2. [B2] che si vede bene, facile da notare per chiunque
     - example: Sulla porta c’erano segni evidenti di un tentativo di furto.
     - collocations: segni evidenti; tracce evidenti; in modo evidente; imbarazzo evidente
  3. [C1] facile da fare o da ottenere
     - example: Non è evidente trovare casa in centro con questi prezzi.
     - frames: non è evidente + inf
     - regions: it-CH only
     - grammar: note usually in the negative, non è evidente

### 5. fila

**Lexeme 1: fila**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈfi.la/ fìla (it-IT)
  1. [A2] persone o veicoli che aspettano il proprio turno uno dietro l’altro
     - example: Davanti allo sportello della posta c’era una fila lunghissima.
     - collocations: fare la fila; mettersi in fila; saltare la fila; stare in fila; una lunga fila
  2. [B1] serie di persone o cose poste in linea o che si susseguono
     - example: Preferisco sedermi nelle ultime file, vicino all’uscita.
     - collocations: in prima fila; di fila; in fila indiana; in fila per due; una fila di alberi; doppia fila
  3. [C1] insieme dei membri di un esercito, di un partito o di un gruppo organizzato
     - example: Per molti anni ha militato nelle file dell’opposizione.
     - frames: nelle file di qcs
     - collocations: serrare le file; rompere le file; ingrossare le file
     - grammar: number Plur
- relations: confusable filo (NOUN): le fila, a plural of filo (tirare le fila), is spelled like the singular of fila

### 6. top

**Lexeme 1: top**, NOUN
- grammar: gender Masc; countability Count; invariable True
- said: /ˈtɔp/ top (it-IT)
  1. [B1] indumento femminile leggero e corto che copre il busto, spesso senza maniche
     - example: Con questo caldo metto un top bianco e una gonna lunga.
     - collocations: top senza maniche; top di pizzo; top aderente
  2. [B1] il livello più alto, la cosa o la condizione migliore possibile
     - example: Per chi ama il mare, questa spiaggia è proprio il top.
     - collocations: il top; essere al top; il top della gamma; al top della forma
     - labels: formality informal
     - grammar: number Sing
  3. [B2] lastra che copre i mobili bassi della cucina e serve da piano di lavoro
     - example: Abbiamo scelto un top in quarzo per la cucina nuova.
     - collocations: top della cucina; top in marmo
**Lexeme 2: top**, ADJ
- grammar: invariable True
- said: /ˈtɔp/ top (it-IT)
  1. [B2] di livello altissimo, tra i migliori del suo genere
     - example: Ieri sera abbiamo cenato in un ristorante davvero top.
     - labels: formality informal

### 7. decidere

**Lexeme 1: decidere**, VERB
- grammar: conjugation 2; irregular True
- said: /de.ˈtʃi.de.re/ decìdere (it-IT)
- forms: decido [Mood=Ind, Tense=Pres, Person=1, Number=Sing] decìdo; decidi [Mood=Ind, Tense=Pres, Person=2, Number=Sing] decìdi; decide [Mood=Ind, Tense=Pres, Person=3, Number=Sing] decìde; decidiamo [Mood=Ind, Tense=Pres, Person=1, Number=Plur] decidiàmo; decidono [Mood=Ind, Tense=Pres, Person=3, Number=Plur] decìdono; decisi [Mood=Ind, Tense=Past, Person=1, Number=Sing] decìsi; decise [Mood=Ind, Tense=Past, Person=3, Number=Sing] decìse; deciderò [Mood=Ind, Tense=Fut, Person=1, Number=Sing] deciderò; decida [Mood=Sub, Tense=Pres, Person=1, Number=Sing] decìda; deciso [VerbForm=Part, Tense=Past] decìso
  1. [A2] scegliere che cosa fare, dopo averci pensato
     - example: Quest'anno abbiamo deciso di passare le vacanze in montagna.
     - frames: decidere di + inf; decidere qcs; decidere che + ind
     - collocations: decidere il da farsi; decidere all'unanimità; decidere in fretta; decidere insieme; decidere da solo
     - grammar: valency Tr; auxiliary have
  2. [B1] arrivare finalmente a una scelta dopo molti dubbi
     - example: Dopo mesi di dubbi mi sono deciso a cambiare lavoro.
     - frames: decidersi a + inf
     - collocations: decidersi finalmente
     - grammar: valency Refl; reflexive True; auxiliary be; note pronominal: decidersi
  3. [B2] risolvere in modo definitivo una questione o una lite
     - example: Sarà il tribunale a decidere la causa tra i due vicini.
     - frames: decidere qcs
     - collocations: decidere una controversia; decidere una causa; decidere una questione
     - grammar: valency Tr; auxiliary have
  4. [B2] determinare il risultato o la sorte di qualcosa
     - example: Un rigore all'ultimo minuto ha deciso la partita.
     - frames: decidere qcs; decidere di qcs
     - collocations: decidere la partita; decidere le sorti
     - grammar: valency Both; auxiliary have

### 8. negozio

**Lexeme 1: negozio**, NOUN
- grammar: gender Masc; countability Count
- said: /ne.ˈɡɔt.tsjo/ negòzio (it-IT)
  1. [A1] locale aperto al pubblico dove si vendono merci
     - example: Sotto casa mia hanno aperto un negozio di scarpe.
     - collocations: negozio di alimentari; negozio di abbigliamento; andare per negozi; aprire un negozio; gestire un negozio; negozio online; vetrina del negozio; chiudere il negozio
  2. [C2] accordo o scambio fatto per guadagnare qualcosa
     - example: I due mercanti conclusero il negozio dopo lunghe trattative.
     - collocations: concludere un negozio
     - labels: style literary, time dated

### 9. industriale

**Lexeme 1: industriale**, ADJ
- grammar: comparable False
- said: /in.dus.ˈtrja.le/ industriàle (it-IT)
  1. [B1] delle fabbriche e della produzione di merci in grande quantità con le macchine
     - example: Nella zona industriale della città hanno chiuso molte fabbriche.
     - collocations: zona industriale; rivoluzione industriale; produzione industriale; settore industriale; prodotto industriale; area industriale; città industriale; design industriale; in quantità industriale
     - opposites: artigianale
- relations: adjective_of industria (NOUN)
**Lexeme 2: industriale**, NOUN
- grammar: gender Com; countability Count
- said: /in.dus.ˈtrja.le/ industriàle (it-IT)
  1. [B2] persona che possiede o dirige una grande impresa che produce merci
     - example: Suo nonno era un ricco industriale del settore tessile.
     - collocations: grande industriale; industriale tessile; associazione degli industriali
- relations: derived_from industriale (ADJ)

### 10. influenza

**Lexeme 1: influenza**, NOUN
- grammar: gender Fem; countability Both
- said: /in.flu.ˈɛn.tsa/ influènza (it-IT)
  1. [B1] effetto che una persona o una cosa produce su altre, modificandole
     - example: La musica africana ha avuto una forte influenza sul jazz.
     - frames: influenza su qcn/qcs; influenza di qcn/qcs; sotto l'influenza di qcn/qcs
     - collocations: esercitare un'influenza; subire l'influenza; avere un'influenza; influenza negativa; influenza positiva; forte influenza
     - grammar: countability Both
  2. [B2] potere di condizionare le scelte altrui grazie al prestigio o all'autorità
     - example: Grazie alla sua influenza, riuscì a ottenere un incontro col ministro.
     - collocations: sfera d'influenza; zona d'influenza; persona di grande influenza; usare la propria influenza
     - grammar: countability Mass
  3. [A2] malattia contagiosa causata da un virus, con febbre, tosse e dolori
     - example: Questa settimana resto a casa perché ho l'influenza.
     - collocations: avere l'influenza; prendere l'influenza; influenza stagionale; vaccino contro l'influenza; a letto con l'influenza; picco dell'influenza
     - grammar: countability Mass, mainly

### 11. istruzione

**Lexeme 1: istruzione**, NOUN
- grammar: gender Fem; countability Both
- said: /is.trut.ˈtsjo.ne/ istruzióne (it-IT)
  1. [B1] formazione data dalla scuola e le conoscenze che una persona riceve con essa
     - example: In molti paesi l’istruzione è gratuita fino ai sedici anni.
     - collocations: istruzione obbligatoria; istruzione superiore; istruzione primaria; livello di istruzione; grado di istruzione; diritto all’istruzione; ministero dell’istruzione
     - grammar: countability Mass
  2. [A2] indicazione su come fare o usare qualcosa, o su come comportarsi
     - example: Prima di montare il mobile, leggete con attenzione le istruzioni.
     - frames: istruzioni per qcs; istruzioni su qcs
     - collocations: istruzioni per l’uso; seguire le istruzioni; libretto di istruzioni; dare istruzioni; ricevere istruzioni; attenersi alle istruzioni
     - grammar: countability Count; number Plur, mainly
  3. [C1] singolo comando che un processore esegue all’interno di un programma
     - example: Il programma si blocca quando arriva a quell’istruzione sbagliata.
     - collocations: eseguire un’istruzione
     - labels: domain computing
     - grammar: countability Count
- relations: verbal_noun_of istruire (VERB)

### 12. ricco

**Lexeme 1: ricco**, ADJ
- grammar: comparable True
- said: /ˈrik.ko/ rìcco (it-IT)
  1. [A1] che possiede molto denaro e molti beni
     - example: Da giovane sognava di diventare ricco e comprarsi una villa al mare.
     - collocations: ricco sfondato; diventare ricco; famiglia ricca; paese ricco
     - opposites: povero
  2. [B1] che ha o contiene qualcosa in grande quantità
     - example: Le arance sono ricche di vitamina C e fanno bene d’inverno.
     - frames: ricco di qcs
     - collocations: ricco di vitamine; ricco di storia; terreno ricco; ricco di fascino
     - opposites: povero
  3. [B2] abbondante e generoso nella quantità o nel valore
     - example: Ogni mattina l’albergo offre una ricca colazione con dolci e frutta.
     - collocations: ricca colazione; ricco premio; ricco bottino; ricco programma
     - grammar: position Attr, mainly
  4. [B2] lussuoso, fatto o ornato con materiali preziosi
     - example: Il salone era decorato con ricchi tappeti e lampadari di cristallo.
**Lexeme 2: ricco**, NOUN
- grammar: gender Masc; countability Count; note feminine ricca; often in the plural: i ricchi
- said: /ˈrik.ko/ rìcco (it-IT)
  1. [A2] persona che possiede molto denaro e molti beni
     - example: Secondo molti, le tasse dovrebbero pesare di più sui ricchi.
     - opposites: povero
- relations: derived_from ricco (ADJ)

### 13. tradizionale

**Lexeme 1: tradizionale**, ADJ
- grammar: comparable True, mainly
- said: /tra.dit.tsjo.ˈna.le/ tradizionàle (it-IT)
  1. [B1] trasmesso da una generazione all'altra e legato agli usi di un popolo o di un luogo
     - example: A Natale prepariamo sempre i dolci tradizionali della nonna.
     - collocations: costume tradizionale; cucina tradizionale; piatto tradizionale; musica tradizionale; festa tradizionale; ricetta tradizionale
     - grammar: comparable False
  2. [B2] che segue i modi abituali e consolidati, senza novità o cambiamenti
     - example: Preferisco i metodi tradizionali di insegnamento a quelli più sperimentali.
     - collocations: medicina tradizionale; metodo tradizionale; in modo tradizionale; famiglia tradizionale; mentalità tradizionale
     - grammar: comparable True
- relations: adjective_of tradizione (NOUN)

### 14. live

**Lexeme 1: live**, ADJ
- grammar: invariable True; comparable False
- said: /ˈlajv/ live (it-IT)
  1. [B1] suonato o registrato davanti a un pubblico, non in uno studio
     - example: Questo disco live contiene i brani del loro ultimo tour.
     - collocations: concerto live; album live; musica live; esibizione live
  2. [B2] trasmesso nello stesso momento in cui avviene
     - example: Domani seguiremo la partita in streaming live sul sito.
     - collocations: streaming live; diretta live
**Lexeme 2: live**, NOUN
- grammar: gender Masc; invariable True; countability Count
- said: /ˈlajv/ live (it-IT)
  1. [B2] concerto davanti al pubblico o la sua registrazione
     - example: Il loro nuovo live è uscito in vinile la settimana scorsa.
     - collocations: vedere un live; registrare un live
  2. [B2] trasmissione in diretta, spesso su internet o sui social
     - example: Ogni sera la cantante fa un live per parlare con i fan.
     - collocations: fare un live; andare in live
     - labels: formality informal
     - grammar: note on social media often feminine: una live
**Lexeme 3: live**, ADV
- said: /ˈlajv/ live (it-IT)
  1. [B1] davanti al pubblico o in diretta, senza registrazione precedente
     - example: Stasera il gruppo suona live nella piazza del paese.
     - collocations: suonare live; cantare live; trasmettere live

### 15. elemento

**Lexeme 1: elemento**, NOUN
- grammar: gender Masc; countability Count
- said: /e.le.ˈmen.to/ eleménto (it-IT)
  1. [B1] parte semplice che, insieme ad altre, forma un tutto
     - example: La fiducia è un elemento fondamentale in ogni rapporto di coppia.
     - frames: elemento di qcs
     - collocations: elemento fondamentale; elemento essenziale; elemento chiave; elemento di novità; elementi di prova
  2. [B2] persona considerata come membro di un gruppo, spesso per le sue capacità
     - example: Quel ragazzo è un ottimo elemento, lo assumerei subito.
     - collocations: un ottimo elemento; un elemento di spicco; elemento di disturbo
  3. [B2] sostanza formata da atomi di un solo tipo, che non si può scomporre
     - example: Nella tavola periodica gli elementi sono ordinati per numero atomico.
     - collocations: elemento chimico; tavola periodica degli elementi
     - labels: domain chemistry
  4. [B2] ciascun pezzo che si unisce ad altri per formare un oggetto o una struttura
     - example: Abbiamo comprato una libreria a elementi da montare in salotto.
     - collocations: mobile a elementi; elemento del termosifone
  5. [C1] nozioni di base di una disciplina o di un'arte
     - example: Il corso insegna gli elementi della programmazione in poche settimane.
     - frames: elementi di qcs
     - grammar: number Plur
  6. [C1] ciascuna delle sostanze che secondo gli antichi formavano tutte le cose
     - example: Gli antichi filosofi greci parlavano di quattro elementi della natura.
     - collocations: i quattro elementi; essere nel proprio elemento
     - labels: domain philosophy
  7. [C1] forze della natura quando si manifestano con violenza
     - example: Il vecchio marinaio lottò per ore contro la furia degli elementi.
     - collocations: la furia degli elementi; lo scatenarsi degli elementi
     - labels: style literary
     - grammar: number Plur

### 16. blocco

**Lexeme 1: blocco**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈblɔk.ko/ blòcco (it-IT)
- forms: blocchi [Number=Plur] blòcchi
  1. [B1] pezzo grande e compatto di un materiale solido
     - example: Lo scultore ha ricavato la statua da un unico blocco di marmo.
     - frames: blocco di qcs
     - collocations: blocco di marmo; blocco di ghiaccio; blocco di pietra; blocco di cemento
  2. [B2] arresto o interruzione di un movimento, di un'attività o di un funzionamento
     - example: Per un incidente in autostrada c'è un lungo blocco del traffico.
     - collocations: posto di blocco; blocco stradale; blocco del traffico; blocco degli stipendi; blocco cardiaco; blocco degli affitti
  3. [B1] insieme di fogli uniti da un lato che si staccano facilmente
     - example: Tengo sempre un blocco accanto al telefono per prendere appunti.
     - collocations: blocco per appunti; blocco da disegno
  4. [C1] chiusura delle vie di accesso a un luogo per isolarlo
     - example: Durante la guerra il blocco navale impediva l'arrivo dei rifornimenti.
     - collocations: blocco navale; blocco economico; forzare il blocco; rompere il blocco
  5. [B2] improvvisa incapacità di agire, pensare o parlare per cause psicologiche
     - example: Davanti al foglio bianco mi viene sempre un blocco.
     - collocations: blocco psicologico; blocco dello scrittore; avere un blocco
  6. [B2] alleanza di stati, partiti o gruppi con interessi comuni
     - example: Negli anni Cinquanta il mondo era diviso in due blocchi contrapposti.
     - collocations: blocco sovietico; blocco occidentale; blocco orientale
  7. [B2] insieme di cose considerate come un tutto unico
     - example: Il negozio ha venduto in blocco tutta la merce rimasta.
     - collocations: in blocco; comprare in blocco

### 17. agenzia

**Lexeme 1: agenzia**, NOUN
- grammar: gender Fem; countability Count
- said: /a.dʒen.ˈtsi.a/ agenzìa (it-IT)
  1. [A2] impresa che offre servizi o cura affari per conto dei clienti
     - example: Abbiamo prenotato il viaggio in un’agenzia vicino alla stazione.
     - frames: agenzia di qcs
     - collocations: agenzia di viaggi; agenzia immobiliare; agenzia pubblicitaria; agenzia di stampa; agenzia matrimoniale; agenzia per il lavoro; agenzia di pompe funebri
  2. [B1] ufficio di una banca o di una società che lavora in una zona lontana dalla sede
     - example: La mia banca ha aperto una nuova agenzia nel quartiere.
     - collocations: agenzia bancaria; agenzia di banca
  3. [B2] ente pubblico che svolge compiti tecnici o amministrativi in un settore preciso
     - example: Le agenzie europee controllano la sicurezza dei farmaci e degli alimenti.
     - collocations: agenzia governativa; agenzia spaziale
- relations: derived_from agente (NOUN)

### 18. utente

**Lexeme 1: utente**, NOUN
- grammar: gender Com; countability Count
- said: /u.ˈtɛn.te/ utènte (it-IT)
  1. [B1] persona che si serve di un bene o di un servizio, spesso pubblico
     - example: Da lunedì gli utenti del treno pagheranno un biglietto più caro.
     - frames: utente di qcs
     - collocations: utenti della strada; utente finale; servizio clienti per gli utenti; utenti del servizio pubblico
  2. [B1] persona che usa un computer, un programma o un sito internet
     - example: Inserisci il nome utente e la password per entrare nel sito.
     - collocations: nome utente; account utente; profilo utente; utente registrato
     - labels: domain computing

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
  1. [B1] usato per introdurre la conseguenza di quanto si è appena detto
     - example: Il treno era in ritardo e pertanto siamo arrivati tardi alla riunione.

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
  1. [B1] uccisione di un essere umano compiuta da un'altra persona
     - example: La polizia sta ancora indagando sull'omicidio avvenuto in periferia.
     - collocations: commettere un omicidio; omicidio colposo; omicidio volontario; omicidio premeditato; tentato omicidio; omicidio stradale; accusare di omicidio; indagare su un omicidio

### 23. insaputa

**Lexeme 1: insaputa**, NOUN
- grammar: gender Fem; number Sing; countability Mass; note only in all'insaputa di qcn and a mia, tua, sua... insaputa
- said: /in.sa.ˈpu.ta/ insapùta (it-IT)
  1. [B2] mancanza di informazione da parte di qualcuno su ciò che accade
     - example: Hanno organizzato la festa all'insaputa di mio padre.
     - frames: all'insaputa di qcn
     - collocations: a mia insaputa; a sua insaputa; a loro insaputa; a tua insaputa

### 24. primavera

**Lexeme 1: primavera**, NOUN
- grammar: gender Fem; countability Count
- said: /pri.ma.ˈvɛ.ra/ primavèra (it-IT)
  1. [A1] stagione dell'anno che viene dopo l'inverno e prima dell'estate
     - example: In primavera gli alberi del parco si riempiono di fiori.
     - collocations: in primavera; primavera inoltrata; l'arrivo della primavera; l'inizio della primavera; fiori di primavera
  2. [B2] periodo iniziale di qualcosa, pieno di energia, speranza e rinnovamento
     - example: Quegli anni furono una vera primavera per il cinema del paese.
     - collocations: la primavera della vita
     - labels: figurative True
  3. [B2] anni di età di una persona
     - example: Mia nonna ha appena compiuto novanta primavere ed è ancora attivissima.
     - labels: formality informal, attitude humorous, figurative True
     - grammar: number Plur

### 25. vento

**Lexeme 1: vento**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈvɛn.to/ vènto (it-IT)
  1. [A1] movimento dell'aria che si sposta, più o meno forte, nell'atmosfera
     - example: Oggi tira un vento così forte che è difficile camminare.
     - collocations: tira vento; c'è vento; soffia il vento; vento forte; raffica di vento; colpo di vento; contro vento; mulino a vento; giacca a vento; rosa dei venti
  2. [B2] tendenza o clima che si diffonde e cambia una situazione
     - example: Nel paese si sente un vento di cambiamento che nessuno può fermare.
     - frames: vento di qcs
     - collocations: vento di cambiamento; il vento è cambiato; vento di rivolta; vento di novità
     - labels: figurative True
     - grammar: countability Count

### 26. facoltà

**Lexeme 1: facoltà**, NOUN
- grammar: gender Fem; countability Count; invariable True
- said: /fa.kol.ˈta/ facoltà (it-IT)
  1. [B1] parte di un'università che riunisce gli studi e i corsi di una disciplina
     - example: Dopo il diploma mio fratello si è iscritto alla facoltà di medicina.
     - frames: facoltà di qcs
     - collocations: iscriversi a una facoltà; cambiare facoltà; facoltà di lettere; facoltà di giurisprudenza; preside di facoltà
     - grammar: note officially replaced by departments since 2010, but still common in everyday use
  2. [B2] capacità naturale della mente o del corpo di fare qualcosa
     - example: Nonostante l'età, il nonno conserva intatte tutte le sue facoltà mentali.
     - frames: facoltà di + inf
     - collocations: facoltà mentali; facoltà intellettive; nel pieno possesso delle proprie facoltà; facoltà di parola
  3. [C1] diritto o permesso di decidere o di fare qualcosa
     - example: Ogni cliente ha facoltà di recedere dal contratto entro quattordici giorni.
     - frames: facoltà di + inf
     - collocations: avere facoltà di; concedere la facoltà; è facoltà di; rientrare nelle facoltà
     - labels: formality formal

### 27. ritardo

**Lexeme 1: ritardo**, NOUN
- grammar: gender Masc; countability Both
- said: /ri.ˈtar.do/ ritàrdo (it-IT)
  1. [A2] tempo in più rispetto al momento stabilito o previsto per qualcosa
     - example: Scusate, sono in ritardo perché il treno non partiva più.
     - collocations: essere in ritardo; arrivare in ritardo; con ritardo; in leggero ritardo; accumulare ritardo; scusarsi per il ritardo; senza ritardo; ritardo del volo
     - opposites: anticipo
  2. [B2] condizione di chi o di ciò che resta indietro rispetto al livello normale o atteso
     - example: Il nostro paese ha un grave ritardo nell’uso delle nuove tecnologie.
     - frames: ritardo in qcs; ritardo rispetto a qcn/qcs
     - collocations: ritardo mentale; ritardo nello sviluppo; recuperare il ritardo; colmare il ritardo
- relations: derived_from ritardare (VERB)

### 28. lago

**Lexeme 1: lago**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈla.ɡo/ làgo (it-IT)
- forms: laghi [Number=Plur] làghi
  1. [A2] ampia massa d'acqua ferma circondata interamente da terra
     - example: D'estate ci piace fare il bagno nel lago vicino a casa.
     - collocations: sul lago; riva del lago; lago artificiale; lago alpino; lago di montagna; lago salato
  2. [B2] grande quantità di liquido sparsa su una superficie
     - example: Il ferito giaceva immobile in un lago di sangue.
     - frames: un lago di qcs
     - collocations: un lago di sangue; un lago di sudore; un lago di lacrime
     - labels: figurative True

### 29. nemico

**Lexeme 1: nemico**, NOUN
- grammar: gender Masc; countability Count; note feminine nemica for a woman
- said: /ne.ˈmi.ko/ nemìco (it-IT)
- forms: nemici [Gender=Masc, Number=Plur] nemìci; nemica [Gender=Fem, Number=Sing] nemìca; nemiche [Gender=Fem, Number=Plur] nemìche
  1. [B1] persona ostile a qualcuno, che lo odia o cerca di fargli del male
     - example: Con quel carattere difficile si è fatto molti nemici in ufficio.
     - frames: nemico di qcn
     - collocations: farsi dei nemici; nemico giurato; acerrimo nemico; peggior nemico; nemico mortale
     - opposites: amico
  2. [B1] l’esercito o il paese contro cui si combatte in una guerra
     - example: All’alba i soldati ricevettero l’ordine di attaccare il nemico.
     - collocations: attaccare il nemico; passare al nemico; arrendersi al nemico; cadere in mano al nemico
     - grammar: number Sing, mainly; note usually il nemico, for the whole opposing side
  3. [B2] cosa che danneggia od ostacola qualcuno o qualcosa
     - example: Lo stress è il primo nemico di un sonno tranquillo.
     - frames: nemico di qcs
     - collocations: nemico numero uno
     - labels: figurative True
**Lexeme 2: nemico**, ADJ
- grammar: comparable False
- said: /ne.ˈmi.ko/ nemìco (it-IT)
- forms: nemici [Gender=Masc, Number=Plur] nemìci
  1. [B1] ostile, o che appartiene alla parte contro cui si combatte
     - example: Gli aerei hanno sorvolato il territorio nemico durante la notte.
     - collocations: territorio nemico; fuoco nemico; truppe nemiche; linee nemiche; paese nemico
     - grammar: position Attr, mainly
     - opposites: amico
  2. [B2] contrario a qualcosa, oppure dannoso per qualcosa
     - example: Mio nonno è sempre stato nemico dei telefoni cellulari.
     - frames: essere nemico di qcs
     - collocations: il meglio è nemico del bene
     - grammar: position Pred, mainly

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
- forms: riesco [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] rièsco; riesci [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] rièsci; riesce [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] rièsce; riusciamo [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] riusciàmo; riescono [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] rièscono; riuscii [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] riuscìi; riuscì [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] riuscì; riuscirò [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] riuscirò; riesca [Mood=Sub, Tense=Pres, Person=1,2,3, Number=Sing, VerbForm=Fin] rièsca; riuscito [VerbForm=Part, Tense=Past] riuscìto
  1. [A1] avere la capacità o la possibilità di fare qualcosa e farla davvero
     - example: Con questo rumore non riesco proprio a dormire.
     - frames: riuscire a + inf
     - collocations: riuscirci; non riuscire a capire
  2. [B1] avere un certo esito, buono o cattivo
     - example: Se segui bene la ricetta, la torta riuscirà benissimo.
     - frames: qcs riesce + avv
     - collocations: riuscire bene; riuscire male
  3. [B2] avere successo in un’attività, in un campo o in un’impresa
     - example: Grazie alla sua costanza, mio fratello è riuscito negli studi.
     - frames: riuscire in qcs
     - collocations: riuscire nella vita; riuscire nell’intento; riuscire nell’impresa
     - opposites: fallire
  4. [B2] risultare o apparire in un certo modo a qualcuno
     - example: Mi riesce difficile credere a questa storia.
     - frames: riuscire + agg a qcn
     - collocations: riuscire difficile; riuscire facile; riuscire simpatico; riuscire gradito
  5. [C1] andare fuori di nuovo da un luogo dopo esserci entrati
     - example: Sono rientrata a prendere l’ombrello e poi sono riuscita subito.
     - frames: riuscire da qcs
- relations: derived_from uscire (VERB)

### 32. aumentare

**Lexeme 1: aumentare**, VERB
- grammar: conjugation 1; irregular False; valency Both; auxiliary both
- said: /aw.men.ˈta.re/ aumentàre (it-IT)
  1. [A2] rendere più grande per quantità, numero, misura o forza
     - example: Quest'anno l'azienda ha deciso di aumentare gli stipendi a tutti.
     - frames: aumentare qcs; aumentare qcs di qcs
     - collocations: aumentare i prezzi; aumentare lo stipendio; aumentare la velocità; aumentare il volume; aumentare il rischio
     - grammar: valency Tr; auxiliary have
     - opposites: diminuire
  2. [A2] diventare più grande per quantità, numero, misura o forza
     - example: Negli ultimi mesi il prezzo della benzina è aumentato molto.
     - frames: qcs aumenta; aumentare di qcs
     - collocations: aumentare di peso; aumentare notevolmente; aumentare costantemente; aumentare di valore
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
- grammar: gender Masc; countability Both
- said: /do.ˈmi.njo/ domìnio (it-IT)
  1. [B2] potere e controllo esercitato su persone, popoli o territori
     - example: Per secoli la regione è rimasta sotto il dominio straniero.
     - frames: dominio su qcn/qcs; dominio di qcn
     - collocations: sotto il dominio; dominio straniero; dominio coloniale; esercitare il dominio; liberarsi dal dominio
     - grammar: countability Mass
  2. [B2] padronanza, capacità di controllare bene qualcosa o se stessi
     - example: Dopo un anno all’estero ha un ottimo dominio della lingua.
     - frames: dominio di qcs
     - collocations: dominio di sé; perdere il dominio di sé
     - grammar: countability Mass
  3. [B2] parte di un indirizzo internet che identifica un sito o una rete
     - example: Abbiamo registrato un nuovo dominio per il sito del negozio.
     - collocations: nome di dominio; registrare un dominio
     - labels: domain computing
     - grammar: countability Count
  4. [C1] campo di attività, di studio o di conoscenza
     - example: Questa domanda esce dal dominio della medicina.
     - collocations: di dominio pubblico
     - grammar: countability Count
  5. [C1] territorio che appartiene a uno stato o a un signore
     - example: I domini dell’impero si estendevano fino al mare.
     - grammar: countability Count; note usually plural

### 36. fisico

**Lexeme 1: fisico**, ADJ
- grammar: comparable False
- said: /ˈfi.zi.ko/ fìsico (it-IT)
- forms: fisica [Gender=Fem, Number=Sing] fìsica; fisici [Gender=Masc, Number=Plur] fìsici; fisiche [Gender=Fem, Number=Plur] fìsiche
  1. [A2] che riguarda il corpo di una persona e le sue condizioni
     - example: Dopo una giornata in cantiere sento una grande stanchezza fisica.
     - collocations: attività fisica; esercizio fisico; educazione fisica; aspetto fisico; forma fisica; dolore fisico; sforzo fisico; contatto fisico
  2. [B2] che riguarda la scienza che studia la materia, l’energia e le loro leggi
     - example: Il ghiaccio che si scioglie è un fenomeno fisico, non chimico.
     - collocations: leggi fisiche; proprietà fisiche; fenomeno fisico; grandezza fisica
  3. [B2] che riguarda la realtà materiale e concreta, non il pensiero o lo spirito
     - example: Molti clienti preferiscono ancora il negozio fisico agli acquisti online.
     - collocations: presenza fisica; mondo fisico; spazio fisico; carta fisica
**Lexeme 2: fisico**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈfi.zi.ko/ fìsico (it-IT)
- forms: fisici [Number=Plur] fìsici; fisica [Gender=Fem, Number=Sing] fìsica; fisiche [Gender=Fem, Number=Plur] fìsiche
  1. [B1] il corpo di una persona, per la sua forma, struttura e resistenza
     - example: Nuota tutti i giorni e ha un fisico atletico.
     - collocations: avere un bel fisico; fisico atletico; fisico asciutto; fisico robusto; fisico gracile; curare il fisico
  2. [B1] persona esperta della scienza che studia la materia e l’energia
     - example: Da grande vorrei fare il fisico e lavorare in un laboratorio.
     - collocations: fisico nucleare; fisico teorico; fisico sperimentale

### 37. ristorante

**Lexeme 1: ristorante**, NOUN
- grammar: gender Masc; countability Count
- said: /ri.sto.ˈran.te/ ristorànte (it-IT)
  1. [A1] locale pubblico dove si servono pasti completi a pagamento
     - example: Stasera ceniamo nel piccolo ristorante sotto casa per festeggiare.
     - collocations: andare al ristorante; mangiare al ristorante; cenare al ristorante; prenotare al ristorante; ristorante di pesce; aprire un ristorante; gestire un ristorante

### 38. comunale

**Lexeme 1: comunale**, ADJ
- grammar: comparable False; position Attr, mainly
- said: /ko.mu.ˈna.le/ comunàle (it-IT)
  1. [B1] che appartiene all’ente che amministra una città o un paese, o ne dipende
     - example: Ogni mattina studio nella biblioteca comunale vicino alla piazza.
     - collocations: consiglio comunale; consigliere comunale; giunta comunale; elezioni comunali; biblioteca comunale; polizia comunale; piscina comunale; tasse comunali
  2. [C1] proprio delle città italiane del Medioevo che si governavano da sole
     - example: Nell’età comunale molte città italiane diventarono ricche e potenti.
     - collocations: età comunale; periodo comunale; città comunale
     - labels: domain history
- relations: adjective_of comune (NOUN)

### 39. biglietto

**Lexeme 1: biglietto**, NOUN
- grammar: gender Masc; countability Count
- said: /biʎ.ˈʎet.to/ bigliétto (it-IT)
  1. [A1] tagliando che dà diritto a viaggiare, entrare in un luogo o partecipare a un'estrazione
     - example: Ricordati di timbrare il biglietto prima di salire sul treno.
     - frames: biglietto per qcs; biglietto per + luogo
     - collocations: fare il biglietto; timbrare il biglietto; obliterare il biglietto; biglietto di andata e ritorno; biglietto di sola andata; biglietto omaggio; controllare i biglietti; biglietto della lotteria
  2. [A2] piccolo foglio o cartoncino su cui si scrive un messaggio
     - example: Ti ho lasciato un biglietto sul tavolo della cucina.
     - collocations: biglietto di auguri; biglietto da visita; lasciare un biglietto; biglietto di condoglianze; scrivere un biglietto
  3. [B1] pezzo di carta stampato che si usa come denaro
     - example: Alla cassa paga sempre con un biglietto da cento euro.
     - collocations: biglietto di banca; biglietto da cento euro

### 40. divertente

**Lexeme 1: divertente**, ADJ
- grammar: comparable True
- said: /di.ver.ˈtɛn.te/ divertènte (it-IT)
  1. [A2] che fa passare il tempo in modo piacevole o che fa ridere
     - example: Mio fratello racconta sempre storie divertenti quando siamo a cena.
     - frames: è divertente + inf; trovare divertente qcs
     - collocations: un film divertente; una serata divertente; una persona divertente; trovare divertente
     - opposites: noioso
- relations: derived_from divertire (VERB)

### 41. successivo

**Lexeme 1: successivo**, ADJ
- grammar: comparable False
- said: /sut.tʃes.ˈsi.vo/ successìvo (it-IT)
  1. [B1] che viene dopo, nel tempo, nello spazio o in un ordine
     - example: La lezione successiva sarà dedicata agli esercizi di grammatica.
     - frames: successivo a qcs
     - collocations: il giorno successivo; l'anno successivo; nei giorni successivi; in un momento successivo; in una fase successiva
     - opposites: precedente

### 42. spiegare

**Lexeme 1: spiegare**, VERB
- grammar: conjugation 1; irregular False; auxiliary both; note adds h before e and i: spieghi, spieghiamo
- said: /spje.ˈɡa.re/ spiegàre (it-IT)
  1. [A2] rendere chiaro e comprensibile qualcosa a qualcuno, con parole o esempi
     - example: La maestra spiega la regola alla lavagna con molti esempi.
     - frames: spiegare qcs a qcn; spiegare a qcn che + ind; spiegare perché + ind
     - collocations: spiegare la lezione; spiegare il motivo; spiegare bene; spiegare nel dettaglio; spiegare in parole semplici
     - grammar: valency Tr; auxiliary have
  2. [B1] essere la causa o la ragione di un fatto
     - example: La pioggia di ieri spiega perché il campo è così fangoso.
     - frames: qcs spiega qcs
     - grammar: valency Tr; auxiliary have
  3. [B2] aprire e distendere una cosa piegata o raccolta
     - example: Prima di uscire dal porto i marinai spiegano le vele.
     - collocations: spiegare le vele; spiegare le ali; spiegare la bandiera
     - grammar: valency Tr; auxiliary have
     - opposites: piegare
  4. [B1] esprimersi in modo chiaro e farsi capire dagli altri
     - example: Forse non mi sono spiegato bene, ripeto tutto da capo.
     - collocations: mi spiego?; spiegarsi meglio
     - grammar: valency Refl; reflexive True; auxiliary be
  5. [B2] riuscire a capire la ragione o il modo di un fatto
     - example: Ancora non mi spiego come abbia potuto dimenticare l'appuntamento.
     - frames: spiegarsi qcs; spiegarsi come + cong; spiegarsi perché + cong
     - collocations: non riuscire a spiegarsi
     - grammar: valency Refl; reflexive True; auxiliary be; note the pronoun means 'to oneself'; mostly with a negation

### 43. morale

**Lexeme 1: morale**, ADJ
- said: /mo.ˈra.le/ moràle (it-IT)
  1. [B1] legato ai principi del bene e del male che guidano il comportamento umano
     - example: Ogni scelta di questo tipo pone un problema morale difficile.
     - collocations: valori morali; giudizio morale; coscienza morale; dovere morale; questione morale; principi morali
     - grammar: comparable False
  2. [B2] onesto e giusto, che si comporta secondo i principi del bene
     - example: Non mi sembra una decisione molto morale, sinceramente.
     - grammar: comparable True
     - opposites: immorale
  3. [B2] che tocca lo spirito e i sentimenti, non le cose materiali
     - example: In quel periodo difficile mi hanno dato un grande sostegno morale.
     - collocations: sostegno morale; danno morale; vincitore morale; obbligo morale
     - grammar: comparable False
**Lexeme 2: morale**, NOUN
- grammar: gender Fem; countability Both
- said: /mo.ˈra.le/ moràle (it-IT)
  1. [B2] insieme di regole e valori che stabiliscono come è giusto comportarsi
     - example: La morale di quell'epoca era molto più severa di oggi.
     - collocations: morale comune; morale cattolica; fare la morale; senso della morale
     - grammar: countability Mass
  2. [B1] insegnamento che si ricava da una storia o da un fatto
     - example: Alla fine di ogni favola c'è sempre una morale da capire.
     - collocations: la morale della favola; trarre la morale
     - grammar: countability Count
- relations: derived_from morale (ADJ)
**Lexeme 3: morale**, NOUN
- grammar: gender Masc; countability Mass; number Sing
- said: /mo.ˈra.le/ moràle (it-IT)
  1. [B1] stato d'animo di una persona o di un gruppo, quanto a fiducia e coraggio
     - example: Dopo la vittoria il morale della squadra è altissimo.
     - collocations: tirare su il morale; alzare il morale; giù di morale; morale alto; morale basso; avere il morale a terra
- relations: derived_from morale (ADJ)

### 44. montagna

**Lexeme 1: montagna**, NOUN
- grammar: gender Fem
- said: /mon.ˈtaɲ.ɲa/ montàgna (it-IT)
  1. [A1] grande rilievo naturale del terreno che si alza molto sopra la zona intorno
     - example: Dalla finestra dell’albergo si vede una montagna coperta di neve.
     - collocations: scalare una montagna; cima della montagna; ai piedi della montagna; catena di montagne; montagna innevata
     - grammar: countability Count
  2. [A2] territorio con rilievi alti, come luogo dove si vive o si va in vacanza
     - example: Ogni estate passiamo due settimane in montagna con i nonni.
     - collocations: andare in montagna; vacanze in montagna; aria di montagna; gente di montagna; rifugio di montagna
     - grammar: number Sing; countability Mass; note usually la montagna or in montagna
  3. [B1] quantità molto grande di cose
     - example: Stasera devo ancora finire una montagna di compiti.
     - frames: una montagna di qcs
     - collocations: una montagna di soldi; una montagna di lavoro
     - labels: figurative True
     - grammar: countability Count
  4. [B2] persona molto alta e robusta
     - example: Il nuovo portiere della squadra è una vera montagna.
     - labels: figurative True, formality informal
     - grammar: countability Count

### 45. senato

**Lexeme 1: senato**, NOUN
- grammar: gender Masc; countability Count; note usually written Senato when naming a particular chamber
- said: /se.ˈna.to/ senàto (it-IT)
  1. [B1] una delle due assemblee che formano il parlamento in molti stati
     - example: Domani il disegno di legge passa all’esame del Senato.
     - collocations: presidente del Senato; Senato della Repubblica; passare al Senato; approvare al Senato; seggio al Senato; aula del Senato; eletto al Senato
  2. [B2] assemblea di cittadini illustri che guidava lo stato nell’antica Roma
     - example: Nell’antica Roma il senato riuniva i membri delle famiglie più potenti.
     - collocations: senato romano
     - labels: time historical
  3. [C1] organo che dirige un’università, formato da professori e rappresentanti
     - example: Il senato accademico ha approvato il nuovo calendario degli esami.
     - collocations: senato accademico
