# Roadmap del proyecto `siscap`

Hoja de ruta por etapas. **Los avances son incrementales y solo se ejecutan
las características que el usuario indica**: no existe un "proyecto final"
predefinido; este documento registra la evolución acordada para no perder el
hilo.

## Etapas realizadas

1. **Catálogos con CRUD en modo texto**: 8 entidades (personas, lenguajes de
   programación, tecnologías, sistemas operativos, herramientas, frameworks,
   bases de datos, habilidades blandas), DAOs por entidad con JDBC y
   `PreparedStatement`, borrado lógico (`activo`) y físico, ingestas
   iniciales y 8 programas CRUD de consola.
2. **Biblioteca de menús** (`IMenu`, `IOpcionMenu`, `OpcionMenu`,
   `MenuTexto`): presentación del menú, lectura y validación en consola,
   estilos de numeración (numérica, letras y romanos). Documentado en
   `03-Explicaciones de Código/menu/`. **Retirada del código** en el cambio
   `retiro-consola-y-menues` (junto con los CRUD de consola); los documentos
   se conservan como material didáctico con nota histórica.
3. **Ciclo 100% Maven**: se incorpora el motor de JUnit 5 (`junit-jupiter-engine`)
   y `exec-maven-plugin` con ejecuciones nombradas; compilar, probar y
   ejecutar sin classpaths a mano. 62 pruebas en verde.
4. **Capa de datos genérica**: `DAOBase<T>` con JDBC y borrado lógico/físico,
   consultas por campo, enriquecimiento de consultas y relaciones entre
   entidades (cursos, inscripciones, certificados y su carga eager/lazy).
5. **Biblioteca `utils.inout` (`ILectorDatos`)**: abstracción de lectura de
   texto, enteros, decimales y booleanos con validación y mensajes por
   defecto; implementación `LectorDatosTexto` (consola) y `LectorDatosDialogos`
   (JOptionPane, compilada sin uso). Los 8 CRUD y el menú leen a través de
   esta abstracción.
6. **Migración a JPA (EclipseLink)**: las 8 entidades son `@Entity` con clave
   primaria `Integer` e `IDENTITY`; `DAOBase<T>` envolvió un `EntityManager`
   (Criteria API para `findAll`, transacciones encapsuladas) y cada DAO concreto
   quedó en dos líneas. La base es SQLite y el esquema ya no se genera desde
   el código. Documentado en
   `03-Explicaciones de Código/daos/01-RefactorCapaDAOs.md`.
7. **Plantilla abstracta de CRUD**: `AbstractCrudEntidad<T>` concentra el menú,
   las cuatro operaciones CRUD y el texto de los prompts
   (`Campo(Valor):`); `CrudBasesDeDatos` es el primer CRUD migrado. Documentado
   en `03-Explicaciones de Código/menu/03-RefactorCrudConMenu.md`.
8. **Gestión gráfica, validación propia y ciclo OpenSpec** (octubre 2026):
   la ventana `PrincipalCatalogos` gestiona 3 catálogos (Personas,
   Lenguajes, Frameworks) con CRUD Swing y controladores; la biblioteca de
   validación sin librerías externas (`AbstractValidador`,
   `ValidadorEntidad` con binding por `Function`) acumula todos los
   errores y el diálogo de captura/edición muestra cada mensaje etiquetado
   con su campo; los errores de persistencia se propagan con
   `DAOException` checked. El alcance se simplificó a **4 catálogos** (se
   retiraron 4 entidades y los 7 CRUD de consola) y el trabajo se organizó
   con el ciclo OpenSpec (cambio → specs → archivo). Documentado en
   `03-Explicaciones de Código/daos/` y `validadores/`.
9. **Ventana única de catálogos** (cambio `ventana-unica-catalogos`): en una
   sola pantalla se selecciona el catálogo (Personas, Lenguajes o
   Frameworks), se listan sus registros paginados (3/5/10), se busca por
   campo (etiqueta → atributo JPA + tipo, valor convertible), se ven los
   detalles y se hacen altas, ediciones y borrados con confirmación sobre el
   mismo formulario. `CatalogoUnicoController` (+ máquina de estados
   ALTA/DETALLE/EDICION) y `UnicaCatalogo` **componen** las piezas ya
   existentes (`IBaseDAO`/`IServicioBase`, `PanelShowEntity`,
   `PanelListEntidades`, `EntityPanelFactory`, `DaoServiceFactory`,
   `ValidadorEntidadFactory`); nada de la vía clásica cambió. Documentado en
   `03-Explicaciones de Código/ventana-unica/01-ReusoYComposicion.md`.
10. **Sistema de capacitaciones sobre JPA** (cambio `sistema-capacitaciones-jpa`):
    vuelven las cuatro cardinales, ahora con entidades JPA (`Tema`, `Curso`,
    `Inscripcion`, `Certificado` con sus *fetch types*: N:1 y dos-por-inscripción
    EAGER, colección `Curso.inscripciones` LAZY, 1:1 del certificado LAZY) y la
    N:M con atributos vía la entidad intermedia `Inscripcion`. DAOs concretos con
    `findByCampo` y Criteria propio (`findAprobadasDeCurso`,
    `findConInscripciones` con JOIN FETCH contra el N+1); servicios con borrado
    lógico y la regla del certificado (`ReglaNegocioException`) en la capa de
    negocio. Tres pantallas Swing (`VentanaCursos`, `VentanaInscripciones`,
    `VentanaCertificados`) integradas en `GestionCapacitaciones`, un tutor
    didáctico `TutorRelaciones` sobre `ILectorDatos`, ingesta idempotente y el
    catálogo `Tema` reutilizando la ventana única. 59 pruebas en verde.
    Documentado en
    `03-Explicaciones de Código/relaciones/02-RelacionesJPA_Capacitaciones.md` y
    `04-Decisiones de Diseño/07-Decision-RelacionesJPA.md`.

