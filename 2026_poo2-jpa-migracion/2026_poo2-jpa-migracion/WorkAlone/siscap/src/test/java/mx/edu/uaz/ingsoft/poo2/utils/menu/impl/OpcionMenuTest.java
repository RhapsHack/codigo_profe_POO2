package mx.edu.uaz.ingsoft.poo2.utils.menu.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de OpcionMenu: construcción, validación, capitalización,
 * cambio de texto y de estado, y representación textual.
 */
public class OpcionMenuTest {

    @Test
    @DisplayName("El constructor mínimo usa la cadena como valor y la capitaliza como texto")
    public void constructorMinimoCapitalizaElTexto() {
        OpcionMenu opcion = new OpcionMenu("abrir");
        assertEquals("abrir", opcion.getValor());
        assertEquals("Abrir", opcion.getTexto());
        assertTrue(opcion.isHabilitada());
    }

    @Test
    @DisplayName("La capitalización respeta acentos")
    public void laCapitalizacionRespetaAcentos() {
        OpcionMenu opcion = new OpcionMenu("árbol");
        assertEquals("Árbol", opcion.getTexto());
    }

    @Test
    @DisplayName("El constructor completo respeta valor, texto y estado recibidos")
    public void constructorCompletoRespetaLosParametros() {
        OpcionMenu opcion = new OpcionMenu("agregar", "Agregar registro", false);
        assertEquals("agregar", opcion.getValor());
        assertEquals("Agregar registro", opcion.getTexto());
        assertFalse(opcion.isHabilitada());
    }

    @Test
    @DisplayName("El valor no puede ser nulo ni vacío")
    public void valorNuloOVacioEsRechazado() {
        assertThrows(IllegalArgumentException.class, () -> new OpcionMenu(null, "Texto", true));
        assertThrows(IllegalArgumentException.class, () -> new OpcionMenu("", "Texto", true));
        assertThrows(IllegalArgumentException.class, () -> new OpcionMenu("   ", "Texto", true));
    }

    @Test
    @DisplayName("El texto no puede ser nulo ni vacío")
    public void textoNuloOVacioEsRechazado() {
        assertThrows(IllegalArgumentException.class, () -> new OpcionMenu("valor", null, true));
        assertThrows(IllegalArgumentException.class, () -> new OpcionMenu("valor", "", true));
    }

    @Test
    @DisplayName("El valor es inmutable; el texto sí puede cambiarse")
    public void elValorEsInmutableYElTextoCambia() {
        OpcionMenu opcion = new OpcionMenu("guardar", "Guardar", true);
        opcion.setTexto("Guardar todo");
        assertEquals("Guardar todo", opcion.getTexto());
        assertEquals("guardar", opcion.getValor());
        assertThrows(IllegalArgumentException.class, () -> opcion.setTexto(""));
    }

    @Test
    @DisplayName("La opción puede habilitarse e inhabilitarse")
    public void elEstadoHabilitadaCambia() {
        OpcionMenu opcion = new OpcionMenu("reportes", "Reportes", true);
        opcion.setHabilitada(false);
        assertFalse(opcion.isHabilitada());
        opcion.setHabilitada(true);
        assertTrue(opcion.isHabilitada());
    }

    @Test
    @DisplayName("toString incluye valor y texto")
    public void toStringIncluyeValorYTexto() {
        OpcionMenu opcion = new OpcionMenu("abrir", "Abrir", true);
        String texto = opcion.toString();
        assertTrue(texto.contains("abrir"));
        assertTrue(texto.contains("Abrir"));
    }
}
