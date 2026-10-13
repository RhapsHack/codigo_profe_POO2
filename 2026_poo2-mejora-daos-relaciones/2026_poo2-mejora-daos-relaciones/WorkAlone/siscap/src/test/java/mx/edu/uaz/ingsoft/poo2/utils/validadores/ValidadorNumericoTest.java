package mx.edu.uaz.ingsoft.poo2.utils.validadores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de {@code min}/{@code max} con valores {@code Double}: el texto del
 * mensaje formatea con {@code %s}, no con {@code %d} fijo (con {@code %d} la
 * registración lanza {@code IllegalFormatConversionException}).
 */
public class ValidadorNumericoTest {

    @Test
    @DisplayName("min/max de un ValidadorNumerico<Double> no lanza error de formato y valida el rango")
    public void minMaxConDoubleNoLanzaErrorDeFormato() {
        ValidadorNumerico<Double> validador = new ValidadorNumerico<>();
        validador.min(1.0);
        validador.max(10.0);

        validador.setValor(100.0);
        assertFalse(validador.validar());
        assertTrue(validador.getErrores().stream().anyMatch(m -> m.contains("maximo")),
                "Debe reportar el máximo incumplido: " + validador.getErrores());

        validador.setValor(0.5);
        assertFalse(validador.validar());
        assertTrue(validador.getErrores().stream().anyMatch(m -> m.contains("minimo")),
                "Debe reportar el mínimo incumplido: " + validador.getErrores());

        validador.setValor(5.0);
        assertTrue(validador.validar());
    }
}
