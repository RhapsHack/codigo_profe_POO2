# Fase 2 sobre JPA: sistema de capacitaciones con relaciones y pantallas de gestión

> Cambio: `sistema-capacitaciones-jpa`.
> Decisión de diseño en `../../04-Decisiones de Diseño/07-Decision-RelacionesJPA.md`.

## ¿Qué aprendemos en esta etapa?

En la era JDBC ya existió una iteración de relaciones (cursos, inscripciones y
certificados) que se retiró junto con los CRUD de consola. Ahora las relaciones
vuelven **sobre JPA** —la tecnología con la que ya se persistían los 4
catálogos— y se opera desde **pantallas Swing**, no desde consola. El eje
didáctico es el mapeo de las cuatro cardinales con sus *fetch types* y la
consulta que evita el problema N+1:

| Relación | Entidades | Fetch | Quién la demuestra |
| --- | --- | --- | --- |
| N:1 (ManyToOne) | `Curso` → `Tema` e instructor (`Persona`) | EAGER | listado de cursos, tutor paso 1 |
| 1:N (OneToMany) | `Curso` → `List<Inscripcion>` | LAZY | tutor paso 3 (lazy vs. JOIN FETCH) |
| N:M con atributos | `Inscripcion` entre `Persona` ↔ `Curso` | dos N:1 EAGER | pantalla de inscripciones |
| 1:1 (OneToOne) | `Certificado` → `Inscripcion` | LAZY | pantalla de certificados, regla en servicio |

## 1. El tema es una entidad propia

`Tema` tiene `nombre` único y `tipo` en el enum `TipoTema` (`LENGUAJE`,
`FRAMEWORK`, `BASE_DATOS`, `HABILIDAD_BLANDA`). `Curso` referencia al tema con
un `@ManyToOne` en lugar de repetir el nombre como texto: se evita duplicar el
conocimiento entre cursos y hay una FK real navegable. `Tema` además se
administra con el CRUD de la **ventana única** (`CAT_TEMA` en
`Constantes`, registrado en `DaoServiceFactory`, `ValidadorEntidadFactory`,
`EntityPanelFactory` y `CamposBusquedaConfig`), de modo que la pantalla de
cursos solo consume el catálogo para poblar su combo.

## 2. El mapeo JPA (D2 del design)

```text
Tema(1) ──< Curso(N)          Curso.instructor → Persona    @ManyToOne EAGER
Persona(1) ──< Curso(N)        Curso.tema       → Tema       @ManyToOne EAGER
Curso(1) ──< Inscripcion(N)    Curso.inscripciones          @OneToMany(mappedBy="curso") LAZY
Inscripcion(1) ──> Curso       Inscripcion.curso    → Curso   @ManyToOne EAGER
Inscripcion(1) ──> Persona     Inscripcion.persona  → Persona @ManyToOne EAGER
Inscripcion(1) ──(1) Certificado  Certificado.inscripcion    @OneToOne LAZY
```

- **Referencias `@ManyToOne` EAGER**: al leer un curso (o una inscripción), la
  referencia ya viene hidratada; el listado muestra el tema y el instructor sin
  consultas extra. Es lo que se quiere enseñar: "hidratación automática".
- **Colección `@OneToMany(mappedBy="curso")` LAZY**: es justo la lección
  central. La colección llega vacía y se llena bajo demanda o con JOIN FETCH;
  de ahí nace el riesgo N+1.
- **`@OneToOne` LAZY en el certificado**: la navegación hacia la inscripción se
  fuerza de forma explícita.
- **La N:M con atributos** (fecha y resultado) no usa `@ManyToMany` (que no
  admite atributos en la tabla de unión) sino la entidad intermedia
  `Inscripcion` con dos N:1 — el lugar natural de la regla de unicidad
  `(curso, persona)`.

### Reglas de unicidad declaradas en la base (`capacitaciones.db`, DDL fuera de la app)

```sql
temas.nombre          UNIQUE
cursos.nombre         UNIQUE
inscripciones         UNIQUE (id_curso, id_persona)   -- no repetir inscripción
certificados.folio    UNIQUE
certificados          UNIQUE (id_inscripcion)          -- regla 1:1
```

La restricción se aplica en dos niveles: la **base protege** (siempre) y el
**servicio da el mensaje claro** al usuario.

## 3. DAOs concretos: `findByCampo` vs. Criteria propio

Los catálogos ya tenían `DAOBase<T>` con Criteria API y `findByCampo`/
`findByCampoUnico`. Los DAOs nuevos se apoyan en eso y añaden lo específico:

| DAO | Método | Cómo |
| --- | --- | --- |
| `CursoDAO` | `findByInstructor(Persona)` / `findByTema(Tema)` | `findByCampo("instructor"/"tema", …)` |
| `CursoDAO` | `findConInscripciones(Curso)` | Criteria/DTO propio con `LEFT JOIN FETCH c.inscripciones` |
| `InscripcionDAO` | `findByCurso(Curso)` / `findByPersona(Persona)` | `findByCampo(…)` |
| `InscripcionDAO` | `findAprobadasDeCurso(Curso)` | Criteria propio: `curso = :c AND resultado = APROBADO` |
| `CertificadoDAO` | `findByInscripcion(Inscripcion)` | `findByCampoUnico("inscripcion", …)` |

