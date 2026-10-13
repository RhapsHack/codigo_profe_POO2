# Visión del proyecto `siscap`

## Qué es

`Siscap` es una aplicación Java para el registro y la gestión de
conocimientos, cursos y habilidades de los trabajadores de una institución:
personas, catálogos técnicos (lenguajes de programación, tecnologías,
herramientas, frameworks, bases de datos, sistemas operativos), habilidades
blandas, y los cursos, inscripciones y certificaciones que construyen sobre
ellos la relación entre cada persona y sus competencias.

## Cómo se construye

- **Por etapas, con intención didáctica.** El proyecto es ante todo una
  herramienta de aprendizaje de POO y Java. Cada cambio se hace de uno en
  uno, cuando el usuario lo solicita, y enseña un concepto nuevo (o aplica
  uno ya visto) de forma explícita.
- **No existe un "proyecto final" predefinido.** Los avances son
  incrementales y se deciden por indicación del usuario, no por un plan
  cerrado. Esta carpeta y el roadmap registran la ruta acordada para no
  perder el hilo, no un compromiso de entrega.
- **La base es la etapa inicial de catálogos independientes** (8 entidades)
  sobre los que después se construyen asociaciones y consultas más ricas.

## Principios de trabajo

- Implementar desde una base sólida: interfaces, `Constantes`, capa de datos
  con DAOs y pruebas automatizadas.
- Explicar cada cambio en `03-Explicaciones de Código/` y registrar la
  decisión de diseño en `04-Decisiones de Diseño/`.
- Avanzar de entorno en entorno (consola de texto → diálogos → Swing → web),
  manteniendo la lógica separada del medio de E/S para poder cambiar el
  medio sin reescribir la aplicación.

## Fuente de verdad

- La documentación canónica vive en `01-Documentacion/` (sube a git).
- Las notas y bitácoras de la IA viven SOLO en `99-OtherDocs/` (local).
- El índice y las convenciones están en `01-Documentacion/README.md`.