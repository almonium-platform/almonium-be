# Portuguese: entries to check (1 of 1)

These are entries of a learner's dictionary of Portuguese, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of Portuguese.

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
  sense, or an example is not natural Portuguese.

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

### 1. jornal

**Lexeme 1: jornal**, NOUN
- grammar: gender Masc; countability Count
- said: /ʒoɾ.ˈnaw/ (pt-BR), /ʒuɾ.ˈnal/ (pt-PT)
  1. [A1] publicação diária com notícias, artigos e opiniões, impressa ou digital
     - example: Meu avô lê o jornal todas as manhãs no café.
  2. [A2] programa de televisão ou rádio que apresenta as notícias do dia
     - example: Vamos ver o jornal das oito antes do jantar?

### 2. donzela

**Lexeme 1: donzela**, NOUN
- grammar: gender Fem; countability Count
- said: /dõ.ˈzɛ.lɐ/ (pt-BR), /dõ.ˈzɛ.lɐ/ (pt-PT)
  1. [B2] mulher jovem e solteira, sobretudo nas histórias de outros tempos
     - example: O cavaleiro partiu a galope para salvar a donzela presa na torre.
     - labels: style literary
  2. [C1] mulher que ainda não teve relações sexuais
     - example: Antigamente, esperava-se que a noiva chegasse donzela ao casamento.
     - labels: time dated

### 3. decurso

**Lexeme 1: decurso**, NOUN
- grammar: gender Masc; countability Mass
- said: /de.ˈkuɾ.su/ (pt-BR), /dɨ.ˈkuɾ.su/ (pt-PT)
  1. [C1] passagem contínua do tempo ou desenvolvimento de um acontecimento
     - example: No decurso da reunião, surgiram várias propostas interessantes.
     - frames: no decurso de algo; com o decurso do tempo
     - labels: formality formal

### 4. predador

**Lexeme 1: predador**, NOUN
- grammar: gender Masc; countability Count; note a woman in sense 2 is 'predadora'
- said: /pɾe.da.ˈdoɾ/ (pt-BR), /pɾɨ.dɐ.ˈdoɾ/ (pt-PT)
  1. [B2] animal que caça e mata outros animais para se alimentar
     - example: O leão é o maior predador da savana africana.
  2. [C1] pessoa que se aproveita dos mais fracos para os explorar ou abusar
     - example: Os pais devem proteger os filhos de predadores na internet.
     - labels: figurative True
**Lexeme 2: predador**, ADJ
- said: /pɾe.da.ˈdoɾ/ (pt-BR), /pɾɨ.dɐ.ˈdoɾ/ (pt-PT)
  1. [B2] que caça e mata outros animais para se alimentar
     - example: Nestas águas vivem várias espécies de peixes predadores.

### 5. metálico

**Lexeme 1: metálico**, ADJ
- said: /me.ˈta.li.ku/ (pt-BR), /mɨ.ˈta.li.ku/ (pt-PT)
  1. [B1] feito de material duro e brilhante que conduz calor e eletricidade
     - example: Guarde os biscoitos numa caixa metálica bem fechada.
  2. [B2] semelhante ao ferro ou à prata no som, na cor ou no brilho
     - example: Ouvimos um som metálico vindo da garagem durante a noite.
- relations: adjective_of metal (NOUN)
**Lexeme 2: metálico**, NOUN
- grammar: gender Masc; countability Mass
- said: /me.ˈta.li.ku/ (pt-BR), /mɨ.ˈta.li.ku/ (pt-PT)
  1. [C1] dinheiro vivo, em notas ou moedas, entregue na hora
     - example: O pagamento pode ser feito em metálico ou com cartão.
     - frames: em metálico
     - regions: pt-PT mainly
- relations: derived_from metálico (ADJ)

### 6. vibrante

**Lexeme 1: vibrante**, ADJ
- grammar: comparable True; note same form for masculine and feminine; plural vibrantes
- said: /vi.ˈbɾɐ̃.tʃi/ (pt-BR), /vi.ˈbɾɐ̃.tɨ/ (pt-PT)
  1. [B1] cheio de energia, vida e entusiasmo; muito animado
     - example: A cidade tem uma vida noturna vibrante e cheia de música.
  2. [B1] de cor muito viva, forte e intensa
     - example: Gosto de pintar as paredes com cores vibrantes.
  3. [B2] que soa com força e ressonância, de modo claro
     - example: Com a sua voz vibrante, o cantor encheu a sala inteira.
- relations: derived_from vibrar (VERB)

### 7. vez

**Lexeme 1: vez**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈves/ (pt-BR), /ˈveʃ/ (pt-PT)
  1. [A1] cada ocasião em que algo acontece ou se repete
     - example: Já visitei esta cidade três vezes e quero voltar.
     - frames: uma vez / duas vezes; às vezes; de vez em quando
  2. [A2] momento em que cabe a uma pessoa fazer algo, seguindo uma ordem
     - example: Espera um pouco, agora é a minha vez de jogar.
     - frames: a vez de alguém; a vez de + inf.

### 8. bastante

**Lexeme 1: bastante**, ADV
- grammar: invariable True
- said: /bas.ˈtɐ̃.tʃi/ (pt-BR), /bɐʃ.ˈtɐ̃.tɨ/ (pt-PT)
  1. [A2] muito, em grau ou quantidade considerável
     - example: Hoje está bastante frio para sair sem casaco.
     - opposites: pouco
  2. [B1] o necessário, na medida certa para o que se precisa
     - example: Acho que não estudei o suficiente; preciso de ler bastante mais.
**Lexeme 2: bastante**, DET
- grammar: note same form for masculine and feminine; plural bastantes
- said: /bas.ˈtɐ̃.tʃi/ (pt-BR), /bɐʃ.ˈtɐ̃.tɨ/ (pt-PT)
- forms: bastantes [Number=Plur] /bas.ˈtɐ̃.tʃis/ /bɐʃ.ˈtɐ̃.tɨʃ/
  1. [A2] em grande número ou quantidade; muito
     - example: Havia bastantes pessoas na fila do banco esta manhã.
     - opposites: pouco
**Lexeme 3: bastante**, ADJ
- grammar: note same form for masculine and feminine; usually after the noun or predicative
- said: /bas.ˈtɐ̃.tʃi/ (pt-BR), /bɐʃ.ˈtɐ̃.tɨ/ (pt-PT)
  1. [B2] que é suficiente para aquilo de que se precisa
     - example: Ainda não temos dinheiro bastante para comprar uma casa.
     - opposites: insuficiente
- relations: derived_from bastar (VERB)

### 9. anterior

**Lexeme 1: anterior**, ADJ
- grammar: note same form for masculine and feminine; plural anteriores
- said: /ɐ̃.te.ɾi.ˈoɾ/ (pt-BR), /ɐ̃.tɨ.ɾi.ˈoɾ/ (pt-PT)
  1. [B1] que aconteceu ou veio antes, no tempo ou numa sequência
     - example: Na semana anterior, choveu quase todos os dias na cidade.
     - frames: anterior a algo
     - opposites: seguinte, posterior
  2. [B2] que fica na frente ou na parte da frente de algo
     - example: O cão machucou uma das patas anteriores ao saltar o muro.
     - opposites: posterior

### 10. desvendar

**Lexeme 1: desvendar**, VERB
- grammar: conjugation 1; valency Tr; auxiliary have
- said: /dʒiz.vẽ.ˈdaɾ/ (pt-BR), /dɨʒ.vẽ.ˈdaɾ/ (pt-PT)
  1. [B2] descobrir e explicar algo que estava escondido, secreto ou misterioso
     - example: Os investigadores ainda tentam desvendar o mistério do desaparecimento.
     - frames: desvendar um mistério; desvendar um segredo
  2. [C1] tirar o pano que cobre os olhos de alguém
     - example: Quando lhe desvendaram os olhos, viu que estava numa sala vazia.
     - opposites: vendar
- relations: derived_from vendar (VERB)

### 11. risco

**Lexeme 1: risco**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈʁis.ku/ (pt-BR), /ˈʁiʃ.ku/ (pt-PT)
  1. [B1] possibilidade de acontecer algo mau, como um perigo ou uma perda
     - example: Fumar aumenta muito o risco de doenças do coração.
     - frames: risco de + inf; correr (o) risco de + inf; em risco
  2. [B1] linha ou marca fina feita numa superfície com lápis ou objeto pontiagudo
     - example: Quem fez este risco na porta do carro novo?

### 12. adequação

**Lexeme 1: adequação**, NOUN
- grammar: gender Fem; countability Both
- said: /a.de.kwa.ˈsɐ̃w/ (pt-BR), /ɐ.dɨ.kwɐ.ˈsɐ̃w/ (pt-PT)
  1. [B2] correspondência apropriada entre uma coisa e o fim ou a situação a que serve
     - example: Os especialistas duvidam da adequação deste método ao problema.
     - frames: adequação de algo a algo
     - grammar: countability Mass
  2. [C1] ajuste feito em algo para que sirva a uma finalidade ou cumpra uma regra
     - example: A empresa terá de fazer a adequação das instalações às novas normas.
     - frames: adequação de algo a algo
     - grammar: countability Count
- relations: verbal_noun_of adequar (VERB)

### 13. aniversário

