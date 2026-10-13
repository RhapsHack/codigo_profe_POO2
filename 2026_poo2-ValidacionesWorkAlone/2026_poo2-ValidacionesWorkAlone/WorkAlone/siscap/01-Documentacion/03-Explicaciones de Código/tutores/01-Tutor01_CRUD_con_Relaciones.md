# Tutor 01: el CRUD sobre tablas con relaciones (TutorRelaciones)

## ¿Qué es?

Un **mini-tutorial de consola** (`programas/tutores/tutor_01/TutorRelaciones.java`)
que recorre el ciclo completo de un registro cuya entidad vive en una tabla con
relaciones (`cursos`, `inscripciones`, `certificados`), usando **los DAOs
reales** del proyecto. Es el complemento didáctico de la iteración de
relaciones: ahí vimos *cómo se navega el grafo*; aquí vemos *cómo se
guarda, busca, edita y borra*.

Es **autocontenido**: no depende de haber corrido la ingesta. Si las personas de
ejemplo no existen, las crea (INSERT) y luego las usa. Cada fase explica en
pantalla qué va a pasar, ejecuta la operación con el DAO y espera **Enter**
(`lector.leerTexto`) para continuar.

Ejecución:

```bash
mvn compile exec:java@tutor-01
```

## Las seis fases y qué demuestran

| Fase | Operación | Qué se demuestra |
| --- | --- | --- |
| 1. Iniciar/vaciar | `borradoFisico` | El borrado físico y el **orden de las claves foráneas**: certificados → inscripciones → cursos. Las personas NO se vacían (son los catálogos raíz). |
| 2. Guardar | `agregar` (INSERT) | Crear objetos en memoria, enlazarlos con `setInstructor`, `setCurso`, `setPersona` y persistirlos. Persona que no existe → se inserta y se relee. |
| 3. Buscar | `obtenerPorCampo` | El **foco didáctico**: relación M:1 EAGER (el instructor viaja en el SELECT) frente a la colección 1:M LAZY (llega vacía, `estaLlenaInscripciones()` = false) que necesita el paso extra `llenarInscripciones`, y la 1:1 con `obtenerCertificadoDeInscripcion`. |
| 4. Editar | `actualizar` (UPDATE) | Cambiar un campo con el setter, persistirlo y **releer** para comprobar que quedó en la base. |
| 5. Borrar | `borrar` y `borradoFisico` | La diferencia entre borrado **lógico** (UPDATE `activo=false`: la fila permanece pero las lecturas —que filtran `activo=true`— ya no la devuelven) y borrado **físico** (DELETE real), de nuevo con el orden de FKs. |
| 6. Cierre | `desconectar` | Resumen de las operaciones vistas y cierre de las conexiones. |

```java
// Esqueleto del programa
private static final ILectorDatos LECTOR = new LectorDatosTexto();
private static final PersonaDAO PERSONA_DAO = new PersonaDAO();
private static final CursoDAO CURSO_DAO = new CursoDAO();
private static final InscripcionDAO INSCRIPCION_DAO = new InscripcionDAO();
private static final CertificadoDAO CERTIFICADO_DAO = new CertificadoDAO();

public static void main(String[] args) {
    // conexión de los 4 DAOs
    faseIniciar();
    Curso curso = faseGuardar();
    curso = faseBuscar();
    faseEditar(curso);
    faseBorrar(curso);
    faseCierre();
}
```

## Cómo se usa el DAO sobre tablas con relaciones (lo importante)

### Guardar

`agregar` ejecuta el INSERT y **no asigna el id al objeto**. El patrón es
"insertar y releer":

