import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsultarDatos_v3 {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:trabajadores.db";
            String sql = crearConsultaSelect(
                    "Trabajador",
                    "nombre_completo", "puesto", "edad", "salario"
            );

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                System.out.println("Registros de la tabla Trabajador:");
                while (rs.next()) {
                    String nombre = rs.getString("nombre_completo");
                    String puesto = rs.getString("puesto");
                    int edad = rs.getInt("edad");
                    double salario = rs.getDouble("salario");

                    System.out.println(nombre + " | " + puesto + " | " + edad + " | " + salario);
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
        }
    }

    public static String crearConsultaSelect(
            String nombreTabla,
            String campo1, String campo2, String campo3, String campo4) {
        return "SELECT " + campo1 + ", " + campo2 + ", " + campo3 + ", " + campo4 + " FROM " + nombreTabla;
    }
}
