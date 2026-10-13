package mx.edu.uaz.ingsoft.poo2.modelo.utils;

import mx.edu.uaz.ingsoft.poo2.modelo.utils.interfaces.ISQLQueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Genera consultas SQL parametrizadas para un catálogo uniforme:
 * una columna "id" (entero), un grupo de columnas de datos registradas con
 * {@code addField} y una columna "activo" que soporta el borrado lógico.
 */
public class SQLQueryBuilder implements ISQLQueryBuilder {
    private static final String COLUMNA_ID = "id";
    private static final String COLUMNA_ACTIVO = "activo";
    private static final String COLUMNA_ORDEN = "nombre";
    private static final String ACTIVO_TRUE = "true";
    private static final String ACTIVO_FALSE = "false";

    private final String nombreTabla;
    private final List<String> campos;
    private String columnaOrden = COLUMNA_ORDEN;

    public SQLQueryBuilder(String nombreTabla) {
        this.nombreTabla = nombreTabla;
        campos = new ArrayList<>();
    }

    @Override
    public SQLQueryBuilder addField(String nombreCampo) {
        campos.add(nombreCampo);
        return this;
    }

    /**
     * Cambia la columna usada en el ORDER BY de {@link #getQueryFindAll()}.
     * Por omisión es "nombre" (todas las tablas de catálogo la tienen), pero
     * las tablas de relación (inscripciones, certificados) no: sus DAOs la
     * cambian a "id".
     */
    @Override
    public SQLQueryBuilder setColumnaOrden(String columnaOrden) {
        this.columnaOrden = columnaOrden;
        return this;
    }

    @Override
    public String getQueryInsert() {
        StringBuilder columnas = new StringBuilder();
        StringBuilder valores = new StringBuilder();
        boolean esPrimero = true;
        for (String campo : campos) {
            if (!esPrimero) {
                columnas.append(", ");
                valores.append(", ");
            }
            columnas.append(campo);
            valores.append("?");
            esPrimero = false;
        }
        return "INSERT INTO " + nombreTabla + " (" + columnas + ", " + COLUMNA_ACTIVO
                + ") VALUES (" + valores + ", ?)";
    }

    @Override
    public String getQueryFindById() {
        return "SELECT " + columnasDeSeleccion() + " FROM " + nombreTabla
                + " WHERE " + COLUMNA_ID + " = ? AND " + COLUMNA_ACTIVO + " = " + ACTIVO_TRUE;
    }

    @Override
    public String getQueryFindByField(String campo) {
        return "SELECT " + columnasDeSeleccion() + " FROM " + nombreTabla
                + " WHERE " + campo + " = ? AND " + COLUMNA_ACTIVO + " = " + ACTIVO_TRUE;
    }

    /**
     * Igual que {@link #getQueryFindByField(String)} pero sin fijar cantidad:
     * trae TODAS las filas activas que cumplen {@code campo = ?}.
     */
    @Override
    public String getQueryFindAllByField(String campo) {
        return "SELECT " + columnasDeSeleccion() + " FROM " + nombreTabla
                + " WHERE " + campo + " = ? AND " + COLUMNA_ACTIVO + " = " + ACTIVO_TRUE;
    }

    @Override
    public String getQueryFindAll() {
        return "SELECT " + columnasDeSeleccion() + " FROM " + nombreTabla
                + " WHERE " + COLUMNA_ACTIVO + " = " + ACTIVO_TRUE
                + " ORDER BY " + columnaOrden + " ASC";
    }

    @Override
    public String getQueryUpdate() {
        StringBuilder sets = new StringBuilder();
        boolean esPrimero = true;
        for (String campo : campos) {
            if (!esPrimero) {
                sets.append(", ");
            }
            sets.append(campo).append(" = ?");
            esPrimero = false;
        }
        sets.append(", ").append(COLUMNA_ACTIVO).append(" = ?");
        return "UPDATE " + nombreTabla + " SET " + sets
                + " WHERE " + COLUMNA_ID + " = ?";
    }

    @Override
    public String getQueryDeleteFromID() {
        return "UPDATE " + nombreTabla + " SET " + COLUMNA_ACTIVO + " = " + ACTIVO_FALSE
                + " WHERE " + COLUMNA_ID + " = ?";
    }

    @Override
    public String getQueryDeleteFisicoFromID() {
        return "DELETE FROM " + nombreTabla + " WHERE " + COLUMNA_ID + " = ?";
    }

    @Override
    public String getQueryDeleteFisicoTodo() {
        return "DELETE FROM " + nombreTabla;
    }

    private String columnasDeSeleccion() {
        StringBuilder columnas = new StringBuilder(COLUMNA_ID);
        for (String campo : campos) {
            columnas.append(", ").append(campo);
        }
        return columnas.append(", ").append(COLUMNA_ACTIVO).toString();
    }
}