```java
Persona instructor = PERSONA_DAO.obtenerPorCampo(FLD_NUMERO_EMPLEADO, "EMP001");
if (instructor == null) {              // la persona no existe -> INSERT simple
    instructor = new Persona(null, ACTIVO, "Ana", "López", ...);
    PERSONA_DAO.agregar(instructor);
    instructor = PERSONA_DAO.obtenerPorCampo(FLD_NUMERO_EMPLEADO, "EMP001"); // releer
}

Curso curso = new Curso(null, ACTIVO, "Programación Orientada a Objetos en Java", ...);
curso.setInstructor(instructor);       // relación M:1 vía setter
CURSO_DAO.agregar(curso);              // INSERT con FK id_instructor
curso = CURSO_DAO.obtenerPorCampo(FLD_NOMBRE, curso.getNombre());  // releer: id real
```

Después se crean las inscripciones (tabla puente N:M, con su `fecha` y
`resultado`) y el certificado 1:1 sobre la inscripción aprobada.

### Buscar: EAGER vs LAZY

```java
Curso curso = CURSO_DAO.obtenerPorCampo(FLD_NOMBRE, NOMBRE_CURSO_EJEMPLO);
curso.getInstructor().getNombre();              // M:1 EAGER: ya viene poblado
curso.getInscripciones().size();                // 1:M LAZY: 0 y estaLlena=false
CURSO_DAO.llenarInscripciones(curso);           // paso extra del DAO
curso.getInscripciones().size();                // ahora viene poblada
Certificado c = CERTIFICADO_DAO
        .obtenerCertificadoDeInscripcion(inscripcion.getId());  // 1:1
```

Esta es la lección que imita a JPA: una **referencia** se hidrata siempre
(eager, costo fijo), una **colección** se llena solo cuando se pide (lazy,
costo variable y el problema N+1) y la 1:1 se consulta por su FK.

### Editar

```java
curso.setObjetivo("Dominar clases, herencia, polimorfismo y composición");
CURSO_DAO.actualizar(curso);                    // UPDATE
Curso releido = CURSO_DAO.obtenerPorCampo(FLD_NOMBRE, NOMBRE_CURSO_EJEMPLO);
```

### Borrar: lógico y físico

```java
INSCRIPCION_DAO.borrar(inscripcion);   // lógico: UPDATE activo=false
INSCRIPCION_DAO.obtenerTodas();        // la inscripción ya no aparece

CERTIFICADO_DAO.borradoFisico(certificado);      // físico, en orden de FKs
INSCRIPCION_DAO.borradoFisico(inscripcion);
CURSO_DAO.borradoFisico(curso);
```

## La lección de las claves foráneas (en vivo)

`DAOBase.conectar()` activa `PRAGMA foreign_keys = ON`, así que SQLite **sí
bloquea** los borrados que rompen referencias. En la fase 5 esto se convierte
en el punto más didáctico:

1. Se borra lógicamente una inscripción: su fila queda con `activo=false`.
2. Se borra físicamente el curso: SQLite lanza
   `[SQLITE_CONSTRAINT_FOREIGNKEY] A foreign key constraint failed`, **porque la
   inscripción borrada lógicamente todavía referencia al curso**.

La fila "borrada" no desapareció: solo dejó de ser visible. La lección es que
**el borrado lógico no libera la clave foránea**; antes de borrar físicamente
el curso hay que eliminar físicamente también esa inscripción (y sus
certificados). El programa lo hace y la corrida termina con 0 cursos activos.

## Comandos de referencia

```bash
mvn compile exec:java@tutor-01  # recorre las 6 fases pidiendo Enter
mvn test                         # 62 pruebas (verifica que el tutorial no rompió DAOs)
```

## Puntos para discusión en clase

1. ¿Por qué `agregar` no asigna el id? ¿Qué habría que cambiar en el DAO para
   devolverlo? (Compáralo con `Statement.RETURN_GENERATED_KEYS`.)
2. ¿Qué ventaja y qué problema trae el borrado lógico? Relaciónalo con la
   recuperación de la fila (UPDATE `activo=true`) y con las FKs (liberar el
   registro realmente requiere el borrado físico).
3. ¿Por qué el orden certificados → inscripciones → cursos es obligatorio en el
   vaciado? ¿Qué pasaría si se invirtiera?
4. La fase 3 muestra eager/lazy de forma manual. ¿Cómo cree que JPA decide qué
   traer con un SELECT?