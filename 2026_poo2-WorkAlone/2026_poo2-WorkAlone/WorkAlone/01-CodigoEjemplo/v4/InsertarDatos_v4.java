import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import static Constantes.DB_URL;

public class InsertarDatos_v4 {


	public static final String MSG_FILAS_INSERTADAS = "Filas insertadas: ";
    public static final String ERR_DRIVER_NOT_FOUND = "Driver SQLite no encontrado: ";
    public static final String ERR_DB = "Error de base de datos: ";
    public static final String NOMBRE_TABLA = "Trabajador";
	
	
    public static final String CAMPO_1 = "nombre_completo";
    public static final String CAMPO_2 = "puesto";
    public static final String CAMPO_3 = "edad";
    public static final String CAMPO_4 = "salario";



    public static final String V1_NOMBRE = "Ana Perez";
    public static final String V1_PUESTO = "Contadora";
    public static final String V1_EDAD = "30";
    public static final String V1_SALARIO = "12500.50";

    public static final String V2_NOMBRE = "Luis Garcia";
    public static final String V2_PUESTO = "Supervisor";
    public static final String V2_EDAD = "42";
    public static final String V2_SALARIO = "18400.00";

    public static final String V3_NOMBRE = "Marta Lopez";
    public static final String V3_PUESTO = "Asistente";
    public static final String V3_EDAD = "25";
    public static final String V3_SALARIO = "9800.75";

    public static void main(String[] args) {
        try {
            Class.forName(DRIVER);

            int filasInsertadas = 0;

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {

                String sql1 = crearConsultaInsert(
                        NOMBRE_TABLA,
                        CAMPO_1, CAMPO_2, CAMPO_3, CAMPO_4,
                        V1_NOMBRE, V1_PUESTO, V1_EDAD, V1_SALARIO
                );
                String sql2 = crearConsultaInsert(
                        NOMBRE_TABLA,
                        CAMPO_1, CAMPO_2, CAMPO_3, CAMPO_4,
                        V2_NOMBRE, V2_PUESTO, V2_EDAD, V2_SALARIO
                );
                String sql3 = crearConsultaInsert(
                        NOMBRE_TABLA,
                        CAMPO_1, CAMPO_2, CAMPO_3, CAMPO_4,
                        V3_NOMBRE, V3_PUESTO, V3_EDAD, V3_SALARIO
                );

                filasInsertadas += stmt.executeUpdate(sql1);
                filasInsertadas += stmt.executeUpdate(sql2);
                filasInsertadas += stmt.executeUpdate(sql3);
            }

            System.out.println(MSG_FILAS_INSERTADAS + filasInsertadas);
        } catch (ClassNotFoundException e) {
            System.err.println(ERR_DRIVER_NOT_FOUND + e.getMessage());
        } catch (SQLException e) {
            System.err.println(ERR_DB + e.getMessage());
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
