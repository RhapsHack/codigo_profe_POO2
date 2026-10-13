# Decisión: ventana única de catálogos con reuso de piezas GUI y fábricas crudas

> Cambio: `ventana-unica-catalogos`.
> Explicación en `../03-Explicaciones de Código/ventana-unica/01-ReusoYComposicion.md`.

## Contexto

`PrincipalCatalogos`/`GestionGeneral` tienen una ventana y un controlador por
catálogo; añadir un catálogo nuevo multiplica clases. Se pidió una **única
ventana CRUD** por catálogo seleccionable: listado paginado, búsqueda por
campo, alta/detalle/edición y borrado con confirmación, reutilizando lo que ya
existe (`IBaseDAO` con `findAll(pagina,tamano)` y `findByCampo`,
`PanelShowEntity`, `PanelListEntidades`, validadores, `DaoServiceFactory`,
`EntityPanelFactory`).

## Decisión

### 1. Un controlador y una ventana, compuestos por fábricas crudas

`CatalogoUnicoController<T>` reúne servicio + validador + formulario +
listado y cifra la máquina de estados (`ALTA`/`DETALLE`/`EDICION`).
`UnicaCatalogo` es el `JFrame` que pregunta a `DaoServiceFactory`,
`ValidadorEntidadFactory` y `EntityPanelFactory` **por nombre de catálogo**
y reconstruye sus paneles y campos de búsqueda al cambiar de catálogo.

Se reutiliza el patrón del proyecto: las fábricas devuelven un tipo crudo y
quien construye la ventana hace el cast en un punto (`reconstruirCatalogo`),
igual que `ControllerFactory` en `GestionGeneral`; así las fábricas no se
tocan y los catálogos futuros se suman registrándolas únicamente.

### 2. Búsqueda por "etiqueta → atributo JPA + tipo", no por nombre de columna

`CamposBusquedaConfig` mantiene por catálogo un mapa inmutable
`etiqueta (llave del bundle) → { atributoJpa, tipo }` p. ej. `lenguaje_base →
(versionReferencia localizable...)`. La UI y el bundle muestran etiquetas
legibles; la capa persistente recibe el **atributo JPA real** que `findByCampo`
(igualdad exacta) espera. El valor se convierte con `convertirValor(...)`
según el tipo (`STRING` trimado, `INTEGER` `parseInt`, `BOOLEAN`
true/false/verdadero/falso); si no convierte o viene vacío, la ventana avisa y
no resetea la lista (requisito "valor no convertible").

### 3. Paginación sin `count` (diseño heredado de la capa DAO)

No hay consulta de total de registros: `recuperarPagina` devuelve la página y
la barra se apoya en el tamaño real devuelto → "Página X de Y" calcula el
último **según se alcanza** (si la página llega incompleta se asume última y
"Siguiente" se deshabilita). Se documenta para no reclamar el contador

### 4. Transiciones que refrescan listado y botones desde un solo lugar

Todo pasa por `aplicarModo` + `establecerListaEntidades`: selección de
listado → DETALLE; `nuevo()` → ALTA editable vacío (listado con la selección
limpia); `borrar()` con `YES_OPTION` → borrado y refresco a la página actual;
`cancelar()` solo regresa del modo de edición sin persistir; fallos de
validación y de `DAOException` se muestran sin cambiar el modo.

## Alternativas descartadas

- **Una ventana por catálogo (statu quo)**: multiplica clases a cada catálogo;
  el pedido era una ventana única.
- **Catálogo genérico con reflexión de campos de la entidad**: magia y typos
  en runtime; el mapa etiqueta→atributo da verificación y control por catálogo
  (mismos bonos que `Function` en el binding del validador).
- **`Búsqueda por LIKE/substring`**: fuera de alcance, `findByCampo` es igualdad
  exacta (igual en los DAOs actuales).
- **Consultar `COUNT(*)`/`getTotal` para la paginación**: requiere cambiar la
  capa DAO (`IBaseDAO`); un cambio más ancho de lo necesario para la UI.
- **Tocar `PanelShowEntity`/`PanelListEntidades` para orientarlos al CRUD
  único**: solo se añadió un método compatible (`limpiarSeleccion`).
- **LLaves en `Constantes` para los textos nuevos**: el módulo GUI usa
  `ResourceBundle` para el texto visible (estilo de la ventana clásica); las
  llaves van al bundle en orden alfabético.

## Consecuencias

**A favor**

- Tres catálogos hoy con el mismo esquema → cero clases nuevas por catálogo.
- `PrincipalCatalogos` y su CRUD clásico coexisten sin cambios; dos vías de
  ejecución Maven (`catalogos-graficos` y `catalogo-unico`).
- Los nuevos métodos en `IServicioBase` (`recuperarPagina`,
  `buscarPorCampo`) no rompen los servicios concretos (solo extienden
  `ServicioBase`); la suite permanece en verde (44 pruebas).

**En contra / pendiente**

- `UnicaCatalogo` hace el cast crudo de las fábricas en un punto: un catálogo
  que no registre el formulario/validador/servicio se degrada de forma segura
  con un mensaje, pero el mapa de conversión de búsqueda debe mantenerse
  sincronizado con el formulario.
- La confirmación de borrado y los diálogos de búsqueda viven en la ventana
  (capa GUI), igual que en la ventana clásica; `CatalogoUnicoController` no
  depende de Swing excepto por `UnicaCatalogo`/`JOptionPane` (se acepta: la
  alternación de captura/edición ya era GUI en `GestionGeneral`).