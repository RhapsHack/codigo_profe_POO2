# Ventana única de catálogos: reuso y composición

> Cambio: `ventana-unica-catalogos`.
> Decisión asociada: `../04-Decisiones de Diseño/06-Decision-VentanaUnica.md`.

## Propósito

Hasta aquí la vía GUI era "una ventana por catálogo" (`PrincipalCatalogos`
con `GestionGeneral` y familia). Este cambio añade **una sola ventana**
(`UnicaCatalogo`) que gestiona los tres catálogos: se selecciona el catálogo,
se listan sus registros paginados, se busca por campo, se ven los detalles y
se hacen altas, ediciones y borrados sobre el mismo formulario.

La ventana no reimplementa nada: **compone las piezas existentes** del CRUD
clásico y añade una fábrica de datos y un controlador únicos.

## Piezas reutilizadas

| Pieza | Rol en la ventana única |
|---|---|
| `IBaseDAO<T>` (`findAll(pagina,tamano)`, `findByCampo(campo,valor)`, `guardar`, `actualizar`, `borrar`) | Persistencia (JPA + SQLite detectada en `EntityManager`). |
| `IServicioBase<T>` / `ServicioBase<T>` | Fachada de servicio; expone `recuperarPagina(pagina,tamano)` y `buscarPorCampo(campo,valor)` delegados en el DAO. |
| `DaoServiceFactory.getServicio(nombre)` | Devuelve el servicio crudo del catálogo; `Optional.empty()` si no lo registra (degrado seguro). |
| `ValidadorEntidadFactory.crearValidador(nombre)` | Validador crudo por catálogo; `Optional.empty()` si no registrado. |
| `EntityPanelFactory.createPanelShowEntity(nombre)` | Formulario (`PanelShowPeople`, `PanelShowFramework`, `PanelShowProgLang`) a la derecha. |
| `PanelShowEntity<T>` | Estado del formulario: `setEntidad`, `actualizarEntidadDesdeFormulario`, `getEntidadFromFormulario`, `setEditable`, `limpiarFormulario`. |
| `PanelListEntidades<T>` | Listado paginado (`JList`) a la izquierda; se añadió `limpiarSeleccion()` para el refresco tras guardar/borrar. |
| `MensajesResourceBundle.MENSAJES` | Todo el texto visible, incluidas las llaves nuevas (7.1 del bundle en orden alfabético). |
| `IValidadorEntidad<T>` | Validación acumulativa por campo (etiqueta + mensaje) antes de persistir. |
| `DAOException` (checked) | Errores de persistencia que el controlador convierte en diálogo. |

## Cómo se conecta

```
UnicaCatalogo (JFrame): selector de catálogo + búsqueda + paginación + botonera
        │  construye el controlador por catálogo
        ▼
CatalogoUnicoController<T>   ←  reúne servicio, validador, formulario, listado
        │     │       │
        │     │       └─ PanelShowEntity<T>  (derecha)
        │     │              · listener de selección → detalle
        │     └─ PanelListEntidades<T>  (izquierda)
        │            · listener de selección → DETALLE o (con datos) la entidad elegida
        └─ IServicioBase<T>  →  IBaseDAO<T>  (JPA)
```

La ventana pregunta a las fábricas por nombre de catálogo
(`CAT_PEOPLE`/`CAT_FRAMEWORK`/`CAT_LANG_PROG` de `Constantes`); si alguna no
lo registra, no reconstruye el panel y muestra `catalogo.desconocido` (sin
crash). Es el mismo estilo "fábrica cruda + cast" de `ControllerFactory`.

## Máquina de estados

`CatalogoUnicoController<T>` mantiene `Modo {ALTA, DETALLE, EDICION}` y un
`modoActual`; cada transición refresca el listado o el estado de los botones
vía `UnicaCatalogo.aplicarModo(...)`.

| Transición | Disparador | Acción |
|---|---|---|
| → ALTA | `nuevo()` | formulario editable vacío; El formulario es `setEditable(true)` + `limpiarFormulario()`. |
| → DETALLE | selección en el listado / tras guardar o borrar | formulario de solo lectura con la entidad. |
| → EDICION | `editar()` (solo desde DETALLE con registro) | formulario editable con los datos cargados. |
| → ALTA | `cancelar()` desde ALTA/EDICION | deja de editar y vuelve a estado limpio (sin cambios). |
| → DETALLE | `aceptar()` con validación OK (ALTA) | `guardar`; refresca página actual y el listado. |
| → DETALLE | `aceptar()` con validación OK (EDICION) | `actualizar`; refresca listado. |
| → DETALLE | `borrar()` con confirmación `YES_OPTION` | `borrar` físico; refresca listado y prepara el siguiente registro. |

Fallo de validación → lista de errores en un diálogo (no cambia el modo).
Fallo de persistencia → `DAOException` capturada y mostrada en diálogo
(`uso.del.error`), conservando el modo para intentarlo de nuevo.

## Paginación y búsqueda

- **Paginación**: `recuperarPagina(1, N)` en el constructor y en cada
  transición; `irPaginaSiguiente`/`irPaginaAnterior` cambian el número y
  recargan. La etiqueta es `Página X de N` donde la página **"anterior"**
  queda deshabilitada en la 1 y Siguiente se deshabilita (por diseño, sin
  `count`) cuando la página llega incompleta.
- **Búsqueda por campo**: el combo de campo se llena con las etiquetas
  (`Página`… `id`, `nombre`, … `activo`) de `CamposBusquedaConfig`; el
  mapa traduce etiqueta → atributo JPA real p. ej. `lenguajeBase`,
  `versionReferencia`. El controlador abre `campo.busqueda` con el valor
  convertido según el tipo (STRING/INTEGER/BOOLEAN) y `buscarPorCampo(...)`;
  un valor vacío o no convertible se avisa con su diálogo y no resetea la
  página. La lista de resultados se muestra sobre el listado del catálogo
  actual y la barra `navegarBusqueda` vuelve al modo normal (`Página`).
- **Cambio de tamaño de página** por el combo (3/5/10): vuelve a la página 1.

## Lo que SÍ se modificó en lo clásico

- `IServicioBase` / `ServicioBase`: dos métodos nuevos delegados en el DAO
  (sin tocar firmas existentes → los servicios concretos solo extienden
  `ServicioBase`, no cambian).
- `PanelListEntidades`: método nuevo `limpiarSeleccion()` (compatible).
- `mensajes.properties`: llaves nuevas insertadas en orden alfabético.

`PrincipalCatalogos` y el CRUD clásico se conservan intactos y coexisten con
la nueva vía (idas Maven con `ejecuta` `catalogo-unico`).