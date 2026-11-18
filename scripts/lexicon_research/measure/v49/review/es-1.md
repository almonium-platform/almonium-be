# Spanish: entries to check (1 of 1)

These are entries of a learner's dictionary of Spanish, written by a model. Do not correct them and do
not rank them. For each entry say which of the faults below it has, if any. Most entries of a good
dictionary have none: report a fault only when you could show it from a dictionary of Spanish.

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

### 1. rendir

**Lexeme 1: rendir**, VERB
- grammar: conjugation 3; irregular True; valency Both; note stem changes e to i, like pedir
- said: /ren.ˈdiɾ/ (es-ES), /ren.ˈdiɾ/ (es-MX)
- forms: rindo [Mood=Ind, Tense=Pres, Person=1, Number=Sing]; rindes [Mood=Ind, Tense=Pres, Person=2, Number=Sing]; rinde [Mood=Ind, Tense=Pres, Person=3, Number=Sing]; rendimos [Mood=Ind, Tense=Pres, Person=1, Number=Plur]; rinden [Mood=Ind, Tense=Pres, Person=3, Number=Plur]; rendí [Mood=Ind, Tense=Past, Person=1, Number=Sing]; rindió [Mood=Ind, Tense=Past, Person=3, Number=Sing]; rendiré [Mood=Ind, Tense=Fut, Person=1, Number=Sing]; rinda [Mood=Sub, Tense=Pres, Person=1, Number=Sing]; rendido [VerbForm=Part, Tense=Past]; rindiendo [VerbForm=Ger]
  1. [B1] dejar de luchar o de resistir y aceptar la derrota
     - example: No te rindas ahora, que ya casi has terminado.
     - frames: rendirse ante algo/alguien; rendirse a alguien
     - collocations: rendirse ante la evidencia; no rendirse nunca
     - grammar: valency Refl; reflexive True
  2. [B2] dar un buen resultado o provecho en el trabajo o en el uso
     - example: Cuando duermo poco, no rindo nada en la oficina.
     - collocations: rendir bien; rendir al máximo; rendir mucho; rendir poco
     - grammar: valency Intr
  3. [B2] dar u ofrecer a alguien respeto, honor o explicaciones
     - example: El pueblo entero quiso rendir homenaje a la vieja maestra.
     - frames: rendir algo a alguien
     - collocations: rendir homenaje; rendir cuentas; rendir culto; rendir tributo; rendir honores
     - grammar: valency Tr
  4. [B1] presentarse a una prueba para ser evaluado
     - example: Mañana tengo que rendir el examen final de matemáticas.
     - frames: rendir algo
     - collocations: rendir un examen; rendir una materia
     - regions: es-005 only
     - grammar: valency Tr
  5. [C1] cansar mucho a alguien, dejarlo sin fuerzas
     - example: Aquella caminata tan larga nos rindió a todos.
     - grammar: valency Tr

### 2. vaina

**Lexeme 1: vaina**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈbaj.na/ (es-ES), /ˈbaj.na/ (es-MX)
  1. [B2] funda ajustada donde se guarda la hoja de una espada o un cuchillo
     - example: El soldado sacó la espada de la vaina sin hacer ruido.
     - collocations: sacar de la vaina; meter en la vaina; vaina de la espada
  2. [B2] cáscara larga y blanda que envuelve las semillas de algunas plantas
     - example: Para preparar la sopa hay que abrir las vainas y sacar los guisantes.
     - collocations: vaina de guisantes; vaina de vainilla; judías en vaina
  3. [B2] cosa o asunto cuyo nombre no se dice o no se recuerda
     - example: Pásame esa vaina que está encima de la mesa.
     - labels: formality informal
     - regions: es-029 mainly, es-CO mainly
  4. [C1] situación molesta o problema que causa disgusto
     - example: Qué vaina, se me olvidaron las llaves en la oficina.
     - collocations: qué vaina
     - labels: formality informal
     - regions: es-029 mainly, es-CO mainly

### 3. carrera

**Lexeme 1: carrera**, NOUN
- grammar: gender Fem; countability Count
- said: /ka.ˈre.ra/ (es-ES), /ka.ˈre.ra/ (es-MX)
  1. [A2] competición en la que gana quien llega primero a una meta
     - example: Mi primo ganó la carrera de bicicletas del pueblo.
     - frames: carrera de algo
     - collocations: correr una carrera; ganar una carrera; carrera de relevos; carrera de obstáculos; carrera de coches; carrera espacial; carrera armamentística
  2. [A2] estudios universitarios que preparan para ejercer una profesión
     - example: Cuando termine la carrera, quiero trabajar en un hospital.
     - frames: carrera de algo
     - collocations: estudiar una carrera; hacer la carrera; terminar la carrera; sacarse la carrera; carrera universitaria
  3. [B1] trayectoria y conjunto de logros de una persona en su profesión
     - example: A lo largo de su carrera ha escrito más de veinte novelas.
     - collocations: hacer carrera; carrera profesional; carrera política; carrera militar; carrera diplomática; a lo largo de su carrera
  4. [B1] acción de correr durante un trecho, a menudo con prisa
     - example: Tuve que darme una carrera para no perder el autobús.
     - collocations: darse una carrera; echar una carrera; a la carrera; de una carrera
  5. [B2] línea de puntos deshechos en una media o un tejido de punto
     - example: Se me ha hecho una carrera en las medias nuevas.
     - collocations: hacerse una carrera
  6. [B2] trayecto que hace un taxi con un pasajero, y su precio
     - example: La carrera hasta el aeropuerto suele costar bastante dinero.
     - collocations: carrera de taxi; cobrar la carrera

### 4. demandar

**Lexeme 1: demandar**, VERB
- grammar: conjugation 1; irregular False; valency Tr
- said: /de.man.ˈdaɾ/ (es-ES), /de.man.ˈdaɾ/ (es-MX)
  1. [B2] acudir a un juez para acusar a alguien y reclamarle algo
     - example: Los vecinos demandaron a la empresa por la contaminación del río.
     - frames: demandar a alguien; demandar a alguien por algo
     - collocations: demandar por daños y perjuicios; demandar judicialmente; demandar a la empresa
     - labels: domain law
  2. [B2] pedir con insistencia algo que se considera necesario o justo
     - example: Los ciudadanos demandan más seguridad en las calles del barrio.
     - frames: demandar algo; demandar algo a alguien
     - collocations: demandar justicia; demandar mejoras; demandar una solución; demandar atención
     - labels: formality formal
  3. [C1] hacer falta algo para que una actividad salga bien
     - example: Este trabajo demanda mucha paciencia y bastante tiempo libre.
     - frames: demandar algo
     - collocations: demandar tiempo; demandar esfuerzo; demandar atención; demandar dedicación
     - labels: formality formal

### 5. anuncio

**Lexeme 1: anuncio**, NOUN
- grammar: gender Masc; countability Count
- said: /a.ˈnun.θjo/ (es-ES), /a.ˈnun.sjo/ (es-MX)
  1. [A2] mensaje o imagen que intenta convencer a la gente de comprar algo
     - example: Vi un anuncio de coches nuevos en la televisión.
     - frames: anuncio de algo
     - collocations: poner un anuncio; anuncio publicitario; anuncio por palabras; anuncios clasificados; anuncio de televisión; anuncio luminoso
  2. [B1] noticia que se da a conocer de forma pública u oficial
     - example: El gobierno hizo ayer el anuncio de las nuevas medidas.
     - frames: anuncio de algo
     - collocations: hacer un anuncio; anuncio oficial
