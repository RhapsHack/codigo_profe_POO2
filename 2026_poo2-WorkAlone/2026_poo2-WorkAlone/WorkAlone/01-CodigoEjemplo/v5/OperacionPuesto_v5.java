public class OperacionPuesto_v5 extends BaseOperacionBD_v5 {
    public static final String TABLA = "Puesto";

    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_DESCRIPCION = "descripcion";
    public static final String CAMPO_DIRECCION = "direccion";
    public static final String CAMPO_TELEFONO = "telefono";

    public static final String[] CAMPOS = {
            CAMPO_NOMBRE, CAMPO_DESCRIPCION, CAMPO_DIRECCION, CAMPO_TELEFONO
    };

    public static final String[] TIPOS = {
            TIPO_TEXT, TIPO_TEXT, TIPO_TEXT, TIPO_TEXT
    };

    public static final String MSG_TABLA_OK = "Tabla Puesto creada o ya existente.";
    public static final String MSG_FILAS_INSERTADAS = "Filas insertadas en Puesto: ";
    public static final String MSG_CONSULTA = "Registros de la tabla Puesto:";

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

        String[] fila1 = {"Gerencia", "Direccion general", "Edificio A", "4921111000"};
        String[] fila2 = {"Recursos Humanos", "Atencion al personal", "Edificio B", "4921112000"};
        String[] fila3 = {"Finanzas", "Control financiero", "Edificio C", "4921113000"};

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
