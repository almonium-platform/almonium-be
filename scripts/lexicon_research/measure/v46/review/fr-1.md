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

### 1. veineux

**Lexeme 1: veineux**, ADJ
- said: /vɛ.ˈnø/ (fr-FR)
- forms: veineuse [Gender=Fem, Number=Sing] /vɛ.ˈnøz/; veineuses [Gender=Fem, Number=Plur] /vɛ.ˈnøz/
  1. [C1] qui appartient aux vaisseaux qui ramènent le sang vers le cœur
     - example: L'infirmière prélève un peu de sang veineux au creux du bras.
     - labels: domain medicine
     - grammar: position Attr; comparable False
     - opposites: artériel
  2. [B2] qui a souvent de la chance
     - example: Tu as gagné encore une fois ? Tu es vraiment veineux !
     - labels: formality informal
     - opposites: malchanceux
  3. [C1] dont les vaisseaux sanguins se voient nettement sous la peau
     - example: Ses mains veineuses tremblaient un peu en tenant la tasse.
- relations: adjective_of veine (NOUN)
**Lexeme 2: veineux**, NOUN
- grammar: gender Masc; countability Count
- said: /vɛ.ˈnø/ (fr-FR)
- forms: veineuse [Gender=Fem, Number=Sing] /vɛ.ˈnøz/
  1. [B2] personne qui a souvent de la chance
     - example: Quel veineux, il a trouvé une place juste devant la porte !
     - labels: formality informal
- relations: derived_from veineux (ADJ)

### 2. autre

**Lexeme 1: autre**, ADJ
- grammar: note same form in the masculine and feminine; usually placed before the noun
- said: /ˈotʁ/ (fr-FR)
  1. [A1] qui n'est pas la même personne ou la même chose, différent
     - example: Cette méthode ne marche pas, essayons une autre solution.
     - frames: un/une autre + nom; l'autre + nom
     - opposites: même
  2. [A1] qui s'ajoute à ce qu'on a déjà, en plus
     - example: Tu veux une autre part de gâteau ?
**Lexeme 2: autre**, PRON
- said: /ˈotʁ/ (fr-FR)
  1. [A1] personne ou chose différente de celle dont on parle
     - example: Ce stylo ne marche plus, donne-m'en un autre.
     - frames: un/une autre; l'autre; d'autres
  2. [A2] les gens qui nous entourent, les personnes en dehors de soi
     - example: Il faut apprendre à respecter les autres.
     - frames: les autres
     - grammar: number Plur

### 3. technicité

**Lexeme 1: technicité**, NOUN
- grammar: gender Fem; countability Mass
- said: /tɛk.ni.si.ˈte/ (fr-FR)
  1. [B2] complexité d'une chose qui demande des connaissances de spécialiste
     - example: La technicité de ce dossier décourage beaucoup de lecteurs non spécialistes.
     - frames: la technicité de qqch
  2. [C1] grande maîtrise des méthodes et des gestes d'un métier ou d'un art
     - example: Ce joueur impressionne par sa technicité et sa vision du jeu.
     - frames: d'une grande technicité
- relations: abstract_noun_of technique (ADJ)

### 4. débat

**Lexeme 1: débat**, NOUN
- grammar: gender Masc; countability Count
- said: /de.ˈba/ (fr-FR)
  1. [B1] discussion où plusieurs personnes donnent des avis différents sur un sujet
     - example: Ce soir, il y a un grand débat à la télévision.
     - frames: débat sur qqch; débat entre qqn et qqn
  2. [B2] séances où une assemblée ou un tribunal examine une affaire
     - example: Les débats du procès reprendront demain matin à neuf heures.
     - grammar: number Plur
- relations: derived_from débattre (VERB)

### 5. dominance

**Lexeme 1: dominance**, NOUN
- grammar: gender Fem; countability Mass
- said: /dɔ.mi.ˈnɑ̃s/ (fr-FR)
  1. [B2] position de supériorité ou de contrôle sur d'autres personnes, groupes ou choses
     - example: La dominance de cette entreprise sur le marché inquiète ses concurrents.
     - frames: la dominance de qqn/qqch sur qqn/qqch
  2. [C1] propriété d'un gène qui s'exprime même en présence d'un gène différent
     - example: Ce caractère se transmet aux enfants grâce à la dominance du gène.
     - labels: domain biology
     - opposites: récessivité
- relations: confusable domination (NOUN): domination is the usual word for exercising power over others; dominance is more technical, used in biology, ethology and economics

### 6. nouveau

**Lexeme 1: nouveau**, ADJ
- grammar: comparable True; note nouvel before a masculine noun starting with a vowel or mute h
- said: /nu.ˈvo/ (fr-FR)
- forms: nouvel [Gender=Masc, Number=Sing] /nu.ˈvɛl/; nouvelle [Gender=Fem, Number=Sing] /nu.ˈvɛl/; nouveaux [Gender=Masc, Number=Plur] /nu.ˈvo/; nouvelles [Gender=Fem, Number=Plur] /nu.ˈvɛl/
  1. [A1] qui vient remplacer ou s'ajouter à ce qui existait avant
     - example: Nous avons un nouveau voisin depuis le début du mois.
     - opposites: ancien
  2. [A2] qui existe ou que l'on connaît depuis peu de temps
     - example: Ce restaurant est nouveau, il a ouvert la semaine dernière.
     - opposites: vieux
  3. [B1] original, différent de tout ce qu'on a vu ou fait avant
     - example: Elle propose une méthode tout à fait nouvelle pour apprendre.
  4. [A2] qui est depuis peu de temps dans un lieu ou une activité
     - example: Je suis nouveau dans l'entreprise, je ne connais personne.
     - frames: être nouveau dans qqch
- relations: confusable neuf (ADJ): neuf means never used, brand-new; nouveau means recent or replacing the previous one
**Lexeme 2: nouveau**, NOUN
- grammar: gender Masc
- said: /nu.ˈvo/ (fr-FR)
- forms: nouvelle [Gender=Fem, Number=Sing] /nu.ˈvɛl/; nouveaux [Gender=Masc, Number=Plur] /nu.ˈvo/; nouvelles [Gender=Fem, Number=Plur] /nu.ˈvɛl/
  1. [A2] personne qui vient d'arriver dans une classe, un groupe, un travail
     - example: Les élèves ont bien accueilli le nouveau dès le premier jour.
     - grammar: countability Count
  2. [B1] ce qui est récent, inconnu jusque-là ou différent
     - example: Tu as du nouveau sur ton projet de voyage ?
     - frames: du nouveau
     - grammar: countability Mass; number Sing
- relations: derived_from nouveau (ADJ)

### 7. prison

**Lexeme 1: prison**, NOUN
- grammar: gender Fem; countability Both
- said: /pʁi.ˈzɔ̃/ (fr-FR)
  1. [A2] bâtiment fermé où l'on enferme les personnes condamnées par la justice
     - example: Après son arrestation, il a passé deux nuits en prison.
     - frames: en prison; aller en prison; sortir de prison
     - grammar: countability Count
  2. [B1] peine qui consiste à être enfermé pendant un certain temps
     - example: Le juge l'a condamné à cinq ans de prison ferme.
     - frames: condamner qqn à … de prison
     - labels: domain law
     - grammar: countability Mass
  3. [B2] lieu ou situation où l'on se sent enfermé et privé de liberté
     - example: Pour elle, ce mariage malheureux était devenu une véritable prison.
     - labels: figurative True
     - grammar: countability Count

### 8. assurer

**Lexeme 1: assurer**, VERB
- grammar: conjugation 1; auxiliary have; valency Both
- said: /a.sy.ˈʁe/ (fr-FR)
  1. [B1] dire à quelqu'un avec force qu'une chose est vraie
     - example: Je t'assure que je n'ai rien dit à personne.
     - frames: assurer à qqn que + ind.; assurer qqn de qqch
     - grammar: valency Tr
  2. [B1] faire en sorte qu'une chose existe ou fonctionne bien
     - example: Des policiers assurent la sécurité du quartier pendant la fête.
     - grammar: valency Tr
  3. [B1] protéger par un contrat contre un risque, en échange d'un paiement
     - example: Votre maison est-elle assurée contre l'incendie et le vol ?
     - frames: assurer qqch contre qqch
     - grammar: valency Tr
  4. [B1] vérifier qu'une chose est vraie ou bien faite
     - example: Assure-toi que la porte est bien fermée avant de partir.
     - frames: s'assurer que + ind.; s'assurer de qqch
     - grammar: valency Refl; reflexive True; auxiliary be
  5. [B2] se montrer très capable, réussir très bien ce qu'on fait
     - example: Franchement, tu as vraiment assuré pendant ton examen oral !
     - labels: formality informal
     - grammar: valency Intr

### 9. retrouver

**Lexeme 1: retrouver**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /ʁə.tʁu.ˈve/ (fr-FR)
  1. [A2] trouver de nouveau une chose perdue ou cherchée
     - example: J'ai enfin retrouvé mes clés sous le canapé.
     - frames: retrouver qqch
     - grammar: valency Tr
     - opposites: perdre
  2. [A2] rejoindre une personne à un endroit et un moment convenus
     - example: Je te retrouve devant le cinéma à huit heures.
     - frames: retrouver qqn
     - grammar: valency Tr
  3. [B1] avoir de nouveau un état ou une qualité qu'on avait perdus
     - example: Après ces vacances, elle a retrouvé le sommeil et le sourire.
     - frames: retrouver qqch
     - grammar: valency Tr
     - opposites: perdre
  4. [A2] se rencontrer ou se réunir de nouveau, souvent à un endroit fixé
     - example: Nous nous retrouvons chaque dimanche au café du port.
     - frames: se retrouver
     - grammar: valency Refl; reflexive True; auxiliary be
  5. [B1] être tout à coup dans une situation ou un lieu, souvent sans le vouloir
     - example: Il s'est retrouvé seul dans une ville inconnue.
     - frames: se retrouver + attribut; se retrouver + lieu
     - grammar: valency Refl; reflexive True; auxiliary be
- relations: derived_from trouver (VERB)

### 10. bovin

**Lexeme 1: bovin**, ADJ
- said: /bɔ.ˈvɛ̃/ (fr-FR)
  1. [B2] du bœuf, de la vache et des animaux de la même espèce
     - example: Cette région vit surtout de l'élevage bovin et de la production laitière.
     - grammar: comparable False
  2. [C1] qui paraît lent, vide et sans intelligence
     - example: Il me regardait d'un air bovin, sans comprendre un mot.
     - labels: figurative True, attitude derogatory
**Lexeme 2: bovin**, NOUN
- grammar: gender Masc; countability Count; note mostly used in the plural
- said: /bɔ.ˈvɛ̃/ (fr-FR)
  1. [B2] grand mammifère ruminant à cornes de l'espèce du bœuf et de la vache
     - example: Les bovins passent tout l'été dans les prairies de montagne.
- relations: derived_from bovin (ADJ)

### 11. application

**Lexeme 1: application**, NOUN
- grammar: gender Fem; countability Both
- said: /a.pli.ka.ˈsjɔ̃/ (fr-FR)
  1. [B1] mise en pratique d'une règle, d'une loi ou d'une théorie
     - example: L'application de la nouvelle loi commencera dès le mois prochain.
     - frames: application de qqch; mettre qqch en application; entrer en application
  2. [A2] programme qu'on installe sur un téléphone ou un ordinateur
     - example: Tu peux télécharger cette application gratuitement sur ton téléphone.
     - grammar: countability Count
  3. [B2] pose d'une crème, d'une peinture ou d'un produit sur une surface
     - example: Laissez bien sécher la peinture entre chaque application.
     - grammar: countability Count
  4. [B2] grand soin et attention qu'on met à faire un travail
     - example: Cet élève fait toujours ses devoirs avec beaucoup d'application.
     - frames: avec application
     - grammar: countability Mass
     - opposites: négligence
