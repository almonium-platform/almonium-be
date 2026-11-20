# Spanish: entries to check (1 of 1)

These are entries of a learner's dictionary of Spanish, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of Spanish.

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
  sense, or an example is not natural Spanish.
- **collocation**: a collocation that is not said that way in Spanish; or one that does not belong
  among the ten a learner is shown for the sense, because anyone would build it from the meanings
  or because it is rare; or a list that lacks the commonest partner of the sense while it has
  rarer ones. Quote the collocation in "what". A frame with the wrong preposition or case is a
  fact, not this.

Do not report: the order of senses, a CEFR level off by one step, how syllables are divided, a
definition you would have worded differently when the given one is true, or anything about style.

These are this dictionary's own definitions, not faults:

- valency Refl stands for every use with a reflexive pronoun, whatever a grammar of Spanish calls it
  (reflexive, reciprocal, pronominal); Tr means the verb takes a direct object, so a verb whose
  object needs a preposition or the dative is Intr.
- a grammar value (countability, valency, position, number) says how the word is ordinarily used, as
  a learner's dictionary of Spanish prints it: "Mass" is "uncountable" in that sense, although kinds
  of the thing can be counted. Followed by "mainly", the other use is common too. Report a value
  only when a learner's dictionary would not print it for this word or sense.
- position Attr means the adjective is used joined to a noun and not after a verb such as "be",
  wherever Spanish puts it (before the noun in English, mostly after it in French); Pred means only
  after such a verb. Neither says on which side of the noun the adjective stands.
- a lexeme is one word with one way of saying and inflecting it. Two homonyms that are said and
  inflected alike are senses of one lexeme here, although dictionaries of Spanish number them apart.
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

### 1. enorme

**Lexeme 1: enorme**, ADJ
- grammar: note same form for masculine and feminine; plural enormes
- said: /e.ˈnoɾ.me/ (es-ES), /e.ˈnoɾ.me/ (es-MX)
  1. [A2] muy grande en tamaño, cantidad o intensidad, mucho más de lo normal
     - example: Vivimos al lado de un parque enorme lleno de árboles.
     - opposites: diminuto

### 2. reciente

**Lexeme 1: reciente**, ADJ
- grammar: comparable True; note same form for masculine and feminine; plural recientes
- said: /re.ˈθjen.te/ (es-ES), /re.ˈsjen.te/ (es-MX)
- forms: recentísimo [Degree=Abs]
  1. [B1] que ha ocurrido, se ha hecho o ha aparecido hace poco tiempo
     - example: Según un estudio reciente, cada vez leemos menos en papel.
     - collocations: estudio reciente; historia reciente; pasado reciente; de reciente creación; en fechas recientes; hasta fecha reciente; el más reciente
     - opposites: antiguo

### 3. sorpresa

**Lexeme 1: sorpresa**, NOUN
- grammar: gender Fem; countability Both
- said: /soɾ.ˈpɾe.sa/ (es-ES), /soɾ.ˈpɾe.sa/ (es-MX)
  1. [A2] impresión que causa en alguien algo que no esperaba
     - example: Para mi sorpresa, el examen fue mucho más fácil de lo previsto.
     - frames: sorpresa ante algo
     - collocations: llevarse una sorpresa; para mi sorpresa; coger por sorpresa; pillar por sorpresa; causar sorpresa; con sorpresa; gran sorpresa
     - grammar: countability Mass, mainly
  2. [A2] cosa o suceso que alguien no espera, a menudo preparado para alegrarlo
     - example: Te tengo una sorpresa guardada en el cajón de la mesa.
     - collocations: dar una sorpresa; fiesta sorpresa; sorpresa agradable; menuda sorpresa; ataque por sorpresa; de sorpresa
     - grammar: countability Count
- relations: derived_from sorprender (VERB)
**Lexeme 2: sorpresa**, INTJ
- grammar: invariable True
- said: /soɾ.ˈpɾe.sa/ (es-ES), /soɾ.ˈpɾe.sa/ (es-MX)
  1. [A2] se usa para presentar a alguien algo inesperado que se le ha preparado
     - example: ¡Sorpresa!, hemos preparado una cena para celebrar tu cumpleaños.
     - labels: formality informal

### 4. escala

**Lexeme 1: escala**, NOUN
- grammar: gender Fem; countability Count
- said: /es.ˈka.la/ (es-ES), /es.ˈka.la/ (es-MX)
  1. [B1] serie ordenada de grados o niveles que sirve para medir, comparar o clasificar
     - example: Valora tu dolor en una escala del uno al diez.
     - collocations: escala de valores; escala salarial; escala jerárquica; escala de grises
  2. [B1] parada que hace un avión o un barco en un lugar intermedio de su viaje
     - example: El avión hizo una escala de dos horas antes de llegar.
     - frames: hacer escala en + lugar
     - collocations: hacer escala; vuelo sin escalas; escala técnica
  3. [B2] relación entre las medidas de un mapa, dibujo o modelo y las medidas reales
     - example: Este mapa está dibujado a una escala de uno a cien mil.
     - collocations: a escala; maqueta a escala; escala del mapa
  4. [B2] tamaño o importancia que tiene una acción, un plan o un fenómeno
     - example: La empresa quiere fabricar el nuevo modelo a gran escala.
     - collocations: a gran escala; a pequeña escala; a escala mundial; a escala nacional
  5. [B2] serie ordenada de notas musicales que suben o bajan por grados
     - example: Cada día practico escalas en el piano durante media hora.
     - collocations: escala mayor; escala menor; tocar escalas; escala cromática
     - labels: domain music
  6. [C1] utensilio portátil de cuerda o madera con peldaños para subir y bajar
     - example: Los marineros bajaron al bote por una escala de cuerda.
     - collocations: escala de cuerda

### 5. seguramente

**Lexeme 1: seguramente**, ADV
- grammar: invariable True
- said: /se.ˌɡu.ɾa.ˈmen.te/ (es-ES), /se.ˌɡu.ɾa.ˈmen.te/ (es-MX)
  1. [A2] con mucha probabilidad, aunque sin tener la certeza completa
     - example: Seguramente llegaremos tarde por culpa del tráfico de hoy.
     - frames: seguramente + ind; seguramente + subj
     - grammar: note usually followed by the indicative; the subjunctive is also used
  2. [C1] sin riesgo ni peligro, con la debida protección
     - example: Los datos de los clientes se guardan seguramente en el servidor.
- relations: adverb_of seguro (ADJ)

### 6. beneficio

