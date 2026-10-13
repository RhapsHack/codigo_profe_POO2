# El enum `EstiloNumeracionMenu` pasa al modelo

## ¿Qué cambia en esta versión?

El enum que define los estilos de numeración de los menús ya no vive anidado
dentro de la interfaz `IMenu`: ahora es una enumeración propia del modelo.

| Antes | Ahora |
| --- | --- |
| `IMenu.EstiloNumeracion` (enum anidado en la interfaz) | `modelo/enumeraciones/EstiloNumeracionMenu` |

Todos los usos se actualizaron (`IMenu`, `MenuTexto`). El comportamiento no
cambia: `NUMEROS`, `LETRAS` y `ROMANOS` siguen etiquetando igual.

```java
// Antes
menu.setEstiloNumeracion(IMenu.EstiloNumeracion.LETRAS);
// Ahora
menu.setEstiloNumeracion(EstiloNumeracionMenu.LETRAS);
```

## ¿Por qué moverlo? (discusión de diseño)

1. **Es un concepto del dominio, no un detalle de una interfaz.** "Cómo se
   numera un menú" es información del problema que resolvemos (presentación de
   menús), y el proyecto ya tiene un lugar para conceptos así: el modelo. Las
   interfaces de menú quedan más limpias: contratan QUÉ se configura
   (`setEstiloNumeracion`), no DÓNDE vive el tipo.

2. **Evita acoplar clientes a una interfaz.** Si mañana otro módulo quisiera
   saber qué estilos existen, no tendría que depender de `IMenu` (ni
   implementarla) para usar el enum.

3. **Un enum de Java es mucho más que una lista de constantes** (tema para la
   siguiente actualización): puede tener constructores, campos, métodos y hasta
   comportarse polimórficamente. Al darle su propio archivo, le estamos dando
   su propio espacio para crecer.

4. **Convención de paquetes:** el proyecto usa `modelo` en singular
   (`modelo.entidades`, `modelo.daos`), así que la nueva casa es
   `modelo.enumeraciones`.

## Conceptos que este cambio ilustra

- **Cohesión y ubicación de tipos:** cada tipo vive donde comunica mejor su
  papel; un enum de dominio no es "paquetería interna de una interfaz".
- **Refactor seguro:** se movió y renombró el tipo, se actualizaron los usos y
  el programa compila y se comporta igual. Refactorizar es cambiar la
  estructura SIN cambiar el comportamiento.
- **Nombres que se leen solos:** `EstiloNumeracionMenu` dice QUÉ es y PARA QUÉ
  se usa; `EstiloNumeracion` a secas era ambiguo (¿numeración de qué?).

## Siguiente paso

Menú para los programas principales y la evolución de los DAOs (nombres
uniformes → clase base genérica → JPA), según lo acordado en clase.