- relations: verbal_noun_of appliquer (VERB)

### 12. faisan

**Lexeme 1: faisan**, NOUN
- grammar: gender Masc; countability Count
- said: /fə.ˈzɑ̃/ (fr-FR)
  1. [B2] gros oiseau à longue queue, chassé ou élevé pour sa viande
     - example: Dans ce bois, on aperçoit souvent un faisan au lever du jour.

### 13. linceul

**Lexeme 1: linceul**, NOUN
- grammar: gender Masc; countability Count
- said: /lɛ̃.ˈsœl/ (fr-FR)
  1. [C1] grand drap de toile dans lequel on enveloppe un mort
     - example: On a enveloppé le corps du vieil homme dans un linceul blanc.
  2. [C2] ce qui recouvre entièrement quelque chose, comme un voile
     - example: Ce matin, un épais linceul de neige recouvre toute la vallée.
     - frames: un linceul de + nom
     - labels: figurative True, style literary

### 14. broc

**Lexeme 1: broc**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈbʁo/ (fr-FR)
  1. [B2] grand récipient à anse et à bec, qui sert à transporter ou verser un liquide
     - example: Chaque matin, ma grand-mère remplissait le broc d'eau fraîche au puits.
     - frames: un broc d'eau; un broc de vin

### 15. arrière

**Lexeme 1: arrière**, NOUN
- grammar: gender Masc; countability Count
- said: /a.ˈʁjɛʁ/ (fr-FR)
  1. [A2] partie d'une chose située derrière, du côté opposé à l'avant
     - example: Les enfants sont assis à l'arrière de la voiture.
     - frames: à l'arrière de qqch
     - opposites: avant
  2. [B2] joueur placé derrière ses partenaires, chargé surtout de défendre son camp
     - example: Notre arrière gauche a marqué un but magnifique hier soir.
     - labels: domain sport
  3. [C1] territoire loin des combats, où vit la population civile pendant une guerre
     - example: Pendant la guerre, les femmes travaillaient à l'arrière dans les usines.
     - labels: domain military
     - opposites: front
**Lexeme 2: arrière**, ADJ
- grammar: invariable True; position Attr; comparable False
- said: /a.ˈʁjɛʁ/ (fr-FR)
  1. [B1] placé derrière, dans la partie opposée à l'avant d'un véhicule ou objet
     - example: Il faut changer les pneus arrière de ta voiture.
     - opposites: avant

### 16. plan

**Lexeme 1: plan**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈplɑ̃/ (fr-FR)
  1. [A2] ensemble d'actions prévues et organisées pour atteindre un but
     - example: Nous avons besoin d'un plan pour sortir de cette crise.
     - frames: un plan de + nom; un plan pour + inf.
  2. [A2] dessin qui représente une ville, un bâtiment ou un lieu vu d'en haut
     - example: Tu peux me montrer la gare sur le plan ?
     - frames: le plan de + nom
  3. [B2] domaine ou point de vue sous lequel on considère une chose
     - example: Sur le plan financier, l'année a été très difficile.
     - frames: sur le plan + adj.; sur le plan de + nom
  4. [B1] organisation des différentes parties d'un texte ou d'un discours
     - example: Avant d'écrire, fais d'abord le plan de ta dissertation.
  5. [B2] image filmée en continu par une caméra, avec un cadrage donné
     - example: Le film s'ouvre sur un gros plan du visage de l'actrice.
     - frames: en gros plan
  6. [B2] surface plate, horizontale ou inclinée
     - example: Elle coupe les légumes sur le plan de travail.
     - frames: plan de travail; plan d'eau
- relations: confusable plant (NOUN): same pronunciation /plɑ̃/; plant means a young plant or seedling
**Lexeme 2: plan**, ADJ
- said: /ˈplɑ̃/ (fr-FR)
- forms: plane [Gender=Fem, Number=Sing] /ˈplan/
  1. [B2] sans creux ni bosses, parfaitement plat
     - example: Posez l'appareil sur une surface plane et stable.

### 17. rectitude

**Lexeme 1: rectitude**, NOUN
- grammar: gender Fem; countability Mass
- said: /ʁɛk.ti.ˈtyd/ (fr-FR)
  1. [C1] qualité d'une personne honnête qui agit toujours selon la morale
     - example: Tous ses collègues admirent la rectitude dont elle fait preuve.
     - frames: rectitude morale
     - labels: style elevated
  2. [C2] qualité d'un raisonnement ou d'un jugement juste et conforme à la raison
     - example: La rectitude de son jugement a convaincu l'ensemble du jury.
     - labels: style literary
  3. [C2] caractère de ce qui est parfaitement droit, sans courbe
     - example: Les arbres de cette allée sont plantés avec une rectitude impeccable.
     - labels: style literary, rare True

### 18. bandoulière

**Lexeme 1: bandoulière**, NOUN
- grammar: gender Fem; countability Count
- said: /bɑ̃.du.ˈljɛʁ/ (fr-FR)
  1. [B2] longue courroie passant d'une épaule à la hanche opposée pour porter un objet
     - example: Elle porte toujours son sac en bandoulière pour avoir les mains libres.
     - frames: porter qqch en bandoulière; un sac à bandoulière

### 19. aboyer