**Lexeme 1: aniversário**, NOUN
- grammar: gender Masc; countability Count
- said: /a.ni.veɾ.ˈsa.ɾju/ (pt-BR), /ɐ.ni.vɨɾ.ˈsa.ɾju/ (pt-PT)
  1. [A1] dia em que uma pessoa completa mais um ano de vida
     - example: Amanhã é o meu aniversário e vou dar uma festa.
  2. [B1] data em que se lembra um acontecimento ocorrido em anos anteriores
     - example: A cidade celebra hoje o centésimo aniversário da sua fundação.
     - frames: aniversário de algo

### 14. independente

**Lexeme 1: independente**, ADJ
- grammar: comparable True; note same form for masculine and feminine; plural independentes
- said: /ĩ.de.pẽ.ˈdẽ.tʃi/ (pt-BR), /ĩ.dɨ.pẽ.ˈdẽ.tɨ/ (pt-PT)
  1. [B1] que vive e decide sozinho, sem precisar da ajuda de outras pessoas
     - example: Desde os dezoito anos, a minha filha é muito independente.
     - opposites: dependente
  2. [B1] que tem governo próprio e não é controlado por outro país
     - example: Aquele pequeno país é independente há quase cinquenta anos.
  3. [B2] que não tem ligação nem relação com outra coisa
     - example: São dois problemas independentes e devem ser resolvidos separadamente.
     - frames: independente de algo
  4. [B2] que não pertence a partido, grande empresa ou grupo que o controle
     - example: Gosto de ouvir bandas independentes que gravam os próprios discos.
- relations: derived_from dependente (ADJ)

### 15. jardim

**Lexeme 1: jardim**, NOUN
- grammar: gender Masc; countability Count
- said: /ʒaɾ.ˈdʒĩ/ (pt-BR), /ʒɐɾ.ˈdĩ/ (pt-PT)
  1. [A1] terreno com flores, árvores e outras plantas, para enfeite ou descanso
     - example: Plantamos rosas vermelhas no jardim da nossa casa.

### 16. acreditar

**Lexeme 1: acreditar**, VERB
- grammar: conjugation 1; irregular False; valency Both
- said: /a.kɾe.dʒi.ˈtaʁ/ (pt-BR), /ɐ.kɾɨ.di.ˈtaɾ/ (pt-PT)
  1. [A2] considerar que algo é verdadeiro ou que alguém diz a verdade
     - example: Ainda não acredito que passamos no exame!
     - frames: acreditar em algo/alguém; acreditar que + oração
     - grammar: valency Both
     - opposites: duvidar
  2. [B1] ter fé ou confiança em alguém, numa ideia ou em Deus
     - example: Você precisa acreditar em si mesmo para vencer.
     - frames: acreditar em alguém/algo
     - grammar: valency Intr
  3. [C1] reconhecer oficialmente alguém ou algo como autorizado ou válido
     - example: O governo acreditou cem jornalistas para cobrir a conferência.
     - labels: style official
     - grammar: valency Tr

### 17. governador

**Lexeme 1: governador**, NOUN
- grammar: gender Masc; countability Count
- said: /ɡo.veɾ.na.ˈdoɾ/ (pt-BR), /ɡu.vɨɾ.nɐ.ˈdoɾ/ (pt-PT)
  1. [B1] pessoa eleita ou nomeada que dirige um estado, província ou região
     - example: Quem você acha que vai ser eleito governador este ano?
     - frames: governador de + lugar
  2. [C1] pessoa que dirige o banco central de um país
     - example: Segundo o governador do banco central, os juros vão subir.
     - frames: governador do banco central
     - labels: domain economics
- relations: agent_noun_of governar (VERB)

### 18. antologia

**Lexeme 1: antologia**, NOUN
- grammar: gender Fem; countability Count
- said: /ɐ̃.to.lo.ˈʒi.ɐ/ (pt-BR), /ɐ̃.tu.lu.ˈʒi.ɐ/ (pt-PT)
  1. [B2] livro ou coleção que reúne textos ou obras escolhidas de um ou vários autores
     - example: Encontrei esse poema numa antologia de poesia contemporânea.
     - frames: antologia de algo

### 19. enfoque

**Lexeme 1: enfoque**, NOUN
- grammar: gender Masc; countability Count
- said: /ẽ.ˈfɔ.ki/ (pt-BR), /ẽ.ˈfɔ.kɨ/ (pt-PT)
  1. [B2] maneira de olhar, tratar ou analisar um assunto ou problema
     - example: Precisamos de um enfoque diferente para resolver este problema.
     - frames: enfoque sobre algo
  2. [B2] aspeto a que se dá mais importância ou atenção
     - example: Neste curso, o enfoque está na conversação do dia a dia.
     - frames: dar enfoque a algo; com enfoque em algo
- relations: verbal_noun_of enfocar (VERB)

### 20. inclusive

**Lexeme 1: inclusive**, ADV
- grammar: invariable True
- said: /ĩ.klu.ˈzi.vi/ (pt-BR), /ĩ.klu.ˈzi.vɨ/ (pt-PT)
  1. [B1] até, também; serve para acrescentar algo talvez inesperado
     - example: Abrimos todos os dias, inclusive aos domingos e feriados.
  2. [B2] contando também o primeiro ou o último elemento indicado
     - example: Leiam com atenção os capítulos de um a cinco, inclusive.
     - opposites: exclusive

### 21. semáforo

**Lexeme 1: semáforo**, NOUN
- grammar: gender Masc; countability Count
- said: /se.ˈma.fo.ɾu/ (pt-BR), /sɨ.ˈma.fu.ɾu/ (pt-PT)
  1. [A2] aparelho de luzes coloridas que controla a passagem de veículos e pessoas
     - example: Espere o semáforo ficar verde antes de atravessar a rua.

### 22. igreja

**Lexeme 1: igreja**, NOUN
- grammar: gender Fem; countability Count
- said: /i.ˈɡɾe.ʒɐ/ (pt-BR), /i.ˈɡɾɐj.ʒɐ/ (pt-PT)
  1. [A1] edifício onde os cristãos se reúnem para rezar e assistir à missa
     - example: Todos os domingos vamos à igreja com os nossos avós.
     - frames: ir à igreja; na igreja
  2. [B1] conjunto dos fiéis e das autoridades de uma religião cristã
     - example: A Igreja Católica tem milhões de membros em todo o mundo.

### 23. jaula

**Lexeme 1: jaula**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈʒaw.lɐ/ (pt-BR), /ˈʒaw.lɐ/ (pt-PT)
  1. [B1] espaço fechado por grades onde se prendem animais ou pessoas
     - example: O leão andava de um lado para o outro dentro da jaula.
  2. [B2] lugar onde alguém se sente fechado e sem liberdade
     - example: Para ele, aquele escritório sem janelas era uma verdadeira jaula.
     - labels: figurative True

### 24. momento

**Lexeme 1: momento**, NOUN
- grammar: gender Masc; countability Count
- said: /mo.ˈmẽ.tu/ (pt-BR), /mu.ˈmẽ.tu/ (pt-PT)
  1. [A1] espaço de tempo muito curto; instante
     - example: Espera só um momento, que eu já volto para te ajudar.
     - frames: um momento; por um momento
  2. [A2] ponto ou período determinado no tempo; ocasião, fase, situação
     - example: Neste momento, o país atravessa uma fase muito difícil.
     - frames: neste momento; no momento em que; de momento

### 25. participação

**Lexeme 1: participação**, NOUN
- grammar: gender Fem; countability Both
- said: /paɾ.tʃi.si.pa.ˈsɐ̃w/ (pt-BR), /pɐɾ.tɨ.si.pɐ.ˈsɐ̃w/ (pt-PT)
  1. [B1] envolvimento de uma pessoa numa atividade, num evento ou numa decisão
     - example: Com a participação de todos, a festa vai ser um sucesso.
     - frames: participação em algo
  2. [B2] parte que alguém possui no capital ou nos lucros de uma empresa
     - example: O banco tem uma participação de dez por cento na empresa.
     - frames: participação em algo
     - labels: domain economics
     - grammar: countability Count
  3. [C1] comunicação formal de um acontecimento a alguém ou a uma autoridade
     - example: Depois do assalto, apresentámos uma participação na esquadra.
     - labels: formality formal
     - grammar: countability Count
- relations: verbal_noun_of participar (VERB)

### 26. maputo

**Lexeme 1: Maputo**, PROPN
- grammar: number Sing; note usado sem artigo
- said: /ma.ˈpu.tu/ (pt-BR), /mɐ.ˈpu.tu/ (pt-PT)
  1. [B1] capital e maior cidade de Moçambique, situada no sul do país
     - example: Nunca estive em Maputo, mas quero muito conhecer a cidade.

### 27. extinguir

