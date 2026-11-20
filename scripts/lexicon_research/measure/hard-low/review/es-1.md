# Spanish: two entries for each word (11 words)

Each word below has two entries of a learner's dictionary of Spanish, A and B, written by two different writers
under the same rules. You are not told which writer wrote which, and the sides change from word to word. Some of
these words are hard on purpose: two words under one spelling, forms that are not words of their own, senses of
one country only, rude words.

For each word say which entry serves a learner better, and name the faults of each. A writer may have refused a
word as no dictionary word of its own ("refused: inflected form of ..."); that is right for an inflected form, an
abbreviation or a name, and wrong for a word a dictionary of Spanish lists.

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

One line of JSON for every word, in the order given, inside a single ```json block:

```json
{"id": 1, "better": "A", "why": "B lacks the metal, the commoner of the two words spelt lead", "A": [], "B": [{"fault": "missing-core", "where": "lexemes", "what": "the metal is missing"}]}
```

"better" is "A", "B" or "same": "same" only when you could not say which you would rather give a learner. "why" is
one sentence. "A" and "B" list each entry's faults as above, empty when it has none.

## The words

### 1. capital

#### Entry A

**Lexeme 1: capital**, NOUN
- grammar: gender Fem; countability Count
- said: /ka.pi.ˈtal/ (es-ES), /ka.pi.ˈtal/ (es-MX)
  1. [A2] ciudad donde están el gobierno de un país o de una región
     - example: Mis abuelos se mudaron a la capital cuando eran muy jóvenes.
     - frames: capital de algo
     - collocations: capital de provincia; capital del país; capital federal
  2. [B2] ciudad o lugar más importante en una actividad determinada
     - example: Durante una semana, esta ciudad se convierte en la capital mundial del cine.
     - frames: capital de algo
     - collocations: capital mundial; capital europea
     - labels: figurative True
**Lexeme 2: capital**, NOUN
- grammar: gender Masc; countability Mass, mainly
- said: /ka.pi.ˈtal/ (es-ES), /ka.pi.ˈtal/ (es-MX)
  1. [B2] dinero o bienes que se tienen o se invierten para obtener ganancias
     - example: Necesitamos más capital para abrir una segunda tienda el año próximo.
     - collocations: invertir capital; capital inicial; capital social; fuga de capitales; ampliación de capital; capital riesgo; capital privado
  2. [C1] conjunto de personas y empresas que poseen el dinero y los medios de producción
     - example: Aquel conflicto enfrentó durante años al capital y a los trabajadores.
     - labels: domain economics
     - grammar: number Sing
  3. [C1] conjunto de recursos o cualidades valiosas que tiene una persona o un grupo
     - example: La empresa considera que su mayor riqueza es el capital humano.
     - collocations: capital humano; capital político; capital cultural
     - labels: figurative True
     - grammar: countability Mass
**Lexeme 3: capital**, ADJ
- grammar: comparable False; note same form for masculine and feminine; plural capitales
- said: /ka.pi.ˈtal/ (es-ES), /ka.pi.ˈtal/ (es-MX)
  1. [C1] principal, de mucha importancia para algo
     - example: Este acuerdo tiene una importancia capital para el futuro de la región.
     - collocations: de capital importancia; pecado capital; papel capital
  2. [C1] que se castiga con la muerte o que la impone como castigo
     - example: En muchos países ya no existe la pena capital.
     - collocations: pena capital; delito capital
     - labels: domain law
     - grammar: position Attr

#### Entry B

**Lexeme 1: capital**, NOUN
- grammar: gender Fem; countability Count
- said: /ka.pi.ˈtal/ (es-ES), /ka.pi.ˈtal/ (es-MX)
  1. [A1] ciudad principal donde está el gobierno de un país, una región o una provincia
     - example: Viajamos en tren hasta la capital para visitar el parlamento.
     - frames: capital de algo
     - collocations: la capital del país; la capital de la provincia; vivir en la capital
  2. [B1] ciudad o lugar que destaca más que otros en una actividad o un campo
     - example: Esa pequeña ciudad se ha convertido en la capital mundial del cine.
     - frames: capital de algo
     - collocations: capital cultural; capital financiera; capital mundial
**Lexeme 2: capital**, NOUN
- grammar: gender Masc; countability Mass, mainly
- said: /ka.pi.ˈtal/ (es-ES), /ka.pi.ˈtal/ (es-MX)
  1. [B1] dinero y conjunto de bienes que posee una persona o una empresa para invertir
     - example: Necesitamos reunir suficiente capital antes de abrir el restaurante.
     - collocations: invertir capital; aportar capital; acumular capital; capital extranjero; capital privado; capital inicial; fuga de capitales
  2. [C1] conjunto de recursos o valores no materiales que dan ventaja a alguien o algo
     - example: La confianza de los vecinos es el mayor capital de este comercio.
     - collocations: capital humano; capital social; capital político
     - labels: figurative True
     - grammar: countability Mass
**Lexeme 3: capital**, ADJ
- grammar: comparable True; note same form for masculine and feminine
- said: /ka.pi.ˈtal/ (es-ES), /ka.pi.ˈtal/ (es-MX)
  1. [B2] muy importante, principal o decisivo para un asunto
     - example: La educación temprana tiene una importancia capital en el desarrollo infantil.
     - collocations: importancia capital; cuestión capital; error capital; obra capital
  2. [C1] que conlleva la muerte como castigo o se considera de máxima gravedad moral
     - example: En aquel país abolieron la pena capital hace más de treinta años.
     - collocations: pena capital; pecado capital; enemigo capital
     - grammar: position Attr; comparable False

### 2. cura

#### Entry A

**Lexeme 1: cura**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈku.ɾa/ (es-ES), /ˈku.ɾa/ (es-MX)
  1. [B1] sacerdote de la Iglesia católica, sobre todo el que atiende una parroquia
     - example: El cura del pueblo celebra la misa todos los domingos.
     - collocations: cura párroco; el cura del pueblo; hacerse cura
**Lexeme 2: cura**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈku.ɾa/ (es-ES), /ˈku.ɾa/ (es-MX)
  1. [B1] remedio o tratamiento que hace desaparecer una enfermedad o un problema
     - example: Los científicos siguen buscando una cura para esta enfermedad.
     - frames: cura para algo; cura contra algo
     - collocations: no tener cura; tener cura; encontrar una cura; buscar una cura; cura de sueño; cura de reposo
  2. [B2] limpieza y tratamiento de una herida para que sane
     - example: La enfermera me hace la cura de la pierna cada mañana.
     - collocations: hacer una cura; hacer las curas

#### Entry B

**Lexeme 1: cura**, NOUN
- grammar: gender Masc; countability Count
- said: /ˈku.ɾa/ (es-ES), /ˈku.ɾa/ (es-MX)
  1. [A2] sacerdote de la Iglesia católica, sobre todo el que atiende una parroquia
     - example: El cura del pueblo celebró la boda en la iglesia antigua.
     - collocations: cura párroco; el señor cura
**Lexeme 2: cura**, NOUN
- grammar: gender Fem; countability Count
- said: /ˈku.ɾa/ (es-ES), /ˈku.ɾa/ (es-MX)
  1. [B1] tratamiento o remedio con que se devuelve la salud a un enfermo
     - example: Los científicos siguen buscando una cura eficaz para esta grave enfermedad.
     - frames: cura para algo; cura contra algo
     - collocations: tener cura; no tener cura; encontrar una cura; cura de reposo; cura de sueño
  2. [B2] cuidado que se aplica en una herida para limpiarla y protegerla
     - example: Tengo que ir al centro médico para hacerme la cura diaria.
     - collocations: hacer una cura; sala de curas
- relations: verbal_noun_of curar (VERB)

### 3. vino

#### Entry A

**Lexeme 1: vino**, NOUN
- grammar: gender Masc; countability Mass, mainly
- said: /ˈbi.no/ (es-ES), /ˈbi.no/ (es-MX)
  1. [A1] bebida con alcohol que se hace fermentando el jugo de la uva
     - example: Para la cena hemos comprado una botella de vino tinto.
     - collocations: vino tinto; vino blanco; vino rosado; vino espumoso; vino dulce; vino seco; una copa de vino; una botella de vino
**Lexeme 2: vino**, ADJ
- grammar: note same form for both genders; plural vino or vinos
- said: /ˈbi.no/ (es-ES), /ˈbi.no/ (es-MX)
- forms: vino [Number=Plur]
  1. [B2] de color rojo oscuro parecido al de la bebida hecha de uva
     - example: Para la fiesta llevé una chaqueta vino y zapatos negros.
     - collocations: color vino

#### Entry B

**Lexeme 1: vino**, NOUN
- grammar: gender Masc; countability Mass, mainly; note counted for a glass or a kind: un vino, vinos chilenos
- said: /ˈbi.no/ (es-ES), /ˈbi.no/ (es-MX)
  1. [A1] bebida alcohólica que se hace con el jugo de uvas fermentado
     - example: Para la cena de hoy compramos una botella de vino tinto.
     - collocations: vino tinto; vino blanco; vino rosado; copa de vino; botella de vino; vino de la casa; vino espumoso; catar un vino; tomar un vino

### 4. agua

#### Entry A

**Lexeme 1: agua**, NOUN
- grammar: gender Fem; note takes el and un immediately before it in the singular: el agua fría, las aguas
- said: /ˈa.ɡwa/ (es-ES), /ˈa.ɡwa/ (es-MX)
  1. [A1] líquido transparente, sin olor ni sabor, que forma los ríos, lagos y mares
     - example: Bebe un vaso de agua fría después de hacer ejercicio.
     - collocations: agua potable; agua dulce; agua salada; agua corriente; agua mineral; agua con gas; agua sin gas; un vaso de agua
     - grammar: countability Mass, mainly
  2. [B1] masa de este líquido que cubre una parte de la tierra
     - example: Las aguas del río bajaban muy sucias tras la tormenta.
     - collocations: aguas territoriales; aguas residuales
     - grammar: countability Count; number Plur, mainly
  3. [B1] lluvia que cae de las nubes sobre la tierra
     - example: Parece que esta tarde va a caer mucha agua.
     - labels: formality informal
     - grammar: countability Mass
**Lexeme 2: agua**, INTJ
- said: /ˈa.ɡwa/ (es-ES), /ˈa.ɡwa/ (es-MX)
  1. [B2] se usa para avisar de un peligro o pedir cuidado
     - example: Oye, ¡agua!, que viene un coche muy rápido por detrás.
     - labels: formality informal
     - regions: es-419 mainly

#### Entry B

**Lexeme 1: agua**, NOUN
- grammar: gender Fem; note feminine, but takes el and un in the singular: el agua fría, las aguas
- said: /ˈa.ɡwa/ (es-ES), /ˈa.ɡwa/ (es-MX)
  1. [A1] líquido transparente, sin color, olor ni sabor, que forma ríos, mares y lluvia
     - example: Bebe más agua cuando hace tanto calor en verano.
     - collocations: agua potable; agua mineral; agua del grifo; agua con gas; agua dulce; agua salada; un vaso de agua; agua caliente; agua fresca
     - grammar: countability Mass
  2. [B2] parte de un mar, río o lago, o zona del mar que controla un país
     - example: El barco pesquero entró en aguas de otro país sin permiso.
     - collocations: aguas territoriales; aguas internacionales; aguas profundas; aguas tranquilas
     - grammar: number Plur; countability Count

### 5. problema

#### Entry A

**Lexeme 1: problema**, NOUN
- grammar: gender Masc; countability Count; note masculine despite ending in -a: el problema, un problema
- said: /pɾo.ˈble.ma/ (es-ES), /pɾo.ˈble.ma/ (es-MX)
  1. [A1] asunto o situación difícil que causa preocupación o exige buscar una solución
     - example: Tenemos un problema grave con la calefacción de la oficina.
     - frames: problema con algo/alguien; problema de algo; problema para + inf
     - collocations: resolver un problema; solucionar un problema; tener un problema; causar un problema; sin problema; no hay problema; un problema grave; meterse en problemas
  2. [B1] cuestión dudosa o difícil que se intenta aclarar mediante el estudio o la reflexión
     - example: El libro analiza los grandes problemas morales de nuestra época.
     - frames: problema de algo
     - collocations: plantear un problema; abordar un problema
  3. [A2] ejercicio de matemáticas o ciencias en que se calcula un dato desconocido
     - example: No consigo resolver el último problema del examen de matemáticas.
     - frames: problema de algo
     - collocations: resolver un problema; plantear un problema; un problema de matemáticas

#### Entry B

**Lexeme 1: problema**, NOUN
- grammar: gender Masc; countability Count; note masculine although it ends in -a: el problema, los problemas
- said: /pɾo.ˈble.ma/ (es-ES), /pɾo.ˈble.ma/ (es-MX)
  1. [A1] situación difícil o molesta que hay que resolver o que causa preocupación
     - example: Tenemos un problema serio con la calefacción de la casa.
     - frames: problema con algo/alguien; problema para + inf; el problema es que + ind
     - collocations: tener un problema; resolver un problema; solucionar un problema; causar problemas; plantear un problema; meterse en problemas; sin problema; no hay problema; problema grave
  2. [A2] pregunta de matemáticas o de lógica que se resuelve a partir de unos datos
     - example: Nos han puesto diez problemas de matemáticas para mañana.
     - collocations: resolver un problema; problema de matemáticas; plantear un problema

### 6. gustar

#### Entry A

**Lexeme 1: gustar**, VERB
- grammar: conjugation 1; irregular False; note the thing liked is the subject and the person is an indirect object: me gusta, te gustan
- said: /ɡus.ˈtaɾ/ (es-ES), /ɡus.ˈtaɾ/ (es-MX)
  1. [A1] causar placer o agrado a una persona
     - example: A mis hijos les gusta mucho jugar en la playa.
     - frames: gustar a alguien; gustar + inf; gustar que + subj
     - collocations: gustar mucho; gustar más; no gustar nada; gustar muchísimo
     - grammar: valency Intr
     - opposites: desagradar
  2. [A2] provocar en una persona atracción física o amorosa
     - example: Creo que le gustas a mi prima desde hace tiempo.
     - frames: gustar a alguien
     - grammar: valency Intr
  3. [C1] tener afición a algo o disfrutar haciéndolo
     - example: Mi abuelo gustaba de pasear por el campo cada tarde.
     - frames: gustar de algo; gustar de + inf
     - labels: style literary
     - grammar: valency Intr; note the person who enjoys is the subject
  4. [B2] querer o desear algo, dicho para ofrecer o invitar con cortesía
     - example: Si usted gusta, puede esperar aquí sentado un momento.
     - collocations: si gusta; como guste
     - labels: formality formal
     - grammar: valency Both; note mostly in offers and in si gusta, como guste

#### Entry B

**Lexeme 1: gustar**, VERB
- grammar: conjugation 1; irregular False
- said: /ɡus.ˈtaɾ/ (es-ES), /ɡus.ˈtaɾ/ (es-MX)
  1. [A1] producir agrado o placer a alguien, o parecerle bien
     - example: A mi hermano le gustan mucho las películas de acción.
     - frames: gustar a alguien; gustar + inf; gustar que + subj
     - collocations: gustar mucho; gustar poco; no gustar nada
     - grammar: valency Intr; note usually with an indirect object pronoun for the person who likes something
     - opposites: disgustar
  2. [A2] sentir atracción romántica o física una persona hacia otra
     - example: Creo que le gustas a mi compañero de trabajo.
     - frames: gustar a alguien
     - grammar: valency Intr; note with an indirect object pronoun; often reciprocal in the plural: nos gustamos
  3. [C1] tener afición por una cosa o disfrutar haciéndola
     - example: Era un hombre tranquilo que gustaba de pasear en silencio.
     - frames: gustar de algo; gustar de + inf
     - labels: style literary
     - grammar: valency Intr
  4. [B2] desear o querer hacer algo, especialmente al ofrecer o invitar
     - example: Pueden pasar al comedor a sentarse cuando ustedes gusten.
     - frames: gustar + inf
     - collocations: si usted gusta; como gustes; cuando guste
     - labels: formality formal
     - grammar: valency Both
  5. [C1] probar una comida o una bebida para percibir su sabor
     - example: El cocinero gustó la salsa antes de servir los platos.
     - frames: gustar algo
     - labels: style literary
     - grammar: valency Tr
- relations: derived_from gusto (NOUN)

### 7. coger

#### Entry A

**Lexeme 1: coger**, VERB
- grammar: conjugation 2; irregular False; valency Both; note spelling change g to j before a and o: cojo, coja
- said: /ko.ˈxeɾ/ (es-ES), /ko.ˈxeɾ/ (es-MX)
- forms: cojo [Mood=Ind, Number=Sing, Person=1, Tense=Pres, VerbForm=Fin]; coja [Mood=Sub, Number=Sing, Person=1,3, Tense=Pres, VerbForm=Fin]
  1. [A1] tomar o sujetar algo o a alguien con la mano o con otro medio
     - example: Coge el paraguas antes de salir de casa, por favor.
     - frames: coger algo; coger a alguien
     - collocations: coger en brazos; coger de la mano; coger el teléfono
     - regions: es-ES mainly
     - grammar: valency Tr
     - opposites: soltar
  2. [A1] subir a un medio de transporte para viajar o elegir un camino
     - example: Todas las mañanas cojo el autobús de las ocho.
     - frames: coger algo
     - collocations: coger el autobús; coger el tren; coger un taxi; coger un avión
     - regions: es-ES mainly
     - grammar: valency Tr
  3. [A2] contraer una enfermedad o pasar a tener cierto estado físico o ánimo
     - example: Abrígate bien para no coger un resfriado en la calle.
     - frames: coger algo
     - collocations: coger frío; coger un resfriado; coger sueño; coger miedo; coger cariño
     - regions: es-ES mainly
     - grammar: valency Tr
  4. [B1] atrapar a alguien o algo que huye, o sorprenderlo haciendo algo
     - example: La policía cogió a los ladrones cerca de la estación.
     - frames: coger a alguien; coger algo
     - collocations: coger desprevenido; coger por sorpresa; coger en una mentira
     - regions: es-ES mainly
     - grammar: valency Tr
  5. [B1] anotar por escrito o grabar datos, palabras o una imagen
     - example: Espera un momento, que te cojo el número de teléfono.
     - frames: coger algo
     - collocations: coger apuntes; coger nota
     - regions: es-ES mainly
     - grammar: valency Tr
  6. [B2] tener espacio o capacidad suficiente para que algo entre o se aloje
     - example: En esa maleta tan pequeña no coge toda mi ropa.
     - frames: coger en + lugar
     - labels: formality informal
     - regions: es-ES mainly
     - grammar: valency Intr
  7. [B2] mantener relaciones sexuales con alguien
     - example: Dicen que esos dos cogieron después de la fiesta.
     - frames: coger con alguien; coger a alguien
     - labels: formality informal, attitude vulgar
     - regions: es-419 mainly
     - grammar: valency Both

#### Entry B

**Lexeme 1: coger**, VERB
- grammar: conjugation 2; irregular False; note g becomes j before a and o: cojo, coja; in much of Latin America tomar or agarrar is preferred
- said: /ko.ˈxeɾ/ (es-ES), /ko.ˈxeɾ/ (es-MX)
- forms: cojo [Mood=Ind, Tense=Pres, Person=1, Number=Sing, VerbForm=Fin]; coja [Mood=Sub, Tense=Pres, Person=1,3, Number=Sing, VerbForm=Fin]
  1. [A2] tomar o sujetar algo o a alguien con la mano
     - example: Coge la bolsa, que yo llevo las llaves y el paraguas.
     - frames: coger algo; coger a alguien de algo
     - collocations: coger de la mano; coger en brazos; coger el teléfono; coger fruta
     - regions: es-ES mainly
     - grammar: valency Tr
  2. [A2] subir a un medio de transporte para viajar en él
     - example: Mañana cogemos el tren de las ocho para llegar a tiempo.
     - frames: coger algo
     - collocations: coger el autobús; coger un taxi; coger el metro; coger un avión; coger el tren
     - regions: es-ES mainly
     - grammar: valency Tr
  3. [B1] alcanzar o atrapar a una persona o un animal que huye
     - example: Los perros no consiguieron coger al zorro en el bosque.
     - frames: coger a alguien
     - grammar: valency Tr
  4. [B1] empezar a tener una enfermedad, un sentimiento o una costumbre
     - example: Si sales sin abrigo, vas a coger un resfriado.
     - frames: coger algo
     - collocations: coger frío; coger un resfriado; coger cariño; coger miedo; coger la costumbre; coger el gusto
     - grammar: valency Tr
  5. [B2] encontrar a alguien en un momento o una situación inesperados
     - example: La tormenta nos cogió en mitad del camino de vuelta.
     - frames: coger a alguien
     - collocations: coger desprevenido; coger por sorpresa; coger con las manos en la masa
     - grammar: valency Tr
  6. [B2] entender el sentido de lo que alguien dice o hace
     - example: Perdona, no he cogido bien lo que acabas de decir.
     - frames: coger algo
     - collocations: coger el chiste; coger la idea
     - labels: formality informal
     - regions: es-ES mainly
     - grammar: valency Tr
  7. [C1] tener relaciones sexuales con una persona
     - example: En esa película los personajes solo piensan en coger.
     - labels: attitude vulgar
     - regions: es-MX mainly, es-AR mainly, es-VE mainly, es-013 mainly
     - grammar: valency Both

### 8. ordenador

#### Entry A

**Lexeme 1: ordenador**, NOUN
- grammar: gender Masc; countability Count
- said: /oɾ.de.na.ˈdoɾ/ (es-ES), /oɾ.de.na.ˈdoɾ/ (es-MX)
  1. [A1] máquina electrónica que guarda y procesa información siguiendo programas
     - example: Necesito un ordenador nuevo porque el mío ya va muy lento.
     - collocations: ordenador portátil; ordenador de sobremesa; encender el ordenador; apagar el ordenador; trabajar con el ordenador; delante del ordenador
     - regions: es-ES mainly
- relations: derived_from ordenar (VERB)
**Lexeme 2: ordenador**, ADJ
- grammar: position Attr, mainly; comparable False; note feminine ordenadora; plural ordenadores, ordenadoras
- said: /oɾ.de.na.ˈdoɾ/ (es-ES), /oɾ.de.na.ˈdoɾ/ (es-MX)
  1. [C1] que pone en orden o da una organización a algo
     - example: La justicia es el principio ordenador de toda nuestra sociedad.
     - collocations: principio ordenador; criterio ordenador; eje ordenador
     - labels: style elevated
- relations: derived_from ordenar (VERB)

#### Entry B

**Lexeme 1: ordenador**, NOUN
- grammar: gender Masc; countability Count
- said: /oɾ.de.na.ˈdoɾ/ (es-ES), /oɾ.de.na.ˈdoɾ/ (es-MX)
  1. [A1] máquina electrónica que recibe, guarda y procesa datos según programas informáticos
     - example: Se me ha estropeado el ordenador y no puedo terminar el informe.
     - collocations: ordenador portátil; ordenador personal; ordenador de sobremesa; encender el ordenador; apagar el ordenador; programa de ordenador
     - regions: es-ES mainly
- relations: derived_from ordenar (VERB)
**Lexeme 2: ordenador**, ADJ
- grammar: comparable False
- said: /oɾ.de.na.ˈdoɾ/ (es-ES), /oɾ.de.na.ˈdoɾ/ (es-MX)
  1. [C1] que pone en orden, organiza o establece reglas en un conjunto
     - example: La nueva ley introduce un criterio ordenador para el gasto público.
     - collocations: principio ordenador; criterio ordenador
     - labels: formality formal
- relations: adjective_of ordenar (VERB)

### 9. vos

#### Entry A

**Lexeme 1: vos**, PRON
- grammar: invariable True; note used as subject and after prepositions; object pronoun te, possessive tu/tuyo
- said: /ˈbos/ (es-ES), /ˈbos/ (es-MX)
  1. [A1] se usa para dirigirse con confianza a la persona con quien se habla
     - example: ¿Vos también venís con nosotros a la fiesta de mañana?
     - collocations: a vos; con vos; para vos; por vos
     - labels: politeness familiar
     - regions: es-AR only, es-UY only, es-PY only, es-013 mainly, es-CO mainly
  2. [C1] se usa para dirigirse con sumo respeto a una autoridad o persona importante
     - example: Como vos ordenéis, señor, saldremos al amanecer hacia la frontera.
     - labels: politeness polite, time archaic, style literary, elevated
     - grammar: note takes second-person plural verb forms, object pronoun os and possessive vuestro
- relations: confusable voz (NOUN): pronounced the same in Latin America and parts of Spain (seseo)

#### Entry B

**Lexeme 1: vos**, PRON
- grammar: invariable True; note used as subject and after prepositions; object pronoun te, possessive tu
- said: /ˈbos/ (es-ES), /ˈbos/ (es-MX)
  1. [A2] pronombre de segunda persona del singular, usado para hablar a alguien con confianza
     - example: Vos sabés muy bien lo que pasó anoche en la fiesta.
     - collocations: con vos; para vos
     - labels: politeness familiar
     - regions: es-AR only, es-UY only, es-PY only, es-013 only, es-CO only, es-BO only
     - grammar: note takes its own verb forms: vos tenés, vos sos, vos vivís
  2. [C1] pronombre usado antiguamente para dirigirse con respeto a una persona
     - example: Señor, vos sois mi única esperanza en este mundo.
     - labels: time archaic, style literary, politeness polite
     - grammar: note takes second-person plural verb forms: vos sois, vos tenéis

### 10. tío

#### Entry A

**Lexeme 1: tío**, NOUN
- grammar: gender Masc; countability Count; note feminine tía; plural tíos can mean uncle and aunt together
- said: /ˈti.o/ (es-ES), /ˈti.o/ (es-MX)
  1. [A1] hermano del padre o de la madre, o marido de la hermana de estos
     - example: Este verano vamos a visitar a mi tío en el pueblo.
     - collocations: tío abuelo; tío segundo; tío político; tío carnal
  2. [B1] hombre o persona cualquiera, sobre todo uno cuyo nombre no se sabe
     - example: En la puerta hay un tío que pregunta por tu hermano.
     - collocations: tío bueno; menudo tío; un tío majo
     - labels: formality informal
     - regions: es-ES mainly
  3. [B1] se usa para dirigirse con confianza a un amigo o para expresar sorpresa
     - example: Oye, tío, déjame tu cargador un momento, que no tengo batería.
     - labels: formality informal
     - regions: es-ES mainly
     - grammar: note used as a form of address, often without an article

#### Entry B

**Lexeme 1: tío**, NOUN
- grammar: gender Masc; countability Count; note feminine tía; plural tíos also means aunt and uncle
- said: /ˈti.o/ (es-ES), /ˈti.o/ (es-MX)
  1. [A1] hermano del padre o de la madre de una persona, o su marido
     - example: Vamos a cenar en casa de mis tíos este domingo.
     - collocations: tío abuelo; tío político; tío carnal
  2. [B1] persona cualquiera, o forma de dirigirse a alguien con mucha confianza
     - example: Ayer conocí a un tío muy simpático en el gimnasio.
     - collocations: tío bueno; un tío legal
     - labels: formality informal
     - regions: es-ES mainly

### 11. soler

#### Entry A

**Lexeme 1: soler**, VERB
- grammar: conjugation 2; irregular True; valency Intr; note defective: used mainly in the present and imperfect tenses
- said: /so.ˈleɾ/ (es-ES), /so.ˈleɾ/ (es-MX)
- forms: suelo [VerbForm=Fin, Mood=Ind, Tense=Pres, Person=1, Number=Sing]; sueles [VerbForm=Fin, Mood=Ind, Tense=Pres, Person=2, Number=Sing]; suele [VerbForm=Fin, Mood=Ind, Tense=Pres, Person=3, Number=Sing]; solemos [VerbForm=Fin, Mood=Ind, Tense=Pres, Person=1, Number=Plur]; suelen [VerbForm=Fin, Mood=Ind, Tense=Pres, Person=3, Number=Plur]; suela [VerbForm=Fin, Mood=Sub, Tense=Pres, Person=1,3, Number=Sing]; solido [VerbForm=Part, Tense=Past, Gender=Masc, Number=Sing]; soliendo [VerbForm=Ger]
  1. [A2] tener la costumbre de hacer algo de manera repetida o frecuente
     - example: Nosotros solemos dar un paseo por el parque después de cenar.
     - frames: soler + inf
  2. [B1] ocurrir un hecho con mucha frecuencia o ser lo más normal
     - example: En esta región suele llover bastante durante los meses de otoño.
     - frames: soler + inf
     - collocations: como suele pasar; como suele ocurrir

#### Entry B

**Lexeme 1: soler**, VERB
- grammar: conjugation 2; irregular True; note o > ue; defective: always before an infinitive, mostly present and imperfect, no future or conditional
- said: /so.ˈleɾ/ (es-ES), /so.ˈleɾ/ (es-MX)
- forms: suelo [Person=1, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin]; sueles [Person=2, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin]; suele [Person=3, Number=Sing, Tense=Pres, Mood=Ind, VerbForm=Fin]; solemos [Person=1, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin]; suelen [Person=3, Number=Plur, Tense=Pres, Mood=Ind, VerbForm=Fin]; solí [Person=1, Number=Sing, Tense=Past, Mood=Ind, VerbForm=Fin] (rare; the imperfect solía is used instead); solió [Person=3, Number=Sing, Tense=Past, Mood=Ind, VerbForm=Fin] (rare; the imperfect solía is used instead); suela [Person=1, Number=Sing, Tense=Pres, Mood=Sub, VerbForm=Fin]; solido [VerbForm=Part] (only in compound tenses, rare: he solido); soliendo [VerbForm=Ger] (rare)
  1. [B1] tener la costumbre de hacer algo con frecuencia
     - example: Los domingos suelo desayunar en la terraza con toda mi familia.
     - frames: soler + inf
     - collocations: como suele decirse
  2. [B1] ocurrir algo con frecuencia o de manera habitual
     - example: En esta zona suele llover mucho durante los meses de otoño.
     - frames: soler + inf
     - collocations: como suele ocurrir; como suele pasar
