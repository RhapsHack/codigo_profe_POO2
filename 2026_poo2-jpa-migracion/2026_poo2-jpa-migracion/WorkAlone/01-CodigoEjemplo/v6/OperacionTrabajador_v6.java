public class OperacionTrabajador_v6 extends BaseOperacionBD_v6 {
    public static final String TABLA = "Trabajador";

    public static final String CAMPO_NOMBRE = "nombre_completo";
    public static final String CAMPO_PUESTO = "puesto";
    public static final String CAMPO_EDAD = "edad";
    public static final String CAMPO_SALARIO = "salario";

    public static final String[] CAMPOS_INSERCION = {
            CAMPO_NOMBRE, CAMPO_PUESTO, CAMPO_EDAD, CAMPO_SALARIO
    };

    public static final String[] TIPOS_INSERCION = {
            TIPO_TEXT, TIPO_TEXT, TIPO_INTEGER, TIPO_REAL
    };

    public static final String[] CAMPOS_CONSULTA = {
            CAMPO_ID, CAMPO_NOMBRE, CAMPO_PUESTO, CAMPO_EDAD, CAMPO_SALARIO
    };

    public static final String MSG_TABLA_OK = "Tabla Trabajador (con ID autoincremental) creada o ya existente.";
    public static final String MSG_FILAS_INSERTADAS = "Filas insertadas en Trabajador: ";

    public static final String MSG_TODOS = "\n[Consulta 1] Todos los registros de Trabajador:";
    public static final String MSG_BUSQUEDA_OK = "\n[Consulta 2] Busqueda por nombre (registro existente):";
    public static final String MSG_BUSQUEDA_FAIL = "\n[Consulta 3] Busqueda por nombre (registro inexistente):";
    public static final String MSG_WHERE_1 = "\n[Consulta 4] Subconjunto WHERE puesto = 'Supervisor':";
    public static final String MSG_WHERE_2 = "\n[Consulta 5] Subconjunto WHERE salario >= 15000:";
    public static final String MSG_NO_ENCONTRADO = "No se encontraron registros para la condicion indicada.";

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

        String[] fila1 = {"Ana Perez", "Contadora", "30", "12500.50"};
        String[] fila2 = {"Luis Garcia", "Supervisor", "42", "18400.00"};
        String[] fila3 = {"Marta Lopez", "Asistente", "25", "9800.75"};
        String[] fila4 = {"Carlos Ruiz", "Supervisor", "36", "16100.00"};
        String[] fila5 = {"Elena Torres", "Analista", "28", "14200.00"};
        String[] fila6 = {"Jose Martinez", "Analista", "33", "15500.00"};

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

        String sqlExiste = super.crearConsultaSelectWhereIgual(
                TABLA,
                CAMPOS_CONSULTA,
                CAMPO_NOMBRE,
                "Ana Perez",
                TIPO_TEXT
        );
        int encontrados = super.ejecutarSelectYMostrar(MSG_BUSQUEDA_OK, sqlExiste, CAMPOS_CONSULTA);
        if (encontrados == 0) {
            System.out.println(MSG_NO_ENCONTRADO);
        }

        String sqlNoExiste = super.crearConsultaSelectWhereIgual(
                TABLA,
                CAMPOS_CONSULTA,
                CAMPO_NOMBRE,
                "Persona Fantasma",
                TIPO_TEXT
        );
        int noEncontrados = super.ejecutarSelectYMostrar(MSG_BUSQUEDA_FAIL, sqlNoExiste, CAMPOS_CONSULTA);
        if (noEncontrados == 0) {
            System.out.println(MSG_NO_ENCONTRADO);
        }

        String sqlWhere1 = super.crearConsultaSelectWhereIgual(
                TABLA,
                CAMPOS_CONSULTA,
                CAMPO_PUESTO,
                "Supervisor",
                TIPO_TEXT
        );
        int subset1 = super.ejecutarSelectYMostrar(MSG_WHERE_1, sqlWhere1, CAMPOS_CONSULTA);
        if (subset1 == 0) {
            System.out.println(MSG_NO_ENCONTRADO);
        }

        String sqlWhere2 = super.crearConsultaSelectWhereComparacion(
                TABLA,
                CAMPOS_CONSULTA,
                CAMPO_SALARIO,
                ">=",
                "15000",
                TIPO_REAL
        );
        int subset2 = super.ejecutarSelectYMostrar(MSG_WHERE_2, sqlWhere2, CAMPOS_CONSULTA);
        if (subset2 == 0) {
            System.out.println(MSG_NO_ENCONTRADO);
        }
    }
}
