import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ProgramaCRUDMenu_v5 {
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:trabajadores.db";

    public static void main(String[] args) {
        OperacionTrabajador_v5 trabajador = new OperacionTrabajador_v5();
        OperacionPuesto_v5 puesto = new OperacionPuesto_v5();

        Scanner sc = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("\n=== MENU VERSION 5 ===");
            System.out.println("1) Crear tabla(s)");
            System.out.println("2) Mostrar registros de la(s) tabla(s)");
            System.out.println("3) Anadir registros a la(s) tabla(s)");
            System.out.println("4) Actualizar registro de Trabajador");
            System.out.println("5) Borrar registro de Trabajador");
            System.out.println("6) Salir");
            System.out.print("Elige opcion: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = 0;
            }

            if (opcion == 1) {
                trabajador.crearTabla();
                puesto.crearTabla();
            } else if (opcion == 2) {
                trabajador.consultarDatos();
                puesto.consultarDatos();
            } else if (opcion == 3) {
                trabajador.insertarDatosEjemplo();
                puesto.insertarDatosEjemplo();
            } else if (opcion == 4) {
                actualizarRegistroTrabajador(sc);
            } else if (opcion == 5) {
                borrarRegistroTrabajador(sc);
            }
        } while (opcion != 6);

        sc.close();
    }

    private static void actualizarRegistroTrabajador(Scanner sc) {
        try {
            Class.forName(DRIVER);

            System.out.print("Nombre del registro a actualizar: ");
            String nombre = sc.nextLine();

            String sqlMostrar = "SELECT nombre_completo, puesto, edad, salario FROM Trabajador WHERE nombre_completo='" + nombre + "'";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlMostrar)) {

                if (rs.next()) {
                    System.out.println("Registro encontrado:");
                    System.out.println(rs.getString("nombre_completo") + " | " + rs.getString("puesto")
                            + " | " + rs.getInt("edad") + " | " + rs.getDouble("salario"));
                } else {
                    System.out.println("No existe ese registro.");
                    return;
                }
            }

            System.out.print("Campo a actualizar (puesto/edad/salario): ");
            String campo = sc.nextLine();
            System.out.print("Nuevo valor: ");
            String nuevoValor = sc.nextLine();

            String valorSql = nuevoValor;
            if ("puesto".equals(campo)) {
                valorSql = "'" + nuevoValor + "'";
            }

            String sqlUpdate = "UPDATE Trabajador SET " + campo + " = " + valorSql
                    + " WHERE nombre_completo='" + nombre + "'";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {
                int filas = stmt.executeUpdate(sqlUpdate);
                System.out.println("Filas actualizadas: " + filas);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
        }
    }

    private static void borrarRegistroTrabajador(Scanner sc) {
        try {
            Class.forName(DRIVER);

            System.out.print("Nombre del registro a borrar: ");
            String nombre = sc.nextLine();

            String sqlDelete = "DELETE FROM Trabajador WHERE nombre_completo='" + nombre + "'";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {
                int filas = stmt.executeUpdate(sqlDelete);
                System.out.println("Filas borradas: " + filas);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
        }
    }
}
