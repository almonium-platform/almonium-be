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

### 1. aluguel

**Lexeme 1: aluguel**, NOUN
- grammar: gender Masc; countability Count; note the usual word in Brazil; Portugal mostly says aluguer or renda
- said: /a.lu.ˈɡɛw/ (pt-BR), /ɐ.lu.ˈɡɛl/ (pt-PT)
- forms: aluguéis [Number=Plur] /a.lu.ˈɡɛjs/ /ɐ.lu.ˈɡɛjʃ/
  1. [A2] dinheiro pago com regularidade para usar a casa ou o bem de outra pessoa
     - example: O aluguel do nosso apartamento vence todo dia cinco.
     - frames: aluguel de algo
     - collocations: pagar o aluguel; aluguel atrasado; casa de aluguel; valor do aluguel; reajuste do aluguel; morar de aluguel
     - regions: pt-BR mainly
  2. [B1] uso de uma coisa por algum tempo em troca de pagamento
     - example: Fizemos o aluguel de um carro para viajar pela costa.
     - frames: aluguel de algo
     - collocations: carro de aluguel; aluguel de carros; fazer o aluguel; para aluguel
     - regions: pt-BR mainly
- relations: derived_from alugar (VERB)

### 2. passageiro

**Lexeme 1: passageiro**, NOUN
- grammar: gender Masc; countability Count; note feminine: passageira
- said: /pa.sa.ˈʒej.ɾu/ (pt-BR), /pɐ.sɐ.ˈʒɐj.ɾu/ (pt-PT)
  1. [A2] pessoa que viaja num veículo sem ser quem o conduz
     - example: Todos os passageiros devem apertar o cinto durante a viagem.
     - collocations: banco do passageiro; transporte de passageiros; embarcar passageiros; passageiro clandestino; lugar do passageiro; passageiros em trânsito
- relations: derived_from passagem (NOUN)
**Lexeme 2: passageiro**, ADJ
- said: /pa.sa.ˈʒej.ɾu/ (pt-BR), /pɐ.sɐ.ˈʒɐj.ɾu/ (pt-PT)
  1. [B2] que dura pouco tempo e logo acaba
     - example: Os médicos disseram que a febre é apenas passageira.
     - collocations: fase passageira; dor passageira; mal-estar passageiro; paixão passageira; moda passageira
     - opposites: permanente

### 3. gerente

**Lexeme 1: gerente**, NOUN
- grammar: gender Com; countability Count
- said: /ʒe.ˈɾẽ.tʃi/ (pt-BR), /ʒɨ.ˈɾẽ.tɨ/ (pt-PT)
  1. [A2] pessoa que dirige e administra uma empresa, uma loja ou um setor
     - example: Se o problema continuar, vou pedir para falar com a gerente.
     - frames: gerente de algo
     - collocations: gerente de banco; gerente de loja; gerente geral; gerente de vendas; gerente de projetos; gerente de conta; chamar o gerente; falar com o gerente

### 4. lhes

Not written as an entry: inflected form of lhe (plural of the indirect object pronoun)

### 5. faro

**Lexeme 1: faro**, NOUN
- grammar: gender Masc; number Sing, mainly
- said: /ˈfa.ɾu/ (pt-BR), /ˈfa.ɾu/ (pt-PT)
  1. [B2] capacidade que os animais têm de sentir cheiros
     - example: Os cães de caça têm um faro muito apurado.
     - collocations: faro apurado; faro aguçado; faro fino; ter bom faro
  2. [C1] capacidade de perceber ou adivinhar as coisas antes dos outros
     - example: Ela sempre teve faro para descobrir bons negócios.
     - frames: faro para algo; faro para + inf
     - collocations: ter faro; faro para negócios; faro jornalístico; faro apurado
     - labels: figurative True
**Lexeme 2: Faro**, PROPN
- said: /ˈfa.ɾu/ (pt-BR), /ˈfa.ɾu/ (pt-PT)
  1. [A2] cidade do sul de Portugal, capital do Algarve
     - example: Chegamos de avião a Faro e seguimos para a praia.

### 6. superior

**Lexeme 1: superior**, ADJ
- grammar: note same form for masculine and feminine
- said: /su.pe.ˈɾjoɾ/ (pt-BR), /su.pɨ.ˈɾjoɾ/ (pt-PT), /su.pe.ɾi.ˈoɾ/ (pt-BR, careful)
  1. [B1] maior em número, quantidade, valor ou grau do que outro
     - example: Amanhã as temperaturas vão ser superiores a trinta graus.
     - frames: superior a algo
     - collocations: muito superior; valor superior; número superior; ligeiramente superior
     - opposites: inferior
  2. [B1] que está colocado mais acima ou na parte de cima
     - example: O nome aparece no canto superior direito da página.
     - collocations: andar superior; lábio superior; parte superior; membros superiores; canto superior
     - opposites: inferior
  3. [B1] de melhor qualidade ou com mais capacidade do que outro
     - example: A nossa equipa foi claramente superior durante todo o jogo.
     - frames: superior a algo/alguém
     - collocations: qualidade superior; claramente superior; sentir-se superior
     - opposites: inferior
  4. [B1] de nível ou posição mais alta numa hierarquia ou num sistema
     - example: Quem tem curso superior encontra emprego com mais facilidade.
     - collocations: ensino superior; curso superior; tribunal superior; oficial superior; ordens superiores; autoridade superior
  5. [C1] que mostra o ar de quem se julga melhor que os outros
     - example: Ele falou conosco num tom superior que irritou toda a gente.
     - collocations: ar superior; tom superior
     - labels: attitude disapproving
**Lexeme 2: superior**, NOUN
- grammar: gender Com; countability Count
- said: /su.pe.ˈɾjoɾ/ (pt-BR), /su.pɨ.ˈɾjoɾ/ (pt-PT), /su.pe.ɾi.ˈoɾ/ (pt-BR, careful)
  1. [B1] pessoa que tem mais autoridade ou posição mais alta no trabalho
     - example: Vou falar com o meu superior antes de assinar o contrato.
     - collocations: superior hierárquico; superior imediato
     - opposites: subordinado
  2. [C1] pessoa que dirige um convento ou uma comunidade religiosa
     - example: O superior do mosteiro recebeu os visitantes à porta.
     - labels: domain religion
     - grammar: note for a woman usually superiora or madre superiora

### 7. marcial

