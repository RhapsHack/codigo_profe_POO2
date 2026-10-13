public class Demo_v5 {
    public static void main(String[] args) {
        OperacionTrabajador_v5 trabajador = new OperacionTrabajador_v5();
        OperacionPuesto_v5 puesto = new OperacionPuesto_v5();

        System.out.println("=== VERSION 5: HERENCIA ===");

        trabajador.crearTabla();
        puesto.crearTabla();

        trabajador.insertarDatosEjemplo();
        puesto.insertarDatosEjemplo();

        trabajador.consultarDatos();
        puesto.consultarDatos();
    }
}
