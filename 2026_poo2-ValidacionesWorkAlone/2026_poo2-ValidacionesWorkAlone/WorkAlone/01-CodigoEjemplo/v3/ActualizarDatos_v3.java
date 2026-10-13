import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ActualizarDatos_v3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:trabajadores.db";

            System.out.print("Nombre del registro a actualizar: ");
            String nombre = sc.nextLine();

            String sqlMostrar = crearConsultaSelectPorNombre("Trabajador", "nombre_completo", "puesto", "edad", "salario", nombre);

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

            String sqlUpdate = crearConsultaUpdate("Trabajador", campo, nuevoValor, "nombre_completo", nombre);

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

    public static String crearConsultaSelectPorNombre(
            String nombreTabla,
            String campo1, String campo2, String campo3, String campo4,
            String nombre) {
        return "SELECT " + campo1 + ", " + campo2 + ", " + campo3 + ", " + campo4
                + " FROM " + nombreTabla + " WHERE nombre_completo='" + nombre + "'";
    }

    public static String crearConsultaUpdate(
            String nombreTabla,
            String campoActualizar,
            String nuevoValor,
            String campoFiltro,
            String valorFiltro) {
        String valorSql = nuevoValor;
        if ("puesto".equals(campoActualizar)) {
            valorSql = "'" + nuevoValor + "'";
        }

        return "UPDATE " + nombreTabla + " SET " + campoActualizar + " = " + valorSql
                + " WHERE " + campoFiltro + "='" + valorFiltro + "'";
    }
}
