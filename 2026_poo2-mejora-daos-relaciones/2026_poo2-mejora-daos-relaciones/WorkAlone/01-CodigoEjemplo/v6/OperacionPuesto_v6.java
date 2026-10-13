public class OperacionPuesto_v6 extends BaseOperacionBD_v6 {
    public static final String TABLA = "Puesto";

    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_DESCRIPCION = "descripcion";
    public static final String CAMPO_DIRECCION = "direccion";
    public static final String CAMPO_TELEFONO = "telefono";

    public static final String[] CAMPOS_INSERCION = {
            CAMPO_NOMBRE, CAMPO_DESCRIPCION, CAMPO_DIRECCION, CAMPO_TELEFONO
    };

    public static final String[] TIPOS_INSERCION = {
            TIPO_TEXT, TIPO_TEXT, TIPO_TEXT, TIPO_TEXT
    };

    public static final String[] CAMPOS_CONSULTA = {
            CAMPO_ID, CAMPO_NOMBRE, CAMPO_DESCRIPCION, CAMPO_DIRECCION, CAMPO_TELEFONO
    };

    public static final String MSG_TABLA_OK = "Tabla Puesto (con ID autoincremental) creada o ya existente.";
    public static final String MSG_FILAS_INSERTADAS = "Filas insertadas en Puesto: ";
    public static final String MSG_TODOS = "\nRegistros de la tabla Puesto:";

    @Override
    public void crearTabla() {
        String sql = super.crearConsultaCreateBaseConId(TABLA, CAMPOS_INSERCION, TIPOS_INSERCION);
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
        String[] fila4 = {"Sistemas", "Soporte tecnico", "Edificio D", "4921114000"};
        String[] fila5 = {"Compras", "Adquisiciones", "Edificio E", "4921115000"};
        String[] fila6 = {"Ventas", "Relacion con clientes", "Edificio F", "4921116000"};

        filas += super.ejecutarUpdate(super.crearConsultaInsertBase(TABLA, CAMPOS_INSERCION, fila1, TIPOS_INSERCION));
        filas += super.ejecutarUpdate(super.crearConsultaInsertBase(TABLA, CAMPOS_INSERCION, fila2, TIPOS_INSERCION));
        filas += super.ejecutarUpdate(super.crearConsultaInsertBase(TABLA, CAMPOS_INSERCION, fila3, TIPOS_INSERCION));
        filas += super.ejecutarUpdate(super.crearConsultaInsertBase(TABLA, CAMPOS_INSERCION, fila4, TIPOS_INSERCION));
        filas += super.ejecutarUpdate(super.crearConsultaInsertBase(TABLA, CAMPOS_INSERCION, fila5, TIPOS_INSERCION));
        filas += super.ejecutarUpdate(super.crearConsultaInsertBase(TABLA, CAMPOS_INSERCION, fila6, TIPOS_INSERCION));

        System.out.println(MSG_FILAS_INSERTADAS + filas);
    }

    @Override
    public void consultarDatos() {
        String sqlTodos = super.crearConsultaSelectBase(TABLA, CAMPOS_CONSULTA);
        super.ejecutarSelectYMostrar(MSG_TODOS, sqlTodos, CAMPOS_CONSULTA);
    }
}