- relations: derived_from anunciar (VERB)

### 6. carta

**Lexeme 1: carta**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈkaɾ.ta/ (es-ES), /ˈkaɾ.ta/ (es-MX)
  1. [A1] papel escrito que se envía a una persona para comunicarle algo
     - example: Todavía guardo las cartas que me escribía mi abuela.
     - frames: carta a alguien; carta de alguien
     - collocations: escribir una carta; mandar una carta; enviar una carta; recibir una carta; echar una carta; carta certificada; carta de amor; carta de presentación; por carta
  2. [A2] cada una de las piezas de cartulina de una baraja que sirven para jugar
     - example: Esta noche jugamos a las cartas en casa de mis tíos.
     - collocations: jugar a las cartas; barajar las cartas; repartir las cartas; echar las cartas; poner las cartas sobre la mesa; tomar cartas en el asunto
  3. [A2] lista de las comidas y bebidas que ofrece un restaurante, con sus precios
     - example: Camarero, ¿nos puede traer la carta, por favor?
     - collocations: pedir la carta; a la carta; carta de vinos; carta de postres
  4. [C1] documento oficial donde se fijan los principios o derechos de una institución o comunidad
     - example: La nueva carta establece los derechos fundamentales de los ciudadanos.
     - collocations: carta magna; carta de naturaleza; carta de ciudadanía
     - labels: style official

### 7. imperio

**Lexeme 1: imperio**, NOUN
- grammar: gender Masc
- said: /im.ˈpe.ɾjo/ (es-ES), /im.ˈpe.ɾjo/ (es-MX)
  1. [B1] conjunto de pueblos y territorios gobernados por un mismo soberano o estado poderoso
     - example: Los romanos construyeron un imperio que duró varios siglos.
     - collocations: el Imperio romano; la caída del imperio; fundar un imperio; imperio colonial; expandir el imperio
     - grammar: countability Count
  2. [B2] grupo de empresas muy grande y poderoso que pertenece a una misma persona o familia
     - example: Su abuelo levantó un imperio textil a partir de un pequeño taller.
     - collocations: levantar un imperio; construir un imperio; imperio mediático; imperio económico
     - labels: figurative True
     - grammar: countability Count
  3. [C1] poder o dominio total que algo o alguien ejerce sobre otros
     - example: En una democracia debe prevalecer siempre el imperio de la ley.
     - collocations: el imperio de la ley
     - labels: style elevated
     - grammar: countability Mass

### 8. demás

**Lexeme 1: demás**, DET
- grammar: invariable True; position Attr; note same form for all genders and numbers; usually after the article: las demás casas
- said: /de.ˈmas/ (es-ES), /de.ˈmas/ (es-MX)
  1. [A2] que forman el resto de las personas o cosas de un grupo
     - example: Las demás alumnas ya se han ido a casa.
     - collocations: los demás días; las demás personas; y demás
**Lexeme 2: demás**, PRON
- grammar: invariable True; note used with an article: los demás, las demás, lo demás
- said: /de.ˈmas/ (es-ES), /de.ˈmas/ (es-MX)
  1. [A2] las otras personas, el resto de la gente
     - example: Hay que respetar a los demás aunque piensen diferente.
     - collocations: los demás; pensar en los demás; respetar a los demás; ayudar a los demás; delante de los demás
  2. [B1] el resto de las cosas, todo lo que queda
     - example: Yo traigo el pan y tú te encargas de lo demás.
     - collocations: lo demás; todo lo demás; por lo demás; y demás

### 9. emblema

**Lexeme 1: emblema**, NOUN
- grammar: gender Masc; countability Count
- said: /em.ˈble.ma/ (es-ES), /em.ˈble.ma/ (es-MX)
  1. [B2] figura o dibujo que representa a un grupo, país o empresa
     - example: Los jugadores llevan el emblema del club en la camiseta.
     - frames: emblema de algo/alguien
     - collocations: emblema nacional; llevar el emblema; el emblema del club; emblema de la empresa
  2. [C1] persona o cosa que representa una idea o una cualidad
     - example: La paloma blanca es el emblema de la paz.
     - frames: emblema de algo
     - collocations: convertirse en emblema; emblema de la paz

### 10. inmigrante

**Lexeme 1: inmigrante**, NOUN
- grammar: gender Com; countability Count
- said: /in.mi.ˈɡɾan.te/ (es-ES), /in.mi.ˈɡɾan.te/ (es-MX)
  1. [B1] persona que llega a vivir a un país que no es el suyo
     - example: Muchos inmigrantes trabajan en el campo durante el verano.
     - collocations: inmigrante ilegal; inmigrante indocumentado; inmigrante sin papeles; inmigrante de segunda generación; integración de los inmigrantes; acoger a los inmigrantes
- relations: agent_noun_of inmigrar (VERB); confusable emigrante (NOUN): emigrante is the one who leaves a country, inmigrante the one who arrives
**Lexeme 2: inmigrante**, ADJ
- grammar: comparable False
- said: /in.mi.ˈɡɾan.te/ (es-ES), /in.mi.ˈɡɾan.te/ (es-MX)
  1. [B2] que ha llegado de otro país para quedarse a vivir
     - example: La población inmigrante ha crecido mucho en esta ciudad.
     - collocations: población inmigrante; mano de obra inmigrante; familia inmigrante; trabajador inmigrante; comunidad inmigrante
- relations: derived_from inmigrar (VERB); confusable emigrante (ADJ): emigrante refers to leaving a country, inmigrante to arriving

### 11. inercia

**Lexeme 1: inercia**, NOUN
- grammar: gender Fem; countability Mass
- said: /i.ˈneɾ.θja/ (es-ES), /i.ˈneɾ.sja/ (es-MX)
  1. [B2] tendencia a seguir actuando igual por costumbre, sin esfuerzo ni cambio
     - example: Muchos siguen en ese trabajo por pura inercia, sin pensar.
     - collocations: por inercia; por pura inercia; romper la inercia; vencer la inercia
  2. [B2] propiedad de un cuerpo de mantenerse quieto o en movimiento sin fuerza externa
     - example: Al frenar el autobús, la inercia nos empujó hacia delante.
     - collocations: fuerza de inercia; momento de inercia; principio de inercia
     - labels: domain physics

### 12. lentamente

**Lexeme 1: lentamente**, ADV
- grammar: invariable True
- said: /ˌlen.ta.ˈmen.te/ (es-ES), /ˌlen.ta.ˈmen.te/ (es-MX)
  1. [A2] a poca velocidad, empleando mucho tiempo en hacer algo
     - example: El anciano subió lentamente las escaleras de la casa.
     - collocations: avanzar lentamente; caminar lentamente; moverse lentamente; recuperarse lentamente; crecer lentamente
     - opposites: rápidamente
- relations: adverb_of lento (ADJ)

### 13. joder

