# French: entries to check (1 of 1)

These are entries of a learner's dictionary of French, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of French.

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
  sense, or an example is not natural French.
- **collocation**: a collocation that is not said that way in French; or one that does not belong
  among the ten a learner is shown for the sense, because anyone would build it from the meanings
  or because it is rare; or a list that lacks the commonest partner of the sense while it has
  rarer ones. Quote the collocation in "what". A frame with the wrong preposition or case is a
  fact, not this.

Do not report: the order of senses, a CEFR level off by one step, how syllables are divided, a
definition you would have worded differently when the given one is true, or anything about style.

These are this dictionary's own definitions, not faults:

- valency Refl stands for every use with a reflexive pronoun, whatever a grammar of French calls it
  (reflexive, reciprocal, pronominal); Tr means the verb takes a direct object, so a verb whose
  object needs a preposition or the dative is Intr.
- a grammar value (countability, valency, position, number) says how the word is ordinarily used, as
  a learner's dictionary of French prints it: "Mass" is "uncountable" in that sense, although kinds
  of the thing can be counted. Followed by "mainly", the other use is common too. Report a value
  only when a learner's dictionary would not print it for this word or sense.
- position Attr means the adjective is used joined to a noun and not after a verb such as "be",
  wherever French puts it (before the noun in English, mostly after it in French); Pred means only
  after such a verb. Neither says on which side of the noun the adjective stands.
- a lexeme is one word with one way of saying and inflecting it. Two homonyms that are said and
  inflected alike are senses of one lexeme here, although dictionaries of French number them apart.
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

### 1. hôpital

**Lexeme 1: hôpital**, NOUN
- grammar: gender Masc; countability Count
- said: /ɔ.pi.ˈtal/ (fr-FR)
- forms: hôpitaux [Number=Plur] /ɔ.pi.ˈto/
  1. [A1] établissement où des médecins soignent les malades et les blessés
     - example: Après l'accident, on l'a transporté d'urgence à l'hôpital le plus proche.
     - collocations: à l'hôpital; être hospitalisé à l'hôpital

### 2. collection

**Lexeme 1: collection**, NOUN
- grammar: gender Fem; countability Count
- said: /kɔ.lɛk.ˈsjɔ̃/ (fr-FR)
  1. [B1] ensemble d'objets de même sorte réunis et gardés pour leur intérêt ou leur valeur
     - example: Mon grand-père possède une belle collection de timbres anciens.
     - frames: collection de + nom pluriel; faire collection de qqch
     - collocations: une pièce de collection; une collection privée; une riche collection; enrichir sa collection; les collections d'un musée; une collection de timbres
  2. [B2] série de livres publiés par un même éditeur sous un titre commun
     - example: Ce roman vient de paraître dans une collection de poche bon marché.
     - collocations: une collection de poche; paraître dans une collection; diriger une collection
  3. [B2] ensemble des vêtements créés par un couturier pour une saison
     - example: Le styliste présentera sa nouvelle collection la semaine prochaine.
     - collocations: la collection printemps-été; la collection automne-hiver; présenter une collection; une nouvelle collection
  4. [C1] grand nombre de choses ou de personnes du même genre
     - example: Ton devoir contient toute une collection de fautes d'orthographe.
     - frames: toute une collection de + nom pluriel
     - labels: figurative True
- relations: confusable collecte (NOUN): collecte is the gathering of money, goods or data (a charity collection, rubbish collection); collection is a set of objects kept together

### 3. accepter

**Lexeme 1: accepter**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /ak.sɛp.ˈte/ (fr-FR)
  1. [A2] recevoir volontiers ce qu'une personne offre ou propose
     - example: Elle a accepté le cadeau avec un grand sourire.
     - frames: accepter qqch
     - collocations: accepter un cadeau; accepter une invitation; accepter une offre; accepter les cartes bancaires; accepter un poste
     - opposites: refuser
  2. [B1] donner son accord à une demande, une proposition ou une action
     - example: Mon directeur accepte enfin que je travaille depuis la maison.
     - frames: accepter de + inf; accepter que + subj; accepter qqch
     - collocations: accepter une proposition; accepter les conditions; accepter volontiers
     - opposites: refuser
  3. [B2] supporter une situation difficile sans protester ni chercher à la changer
     - example: Avec le temps, j'apprends à accepter mes propres limites.
     - frames: accepter qqch; accepter que + subj
     - collocations: accepter son sort; accepter la défaite; accepter la critique; accepter la réalité
  4. [B2] laisser une personne entrer dans un groupe et la traiter en membre
     - example: Les autres enfants l'ont vite acceptée dans leur bande.
     - frames: accepter qqn dans qqch
     - opposites: rejeter

### 4. propriété

**Lexeme 1: propriété**, NOUN
- grammar: gender Fem
- said: /pʁɔ.pʁi.je.ˈte/ (fr-FR)
  1. [B1] terre, maison ou autre bien qui appartient à quelqu'un
     - example: Ils ont acheté une belle propriété avec un grand jardin.
     - collocations: propriété privée; une grande propriété; une propriété à la campagne; acheter une propriété
     - grammar: countability Count
  2. [B2] droit de posséder une chose et d'en faire ce que l'on veut
     - example: Cet appartement devient notre propriété dès la signature chez le notaire.
     - frames: être la propriété de qqn
     - collocations: droit de propriété; titre de propriété; accéder à la propriété; propriété intellectuelle; la propriété privée
     - grammar: countability Mass
  3. [B2] qualité particulière qui caractérise une chose ou une matière
     - example: Cette plante est connue pour ses propriétés apaisantes contre le stress.
     - frames: les propriétés de qqch
     - collocations: propriétés chimiques; propriétés physiques; propriétés médicinales
     - grammar: countability Count
- relations: confusable propreté (NOUN): similar spelling and sound; propreté means cleanliness, propriété ownership or property

### 5. papier

**Lexeme 1: papier**, NOUN
- grammar: gender Masc
- said: /pa.ˈpje/ (fr-FR)
  1. [A1] matière fine et plate, faite de fibres végétales, sur laquelle on écrit ou imprime
     - example: Pour ce dessin, choisis un papier épais qui ne se déchire pas.
     - collocations: une feuille de papier; papier recyclé; papier toilette; papier peint; papier d'aluminium; papier cadeau; sur papier
     - grammar: countability Mass
  2. [A2] morceau ou feuille portant un texte écrit ou imprimé
     - example: J'ai noté son adresse sur un papier que je ne retrouve plus.
     - collocations: remplir des papiers; signer un papier; un bout de papier
     - grammar: countability Count
  3. [A2] documents officiels qui prouvent l'identité d'une personne
     - example: Le policier nous a demandé de montrer nos papiers à la frontière.
     - collocations: papiers d'identité; vos papiers, s'il vous plaît; avoir ses papiers en règle; demander les papiers
     - grammar: countability Count; number Plur
  4. [B2] texte écrit par un journaliste et publié dans un journal
     - example: Elle prépare un long papier sur la crise du logement.
     - frames: papier sur qqch
     - collocations: écrire un papier; faire un papier
     - grammar: countability Count

### 6. section

**Lexeme 1: section**, NOUN
- grammar: gender Fem; countability Count
- said: /sɛk.ˈsjɔ̃/ (fr-FR)
  1. [B1] chacune des parties dans lesquelles on divise un texte, un lieu ou un ensemble
     - example: Vous trouverez les règles du jeu dans la deuxième section du livret.
     - frames: section de qqch
     - collocations: une section de route; la première section; diviser en sections
  2. [B1] groupe de personnes formant une partie d'une organisation, d'une administration ou d'une école
     - example: Ma fille entre cette année en petite section de maternelle.
     - collocations: section syndicale; section locale; petite section; grande section; section scientifique; section européenne
  3. [C1] surface qu'on obtient en coupant un objet par un plan
     - example: Ce câble a une section de deux millimètres carrés.
     - collocations: section transversale; section circulaire
     - labels: domain mathematics

