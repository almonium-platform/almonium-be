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

Do not report: the order of senses, a CEFR level off by one step, how syllables are divided, a
definition you would have worded differently when the given one is true, or anything about style.

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

### 1. tropicale

**Lexeme 1: tropicale**, ADJ
- grammar: note one form for masculine and feminine; plural tropicali
- said: /tro.pi.ˈka.le/ tropicàle (it-IT)
  1. [B1] delle zone calde della Terra vicine all'equatore
     - example: Nelle foreste tropicali vivono moltissime specie di animali.
  2. [B2] molto caldo e umido, come nei paesi vicini all'equatore
     - example: Che caldo tropicale oggi, non si riesce a respirare!
     - labels: figurative True
- relations: adjective_of tropico (NOUN)

### 2. inizio

**Lexeme 1: inizio**, NOUN
- grammar: gender Masc; countability Count
- said: /i.ˈnit.tsjo/ inìzio (it-IT)
  1. [A2] momento o prima parte in cui qualcosa comincia
     - example: All'inizio del film non capivo bene la storia.
     - frames: all'inizio (di qc); dare inizio a qc; avere inizio
     - opposites: fine

### 3. amoroso

**Lexeme 1: amoroso**, ADJ
- grammar: comparable True
- said: /a.mo.ˈro.so/ amoróso (it-IT)
  1. [B1] che mostra affetto e tenerezza verso le persone care
     - example: Mia nonna era sempre molto amorosa con tutti i nipoti.
     - frames: amoroso con qcn; amoroso verso qcn
  2. [B1] che riguarda i sentimenti e i rapporti di coppia
     - example: Ultimamente la sua vita amorosa è piuttosto complicata.
- relations: adjective_of amore (NOUN)
**Lexeme 2: amoroso**, NOUN
- grammar: gender Masc; countability Count
- said: /a.mo.ˈro.so/ amoróso (it-IT)
  1. [B2] ragazzo o uomo con cui si ha una relazione sentimentale
     - example: Stasera esce con il suo amoroso e torna tardi.
     - labels: formality informal
- relations: derived_from amore (NOUN)

### 4. sensoriale

**Lexeme 1: sensoriale**, ADJ
- grammar: comparable False
- said: /sen.so.ˈrja.le/ sensoriàle (it-IT)
  1. [B2] della percezione del mondo esterno attraverso gli organi del corpo
     - example: Assaggiare questi vini in cantina è un'esperienza sensoriale unica.

### 5. rifacimento

**Lexeme 1: rifacimento**, NOUN
- grammar: gender Masc; countability Count
- said: /ri.fa.tʃi.ˈmen.to/ rifaciménto (it-IT)
  1. [B2] lavoro con cui si costruisce o si sistema di nuovo una cosa
     - example: Per il rifacimento della strada servono almeno tre mesi di lavori.
     - frames: rifacimento di qcs
  2. [C1] nuova versione di un'opera già esistente, come un film o un testo
     - example: Questo film è il rifacimento di una commedia degli anni Cinquanta.
     - frames: rifacimento di qcs
- relations: verbal_noun_of rifare (VERB)

### 6. modulare

**Lexeme 1: modulare**, VERB
- grammar: conjugation 1; irregular False; valency Tr; auxiliary have
- said: /mo.du.ˈla.re/ modulàre (it-IT)
  1. [C1] variare l’altezza, il tono o la forza della voce o di un suono
     - example: Un buon attore sa modulare la voce secondo il personaggio.
     - frames: modulare la voce
  2. [C1] adattare e regolare qualcosa secondo le esigenze o la situazione
     - example: Possiamo modulare l’orario di lavoro in base alle tue necessità.
     - frames: modulare qualcosa in base a qualcosa
**Lexeme 2: modulare**, ADJ
- grammar: comparable False
- said: /mo.du.ˈla.re/ modulàre (it-IT)
  1. [B2] formato da parti separate che si possono unire e combinare in vari modi
     - example: Abbiamo comprato una libreria modulare da montare in salotto.
- relations: adjective_of modulo (NOUN)

### 7. immune

**Lexeme 1: immune**, ADJ
- grammar: note same form for masculine and feminine; plural immuni
- said: /im.ˈmu.ne/ immùne (it-IT)
  1. [B2] protetto da una malattia, che non può prenderla
     - example: Chi ha già avuto il morbillo di solito è immune.
     - frames: immune a qcs; immune da qcs
  2. [B2] che non subisce l'effetto di qualcosa di negativo o sgradito
     - example: Purtroppo nessuno di noi è immune dagli errori.
     - frames: immune da qcs; immune a qcs
     - labels: figurative True

### 8. qui

**Lexeme 1: qui**, ADV
- grammar: invariable True
- said: /ˈkwi/ qui (it-IT)
  1. [A1] in questo luogo, vicino alla persona che parla
     - example: Vieni qui, ti devo mostrare una cosa importante.
     - opposites: lì
  2. [B1] in questo momento, oppure a questo punto di un discorso o di un fatto
     - example: Fin qui la storia sembra semplice, ma poi cambia tutto.
     - frames: fin qui; da qui in poi

### 9. scapolo

**Lexeme 1: scapolo**, NOUN
- grammar: gender Masc; countability Count; note used of men; for a woman the word is nubile
- said: /ˈska.po.lo/ scàpolo (it-IT)
  1. [B1] uomo che non si è mai sposato
     - example: Lo zio era uno scapolo che amava viaggiare per il mondo.
     - frames: scapolo d'oro
**Lexeme 2: scapolo**, ADJ
- grammar: comparable False; note used of men; for a woman the word is nubile
- said: /ˈska.po.lo/ scàpolo (it-IT)
  1. [B1] non sposato, detto di un uomo
     - example: Mio fratello è ancora scapolo e vive da solo in città.
     - opposites: sposato

### 10. incisivo

**Lexeme 1: incisivo**, ADJ
- grammar: comparable True
- said: /in.tʃi.ˈzi.vo/ incisìvo (it-IT)
  1. [B2] efficace e capace di colpire, di produrre un effetto chiaro e forte
     - example: Il suo discorso è stato breve ma molto incisivo.
  2. [C1] adatto a tagliare, detto dei denti anteriori
     - example: Il bambino ha già messo i quattro denti incisivi.
     - labels: domain anatomy
     - grammar: position Attr
**Lexeme 2: incisivo**, NOUN
- grammar: gender Masc; countability Count
- said: /in.tʃi.ˈzi.vo/ incisìvo (it-IT)
  1. [B2] ciascuno dei denti anteriori, piatti e taglienti, che servono a tagliare il cibo
     - example: Cadendo dalla bici mi sono scheggiato un incisivo.
- relations: derived_from incisivo (ADJ)

### 11. provino

**Lexeme 1: provino**, NOUN
- grammar: gender Masc; countability Count
- said: /pro.ˈvi.no/ provìno (it-IT)
  1. [B2] breve esibizione per scegliere attori, cantanti o ballerini per uno spettacolo
     - example: Domani mia sorella fa un provino per un film importante.
     - frames: fare un provino; provino per qc
  2. [C1] registrazione semplice di un brano, fatta per presentarlo a una casa discografica
     - example: Abbiamo mandato il nostro provino a tre etichette musicali.
     - labels: domain music
- relations: derived_from prova (NOUN)

### 12. anno

**Lexeme 1: anno**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈan.no/ ànno (it-IT)
  1. [A1] periodo di dodici mesi, di solito da gennaio a dicembre
     - example: Ogni anno passiamo le vacanze al mare con i nonni.
  2. [A1] al plurale, l'età di una persona o di una cosa
     - example: Quanti anni ha tuo fratello più piccolo?
     - frames: avere ... anni
  3. [A2] ciascuno dei periodi in cui è diviso un corso di studi
     - example: Mia sorella frequenta il secondo anno di medicina.

### 13. candidare

**Lexeme 1: candidare**, VERB
- grammar: conjugation 1; irregular False; valency Tr; auxiliary both
- said: /kan.di.ˈda.re/ candidàre (it-IT)
  1. [B1] proporsi per un'elezione, un incarico, un premio o un posto di lavoro
     - example: Mi sono candidata per un posto da cameriera in centro.
     - frames: candidarsi a qualcosa; candidarsi per qualcosa; candidarsi a + infinito
     - grammar: valency Refl; reflexive True; auxiliary be
  2. [B2] proporre qualcuno per un'elezione, un incarico o un premio
     - example: Alle prossime elezioni il partito candiderà una giovane avvocata.
     - frames: candidare qualcuno a qualcosa
     - grammar: valency Tr; auxiliary have

### 14. euro

**Lexeme 1: euro**, NOUN
- grammar: gender Masc; invariable True; countability Count
- said: /ˈɛu.ro/ èuro (it-IT)
  1. [A1] moneta comune di molti paesi dell'Unione, divisa in cento centesimi
     - example: Questo libro costa dodici euro e cinquanta centesimi.

### 15. praticamente

**Lexeme 1: praticamente**, ADV
- said: /pra.ti.ka.ˈmen.te/ praticaménte (it-IT)
  1. [B1] quasi del tutto, con poca o nessuna differenza reale
     - example: A quest'ora le strade del centro sono praticamente vuote.
  2. [B2] nei fatti e in modo concreto, non soltanto in teoria
     - example: Dobbiamo capire come risolvere praticamente questo problema.
     - opposites: teoricamente
- relations: adverb_of pratico (ADJ)

### 16. stagione

