import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class LimpiarBaseDatos {
    public static void main(String[] args) {
        limpiarBase("trabajadores.db");
        limpiarBase("trabajadores_v6.db");
    }

    public static void limpiarBase(String dbName) {
        String url = "jdbc:sqlite:" + dbName;

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {

                stmt.execute("DELETE FROM Trabajador");
                stmt.execute("DELETE FROM sqlite_sequence WHERE name='Trabajador'");
            } catch (SQLException e) {
                // La tabla puede no existir en algunas versiones.
            }

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {

                stmt.execute("DELETE FROM Puesto");
                stmt.execute("DELETE FROM sqlite_sequence WHERE name='Puesto'");
            } catch (SQLException e) {
                // La tabla puede no existir en algunas versiones.
            }

            System.out.println("Limpieza completada en " + dbName);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite no encontrado: " + e.getMessage());
        }
    }
}
