import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BorrarDatos_v4 {
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_NAME = "trabajadores.db";
    public static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

    public static final String NOMBRE_TABLA = "Trabajador";
    public static final String CAMPO_NOMBRE = "nombre_completo";

    public static final String MSG_PEDIR_NOMBRE = "Nombre del registro a borrar: ";
    public static final String MSG_FILAS = "Filas borradas: ";

    public static final String ERR_DRIVER_NOT_FOUND = "Driver SQLite no encontrado: ";
    public static final String ERR_DB = "Error de base de datos: ";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Class.forName(DRIVER);

            System.out.print(MSG_PEDIR_NOMBRE);
            String nombre = sc.nextLine();

            String sqlDelete = crearConsultaDelete(NOMBRE_TABLA, CAMPO_NOMBRE, nombre);

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {
                int filas = stmt.executeUpdate(sqlDelete);
                System.out.println(MSG_FILAS + filas);
            }
        } catch (ClassNotFoundException e) {
            System.err.println(ERR_DRIVER_NOT_FOUND + e.getMessage());
        } catch (SQLException e) {
            System.err.println(ERR_DB + e.getMessage());
        } finally {
            sc.close();
        }
    }

    public static String crearConsultaDelete(String nombreTabla, String campoFiltro, String valorFiltro) {
        return "DELETE FROM " + nombreTabla + " WHERE " + campoFiltro + "='" + valorFiltro + "'";
    }
}
