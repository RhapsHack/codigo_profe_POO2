package mx.edu.uaz.ingsoft.poo2.modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * Curso de capacitación que imparte la empresa.
 *
 * <p>Reúne tres relaciones del dominio:
 * <ul>
 *   <li>{@code Curso N:1 Tema}: el conocimiento que cubre.</li>
 *   <li>{@code Curso N:1 Persona} (instructor): quién lo dicta.</li>
 *   <li>{@code Curso 1:N Inscripcion}: quiénes lo toman.</li>
 * </ul>
 *
 * <p>Las referencias {@code tema} e {@code instructor} se cargan de forma
 * <b>EAGER</b> para que el listado las muestre sin consultas extra. La
 * colección {@code inscripciones} es <b>LAZY</b>: llega sin poblar y solo se
 * llena al navegarla o al pedirla explícitamente con el JOIN FETCH del DAO.
 * El campo {@code inscripcionesCargadas} es memoria (no persiste) y sirve para
 * que el tutor didáctico pueda contrastar si la colección ya se leyó.
 */
@Entity
@Table(name = "cursos")
public class Curso implements IEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER")
    private Integer id;

    @Column(name = "activo")
    private boolean activo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "objetivo")
    private String objetivo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tema")
    private Tema tema;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_instructor")
    private Persona instructor;

    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    @Transient
    private boolean inscripcionesCargadas = false;

    public Curso() {
    }

    public Curso(Integer id, boolean activo, String nombre, String objetivo, Tema tema, Persona instructor) {
        this.id = id;
        this.activo = activo;
        this.nombre = nombre;
        this.objetivo = objetivo;
        this.tema = tema;
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "id=" + id +
                ", activo=" + activo +
                ", nombre='" + nombre + '\'' +
                ", objetivo='" + objetivo + '\'' +
                ", tema=" + (tema == null ? null : tema.getNombre()) +
                ", instructor=" + (instructor == null ? null : instructor.getNombre()) +
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

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public Persona getInstructor() {
        return instructor;
    }

    public void setInstructor(Persona instructor) {
        this.instructor = instructor;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }

    public boolean isInscripcionesCargadas() {
        return inscripcionesCargadas;
    }

    public void setInscripcionesCargadas(boolean inscripcionesCargadas) {
        this.inscripcionesCargadas = inscripcionesCargadas;
    }

    /**
     * Mantiene ambos lados de la relación {@code Curso 1:N Inscripcion}: al
     * agregar una inscripción aquí también se fija su curso.
     */
    public void agregarInscripcion(Inscripcion inscripcion) {
        inscripciones.add(inscripcion);
        inscripcion.setCurso(this);
    }
}