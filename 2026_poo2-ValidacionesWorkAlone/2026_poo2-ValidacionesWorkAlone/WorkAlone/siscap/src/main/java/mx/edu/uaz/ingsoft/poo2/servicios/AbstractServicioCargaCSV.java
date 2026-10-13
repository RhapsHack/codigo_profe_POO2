package mx.edu.uaz.ingsoft.poo2.servicios;



import mx.edu.uaz.ingsoft.poo2.modelo.daos.DaoServiceFactory;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IServicioBase;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IServicioCarga;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

import static java.nio.file.Files.*;

public abstract class AbstractServicioCargaCSV<T extends IEntidad> implements IServicioCarga {

    protected static Logger log = Logger.getLogger("AbstractServicioCargaCSV");
    private final String nombreCatalogo;

    public AbstractServicioCargaCSV(String nombreCatalogo) {
        this.nombreCatalogo = nombreCatalogo;
    }

    @Override
    public void cargarArchivo(Path ruta) {
        var servicioRecuperado = DaoServiceFactory.getServicio(nombreCatalogo);
        IServicioBase<T> servicio;
        if(servicioRecuperado.isPresent()){
            servicio = servicioRecuperado.get();
            try(var lineasStream = lines(ruta)){
                ArrayList<T> listaEntidades = new ArrayList<>();
                lineasStream.forEach(linea->{
                    var entidad = crearEntidad(linea);
                    if(entidad.isPresent()){
                        listaEntidades.add(entidad.get());
                    }else{
                        log.info("Error en linea: "+linea);
                    }
                });
                servicio.guardarTodos(listaEntidades);
            }catch (IOException e){
                log.severe(e.getMessage());
            }
        }

    }
    public abstract Optional<T> crearEntidad(String linea);

}
