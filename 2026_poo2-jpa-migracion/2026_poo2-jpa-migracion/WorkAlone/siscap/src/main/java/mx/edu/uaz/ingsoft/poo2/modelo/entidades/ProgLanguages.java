package mx.edu.uaz.ingsoft.poo2.modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "prog_languages")
public class ProgLanguages implements IEntidad {
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

    @Column(name = "paradigma")
    private String paradigma;

    @Column(name = "version_referencia")
    private String versionReferencia;

    @Column(name = "descripcion")
    private String descripcion;

    public ProgLanguages() {
    }

    public ProgLanguages(Integer id, boolean activo, String nombre, String paradigma, String versionReferencia,
                         String descripcion) {
        this.id = id;
        this.activo = activo;
        this.nombre = nombre;
        this.paradigma = paradigma;
        this.versionReferencia = versionReferencia;
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

    public String getParadigma() {
        return paradigma;
    }

    public void setParadigma(String paradigma) {
        this.paradigma = paradigma;
    }

    public String getVersionReferencia() {
        return versionReferencia;
    }

    public void setVersionReferencia(String versionReferencia) {
        this.versionReferencia = versionReferencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}