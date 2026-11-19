# Portuguese: entries to check (1 of 1)

These are entries of a learner's dictionary of Portuguese, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of Portuguese.

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

Do not report: the order of senses, a CEFR level off by one step, how syllables are divided, a
definition you would have worded differently when the given one is true, or anything about style.

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

### 1. computador

**Lexeme 1: computador**, NOUN
- grammar: gender Masc; countability Count
- said: /kõ.pu.ta.ˈdoɾ/ (pt-BR), /kõ.pu.tɐ.ˈdoɾ/ (pt-PT)
  1. [A1] máquina que guarda e processa dados e executa programas
     - example: Preciso de um computador novo para trabalhar em casa.
- relations: derived_from computar (VERB)

### 2. raio

**Lexeme 1: raio**, NOUN
- grammar: gender Masc; countability Count
- said: /ʁaj.ˈu/ (pt-BR), /ʁaj.ˈu/ (pt-PT)
  1. [B1] descarga elétrica forte entre as nuvens e a terra numa tempestade
     - example: Durante a tempestade, um raio caiu em cima da árvore do quintal.
     - collocations: cair um raio; atingido por um raio; rápido como um raio; raios e trovões
  2. [B1] linha de luz ou de energia que parte de uma fonte
     - example: Os primeiros raios de sol entravam pela janela do quarto.
     - collocations: raio de sol; raio de luz; raio laser; raio X; raios ultravioleta; raio de esperança
  3. [B2] segmento que vai do centro de um círculo até qualquer ponto da sua linha
     - example: Para calcular a área do círculo, precisamos de saber o raio.
     - labels: domain mathematics
  4. [B2] distância a partir de um ponto, que marca a área à sua volta
     - example: Não há nenhuma farmácia num raio de vinte quilómetros.
     - frames: num raio de + distância
     - collocations: raio de ação; raio de alcance
  5. [C1] cada uma das barras que ligam o centro de uma roda à sua parte de fora
     - example: A roda da bicicleta tem dois raios partidos depois da queda.
  6. [C1] usado antes de um nome para mostrar irritação, impaciência ou surpresa
     - example: Que raio de barulho é este a meio da noite?
     - frames: o raio de algo/alguém; que raio de algo
     - collocations: que raio; raios te partam; raios partam
     - labels: formality informal
     - regions: pt-PT mainly

### 3. agricultura

**Lexeme 1: agricultura**, NOUN
- grammar: gender Fem; countability Mass
- said: /a.ɡɾi.kuw.ˈtu.ɾɐ/ (pt-BR), /ɐ.ɡɾi.kul.ˈtu.ɾɐ/ (pt-PT)
  1. [B1] trabalho de preparar a terra e plantar para obter alimentos e outros produtos
     - example: Nesta região, a maioria das famílias ainda vive da agricultura.
     - collocations: viver da agricultura; dedicar-se à agricultura; agricultura familiar; agricultura de subsistência; agricultura intensiva; agricultura biológica; agricultura orgânica

### 4. calor

**Lexeme 1: calor**, NOUN
- grammar: gender Masc; countability Mass
- said: /ka.ˈloɾ/ (pt-BR), /kɐ.ˈloɾ/ (pt-PT)
  1. [A1] temperatura alta do ar, de um lugar ou de uma coisa
     - example: Estou com calor, podes abrir a janela?
     - collocations: fazer calor; onda de calor; calor intenso; calor insuportável; calor do sol; calor do fogo
     - opposites: frio
  2. [A1] sensação de quente que o corpo sente
     - example: No verão faz muito calor nesta cidade do interior.
     - collocations: estar com calor; sentir calor; passar calor; ter calor
     - opposites: frio
  3. [B2] força e entusiasmo de um momento, de uma discussão ou de um sentimento
     - example: No calor da discussão, disse coisas de que se arrependeu.
     - collocations: no calor do momento; no calor da discussão; no calor da batalha
     - labels: figurative True
  4. [B2] afeto e carinho com que se recebe ou trata alguém
     - example: Fomos recebidos com o calor de uma família amiga.
     - collocations: calor humano
     - labels: figurative True
     - opposites: frieza

### 5. competição

**Lexeme 1: competição**, NOUN
- grammar: gender Fem; countability Both
- said: /kõ.pe.tʃi.ˈsɐ̃w/ (pt-BR), /kõ.pɨ.ti.ˈsɐ̃w/ (pt-PT)
  1. [B1] evento em que vários participantes tentam vencer os outros
     - example: A nossa escola vai participar numa competição de matemática este ano.
     - frames: competição de algo
     - collocations: ganhar uma competição; vencer a competição; participar de uma competição; participar numa competição; competição internacional; competição esportiva; competição desportiva
     - grammar: countability Count
  2. [B1] situação em que várias pessoas ou grupos procuram obter a mesma coisa
     - example: Há cada vez mais competição entre as empresas deste setor.
     - frames: competição entre alguém e alguém; competição por algo; competição com alguém
     - collocations: forte competição; competição acirrada; competição saudável; competição desleal; em competição
     - grammar: countability Mass
     - opposites: cooperação
- relations: verbal_noun_of competir (VERB)

### 6. canto

**Lexeme 1: canto**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈkɐ̃.tu/ (pt-BR), /ˈkɐ̃.tu/ (pt-PT)
  1. [A2] lugar onde se encontram duas paredes, dois lados ou duas linhas
     - example: Coloca o sofá no canto da sala, perto da janela.
     - frames: canto de algo
     - collocations: no canto; canto da boca; canto do olho; olhar pelo canto do olho; canto superior; canto inferior
     - grammar: countability Count
  2. [B1] lugar pequeno, afastado ou escondido
     - example: Encontrámos um canto sossegado para ler em paz.
     - collocations: canto do mundo; canto sossegado; em todo canto; pelos cantos
     - grammar: countability Count
  3. [A2] arte e prática de fazer música com a voz
     - example: Ela tem aulas de canto duas vezes por semana.
     - collocations: aulas de canto; professor de canto; canto lírico; canto coral; canto gregoriano
     - grammar: countability Mass
  4. [B1] som musical que certas aves e outros animais emitem
     - example: Acordamos todos os dias com o canto dos pássaros.
     - collocations: canto dos pássaros; canto do galo
     - grammar: countability Count
  5. [C1] cada uma das partes em que se divide um poema longo
     - example: O poeta narra a tempestade no sexto canto da obra.
     - labels: domain literature
     - grammar: countability Count
  6. [B2] reposição da bola feita da esquina do campo, depois de sair pela linha de fundo
     - example: O árbitro assinalou canto a favor da equipa da casa.
     - collocations: pontapé de canto; marcar um canto; bater o canto; assinalar canto
     - regions: pt-PT mainly
     - grammar: countability Count
- relations: derived_from cantar (VERB); confusable esquina (NOUN): canto is mostly the inside corner of a room or space, esquina the outside corner where two streets or walls meet

### 7. perigo

**Lexeme 1: perigo**, NOUN
- grammar: gender Masc; countability Both
- said: /pe.ˈɾi.ɡu/ (pt-BR), /pɨ.ˈɾi.ɡu/ (pt-PT)
  1. [A2] situação em que alguém ou algo pode sofrer um dano ou um mal
     - example: Os bombeiros garantem que as crianças já estão fora de perigo.
     - frames: perigo de + inf; perigo de algo; perigo para alguém/algo
     - collocations: correr perigo; estar em perigo; pôr em perigo; fora de perigo; perigo de vida; perigo de morte; perigo iminente; sinal de perigo; enfrentar o perigo
     - opposites: segurança
  2. [B1] pessoa ou coisa que pode causar dano ou ameaçar alguém
     - example: Aquele cruzamento sem semáforo é um perigo para quem atravessa.
     - frames: perigo para alguém/algo
     - collocations: ser um perigo; perigo público
     - grammar: countability Count

### 8. recentemente

**Lexeme 1: recentemente**, ADV
- said: /ʁe.sẽ.tʃi.ˈmẽ.tʃi/ (pt-BR), /ʁɨ.sẽ.tɨ.ˈmẽ.tɨ/ (pt-PT)
  1. [B1] há pouco tempo, num momento não muito distante do atual
     - example: Mudei-me recentemente para um apartamento perto do centro.
     - collocations: até recentemente; só recentemente; muito recentemente; mais recentemente
- relations: adverb_of recente (ADJ)

### 9. concurso

