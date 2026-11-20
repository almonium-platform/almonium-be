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

### 2. agricultura

**Lexeme 1: agricultura**, NOUN
- grammar: gender Fem; countability Mass
- said: /a.ɡɾi.kuw.ˈtu.ɾɐ/ (pt-BR), /ɐ.ɡɾi.kul.ˈtu.ɾɐ/ (pt-PT)
  1. [B1] trabalho de preparar a terra e plantar para obter alimentos e outros produtos
     - example: Grande parte da população da região vive da agricultura.
     - collocations: viver da agricultura; dedicar-se à agricultura; agricultura familiar; agricultura de subsistência; agricultura intensiva; agricultura sustentável; agricultura orgânica; agricultura biológica

### 3. competição

**Lexeme 1: competição**, NOUN
- grammar: gender Fem; countability Both
- said: /kõ.pe.tʃi.ˈsɐ̃w/ (pt-BR), /kõ.pɨ.ti.ˈsɐ̃w/ (pt-PT)
  1. [B1] disputa entre pessoas ou grupos que procuram obter a mesma coisa
     - example: Há uma competição muito forte entre as duas lojas do bairro.
     - frames: competição entre alguém e alguém; competição por algo; competição com alguém
     - collocations: competição acirrada; competição feroz; competição saudável; forte competição; competição desleal
     - grammar: countability Mass, mainly
     - opposites: cooperação
  2. [A2] evento em que vários participantes disputam uma vitória ou um título
     - example: Os atletas treinam todos os dias para a próxima competição internacional.
     - frames: competição de algo
     - collocations: ganhar uma competição; vencer a competição; organizar uma competição; competição esportiva; competição desportiva; fora de competição
     - grammar: countability Count
- relations: verbal_noun_of competir (VERB)

### 4. canto

**Lexeme 1: canto**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈkɐ̃.tu/ (pt-BR), /ˈkɐ̃.tu/ (pt-PT)
  1. [A2] lugar onde se encontram duas paredes, dois lados ou duas linhas
     - example: O gato dorme sempre no canto do sofá.
     - collocations: canto do olho; canto da boca; olhar de canto
     - grammar: countability Count
  2. [B1] arte ou ação de produzir sons musicais com a voz
     - example: Ela tem aulas de canto duas vezes por semana.
     - collocations: aula de canto; professor de canto; canto coral; canto gregoriano
     - grammar: countability Mass
  3. [B1] som melodioso que as aves e alguns outros animais produzem
     - example: Acordamos cedo com o canto dos pássaros.
     - collocations: canto dos pássaros; canto do galo
  4. [B2] lugar afastado ou reservado, ou um lugar qualquer não determinado
     - example: Há gente assim em todos os cantos do mundo.
     - collocations: os quatro cantos do mundo; em todo canto; em qualquer canto
     - grammar: countability Count
  5. [B2] pontapé dado de um ângulo do campo depois de a bola sair pela linha de fundo
     - example: A equipa marcou o golo na sequência de um canto.
     - collocations: marcar um canto
     - regions: pt-PT only
     - grammar: countability Count
  6. [C1] cada uma das partes em que se divide um poema longo
     - example: O poema épico divide-se em dez cantos.
     - labels: domain literature
     - grammar: countability Count

### 5. perigo

**Lexeme 1: perigo**, NOUN
- grammar: gender Masc; countability Both
- said: /pe.ˈɾi.ɡu/ (pt-BR), /pɨ.ˈɾi.ɡu/ (pt-PT), /pi.ˈɾi.ɡu/ (pt-BR), /ˈpɾi.ɡu/ (pt-PT, casual)
  1. [A2] situação em que alguém ou algo pode sofrer dano ou mal
     - example: Os bombeiros dizem que as famílias já estão fora de perigo.
     - frames: perigo de + inf; perigo de algo
     - collocations: correr perigo; em perigo; fora de perigo; pôr em perigo; perigo de vida; perigo iminente; sinal de perigo
     - grammar: countability Mass, mainly
     - opposites: segurança
  2. [B1] aquilo ou aquele que pode causar dano ou mal
     - example: Conduzir depois de beber é um perigo para todos na estrada.
     - frames: perigo para algo/alguém
     - collocations: enfrentar os perigos; ser um perigo; perigo público
     - grammar: countability Count

### 6. recentemente

**Lexeme 1: recentemente**, ADV
- said: /ʁe.ˌsẽ.tʃi.ˈmẽ.tʃi/ (pt-BR), /ʁɨ.ˌsẽ.tɨ.ˈmẽ.tɨ/ (pt-PT)
  1. [B1] há pouco tempo, num passado não muito distante
     - example: Recentemente, tenho dormido muito mal por causa do calor.
     - collocations: até recentemente; só recentemente; mais recentemente; muito recentemente
- relations: adverb_of recente (ADJ)

### 7. concurso

**Lexeme 1: concurso**, NOUN
- grammar: gender Masc
- said: /kõ.ˈkuɾ.su/ (pt-BR), /kõ.ˈkuʁ.su/ (pt-BR, regional), /kõ.ˈkuɾ.su/ (pt-PT)
  1. [A2] competição em que várias pessoas disputam um prémio ou uma distinção
     - example: A minha filha vai participar num concurso de desenho da escola.
     - frames: concurso de algo
     - collocations: participar num concurso; ganhar um concurso; vencer o concurso; concurso de beleza; concurso de televisão; inscrever-se num concurso
     - grammar: countability Count
  2. [B1] processo público de seleção, com provas ou propostas, para um cargo ou um contrato
     - example: Ele estuda todos os dias porque quer passar no concurso.
     - frames: concurso para algo
     - collocations: concurso público; prestar concurso; passar no concurso; abrir concurso; aprovado em concurso; fazer concurso
     - grammar: countability Count
  3. [C1] ação conjunta de pessoas ou fatores para chegar a um resultado
     - example: A obra só foi possível com o concurso de muitos voluntários.
     - frames: com o concurso de alguém/algo
     - collocations: concurso de circunstâncias
     - labels: formality formal
     - grammar: countability Mass

### 8. vender

**Lexeme 1: vender**, VERB
- grammar: conjugation 2; irregular False
- said: /vẽ.ˈdeɾ/ (pt-BR), /vẽ.ˈdeɾ/ (pt-PT), /vẽ.ˈde/ (pt-BR, casual)
  1. [A1] dar algo a alguém em troca de dinheiro
     - example: Vamos vender o carro antigo antes de comprar outro.
     - frames: vender algo a alguém; vender algo para alguém; vender algo por algo
     - collocations: vender barato; vender caro; vender bem; vender a prazo; vender à vista; vender fiado
     - grammar: valency Tr, mainly; note vender algo para alguém is mostly Brazilian
     - opposites: comprar
  2. [C1] aceitar dinheiro ou vantagens para agir contra os próprios princípios
     - example: Muitos acreditam que aquele juiz se vendeu aos poderosos.
     - frames: vender-se a alguém
     - labels: figurative True, attitude disapproving
     - grammar: valency Refl