**Lexeme 1: extinguir**, VERB
- grammar: conjugation 3; valency Tr; irregular False; note gu becomes g before a and o (extingo, extinga); two past participles, extinguido and extinto
- said: /is.tʃĩ.ˈɡiɾ/ (pt-BR), /ɐjʃ.tĩ.ˈɡiɾ/ (pt-PT), /iʃ.tĩ.ˈɡiɾ/ (pt-PT)
- forms: extingo [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /is.ˈtʃĩ.ɡu/ /ɐjʃ.ˈtĩ.ɡu/; extingues [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin] /is.ˈtʃĩ.ɡis/ /ɐjʃ.ˈtĩ.ɡɨʃ/; extingue [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin] /is.ˈtʃĩ.ɡi/ /ɐjʃ.ˈtĩ.ɡɨ/; extinguimos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin] /is.tʃĩ.ˈɡi.mus/ /ɐjʃ.tĩ.ˈɡi.muʃ/; extinguem [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin] /is.ˈtʃĩ.ɡẽj/ /ɐjʃ.ˈtĩ.ɡɐ̃j/; extingui [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin] /is.tʃĩ.ˈɡi/ /ɐjʃ.tĩ.ˈɡi/; extinguiu [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] /is.tʃĩ.ˈɡiw/ /ɐjʃ.tĩ.ˈɡiw/; extinga [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin] /is.ˈtʃĩ.ɡɐ/ /ɐjʃ.ˈtĩ.ɡɐ/; extinguido [VerbForm=Part, Tense=Past] /is.tʃĩ.ˈɡi.du/ /ɐjʃ.tĩ.ˈɡi.du/ (with ter and haver); extinto [VerbForm=Part, Tense=Past] /is.ˈtʃĩ.tu/ /ɐjʃ.ˈtĩ.tu/ (with ser and estar)
  1. [B2] fazer com que um fogo ou uma luz deixe de arder
     - example: Os bombeiros levaram horas para extinguir o incêndio na floresta.
     - frames: extinguir algo
     - opposites: acender
  2. [B2] pôr fim a algo, fazendo com que deixe de existir
     - example: O governo pretende extinguir esse imposto no próximo ano.
     - frames: extinguir algo
     - labels: formality formal
  3. [B2] deixar de existir por completo, desaparecer
     - example: Muitas espécies podem extinguir-se se nada for feito.
     - frames: extinguir-se
     - grammar: valency Refl; note the pronoun does not mean oneself: the subject itself disappears

### 28. tipo

**Lexeme 1: tipo**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈtʃi.pu/ (pt-BR), /ˈti.pu/ (pt-PT)
  1. [A2] grupo de coisas ou pessoas com características iguais; espécie, género
     - example: Que tipo de música costumas ouvir quando estás sozinho?
     - frames: tipo de + substantivo
  2. [B1] pessoa, geralmente homem, que não se conhece ou não se quer nomear
     - example: Estava um tipo esquisito à porta à tua espera.
     - labels: formality informal

### 29. hepatite

**Lexeme 1: hepatite**, NOUN
- grammar: gender Fem; countability Both
- said: /e.pa.ˈtʃi.tʃi/ (pt-BR), /ɛ.pɐ.ˈti.tɨ/ (pt-PT)
  1. [B2] inflamação do fígado, geralmente causada por um vírus
     - example: Muitas pessoas têm hepatite e nem sabem disso.

### 30. destacamento

**Lexeme 1: destacamento**, NOUN
- grammar: gender Masc; countability Count
- said: /des.ta.ka.ˈmẽ.tu/ (pt-BR), /dɨʃ.tɐ.kɐ.ˈmẽ.tu/ (pt-PT)
  1. [C1] grupo de soldados ou agentes separado do conjunto para uma missão ou um lugar
     - example: Um pequeno destacamento ficou a guardar a ponte durante a noite.
     - labels: domain military
  2. [C1] envio temporário de um funcionário para trabalhar noutro serviço ou lugar
     - example: Pedi destacamento para uma escola mais perto de casa.
     - labels: formality formal
     - regions: pt-PT mainly
- relations: derived_from destacar (VERB)

### 31. tupi

**Lexeme 1: tupi**, NOUN
- grammar: gender Masc; countability Mass; number Sing
- said: /tu.ˈpi/ (pt-BR), /tu.ˈpi/ (pt-PT)
  1. [B2] língua indígena antiga, muito falada no litoral do Brasil colonial
     - example: Muitos nomes de rios e cidades brasileiras vêm do tupi.
**Lexeme 2: tupi**, NOUN
- grammar: gender Com; countability Count
- said: /tu.ˈpi/ (pt-BR), /tu.ˈpi/ (pt-PT)
  1. [B2] pessoa de um povo indígena que vivia no litoral do Brasil
     - example: Quando os portugueses chegaram, os tupis ocupavam grande parte da costa.
**Lexeme 3: tupi**, ADJ
- grammar: note same form for masculine and feminine; plural tupis
- said: /tu.ˈpi/ (pt-BR), /tu.ˈpi/ (pt-PT)
  1. [B2] que pertence a esse povo indígena brasileiro ou à sua língua
     - example: A palavra abacaxi tem origem tupi, como tantas outras do português.

### 32. normal

**Lexeme 1: normal**, ADJ
- grammar: comparable True; note same form for masculine and feminine; plural normais
- said: /noɾ.ˈmaw/ (pt-BR), /nuɾ.ˈmal/ (pt-PT)
  1. [A2] que acontece como de costume, sem surpresa, conforme o esperado
     - example: É normal sentir algum nervosismo antes de uma entrevista.
     - frames: é normal + inf.; é normal que + conj.
     - opposites: anormal
  2. [A2] comum, sem nada de especial ou diferente dos outros
     - example: Hoje foi um dia normal, sem grandes novidades no trabalho.
     - opposites: especial
- relations: adjective_of norma (NOUN)

### 33. equidade

**Lexeme 1: equidade**, NOUN
- grammar: gender Fem; countability Mass
- said: /e.kwi.ˈda.dʒi/ (pt-BR), /e.kwi.ˈda.dɨ/ (pt-PT), /e.ki.ˈda.dʒi/ (pt-BR), /e.ki.ˈda.dɨ/ (pt-PT)
- spellings: equidade (pt-BR, pt-PT); eqüidade (pt-BR) [older books only]
  1. [C1] tratamento justo e imparcial que respeita o direito de cada pessoa
     - example: Queremos uma escola que trate todos os alunos com equidade.
     - frames: com equidade; equidade entre pessoas ou grupos
     - labels: formality formal
  2. [C2] critério de justiça que adapta a lei às circunstâncias de cada caso
     - example: O juiz decidiu por equidade, já que a lei nada previa.
     - labels: domain law

### 34. inferioridade

**Lexeme 1: inferioridade**, NOUN
- grammar: gender Fem; countability Mass
- said: /ĩ.fe.ɾjo.ɾi.ˈda.dʒi/ (pt-BR), /ĩ.fɨ.ɾju.ɾi.ˈda.dɨ/ (pt-PT)
  1. [B2] posição, valor ou qualidade mais baixa em comparação com outra pessoa ou coisa
     - example: Desde criança, sentia certa inferioridade diante dos colegas mais velhos.
     - frames: inferioridade em relação a algo ou alguém; em inferioridade numérica
     - opposites: superioridade
- relations: abstract_noun_of inferior (ADJ)

### 35. idêntico

**Lexeme 1: idêntico**, ADJ
- said: /i.ˈdẽ.tʃi.ku/ (pt-BR), /i.ˈdẽ.ti.ku/ (pt-PT)
  1. [B1] exatamente igual a outro, sem nenhuma diferença
     - example: O teu casaco é idêntico ao que comprei no ano passado.
     - frames: idêntico a algo
     - opposites: diferente

### 36. casamento

**Lexeme 1: casamento**, NOUN
- grammar: gender Masc; countability Count
- said: /ka.za.ˈmẽ.tu/ (pt-BR), /kɐ.zɐ.ˈmẽ.tu/ (pt-PT)
  1. [A2] festa e ritual em que duas pessoas se unem oficialmente como família
     - example: Fomos convidados para o casamento da minha prima em junho.
  2. [B1] união legal e afetiva entre duas pessoas que vivem juntas
     - example: O nosso casamento já dura mais de vinte anos.
     - opposites: divórcio
  3. [C1] combinação harmoniosa de duas coisas diferentes
     - example: Este vinho faz um ótimo casamento com queijos fortes.
     - frames: casamento de algo com algo
     - labels: figurative True
- relations: verbal_noun_of casar (VERB)

### 37. afogamento

**Lexeme 1: afogamento**, NOUN
- grammar: gender Masc; countability Both
- said: /a.fo.ɡa.ˈmẽ.tu/ (pt-BR), /ɐ.fu.ɡɐ.ˈmẽ.tu/ (pt-PT)
  1. [B2] asfixia causada pela entrada de água ou outro líquido nos pulmões
     - example: O afogamento é uma das principais causas de morte entre crianças.
- relations: verbal_noun_of afogar (VERB)

### 38. patrono

**Lexeme 1: patrono**, NOUN
- grammar: gender Masc; countability Count; note feminine patrona
- said: /pa.ˈtɾo.nu/ (pt-BR), /pɐ.ˈtɾo.nu/ (pt-PT)
  1. [B2] pessoa que protege, apoia ou financia alguém, uma causa ou uma atividade
     - example: Muitos pintores viviam graças ao dinheiro de um patrono generoso.
     - frames: patrono de algo ou alguém
  2. [B2] santo ou pessoa ilustre escolhida como protetora ou modelo de um grupo
     - example: O patrono da nossa turma fez um discurso emocionante na formatura.
     - frames: patrono de algo
  3. [C1] advogado que representa uma das partes num processo
     - example: O patrono do réu pediu o adiamento da audiência.
     - labels: domain law, formality formal
- relations: confusable patrão (NOUN): patrão is an employer or boss; patrono is a protector, honoured figure or lawyer

### 39. assim

