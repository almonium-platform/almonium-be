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
  1. [B1] que ha ocurrido, aparecido o se ha hecho hace poco tiempo
     - example: Según un estudio reciente, cada vez leemos menos libros en papel.
     - collocations: historia reciente; de reciente creación; en fecha reciente; hasta fecha reciente; estudios recientes; noticias recientes; el más reciente
     - opposites: antiguo

### 3. sorpresa

**Lexeme 1: sorpresa**, NOUN
- grammar: gender Fem; countability Both
- said: /soɾ.ˈpɾe.sa/ (es-ES), /soɾ.ˈpɾe.sa/ (es-MX)
  1. [A2] cosa o suceso que alguien no espera y que le causa asombro
     - example: Mis amigos me prepararon una fiesta para mi cumpleaños y fue una gran sorpresa.
     - collocations: dar una sorpresa; llevarse una sorpresa; fiesta sorpresa; por sorpresa; una grata sorpresa; sorpresa agradable; tomar por sorpresa; coger por sorpresa
     - grammar: countability Count
  2. [B1] impresión que produce en alguien algo que no esperaba
     - example: Abrió la carta y su cara mostró una enorme sorpresa.
     - frames: para sorpresa de alguien; ante la sorpresa de alguien
     - collocations: causar sorpresa; con sorpresa; expresión de sorpresa; cara de sorpresa
     - grammar: countability Mass
- relations: derived_from sorprender (VERB)

### 4. escala

**Lexeme 1: escala**, NOUN
- grammar: gender Fem; countability Count
- said: /es.ˈka.la/ (es-ES), /es.ˈka.la/ (es-MX)
  1. [B1] serie ordenada de grados o valores que sirve para medir o clasificar algo
     - example: Valora el servicio del hotel en una escala del uno al diez.
     - frames: escala de algo
     - collocations: escala de valores; escala social; escala salarial; escala de Richter; en una escala del uno al diez
  2. [B2] tamaño o alcance con que se hace o se considera algo
     - example: La empresa quiere fabricar este producto a gran escala.
     - collocations: a gran escala; a pequeña escala; a escala mundial; a escala nacional
     - grammar: number Sing
  3. [B1] parada que hace un avión o un barco antes de llegar a su destino
     - example: Nuestro vuelo hace escala en una ciudad europea durante dos horas.
     - collocations: hacer escala; vuelo sin escalas; escala técnica
  4. [B2] proporción entre las medidas de un mapa, plano o modelo y las reales
     - example: Este mapa tiene una escala de uno a cien mil.
     - collocations: a escala; maqueta a escala; escala reducida
  5. [B2] serie de notas musicales ordenadas de más grave a más aguda
     - example: Cada día practico las escalas al piano antes de tocar.
     - collocations: escala mayor; escala menor; tocar escalas
     - labels: domain music
  6. [C1] objeto de cuerda o madera con peldaños que sirve para subir o bajar
     - example: Los marineros subieron al barco por una escala de cuerda.
     - collocations: escala de cuerda

### 5. seguramente

**Lexeme 1: seguramente**, ADV
- grammar: invariable True
- said: /se.ˌɡu.ɾa.ˈmen.te/ (es-ES), /se.ˌɡu.ɾa.ˈmen.te/ (es-MX)
  1. [A2] se usa para indicar que algo es muy probable, aunque no del todo cierto
     - example: Seguramente lleguemos tarde porque hay mucho tráfico en la autopista.
     - frames: seguramente + ind; seguramente + subj
     - grammar: note the verb after it may be in the indicative or the subjunctive
- relations: adverb_of seguro (ADJ)

### 6. beneficio

**Lexeme 1: beneficio**, NOUN
- grammar: gender Masc; countability Count
- said: /be.ne.ˈfi.θjo/ (es-ES), /be.ne.ˈfi.sjo/ (es-MX)
  1. [B1] bien o ventaja que alguien o algo recibe de una cosa o acción
     - example: Hacer ejercicio todos los días tiene muchos beneficios para la salud.
     - frames: beneficio para alguien/algo; en beneficio de alguien/algo; a beneficio de alguien/algo
     - collocations: sacar beneficio; en beneficio propio; beneficio mutuo; traer beneficios; el beneficio de la duda
     - opposites: perjuicio
  2. [B2] ganancia de dinero que se obtiene con un negocio o una inversión
     - example: Este año la empresa ha tenido beneficios mucho mayores que el anterior.
     - collocations: obtener beneficios; dar beneficios; beneficio neto; beneficio bruto; margen de beneficio; beneficios empresariales
     - labels: domain economics
     - grammar: note often in the plural
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
  1. [B1] enfrentamiento de palabras entre personas que no están de acuerdo, a menudo con enfado
     - example: Anoche tuve una discusión muy fuerte con mi hermano.
     - frames: discusión con alguien; discusión entre alguien; discusión por algo
     - collocations: tener una discusión; discusión acalorada; una fuerte discusión; provocar una discusión; entrar en discusiones; terminar la discusión
  2. [B1] conversación en la que se examinan con razones las opiniones sobre un asunto
     - example: La discusión del nuevo presupuesto durará toda la mañana.
     - frames: discusión sobre algo; discusión de algo
     - collocations: someter a discusión; abrir la discusión; estar en discusión; grupo de discusión; discusión pública
- relations: verbal_noun_of discutir (VERB)

### 9. intervención