**Lexeme 1: aboyer**, VERB
- grammar: conjugation 1; valency Both; auxiliary have; note y becomes i before a silent e (il aboie, j'aboierai)
- said: /a.bwa.ˈje/ (fr-FR)
- forms: aboie [Mood=Ind,Sub, Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin] /a.ˈbwa/; aboient [Mood=Ind,Sub, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /a.ˈbwa/; aboierai [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] /a.bwa.ˈʁe/
  1. [B1] pousser son cri bref et fort, en parlant d'un chien
     - example: Chaque nuit, le chien du voisin aboie dès qu'une voiture passe.
     - frames: aboyer contre qqn; aboyer après qqn
     - grammar: valency Intr
  2. [C1] crier d'une voix forte et dure, souvent avec colère
     - example: Le chef aboyait des ordres à toute l'équipe sans jamais sourire.
     - frames: aboyer qqch; aboyer contre qqn
     - labels: figurative True
     - grammar: valency Both

### 20. vérité

**Lexeme 1: vérité**, NOUN
- grammar: gender Fem; countability Both
- said: /ve.ʁi.ˈte/ (fr-FR)
  1. [A2] ce qui correspond à la réalité, à ce qui s'est réellement passé
     - example: Tu dois me dire toute la vérité sur cette soirée.
     - frames: dire la vérité à qqn; en vérité
     - grammar: countability Mass
     - opposites: mensonge
  2. [B1] idée ou affirmation reconnue comme exacte et conforme à la réalité
     - example: Cette loi physique est devenue une vérité scientifique admise par tous.
     - grammar: countability Count
  3. [C1] qualité de ce qui représente fidèlement la nature ou la réalité
     - example: Ce portrait frappe surtout par la vérité de son regard.
     - grammar: countability Mass

### 21. mot

**Lexeme 1: mot**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈmo/ (fr-FR)
  1. [A1] suite de lettres ou de sons qui a un sens dans une langue
     - example: Je ne comprends pas ce mot, tu peux m'aider ?
  2. [A2] court message écrit qu'on laisse ou qu'on envoie à quelqu'un
     - example: Laisse-moi un petit mot sur la table avant de partir.
     - frames: laisser un mot à qqn; écrire un mot à qqn
  3. [B1] courte phrase dite à quelqu'un, souvent gentille, drôle ou bien trouvée
     - example: Pour chaque invité, il a trouvé un mot gentil.
     - frames: dire un mot à qqn
- relations: confusable mal (NOUN): the plural maux sounds exactly like mots

### 22. malade

**Lexeme 1: malade**, ADJ
- grammar: comparable True
- said: /ma.ˈlad/ (fr-FR)
  1. [A1] qui a un problème de santé, qui ne va pas bien
     - example: Je reste à la maison aujourd'hui parce que je suis malade.
     - frames: tomber malade
     - opposites: sain
  2. [B2] très touché par un sentiment pénible, jusqu'à en souffrir
     - example: Elle était malade d'inquiétude en attendant de tes nouvelles.
     - frames: malade de qqch; rendre qqn malade
     - labels: figurative True
  3. [B2] qui a perdu la raison ou agit sans penser au danger
     - example: Tu roules beaucoup trop vite, tu es malade ou quoi ?
     - labels: formality informal
  4. [B2] qui fonctionne mal ou se trouve en mauvais état
     - example: Notre économie est malade depuis plusieurs années.
     - labels: figurative True
**Lexeme 2: malade**, NOUN
- grammar: gender Com; countability Count
- said: /ma.ˈlad/ (fr-FR)
  1. [A2] personne qui souffre d'un problème de santé
     - example: Le médecin visite ses malades tous les matins.
  2. [B2] personne qui agit de façon folle ou excessive
     - example: J'ai travaillé comme un malade toute la semaine.
     - frames: comme un malade
     - labels: formality informal

### 23. vol

**Lexeme 1: vol**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈvɔl/ (fr-FR)
  1. [B1] délit qui consiste à prendre une chose appartenant à quelqu'un d'autre
     - example: Elle est allée porter plainte pour vol au commissariat.
     - frames: vol de qqch; porter plainte pour vol
     - grammar: countability Both
  2. [A2] trajet fait en avion d'un lieu à un autre
     - example: Notre vol a deux heures de retard à cause de la neige.
  3. [B1] déplacement dans l'air d'un oiseau, d'un insecte ou d'un appareil
     - example: Ce petit oiseau peut rester en vol pendant des heures.
     - frames: en vol; à vol d'oiseau
- relations: derived_from voler (VERB)

### 24. français

**Lexeme 1: français**, ADJ
- said: /fʁɑ̃.ˈsɛ/ (fr-FR)
- forms: française [Gender=Fem, Number=Sing] /fʁɑ̃.ˈsɛz/; français [Gender=Masc, Number=Plur] /fʁɑ̃.ˈsɛ/; françaises [Gender=Fem, Number=Plur] /fʁɑ̃.ˈsɛz/
  1. [A1] qui vient du pays dont Paris est la capitale, ou qui le concerne
     - example: J'adore la cuisine française, surtout ses fromages et ses desserts.
**Lexeme 2: français**, NOUN
- grammar: gender Masc; number Sing; countability Mass
- said: /fʁɑ̃.ˈsɛ/ (fr-FR)
  1. [A1] langue romane parlée en Europe, au Canada, en Afrique et ailleurs
     - example: Mon frère apprend le français depuis trois ans au lycée.
     - frames: parler français; en français
**Lexeme 3: Français**, NOUN
- grammar: gender Masc; countability Count; note féminin : Française; pluriel invariable
- said: /fʁɑ̃.ˈsɛ/ (fr-FR)
  1. [A1] personne qui a la nationalité du pays dont Paris est la capitale
     - example: Beaucoup de Français partent en vacances au mois d'août.

### 25. amorçage

**Lexeme 1: amorçage**, NOUN
- grammar: gender Masc; countability Both
- said: /a.mɔʁ.ˈsaʒ/ (fr-FR)
  1. [C1] début d'une action ou d'un processus, sa première étape
     - example: Ces mesures devraient permettre l'amorçage d'une véritable reprise économique.
     - frames: l'amorçage de qqch; phase d'amorçage; capital d'amorçage
  2. [C1] préparation d'une pompe, d'une charge explosive ou d'un appareil pour le mettre en état de fonctionner
     - example: Avant toute utilisation, l'amorçage de la pompe se fait avec un peu d'eau.
     - labels: domain technology
  3. [C1] démarrage d'un ordinateur et chargement de son système d'exploitation
     - example: Un problème survient pendant l'amorçage, et l'écran reste noir.
     - labels: domain computing
  4. [C2] dépôt de nourriture dans l'eau pour attirer les poissons à un endroit
     - example: Un bon amorçage attire rapidement les poissons près de ta ligne.
     - labels: domain fishing
- relations: verbal_noun_of amorcer (VERB)

### 26. premier

**Lexeme 1: premier**, ADJ
- grammar: comparable False
- said: /pʁə.ˈmje/ (fr-FR)
- forms: première [Gender=Fem, Number=Sing] /pʁə.ˈmjɛʁ/; premières [Gender=Fem, Number=Plur] /pʁə.ˈmjɛʁ/
  1. [A1] qui vient avant tous les autres dans le temps ou dans un ordre
     - example: C'est la première fois que je visite cette ville.
     - frames: le premier à + inf
     - opposites: dernier
  2. [B1] qui est le meilleur ou le plus important parmi d'autres
     - example: Ce petit restaurant sert des produits de première qualité.
  3. [B2] qui est à l'origine de quelque chose, de base, essentiel
     - example: Le sens premier de ce mot a beaucoup changé.
**Lexeme 2: premier**, NOUN
- grammar: gender Masc; countability Count
- said: /pʁə.ˈmje/ (fr-FR)
  1. [A2] étage d'un immeuble situé juste au-dessus du rez-de-chaussée
     - example: Nous habitons au premier, juste au-dessus de la boulangerie.
  2. [A2] le jour qui ouvre chaque mois du calendrier
     - example: Le loyer doit être payé avant le premier de chaque mois.

### 27. permettre

**Lexeme 1: permettre**, VERB
- grammar: conjugation 3; irregular True; auxiliary have; valency Tr
- said: /pɛʁ.ˈmɛtʁ/ (fr-FR)
- forms: permets [Person=1, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /pɛʁ.ˈmɛ/; permet [Person=3, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /pɛʁ.ˈmɛ/; permettons [Person=1, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /pɛʁ.mɛ.ˈtɔ̃/; permettent [Person=3, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /pɛʁ.ˈmɛt/; permis [VerbForm=Part, Tense=Past] /pɛʁ.ˈmi/; permit [Person=3, Number=Sing, Tense=Past, Mood=Ind, VerbForm=Fin] /pɛʁ.ˈmi/; permettrai [Person=1, Number=Sing, Tense=Fut, Mood=Ind, VerbForm=Fin] /pɛʁ.mɛ.ˈtʁe/; permette [Person=1, Number=Sing, Tense=Pres, Mood=Sub, VerbForm=Fin] /pɛʁ.ˈmɛt/
  1. [B1] rendre une chose possible, donner le moyen de faire
     - example: Ce nouveau train permet d'aller plus vite à la mer.
     - frames: permettre qqch; permettre de + inf
  2. [A2] donner à quelqu'un le droit ou l'accord de faire quelque chose
     - example: Mes parents ne me permettent pas de sortir le soir.
     - frames: permettre qqch à qqn; permettre à qqn de + inf
     - opposites: interdire
  3. [B1] oser faire quelque chose sans en demander l'accord, s'accorder une liberté
     - example: Je me permets de vous écrire au sujet de votre annonce.
     - frames: se permettre qqch; se permettre de + inf
     - grammar: reflexive True; auxiliary be
- relations: derived_from mettre (VERB)

### 28. principe

**Lexeme 1: principe**, NOUN
- grammar: gender Masc; countability Count
- said: /pʁɛ̃.ˈsip/ (fr-FR)
  1. [B1] règle de conduite morale qu'une personne se donne et respecte
     - example: Par principe, je ne prête jamais d'argent à mes amis.
     - frames: avoir des principes; par principe
  2. [B1] loi ou idée de base sur laquelle repose un système ou un raisonnement
     - example: Le principe de ce moteur est plus simple qu'on ne croit.
     - frames: le principe de qqch; en principe
  3. [C1] origine, cause première d'où vient quelque chose
     - example: Pour ces philosophes, l'eau était le principe de toute chose.
     - labels: style literary

### 29. énergie

**Lexeme 1: énergie**, NOUN
- grammar: gender Fem; countability Both
- said: /e.nɛʁ.ˈʒi/ (fr-FR)
  1. [B1] force tirée du soleil, du vent, du pétrole, etc., qui fait marcher les machines
     - example: Notre pays veut produire plus d'énergie grâce au vent.
     - frames: énergie solaire; énergies renouvelables
     - grammar: countability Both
  2. [A2] force physique ou morale qui permet d'agir avec vigueur
     - example: Après les vacances, je me sens plein d'énergie pour travailler.
     - frames: avec énergie; avoir de l'énergie
     - grammar: countability Mass

### 30. ciel

**Lexeme 1: ciel**, NOUN
- grammar: gender Masc; countability Count; note pluriel cieux; ciels en peinture, en météo et pour un ciel de lit
- said: /ˈsjɛl/ (fr-FR)
- forms: cieux [Number=Plur] /ˈsjø/; ciels [Number=Plur] /ˈsjɛl/
  1. [A1] espace au-dessus de la terre, où l'on voit les nuages et les étoiles
     - example: Regarde le ciel : il n'y a pas un seul nuage.
     - opposites: terre
  2. [B1] lieu où vivent Dieu et les âmes des morts, selon les croyances
     - example: Elle pense que son grand-père est monté au ciel.
     - opposites: enfer
**Lexeme 2: ciel**, INTJ
- said: /ˈsjɛl/ (fr-FR)
  1. [C1] cri qui exprime une grande surprise ou une soudaine peur
     - example: « Ciel, mon mari ! » s'écria-t-elle en ouvrant la porte.
     - labels: time dated, attitude humorous
- relations: derived_from ciel (NOUN)

### 31. victoire

**Lexeme 1: victoire**, NOUN
- grammar: gender Fem; countability Count
- said: /vik.ˈtwaʁ/ (fr-FR)
  1. [A2] fait de battre un adversaire dans une guerre, un combat ou un match
     - example: Notre équipe a remporté une belle victoire hier soir.
     - frames: remporter une victoire; victoire sur qqn; crier victoire
     - opposites: défaite
  2. [B1] succès obtenu après un effort contre une difficulté ou un problème
     - example: Arrêter de fumer, pour elle, c'est une vraie victoire.
     - frames: victoire sur qqch
     - labels: figurative True
     - opposites: échec

### 32. guerre

**Lexeme 1: guerre**, NOUN
- grammar: gender Fem; countability Both
- said: /ˈɡɛʁ/ (fr-FR)
  1. [A2] lutte armée entre des pays ou entre des groupes d'un même pays
     - example: Mon grand-père a connu la guerre quand il était enfant.
     - frames: être en guerre; faire la guerre à qqn; déclarer la guerre à qqn
     - opposites: paix
  2. [B2] lutte sans armes, conflit dur entre des personnes ou des groupes
     - example: Les deux entreprises se livrent une guerre des prix depuis des mois.
     - frames: guerre contre qqch; faire la guerre à qqch
     - labels: figurative True

### 33. lors

**Lexeme 1: lors**, ADV
- grammar: invariable True; note almost only in set phrases: lors de, dès lors, depuis lors, pour lors
- said: /ˈlɔʁ/ (fr-FR)
  1. [B1] au moment d'un événement, pendant qu'il a lieu
     - example: Nous nous sommes rencontrés lors d'un voyage en Italie.
     - frames: lors de + nom
  2. [B2] à partir de ce moment-là, ou depuis ce temps-là
     - example: Il a quitté la ville ; depuis lors, personne ne l'a revu.
     - frames: dès lors; depuis lors
- relations: confusable alors (ADV): similar form and meaning; 'alors' stands alone, while 'lors' is used in set phrases like 'lors de'

### 34. génomique

**Lexeme 1: génomique**, ADJ
- grammar: comparable False
- said: /ʒe.nɔ.ˈmik/ (fr-FR)
  1. [C1] qui porte sur l'ensemble des gènes d'un être vivant
     - example: Les chercheurs publient chaque année davantage de données génomiques sur les plantes.
     - labels: domain biology
- relations: adjective_of génome (NOUN)
**Lexeme 2: génomique**, NOUN
- grammar: gender Fem; countability Mass
- said: /ʒe.nɔ.ˈmik/ (fr-FR)
  1. [C1] science qui étudie l'ensemble des gènes des êtres vivants et leur fonctionnement
     - example: Grâce à la génomique, on comprend mieux certaines maladies rares.
     - labels: domain biology
- relations: derived_from génomique (ADJ)

### 35. problème

**Lexeme 1: problème**, NOUN
- grammar: gender Masc; countability Count
- said: /pʁɔ.ˈblɛm/ (fr-FR)
  1. [A1] difficulté ou situation gênante qu'il faut régler
     - example: Si tu as un problème avec ta voiture, appelle-moi.
     - frames: avoir un problème avec qqn/qqch; pas de problème
  2. [A2] exercice ou question dont on cherche la réponse à partir de données
     - example: Pour demain, les élèves doivent résoudre trois problèmes de géométrie.
     - frames: résoudre un problème

### 36. grammatical

**Lexeme 1: grammatical**, ADJ
- grammar: comparable False
- said: /ɡʁa.ma.ti.ˈkal/ (fr-FR)
- forms: grammaticaux [Gender=Masc, Number=Plur] /ɡʁa.ma.ti.ˈko/
  1. [B1] qui touche aux règles d'accord et de construction des phrases d'une langue
     - example: Ton texte contient encore quelques fautes grammaticales faciles à corriger.
  2. [C1] construit selon les règles d'une langue, donc correct dans sa forme
     - example: Cette phrase est grammaticale, mais personne ne parlerait vraiment ainsi.
     - labels: domain linguistics
     - opposites: agrammatical
- relations: adjective_of grammaire (NOUN)

### 37. vendéen

**Lexeme 1: vendéen**, ADJ
- said: /vɑ̃.de.ˈɛ̃/ (fr-FR)
- forms: vendéenne [Gender=Fem, Number=Sing] /vɑ̃.de.ˈɛn/; vendéennes [Gender=Fem, Number=Plur] /vɑ̃.de.ˈɛn/
  1. [B2] de la Vendée, département de l'ouest de la France, ou de ses habitants
     - example: Nous passons nos vacances chaque été sur la côte vendéenne.
  2. [C1] des paysans royalistes de l'Ouest révoltés contre la Révolution en 1793
     - example: L'armée vendéenne remporta d'abord plusieurs victoires contre les républicains.
     - labels: time historical, domain history
- relations: adjective_of Vendée (PROPN)
**Lexeme 2: Vendéen**, NOUN
- grammar: gender Masc; countability Count
- said: /vɑ̃.de.ˈɛ̃/ (fr-FR)
- forms: Vendéenne [Gender=Fem, Number=Sing] /vɑ̃.de.ˈɛn/
  1. [B2] personne qui habite la Vendée ou qui en est originaire
     - example: Beaucoup de Vendéens travaillent dans le tourisme pendant l'été.
  2. [C1] paysan royaliste de l'Ouest révolté contre la Révolution en 1793
     - example: Les Vendéens se soulevèrent contre la levée de trois cent mille hommes.
     - labels: time historical, domain history
- relations: derived_from Vendée (PROPN)

### 38. mutiler

**Lexeme 1: mutiler**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /my.ti.ˈle/ (fr-FR)
  1. [B2] blesser gravement un être vivant en coupant ou en abîmant une partie de son corps
     - example: L'explosion a mutilé plusieurs soldats qui gardaient le pont.
     - frames: mutiler qqn; se mutiler
  2. [C1] abîmer gravement une chose, une œuvre ou un texte en lui ôtant des parties
     - example: La censure a mutilé ce film en supprimant les meilleures scènes.
     - labels: figurative True

### 39. amour

**Lexeme 1: amour**, NOUN
- grammar: gender Masc; countability Both; note Often feminine in the plural in literary style (de belles amours).
- said: /a.ˈmuʁ/ (fr-FR)
  1. [A1] sentiment très fort d'attirance et de tendresse pour une autre personne
     - example: Ils vivent un grand amour depuis leur rencontre à l'université.
     - opposites: haine
  2. [A2] attachement profond et tendre pour quelqu'un ou pour quelque chose
     - example: L'amour d'une mère pour ses enfants n'a pas de limites.
     - frames: l'amour de qqch; l'amour pour qqn
  3. [A2] personne pour qui on éprouve un sentiment tendre et profond
     - example: Bonne nuit, mon amour, je t'appelle demain matin.
  4. [B2] personne ou chose très charmante, très gentille, adorable
     - example: Ta fille est un amour, elle m'a aidé toute la journée.
     - frames: un amour de + nom
     - labels: formality informal

### 40. éblouir

**Lexeme 1: éblouir**, VERB
- grammar: conjugation 2; valency Tr; auxiliary have
- said: /e.blu.ˈiʁ/ (fr-FR)
  1. [B1] gêner la vue par une lumière trop forte, au point de ne plus bien voir
     - example: Les phares de la voiture d'en face m'éblouissent sur cette route.
     - grammar: valency Tr
  2. [B2] impressionner très fortement par sa beauté, son talent ou son éclat
     - example: Cette jeune pianiste a ébloui tout le public dès les premières notes.
     - frames: éblouir qqn par qqch; être ébloui par qqch
     - labels: figurative True
     - grammar: valency Tr

### 41. origine

**Lexeme 1: origine**, NOUN
- grammar: gender Fem; countability Count
- said: /ɔ.ʁi.ˈʒin/ (fr-FR)
  1. [B1] commencement, point de départ ou cause première de quelque chose
     - example: Personne ne connaît vraiment l'origine de cette vieille légende.
     - frames: l'origine de qqch; à l'origine; être à l'origine de qqch
     - opposites: fin
  2. [A2] pays, famille ou milieu social d'où vient une personne
     - example: Mes grands-parents sont d'origine portugaise, mais je suis né ici.
     - frames: d'origine + adj; les origines de qqn
  3. [B1] lieu d'où vient un produit ou une chose
     - example: Vérifiez toujours le pays d'origine indiqué sur l'étiquette.
     - frames: pays d'origine; d'origine + adj

### 42. tourte

**Lexeme 1: tourte**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈtuʁt/ (fr-FR)
  1. [B2] plat rond de pâte cuite au four, garni et fermé par un couvercle de pâte
     - example: Pour le dîner, je prépare une tourte aux poireaux et au fromage.
     - frames: une tourte à/aux + nom
     - labels: domain cooking
  2. [C1] personne lente à comprendre, peu intelligente
     - example: Quelle tourte, il n'a toujours pas compris ce qu'on lui demandait !
     - labels: formality informal, attitude derogatory

### 43. arrivée

**Lexeme 1: arrivée**, NOUN
- grammar: gender Fem; countability Count
- said: /a.ʁi.ˈve/ (fr-FR)
  1. [A2] moment où une personne ou une chose atteint le lieu prévu
     - example: Nous attendons ton arrivée avec impatience depuis ce matin.
     - frames: l'arrivée de qqn/qqch; à l'arrivée
     - opposites: départ
  2. [B1] endroit où se termine une course, un voyage ou un trajet
     - example: Les coureurs franchissent la ligne d'arrivée sous les applaudissements.
     - opposites: départ
- relations: verbal_noun_of arriver (VERB)

### 44. aussi

**Lexeme 1: aussi**, ADV
- said: /o.ˈsi/ (fr-FR)
  1. [A1] de la même façon, en plus de ce qui vient d'être dit
     - example: Moi aussi, j'aime boire un chocolat chaud le matin.
  2. [A2] à un degré égal, pour comparer deux personnes ou deux choses
     - example: Ton frère est maintenant aussi grand que ton père.
     - frames: aussi + adj./adv. + que
  3. [B1] à ce point, à un si haut degré
     - example: Je n'ai jamais vu un film aussi ennuyeux de toute ma vie.
  4. [B2] en début de phrase : pour cette raison, c'est pourquoi
     - example: Il pleuvait très fort ; aussi sommes-nous restés à la maison.
     - frames: aussi + verbe + sujet inversé
     - labels: formality formal

### 45. magouille

**Lexeme 1: magouille**, NOUN
- grammar: gender Fem; countability Both
- said: /ma.ˈɡuj/ (fr-FR)
  1. [B2] arrangement secret et malhonnête pour obtenir un avantage
     - example: Ce maire a été condamné pour des magouilles avec certaines entreprises.
     - frames: faire des magouilles
     - labels: formality informal, attitude disapproving

### 46. version

**Lexeme 1: version**, NOUN
- grammar: gender Fem; countability Count
- said: /vɛʁ.ˈsjɔ̃/ (fr-FR)
  1. [B1] forme particulière d'une œuvre, d'un texte ou d'un produit
     - example: La nouvelle version de ce logiciel sort le mois prochain.
     - frames: en version originale; la version de qqch
  2. [B2] manière dont une personne raconte ou explique un fait
     - example: Chaque témoin donne une version différente de l'accident.
     - frames: la version de qqn
  3. [C1] exercice scolaire de traduction d'une langue étrangère vers sa propre langue
     - example: Demain matin, nous avons une version latine de deux heures.
     - labels: domain education
     - opposites: thème

### 47. rapport

**Lexeme 1: rapport**, NOUN
- grammar: gender Masc; countability Count
- said: /ʁa.ˈpɔʁ/ (fr-FR)
  1. [A2] lien ou relation qui existe entre deux choses ou deux idées
     - example: Je ne vois vraiment pas le rapport entre ces deux questions.
     - frames: rapport avec qqch; par rapport à qqch; en rapport avec qqch
  2. [B1] texte qui présente les résultats d'une étude, d'une enquête ou d'une mission
     - example: La commission doit remettre son rapport au ministre avant l'été.
     - frames: rapport sur qqch
  3. [B1] relations entre des personnes, manière dont elles se comportent ensemble
     - example: Nous avons toujours eu de très bons rapports avec nos voisins.
     - frames: rapports avec qqn; rapports entre des personnes
  4. [B2] proportion entre deux quantités ou deux valeurs que l'on compare
     - example: Ce petit hôtel offre un excellent rapport qualité-prix.
     - frames: rapport qualité-prix
  5. [B2] relation sexuelle entre deux personnes
     - example: Utilisez un préservatif à chaque rapport pour vous protéger.
     - frames: rapport sexuel
- relations: derived_from rapporter (VERB)

### 48. pivoter

**Lexeme 1: pivoter**, VERB
- grammar: conjugation 1; valency Intr; auxiliary have
- said: /pi.vɔ.ˈte/ (fr-FR)
  1. [B2] tourner autour d'un axe ou sur soi-même, sans se déplacer
     - example: Le danseur pivote lentement sur un pied au centre de la scène.
     - frames: pivoter sur qqch; pivoter autour de qqch; faire pivoter qqch
  2. [C1] changer nettement d'orientation, de stratégie ou d'idées
     - example: Après deux années difficiles, notre entreprise a pivoté vers les services en ligne.
     - frames: pivoter vers qqch
     - labels: figurative True
- relations: derived_from pivot (NOUN)

### 49. bord

**Lexeme 1: bord**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈbɔʁ/ (fr-FR)
  1. [A2] partie extrême d'une surface ou d'un objet, là où il finit
     - example: Ne pose pas ton verre si près du bord de la table.
     - frames: le bord de qqch
     - opposites: centre
  2. [A2] terre qui longe la mer, un lac ou une rivière
     - example: Chaque été, nous passons nos vacances au bord de la mer.
     - frames: au bord de la mer; au bord de l'eau
  3. [B1] intérieur d'un bateau, d'un avion ou d'un autre véhicule
     - example: Tous les passagers sont maintenant à bord de l'avion.
     - frames: à bord de qqch; monter à bord
  4. [B2] situation très proche d'un état grave ou d'un malheur
     - example: Après cette nouvelle, elle était au bord des larmes.
     - frames: au bord de qqch
     - labels: figurative True
  5. [C1] camp politique ou groupe de personnes qui partagent les mêmes idées
     - example: Ils ne sont pas du même bord, mais ils s'entendent bien.
     - frames: être du même bord

### 50. principal

**Lexeme 1: principal**, ADJ
- grammar: irregular True; comparable False
- said: /pʁɛ̃.si.ˈpal/ (fr-FR)
- forms: principaux [Gender=Masc, Number=Plur] /pʁɛ̃.si.ˈpo/
  1. [A2] qui est le plus important parmi plusieurs choses ou personnes
     - example: Quelle est la raison principale de votre départ ?
     - opposites: secondaire
**Lexeme 2: principal**, NOUN
- grammar: gender Masc; irregular True
- said: /pʁɛ̃.si.ˈpal/ (fr-FR)
- forms: principaux [Number=Plur] /pʁɛ̃.si.ˈpo/
  1. [B1] ce qui compte le plus dans une situation donnée
     - example: Le principal, c'est que tu sois en bonne santé.
     - frames: le principal, c'est que + subj.; le principal est de + inf.
     - grammar: number Sing; countability Mass
  2. [B1] personne qui dirige un collège et qui en est responsable
     - example: Nous avons rendez-vous avec le principal du collège demain matin.
     - regions: fr-FR
     - grammar: countability Count
- relations: derived_from principal (ADJ)

### 51. gros

**Lexeme 1: gros**, ADJ
- grammar: comparable True
- said: /ˈɡʁo/ (fr-FR)
- forms: grosse [Gender=Fem, Number=Sing] /ˈɡʁos/; gros [Gender=Masc, Number=Plur] /ˈɡʁo/; grosses [Gender=Fem, Number=Plur] /ˈɡʁos/
  1. [A1] qui est grand en volume, en épaisseur ou en taille
     - example: Il porte toujours un gros sac sur le dos.
     - opposites: petit
  2. [A2] dont le corps est large, qui pèse trop lourd
     - example: Mon chat est devenu trop gros depuis l'hiver.
     - opposites: mince, maigre
  3. [B1] important par la quantité, la force ou la gravité
     - example: Nous avons un gros problème avec la voiture ce matin.
- relations: confusable gras (ADJ): gras means greasy or fatty (food, skin, substances), gros means big or fat (size, body)
**Lexeme 2: gros**, NOUN
- grammar: gender Masc; number Sing; countability Mass
- said: /ˈɡʁo/ (fr-FR)
  1. [B2] vente de marchandises en grande quantité aux commerçants
     - example: Ce magasin vend uniquement en gros aux restaurants du quartier.
     - frames: en gros; commerce de gros
     - opposites: détail
  2. [B2] la partie la plus importante d'une chose ou d'un groupe
     - example: Heureusement, le gros du travail est déjà fait.
     - frames: le gros de qqch
**Lexeme 3: gros**, ADV
- grammar: invariable True
- said: /ˈɡʁo/ (fr-FR)
  1. [B2] beaucoup, une grande quantité, surtout d'argent
     - example: Avec ce projet, tu risques gros, fais bien attention.
     - frames: gagner gros; risquer gros

### 52. magnolia

**Lexeme 1: magnolia**, NOUN
- grammar: gender Masc; countability Count
- said: /ma.ɲɔ.ˈlja/ (fr-FR)
  1. [B2] arbre ou arbuste aux grandes fleurs blanches ou roses très parfumées
     - example: Chaque printemps, le magnolia du jardin se couvre de fleurs roses.

### 53. rétrocession

**Lexeme 1: rétrocession**, NOUN
- grammar: gender Fem; countability Count
- said: /ʁe.tʁo.sɛ.ˈsjɔ̃/ (fr-FR)
  1. [C1] restitution d'un bien, d'un droit ou d'un territoire à celui qui le possédait avant
     - example: La rétrocession du territoire à son ancien pays a eu lieu en grande pompe.
     - frames: la rétrocession de qqch à qqn
     - labels: domain law
  2. [C2] reversement à une autre personne d'une partie d'une somme reçue
     - example: Le contrat prévoit une rétrocession d'honoraires de vingt pour cent au cabinet.
     - frames: rétrocession de + nom (honoraires, commission)
     - labels: domain finance
- relations: verbal_noun_of rétrocéder (VERB)

### 54. étranger

**Lexeme 1: étranger**, ADJ
- grammar: comparable False
- said: /e.tʁɑ̃.ˈʒe/ (fr-FR)
- forms: étrangère [Gender=Fem, Number=Sing] /e.tʁɑ̃.ˈʒɛʁ/; étrangères [Gender=Fem, Number=Plur] /e.tʁɑ̃.ˈʒɛʁ/
  1. [A2] qui vient d'un autre pays ou qui concerne un autre pays
     - example: Elle parle couramment trois langues étrangères.
  2. [B2] que l'on ne connaît pas, qui ne semble pas familier
     - example: Ce visage ne m'est pas étranger, je l'ai déjà vu.
     - frames: être étranger à qqn
     - opposites: familier
  3. [B2] qui n'a aucun rapport avec une chose ou n'y participe pas
     - example: Il est totalement étranger à cette affaire de vol.
     - frames: étranger à qqch
- relations: confusable étrange (ADJ): étrange means strange; étranger means foreign, despite English 'stranger'
**Lexeme 2: étranger**, NOUN
- grammar: gender Masc; countability Count
- said: /e.tʁɑ̃.ˈʒe/ (fr-FR)
- forms: étrangère [Gender=Fem, Number=Sing] /e.tʁɑ̃.ˈʒɛʁ/
  1. [A2] personne qui vient d'un autre pays que celui où elle se trouve
     - example: Beaucoup d'étrangers viennent visiter cette ville chaque été.
  2. [B1] personne qu'on ne connaît pas ou qui ne fait pas partie du groupe
     - example: Je me sentais comme un étranger dans ma propre famille.
  3. [A2] ensemble des pays autres que celui où l'on vit
     - example: Mon frère travaille à l'étranger depuis deux ans.
     - frames: à l'étranger; partir à l'étranger
     - grammar: number Sing; countability Mass

### 55. connaître

**Lexeme 1: connaître**, VERB
- grammar: conjugation 3; irregular True; auxiliary have; valency Tr
- said: /kɔ.ˈnɛtʁ/ (fr-FR)
- spellings: connaître; connaitre
- forms: connais [Person=1, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /kɔ.ˈnɛ/; connaît [Person=3, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin] /kɔ.ˈnɛ/; connaissons [Person=1, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /kɔ.nɛ.ˈsɔ̃/; connaissent [Person=3, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin] /kɔ.ˈnɛs/; connu [Tense=Past, VerbForm=Part] /kɔ.ˈny/; connut [Person=3, Number=Sing, Tense=Past, Mood=Ind, VerbForm=Fin] /kɔ.ˈny/; connaîtrai [Person=1, Number=Sing, Tense=Fut, Mood=Ind, VerbForm=Fin] /kɔ.nɛ.ˈtʁe/; connaisse [Person=1, Number=Sing, Tense=Pres, Mood=Sub, VerbForm=Fin] /kɔ.ˈnɛs/
  1. [A1] avoir une information dans l'esprit, être au courant d'une chose
     - example: Est-ce que tu connais le nom de ce restaurant ?
     - frames: connaître qqch
     - opposites: ignorer
  2. [A1] avoir déjà rencontré une personne ou visité un lieu
     - example: Nous nous connaissons depuis l'école primaire.
     - frames: connaître qqn; se connaître
  3. [A2] savoir bien une langue, une matière ou une technique
     - example: Mon frère s'y connaît vraiment en informatique.
     - frames: connaître qqch; s'y connaître en qqch
  4. [B1] vivre une situation, passer par une expérience ou un événement
     - example: Ces dernières années, le pays a connu une forte croissance.
     - frames: connaître qqch
- relations: confusable savoir (VERB): both translate 'know'; connaître takes a noun (people, places), savoir a clause or infinitive

### 56. haut

**Lexeme 1: haut**, ADJ
- grammar: h_aspire True; comparable True
- said: /ˈo/ (fr-FR)
  1. [A1] qui a une grande dimension du bas jusqu'au sommet
     - example: Notre immeuble est le plus haut du quartier.
     - opposites: bas
  2. [A2] placé loin au-dessus du sol ou d'un niveau
     - example: En été, le soleil est haut dans le ciel.
     - opposites: bas
  3. [B1] plus grand que la normale en valeur, en degré ou en quantité
     - example: Il faut cuire ce gâteau à haute température.
     - opposites: bas
  4. [A2] fort et facile à entendre, en parlant de la voix ou d'un son
     - example: Lis ce texte à voix haute, s'il te plaît.
     - frames: à voix haute
     - opposites: bas
  5. [B2] supérieur par le rang, l'importance ou la qualité
     - example: Elle occupe un poste de haut fonctionnaire au ministère.
     - grammar: position Attr
**Lexeme 2: haut**, NOUN
- grammar: gender Masc; countability Count; h_aspire True
- said: /ˈo/ (fr-FR)
  1. [A2] la partie la plus élevée d'un objet ou d'un lieu
     - example: Le chat s'est caché en haut de l'armoire.
     - frames: en haut de qqch; du haut de qqch
     - opposites: bas
  2. [A2] vêtement qui couvre le buste, comme un tee-shirt ou un pull
     - example: J'ai acheté un joli haut rouge pour la fête.
  3. [B1] dimension verticale d'une chose, mesurée du bas jusqu'au sommet
     - example: Ce mur fait trois mètres de haut.
     - frames: … mètres de haut
- relations: derived_from haut (ADJ)
**Lexeme 3: haut**, ADV
- grammar: h_aspire True; invariable True
- said: /ˈo/ (fr-FR)
  1. [A2] à une grande distance au-dessus du sol
     - example: Les oiseaux volent très haut aujourd'hui.
     - opposites: bas
  2. [B1] d'une voix forte, que tout le monde peut entendre
     - example: Ne parle pas si haut, le bébé dort.
     - frames: parler haut; tout haut
     - opposites: bas
  3. [B2] plus tôt dans le même texte, dans les pages précédentes
     - example: Comme indiqué plus haut, la réunion est annulée.
     - frames: plus haut
- relations: derived_from haut (ADJ)

### 57. note

**Lexeme 1: note**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈnɔt/ (fr-FR)
  1. [A2] chiffre ou lettre qui évalue la qualité d'un travail scolaire
     - example: J'ai eu une très bonne note à l'examen de maths.
     - frames: avoir une bonne/mauvaise note; une note sur vingt
  2. [A2] quelques mots écrits rapidement pour garder une information en mémoire
     - example: Prends des notes pendant le cours, tu en auras besoin.
     - frames: prendre des notes; prendre note de qqch
  3. [B1] son musical d'une hauteur précise, ou signe qui le représente
     - example: Cette chanteuse atteint facilement les notes les plus aiguës.
  4. [B1] somme à payer pour un repas, une chambre ou un service
     - example: Pouvez-vous nous apporter la note, s'il vous plaît ?
     - frames: régler la note
  5. [B2] court texte qui explique un passage, souvent placé en bas de page
     - example: Les notes en bas de page expliquent les mots difficiles du roman.
     - frames: note de bas de page
  6. [B2] légère touche d'un sentiment ou d'un caractère qu'on remarque dans quelque chose
     - example: Sa lettre se termine sur une note d'espoir.
     - frames: une note de + nom
     - labels: figurative True

### 58. ciboulette

**Lexeme 1: ciboulette**, NOUN
- grammar: gender Fem; countability Mass
- said: /si.bu.ˈlɛt/ (fr-FR)
  1. [B1] plante aromatique aux feuilles fines et creuses, au léger goût d'oignon
     - example: Coupez finement un peu de ciboulette et ajoutez-la à l'omelette.

### 59. sinon

**Lexeme 1: sinon**, CONJ
- grammar: invariable True
- said: /si.ˈnɔ̃/ (fr-FR)
  1. [A2] dans le cas contraire, introduit ce qui arrivera autrement
     - example: Mets ton manteau, sinon tu vas attraper froid.
  2. [B2] à part, excepté, surtout après une négation ou une question
     - example: Que pouvions-nous faire sinon attendre la fin de l'orage ?
     - frames: ne… rien sinon…; que… sinon… ?
  3. [B2] pour proposer une idée plus forte, sans en être tout à fait sûr
     - example: C'est l'un des meilleurs restaurants, sinon le meilleur, de la ville.
     - frames: un des…, sinon le…

### 60. encore

**Lexeme 1: encore**, ADV
- said: /ɑ̃.ˈkɔʁ/ (fr-FR)
  1. [A1] jusqu'à maintenant ou jusqu'au moment dont on parle, sans arrêt
     - example: À minuit, mes voisins faisaient encore du bruit dans la cuisine.
     - frames: pas encore
  2. [A1] une fois de plus, de nouveau
     - example: Tu as encore oublié tes clés à la maison ?
     - frames: encore une fois
  3. [A2] en plus de ce qu'il y a déjà, en quantité supplémentaire
     - example: Voulez-vous encore un peu de soupe avant le dessert ?
     - frames: encore un/du + nom
  4. [B1] devant un comparatif, pour renforcer la différence : davantage
     - example: Cette année, les loyers sont encore plus chers qu'avant.
     - frames: encore plus/moins + adj

### 61. date

**Lexeme 1: date**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈdat/ (fr-FR)
  1. [A1] indication du jour, du mois et de l'année
     - example: N'oubliez pas d'écrire la date en haut de la feuille.
     - frames: la date de qqch; à la date du...
  2. [A2] moment précis ou époque où un événement a lieu
     - example: Nous avons enfin fixé une date pour notre mariage.
     - frames: de longue date; fixer une date
- relations: confusable datte (NOUN): same pronunciation; datte is the fruit of the date palm

### 62. geisha

**Lexeme 1: geisha**, NOUN
- grammar: gender Fem; countability Count
- said: /ɡɛ.ˈʃa/ (fr-FR), /ɡɛj.ˈʃa/ (fr-FR)
  1. [B2] femme japonaise formée aux arts traditionnels pour divertir des invités
     - example: Dans ce vieux quartier de Kyoto, on croise parfois une geisha en kimono.

### 63. heure

**Lexeme 1: heure**, NOUN
- grammar: gender Fem; countability Count; h_aspire False
- said: /ˈœʁ/ (fr-FR)
  1. [A1] durée de soixante minutes, il y en a vingt-quatre par jour
     - example: Le trajet en train dure environ deux heures.
     - frames: à l'heure (par heure); être payé à l'heure
  2. [A1] moment précis de la journée, tel que l'indique une montre
     - example: Tu peux me dire quelle heure il est, s'il te plaît ?
     - frames: quelle heure est-il ?; à … heures
  3. [A2] moment prévu ou habituel pour faire quelque chose
     - example: C'est l'heure de partir, les enfants, mettez vos chaussures !
     - frames: l'heure de + nom; l'heure de + inf; être à l'heure
  4. [B2] période ou moment particulier d'une époque ou d'une vie
     - example: À l'heure actuelle, beaucoup de jeunes travaillent depuis chez eux.
     - frames: à l'heure actuelle; à l'heure de + nom

### 64. mal

**Lexeme 1: mal**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈmal/ (fr-FR)
- forms: maux [Number=Plur] /ˈmo/
  1. [A1] douleur ressentie dans une partie du corps
     - example: J'ai mal au dos depuis ce matin.
     - frames: avoir mal à + partie du corps; faire mal à qqn
  2. [A2] peine, effort nécessaire pour réussir à faire quelque chose
     - example: Nous avons du mal à comprendre ses explications.
     - frames: avoir du mal à + inf
  3. [B1] tort ou dommage fait à une personne ou à une chose
     - example: Ces paroles dures lui ont fait beaucoup de mal.
     - frames: faire du mal à qqn
  4. [B1] ce qui est contraire à la morale et à la vertu
     - example: Les enfants apprennent peu à peu à distinguer le bien du mal.
     - opposites: bien
  5. [B2] problème grave ou maladie qui touche une personne ou une société
     - example: Le chômage reste l'un des grands maux de notre époque.
- relations: confusable mâle (NOUN): similar spelling and sound; mâle means male
**Lexeme 2: mal**, ADV
- grammar: comparable True
- said: /ˈmal/ (fr-FR)
  1. [A1] d'une manière qui n'est pas bonne ou pas correcte
     - example: Tu as mal fermé la porte, elle est encore ouverte.
     - opposites: bien
  2. [A2] dans un mauvais état de santé ou de confort
     - example: Après ce long voyage en bateau, je me sens mal.
     - frames: se sentir mal; être mal
     - opposites: bien
**Lexeme 3: mal**, ADJ
- grammar: invariable True; position Pred
- said: /ˈmal/ (fr-FR)
  1. [B1] contraire à la morale, à ce qu'on doit faire
     - example: Mentir à ses meilleurs amis, c'est vraiment mal.
     - frames: c'est mal de + inf
     - opposites: bien

### 65. prix

**Lexeme 1: prix**, NOUN
- grammar: gender Masc; countability Count; note plural unchanged: les prix
- said: /ˈpʁi/ (fr-FR)
  1. [A1] somme d'argent qu'il faut payer pour acheter une chose
     - example: Quel est le prix de ce manteau en vitrine ?
     - frames: le prix de qqch
  2. [A2] récompense donnée au meilleur dans un concours ou une compétition
     - example: Notre classe a remporté le premier prix du concours de dessin.
     - frames: remporter un prix; le prix de qqch
  3. [B2] ce qu'on doit sacrifier ou supporter pour obtenir quelque chose
     - example: Il a fini par réussir, mais au prix de grands sacrifices.
     - frames: au prix de qqch; à tout prix
     - labels: figurative True

### 66. perdu

**Lexeme 1: perdu**, ADJ
- grammar: comparable False
- said: /pɛʁ.ˈdy/ (fr-FR)
  1. [A2] qu'on ne trouve plus, qu'on ne sait plus où chercher
     - example: Les objets perdus sont rangés à l'accueil de la gare.
  2. [A2] qui ne sait plus où il se trouve ni quel chemin prendre
     - example: Sans carte, nous étions perdus au milieu de la forêt.
  3. [B1] qui ne comprend plus rien, ne sait plus quoi faire
     - example: Avec toutes ces règles, je suis complètement perdu.
     - labels: figurative True
  4. [B1] situé loin de tout, difficile à atteindre
     - example: Ils habitent dans un petit village perdu dans la montagne.
     - opposites: central
  5. [B1] utilisé sans profit, qui n'a servi à rien
     - example: Attendre ici une heure, c'est vraiment du temps perdu.
  6. [B2] qui n'a plus aucune chance d'être sauvé ou de réussir
     - example: Quand la police est arrivée, le voleur a compris qu'il était perdu.
     - frames: tout est perdu
- relations: derived_from perdre (VERB)

### 67. sonnet

**Lexeme 1: sonnet**, NOUN
- grammar: gender Masc; countability Count
- said: /sɔ.ˈnɛ/ (fr-FR)
  1. [B2] poème de quatorze vers formé de deux quatrains et de deux tercets
     - example: Pour demain, chaque élève doit apprendre un sonnet de la Renaissance.
     - labels: domain literature

### 68. mouvement

**Lexeme 1: mouvement**, NOUN
- grammar: gender Masc; countability Count
- said: /muv.ˈmɑ̃/ (fr-FR)
  1. [A2] déplacement d'un corps, d'un objet ou d'une partie du corps
     - example: Ne fais aucun mouvement brusque, le chat pourrait avoir peur.
     - frames: en mouvement
     - opposites: immobilité
  2. [B1] groupe de personnes qui agissent ensemble pour défendre des idées
     - example: Ce mouvement écologiste attire de plus en plus de jeunes.
  3. [B2] réaction soudaine du cœur ou de l'esprit, élan intérieur
     - example: Dans un mouvement de colère, elle a claqué la porte.
     - frames: un mouvement de + nom
  4. [C1] chacune des grandes parties d'une symphonie ou d'un concerto
     - example: Le deuxième mouvement de cette symphonie est lent et triste.
     - labels: domain music
- relations: derived_from mouvoir (VERB)

### 69. type

**Lexeme 1: type**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈtip/ (fr-FR)
  1. [A2] ensemble de choses ou de personnes qui ont les mêmes caractères
     - example: Quel type de musique écoutes-tu le plus souvent ?
     - frames: type de + nom
  2. [B1] homme dont on ne connaît pas ou ne dit pas le nom
     - example: Un drôle de type m'a demandé l'heure dans la rue.
     - labels: formality informal
  3. [B2] personne ou chose qui représente parfaitement une catégorie
     - example: Ce vieux professeur est le type même du savant distrait.
     - frames: le type même de + nom

### 70. téléspectateur

**Lexeme 1: téléspectateur**, NOUN
- grammar: gender Masc; countability Count
- said: /te.le.spɛk.ta.ˈtœʁ/ (fr-FR)
- forms: téléspectatrice [Gender=Fem, Number=Sing] /te.le.spɛk.ta.ˈtʁis/
  1. [B1] personne qui regarde une émission ou un programme sur un poste de télévision
     - example: Des millions de téléspectateurs ont suivi la finale hier soir.
- relations: derived_from spectateur (NOUN)

### 71. régime

**Lexeme 1: régime**, NOUN
- grammar: gender Masc; countability Count
- said: /ʁe.ˈʒim/ (fr-FR)
  1. [B1] manière dont un pays est gouverné, forme de gouvernement
     - example: Ce pays a connu un régime militaire pendant vingt ans.
     - frames: régime politique
     - labels: domain politics
  2. [A2] façon de manger contrôlée, souvent pour perdre du poids ou se soigner
     - example: Depuis janvier, je suis au régime et je ne mange plus de sucre.
     - frames: être au régime; faire un régime; se mettre au régime
  3. [B2] ensemble des règles qui organisent une situation ou une institution
     - example: Les salariés de cette entreprise ont un régime de retraite particulier.
     - frames: régime de qqch
  4. [C1] vitesse de fonctionnement d'un moteur ou d'une activité
     - example: Avant les fêtes, l'usine tourne à plein régime jour et nuit.
     - frames: à plein régime
  5. [C1] grosse grappe de fruits qui pousse sur un bananier ou un palmier
     - example: Il portait sur l'épaule un lourd régime de bananes vertes.
     - frames: régime de bananes; régime de dattes

### 72. exécutoire

**Lexeme 1: exécutoire**, ADJ
- grammar: comparable False
- said: /ɛɡ.ze.ky.ˈtwaʁ/ (fr-FR)
  1. [C1] qui doit être appliqué et peut l'être, au besoin par la force publique
     - example: Ce jugement sera exécutoire dès la fin du mois prochain.
     - frames: exécutoire par provision
     - labels: domain law
- relations: confusable exutoire (NOUN): similar sound; exutoire means an outlet or release for feelings, and exécutoire is sometimes wrongly used for it

### 73. truquer

**Lexeme 1: truquer**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /tʁy.ˈke/ (fr-FR)
  1. [B2] modifier une chose en secret pour tromper ou fausser un résultat
     - example: Les organisateurs ont truqué le match pour gagner de l'argent.
     - frames: truquer qqch; un match truqué; une photo truquée
- relations: derived_from truc (NOUN)

### 74. international

**Lexeme 1: international**, ADJ
- grammar: comparable False
- said: /ɛ̃.tɛʁ.na.sjɔ.ˈnal/ (fr-FR)
- forms: internationaux [Gender=Masc, Number=Plur] /ɛ̃.tɛʁ.na.sjɔ.ˈno/
  1. [A2] qui concerne plusieurs pays ou se passe entre plusieurs pays
     - example: Notre ville accueille chaque été un festival international de musique.
     - opposites: national
- relations: derived_from national (ADJ)
**Lexeme 2: international**, NOUN
- grammar: gender Masc; countability Count
- said: /ɛ̃.tɛʁ.na.sjɔ.ˈnal/ (fr-FR)
- forms: internationaux [Gender=Masc, Number=Plur] /ɛ̃.tɛʁ.na.sjɔ.ˈno/; internationale [Gender=Fem, Number=Sing] /ɛ̃.tɛʁ.na.sjɔ.ˈnal/
  1. [B2] sportif choisi pour jouer dans l'équipe qui représente son pays
     - example: Ce jeune international a déjà marqué trois buts cette saison.
     - labels: domain sport
- relations: derived_from international (ADJ)

### 75. ouverture

**Lexeme 1: ouverture**, NOUN
- grammar: gender Fem; countability Count
- said: /u.vɛʁ.ˈtyʁ/ (fr-FR)
  1. [A2] action de rendre accessible un lieu ou un objet fermé
     - example: Les heures d'ouverture du magasin sont affichées sur la porte.
     - opposites: fermeture
  2. [B1] espace vide qui permet de passer ou de voir à travers
     - example: Le chat s'est glissé par une petite ouverture dans le mur.
  3. [B1] début officiel d'une activité, d'une saison ou d'une réunion
     - example: Nous attendons avec impatience l'ouverture de la saison de ski.
     - frames: l'ouverture de qqch
     - opposites: clôture
  4. [B2] attitude d'une personne qui accepte les idées différentes des siennes
     - example: Cette enseignante fait preuve d'une grande ouverture d'esprit.
     - frames: ouverture d'esprit; ouverture à qqch
     - grammar: countability Mass
  5. [C1] morceau de musique joué par l'orchestre au début d'un opéra
     - example: Avant le lever du rideau, l'orchestre joue une longue ouverture.
     - labels: domain music
- relations: verbal_noun_of ouvrir (VERB)

### 76. belote

**Lexeme 1: belote**, NOUN
- grammar: gender Fem; countability Both
- said: /bə.ˈlɔt/ (fr-FR)
  1. [B1] jeu de cartes à trente-deux cartes, souvent joué à quatre en équipes
     - example: Le dimanche après-midi, mes grands-parents jouent à la belote avec leurs voisins.
     - frames: jouer à la belote; faire une belote
  2. [C1] réunion du roi et de la dame d'atout dans la main d'un joueur
     - example: Il pose le roi d'atout en annonçant belote, puis la dame avec rebelote.
     - frames: belote et rebelote

### 77. fanon

**Lexeme 1: fanon**, NOUN
- grammar: gender Masc; countability Count
- said: /fa.ˈnɔ̃/ (fr-FR)
  1. [C1] lame souple et cornée qui pend de la mâchoire supérieure de la baleine
     - example: La baleine filtre l'eau de mer à travers ses fanons.
     - labels: domain zoology
  2. [C2] pli de peau qui pend sous le cou de certains animaux
     - example: Le vieux taureau avait un fanon épais qui tombait jusqu'au poitrail.

### 78. doute

**Lexeme 1: doute**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈdut/ (fr-FR)
  1. [B1] état d'esprit de quelqu'un qui n'est pas sûr qu'une chose soit vraie
     - example: J'ai encore un doute sur la date exacte de la réunion.
     - frames: avoir un doute sur qqch; mettre qqch en doute; il n'y a pas de doute que
     - opposites: certitude
  2. [B2] manque de confiance envers quelqu'un, soupçon sur son honnêteté
     - example: Ses explications confuses ont fait naître des doutes chez les enquêteurs.
     - frames: avoir des doutes sur qqn
     - opposites: confiance
- relations: derived_from douter (VERB)

### 79. bon

**Lexeme 1: bon**, ADJ
- grammar: comparable True; note comparative is meilleur, not plus bon; usually placed before the noun
- said: /ˈbɔ̃/ (fr-FR)
- forms: bonne [Gender=Fem, Number=Sing] /ˈbɔn/; bonnes [Gender=Fem, Number=Plur] /ˈbɔn/; meilleur [Degree=Cmp, Gender=Masc, Number=Sing] /mɛ.ˈjœʁ/
  1. [A1] qui a les qualités attendues, de qualité ou d'un niveau élevé
     - example: C'est un très bon film, je te le conseille.
     - opposites: mauvais
  2. [A1] qui convient, qui est juste ou exact dans une situation
     - example: Tu as trouvé la bonne réponse du premier coup !
     - opposites: mauvais
  3. [A1] employé dans les vœux pour souhaiter quelque chose d'agréable à quelqu'un
     - example: Bonne journée, et à ce soir à la maison !
  4. [A1] agréable à manger, à boire ou à goûter
     - example: Cette soupe aux légumes est vraiment très bonne.
     - opposites: mauvais
  5. [A2] qui fait du bien, utile ou favorable à quelqu'un ou quelque chose
     - example: Manger des fruits chaque jour est bon pour la santé.
     - frames: bon pour qqn/qqch
  6. [A2] qui réussit dans une activité, qui a du talent pour elle
     - example: Ma sœur est très bonne en mathématiques.
     - frames: bon en qqch
     - opposites: mauvais
  7. [B1] qui aime faire le bien, généreux et gentil avec les autres
     - example: Notre grand-mère était une femme simple et bonne.
     - opposites: méchant
  8. [B1] un peu plus grand ou plus long que la quantité indiquée
     - example: J'ai attendu le bus une bonne demi-heure ce matin.
- relations: confusable bien (ADV): learners use the adjective bon where the adverb bien is needed (il chante bien)
**Lexeme 2: bon**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈbɔ̃/ (fr-FR)
  1. [B1] papier qui permet d'obtenir un produit, un service ou de l'argent
     - example: Avec ce bon d'achat, vous pouvez choisir un livre.
     - frames: bon de qqch
**Lexeme 3: bon**, INTJ
- grammar: invariable True
- said: /ˈbɔ̃/ (fr-FR)
  1. [A2] mot qui sert à conclure, à accepter ou à passer à autre chose
     - example: Bon, il est tard, on rentre à la maison ?
**Lexeme 4: bon**, ADV
- grammar: invariable True
- said: /ˈbɔ̃/ (fr-FR)
  1. [A2] de manière agréable, en parlant d'une odeur
     - example: Les roses du jardin sentent bon au printemps.
     - frames: sentir bon
  2. [A2] avec une température douce et agréable
     - example: Ce soir, il fait bon sur la terrasse.
     - frames: il fait bon

### 80. contrat

**Lexeme 1: contrat**, NOUN
- grammar: gender Masc; countability Count
- said: /kɔ̃.ˈtʁa/ (fr-FR)
  1. [A2] accord officiel entre personnes qui s'engagent, souvent écrit et signé
     - example: Avant de commencer, vous devez signer votre contrat de travail.
     - frames: signer un contrat; contrat de travail; contrat avec qqn
  2. [C1] accord pour faire tuer quelqu'un contre de l'argent
     - example: Un tueur à gages a accepté un contrat sur ce juge.
     - frames: contrat sur qqn
     - labels: formality informal

### 81. paix

**Lexeme 1: paix**, NOUN
- grammar: gender Fem; countability Both
- said: /ˈpɛ/ (fr-FR)
  1. [A2] situation d'un pays ou d'un peuple qui n'est pas en guerre
     - example: Après des années de combats, le pays vit enfin en paix.
     - frames: en paix; signer la paix
     - opposites: guerre
  2. [B1] calme et tranquillité, quand personne ne vous dérange
     - example: Laisse-moi en paix, j'essaie de lire mon livre.
     - frames: laisser qqn en paix; avoir la paix
     - grammar: countability Mass
  3. [B1] bonne entente entre des personnes, sans dispute ni conflit
     - example: Ils se sont disputés hier, mais ce matin ils ont fait la paix.
     - frames: faire la paix avec qqn
     - grammar: countability Mass

### 82. compte

**Lexeme 1: compte**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈkɔ̃t/ (fr-FR)
  1. [A2] argent déposé dans une banque au nom d'une personne
     - example: J'ai ouvert un compte dans une banque près de chez moi.
     - frames: compte en banque; ouvrir un compte
  2. [A2] accès personnel à un site ou un service, avec un identifiant
     - example: Pour commander, vous devez d'abord créer un compte sur le site.
     - frames: créer un compte
  3. [B1] calcul d'un nombre ou d'une quantité, et total obtenu
     - example: Fais le compte des invités avant de réserver la salle.
     - frames: faire le compte de qqch; le compte est bon
  4. [B2] explications qu'on doit donner sur ses actes à une autorité
     - example: Tôt ou tard, le ministre devra rendre des comptes aux citoyens.
     - frames: rendre des comptes à qqn; demander des comptes à qqn
- relations: derived_from compter (VERB); confusable conte (NOUN): same pronunciation; conte means a story or tale; confusable comte (NOUN): same pronunciation; comte is a nobleman, a count

### 83. coter

**Lexeme 1: coter**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /kɔ.ˈte/ (fr-FR)
  1. [B2] fixer le prix d'une action ou d'une marchandise sur un marché financier
     - example: Cette entreprise familiale est cotée en Bourse depuis l'an dernier.
     - frames: être coté en Bourse; coter + prix
     - labels: domain finance
     - grammar: valency Both
  2. [B2] juger la valeur de quelqu'un ou de quelque chose en lui donnant une note
     - example: Le guide cote chaque restaurant de une à cinq étoiles.
     - frames: coter qqch sur + nombre; être bien coté
     - grammar: valency Tr
  3. [C1] indiquer les mesures exactes sur un plan ou un dessin technique
     - example: Avant d'envoyer le plan, l'architecte doit encore coter toutes les pièces.
     - labels: domain technology
     - grammar: valency Tr
- relations: derived_from cote (NOUN)

### 84. flocon

**Lexeme 1: flocon**, NOUN
- grammar: gender Masc; countability Count
- said: /flɔ.ˈkɔ̃/ (fr-FR)
  1. [A2] petit morceau léger de neige qui tombe du ciel
     - example: De gros flocons tombent sur la ville depuis ce matin.
     - frames: flocon de neige
  2. [B2] petite masse légère et douce de laine, de coton ou de poussière
     - example: Sous le lit, j'ai trouvé quelques flocons de poussière grise.
     - frames: flocon de + nom
  3. [B1] petite lamelle de céréale ou de légume, aplatie et séchée
     - example: Chaque matin, je mange des flocons d'avoine avec du lait.
     - frames: flocons d'avoine; purée en flocons
     - grammar: note mostly in the plural

### 85. centraliser

**Lexeme 1: centraliser**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /sɑ̃.tʁa.li.ˈze/ (fr-FR)
  1. [B2] rassembler en un même lieu ou entre les mains d'une seule personne
     - example: Notre secrétariat centralise toutes les demandes d'inscription des étudiants.
     - frames: centraliser qqch
     - opposites: disperser
  2. [C1] faire dépendre toutes les décisions d'un État d'un seul pouvoir, dans la capitale
     - example: Pendant des siècles, les rois ont cherché à centraliser l'administration du pays.
     - frames: centraliser le pouvoir
     - opposites: décentraliser
- relations: derived_from central (ADJ)

### 86. maquereau

**Lexeme 1: maquereau**, NOUN
- grammar: gender Masc; countability Both
- said: /ma.ˈkʁo/ (fr-FR)
  1. [B1] poisson de mer au dos bleu et vert rayé de noir
     - example: Ce soir, nous mangeons des maquereaux grillés avec des pommes de terre.
**Lexeme 2: maquereau**, NOUN
- grammar: gender Masc; countability Count
- said: /ma.ˈkʁo/ (fr-FR)
  1. [C1] homme qui vit de l'argent gagné par des prostituées
     - example: La police a enfin arrêté le maquereau qui exploitait ces jeunes femmes.
     - labels: formality informal, attitude derogatory

### 87. mieux

**Lexeme 1: mieux**, ADV
- grammar: invariable True; note comparatif de « bien » ; superlatif « le mieux »
- said: /ˈmjø/ (fr-FR)
  1. [A2] d'une manière plus satisfaisante, plus juste ou plus réussie
     - example: Parle plus lentement, je comprendrai mieux ce que tu dis.
     - frames: mieux que qqn/qqch; aller mieux; aimer mieux + inf.
  2. [A2] de la manière la plus réussie, avec le plus d'habileté
     - example: C'est elle qui danse le mieux de toute la classe.
     - frames: le mieux
- relations: confusable meilleur (ADJ): both translate 'better'; meilleur is the adjective comparative of bon, mieux the adverb comparative of bien
**Lexeme 2: mieux**, ADJ
- grammar: invariable True; position Pred
- said: /ˈmjø/ (fr-FR)
  1. [A2] plus satisfaisant, plus convenable, préférable
     - example: Ce sera mieux si nous partons avant la nuit.
  2. [B1] plus beau, plus agréable à regarder
     - example: Tu es mieux avec les cheveux courts, crois-moi.
     - labels: formality informal
- relations: confusable meilleur (ADJ): both mean 'better'; after être, mieux is often used where English learners expect meilleur
**Lexeme 3: mieux**, NOUN
- grammar: gender Masc; number Sing
- said: /ˈmjø/ (fr-FR)
  1. [A2] ce qu'on peut faire de plus réussi, le maximum possible
     - example: Ne t'inquiète pas, je ferai de mon mieux demain.
     - frames: faire de son mieux; au mieux
  2. [B2] changement favorable, progrès, surtout dans l'état d'un malade
     - example: On observe un léger mieux dans son état depuis hier.

### 88. désinfecter

**Lexeme 1: désinfecter**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /de.zɛ̃.fɛk.ˈte/ (fr-FR)
  1. [B1] nettoyer quelque chose pour détruire les microbes qui s'y trouvent
     - example: Avant de mettre le pansement, il faut bien désinfecter la plaie.
     - frames: désinfecter qqch avec qqch
  2. [B2] retirer d'un ordinateur ou d'un fichier les virus et programmes dangereux
     - example: Ce logiciel gratuit permet de désinfecter ton ordinateur en quelques minutes.
     - labels: domain computing
- relations: derived_from infecter (VERB)

### 89. annonce

**Lexeme 1: annonce**, NOUN
- grammar: gender Fem; countability Count
- said: /a.ˈnɔ̃s/ (fr-FR)
  1. [B1] message par lequel on fait savoir une nouvelle au public
     - example: L'annonce de son départ a surpris toute l'équipe hier soir.
     - frames: l'annonce de qqch; faire une annonce
  2. [A2] court texte publié pour vendre, louer, chercher quelque chose ou proposer un emploi
     - example: Je lis les petites annonces pour trouver un appartement moins cher.
     - frames: petite annonce; passer une annonce
- relations: verbal_noun_of annoncer (VERB)

### 90. machination

**Lexeme 1: machination**, NOUN
- grammar: gender Fem; countability Count
- said: /ma.ʃi.na.ˈsjɔ̃/ (fr-FR)
  1. [C1] ensemble de manœuvres secrètes préparées pour nuire à quelqu'un
     - example: Le ministre affirme être la victime d'une machination politique.
     - frames: une machination contre qqn; ourdir une machination

### 91. treuil

**Lexeme 1: treuil**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈtʁœj/ (fr-FR)
  1. [C1] appareil qui enroule un câble autour d'un cylindre pour tirer ou lever des charges
     - example: Grâce au treuil, ils ont pu sortir la voiture du fossé.

### 92. paprika

**Lexeme 1: paprika**, NOUN
- grammar: gender Masc; countability Mass
- said: /pa.pʁi.ˈka/ (fr-FR)
  1. [B1] épice rouge en poudre, faite avec un piment séché et moulu
     - example: Saupoudrez un peu de paprika sur les œufs avant de servir.

### 93. toujours

**Lexeme 1: toujours**, ADV
- said: /tu.ˈʒuʁ/ (fr-FR)
  1. [A1] à tous les moments, chaque fois, sans aucune exception
     - example: Mon grand-père se lève toujours avant le soleil.
     - frames: pour toujours; comme toujours
     - opposites: jamais
  2. [A2] encore maintenant, comme avant, jusqu'au moment présent
     - example: Tu habites toujours dans le même quartier ?
     - frames: toujours pas
  3. [B1] en tout cas, malgré tout, au moins pour essayer
     - example: Tu peux toujours lui demander, il dira peut-être oui.
     - frames: pouvoir toujours + inf

### 94. niveau

**Lexeme 1: niveau**, NOUN
- grammar: gender Masc; countability Count
- said: /ni.ˈvo/ (fr-FR)
  1. [A2] hauteur d'une chose ou d'une surface par rapport à un point de repère
     - example: Après les fortes pluies, le niveau de la rivière a beaucoup monté.
     - frames: au niveau de qqch; le niveau de la mer
  2. [A2] degré de qualité, de connaissance ou d'importance atteint par quelqu'un ou quelque chose
     - example: Tu as un très bon niveau en anglais maintenant.
     - frames: niveau en qqch; niveau de vie
  3. [B1] chacun des étages d'un bâtiment, d'un parking ou d'un lieu
     - example: Prenez l'ascenseur jusqu'au troisième niveau du parking.

### 95. mémoire

**Lexeme 1: mémoire**, NOUN
- grammar: gender Fem; countability Both
- said: /me.ˈmwaʁ/ (fr-FR)
  1. [A2] capacité de l'esprit à garder et retrouver les souvenirs et les connaissances
     - example: Ma grand-mère a encore une très bonne mémoire.
     - frames: avoir de la mémoire; de mémoire
  2. [B1] souvenir qu'on garde d'une personne morte ou d'un événement passé
     - example: Ce monument rappelle la mémoire des soldats morts pendant la guerre.
     - frames: à la mémoire de qqn; en mémoire de qqn
  3. [B1] partie d'un ordinateur qui conserve les données et les programmes
     - example: Ton téléphone n'a plus assez de mémoire pour ces photos.
     - labels: domain computing
**Lexeme 2: mémoire**, NOUN
- grammar: gender Masc; countability Count
- said: /me.ˈmwaʁ/ (fr-FR)
  1. [B2] long travail écrit présenté à la fin d'études universitaires
     - example: Elle rédige son mémoire de master sur la poésie moderne.
     - frames: mémoire de master; mémoire sur qqch
  2. [B2] récit écrit par une personne sur les événements de sa propre vie
     - example: Le vieux général a enfin publié ses mémoires l'an dernier.
     - grammar: number Plur

### 96. faire

**Lexeme 1: faire**, VERB
- grammar: irregular True; conjugation 3; auxiliary have; valency Both
- said: /ˈfɛʁ/ (fr-FR)
- forms: fais [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈfɛ/; fait [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈfɛ/; faisons [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /fə.ˈzɔ̃/; faites [Mood=Ind, Tense=Pres, Person=2, Number=Plur, VerbForm=Fin] /ˈfɛt/; font [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈfɔ̃/; fait [Tense=Past, VerbForm=Part] /ˈfɛ/; fit [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /ˈfi/; ferai [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin] /fə.ˈʁe/; fasse [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈfas/
  1. [A1] accomplir une action ou pratiquer une activité
     - example: Qu'est-ce que tu fais ce week-end avec tes amis ?
     - frames: faire qqch; faire du sport / de la musique
     - grammar: valency Tr
  2. [A1] fabriquer, créer ou préparer quelque chose
     - example: Chaque dimanche, ma grand-mère fait un gâteau au chocolat.
     - frames: faire qqch
     - grammar: valency Tr
  3. [A2] être la cause d'une action, pousser quelqu'un ou quelque chose à agir
     - example: Ce film triste m'a fait pleurer du début à la fin.
     - frames: faire + inf; faire faire qqch à qqn
     - grammar: valency Tr
  4. [A1] indiquer le temps ou la température qu'il y a dehors
     - example: Il fait très froid ce matin, prends ton manteau.
     - frames: il fait beau / froid / chaud
     - grammar: valency Impers
  5. [A2] avoir pour mesure, poids, prix ou résultat
     - example: Deux paires de chaussures, ça fait cent euros en tout.
     - frames: ça fait combien ?
     - grammar: valency Tr
  6. [A2] indiquer combien de temps s'est passé depuis un moment
     - example: Ça fait trois ans que nous habitons dans cette ville.
     - frames: ça fait + durée + que
     - grammar: valency Impers
  7. [B1] avoir l'apparence de, sembler avoir tel âge ou telle qualité
     - example: Avec ce costume, tu fais beaucoup plus vieux que ton frère.
     - frames: faire + adj
     - grammar: valency Intr
  8. [B1] jouer un rôle, se comporter comme une certaine personne
     - example: Arrête de faire l'idiot, tout le monde nous regarde.
     - frames: faire l'idiot / le malin
     - grammar: valency Tr

### 97. zoomer

**Lexeme 1: zoomer**, VERB
- grammar: conjugation 1; valency Both; auxiliary have
- said: /zu.ˈme/ (fr-FR)
  1. [B1] grossir une partie d'une image, avec un appareil photo ou sur un écran
     - example: Zoome sur la carte pour voir le nom de la rue.
     - frames: zoomer sur qqch; zoomer qqch
     - grammar: valency Both
     - opposites: dézoomer
  2. [B2] porter toute son attention sur un point précis d'un sujet
     - example: Dans ce chapitre, l'auteur zoome sur la vie des ouvriers.
     - frames: zoomer sur qqch
     - labels: figurative True, formality informal
     - grammar: valency Intr
- relations: derived_from zoom (NOUN)

### 98. sûr

**Lexeme 1: sûr**, ADJ
- grammar: comparable True
- said: /ˈsyʁ/ (fr-FR)
  1. [A2] qui n'a aucun doute sur quelque chose, qui en est convaincu
     - example: Tu es sûr de vouloir partir si tôt ce soir ?
     - frames: être sûr de qqch / de + inf.; être sûr que + ind.; sûr de soi
     - opposites: incertain
  2. [A2] dont on ne peut pas douter, qui va arriver à coup de certitude
     - example: Une chose est sûre : demain, il fera plus froid.
     - frames: c'est sûr
     - opposites: incertain
  3. [B1] où l'on ne court aucun danger, sans risque
     - example: Ce quartier reste calme et très sûr, même la nuit.
     - opposites: dangereux
  4. [B1] en qui ou en quoi l'on peut avoir confiance
     - example: Pour ce travail, il me faut une personne sûre et discrète.
- relations: confusable sur (ADP): pronounced the same; the preposition 'on' has no circumflex

### 99. emballement

**Lexeme 1: emballement**, NOUN
- grammar: gender Masc; countability Count
- said: /ɑ̃.bal.ˈmɑ̃/ (fr-FR)
  1. [C1] accélération brusque et incontrôlée d'un moteur, d'un animal ou d'un phénomène
     - example: Les experts redoutent un emballement des prix dans les mois à venir.
     - frames: l'emballement de qqch; emballement médiatique
  2. [C1] enthousiasme vif et soudain, souvent irréfléchi et passager
     - example: Ne prends pas de décision importante sous le coup de l'emballement.
     - frames: emballement pour qqch / qqn
- relations: verbal_noun_of emballer (VERB); confusable emballage (NOUN): emballage is packaging (from emballer, to wrap); emballement comes from s'emballer, to bolt or get carried away

### 100. pylône

**Lexeme 1: pylône**, NOUN
- grammar: gender Masc; countability Count
- said: /pi.ˈlon/ (fr-FR)
  1. [B2] haute construction en métal ou en béton qui porte des câbles ou des antennes
     - example: Un pylône électrique est tombé sur la route pendant la tempête.
     - frames: pylône électrique
  2. [C2] grande porte monumentale à l'entrée d'un temple de l'ancienne Égypte
     - example: Les visiteurs entrent dans le temple en passant sous un immense pylône.
     - labels: domain architecture