**Lexeme 1: concurso**, NOUN
- grammar: gender Masc; countability Count
- said: /kõ.ˈkuɾ.su/ (pt-BR), /kõ.ˈkuɾ.su/ (pt-PT)
  1. [A2] competição em que várias pessoas disputam um prémio ou uma distinção
     - example: Ela vai participar num concurso de fotografia este verão.
     - frames: concurso de algo
     - collocations: concurso de beleza; ganhar um concurso; vencer um concurso; participar num concurso; participar de um concurso; concurso de talentos; júri do concurso
  2. [B1] seleção de candidatos por provas ou avaliação para um cargo ou contrato
     - example: Estudo há meses para passar no concurso da universidade.
     - frames: concurso para algo
     - collocations: concurso público; prestar concurso; passar no concurso; abrir concurso; concorrer a um concurso; fazer concurso; edital do concurso
  3. [C1] ajuda ou participação de alguém para que algo se realize
     - example: O projeto só foi possível com o concurso de muitos voluntários.
     - frames: com o concurso de alguém/algo
     - labels: formality formal

### 10. vender

**Lexeme 1: vender**, VERB
- grammar: conjugation 2; irregular False
- said: /vẽ.ˈdeɾ/ (pt-BR), /vẽ.ˈdeɾ/ (pt-PT), /vẽ.ˈde/ (pt-BR, casual)
  1. [A1] dar algo a alguém em troca de dinheiro
     - example: Vendemos o carro antigo a um vizinho por pouco dinheiro.
     - frames: vender algo a alguém; vender algo por + quantia
     - collocations: vender bem; vender caro; vender barato; vender a prazo; vender à vista; vender fiado
     - grammar: valency Tr, mainly; note also used without an object: este modelo vende bem
     - opposites: comprar
  2. [B2] apresentar algo de modo a convencer outros do seu valor
     - example: Ela sabe vender as suas ideias em qualquer reunião.
     - frames: vender algo a alguém
     - collocations: vender uma ideia; vender a imagem; vender um projeto
     - labels: figurative True
     - grammar: valency Tr
  3. [C1] aceitar dinheiro ou vantagens para agir contra os próprios princípios
     - example: Nunca vou me vender por um cargo no governo.
     - frames: vender-se por algo; vender-se a alguém
     - labels: figurative True, attitude disapproving
     - grammar: valency Refl; reflexive True

### 11. estrela

**Lexeme 1: estrela**, NOUN
- grammar: gender Fem; countability Count
- said: /is.ˈtɾe.lɐ/ (pt-BR), /ɨʃ.ˈtɾe.lɐ/ (pt-PT)
  1. [A1] astro que brilha no céu à noite, sobretudo o que tem luz própria
     - example: Longe da cidade, conseguimos ver milhares de estrelas no céu.
     - collocations: estrela cadente; estrela polar; as estrelas brilham; céu cheio de estrelas; olhar as estrelas
  2. [A2] figura ou objeto com várias pontas à volta de um centro
     - example: As crianças recortaram estrelas de papel para enfeitar a sala.
     - collocations: hotel de cinco estrelas; estrela de cinco pontas; dar estrelas
  3. [B1] pessoa muito famosa ou que mais se destaca num grupo ou atividade
     - example: Aquele jovem atacante tornou-se a estrela da equipa este ano.
     - frames: estrela de algo
     - collocations: estrela de cinema; estrela do rock; grande estrela; virar estrela
     - grammar: note always feminine, also for a man: ele é a estrela do filme

### 12. precisar

**Lexeme 1: precisar**, VERB
- grammar: conjugation 1; irregular False; valency Both
- said: /pɾe.si.ˈzaɾ/ (pt-BR), /pɾɨ.si.ˈzaɾ/ (pt-PT), /pɾe.si.ˈza/ (pt-BR, casual)
  1. [A1] ter necessidade de alguma coisa, de alguém ou de fazer algo
     - example: Preciso de ajuda para levar estas caixas até ao carro.
     - frames: precisar de algo/alguém; precisar + inf; precisar de + inf; precisar que + subj; precisa-se de algo/alguém
     - collocations: precisar de ajuda; não precisa; precisar urgentemente; precisar de tempo; precisar de dinheiro
     - grammar: valency Intr; note before an infinitive, de is usual in Portugal and usually left out in Brazil
  2. [B2] não ter o necessário para viver; passar dificuldades
     - example: Vamos doar estas roupas a famílias que precisam.
     - grammar: valency Intr
  3. [C1] indicar ou determinar com exatidão
     - example: As testemunhas não souberam precisar a hora do acidente.
     - frames: precisar algo
     - collocations: precisar a data
     - labels: formality formal
     - grammar: valency Tr

### 13. crescer