`findConInscripciones` es la respuesta al N+1: una sola consulta trae el curso
con todas sus inscripciones (`inscripcionesCargadas = true`), donde leer cada
colección lazily a mano costaría `1 + N` SELECTs. `findAprobadasDeCurso`
combinar condiciones (curso + resultado), cosa que un `findByCampo` no puede.

## 4. Servicios con borrado lógico y regla de negocio en el servicio

- Los cuatro servicios extienden `ServicioConBorradoLogico` (sobre
  `ServicioBase`): "borrar" marca `activo = false` y los listados omiten las
  filas inactivas; el filtrado de activos vive en la consulta del DAO
  (predicado `activo = true`), **sin tocar** el `DAOBase` de los catálogos.
- `CursoService.guardar` puebla instructor y tema; expone
  `cargarInscripciones` (JOIN FETCH).
- `InscripcionService.inscribir` fija **ambos lados** de la relación
  `Curso`–`Inscripcion` (`inscripcion.setCurso(curso)` +
  `curso.getInscripciones().add(...)`) y valida la no duplicidad del par, con
  mensaje claro antes de llegar a la BD.
- `CertificadoService.emitir(...)` **exige** inscripción APROBADA y sin
  certificado previo; si no, lanza `ReglaNegocioException` (checked, de
  negocio) — separada de `DAOException` (de persistencia). La GUI y el tutor
  solo invocan la regla; no la deciden.

## 5. Las tres pantallas y su integración

Cada pantalla es un `JFrame` con un campo de formulario, la lista de activos y
una botonera; **todo error (validación o persistencia) sale en un diálogo** y
la pantalla queda operable:

- `VentanaCursos` (`CursosController`): alta/edición con combos de instructor
  (personas) y tema (temas); nombre único protegido por la BD y avisado por el
  diálogo. Un instructor/tema inexistente (o no seleccionado) no guarda y
  avisa.
- `VentanaInscripciones` (`InscripcionesController`): inscribe persona+curso
  con fecha (resultado inicial PENDIENTE) y cambia el resultado de la
  evaluación; duplicados rechazados por el servicio.
- `VentanaCertificados` (`CertificadosController`): emite el certificado de una
  inscripción (folio + fecha) y lo consulta; "no aprobada" y "duplicado" se
  informan y no persisten.

`GestionCapacitaciones` (`mvn compile exec:java@capacitaciones`) es el `main`
que abre las tres pantallas desde un menú de botones. Los textos visibles
(títulos, etiquetas y botones) salen del `ResourceBundle` `mensajes.properties`
(orden alfabético), como el resto de la GUI.

## 6. Tutor didáctico `TutorRelaciones`

Recorre las cuatro cardinales **paso a paso** usando `ILectorDatos` (el `main`
inyecta `LectorDatosDialogos`, pero el tutor solo depende de la interfaz, así
que funcionaría igual con `LectorDatosTexto`):

1. **N:1**: crea un curso eligiendo tema e instructor entre los existentes
   (referencias EAGER ya hidratadas).
2. **N:M con atributos**: inscribe una persona en el curso.
3. **LAZY vs. JOIN FETCH**: tras la búsqueda normal, `inscripcionesCargadas`
   está en falso; tras `cargarInscripciones` queda en verdadero y la colección
   poblada con una sola consulta — se explica el N+1.
4. **1:1**: actualiza el resultado a APROBADO, emite el certificado y **prueba
   que el duplicado es rechazado** por el servicio.
5. **Borrado lógico**: inactiva el curso y muestra que los listados lo omiten.

## 7. Ingesta y pruebas

`IngestaCapacitaciones` (`mvn compile exec:java@ingesta-capacitaciones`) es
idempotente: vacía en orden inverso de dependencias (certificados →
inscripciones → cursos → temas → personas) y siembra temas, cursos,
inscripciones y certificados con FKs válidas, incluida una inscripción NO
APROBADA y una APROBADA **sin** certificado para poder ejercitar la regla.

La suite (`mvn test`) cubre los **cuatro DAOs** (`TemaDAOTest`,
`CursoDAOTest` con `findConInscripciones`, `InscripcionDAOTest` con
`findAprobadasDeCurso`, `CertificadoDAOTest` con `findByInscripcion`) y la
regla de negocio del certificado (`CertificadoServiceTest`: rechaza no aprobada
y duplicado, emite la válida). Cada prueba re-siembra con la ingesta para ser
independiente del orden. 59 pruebas en verde.

## Comandos de referencia

```bash
mvn test                                       # suite completa
mvn compile exec:java@ingesta-capacitaciones   # semeilla idempotente
mvn compile exec:java@tutor-relaciones         # tutor paso a paso (diálogos)
mvn compile exec:java@capacitaciones           # las tres pantallas Swing
```

Incluir también la ingesta y un pase guiado por una pantalla completa
(`exec:java@capacitaciones`) como verificación de flujo.