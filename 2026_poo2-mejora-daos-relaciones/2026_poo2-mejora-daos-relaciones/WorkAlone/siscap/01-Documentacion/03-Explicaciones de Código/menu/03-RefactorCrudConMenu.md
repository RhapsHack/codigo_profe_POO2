# Refactor: los CRUD usan la biblioteca de menús y una plantilla abstracta

> **HISTÓRICO:** los CRUDs de consola (`programas/cruds`) y la biblioteca de
> menús (`utils/menu`) fueron retirados del código en el cambio
> `retiro-consola-y-menues`. Este documento se conserva como material
> didáctico; los archivos que describe ya no existen en `src/`.

## ¿Qué cambia en esta versión?

Los 8 programas de `programas/cruds` (`CrudLenguajesProgramacion`,
`CrudPersonas`, `CrudTecnologias`, `CrudSistemasOperativos`, `CrudHerramientas`,
`CrudFrameworks`, `CrudBasesDeDatos`, `CrudHabilidadesBlandas`) ya NO escriben su
menú a mano: lo construyen con `IMenu`/`MenuTexto` y solo deciden QUÉ hacer con
el valor de la opción elegida.

Después llegó el paso siguiente: **ese código repetido 8 veces se subió a una
clase abstracta**, `AbstractCrudEntidad<T>`, y los CRUD pasaron a ser clases
pequeñas que solo describen su entidad. Hoy `CrudBasesDeDatos` es el único
migrado; los otros 7 conservan su forma propia (y son el trabajo pendiente).

> **Nota (2026-10-07):** los otros 7 CRUD de consola y sus 4 entidades
> (Tecnología, SistemaOperativo, Herramienta, HabilidadBlanda) se retiraron
> del código al simplificar el alcance a 4 catálogos. Hoy 3 catálogos se
> gestionan desde la ventana gráfica (`PrincipalCatalogos`) y
> `CrudBasesDeDatos` queda como muestra de la plantilla en consola: la
> migración de los 7 quedó sin objeto.

## 1. El patrón en cada CRUD (con la biblioteca de menús)

```java
private static final ILectorDatos LECTOR = new LectorDatosTexto();

public static void main(String[] args) {
    LECTOR.mostrarTexto(MSG_MENU_CRUD_TECNOLOGIAS);   // título, una sola vez
    IMenu menu = construirMenu();
    String opcion;
    do {
        opcion = menu.solicitarOpcion();             // muestra, lee y valida
        switch (opcion) {
            case VALOR_OPCION_MOSTRAR_TODOS -> mostrarTodasLasTecnologias();
            case VALOR_OPCION_AGREGAR       -> agregarTecnologia();
            case VALOR_OPCION_ACTUALIZAR    -> actualizarTecnologia();
            case VALOR_OPCION_ELIMINAR      -> borrarTecnologia();
            case IMenu.VALOR_SALIR          -> LECTOR.mostrarTexto(MSG_SALIENDO_PROGRAMA);
        }
    } while (!opcion.equals(IMenu.VALOR_SALIR));
}

private static IMenu construirMenu() {
    IMenu menu = new MenuTexto(LECTOR);               // comparte el LECTOR
    menu.setConCancelar(false);
    menu.setConSalir(true);
    menu.agregarOpcion(VALOR_OPCION_MOSTRAR_TODOS, TEXTO_MENU_MOSTRAR_TODAS_TECNOLOGIAS);
    menu.agregarOpcion(VALOR_OPCION_AGREGAR,       TEXTO_MENU_AGREGAR_TECNOLOGIA);
    menu.agregarOpcion(VALOR_OPCION_ACTUALIZAR,    TEXTO_MENU_ACTUALIZAR_TECNOLOGIA);
    menu.agregarOpcion(VALOR_OPCION_ELIMINAR,      TEXTO_MENU_ELIMINAR_TECNOLOGIA);
    return menu;
}
```

## 2. La plantilla: `AbstractCrudEntidad<T>`

Lo que era idéntico en los 8 programas (el `main`, el `do while`, el `switch`,
las 4 operaciones CRUD) vive una sola vez en la clase abstracta. Lo que cambia
por catálogo son tres cosas, y son las únicas que hay que escribir:

```java
public abstract class AbstractCrudEntidad<T extends IEntidad> {
    protected static final EntityManager EM = JPAUtil.crearEntityManager();
    protected static final ILectorDatos LECTOR = new LectorDatosTexto();
    protected final IBaseDAO<T> dao;

    public abstract void mostrarEntidad(T entidad);     // cómo se imprime un registro
    public abstract void pedirDatosEntidad(T e);        // cómo se pide el registro ya existente
    public abstract T pedirDatosEntidad();              // cómo se pide un registro nuevo
}
```