**Lexeme 1: marcial**, ADJ
- grammar: note same form for masculine and feminine; plural marciais
- said: /maɾ.ˈsjaw/ (pt-BR), /mɐɾ.ˈsjal/ (pt-PT)
  1. [B2] da guerra, dos soldados ou das forças armadas
     - example: O governo decretou lei marcial depois de dias de protestos violentos.
     - collocations: lei marcial; artes marciais; corte marcial; música marcial
     - grammar: comparable False
  2. [C1] firme, direito e enérgico, com ar de soldado
     - example: Os cadetes atravessam a praça com passo marcial e rosto sério.
     - collocations: passo marcial; porte marcial; ar marcial
     - labels: style literary

### 8. bravo

**Lexeme 1: bravo**, ADJ
- grammar: comparable True
- said: /ˈbɾa.vu/ (pt-BR), /ˈbɾa.vu/ (pt-PT)
  1. [A2] irritado ou com raiva de alguém ou de alguma coisa
     - example: Minha mãe vai ficar brava se eu chegar tarde.
     - frames: bravo com alguém; bravo com algo
     - collocations: ficar bravo; estar bravo
     - regions: pt-BR mainly
     - grammar: position Pred, mainly
  2. [B1] que não é domesticado nem cultivado e pode ser perigoso ou agressivo
     - example: Cuidado com o portão, que ali dentro há um cão bravo.
     - collocations: cão bravo; touro bravo; animal bravo
     - opposites: manso
  3. [B2] que enfrenta o perigo sem medo; valente, corajoso
     - example: Os bravos soldados defenderam a cidade até ao fim.
     - labels: style literary
     - opposites: covarde
  4. [B2] agitado e violento, com ondas fortes
     - example: Hoje o mar está bravo e ninguém deve entrar na água.
     - collocations: mar bravo
**Lexeme 2: bravo**, INTJ
- said: /ˈbɾa.vu/ (pt-BR), /ˈbɾa.vu/ (pt-PT)
  1. [B1] usado para aplaudir ou aprovar com entusiasmo uma atuação
     - example: Bravo, foi o melhor concerto que ouvimos este ano!

### 9. pulmão

**Lexeme 1: pulmão**, NOUN
- grammar: gender Masc; countability Count
- said: /puw.ˈmɐ̃w/ (pt-BR), /pul.ˈmɐ̃w/ (pt-PT)
- forms: pulmões [Number=Plur] /puw.ˈmõjs/ /pul.ˈmõjʃ/
  1. [A2] cada um dos dois órgãos do peito que servem para respirar
     - example: Fumar durante tantos anos prejudicou muito os seus pulmões.
     - collocations: a plenos pulmões; encher os pulmões; cancro do pulmão; câncer de pulmão; pulmão direito; pulmão esquerdo; transplante de pulmão
  2. [B2] área com muitas árvores que torna mais puro o ar de um lugar
     - example: Este parque é considerado o pulmão verde da cidade.
     - frames: pulmão de algo
     - collocations: pulmão verde; pulmão do mundo
     - labels: figurative True

### 10. selva

**Lexeme 1: selva**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈsɛw.vɐ/ (pt-BR), /ˈsɛl.vɐ/ (pt-PT)
  1. [B1] floresta muito densa, cheia de árvores e plantas, em regiões quentes e chuvosas
     - example: Os exploradores passaram três semanas perdidos no meio da selva.
     - collocations: no meio da selva; selva tropical; selva densa; rei da selva
  2. [B2] lugar ou meio onde reinam a desordem e a luta sem regras
     - example: Para quem chega do campo, a cidade grande parece uma selva.
     - collocations: lei da selva; selva de pedra; selva urbana
     - labels: figurative True

### 11. herança

**Lexeme 1: herança**, NOUN
- grammar: gender Fem; countability Both
- said: /e.ˈɾɐ̃.sɐ/ (pt-BR), /ɨ.ˈɾɐ̃.sɐ/ (pt-PT)
  1. [B1] bens e dinheiro que passam para outras pessoas quando alguém morre
     - example: Os irmãos discutiram durante anos por causa da herança do pai.
     - collocations: receber uma herança; deixar uma herança; dividir a herança; partilha da herança; herança de família
     - grammar: countability Count
  2. [B2] o que uma geração ou época passa às seguintes, como costumes e ideias
     - example: A música desta região é uma herança dos povos africanos.
     - frames: herança de algo/alguém
     - collocations: herança cultural; herança histórica; herança colonial; receber como herança
     - labels: figurative True
     - grammar: countability Count, mainly
  3. [C1] características que os filhos recebem dos pais através dos genes
     - example: A cor dos olhos depende em grande parte da herança genética.
     - collocations: herança genética
     - labels: domain biology
     - grammar: countability Mass

### 12. noticiário

**Lexeme 1: noticiário**, NOUN
- grammar: gender Masc; countability Count
- said: /no.tʃi.si.ˈa.ɾju/ (pt-BR), /nu.ti.ˈsja.ɾju/ (pt-PT)
  1. [A2] programa de rádio ou televisão que informa sobre os acontecimentos recentes
     - example: Vejo sempre o noticiário da noite depois do jantar.
     - collocations: ver o noticiário; ouvir o noticiário; assistir ao noticiário; noticiário da noite; noticiário da manhã; noticiário televisivo
  2. [B2] conjunto das informações publicadas pela imprensa sobre um assunto
     - example: O noticiário sobre as eleições ocupa as primeiras páginas dos jornais.
     - frames: noticiário sobre algo
     - collocations: noticiário político; noticiário internacional
- relations: derived_from notícia (NOUN)

### 13. spam

**Lexeme 1: spam**, NOUN
- grammar: gender Masc; countability Both, mainly
- said: /is.ˈpɐ̃/ (pt-BR), /ˈspam/ (pt-PT)
  1. [B1] mensagens não pedidas enviadas em grande quantidade pela internet, geralmente publicidade
     - example: Todos os dias apago dezenas de mensagens de spam do meu e-mail.
     - collocations: filtro de spam; pasta de spam; caixa de spam; receber spam; enviar spam; marcar como spam
     - labels: domain computing
     - grammar: countability Mass, mainly

### 14. aeronave

**Lexeme 1: aeronave**, NOUN
- grammar: gender Fem; countability Count
- said: /a.e.ɾo.ˈna.vi/ (pt-BR), /a.ɛ.ɾɔ.ˈna.vɨ/ (pt-PT)
  1. [B2] qualquer veículo capaz de voar e se deslocar pelo ar
     - example: A aeronave pousou em segurança depois de uma falha no motor.
     - collocations: pilotar uma aeronave; tripulação da aeronave; aeronave não tripulada; aeronave comercial; aeronave militar; aeronave de pequeno porte
