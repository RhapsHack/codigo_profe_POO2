# Decisión: Versión de Java 21 LTS (compilación `--release 21`)

## Contexto

`siscap` migrará su capa de persistencia de JDBC a **JPA** usando **EclipseLink**
(implementación de referencia), y a futuro se servirá como aplicación web en un
servidor Java (candidatos: Tomcat, TomEE o WildFly). El equipo instala JDK 25
en la máquina de desarrollo.

Aparecen tres preguntas:

1. ¿Qué versión de Java fijamos para el proyecto?
2. ¿Qué características **podemos** usar al elegirla?
3. ¿Qué características perdemos y qué tan grave es?

## Decisión

Adoptar **Java 21 LTS** como versión objetivo. En `pom.xml` esto se expresa con
`maven.compiler.release=21`, que obliga a `javac` a compilar con la opción
`--release 21`: el bytecode generado es de Java 21 y solo se permiten las API
del JDK 21. **No es necesario instalar JDK 21**: funciona con el JDK 25 ya
instalado.

**Por qué 21 y no otro número:**

| Criterio | Qué exige nuestro futuro | Java 21 |
| --- | --- | --- |
| Soporte | LTS con soporte a largo plazo (hasta ~2030) | ✔ (LTS desde sep/2023) |
| Jakarta EE | Los servidores web modernos (Tomcat 11, TomEE 10, WildFly 33+) usan Servlet/Jakarta EE 10 | ✔ (Java 17+) |
| EclipseLink | EclipseLink 4.x requiere Java 17 o superior | ✔ (holgado) |
| Productividad | `record`, `sealed`, pattern matching, virtual threads ya finales | ✔ |
| Futuro del WAR | El servidor correrá en un JRE del servidor (17–21 típico); bytecode 21 es seguro en todos | ✔ |

## Lo que SÍ podemos usar: cambios importantes de Java 8 a Java 21

Mensaje principal: **toda la modernidad estable del lenguaje está en Java 21**.
El salto pedagógicamente grande es de Java 8 (2014) a Java 21; lo de 9→21 son
refinamientos del mismo estilo.

| Versión | Cambios importantes (ya estables en esa versión) |
| --- | --- |
| **Java 8** (LTS, 2014) | Lambdas, referencias a métodos, `Stream`, `Optional`, API nueva de fechas/horas (`java.time`), métodos default en interfaces, `CompletableFuture`. |
| **Java 9** (2017) | Módulos (JPMS), `JShell`, fábricas de colecciones inmutables (`List.of`, `Map.of`), `try` con recursos mejorado. |
| **Java 10** (2018) | Inferencia de tipo local con `var`. |
| **Java 11** (LTS, 2018) | `HttpClient` estable, nuevos métodos en `String` (`isBlank`, `strip`, `repeat`, `lines`), ejecución de un `*.java` directo (`java Clase.java`). |
| **Java 12–13** | *Switch expressions* (preview), *text blocks* (preview). |
| **Java 14** (2020) | *Switch expressions* finales, `record` (preview), mensajes de `NullPointerException` útiles. |
| **Java 15** (2020) | *Text blocks* finales, *sealed* (preview). |
| **Java 16** (2021) | `record` finales, pattern matching para `instanceof` final, `Stream.toList()`. |
| **Java 17** (LTS, 2021) | *Sealed classes* finales, *switch* con pattern matching (preview), UTF-8 como charset default. |
| **Java 18–20** | Charset UTF-8 por defecto (18), *virtual threads* y *structured concurrency* en preview (19/20). |
| **Java 21** (LTS, 2023) | **Virtual threads** finales, **record patterns** finales, **pattern matching para `switch`** final, **Sequenced Collections** finales, ZGC generacional, previews de strings templates / structured concurrency. |

En nuestro proyecto los más útiles de inmediato son: `record` (entidades/DTOs
y lectores), `var`, `switch` con patterns (válida de la capa de menús),
`Stream` (`map`/`flatMap`/`filter`) y `java.time` (fechas de certificados).

## Lo que NO podemos usar: Java 22 → actual (25)

