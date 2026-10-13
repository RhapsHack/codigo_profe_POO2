package mx.edu.uaz.ingsoft.poo2.utils;

public class Constantes {
    // Constantes para la conexión a la base de datos SQLite
    public static final String DATABASE_NAME = "capacitaciones.db";
    public static final String URL_DB = "jdbc:sqlite:" + DATABASE_NAME;

    // Constantes compartidas para campos
    public static final String FLD_ID = "ID";
    public static final String FLD_NOMBRE = "nombre";
    public static final String FLD_DESCRIPCION = "descripcion";
    public static final String FLD_ACTIVO = "activo";
    public static final String FLD_MODELO = "modelo";
    public static final String FLD_PROVEEDOR = "proveedor";
    public static final String FLD_PRIMER_APELLIDO = "primer_apellido";
    public static final String FLD_SEGUNDO_APELLIDO = "segundo_apellido";
    public static final String FLD_NUMERO_EMPLEADO = "numero_empleado";
    public static final String FLD_CORREO_ELECTRONICO = "correo_electronico";
    public static final String FLD_PARADIGMA = "paradigma";
    public static final String FLD_VERSION_REFERENCIA = "version_referencia";
    public static final String FLD_CATEGORIA_GENERAL = "categoria_general";
    public static final String FLD_FABRICANTE_COMUNIDAD = "fabricante_comunidad";
    public static final String FLD_FAMILIA = "familia";
    public static final String FLD_TIPO = "tipo";
    public static final String FLD_LENGUAJE_BASE = "lenguaje_base";
    public static final String FLD_CATEGORIA = "categoria";
    public static final String FLD_NIVEL_ESPERADO = "nivel_esperado";

    // Constantes de tipos y restricciones SQL
    public static final String TIPO_INTEGER = "INTEGER";
    public static final String TIPO_TEXT = "TEXT";
    public static final String TIPO_BOOLEAN = "BOOLEAN";
    public static final String RESTRICCION_PRIMARY_KEY_AUTOINCREMENT = "PRIMARY KEY AUTOINCREMENT";
    public static final String RESTRICCION_NOT_NULL = "NOT NULL";
    public static final String RESTRICCION_UNIQUE = "UNIQUE";

    // Constantes booleanas
    public static final boolean ACTIVO = true;
    public static final boolean INACTIVO = false;

    // Constantes para la tabla bases_de_datos
    public static final String TABLA_BASES_DE_DATOS = "bases_de_datos";

    // Constantes para la tabla personas
    public static final String TABLA_PERSONAS = "personas";

    // Constantes para la tabla prog_languages
    public static final String TABLA_PROG_LANGUAGES = "prog_languages";

    // Constantes para la tabla tecnologias
    public static final String TABLA_TECNOLOGIAS = "tecnologias";

    // Constantes para la tabla sistemas_operativos
    public static final String TABLA_SISTEMAS_OPERATIVOS = "sistemas_operativos";

    // Constantes para la tabla herramientas
    public static final String TABLA_HERRAMIENTAS = "herramientas";

    // Constantes para la tabla frameworks
    public static final String TABLA_FRAMEWORKS = "frameworks";

    // Constantes para la tabla habilidades_blandas
    public static final String TABLA_HABILIDADES_BLANDAS = "habilidades_blandas";

    // Mensajes para el menú y para programas de CRUD
    // Nota didáctica: las antiguas constantes numéricas OP_* (1..5) y los
    // textos de menú "a mano" (MSG_MENU_MOSTRAR_*, MSG_MENU_SALIR,
    // MSG_INGRESE_OPCION_MENU) se retiraron al adoptar la biblioteca de menús:
    // las opciones ahora se registran con VALOR_OPCION_* + TEXTO_MENU_*.

