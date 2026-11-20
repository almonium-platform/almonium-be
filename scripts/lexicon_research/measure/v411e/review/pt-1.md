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
  1. [A2] trabalho de cultivar a terra para produzir alimentos e outros produtos
     - example: Grande parte da população da região vive da agricultura.
     - collocations: viver da agricultura; dedicar-se à agricultura; agricultura familiar; agricultura de subsistência; agricultura intensiva; agricultura biológica; agricultura orgânica; ministério da agricultura

### 3. competição

**Lexeme 1: competição**, NOUN
- grammar: gender Fem; countability Both
- said: /kõ.pe.tʃi.ˈsɐ̃w/ (pt-BR), /kõ.pɨ.ti.ˈsɐ̃w/ (pt-PT)
  1. [B1] prova ou evento organizado em que os participantes disputam um prémio ou título
     - example: A nossa equipa vai participar numa competição internacional de natação.
     - collocations: participar de uma competição; participar numa competição; ganhar uma competição; vencer a competição; competição esportiva; competição desportiva; competição internacional; fora de competição
     - grammar: countability Count
  2. [B1] disputa entre pessoas ou grupos que querem obter a mesma coisa
     - example: Existe muita competição entre as empresas deste setor.
     - frames: competição entre alguém e alguém; competição por algo; competição com alguém
     - collocations: competição acirrada; competição feroz; forte competição; competição saudável; competição desleal; em competição
     - grammar: countability Mass, mainly
     - opposites: cooperação
- relations: verbal_noun_of competir (VERB)

### 4. canto

**Lexeme 1: canto**, NOUN
- grammar: gender Masc
- said: /ˈkɐ̃.tu/ (pt-BR), /ˈkɐ̃.tu/ (pt-PT)
  1. [A2] lugar onde se encontram duas paredes, dois lados ou duas linhas
     - example: Põe o vaso grande naquele canto, perto da janela.
     - frames: canto de algo
     - collocations: canto do olho; canto da boca; canto da sala; olhar de canto; no canto; ficar num canto
     - grammar: countability Count
  2. [B1] arte de produzir sons musicais com a voz
     - example: Ela tem aulas de canto duas vezes por semana.
     - collocations: aulas de canto; canto lírico; canto coral; canto gregoriano; professor de canto
     - grammar: countability Mass, mainly
  3. [B1] som melodioso que fazem as aves e alguns outros animais
     - example: Acordamos todos os dias com o canto dos pássaros.
     - collocations: canto dos pássaros; canto do galo; canto das cigarras
     - grammar: countability Count
  4. [B2] lugar pequeno, afastado ou que não se sabe bem qual é
     - example: Ele foi viver num canto esquecido do interior.
     - collocations: por todos os cantos; em todos os cantos; num canto qualquer; canto do mundo
     - grammar: countability Count
  5. [B2] pontapé dado do ângulo do campo depois de a bola sair pela linha de fundo
     - example: O golo nasceu de um canto batido do lado direito.
     - collocations: marcar um canto; bater um canto; pontapé de canto
     - regions: pt-PT mainly
     - grammar: countability Count
  6. [C1] cada uma das grandes partes em que se divide um poema longo
     - example: O poeta dividiu a sua epopeia em dez cantos.
     - labels: domain literature
     - grammar: countability Count

### 5. perigo

**Lexeme 1: perigo**, NOUN
- grammar: gender Masc; countability Both
- said: /pe.ˈɾi.ɡu/ (pt-BR), /pɨ.ˈɾi.ɡu/ (pt-PT), /pi.ˈɾi.ɡu/ (pt-BR), /ˈpɾi.ɡu/ (pt-PT, casual)
  1. [A2] situação em que alguém ou algo pode sofrer um mal ou dano
     - example: Os médicos dizem que o paciente já está fora de perigo.
     - frames: perigo de algo; perigo de + inf
     - collocations: correr perigo; pôr em perigo; colocar em perigo; em perigo; fora de perigo; perigo de vida; perigo iminente; enfrentar o perigo
     - grammar: countability Mass, mainly
     - opposites: segurança
  2. [B1] coisa ou pessoa que pode causar mal ou dano
     - example: Conduzir depois de beber é um perigo para todos na estrada.
     - frames: perigo para algo/alguém
     - collocations: ser um perigo; perigo público; os perigos da estrada
     - grammar: countability Count

### 6. recentemente

**Lexeme 1: recentemente**, ADV
- said: /ʁe.ˌsẽ.tʃi.ˈmẽ.tʃi/ (pt-BR), /ʁɨ.ˌsẽ.tɨ.ˈmẽ.tɨ/ (pt-PT)
  1. [B1] num momento ainda próximo do presente, não muito tempo atrás
     - example: Mudei-me para esta cidade recentemente e ainda conheço pouca gente.
     - collocations: até recentemente; mais recentemente; só recentemente
- relations: adverb_of recente (ADJ)

### 7. concurso

**Lexeme 1: concurso**, NOUN
- grammar: gender Masc
- said: /kõ.ˈkuɾ.su/ (pt-BR), /kõ.ˈkuʁ.su/ (pt-BR, regional), /kõ.ˈkuɾ.su/ (pt-PT)
  1. [A2] competição em que várias pessoas disputam uma vitória ou uma recompensa
     - example: A minha irmã ganhou um concurso de fotografia organizado pela escola.
     - frames: concurso de algo
     - collocations: concurso de beleza; concurso de talentos; ganhar um concurso; vencer um concurso; participar de um concurso; participar num concurso; inscrever-se num concurso
     - grammar: countability Count
  2. [B1] processo de seleção, com provas, para escolher quem vai ocupar um cargo
     - example: Ele estuda todos os dias para passar no concurso público.
     - frames: concurso para algo
     - collocations: concurso público; passar no concurso; prestar concurso; fazer concurso; abrir concurso; edital do concurso
     - grammar: countability Count
  3. [C1] ajuda ou participação de alguém para chegar a um resultado
     - example: A ponte foi construída com o concurso de vários engenheiros estrangeiros.
     - frames: com o concurso de alguém/algo
     - labels: formality formal
     - grammar: countability Mass

### 8. vender

**Lexeme 1: vender**, VERB
- grammar: conjugation 2; irregular False
- said: /vẽ.ˈdeɾ/ (pt-BR), /vẽ.ˈdeɾ/ (pt-PT), /vẽ.ˈde/ (pt-BR, casual)
  1. [A1] passar algo a outra pessoa em troca de dinheiro
     - example: Vamos vender o carro antigo e comprar um mais pequeno.
     - frames: vender algo a alguém; vender algo para alguém; vender algo por algo
     - collocations: vender fiado; vender a prazo; vender à vista; vender caro; vender barato; vender bem
     - grammar: valency Tr
     - opposites: comprar
  2. [B2] convencer outras pessoas a aceitar ou valorizar uma ideia, imagem ou projeto
     - example: Ela soube vender muito bem o projeto aos investidores.
     - frames: vender algo a alguém
     - collocations: vender uma ideia; vender uma imagem
     - labels: figurative True
     - grammar: valency Tr
  3. [C1] aceitar dinheiro ou favores para agir contra os próprios princípios
     - example: Aquele jornalista nunca se vendeu, apesar de todas as ofertas.
     - labels: attitude disapproving
     - grammar: valency Refl

