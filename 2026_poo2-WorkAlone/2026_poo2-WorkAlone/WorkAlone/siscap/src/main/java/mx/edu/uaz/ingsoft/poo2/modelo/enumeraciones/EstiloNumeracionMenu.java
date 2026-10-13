package mx.edu.uaz.ingsoft.poo2.modelo.enumeraciones;

/**
 * Estilos de etiquetado que un menú puede usar para presentar sus opciones.
 * La etiqueta se asigna por posición al momento de generar el menú:
 *
 * NUMEROS: 1, 2, 3, ... (predeterminado)
 * LETRAS:  a, b, c, ... z, aa, ab, ...
 * ROMANOS: i, ii, iii, iv, ...
 *
 * Nota didáctica: el enum vive en el modelo (modelo.enumeraciones) porque es
 * un concepto del dominio del problema, no un detalle de una clase concreta:
 * cualquier implementación de menú (MenuTexto, MenuDialog, ...) lo usa, y
 * así ningún cliente de las interfaces queda acoplado a una clase en
 * particular.
 */
public enum EstiloNumeracionMenu {
    NUMEROS,
    LETRAS,
    ROMANOS
}
