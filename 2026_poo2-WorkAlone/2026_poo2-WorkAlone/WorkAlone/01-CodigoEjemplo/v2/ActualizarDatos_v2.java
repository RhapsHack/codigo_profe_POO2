import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ActualizarDatos_v2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:trabajadores.db";

            System.out.print("Nombre del registro a actualizar: ");
            String nombre = sc.nextLine();

            String sqlMostrar = "SELECT nombre_completo, puesto, edad, salario FROM Trabajador WHERE nombre_completo='" + nombre + "'";

            try (Connection conn = DriverManager.getConnection(url);
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

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {
                int filas = stmt.executeUpdate(sqlUpdate);
                System.out.println("Filas actualizadas: " + filas);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
