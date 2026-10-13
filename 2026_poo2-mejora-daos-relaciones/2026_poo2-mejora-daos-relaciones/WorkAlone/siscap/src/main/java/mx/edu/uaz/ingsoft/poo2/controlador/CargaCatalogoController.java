package mx.edu.uaz.ingsoft.poo2.controlador;



import mx.edu.uaz.ingsoft.poo2.servicios.CargaFrameworkService;
import mx.edu.uaz.ingsoft.poo2.vista.VistaCargaGrafico;

public class CargaCatalogoController {

    public static void iniciarCarga(String nombreCatalogo) {
        var servicioCarga = new CargaFrameworkService();
        var rutaSeleccionada = (new VistaCargaGrafico()).pedirRutaArchivo();
        rutaSeleccionada.ifPresent(servicioCarga::cargarArchivo);
    }
}
