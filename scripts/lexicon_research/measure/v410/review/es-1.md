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
- grammar: comparable True; note same form for masculine and feminine; plural recientes; superlative recentísimo
- said: /re.ˈθjen.te/ (es-ES), /re.ˈsjen.te/ (es-MX)
- forms: recentísimo [Degree=Abs, Gender=Masc, Number=Sing]
  1. [B1] que ha ocurrido, aparecido o se ha hecho hace poco tiempo
     - example: Según un estudio reciente, los jóvenes leen cada vez menos.
     - collocations: más reciente; estudio reciente; historia reciente; pasado reciente; de reciente creación; en los últimos tiempos recientes; fecha reciente
     - opposites: antiguo

### 3. denuncia

**Lexeme 1: denuncia**, NOUN
- grammar: gender Masc; countability Count
- said: /de.ˈnun.θja/ (es-ES), /de.ˈnun.sja/ (es-MX)
  1. [B1] aviso a la policía o a un juez sobre un delito o una falta
     - example: Mis vecinos fueron a comisaría a poner una denuncia por el robo.
     - frames: denuncia contra alguien; denuncia por algo
     - collocations: poner una denuncia; presentar una denuncia; interponer una denuncia; retirar una denuncia; denuncia falsa; recibir una denuncia; tramitar una denuncia
  2. [B2] crítica pública de una situación injusta, ilegal o dañina
     - example: Su última película es una dura denuncia de la pobreza infantil.
     - frames: denuncia de algo
     - collocations: denuncia social; denuncia pública; hacer una denuncia
- relations: verbal_noun_of denunciar (VERB)

### 4. sorpresa

**Lexeme 1: sorpresa**, NOUN
- grammar: gender Fem; countability Both
- said: /soɾ.ˈpɾe.sa/ (es-ES), /soɾ.ˈpɾe.sa/ (es-MX)
  1. [A2] impresión que produce en alguien algo que no esperaba
     - example: Me miró con sorpresa cuando le conté la noticia.
     - frames: para sorpresa de alguien
     - collocations: llevarse una sorpresa; con sorpresa; por sorpresa; pillar por sorpresa; coger por sorpresa; causar sorpresa; cara de sorpresa; gran sorpresa
  2. [A2] cosa o hecho inesperado, como un regalo preparado en secreto
     - example: Te hemos preparado una sorpresa para tu cumpleaños.
     - collocations: dar una sorpresa; preparar una sorpresa; fiesta sorpresa; sorpresa agradable; sorpresa desagradable
     - grammar: countability Count

### 5. escala

**Lexeme 1: escala**, NOUN
- grammar: gender Fem; countability Count
- said: /es.ˈka.la/ (es-ES), /es.ˈka.la/ (es-MX)
  1. [B1] serie ordenada de grados o niveles para medir o comparar una cualidad
     - example: Puntúa la calidad del servicio en una escala del uno al diez.
     - collocations: escala de valores; escala salarial; escala de grises; en una escala de
  2. [B1] relación entre las medidas de un mapa o modelo y las medidas reales
     - example: Mi abuelo construyó una maqueta del barco a escala.
     - collocations: a escala; modelo a escala; dibujar a escala; mapa a escala
  3. [B1] parada que hace un avión o un barco antes de llegar a su destino
     - example: Nuestro vuelo hace escala en una isla para cargar combustible.
     - frames: hacer escala en + lugar
     - collocations: hacer escala; vuelo sin escalas; escala técnica
  4. [B2] tamaño, extensión o importancia con que se hace o sucede algo
     - example: La empresa empezó a fabricar coches eléctricos a gran escala.
     - collocations: a gran escala; a pequeña escala; a escala mundial; a escala nacional
  5. [B2] serie ordenada de notas musicales, de más grave a más aguda o al revés
     - example: Antes de cada concierto, la pianista practica las escalas durante una hora.
     - collocations: escala mayor; escala menor; tocar escalas
     - labels: domain music
  6. [C1] utensilio de cuerda o madera con peldaños, que sirve para subir o bajar
     - example: Los marineros subieron a bordo por una escala de cuerda.
     - collocations: escala de cuerda

### 6. seguramente

**Lexeme 1: seguramente**, ADV
- grammar: invariable True
- said: /se.ɡu.ˌɾa.ˈmen.te/ (es-ES), /se.ɡu.ˌɾa.ˈmen.te/ (es-MX)
  1. [A2] se usa para decir que algo es muy posible, aunque no del todo cierto
     - example: Seguramente mañana lloverá, así que lleva un paraguas.
     - grammar: note usually followed by the indicative
  2. [B2] de modo firme y fiable, sin peligro ni riesgo
     - example: El dinero estaba guardado seguramente en la caja fuerte del banco.
- relations: adverb_of seguro (ADJ)

### 7. beneficio

**Lexeme 1: beneficio**, NOUN
- grammar: gender Masc; countability Count
- said: /be.ne.ˈfi.θjo/ (es-ES), /be.ne.ˈfi.sjo/ (es-MX)
  1. [B1] bien, provecho o ventaja que alguien o algo recibe
     - example: El ejercicio diario tiene muchos beneficios para la salud.
     - frames: en beneficio de algo/alguien; a beneficio de algo/alguien; beneficio para algo/alguien
     - collocations: en beneficio propio; sacar beneficio; el beneficio de la duda; beneficio mutuo; beneficios sociales; beneficio fiscal
     - opposites: perjuicio
  2. [B2] dinero que se gana en un negocio después de restar los gastos
     - example: La empresa ha duplicado sus beneficios en los últimos dos años.
     - collocations: obtener beneficios; beneficio neto; beneficio bruto; margen de beneficio; generar beneficios; reportar beneficios; sin ánimo de beneficio
     - labels: domain economics
     - grammar: note often in the plural
     - opposites: pérdida

### 8. estudiar

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

### 9. discusión

**Lexeme 1: discusión**, NOUN
- grammar: gender Fem; countability Count
- said: /dis.ku.ˈsjon/ (es-ES), /dis.ku.ˈsjon/ (es-MX)
- forms: discusiones [Number=Plur]
  1. [B1] conversación en la que varias personas examinan un asunto y dan opiniones distintas
     - example: El proyecto de ley pasará a discusión en el Congreso la próxima semana.
     - frames: discusión sobre algo; discusión de algo
     - collocations: someter a discusión; abrir la discusión; en discusión; fuera de discusión; sin discusión; discusión pública
  2. [A2] enfrentamiento de palabras entre personas que no están de acuerdo, a menudo con enfado
     - example: Anoche tuve una discusión muy fuerte con mi hermano por la herencia.
     - frames: discusión con alguien; discusión por algo
     - collocations: tener una discusión; discusión acalorada; entrar en discusiones; una fuerte discusión; evitar discusiones
- relations: verbal_noun_of discutir (VERB)

### 10. intervención

