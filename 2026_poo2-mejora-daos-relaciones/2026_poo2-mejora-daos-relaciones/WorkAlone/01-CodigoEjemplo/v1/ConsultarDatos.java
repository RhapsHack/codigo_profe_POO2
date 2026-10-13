import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsultarDatos {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:trabajadores.db";
            String sql = "SELECT nombre_completo, puesto, edad, salario FROM Trabajador";

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
}
