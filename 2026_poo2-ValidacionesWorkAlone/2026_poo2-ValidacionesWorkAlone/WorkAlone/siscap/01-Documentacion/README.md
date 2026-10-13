# Documentación del proyecto `siscap`

Esta carpeta es la **fuente canónica** de la documentación del proyecto: es la
única documentación que se sube a git (junto con el código fuente). Todo lo que
genere la herramienta de IA como notas, planes o borradores vive en
`99-OtherDocs/` (SOLO LOCAL, ignorada por git).

## Regla de versionado

A git solo suben el **código** y **`01-Documentacion`**. El `.gitignore`
excluye `99-OtherDocs/`, `.freebuff/`, `98-OldSRC/`, `.opencode/` y `openspec/`.

## Estructura

| Carpeta | Contenido | Fuente |
| --- | --- | --- |
| `01-Modelo Dominio/` | Tablas, entidades y vocabulario del dominio. | Canónica |
| `02-Diseño de Base de Datos/` | Diseño, esquemas y decisiones de BD. | Por completar |
| `03-Explicaciones de Código/` | Explicaciones didácticas de partes del código (interfaces, implementaciones, refactors). | Canónica |
| `04-Decisiones de Diseño/` | Registro de decisiones de diseño (ADR) de cada cambio. | Canónica |
| `05-Visión y Roadmap/` | Visión del proyecto y hoja de ruta por etapas. | Canónica |

## Convenciones

- **Categorías numeradas** (`01-`, `02-`, ...) para un orden estable en el
  explorador de archivos.
- Cada cambio con código aporta sus explicaciones en `03-Explicaciones de
  Código/` y su decisión de diseño en `04-Decisiones de Diseño/`.
- Las carpetas nuevas se crean con un `README.md` de marcador para que git las
  pueda rastrear y para documentar su propósito.
- Las notas de trabajo de la IA **no** se copian aquí; quedan en
  `99-OtherDocs/` (local, ignorada).

## Lecturas recomendadas

- `05-Visión y Roadmap/vision.md` — qué es el proyecto y sus principios de trabajo.
- `05-Visión y Roadmap/roadmap.md` — etapas de evolución y buenas prácticas objetivo.