### 9. estrela

**Lexeme 1: estrela**, NOUN
- grammar: gender Fem; countability Count
- said: /is.ˈtɾe.lɐ/ (pt-BR), /iʃ.ˈtɾe.lɐ/ (pt-PT), /es.ˈtɾe.lɐ/ (pt-BR, regional), /ˈʃtɾe.lɐ/ (pt-PT, casual)
  1. [A1] astro que tem luz própria e que, à noite, parece um ponto brilhante no céu
     - example: Longe da cidade, conseguimos ver milhares de estrelas no céu.
     - collocations: estrela cadente; estrela polar; céu cheio de estrelas; as estrelas brilham; à luz das estrelas
  2. [A2] figura com várias pontas que saem todas de um mesmo centro
     - example: As crianças recortaram estrelas de papel para enfeitar a sala.
     - collocations: estrela de cinco pontas; em forma de estrela; hotel de cinco estrelas
  3. [B1] pessoa muito famosa ou que mais se destaca num grupo pelo seu talento
     - example: Depois do primeiro filme, ela tornou-se uma verdadeira estrela.
     - frames: estrela de algo
     - collocations: estrela de cinema; estrela do rock; estrela da equipe; estrela pop; virar estrela
  4. [C1] sorte ou destino que se acredita guiar a vida de alguém
     - example: Ele sempre teve boa estrela e saiu ileso do acidente.
     - collocations: ter boa estrela; nascer com boa estrela; nascer com estrela
     - labels: figurative True

### 10. precisar

**Lexeme 1: precisar**, VERB
- grammar: conjugation 1; irregular False; valency Both
- said: /pɾe.si.ˈzaɾ/ (pt-BR), /pɾɨ.si.ˈzaɾ/ (pt-PT), /pɾi.si.ˈzaɾ/ (pt-BR, regional), /pɾe.si.ˈza/ (pt-BR, casual)
  1. [A1] ter necessidade de algo ou de alguém
     - example: Preciso de um carro maior para levar a família toda.
     - frames: precisar de algo/alguém
     - collocations: precisar de ajuda; precisar muito; precisar urgentemente; se precisar
     - grammar: valency Intr; note often used without an object: ajudar quem precisa
  2. [A1] ter a obrigação ou a necessidade de fazer algo
     - example: Amanhã você precisa acordar cedo para o exame.
     - frames: precisar + inf; precisar de + inf; precisar que + subj
     - collocations: não precisar
     - grammar: valency Tr; note before an infinitive, Brazil usually drops de; Portugal often keeps it
  3. [C1] dizer ou indicar algo com exatidão e rigor
     - example: A testemunha não soube precisar a hora do acidente.
     - frames: precisar algo
     - collocations: precisar a data; precisar o momento
     - labels: formality formal
     - grammar: valency Tr
- relations: derived_from preciso (ADJ)

### 11. crescer

