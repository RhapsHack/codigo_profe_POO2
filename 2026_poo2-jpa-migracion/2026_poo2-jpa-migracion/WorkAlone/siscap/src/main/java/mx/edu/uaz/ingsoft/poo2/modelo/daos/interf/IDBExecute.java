package mx.edu.uaz.ingsoft.poo2.modelo.daos.interf;

@FunctionalInterface
public interface IDBExecute<T> {
    T executeDBCommand();
}