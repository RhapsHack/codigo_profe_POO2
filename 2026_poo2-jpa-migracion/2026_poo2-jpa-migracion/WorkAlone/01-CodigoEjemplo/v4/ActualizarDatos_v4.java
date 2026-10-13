import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ActualizarDatos_v4 {
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_NAME = "trabajadores.db";
    public static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

    public static final String NOMBRE_TABLA = "Trabajador";
    public static final String CAMPO_NOMBRE = "nombre_completo";
    public static final String CAMPO_PUESTO = "puesto";
    public static final String CAMPO_EDAD = "edad";
    public static final String CAMPO_SALARIO = "salario";

    public static final String MSG_PEDIR_NOMBRE = "Nombre del registro a actualizar: ";
    public static final String MSG_REGISTRO = "Registro encontrado:";
    public static final String MSG_NO_EXISTE = "No existe ese registro.";
    public static final String MSG_PEDIR_CAMPO = "Campo a actualizar (puesto/edad/salario): ";
    public static final String MSG_PEDIR_NUEVO_VALOR = "Nuevo valor: ";
    public static final String MSG_FILAS = "Filas actualizadas: ";

    public static final String ERR_DRIVER_NOT_FOUND = "Driver SQLite no encontrado: ";
    public static final String ERR_DB = "Error de base de datos: ";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Class.forName(DRIVER);

            System.out.print(MSG_PEDIR_NOMBRE);
            String nombre = sc.nextLine();

            String sqlMostrar = crearConsultaSelectPorNombre(
                    NOMBRE_TABLA,
                    CAMPO_NOMBRE, CAMPO_PUESTO, CAMPO_EDAD, CAMPO_SALARIO,
                    nombre
            );

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlMostrar)) {
                if (rs.next()) {
                    System.out.println(MSG_REGISTRO);
                    System.out.println(rs.getString(CAMPO_NOMBRE) + " | " + rs.getString(CAMPO_PUESTO)
                            + " | " + rs.getInt(CAMPO_EDAD) + " | " + rs.getDouble(CAMPO_SALARIO));
                } else {
                    System.out.println(MSG_NO_EXISTE);
                    return;
                }
            }

            System.out.print(MSG_PEDIR_CAMPO);
            String campo = sc.nextLine();
            System.out.print(MSG_PEDIR_NUEVO_VALOR);
            String nuevoValor = sc.nextLine();

            String sqlUpdate = crearConsultaUpdate(NOMBRE_TABLA, campo, nuevoValor, CAMPO_NOMBRE, nombre);

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {
                int filas = stmt.executeUpdate(sqlUpdate);
                System.out.println(MSG_FILAS + filas);
            }
        } catch (ClassNotFoundException e) {
            System.err.println(ERR_DRIVER_NOT_FOUND + e.getMessage());
        } catch (SQLException e) {
            System.err.println(ERR_DB + e.getMessage());
        } finally {
            sc.close();
        }
    }

    public static String crearConsultaSelectPorNombre(
            String nombreTabla,
            String campo1, String campo2, String campo3, String campo4,
            String nombre) {
        return "SELECT " + campo1 + ", " + campo2 + ", " + campo3 + ", " + campo4
                + " FROM " + nombreTabla + " WHERE " + CAMPO_NOMBRE + "='" + nombre + "'";
    }

    public static String crearConsultaUpdate(
            String nombreTabla,
            String campoActualizar,
            String nuevoValor,
            String campoFiltro,
            String valorFiltro) {
        String valorSql = nuevoValor;
        if (CAMPO_PUESTO.equals(campoActualizar)) {
            valorSql = "'" + nuevoValor + "'";
        }

        return "UPDATE " + nombreTabla + " SET " + campoActualizar + " = " + valorSql
                + " WHERE " + campoFiltro + "='" + valorFiltro + "'";
    }
}
