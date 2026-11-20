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
  1. [A2] ensemble d'objets de même sorte réunis pour leur intérêt ou leur valeur
     - example: Mon grand-père possède une collection de timbres venus du monde entier.
     - frames: collection de qqch
     - collocations: faire collection de; pièce de collection; objet de collection; commencer une collection; enrichir sa collection; collection privée; collection permanente
  2. [B1] série de livres publiés par un même éditeur sous un titre commun
     - example: Ce roman policier est paru dans une collection de poche très connue.
     - collocations: paraître dans une collection; collection de poche; directeur de collection
  3. [B1] ensemble des vêtements créés par un couturier ou une marque pour une saison
     - example: La styliste présentera sa nouvelle collection lors du défilé de mars.
     - collocations: collection printemps-été; collection automne-hiver; présenter sa collection; nouvelle collection
- relations: confusable collecte (NOUN): collecte is the gathering of money, donations or waste; collection is a set of objects kept together

### 3. accepter

**Lexeme 1: accepter**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /ak.sɛp.ˈte/ (fr-FR)
  1. [A2] prendre ou recevoir volontiers ce que quelqu'un offre ou propose
     - example: Merci beaucoup, j'accepte votre invitation avec grand plaisir.
     - frames: accepter qqch; accepter qqch de qqn
     - collocations: accepter une invitation; accepter une offre; accepter une proposition; accepter un cadeau; accepter un poste; accepter un chèque
     - opposites: refuser
  2. [A2] dire oui, être d'accord pour faire ou laisser faire quelque chose
     - example: Elle accepte de nous aider à déménager samedi prochain.
     - frames: accepter de + inf; accepter que + subj
     - opposites: refuser
  3. [B1] supporter une chose difficile ou désagréable sans se révolter contre elle
     - example: Il a encore du mal à accepter la mort de son père.
     - collocations: accepter son sort; accepter la défaite; accepter la critique; accepter la réalité; accepter la situation
  4. [B1] recevoir quelqu'un comme membre d'un groupe, d'une école ou d'une famille
     - example: Cette école n'accepte que vingt élèves dans chaque classe.
     - frames: accepter qqn dans qqch; accepter qqn comme + nom
     - collocations: être accepté dans une école; se faire accepter
     - opposites: refuser

### 4. propriété

**Lexeme 1: propriété**, NOUN
- grammar: gender Fem; countability Both
- said: /pʁɔ.pʁi.je.ˈte/ (fr-FR)
  1. [B1] droit d'utiliser une chose et d'en disposer comme on le veut
     - example: Ce terrain est la propriété de la commune depuis longtemps.
     - frames: la propriété de qqn
     - collocations: propriété privée; titre de propriété; propriété intellectuelle; accéder à la propriété; en toute propriété; droit de propriété
     - grammar: countability Mass
  2. [B1] maison avec un grand terrain, ou terres qu'une personne possède
     - example: Mes grands-parents possèdent une belle propriété à la campagne.
     - collocations: une grande propriété; propriété viticole; propriété agricole; acheter une propriété
     - grammar: countability Count
  3. [B2] qualité particulière qui caractérise une chose ou une substance
     - example: On connaît depuis longtemps les propriétés médicinales de cette plante.
     - frames: avoir la propriété de + inf
     - collocations: propriétés chimiques; propriétés physiques; propriétés médicinales
     - grammar: countability Count; note often in the plural
  4. [C2] exactitude d'un mot qui correspond bien à ce qu'on veut dire
     - example: Le professeur insiste sur la propriété des termes dans les dissertations.
     - collocations: propriété des termes
     - labels: style literary
     - grammar: countability Mass
     - opposites: impropriété
- relations: confusable propreté (NOUN): propreté means cleanliness; the two words differ by only one syllable

### 5. papier

**Lexeme 1: papier**, NOUN
- grammar: gender Masc
- said: /pa.ˈpje/ (fr-FR)
  1. [A1] matière mince faite de fibres, en feuilles, pour écrire, imprimer ou emballer
     - example: Je n'ai plus de papier pour imprimer mes billets de train.
     - collocations: une feuille de papier; papier recyclé; papier toilette; papier peint; papier journal; papier cadeau; papier aluminium; en papier
     - grammar: countability Mass, mainly
  2. [A2] feuille portant un texte écrit ou imprimé
     - example: Tu dois signer ce papier avant de partir en voyage.
     - collocations: signer un papier; remplir des papiers
     - grammar: countability Count
  3. [B1] documents officiels qui prouvent l'identité d'une personne
     - example: Le policier lui a demandé de montrer ses papiers.
     - collocations: papiers d'identité; avoir ses papiers en règle; montrer ses papiers; sans papiers
     - grammar: countability Count; number Plur
  4. [B2] article écrit pour un journal, un magazine ou un site d'information
     - example: Ce journaliste prépare un papier sur la crise du logement.
     - frames: papier sur qqch
     - collocations: écrire un papier
     - grammar: countability Count

### 6. section

**Lexeme 1: section**, NOUN
- grammar: gender Fem; countability Count
- said: /sɛk.ˈsjɔ̃/ (fr-FR)
  1. [B1] chacune des parties distinctes qui composent un texte, un ensemble ou un lieu
     - example: Vous trouverez les réponses dans la dernière section du manuel.
     - frames: section de qqch
     - collocations: la section suivante; diviser en sections
  2. [B1] groupe de personnes formant une division d'une organisation, d'une école ou d'une armée
     - example: Ma fille entre en grande section à la rentrée prochaine.
     - collocations: section syndicale; section locale; grande section; moyenne section; petite section; chef de section
  3. [C1] surface obtenue quand on coupe un objet par un plan
     - example: Pour ce four, il faut un câble d'une section plus grande.
     - collocations: section transversale; section d'un câble

### 7. espoir

**Lexeme 1: espoir**, NOUN
- grammar: gender Masc; countability Both
- said: /ɛs.ˈpwaʁ/ (fr-FR)
  1. [A2] sentiment de confiance qu'une chose désirée va se réaliser
     - example: Malgré les difficultés, nous gardons l'espoir de retrouver notre chat.
     - frames: espoir de + inf; espoir que + ind; espoir de qqch; dans l'espoir de + inf
     - collocations: garder espoir; perdre espoir; redonner espoir; plein d'espoir; sans espoir; nourrir l'espoir; un faible espoir; un immense espoir; un espoir de guérison
     - opposites: désespoir
  2. [B1] personne ou chose sur laquelle on compte pour obtenir ce qu'on désire
     - example: Ce nouveau traitement représente le dernier espoir de nombreux malades.
     - collocations: le dernier espoir; le seul espoir; tous ses espoirs
     - grammar: countability Count
  3. [B2] jeune personne dont on attend de grands succès dans son domaine
     - example: À seize ans, cette nageuse est déjà un grand espoir du pays.
     - frames: espoir de qqch
     - collocations: un jeune espoir; un espoir du football; meilleur espoir
     - grammar: countability Count
- relations: derived_from espérer (VERB); confusable espérance (NOUN): espérance is more literary or abstract and is used in set phrases like espérance de vie; espoir is the everyday word for hope

### 8. approche

**Lexeme 1: approche**, NOUN
- grammar: gender Fem; countability Count
- said: /a.ˈpʁɔʃ/ (fr-FR)
  1. [B2] manière de considérer et de traiter un sujet ou un problème
     - example: Cette école propose une approche différente de l'apprentissage des langues.
     - frames: approche de qqch
     - collocations: adopter une approche; changer d'approche; une nouvelle approche; une approche globale; une approche pragmatique
  2. [B1] fait de venir plus près d'un lieu, d'une personne ou d'un moment
     - example: À l'approche de l'orage, les promeneurs rentrent vite chez eux.
     - frames: à l'approche de qqch; à l'approche de qqn
     - collocations: à son approche; l'approche des vacances
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
  1. [A2] souvent et de façon habituelle, à intervalles à peu près égaux
     - example: Je vais régulièrement voir mes grands-parents à la campagne.
     - collocations: faire régulièrement du sport; se rendre régulièrement; mettre à jour régulièrement
     - opposites: irrégulièrement
  2. [B1] de manière égale et uniforme, sans changement brusque
     - example: Depuis qu'il dort, le malade respire régulièrement et calmement.
     - collocations: respirer régulièrement; augmenter régulièrement; répartir régulièrement; progresser régulièrement
     - opposites: irrégulièrement
  3. [C1] d'une manière conforme à la loi ou aux procédures prévues
     - example: Le nouveau conseil a été régulièrement élu par les habitants.
     - collocations: régulièrement élu; régulièrement inscrit
     - labels: style official
     - opposites: irrégulièrement