### 7. espoir

**Lexeme 1: espoir**, NOUN
- grammar: gender Masc
- said: /ɛs.ˈpwaʁ/ (fr-FR)
  1. [A2] sentiment de confiance qu'une chose que l'on désire va arriver
     - example: Nous gardons l'espoir de retrouver notre chat avant l'hiver.
     - frames: l'espoir de + inf; l'espoir de qqch; l'espoir que + ind; dans l'espoir de + inf
     - collocations: garder espoir; perdre espoir; avoir bon espoir; reprendre espoir; donner de l'espoir; sans espoir; un faible espoir; un immense espoir; nourrir l'espoir
     - grammar: countability Both
     - opposites: désespoir
  2. [B1] personne ou chose sur laquelle on compte pour obtenir ce qu'on désire
     - example: Ce nouveau traitement représente le dernier espoir de nombreux malades.
     - collocations: le dernier espoir; le seul espoir
     - grammar: countability Count
  3. [B2] jeune personne dont on attend de grands succès dans son domaine
     - example: Cette jeune nageuse est l'un des grands espoirs du pays.
     - collocations: un jeune espoir; un grand espoir
     - grammar: countability Count; note stays masculine when it refers to a woman
- relations: confusable espérance (NOUN): espérance is more literary or religious; espoir is the everyday word for hope

### 8. approche

**Lexeme 1: approche**, NOUN
- grammar: gender Fem
- said: /a.ˈpʁɔʃ/ (fr-FR)
  1. [B2] manière de considérer et de traiter un sujet ou un problème
     - example: Notre équipe propose une approche différente pour réduire les déchets.
     - frames: approche de qqch
     - collocations: adopter une approche; une nouvelle approche; une approche globale; une approche différente; changer d'approche
     - grammar: countability Count
  2. [B1] mouvement vers un lieu, ou moment où un événement va bientôt arriver
     - example: À l'approche de l'hiver, les oiseaux partent vers le sud.
     - frames: à l'approche de qqch; l'approche de qqch
     - collocations: à l'approche des fêtes; l'approche de la nuit
     - grammar: countability Mass, mainly
- relations: verbal_noun_of approcher (VERB)

### 9. continuer

**Lexeme 1: continuer**, VERB
- grammar: conjugation 1; valency Both; auxiliary have
- said: /kɔ̃.ti.ˈnɥe/ (fr-FR)
  1. [A2] poursuivre une activité déjà commencée, sans s’arrêter ou après une pause
     - example: Malgré la pluie, nous avons continué à marcher jusqu'au village.
     - frames: continuer qqch; continuer à + inf; continuer de + inf
     - grammar: valency Both
     - opposites: arrêter
  2. [A2] se prolonger dans le temps ou dans l’espace
     - example: La pluie continue depuis ce matin sans aucune interruption.
     - grammar: valency Intr

### 10. régulièrement

**Lexeme 1: régulièrement**, ADV
- said: /ʁe.ɡy.ljɛʁ.ˈmɑ̃/ (fr-FR)
  1. [A2] de façon répétée, à des moments séparés par des durées à peu près égales
     - example: Je fais du sport régulièrement, au moins trois fois par semaine.
     - collocations: pratiquer régulièrement; se voir régulièrement; revenir régulièrement; mettre à jour régulièrement
     - opposites: irrégulièrement
  2. [B1] de façon égale et uniforme, avec les mêmes écarts ou le même rythme
     - example: Les arbres sont plantés régulièrement des deux côtés de l'allée.
     - collocations: répartir régulièrement; respirer régulièrement
     - opposites: irrégulièrement
  3. [C1] conformément à la loi ou aux procédures prévues
     - example: Le nouveau maire a été régulièrement élu par le conseil municipal.
     - collocations: régulièrement élu; régulièrement inscrit
- relations: adverb_of régulier (ADJ)

### 11. surprise

**Lexeme 1: surprise**, NOUN
- grammar: gender Fem
- said: /syʁ.ˈpʁiz/ (fr-FR)
  1. [A2] émotion que l'on ressent devant une chose à laquelle on ne s'attendait pas
     - example: À ma grande surprise, le magasin était encore ouvert à minuit.
     - collocations: à ma grande surprise; à la surprise générale; un cri de surprise; avoir la surprise de; au grand étonnement et à la surprise de tous
     - grammar: countability Mass
  2. [A2] événement ou chose que l'on n'attendait pas
     - example: Les résultats de l'élection n'ont réservé aucune surprise cette année.
     - frames: prendre qqn par surprise
     - collocations: une bonne surprise; une mauvaise surprise; créer la surprise; réserver une surprise; sans surprise; par surprise
     - grammar: countability Count
  3. [A2] cadeau ou plaisir préparé en secret pour faire plaisir à quelqu'un
     - example: Nous préparons une surprise pour l'anniversaire de notre mère.
     - frames: faire une surprise à qqn
     - collocations: faire une surprise; une fête surprise; une visite surprise
     - grammar: countability Count
- relations: derived_from surprendre (VERB)

### 12. couple

**Lexeme 1: couple**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈkupl/ (fr-FR)
  1. [A2] deux personnes unies par une relation amoureuse ou par le mariage
     - example: Nos voisins forment un couple très uni depuis trente ans.
     - collocations: être en couple; vivre en couple; former un couple
  2. [B1] ensemble de deux personnes, animaux ou choses associés
     - example: Les danseurs se répartissent par couples avant le début du cours.
     - frames: un couple de + nom pluriel
     - collocations: par couples
- relations: confusable paire (NOUN): paire often denotes two matching or complementary items; couple emphasizes an association, especially a romantic relationship
**Lexeme 2: couple**, NOUN
- grammar: gender Fem; countability Count; note mostly in une couple de + plural noun
- said: /ˈkupl/ (fr-FR)
  1. [B2] groupe de deux ou de quelques personnes ou choses
     - example: Attends-moi ici, je reviens dans une couple de minutes.
     - frames: une couple de + nom pluriel
     - labels: formality informal
     - regions: fr-CA mainly

### 13. bizarre

**Lexeme 1: bizarre**, ADJ
- grammar: comparable True
- said: /bi.ˈzaʁ/ (fr-FR)
  1. [A2] qui ne ressemble pas à ce qu'on voit d'habitude et qui surprend
     - example: Tu n'entends pas un bruit bizarre dans la cuisine ?
     - frames: il est bizarre que + subj; c'est bizarre de + inf
     - collocations: trouver ça bizarre; avoir l'air bizarre; se sentir bizarre; une odeur bizarre; un bruit bizarre; un comportement bizarre; une impression bizarre
     - opposites: normal
  2. [B1] dont la façon d'agir ou de penser étonne par son étrangeté
     - example: Depuis quelques jours, mon voisin est vraiment bizarre avec moi.
     - collocations: un type bizarre; un drôle de bizarre

### 14. rencontrer

