import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BorrarDatos_v3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:trabajadores.db";

            System.out.print("Nombre del registro a borrar: ");
            String nombre = sc.nextLine();

            String sqlDelete = crearConsultaDelete("Trabajador", "nombre_completo", nombre);

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {
                int filas = stmt.executeUpdate(sqlDelete);
                System.out.println("Filas borradas: " + filas);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    public static String crearConsultaDelete(String nombreTabla, String campoFiltro, String valorFiltro) {
        return "DELETE FROM " + nombreTabla + " WHERE " + campoFiltro + "='" + valorFiltro + "'";
    }
}
