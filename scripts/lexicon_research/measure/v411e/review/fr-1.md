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
  1. [A2] ensemble d'objets de même sorte réunis et gardés par goût ou pour leur valeur
     - example: Mon grand-père garde précieusement sa collection de timbres anciens.
     - frames: collection de qqch
     - collocations: faire collection de; une collection de timbres; une collection privée; une pièce de collection; une voiture de collection; toute une collection de
  2. [B2] série de livres publiés par un même éditeur sous un titre commun
     - example: Ce roman est paru dans une collection de poche bon marché.
     - collocations: une collection de poche; diriger une collection
  3. [B1] ensemble des vêtements ou objets créés par une marque pour une saison
     - example: La styliste présentera sa nouvelle collection d'hiver la semaine prochaine.
     - collocations: présenter une collection; la collection printemps-été; la collection automne-hiver; une nouvelle collection
- relations: confusable collecte (NOUN): collecte is the act of gathering money, donations or waste; collection is a set of objects kept together

### 3. accepter

**Lexeme 1: accepter**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /ak.sɛp.ˈte/ (fr-FR)
  1. [A2] recevoir volontiers ce qu'on vous offre ou ce qu'on vous propose
     - example: Je ne peux pas accepter un cadeau aussi généreux de ta part.
     - frames: accepter qqch; accepter qqch de qqn; accepter de + inf
     - collocations: accepter une invitation; accepter une offre; accepter une proposition; accepter un poste; accepter un cadeau; accepter avec plaisir; accepter volontiers
     - opposites: refuser
  2. [B1] admettre une chose comme juste, vraie ou inévitable, même sans l'aimer
     - example: Elle n'accepte pas que ses enfants rentrent après minuit.
     - frames: accepter qqch; accepter que + subj
     - collocations: accepter la défaite; accepter son sort; accepter la critique; accepter la réalité; accepter les conditions; mal accepter
     - opposites: refuser
  3. [B1] laisser une personne entrer dans un groupe ou y prendre sa place
     - example: Ce club accepte les nouveaux membres à partir de seize ans.
     - frames: accepter qqn dans qqch; accepter qqn comme + nom
     - collocations: être accepté
     - opposites: rejeter

### 4. propriété

**Lexeme 1: propriété**, NOUN
- grammar: gender Fem
- said: /pʁɔ.pʁi.je.ˈte/ (fr-FR)
  1. [B1] droit de posséder une chose et d'en faire ce que l'on veut
     - example: Ce terrain est la propriété de la commune depuis longtemps.
     - frames: être la propriété de qqn
     - collocations: la propriété privée; titre de propriété; la propriété intellectuelle; accès à la propriété
     - grammar: countability Mass
  2. [B1] terrain avec une maison, qui appartient à quelqu'un
     - example: Mes grands-parents ont une grande propriété à la campagne.
     - collocations: acheter une propriété; une grande propriété; une propriété à la campagne; une propriété viticole
     - grammar: countability Count
  3. [B2] qualité particulière qui distingue une chose ou une matière des autres
     - example: On utilise cette plante pour ses propriétés médicinales.
     - frames: propriété de qqch; avoir la propriété de + inf
     - collocations: propriétés physiques; propriétés chimiques; propriétés médicinales
     - grammar: countability Count
- relations: confusable propreté (NOUN): propreté means cleanliness; both are built on propre and look alike

### 5. papier

**Lexeme 1: papier**, NOUN
- grammar: gender Masc; countability Both
- said: /pa.ˈpje/ (fr-FR)
  1. [A1] matière fine faite de fibres végétales, sur laquelle on écrit, imprime ou emballe
     - example: Pour ce bricolage, il nous faut du papier et des ciseaux.
     - collocations: une feuille de papier; un morceau de papier; papier recyclé; papier à lettres; papier toilette; papier journal; sur papier; papier d'aluminium
     - grammar: countability Mass
  2. [A2] feuille écrite ou imprimée, souvent un document officiel ou administratif
     - example: Tu dois encore signer ce papier avant de partir.
     - collocations: remplir des papiers; signer un papier; papiers administratifs
     - grammar: countability Count
  3. [B1] documents officiels qui prouvent qui est une personne
     - example: Le policier m'a demandé de présenter mes papiers à la frontière.
     - collocations: papiers d'identité; montrer ses papiers; contrôle des papiers; avoir ses papiers en règle; sans papiers
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
  1. [B1] chacune des parties distinctes d'un texte, d'un trajet ou d'un ensemble
     - example: Vous trouverez les réponses dans la dernière section du manuel.
     - frames: section de qqch
     - collocations: la section suivante; section d'autoroute; diviser en sections
  2. [B1] groupe de personnes formant une partie d'une organisation, d'un parti ou d'une école
     - example: Ma fille est inscrite en section bilingue depuis la rentrée.
     - collocations: section syndicale; section locale; section sportive; section bilingue; section européenne
  3. [C1] coupure nette d'une chose, ou surface qu'on obtient en la coupant
     - example: Ce câble électrique a une section de deux millimètres carrés.
     - frames: section de qqch
     - collocations: section transversale; en section
  4. [C1] petite unité de soldats placée sous les ordres d'un officier ou d'un sous-officier
     - example: Le lieutenant rassemble sa section avant le départ en patrouille.
     - collocations: chef de section
     - labels: domain military

### 7. espoir

**Lexeme 1: espoir**, NOUN
- grammar: gender Masc
- said: /ɛs.ˈpwaʁ/ (fr-FR)
  1. [B1] fait d'attendre avec confiance qu'une chose désirée arrive
     - example: Malgré la crise, nous gardons l'espoir de retrouver bientôt du travail.
     - frames: l'espoir de + inf; l'espoir que + ind; dans l'espoir de + inf; dans l'espoir que + ind
     - collocations: garder espoir; perdre espoir; reprendre espoir; avoir bon espoir; redonner espoir; nourrir l'espoir; plein d'espoir; sans espoir; un faible espoir; un immense espoir
     - grammar: countability Mass, mainly
     - opposites: désespoir
  2. [B1] chose ou possibilité sur laquelle on compte pour obtenir ce qu'on veut
     - example: Ce nouveau traitement est notre dernier espoir pour le guérir.
     - collocations: le dernier espoir; le seul espoir; il reste un espoir; un espoir de victoire
     - grammar: countability Count
  3. [B2] jeune personne dont on attend de grands succès dans son domaine
     - example: À seize ans, elle est déjà considérée comme un espoir du tennis.
     - frames: un espoir de qqch
     - collocations: un jeune espoir; un grand espoir
     - grammar: countability Count
- relations: derived_from espérer (VERB); confusable espérance (NOUN): espérance is a more literary or abstract word for hope, and is also used in fixed phrases such as espérance de vie

### 8. approche