**Lexeme 1: joder**, VERB
- grammar: conjugation 2; irregular False; valency Both
- said: /xo.ˈdeɾ/ (es-ES), /xo.ˈdeɾ/ (es-MX)
  1. [B2] causar molestias o enfado a alguien de forma insistente
     - example: Deja de joder a tu hermano y ponte a estudiar.
     - frames: joder a alguien
     - collocations: no me jodas; dejar de joder
     - labels: attitude vulgar
     - grammar: valency Both
  2. [B2] estropear algo o causarle un daño grave
     - example: Con ese golpe has jodido la pantalla del móvil.
     - frames: joder algo
     - labels: attitude vulgar
     - grammar: valency Tr; note joderse: to break down, of a thing (se ha jodido la lavadora)
  3. [C1] sufrir un daño o tener que aguantar algo desagradable
     - example: Si no te gusta la comida, te jodes y te la comes.
     - collocations: que se joda
     - labels: attitude vulgar
     - grammar: valency Refl; reflexive True
  4. [C1] tener relaciones sexuales con otra persona
     - example: Dicen que aquellos dos jodían a escondidas en el almacén.
     - frames: joder con alguien; joder a alguien
     - labels: attitude vulgar
     - grammar: valency Both
**Lexeme 2: joder**, INTJ
- grammar: invariable True
- said: /xo.ˈdeɾ/ (es-ES), /xo.ˈdeɾ/ (es-MX)
  1. [B2] se usa para expresar enfado, sorpresa, dolor o admiración
     - example: ¡Joder, qué frío hace hoy en la calle!
     - labels: attitude vulgar
     - regions: es-ES mainly

### 14. contribución

**Lexeme 1: contribución**, NOUN
- grammar: gender Fem; countability Count
- said: /kon.tɾi.bu.ˈθjon/ (es-ES), /kon.tɾi.bu.ˈsjon/ (es-MX)
  1. [B1] lo que alguien da o hace para ayudar a conseguir un fin común
     - example: Su contribución al proyecto ha sido fundamental para terminarlo a tiempo.
     - frames: contribución a algo; contribución de alguien
     - collocations: hacer una contribución; contribución económica; contribución voluntaria; contribución importante; gran contribución; valiosa contribución
  2. [C1] cantidad de dinero que se paga al Estado o a un municipio como impuesto
     - example: Todos los propietarios deben pagar las contribuciones antes de fin de año.
     - collocations: pagar contribuciones; contribución urbana
     - grammar: note often in the plural
- relations: verbal_noun_of contribuir (VERB)

### 15. cierre

**Lexeme 1: cierre**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈθje.re/ (es-ES), /ˈsje.re/ (es-MX)
  1. [B1] final de la actividad de un negocio, o corte del paso por un lugar
     - example: El cierre de la fábrica dejó a cien personas sin trabajo.
     - frames: cierre de algo
     - collocations: cierre definitivo; cierre temporal; anunciar el cierre; horario de cierre; cierre de fronteras; cierre patronal
  2. [B2] parte final de un acto, un programa o un periodo de tiempo
     - example: El cantante actuó en el cierre del festival de verano.
     - frames: cierre de algo
     - collocations: ceremonia de cierre; acto de cierre; cierre de campaña; al cierre de
  3. [B1] tira con dos filas de dientes que une las partes de una prenda
     - example: Se me rompió el cierre de la chaqueta esta mañana.
     - collocations: subir el cierre; bajar el cierre; cierre relámpago
     - regions: es-419 mainly
- relations: verbal_noun_of cerrar (VERB)

### 16. saga

**Lexeme 1: saga**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈsa.ɡa/ (es-ES), /ˈsa.ɡa/ (es-MX)
  1. [B1] serie de novelas, películas o juegos que continúan una misma historia
     - example: Estoy leyendo el tercer libro de una saga de ciencia ficción.
     - collocations: saga de películas; saga literaria; saga fantástica; última entrega de la saga
  2. [B2] familia cuyos miembros se dedican a lo mismo durante varias generaciones
     - example: Pertenece a una larga saga de médicos rurales.
     - frames: saga de algo
     - collocations: saga familiar; una larga saga
  3. [B2] novela larga que cuenta la vida de varias generaciones de una familia
     - example: Su última novela es una saga que recorre todo el siglo veinte.
     - labels: domain literature
  4. [C1] relato tradicional de la Edad Media escandinava sobre héroes y dioses
     - example: Las antiguas sagas islandesas narran viajes y batallas de los vikingos.
     - collocations: saga islandesa; saga nórdica
     - labels: domain literature

### 17. revelación

**Lexeme 1: revelación**, NOUN
- grammar: gender Fem; countability Count
- said: /re.be.la.ˈθjon/ (es-ES), /re.be.la.ˈsjon/ (es-MX)
  1. [B2] comunicación de algo que estaba oculto o no se sabía, y lo que se descubre
     - example: Sus revelaciones sobre la empresa causaron un gran escándalo.
     - frames: revelación de algo
     - collocations: hacer una revelación; revelación de secretos; una revelación sorprendente
  2. [B2] persona o cosa que de pronto muestra un talento o valor inesperado
     - example: Esa joven cantante ha sido la revelación de la temporada.
     - collocations: la revelación del año; ser una revelación; actor revelación
  3. [C1] manifestación de una verdad oculta que Dios hace a las personas
     - example: Muchas religiones se basan en una revelación divina.
     - collocations: revelación divina
     - labels: domain religion
- relations: verbal_noun_of revelar (VERB)

### 18. pro

**Lexeme 1: pro**, NOUN
- grammar: gender Masc; countability Count; number Plur, mainly; note plural pros; usually in the plural, paired with contras
- said: /ˈpɾo/ (es-ES), /ˈpɾo/ (es-MX)
  1. [B2] aspecto favorable o ventaja de algo que se valora
     - example: Antes de cambiar de trabajo, conviene pensar bien los pros.
     - collocations: los pros y los contras; sopesar los pros y los contras; en pro de
     - opposites: contra
**Lexeme 2: pro**, ADP
- grammar: invariable True; note used before a noun without an article
- said: /ˈpɾo/ (es-ES), /ˈpɾo/ (es-MX)
  1. [C1] se usa para indicar que se apoya o se defiende algo
     - example: Varias asociaciones pro derechos humanos firmaron la carta abierta.
     - frames: pro + sustantivo
     - collocations: pro derechos humanos; asociación pro

### 19. super

Not written as an entry: misspelling of súper (as a word it needs the accent; super- alone is a prefix written joined to the next word)

### 20. cuenca

**Lexeme 1: cuenca**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈkwen.ka/ (es-ES), /ˈkwen.ka/ (es-MX)
  1. [B2] territorio cuyas aguas van a parar a un mismo río, lago o mar
     - example: La cuenca de este río abarca tres provincias del norte.
     - collocations: cuenca hidrográfica; cuenca fluvial; cuenca del río; cuenca mediterránea
  2. [B2] zona extensa de terreno con yacimientos de carbón u otro mineral
     - example: Muchas familias se marcharon de la cuenca minera cuando cerraron los pozos.
     - collocations: cuenca minera; cuenca hullera; cuenca carbonífera; cuenca petrolífera
  3. [C1] hueco del cráneo donde se encuentra cada uno de los ojos
     - example: Después de tantas noches sin dormir, tenía los ojos hundidos en las cuencas.
     - collocations: cuenca del ojo; ojos hundidos en las cuencas

### 21. dormir

