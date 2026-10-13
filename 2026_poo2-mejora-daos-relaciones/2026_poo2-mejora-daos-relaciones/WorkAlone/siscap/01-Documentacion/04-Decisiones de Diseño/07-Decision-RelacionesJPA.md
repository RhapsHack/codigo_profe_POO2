# Decisión: sistema de capacitaciones sobre JPA — mapeo, fetch types y reglas en el servicio

> Cambio: `sistema-capacitaciones-jpa`.
> Explicación en `../03-Explicaciones de Código/relaciones/02-RelacionesJPA_Capacitaciones.md`.

## Contexto

La persistencia ya es JPA (EclipseLink) con `DAOBase<T>` genérico y servicios
`ServicioBase<T,U>`; la GUI reutilizable (`UnicaCatalogo`, validadores,
`EntityPanelFactory`) existe y los catálogos son 4. Las relaciones habían
existido en la era JDBC, pero se retiraron con los CRUD de consola. En esta
etapa se quiso **volver a enseñar las cuatro cardinales sobre JPA** y operarlas
desde pantallas Swing, con un tutor didáctico, reutilizando las piezas
existentes.

## Decisión

### 1. El tema es su propia entidad (`Tema`) con tipo controlado

`Curso` referencia a `Tema` (N:1) en vez de repetir el nombre como texto.
- **Por qué**: evita duplicar el conocimiento entre cursos, da una FK real
  navegable y permite que el certificado sea "del tema".
- **Alternativas descartadas**: `tema` como texto (no hay relación que
  enseñar); referencias polimórficas a los catálogos existentes (complican el
  modelo y contradicen el eje didáctico).

### 2. Política de *fetch types* pensada para enseñar

| Relación | Fetch | Para enseñar |
| --- | --- | --- |
| `Curso.instructor` y `Curso.tema` (`@ManyToOne`) | EAGER | hidratación automática de la referencia |
| `Curso.inscripciones` (`@OneToMany`, `mappedBy="curso"`) | LAZY | la colección llega vacía y se llena al navegar |
| `Inscripcion.curso` y `.persona` (`@ManyToOne`) | EAGER | listar inscripciones con ambas partes pobladas |
| `Certificado.inscripcion` (`@OneToOne`) | LAZY | forzar la navegación explícita |

- La colección es el lado inverso (`mappedBy`); la consistencia bidireccional
  se mantiene fijando ambos lados en los servicios.
- Las referencias EAGER dan el listado poblado sin consultas extra, y la
  colección LAZY es la fuente del problema N+1 que `findConInscripciones`
  (JOIN FETCH) resuelve.

### 3. La N:M con atributos se materializa con `Inscripcion`

Una persona–curso es N:M **con atributos** (fecha y resultado), por eso no se
usa `@ManyToMany` (no admite atributos en la tabla de unión): una entidad
intermedia con dos `@ManyToOne` además es el lugar de la regla de unicidad
`(curso, persona)` y de la evaluación.

### 4. Consulta por DAO concreto: `findByCampo` o Criteria propio

- Igualdad simple → `findByCampo`/`findByCampoUnico` (`findByInstructor`,
  `findByTema`, `findByCurso`, `findByPersona`, `findByInscripcion`).
- Combinar condiciones o poblar una colección → consulta propia con Criteria:
  `findAprobadasDeCurso` (`curso = :c AND resultado = APROBADO`) y
  `findConInscripciones` (`LEFT JOIN FETCH c.inscripciones`).
- **Por qué**: mostrar ambos estilos enseña cuándo una igualdad basta y cuándo
  hace falta un join con condiciones.

### 5. La regla del certificado vive en el servicio (D5)

`CertificadoService.emitir(...)` exige inscripción APROBADA y sin certificado;
si no, lanza **`ReglaNegocioException`** (checked de negocio), separada de
`DAOException` (persistencia). La GUI y el tutor invocan la regla, no la
deciden — así se cumple por cualquier ruta.

### 6. Borrado lógico sin tocar la capa genérica

Para estas cuatro entidades "borrar" es `activo = false` y los listados omite
inactivos; el filtrado se resuelve en la consulta del DAO (`activo = true`),
sin alterar el `DAOBase` de los catálogos para no cambiar su semántica.

### 7. `Tema` se administra en la ventana única (D9)

`CAT_TEMA` se registra como catálogo más: `Constantes`, `DaoServiceFactory`,
`ValidadorEntidadFactory`, `EntityPanelFactory`, `CamposBusquedaConfig` y
`catalogosDisponibles` de `UnicaCatalogo`. Reutiliza el CRUD completo ya
probado (listado, búsqueda, paginación, alta/edición/borrado); la pantalla de
Cursos solo consume el catálogo para el combo. Pantalla dedicada o
mini-catálogo embebido duplicarían código.

### 8. El tutor usa la abstracción de entrada (D7)

`TutorRelaciones` es un `main` que recorre pasos ordenados y lee por
`ILectorDatos`; el `main` inyecta `LectorDatosDialogos`, pero el código depende
solo de la interfaz (se cambiaría a `LectorDatosTexto` sin tocarlo).

## Alternativas descartadas

- **Unidireccionalidad de la colección** (`Curso` → `inscripciones`): no
  permite recorrer `Curso → inscripciones`, que es la relación 1:N a mostrar.
- **`@ManyToMany` para persona–curso**: pierde atributos en la tabla de unión y
  el lugar de la regla de unicidad/evaluación.
- **Regla del certificado en la vista o el DAO**: rompe la capa de servicios y
  dejaría rutas (pantalla vs. tutor) con comportamientos distintos.
- **Cambiar `DAOBase` para el filtro de activos**: afectaría a los catálogos;
  el predicado de activos vive en el `findAll` del DAO concreto.
- **Repetir la validación de unicidad solo en la BD**: la BD protege, pero el
  servicio convierte la excepción en el mensaje claro del usuario.

## Consecuencias

**A favor**

- Se cubren las cuatro cardinales sobre JPA con fetch types explícitos,
  reutilizando la capa genérica, la ventana única y el catálogo `Tema`.
- Tres pantallas Swing + tutor + ingesta idempotente, con reglas de negocio
  en la capa de servicios.
- La suite queda en verde (59 pruebas), incluyendo los cuatro DAOs y la regla
  del certificado.

**En contra / pendiente**

- `EntityManager` estático de vida larga: los listados se refrescan tras cada
  cambio (caché de primer nivel); aceptable y didáctico.
- El esquema se crea fuera de la app: si falta una tabla, la operación falla de
  forma ruidosa con `DAOException`; el rollback es `DROP` de las 4 tablas
  nuevas + revertir `persistence.xml`.
- Los datos obsoletos por caché de primer nivel se atenúan refrescando las
  listas; no se añadió `clear()` a `DAOBase` para no tocar los catálogos.