- relations: adverb_of régulier (ADJ)

### 11. surprise

**Lexeme 1: surprise**, NOUN
- grammar: gender Fem; note also used after a noun, without changing its form: une visite surprise
- said: /syʁ.ˈpʁiz/ (fr-FR)
  1. [A2] sentiment causé par quelque chose qu'on n'attendait pas
     - example: À ma grande surprise, il a accepté sans discuter.
     - collocations: à ma grande surprise; à la surprise générale; avec surprise; un cri de surprise; de surprise
     - grammar: countability Mass
  2. [A2] chose ou événement auquel on ne s'attendait pas
     - example: Ce petit restaurant de quartier a été une très bonne surprise.
     - frames: avoir la surprise de + inf
     - collocations: une bonne surprise; une mauvaise surprise; sans surprise; par surprise; créer la surprise; quelle surprise
     - grammar: countability Count
  3. [A2] cadeau ou plaisir préparé pour quelqu'un sans qu'il le sache
     - example: Nous préparons une surprise pour l'anniversaire de notre mère.
     - frames: faire une surprise à qqn
     - collocations: faire une surprise; une fête surprise; c'est une surprise
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
  1. [A2] qui sort de l'ordinaire et surprend, difficile à comprendre ou à expliquer
     - example: J'entends un bruit bizarre dans la cuisine depuis ce matin.
     - frames: il est bizarre que + subj; c'est bizarre de + inf
     - collocations: c'est bizarre; trouver ça bizarre; un bruit bizarre; une odeur bizarre; une impression bizarre; tout de même bizarre
     - opposites: normal
  2. [B1] dont le comportement ou la manière d'être paraît curieux aux autres
     - example: Ton frère est un peu bizarre ces temps-ci, il ne parle plus.
     - collocations: un type bizarre; avoir l'air bizarre; un peu bizarre
  3. [B1] pas tout à fait bien, mal à l'aise dans son corps ou son esprit
     - example: Après ce médicament, je me sens bizarre et j'ai la tête qui tourne.
     - collocations: se sentir bizarre
     - labels: formality informal

### 14. rencontrer

**Lexeme 1: rencontrer**, VERB
- grammar: conjugation 1
- said: /ʁɑ̃.kɔ̃.ˈtʁe/ (fr-FR)
  1. [A2] se trouver par hasard en présence de quelqu'un
     - example: Ce matin, j'ai rencontré mon ancien professeur au marché.
     - frames: rencontrer qqn
     - collocations: rencontrer par hasard
     - grammar: auxiliary have; valency Tr
  2. [A2] voir quelqu'un pour la première fois et faire sa connaissance
     - example: Elle a rencontré son mari pendant un voyage à l'étranger.
     - frames: rencontrer qqn
     - grammar: auxiliary have; valency Tr
  3. [B1] voir quelqu'un à un moment prévu pour parler avec lui
     - example: Le directeur rencontrera les parents d'élèves jeudi prochain.
     - frames: rencontrer qqn
     - collocations: rencontrer la presse
     - grammar: auxiliary have; valency Tr
  4. [B1] trouver sur son chemin une difficulté, un obstacle ou une réaction
     - example: Nous rencontrons quelques problèmes techniques depuis ce matin.
     - frames: rencontrer qqch
     - collocations: rencontrer des difficultés; rencontrer un problème; rencontrer un obstacle; rencontrer une résistance; rencontrer un grand succès
     - grammar: auxiliary have; valency Tr
  5. [B2] jouer contre un adversaire ou une équipe dans une compétition
     - example: Notre équipe rencontrera le champion en demi-finale samedi.
     - frames: rencontrer qqn
     - grammar: auxiliary have; valency Tr
  6. [B2] exister, pouvoir être trouvé ou observé quelque part
     - example: Une telle générosité se rencontre rarement de nos jours.
     - collocations: se rencontrer rarement; se rencontrer souvent
     - grammar: valency Refl; reflexive True; auxiliary be; note pronominal: se rencontrer, with a thing as subject

### 15. artiste

**Lexeme 1: artiste**, NOUN
- grammar: gender Com; countability Mass
- said: /aʁ.ˈtist/ (fr-FR)
  1. [A2] personne dont le métier ou la passion est de créer des œuvres originales
     - example: Cette artiste expose ses sculptures dans une petite galerie du quartier.
     - collocations: artiste peintre; artiste contemporain; œuvre d'un artiste; atelier d'artiste; artiste engagé
  2. [B1] personne qui joue, chante, danse ou fait de la musique devant un public
     - example: Après le concert, les artistes sont venus saluer le public.
     - collocations: l'entrée des artistes; artiste de variétés; artiste de cirque; loge d'artiste
  3. [B2] personne qui fait quelque chose avec beaucoup d'habileté et de goût
     - example: Regarde ce gâteau : ton frère est un vrai artiste en cuisine !
     - collocations: un vrai artiste; un travail d'artiste
     - labels: figurative True
- relations: confusable artisan (NOUN): an artisan makes useful objects by hand as a trade; an artiste creates works of art or performs
**Lexeme 2: artiste**, ADJ
- grammar: comparable True
- said: /aʁ.ˈtist/ (fr-FR)
  1. [B2] qui a une sensibilité créative et le goût du beau
     - example: Elle a toujours eu un tempérament artiste et un peu rêveur.
     - collocations: tempérament artiste; âme d'artiste
- relations: derived_from artiste (NOUN); confusable artistique (ADJ): artistique means relating to art (une œuvre artistique); artiste describes a person's creative temperament

### 16. dormir

