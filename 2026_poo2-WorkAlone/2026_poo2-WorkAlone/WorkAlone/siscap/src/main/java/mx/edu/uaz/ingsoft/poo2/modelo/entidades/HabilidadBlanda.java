package mx.edu.uaz.ingsoft.poo2.modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "habilidades_blandas")
public class HabilidadBlanda implements IEntidad {
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

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "nivel_esperado")
    private String nivelEsperado;

    @Column(name = "descripcion")
    private String descripcion;

    public HabilidadBlanda() {
    }

    public HabilidadBlanda(Integer id, boolean activo, String nombre, String categoria, String nivelEsperado,
                           String descripcion) {
        this.id = id;
        this.activo = activo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.nivelEsperado = nivelEsperado;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNivelEsperado() {
        return nivelEsperado;
    }

    public void setNivelEsperado(String nivelEsperado) {
        this.nivelEsperado = nivelEsperado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}