- relations: derived_from nave (NOUN)

### 15. decreto

**Lexeme 1: decreto**, NOUN
- grammar: gender Masc; countability Count
- said: /de.ˈkɾɛ.tu/ (pt-BR), /dɨ.ˈkɾɛ.tu/ (pt-PT)
  1. [B2] ordem ou decisão oficial escrita, dada por uma autoridade com poder para isso
     - example: O presidente assinou um decreto que aumenta o salário mínimo.
     - collocations: assinar um decreto; publicar um decreto; revogar um decreto; promulgar um decreto; decreto presidencial; por decreto

### 16. aranha

**Lexeme 1: aranha**, NOUN
- grammar: gender Fem; countability Count
- said: /a.ˈɾɐ̃.ɲɐ/ (pt-BR), /ɐ.ˈɾɐ.ɲɐ/ (pt-PT)
  1. [A2] pequeno animal de oito patas que produz fios de seda e caça insetos
     - example: Tem uma aranha enorme escondida no canto do teto.
     - collocations: teia de aranha; picada de aranha; aranha venenosa; medo de aranhas

### 17. órbita

**Lexeme 1: órbita**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈɔɾ.bi.tɐ/ (pt-BR), /ˈɔɾ.bi.tɐ/ (pt-PT)
  1. [B1] caminho curvo que um planeta ou satélite faz à volta de outro corpo
     - example: O satélite entrou em órbita poucos minutos depois do lançamento.
     - frames: órbita de algo; órbita em torno de algo
     - collocations: entrar em órbita; em órbita; pôr em órbita; colocar em órbita; órbita terrestre; órbita baixa; sair de órbita
  2. [C1] cada um dos buracos do crânio onde ficam os olhos
     - example: Com o susto, os olhos quase lhe saltaram das órbitas.
     - collocations: saltar das órbitas; órbita ocular
     - labels: domain anatomy
  3. [C1] área de influência ou de controle de um país, pessoa ou grupo
     - example: Durante décadas, o país ficou na órbita política da potência vizinha.
     - frames: órbita de algo/alguém
     - collocations: na órbita de; órbita de influência; órbita política
     - labels: figurative True

### 18. chumbo

**Lexeme 1: chumbo**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈʃũ.bu/ (pt-BR), /ˈʃũ.bu/ (pt-PT)
  1. [B1] metal pesado, mole e cinzento, usado em baterias e proteções
     - example: Os canos antigos desta casa ainda eram feitos de chumbo.
     - collocations: gasolina sem chumbo; pesado como chumbo; soldadinho de chumbo; intoxicação por chumbo; canos de chumbo
     - grammar: countability Mass
  2. [B2] pequenas bolas de metal disparadas por espingardas de caça
     - example: O caçador carregou a espingarda com chumbo para caçar perdizes.
     - collocations: chumbo grosso; carregar com chumbo
     - grammar: countability Mass
  3. [B2] recusa de uma proposta ou lei numa votação
     - example: O chumbo do orçamento no parlamento provocou uma crise política.
     - frames: chumbo de algo
     - collocations: chumbo do orçamento; chumbo da proposta
     - regions: pt-PT only
     - grammar: countability Count
  4. [B1] reprovação num exame, numa disciplina ou num ano escolar
     - example: Depois do chumbo no exame, teve de estudar o verão inteiro.
     - frames: chumbo em algo; chumbo a algo
     - collocations: ter um chumbo; levar um chumbo
     - labels: formality informal
     - regions: pt-PT only
     - grammar: countability Count
  5. [B2] material com que o dentista enche um buraco num dente
     - example: Ontem o dentista pôs-me um chumbo num dente de trás.
     - collocations: pôr um chumbo
     - labels: formality informal
     - regions: pt-PT only
     - grammar: countability Count
- relations: derived_from chumbar (VERB)

### 19. bit

**Lexeme 1: bit**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈbi.tʃi/ (pt-BR), /ˈbit/ (pt-PT)
- forms: bits [Number=Plur] /ˈbi.tʃis/ /ˈbitʃ/
  1. [B2] menor unidade de informação digital, que pode valer zero ou um
     - example: Cada byte é formado por oito bits de informação.
     - collocations: bits por segundo; 64 bits; 32 bits
     - labels: domain computing

### 20. dirigente

**Lexeme 1: dirigente**, NOUN
- grammar: gender Com; countability Count
- said: /dʒi.ɾi.ˈʒẽ.tʃi/ (pt-BR), /di.ɾi.ˈʒẽ.tɨ/ (pt-PT)
  1. [B2] pessoa que ocupa um cargo de chefia numa organização ou instituição
     - example: Os dirigentes do clube decidiram contratar um novo treinador.
     - frames: dirigente de algo
     - collocations: dirigente sindical; dirigente partidário; dirigente político; alto dirigente; dirigente desportivo; dirigente esportivo
- relations: agent_noun_of dirigir (VERB)
**Lexeme 2: dirigente**, ADJ
- grammar: comparable False; note same form for masculine and feminine
- said: /dʒi.ɾi.ˈʒẽ.tʃi/ (pt-BR), /di.ɾi.ˈʒẽ.tɨ/ (pt-PT)
  1. [C1] que tem o poder de comandar e orientar um grupo ou instituição
     - example: O órgão dirigente do partido aprovou ontem a nova estratégia.
     - collocations: classe dirigente; órgão dirigente; elite dirigente
- relations: derived_from dirigir (VERB)

### 21. desconto

**Lexeme 1: desconto**, NOUN
- grammar: gender Masc; countability Count
- said: /dʒis.ˈkõ.tu/ (pt-BR), /dɨʃ.ˈkõ.tu/ (pt-PT)
  1. [A2] redução no preço normal de um produto ou serviço
     - example: A loja oferece um desconto de vinte por cento nos sapatos.
     - frames: desconto em algo; desconto de + quantia
     - collocations: fazer um desconto; dar um desconto; com desconto; pedir um desconto; oferecer um desconto; ter desconto; desconto especial
  2. [B2] quantia retirada de um salário ou de um pagamento
     - example: Depois dos descontos, o meu salário ficou bem mais baixo.
     - frames: desconto em algo; desconto para algo
     - collocations: fazer descontos; desconto em folha
- relations: verbal_noun_of descontar (VERB)

### 22. critério