**Lexeme 1: rencontrer**, VERB
- grammar: conjugation 1
- said: /ʁɑ̃.kɔ̃.ˈtʁe/ (fr-FR)
  1. [A2] se trouver par hasard en présence de quelqu'un
     - example: Ce matin, j'ai rencontré mon ancien professeur au marché.
     - frames: rencontrer qqn; rencontrer qqn dans la rue
     - collocations: rencontrer par hasard
     - grammar: auxiliary have; valency Tr
  2. [A2] voir quelqu'un pour la première fois et faire sa connaissance
     - example: Elle a rencontré son mari pendant un voyage en Italie.
     - frames: rencontrer qqn
     - collocations: rencontrer l'amour; rencontrer quelqu'un
     - grammar: auxiliary have; valency Tr; note reciprocal se rencontrer: to meet each other
  3. [B1] voir quelqu'un comme prévu pour parler d'une affaire
     - example: Le ministre rencontre les syndicats demain pour discuter des salaires.
     - frames: rencontrer qqn
     - collocations: rencontrer les représentants; rencontrer la presse
     - grammar: auxiliary have; valency Tr
  4. [B1] trouver sur son chemin une difficulté, un obstacle ou une réaction
     - example: Nous rencontrons quelques difficultés avec le nouveau logiciel.
     - frames: rencontrer qqch
     - collocations: rencontrer des difficultés; rencontrer un problème; rencontrer un obstacle; rencontrer un grand succès; rencontrer une résistance
     - grammar: auxiliary have; valency Tr
  5. [B2] jouer contre un adversaire dans un match ou une compétition
     - example: Notre équipe rencontrera le champion en demi-finale.
     - frames: rencontrer qqn
     - grammar: auxiliary have; valency Tr
  6. [B2] exister, être présent ou visible quelque part
     - example: Ce genre d'oiseau se rencontre surtout dans les montagnes.
     - grammar: valency Refl; reflexive True; auxiliary be; note se rencontrer with a thing as subject: 'is found'; the pronoun does not mean oneself

### 15. dormir

