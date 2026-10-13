import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertarDatos_v2 {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:trabajadores.db";
            int filasInsertadas = 0;

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {

                String sql1 = "INSERT INTO Trabajador(nombre_completo, puesto, edad, salario) VALUES('Ana Perez', 'Contadora', 30, 12500.50)";
                String sql2 = "INSERT INTO Trabajador(nombre_completo, puesto, edad, salario) VALUES('Luis Garcia', 'Supervisor', 42, 18400.00)";
                String sql3 = "INSERT INTO Trabajador(nombre_completo, puesto, edad, salario) VALUES('Marta Lopez', 'Asistente', 25, 9800.75)";

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
}
