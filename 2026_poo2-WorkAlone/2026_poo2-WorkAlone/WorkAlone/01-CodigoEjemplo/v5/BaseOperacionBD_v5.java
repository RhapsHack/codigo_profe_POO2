import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseOperacionBD_v5 {
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_NAME = "trabajadores.db";
    public static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

    public static final String TIPO_TEXT = "TEXT";
    public static final String TIPO_INTEGER = "INTEGER";
    public static final String TIPO_REAL = "REAL";

    public static final String ERR_DRIVER_NOT_FOUND = "Driver SQLite no encontrado: ";
    public static final String ERR_DB = "Error de base de datos: ";

    protected boolean cargarDriver() {
        try {
            Class.forName(DRIVER);
            return true;
        } catch (ClassNotFoundException e) {
            imprimirError(e);
            return false;
        }
    }

    protected Connection abrirConexion() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            imprimirError(e);
            return null;
        }
    }

    protected String crearConsultaCreateBase(String nombreTabla, String[] campos, String[] tipos) {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sql.append(nombreTabla).append(" (");

        for (int i = 0; i < campos.length; i++) {
            sql.append(campos[i]).append(" ").append(tipos[i]);
            if (i < campos.length - 1) {
                sql.append(", ");
            }
        }

        sql.append(");");
        return sql.toString();
    }

    protected String crearConsultaInsertBase(String nombreTabla, String[] campos, String[] valores, String[] tipos) {
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(nombreTabla).append("(");

        for (int i = 0; i < campos.length; i++) {
            sql.append(campos[i]);
            if (i < campos.length - 1) {
                sql.append(", ");
            }
        }

        sql.append(") VALUES(");

        for (int i = 0; i < valores.length; i++) {
            sql.append(formatearValor(valores[i], tipos[i]));
            if (i < valores.length - 1) {
                sql.append(", ");
            }
        }

        sql.append(")");
        return sql.toString();
    }

    protected String crearConsultaSelectBase(String nombreTabla, String[] campos) {
        StringBuilder sql = new StringBuilder("SELECT ");

        for (int i = 0; i < campos.length; i++) {
            sql.append(campos[i]);
            if (i < campos.length - 1) {
                sql.append(", ");
            }
        }

        sql.append(" FROM ").append(nombreTabla);
        return sql.toString();
    }

    protected int ejecutarUpdate(String sql) {
        if (!cargarDriver()) {
            return 0;
        }

        Connection conexion = abrirConexion();
        if (conexion == null) {
            return 0;
        }

        try (Connection conn = conexion; Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            imprimirError(e);
            return 0;
        }
    }

    protected boolean ejecutarDDL(String sql) {
        if (!cargarDriver()) {
            return false;
        }

        Connection conexion = abrirConexion();
        if (conexion == null) {
            return false;
        }

        try (Connection conn = conexion; Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            return true;
        } catch (SQLException e) {
            imprimirError(e);
            return false;
        }
    }

    protected boolean ejecutarSelectYMostrar(String titulo, String sql, String[] campos) {
        if (!cargarDriver()) {
            return false;
        }

        Connection conexion = abrirConexion();
        if (conexion == null) {
            return false;
        }

        try (Connection conn = conexion;
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println(titulo);
            while (rs.next()) {
                StringBuilder fila = new StringBuilder();
                for (int i = 0; i < campos.length; i++) {
                    fila.append(rs.getString(campos[i]));
                    if (i < campos.length - 1) {
                        fila.append(" | ");
                    }
                }
                System.out.println(fila);
            }
            return true;
        } catch (SQLException e) {
            imprimirError(e);
            return false;
        }
    }

    protected void imprimirError(Exception e) {
        if (e instanceof ClassNotFoundException) {
            System.err.println(ERR_DRIVER_NOT_FOUND + e.getMessage());
        } else {
            System.err.println(ERR_DB + e.getMessage());
        }
    }

    private String formatearValor(String valor, String tipo) {
        if (TIPO_TEXT.equals(tipo)) {
            String valorEscapado = valor.replace("'", "''");
            return "'" + valorEscapado + "'";
        }
        return valor;
    }

    public abstract void crearTabla();

    public abstract void insertarDatosEjemplo();

    public abstract void consultarDatos();
}