    public static final String MSG_SALIENDO_PROGRAMA = "Saliendo del programa...";
    public static final String MSG_MENU_CRUD_LENGUAJES = "=== Menú de CRUD de Lenguajes de Programación ===";
    public static final String TEXTO_MENU_MOSTRAR_TODOS_LENGUAJES = "Mostrar todos los lenguajes de programación";
    public static final String TEXTO_MENU_AGREGAR_LENGUAJE = "Agregar un lenguaje de programación";
    public static final String TEXTO_MENU_ACTUALIZAR_LENGUAJE = "Actualizar un lenguaje de programación";
    public static final String TEXTO_MENU_ELIMINAR_LENGUAJE = "Eliminar un lenguaje de programación";
    public static final String MSG_INGESTA_LENGUAJES_COMPLETADA =
            "La tabla de lenguajes de programación fue reiniciada con los registros iniciales.";
    public static final String MSG_CONFIRMAR_SALIDA = "¿Desea salir del programa? (S/N): ";
    public static final String MSG_ENCABEZADO_LENGUAJES = "=== Lenguajes de programación registrados ===";
    public static final String MSG_SIN_LENGUAJES = "No hay lenguajes de programación registrados.";
    public static final String MSG_INGRESE_ID_LENGUAJE = "Ingrese el id del lenguaje de programación: ";
    public static final String MSG_INGRESE_NOMBRE_LENGUAJE = "Ingrese el nombre del lenguaje de programación: ";
    public static final String MSG_INGRESE_PARADIGMA_LENGUAJE = "Ingrese el paradigma del lenguaje de programación: ";
    public static final String MSG_INGRESE_VERSION_REFERENCIA_LENGUAJE = "Ingrese la versión de referencia del lenguaje de programación: ";
    public static final String MSG_INGRESE_DESCRIPCION_LENGUAJE = "Ingrese la descripción del lenguaje de programación: ";
    public static final String MSG_LENGUAJE_AGREGADO = "Lenguaje de programación agregado correctamente.";
    public static final String MSG_LENGUAJE_ACTUALIZADO = "Lenguaje de programación actualizado correctamente.";
    public static final String MSG_LENGUAJE_BORRADO = "Lenguaje de programación borrado correctamente.";
    public static final String MSG_LENGUAJE_NO_ENCONTRADO = "No se encontró el lenguaje de programación indicado.";
    public static final String MSG_MANTENER_VALOR_ACTUAL = "Si solo presiona Enter, se conservará el valor actual.";
    public static final String MSG_VALOR_ACTUAL = "Valor actual: ";
    public static final String MSG_INGRESE_NUEVO_VALOR = "Ingrese el nuevo valor: ";
    public static final String MSG_INGRESE_ACTIVO_LENGUAJE = "Ingrese V o F para indicar si el lenguaje está activo: ";
    public static final String MSG_DETALLE_ID = "Id: ";
    public static final String MSG_DETALLE_NOMBRE = ", Nombre: ";
    public static final String MSG_DETALLE_PARADIGMA = ", Paradigma: ";
    public static final String MSG_DETALLE_VERSION_REFERENCIA = ", Versión referencia: ";
    public static final String MSG_DETALLE_DESCRIPCION = ", Descripción: ";
    public static final String MSG_DETALLE_ACTIVO = ", Activo: ";
    public static final String MSG_DETALLE_MODELO = ", Modelo: ";
    public static final String MSG_DETALLE_PROVEEDOR = ", Proveedor: ";
    public static final String MSG_DETALLE_PRIMER_APELLIDO = ", Primer apellido: ";
    public static final String MSG_DETALLE_SEGUNDO_APELLIDO = ", Segundo apellido: ";
    public static final String MSG_DETALLE_NUMERO_EMPLEADO = ", Número empleado: ";
    public static final String MSG_DETALLE_CORREO_ELECTRONICO = ", Correo electrónico: ";
    public static final String MSG_DETALLE_CATEGORIA_GENERAL = ", Categoría general: ";
    public static final String MSG_DETALLE_FABRICANTE_COMUNIDAD = ", Fabricante/comunidad: ";
    public static final String MSG_DETALLE_FAMILIA = ", Familia: ";
    public static final String MSG_DETALLE_TIPO = ", Tipo: ";
    public static final String MSG_DETALLE_LENGUAJE_BASE = ", Lenguaje base: ";
    public static final String MSG_DETALLE_CATEGORIA = ", Categoría: ";
    public static final String MSG_DETALLE_NIVEL_ESPERADO = ", Nivel esperado: ";

