import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertarDatos_v3 {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:trabajadores.db";
            int filasInsertadas = 0;

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {

                String sql1 = crearConsultaInsert(
                        "Trabajador",
                        "nombre_completo", "puesto", "edad", "salario",
                        "Ana Perez", "Contadora", "30", "12500.50"
                );
                String sql2 = crearConsultaInsert(
                        "Trabajador",
                        "nombre_completo", "puesto", "edad", "salario",
                        "Luis Garcia", "Supervisor", "42", "18400.00"
                );
                String sql3 = crearConsultaInsert(
                        "Trabajador",
                        "nombre_completo", "puesto", "edad", "salario",
                        "Marta Lopez", "Asistente", "25", "9800.75"
                );

                filasInsertadas += stmt.executeUpdate(sql1);
                filasInsertadas += stmt.executeUpdate(sql2);
                filasInsertadas += stmt.executeUpdate(sql3);
            }

            System.out.println("Filas insertadas: " + filasInsertadas);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
        }
    }

    public static String crearConsultaInsert(
            String nombreTabla,
            String campo1, String campo2, String campo3, String campo4,
            String valor1, String valor2, String valor3, String valor4) {
        return "INSERT INTO " + nombreTabla + "(" + campo1 + ", " + campo2 + ", " + campo3 + ", " + campo4 + ") VALUES('"
                + valor1 + "', '" + valor2 + "', " + valor3 + ", " + valor4 + ")";
    }
}