**Lexeme 1: critério**, NOUN
- grammar: gender Masc; countability Both
- said: /kɾi.ˈtɛ.ɾju/ (pt-BR), /kɾi.ˈtɛ.ɾju/ (pt-PT)
  1. [B1] princípio ou norma que serve de base para julgar, escolher ou decidir
     - example: Os candidatos foram escolhidos segundo critérios bem claros.
     - frames: critério de algo; critério para algo
     - collocations: critério de seleção; critério de avaliação; estabelecer critérios; adotar um critério; cumprir os critérios; segundo o critério; critério objetivo
     - grammar: countability Count
  2. [B2] capacidade de avaliar as coisas e escolher com cuidado e bom senso
     - example: Gasta o dinheiro sem critério nenhum e depois fica sem nada.
     - collocations: com critério; sem critério; a critério de; ficar ao critério de
     - grammar: countability Mass

### 23. circo

**Lexeme 1: circo**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈsiɾ.ku/ (pt-BR), /ˈsiɾ.ku/ (pt-PT)
  1. [A2] espetáculo itinerante de acrobatas, palhaços e outros artistas, apresentado numa grande tenda
     - example: Quando eu era criança, meu pai me levava ao circo todo ano.
     - collocations: ir ao circo; montar o circo; lona do circo; artista de circo; espetáculo de circo; palhaço de circo
  2. [B2] situação confusa e sem seriedade, que parece um espetáculo ridículo
     - example: A reunião de ontem virou um verdadeiro circo.
     - collocations: virar um circo; armar um circo; um verdadeiro circo
     - labels: figurative True
  3. [C1] grande recinto da Roma antiga onde se faziam corridas de carros e jogos públicos
     - example: Milhares de romanos assistiam às corridas de carros no circo.
     - labels: time historical

### 24. nosso

**Lexeme 1: nosso**, DET
- grammar: note agrees with the thing possessed; in pt-PT usually after the article, in pt-BR often without it
- said: /ˈnɔ.su/ (pt-BR), /ˈnɔ.su/ (pt-PT)
  1. [A1] que pertence a quem fala e a outras pessoas, ou se relaciona com eles
     - example: Amanhã vamos visitar a nossa avó no campo.
     - collocations: um amigo nosso; a nosso ver; Nosso Senhor; em nossa casa
**Lexeme 2: nosso**, PRON
- grammar: note usually with the article: o nosso, a nossa, os nossos, as nossas
- said: /ˈnɔ.su/ (pt-BR), /ˈnɔ.su/ (pt-PT)
  1. [A2] aquilo que pertence a quem fala e a outras pessoas, já mencionado antes
     - example: O vosso carro é azul, mas o nosso é vermelho.
  2. [B1] as pessoas da família, do grupo ou do lado de quem fala
     - example: Os nossos ganharam o jogo ontem à noite.
     - grammar: number Plur; note used with the article: os nossos

### 25. fígado

**Lexeme 1: fígado**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈfi.ɡa.du/ (pt-BR), /ˈfi.ɡɐ.du/ (pt-PT)
  1. [B1] órgão grande do abdómen que produz a bílis e limpa o sangue
     - example: Beber álcool em excesso faz muito mal ao fígado.
     - collocations: fazer mal ao fígado; doença do fígado; bife de fígado; fígado de galinha; fígado de vaca; transplante de fígado

### 26. ciclo

**Lexeme 1: ciclo**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈsi.klu/ (pt-BR), /ˈsi.klu/ (pt-PT)
  1. [B1] série de fases ou acontecimentos que se repetem sempre pela mesma ordem
     - example: O ciclo da água inclui a evaporação e a chuva.
     - frames: ciclo de algo
     - collocations: ciclo vicioso; ciclo de vida; ciclo menstrual; fechar um ciclo; completar um ciclo; quebrar o ciclo; ciclo da água
  2. [B2] cada uma das etapas de vários anos em que se divide o ensino
     - example: O meu filho entra no segundo ciclo no próximo ano.
     - collocations: primeiro ciclo; segundo ciclo; terceiro ciclo; ciclo básico
     - regions: pt-PT mainly
  3. [B2] série de palestras, filmes ou eventos organizados sobre o mesmo tema
     - example: A faculdade organizou um ciclo de conferências sobre o ambiente.
     - frames: ciclo de algo
     - collocations: ciclo de conferências; ciclo de palestras; ciclo de cinema; organizar um ciclo

### 27. sacola

**Lexeme 1: sacola**, NOUN
- grammar: gender Fem; countability Count
- said: /sa.ˈkɔ.lɐ/ (pt-BR), /sɐ.ˈkɔ.lɐ/ (pt-PT)
  1. [A2] saco com alças usado para carregar compras ou outros objetos
     - example: Sempre levo uma sacola de pano quando vou à feira.
     - collocations: sacola plástica; sacola de compras; sacola de supermercado; sacola de pano; sacola retornável; encher a sacola

### 28. gospel

**Lexeme 1: gospel**, NOUN
- grammar: gender Masc; countability Mass
- said: /ˈɡɔs.pew/ (pt-BR), /ˈɡɔʃ.pɛl/ (pt-PT)
  1. [B1] gênero de música religiosa cristã nascido nas igrejas negras americanas
     - example: O coro da nossa igreja canta gospel todos os domingos.
     - collocations: cantar gospel; coro de gospel; ouvir gospel
**Lexeme 2: gospel**, ADJ
- grammar: invariable True; comparable False; note same form for masculine, feminine, singular and plural
- said: /ˈɡɔs.pew/ (pt-BR), /ˈɡɔʃ.pɛl/ (pt-PT)
  1. [B1] de música religiosa cristã, com origem nas igrejas negras americanas
     - example: Ela começou a carreira como cantora gospel ainda adolescente.
     - collocations: música gospel; cantor gospel; banda gospel; coral gospel
     - grammar: position Pred, mainly; note follows the noun
  2. [B2] ligado ao meio cristão evangélico e à sua cultura
     - example: O mercado gospel cresce todos os anos no país.
     - collocations: mercado gospel; público gospel; evento gospel
     - regions: pt-BR mainly

### 29. corda

**Lexeme 1: corda**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈkɔɾ.dɐ/ (pt-BR), /ˈkɔɾ.dɐ/ (pt-PT)
  1. [A2] fio grosso de fibras torcidas, usado para amarrar, prender ou puxar
     - example: Os alpinistas subiram a montanha presos a uma corda grossa.
     - collocations: corda grossa; amarrar com uma corda; puxar a corda; esticar a corda; corda bamba; pular corda; saltar à corda; com a corda no pescoço
  2. [B1] fio esticado que vibra e produz som num instrumento musical
     - example: Uma corda da guitarra partiu a meio do concerto.
     - collocations: instrumento de corda; afinar as cordas; corda de violão; partir uma corda
  3. [B2] mola que, depois de enrolada, faz funcionar um relógio ou brinquedo
     - example: Antes de dormir, o avô dava corda ao relógio antigo.
     - frames: dar corda a algo
     - collocations: dar corda; acabar a corda