**Lexeme 1: approche**, NOUN
- grammar: gender Fem; countability Count
- said: /a.ˈpʁɔʃ/ (fr-FR)
  1. [B1] manière de considérer une question ou de traiter un problème
     - example: Notre équipe propose une approche totalement différente de ce problème.
     - frames: approche de qqch
     - collocations: une nouvelle approche; une approche globale; une approche différente; adopter une approche; changer d'approche
  2. [B1] fait de venir plus près, dans l'espace ou dans le temps
     - example: À l'approche de l'hiver, les oiseaux partent vers le sud.
     - frames: à l'approche de qqch; l'approche de qqn/qqch
     - collocations: à l'approche de; l'approche de l'hiver; l'approche des vacances; entendre l'approche de
  3. [C1] espaces situés tout près d'un lieu, par lesquels on y arrive
     - example: Les embouteillages commencent dès les approches de la ville.
     - collocations: aux approches de
     - labels: style literary
     - grammar: number Plur
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
  1. [A2] souvent et à des moments qui reviennent de façon habituelle
     - example: Je fais régulièrement du sport avec mes collègues après le travail.
     - collocations: très régulièrement; assez régulièrement; se voir régulièrement; vérifier régulièrement; mettre à jour régulièrement
     - opposites: irrégulièrement
  2. [B2] de façon égale et uniforme, sans changement ni écart
     - example: Étalez la pâte bien régulièrement sur toute la surface du moule.
     - collocations: respirer régulièrement; répartir régulièrement; augmenter régulièrement
     - opposites: irrégulièrement
  3. [C1] en respectant la loi et les procédures prévues
     - example: Le nouveau maire a été régulièrement élu par le conseil municipal.
     - collocations: régulièrement élu; régulièrement inscrit; régulièrement convoqué
     - labels: style official
     - opposites: irrégulièrement
- relations: adverb_of régulier (ADJ)

### 11. surprise

**Lexeme 1: surprise**, NOUN
- grammar: gender Fem; note often placed after a noun as an invariable modifier: une visite surprise
- said: /syʁ.ˈpʁiz/ (fr-FR)
  1. [A2] événement ou fait inattendu qui étonne
     - example: Quelle bonne surprise de te voir ici ce soir !
     - frames: à la surprise de qqn
     - collocations: à la surprise générale; créer la surprise; par surprise; sans surprise; une mauvaise surprise; une bonne surprise; avoir une surprise; visite surprise
     - grammar: countability Count
  2. [B1] sentiment d'étonnement devant une chose que l'on n'attendait pas
     - example: À ma grande surprise, le magasin était encore ouvert.
     - collocations: à ma grande surprise; un cri de surprise; revenir de sa surprise; feindre la surprise
     - grammar: countability Mass
  3. [A2] cadeau ou plaisir inattendu préparé en secret pour quelqu'un
     - example: Nous préparons une surprise pour l'anniversaire de notre mère.
     - frames: faire une surprise à qqn
     - collocations: faire une surprise; préparer une surprise; fête surprise; pochette surprise
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
  1. [A2] qui surprend parce qu'il est différent de ce qui est habituel
     - example: J'entends un bruit bizarre dans la cuisine depuis ce matin.
     - frames: c'est bizarre que + subj; trouver qqch bizarre; c'est bizarre de + inf
     - collocations: trouver ça bizarre; avoir l'air bizarre; un bruit bizarre; une odeur bizarre; un type bizarre; un comportement bizarre; de plus en plus bizarre
     - opposites: normal
  2. [B1] qui ne se sent pas tout à fait bien, dans son corps ou son esprit
     - example: Depuis que j'ai pris ce médicament, je me sens un peu bizarre.
     - collocations: se sentir bizarre; être tout bizarre
     - labels: formality informal

### 14. rencontrer

**Lexeme 1: rencontrer**, VERB
- grammar: conjugation 1
- said: /ʁɑ̃.kɔ̃.ˈtʁe/ (fr-FR)
  1. [A2] se trouver par hasard en présence de quelqu'un ou de quelque chose
     - example: Ce matin, j'ai rencontré notre ancien professeur au marché.
     - frames: rencontrer qqn
     - collocations: rencontrer par hasard
     - grammar: auxiliary have; valency Tr
  2. [A2] voir une personne pour la première fois et faire sa connaissance
     - example: C'est pendant leurs études qu'ils se sont rencontrés.
     - frames: rencontrer qqn
     - collocations: rencontrer l'âme sœur
     - grammar: auxiliary have; valency Tr
  3. [B1] avoir un entretien prévu avec quelqu'un pour discuter d'un sujet
     - example: Le ministre rencontrera les représentants des syndicats demain.
     - frames: rencontrer qqn
     - grammar: auxiliary have; valency Tr
  4. [B1] trouver sur son chemin une difficulté, un obstacle ou une réaction
     - example: Nous rencontrons quelques problèmes avec le nouveau logiciel.
     - frames: rencontrer qqch
     - collocations: rencontrer des difficultés; rencontrer un problème; rencontrer un obstacle; rencontrer un succès; rencontrer une résistance
     - grammar: auxiliary have; valency Tr
  5. [B2] jouer contre un adversaire dans un match ou une compétition
     - example: En demi-finale, notre équipe rencontrera le champion en titre.
     - frames: rencontrer qqn
     - grammar: auxiliary have; valency Tr
  6. [B2] exister, pouvoir être trouvé quelque part
     - example: Ce genre d'erreur se rencontre souvent chez les débutants.
     - frames: se rencontrer
     - grammar: valency Refl; reflexive True; auxiliary be; note the pronoun does not mean oneself; 'is found', 'occurs'
  7. [B2] se rejoindre en un même point, en parlant de lignes, routes ou cours d'eau
     - example: Les deux rivières se rencontrent juste en dessous du village.
     - frames: se rencontrer
     - grammar: valency Refl; reflexive True; auxiliary be

### 15. dormir

