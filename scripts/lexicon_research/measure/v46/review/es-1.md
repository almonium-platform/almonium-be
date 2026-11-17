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

### 1. subsecuente

**Lexeme 1: subsecuente**, ADJ
- grammar: note same form for masculine and feminine; plural subsecuentes
- said: /sub.se.ˈkwen.te/ (es-ES), /sub.se.ˈkwen.te/ (es-MX)
  1. [C1] que viene inmediatamente después de otra cosa, en el tiempo o en orden
     - example: Las lluvias y las inundaciones subsecuentes dejaron a muchas familias sin casa.
     - frames: subsecuente a algo
     - labels: formality formal
     - opposites: anterior

### 2. segmentación

**Lexeme 1: segmentación**, NOUN
- grammar: gender Fem; countability Both
- said: /seɡ.men.ta.ˈθjon/ (es-ES), /seɡ.men.ta.ˈsjon/ (es-MX)
  1. [C1] división de algo en partes o trozos separados
     - example: La segmentación del texto en párrafos facilita mucho la lectura.
     - frames: segmentación de algo; segmentación en algo
  2. [C1] división de un mercado o de un público en grupos con rasgos comunes
     - example: Nuestra empresa necesita una mejor segmentación de los clientes por edad.
     - frames: segmentación del mercado; segmentación de clientes
     - labels: domain economics
- relations: verbal_noun_of segmentar (VERB)

### 3. perder

