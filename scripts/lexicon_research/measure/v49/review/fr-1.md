# French: entries to check (1 of 1)

These are entries of a learner's dictionary of French, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of French.

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

### 1. défaire

**Lexeme 1: défaire**, VERB
- grammar: conjugation 3; irregular True; note conjugated like faire
- said: /de.ˈfɛʁ/ (fr-FR)
- forms: défais [Person=1, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /de.ˈfɛ/; défait [Person=3, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /de.ˈfɛ/; défaisons [Person=1, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /de.fə.ˈzɔ̃/; défont [Person=3, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /de.ˈfɔ̃/; défait [Tense=Past, VerbForm=Part] /de.ˈfɛ/; défit [Person=3, Number=Sing, Tense=Past, Mood=Ind, VerbForm=Fin] /de.ˈfi/; déferai [Person=1, Number=Sing, Tense=Fut, Mood=Ind, VerbForm=Fin] /de.fə.ˈʁe/; défasse [Person=1, Number=Sing, Tense=Pres, Mood=Sub, VerbForm=Fin] /de.ˈfas/
  1. [B1] remettre dans son état d'avant ce qui était monté, noué ou arrangé
     - example: Je défais toujours mes valises dès que j'arrive à l'hôtel.
     - collocations: défaire ses valises; défaire un nœud; défaire le lit; défaire ses lacets; défaire un paquet; défaire sa ceinture
     - grammar: auxiliary have; valency Tr
  2. [B2] se séparer d'une personne ou d'une chose dont on ne veut plus
     - example: Il a eu du mal à se défaire de ses vieilles habitudes.
     - frames: se défaire de qqch/qqn
     - collocations: se défaire d'une habitude
     - grammar: valency Refl; reflexive True; auxiliary be
  3. [C1] vaincre une armée ou un adversaire dans un combat
     - example: Les troupes du roi défirent l'ennemi près de la rivière.
     - labels: style literary
     - grammar: auxiliary have; valency Tr
- relations: derived_from faire (VERB)

### 2. bonnet

**Lexeme 1: bonnet**, NOUN
- grammar: gender Masc; countability Count
- said: /bɔ.ˈnɛ/ (fr-FR)
  1. [A2] coiffure souple et sans bord qui couvre la tête
     - example: Mets ton bonnet et tes gants, il gèle ce matin.
     - collocations: bonnet de laine; bonnet de bain; bonnet de nuit; mettre un bonnet; porter un bonnet; enfoncer son bonnet; bonnet phrygien
  2. [B2] chacune des deux parties d'un soutien-gorge qui couvrent les seins
     - example: Ce modèle de maillot existe avec des bonnets de plusieurs tailles.
     - collocations: taille de bonnet

### 3. malédiction

**Lexeme 1: malédiction**, NOUN
- grammar: gender Fem; countability Count
- said: /ma.le.dik.ˈsjɔ̃/ (fr-FR)
  1. [B2] paroles magiques qui appellent un sort terrible sur une personne
     - example: La sorcière a jeté une malédiction sur tout le village.
     - frames: une malédiction sur qqn
     - collocations: jeter une malédiction; lancer une malédiction; lever une malédiction; briser une malédiction; conjurer une malédiction
     - opposites: bénédiction
  2. [C1] destin terrible qui semble poursuivre une personne ou un groupe
     - example: Cette famille semble poursuivie par une véritable malédiction depuis des années.
     - frames: la malédiction de qqch
     - collocations: une véritable malédiction; être frappé par la malédiction; briser la malédiction

### 4. oriental

**Lexeme 1: oriental**, ADJ
- said: /ɔ.ʁjɑ̃.ˈtal/ (fr-FR)
- forms: orientaux [Gender=Masc, Number=Plur] /ɔ.ʁjɑ̃.ˈto/
  1. [B1] situé du côté où le soleil se lève, à l'est
     - example: Nous passerons nos vacances sur la côte orientale de l'île.
     - collocations: l'Europe orientale; la partie orientale; la côte orientale; la frontière orientale
     - opposites: occidental
  2. [B1] propre aux pays d'Asie et du monde arabe, ou qui en vient
     - example: Ce petit restaurant propose une cuisine orientale très parfumée.
     - collocations: la cuisine orientale; la danse orientale; les langues orientales; la musique orientale; les pays orientaux
     - opposites: occidental
- relations: adjective_of Orient (NOUN)
**Lexeme 2: Oriental**, NOUN
- grammar: gender Masc; countability Count; note feminine Orientale; usually in the plural
- said: /ɔ.ʁjɑ̃.ˈtal/ (fr-FR)
- forms: Orientaux [Gender=Masc, Number=Plur] /ɔ.ʁjɑ̃.ˈto/; Orientale [Gender=Fem, Number=Sing] /ɔ.ʁjɑ̃.ˈtal/
  1. [B2] personne qui vient des pays d'Asie ou du monde arabe
     - example: Les voyageurs d'autrefois décrivaient les Orientaux avec beaucoup d'idées fausses.
- relations: derived_from oriental (ADJ)

### 5. vocation

**Lexeme 1: vocation**, NOUN
- grammar: gender Fem; countability Count
- said: /vɔ.ka.ˈsjɔ̃/ (fr-FR)
  1. [B1] fort désir de faire un métier ou de mener une certaine vie
     - example: Depuis l'enfance, il sentait une vraie vocation pour la médecine.
     - frames: vocation pour qqch; avoir la vocation de + inf
     - collocations: avoir la vocation; trouver sa vocation; manquer sa vocation; une vocation tardive; une vocation religieuse; susciter des vocations
  2. [B2] rôle principal pour lequel un lieu ou un organisme existe
     - example: Cette association a pour vocation d'aider les familles en difficulté.
     - frames: avoir pour vocation de + inf; à vocation + adj
     - collocations: avoir pour vocation; à vocation sociale; vocation première
- relations: confusable vacation (NOUN): looks and sounds alike; French vacation is a work session or fee, not a holiday as in English

### 6. joueuse

**Lexeme 1: joueuse**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈʒwøz/ (fr-FR), /ʒu.ˈøz/ (fr-FR, careful)
  1. [A2] femme ou fille qui participe à un sport ou à une partie
     - example: Cette joueuse de tennis a remporté trois tournois cette saison.
     - frames: joueuse de + nom
     - collocations: joueuse de tennis; une joueuse professionnelle; mauvaise joueuse; bonne joueuse; la meilleure joueuse
  2. [B2] femme qui aime miser de l'argent dans l'espoir de gagner
     - example: Sa grand-mère était une joueuse invétérée qui fréquentait les casinos.
     - collocations: une joueuse invétérée
  3. [B2] femme qui sait se servir d'un instrument de musique
     - example: Une joueuse de harpe accompagnait la chorale pendant le concert.
     - frames: joueuse de + nom
- relations: feminine_of joueur (NOUN)

### 7. vider

**Lexeme 1: vider**, VERB
- grammar: conjugation 1
- said: /vi.ˈde/ (fr-FR)
  1. [A2] enlever tout ce qui se trouve dans un récipient ou un lieu
     - example: Avant de partir, vide bien tes poches sur la table.
     - frames: vider qqch; vider qqch de qqch; vider qqch dans qqch
     - collocations: vider son verre; vider ses poches; vider la poubelle; vider le lave-vaisselle; vider les lieux; vider son sac
     - grammar: auxiliary have; valency Tr
     - opposites: remplir
  2. [B1] perdre peu à peu son contenu ou les personnes présentes
     - example: Après le concert, la salle se vide en quelques minutes.
     - frames: se vider de qqch
     - collocations: se vider de son sang
     - grammar: valency Refl; auxiliary be
     - opposites: remplir
  3. [B2] faire sortir quelqu'un de force d'un endroit ou d'un groupe
     - example: Le videur l'a vidé de la boîte à deux heures du matin.
     - frames: vider qqn; vider qqn de qqch
     - collocations: se faire vider
     - labels: formality informal
     - grammar: auxiliary have; valency Tr
  4. [B2] fatiguer beaucoup, enlever toute force physique ou morale
     - example: Cette semaine de travail m'a complètement vidé.
     - frames: vider qqn
     - collocations: être vidé; complètement vidé
     - labels: formality informal, figurative True
     - grammar: auxiliary have; valency Tr
  5. [B2] retirer l'intérieur d'un animal avant de le cuisiner
     - example: Le poissonnier peut vider le poisson si vous le souhaitez.
     - collocations: vider un poisson; vider une volaille
     - labels: domain cooking
     - grammar: auxiliary have; valency Tr
- relations: derived_from vide (ADJ)

### 8. prédire

**Lexeme 1: prédire**, VERB
- grammar: conjugation 3; irregular True; valency Tr; auxiliary have; note conjugated like dire, but vous prédisez in the present
- said: /pʁe.ˈdiʁ/ (fr-FR)
- forms: prédis [Person=1, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /pʁe.ˈdi/; prédit [Person=3, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /pʁe.ˈdi/; prédisons [Person=1, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /pʁe.di.ˈzɔ̃/; prédisez [Person=2, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /pʁe.di.ˈze/; prédisent [Person=3, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /pʁe.ˈdiz/; prédit [Tense=Past, VerbForm=Part] /pʁe.ˈdi/; prédit [Person=3, Number=Sing, Tense=Past, Mood=Ind, VerbForm=Fin] /pʁe.ˈdi/; prédirai [Person=1, Number=Sing, Tense=Fut, Mood=Ind, VerbForm=Fin] /pʁe.di.ˈʁe/; prédise [Person=1, Number=Sing, Tense=Pres, Mood=Sub, VerbForm=Fin] /pʁe.ˈdiz/
  1. [B1] annoncer à l'avance ce qui va arriver, par intuition, calcul ou magie
     - example: Les experts prédisent une hausse des prix pour l'année prochaine.
     - frames: prédire qqch; prédire qqch à qqn; prédire que + ind
     - collocations: prédire l'avenir; prédire le futur; prédire la victoire; prédire la fin du monde; difficile à prédire
- relations: confusable prévoir (VERB): prévoir also means to plan or provide for something, prédire only to announce what will happen

### 9. tri

**Lexeme 1: tri**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈtʁi/ (fr-FR)
  1. [B1] séparation d'éléments en groupes, pour les classer ou choisir les meilleurs
     - example: Chaque soir, nous faisons le tri des déchets avant de sortir les poubelles.
     - frames: tri de qqch
     - collocations: faire le tri; tri sélectif; tri des déchets; centre de tri; tri postal; faire un tri
- relations: verbal_noun_of trier (VERB)

### 10. dessous

**Lexeme 1: dessous**, ADV
- grammar: invariable True
- said: /də.ˈsu/ (fr-FR)
  1. [B1] à un endroit plus bas qu'une chose, ou caché par elle
     - example: Soulève le tapis, la clé est cachée dessous.
     - collocations: en dessous; là-dessous; par-dessous; sens dessus dessous; juste dessous
     - opposites: dessus
- relations: confusable dessus (ADV): close sound (/də.sy/ vs /də.su/) and opposite meaning
**Lexeme 2: dessous**, NOUN
- grammar: gender Masc; countability Count
- said: /də.ˈsu/ (fr-FR)
  1. [B1] la face la plus basse d'un objet, celle tournée vers le sol
     - example: Le dessous de la table est couvert de poussière.
     - frames: le dessous de qqch
     - collocations: le dessous de la table; le dessous du pied; avoir le dessous
     - opposites: dessus
  2. [B2] vêtements intimes portés directement sur la peau, surtout par les femmes
     - example: Elle a acheté des dessous en dentelle pour son anniversaire.
     - collocations: des dessous en dentelle; des dessous chics; des dessous féminins
     - grammar: number Plur
  3. [C1] les côtés cachés et secrets d'une affaire ou d'une situation
     - example: Le journaliste a découvert les dessous de ce scandale politique.
     - frames: les dessous de qqch
     - collocations: les dessous d'une affaire; le dessous des cartes; connaître les dessous
     - labels: figurative True
     - grammar: number Plur
- relations: derived_from dessous (ADV); confusable dessus (NOUN): close sound (/də.sy/ vs /də.su/) and opposite meaning

### 11. soi

**Lexeme 1: soi**, PRON
- grammar: invariable True; note stressed reflexive of the 3rd person; mostly after a preposition, referring to on, chacun, personne, etc.
- said: /ˈswa/ (fr-FR)
  1. [B1] sert à désigner la personne même dont on parle, quand elle reste générale ou indéterminée
     - example: Après une longue journée, on aime rentrer tranquillement chez soi.
     - collocations: chez soi; chacun pour soi; cela va de soi; en soi; la confiance en soi; être sûr de soi; prendre sur soi; la maîtrise de soi; garder pour soi; revenir à soi
**Lexeme 2: soi**, NOUN
- grammar: gender Masc; number Sing; note usually with the article: le soi
- said: /ˈswa/ (fr-FR)
  1. [C1] ce qui fait qu'une personne a conscience d'être elle-même et unique
     - example: Ce livre de psychologie étudie la construction du soi chez l'adolescent.
     - labels: domain psychology
- relations: derived_from soi (PRON)

### 12. labo

**Lexeme 1: labo**, NOUN
- grammar: gender Masc; countability Count; note short form of laboratoire
- said: /la.ˈbo/ (fr-FR)
  1. [B1] lieu équipé où des scientifiques font des recherches et des expériences
     - example: Elle passe toutes ses journées au labo à faire des analyses.
     - collocations: au labo; labo photo; labo d'analyses; labo de langues; labo pharmaceutique; travailler dans un labo
     - labels: formality informal
- relations: derived_from laboratoire (NOUN): clipped form

### 13. martial

**Lexeme 1: martial**, ADJ
- said: /maʁ.ˈsjal/ (fr-FR)
- forms: martiaux [Gender=Masc, Number=Plur] /maʁ.ˈsjo/
  1. [B2] qui concerne la guerre, l'armée ou le combat
     - example: Le gouvernement vient de décréter la loi martiale dans tout le pays.
     - collocations: loi martiale; cour martiale; arts martiaux; décréter la loi martiale; passer en cour martiale; pratiquer un art martial
     - grammar: position Attr; comparable False; note mostly in set phrases, after the noun
  2. [C1] qui a l'allure décidée et énergique d'un soldat
     - example: Mon grand-père traverse toujours la place d'un pas martial.
     - collocations: air martial; pas martial; ton martial; allure martiale; musique martiale
     - grammar: comparable True

### 14. fier

**Lexeme 1: fier**, ADJ
- grammar: comparable True
- said: /ˈfjɛʁ/ (fr-FR)
- forms: fière [Gender=Fem, Number=Sing] /ˈfjɛʁ/
  1. [A2] content et satisfait de ce que l'on a fait, de ce que l'on a ou de quelqu'un
     - example: Mes parents sont très fiers de ma réussite au concours.
     - frames: fier de qqn/qqch; fier de + inf; fier que + subj
     - collocations: être fier de soi; fier comme un paon; il n'y a pas de quoi être fier
     - opposites: honteux
  2. [B1] qui se croit supérieur aux autres et le montre dans son attitude
     - example: Depuis sa promotion, elle est devenue fière et distante avec nous.
     - collocations: un air fier; faire le fier
     - opposites: modeste
  3. [B2] qui a beaucoup de dignité et refuse de s'abaisser devant les autres
     - example: Il est trop fier pour demander de l'aide à ses voisins.
     - frames: trop fier pour + inf
     - collocations: un peuple fier
**Lexeme 2: fier**, VERB
- grammar: conjugation 1; valency Refl; reflexive True; auxiliary be; note used only as se fier; imperfect nous nous fiions
- said: /ˈfje/ (fr-FR)
  1. [B1] compter sur une personne ou une chose en croyant qu'elle ne trompera pas
     - example: Je ne me fie jamais aux prévisions météo de ce site.
     - frames: se fier à qqn/qqch
     - collocations: se fier aux apparences; se fier à son instinct; ne se fier à personne; se fier à sa mémoire
     - grammar: valency Refl; auxiliary be
- relations: confusable fier (ADJ): same spelling as the adjective fier (proud), but pronounced /fje/, not /fjɛʁ/

### 15. méthode

**Lexeme 1: méthode**, NOUN
- grammar: gender Fem; countability Both
- said: /me.ˈtɔd/ (fr-FR)
  1. [A2] ensemble ordonné de moyens utilisés pour obtenir un résultat
     - example: Cette nouvelle méthode permet d'apprendre le vocabulaire beaucoup plus vite.
     - frames: méthode de + nom; méthode pour + inf
     - collocations: appliquer une méthode; employer une méthode; changer de méthode; une méthode efficace; méthode de travail; méthode scientifique
     - grammar: countability Count
  2. [B2] manière ordonnée et logique d'agir, de travailler ou de penser
     - example: Tu réussirais mieux si tu travaillais avec un peu plus de méthode.
     - collocations: avec méthode; avoir de la méthode; manquer de méthode
     - grammar: countability Mass
  3. [B1] livre conçu pour apprendre pas à pas une langue ou un art
     - example: J'ai acheté une méthode de guitare pour les débutants.
     - frames: méthode de + nom
     - collocations: méthode de lecture; méthode de langue
     - grammar: countability Count

### 16. ban

**Lexeme 1: ban**, NOUN
- grammar: gender Masc
- said: /ˈbɑ̃/ (fr-FR)
  1. [C1] situation d'une personne rejetée et exclue par un groupe ou une communauté
     - example: Après ce scandale, il s'est retrouvé au ban de la profession.
     - frames: mettre qqn au ban de qqch
     - collocations: être au ban de la société; mettre au ban; au ban de
     - grammar: countability Mass; note almost always in au ban de
  2. [B2] applaudissements rythmés lancés ensemble en l'honneur de quelqu'un
     - example: Faisons tous un ban pour les jeunes mariés qui arrivent !
     - frames: un ban pour qqn
     - collocations: faire un ban; un triple ban
     - grammar: countability Count
  3. [C1] roulement de tambour ou sonnerie annonçant le début ou la fin d'une cérémonie
     - example: Le clairon ouvre le ban avant la remise des médailles.
     - collocations: ouvrir le ban; fermer le ban
     - labels: domain military
     - grammar: countability Count
  4. [C1] annonce officielle d'un mariage prévu, affichée à la mairie ou à l'église
     - example: Les bans sont publiés à la mairie plusieurs jours avant la cérémonie.
     - collocations: publier les bans; la publication des bans
     - grammar: countability Count; number Plur, mainly
  5. [C2] appel par lequel un seigneur réunissait ses vassaux pour la guerre
     - example: Le roi convoqua le ban et l'arrière-ban pour défendre le royaume.
     - collocations: le ban et l'arrière-ban; convoquer le ban
     - labels: time historical
     - grammar: countability Count

### 17. hop

**Lexeme 1: hop**, INTJ
- grammar: h_aspire True
- said: /ˈɔp/ (fr-FR)
  1. [B1] s'emploie pour pousser quelqu'un à bouger, à sauter ou à agir vite
     - example: Allez, hop, tout le monde dans la voiture, on part !
     - collocations: allez, hop; hop là
     - labels: formality informal
  2. [B1] s'emploie pour souligner qu'une action se fait vite et facilement
     - example: Tu appuies sur ce bouton et hop, la machine démarre.
     - collocations: et hop
     - labels: formality informal

### 18. ressource

**Lexeme 1: ressource**, NOUN
- grammar: gender Fem
- said: /ʁə.ˈsuʁs/ (fr-FR)
  1. [B1] richesses et moyens matériels, naturels ou humains dont on dispose
     - example: Notre pays doit mieux protéger ses ressources en eau potable.
     - collocations: ressources naturelles; ressources humaines; ressources énergétiques; épuiser les ressources; gérer les ressources; une ressource rare; ressources pédagogiques
     - grammar: countability Count; number Plur, mainly
  2. [B1] argent dont une personne ou une famille dispose pour vivre
     - example: Avec des ressources aussi modestes, ils peinent à payer le loyer.
     - collocations: sans ressources; ressources financières; ressources modestes; conditions de ressources; plafond de ressources
     - grammar: countability Count; number Plur
  3. [B2] moyen auquel on peut recourir pour sortir d'une difficulté
     - example: Je n'ai d'autre ressource que d'accepter sa proposition.
     - frames: n'avoir d'autre ressource que de + inf; avoir la ressource de + inf
     - collocations: en dernière ressource; la seule ressource
     - grammar: countability Count
  4. [C1] capacité à trouver des solutions et à réagir face aux difficultés
     - example: Ne t'inquiète pas pour elle, elle a de la ressource.
     - collocations: avoir de la ressource; des ressources insoupçonnées; plein de ressources
     - grammar: countability Mass, mainly

### 19. aîné

**Lexeme 1: aîné**, ADJ
- grammar: comparable False
- said: /ɛ.ˈne/ (fr-FR), /e.ˈne/ (fr-FR)
- spellings: aîné; ainé
  1. [A2] né le premier parmi les enfants d'une même famille
     - example: Ma sœur aînée habite maintenant à l'étranger avec ses enfants.
     - collocations: fils aîné; fille aînée; frère aîné; sœur aînée
     - opposites: cadet
**Lexeme 2: aîné**, NOUN
- grammar: gender Masc; countability Count; note feminine aînée for a woman or girl
- said: /ɛ.ˈne/ (fr-FR), /e.ˈne/ (fr-FR)
- spellings: aîné; ainé
- forms: aînée [Gender=Fem, Number=Sing] /ɛ.ˈne/
  1. [B1] enfant né le premier dans une famille
     - example: C'est toujours l'aîné qui doit surveiller les plus petits.
     - frames: l'aîné de qqn
     - collocations: l'aîné de la famille; l'aîné des enfants
     - opposites: cadet
  2. [B2] personne plus âgée qu'une autre
     - example: Elle est mon aînée de cinq ans, mais nous sommes très proches.
     - frames: être l'aîné de qqn
     - opposites: cadet
  3. [B2] personnes âgées, en particulier celles qui ne travaillent plus
     - example: La ville organise chaque semaine des activités gratuites pour les aînés.
     - regions: fr-CA mainly
     - grammar: number Plur, mainly
- relations: derived_from aîné (ADJ)

### 20. interdire

**Lexeme 1: interdire**, VERB
- grammar: conjugation 3; irregular True
- said: /ɛ̃.tɛʁ.ˈdiʁ/ (fr-FR)
- forms: interdis [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ɛ̃.tɛʁ.ˈdi/; interdit [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ɛ̃.tɛʁ.ˈdi/; interdisons [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /ɛ̃.tɛʁ.di.ˈzɔ̃/; interdisez [Mood=Ind, Tense=Pres, Person=2, Number=Plur, VerbForm=Fin] /ɛ̃.tɛʁ.di.ˈze/; interdisent [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ɛ̃.tɛʁ.ˈdiz/; interdit [Tense=Past, VerbForm=Part] /ɛ̃.tɛʁ.ˈdi/; interdit [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /ɛ̃.tɛʁ.ˈdi/; interdirai [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] /ɛ̃.tɛʁ.di.ˈʁe/; interdise [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ɛ̃.tɛʁ.ˈdiz/
  1. [A2] ne pas permettre, par une règle ou un ordre, de faire quelque chose
     - example: Le médecin m'interdit de manger trop de sel pendant un mois.
     - frames: interdire qqch à qqn; interdire à qqn de + inf; il est interdit de + inf
     - collocations: interdire formellement; interdire strictement; interdire l'accès; interdire l'entrée; interdire la vente; interdire de fumer
     - grammar: auxiliary have; valency Tr
     - opposites: permettre, autoriser
  2. [B2] rendre une chose impossible pour quelqu'un, l'empêcher de se faire
     - example: Son état de santé lui interdit tout effort physique cet hiver.
     - frames: interdire qqch à qqn
     - grammar: auxiliary have; valency Tr
  3. [B2] se refuser à soi-même une chose, décider de ne pas la faire
     - example: Je m'interdis de regarder mon téléphone pendant les repas.
     - frames: s'interdire qqch; s'interdire de + inf
     - grammar: valency Refl; reflexive True; auxiliary be

### 21. mouton

**Lexeme 1: mouton**, NOUN
- grammar: gender Masc; countability Both
- said: /mu.ˈtɔ̃/ (fr-FR)
  1. [A1] animal domestique couvert de laine, qu'on élève en troupeau
     - example: Dans cette vallée, les bergers gardent encore leurs moutons en montagne.
     - collocations: un troupeau de moutons; élever des moutons; garder les moutons; tondre un mouton; compter les moutons; laine de mouton; doux comme un mouton; le mouton noir
     - grammar: countability Count
  2. [A2] viande d'un animal domestique couvert de laine, qu'on mange cuite
     - example: Ce soir, nous mangeons du mouton aux haricots blancs.
     - collocations: gigot de mouton; côtelette de mouton; ragoût de mouton; épaule de mouton
     - grammar: countability Mass
  3. [B2] personne docile qui fait ce que font les autres sans réfléchir
     - example: Ne sois pas un mouton, fais tes propres choix !
     - collocations: mouton de Panurge; suivre comme des moutons
     - labels: figurative True
     - grammar: countability Count
  4. [C1] petit amas de poussière qui se forme sous les meubles
     - example: En déplaçant le lit, j'ai trouvé plein de moutons dessous.
     - collocations: moutons de poussière
     - labels: formality informal
     - grammar: countability Count; number Plur, mainly

### 22. princesse

**Lexeme 1: princesse**, NOUN
- grammar: gender Fem; countability Count
- said: /pʁɛ̃.ˈsɛs/ (fr-FR)
  1. [A2] fille d'un roi ou d'une reine, ou femme d'un fils de roi
     - example: La petite fille rêve de devenir une princesse dans un château.
     - collocations: princesse héritière; robe de princesse; une princesse de conte de fées; traiter comme une princesse; aux frais de la princesse
- relations: feminine_of prince (NOUN)

### 23. ciné

**Lexeme 1: ciné**, NOUN
- grammar: gender Masc; countability Both
- said: /si.ˈne/ (fr-FR)
  1. [A2] salle où l'on paie pour voir des films projetés sur un grand écran
     - example: Ce soir, on va au ciné voir le dernier film d'animation.
     - collocations: aller au ciné; se faire un ciné; une place de ciné; une sortie au ciné
     - labels: formality informal
     - grammar: countability Count
  2. [B1] art et industrie qui consistent à réaliser des films
     - example: Elle adore le ciné italien des années soixante.
     - collocations: le ciné français; faire du ciné
     - labels: formality informal
     - grammar: countability Mass
- relations: derived_from cinéma (NOUN)

### 24. blé

**Lexeme 1: blé**, NOUN
- grammar: gender Masc; countability Mass, mainly
- said: /ˈble/ (fr-FR)
  1. [A2] céréale dont les grains servent surtout à faire la farine du pain
     - example: Dans cette plaine, les agriculteurs cultivent surtout du blé et du maïs.
     - collocations: champ de blé; farine de blé; épi de blé; blé dur; cultiver le blé; récolter le blé; la moisson du blé
  2. [B2] argent qu'une personne possède ou gagne
     - example: Avec ce nouveau boulot, tu vas enfin te faire du blé.
     - collocations: avoir du blé; se faire du blé; plein de blé
     - labels: formality informal

### 25. soumettre

**Lexeme 1: soumettre**, VERB
- grammar: conjugation 3; irregular True; note conjugated like mettre
- said: /su.ˈmɛtʁ/ (fr-FR)
- forms: soumets [Person=1, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /su.ˈmɛ/; soumet [Person=3, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /su.ˈmɛ/; soumettons [Person=1, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /su.mɛ.ˈtɔ̃/; soumettent [Person=3, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /su.ˈmɛt/; soumis [VerbForm=Part, Tense=Past] /su.ˈmi/; soumit [Person=3, Number=Sing, Tense=Past, Mood=Ind, VerbForm=Fin] /su.ˈmi/; soumettrai [Person=1, Number=Sing, Tense=Fut, Mood=Ind, VerbForm=Fin] /su.mɛ.ˈtʁe/; soumette [Person=1, Number=Sing, Tense=Pres, Mood=Sub, VerbForm=Fin] /su.ˈmɛt/
  1. [B2] présenter une chose à quelqu'un pour avoir son avis ou sa décision
     - example: Nous soumettrons notre projet au directeur avant la fin du mois.
     - frames: soumettre qqch à qqn
     - collocations: soumettre un projet; soumettre une proposition; soumettre au vote; soumettre une candidature; soumettre une idée
     - grammar: auxiliary have; valency Tr
  2. [B2] faire subir à quelqu'un ou à quelque chose une épreuve, une règle ou une action
     - example: Tous les candidats sont soumis à un examen médical obligatoire.
     - frames: soumettre qqn/qqch à qqch
     - collocations: soumettre à un examen; soumettre à l'impôt; soumettre à des tests; être soumis à une autorisation
     - grammar: auxiliary have; valency Tr
  3. [C1] obliger un peuple ou un groupe à obéir, par la force
     - example: L'armée a mis plusieurs années à soumettre les tribus de la montagne.
     - collocations: soumettre un peuple; soumettre les rebelles
     - grammar: auxiliary have; valency Tr
  4. [B2] accepter d'obéir à une autorité, à une règle ou à une décision
     - example: Il refuse de se soumettre aux règles de l'entreprise.
     - frames: se soumettre à qqn/qqch
     - collocations: se soumettre à la loi; se soumettre à une décision; se soumettre à l'autorité
     - grammar: valency Refl; reflexive True; auxiliary be

### 26. détente

**Lexeme 1: détente**, NOUN
- grammar: gender Fem
- said: /de.ˈtɑ̃t/ (fr-FR)
  1. [B1] moment de repos où le corps et l'esprit se reposent du stress
     - example: Après une longue semaine, ce week-end à la mer sera une vraie détente.
     - collocations: un moment de détente; un espace de détente; la détente musculaire; se faire plaisir en toute détente
     - grammar: countability Mass, mainly
     - opposites: tension
  2. [B2] diminution des conflits et des menaces entre des pays ou des groupes
     - example: Les deux gouvernements cherchent une détente après des mois de crise.
     - collocations: la détente internationale; une politique de détente; un signe de détente
     - grammar: countability Mass
     - opposites: tension
  3. [B2] petite pièce d'une arme à feu qu'on presse avec le doigt pour tirer
     - example: Le chasseur appuie doucement sur la détente sans quitter la cible des yeux.
     - collocations: appuyer sur la détente; presser la détente; le doigt sur la détente
     - grammar: countability Count
  4. [C1] capacité des jambes à pousser fort pour sauter haut ou loin
     - example: Ce basketteur possède une détente impressionnante pour sa taille.
     - collocations: avoir de la détente; une détente verticale
     - labels: domain sport
     - grammar: countability Mass
- relations: derived_from détendre (VERB)

### 27. gamine

**Lexeme 1: gamine**, NOUN
- grammar: gender Fem; countability Count
- said: /ɡa.ˈmin/ (fr-FR)
  1. [B1] petite fille ou jeune fille, enfant ou adolescente
     - example: Quand j'étais gamine, je passais mes étés chez ma grand-mère.
     - collocations: une petite gamine; quand j'étais gamine; une gamine des rues
     - labels: formality informal
  2. [B1] fille de quelqu'un, l'enfant de sexe féminin de ses parents
     - example: Ce soir, je dois aller chercher ma gamine à la danse.
     - collocations: ma gamine; sa gamine
     - labels: formality informal
  3. [B2] femme adulte qui se conduit sans sérieux, comme une enfant
     - example: Arrête de bouder, tu te comportes vraiment comme une gamine.
     - collocations: faire la gamine; se conduire comme une gamine
     - labels: formality informal, attitude disapproving
- relations: feminine_of gamin (NOUN)

### 28. proverbe

**Lexeme 1: proverbe**, NOUN
- grammar: gender Masc; countability Count
- said: /pʁɔ.ˈvɛʁb/ (fr-FR)
  1. [B1] phrase courte et connue de tous qui exprime une vérité ou un conseil
     - example: Comme dit le proverbe, la nuit porte conseil avant de décider.
     - collocations: comme dit le proverbe; un vieux proverbe; un proverbe populaire; selon le proverbe

### 29. pouce

**Lexeme 1: pouce**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈpus/ (fr-FR)
  1. [A2] le doigt court et épais de la main, séparé des quatre autres
     - example: Le bébé s'endort toujours en suçant son pouce.
     - collocations: sucer son pouce; donner un coup de pouce; se tourner les pouces; manger sur le pouce; lever le pouce; faire du pouce
  2. [B1] unité de longueur anglo-saxonne valant environ deux centimètres et demi
     - example: L'écran de cet ordinateur mesure quinze pouces de diagonale.
     - collocations: ne pas bouger d'un pouce; ne pas céder d'un pouce; un écran de quinze pouces
- relations: confusable pousse (NOUN): same pronunciation /pus/, means a young shoot of a plant
**Lexeme 2: pouce**, INTJ
- grammar: invariable True
- said: /ˈpus/ (fr-FR)
  1. [B2] mot que disent les enfants pour arrêter un jeu un moment
     - example: Pouce, je m'arrête une minute pour refaire mon lacet !
     - labels: formality informal
- relations: derived_from pouce (NOUN)

### 30. western

**Lexeme 1: western**, NOUN
- grammar: gender Masc; countability Both
- said: /wɛs.ˈtɛʁn/ (fr-FR)
  1. [B1] film dont l'action se passe dans l'Ouest américain à l'époque des pionniers
     - example: Le dimanche soir, mon grand-père regardait souvent un vieux western à la télévision.
     - collocations: un western spaghetti; un grand classique du western; regarder un western; tourner un western; un héros de western

### 31. discrètement

**Lexeme 1: discrètement**, ADV
- said: /dis.kʁɛt.ˈmɑ̃/ (fr-FR)
  1. [B1] sans se faire remarquer, de manière à ne pas attirer l'attention
     - example: Pendant le discours, il a quitté discrètement la salle par le fond.
     - collocations: s'éclipser discrètement; sortir discrètement; partir discrètement; glisser discrètement; observer discrètement
  2. [B2] de façon simple et sobre, sans rien de voyant ni d'excessif
     - example: Pour l'entretien, habille-toi discrètement et évite les couleurs vives.
     - collocations: discrètement maquillé; s'habiller discrètement; discrètement éclairé
- relations: adverb_of discret (ADJ)

### 32. citoyenneté

**Lexeme 1: citoyenneté**, NOUN
- grammar: gender Fem; countability Mass, mainly
- said: /si.twa.jɛn.ˈte/ (fr-FR)
  1. [B2] statut légal d'une personne membre d'un État, avec ses droits et ses devoirs
     - example: Après dix ans dans le pays, elle a obtenu la citoyenneté.
     - collocations: obtenir la citoyenneté; acquérir la citoyenneté; la double citoyenneté; la citoyenneté européenne; perdre sa citoyenneté
  2. [C1] fait de se conduire de manière responsable et utile à la vie en société
     - example: L'école doit aussi former les élèves à la citoyenneté.
     - collocations: éducation à la citoyenneté; acte de citoyenneté; exercer sa citoyenneté
- relations: abstract_noun_of citoyen (NOUN); confusable nationalité (NOUN): nationalité is belonging to a nation; citoyenneté stresses the political rights and duties in a state, though the two often overlap

### 33. produire

**Lexeme 1: produire**, VERB
- grammar: conjugation 3; irregular True
- said: /pʁɔ.ˈdɥiʁ/ (fr-FR)
- forms: produis [VerbForm=Fin, Mood=Ind, Tense=Pres, Person=1, Number=Sing] /pʁɔ.ˈdɥi/; produit [VerbForm=Fin, Mood=Ind, Tense=Pres, Person=3, Number=Sing] /pʁɔ.ˈdɥi/; produisons [VerbForm=Fin, Mood=Ind, Tense=Pres, Person=1, Number=Plur] /pʁɔ.dɥi.ˈzɔ̃/; produisent [VerbForm=Fin, Mood=Ind, Tense=Pres, Person=3, Number=Plur] /pʁɔ.ˈdɥiz/; produit [VerbForm=Part, Tense=Past] /pʁɔ.ˈdɥi/; produisit [VerbForm=Fin, Mood=Ind, Tense=Past, Person=3, Number=Sing] /pʁɔ.dɥi.ˈzi/; produirai [VerbForm=Fin, Mood=Ind, Tense=Fut, Person=1, Number=Sing] /pʁɔ.dɥi.ˈʁe/; produise [VerbForm=Fin, Mood=Sub, Tense=Pres, Person=1, Number=Sing] /pʁɔ.ˈdɥiz/
  1. [A2] fabriquer des objets ou faire pousser des choses en grande quantité
     - example: Cette région produit surtout du vin et des fromages.
     - collocations: produire en série; produire en masse; produire de l'électricité; produire du vin; produire de l'énergie
     - grammar: auxiliary have; valency Tr
  2. [B1] être la cause d'un effet ou d'un résultat
     - example: Ses paroles ont produit une forte impression sur le public.
     - collocations: produire un effet; produire une impression; produire des résultats; produire des changements
     - grammar: auxiliary have; valency Tr
  3. [B1] arriver, avoir lieu, en parlant d'un événement
     - example: Un grave accident s'est produit hier soir sur l'autoroute.
     - frames: il se produit qqch
     - collocations: se produire rarement; se produire soudain
     - grammar: reflexive True; valency Refl; auxiliary be
  4. [B2] jouer, chanter ou danser devant un public, sur une scène
     - example: Le groupe se produira samedi soir dans une petite salle.
     - collocations: se produire sur scène; se produire en concert; se produire en public
     - grammar: reflexive True; valency Refl; auxiliary be
  5. [C1] montrer un document officiel pour prouver quelque chose
     - example: Vous devez produire une pièce d'identité à l'entrée.
     - collocations: produire une pièce d'identité; produire un justificatif; produire des preuves; produire un certificat
     - labels: formality formal
     - grammar: auxiliary have; valency Tr
  6. [B2] financer et organiser la réalisation d'un film, d'un disque ou d'un spectacle
     - example: Ce petit studio indépendant produit deux ou trois films par an.
     - collocations: produire un film; produire un album; produire une émission
     - grammar: auxiliary have; valency Tr

### 34. piège

**Lexeme 1: piège**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈpjɛʒ/ (fr-FR)
  1. [B1] appareil installé pour attraper des animaux
     - example: Le chasseur a posé un piège près de la rivière.
     - frames: piège à + nom
     - collocations: poser un piège; tendre un piège; prendre au piège; piège à souris; piège à loups; tomber dans un piège
  2. [B1] ruse préparée pour tromper une personne ou la prendre par surprise
     - example: Les policiers ont tendu un piège aux voleurs cette nuit.
     - frames: tendre un piège à qqn
     - collocations: tomber dans le piège; être pris au piège; tendre un piège; flairer le piège; déjouer un piège; un piège grossier
  3. [B2] difficulté cachée où l'on risque facilement de faire une erreur
     - example: Cette dictée est pleine de pièges pour les élèves.
     - collocations: éviter les pièges; question piège; plein de pièges; déjouer les pièges

### 35. rayonnement

**Lexeme 1: rayonnement**, NOUN
- grammar: gender Masc
- said: /ʁɛ.jɔn.ˈmɑ̃/ (fr-FR)
  1. [B2] énergie émise par une source sous forme d'ondes ou de particules
     - example: En été, le rayonnement solaire devient dangereux pour la peau.
     - collocations: rayonnement solaire; rayonnements ionisants; rayonnement ultraviolet; rayonnement infrarouge; exposition aux rayonnements; émettre un rayonnement
     - grammar: countability Both
  2. [B2] influence et prestige qui s'étendent loin autour d'une personne, d'un pays ou d'une institution
     - example: Cette université cherche à accroître son rayonnement à l'étranger.
     - frames: rayonnement de qqn/qqch
     - collocations: rayonnement culturel; rayonnement international; accroître son rayonnement; contribuer au rayonnement
     - labels: figurative True
     - grammar: countability Mass
  3. [C1] éclat lumineux de joie ou de bonheur qui se dégage d'une personne
     - example: Tout le monde remarquait le rayonnement de son visage ce jour-là.
     - labels: style literary, figurative True
     - grammar: countability Mass
- relations: verbal_noun_of rayonner (VERB)

### 36. ténèbres

**Lexeme 1: ténèbres**, NOUN
- grammar: gender Fem; number Plur
- said: /te.ˈnɛbʁ/ (fr-FR)
  1. [B2] obscurité profonde, absence presque totale de lumière
     - example: Après la panne, toute la maison était plongée dans les ténèbres.
     - collocations: plongé dans les ténèbres; dans les ténèbres; les ténèbres de la nuit; percer les ténèbres
     - labels: style literary
     - opposites: lumière
  2. [C1] état d'ignorance, de malheur ou de mal, ce qui reste obscur et caché
     - example: Le pays sort enfin des ténèbres d'une longue dictature.
     - frames: les ténèbres de qqch
     - collocations: les ténèbres de l'ignorance; le prince des ténèbres; les puissances des ténèbres; sortir des ténèbres
     - labels: figurative True, style literary
     - opposites: lumière

### 37. érable

**Lexeme 1: érable**, NOUN
- grammar: gender Masc; countability Count, mainly; note uncountable when the wood is meant
- said: /e.ˈʁabl/ (fr-FR)
  1. [B2] grand arbre des régions tempérées aux feuilles découpées en pointes
     - example: En automne, les feuilles de cet érable deviennent rouge vif.
     - collocations: sirop d'érable; feuille d'érable; érable à sucre; bois d'érable; érable rouge

### 38. manoir

**Lexeme 1: manoir**, NOUN
- grammar: gender Masc; countability Count
- said: /ma.ˈnwaʁ/ (fr-FR)
  1. [B2] grande et belle maison ancienne entourée de terres, à la campagne
     - example: Ils ont passé le week-end dans un vieux manoir normand.
     - collocations: un vieux manoir; un manoir hanté; le seigneur du manoir

### 39. alinéa

**Lexeme 1: alinéa**, NOUN
- grammar: gender Masc; countability Count
- said: /a.li.ne.ˈa/ (fr-FR)
  1. [B2] partie d'un texte comprise entre deux retours à la ligne, surtout dans un texte de loi
     - example: Le deuxième alinéa de cet article précise les conditions d'application.
     - collocations: au premier alinéa; le dernier alinéa; alinéa premier
  2. [C1] retrait de la première ligne d'un paragraphe par rapport aux autres lignes
     - example: N'oubliez pas de commencer chaque nouveau paragraphe par un alinéa.
     - collocations: faire un alinéa; aller à l'alinéa
- relations: confusable paragraphe (NOUN): alinéa is mostly used for the numbered parts of legal texts or for an indented line; paragraphe is the ordinary word for a block of text

### 40. livraison

**Lexeme 1: livraison**, NOUN
- grammar: gender Fem; countability Count
- said: /li.vʁɛ.ˈzɔ̃/ (fr-FR)
  1. [A2] remise d'une marchandise ou d'un bien à la personne qui l'a commandé
     - example: La livraison à domicile est gratuite pour toute commande supérieure à cinquante euros.
     - frames: livraison de qqch à qqn
     - collocations: livraison à domicile; frais de livraison; délai de livraison; bon de livraison; livraison gratuite; effectuer une livraison; prendre livraison de; camion de livraison; service de livraison
  2. [C1] partie d'un ouvrage publié en plusieurs fois, vendue séparément
     - example: Chaque mois, les abonnés recevaient une nouvelle livraison du roman.
     - frames: livraison de qqch
- relations: verbal_noun_of livrer (VERB)

### 41. norme

**Lexeme 1: norme**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈnɔʁm/ (fr-FR)
  1. [B2] règle ou principe qui indique comment il faut agir ou penser
     - example: Chaque société possède ses propres normes de comportement et de politesse.
     - collocations: normes sociales; respecter les normes; les normes en vigueur; imposer une norme
  2. [B2] ce qui se fait ou arrive le plus souvent, la situation habituelle
     - example: Aujourd'hui, travailler depuis chez soi est devenu la norme dans notre service.
     - collocations: devenir la norme; dans la norme; hors norme; hors normes; s'écarter de la norme
  3. [B2] ensemble de règles techniques officielles qu'un produit ou une installation doit respecter
     - example: Cette vieille installation électrique n'est plus aux normes actuelles.
     - collocations: aux normes; mettre aux normes; normes de sécurité; conforme aux normes; norme européenne

### 42. aucun

**Lexeme 1: aucun**, DET
- said: /o.ˈkœ̃/ (fr-FR)
- forms: aucune [Gender=Fem, Number=Sing] /o.ˈkyn/; aucuns [Gender=Masc, Number=Plur] /o.ˈkœ̃/ (only before nouns used only in the plural); aucunes [Gender=Fem, Number=Plur] /o.ˈkyn/ (only before nouns used only in the plural)
  1. [A2] sert à dire qu'il n'existe pas même une seule personne ou chose
     - example: Je n'ai reçu aucune réponse à mon message d'hier soir.
     - frames: ne ... aucun + nom; sans aucun + nom
     - collocations: aucune idée; en aucun cas; sans aucun doute; aucun problème; à aucun moment; en aucune façon
     - grammar: note usually with ne before the verb, or alone in a short answer
  2. [B2] n'importe quel, pris parmi tous ceux qui existent
     - example: Elle connaît cette ville mieux qu'aucun autre guide.
     - frames: plus que aucun + nom
     - collocations: plus qu'aucun autre; mieux qu'aucun autre
     - labels: formality formal
     - grammar: note used in comparisons and after verbs of doubt, without ne
**Lexeme 2: aucun**, PRON
- said: /o.ˈkœ̃/ (fr-FR)
- forms: aucune [Gender=Fem, Number=Sing] /o.ˈkyn/
  1. [A2] pas une seule personne ou chose parmi celles dont on parle
     - example: Aucun de mes amis n'a pu venir à la fête samedi.
     - frames: aucun de + nom pluriel; aucun d'entre + pron
     - collocations: aucun d'entre eux; aucun des deux
     - grammar: note usually with ne before the verb

### 43. égard

**Lexeme 1: égard**, NOUN
- grammar: gender Masc; countability Both
- said: /e.ˈɡaʁ/ (fr-FR)
  1. [B2] aspect ou point de vue sous lequel on considère un sujet
     - example: À cet égard, je partage tout à fait votre opinion.
     - collocations: à cet égard; à tous égards; à bien des égards; à certains égards; à l'égard de; eu égard à
     - grammar: countability Count
  2. [B2] attention respectueuse qu'on montre envers une personne ou ses sentiments
     - example: Par égard pour sa mère, il n'a rien dit.
     - frames: par égard pour qqn/qqch; avoir égard à qqch
     - collocations: par égard pour; manquer d'égards; avoir des égards; sans égard pour; être plein d'égards
     - grammar: note often in the plural (les égards)

### 44. chaque

**Lexeme 1: chaque**, DET
- grammar: invariable True; note always before a singular noun
- said: /ˈʃak/ (fr-FR)
  1. [A1] s'emploie pour désigner un à un tous les éléments d'un ensemble
     - example: Je bois un grand verre d'eau chaque matin avant de partir.
     - frames: chaque + nom singulier
     - collocations: chaque fois; à chaque fois; chaque fois que; chaque jour; chaque année; à chaque instant; chaque chose en son temps
- relations: confusable chacun (PRON): chaque always comes before a noun (chaque élève); chacun stands alone as a pronoun (chacun a son livre)
**Lexeme 2: chaque**, PRON
- grammar: invariable True; note usually after a price or quantity, at the end of the phrase
- said: /ˈʃak/ (fr-FR)
  1. [B2] pour un seul élément d'un ensemble, pris séparément
     - example: Au marché, ces melons coûtent deux euros chaque.
     - labels: formality informal, normativity nonstandard
     - standard word: chacun

### 45. top

**Lexeme 1: top**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈtɔp/ (fr-FR)
  1. [B1] vêtement court et léger qui couvre le haut du corps
     - example: Pour la soirée, elle a choisi un top noir et un jean.
     - collocations: un top à bretelles; un top moulant
  2. [B1] ce qu'il y a de meilleur dans un domaine
     - example: Pour se reposer au soleil, cette petite plage, c'est vraiment le top.
     - collocations: c'est le top; le top du top; être au top
     - labels: formality informal
     - grammar: number Sing
  3. [B2] bref signal sonore qui indique un moment précis
     - example: Au quatrième top, il sera exactement midi.
     - collocations: top départ; donner le top
  4. [B2] classement des premiers éléments d'une catégorie, par ordre de qualité
     - example: Ce film figure sans hésitation dans mon top cinq de l'année.
     - collocations: entrer dans le top; le top dix
     - labels: formality informal
     - grammar: note usually followed by a number
**Lexeme 2: top**, ADJ
- grammar: invariable True
- said: /ˈtɔp/ (fr-FR)
  1. [B1] excellent, très agréable ou très réussi
     - example: Franchement, ton idée de pique-nique au bord du lac est top.
     - collocations: c'est top; trop top; une ambiance top
     - labels: formality informal

### 46. perturbé

**Lexeme 1: perturbé**, ADJ
- grammar: note also the past participle of perturber
- said: /pɛʁ.tyʁ.ˈbe/ (fr-FR)
  1. [B1] qui ne fonctionne pas normalement à cause d'un problème ou d'un incident
     - example: À cause de la grève, le trafic des trains est très perturbé.
     - frames: perturbé par qqch
     - collocations: trafic perturbé; circulation perturbée; sommeil perturbé; temps perturbé; fortement perturbé; service perturbé
  2. [B2] qui a l'esprit agité et inquiet à cause d'un problème
     - example: Depuis le divorce de ses parents, le garçon semble très perturbé.
     - frames: perturbé par qqch
     - collocations: profondément perturbé; enfant perturbé; psychologiquement perturbé; l'air perturbé
- relations: derived_from perturber (VERB)

### 47. prêtre

**Lexeme 1: prêtre**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈpʁɛtʁ/ (fr-FR)
  1. [B1] homme ordonné par l'Église pour célébrer la messe et donner les sacrements
     - example: Le prêtre a béni les mariés à la fin de la cérémonie.
     - collocations: devenir prêtre; se faire prêtre; être ordonné prêtre; un prêtre catholique; un jeune prêtre
     - labels: domain religion
  2. [B2] personne chargée de célébrer le culte d'une divinité, dans toute religion
     - example: Dans l'Égypte ancienne, les prêtres servaient les dieux dans les temples.
     - frames: prêtre de qqch
     - collocations: grand prêtre; les prêtres du temple
- relations: confusable curé (NOUN): a curé is a priest in charge of a parish; prêtre is the general word for any priest

### 48. dune

**Lexeme 1: dune**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈdyn/ (fr-FR)
  1. [B1] colline de sable formée et déplacée par le vent
     - example: Les enfants courent pieds nus sur les dunes derrière la plage.
     - collocations: dune de sable; gravir une dune; au sommet de la dune; les dunes du désert; dune mouvante

### 49. idéalement

**Lexeme 1: idéalement**, ADV
- grammar: invariable True
- said: /i.de.al.ˈmɑ̃/ (fr-FR)
  1. [B1] dans la meilleure situation possible, si tout se passait bien
     - example: Idéalement, il faudrait finir ce travail avant la fin du mois.
  2. [B2] d'une manière parfaite, qui ne pourrait pas être meilleure
     - example: L'hôtel est idéalement situé, à deux pas de la plage.
     - collocations: idéalement situé; idéalement placé; idéalement adapté
- relations: adverb_of idéal (ADJ)

### 50. cul

**Lexeme 1: cul**, NOUN
- grammar: gender Masc
- said: /ˈky/ (fr-FR)
  1. [B2] partie arrière du corps humain, sur laquelle on s'assoit
     - example: En glissant sur la glace, je suis tombé sur le cul.
     - collocations: tomber sur le cul; un coup de pied au cul; botter le cul; lécher le cul; cul nu
     - labels: formality informal
     - grammar: countability Count
  2. [C1] partie du fond ou de l'arrière d'un objet
     - example: Il reste encore un peu de vin au cul de la bouteille.
     - frames: cul de qqch
     - collocations: cul de bouteille; faire cul sec
     - labels: formality informal
     - grammar: countability Count
  3. [C1] relations sexuelles et sexualité, vues de façon crue
     - example: Ce film ne parle que de cul du début à la fin.
     - collocations: une histoire de cul; un film de cul; parler de cul
     - labels: formality informal, attitude vulgar
     - grammar: countability Mass
  4. [C1] chance, hasard favorable dont une personne profite
     - example: Tu as vraiment eu du cul de trouver une place ici.
     - collocations: avoir du cul; un coup de cul
     - labels: formality slang
     - grammar: countability Mass

### 51. actif

**Lexeme 1: actif**, ADJ
- grammar: comparable True
- said: /ak.ˈtif/ (fr-FR)
- forms: active [Gender=Fem, Number=Sing] /ak.ˈtiv/; actives [Gender=Fem, Number=Plur] /ak.ˈtiv/
  1. [A2] qui a beaucoup d'énergie et fait ou bouge beaucoup
     - example: Malgré son âge, mon grand-père reste très actif et sort chaque jour.
     - collocations: rester actif; mener une vie active; prendre une part active; jouer un rôle actif; participer de façon active
     - opposites: inactif
  2. [B2] qui a un emploi ou qui cherche du travail
     - example: La population active a augmenté dans la région depuis dix ans.
     - collocations: population active; vie active; entrer dans la vie active
     - grammar: position Attr; note mostly in « population active » and « vie active »
  3. [B2] qui fonctionne encore ou qui produit un effet réel
     - example: Ce volcan est toujours actif et peut entrer en éruption.
     - collocations: volcan actif; principe actif; substance active
     - opposites: inactif
  4. [B1] se dit d'une forme du verbe où le sujet fait l'action
     - example: Mettez cette phrase à la voix active, s'il vous plaît.
     - collocations: voix active; forme active
     - labels: domain grammar
     - opposites: passif
**Lexeme 2: actif**, NOUN
- grammar: gender Masc; countability Count; note for a working woman, the feminine « une active » is used
- said: /ak.ˈtif/ (fr-FR)
  1. [B2] personne qui a un travail payé ou qui en cherche un
     - example: Beaucoup d'actifs prennent le train chaque matin pour aller au bureau.
     - collocations: les jeunes actifs; les actifs occupés
     - grammar: number Plur, mainly
  2. [C1] ensemble des biens et de l'argent que possède une entreprise
     - example: L'actif de l'entreprise dépasse largement ses dettes cette année.
     - collocations: l'actif et le passif; actif net; actifs financiers
     - labels: domain finance
     - opposites: passif
- relations: derived_from actif (ADJ)

### 52. senior

**Lexeme 1: senior**, NOUN
- grammar: gender Com; countability Count
- said: /se.ˈnjɔʁ/ (fr-FR)
- spellings: senior; sénior
  1. [B1] personne âgée de plus de cinquante ou soixante ans
     - example: Notre club propose des cours de gymnastique douce pour les seniors.
     - collocations: les seniors; l'emploi des seniors; une résidence pour seniors
     - grammar: note mostly in the plural, les seniors
  2. [B2] sportif adulte, d'une catégorie plus âgée que celle des jeunes
     - example: Après trois saisons chez les juniors, elle joue enfin avec les seniors.
     - collocations: chez les seniors
     - labels: domain sport
     - opposites: junior
**Lexeme 2: senior**, ADJ
- grammar: position Attr; note after the noun; same form in the feminine, plural seniors
- said: /se.ˈnjɔʁ/ (fr-FR)
- spellings: senior; sénior
  1. [B2] qui a beaucoup d'expérience dans son métier
     - example: L'entreprise cherche un consultant senior pour diriger ce projet.
     - collocations: un consultant senior; un cadre senior
     - opposites: junior
  2. [B2] qui appartient à la catégorie des sportifs adultes
     - example: L'équipe senior du village s'entraîne tous les mardis soir.
     - collocations: l'équipe senior; la catégorie senior
     - labels: domain sport
     - opposites: junior

### 53. direction

**Lexeme 1: direction**, NOUN
- grammar: gender Fem
- said: /di.ʁɛk.ˈsjɔ̃/ (fr-FR)
  1. [A2] côté vers lequel on va, on regarde ou une chose est tournée
     - example: Pour aller à la gare, prenez la direction du centre-ville.
     - frames: en direction de qqch; dans la direction de qqch
     - collocations: prendre la direction de; changer de direction; dans toutes les directions; dans la bonne direction; indiquer la direction; en direction de
     - grammar: countability Count
  2. [B1] responsabilité de conduire et d'organiser un groupe, une entreprise ou un travail
     - example: Elle a pris la direction de l'usine il y a deux ans.
     - frames: la direction de qqch; sous la direction de qqn
     - collocations: prendre la direction; assurer la direction; confier la direction; sous la direction de
     - grammar: countability Mass
  3. [B1] ensemble des personnes qui gèrent une entreprise ou une organisation
     - example: Les employés attendent toujours une réponse claire de la direction.
     - collocations: la direction générale; se plaindre à la direction; la direction des ressources humaines
     - grammar: countability Count
  4. [B2] ensemble des pièces qui permettent d'orienter les roues d'un véhicule
     - example: Ma vieille voiture n'a pas de direction assistée, c'est fatigant.
     - collocations: direction assistée
     - grammar: countability Mass

### 54. doigt

**Lexeme 1: doigt**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈdwa/ (fr-FR)
  1. [A1] chacune des cinq parties longues et mobiles au bout de la main
     - example: Je me suis coupé le doigt en préparant les légumes.
     - collocations: lever le doigt; montrer du doigt; croiser les doigts; claquer des doigts; le petit doigt; doigt de pied; du bout des doigts; se mettre le doigt dans l'œil; les doigts dans le nez; se lécher les doigts
  2. [B2] très petite quantité d'une boisson versée dans un verre
     - example: Tu veux juste un doigt de vin avec le fromage ?
     - frames: un doigt de + nom
     - collocations: un doigt de vin; un doigt de whisky
     - labels: formality informal
- relations: confusable devoir (VERB): the forms dois and doit sound the same as doigt

### 55. linguistique

**Lexeme 1: linguistique**, NOUN
- grammar: gender Fem; countability Mass
- said: /lɛ̃.ɡɥis.ˈtik/ (fr-FR)
  1. [B2] science qui étudie les langues et la façon dont elles fonctionnent
     - example: Elle étudie la linguistique à l'université depuis deux ans.
     - collocations: linguistique générale; linguistique appliquée; linguistique historique; étudier la linguistique
- relations: derived_from linguiste (NOUN)
**Lexeme 2: linguistique**, ADJ
- said: /lɛ̃.ɡɥis.ˈtik/ (fr-FR)
  1. [B1] qui concerne une langue ou l'apprentissage des langues
     - example: Ce séjour linguistique m'a beaucoup aidé à parler anglais.
     - collocations: séjour linguistique; barrière linguistique; compétences linguistiques; minorité linguistique; frontière linguistique; diversité linguistique
  2. [C1] qui concerne la science du langage et des langues
     - example: Les théories linguistiques modernes s'intéressent beaucoup au sens des phrases.
     - collocations: analyse linguistique; théorie linguistique; recherche linguistique