**Lexeme 1: crescer**, VERB
- grammar: conjugation 2; valency Intr; irregular False; note c is written ç before a and o (cresço, cresça); stressed e is /e/ in cresço but /ɛ/ in cresce
- said: /kɾe.ˈseɾ/ (pt-BR), /kɾɨʃ.ˈseɾ/ (pt-PT), /kɾe.ˈse/ (pt-BR, casual)
- forms: cresço [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈkɾe.su/ /ˈkɾeʃ.su/; cresces [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /ˈkɾɛ.sis/ /ˈkɾɛʃ.sɨʃ/; cresce [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈkɾɛ.si/ /ˈkɾɛʃ.sɨ/; crescemos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /kɾe.ˈse.mus/ /kɾɨʃ.ˈse.muʃ/; crescem [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈkɾɛ.sẽj/ /ˈkɾɛʃ.sɐ̃j/; cresci [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /kɾe.ˈsi/ /kɾɨʃ.ˈsi/; cresceu [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /kɾe.ˈsew/ /kɾɨʃ.ˈsew/; cresça [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈkɾe.sɐ/ /ˈkɾeʃ.sɐ/; crescido [VerbForm=Part, Tense=Past] /kɾe.ˈsi.du/ /kɾɨʃ.ˈsi.du/
  1. [A2] ficar maior com o tempo, por desenvolvimento natural
     - example: As plantas do quintal crescem depressa quando chove muito.
     - collocations: crescer depressa; crescer muito; deixar crescer o cabelo; deixar crescer a barba
  2. [B1] aumentar em número, quantidade, intensidade ou importância
     - example: Este ano a economia do país cresceu mais do que se esperava.
     - frames: crescer em algo
     - collocations: crescer rapidamente; crescer em número; a economia cresce; crescer a um ritmo
     - opposites: diminuir
  3. [A2] deixar de ser criança e tornar-se adulto
     - example: Cresci numa aldeia perto do mar e ainda sinto saudades.
     - frames: crescer em + lugar
     - collocations: quando eu crescer
  4. [B2] ganhar maturidade, experiência ou valor como pessoa ou profissional
     - example: Com aquela experiência difícil, ela cresceu muito como pessoa.
     - frames: crescer como algo
     - labels: figurative True

### 14. escolher

**Lexeme 1: escolher**, VERB
- grammar: conjugation 2; valency Both; irregular False; note stressed o alternates between /o/ in escolho and /ɔ/ in escolhe
- said: /is.ko.ˈʎeɾ/ (pt-BR), /iʃ.ku.ˈʎeɾ/ (pt-PT), /is.ko.ˈʎe/ (pt-BR, casual), /ʃku.ˈʎeɾ/ (pt-PT)
- forms: escolho [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /is.ˈko.ʎu/ /iʃ.ˈko.ʎu/; escolhes [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /is.ˈkɔ.ʎis/ /iʃ.ˈkɔ.ʎɨʃ/; escolhe [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /is.ˈkɔ.ʎi/ /iʃ.ˈkɔ.ʎɨ/; escolhemos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /is.ko.ˈʎe.mus/ /iʃ.ku.ˈʎe.muʃ/; escolhem [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /is.ˈkɔ.ʎẽj/ /iʃ.ˈkɔ.ʎɐ̃j/; escolhi [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /is.ko.ˈʎi/ /iʃ.ku.ˈʎi/; escolheu [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /is.ko.ˈʎew/ /iʃ.ku.ˈʎew/; escolha [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /is.ˈko.ʎɐ/ /iʃ.ˈko.ʎɐ/; escolhido [VerbForm=Part, Tense=Past] /is.ko.ˈʎi.du/ /iʃ.ku.ˈʎi.du/
  1. [A1] decidir qual das possibilidades se quer ou o que fazer
     - example: Ainda não consegui escolher entre os dois vestidos.
     - frames: escolher algo/alguém; escolher entre algo e algo; escolher + inf
  2. [B2] separar o que se pode aproveitar do que se deve retirar
     - example: Ajuda a escolher o feijão e tira as pedrinhas.
     - frames: escolher algo

### 15. ciência

**Lexeme 1: ciência**, NOUN
- grammar: gender Fem; countability Both
- said: /si.ˈẽ.sjɐ/ (pt-BR), /ˈsjẽ.sjɐ/ (pt-PT)
- spellings: ciência (pt-BR, pt-PT); sciencia (pt-BR, pt-PT) [older books only]; sciência (pt-PT) [older books only]
  1. [B1] conhecimento construído por meio de observação, estudo e investigação
     - example: A ciência ainda não explica todos os mistérios do universo.
     - grammar: countability Mass
  2. [B1] área de estudo que investiga certas questões com métodos próprios
     - example: A astronomia é a ciência que estuda os astros.
     - grammar: countability Count
  3. [C1] conhecimento ou informação que alguém tem sobre um assunto
     - example: Você tem ciência dos riscos deste tratamento?
     - frames: ciência de algo
     - collocations: ter ciência; tomar ciência; dar ciência
     - labels: formality formal
     - grammar: countability Mass

### 16. homenagem

**Lexeme 1: homenagem**, NOUN
- grammar: gender Fem; countability Count
- said: /o.me.ˈna.ʒẽj/ (pt-BR), /ɔ.mɨ.ˈna.ʒɐ̃j/ (pt-PT)
  1. [B1] gesto, obra ou palavras que mostram respeito e admiração por alguém
     - example: A banda tocou essa canção como homenagem ao antigo baterista.
     - frames: homenagem a alguém/algo; em homenagem a alguém/algo
     - collocations: prestar homenagem; render homenagem; fazer uma homenagem; receber uma homenagem; justa homenagem; homenagem póstuma; merecida homenagem
  2. [B2] cerimónia ou evento organizado para honrar uma pessoa ou um acontecimento
     - example: Vamos organizar uma homenagem aos professores que se reformam este ano.
     - frames: homenagem a alguém/algo
     - collocations: organizar uma homenagem; sessão de homenagem; jantar de homenagem

### 17. fronteira

**Lexeme 1: fronteira**, NOUN
- grammar: gender Fem; countability Count
- said: /fɾõ.tej.ˈɾɐ/ (pt-BR), /fɾõ.tɐj.ˈɾɐ/ (pt-PT)
  1. [B1] linha que separa dois países, estados ou regiões, e a zona perto dela
     - example: Vamos atravessar a fronteira de carro amanhã de manhã.
     - frames: fronteira com algo; fronteira entre algo e algo
     - collocations: atravessar a fronteira; cruzar a fronteira; fechar a fronteira; abrir a fronteira; passar a fronteira; controle de fronteira; cidade de fronteira; na fronteira
  2. [B2] limite que separa duas coisas diferentes ou até onde algo pode chegar
     - example: Às vezes a fronteira entre coragem e loucura é muito fina.
     - frames: fronteira entre algo e algo; fronteiras de algo
     - collocations: sem fronteiras; ultrapassar as fronteiras; fronteiras do conhecimento
     - labels: figurative True

### 18. sentimento

**Lexeme 1: sentimento**, NOUN
- grammar: gender Masc; countability Both
- said: /sẽ.tʃi.ˈmẽ.tu/ (pt-BR), /sẽ.ti.ˈmẽ.tu/ (pt-PT)
  1. [B1] estado afetivo que alguém vive perante pessoas, coisas ou situações
     - example: Nunca escondeu os seus sentimentos pela antiga colega de escola.
     - frames: sentimento de algo; sentimento por alguém
     - collocations: sentimento de culpa; ferir os sentimentos; esconder os sentimentos; expressar os sentimentos; sentimentos fortes; sentimento de impotência; sentimento de alívio
     - grammar: countability Count
  2. [B2] emoção e sensibilidade que se põem no que se faz ou diz
     - example: Ela tocou a canção ao piano com muito sentimento.
     - collocations: com sentimento; com muito sentimento
     - grammar: countability Mass
  3. [B2] pesar que se manifesta a quem perdeu uma pessoa querida
     - example: Aceite os meus sentimentos pela perda do seu irmão.
     - collocations: meus sentimentos
     - grammar: number Plur; note used in the plural in expressions of condolence
- relations: derived_from sentir (VERB)

### 19. dono

**Lexeme 1: dono**, NOUN
- grammar: gender Masc; countability Count; note feminine: dona
- said: /ˈdo.nu/ (pt-BR), /ˈdo.nu/ (pt-PT)
  1. [A2] pessoa a quem pertence uma coisa, um negócio ou um animal
     - example: O dono do restaurante veio cumprimentar todos os clientes.
     - frames: dono de algo/alguém
     - collocations: dono da casa; dono do cachorro; dono do cão; legítimo dono; novo dono; dono do negócio; dono da empresa
  2. [B2] pessoa que tem pleno domínio sobre uma situação ou sobre si
     - example: Mesmo sob muita pressão, o treinador continuou dono da situação.
     - frames: dono de algo
     - collocations: dono da situação; dono de si; dono do seu nariz; dono do próprio destino
     - labels: figurative True

### 20. leão

**Lexeme 1: leão**, NOUN
- grammar: gender Fem; countability Count; note feminine leoa
- said: /le.ˈɐ̃w/ (pt-BR), /li.ˈɐ̃w/ (pt-PT)
- forms: leões [Number=Plur] /le.ˈõjs/ /li.ˈõjʃ/
  1. [A2] grande felino selvagem de pelo amarelado, cujo macho tem juba
     - example: No jardim zoológico, o leão dormia à sombra de uma árvore.
     - collocations: rugido do leão; juba do leão; jaula do leão; domador de leões
  2. [B2] pessoa muito corajosa, forte ou combativa
     - example: Quando se trata de defender os filhos, ela é um leão.
     - collocations: lutar como um leão
     - labels: figurative True
**Lexeme 2: Leão**, PROPN
- grammar: gender Masc
- said: /le.ˈɐ̃w/ (pt-BR), /li.ˈɐ̃w/ (pt-PT)
  1. [B1] quinto signo do zodíaco, de quem nasce entre julho e agosto
     - example: A minha irmã nasceu em agosto, por isso é de Leão.
     - collocations: signo de Leão
  2. [C1] órgão do governo que cobra o imposto de renda
     - example: Todos os anos, em abril, tenho de prestar contas ao Leão.
     - collocations: mordida do Leão
     - labels: formality informal
     - regions: pt-BR only

### 21. limpar

**Lexeme 1: limpar**, VERB
- grammar: conjugation 1; irregular False; note two past participles: limpado and limpo
- said: /lĩ.ˈpaɾ/ (pt-BR), /lĩ.ˈpaɾ/ (pt-PT), /lĩ.ˈpa/ (pt-BR, casual)
- forms: limpado [VerbForm=Part, Tense=Past] /lĩ.ˈpa.du/ /lĩ.ˈpa.du/ (mainly with ter and haver); limpo [VerbForm=Part, Tense=Past] /ˈlĩ.pu/ /ˈlĩ.pu/ (mainly with ser and estar; with ter too, especially in Brazil)
  1. [A1] tirar o pó, as manchas ou o que suja algo ou alguém
     - example: Limpa bem os pés no tapete antes de entrar.
     - frames: limpar algo/alguém; limpar algo com algo
     - collocations: limpar o pó; limpar a casa; limpar o chão; limpar os dentes; limpar o nariz; limpar as mãos; limpar a mesa; limpar bem
     - grammar: valency Tr, mainly
     - opposites: sujar
  2. [B2] tirar de um alimento as partes que não se comem
     - example: Antes de assar o peixe, é preciso limpá-lo bem.
     - frames: limpar algo
     - collocations: limpar o peixe; limpar o frango
     - grammar: valency Tr, mainly
  3. [B2] levar ou gastar tudo o que há num lugar, deixando-o vazio
     - example: Os ladrões limparam a loja durante a madrugada.
     - frames: limpar algo
     - collocations: limpar o prato
     - labels: formality informal
     - grammar: valency Tr, mainly
  4. [B2] livrar de culpa, suspeita ou má fama
     - example: Ele passou anos nos tribunais para limpar o nome da família.
     - frames: limpar algo
     - collocations: limpar o nome; limpar a imagem; limpar a reputação
     - labels: figurative True
     - grammar: valency Tr, mainly
  5. [B2] ficar sem nuvens ou sem chuva, falando do tempo ou do céu
     - example: Se o tempo limpar, amanhã vamos todos à praia.
     - grammar: valency Intr
- relations: derived_from limpo (ADJ)

### 22. bagagem

**Lexeme 1: bagagem**, NOUN
- grammar: gender Fem; countability Mass, mainly; note the plural bagagens is often used for several travellers' luggage
- said: /ba.ˈɡa.ʒẽj/ (pt-BR), /bɐ.ˈɡa.ʒɐ̃j/ (pt-PT)
  1. [A2] conjunto das malas e objetos que alguém leva consigo numa viagem
     - example: Deixamos a bagagem no hotel e fomos passear pela cidade.
     - collocations: bagagem de mão; excesso de bagagem; despachar a bagagem; bagagem extraviada; extravio de bagagem; fazer a bagagem; recolher a bagagem
  2. [C1] conjunto de conhecimentos e experiências que uma pessoa acumulou
     - example: Com a sua bagagem profissional, vai adaptar-se depressa ao novo cargo.
     - collocations: bagagem cultural; bagagem intelectual; bagagem profissional
     - labels: figurative True

### 23. tratar

**Lexeme 1: tratar**, VERB
- grammar: conjugation 1; valency Both; irregular False
- said: /tɾa.ˈtaɾ/ (pt-BR), /tɾɐ.ˈtaɾ/ (pt-PT)
  1. [A2] comportar-se de certa maneira com uma pessoa ou um animal
     - example: Ela trata sempre os clientes com muita simpatia.
     - frames: tratar alguém como algo/alguém
     - collocations: tratar bem; tratar mal; tratar com respeito; tratar com carinho; tratar como uma criança
     - grammar: valency Tr
  2. [B1] cuidar de um doente ou de uma doença para conseguir a cura
     - example: O médico tratou a infecção com antibióticos durante uma semana.
     - frames: tratar algo/alguém; tratar algo com algo
     - collocations: tratar uma doença; tratar um paciente; tratar uma ferida; tratar o câncer
     - grammar: valency Tr; note tratar-se: to have medical treatment
  3. [B1] ocupar-se de alguém ou de um assunto, fazendo o que é preciso
     - example: Não te preocupes, eu trato disso amanhã de manhã.
     - frames: tratar de algo/alguém; tratar algo com alguém
     - collocations: tratar da papelada; tratar de negócios; tratar dos filhos
     - grammar: valency Intr
  4. [B1] ter um assunto como tema ou falar sobre ele
     - example: O filme trata da vida de uma família de pescadores.
     - frames: tratar de algo; tratar sobre algo
     - grammar: valency Intr
  5. [B2] ser aquilo de que se fala ou que está em questão
     - example: Não se trata de dinheiro, trata-se de respeito.
     - frames: tratar-se de algo/alguém; tratar-se de + inf
     - grammar: valency Impers; reflexive True; note only in the third person singular with se, without a subject
  6. [B2] dirigir-se a alguém usando certa forma ou certo título
     - example: Podes tratar-me por tu, não sou assim tão velho.
     - frames: tratar alguém por algo; tratar alguém de algo
     - collocations: tratar por tu; tratar por você; tratar de senhor
     - grammar: valency Tr
  7. [B2] esforçar-se para fazer algo ou para que algo aconteça
     - example: Trate de chegar cedo, senão perde o voo.
     - frames: tratar de + inf
     - grammar: valency Intr
  8. [B2] submeter algo a um processo para o limpar, conservar ou modificar
     - example: A cidade precisa de tratar melhor as águas dos esgotos.
     - frames: tratar algo
     - collocations: tratar a água; tratar o lixo; tratar os dados; tratar a madeira
     - grammar: valency Tr

### 24. prol

**Lexeme 1: prol**, NOUN
- grammar: gender Masc; countability Mass; number Sing; note almost only used in the phrase em prol de
- said: /ˈpɾɔw/ (pt-BR), /ˈpɾɔl/ (pt-PT)
  1. [B2] benefício ou vantagem a favor de uma pessoa, um grupo ou uma causa
     - example: A comunidade organizou uma campanha em prol das famílias desabrigadas.
     - frames: em prol de algo/alguém

### 25. rural

**Lexeme 1: rural**, ADJ
- grammar: note same form for masculine and feminine; plural rurais
- said: /ʁu.ˈɾaw/ (pt-BR), /ʁu.ˈɾal/ (pt-PT)
  1. [B1] do campo e da vida fora das cidades, incluindo a agricultura
     - example: Muitos jovens deixam a zona rural à procura de emprego.
     - collocations: zona rural; meio rural; área rural; êxodo rural; população rural; turismo rural; produtor rural; trabalhador rural; propriedade rural
     - opposites: urbano
- relations: adjective_of campo (NOUN)

### 26. grau

**Lexeme 1: grau**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈɡɾaw/ (pt-BR), /ˈɡɾaw/ (pt-PT)
  1. [B1] nível em que algo existe ou acontece, numa escala de intensidade
     - example: Este exercício tem um grau de dificuldade muito elevado.
     - frames: grau de algo
     - collocations: em certo grau; em maior grau; grau de dificuldade; grau elevado; grau de confiança; queimadura de terceiro grau
  2. [A2] unidade com que se medem a temperatura e os ângulos
     - example: Amanhã a temperatura vai chegar aos trinta graus.
     - collocations: graus Celsius; graus negativos; ângulo de noventa graus; volta de 180 graus
  3. [B2] título que uma universidade dá a quem conclui certos estudos
     - example: Ela obteve o grau de doutora em química no ano passado.
     - frames: grau de + título
     - collocations: obter o grau; conferir o grau; colação de grau; grau de mestre; grau de doutor
  4. [B2] distância entre parentes, contada pelas gerações que os separam
     - example: Somos primos em segundo grau, mas quase não nos conhecemos.
     - collocations: grau de parentesco; primo em primeiro grau; primo de segundo grau
  5. [B1] medida da correção que uma lente dá à visão
     - example: Meu grau aumentou e preciso trocar as lentes dos óculos.
     - collocations: óculos de grau; lentes de grau; aumentar o grau
     - regions: pt-BR mainly

### 27. pobre

**Lexeme 1: pobre**, ADJ
- grammar: note same form for masculine and feminine
- said: /ˈpɔ.bɾi/ (pt-BR), /ˈpɔ.bɾɨ/ (pt-PT)
- forms: paupérrimo [Degree=Abs, Gender=Masc, Number=Sing] /paw.ˈpɛ.ʁi.mu/ /paw.ˈpɛ.ʁi.mu/ (formal; pobríssimo is usual in speech); pobríssimo [Degree=Abs, Gender=Masc, Number=Sing] /po.ˈbɾi.si.mu/ /pu.ˈbɾi.si.mu/
  1. [A1] que tem pouco dinheiro e poucos bens para viver
     - example: Cresci numa família pobre, mas nunca nos faltou carinho.
     - collocations: país pobre; família pobre; bairro pobre; nascer pobre; ficar pobre
     - opposites: rico
  2. [B2] que contém pouca quantidade de alguma coisa ou produz pouco
     - example: Esta terra é pobre e quase nada cresce aqui.
     - frames: pobre em algo
     - collocations: solo pobre; terra pobre; alimentação pobre
     - opposites: rico
  3. [B1] que desperta pena ou compaixão pelo que lhe aconteceu
     - example: A pobre menina perdeu o gato e chorou a tarde toda.
     - frames: pobre de alguém
     - collocations: pobre coitado; pobre diabo; pobre de mim
     - grammar: position Attr; note placed before the noun in this sense
  4. [B2] de pouco valor ou qualidade; fraco e insuficiente
     - example: O texto tem boas ideias, mas um vocabulário muito pobre.
     - collocations: vocabulário pobre; argumento pobre
**Lexeme 2: pobre**, NOUN
- grammar: gender Com; countability Count
- said: /ˈpɔ.bɾi/ (pt-BR), /ˈpɔ.bɾɨ/ (pt-PT)
  1. [A2] pessoa que tem pouco dinheiro e vive com dificuldades
     - example: A cidade criou um programa de ajuda aos pobres.
     - collocations: ajudar os pobres; dar aos pobres
     - opposites: rico
  2. [B1] pessoa que desperta pena pelo que lhe aconteceu
     - example: O pobre esperou horas à chuva sem guarda-chuva.

### 28. prédio

**Lexeme 1: prédio**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈpɾɛ.dʒju/ (pt-BR), /ˈpɾɛ.dju/ (pt-PT)
  1. [A1] construção com vários andares onde as pessoas moram ou trabalham
     - example: Moramos no terceiro andar de um prédio sem elevador.
     - collocations: prédio de apartamentos; prédio residencial; prédio comercial; prédio de escritórios; andar do prédio; porteiro do prédio; construir um prédio
  2. [C1] bem imóvel, terreno ou construção, considerado como propriedade
     - example: O prédio rústico inclui a casa e os terrenos à volta.
     - collocations: prédio rústico; prédio urbano
     - labels: domain law
- relations: confusable edifício (NOUN): in Portugal 'prédio' is mostly an apartment block, while any large building is usually 'edifício'; Brazil uses 'prédio' for buildings in general

### 29. global

**Lexeme 1: global**, ADJ
- grammar: note same form for masculine and feminine
- said: /ɡlo.ˈbaw/ (pt-BR), /ɡlu.ˈbal/ (pt-PT)
  1. [B1] que tem alcance mundial ou envolve países de vários continentes
     - example: O aquecimento global preocupa cientistas de todos os continentes.
     - opposites: local
  2. [B2] considerado no seu conjunto, sem separar as partes
     - example: Qual é o custo global da obra, com todos os materiais?
     - opposites: parcial
- relations: adjective_of globo (NOUN)

### 30. campeão

**Lexeme 1: campeão**, NOUN
- grammar: gender Masc; countability Count; note feminine campeã
- said: /kɐ̃.pi.ˈɐ̃w/ (pt-BR), /kɐ̃.ˈpjɐ̃w/ (pt-PT)
- forms: campeões [Number=Plur] /kɐ̃.pi.ˈõjs/ /kɐ̃.ˈpjõjʃ/
  1. [A2] pessoa ou equipa que ganha uma competição ou um torneio
     - example: Os atuais campeões vão defender o título no próximo mês.
     - frames: campeão de algo
     - collocations: campeão mundial; campeão olímpico; campeão nacional; atual campeão; sagrar-se campeão; ser campeão; campeão europeu
  2. [B2] pessoa ou coisa que se destaca mais do que todas as outras em algo
     - example: Este livro foi o campeão de vendas do ano passado.
     - frames: campeão de algo
     - collocations: campeão de vendas; campeão de audiência; campeão de bilheteira
     - labels: figurative True
  3. [C1] pessoa que defende uma causa com muito empenho e coragem
     - example: Ele sempre foi um campeão dos direitos dos trabalhadores.
     - frames: campeão de algo
     - labels: style literary
**Lexeme 2: campeão**, ADJ
- grammar: comparable False
- said: /kɐ̃.pi.ˈɐ̃w/ (pt-BR), /kɐ̃.ˈpjɐ̃w/ (pt-PT)
- forms: campeã [Gender=Fem, Number=Sing] /kɐ̃.pi.ˈɐ̃/ /kɐ̃.ˈpjɐ̃/; campeões [Gender=Masc, Number=Plur] /kɐ̃.pi.ˈõjs/ /kɐ̃.ˈpjõjʃ/; campeãs [Gender=Fem, Number=Plur] /kɐ̃.pi.ˈɐ̃s/ /kɐ̃.ˈpjɐ̃ʃ/
  1. [B1] que venceu uma competição ou um torneio
     - example: A equipa campeã desfilou pelas ruas da cidade.
     - collocations: equipa campeã; time campeão; clube campeão

### 31. impacto

**Lexeme 1: impacto**, NOUN
- grammar: gender Masc; countability Count
- said: /ĩ.ˈpak.tu/ (pt-BR), /ĩ.ˈpak.tu/ (pt-PT)
  1. [B1] efeito forte que um acontecimento ou uma ação produz em algo ou alguém
     - example: As novas regras vão ter um impacto enorme na vida dos estudantes.
     - frames: impacto em algo/alguém; impacto sobre algo/alguém; impacto de algo
     - collocations: ter impacto; causar impacto; impacto ambiental; impacto social; impacto negativo; impacto positivo; grande impacto; avaliar o impacto; reduzir o impacto
  2. [B2] choque violento de um corpo que bate contra outro
     - example: Com o impacto, o vidro da frente partiu-se em pedaços.
     - frames: impacto contra algo; impacto com algo
     - collocations: a força do impacto; no momento do impacto; ponto de impacto; resistir ao impacto; amortecer o impacto

### 32. palco

**Lexeme 1: palco**, NOUN
- grammar: gender Masc; countability Mass
- said: /ˈpaw.ku/ (pt-BR), /ˈpal.ku/ (pt-PT)
  1. [A2] plataforma elevada onde artistas se apresentam diante do público
     - example: Os músicos subiram ao palco sob muitos aplausos.
     - collocations: subir ao palco; subir no palco; entrar em palco; em cima do palco; pisar o palco; sair do palco; palco principal
  2. [B2] lugar onde ocorre um acontecimento ou uma série de acontecimentos
     - example: Esta praça foi palco de vários protestos durante o ano.
     - frames: palco de algo; ser palco de algo
     - labels: figurative True

### 33. rico

**Lexeme 1: rico**, ADJ
- said: /ˈʁi.ku/ (pt-BR), /ˈʁi.ku/ (pt-PT)
  1. [A1] que tem muito dinheiro ou muitos bens
     - example: Ele ficou rico depois de vender a empresa.
     - collocations: ficar rico; país rico; família rica; rico e famoso
     - opposites: pobre
  2. [B1] que contém grande quantidade de alguma substância ou elemento
     - example: Este peixe é rico em proteínas e muito saudável.
     - frames: rico em algo
     - collocations: rico em vitaminas; rico em proteínas; solo rico; alimentação rica
     - opposites: pobre
  3. [B2] cheio de variedade, conteúdo ou valor
     - example: Viver no estrangeiro foi uma experiência muito rica para mim.
     - collocations: vocabulário rico; experiência rica; cultura rica; história rica
     - opposites: pobre
  4. [C1] muito querido, dito com carinho de uma pessoa
     - example: Anda cá, meu rico filho, dá um abraço à mãe.
     - labels: formality informal, attitude endearing
     - regions: pt-PT mainly
     - grammar: position Attr; note before the noun, usually after a possessive
**Lexeme 2: rico**, NOUN
- grammar: gender Masc; countability Count; number Plur, mainly; note feminine rica; mostly in the plural, os ricos
- said: /ˈʁi.ku/ (pt-BR), /ˈʁi.ku/ (pt-PT)
  1. [A2] pessoa que tem muito dinheiro ou muitos bens
     - example: Os ricos deviam pagar mais impostos do que os outros.
     - collocations: ricos e pobres
     - opposites: pobre
- relations: derived_from rico (ADJ)

### 34. expressão

**Lexeme 1: expressão**, NOUN
- grammar: gender Masc; countability Both
- said: /es.pɾe.ˈsɐ̃w/ (pt-BR), /is.pɾe.ˈsɐ̃w/ (pt-BR), /ɐjʃ.pɾɨ.ˈsɐ̃w/ (pt-PT), /ɨʃ.pɾɨ.ˈsɐ̃w/ (pt-PT)
- forms: expressões [Number=Plur] /es.pɾe.ˈsõjs/ /ɐjʃ.pɾɨ.ˈsõjʃ/
  1. [B1] comunicação de ideias, sentimentos ou opiniões por palavras, gestos ou arte
     - example: A liberdade de expressão é um direito de todos os cidadãos.
     - frames: expressão de algo
     - collocations: liberdade de expressão; forma de expressão; meio de expressão; expressão artística; expressão corporal; expressão escrita; expressão oral
     - grammar: countability Mass, mainly
  2. [B1] aspeto do rosto que mostra o que uma pessoa sente ou pensa
     - example: Pela expressão do teu rosto, percebo que não gostaste da notícia.
     - frames: expressão de algo
     - collocations: expressão facial; mudar de expressão; expressão séria; expressão de surpresa; expressão de alívio
     - grammar: countability Count
  3. [A2] palavra ou grupo de palavras usado com um sentido próprio
     - example: Os alunos aprenderam hoje várias expressões populares sobre o tempo.
     - collocations: expressão idiomática; expressão popular; expressão fixa; usar uma expressão
     - grammar: countability Count
  4. [B2] conjunto de números, letras e sinais que representa um valor ou cálculo
     - example: Simplifiquem a expressão antes de substituir o valor de x.
     - collocations: expressão algébrica; expressão numérica; simplificar uma expressão
     - labels: domain mathematics
     - grammar: countability Count
  5. [C1] importância ou peso que algo tem num conjunto
     - example: O movimento ganhou expressão nas grandes cidades nos últimos anos.
     - collocations: sem expressão; ganhar expressão; expressão significativa; expressão eleitoral
     - grammar: countability Mass

### 35. atitude

**Lexeme 1: atitude**, NOUN
- grammar: gender Fem
- said: /a.tʃi.ˈtu.dʒi/ (pt-BR), /ɐ.ti.ˈtu.dɨ/ (pt-PT)
  1. [B1] maneira de pensar e de se comportar diante de alguém ou de algo
     - example: Os professores notaram uma mudança de atitude nos alunos este ano.
     - frames: atitude perante algo/alguém; atitude em relação a algo/alguém; atitude com alguém
     - collocations: mudança de atitude; mudar de atitude; atitude positiva; atitude negativa; adotar uma atitude; assumir uma atitude
     - grammar: countability Count
  2. [B1] ação ou decisão para resolver uma situação ou um problema
     - example: Se ninguém tomar uma atitude, a situação vai piorar.
     - collocations: tomar uma atitude; atitude drástica; atitude firme
     - grammar: countability Count
  3. [B2] confiança e personalidade forte na forma de agir
     - example: Ela é uma cantora com muita atitude em palco.
     - collocations: ter atitude; de atitude
     - labels: formality informal
     - grammar: countability Mass
  4. [C1] posição em que o corpo está ou se mantém
     - example: O modelo manteve a mesma atitude durante horas para o pintor.
     - labels: style literary
     - grammar: countability Count

### 36. policial

**Lexeme 1: policial**, ADJ
- grammar: comparable False
- said: /po.li.ˈsjaw/ (pt-BR), /pu.li.ˈsjal/ (pt-PT)
  1. [B1] das forças que mantêm a ordem pública e combatem o crime
     - example: Houve uma operação policial no bairro durante a noite.
  2. [B1] que conta ou desenvolve uma história de crime e investigação
     - example: Adoro ler romances policiais antes de dormir.
- relations: adjective_of polícia (NOUN)
**Lexeme 2: policial**, NOUN
- grammar: gender Com; countability Count
- said: /po.li.ˈsjaw/ (pt-BR), /pu.li.ˈsjal/ (pt-PT)
  1. [A2] agente encarregado de manter a ordem pública e combater o crime
     - example: Uma policial nos ajudou a encontrar a estação.
     - regions: pt-BR mainly
- relations: derived_from polícia (NOUN); confusable polícia (NOUN): in Portugal a police officer is usually called 'um polícia', while Brazil says 'um policial'
**Lexeme 3: policial**, NOUN
- grammar: gender Masc; countability Count
- said: /po.li.ˈsjaw/ (pt-BR), /pu.li.ˈsjal/ (pt-PT)
  1. [B2] obra de ficção que acompanha um crime e a sua investigação
     - example: Li um policial e só descobri o culpado na última página.

### 37. divisão

**Lexeme 1: divisão**, NOUN
- grammar: gender Fem
- said: /dʒi.vi.ˈzɐ̃w/ (pt-BR), /di.vi.ˈzɐ̃w/ (pt-PT)
- forms: divisões [Number=Plur] /dʒi.vi.ˈzõjs/ /di.vi.ˈzõjʃ/
  1. [B1] separação de um todo em partes que se repartem
     - example: A divisão das tarefas entre os irmãos causou muitas discussões.
     - frames: divisão de algo; divisão de algo entre alguém
     - collocations: divisão de tarefas; divisão de bens; divisão do trabalho; divisão justa; divisão igual
     - grammar: countability Both
  2. [B2] parte de uma empresa ou organização com funções próprias
     - example: Ela trabalha na divisão de vendas há dois anos.
     - collocations: chefe de divisão
     - grammar: countability Count
  3. [B1] cada um dos grupos de equipas de uma competição, por nível
     - example: O clube vai jogar na primeira divisão na próxima época.
     - collocations: primeira divisão; segunda divisão; subir de divisão; descer de divisão
     - grammar: countability Count
  4. [A2] operação de calcular quantas vezes um número cabe noutro
     - example: As crianças aprendem a divisão depois da multiplicação.
     - frames: divisão de algo por algo
     - collocations: fazer uma divisão; divisão exata
     - grammar: countability Count
     - opposites: multiplicação
  5. [B2] falta de acordo ou de união entre pessoas de um grupo
     - example: A divisão dentro do partido enfraqueceu a campanha eleitoral.
     - collocations: divisão interna; provocar divisão; criar divisão
     - grammar: countability Both
     - opposites: união
  6. [B1] cada um dos espaços separados por paredes dentro de uma casa
     - example: Procuramos um apartamento com quatro divisões perto do centro.
     - regions: pt-PT mainly
     - grammar: countability Count
  7. [C1] grande unidade do exército formada por várias brigadas
     - example: Uma divisão inteira foi enviada para a fronteira.
     - labels: domain military
     - grammar: countability Count
- relations: verbal_noun_of dividir (VERB)

### 38. agradecer

**Lexeme 1: agradecer**, VERB
- grammar: conjugation 2; valency Tr, mainly; irregular False; note c becomes ç before a and o (agradeço, agradeça); stressed e is open in agradece
- said: /a.ɡɾa.de.ˈseɾ/ (pt-BR), /ɐ.ɡɾɐ.dɨ.ˈseɾ/ (pt-PT)
- forms: agradeço [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /a.ɡɾa.ˈde.su/ /ɐ.ɡɾɐ.ˈde.su/; agradeces [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /a.ɡɾa.ˈdɛ.sis/ /ɐ.ɡɾɐ.ˈdɛ.sɨʃ/; agradece [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /a.ɡɾa.ˈdɛ.si/ /ɐ.ɡɾɐ.ˈdɛ.sɨ/; agradecemos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /a.ɡɾa.de.ˈse.mus/ /ɐ.ɡɾɐ.dɨ.ˈse.muʃ/; agradecem [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /a.ɡɾa.ˈdɛ.sẽj/ /ɐ.ɡɾɐ.ˈdɛ.sɐ̃j/; agradeci [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /a.ɡɾa.de.ˈsi/ /ɐ.ɡɾɐ.dɨ.ˈsi/; agradeceu [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /a.ɡɾa.de.ˈsew/ /ɐ.ɡɾɐ.dɨ.ˈsew/; agradeça [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /a.ɡɾa.ˈde.sɐ/ /ɐ.ɡɾɐ.ˈde.sɐ/; agradecido [VerbForm=Part, Tense=Past] /a.ɡɾa.de.ˈsi.du/ /ɐ.ɡɾɐ.dɨ.ˈsi.du/
  1. [A2] dizer ou mostrar a alguém que se está grato pelo que fez
     - example: Quero agradecer a todos pela ajuda durante a mudança.
     - frames: agradecer algo a alguém; agradecer a alguém por algo; agradecer por algo; agradecer que + subj
     - collocations: agradecer de coração; agradecer imensamente; agradecer a ajuda; agradecer a atenção; agradecer a Deus; agradecer pessoalmente; agradecer o convite
     - grammar: note in Brazil the person thanked is also often a direct object (agradecer alguém)

### 39. assembleia

**Lexeme 1: assembleia**, NOUN
- grammar: gender Fem; countability Count
- said: /a.sẽ.ˈblɛj.ɐ/ (pt-BR), /ɐ.sẽ.ˈblɐj.ɐ/ (pt-PT)
- spellings: assembleia (pt-BR, pt-PT); assembléia (pt-BR) [older books only]
  1. [B1] reunião dos membros de um grupo para discutir e decidir assuntos comuns
     - example: Os moradores vão se reunir em assembleia na próxima semana.
     - collocations: assembleia geral; convocar uma assembleia; reunir-se em assembleia; realizar uma assembleia; assembleia de acionistas; assembleia de condomínio
  2. [B2] órgão formado por representantes eleitos que discute e aprova as leis
     - example: A assembleia aprovou ontem a nova lei por grande maioria.
     - collocations: assembleia legislativa; assembleia constituinte; assembleia municipal; Assembleia da República

### 40. aceitar

**Lexeme 1: aceitar**, VERB
- grammar: conjugation 1; valency Tr; irregular False; note two past participles: aceitado with ter, aceito (pt-BR) or aceite (pt-PT) mostly with ser and estar
- said: /a.sej.ˈtaɾ/ (pt-BR), /ɐ.sɐj.ˈtaɾ/ (pt-PT)
- forms: aceito [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /a.ˈsej.tu/ /ɐ.ˈsɐj.tu/; aceitas [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /a.ˈsej.tas/ /ɐ.ˈsɐj.tɐʃ/; aceita [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /a.ˈsej.tɐ/ /ɐ.ˈsɐj.tɐ/; aceitamos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /a.sej.ˈtɐ̃.mus/ /ɐ.sɐj.ˈtɐ.muʃ/; aceitam [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /a.ˈsej.tɐ̃w/ /ɐ.ˈsɐj.tɐ̃w/; aceitei [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /a.sej.ˈtej/ /ɐ.sɐj.ˈtɐj/; aceitou [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /a.sej.ˈto/ /ɐ.sɐj.ˈto/; aceite [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /a.ˈsej.tʃi/ /ɐ.ˈsɐj.tɨ/; aceitado [VerbForm=Part, Tense=Past] /a.sej.ˈta.du/ /ɐ.sɐj.ˈta.du/ (mostly after ter and haver); aceito [VerbForm=Part, Tense=Past] /a.ˈsej.tu/ (pt-BR) (mostly after ser and estar); aceite [VerbForm=Part, Tense=Past] /ɐ.ˈsɐj.tɨ/ (pt-PT) (mostly after ser and estar)
  1. [A2] receber de boa vontade o que é oferecido ou proposto
     - example: Aceitei o convite para jantar com eles no sábado.
     - frames: aceitar algo; aceitar algo de alguém; aceitar + inf
     - collocations: aceitar um convite; aceitar uma proposta; aceitar um presente; aceitar uma oferta; aceitar um emprego; aceitar um desafio; aceitar cartão
     - opposites: recusar
  2. [B1] considerar verdadeiro, correto ou válido
     - example: Os cientistas demoraram muito a aceitar a nova teoria.
     - frames: aceitar algo; aceitar que + ind
     - collocations: aceitar uma desculpa; aceitar as regras; aceitar críticas; aceitar uma explicação
     - opposites: rejeitar
  3. [B1] conformar-se com uma situação difícil que não se pode mudar
     - example: Custa aceitar que nunca mais vamos voltar àquela casa.
     - frames: aceitar algo; aceitar que + subj
     - collocations: aceitar a derrota; aceitar a realidade; aceitar o destino; aceitar a morte
  4. [B1] admitir alguém num grupo ou instituição, tratando-o como parte dele
     - example: O clube só aceita sócios maiores de dezoito anos.
     - frames: aceitar alguém; aceitar alguém em algo
     - opposites: rejeitar

### 41. exposição

**Lexeme 1: exposição**, NOUN
- grammar: gender Fem; countability Both
- said: /es.po.zi.ˈsɐ̃w/ (pt-BR), /ɐjʃ.pu.zi.ˈsɐ̃w/ (pt-PT), /is.po.zi.ˈsɐ̃w/ (pt-BR), /iʃ.pu.zi.ˈsɐ̃w/ (pt-PT)
  1. [A2] mostra pública de obras, objetos ou produtos para as pessoas verem
     - example: A exposição prolongada ao sol pode causar queimaduras graves.
     - frames: exposição de algo
     - collocations: visitar uma exposição; inaugurar uma exposição; exposição de arte; exposição permanente; exposição temporária; sala de exposição
     - grammar: countability Count
  2. [B2] situação de estar sem proteção sob a ação de algo
     - example: Vamos visitar a exposição de fotografia no museu amanhã.
     - frames: exposição a algo
     - collocations: exposição ao sol; exposição prolongada; exposição à radiação
     - grammar: countability Mass
  3. [B2] apresentação ordenada de ideias ou argumentos, falada ou por escrito
     - example: O professor fez uma exposição clara sobre a Revolução Industrial.
     - frames: exposição sobre algo; exposição de algo
     - collocations: fazer uma exposição; exposição de motivos; exposição oral
     - grammar: countability Count
  4. [C1] situação de estar à vista do público ou dos meios de comunicação
     - example: Muitos artistas sofrem com a exposição excessiva da sua vida privada.
     - collocations: exposição pública; exposição excessiva; evitar a exposição
     - grammar: countability Mass
  5. [C1] luz recebida pelo filme ou pelo sensor ao tirar uma fotografia
     - example: Com pouca luz, é preciso aumentar o tempo de exposição.
     - collocations: tempo de exposição
     - labels: domain photography
- relations: verbal_noun_of expor (VERB)

### 42. álbum

**Lexeme 1: álbum**, NOUN
- grammar: gender Masc; countability Count; note plural álbuns
- said: /ˈaw.bũ/ (pt-BR), /ˈal.bũ/ (pt-PT)
  1. [A2] conjunto de músicas gravadas e lançadas juntas por um artista
     - example: A banda vai lançar o seu terceiro álbum no próximo mês.
     - collocations: lançar um álbum; gravar um álbum; álbum de estreia; álbum ao vivo; álbum duplo; novo álbum
  2. [A2] livro com páginas próprias para guardar e mostrar fotografias ou coleções
     - example: Guardo as fotos antigas da família num álbum de couro.
     - collocations: álbum de fotografias; álbum de fotos; álbum de família; álbum de casamento; álbum de figurinhas; folhear um álbum

### 43. gênero

**Lexeme 1: gênero**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈʒe.ne.ɾu/ (pt-BR, gênero), /ˈʒɛ.nɨ.ɾu/ (pt-PT, género)
- spellings: gênero (pt-BR); género (pt-PT)
  1. [B1] conjunto de coisas ou seres que têm características em comum
     - example: Nunca tinha visto um problema deste gênero na empresa.
     - frames: gênero de algo
     - collocations: algo do gênero; do mesmo gênero; único no seu gênero
  2. [B1] cada categoria de obras de arte com forma, estilo ou tema próprios
     - example: O terror é o gênero de cinema de que mais gosto.
     - collocations: gênero literário; gênero musical; gênero cinematográfico; gênero textual
  3. [B2] papéis e identidades que a sociedade associa a ser homem ou mulher
     - example: A escola promove debates sobre igualdade de gênero entre os alunos.
     - collocations: identidade de gênero; igualdade de gênero; violência de gênero; estudos de gênero; questões de gênero
  4. [B1] categoria gramatical que divide os nomes em masculinos e femininos
     - example: Em português, o adjetivo concorda em gênero e número com o substantivo.
     - collocations: gênero masculino; gênero feminino; concordar em gênero e número
     - labels: domain grammar
  5. [B2] mercadorias, sobretudo alimentos, que se compram e vendem
     - example: Os preços dos gêneros alimentícios subiram bastante este mês.
     - collocations: gêneros alimentícios; gêneros de primeira necessidade
     - grammar: number Plur

### 44. quadro

**Lexeme 1: quadro**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈkwa.dɾu/ (pt-BR), /ˈkwa.dɾu/ (pt-PT)
  1. [A2] obra de pintura ou desenho feita numa superfície, muitas vezes com moldura
     - example: Na sala da minha avó há um quadro com flores amarelas.
     - collocations: pendurar um quadro; pintar um quadro; quadro a óleo; exposição de quadros
  2. [A1] superfície lisa, presa à parede, onde se escreve com giz ou marcador
     - example: A professora escreveu a data no quadro antes da aula.
     - collocations: escrever no quadro; apagar o quadro; ir ao quadro; quadro branco; quadro de avisos
  3. [B1] tabela que apresenta dados organizados em linhas e colunas
     - example: Os resultados do inquérito aparecem no quadro da página seguinte.
     - collocations: quadro comparativo; quadro-resumo
  4. [B2] conjunto das circunstâncias em que algo se encontra num dado momento
     - example: O médico explicou que o quadro do doente ainda é grave.
     - frames: no quadro de algo
     - collocations: quadro clínico; quadro político; quadro econômico; traçar um quadro; agravar o quadro
  5. [B2] conjunto das pessoas que trabalham de forma fixa numa empresa ou organização
     - example: A empresa vai aumentar o quadro de funcionários no próximo ano.
     - collocations: quadro de funcionários; quadro de pessoal; fazer parte do quadro; entrar para o quadro; quadro efetivo
     - grammar: number Sing, mainly
  6. [C1] pessoa com cargo técnico ou de direção numa empresa, partido ou organização
     - example: Muitos quadros qualificados deixam o país à procura de melhores salários.
     - collocations: quadros superiores; quadros técnicos; quadros do partido
     - grammar: number Plur, mainly
  7. [B2] estrutura de metal que forma o corpo de uma bicicleta ou mota
     - example: O quadro desta bicicleta é de alumínio e pesa muito pouco.
     - collocations: quadro da bicicleta
  8. [B2] parte de um programa de televisão ou rádio com tema próprio
     - example: O quadro de culinária é a parte mais vista do programa.
     - regions: pt-BR mainly

### 45. sessão

**Lexeme 1: sessão**, NOUN
- grammar: gender Fem; countability Mass
- said: /se.ˈsɐ̃w/ (pt-BR), /sɨ.ˈsɐ̃w/ (pt-PT)
- forms: sessões [Number=Plur] /se.ˈsõjs/ /sɨ.ˈsõjʃ/
  1. [B1] reunião de um órgão ou grupo para discutir e decidir assuntos
     - example: A sessão da câmara começa às nove horas da manhã.
     - collocations: abrir a sessão; encerrar a sessão; sessão plenária; sessão solene; sessão extraordinária; em sessão
  2. [A2] cada vez que um filme ou espetáculo é apresentado ao público
     - example: Vamos à sessão das sete, que costuma ter menos gente.
     - collocations: sessão de cinema; última sessão; sessão da tarde; sessão esgotada
  3. [B1] período de tempo dedicado a uma certa atividade
     - example: Cada sessão de fisioterapia dura cerca de quarenta minutos.
     - frames: sessão de algo
     - collocations: sessão de fotos; sessão de terapia; sessão de treino; sessão de autógrafos
  4. [B1] período em que alguém está identificado e ligado a um sistema informático
     - example: Não te esqueças de terminar a sessão no computador da biblioteca.
     - collocations: iniciar sessão; terminar sessão; encerrar a sessão
- relations: confusable cessão (NOUN): sounds the same but means the act of giving up or transferring something; confusable seção (NOUN): similar spelling and sound; seção (pt-PT secção) means a section or department

### 46. localização

**Lexeme 1: localização**, NOUN
- grammar: gender Fem
- said: /lo.ka.li.za.ˈsɐ̃w/ (pt-BR), /lu.kɐ.li.zɐ.ˈsɐ̃w/ (pt-PT)
  1. [B1] posição de algo ou alguém num espaço ou num território
     - example: A localização do apartamento é perfeita para quem trabalha no centro.
     - frames: localização de algo/alguém
     - collocations: boa localização; localização privilegiada; localização geográfica; localização exata; localização central
     - grammar: countability Count
  2. [B2] descoberta do ponto exato onde algo ou alguém se encontra
     - example: A polícia trabalha na localização dos turistas desaparecidos na serra.
     - frames: localização de algo/alguém
     - collocations: localização por GPS; sistema de localização
     - grammar: countability Mass
- relations: verbal_noun_of localizar (VERB)

### 47. programação

**Lexeme 1: programação**, NOUN
- grammar: gender Fem; countability Both
- said: /pɾo.ɡɾa.ma.ˈsɐ̃w/ (pt-BR), /pɾu.ɡɾɐ.mɐ.ˈsɐ̃w/ (pt-PT)
  1. [B1] atividade de escrever as instruções que um computador executa
     - example: Estou a aprender programação para criar os meus próprios jogos.
     - collocations: linguagem de programação; aprender programação; curso de programação; programação de computadores
     - grammar: countability Mass
  2. [B1] conjunto das emissões ou atividades previstas, com os seus horários
     - example: A programação do festival inclui concertos ao ar livre todas as noites.
     - frames: programação de algo
     - collocations: programação da televisão; programação cultural; programação infantil; grade de programação; horário nobre da programação
     - grammar: countability Count, mainly
  3. [B2] organização antecipada de tarefas, gastos ou atividades a realizar
     - example: Com uma boa programação, conseguimos visitar três cidades numa semana.
     - frames: programação de algo
     - collocations: programação financeira; fazer a programação
     - grammar: countability Mass, mainly
- relations: verbal_noun_of programar (VERB)

### 48. quente

**Lexeme 1: quente**, ADJ
- grammar: comparable True; note same form for masculine and feminine
- said: /ˈkẽ.tʃi/ (pt-BR), /ˈkẽ.tɨ/ (pt-PT)
  1. [A1] que tem ou transmite temperatura elevada
     - example: Cuidado, a sopa ainda está muito quente!
     - collocations: água quente; tempo quente; dia quente; clima quente; comida quente; bebida quente; banho quente
     - opposites: frio
  2. [A2] que protege do frio e conserva o calor do corpo
     - example: Leva um casaco quente, que à noite faz frio.
     - collocations: roupa quente; casaco quente; cobertor quente
  3. [B2] que causa muita discussão, interesse ou emoção
     - example: A reforma das pensões continua a ser um tema quente no parlamento.
     - collocations: tema quente; assunto quente; discussão quente; notícia quente
     - labels: figurative True
  4. [B2] diz-se das cores e tons que lembram o fogo e o sol
     - example: Pintámos a sala com cores quentes para a tornar acolhedora.
     - collocations: cores quentes; tons quentes
     - opposites: frio
  5. [B2] cheio de desejo e sensualidade
     - example: O filme tem algumas cenas quentes que não são para crianças.
     - collocations: cena quente
     - labels: formality informal

### 49. orgulho

**Lexeme 1: orgulho**, NOUN
- grammar: gender Masc
- said: /oɾ.ˈɡu.ʎu/ (pt-BR), /ɔɾ.ˈɡu.ʎu/ (pt-PT)
  1. [B1] satisfação que se sente pelo que se fez ou pelo valor de alguém próximo
     - example: Os pais assistiram à formatura da filha cheios de orgulho.
     - frames: orgulho de algo/alguém; orgulho em + inf; orgulho por algo/alguém
     - collocations: ter orgulho; sentir orgulho; motivo de orgulho; com orgulho; encher de orgulho; orgulho nacional
     - grammar: countability Mass
     - opposites: vergonha
  2. [B2] opinião exagerada sobre o próprio valor, que leva a desprezar os outros
     - example: Não lhe peço desculpa porque o orgulho não me deixa.
     - collocations: orgulho ferido; engolir o orgulho; ferir o orgulho
     - grammar: countability Mass
     - opposites: humildade
  3. [B2] pessoa ou coisa de que alguém tem muita satisfação
     - example: Este jardim é o orgulho do meu avô.
     - frames: o orgulho de alguém/algo
     - grammar: countability Count; number Sing

### 50. pretenso

**Lexeme 1: pretenso**, ADJ
- grammar: comparable False; position Attr, mainly; note usually placed before the noun
- said: /pɾe.ˈtẽ.su/ (pt-BR), /pɾɨ.ˈtẽ.su/ (pt-PT)
  1. [C1] que se apresenta ou é tido como algo, sem o ser realmente
     - example: O pretenso médico foi preso por enganar dezenas de pacientes.
     - labels: formality formal
- relations: confusable pretensioso (ADJ): pretenso means 'so-called, alleged', while pretensioso means 'pretentious, conceited'

### 51. live

**Lexeme 1: live**, NOUN
- grammar: gender Fem; countability Count; note English loanword; plural lives
- said: /ˈlaj.vi/ (pt-BR), /ˈlajv/ (pt-PT)
  1. [B1] transmissão de vídeo em tempo real pela internet, feita numa rede social
     - example: Vou fazer uma live hoje à noite para responder às perguntas.
     - collocations: fazer uma live; assistir à live; entrar na live; live solidária
     - regions: pt-BR mainly

### 52. audiência

**Lexeme 1: audiência**, NOUN
- grammar: gender Fem; countability Count
- said: /aw.dʒi.ˈẽ.sjɐ/ (pt-BR), /aw.ˈdjẽ.sjɐ/ (pt-PT)
  1. [B1] conjunto das pessoas que assistem ou ouvem um programa, espetáculo ou discurso
     - example: O programa de domingo teve a maior audiência do ano.
     - collocations: índice de audiência; líder de audiência; recorde de audiência; ganhar audiência; perder audiência; audiência televisiva
  2. [B2] sessão de tribunal em que o juiz ouve as partes e as testemunhas
     - example: A audiência foi adiada para a próxima semana por falta de testemunhas.
     - collocations: audiência de julgamento; marcar uma audiência; adiar a audiência; audiência de custódia
     - labels: domain law
  3. [B2] encontro formal em que uma autoridade recebe alguém para o ouvir
     - example: Os moradores pediram uma audiência ao prefeito para discutir o problema.
     - frames: audiência com alguém
     - collocations: pedir uma audiência; conceder uma audiência; receber em audiência; audiência pública; audiência privada
     - labels: formality formal
- relations: confusable audição (NOUN): English 'hearing' is 'audiência' in court but 'audição' for the sense of hearing; 'audição' is also an audition

### 53. independência

**Lexeme 1: independência**, NOUN
- grammar: gender Fem; countability Mass
- said: /ĩ.de.pẽ.ˈdẽ.sjɐ/ (pt-BR), /ĩ.dɨ.pẽ.ˈdẽ.sjɐ/ (pt-PT)
  1. [B1] situação de um país que se governa sozinho, sem domínio estrangeiro
     - example: O país conquistou a independência depois de uma longa guerra.
     - frames: independência de algo
     - collocations: declarar a independência; proclamar a independência; conquistar a independência; lutar pela independência; Dia da Independência; guerra da independência
  2. [B1] capacidade de viver e decidir sozinho, sem ajuda ou controlo dos outros
     - example: Com o primeiro emprego, ela ganhou finalmente a sua independência.
     - frames: independência de alguém
     - collocations: independência financeira; ganhar independência; perder a independência
     - opposites: dependência
  3. [B2] liberdade de agir ou julgar sem pressão de poderes ou interesses externos
     - example: A independência dos juízes é essencial numa democracia.
     - collocations: independência judicial; independência editorial; garantir a independência
- relations: abstract_noun_of independente (ADJ)