**Lexeme 1: dormir**, VERB
- grammar: conjugation 3; irregular True; valency Both; note stem changes o > ue when stressed and o > u in some forms (durmió, durmiendo)
- said: /doɾ.ˈmiɾ/ (es-ES), /doɾ.ˈmiɾ/ (es-MX)
- forms: duermo [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; duermes [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin]; duerme [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin]; dormimos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin]; duermen [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin]; dormí [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin]; durmió [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin]; dormiré [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin]; duerma [Mood=Sub, Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin]; dormido [VerbForm=Part, Tense=Past]; durmiendo [VerbForm=Ger]
  1. [A1] estar en reposo con los ojos cerrados y sin conciencia, para descansar
     - example: Esta noche he dormido muy mal por culpa del calor.
     - collocations: dormir bien; dormir mal; dormir la siesta; dormir profundamente; dormir a pierna suelta; dormir como un tronco; dormir poco
     - grammar: valency Intr
  2. [A2] pasar la noche en un lugar
     - example: Si perdemos el último tren, dormiremos en un hotel cerca de la estación.
     - frames: dormir en + lugar
     - collocations: dormir fuera; dormir en casa
     - grammar: valency Intr
  3. [B1] hacer que una persona o un animal empiece a descansar sin conciencia
     - example: Le cuesta mucho dormir al bebé cuando tiene hambre.
     - frames: dormir a alguien
     - collocations: dormir a un niño
     - grammar: valency Tr
  4. [A2] empezar a descansar con los ojos cerrados y sin conciencia
     - example: Me dormí en el sofá mientras veía la película.
     - collocations: dormirse en clase; quedarse dormido
     - grammar: valency Refl
     - opposites: despertar
  5. [B2] perder una parte del cuerpo la sensibilidad durante un rato
     - example: Después de estar tanto rato sentada, se me durmió la pierna.
     - frames: dormírsele algo a alguien
     - grammar: valency Refl; note usually with an indirect object pronoun for the person: se me durmió el brazo

### 22. delito

**Lexeme 1: delito**, NOUN
- grammar: gender Masc; countability Count
- said: /de.ˈli.to/ (es-ES), /de.ˈli.to/ (es-MX)
  1. [B1] acción u omisión que va contra la ley y se castiga con una pena
     - example: Conducir sin carné se considera un delito en muchos países.
     - frames: delito de algo; delito contra algo/alguien
     - collocations: cometer un delito; delito grave; delito leve; acusar de un delito; delito fiscal; delito informático; delito de odio; cuerpo del delito; en flagrante delito
  2. [B2] cosa que se considera muy mal hecha, injusta o lamentable
     - example: Tirar tanta comida a la basura es un delito.
     - frames: es un delito + inf; es un delito que + subj
     - labels: figurative True, formality informal

### 23. cómo

**Lexeme 1: cómo**, ADV
- grammar: invariable True; note interrogative and exclamative; used in direct and indirect questions
- said: /ˈko.mo/ (es-ES), /ˈko.mo/ (es-MX)
  1. [A1] de qué modo, de qué manera o en qué estado
     - example: No sé cómo llegar a la estación desde aquí.
     - frames: cómo + inf
     - collocations: ¿cómo estás?; ¿cómo te llamas?; ¿cómo se dice?; ¿cómo es?; ¿a cómo está?
  2. [B1] por qué motivo, expresando sorpresa, extrañeza o enfado
     - example: ¿Cómo no me avisaste de que llegabas tarde?
     - frames: cómo es que + ind; cómo que + ind
     - collocations: ¡cómo no!
  3. [B1] cuánto, en qué gran medida o con qué intensidad
     - example: ¡Cómo ha crecido tu hijo desde el verano pasado!
- relations: confusable como (ADV): same sound; como is written without an accent when it is not interrogative or exclamative
**Lexeme 2: cómo**, INTJ
- grammar: invariable True
- said: /ˈko.mo/ (es-ES), /ˈko.mo/ (es-MX)
  1. [A2] se usa para pedir que se repita algo que no se ha oído o entendido
     - example: Perdona, ¿cómo?, no te he oído bien con tanto ruido.
  2. [B1] se usa para expresar sorpresa, extrañeza o enfado ante lo que se oye
     - example: ¡Cómo!, ¿ya has terminado todo el trabajo tú solo?
**Lexeme 3: cómo**, NOUN
- grammar: gender Masc; countability Count; number Sing, mainly; note usually with the article: el cómo
- said: /ˈko.mo/ (es-ES), /ˈko.mo/ (es-MX)
  1. [C1] manera o modo en que se hace o sucede algo
     - example: Nos explicó con detalle el cómo y el porqué de su decisión.
     - collocations: el cómo y el porqué; el qué y el cómo

### 24. masaje

**Lexeme 1: masaje**, NOUN
- grammar: gender Masc; countability Count
- said: /ma.ˈsa.xe/ (es-ES), /ma.ˈsa.xe/ (es-MX)
  1. [B1] presión y frotamiento rítmicos sobre el cuerpo para relajar los músculos o curar
     - example: Después de correr, un buen masaje en las piernas me ayuda mucho.
     - frames: masaje en algo; dar un masaje a alguien
     - collocations: dar un masaje; hacer un masaje; recibir un masaje; masaje relajante; masaje terapéutico; masaje cardíaco; masaje deportivo; sala de masajes

### 25. prácticamente

**Lexeme 1: prácticamente**, ADV
- grammar: invariable True
- said: /ˌpɾak.ti.ka.ˈmen.te/ (es-ES), /ˌpɾak.ti.ka.ˈmen.te/ (es-MX)
  1. [B1] casi del todo, faltando muy poco para que algo sea completo
     - example: Ya tenemos la casa prácticamente lista para la fiesta.
     - collocations: prácticamente nada; prácticamente todo; prácticamente nadie; prácticamente imposible; prácticamente igual; prácticamente terminado
  2. [C1] de una manera real y útil, no solo en teoría
     - example: Aprendió el oficio prácticamente, trabajando cada día en el taller.
- relations: adverb_of práctico (ADJ)

### 26. tontería

**Lexeme 1: tontería**, NOUN
- grammar: gender Fem
- said: /ton.te.ˈɾi.a/ (es-ES), /ton.te.ˈɾi.a/ (es-MX)
  1. [B1] cosa que se dice o se hace sin sentido ni buen juicio
     - example: Deja de decir tonterías y ayúdame a preparar la cena.
     - collocations: decir tonterías; hacer una tontería; cometer una tontería; dejarse de tonterías; ¡qué tontería!
     - grammar: countability Count
  2. [B1] cosa de muy poco valor o importancia
     - example: Discutieron muy en serio por una tontería sin importancia.
     - collocations: por una tontería
     - grammar: countability Count
  3. [B2] falta de inteligencia o de sensatez en una persona
     - example: Su tontería a veces nos hace reír a todos.
     - grammar: countability Mass
- relations: abstract_noun_of tonto (ADJ)

### 27. abusar

**Lexeme 1: abusar**, VERB
- grammar: conjugation 1; irregular False; valency Intr; note usually with the preposition de
- said: /a.bu.ˈsaɾ/ (es-ES), /a.bu.ˈsaɾ/ (es-MX)
  1. [B1] usar algo demasiado o de una manera mala o incorrecta
     - example: Beber demasiado alcohol cada fin de semana es abusar de él.
     - frames: abusar de algo
     - collocations: abusar del alcohol; abusar del poder; abusar de los medicamentos; abusar de la sal
  2. [B2] aprovecharse de una persona, de su confianza o de su bondad
     - example: Mi vecino siempre abusa de mi paciencia y nunca me ayuda.
     - frames: abusar de alguien; abusar de algo
     - collocations: abusar de la confianza; abusar de la paciencia; abusar de la bondad; abusar de la hospitalidad
  3. [B2] obligar a una persona a tener contacto sexual contra su voluntad
     - example: El hombre fue detenido por abusar de una menor de edad.
     - frames: abusar de alguien
     - collocations: abusar sexualmente; abusar de un menor
