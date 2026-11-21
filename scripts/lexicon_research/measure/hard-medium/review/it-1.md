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
  1. [A1] fino a questo momento o al momento di cui si parla, come prima
     - example: A quest’ora la biblioteca è ancora aperta al pubblico.
     - collocations: non ancora; ancora oggi; ancora adesso; ancora in tempo; ancora per poco
  2. [A1] una seconda volta o un’altra volta dopo quelle precedenti
     - example: Se non hai capito la regola, te la spiego ancora.
     - collocations: ancora una volta
  3. [A2] in aggiunta a quanto c’è già, oppure in misura maggiore
     - example: Restiamo ancora dieci minuti al parco e poi torniamo a casa.
     - collocations: ancora un po’; ancora meglio; ancora peggio
**Lexeme 2: ancora**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈan.ko.ra/ àncora (it-IT)
  1. [B2] pesante oggetto di metallo calato in acqua per tenere ferma un’imbarcazione
     - example: Al tramonto i marinai hanno gettato l’ancora vicino alla costa.
     - collocations: gettare l’ancora; levare l’ancora; salpare l’ancora; all’ancora
  2. [B2] persona o cosa che offre sicurezza, sostegno o aiuto in una situazione difficile
     - example: Quando tutto sembra perduto, un amico sincero diventa un’ancora di salvezza.
     - collocations: ancora di salvezza
     - labels: figurative True
- relations: confusable ancora (ADV): written the same as the adverb ancora, which is stressed on the second syllable

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
  1. [A2] frutto rotondo dalla polpa dolce e succosa, con buccia sottile e un nocciolo duro
     - example: Per merenda taglio una pesca matura e la mangio con lo yogurt.
     - collocations: pesca noce; tè alla pesca; color pesca; succo di pesca; pesca sciroppata; pesca gialla
- relations: confusable pesco (NOUN): the peach tree, whereas pesca is the fruit; confusable pesce (NOUN): means fish, not peach
**Lexeme 2: pesca**, NOUN
- grammar: gender Fem; countability Both
- said: /ˈpes.ka/ pésca (it-IT)
  1. [A2] cattura di animali che vivono in acqua, praticata per lavoro, cibo o sport
     - example: Domenica mattina andremo a pesca lungo il fiume con nostro nonno.
     - collocations: andare a pesca; canna da pesca; barca da pesca; rete da pesca; pesca sportiva; pesca subacquea; pesca a strascico; licenza di pesca
     - grammar: countability Mass
  2. [B1] insieme o quantità degli animali acquatici che sono stati catturati
     - example: Al ritorno in porto i marinai vendono tutta la pesca al mercato.
     - collocations: fare una buona pesca; pesca abbondante; pesca miracolosa
     - grammar: countability Count
  3. [B2] gioco a premi in cui si estraggono a sorte biglietti numerati o oggetti
     - example: Alla festa della scuola ho vinto una sciarpa alla pesca di beneficenza.
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
  1. [A2] condizione o modo in cui si trova o si presenta qualcuno o qualcosa
     - example: Domani parleremo con il medico dello stato di salute di mio nonno.
     - frames: stato di qcn/qcs; in stato di qcs
     - collocations: stato d’animo; stato interessante; in ottimo stato; stato di salute; stato di emergenza; allo stato puro; allo stato liquido
  2. [A2] comunità politica con un territorio e un governo, o le sue istituzioni pubbliche
     - example: Ogni stato stabilisce le proprie regole per la sicurezza delle strade.
     - collocations: stato di diritto; stato sociale; esame di stato; colpo di stato; capo di stato; segreto di stato; stato membro
     - grammar: note often capitalized when referring to public institutions
  3. [B2] posizione legale o sociale di una persona nella famiglia o nella comunità
     - example: Nel modulo ho dovuto indicare anche il mio stato civile.
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
  1. [A2] luogo riparato e attrezzato lungo una riva dove approdano e sostano le imbarcazioni
     - example: Dal balcone vediamo i traghetti che entrano in porto ogni mattina.
     - collocations: entrare in porto; attraccare in porto; lasciare il porto; porto turistico; porto commerciale; porto fluviale
     - grammar: countability Count
  2. [B2] luogo o condizione in cui si trovano rifugio, sicurezza e tranquillità
     - example: Dopo tanti anni difficili, hai finalmente trovato un porto sicuro tra gli amici.
     - collocations: porto sicuro; porto di pace
     - labels: figurative True
     - grammar: countability Count
  3. [B2] il circolare con armi fuori casa, o la licenza ufficiale che lo consente
     - example: Per comprare un fucile da caccia bisogna prima richiedere il porto d’armi.
     - collocations: porto d’armi; porto abusivo d’armi; revocare il porto d’armi
     - grammar: countability Both
  4. [C1] spedizione e consegna di merci, o il costo richiesto per questo servizio
     - example: Se ordini almeno tre libri, la libreria spedisce il pacco franco di porto.
     - collocations: franco di porto; porto assegnato; porto pagato; spese di porto
     - grammar: countability Mass