### 30. eis

**Lexeme 1: eis**, ADV
- grammar: note object pronouns attach to it with a hyphen: eis-me, eis-nos, ei-lo
- said: /ˈejs/ (pt-BR), /ˈɐjʃ/ (pt-PT)
  1. [B2] usado para apresentar ou mostrar algo ou alguém que está ali
     - example: Eis o relatório que me pediste na reunião de ontem.
     - frames: eis algo/alguém
     - collocations: eis que; eis aqui; eis porque; eis a questão; eis senão quando
     - labels: formality formal

### 31. stop

**Lexeme 1: stop**, NOUN
- grammar: gender Masc; note English loanword; plural stops
- said: /is.ˈtɔ.pi/ (pt-BR), /ˈʃtɔp/ (pt-PT)
- forms: stops [Number=Plur] /is.ˈtɔ.pis/ /ˈʃtɔps/
  1. [B1] sinal de trânsito que obriga os condutores a parar completamente
     - example: Quem não parar no stop pode levar uma multa pesada.
     - collocations: sinal de stop; parar no stop; respeitar o stop
     - regions: pt-PT mainly
     - grammar: countability Count
  2. [B2] luz traseira de um veículo que acende quando se trava
     - example: A luz do stop do lado esquerdo está fundida.
     - collocations: luz de stop
     - regions: pt-PT mainly
     - grammar: countability Count
  3. [B2] interrupção ou pausa numa atividade
     - example: Depois de tanto trabalho, preciso dar um stop nas redes sociais.
     - frames: stop em algo
     - collocations: dar um stop
     - labels: formality informal
     - regions: pt-BR mainly
     - grammar: countability Count
  4. [B2] jogo em que se escrevem palavras de várias categorias começadas pela mesma letra
     - example: Nas férias, a gente jogava stop com os primos depois do jantar.
     - collocations: jogar stop
     - regions: pt-BR mainly
     - grammar: countability Mass
**Lexeme 2: stop**, INTJ
- said: /is.ˈtɔ.pi/ (pt-BR), /ˈʃtɔp/ (pt-PT)
  1. [B1] usado para mandar alguém parar ou acabar com algo imediatamente
     - example: Stop, já chega de discussões por hoje!
     - labels: formality informal

### 32. paisagem

**Lexeme 1: paisagem**, NOUN
- grammar: gender Fem; countability Count
- said: /paj.ˈza.ʒẽj/ (pt-BR), /pɐj.ˈza.ʒɐ̃j/ (pt-PT)
  1. [A2] conjunto de terras, campos e natureza que se vê de um lugar
     - example: Da janela do comboio víamos uma paisagem verde e tranquila.
     - collocations: apreciar a paisagem; admirar a paisagem; paisagem natural; paisagem urbana; paisagem rural; paisagem deslumbrante; mudança de paisagem
  2. [B1] pintura ou fotografia que mostra campos, montanhas ou outros lugares naturais
     - example: Na sala havia uma paisagem pintada a óleo por um artista local.
     - collocations: pintar uma paisagem; pintor de paisagens; paisagem a óleo
- relations: confusable passagem (NOUN): similar sound and spelling; passagem means passage or ticket

### 33. vestuário

**Lexeme 1: vestuário**, NOUN
- grammar: gender Masc; countability Mass, mainly
- said: /ves.tu.ˈa.ɾju/ (pt-BR), /vɨʃ.ˈtwa.ɾju/ (pt-PT)
  1. [B1] conjunto das peças de roupa e acessórios que se usam para cobrir o corpo
     - example: Esta loja vende vestuário infantil a preços bastante acessíveis.
     - collocations: peça de vestuário; indústria do vestuário; artigos de vestuário; loja de vestuário; vestuário feminino; vestuário masculino; vestuário infantil
- relations: confusable vestiário (NOUN): similar spelling; vestiário is a changing room or cloakroom, while vestuário is clothing

### 34. hipótese

**Lexeme 1: hipótese**, NOUN
- grammar: gender Fem; countability Count
- said: /i.ˈpɔ.te.zi/ (pt-BR), /i.ˈpɔ.tɨ.zɨ/ (pt-PT)
  1. [B2] ideia ainda não provada que se usa para explicar um fato
     - example: Os cientistas querem testar a hipótese com novos experimentos.
     - frames: hipótese de que + ind; hipótese sobre algo
     - collocations: levantar uma hipótese; formular uma hipótese; testar uma hipótese; confirmar a hipótese; descartar uma hipótese; hipótese científica
  2. [B2] situação que pode acontecer ou que se considera possível
     - example: Em hipótese nenhuma você deve abrir essa porta sozinho.
     - frames: na hipótese de + inf; na hipótese de que + subj
     - collocations: em hipótese alguma; em hipótese nenhuma; na pior das hipóteses; na melhor das hipóteses; por hipótese

### 35. primário

**Lexeme 1: primário**, ADJ
- said: /pɾi.ˈma.ɾju/ (pt-BR), /pɾi.ˈma.ɾju/ (pt-PT)
  1. [B2] que vem em primeiro lugar numa ordem, série ou desenvolvimento
     - example: A doença ainda estava na fase primária quando foi descoberta.
     - collocations: setor primário; cor primária; fase primária; eleições primárias
     - grammar: comparable False
     - opposites: secundário
  2. [B2] que é o mais importante ou está na base de tudo
     - example: O objetivo primário do projeto é reduzir os custos.
     - collocations: objetivo primário; função primária; necessidades primárias; causa primária
     - opposites: secundário
  3. [A2] do primeiro nível do ensino escolar, destinado às crianças
     - example: A minha mãe foi professora primária durante trinta anos.
     - collocations: escola primária; ensino primário; professor primário; curso primário
     - grammar: comparable False; position Attr
  4. [C1] muito simples, pouco pensado ou pouco elaborado
     - example: Acho a reação dele muito primária para um adulto.
     - collocations: reação primária; instinto primário; raciocínio primário
     - labels: attitude disapproving
**Lexeme 2: primário**, NOUN
- grammar: gender Masc; countability Count; number Sing, mainly
- said: /pɾi.ˈma.ɾju/ (pt-BR), /pɾi.ˈma.ɾju/ (pt-PT)
  1. [B1] primeiro nível do ensino escolar, frequentado pelas crianças
     - example: Minha avó só pôde estudar até o primário.
     - collocations: fazer o primário; terminar o primário
     - regions: pt-BR mainly

