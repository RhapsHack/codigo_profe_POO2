package mx.edu.uaz.ingsoft.poo2.utils.validadores.interfaces;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;

import java.util.List;
import java.util.function.Function;


public interface IValidadorEntidad<T extends IEntidad> {
    boolean validar (T entidad);

    /**
     * Registra la validación de un campo en una sola operación: el nombre
     * del campo, su validador y cómo leer el valor desde la entidad.
     * El compilador verifica que el getter lea el tipo que el validador
     * espera, por lo que el registro no puede quedar incompleto.
     */
    <V> void agregarValidador(String nombreCampo, IValidador<V, ?> validador, Function<T, V> getter);

    List<String> getErrores();
}
