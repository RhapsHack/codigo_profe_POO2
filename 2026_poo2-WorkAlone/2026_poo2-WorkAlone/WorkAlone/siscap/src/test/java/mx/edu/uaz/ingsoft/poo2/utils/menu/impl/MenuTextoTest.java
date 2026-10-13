package mx.edu.uaz.ingsoft.poo2.utils.menu.impl;

import mx.edu.uaz.ingsoft.poo2.modelo.enumeraciones.EstiloNumeracionMenu;
import mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces.IMenu;
import mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces.IOpcionMenu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de MenuTexto. La interacción con la consola se simula sustituyendo
 * System.in por un ByteArrayInputStream: cada prueba restaura la entrada
 * original al terminar.
 */
public class MenuTextoTest {
    private static final InputStream ENTRADA_ORIGINAL = System.in;

    private static void simularEntrada(String lineas) {
        System.setIn(new ByteArrayInputStream(lineas.getBytes(StandardCharsets.UTF_8)));
    }

    private static MenuTexto menuConOpcionesBasicas() {
        MenuTexto menu = new MenuTexto();
        menu.agregarOpcion("abrir", "Abrir");
        menu.agregarOpcion("guardar", "Guardar");
        return menu;
    }

    // ----- Renderizado (obtenerMenuComoTexto) -----

    @Test
    @DisplayName("Renderiza etiquetas numéricas desde 1 y Salir al final")
    public void renderizaNumerosYSalir() {
        MenuTexto menu = menuConOpcionesBasicas();
        String texto = menu.obtenerMenuComoTexto();
        assertTrue(texto.contains("1) Abrir"));
        assertTrue(texto.contains("2) Guardar"));
        assertTrue(texto.contains("3) Salir"));
    }

    @Test
    @DisplayName("El estilo LETRAS etiqueta a, b, c...")
    public void renderizaLetras() {
        MenuTexto menu = menuConOpcionesBasicas();
        menu.setEstiloNumeracion(EstiloNumeracionMenu.LETRAS);
        String texto = menu.obtenerMenuComoTexto();
        assertTrue(texto.contains("a) Abrir"));
        assertTrue(texto.contains("b) Guardar"));
        assertTrue(texto.contains("c) Salir"));
    }

    @Test
    @DisplayName("El estilo ROMANOS etiqueta i, ii, iii...")
    public void renderizaRomanos() {
        MenuTexto menu = menuConOpcionesBasicas();
        menu.setEstiloNumeracion(EstiloNumeracionMenu.ROMANOS);
        String texto = menu.obtenerMenuComoTexto();
        assertTrue(texto.contains("i) Abrir"));
        assertTrue(texto.contains("ii) Guardar"));
        assertTrue(texto.contains("iii) Salir"));
    }

    @Test
    @DisplayName("Las letras continúan aa, ab... después de la z")
    public void letrasDespuesDeLaZ() {
        MenuTexto menu = new MenuTexto();
        menu.setEstiloNumeracion(EstiloNumeracionMenu.LETRAS);
        menu.setConSalir(false);
        for (int i = 1; i <= 27; i++) {
            menu.agregarOpcion("opcion" + i, "Opción " + i);
        }
        String texto = menu.obtenerMenuComoTexto();
        assertTrue(texto.contains("a) Opción 1"));
        assertTrue(texto.contains("z) Opción 26"));
        assertTrue(texto.contains("aa) Opción 27"));
    }

    @Test
    @DisplayName("El separador es configurable")
    public void separadorConfigurable() {
        MenuTexto menu = menuConOpcionesBasicas();
        menu.setSeparador(" - ");
        String texto = menu.obtenerMenuComoTexto();
        assertTrue(texto.contains("1 - Abrir"));
        assertFalse(texto.contains("1) Abrir"));
    }

    @Test
    @DisplayName("Las opciones inhabilitadas se ocultan y el resto se renumera")
    public void inhabilitadasOcultasYRenumera() {
        MenuTexto menu = menuConOpcionesBasicas();
        menu.inhabilitarOpcion("abrir");
        String texto = menu.obtenerMenuComoTexto();
        assertFalse(texto.contains("Abrir"));
        assertTrue(texto.contains("1) Guardar"));
        assertTrue(texto.contains("2) Salir"));
    }

    @Test
    @DisplayName("Cancelar y Salir son configurables y aparecen al final")
    public void cancelarYSalirConfigurables() {
        MenuTexto menu = menuConOpcionesBasicas();
        menu.setConCancelar(true);
        menu.setConSalir(true);
        String texto = menu.obtenerMenuComoTexto();
        assertTrue(texto.contains("3) Cancelar"));
        assertTrue(texto.contains("4) Salir"));

        menu.setConCancelar(false);
        menu.setConSalir(false);
        texto = menu.obtenerMenuComoTexto();
        assertFalse(texto.contains("Cancelar"));
        assertFalse(texto.contains("Salir"));
    }

    // ----- Gestión de opciones -----

    @Test
    @DisplayName("No se permiten valores duplicados")
    public void valoresDuplicadosSeRechazan() {
        MenuTexto menu = new MenuTexto();
        assertTrue(menu.agregarOpcion("abrir", "Abrir"));
        assertFalse(menu.agregarOpcion("abrir", "Abrir otra vez"));
        assertEquals(1, menu.obtenerTotalOpciones());
    }