**Lexeme 1: assim**, ADV
- said: /a.ˈsĩ/ (pt-BR), /ɐ.ˈsĩ/ (pt-PT)
  1. [A1] desta maneira, deste modo, como se mostra ou se diz
     - example: Segure a raquete assim, com a mão um pouco mais firme.
  2. [B1] por esse motivo; usado para introduzir uma consequência ou conclusão
     - example: Choveu muito durante a noite; assim, o jogo foi adiado.
  3. [A2] deste tipo, semelhante ao que se mostra ou se descreve
     - example: Nunca vi uma tempestade assim em toda a minha vida.

### 40. ciência

**Lexeme 1: ciência**, NOUN
- grammar: gender Fem; countability Both
- said: /si.ˈẽ.sjɐ/ (pt-BR), /ˈsjẽ.sjɐ/ (pt-PT)
  1. [B1] conjunto de conhecimentos obtidos pela observação, experiência e estudo rigoroso do mundo
     - example: A ciência ainda não explica todos os mistérios do universo.
     - grammar: countability Mass
  2. [B1] cada área de estudo com objeto e métodos próprios, como a física
     - example: Na universidade, ela quer estudar ciências sociais ou economia.
     - grammar: countability Count
  3. [C1] conhecimento ou informação que alguém tem sobre um assunto
     - example: Você tem ciência dos riscos deste tratamento?
     - frames: ter ciência de algo; tomar ciência de algo; dar ciência a alguém de algo
     - labels: formality formal
     - grammar: countability Mass

### 41. gaiola

**Lexeme 1: gaiola**, NOUN
- grammar: gender Fem; countability Count
- said: /ɡaj.ˈɔ.lɐ/ (pt-BR), /ɡɐj.ˈɔ.lɐ/ (pt-PT)
  1. [A2] caixa com grades onde se prendem pássaros ou outros pequenos animais
     - example: Todas as manhãs, o canário canta dentro da gaiola.
  2. [B2] lugar ou situação em que alguém se sente preso, sem liberdade
     - example: Para ela, aquele casamento rico era uma gaiola dourada.
     - labels: figurative True

### 42. rentabilidade

**Lexeme 1: rentabilidade**, NOUN
- grammar: gender Fem; countability Both
- said: /ʁẽ.ta.bi.li.ˈda.dʒi/ (pt-BR), /ʁẽ.tɐ.bi.li.ˈda.dɨ/ (pt-PT)
  1. [B2] capacidade de dar lucro em relação ao que se gastou ou investiu
     - example: A empresa precisa aumentar a rentabilidade para continuar no mercado.
     - labels: domain economics
     - grammar: countability Mass
  2. [C1] ganho que um investimento produz, medido em relação ao valor aplicado
     - example: Este ano, a rentabilidade da poupança ficou abaixo da inflação.
     - labels: domain finance
     - grammar: countability Count
- relations: abstract_noun_of rentável (ADJ)

### 43. despertador

**Lexeme 1: despertador**, NOUN
- grammar: gender Masc; countability Count
- said: /des.peɾ.ta.ˈdoɾ/ (pt-BR), /dɨʃ.pɨɾ.tɐ.ˈdoɾ/ (pt-PT), /dʒis.peɾ.ta.ˈdoɾ/ (pt-BR, casual)
  1. [A2] relógio que toca um som a uma hora marcada para acordar alguém
     - example: Ponho sempre o despertador para as sete da manhã.
     - frames: pôr o despertador para uma hora
- relations: derived_from despertar (VERB)

### 44. agosto

**Lexeme 1: agosto**, NOUN
- grammar: gender Masc; countability Count
- said: /a.ˈɡos.tu/ (pt-BR), /ɐ.ˈɡoʃ.tu/ (pt-PT)
  1. [A1] oitavo mês do ano, entre julho e setembro
     - example: Muitas famílias tiram férias em agosto, quando faz mais calor.
     - frames: em agosto; no dia X de agosto

### 45. ajoelhar

**Lexeme 1: ajoelhar**, VERB
- grammar: conjugation 1; irregular False; valency Both; note mostly used as ajoelhar-se; the form without se is also common, especially in Brazil
- said: /a.ʒo.e.ˈʎaɾ/ (pt-BR), /ɐ.ʒu.ɨ.ˈʎaɾ/ (pt-PT)
  1. [B1] pôr-se com os joelhos apoiados no chão
     - example: Os fiéis ajoelham-se quando começa a oração.
     - frames: ajoelhar-se; ajoelhar(-se) diante de alguém ou algo
     - grammar: valency Refl
  2. [C1] aceitar a autoridade ou a vontade de alguém, perdendo a dignidade
     - example: Este povo nunca vai se ajoelhar perante um invasor.
     - frames: ajoelhar-se perante alguém
     - labels: figurative True
     - grammar: valency Refl
- relations: derived_from joelho (NOUN)

### 46. intérprete

**Lexeme 1: intérprete**, NOUN
- grammar: gender Com; countability Count
- said: /ĩ.ˈtɛɾ.pɾe.tʃi/ (pt-BR), /ĩ.ˈtɛɾ.pɾɨ.tɨ/ (pt-PT)
  1. [B1] pessoa que traduz em voz alta, de uma língua para outra, o que alguém diz
     - example: Precisamos de um intérprete para falar com os turistas japoneses.
  2. [B2] artista que canta uma música ou representa uma personagem
     - example: Ela é a intérprete original dessa canção tão famosa.
     - frames: intérprete de algo
  3. [C1] pessoa que explica o sentido de textos, leis ou sinais
     - example: Cabe aos juízes ser os intérpretes da lei e da Constituição.
     - frames: intérprete de algo

### 47. destaque

**Lexeme 1: destaque**, NOUN
- grammar: gender Masc; countability Both
- said: /des.ˈta.ki/ (pt-BR), /dɨʃ.ˈta.kɨ/ (pt-PT)
  1. [B1] importância especial que faz algo ser mais notado do que o resto
     - example: O jornal deu grande destaque à notícia na primeira página.
     - frames: dar destaque a algo; em destaque; de destaque
     - grammar: countability Mass
  2. [B2] pessoa ou coisa que chama mais a atenção num grupo ou evento
     - example: A jovem atacante foi o grande destaque do jogo de ontem.
     - frames: o destaque de algo
     - grammar: countability Count
- relations: derived_from destacar (VERB)

### 48. junho

**Lexeme 1: junho**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈʒũ.ɲu/ (pt-BR), /ˈʒu.ɲu/ (pt-PT)
  1. [A1] o sexto mês do ano, entre maio e julho
     - example: As nossas férias de verão começam no fim de junho.
     - frames: em junho

### 49. brindar

**Lexeme 1: brindar**, VERB
- grammar: conjugation 1; valency Both; irregular False
- said: /bɾĩ.ˈdaɾ/ (pt-BR), /bɾĩ.ˈdaɾ/ (pt-PT)
  1. [B1] levantar o copo e beber em honra de alguém ou de algo
     - example: Vamos brindar aos noivos e à felicidade deles!
     - frames: brindar a alguém ou algo; brindar por algo
     - grammar: valency Intr
  2. [B2] dar algo a alguém como presente ou oferta agradável
     - example: A loja brinda os clientes com um desconto especial no Natal.
     - frames: brindar alguém com algo; brindar algo a alguém
     - grammar: valency Tr

### 50. opressão

**Lexeme 1: opressão**, NOUN
- grammar: gender Fem; countability Both
- said: /o.pɾe.ˈsɐ̃w/ (pt-BR), /ɔ.pɾɨ.ˈsɐ̃w/ (pt-PT)
  1. [B2] uso cruel e injusto do poder sobre pessoas ou povos
     - example: Durante anos, o povo lutou contra a opressão do regime.
     - grammar: countability Mass
     - opposites: liberdade
  2. [C1] sensação de peso ou aperto no peito, com dificuldade em respirar
     - example: Sinto uma opressão no peito sempre que subo escadas.
     - grammar: countability Count
- relations: verbal_noun_of oprimir (VERB)

### 51. excessivo

**Lexeme 1: excessivo**, ADJ
- grammar: comparable True
- said: /e.se.ˈsi.vu/ (pt-BR), /ɐjʃ.sɨ.ˈsi.vu/ (pt-PT)
  1. [B1] que passa da medida normal ou do que é razoável
     - example: Evite o uso excessivo de sal na comida das crianças.
     - opposites: moderado
- relations: adjective_of excesso (NOUN)

### 52. celta

**Lexeme 1: celta**, ADJ
- grammar: comparable False; note same form for masculine and feminine
- said: /ˈsɛw.tɐ/ (pt-BR), /ˈsɛl.tɐ/ (pt-PT)
  1. [B2] dos antigos povos indo-europeus da Europa central e ocidental, ou da sua cultura
     - example: No museu há várias joias celtas encontradas perto do rio.
**Lexeme 2: celta**, NOUN
- grammar: gender Com; countability Count
- said: /ˈsɛw.tɐ/ (pt-BR), /ˈsɛl.tɐ/ (pt-PT)
  1. [B2] pessoa dos antigos povos indo-europeus da Europa central e ocidental
     - example: Os celtas deixaram muitos vestígios nesta região do norte.
