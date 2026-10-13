package mx.edu.uaz.ingsoft.poo2.servicios;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Framework;
import mx.edu.uaz.ingsoft.poo2.utils.Constantes;

import java.util.Optional;

public class CargaFrameworkService extends AbstractServicioCargaCSV<Framework>{
    public CargaFrameworkService() {
        super(Constantes.CAT_FRAMEWORK);
    }

    @Override
    public Optional<Framework> crearEntidad(String linea) {
        String [] campos = linea.split(",");
        Framework salida = new Framework();
        // nombre, tipo, lenguajeBase, descripcion
        salida.setNombre(campos[1]);
        salida.setTipo(campos[2]);
        salida.setLenguajeBase(campos[3]);
        salida.setDescripcion(campos[4]);
        // Se regresa un opcional por si hay algún error al leer los datos
        return Optional.of(salida);
    }
}
