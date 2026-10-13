# Decisión: Tutor 01 — CRUD didáctico sobre tablas con relaciones

## Contexto

Después de implementar la capa de persistencia JDBC con relaciones
(`cursos`, `inscripciones`, `certificados`), se necesita un **programa
didáctico** que muestre a la clase el ciclo completo del DAO sobre una entidad
con relaciones: guardar, buscar, editar y borrar. Debe reutilizar los DAOs y la
capa `utils.inout` ya existentes, ser **autocontenido** (no depender de haber
corrido la ingesta) y servir de material de discusión sobre cargas eager/lazy
y sobre borrado lógico vs. físico.

## Decisión

Se creó un **único programa de consola** `TutorRelaciones` en la subcarpeta
`programas/tutores/tutor_01/`, ejecutable con `mvn compile exec:java@tutor-01`
(exec `tutor-01` en el `pom.xml`).

- Estructura lineal de **6 fases numeradas** (iniciar/vaciar, guardar, buscar,
  editar, borrar, cierre). Cada fase explica qué hará, ejecuta la operación
  real con los DAOs y espera Enter.
- Medio de E/S: `ILectorDatos` (concreto `LectorDatosTexto`), igual que los
  CRUD. Todos los textos viven en `Constantes` como bloque `MSG_TUT_*`.
- La fase 3 es el **foco didáctico**: se muestra la referencia M:1 que llega
  eager, la colección 1:M que llega vacía (lazy, `estaLlenaInscripciones()`
  falso) y el paso extra `llenarInscripciones`, además de la 1:1.
- El vaciado (fase 1) y el borrado físico (fase 5) respetan el **orden de
  claves foráneas**: certificados → inscripciones → cursos.
- La fase 5 aprovecha el `PRAGMA foreign_keys = ON` para demostrar en vivo que
  el **borrado lógico no libera la FK**: la inscripción borrada lógicamente
  bloquea el borrado físico del curso hasta que también se elimina físicamente.

Vínculos: explicación completa en
`../03-Explicaciones de Código/tutores/01-Tutor01_CRUD_con_Relaciones.md` y
especificación en el cambio OpenSpec `tutorial-crud-relaciones`.

## Alternativas descartadas

- **Varios programas pequeños (uno por operación)** → Un solo recorrido lineal
  es más claro para la clase y evita repetición. La carpeta `tutores/` queda
  preparada para futuros `tutor_02...`.
- **Diálogos con `JOptionPane` (lector dialogado)** → Se descarta en esta
  iteración: el proyecto interactúa por consola. Al programar contra
  `ILectorDatos`, el cambio de medio llegará con `LectorDatosDialogos` sin
  tocar la clase.
- **`System.out` + `Scanner` directos** → Ya no se usa en el proyecto (capa
  `utils.inout`).
- **Reutilizar `IngestaCapacitaciones` para la siembra** → Ocultaría el paso
  didáctico de "crear objetos en memoria y persistirlos"; además la ingesta
  vacía solo filas activas. El tutorial crea sus propios datos (y personas si
  faltan), quedando autocontenido.
- **Agregar un DAO que borre físicamente tablas completas**
  (`getQueryDeleteFisicoTodo` no expuesto) → Fuera de alcance: la fase 1 usa el
  mismo patrón `borradoFisico(obtenerTodas())` de la ingesta, y los DAOs no se
  modifican en este cambio.

## Consecuencias

- El programa es seguro de re-ejecutar: la fase 1 vacía y la fase 5 deja las
  tres tablas sin filas, por lo que una corrida completa es idempotente (se
  verificó con `mvn compile exec:java@tutor-01` dos veces seguidas).
- Riesgo mitigado: el borrado físico del curso solo se ejecuta después de
  eliminar físicamente certificados, inscripciones activas y la inscripción
  borrada lógicamente, que todavía referencia al curso por FK.
- `mvn test` sigue en verde (62 pruebas); no se alteró ningún DAO.