    public static final String MSG_MENU_CRUD_BASES_DE_DATOS = "=== Menú de CRUD de Bases de Datos ===";
    public static final String TEXTO_MENU_MOSTRAR_TODAS_BASES_DE_DATOS = "Mostrar todas las bases de datos";
    public static final String TEXTO_MENU_AGREGAR_BASE_DE_DATOS = "Agregar una base de datos";
    public static final String TEXTO_MENU_ACTUALIZAR_BASE_DE_DATOS = "Actualizar una base de datos";
    public static final String TEXTO_MENU_ELIMINAR_BASE_DE_DATOS = "Eliminar una base de datos";
    public static final String MSG_ENCABEZADO_BASES_DE_DATOS = "=== Bases de datos registradas ===";
    public static final String MSG_SIN_BASES_DE_DATOS = "No hay bases de datos registradas.";
    public static final String MSG_INGRESE_ID_BASE_DE_DATOS = "Ingrese el id de la base de datos: ";
    public static final String MSG_INGRESE_NOMBRE_BASE_DE_DATOS = "Ingrese el nombre de la base de datos: ";
    public static final String MSG_INGRESE_MODELO_BASE_DE_DATOS = "Ingrese el modelo de la base de datos: ";
    public static final String MSG_INGRESE_PROVEEDOR_BASE_DE_DATOS = "Ingrese el proveedor de la base de datos: ";
    public static final String MSG_INGRESE_DESCRIPCION_BASE_DE_DATOS = "Ingrese la descripción de la base de datos: ";
    public static final String MSG_INGRESE_ACTIVO_BASE_DE_DATOS = "Ingrese V o F para indicar si la base de datos está activa: ";
    public static final String MSG_BASE_DE_DATOS_AGREGADA = "Base de datos agregada correctamente.";
    public static final String MSG_BASE_DE_DATOS_ACTUALIZADA = "Base de datos actualizada correctamente.";
    public static final String MSG_BASE_DE_DATOS_BORRADA = "Base de datos borrada correctamente.";
    public static final String MSG_BASE_DE_DATOS_NO_ENCONTRADA = "No se encontró la base de datos indicada.";
    public static final String MSG_INGESTA_BASES_DE_DATOS_COMPLETADA = "La tabla de bases de datos fue reiniciada con los registros iniciales.";

