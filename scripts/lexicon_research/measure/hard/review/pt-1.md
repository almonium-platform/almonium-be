# Portuguese: two entries for each word (11 words)

Each word below has two entries of a learner's dictionary of Portuguese, A and B, written by two different writers
under the same rules. You are not told which writer wrote which, and the sides change from word to word. Some of
these words are hard on purpose: two words under one spelling, forms that are not words of their own, senses of
one country only, rude words.

For each word say which entry serves a learner better, and name the faults of each. A writer may have refused a
word as no dictionary word of its own ("refused: inflected form of ..."); that is right for an inflected form, an
abbreviation or a name, and wrong for a word a dictionary of Portuguese lists.

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
  sense, or an example is not natural Portuguese.
- **collocation**: a collocation that is not said that way in Portuguese; or one that does not belong
  among the ten a learner is shown for the sense, because anyone would build it from the meanings
  or because it is rare; or a list that lacks the commonest partner of the sense while it has
  rarer ones. Quote the collocation in "what". A frame with the wrong preposition or case is a
  fact, not this.

These are this dictionary's own definitions, not faults:

- valency Refl stands for every use with a reflexive pronoun, whatever a grammar of Portuguese calls it
  (reflexive, reciprocal, pronominal); Tr means the verb takes a direct object, so a verb whose
  object needs a preposition or the dative is Intr.
- a grammar value (countability, valency, position, number) says how the word is ordinarily used, as
  a learner's dictionary of Portuguese prints it: "Mass" is "uncountable" in that sense, although kinds
  of the thing can be counted. Followed by "mainly", the other use is common too. Report a value
  only when a learner's dictionary would not print it for this word or sense.
- position Attr means the adjective is used joined to a noun and not after a verb such as "be",
  wherever Portuguese puts it (before the noun in English, mostly after it in French); Pred means only
  after such a verb. Neither says on which side of the noun the adjective stands.
- a lexeme is one word with one way of saying and inflecting it. Two homonyms that are said and
  inflected alike are senses of one lexeme here, although dictionaries of Portuguese number them apart.
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

### 1. sede

#### Entry A

**Lexeme 1: sede**, NOUN
- grammar: gender Fem; countability Mass
- said: /ˈse.dʒi/ (pt-BR), /ˈse.dɨ/ (pt-PT)
  1. [A1] necessidade ou vontade de beber água ou outro líquido
     - example: Depois da corrida, fiquei com tanta sede que bebi dois copos.
     - collocations: ter sede; estar com sede; matar a sede; morrer de sede; sentir sede; dar sede
  2. [B2] desejo forte e impaciente de ter ou conseguir alguma coisa
     - example: A sede de poder acabou por destruir a carreira do ministro.
     - frames: sede de algo
     - collocations: sede de poder; sede de vingança; sede de justiça; sede de conhecimento
     - labels: figurative True
**Lexeme 2: sede**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈsɛ.dʒi/ (pt-BR), /ˈse.dʒi/ (pt-BR), /ˈsɛ.dɨ/ (pt-PT)
  1. [B1] lugar onde funciona a direção principal de uma organização ou governo
     - example: A sede da empresa fica no centro da cidade.
     - frames: sede de algo; ter sede em + lugar
     - collocations: sede social; sede do governo; sede central; mudar de sede
  2. [B2] lugar escolhido para receber um evento, uma competição ou uma reunião
     - example: Esta cidade foi escolhida como sede dos próximos jogos.
     - frames: sede de algo
     - collocations: cidade-sede; país-sede; ser sede

#### Entry B

**Lexeme 1: sede**, NOUN
- grammar: gender Fem; countability Mass
- said: /ˈse.dʒi/ (pt-BR), /ˈse.dɨ/ (pt-PT)
- spellings: sede (pt-BR, pt-PT); sêde (pt-BR, pt-PT) [older books only]
  1. [A1] vontade ou necessidade física de beber água ou outro líquido
     - example: Fiquei com muita sede depois de caminhar horas ao sol.
     - collocations: ter sede; estar com sede; matar a sede; morrer de sede; sentir sede; passar sede
  2. [B2] desejo intenso de alcançar, obter ou conhecer alguma coisa
     - example: Os jovens estudantes chegaram à universidade com sede de aprender.
     - frames: sede de algo; sede de + inf
     - collocations: sede de poder; sede de vingança; sede de justiça; sede de saber
     - labels: figurative True
**Lexeme 2: sede**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈsɛ.dʒi/ (pt-BR), /ˈsɛ.dɨ/ (pt-PT)
  1. [B1] local principal onde funciona a direção de uma empresa, instituição ou governo
     - example: Transferiram a sede da associação para um prédio mais amplo.
     - frames: sede de algo
     - collocations: sede social; sede própria; sede central; transferir a sede; mudar de sede
  2. [B2] lugar escolhido para receber um grande evento, encontro ou competição
     - example: Qual cidade vai ser a sede do próximo campeonato mundial?
     - frames: sede de algo
     - collocations: ser sede; servir de sede

### 2. colher

#### Entry A