**Lexeme 1: intervención**, NOUN
- grammar: gender Fem; countability Count
- said: /in.teɾ.ben.ˈθjon/ (es-ES), /in.teɾ.ben.ˈsjon/ (es-MX)
  1. [B1] participación de una persona o un grupo en un asunto para influir en él
     - example: La intervención de los vecinos evitó que el incendio se extendiera.
     - frames: intervención en algo; intervención de alguien
     - collocations: intervención militar; intervención extranjera; intervención del Estado; pedir la intervención; sin intervención
  2. [B2] momento en que una persona habla ante otras en una reunión o debate
     - example: Su intervención en el congreso duró apenas diez minutos.
     - collocations: breve intervención; turno de intervenciones; hacer una intervención
  3. [B2] operación que hace un cirujano en el cuerpo de un paciente
     - example: Mi abuelo se recupera bien después de la intervención.
     - collocations: intervención quirúrgica; someterse a una intervención
- relations: verbal_noun_of intervenir (VERB)

### 10. sentencia

**Lexeme 1: sentencia**, NOUN
- grammar: gender Fem; countability Count
- said: /sen.ˈten.θja/ (es-ES), /sen.ˈten.sja/ (es-MX)
  1. [B2] decisión de un juez o de un tribunal que resuelve un caso
     - example: Mañana el juez dará a conocer la sentencia del caso.
     - frames: sentencia contra alguien; sentencia sobre algo
     - collocations: dictar sentencia; sentencia firme; recurrir una sentencia; apelar una sentencia; sentencia absolutoria; sentencia condenatoria; sentencia de muerte; cumplir una sentencia
  2. [C1] frase breve que expresa una enseñanza o una verdad moral
     - example: Mi abuelo siempre repetía aquella vieja sentencia sobre la paciencia.
     - labels: style literary

### 11. imaginar

**Lexeme 1: imaginar**, VERB
- grammar: conjugation 1; irregular False
- said: /i.ma.xi.ˈnaɾ/ (es-ES), /i.ma.xi.ˈnaɾ/ (es-MX)
  1. [A2] formar en la mente la idea de algo que no se ve o no existe
     - example: Cierra los ojos e imagina que estás en una playa tranquila.
     - frames: imaginar algo; imaginar que + ind; imaginar + inf
     - collocations: difícil de imaginar; fácil de imaginar; no poder imaginar; imaginar el futuro
     - grammar: valency Tr; note also used with se: imagínate
  2. [B1] creer o suponer algo a partir de algunas señales, sin estar seguro
     - example: Me imagino que estarás cansado después de un viaje tan largo.
     - frames: imaginarse que + ind; imaginarse algo
     - collocations: ya me imagino; me lo imaginaba
     - grammar: valency Refl, mainly; note usually imaginarse; imaginar que is also used

### 12. significado

**Lexeme 1: significado**, NOUN
- grammar: gender Masc
- said: /siɡ.ni.fi.ˈka.do/ (es-ES), /siɡ.ni.fi.ˈka.do/ (es-MX)
  1. [A2] idea o contenido que expresa una palabra, una frase o un signo
     - example: Busca en el diccionario el significado de esta palabra.
     - frames: el significado de algo
     - collocations: buscar el significado; tener significado; significado literal; significado figurado; doble significado; sin significado
     - grammar: countability Count
  2. [B2] importancia o valor que tiene algo por lo que representa
     - example: Pocos entendieron entonces el verdadero significado de aquel acuerdo.
     - frames: el significado de algo
     - collocations: significado histórico; significado especial; significado simbólico; verdadero significado; cobrar significado
     - grammar: countability Mass, mainly
- relations: derived_from significar (VERB)
**Lexeme 2: significado**, ADJ
- grammar: position Attr, mainly; comparable False
- said: /siɡ.ni.fi.ˈka.do/ (es-ES), /siɡ.ni.fi.ˈka.do/ (es-MX)
  1. [C1] conocido y con prestigio dentro de un grupo o una actividad
     - example: Varios significados miembros del partido asistieron a la reunión.
     - labels: formality formal
- relations: derived_from significar (VERB)

### 13. mezcla

**Lexeme 1: mezcla**, NOUN
- grammar: gender Fem
- said: /ˈmeθ.kla/ (es-ES), /ˈmes.kla/ (es-MX)
  1. [B1] conjunto que resulta de juntar cosas o personas distintas
     - example: Esta salsa es una mezcla de tomate, ajo y aceite.
     - frames: mezcla de algo
     - collocations: una mezcla explosiva; una extraña mezcla; mezcla de culturas; mezcla homogénea; mezcla de sentimientos
     - grammar: countability Count
  2. [B1] acción de juntar cosas distintas para que formen un solo conjunto
     - example: Hay que hacer la mezcla de los ingredientes con mucho cuidado.
     - collocations: hacer la mezcla; mezcla de sonido
     - grammar: countability Both
  3. [C1] masa de cal o cemento, arena y agua que sirve para unir ladrillos
     - example: El albañil preparó la mezcla antes de levantar la pared.
     - collocations: preparar la mezcla
     - grammar: countability Mass
- relations: verbal_noun_of mezclar (VERB)

### 14. elegido

**Lexeme 1: elegido**, ADJ
- grammar: comparable False
- said: /e.le.ˈxi.do/ (es-ES), /e.le.ˈxi.do/ (es-MX)
  1. [C1] escogido por Dios o por el destino para una misión especial
     - example: Según aquella antigua tradición, eran el pueblo elegido por Dios.
     - collocations: pueblo elegido
- relations: derived_from elegir (VERB)
**Lexeme 2: elegido**, NOUN
- grammar: gender Masc; countability Count; note feminine elegida for a woman
- said: /e.le.ˈxi.do/ (es-ES), /e.le.ˈxi.do/ (es-MX)
- forms: elegida [Gender=Fem, Number=Sing]; elegidas [Gender=Fem, Number=Plur]
  1. [B2] persona escogida por Dios o por el destino para una misión especial
     - example: En la película, todos creen que ese chico es el elegido.
     - collocations: el elegido
  2. [C1] persona que forma parte de un grupo pequeño y privilegiado
     - example: Solo unos pocos elegidos pueden entrar en ese club.
     - collocations: unos pocos elegidos
     - grammar: number Plur, mainly
