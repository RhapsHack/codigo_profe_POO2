# Iteración 1: relaciones entre tablas con JDBC (1:N, N:M con atributos y 1:1)

## ¿Qué aprendemos en esta iteración?

Las tablas de catálogo (1 fila = 1 dato aislado) se quedan cortas cuando el
modelo empieza a **navegar**: "¿qué cursos imparte Ana?", "¿quiénes cursaron
POO?", "¿qué constancia tiene la inscripción?". Esta iteración modela el
área de **capacitación** de la empresa con las tres cardinales sobre la tabla
`personas` que ya existía:

| Cardinalidad | Tablas | Descripción |
| --- | --- | --- |
| 1:N | `cursos` → `personas` (id_instructor) | Una persona imparte muchos cursos; cada curso tiene UN instructor. |
| N:M con atributos | `inscripciones` (id_curso + id_persona + fecha + resultado) | Una persona se inscribe a varios cursos y un curso tiene varias personas; los datos de la participación viven en la tabla puente. |
| 1:1 | `certificados` → `inscripciones` (id_inscripcion UNIQUE) | Una inscripción aprobada tiene a lo sumo UN certificado. |

Todo lo modelamos **a mano con JDBC**, pero con entidades "estilo JPA" que
navegan entre sí y con DAOs que saben hidratar esas navegaciones. Es el paso
previo didáctico a JPA: cuando llegue JPA, estos conceptos ya serán familiares.

## 1. Las tres tablas nuevas (DDL)

Se declararon en `Constantes` y se crean con `CREATE TABLE IF NOT EXISTS`
desde `DAOBase.conectar()`.

```sql
CREATE TABLE IF NOT EXISTS cursos (
    ID          INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre      TEXT    NOT NULL UNIQUE,
    objetivo    TEXT    NOT NULL,
    id_instructor INTEGER NOT NULL REFERENCES personas(ID),
    activo      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS inscripciones (
    ID          INTEGER PRIMARY KEY AUTOINCREMENT,
    id_curso    INTEGER NOT NULL REFERENCES cursos(ID),
    id_persona  INTEGER NOT NULL REFERENCES personas(ID),
    fecha       TEXT    NOT NULL,
    resultado   TEXT    NOT NULL,
    activo      BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uc_curso_persona UNIQUE (id_curso, id_persona)
);

CREATE TABLE IF NOT EXISTS certificados (
    ID            INTEGER PRIMARY KEY AUTOINCREMENT,
    id_inscripcion INTEGER NOT NULL UNIQUE REFERENCES inscripciones(ID),
    folio         TEXT    NOT NULL UNIQUE,
    fecha_emision TEXT    NOT NULL,
    activo        BOOLEAN NOT NULL DEFAULT TRUE
);
```

Las reglas se declaran **en la base** y SQLite las hace cumplir:

- `cursos.nombre` UNIQUE y `certificados.folio` UNIQUE → no se repiten.
- `inscripciones` UNIQUE(`id_curso`, `id_persona`) → una persona no se inscribe
  dos veces al mismo curso.
- `certificados.id_inscripcion` UNIQUE → **regla 1:1** (una inscripción no
  puede tener dos constancias).
- Las `REFERENCES` → **clave foránea**: no puede existir un curso con un
  instructor que no esté en `personas`.

## 2. Por qué SQLite necesita `PRAGMA foreign_keys = ON`

SQLite **no valida las claves foráneas por defecto**: la restricción existe,
pero es decorativa hasta que se activa. Hay que ejecutar el `PRAGMA` **en cada
conexión abierta** (es una configuración por conexión, no de la base). En
`DAOBase.conectar()`:

```java
try (PreparedStatement ps = conexion.prepareStatement("PRAGMA foreign_keys = ON")) {
    ps.execute();
}
```

Solo con este PRAGMA activo, en la demo el intento de "Curso con instructor
inexistente" es bloqueado con
`[SQLITE_CONSTRAINT_FOREIGNKEY] A foreign key constraint failed`.

## 3. Entidades que navegan (estilo JPA)

Cada entidad nueva declara **atributos de navegación**, igual que hará la
entidad con anotaciones de JPA:

| Entidad | Atributos propios | Navegación declarada | Cardinalidad |
| --- | --- | --- | --- |
| `Curso` | id, nombre, objetivo, activo | `Persona instructor` + `List<Inscripcion> inscripciones` | ManyToOne + OneToMany |
| `Inscripcion` | id, fecha, resultado, activo | `Curso curso` + `Persona persona` | Two ManyToOne |
| `Certificado` | id, folio, fechaEmision, activo | `Inscripcion inscripcion` | OneToOne |