### 36. embora

**Lexeme 1: embora**, SCONJ
- said: /ẽ.ˈbɔ.ɾɐ/ (pt-BR), /ẽ.ˈbɔ.ɾɐ/ (pt-PT)
  1. [B1] usado para introduzir um facto que contrasta com o principal sem o impedir
     - example: Embora chova muito, vamos sair para jantar esta noite.
     - frames: embora + subj; embora + adj
**Lexeme 2: embora**, ADV
- said: /ẽ.ˈbɔ.ɾɐ/ (pt-BR), /ẽ.ˈbɔ.ɾɐ/ (pt-PT)
  1. [A2] para longe do lugar onde se está, deixando esse lugar
     - example: Está muito tarde, por isso vou embora daqui a pouco.
     - collocations: ir embora; vamos embora; mandar embora; ir-se embora

### 37. defender

**Lexeme 1: defender**, VERB
- grammar: conjugation 2; irregular False; valency Tr
- said: /de.fẽ.ˈdeɾ/ (pt-BR), /dɨ.fẽ.ˈdeɾ/ (pt-PT)
  1. [A2] proteger alguém ou algo contra um ataque, perigo ou dano
     - example: Os soldados defenderam a cidade durante três dias de cerco.
     - frames: defender alguém/algo de algo; defender alguém/algo contra algo; defender-se de algo
     - collocations: defender a pátria; defender a honra; defender o território; defender-se de um ataque; defender com unhas e dentes
  2. [B1] falar a favor de uma ideia, pessoa ou causa, dando argumentos
     - example: Ela sempre defendeu os direitos dos trabalhadores rurais.
     - frames: defender que + ind; defender algo/alguém
     - collocations: defender uma ideia; defender os direitos; defender uma posição; defender uma causa; defender uma tese; defender os interesses
  3. [B2] representar como advogado uma pessoa acusada num tribunal
     - example: O advogado vai defender o réu no julgamento de amanhã.
     - collocations: defender o réu; defender em tribunal; defender um cliente
     - labels: domain law
  4. [B2] impedir, como guarda-redes ou goleiro, que a bola entre na baliza
     - example: O goleiro defendeu o pênalti no último minuto do jogo.
     - collocations: defender o remate; defender a bola

### 38. apresentadora

Not written as an entry: inflected form (feminine) of apresentador

### 39. grato

**Lexeme 1: grato**, ADJ
- grammar: comparable True
- said: /ˈɡɾa.tu/ (pt-BR), /ˈɡɾa.tu/ (pt-PT)
  1. [B1] que reconhece e agradece um favor ou uma ajuda recebida
     - example: Fico muito grato pela ajuda que você me deu ontem.
     - frames: grato a alguém; grato por algo; grato por + inf
     - collocations: muito grato; eternamente grato; profundamente grato; ficar grato; sentir-se grato; estar grato
     - grammar: position Pred, mainly
     - opposites: ingrato
  2. [C1] que causa prazer ou alegria a quem o recebe
     - example: A visita dos antigos colegas foi uma grata surpresa.
     - collocations: grata surpresa; grata recordação; grata satisfação
     - labels: formality formal
     - grammar: position Attr, mainly

### 40. plenamente

**Lexeme 1: plenamente**, ADV
- grammar: invariable True
- said: /ˌple.na.ˈmẽ.tʃi/ (pt-BR), /ˌple.nɐ.ˈmẽ.tɨ/ (pt-PT)
  1. [B2] de forma total e completa, sem faltar nenhuma parte
     - example: Estou plenamente consciente dos riscos deste projeto.
     - collocations: plenamente consciente; concordar plenamente; plenamente satisfeito; plenamente convencido; plenamente justificado; funcionar plenamente
- relations: adverb_of pleno (ADJ)

### 41. garganta

**Lexeme 1: garganta**, NOUN
- grammar: gender Fem; countability Count
- said: /ɡaɾ.ˈɡɐ̃.tɐ/ (pt-BR), /ɡɐɾ.ˈɡɐ̃.tɐ/ (pt-PT)
  1. [A2] parte da frente do pescoço e canal interior por onde passam ar e alimentos
     - example: Tenho a garganta inflamada e custa-me engolir.
     - collocations: dor de garganta; garganta inflamada; garganta seca; limpar a garganta; nó na garganta; pela garganta
  2. [C1] passagem estreita e funda entre montanhas ou rochas
     - example: O rio corre no fundo de uma garganta muito profunda.
     - collocations: garganta profunda; garganta rochosa

### 42. rapariga

**Lexeme 1: rapariga**, NOUN
- grammar: gender Fem; countability Count
- said: /ʁa.pa.ˈɾi.ɡɐ/ (pt-BR), /ʁɐ.pɐ.ˈɾi.ɡɐ/ (pt-PT)
  1. [A1] pessoa jovem do sexo feminino, entre a infância e a idade adulta
     - example: Aquela rapariga de casaco vermelho é a minha prima.
     - collocations: rapariga nova
     - regions: pt-PT only, pt-002 only
  2. [C1] mulher que se prostitui ou que é amante de um homem casado
     - example: Ele largou a família para viver com uma rapariga.
     - labels: formality informal, attitude derogatory, offensive
     - regions: pt-BR only
- relations: feminine_of rapaz (NOUN)

### 43. assassinato

**Lexeme 1: assassinato**, NOUN
- grammar: gender Masc; countability Count
- said: /a.sa.si.ˈna.tu/ (pt-BR), /ɐ.sɐ.si.ˈna.tu/ (pt-PT)
  1. [B2] morte de uma pessoa causada de propósito por outra
     - example: A polícia continua a investigar o assassinato do empresário.
     - frames: assassinato de alguém
     - collocations: cometer um assassinato; tentativa de assassinato; acusado de assassinato; assassinato a sangue-frio; investigar um assassinato; assassinato político
- relations: verbal_noun_of assassinar (VERB); confusable assassínio (NOUN): both mean murder; assassínio is the more usual word in Portugal, assassinato in Brazil

### 44. lanterna