- relations: derived_from elegir (VERB)

### 15. reír

**Lexeme 1: reír**, VERB
- grammar: conjugation 3; irregular True; valency Both; note often used as reírse with the same meaning
- said: /re.ˈiɾ/ (es-ES), /re.ˈiɾ/ (es-MX)
- forms: río [Mood=Ind, Tense=Pres, Person=1, Number=Sing]; ríes [Mood=Ind, Tense=Pres, Person=2, Number=Sing]; ríe [Mood=Ind, Tense=Pres, Person=3, Number=Sing]; reímos [Mood=Ind, Tense=Pres, Person=1, Number=Plur]; ríen [Mood=Ind, Tense=Pres, Person=3, Number=Plur]; reí [Mood=Ind, Tense=Past, Person=1, Number=Sing]; rio [Mood=Ind, Tense=Past, Person=3, Number=Sing] (written rió before the 2010 spelling rules); reiré [Mood=Ind, Tense=Fut, Person=1, Number=Sing]; ría [Mood=Sub, Tense=Pres, Person=1, Number=Sing]; reído [VerbForm=Part, Tense=Past]; riendo [VerbForm=Ger]
  1. [A2] mostrar alegría o diversión con sonidos y movimientos de la cara
     - example: Los niños reían sin parar mientras miraban al payaso.
     - frames: reírse de algo; reír con alguien
     - collocations: reír a carcajadas; echarse a reír; hacer reír; reírse a carcajadas; reír sin parar; no poder parar de reír
     - grammar: valency Intr; note also reírse, with the same meaning
  2. [B1] burlarse de alguien o de algo, no tomarlo en serio
     - example: No te rías de tu hermano, que lo está pasando mal.
     - frames: reírse de alguien/algo
     - grammar: valency Refl
  3. [B2] celebrar con alegría lo que otra persona dice o hace
     - example: Siempre le ríe los chistes a su jefe, aunque no tengan gracia.
     - frames: reírle algo a alguien
     - collocations: reír las gracias; reír un chiste
     - grammar: valency Tr

### 16. impuesto

**Lexeme 1: impuesto**, NOUN
- grammar: gender Masc; countability Count
- said: /im.ˈpwes.to/ (es-ES), /im.ˈpwes.to/ (es-MX)
  1. [B1] dinero que las personas y empresas deben pagar obligatoriamente al Estado
     - example: Este año vamos a pagar más impuestos que el anterior.
     - frames: impuesto sobre algo; impuesto a algo
     - collocations: pagar impuestos; cobrar impuestos; subir los impuestos; bajar los impuestos; impuesto sobre la renta; libre de impuestos; evadir impuestos; declaración de impuestos
     - grammar: note impuesto a algo is mostly used in Latin America
- relations: derived_from imponer (VERB)

### 17. clima

**Lexeme 1: clima**, NOUN
- grammar: gender Masc; countability Count; note masculine although it ends in -a: el clima
- said: /ˈkli.ma/ (es-ES), /ˈkli.ma/ (es-MX)
  1. [A2] conjunto de condiciones atmosféricas propias de una región a lo largo del año
     - example: El clima de esta región es seco y caluroso casi todo el año.
     - collocations: clima templado; clima tropical; clima mediterráneo; clima seco; clima húmedo; clima frío
  2. [A2] estado de la atmósfera en un lugar y un momento concretos
     - example: Hoy el clima está perfecto para ir a la playa.
     - collocations: buen clima; mal clima
     - regions: es-419 mainly
     - grammar: number Sing
  3. [B2] ambiente o conjunto de circunstancias que rodean una situación o un grupo
     - example: En la reunión se respiraba un clima de tensión difícil de soportar.
     - frames: clima de algo
     - collocations: clima de tensión; clima de confianza; clima laboral; clima político; crear un clima; clima de violencia
     - labels: figurative True
  4. [B1] aparato que enfría o calienta el aire de un lugar cerrado
     - example: Prende el clima, que hace muchísimo calor aquí dentro.
     - collocations: prender el clima; apagar el clima
     - labels: formality informal
     - regions: es-MX mainly

### 18. permitir

**Lexeme 1: permitir**, VERB
- grammar: conjugation 3; irregular False
- said: /peɾ.mi.ˈtiɾ/ (es-ES), /peɾ.mi.ˈtiɾ/ (es-MX)
  1. [A2] dar a alguien la autorización para hacer algo, o no impedirlo
     - example: Mis padres no me permiten salir hasta tan tarde.
     - frames: permitir algo a alguien; permitir a alguien + inf; permitir que + subj
     - collocations: no se permite fumar; permitir la entrada; permitir el paso; si me permite
     - grammar: valency Tr
     - opposites: prohibir
  2. [B1] hacer posible o más fácil que algo ocurra
     - example: El nuevo tren permite llegar a la capital en dos horas.
     - frames: permitir + inf; permitir que + subj; permitir algo
     - grammar: valency Tr
  3. [B1] tener el dinero, el tiempo o los medios para hacer o tener algo
     - example: Este año no podemos permitirnos unas vacaciones en el extranjero.
     - frames: permitirse algo; permitirse + inf
     - collocations: permitirse el lujo de; no poder permitirse
     - grammar: valency Refl; note used as permitirse; se means for oneself, and the verb takes a direct object
  4. [C1] tomarse la libertad de decir o hacer algo
     - example: Me permito recordarle que la reunión empieza a las diez.
     - frames: permitirse + inf
     - labels: formality formal
     - grammar: valency Refl; note used as permitirse, often in polite formulas

### 19. víctima