**Lexeme 1: colher**, NOUN
- grammar: gender Fem; countability Count
- said: /ku.ˈʎɛɾ/ (pt-BR), /ko.ˈʎɛɾ/ (pt-BR), /ku.ˈʎɛɾ/ (pt-PT)
  1. [A1] utensílio de mesa com cabo e ponta côncava para comer ou servir
     - example: Mexa o molho devagar com uma colher de pau grande.
     - collocations: colher de sopa; colher de chá; colher de sobremesa; colher de café; colher de pau
  2. [A2] porção que cabe na ponta côncava do utensílio de tomar sopa
     - example: Adicione duas colheres de açúcar à massa antes de bater.
     - frames: colher de algo
     - collocations: colher de sopa; colher de chá; colher cheia; colher rasa
- relations: confusable colher (VERB): spelled the same, but the noun has open /ɛ/ and the verb has close /e/
**Lexeme 2: colher**, VERB
- grammar: conjugation 2; valency Tr; irregular False; note stressed o alternates between /o/ in colho and /ɔ/ in colhe
- said: /ko.ˈʎeɾ/ (pt-BR), /ku.ˈʎeɾ/ (pt-PT), /ko.ˈʎe/ (pt-BR, casual)
- forms: colho [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈko.ʎu/ /ˈko.ʎu/; colhes [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /ˈkɔ.ʎis/ /ˈkɔ.ʎɨʃ/; colhe [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈkɔ.ʎi/ /ˈkɔ.ʎɨ/; colhemos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /ko.ˈʎe.mus/ /ku.ˈʎe.muʃ/; colhem [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈkɔ.ʎẽj/ /ˈkɔ.ʎɐ̃j/; colhi [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /ko.ˈʎi/ /ku.ˈʎi/; colheu [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /ko.ˈʎew/ /ku.ˈʎew/; colha [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈko.ʎɐ/ /ˈko.ʎɐ/; colhido [VerbForm=Part, Tense=Past] /ko.ˈʎi.du/ /ku.ˈʎi.du/
  1. [B1] retirar da planta ou da terra frutos, flores ou produtos agrícolas
     - example: No outono, ajudamos os vizinhos a colher uvas na fazenda.
     - frames: colher algo
     - collocations: colher frutas; colher flores; colher café
  2. [B2] reunir informações, provas, depoimentos ou amostras para estudo ou avaliação
     - example: Os investigadores foram ao local para colher provas e depoimentos.
     - frames: colher algo
     - collocations: colher informações; colher dados; colher depoimentos; colher provas; colher assinaturas; colher sangue
  3. [B2] obter consequências, boas ou más, daquilo que se fez antes
     - example: Depois de anos de esforço, finalmente colhemos bons resultados.
     - frames: colher algo
     - collocations: colher os frutos; colher bons resultados
     - labels: figurative True
- relations: confusable colher (NOUN): spelled the same, but the verb has close /e/ and the noun has open /ɛ/

#### Entry B

**Lexeme 1: colher**, NOUN
- grammar: gender Fem; countability Count
- said: /ko.ˈʎɛɾ/ (pt-BR), /ku.ˈʎɛɾ/ (pt-PT), /ku.ˈʎɛɾ/ (pt-BR, casual)
  1. [A1] utensílio com cabo e uma parte côncava, usado para comer ou mexer
     - example: Podes trazer uma colher para eu provar a sopa?
     - collocations: colher de sopa; colher de chá; colher de sobremesa; colher de café; colher de pau
  2. [C1] ferramenta plana e triangular com cabo, usada para espalhar argamassa
     - example: O pedreiro alisava o cimento com a colher.
     - collocations: colher de pedreiro; colher de trolha
- relations: confusable colher (VERB): same spelling, but the noun has an open stressed é (/ɛ/) and the verb a close ê (/e/)
**Lexeme 2: colher**, VERB
- grammar: conjugation 2; valency Tr; irregular False; note stressed o alternates between /o/ in colho and /ɔ/ in colhe
- said: /ko.ˈʎeɾ/ (pt-BR), /ku.ˈʎeɾ/ (pt-PT)
- forms: colho [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈko.ʎu/ /ˈko.ʎu/; colhes [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /ˈkɔ.ʎis/ /ˈkɔ.ʎɨʃ/; colhe [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈkɔ.ʎi/ /ˈkɔ.ʎɨ/; colhemos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /ko.ˈʎe.mus/ /ku.ˈʎe.muʃ/; colhem [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈkɔ.ʎẽj/ /ˈkɔ.ʎɐ̃j/; colhi [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /ko.ˈʎi/ /ku.ˈʎi/; colheu [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /ko.ˈʎew/ /ku.ˈʎew/; colha [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈko.ʎɐ/ /ˈko.ʎɐ/; colhido [VerbForm=Part, Tense=Past] /ko.ˈʎi.du/ /ku.ˈʎi.du/
  1. [B1] tirar frutos, flores ou outros produtos da planta ou da terra
     - example: Todos os verões colhemos uvas na quinta dos meus avós.
     - frames: colher algo
     - collocations: colher frutos; colher flores; colher uvas; colher a safra
  2. [B2] juntar ou obter informações, provas ou opiniões de várias fontes
     - example: A polícia ainda está a colher depoimentos das testemunhas.
     - frames: colher algo
     - collocations: colher dados; colher informações; colher depoimentos; colher assinaturas; colher amostras
  3. [B2] obter como resultado do que se fez antes
     - example: Depois de anos de esforço, ela começa a colher os frutos.
     - frames: colher algo
     - collocations: colher os frutos; colher o que se plantou; colher resultados
     - labels: figurative True
  4. [B2] bater com força numa pessoa ou num animal, falando de um veículo
     - example: Um carro colheu o ciclista na estrada nacional.
     - frames: colher alguém
     - regions: pt-PT mainly
     - grammar: note often in the passive: ser colhido por um carro

### 3. avô

#### Entry A

**Lexeme 1: avô**, NOUN
- grammar: gender Masc; countability Count; note plural avôs means grandfathers, while os avós means grandparents
- said: /a.ˈvo/ (pt-BR), /ɐ.ˈvo/ (pt-PT)
  1. [A1] pai do pai ou da mãe de alguém
     - example: Costumo almoçar na casa do meu avô todos os domingos.
     - collocations: avô paterno; avô materno
- relations: confusable avó (NOUN): avó with an acute accent and open vowel means grandmother

#### Entry B

**Lexeme 1: avô**, NOUN
- grammar: gender Masc; countability Count; note plural avôs for grandfathers; avós for grandparents together
- said: /a.ˈvo/ (pt-BR), /ɐ.ˈvo/ (pt-PT)
- forms: avôs [Number=Plur] /a.ˈvos/ /ɐ.ˈvoʃ/ (grandfathers only); avós [Number=Plur] /a.ˈvɔs/ /ɐ.ˈvɔʃ/ (grandfather and grandmother together, or ancestors)
  1. [A1] pai do pai ou da mãe de uma pessoa
     - example: O meu avô conta histórias da guerra sempre que o visitamos.
     - collocations: avô materno; avô paterno
  2. [C1] pessoas de quem uma família descende e que viveram há muito tempo
     - example: Estas tradições vêm dos nossos avós de há muitos séculos.
     - labels: style literary
     - grammar: number Plur
- relations: confusable avó (NOUN): avó (grandmother) differs only by the accent: closed ô /o/ in avô, open ó /ɔ/ in avó

### 4. cidadão

#### Entry A

**Lexeme 1: cidadão**, NOUN
- grammar: gender Masc; countability Count
- said: /si.da.ˈdɐ̃w/ (pt-BR), /si.dɐ.ˈdɐ̃w/ (pt-PT)
- forms: cidadãos [Number=Plur] /si.da.ˈdɐ̃ws/ /si.dɐ.ˈdɐ̃wʃ/
  1. [B1] membro de um Estado com direitos e deveres civis e políticos
     - example: Todo cidadão tem o direito de votar nas eleições.
     - collocations: cidadão do mundo; cidadão comum; cidadão de bem; cidadão honorário; cidadão estrangeiro; direitos do cidadão; deveres do cidadão
  2. [B2] pessoa qualquer cujo nome não se sabe ou não se quer dizer
     - example: Apareceu aqui um cidadão perguntando pelo dono da oficina.
     - labels: formality informal
- relations: derived_from cidade (NOUN)
**Lexeme 2: cidadão**, ADJ
- said: /si.da.ˈdɐ̃w/ (pt-BR), /si.dɐ.ˈdɐ̃w/ (pt-PT)
- forms: cidadã [Gender=Fem, Number=Sing] /si.da.ˈdɐ̃/ /si.dɐ.ˈdɐ̃/; cidadãos [Gender=Masc, Number=Plur] /si.da.ˈdɐ̃ws/ /si.dɐ.ˈdɐ̃wʃ/; cidadãs [Gender=Fem, Number=Plur] /si.da.ˈdɐ̃s/ /si.dɐ.ˈdɐ̃ʃ/
  1. [B2] que respeita ou promove os direitos e deveres na vida em sociedade
     - example: Precisamos estimular uma consciência cidadã entre os mais jovens.
     - collocations: participação cidadã; consciência cidadã; empresa cidadã; jornalismo cidadão
- relations: derived_from cidade (NOUN)

#### Entry B

**Lexeme 1: cidadão**, NOUN
- grammar: gender Masc; countability Count; note feminine cidadã, plural cidadãos
- said: /si.da.ˈdɐ̃w/ (pt-BR), /si.dɐ.ˈdɐ̃w/ (pt-PT)
- forms: cidadãos [Number=Plur, Gender=Masc] /si.da.ˈdɐ̃ws/ /si.dɐ.ˈdɐ̃wʃ/; cidadã [Number=Sing, Gender=Fem] /si.da.ˈdɐ̃/ /si.dɐ.ˈdɐ̃/; cidadãs [Number=Plur, Gender=Fem] /si.da.ˈdɐ̃s/ /si.dɐ.ˈdɐ̃ʃ/
  1. [B1] pessoa que pertence a um país e nele tem direitos e deveres
     - example: Todo cidadão tem o direito de votar nas eleições.
     - collocations: cidadão comum; direitos do cidadão; cidadão de bem; cidadão do mundo; cidadão exemplar; cidadão estrangeiro
  2. [B2] pessoa que mora numa cidade
     - example: Os cidadãos da capital reclamam do trânsito todos os dias.
  3. [B2] homem qualquer, cujo nome não se sabe ou não se diz
     - example: Um cidadão parou o carro e me perguntou o caminho.
     - labels: formality informal
     - regions: pt-BR mainly
- relations: derived_from cidade (NOUN)
**Lexeme 2: cidadão**, ADJ
- grammar: comparable False; position Attr, mainly
- said: /si.da.ˈdɐ̃w/ (pt-BR), /si.dɐ.ˈdɐ̃w/ (pt-PT)
- forms: cidadã [Number=Sing, Gender=Fem] /si.da.ˈdɐ̃/ /si.dɐ.ˈdɐ̃/; cidadãos [Number=Plur, Gender=Masc] /si.da.ˈdɐ̃ws/ /si.dɐ.ˈdɐ̃wʃ/; cidadãs [Number=Plur, Gender=Fem] /si.da.ˈdɐ̃s/ /si.dɐ.ˈdɐ̃ʃ/
  1. [C1] que envolve a participação ativa das pessoas na vida da sociedade
     - example: O projeto promove a participação cidadã nas decisões do bairro.
     - collocations: participação cidadã; consciência cidadã; jornalismo cidadão
- relations: derived_from cidade (NOUN)

### 5. óculos

#### Entry A

**Lexeme 1: óculos**, NOUN
- grammar: gender Masc; number Plur; countability Count; note plural agreement even for one pair (os meus óculos); 'o óculos' is heard in Brazil but is nonstandard
- said: /ˈɔ.ku.lus/ (pt-BR), /ˈɔ.ku.luʃ/ (pt-PT)
  1. [A2] par de lentes presas numa armação, usado diante dos olhos para ver melhor ou protegê-los
     - example: Esqueci os meus óculos em casa e não consigo ler nada.
     - collocations: usar óculos; pôr os óculos; tirar os óculos; óculos de sol; óculos escuros; óculos de grau; óculos graduados; óculos de leitura; óculos de proteção; armação dos óculos

#### Entry B

**Lexeme 1: óculos**, NOUN
- grammar: gender Masc; number Plur; countability Count; note often used as singular in informal Brazilian speech
- said: /ˈɔ.ku.lus/ (pt-BR), /ˈɔ.ku.luʃ/ (pt-PT)
  1. [A1] par de lentes numa armação que ajuda a ver ou protege os olhos
     - example: Não consigo ler as letras pequenas do jornal sem os meus óculos.
     - collocations: usar óculos; pôr os óculos; tirar os óculos; par de óculos; óculos de sol; óculos escuros

### 6. haver

#### Entry A

**Lexeme 1: haver**, VERB
- grammar: conjugation 2; irregular True; note meaning 'exist', 'happen' or time ago, used only in the 3rd person singular: há, houve, havia
- said: /a.ˈveɾ/ (pt-BR), /ɐ.ˈveɾ/ (pt-PT), /a.ˈve/ (pt-BR, casual)
- forms: hei [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈej/ /ˈɐj/; hás [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /ˈas/ /ˈaʃ/; há [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈa/ /ˈa/; havemos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /a.ˈve.mus/ /ɐ.ˈve.muʃ/; hão [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈɐ̃w/ /ˈɐ̃w/; houve [Mood=Ind, Tense=Past, Person=1,3, Number=Sing, VerbForm=Fin] /ˈow.vi/ /ˈo.vɨ/; haja [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈa.ʒɐ/ /ˈa.ʒɐ/; havido [VerbForm=Part, Tense=Past] /a.ˈvi.du/ /ɐ.ˈvi.du/
  1. [A1] existir ou estar presente em algum lugar ou momento
     - example: Perto da minha casa há muitas lojas e dois mercados.
     - frames: haver algo; haver alguém
     - collocations: não há problema; não há dúvida; há de tudo; há quem diga; não há nada
     - grammar: valency Impers; note the verb stays singular: há livros, houve festas
  2. [A2] acontecer ou realizar-se
     - example: Ontem houve um acidente grave na estrada principal.
     - frames: haver algo
     - collocations: haver um acidente; haver uma reunião; haver um problema
     - grammar: valency Impers; note the verb stays singular: houve muitas greves
  3. [A2] usado para dizer quanto tempo passou desde um acontecimento
     - example: Moro nesta cidade há cinco anos e gosto muito.
     - frames: há + tempo
     - collocations: há muito tempo; há pouco; há anos; há dias
     - grammar: valency Impers
  4. [B2] usado com de para exprimir intenção firme, certeza ou futuro
     - example: Um dia hei de conhecer todos os países da Europa.
     - frames: haver de + inf
  5. [B2] usado com o particípio para formar os tempos compostos
     - example: Quando cheguei, ela já havia saído de casa.
     - frames: haver + particípio
     - labels: formality formal
  6. [C1] enfrentar alguém ou ter de lhe prestar contas
     - example: Se mexeres nas minhas coisas, vais ter de te haver comigo!
     - frames: haver-se com alguém
     - grammar: valency Refl; note the pronoun does not mean 'oneself'; it is part of the verb
**Lexeme 2: haver**, NOUN
- grammar: gender Masc; countability Count
- said: /a.ˈveɾ/ (pt-BR), /ɐ.ˈveɾ/ (pt-PT)
  1. [C1] bens e riquezas que uma pessoa possui
     - example: Antes de morrer, deixou todos os seus haveres aos sobrinhos.
     - labels: formality formal
     - grammar: number Plur
  2. [C2] parte de uma conta onde se registam os valores recebidos
     - example: O contabilista confere o deve e o haver todos os meses.
     - collocations: o deve e o haver
     - labels: domain finance
- relations: derived_from haver (VERB)

#### Entry B

**Lexeme 1: haver**, VERB
- grammar: conjugation 2; valency Impers; irregular True; note used only in the third person singular
- said: /a.ˈveɾ/ (pt-BR), /ɐ.ˈveɾ/ (pt-PT), /a.ˈve/ (pt-BR, casual)
- forms: hei [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈej/ /ˈɐj/; hás [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /ˈas/ /ˈaʃ/; há [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈa/ /ˈa/; havemos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /a.ˈve.mus/ /ɐ.ˈve.muʃ/; hão [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈɐ̃w/ /ˈɐ̃w/; houve [Mood=Ind, Tense=Past, Person=1,3, Number=Sing, VerbForm=Fin] /ˈo.vi/ /ˈo.vɨ/; haja [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈa.ʒɐ/ /ˈa.ʒɐ/; havido [VerbForm=Part, Tense=Past] /a.ˈvi.du/ /ɐ.ˈvi.du/
  1. [A1] existir ou estar presente em determinado lugar ou situação
     - example: Ainda há lugares vagos nas primeiras filas do teatro.
     - frames: haver algo/alguém
     - collocations: não haver dúvida; não haver problema; haver necessidade; haver possibilidade; haver motivo; haver tempo
  2. [A2] acontecer, ocorrer ou realizar-se em determinado momento ou lugar
     - example: Ontem à noite houve uma tempestade muito forte no litoral.
     - frames: haver algo
     - collocations: haver um acidente; haver uma reunião; haver eleições; haver mudança
  3. [A2] ter passado ou decorrido determinado período de tempo
     - example: Trabalho nesta mesma empresa há mais de dez anos.
- relations: confusable ter (VERB), for sense 1 only: in spoken Brazilian Portuguese, 'ter' is commonly used instead of 'haver' to mean 'there is'; confusable a (ADP), for sense 3 only: 'há' expresses elapsed time ('há dois dias', two days ago), while 'a' expresses future time or distance
**Lexeme 2: haver**, AUX
- grammar: conjugation 2; irregular True
- said: /a.ˈveɾ/ (pt-BR), /ɐ.ˈveɾ/ (pt-PT), /a.ˈve/ (pt-BR, casual)
- forms: hei [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈej/ /ˈɐj/; hás [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /ˈas/ /ˈaʃ/; há [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈa/ /ˈa/; havemos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /a.ˈve.mus/ /ɐ.ˈve.muʃ/; hão [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈɐ̃w/ /ˈɐ̃w/; houve [Mood=Ind, Tense=Past, Person=1,3, Number=Sing, VerbForm=Fin] /ˈo.vi/ /ˈo.vɨ/; haja [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈa.ʒɐ/ /ˈa.ʒɐ/; havido [VerbForm=Part, Tense=Past] /a.ˈvi.du/ /ɐ.ˈvi.du/
  1. [B1] usado para expressar uma ação concluída antes de outro momento
     - example: Quando eles chegaram ao porto, o navio já havia partido.
     - labels: formality formal
     - grammar: note used with a past participle to form compound tenses; 'ter' is more common
  2. [B2] usado para expressar intenção firme, certeza ou possibilidade no futuro
     - example: Não desista agora, porque tudo há de correr bem.
     - frames: haver de + inf
  3. [B2] usado para indicar que é necessário ou preciso fazer algo
     - example: Diante de tantas mudanças, há que manter a calma.
     - frames: haver que + inf
     - grammar: valency Impers; note used only in the third person singular
**Lexeme 3: haver**, NOUN
- grammar: gender Masc; countability Count
- said: /a.ˈveɾ/ (pt-BR), /ɐ.ˈveɾ/ (pt-PT)
  1. [C1] bens materiais, dinheiro ou propriedades que pertencem a alguém
     - example: Os moradores perderam quase todos os seus haveres na enchente.
     - labels: formality formal
     - grammar: number Plur
- relations: derived_from haver (VERB)

### 7. meio

#### Entry A

**Lexeme 1: meio**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈmej.u/ (pt-BR), /ˈmɐj.u/ (pt-PT)
  1. [A1] parte ou ponto situado entre as extremidades ou entre o início e o fim
     - example: Colocaram uma mesa redonda bem no meio da sala.
     - frames: meio de algo
     - collocations: no meio; ao meio; cortar ao meio; partir ao meio; no meio do caminho
  2. [B1] recurso, instrumento ou modo usado para realizar algo ou alcançar um fim
     - example: A bicicleta é o meu principal meio de transporte na cidade.
     - frames: meio de algo; meio de + inf; meio para + inf
     - collocations: meio de transporte; meio de comunicação; meio de pagamento; meios financeiros
  3. [B1] conjunto das condições naturais, sociais ou culturais em que se vive
     - example: Crescemos num meio artístico onde todos tocavam algum instrumento.
     - collocations: meio ambiente; meio social; meio familiar; meio rural; meio urbano
**Lexeme 2: meio**, ADJ
- grammar: comparable False
- said: /ˈmej.u/ (pt-BR), /ˈmɐj.u/ (pt-PT)
  1. [A1] que corresponde a uma das duas partes iguais de um todo
     - example: Esperei mais de meia hora na fila do banco.
     - collocations: meia hora; meia dúzia; meio quilo; meio litro
     - grammar: note stands before a noun without an article, or after 'e' following a whole number
     - opposites: inteiro
  2. [B2] que não é completo, intenso ou dito de forma direta
     - example: Ao ouvir a desculpa, deu apenas um meio sorriso e mudou de assunto.
     - collocations: meias palavras; meio sorriso; a meio gás
     - grammar: position Attr
**Lexeme 3: meio**, ADV
- grammar: invariable True; note does not change for gender or number in standard use
- said: /ˈmej.u/ (pt-BR), /ˈmɐj.u/ (pt-PT)
  1. [A2] um pouco, até certo ponto ou não por completo
     - example: Ainda me sinto meio cansada depois da viagem de ontem.
     - frames: meio + adj

#### Entry B

**Lexeme 1: meio**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈmej.u/ (pt-BR), /ˈmɐj.u/ (pt-PT)
  1. [A1] ponto ou parte que fica à mesma distância das extremidades
     - example: Deixaram a mesa grande bem no meio da sala.
     - frames: no meio de algo; a meio de algo
     - collocations: no meio; pelo meio; a meio; meio da noite; meio do caminho; meio da rua
     - grammar: note 'a meio de' is mostly used in Portugal
  2. [B1] aquilo que se usa para conseguir ou fazer alguma coisa
     - example: A bicicleta é o meio de transporte mais barato da cidade.
     - frames: por meio de algo; meio de + inf; meio para algo
     - collocations: meio de transporte; meios de comunicação; meio de pagamento; por todos os meios; por meio de
  3. [B1] ambiente físico ou social em que alguém vive ou trabalha
     - example: Ela cresceu no meio rural e só depois foi estudar fora.
     - collocations: meio ambiente; meio rural; meio urbano; meio artístico; meio académico; meio social
  4. [B2] recursos, sobretudo dinheiro, de que alguém dispõe
     - example: A família não tinha meios para pagar a universidade.
     - frames: meios para + inf; meios para algo
     - collocations: falta de meios; meios financeiros; ter meios
     - grammar: number Plur
**Lexeme 2: meio**, ADJ
- grammar: position Attr; comparable False; note feminine meia, plurals meios and meias
- said: /ˈmej.u/ (pt-BR), /ˈmɐj.u/ (pt-PT)
  1. [A1] que corresponde a uma de duas partes iguais de um todo
     - example: Compra meio quilo de tomates e uma dúzia de ovos.
     - collocations: meia hora; meio quilo; meio litro; meia dúzia; e meia
- relations: confusable metade (NOUN): meio stands before a noun (meio bolo), while 'half of' is metade de (metade do bolo)
**Lexeme 3: meio**, ADV
- grammar: invariable True; note stays meio before a feminine adjective in standard use (meio cansada)
- said: /ˈmej.u/ (pt-BR), /ˈmɐj.u/ (pt-PT)
  1. [A2] um pouco, em parte, não completamente
     - example: Hoje estou meio cansada, por isso vou deitar-me cedo.
     - frames: meio + adj
     - collocations: meio cansado; meio estranho; meio triste; meio aberto

### 8. rapariga

#### Entry A

**Lexeme 1: rapariga**, NOUN
- grammar: gender Fem; countability Count
- said: /ʁa.pa.ˈɾi.ɡɐ/ (pt-BR), /ʁɐ.pɐ.ˈɾi.ɡɐ/ (pt-PT)
  1. [A1] mulher jovem, adolescente ou criança do sexo feminino
     - example: Aquela rapariga de casaco azul trabalha na livraria da esquina.
     - regions: pt-PT mainly, pt-002 mainly
  2. [B2] mulher que cobra por sexo ou mantém relação com alguém já comprometido
     - example: Durante a discussão na rua, chamaram a vizinha de rapariga.
     - labels: formality informal, attitude derogatory, offensive
     - regions: pt-BR only
- relations: feminine_of rapaz (NOUN), for sense 1 only

#### Entry B

**Lexeme 1: rapariga**, NOUN
- grammar: gender Fem; countability Count
- said: /ʁa.pa.ˈɾi.ɡɐ/ (pt-BR), /ʁɐ.pɐ.ˈɾi.ɡɐ/ (pt-PT)
  1. [A2] pessoa jovem do sexo feminino, já não criança mas ainda não adulta
     - example: Aquela rapariga de cabelo curto é a minha prima.
     - collocations: rapariga nova
     - regions: pt-PT only, pt-002 only
  2. [C1] mulher que recebe dinheiro para ter relações sexuais
     - example: Chamar uma mulher de rapariga é um insulto grave aqui.
     - labels: attitude derogatory, vulgar
     - regions: pt-BR only
- relations: feminine_of rapaz (NOUN), for sense 1 only; confusable menina (NOUN): the ordinary word for a girl in Portugal is an insult in Brazil, where 'menina' or 'moça' is used instead

### 9. ônibus

#### Entry A

**Lexeme 1: ônibus**, NOUN
- grammar: gender Masc; countability Count; invariable True; note plural is the same: os ônibus
- said: /ˈo.ni.bus/ (pt-BR), /ˈɔ.ni.buʃ/ (pt-PT)
- spellings: ônibus (pt-BR); ónibus (pt-PT)
  1. [A1] veículo grande que leva muitos passageiros por ruas e estradas
     - example: Pego o ônibus das sete para chegar cedo ao trabalho.
     - collocations: pegar o ônibus; ponto de ônibus; andar de ônibus; ir de ônibus; perder o ônibus; linha de ônibus; motorista de ônibus; passagem de ônibus; ônibus lotado
     - regions: pt-BR mainly

#### Entry B

**Lexeme 1: ônibus**, NOUN
- grammar: gender Masc; countability Count; invariable True
- said: /ˈõ.ni.bus/ (pt-BR, ônibus), /ˈɔ.ni.buʃ/ (pt-PT, ónibus)
- spellings: ônibus (pt-BR); ónibus (pt-PT)
  1. [A1] veículo grande motorizado que transporta muitos passageiros por ruas e estradas
     - example: Costumo ir de ônibus para o trabalho todos os dias.
     - collocations: de ônibus; pegar o ônibus; perder o ônibus; ponto de ônibus; linha de ônibus; passagem de ônibus
     - regions: pt-BR only

### 10. fato

#### Entry A

**Lexeme 1: fato**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈfa.tu/ (pt-BR, fato), /ˈfak.tu/ (pt-PT, facto)
- spellings: fato (pt-BR); facto (pt-PT)
  1. [A2] aquilo que realmente aconteceu ou cuja verdade se pode comprovar
     - example: Ninguém pode negar os fatos apresentados durante a reunião.
     - frames: o fato de + inf; o fato de que + ind; o fato de que + subj
     - collocations: de fato; fato consumado; baseado em fatos reais; negar um fato; contra fatos não há argumentos
**Lexeme 2: fato**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈfa.tu/ (pt-BR), /ˈfa.tu/ (pt-PT)
  1. [A2] conjunto de casaco e calças ou saia do mesmo tecido
     - example: Vou comprar um fato escuro para o casamento da minha irmã.
     - collocations: fato completo; fato e gravata; vestir um fato
     - regions: pt-PT only, pt-002 only
  2. [A2] roupa ou conjunto de peças próprio para certa atividade ou ocasião
     - example: Não te esqueças de levar o fato de banho para a praia.
     - collocations: fato de banho; fato de treino; fato de trabalho
     - regions: pt-PT only, pt-002 only

#### Entry B

**Lexeme 1: fato**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈfa.tu/ (pt-BR, fato), /ˈfak.tu/ (pt-PT, facto)
- spellings: fato (pt-BR); facto (pt-PT)
  1. [A2] coisa que realmente aconteceu ou que se sabe ser verdadeira
     - example: Os fatos mostram que a situação melhorou bastante este ano.
     - frames: o fato de + inf; o fato de que + ind; o fato de que + subj
     - collocations: de fato; o fato é que; pelo fato de; fato consumado; fato histórico; apresentar os fatos; ater-se aos fatos; na verdade e de fato
**Lexeme 2: fato**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈfa.tu/ (pt-PT), /ˈfa.tu/ (pt-BR)
  1. [A2] conjunto de casaco e calças do mesmo tecido
     - example: Ele vestiu um fato escuro para o casamento da irmã.
     - collocations: fato e gravata; vestir um fato; fato completo
     - regions: pt-PT mainly
  2. [B1] roupa própria para fazer uma certa atividade
     - example: Não te esqueças de levar o fato de banho para a praia.
     - frames: fato de algo
     - collocations: fato de banho; fato de treino; fato de mergulho; fato de macaco
     - regions: pt-PT mainly

### 11. ficar

#### Entry A

**Lexeme 1: ficar**, VERB
- grammar: conjugation 1; valency Intr; irregular False; note c is written qu before e (fiquei, fique)
- said: /fi.ˈkaɾ/ (pt-BR), /fi.ˈkaɾ/ (pt-PT), /fi.ˈka/ (pt-BR, casual)
  1. [A1] passar a ter outro estado, condição ou qualidade
     - example: As crianças ficaram muito felizes com a notícia da viagem.
     - frames: ficar + adj; ficar sem algo
     - collocations: ficar pronto; ficar doente; ficar nervoso; ficar calado; ficar grávida; ficar em silêncio; ficar sem palavras
  2. [A1] continuar num lugar sem sair dele ou passar ali algum tempo
     - example: Chove tanto lá fora que prefiro ficar em casa hoje.
     - frames: ficar + lugar
     - collocations: ficar em casa; ficar de pé; ficar sentado; ficar para trás; ficar de fora
  3. [A1] estar localizado num determinado ponto ou a certa distância
     - example: Sabes se a farmácia mais próxima fica longe daqui?
     - frames: ficar + lugar
     - collocations: ficar perto; ficar longe
  4. [A2] passar a ter algo ou guardá-lo consigo sem devolver
     - example: Pode ficar com o troco, não precisa devolver nada.
     - frames: ficar com algo/alguém
     - collocations: ficar com o troco
  5. [A2] continuar a realizar uma ação durante um período de tempo
     - example: Fiquei olhando pela janela enquanto a chuva caía na rua.
     - frames: ficar + ger; ficar a + inf
     - grammar: note usually 'ficar + gerúndio' in Brazil and 'ficar a + infinitivo' in Portugal
  6. [A2] assentar de determinada maneira no corpo ou combinar com alguém
     - example: Esse casaco escuro fica muito bem com as tuas calças.
     - frames: ficar + adv
     - collocations: ficar bem; ficar mal; ficar largo; ficar apertado
  7. [B1] combinar ou comprometer-se a fazer alguma coisa mais tarde
     - example: Eles ficaram de avisar assim que chegassem à estação.
     - frames: ficar de + inf
  8. [B1] ter determinado preço final ou atingir certo valor total
     - example: Toda a reparação do carro ficou por trezentos euros.
     - frames: ficar em algo; ficar por algo
  9. [B1] beijar ou envolver-se amorosamente com outra pessoa sem compromisso sério
     - example: Eles ficaram numa festa no mês passado, mas não namoram.
     - frames: ficar com alguém
     - labels: formality informal
     - regions: pt-BR mainly

#### Entry B

**Lexeme 1: ficar**, VERB
- grammar: conjugation 1; valency Intr; irregular False; note c becomes qu before e (fiquei, fique)
- said: /fi.ˈkaɾ/ (pt-BR), /fi.ˈkaɾ/ (pt-PT), /fi.ˈka/ (pt-BR, casual)
- forms: fico [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈfi.ku/ /ˈfi.ku/; ficas [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /ˈfi.kɐs/ /ˈfi.kɐʃ/; fica [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈfi.kɐ/ /ˈfi.kɐ/; ficamos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /fi.ˈkɐ̃.mus/ /fi.ˈkɐ.muʃ/; ficam [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈfi.kɐ̃w/ /ˈfi.kɐ̃w/; fiquei [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /fi.ˈkej/ /fi.ˈkɐj/; ficou [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /fi.ˈkow/ /fi.ˈko/; fique [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈfi.ki/ /ˈfi.kɨ/; ficado [VerbForm=Part, Tense=Past] /fi.ˈka.du/ /fi.ˈka.du/
  1. [A1] continuar no mesmo lugar ou na mesma situação, sem sair nem mudar
     - example: Hoje vou ficar em casa porque está a chover muito.
     - frames: ficar em + lugar; ficar + adj
     - collocations: ficar em casa; ficar parado; ficar quieto; ficar acordado; ficar sozinho; ficar calado
  2. [A1] passar a estar num estado ou numa condição diferente
     - example: A minha mãe ficou muito contente com a notícia.
     - frames: ficar + adj
     - collocations: ficar doente; ficar com medo; ficar com fome; ficar nervoso; ficar grávida; ficar pronto; ficar sem dinheiro; ficar com raiva
  3. [A1] estar situado num determinado lugar
     - example: A farmácia fica ao lado do supermercado.
     - frames: ficar em + lugar
     - collocations: ficar perto; ficar longe
  4. [A2] guardar para si ou passar a ter alguma coisa
     - example: Pode ficar com o troco, não precisa devolver.
     - frames: ficar com algo
     - collocations: ficar com o troco
  5. [B1] continuar a fazer alguma coisa durante algum tempo
     - example: Ficamos conversando na varanda até de madrugada.
     - frames: ficar + gerúndio; ficar a + inf
     - grammar: note ficar a + inf is mostly used in Portugal, ficar + gerund in Brazil
  6. [B1] ter como preço ou custo total
     - example: O conserto do carro ficou em quinhentos reais.
     - frames: ficar em + quantia
     - collocations: ficar caro; ficar barato
  7. [B1] combinar ou prometer fazer alguma coisa
     - example: Ele ficou de me ligar amanhã à tarde.
     - frames: ficar de + inf
  8. [B1] ter boa ou má aparência em quem usa ou veste
     - example: Esse casaco fica muito bem em você.
     - frames: ficar + adv + em alguém; ficar + adv + a alguém
     - collocations: ficar bem; ficar mal
     - grammar: note em alguém is usual in Brazil, a alguém in Portugal
  9. [B1] ser adiado ou marcado para outro momento
     - example: A reunião ficou para a próxima semana.
     - frames: ficar para + tempo
  10. [B2] trocar beijos e carícias com alguém sem compromisso de namoro
     - example: Os dois ficaram na festa, mas não estão namorando.
     - frames: ficar com alguém
     - labels: formality informal
     - regions: pt-BR only
