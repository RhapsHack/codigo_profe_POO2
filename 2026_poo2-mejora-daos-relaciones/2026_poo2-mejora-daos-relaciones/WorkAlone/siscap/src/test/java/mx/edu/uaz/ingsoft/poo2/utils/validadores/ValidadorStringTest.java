package mx.edu.uaz.ingsoft.poo2.utils.validadores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas del comportamiento observable de un validador de campo:
 * acumulación de todos los mensajes incumplidos, caso nulo, copia
 * inmutable de errores y fallo rápido ante condición nula.
 */
public class ValidadorStringTest {

    @Test
    @DisplayName("Un valor que incumple dos condiciones acumula los dos mensajes, en orden de registro")
    public void dosCondicionesIncumplidasAcumulaLosDosMensajes() {
        ValidadorString validador = new ValidadorString().noVacia().soloAlfabeticas();
        validador.setValor("   ");

        assertFalse(validador.validar());
        assertEquals(
                List.of("La cadena está vacía", "El texto debe ser solo alfabéticas"),
                validador.getErrores());
    }

    @Test
    @DisplayName("Cuando todas las condiciones se cumplen, el campo no produce errores")
    public void todasLasCondicionesCumplidasNoReportaErrores() {
        ValidadorString validador = new ValidadorString().noVacia().soloAlfabeticas();
        validador.setValor("Ana");

        assertTrue(validador.validar());
        assertTrue(validador.getErrores().isEmpty());
    }

    @Test
    @DisplayName("Un valor nulo reporta un error que indica que el valor es nulo")
    public void valorNuloReportaErrorDeNulo() {
        ValidadorString validador = new ValidadorString().noVacia();
        validador.setValor(null);

        assertFalse(validador.validar());
        assertEquals(List.of("El valor a evaluar es nulo"), validador.getErrores());
    }

    @Test
    @DisplayName("getErrores() devuelve copia inmutable: tocarla no altera los errores internos")
    public void getErroresDevuelveCopiaInmutable() {
        ValidadorString validador = new ValidadorString().noVacia();
        validador.setValor("");
        assertFalse(validador.validar());

        List<String> errores = validador.getErrores();
        assertEquals(1, errores.size());
        assertThrows(UnsupportedOperationException.class, errores::clear);
        assertEquals(1, validador.getErrores().size(),
                "La consulta posterior debe seguir devolviendo los errores originales");
    }

    @Test
    @DisplayName("Registrar una condición nula falla de inmediato con un error claro")
    public void registrarCondicionNulaFallaDeInmediato() {
        ValidadorString validador = new ValidadorString();

        NullPointerException error = assertThrows(NullPointerException.class,
                () -> validador.addCondicion(null, "mensaje de la condición"));

        assertTrue(error.getMessage().contains("condición"),
                "El mensaje debe señalar la condición nula: " + error.getMessage());
    }
}