**Lexeme 1: víctima**, NOUN
- grammar: gender Fem; countability Count; note always feminine, also when it refers to a man: él fue la víctima
- said: /ˈbik.ti.ma/ (es-ES), /ˈbik.ti.ma/ (es-MX)
  1. [B1] persona o animal que sufre un daño causado por otros, por un accidente o por una desgracia
     - example: Muchas familias fueron víctimas de las inundaciones del año pasado.
     - frames: víctima de algo; víctima de alguien
     - collocations: ser víctima de; hacerse la víctima; víctima mortal; víctimas civiles; cobrarse víctimas; víctima inocente; víctima de un robo; víctima de violencia
  2. [C1] persona o animal que se mata como ofrenda a un dios
     - example: Los sacerdotes ofrecían una víctima a los dioses cada primavera.
     - collocations: víctima propiciatoria
     - labels: domain religion

### 20. vender

**Lexeme 1: vender**, VERB
- grammar: conjugation 2; irregular False
- said: /ben.ˈdeɾ/ (es-ES), /ben.ˈdeɾ/ (es-MX)
  1. [A1] dar algo a otra persona a cambio de una cantidad de dinero
     - example: Mis vecinos quieren vender su casa antes del verano.
     - frames: vender algo a alguien; vender algo por + cantidad
     - collocations: vender caro; vender barato; vender a plazos; vender al por mayor; vender al por menor; se vende; vender bien
     - grammar: valency Tr, mainly
     - opposites: comprar
  2. [B2] traicionar a alguien que confiaba en uno, entregándolo a otros
     - example: Nunca pensé que mi mejor amigo me vendería de esa manera.
     - frames: vender a alguien
     - labels: figurative True
     - grammar: valency Tr
  3. [C1] aceptar dinero o favores a cambio de actuar contra sus principios
     - example: Aquel periodista se vendió por un puñado de billetes.
     - frames: venderse a alguien; venderse por algo
     - labels: figurative True
     - grammar: valency Refl

### 21. definitivamente

**Lexeme 1: definitivamente**, ADV
- grammar: invariable True
- said: /de.fi.ni.ˌti.ba.ˈmen.te/ (es-ES), /de.fi.ni.ˌti.ba.ˈmen.te/ (es-MX)
  1. [B1] de manera final, sin posibilidad de cambio ni vuelta atrás
     - example: Tras muchos años fuera, decidió quedarse definitivamente en su pueblo.
     - collocations: cerrar definitivamente; abandonar definitivamente; resolver definitivamente; quedarse definitivamente
  2. [B1] sin ninguna duda, con total seguridad
     - example: Definitivamente, esta es la mejor película que he visto este año.
     - collocations: definitivamente no
- relations: adverb_of definitivo (ADJ)

### 22. tranquilo

**Lexeme 1: tranquilo**, ADJ
- grammar: comparable True
- said: /tɾan.ˈki.lo/ (es-ES), /tɾan.ˈki.lo/ (es-MX)
  1. [A1] sin ruido, agitación ni movimiento fuerte; en calma y en paz
     - example: Buscamos un pueblo tranquilo para pasar el verano.
     - collocations: barrio tranquilo; vida tranquila; llevar una vida tranquila; noche tranquila; mar tranquilo; lugar tranquilo
  2. [A2] que no siente nervios, miedo ni preocupación en un momento dado
     - example: Ya puedes estar tranquilo, el médico dice que no es nada.
     - frames: dejar tranquilo a alguien
     - collocations: estar tranquilo; quedarse tranquilo; dejar tranquilo; dormir tranquilo; tener la conciencia tranquila
     - opposites: intranquilo
  3. [A2] dicho de una persona, de carácter sereno, que no se altera fácilmente
     - example: Mi hijo pequeño es mucho más tranquilo que su hermana.
     - collocations: ser tranquilo; carácter tranquilo
     - opposites: nervioso
**Lexeme 2: tranquilo**, INTJ
- grammar: note agrees with the person addressed: tranquila, tranquilos, tranquilas
- said: /tɾan.ˈki.lo/ (es-ES), /tɾan.ˈki.lo/ (es-MX)
- forms: tranquila [Gender=Fem, Number=Sing]; tranquilos [Gender=Masc, Number=Plur]; tranquilas [Gender=Fem, Number=Plur]
  1. [A2] se usa para pedir a alguien que se calme o que no se preocupe
     - example: Tranquilo, hombre, que todo va a salir bien.

### 23. descubrir

**Lexeme 1: descubrir**, VERB
- grammar: conjugation 3; irregular True; note irregular only in the past participle descubierto
- said: /des.ku.ˈbɾiɾ/ (es-ES), /des.ku.ˈbɾiɾ/ (es-MX)
- forms: descubro [Mood=Ind, Tense=Pres, Person=1, Number=Sing]; descubres [Mood=Ind, Tense=Pres, Person=2, Number=Sing]; descubre [Mood=Ind, Tense=Pres, Person=3, Number=Sing]; descubrimos [Mood=Ind, Tense=Pres, Person=1, Number=Plur]; descubren [Mood=Ind, Tense=Pres, Person=3, Number=Plur]; descubrí [Mood=Ind, Tense=Past, Person=1, Number=Sing]; descubrió [Mood=Ind, Tense=Past, Person=3, Number=Sing]; descubriré [Mood=Ind, Tense=Fut, Person=1, Number=Sing]; descubra [Mood=Sub, Tense=Pres, Person=1, Number=Sing]; descubierto [VerbForm=Part, Tense=Past]; descubriendo [VerbForm=Ger]
  1. [A2] hallar algo o a alguien que no se conocía o estaba escondido
     - example: Los científicos han descubierto una nueva especie de rana en la selva.
     - frames: descubrir algo/alguien
     - collocations: descubrir una cura; descubrir un tesoro; descubrir una especie; descubrir un talento
     - grammar: valency Tr
  2. [B1] llegar a saber algo que antes se ignoraba
     - example: Hace poco descubrí que mi vecino también toca el piano.
     - frames: descubrir algo; descubrir que + ind
     - collocations: descubrir la verdad; descubrir el engaño
     - grammar: valency Tr
  3. [B2] dar a conocer algo que era secreto o estaba oculto
     - example: Por favor, no le descubras a nadie nuestro plan.
     - frames: descubrir algo a alguien
     - collocations: descubrir un secreto; descubrir las intenciones; descubrir el juego
     - grammar: valency Tr
     - opposites: ocultar
  4. [B2] quitar lo que tapa una cosa para dejarla a la vista
     - example: El alcalde descubrirá mañana la placa en honor de los vecinos.
     - collocations: descubrir una placa; descubrir una estatua
     - grammar: valency Tr
     - opposites: cubrir, tapar
  5. [C1] quitarse el sombrero o la gorra, a menudo como señal de respeto o admiración
     - example: Ante un trabajo tan bien hecho, me descubro.
     - frames: descubrirse ante algo/alguien
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
     - example: Miércoles, otra vez me he dejado las llaves dentro de casa.
     - labels: formality informal, attitude euphemistic

