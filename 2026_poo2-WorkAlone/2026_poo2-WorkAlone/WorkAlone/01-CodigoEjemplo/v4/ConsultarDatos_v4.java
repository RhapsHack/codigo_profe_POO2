import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsultarDatos_v4 {
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_NAME = "trabajadores.db";
    public static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

    public static final String NOMBRE_TABLA = "Trabajador";
    public static final String CAMPO_1 = "nombre_completo";
    public static final String CAMPO_2 = "puesto";
    public static final String CAMPO_3 = "edad";
    public static final String CAMPO_4 = "salario";

    public static final String MSG_REGISTROS = "Registros de la tabla Trabajador:";
    public static final String SEPARADOR = " | ";
    public static final String ERR_DRIVER_NOT_FOUND = "Driver SQLite no encontrado: ";
    public static final String ERR_DB = "Error de base de datos: ";

    public static void main(String[] args) {
        try {
            Class.forName(DRIVER);

            String sql = crearConsultaSelect(
                    NOMBRE_TABLA,
                    CAMPO_1, CAMPO_2, CAMPO_3, CAMPO_4
            );

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                System.out.println(MSG_REGISTROS);
                while (rs.next()) {
                    String nombre = rs.getString(CAMPO_1);
                    String puesto = rs.getString(CAMPO_2);
                    int edad = rs.getInt(CAMPO_3);
                    double salario = rs.getDouble(CAMPO_4);

                    System.out.println(nombre + SEPARADOR + puesto + SEPARADOR + edad + SEPARADOR + salario);
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println(ERR_DRIVER_NOT_FOUND + e.getMessage());
        } catch (SQLException e) {
            System.err.println(ERR_DB + e.getMessage());
        }
    }

    public static String crearConsultaSelect(
            String nombreTabla,
            String campo1, String campo2, String campo3, String campo4) {
        return "SELECT " + campo1 + ", " + campo2 + ", " + campo3 + ", " + campo4 + " FROM " + nombreTabla;
    }
}