- relations: derived_from abuso (NOUN)

### 28. representación

**Lexeme 1: representación**, NOUN
- grammar: gender Fem
- said: /re.pre.sen.ta.ˈθjon/ (es-ES), /re.pre.sen.ta.ˈsjon/ (es-MX)
  1. [B2] papel de quien actúa o habla en nombre de otra persona o grupo
     - example: Asistió a la reunión en representación de toda la empresa.
     - frames: representación de alguien/algo
     - collocations: en representación de; ostentar la representación; tener la representación; representación legal
     - grammar: countability Mass
  2. [B1] obra de teatro u otro espectáculo que se presenta ante el público
     - example: La representación de la obra duró casi tres horas.
     - frames: representación de algo
     - collocations: representación teatral; asistir a una representación; dar una representación
     - grammar: countability Count
  3. [B2] imagen, figura o idea que muestra o sustituye a otra cosa
     - example: El cuadro es una representación del paisaje del pueblo.
     - frames: representación de algo
     - collocations: representación gráfica; representación simbólica; representación fiel
     - grammar: countability Count
  4. [C1] grupo de personas que actúa en nombre de un país u organización
     - example: La representación española llegó ayer a la cumbre internacional.
     - frames: representación de algo
     - collocations: representación diplomática; representación permanente
     - grammar: countability Count
- relations: verbal_noun_of representar (VERB)

### 29. hábil

**Lexeme 1: hábil**, ADJ
- grammar: note same form for masculine and feminine; plural hábiles
- said: /ˈa.bil/ (es-ES), /ˈa.bil/ (es-MX)
  1. [B1] que hace las cosas bien y con facilidad, destreza o inteligencia
     - example: Mi abuelo es muy hábil con las manos y arregla todo.
     - frames: hábil con algo; hábil para algo; hábil en algo; hábil para + inf
     - collocations: hábil con las manos; hábil negociador; hábil maniobra; hábil jugador
     - grammar: comparable True
     - opposites: torpe
  2. [B2] dicho de un día o una hora, en que se trabaja y funcionan las oficinas
     - example: Tiene un plazo de diez días hábiles para presentar la solicitud.
     - collocations: día hábil; días hábiles; horas hábiles
     - labels: style official
     - grammar: position Attr; comparable False; note usually after día or hora
     - opposites: inhábil

### 30. consentimiento

**Lexeme 1: consentimiento**, NOUN
- grammar: gender Masc; countability Mass, mainly
- said: /kon.sen.ti.ˈmjen.to/ (es-ES), /kon.sen.ti.ˈmjen.to/ (es-MX)
  1. [B2] permiso o acuerdo que alguien da para que se haga algo
     - example: Ningún paciente puede ser operado sin su consentimiento por escrito.
     - frames: consentimiento de alguien; consentimiento para algo; consentimiento para + inf
     - collocations: dar su consentimiento; otorgar el consentimiento; sin consentimiento; con el consentimiento de; consentimiento informado; consentimiento por escrito; consentimiento expreso; consentimiento paterno
- relations: verbal_noun_of consentir (VERB)

### 31. jamás

**Lexeme 1: jamás**, ADV
- grammar: invariable True; note before the verb, or after it with no: jamás miente, no miente jamás
- said: /xa.ˈmas/ (es-ES), /xa.ˈmas/ (es-MX)
  1. [B1] en ningún momento, ni una sola vez
     - example: Jamás olvidaré el día en que nos conocimos.
     - collocations: nunca jamás; jamás en la vida; jamás de los jamases; por siempre jamás
     - opposites: siempre

### 32. bruto

**Lexeme 1: bruto**, ADJ
- grammar: note feminine bruta; plural brutos, brutas
- said: /ˈbɾu.to/ (es-ES), /ˈbɾu.to/ (es-MX)
  1. [B1] que actúa con demasiada fuerza o sin cuidado ni delicadeza
     - example: No seas tan bruto con el perro, que le haces daño.
     - frames: bruto con algo/alguien
     - collocations: fuerza bruta; a lo bruto
     - labels: formality informal
     - grammar: comparable True
     - opposites: delicado
  2. [B1] poco inteligente o que entiende las cosas con mucha dificultad
     - example: Mira que eres bruto, te lo he explicado tres veces.
     - collocations: más bruto que un arado
     - labels: formality informal
     - grammar: comparable True
  3. [B2] dicho de una cantidad, sin descontar impuestos, gastos o el peso del envase
     - example: Mi sueldo bruto es bastante más alto que lo que cobro.
     - collocations: sueldo bruto; peso bruto; producto interior bruto; salario bruto; beneficio bruto; ingresos brutos
     - grammar: comparable False
     - opposites: neto
  4. [B2] en su estado natural, todavía sin pulir ni elaborar
     - example: El país exporta sobre todo petróleo bruto y algunos minerales.
     - collocations: en bruto; petróleo bruto; diamante en bruto
     - grammar: comparable False
**Lexeme 2: bruto**, NOUN
- grammar: gender Masc; countability Count; note feminine bruta for a woman
- said: /ˈbɾu.to/ (es-ES), /ˈbɾu.to/ (es-MX)
- forms: bruta [Gender=Fem, Number=Sing]
  1. [B1] persona que actúa con violencia o sin ninguna delicadeza
     - example: Ese bruto empujó a todo el mundo para subir al autobús.
     - collocations: hecho un bruto; como un bruto
     - labels: formality informal, attitude derogatory
  2. [B2] persona ignorante o que entiende las cosas con mucha dificultad
     - example: Cualquier bruto sabe que eso no se puede hacer así.
     - labels: formality informal, attitude derogatory

### 33. salvar

**Lexeme 1: salvar**, VERB
- grammar: conjugation 1; irregular False; valency Tr
- said: /sal.ˈbaɾ/ (es-ES), /sal.ˈbaɾ/ (es-MX)
  1. [A2] librar a una persona o cosa de un peligro, un daño o la destrucción
     - example: Los bomberos consiguieron salvar a tres niños del incendio.
     - frames: salvar a alguien de algo; salvar algo de algo; salvarse de algo
     - collocations: salvar la vida; salvar vidas; salvarse por los pelos; salvar el pellejo; salvar la situación
     - grammar: valency Tr; note also reflexive salvarse, to escape a danger oneself
  2. [B2] superar un obstáculo o una dificultad, o recorrer una distancia
     - example: Este puente salva el río con un solo arco de piedra.
     - collocations: salvar un obstáculo; salvar la distancia; salvar un desnivel; salvando las distancias
     - grammar: valency Tr
  3. [B1] guardar un archivo o unos datos en la memoria de una computadora
     - example: No olvides salvar el documento antes de apagar la computadora.
     - collocations: salvar los cambios; salvar un archivo
     - regions: es-419 mainly
     - grammar: valency Tr
  4. [C1] librar a alguien del pecado y darle la vida eterna
     - example: Según los creyentes, la fe puede salvar el alma de cualquier pecador.
     - collocations: salvar el alma
     - labels: domain religion
     - grammar: valency Tr
     - opposites: condenar

### 34. estrés

**Lexeme 1: estrés**, NOUN
- grammar: gender Masc; countability Mass, mainly
- said: /es.ˈtɾes/ (es-ES), /es.ˈtɾes/ (es-MX)
  1. [B1] tensión física y nerviosa causada por exceso de trabajo, problemas o presión
     - example: Últimamente tengo mucho estrés por culpa de los exámenes finales.
     - frames: estrés por algo
     - collocations: estrés laboral; sufrir estrés; tener estrés; reducir el estrés; combatir el estrés; nivel de estrés; estrés postraumático; situación de estrés