**Lexeme 1: beneficio**, NOUN
- grammar: gender Masc; countability Count
- said: /be.ne.ˈfi.θjo/ (es-ES), /be.ne.ˈfi.sjo/ (es-MX)
  1. [B1] bien o ventaja que alguien recibe o que algo produce
     - example: Caminar cada día tiene muchos beneficios para la salud.
     - frames: beneficio para alguien/algo; en beneficio de alguien/algo; a beneficio de alguien/algo
     - collocations: sacar beneficio; beneficio mutuo; en beneficio propio; obtener un beneficio; el beneficio de la duda
     - opposites: perjuicio
  2. [B2] dinero que se gana en un negocio después de restar los gastos
     - example: Este año la empresa ha obtenido unos beneficios muy altos.
     - collocations: obtener beneficios; dar beneficios; reportar beneficios; beneficio neto; beneficio bruto; margen de beneficio
     - grammar: note often plural: los beneficios
     - opposites: pérdida

### 7. estudiar

**Lexeme 1: estudiar**, VERB
- grammar: conjugation 1; irregular False; valency Both
- said: /es.tu.ˈdjaɾ/ (es-ES), /es.tu.ˈdjaɾ/ (es-MX)
  1. [A1] esforzarse por aprender algo leyendo, practicando o memorizando
     - example: Esta noche tengo que estudiar para el examen de historia.
     - frames: estudiar algo; estudiar para algo
     - grammar: valency Both
  2. [A1] seguir los cursos de una carrera o materia en un centro de enseñanza
     - example: Mi hermana estudia medicina en una universidad pública.
     - frames: estudiar algo; estudiar en + lugar
     - grammar: valency Both
  3. [B1] examinar algo con atención para entenderlo o tomar una decisión
     - example: Vamos a estudiar tu propuesta con calma antes de responder.
     - grammar: valency Tr

### 8. discusión

**Lexeme 1: discusión**, NOUN
- grammar: gender Fem; countability Count
- said: /dis.ku.ˈsjon/ (es-ES), /dis.ku.ˈsjon/ (es-MX)
  1. [B1] conversación en la que varias personas examinan un tema y exponen sus opiniones
     - example: Después de la película hubo una discusión muy interesante sobre el final.
     - frames: discusión sobre algo; discusión de algo
     - collocations: someter a discusión; estar en discusión; fuera de discusión; sin discusión; abrir una discusión; discusión abierta
  2. [B1] intercambio de palabras en el que dos o más personas muestran su desacuerdo, a menudo con enfado
     - example: Anoche tuve una discusión con mi vecino por el ruido.
     - frames: discusión con alguien; discusión por algo
     - collocations: tener una discusión; discusión acalorada; una fuerte discusión; meterse en una discusión; discusión de pareja
- relations: verbal_noun_of discutir (VERB)

### 9. intervención

**Lexeme 1: intervención**, NOUN
- grammar: gender Fem
- said: /in.teɾ.ben.ˈθjon/ (es-ES), /in.teɾ.ben.ˈsjon/ (es-MX)
- forms: intervenciones [Number=Plur]
  1. [B2] participación activa en un asunto para influir en su desarrollo o resultado
     - example: La rápida intervención de los bomberos evitó una tragedia mayor.
     - frames: intervención en algo; intervención de alguien
     - collocations: intervención militar; intervención del Estado; intervención humanitaria; intervención extranjera; pedir la intervención; sin intervención de nadie
     - grammar: countability Both
  2. [B2] lo que dice una persona cuando toma la palabra en una reunión o debate
     - example: Tu intervención en la reunión de ayer convenció a todos.
     - frames: intervención en algo
     - collocations: breve intervención; turno de intervención; en su intervención
     - grammar: countability Count
  3. [B2] operación que hace un cirujano en el cuerpo de un paciente
     - example: Mi abuelo se recupera bien después de la intervención de rodilla.
     - collocations: intervención quirúrgica; someterse a una intervención; intervención de urgencia
     - grammar: countability Count
- relations: verbal_noun_of intervenir (VERB)

### 10. sentencia

**Lexeme 1: sentencia**, NOUN
- grammar: gender Fem; countability Count
- said: /sen.ˈten.θja/ (es-ES), /sen.ˈten.sja/ (es-MX)
  1. [B2] decisión final de un juez o un tribunal que resuelve un juicio
     - example: El juez dictará sentencia la próxima semana tras escuchar a los testigos.
     - frames: sentencia contra alguien
     - collocations: dictar sentencia; sentencia firme; recurrir una sentencia; apelar una sentencia; cumplir una sentencia; sentencia condenatoria; sentencia absolutoria; sentencia de muerte
  2. [C1] frase breve que expresa una enseñanza o una verdad moral
     - example: Mi abuelo siempre repetía aquella vieja sentencia sobre la paciencia.
     - labels: style literary

### 11. imaginar

**Lexeme 1: imaginar**, VERB
- grammar: conjugation 1; irregular False; valency Tr
- said: /i.ma.xi.ˈnaɾ/ (es-ES), /i.ma.xi.ˈnaɾ/ (es-MX)
  1. [A2] crear o ver en el pensamiento algo que no es real o no está delante
     - example: Imagina una playa tranquila al atardecer y relájate un poco.
     - frames: imaginar algo; imaginarse algo; imaginarse a alguien; imaginar que + ind
     - collocations: no te puedes imaginar; difícil de imaginar; imaginar el futuro; imaginarse la escena
     - grammar: valency Tr; note often used as imaginarse with the same meaning
  2. [B1] creer o suponer algo a partir de pocos datos
     - example: Me imagino que estarás cansado después de un viaje tan largo.
     - frames: imaginar que + ind; imaginarse que + ind; imaginarse algo
     - collocations: ya me lo imaginaba; me imagino que sí
     - grammar: valency Tr; note mostly used as imaginarse

### 12. significado

**Lexeme 1: significado**, NOUN
- grammar: gender Masc; countability Count
- said: /siɡ.ni.fi.ˈka.do/ (es-ES), /siɡ.ni.fi.ˈka.do/ (es-MX)
  1. [A2] idea o contenido que expresa una palabra, una frase o un signo
     - example: Busca en el diccionario el significado de las palabras nuevas.
     - frames: el significado de algo
     - collocations: buscar el significado; doble significado; significado literal; significado figurado; tener significado
  2. [B2] importancia o valor que algo tiene para alguien o en una situación
     - example: Aquel viaje tuvo un significado especial para toda la familia.
     - frames: el significado de algo; significado para alguien
     - collocations: significado especial; significado histórico; significado profundo; comprender el significado
- relations: derived_from significar (VERB)
**Lexeme 2: significado**, ADJ
- grammar: position Attr, mainly; comparable False
- said: /siɡ.ni.fi.ˈka.do/ (es-ES), /siɡ.ni.fi.ˈka.do/ (es-MX)
  1. [C2] conocido y respetado por su importancia dentro de un grupo
     - example: Varios significados miembros del partido asistieron a la reunión.
     - labels: formality formal
- relations: derived_from significar (VERB)

