package mx.edu.uaz.ingsoft.poo2.modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tecnologias")
public class Tecnologia implements IEntidad {
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

    @Column(name = "categoria_general")
    private String categoriaGeneral;

    @Column(name = "fabricante_comunidad")
    private String fabricanteComunidad;

    @Column(name = "descripcion")
    private String descripcion;

    public Tecnologia() {
    }

    public Tecnologia(Integer id, boolean activo, String nombre, String categoriaGeneral, String fabricanteComunidad,
                      String descripcion) {
        this.id = id;
        this.activo = activo;
        this.nombre = nombre;
        this.categoriaGeneral = categoriaGeneral;
        this.fabricanteComunidad = fabricanteComunidad;
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

    public String getCategoriaGeneral() {
        return categoriaGeneral;
    }

    public void setCategoriaGeneral(String categoriaGeneral) {
        this.categoriaGeneral = categoriaGeneral;
    }

    public String getFabricanteComunidad() {
        return fabricanteComunidad;
    }

    public void setFabricanteComunidad(String fabricanteComunidad) {
        this.fabricanteComunidad = fabricanteComunidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}