**Lexeme 1: lanterna**, NOUN
- grammar: gender Fem; countability Count
- said: /lɐ̃.ˈtɛɾ.nɐ/ (pt-BR), /lɐ̃.ˈtɛɾ.nɐ/ (pt-PT)
  1. [A2] aparelho pequeno de pilhas, que se leva na mão para iluminar
     - example: Leva uma lanterna, porque a trilha fica escura muito cedo.
     - collocations: acender a lanterna; apagar a lanterna; ligar a lanterna; desligar a lanterna; lanterna de bolso; lanterna do celular
  2. [B1] caixa com lados de vidro ou papel, com uma chama ou luz dentro
     - example: Penduraram lanternas de papel por todo o jardim para a festa.
     - collocations: lanterna de papel; lanterna a óleo; lanterna chinesa
  3. [B2] cada uma das luzes pequenas na frente ou atrás de um veículo
     - example: O carro bateu no poste e quebrou a lanterna traseira.
     - collocations: lanterna traseira; lanterna dianteira
     - regions: pt-BR mainly
  4. [C1] último lugar na classificação de um campeonato
     - example: Depois de mais uma derrota, o time continua na lanterna.
     - collocations: estar na lanterna; sair da lanterna
     - labels: formality informal, figurative True, domain sport
     - regions: pt-BR mainly
     - grammar: number Sing

### 45. praga

**Lexeme 1: praga**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈpɾa.ɡɐ/ (pt-BR), /ˈpɾa.ɡɐ/ (pt-PT)
  1. [B2] animais ou plantas que se multiplicam muito e estragam plantações ou criações
     - example: Uma praga de gafanhotos destruiu quase toda a colheita deste ano.
     - frames: praga de algo
     - collocations: combater uma praga; controle de pragas; praga agrícola; praga de gafanhotos; atacado por pragas
  2. [B2] palavras com que se deseja mal ou desgraça a alguém
     - example: Ela rogou uma praga a quem lhe roubou as galinhas.
     - frames: rogar praga a alguém
     - collocations: rogar uma praga; jogar praga; pegar uma praga
  3. [B2] mal ou desgraça que se espalha e faz sofrer muitas pessoas
     - example: A corrupção tornou-se uma praga difícil de eliminar.
     - collocations: verdadeira praga; as pragas do Egito
     - labels: figurative True
  4. [B2] pessoa ou coisa muito chata, que incomoda sem parar
     - example: Esse menino é uma praga, não para quieto um minuto!
     - labels: formality informal
**Lexeme 2: Praga**, PROPN
- grammar: gender Fem
- said: /ˈpɾa.ɡɐ/ (pt-BR), /ˈpɾa.ɡɐ/ (pt-PT)
  1. [A2] cidade da Europa Central, atravessada pelo rio Moldava, capital nacional
     - example: No verão, passeamos pelas pontes antigas de Praga.

### 46. antiguidade

**Lexeme 1: antiguidade**, NOUN
- grammar: gender Fem; countability Both
- said: /ɐ̃.tʃi.ɡwi.ˈda.dʒi/ (pt-BR), /ɐ̃.ti.ɡwi.ˈda.dɨ/ (pt-PT), /ɐ̃.tʃi.ɡi.ˈda.dʒi/ (pt-BR), /ɐ̃.ti.ɡi.ˈda.dɨ/ (pt-PT)
- spellings: antiguidade (pt-BR, pt-PT); antigüidade (pt-BR) [older books only]
  1. [B1] período da história das primeiras civilizações, antes da Idade Média
     - example: Os filósofos da Antiguidade ainda influenciam o pensamento moderno.
     - collocations: Antiguidade Clássica; Antiguidade Oriental; na Antiguidade; desde a Antiguidade
     - grammar: countability Mass; note usually written with a capital A
  2. [B2] objeto antigo e de valor, procurado por colecionadores
     - example: Ela abriu uma loja de antiguidades no centro da cidade.
     - collocations: loja de antiguidades; feira de antiguidades; comerciante de antiguidades; colecionar antiguidades
     - grammar: countability Count; number Plur, mainly
  3. [B2] condição de existir há muito tempo
     - example: A antiguidade da igreja atrai muitos turistas à vila.
     - grammar: countability Mass
  4. [C1] tempo de serviço que uma pessoa tem num emprego ou cargo
     - example: Aqui as promoções dependem do mérito e da antiguidade.
     - collocations: por antiguidade; promoção por antiguidade; tempo de antiguidade
     - grammar: countability Mass
- relations: abstract_noun_of antigo (ADJ)

### 47. atlântico

**Lexeme 1: atlântico**, ADJ
- grammar: comparable False
- said: /a.ˈtlɐ̃.tʃi.ku/ (pt-BR), /a.tʃi.ˈlɐ̃.tʃi.ku/ (pt-BR, casual), /ɐ.ˈtlɐ̃.ti.ku/ (pt-PT)
  1. [B2] do oceano que separa a Europa e a África das Américas
     - example: A costa atlântica do país tem praias com ondas fortes.
     - collocations: oceano Atlântico; costa atlântica; litoral atlântico; Mata Atlântica; Aliança Atlântica; clima atlântico
**Lexeme 2: Atlântico**, PROPN
- grammar: gender Masc; note used with the article: o Atlântico
- said: /a.ˈtlɐ̃.tʃi.ku/ (pt-BR), /a.tʃi.ˈlɐ̃.tʃi.ku/ (pt-BR, casual), /ɐ.ˈtlɐ̃.ti.ku/ (pt-PT)
  1. [A2] oceano que separa a Europa e a África das Américas
     - example: Muitos emigrantes atravessaram o Atlântico de navio há um século.
     - collocations: atravessar o Atlântico; do outro lado do Atlântico; Atlântico Norte; Atlântico Sul

### 48. gay

**Lexeme 1: gay**, ADJ
- grammar: note same form for masculine and feminine; plural gays or unchanged gay
- said: /ˈɡej/ (pt-BR), /ˈɡɐj/ (pt-PT), /ˈɡej/ (pt-PT)
  1. [B1] que sente atração amorosa e sexual por pessoas do mesmo sexo
     - example: O meu irmão é gay e vive com o namorado.
     - collocations: casal gay; homem gay; assumir-se gay
     - opposites: heterossexual
  2. [B2] das pessoas homossexuais ou feito para elas
     - example: Muitos países já reconhecem o casamento gay por lei.
     - collocations: casamento gay; parada gay; bar gay; comunidade gay; orgulho gay
     - grammar: comparable False
**Lexeme 2: gay**, NOUN
- grammar: gender Masc; countability Count; note usually of a man; plural gays
- said: /ˈɡej/ (pt-BR), /ˈɡɐj/ (pt-PT), /ˈɡej/ (pt-PT)
  1. [B1] pessoa, sobretudo homem, que sente atração por pessoas do mesmo sexo
     - example: Durante décadas, os gays lutaram pelo direito de se casar.
     - collocations: direitos dos gays
- relations: derived_from gay (ADJ)

### 49. fogão

