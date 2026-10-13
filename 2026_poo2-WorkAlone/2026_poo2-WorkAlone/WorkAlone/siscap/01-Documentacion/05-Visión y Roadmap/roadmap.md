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
   `03-Explicaciones de Código/menu/`.
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

## Evolución acordada siguiente

### Entornos de E/S (por etapas, en este orden)

1. ~~Consola de texto (`System.out` + `Scanner`).~~ HECHA. Sustituida por la
   biblioteca `utils.inout`.
2. **Cuadros de diálogo (`JOptionPane`)**: activar
   `LectorDatosDialogos` y probarla como implementación intercambiable del
   mismo contrato (polimorfismo, LSP).
3. **Swing**: interfaz gráfica de escritorio manteniendo la separación
   lógica/medio.
4. **Web (JSF)** con Tomcat / TomEE / WildFly como entorno servido.

### Persistencia

1. ~~DAOs concretos JDBC~~ → ~~nombres de métodos uniformes~~ →
   ~~`DAOBase<T>` genérico~~. HECHO.
2. ~~Migración a JPA~~. HECHA (etapa 6): el patrón DAO sobrevivió al cambio de
   tecnología, que era justo lo que se quería demostrar.
3. **Completar la migración de los CRUD**: pasar los 7 que faltan a
   `AbstractCrudEntidad<T>` (etapa 7) y registrar un `id` de ejecución por
   cada uno en el `pom.xml`.

### Calidad pendiente

- Las transacciones de `DAOBase` registran el error y devuelven `null` en vez
  de propagarlo: el CRUD no puede avisar al usuario de que su cambio no se
  guardó.
- `CalculadoraTest.probarSuma` falla (la implementación de `Calculadora.suma`
  está mal); sirve de ejemplo de por qué las pruebas en verde importan.

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