**Lexeme 3: celta**, NOUN
- grammar: gender Masc; countability Mass
- said: /ˈsɛw.tɐ/ (pt-BR), /ˈsɛl.tɐ/ (pt-PT)
  1. [C1] grupo de línguas indo-europeias faladas outrora em grande parte da Europa ocidental
     - example: Muitos nomes de rios desta zona vêm do celta.
     - labels: domain linguistics

### 53. isqueiro

**Lexeme 1: isqueiro**, NOUN
- grammar: gender Masc; countability Count
- said: /is.ˈkej.ɾu/ (pt-BR), /iʃ.ˈkɐj.ɾu/ (pt-PT), /is.ˈke.ɾu/ (pt-BR, casual)
  1. [B1] pequeno objeto de bolso que produz uma chama para acender cigarros ou fogo
     - example: Esqueci o isqueiro e não consigo acender a lareira.

### 54. penitência

**Lexeme 1: penitência**, NOUN
- grammar: gender Fem; countability Both
- said: /pe.ni.ˈtẽ.sjɐ/ (pt-BR), /pɨ.ni.ˈtẽ.sjɐ/ (pt-PT)
  1. [B2] sacrifício ou castigo que alguém cumpre para pagar pelos seus pecados
     - example: O padre deu-lhe como penitência rezar dez ave-marias.
     - frames: fazer penitência; cumprir uma penitência
     - labels: domain religion
  2. [C1] arrependimento sincero pelos pecados ou faltas cometidas
     - example: Viveu os últimos anos em silêncio e penitência.
     - labels: style literary
     - grammar: countability Mass
  3. [C1] coisa aborrecida ou difícil que é preciso suportar
     - example: Para mim, aquelas reuniões de segunda-feira são uma verdadeira penitência.
     - labels: figurative True, formality informal
     - grammar: countability Count

### 55. defensivo

**Lexeme 1: defensivo**, ADJ
- grammar: comparable True
- said: /de.fẽ.ˈsi.vu/ (pt-BR), /dɨ.fẽ.ˈsi.vu/ (pt-PT)
  1. [B2] que serve para proteger contra um ataque ou para evitar perder
     - example: A equipa adotou uma tática defensiva durante a segunda parte.
     - opposites: ofensivo
  2. [B2] que reage a uma crítica ou pergunta como se fosse atacado
     - example: Você fica sempre defensivo quando falamos do seu trabalho.
**Lexeme 2: defensivo**, NOUN
- grammar: gender Masc; countability Count
- said: /de.fẽ.ˈsi.vu/ (pt-BR), /dɨ.fẽ.ˈsi.vu/ (pt-PT)
  1. [C1] produto químico usado para proteger plantações contra pragas e doenças
     - example: Os agricultores usam menos defensivos nas lavouras orgânicas.
     - frames: defensivo agrícola
     - regions: pt-BR only

### 56. redenção

**Lexeme 1: redenção**, NOUN
- grammar: gender Fem; countability Mass
- said: /ʁe.dẽ.ˈsɐ̃w/ (pt-BR), /ʁɨ.dẽ.ˈsɐ̃w/ (pt-PT)
  1. [C1] salvação do ser humano do pecado, segundo a fé cristã
     - example: Muitos fiéis rezam pela redenção dos seus pecados antes de morrer.
     - frames: redenção de algo
     - labels: domain religion
  2. [C1] recuperação da honra ou do valor depois de um erro ou fracasso
     - example: Para o atacante, aquele gol foi a redenção depois de meses difíceis.
     - labels: figurative True
- relations: verbal_noun_of redimir (VERB)

### 57. certeza

**Lexeme 1: certeza**, NOUN
- grammar: gender Fem; countability Both; note pt-BR ter certeza; pt-PT usually ter a certeza
- said: /seʁ.ˈte.zɐ/ (pt-BR), /sɨɾ.ˈte.zɐ/ (pt-PT)
  1. [A2] conhecimento firme de que algo é verdadeiro, sem nenhuma dúvida
     - example: Você tem certeza de que fechou a porta de casa?
     - frames: ter (a) certeza de algo; ter (a) certeza de que; com certeza
     - grammar: countability Mass
     - opposites: dúvida, incerteza
  2. [B1] coisa ou fato que se sabe ser verdadeiro e seguro
     - example: Na vida, poucas coisas são certezas absolutas.
     - grammar: countability Count
- relations: abstract_noun_of certo (ADJ)

### 58. mónica

**Lexeme 1: Mónica**, PROPN
- grammar: gender Fem; number Sing
- said: /ˈmõ.ni.kɐ/ (pt-BR, Mônica), /ˈmɔ.ni.kɐ/ (pt-PT, Mónica)
- spellings: Mónica (pt-PT); Mônica (pt-BR)
  1. [A1] nome próprio dado a pessoas do sexo feminino
     - example: Na turma da minha filha há três meninas chamadas Mónica.

### 59. unidade

**Lexeme 1: unidade**, NOUN
- grammar: gender Fem; countability Count
- said: /u.ni.ˈda.dʒi/ (pt-BR), /u.ni.ˈda.dɨ/ (pt-PT)
  1. [B1] quantidade fixa usada como padrão para medir outras
     - example: O metro é a unidade de comprimento mais usada no mundo.
     - frames: unidade de + medida
  2. [B1] cada objeto ou peça de um conjunto de coisas iguais
     - example: Cada unidade deste sabonete custa dois euros na farmácia.
  3. [B2] parte de uma empresa ou serviço que funciona em local próprio
     - example: A empresa vai abrir uma nova unidade no norte do país.
  4. [B2] união e acordo entre as pessoas de um grupo, sem divisões
     - example: Sem unidade dentro do partido, não vamos ganhar estas eleições.
     - grammar: countability Mass
     - opposites: divisão
  5. [A2] cada uma das partes em que se divide um livro ou curso
     - example: Hoje vamos começar a unidade cinco do manual de inglês.

### 60. enfermeiro

**Lexeme 1: enfermeiro**, NOUN
- grammar: gender Masc; countability Count
- said: /ẽ.feɾ.ˈmej.ɾu/ (pt-BR), /ẽ.fɨɾ.ˈmɐj.ɾu/ (pt-PT)
  1. [A2] profissional de saúde que cuida de doentes, sobretudo em hospitais
     - example: Durante a noite, o enfermeiro passou várias vezes pelo quarto.

### 61. inquietação

**Lexeme 1: inquietação**, NOUN
- grammar: gender Fem; countability Both
- said: /ĩ.kje.ta.ˈsɐ̃w/ (pt-BR), /ĩ.kjɨ.tɐ.ˈsɐ̃w/ (pt-PT)
  1. [B2] sentimento de preocupação e nervosismo diante do que pode acontecer
     - example: A demora das notícias causava grande inquietação em toda a família.
     - grammar: countability Mass
     - opposites: tranquilidade
  2. [B2] agitação de quem não consegue ficar parado ou sossegado
     - example: Notei a inquietação das crianças nos últimos minutos da aula.
     - grammar: countability Mass
  3. [C1] questão ou dúvida que ocupa o pensamento e pede resposta
     - example: Este livro nasceu das minhas inquietações sobre o futuro do planeta.
     - grammar: countability Count; note often in the plural
- relations: verbal_noun_of inquietar (VERB)

### 62. abaixo

**Lexeme 1: abaixo**, ADV
- grammar: invariable True
- said: /a.ˈbaj.ʃu/ (pt-BR), /ɐ.ˈbaj.ʃu/ (pt-PT)
  1. [B1] em lugar ou posição mais baixa; em direção ao chão
     - example: O barco seguiu rio abaixo durante toda a manhã.
     - frames: abaixo de algo; rio/rua/escada abaixo
     - opposites: acima
  2. [A2] mais adiante num texto, página ou documento
     - example: Preencha o formulário abaixo com os seus dados pessoais.
     - opposites: acima
  3. [B1] em valor, grau ou categoria inferior a outro
     - example: Esta noite a temperatura vai ficar abaixo de zero.
     - frames: abaixo de algo
     - opposites: acima
- relations: derived_from baixo (ADJ)
**Lexeme 2: abaixo**, INTJ
- said: /a.ˈbaj.ʃu/ (pt-BR), /ɐ.ˈbaj.ʃu/ (pt-PT)
  1. [B2] grito de protesto que pede o fim ou a queda de algo
     - example: Na praça, a multidão gritava: abaixo a corrupção!
     - frames: abaixo + subst.

### 63. leque

**Lexeme 1: leque**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈlɛ.ki/ (pt-BR), /ˈlɛ.kɨ/ (pt-PT)
  1. [B2] objeto dobrável que se abre e se agita para fazer vento no rosto
     - example: Está tanto calor que vou levar um leque para o concerto.
  2. [B2] conjunto variado de coisas, opções ou possibilidades à escolha
     - example: A universidade oferece um amplo leque de cursos noturnos.
     - frames: leque de + substantivo
     - labels: figurative True

### 64. ema

**Lexeme 1: ema**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈe.mɐ/ (pt-BR), /ˈe.mɐ/ (pt-PT), /ˈẽ.mɐ/ (pt-BR)
  1. [B2] grande ave sul-americana que não voa, de pernas longas e penas cinzentas
     - example: Vimos uma ema correndo pelo campo perto da estrada.
     - regions: pt-BR mainly
  2. [B2] grande ave australiana que não voa, de pernas longas e pescoço comprido
     - example: No jardim zoológico há duas emas muito curiosas.
     - regions: pt-PT mainly

### 65. travesti