**Lexeme 1: fogão**, NOUN
- grammar: gender Masc; countability Count
- said: /fo.ˈɡɐ̃w/ (pt-BR), /fu.ˈɡɐ̃w/ (pt-PT)
- forms: fogões [Number=Plur] /fo.ˈɡõjs/ /fu.ˈɡõjʃ/
  1. [A2] aparelho de cozinha com bocas e muitas vezes forno, onde se preparam alimentos
     - example: Deixei a sopa no fogão para não esfriar.
     - collocations: fogão a gás; fogão a lenha; fogão elétrico; acender o fogão; desligar o fogão; boca do fogão; no fogão
- relations: derived_from fogo (NOUN)

### 50. liceu

**Lexeme 1: liceu**, NOUN
- grammar: gender Masc; countability Count
- said: /li.ˈsew/ (pt-BR), /li.ˈsew/ (pt-PT)
  1. [B1] escola de ensino secundário, entre a escola básica e a universidade
     - example: Ainda me lembro bem dos amigos que fiz no liceu.
     - collocations: andar no liceu; no liceu; professor de liceu; tempo do liceu; entrar para o liceu
     - regions: pt-PT mainly
     - grammar: note in Portugal the official name is now escola secundária, but liceu is still common in speech

### 51. toque

**Lexeme 1: toque**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈtɔ.ki/ (pt-BR), /ˈtɔ.kɨ/ (pt-PT)
  1. [B1] contacto leve da mão ou do corpo com algo ou alguém
     - example: Senti um toque leve no ombro e olhei para trás.
     - frames: toque em algo/alguém
     - collocations: toque leve; toque suave; sentir um toque; ao toque
  2. [B1] som de aviso ou sinal produzido por um aparelho ou instrumento
     - example: Mudei o toque do telefone porque o antigo era irritante.
     - collocations: toque de recolher; toque da campainha; toque do sino; toque do telefone
  3. [B2] pequeno detalhe que dá um estilo especial a alguma coisa
     - example: As flores na mesa deram um toque de cor à sala.
     - frames: toque de algo
     - collocations: dar um toque; toque final; toque pessoal; toque de classe
  4. [B2] aviso ou conselho rápido dado a alguém de forma discreta
     - example: Obrigado pelo toque, eu já tinha esquecido a reunião.
     - frames: dar um toque em alguém; dar um toque a alguém
     - collocations: dar um toque
     - labels: formality informal
     - regions: pt-BR mainly
- relations: verbal_noun_of tocar (VERB)

### 52. sentença

**Lexeme 1: sentença**, NOUN
- grammar: gender Fem; countability Count
- said: /sẽ.ˈtẽ.sɐ/ (pt-BR), /sẽ.ˈtẽ.sɐ/ (pt-PT)
  1. [B2] decisão de um juiz ou tribunal que encerra um processo
     - example: O advogado vai recorrer da sentença assim que for possível.
     - frames: sentença contra alguém; sentença a favor de alguém
     - collocations: proferir a sentença; ditar a sentença; cumprir a sentença; recorrer da sentença; sentença condenatória; sentença absolutória; sentença de morte; ler a sentença
  2. [C1] conjunto de palavras com sentido completo, que forma uma unidade
     - example: Leia cada sentença com atenção antes de responder ao exercício.
     - labels: domain linguistics
     - regions: pt-BR mainly
  3. [C2] frase curta que exprime um ensinamento ou uma verdade geral
     - example: O velho professor gostava de repetir sentenças sobre a vida.
     - labels: style literary

### 53. socorro

**Lexeme 1: socorro**, NOUN
- grammar: gender Masc; note plural socorros is said with open ó
- said: /so.ˈko.ʁu/ (pt-BR), /su.ˈko.ʁu/ (pt-PT)
- forms: socorros [Number=Plur] /so.ˈkɔ.ʁus/ /su.ˈkɔ.ʁuʃ/
  1. [A2] ajuda dada a alguém que está em perigo ou em dificuldade
     - example: O homem gritou por socorro, mas ninguém o ouviu.
     - frames: pedir socorro a alguém; em socorro de alguém/algo
     - collocations: pedir socorro; gritar por socorro; primeiros socorros; prestar socorro; ir em socorro de; pedido de socorro; sem socorro
     - grammar: countability Mass
  2. [B1] pessoas ou serviço que vêm ajudar em casos de emergência
     - example: O socorro chegou ao local do acidente em dez minutos.
     - collocations: chamar o socorro; equipe de socorro; esperar o socorro; o socorro chegou
     - grammar: countability Mass
- relations: derived_from socorrer (VERB)
**Lexeme 2: socorro**, INTJ
- grammar: invariable True
- said: /so.ˈko.ʁu/ (pt-BR), /su.ˈko.ʁu/ (pt-PT)
  1. [A2] grito usado por quem está em perigo e precisa de ajuda
     - example: Socorro, alguém me ajude, há fogo na cozinha!

### 54. mangueira

**Lexeme 1: mangueira**, NOUN
- grammar: gender Fem; countability Count
- said: /mɐ̃.ˈɡej.ɾɐ/ (pt-BR), /mɐ̃.ˈɡɐj.ɾɐ/ (pt-PT)
  1. [B1] tubo comprido e flexível por onde passa água ou outro líquido
     - example: Todas as tardes rego o jardim com a mangueira.
     - collocations: mangueira de jardim; mangueira de incêndio; regar com a mangueira; abrir a mangueira; mangueira de borracha
  2. [B2] árvore tropical grande cujo fruto é doce, amarelo e de caroço grande
     - example: No quintal da avó havia uma mangueira carregada de frutos.
     - collocations: pé de mangueira; sombra da mangueira
- relations: derived_from manga (NOUN)

### 55. agressivo

**Lexeme 1: agressivo**, ADJ
- grammar: comparable True
- said: /a.ɡɾe.ˈsi.vu/ (pt-BR), /ɐ.ɡɾɨ.ˈsi.vu/ (pt-PT)
  1. [B1] que tem tendência a atacar ou a tratar os outros com violência
     - example: O cão ficou agressivo quando um estranho entrou no quintal.
     - frames: agressivo com alguém
     - collocations: comportamento agressivo; tom agressivo; ficar agressivo; atitude agressiva; cão agressivo
     - opposites: pacífico
  2. [B2] que age com muita força e determinação para vencer ou conseguir algo
     - example: A empresa lançou uma campanha de vendas muito agressiva este ano.
     - collocations: campanha agressiva; estratégia agressiva; marketing agressivo; política agressiva
- relations: derived_from agressão (NOUN)
