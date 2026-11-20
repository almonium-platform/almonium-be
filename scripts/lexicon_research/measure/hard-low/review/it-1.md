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
- grammar: invariable True
- said: /an.ˈko.ra/ ancóra (it-IT)
- spellings: ancora (it-IT); ancor (it-IT)
  1. [A1] fino a questo momento o al momento di cui si parla, come prima
     - example: Sono le dieci e i bambini dormono ancora nelle loro camere.
     - collocations: ancora una volta; non ancora; ancora oggi; ancora in vita
  2. [A1] un’altra volta, di nuovo o in quantità maggiore rispetto a prima
     - example: Se hai sete, versati pure ancora un po’ di acqua fresca.
     - frames: ancora + agg
     - collocations: ancora di più; ancora meglio; ancora peggio; ancora un po’
  3. [A2] anche in un tempo futuro, più avanti rispetto al momento attuale
     - example: Restiamo in città ancora per qualche giorno prima di tornare a casa.
     - collocations: ancora a lungo; per molto tempo ancora
  4. [B2] perfino, anche, per aggiungere un altro elemento a quanto già detto
     - example: Oltre al pane e alla frutta, dobbiamo comprare ancora il latte.
     - collocations: e ancora
- relations: confusable ancora (NOUN): same spelling, stressed on the first syllable (/ˈan.ko.ra/) for the ship's anchor
**Lexeme 2: ancora**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈan.ko.ra/ àncora (it-IT)
  1. [B1] pesante attrezzo di metallo calato sul fondo per tenere ferma un’imbarcazione
     - example: Il capitano ha ordinato ai marinai di gettare l’ancora vicino alla costa.
     - collocations: gettare l’ancora; levare l’ancora; salpare l’ancora; stare all’ancora
  2. [B2] persona o cosa che offre sicurezza e sostegno nei momenti difficili
     - example: Nei mesi più duri il lavoro è stato la mia unica ancora.
     - collocations: ancora di salvezza
     - labels: figurative True
- relations: confusable ancora (ADV): same spelling, stressed on the second syllable (/an.ˈko.ra/) for still or again

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
- grammar: gender Fem; countability Both
- said: /ˈpes.ka/ pésca (it-IT)
  1. [A2] attività di catturare pesci o altri animali acquatici in mare, fiumi o laghi
     - example: Durante le vacanze al mare mio zio si sveglia presto per andare a pesca.
     - collocations: andare a pesca; canna da pesca; barca da pesca; licenza di pesca; pesca sportiva; pesca subacquea
     - grammar: countability Mass
  2. [B2] insieme dei pesci e degli altri animali acquatici catturati in una volta
     - example: I marinai sono tornati in porto all’alba con una pesca molto abbondante.
     - collocations: buona pesca; pesca abbondante; pesca miracolosa
     - grammar: countability Count; number Sing, mainly
  3. [B2] gioco in cui si estrae a sorte un biglietto per vincere un premio
     - example: Alla festa del paese abbiamo organizzato una pesca di beneficenza per la scuola.
     - collocations: pesca di beneficenza
     - grammar: countability Count