- relations: derived_from portare (VERB), for sense 3, 4 only
**Lexeme 2: porto**, NOUN
- grammar: gender Masc; countability Mass; invariable True
- said: /ˈpɔr.to/ pòrto (it-IT)
  1. [B2] vino rosso o bianco ad alta gradazione alcolica originario del Portogallo
     - example: A fine cena hanno servito dei biscotti secchi con un bicchiere di porto.
     - collocations: bicchiere di porto; porto rosso; porto bianco

### 5. uovo

#### Entry A

**Lexeme 1: uovo**, NOUN
- grammar: gender Masc; countability Count; irregular True
- said: /ˈwɔ.vo/ uòvo (it-IT)
- forms: uova [Gender=Fem, Number=Plur] /ˈwɔ.va/ uòva
  1. [A1] corpo con guscio o membrana deposto da molti animali, usato anche come alimento
     - example: A colazione mangio volentieri un uovo sodo con una fetta di pane.
     - collocations: uovo sodo; uovo al tegamino; uovo in camicia; uova strapazzate; deporre le uova; sbattere le uova; pasta all’uovo
  2. [B1] oggetto o dolce dalla forma tondeggiante, più stretta a una delle estremità
     - example: Dentro quel grande uovo di cioccolato abbiamo trovato una bella sorpresa.
     - collocations: uovo di Pasqua; uovo di cioccolato

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
  1. [A1] ciascuno dei due arti superiori del corpo umano, dalla spalla alla mano
     - example: Dopo la caduta in giardino non riuscivo più a muovere il braccio sinistro.
     - collocations: prendere in braccio; tenere in braccio; a braccia aperte; incrociare le braccia; stringere tra le braccia; dare il braccio
  2. [B2] parte lunga e sporgente di un oggetto, uno strumento o una macchina
     - example: Puoi spostare il braccio della lampada per illuminare meglio la scrivania.
     - collocations: braccio meccanico; braccio articolato; braccio di una gru; bracci di una bilancia
  3. [B2] tratto stretto e allungato di mare o fiume, oppure ala di un edificio
     - example: Un breve braccio di mare separa la piccola isola dalla costa.
     - collocations: braccio di mare; braccio di un fiume; braccio di un carcere; braccio della morte

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
  1. [A1] difficoltà, inconveniente o questione complessa che bisogna affrontare o risolvere
     - example: Se hai un problema con il biglietto, rivolgiti subito allo sportello.
     - frames: problema con qcn/qcs; problema di qcs
     - collocations: senza problemi; sollevare un problema; porre un problema; creare problemi; affrontare un problema; risolvere un problema; un problema grave
  2. [A2] esercizio in cui si cerca la soluzione con il calcolo o il ragionamento
     - example: Mio fratello sta risolvendo tre problemi di geometria sul quaderno.
     - collocations: svolgere un problema; impostare un problema; risolvere un problema; i dati del problema

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
- forms: piaccio [Mood=Ind, Number=Sing, Person=1, Tense=Pres, VerbForm=Fin] /ˈpjat.tʃo/ piàccio; piaci [Mood=Ind, Number=Sing, Person=2, Tense=Pres, VerbForm=Fin] /ˈpja.tʃi/ piàci; piace [Mood=Ind, Number=Sing, Person=3, Tense=Pres, VerbForm=Fin] /ˈpja.tʃe/ piàce; piacciamo [Mood=Ind, Number=Plur, Person=1, Tense=Pres, VerbForm=Fin] /pjat.ˈtʃa.mo/ piacciàmo; piacciono [Mood=Ind, Number=Plur, Person=3, Tense=Pres, VerbForm=Fin] /ˈpjat.tʃo.no/ piàcciono; piacqui [Mood=Ind, Number=Sing, Person=1, Tense=Past, VerbForm=Fin] /ˈpjak.kwi/ piàcqui; piacque [Mood=Ind, Number=Sing, Person=3, Tense=Past, VerbForm=Fin] /ˈpjak.kwe/ piàcque; piacerò [Mood=Ind, Number=Sing, Person=1, Tense=Fut, VerbForm=Fin] /pja.tʃe.ˈrɔ/ piacerò; piaccia [Mood=Sub, Number=Sing, Person=1, Tense=Pres, VerbForm=Fin] /ˈpjat.tʃa/ piàccia; piaciuto [Tense=Past, VerbForm=Part] /pja.ˈtʃu.to/ piaciùto
  1. [A1] essere gradito ai sensi o alla mente, o corrispondere ai gusti di qualcuno
     - example: A mio fratello piace camminare nei boschi quando cade la neve.
     - frames: piacere a qcn; a qcn piace + inf; a qcn piace che + cong
     - collocations: piacere da matti; piacere da morire; fare come pare e piace; piacere a prima vista; piacere al pubblico
     - grammar: valency Intr
  2. [A2] suscitare simpatia, affetto o attrazione in una persona
     - example: Credo che a mia sorella piaccia davvero il nuovo vicino di casa.
     - frames: piacere a qcn
     - collocations: piacere al primo sguardo
     - grammar: valency Intr
  3. [B1] provare simpatia, interesse o attrazione in modo reciproco
     - example: Si sono conosciuti in treno e si sono piaciuti quasi subito.
     - grammar: valency Refl; reflexive True; auxiliary be; note reciprocal: each other