### 9. estrela

**Lexeme 1: estrela**, NOUN
- grammar: gender Fem; countability Count
- said: /is.ˈtɾe.lɐ/ (pt-BR), /iʃ.ˈtɾe.lɐ/ (pt-PT), /es.ˈtɾe.lɐ/ (pt-BR, less_common), /ˈʃtɾe.lɐ/ (pt-PT)
  1. [A1] astro que produz luz e calor próprios e brilha no céu
     - example: Longe da cidade, conseguimos ver milhares de estrelas no céu.
     - collocations: estrela cadente; céu estrelado; estrela brilhante; observar as estrelas; contar as estrelas
  2. [A2] figura ou objeto com várias pontas que saem de um centro
     - example: As crianças recortaram estrelas de papel para enfeitar a sala.
     - collocations: estrela de cinco pontas; em forma de estrela
  3. [B1] pessoa muito famosa ou que se destaca muito na sua atividade
     - example: Ela sonha ser uma estrela de cinema um dia.
     - collocations: estrela de cinema; estrela do rock; estrela da equipa; estrela do time; estrela internacional
     - labels: figurative True
  4. [B1] sinal usado para indicar a categoria ou a qualidade de algo numa escala
     - example: Ficamos num hotel de cinco estrelas perto da praia.
     - collocations: hotel de cinco estrelas; dar estrelas; ganhar uma estrela
  5. [C1] sorte ou destino que se acredita guiar a vida de alguém
     - example: Sempre teve boa estrela e saiu ileso de todos os acidentes.
     - collocations: ter boa estrela; nascer com boa estrela; nascer com estrela
     - labels: figurative True

### 10. precisar

**Lexeme 1: precisar**, VERB
- grammar: conjugation 1; irregular False; valency Both
- said: /pɾe.si.ˈzaɾ/ (pt-BR), /pɾi.si.ˈzaɾ/ (pt-BR), /pɾe.si.ˈza/ (pt-BR, casual), /pɾɨ.si.ˈzaɾ/ (pt-PT)
  1. [A1] ter falta de algo ou alguém, ou ser obrigado a fazer algo
     - example: Preciso de ajuda para levar estas caixas até ao carro.
     - frames: precisar de algo/alguém; precisar + inf; precisar de + inf; precisar que + subj
     - collocations: precisar de ajuda; precisar urgentemente; precisa-se; se precisar; não precisa
     - grammar: note before an infinitive, de is usual in Portugal and usually left out in Brazil
  2. [B1] viver com falta de dinheiro ou do que é necessário para viver
     - example: Ela sempre deu roupas às famílias que mais precisavam.
     - collocations: quem precisa
     - grammar: valency Intr
  3. [C1] indicar ou determinar com exatidão
     - example: A testemunha não soube precisar a hora do acidente.
     - frames: precisar algo
     - collocations: difícil de precisar
     - labels: formality formal
     - grammar: valency Tr
- relations: derived_from preciso (ADJ)

### 11. crescer

