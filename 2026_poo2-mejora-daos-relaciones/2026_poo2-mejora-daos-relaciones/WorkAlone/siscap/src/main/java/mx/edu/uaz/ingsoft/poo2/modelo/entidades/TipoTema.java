package mx.edu.uaz.ingsoft.poo2.modelo.entidades;

/**
 * Clasificación del conocimiento que cubre un {@link Tema}.
 *
 * <p>El conjunto es cerrado: modela a grandes rasgos los catálogos que ya
 * administra la empresa (lenguajes, frameworks, bases de datos, etc.) sin
 * obligar a un tema a apuntar a cada catálogo en particular.
 */
public enum TipoTema {
    LENGUAJE,
    FRAMEWORK,
    BASE_DATOS,
    SISTEMA_OPERATIVO,
    HERRAMIENTA,
    HABILIDAD_BLANDA
}