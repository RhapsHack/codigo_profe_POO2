package mx.edu.uaz.ingsoft.poo2.utils.config;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_FRAMEWORK;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_LANG_PROG;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_PEOPLE;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_TEMA;

/**
 * Configuración de los campos buscables de cada catálogo para la ventana
 * única de CRUD.
 *
 * <p>El mapa de cada catálogo asocia la etiqueta de {@code mensajes.properties}
 * (la llave del bundle, que se muestra en el combo de campos) con el atributo
 * JPA real y su tipo. Así la ventana solo presenta etiquetas amigables y el
 * texto escrito se convierte al tipo del campo antes de llamar a
 * {@code findByCampo} (igualdad exacta).
 */
public abstract class CamposBusquedaConfig {

    private static final Map<String, CampoBusqueda> CAMPOS_PEOPLE = campos(
            entry("id", "id", TipoCampoBusqueda.INTEGER),
            entry("nombre", "nombre", TipoCampoBusqueda.STRING),
            entry("primer_apellido", "primerApellido", TipoCampoBusqueda.STRING),
            entry("segundo_apellido", "segundoApellido", TipoCampoBusqueda.STRING),
            entry("numero_empleado", "numeroEmpleado", TipoCampoBusqueda.STRING),
            entry("correo_electronico", "correoElectronico", TipoCampoBusqueda.STRING),
            entry("activo", "activo", TipoCampoBusqueda.BOOLEAN)
    );

    private static final Map<String, CampoBusqueda> CAMPOS_FRAMEWORK = campos(
            entry("id", "id", TipoCampoBusqueda.INTEGER),
            entry("nombre", "nombre", TipoCampoBusqueda.STRING),
            entry("tipo", "tipo", TipoCampoBusqueda.STRING),
            entry("lenguaje_base", "lenguajeBase", TipoCampoBusqueda.STRING),
            entry("descripcion", "descripcion", TipoCampoBusqueda.STRING),
            entry("activo", "activo", TipoCampoBusqueda.BOOLEAN)
    );

    private static final Map<String, CampoBusqueda> CAMPOS_LANG_PROG = campos(
            entry("id", "id", TipoCampoBusqueda.INTEGER),
            entry("nombre", "nombre", TipoCampoBusqueda.STRING),
            entry("paradigma", "paradigma", TipoCampoBusqueda.STRING),
            entry("version_referencia", "versionReferencia", TipoCampoBusqueda.STRING),
            entry("descripcion", "descripcion", TipoCampoBusqueda.STRING),
            entry("activo", "activo", TipoCampoBusqueda.BOOLEAN)
    );

    private static final Map<String, CampoBusqueda> CAMPOS_TEMA = campos(
            entry("id", "id", TipoCampoBusqueda.INTEGER),
            entry("nombre", "nombre", TipoCampoBusqueda.STRING),
            entry("tipo", "tipo", TipoCampoBusqueda.STRING),
            entry("activo", "activo", TipoCampoBusqueda.BOOLEAN)
    );

    private CamposBusquedaConfig() {
    }

    private static Map<String, CampoBusqueda> campos(CampoBusqueda... registros) {
        var salida = new LinkedHashMap<String, CampoBusqueda>();
        for (CampoBusqueda registro : registros) {
            salida.put(registro.key(), registro);
        }
        return salida;
    }

    private static CampoBusqueda entry(String key, String atributoJpa, TipoCampoBusqueda tipo) {
        return new CampoBusqueda(key, atributoJpa, tipo);
    }

    public static Map<String, CampoBusqueda> campos(String nombreCatalogo) {
        return switch (nombreCatalogo) {
            case CAT_PEOPLE -> CAMPOS_PEOPLE;
            case CAT_FRAMEWORK -> CAMPOS_FRAMEWORK;
            case CAT_LANG_PROG -> CAMPOS_LANG_PROG;
            case CAT_TEMA -> CAMPOS_TEMA;
            default -> Map.of();
        };
    }

    public static Optional<CampoBusqueda> campoPorEtiqueta(String nombreCatalogo, String etiquetaBundle) {
        return Optional.ofNullable(campos(nombreCatalogo).get(etiquetaBundle));
    }

    /**
     * Convierte el texto escrito a un valor del tipo del campo buscado.
     *
     * @param campo etiqueta del campo en el bundle, usada como llave del mapa
     * @param texto valor escrito por el usuario
     * @return el valor tipado listo para {@code findByCampo}, o vacío si el
     *         texto no puede convertirse al tipo del campo
     */
    public static Optional<Object> convertirValor(CampoBusqueda campo, String texto) {
        if (campo == null || texto == null) {
            return Optional.empty();
        }
        var valor = texto.trim();
        return switch (campo.tipo()) {
            case STRING -> Optional.of(valor);
            case INTEGER -> convertirEntero(valor);
            case BOOLEAN -> convertirBooleano(valor);
        };
    }

    public static Optional<Object> convertirValor(String nombreCatalogo, String etiquetaBundle, String texto) {
        return campoPorEtiqueta(nombreCatalogo, etiquetaBundle).flatMap(campo -> convertirValor(campo, texto));
    }

    private static Optional<Object> convertirEntero(String valor) {
        try {
            return Optional.of(Integer.parseInt(valor));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static Optional<Object> convertirBooleano(String valor) {
        var texto = valor.toLowerCase(Locale.ROOT);
        if (texto.equals("true") || texto.equals("verdadero")) {
            return Optional.of(Boolean.TRUE);
        }
        if (texto.equals("false") || texto.equals("falso")) {
            return Optional.of(Boolean.FALSE);
        }
        return Optional.empty();
    }
}