**Lexeme 1: intervención**, NOUN
- grammar: gender Fem
- said: /in.teɾ.ben.ˈθjon/ (es-ES), /in.teɾ.ben.ˈsjon/ (es-MX)
  1. [B2] participación en un asunto o conflicto para influir en él o resolverlo
     - example: La rápida intervención de los bomberos evitó una tragedia mayor.
     - frames: intervención en algo; intervención de alguien
     - collocations: intervención militar; intervención humanitaria; intervención del Estado; pedir la intervención; solicitar la intervención; gracias a la intervención
     - grammar: countability Both
  2. [B2] lo que dice una persona cuando le toca hablar en un debate o reunión
     - example: Durante su intervención, la ministra defendió la nueva ley de vivienda.
     - frames: intervención en algo
     - collocations: hacer una intervención; breve intervención; turno de intervenciones
     - grammar: countability Count
  3. [B2] operación que hace un médico en el cuerpo de un paciente para curarlo
     - example: Mi abuelo se recupera bien después de la intervención de rodilla.
     - collocations: intervención quirúrgica; someterse a una intervención; realizar una intervención
     - grammar: countability Count
- relations: verbal_noun_of intervenir (VERB)

### 11. sentencia

**Lexeme 1: sentencia**, NOUN
- grammar: gender Fem; countability Count
- said: /sen.ˈten.θja/ (es-ES), /sen.ˈten.sja/ (es-MX)
  1. [B2] decisión con la que un juez o tribunal resuelve un juicio
     - example: El tribunal hará pública la sentencia la próxima semana.
     - frames: sentencia contra alguien; sentencia sobre algo
     - collocations: dictar sentencia; sentencia firme; recurrir la sentencia; apelar la sentencia; sentencia condenatoria; sentencia absolutoria; sentencia de muerte; ejecutar la sentencia
  2. [C1] frase breve que expresa una enseñanza o un pensamiento sabio
     - example: Mi abuelo siempre repetía viejas sentencias sobre el trabajo y la paciencia.
     - labels: style literary

### 12. imaginar

**Lexeme 1: imaginar**, VERB
- grammar: conjugation 1; irregular False; valency Tr; note often used as imaginarse with the same meaning: me imagino
- said: /i.ma.xi.ˈnaɾ/ (es-ES), /i.ma.xi.ˈnaɾ/ (es-MX)
  1. [A2] formar en la mente la figura de algo que no se ve o no existe
     - example: Cierra los ojos e imagina que estás en una playa tranquila.
     - frames: imaginar algo; imaginar a alguien; imaginarse algo; imaginar que + ind
     - collocations: difícil de imaginar; imaginar el futuro; ni te imaginas
     - grammar: valency Tr; note imaginarse is common; the pronoun adds emphasis, not a reflexive meaning
  2. [B1] creer o pensar algo como probable sin tener pruebas
     - example: Me imagino que estarás cansado después de un viaje tan largo.
     - frames: imaginar que + ind; imaginarse que + ind; no imaginar que + subj; imaginarse algo
     - collocations: ya me imagino; como te puedes imaginar
     - grammar: valency Tr; note mostly as imaginarse in speech: me imagino que...
  3. [B2] crear con la mente algo nuevo, como un plan o un proyecto
     - example: Los ingenieros imaginaron una solución sencilla para ahorrar agua.
     - frames: imaginar algo
     - collocations: imaginar una solución; imaginar un plan
     - grammar: valency Tr

### 13. usado

**Lexeme 1: usado**, ADJ
- said: /u.sa.ˈdo/ (es-ES), /u.sa.ˈdo/ (es-MX)
  1. [A2] que ya ha tenido otro dueño antes y se vende o se compra de nuevo
     - example: Mis padres compraron un coche usado porque era más barato.
     - collocations: coche usado; ropa usada; libros usados; de segunda mano
     - grammar: comparable False
     - opposites: nuevo
  2. [B1] gastado o estropeado por haber servido mucho tiempo
     - example: Estos zapatos ya están muy usados, necesito unos nuevos.
     - grammar: comparable True; position Pred, mainly
- relations: derived_from usar (VERB)

### 14. significado

**Lexeme 1: significado**, NOUN
- grammar: gender Masc; countability Count
- said: /siɡ.ni.fi.ˈka.do/ (es-ES), /siɡ.ni.fi.ˈka.do/ (es-MX)
  1. [A2] idea o concepto que expresa una palabra, una frase o un signo
     - example: Busca en el diccionario el significado de las palabras nuevas.
     - frames: el significado de algo
     - collocations: buscar el significado; significado literal; significado figurado; doble significado; tener significado
  2. [B1] importancia o valor que algo tiene para alguien o en una situación
     - example: Este viejo reloj tiene un significado especial para mi familia.
     - frames: el significado de algo; significado para alguien
     - collocations: el significado de la vida; significado especial; significado histórico; significado simbólico
     - grammar: countability Count; note usually singular
- relations: derived_from significar (VERB)
**Lexeme 2: significado**, ADJ
- grammar: position Attr, mainly
- said: /siɡ.ni.fi.ˈka.do/ (es-ES), /siɡ.ni.fi.ˈka.do/ (es-MX)
  1. [C2] conocido e importante dentro de un grupo o una actividad
     - example: Varios significados miembros del partido asistieron ayer a la reunión.
     - labels: formality formal
- relations: derived_from significar (VERB)

### 15. mezcla

**Lexeme 1: mezcla**, NOUN
- grammar: gender Fem
- said: /ˈmeθ.kla/ (es-ES), /ˈmes.kla/ (es-MX)
  1. [B1] conjunto que resulta de juntar cosas, sustancias o personas distintas
     - example: Echa la mezcla en el molde y hornéala media hora.
     - frames: mezcla de algo
     - collocations: batir la mezcla; una mezcla explosiva; mezcla homogénea; mezcla de culturas; una extraña mezcla
     - grammar: countability Count
  2. [B2] acción de juntar o unir cosas distintas para que formen un todo
     - example: La mezcla de los colores debe hacerse con mucho cuidado.
     - frames: mezcla de algo con algo
     - grammar: countability Mass, mainly
  3. [C1] masa de cal o cemento, arena y agua que se usa para unir ladrillos
     - example: El albañil preparó la mezcla antes de levantar la pared.
     - grammar: countability Mass
  4. [C1] combinación de varias pistas de sonido para obtener la grabación final de una pieza
     - example: Pasaron varias semanas en el estudio haciendo la mezcla del disco.
     - collocations: mesa de mezclas; mezcla de sonido
     - labels: domain music
     - grammar: countability Count
- relations: verbal_noun_of mezclar (VERB)

### 16. elegido

**Lexeme 1: elegido**, ADJ
- grammar: comparable False
- said: /e.le.ˈxi.do/ (es-ES), /e.le.ˈxi.do/ (es-MX)
  1. [B1] que ha sido escogido entre varios para algo
     - example: El restaurante elegido para la cena estaba cerca del hotel.
     - frames: elegido para algo; elegido por alguien
     - collocations: pueblo elegido; candidato elegido; cuidadosamente elegido; bien elegido
- relations: derived_from elegir (VERB)
**Lexeme 2: elegido**, NOUN
- grammar: gender Masc; countability Count
- said: /e.le.ˈxi.do/ (es-ES), /e.le.ˈxi.do/ (es-MX)
- forms: elegida [Gender=Fem, Number=Sing]; elegidas [Gender=Fem, Number=Plur]
  1. [B2] persona escogida entre otras, a menudo por Dios o el destino, para una misión especial
     - example: Según la leyenda, solo el elegido podrá abrir esa puerta.
     - collocations: el elegido; los elegidos