**Lexeme 1: crescer**, VERB
- grammar: conjugation 2; valency Intr; irregular False; note c becomes ç before a and o (cresço, cresça); stressed e is open in cresces, cresce, crescem
- said: /kɾe.ˈseɾ/ (pt-BR), /kɾɨʃ.ˈseɾ/ (pt-PT), /kɾe.ˈse/ (pt-BR, casual)
- forms: cresço [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈkɾe.su/ /ˈkɾeʃ.su/; cresces [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /ˈkɾɛ.sis/ /ˈkɾɛʃ.sɨʃ/; cresce [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈkɾɛ.si/ /ˈkɾɛʃ.sɨ/; crescemos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /kɾe.ˈse.mus/ /kɾɨʃ.ˈse.muʃ/; crescem [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈkɾɛ.sẽj/ /ˈkɾɛʃ.sɐ̃j/; cresci [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /kɾe.ˈsi/ /kɾɨʃ.ˈsi/; cresceu [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /kɾe.ˈsew/ /kɾɨʃ.ˈsew/; cresça [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈkɾe.sɐ/ /ˈkɾeʃ.sɐ/; crescido [VerbForm=Part, Tense=Past] /kɾe.ˈsi.du/ /kɾɨʃ.ˈsi.du/
  1. [A2] ficar maior ou mais alto, desenvolvendo-se de forma natural
     - example: As plantas ficam maiores mais depressa quando crescem ao sol.
     - collocations: deixar crescer; crescer rápido; crescer depressa
  2. [A2] passar de criança a adulto; ir ficando mais velho
     - example: Quando eu crescer, quero ser médica como a minha mãe.
     - frames: crescer em + lugar
     - collocations: quando crescer
  3. [B1] aumentar em número, quantidade, valor ou intensidade
     - example: O número de turistas cresceu muito nos últimos anos.
     - frames: crescer em algo
     - collocations: crescer muito; a economia cresce; crescer a um ritmo; crescer em número
     - opposites: diminuir
  4. [B2] progredir e ganhar importância como pessoa ou na carreira
     - example: Ela cresceu muito como profissional desde que mudou de empresa.
     - collocations: crescer na vida; crescer como pessoa; crescer profissionalmente

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
  1. [B1] demonstração de respeito, admiração ou gratidão por alguém ou algo
     - example: Os alunos prestaram uma homenagem emocionante à professora que se aposentou.
     - frames: homenagem a alguém/algo; em homenagem a alguém/algo
     - collocations: prestar homenagem; render homenagem; fazer uma homenagem; receber uma homenagem; homenagem póstuma; justa homenagem; uma singela homenagem

### 15. sentimento

**Lexeme 1: sentimento**, NOUN
- grammar: gender Masc; countability Both
- said: /sẽ.tʃi.ˈmẽ.tu/ (pt-BR), /sẽ.ti.ˈmẽ.tu/ (pt-PT)
  1. [A2] estado afetivo ou emoção que uma pessoa experimenta perante algo ou alguém
     - example: Ele tem dificuldade em mostrar os seus sentimentos aos outros.
     - frames: sentimento de algo; sentimento por alguém
     - collocations: ferir os sentimentos; magoar os sentimentos; sentimento de culpa; expressar sentimentos; esconder os sentimentos; sentimentos contraditórios; sentimento profundo
     - grammar: countability Count
  2. [B2] impressão ou opinião que muitas pessoas partilham sobre uma situação
     - example: O sentimento geral na empresa é de que tudo vai melhorar.
     - frames: sentimento de que + ind
     - collocations: sentimento geral; sentimento de insegurança
     - grammar: countability Count
  3. [B2] emoção e sensibilidade com que alguém faz ou exprime algo
     - example: A pianista tocou aquela peça antiga com muito sentimento.
     - collocations: com sentimento
     - grammar: countability Mass

### 16. dono

**Lexeme 1: dono**, NOUN
- grammar: gender Masc; countability Count; note feminine dona, plural donos
- said: /ˈdõ.nu/ (pt-BR), /ˈdo.nu/ (pt-PT)
  1. [A2] pessoa a quem uma coisa ou um animal pertence
     - example: O dono do restaurante veio cumprimentar todos os clientes.
     - frames: dono de algo
     - collocations: dono da casa; legítimo dono; mudar de dono; sem dono; dono do cachorro; dono da empresa
  2. [B2] pessoa que controla ou domina uma situação ou a si mesma
     - example: Ele age como se fosse o dono da verdade.
     - frames: dono de algo
     - collocations: dono da situação; dono da verdade; dono do próprio nariz; dono de si
     - labels: figurative True

### 17. limpar

**Lexeme 1: limpar**, VERB
- grammar: conjugation 1; irregular False; note two past participles: limpado mainly with ter and haver, limpo mainly with ser and estar
- said: /lĩ.ˈpaɾ/ (pt-BR), /lĩ.ˈpaɾ/ (pt-PT), /lĩ.ˈpa/ (pt-BR, casual)
- forms: limpo [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈlĩ.pu/ /ˈlĩ.pu/; limpas [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /ˈlĩ.pɐs/ /ˈlĩ.pɐʃ/; limpa [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /ˈlĩ.pɐ/ /ˈlĩ.pɐ/; limpamos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /lĩ.ˈpɐ.mus/ /lĩ.ˈpɐ.muʃ/; limpam [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /ˈlĩ.pɐ̃w/ /ˈlĩ.pɐ̃w/; limpei [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /lĩ.ˈpej/ /lĩ.ˈpɐj/; limpou [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /lĩ.ˈpow/ /lĩ.ˈpo/; limpe [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /ˈlĩ.pi/ /ˈlĩ.pɨ/; limpado [VerbForm=Part, Tense=Past] /lĩ.ˈpa.du/ /lĩ.ˈpa.du/ (mainly with ter and haver); limpo [VerbForm=Part, Tense=Past] /ˈlĩ.pu/ /ˈlĩ.pu/ (mainly with ser and estar)
  1. [A1] tirar sujeira, pó ou líquido de um lugar, objeto ou parte do corpo
     - example: Vou limpar a cozinha depois do jantar, antes de deitar.
     - frames: limpar algo; limpar algo com algo; limpar-se
     - collocations: limpar a casa; limpar o chão; limpar o pó; limpar a mesa; limpar o nariz; limpar as lágrimas; limpar o suor; limpar a boca
     - grammar: valency Tr
     - opposites: sujar
  2. [B2] tirar de um peixe, ave ou legume as partes que não se comem
     - example: Antes de grelhar o peixe, é preciso limpá-lo bem.
     - frames: limpar algo
     - collocations: limpar o peixe; limpar o frango; limpar o feijão
     - grammar: valency Tr
  3. [B2] livrar de pessoas, coisas ou culpas indesejadas ou vergonhosas
     - example: Ele passou anos tentando limpar o nome da família.
     - frames: limpar algo de algo/alguém
     - collocations: limpar o nome; limpar a imagem
     - labels: figurative True
     - grammar: valency Tr
  4. [B2] levar todo o dinheiro ou todos os bens de um lugar ou de alguém
     - example: Os ladrões limparam o apartamento enquanto estávamos de férias.
     - frames: limpar algo/alguém
     - labels: formality informal
     - grammar: valency Tr
  5. [B2] deixar de estar coberto de nuvens, chuva ou nevoeiro
     - example: Depois da tempestade, o céu limpou e o sol apareceu.
     - collocations: o céu limpou; o tempo limpou
     - grammar: valency Intr; note said of the sky or the weather
- relations: derived_from limpo (ADJ)

### 18. bagagem

**Lexeme 1: bagagem**, NOUN
- grammar: gender Fem; countability Mass, mainly; note the plural bagagens is often used for several bags
- said: /ba.ˈɡa.ʒẽj/ (pt-BR), /bɐ.ˈɡa.ʒɐ̃j/ (pt-PT)
  1. [A2] malas e outros objetos que uma pessoa leva consigo numa viagem
     - example: Deixe a bagagem no carro enquanto procuramos o hotel.
     - collocations: bagagem de mão; despachar a bagagem; excesso de bagagem; bagagem extraviada; recolha de bagagem; esteira de bagagem; carrinho de bagagem; fazer a bagagem
  2. [C1] conjunto de conhecimentos e experiências que alguém acumulou ao longo da vida
     - example: Ela chegou à empresa com uma enorme bagagem profissional.
     - collocations: bagagem cultural; bagagem intelectual; bagagem profissional; bagagem teórica
     - labels: figurative True

### 19. tratar

**Lexeme 1: tratar**, VERB
- grammar: conjugation 1; valency Both; irregular False
- said: /tɾa.ˈtaɾ/ (pt-BR), /tɾɐ.ˈtaɾ/ (pt-PT), /tɾa.ˈta/ (pt-BR, casual)
  1. [A2] agir de certa maneira com uma pessoa, um animal ou uma coisa
     - example: Ela sempre trata os vizinhos com muita simpatia.
     - frames: tratar alguém/algo + adv; tratar alguém como algo/alguém; tratar alguém com algo
     - collocations: tratar bem; tratar mal; tratar com respeito; tratar com carinho; tratar com desprezo
     - grammar: valency Tr
  2. [B1] ter como assunto ou tema
     - example: Este livro trata da vida dos pescadores no século passado.
     - frames: tratar de algo; tratar sobre algo
     - grammar: valency Intr
  3. [B1] ocupar-se de um assunto e fazer o necessário para o resolver
     - example: Amanhã vou tratar dos documentos para a viagem.
     - frames: tratar de algo; tratar algo
     - collocations: tratar do assunto; tratar de negócios; tratar da papelada
  4. [B2] ser exatamente aquilo de que se fala; ser o caso em questão
     - example: Não se trata de dinheiro, mas de respeito.
     - frames: tratar-se de algo/alguém; tratar-se de + inf
     - grammar: valency Impers; reflexive True; note only in the third person singular with se: trata-se de
  5. [B1] cuidar de um doente ou de uma doença para conseguir a cura
     - example: O médico tratou a ferida com um creme especial.
     - frames: tratar algo/alguém; tratar algo com algo; tratar de alguém
     - collocations: tratar uma doença; tratar um paciente; tratar uma ferida
     - grammar: valency Tr, mainly
  6. [B1] usar uma certa forma de dirigir-se a uma pessoa ao falar com ela
     - example: Os alunos tratam a professora por senhora.
     - frames: tratar alguém por algo; tratar alguém de algo
     - collocations: tratar por tu; tratar por você
     - grammar: valency Tr
  7. [B2] esforçar-se por fazer algo, muitas vezes sem demora
     - example: Trata de arrumar o teu quarto antes do jantar.
     - frames: tratar de + inf
     - grammar: valency Intr; note often in commands, urging someone to act
  8. [B2] combinar ou negociar as condições de algo com outra pessoa
     - example: Ainda temos de tratar o preço com o dono da casa.
     - frames: tratar algo com alguém; tratar com alguém
  9. [C1] submeter uma substância a um processo para a limpar ou modificar
     - example: A cidade trata a água antes de a distribuir pelas casas.
     - collocations: tratar a água; tratar o esgoto; tratar o lixo; tratar a madeira
     - grammar: valency Tr

### 20. prol

**Lexeme 1: prol**, NOUN
- grammar: gender Masc; countability Mass; note almost always in the phrase em prol de
- said: /ˈpɾɔw/ (pt-BR), /ˈpɾɔl/ (pt-PT)
- forms: próis [Number=Plur] /ˈpɾɔjs/ /ˈpɾɔjʃ/
  1. [B2] benefício ou vantagem que algo traz a uma pessoa ou causa
     - example: Os moradores organizaram uma festa em prol da escola do bairro.
     - frames: em prol de algo/alguém

### 21. rural

**Lexeme 1: rural**, ADJ
- grammar: note same form for masculine and feminine; plural rurais
- said: /ʁu.ˈɾaw/ (pt-BR), /ʁu.ˈɾal/ (pt-PT)
  1. [B1] do campo e da vida fora das cidades, ligada à agricultura
     - example: Muitas famílias trocaram a vida rural pelo trabalho na cidade.
     - collocations: zona rural; área rural; meio rural; êxodo rural; produtor rural; turismo rural; população rural; escola rural; vida rural
     - opposites: urbano

### 22. grau

**Lexeme 1: grau**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈɡɾaw/ (pt-BR), /ˈɡɾaw/ (pt-PT)
  1. [B1] cada um dos níveis de uma escala de intensidade, valor ou importância
     - example: O grau de dificuldade desta prova é bastante alto.
     - frames: grau de algo
     - collocations: em certo grau; em maior grau; em menor grau; grau de parentesco; parente de primeiro grau; queimadura de terceiro grau; alto grau; grau de dificuldade; grau de escolaridade
  2. [A2] cada unidade das escalas usadas para medir a temperatura
     - example: Amanhã a temperatura pode chegar aos trinta e cinco graus.
     - collocations: graus Celsius; graus centígrados; graus abaixo de zero; graus de febre
  3. [B1] unidade de medida de ângulos, igual a uma de trezentas e sessenta partes da volta completa
     - example: Os ângulos de um triângulo somam sempre cento e oitenta graus.
     - collocations: ângulo de noventa graus; virar cento e oitenta graus; graus de latitude; graus de longitude
  4. [B2] título dado por uma universidade a quem conclui um curso
     - example: Ela recebeu o grau de mestre em engenharia no ano passado.
     - collocations: grau de mestre; grau de doutor; grau acadêmico; obter um grau; conferir um grau; colação de grau; colar grau
  5. [B2] medida da força de uma lente que corrige a visão
     - example: Meu grau aumentou e preciso trocar as lentes dos óculos.
     - collocations: óculos de grau; lentes de grau; ter grau; aumentar o grau
     - regions: pt-BR mainly

### 23. pobre

**Lexeme 1: pobre**, ADJ
- grammar: note same form for masculine and feminine
- said: /ˈpɔ.bɾi/ (pt-BR), /ˈpɔ.bɾɨ/ (pt-PT)
- forms: paupérrimo [Degree=Abs, Gender=Masc, Number=Sing] /paw.ˈpɛ.ʁi.mu/ /paw.ˈpɛ.ʁi.mu/ (formal; pobríssimo is the everyday form); pobríssimo [Degree=Abs, Gender=Masc, Number=Sing] /po.ˈbɾi.si.mu/ /pu.ˈbɾi.si.mu/
  1. [A1] que tem pouco dinheiro ou poucos bens para viver
     - example: Cresci numa família pobre, mas nunca nos faltou carinho.
     - collocations: país pobre; família pobre; bairro pobre; região pobre; ficar pobre; extremamente pobre
     - opposites: rico
  2. [B1] que tem pouca quantidade de alguma coisa necessária ou desejada
     - example: Os médicos recomendam uma dieta pobre em açúcar e sal.
     - frames: pobre em algo
     - collocations: solo pobre; terra pobre; dieta pobre
     - opposites: rico
  3. [A2] que merece pena ou compaixão por estar em má situação
     - example: A pobre mulher esperou horas sem ninguém a atender.
     - collocations: pobre coitado; pobre de mim; pobre diabo
     - grammar: position Attr; note in this sense usually placed before the noun
  4. [B2] de fraca qualidade, sem variedade nem riqueza de ideias
     - example: O texto tem um vocabulário pobre e muitas repetições.
     - collocations: vocabulário pobre; argumento pobre
     - opposites: rico
**Lexeme 2: pobre**, NOUN
- grammar: gender Com; countability Count
- said: /ˈpɔ.bɾi/ (pt-BR), /ˈpɔ.bɾɨ/ (pt-PT)
  1. [A2] pessoa que tem pouco dinheiro ou poucos bens para viver
     - example: Esta associação distribui comida aos pobres todas as semanas.
     - collocations: ajudar os pobres; ricos e pobres; dar esmola a um pobre
     - grammar: number Plur, mainly
     - opposites: rico
  2. [B1] pessoa que merece pena por estar em má situação
     - example: O pobre ficou sem saber o que responder ao chefe.
     - labels: formality informal

### 24. prédio

**Lexeme 1: prédio**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈpɾɛ.dʒju/ (pt-BR), /ˈpɾɛ.dju/ (pt-PT)
  1. [A1] construção feita para morar, trabalhar ou outros usos, geralmente com vários andares
     - example: Moramos no quinto andar de um prédio perto da praia.
     - collocations: prédio de apartamentos; prédio de escritórios; prédio alto; morar num prédio; construir um prédio; porteiro do prédio; entrada do prédio

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
  1. [A2] pessoa ou equipa que ganha uma competição ou um torneio
     - example: Os campeões receberam a taça diante de um estádio cheio.
     - frames: campeão de algo
     - collocations: sagrar-se campeão; campeão mundial; campeão do mundo; campeão olímpico; campeão nacional; atual campeão; campeão invicto
  2. [B2] pessoa ou coisa que supera todas as outras em alguma coisa
     - example: Esse filme foi o campeão de bilheteria do ano passado.
     - frames: campeão de algo; campeão em algo
     - collocations: campeão de vendas; campeão de audiência; campeão de bilheteria
     - labels: figurative True
  3. [C1] pessoa que luta com empenho para defender uma causa ou um grupo
     - example: Ela foi uma campeã dos direitos das mulheres durante décadas.
     - frames: campeão de algo
     - labels: style literary
**Lexeme 2: campeão**, ADJ
- grammar: comparable False; note feminine campeã
- said: /kɐ̃.pi.ˈɐ̃w/ (pt-BR), /kɐ̃.ˈpjɐ̃w/ (pt-PT), /kɐ̃.ˈpjɐ̃w/ (pt-BR, casual)
- forms: campeões [Gender=Masc, Number=Plur] /kɐ̃.pi.ˈõjs/ /kɐ̃.ˈpjõjʃ/; campeã [Gender=Fem, Number=Sing] /kɐ̃.pi.ˈɐ̃/ /kɐ̃.ˈpjɐ̃/; campeãs [Gender=Fem, Number=Plur] /kɐ̃.pi.ˈɐ̃s/ /kɐ̃.ˈpjɐ̃ʃ/
  1. [B1] que venceu um torneio ou uma competição
     - example: A equipa campeã desfilou pelas ruas da cidade.
     - collocations: time campeão; equipa campeã; seleção campeã

### 27. impacto

**Lexeme 1: impacto**, NOUN
- grammar: gender Masc; countability Count
- said: /ĩ.ˈpak.tu/ (pt-BR), /ĩ.ˈpa.ki.tu/ (pt-BR, casual), /ĩ.ˈpak.tu/ (pt-PT)
  1. [B1] efeito forte que algo produz sobre pessoas, coisas ou situações
     - example: As novas regras tiveram um grande impacto na vida dos moradores.
     - frames: impacto em algo/alguém; impacto sobre algo/alguém; impacto de algo
     - collocations: causar impacto; ter impacto; impacto ambiental; impacto social; impacto negativo; impacto positivo; avaliar o impacto; reduzir o impacto; de impacto
  2. [B2] choque violento de um corpo contra outro
     - example: O capacete protege a cabeça do motociclista no momento do impacto.
     - frames: impacto contra algo; impacto com algo
     - collocations: absorver o impacto; ponto de impacto; zona de impacto; força do impacto; resistir ao impacto

### 28. rico

**Lexeme 1: rico**, ADJ
- said: /ˈʁi.ku/ (pt-BR), /ˈʁi.ku/ (pt-PT)
- forms: riquíssimo [Degree=Abs, Gender=Masc, Number=Sing] /ʁi.ˈki.si.mu/ /ʁi.ˈki.si.mu/
  1. [A1] que tem muito dinheiro ou muitos bens
     - example: Com este salário, nunca vou ficar rico.
     - collocations: ficar rico; família rica; país rico; muito rico; homem rico
     - opposites: pobre
  2. [B1] que contém grande quantidade de alguma coisa
     - example: O espinafre é rico em ferro e vitaminas.
     - frames: rico em algo
     - collocations: rico em vitaminas; rico em proteínas; rico em fibras; solo rico
     - grammar: position Pred, mainly
     - opposites: pobre
  3. [B2] variado e de grande valor ou qualidade
     - example: Esta região tem uma cultura muito rica e antiga.
     - collocations: cultura rica; vocabulário rico; rica experiência; história rica
     - opposites: pobre
  4. [B1] muito bom ou agradável, sobretudo ao paladar
     - example: Que rico almoço nos preparaste hoje!
     - labels: formality informal
     - regions: pt-PT mainly
     - grammar: position Attr, mainly; note usually before the noun, often after que in exclamations
  5. [C1] feito com materiais caros e muitos enfeites; luxuoso
     - example: A sala tinha cortinas ricas e móveis de madeira escura.
     - labels: style literary
**Lexeme 2: rico**, NOUN
- grammar: gender Masc; countability Count; number Plur, mainly; note feminine rica for a woman; usually in the plural, os ricos
- said: /ˈʁi.ku/ (pt-BR), /ˈʁi.ku/ (pt-PT)
  1. [B1] pessoa que tem muito dinheiro ou muitos bens
     - example: A diferença entre ricos e pobres aumentou nos últimos anos.
     - collocations: ricos e pobres; os ricos e famosos
     - opposites: pobre
- relations: derived_from rico (ADJ)

### 29. atitude

**Lexeme 1: atitude**, NOUN
- grammar: gender Fem
- said: /a.tʃi.ˈtu.dʒi/ (pt-BR), /ɐ.ti.ˈtu.dɨ/ (pt-PT)
  1. [B1] maneira de agir ou de reagir diante de uma situação
     - example: Precisamos tomar uma atitude antes que o problema piore.
     - collocations: tomar uma atitude; mudar de atitude; atitude corajosa; atitude correta; atitude infantil; adotar uma atitude
     - grammar: countability Count
  2. [B1] modo de pensar ou de sentir sobre alguém ou algo
     - example: A atitude dele em relação ao trabalho melhorou muito.
     - frames: atitude em relação a algo/alguém; atitude perante algo/alguém; atitude para com alguém
     - collocations: atitude positiva; atitude negativa; atitude crítica
     - grammar: countability Count
  3. [B2] confiança e ousadia de quem se mostra seguro de si
     - example: Essa cantora tem muita atitude no palco.
     - collocations: ter atitude
     - labels: formality informal
     - regions: pt-BR mainly
     - grammar: countability Mass
  4. [C1] posição do corpo que mostra uma intenção ou um sentimento
     - example: Os fiéis permaneciam de joelhos, em atitude de oração.
     - frames: em atitude de + algo
     - labels: style literary
     - grammar: countability Count

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
  1. [B1] separação de um todo em partes ou repartição entre várias pessoas
     - example: A divisão das tarefas domésticas ainda é desigual em muitas casas.
     - frames: divisão de algo; divisão de algo entre alguém; divisão em algo
     - collocations: divisão do trabalho; divisão de tarefas; divisão de bens; fazer a divisão; divisão justa; divisão igualitária
     - grammar: countability Both
  2. [A2] operação matemática que calcula quantas vezes um número cabe noutro
     - example: Hoje vamos aprender a fazer contas de divisão com números grandes.
     - collocations: fazer uma divisão; conta de divisão; divisão exata
     - grammar: countability Count
  3. [B2] falta de acordo que separa pessoas de um grupo em lados opostos
     - example: A proposta provocou uma profunda divisão entre os membros do partido.
     - frames: divisão entre alguém
     - collocations: divisão profunda; provocar divisão; criar divisões
     - grammar: countability Both
     - opposites: união
  4. [B1] cada grupo de equipas do mesmo nível num campeonato
     - example: O nosso clube voltou à primeira divisão depois de três anos.
     - collocations: primeira divisão; segunda divisão; subir de divisão; descer de divisão
     - grammar: countability Count
  5. [B2] setor de uma empresa ou organização com funções próprias
     - example: Ela trabalha na divisão de vendas há cinco anos.
     - frames: divisão de algo
     - grammar: countability Count
  6. [B1] cada espaço de uma casa separado dos outros por paredes
     - example: O apartamento é pequeno, mas tem três divisões bem iluminadas.
     - regions: pt-PT mainly
     - grammar: countability Count
  7. [C1] grande unidade do exército formada por várias brigadas ou regimentos
     - example: Uma divisão de infantaria avançou durante a madrugada.
     - collocations: divisão blindada; divisão de infantaria
     - labels: domain military
     - grammar: countability Count
- relations: verbal_noun_of dividir (VERB)

### 32. agradecer

**Lexeme 1: agradecer**, VERB
- grammar: conjugation 2; valency Both; irregular False; note c becomes ç before a and o (agradeço, agradeça); stressed e is /e/ in agradeço and /ɛ/ in agradece
- said: /a.ɡɾa.de.ˈseɾ/ (pt-BR), /ɐ.ɡɾɐ.dɨ.ˈseɾ/ (pt-PT), /a.ɡɾa.de.ˈse/ (pt-BR, casual)
- forms: agradeço [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /a.ɡɾa.ˈde.su/ /ɐ.ɡɾɐ.ˈde.su/; agradeces [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /a.ɡɾa.ˈdɛ.sis/ /ɐ.ɡɾɐ.ˈdɛ.sɨʃ/; agradece [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /a.ɡɾa.ˈdɛ.si/ /ɐ.ɡɾɐ.ˈdɛ.sɨ/; agradecemos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /a.ɡɾa.de.ˈse.mus/ /ɐ.ɡɾɐ.dɨ.ˈse.muʃ/; agradecem [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /a.ɡɾa.ˈdɛ.sẽj/ /ɐ.ɡɾɐ.ˈdɛ.sɐ̃j/; agradeci [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /a.ɡɾa.de.ˈsi/ /ɐ.ɡɾɐ.dɨ.ˈsi/; agradeceu [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /a.ɡɾa.de.ˈsew/ /ɐ.ɡɾɐ.dɨ.ˈsew/; agradeça [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /a.ɡɾa.ˈde.sɐ/ /ɐ.ɡɾɐ.ˈde.sɐ/; agradecido [VerbForm=Part, Tense=Past] /a.ɡɾa.de.ˈsi.du/ /ɐ.ɡɾɐ.dɨ.ˈsi.du/
  1. [A2] dizer obrigado ou mostrar gratidão a alguém pelo que recebeu
     - example: Quero agradecer a todos pela ajuda durante a mudança.
     - frames: agradecer algo a alguém; agradecer a alguém por algo; agradecer por algo; agradecer a alguém; agradecer por + inf
     - collocations: agradecer de coração; agradecer a atenção; agradecer a ajuda; agradecer a Deus; agradecer imensamente; agradecer o convite; agradecer a preferência
  2. [B2] tirar proveito de um cuidado recebido, mostrando melhora
     - example: Regue as plantas todos os dias, que elas agradecem.
     - labels: figurative True, formality informal
     - grammar: valency Both

### 33. assembleia

**Lexeme 1: assembleia**, NOUN
- grammar: gender Fem; countability Count
- said: /a.sẽ.ˈblɛj.ɐ/ (pt-BR), /ɐ.sẽ.ˈblɐj.ɐ/ (pt-PT)
- spellings: assembleia (pt-BR, pt-PT); assembléia (pt-BR) [older books only]
  1. [B1] reunião de pessoas para discutir e decidir assuntos de interesse comum
     - example: Os trabalhadores decidiram em assembleia fazer greve na segunda-feira.
     - collocations: assembleia geral; convocar uma assembleia; realizar uma assembleia; em assembleia; assembleia de acionistas
  2. [B2] conjunto de representantes eleitos que fazem as leis de um país ou região
     - example: A assembleia vota hoje a nova lei de proteção do ambiente.
     - collocations: assembleia legislativa; assembleia constituinte; assembleia municipal

### 34. aceitar

**Lexeme 1: aceitar**, VERB
- grammar: conjugation 1; valency Tr; irregular False; note has a short past participle besides aceitado: aceito in Brazil, aceite in Portugal
- said: /a.sej.ˈtaɾ/ (pt-BR), /ɐ.sɐj.ˈtaɾ/ (pt-PT), /a.sej.ˈta/ (pt-BR, casual)
- forms: aceito [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /a.ˈsej.tu/ /ɐ.ˈsɐj.tu/; aceitas [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /a.ˈsej.tas/ /ɐ.ˈsɐj.tɐʃ/; aceita [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /a.ˈsej.tɐ/ /ɐ.ˈsɐj.tɐ/; aceitamos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /a.sej.ˈtɐ̃.mus/ /ɐ.sɐj.ˈtɐ.muʃ/; aceitam [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /a.ˈsej.tɐ̃w/ /ɐ.ˈsɐj.tɐ̃w/; aceitei [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /a.sej.ˈtej/ /ɐ.sɐj.ˈtɐj/; aceitou [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /a.sej.ˈtow/ /ɐ.sɐj.ˈto/; aceite [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /a.ˈsej.tʃi/ /ɐ.ˈsɐj.tɨ/; aceitado [VerbForm=Part, Tense=Past] /a.sej.ˈta.du/ /ɐ.sɐj.ˈta.du/ (mostly after ter and haver); aceito [VerbForm=Part, Tense=Past] /a.ˈsej.tu/ /ɐ.ˈsɐj.tu/ (pt-BR) (usual after ser and estar, also common after ter); aceite [VerbForm=Part, Tense=Past] /a.ˈsej.tʃi/ /ɐ.ˈsɐj.tɨ/ (pt-PT) (usual after ser and estar, also common after ter)
  1. [A2] receber de bom grado o que é oferecido ou proposto
     - example: Ela aceitou o convite para jantar sem pensar duas vezes.
     - frames: aceitar algo; aceitar + inf
     - collocations: aceitar um convite; aceitar uma proposta; aceitar um desafio; aceitar uma oferta; aceitar um emprego; aceitar um pedido de desculpas; aceitar cartão; aceitar um presente
     - opposites: recusar
  2. [B1] reconhecer como verdadeiro, correto ou válido
     - example: Hoje a maioria dos cientistas aceita essa teoria sem discussão.
     - frames: aceitar algo; aceitar que + ind
     - collocations: aceitar uma teoria; aceitar uma explicação; aceitar os argumentos; aceitar uma sugestão
     - opposites: rejeitar
  3. [B1] suportar sem revolta algo difícil ou que não se pode mudar
     - example: É difícil aceitar a perda de alguém que amamos muito.
     - frames: aceitar algo; aceitar que + subj
     - collocations: aceitar a derrota; aceitar a realidade; aceitar o destino; aceitar a morte; aceitar as críticas; aceitar alguém como é
  4. [B1] admitir uma pessoa como membro de um grupo, escola ou instituição
     - example: A escola aceita alunos de todas as idades a partir de setembro.
     - frames: aceitar alguém em algo; aceitar alguém como algo
     - collocations: ser aceito na universidade; aceitar novos membros
     - opposites: rejeitar

### 35. álbum

**Lexeme 1: álbum**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈaw.bũ/ (pt-BR), /ˈal.bũ/ (pt-PT)
- forms: álbuns [Number=Plur] /ˈaw.bũs/ /ˈal.bũʃ/
  1. [A2] livro com páginas próprias para guardar ou colar fotografias e outras imagens
     - example: A minha avó guarda as fotos antigas num álbum de capa azul.
     - collocations: álbum de fotografias; álbum de fotos; álbum de família; álbum de casamento; álbum de figurinhas; folhear um álbum
  2. [A2] conjunto de músicas de um artista lançado ao mesmo tempo, em disco ou digitalmente
     - example: Esta banda vai lançar um novo álbum no próximo mês.
     - collocations: lançar um álbum; gravar um álbum; álbum de estreia; álbum ao vivo; álbum de estúdio; faixa do álbum

### 36. gênero

**Lexeme 1: gênero**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈʒẽ.ne.ɾu/ (pt-BR, gênero), /ˈʒɛ.nɨ.ɾu/ (pt-PT, género)
- spellings: gênero (pt-BR); género (pt-PT)
  1. [B1] conjunto de coisas ou seres com características em comum; tipo, espécie
     - example: Nunca tinha visto nada do gênero em toda a minha vida.
     - frames: gênero de algo
     - collocations: do gênero; algo do gênero; desse gênero; o gênero humano
  2. [B1] categoria de obras artísticas que partilham forma, estilo ou tema
     - example: O romance policial é o gênero literário que eu mais leio.
     - collocations: gênero literário; gênero musical; gênero cinematográfico; gênero textual
  3. [B1] categoria que divide as palavras em masculinas, femininas ou neutras
     - example: Em português, o adjetivo concorda em gênero e número com o substantivo.
     - collocations: gênero masculino; gênero feminino; gênero neutro; concordar em gênero e número
     - labels: domain grammar
  4. [B2] identidade e papel social de uma pessoa como homem, mulher ou outra, além do sexo biológico
     - example: A empresa lançou um programa para combater a desigualdade de gênero.
     - collocations: igualdade de gênero; identidade de gênero; violência de gênero; questões de gênero; estudos de gênero
  5. [C1] grupo de classificação dos seres vivos, acima da espécie e abaixo da família
     - example: O lobo e o cão são espécies do mesmo gênero.
     - labels: domain biology
  6. [B2] produtos que se compram e vendem, sobretudo alimentos
     - example: Os preços dos gêneros alimentícios subiram muito este mês.
     - collocations: gêneros alimentícios; gêneros de primeira necessidade
     - grammar: number Plur

### 37. quadro

**Lexeme 1: quadro**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈkwa.dɾu/ (pt-BR), /ˈkwa.dɾu/ (pt-PT)
  1. [A2] pintura ou desenho feito numa tela ou placa, geralmente com moldura
     - example: Comprámos um quadro antigo para pendurar por cima do sofá.
     - collocations: pendurar um quadro; pintar um quadro; quadro a óleo; exposição de quadros
  2. [A2] superfície lisa presa à parede onde se escreve com giz ou caneta
     - example: Copiem para o caderno tudo o que está escrito no quadro.
     - collocations: escrever no quadro; apagar o quadro; ir ao quadro; quadro negro; quadro branco; quadro de avisos
  3. [B1] conjunto de dados organizados em linhas e colunas ou num esquema
     - example: Os resultados aparecem resumidos no quadro da página seguinte.
     - collocations: quadro comparativo; quadro de horários
  4. [B2] situação ou conjunto de circunstâncias num dado momento
     - example: O médico disse que o quadro do doente continua estável.
     - frames: quadro de algo
     - collocations: quadro clínico; quadro político; quadro geral; traçar um quadro; agravamento do quadro
  5. [B2] conjunto das pessoas que trabalham de forma permanente numa organização
     - example: Ela entrou este ano para o quadro permanente da universidade.
     - collocations: quadro de pessoal; fazer parte do quadro; quadro efetivo; entrar para o quadro
  6. [B2] estrutura de metal que forma o corpo de uma bicicleta ou moto
     - example: O quadro desta bicicleta é de alumínio e muito leve.
  7. [B2] parte de um programa de televisão ou rádio com tema próprio
     - example: Meu quadro favorito do programa é o das receitas rápidas.
     - regions: pt-BR mainly
  8. [B1] caixa com os disjuntores e interruptores da instalação elétrica de um edifício
     - example: Se faltar a luz, verifica primeiro o quadro elétrico.
     - collocations: quadro elétrico; quadro de luz

### 38. localização

**Lexeme 1: localização**, NOUN
- grammar: gender Fem; countability Both
- said: /lo.ka.li.za.ˈsɐ̃w/ (pt-BR), /lu.kɐ.li.zɐ.ˈsɐ̃w/ (pt-PT)
  1. [B1] lugar onde alguém ou alguma coisa está ou fica
     - example: A localização do hotel é ótima, bem perto da praia.
     - frames: localização de algo/alguém
     - collocations: boa localização; localização privilegiada; localização exata; localização geográfica; localização central
     - grammar: countability Count
  2. [B2] descoberta do ponto exato onde alguém ou alguma coisa se encontra
     - example: Ative a localização no telemóvel para encontrar o restaurante.
     - frames: localização de algo/alguém
     - collocations: ativar a localização; sistema de localização; serviço de localização
     - grammar: countability Mass
- relations: verbal_noun_of localizar (VERB)

### 39. programação

**Lexeme 1: programação**, NOUN
- grammar: gender Fem; countability Both
- said: /pɾo.ɡɾa.ma.ˈsɐ̃w/ (pt-BR), /pɾu.ɡɾɐ.mɐ.ˈsɐ̃w/ (pt-PT)
  1. [B1] atividade de escrever o código e as instruções que um computador executa
     - example: Ela estuda programação para criar os seus próprios jogos.
     - collocations: linguagem de programação; aprender programação; curso de programação; programação web
     - grammar: countability Mass
  2. [B1] conjunto das emissões que um canal de televisão ou rádio transmite, com seus horários
     - example: A programação da televisão no domingo à tarde é muito fraca.
     - collocations: grade de programação; programação infantil; programação da televisão; horário de programação
     - grammar: countability Count
  3. [B1] conjunto das atividades previstas para um evento ou um período, com datas e horários
     - example: Já viste a programação do festival deste ano?
     - frames: programação de algo
     - collocations: programação cultural; fazer a programação; programação do festival
     - grammar: countability Count
  4. [B2] ato de preparar um aparelho ou um plano para funcionar de certo modo
     - example: A programação do alarme leva só alguns minutos.
     - frames: programação de algo
     - grammar: countability Mass
- relations: verbal_noun_of programar (VERB)

### 40. quente

**Lexeme 1: quente**, ADJ
- grammar: note same form for masculine and feminine
- said: /ˈkẽ.tʃi/ (pt-BR), /ˈkẽ.tɨ/ (pt-PT)
  1. [A1] que tem temperatura alta ou mais alta do que o normal
     - example: Cuidado, a sopa ainda está muito quente.
     - collocations: água quente; banho quente; tempo quente; dia quente; clima quente; bebida quente; estar quente
     - opposites: frio
  2. [A2] que protege do frio e conserva o calor do corpo
     - example: Leva um casaco quente, que à noite faz muito frio.
     - collocations: roupa quente; casaco quente; cobertor quente; meias quentes
     - grammar: position Attr, mainly
  3. [B2] que desperta muito interesse ou discussão no momento
     - example: Esse assunto continua quente nas redes sociais esta semana.
     - collocations: assunto quente; notícia quente; tema quente
     - labels: figurative True
  4. [B2] de tons avermelhados ou amarelados que dão sensação de calor
     - example: Para a sala, prefiro paredes pintadas em cores quentes.
     - collocations: cores quentes; tons quentes
     - opposites: frio
  5. [B2] que tem conteúdo sexual ou provoca desejo
     - example: O filme tem algumas cenas quentes e não é para crianças.
     - collocations: cena quente
     - labels: formality informal

### 41. orgulho

**Lexeme 1: orgulho**, NOUN
- grammar: gender Masc; countability Mass
- said: /oɾ.ˈɡu.ʎu/ (pt-BR), /ɔɾ.ˈɡu.ʎu/ (pt-PT)
  1. [B1] sentimento de satisfação pelo que se conseguiu ou pelo valor de alguém próximo
     - example: Sinto muito orgulho do trabalho que a minha filha fez.
     - frames: orgulho de algo/alguém; orgulho em + inf; ser o orgulho de alguém
     - collocations: ter orgulho; sentir orgulho; motivo de orgulho; com orgulho; cheio de orgulho; encher de orgulho; orgulho nacional
  2. [B2] ideia exagerada do próprio valor, que leva a desprezar os outros
     - example: O seu orgulho acabou por afastá-lo de todos os colegas.
     - collocations: orgulho excessivo
     - opposites: humildade
  3. [B2] sentimento da própria dignidade, que não aceita ser humilhado
     - example: Aquelas palavras duras feriram o orgulho do rapaz.
     - collocations: ferir o orgulho; engolir o orgulho; orgulho ferido; pôr o orgulho de lado

### 42. pretenso

**Lexeme 1: pretenso**, ADJ
- grammar: position Attr, mainly; comparable False; note usually placed before the noun
- said: /pɾe.ˈtẽ.su/ (pt-BR), /pɾɨ.ˈtẽ.su/ (pt-PT)
  1. [C1] que se diz ou se julga ser algo, sem que isso esteja provado
     - example: O pretenso especialista não soube responder às perguntas mais simples.
     - collocations: pretenso autor; pretensa vítima; pretenso direito
- relations: confusable pretensioso (ADJ): looks like English 'pretentious', which is 'pretensioso'; 'pretenso' means supposed or alleged

### 43. live

**Lexeme 1: live**, NOUN
- grammar: gender Fem; countability Count; note loanword from English; occasionally masculine
- said: /ˈlaj.vi/ (pt-BR), /ˈlaj.vɨ/ (pt-PT)
  1. [B1] transmissão de vídeo pela internet feita em tempo real, ou a sua gravação
     - example: Vou fazer uma live hoje à noite para conversar com os fãs.
     - collocations: fazer uma live; assistir à live; entrar na live; live solidária
     - regions: pt-BR mainly

### 44. audiência

**Lexeme 1: audiência**, NOUN
- grammar: gender Fem; countability Count
- said: /aw.dʒi.ˈẽ.sjɐ/ (pt-BR), /aw.ˈdjẽ.sjɐ/ (pt-PT), /aw.ˈdʒjẽ.sjɐ/ (pt-BR, casual)
  1. [B1] conjunto das pessoas que veem ou escutam um programa ou espetáculo
     - example: A novela das nove ainda tem uma audiência enorme no país.
     - collocations: índice de audiência; recorde de audiência; líder de audiência; ganhar audiência; perder audiência; grande audiência
  2. [B2] sessão em que um juiz ouve as partes e as testemunhas de um processo
     - example: O advogado pediu que a audiência fosse adiada por uma semana.
     - collocations: marcar a audiência; adiar a audiência; audiência de julgamento; audiência de custódia
     - labels: domain law
  3. [C1] encontro formal em que uma autoridade recebe alguém para o ouvir
     - example: Os moradores pediram audiência ao presidente da câmara.
     - frames: audiência com alguém
     - collocations: pedir audiência; conceder uma audiência; receber em audiência; audiência privada
     - labels: formality formal

### 45. independência

**Lexeme 1: independência**, NOUN
- grammar: gender Fem; countability Mass, mainly
- said: /ĩ.de.pẽ.ˈdẽ.sjɐ/ (pt-BR), /ĩ.dɨ.pẽ.ˈdẽ.sjɐ/ (pt-PT)
  1. [B1] situação de um país que se governa sozinho, sem o domínio de outro
     - example: O país comemora este ano dois séculos de independência.
     - collocations: declarar a independência; proclamar a independência; conquistar a independência; luta pela independência; guerra da independência; dia da independência
  2. [B1] capacidade de viver, agir e decidir sozinho, sem ajuda nem controlo de outros
     - example: Ter o próprio salário dá aos jovens muito mais independência.
     - frames: independência de algo/alguém; independência em relação a algo/alguém
     - collocations: independência financeira; ganhar independência; perder a independência; independência dos tribunais
     - opposites: dependência
- relations: abstract_noun_of independente (ADJ)
