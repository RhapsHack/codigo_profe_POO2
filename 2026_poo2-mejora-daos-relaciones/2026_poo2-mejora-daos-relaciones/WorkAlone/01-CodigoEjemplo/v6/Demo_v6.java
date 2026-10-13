public class Demo_v6 {
    public static void main(String[] args) {
        OperacionTrabajador_v6 trabajador = new OperacionTrabajador_v6();
        OperacionPuesto_v6 puesto = new OperacionPuesto_v6();

        System.out.println("=== VERSION 6: IDs AUTOINCREMENT Y CONSULTAS AVANZADAS ===");

        trabajador.crearTabla();
        puesto.crearTabla();

        trabajador.insertarDatosEjemplo();
        puesto.insertarDatosEjemplo();

        trabajador.consultarDatos();
        puesto.consultarDatos();
    }
}
