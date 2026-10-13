package mx.edu.uaz.ingsoft.poo2.modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import mx.edu.uaz.ingsoft.poo2.modelo.convertidores.LocalDateAttributeConverter;

import java.time.LocalDate;

/**
 * Inscripción de una persona a un curso.
 *
 * <p>Materializa la relación <b>N:M</b> entre {@link Persona} y {@link Curso}:
 * la tabla puente lleva atributos propios (la fecha de inscripción y el
 * resultado de la evaluación) que {@code @ManyToMany} no podría modelar. La
 * unicidad del par {@code (curso, persona)} impide inscribir dos veces a la
 * misma persona en el mismo curso.
 *
 * <p>Sus dos referencias se cargan <b>EAGER</b> para que al listar
 * inscripciones se tenga siempre el curso y la persona poblados.
 */
@Entity
@Table(name = "inscripciones",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_curso", "id_persona"}))
public class Inscripcion implements IEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Integer id;

    @Column(name = "activo")
    private boolean activo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_curso")
    private Curso curso;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_persona")
    private Persona persona;

    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name = "fecha")
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado")
    private ResultadoInscripcion resultado;

    public Inscripcion() {
    }

    public Inscripcion(Integer id, boolean activo, Curso curso, Persona persona,
                       LocalDate fecha, ResultadoInscripcion resultado) {
        this.id = id;
        this.activo = activo;
        this.curso = curso;
        this.persona = persona;
        this.fecha = fecha;
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "Inscripcion{" +
                "id=" + id +
                ", activo=" + activo +
                ", curso=" + (curso == null ? null : curso.getNombre()) +
                ", persona=" + (persona == null ? null : persona.getNombre()) +
                ", fecha=" + fecha +
                ", resultado=" + resultado +
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

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public ResultadoInscripcion getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoInscripcion resultado) {
        this.resultado = resultado;
    }
}