**Lexeme 1: travesti**, NOUN
- grammar: gender Com; countability Count; note In pt-BR the gender-identity sense is used in the feminine (a travesti); sense 2 is usually masculine.
- said: /tɾa.ves.ˈtʃi/ (pt-BR), /tɾɐ.vɨʃ.ˈti/ (pt-PT)
  1. [B2] pessoa designada homem ao nascer que vive uma identidade de gênero feminina
     - example: Ela se identifica como travesti e luta pelos direitos das pessoas trans.
  2. [B2] homem que usa roupas e aparência de mulher, sobretudo em espetáculos
     - example: Naquela comédia antiga, o ator fazia o papel de um travesti.

### 66. erroneamente

**Lexeme 1: erroneamente**, ADV
- said: /e.ʁo.ni.a.ˈmẽ.tʃi/ (pt-BR), /i.ʁɔ.ni.ɐ.ˈmẽ.tɨ/ (pt-PT)
  1. [B2] de modo incorreto ou contrário à verdade dos factos
     - example: Muitas pessoas acreditam erroneamente que o álcool aquece o corpo.
     - opposites: corretamente
- relations: adverb_of errôneo (ADJ)

### 67. espírito

**Lexeme 1: espírito**, NOUN
- grammar: gender Masc; countability Count
- said: /is.ˈpi.ɾi.tu/ (pt-BR), /ɨʃ.ˈpi.ɾi.tu/ (pt-PT)
  1. [B1] parte não material do ser humano, que pensa e sente
     - example: Muitas religiões acreditam que o espírito sobrevive à morte do corpo.
     - opposites: corpo
  2. [B1] ser sobrenatural sem corpo, como a alma de um morto
     - example: Dizem que nesta casa antiga aparecem espíritos à meia-noite.
  3. [B2] atitude, modo de pensar ou disposição de uma pessoa ou grupo
     - example: Precisamos de mais espírito de colaboração entre os colegas.
     - frames: espírito de + subst.
  4. [C1] sentido ou intenção principal de uma lei, texto ou época
     - example: O juiz decidiu respeitando o espírito da lei, não a letra.

### 68. otomano

**Lexeme 1: otomano**, ADJ
- grammar: comparable False
- said: /o.to.ˈmɐ̃.nu/ (pt-BR), /ɔ.tu.ˈmɐ.nu/ (pt-PT)
  1. [B2] do antigo império turco, que existiu do século XIV ao início do XX
     - example: Durante séculos, o exército otomano controlou grande parte dos Bálcãs.
**Lexeme 2: otomano**, NOUN
- grammar: gender Masc; countability Count; note feminine otomana
- said: /o.to.ˈmɐ̃.nu/ (pt-BR), /ɔ.tu.ˈmɐ.nu/ (pt-PT)
  1. [C1] pessoa que vivia no antigo império turco ou era súdita dele
     - example: Em 1453, os otomanos conquistaram a cidade de Constantinopla.

### 69. automação

**Lexeme 1: automação**, NOUN
- grammar: gender Fem; countability Both
- said: /aw.to.ma.ˈsɐ̃w/ (pt-BR), /aw.tu.mɐ.ˈsɐ̃w/ (pt-PT)
  1. [B2] uso de máquinas ou programas para fazer tarefas sem trabalho humano
     - example: A automação das fábricas mudou o trabalho de milhares de pessoas.

### 70. farmacêutico

**Lexeme 1: farmacêutico**, NOUN
- grammar: gender Masc; countability Count; note feminino: farmacêutica
- said: /faɾ.ma.ˈsew.tʃi.ku/ (pt-BR), /fɐɾ.mɐ.ˈsew.ti.ku/ (pt-PT)
  1. [B1] profissional formado que prepara, controla e vende medicamentos
     - example: Pergunte ao farmacêutico como deve tomar este remédio.
- relations: derived_from farmácia (NOUN)
**Lexeme 2: farmacêutico**, ADJ
- grammar: comparable False
- said: /faɾ.ma.ˈsew.tʃi.ku/ (pt-BR), /fɐɾ.mɐ.ˈsew.ti.ku/ (pt-PT)
  1. [B2] ligado à produção, ao estudo ou à venda de medicamentos
     - example: A indústria farmacêutica investe muito dinheiro em novas vacinas.
- relations: adjective_of farmácia (NOUN)

### 71. assunto

**Lexeme 1: assunto**, NOUN
- grammar: gender Masc; countability Count
- said: /a.ˈsũ.tu/ (pt-BR), /ɐ.ˈsũ.tu/ (pt-PT)
  1. [A2] aquilo de que se fala ou escreve numa conversa ou texto
     - example: Vamos mudar de assunto, que esta conversa já cansou.
     - frames: assunto de algo; mudar de assunto
  2. [B1] questão ou problema que alguém precisa de tratar ou resolver
     - example: Tenho um assunto pessoal para resolver no banco amanhã.

### 72. gigantesco

**Lexeme 1: gigantesco**, ADJ
- said: /ʒi.ɡɐ̃.ˈtes.ku/ (pt-BR), /ʒi.ɡɐ̃.ˈteʃ.ku/ (pt-PT)
  1. [B1] muito grande, de tamanho ou quantidade fora do comum
     - example: Hoje há uma fila gigantesca à porta do museu.
     - opposites: minúsculo
- relations: adjective_of gigante (NOUN)

### 73. natureza

**Lexeme 1: natureza**, NOUN
- grammar: gender Fem; countability Both
- said: /na.tu.ˈɾe.zɐ/ (pt-BR), /nɐ.tu.ˈɾe.zɐ/ (pt-PT)
  1. [A2] mundo das plantas, dos animais e das paisagens, sem intervenção humana
     - example: Nas férias, gostamos de passear no meio da natureza.
     - grammar: countability Mass; number Sing
  2. [B1] conjunto das qualidades essenciais de uma coisa; o seu tipo ou carácter
     - example: Ninguém percebeu a verdadeira natureza daquele acordo entre as empresas.
     - frames: de natureza + adj
     - grammar: countability Count
  3. [B1] maneira de ser e temperamento próprio de uma pessoa
     - example: Ela é curiosa por natureza e pergunta sempre tudo.
     - frames: por natureza
     - grammar: countability Count

### 74. razão

**Lexeme 1: razão**, NOUN
- grammar: gender Fem; countability Both
- said: /ʁa.ˈzɐ̃w/ (pt-BR), /ʁɐ.ˈzɐ̃w/ (pt-PT)
- forms: razões [Number=Plur] /ʁa.ˈzõjs/ /ʁɐ.ˈzõjʃ/
  1. [A2] motivo ou causa que explica por que algo acontece ou se faz
     - example: Não vejo nenhuma razão para mudarmos de plano agora.
     - frames: razão de/para algo; por essa razão
     - grammar: countability Count
  2. [A2] qualidade de quem está certo numa opinião ou numa discussão
     - example: Você tem razão, eu estava errado sobre o horário.
     - frames: ter razão; dar razão a alguém
     - grammar: countability Mass; number Sing
  3. [B2] capacidade humana de pensar, compreender e julgar com lógica
     - example: Nas decisões difíceis, convém ouvir a razão e não só o coração.
     - grammar: countability Mass; number Sing

### 75. torpedo

**Lexeme 1: torpedo**, NOUN
- grammar: gender Masc; countability Count
- said: /toɾ.ˈpe.du/ (pt-BR), /tuɾ.ˈpe.du/ (pt-PT)
  1. [B2] arma explosiva comprida que se move debaixo de água até atingir o alvo
     - example: O submarino lançou dois torpedos contra o navio inimigo.
  2. [B2] mensagem de texto enviada de um telefone celular para outro
     - example: Mande um torpedo quando chegar em casa, por favor.
     - labels: formality informal, time dated
     - regions: pt-BR only
  3. [C2] peixe de corpo achatado e redondo que dá descargas elétricas
     - example: Os pescadores evitam tocar no torpedo preso na rede.
     - labels: domain zoology, rare True

### 76. evo

**Lexeme 1: evo**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈɛ.vu/ (pt-BR), /ˈɛ.vu/ (pt-PT)
  1. [C2] duração sem princípio nem fim; tempo que nunca acaba
     - example: Por todo o evo, as estrelas continuarão a brilhar sobre nós.
     - labels: style literary, rare True
     - grammar: countability Mass
  2. [C2] período de tempo muito longo; idade ou época antiga
     - example: Naqueles evos distantes, os homens ainda viviam em cavernas.
     - labels: style poetic, rare True
     - grammar: countability Count

### 77. seguradora

**Lexeme 1: seguradora**, NOUN
- grammar: gender Fem; countability Count
- said: /se.ɡu.ɾa.ˈdo.ɾɐ/ (pt-BR), /sɨ.ɡu.ɾɐ.ˈdo.ɾɐ/ (pt-PT)
  1. [B1] empresa que, em troca de pagamentos regulares, cobre prejuízos e danos dos clientes
     - example: Depois do acidente, liguei logo para a seguradora do carro.
- relations: derived_from segurar (VERB)

### 78. partir