    public static final String MSG_MENU_CRUD_PERSONAS = "=== Menú de CRUD de Personas ===";
    public static final String TEXTO_MENU_MOSTRAR_TODAS_PERSONAS = "Mostrar todas las personas";
    public static final String TEXTO_MENU_AGREGAR_PERSONA = "Agregar una persona";
    public static final String TEXTO_MENU_ACTUALIZAR_PERSONA = "Actualizar una persona";
    public static final String TEXTO_MENU_ELIMINAR_PERSONA = "Eliminar una persona";
    public static final String MSG_ENCABEZADO_PERSONAS = "=== Personas registradas ===";
    public static final String MSG_SIN_PERSONAS = "No hay personas registradas.";
    public static final String MSG_INGRESE_ID_PERSONA = "Ingrese el id de la persona: ";
    public static final String MSG_INGRESE_NOMBRE_PERSONA = "Ingrese el nombre de la persona: ";
    public static final String MSG_INGRESE_PRIMER_APELLIDO_PERSONA = "Ingrese el primer apellido de la persona: ";
    public static final String MSG_INGRESE_SEGUNDO_APELLIDO_PERSONA = "Ingrese el segundo apellido de la persona: ";
    public static final String MSG_INGRESE_NUMERO_EMPLEADO_PERSONA = "Ingrese el número de empleado de la persona: ";
    public static final String MSG_INGRESE_CORREO_PERSONA = "Ingrese el correo electrónico de la persona: ";
    public static final String MSG_INGRESE_ACTIVO_PERSONA = "Ingrese V o F para indicar si la persona está activa: ";
    public static final String MSG_PERSONA_AGREGADA = "Persona agregada correctamente.";
    public static final String MSG_PERSONA_ACTUALIZADA = "Persona actualizada correctamente.";
    public static final String MSG_PERSONA_BORRADA = "Persona borrada correctamente.";
    public static final String MSG_PERSONA_NO_ENCONTRADA = "No se encontró la persona indicada.";
    public static final String MSG_INGESTA_PERSONAS_COMPLETADA = "La tabla de personas fue reiniciada con los registros iniciales.";

    public static final String MSG_MENU_CRUD_TECNOLOGIAS = "=== Menú de CRUD de Tecnologías ===";
    public static final String TEXTO_MENU_MOSTRAR_TODAS_TECNOLOGIAS = "Mostrar todas las tecnologías";
    public static final String TEXTO_MENU_AGREGAR_TECNOLOGIA = "Agregar una tecnología";
    public static final String TEXTO_MENU_ACTUALIZAR_TECNOLOGIA = "Actualizar una tecnología";
    public static final String TEXTO_MENU_ELIMINAR_TECNOLOGIA = "Eliminar una tecnología";
    public static final String MSG_ENCABEZADO_TECNOLOGIAS = "=== Tecnologías registradas ===";
    public static final String MSG_SIN_TECNOLOGIAS = "No hay tecnologías registradas.";
    public static final String MSG_INGRESE_ID_TECNOLOGIA = "Ingrese el id de la tecnología: ";
    public static final String MSG_INGRESE_NOMBRE_TECNOLOGIA = "Ingrese el nombre de la tecnología: ";
    public static final String MSG_INGRESE_CATEGORIA_GENERAL_TECNOLOGIA = "Ingrese la categoría general de la tecnología: ";
    public static final String MSG_INGRESE_FABRICANTE_COMUNIDAD_TECNOLOGIA = "Ingrese el fabricante o comunidad de la tecnología: ";
    public static final String MSG_INGRESE_DESCRIPCION_TECNOLOGIA = "Ingrese la descripción de la tecnología: ";
    public static final String MSG_INGRESE_ACTIVO_TECNOLOGIA = "Ingrese V o F para indicar si la tecnología está activa: ";
    public static final String MSG_TECNOLOGIA_AGREGADA = "Tecnología agregada correctamente.";
    public static final String MSG_TECNOLOGIA_ACTUALIZADA = "Tecnología actualizada correctamente.";
    public static final String MSG_TECNOLOGIA_BORRADA = "Tecnología borrada correctamente.";
    public static final String MSG_TECNOLOGIA_NO_ENCONTRADA = "No se encontró la tecnología indicada.";
    public static final String MSG_INGESTA_TECNOLOGIAS_COMPLETADA = "La tabla de tecnologías fue reiniciada con los registros iniciales.";

