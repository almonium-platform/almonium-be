# Italian: two entries for each word (11 words)

Each word below has two entries of a learner's dictionary of Italian, A and B, written by two different writers
under the same rules. You are not told which writer wrote which, and the sides change from word to word. Some of
these words are hard on purpose: two words under one spelling, forms that are not words of their own, senses of
one country only, rude words.

For each word say which entry serves a learner better, and name the faults of each. A writer may have refused a
word as no dictionary word of its own ("refused: inflected form of ..."); that is right for an inflected form, an
abbreviation or a name, and wrong for a word a dictionary of Italian lists.

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

One line of JSON for every word, in the order given, inside a single ```json block:

```json
{"id": 1, "better": "A", "why": "B lacks the metal, the commoner of the two words spelt lead", "A": [], "B": [{"fault": "missing-core", "where": "lexemes", "what": "the metal is missing"}]}
```

"better" is "A", "B" or "same": "same" only when you could not say which you would rather give a learner. "why" is
one sentence. "A" and "B" list each entry's faults as above, empty when it has none.

## The words

### 1. ancora

#### Entry A

**Lexeme 1: ancora**, ADV
- said: /an.ˈko.ra/ ancóra (it-IT)
  1. [A1] anche nel momento di cui si parla, continuando come prima
     - example: Sono già le dieci di sera e lavoriamo ancora in ufficio.
     - collocations: non ancora; ancora oggi; ancora adesso; ancora vivo
  2. [A1] in quantità o in misura maggiore, in aggiunta a prima
     - example: Vuoi ancora un po’ di tè caldo o basta così?
     - collocations: ancora un po’; ancora più; ancora meno; ancora meglio; ancora peggio
  3. [A1] di nuovo, un’altra volta rispetto a quanto è già avvenuto
     - example: Ho bussato ancora alla porta, ma dentro non rispondeva nessuno.
     - collocations: ancora una volta; provare ancora; succedere ancora
**Lexeme 2: ancora**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈan.ko.ra/ àncora (it-IT)
  1. [B1] pesante oggetto di metallo calato in acqua per tenere ferma un’imbarcazione
     - example: Prima del tramonto l’equipaggio ha gettato l’ancora nella piccola baia.
     - collocations: gettare l’ancora; levare l’ancora; calare l’ancora; essere all’ancora
  2. [B2] persona o cosa che dà sicurezza e aiuto nelle difficoltà
     - example: Il suo lavoro è stato un’ancora di salvezza durante la crisi.
     - collocations: ancora di salvezza
     - labels: figurative True

#### Entry B

**Lexeme 1: ancora**, ADV
- said: /an.ˈko.ra/ ancóra (it-IT, ancora), /an.ˈkor/ ancor (it-IT, ancor: truncated form, mostly before a consonant: ancor più)
- spellings: ancora (it-IT); ancor (it-IT)
  1. [A1] anche adesso o fino al momento indicato, senza interruzione
     - example: Alle undici di sera mio fratello stava ancora studiando.
     - collocations: non ancora; ancora oggi; ancora adesso
  2. [A2] un’altra volta, ripetendo ciò che è già avvenuto
     - example: Puoi spiegarmi ancora come si arriva alla stazione?
     - collocations: ancora una volta
  3. [A2] in aggiunta a quanto c’è già o è già stato dato
     - example: Vuoi ancora un po’ di pasta o hai finito?
     - collocations: ancora un po’; ancora cinque minuti
  4. [B1] usato davanti a un comparativo per renderlo più forte
     - example: Oggi fa ancora più freddo di ieri sera.
     - frames: ancora più + agg; ancora meno + agg
     - collocations: ancora di più; ancor prima
**Lexeme 2: ancora**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈan.ko.ra/ àncora (it-IT)
  1. [B1] attrezzo di metallo che, calato sul fondo, tiene ferma una nave
     - example: La barca ha gettato l’ancora in una piccola baia tranquilla.
     - collocations: gettare l’ancora; levare l’ancora; salpare l’ancora; all’ancora
  2. [B2] persona o cosa che offre aiuto e sicurezza in un momento difficile
     - example: Per lui quel lavoro è stato un’ancora di salvezza.
     - collocations: ancora di salvezza
     - labels: figurative True
- relations: confusable ancora (ADV): spelt the same but stressed on the second syllable (ancóra, still/again) instead of the first (àncora)

### 2. pesca

#### Entry A

**Lexeme 1: pesca**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈpɛs.ka/ pèsca (it-IT)
  1. [A2] frutto estivo rotondo e dolce, con polpa succosa e un grosso nocciolo
     - example: Per merenda ho preso una pesca ben matura dal cestino della frutta.
     - collocations: pesca gialla; pesca bianca; pesca noce; tè alla pesca; succo alla pesca; color pesca
**Lexeme 2: pesca**, NOUN
- grammar: gender Fem; countability Both
- said: /ˈpes.ka/ pésca (it-IT)
  1. [A2] attività con cui si catturano animali acquatici per lavoro, commercio o sport
     - example: Mio zio si sveglia presto la domenica per andare a pesca.
     - collocations: andare a pesca; canna da pesca; barca da pesca; rete da pesca; pesca sportiva; pesca subacquea
     - grammar: countability Mass
  2. [B1] quantità di animali acquatici catturati in una volta sola
     - example: Nonostante il vento forte, ieri abbiamo fatto davvero una buona pesca.
     - collocations: fare una buona pesca; pesca abbondante
     - grammar: countability Count; note usually singular
  3. [B2] gioco in cui si paga un biglietto per estrarre a sorte un premio
     - example: Alla festa della scuola organizzeremo una pesca di beneficenza per i bambini.
     - collocations: pesca di beneficenza
     - grammar: countability Count
- relations: derived_from pescare (VERB)

#### Entry B

**Lexeme 1: pesca**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈpɛs.ka/ pèsca (it-IT)
  1. [A2] frutto rotondo con buccia vellutata, polpa dolce e succosa e un grosso nocciolo
     - example: D'estate mi piace mangiare una pesca fresca dopo pranzo.
     - collocations: pesche sciroppate; succo di pesca; tè alla pesca; marmellata di pesche; sbucciare una pesca
**Lexeme 2: pesca**, NOUN
- grammar: gender Fem
- said: /ˈpes.ka/ pésca (it-IT)
  1. [A2] attività di catturare animali che vivono nell'acqua, per lavoro o per sport
     - example: Ogni domenica mattina mio nonno va a pesca sul lago.
     - collocations: andare a pesca; canna da pesca; pesca sportiva; pesca subacquea; pesca a strascico; pesca d'altura; divieto di pesca
     - grammar: countability Mass
  2. [B2] gioco a premi in cui si estraggono a sorte dei biglietti
     - example: Alla festa del paese hanno organizzato una pesca di beneficenza.
     - collocations: pesca di beneficenza; biglietto della pesca
     - grammar: countability Count
- relations: verbal_noun_of pescare (VERB), for sense 1 only

### 3. stato

#### Entry A

**Lexeme 1: stato**, NOUN
- grammar: gender Masc
- said: /ˈsta.to/ stàto (it-IT)
  1. [A2] modo di essere o condizione in cui si trova qualcuno o qualcosa
     - example: Nonostante gli anni, la vecchia bicicletta è ancora in buono stato.
     - frames: in stato di qcs
     - collocations: in buono stato; in cattivo stato; stato di salute; stato d'animo; stato di emergenza; allo stato liquido; stato interessante
     - grammar: countability Count
  2. [B1] comunità di persone che vive su un territorio con un proprio governo e proprie leggi
     - example: Lo Stato deve garantire a tutti un'istruzione gratuita.
     - collocations: colpo di Stato; capo dello Stato; uomo di Stato; segreto di Stato; esame di Stato; Stato membro; Stato sociale
     - grammar: countability Count; note often written with a capital letter: Stato
  3. [B2] posizione di una persona rispetto alla legge o alla società
     - example: Nel modulo bisogna indicare anche il proprio stato civile.
     - collocations: stato civile; stato di famiglia
     - grammar: countability Mass

#### Entry B

**Lexeme 1: stato**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈsta.to/ stàto (it-IT)
  1. [A2] condizione o modo di essere di qualcuno o qualcosa in un dato momento
     - example: Dopo tanti anni la vecchia bicicletta è ancora in ottimo stato.
     - collocations: in buono stato; in cattivo stato; stato di salute; stato d'animo; stato di emergenza; allo stato attuale; stato liquido
  2. [A2] organizzazione politica che governa un territorio, o il paese stesso
     - example: Diversi stati confinanti hanno firmato un nuovo accordo sul trasporto ferroviario.
     - collocations: capo di stato; colpo di stato; segreto di stato; esame di stato; stato di diritto; stato sociale; stato membro
     - grammar: note often capitalized as Stato when referring to public institutions
  3. [B1] posizione giuridica o sociale di una persona nella famiglia o nella società
     - example: Nel modulo devi indicare il tuo indirizzo e lo stato civile.
     - collocations: stato civile; stato di famiglia; stato libero

### 4. porto

#### Entry A

**Lexeme 1: porto**, NOUN
- grammar: gender Masc
- said: /ˈpɔr.to/ pòrto (it-IT)
  1. [A2] luogo riparato sulla costa dove le navi si fermano, caricano e scaricano
     - example: Ogni mattina i pescatori rientrano in porto con le reti piene.
     - collocations: entrare in porto; lasciare il porto; porto turistico; porto commerciale; porto franco; capitaneria di porto
     - grammar: countability Count
  2. [B2] luogo o condizione che offre riparo e sicurezza, o meta raggiunta con successo
     - example: Dopo tanti viaggi, questa casa è diventata il mio porto sicuro.
     - collocations: porto sicuro; andare in porto; condurre in porto
     - labels: figurative True
     - grammar: countability Count
  3. [C1] il portare con sé armi, permesso dalla legge solo con una licenza
     - example: Per andare a caccia bisogna avere il porto d’armi.
     - collocations: porto d’armi; porto abusivo di armi
     - labels: domain law
     - grammar: countability Mass
  4. [C1] spesa per il trasporto o la spedizione di una merce
     - example: Il pacco arriverà con porto assegnato, quindi paga il destinatario.
     - collocations: porto assegnato; franco di porto
     - grammar: countability Mass
  5. [B2] vino dolce e forte prodotto nel nord del Portogallo
     - example: Dopo cena il nonno beve sempre un bicchierino di porto.
     - grammar: countability Mass
- relations: derived_from portare (VERB), for sense 3, 4 only

#### Entry B

**Lexeme 1: porto**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈpɔr.to/ pòrto (it-IT)
  1. [A2] tratto di acqua riparato e attrezzato per l’approdo e la sosta delle navi
     - example: Il traghetto entrerà in porto con mezz’ora di ritardo per il maltempo.
     - collocations: porto di mare; porto turistico; porto commerciale; entrare in porto; uscire dal porto; capitaneria di porto
     - grammar: countability Count
  2. [B2] luogo o condizione in cui si trovano rifugio, sicurezza e tranquillità
     - example: Dopo tanti anni difficili, abbiamo trovato finalmente un porto sicuro in campagna.
     - collocations: porto sicuro
     - labels: figurative True
     - grammar: countability Count
  3. [B2] fatto di avere con sé un’arma fuori casa, o il relativo permesso
     - example: Per comprare un fucile da caccia bisogna prima ottenere il porto d’armi.
     - frames: porto di qcs
     - collocations: porto d’armi; porto abusivo
  4. [C1] spedizione e consegna di merci o corrispondenza, oppure il costo del servizio
     - example: Spediremo le scatole in porto assegnato, quindi pagherai tutto direttamente al corriere.
     - collocations: franco di porto; porto assegnato; spese di porto
     - grammar: countability Mass
- relations: derived_from portare (VERB), for sense 3, 4 only
**Lexeme 2: porto**, NOUN
- grammar: gender Masc; countability Mass, mainly; invariable True
- said: /ˈpɔr.to/ pòrto (it-IT)
  1. [B2] vino liquoroso dolce o secco, rosso o bianco, originario del Portogallo
     - example: A fine cena bevo volentieri un piccolo calice di porto con i dolci.

### 5. uovo

#### Entry A

**Lexeme 1: uovo**, NOUN
- grammar: gender Masc; countability Count; irregular True
- said: /ˈwɔ.vo/ uòvo (it-IT)
- forms: uova [Gender=Fem, Number=Plur] /ˈwɔ.va/ uòva
  1. [A1] corpo con guscio deposto da uccelli e altri animali, molto comune anche come alimento
     - example: Per fare la frittata ho sbattuto quattro uova fresche in una ciotola.
     - collocations: uovo sodo; uovo al tegamino; uovo alla coque; uova strapazzate; sbattere le uova; deporre le uova; guscio d'uovo
  2. [A2] dolce cavo di cioccolato di forma ovale che contiene una sorpresa, tipico di Pasqua
     - example: Abbiamo comprato un grande uovo di cioccolato fondente con una sorpresa dentro.
     - collocations: uovo di Pasqua; uovo di cioccolato; aprire l'uovo

#### Entry B

**Lexeme 1: uovo**, NOUN
- grammar: gender Masc; countability Count; irregular True; note plural is feminine: le uova
- said: /ˈwɔ.vo/ uòvo (it-IT)
- forms: uova [Number=Plur, Gender=Fem] /ˈwɔ.va/ uòva
  1. [A1] corpo ovale deposto da uccelli e altri animali, da cui nasce il piccolo
     - example: Per fare questa torta servono tre uova e un po' di zucchero.
     - collocations: uovo sodo; uovo alla coque; uovo al tegamino; uova strapazzate; rompere un uovo; sbattere le uova; deporre le uova; covare le uova; uovo fresco; guscio d'uovo
  2. [A2] dolce od oggetto di forma ovale, spesso di cioccolato, regalato a Pasqua
     - example: Ai bambini piace soprattutto la sorpresa dentro l'uovo di cioccolato.
     - collocations: uovo di Pasqua; uovo di cioccolato; rompere l'uovo
  3. [C1] cellula riproduttiva femminile che, se fecondata, dà origine a un nuovo essere
     - example: Lo spermatozoo raggiunge l'uovo e lo feconda nelle tube.
     - collocations: uovo fecondato; fecondare l'uovo
     - labels: domain biology

### 6. braccio

#### Entry A

**Lexeme 1: braccio**, NOUN
- grammar: gender Masc; countability Count; irregular True
- said: /ˈbrat.tʃo/ bràccio (it-IT)
- forms: braccia [Gender=Fem, Number=Plur] /ˈbrat.tʃa/ bràccia; bracci [Gender=Masc, Number=Plur] /ˈbrat.tʃi/ bràcci
  1. [A1] parte del corpo umano compresa tra la spalla e la mano
     - example: Il bambino si è addormentato in braccio a suo padre.
     - collocations: prendere in braccio; tenere in braccio; incrociare le braccia; a braccia aperte; a braccia conserte; alzare le braccia
  2. [B2] parte allungata o sporgente di un oggetto, un meccanismo o un edificio
     - example: Sulla scrivania ho messo una lampada con il braccio regolabile.
     - collocations: braccio meccanico; braccio di una gru; braccio della bilancia; braccio di un carcere
  3. [B2] tratto stretto e lungo di mare, di lago o di fiume
     - example: Un breve braccio di mare separa la piccola isola dalla terraferma.
     - collocations: braccio di mare; braccio di fiume

#### Entry B

**Lexeme 1: braccio**, NOUN
- grammar: gender Masc; countability Count; note plural le braccia for the body, i bracci for the other senses
- said: /ˈbrat.tʃo/ bràccio (it-IT)
- forms: braccia [Number=Plur, Gender=Fem] bràccia; bracci [Number=Plur, Gender=Masc] bràcci
  1. [A1] ciascuno dei due arti superiori del corpo umano, dalla spalla alla mano
     - example: Il bambino si è addormentato tra le braccia della mamma.
     - collocations: prendere in braccio; tenere in braccio; a braccia aperte; incrociare le braccia; a braccia conserte; allargare le braccia; rompersi un braccio
  2. [B2] parte allungata che si stacca da un corpo centrale o da una struttura
     - example: La nave entrò in un braccio di mare riparato dal vento.
     - collocations: braccio di mare; braccio di fiume; braccio della gru; braccio di un edificio
  3. [B2] persona o gruppo che agisce per conto di altri, eseguendone la volontà
     - example: Da molti anni lei è il braccio destro del direttore.
     - collocations: braccio destro; braccio armato; il braccio della legge
     - labels: figurative True

### 7. problema

#### Entry A

**Lexeme 1: problema**, NOUN
- grammar: gender Masc; countability Count
- said: /pro.ˈblɛ.ma/ problèma (it-IT)
  1. [A1] situazione difficile o ostacolo che richiede di essere affrontato e superato
     - example: Se hai qualche problema con il computer, chiedi pure al tecnico.
     - frames: problema con qcn/qcs; problema di qcs
     - collocations: nessun problema; risolvere un problema; affrontare un problema; creare problemi; dare problemi; porsi un problema; problema grave; problemi di salute
  2. [A2] quesito o esercizio da risolvere con il calcolo o con il ragionamento
     - example: Per domani dobbiamo svolgere sul quaderno tre problemi di geometria.
     - frames: problema di qcs
     - collocations: problema di matematica; problema di geometria; svolgere un problema; impostare un problema

#### Entry B

**Lexeme 1: problema**, NOUN
- grammar: gender Masc; countability Count
- said: /pro.ˈblɛ.ma/ problèma (it-IT)
- forms: problemi [Number=Plur] problèmi
  1. [A1] situazione difficile o questione da chiarire che bisogna affrontare e risolvere
     - example: Se hai qualche problema con il computer, chiamami pure.
     - frames: problema di qcs; problema con qcn/qcs; il problema è che + ind
     - collocations: risolvere un problema; affrontare un problema; porre un problema; creare problemi; avere problemi; nessun problema; problema serio; problemi di salute
  2. [A2] esercizio in cui si deve trovare un risultato partendo da dati conosciuti
     - example: Per domani dobbiamo fare tre problemi di geometria.
     - collocations: risolvere un problema; problema di matematica; problema di geometria
  3. [B2] persona o cosa che dà continue preoccupazioni o fastidi
     - example: Quel cane è diventato un vero problema per tutto il condominio.
     - labels: formality informal, figurative True

### 8. piacere

#### Entry A

**Lexeme 1: piacere**, VERB
- grammar: conjugation 2; irregular True; valency Intr; auxiliary be; note the person who likes is the indirect object: mi piace, ti piacciono
- said: /pja.ˈtʃe.re/ piacére (it-IT)
- forms: piaccio [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] piàccio; piaci [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] piàci; piace [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] piàce; piacciamo [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] piacciàmo; piacciono [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] piàcciono; piacqui [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] piàcqui; piacque [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] piàcque; piacerò [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] piacerò; piaccia [Mood=Sub, Tense=Pres, Person=1,2,3, Number=Sing, VerbForm=Fin] piàccia; piaciuto [Tense=Past, VerbForm=Part] piaciùto
  1. [A1] risultare gradito, dare una sensazione gradevole a qualcuno
     - example: Mi piace molto la cucina di mia nonna.
     - frames: qcs piace a qcn; a qcn piace + inf
     - collocations: piacere molto; piacere da morire; piacere un sacco; piacere a tutti
  2. [A2] suscitare in qualcuno attrazione fisica o interesse amoroso
     - example: Credo proprio di piacere a quel ragazzo della mia classe.
     - frames: qcn piace a qcn
**Lexeme 2: piacere**, NOUN
- grammar: gender Masc; countability Both
- said: /pja.ˈtʃe.re/ piacére (it-IT)
  1. [A2] sensazione di gioia e soddisfazione, o ciò che la procura
     - example: Leggere un buon libro la sera è per me un grande piacere.
     - frames: fare piacere a qcn
     - collocations: con piacere; provare piacere; i piaceri della vita; a piacere
     - grammar: countability Mass, mainly
     - opposites: dispiacere
  2. [A2] aiuto o cortesia che si fa a qualcuno
     - example: Mi fai un piacere e chiudi la finestra della cucina?
     - collocations: fare un piacere; chiedere un piacere; per piacere
     - grammar: countability Count
- relations: derived_from piacere (VERB)
**Lexeme 3: piacere**, INTJ
- said: /pja.ˈtʃe.re/ piacére (it-IT)
  1. [A1] usato per salutare quando si viene presentati a qualcuno
     - example: Piacere, sono la nuova vicina del terzo piano.
     - collocations: piacere mio; molto piacere; tanto piacere
- relations: derived_from piacere (NOUN)

#### Entry B

**Lexeme 1: piacere**, VERB
- grammar: conjugation 2; irregular True; auxiliary be
- said: /pja.ˈtʃe.re/ piacére (it-IT)
- forms: piaccio [Mood=Ind, Number=Sing, Person=1, Tense=Pres, VerbForm=Fin] piàccio; piaci [Mood=Ind, Number=Sing, Person=2, Tense=Pres, VerbForm=Fin] piàci; piace [Mood=Ind, Number=Sing, Person=3, Tense=Pres, VerbForm=Fin] piàce; piacciamo [Mood=Ind,Sub, Number=Plur, Person=1, Tense=Pres, VerbForm=Fin] piacciàmo; piacciono [Mood=Ind, Number=Plur, Person=3, Tense=Pres, VerbForm=Fin] piàcciono; piacqui [Mood=Ind, Number=Sing, Person=1, Tense=Past, VerbForm=Fin] piàcqui; piacque [Mood=Ind, Number=Sing, Person=3, Tense=Past, VerbForm=Fin] piàcque; piacquero [Mood=Ind, Number=Plur, Person=3, Tense=Past, VerbForm=Fin] piàcquero; piacerò [Mood=Ind, Number=Sing, Person=1, Tense=Fut, VerbForm=Fin] piacerò; piaccia [Mood=Sub, Number=Sing, Person=1,2,3, Tense=Pres, VerbForm=Fin] piàccia; piacciano [Mood=Sub, Number=Plur, Person=3, Tense=Pres, VerbForm=Fin] piàcciano; piaciuto [Gender=Masc, Number=Sing, Tense=Past, VerbForm=Part] piaciùto
  1. [A1] risultare gradito a qualcuno, suscitando interesse, soddisfazione o simpatia
     - example: A mio fratello piace molto cucinare per gli amici il sabato sera.
     - frames: qcn/qcs piace a qcn; a qcn piace + inf; a qcn piace che + cong
     - collocations: piacere da matti; piacere da morire; farsi piacere
     - grammar: valency Intr; auxiliary be
     - opposites: spiacere
  2. [B1] provare attrazione o simpatia l’uno verso l’altro
     - example: Si sono piaciuti subito fin dal loro primo incontro in biblioteca.
     - grammar: valency Refl; reflexive True; auxiliary be; note reciprocal: each other
**Lexeme 2: piacere**, NOUN
- grammar: gender Masc; countability Both
- said: /pja.ˈtʃe.re/ piacére (it-IT)
  1. [A2] sensazione viva di benessere, gioia o soddisfazione fisica o mentale
     - example: Ho riletto con grande piacere le vecchie lettere dei miei nonni.
     - collocations: con piacere; fare piacere; provare piacere; viaggio di piacere; i piaceri della tavola
     - grammar: countability Both
     - opposites: dispiacere
  2. [A2] favore o atto gentile che si chiede o si fa a qualcuno
     - example: Mi faresti il piacere di chiudere la finestra prima di uscire?
     - frames: il piacere di + inf
     - collocations: per piacere; fare un piacere; chiedere un piacere
     - grammar: countability Count
  3. [B1] libera scelta o preferenza personale nel fare qualcosa
     - example: Potete aggiungere spezie e aromi a piacere sulla zuppa calda.
     - collocations: a piacere; a proprio piacere
     - grammar: countability Mass
- relations: derived_from piacere (VERB)
**Lexeme 3: piacere**, INTJ
- said: /pja.ˈtʃe.re/ piacére (it-IT)
  1. [A1] usato come saluto cortese quando ci si presenta a una persona
     - example: Piacere, sono il nuovo insegnante di storia di questa scuola.
     - collocations: molto piacere; piacere mio
- relations: derived_from piacere (NOUN)

### 9. bisognare

#### Entry A

**Lexeme 1: bisognare**, VERB
- grammar: conjugation 1; irregular False; auxiliary be; note defective: used only in the third person; compound tenses are rare
- said: /bi.zoɲ.ˈɲa.re/ bisognàre (it-IT)
  1. [A2] essere necessario o opportuno fare qualcosa
     - example: Per arrivare in tempo bisogna partire prima delle sette.
     - frames: bisogna + inf; bisogna che + cong
     - collocations: bisogna dire che; bisogna ammettere che; bisogna vedere; non bisogna
     - grammar: valency Impers
  2. [C1] essere necessario a qualcuno, servire
     - example: Per finire il lavoro ci bisognano almeno due settimane.
     - frames: qcs bisogna a qcn
     - labels: style literary
     - grammar: valency Intr; note used in the third person singular and plural, agreeing with what is needed
- relations: derived_from bisogno (NOUN)

#### Entry B

**Lexeme 1: bisognare**, VERB
- grammar: conjugation 1; irregular False; auxiliary be; note rarely used in compound tenses
- said: /bi.zoɲ.ˈɲa.re/ bisognàre (it-IT)
  1. [A1] essere necessario o opportuno che si faccia qualcosa o che qualcosa avvenga
     - example: Per arrivare puntuali alla stazione bisogna uscire di casa subito.
     - frames: bisogna + inf; bisogna che + cong
     - grammar: valency Impers; note used only in the third person singular
  2. [C1] essere necessario, utile o indispensabile a qualcuno
     - example: Prendi pure dalla dispensa tutto ciò che ti bisogna per il viaggio.
     - frames: qcs bisogna a qcn
     - labels: time dated, style literary
     - grammar: valency Intr
- relations: derived_from bisogno (NOUN)

### 10. figo

#### Entry A

**Lexeme 1: figo**, ADJ
- grammar: comparable True
- said: /ˈfi.ɡo/ fìgo (it-IT)
  1. [B2] molto bello, divertente, originale o capace di suscitare entusiasmo
     - example: Abbiamo scoperto un locale davvero figo vicino alla stazione.
     - collocations: fare figo
     - labels: formality informal
  2. [B2] fisicamente molto attraente, curato nell’aspetto o pieno di fascino
     - example: Tutti dicono che il nuovo istruttore di nuoto è molto figo.
     - labels: formality informal
**Lexeme 2: figo**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈfi.ɡo/ fìgo (it-IT)
  1. [B2] uomo o ragazzo molto attraente, ammirato o sicuro di sé
     - example: Si crede un gran figo solo perché guida un’auto sportiva.
     - collocations: fare il figo; credersi un figo; un gran figo
     - labels: formality informal
- relations: derived_from figo (ADJ)

#### Entry B

**Lexeme 1: figo**, ADJ
- grammar: comparable True
- said: /ˈfi.ɡo/ fìgo (it-IT, figo), /ˈfi.ko/ fìco (it-IT, fico)
- spellings: figo (it-IT); fico (it-IT)
  1. [B1] di aspetto molto bello e attraente
     - example: Il nuovo collega di mia sorella è proprio figo.
     - labels: formality informal
  2. [B1] che piace molto perché bello, originale o di moda
     - example: Ieri sera abbiamo visto un film davvero figo al cinema.
     - collocations: che figo
     - labels: formality informal
**Lexeme 2: figo**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈfi.ɡo/ fìgo (it-IT, figo), /ˈfi.ko/ fìco (it-IT, fico)
- spellings: figo (it-IT); fico (it-IT)
  1. [B2] ragazzo o uomo molto bello e attraente
     - example: Si crede un gran figo, ma nessuno lo sopporta.
     - collocations: fare il figo; un gran figo
     - labels: formality informal
- relations: derived_from figo (ADJ)

### 11. casino

#### Entry A

**Lexeme 1: casino**, NOUN
- grammar: gender Masc; countability Count
- said: /ka.ˈzi.no/ casìno (it-IT)
  1. [B1] situazione di grande disordine, confusione e rumore
     - example: Stasera i vicini stanno facendo un casino incredibile.
     - collocations: fare casino; che casino; un casino infernale
     - labels: formality informal
  2. [B2] situazione difficile e complicata, o errore che la provoca
     - example: Se il capo scopre l’errore, siamo davvero nei casini.
     - collocations: essere nei casini; mettersi nei casini; combinare un casino; un bel casino
     - labels: formality informal
  3. [B2] quantità molto grande di persone o di cose
     - example: Al concerto c’era un casino di gente davanti al palco.
     - frames: un casino di qcs
     - collocations: un casino di gente; un casino di soldi; piacere un casino
     - labels: formality informal
     - grammar: countability Count; note only in the singular with un; un casino also used as an adverb: very much
  4. [C1] casa dove si pratica la prostituzione
     - example: Nel romanzo il protagonista passa le notti in un casino di periferia.
     - labels: formality informal, time dated
  5. [C1] piccola villa di campagna usata per la caccia o lo svago
     - example: Il principe fece costruire un casino di caccia in mezzo al bosco.
     - collocations: casino di caccia
- relations: derived_from casa (NOUN), for sense 5 only; confusable casinò (NOUN): casinò, stressed on the last syllable, is a gambling house

#### Entry B

**Lexeme 1: casino**, NOUN
- grammar: gender Masc; countability Both
- said: /ka.ˈzi.no/ casìno (it-IT)
  1. [B1] grande confusione, fracasso, disordine o situazione molto difficile da gestire
     - example: Con i lavori stradali stamattina c’era un gran casino in centro.
     - collocations: fare casino; combinare un casino; piantare un casino
     - labels: formality informal, figurative True
     - grammar: countability Both
  2. [B2] grandissima quantità di cose o numero molto alto di persone
     - example: Abbiamo speso un casino di soldi per riparare il tetto.
     - frames: un casino di qcn/qcs
     - labels: formality informal, figurative True
     - grammar: countability Count; number Sing
  3. [C1] locale in cui un tempo si esercitava legalmente la prostituzione
     - example: Una legge degli anni cinquanta ordinò la chiusura di tutti i casini.
     - labels: time historical
     - grammar: countability Count
  4. [C2] piccola villa in campagna destinata al riposo, agli incontri o alla caccia
     - example: Nel parco della tenuta sorge ancora un antico casino di caccia.
     - collocations: casino di caccia
     - labels: time dated
     - grammar: countability Count
- relations: derived_from casa (NOUN); confusable casinò (NOUN): casinò with a final accent means gambling house