La entidad `Persona` es **hoja**: no conoce sus cursos. (En JPA real podría
tener la colección inversa, pero aquí la omitimos a propósito para que la
dirección de la navegación sea explícita.)

## 4. Hidratación: referencias eager, colecciones lazy

`DAOBase` invoca el hook `cargarRelaciones(T)` después de `mapearEntidad` en
**todas las lecturas**. Los DAOs de estos tres objetos lo sobrescriben con una
política didáctica que imita a JPA:

- **Referencias eager**: `instructor`, `curso`, `persona`, `inscripcion`
  SIEMPRE se llenan al leer la entidad, haciendo un `obtenerPorId` del DAO
  colaborador, **solo a profundidad 1** (no hay recursión).
- **Colecciones lazy**: `Curso.inscripciones` NO se llena al leer. Empieza
  vacía y solo se puebla con `cursoDAO.llenarInscripciones(curso)`, que además
  pone la bandera de memoria `inscripcionesCargadas` → `estaLlenaInscripciones()`.
  Es una imitación intencional del lazy de JPA, para saber si una colección ya
  fue inicializada.

```java
// CursoDAO: eager para el instructor, lazy para la colección
@Override
public void cargarRelaciones(Curso curso) {
    if (curso == null || curso.getInstructor().getId() == null) return;
    curso.setInstructor(personaDAO().obtenerPorId(curso.getInstructor().getId()));
}

@Override
public void llenarInscripciones(Curso curso) {
    curso.setInscripciones(inscripcionDAO().obtenerInscripcionesDeCurso(curso.getId()));
    curso.setInscripcionesCargadas(true);
}
```

Nota de diseño: **los DAOs colaboradores se crean de forma perezosa**
(`private PersonaDAO personaDAO() { if (personaDAO == null) personaDAO = new PersonaDAO(); return personaDAO; }`).
Si se crearan en el constructor habría un ciclo infinito:
`CursoDAO` → `InscripcionDAO` → `CursoDAO` → ...

### El problema N+1, en vivo

La carga lazy tiene un costo conocido: si lees 20 cursos y llenas la colección
de cada uno, pagas `1 + N` SELECTs (uno para listar los cursos y uno por cada
colección, más sus referencias). Para mostrarlo a la clase, `DAOBase` lleva un
**contador didáctico de consultas de lectura** (`getConsultasDeLectura()` /
`reiniciarContadorDeConsultas()`). La demo imprime el total:

```
1 SELECT para listar los cursos + 1 SELECT por cada coleccion llenada (+ sus referencias): SELECTs ejecutados: 25
```

En JPA este problema se resuelve con `JOIN FETCH` o `@BatchSize`; aquí lo
vemos aparecer con JDBC a mano, para que se entienda por qué JPA ofrece esos
atajos.

## 5. Los programas

- `IngestaCapacitaciones` (`mvn compile exec:java@ingesta-capacitaciones`):
  vacía las 3 tablas respetando el orden de las FKs (certificados → inscripciones
  → cursos) y siembra los datos con FKs **válidas** hacia las `personas`
  existentes, buscadas por su `numero_empleado` (así el orden alfabético del
  catálogo no altera la identidad).
- `DemoRelaciones` (`mvn compile exec:java@demo-relaciones`): recorre el grafo
  mostrando 1:N, lazy + bandera, N:M, N+1 con el contador, 1:1 y las 4 reglas
  de integridad bloqueadas por SQLite.
- `mvn test`: agrega pruebas JUnit para los 3 DAOs nuevos (altas, duplicados
  rechazados, hidratación a profundidad 1 y regla 1:1). La base se re-siembra
  en cada prueba: primero se vacía la capacitación y **después** las personas,
  porque las FKs impiden borrar personas referenciadas.

## Puntos para discusión en clase

1. ¿Por qué la colección es lazy pero la referencia es eager? ¿Qué pasaría con
   el desempeño si cada curso arrastrara sus inscripciones al leerse?
   (Relaciónalo con el N+1 visto en la demo.)
2. ¿Por qué hay que borrar certificados antes que inscripciones y estas antes
   que cursos en el vaciado idempotente?
3. ¿Qué diferencias ves entre declarar las reglas en el DDL (`UNIQUE`,
   `REFERENCES`) y validarlas solo en Java? ¿Qué pasa si otro programa escribe
   en la misma base?

## Comandos de referencia

```bash
mvn compile exec:java@ingesta-capacitaciones  # reseed de las 3 tablas
mvn compile exec:java@demo-relaciones         # recorre 1:N, N:M, 1:1, lazy, N+1
mvn test                                       # 62 pruebas (49 previas + 13 nuevas)
```