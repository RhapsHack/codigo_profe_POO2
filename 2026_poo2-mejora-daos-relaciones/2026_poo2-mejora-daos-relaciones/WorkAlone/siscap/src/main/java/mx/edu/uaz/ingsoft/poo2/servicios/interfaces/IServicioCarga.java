package mx.edu.uaz.ingsoft.poo2.servicios.interfaces;



import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;

import java.nio.file.Path;
import java.util.Optional;

public interface IServicioCarga<T extends IEntidad> {

    void cargarArchivo(Path ruta);
    Optional<T> crearEntidad(String linea);
}