**Lexeme 1: dormir**, VERB
- grammar: conjugation 3; irregular True; valency Intr; auxiliary have
- said: /dɔʁ.ˈmiʁ/ (fr-FR)
- forms: dors [Mood=Ind, Tense=Pres, Person=1,2, Number=Sing, VerbForm=Fin] /ˈdɔʁ/; dort [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈdɔʁ/; dormons [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /dɔʁ.ˈmɔ̃/; dorment [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈdɔʁm/; dormi [Tense=Past, VerbForm=Part] /dɔʁ.ˈmi/; dormit [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /dɔʁ.ˈmi/; dormirai [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] /dɔʁ.mi.ˈʁe/; dorme [Mood=Sub, Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin] /ˈdɔʁm/
  1. [A1] être dans l'état de repos où les yeux sont fermés et l'esprit inconscient
     - example: Ce soir, je dors chez ma sœur parce que mon train part tôt.
     - collocations: dormir profondément; bien dormir; mal dormir; dormir comme un loir; dormir à poings fermés; aller dormir; dormir tard; dormir à l'hôtel
  2. [B2] rester sans activité, sans mouvement ou sans être utilisé
     - example: Tout cet argent dort sur un compte depuis des années.
     - collocations: laisser dormir un dossier; la ville dort
     - labels: figurative True
  3. [B1] manquer d'attention, ne pas réagir assez vite
     - example: Réveille-toi un peu, tu dors pendant toute la réunion !
     - labels: formality informal, figurative True
- relations: confusable endormir (VERB): s'endormir means to fall asleep; dormir means to be asleep

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
- forms: sens [Person=1, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /ˈsɑ̃/; sent [Person=3, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /ˈsɑ̃/; sentons [Person=1, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /sɑ̃.ˈtɔ̃/; sentent [Person=3, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /ˈsɑ̃t/; senti [Tense=Past, VerbForm=Part] /sɑ̃.ˈti/; sentit [Person=3, Number=Sing, Tense=Past, Mood=Ind, VerbForm=Fin] /sɑ̃.ˈti/; sentirai [Person=1, Number=Sing, Tense=Fut, Mood=Ind, VerbForm=Fin] /sɑ̃.ti.ˈʁe/; sente [Person=1, Number=Sing, Tense=Pres, Mood=Sub, VerbForm=Fin] /ˈsɑ̃t/
  1. [A2] percevoir une odeur par le nez
     - example: En entrant dans la cuisine, je sens une odeur de brûlé.
     - frames: sentir qqch; sentir le parfum de qqn
     - collocations: sentir une odeur
     - grammar: auxiliary have; valency Tr
  2. [A2] répandre une certaine odeur, agréable ou désagréable
     - example: Ces roses sentent vraiment bon depuis qu'on les a coupées.
     - frames: sentir + adj; sentir qqch
     - collocations: sentir bon; sentir mauvais; sentir fort; sentir le brûlé; sentir le renfermé; sentir la transpiration
     - grammar: auxiliary have
  3. [A2] percevoir par le corps une impression physique, agréable ou pénible
     - example: Avec ce froid, je ne sens plus mes doigts.
     - frames: sentir qqch; sentir qqch + inf
     - collocations: sentir la douleur; sentir le froid; sentir la chaleur; se faire sentir
     - grammar: auxiliary have; valency Tr
  4. [B1] se rendre compte d'une chose ou la deviner sans preuve
     - example: Elle sentait que quelque chose n'allait pas chez son fils.
     - frames: sentir que + ind; sentir qqch
     - collocations: sentir le danger; sentir venir le coup
     - grammar: auxiliary have; valency Tr
  5. [A2] avoir une certaine impression de son propre état physique ou moral
     - example: Depuis que je fais du sport, je me sens beaucoup mieux.
     - frames: se sentir + adj; se sentir + inf
     - collocations: se sentir bien; se sentir mal; se sentir mieux; se sentir seul; se sentir à l'aise; se sentir chez soi
     - grammar: valency Refl; reflexive True; auxiliary be
  6. [B2] laisser deviner, annoncer une chose qui va arriver
     - example: Tous ces cartons dans l'entrée, ça sent le déménagement.
     - frames: ça sent qqch
     - collocations: ça sent le roussi; sentir le piège
     - labels: formality informal, figurative True
     - grammar: auxiliary have; valency Tr; note usually with ça as subject
  7. [B2] supporter une personne, l'accepter près de soi
     - example: Mon frère ne peut pas sentir son nouveau collègue.
     - frames: ne pas pouvoir sentir qqn
     - labels: formality informal
     - grammar: auxiliary have; valency Tr; note only in the negative: ne pas pouvoir sentir qqn

### 18. tenter

**Lexeme 1: tenter**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /tɑ̃.ˈte/ (fr-FR)
  1. [B1] faire des efforts pour réussir quelque chose, sans être sûr du résultat
     - example: Nous allons tenter de rejoindre le refuge avant la nuit.
     - frames: tenter qqch; tenter de + inf
     - collocations: tenter sa chance; tenter le coup; tenter l'impossible; tenter une expérience; tenter vainement
  2. [B1] donner envie à quelqu'un, attirer par ce qui plaît
     - example: Ce petit voyage au bord de la mer me tente beaucoup.
     - frames: tenter qqn; être tenté de + inf; ça me tente de + inf
     - collocations: se laisser tenter; être tenté; tenter le diable
  3. [C1] pousser quelqu'un à mal agir ou à désobéir à Dieu
     - example: Selon le récit, le serpent tente la femme dans le jardin.
     - frames: tenter qqn
     - labels: domain religion

### 19. valoir

**Lexeme 1: valoir**, VERB
- grammar: conjugation 3; irregular True; valency Both
- said: /va.ˈlwaʁ/ (fr-FR)
- forms: vaux [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈvo/; vaut [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈvo/; valons [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /va.ˈlɔ̃/; valent [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈval/; valu [Tense=Past, VerbForm=Part] /va.ˈly/; valut [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /va.ˈly/; vaudrai [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] /vo.ˈdʁe/; vaille [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈvaj/
  1. [A2] avoir un certain prix, coûter une certaine somme d'argent
     - example: Ce vieux tableau vaut aujourd'hui plusieurs milliers d'euros.
     - frames: valoir qqch
     - collocations: valoir cher; valoir une fortune; valoir de l'or
     - grammar: auxiliary have; valency Tr
  2. [B1] avoir un certain mérite, un certain intérêt ou une certaine qualité
     - example: Franchement, ce restaurant ne vaut pas grand-chose.
     - collocations: ne rien valoir; ne pas valoir grand-chose
     - grammar: auxiliary have; valency Tr
  3. [A2] être préférable, être plus raisonnable
     - example: Il vaut mieux partir tôt pour éviter les bouchons.
     - frames: il vaut mieux + inf; il vaut mieux que + subj; mieux vaut + inf
     - collocations: il vaut mieux; mieux vaut tard que jamais
     - grammar: auxiliary have; valency Impers; note always with mieux: il vaut mieux, mieux vaut
  4. [B1] mériter l'effort, le temps ou l'attention qu'on y consacre
     - example: Ce musée vaut vraiment le détour si tu passes par là.
     - frames: valoir la peine de + inf; valoir la peine que + subj
     - collocations: valoir la peine; valoir le coup; valoir le détour
     - grammar: auxiliary have; valency Tr
  5. [B2] faire obtenir à quelqu'un une chose, bonne ou mauvaise
     - example: Ses efforts lui ont valu les félicitations du directeur.
     - frames: valoir qqch à qqn
     - collocations: valoir des ennuis; valoir des reproches
     - grammar: auxiliary have; valency Tr
  6. [B1] être égal à une certaine quantité, représenter la même chose
     - example: Dans ce jeu, chaque carte rouge vaut deux points.
     - frames: valoir qqch
     - grammar: auxiliary have; valency Tr
  7. [B2] s'appliquer à quelqu'un ou à quelque chose, le concerner
     - example: Ce que je dis là vaut aussi pour toi.
     - frames: valoir pour qqn/qqch
     - grammar: auxiliary have; valency Intr
  8. [B2] avoir le même mérite, être de qualité semblable
     - example: Ces deux solutions se valent, choisis celle que tu préfères.
     - collocations: ça se vaut
     - grammar: valency Refl; auxiliary be; note the pronoun means each other; subject is plural

### 20. améliorer

**Lexeme 1: améliorer**, VERB
- grammar: conjugation 1
- said: /a.me.ljɔ.ˈʁe/ (fr-FR)
  1. [A2] faire progresser qqch, le rendre plus satisfaisant ou de plus grande qualité
     - example: Nous cherchons des moyens d'améliorer l'accueil de nos clients.
     - frames: améliorer qqch
     - collocations: améliorer la qualité; améliorer la situation; améliorer les conditions de vie; améliorer ses résultats; améliorer son record; nettement améliorer; améliorer son français
     - grammar: auxiliary have; valency Tr
     - opposites: détériorer
  2. [A2] devenir plus satisfaisant, progresser en qualité ou en valeur
     - example: Depuis le printemps, sa santé s'améliore de semaine en semaine.
     - collocations: la situation s'améliore; le temps s'améliore; s'améliorer nettement; s'améliorer progressivement
     - grammar: valency Refl; reflexive True; auxiliary be
     - opposites: détériorer

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
  1. [A2] qui hors du commun par sa qualité, sa force ou sa taille
     - example: Depuis la terrasse, on a une vue incroyable sur toute la baie.
     - collocations: une chance incroyable; un talent incroyable; une vitesse incroyable; une vue incroyable; vraiment incroyable
  2. [A2] qu'on a beaucoup de mal à croire, qui semble impossible
     - example: Il est incroyable qu'il ait réussi sans jamais ouvrir un livre.
     - frames: il est incroyable que + subj; c'est incroyable de + inf
     - collocations: une histoire incroyable; c'est incroyable
     - opposites: croyable
  3. [B2] dont le comportement étonne et agace à la fois
     - example: Tu es vraiment incroyable, tu as encore oublié tes clés !
     - labels: formality informal
     - grammar: position Pred, mainly; note said of a person
- relations: derived_from croyable (ADJ)

### 23. mourir

**Lexeme 1: mourir**, VERB
- grammar: conjugation 3; irregular True; auxiliary be
- said: /mu.ˈʁiʁ/ (fr-FR)
- forms: meurs [Mood=Ind, Tense=Pres, Person=1, Number=Sing] /ˈmœʁ/; meurt [Mood=Ind, Tense=Pres, Person=3, Number=Sing] /ˈmœʁ/; mourons [Mood=Ind, Tense=Pres, Person=1, Number=Plur] /mu.ˈʁɔ̃/; meurent [Mood=Ind, Tense=Pres, Person=3, Number=Plur] /ˈmœʁ/; mort [VerbForm=Part, Tense=Past] /ˈmɔʁ/; mourut [Mood=Ind, Tense=Past, Person=3, Number=Sing] /mu.ˈʁy/; mourrai [Mood=Ind, Tense=Fut, Person=1, Number=Sing] /muʁ.ˈʁe/; meure [Mood=Sub, Tense=Pres, Person=1, Number=Sing] /ˈmœʁ/
  1. [A2] cesser de vivre
     - example: Son grand-père est mort l'hiver dernier, à quatre-vingt-dix ans.
     - frames: mourir de qqch
     - collocations: mourir de vieillesse; mourir sur le coup; mourir dans son sommeil; mourir jeune; mourir d'une maladie; mourir de faim; mourir au combat
     - grammar: valency Intr
  2. [B1] ressentir très fortement une sensation ou un sentiment
     - example: Ouvre la fenêtre, je meurs de chaud dans ce bureau.
     - frames: mourir de qqch; mourir d'envie de + inf
     - collocations: mourir de rire; mourir de peur; mourir d'ennui; mourir de faim; mourir de froid; mourir de honte
     - labels: figurative True
     - grammar: valency Intr
  3. [B2] s'affaiblir peu à peu jusqu'à disparaître complètement
     - example: Dans nos campagnes, les vieilles traditions meurent les unes après les autres.
     - labels: figurative True
     - grammar: valency Intr
  4. [C1] être en train de perdre la vie, peu à peu
     - example: Le vieil homme se mourait doucement dans sa chambre.
     - labels: style literary
     - grammar: valency Refl; reflexive True; auxiliary be; note se mourir; used only in simple tenses, mostly the imperfect

### 24. tiers

**Lexeme 1: tiers**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈtjɛʁ/ (fr-FR)
  1. [A2] chacune des trois parties égales d'un tout
     - example: Un tiers des élèves habitent loin de l'école.
     - frames: le tiers de qqch; un tiers de + nom
     - collocations: les deux tiers; plus d'un tiers; réduire d'un tiers; aux deux tiers
  2. [B2] personne qui ne participe pas à une affaire entre d'autres personnes
     - example: Ne racontez pas nos projets à des tiers sans mon accord.
     - collocations: assurance au tiers; être assuré au tiers; un tiers de confiance; à l'égard des tiers
**Lexeme 2: tiers**, ADJ
- grammar: comparable False; position Attr
- said: /ˈtjɛʁ/ (fr-FR)
- forms: tierce [Gender=Fem, Number=Sing] /ˈtjɛʁs/; tierces [Gender=Fem, Number=Plur] /ˈtjɛʁs/
  1. [B2] qui vient de l'extérieur, en dehors des personnes ou groupes concernés
     - example: Nous avons fait appel à une tierce personne pour régler le conflit.
     - collocations: une tierce personne; un pays tiers; une tierce partie

### 25. docteur

**Lexeme 1: docteur**, NOUN
- grammar: gender Masc; countability Count; note also used of a woman; the feminine docteure is common in Canada and spreading elsewhere
- said: /dɔk.ˈtœʁ/ (fr-FR)
  1. [A1] personne qui a fait des études de médecine et soigne les malades
     - example: Si ta fièvre ne baisse pas, il faut appeler le docteur.
     - collocations: aller chez le docteur; appeler le docteur; consulter un docteur; bonjour docteur
  2. [B2] personne qui a obtenu le plus haut diplôme d'une université
     - example: Ma sœur est devenue docteur en chimie après quatre ans de recherches.
     - frames: docteur en + nom
     - collocations: docteur en médecine; docteur en droit; docteur ès lettres; titre de docteur

### 26. quatrième

**Lexeme 1: quatrième**, ADJ
- grammar: comparable False
- said: /ka.tʁi.ˈjɛm/ (fr-FR)
  1. [A1] qui vient juste après le troisième dans une suite ou un classement
     - example: Notre bureau se trouve au quatrième étage, à gauche de l'ascenseur.
     - collocations: arriver quatrième; la quatrième place; en quatrième position; le quatrième étage; pour la quatrième fois
- relations: derived_from quatre (NUM)
**Lexeme 2: quatrième**, NOUN
- grammar: gender Masc; countability Count
- said: /ka.tʁi.ˈjɛm/ (fr-FR)
  1. [A2] étage d'un immeuble situé juste au-dessus du troisième
     - example: Nous habitons au quatrième, mais il n'y a pas d'ascenseur.
     - collocations: au quatrième; habiter au quatrième; monter au quatrième
  2. [B1] arrondissement d'une grande ville qui porte ce numéro
     - example: Ils ont trouvé un petit appartement dans le quatrième.
     - collocations: dans le quatrième
- relations: derived_from quatrième (ADJ)
**Lexeme 3: quatrième**, NOUN
- grammar: gender Fem; countability Count
- said: /ka.tʁi.ˈjɛm/ (fr-FR)
  1. [A2] classe du collège où entrent les élèves d'environ treize ans
     - example: Ma fille entre en quatrième à la rentrée prochaine.
     - collocations: en quatrième; entrer en quatrième; redoubler sa quatrième; une classe de quatrième; un élève de quatrième
  2. [B1] rapport de vitesse d'un véhicule qui vient après la troisième
     - example: Sur cette route droite, tu peux passer la quatrième sans crainte.
     - collocations: passer la quatrième; rouler en quatrième
  3. [C1] dernière page extérieure d'un livre, avec un texte qui le présente
     - example: Avant d'acheter un roman, je lis toujours la quatrième.
- relations: derived_from quatrième (ADJ)

### 27. député

**Lexeme 1: député**, NOUN
- grammar: gender Masc; countability Count
- said: /de.py.ˈte/ (fr-FR)
- forms: députée [Gender=Fem, Number=Sing] /de.py.ˈte/; députées [Gender=Fem, Number=Plur] /de.py.ˈte/
  1. [B1] personne élue pour représenter les citoyens dans une assemblée qui vote les lois
     - example: Notre député défend ce projet de loi à l'Assemblée depuis des mois.
     - collocations: élire un député; être élu député; député européen; député de la majorité; député de l'opposition; siéger comme député
  2. [C1] personne envoyée par un groupe pour le représenter et parler en son nom
     - example: La ville choisit deux députés pour porter ses plaintes au roi.
     - frames: député de qqch auprès de qqn
     - labels: style literary
- relations: derived_from députer (VERB)

### 28. européen

**Lexeme 1: européen**, ADJ
- grammar: comparable False
- said: /ø.ʁɔ.pe.ˈɛ̃/ (fr-FR)
- forms: européenne [Gender=Fem, Number=Sing] /ø.ʁɔ.pe.ˈɛn/; européennes [Gender=Fem, Number=Plur] /ø.ʁɔ.pe.ˈɛn/
  1. [A2] qui appartient à l'Europe, s'y trouve ou en vient
     - example: Plusieurs capitales européennes sont reliées par des trains rapides.
     - collocations: pays européens; continent européen; à l'échelle européenne; niveau européen
  2. [B1] qui concerne l'union politique et économique de nombreux pays d'Europe
     - example: Le Parlement européen doit voter cette nouvelle loi la semaine prochaine.
     - collocations: Union européenne; Parlement européen; Commission européenne; élections européennes; construction européenne
- relations: adjective_of Europe (PROPN)
**Lexeme 2: Européen**, NOUN
- grammar: gender Masc; countability Count; note written with a capital letter; feminine Européenne
- said: /ø.ʁɔ.pe.ˈɛ̃/ (fr-FR)
- forms: Européenne [Gender=Fem, Number=Sing] /ø.ʁɔ.pe.ˈɛn/
  1. [A2] personne qui habite l'Europe ou qui en est originaire
     - example: Beaucoup d'Européens passent leurs vacances au bord de la mer.
- relations: derived_from européen (ADJ)

### 29. verre

**Lexeme 1: verre**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈvɛʁ/ (fr-FR)
  1. [A1] petit récipient sans anse dans lequel on boit
     - example: Peux-tu mettre deux verres sur la table, s'il te plaît ?
     - frames: verre à + nom
     - collocations: un verre à vin; un verre à pied; remplir un verre; vider son verre; lever son verre; un verre en plastique
     - grammar: countability Count
  2. [A2] matière dure, transparente et fragile, fabriquée surtout avec du sable
     - example: Je préfère ranger la confiture dans des pots en verre.
     - collocations: en verre; du verre brisé; des éclats de verre; verre trempé; souffler le verre; bouteille en verre
     - grammar: countability Mass
  3. [A2] quantité de boisson que contient ce récipient, souvent bue avec d'autres
     - example: Après le travail, on va boire un verre en terrasse ?
     - frames: verre de + nom; offrir un verre à qqn
     - collocations: boire un verre; prendre un verre; payer un verre; un verre de trop; un verre d'eau
     - grammar: countability Count
  4. [B1] pièce transparente taillée pour corriger ou protéger la vue
     - example: Mes nouveaux verres me permettent enfin de lire sans fatigue.
     - collocations: verres de contact; verres progressifs; verres teintés; porter des verres
     - grammar: countability Count; number Plur, mainly
- relations: confusable vert (ADJ): sounds the same as vert (green), ver (worm) and vers (towards); confusable ver (NOUN): homophone: ver means worm

### 30. théorie

**Lexeme 1: théorie**, NOUN
- grammar: gender Fem
- said: /te.ɔ.ˈʁi/ (fr-FR)
  1. [B1] ensemble organisé d'idées et de lois qui explique un groupe de faits
     - example: Cette théorie explique pourquoi les continents se déplacent lentement.
     - frames: théorie de qqch; théorie sur qqch
     - collocations: élaborer une théorie; formuler une théorie; vérifier une théorie; réfuter une théorie; une théorie scientifique; la théorie de l'évolution; la théorie de la relativité
     - grammar: countability Count
  2. [B1] connaissance abstraite des principes, opposée à l'application concrète
     - example: En théorie, les travaux doivent se terminer avant la fin du mois.
     - collocations: en théorie; la théorie et la pratique; passer de la théorie à la pratique
     - grammar: countability Mass
     - opposites: pratique
  3. [B2] idée personnelle avancée pour expliquer un fait, sans preuve solide
     - example: J'ai ma petite théorie sur la disparition de nos clés.
     - frames: théorie sur qqch
     - collocations: avoir une théorie; avoir sa petite théorie; la théorie du complot
     - grammar: countability Count

### 31. concept

**Lexeme 1: concept**, NOUN
- grammar: gender Masc; countability Count
- said: /kɔ̃.ˈsɛpt/ (fr-FR)
  1. [B1] idée générale et abstraite que l'esprit se forme d'une chose
     - example: Les jeunes enfants ont souvent du mal à saisir le concept de temps.
     - frames: le concept de qqch
     - collocations: un concept clé; un concept abstrait; définir un concept; maîtriser un concept; élaborer un concept
  2. [B2] idée nouvelle sur laquelle repose un produit, un commerce ou un spectacle
     - example: Ce café a lancé un concept original : on paie le temps passé, pas les boissons.
     - collocations: un nouveau concept; un concept original; un concept innovant; lancer un concept

### 32. certainement

**Lexeme 1: certainement**, ADV
- said: /sɛʁ.tɛn.ˈmɑ̃/ (fr-FR)
  1. [A2] de façon sûre, sans qu'il y ait le moindre doute
     - example: C'est certainement le plus beau jour de toute ma vie.
  2. [B1] très probablement, sans qu'on en soit tout à fait sûr
     - example: Ils ont certainement raté leur train à cause de la grève.
  3. [B1] sert à répondre oui avec force, à accepter sans hésiter
     - example: Certainement, je vous apporterai les documents dès demain matin.
     - collocations: certainement pas; mais certainement
- relations: adverb_of certain (ADJ)

### 33. agir

**Lexeme 1: agir**, VERB
- grammar: conjugation 2
- said: /a.ˈʒiʁ/ (fr-FR)
  1. [B1] faire quelque chose pour obtenir un résultat ou changer une situation
     - example: Si nous voulons sauver la forêt, il faut agir maintenant.
     - frames: agir pour + inf; agir au nom de qqn; agir pour le compte de qqn
     - collocations: il est temps d'agir; agir vite; agir à temps; agir seul; agir ensemble
     - grammar: auxiliary have; valency Intr
  2. [B1] se conduire d'une certaine manière dans une situation donnée
     - example: Tu as agi avec beaucoup de prudence dans cette affaire.
     - frames: agir en + nom; agir comme + nom; agir avec + nom
     - collocations: agir en ami; agir de bonne foi; agir à la légère; agir avec prudence; agir en connaissance de cause
     - grammar: auxiliary have; valency Intr
  3. [B2] produire un effet sur une personne, un corps ou une chose
     - example: Ce médicament agit en moins d'une heure sur la douleur.
     - frames: agir sur qqn/qqch
     - collocations: agir rapidement; agir efficacement
     - grammar: auxiliary have; valency Intr
  4. [B1] être question de quelqu'un ou de quelque chose, avoir pour sujet
     - example: Dans ce roman, il s'agit d'une famille qui quitte son pays.
     - frames: il s'agit de qqch; il s'agit de qqn
     - collocations: de quoi s'agit-il; s'il s'agit de
     - grammar: valency Impers; reflexive True; auxiliary be; note only as il s'agit de; the pronoun has no meaning of its own
  5. [B2] être nécessaire, falloir faire quelque chose
     - example: Maintenant, il s'agit de trouver une solution avant ce soir.
     - frames: il s'agit de + inf; il s'agit que + subj
     - grammar: valency Impers; reflexive True; auxiliary be; note only as il s'agit de; il s'agit que + subj is formal

### 34. processus

**Lexeme 1: processus**, NOUN
- grammar: gender Masc; countability Count
- said: /pʁɔ.sɛ.ˈsys/ (fr-FR), /pʁɔ.se.ˈsys/ (fr-FR)
  1. [B1] suite ordonnée d'étapes ou d'opérations qui mène à un résultat
     - example: Le processus de recrutement prend généralement plusieurs semaines dans cette entreprise.
     - frames: processus de + nom
     - collocations: engager un processus; lancer un processus; enclencher un processus; processus de paix; processus de décision; processus de fabrication; un long processus
  2. [B2] évolution progressive d'un phénomène qui se fait en plusieurs phases
     - example: Le vieillissement de la peau est un processus naturel et lent.
     - frames: processus de + nom
     - collocations: processus naturel; processus de vieillissement; un processus irréversible; processus inflammatoire
  3. [C1] programme ou tâche en train de s'exécuter sur un ordinateur
     - example: Ferme ce processus, il utilise presque toute la mémoire de l'ordinateur.
     - labels: domain computing
- relations: confusable procédure (NOUN): procédure is a set method or formal rules to follow; processus is the sequence of steps or changes itself

### 35. perte

**Lexeme 1: perte**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈpɛʁt/ (fr-FR)
  1. [B1] disparition d'une chose qu'on avait ou d'une capacité qu'on possédait
     - example: Après sa chute, elle a souffert d'une courte perte de mémoire.
     - frames: perte de qqch
     - collocations: perte de connaissance; perte de mémoire; perte de poids; perte d'emploi; perte de contrôle; perte de vitesse
  2. [B1] diminution de l'argent ou des biens d'une personne ou d'une entreprise
     - example: Cette année, la société a enregistré une perte de deux millions d'euros.
     - collocations: vendre à perte; subir des pertes; enregistrer des pertes; être en perte; pertes et profits
     - opposites: gain, bénéfice
  3. [B1] utilisation inutile d'une chose qu'on aurait pu mieux employer
     - example: Franchement, cette réunion était une perte de temps pour tout le monde.
     - frames: perte de qqch
     - collocations: une perte de temps; une perte d'énergie; une perte d'argent
     - grammar: number Sing, mainly
  4. [B2] mort d'une personne chère, vécue comme une privation par ses proches
     - example: Toute la famille a encore du mal à surmonter la perte du grand-père.
     - frames: perte de qqn
     - collocations: la perte d'un être cher; la perte d'un proche; une perte cruelle
  5. [B2] personnes tuées ou blessées lors d'un combat ou d'une catastrophe
     - example: Les deux armées ont subi de lourdes pertes pendant la bataille.
     - collocations: pertes humaines; de lourdes pertes; subir des pertes
     - grammar: number Plur
  6. [C1] ruine complète d'une personne, sur le plan matériel ou moral
     - example: Son orgueil démesuré le conduira tôt ou tard à sa perte.
     - collocations: courir à sa perte; aller à sa perte; causer la perte; jurer la perte
     - labels: style literary
     - grammar: number Sing
- relations: verbal_noun_of perdre (VERB)

### 36. préparer

**Lexeme 1: préparer**, VERB
- grammar: conjugation 1
- said: /pʁe.pa.ˈʁe/ (fr-FR)
  1. [A1] mettre une chose en état de servir ou d'être utilisée
     - example: Je prépare la chambre d'amis avant l'arrivée de mes parents.
     - frames: préparer qqch à qqn; préparer qqch pour qqn
     - collocations: préparer le repas; préparer le dîner; préparer un café; préparer sa valise; préparer ses bagages
     - grammar: auxiliary have; valency Tr
  2. [A2] organiser à l'avance tout ce qu'il faut pour qu'une chose réussisse
     - example: Toute l'équipe prépare activement la fête de fin d'année.
     - collocations: préparer un voyage; préparer une surprise; préparer un discours; préparer le terrain; préparer soigneusement
     - grammar: auxiliary have; valency Tr
  3. [A2] travailler et étudier pour réussir un examen ou un concours
     - example: Ma sœur prépare le concours d'entrée dans une grande école.
     - collocations: préparer un examen; préparer le bac; préparer un concours
     - grammar: auxiliary have; valency Tr
  4. [B1] rendre quelqu'un capable de faire face à une situation
     - example: Rien ne nous avait préparés à une épreuve aussi difficile.
     - frames: préparer qqn à qqch; préparer qqn à + inf
     - collocations: préparer les esprits
     - grammar: auxiliary have; valency Tr
  5. [A2] se rendre prêt à sortir, à partir ou à agir
     - example: Dépêche-toi de te préparer, le taxi arrive dans dix minutes.
     - frames: se préparer à + inf; se préparer à qqch; se préparer pour qqch
     - collocations: se préparer au pire
     - grammar: valency Refl; reflexive True; auxiliary be
  6. [B1] commencer à se former, être sur le point d'arriver
     - example: Regarde ces gros nuages noirs, un orage se prépare.
     - collocations: un orage se prépare
     - grammar: valency Refl; reflexive True; auxiliary be; note with a thing or an event as subject

### 37. domicile

**Lexeme 1: domicile**, NOUN
- grammar: gender Masc; countability Count
- said: /dɔ.mi.ˈsil/ (fr-FR)
  1. [B1] lieu où une personne vit habituellement
     - example: Ce soir-là, elle a regagné son domicile très tard après le travail.
     - collocations: à domicile; livraison à domicile; travailler à domicile; sans domicile fixe; regagner son domicile; quitter le domicile; domicile conjugal; changer de domicile; violation de domicile
  2. [B2] lieu où une personne est officiellement établie aux yeux de la loi
     - example: Pour ouvrir un compte, il faut fournir un justificatif de domicile récent.
     - collocations: justificatif de domicile; élire domicile; domicile légal; domicile fiscal
     - labels: domain law

### 38. recevoir

**Lexeme 1: recevoir**, VERB
- grammar: conjugation 3; irregular True; auxiliary have
- said: /ʁə.sə.ˈvwaʁ/ (fr-FR)
- forms: reçois [Mood=Ind, Tense=Pres, Person=1,2, Number=Sing, VerbForm=Fin] /ʁə.ˈswa/; reçoit [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ʁə.ˈswa/; recevons [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /ʁə.sə.ˈvɔ̃/; reçoivent [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ʁə.ˈswav/; reçu [VerbForm=Part, Tense=Past] /ʁə.ˈsy/; reçut [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /ʁə.ˈsy/; recevrai [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] /ʁə.sə.ˈvʁe/; reçoive [Mood=Sub, Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin] /ʁə.ˈswav/
  1. [A1] obtenir ce qu'une autre personne donne, envoie ou accorde
     - example: Ce matin, j'ai reçu une longue lettre de ma grand-mère.
     - frames: recevoir qqch de qqn
     - collocations: recevoir une lettre; recevoir un message; recevoir un cadeau; recevoir des nouvelles; recevoir des ordres; recevoir un prix; recevoir un salaire; recevoir une formation
     - grammar: valency Tr
  2. [A2] accueillir chez soi des personnes que l'on a invitées
     - example: Nous recevons des amis à dîner samedi soir.
     - collocations: recevoir à dîner; recevoir des invités; bien recevoir; recevoir chaleureusement
     - grammar: valency Both
  3. [B1] accepter de voir une personne qui vient pour un entretien
     - example: Le directeur vous recevra dans son bureau à dix heures.
     - collocations: recevoir sur rendez-vous; recevoir un client
     - grammar: valency Both
  4. [B1] subir un coup, un choc ou une attaque venant de l'extérieur
     - example: Pendant le match, il a reçu un ballon en pleine figure.
     - collocations: recevoir un coup; recevoir une gifle; recevoir une balle; recevoir des critiques
     - grammar: valency Tr
  5. [B1] être admis après avoir réussi un examen ou un concours
     - example: Ma sœur a été reçue au concours d'entrée de l'école.
     - frames: être reçu à qqch
     - collocations: être reçu à un examen; être reçu au baccalauréat
     - grammar: valency Tr; note used in the passive: être reçu
  6. [B2] capter un signal de radio, de télévision ou de téléphone
     - example: Dans cette vallée, on reçoit très mal les chaînes de télévision.
     - collocations: recevoir une chaîne; recevoir un appel
     - grammar: valency Tr

### 39. aimer

**Lexeme 1: aimer**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /ɛ.ˈme/ (fr-FR), /e.ˈme/ (fr-FR)
  1. [A1] avoir une tendresse profonde et un attachement fort pour une personne
     - example: Je t'aime depuis le premier jour de notre rencontre.
     - frames: aimer qqn
     - collocations: aimer passionnément; aimer profondément; aimer tendrement; s'aimer
     - opposites: haïr
  2. [A1] trouver agréable une personne, une chose ou une activité
     - example: Mes enfants aiment beaucoup jouer dans le jardin après l'école.
     - frames: aimer qqch; aimer + inf; aimer que + subj; aimer qqn
     - collocations: aimer bien; aimer beaucoup; aimer mieux; j'aimerais bien
     - opposites: détester
  3. [B1] montrer en ligne son approbation d'un contenu en cliquant sur un bouton
     - example: Plus de mille personnes ont aimé la photo que tu as publiée.
     - frames: aimer qqch
     - collocations: aimer une publication; aimer une page
  4. [B2] avoir besoin de certaines conditions pour bien pousser ou vivre
     - example: Cette plante aime le soleil et supporte mal le froid.
     - frames: aimer qqch

### 40. prince

**Lexeme 1: prince**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈpʁɛ̃s/ (fr-FR)
  1. [A2] fils d'un roi, ou homme d'une famille qui règne sur un pays
     - example: Le jeune prince montera sur le trône à la mort de son père.
     - collocations: prince héritier; prince charmant; prince consort; le petit prince
  2. [B1] homme qui gouverne une principauté, ou noble qui porte ce titre
     - example: Ce prince règne depuis vingt ans sur un tout petit territoire.
     - collocations: prince souverain; titre de prince
  3. [C1] homme considéré comme le premier ou le meilleur dans son domaine
     - example: On le surnommait déjà le prince des poètes de son vivant.
     - frames: le prince de + nom pluriel
     - collocations: le prince des poètes
     - labels: style literary, figurative True

### 41. disposition

**Lexeme 1: disposition**, NOUN
- grammar: gender Fem
- said: /dis.po.zi.ˈsjɔ̃/ (fr-FR)
  1. [B1] possibilité d'utiliser librement quelque chose ou de faire appel à quelqu'un
     - example: Notre équipe reste à votre disposition pour toute question.
     - frames: à la disposition de qqn; mettre qqch à la disposition de qqn
     - collocations: mettre à disposition; à votre disposition; rester à disposition; tenir à la disposition; avoir à sa disposition
     - grammar: number Sing; countability Mass
  2. [B1] mesure prise à l'avance pour préparer ou organiser quelque chose
     - example: Nous avons pris toutes les dispositions pour accueillir les invités.
     - frames: prendre des dispositions pour + inf
     - collocations: prendre des dispositions; prendre toutes les dispositions nécessaires; dispositions particulières
     - grammar: countability Count; number Plur, mainly
  3. [B2] point précis fixé par une loi, un règlement, un contrat ou un jugement
     - example: Cette disposition du contrat entre en vigueur dès janvier prochain.
     - collocations: dispositions légales; en vertu des dispositions; dispositions transitoires; disposition fiscale
     - labels: domain law
     - grammar: countability Count
  4. [B2] manière dont les choses ou les parties d'un ensemble sont placées
     - example: La disposition des pièces rend cet appartement très agréable à vivre.
     - frames: disposition de qqch
     - collocations: disposition des lieux; disposition des meubles
     - grammar: countability Count
  5. [B2] aptitude naturelle à faire quelque chose ou à réussir dans un domaine
     - example: Cette enfant montre de réelles dispositions pour le dessin.
     - frames: dispositions pour qqch
     - grammar: countability Count; number Plur
  6. [C1] humeur ou état d'esprit dans lequel on se trouve envers quelqu'un ou quelque chose
     - example: Je te trouve dans de bien meilleures dispositions qu'hier soir.
     - frames: dispositions envers qqn; dispositions à l'égard de qqn/qqch; être en disposition de + inf
     - collocations: être dans de bonnes dispositions; dans les meilleures dispositions
     - grammar: countability Count; number Plur, mainly
- relations: verbal_noun_of disposer (VERB)

### 42. moteur

**Lexeme 1: moteur**, NOUN
- grammar: gender Masc; countability Count
- said: /mɔ.ˈtœʁ/ (fr-FR), /mo.ˈtœʁ/ (fr-FR)
  1. [A2] machine qui transforme une énergie en mouvement pour faire marcher un véhicule ou un appareil
     - example: Chaque matin, mon voisin laisse tourner son moteur pendant dix minutes.
     - collocations: démarrer le moteur; couper le moteur; le moteur cale; faire tourner le moteur; moteur électrique; moteur diesel; moteur à essence; un bruit de moteur
  2. [B2] personne ou chose qui pousse à agir et fait avancer quelque chose
     - example: Les exportations restent le principal moteur de la croissance du pays.
     - frames: le moteur de qqch
     - collocations: être le moteur de; moteur de la croissance; moteur du changement
     - labels: figurative True
  3. [B2] programme informatique chargé d'une tâche précise à l'intérieur d'un logiciel
     - example: Je préfère utiliser un moteur de recherche qui protège mes données.
     - collocations: moteur de recherche; moteur de jeu
     - labels: domain computing
**Lexeme 2: moteur**, ADJ
- grammar: comparable False
- said: /mɔ.ˈtœʁ/ (fr-FR), /mo.ˈtœʁ/ (fr-FR)
- forms: motrice [Gender=Fem, Number=Sing] /mɔ.ˈtʁis/; motrices [Gender=Fem, Number=Plur] /mɔ.ˈtʁis/
  1. [B2] qui produit ou transmet un mouvement
     - example: Sur ce tracteur, les roues motrices se trouvent à l'arrière.
     - collocations: force motrice; roues motrices; rôle moteur
  2. [C1] qui touche la capacité du corps à faire des mouvements
     - example: Depuis son accident, elle garde quelques troubles moteurs à la main droite.
     - collocations: handicap moteur; troubles moteurs; développement moteur

### 43. opposition

**Lexeme 1: opposition**, NOUN
- grammar: gender Fem; countability Both
- said: /ɔ.po.zi.ˈsjɔ̃/ (fr-FR)
  1. [B1] résistance et refus face à une décision, une idée ou une personne
     - example: Le projet de nouvelle route rencontre une forte opposition chez les habitants.
     - frames: opposition à qqch; opposition de qqn; faire opposition à qqch; être en opposition avec qqn
     - collocations: rencontrer une opposition; une forte opposition; une opposition farouche; sans opposition; manifester son opposition
     - grammar: countability Mass, mainly
  2. [B1] partis et élus politiques qui combattent le gouvernement au pouvoir
     - example: Les députés de l'opposition ont voté contre le budget cette année.
     - collocations: les partis de l'opposition; un député de l'opposition; le chef de l'opposition; passer dans l'opposition; être dans l'opposition
     - labels: domain politics
     - grammar: number Sing
     - opposites: majorité
  3. [B2] contraste marqué entre deux choses très différentes
     - example: Ce roman repose sur l'opposition entre la ville et la campagne.
     - frames: opposition entre qqch et qqch; par opposition à qqch; en opposition avec qqch
  4. [B2] démarche pour bloquer une carte ou un chèque perdus ou volés
     - example: J'ai perdu mon portefeuille, je dois faire opposition sur ma carte.
     - frames: faire opposition sur qqch
     - collocations: faire opposition; le service d'opposition
     - grammar: countability Count
- relations: verbal_noun_of opposer (VERB)

### 44. réduction

**Lexeme 1: réduction**, NOUN
- grammar: gender Fem
- said: /ʁe.dyk.ˈsjɔ̃/ (fr-FR)
  1. [B1] diminution de la quantité, de la taille ou de l'importance d'une chose
     - example: Le gouvernement annonce une réduction importante des dépenses publiques.
     - frames: réduction de qqch
     - collocations: réduction des coûts; réduction d'impôts; réduction des effectifs; réduction du temps de travail; réduction des émissions; une forte réduction
     - grammar: countability Both
     - opposites: augmentation
  2. [A2] somme retirée du prix normal d'un produit ou d'un service
     - example: Avec ta carte d'étudiant, tu as droit à une réduction au cinéma.
     - frames: réduction sur qqch
     - collocations: bénéficier d'une réduction; faire une réduction; obtenir une réduction; accorder une réduction; carte de réduction; bon de réduction
     - grammar: countability Count
  3. [C1] copie d'un objet ou d'une image faite en plus petit
     - example: Le musée vend des réductions en bronze de ses statues célèbres.
     - collocations: en réduction
     - grammar: countability Count
- relations: verbal_noun_of réduire (VERB)

### 45. cheveu

**Lexeme 1: cheveu**, NOUN
- grammar: gender Masc; countability Count; number Plur, mainly; note in the plural, all the hair on a person's head
- said: /ʃə.ˈvø/ (fr-FR), /ˈʃvø/ (fr-FR, casual)
- forms: cheveux [Number=Plur] /ʃə.ˈvø/
  1. [A1] chacun des poils fins qui poussent sur la tête d'une personne
     - example: Je me fais couper les cheveux tous les deux mois.
     - collocations: se faire couper les cheveux; se laver les cheveux; se brosser les cheveux; perdre ses cheveux; avoir les cheveux longs; cheveux courts; cheveux bouclés; cheveux raides; cheveux blancs
