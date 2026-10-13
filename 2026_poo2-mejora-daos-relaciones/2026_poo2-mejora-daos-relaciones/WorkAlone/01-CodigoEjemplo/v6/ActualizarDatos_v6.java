import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ActualizarDatos_v6 {
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:trabajadores_v6.db";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Class.forName(DRIVER);

            System.out.print("ID del registro a actualizar: ");
            int id = Integer.parseInt(sc.nextLine());

            String sqlMostrar = "SELECT id, nombre_completo, puesto, edad, salario FROM Trabajador WHERE id=" + id;

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlMostrar)) {
                if (rs.next()) {
                    System.out.println("Registro encontrado:");
                    System.out.println(rs.getInt("id") + " | " + rs.getString("nombre_completo") + " | "
                            + rs.getString("puesto") + " | " + rs.getInt("edad") + " | " + rs.getDouble("salario"));
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

            String sqlUpdate = "UPDATE Trabajador SET " + campo + " = " + valorSql + " WHERE id=" + id;

            try (Connection conn = DriverManager.getConnection(DB_URL);
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