**Lexeme 1: stagione**, NOUN
- grammar: gender Fem; countability Count
- said: /sta.ˈdʒo.ne/ stagióne (it-IT)
  1. [A1] ciascuno dei quattro periodi dell'anno: primavera, estate, autunno, inverno
     - example: La mia stagione preferita è l'autunno, per i suoi colori.
  2. [B1] periodo dell'anno adatto a un'attività, a un raccolto o a uno spettacolo
     - example: In alta stagione gli alberghi al mare costano molto di più.
     - frames: alta/bassa stagione; di stagione

### 17. divo

**Lexeme 1: divo**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈdi.vo/ dìvo (it-IT)
  1. [B2] persona molto famosa e ammirata dal pubblico, soprattutto nello spettacolo
     - example: All'uscita del teatro i fan aspettavano il divo per un autografo.
     - frames: divo del cinema; fare il divo
**Lexeme 2: divo**, ADJ
- grammar: comparable False
- said: /ˈdi.vo/ dìvo (it-IT)
  1. [C2] che appartiene a un dio o che è degno di un dio
     - example: Il poeta invoca la diva Musa all'inizio del canto.
     - labels: style literary, poetic

### 18. catalogazione

**Lexeme 1: catalogazione**, NOUN
- grammar: gender Fem; countability Both
- said: /ka.ta.lo.ɡat.ˈtsjo.ne/ catalogazióne (it-IT)
  1. [C1] ordinamento e descrizione sistematica di oggetti, libri o documenti in un elenco
     - example: La catalogazione dei libri antichi richiederà almeno due anni di lavoro.
     - frames: catalogazione di qualcosa
- relations: verbal_noun_of catalogare (VERB)

### 19. vendita

**Lexeme 1: vendita**, NOUN
- grammar: gender Fem; countability Both
- said: /ven.ˈdi.ta/ vendìta (it-IT)
  1. [A2] cessione di un bene o di un servizio a qualcuno in cambio di denaro
     - example: Questa casa è in vendita da più di un anno.
     - frames: in vendita; vendita di qcs; mettere in vendita qcs
     - opposites: acquisto
  2. [B1] quantità di merce venduta da un'azienda in un certo periodo
     - example: Quest'anno le vendite dei nostri prodotti sono cresciute molto.
     - grammar: number Plur
- relations: verbal_noun_of vendere (VERB)

### 20. collaborazione

**Lexeme 1: collaborazione**, NOUN
- grammar: gender Fem; countability Both
- said: /kol.la.bo.rat.ˈtsjo.ne/ collaborazióne (it-IT)
  1. [B1] lavoro fatto insieme ad altre persone per raggiungere uno scopo comune
     - example: Grazie alla collaborazione di tutti, finiremo il progetto in tempo.
     - frames: collaborazione con qcn; in collaborazione con qcn/qcs
  2. [B2] rapporto di lavoro non fisso con un'azienda, un giornale o un ente
     - example: Da anni ha una collaborazione con un quotidiano locale.
     - frames: collaborazione a/con qcs
     - grammar: countability Count
- relations: verbal_noun_of collaborare (VERB)

### 21. merito

**Lexeme 1: merito**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈmɛ.ri.to/ mèrito (it-IT)
  1. [B1] qualità o azione che rende una persona degna di lode o di premio
     - example: Ha ottenuto quel posto per merito, non grazie alle conoscenze.
     - frames: per merito; per merito di qcn/qcs
     - opposites: demerito
  2. [B1] fatto di essere la causa di un buon risultato
     - example: Se il progetto è finito in tempo, è merito tuo.
     - frames: essere merito di qcn; dare merito a qcn di qcs
  3. [B2] sostanza, contenuto essenziale di una questione o di un discorso
     - example: Preferisco non entrare nel merito della discussione stasera.
     - frames: entrare nel merito di qcs; in merito a qcs

### 22. astensione

**Lexeme 1: astensione**, NOUN
- grammar: gender Fem; countability Both
- said: /as.ten.ˈsjo.ne/ astensióne (it-IT)
  1. [B2] scelta di non votare in un'elezione o in una votazione
     - example: Alle ultime elezioni l'astensione ha superato il quaranta per cento.
     - frames: astensione dal voto
  2. [C1] rinuncia volontaria a fare o a usare qualcosa
     - example: Il medico mi ha consigliato l'astensione completa dall'alcol per un mese.
     - frames: astensione da qualcosa
- relations: verbal_noun_of astenersi (VERB)

### 23. macchina

**Lexeme 1: macchina**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈmak.ki.na/ màcchina (it-IT)
  1. [A1] veicolo a quattro ruote con motore, per poche persone
     - example: Domani prendiamo la macchina e andiamo al mare con i bambini.
     - frames: in macchina; andare in macchina
  2. [A2] apparecchio fatto di più parti che compie un lavoro preciso
     - example: Questa vecchia macchina serve a tagliare la carta in fogli.
     - frames: macchina da + noun; macchina per + noun
  3. [B2] insieme organizzato di persone e mezzi che funziona in modo complesso
     - example: La macchina dello Stato è lenta e piena di regole inutili.
     - labels: figurative True

### 24. sabotare

**Lexeme 1: sabotare**, VERB
- grammar: conjugation 1; irregular False; valency Tr; auxiliary have
- said: /sa.bo.ˈta.re/ sabotàre (it-IT)
  1. [B2] danneggiare di proposito macchine, impianti o mezzi per impedirne il funzionamento
     - example: Durante la guerra i partigiani sabotavano i binari della ferrovia.
     - frames: sabotare qualcosa
  2. [B2] ostacolare di nascosto un progetto o un'attività perché non riesca
     - example: Qualcuno in ufficio sta cercando di sabotare il nostro lavoro.
     - frames: sabotare qualcosa
     - labels: figurative True

### 25. letteratura

**Lexeme 1: letteratura**, NOUN
- grammar: gender Fem; countability Both
- said: /let.te.ra.ˈtu.ra/ letteratùra (it-IT)
  1. [A2] insieme delle opere scritte di valore artistico di un popolo o di un'epoca
     - example: All'università studio la letteratura francese del Novecento.
  2. [C1] insieme degli studi e dei testi pubblicati su un determinato argomento
     - example: Su questa malattia esiste ormai una letteratura scientifica molto ampia.
     - frames: letteratura su qlco

### 26. raggiungere

