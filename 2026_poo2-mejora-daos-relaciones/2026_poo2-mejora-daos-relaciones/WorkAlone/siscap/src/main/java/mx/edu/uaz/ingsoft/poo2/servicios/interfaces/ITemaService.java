package mx.edu.uaz.ingsoft.poo2.servicios.interfaces;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;

import java.util.Optional;

public interface ITemaService extends IServicioBase<Tema> {
    Optional<Tema> buscarPorNombre(String nombre);
}