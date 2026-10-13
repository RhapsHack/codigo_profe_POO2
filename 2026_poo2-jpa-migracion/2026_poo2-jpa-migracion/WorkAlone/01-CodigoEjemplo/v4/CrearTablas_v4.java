import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import static Constantes.DB_URL;

public class CrearTablas_v4 {


    public static final String NOMBRE_TABLA = "Trabajador";
    public static final String CAMPO_1 = "nombre_completo";
    public static final String CAMPO_2 = "puesto";
    public static final String CAMPO_3 = "edad";
    public static final String CAMPO_4 = "salario";

    public static final String TIPO_1 = "TEXT";
    public static final String TIPO_2 = "TEXT";
    public static final String TIPO_3 = "INTEGER";
    public static final String TIPO_4 = "REAL";

    public static final String MSG_TABLA_OK = "Tabla Trabajador creada o ya existente.";
    public static final String ERR_DRIVER_NOT_FOUND = "Driver SQLite no encontrado: ";
    public static final String ERR_DB = "Error de base de datos: ";

    public static void main(String[] args) {
        try {
            Class.forName(DB_URL);

            String sql = crearConsultaCreateTable(
                    NOMBRE_TABLA,
                    CAMPO_1, TIPO_1,
                    CAMPO_2, TIPO_2,
                    CAMPO_3, TIPO_3,
                    CAMPO_4, TIPO_4
            );

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println(MSG_TABLA_OK);
            }
        } catch (ClassNotFoundException e) {
            System.err.println(Constantes.ERR_DRIVER_NOT_FOUND);
        } catch (SQLException e) {
            System.err.println(ERR_DB + e.getMessage());
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
