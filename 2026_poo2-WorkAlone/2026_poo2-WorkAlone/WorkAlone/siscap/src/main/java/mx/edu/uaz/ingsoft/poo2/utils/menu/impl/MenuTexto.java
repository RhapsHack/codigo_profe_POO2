package mx.edu.uaz.ingsoft.poo2.utils.menu.impl;

import java.util.ArrayList;
import java.util.List;

import mx.edu.uaz.ingsoft.poo2.modelo.enumeraciones.EstiloNumeracionMenu;
import mx.edu.uaz.ingsoft.poo2.utils.inout.impl.LectorDatosTexto;
import mx.edu.uaz.ingsoft.poo2.utils.inout.interfaces.ILectorDatos;
import mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces.IMenu;
import mx.edu.uaz.ingsoft.poo2.utils.menu.interfaces.IOpcionMenu;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.*;

/**
 * Implementación de {@link IMenu} que muestra el menú y captura la respuesta
 * del usuario en la CONSOLA: la presentación usa {@code System.out}, los
 * errores van a {@code System.err} y la lectura se delega en un
 * {@link ILectorDatos} (por defecto, un {@code LectorDatosTexto}).
 *
 * Igual que cualquier implementación de IMenu, este menú NO ejecuta acciones:
 * {@code solicitarOpcion()} se limita a mostrar, leer y validar; devuelve el
 * valor de la opción elegida y es el programa cliente quien decide qué hacer
 * con él.
 *
 * Nota didáctica (etiquetas posicionales): la etiqueta de cada opción (1, 2,
 * ... a, b, ... i, ii, ...) se calcula al momento de generar el texto del
 * menú, según la posición de cada opción HABILITADA; por eso las opciones
 * inhabilitadas se omiten y las restantes se reetiquetan solas.
 *
 * Nota didáctica (contrato booleano): tal como documenta la interfaz, las
 * operaciones de gestión NO lanzan excepciones cuando la opción no existe o
 * el valor es inválido: devuelven false (o null en obtenerOpcion). Así, el
 * programa cliente decide qué hacer con el resultado. Las excepciones quedan
 * reservadas para errores de programación detectados al construir objetos
 * (ver OpcionMenu) o al configurar el menú con valores nulos.
 *
 * Configuración predeterminada de esta implementación: estilo NUMEROS (la
 * numeración inicia en 1), separador ") ", opción Salir activada y opción
 * Cancelar desactivada.
 */
public class MenuTexto implements IMenu {
    private static final EstiloNumeracionMenu ESTILO_PREDETERMINADO = EstiloNumeracionMenu.NUMEROS;
    private static final String SEPARADOR_PREDETERMINADO = SEPARADOR_MENU_DEFAULT;
    private static final boolean CON_CANCELAR_PREDETERMINADO = false;
    private static final boolean CON_SALIR_PREDETERMINADO = true;
    private static final int NUMERACION_DESDE = 1;

    private static final String ETIQUETA_CANCELAR = TEXTO_OPCION_CANCELAR;
    private static final String ETIQUETA_SALIR = TEXTO_OPCION_SALIR;

    private final List<IOpcionMenu> opciones;
    private final ILectorDatos lector;
    private EstiloNumeracionMenu estilo;
    private String separador;
    private boolean conCancelar;
    private boolean conSalir;

    /** Construye el menú con la configuración predeterminada y su propio lector. */
    public MenuTexto() {
        this(new LectorDatosTexto());
    }

    /**
     * Construye el menú con la configuración predeterminada usando el lector
     * recibido. Permite que el menú y el programa cliente compartan UNA sola
     * lectura de la entrada estándar: dos lectores distintos sobre System.in se
     * "roban" la entrada entre sí (sobre todo con entrada redirigida).
     */
    public MenuTexto(ILectorDatos lector) {
        if (lector == null) {
            throw new IllegalArgumentException("El lector no puede ser nulo.");
        }
        this.opciones = new ArrayList<>();
        this.lector = lector;
        this.estilo = ESTILO_PREDETERMINADO;
        this.separador = SEPARADOR_PREDETERMINADO;
        this.conCancelar = CON_CANCELAR_PREDETERMINADO;
        this.conSalir = CON_SALIR_PREDETERMINADO;
    }

    // ----- Gestión de opciones -----

