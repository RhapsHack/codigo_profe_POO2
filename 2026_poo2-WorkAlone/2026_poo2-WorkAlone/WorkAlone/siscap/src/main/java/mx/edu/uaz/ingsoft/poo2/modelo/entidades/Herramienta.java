package mx.edu.uaz.ingsoft.poo2.modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "herramientas")
public class Herramienta implements IEntidad {
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

    @Column(name = "proveedor")
    private String proveedor;

    @Column(name = "descripcion")
    private String descripcion;

    public Herramienta() {
    }

    public Herramienta(Integer id, boolean activo, String nombre, String tipo, String proveedor, String descripcion) {
        this.id = id;
        this.activo = activo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.proveedor = proveedor;
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

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}