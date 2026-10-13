package mx.edu.uaz.ingsoft.poo2.modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "frameworks")
public class Framework implements IEntidad {
    @Id
    // OBLIGATORIO: IDENTITY es la única estrategia que SQLite soporta nativamente
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // OBLIGATORIO: Forzar a que el DDL generator use "INTEGER" y no "BIGINT" o "INT"
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Integer id;

    @Column(name = "activo")
    private boolean activo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "lenguaje_base")
    private String lenguajeBase;

    @Column(name = "descripcion")
    private String descripcion;

    public Framework() {
    }

    @Override
    public String toString() {
        return "Framework{" +
                "id=" + id +
                ", activo=" + activo +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", lenguajeBase='" + lenguajeBase + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    public Framework(Integer id, boolean activo, String nombre, String tipo, String lenguajeBase, String descripcion) {
        this.id = id;
        this.activo = activo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.lenguajeBase = lenguajeBase;
        this.descripcion = descripcion;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLenguajeBase() {
        return lenguajeBase;
    }

    public void setLenguajeBase(String lenguajeBase) {
        this.lenguajeBase = lenguajeBase;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}