## Evolución acordada siguiente

### Entornos de E/S (por etapas, en este orden)

1. ~~Consola de texto (`System.out` + `Scanner`).~~ HECHA. Sustituida por la
   biblioteca `utils.inout`.
2. **Cuadros de diálogo (`JOptionPane`)**: activar
   `LectorDatosDialogos` y probarla como implementación intercambiable del
   mismo contrato (polimorfismo, LSP).
3. **Swing**: ~~interfaz gráfica de escritorio~~ HECHA para 3 catálogos
   (`PrincipalCatalogos`, etapa 8) y con ventana única (`UnicaCatalogo`,
   etapa 9); queda `Bases de Datos` y conviene activar antes
   `LectorDatosDialogos` como paso intermedio.
4. **Web (JSF)** con Tomcat / TomEE / WildFly como entorno servido.

### Persistencia

1. ~~DAOs concretos JDBC~~ → ~~nombres de métodos uniformes~~ →
   ~~`DAOBase<T>` genérico~~. HECHO.
2. ~~Migración a JPA~~. HECHA (etapa 6): el patrón DAO sobrevivió al cambio de
   tecnología, que era justo lo que se quería demostrar.
3. ~~Completar la migración de los CRUD~~ → **redefinido (2026-10-07)**:
   los 7 CRUD de consola y sus 4 entidades se retiraron al simplificar el
   alcance a 4 catálogos (commit `801a96d`); el último CRUD de consola
   (`Bases de Datos`, `PruebaCruds`) y la biblioteca de menús se retiraron
   después en el cambio `retiro-consola-y-menues`. **Hoy la vía de CRUD es
   100% GUI**: `PrincipalCatalogos` (una ventana por catálogo) y la nueva
   `UnicaCatalogo` (ventana única, etapa 9); los `id` de ejecución del
   `pom.xml` son `ingesta-inicial`, `catalogos-graficos` y `catalogo-unico`.

### Relaciones (fase 2)

1. **Intro** ← **HECHA (etapa 10)** con el cambio `sistema-capacitaciones-jpa`:
   `Tema`, `Curso`, `Inscripcion` y `Certificado` como entidades JPA (las cuatro
   cardinales con *fetch types* explícitos), `Tema` administrado en la ventana
   única, tres pantallas Swing, el tutor `TutorRelaciones` (paso a paso:
   guardar, buscar, editar y borrar con relaciones y el contraste LAZY vs. JOIN
   FETCH) e ingesta idempotente. La idea del dominio doble
   (`entidades/jpa` + `entidades/manuales`) quedó descartada: hoy todo es JPA.
2. ~~Mini-tutorial didáctico `TutorRelaciones`~~ → **HECHO** (mismo programa,
   ahora sobre `ILectorDatos` y `LectorDatosDialogos`).

### Calidad (resuelta en el ciclo OpenSpec de octubre 2026)

- ~~Las transacciones de `DAOBase` registran el error y devuelven `null`~~
  → resuelto con `DAOException` checked: el error llega al CRUD y al
  diálogo del usuario (cambio `daos-errores-paginacion`).
- ~~`CalculadoraTest.probarSuma` falla~~ → la suite completa está en
  verde (71 pruebas con `mvn test`; 44 tras retirar las 27 pruebas de la
  biblioteca de menús en `retiro-consola-y-menues`; 59 con el sistema de
  capacitaciones, etapa 10).

## Buenas prácticas objetivo

- Separación de responsabilidades (MVC) y baja cohesión de la lógica con el
  medio de E/S y la persistencia.
- GRASP (información experta, alta cohesión, bajo acoplamiento, controlador,
  fabricación pura...).
- Patrones de diseño clásicos (Template Method, Strategy, etc.) aplicados de
  forma justificada y documentada.
- Patrones JEE (DAO como punto de partida hacia JPA).
- PRINCIPIOS SOLID, en particular programar contra interfaces
  (`ILectorDatos`, `IMenu`, `IBaseDAO`) y contra la abstracción, no el concreto.
- Evitar antipatrones (código duplicado, clases *God*, acoplamiento a
  `System.*` fuera de la capa de E/S).

## Reglas de trabajo

- Cada cambio con código aporta su explicación en
  `03-Explicaciones de Código/` y su decisión de diseño en
  `04-Decisiones de Diseño/`.
- Todos los textos/valores del programa viven en `Constantes`.
- Ejecutar y probar siempre con Maven (`mvn test`,
  `mvn compile exec:java@<id>`), nunca con classpaths a mano.
- La información de la IA (bitácoras locales, planes) vive SOLO en
  `99-OtherDocs/` (local, ignorada por git).