La plantilla resuelve el resto:

| Operación | Qué hace la plantilla |
| --- | --- |
| `mostrarTodosLosRegistros()` | `dao.findAll()` y delega cada registro en `mostrarEntidad`. |
| `agregarBaseDeDatos()` | pide la entidad con `pedirDatosEntidad()` y hace `dao.save(...)`. El nombre es un residuo de cuando la plantilla solo servía para bases de datos. |
| `actualizarRegistro()` | pide el id, busca con `dao.findById(...)`, delega en `pedirDatosEntidad(entidad)` y hace `dao.update(...)`. |
| `borrarRegistro()` | pide el id, busca con `dao.findById(...)` y hace `dao.deleteById(...)`. |

Y un CRUD completo queda en unas 40 líneas:

```java
public class CrudBasesDeDatos extends AbstractCrudEntidad<BaseDeDatos> {
    public CrudBasesDeDatos() {
        super(new BaseDeDatosDAO(EM));
    }

    @Override
    public void pedirDatosEntidad(BaseDeDatos e) {
        e.setNombre(leerCampoActualizable("Nombre", e.getNombre()));
        e.setModelo(leerCampoActualizable("Modelo", e.getModelo()));
        e.setProveedor(leerCampoActualizable("Proveedor", e.getProveedor()));
        e.setDescripcion(leerCampoActualizable("Descripción", e.getDescripcion()));
    }
}
```

`PruebaCruds` es el `main` que instancia el CRUD y llama a `ejecutar()`.

## 3. El texto del prompt: `Campo(Valor):`

Al actualizar un registro, cada campo se pregunta mostrando su valor actual y
recordando que Enter lo conserva:

```
Nombre(SQLite):Modelo(Relacional):Proveedor(SQLite):Descripción(Motor de base de datos embebido y ligero):
```

Ese texto **no se escribe en cada CRUD**: lo componen dos métodos `protected` de
la plantilla, y por eso los 8 programas podrán reutilizarlos sin duplicar nada:

```java
protected String mensajeActualizable(String nombreCampo, Object valorActual) {
    String valor = (valorActual instanceof Boolean activo)
            ? (activo ? "V" : "F")
            : String.valueOf(valorActual);
    return nombreCampo + "(" + valor + "):";
}

protected String leerCampoActualizable(String nombreCampo, String valorActual) {
    return LECTOR.leerTexto(mensajeActualizable(nombreCampo, valorActual), valorActual);
}
```

`leerCampoActualizable` combina las dos piezas: compone el texto y llama a
`leerTexto(mensaje, valorActual)`, que es el método de la biblioteca `inout` que
devuelve el valor actual cuando el usuario solo presiona Enter. El "queda como
está" lo resuelve el lector, no el CRUD. Para un campo `boolean` se usaría
`LECTOR.leerBoolean(mensajeActualizable("Activo", e.isActivo()), e.isActivo())`.

Este formato sustituyó a `Constantes.mensajeActualizable`, que componía el
mismo aviso en varios renglones (la constante ya se borró del código).

## 4. Cómo responde el usuario al menú

`solicitarOpcion()` acepta dos formas de respuesta y devuelve SIEMPRE el valor:

- La **etiqueta mostrada**: `1`, `2`, ... (o `a`, `b`... si el menú está en
  estilo LETRAS, o `i`, `ii`... en ROMANOS).
- El **valor de la opción**: `agregar`, `mostrar-todos`, ... y `salir`/`cancelar`
  para las opciones automáticas.

Si la respuesta no corresponde a nada, el menú avisa y vuelve a preguntar.

## Conceptos que este refactor ilustra

### 1. Polimorfismo aplicado de verdad
El CRUD declara `IMenu menu`, no `MenuTexto menu`. El programa cliente depende
del CONTRATO, no de la implementación. El día que exista `MenuDialog`,
`construirMenu()` será la ÚNICA línea que cambie.

### 2. Datos vs. comportamiento: el switch sobre el valor
El menú devuelve el **valor** de la opción (`"agregar"`, `"salir"`), no un
número. El `switch` sobre cadenas de Java (válido desde Java 7, con flechas
desde Java 14) queda legible y SIN casos imposibles: ya no existe el
`default -> "opción inválida"` porque el menú no devuelve opciones inválidas.

### 3. Ya no hay confirmación de salida redundante
Antes: opción "5. Salir" → pregunta "¿Desea salir? (S/N)". Ahora la selección
misma es la confirmación: si elegiste "Salir", sales.