**Lexeme 1: perder**, VERB
- grammar: conjugation 2; irregular True; valency Both; note stem change e→ie in stressed syllables
- said: /peɾ.ˈdeɾ/ (es-ES), /peɾ.ˈdeɾ/ (es-MX)
- forms: pierdo [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; pierdes [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin]; pierde [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin]; perdemos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin]; pierden [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin]; perdí [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin]; perdió [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin]; perderé [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin]; pierda [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; perdido [Tense=Past, VerbForm=Part]; perdiendo [VerbForm=Ger]
  1. [A1] dejar de tener algo o a alguien, o no saber dónde está
     - example: Siempre pierdo las llaves cuando salgo de casa con prisa.
     - frames: perder algo; perder a alguien
     - grammar: valency Tr
     - opposites: encontrar
  2. [A2] no ganar en un juego, una competición o una lucha
     - example: Si perdemos este partido, quedamos fuera del torneo.
     - frames: perder algo; perder contra alguien
     - grammar: valency Both
     - opposites: ganar
  3. [B1] no llegar a tiempo para tomar un tren, autobús o avión
     - example: Corre, que vamos a perder el último autobús.
     - frames: perder el tren/autobús
     - grammar: valency Tr
  4. [B1] usar mal el tiempo, el dinero o el esfuerzo sin sacar provecho
     - example: No pierdas el tiempo con discusiones que no llevan a nada.
     - frames: perder el tiempo en algo
     - grammar: valency Tr
  5. [A2] no saber dónde uno está ni encontrar el camino
     - example: Me perdí en el centro porque no tenía un mapa.
     - frames: perderse en un lugar
     - grammar: valency Refl; reflexive True
  6. [B1] no ir a algo interesante o no disfrutar de algo bueno
     - example: No te pierdas el concierto de esta noche, va a ser genial.
     - frames: perderse algo
     - grammar: valency Refl; reflexive True

### 4. superior

**Lexeme 1: superior**, ADJ
- grammar: note same form for masculine and feminine: la parte superior
- said: /su.pe.ˈɾjoɾ/ (es-ES), /su.pe.ˈɾjoɾ/ (es-MX)
  1. [A2] que está situado más arriba o encima de otra cosa
     - example: Guarda las toallas en el estante superior del armario.
     - opposites: inferior
  2. [B1] mayor en cantidad, número, tamaño o grado que otra cosa
     - example: Este año las ventas han sido superiores a las del anterior.
     - frames: superior a algo
     - opposites: inferior
  3. [B1] mejor o más importante que otros en calidad, nivel o categoría
     - example: No soporto a la gente que se cree superior a los demás.
     - frames: superior a alguien
     - opposites: inferior
**Lexeme 2: superior**, NOUN
- grammar: gender Com; countability Count; note feminine also superiora, especially in religious orders
- said: /su.pe.ˈɾjoɾ/ (es-ES), /su.pe.ˈɾjoɾ/ (es-MX)
  1. [B1] persona que tiene autoridad sobre otras en un trabajo o en el ejército
     - example: Tienes que pedir permiso a tu superior antes de salir.
     - opposites: subordinado
  2. [C1] persona que dirige una comunidad religiosa, como un convento o monasterio
     - example: El superior del convento recibió a los visitantes en la entrada.
     - labels: domain religion

### 5. tienda

**Lexeme 1: tienda**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈtjen.da/ (es-ES), /ˈtjen.da/ (es-MX)
  1. [A1] establecimiento donde se venden cosas al público
     - example: Voy a la tienda a comprar pan y leche.
     - frames: tienda de algo; ir de tiendas
  2. [B1] refugio de tela que se monta para dormir en el campo
     - example: Montamos la tienda junto al río antes de que anocheciera.
     - frames: tienda de campaña
     - regions: es-ES, es-MX

### 6. idioma

**Lexeme 1: idioma**, NOUN
- grammar: gender Masc; countability Count; note masculine despite ending in -a: el idioma
- said: /i.ˈdjo.ma/ (es-ES), /i.ˈdjo.ma/ (es-MX)
  1. [A1] lengua que habla un pueblo o una nación, como el francés
     - example: ¿Cuántos idiomas hablas además del español?
- relations: confusable lenguaje (NOUN): lenguaje is the faculty or style of expression; idioma is a particular language such as French

### 7. dar

**Lexeme 1: dar**, VERB
- grammar: irregular True; conjugation 1; valency Both; auxiliary have
- said: /ˈdaɾ/ (es-ES), /ˈdaɾ/ (es-MX)
- forms: doy [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; das [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin]; da [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin]; damos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin]; dan [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin]; di [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin]; dio [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin]; daré [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin]; dé [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; dado [Tense=Past, VerbForm=Part]; dando [VerbForm=Ger]
  1. [A1] pasar algo a otra persona para que lo tenga o lo use
     - example: Te doy mi número de teléfono por si necesitas algo.
     - frames: dar algo a alguien
     - grammar: valency Tr
     - opposites: recibir
  2. [A2] causar en alguien un sentimiento, una sensación o un efecto
     - example: Me da miedo caminar solo por la noche.
     - frames: dar algo a alguien; dar miedo/hambre/sueño
     - grammar: valency Tr
  3. [A2] realizar la acción que expresa el nombre que sigue
     - example: ¿Damos un paseo por el parque después de comer?
     - frames: dar un paseo/un beso/un grito
     - grammar: valency Tr
  4. [B1] organizar una fiesta u ofrecer una clase, una charla o un espectáculo
     - example: Mañana damos una fiesta para celebrar su cumpleaños.
     - grammar: valency Tr
  5. [B1] estar orientado hacia un lugar, con vista o salida a él
     - example: La ventana de mi habitación da al jardín.
     - frames: dar a algo
     - grammar: valency Intr
  6. [B1] producir una planta o la tierra frutos o beneficios
     - example: Este árbol viejo todavía da unas manzanas muy dulces.
     - grammar: valency Tr
  7. [B1] sonar en un reloj las campanadas que marcan una hora
     - example: Ya han dado las diez y los niños siguen despiertos.
     - grammar: valency Intr
  8. [B2] encontrar algo o a alguien después de buscarlo
     - example: Por fin di con la calle que buscaba.
     - frames: dar con algo/alguien
     - grammar: valency Intr

### 8. novela

**Lexeme 1: novela**, NOUN
- grammar: gender Fem; countability Count
- said: /no.ˈbe.la/ (es-ES), /no.ˈbe.la/ (es-MX)
  1. [A2] obra literaria larga en prosa que cuenta una historia inventada
     - example: Estoy leyendo una novela de misterio muy entretenida.
  2. [B2] género literario formado por las obras narrativas largas en prosa
     - example: En clase estudiamos la novela española del siglo veinte.
     - grammar: countability Mass
  3. [B1] serie de televisión de muchos capítulos con una historia de amor
     - example: Mi abuela ve su novela todas las tardes a las cinco.
     - regions: es-MX, es-AR, es-CO

### 9. ido

**Lexeme 1: ido**, ADJ
- grammar: position Pred; note usually with estar
- said: /ˈi.do/ (es-ES), /ˈi.do/ (es-MX)
  1. [B2] distraído, con la mente en otra cosa y sin prestar atención
     - example: Hoy estás muy ido, no has escuchado nada de la reunión.
     - frames: estar ido
     - labels: formality informal
  2. [C1] que ha perdido el juicio o no razona con normalidad
     - example: Desde el accidente, el pobre hombre parece un poco ido.
     - frames: estar ido
     - labels: formality informal
- relations: derived_from ir (VERB)

### 10. odio

**Lexeme 1: odio**, NOUN
- grammar: gender Masc; countability Mass
- said: /ˈo.djo/ (es-ES), /ˈo.djo/ (es-MX)
  1. [B1] sentimiento muy fuerte de rechazo y deseo de mal hacia alguien
     - example: Nunca he sentido tanto odio hacia una persona como ahora.
     - frames: odio a/hacia algo o alguien
     - opposites: amor

### 11. férreo

**Lexeme 1: férreo**, ADJ
- grammar: comparable True
- said: /ˈfe.re.o/ (es-ES), /ˈfe.re.o/ (es-MX)
  1. [C1] muy duro, firme y difícil de cambiar o de vencer
     - example: Mi abuela mantiene una disciplina férrea con sus horarios de comida.
     - labels: figurative True
     - grammar: comparable True
  2. [C1] hecho de hierro o que contiene hierro
     - example: Una antigua vía férrea cruza el valle de norte a sur.
     - labels: style elevated
     - grammar: comparable False
- relations: adjective_of hierro (NOUN)

### 12. futuro

**Lexeme 1: futuro**, NOUN
- grammar: gender Masc; countability Both
- said: /fu.ˈtu.ɾo/ (es-ES), /fu.ˈtu.ɾo/ (es-MX)
  1. [A2] tiempo que todavía no ha llegado y que vendrá después
     - example: Nadie sabe con seguridad qué nos traerá el futuro.
     - frames: en el futuro
     - grammar: number Sing
     - opposites: pasado
  2. [B1] posibilidad de éxito o buena situación que espera a alguien o algo
     - example: Creo que este negocio no tiene mucho futuro aquí.
     - frames: tener futuro
  3. [A2] tiempo verbal que expresa acciones que ocurrirán más adelante
     - example: Hoy vamos a practicar el futuro de los verbos irregulares.
     - labels: domain grammar
**Lexeme 2: futuro**, ADJ
- grammar: comparable False
- said: /fu.ˈtu.ɾo/ (es-ES), /fu.ˈtu.ɾo/ (es-MX)
  1. [B1] que va a existir o suceder más adelante
     - example: Debemos cuidar el planeta para las generaciones futuras.
     - opposites: pasado

### 13. miniserie

**Lexeme 1: miniserie**, NOUN
- grammar: gender Fem; countability Count
- said: /mi.ni.ˈse.ɾje/ (es-ES), /mi.ni.ˈse.ɾje/ (es-MX)
  1. [B1] obra de televisión con pocos capítulos que cuenta una historia completa
     - example: Este invierno estrenan una miniserie basada en una novela famosa.
- relations: derived_from serie (NOUN)

### 14. mundial

**Lexeme 1: mundial**, ADJ
- grammar: comparable False; note same form for masculine and feminine; plural mundiales
- said: /mun.ˈdjal/ (es-ES), /mun.ˈdjal/ (es-MX)
  1. [A2] que afecta a todos los países de la Tierra o existe en ellos
     - example: El cambio climático es un problema mundial que nos afecta a todos.
     - opposites: local
- relations: adjective_of mundo (NOUN)
**Lexeme 2: mundial**, NOUN
- grammar: gender Masc; countability Count
- said: /mun.ˈdjal/ (es-ES), /mun.ˈdjal/ (es-MX)
  1. [A2] campeonato deportivo en el que compiten equipos de muchos países
     - example: ¿Vas a ver la final del Mundial con tus amigos?
- relations: derived_from mundial (ADJ)

### 15. moralista

**Lexeme 1: moralista**, ADJ
- grammar: comparable True; note same form for masculine and feminine; plural moralistas
- said: /mo.ɾa.ˈlis.ta/ (es-ES), /mo.ɾa.ˈlis.ta/ (es-MX)
  1. [B2] que pretende enseñar a los demás lo que está bien y lo que está mal
     - example: La película tiene un final demasiado moralista para mi gusto.
- relations: derived_from moral (NOUN)
**Lexeme 2: moralista**, NOUN
- grammar: gender Com; countability Count
- said: /mo.ɾa.ˈlis.ta/ (es-ES), /mo.ɾa.ˈlis.ta/ (es-MX)
  1. [B2] persona que da lecciones a otros sobre cómo deben comportarse
     - example: No te pongas de moralista, que tú tampoco eres perfecto.
     - frames: ponerse de moralista
     - labels: attitude disapproving
  2. [C1] autor que escribe y reflexiona sobre las costumbres y la conducta humana
     - example: Los grandes moralistas del siglo XVII escribieron máximas muy agudas.
     - labels: style literary
- relations: derived_from moral (NOUN)

### 16. eyaculación

**Lexeme 1: eyaculación**, NOUN
- grammar: gender Fem; countability Count
- said: /e.ʝa.ku.la.ˈθjon/ (es-ES), /e.ʝa.ku.la.ˈsjon/ (es-MX)
  1. [C1] salida del semen por el pene, normalmente al llegar al orgasmo
     - example: Muchos hombres consultan al médico por problemas de eyaculación precoz.
- relations: verbal_noun_of eyacular (VERB)

### 17. líder

**Lexeme 1: líder**, NOUN
- grammar: gender Com; countability Count
- said: /ˈli.deɾ/ (es-ES), /ˈli.deɾ/ (es-MX)
  1. [B1] persona que dirige un grupo y a la que otros siguen
     - example: Necesitamos una líder fuerte para sacar adelante el proyecto.
     - frames: líder de algo
  2. [B2] persona, equipo o empresa que ocupa el primer puesto en una competición o mercado
     - example: Después de esta victoria, seguimos siendo líderes de la liga.
     - frames: líder en algo; líder de algo

### 18. muerto

**Lexeme 1: muerto**, ADJ
- grammar: comparable False
- said: /ˈmweɾ.to/ (es-ES), /ˈmweɾ.to/ (es-MX)
  1. [A2] que ya no tiene vida
     - example: Encontramos un pájaro muerto debajo del árbol del jardín.
     - frames: estar muerto
     - opposites: vivo
  2. [B1] que siente algo, como hambre, frío o miedo, con mucha fuerza
     - example: Los niños llegaron a casa muertos de hambre.
     - frames: muerto de + sustantivo
     - labels: formality informal
  3. [B1] muy cansado, sin energía para hacer nada más
     - example: Después de correr diez kilómetros, estoy muerto.
     - frames: estar muerto
     - labels: formality informal
  4. [B2] sin actividad, movimiento ni animación
     - example: En invierno este pueblo parece un lugar muerto.
     - labels: figurative True
- relations: derived_from morir (VERB); confusable muerte (NOUN): muerte is the noun 'death'; muerto means 'dead' or 'dead person'
**Lexeme 2: muerto**, NOUN
- grammar: gender Masc; countability Count; note feminine la muerta for a woman
- said: /ˈmweɾ.to/ (es-ES), /ˈmweɾ.to/ (es-MX)
  1. [B1] persona que ha perdido la vida; cuerpo sin vida de una persona
     - example: El accidente de ayer dejó dos muertos y varios heridos.
     - opposites: vivo
- relations: derived_from morir (VERB)

### 19. clasicismo

**Lexeme 1: clasicismo**, NOUN
- grammar: gender Masc; countability Mass
- said: /kla.si.ˈθis.mo/ (es-ES), /kla.si.ˈsis.mo/ (es-MX)
  1. [C1] movimiento artístico y literario que imita los modelos de la Antigüedad griega y romana
     - example: El clasicismo francés buscaba el orden y la claridad en el teatro.
     - labels: domain art
  2. [C1] gusto por el equilibrio, la sobriedad y las formas tradicionales en el arte o la conducta
     - example: Me sorprende el clasicismo de su estilo, tan alejado de las modas.
- relations: derived_from clásico (ADJ)

### 20. brócoli

**Lexeme 1: brócoli**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈbɾo.ko.li/ (es-ES, brócoli), /ˈbɾo.ko.li/ (es-MX, brócoli), /ˈbɾo.ku.li/ (es-ES, bróculi), /ˈbɾo.ku.li/ (es-MX, bróculi)
- spellings: brócoli; bróculi
  1. [A2] verdura de tallo grueso con muchas flores verdes y apretadas, que se come cocinada
     - example: Esta noche cenaremos pescado con brócoli al vapor.

### 21. verdadero

**Lexeme 1: verdadero**, ADJ
- grammar: comparable True
- said: /beɾ.da.ˈde.ɾo/ (es-ES), /beɾ.da.ˈde.ɾo/ (es-MX)
  1. [A2] que es cierto y corresponde a lo que pasó o existe
     - example: ¿Es verdadera la historia que nos contaste ayer?
     - opposites: falso
  2. [B1] que es real y auténtico, no solo aparente ni imitado
     - example: Para mí eres un verdadero amigo desde la infancia.
     - opposites: falso
- relations: adjective_of verdad (NOUN)

### 22. abril

**Lexeme 1: abril**, NOUN
- grammar: gender Masc; countability Count
- said: /a.ˈbɾil/ (es-ES), /a.ˈbɾil/ (es-MX)
  1. [A1] cuarto mes del año, que va entre marzo y mayo
     - example: En abril suele llover bastante en esta región.
  2. [C1] años de edad de una persona joven
     - example: Con sus quince abriles, ya soñaba con viajar sola por el mundo.
     - labels: style literary, figurative True
     - grammar: number Plur

### 23. distancia

**Lexeme 1: distancia**, NOUN
- grammar: gender Fem; countability Both
- said: /dis.ˈtan.θja/ (es-ES), /dis.ˈtan.sja/ (es-MX)
  1. [A2] espacio o tiempo que hay entre dos lugares, cosas o momentos
     - example: ¿Qué distancia hay entre tu casa y la escuela?
     - frames: la distancia entre algo y algo; a distancia; a una distancia de
     - opposites: proximidad
  2. [B2] falta de cercanía, confianza o afecto en el trato entre personas
     - example: Después de la discusión, noto cierta distancia entre nosotros.
     - frames: mantener la distancia
     - labels: figurative True
     - opposites: cercanía

### 24. merendar

**Lexeme 1: merendar**, VERB
- grammar: conjugation 1; irregular True; valency Both; note stem change e > ie in stressed syllables (meriendo)
- said: /me.ɾen.ˈdaɾ/ (es-ES), /me.ɾen.ˈdaɾ/ (es-MX)
- forms: meriendo [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; meriendas [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin]; merienda [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin]; merendamos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin]; meriendan [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin]; merendé [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin]; merendó [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin]; merendaré [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin]; meriende [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; merendado [VerbForm=Part, Tense=Past]; merendando [VerbForm=Ger]
  1. [A2] tomar una comida ligera a media tarde, entre el almuerzo y la cena
     - example: Los niños meriendan pan con chocolate al salir del colegio.
     - frames: merendar algo
     - grammar: valency Both
  2. [C1] vencer a alguien con mucha facilidad en una competición o una discusión
     - example: En la segunda parte, el equipo visitante se merendó a los locales.
     - frames: merendarse a alguien
     - labels: formality informal, figurative True
     - regions: es-ES mainly
     - grammar: valency Tr; reflexive True; note used as merendarse; the pronoun adds emphasis

### 25. quetzal

**Lexeme 1: quetzal**, NOUN
- grammar: gender Masc; countability Count
- said: /ket.ˈθal/ (es-ES), /ket.ˈsal/ (es-MX)
  1. [C1] ave de bosques tropicales americanos, de plumas verdes brillantes y cola muy larga
     - example: En lo alto de aquel árbol vimos un quetzal macho.
  2. [C1] unidad de moneda oficial de Guatemala
     - example: Esta camisa cuesta cien quetzales en el mercado del pueblo.

### 26. hoy

**Lexeme 1: hoy**, ADV
- said: /ˈoj/ (es-ES), /ˈoj/ (es-MX)
  1. [A1] en este día, el día en que estamos ahora
     - example: ¿Qué día es hoy? Creo que es martes.
  2. [A2] en la época actual, en el tiempo en que vivimos
     - example: Hoy en día casi todos los jóvenes tienen teléfono móvil.
     - frames: hoy en día; hoy día

### 27. viernes

**Lexeme 1: viernes**, NOUN
- grammar: gender Masc; countability Count; invariable True; note same form in singular and plural: el viernes, los viernes
- said: /ˈbjeɾ.nes/ (es-ES), /ˈbjeɾ.nes/ (es-MX)
  1. [A1] día de la semana que va después del jueves y antes del sábado
     - example: Los viernes salgo a cenar con mis compañeros de trabajo.
     - frames: el viernes; los viernes

### 28. planeta

**Lexeme 1: planeta**, NOUN
- grammar: gender Masc; countability Count; note masculino aunque termina en -a: el planeta
- said: /pla.ˈne.ta/ (es-ES), /pla.ˈne.ta/ (es-MX)
  1. [A2] cuerpo celeste grande y redondo que gira alrededor de una estrella
     - example: ¿Sabes cuál es el planeta más grande del sistema solar?
  2. [B1] mundo donde vivimos los seres humanos, con toda su naturaleza
     - example: Tenemos que cuidar el planeta para las próximas generaciones.
     - frames: el planeta
     - grammar: number Sing

### 29. acontecer

**Lexeme 1: acontecer**, VERB
- grammar: conjugation 2; irregular True; valency Intr; note defective: used only in the third person and non-finite forms; -zc- before a and o
- said: /a.kon.te.ˈθeɾ/ (es-ES), /a.kon.te.ˈseɾ/ (es-MX)
- forms: acontece [Mood=Ind, Tense=Pres, Person=3, Number=Sing]; acontecen [Mood=Ind, Tense=Pres, Person=3, Number=Plur]; aconteció [Mood=Ind, Tense=Past, Person=3, Number=Sing]; acontecerá [Mood=Ind, Tense=Fut, Person=3, Number=Sing]; acontezca [Mood=Sub, Tense=Pres, Person=3, Number=Sing]; acontecido [VerbForm=Part]; aconteciendo [VerbForm=Ger]
  1. [C1] producirse o tener lugar un hecho
     - example: Nadie sabía explicar lo que había acontecido aquella noche en el pueblo.
     - frames: acontecer algo a alguien
     - labels: formality formal
**Lexeme 2: acontecer**, NOUN
- grammar: gender Masc; countability Mass; number Sing
- said: /a.kon.te.ˈθeɾ/ (es-ES), /a.kon.te.ˈseɾ/ (es-MX)
  1. [C1] conjunto de hechos que van ocurriendo a lo largo del tiempo
     - example: El periódico recoge cada mañana el acontecer político del país.
     - frames: el acontecer diario; el acontecer de algo
     - labels: formality formal
- relations: derived_from acontecer (VERB)

### 30. especulativo

**Lexeme 1: especulativo**, ADJ
- grammar: note regular: especulativa, especulativos, especulativas
- said: /es.pe.ku.la.ˈti.bo/ (es-ES), /es.pe.ku.la.ˈti.bo/ (es-MX)
  1. [C1] que busca ganancias comprando y vendiendo según los cambios de precio
     - example: La subida de los alquileres se debe en parte a inversiones especulativas.
     - frames: burbuja especulativa
  2. [C1] basado en suposiciones o teorías y no en hechos comprobados
     - example: Por ahora esa explicación es puramente especulativa y nadie la ha demostrado.
- relations: adjective_of especulación (NOUN)

### 31. libertad

**Lexeme 1: libertad**, NOUN
- grammar: gender Fem; countability Both
- said: /li.beɾ.ˈtad/ (es-ES), /li.beɾ.ˈtad/ (es-MX)
  1. [A2] capacidad de una persona para actuar y decidir según su propia voluntad
     - example: Todos tenemos derecho a la libertad de expresión y de opinión.
     - frames: libertad de + sustantivo; libertad para + infinitivo
  2. [B1] situación de quien no está preso ni es esclavo
     - example: Después de diez años en la cárcel, por fin recuperó la libertad.
     - frames: en libertad; poner a alguien en libertad
     - opposites: cautiverio
  3. [B2] confianza o falta de formalidad en el trato con otra persona
     - example: Me tomé la libertad de invitar a tu hermano a la cena.
     - frames: tomarse la libertad de + infinitivo; con toda libertad
- relations: abstract_noun_of libre (ADJ)

### 32. topográfico

**Lexeme 1: topográfico**, ADJ
- grammar: comparable False
- said: /to.po.ˈɡɾa.fi.ko/ (es-ES), /to.po.ˈɡɾa.fi.ko/ (es-MX)
  1. [C1] que describe o representa con detalle la forma y el relieve de un terreno
     - example: Antes de construir la carretera, los ingenieros estudiaron un mapa topográfico de la zona.
     - frames: mapa topográfico; levantamiento topográfico
- relations: adjective_of topografía (NOUN)

### 33. objeto

**Lexeme 1: objeto**, NOUN
- grammar: gender Masc; countability Count
- said: /ob.ˈxe.to/ (es-ES), /ob.ˈxe.to/ (es-MX)
  1. [A2] cosa material que se puede ver y tocar
     - example: En esta caja guardo objetos de mucho valor sentimental.
  2. [B2] fin o propósito que se busca al hacer algo
     - example: La reunión tiene por objeto resolver el problema del agua.
     - frames: con objeto de + inf; tener por objeto + inf
  3. [B2] persona o cosa hacia la que se dirige una acción o un sentimiento
     - example: Durante años fue objeto de burlas en el colegio.
     - frames: ser objeto de algo
  4. [B1] parte de la oración que recibe la acción del verbo
     - example: ¿Puedes encontrar el objeto directo en esta frase?
     - labels: domain grammar
     - opposites: sujeto

### 34. lengua

**Lexeme 1: lengua**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈlen.ɡwa/ (es-ES), /ˈlen.ɡwa/ (es-MX)
  1. [A2] sistema de palabras y reglas que usa un pueblo para comunicarse
     - example: ¿Cuántas lenguas se hablan hoy en tu país?
  2. [A2] órgano blando y móvil dentro de la boca que sirve para saborear y hablar
     - example: El médico me pidió que sacara la lengua.
  3. [C1] cosa larga y estrecha con la forma de ese órgano
     - example: Una lengua de fuego subía por la pared de la casa.
     - frames: lengua de tierra; lengua de fuego
     - labels: figurative True
- relations: confusable lenguaje (NOUN): lenguaje is the faculty or manner of speaking (body language, legal language); lengua is a particular language such as Spanish

### 35. hispanidad

**Lexeme 1: hispanidad**, NOUN
- grammar: gender Fem; countability Mass; number Sing; note often written with a capital: la Hispanidad
- said: /is.pa.ni.ˈdad/ (es-ES), /is.pa.ni.ˈdad/ (es-MX)
  1. [B2] conjunto de los pueblos y naciones que comparten la lengua y la cultura españolas
     - example: Cada 12 de octubre se celebra en muchos países el Día de la Hispanidad.
     - frames: el Día de la Hispanidad
  2. [C1] rasgos culturales e históricos comunes a los pueblos de lengua española
     - example: Varios ensayistas del siglo pasado escribieron sobre el sentido de la hispanidad.
- relations: derived_from hispano (ADJ)

### 36. medio

**Lexeme 1: medio**, ADJ
- said: /ˈme.dʝo/ (es-ES), /ˈme.dʝo/ (es-MX)
  1. [A1] que equivale a una de las dos partes iguales de algo
     - example: Necesito medio kilo de tomates para la ensalada de hoy.
     - grammar: position Attr
  2. [B1] que corresponde al valor normal o típico de un grupo
     - example: La temperatura media de agosto supera los treinta grados.
  3. [B1] que está en el centro o entre dos extremos
     - example: Muchas familias de clase media viven en este barrio.
- relations: confusable mitad (NOUN): English 'half' as a noun is mitad (la mitad de la tarta); medio goes directly before the noun (medio kilo)
**Lexeme 2: medio**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈme.dʝo/ (es-ES), /ˈme.dʝo/ (es-MX)
  1. [A2] cosa o forma que se usa para conseguir un fin
     - example: Para mí, el tren es el medio de transporte más cómodo.
     - frames: medio de + sust.; por medio de algo
  2. [B1] parte o lugar que está en el centro de algo
     - example: Pon la mesa en medio de la habitación, por favor.
     - frames: en medio de algo; en el medio
  3. [B1] empresas y canales que difunden noticias e información al público
     - example: Los medios hablaron mucho del accidente durante toda la semana.
     - frames: medios de comunicación
     - grammar: number Plur
  4. [B2] lugar y conjunto de condiciones en que vive un ser
     - example: Estos animales se adaptan bien a un medio frío y seco.
     - frames: medio ambiente
**Lexeme 3: medio**, ADV
- grammar: invariable True
- said: /ˈme.dʝo/ (es-ES), /ˈme.dʝo/ (es-MX)
  1. [B1] no del todo, de manera incompleta o en parte
     - example: Todavía estaba medio dormida cuando sonó el teléfono.

### 37. satánico

**Lexeme 1: satánico**, ADJ
- said: /sa.ˈta.ni.ko/ (es-ES), /sa.ˈta.ni.ko/ (es-MX)
  1. [B2] del diablo o del culto que algunas personas le rinden
     - example: En las paredes de la vieja casa había símbolos satánicos pintados.
     - frames: secta satánica; ritual satánico
  2. [C1] extremadamente malvado o perverso, como si viniera del diablo
     - example: Aquel villano tenía una risa satánica que asustaba a cualquiera.
     - labels: figurative True
- relations: adjective_of Satanás (PROPN)

### 38. tártaro

**Lexeme 1: tártaro**, ADJ
- grammar: comparable False; note feminine tártara; plural tártaros, tártaras
- said: /ˈtaɾ.ta.ɾo/ (es-ES), /ˈtaɾ.ta.ɾo/ (es-MX)
  1. [C1] de un pueblo de origen turco de Rusia y Asia central, o de su lengua
     - example: En el museo había una colección de tapices tártaros muy antiguos.
  2. [C1] preparado con carne o pescado crudo picado y aliñado
     - example: De primero pedimos un bistec tártaro con pan tostado.
     - frames: bistec tártaro; salsa tártara
     - labels: domain cooking
**Lexeme 2: tártaro**, NOUN
- grammar: gender Masc; countability Count; note feminine tártara for a woman
- said: /ˈtaɾ.ta.ɾo/ (es-ES), /ˈtaɾ.ta.ɾo/ (es-MX)
  1. [C1] persona de un pueblo de origen turco de Rusia y Asia central
     - example: Muchos tártaros viven hoy en las ciudades a orillas del Volga.
  2. [C2] lengua de origen turco que habla ese pueblo
     - example: Mi abuela todavía habla tártaro con sus hermanas.
     - grammar: countability Mass
**Lexeme 3: tártaro**, NOUN
- grammar: gender Masc; countability Mass
- said: /ˈtaɾ.ta.ɾo/ (es-ES), /ˈtaɾ.ta.ɾo/ (es-MX)
  1. [C1] capa dura y amarillenta que se pega a los dientes
     - example: El dentista me quitó el tártaro acumulado en las muelas.
     - frames: tártaro dental
  2. [C2] sal que forma una costra en los recipientes donde fermenta el vino
     - example: Las barricas viejas tienen las paredes cubiertas de tártaro.
     - frames: crémor tártaro
     - labels: domain chemistry
**Lexeme 4: Tártaro**, PROPN
- grammar: gender Masc; number Sing
- said: /ˈtaɾ.ta.ɾo/ (es-ES), /ˈtaɾ.ta.ɾo/ (es-MX)
  1. [C2] lugar más profundo del mundo de los muertos en la religión griega antigua
     - example: Según el mito, los titanes fueron encerrados para siempre en el Tártaro.
     - frames: el Tártaro
     - labels: domain mythology

### 39. cine

**Lexeme 1: cine**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈθi.ne/ (es-ES), /ˈsi.ne/ (es-MX)
  1. [A1] local público donde se proyectan películas en una pantalla grande
     - example: ¿Vamos al cine esta noche después de cenar?
     - frames: ir al cine
     - grammar: countability Count
  2. [A2] arte e industria de hacer películas
     - example: Me interesa mucho el cine europeo de los años sesenta.
     - grammar: countability Mass
- relations: derived_from cinematógrafo (NOUN)

### 40. campo

**Lexeme 1: campo**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈkam.po/ (es-ES), /ˈkam.po/ (es-MX)
  1. [A1] zona fuera de las ciudades, con pueblos, granjas y naturaleza
     - example: Los fines de semana nos gusta salir al campo.
     - grammar: number Sing; countability Mass
     - opposites: ciudad
  2. [A2] terreno abierto donde se cultivan plantas o come el ganado
     - example: En verano, los campos de trigo se vuelven amarillos.
     - grammar: countability Count
  3. [A2] terreno preparado para jugar a un deporte
     - example: Cuando el equipo salió al campo, todos aplaudieron.
     - frames: campo de + deporte
     - grammar: countability Count
  4. [B2] conjunto de temas o actividades que forman una ciencia o profesión
     - example: Es una investigadora muy conocida en el campo de la genética.
     - frames: en el campo de algo
     - grammar: countability Count
  5. [B2] lugar con instalaciones donde se reúne a refugiados, prisioneros u otras personas
     - example: Miles de familias siguen viviendo en campos de refugiados.
     - frames: campo de refugiados; campo de concentración
     - grammar: countability Count

### 41. condensador

**Lexeme 1: condensador**, NOUN
- grammar: gender Masc; countability Count
- said: /kon.den.sa.ˈdoɾ/ (es-ES), /kon.den.sa.ˈdoɾ/ (es-MX)
  1. [C1] aparato de dos conductores separados por un aislante que almacena carga eléctrica
     - example: Para arreglar la radio hay que cambiar un condensador quemado.
     - labels: domain electricity
  2. [C1] aparato que enfría un gas o un vapor para convertirlo en líquido
     - example: El condensador del aire acondicionado está fuera, en el balcón.
     - labels: domain technology
- relations: derived_from condensar (VERB)

### 42. ovino

**Lexeme 1: ovino**, ADJ
- grammar: comparable False
- said: /o.ˈbi.no/ (es-ES), /o.ˈbi.no/ (es-MX)
  1. [B2] de las ovejas o del ganado formado por ovejas
     - example: La producción de carne ovina ha bajado bastante este año.
     - frames: ganado ovino
- relations: adjective_of oveja (NOUN)
**Lexeme 2: ovino**, NOUN
- grammar: gender Masc; countability Count
- said: /o.ˈbi.no/ (es-ES), /o.ˈbi.no/ (es-MX)
  1. [C1] animal del grupo de las ovejas, criado por su carne, lana o leche
     - example: En esta comarca se crían ovinos y cabras desde hace siglos.
     - frames: carne de ovino

### 43. lugar

**Lexeme 1: lugar**, NOUN
- grammar: gender Masc; countability Count
- said: /lu.ˈɡaɾ/ (es-ES), /lu.ˈɡaɾ/ (es-MX)
  1. [A1] parte del espacio que ocupa o puede ocupar una persona o cosa
     - example: Necesito un lugar tranquilo para estudiar esta tarde.
     - frames: tener lugar; en lugar de algo
  2. [B1] puesto que ocupa alguien o algo en una serie o clasificación
     - example: Nuestro equipo terminó la carrera en segundo lugar.
     - frames: en primer lugar
  3. [B1] situación o circunstancias en que se encuentra otra persona
     - example: Si estuvieras en mi lugar, ¿qué harías tú?
     - frames: ponerse en el lugar de alguien
  4. [B2] motivo u ocasión para que algo ocurra
     - example: Sus palabras dieron lugar a muchas discusiones en la familia.
     - frames: dar lugar a algo; sin lugar a dudas

### 44. riesgo

**Lexeme 1: riesgo**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈrjes.ɡo/ (es-ES), /ˈrjes.ɡo/ (es-MX)
  1. [B1] posibilidad de que ocurra un daño o algo malo
     - example: Fumar aumenta mucho el riesgo de sufrir enfermedades del corazón.
     - frames: riesgo de + inf/sust; correr el riesgo de + inf; en riesgo
     - opposites: seguridad
  2. [B2] cada uno de los posibles daños que cubre un seguro
     - example: ¿Tu coche tiene un seguro a todo riesgo o solo básico?
     - frames: a todo riesgo
     - grammar: countability Count

### 45. simple

**Lexeme 1: simple**, ADJ
- grammar: comparable True; note same form for masculine and feminine; plural simples
- said: /ˈsim.ple/ (es-ES), /ˈsim.ple/ (es-MX)
  1. [A2] fácil de hacer o de entender, sin complicaciones
     - example: La receta es muy simple y solo tarda diez minutos.
     - opposites: complicado
  2. [B1] solo eso y nada más, sin otra importancia
     - example: Fue un simple error, no hace falta pedir tantas disculpas.
     - frames: un simple + sustantivo
     - grammar: position Attr
  3. [B2] formado por un solo elemento, sin partes distintas
     - example: En esta lección estudiamos los tiempos verbales simples del indicativo.
     - opposites: compuesto
  4. [B2] dicho de una persona, con poca inteligencia o demasiado ingenua
     - example: No seas tan simple, ¿no ves que te están engañando?
     - labels: attitude disapproving

### 46. peso

**Lexeme 1: peso**, NOUN
- grammar: gender Masc; countability Both
- said: /ˈpe.so/ (es-ES), /ˈpe.so/ (es-MX)
  1. [A2] cantidad de kilos o gramos que tiene un cuerpo o una persona
     - example: Mi médico dice que tengo que perder un poco de peso.
     - frames: perder peso; ganar peso
     - grammar: countability Mass
  2. [A2] moneda oficial de México, Argentina, Colombia y otros países americanos
     - example: ¿Cuántos pesos cuesta este libro de cocina?
     - grammar: countability Count
  3. [B2] importancia o influencia que tiene algo o alguien en una situación
     - example: Sus opiniones tienen mucho peso en las decisiones del grupo.
     - frames: tener peso
     - labels: figurative True
     - grammar: countability Mass
  4. [B2] preocupación o responsabilidad que hace sufrir a alguien, como una carga
     - example: Al decirle la verdad, me quité un gran peso de encima.
     - frames: quitarse un peso de encima
     - labels: figurative True
     - grammar: countability Count
- relations: derived_from pesar (VERB)

### 47. sobradamente

**Lexeme 1: sobradamente**, ADV
- grammar: invariable True
- said: /so.ˌbɾa.da.ˈmen.te/ (es-ES), /so.ˌbɾa.da.ˈmen.te/ (es-MX)
  1. [C1] más que suficientemente, en un grado mayor del necesario
     - example: Su talento como pianista está sobradamente demostrado desde hace años.
     - frames: sobradamente conocido; sobradamente demostrado
- relations: adverb_of sobrado (ADJ)

### 48. azul

**Lexeme 1: azul**, ADJ
- grammar: note same form for masculine and feminine; plural azules
- said: /a.ˈθul/ (es-ES), /a.ˈsul/ (es-MX)
  1. [A1] del color del cielo sin nubes o del mar en un día claro
     - example: Mañana voy a ponerme la camisa azul para la entrevista.
**Lexeme 2: azul**, NOUN
- grammar: gender Masc; countability Both
- said: /a.ˈθul/ (es-ES), /a.ˈsul/ (es-MX)
  1. [A1] color como el del cielo despejado durante el día
     - example: Desde pequeño, el azul es mi color favorito.

### 49. favor

**Lexeme 1: favor**, NOUN
- grammar: gender Masc; countability Count
- said: /fa.ˈboɾ/ (es-ES), /fa.ˈboɾ/ (es-MX)
  1. [A2] ayuda que se da a alguien sin pedir nada a cambio
     - example: ¿Me puedes hacer un favor y cerrar la ventana?
     - frames: hacer un favor a alguien; pedir un favor a alguien
  2. [B1] apoyo, preferencia o buena opinión que alguien da a una persona o idea
     - example: La mayoría de los vecinos está a favor del nuevo parque.
     - frames: a favor de algo; en favor de alguien
     - grammar: countability Mass

### 50. repoblación

**Lexeme 1: repoblación**, NOUN
- grammar: gender Fem; countability Both
- said: /re.po.bla.ˈθjon/ (es-ES), /re.po.bla.ˈsjon/ (es-MX)
  1. [B2] plantación de árboles o introducción de animales en un lugar donde faltaban
     - example: Tras el incendio comenzó la repoblación del monte con pinos y encinas.
     - frames: repoblación forestal; repoblación de algo
  2. [C1] llegada y asentamiento de nuevos habitantes en un territorio vacío o abandonado
     - example: Muchos pueblos pequeños necesitan planes de repoblación para no desaparecer.
- relations: verbal_noun_of repoblar (VERB)

### 51. ostracismo

**Lexeme 1: ostracismo**, NOUN
- grammar: gender Masc; countability Mass
- said: /os.tɾa.ˈθis.mo/ (es-ES), /os.tɾa.ˈsis.mo/ (es-MX)
  1. [C1] exclusión de una persona de la vida social, política o profesional de un grupo
     - example: Tras el escándalo, el actor vivió varios años de ostracismo.
     - frames: condenar a alguien al ostracismo; sufrir el ostracismo
  2. [C2] en la antigua Atenas, destierro de un ciudadano decidido por votación popular
     - example: Los atenienses usaban el ostracismo contra los políticos que parecían demasiado poderosos.
     - labels: time historical, domain history

### 52. matrimonio

**Lexeme 1: matrimonio**, NOUN
- grammar: gender Masc; countability Count
- said: /ma.tɾi.ˈmo.njo/ (es-ES), /ma.tɾi.ˈmo.njo/ (es-MX)
  1. [B1] unión legal o religiosa de dos personas que forman una pareja
     - example: Llevan veinte años de matrimonio y siguen muy felices.
     - frames: contraer matrimonio; matrimonio con alguien
  2. [B1] pareja formada por dos personas casadas
     - example: En el piso de arriba vive un matrimonio con dos niños.

### 53. zonal

**Lexeme 1: zonal**, ADJ
- grammar: comparable False; note same form for masculine and feminine; plural zonales
- said: /θo.ˈnal/ (es-ES), /so.ˈnal/ (es-MX)
  1. [C1] propio de un área o región determinada, o limitado a ella
     - example: Las autoridades han aprobado un plan zonal para mejorar los caminos rurales.
  2. [C1] en que cada jugador defiende un área del campo y no a un rival concreto
     - example: Nuestro equipo siempre juega con una defensa zonal en los córners.
     - frames: defensa zonal; marcaje zonal
     - labels: domain sport
- relations: adjective_of zona (NOUN)

### 54. enero

**Lexeme 1: enero**, NOUN
- grammar: gender Masc; countability Count
- said: /e.ˈne.ɾo/ (es-ES), /e.ˈne.ɾo/ (es-MX)
  1. [A1] primer mes del año, que tiene treinta y un días
     - example: En enero suele hacer mucho frío en esta región.
     - frames: en enero; el X de enero

### 55. plata

**Lexeme 1: plata**, NOUN
- grammar: gender Fem; countability Mass
- said: /ˈpla.ta/ (es-ES), /ˈpla.ta/ (es-MX)
  1. [A2] metal precioso de color blanco y brillante, usado en joyas y monedas
     - example: Mi abuela me regaló unos pendientes de plata muy bonitos.
     - frames: de plata
  2. [B1] dinero, sobre todo el que alguien tiene para gastar
     - example: Este mes no tengo plata para salir a cenar.
     - labels: formality informal
     - regions: es-AR, es-CO

### 56. área

**Lexeme 1: área**, NOUN
- grammar: gender Fem; countability Count; note takes el and un in the singular (el área, un área) but stays feminine
- said: /ˈa.ɾe.a/ (es-ES), /ˈa.ɾe.a/ (es-MX)
  1. [A2] zona o parte de un lugar con límites o un uso concreto
     - example: Esta área del parque está reservada para los niños pequeños.
  2. [B1] campo de actividad, de conocimiento o de trabajo
     - example: Trabajo en el área de recursos humanos desde hace años.
  3. [B1] medida de la superficie que ocupa una figura plana
     - example: ¿Sabes calcular el área de un círculo con este radio?
     - labels: domain mathematics
  4. [B2] zona marcada delante de la portería en un campo de fútbol
     - example: El defensa cometió una falta clara dentro del área.
     - labels: domain sport

### 57. elegantemente

**Lexeme 1: elegantemente**, ADV
- grammar: invariable True
- said: /e.le.ˌɡan.te.ˈmen.te/ (es-ES), /e.le.ˌɡan.te.ˈmen.te/ (es-MX)
  1. [B2] con buen gusto, gracia y distinción en el aspecto o los movimientos
     - example: Todos los invitados llegaron elegantemente vestidos a la boda.
     - frames: elegantemente vestido
  2. [C1] de manera hábil, sencilla y eficaz, sin complicaciones innecesarias
     - example: La autora resuelve elegantemente el misterio en el último capítulo.
- relations: adverb_of elegante (ADJ)

### 58. jugador

**Lexeme 1: jugador**, NOUN
- grammar: gender Masc; countability Count
- said: /xu.ɡa.ˈdoɾ/ (es-ES), /xu.ɡa.ˈdoɾ/ (es-MX)
  1. [A2] persona que participa en un deporte, partido o competición
     - example: Nuestro equipo necesita un jugador más para el partido del sábado.
     - frames: jugador de + deporte
  2. [B2] persona que tiene el vicio de apostar dinero
     - example: Su abuelo era un jugador que perdió toda su fortuna en el casino.
- relations: agent_noun_of jugar (VERB)

### 59. yunque

**Lexeme 1: yunque**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈʝun.ke/ (es-ES), /ˈʝun.ke/ (es-MX)
  1. [B2] bloque de hierro sobre el que se golpean los metales para darles forma
     - example: El herrero colocó la herradura caliente sobre el yunque.
  2. [C1] pequeño hueso del oído medio, situado entre el martillo y el estribo
     - example: Un golpe fuerte en la cabeza puede dañar el yunque del oído.
     - labels: domain anatomy

### 60. jajaja

**Lexeme 1: jajaja**, INTJ
- grammar: invariable True; note written imitation of laughter; the academic norm prefers ja, ja, ja
- said: /xa.ˈxa.xa/ (es-ES), /xa.ˈxa.xa/ (es-MX)
  1. [A2] expresión que imita el sonido de la risa, sobre todo en mensajes
     - example: ¡Jajaja, no me esperaba ese final para nada!
     - labels: formality informal
- relations: derived_from ja (INTJ)

### 61. sábado

**Lexeme 1: sábado**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈsa.ba.do/ (es-ES), /ˈsa.ba.do/ (es-MX)
  1. [A1] día de la semana que va después del viernes y antes del domingo
     - example: Los sábados por la mañana vamos al mercado con mis abuelos.
     - frames: el sábado; los sábados
  2. [C1] día semanal de descanso y oración en la religión judía
     - example: En la tradición judía, nadie debe trabajar durante el sábado.
     - labels: domain religion

### 62. región

**Lexeme 1: región**, NOUN
- grammar: gender Fem; countability Count
- said: /re.ˈxjon/ (es-ES), /re.ˈxjon/ (es-MX)
  1. [A2] parte de un territorio con características propias de clima, cultura o gobierno
     - example: En esta región del país llueve mucho durante el invierno.
  2. [C1] zona determinada del cuerpo de una persona o de un animal
     - example: Siento un dolor bastante fuerte en la región lumbar desde ayer.
     - labels: domain anatomy

### 63. dinero

**Lexeme 1: dinero**, NOUN
- grammar: gender Masc; countability Mass
- said: /di.ˈne.ɾo/ (es-ES), /di.ˈne.ɾo/ (es-MX)
  1. [A1] monedas y billetes que se usan para comprar y pagar cosas
     - example: ¿Llevas dinero para pagar el taxi?
     - frames: dinero en efectivo; ganar dinero; pagar con dinero
  2. [A2] conjunto de bienes y riquezas que tiene una persona
     - example: Su familia tiene mucho dinero, pero vive con sencillez.

### 64. noticiario

**Lexeme 1: noticiario**, NOUN
- grammar: gender Masc; countability Count
- said: /no.ti.ˈθja.ɾjo/ (es-ES), /no.ti.ˈsja.ɾjo/ (es-MX)
  1. [B1] programa de radio o televisión en el que se cuentan los hechos más recientes
     - example: Todas las noches vemos el noticiario mientras cenamos en casa.
     - frames: el noticiario de la noche
  2. [C1] película corta con hechos de actualidad que se proyectaba en los cines antes de la película principal
     - example: Antes de la película, el cine proyectaba un noticiario sobre la guerra.
     - labels: time historical
- relations: derived_from noticia (NOUN)

### 65. brillantemente

**Lexeme 1: brillantemente**, ADV
- grammar: invariable True
- said: /bɾi.ˌʝan.te.ˈmen.te/ (es-ES), /bɾi.ˌʝan.te.ˈmen.te/ (es-MX)
  1. [B2] de manera excelente, con mucho talento o éxito
     - example: La joven pianista superó brillantemente todas las pruebas del concurso.
     - opposites: pésimamente
  2. [C1] con mucha luz o de forma muy viva y resplandeciente
     - example: Entramos en un salón brillantemente iluminado por enormes lámparas.
     - frames: brillantemente iluminado
- relations: adverb_of brillante (ADJ)

### 66. calidad

**Lexeme 1: calidad**, NOUN
- grammar: gender Fem; countability Both
- said: /ka.li.ˈdad/ (es-ES), /ka.li.ˈdad/ (es-MX)
  1. [A2] conjunto de propiedades que hacen a una cosa mejor o peor
     - example: Me preocupa mucho la calidad del agua en esta zona.
     - frames: calidad de algo
  2. [B1] valor alto o excelencia de algo frente a lo común
     - example: En esta tienda solo venden ropa de calidad.
     - frames: de calidad
     - grammar: countability Mass
  3. [C1] condición o función con la que alguien actúa en una situación
     - example: Hoy hablo en calidad de presidente de la asociación.
     - frames: en calidad de + sustantivo
     - labels: formality formal

### 67. freír

**Lexeme 1: freír**, VERB
- grammar: conjugation 3; irregular True; valency Tr; note e changes to i like reír; two past participles, frito and freído
- said: /fɾe.ˈiɾ/ (es-ES), /fɾe.ˈiɾ/ (es-MX)
- forms: frío [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; fríes [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin]; fríe [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin]; freímos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin]; fríen [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin]; freí [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin]; frio [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin]; freiré [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin]; fría [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; frito [VerbForm=Part]; freído [VerbForm=Part] (less common than frito; used only in compound tenses); friendo [VerbForm=Ger]
  1. [A2] cocinar un alimento metiéndolo en aceite o grasa muy caliente
     - example: Primero fríe las patatas y después añade los huevos.
     - frames: freír algo en aceite
     - grammar: valency Tr
  2. [C1] molestar mucho a alguien con algo que se repite sin parar
     - example: Los periodistas lo frieron a preguntas al salir del juicio.
     - frames: freír a alguien a preguntas
     - labels: formality informal, figurative True
     - grammar: valency Tr
  3. [B2] pasar muchísimo calor, sobre todo por el sol
     - example: En esta oficina sin aire acondicionado nos freímos todo el verano.
     - frames: freírse de calor
     - labels: formality informal, figurative True
     - grammar: valency Refl; reflexive True

### 68. liberador

**Lexeme 1: liberador**, ADJ
- grammar: note feminine liberadora; plural liberadores, liberadoras
- said: /li.be.ɾa.ˈdoɾ/ (es-ES), /li.be.ɾa.ˈdoɾ/ (es-MX)
- forms: liberadora [Gender=Fem, Number=Sing]; liberadores [Gender=Masc, Number=Plur]; liberadoras [Gender=Fem, Number=Plur]
  1. [B2] que produce una sensación de alivio, como si se quitara un peso
     - example: Hablar por fin con mi familia fue una experiencia muy liberadora.
  2. [C1] que deja libre a alguien o algo de una opresión, un dominio o un encierro
     - example: El ejército liberador entró en la ciudad al amanecer.
- relations: derived_from liberar (VERB); confusable libertador (NOUN): libertador is the usual word for a person who frees a country or people
**Lexeme 2: liberador**, NOUN
- grammar: gender Masc; countability Count; note feminine liberadora
- said: /li.be.ɾa.ˈdoɾ/ (es-ES), /li.be.ɾa.ˈdoɾ/ (es-MX)
- forms: liberadora [Gender=Fem, Number=Sing]
  1. [C1] persona que deja libre a otras de una opresión o un encierro
     - example: La gente recibió a sus liberadores con flores y aplausos.
     - frames: el liberador de algo/alguien
- relations: agent_noun_of liberar (VERB); confusable libertador (NOUN): libertador is the more common noun for someone who frees a people

### 69. prensa

**Lexeme 1: prensa**, NOUN
- grammar: gender Fem; countability Both
- said: /ˈpɾen.sa/ (es-ES), /ˈpɾen.sa/ (es-MX)
  1. [B1] conjunto de periódicos y revistas, y las personas que trabajan en ellos
     - example: El ministro no quiso hablar con la prensa después de la reunión.
     - frames: la prensa; rueda de prensa
     - grammar: countability Mass
  2. [B2] máquina que sirve para apretar o aplastar algo con mucha fuerza
     - example: Usamos una prensa para sacar el aceite de las aceitunas.
     - grammar: countability Count
  3. [C1] opinión buena o mala que la gente tiene de alguien o algo
     - example: Los videojuegos tienen mala prensa entre muchos padres y maestros.
     - frames: tener buena prensa; tener mala prensa
     - labels: figurative True
     - grammar: countability Mass

### 70. dado

**Lexeme 1: dado**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈda.do/ (es-ES), /ˈda.do/ (es-MX)
  1. [A2] pieza en forma de cubo con puntos del uno al seis, usada en juegos
     - example: Te toca tirar los dados para mover la ficha.
  2. [B2] trozo pequeño de alimento cortado en forma de cubo
     - example: Corta la zanahoria en dados y añádela a la sopa.
     - frames: cortar en dados
**Lexeme 2: dado**, ADJ
- said: /ˈda.do/ (es-ES), /ˈda.do/ (es-MX)
  1. [B2] determinado, concreto, del que se habla en particular
     - example: En un momento dado, decidimos cambiar de planes.
     - frames: en un momento dado
  2. [C1] que tiene tendencia o costumbre de hacer algo
     - example: Mi abuelo nunca fue muy dado a hablar de su infancia.
     - frames: ser dado a algo; ser dado a + inf
     - grammar: position Pred
- relations: derived_from dar (VERB)

### 71. poblar

**Lexeme 1: poblar**, VERB
- grammar: conjugation 1; irregular True; valency Tr; note stem change o > ue when stressed: pueblo, poblamos
- said: /po.ˈblaɾ/ (es-ES), /po.ˈblaɾ/ (es-MX)
- forms: pueblo [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; pueblas [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin]; puebla [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin]; poblamos [Mood=Ind, Tense=Pres, Person=1, Number=Plur, VerbForm=Fin]; pueblan [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin]; poblé [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin]; pobló [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin]; poblaré [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin]; pueble [Mood=Sub, Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin]; poblado [VerbForm=Part, Tense=Past]; poblando [VerbForm=Ger]
  1. [B2] llevar personas, animales o plantas a un lugar para que vivan allí
     - example: Los primeros colonos poblaron estas tierras hace tres siglos.
     - frames: poblar algo de/con algo
     - grammar: valency Tr
     - opposites: despoblar
  2. [B2] vivir en un lugar o estar en él en gran número
     - example: En invierno, miles de aves pueblan estas lagunas.
     - grammar: valency Tr
  3. [C1] llenarse un lugar o una cosa de algo
     - example: Al anochecer, el cielo se pobló de estrellas.
     - frames: poblarse de algo
     - grammar: valency Refl; note used as poblarse
- relations: derived_from pueblo (NOUN)

### 72. análisis

**Lexeme 1: análisis**, NOUN
- grammar: gender Masc; countability Count; note same form in the plural: los análisis
- said: /a.ˈna.li.sis/ (es-ES), /a.ˈna.li.sis/ (es-MX)
- forms: análisis [Number=Plur]
  1. [B1] estudio detallado de algo, separando y examinando cada una de sus partes
     - example: Necesitamos un análisis más profundo de los resultados de la encuesta.
     - frames: análisis de algo
     - opposites: síntesis
  2. [A2] prueba médica de la sangre, la orina u otra sustancia del cuerpo
     - example: Mañana tengo que hacerme unos análisis de sangre en ayunas.
     - frames: hacerse un análisis; análisis de sangre

### 73. socavar

**Lexeme 1: socavar**, VERB
- grammar: conjugation 1; irregular False; valency Tr
- said: /so.ka.ˈbaɾ/ (es-ES), /so.ka.ˈbaɾ/ (es-MX)
  1. [C1] debilitar algo poco a poco, quitándole fuerza, valor o apoyo
     - example: Tantas mentiras acaban socavando la confianza entre los socios.
     - frames: socavar algo
     - labels: figurative True
  2. [C2] hacer un hueco por debajo de algo, dejándolo sin apoyo
     - example: El agua del río ha ido socavando los cimientos del puente.
     - frames: socavar algo
- relations: derived_from cavar (VERB)

### 74. espíritu

**Lexeme 1: espíritu**, NOUN
- grammar: gender Masc; countability Count
- said: /es.ˈpi.ɾi.tu/ (es-ES), /es.ˈpi.ɾi.tu/ (es-MX)
  1. [B1] parte inmaterial de la persona, donde están los pensamientos y sentimientos
     - example: Hacer ejercicio es bueno para el cuerpo y para el espíritu.
     - opposites: cuerpo
  2. [B1] ser sin cuerpo, como un fantasma, un ángel o un demonio
     - example: Muchos creen que en esa casa vieja vive un espíritu.
  3. [B2] ánimo, valor y energía para actuar o enfrentar dificultades
     - example: A pesar de la derrota, el equipo no perdió el espíritu.
  4. [B2] actitud, carácter o sentido esencial que define a un grupo, una época o un texto
     - example: Hay que respetar el espíritu de la ley, no solo su letra.
     - frames: espíritu de + sustantivo

### 75. voto

**Lexeme 1: voto**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈbo.to/ (es-ES), /ˈbo.to/ (es-MX)
  1. [B1] decisión que una persona expresa al elegir entre candidatos u opciones
     - example: Mi voto en estas elecciones será para el candidato más joven.
     - frames: voto a favor de algo; voto en contra de algo; derecho a voto
  2. [C1] promesa que se hace a Dios, sobre todo al entrar en una orden religiosa
     - example: Las monjas hacen voto de pobreza al entrar en el convento.
     - frames: hacer voto de algo; votos de castidad
     - labels: domain religion
  3. [C1] deseo bueno que se expresa a alguien, normalmente en plural
     - example: Hacemos votos por tu pronta recuperación después de la operación.
     - frames: hacer votos por algo
     - labels: formality formal

### 76. deporte

**Lexeme 1: deporte**, NOUN
- grammar: gender Masc; countability Both
- said: /de.ˈpoɾ.te/ (es-ES), /de.ˈpoɾ.te/ (es-MX)
  1. [A1] actividad física con reglas que se practica como juego o competición
     - example: El baloncesto es el deporte que más me gusta.
     - frames: practicar un deporte
     - grammar: countability Count
  2. [A2] ejercicio físico que se hace por salud o por placer
     - example: Hago deporte tres veces por semana para estar en forma.
     - frames: hacer deporte; practicar deporte
     - grammar: countability Mass

### 77. nivelar

**Lexeme 1: nivelar**, VERB
- grammar: conjugation 1; irregular False; valency Tr
- said: /ni.be.ˈlaɾ/ (es-ES), /ni.be.ˈlaɾ/ (es-MX)
  1. [B2] dejar una superficie plana y horizontal, sin partes más altas ni más bajas
     - example: Antes de poner el suelo hay que nivelar bien el terreno.
     - frames: nivelar algo
     - grammar: valency Tr
  2. [B2] hacer que dos o más cosas queden iguales en cantidad, valor o importancia
     - example: El gobierno quiere nivelar los sueldos de hombres y mujeres.
     - frames: nivelar algo; nivelar algo con algo; nivelarse
     - grammar: valency Both; note also nivelarse, to become equal
- relations: derived_from nivel (NOUN)

### 78. conjunto

**Lexeme 1: conjunto**, NOUN
- grammar: gender Masc; countability Count
- said: /kon.ˈxun.to/ (es-ES), /kon.ˈxun.to/ (es-MX)
  1. [B1] grupo de personas o cosas que forman un todo
     - example: El gobierno anunció ayer un conjunto de medidas contra la crisis.
     - frames: conjunto de + sust.; en conjunto
  2. [B1] grupo de músicos que tocan o cantan habitualmente
     - example: Mi hermano toca la guitarra en un conjunto de rock.
  3. [B1] prendas de vestir que se llevan combinadas
     - example: ¿Te gusta este conjunto de falda y chaqueta azul?
  4. [B2] equipo deportivo, sobre todo en el lenguaje de los periódicos
     - example: El conjunto local ganó el partido en el último minuto.
     - labels: domain sport
  5. [B2] colección de elementos considerada como un único objeto matemático
     - example: Para mañana tenéis que estudiar la unión de dos conjuntos.
     - labels: domain mathematics
**Lexeme 2: conjunto**, ADJ
- grammar: comparable False
- said: /kon.ˈxun.to/ (es-ES), /kon.ˈxun.to/ (es-MX)
  1. [B2] hecho o compartido entre dos o más personas o grupos
     - example: Ambos países firmaron una declaración conjunta sobre el clima.
     - opposites: individual

### 79. cuanto

**Lexeme 1: cuanto**, PRON
- grammar: note relative; agrees in gender and number: cuanta, cuantos, cuantas
- said: /ˈkwan.to/ (es-ES), /ˈkwan.to/ (es-MX)
  1. [B2] todo lo que, o todas las personas o cosas que
     - example: Le conté cuanto sabía sobre aquel asunto tan extraño.
     - frames: todo cuanto + verbo; cuantos + verbo
     - labels: style literary
- relations: confusable cuánto (PRON): cuánto, with an accent, is the question and exclamation word
**Lexeme 2: cuanto**, DET
- grammar: note relative; agrees with the noun: cuanta, cuantos, cuantas
- said: /ˈkwan.to/ (es-ES), /ˈkwan.to/ (es-MX)
  1. [B2] todos los o todas las que, en la cantidad que sea
     - example: Puedes coger cuantas galletas quieras de la caja.
     - frames: cuantos + sustantivo + verbo
- relations: confusable cuánto (DET): cuánto, with an accent, is the question and exclamation word
**Lexeme 3: cuanto**, ADV
- grammar: invariable True
- said: /ˈkwan.to/ (es-ES), /ˈkwan.to/ (es-MX)
  1. [B1] con más o menos, indica que dos cosas aumentan o disminuyen juntas
     - example: Cuanto más practiques, mejor hablarás el idioma.
     - frames: cuanto más/menos..., más/menos...; cuanto mejor/peor..., mejor/peor...
- relations: confusable cuánto (ADV): cuánto, with an accent, is the question and exclamation word

### 80. través

**Lexeme 1: través**, NOUN
- grammar: gender Masc; countability Count; note used almost only in the phrases a través de and de través
- said: /tɾa.ˈbes/ (es-ES), /tɾa.ˈbes/ (es-MX)
  1. [B1] dirección o posición que cruza algo de un lado a otro
     - example: Miramos el paisaje a través de la ventana del tren.
     - frames: a través de algo; de través

### 81. iniciativa

**Lexeme 1: iniciativa**, NOUN
- grammar: gender Fem; countability Both
- said: /i.ni.θja.ˈti.ba/ (es-ES), /i.ni.sja.ˈti.ba/ (es-MX)
  1. [B1] idea o plan que alguien propone para conseguir o mejorar algo
     - example: El ayuntamiento apoya una iniciativa para plantar árboles en el barrio.
     - frames: iniciativa para + inf; por iniciativa de algn
     - grammar: countability Count
  2. [B2] capacidad de decidir y actuar sin esperar a que otros lo manden
     - example: Buscamos personas con iniciativa y ganas de aprender.
     - frames: tener iniciativa
     - grammar: countability Mass
  3. [B2] primer paso que da alguien antes que los demás en un asunto
     - example: Si nadie toma la iniciativa, nunca resolveremos este problema.
     - frames: tomar la iniciativa; llevar la iniciativa
     - grammar: countability Mass

### 82. debajo

**Lexeme 1: debajo**, ADV
- grammar: invariable True
- said: /de.ˈba.xo/ (es-ES), /de.ˈba.xo/ (es-MX)
  1. [A1] en un lugar inferior a otra cosa, a menudo cubierto por ella
     - example: El gato duerme debajo de la cama todas las tardes.
     - frames: debajo de algo
     - opposites: encima
  2. [B1] en un nivel, cantidad o categoría menor que otro
     - example: Esta noche las temperaturas estarán por debajo de cero.
     - frames: por debajo de algo
     - labels: figurative True
     - opposites: encima
- relations: confusable abajo (ADV): both mean 'below'; debajo usually takes de + noun (under something), abajo is used alone or for direction (downstairs, down)

### 83. ridiculez

**Lexeme 1: ridiculez**, NOUN
- grammar: gender Fem; countability Both; note plural ridiculeces
- said: /ri.di.ku.ˈleθ/ (es-ES), /ri.di.ku.ˈles/ (es-MX)
  1. [B2] dicho o hecho absurdo que provoca risa o desprecio
     - example: Deja de decir ridiculeces y ayúdame con la mudanza.
     - frames: decir ridiculeces; ser una ridiculez
     - grammar: countability Count
  2. [B2] cosa o cantidad muy pequeña, de poco valor o importancia
     - example: Me cobraron una ridiculez por arreglar la bicicleta.
     - frames: una ridiculez
     - grammar: countability Count
  3. [C1] cualidad de lo que es tan absurdo o extravagante que provoca risa
     - example: La ridiculez de la situación nos hizo reír a todos.
     - frames: la ridiculez de algo
     - grammar: countability Mass
- relations: abstract_noun_of ridículo (ADJ)

### 84. siempre

**Lexeme 1: siempre**, ADV
- grammar: invariable True
- said: /ˈsjem.pɾe/ (es-ES), /ˈsjem.pɾe/ (es-MX)
  1. [A1] en todo momento, cada vez o sin excepción en el tiempo
     - example: Mi abuela siempre desayuna café con pan tostado.
     - opposites: nunca
  2. [B2] al final, después de dudar o de cambiar de idea
     - example: ¿Siempre sí vienes con nosotros al concierto mañana?
     - frames: siempre sí; siempre no
     - labels: formality informal
     - regions: es-MX

### 85. limitante

**Lexeme 1: limitante**, ADJ
- grammar: note same form for masculine and feminine; plural limitantes
- said: /li.mi.ˈtan.te/ (es-ES), /li.mi.ˈtan.te/ (es-MX)
  1. [C1] que pone límites a algo o impide que crezca o avance
     - example: La falta de agua es un factor limitante para la agricultura.
- relations: derived_from limitar (VERB)
**Lexeme 2: limitante**, NOUN
- grammar: countability Count; note masculine or feminine: el limitante or la limitante
- said: /li.mi.ˈtan.te/ (es-ES), /li.mi.ˈtan.te/ (es-MX)
  1. [C1] cosa que impide o dificulta hacer algo o avanzar
     - example: Para muchas familias, el precio sigue siendo una limitante importante.
     - frames: una limitante para algo
     - regions: es-419 mainly
- relations: derived_from limitar (VERB)

### 86. atrayente

**Lexeme 1: atrayente**, ADJ
- grammar: comparable True; note same form for masculine and feminine; plural atrayentes
- said: /a.tɾa.ˈʝen.te/ (es-ES), /a.tɾa.ˈʝen.te/ (es-MX)
  1. [B2] que despierta interés o deseo y hace que alguien se acerque
     - example: La oferta de trabajo resultaba muy atrayente para los jóvenes.
     - frames: atrayente para alguien
- relations: derived_from atraer (VERB)

### 87. subdivisión

**Lexeme 1: subdivisión**, NOUN
- grammar: gender Fem; countability Both
- said: /sub.di.bi.ˈsjon/ (es-ES), /sub.di.bi.ˈsjon/ (es-MX)
  1. [C1] cada una de las partes menores en que se separa otra parte de un todo
     - example: Cada provincia tiene varias subdivisiones con sus propias autoridades locales.
     - frames: subdivisión de algo
     - grammar: countability Count
  2. [C1] separación en partes más pequeñas de algo que ya es una parte
     - example: La subdivisión del terreno en parcelas tardó casi dos años.
     - frames: subdivisión de algo en algo
     - grammar: countability Mass
- relations: verbal_noun_of subdividir (VERB)

### 88. tradicional

**Lexeme 1: tradicional**, ADJ
- grammar: comparable True
- said: /tɾa.di.θjo.ˈnal/ (es-ES), /tɾa.di.sjo.ˈnal/ (es-MX)
  1. [A2] que viene de las costumbres antiguas de un pueblo o país
     - example: En esta fiesta todos llevan la ropa tradicional del pueblo.
  2. [B1] que sigue las ideas o maneras de antes y no las nuevas
     - example: Mis abuelos son bastante tradicionales y no aceptan esos cambios.
     - opposites: moderno
- relations: adjective_of tradición (NOUN)

### 89. edificio

**Lexeme 1: edificio**, NOUN
- grammar: gender Masc; countability Count
- said: /e.di.ˈfi.θjo/ (es-ES), /e.di.ˈfi.sjo/ (es-MX)
  1. [A1] construcción grande con paredes, techo y pisos, donde vive o trabaja la gente
     - example: Mi oficina está en el último piso de ese edificio.

### 90. vulva

**Lexeme 1: vulva**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈbul.ba/ (es-ES), /ˈbul.ba/ (es-MX)
  1. [C1] parte externa de los órganos sexuales de la mujer y de las hembras de los mamíferos
     - example: La ginecóloga revisó la vulva de la paciente durante la consulta.
     - labels: domain anatomy

### 91. napo

**Lexeme 1: napo**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈna.po/ (es-ES), /ˈna.po/ (es-MX)
  1. [C2] billete de mil pesetas, la antigua moneda de España
     - example: Mi abuelo guardaba siempre un napo doblado dentro de la cartera.
     - labels: formality informal, time dated
     - regions: es-ES only

### 92. arriba

**Lexeme 1: arriba**, ADV
- grammar: invariable True
- said: /a.ˈri.ba/ (es-ES), /a.ˈri.ba/ (es-MX)
  1. [A1] en un lugar más alto o en la parte superior
     - example: Los vasos limpios están arriba, en el último estante.
     - frames: de arriba; arriba de algo
     - opposites: abajo
  2. [A2] hacia un lugar más alto
     - example: Mira hacia arriba y verás todas las estrellas.
     - frames: hacia arriba; calle arriba
     - opposites: abajo
  3. [B2] en un nivel superior de mando o de importancia
     - example: Nosotros solo cumplimos las órdenes que llegan de arriba.
     - labels: figurative True
     - opposites: abajo
  4. [B2] en una parte anterior de un mismo texto
     - example: Como se explica más arriba, el plazo termina en mayo.
     - frames: más arriba
     - opposites: abajo
**Lexeme 2: arriba**, INTJ
- said: /a.ˈri.ba/ (es-ES), /a.ˈri.ba/ (es-MX)
  1. [A2] expresión para animar a alguien o pedirle que se levante
     - example: ¡Arriba, chicos, que ya son las siete!
- relations: derived_from arriba (ADV)

### 93. fiscalidad

**Lexeme 1: fiscalidad**, NOUN
- grammar: gender Fem; countability Mass
- said: /fis.ka.li.ˈdad/ (es-ES), /fis.ka.li.ˈdad/ (es-MX)
  1. [C1] conjunto de leyes y normas que regulan los impuestos
     - example: El nuevo gobierno quiere reformar la fiscalidad de las pequeñas empresas.
  2. [C1] conjunto de impuestos que pagan las personas y las empresas
     - example: En esta región la fiscalidad sobre la vivienda es bastante alta.
     - frames: la fiscalidad sobre algo
- relations: derived_from fiscal (ADJ)

### 94. además

**Lexeme 1: además**, ADV
- grammar: invariable True
- said: /a.de.ˈmas/ (es-ES), /a.de.ˈmas/ (es-MX)
  1. [A2] también, por otra parte; para añadir algo a lo ya dicho
     - example: Además de ser barato, el piso está muy cerca del centro.
     - frames: además de + sust./inf.; además, + oración

### 95. cielo

**Lexeme 1: cielo**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈθje.lo/ (es-ES), /ˈsje.lo/ (es-MX)
  1. [A1] espacio azul sobre la tierra donde se ven las nubes y las estrellas
     - example: Esta mañana el cielo estaba despejado y muy azul.
     - opposites: tierra
  2. [A2] lugar donde, según algunas religiones, están Dios y los buenos tras morir
     - example: Mi abuela siempre decía que los niños buenos van al cielo.
     - labels: domain religion
     - opposites: infierno
  3. [B1] palabra cariñosa para llamar a una persona muy querida
     - example: Ven aquí, cielo, que te quiero dar un abrazo.
     - labels: formality informal, attitude endearing

### 96. acceso

**Lexeme 1: acceso**, NOUN
- grammar: gender Masc; countability Both
- said: /ak.ˈθe.so/ (es-ES), /ak.ˈse.so/ (es-MX)
  1. [B1] posibilidad o permiso de entrar en un lugar o de llegar a él
     - example: El acceso al edificio está prohibido sin tarjeta.
     - frames: acceso a algo
  2. [B1] camino, puerta o lugar por donde se entra a un sitio
     - example: Los accesos a la ciudad siguen cortados por la nieve.
     - grammar: countability Count
  3. [B1] posibilidad de obtener o usar algo, como información o servicios
     - example: Todos los niños deberían tener acceso a una buena educación.
     - frames: tener acceso a algo
     - grammar: countability Mass
  4. [C1] ataque repentino y breve de tos, de un sentimiento o de una enfermedad
     - example: Le dio un acceso de tos en mitad del concierto.
     - frames: acceso de algo
     - grammar: countability Count
- relations: derived_from acceder (VERB)

### 97. regreso

**Lexeme 1: regreso**, NOUN
- grammar: gender Masc; countability Count
- said: /re.ˈɡɾe.so/ (es-ES), /re.ˈɡɾe.so/ (es-MX)
  1. [A2] vuelta al lugar de donde se salió o a una situación anterior
     - example: De regreso a casa compramos pan para la cena.
     - frames: regreso a + lugar; de regreso
     - opposites: ida
- relations: derived_from regresar (VERB)

### 98. lonja

**Lexeme 1: lonja**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈlon.xa/ (es-ES), /ˈlon.xa/ (es-MX)
  1. [B1] trozo ancho y delgado que se corta de un alimento, sobre todo de carne
     - example: Para el bocadillo corta un par de lonjas de jamón.
     - frames: una lonja de algo
  2. [B2] edificio público donde se compran y venden mercancías al por mayor
     - example: Cada mañana los pescadores llevan sus capturas a la lonja del puerto.
     - frames: lonja de pescado
     - regions: es-ES mainly
  3. [B2] pliegue de grasa que se forma en la cintura o el vientre
     - example: Después de las fiestas me salieron unas lonjas en la cintura.
     - labels: formality informal
     - regions: es-MX mainly
     - grammar: note usually in the plural

### 99. cotidianamente

**Lexeme 1: cotidianamente**, ADV
- grammar: invariable True
- said: /ko.ti.ˌdja.na.ˈmen.te/ (es-ES), /ko.ti.ˌdja.na.ˈmen.te/ (es-MX)
  1. [B2] todos los días o de manera habitual, como parte de la vida normal
     - example: Usamos el transporte público cotidianamente para ir al trabajo.
- relations: adverb_of cotidiano (ADJ)

### 100. morir

**Lexeme 1: morir**, VERB
- grammar: conjugation 3; irregular True; valency Intr; note o>ue/u stem change; often used as morirse
- said: /mo.ˈɾiɾ/ (es-ES), /mo.ˈɾiɾ/ (es-MX)
- forms: muero [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; mueres [Mood=Ind, Tense=Pres, Person=2, Number=Sing, VerbForm=Fin]; muere [Mood=Ind, Tense=Pres, Person=3, Number=Sing, VerbForm=Fin]; morimos [Mood=Ind, Tense=Pres,Past, Person=1, Number=Plur, VerbForm=Fin]; mueren [Mood=Ind, Tense=Pres, Person=3, Number=Plur, VerbForm=Fin]; morí [Mood=Ind, Tense=Past, Person=1, Number=Sing, VerbForm=Fin]; murió [Mood=Ind, Tense=Past, Person=3, Number=Sing, VerbForm=Fin]; moriré [Mood=Ind, Tense=Fut, Person=1, Number=Sing, VerbForm=Fin]; muera [Mood=Sub, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; muerto [VerbForm=Part, Tense=Past, Gender=Masc, Number=Sing]; muriendo [VerbForm=Ger]
  1. [A2] dejar de vivir una persona, un animal o una planta
     - example: Su abuela murió tranquila a los noventa años.
     - frames: morir de algo; morirse
     - opposites: nacer
  2. [B1] sentir algo con muchísima fuerza, como hambre, risa o deseo
     - example: ¡Me muero de hambre! ¿Cuándo vamos a comer?
     - frames: morirse de algo; morirse por algo; morirse por hacer algo
     - labels: figurative True
  3. [B2] llegar algo poco a poco a su final
     - example: Cuando muere la tarde, las calles del pueblo se quedan vacías.
     - labels: figurative True, style literary