    @Override
    public boolean agregarOpcion(IOpcionMenu opcion) {
        if (opcion == null) {
            return false;
        }
        String valor = opcion.getValor();
        if (valor == null || valor.isBlank()) {
            return false;
        }
        if (buscarOpcion(valor) != null) {
            return false;
        }
        return opciones.add(opcion);
    }

    @Override
    public boolean agregarOpcion(String texto) {
        if (texto == null || texto.isBlank()) {
            return false;
        }
        // El valor es la cadena tal cual; el texto la muestra capitalizada
        // (lo hace el constructor mínimo de OpcionMenu).
        return agregarOpcion(new OpcionMenu(texto));
    }

    @Override
    public boolean agregarOpcion(String valor, String texto) {
        if (valor == null || valor.isBlank()) {
            return false;
        }
        if (texto == null || texto.isBlank()) {
            return false;
        }
        return agregarOpcion(new OpcionMenu(valor, texto, true));
    }

    @Override
    public boolean quitarOpcion(String valor) {
        IOpcionMenu objetivo = buscarOpcion(valor);
        if (objetivo == null) {
            return false;
        }
        return opciones.remove(objetivo);
    }

    @Override
    public boolean habilitarOpcion(String valor) {
        return cambiarEstadoOpcion(valor, true);
    }

    @Override
    public boolean inhabilitarOpcion(String valor) {
        return cambiarEstadoOpcion(valor, false);
    }

    @Override
    public boolean tieneOpcion(String valor) {
        return buscarOpcion(valor) != null;
    }

    @Override
    public IOpcionMenu obtenerOpcion(String valor) {
        return buscarOpcion(valor);
    }

    @Override
    public int obtenerTotalOpciones() {
        return opciones.size();
    }

    // ----- Configuración del menú -----

    @Override
    public void setEstiloNumeracion(EstiloNumeracionMenu estilo) {
        if (estilo == null) {
            throw new IllegalArgumentException(ERR_MENU_ESTILO_REQUERIDO);
        }
        this.estilo = estilo;
    }

    @Override
    public void setSeparador(String separador) {
        if (separador == null || separador.isEmpty()) {
            throw new IllegalArgumentException(ERR_MENU_SEPARADOR_REQUERIDO);
        }
        this.separador = separador;
    }

    @Override
    public void setConCancelar(boolean conCancelar) {
        this.conCancelar = conCancelar;
    }

    @Override
    public void setConSalir(boolean conSalir) {
        this.conSalir = conSalir;
    }

    @Override
    public boolean tieneCancelar() {
        return conCancelar;
    }

    @Override
    public boolean tieneSalir() {
        return conSalir;
    }

    // ----- Presentación e interacción -----

    @Override
    public String obtenerMenuComoTexto() {
        StringBuilder texto = new StringBuilder();
        int posicion = 0;
        for (IOpcionMenu opcion : opciones) {
            if (!opcion.isHabilitada()) {
                continue;
            }
            texto.append(construirEtiqueta(posicion));
            texto.append(separador);
            texto.append(opcion.getTexto());
            texto.append(System.lineSeparator());
            posicion++;
        }
        if (conCancelar) {
            texto.append(construirEtiqueta(posicion));
            texto.append(separador);
            texto.append(ETIQUETA_CANCELAR);
            texto.append(System.lineSeparator());
            posicion++;
        }
        if (conSalir) {
            texto.append(construirEtiqueta(posicion));
            texto.append(separador);
            texto.append(ETIQUETA_SALIR);
            texto.append(System.lineSeparator());
        }
        return texto.toString();
    }

    @Override
    public void mostrarMenu() {
        System.out.print(obtenerMenuComoTexto());
    }

    @Override
    public String solicitarOpcion() {
        mostrarMenu();
        String respuesta = lector.leerTexto(MSG_MENU_SOLICITAR_OPCION);
        String valorResuelto = resolverRespuesta(respuesta);
        while (valorResuelto == null) {
            System.err.println(MSG_MENU_OPCION_NO_VALIDA);
            mostrarMenu();
            respuesta = lector.leerTexto(MSG_MENU_SOLICITAR_OPCION);
            valorResuelto = resolverRespuesta(respuesta);
        }
        return valorResuelto;
    }

    // ----- Métodos privados de apoyo -----