- relations: verbal_noun_of pescare (VERB), for sense 1 only; derived_from pescare (VERB), for sense 2, 3 only
**Lexeme 2: pesca**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈpɛs.ka/ pèsca (it-IT)
  1. [A2] frutto estivo rotondo e dolce, con buccia sottile e un grosso nocciolo
     - example: A fine pasto ho mangiato una pesca matura appena colta dall’albero.
     - collocations: pesca noce; pesca gialla; pesca bianca; pesche sciroppate; tè alla pesca; succo di pesca
  2. [C1] livido scuro o gonfiore sotto l’occhio causato da un colpo
     - example: È scivolato dalle scale e si è ritrovato con una brutta pesca sotto l’occhio.
     - labels: formality informal, figurative True

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
- spellings: stato (it-IT); Stato (it-IT)
  1. [A2] modo di essere o condizione in cui si trova qualcuno o qualcosa
     - example: La vecchia casa dei nonni è ancora in buono stato.
     - frames: in stato di qcs; in uno stato + agg
     - collocations: in buono stato; in cattivo stato; stato d'animo; stato di salute; stato civile; stato interessante
  2. [A2] comunità politica sovrana su un territorio con leggi e istituzioni proprie
     - example: Il nuovo accordo è stato firmato da venti stati diversi.
     - collocations: capo di Stato; colpo di Stato; segreto di Stato; esame di Stato; stato membro; stato di diritto
     - grammar: note often capitalized when referring to a state's institutions or government
  3. [B1] ciascuna delle forme fisiche fondamentali che la materia può assumere
     - example: A zero gradi l'acqua passa dallo stato liquido a quello solido.
     - collocations: stato liquido; stato solido; stato gassoso; passaggio di stato

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
- grammar: gender Masc
- said: /ˈpɔr.to/ pòrto (it-IT)
  1. [A1] luogo sulla costa o su un fiume dove le navi sostano al sicuro
     - example: La nave entrerà in porto domani mattina alle sette.
     - collocations: entrare in porto; uscire dal porto; porto turistico; porto commerciale; capitaneria di porto; andare in porto
     - grammar: countability Count
  2. [B2] luogo sicuro dove trovare pace, riparo o conforto dopo molte difficoltà
     - example: Dopo tanti anni di viaggi ha trovato nella lettura un porto tranquillo.
     - collocations: un porto sicuro
     - labels: figurative True
     - grammar: countability Count
  3. [B2] vino dolce e forte prodotto nel nord del Portogallo
     - example: A fine cena ci hanno servito due bicchierini di porto invecchiato.
     - collocations: un bicchiere di porto
     - grammar: countability Mass, mainly
**Lexeme 2: porto**, NOUN
- grammar: gender Masc; countability Mass
- said: /ˈpɔr.to/ pòrto (it-IT)
  1. [B2] facoltà concessa dalla legge di tenere con sé un’arma fuori casa
     - example: Per praticare la caccia è obbligatorio avere il porto d’armi valido.
     - collocations: porto d’armi; porto abusivo d’armi
  2. [C1] trasporto e consegna di merci o corrispondenza, o il relativo prezzo
     - example: La merce viaggia in porto franco fino al magazzino del cliente.
     - collocations: porto franco; porto assegnato
     - labels: domain economics
- relations: derived_from portare (VERB)

### 5. uovo

#### Entry A