### 13. mezcla

**Lexeme 1: mezcla**, NOUN
- grammar: gender Fem
- said: /ˈmeθ.kla/ (es-ES), /ˈmes.kla/ (es-MX)
  1. [B1] conjunto formado por cosas distintas unidas o combinadas entre sí
     - example: Su mirada mostraba una extraña mezcla de miedo y curiosidad.
     - frames: mezcla de algo
     - collocations: una extraña mezcla; mezcla explosiva; preparar una mezcla; mezcla homogénea; batir la mezcla
     - grammar: countability Count
  2. [B2] acción de juntar cosas distintas para que formen un todo
     - example: La mezcla de los ingredientes debe hacerse poco a poco.
     - frames: mezcla de algo
     - collocations: mezcla de culturas
     - grammar: countability Mass, mainly
  3. [C1] proceso de ajustar y combinar las distintas pistas de sonido de una grabación
     - example: El grupo terminó la mezcla del nuevo disco en un estudio pequeño.
     - collocations: mesa de mezclas
     - labels: domain music
     - grammar: countability Count
- relations: verbal_noun_of mezclar (VERB)

### 14. elegido

**Lexeme 1: elegido**, NOUN
- grammar: gender Masc; countability Count; note feminine elegida for a woman; often in the plural
- said: /e.le.ˈxi.do/ (es-ES), /e.le.ˈxi.do/ (es-MX)
- forms: elegida [Gender=Fem, Number=Sing]; elegidas [Gender=Fem, Number=Plur]
  1. [B2] persona escogida entre muchas para algo especial o por el destino
     - example: Solo unos pocos elegidos pudieron entrar en aquella fiesta privada.
     - collocations: unos pocos elegidos; el elegido
  2. [C1] persona que Dios ha destinado a salvarse y gozar de la vida eterna
     - example: Según aquel predicador, los elegidos verían el rostro de Dios.
     - collocations: los elegidos de Dios
     - labels: domain religion
     - grammar: number Plur, mainly
- relations: derived_from elegir (VERB)

### 15. reír

