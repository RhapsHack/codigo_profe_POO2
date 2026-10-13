public class OperacionTrabajador_v5 extends BaseOperacionBD_v5 {
    public static final String TABLA = "Trabajador";

    public static final String CAMPO_NOMBRE = "nombre_completo";
    public static final String CAMPO_PUESTO = "puesto";
    public static final String CAMPO_EDAD = "edad";
    public static final String CAMPO_SALARIO = "salario";

    public static final String[] CAMPOS = {
            CAMPO_NOMBRE, CAMPO_PUESTO, CAMPO_EDAD, CAMPO_SALARIO
    };

    public static final String[] TIPOS = {
            TIPO_TEXT, TIPO_TEXT, TIPO_INTEGER, TIPO_REAL
    };

    public static final String MSG_TABLA_OK = "Tabla Trabajador creada o ya existente.";
    public static final String MSG_FILAS_INSERTADAS = "Filas insertadas en Trabajador: ";
    public static final String MSG_CONSULTA = "Registros de la tabla Trabajador:";

    @Override
    public void crearTabla() {
        String sql = super.crearConsultaCreateBase(TABLA, CAMPOS, TIPOS);
        if (super.ejecutarDDL(sql)) {
            System.out.println(MSG_TABLA_OK);
        }
    }

    @Override
    public void insertarDatosEjemplo() {
        int filas = 0;

        String[] fila1 = {"Ana Perez", "Contadora", "30", "12500.50"};
        String[] fila2 = {"Luis Garcia", "Supervisor", "42", "18400.00"};
        String[] fila3 = {"Marta Lopez", "Asistente", "25", "9800.75"};

        filas += super.ejecutarUpdate(super.crearConsultaInsertBase(TABLA, CAMPOS, fila1, TIPOS));
        filas += super.ejecutarUpdate(super.crearConsultaInsertBase(TABLA, CAMPOS, fila2, TIPOS));
        filas += super.ejecutarUpdate(super.crearConsultaInsertBase(TABLA, CAMPOS, fila3, TIPOS));

        System.out.println(MSG_FILAS_INSERTADAS + filas);
    }

    @Override
    public void consultarDatos() {
        String sql = super.crearConsultaSelectBase(TABLA, CAMPOS);
        super.ejecutarSelectYMostrar(MSG_CONSULTA, sql, CAMPOS);
    }
}
