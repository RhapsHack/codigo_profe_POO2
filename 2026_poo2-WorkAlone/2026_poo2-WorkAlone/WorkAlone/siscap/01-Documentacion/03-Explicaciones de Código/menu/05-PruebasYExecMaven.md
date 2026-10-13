# 05 · Pruebas y ejecución 100% Maven (JUnit 5 + exec-maven-plugin)

## ¿Qué enseña este cambio?

Hasta ahora ejecutábamos los programas compilando con Maven, pero corriendo
`java` a mano o con `jshell`. Eso obligaba a armar el *classpath* a mano
(incluyendo buscar el jar del driver de SQLite en el repositorio local
`~/.m2`): frágil, repetitivo y distinto en cada computadora. Este cambio
deja **todo el ciclo compilar → probar → ejecutar dentro de Maven**, que es
la herramienta estándar de construcción en proyectos Java profesionales.

### 1. Maven gestiona el classpath por ti

Maven lee el `pom.xml`, descarga las dependencias declaradas y arma el
classpath de compilación, de pruebas y de ejecución. Nunca más rutas a jars:
el mismo `pom.xml` funciona igual en Windows, Linux o macOS.

### 2. `mvn test`: por qué las pruebas "no se ejecutaban" antes

El proyecto ya tenía `junit-jupiter-api` (las anotaciones `@Test`, los
`assertEquals`...). Pero **la API no ejecuta nada**: quien corre las pruebas
es el *motor* (`junit-jupiter-engine`), que el plugin **Surefire** de Maven
descubre en tiempo de ejecución. Sin el motor, Surefire compilaba las clases
de prueba y las ignoraba silenciosamente: `mvn test` decía "Tests run: 0".

Lección: **dependencia de compilación ≠ dependencia de ejecución**.

### 3. `exec-maven-plugin` con ejecuciones nombradas

En el `pom.xml` se declaró una `<execution>` con `id` por cada programa
principal. Eso permite correrlos con nombres cortos que Maven resuelve
completos (clase y classpath incluidos):

```bash
mvn compile exec:java@crud-tecnologias     # CRUD de tecnologías
mvn compile exec:java@crud-personas        # CRUD de personas
mvn compile exec:java@ingesta-inicial      # recarga los datos semilla
mvn compile exec:java@ingesta-capacitaciones  # recarga cursos/inscripciones/certificados
mvn compile exec:java@demo-relaciones      # recorre 1:N, N:M, 1:1, lazy y N+1
```

Si mañana se agrega un programa, solo se añade su `<execution>` en el `pom`.

### 4. Probar un menú de consola desde JUnit

`MenuTexto` lee de `System.in` con `Scanner`. ¿Cómo se automatiza una prueba
de algo interactivo? **Sustituyendo la entrada estándar**:

```java
System.setIn(new ByteArrayInputStream("2\n".getBytes(UTF_8)));
```

El `Scanner` del menú (que lee de `System.in`) recibe `"2"` como si el
usuario lo hubiera tecleado. Cada prueba restaura la entrada original en un
`finally` para no contaminar a las demás. Es la misma idea que usará
cualquier prueba de programas de consola.

### 5. Qué se probó (62 pruebas, todas en `src/test/java`)

* **`OpcionMenuTest`** (8): construcción, validación de valor/texto nulos o
  vacíos, capitalización con acentos (`árbol → Árbol`), inmutabilidad del
  valor, cambio de texto y de estado, `toString`.
* **`MenuTextoTest`** (19): los tres estilos de numeración (1..n, a..z, aa,
  i, ii), separador configurable, opciones inhabilitadas ocultas y
  renumeradas, Cancelar/Salir activables, duplicados rechazados, contrato
  `false`/`null` de quitar/habilitar/obtener, y las respuestas del usuario:
  por etiqueta mostrada (`2` → `guardar`), por valor (`abrir`), con
  reintentos ante opciones inválidas o inhabilitadas.
* **`LectorDatosTextoTest`** (21): lectura con valores válidos, inválidos y
  neutrales de los métodos del lector sobre `System.in`.
* **`CursoDAOTest`** (4): instructor eager al leer, colección lazy vacía con
  bandera `false`, `llenarInscripciones` (3 participantes + bandera `true`),
  cursos de un instructor y curso con instructor inexistente rechazado por FK.
* **`InscripcionDAOTest`** (4): hidratación eager de curso y persona, consultas
  por curso y por persona, y duplicado curso+persona rechazado por UNIQUE.
* **`CertificadoDAOTest`** (5): constancia con folio y cadena eager
  (certificado → inscripción → curso + persona), inscripción sin constancia
  devuelve `null`, segundo certificado para la misma inscripción rechazado
  (regla 1:1) y folio repetido rechazado por UNIQUE.

Nota de depuración real: una primera versión de la prueba de letras **olvidó
configurar el estilo LETRAS**, así que el menú (correctamente) numeró con
dígitos y la prueba falló. La falla estaba en el **test**, no en el código
de producción: `mvn test` señala dónde mirar, no quién tiene la culpa.

## Puntos para discusión en clase

1. ¿Por qué `mvn test` es reproducible en cualquier máquina, pero correr
   `java -cp ...` a mano no lo es?
2. ¿Qué pasa si dos pruebas no restauran `System.in`? ¿Qué tipo de error
   sería: de compilación, lógico o de prueba?
3. `System.setIn` es un *recurso global mutable*. ¿Qué problema tendría si
   las pruebas corrieran en paralelo?

## Comandos de referencia

```bash
mvn test                        # compila y corre las 62 pruebas
mvn test -Dtest=MenuTextoTest   # una sola clase de pruebas
mvn compile exec:java@<id>      # corre un programa (ver pom.xml)
```