**Lexeme 1: dormir**, VERB
- grammar: conjugation 3; irregular True; valency Intr; auxiliary have
- said: /dɔʁ.ˈmiʁ/ (fr-FR)
- forms: dors [Mood=Ind, Tense=Pres, Person=1,2, Number=Sing, VerbForm=Fin] /ˈdɔʁ/; dort [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈdɔʁ/; dormons [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /dɔʁ.ˈmɔ̃/; dorment [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈdɔʁm/; dormi [Tense=Past, VerbForm=Part] /dɔʁ.ˈmi/; dormit [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /dɔʁ.ˈmi/; dormirai [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] /dɔʁ.mi.ˈʁe/; dorme [Mood=Sub, Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin] /ˈdɔʁm/
  1. [A1] être dans le sommeil, en repos, les yeux fermés et sans conscience
     - example: Les enfants dorment déjà, parle moins fort s'il te plaît.
     - collocations: dormir profondément; dormir à poings fermés; dormir comme un loir; bien dormir; mal dormir; aller dormir; dormir à la belle étoile; dormir tard
     - opposites: veiller
  2. [B2] rester sans être utilisé, sans activité ou sans changement
     - example: Ton argent dort sur ce compte depuis des années sans rien rapporter.
     - collocations: laisser dormir un projet; laisser dormir un dossier
     - labels: figurative True
  3. [B2] manquer d'attention, ne pas réagir assez vite
     - example: Allez, on ne dort pas, le match reprend dans deux minutes !
     - labels: formality informal, figurative True

### 17. historique

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

### 18. sentir

**Lexeme 1: sentir**, VERB
- grammar: conjugation 3; irregular True
- said: /sɑ̃.ˈtiʁ/ (fr-FR)
- forms: sens [Person=1,2, Number=Sing, Mood=Ind, Tense=Pres, VerbForm=Fin] /ˈsɑ̃/; sent [Person=3, Number=Sing, Mood=Ind, Tense=Pres, VerbForm=Fin] /ˈsɑ̃/; sentons [Person=1, Number=Plur, Mood=Ind, Tense=Pres, VerbForm=Fin] /sɑ̃.ˈtɔ̃/; sentent [Person=3, Number=Plur, Mood=Ind, Tense=Pres, VerbForm=Fin] /ˈsɑ̃t/; senti [Tense=Past, VerbForm=Part] /sɑ̃.ˈti/; sentit [Person=3, Number=Sing, Mood=Ind, Tense=Past, VerbForm=Fin] /sɑ̃.ˈti/; sentirai [Person=1, Number=Sing, Mood=Ind, Tense=Fut, VerbForm=Fin] /sɑ̃.ti.ˈʁe/; sente [Person=1,3, Number=Sing, Mood=Sub, Tense=Pres, VerbForm=Fin] /ˈsɑ̃t/
  1. [A2] percevoir par le corps une sensation physique, ou éprouver une émotion
     - example: Je sens une légère douleur dans le dos depuis ce matin.
     - frames: sentir qqch; sentir qqn/qqch + inf
     - collocations: se faire sentir; sentir la douleur; sentir le froid; sentir la fatigue
     - grammar: auxiliary have; valency Tr
  2. [A2] être dans un certain état physique ou moral et en avoir conscience
     - example: Après une bonne nuit, tu te sentiras beaucoup mieux.
     - frames: se sentir + adj
     - collocations: se sentir bien; se sentir mal; se sentir seul; se sentir à l'aise; se sentir chez soi
     - grammar: valency Refl; reflexive True; auxiliary be
  3. [A2] percevoir une odeur par le nez
     - example: Dans la cuisine, on sent déjà l'odeur du gâteau.
     - frames: sentir qqch
     - collocations: sentir une odeur; sentir le parfum
     - grammar: auxiliary have; valency Tr
  4. [A2] répandre une odeur, agréable ou désagréable
     - example: Ces roses sentent très bon dans le salon.
     - frames: sentir qqch
     - collocations: sentir bon; sentir mauvais; sentir le brûlé; sentir le renfermé; sentir la transpiration
     - grammar: auxiliary have; valency Both
  5. [B1] comprendre ou deviner par intuition, sans en avoir de preuve
     - example: Elle sentait que quelque chose n'allait pas chez son frère.
     - frames: sentir que + ind; sentir qqch
     - collocations: sentir le danger; sentir venir
     - grammar: auxiliary have; valency Tr
  6. [B2] faire penser à une chose, en montrer les signes
     - example: Avec ces valises dans l'entrée, ça sent les vacances.
     - frames: sentir qqch
     - collocations: ça sent la fin; ça sent le roussi
     - labels: figurative True
     - grammar: auxiliary have; valency Tr
- relations: confusable ressentir (VERB): ressentir is used mostly for emotions and lasting effects (ressentir de la colère); sentir also covers smells and bodily sensations

### 19. tenter

**Lexeme 1: tenter**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /tɑ̃.ˈte/ (fr-FR)
  1. [B1] faire un effort pour réussir une chose dont le résultat est incertain
     - example: Je vais tenter de joindre le service client avant midi.
     - frames: tenter de + inf; tenter qqch
     - collocations: tenter sa chance; tenter le coup; tenter l'impossible; tenter une expérience; tenter une approche
  2. [B1] donner à quelqu'un l'envie de faire ou d'avoir une chose
     - example: Cette petite robe rouge me tente beaucoup, mais elle coûte cher.
     - frames: tenter qqn; être tenté de + inf; être tenté par qqch
     - collocations: se laisser tenter; ça me tente; être fortement tenté
  3. [C1] pousser quelqu'un à faire le mal ou à désobéir à Dieu
     - example: Dans ce récit, le diable tente l'ermite pendant quarante jours.
     - frames: tenter qqn
     - labels: domain religion

### 20. valoir

**Lexeme 1: valoir**, VERB
- grammar: conjugation 3; irregular True; valency Both
- said: /va.ˈlwaʁ/ (fr-FR)
- forms: vaux [Mood=Ind, Tense=Pres, Person=1,2, Number=Sing, VerbForm=Fin] /ˈvo/; vaut [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈvo/; valons [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /va.ˈlɔ̃/; valent [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈval/; valu [VerbForm=Part, Tense=Past] /va.ˈly/; valut [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /va.ˈly/; vaudrai [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] /vo.ˈdʁe/; vaille [Mood=Sub, Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin] /ˈvaj/
  1. [A2] avoir un certain prix, pouvoir être vendu ou acheté pour une somme
     - example: Cette vieille montre vaut beaucoup plus que je ne pensais.
     - collocations: valoir cher; valoir une fortune; valoir de l'or; combien ça vaut
     - grammar: auxiliary have; valency Intr; note the price is not a direct object: the participle does not agree
  2. [B1] avoir une certaine qualité, un certain intérêt ou mérite
     - example: Ton idée ne vaut rien sans un bon plan pour l'appliquer.
     - collocations: ne rien valoir; ne pas valoir grand-chose; valoir mieux que
     - grammar: auxiliary have; valency Intr
  3. [A2] être préférable, plus sage ou plus utile à faire
     - example: Il vaut mieux partir tôt pour éviter les embouteillages.
     - frames: il vaut mieux + inf; il vaut mieux que + subj; mieux vaut + inf
     - collocations: il vaut mieux; mieux vaut tard que jamais
     - grammar: auxiliary have; valency Impers; note always with mieux: il vaut mieux, mieux vaut
  4. [B1] mériter le temps, l'effort ou le déplacement qu'on y consacre
     - example: Ce petit musée vaut vraiment le détour si vous passez par là.
     - frames: valoir la peine de + inf; valoir la peine que + subj
     - collocations: valoir la peine; valoir le coup; valoir le détour
     - grammar: auxiliary have; valency Tr
  5. [B2] faire obtenir ou causer quelque chose, de bon ou de mauvais, à quelqu'un
     - example: Son courage lui a valu l'admiration de tous ses collègues.
     - frames: valoir qqch à qqn
     - grammar: auxiliary have; valency Tr
  6. [B2] s'appliquer aussi, de la même manière, à quelqu'un ou quelque chose
     - example: Cette règle vaut pour tous les élèves, sans exception.
     - frames: valoir pour qqn/qqch
     - grammar: auxiliary have; valency Intr
  7. [B2] avoir la même valeur, être aussi bon ou mauvais l'un que l'autre
     - example: Les deux solutions se valent, choisis celle que tu préfères.
     - collocations: ça se vaut
     - grammar: valency Refl; reflexive True; auxiliary be; note plural or collective subject; se means each other

### 21. améliorer

**Lexeme 1: améliorer**, VERB
- grammar: conjugation 1
- said: /a.me.ljɔ.ˈʁe/ (fr-FR)
  1. [A2] rendre une chose plus satisfaisante, de plus grande qualité ou plus efficace
     - example: Ce nouveau logiciel doit améliorer la qualité de nos services.
     - frames: améliorer qqch
     - collocations: améliorer la situation; améliorer la qualité; améliorer ses résultats; améliorer les conditions de travail; améliorer son niveau; améliorer la sécurité; nettement améliorer
     - grammar: auxiliary have; valency Tr
     - opposites: détériorer
  2. [B1] devenir plus satisfaisant, gagner en qualité ou en valeur
     - example: Depuis quelques jours, le temps s'améliore enfin dans la région.
     - collocations: la situation s'améliore; le temps s'améliore; s'améliorer nettement; s'améliorer progressivement
     - grammar: valency Refl; reflexive True; auxiliary be

### 22. normal

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

### 23. être

**Lexeme 1: être**, VERB
- grammar: conjugation 3; irregular True; auxiliary have
- said: /ˈɛtʁ/ (fr-FR)
- forms: suis [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈsɥi/; es [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /ˈɛ/; est [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈɛ/; sommes [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /ˈsɔm/; êtes [Mood=Ind, Tense=Pres, Person=2, Number=Plur, VerbForm=Fin] /ˈɛt/; sont [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈsɔ̃/; été [VerbForm=Part, Tense=Past] /e.ˈte/; étant [VerbForm=Part, Tense=Pres] /e.ˈtɑ̃/; étais [Mood=Ind, Tense=Imp, Person=1,2, Number=Sing, VerbForm=Fin] /e.ˈtɛ/; fut [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /ˈfy/; serai [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] /sə.ˈʁe/; sois [Mood=Sub, Tense=Pres, Person=1,2, Number=Sing, VerbForm=Fin] /ˈswa/; soit [Mood=Sub, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈswa/; sois [Mood=Imp, Person=2, Number=Sing, VerbForm=Fin] /ˈswa/; soyons [Mood=Sub,Imp, Person=1, Number=Plur, VerbForm=Fin] /swa.ˈjɔ̃/; soyez [Mood=Sub,Imp, Person=2, Number=Plur, VerbForm=Fin] /swa.ˈje/
  1. [A1] avoir une qualité, un état, une identité ou une profession donnés
     - example: Les enfants sont dans le jardin depuis le début de l'après-midi.
     - frames: être + adj; être + nom
     - collocations: être d'accord; être en retard; être à l'heure; être en colère; être en forme
     - grammar: valency Intr
  2. [A1] se trouver dans un lieu ou dans une situation
     - example: Ma sœur est infirmière dans une petite clinique de quartier.
     - frames: être chez qqn
     - collocations: être à la maison; être en vacances; être chez soi
     - grammar: valency Intr
  3. [A1] servir à indiquer l'heure, la date ou le moment présent
     - example: Il est déjà minuit, nous devrions aller dormir.
     - frames: il est + nom; nous sommes + nom
     - collocations: il est tard; il est tôt
     - grammar: valency Impers; note with il for the hour, with nous or on for the day or date
  4. [A2] appartenir à une personne
     - example: Ce vélo rouge est à mon frère aîné.
     - frames: être à qqn
     - grammar: valency Intr
  5. [B1] aller quelque part puis en revenir
     - example: J'ai été au marché ce matin avec ma voisine.
     - labels: formality informal
     - grammar: valency Intr; note only in compound tenses with avoir and in the passé simple
  6. [B2] avoir une existence réelle, se trouver dans le monde
     - example: Il est des moments où l'on préfère rester seul.
     - frames: il est des + nom pluriel
     - collocations: il était une fois
     - labels: style literary
     - grammar: valency Intr
**Lexeme 2: être**, AUX
- grammar: conjugation 3; irregular True; auxiliary have
- said: /ˈɛtʁ/ (fr-FR)
- forms: suis [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈsɥi/; es [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /ˈɛ/; est [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈɛ/; sommes [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /ˈsɔm/; êtes [Mood=Ind, Tense=Pres, Person=2, Number=Plur, VerbForm=Fin] /ˈɛt/; sont [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈsɔ̃/; été [VerbForm=Part, Tense=Past] /e.ˈte/; étant [VerbForm=Part, Tense=Pres] /e.ˈtɑ̃/; étais [Mood=Ind, Tense=Imp, Person=1,2, Number=Sing, VerbForm=Fin] /e.ˈtɛ/; fut [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /ˈfy/; serai [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] /sə.ˈʁe/; sois [Mood=Sub, Tense=Pres, Person=1,2, Number=Sing, VerbForm=Fin] /ˈswa/; soit [Mood=Sub, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈswa/; soyons [Mood=Sub,Imp, Person=1, Number=Plur, VerbForm=Fin] /swa.ˈjɔ̃/; soyez [Mood=Sub,Imp, Person=2, Number=Plur, VerbForm=Fin] /swa.ˈje/
  1. [A1] sert à former les temps composés de certains verbes et des verbes pronominaux
     - example: Nous sommes arrivés à la gare bien avant le départ du train.
     - frames: être + participe passé
     - grammar: note with verbs of movement or change such as aller, venir, naître, and all pronominal verbs
  2. [A2] sert à former le passif des verbes qui ont un complément direct
     - example: Cette maison a été construite par mon grand-père.
     - frames: être + participe passé
**Lexeme 3: être**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈɛtʁ/ (fr-FR)
  1. [B1] ce qui vit, personne ou animal
     - example: Tous les êtres vivants ont besoin d'eau pour survivre.
     - collocations: être humain; être vivant
  2. [B2] personne, considérée avec sa nature profonde et son caractère
     - example: C'est un être sensible qui pleure facilement au cinéma.
     - collocations: un être cher; de tout son être; au plus profond de son être
- relations: derived_from être (VERB)

### 24. incroyable

**Lexeme 1: incroyable**, ADJ
- grammar: comparable True
- said: /ɛ̃.kʁwa.ˈjabl/ (fr-FR)
  1. [A2] si étonnant qu'on a du mal à penser que c'est vrai
     - example: Il est incroyable qu'il ait survécu à une chute pareille.
     - frames: il est incroyable que + subj; c'est incroyable de + inf
     - collocations: c'est incroyable; une histoire incroyable; à peine croyable; presque incroyable
     - opposites: croyable
  2. [A2] qui dépasse de loin l'ordinaire par sa qualité, sa taille ou sa force
     - example: Nous avons passé une soirée incroyable au bord du lac.
     - collocations: une chance incroyable; un succès incroyable; une vue incroyable; vraiment incroyable; un nombre incroyable
  3. [B1] qui surprend ou agace par une conduite étrange ou exagérée
     - example: Tu es incroyable, tu arrives encore en retard sans t'excuser !
     - labels: formality informal
     - grammar: position Pred, mainly; note said of a person
- relations: derived_from croyable (ADJ)

### 25. province

**Lexeme 1: province**, NOUN
- grammar: gender Fem; countability Count
- said: /pʁɔ.ˈvɛ̃s/ (fr-FR)
  1. [B1] grande division administrative d'un pays ou d'un empire
     - example: Après vingt ans dans la capitale, ils s'installent enfin en province.
     - collocations: province canadienne; province romaine; gouverneur de province; capitale de province; les provinces maritimes
  2. [B1] ensemble du pays en dehors de la capitale et de sa région
     - example: Chaque province possède son propre gouvernement et son propre parlement.
     - collocations: en province; ville de province; vivre en province; venir de province; monter de province
     - grammar: number Sing; note always with la, or in en province, de province

### 26. mourir

**Lexeme 1: mourir**, VERB
- grammar: conjugation 3; irregular True; valency Intr; auxiliary be
- said: /mu.ˈʁiʁ/ (fr-FR)
- forms: meurs [Mood=Ind, Tense=Pres, Person=1, Number=Sing] /ˈmœʁ/; meurt [Mood=Ind, Tense=Pres, Person=3, Number=Sing] /ˈmœʁ/; mourons [Mood=Ind, Tense=Pres, Person=1, Number=Plur] /mu.ˈʁɔ̃/; meurent [Mood=Ind, Tense=Pres, Person=3, Number=Plur] /ˈmœʁ/; mort [VerbForm=Part, Tense=Past] /ˈmɔʁ/; mourut [Mood=Ind, Tense=Past, Person=3, Number=Sing] /mu.ˈʁy/; mourrai [Mood=Ind, Tense=Fut, Person=1, Number=Sing] /muʁ.ˈʁe/; meure [Mood=Sub, Tense=Pres, Person=1, Number=Sing] /ˈmœʁ/
  1. [A2] cesser de vivre, pour un être humain, un animal ou une plante
     - example: Mon grand-père est mort paisiblement dans son sommeil l'hiver dernier.
     - frames: mourir de qqch
     - collocations: mourir de vieillesse; mourir de maladie; mourir jeune; mourir sur le coup; mourir dans son sommeil; mourir en paix; mourir d'un cancer
  2. [B1] ressentir très fortement une sensation ou un sentiment
     - example: Je meurs de faim, on passe à table tout de suite ?
     - frames: mourir de qqch; mourir d'envie de + inf
     - collocations: mourir de faim; mourir de soif; mourir de froid; mourir de rire; mourir de peur; mourir d'ennui; mourir d'envie
     - labels: figurative True, formality informal
  3. [B2] disparaître peu à peu, perdre toute force ou toute existence
     - example: Dans ces villages, les vieilles traditions meurent avec les anciens.
     - labels: figurative True

### 27. découverte

**Lexeme 1: découverte**, NOUN
- grammar: gender Masc; countability Count
- said: /de.ku.ˈvɛʁt/ (fr-FR)
  1. [B1] chose inconnue ou cachée que l'on trouve, ou action de la trouver
     - example: La découverte de la pénicilline a sauvé des millions de vies.
     - frames: la découverte de qqch
     - collocations: faire une découverte; une découverte scientifique; une découverte majeure; une découverte archéologique; une grande découverte
  2. [B1] première rencontre avec un lieu, une personne ou une activité jusque-là inconnus
     - example: Cet été, nous partons à la découverte de la montagne.
     - frames: à la découverte de qqch
     - collocations: partir à la découverte; aller à la découverte; une classe découverte
- relations: verbal_noun_of découvrir (VERB)

### 28. tiers

**Lexeme 1: tiers**, NOUN
- grammar: gender Masc; countability Count; note same form in the plural
- said: /ˈtjɛʁ/ (fr-FR)
  1. [A2] chacune des trois parties égales d'un tout
     - example: Un tiers des élèves habite à plus de dix kilomètres d'ici.
     - frames: le tiers de qqch; un tiers de + nom
     - collocations: les deux tiers; aux deux tiers; réduire d'un tiers; augmenter d'un tiers; un tiers temps
  2. [B2] personne extérieure à une affaire, à un contrat ou à un groupe
     - example: Ne communiquez jamais votre code secret à un tiers.
     - collocations: assurance au tiers; assuré au tiers; au profit d'un tiers; par un tiers
**Lexeme 2: tiers**, ADJ
- grammar: position Attr; comparable False
- said: /ˈtjɛʁ/ (fr-FR)
- forms: tierce [Gender=Fem, Number=Sing] /ˈtjɛʁs/; tierces [Gender=Fem, Number=Plur] /ˈtjɛʁs/
  1. [C1] qui n'est pas directement concerné par une affaire entre deux parties
     - example: Ils préfèrent régler ce conflit sans l'aide d'une tierce personne.
     - collocations: une tierce personne; un pays tiers; un État tiers; une partie tierce

### 29. docteur

**Lexeme 1: docteur**, NOUN
- grammar: gender Masc; countability Count; note for a woman, docteure is now common; doctoresse is dated
- said: /dɔk.ˈtœʁ/ (fr-FR)
  1. [A1] personne dont le métier est de soigner les malades
     - example: Si ta fièvre ne baisse pas demain, appelle le docteur.
     - collocations: aller chez le docteur; appeler le docteur; consulter un docteur; bonjour docteur
  2. [B2] personne qui a obtenu le plus haut diplôme délivré par une université
     - example: Elle est devenue docteur en physique après cinq ans de recherches.
     - frames: docteur en + nom
     - collocations: docteur en droit; docteur en médecine; docteur ès lettres
- relations: confusable médecin (NOUN): médecin names the profession; docteur is used as a title and to address a doctor, and also means a holder of a doctorate

### 30. mardi

**Lexeme 1: mardi**, NOUN
- grammar: gender Masc; countability Mass
- said: /maʁ.ˈdi/ (fr-FR)
  1. [A1] jour de la semaine qui vient après lundi et avant mercredi
     - example: Le marché du village a lieu tous les mardis matin.
     - collocations: le mardi; mardi prochain; mardi dernier; tous les mardis; mardi matin; mardi soir

### 31. quatrième

**Lexeme 1: quatrième**, ADJ
- grammar: comparable False
- said: /ka.tʁi.ˈjɛm/ (fr-FR)
  1. [A1] qui vient juste après le troisième dans une série ou un classement
     - example: Nous habitons au quatrième étage d'un immeuble sans ascenseur.
     - collocations: arriver quatrième; finir quatrième; le quatrième jour; le quatrième âge
- relations: derived_from quatre (NUM)
**Lexeme 2: quatrième**, NOUN
- grammar: gender Masc; countability Count
- said: /ka.tʁi.ˈjɛm/ (fr-FR)
  1. [A2] étage d'un immeuble situé au-dessus du troisième
     - example: Ils vivent au quatrième, juste au-dessus de chez nous.
     - collocations: au quatrième; monter au quatrième
  2. [B1] arrondissement d'une grande ville portant le numéro quatre
     - example: Mon bureau se trouve dans le quatrième, près de la Seine.
     - collocations: dans le quatrième
- relations: derived_from quatrième (ADJ)
**Lexeme 3: quatrième**, NOUN
- grammar: gender Fem; countability Count
- said: /ka.tʁi.ˈjɛm/ (fr-FR)
  1. [A2] classe du collège qui suit la cinquième et précède la troisième
     - example: Ma fille entre en quatrième à la rentrée de septembre.
     - collocations: en quatrième; entrer en quatrième; redoubler sa quatrième; un élève de quatrième
  2. [B1] vitesse d'un véhicule qui vient après la troisième
     - example: Sur cette route droite, tu peux passer la quatrième sans problème.
     - collocations: passer la quatrième; rouler en quatrième
- relations: derived_from quatrième (ADJ)

### 32. député

**Lexeme 1: député**, NOUN
- grammar: gender Masc; countability Count; note feminine: députée
- said: /de.py.ˈte/ (fr-FR)
  1. [B1] personne élue par les citoyens pour voter les lois dans une assemblée
     - example: Notre député a voté contre ce projet de loi hier soir.
     - frames: député de qqch
     - collocations: élire un député; député européen; député de la majorité; député de l'opposition; siéger comme député; député sortant; Chambre des députés
  2. [C1] personne envoyée par un groupe pour le représenter et parler en son nom
     - example: Chaque village envoya un député auprès du roi pour présenter ses plaintes.
     - labels: time dated
- relations: derived_from députer (VERB)

### 33. européen

**Lexeme 1: européen**, ADJ
- said: /ø.ʁɔ.pe.ˈɛ̃/ (fr-FR)
- forms: européenne [Gender=Fem, Number=Sing] /ø.ʁɔ.pe.ˈɛn/
  1. [A2] de l'Europe, de ses pays ou de ses habitants
     - example: Nous avons visité plusieurs capitales européennes pendant l'été.
     - collocations: pays européens; continent européen; à l'échelle européenne; capitale européenne
     - grammar: comparable False
  2. [B1] qui dépend de l'union politique et économique des pays d'Europe
     - example: Cette nouvelle directive européenne s'applique dans tous les États membres.
     - collocations: Union européenne; Parlement européen; Commission européenne; élections européennes; directive européenne; construction européenne
     - grammar: comparable False
  3. [C1] favorable à une union plus étroite entre les pays d'Europe
     - example: Ce candidat très européen défend une coopération renforcée entre les pays.
     - grammar: comparable True
- relations: adjective_of Europe (PROPN)
**Lexeme 2: Européen**, NOUN
- grammar: gender Masc; countability Count
- said: /ø.ʁɔ.pe.ˈɛ̃/ (fr-FR)
- forms: Européenne [Gender=Fem, Number=Sing] /ø.ʁɔ.pe.ˈɛn/
  1. [A2] personne qui habite l'Europe ou qui en est originaire
     - example: Beaucoup d'Européens passent leurs vacances d'été au bord de la mer.
- relations: derived_from européen (ADJ)

### 34. verre

**Lexeme 1: verre**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈvɛʁ/ (fr-FR)
  1. [A1] récipient dans lequel on boit, ou son contenu
     - example: Tu veux un verre d'eau fraîche avant de partir ?
     - frames: un verre de qqch
     - collocations: un verre à vin; un verre d'eau; prendre un verre; boire un verre; remplir un verre; lever son verre; vider son verre; offrir un verre
     - grammar: countability Count
  2. [A2] matière dure et transparente qui se casse facilement
     - example: Ce vase en verre vient d'un petit atelier de la région.
     - collocations: en verre; un éclat de verre; verre brisé; verre trempé; verre dépoli; souffler le verre; une bouteille en verre
     - grammar: countability Mass
  3. [B1] morceau transparent qui corrige la vue, placé dans des lunettes
     - example: Mes nouvelles lunettes ont des verres beaucoup plus fins.
     - collocations: verres de contact; verres correcteurs; verres progressifs
     - grammar: countability Count
- relations: confusable ver (NOUN): ver (worm) is pronounced the same as verre, as are vert and vers

### 35. théorie

**Lexeme 1: théorie**, NOUN
- grammar: gender Fem
- said: /te.ɔ.ˈʁi/ (fr-FR)
  1. [B1] ensemble organisé d'idées et de principes qui explique un domaine de faits
     - example: Les élèves étudient cette année la théorie de l'évolution des espèces.
     - frames: théorie de qqch; théorie selon laquelle + ind
     - collocations: élaborer une théorie; formuler une théorie; vérifier une théorie; confirmer une théorie; réfuter une théorie; une théorie scientifique; la théorie de la relativité
     - grammar: countability Count
  2. [B1] connaissance abstraite des règles, séparée de leur application concrète
     - example: En théorie, ce plan devrait marcher, mais je reste prudent.
     - collocations: en théorie; la théorie et la pratique; passer de la théorie à la pratique; cours de théorie
     - grammar: countability Mass
     - opposites: pratique
  3. [B2] idée personnelle qu'on propose pour expliquer un fait, sans preuve
     - example: J'ai ma petite théorie sur la raison de son départ.
     - frames: théorie sur qqch
     - collocations: avoir une théorie; une théorie du complot; une drôle de théorie
     - grammar: countability Count
  4. [C2] longue file de personnes ou de choses qui avancent l'une après l'autre
     - example: Une théorie de pèlerins montait lentement vers la chapelle.
     - frames: une théorie de + nom pluriel
     - labels: style literary
     - grammar: countability Count

### 36. concept

**Lexeme 1: concept**, NOUN
- grammar: gender Masc; countability Count
- said: /kɔ̃.ˈsɛpt/ (fr-FR)
  1. [B2] idée générale et abstraite que l'esprit se fait d'une chose
     - example: Pour un enfant, le concept de temps reste difficile à saisir.
     - frames: le concept de qqch
     - collocations: un concept clé; un concept abstrait; définir un concept; un concept de base
  2. [B2] idée de départ originale sur laquelle repose un produit, un commerce ou un projet
     - example: Ils ont lancé un nouveau concept de restaurant en plein centre-ville.
     - collocations: un nouveau concept; lancer un concept; un concept original; un concept innovant

### 37. certainement

**Lexeme 1: certainement**, ADV
- said: /sɛʁ.tɛn.ˈmɑ̃/ (fr-FR)
  1. [B1] selon toute probabilité, sans grand risque de se tromper
     - example: Avec ces embouteillages, nous arriverons certainement en retard ce soir.
     - collocations: très certainement
  2. [B1] utilisé pour dire oui avec force, pour accepter ou confirmer
     - example: « Certainement, je vous envoie le dossier dès demain », répond la secrétaire.
     - collocations: mais certainement; certainement pas
- relations: adverb_of certain (ADJ)

### 38. agir

**Lexeme 1: agir**, VERB
- grammar: conjugation 2; irregular False
- said: /a.ˈʒiʁ/ (fr-FR)
  1. [A2] faire quelque chose pour changer une situation, au lieu d'attendre
     - example: Face à la pollution, nous devons agir sans perdre de temps.
     - frames: agir pour + inf; agir contre qqch
     - collocations: agir vite; il est temps d'agir; agir sans tarder; agir ensemble; agir à temps
     - grammar: auxiliary have; valency Intr
  2. [B1] se conduire d'une certaine manière, montrer une certaine attitude
     - example: Tu as agi comme un enfant pendant toute la réunion.
     - frames: agir en + nom; agir comme + nom; agir envers qqn
     - collocations: agir en adulte; agir de bonne foi; agir en connaissance de cause; bien agir; mal agir
     - grammar: auxiliary have; valency Intr
  3. [B1] produire un effet sur une personne ou une chose
     - example: Ce médicament agit en moins d'une heure contre la douleur.
     - frames: agir sur qqn/qqch
     - collocations: agir rapidement; agir sur l'organisme
     - grammar: auxiliary have; valency Intr
  4. [B1] être question de quelque chose, avoir pour sujet ou pour but
     - example: Dans ce livre, il s'agit d'une famille pendant la guerre.
     - frames: il s'agit de qqch; il s'agit de + inf
     - collocations: de quoi s'agit-il
     - grammar: valency Impers; reflexive True; auxiliary be; note only as il s'agit, with impersonal il; se does not mean oneself

### 39. processus

**Lexeme 1: processus**, NOUN
- grammar: gender Masc; countability Count; note same form in the plural
- said: /pʁɔ.se.ˈsys/ (fr-FR)
  1. [B1] suite de changements ou d'étapes qui se déroulent dans le temps
     - example: Le processus de paix avance lentement malgré les tensions.
     - frames: processus de qqch
     - collocations: processus de paix; processus électoral; processus de décision; enclencher un processus; engager un processus; processus naturel; un long processus; en cours de processus
  2. [B2] ensemble ordonné d'opérations servant à obtenir un résultat
     - example: Notre entreprise a modernisé tout son processus de fabrication cette année.
     - collocations: processus de fabrication; processus de production; processus de recrutement; suivre un processus
- relations: confusable procédé (NOUN): procédé is a particular method or technique; processus is a sequence of steps or changes over time; confusable procès (NOUN): procès means a trial in court, not a process

### 40. perte

**Lexeme 1: perte**, NOUN
- grammar: gender Fem
- said: /ˈpɛʁt/ (fr-FR)
  1. [B1] fait de ne plus avoir ou de ne plus trouver une chose qu'on avait
     - example: La perte de mes clés m'a obligé à changer toutes les serrures.
     - frames: perte de qqch
     - collocations: une perte de temps; perte de poids; perte de mémoire; perte de connaissance; perte d'emploi; perte de contrôle; une perte d'argent
     - grammar: countability Count
  2. [B1] mort d'une personne, qui prive ses proches de sa présence
     - example: Toute la famille a été très affectée par la perte de leur grand-père.
     - frames: perte de qqn
     - collocations: une perte irréparable; une lourde perte; une perte cruelle
     - grammar: countability Count
  3. [B2] somme d'argent perdue dans une affaire ou une activité
     - example: Cette année, l'entreprise a encore enregistré de lourdes pertes.
     - collocations: vendre à perte; subir des pertes; enregistrer des pertes; une perte sèche; pertes et profits; pertes financières
     - grammar: countability Count; number Plur, mainly
     - opposites: bénéfice
  4. [B2] personnes tuées ou blessées lors d'une guerre ou d'une catastrophe
     - example: Les deux armées ont subi des pertes considérables pendant la bataille.
     - collocations: pertes humaines; de lourdes pertes; subir des pertes
     - grammar: countability Count; number Plur
  5. [C1] ruine complète, matérielle ou morale, d'une personne
     - example: Son orgueil démesuré finira par causer sa perte.
     - frames: causer la perte de qqn; jurer la perte de qqn
     - collocations: courir à sa perte
     - labels: style literary
     - grammar: countability Mass
- relations: verbal_noun_of perdre (VERB)

### 41. retraite

**Lexeme 1: retraite**, NOUN
- grammar: gender Masc
- said: /ʁə.ˈtʁɛt/ (fr-FR)
  1. [A2] arrêt définitif du travail à cause de l'âge, et période de vie qui suit
     - example: Mon grand-père profite de sa retraite pour voyager et jardiner.
     - collocations: prendre sa retraite; partir à la retraite; être à la retraite; l'âge de la retraite; une retraite anticipée; départ à la retraite; mettre à la retraite; maison de retraite
     - grammar: countability Mass
  2. [B1] somme d'argent versée chaque mois à une personne qui a cessé de travailler
     - example: Avec sa petite retraite, elle a du mal à payer son loyer.
     - collocations: toucher sa retraite; une petite retraite; caisse de retraite; retraite complémentaire; cotiser pour sa retraite; réforme des retraites
     - grammar: countability Count
  3. [B2] mouvement d'une armée qui recule devant l'ennemi
     - example: Face à l'attaque, les soldats ont dû battre en retraite.
     - collocations: battre en retraite; sonner la retraite; une retraite précipitée; couper la retraite
     - grammar: countability Count
  4. [B2] période passée loin du monde pour prier ou réfléchir
     - example: Chaque année, je fais une retraite de quelques jours dans un monastère.
     - collocations: faire une retraite; retraite spirituelle; partir en retraite
     - grammar: countability Count

### 42. préparer

**Lexeme 1: préparer**, VERB
- grammar: conjugation 1
- said: /pʁe.pa.ˈʁe/ (fr-FR)
  1. [A1] mettre une chose en état de servir, d'être utilisée ou d'être mangée
     - example: Ce soir, je prépare le dîner pendant que tu ranges la cuisine.
     - frames: préparer qqch; préparer qqch à qqn; préparer qqch pour qqn
     - collocations: préparer le repas; préparer le dîner; préparer un café; préparer sa valise; préparer ses affaires; préparer la chambre
     - grammar: auxiliary have; valency Tr
  2. [A2] organiser à l'avance ce qui doit se faire plus tard
     - example: Nous préparons notre voyage en Italie depuis plusieurs mois.
     - frames: préparer qqch; préparer qqch à qqn
     - collocations: préparer un voyage; préparer une fête; préparer une surprise; préparer un projet; préparer le terrain; préparer soigneusement
     - grammar: auxiliary have; valency Tr
  3. [A2] travailler et étudier pour réussir un examen ou un concours
     - example: Ma sœur prépare le bac et sort très peu cette année.
     - frames: préparer qqch
     - collocations: préparer un examen; préparer un concours; préparer le bac; préparer un diplôme
     - grammar: auxiliary have; valency Tr
  4. [B1] rendre quelqu'un capable de faire face à une situation
     - example: Cette formation prépare les élèves à la vie professionnelle.
     - frames: préparer qqn à qqch; préparer qqn à + inf
     - collocations: préparer psychologiquement
     - grammar: auxiliary have; valency Tr
  5. [A2] se mettre en état de faire quelque chose ou de sortir
     - example: Dépêche-toi de te préparer, le taxi arrive dans dix minutes.
     - frames: se préparer à qqch; se préparer à + inf; se préparer pour qqch
     - collocations: se préparer au pire; se préparer à partir; se préparer mentalement
     - grammar: valency Refl; reflexive True; auxiliary be
  6. [B2] être sur le point d'arriver, commencer à se former
     - example: Le ciel devient noir : un violent orage se prépare.
     - collocations: un orage se prépare
     - grammar: valency Refl; reflexive True; auxiliary be; note the subject is an event or thing; the pronoun does not mean oneself

### 43. domicile

**Lexeme 1: domicile**, NOUN
- grammar: gender Masc; countability Count
- said: /dɔ.mi.ˈsil/ (fr-FR)
  1. [B1] lieu où une personne habite de façon habituelle
     - example: Le médecin passe voir ma grand-mère à domicile chaque semaine.
     - collocations: à domicile; livraison à domicile; travailler à domicile; sans domicile fixe; regagner son domicile; quitter le domicile; le domicile familial; le domicile conjugal
  2. [B2] lieu d'habitation officiel d'une personne, reconnu par la loi
     - example: Veuillez indiquer votre domicile sur le formulaire de déclaration.
     - collocations: élire domicile; violation de domicile; changement de domicile; justificatif de domicile; adresse du domicile
     - labels: domain law

### 44. recevoir

**Lexeme 1: recevoir**, VERB
- grammar: conjugation 3; irregular True
- said: /ʁə.sə.ˈvwaʁ/ (fr-FR)
- forms: reçois [Mood=Ind, Tense=Pres, Person=1, Number=Sing] /ʁə.ˈswa/; reçoit [Mood=Ind, Tense=Pres, Person=3, Number=Sing] /ʁə.ˈswa/; recevons [Mood=Ind, Tense=Pres, Person=1, Number=Plur] /ʁə.sə.ˈvɔ̃/; reçoivent [Mood=Ind, Tense=Pres, Person=3, Number=Plur] /ʁə.ˈswav/; reçu [VerbForm=Part, Tense=Past] /ʁə.ˈsy/; reçut [Mood=Ind, Tense=Past, Person=3, Number=Sing] /ʁə.ˈsy/; recevrai [Mood=Ind, Tense=Fut, Person=1, Number=Sing] /ʁə.sə.ˈvʁe/; reçoive [Mood=Sub, Tense=Pres, Person=1, Number=Sing] /ʁə.ˈswav/
  1. [A1] se voir donner, envoyer ou adresser quelque chose par quelqu'un
     - example: J'ai reçu ta lettre hier matin, merci beaucoup !
     - frames: recevoir qqch de qqn
     - collocations: recevoir une lettre; recevoir un cadeau; recevoir un message; recevoir un appel; recevoir des nouvelles; recevoir un prix; recevoir une formation; recevoir un salaire
     - grammar: auxiliary have; valency Tr
  2. [A2] accueillir une personne chez soi ou dans son lieu de travail
     - example: Nous recevons quelques amis à dîner samedi soir.
     - frames: recevoir qqn
     - collocations: recevoir des amis; recevoir à dîner; recevoir chaleureusement; recevoir sur rendez-vous; recevoir des invités
     - grammar: auxiliary have; valency Both
  3. [B1] être touché ou atteint par un coup, un choc ou un objet
     - example: Le joueur a reçu un coup violent à la tête.
     - frames: recevoir qqch sur qqch
     - collocations: recevoir un coup; recevoir une gifle; recevoir une balle; recevoir la pluie
     - grammar: auxiliary have; valency Tr
  4. [B1] déclarer qu'un candidat a réussi un examen ou un concours
     - example: Ma sœur a été reçue au concours d'entrée de l'école.
     - frames: être reçu à qqch
     - collocations: être reçu à un examen; être reçu au concours; être reçu au baccalauréat
     - grammar: auxiliary have; valency Tr; note mostly in the passive: être reçu
  5. [B2] pouvoir contenir ou accueillir un certain nombre de personnes
     - example: Cette salle de spectacle peut recevoir plus de mille spectateurs.
     - grammar: auxiliary have; valency Tr
  6. [C1] toucher le sol après un saut ou une chute
     - example: Après son saut, la gymnaste s'est mal reçue sur la jambe gauche.
     - labels: domain sport
     - grammar: valency Refl; reflexive True; auxiliary be

### 45. aimer

**Lexeme 1: aimer**, VERB
- grammar: conjugation 1
- said: /e.ˈme/ (fr-FR)
  1. [A1] éprouver une tendresse profonde ou une passion pour quelqu'un
     - example: Je t'aime depuis le premier jour de notre rencontre.
     - frames: aimer qqn
     - collocations: je t'aime; aimer passionnément; aimer tendrement; aimer à la folie; aimer de tout son cœur
     - grammar: auxiliary have; valency Tr
     - opposites: haïr
  2. [A1] trouver agréable, avoir du goût pour une chose ou une activité
     - example: Mes enfants aiment beaucoup jouer dans le jardin après l'école.
     - frames: aimer qqch; aimer + inf; aimer que + subj
     - collocations: aimer bien; aimer beaucoup; aimer mieux; aimer la musique; aimer lire
     - grammar: auxiliary have; valency Tr
     - opposites: détester
  3. [A2] souhaiter, avoir envie d'une chose ou d'une action
     - example: J'aimerais visiter ce musée avant de quitter la ville.
     - frames: aimer + inf; aimer que + subj
     - collocations: j'aimerais bien
     - grammar: auxiliary have; valency Tr; note in the conditional
  4. [A2] éprouver l'un pour l'autre une tendresse profonde ou une passion
     - example: Ils s'aiment toujours autant après vingt ans de mariage.
     - grammar: valency Refl; reflexive True; auxiliary be; note se means each other
  5. [B2] avoir besoin de certaines conditions pour bien se développer
     - example: Cette plante aime le soleil et supporte mal le froid.
     - frames: aimer qqch
     - grammar: auxiliary have; valency Tr; note said of plants and animals

### 46. prince

**Lexeme 1: prince**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈpʁɛ̃s/ (fr-FR)
  1. [A2] homme d'une famille royale, en particulier le fils d'un roi ou d'une reine
     - example: Le jeune prince montera sur le trône à la mort de son père.
     - collocations: prince héritier; prince charmant; prince consort; le petit prince
  2. [B1] homme qui règne sur un État, souvent un petit pays souverain
     - example: Dans ce minuscule pays, c'est le prince qui nomme le gouvernement.
     - collocations: le prince régnant; le fait du prince
  3. [C1] homme considéré comme le premier ou le meilleur dans son domaine
     - example: On l'appelait déjà le prince des poètes de son vivant.
     - frames: prince de + nom
     - labels: figurative True, style literary

### 47. disposition

**Lexeme 1: disposition**, NOUN
- grammar: gender Fem; countability Count
- said: /dis.pɔ.zi.ˈsjɔ̃/ (fr-FR)
  1. [B1] possibilité d'utiliser librement une chose ou de faire appel à quelqu'un
     - example: Une voiture reste à votre disposition pendant tout le séjour.
     - frames: à la disposition de qqn; mettre qqch à la disposition de qqn
     - collocations: à disposition; mettre à disposition; se tenir à disposition; rester à votre disposition
     - grammar: number Sing
  2. [B1] mesures prises pour préparer une action ou faire face à une situation
     - example: Nous avons pris toutes les dispositions pour accueillir les invités.
     - frames: prendre des dispositions pour + inf
     - collocations: prendre des dispositions; prendre ses dispositions; les dispositions nécessaires
     - grammar: number Plur, mainly
  3. [B2] règle précise contenue dans une loi, un contrat ou un texte officiel
     - example: Cette disposition du contrat protège le locataire en cas de vente.
     - collocations: dispositions légales; disposition transitoire; les dispositions de la loi
     - labels: domain law
  4. [B2] manière dont les éléments d'un ensemble sont placés les uns par rapport aux autres
     - example: J'aime beaucoup la disposition des pièces dans cet appartement.
     - collocations: la disposition des lieux; la disposition des meubles
  5. [C1] capacité naturelle à réussir dans une activité
     - example: Dès l'enfance, elle montrait de réelles dispositions pour le dessin.
     - frames: des dispositions pour qqch
     - collocations: avoir des dispositions
     - grammar: number Plur, mainly
  6. [C1] humeur ou intention dans laquelle on se trouve à un moment donné
     - example: Le patron semble dans de bonnes dispositions ce matin, profites-en.
     - frames: dispositions à l'égard de qqn
     - collocations: être dans de bonnes dispositions; dans les meilleures dispositions
     - grammar: number Plur, mainly

### 48. moteur

**Lexeme 1: moteur**, NOUN
- grammar: gender Masc; countability Count
- said: /mɔ.ˈtœʁ/ (fr-FR)
  1. [A2] appareil qui transforme une énergie en mouvement pour faire marcher une machine
     - example: Ce matin, le moteur de ma voiture refuse de démarrer à cause du froid.
     - collocations: démarrer le moteur; couper le moteur; le moteur tourne; faire tourner le moteur; moteur électrique; moteur diesel; moteur à explosion; bateau à moteur
  2. [B2] personne ou chose qui pousse à agir et fait avancer quelque chose
     - example: Les exportations restent le principal moteur de la croissance du pays.
     - frames: le moteur de qqch
     - collocations: être le moteur de; moteur de la croissance; moteur de l'économie
     - labels: figurative True
**Lexeme 2: moteur**, ADJ
- grammar: comparable False
- said: /mɔ.ˈtœʁ/ (fr-FR)
- forms: motrice [Gender=Fem, Number=Sing] /mɔ.ˈtʁis/; motrices [Gender=Fem, Number=Plur] /mɔ.ˈtʁis/
  1. [B2] qui produit un mouvement ou le transmet à une machine
     - example: Cette voiture a quatre roues motrices pour rouler dans la neige.
     - collocations: force motrice; roues motrices; quatre roues motrices
  2. [B2] qui concerne les mouvements du corps commandés par les nerfs
     - example: Depuis son accident, elle souffre de troubles moteurs assez importants.
     - collocations: troubles moteurs; handicap moteur; nerf moteur
  3. [B2] qui pousse les autres à agir et fait avancer les choses
     - example: Les jeunes ont joué un rôle moteur dans ce mouvement de protestation.
     - collocations: rôle moteur; jouer un rôle moteur; élément moteur
     - labels: figurative True

### 49. opposition

**Lexeme 1: opposition**, NOUN
- grammar: gender Fem; countability Both
- said: /ɔ.po.zi.ˈsjɔ̃/ (fr-FR)
  1. [B1] refus d'accepter une décision ou une idée, et efforts pour l'empêcher
     - example: Le projet de loi rencontre une forte opposition chez les agriculteurs.
     - frames: opposition à qqch; opposition de qqn; être en opposition avec qqn
     - collocations: rencontrer une opposition; une forte opposition; sans opposition; entrer en opposition avec; une vive opposition
     - grammar: countability Mass, mainly
  2. [B1] ensemble des partis et des personnes politiques qui ne sont pas au pouvoir
     - example: Les députés de l'opposition ont quitté la salle pendant le vote.
     - collocations: chef de l'opposition; parti d'opposition; être dans l'opposition; passer dans l'opposition
     - labels: domain politics
     - grammar: number Sing; note usually with the definite article: l'opposition
  3. [B2] différence très nette entre deux choses ou deux idées contraires
     - example: On retrouve dans ce roman l'opposition entre la ville et la campagne.
     - frames: opposition entre qqch et qqch; par opposition à qqch
     - grammar: countability Count
  4. [B2] démarche auprès d'une banque pour bloquer un paiement ou une carte
     - example: J'ai perdu mon portefeuille, je dois vite faire opposition sur ma carte.
     - frames: faire opposition sur qqch; faire opposition à qqch
     - collocations: faire opposition; faire opposition à un chèque
     - grammar: countability Mass
- relations: verbal_noun_of opposer (VERB)

### 50. réduction

**Lexeme 1: réduction**, NOUN
- grammar: gender Fem
- said: /ʁe.dyk.ˈsjɔ̃/ (fr-FR)
  1. [B1] diminution de la quantité, de la taille ou de l'importance d'une chose
     - example: Le gouvernement promet une forte réduction des impôts l'an prochain.
     - frames: réduction de qqch
     - collocations: réduction des coûts; réduction des dépenses; réduction d'effectifs; réduction du temps de travail; réduction des émissions; forte réduction
     - grammar: countability Both
     - opposites: augmentation
  2. [A2] somme retirée du prix normal d'un produit ou d'un service
     - example: Avec ta carte d'étudiant, tu as droit à une réduction au cinéma.
     - frames: réduction sur qqch
     - collocations: avoir droit à une réduction; faire une réduction; obtenir une réduction; bénéficier d'une réduction; accorder une réduction; carte de réduction
     - grammar: countability Count
  3. [C1] reproduction d'un objet en plus petit, avec les mêmes proportions
     - example: Ce musée expose une réduction en bronze de la célèbre statue.
     - frames: réduction de qqch
     - collocations: en réduction
     - grammar: countability Count
- relations: verbal_noun_of réduire (VERB)

### 51. cheveu

**Lexeme 1: cheveu**, NOUN
- grammar: gender Masc; countability Count; note usually plural: les cheveux means the hair on the head
- said: /ʃə.ˈvø/ (fr-FR)
- forms: cheveux [Number=Plur] /ʃə.ˈvø/
  1. [A1] poil qui pousse sur la tête d'une personne
     - example: Je me fais couper les cheveux une fois par mois.
     - collocations: se faire couper les cheveux; se laver les cheveux; se brosser les cheveux; cheveux longs; cheveux courts; cheveux blancs; s'arracher les cheveux; couper les cheveux en quatre; tiré par les cheveux; avoir un cheveu sur la langue
     - grammar: number Plur, mainly
  2. [B2] quantité ou distance très petite, presque rien
     - example: Nous sommes passés à un cheveu de la catastrophe hier soir.
     - frames: à un cheveu de qqch; à un cheveu de + inf
     - collocations: il s'en est fallu d'un cheveu; à un cheveu près
     - labels: figurative True
     - grammar: number Sing
