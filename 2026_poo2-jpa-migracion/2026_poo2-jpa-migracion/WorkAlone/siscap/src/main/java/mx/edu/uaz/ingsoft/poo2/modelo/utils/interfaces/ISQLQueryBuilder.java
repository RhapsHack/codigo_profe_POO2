package mx.edu.uaz.ingsoft.poo2.modelo.utils.interfaces;

import mx.edu.uaz.ingsoft.poo2.modelo.utils.SQLQueryBuilder;

public interface ISQLQueryBuilder {
    SQLQueryBuilder addField(String nombreCampo);

    SQLQueryBuilder setColumnaOrden(String columnaOrden);

    String getQueryInsert();

    String getQueryFindById();

    String getQueryFindByField(String campo);

    String getQueryFindAllByField(String campo);

    String getQueryFindAll();

    String getQueryUpdate();

    String getQueryDeleteFromID();

    String getQueryDeleteFisicoFromID();

    String getQueryDeleteFisicoTodo();
}