### 25. regresar

**Lexeme 1: regresar**, VERB
- grammar: conjugation 1; irregular False
- said: /re.ɡɾe.ˈsaɾ/ (es-ES), /re.ɡɾe.ˈsaɾ/ (es-MX)
  1. [A2] ir o venir de nuevo al lugar de donde se había salido
     - example: Mañana regresamos a la ciudad después de dos semanas en la playa.
     - frames: regresar a + lugar; regresar de + lugar
     - collocations: regresar a casa; regresar del trabajo; regresar pronto; regresar tarde; regresar sano y salvo
     - grammar: valency Intr; note in Latin America also used as regresarse with the same meaning
  2. [B1] dar una cosa a la persona que la tenía antes
     - example: Te regreso el libro en cuanto termine de leerlo.
     - frames: regresar algo a alguien
     - regions: es-419 mainly
     - grammar: valency Tr

### 26. acceder

**Lexeme 1: acceder**, VERB
- grammar: conjugation 2; irregular False; valency Intr
- said: /ak.θe.ˈdeɾ/ (es-ES), /ak.se.ˈdeɾ/ (es-MX)
  1. [B1] entrar o llegar a un lugar, o poder pasar a él
     - example: Desde la plaza se accede al castillo por una escalera estrecha.
     - frames: acceder a + lugar; acceder por + lugar
     - collocations: acceder al edificio; acceder directamente; acceder libremente
  2. [B1] conseguir entrar en un sistema informático, una cuenta o unos datos para usarlos
     - example: Necesitas una contraseña para acceder a tu cuenta del banco.
     - frames: acceder a algo
     - collocations: acceder a internet; acceder a la cuenta; acceder a los datos; acceder a la información
  3. [B2] llegar a tener un cargo, una situación o un derecho
     - example: Muchos jóvenes no pueden acceder a una vivienda propia.
     - frames: acceder a algo
     - collocations: acceder al poder; acceder al trono; acceder a la universidad; acceder a un puesto; acceder a una vivienda
  4. [B2] aceptar lo que otra persona pide o propone
     - example: Al final, el director accedió a cambiar la fecha del examen.
     - frames: acceder a algo; acceder a + inf
     - collocations: acceder a una petición; acceder a una solicitud; acceder a las demandas
     - opposites: negarse

### 27. pasión

**Lexeme 1: pasión**, NOUN
- grammar: gender Fem; countability Both
- said: /pa.ˈsjon/ (es-ES), /pa.ˈsjon/ (es-MX)
- forms: pasiones [Number=Plur]
  1. [B1] sentimiento muy intenso, sobre todo de amor o deseo, difícil de controlar
     - example: Entre ellos nació una pasión que duró toda la vida.
     - collocations: con pasión; pasión amorosa; despertar pasiones; dejarse llevar por la pasión; crimen pasional
  2. [B1] gusto o interés muy grande por una actividad o una cosa
     - example: Desde pequeño tengo una auténtica pasión por la música clásica.
     - frames: pasión por algo; pasión por + inf
     - collocations: sentir pasión por; gran pasión; vivir con pasión
  3. [C1] sufrimientos de Jesucristo desde su detención hasta su muerte en la cruz
     - example: En Semana Santa se representa la Pasión en muchos pueblos.
     - collocations: la Pasión de Cristo; domingo de Pasión
     - labels: domain religion
     - grammar: number Sing; countability Count; note usually written with a capital: la Pasión

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
  1. [A2] que pertenece al arte de combinar sonidos y ritmos, o sirve para él
     - example: Mi hijo quiere aprender a tocar un instrumento musical este año.
     - collocations: instrumento musical; género musical; grupo musical; nota musical; oído musical; gusto musical; educación musical; banda musical
     - grammar: comparable False
  2. [B2] de sonido agradable, suave y armonioso al oído
     - example: Tiene una voz tan musical que da gusto escucharla.
     - collocations: voz musical; acento musical
     - grammar: comparable True
- relations: adjective_of música (NOUN)
**Lexeme 2: musical**, NOUN
- grammar: gender Masc; countability Count
- said: /mu.si.ˈkal/ (es-ES), /mu.si.ˈkal/ (es-MX)
  1. [B1] obra de teatro o película en la que los personajes cantan y bailan
     - example: Este fin de semana vamos a ver un musical en el centro.
     - collocations: ver un musical; estrenar un musical; protagonizar un musical

### 31. martes