    private boolean cambiarEstadoOpcion(String valor, boolean estado) {
        IOpcionMenu objetivo = buscarOpcion(valor);
        if (objetivo == null) {
            return false;
        }
        objetivo.setHabilitada(estado);
        return true;
    }

    /**
     * Busca la opción por su valor; devuelve null si el valor es nulo, vacío
     * o no corresponde a ninguna opción del menú.
     */
    private IOpcionMenu buscarOpcion(String valor) {
        if (valor == null) {
            return null;
        }
        for (IOpcionMenu opcion : opciones) {
            if (opcion.getValor().equals(valor)) {
                return opcion;
            }
        }
        return null;
    }

    /**
     * Devuelve la etiqueta de la posición indicada (0-based) según el estilo
     * de numeración configurado. La numeración inicia en {@code NUMERACION_DESDE}.
     */
    private String construirEtiqueta(int posicion) {
        int numero = posicion + NUMERACION_DESDE;
        switch (estilo) {
            case LETRAS:
                return construirEtiquetaConLetras(numero);
            case ROMANOS:
                return construirEtiquetaConRomanos(numero);
            case NUMEROS:
            default:
                return String.valueOf(numero);
        }
    }

    /** Conversión a letras: 1=a, 2=b, ... 26=z, 27=aa, 28=ab, ... */
    private String construirEtiquetaConLetras(int numero) {
        StringBuilder etiqueta = new StringBuilder();
        while (numero > 0) {
            numero--;
            char letra = (char) ('a' + (numero % 26));
            etiqueta.insert(0, letra);
            numero = numero / 26;
        }
        return etiqueta.toString();
    }

    /** Conversión a números romanos en minúscula: 1=i, 2=ii, 4=iv, 9=ix, ... */
    private String construirEtiquetaConRomanos(int numero) {
        final int[] valores = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        final String[] simbolos = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder etiqueta = new StringBuilder();
        for (int i = 0; i < valores.length; i++) {
            while (numero >= valores[i]) {
                etiqueta.append(simbolos[i]);
                numero = numero - valores[i];
            }
        }
        return etiqueta.toString().toLowerCase();
    }

    /**
     * Resuelve la respuesta capturada y devuelve el valor de la opción a la
     * que corresponde, o null si la respuesta no es válida.
     *
     * El usuario puede responder de dos formas, ambas válidas:
     * 1) Con la ETIQUETA mostrada ("1", "a", "iii", ...), que se calcula con
     *    el estilo y el separador vigentes sobre las opciones habilitadas.
     * 2) Con el VALOR de la opción (p. ej. "agregar"), o con los valores
     *    "cancelar"/"salir" si esas opciones automáticas están activadas.
     */
    private String resolverRespuesta(String respuesta) {
        if (respuesta == null) {
            return null;
        }
        String candidata = respuesta.trim();
        if (candidata.isEmpty()) {
            return null;
        }
        IOpcionMenu porValor = buscarOpcion(candidata);
        if (porValor != null && porValor.isHabilitada()) {
            return porValor.getValor();
        }
        if (conCancelar && VALOR_OPCION_CANCELAR.equalsIgnoreCase(candidata)) {
            return VALOR_OPCION_CANCELAR;
        }
        if (conSalir && VALOR_OPCION_SALIR.equalsIgnoreCase(candidata)) {
            return VALOR_OPCION_SALIR;
        }
        int posicion = 0;
        for (IOpcionMenu actual : opciones) {
            if (!actual.isHabilitada()) {
                continue;
            }
            if (etiquetaCoincide(construirEtiqueta(posicion), candidata)) {
                return actual.getValor();
            }
            posicion++;
        }
        if (conCancelar) {
            if (etiquetaCoincide(construirEtiqueta(posicion), candidata)) {
                return VALOR_OPCION_CANCELAR;
            }
            posicion++;
        }
        if (conSalir && etiquetaCoincide(construirEtiqueta(posicion), candidata)) {
            return VALOR_OPCION_SALIR;
        }
        return null;
    }

    /** Compara la etiqueta con la respuesta sin distinguir mayúsculas/minúsculas. */
    private boolean etiquetaCoincide(String etiqueta, String respuesta) {
        return etiqueta.equalsIgnoreCase(respuesta);
    }
}