- relations: derived_from elegir (VERB)

### 17. reír

**Lexeme 1: reír**, VERB
- grammar: conjugation 3; irregular True; note very often used as reírse
- said: /re.ˈiɾ/ (es-ES), /re.ˈiɾ/ (es-MX)
- forms: río [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; ríes [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin]; ríe [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin]; reímos [Mood=Ind, Tense=Pres,Past, Person=1, Number=Plur, VerbForm=Fin]; ríen [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin]; reí [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin]; rio [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin] (written rió before the 2010 orthography); rieron [Mood=Ind, Tense=Past, Person=3, Number=Plur, VerbForm=Fin]; reiré [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin]; ría [Mood=Sub, Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin]; riamos [Mood=Sub, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin]; reído [VerbForm=Part, Tense=Past]; riendo [VerbForm=Ger]
  1. [A2] mostrar alegría o diversión con la cara y con sonidos de la voz
     - example: Los niños se ríen mucho con los payasos del circo.
     - frames: reírse con algo/alguien; hacer reír a alguien
     - collocations: reír a carcajadas; reírse a carcajadas; echarse a reír; hacer reír; reír por lo bajo; reírse mucho; reír las gracias
     - grammar: valency Intr; note reír and reírse are both used, reírse more often in speech
     - opposites: llorar
  2. [B1] burlarse de una persona o cosa o no tomarla en serio
     - example: No te rías de tu hermano, que lo está pasando mal.
     - frames: reírse de algo/alguien
     - collocations: reírse en la cara; reírse de uno mismo
     - grammar: valency Refl; reflexive True

### 18. impuesto

**Lexeme 1: impuesto**, NOUN
- grammar: gender Masc; countability Count
- said: /im.ˈpwes.to/ (es-ES), /im.ˈpwes.to/ (es-MX)
  1. [B1] dinero que hay que pagar obligatoriamente al Estado para los gastos públicos
     - example: Este año vamos a pagar más impuestos por la casa nueva.
     - frames: impuesto sobre algo; impuesto a algo
     - collocations: pagar impuestos; subir los impuestos; bajar los impuestos; cobrar impuestos; recaudar impuestos; evadir impuestos; libre de impuestos; impuesto sobre la renta; impuesto sobre el valor añadido; declaración de impuestos
     - grammar: note impuesto a algo is usual in Latin America, impuesto sobre algo in Spain
- relations: derived_from imponer (VERB)

### 19. clima

**Lexeme 1: clima**, NOUN
- grammar: gender Masc; note masculine although it ends in -a: el clima, los climas
- said: /ˈkli.ma/ (es-ES), /ˈkli.ma/ (es-MX)
  1. [A2] condiciones del tiempo, como temperatura y lluvias, propias de una región
     - example: En esta zona de la costa el clima es suave todo el año.
     - collocations: clima templado; clima tropical; clima seco; clima húmedo; clima cálido; clima frío; clima mediterráneo
     - grammar: countability Count
  2. [B2] ambiente o circunstancias que rodean una situación o a un grupo de personas
     - example: Las negociaciones empezaron en un clima de desconfianza mutua.
     - frames: clima de algo
     - collocations: clima de tensión; clima de confianza; clima laboral; clima político; clima de violencia; crear un clima
     - labels: figurative True
     - grammar: countability Count; note mostly singular
  3. [A2] estado de la atmósfera en un lugar y un momento concretos
     - example: Mañana vamos a la playa si el clima lo permite.
     - collocations: buen clima; mal clima; pronóstico del clima
     - regions: es-419 mainly
     - grammar: countability Mass
  4. [B1] aparato que enfría el aire de un lugar cerrado
     - example: Hace mucho calor en la oficina, prende el clima, por favor.
     - collocations: prender el clima; apagar el clima
     - labels: formality informal
     - regions: es-MX mainly
     - grammar: countability Count

### 20. extremo

**Lexeme 1: extremo**, ADJ
- grammar: comparable True
- said: /eks.tɾe.ˈmo/ (es-ES), /eks.tɾe.ˈmo/ (es-MX)
  1. [B1] que tiene el grado más alto o más intenso posible
     - example: Esta semana se esperan temperaturas extremas en todo el sur.
     - collocations: pobreza extrema; calor extremo; frío extremo; caso extremo; extrema gravedad; deportes extremos; con extremo cuidado
  2. [B2] muy alejado de lo moderado o de lo que piensa la mayoría
     - example: No estoy de acuerdo con sus ideas tan extremas sobre la inmigración.
     - collocations: extrema derecha; extrema izquierda; medidas extremas; posturas extremas
     - opposites: moderado
  3. [B2] que está en la parte más lejana o al final de algo
     - example: Viven en una aldea situada en el extremo norte del país.
     - collocations: Extremo Oriente; extremo norte; extremo sur
     - grammar: position Attr, mainly
**Lexeme 2: extremo**, NOUN
- grammar: gender Masc; countability Count
- said: /eks.ˈtɾe.mo/ (es-ES), /eks.ˈtɾe.mo/ (es-MX)
  1. [B1] parte final o punta de una cosa larga o de un lugar
     - example: Ata bien el otro extremo de la cuerda al árbol.
     - frames: el extremo de algo
     - collocations: al otro extremo; de extremo a extremo; en un extremo; el extremo opuesto
  2. [B2] punto o grado máximo al que puede llegar algo o alguien
     - example: La situación llegó a tal extremo que tuvieron que llamar a la policía.
     - frames: llegar al extremo de + inf; hasta el extremo de + inf
     - collocations: llegar a tal extremo; en último extremo; pasar de un extremo a otro; los extremos se tocan; en extremo
  3. [C1] jugador que ocupa una de las bandas en el ataque de un equipo
     - example: El nuevo extremo izquierdo es muy rápido con el balón.
     - collocations: extremo derecho; extremo izquierdo
     - labels: domain sport

### 21. permitir

**Lexeme 1: permitir**, VERB
- grammar: conjugation 3; irregular False
- said: /peɾ.mi.ˈtiɾ/ (es-ES), /peɾ.mi.ˈtiɾ/ (es-MX)
  1. [A2] dar permiso a alguien para hacer algo o no impedirlo
     - example: Mis padres no me permiten salir solo por la noche.
     - frames: permitir algo a alguien; permitir a alguien + inf; permitir que + subj
     - collocations: permitir la entrada; no se permite fumar; permitir el paso
     - grammar: valency Tr
     - opposites: prohibir
  2. [B1] hacer posible que algo ocurra o que alguien pueda hacerlo
     - example: Esta nueva máquina permite ahorrar mucha energía en casa.
     - frames: permitir + inf; permitir algo a alguien; permitir que + subj
     - collocations: si el tiempo lo permite
     - grammar: valency Tr
     - opposites: impedir
  3. [B1] tener el dinero, el tiempo o la libertad para hacer o tener algo
     - example: Este año no podemos permitirnos unas vacaciones tan caras.
     - frames: permitirse algo; permitirse + inf
     - collocations: permitirse el lujo; poder permitirse
     - grammar: valency Refl; note the pronoun means that the subject grants it to themselves

### 22. víctima

**Lexeme 1: víctima**, NOUN
- grammar: gender Fem; countability Count; note always feminine, also when it refers to a man: la víctima
- said: /ˈbik.ti.ma/ (es-ES), /ˈbik.ti.ma/ (es-MX)
  1. [B1] persona o animal que sufre un daño o muere por culpa de otro o por un accidente
     - example: Las víctimas del accidente fueron trasladadas al hospital más cercano.
     - frames: víctima de algo/alguien; ser víctima de algo
     - collocations: víctima mortal; hacerse la víctima; víctimas civiles; víctima inocente; caer víctima de; cobrarse víctimas; víctima de su propio éxito
  2. [C1] persona o animal que se mata como ofrenda a un dios
     - example: En aquel templo antiguo se ofrecían víctimas a los dioses.
     - labels: domain religion

### 23. vender

**Lexeme 1: vender**, VERB
- grammar: conjugation 2; irregular False
- said: /ben.ˈdeɾ/ (es-ES), /ben.ˈdeɾ/ (es-MX)
  1. [A1] dar algo a otra persona a cambio de dinero
     - example: Mis padres quieren vender la casa del pueblo este año.
     - frames: vender algo a alguien; vender algo por + cantidad; vender algo a + precio
     - collocations: vender barato; vender caro; vender al por mayor; vender al por menor; se vende; poner a la venta; vender a plazos; vender bien
     - grammar: valency Tr, mainly; note also used without an object: esta marca vende mucho
     - opposites: comprar
  2. [B2] traicionar a alguien, sobre todo a cambio de un beneficio
     - example: Nunca pensé que mi mejor amigo me vendería a la policía.
     - frames: vender a alguien
     - labels: figurative True
     - grammar: valency Tr
  3. [C1] dejarse comprar o sobornar, actuar contra sus principios a cambio de algo
     - example: Aquel periodista se vendió al partido que estaba en el gobierno.
     - frames: venderse a alguien; venderse por algo
     - labels: figurative True, attitude disapproving
     - grammar: valency Refl

### 24. definitivamente

**Lexeme 1: definitivamente**, ADV
- grammar: invariable True
- said: /de.fi.ni.ˌti.ba.ˈmen.te/ (es-ES), /de.fi.ni.ˌti.ba.ˈmen.te/ (es-MX)
  1. [B1] de forma final y para siempre, sin posibilidad de cambio
     - example: La fábrica del pueblo cerrará definitivamente a finales de año.
     - collocations: cerrar definitivamente; abandonar definitivamente; instalarse definitivamente; quedar definitivamente resuelto
  2. [B1] sin ninguna duda, con total seguridad
     - example: Definitivamente, este es el mejor restaurante de la ciudad.
     - collocations: definitivamente sí; definitivamente no
- relations: adverb_of definitivo (ADJ)

### 25. tranquilo

**Lexeme 1: tranquilo**, ADJ
- grammar: comparable True
- said: /tɾan.ˈki.lo/ (es-ES), /tɾan.ˈki.lo/ (es-MX)
  1. [A2] que está en calma, sin ruido, movimiento ni agitación
     - example: Buscamos un barrio tranquilo para criar a nuestros hijos.
     - collocations: barrio tranquilo; vida tranquila; mar tranquilo; noche tranquila; zona tranquila; ambiente tranquilo
     - opposites: ruidoso
  2. [A2] dicho de una persona, que no está nerviosa ni preocupada o no se altera con facilidad
     - example: Tranquilo, el médico dice que no es nada grave.
     - collocations: estar tranquilo; quedarse tranquilo; dejar tranquilo; conciencia tranquila; persona tranquila
     - opposites: nervioso
  3. [B2] que no se preocupa por algo que debería importarle
     - example: Rompió el jarrón y se quedó tan tranquilo, como si nada.
     - collocations: tan tranquilo; quedarse tan tranquilo

### 26. descubrir

**Lexeme 1: descubrir**, VERB
- grammar: conjugation 3; irregular True; note only the past participle is irregular: descubierto
- said: /des.ku.ˈbɾiɾ/ (es-ES), /des.ku.ˈbɾiɾ/ (es-MX)
- forms: descubro [Mood=Ind, Tense=Pres, Person=1, Number=Sing]; descubres [Mood=Ind, Tense=Pres, Person=2, Number=Sing]; descubre [Mood=Ind, Tense=Pres, Person=3, Number=Sing]; descubrimos [Mood=Ind, Tense=Pres, Person=1, Number=Plur]; descubren [Mood=Ind, Tense=Pres, Person=3, Number=Plur]; descubrí [Mood=Ind, Tense=Past, Person=1, Number=Sing]; descubrió [Mood=Ind, Tense=Past, Person=3, Number=Sing]; descubriré [Mood=Ind, Tense=Fut, Person=1, Number=Sing]; descubra [Mood=Sub, Tense=Pres, Person=1, Number=Sing]; descubierto [VerbForm=Part, Tense=Past]; descubriendo [VerbForm=Ger]
  1. [B1] encontrar algo que no se conocía o que estaba escondido
     - example: Unos científicos han descubierto una nueva especie de rana en la selva.
     - frames: descubrir algo
     - collocations: descubrir un tesoro; descubrir una vacuna; descubrir un lugar; descubrir el mundo
     - grammar: valency Tr
  2. [B1] llegar a saber algo que se ignoraba o que alguien ocultaba
     - example: Al final descubrí que mi amigo me había mentido.
     - frames: descubrir algo; descubrir que + ind; descubrir a alguien
     - collocations: descubrir la verdad; descubrir un secreto; descubrir el engaño; descubrir al culpable
     - grammar: valency Tr
  3. [B2] quitar lo que tapa una cosa para que se vea
     - example: El alcalde descubrirá mañana la placa en honor del poeta.
     - frames: descubrir algo
     - collocations: descubrir una placa; descubrir una estatua
     - grammar: valency Tr
     - opposites: tapar
  4. [B2] dejar ver o dar a conocer algo que se mantenía oculto
     - example: Con aquella pregunta descubrió sus verdaderas intenciones.
     - frames: descubrir algo
     - collocations: descubrir sus intenciones; descubrir sus cartas
     - grammar: valency Tr
     - opposites: ocultar
  5. [C1] quitarse el sombrero, o mostrar gran admiración por alguien
     - example: Me descubro ante tu valor y tu paciencia.
     - frames: descubrirse ante alguien/algo
     - grammar: valency Refl; reflexive True
- relations: derived_from cubrir (VERB)

### 27. miércoles

**Lexeme 1: miércoles**, NOUN
- grammar: gender Masc; countability Count; note same form in the plural: el miércoles, los miércoles
- said: /ˈmjeɾ.ko.les/ (es-ES), /ˈmjeɾ.ko.les/ (es-MX)
- forms: miércoles [Number=Plur]
  1. [A1] día de la semana que va después del martes y antes del jueves
     - example: Tenemos clase de natación todos los miércoles por la tarde.
     - collocations: el miércoles; los miércoles; miércoles de ceniza
**Lexeme 2: miércoles**, INTJ
- grammar: invariable True
- said: /ˈmjeɾ.ko.les/ (es-ES), /ˈmjeɾ.ko.les/ (es-MX)
  1. [B2] se usa para expresar enfado, disgusto o sorpresa sin decir una palabrota
     - example: ¡Miércoles, se me olvidaron las llaves en la oficina!
     - labels: formality informal, attitude euphemistic
     - regions: es-419 mainly

### 28. regresar

**Lexeme 1: regresar**, VERB
- grammar: conjugation 1; irregular False
- said: /re.ɡɾe.ˈsaɾ/ (es-ES), /re.ɡɾe.ˈsaɾ/ (es-MX)
  1. [A2] ir o venir otra vez al lugar de donde se había salido
     - example: Mañana regresamos de las vacaciones muy temprano por la mañana.
     - frames: regresar a + lugar; regresar de + lugar
     - collocations: regresar a casa; regresar del trabajo; regresar a tiempo; regresar sano y salvo
     - grammar: valency Intr; note in Latin America also used as regresarse with the same meaning
  2. [B1] dar algo de nuevo a la persona que lo tenía antes
     - example: Tengo que regresar estos libros a la biblioteca antes del viernes.
     - frames: regresar algo a alguien
     - regions: es-419 mainly
     - grammar: valency Tr

### 29. acceder

**Lexeme 1: acceder**, VERB
- grammar: conjugation 2; irregular False; valency Intr
- said: /ak.θe.ˈdeɾ/ (es-ES), /ak.se.ˈdeɾ/ (es-MX)
  1. [B1] llegar a un lugar o entrar en él, también en un sistema informático
     - example: Se puede acceder al museo por la puerta lateral del jardín.
     - frames: acceder a algo
     - collocations: acceder a internet; acceder a la cuenta; acceder a la información; acceder a los datos; acceder al edificio
  2. [B2] aceptar o permitir lo que otra persona pide o propone
     - example: Después de mucho insistir, mi padre accedió a prestarme el coche.
     - frames: acceder a algo; acceder a + inf
     - collocations: acceder a una petición; acceder a una solicitud; acceder a las demandas; finalmente acceder
  3. [B2] llegar a obtener un puesto, una condición o un grado superior
     - example: Con este título podrás acceder a la universidad sin hacer examen.
     - frames: acceder a algo
     - collocations: acceder al poder; acceder al trono; acceder a la universidad; acceder al cargo; acceder a un puesto

### 30. pasión

**Lexeme 1: pasión**, NOUN
- grammar: gender Fem; countability Both
- said: /pa.ˈsjon/ (es-ES), /pa.ˈsjon/ (es-MX)
  1. [B1] emoción muy fuerte e intensa que domina la voluntad y el juicio
     - example: Defendió sus ideas con una fuerza y una pasión que sorprendieron a todos.
     - collocations: con pasión; despertar pasiones; dejarse llevar por la pasión; pasión desbordante
     - grammar: countability Mass, mainly
  2. [B1] amor muy intenso y deseo físico hacia otra persona
     - example: Entre los dos nació una pasión que duró muchos años.
     - frames: pasión por alguien
     - collocations: pasión amorosa; sentir pasión; crimen pasional
  3. [B1] gusto o interés muy grande por una actividad o una cosa
     - example: Mi gran pasión siempre ha sido la música clásica.
     - frames: pasión por algo
     - collocations: gran pasión; sentir pasión; compartir una pasión
     - grammar: countability Count
  4. [C1] sufrimiento y muerte de Jesucristo, según los Evangelios
     - example: En Semana Santa muchos pueblos representan la Pasión en sus calles.
     - collocations: la Pasión de Cristo
     - labels: domain religion
     - grammar: countability Mass; number Sing; note usually written with a capital: la Pasión

### 31. lunes

**Lexeme 1: lunes**, NOUN
- grammar: gender Masc; countability Count; note same form in the plural: el lunes, los lunes
- said: /ˈlu.nes/ (es-ES), /ˈlu.nes/ (es-MX)
- forms: lunes [Number=Plur]
  1. [A1] día de la semana que va después del domingo y antes del martes
     - example: Los lunes siempre me cuesta mucho levantarme temprano.
     - collocations: el lunes; los lunes

### 32. genial

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

### 33. musical

**Lexeme 1: musical**, ADJ
- grammar: note same form for masculine and feminine; plural musicales
- said: /mu.si.ˈkal/ (es-ES), /mu.si.ˈkal/ (es-MX)
  1. [A2] que pertenece al arte de combinar sonidos o se usa en él
     - example: Mi hijo aprende a tocar un instrumento musical en la escuela.
     - collocations: instrumento musical; género musical; oído musical; grupo musical; nota musical; educación musical; comedia musical; gusto musical
     - grammar: comparable False; position Attr, mainly
  2. [B2] que suena de forma agradable y armoniosa, como una canción
     - example: Tiene una voz suave y muy musical cuando lee en voz alta.
     - grammar: comparable True
- relations: adjective_of música (NOUN)
**Lexeme 2: musical**, NOUN
- grammar: gender Masc; countability Count
- said: /mu.si.ˈkal/ (es-ES), /mu.si.ˈkal/ (es-MX)
  1. [B1] obra de teatro o película en la que gran parte de la historia se canta y se baila
     - example: Este sábado vamos a ver un musical en el centro de la ciudad.
     - collocations: ver un musical; estrenar un musical; musical de Broadway
- relations: derived_from musical (ADJ)

### 34. martes

**Lexeme 1: martes**, NOUN
- grammar: gender Masc; countability Count; note same form in the plural: el martes, los martes
- said: /ˈmaɾ.tes/ (es-ES), /ˈmaɾ.tes/ (es-MX)
- forms: martes [Number=Plur]
  1. [A1] día de la semana que va después del lunes y antes del miércoles
     - example: Tenemos clase de natación todos los martes por la tarde.
     - collocations: el martes; los martes; martes y trece; martes de carnaval

### 35. combate

**Lexeme 1: combate**, NOUN
- grammar: gender Masc; countability Count
- said: /kom.ˈba.te/ (es-ES), /kom.ˈba.te/ (es-MX)
  1. [B1] lucha con armas entre soldados o grupos enfrentados
     - example: Muchos jóvenes soldados murieron en combate durante aquella guerra.
     - collocations: morir en combate; entrar en combate; combate cuerpo a cuerpo; zona de combate; avión de combate; fuera de combate; duro combate
  2. [B2] enfrentamiento entre dos deportistas que luchan siguiendo unas reglas
     - example: El campeón ganó el combate en el tercer asalto.
     - collocations: combate de boxeo; ganar un combate; perder un combate; combate por el título; dejar fuera de combate
  3. [B2] conjunto de acciones para acabar con algo malo o dañino
     - example: El gobierno anunció nuevas medidas para el combate contra la corrupción.
     - frames: combate contra algo; combate a algo; combate de algo
     - collocations: combate contra la pobreza; combate al crimen
     - labels: figurative True
- relations: verbal_noun_of combatir (VERB)

### 36. responsable

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

### 37. dignidad

**Lexeme 1: dignidad**, NOUN
- grammar: gender Fem
- said: /diɡ.ni.ˈdad/ (es-ES), /diɡ.ni.ˈdad/ (es-MX)
  1. [B2] valor que tiene toda persona por ser humana y que merece respeto
     - example: Todos los presos deben ser tratados con respeto a su dignidad.
     - collocations: dignidad humana; respetar la dignidad; atentar contra la dignidad; herir la dignidad; vivir con dignidad; una vida digna
     - grammar: countability Mass
  2. [B2] seriedad, orgullo y respeto por uno mismo en la manera de actuar
     - example: Aceptó la derrota con mucha dignidad y felicitó a su rival.
     - collocations: con dignidad; perder la dignidad; mantener la dignidad; conservar la dignidad
     - grammar: countability Mass
  3. [C1] cargo o título de mucho honor y autoridad
     - example: El obispo recibió esa alta dignidad cuando era todavía joven.
     - collocations: dignidad eclesiástica; alta dignidad
     - labels: formality formal
     - grammar: countability Count
- relations: abstract_noun_of digno (ADJ)

### 38. comenzar

**Lexeme 1: comenzar**, VERB
- grammar: conjugation 1; irregular True; valency Both; note stem change e > ie when stressed; z > c before e
- said: /ko.men.ˈθaɾ/ (es-ES), /ko.men.ˈsaɾ/ (es-MX)
- forms: comienzo [Mood=Ind, Tense=Pres, Person=1, Number=Sing]; comienzas [Mood=Ind, Tense=Pres, Person=2, Number=Sing]; comienza [Mood=Ind, Tense=Pres, Person=3, Number=Sing]; comenzamos [Mood=Ind, Tense=Pres, Person=1, Number=Plur]; comienzan [Mood=Ind, Tense=Pres, Person=3, Number=Plur]; comencé [Mood=Ind, Tense=Past, Person=1, Number=Sing]; comenzó [Mood=Ind, Tense=Past, Person=3, Number=Sing]; comenzaré [Mood=Ind, Tense=Fut, Person=1, Number=Sing]; comience [Mood=Sub, Tense=Pres, Person=1, Number=Sing]; comenzado [VerbForm=Part, Tense=Past]; comenzando [VerbForm=Ger]
  1. [A2] dar principio a una acción o una actividad, hacer su primera parte
     - example: Mañana comenzamos a pintar las paredes del salón.
     - frames: comenzar algo; comenzar a + inf; comenzar por algo; comenzar + ger
     - collocations: comenzar a trabajar; comenzar el día; comenzar de nuevo; comenzar de cero; comenzar una carrera
     - grammar: valency Tr, mainly
     - opposites: terminar
  2. [A2] tener principio, empezar a existir o a ocurrir algo
     - example: La reunión comienza a las nueve en la sala grande.
     - frames: comenzar con algo; comenzar en + lugar
     - collocations: comenzar la clase; comenzar la temporada; comenzar la guerra
     - grammar: valency Intr
     - opposites: terminar

### 39. jornada

**Lexeme 1: jornada**, NOUN
- grammar: gender Fem; countability Count
- said: /xoɾ.ˈna.da/ (es-ES), /xoɾ.ˈna.da/ (es-MX)
  1. [B1] tiempo que una persona dedica a trabajar cada día o cada semana
     - example: Este año mi madre trabaja a media jornada por las tardes.
     - collocations: jornada laboral; jornada completa; media jornada; jornada de trabajo; jornada intensiva; jornada partida; reducir la jornada
  2. [B1] día entero, sobre todo considerado por lo que ocurre en él
     - example: Fue una jornada muy larga y llegamos agotados a casa.
     - collocations: jornada electoral; jornada de huelga; jornada histórica; jornada de reflexión; una dura jornada
  3. [B2] cada una de las fechas en que se juegan los partidos de una competición
     - example: El equipo puede ganar el campeonato en la próxima jornada.
     - collocations: jornada de liga; primera jornada; última jornada
  4. [B2] reuniones de uno o varios días en las que se trata un tema concreto
     - example: La universidad organiza unas jornadas sobre medio ambiente en mayo.
     - frames: jornadas sobre algo; jornadas de algo
     - collocations: jornadas de puertas abiertas; organizar unas jornadas; jornadas técnicas
     - grammar: number Plur
  5. [C1] camino que se recorre en un día de viaje
     - example: El pueblo más cercano estaba a dos jornadas a caballo.
     - labels: style literary

### 40. dueño

**Lexeme 1: dueño**, NOUN
- grammar: gender Masc; countability Mass; note feminine dueña
- said: /ˈdwe.ɲo/ (es-ES), /ˈdwe.ɲo/ (es-MX)
  1. [A2] persona a quien pertenece una cosa, un animal o un negocio
     - example: Los dueños del restaurante quieren venderlo antes del verano.
     - frames: dueño de algo
     - collocations: el dueño de la casa; cambiar de dueño; nuevo dueño; legítimo dueño; dueño del perro; dueño del negocio
  2. [B2] persona que tiene control o libertad para decidir sobre algo
     - example: Cada uno es dueño de hacer con su vida lo que quiera.
     - frames: dueño de algo; ser dueño de + inf
     - collocations: ser dueño de sí mismo; hacerse dueño de la situación; dueño de su destino; dueño de sus actos

### 41. paciente

**Lexeme 1: paciente**, ADJ
- grammar: comparable True; note same form for masculine and feminine; plural pacientes
- said: /pa.ˈθjen.te/ (es-ES), /pa.ˈsjen.te/ (es-MX)
  1. [A2] que sabe esperar y soporta las molestias con calma, sin enfadarse
     - example: Hay que ser paciente con los niños cuando aprenden a leer.
     - frames: paciente con alguien
     - collocations: ser paciente; muy paciente; esperar paciente
     - opposites: impaciente
**Lexeme 2: paciente**, NOUN
- grammar: gender Com; countability Count
- said: /pa.ˈθjen.te/ (es-ES), /pa.ˈsjen.te/ (es-MX)
  1. [A2] persona que recibe atención o tratamiento de un médico
     - example: La doctora atiende a sus pacientes todas las mañanas.
     - collocations: atender a un paciente; paciente ingresado; paciente crónico; paciente terminal; dar de alta a un paciente; historial del paciente

### 42. terrible

**Lexeme 1: terrible**, ADJ
- grammar: comparable True; note same form for masculine and feminine; plural terribles
- said: /te.ˈri.ble/ (es-ES), /te.ˈri.ble/ (es-MX)
  1. [A2] que causa miedo, horror o mucho sufrimiento
     - example: La comida de ese restaurante es terrible y además carísima.
     - collocations: un accidente terrible; una noticia terrible; una tragedia terrible; una enfermedad terrible; un terrible error
  2. [A2] muy malo, de muy poca calidad o muy desagradable
     - example: Anoche hubo un accidente terrible en la carretera del norte.
     - collocations: un tiempo terrible; una película terrible
     - opposites: excelente
  3. [B1] muy grande o muy intenso, mucho más de lo normal
     - example: Tengo un hambre terrible desde esta mañana.
     - collocations: un hambre terrible; un dolor terrible; un calor terrible; un frío terrible; un miedo terrible
  4. [B1] de carácter difícil, inquieto o travieso, que cuesta soportar
     - example: Este niño es terrible, no se queda quieto ni un minuto.
     - labels: formality informal
     - grammar: position Pred, mainly

### 43. orgullo

**Lexeme 1: orgullo**, NOUN
- grammar: gender Fem
- said: /oɾ.ˈɡu.ʝo/ (es-ES), /oɾ.ˈɡu.ʝo/ (es-MX)
  1. [B1] sentimiento de satisfacción por lo que uno mismo u otra persona cercana ha hecho o tiene
     - example: Mis padres hablan con orgullo de los estudios de mi hermana.
     - frames: orgullo de algo/alguien; orgullo por algo; orgullo de + inf
     - collocations: con orgullo; sentir orgullo; llenar de orgullo; motivo de orgullo; orgullo nacional; un gran orgullo
     - grammar: countability Mass
  2. [B1] alta opinión de uno mismo y de su dignidad, muchas veces excesiva, que impide ceder
     - example: Por orgullo, nunca reconoce que se ha equivocado.
     - collocations: herir el orgullo; tragarse el orgullo; orgullo herido; por orgullo; dejar el orgullo
     - grammar: countability Mass
     - opposites: humildad
  3. [B2] persona o cosa que hace sentir muy satisfecho a alguien
     - example: Este hospital nuevo es el orgullo de todo el pueblo.
     - frames: el orgullo de alguien/algo
     - collocations: ser el orgullo de la familia
     - grammar: countability Count

### 44. salón

**Lexeme 1: salón**, NOUN
- grammar: gender Masc; countability Mass; note plural salones, without the accent
- said: /sa.ˈlon/ (es-ES), /sa.ˈlon/ (es-MX)
  1. [A1] habitación principal de una casa, donde la familia pasa el tiempo y recibe visitas
     - example: Después de cenar solemos ver la televisión en el salón.
     - collocations: salón comedor; mueble de salón
     - regions: es-ES mainly
  2. [B1] sala grande de un edificio donde se celebran fiestas, reuniones o actos públicos
     - example: La boda se celebrará en un salón del hotel junto al mar.
     - collocations: salón de actos; salón de baile; salón de fiestas; salón de banquetes; salón de eventos
  3. [B1] local abierto al público donde se ofrece un servicio determinado
     - example: Mi madre trabaja desde hace años en un salón de belleza.
     - collocations: salón de belleza; salón de té; salón de juegos; salón recreativo
  4. [B2] exposición o feria donde se muestran los productos de una industria
     - example: Este año el salón del automóvil presenta muchos coches eléctricos.
     - collocations: salón del automóvil; salón del libro
  5. [A2] habitación de una escuela donde los alumnos reciben las clases
     - example: Cuando sonó el timbre, todos regresamos al salón en silencio.
     - collocations: salón de clases; salón de clase
     - regions: es-419 mainly
- relations: augmentative_of sala (NOUN)

### 45. parar

**Lexeme 1: parar**, VERB
- grammar: conjugation 1; irregular False; valency Both
- said: /pa.ˈɾaɾ/ (es-ES), /pa.ˈɾaɾ/ (es-MX)
  1. [A2] dejar de moverse o dejar de hacer una acción
     - example: Mi vecino no para de hablar por teléfono en todo el día.
     - frames: parar de + inf; pararse a + inf
     - collocations: sin parar; parar en seco; pararse a pensar
     - grammar: valency Intr; note also pararse with the same meaning
  2. [A2] hacer que algo o alguien deje de moverse o de funcionar
     - example: Para el motor antes de bajar del coche, por favor.
     - collocations: parar un taxi; parar el motor; parar un penalti; parar el balón; parar un golpe
     - grammar: valency Tr
  3. [B1] ponerse de pie después de estar sentado o tumbado
     - example: Todos los alumnos se pararon cuando entró la maestra.
     - regions: es-419 mainly
     - grammar: valency Refl
  4. [B2] quedarse a vivir o estar durante un tiempo en un lugar
     - example: Cuando voy a la capital, paro en casa de mi tía.
     - frames: parar en + lugar
     - labels: formality informal
     - grammar: valency Intr
  5. [B2] llegar al final a un lugar o a una situación, a menudo sin esperarlo
     - example: No sé dónde habrán ido a parar mis llaves de casa.
     - frames: ir a parar a + lugar; parar en algo
     - grammar: valency Intr

### 46. móvil

**Lexeme 1: móvil**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈmo.bil/ (es-ES), /ˈmo.bil/ (es-MX)
  1. [A1] teléfono pequeño y sin cables que se lleva encima a todas partes
     - example: Apaga el móvil antes de entrar en la sala, por favor.
     - collocations: llamar al móvil; número de móvil; cargar el móvil; apagar el móvil; mirar el móvil; contestar el móvil
     - regions: es-ES mainly
  2. [B2] razón o causa que lleva a una persona a hacer algo, sobre todo un delito
     - example: La policía todavía no ha descubierto el móvil del asesinato.
     - frames: el móvil de algo
     - collocations: el móvil del crimen; móvil económico; móvil del robo
  3. [B2] vehículo, sobre todo el de la policía o el de un medio de comunicación
     - example: Un móvil policial llegó enseguida al lugar del accidente.
     - collocations: móvil policial
     - regions: es-AR mainly, es-UY mainly
  4. [C1] adorno de piezas colgantes que se mueven con el aire
     - example: Le compramos al bebé un móvil de estrellas para la cuna.
**Lexeme 2: móvil**, ADJ
- grammar: note same form for masculine and feminine; plural móviles
- said: /ˈmo.bil/ (es-ES), /ˈmo.bil/ (es-MX)
  1. [B1] que se mueve o puede cambiar de lugar con facilidad
     - example: Esta cocina tiene una mesa móvil con ruedas muy práctica.
     - collocations: teléfono móvil; telefonía móvil; dispositivo móvil; unidad móvil; fiesta móvil
     - opposites: inmóvil, fijo

### 47. definir

**Lexeme 1: definir**, VERB
- grammar: conjugation 3; irregular False
- said: /de.fi.ˈniɾ/ (es-ES), /de.fi.ˈniɾ/ (es-MX)
  1. [B1] explicar con exactitud el significado de una palabra o la naturaleza de algo
     - example: Es difícil explicar con pocas palabras cómo se define la felicidad.
     - frames: definir algo; definir algo como algo
     - collocations: definir un concepto; definir un término; difícil de definir; definir con precisión
     - grammar: valency Tr
  2. [B2] decidir o fijar algo de manera clara y exacta
     - example: Todavía tenemos que definir la fecha de la próxima reunión.
     - frames: definir algo
     - collocations: definir una estrategia; definir los objetivos; definir las reglas; definir los detalles; definir la fecha
     - grammar: valency Tr
  3. [B2] ser el rasgo principal que distingue a una persona o una cosa
     - example: Su generosidad la define mejor que cualquier otra cualidad.
     - frames: definir a alguien/algo
     - grammar: valency Tr
  4. [B2] declarar con claridad la propia postura, opinión o identidad
     - example: El partido todavía no se ha definido sobre la nueva ley.
     - frames: definirse sobre algo; definirse como algo
     - grammar: valency Refl

### 48. interesar

**Lexeme 1: interesar**, VERB
- grammar: conjugation 1; irregular False
- said: /in.te.ɾe.ˈsaɾ/ (es-ES), /in.te.ɾe.ˈsaɾ/ (es-MX)
  1. [A2] despertar la atención, la curiosidad o el gusto de alguien
     - example: Te interesa llegar temprano si quieres conseguir un buen sitio.
     - frames: interesar a alguien
     - collocations: interesar mucho; no interesar nada; interesar especialmente
     - grammar: valency Intr; note the person takes an indirect object, like gustar: me interesa
  2. [B1] ser útil, conveniente o de provecho para alguien
     - example: A mis hijos les interesan mucho los dinosaurios y los volcanes.
     - frames: interesar a alguien; interesar + inf; interesar que + subj
     - grammar: valency Intr; note the person takes an indirect object: nos interesa vender
  3. [B1] mostrar curiosidad o preocupación por algo o alguien y preguntar por ello
     - example: Mi vecina siempre se interesa por la salud de mi madre.
     - frames: interesarse por algo/alguien; interesarse en algo
     - grammar: valency Refl; reflexive True
- relations: derived_from interés (NOUN)

### 49. promoción

**Lexeme 1: promoción**, NOUN
- grammar: gender Fem
- said: /pɾo.mo.ˈθjon/ (es-ES), /pɾo.mo.ˈsjon/ (es-MX)
  1. [B1] conjunto de acciones para dar a conocer un producto o servicio y venderlo más
     - example: Este mes el supermercado tiene una promoción de dos por uno.
     - frames: promoción de algo
     - collocations: en promoción; campaña de promoción; promoción especial; precio de promoción; hacer una promoción; lanzar una promoción
     - grammar: countability Count
  2. [B2] paso de una persona a un puesto o categoría más alta en su trabajo
     - example: Después de cinco años en la empresa, por fin le dieron la promoción.
     - collocations: promoción interna; promoción profesional; conseguir una promoción
     - grammar: countability Count
  3. [B2] actividad para impulsar o favorecer el desarrollo de algo
     - example: El ayuntamiento destina más dinero a la promoción del turismo local.
     - frames: promoción de algo
     - collocations: promoción del empleo; promoción de la salud; promoción cultural
     - grammar: countability Mass
  4. [B1] grupo de personas que terminan sus estudios u obtienen un título al mismo tiempo
     - example: Cada diez años nos reunimos todos los compañeros de promoción.
     - collocations: compañero de promoción; la promoción de un año
     - grammar: countability Count
  5. [C1] conjunto de viviendas que una empresa construye y pone a la venta
     - example: Han empezado a vender los pisos de una nueva promoción junto al río.
     - collocations: promoción de viviendas; promoción inmobiliaria
     - regions: es-ES mainly
     - grammar: countability Count
- relations: verbal_noun_of promover (VERB)

### 50. concierto

**Lexeme 1: concierto**, NOUN
- grammar: gender Masc
- said: /kon.ˈθjeɾ.to/ (es-ES), /kon.ˈsjeɾ.to/ (es-MX)
  1. [A2] actuación en la que se toca música delante de un público
     - example: Este sábado vamos a un concierto de rock en el estadio.
     - frames: concierto de algo/alguien
     - collocations: dar un concierto; ir a un concierto; concierto en directo; concierto benéfico; sala de conciertos; entradas para el concierto
     - grammar: countability Count
  2. [B2] obra musical para uno o varios instrumentos solistas acompañados por una orquesta
     - example: La orquesta interpretará esta noche un concierto para violín muy conocido.
     - frames: concierto para algo
     - collocations: concierto para piano; concierto para violín y orquesta
     - labels: domain music
     - grammar: countability Count
  3. [C1] acuerdo o colaboración entre varias personas, grupos o países
     - example: Los dos gobiernos actuaron de concierto para resolver la crisis.
     - frames: de concierto con alguien
     - collocations: de concierto; el concierto internacional; el concierto de las naciones
     - labels: style elevated
     - grammar: countability Mass
- relations: derived_from concertar (VERB)

### 51. chino

**Lexeme 1: chino**, ADJ
- grammar: comparable False
- said: /ˈtʃi.no/ (es-ES), /ˈtʃi.no/ (es-MX)
  1. [A1] de China o de sus habitantes
     - example: Esta noche vamos a cenar en un restaurante chino del centro.
     - collocations: comida china; restaurante chino; año nuevo chino; tinta china
  2. [B1] que tiene el pelo con muchos rizos
     - example: Mi hijo tiene el pelo chino como su abuela.
     - collocations: pelo chino
     - regions: es-MX only
- relations: adjective_of China (PROPN)
**Lexeme 2: chino**, NOUN
- grammar: gender Masc
- said: /ˈtʃi.no/ (es-ES), /ˈtʃi.no/ (es-MX)
- forms: china [Gender=Fem]
  1. [A1] persona nacida en China o que tiene su nacionalidad
     - example: En mi barrio viven muchos chinos desde hace años.
     - grammar: countability Count
  2. [A1] lengua que se habla en China
     - example: Estoy aprendiendo chino porque quiero trabajar en Pekín.
     - collocations: hablar chino; chino mandarín; sonar a chino
     - grammar: countability Mass
  3. [B2] pantalón de algodón ligero y corte recto, de uso informal
     - example: Para la oficina suelo ponerme unos chinos y una camisa.
     - grammar: countability Count; number Plur, mainly
  4. [B2] tienda pequeña que vende de todo, llevada por inmigrantes asiáticos
     - example: Baja al chino y compra un par de pilas, por favor.
     - labels: formality informal
     - regions: es-ES only
     - grammar: countability Count

### 52. considerado

**Lexeme 1: considerado**, ADJ
- grammar: comparable True
- said: /kon.si.de.ˈɾa.do/ (es-ES), /kon.si.de.ˈɾa.do/ (es-MX)
  1. [B2] que piensa en los demás y los trata con atención y respeto
     - example: Mi vecino es muy considerado y nunca pone la música alta.
     - frames: considerado con alguien
     - collocations: muy considerado; ser considerado con los demás
     - opposites: desconsiderado
  2. [C1] que recibe aprecio y respeto de los demás por sus cualidades
     - example: Era un médico muy considerado en toda la comarca.
     - collocations: bien considerado; mal considerado
- relations: derived_from considerar (VERB)

### 53. alternativa

**Lexeme 1: alternativa**, NOUN
- grammar: gender Fem; countability Count
- said: /al.teɾ.na.ˈti.ba/ (es-ES), /al.teɾ.na.ˈti.ba/ (es-MX)
  1. [B1] cada una de las opciones posibles entre las que se puede elegir
     - example: Si el tren no sale, no nos queda otra alternativa que esperar.
     - frames: alternativa a algo; alternativa entre algo y algo
     - collocations: no tener alternativa; no quedar otra alternativa; buscar una alternativa; ofrecer una alternativa; alternativa viable; única alternativa; como alternativa
  2. [C2] ceremonia en la que un torero joven recibe el título de matador
     - example: El joven torero tomará la alternativa esta tarde en la plaza.
     - collocations: tomar la alternativa; dar la alternativa
- relations: derived_from alternativo (ADJ)