    public static final String MSG_MENU_CRUD_SISTEMAS_OPERATIVOS = "=== Menú de CRUD de Sistemas Operativos ===";
    public static final String TEXTO_MENU_MOSTRAR_TODOS_SISTEMAS_OPERATIVOS = "Mostrar todos los sistemas operativos";
    public static final String TEXTO_MENU_AGREGAR_SISTEMA_OPERATIVO = "Agregar un sistema operativo";
    public static final String TEXTO_MENU_ACTUALIZAR_SISTEMA_OPERATIVO = "Actualizar un sistema operativo";
    public static final String TEXTO_MENU_ELIMINAR_SISTEMA_OPERATIVO = "Eliminar un sistema operativo";
    public static final String MSG_ENCABEZADO_SISTEMAS_OPERATIVOS = "=== Sistemas operativos registrados ===";
    public static final String MSG_SIN_SISTEMAS_OPERATIVOS = "No hay sistemas operativos registrados.";
    public static final String MSG_INGRESE_ID_SISTEMA_OPERATIVO = "Ingrese el id del sistema operativo: ";
    public static final String MSG_INGRESE_NOMBRE_SISTEMA_OPERATIVO = "Ingrese el nombre del sistema operativo: ";
    public static final String MSG_INGRESE_FAMILIA_SISTEMA_OPERATIVO = "Ingrese la familia del sistema operativo: ";
    public static final String MSG_INGRESE_PROVEEDOR_SISTEMA_OPERATIVO = "Ingrese el proveedor del sistema operativo: ";
    public static final String MSG_INGRESE_DESCRIPCION_SISTEMA_OPERATIVO = "Ingrese la descripción del sistema operativo: ";
    public static final String MSG_INGRESE_ACTIVO_SISTEMA_OPERATIVO = "Ingrese V o F para indicar si el sistema operativo está activo: ";
    public static final String MSG_SISTEMA_OPERATIVO_AGREGADO = "Sistema operativo agregado correctamente.";
    public static final String MSG_SISTEMA_OPERATIVO_ACTUALIZADO = "Sistema operativo actualizado correctamente.";
    public static final String MSG_SISTEMA_OPERATIVO_BORRADO = "Sistema operativo borrado correctamente.";
    public static final String MSG_SISTEMA_OPERATIVO_NO_ENCONTRADO = "No se encontró el sistema operativo indicado.";
    public static final String MSG_INGESTA_SISTEMAS_OPERATIVOS_COMPLETADA = "La tabla de sistemas operativos fue reiniciada con los registros iniciales.";

    public static final String MSG_MENU_CRUD_HERRAMIENTAS = "=== Menú de CRUD de Herramientas ===";
    public static final String TEXTO_MENU_MOSTRAR_TODAS_HERRAMIENTAS = "Mostrar todas las herramientas";
    public static final String TEXTO_MENU_AGREGAR_HERRAMIENTA = "Agregar una herramienta";
    public static final String TEXTO_MENU_ACTUALIZAR_HERRAMIENTA = "Actualizar una herramienta";
    public static final String TEXTO_MENU_ELIMINAR_HERRAMIENTA = "Eliminar una herramienta";
    public static final String MSG_ENCABEZADO_HERRAMIENTAS = "=== Herramientas registradas ===";
    public static final String MSG_SIN_HERRAMIENTAS = "No hay herramientas registradas.";
    public static final String MSG_INGRESE_ID_HERRAMIENTA = "Ingrese el id de la herramienta: ";
    public static final String MSG_INGRESE_NOMBRE_HERRAMIENTA = "Ingrese el nombre de la herramienta: ";
    public static final String MSG_INGRESE_TIPO_HERRAMIENTA = "Ingrese el tipo de la herramienta: ";
    public static final String MSG_INGRESE_PROVEEDOR_HERRAMIENTA = "Ingrese el proveedor de la herramienta: ";
    public static final String MSG_INGRESE_DESCRIPCION_HERRAMIENTA = "Ingrese la descripción de la herramienta: ";
    public static final String MSG_INGRESE_ACTIVO_HERRAMIENTA = "Ingrese V o F para indicar si la herramienta está activa: ";
    public static final String MSG_HERRAMIENTA_AGREGADA = "Herramienta agregada correctamente.";
    public static final String MSG_HERRAMIENTA_ACTUALIZADA = "Herramienta actualizada correctamente.";
    public static final String MSG_HERRAMIENTA_BORRADA = "Herramienta borrada correctamente.";
    public static final String MSG_HERRAMIENTA_NO_ENCONTRADA = "No se encontró la herramienta indicada.";
    public static final String MSG_INGESTA_HERRAMIENTAS_COMPLETADA = "La tabla de herramientas fue reiniciada con los registros iniciales.";