**Lexeme 1: martes**, NOUN
- grammar: gender Masc; countability Count; note same form in the plural: el martes, los martes
- said: /ˈmaɾ.tes/ (es-ES), /ˈmaɾ.tes/ (es-MX)
- forms: martes [Number=Plur]
  1. [A1] día de la semana que va después del lunes y antes del miércoles
     - example: Tenemos clase de natación todos los martes por la tarde.
     - collocations: el martes; los martes; martes y trece

### 32. combate

**Lexeme 1: combate**, NOUN
- grammar: gender Masc; countability Count
- said: /kom.ˈba.te/ (es-ES), /kom.ˈba.te/ (es-MX)
  1. [B1] lucha con armas entre soldados o grupos armados
     - example: Muchos soldados murieron durante el combate cerca del río.
     - collocations: entrar en combate; fuera de combate; combate cuerpo a combate; zona de combate; avión de combate; muerto en combate; combate feroz
  2. [B1] pelea entre dos deportistas, según unas reglas, en boxeo y deportes parecidos
     - example: El campeón ganó el combate en el tercer asalto.
     - collocations: combate de boxeo; ganar un combate; perder un combate; dejar fuera de combate
     - labels: domain sport
  3. [B2] conjunto de acciones para acabar con un problema o un mal
     - example: El gobierno anunció nuevas medidas para el combate contra la corrupción.
     - frames: combate contra algo; combate de algo
     - collocations: combate contra la pobreza; combate contra el crimen
     - labels: figurative True
- relations: verbal_noun_of combatir (VERB)

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
  1. [B2] valor que tiene toda persona y que merece el respeto de los demás
     - example: Todos los trabajadores merecen un trato que respete su dignidad.
     - collocations: dignidad humana; respetar la dignidad; atentar contra la dignidad; herir la dignidad; vivir con dignidad; morir con dignidad; perder la dignidad
     - grammar: countability Mass
  2. [B2] seriedad y respeto por uno mismo en la manera de comportarse
     - example: Aceptó la derrota con mucha dignidad y felicitó al ganador.
     - collocations: con dignidad; mantener la dignidad
     - grammar: countability Mass
  3. [C1] cargo o puesto importante que da honor a quien lo ocupa
     - example: Durante años ocupó la dignidad de obispo en aquella ciudad.
     - labels: style elevated
     - grammar: countability Count
- relations: abstract_noun_of digno (ADJ)

### 35. comenzar

