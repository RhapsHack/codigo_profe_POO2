package mx.edu.uaz.ingsoft.poo2.utils.inout.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de {@code LectorDatosTexto}. La entrada se simula con un
 * {@code ByteArrayInputStream} inyectado en el constructor; la salida de la
 * consola ({@code System.out} y {@code System.err}) se captura y se restaura
 * en cada prueba.
 */
public class LectorDatosTextoTest {

    private static final PrintStream SALIDA_ORIGINAL = System.out;
    private static final PrintStream ERROR_ORIGINAL = System.err;

    private ByteArrayOutputStream salida;
    private ByteArrayOutputStream error;

    @BeforeEach
    void capturarConsola() {
        salida = new ByteArrayOutputStream();
        error = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salida, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(error, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restaurarConsola() {
        System.setOut(SALIDA_ORIGINAL);
        System.setErr(ERROR_ORIGINAL);
    }

    /** Construye un lector de consola que lee las líneas indicadas. */
    private LectorDatosTexto lector(String lineas) {
        return new LectorDatosTexto(
                new ByteArrayInputStream(lineas.getBytes(StandardCharsets.UTF_8)));
    }

    private String textoSalida() {
        return salida.toString(StandardCharsets.UTF_8);
    }

    private String textoErrores() {
        return error.toString(StandardCharsets.UTF_8);
    }

    // ----- Lectura de texto -----

    @Test
    @DisplayName("leerTexto() lee una línea sin mostrar mensaje")
    public void leerTextoLeelineaSinMensaje() {
        LectorDatosTexto lector = lector("Hola mundo\n");
        assertEquals("Hola mundo", lector.leerTexto());
        assertEquals("", textoSalida());
    }

    @Test
    @DisplayName("leerTexto(mensaje) muestra el mensaje antes de leer")
    public void leerTextoConMensajeLoMuestra() {
        LectorDatosTexto lector = lector("Ana\n");
        assertEquals("Ana", lector.leerTexto("Nombre: "));
        assertTrue(textoSalida().contains("Nombre: "));
    }

    @Test
    @DisplayName("Por defecto se acepta una línea vacía (política true)")
    public void politicaVaciosDefaultAceptaVacio() {
        LectorDatosTexto lector = lector("\n");
        assertEquals("", lector.leerTexto());
    }

    @Test
    @DisplayName("Con permitirValoresVacios=false el vacío se rechaza y se repite")
    public void politicaVaciosFalseRechazaYRepite() {
        LectorDatosTexto lector = lector("\nAna\n");
        lector.setPermitirValoresVacios(false);
        assertEquals("Ana", lector.leerTexto("Nombre: "));
        assertTrue(textoErrores().contains("vacío"));
    }

    @Test
    @DisplayName("leerTextoNoVacio rechaza el vacío aunque la política sea true")
    public void leerTextoNoVacioRechazaVacio() {
        LectorDatosTexto lector = lector("\nAna\n");
        assertEquals("Ana", lector.leerTextoNoVacio("Nombre: "));
        assertTrue(textoErrores().contains("vacío"));
    }

    @Test
    @DisplayName("leerTexto(mensaje, actual) conserva el valor actual con Enter y reemplaza con texto")
    public void leerTextoActualizableConservaORemplaza() {
        LectorDatosTexto conserva = lector("\n");
        assertEquals("Actual", conserva.leerTexto("Nombre: ", "Actual"));

        LectorDatosTexto reemplaza = lector("Nuevo\n");
        assertEquals("Nuevo", reemplaza.leerTexto("Nombre: ", "Actual"));
    }

    // ----- Lectura de entero -----

    @Test
    @DisplayName("leerEntero devuelve un entero válido")
    public void leerEnteroValido() {
        LectorDatosTexto lector = lector("42\n");
        assertEquals(42, lector.leerEntero("Edad: "));
    }

    @Test
    @DisplayName("leerEntero rechaza texto y repite hasta leer un entero")
    public void leerEnteroInvalidoRepite() {
        LectorDatosTexto lector = lector("abc\n7\n");
        assertEquals(7, lector.leerEntero());
        assertTrue(textoErrores().contains("entero"));
    }

    @Test
    @DisplayName("leerEntero con mínimo rechaza valores menores")
    public void leerEnteroRespetarMínimo() {
        LectorDatosTexto lector = lector("0\n5\n");
        assertEquals(5, lector.leerEntero("", 1));
        assertTrue(textoErrores().contains("rango"));
    }

    @Test
    @DisplayName("leerEntero con rango rechaza valores fuera de [min, max]")
    public void leerEnteroRespetarRango() {
        LectorDatosTexto lector = lector("0\n11\n5\n");
        assertEquals(5, lector.leerEntero("", 1, 10));
        assertTrue(textoErrores().contains("rango"));
    }

    @Test
    @DisplayName("leerEnteroActualizable conserva el valor actual con Enter y reemplaza si escribe")
    public void leerEnteroActualizableConserva() {
        LectorDatosTexto conserva = lector("\n");
        assertEquals(10, conserva.leerEnteroActualizable("Id: ", 10));

        LectorDatosTexto reemplaza = lector("7\n");
        assertEquals(7, reemplaza.leerEnteroActualizable("Id: ", 10));
    }

    // ----- Lectura de double -----

    @Test
    @DisplayName("leerDouble devuelve un decimal válido")
    public void leerDoubleValido() {
        LectorDatosTexto lector = lector("3.5\n");
        assertEquals(3.5, lector.leerDouble("Sueldo: "), 0.0001);
    }

    @Test
    @DisplayName("leerDouble rechaza texto y repite hasta leer un decimal")
    public void leerDoubleInvalidoRepite() {
        LectorDatosTexto lector = lector("tres\n2.5\n");
        assertEquals(2.5, lector.leerDouble(), 0.0001);
        assertTrue(textoErrores().contains("decimal"));
    }

    @Test
    @DisplayName("leerDouble con rango rechaza valores fuera de [min, max]")
    public void leerDoubleRespetarRango() {
        LectorDatosTexto lector = lector("1.0\n4.0\n");
        assertEquals(4.0, lector.leerDouble("", 2.0, 5.0), 0.0001);
        assertTrue(textoErrores().contains("rango"));
    }

    @Test
    @DisplayName("leerDoubleActualizable conserva el valor actual con Enter y reemplaza si escribe")
    public void leerDoubleActualizableConserva() {
        LectorDatosTexto conserva = lector("\n");
        assertEquals(2.5, conserva.leerDoubleActualizable("Sueldo: ", 2.5), 0.0001);

        LectorDatosTexto reemplaza = lector("3.0\n");
        assertEquals(3.0, reemplaza.leerDoubleActualizable("Sueldo: ", 2.5), 0.0001);
    }

    // ----- Lectura de booleano -----

    @Test
    @DisplayName("leerBoolean acepta V o F sin importar mayúsculas")
    public void leerBooleanAceptaVF() {
        LectorDatosTexto lector = lector("V\nf\n");
        assertTrue(lector.leerBoolean());
        assertFalse(lector.leerBoolean());
    }

    @Test
    @DisplayName("leerBoolean rechaza valores distintos de V/F y repite")
    public void leerBooleanInvalidoRepite() {
        LectorDatosTexto lector = lector("x\nV\n");
        assertTrue(lector.leerBoolean());
        assertTrue(textoErrores().contains("V o F"));
    }

    @Test
    @DisplayName("leerBoolean(mensaje, actual) conserva el valor actual con Enter")
    public void leerBooleanActualizableConserva() {
        LectorDatosTexto conserva = lector("\n");
        assertTrue(conserva.leerBoolean("", true));

        LectorDatosTexto reemplaza = lector("F\n");
        assertFalse(reemplaza.leerBoolean("", true));
    }

    // ----- Guardas -----

    @Test
    @DisplayName("El valor actual nulo en métodos actualizables es rechazado")
    public void valorActualNuloEsRechazado() {
        LectorDatosTexto lector = lector("");
        assertThrows(IllegalArgumentException.class,
                () -> lector.leerEnteroActualizable("", (Integer) null));
        assertThrows(IllegalArgumentException.class,
                () -> lector.leerDoubleActualizable("", (Double) null));
        assertThrows(IllegalArgumentException.class,
                () -> lector.leerBoolean("", (Boolean) null));
    }

    // ----- Presentación -----

    @Test
    @DisplayName("mostrarTextos(String[]) muestra cada elemento")
    public void mostrarTextosArreglo() {
        LectorDatosTexto lector = lector("");
        lector.mostrarTextos(new String[]{"Uno", "Dos"});
        assertTrue(textoSalida().contains("Uno"));
        assertTrue(textoSalida().contains("Dos"));
    }

    @Test
    @DisplayName("mostrarTextos(List<String>) muestra cada elemento")
    public void mostrarTextosLista() {
        List<String> textos = Arrays.asList("Primero", "Segundo", "Tercero");
        LectorDatosTexto lector = lector("");
        lector.mostrarTextos(textos);
        assertTrue(textoSalida().contains("Primero"));
        assertTrue(textoSalida().contains("Segundo"));
        assertTrue(textoSalida().contains("Tercero"));
    }
}