Compilando a `--release 21`, **no están disponibles** los siguientes cambios
(reales o previos a Java 21):

| Característica | Estado | Dónde vive |
| --- | --- | --- |
| **Unnamed variables & patterns** (`case _ ->`) | Final en 22 | 22 |
| **Foreign Function & Memory API** | Final en 22 | 22 |
| **Stream Gatherers** | Final en 24 | 22–24 |
| **Scoped Values** | Final en 24 | 20–24 |
| **Class-File API** | Final en 24 | 22–24 |
| **Instance main methods / clases simplificadas** | Final en 24 | 23–24 |
| Markdown en javadoc | Final en 23 | 23 |
| Key Derivation Function (cripto) | Final en 23/24 | 23–24 |
| **Primitive patterns** (`instanceof int`, `switch` numérico) | Final en 25 | 23–25 |
| **Flexible constructor bodies** | Final en 25 | 23–25 |
| **Value classes / Valhalla** | Incubador/preview | 24–25 |

### Por qué perderlas NO es importante

1. **Casi todas fueron *previews*.** Un preview exige la bandera
   `--enable-preview` y puede cambiar entre versiones (ej. *string templates*
   se retiraron en 24). El código de producción y de servidores no debe
   depender de previews; por diseño no los usaríamos aunque compilásemos con 25.
2. **Nuestro stack no las usa.** JPA/EclipseLink, Jakarta EE, SQLite y los
   servidores (Tomcat/TomEE/WildFly) no requieren ninguna API de las 22+.
3. **No son para primer ciclo de POO.** Funciones como FFM o Valhalla resuelven
   problemas de infraestructura, no de modelado de dominio.
4. **Son refinamientos, no revoluciones.** Gatherers y scoped values mejoran
   casos concretos de `Stream` y `ThreadLocal`; se migran en minutos cuando las
   herramientas del ecosistema (servidores, build, IDE) maduren.
5. **21 es la coincidencia correcta.** Es el LTS que alinea a los servidores
   Jakarta EE 10 (que piden Java 17+) con EclipseLink 4.x, y sigue siendo válido
   para Jakarta EE 11 (Java 17+). Compilar a 25 arriesga incompatibilidad con un
   JRE de servidor que aún corra 17/21.

## Alternativas descartadas

- **Java 8** → No admite `jakarta.*` ni EclipseLink 4.x; perderíamos 15 años de
  mejoras del lenguaje y el stack de Jakarta EE.
- **Java 17 (LTS)** → Válido y compatible con todo nuestro stack, pero es una
  generación anterior: sin virtual threads ni *pattern switch* finales. 21 da lo
  mismo por el servidor y más madurez de lenguaje.
- **Java 25 (LTS actual, sep/2025)** → Técnicamente funcionaría en Tomcat 11 /
  TomEE 10 / WildFly 33+, y traería primitive patterns y Valhalla en preview.
  Se descarta porque el bytecode 25 no lo garantizan los JRE de servidores
  actuales, `--release 21` es el estándar de facto para Jakarta EE 10/11, y las
  características extra son previews que no se usarían.
- **Sin fijar versión (compilar con el JDK del día)** → Se descarta: Maven
  compilaría según el JDK instalado y el build dejaría de ser reproducible.

## Consecuencias

- La compilación genera bytecode de Java 21 aunque Maven corra con JDK 25
  (`mvn -q clean compile` exitoso).
- El equipo no necesita instalar JDK 21; para verificar la versión objetivo:
  `mvn -q -DskipTests package` y `javap -v` sobre una clase compilada (major
  version 65 = Java 21).
- Al pasar a JPA con EclipseLink 4.0.9 (JPA 3.1 / `jakarta.persistence`) no hay
  conflicto de versiones: el artefacto está en Maven Central y corre en Java 17+.
- Cualquier intento de usar API 22+ falla en compilación con un error claro de
  `--release 21`; el IDE marcará red/yellow según su SDK (JDK 25 instalado),
  por lo que hay que configurar el proyecto del IDE con "Language level 21".
- El futuro WAR será compatible con Tomcat 11, TomEE 10+ y WildFly 33+ (todos
  requieren Java 17+).