**Lexeme 1: reír**, VERB
- grammar: conjugation 3; irregular True; note often used as reírse with the same meaning
- said: /re.ˈiɾ/ (es-ES), /re.ˈiɾ/ (es-MX)
- forms: río [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; ríes [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin]; ríe [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin]; reímos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin]; ríen [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin]; reí [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin]; rio [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin]; reiré [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin]; ría [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; reído [VerbForm=Part, Tense=Past]; riendo [VerbForm=Ger]
  1. [A2] expresar alegría o diversión con sonidos y movimientos de la boca y la cara
     - example: Los niños no paraban de reír con las bromas del payaso.
     - collocations: reír a carcajadas; echarse a reír; hacer reír; reírse a carcajadas; reír a mandíbula batiente
     - grammar: valency Intr; note reír and reírse are both common here
  2. [B1] burlarse de alguien o de algo, o no tomarlo en serio
     - example: No te rías de tu hermano, que lo está intentando.
     - frames: reírse de algo/alguien
     - collocations: reírse en la cara de alguien
     - grammar: valency Refl
  3. [B2] celebrar con carcajadas lo que otra persona dice o hace
     - example: Todos le ríen los chistes al jefe, aunque no tengan gracia.
     - frames: reírle algo a alguien
     - collocations: reírle las gracias a alguien
     - grammar: valency Tr

### 16. impuesto

**Lexeme 1: impuesto**, NOUN
- grammar: gender Masc; countability Count
- said: /im.ˈpwes.to/ (es-ES), /im.ˈpwes.to/ (es-MX)
  1. [B1] dinero que las personas y las empresas están obligadas a pagar al Estado
     - example: Este año vamos a pagar más impuestos por la casa.
     - frames: impuesto sobre algo
     - collocations: pagar impuestos; impuesto sobre la renta; subir los impuestos; bajar los impuestos; cobrar un impuesto; evadir impuestos; libre de impuestos; impuesto sobre el valor añadido
- relations: derived_from imponer (VERB)

### 17. clima

**Lexeme 1: clima**, NOUN
- grammar: gender Masc; countability Count; note masculine although it ends in -a: el clima
- said: /ˈkli.ma/ (es-ES), /ˈkli.ma/ (es-MX)
  1. [A2] conjunto de condiciones del tiempo atmosférico propias de una región
     - example: El clima de esta costa es suave durante casi todo el año.
     - collocations: clima templado; clima tropical; clima mediterráneo; clima seco; clima húmedo; clima frío; clima cálido
  2. [A2] estado de la atmósfera en un lugar y un momento concretos
     - example: Hoy el clima está perfecto para salir a caminar.
     - collocations: buen clima; mal clima
     - regions: es-419 mainly
     - grammar: number Sing
  3. [B2] ambiente formado por las circunstancias y actitudes que rodean a una persona o grupo
     - example: Las negociaciones se desarrollaron en un clima de confianza.
     - collocations: clima de tensión; clima de confianza; clima de violencia; clima laboral; clima político; crear un clima
     - labels: figurative True
  4. [B1] aparato que enfría o calienta el aire de un espacio cerrado
     - example: Prende el clima, que en esta oficina hace mucho calor.
     - collocations: prender el clima; apagar el clima
     - labels: formality informal
     - regions: es-MX only

### 18. permitir

**Lexeme 1: permitir**, VERB
- grammar: conjugation 3; irregular False
- said: /peɾ.mi.ˈtiɾ/ (es-ES), /peɾ.mi.ˈtiɾ/ (es-MX)
  1. [A2] dar a alguien el consentimiento para hacer algo, o no impedirlo
     - example: Mis padres no me permiten salir hasta tan tarde.
     - frames: permitir algo a alguien; permitir a alguien + inf; permitir que + subj
     - collocations: no se permite fumar; permitir la entrada; permitir el paso
     - grammar: valency Tr
     - opposites: prohibir
  2. [B1] hacer posible o más fácil que algo ocurra
     - example: Esta nueva máquina nos permite trabajar mucho más rápido.
     - frames: permitir + inf; permitir algo; permitir que + subj
     - grammar: valency Tr
  3. [B1] tener el dinero, el tiempo o la ocasión para hacer o tener algo
     - example: Este año no podemos permitirnos unas vacaciones en la playa.
     - frames: permitirse algo; permitirse + inf
     - collocations: poder permitirse; permitirse el lujo
     - grammar: valency Refl; reflexive True
  4. [B2] tomarse la libertad de hacer o decir algo
     - example: Me permito recordarle que la reunión empieza a las nueve.
     - frames: permitirse + inf
     - labels: formality formal
     - grammar: valency Refl; reflexive True

### 19. víctima

**Lexeme 1: víctima**, NOUN
- grammar: gender Fem; countability Count; note always feminine, also when it refers to a man: él fue la víctima
- said: /ˈbik.ti.ma/ (es-ES), /ˈbik.ti.ma/ (es-MX)
  1. [B1] persona o animal que sufre un daño causado por otros o por algo
     - example: Mi vecino fue víctima de un robo la semana pasada.
     - frames: víctima de algo/alguien
     - collocations: ser víctima de; hacerse la víctima; víctima inocente; víctimas del terrorismo; víctima de la violencia; víctima de un engaño
  2. [B1] persona que muere a causa de un accidente, una violencia o una desgracia
     - example: El terremoto dejó cientos de víctimas en la región.
     - collocations: víctima mortal; cobrarse víctimas; causar víctimas; número de víctimas
  3. [C1] persona o animal que se mata como ofrenda a un dios
     - example: Los sacerdotes llevaban la víctima al altar del templo.
     - labels: domain religion

### 20. vender

**Lexeme 1: vender**, VERB
- grammar: conjugation 2; irregular False
- said: /ben.ˈdeɾ/ (es-ES), /ben.ˈdeɾ/ (es-MX)
  1. [A1] dar algo o hacer un servicio a otra persona a cambio de un precio
     - example: Mis vecinos quieren vender su casa antes del verano.
     - frames: vender algo a alguien; vender algo por + cantidad
     - collocations: vender barato; vender caro; vender al por mayor; vender a plazos; se vende; vender entradas
     - grammar: valency Tr, mainly
     - opposites: comprar
  2. [B1] encontrar compradores un producto, en la cantidad o al precio que se dice
     - example: Sus novelas se venden muy bien en todo el país.
     - frames: venderse a + precio
     - collocations: venderse bien; venderse como churros
     - grammar: valency Refl; note used with se; the thing sold is the subject
  3. [B2] presentar algo o a alguien de manera que otros lo acepten o lo valoren
     - example: Hay que saber vender bien el proyecto a los inversores.
     - frames: vender algo a alguien
     - collocations: vender una idea; saber venderse; vender una imagen
     - grammar: valency Tr
  4. [C1] traicionar a alguien o algo a cambio de dinero u otro beneficio
     - example: Aquel soldado vendió a sus compañeros por unas monedas.
     - frames: vender a alguien por algo; venderse a alguien
     - grammar: valency Tr; note venderse: to let oneself be bribed

### 21. definitivamente

**Lexeme 1: definitivamente**, ADV
- grammar: invariable True
- said: /de.fi.ni.ˌti.ba.ˈmen.te/ (es-ES), /de.fi.ni.ˌti.ba.ˈmen.te/ (es-MX)
  1. [B1] de manera final, sin posibilidad de cambio ni vuelta atrás
     - example: La fábrica del pueblo cerrará definitivamente a finales de año.
     - collocations: cerrar definitivamente; abandonar definitivamente; resolver definitivamente; instalarse definitivamente
     - opposites: provisionalmente
  2. [B1] sin ninguna duda, con total seguridad
     - example: Definitivamente, este es el mejor restaurante que conozco.
- relations: adverb_of definitivo (ADJ)

### 22. tranquilo

**Lexeme 1: tranquilo**, ADJ
- grammar: comparable True
- said: /tɾan.ˈki.lo/ (es-ES), /tɾan.ˈki.lo/ (es-MX)
  1. [A2] que no está nervioso ni preocupado y no se altera con facilidad
     - example: Puedes estar tranquilo, yo me encargo de todo esta noche.
     - frames: dejar tranquilo a alguien
     - collocations: estar tranquilo; quedarse tranquilo; conciencia tranquila; dejar tranquilo; mantenerse tranquilo
     - opposites: nervioso
  2. [A2] sin ruido, movimiento ni problemas que molesten
     - example: Buscamos un pueblo tranquilo para pasar las vacaciones con los niños.
     - collocations: barrio tranquilo; vida tranquila; noche tranquila; mar tranquilo; zona tranquila; ambiente tranquilo
     - opposites: agitado
**Lexeme 2: tranquilo**, INTJ
- grammar: note agrees with the person spoken to: tranquila, tranquilos, tranquilas
- said: /tɾan.ˈki.lo/ (es-ES), /tɾan.ˈki.lo/ (es-MX)
- forms: tranquila [Gender=Fem, Number=Sing]; tranquilos [Gender=Masc, Number=Plur]; tranquilas [Gender=Fem, Number=Plur]
  1. [A2] se usa para pedir a alguien que se calme o que no se preocupe
     - example: Tranquilo, hombre, que no ha pasado nada grave.
     - labels: formality informal

### 23. descubrir

**Lexeme 1: descubrir**, VERB
- grammar: conjugation 3; irregular True; note irregular only in the past participle descubierto
- said: /des.ku.ˈbɾiɾ/ (es-ES), /des.ku.ˈbɾiɾ/ (es-MX)
- forms: descubro [Mood=Ind, Tense=Pres, Person=1, Number=Sing]; descubres [Mood=Ind, Tense=Pres, Person=2, Number=Sing]; descubre [Mood=Ind, Tense=Pres, Person=3, Number=Sing]; descubrimos [Mood=Ind, Tense=Pres, Person=1, Number=Plur]; descubren [Mood=Ind, Tense=Pres, Person=3, Number=Plur]; descubrí [Mood=Ind, Tense=Past, Person=1, Number=Sing]; descubrió [Mood=Ind, Tense=Past, Person=3, Number=Sing]; descubriré [Mood=Ind, Tense=Fut, Person=1, Number=Sing]; descubra [Mood=Sub, Tense=Pres, Person=1, Number=Sing]; descubierto [VerbForm=Part, Tense=Past]; descubriendo [VerbForm=Ger]
  1. [A2] llegar a saber algo que antes no se sabía
     - example: Al final descubrimos que el restaurante cerraba los lunes.
     - frames: descubrir algo; descubrir que + ind
     - collocations: descubrir la verdad; descubrir un error; descubrir por casualidad; descubrir de repente
     - grammar: valency Tr
  2. [B1] encontrar por primera vez algo que existía y nadie conocía
     - example: Unos científicos han descubierto una nueva especie de rana en la selva.
     - frames: descubrir algo
     - collocations: descubrir una vacuna; descubrir un planeta; descubrir una cura; descubrir un tesoro; descubrir América
     - grammar: valency Tr
  3. [B2] quitar lo que tapa algo para dejarlo a la vista
     - example: El alcalde descubrió la placa delante de todos los vecinos.
     - frames: descubrir algo
     - collocations: descubrir una placa; descubrir una estatua
     - grammar: valency Tr
     - opposites: cubrir
  4. [B2] dar a conocer algo secreto o a la persona que se escondía
     - example: Aquella carta descubrió sus verdaderas intenciones a toda la familia.
     - frames: descubrir algo; descubrir a alguien
     - collocations: descubrir un secreto; descubrir el juego; descubrir las intenciones
     - grammar: valency Tr
     - opposites: ocultar
  5. [C1] quitarse el sombrero o lo que lleva en la cabeza, a menudo en señal de respeto
     - example: Todos nos descubrimos ante el valor de aquella mujer.
     - frames: descubrirse ante alguien/algo
     - grammar: valency Refl; reflexive True; note often figurative: to show great admiration
- relations: derived_from cubrir (VERB)

### 24. miércoles

**Lexeme 1: miércoles**, NOUN
- grammar: gender Masc; countability Count; note same form in the plural: el miércoles, los miércoles
- said: /ˈmjeɾ.ko.les/ (es-ES), /ˈmjeɾ.ko.les/ (es-MX)
- forms: miércoles [Number=Plur]
  1. [A1] día de la semana que va después del martes y antes del jueves
     - example: Los miércoles por la tarde tengo clase de natación.
     - collocations: el miércoles; los miércoles; Miércoles de Ceniza
**Lexeme 2: miércoles**, INTJ
- grammar: invariable True
- said: /ˈmjeɾ.ko.les/ (es-ES), /ˈmjeɾ.ko.les/ (es-MX)
  1. [B2] se usa para expresar enfado, sorpresa o disgusto sin decir una palabrota
     - example: Miércoles, otra vez se me olvidaron las llaves en casa.
     - labels: formality informal, attitude euphemistic

### 25. regresar

**Lexeme 1: regresar**, VERB
- grammar: conjugation 1; irregular False
- said: /re.ɡɾe.ˈsaɾ/ (es-ES), /re.ɡɾe.ˈsaɾ/ (es-MX)
  1. [A2] ir o venir de nuevo al lugar de donde se salió
     - example: Mañana regresamos a casa después de dos semanas de viaje.
     - frames: regresar a + lugar; regresar de + lugar
     - collocations: regresar a casa; regresar del trabajo; regresar sano y salvo; regresar a tiempo
     - grammar: valency Intr; note in Latin America also regresarse with the same meaning
  2. [B1] dar una cosa a su dueño o a quien la tenía antes
     - example: Te regreso el libro en cuanto termine de leerlo.
     - frames: regresar algo a alguien
     - regions: es-419 only
     - grammar: valency Tr

### 26. acceder

**Lexeme 1: acceder**, VERB
- grammar: conjugation 2; irregular False; valency Intr
- said: /ak.θe.ˈdeɾ/ (es-ES), /ak.se.ˈdeɾ/ (es-MX)
  1. [B1] entrar en un lugar o poder llegar hasta él
     - example: Se accede al museo por una puerta lateral muy pequeña.
     - frames: acceder a + lugar
     - collocations: acceder al interior; acceder al edificio
  2. [B1] conseguir entrar en un sistema informático o consultar información guardada en él
     - example: Desde ayer no puedo acceder a mi cuenta de correo.
     - frames: acceder a algo
     - collocations: acceder a internet; acceder a la cuenta; acceder a los datos; acceder a la información
  3. [B2] llegar a tener un puesto, un nivel o una situación mejor
     - example: Muchos jóvenes no consiguen acceder a una vivienda propia.
     - frames: acceder a algo
     - collocations: acceder al poder; acceder al trono; acceder a la universidad; acceder a un puesto; acceder a un empleo
  4. [B2] aceptar lo que otra persona pide o propone
     - example: Después de mucho insistir, mi padre accedió a prestarme el coche.
     - frames: acceder a algo; acceder a + inf
     - collocations: acceder a una petición; acceder a las demandas
     - opposites: negarse

### 27. pasión

**Lexeme 1: pasión**, NOUN
- grammar: gender Fem; countability Both
- said: /pa.ˈsjon/ (es-ES), /pa.ˈsjon/ (es-MX)
  1. [B1] gusto o interés muy grande por una actividad o una cosa
     - example: Desde niño, la música ha sido siempre mi gran pasión.
     - frames: pasión por algo
     - collocations: sentir pasión por algo; con pasión; gran pasión; pasión por la lectura
     - grammar: countability Count, mainly
  2. [B2] sentimiento muy intenso que puede dominar la voluntad y la razón
     - example: Los celos son una pasión muy difícil de controlar.
     - collocations: dejarse llevar por la pasión; despertar pasiones; levantar pasiones
  3. [B2] atracción amorosa y deseo muy intensos hacia otra persona
     - example: Entre ellos nació una pasión que duró toda la vida.
     - frames: pasión por alguien
     - collocations: pasión amorosa; sentir pasión por alguien; pasión desenfrenada; pasión ciega; con pasión
  4. [C1] sufrimientos de Jesucristo desde que lo apresaron hasta su muerte en la cruz
     - example: En muchos pueblos se representa la Pasión durante la Semana Santa.
     - collocations: la Pasión de Cristo; domingo de Pasión
     - labels: domain religion
     - grammar: number Sing; note usually capitalized: la Pasión

### 28. lunes

**Lexeme 1: lunes**, NOUN
- grammar: gender Masc; countability Count; note same form in the plural: el lunes, los lunes
- said: /ˈlu.nes/ (es-ES), /ˈlu.nes/ (es-MX)
- forms: lunes [Number=Plur]
  1. [A1] día de la semana que va después del domingo y antes del martes
     - example: Los lunes siempre me cuesta mucho levantarme temprano.
     - collocations: el lunes; los lunes

### 29. genial

**Lexeme 1: genial**, ADJ
- grammar: comparable True; note same form for masculine and feminine
- said: /xe.ˈnjal/ (es-ES), /xe.ˈnjal/ (es-MX)
  1. [A2] muy bueno, estupendo, que gusta mucho
     - example: Tu plan para el fin de semana me parece genial.
     - labels: formality informal
     - opposites: horrible
  2. [B2] que tiene o demuestra un talento o una inteligencia extraordinarios
     - example: Fue un escritor genial que cambió la novela moderna.
- relations: adjective_of genio (NOUN)
**Lexeme 2: genial**, ADV
- grammar: invariable True
- said: /xe.ˈnjal/ (es-ES), /xe.ˈnjal/ (es-MX)
  1. [A2] muy bien o de una manera muy agradable
     - example: Lo pasamos genial durante las vacaciones de verano.
     - collocations: pasarlo genial; sentar genial
     - labels: formality informal

### 30. musical

**Lexeme 1: musical**, ADJ
- grammar: note same form for masculine and feminine; plural musicales
- said: /mu.si.ˈkal/ (es-ES), /mu.si.ˈkal/ (es-MX)
  1. [A2] de los sonidos combinados con arte y ritmo, o que sirve para producirlos
     - example: En casa tenemos varios instrumentos musicales que nadie sabe tocar.
     - collocations: instrumento musical; género musical; grupo musical; banda musical; nota musical; educación musical; oído musical; comedia musical
     - grammar: comparable False
  2. [B2] que tiene facilidad o talento para cantar o tocar instrumentos
     - example: Todos los niños de esa familia son muy musicales.
     - grammar: comparable True
  3. [C1] que tiene un sonido agradable y armonioso
     - example: Me encanta escuchar su voz suave y musical por la radio.
     - collocations: voz musical
     - grammar: comparable True
- relations: adjective_of música (NOUN)
**Lexeme 2: musical**, NOUN
- grammar: gender Masc; countability Count
- said: /mu.si.ˈkal/ (es-ES), /mu.si.ˈkal/ (es-MX)
  1. [B1] obra de teatro o película que cuenta una historia con canciones y bailes
     - example: Este fin de semana vamos a ver un musical en el centro.
     - collocations: ver un musical; estrenar un musical; protagonizar un musical

### 31. martes

**Lexeme 1: martes**, NOUN
- grammar: gender Masc; countability Count; note same form in the plural: el martes, los martes
- said: /ˈmaɾ.tes/ (es-ES), /ˈmaɾ.tes/ (es-MX)
- forms: martes [Number=Plur]
  1. [A1] día de la semana que va después del lunes y antes del miércoles
     - example: Tengo clase de inglés todos los martes por la tarde.
     - collocations: el martes; los martes; martes y trece

### 32. combate

**Lexeme 1: combate**, NOUN
- grammar: gender Masc; countability Count
- said: /kom.ˈba.te/ (es-ES), /kom.ˈba.te/ (es-MX)
  1. [B1] lucha violenta con armas entre personas, grupos o ejércitos
     - example: Muchos soldados murieron en combate durante aquella larga guerra.
     - collocations: entrar en combate; morir en combate; combate cuerpo a cuerpo; zona de combate; avión de combate; combate naval; duro combate
  2. [B1] pelea deportiva entre dos personas que siguen unas reglas
     - example: El boxeador ganó el combate en el tercer asalto.
     - collocations: dejar fuera de combate; combate de boxeo; ganar el combate; perder el combate; combate por el título
  3. [B2] esfuerzo organizado para acabar con un mal o un problema
     - example: El gobierno anunció nuevas medidas para el combate contra la pobreza.
     - frames: combate contra algo; combate de algo; combate a algo
     - labels: figurative True
- relations: derived_from combatir (VERB)

### 33. responsable

**Lexeme 1: responsable**, ADJ
- grammar: comparable True
- said: /res.pon.ˈsa.ble/ (es-ES), /res.pon.ˈsa.ble/ (es-MX)
  1. [B1] que tiene la obligación de cuidar algo o dar cuenta de ello
     - example: Cada alumno es responsable de su propio material escolar.
     - frames: responsable de algo; responsable de + inf
  2. [B1] que cumple sus obligaciones y actúa con cuidado y sensatez
     - example: Mi hermana mayor siempre ha sido muy responsable con el dinero.
     - opposites: irresponsable
  3. [B2] que causa un hecho o influye de manera decisiva en él
     - example: La falta de lluvia es responsable de esta mala cosecha.
     - frames: responsable de algo
**Lexeme 2: responsable**, NOUN
- grammar: gender Com; countability Count
- said: /res.pon.ˈsa.ble/ (es-ES), /res.pon.ˈsa.ble/ (es-MX)
  1. [B1] persona que tiene a su cargo una actividad, un grupo o un área
     - example: Hable usted con la responsable de ventas, por favor.
     - frames: responsable de algo
  2. [B2] persona que debe responder de un hecho o de sus consecuencias
     - example: Los responsables del robo fueron detenidos ayer por la tarde.
     - frames: responsable de algo

### 34. dignidad

**Lexeme 1: dignidad**, NOUN
- grammar: gender Fem
- said: /diɡ.ni.ˈdad/ (es-ES), /diɡ.ni.ˈdad/ (es-MX)
  1. [B2] valor que tiene toda persona por el que merece ser respetada
     - example: Todos los trabajadores merecen un trato que respete su dignidad.
     - collocations: dignidad humana; vivir con dignidad; morir con dignidad; respetar la dignidad; atentar contra la dignidad; herir la dignidad
     - grammar: countability Mass
  2. [B2] manera seria y honrada de comportarse que hace que los demás respeten a alguien
     - example: Supo aceptar la derrota con mucha dignidad y sin quejarse.
     - collocations: comportarse con dignidad; perder la dignidad; mantener la dignidad; conservar la dignidad
     - grammar: countability Mass
  3. [C1] cargo o puesto importante que da autoridad y honor a quien lo ocupa
     - example: Durante años ocupó las más altas dignidades del Estado.
     - collocations: dignidad eclesiástica
     - labels: style elevated
     - grammar: countability Count
- relations: abstract_noun_of digno (ADJ)

### 35. comenzar

**Lexeme 1: comenzar**, VERB
- grammar: conjugation 1; irregular True; valency Both; note stem changes e > ie when stressed (comienzo); z becomes c before e (comencé)
- said: /ko.men.ˈθaɾ/ (es-ES), /ko.men.ˈsaɾ/ (es-MX)
- forms: comienzo [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; comienzas [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin]; comienza [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin]; comenzamos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin]; comienzan [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin]; comencé [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin]; comenzó [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin]; comenzaré [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin]; comience [Mood=Sub, Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin]; comenzado [VerbForm=Part, Tense=Past]; comenzando [VerbForm=Ger]
  1. [A2] dar principio a una acción, una tarea o una actividad
     - example: Comenzó a llover justo cuando salíamos de casa.
     - frames: comenzar algo; comenzar a + inf; comenzar por algo
     - collocations: comenzar de nuevo; comenzar de cero; comenzar un proyecto; comenzar una nueva vida
     - grammar: valency Tr; note with a + inf it takes no direct object
     - opposites: terminar
  2. [A2] tener principio una cosa en el tiempo o en el espacio
     - example: El concierto comienza a las nueve en punto.
     - frames: comenzar con algo; comenzar en + lugar
     - collocations: comenzar puntualmente
     - grammar: valency Intr
     - opposites: terminar

### 36. jornada

**Lexeme 1: jornada**, NOUN
- grammar: gender Fem; countability Count
- said: /xoɾ.ˈna.da/ (es-ES), /xoɾ.ˈna.da/ (es-MX)
  1. [B1] tiempo que una persona dedica a trabajar cada día o cada semana
     - example: Mi padre trabaja a media jornada desde que nació mi hermano.
     - collocations: jornada laboral; jornada completa; media jornada; jornada partida; jornada intensiva; reducción de jornada; jornada de trabajo
  2. [B1] día, considerado sobre todo por lo que se hace o sucede en él
     - example: Después de una larga jornada, solo quiero cenar y dormir.
     - collocations: jornada electoral; jornada de huelga; jornada de reflexión; jornada de puertas abiertas; una larga jornada
  3. [B2] conjunto de partidos de una competición que se juegan en las mismas fechas
     - example: El equipo se juega el título en la última jornada de liga.
     - collocations: primera jornada; última jornada; jornada de liga
  4. [B2] reunión de uno o varios días en que especialistas tratan un tema con charlas y debates
     - example: La universidad organiza unas jornadas sobre cambio climático en marzo.
     - frames: jornadas sobre algo; jornadas de algo
     - collocations: organizar unas jornadas; jornadas técnicas; celebrar unas jornadas
     - grammar: number Plur, mainly
  5. [C1] camino que se recorre en un día de viaje
     - example: El pueblo más cercano estaba a tres jornadas de camino.
     - collocations: jornada de camino; jornada de viaje

### 37. paciente

**Lexeme 1: paciente**, ADJ
- grammar: comparable True; note same form for masculine and feminine; plural pacientes
- said: /pa.ˈθjen.te/ (es-ES), /pa.ˈsjen.te/ (es-MX)
  1. [A2] que sabe esperar con calma y soporta las molestias sin enfadarse
     - example: Hay que ser muy paciente para enseñar a leer a un niño.
     - frames: paciente con alguien
     - collocations: espera paciente; trabajo paciente
     - opposites: impaciente
**Lexeme 2: paciente**, NOUN
- grammar: gender Com; countability Count
- said: /pa.ˈθjen.te/ (es-ES), /pa.ˈsjen.te/ (es-MX)
  1. [A2] persona enferma o que recibe la atención de un médico
     - example: La doctora atiende a más de veinte pacientes cada mañana.
     - collocations: atender a un paciente; paciente ingresado; paciente crónico; paciente terminal; dar de alta a un paciente; historial del paciente

### 38. parar

**Lexeme 1: parar**, VERB
- grammar: conjugation 1; irregular False; valency Both
- said: /pa.ˈɾaɾ/ (es-ES), /pa.ˈɾaɾ/ (es-MX)
  1. [A1] dejar de moverse, de funcionar o de hacer algo
     - example: Este autobús para delante del hospital cada diez minutos.
     - frames: parar de + inf; parar en + lugar
     - collocations: sin parar; parar en seco; no parar de llover; parar un momento
     - grammar: valency Intr; note also pararse with the same meaning
     - opposites: seguir
  2. [A2] hacer que algo o alguien deje de moverse o de funcionar
     - example: Para el coche en la próxima gasolinera, por favor.
     - collocations: parar un taxi; parar el motor; parar el coche; parar el golpe
     - grammar: valency Tr
  3. [B1] detener el balón e impedir que entre en la portería
     - example: Nuestro portero paró dos penaltis en la final.
     - collocations: parar un penalti; parar un tiro
     - grammar: valency Tr
  4. [B1] levantarse y quedarse de pie
     - example: Todos nos paramos cuando entró la directora.
     - regions: es-419 mainly
     - grammar: valency Refl; note used as pararse
     - opposites: sentarse
  5. [B2] estar o quedarse en un lugar, sobre todo para vivir o alojarse
     - example: Mi hijo mayor casi nunca para en casa los sábados.
     - frames: parar en + lugar
     - collocations: no parar en casa
     - labels: formality informal
     - grammar: valency Intr; note mostly in negative sentences
  6. [B2] llegar finalmente a un lugar o situación, a menudo sin esperarlo
     - example: No sé dónde habrán ido a parar mis llaves.
     - frames: ir a parar a + lugar
     - collocations: ir a parar; venir a parar
     - grammar: valency Intr; note usually after ir a or venir a
  7. [B2] dejar de trabajar como protesta para defender unos derechos
     - example: Los maestros van a parar mañana por los salarios.
     - regions: es-419 mainly
     - grammar: valency Intr

### 39. móvil

**Lexeme 1: móvil**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈmo.bil/ (es-ES), /ˈmo.bil/ (es-MX)
  1. [A1] teléfono pequeño y sin cables que se puede llevar a todas partes
     - example: Por favor, apagad el móvil antes de entrar en clase.
     - collocations: llamar al móvil; cargar el móvil; apagar el móvil; número de móvil; mirar el móvil
     - regions: es-ES mainly
  2. [C1] causa o razón que lleva a alguien a actuar de una manera
     - example: La policía todavía no conoce el móvil del asesinato.
     - frames: el móvil de algo
     - collocations: el móvil del crimen; móvil económico
  3. [C1] adorno de piezas colgadas de hilos que se mueven con el aire
     - example: Encima de la cuna del bebé hay un móvil de estrellas.
**Lexeme 2: móvil**, ADJ
- grammar: comparable False; note same form for masculine and feminine; plural móviles
- said: /ˈmo.bil/ (es-ES), /ˈmo.bil/ (es-MX)
  1. [B1] que puede moverse o ser llevado de un lugar a otro
     - example: La televisión envió una unidad móvil al lugar del accidente.
     - collocations: teléfono móvil; telefonía móvil; unidad móvil; dispositivo móvil
     - opposites: inmóvil

### 40. definir

**Lexeme 1: definir**, VERB
- grammar: conjugation 3; irregular False
- said: /de.fi.ˈniɾ/ (es-ES), /de.fi.ˈniɾ/ (es-MX)
  1. [B1] explicar con claridad y exactitud qué significa una palabra o una idea
     - example: Es difícil definir la felicidad con pocas palabras.
     - frames: definir algo; definir algo como algo
     - collocations: definir un término; definir un concepto; definir una palabra
     - grammar: valency Tr
  2. [B2] fijar con precisión los límites, las características o los detalles de algo
     - example: Primero tenemos que definir bien los objetivos del proyecto.
     - collocations: definir los objetivos; definir una estrategia; definir las reglas; definir claramente; definir las prioridades
     - grammar: valency Tr
  3. [B2] ser el rasgo propio que distingue a una persona o una cosa
     - example: La generosidad es lo que mejor define a mi abuela.
     - frames: definir a alguien
     - grammar: valency Tr
  4. [C1] tomar una postura clara y decir lo que uno piensa o es
     - example: El candidato todavía no se ha definido sobre ese tema.
     - frames: definirse sobre algo; definirse como algo
     - collocations: definirse políticamente
     - grammar: valency Refl; reflexive True
  5. [B2] decidir el resultado de un partido, una competición o un asunto pendiente
     - example: El campeonato se va a definir en la última jornada.
     - frames: definirse en algo
     - collocations: definir el partido; definir el campeonato
     - regions: es-419 mainly
     - grammar: valency Both

### 41. promoción

**Lexeme 1: promoción**, NOUN
- grammar: gender Fem; countability Both
- said: /pɾo.mo.ˈθjon/ (es-ES), /pɾo.mo.ˈsjon/ (es-MX)
- forms: promociones [Number=Plur]
  1. [B1] conjunto de acciones para dar a conocer un producto y aumentar sus ventas
     - example: La empresa gasta mucho dinero en la promoción de su nueva bebida.
     - frames: promoción de algo
     - collocations: campaña de promoción; promoción de ventas; promoción turística; hacer promoción
     - grammar: countability Mass
  2. [B1] oferta en que un producto se vende más barato o con algún regalo
     - example: Este mes hay una promoción de dos camisetas por el precio de una.
     - collocations: estar en promoción; promoción especial; aprovechar una promoción; precio de promoción
     - grammar: countability Count
  3. [B2] impulso que se da a algo para que se desarrolle o mejore
     - example: El ayuntamiento quiere invertir más en la promoción del deporte infantil.
     - frames: promoción de algo
     - collocations: promoción de la salud; promoción del empleo; promoción cultural
     - grammar: countability Mass
  4. [B2] paso de una persona o un equipo a un puesto o una categoría superior
     - example: Después de cinco años en la empresa, por fin le llegó la promoción.
     - collocations: promoción interna; promoción profesional; fase de promoción
     - grammar: countability Count
  5. [B2] conjunto de personas que terminan sus estudios u obtienen un título el mismo año
     - example: Mi padre y su mejor amigo son de la misma promoción de médicos.
     - collocations: compañero de promoción; cena de promoción
     - grammar: countability Count
- relations: verbal_noun_of promover (VERB)

### 42. concierto

**Lexeme 1: concierto**, NOUN
- grammar: gender Masc
- said: /kon.ˈθjeɾ.to/ (es-ES), /kon.ˈsjeɾ.to/ (es-MX)
  1. [A2] actuación en la que uno o varios músicos tocan ante un público
     - example: Esta noche vamos a un concierto de rock en el estadio.
     - collocations: dar un concierto; ir a un concierto; concierto de rock; sala de conciertos; concierto en directo; concierto benéfico; concierto al aire libre
     - grammar: countability Count
  2. [B2] obra musical en que uno o más instrumentos solistas tocan acompañados por una orquesta
     - example: La orquesta interpretará un concierto para piano de un compositor ruso.
     - collocations: concierto para piano; concierto para violín y orquesta
     - labels: domain music
     - grammar: countability Count
  3. [C1] acuerdo, orden o armonía entre varias personas, grupos o países
     - example: Ningún país puede resolver este problema sin el concierto internacional.
     - frames: en concierto con alguien
     - collocations: de concierto; el concierto internacional; el concierto de las naciones
     - labels: formality formal
     - grammar: countability Mass

### 43. chino

**Lexeme 1: chino**, ADJ
- grammar: note feminine china; plural chinos, chinas
- said: /ˈtʃi.no/ (es-ES), /ˈtʃi.no/ (es-MX)
  1. [A1] de China, de su gente, de su lengua o de su cultura
     - example: Esta noche vamos a cenar a un restaurante chino del centro.
     - collocations: comida china; restaurante chino; año nuevo chino; barrio chino; la muralla china
     - grammar: comparable False
  2. [B2] dicho del pelo, que forma rizos muy pequeños y apretados
     - example: Mi hija tiene el pelo chino igual que su abuela.
     - collocations: pelo chino
     - labels: formality informal
     - regions: es-MX mainly
     - opposites: lacio
- relations: adjective_of China (PROPN)
**Lexeme 2: chino**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈtʃi.no/ (es-ES), /ˈtʃi.no/ (es-MX)
- forms: china [Gender=Fem, Number=Sing]; chinas [Gender=Fem, Number=Plur]
  1. [A1] persona nacida en China o que tiene su nacionalidad
     - example: En mi clase de idiomas hay dos chinas y un japonés.
  2. [B2] niño o persona joven
     - example: Los chinos del barrio juegan fútbol en la calle todas las tardes.
     - labels: formality informal
     - regions: es-CO mainly
**Lexeme 3: chino**, NOUN
- grammar: gender Masc
- said: /ˈtʃi.no/ (es-ES), /ˈtʃi.no/ (es-MX)
  1. [A1] idioma que se habla en China y en muchas comunidades de su gente
     - example: Mi hermano lleva tres años estudiando chino en la universidad.
     - collocations: hablar chino; aprender chino; chino mandarín
     - grammar: countability Mass
  2. [B2] lenguaje o asunto muy difícil o imposible de entender
     - example: Para mí, estas instrucciones del ordenador son puro chino.
     - collocations: sonar a chino; hablar en chino
     - labels: formality informal, figurative True
     - grammar: countability Mass
  3. [B1] tienda o restaurante cuyo dueño procede de China
     - example: Compré las pilas en el chino que hay al lado de casa.
     - collocations: ir al chino
     - labels: formality informal
     - grammar: countability Count
  4. [C1] juego en que se adivina cuántas monedas esconden los jugadores en la mano
     - example: Jugamos a los chinos para decidir quién pagaba los cafés.
     - collocations: jugar a los chinos
     - regions: es-ES mainly
     - grammar: number Plur; countability Count

### 44. considerado

**Lexeme 1: considerado**, ADJ
- grammar: comparable True
- said: /kon.si.de.ˈɾa.do/ (es-ES), /kon.si.de.ˈɾa.do/ (es-MX)
  1. [B2] que piensa en los demás y los trata con respeto y atención
     - example: Mi vecino siempre ha sido muy considerado con las personas mayores.
     - frames: considerado con alguien
     - collocations: muy considerado; considerado con los demás
     - opposites: desconsiderado
  2. [C1] que goza del respeto y la buena opinión de los demás
     - example: Es una profesora muy bien considerada en toda la universidad.
     - collocations: bien considerado; mal considerado
- relations: derived_from considerar (VERB)

### 45. alternativa

**Lexeme 1: alternativa**, NOUN
- grammar: gender Fem; countability Count
- said: /al.teɾ.na.ˈti.ba/ (es-ES), /al.teɾ.na.ˈti.ba/ (es-MX)
  1. [B1] posibilidad de elegir entre dos o más caminos o soluciones
     - example: No me dejaron alternativa y tuve que aceptar sus condiciones.
     - collocations: no tener alternativa; no quedar otra alternativa; única alternativa; dejar sin alternativa
  2. [B1] opción que puede sustituir a otra o usarse en su lugar
     - example: La bicicleta es una buena alternativa al coche en la ciudad.
     - frames: alternativa a algo
     - collocations: buscar una alternativa; ofrecer una alternativa; proponer una alternativa; alternativa viable; alternativa real
- relations: derived_from alternativo (ADJ)