### 35. paloma

**Lexeme 1: paloma**, NOUN
- grammar: gender Fem; countability Count
- said: /pa.ˈlo.ma/ (es-ES), /pa.ˈlo.ma/ (es-MX)
  1. [A2] ave de cabeza pequeña, pico corto y plumas grises o blancas, frecuente en ciudades
     - example: Cada mañana mi abuelo da de comer a las palomas del parque.
     - collocations: paloma mensajera; paloma de la paz; paloma blanca; dar de comer a las palomas
  2. [C1] persona que defiende el diálogo y las soluciones pacíficas en un conflicto
     - example: Dentro del gobierno, las palomas prefieren seguir negociando con el enemigo.
     - labels: domain politics, figurative True
     - grammar: note usually in the plural, contrasted with halcones
     - opposites: halcón

### 36. municipalidad

**Lexeme 1: municipalidad**, NOUN
- grammar: gender Fem; countability Count
- said: /mu.ni.θi.pa.li.ˈdad/ (es-ES), /mu.ni.si.pa.li.ˈdad/ (es-MX)
  1. [B2] órgano formado por el alcalde y los concejales que gobierna una ciudad o pueblo, y su edificio
     - example: Mañana tengo que ir a la municipalidad para pagar un impuesto.
     - collocations: alcalde de la municipalidad; trabajar en la municipalidad
     - regions: es-419 mainly
  2. [C1] territorio que gobierna un mismo ayuntamiento
     - example: En esta municipalidad viven unas veinte mil personas.
     - regions: es-419 mainly
- relations: derived_from municipal (ADJ)

### 37. permanencia

**Lexeme 1: permanencia**, NOUN
- grammar: gender Fem; countability Both
- said: /peɾ.ma.ˈnen.θja/ (es-ES), /peɾ.ma.ˈnen.sja/ (es-MX)
  1. [B2] tiempo que una persona pasa en un lugar antes de irse
     - example: Durante su permanencia en el hospital recibió muchas visitas.
     - frames: permanencia en algo
     - collocations: durante su permanencia; tiempo de permanencia; permanencia prolongada; larga permanencia
     - grammar: countability Count
  2. [B2] hecho de seguir en un puesto, un grupo o una categoría
     - example: El equipo luchará hasta el final por la permanencia en la categoría.
     - frames: permanencia en algo
     - collocations: luchar por la permanencia; lograr la permanencia; salvar la permanencia; conseguir la permanencia
     - grammar: countability Mass
  3. [C1] cualidad de lo que dura mucho tiempo sin cambiar ni desaparecer
     - example: Nada en la vida tiene garantizada la permanencia.
     - grammar: countability Mass
     - opposites: transitoriedad
- relations: abstract_noun_of permanente (ADJ); confusable pertenencia (NOUN): similar sound; pertenencia means belonging or possession

### 38. judío

**Lexeme 1: judío**, ADJ
- grammar: note feminine judía; plural judíos, judías
- said: /xu.ˈdi.o/ (es-ES), /xu.ˈdi.o/ (es-MX)
  1. [B1] del pueblo hebreo o de la religión que sigue la ley de Moisés
     - example: En el centro de la ciudad todavía se conserva el antiguo barrio judío.
     - collocations: comunidad judía; pueblo judío; barrio judío; religión judía; cementerio judío
**Lexeme 2: judío**, NOUN
- grammar: gender Masc; countability Count; note feminine judía
- said: /xu.ˈdi.o/ (es-ES), /xu.ˈdi.o/ (es-MX)
  1. [B1] persona que pertenece al pueblo hebreo o practica la religión de Moisés
     - example: Muchos judíos celebran la Pascua con una cena familiar.
     - collocations: judío ortodoxo; judío sefardí; judío converso

### 39. señal

**Lexeme 1: señal**, NOUN
- grammar: gender Fem; countability Count
- said: /se.ˈɲal/ (es-ES), /se.ˈɲal/ (es-MX)
  1. [B1] hecho o cosa que permite saber o suponer otra cosa
     - example: Que el bebé tenga tanta hambre es buena señal.
     - frames: señal de algo; señal de que + ind
     - collocations: buena señal; mala señal; en señal de; dar señales de vida; ser señal de
  2. [B1] gesto o movimiento que se hace para comunicar algo sin palabras
     - example: El camarero me hizo una señal para que me acercara.
     - frames: hacer una señal a alguien; señal para que + subj
     - collocations: hacer una señal; hacer señales; señal con la mano; dar la señal; a mi señal
  3. [B1] onda o impulso eléctrico que transmite sonidos, imágenes o datos
     - example: Aquí arriba no hay señal y no puedo llamar a nadie.
     - collocations: no hay señal; tener señal; perder la señal; señal de televisión; señal débil; recibir la señal
  4. [A2] placa con dibujos o palabras que informa o da órdenes a quien circula
     - example: Esa señal indica que está prohibido aparcar en esta calle.
     - collocations: señal de tráfico; señal de stop; respetar las señales; señal de prohibido
  5. [B2] marca que se pone en una cosa o que queda en ella
     - example: Pon una señal en la página donde te has quedado.
     - collocations: poner una señal; dejar una señal
  6. [B2] parte del precio que se paga por adelantado para asegurar una compra
     - example: Dejamos cien euros de señal para reservar el apartamento.
     - collocations: dejar una señal; pagar una señal
     - regions: es-ES mainly

### 40. abuso

**Lexeme 1: abuso**, NOUN
- grammar: gender Masc; countability Both
- said: /a.ˈbu.so/ (es-ES), /a.ˈbu.so/ (es-MX)
  1. [B1] uso excesivo o incorrecto de algo, o de un poder o derecho
     - example: El abuso del alcohol causa graves problemas de salud.
     - frames: abuso de algo
     - collocations: abuso de poder; abuso de autoridad; abuso de confianza; abuso de alcohol; abuso de drogas; cometer un abuso
  2. [B2] trato cruel o violento a una persona, especialmente de tipo sexual
     - example: La víctima denunció los abusos que sufrió durante años.
     - frames: abuso contra alguien; abuso a alguien
     - collocations: abuso sexual; abuso infantil; abusos a menores; sufrir abusos; denunciar abusos; víctima de abusos
     - grammar: countability Count
  3. [B2] cosa injusta o exagerada que alguien hace o exige
     - example: Cobrar diez euros por un café es un abuso.
     - collocations: ser un abuso; qué abuso
     - grammar: countability Count
- relations: verbal_noun_of abusar (VERB)

### 41. cortina

**Lexeme 1: cortina**, NOUN
- grammar: gender Fem; countability Count
- said: /koɾ.ˈti.na/ (es-ES), /koɾ.ˈti.na/ (es-MX)
  1. [A2] tela que cuelga delante de una ventana o un hueco para tapar la luz o la vista
     - example: Por favor, cierra las cortinas, que entra demasiado sol.
     - collocations: correr la cortina; descorrer la cortina; abrir las cortinas; cerrar las cortinas; cortina de la ducha; cortina de baño
  2. [B2] cosa que se extiende delante de algo y lo oculta o impide verlo
     - example: Una espesa cortina de lluvia nos impedía ver la carretera.
     - frames: cortina de algo
     - collocations: cortina de humo; cortina de agua; cortina de lluvia; cortina de niebla
     - labels: figurative True