**Lexeme 1: comenzar**, VERB
- grammar: conjugation 1; irregular True; valency Both; note stem changes e > ie when stressed; z becomes c before e
- said: /ko.men.ˈθaɾ/ (es-ES), /ko.men.ˈsaɾ/ (es-MX)
- forms: comienzo [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; comienzas [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin]; comienza [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin]; comenzamos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin]; comienzan [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin]; comencé [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin]; comenzó [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin]; comenzaré [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin]; comience [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; comenzado [Tense=Past, VerbForm=Part]; comenzando [VerbForm=Ger]
  1. [A2] dar principio a una acción o actividad, ponerse a hacerla
     - example: Mañana comenzamos las clases de inglés en la academia.
     - frames: comenzar algo; comenzar a + inf; comenzar por algo; comenzar por + inf
     - collocations: comenzar de nuevo; comenzar de cero; comenzar un proyecto; comenzar una carrera; comenzar a trabajar
     - grammar: valency Tr, mainly
     - opposites: terminar
  2. [A2] tener principio una cosa en un momento o lugar determinado
     - example: La película comienza a las nueve y dura dos horas.
     - frames: comenzar con algo; comenzar en + lugar
     - collocations: comenzar el curso; comenzar la temporada
     - grammar: valency Intr
     - opposites: terminar

### 36. jornada

**Lexeme 1: jornada**, NOUN
- grammar: gender Fem; countability Count
- said: /xoɾ.ˈna.da/ (es-ES), /xoɾ.ˈna.da/ (es-MX)
  1. [B1] tiempo que una persona dedica cada día o cada semana a trabajar
     - example: Mi madre trabaja a media jornada para cuidar de mis abuelos.
     - collocations: jornada laboral; jornada completa; media jornada; jornada partida; jornada intensiva; jornada reducida; jornada de ocho horas; trabajar a jornada completa
  2. [B1] día considerado por lo que ocurre o se hace en él
     - example: Ha sido una jornada muy larga y estoy agotado.
     - collocations: jornada electoral; jornada histórica; jornada de huelga; jornada de reflexión; una larga jornada; jornada de puertas abiertas
  3. [B2] conjunto de partidos de una competición que se juegan en las mismas fechas
     - example: El equipo puede ganar la liga en la próxima jornada.
     - collocations: jornada de liga; primera jornada; última jornada
  4. [B2] reunión de varios días en la que especialistas presentan y discuten sobre un tema
     - example: La universidad organiza unas jornadas sobre el cambio climático.
     - frames: jornadas sobre algo; jornadas de algo
     - collocations: organizar unas jornadas; jornadas técnicas; celebrar unas jornadas
     - grammar: number Plur
  5. [C1] camino que se recorre en un día de viaje
     - example: El pueblo más cercano quedaba a dos jornadas de camino.
     - collocations: jornada de camino; jornada de viaje
     - labels: style literary

### 37. paciente

**Lexeme 1: paciente**, ADJ
- grammar: comparable True; note same form for masculine and feminine; plural pacientes
- said: /pa.ˈθjen.te/ (es-ES), /pa.ˈsjen.te/ (es-MX)
  1. [A2] que sabe esperar con calma y soporta las molestias sin enfadarse
     - example: Hay que ser muy paciente para enseñar a niños pequeños.
     - frames: paciente con alguien
     - collocations: muy paciente; ser paciente; tener que ser paciente
     - opposites: impaciente
**Lexeme 2: paciente**, NOUN
- grammar: gender Com; countability Count
- said: /pa.ˈθjen.te/ (es-ES), /pa.ˈsjen.te/ (es-MX)
  1. [B1] persona a la que un médico atiende o trata de una enfermedad
     - example: La doctora visita a sus pacientes cada mañana en el hospital.
     - collocations: atender a un paciente; paciente ingresado; paciente grave; dar de alta a un paciente; paciente crónico

### 38. parar

**Lexeme 1: parar**, VERB
- grammar: conjugation 1; irregular False; valency Both
- said: /pa.ˈɾaɾ/ (es-ES), /pa.ˈɾaɾ/ (es-MX)
  1. [A2] dejar de moverse, de funcionar o de hacer algo
     - example: Mi vecino no ha parado de hablar en toda la tarde.
     - frames: parar de + inf; parar en + lugar; pararse a + inf
     - collocations: sin parar; no parar de; parar en seco; parar un momento
     - grammar: valency Intr; note often used as pararse with the same meaning
     - opposites: continuar
  2. [A2] hacer que alguien o algo deje de moverse o de funcionar
     - example: Para el coche aquí, que quiero comprar el periódico.
     - collocations: parar un taxi; parar el coche; parar el motor; parar un penalti; parar un golpe; parar los pies
     - grammar: valency Tr
  3. [B1] ponerse de pie una persona que estaba sentada o tumbada
     - example: Todos nos paramos cuando la jueza entró en la sala.
     - regions: es-419 only
     - grammar: valency Refl
     - opposites: sentarse
  4. [B2] terminar llegando a un lugar o a una situación, a menudo sin quererlo
     - example: Nadie sabe dónde fue a parar el dinero de la fiesta.
     - frames: ir a parar a + lugar; parar en algo
     - collocations: ir a parar
     - grammar: valency Intr; note mostly in the phrase ir a parar
  5. [B2] vivir o estar alojado en un sitio, o estar en él con frecuencia
     - example: Este mes estoy parando en casa de unos amigos.
     - frames: parar en + lugar
     - collocations: no parar en casa
     - labels: formality informal
     - grammar: valency Intr
  6. [B2] dejar de trabajar los empleados como protesta
     - example: Los conductores de autobús pararán durante veinticuatro horas.
     - grammar: valency Intr
  7. [B2] poner algo derecho o en posición vertical
     - example: Para la escoba contra la pared, por favor.
     - collocations: parar la oreja
     - regions: es-419 only
     - grammar: valency Tr

### 39. móvil

**Lexeme 1: móvil**, ADJ
- grammar: note same form for masculine and feminine; plural móviles
- said: /ˈmo.bil/ (es-ES), /ˈmo.bil/ (es-MX)
  1. [B1] que puede moverse o llevarse de un lugar a otro
     - example: Una unidad móvil del hospital atiende a los pueblos de la sierra.
     - collocations: teléfono móvil; telefonía móvil; dispositivo móvil; unidad móvil; escalera móvil
     - opposites: inmóvil
**Lexeme 2: móvil**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈmo.bil/ (es-ES), /ˈmo.bil/ (es-MX)
  1. [A1] teléfono pequeño sin cables que se puede llevar a cualquier sitio
     - example: Apaga el móvil antes de entrar en el cine, por favor.
     - collocations: llamar al móvil; cargar el móvil; apagar el móvil; mirar el móvil; número de móvil; quedarse sin batería en el móvil
     - regions: es-ES mainly
  2. [B2] razón o causa que lleva a alguien a cometer un delito o actuar
     - example: La policía todavía no conoce el móvil del asesinato.
     - frames: el móvil de algo
     - collocations: el móvil del crimen; móvil económico; móvil político
  3. [C1] adorno de piezas que cuelgan de hilos y se mueven con el aire
     - example: Sobre la cuna del bebé hay un móvil de estrellas de colores.

### 40. definir

**Lexeme 1: definir**, VERB
- grammar: conjugation 3; irregular False
- said: /de.fi.ˈniɾ/ (es-ES), /de.fi.ˈniɾ/ (es-MX)
  1. [B1] explicar con exactitud el significado de una palabra o una idea
     - example: El profesor nos pidió definir cada término con nuestras propias palabras.
     - frames: definir algo; definir algo como algo
     - collocations: definir un concepto; definir un término; difícil de definir
     - grammar: valency Tr
  2. [B2] fijar con claridad y precisión los rasgos, límites o detalles de algo
     - example: Primero debemos definir los objetivos del proyecto con todo el equipo.
     - frames: definir algo
     - collocations: definir una estrategia; definir los objetivos; definir las reglas; definir claramente; definir los límites
     - grammar: valency Tr
  3. [B2] mostrar uno con claridad su postura, opinión o manera de ser
     - example: Ella se define como una persona tranquila y muy trabajadora.
     - frames: definirse como algo; definirse sobre algo
     - grammar: valency Refl; reflexive True

### 41. promoción

**Lexeme 1: promoción**, NOUN
- grammar: gender Fem
- said: /pɾo.mo.ˈθjon/ (es-ES), /pɾo.mo.ˈsjon/ (es-MX)
  1. [B1] paso de una persona a un cargo, una categoría o un grado superior
     - example: Después de cinco años en la empresa, por fin le dieron la promoción.
     - collocations: conseguir una promoción; promoción interna; promoción profesional
     - grammar: countability Count
  2. [B1] conjunto de acciones para dar a conocer algo o a alguien y aumentar sus ventas
     - example: La cantante está de gira de promoción de su nuevo disco.
     - frames: promoción de algo
     - collocations: campaña de promoción; hacer promoción; gira de promoción; promoción turística
     - grammar: countability Count
  3. [B1] oferta especial que permite comprar un producto más barato o con un regalo
     - example: Esta semana los yogures están en promoción en el supermercado.
     - collocations: en promoción; promoción especial; aprovechar una promoción
     - grammar: countability Count
  4. [B2] acción de impulsar el desarrollo o la mejora de una actividad o un grupo
     - example: El ayuntamiento destina más dinero a la promoción del empleo juvenil.
     - frames: promoción de algo
     - collocations: promoción de la salud; promoción del empleo; promoción cultural
     - grammar: countability Mass
  5. [B2] conjunto de personas que terminan sus estudios u obtienen un grado al mismo tiempo
     - example: Todavía me reúno cada año con mis compañeros de promoción.
     - collocations: compañero de promoción
     - grammar: countability Count
  6. [C1] conjunto de viviendas que una empresa construye para venderlas
     - example: Han empezado a vender los pisos de una nueva promoción cerca del río.
     - collocations: promoción de viviendas; promoción inmobiliaria
     - regions: es-ES mainly
     - grammar: countability Count

### 42. concierto

**Lexeme 1: concierto**, NOUN
- grammar: gender Masc
- said: /kon.ˈθjeɾ.to/ (es-ES), /kon.ˈsjeɾ.to/ (es-MX)
  1. [A2] función en la que uno o varios músicos tocan o cantan ante un público
     - example: Anoche fuimos a un concierto de rock en el estadio.
     - collocations: dar un concierto; ir a un concierto; concierto de rock; sala de conciertos; concierto al aire libre; entradas para el concierto
     - grammar: countability Count
  2. [B2] obra musical para uno o más instrumentos solistas acompañados por una orquesta
     - example: La orquesta interpretará esta noche un concierto para piano.
     - collocations: concierto para piano; concierto para violín
     - labels: domain music
     - grammar: countability Count
  3. [C1] acuerdo o buena coordinación entre varias personas, grupos o países
     - example: Estas decisiones exigen el concierto de todas las administraciones.
     - collocations: el concierto internacional; sin orden ni concierto
     - labels: formality formal
     - grammar: countability Mass

### 43. chino

**Lexeme 1: chino**, ADJ
- said: /ˈtʃi.no/ (es-ES), /ˈtʃi.no/ (es-MX)
  1. [A1] de China, de sus habitantes o de su lengua
     - example: Esta noche vamos a probar un restaurante chino nuevo.
     - collocations: comida china; restaurante chino; año nuevo chino; barrio chino; muralla china
     - grammar: comparable False
  2. [B1] dicho del pelo, que forma rizos pequeños y apretados
     - example: Mi hijo tiene el pelo chino igual que su abuelo.
     - collocations: pelo chino
     - regions: es-MX mainly
     - grammar: note mostly said of hair
     - opposites: lacio
- relations: adjective_of China (PROPN)
**Lexeme 2: chino**, NOUN
- grammar: gender Masc; countability Count; note feminine china for a woman
- said: /ˈtʃi.no/ (es-ES), /ˈtʃi.no/ (es-MX)
- forms: china [Gender=Fem, Number=Sing]; chinas [Gender=Fem, Number=Plur]
  1. [A1] persona nacida en China o que tiene su nacionalidad
     - example: En el congreso conocí a varios chinos que estudiaban ingeniería.
**Lexeme 3: chino**, NOUN
- grammar: gender Masc
- said: /ˈtʃi.no/ (es-ES), /ˈtʃi.no/ (es-MX)
  1. [A1] lengua que se habla en China, con varias formas regionales
     - example: Desde hace dos años estudio chino en una academia.
     - collocations: hablar chino; aprender chino; chino mandarín
     - grammar: countability Mass; number Sing
  2. [B2] lenguaje o asunto que resulta imposible de entender
     - example: Para mí, las instrucciones de este aparato son puro chino.
     - collocations: sonar a chino; hablar en chino
     - labels: formality informal, figurative True
     - grammar: countability Mass; number Sing
  3. [B1] restaurante de cocina oriental, atendido normalmente por inmigrantes asiáticos
     - example: Cuando no tenemos ganas de cocinar, pedimos comida al chino.
     - collocations: ir al chino; cenar en el chino
     - labels: formality informal
     - regions: es-ES mainly
     - grammar: countability Count
  4. [B1] tienda de barrio con productos variados y baratos, atendida por inmigrantes asiáticos
     - example: Compré pilas y cinta adhesiva en el chino de abajo.
     - collocations: el chino de la esquina
     - labels: formality informal
     - regions: es-ES mainly, es-AR mainly
     - grammar: countability Count

### 44. considerado

**Lexeme 1: considerado**, ADJ
- grammar: comparable True
- said: /kon.si.de.ˈɾa.do/ (es-ES), /kon.si.de.ˈɾa.do/ (es-MX)
  1. [B2] que piensa en los demás y los trata con atención y respeto
     - example: Mi vecino es muy considerado y nunca pone la música alta.
     - frames: considerado con alguien
     - collocations: muy considerado; ser considerado con los demás
     - opposites: desconsiderado
- relations: derived_from considerar (VERB)

### 45. alternativa

**Lexeme 1: alternativa**, NOUN
- grammar: gender Fem; countability Count
- said: /al.teɾ.na.ˈti.ba/ (es-ES), /al.teɾ.na.ˈti.ba/ (es-MX)
  1. [B1] opción o posibilidad que se puede elegir en lugar de otra
     - example: La bicicleta es una buena alternativa al coche en la ciudad.
     - frames: alternativa a algo; alternativa para algo
     - collocations: no tener alternativa; no quedar otra alternativa; buscar una alternativa; ofrecer una alternativa; única alternativa; alternativa viable; como alternativa
  2. [C1] ceremonia en que un torero joven pasa a ser matador de toros
     - example: El joven torero tomará la alternativa el próximo domingo en la plaza.
     - collocations: tomar la alternativa; dar la alternativa
- relations: derived_from alternativo (ADJ)
