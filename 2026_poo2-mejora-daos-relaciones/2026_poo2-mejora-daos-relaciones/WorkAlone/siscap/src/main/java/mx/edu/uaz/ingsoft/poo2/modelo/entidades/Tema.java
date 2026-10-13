package mx.edu.uaz.ingsoft.poo2.modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Tema o conocimiento que puede cubrir un curso (lado "uno" de la relación
 * {@code Tema 1:N Curso}). Se separa del nombre del curso para no repetir el
 * conocimiento en cada curso que lo aborda.
 */
@Entity
@Table(name = "temas")
public class Tema implements IEntidad {

    @Id
    // IDENTITY es la única estrategia de generación que SQLite soporta nativamente.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // columnDefinition fija el DDL a INTEGER (no BIGINT/INT) para SQLite.
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Integer id;

    @Column(name = "activo")
    private boolean activo;

    @Column(name = "nombre")
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoTema tipo;

    public Tema() {
    }

    public Tema(Integer id, boolean activo, String nombre, TipoTema tipo) {
        this.id = id;
        this.activo = activo;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Tema{" +
                "id=" + id +
                ", activo=" + activo +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                '}';
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean isActivo() {
        return activo;
    }

    @Override
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoTema getTipo() {
        return tipo;
    }

    public void setTipo(TipoTema tipo) {
        this.tipo = tipo;
    }
}