### 42. miel

**Lexeme 1: miel**, NOUN
- grammar: gender Fem; note plural mieles
- said: /ˈmjel/ (es-ES), /ˈmjel/ (es-MX)
  1. [A2] sustancia espesa, dulce y amarilla que hacen las abejas con el néctar de las flores
     - example: Por las mañanas tomo un yogur con un poco de miel.
     - collocations: miel de abeja; miel de flores; una cucharada de miel; panal de miel; luna de miel; dulce como la miel; miel sobre hojuelas
     - grammar: countability Mass, mainly
  2. [C1] placer o satisfacción que produce algo bueno que se consigue
     - example: Después de tantos años, el equipo por fin probó las mieles del triunfo.
     - collocations: las mieles del éxito; las mieles del triunfo; probar las mieles
     - labels: figurative True, style literary
     - grammar: number Plur, mainly; countability Count

### 43. posición

**Lexeme 1: posición**, NOUN
- grammar: gender Fem; countability Count
- said: /po.si.ˈθjon/ (es-ES), /po.si.ˈsjon/ (es-MX)
  1. [B1] manera en que está colocado un cuerpo o un objeto
     - example: Duermo mucho mejor en esta posición, boca arriba y sin almohada.
     - collocations: cambiar de posición; posición vertical; posición horizontal; posición fetal; en posición de firmes
  2. [B1] lugar exacto donde se encuentra algo o alguien
     - example: El sistema indica la posición del barco en todo momento.
     - collocations: determinar la posición; posición exacta; posición geográfica
  3. [B1] lugar que ocupa alguien o algo en una clasificación o un orden
     - example: Nuestro equipo terminó la liga en tercera posición.
     - collocations: primera posición; última posición; ocupar una posición; escalar posiciones; perder posiciones
  4. [B2] opinión o actitud que alguien adopta ante un asunto
     - example: El gobierno todavía no ha fijado su posición sobre la reforma.
     - frames: posición sobre algo; posición ante algo
     - collocations: tomar posición; fijar una posición; mantener su posición; posición firme
  5. [B2] situación social o económica de una persona dentro de un grupo
     - example: Su familia tenía una buena posición en el pueblo.
     - frames: estar en posición de + inf
     - collocations: posición social; buena posición; posición privilegiada; posición dominante
  6. [C1] lugar ocupado y defendido por tropas en una guerra
     - example: Los soldados atacaron las posiciones enemigas al amanecer.
     - collocations: posiciones enemigas; defender una posición
     - labels: domain military

### 44. memoria

**Lexeme 1: memoria**, NOUN
- grammar: gender Fem
- said: /me.ˈmo.ɾja/ (es-ES), /me.ˈmo.ɾja/ (es-MX)
  1. [A2] capacidad de la mente para guardar y recordar hechos, ideas e imágenes
     - example: Mi abuela tiene una memoria increíble para las fechas.
     - frames: memoria para algo
     - collocations: de memoria; saber algo de memoria; tener buena memoria; tener mala memoria; perder la memoria; hacer memoria; memoria fotográfica; falta de memoria
  2. [B2] recuerdo que se guarda de una persona o de un hecho pasado
     - example: Plantaron un árbol en memoria de los vecinos fallecidos.
     - frames: en memoria de alguien/algo
     - collocations: en memoria de; memoria histórica; memoria colectiva; honrar la memoria
  3. [B1] parte de un ordenador o aparato electrónico donde se guardan datos
     - example: Mi teléfono ya no tiene memoria suficiente para más fotos.
     - collocations: memoria RAM; memoria USB; tarjeta de memoria; memoria interna; memoria externa
     - labels: domain computing
  4. [B2] libro en que una persona cuenta hechos de su propia vida
     - example: El viejo general dedicó su retiro a escribir sus memorias.
     - collocations: escribir sus memorias; publicar sus memorias; libro de memorias
     - grammar: number Plur; countability Count
  5. [C1] documento escrito que explica un trabajo, una actividad o un proyecto
     - example: La empresa publica cada año una memoria con sus resultados.
     - collocations: memoria anual; memoria de actividades; memoria de prácticas; presentar una memoria
     - grammar: countability Count

### 45. reunión

**Lexeme 1: reunión**, NOUN
- grammar: gender Fem; countability Count
- said: /re.u.ˈnjon/ (es-ES), /re.u.ˈnjon/ (es-MX)
  1. [A2] encuentro de varias personas en un lugar para hablar, decidir algo o pasar un rato juntas
     - example: Mañana tenemos una reunión importante con el director a las nueve.
     - frames: reunión con alguien; reunión sobre algo
     - collocations: tener una reunión; convocar una reunión; celebrar una reunión; estar en una reunión; aplazar una reunión; sala de reuniones; reunión de trabajo; reunión familiar; reunión de vecinos
  2. [B2] hecho de juntar en un mismo lugar cosas o personas que estaban separadas
     - example: La reunión de todos los documentos nos llevó varias semanas.
     - frames: reunión de algo
- relations: verbal_noun_of reunir (VERB)

### 46. ensalada

**Lexeme 1: ensalada**, NOUN
- grammar: gender Fem; countability Both
- said: /en.sa.ˈla.da/ (es-ES), /en.sa.ˈla.da/ (es-MX)
  1. [A1] plato frío de verduras crudas u otros alimentos mezclados y aliñados
     - example: Para cenar prefiero algo ligero, como una ensalada con atún.
     - collocations: ensalada mixta; ensalada verde; aliñar la ensalada; aderezar la ensalada; ensalada de frutas; ensalada rusa; preparar una ensalada; ensalada de tomate
     - grammar: countability Both
  2. [C1] mezcla confusa de cosas muy distintas, sin orden ni relación
     - example: Su discurso fue una ensalada de ideas sin ningún sentido.
     - frames: una ensalada de algo
     - collocations: ensalada de ideas; ensalada de cifras
     - labels: figurative True, formality informal
     - grammar: countability Count

### 47. provecho

**Lexeme 1: provecho**, NOUN
- grammar: gender Masc; countability Mass
- said: /pɾo.ˈbe.tʃo/ (es-ES), /pɾo.ˈbe.tʃo/ (es-MX)
  1. [B1] beneficio o utilidad que se obtiene de algo o de alguien
     - example: Intenta sacar el máximo provecho de tus vacaciones este verano.
     - frames: sacar provecho de algo; en provecho de alguien
     - collocations: sacar provecho; ¡buen provecho!; en provecho propio; el máximo provecho; persona de provecho; de poco provecho; de provecho

### 48. sustitución

**Lexeme 1: sustitución**, NOUN
- grammar: gender Fem; countability Both
- said: /sus.ti.tu.ˈθjon/ (es-ES), /sus.ti.tu.ˈsjon/ (es-MX)
  1. [B1] cambio de una cosa o persona por otra que ocupa su lugar
     - example: La sustitución de las ventanas viejas costó mucho dinero.
     - frames: sustitución de algo por algo; sustitución de alguien por alguien
     - collocations: en sustitución de; la sustitución de piezas; proceder a la sustitución
  2. [B1] cambio de un jugador por otro durante un partido
     - example: El entrenador hizo una sustitución en el minuto setenta.
     - collocations: hacer una sustitución; realizar una sustitución; agotar las sustituciones
     - grammar: countability Count
  3. [B2] trabajo temporal en el puesto de alguien que está ausente
     - example: Este mes estoy haciendo una sustitución en un colegio.
     - collocations: hacer una sustitución; cubrir una sustitución
     - regions: es-ES mainly
     - grammar: countability Count