**Lexeme 1: crescer**, VERB
- grammar: conjugation 2; valency Intr; irregular False; note sc becomes sç before a and o (cresço, cresça); stressed e is close in cresço, open in cresce
- said: /kɾe.ˈseɾ/ (pt-BR), /kɾɨʃ.ˈseɾ/ (pt-PT), /kɾe.ˈse/ (pt-BR, casual), /kɾɨ.ˈseɾ/ (pt-PT)
- forms: cresço [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈkɾe.su/ /ˈkɾeʃ.su/; cresces [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /ˈkɾɛ.sis/ /ˈkɾɛʃ.sɨʃ/; cresce [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈkɾɛ.si/ /ˈkɾɛʃ.sɨ/; crescemos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /kɾe.ˈse.mus/ /kɾɨʃ.ˈse.muʃ/; crescem [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈkɾɛ.sẽj/ /ˈkɾɛʃ.sɐ̃j/; cresci [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /kɾe.ˈsi/ /kɾɨʃ.ˈsi/; cresceu [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /kɾe.ˈsew/ /kɾɨʃ.ˈsew/; cresça [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈkɾe.sɐ/ /ˈkɾeʃ.sɐ/; crescido [VerbForm=Part, Tense=Past] /kɾe.ˈsi.du/ /kɾɨʃ.ˈsi.du/
  1. [A2] ficar maior ou mais alto com o passar do tempo
     - example: As plantas do quintal cresceram muito depois das chuvas.
     - collocations: deixar crescer; crescer depressa; crescer muito
  2. [A2] passar de criança a adulto; ficar mais velho
     - example: Quando eu crescer, quero ser médica como a minha tia.
     - frames: crescer em + lugar
     - collocations: quando crescer
  3. [B1] aumentar em número, quantidade, valor ou intensidade
     - example: A população da cidade cresce a cada ano que passa.
     - frames: crescer em algo
     - collocations: crescer rapidamente; crescer a um ritmo
     - opposites: diminuir
  4. [B2] desenvolver-se e ganhar importância, capacidade ou maturidade
     - example: Ela cresceu muito como pessoa depois daquela viagem.
     - frames: crescer como + n; crescer em algo
     - collocations: crescer profissionalmente; crescer na carreira; crescer como pessoa
     - labels: figurative True

### 12. escolher

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

### 13. ciência

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

### 14. homenagem

**Lexeme 1: homenagem**, NOUN
- grammar: gender Fem; countability Count
- said: /o.me.ˈna.ʒẽj/ (pt-BR), /ɔ.mɨ.ˈna.ʒɐ̃j/ (pt-PT)
  1. [B1] demonstração pública de respeito, admiração ou gratidão por alguém ou algo
     - example: A escola vai fazer uma homenagem aos professores aposentados.
     - frames: homenagem a alguém/algo; em homenagem a alguém/algo
     - collocations: prestar homenagem; render homenagem; fazer uma homenagem; receber uma homenagem; homenagem póstuma; justa homenagem

### 15. sentimento

**Lexeme 1: sentimento**, NOUN
- grammar: gender Masc
- said: /sẽ.tʃi.ˈmẽ.tu/ (pt-BR), /sẽ.ti.ˈmẽ.tu/ (pt-PT)
  1. [A2] estado afetivo que uma pessoa experimenta perante alguém ou alguma situação
     - example: Nem sempre é fácil falar dos nossos sentimentos com os pais.
     - frames: sentimento de algo; sentimento por alguém
     - collocations: sentimento de culpa; ferir os sentimentos; magoar os sentimentos; expressar os sentimentos; esconder os sentimentos; sentimento profundo; sentimento de impotência
     - grammar: countability Count
  2. [B1] expressão de pesar dirigida a quem perdeu alguém querido
     - example: Os meus sentimentos pela morte do seu avô.
     - collocations: meus sentimentos; apresentar os sentimentos
     - grammar: countability Count; number Plur
  3. [B2] emoção e expressividade com que alguém canta, toca ou interpreta algo
     - example: Ela canta aquela canção antiga com muito sentimento.
     - collocations: com sentimento; cantar com sentimento; tocar com sentimento
     - grammar: countability Mass
- relations: derived_from sentir (VERB)

### 16. dono

**Lexeme 1: dono**, NOUN
- grammar: gender Masc; countability Count; note feminine dona
- said: /ˈdõ.nu/ (pt-BR), /ˈdo.nu/ (pt-PT)
- forms: dona [Gender=Fem, Number=Sing] /ˈdõ.nɐ/ /ˈdo.nɐ/; donas [Gender=Fem, Number=Plur] /ˈdõ.nɐs/ /ˈdo.nɐʃ/
  1. [A2] pessoa a quem uma coisa ou um animal pertence
     - example: O dono do restaurante veio cumprimentar todos os clientes.
     - frames: dono de algo
     - collocations: legítimo dono; verdadeiro dono; dono da casa; dono do negócio; dono do cão

### 17. limpar

**Lexeme 1: limpar**, VERB
- grammar: conjugation 1; irregular False; note two past participles: limpado with ter and haver, limpo with ser and estar
- said: /lĩ.ˈpaɾ/ (pt-BR), /lĩ.ˈpaɾ/ (pt-PT)
- forms: limpado [VerbForm=Part, Tense=Past] /lĩ.ˈpa.du/ /lĩ.ˈpa.du/ (with ter and haver); limpo [VerbForm=Part, Tense=Past] /ˈlĩ.pu/ /ˈlĩ.pu/ (with ser and estar; also used with ter in speech)
  1. [A1] tirar a sujeira, o pó ou as manchas de algo ou de alguém
     - example: Todos os sábados limpamos a casa juntos, da cozinha aos quartos.
     - frames: limpar algo/alguém; limpar algo com algo; limpar-se
     - collocations: limpar a casa; limpar o chão; limpar a mesa; limpar o pó; limpar o nariz; limpar as lágrimas; limpar os pés
     - grammar: valency Tr
     - opposites: sujar
  2. [B1] tirar de algo as partes que não servem ou que fazem mal
     - example: Antes de grelhar, é preciso limpar bem o peixe.
     - frames: limpar algo; limpar algo de algo
     - collocations: limpar o peixe; limpar o terreno; limpar o mato
     - grammar: valency Tr
  3. [B2] levar, gastar ou comer tudo o que havia num lugar ou recipiente
     - example: Os ladrões limparam a loja durante a madrugada.
     - frames: limpar algo
     - collocations: limpar o prato
     - labels: formality informal, figurative True
     - grammar: valency Tr
  4. [B2] deixar de ter nuvens, chuva ou nevoeiro
     - example: Se o tempo limpar, vamos à praia à tarde.
     - collocations: o céu limpou; o tempo limpou
     - grammar: valency Intr; note said of the sky or the weather
- relations: derived_from limpo (ADJ)

### 18. bagagem

**Lexeme 1: bagagem**, NOUN
- grammar: gender Fem; countability Both
- said: /ba.ˈɡa.ʒẽj/ (pt-BR), /bɐ.ˈɡa.ʒɐ̃j/ (pt-PT)
  1. [A2] malas e outros objetos que uma pessoa leva quando viaja
     - example: Não se esqueça de etiquetar a bagagem antes de ir para o aeroporto.
     - collocations: bagagem de mão; despachar a bagagem; excesso de bagagem; perder a bagagem; recolher a bagagem; franquia de bagagem
     - grammar: countability Both; note the singular often refers to all of a traveller's things; the plural is also common
  2. [C1] conjunto de conhecimentos e experiências que alguém acumulou ao longo da vida
     - example: Com a bagagem que tem, ela vai se sair bem no novo cargo.
     - collocations: bagagem cultural; bagagem intelectual; bagagem profissional
     - labels: figurative True
     - grammar: countability Mass

### 19. tratar

**Lexeme 1: tratar**, VERB
- grammar: conjugation 1; valency Both; irregular False
- said: /tɾa.ˈtaɾ/ (pt-BR), /tɾɐ.ˈtaɾ/ (pt-PT), /tɾa.ˈta/ (pt-BR, casual)
  1. [A2] comportar-se de certa maneira com uma pessoa, um animal ou uma coisa
     - example: Ela trata todos os clientes com muita gentileza.
     - frames: tratar alguém/algo; tratar alguém como alguém
     - collocations: tratar bem; tratar mal; tratar com respeito; tratar com carinho; tratar com desprezo
     - grammar: valency Tr
  2. [B1] ser na verdade aquilo de que se fala; ser a questão
     - example: Não se trata de dinheiro, trata-se de respeito.
     - frames: tratar-se de algo; tratar-se de + inf
     - grammar: valency Impers; reflexive True; note only in the third person with se: trata-se de, tratava-se de
  3. [B1] ocupar-se de um assunto, tarefa ou pessoa para resolver ou cuidar
     - example: Amanhã vou tratar dos documentos do carro.
     - frames: tratar de algo/alguém
     - collocations: tratar do assunto; tratar de negócios; tratar da papelada
     - grammar: valency Intr
  4. [B1] ter como assunto ou tema
     - example: O livro trata da vida dos pescadores do litoral.
     - frames: tratar de algo; tratar sobre algo
     - grammar: valency Intr
  5. [B1] dar cuidados médicos a uma pessoa ou combater uma doença
     - example: O médico tratou a infecção com antibióticos.
     - frames: tratar alguém/algo; tratar algo com algo
     - collocations: tratar uma doença; tratar o paciente; tratar uma ferida
     - grammar: valency Tr
  6. [B2] fazer algo sem demora ou com esforço, como uma obrigação
     - example: Trate de estudar, porque a prova é amanhã.
     - frames: tratar de + inf
     - grammar: valency Intr; note often in orders and warnings
  7. [B1] dirigir-se a alguém usando certa forma de cortesia ou título
     - example: Os alunos tratam a professora por senhora.
     - frames: tratar alguém por algo; tratar alguém de algo
     - collocations: tratar por tu; tratar por você
     - grammar: valency Tr
  8. [B2] falar ou negociar com alguém para resolver um assunto
     - example: Prefiro tratar diretamente com o dono da loja.
     - frames: tratar com alguém
     - grammar: valency Intr
  9. [B2] submeter algo a um processo para o limpar ou modificar
     - example: A cidade trata a água antes de a distribuir.
     - frames: tratar algo
     - collocations: tratar a água; tratar o esgoto; tratar o lixo
     - grammar: valency Tr

### 20. prol

**Lexeme 1: prol**, NOUN
- grammar: gender Masc; number Sing, mainly; countability Mass; note almost only in the phrase em prol de
- said: /ˈpɾɔw/ (pt-BR), /ˈpɾɔl/ (pt-PT)
  1. [B2] proveito ou vantagem que algo traz a alguém ou a uma causa
     - example: Os moradores organizaram uma festa em prol do hospital do bairro.
     - frames: em prol de algo/alguém
     - labels: formality formal

### 21. rural

**Lexeme 1: rural**, ADJ
- grammar: comparable False; note same form for masculine and feminine
- said: /ʁu.ˈɾaw/ (pt-BR), /ʁu.ˈɾal/ (pt-PT)
  1. [B1] do campo e da vida fora das cidades, ligado à agricultura
     - example: Muitos jovens deixam as zonas rurais para estudar na capital.
     - collocations: zona rural; área rural; meio rural; êxodo rural; produtor rural; turismo rural; população rural; escola rural
     - opposites: urbano

### 22. grau

**Lexeme 1: grau**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈɡɾaw/ (pt-BR), /ˈɡɾaw/ (pt-PT)
  1. [B1] nível de intensidade, quantidade ou importância que algo atinge
     - example: Esta prova tem um grau de dificuldade bastante elevado.
     - frames: grau de algo
     - collocations: em maior ou menor grau; em certo grau; grau de dificuldade; alto grau; grau elevado; queimadura de terceiro grau
  2. [A2] unidade usada para medir a temperatura ou a abertura de um ângulo
     - example: Amanhã a temperatura deve chegar aos trinta e cinco graus.
     - collocations: graus Celsius; graus negativos; ângulo de noventa graus; volta de cento e oitenta graus; abaixo de zero graus
  3. [B2] título que uma universidade dá a quem completa um curso
     - example: Ela obteve o grau de mestre com uma tese sobre o clima.
     - frames: grau de + título
     - collocations: grau académico; obter o grau; conferir o grau; colação de grau; grau de doutor
  4. [B2] distância que separa duas pessoas da mesma família na linha de parentesco
     - example: Só os parentes de primeiro grau podem visitar o doente.
     - collocations: grau de parentesco; parente de primeiro grau; primo em segundo grau; primeiro grau

### 23. pobre

**Lexeme 1: pobre**, ADJ
- grammar: note same form for masculine and feminine
- said: /ˈpɔ.bɾi/ (pt-BR), /ˈpɔ.bɾɨ/ (pt-PT)
- forms: paupérrimo [Degree=Abs, Gender=Masc, Number=Sing] /paw.ˈpɛ.ʁi.mu/ /paw.ˈpɛ.ʁi.mu/ (formal absolute superlative); pobríssimo [Degree=Abs, Gender=Masc, Number=Sing] /po.ˈbɾi.si.mu/ /pu.ˈbɾi.si.mu/
  1. [A1] que tem pouco dinheiro ou poucos bens para viver
     - example: Cresci numa família pobre, mas nunca nos faltou carinho.
     - collocations: país pobre; família pobre; bairro pobre; nascer pobre; ficar pobre
     - opposites: rico
  2. [B1] que contém pouca quantidade de alguma coisa necessária ou desejada
     - example: Este solo é pobre em nutrientes e quase nada cresce aqui.
     - frames: pobre em algo
     - collocations: solo pobre; dieta pobre; alimentação pobre
     - opposites: rico
  3. [A2] que desperta pena ou compaixão por estar numa situação difícil
     - example: A pobre senhora esperou horas sozinha na chuva.
     - collocations: pobre coitado; pobre de mim; pobre criança; pobre homem
     - grammar: position Attr; note before the noun; after the noun it usually means having little money
  4. [B2] de pouca qualidade, pouco desenvolvido ou de pouco valor
     - example: O texto tem boas ideias, mas o vocabulário é pobre.
     - collocations: vocabulário pobre; argumento pobre; desempenho pobre
**Lexeme 2: pobre**, NOUN
- grammar: gender Com; countability Count
- said: /ˈpɔ.bɾi/ (pt-BR), /ˈpɔ.bɾɨ/ (pt-PT)
  1. [A2] pessoa que tem pouco dinheiro ou poucos bens para viver
     - example: A associação distribui refeições quentes aos pobres da cidade.
     - collocations: ajudar os pobres; ricos e pobres
     - grammar: note often in the plural: os pobres
     - opposites: rico

### 24. prédio

**Lexeme 1: prédio**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈpɾɛ.dʒju/ (pt-BR), /ˈpɾɛ.dju/ (pt-PT)
  1. [A2] construção grande, geralmente de vários andares, para morar ou trabalhar
     - example: Moramos no quinto andar de um prédio sem elevador.
     - collocations: prédio de apartamentos; prédio de escritórios; prédio residencial; prédio alto; morar num prédio; construir um prédio; andar do prédio; porteiro do prédio
  2. [C1] propriedade imóvel, seja terreno ou construção
     - example: O imposto incide sobre cada prédio urbano registado em nome do proprietário.
     - collocations: prédio rústico; prédio urbano
     - labels: domain law

### 25. global

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

### 26. campeão

**Lexeme 1: campeão**, NOUN
- grammar: gender Masc; countability Count; note feminine campeã
- said: /kɐ̃.pi.ˈɐ̃w/ (pt-BR), /kɐ̃.ˈpjɐ̃w/ (pt-PT), /kɐ̃.ˈpjɐ̃w/ (pt-BR, casual)
- forms: campeões [Gender=Masc, Number=Plur] /kɐ̃.pi.ˈõjs/ /kɐ̃.ˈpjõjʃ/; campeã [Gender=Fem, Number=Sing] /kɐ̃.pi.ˈɐ̃/ /kɐ̃.ˈpjɐ̃/; campeãs [Gender=Fem, Number=Plur] /kɐ̃.pi.ˈɐ̃s/ /kɐ̃.ˈpjɐ̃ʃ/
  1. [A2] pessoa ou equipa que vence uma competição ou um torneio
     - example: Os campeões levantaram a taça diante de milhares de adeptos.
     - frames: campeão de algo
     - collocations: sagrar-se campeão; campeão do mundo; campeão mundial; campeão olímpico; campeão nacional; atual campeão; campeão invicto
  2. [B2] pessoa ou coisa que se destaca mais do que todas as outras em algo
     - example: Este livro foi o campeão de vendas do ano passado.
     - frames: campeão de algo
     - collocations: campeão de vendas; campeão de audiência
     - labels: figurative True
  3. [C1] pessoa que defende uma causa ou uma ideia com grande empenho
     - example: Ele sempre se apresentou como campeão da liberdade de imprensa.
     - frames: campeão de algo
     - labels: style literary
**Lexeme 2: campeão**, ADJ
- grammar: comparable False; note feminine campeã
- said: /kɐ̃.pi.ˈɐ̃w/ (pt-BR), /kɐ̃.ˈpjɐ̃w/ (pt-PT), /kɐ̃.ˈpjɐ̃w/ (pt-BR, casual)
- forms: campeões [Gender=Masc, Number=Plur] /kɐ̃.pi.ˈõjs/ /kɐ̃.ˈpjõjʃ/; campeã [Gender=Fem, Number=Sing] /kɐ̃.pi.ˈɐ̃/ /kɐ̃.ˈpjɐ̃/; campeãs [Gender=Fem, Number=Plur] /kɐ̃.pi.ˈɐ̃s/ /kɐ̃.ˈpjɐ̃ʃ/
  1. [B1] que venceu uma competição ou um torneio
     - example: A seleção campeã foi recebida em festa no aeroporto.
     - collocations: equipa campeã; time campeão; seleção campeã

### 27. impacto

**Lexeme 1: impacto**, NOUN
- grammar: gender Masc; countability Count
- said: /ĩ.ˈpak.tu/ (pt-BR), /ĩ.ˈpa.ki.tu/ (pt-BR, casual), /ĩ.ˈpak.tu/ (pt-PT)
  1. [B1] efeito forte que algo produz sobre pessoas, situações ou o ambiente
     - example: As novas regras vão ter um grande impacto na economia local.
     - frames: impacto em algo/alguém; impacto sobre algo/alguém; impacto de algo
     - collocations: ter impacto; causar impacto; impacto ambiental; impacto social; impacto positivo; impacto negativo; avaliar o impacto; reduzir o impacto
     - labels: figurative True
  2. [B2] choque violento de um corpo contra outro
     - example: Com o impacto, o vidro da janela partiu-se em pedaços.
     - frames: impacto contra algo; impacto com algo
     - collocations: ponto de impacto; força do impacto; sofrer o impacto; resistir ao impacto; no momento do impacto

### 28. rico

**Lexeme 1: rico**, ADJ
- grammar: comparable True
- said: /ˈʁi.ku/ (pt-BR), /ˈʁi.ku/ (pt-PT)
  1. [A1] que tem muito dinheiro ou muitos bens
     - example: Se eu ficasse rico, comprava uma casa perto do mar.
     - collocations: ficar rico; país rico; família rica; homem rico
     - opposites: pobre
  2. [B1] que tem grande quantidade ou variedade de algo bom ou útil
     - example: Esta fruta é rica em vitaminas e minerais.
     - frames: rico em algo
     - collocations: rico em vitaminas; rico em proteínas; vocabulário rico; experiência rica; alimentação rica
     - opposites: pobre
  3. [B2] feito com materiais caros e muitos enfeites, de grande luxo
     - example: A noiva usava um vestido rico, todo bordado com pérolas.
  4. [B1] muito bom ou agradável, sobretudo no sabor
     - example: Que rico bolo fizeste para o lanche!
     - labels: formality informal
     - regions: pt-PT mainly
  5. [B2] muito querido ou estimado por quem fala
     - example: Não mexas no meu rico carro sem pedir licença.
     - labels: formality informal
     - regions: pt-PT mainly
     - grammar: position Attr; note before the noun, usually after a possessive
**Lexeme 2: rico**, NOUN
- grammar: gender Masc; countability Count; number Plur, mainly; note feminine rica; mostly in the plural, os ricos
- said: /ˈʁi.ku/ (pt-BR), /ˈʁi.ku/ (pt-PT)
  1. [A2] pessoa que tem muito dinheiro ou muitos bens
     - example: Neste país, a distância entre ricos e pobres continua enorme.
     - opposites: pobre

### 29. atitude

**Lexeme 1: atitude**, NOUN
- grammar: gender Fem
- said: /a.tʃi.ˈtu.dʒi/ (pt-BR), /ɐ.ti.ˈtu.dɨ/ (pt-PT)
- spellings: atitude (pt-BR, pt-PT); attitude (pt-BR, pt-PT) [older books only]
  1. [B1] maneira de pensar e de se comportar perante uma pessoa ou situação
     - example: Admiro a tua atitude perante as dificuldades da vida.
     - frames: atitude perante algo/alguém; atitude em relação a algo/alguém; atitude com alguém
     - collocations: mudar de atitude; atitude positiva; atitude negativa; boa atitude; má atitude; atitude correta; adotar uma atitude
     - grammar: countability Count
  2. [B1] ação que alguém decide fazer para resolver uma situação
     - example: Se o barulho continuar, vamos ter de tomar uma atitude.
     - collocations: tomar uma atitude; atitude drástica; atitude firme
     - grammar: countability Count
  3. [B2] confiança e coragem para agir e mostrar o que se pensa
     - example: Essa cantora tem muita atitude em cima do palco.
     - collocations: ter atitude
     - labels: formality informal
     - regions: pt-BR mainly
     - grammar: countability Mass

### 30. policial

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

### 31. divisão

**Lexeme 1: divisão**, NOUN
- grammar: gender Fem
- said: /dʒi.vi.ˈzɐ̃w/ (pt-BR), /di.vi.ˈzɐ̃w/ (pt-PT), /dɨ.vi.ˈzɐ̃w/ (pt-PT)
- forms: divisões [Number=Plur] /dʒi.vi.ˈzõjs/ /di.vi.ˈzõjʃ/
  1. [B1] separação de um todo em partes ou repartição de algo entre várias pessoas
     - example: Em nossa casa, a divisão das tarefas é sempre justa.
     - frames: divisão de algo; divisão de algo em algo; divisão de algo entre alguém
     - collocations: divisão de tarefas; divisão de bens; divisão do trabalho; divisão de poderes; divisão celular; divisão justa
     - grammar: countability Both
  2. [B2] cada uma das partes em que se organiza uma empresa, um serviço ou um exército
     - example: Ela trabalha há cinco anos na divisão de vendas.
     - collocations: divisão de vendas; divisão comercial; divisão de infantaria; chefe de divisão
     - grammar: countability Count
  3. [B1] cada um dos níveis em que se agrupam os clubes de um campeonato
     - example: O clube da minha cidade voltou a jogar na primeira divisão.
     - collocations: primeira divisão; segunda divisão; subir de divisão; descer de divisão
     - grammar: countability Count
  4. [A2] operação matemática que calcula quantas vezes um número cabe em outro
     - example: As crianças estão aprendendo a fazer divisão na escola.
     - collocations: fazer uma divisão; conta de divisão; divisão exata; resto da divisão
     - grammar: countability Count
  5. [B1] cada um dos espaços de uma casa separados por paredes
     - example: Procuramos um apartamento com três divisões e uma varanda.
     - regions: pt-PT mainly
     - grammar: countability Count
  6. [B2] desacordo que separa pessoas ou grupos com opiniões diferentes
     - example: Essa decisão provocou uma profunda divisão dentro do partido.
     - collocations: divisão interna; profunda divisão; provocar divisões; criar divisão
     - grammar: countability Count
     - opposites: união
- relations: verbal_noun_of dividir (VERB); confusable cômodo (NOUN): a room of a house is usually 'divisão' in Portugal but 'cômodo' in Brazil

### 32. agradecer

**Lexeme 1: agradecer**, VERB
- grammar: conjugation 2; valency Tr; irregular False; note c becomes ç before a and o (agradeço, agradeça); stressed e is open in agradece, agradecem
- said: /a.ɡɾa.de.ˈseɾ/ (pt-BR), /ɐ.ɡɾɐ.dɨ.ˈseɾ/ (pt-PT), /a.ɡɾa.de.ˈse/ (pt-BR, casual)
- forms: agradeço [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /a.ɡɾa.ˈde.su/ /ɐ.ɡɾɐ.ˈde.su/; agradeces [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /a.ɡɾa.ˈdɛ.sis/ /ɐ.ɡɾɐ.ˈdɛ.sɨʃ/; agradece [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /a.ɡɾa.ˈdɛ.si/ /ɐ.ɡɾɐ.ˈdɛ.sɨ/; agradecemos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /a.ɡɾa.de.ˈse.mus/ /ɐ.ɡɾɐ.dɨ.ˈse.muʃ/; agradecem [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /a.ɡɾa.ˈdɛ.sẽj/ /ɐ.ɡɾɐ.ˈdɛ.sɐ̃j/; agradeci [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /a.ɡɾa.de.ˈsi/ /ɐ.ɡɾɐ.dɨ.ˈsi/; agradeceu [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /a.ɡɾa.de.ˈsew/ /ɐ.ɡɾɐ.dɨ.ˈsew/; agradeça [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /a.ɡɾa.ˈde.sɐ/ /ɐ.ɡɾɐ.ˈde.sɐ/; agradecido [VerbForm=Part, Tense=Past] /a.ɡɾa.de.ˈsi.du/ /ɐ.ɡɾɐ.dɨ.ˈsi.du/
  1. [A2] dizer ou mostrar a alguém que se está grato pelo que fez ou deu
     - example: Quero agradecer a todos pela ajuda durante a mudança.
     - frames: agradecer algo a alguém; agradecer a alguém por algo; agradecer por algo; agradecer a alguém
     - collocations: agradecer de coração; agradecer imensamente; agradecer profundamente; agradecer a atenção; agradecer a presença; agradecer o convite; agradecer a Deus

### 33. assembleia

**Lexeme 1: assembleia**, NOUN
- grammar: gender Fem; countability Count
- said: /a.sẽ.ˈblɛj.ɐ/ (pt-BR), /ɐ.sẽ.ˈblɐj.ɐ/ (pt-PT), /ɐ.sẽ.ˈblej.ɐ/ (pt-PT)
- spellings: assembleia (pt-BR, pt-PT); assembléia (pt-BR) [older books only]
  1. [B1] reunião de várias pessoas para discutir e decidir assuntos de interesse comum
     - example: Os moradores vão discutir as obras na próxima assembleia do prédio.
     - collocations: assembleia geral; convocar uma assembleia; realizar uma assembleia; assembleia de condomínio; assembleia de acionistas; assembleia extraordinária
  2. [B2] grupo de representantes eleitos que discute e aprova as leis
     - example: A proposta será votada pela assembleia no início do próximo mês.
     - collocations: assembleia legislativa; assembleia constituinte; assembleia municipal; Assembleia da República

### 34. aceitar

**Lexeme 1: aceitar**, VERB
- grammar: conjugation 1; valency Tr; irregular False; note two past participles: aceitado, mostly with ter; aceito (Brazil) or aceite (Portugal), mostly with ser and estar
- said: /a.sej.ˈtaɾ/ (pt-BR), /ɐ.sɐj.ˈtaɾ/ (pt-PT), /a.sej.ˈta/ (pt-BR, casual)
- forms: aceito [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /a.ˈsej.tu/ /ɐ.ˈsɐj.tu/; aceitas [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /a.ˈsej.tɐs/ /ɐ.ˈsɐj.tɐʃ/; aceita [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /a.ˈsej.tɐ/ /ɐ.ˈsɐj.tɐ/; aceitamos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /a.sej.ˈtɐ.mus/ /ɐ.sɐj.ˈtɐ.muʃ/; aceitam [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /a.ˈsej.tɐ̃w/ /ɐ.ˈsɐj.tɐ̃w/; aceitei [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /a.sej.ˈtej/ /ɐ.sɐj.ˈtɐj/; aceitou [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /a.sej.ˈto/ /ɐ.sɐj.ˈto/; aceite [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /a.ˈsej.tʃi/ /ɐ.ˈsɐj.tɨ/; aceitado [VerbForm=Part, Tense=Past] /a.sej.ˈta.du/ /ɐ.sɐj.ˈta.du/ (mostly with ter and haver); aceito [VerbForm=Part, Tense=Past] /a.ˈsej.tu/ /ɐ.ˈsɐj.tu/ (pt-BR); aceite [VerbForm=Part, Tense=Past] /a.ˈsej.tʃi/ /ɐ.ˈsɐj.tɨ/ (pt-PT)
  1. [A2] receber de bom grado o que é oferecido, proposto ou dado
     - example: Vou aceitar a proposta de emprego que recebi ontem.
     - frames: aceitar algo; aceitar + inf
     - collocations: aceitar um convite; aceitar uma proposta; aceitar um pedido de desculpas; aceitar um presente; aceitar um emprego; aceitar cartão; aceitar ajuda
     - opposites: recusar
  2. [B1] concordar com algo ou reconhecer que é verdadeiro ou correto
     - example: Todos aceitaram as condições do contrato sem discutir.
     - frames: aceitar algo; aceitar que + subj; aceitar que + ind
     - collocations: aceitar as regras; aceitar as condições; aceitar uma explicação; aceitar críticas; aceitar sugestões
     - opposites: rejeitar
  3. [B1] suportar sem revolta uma situação difícil ou que não se pode mudar
     - example: Demorou muito tempo a aceitar a morte do pai.
     - frames: aceitar algo; aceitar que + subj
     - collocations: aceitar a derrota; aceitar a realidade; aceitar o destino; aceitar a morte
  4. [B1] admitir alguém como membro, aluno ou participante
     - example: A escola só aceita alunos que vivam no bairro.
     - frames: aceitar alguém em algo; aceitar alguém como algo
     - grammar: note often in the passive: ser aceito/aceite em algo

### 35. álbum

**Lexeme 1: álbum**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈaw.bũ/ (pt-BR), /ˈal.bũ/ (pt-PT)
  1. [A2] livro com páginas próprias para guardar fotografias ou objetos de uma coleção
     - example: Encontrei na gaveta um álbum com fotos antigas dos meus avós.
     - collocations: álbum de fotografias; álbum de fotos; álbum de família; álbum de casamento; álbum de figurinhas; folhear um álbum
  2. [A2] conjunto de músicas gravadas e lançadas juntas por um artista
     - example: A banda vai lançar um álbum novo no próximo verão.
     - collocations: lançar um álbum; gravar um álbum; álbum de estreia; álbum ao vivo; álbum duplo; novo álbum

### 36. gênero

**Lexeme 1: gênero**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈʒẽ.ne.ɾu/ (pt-BR, gênero), /ˈʒɛ.nɨ.ɾu/ (pt-PT, género)
- spellings: gênero (pt-BR); género (pt-PT)
  1. [B1] grupo de coisas ou seres que têm características em comum
     - example: Não gosto desse gênero de comentário durante as reuniões.
     - frames: gênero de algo
     - collocations: coisas do gênero; de todo gênero; único no gênero
  2. [B1] categoria de obras artísticas com forma, estilo ou tema em comum
     - example: O romance policial é um gênero que conquista muitos leitores.
     - collocations: gênero literário; gênero musical; gênero cinematográfico
  3. [B1] categoria gramatical que distingue palavras masculinas, femininas ou neutras
     - example: Em português, os adjetivos concordam em gênero e número com o substantivo.
     - collocations: gênero masculino; gênero feminino; concordar em gênero e número
     - labels: domain grammar
  4. [B2] condição de homem, mulher ou outra identidade, vista do ponto de vista social
     - example: A empresa quer acabar com a desigualdade de gênero nos salários.
     - collocations: igualdade de gênero; identidade de gênero; violência de gênero; desigualdade de gênero; questões de gênero
  5. [C1] grupo de espécies próximas na classificação dos seres vivos
     - example: O lobo e o cão pertencem ao mesmo gênero de mamíferos.
     - labels: domain biology
  6. [B2] produtos, sobretudo alimentos, que se compram e se vendem
     - example: Os preços dos gêneros alimentícios voltaram a subir este mês.
     - collocations: gêneros alimentícios; gêneros de primeira necessidade
     - grammar: number Plur

### 37. quadro

**Lexeme 1: quadro**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈkwa.dɾu/ (pt-BR), /ˈkwa.dɾu/ (pt-PT)
  1. [A2] obra de pintura ou desenho, geralmente pendurada na parede
     - example: Na sala há um quadro enorme com uma paisagem de montanhas.
     - collocations: pintar um quadro; pendurar um quadro; quadro a óleo; exposição de quadros
  2. [A2] superfície lisa presa à parede onde se escreve com giz ou caneta
     - example: A professora escreveu as datas importantes no quadro.
     - collocations: escrever no quadro; apagar o quadro; quadro branco; ir ao quadro
  3. [B2] conjunto de condições e circunstâncias em que algo se encontra ou acontece
     - example: O médico disse que o quadro do paciente melhorou bastante.
     - frames: no quadro de algo
     - collocations: quadro clínico; quadro político; traçar um quadro; agravamento do quadro
  4. [B2] conjunto das pessoas que trabalham de forma fixa numa organização
     - example: A empresa vai contratar mais engenheiros para o seu quadro.
     - frames: quadro de algo
     - collocations: quadro de funcionários; quadro de pessoal; fazer parte do quadro; quadro efetivo
  5. [B1] tabela ou esquema que organiza informações em linhas e colunas
     - example: Consultem o quadro da página seguinte para comparar os preços.
     - collocations: quadro comparativo; quadro-resumo
  6. [B2] parte fixa de um programa de televisão ou rádio, com tema próprio
     - example: O programa de domingo estreou um quadro de humor muito engraçado.
     - collocations: quadro de humor
     - regions: pt-BR mainly
  7. [C1] estrutura rígida que forma o corpo de uma bicicleta ou moto
     - example: Esta bicicleta tem um quadro de alumínio muito leve.
     - collocations: quadro de alumínio; quadro de carbono

### 38. localização

**Lexeme 1: localização**, NOUN
- grammar: gender Fem; countability Both
- said: /lo.ka.li.za.ˈsɐ̃w/ (pt-BR), /lu.kɐ.li.zɐ.ˈsɐ̃w/ (pt-PT)
  1. [B1] lugar exato onde algo ou alguém se encontra
     - example: A localização do hotel é ótima, mesmo ao lado da praia.
     - frames: localização de algo/alguém
     - collocations: boa localização; localização privilegiada; localização geográfica; localização exata
     - grammar: countability Count
  2. [B2] ação de descobrir ou determinar onde algo ou alguém está
     - example: Os bombeiros continuam a trabalhar na localização dos desaparecidos.
     - frames: localização de algo/alguém
     - collocations: sistema de localização; serviço de localização
     - grammar: countability Mass
- relations: verbal_noun_of localizar (VERB)

### 39. programação

**Lexeme 1: programação**, NOUN
- grammar: gender Fem; countability Both
- said: /pɾo.ɡɾa.ma.ˈsɐ̃w/ (pt-BR), /pɾu.ɡɾɐ.mɐ.ˈsɐ̃w/ (pt-PT)
  1. [B1] escrita de instruções em código para um computador ou máquina funcionar sozinho
     - example: Aprender programação abre muitas portas no mercado de trabalho.
     - collocations: linguagem de programação; aprender programação; curso de programação; erro de programação
     - grammar: countability Mass
  2. [B1] conjunto das emissões ou atividades previstas por um canal ou evento
     - example: A programação da televisão melhora bastante ao fim de semana.
     - frames: programação de algo
     - collocations: programação cultural; programação infantil; grade de programação; fazer parte da programação; programação do festival
     - grammar: countability Count
  3. [B2] organização prévia do que se vai fazer e de quando
     - example: Fizemos a programação da viagem com dois meses de antecedência.
     - frames: programação de algo
     - collocations: fazer a programação
- relations: verbal_noun_of programar (VERB)

### 40. quente

**Lexeme 1: quente**, ADJ
- grammar: note same form for masculine and feminine
- said: /ˈkẽ.tʃi/ (pt-BR), /ˈkẽ.tɨ/ (pt-PT)
  1. [A1] que tem temperatura alta ou mais alta do que o normal
     - example: Cuidado, a sopa ainda está muito quente.
     - collocations: água quente; banho quente; tempo quente; clima quente; dia quente; café quente; comida quente
     - opposites: frio
  2. [A2] que protege do frio e conserva o calor do corpo
     - example: Leva um casaco quente, porque à noite faz frio.
     - collocations: roupa quente; casaco quente; cobertor quente
     - opposites: frio
  3. [B2] cheio de tensão, emoção ou energia; muito animado ou intenso
     - example: A reunião ficou quente quando começaram a falar de salários.
     - collocations: discussão quente; debate quente
     - labels: figurative True
  4. [B2] muito atual e que desperta grande interesse no momento
     - example: Esse é o assunto mais quente da semana nas redes sociais.
     - collocations: notícia quente; assunto quente; tema quente
     - labels: figurative True
  5. [B2] que lembra o fogo e transmite sensação de calor
     - example: Pintamos a sala com cores quentes para ficar mais acolhedora.
     - collocations: cor quente; tom quente; luz quente
     - grammar: position Attr, mainly
     - opposites: frio

### 41. orgulho

**Lexeme 1: orgulho**, NOUN
- grammar: gender Masc
- said: /oɾ.ˈɡu.ʎu/ (pt-BR), /ɔɾ.ˈɡu.ʎu/ (pt-PT)
  1. [B1] satisfação e alegria pelo valor do que se fez ou do que se tem
     - example: Os pais olhavam para a filha com muito orgulho.
     - frames: orgulho de algo/alguém; orgulho de + inf; orgulho em + inf
     - collocations: ter orgulho; sentir orgulho; motivo de orgulho; encher de orgulho; com orgulho; orgulho nacional
     - grammar: countability Mass
     - opposites: vergonha
  2. [B2] sentimento do próprio valor e da própria dignidade
     - example: Ele não pediu ajuda porque o orgulho não deixou.
     - collocations: ferir o orgulho; engolir o orgulho; orgulho ferido; pôr o orgulho de lado
     - grammar: countability Mass
  3. [B2] ideia exagerada do próprio valor, que leva a desprezar os outros
     - example: O orgulho dela acabou afastando todos os amigos.
     - collocations: cheio de orgulho
     - labels: attitude disapproving
     - grammar: countability Mass
     - opposites: humildade
  4. [B2] pessoa ou coisa de que alguém tem grande satisfação
     - example: Este jardim é o orgulho do meu avô.
     - frames: o orgulho de alguém/algo
     - grammar: countability Count; number Sing

### 42. pretenso

**Lexeme 1: pretenso**, ADJ
- grammar: position Attr; comparable False; note usually placed before the noun
- said: /pɾe.ˈtẽ.su/ (pt-BR), /pɾɨ.ˈtẽ.su/ (pt-PT)
  1. [C1] que se supõe ou se diz ser, sem que isso esteja provado
     - example: O pretenso especialista não soube responder às perguntas mais simples.
     - collocations: pretenso especialista; pretensa vítima; pretenso autor
     - labels: formality formal
- relations: confusable pretensioso (ADJ): pretenso means 'supposed, alleged', while pretensioso means 'pretentious'

### 43. live

**Lexeme 1: live**, NOUN
- grammar: gender Fem; countability Count; note loanword from English; sometimes masculine
- said: /ˈlaj.vi/ (pt-BR), /ˈlaj.vɨ/ (pt-PT)
- forms: lives [Number=Plur] /ˈlaj.vis/ /ˈlaj.vɨʃ/
  1. [B1] transmissão de vídeo ao vivo pela internet, ou a gravação dela
     - example: Hoje à noite vou fazer uma live para responder às perguntas.
     - collocations: fazer uma live; assistir à live; entrar na live; live solidária
     - regions: pt-BR mainly

### 44. audiência

**Lexeme 1: audiência**, NOUN
- grammar: gender Fem
- said: /aw.dʒi.ˈẽ.sjɐ/ (pt-BR), /aw.ˈdʒjẽ.sjɐ/ (pt-BR, casual), /aw.ˈdjẽ.sjɐ/ (pt-PT)
  1. [B1] conjunto das pessoas que assistem a um espetáculo ou acompanham um programa
     - example: O programa de domingo bateu recorde de audiência este mês.
     - collocations: índice de audiência; recorde de audiência; líder de audiência; horário de maior audiência; conquistar audiência; perder audiência; grande audiência
     - grammar: countability Both
  2. [B2] sessão em que um juiz ouve as partes de um processo
     - example: A audiência foi adiada porque a testemunha não compareceu.
     - collocations: marcar uma audiência; adiar a audiência; audiência pública; audiência de conciliação; audiência de julgamento
     - labels: domain law
     - grammar: countability Count
  3. [C1] encontro formal em que uma autoridade recebe alguém para o escutar
     - example: Vamos pedir uma audiência ao ministro para expor o problema.
     - frames: audiência com alguém
     - collocations: pedir uma audiência; conceder uma audiência; receber em audiência; audiência privada
     - grammar: countability Count

### 45. independência

**Lexeme 1: independência**, NOUN
- grammar: gender Fem; countability Mass
- said: /ĩ.de.pẽ.ˈdẽ.sjɐ/ (pt-BR), /ĩ.dɨ.pẽ.ˈdẽ.sjɐ/ (pt-PT)
  1. [B1] liberdade de um país ou povo para se governar sem domínio estrangeiro
     - example: O país conquistou a independência depois de uma longa guerra.
     - frames: independência de algo
     - collocations: proclamar a independência; declarar a independência; conquistar a independência; guerra da independência; dia da independência
     - labels: domain politics
  2. [B1] capacidade de viver, agir e decidir sem precisar de outros nem ser controlado
     - example: Com o primeiro emprego, ganhei mais independência em relação aos meus pais.
     - frames: independência de alguém/algo; independência em relação a alguém/algo
     - collocations: independência financeira; ganhar independência; perder a independência; independência económica
     - opposites: dependência
- relations: abstract_noun_of independente (ADJ)