**Lexeme 1: partir**, VERB
- grammar: conjugation 3; irregular False; valency Both
- said: /paɾ.ˈtʃiɾ/ (pt-BR), /pɐɾ.ˈtiɾ/ (pt-PT)
  1. [A2] ir embora de um lugar, começar uma viagem
     - example: Amanhã nós partimos bem cedo para a praia.
     - frames: partir para + lugar; partir de + lugar
     - grammar: valency Intr
     - opposites: chegar
  2. [B1] quebrar alguma coisa em pedaços, com força ou por acidente
     - example: O vento forte partiu vários galhos da árvore.
     - grammar: valency Tr
  3. [B1] dividir alguma coisa em pedaços ou porções
     - example: Vou partir o bolo em oito pedaços iguais.
     - frames: partir algo em + partes
     - grammar: valency Tr
  4. [B2] tomar uma ideia como base para pensar ou agir
     - example: Partimos do princípio de que todos estão de acordo.
     - frames: partir de algo
     - grammar: valency Intr

### 79. ambientalista

**Lexeme 1: ambientalista**, NOUN
- grammar: gender Com; countability Count
- said: /ɐ̃.bi.ẽ.ta.ˈlis.tɐ/ (pt-BR), /ɐ̃.bjẽ.tɐ.ˈliʃ.tɐ/ (pt-PT)
  1. [B2] pessoa que trabalha ou luta pela proteção da natureza
     - example: Os ambientalistas protestaram contra a construção da barragem no rio.
**Lexeme 2: ambientalista**, ADJ
- grammar: note same form for masculine and feminine
- said: /ɐ̃.bi.ẽ.ta.ˈlis.tɐ/ (pt-BR), /ɐ̃.bjẽ.tɐ.ˈliʃ.tɐ/ (pt-PT)
  1. [B2] que defende a proteção da natureza e combate a sua destruição
     - example: Vários grupos ambientalistas pedem leis mais duras contra o desmatamento.
- relations: confusable ambiental (ADJ): ambiental means 'of the environment' (impacto ambiental), while ambientalista means 'campaigning for the environment' (movimento ambientalista)

### 80. revitalização

**Lexeme 1: revitalização**, NOUN
- grammar: gender Fem; countability Count
- said: /ʁe.vi.ta.li.za.ˈsɐ̃w/ (pt-BR), /ʁɨ.vi.tɐ.li.zɐ.ˈsɐ̃w/ (pt-PT)
  1. [B2] recuperação de um lugar, setor ou atividade, dando-lhe novo vigor e uso
     - example: Com a revitalização do porto, o bairro voltou a atrair turistas.
     - frames: revitalização de algo
- relations: verbal_noun_of revitalizar (VERB)

### 81. estatura

**Lexeme 1: estatura**, NOUN
- grammar: gender Fem; countability Mass
- said: /is.ta.ˈtu.ɾɐ/ (pt-BR), /iʃ.tɐ.ˈtu.ɾɐ/ (pt-PT)
  1. [B2] altura de uma pessoa, medida dos pés à cabeça
     - example: Procuramos um homem de estatura média e cabelo grisalho.
     - frames: de estatura baixa/média/alta; de pequena/grande estatura
  2. [C1] importância ou valor que alguém tem pela sua capacidade ou caráter
     - example: Poucos políticos têm a estatura moral exigida por esse cargo.
     - labels: figurative True

### 82. face

**Lexeme 1: face**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈfa.si/ (pt-BR), /ˈfa.sɨ/ (pt-PT)
  1. [B1] parte da frente da cabeça, onde ficam os olhos e a boca
     - example: Lavo a face com água fria todas as manhãs.
     - frames: face a face
  2. [B1] cada um dos dois lados do rosto, abaixo dos olhos
     - example: Dei um beijo na face da minha avó.
  3. [B2] cada uma das superfícies que formam o exterior de um objeto
     - example: Um cubo tem seis faces iguais.
  4. [B2] aspecto ou lado que uma coisa ou situação apresenta
     - example: Conheci então a face mais cruel da guerra.
     - labels: figurative True

### 83. envolvente

**Lexeme 1: envolvente**, ADJ
- grammar: note same form for masculine and feminine
- said: /ẽ.vow.ˈvẽ.tʃi/ (pt-BR), /ẽ.vɔl.ˈvẽ.tɨ/ (pt-PT)
  1. [B2] que prende a atenção e o interesse de quem vê, lê ou ouve
     - example: Este romance tem uma narrativa tão envolvente que li tudo numa noite.
  2. [C1] que está em volta de algo, rodeando-o por todos os lados
     - example: Da janela vê-se toda a paisagem envolvente da aldeia.
- relations: derived_from envolver (VERB)

### 84. humorístico

**Lexeme 1: humorístico**, ADJ
- said: /u.mo.ˈɾis.tʃi.ku/ (pt-BR), /u.mu.ˈɾiʃ.ti.ku/ (pt-PT)
  1. [B1] feito para divertir e fazer rir as pessoas
     - example: Todas as semanas escrevo uma crónica humorística para o jornal local.
     - opposites: sério
- relations: adjective_of humor (NOUN)
**Lexeme 2: humorístico**, NOUN
- grammar: gender Masc; countability Count
- said: /u.mo.ˈɾis.tʃi.ku/ (pt-BR), /u.mu.ˈɾiʃ.ti.ku/ (pt-PT)
  1. [B2] programa de televisão ou rádio feito para fazer rir
     - example: Meu avô nunca perdia aquele humorístico de domingo à noite.
     - regions: pt-BR mainly

### 85. humorista

**Lexeme 1: humorista**, NOUN
- grammar: gender Com; countability Count
- said: /u.mo.ˈɾis.tɐ/ (pt-BR), /u.mu.ˈɾiʃ.tɐ/ (pt-PT)
  1. [B1] artista ou escritor que faz o público rir com piadas e textos engraçados
     - example: Minha irmã quer ser humorista e já se apresenta em bares.
- relations: derived_from humor (NOUN)

### 86. material

**Lexeme 1: material**, NOUN
- grammar: gender Masc; countability Both
- said: /ma.te.ɾi.ˈaw/ (pt-BR), /mɐ.tɨ.ɾi.ˈal/ (pt-PT)
  1. [A2] substância de que uma coisa é feita ou com que se constrói
     - example: Esta mesa é feita de um material muito resistente.
     - frames: material de + noun
     - grammar: countability Count
  2. [A2] conjunto de objetos e instrumentos necessários para uma atividade
     - example: Ainda preciso comprar o material escolar das crianças.
     - grammar: countability Mass
  3. [B1] conjunto de textos e informações usados para estudar ou trabalhar
     - example: O professor vai enviar por e-mail o material da aula.
     - grammar: countability Mass
**Lexeme 2: material**, ADJ
- grammar: note same form for masculine and feminine; plural materiais
- said: /ma.te.ɾi.ˈaw/ (pt-BR), /mɐ.tɨ.ɾi.ˈal/ (pt-PT)
  1. [B1] ligado às coisas físicas, ao corpo e ao dinheiro, não ao espírito
     - example: Para muita gente, os bens materiais não trazem felicidade.
     - opposites: espiritual
- relations: adjective_of matéria (NOUN)

### 87. santa

**Lexeme 1: santa**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈsɐ̃.tɐ/ (pt-BR), /ˈsɐ̃.tɐ/ (pt-PT)
  1. [B1] mulher reconhecida pela Igreja pela sua vida virtuosa e venerada pelos fiéis
     - example: Esta igreja tem o nome de uma santa muito antiga.
     - labels: domain religion
  2. [B2] mulher muito bondosa e paciente, incapaz de fazer mal a alguém
     - example: Aguentar aquele marido tantos anos? Só mesmo uma santa!
     - labels: figurative True, formality informal
- relations: feminine_of santo (NOUN)

### 88. tamanho

**Lexeme 1: tamanho**, NOUN
- grammar: gender Masc; countability Count
- said: /ta.ˈmɐ̃.ɲu/ (pt-BR), /tɐ.ˈmɐ.ɲu/ (pt-PT)
  1. [A2] grandeza ou dimensão de uma coisa, que pode ser maior ou menor
     - example: Esta caixa tem a medida certa? Não, o tamanho é pequeno demais.
     - frames: de tamanho + adj; do tamanho de algo
  2. [A2] medida padronizada de roupas ou sapatos, indicada por número ou letra
     - example: Você tem esta camisa num tamanho maior?
**Lexeme 2: tamanho**, ADJ
- grammar: position Attr; comparable False; note usually placed before the noun; agrees in gender and number (tamanha, tamanhos)
- said: /ta.ˈmɐ̃.ɲu/ (pt-BR), /tɐ.ˈmɐ.ɲu/ (pt-PT)
  1. [C1] tão grande ou tão forte, usado para dar ênfase a algo
     - example: Nunca vi tamanha coragem numa pessoa tão jovem.
     - labels: style literary

### 89. aperfeiçoamento

**Lexeme 1: aperfeiçoamento**, NOUN
- grammar: gender Masc; countability Both
- said: /a.peɾ.fej.so.a.ˈmẽ.tu/ (pt-BR), /ɐ.pɨɾ.fɐj.su.ɐ.ˈmẽ.tu/ (pt-PT)
  1. [B2] processo de tornar algo melhor, mais completo ou com menos defeitos
     - example: Esta técnica ainda precisa de algum aperfeiçoamento antes de ser usada.
     - frames: aperfeiçoamento de algo
  2. [B2] formação complementar que aumenta os conhecimentos e as capacidades numa profissão
     - example: Vou fazer um curso de aperfeiçoamento em gestão no próximo semestre.
     - frames: curso de aperfeiçoamento