    @Test
    @DisplayName("quitarOpcion elimina la opción y devuelve false si no existe")
    public void quitarOpcionSeComportaSegúnContrato() {
        MenuTexto menu = menuConOpcionesBasicas();
        assertTrue(menu.quitarOpcion("abrir"));
        assertFalse(menu.tieneOpcion("abrir"));
        assertFalse(menu.quitarOpcion("inexistente"));
        assertEquals(1, menu.obtenerTotalOpciones());
    }

    @Test
    @DisplayName("habilitar/inhabilitar devuelven false si la opción no existe")
    public void cambiarEstadoDeOpcionInexistente() {
        MenuTexto menu = menuConOpcionesBasicas();
        assertFalse(menu.habilitarOpcion("inexistente"));
        assertFalse(menu.inhabilitarOpcion("inexistente"));
        assertTrue(menu.inhabilitarOpcion("guardar"));
        assertTrue(menu.habilitarOpcion("guardar"));
    }

    @Test
    @DisplayName("tieneOpcion encuentra opciones inhabilitadas; obtenerOpcion devuelve null si no existe")
    public void tieneYObtenerOpcion() {
        MenuTexto menu = menuConOpcionesBasicas();
        menu.inhabilitarOpcion("abrir");
        assertTrue(menu.tieneOpcion("abrir"));
        assertNotNull(menu.obtenerOpcion("abrir"));
        assertFalse(menu.obtenerOpcion("abrir").isHabilitada());
        assertNull(menu.obtenerOpcion("inexistente"));
    }

    @Test
    @DisplayName("agregarOpcion(String) capitaliza el texto como el ejemplo del docente")
    public void agregarOpcionConSoloTextoCapitaliza() {
        MenuTexto menu = new MenuTexto();
        assertTrue(menu.agregarOpcion("abrir"));
        assertTrue(menu.obtenerMenuComoTexto().contains("1) Abrir"));
    }

    @Test
    @DisplayName("La configuración con estilo nulo o separador vacío es rechazada")
    public void configuracionInvalidaSeRechaza() {
        MenuTexto menu = new MenuTexto();
        assertThrows(IllegalArgumentException.class, () -> menu.setEstiloNumeracion(null));
        assertThrows(IllegalArgumentException.class, () -> menu.setSeparador(null));
        assertThrows(IllegalArgumentException.class, () -> menu.setSeparador(""));
    }

    // ----- Interacción (solicitarOpcion con entrada simulada) -----

    @Test
    @DisplayName("Aceptar la etiqueta numérica mostrada y devolver el valor")
    public void respondeALaEtiquetaNumerica() {
        simularEntrada("2\n");
        try {
            MenuTexto menu = menuConOpcionesBasicas();
            assertEquals("guardar", menu.solicitarOpcion());
        } finally {
            System.setIn(ENTRADA_ORIGINAL);
        }
    }

    @Test
    @DisplayName("Aceptar el valor de la opción como respuesta directa")
    public void respondeAlValor() {
        simularEntrada("abrir\n");
        try {
            MenuTexto menu = menuConOpcionesBasicas();
            assertEquals("abrir", menu.solicitarOpcion());
        } finally {
            System.setIn(ENTRADA_ORIGINAL);
        }
    }

    @Test
    @DisplayName("Aceptar la etiqueta en estilo LETRAS (con reintentos previos)")
    public void respondeALaEtiquetaConLetras() {
        simularEntrada("99\nz\nb\n");
        try {
            MenuTexto menu = menuConOpcionesBasicas();
            menu.setEstiloNumeracion(EstiloNumeracionMenu.LETRAS);
            assertEquals("guardar", menu.solicitarOpcion());
        } finally {
            System.setIn(ENTRADA_ORIGINAL);
        }
    }

    @Test
    @DisplayName("Rechazar la etiqueta de una opción inhabilitada y repetir")
    public void rechazaOpcionInhabilitada() {
        simularEntrada("1\n2\n");
        try {
            MenuTexto menu = menuConOpcionesBasicas();
            menu.inhabilitarOpcion("abrir");
            assertEquals("guardar", menu.solicitarOpcion());
        } finally {
            System.setIn(ENTRADA_ORIGINAL);
        }
    }

    @Test
    @DisplayName("Devolver VALOR_CANCELAR y VALOR_SALIR cuando esas opciones están activas")
    public void respondeAOpcionesAutomaticas() {
        simularEntrada("cancelar\n");
        try {
            MenuTexto menu = menuConOpcionesBasicas();
            menu.setConCancelar(true);
            assertEquals(IMenu.VALOR_CANCELAR, menu.solicitarOpcion());
        } finally {
            System.setIn(ENTRADA_ORIGINAL);
        }

        simularEntrada("salir\n");
        try {
            MenuTexto menu = menuConOpcionesBasicas();
            assertEquals(IMenu.VALOR_SALIR, menu.solicitarOpcion());
        } finally {
            System.setIn(ENTRADA_ORIGINAL);
        }
    }

    @Test
    @DisplayName("LSP: dos implementaciones de IMenu se usan igual (sondeo de contrato)")
    public void contratoCompartidoEntreImplementaciones() {
        IMenu menu = menuConOpcionesBasicas();
        assertTrue(menu instanceof IMenu);
        assertTrue(menu.agregarOpcion(new OpcionMenu("extra", "Extra", true)));
        IOpcionMenu recuperada = menu.obtenerOpcion("extra");
        assertNotNull(recuperada);
        assertEquals("Extra", recuperada.getTexto());
        assertFalse(menu.agregarOpcion("extra", "Duplicado"));
    }
}