    public static final String MSG_MENU_CRUD_FRAMEWORKS = "=== Menú de CRUD de Frameworks ===";
    public static final String TEXTO_MENU_MOSTRAR_TODOS_FRAMEWORKS = "Mostrar todos los frameworks";
    public static final String TEXTO_MENU_AGREGAR_FRAMEWORK = "Agregar un framework";
    public static final String TEXTO_MENU_ACTUALIZAR_FRAMEWORK = "Actualizar un framework";
    public static final String TEXTO_MENU_ELIMINAR_FRAMEWORK = "Eliminar un framework";
    public static final String MSG_ENCABEZADO_FRAMEWORKS = "=== Frameworks registrados ===";
    public static final String MSG_SIN_FRAMEWORKS = "No hay frameworks registrados.";
    public static final String MSG_INGRESE_ID_FRAMEWORK = "Ingrese el id del framework: ";
    public static final String MSG_INGRESE_NOMBRE_FRAMEWORK = "Ingrese el nombre del framework: ";
    public static final String MSG_INGRESE_TIPO_FRAMEWORK = "Ingrese el tipo del framework: ";
    public static final String MSG_INGRESE_LENGUAJE_BASE_FRAMEWORK = "Ingrese el lenguaje base del framework: ";
    public static final String MSG_INGRESE_DESCRIPCION_FRAMEWORK = "Ingrese la descripción del framework: ";
    public static final String MSG_INGRESE_ACTIVO_FRAMEWORK = "Ingrese V o F para indicar si el framework está activo: ";
    public static final String MSG_FRAMEWORK_AGREGADO = "Framework agregado correctamente.";
    public static final String MSG_FRAMEWORK_ACTUALIZADO = "Framework actualizado correctamente.";
    public static final String MSG_FRAMEWORK_BORRADO = "Framework borrado correctamente.";
    public static final String MSG_FRAMEWORK_NO_ENCONTRADO = "No se encontró el framework indicado.";
    public static final String MSG_INGESTA_FRAMEWORKS_COMPLETADA = "La tabla de frameworks fue reiniciada con los registros iniciales.";

    public static final String MSG_MENU_CRUD_HABILIDADES_BLANDAS = "=== Menú de CRUD de Habilidades Blandas ===";
    public static final String TEXTO_MENU_MOSTRAR_TODAS_HABILIDADES_BLANDAS = "Mostrar todas las habilidades blandas";
    public static final String TEXTO_MENU_AGREGAR_HABILIDAD_BLANDA = "Agregar una habilidad blanda";
    public static final String TEXTO_MENU_ACTUALIZAR_HABILIDAD_BLANDA = "Actualizar una habilidad blanda";
    public static final String TEXTO_MENU_ELIMINAR_HABILIDAD_BLANDA = "Eliminar una habilidad blanda";
    public static final String MSG_ENCABEZADO_HABILIDADES_BLANDAS = "=== Habilidades blandas registradas ===";
    public static final String MSG_SIN_HABILIDADES_BLANDAS = "No hay habilidades blandas registradas.";
    public static final String MSG_INGRESE_ID_HABILIDAD_BLANDA = "Ingrese el id de la habilidad blanda: ";
    public static final String MSG_INGRESE_NOMBRE_HABILIDAD_BLANDA = "Ingrese el nombre de la habilidad blanda: ";
    public static final String MSG_INGRESE_CATEGORIA_HABILIDAD_BLANDA = "Ingrese la categoría de la habilidad blanda: ";
    public static final String MSG_INGRESE_NIVEL_ESPERADO_HABILIDAD_BLANDA = "Ingrese el nivel esperado de la habilidad blanda: ";
    public static final String MSG_INGRESE_DESCRIPCION_HABILIDAD_BLANDA = "Ingrese la descripción de la habilidad blanda: ";
    public static final String MSG_INGRESE_ACTIVO_HABILIDAD_BLANDA = "Ingrese V o F para indicar si la habilidad blanda está activa: ";
    public static final String MSG_HABILIDAD_BLANDA_AGREGADA = "Habilidad blanda agregada correctamente.";
    public static final String MSG_HABILIDAD_BLANDA_ACTUALIZADA = "Habilidad blanda actualizada correctamente.";
    public static final String MSG_HABILIDAD_BLANDA_BORRADA = "Habilidad blanda borrada correctamente.";
    public static final String MSG_HABILIDAD_BLANDA_NO_ENCONTRADA = "No se encontró la habilidad blanda indicada.";
    public static final String MSG_INGESTA_HABILIDADES_BLANDAS_COMPLETADA = "La tabla de habilidades blandas fue reiniciada con los registros iniciales.";
    public static final String MSG_INGESTA_INICIAL_COMPLETADA = "La ingesta inicial de todos los catálogos se realizó correctamente.";

