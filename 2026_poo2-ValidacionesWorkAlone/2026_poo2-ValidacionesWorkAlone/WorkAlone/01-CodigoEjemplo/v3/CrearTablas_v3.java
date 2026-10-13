import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CrearTablas_v3 {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:trabajadores.db";
            String sql = crearConsultaCreateTable(
                    "Trabajador",
                    "nombre_completo", "TEXT",
                    "puesto", "TEXT",
                    "edad", "INTEGER",
                    "salario", "REAL"
            );

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

    public static String crearConsultaCreateTable(
            String nombreTabla,
            String campo1, String tipo1,
            String campo2, String tipo2,
            String campo3, String tipo3,
            String campo4, String tipo4) {
        return "CREATE TABLE IF NOT EXISTS " + nombreTabla + " ("
                + campo1 + " " + tipo1 + ", "
                + campo2 + " " + tipo2 + ", "
                + campo3 + " " + tipo3 + ", "
                + campo4 + " " + tipo4
                + ");";
    }
}