**Lexeme 1: uovo**, NOUN
- grammar: gender Masc; countability Count; irregular True
- said: /ˈwɔ.vo/ uòvo (it-IT)
- forms: uova [Gender=Fem, Number=Plur] /ˈwɔ.va/ uòva
  1. [A1] corpo tondeggiante col guscio deposto dalle femmine di uccelli e altri animali, usato anche come cibo
     - example: Per preparare questa torta servono tre uova fresche e un po' di latte.
     - collocations: uovo sodo; uovo alla coque; uovo al tegamino; uova strapazzate; sbattere le uova; depongono le uova; uovo di Pasqua; cercare il pelo nell'uovo
  2. [B2] cellula femminile da cui nasce un nuovo individuo dopo la fecondazione
     - example: Negli esseri umani la fecondazione dell'uovo avviene all'interno del corpo.
     - labels: domain biology

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
- forms: braccia [Number=Plur, Gender=Fem] /ˈbrat.tʃa/ bràccia; bracci [Number=Plur, Gender=Masc] /ˈbrat.tʃi/ bràcci
  1. [A1] parte del corpo umano che va dalla spalla fino alla mano
     - example: Sono caduto dalla bicicletta e mi sono rotto il braccio sinistro.
     - collocations: a braccia aperte; incrociare le braccia; prendere in braccio; portare in braccio; sotto braccio; braccio destro; dare una mano o un braccio
  2. [B2] parte sporgente e allungata di un oggetto, di una costruzione o del mare
     - example: Il nuovo edificio è diviso in tre lunghi bracci indipendenti.
     - collocations: braccio della gru; braccio di mare; braccio della bilancia; braccio di un carcere

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
- grammar: gender Masc; countability Count; irregular True
- said: /pro.ˈblɛ.ma/ problèma (it-IT)
- forms: problemi [Number=Plur] /pro.ˈblɛ.mi/ problèmi
  1. [A1] situazione difficile o spiacevole che richiede un intervento per essere superata
     - example: Dobbiamo parlare insieme per risolvere questo problema il prima possibile.
     - frames: problema di qcs; problema con qcn/qcs
     - collocations: risolvere un problema; affrontare un problema; creare problemi; porre un problema; senza problemi; problema grave; non c'è problema
  2. [B1] questione teorica o pratica che chiede una risposta o una spiegazione
     - example: Il libro discute il problema dell'origine della vita sulla Terra.
     - frames: il problema di qcs
     - collocations: esaminare un problema; sollevare un problema; problema aperto
  3. [A2] esercizio scolastico o scientifico in cui si cercano risultati partendo da dati noti
     - example: Per domani l'insegnante ci ha assegnato tre problemi di geometria.
     - frames: problema di qcs
     - collocations: risolvere un problema; impostare un problema; problema di matematica

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
- grammar: conjugation 2; irregular True; valency Intr; auxiliary be
- said: /pja.ˈtʃe.re/ piacére (it-IT)
- forms: piaccio [Mood=Ind, Number=Sing, Person=1, Tense=Pres, VerbForm=Fin] /ˈpjat.tʃo/ piàccio; piaci [Mood=Ind, Number=Sing, Person=2, Tense=Pres, VerbForm=Fin] /ˈpja.tʃi/ piàci; piace [Mood=Ind, Number=Sing, Person=3, Tense=Pres, VerbForm=Fin] /ˈpja.tʃe/ piàce; piacciamo [Mood=Ind, Number=Plur, Person=1, Tense=Pres, VerbForm=Fin] /pjat.ˈtʃa.mo/ piacciàmo; piacciono [Mood=Ind, Number=Plur, Person=3, Tense=Pres, VerbForm=Fin] /ˈpjat.tʃo.no/ piàcciono; piacqui [Mood=Ind, Number=Sing, Person=1, Tense=Past, VerbForm=Fin] /ˈpjak.kwi/ piàcqui; piacque [Mood=Ind, Number=Sing, Person=3, Tense=Past, VerbForm=Fin] /ˈpjak.kwe/ piàcque; piacerò [Mood=Ind, Number=Sing, Person=1, Tense=Fut, VerbForm=Fin] /pja.tʃe.ˈrɔ/ piacerò; piaccia [Mood=Sub, Number=Sing, Person=1, Tense=Pres, VerbForm=Fin] /ˈpjat.tʃa/ piàccia; piaciuto [Tense=Past, VerbForm=Part] /pja.ˈtʃu.to/ piaciùto
  1. [A1] essere gradito, suscitare approvazione o corrispondere ai gusti di qualcuno
     - example: Mi piace molto passeggiare nel parco la mattina presto.
     - frames: qcs piace a qcn; a qcn piace + inf; a qcn piace che + cong
     - collocations: piacere da morire; piacere da impazzire; piacere al pubblico; farsi piacere
  2. [A2] suscitare interesse amoroso o attrazione fisica in una persona
     - example: Credo che il nuovo vicino di casa le piaccia parecchio.
     - frames: qcn piace a qcn
