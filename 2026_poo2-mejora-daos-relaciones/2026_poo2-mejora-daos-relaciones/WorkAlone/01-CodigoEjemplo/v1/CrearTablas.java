import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CrearTablas {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:trabajadores.db";
            String sql = "CREATE TABLE IF NOT EXISTS Trabajador ("
                    + "nombre_completo TEXT, "
                    + "puesto TEXT, "
                    + "edad INTEGER, "
                    + "salario REAL"
                    + ");";

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tabla Trabajador creada o ya existente.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
        }
    }
}