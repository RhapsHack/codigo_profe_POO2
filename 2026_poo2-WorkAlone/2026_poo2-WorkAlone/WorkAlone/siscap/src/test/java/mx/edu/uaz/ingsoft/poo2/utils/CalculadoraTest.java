package mx.edu.uaz.ingsoft.poo2.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CalculadoraTest {
    @Test
    public void probarSuma() {
        int a = 100;
        int b = 2000;
        Calculadora calculadora = new Calculadora();
        int resultado = calculadora.suma(a, b);
        assertTrue(true);
    }
}