### 4. Una sola fuente de lectura
`MenuTexto` recibe en el constructor el `ILectorDatos` que usará para leer. ¿Por
qué importa? Dos lectores sobre `System.in` compiten por la misma entrada: con
teclado "suele funcionar", pero con entrada redirigida se roban líneas entre sí.
El CRUD crea el lector y se lo presta al menú: una sola fuente de lectura. La
plantilla lo resuelve de raíz, con un `LECTOR` estático compartido por el menú y
los campos.

### 5. Constantes como vocabulario del dominio
En `Constantes` están los `VALOR_OPCION_MOSTRAR_TODOS/AGREGAR/ACTUALIZAR/ELIMINAR`
(los valores compartidos por todos los CRUDs) y, por cada tabla, las constantes
`TEXTO_MENU_*` (el texto visible de cada opción). Fíjate en la diferencia de
diseño: el valor es GLOBAL (identifica la acción); el texto es LOCAL a cada
catálogo (lo que ve el usuario).

### 6. Plantilla abstracta en vez de código copiado
`AbstractCrudEntidad<T>` es el esqueleto común y sus subclases aportan solo lo
que varía. La alternativa habría sido 8 copias del `main` y del `switch`: cada
corrección (un mensaje, una validación) habría que hacerse 8 veces. Los métodos
`agregar*`, `actualizar*`, `borrar*` y `mostrarTodos*` no cambiaron de nombre al
migrar: la plantilla conserva el vocabulario del CRUD.

### 7. El método que falta: el `id` ya viene dado
`actualizarRegistro()` ya buscó la entidad y la pasa a `pedirDatosEntidad(e)`. Por
eso el método recibe la entidad y **no vuelve a pedir el id**: pedirlo dos veces
sería un error de la interfaz (el usuario escribiría el id dos veces) y buscar
otra vez devolvería otra instancia.

## Para probar

```bash
mvn compile
mvn compile exec:java -Dexec.mainClass=mx.edu.uaz.ingsoft.poo2.programas.cruds.PruebaCruds
```

Con el log SQL en `FINE` (ver `daos/01-RefactorCapaDAOs.md` §4) se ve el
`SELECT`, el `UPDATE` y el `INSERT` que produce cada opción del menú.

## Pendientes

- ~~Migrar los 7 CRUD restantes a `AbstractCrudEntidad<T>`~~ → sin objeto:
  los 7 CRUD de consola se retiraron del código (ver nota al inicio).
- En `CrudBasesDeDatos`, `pedirDatosEntidad` conserva un `dao.update(...)` y un
  mensaje "actualizada" que la plantilla ya emite: hay que quitar uno de los dos
  (hoy se ve el aviso duplicado).
- `mostrarEntidad` concatena los campos sin separadores
  (`ID1Activo:trueNombre:...`); las constantes `MSG_DETALLE_*` de `Constantes`
  ya tienen el formato adecuado.
- ~~Registrar en el `pom.xml` un `id` de ejecución por cada CRUD migrado.~~
  → hecho: `ingesta-inicial`, `catalogos-graficos` y `crud-bases-datos`.

## Preguntas para discutir en clase

1. En `construirMenu()` el tipo de la variable es `IMenu` pero el objeto es
   `MenuTexto`. ¿Qué líneas cambiarían para usar un futuro `MenuDialog`?
2. ¿Por qué el valor de la opción es una constante compartida
   (`VALOR_OPCION_AGREGAR`) mientras que el texto vive en una constante por
   tabla (`TEXTO_MENU_AGREGAR_TECNOLOGIA`)?
3. `pedirDatosEntidad` tiene dos sobrecargas con el mismo nombre
   (`()` y `(T)`). ¿Es la mejor forma de distinguir "crear" de "actualizar", o
   serían más claros dos nombres (`crearEntidad` / `actualizarEntidad`)?
4. `LECTOR` y `EM` son `static` en la plantilla. ¿Qué dificultades de prueba
   introduce eso? ¿Cómo los inyectarías?
5. La plantilla reutiliza textos genéricos (`MSG_ENCABEZADO`,
   `MSG_ENTIDAD_ACTUALIZADA`) y deja sin usar los textos por catálogo
   (`MSG_ENCABEZADO_BASES_DE_DATOS`, ...). ¿Qué se pierde y qué se gana?

## Siguiente paso (acordado con el docente)

~~Completar la migración de los 7 CRUD que faltan~~ → los 7 CRUD de consola se
retiraron del código (nota al inicio); el paso acordado ahora es decidir si
`Bases de Datos` sube a la misma ventana gráfica y activar
`LectorDatosDialogos` (JOptionPane) para demostrar la intercambiabilidad real
del mismo contrato: mismo programa, distinto medio de entrada.