**Lexeme 1: dormir**, VERB
- grammar: conjugation 3; irregular True; valency Intr; auxiliary have
- said: /dɔʁ.ˈmiʁ/ (fr-FR)
- forms: dors [Mood=Ind, Tense=Pres, Person=1,2, Number=Sing, VerbForm=Fin] /ˈdɔʁ/; dort [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈdɔʁ/; dormons [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /dɔʁ.ˈmɔ̃/; dorment [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈdɔʁm/; dormi [Tense=Past, VerbForm=Part] /dɔʁ.ˈmi/; dormit [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /dɔʁ.ˈmi/; dormirai [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] /dɔʁ.mi.ˈʁe/; dorme [Mood=Sub, Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin] /ˈdɔʁm/
  1. [A1] être dans un état de repos naturel, les yeux fermés et sans conscience
     - example: Ce soir, je dors chez mes grands-parents à la campagne.
     - frames: dormir chez qqn
     - collocations: dormir à poings fermés; dormir comme un loir; dormir profondément; ne dormir que d'un œil; bien dormir; mal dormir; dormir tard; aller dormir
  2. [B2] rester sans activité, sans être utilisé et sans changer
     - example: Ses économies dorment sur un compte depuis des années.
     - collocations: laisser dormir un projet; l'argent dort
     - labels: figurative True

### 16. historique

**Lexeme 1: historique**, ADJ
- said: /is.tɔ.ˈʁik/ (fr-FR)
  1. [B1] qui présente ou étudie les événements et les sociétés du passé
     - example: Nous préparons une exposition historique sur la vie du village.
  2. [B1] assez marquant pour rester longtemps dans la mémoire collective
     - example: Cette victoire historique marque un tournant pour toute l’équipe.
  3. [B2] dont la réalité est établie, contrairement aux récits inventés
     - example: Ce personnage est historique, même si ses aventures sont inventées.
     - opposites: imaginaire
  4. [B2] jamais atteint auparavant, au point de marquer les esprits
     - example: Le chômage atteint un niveau historique dans cette région.
  5. [B2] présent depuis les débuts d’une organisation ou d’une activité
     - example: Le dirigeant historique du mouvement annonce enfin son départ.
- relations: adjective_of histoire (NOUN)
**Lexeme 2: historique**, NOUN
- grammar: gender Masc; countability Count
- said: /is.tɔ.ˈʁik/ (fr-FR)
  1. [B1] liste enregistrée des pages visitées ou des actions faites sur un appareil
     - example: Pense à effacer l'historique de ton navigateur sur cet ordinateur.
     - labels: domain computing
  2. [B2] récit, dans l'ordre du temps, des faits passés concernant une chose
     - example: Pouvez-vous faire un bref historique du projet pour nos nouveaux collègues ?
     - frames: historique de qqch
     - collocations: faire l’historique
- relations: derived_from historique (ADJ)

### 17. sentir

**Lexeme 1: sentir**, VERB
- grammar: conjugation 3; irregular True; valency Both
- said: /sɑ̃.ˈtiʁ/ (fr-FR)
- forms: sens [Mood=Ind, Tense=Pres, Number=Sing, Person=1,2, VerbForm=Fin] /ˈsɑ̃/; sent [Mood=Ind, Tense=Pres, Number=Sing, Person=3, VerbForm=Fin] /ˈsɑ̃/; sentons [Mood=Ind, Tense=Pres, Number=Plur, Person=1, VerbForm=Fin] /sɑ̃.ˈtɔ̃/; sentent [Mood=Ind, Tense=Pres, Number=Plur, Person=3, VerbForm=Fin] /ˈsɑ̃t/; senti [Tense=Past, VerbForm=Part, Gender=Masc, Number=Sing] /sɑ̃.ˈti/; sentit [Mood=Ind, Tense=Past, Number=Sing, Person=3, VerbForm=Fin] /sɑ̃.ˈti/; sentirai [Mood=Ind, Tense=Fut, Number=Sing, Person=1, VerbForm=Fin] /sɑ̃.ti.ˈʁe/; sente [Mood=Sub, Tense=Pres, Number=Sing, Person=1,3, VerbForm=Fin] /ˈsɑ̃t/
  1. [A2] percevoir une impression physique par le toucher ou par le corps
     - example: Je sens le vent froid sur mon visage ce matin.
     - frames: sentir qqch; sentir qqn/qqch + inf
     - collocations: se faire sentir; sentir la fatigue; sentir la douleur; ne rien sentir
     - grammar: auxiliary have; valency Tr
  2. [A2] éprouver en soi un état physique ou moral
     - example: Depuis que j'ai déménagé, je me sens beaucoup mieux.
     - frames: se sentir + adj; se sentir + inf
     - collocations: se sentir bien; se sentir mal; se sentir seul; se sentir à l'aise; se sentir chez soi; se sentir coupable
     - grammar: valency Refl; reflexive True; auxiliary be
  3. [A2] dégager une odeur, bonne ou mauvaise
     - example: Toute la maison sent bon le pain chaud et le café.
     - frames: sentir + adj; sentir qqch
     - collocations: sentir bon; sentir mauvais; sentir le brûlé; sentir la fumée; sentir le renfermé
     - grammar: auxiliary have; valency Both
  4. [A2] percevoir ou chercher à percevoir une odeur par le nez
     - example: Approche-toi et sens ces roses, elles viennent du jardin.
     - frames: sentir qqch
     - grammar: auxiliary have; valency Tr
  5. [B1] se rendre compte de quelque chose de façon intuitive, sans preuve
     - example: Je sens que cette journée va mal se terminer.
     - frames: sentir que + ind; sentir qqch; sentir venir qqch
     - collocations: sentir le danger
     - grammar: auxiliary have; valency Tr
  6. [B2] laisser deviner, faire penser à une chose qui existe ou approche
     - example: Cette offre trop généreuse sent l'arnaque à plein nez.
     - frames: sentir qqch
     - collocations: sentir à plein nez; ça sent le roussi; sentir le piège
     - labels: figurative True
     - grammar: auxiliary have; valency Tr

### 18. tenter

**Lexeme 1: tenter**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /tɑ̃.ˈte/ (fr-FR)
  1. [B1] essayer de faire quelque chose dont le succès n'est pas certain
     - example: Les secouristes tentent de rejoindre les randonneurs bloqués par la neige.
     - frames: tenter qqch; tenter de + inf
     - collocations: tenter sa chance; tenter le coup; tenter l'impossible; tenter une expérience; tenter une approche
  2. [B1] faire envie à quelqu'un, attirer par ce qui plaît
     - example: Une glace au chocolat après le repas, ça te tente ?
     - frames: tenter qqn; être tenté de + inf; être tenté par qqch
     - collocations: se laisser tenter; ça me tente
  3. [B2] chercher à pousser quelqu'un vers le mal ou le péché
     - example: Dans ce vieux récit, le démon tente l'ermite pendant quarante jours.
     - frames: tenter qqn
     - labels: domain religion

### 19. valoir

**Lexeme 1: valoir**, VERB
- grammar: conjugation 3; irregular True; valency Both
- said: /va.ˈlwaʁ/ (fr-FR)
- forms: vaux [Person=1, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /ˈvo/; vaut [Person=3, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /ˈvo/; valons [Person=1, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /va.ˈlɔ̃/; valent [Person=3, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /ˈval/; valu [Tense=Past, VerbForm=Part] /va.ˈly/; valut [Person=3, Number=Sing, Tense=Past, Mood=Ind, VerbForm=Fin] /va.ˈly/; vaudrai [Person=1, Number=Sing, Tense=Fut, Mood=Ind, VerbForm=Fin] /vo.ˈdʁe/; vaille [Person=1, Number=Sing, Tense=Pres, Mood=Sub, VerbForm=Fin] /ˈvaj/
  1. [A2] avoir un certain prix, une certaine qualité ou une certaine importance
     - example: Aujourd'hui, cette vieille voiture ne vaut plus grand-chose.
     - collocations: valoir cher; ne rien valoir; valoir de l'or; valoir son pesant d'or
     - grammar: auxiliary have
  2. [A2] être préférable, plus avantageux ou plus raisonnable
     - example: Il vaut mieux partir tôt pour éviter les embouteillages.
     - frames: il vaut mieux + inf; il vaut mieux que + subj; mieux vaut + inf; valoir mieux que qqch
     - collocations: mieux vaut tard que jamais
     - grammar: auxiliary have; note always with mieux, mostly in the impersonal il vaut mieux
  3. [B1] mériter, par son intérêt, l'effort ou le temps qu'on y consacre
     - example: Ce petit village vaut vraiment le détour, tu verras.
     - frames: valoir la peine de + inf; valoir la peine que + subj
     - collocations: valoir la peine; valoir le coup; valoir le détour
     - grammar: auxiliary have; valency Tr
  4. [B2] faire obtenir quelque chose à quelqu'un, en bien ou en mal
     - example: Son retard lui a valu une remarque sévère du directeur.
     - frames: valoir qqch à qqn
     - collocations: valoir des ennuis; valoir des critiques
     - grammar: auxiliary have; valency Tr
  5. [B2] s'appliquer à quelqu'un ou à quelque chose, être vrai aussi pour lui
     - example: Cette remarque vaut aussi pour ceux qui arrivent en retard.
     - frames: valoir pour qqn/qqch
     - grammar: auxiliary have; valency Intr
  6. [B2] être de même qualité ou de même niveau, sans réelle différence
     - example: Ces deux candidats se valent, le choix sera difficile.
     - grammar: valency Refl; reflexive True; auxiliary be; note plural subject; the pronoun means each other

### 20. améliorer

**Lexeme 1: améliorer**, VERB
- grammar: conjugation 1
- said: /a.me.ljɔ.ˈʁe/ (fr-FR)
  1. [A2] changer qqch pour le rendre plus satisfaisant ou plus efficace
     - example: Je lis beaucoup pour améliorer mon français avant le voyage.
     - frames: améliorer qqch
     - collocations: améliorer la situation; améliorer la qualité; améliorer ses résultats; améliorer les conditions de vie; améliorer son niveau; nettement améliorer
     - grammar: auxiliary have; valency Tr
     - opposites: aggraver
  2. [B1] devenir plus satisfaisant, évoluer dans un sens favorable
     - example: Depuis quelques jours, le temps s'améliore enfin sur la côte.
     - collocations: la situation s'améliore; le temps s'améliore; s'améliorer nettement; s'améliorer progressivement
     - grammar: valency Refl; reflexive True; auxiliary be
     - opposites: empirer

### 21. normal

**Lexeme 1: normal**, ADJ
- grammar: comparable True
- said: /nɔʁ.ˈmal/ (fr-FR)
- forms: normaux [Gender=Masc, Number=Plur] /nɔʁ.ˈmo/
  1. [A2] conforme à ce qui arrive le plus souvent, sans rien de particulier
     - example: Aujourd'hui, les températures restent normales pour la saison.
     - opposites: anormal
  2. [A2] qui ne surprend pas, que l'on peut facilement comprendre ou accepter
     - example: Il est normal que tu sois inquiet avant l'examen.
     - frames: il est normal que + subj; c'est normal de + inf
  3. [B1] qui fonctionne ou se développe sans problème particulier
     - example: Depuis sa réparation, le moteur retrouve un fonctionnement normal.
     - opposites: anormal
- relations: adjective_of norme (NOUN)

### 22. incroyable

**Lexeme 1: incroyable**, ADJ
- grammar: comparable True
- said: /ɛ̃.kʁwa.ˈjabl/ (fr-FR)
  1. [A2] si étonnant qu'on a du mal à le tenir pour vrai
     - example: Il m'a raconté une histoire incroyable sur son voyage en mer.
     - frames: il est incroyable que + subj; c'est incroyable de + inf
     - collocations: incroyable mais vrai; c'est incroyable; une histoire incroyable; une nouvelle incroyable
     - opposites: croyable
  2. [B1] qui dépasse de loin ce qui est habituel, par sa force ou sa qualité
     - example: Tu as eu une chance incroyable de trouver ce billet.
     - collocations: une chance incroyable; un talent incroyable; une vitesse incroyable; un nombre incroyable; une énergie incroyable; vraiment incroyable
- relations: derived_from croyable (ADJ)

### 23. mourir

**Lexeme 1: mourir**, VERB
- grammar: conjugation 3; irregular True; valency Intr; auxiliary be
- said: /mu.ˈʁiʁ/ (fr-FR)
- forms: meurs [Mood=Ind, Tense=Pres, Person=1,2, Number=Sing, VerbForm=Fin] /ˈmœʁ/; meurt [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈmœʁ/; mourons [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /mu.ˈʁɔ̃/; meurent [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈmœʁ/; mort [VerbForm=Part, Tense=Past] /ˈmɔʁ/; mourut [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /mu.ˈʁy/; mourrai [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] /muʁ.ˈʁe/; meure [Mood=Sub, Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin] /ˈmœʁ/
  1. [A2] cesser de vivre, pour une personne, un animal ou une plante
     - example: Mon grand-père est mort paisiblement dans son sommeil l'hiver dernier.
     - frames: mourir de qqch
     - collocations: mourir jeune; mourir de vieillesse; mourir sur le coup; mourir de ses blessures; mourir dans un accident; mourir en paix
     - opposites: naître
  2. [B1] ressentir très fortement une sensation, un besoin ou un sentiment
     - example: Ouvre donc la fenêtre, on meurt de chaud dans cette pièce.
     - frames: mourir de qqch; mourir d'envie de + inf
     - collocations: mourir de faim; mourir de soif; mourir de peur; mourir d'ennui; mourir de rire; mourir de chaud; mourir d'envie
     - labels: figurative True
  3. [B2] disparaître peu à peu, perdre sa force jusqu'à ne plus exister
     - example: Dans ces villages isolés, les vieilles traditions meurent lentement.
     - labels: figurative True

### 24. tiers

**Lexeme 1: tiers**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈtjɛʁ/ (fr-FR)
  1. [A2] chacune des trois parties égales qui forment un tout
     - example: Près d'un tiers des élèves choisissent l'option musique cette année.
     - frames: un tiers de qqch
     - collocations: les deux tiers; réduire d'un tiers; un bon tiers; le premier tiers; le dernier tiers
  2. [B2] personne extérieure à une affaire, qui n'y est pas directement concernée
     - example: Vos données personnelles ne seront jamais transmises à des tiers.
     - collocations: assurance au tiers; par l'intermédiaire d'un tiers; un tiers de confiance; transmettre à des tiers
**Lexeme 2: tiers**, ADJ
- grammar: position Attr; comparable False; note after the noun in pays tiers, before it in tierce personne
- said: /ˈtjɛʁ/ (fr-FR)
- forms: tierce [Gender=Fem, Number=Sing] /ˈtjɛʁs/; tierces [Gender=Fem, Number=Plur] /ˈtjɛʁs/; tiers [Gender=Masc, Number=Plur] /ˈtjɛʁ/
  1. [B2] qui vient de l'extérieur, qui n'est pas l'une des parties concernées
     - example: Une tierce personne devra signer ce document comme témoin.
     - collocations: une tierce personne; un pays tiers; le tiers payant; en main tierce

### 25. docteur

**Lexeme 1: docteur**, NOUN
- grammar: gender Masc; countability Count; note feminine docteure, now common; docteur is also still used for a woman
- said: /dɔk.ˈtœʁ/ (fr-FR)
- forms: docteure [Gender=Fem, Number=Sing] /dɔk.ˈtœʁ/
  1. [A1] personne dont le métier est d'examiner et de soigner les malades
     - example: J'ai de la fièvre, alors je vais chez le docteur demain.
     - collocations: aller chez le docteur; appeler le docteur; consulter un docteur; bonjour, docteur
  2. [B2] personne qui a obtenu le plus haut grade délivré par une université
     - example: Elle est docteur en chimie et enseigne maintenant à l'université.
     - frames: docteur en + nom
     - collocations: docteur en médecine; docteur en droit; docteur ès lettres; docteur honoris causa
- relations: confusable médecin (NOUN): médecin names the profession; docteur is used to address a physician and in everyday speech, and also means anyone holding a doctorate

### 26. quatrième

**Lexeme 1: quatrième**, ADJ
- grammar: comparable False
- said: /ka.tʁi.ˈjɛm/ (fr-FR)
  1. [A1] qui vient juste après le troisième dans une suite ou un classement
     - example: Votre bureau se trouve au bout du couloir, c'est la quatrième porte.
     - collocations: arriver quatrième; finir quatrième; le quatrième âge
     - grammar: note also used as a noun for a person or thing: le quatrième, la quatrième
- relations: derived_from quatre (NUM)
**Lexeme 2: quatrième**, NOUN
- grammar: gender Masc; countability Count; number Sing, mainly
- said: /ka.tʁi.ˈjɛm/ (fr-FR)
  1. [A2] étage d'un immeuble situé juste au-dessus du troisième
     - example: Nous habitons au quatrième, mais l'ascenseur est souvent en panne.
     - collocations: au quatrième; au quatrième étage
- relations: derived_from quatrième (ADJ)
**Lexeme 3: quatrième**, NOUN
- grammar: gender Fem; countability Count; number Sing, mainly
- said: /ka.tʁi.ˈjɛm/ (fr-FR)
  1. [B1] troisième année du collège, pour des élèves d'environ treize ans
     - example: Ma fille entre en quatrième à la rentrée de septembre.
     - collocations: en quatrième; entrer en quatrième; redoubler sa quatrième
     - labels: domain education
  2. [B1] vitesse d'une voiture qui vient après la troisième
     - example: Sur cette route, tu peux passer la quatrième sans problème.
     - collocations: passer la quatrième; rouler en quatrième
- relations: derived_from quatrième (ADJ)

### 27. député

**Lexeme 1: député**, NOUN
- grammar: gender Masc; countability Count
- said: /de.py.ˈte/ (fr-FR)
- forms: députée [Gender=Fem, Number=Sing] /de.py.ˈte/
  1. [B1] personne élue par les citoyens pour voter les lois dans une assemblée
     - example: Notre député vient d'être réélu avec une large majorité.
     - frames: député de qqch
     - collocations: élire un député; député européen; député de la majorité; député de l'opposition; siéger comme député; une proposition de loi déposée par un député

### 28. européen

**Lexeme 1: européen**, ADJ
- said: /ø.ʁɔ.pe.ˈɛ̃/ (fr-FR)
- forms: européenne [Gender=Fem, Number=Sing] /ø.ʁɔ.pe.ˈɛn/; européens [Gender=Masc, Number=Plur] /ø.ʁɔ.pe.ˈɛ̃/; européennes [Gender=Fem, Number=Plur] /ø.ʁɔ.pe.ˈɛn/
  1. [A2] qui se trouve en Europe ou qui concerne l'Europe et ses habitants
     - example: Nous avons visité plusieurs capitales européennes pendant les vacances d'été.
     - collocations: le continent européen; les pays européens; une capitale européenne; la culture européenne
     - grammar: comparable False
  2. [B1] qui concerne l'union politique et économique de nombreux pays d'Europe
     - example: Cette nouvelle règle européenne s'applique dans tous les États membres.
     - collocations: l'Union européenne; les élections européennes; la Commission européenne; le Parlement européen; une directive européenne; la construction européenne
     - grammar: comparable False
  3. [B2] favorable à une union plus étroite entre les pays d'Europe
     - example: Ce candidat se montre très européen dans son programme.
     - grammar: comparable True
- relations: adjective_of Europe (PROPN)
**Lexeme 2: Européen**, NOUN
- grammar: gender Masc; countability Count
- said: /ø.ʁɔ.pe.ˈɛ̃/ (fr-FR)
- forms: Européenne [Gender=Fem, Number=Sing] /ø.ʁɔ.pe.ˈɛn/
  1. [A2] personne née en Europe ou qui y habite
     - example: Beaucoup d'Européens choisissent de passer l'hiver dans les pays chauds.
- relations: derived_from européen (ADJ)

### 29. verre

**Lexeme 1: verre**, NOUN
- grammar: gender Masc
- said: /ˈvɛʁ/ (fr-FR)
  1. [A1] petit récipient sans anse dans lequel on sert et on boit une boisson
     - example: Pose ton verre sur la table avant de te lever.
     - frames: un verre de qqch
     - collocations: un verre à vin; un verre à pied; un verre d'eau; remplir un verre; vider son verre; lever son verre; trinquer avec son verre
     - grammar: countability Count
  2. [A2] matière dure, transparente et fragile, obtenue en faisant fondre du sable
     - example: Cette vieille bouteille en verre vient de la cave de mes grands-parents.
     - collocations: en verre; un éclat de verre; du verre brisé; du verre trempé; souffler le verre; recycler le verre
     - grammar: countability Mass
  3. [A2] boisson, souvent alcoolisée, que l'on prend en compagnie d'autres personnes
     - example: Après le travail, on va prendre un verre en terrasse.
     - collocations: prendre un verre; boire un verre; offrir un verre; payer un verre; un verre de trop
     - grammar: countability Count
  4. [B1] lentille transparente placée dans des lunettes pour corriger ou protéger la vue
     - example: Mes nouveaux verres sont plus fins et beaucoup plus légers.
     - collocations: des verres correcteurs; des verres progressifs; des verres de contact; porter des verres
     - grammar: countability Count; number Plur, mainly
- relations: confusable vert (ADJ): same pronunciation /vɛʁ/; confusable ver (NOUN): same pronunciation /vɛʁ/; ver is a worm

### 30. théorie

**Lexeme 1: théorie**, NOUN
- grammar: gender Fem
- said: /te.ɔ.ˈʁi/ (fr-FR)
  1. [B1] ensemble organisé d'idées et de lois qui explique des faits ou des phénomènes
     - example: Ce physicien a passé sa vie à défendre une théorie nouvelle.
     - frames: théorie de qqch; théorie sur qqch
     - collocations: la théorie de l'évolution; la théorie de la relativité; élaborer une théorie; formuler une théorie; réfuter une théorie; une théorie scientifique
     - grammar: countability Count
  2. [B1] connaissance abstraite d'un domaine, sans application concrète
     - example: En théorie, le plan est simple, mais tout se complique ensuite.
     - collocations: en théorie; la théorie et la pratique; un cours de théorie
     - grammar: countability Mass
     - opposites: pratique
  3. [B2] idée personnelle que l'on propose pour expliquer quelque chose
     - example: J'ai ma petite théorie sur la disparition de nos clés.
     - frames: la théorie selon laquelle + ind
     - collocations: avoir une théorie; la théorie du complot; une théorie fumeuse
     - grammar: countability Count

### 31. concept

**Lexeme 1: concept**, NOUN
- grammar: gender Masc; countability Count
- said: /kɔ̃.ˈsɛpt/ (fr-FR)
  1. [B2] représentation générale et abstraite que l'esprit se fait d'une chose
     - example: Pour un jeune enfant, le concept de temps reste difficile à saisir.
     - frames: le concept de qqch
     - collocations: un concept abstrait; définir un concept; un concept clé; maîtriser un concept
  2. [B2] idée nouvelle sur laquelle reposent un produit, un service ou un projet
     - example: Nous avons lancé un concept original de restaurant sans menu fixe.
     - collocations: un nouveau concept; un concept original; lancer un concept; développer un concept

### 32. certainement

**Lexeme 1: certainement**, ADV
- said: /sɛʁ.tɛn.ˈmɑ̃/ (fr-FR)
  1. [A2] selon toute probabilité, sans en être tout à fait sûr
     - example: Sa voiture n'est plus là, il est certainement déjà parti.
     - collocations: très certainement
  2. [B1] de façon sûre, sans aucun doute possible
     - example: Je viendrai certainement à ta fête, je te le promets.
     - collocations: certainement pas; mais certainement
- relations: adverb_of certain (ADJ)

### 33. agir

**Lexeme 1: agir**, VERB
- grammar: conjugation 2
- said: /a.ˈʒiʁ/ (fr-FR)
  1. [A2] faire quelque chose pour obtenir un résultat ou changer une situation
     - example: Face à la pollution, nous devons agir sans attendre.
     - frames: agir pour qqn/qqch; agir au nom de qqn
     - collocations: agir vite; il faut agir; agir à temps; agir en connaissance de cause
     - grammar: auxiliary have; valency Intr
  2. [B1] se conduire d'une certaine manière, surtout envers les autres
     - example: Tu as agi comme un enfant pendant toute la réunion.
     - frames: agir en + nom; agir comme + nom; agir envers qqn
     - collocations: agir de bonne foi; agir en conséquence; mal agir; agir honnêtement
     - grammar: auxiliary have; valency Intr
  3. [B1] produire un effet sur quelqu'un ou sur quelque chose
     - example: Ce médicament agit rapidement contre la douleur.
     - frames: agir sur qqn/qqch
     - collocations: agir rapidement; agir sur l'organisme
     - grammar: auxiliary have; valency Intr
  4. [B1] être question de quelque chose, ou être nécessaire de faire quelque chose
     - example: Maintenant, il s'agit de trouver une solution avant ce soir.
     - frames: il s'agit de qqch; il s'agit de + inf; il s'agit que + subj
     - collocations: de quoi s'agit-il; quand il s'agit de
     - grammar: valency Impers; reflexive True; auxiliary be; note only in the impersonal form il s'agit; s'agit-il in questions

### 34. processus

**Lexeme 1: processus**, NOUN
- grammar: gender Masc; countability Count; invariable True
- said: /pʁɔ.se.ˈsys/ (fr-FR), /pʁɔ.sɛ.ˈsys/ (fr-FR)
  1. [B1] suite d'étapes ou de faits qui mène peu à peu à un résultat
     - example: Apprendre une langue est un processus long qui demande de la patience.
     - frames: processus de qqch
     - collocations: processus de paix; processus de décision; engager un processus; lancer un processus; un processus long; processus de fabrication; processus naturel; au cours du processus
- relations: confusable procès (NOUN): procès means a trial in court, while English 'process' is usually processus

### 35. perte

**Lexeme 1: perte**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈpɛʁt/ (fr-FR)
  1. [B1] disparition d'une chose, d'une qualité ou d'une capacité qu'on avait
     - example: Je dois signaler la perte de mon passeport à la police.
     - frames: perte de qqch
     - collocations: perte de poids; perte de mémoire; perte de connaissance; perte d'emploi; perte de confiance; déclarer la perte
  2. [A2] mauvais emploi du temps, de l'argent ou des efforts, sans résultat utile
     - example: Franchement, cette réunion était une perte de temps pour tout le monde.
     - frames: perte de qqch
     - collocations: perte de temps; perte d'argent; en pure perte
     - grammar: countability Count; note usually in the singular
  3. [B1] somme d'argent perdue dans une affaire ou une activité
     - example: L'entreprise annonce de lourdes pertes pour la deuxième année consécutive.
     - collocations: vendre à perte; subir des pertes; enregistrer des pertes; perte sèche; pertes et profits; être en perte
     - opposites: bénéfice
  4. [B1] mort d'une personne proche, ressentie comme un grand manque
     - example: Elle ne s'est jamais vraiment remise de la perte de son père.
     - frames: perte de qqn
     - collocations: la perte d'un être cher; une perte cruelle
  5. [B2] personnes tuées ou blessées pendant une guerre ou une catastrophe
     - example: Les deux armées ont subi des pertes très importantes pendant l'hiver.
     - collocations: pertes humaines; subir des pertes; de lourdes pertes
     - grammar: number Plur
  6. [C1] ruine ou malheur grave qui détruit complètement une personne
     - example: Son orgueil finira par causer sa perte, j'en suis certain.
     - frames: causer la perte de qqn; jurer la perte de qqn
     - collocations: courir à sa perte
     - labels: style literary
- relations: verbal_noun_of perdre (VERB)

### 36. préparer

**Lexeme 1: préparer**, VERB
- grammar: conjugation 1
- said: /pʁe.pa.ˈʁe/ (fr-FR)
  1. [A2] mettre une chose en état de servir ou d'être utilisée
     - example: Je prépare la chambre d'amis avant l'arrivée de mes parents.
     - frames: préparer qqch pour qqn; préparer qqch pour qqch
     - collocations: préparer ses bagages; préparer sa valise; préparer ses affaires; préparer le terrain
     - grammar: auxiliary have; valency Tr
  2. [A1] faire un plat ou un repas avec des aliments
     - example: Ce soir, c'est mon frère qui prépare le dîner pour tout le monde.
     - frames: préparer qqch à qqn
     - collocations: préparer le dîner; préparer un repas; préparer à manger; préparer le café; préparer une recette
     - grammar: auxiliary have; valency Tr
  3. [A2] organiser à l'avance tout ce qu'il faut pour une action
     - example: Nous préparons une fête surprise pour son anniversaire.
     - collocations: préparer un voyage; préparer une surprise; préparer un projet; préparer un discours; préparer une réunion; préparer soigneusement
     - grammar: auxiliary have; valency Tr
  4. [B1] travailler pour être capable de réussir un examen ou un concours
     - example: Elle prépare le baccalauréat et révise tous les soirs.
     - collocations: préparer un examen; préparer un concours; préparer un diplôme
     - grammar: auxiliary have; valency Tr
  5. [B1] rendre une personne capable de faire face à une situation
     - example: Ces années de stage l'ont bien préparé à ce nouveau métier.
     - frames: préparer qqn à qqch; préparer qqn à + inf
     - collocations: préparer psychologiquement; être mal préparé
     - grammar: auxiliary have; valency Tr
  6. [A2] se rendre prêt pour une action ou pour sortir
     - example: Dépêche-toi de te préparer, le taxi arrive dans dix minutes.
     - frames: se préparer à + inf; se préparer à qqch; se préparer pour qqch
     - collocations: se préparer au pire; se préparer pour sortir
     - grammar: valency Refl; reflexive True; auxiliary be
  7. [B2] être sur le point d'arriver ou de se produire
     - example: Regarde ces nuages noirs : un gros orage se prépare.
     - collocations: un orage se prépare
     - grammar: valency Refl; reflexive True; auxiliary be; note with a thing or an event as subject

### 37. domicile

**Lexeme 1: domicile**, NOUN
- grammar: gender Masc; countability Count
- said: /dɔ.mi.ˈsil/ (fr-FR)
  1. [B1] lieu où une personne vit de façon habituelle
     - example: Le médecin passe à domicile quand le patient ne peut pas se déplacer.
     - frames: au domicile de qqn
     - collocations: à domicile; livraison à domicile; sans domicile fixe; élire domicile; regagner son domicile; quitter le domicile; domicile conjugal; violation de domicile
  2. [B2] adresse officielle où une personne a son principal établissement, selon la loi
     - example: Vous devez déclarer tout changement de domicile à l'administration.
     - collocations: changement de domicile; domicile légal; justificatif de domicile
     - labels: domain law

### 38. recevoir

**Lexeme 1: recevoir**, VERB
- grammar: conjugation 3; irregular True; valency Tr; auxiliary have
- said: /ʁə.sə.ˈvwaʁ/ (fr-FR)
- forms: reçois [Mood=Ind, Tense=Pres, Person=1, Number=Sing] /ʁə.ˈswa/; reçoit [Mood=Ind, Tense=Pres, Person=3, Number=Sing] /ʁə.ˈswa/; recevons [Mood=Ind, Tense=Pres, Person=1, Number=Plur] /ʁə.sə.ˈvɔ̃/; reçoivent [Mood=Ind, Tense=Pres, Person=3, Number=Plur] /ʁə.ˈswav/; reçu [VerbForm=Part, Tense=Past] /ʁə.ˈsy/; reçut [Mood=Ind, Tense=Past, Person=3, Number=Sing] /ʁə.ˈsy/; recevrai [Mood=Ind, Tense=Fut, Person=1, Number=Sing] /ʁə.sə.ˈvʁe/; reçoive [Mood=Sub, Tense=Pres, Person=1, Number=Sing] /ʁə.ˈswav/
  1. [A1] obtenir ce qu'une personne donne, envoie ou attribue
     - example: Chaque année, je reçois une carte de ma tante pour mon anniversaire.
     - frames: recevoir qqch de qqn
     - collocations: recevoir une lettre; recevoir un message; recevoir un cadeau; recevoir un prix; recevoir des nouvelles; recevoir une réponse; recevoir des ordres; recevoir un salaire
     - grammar: valency Tr
  2. [A2] accueillir une personne qui vient chez soi ou dans son bureau
     - example: Ce soir, nous recevons quelques amis à dîner à la maison.
     - frames: recevoir qqn
     - collocations: recevoir des invités; recevoir à dîner; recevoir chez soi; recevoir sur rendez-vous; être bien reçu; recevoir un patient
     - grammar: valency Tr, mainly
  3. [B1] être touché ou frappé par quelque chose, souvent de désagréable
     - example: Pendant le match, le gardien a reçu le ballon en plein visage.
     - collocations: recevoir un coup; recevoir une gifle; recevoir une blessure; recevoir des critiques
     - grammar: valency Tr
  4. [B1] déclarer qu'un candidat a réussi un examen ou un concours
     - example: Ma sœur a été reçue au concours d'entrée de l'école.
     - frames: être reçu à qqch
     - collocations: être reçu à un examen; être reçu au concours; être reçu premier
     - grammar: valency Tr; note mostly in the passive: être reçu à
  5. [B2] capter les signaux d'une radio, d'une télévision ou d'un téléphone
     - example: Dans ce village de montagne, on reçoit mal les chaînes de télévision.
     - collocations: recevoir une chaîne; recevoir le signal
     - grammar: valency Tr

### 39. aimer

**Lexeme 1: aimer**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /ɛ.ˈme/ (fr-FR), /e.ˈme/ (fr-FR)
  1. [A1] éprouver une profonde affection ou un sentiment amoureux pour quelqu'un
     - example: Je t'aime depuis le premier jour de notre rencontre.
     - frames: aimer qqn
     - collocations: aimer passionnément; aimer tendrement; aimer à la folie; s'aimer
     - grammar: valency Tr; note aimer bien or aimer beaucoup said of a person means only to like them
     - opposites: détester, haïr
  2. [A1] trouver quelque chose agréable et y prendre plaisir
     - example: Nous aimons beaucoup nous promener au bord de la mer.
     - frames: aimer qqch; aimer + inf; aimer que + subj
     - collocations: aimer bien; aimer beaucoup; aimer mieux; aimer autant
     - grammar: valency Tr
     - opposites: détester
  3. [A2] désirer faire ou obtenir quelque chose, sans l'exiger
     - example: J'aimerais visiter ce musée avant la fin des vacances.
     - frames: aimer + inf; aimer que + subj; aimer qqch
     - grammar: valency Tr; note in this sense used in the conditional (j'aimerais)

### 40. prince

**Lexeme 1: prince**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈpʁɛ̃s/ (fr-FR)
  1. [A2] fils ou proche parent d'un roi ou d'une reine
     - example: Le jeune prince deviendra roi à la mort de son père.
     - collocations: prince héritier; prince charmant; le prince de Galles; prince consort
  2. [B1] homme qui gouverne seul un petit État ou un territoire
     - example: Le prince règne sur cette petite principauté depuis vingt ans.
     - collocations: prince régnant; le prince de Monaco
  3. [C1] personne considérée comme la meilleure dans son domaine
     - example: On le surnommait le prince des poètes de son époque.
     - frames: le prince de + nom pluriel
     - labels: figurative True, style literary

### 41. disposition

**Lexeme 1: disposition**, NOUN
- grammar: gender Fem
- said: /dis.po.zi.ˈsjɔ̃/ (fr-FR)
  1. [B1] possibilité d'utiliser librement une chose ou l'aide d'une personne
     - example: Une voiture de service est à votre disposition pendant le séjour.
     - frames: à la disposition de qqn; mettre qqch à la disposition de qqn
     - collocations: à disposition; se tenir à la disposition de; rester à la disposition de; mettre à disposition; avoir à sa disposition
     - grammar: countability Mass; number Sing
  2. [B2] mesure prise pour préparer une action ou faire face à une situation
     - example: Nous avons pris toutes les dispositions nécessaires pour votre arrivée.
     - collocations: prendre des dispositions; prendre ses dispositions; dispositions nécessaires
     - grammar: countability Count; number Plur, mainly
  3. [B2] manière dont les éléments d'un ensemble sont placés les uns par rapport aux autres
     - example: La disposition des meubles rend ce salon beaucoup plus agréable.
     - frames: disposition de qqch
     - collocations: disposition des lieux; disposition des pièces
     - grammar: countability Count
  4. [C1] partie d'un texte officiel qui fixe une règle précise
     - example: Ces nouvelles dispositions entreront en vigueur au début de l'année.
     - collocations: dispositions légales; disposition de la loi; dispositions transitoires; entrer en vigueur
     - labels: domain law
     - grammar: countability Count
  5. [C1] aptitude ou tendance naturelle d'une personne à faire quelque chose
     - example: Dès l'enfance, elle montrait de grandes dispositions pour la musique.
     - frames: dispositions pour qqch; dispositions pour + inf
     - collocations: avoir des dispositions
     - grammar: countability Count; number Plur, mainly
  6. [C1] état d'esprit d'une personne à un moment donné ou envers quelqu'un
     - example: Je te trouve dans de meilleures dispositions qu'hier soir.
     - frames: dispositions envers qqn; dispositions à l'égard de qqn
     - collocations: être dans de bonnes dispositions; être dans de mauvaises dispositions
     - grammar: countability Count; number Plur, mainly
- relations: derived_from disposer (VERB)

### 42. moteur

**Lexeme 1: moteur**, NOUN
- grammar: gender Masc; countability Count
- said: /mɔ.ˈtœʁ/ (fr-FR)
  1. [A2] machine qui transforme une énergie en mouvement pour faire marcher un véhicule ou un appareil
     - example: Le moteur de ma voiture fait un bruit étrange depuis hier.
     - collocations: démarrer le moteur; couper le moteur; le moteur tourne; moteur électrique; moteur diesel; moteur à essence; panne de moteur
  2. [B2] personne ou chose qui pousse à agir et fait avancer quelque chose
     - example: Les jeunes sont souvent le moteur du changement dans une société.
     - frames: le moteur de qqch
     - collocations: être le moteur de; moteur de la croissance
     - labels: figurative True
  3. [B1] programme qui fait le travail principal d'un logiciel ou d'un site
     - example: Ce moteur de recherche donne des résultats plus précis que les autres.
     - collocations: moteur de recherche; moteur de jeu
     - labels: domain computing
**Lexeme 2: moteur**, ADJ
- grammar: comparable False
- said: /mɔ.ˈtœʁ/ (fr-FR)
- forms: motrice [Gender=Fem, Number=Sing] /mɔ.ˈtʁis/; motrices [Gender=Fem, Number=Plur] /mɔ.ˈtʁis/
  1. [B2] qui produit un mouvement ou qui le transmet
     - example: Sur cette voiture, seules les roues avant sont motrices.
     - collocations: force motrice; roue motrice; quatre roues motrices
  2. [B2] qui concerne les mouvements que fait le corps
     - example: Après son accident, il garde un léger handicap moteur.
     - collocations: handicap moteur; troubles moteurs; nerf moteur
  3. [B2] qui pousse à agir et fait avancer un projet ou un groupe
     - example: Notre association joue un rôle moteur dans la vie du quartier.
     - collocations: jouer un rôle moteur; élément moteur
     - labels: figurative True

### 43. opposition

**Lexeme 1: opposition**, NOUN
- grammar: gender Fem; countability Count
- said: /ɔ.po.zi.ˈsjɔ̃/ (fr-FR)
  1. [B1] refus d'accepter quelque chose et volonté d'y faire obstacle
     - example: Le projet de nouvelle route rencontre une forte opposition des habitants.
     - frames: opposition à qqch; opposition de qqn
     - collocations: rencontrer une opposition; se heurter à une opposition; une opposition farouche; une forte opposition; sans opposition
     - opposites: soutien
  2. [B1] ensemble des partis et des personnes qui ne sont pas au pouvoir
     - example: Les députés de l'opposition ont voté contre la nouvelle loi.
     - collocations: dans l'opposition; le chef de l'opposition; les partis d'opposition; passer dans l'opposition
     - grammar: number Sing; note usually with the definite article: l'opposition
     - opposites: majorité
  3. [B2] grande différence entre deux choses que l'on compare
     - example: Ce roman repose sur l'opposition entre la ville et la campagne.
     - frames: opposition entre qqch et qqch; par opposition à qqch
     - collocations: en opposition avec
  4. [B2] démarche officielle pour bloquer un paiement ou contester une décision
     - example: J'ai perdu ma carte bancaire, je dois vite faire opposition.
     - frames: faire opposition à qqch
     - collocations: faire opposition; faire opposition à un chèque; faire opposition sur une carte
- relations: verbal_noun_of opposer (VERB)

### 44. réduction

**Lexeme 1: réduction**, NOUN
- grammar: gender Fem; countability Count
- said: /ʁe.dyk.ˈsjɔ̃/ (fr-FR)
  1. [B1] diminution de la quantité, de la taille ou de l'importance d'une chose
     - example: Le gouvernement promet une réduction importante des dépenses publiques l'an prochain.
     - frames: réduction de qqch
     - collocations: réduction des coûts; réduction des effectifs; réduction des impôts; réduction du temps de travail; réduction des dépenses; forte réduction
     - opposites: augmentation
  2. [A2] somme retirée du prix normal d'un produit ou d'un service
     - example: Avec ta carte d'étudiant, tu as droit à une réduction au cinéma.
     - frames: réduction sur qqch
     - collocations: faire une réduction; obtenir une réduction; bénéficier d'une réduction; accorder une réduction; avoir droit à une réduction; carte de réduction
  3. [C1] copie d'un objet ou d'une œuvre faite à une plus petite échelle
     - example: Sur son bureau trône une réduction en bronze de la célèbre statue.
     - collocations: en réduction
- relations: verbal_noun_of réduire (VERB)

### 45. cheveu

**Lexeme 1: cheveu**, NOUN
- grammar: gender Masc; countability Count; number Plur, mainly; note usually plural: les cheveux means the hair of the head as a whole
- said: /ʃə.ˈvø/ (fr-FR), /ˈʃvø/ (fr-FR, casual)
- forms: cheveux [Number=Plur] /ʃə.ˈvø/
  1. [A1] poil qui pousse sur la tête d'une personne
     - example: Je me lave les cheveux tous les deux jours avec un shampoing doux.
     - collocations: se laver les cheveux; se brosser les cheveux; se faire couper les cheveux; avoir les cheveux longs; cheveux courts; cheveux blancs; cheveux bouclés; un cheveu blanc