- relations: verbal_noun_of sustituir (VERB)

### 49. órgano

**Lexeme 1: órgano**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈoɾ.ɡa.no/ (es-ES), /ˈoɾ.ɡa.no/ (es-MX)
  1. [B1] parte del cuerpo de un ser vivo que cumple una función concreta
     - example: El corazón y los pulmones son órganos vitales para vivir.
     - collocations: órgano vital; donación de órganos; donante de órganos; trasplante de órganos; órganos internos; órganos sexuales
  2. [B1] instrumento musical de teclado con tubos por donde pasa el aire
     - example: Cada domingo, mi abuelo toca el órgano en la iglesia del pueblo.
     - collocations: tocar el órgano; órgano de tubos; órgano electrónico
  3. [C1] grupo de personas o institución que realiza una función dentro de una organización
     - example: El consejo es el máximo órgano de decisión de la empresa.
     - frames: órgano de algo
     - collocations: órgano de gobierno; órgano judicial; órgano consultivo; máximo órgano; órgano colegiado
  4. [C2] periódico o medio que expresa las ideas de un partido o grupo
     - example: Aquel diario fue durante años el órgano oficial del partido.
     - frames: órgano de algo
     - collocations: órgano oficial; órgano de expresión

### 50. unión

**Lexeme 1: unión**, NOUN
- grammar: gender Fem; countability Both
- said: /u.ˈnjon/ (es-ES), /u.ˈnjon/ (es-MX)
  1. [B1] hecho de juntar dos o más cosas o personas para formar un todo
     - example: La unión de ambas empresas creará cientos de puestos de trabajo.
     - frames: unión de algo/alguien; unión de algo con algo; unión entre algo/alguien
     - collocations: en unión con; en unión de
  2. [B1] grupo formado por personas, países u organizaciones con un fin común
     - example: Varios pueblos de la comarca han formado una unión de agricultores.
     - collocations: unión aduanera; unión monetaria; unión de consumidores
     - grammar: countability Count
  3. [B1] buena relación, acuerdo y apoyo mutuo entre las personas de un grupo
     - example: En esta familia siempre ha habido mucha unión entre los hermanos.
     - frames: unión entre alguien
     - collocations: la unión hace la fuerza; fomentar la unión
     - grammar: countability Mass
     - opposites: desunión
  4. [B2] relación estable de pareja, con matrimonio o sin él
     - example: Su unión duró más de cuarenta años y tuvieron cinco hijos.
     - collocations: unión de hecho; unión civil; unión matrimonial
     - grammar: countability Count
  5. [B2] lugar o línea donde se juntan dos piezas o partes
     - example: El agua entra por la unión entre los dos tubos.
     - collocations: punto de unión
     - grammar: countability Count
- relations: verbal_noun_of unir (VERB)

### 51. antena

**Lexeme 1: antena**, NOUN
- grammar: gender Fem; countability Count
- said: /an.ˈte.na/ (es-ES), /an.ˈte.na/ (es-MX)
  1. [B1] aparato que sirve para enviar o recibir señales de radio, televisión o teléfono
     - example: Hemos instalado una antena nueva en el tejado del edificio.
     - collocations: antena parabólica; antena de televisión; en antena; instalar una antena; orientar la antena; antena colectiva
  2. [B2] cada uno de los órganos largos y finos que tienen en la cabeza los insectos y otros animales
     - example: Las hormigas se tocan con las antenas para comunicarse entre ellas.
     - labels: domain zoology

### 52. sucio

**Lexeme 1: sucio**, ADJ
- grammar: comparable True
- said: /ˈsu.θjo/ (es-ES), /ˈsu.sjo/ (es-MX)
  1. [A1] que tiene manchas, polvo o basura y no está limpio
     - example: Deja los zapatos sucios en la puerta, por favor.
     - frames: sucio de algo
     - collocations: ropa sucia; platos sucios; manos sucias; muy sucio
     - opposites: limpio
  2. [B2] que no es honrado o que va contra las reglas
     - example: Ganaron el partido con un juego sucio y lleno de trampas.
     - collocations: jugar sucio; juego sucio; trabajo sucio; dinero sucio; guerra sucia; negocio sucio
     - opposites: limpio
  3. [B2] que habla de sexo de una forma poco educada o grosera
     - example: Mi tío siempre cuenta chistes sucios en las cenas familiares.
     - collocations: chiste sucio; mente sucia
     - labels: formality informal

### 53. perdón

**Lexeme 1: perdón**, NOUN
- grammar: gender Masc; countability Mass, mainly; note plural perdones, without the accent
- said: /peɾ.ˈdon/ (es-ES), /peɾ.ˈdon/ (es-MX)
  1. [A2] renuncia a castigar a alguien o a guardarle rencor por una ofensa o una falta
     - example: Te pido perdón por las palabras tan duras de ayer.
     - frames: perdón por algo; pedir perdón a alguien; perdón de algo
     - collocations: pedir perdón; conceder el perdón; otorgar el perdón; obtener el perdón; con perdón; perdón de los pecados
- relations: derived_from perdonar (VERB)
**Lexeme 2: perdón**, INTJ
- grammar: invariable True
- said: /peɾ.ˈdon/ (es-ES), /peɾ.ˈdon/ (es-MX)
  1. [A1] se usa para disculparse por una molestia o un error, o para llamar la atención con cortesía
     - example: Perdón, no me había dado cuenta de que estabas sentado aquí.
  2. [A2] se usa para pedir a alguien que repita lo que acaba de decir
     - example: ¿Perdón?, con tanto ruido no he oído bien tu nombre.

### 54. certificación

**Lexeme 1: certificación**, NOUN
- grammar: gender Fem
- said: /θeɾ.ti.fi.ka.ˈθjon/ (es-ES), /seɾ.ti.fi.ka.ˈsjon/ (es-MX)
- forms: certificaciones [Number=Plur]
  1. [B2] confirmación oficial de que algo es cierto, válido o cumple ciertas normas
     - example: La empresa espera obtener la certificación de calidad este año.
     - frames: certificación de algo
     - collocations: obtener la certificación; certificación de calidad; certificación oficial; proceso de certificación; certificación energética
     - grammar: countability Both
  2. [B2] documento oficial en el que se asegura que algo es verdadero
     - example: Necesito pedir una certificación de mis notas en la secretaría.
     - frames: certificación de algo
     - collocations: expedir una certificación; solicitar una certificación; certificación académica
     - grammar: countability Count
- relations: verbal_noun_of certificar (VERB)

### 55. limón

**Lexeme 1: limón**, NOUN
- grammar: gender Masc; countability Count, mainly
- said: /li.ˈmon/ (es-ES), /li.ˈmon/ (es-MX)
  1. [A1] fruto cítrico de piel amarilla o verde y sabor muy ácido
     - example: Échale unas gotas de limón al pescado antes de servirlo.
     - collocations: zumo de limón; jugo de limón; exprimir un limón; té con limón; rodaja de limón; ralladura de limón; agua de limón; sabor a limón
  2. [B2] árbol pequeño de hojas siempre verdes y flores blancas que da un fruto ácido
     - example: En el patio de mis abuelos crece un limón muy antiguo.
     - grammar: countability Count
