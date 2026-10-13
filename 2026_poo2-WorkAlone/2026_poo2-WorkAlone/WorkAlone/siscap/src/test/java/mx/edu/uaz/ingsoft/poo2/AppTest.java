package mx.edu.uaz.ingsoft.poo2;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }
    @Test
    public void probarSuma(){
        int a;
        int b;
        int resultado;
        a = 100;
        b = 2000;
        resultado = a + b +5;
        assertTrue(resultado == 2105);
    }
}