**Lexeme 1: raggiungere**, VERB
- grammar: conjugation 2; irregular True; auxiliary have; valency Tr
- said: /rad.ˈdʒun.dʒe.re/ raggiùngere (it-IT)
- forms: raggiungo [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /rad.ˈdʒun.ɡo/ raggiùngo; raggiungi [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /rad.ˈdʒun.dʒi/ raggiùngi; raggiunge [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /rad.ˈdʒun.dʒe/ raggiùnge; raggiungiamo [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /rad.dʒun.ˈdʒa.mo/ raggiungiàmo; raggiungono [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /rad.ˈdʒun.ɡo.no/ raggiùngono; raggiunsi [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /rad.ˈdʒun.si/ raggiùnsi; raggiunse [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /rad.ˈdʒun.se/ raggiùnse; raggiungerò [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] /rad.dʒun.dʒe.ˈrɔ/ raggiungerò; raggiunga [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /rad.ˈdʒun.ɡa/ raggiùnga; raggiunto [VerbForm=Part, Tense=Past, Gender=Masc, Number=Sing] /rad.ˈdʒun.to/ raggiùnto
  1. [A2] arrivare in un luogo, spesso dopo un percorso o un viaggio
     - example: Dopo tre ore di cammino abbiamo raggiunto la cima.
     - frames: raggiungere un luogo
  2. [B1] andare dove si trova una persona, o arrivare alla sua stessa posizione
     - example: Vai pure avanti, ti raggiungo al bar tra dieci minuti.
     - frames: raggiungere qcn
  3. [B1] ottenere un risultato o arrivare a un livello desiderato
     - example: Speriamo di raggiungere un accordo entro la fine del mese.
     - frames: raggiungere un obiettivo
- relations: derived_from giungere (VERB)

### 27. commerciale

**Lexeme 1: commerciale**, ADJ
- said: /kom.mer.ˈtʃa.le/ commerciàle (it-IT)
  1. [A2] legato all'attività di comprare e vendere merci o servizi
     - example: Hanno aperto un nuovo centro commerciale vicino alla stazione.
  2. [B2] fatto per vendere molto, senza grande valore artistico
     - example: Non mi piacciono i film troppo commerciali.
     - labels: attitude disapproving
- relations: adjective_of commercio (NOUN)
**Lexeme 2: commerciale**, NOUN
- grammar: gender Com; countability Count
- said: /kom.mer.ˈtʃa.le/ commerciàle (it-IT)
  1. [B2] persona che in un'azienda si occupa delle vendite ai clienti
     - example: Lavoro come commerciale in una ditta di mobili.
- relations: derived_from commerciale (ADJ)

### 28. pilotare

**Lexeme 1: pilotare**, VERB
- grammar: conjugation 1; irregular False; valency Tr; auxiliary have
- said: /pi.lo.ˈta.re/ pilotàre (it-IT)
  1. [B1] guidare un aereo, una nave o un veicolo da corsa
     - example: Da grande vorrei pilotare gli aerei di linea.
     - frames: pilotare qualcosa
  2. [B2] manovrare di nascosto una scelta o un processo per ottenere il risultato voluto
     - example: Secondo i giudici, qualcuno aveva pilotato l'assegnazione dell'appalto.
     - frames: pilotare qualcosa
     - labels: figurative True
  3. [C1] dirigere con abilità un gruppo o un'impresa, spesso in un momento difficile
     - example: La nuova direttrice ha pilotato l'azienda fuori dalla crisi.
     - frames: pilotare qualcosa fuori da qualcosa
     - labels: figurative True
- relations: derived_from pilota (NOUN)

### 29. infinitamente

**Lexeme 1: infinitamente**, ADV
- said: /in.fi.ni.ta.ˈmen.te/ infinitaménte (it-IT)
  1. [B1] moltissimo, in misura molto grande
     - example: Vi ringrazio infinitamente per tutto l'aiuto che mi avete dato.
  2. [B2] senza limiti, senza una fine nello spazio o nel tempo
     - example: Secondo alcuni scienziati l'universo si estende infinitamente in ogni direzione.
- relations: adverb_of infinito (ADJ)

### 30. compito

**Lexeme 1: compito**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈkom.pi.to/ cómpito (it-IT)
  1. [B1] lavoro o incarico affidato a una persona, che deve svolgerlo
     - example: Il mio compito è controllare che tutte le porte siano chiuse.
     - frames: avere il compito di fare qlco; essere compito di qlcu
  2. [A1] esercizio che l'insegnante dà agli studenti da fare a casa o in classe
     - example: Hai già finito i compiti per domani?
     - frames: fare i compiti; compito in classe
**Lexeme 2: compito**, ADJ
- grammar: comparable True
- said: /kom.ˈpi.to/ compìto (it-IT)
  1. [C2] che si comporta con grande educazione e modi gentili
     - example: Era un signore molto compito, che salutava tutti con un sorriso.
     - opposites: maleducato
- relations: derived_from compire (VERB); confusable compito (NOUN): same spelling; the noun cómpito (task, homework) is stressed on the first syllable

### 31. attentatore

**Lexeme 1: attentatore**, NOUN
- grammar: gender Masc; countability Count; note feminine attentatrice
- said: /at.ten.ta.ˈto.re/ attentatóre (it-IT)
  1. [B2] persona che compie o prova a compiere un'azione violenta e premeditata contro persone o cose
     - example: La polizia ha arrestato l'attentatore poche ore dopo l'esplosione.
- relations: agent_noun_of attentare (VERB)

### 32. pagina

**Lexeme 1: pagina**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈpa.dʒi.na/ pàgina (it-IT)
  1. [A1] ciascuna delle due facce di un foglio di libro, quaderno o giornale
     - example: Aprite il libro a pagina venti e leggete ad alta voce.
  2. [B1] documento di un sito internet che si vede sullo schermo
     - example: Questa pagina non si apre, forse la connessione è lenta.
     - frames: pagina web
     - labels: domain computing
  3. [B2] episodio o periodo importante nella storia di un popolo o di una persona
     - example: Quella guerra resta una pagina buia della nostra storia.
     - labels: figurative True

### 33. impostare

**Lexeme 1: impostare**, VERB
- grammar: conjugation 1; auxiliary have; valency Tr; irregular False
- said: /im.po.ˈsta.re/ impostàre (it-IT)
  1. [B2] organizzare le linee generali di un lavoro, un progetto o un problema
     - example: Prima di cominciare a scrivere, devi impostare bene la tesi.
  2. [B1] regolare un apparecchio o un programma scegliendo valori e opzioni
     - example: Puoi impostare la sveglia alle sette di domani mattina?
  3. [B2] mettere una lettera o una cartolina nella cassetta della posta
     - example: Ricordati di impostare la lettera quando esci di casa.

### 34. portante

**Lexeme 1: portante**, ADJ
- grammar: note same form for masculine and feminine; plural portanti
- said: /por.ˈtan.te/ portànte (it-IT)
  1. [B2] che sostiene il peso di una costruzione o di altre sue parti
     - example: Non potete abbattere quella parete, perché è un muro portante.
  2. [C1] che costituisce la base principale di un'idea, di un'opera o di un progetto
     - example: La libertà resta il tema portante di tutto il romanzo.
     - labels: figurative True
- relations: derived_from portare (VERB)

### 35. bestiale

**Lexeme 1: bestiale**, ADJ
- grammar: comparable True
- said: /bes.ˈtja.le/ bestiàle (it-IT)
  1. [B2] crudele e violento, privo di ogni sentimento umano
     - example: Il prigioniero fu trattato con una violenza bestiale.
     - opposites: umano
  2. [B1] molto grande o intenso, di solito in modo spiacevole
     - example: Oggi fa un caldo bestiale, restiamo in casa.
     - labels: formality informal
  3. [B2] bellissimo, eccezionale, che piace moltissimo
     - example: Sabato scorso abbiamo fatto una festa bestiale in spiaggia.
     - labels: formality informal
- relations: adjective_of bestia (NOUN)

### 36. spazzare

**Lexeme 1: spazzare**, VERB
- grammar: conjugation 1; irregular False; valency Tr; auxiliary have
- said: /spat.ˈtsa.re/ spazzàre (it-IT)
  1. [B1] pulire un pavimento o un luogo togliendo la polvere con la scopa
     - example: Ogni sera spazzo la cucina prima di andare a dormire.
     - frames: spazzare qualcosa
  2. [B2] portare via o eliminare qualcosa con forza e completamente
     - example: Il vento forte ha spazzato via tutte le nuvole.
     - frames: spazzare via qualcosa
     - labels: figurative True
  3. [C1] mangiare tutto il cibo che c'è, senza lasciare niente
     - example: I ragazzi hanno spazzato tutto quello che c'era in frigo.
     - frames: spazzare il piatto
     - labels: formality informal, figurative True

### 37. congelamento

**Lexeme 1: congelamento**, NOUN
- grammar: gender Masc; countability Both
- said: /kon.dʒe.la.ˈmen.to/ congelaménto (it-IT)
  1. [B2] passaggio allo stato solido per effetto del freddo, anche per conservare i cibi
     - example: Il congelamento rapido mantiene intatto il sapore delle verdure.
     - grammar: countability Mass
     - opposites: scongelamento
  2. [C1] lesione dei tessuti del corpo causata da un freddo molto intenso
     - example: Gli alpinisti sono tornati con un principio di congelamento alle dita.
     - grammar: countability Count
  3. [C1] blocco temporaneo di prezzi, salari, beni o attività deciso da un’autorità
     - example: Il governo ha annunciato il congelamento degli stipendi pubblici per due anni.
     - frames: congelamento di qualcosa
     - labels: figurative True
     - grammar: countability Count
- relations: verbal_noun_of congelare (VERB)

### 38. civile

**Lexeme 1: civile**, ADJ
- said: /tʃi.ˈvi.le/ civìle (it-IT)
  1. [B1] che riguarda le persone di uno Stato e i loro diritti, non militare né religioso
     - example: Si sono sposati con rito civile in municipio, senza andare in chiesa.
     - opposites: militare
  2. [B1] educato e rispettoso verso gli altri
     - example: Cerca di comportarti in modo civile anche quando sei arrabbiato.
     - opposites: incivile
  3. [B2] di un popolo o paese con leggi, cultura e progresso sviluppati
     - example: In un paese civile nessuno dovrebbe dormire per strada.
     - opposites: barbaro
**Lexeme 2: civile**, NOUN
- grammar: gender Com; countability Count
- said: /tʃi.ˈvi.le/ civìle (it-IT)
  1. [B2] persona che non fa parte dell'esercito o delle forze armate
     - example: Durante il bombardamento sono morti molti civili innocenti.
     - opposites: militare

### 39. corrompere

**Lexeme 1: corrompere**, VERB
- grammar: conjugation 2; irregular True; valency Tr; auxiliary have; note conjugated like rompere
- said: /kor.ˈrom.pe.re/ corrómpere (it-IT)
- forms: corrompo [Mood=Ind, Tense=Pres, Person=1, Number=Sing] corrómpo; corrompi [Mood=Ind, Tense=Pres, Person=2, Number=Sing] corrómpi; corrompe [Mood=Ind, Tense=Pres, Person=3, Number=Sing] corrómpe; corrompiamo [Mood=Ind, Tense=Pres, Person=1, Number=Plur] corrompiàmo; corrompono [Mood=Ind, Tense=Pres, Person=3, Number=Plur] corrómpono; corruppi [Mood=Ind, Tense=Past, Person=1, Number=Sing] corrùppi; corruppe [Mood=Ind, Tense=Past, Person=3, Number=Sing] corrùppe; corromperò [Mood=Ind, Tense=Fut, Person=1, Number=Sing] corromperò; corrompa [Mood=Sub, Tense=Pres, Person=1, Number=Sing] corrómpa; corrotto [VerbForm=Part, Tense=Past] corrótto
  1. [B2] convincere qualcuno con soldi o favori ad agire contro il proprio dovere
     - example: Hanno cercato di corrompere un giudice per vincere la causa.
     - frames: corrompere qualcuno con qualcosa
     - grammar: valency Tr; auxiliary have
  2. [C1] rendere moralmente peggiore, spingere verso il vizio o la disonestà
     - example: Il potere finisce spesso per corrompere anche le persone oneste.
     - grammar: valency Tr; auxiliary have
  3. [C1] alterare una cosa rendendola guasta, impura o peggiore
     - example: Col tempo l'umidità può corrompere anche i manoscritti più preziosi.
     - frames: corrompere qualcosa; corrompersi
     - grammar: valency Tr; auxiliary have; note also pronominal corrompersi, to decay or degenerate, with essere

### 40. masticare

**Lexeme 1: masticare**, VERB
- grammar: conjugation 1; irregular False; valency Both; auxiliary have
- said: /ma.sti.ˈka.re/ masticàre (it-IT)
  1. [B1] schiacciare e sminuzzare qualcosa con i denti muovendo la bocca
     - example: Bisogna masticare bene il cibo prima di inghiottirlo.
     - frames: masticare qualcosa; masticare una gomma
     - grammar: valency Both
  2. [B2] conoscere poco una lingua e parlarla con una certa fatica
     - example: Mio padre mastica un po' di tedesco per lavoro.
     - frames: masticare un po' di + lingua
     - labels: formality informal, figurative True
     - grammar: valency Tr

### 41. ripiegare

**Lexeme 1: ripiegare**, VERB
- grammar: conjugation 1; irregular False; valency Both; auxiliary have; note adds h before e and i: ripieghi, ripieghiamo
- said: /ri.pje.ˈɡa.re/ ripiegàre (it-IT)
  1. [B1] chiudere qualcosa sovrapponendone una parte all'altra, una o più volte
     - example: Dopo pranzo ripiega la tovaglia e mettila nel cassetto.
     - frames: ripiegare qualcosa
     - grammar: valency Tr; auxiliary have
     - opposites: spiegare
  2. [B2] accontentarsi di una soluzione meno buona di quella desiderata
     - example: Non c'erano più biglietti, così abbiamo ripiegato su un altro film.
     - frames: ripiegare su qualcosa
     - grammar: valency Intr; auxiliary have
  3. [C1] ritirarsi indietro in modo ordinato davanti al nemico
     - example: Sotto il fuoco nemico, il battaglione fu costretto a ripiegare.
     - labels: domain military
     - grammar: valency Intr; auxiliary have
     - opposites: avanzare
  4. [C1] curvarsi verso il basso o chiudersi su se stesso
     - example: I rami degli alberi si ripiegano sotto il peso della neve.
     - frames: ripiegarsi su se stesso
     - grammar: valency Refl; auxiliary be
- relations: derived_from piegare (VERB)

### 42. export

**Lexeme 1: export**, NOUN
- grammar: gender Masc; invariable True; countability Mass; note English loanword
- said: /ˈɛks.port/ èxport (it-IT)
  1. [B2] vendita di merci e prodotti di un paese verso paesi stranieri
     - example: Le piccole aziende puntano sempre di più sull'export.
     - labels: domain economics
     - opposites: import

### 43. politico

**Lexeme 1: politico**, ADJ
- grammar: comparable False
- said: /po.ˈli.ti.ko/ polìtico (it-IT)
  1. [B1] che concerne lo Stato, il governo, i partiti e la vita pubblica
     - example: Dopo le elezioni la situazione politica del paese resta molto incerta.
- relations: adjective_of politica (NOUN)
**Lexeme 2: politico**, NOUN
- grammar: gender Masc; countability Count
- said: /po.ˈli.ti.ko/ polìtico (it-IT)
  1. [B1] persona che fa parte di un partito o lavora nel governo dello Stato
     - example: Molti politici promettono cose che poi non riescono a fare.
- relations: derived_from politica (NOUN)

### 44. costruzione

**Lexeme 1: costruzione**, NOUN
- grammar: gender Fem; countability Both
- said: /kos.trut.ˈtsjo.ne/ costruzióne (it-IT)
  1. [B1] lavoro con cui si realizza un edificio, una strada o un'altra opera
     - example: Il nuovo ospedale è ancora in costruzione da due anni.
     - frames: costruzione di qcs; in costruzione
     - opposites: distruzione
  2. [B1] edificio o altra opera fatta di muri, pietre o materiali solidi
     - example: Accanto alla chiesa c'è una piccola costruzione di pietra.
     - grammar: countability Count
  3. [C1] modo in cui le parole sono ordinate e collegate in una frase
     - example: Questa costruzione è corretta, ma suona poco naturale in italiano.
     - labels: domain grammar
     - grammar: countability Count
- relations: verbal_noun_of costruire (VERB)

### 45. dimagrire

**Lexeme 1: dimagrire**, VERB
- grammar: conjugation 3; irregular False; valency Both; auxiliary be; note inserts -isc- in the present: dimagrisco, dimagrisci, dimagriscono
- said: /di.ma.ˈɡri.re/ dimagrìre (it-IT)
  1. [A2] perdere peso e diventare più snello nel corpo
     - example: Da quando corre ogni mattina, è dimagrita di quattro chili.
     - frames: dimagrire di + quantità
     - grammar: valency Intr; auxiliary be
     - opposites: ingrassare
  2. [B2] far sembrare una persona più snella, detto di vestiti o colori
     - example: Quel vestito nero ti dimagrisce molto, sai?
     - grammar: valency Tr; auxiliary have
  3. [B2] diminuire di quantità o di dimensioni, ridursi
     - example: Con la crisi, i risparmi di molte famiglie sono dimagriti parecchio.
     - labels: figurative True
     - grammar: valency Intr; auxiliary be
- relations: derived_from magro (ADJ)

### 46. sollevazione

**Lexeme 1: sollevazione**, NOUN
- grammar: gender Fem; countability Count
- said: /sol.le.vat.ˈtsjo.ne/ sollevazióne (it-IT)
  1. [C1] ribellione di un popolo o di un gruppo contro chi ha il potere
     - example: Il nuovo aumento delle tasse provocò una sollevazione in tutta la regione.
     - frames: sollevazione contro qualcuno
- relations: verbal_noun_of sollevare (VERB)

### 47. lazzaro

**Lexeme 1: Lazzaro**, PROPN
- grammar: gender Masc; number Sing
- said: /ˈlad.dza.ro/ Làzzaro (it-IT)
  1. [B2] nel Vangelo, amico di Gesù riportato da lui in vita dopo la morte
     - example: Secondo il Vangelo, Gesù chiamò Lazzaro fuori dal sepolcro.
     - labels: domain religion
**Lexeme 2: lazzaro**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈlad.dza.ro/ làzzaro (it-IT)
  1. [C1] persona disonesta e senza scrupoli, mascalzone, furfante
     - example: Non fidarti di quel lazzaro, ti ruberà anche il portafoglio.
     - labels: attitude derogatory
- relations: derived_from Lazzaro (PROPN)

### 48. budino

**Lexeme 1: budino**, NOUN
- grammar: gender Masc; countability Count
- said: /bu.ˈdi.no/ budìno (it-IT)
  1. [A2] dolce morbido da mangiare col cucchiaio, a base di latte, rassodato in uno stampo
     - example: Per merenda i bambini hanno mangiato un budino al cioccolato.
     - frames: budino al cioccolato; budino alla vaniglia

### 49. processore

**Lexeme 1: processore**, NOUN
- grammar: gender Masc; countability Count
- said: /pro.tʃes.ˈso.re/ processóre (it-IT)
  1. [B2] parte principale di un computer che esegue le istruzioni ed elabora i dati
     - example: Questo portatile ha un processore molto veloce e poca memoria.
     - labels: domain computing

### 50. missione

**Lexeme 1: missione**, NOUN
- grammar: gender Fem; countability Count
- said: /mis.ˈsjo.ne/ missióne (it-IT)
  1. [B1] incarico importante dato a qualcuno da svolgere spesso lontano da casa
     - example: I soldati partiranno domani per una missione di pace.
     - frames: in missione; missione di qlco
  2. [B2] compito morale a cui una persona dedica tutta la vita
     - example: Per lei insegnare ai bambini è una vera missione.
  3. [B2] attività dei religiosi che diffondono la fede in paesi lontani; il luogo dove lavorano
     - example: Mio zio ha passato vent'anni in una missione in Africa.
     - labels: domain religion

### 51. calmare

**Lexeme 1: calmare**, VERB
- grammar: conjugation 1; irregular False; valency Tr; auxiliary have
- said: /kal.ˈma.re/ calmàre (it-IT)
  1. [B1] rendere tranquillo qualcuno, far passare agitazione, rabbia o paura
     - example: Le parole della mamma riuscirono a tranquillizzare e calmare il bambino spaventato.
     - frames: calmare qcn
     - grammar: valency Tr
     - opposites: agitare
  2. [B2] rendere meno forte un dolore, un fastidio o un bisogno
     - example: Questa medicina calma il dolore in pochi minuti.
     - frames: calmare qcs
     - grammar: valency Tr
  3. [A2] diventare tranquillo dopo agitazione, rabbia o paura; detto anche di mare e vento
     - example: Calmati, non è successo niente di grave!
     - frames: calmarsi
     - grammar: valency Refl; reflexive True; auxiliary be
- relations: derived_from calma (NOUN)

### 52. compensare

**Lexeme 1: compensare**, VERB
- grammar: conjugation 1; irregular False; valency Tr; auxiliary have
- said: /kom.pen.ˈsa.re/ compensàre (it-IT)
  1. [B2] bilanciare una perdita o uno svantaggio con qualcosa di positivo
     - example: Cerco di recuperare il sonno perso dormendo di più nel weekend, per compensare la settimana.
     - frames: compensare qcs con qcs
  2. [B2] pagare qualcuno per un lavoro svolto o per un danno subito
     - example: Vi compenseremo per tutto il tempo che avete perso.
     - frames: compensare qcn per qcs; compensare qcn di qcs

### 53. indulgenza

**Lexeme 1: indulgenza**, NOUN
- grammar: gender Fem; countability Both
- said: /in.dul.ˈdʒɛn.tsa/ indulgènza (it-IT)
  1. [C1] disposizione a perdonare o a giudicare gli errori altrui senza severità
     - example: Con i bambini più piccoli bisogna usare un po' di indulgenza.
     - frames: indulgenza verso qualcuno; trattare qualcuno con indulgenza
     - grammar: countability Mass
     - opposites: severità
  2. [C1] remissione concessa dalla Chiesa della pena per peccati già perdonati
     - example: Il papa ha concesso l'indulgenza plenaria ai pellegrini del Giubileo.
     - frames: concedere l'indulgenza
     - labels: domain religion
     - grammar: countability Count
- relations: abstract_noun_of indulgente (ADJ)

### 54. vittima

**Lexeme 1: vittima**, NOUN
- grammar: gender Fem; countability Count; note sempre femminile, anche quando indica un uomo
- said: /ˈvit.ti.ma/ vìttima (it-IT)
  1. [B1] persona che muore o resta ferita in un incidente, una guerra o un delitto
     - example: Il terremoto ha causato più di cento vittime.
     - frames: vittima di qlco
  2. [B1] persona che subisce un danno, un'ingiustizia o la cattiveria di altri
     - example: Molti anziani sono vittime di truffe al telefono.
     - frames: essere vittima di qlcu/qlco; fare la vittima
  3. [C1] animale o persona uccisi e offerti a una divinità
     - example: I sacerdoti portavano la vittima davanti all'altare del tempio.
     - labels: domain religion

### 55. video

**Lexeme 1: video**, NOUN
- grammar: gender Masc; countability Count; invariable True
- said: /ˈvi.de.o/ vìdeo (it-IT)
  1. [A1] breve filmato registrato che si guarda su computer, telefono o televisore
     - example: Mi hai mandato quel video del concerto di ieri sera?
  2. [B2] schermo di un televisore o di un computer
     - example: Passo troppe ore davanti al video per lavoro.

### 56. sterilizzazione

**Lexeme 1: sterilizzazione**, NOUN
- grammar: gender Fem; countability Both
- said: /ste.ri.lid.dzat.ˈtsjo.ne/ sterilizzazióne (it-IT)
  1. [B2] eliminazione di germi e batteri da oggetti, cibi o ambienti
     - example: Nei primi mesi la sterilizzazione dei biberon è molto importante.
     - grammar: countability Mass
  2. [B2] operazione che rende una persona o un animale incapace di avere figli
     - example: Il veterinario consiglia di fare la sterilizzazione della gatta.
     - grammar: countability Count
- relations: verbal_noun_of sterilizzare (VERB)

### 57. fomentare

**Lexeme 1: fomentare**, VERB
- grammar: conjugation 1; irregular False; valency Tr; auxiliary have
- said: /fo.men.ˈta.re/ fomentàre (it-IT)
  1. [C1] suscitare o rendere più forte un sentimento ostile o un disordine
     - example: Certi messaggi in rete servono solo a fomentare l’odio tra vicini.
     - frames: fomentare l’odio; fomentare una rivolta
     - opposites: sedare

### 58. feccia

**Lexeme 1: feccia**, NOUN
- grammar: gender Fem; countability Mass
- said: /ˈfet.tʃa/ fèccia (it-IT)
  1. [C1] la parte peggiore e più spregevole di un gruppo di persone
     - example: Per molti quei criminali sono la feccia della società.
     - frames: la feccia di qlco
     - labels: figurative True, attitude derogatory
     - grammar: number Sing
  2. [C2] deposito denso che si forma sul fondo delle botti di vino
     - example: Prima di imbottigliare, bisogna separare il vino dalla feccia.

### 59. monte

**Lexeme 1: monte**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈmon.te/ mónte (it-IT)
  1. [A2] rilievo naturale del terreno molto alto, più grande di una collina
     - example: Dalla cima del monte si vede tutta la valle.
  2. [B2] grande quantità di cose o di qualcosa
     - example: Oggi ho ancora un monte di cose da fare.
     - frames: un monte di qc
     - labels: formality informal, figurative True
  3. [C1] quantità totale di ore, soldi o premi sommati insieme
     - example: Il contratto prevede un monte ore di quaranta ore al mese.
     - frames: monte ore; monte premi

### 60. vandalismo

**Lexeme 1: vandalismo**, NOUN
- grammar: gender Masc; countability Both
- said: /van.da.ˈliz.mo/ vandalìsmo (it-IT)
  1. [B2] danneggiamento o distruzione senza motivo di beni pubblici o di altri
     - example: Ogni notte nel parco si ripetono atti di vandalismo contro le panchine.
     - frames: atto di vandalismo
- relations: derived_from vandalo (NOUN)

### 61. dietro

**Lexeme 1: dietro**, ADP
- said: /ˈdjɛ.tro/ dièTro (it-IT)
  1. [A1] nella parte posteriore di qualcosa o qualcuno, alle spalle
     - example: Il gatto si è nascosto dietro il divano del salotto.
     - frames: dietro (a) qlco/qlcu; dietro di + pronome
     - opposites: davanti
  2. [C1] in cambio di, in seguito a una condizione o una richiesta
     - example: I documenti vengono consegnati soltanto dietro presentazione della ricevuta.
     - frames: dietro pagamento; dietro richiesta
     - labels: style official
  3. [B2] nascosto in qualcosa, come causa o motivo non visibile
     - example: Secondo me, dietro quel sorriso c'è molta tristezza.
     - labels: figurative True
- relations: confusable indietro (ADV): indietro means backwards or behind in progress; dietro means in a position behind something
**Lexeme 2: dietro**, ADV
- said: /ˈdjɛ.tro/ dièTro (it-IT)
  1. [A2] nella parte posteriore, alle spalle di chi parla
     - example: Preferisci sederti davanti o dietro quando viaggi in macchina?
     - frames: di dietro; stare dietro
     - opposites: davanti
  2. [B1] con sé, seguendo chi si sposta
     - example: Portati dietro l'ombrello, perché stasera pioverà di sicuro.
     - frames: portarsi dietro qlco; venire dietro a qlcu
**Lexeme 3: dietro**, NOUN
- grammar: gender Masc; invariable True; countability Count
- said: /ˈdjɛ.tro/ dièTro (it-IT)
  1. [B2] la parte posteriore di un oggetto, di un edificio o del corpo
     - example: Il dietro della casa dà su un piccolo giardino.
     - opposites: davanti

### 62. promettere

**Lexeme 1: promettere**, VERB
- grammar: conjugation 2; irregular True; auxiliary have; valency Tr; note conjugated like mettere
- said: /pro.ˈmet.te.re/ prométtere (it-IT)
- forms: prometto [Person=1, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /pro.ˈmet.to/ prométto; prometti [Person=2, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /pro.ˈmet.ti/ prométti; promette [Person=3, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /pro.ˈmet.te/ prométte; promettiamo [Person=1, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /pro.met.t.ˈja.mo/ promettiàmo; promettono [Person=3, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /pro.ˈmet.to.no/ prométtono; promisi [Person=1, Number=Sing, Tense=Past, Mood=Ind, VerbForm=Fin] /pro.ˈmi.zi/ promìsi; promise [Person=3, Number=Sing, Tense=Past, Mood=Ind, VerbForm=Fin] /pro.ˈmi.ze/ promìse; prometterò [Person=1, Number=Sing, Tense=Fut, Mood=Ind, VerbForm=Fin] /pro.met.te.ˈrɔ/ prometterò; prometta [Person=1, Number=Sing, Tense=Pres, Mood=Sub, VerbForm=Fin] /pro.ˈmet.ta/ prométta; promesso [VerbForm=Part, Tense=Past, Gender=Masc, Number=Sing] /pro.ˈmes.so/ promésso
  1. [A2] impegnarsi con qualcuno a fare o dare sicuramente qualcosa
     - example: Mi hai promesso di aiutarmi con il trasloco sabato.
     - frames: promettere qcs a qcn; promettere di + inf; promettere che
  2. [B2] far prevedere o lasciar sperare qualcosa per il futuro
     - example: Queste nuvole nere promettono pioggia per stasera.
     - frames: promettere bene
- relations: derived_from mettere (VERB)

### 63. quindi

**Lexeme 1: quindi**, CONJ
- grammar: invariable True
- said: /ˈkwin.di/ quìndi (it-IT)
  1. [A2] per questo motivo, di conseguenza; introduce una conclusione
     - example: Piove molto forte, quindi oggi restiamo tutti a casa.
**Lexeme 2: quindi**, ADV
- grammar: invariable True
- said: /ˈkwin.di/ quìndi (it-IT)
  1. [B1] dopo, in seguito, subito dopo un'altra azione
     - example: Mescolate bene le uova, quindi aggiungete la farina e il latte.

### 64. dilatazione

**Lexeme 1: dilatazione**, NOUN
- grammar: gender Fem; countability Both
- said: /di.la.tat.ˈtsjo.ne/ dilatazióne (it-IT)
  1. [B2] aumento di volume o di lunghezza di un corpo, di solito per effetto del calore
     - example: Con il caldo estivo la dilatazione dei binari può creare problemi.
     - frames: dilatazione termica; dilatazione di qualcosa
     - labels: domain physics
     - opposites: contrazione
  2. [C1] allargamento di un organo, di un vaso o di un'apertura del corpo
     - example: Il medico ha notato una leggera dilatazione delle pupille.
     - frames: dilatazione di qualcosa
     - labels: domain medicine
  3. [C1] aumento o prolungamento eccessivo di tempi, spese o quantità
     - example: I cittadini protestano contro la dilatazione dei tempi d'attesa negli ospedali.
     - frames: dilatazione dei tempi
     - labels: figurative True
- relations: verbal_noun_of dilatare (VERB)

### 65. ambiente

**Lexeme 1: ambiente**, NOUN
- grammar: gender Masc; countability Count
- said: /am.ˈbjɛn.te/ ambiènte (it-IT)
  1. [A2] la natura in cui viviamo: aria, acqua, terra, piante e animali
     - example: Dobbiamo proteggere l'ambiente usando meno plastica ogni giorno.
     - grammar: countability Mass
  2. [B1] luogo e condizioni in cui qualcuno vive, lavora o si trova
     - example: In questo ufficio c'è un ambiente tranquillo e piacevole.
     - frames: ambiente di lavoro; ambiente familiare
  3. [B2] gruppo di persone che hanno la stessa attività o gli stessi interessi
     - example: Negli ambienti politici si parla già di nuove elezioni.
     - frames: l'ambiente di qc; negli ambienti di qc
  4. [B2] stanza di una casa o di un edificio
     - example: Cerchiamo un appartamento con tre ambienti e un piccolo giardino.

### 66. precisazione

**Lexeme 1: precisazione**, NOUN
- grammar: gender Fem; countability Count
- said: /pre.tʃi.zat.ˈtsjo.ne/ precisazióne (it-IT)
  1. [B2] spiegazione o aggiunta che rende più chiaro ed esatto quanto è stato detto
     - example: Vorrei fare una piccola precisazione su quello che hai detto.
     - frames: precisazione su qcs
- relations: verbal_noun_of precisare (VERB)

### 67. mirare

**Lexeme 1: mirare**, VERB
- grammar: conjugation 1; irregular False; valency Both; auxiliary have
- said: /mi.ˈra.re/ miràre (it-IT)
  1. [B2] puntare un'arma o un oggetto verso un bersaglio per colpirlo
     - example: Il cacciatore mirò con calma al cervo e poi sparò.
     - frames: mirare a qualcosa; mirare a qualcuno
     - grammar: valency Intr
  2. [B2] avere come scopo o cercare di ottenere qualcosa
     - example: Il nuovo progetto mira a ridurre il traffico in centro.
     - frames: mirare a qualcosa; mirare a + infinito
     - grammar: valency Intr
  3. [C1] guardare a lungo con attenzione o con meraviglia
     - example: Dalla finestra mirava in silenzio le stelle del cielo d'estate.
     - labels: style literary
     - grammar: valency Tr

### 68. troppo

**Lexeme 1: troppo**, ADV
- grammar: invariable True
- said: /ˈtrɔp.po/ tròppo (it-IT)
  1. [A1] più del giusto, del necessario o di quanto si possa sopportare
     - example: Stasera fa troppo freddo per uscire senza cappotto.
     - frames: troppo + agg./avv.; troppo + per + inf.
     - opposites: poco
  2. [B1] molto, in grado altissimo, per esprimere entusiasmo o ammirazione
     - example: Questo film è troppo bello, devi vederlo anche tu!
     - labels: formality informal
**Lexeme 2: troppo**, DET
- said: /ˈtrɔp.po/ tròppo (it-IT)
  1. [A2] in quantità o numero maggiore del giusto o del necessario
     - example: Ci sono troppe macchine in centro a quest'ora.
     - opposites: poco
**Lexeme 3: troppo**, PRON
- said: /ˈtrɔp.po/ tròppo (it-IT)
  1. [A2] una quantità o un numero eccessivo di persone o cose
     - example: Di biscotti ne hai già mangiati troppi oggi.
     - opposites: poco

### 69. militare

**Lexeme 1: militare**, ADJ
- grammar: comparable False
- said: /mi.li.ˈta.re/ militàre (it-IT)
  1. [B1] che riguarda l'esercito, i soldati e le forze armate
     - example: Mio nonno ha fatto il servizio militare in montagna.
     - opposites: civile
**Lexeme 2: militare**, NOUN
- grammar: gender Masc; countability Count; note also used with feminine articles for a woman (la militare)
- said: /mi.li.ˈta.re/ militàre (it-IT)
  1. [B1] persona che fa parte dell'esercito o delle forze armate
     - example: Davanti all'ambasciata ci sono sempre due militari armati.
     - opposites: civile
**Lexeme 3: militare**, VERB
- grammar: conjugation 1; irregular False; valency Intr; auxiliary have; note stress on the stem in mìlito, mìliti, mìlita, mìlitano
- said: /mi.li.ˈta.re/ militàre (it-IT)
  1. [C1] fare parte in modo attivo di un partito, un movimento o una squadra
     - example: Da giovane militavo in un piccolo partito di sinistra.
     - frames: militare in qcs
  2. [C2] essere un argomento o una ragione a favore o contro qualcosa
     - example: Tutti questi indizi militano a favore della sua innocenza.
     - frames: militare a favore di qcs; militare contro qcs
     - labels: style literary

### 70. catastrofico

**Lexeme 1: catastrofico**, ADJ
- grammar: comparable True
- said: /ka.ta.ˈstrɔ.fi.ko/ catastròfico (it-IT)
  1. [B2] che provoca danni gravissimi, distruzione o morte
     - example: Il terremoto ha avuto effetti catastrofici su tutta la regione.
  2. [B2] molto brutto, riuscito nel modo peggiore
     - example: Il mio primo esame all’università è stato catastrofico.
     - labels: formality informal, figurative True
  3. [C1] che prevede sempre il peggio, molto pessimista
     - example: Non essere così catastrofico, troveremo una soluzione.
     - opposites: ottimista
- relations: adjective_of catastrofe (NOUN)

### 71. sviluppatore

**Lexeme 1: sviluppatore**, NOUN
- grammar: gender Masc; countability Count
- said: /zvi.lup.pa.ˈto.re/ sviluppatóre (it-IT)
  1. [B1] persona che progetta e scrive programmi, applicazioni o siti per computer
     - example: Mio fratello lavora come sviluppatore in un'azienda di videogiochi.
- relations: agent_noun_of sviluppare (VERB)

### 72. bunker

**Lexeme 1: bunker**, NOUN
- grammar: gender Masc; countability Count; invariable True
- said: /ˈbun.ker/ bùnker (it-IT)
  1. [B2] rifugio fortificato, spesso sotto terra, che protegge da bombe e attacchi
     - example: Durante la guerra la popolazione si nascondeva nei bunker sotto la città.
  2. [C2] nel golf, buca piena di sabbia che rende difficile il tiro
     - example: Purtroppo la mia pallina è finita di nuovo nel bunker.
     - labels: domain sport

### 73. riorganizzazione

**Lexeme 1: riorganizzazione**, NOUN
- grammar: gender Fem; countability Count
- said: /ri.or.ɡa.nid.dzat.ˈtsjo.ne/ riorganizzazióne (it-IT)
  1. [B2] cambiamento della struttura e del funzionamento di un'azienda, un ente o un'attività
     - example: Con la riorganizzazione dell'ufficio cambieranno gli orari di tutti i dipendenti.
     - frames: riorganizzazione di qcs
- relations: verbal_noun_of riorganizzare (VERB)

### 74. rottamazione

**Lexeme 1: rottamazione**, NOUN
- grammar: gender Fem; countability Both
- said: /rot.ta.mat.ˈtsjo.ne/ rottamazióne (it-IT)
  1. [B2] demolizione di un veicolo o di un apparecchio vecchio per sostituirlo o recuperarne i materiali
     - example: Con gli incentivi per la rottamazione abbiamo cambiato la vecchia auto.
  2. [C1] possibilità di pagare debiti con il fisco senza sanzioni né interessi
     - example: Molti contribuenti hanno aderito alla rottamazione delle cartelle esattoriali.
     - frames: rottamazione delle cartelle
     - labels: domain finance
  3. [C1] sostituzione dei vecchi dirigenti di un partito con persone più giovani
     - example: Il nuovo segretario ha promesso la rottamazione dell’intera classe dirigente.
     - labels: figurative True, domain politics
- relations: verbal_noun_of rottamare (VERB)

### 75. possibile

**Lexeme 1: possibile**, ADJ
- grammar: note same form for masculine and feminine; plural possibili
- said: /pos.ˈsi.bi.le/ possìbile (it-IT)
  1. [A2] che può succedere, esistere o essere fatto
     - example: Secondo te è possibile finire il lavoro entro stasera?
     - frames: è possibile + inf.; è possibile che + congiuntivo
     - opposites: impossibile
  2. [B1] dopo un superlativo, indica il grado più alto che si può raggiungere
     - example: Rispondimi il prima possibile, per favore.
     - frames: il più/il meno ... possibile; il prima possibile
**Lexeme 2: possibile**, NOUN
- grammar: gender Masc; number Sing
- said: /pos.ˈsi.bi.le/ possìbile (it-IT)
  1. [B1] tutto quello che una persona riesce a fare
     - example: Faremo tutto il possibile per aiutare la tua famiglia.
     - frames: fare (tutto) il possibile

### 76. acquario

**Lexeme 1: acquario**, NOUN
- grammar: gender Masc; countability Count
- said: /ak.ˈkwa.rjo/ acquàrio (it-IT)
  1. [A2] vasca di vetro piena d'acqua dove si tengono pesci e piante
     - example: Nel salotto teniamo un piccolo acquario con tre pesci rossi.
  2. [B1] edificio pubblico dove si possono vedere molti animali marini
     - example: Domani porteremo i bambini a visitare l'acquario della città.
- relations: derived_from acqua (NOUN)
**Lexeme 2: Acquario**, PROPN
- grammar: gender Masc
- said: /ak.ˈkwa.rjo/ Acquàrio (it-IT)
  1. [B1] undicesimo segno dello zodiaco, per chi nasce tra gennaio e febbraio
     - example: Mia sorella è dell'Acquario e compie gli anni a febbraio.
     - frames: essere dell'Acquario

### 77. maggioranza

**Lexeme 1: maggioranza**, NOUN
- grammar: gender Fem; countability Count
- said: /mad.dʒo.ˈran.tsa/ maggiorànza (it-IT)
  1. [B1] la parte più numerosa di un gruppo di persone o cose
     - example: La maggioranza degli studenti preferisce studiare la sera.
     - frames: la maggioranza di + nome; nella maggioranza dei casi
     - opposites: minoranza
  2. [B2] numero di voti più alto di quello ottenuto dagli altri in una votazione
     - example: Secondo me la legge passerà con una maggioranza di pochi voti.
     - frames: a maggioranza; maggioranza assoluta
     - opposites: minoranza
  3. [B2] insieme dei partiti che hanno più seggi in parlamento e sostengono il governo
     - example: Domani i partiti di maggioranza discuteranno la nuova riforma.
     - frames: la maggioranza di governo
     - opposites: opposizione
- relations: derived_from maggiore (ADJ)

### 78. erogare

**Lexeme 1: erogare**, VERB
- grammar: conjugation 1; irregular False; valency Tr; auxiliary have; note stress falls on the first syllable in the present: èrogo, èroga, èrogano
- said: /e.ro.ˈɡa.re/ erogàre (it-IT)
  1. [B2] fornire in modo continuo un servizio, energia o una sostanza a chi ne ha bisogno
     - example: In estate l'acquedotto eroga meno acqua nelle ore di punta.
     - frames: erogare qualcosa a qualcuno
  2. [C1] concedere o pagare una somma di denaro per uno scopo preciso
     - example: La banca ha erogato il prestito dopo appena due settimane.
     - frames: erogare un finanziamento a qualcuno

### 79. rotolo

**Lexeme 1: rotolo**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈrɔ.to.lo/ ròtolo (it-IT)
  1. [B1] carta, stoffa o altro materiale avvolto su se stesso a forma di cilindro
     - example: Mi passi un rotolo di carta igienica, per favore?
     - frames: un rotolo di + sostantivo
  2. [B2] nell'espressione andare a: andare in rovina, peggiorare in modo rapido
     - example: Senza un buon direttore, l'azienda sta andando a rotoli.
     - frames: andare a rotoli
     - labels: figurative True, formality informal

### 80. autosufficiente

**Lexeme 1: autosufficiente**, ADJ
- grammar: comparable True
- said: /ˌaw.to.suf.fi.ˈtʃɛn.te/ autosufficiènte (it-IT)
  1. [B2] capace di badare da solo a sé stesso e ai propri bisogni quotidiani
     - example: Mio nonno ha novant’anni, ma è ancora del tutto autosufficiente.
     - frames: non autosufficiente
  2. [C1] che produce da sé ciò di cui ha bisogno, senza dipendere dall’esterno
     - example: Grazie ai pannelli solari, questa casa è autosufficiente per l’energia.
     - frames: autosufficiente in/per qualcosa
- relations: derived_from sufficiente (ADJ)

### 81. verità

**Lexeme 1: verità**, NOUN
- grammar: gender Fem; invariable True; countability Both
- said: /ve.ri.ˈta/ verità (it-IT)
  1. [A2] corrispondenza di ciò che si dice o si pensa con la realtà dei fatti
     - example: Devi dirmi la verità su quello che è successo ieri.
     - frames: dire la verità; in verità
     - grammar: countability Mass
     - opposites: falsità
  2. [B2] fatto, idea o principio accettato come certo e reale
     - example: Ogni religione insegna ai fedeli le proprie verità.
     - grammar: countability Count
- relations: abstract_noun_of vero (ADJ)

### 82. fragola

**Lexeme 1: fragola**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈfra.ɡo.la/ fràgola (it-IT)
  1. [A2] frutto piccolo, rosso e dolce, con semi minuti sulla superficie, o la sua pianta
     - example: A maggio compriamo sempre le fragole al mercato del paese.
     - frames: gelato alla fragola; fragole con la panna

### 83. intermediazione

**Lexeme 1: intermediazione**, NOUN
- grammar: gender Fem; countability Both
- said: /in.ter.me.djat.ˈtsjo.ne/ intermediazióne (it-IT)
  1. [C1] attività di chi fa da tramite tra due parti in un affare o uno scambio
     - example: L’agenzia chiede una commissione per l’intermediazione nella vendita della casa.
     - frames: intermediazione in qualcosa; intermediazione tra qualcuno e qualcuno

### 84. preferenziale

**Lexeme 1: preferenziale**, ADJ
- said: /pre.fe.ren.ˈtsja.le/ preferenziàle (it-IT)
  1. [B2] che dà o riceve un trattamento di favore rispetto ad altri
     - example: Le donne in gravidanza hanno un accesso preferenziale ai servizi sanitari.
     - frames: trattamento preferenziale; corsia preferenziale
- relations: adjective_of preferenza (NOUN)

### 85. demente

**Lexeme 1: demente**, ADJ
- said: /de.ˈmɛn.te/ demènte (it-IT)
  1. [C1] che ha perso in modo grave e duraturo le proprie capacità mentali
     - example: Il nonno ormai è demente e non riconosce più nessuno.
     - labels: domain medicine
  2. [B2] folle, privo di buon senso, detto di persone o di azioni
     - example: Guidare a quella velocità sotto la pioggia è un’idea demente.
     - labels: formality informal, attitude derogatory
     - opposites: sensato
**Lexeme 2: demente**, NOUN
- grammar: gender Com; countability Count
- said: /de.ˈmɛn.te/ demènte (it-IT)
  1. [C1] persona che ha perso in modo grave e duraturo le proprie capacità mentali
     - example: In quel reparto si curano anziani dementi e malati cronici.
     - labels: domain medicine
  2. [B2] persona che si comporta in modo folle e senza buon senso
     - example: Solo un demente poteva lasciare il gas acceso tutta la notte.
     - labels: formality informal, attitude derogatory
- relations: derived_from demente (ADJ)

### 86. onorare

**Lexeme 1: onorare**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have; irregular False
- said: /o.no.ˈra.re/ onoràre (it-IT)
  1. [B2] mostrare grande rispetto e stima verso una persona o un ricordo
     - example: Oggi onoriamo la memoria di chi ha lottato per la libertà.
     - frames: onorare qcn; onorare qcn di/con qcs
     - opposites: disonorare
  2. [C1] rendere una persona degna di stima e buona reputazione
     - example: La tua sincerità ti onora, anche se è stata difficile.
     - opposites: disonorare
  3. [B2] rispettare un impegno preso o pagare quanto si deve
     - example: Devi onorare l'impegno che hai preso con loro.
     - frames: onorare un impegno; onorare un debito
- relations: derived_from onore (NOUN)

### 87. nuziale

**Lexeme 1: nuziale**, ADJ
- grammar: comparable False
- said: /nut.ˈtsja.le/ nuziàle (it-IT)
  1. [B2] del matrimonio, usato o fatto durante la cerimonia e la festa degli sposi
     - example: Domani andiamo a scegliere l'abito nuziale insieme alla mamma.
- relations: adjective_of nozze (NOUN)

### 88. infettare

**Lexeme 1: infettare**, VERB
- grammar: conjugation 1; irregular False; valency Tr; auxiliary have
- said: /in.fet.ˈta.re/ infettàre (it-IT)
  1. [B2] trasmettere germi che causano malattie a una persona, un animale o una cosa
     - example: Una zanzara può infettare molte persone in pochi giorni.
     - frames: infettare qualcuno o qualcosa
     - grammar: valency Tr; auxiliary have
  2. [B2] diventare sede di germi che causano una malattia
     - example: Pulisci bene quel taglio, altrimenti rischia di infettarsi.
     - frames: infettarsi
     - grammar: valency Refl; auxiliary be; reflexive True; note the pronoun marks a change of state, not an action on oneself
  3. [B2] danneggiare un computer o un file con un programma nascosto e dannoso
     - example: Quell'allegato sospetto ha infettato tutti i computer dell'ufficio.
     - labels: domain computing
     - grammar: valency Tr; auxiliary have
  4. [C1] rovinare moralmente qualcuno o qualcosa diffondendo idee o abitudini dannose
     - example: La corruzione aveva ormai infettato ogni livello dell'amministrazione.
     - labels: figurative True
     - grammar: valency Tr; auxiliary have
- relations: derived_from infetto (ADJ)

### 89. adattare

**Lexeme 1: adattare**, VERB
- grammar: conjugation 1; irregular False; valency Tr; auxiliary have; note reflexive adattarsi takes essere
- said: /a.dat.ˈta.re/ adattàre (it-IT)
  1. [B1] modificare qualcosa perché vada bene per un uso o una situazione
     - example: Dobbiamo adattare il programma alle esigenze dei nuovi studenti.
     - frames: adattare qcs a qcs
     - grammar: valency Tr
  2. [B1] abituarsi a condizioni o ambienti nuovi, anche difficili
     - example: Mi sono adattato presto alla vita in una grande città.
     - frames: adattarsi a qcs
     - grammar: valency Refl; reflexive True; auxiliary be
  3. [B2] trasformare un'opera scritta per il cinema, il teatro o la televisione
     - example: Il regista vuole adattare quel vecchio romanzo per il teatro.
     - frames: adattare qcs per qcs
     - grammar: valency Tr

### 90. indigeno

**Lexeme 1: indigeno**, ADJ
- grammar: comparable False
- said: /in.ˈdi.dʒe.no/ indìgeno (it-IT)
  1. [B2] originario del paese o della regione in cui vive o si trova
     - example: In questo parco crescono soltanto piante indigene della zona.
**Lexeme 2: indigeno**, NOUN
- grammar: gender Masc; countability Count
- said: /in.ˈdi.dʒe.no/ indìgeno (it-IT)
  1. [B2] persona che discende dai primi abitanti di una terra e vi abita
     - example: Gli indigeni dell’isola parlano ancora la loro lingua antica.
- relations: derived_from indigeno (ADJ)

### 91. immutabile

**Lexeme 1: immutabile**, ADJ
- said: /im.mu.ˈta.bi.le/ immutàbile (it-IT)
  1. [C1] che non cambia o non può essere cambiato nel tempo
     - example: Per secoli quel paesaggio di montagna è rimasto quasi immutabile.
     - opposites: mutabile
- relations: derived_from mutabile (ADJ)

### 92. irreale

**Lexeme 1: irreale**, ADJ
- grammar: comparable True
- said: /ir.re.ˈa.le/ irreàle (it-IT)
  1. [B2] che non esiste davvero ma solo nella fantasia o nell'immaginazione
     - example: Nei suoi romanzi descrive mondi irreali popolati da creature magiche.
     - opposites: reale
  2. [B2] così strano o insolito da sembrare un sogno
     - example: Dopo la nevicata, in città regnava un silenzio irreale.
- relations: derived_from reale (ADJ)

### 93. tab

**Lexeme 1: tab**, NOUN
- grammar: gender Masc; invariable True; countability Count; note English loanword
- said: /ˈtab/ tab (it-IT)
  1. [B2] tasto della tastiera che sposta il cursore avanti di alcuni spazi
     - example: Premi il tab per passare al campo successivo del modulo.
     - labels: domain computing

### 94. vicino

**Lexeme 1: vicino**, ADJ
- grammar: comparable True
- said: /vi.ˈtʃi.no/ vicìno (it-IT)
  1. [A1] che si trova a poca distanza nello spazio
     - example: La stazione è molto vicina al nostro albergo.
     - frames: vicino a qcs/qcn
     - opposites: lontano
  2. [A2] che sta per arrivare o è successo da poco tempo
     - example: Ormai le vacanze di Natale sono vicine, finalmente!
     - opposites: lontano
  3. [B1] legato da affetto o simile per idee e sentimenti
     - example: In questo momento difficile ti siamo tutti molto vicini.
     - frames: essere vicino a qcn
     - labels: figurative True
**Lexeme 2: vicino**, ADV
- said: /vi.ˈtʃi.no/ vicìno (it-IT)
  1. [A1] a poca distanza, non lontano da un luogo
     - example: Abito qui vicino, posso venire a piedi.
     - frames: vicino a qcs
     - opposites: lontano
**Lexeme 3: vicino**, NOUN
- grammar: gender Masc; countability Count; note femminile: vicina
- said: /vi.ˈtʃi.no/ vicìno (it-IT)
  1. [A2] persona che abita nella casa accanto o nello stesso palazzo
     - example: I nostri vicini fanno sempre festa il sabato sera.
     - frames: vicino di casa; vicino di banco
- relations: derived_from vicino (ADJ)

### 95. oratorio

**Lexeme 1: oratorio**, NOUN
- grammar: gender Masc; countability Count
- said: /o.ra.ˈtɔ.rjo/ oratòrio (it-IT)
  1. [B1] luogo della parrocchia dove i ragazzi si riuniscono per giocare e fare attività
     - example: Ogni pomeriggio i bambini vanno all'oratorio a giocare a calcio.
  2. [B2] piccola chiesa o cappella destinata alla preghiera
     - example: Sulla collina sorge un antico oratorio dedicato alla Madonna.
     - labels: domain religion
  3. [C1] composizione musicale su tema religioso per voci soliste, coro e orchestra
     - example: Il coro sta preparando un oratorio per il concerto di Natale.
     - labels: domain music
**Lexeme 2: oratorio**, ADJ
- said: /o.ra.ˈtɔ.rjo/ oratòrio (it-IT)
  1. [C1] usato nel parlare in pubblico per convincere e commuovere chi ascolta
     - example: Il suo stile oratorio conquistò subito tutto il pubblico in sala.
     - labels: style literary
- relations: adjective_of oratore (NOUN)

### 96. adorare

**Lexeme 1: adorare**, VERB
- grammar: conjugation 1; irregular False; valency Tr; auxiliary have
- said: /a.do.ˈra.re/ adoràre (it-IT)
  1. [A2] amare moltissimo una persona o provare grande piacere per qualcosa
     - example: Adoro passeggiare in riva al mare la mattina presto.
     - frames: adorare qualcuno/qualcosa; adorare + infinito
     - opposites: odiare
  2. [B2] rendere onore e preghiere a un dio, riconoscendone la potenza
     - example: In quella valle si adoravano antiche divinità della natura.
     - frames: adorare Dio
     - labels: domain religion

### 97. aspettare

**Lexeme 1: aspettare**, VERB
- grammar: conjugation 1; auxiliary have; valency Tr; irregular False
- said: /a.spet.ˈta.re/ aspettàre (it-IT)
  1. [A1] restare in un posto finché arriva qualcuno o succede qualcosa
     - example: Ti aspetto davanti alla stazione alle otto.
     - frames: aspettare qcn/qcs; aspettare che + congiuntivo; aspettare di + infinito
  2. [A2] sapere che qualcuno o qualcosa arriverà presto e prepararsi
     - example: Stasera aspettiamo ospiti, quindi devo cucinare molto.
     - frames: aspettare qcn/qcs; aspettare un bambino
  3. [B1] essere nel futuro di qualcuno, detto di fatti o impegni
     - example: Domani ci aspetta una lunga giornata di lavoro.
     - labels: figurative True
  4. [B1] credere che qualcosa succederà o che qualcuno farà qualcosa
     - example: Non mi aspettavo di vederti qui a quest'ora.
     - frames: aspettarsi qcs (da qcn); aspettarsi che + congiuntivo; aspettarsi di + infinito
     - grammar: reflexive True; valency Refl; auxiliary be

### 98. ostruzionismo

**Lexeme 1: ostruzionismo**, NOUN
- grammar: gender Masc; countability Mass
- said: /o.strut.tsjo.ˈniz.mo/ ostruzionìsmo (it-IT)
  1. [C1] tattica di chi in parlamento ritarda o blocca l'approvazione di una legge
     - example: L'opposizione ha fatto ostruzionismo presentando migliaia di emendamenti.
     - frames: fare ostruzionismo
     - labels: domain politics
  2. [C1] comportamento di chi crea di continuo difficoltà alle azioni degli altri
     - example: Il nuovo progetto avanza lentamente per l'ostruzionismo di alcuni colleghi.
     - frames: fare ostruzionismo a qualcuno
     - opposites: collaborazione
- relations: derived_from ostruzione (NOUN)

### 99. pluviale

**Lexeme 1: pluviale**, ADJ
- grammar: comparable False
- said: /plu.ˈvja.le/ pluviàle (it-IT)
  1. [B2] della pioggia, prodotto dalla pioggia o ricco di piogge
     - example: In questa zona l'acqua pluviale viene raccolta in grandi cisterne.
     - frames: acque pluviali; foresta pluviale
**Lexeme 2: pluviale**, NOUN
- grammar: gender Masc; countability Count
- said: /plu.ˈvja.le/ pluviàle (it-IT)
  1. [C1] tubo verticale che porta a terra l'acqua piovana raccolta dal tetto
     - example: Dopo il temporale il pluviale era pieno di foglie secche.
- relations: derived_from pluviale (ADJ)

### 100. spirito

**Lexeme 1: spirito**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈspi.ri.to/ spìrito (it-IT)
  1. [B1] parte non materiale dell'essere umano, sede del pensiero e dei sentimenti
     - example: Lo sport fa bene al corpo e anche allo spirito.
     - opposites: corpo
  2. [B1] atteggiamento interiore con cui una persona o un gruppo affronta qualcosa
     - example: In questa squadra manca davvero lo spirito di gruppo.
     - frames: spirito di + nome; con spirito + agg.
     - grammar: countability Mass
  3. [B2] capacità di dire cose divertenti e intelligenti; senso dell'umorismo
     - example: Ha molto spirito e a tavola fa ridere tutti.
     - frames: persona di spirito; battuta di spirito
     - grammar: countability Mass
  4. [B2] essere soprannaturale senza corpo, come un fantasma o un'anima dei morti
     - example: Dicono che in quel vecchio castello vivano ancora gli spiriti.
     - grammar: countability Count
  5. [B2] alcol puro usato per conservare frutta o per pulire
     - example: Mia nonna metteva sempre le ciliegie sotto spirito.
     - frames: sotto spirito
     - grammar: countability Mass
