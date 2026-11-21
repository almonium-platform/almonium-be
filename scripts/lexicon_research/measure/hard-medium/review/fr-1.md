# French: two entries for each word (12 words)

Each word below has two entries of a learner's dictionary of French, A and B, written by two different writers
under the same rules. You are not told which writer wrote which, and the sides change from word to word. Some of
these words are hard on purpose: two words under one spelling, forms that are not words of their own, senses of
one country only, rude words.

For each word say which entry serves a learner better, and name the faults of each. A writer may have refused a
word as no dictionary word of its own ("refused: inflected form of ..."); that is right for an inflected form, an
abbreviation or a name, and wrong for a word a dictionary of French lists.

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

One line of JSON for every word, in the order given, inside a single ```json block:

```json
{"id": 1, "better": "A", "why": "B lacks the metal, the commoner of the two words spelt lead", "A": [], "B": [{"fault": "missing-core", "where": "lexemes", "what": "the metal is missing"}]}
```

"better" is "A", "B" or "same": "same" only when you could not say which you would rather give a learner. "why" is
one sentence. "A" and "B" list each entry's faults as above, empty when it has none.

## The words

### 1. tour

#### Entry A

**Lexeme 1: tour**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈtuʁ/ (fr-FR)
  1. [A2] mouvement complet décrit en cercle par quelqu'un ou par quelque chose
     - example: Nous avons fait le tour du lac à vélo en une heure.
     - frames: tour de qqch
     - collocations: faire le tour; fermer à double tour; un tour de clé; un tour de piste
  2. [A2] moment où une personne doit agir ou parler dans un ordre établi
     - example: Patiente encore quelques minutes, ce sera bientôt ton tour de passer.
     - frames: tour de + inf; au tour de qqn
     - collocations: attendre son tour; passer son tour; chacun son tour; à son tour; à tour de rôle
  3. [A2] courte sortie ou promenade faite pour se détendre ou voir un endroit
     - example: Après le dîner, je sors souvent faire un petit tour dans le quartier.
     - collocations: faire un tour; aller faire un tour; un petit tour
  4. [B1] chacune des étapes successives d'une élection, d'une compétition ou d'un jeu
     - example: Aucun candidat n'a obtenu la majorité absolue au premier tour de l'élection.
     - collocations: le premier tour; le second tour; le deuxième tour; dès le premier tour; passer au tour suivant
  5. [B1] action habile ou ruse destinée à amuser, à surprendre ou à tromper
     - example: Pendant la fête, sa sœur lui a joué un drôle de tour.
     - collocations: un tour de magie; jouer un tour; un mauvais tour; un tour de passe-passe; un tour de force
**Lexeme 2: tour**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈtuʁ/ (fr-FR)
  1. [A2] bâtiment ou élément d'architecture très élevé, beaucoup plus haut que large
     - example: Du sommet de la vieille tour, on aperçoit toute la vallée environnante.
     - collocations: une tour de contrôle; une tour de guet; une tour de bureaux; une tour d'habitation
  2. [B2] pièce du jeu d'échecs qui se déplace horizontalement ou verticalement
     - example: En fin de partie, tu peux déplacer ta tour sur toute la colonne.

#### Entry B

**Lexeme 1: tour**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈtuʁ/ (fr-FR)
  1. [A2] moment où une personne doit agir, après ou avant les autres
     - example: Attends ton tour, la dame était là avant toi.
     - collocations: à tour de rôle; chacun son tour; attendre son tour; à mon tour; c'est ton tour
  2. [A2] courte sortie à pied ou en véhicule, pour se détendre
     - example: Après le dîner, nous allons souvent faire un tour au parc.
     - collocations: faire un tour; faire un petit tour; un tour à vélo; un tour en voiture
  3. [A2] parcours complet autour d'un lieu ou à travers tout un espace
     - example: Mon rêve est de faire le tour du monde en bateau.
     - frames: faire le tour de qqch
     - collocations: faire le tour du monde; faire le tour de la question; faire le tour du propriétaire
  4. [B1] mouvement complet d'un objet autour de son axe
     - example: Donne encore un tour de clé avant de partir.
     - collocations: un tour de clé; tours par minute; un tour sur soi-même; un tour de vis
  5. [B1] action habile ou rusée faite pour amuser ou pour tromper quelqu'un
     - example: Les enfants adorent les tours de magie de leur grand-père.
     - frames: jouer un tour à qqn
     - collocations: un tour de magie; jouer un tour; un sale tour; un tour de passe-passe; un tour de cartes
  6. [B1] chacune des étapes successives d'une élection ou d'une compétition
     - example: Aucun candidat n'a obtenu la majorité au premier tour.
     - collocations: premier tour; second tour; au premier tour; élu au second tour; passer le premier tour
  7. [B2] mesure de la longueur autour d'une partie du corps ou d'un objet
     - example: Le tailleur prend mon tour de taille pour ajuster le pantalon.
     - collocations: tour de taille; tour de poitrine; tour de cou; tour de hanches
  8. [C1] manière dont une situation ou une affaire évolue
     - example: La discussion a pris un tour inquiétant en fin de soirée.
     - collocations: prendre un mauvais tour; prendre un tour inattendu
     - grammar: note mostly with prendre
  9. [C1] machine qui fait pivoter une pièce pour lui donner une forme
     - example: Elle façonne ses vases sur un vieux tour de potier.
     - collocations: tour de potier
     - labels: domain technology
**Lexeme 2: tour**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈtuʁ/ (fr-FR)
  1. [A2] construction très haute et étroite, ou grand immeuble à nombreux étages
     - example: Depuis le sommet de la tour, on voit toute la ville.
     - collocations: la tour Eiffel; tour de contrôle; tour d'habitation; tour de bureaux; s'enfermer dans sa tour d'ivoire
  2. [B2] pièce du jeu d'échecs qui se déplace en ligne droite
     - example: Avec sa tour, il menace maintenant mon roi.
     - labels: domain sport

### 2. fils

#### Entry A

**Lexeme 1: fils**, NOUN
- grammar: gender Masc; countability Count; invariable True
- said: /ˈfis/ (fr-FR)
  1. [A1] enfant de sexe masculin, par rapport à son père ou à sa mère
     - example: Leur fils aîné vient de commencer ses études de médecine.
     - frames: fils de qqn
     - collocations: fils unique; fils aîné; fils cadet; fils adoptif; de père en fils
     - opposites: fille
  2. [C1] homme considéré comme issu d'un pays, d'un milieu ou d'une époque
     - example: Ce poète, fils du peuple, n'a jamais oublié ses origines modestes.
     - frames: fils de qqch
     - labels: style literary, figurative True
  3. [B2] Jésus-Christ, deuxième personne de la Trinité chrétienne
     - example: Le prêtre bénit l'assemblée au nom du Père, du Fils et du Saint-Esprit.
     - collocations: le Fils de Dieu
     - labels: domain religion
     - grammar: note written with a capital: le Fils
- relations: confusable fil (NOUN): the plural of fil (thread, wire) is also spelled fils but is said /fil/

#### Entry B

**Lexeme 1: fils**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈfis/ (fr-FR)
  1. [A1] garçon ou homme considéré par rapport à ses parents
     - example: Nous accompagnons notre fils aîné à la gare demain matin.
     - collocations: de père en fils; fils unique; fils aîné; fils cadet; fils adoptif
  2. [C1] homme issu d'un pays, d'un peuple ou marqué par une époque
     - example: Par ses idées, cet artiste reste un vrai fils de son siècle.
     - frames: fils de qqch
     - collocations: fils du pays; fils de son temps
     - labels: figurative True, style literary
- relations: confusable fil (NOUN): the plural of fil (thread, wire) is also spelled fils, but pronounced with an l and a silent s

### 3. couvent

#### Entry A

**Lexeme 1: couvent**, NOUN
- grammar: gender Masc; countability Count
- said: /ku.ˈvɑ̃/ (fr-FR)
  1. [B1] maison où des religieux ou des religieuses vivent ensemble selon une règle
     - example: Elle a décidé d'entrer au couvent à vingt ans.
     - collocations: entrer au couvent; mettre au couvent; un ancien couvent; les murs du couvent; la vie au couvent
  2. [C1] école tenue par des religieuses, où les élèves logent souvent
     - example: Ma grand-mère a été élevée au couvent jusqu'à quinze ans.
     - collocations: élevée au couvent
     - labels: time dated
- relations: confusable couver (VERB): ils couvent, a form of couver (to brood), is spelled the same but said /kuv/

#### Entry B

**Lexeme 1: couvent**, NOUN
- grammar: gender Masc; countability Count
- said: /ku.ˈvɑ̃/ (fr-FR)
  1. [B1] établissement où des religieux ou des religieuses vivent en communauté
     - example: Depuis dix ans, plusieurs religieuses vivent ensemble dans ce petit couvent de montagne.
     - collocations: entrer au couvent; se retirer au couvent; vivre au couvent; un couvent de religieuses
  2. [C1] internat de jeunes filles dirigé par des religieuses
     - example: Autrefois, beaucoup de familles aisées envoyaient leurs filles étudier au couvent.
     - collocations: être élevée au couvent; sortir du couvent
     - labels: time dated
- relations: confusable couver (VERB): its third-person plural form couvent has the same spelling, with a silent -ent

### 4. est

#### Entry A

**Lexeme 1: est**, NOUN
- grammar: gender Masc; number Sing; invariable True
- said: /ˈɛst/ (fr-FR)
  1. [A2] point cardinal situé du côté où le soleil se lève
     - example: Notre village se trouve à dix kilomètres à l'est de la gare.
     - frames: à l'est de qqch
     - collocations: à l'est; plein est; vent d'est; d'est en ouest
     - opposites: ouest
  2. [A2] partie d'un pays ou d'un continent située du côté du soleil levant
     - example: De nombreuses usines textiles se sont installées dans l'est du pays.
     - frames: l'est de qqch
     - collocations: pays de l'Est; bloc de l'Est
     - grammar: note capitalized as Est when used alone to name a region
     - opposites: ouest
**Lexeme 2: est**, ADJ
- grammar: invariable True; position Attr; comparable False; note placed after the noun
- said: /ˈɛst/ (fr-FR)
  1. [B1] situé du côté où le soleil se lève, tourné vers ce point cardinal
     - example: Nous préférons longer la côte est de l'île pour éviter le vent.
     - collocations: la côte est; le versant est; la façade est; la banlieue est
     - opposites: ouest
- relations: derived_from est (NOUN)

#### Entry B

**Lexeme 1: est**, NOUN
- grammar: gender Masc; number Sing
- said: /ˈɛst/ (fr-FR)
  1. [A2] point cardinal situé du côté où le soleil se lève
     - example: Le vent souffle de l'est depuis plusieurs jours.
     - frames: à l'est de qqch
     - collocations: vers l'est; plein est; vent d'est; orienté à l'est; exposé à l'est
     - opposites: ouest
  2. [B1] partie d'un pays ou d'un territoire située du côté où le soleil se lève
     - example: Mes grands-parents habitent dans l'Est, près de la frontière allemande.
     - collocations: les pays de l'Est; l'Europe de l'Est; dans l'Est
     - grammar: note usually capitalised: l'Est
     - opposites: ouest
**Lexeme 2: est**, ADJ
- grammar: invariable True; position Attr; comparable False
- said: /ˈɛst/ (fr-FR)
  1. [B1] situé du côté où le soleil se lève
     - example: Nous avons garé la voiture près de l'entrée est du parc.
     - collocations: la côte est; la façade est; longitude est
     - grammar: note always after the noun
     - opposites: ouest
- relations: derived_from est (NOUN)

### 5. été

#### Entry A

**Lexeme 1: été**, NOUN
- grammar: gender Masc; countability Count
- said: /e.ˈte/ (fr-FR)
  1. [A1] saison la plus chaude de l'année, entre le printemps et l'automne
     - example: En été, nous aimons dîner dehors sur la terrasse quand il fait doux.
     - collocations: en été; en plein été; vacances d'été; heure d'été; passer l'été

#### Entry B

**Lexeme 1: été**, NOUN
- grammar: gender Masc; countability Count
- said: /e.ˈte/ (fr-FR)
  1. [A1] saison la plus chaude de l'année, entre le printemps et l'automne
     - example: Chaque été, nous partons camper au bord de la mer.
     - collocations: en été; cet été; l'été dernier; pendant l'été; les vacances d'été; l'heure d'été; un été indien; en plein été

### 6. œil

#### Entry A

**Lexeme 1: œil**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈœj/ (fr-FR)
- forms: yeux [Number=Plur] /ˈjø/
  1. [A1] organe de la vue chez l'être humain et chez les animaux
     - example: Ferme les yeux quelques secondes et écoute le bruit des vagues.
     - collocations: à l'œil nu; cligner des yeux; lever les yeux; baisser les yeux; ouvrir les yeux; fermer les yeux
  2. [A2] regard ou attention que l'on porte sur quelqu'un ou quelque chose
     - example: Peux-tu garder un œil sur mes valises pendant une minute ?
     - frames: jeter un œil à qqch; jeter un œil sur qqch; garder un œil sur qqn/qqch; sous les yeux de qqn
     - collocations: jeter un œil; du coin de l'œil; ouvrir l'œil; avoir l'œil à tout
  3. [B1] manière de considérer, de juger ou d'apprécier quelqu'un ou quelque chose
     - example: Aux yeux de ses collègues, cette proposition paraît tout à fait raisonnable.
     - frames: aux yeux de qqn
     - collocations: d'un bon œil; d'un mauvais œil; d'un œil critique; d'un œil neuf
     - labels: figurative True

#### Entry B

**Lexeme 1: œil**, NOUN
- grammar: gender Masc; countability Count; irregular True
- said: /ˈœj/ (fr-FR)
- forms: yeux [Number=Plur] /ˈjø/
  1. [A1] organe de la vue, placé dans le visage chez l'homme et les animaux
     - example: J'ai une poussière dans l'œil depuis ce matin, ça me gêne.
     - collocations: ouvrir les yeux; fermer les yeux; lever les yeux; baisser les yeux; cligner des yeux; avoir les yeux bleus; à l'œil nu; en un clin d'œil
  2. [B1] regard que l'on porte sur quelqu'un ou quelque chose, avec attention ou jugement
     - example: Garde un œil sur ton petit frère pendant que je travaille.
     - frames: garder un œil sur qqn/qqch; avoir l'œil sur qqn/qqch; sous l'œil de qqn
     - collocations: jeter un coup d'œil; avoir l'œil; voir d'un bon œil; voir d'un mauvais œil; d'un œil attentif
  3. [C1] petit trou par lequel on fait passer un fil, une corde ou un manche
     - example: Elle a du mal à passer le fil dans l'œil de l'aiguille.

### 7. gens

#### Entry A

**Lexeme 1: gens**, NOUN
- grammar: gender Masc; number Plur; note an adjective placed just before it is feminine (les vieilles gens); not counted with numbers
- said: /ˈʒɑ̃/ (fr-FR)
  1. [A1] personnes dont on ne précise ni le nombre ni l'identité
     - example: Beaucoup de gens attendent le bus devant la gare ce matin.
     - collocations: la plupart des gens; beaucoup de gens; les gens disent que; des gens bien; les braves gens; les petites gens; les jeunes gens
  2. [B2] personnes qui ont le même métier ou le même mode de vie
     - example: Les gens de mer connaissent bien les dangers de cette côte.
     - frames: gens de + nom
     - collocations: les gens du voyage; les gens de lettres; les gens de mer; les gens du métier; les gens de maison

#### Entry B

**Lexeme 1: gens**, NOUN
- grammar: gender Masc; number Plur; note adjectives directly before it with a distinct feminine form usually take the feminine
- said: /ˈʒɑ̃/ (fr-FR)
  1. [A1] personnes prises dans leur ensemble ou en nombre non précisé
     - example: Dans ce quartier vivant, nous croisons toujours des gens très accueillants.
     - collocations: des gens bien; de braves gens; les jeunes gens; les petites gens; se moquer des gens; rencontrer des gens
  2. [C1] personnes travaillant comme domestiques au service d'un maître
     - example: Avant le bal, la comtesse a donné des ordres précis à ses gens.
     - labels: time dated, style literary
     - grammar: note usually with a possessive determiner
- relations: confusable personne (NOUN), for sense 1 only: personne is countable and used after numbers (trois personnes, never trois gens) and in the singular

### 8. falloir

#### Entry A

**Lexeme 1: falloir**, VERB
- grammar: conjugation 3; irregular True; valency Impers; auxiliary have; note defective: conjugated only in the 3rd person singular; impersonal il is often dropped in informal speech
- said: /fa.ˈlwaʁ/ (fr-FR)
- forms: faut [Mood=Ind, Number=Sing, Person=3, Tense=Pres, VerbForm=Fin] /ˈfo/; fallait [Mood=Ind, Number=Sing, Person=3, Tense=Imp, VerbForm=Fin] /fa.ˈlɛ/; fallut [Mood=Ind, Number=Sing, Person=3, Tense=Past, VerbForm=Fin] /fa.ˈly/; faudra [Mood=Ind, Number=Sing, Person=3, Tense=Fut, VerbForm=Fin] /fo.ˈdʁa/; faudrait [Mood=Cnd, Number=Sing, Person=3, Tense=Pres, VerbForm=Fin] /fo.ˈdʁɛ/; faille [Mood=Sub, Number=Sing, Person=3, Tense=Pres, VerbForm=Fin] /ˈfaj/; fallu [Tense=Past, VerbForm=Part] /fa.ˈly/
  1. [A1] être obligatoire, indispensable ou souhaitable selon les règles ou les circonstances
     - example: Pour réussir cet examen, il faut travailler régulièrement toute l'année.
     - frames: il faut + inf; il faut que + subj
     - collocations: comme il faut; s'il le faut; faire ce qu'il faut; quand il le faut; il fallait s'y attendre
  2. [A2] être nécessaire à une personne ou indispensable pour faire quelque chose
     - example: Il me faudrait deux jours de plus pour terminer ce dossier.
     - frames: il faut qqch; il faut qqch à qqn; il faut qqch pour + inf
     - collocations: avoir tout ce qu'il faut; plus qu'il n'en faut
- relations: confusable valoir (VERB): similar spelling and pronunciation, especially il faut (it is necessary) and il vaut mieux (it is better); confusable faillir (VERB): similar spelling, and falloir has the subjunctive faille; faillir + infinitive means to almost do something

#### Entry B

**Lexeme 1: falloir**, VERB
- grammar: conjugation 3; irregular True; valency Impers; note used only in the third person singular, with the subject il
- said: /fa.ˈlwaʁ/ (fr-FR)
- forms: faut [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈfo/; fallu [VerbForm=Part, Tense=Past] /fa.ˈly/; fallut [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /fa.ˈly/; faudra [Mood=Ind, Tense=Fut, Person=3, Number=Sing, VerbForm=Fin] /fo.ˈdʁa/; faille [Mood=Sub, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈfaj/; fallait [Mood=Ind, Tense=Imp, Person=3, Number=Sing, VerbForm=Fin] /fa.ˈlɛ/
  1. [A1] être obligatoire ou nécessaire de faire une chose
     - example: Il faut que tu finisses tes devoirs avant de sortir.
     - frames: il faut + inf; il faut que + subj; il faut à qqn + inf
     - collocations: comme il faut; s'il le faut; il faut bien; il le faut
     - grammar: auxiliary have
  2. [A2] être nécessaire à quelqu'un ou pour obtenir un résultat
     - example: Il me faut encore deux semaines pour terminer ce travail.
     - frames: il faut qqch à qqn; il faut qqch pour + inf
     - collocations: il faut du temps; tout ce qu'il faut; il en faut
     - grammar: auxiliary have
  3. [B2] être sûrement le cas, d'après ce que l'on constate
     - example: Il faut être bien naïf pour croire une histoire pareille.
     - frames: il faut + inf
     - grammar: auxiliary have
  4. [C1] manquer pour qu'une chose soit complète ou se réalise
     - example: Il s'en est fallu de peu que nous manquions le dernier train.
     - frames: il s'en faut de qqch; il s'en faut de peu que + subj
     - collocations: il s'en est fallu de peu; peu s'en faut; tant s'en faut
     - grammar: reflexive True; valency Impers; auxiliary be; note always with en: s'en falloir; the pronoun has no reflexive meaning

### 9. monter

#### Entry A

**Lexeme 1: monter**, VERB
- grammar: conjugation 1; valency Both; auxiliary both
- said: /mɔ̃.ˈte/ (fr-FR)
  1. [A1] aller ou se déplacer d'un lieu vers un endroit situé plus haut
     - example: Je suis monté au troisième étage à pied pour prévenir ma voisine.
     - frames: monter à qqch; monter sur qqch; monter + inf
     - collocations: monter sur scène; monter à l'étage; monter à pied; monter au sommet
     - grammar: valency Intr; auxiliary be
     - opposites: descendre
  2. [A1] prendre place à bord d'un véhicule ou sur le dos d'un animal
     - example: Dépêchez-vous de monter dans le train avant la fermeture automatique des portes.
     - frames: monter dans qqch; monter sur qqch
     - collocations: monter en voiture; monter à cheval; monter à bord; monter dans le train; monter dans le bus; monter dans un avion
     - grammar: valency Intr; auxiliary be
     - opposites: descendre
  3. [A2] augmenter en niveau, en valeur ou en intensité
     - example: Avec la chaleur de cet été, la température monte rapidement dans les bureaux.
     - collocations: monter en flèche; monter en puissance; faire monter les prix; faire monter la pression; faire monter les enchères
     - grammar: valency Intr; auxiliary both; note usually takes avoir, sometimes être for the resulting state
     - opposites: baisser
  4. [A2] parcourir un passage ou une pente en allant du bas vers le haut
     - example: Tu montes toujours les escaliers quatre à quatre malgré ton sac très lourd.
     - collocations: monter l'escalier; monter les marches; monter une côte; monter une pente
     - grammar: valency Tr; auxiliary have
     - opposites: descendre
  5. [A2] porter ou transporter quelque chose vers un lieu plus élevé
     - example: Mon frère a monté une tasse de thé chaud à notre grand-mère.
     - frames: monter qqch; monter qqch à qqn
     - collocations: monter les valises; monter les bagages; monter les courses
     - grammar: valency Tr; auxiliary have
     - opposites: descendre
  6. [B1] assembler et fixer les différentes pièces d'un objet ou d'un équipement
     - example: Sans notice claire, il faudra au moins deux heures pour monter cette armoire.
     - collocations: monter un meuble; monter une tente; monter une étagère
     - grammar: valency Tr; auxiliary have
     - opposites: démonter
  7. [B1] créer, organiser et mettre en place un projet, une activité ou un spectacle
     - example: Après ses études, ma sœur souhaite monter sa propre entreprise de menuiserie.
     - collocations: monter une entreprise; monter un projet; monter un spectacle; monter une pièce; monter un dossier
     - grammar: valency Tr; auxiliary have
- relations: confusable montrer (VERB): similar spelling and pronunciation; montrer means to show

#### Entry B

**Lexeme 1: monter**, VERB
- grammar: conjugation 1; valency Both; auxiliary both
- said: /mɔ̃.ˈte/ (fr-FR)
  1. [A1] se déplacer vers un endroit plus haut que celui où l'on est
     - example: Je monte au grenier chercher les vieilles photos de famille.
     - frames: monter à qqch; monter sur qqch; monter + inf
     - collocations: monter à l'étage; monter au grenier; monter sur une chaise; monter à Paris; monter se coucher
     - grammar: valency Intr; auxiliary be
     - opposites: descendre
  2. [A1] prendre place dans un véhicule ou sur un animal
     - example: Dépêche-toi de monter dans le train, il va partir !
     - frames: monter dans qqch; monter sur qqch; monter à qqch
     - collocations: monter dans le train; monter en voiture; monter à cheval; monter à vélo; monter à bord
     - grammar: valency Intr; auxiliary be
     - opposites: descendre
  3. [A2] devenir plus élevé en valeur, en niveau ou en intensité
     - example: Depuis le début de l'année, les prix de l'essence montent sans cesse.
     - collocations: monter en flèche; les prix montent; la température monte; le ton monte; la tension monte
     - grammar: valency Intr; auxiliary be
     - opposites: baisser
  4. [A2] parcourir quelque chose en allant du bas vers le haut
     - example: Avec ses courses, elle a monté l'escalier très lentement.
     - collocations: monter l'escalier; monter les marches; monter la côte
     - grammar: valency Tr; auxiliary have
     - opposites: descendre
  5. [A2] porter, placer ou régler quelque chose plus haut
     - example: Tu peux monter le son ? On n'entend rien au fond.
     - collocations: monter les valises; monter le son; monter le volume; monter le chauffage
     - grammar: valency Tr; auxiliary have
  6. [B1] assembler les différentes pièces d'un objet pour le rendre utilisable
     - example: Il nous a fallu deux heures pour monter cette armoire.
     - collocations: monter un meuble; monter une tente; monter un film; monter une vidéo
     - grammar: valency Tr; auxiliary have
     - opposites: démonter
  7. [B1] organiser et mettre en place une activité, un projet ou une action
     - example: Avec deux amis, nous voulons monter notre propre entreprise.
     - collocations: monter une entreprise; monter un projet; monter un spectacle; monter une pièce; monter un coup
     - grammar: valency Tr; auxiliary have
  8. [B2] atteindre au total une certaine somme ou quantité
     - example: Les frais de réparation se montent à plus de mille euros.
     - frames: se monter à qqch
     - grammar: valency Refl; reflexive True; auxiliary be
  9. [C1] pousser quelqu'un à devenir hostile envers une autre personne
     - example: Elle essaie de monter les enfants contre leur père.
     - frames: monter qqn contre qqn
     - grammar: valency Tr; auxiliary have

### 10. septante

#### Entry A

**Lexeme 1: septante**, NUM
- grammar: invariable True
- said: /sɛp.ˈtɑ̃t/ (fr-FR)
  1. [A2] nombre égal à soixante plus dix
     - example: Ma grand-mère vient de fêter ses septante ans en famille.
     - collocations: septante et un; septante ans
     - regions: fr-BE only, fr-CH only, fr-CD only

#### Entry B

**Lexeme 1: septante**, NUM
- grammar: invariable True
- said: /sɛp.ˈtɑ̃t/ (fr-FR)
  1. [A2] nombre égal à soixante plus dix, qui vient juste après soixante-neuf
     - example: Nous attendons environ septante personnes pour le repas de samedi soir.
     - collocations: les années septante
     - regions: fr-BE mainly, fr-CH mainly

### 11. con

#### Entry A

**Lexeme 1: con**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈkɔ̃/ (fr-FR)
  1. [B1] personne qui manque d'intelligence ou qui agit de façon désagréable
     - example: N'écoute pas ses insultes, ce type est vraiment un pauvre con.
     - collocations: faire le con; un pauvre con; un petit con; un vieux con; une bande de cons
     - labels: formality informal, attitude vulgar, derogatory
  2. [C1] partie du corps de la femme qui forme ses organes génitaux
     - example: Sur cette toile audacieuse, le peintre dévoile le con d'une femme nue.
     - labels: attitude vulgar
**Lexeme 2: con**, ADJ
- grammar: comparable True
- said: /ˈkɔ̃/ (fr-FR)
- forms: conne [Gender=Fem, Number=Sing] /ˈkɔn/
  1. [B1] qui manque d'intelligence, de jugement ou de bon sens
     - example: Excuse-moi d'avoir posé une question aussi conne devant tout le groupe.
     - collocations: avoir l'air con; con comme ses pieds; une question conne
     - labels: formality informal, attitude vulgar, derogatory
     - opposites: intelligent
  2. [B2] qui cause de la déception, du regret ou de la contrariété
     - example: C'est vraiment con d'avoir oublié nos billets sur la table.
     - frames: c'est con de + inf; c'est con que + subj
     - labels: formality informal, attitude vulgar
- relations: derived_from con (NOUN)

#### Entry B

**Lexeme 1: con**, NOUN
- grammar: gender Masc; countability Count; note for a woman, the feminine conne in the insulting sense
- said: /ˈkɔ̃/ (fr-FR)
- forms: conne [Gender=Fem, Number=Sing] /ˈkɔn/; connes [Gender=Fem, Number=Plur] /ˈkɔn/
  1. [B2] personne stupide ou qui se conduit de façon désagréable
     - example: Arrête de faire le con, tu vas finir par te blesser.
     - collocations: faire le con; pauvre con; espèce de con; petit con; sale con; à la con; jouer au con
     - labels: formality informal, attitude vulgar, derogatory
  2. [C2] organes génitaux externes de la femme
     - example: Les vieilles chansons paillardes parlent souvent du con sans détour.
     - labels: attitude vulgar
**Lexeme 2: con**, ADJ
- grammar: comparable True
- said: /ˈkɔ̃/ (fr-FR)
- forms: conne [Gender=Fem, Number=Sing] /ˈkɔn/; connes [Gender=Fem, Number=Plur] /ˈkɔn/
  1. [B2] qui manque d'intelligence ou de bon sens
     - example: Je suis vraiment con, j'ai encore oublié mes clés.
     - collocations: con comme un balai; con comme la lune; complètement con; pas si con
     - labels: formality informal, attitude vulgar
     - opposites: intelligent
  2. [B2] regrettable, qui cause de la déception
     - example: C'est con que tu ne puisses pas venir ce soir.
     - frames: c'est con que + subj; c'est con de + inf
     - collocations: c'est con
     - labels: formality informal
     - grammar: position Pred

### 12. plus

#### Entry A

**Lexeme 1: plus**, ADV
- grammar: invariable True
- said: /ˈply/ (fr-FR: in negation, and before an adjective or an adverb), /ˈplys/ (fr-FR: meaning more, after a verb or at the end of a phrase)
  1. [A1] à un degré supérieur, dans une comparaison avec autre chose
     - example: Mon frère est plus grand que moi depuis l'année dernière.
     - frames: plus + adj + que qqn/qqch; plus + adv + que qqn/qqch
     - collocations: de plus en plus; plus ou moins; beaucoup plus; bien plus
     - opposites: moins
  2. [A1] marque qu'une action ou un état qui existait avant a cessé
     - example: Nous n'habitons plus dans ce quartier depuis cinq ans.
     - frames: ne + verbe + plus
     - collocations: plus jamais; ne plus rien; plus personne; non plus
     - grammar: note with ne in standard French; ne is often dropped in speech
  3. [A2] en plus grande quantité, en plus grand nombre ou plus souvent
     - example: Tu devrais dormir plus si tu veux être en forme.
     - frames: plus de + nom; plus que qqn/qqch
     - collocations: plus de la moitié; de plus; sans plus; travailler plus
     - opposites: moins
  4. [A2] au degré le plus haut parmi plusieurs personnes ou choses
     - example: C'est la plus belle plage que j'aie jamais vue.
     - frames: le plus + adj; le plus + adv; le plus de + nom
     - collocations: le plus possible; au plus tard; tout au plus; au plus vite
     - opposites: moins
**Lexeme 2: plus**, NOUN
- grammar: gender Masc; countability Count; invariable True
- said: /ˈplys/ (fr-FR)
  1. [B1] élément positif qui donne un avantage supplémentaire
     - example: Parler anglais est un vrai plus pour ce poste.
     - collocations: un vrai plus; un petit plus; apporter un plus
     - labels: formality informal
  2. [B1] signe d'une croix qui indique une addition ou un nombre positif
     - example: N'oublie pas d'écrire le plus entre les deux nombres.
     - collocations: le signe plus
     - opposites: moins
- relations: derived_from plus (ADV)
**Lexeme 3: plus**, CONJ
- grammar: invariable True
- said: /ˈplys/ (fr-FR)
  1. [A1] sert à indiquer qu'on ajoute un nombre ou un élément à un autre
     - example: Trois plus quatre font sept, c'est facile à calculer.
     - frames: qqch plus qqch

#### Entry B

**Lexeme 1: plus**, ADV
- said: /ˈply/ (fr-FR: in negation, or before an adjective, adverb or participle), /ˈplys/ (fr-FR: in positive senses at the end of a phrase, alone, or often before de or que)
  1. [A1] à un degré supérieur ou en quantité supérieure
     - example: Avec ce nouveau train, vous arriverez plus vite que par la route.
     - frames: plus + adj; plus + adv; plus de qqn/qqch; plus que qqn/qqch
     - collocations: beaucoup plus; bien plus; encore plus; un peu plus; plus que jamais; une fois de plus
     - opposites: moins
  2. [A1] sans se poursuivre, après avoir existé ou eu lieu auparavant
     - example: Nous n'avons plus de pain pour le repas de ce soir.
     - frames: ne plus + inf; plus de qqch
     - collocations: plus du tout; plus jamais; plus rien; plus personne; plus maintenant
     - grammar: note usually with ne before the verb; ne is often dropped in informal speech
     - opposites: encore
  3. [A2] au degré ou au niveau supérieur à tous les autres
     - example: Quel est le monument le plus ancien de cette ville ?
     - frames: le plus + adj; le plus + adv; le plus de qqch
     - collocations: le plus souvent; le plus tôt possible; le plus vite possible
     - grammar: note preceded by le, la, les or a possessive determiner
     - opposites: moins
**Lexeme 2: plus**, CONJ
- said: /ˈplys/ (fr-FR)
  1. [A2] sert à marquer l'addition de nombres ou l'ajout d'un élément
     - example: Pour ce voyage, comptez cent euros de train, plus le repas.
- relations: derived_from plus (ADV)
**Lexeme 3: plus**, NOUN
- grammar: gender Masc; countability Count; invariable True
- said: /ˈplys/ (fr-FR)
  1. [B1] avantage ou élément positif qui améliore une situation ou une chose
     - example: Parler une troisième langue représente un vrai plus pour cet emploi.
     - collocations: un vrai plus; un petit plus; apporter un plus
  2. [A2] signe en forme de croix qui marque une addition ou une valeur positive
     - example: Mettez le signe plus entre ces deux nombres sur la feuille.
     - collocations: le signe plus
     - opposites: moins
- relations: derived_from plus (ADV)