**Lexeme 2: piacere**, NOUN
- grammar: gender Masc; countability Both
- said: /pja.ˈtʃe.re/ piacére (it-IT)
  1. [A2] sensazione di gioia, benessere o soddisfazione, o ciò che la procura
     - example: Accetto con grande piacere il vostro invito per la cena di sabato.
     - frames: il piacere di qcs; il piacere di + inf; è un piacere + inf
     - collocations: con piacere; fare piacere; provare piacere; per puro piacere; viaggio di piacere; i piaceri della tavola
     - grammar: countability Both
     - opposites: dispiacere
  2. [A2] favore, servizio o cortesia che si chiede o si fa a qualcuno
     - example: Ti chiedo il piacere di accompagnare mio nonno fino alla stazione.
     - frames: il piacere di + inf
     - collocations: per piacere; fare un piacere; chiedere un piacere; ricambiare un piacere
     - grammar: countability Count
- relations: derived_from piacere (VERB)
**Lexeme 3: piacere**, INTJ
- said: /pja.ˈtʃe.re/ piacére (it-IT)
  1. [A1] usato come formula di cortesia quando ci si presenta a qualcuno
     - example: Buongiorno, piacere, sono il nuovo insegnante di storia.
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
- grammar: conjugation 1; irregular False; auxiliary be; note defective; used only in the 3rd person singular and rarely in compound tenses
- said: /bi.zoɲ.ˈɲa.re/ bisognàre (it-IT)
  1. [A2] essere necessario o opportuno fare qualcosa, o che qualcosa avvenga
     - example: Per arrivare in orario alla stazione bisogna partire subito.
     - frames: bisogna + inf; bisogna che + cong
     - grammar: valency Impers
  2. [C1] essere utile o indispensabile a qualcuno per uno scopo
     - example: Ditemi pure tutto quello che vi bisogna per il viaggio.
     - frames: bisognare a qcn
     - labels: time dated, style literary
     - grammar: valency Intr; note used in the 3rd person singular and plural
- relations: derived_from bisogno (NOUN)

### 10. figo

#### Entry A

**Lexeme 1: figo**, ADJ
- grammar: comparable True
- said: /ˈfi.ɡo/ fìgo (it-IT, figo), /ˈfi.ko/ fìco (it-IT, fico)
- spellings: figo (it-IT); fico (it-IT)
  1. [B1] molto bello, divertente o interessante, tale da piacere subito
     - example: Ieri sera abbiamo visto un film davvero figo al cinema.
     - labels: formality informal
  2. [B2] di bell’aspetto, curato e fisicamente attraente
     - example: Con quella giacca nuova sembri ancora più figo del solito.
     - labels: formality informal
**Lexeme 2: figo**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈfi.ɡo/ fìgo (it-IT, figo), /ˈfi.ko/ fìco (it-IT, fico)
- spellings: figo (it-IT); fico (it-IT)
  1. [B2] ragazzo o uomo molto bello e fisicamente attraente
     - example: Il nuovo istruttore della palestra è proprio un bel figo.
     - collocations: fare il figo
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
- said: /ka.ˈzi.no/ casìno (it-IT), /ka.ˈsi.no/ casìno (it-IT, regional)
  1. [B1] grande confusione, disordine, rumore fastidioso o situazione complicata e difficile
     - example: In camera tua c’è un casino che non si riesce a passare.
     - collocations: fare casino; piantare un casino; combinare un casino; mettersi nei casini; un casino di
     - labels: formality informal
     - grammar: countability Both
  2. [C1] locale in cui si esercita la prostituzione in modo organizzato
     - example: In Italia i casini furono chiusi per legge nel dopoguerra.
     - labels: formality informal, time historical
     - grammar: countability Count
  3. [C2] piccola costruzione elegante di campagna usata per il riposo o la caccia
     - example: Nel parco della villa sorge un antico casino di caccia.
     - collocations: casino di caccia
     - grammar: countability Count
- relations: derived_from casa (NOUN); confusable casinò (NOUN): casinò, stressed on the last syllable, means gambling house