- relations: verbal_noun_of aperfeiçoar (VERB)

### 90. desvalorização

**Lexeme 1: desvalorização**, NOUN
- grammar: gender Fem; countability Both
- said: /dʒiz.va.lo.ɾi.za.ˈsɐ̃w/ (pt-BR), /dɨʒ.vɐ.lu.ɾi.zɐ.ˈsɐ̃w/ (pt-PT)
  1. [B2] perda do poder de compra de uma moeda ou queda do preço de um bem
     - example: A desvalorização da moeda encareceu os produtos importados.
     - frames: desvalorização de algo
     - opposites: valorização
  2. [C1] atitude de não dar a alguém ou a algo a importância merecida
     - example: Muitos professores queixam-se da desvalorização da sua profissão.
     - grammar: countability Mass
     - opposites: valorização
- relations: verbal_noun_of desvalorizar (VERB)

### 91. distúrbio

**Lexeme 1: distúrbio**, NOUN
- grammar: gender Masc; countability Count
- said: /dʒis.ˈtuɾ.bju/ (pt-BR), /diʃ.ˈtuɾ.bju/ (pt-PT)
  1. [B2] problema de saúde que altera o funcionamento normal do corpo ou da mente
     - example: Muitos adolescentes sofrem de algum distúrbio alimentar sem saber.
     - frames: distúrbio de + subst.
  2. [B2] confusão violenta em lugar público, com brigas, gritos ou protestos
     - example: A polícia foi chamada para conter os distúrbios no centro da cidade.
     - opposites: ordem

### 92. fabricar

**Lexeme 1: fabricar**, VERB
- grammar: conjugation 1; valency Tr; irregular False; note c becomes qu before e: fabriquei, fabrique
- said: /fa.bɾi.ˈkaʁ/ (pt-BR), /fɐ.bɾi.ˈkaɾ/ (pt-PT)
  1. [B1] produzir objetos em quantidade, geralmente com máquinas, numa indústria
     - example: Nesta cidade, muitas pequenas empresas fabricam móveis de madeira.
  2. [B2] inventar algo falso, como provas ou desculpas, para enganar alguém
     - example: Não adianta fabricar desculpas, eu já sei o que aconteceu.
     - labels: figurative True

### 93. galileu

**Lexeme 1: galileu**, ADJ
- grammar: comparable False
- said: /ɡa.li.ˈlew/ (pt-BR), /ɡɐ.li.ˈlew/ (pt-PT)
- forms: galileia [Gender=Fem, Number=Sing] /ɡa.li.ˈlɛj.ɐ/ /ɡɐ.li.ˈlɐj.ɐ/; galileias [Gender=Fem, Number=Plur] /ɡa.li.ˈlɛj.ɐs/ /ɡɐ.li.ˈlɐj.ɐʃ/
  1. [C1] da Galileia, antiga região do norte da Palestina
     - example: Os pescadores galileus lançavam as redes ao amanhecer.
- relations: adjective_of Galileia (PROPN)
**Lexeme 2: galileu**, NOUN
- grammar: gender Masc; countability Count; note feminine galileia
- said: /ɡa.li.ˈlew/ (pt-BR), /ɡɐ.li.ˈlew/ (pt-PT)
- forms: galileia [Gender=Fem, Number=Sing] /ɡa.li.ˈlɛj.ɐ/ /ɡɐ.li.ˈlɐj.ɐ/
  1. [C1] pessoa nascida ou que vivia na Galileia
     - example: Segundo os evangelhos, muitos dos primeiros discípulos eram galileus.
- relations: derived_from Galileia (PROPN)

### 94. indisponível

**Lexeme 1: indisponível**, ADJ
- grammar: comparable False; note same form for masculine and feminine; plural indisponíveis
- said: /ĩ.dis.po.ˈni.vew/ (pt-BR), /ĩ.diʃ.pu.ˈni.vɛl/ (pt-PT)
  1. [B1] que não se pode usar, obter ou contactar naquele momento
     - example: O site ficará indisponível durante a manutenção desta noite.
     - opposites: disponível
- relations: derived_from disponível (ADJ)

### 95. velho

**Lexeme 1: velho**, ADJ
- grammar: comparable True
- said: /ˈvɛ.ʎu/ (pt-BR), /ˈvɛ.ʎu/ (pt-PT)
  1. [A1] que tem muitos anos de vida; de idade avançada
     - example: A minha avó já está muito velha, mas continua ativa.
     - opposites: jovem, novo
  2. [A1] que foi feito há muito tempo ou está gasto pelo uso
     - example: Preciso de trocar estes sapatos velhos por uns novos.
     - opposites: novo
  3. [B1] que se conhece ou existe há muito tempo; de longa data
     - example: Ontem encontrei por acaso um velho amigo da escola.
     - grammar: position Attr
**Lexeme 2: velho**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈvɛ.ʎu/ (pt-BR), /ˈvɛ.ʎu/ (pt-PT)
  1. [A2] homem de idade avançada
     - example: Todas as manhãs, um velho dá comida aos pombos na praça.
     - opposites: jovem
  2. [B1] o pai de alguém; no plural, os pais
     - example: Vou passar o fim de semana em casa dos meus velhos.
     - labels: formality informal
- relations: derived_from velho (ADJ)

### 96. charmoso

**Lexeme 1: charmoso**, ADJ
- grammar: comparable True; note stressed o opens to ɔ in the feminine and plural forms
- said: /ʃaʁ.ˈmo.zu/ (pt-BR), /ʃɐɾ.ˈmo.zu/ (pt-PT)
- forms: charmosa [Gender=Fem, Number=Sing] /ʃaʁ.ˈmɔ.zɐ/ /ʃɐɾ.ˈmɔ.zɐ/; charmosos [Gender=Masc, Number=Plur] /ʃaʁ.ˈmɔ.zus/ /ʃɐɾ.ˈmɔ.zuʃ/; charmosas [Gender=Fem, Number=Plur] /ʃaʁ.ˈmɔ.zɐs/ /ʃɐɾ.ˈmɔ.zɐʃ/
  1. [B1] que atrai e agrada pela graça, elegância ou simpatia
     - example: Aquele bairro antigo tem ruas estreitas e cafés muito charmosos.
- relations: adjective_of charme (NOUN)

### 97. invariavelmente

**Lexeme 1: invariavelmente**, ADV
- said: /ĩ.va.ɾi.ˌa.vew.ˈmẽ.tʃi/ (pt-BR), /ĩ.vɐ.ɾi.ˌa.vɛl.ˈmẽ.tɨ/ (pt-PT)
  1. [C1] em todas as ocasiões, sem nenhuma exceção ou mudança
     - example: Às sextas-feiras, o trânsito perto da ponte fica invariavelmente parado.
- relations: adverb_of invariável (ADJ)

### 98. leste

**Lexeme 1: leste**, NOUN
- grammar: gender Masc
- said: /ˈlɛs.tʃi/ (pt-BR), /ˈlɛʃ.tɨ/ (pt-PT)
  1. [A2] ponto cardeal do lado onde o sol nasce
     - example: A nossa janela fica virada para leste, por isso entra sol de manhã.
     - frames: a leste de; para leste
     - opposites: oeste
  2. [B1] região ou parte de um território situada do lado onde nasce o sol
     - example: Muitos trabalhadores vieram do Leste da Europa nos anos noventa.
- relations: confusable ler (VERB): leste is also the 2nd person singular preterite of ler (you read)

### 99. desgaste

**Lexeme 1: desgaste**, NOUN
- grammar: gender Masc; countability Mass
- said: /dʒiz.ˈɡas.tʃi/ (pt-BR), /dɨʒ.ˈɡaʃ.tɨ/ (pt-PT)
  1. [B2] perda gradual de material causada pelo uso ou pelo atrito
     - example: Com tantas viagens, o desgaste dos pneus foi muito rápido.
     - frames: desgaste de algo
  2. [B2] cansaço físico ou emocional causado por esforço longo ou tensão
     - example: Trabalhar à noite provoca um enorme desgaste no corpo.
  3. [C1] perda de prestígio, confiança ou apoio, sobretudo na política
     - example: O escândalo causou grande desgaste à imagem do governo.
     - labels: figurative True
- relations: verbal_noun_of desgastar (VERB)

### 100. cínico

**Lexeme 1: cínico**, ADJ
- said: /ˈsi.ni.ku/ (pt-BR), /ˈsi.ni.ku/ (pt-PT)
  1. [B2] que mente ou finge sem vergonha e despreza os valores morais
     - example: Ele deu um sorriso cínico e continuou a negar tudo.
     - opposites: sincero
  2. [C2] da antiga escola grega que desprezava as convenções sociais e as riquezas
     - example: A filosofia cínica defendia uma vida simples e sem luxos.
     - labels: domain philosophy
     - grammar: comparable False
**Lexeme 2: cínico**, NOUN
- grammar: gender Masc; countability Count; note feminine cínica
- said: /ˈsi.ni.ku/ (pt-BR), /ˈsi.ni.ku/ (pt-PT)
  1. [B2] pessoa que mente ou finge sem vergonha e despreza a moral
     - example: Só um cínico seria capaz de mentir assim aos próprios filhos.
  2. [C2] seguidor da antiga escola grega que desprezava as convenções sociais
     - example: Os cínicos viviam com pouco e recusavam as riquezas.
     - labels: domain philosophy