**Lexeme 2: piacere**, NOUN
- grammar: gender Masc; countability Both
- said: /pja.ˈtʃe.re/ piacére (it-IT)
  1. [A2] sensazione viva di benessere, gioia o soddisfazione del corpo o della mente
     - example: Accetto con grande piacere il vostro invito per la cena di sabato.
     - frames: il piacere di + inf; il piacere di qcs
     - collocations: con piacere; fare piacere; provare piacere; per puro piacere; i piaceri della tavola; i piaceri della vita
     - grammar: countability Both
     - opposites: dispiacere
  2. [A2] azione gentile fatta per aiutare qualcuno o rispondere a una richiesta
     - example: Potresti farmi il piacere di ritirare questo pacco prima di tornare?
     - frames: il piacere di + inf
     - collocations: per piacere; fare un piacere; chiedere un piacere; ricambiare il piacere
     - grammar: countability Count
  3. [B1] libera scelta, volontà o preferenza di una persona
     - example: Nella prova orale puoi iniziare parlando di un argomento a piacere.
     - collocations: a piacere; argomento a piacere; a proprio piacere
     - grammar: countability Mass
- relations: derived_from piacere (VERB)
**Lexeme 3: piacere**, INTJ
- said: /pja.ˈtʃe.re/ piacére (it-IT)
  1. [A1] si usa per salutare con cortesia una persona appena conosciuta
     - example: Sono la nuova insegnante di musica, piacere, cercavo proprio la segreteria.
     - collocations: piacere mio
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
- grammar: conjugation 1; irregular False; valency Impers; auxiliary be; note usually only in simple tenses
- said: /bi.zoɲ.ˈɲa.re/ bisognàre (it-IT)
  1. [A1] essere necessario, opportuno o doveroso che qualcosa si faccia o avvenga
     - example: Per arrivare in orario alla stazione bisogna uscire di casa subito.
     - frames: bisogna + inf; bisogna che + cong
- relations: derived_from bisogno (NOUN)

### 10. figo

#### Entry A

**Lexeme 1: figo**, ADJ
- grammar: comparable True
- said: /ˈfi.ɡo/ fìgo (it-IT)
  1. [B1] molto bello, piacevole, divertente o interessante, capace di colpire ed entusiasmare
     - example: Conosco un posto davvero figo dove ascoltare musica dal vivo stasera.
     - collocations: che figo; troppo figo
     - labels: formality informal
  2. [B2] molto attraente nell’aspetto fisico, affascinante o curato nello stile
     - example: Con quel taglio di capelli sembri ancora più figo del solito.
     - collocations: sentirsi figo
     - labels: formality informal
**Lexeme 2: figo**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈfi.ɡo/ fìgo (it-IT)
  1. [B2] ragazzo o uomo molto attraente, elegante o sicuro di sé
     - example: All’università faceva sempre il figo per farsi notare da tutti.
     - collocations: fare il figo; credersi un figo
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
- said: /ka.ˈzi.no/ casìno (it-IT), /ka.ˈsi.no/ casìno (it-IT)
  1. [B1] grande confusione, rumore o disordine, oppure situazione complicata e difficile
     - example: Non fate casino in corridoio mentre gli altri stanno dormendo.
     - collocations: fare casino; combinare un casino; essere nei casini; mettersi nei casini; piantare un casino
     - labels: formality informal, figurative True
     - grammar: countability Both
  2. [B2] locale in cui si esercita la prostituzione in modo organizzato
     - example: Molti decenni fa una legge nazionale fece chiudere tutti i casini.
     - labels: formality informal
     - grammar: countability Count
  3. [C1] piccolo edificio in campagna o in un parco, usato per riposo o caccia
     - example: Durante la gita visiteremo un antico casino di caccia immerso nel bosco.
     - collocations: casino di caccia; casino di campagna
     - grammar: countability Count
- relations: derived_from casa (NOUN); confusable casinò (NOUN): casinò, with a final accent, means gambling house
