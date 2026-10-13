package mx.edu.uaz.ingsoft.poo2.modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import mx.edu.uaz.ingsoft.poo2.modelo.convertidores.LocalDateAttributeConverter;

import java.time.LocalDate;

/**
 * Constancia que acredita una inscripción aprobada.
 *
 * <p>Modela la relación <b>1:1</b> con {@link Inscripcion}: la columna
 * {@code id_inscripcion} es única, así que una inscripción tiene a lo sumo un
 * certificado. La emisión solo procede sobre una inscripción aprobada (lo
 * impone {@code CertificadoService}, no la entidad).
 *
 * <p>La inscripción se carga <b>LAZY</b> para forzar la navegación explícita y
 * poder enseñar en el tutor cuándo se toca la base de datos.
 */
@Entity
@Table(name = "certificados")
public class Certificado implements IEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Integer id;

    @Column(name = "activo")
    private boolean activo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inscripcion", unique = true)
    private Inscripcion inscripcion;

    @Column(name = "folio")
    private String folio;

    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    public Certificado() {
    }

    public Certificado(Integer id, boolean activo, Inscripcion inscripcion, String folio, LocalDate fechaEmision) {
        this.id = id;
        this.activo = activo;
        this.inscripcion = inscripcion;
        this.folio = folio;
        this.fechaEmision = fechaEmision;
    }

    @Override
    public String toString() {
        return "Certificado{" +
                "id=" + id +
                ", activo=" + activo +
                ", folio='" + folio + '\'' +
                ", fechaEmision=" + fechaEmision +
                ", inscripcionId=" + (inscripcion == null ? null : inscripcion.getId()) +
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

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
}