    // ====== Constantes para la biblioteca de menús (IMenu / IOpcionMenu) ======

    // Valores que identifican a las opciones automáticas del menú
    public static final String VALOR_OPCION_CANCELAR = "cancelar";
    public static final String VALOR_OPCION_SALIR = "salir";

    // Textos de las opciones automáticas del menú
    public static final String TEXTO_OPCION_CANCELAR = "Cancelar";
    public static final String TEXTO_OPCION_SALIR = "Salir";

    // Configuración predeterminada del menú
    public static final String SEPARADOR_MENU_DEFAULT = ") ";

    // Mensajes que usan las implementaciones de menú al interactuar con el usuario
    public static final String MSG_MENU_SOLICITAR_OPCION = "Seleccione una opción: ";
    public static final String MSG_MENU_OPCION_NO_VALIDA = "La opción indicada no es válida. Intente de nuevo.";

    // Valores de las opciones comunes de los CRUD (identidad de cada opción de menú)
    public static final String VALOR_OPCION_MOSTRAR_TODOS = "mostrar-todos";
    public static final String VALOR_OPCION_AGREGAR = "agregar";
    public static final String VALOR_OPCION_ACTUALIZAR = "actualizar";
    public static final String VALOR_OPCION_ELIMINAR = "eliminar";

    // Mensajes de error de la biblioteca de menús (se muestran por System.err)
    public static final String ERR_OPCION_VALOR_REQUERIDO = "El valor de la opción de menú es requerido";
    public static final String ERR_OPCION_TEXTO_REQUERIDO = "El texto de la opción de menú es requerido";
    public static final String ERR_MENU_ESTILO_REQUERIDO = "El estilo de numeración del menú es requerido";
    public static final String ERR_MENU_SEPARADOR_REQUERIDO = "El separador del menú es requerido";

    /**
     * Compone el prompt para una lectura "actualizable": encabezado del campo,
     * valor actual y la indicación de que Enter conserva el valor. Lo usan los
     * CRUD al leer campos con {@code ILectorDatos}.
     */
    public static String mensajeActualizable(String mensajeCampo, Object valorActual) {
        String valor = (valorActual instanceof Boolean bool)
                ? (bool ? "V" : "F")
                : String.valueOf(valorActual);
        return mensajeCampo + System.lineSeparator()
                + MSG_VALOR_ACTUAL + valor + System.lineSeparator()
                + MSG_MANTENER_VALOR_ACTUAL + System.lineSeparator()
                + MSG_INGRESE_NUEVO_VALOR;
    }
}