# Catalogos iniciales del modelo de dominio

Este conjunto de tablas representa catalogos independientes. En esta etapa no tienen relaciones entre si y se pueden usar despues para construir tablas intermedias o asociaciones con `Persona`.

## Persona

| Campo | Tipo sugerido | Descripcion | Ejemplo |
| --- | --- | --- | --- |
| idPersona | INTEGER | Identificador de la persona. | 1 |
| nombre | VARCHAR(80) | Nombre o nombres de la persona. | Ana Laura |
| primerApellido | VARCHAR(80) | Primer apellido. | Martinez |
| segundoApellido | VARCHAR(80) | Segundo apellido. | Lopez |
| numeroEmpleado | VARCHAR(20) | Clave interna de la empresa o escuela. | EMP-001 |
| correoElectronico | VARCHAR(120) | Correo de contacto. | ana.martinez@empresa.com |

## ProgLanguages

| Campo | Tipo sugerido | Descripcion | Ejemplo |
| --- | --- | --- | --- |
| idProgLanguage | INTEGER | Identificador del lenguaje. | 1 |
| nombre | VARCHAR(60) | Nombre del lenguaje. | Java |
| paradigma | VARCHAR(80) | Paradigma principal o dominante. | Orientado a objetos |
| versionReferencia | VARCHAR(30) | Version usada como referencia. | 21 |
| descripcion | VARCHAR(200) | Nota corta sobre el lenguaje. | Lenguaje usado para aplicaciones empresariales |

## Tecnologia

| Campo | Tipo sugerido | Descripcion | Ejemplo |
| --- | --- | --- | --- |
| idTecnologia | INTEGER | Identificador de la tecnologia. | 1 |
| nombre | VARCHAR(80) | Nombre de la tecnologia. | Docker |
| categoriaGeneral | VARCHAR(80) | Grupo amplio al que pertenece. | Contenedores |
| fabricanteComunidad | VARCHAR(100) | Empresa o comunidad responsable. | Docker Inc. |
| descripcion | VARCHAR(200) | Nota corta sobre la tecnologia. | Plataforma para empaquetar aplicaciones en contenedores |

## SistemaOperativo

| Campo | Tipo sugerido | Descripcion | Ejemplo |
| --- | --- | --- | --- |
| idSistemaOperativo | INTEGER | Identificador del sistema operativo. | 1 |
| nombre | VARCHAR(80) | Nombre del sistema operativo. | Ubuntu |
| familia | VARCHAR(60) | Familia principal. | Linux |
| proveedor | VARCHAR(100) | Empresa o comunidad responsable. | Canonical |
| descripcion | VARCHAR(200) | Nota corta sobre el sistema operativo. | Distribucion usada en servidores y desarrollo |

## Herramienta

| Campo | Tipo sugerido | Descripcion | Ejemplo |
| --- | --- | --- | --- |
| idHerramienta | INTEGER | Identificador de la herramienta. | 1 |
| nombre | VARCHAR(80) | Nombre de la herramienta. | Git |
| tipo | VARCHAR(80) | Tipo de herramienta. | Control de versiones |
| proveedor | VARCHAR(100) | Empresa o comunidad responsable. | Software Freedom Conservancy |
| descripcion | VARCHAR(200) | Nota corta sobre la herramienta. | Herramienta para control de versiones distribuido |

## Framework

| Campo | Tipo sugerido | Descripcion | Ejemplo |
| --- | --- | --- | --- |
| idFramework | INTEGER | Identificador del framework. | 1 |
| nombre | VARCHAR(80) | Nombre del framework. | Spring Boot |
| tipo | VARCHAR(80) | Tipo de framework. | Backend |
| lenguajeBase | VARCHAR(80) | Lenguaje principal con el que trabaja. | Java |
| descripcion | VARCHAR(200) | Nota corta sobre el framework. | Framework para crear aplicaciones empresariales |

## BaseDeDatos

| Campo | Tipo sugerido | Descripcion | Ejemplo |
| --- | --- | --- | --- |
| idBaseDeDatos | INTEGER | Identificador del motor o tecnologia de datos. | 1 |
| nombre | VARCHAR(80) | Nombre de la base de datos. | SQLite |
| modelo | VARCHAR(80) | Modelo principal de datos. | Relacional |
| proveedor | VARCHAR(100) | Empresa o comunidad responsable. | SQLite Consortium |
| descripcion | VARCHAR(200) | Nota corta sobre la base de datos. | Base de datos ligera embebida |

## HabilidadBlanda

| Campo | Tipo sugerido | Descripcion | Ejemplo |
| --- | --- | --- | --- |
| idHabilidadBlanda | INTEGER | Identificador de la habilidad. | 1 |
| nombre | VARCHAR(80) | Nombre de la habilidad. | Comunicacion efectiva |
| categoria | VARCHAR(80) | Grupo general de la habilidad. | Comunicacion |
| nivelEsperado | VARCHAR(40) | Nivel que la empresa podria usar como referencia. | Intermedio |
| descripcion | VARCHAR(200) | Nota corta sobre la habilidad. | Capacidad para expresar ideas de forma clara |

## Comentario de diseno

Se mantuvo la clase `Tecnologia` porque ya existe y puede servir como un catalogo amplio e intencionalmente ambiguo para tus primeras etapas. Al mismo tiempo, se agregaron catalogos mas especificos para que despues puedas mostrar una evolucion del modelo y discutir por que separar conceptos como sistemas operativos, herramientas, frameworks y bases de datos.
