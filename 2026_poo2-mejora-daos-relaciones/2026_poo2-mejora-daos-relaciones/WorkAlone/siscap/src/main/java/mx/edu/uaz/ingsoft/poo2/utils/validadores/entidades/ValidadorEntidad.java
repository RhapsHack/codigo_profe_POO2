package mx.edu.uaz.ingsoft.poo2.utils.validadores.entidades;




import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.modelo.utils.MensajesResourceBundle;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces.IValidador;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces.IValidadorEntidad;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;


public abstract class ValidadorEntidad<T extends IEntidad> implements IValidadorEntidad<T> {

    private static final String FORMATO_ERROR_CON_CAMPO = "%s: %s";

    /**
     * Un campo registrado: nombre, validador y forma de leer el valor.
     * Sustituye a las dos listas paralelas y al {@code Map} de llaves
     * string de la versión anterior; el tipo {@code V} amarra el getter al
     * tipo que el validador espera, sin posibilidad de desincronizarse.
     */
    private record ValidadorCampo<T, V>(String nombreCampo, IValidador<V, ?> validador, Function<T, V> getter) {
        void tomarValorDe(T entidad) {
            validador.setValor(getter.apply(entidad));
        }
    }

    private final List<ValidadorCampo<T, ?>> campos = new ArrayList<>();
    private final ResourceBundle etiquetas = MensajesResourceBundle.MENSAJES.getResourceBundle();

    @Override
    public <V> void agregarValidador(String nombreCampo, IValidador<V, ?> validador, Function<T, V> getter) {
        campos.add(new ValidadorCampo<>(nombreCampo, validador, getter));
    }

    @Override
    public boolean validar(T entidad) {
        boolean resultadoValidacion = true;
        for (var campo : campos) {
            campo.tomarValorDe(entidad);
            if (!campo.validador().validar()) {
                resultadoValidacion = false;
            }
        }
        return resultadoValidacion;
    }

    @Override
    public List<String> getErrores() {
        var listaErrores = new ArrayList<String>();
        campos.forEach(campo -> campo.validador().getErrores()
                .forEach(mensaje -> listaErrores.add(
                        String.format(FORMATO_ERROR_CON_CAMPO, etiquetaDe(campo.nombreCampo()), mensaje))));
        return listaErrores;
    }

    private String etiquetaDe(String llaveCampo) {
        return etiquetas.containsKey(llaveCampo) ? etiquetas.getString(llaveCampo) : llaveCampo;
    }

}
