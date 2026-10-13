package mx.edu.uaz.ingsoft.poo2.programas.capacitaciones;

import mx.edu.uaz.ingsoft.poo2.modelo.daos.DAOException;
import mx.edu.uaz.ingsoft.poo2.modelo.daos.DaoServiceFactory;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Certificado;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Curso;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Inscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Persona;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.ResultadoInscripcion;
import mx.edu.uaz.ingsoft.poo2.modelo.entidades.Tema;
import mx.edu.uaz.ingsoft.poo2.servicios.ReglaNegocioException;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.ICertificadoService;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.ICursoService;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IInscripcionService;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.IPeopleService;
import mx.edu.uaz.ingsoft.poo2.servicios.interfaces.ITemaService;
import mx.edu.uaz.ingsoft.poo2.utils.inout.impl.LectorDatosDialogos;
import mx.edu.uaz.ingsoft.poo2.utils.inout.interfaces.ILectorDatos;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_CERTIFICADO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_CURSO;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_INSCRIPCION;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_PEOPLE;
import static mx.edu.uaz.ingsoft.poo2.utils.Constantes.CAT_TEMA;

/**
 * Tutor didáctico de relaciones JPA: recorre, paso a paso, las cuatro
 * relaciones del dominio de capacitaciones (N:1, 1:N, N:M con atributos y 1:1)
 * y demuestra la carga diferida de una colección.
 *
 * <p>Depende únicamente de {@link ILectorDatos}; el medio de entrada real se
 * inyecta en el constructor. El {@code main} usa {@link LectorDatosDialogos},
 * pero el código del tutor funcionaría igual con {@code LectorDatosTexto}.
 */
public class TutorRelaciones {

    private final ILectorDatos lector;

    private final IPeopleService personaService;
    private final ITemaService temaService;
    private final ICursoService cursoService;
    private final IInscripcionService inscripcionService;
    private final ICertificadoService certificadoService;

    public TutorRelaciones(ILectorDatos lector) {
        this.lector = lector;
        this.personaService = (IPeopleService) DaoServiceFactory.getServicio(CAT_PEOPLE).orElseThrow();
        this.temaService = (ITemaService) DaoServiceFactory.getServicio(CAT_TEMA).orElseThrow();
        this.cursoService = (ICursoService) DaoServiceFactory.getServicio(CAT_CURSO).orElseThrow();
        this.inscripcionService = (IInscripcionService) DaoServiceFactory.getServicio(CAT_INSCRIPCION).orElseThrow();
        this.certificadoService = (ICertificadoService) DaoServiceFactory.getServicio(CAT_CERTIFICADO).orElseThrow();
    }

    public static void main(String[] args) {
        new TutorRelaciones(new LectorDatosDialogos()).recorrer();
    }

    public void recorrer() {
        lector.mostrarError("Tutor de relaciones JPA (capacitaciones)\n\n"
                + "Recorreremos: N:1 (Curso → Tema/Instructor), 1:N (Curso → Inscripciones),\n"
                + "N:M con atributos (Persona ↔ Curso vía Inscripción) y 1:1 (Inscripción ↔ Certificado).");

        Curso curso = pasoRelacionMuchosAUno();
        if (curso == null) {
            lector.mostrarError("No se pudo crear el curso. Fin del recorrido.");
            return;
        }
        Inscripcion inscripcion = pasoRelacionMuchosAMuchos(curso);
        pasoCargaDiferida(curso);
        if (inscripcion != null) {
            pasoRelacionUnoAUno(inscripcion);
        }
        pasoBorradoLogico(curso);
    }

    // ------------------------------------------------------------------
    // Paso 1: N:1 — Curso → Tema (EAGER) y Curso → Persona instructor (EAGER)
    // ------------------------------------------------------------------
    private Curso pasoRelacionMuchosAUno() {
        lector.mostrarError("PASO 1 — Relación N:1 (Muchos a Uno)\n\n"
                + "Un Curso pertenece a UN Tema y lo imparte UNA Persona. En la base,\n"
                + "cursos guarda las claves foráneas id_tema e id_instructor. Ambas\n"
                + "referencias se cargan EAGER: al leer un curso ya vienen hidratadas.\n\n"
                + "Elige el tema y el instructor entre los registros existentes.");

        Optional<Tema> tema = buscarTemaInteractivo();
        Optional<Persona> instructor = buscarPersonaInteractiva();
        if (tema.isEmpty() || instructor.isEmpty()) {
            lector.mostrarError("Tema o instructor inexistente.");
            return null;
        }
        String nombre = lector.leerTextoNoVacio("Nombre del curso: ");
        String objetivo = lector.leerTexto("Objetivo del curso: ");
        try {
            var nuevo = new Curso(null, true, nombre, objetivo, tema.get(), instructor.get());
            cursoService.guardar(nuevo);
            lector.mostrarError("Curso creado: " + nuevo);
            return nuevo;
        } catch (DAOException e) {
            lector.mostrarError("Error al guardar el curso (¿nombre duplicado?): " + e.getMessage());
            return null;
        }
    }

    // ------------------------------------------------------------------
    // Paso 2: N:M con atributos — Persona ↔ Curso vía Inscripcion
    // ------------------------------------------------------------------
    private Inscripcion pasoRelacionMuchosAMuchos(Curso curso) {
        lector.mostrarError("PASO 2 — Relación N:M con atributos\n\n"
                + "Una persona toma varios cursos y un curso tiene varias personas.\n"
                + "Como la inscripción guarda fecha y resultado, la N:M se materializa\n"
                + "con la entidad intermedia Inscripcion (dos N:1). El servicio fija\n"
                + "ambos lados de la relación Curso 1:N Inscripcion.");

        Optional<Persona> persona = buscarPersonaInteractiva();
        if (persona.isEmpty()) {
            lector.mostrarError("Persona inexistente.");
            return null;
        }
        LocalDate fecha = leerFecha("Fecha de inscripción (aaaa-mm-dd): ");
        if (fecha == null) {
            lector.mostrarError("Fecha inválida.");
            return null;
        }
        try {
            Inscripcion inscripcion = inscripcionService.inscribir(persona.get(), curso, fecha);
            lector.mostrarError("Inscripción creada con resultado inicial PENDIENTE: " + inscripcion);
            return inscripcion;
        } catch (ReglaNegocioException e) {
            lector.mostrarError("Regla de negocio: " + e.getMessage());
            return null;
        } catch (DAOException e) {
            lector.mostrarError("Error de persistencia: " + e.getMessage());
            return null;
        }
    }

    // ------------------------------------------------------------------
    // Paso 3: carga diferida vs. JOIN FETCH
    // ------------------------------------------------------------------
    private void pasoCargaDiferida(Curso curso) {
        lector.mostrarError("PASO 3 — Carga diferida (LAZY) vs. traer de una vez (JOIN FETCH)\n\n"
                + "Curso.inscripciones es LAZY: al buscar el curso no se consulta la\n"
                + "colección. El indicador inscripcionesCargadas nos dice si ya se pidió\n"
                + "de forma explícita.");
        Curso recargado = cursoService.buscarUnRegistro(curso.getId()).orElse(curso);
        lector.mostrarError("Tras la búsqueda normal:\n"
                + "  inscripcionesCargadas = " + recargado.isInscripcionesCargadas()
                + "\n  (la colección aún no se ha traído con un JOIN FETCH)");

        Curso conInscripciones = cursoService.cargarInscripciones(recargado);
        lector.mostrarError("Tras cargarInscripciones (JOIN FETCH):\n"
                + "  inscripcionesCargadas = " + conInscripciones.isInscripcionesCargadas()
                + "\n  inscripciones = " + conInscripciones.getInscripciones()
                + "\n\nAsí se evita el problema N+1: una sola consulta trae el curso con\n"
                + "todas sus inscripciones, en lugar de una consulta por inscripción.");
    }

    // ------------------------------------------------------------------
    // Paso 4: 1:1 — Inscripcion ↔ Certificado (regla en el servicio)
    // ------------------------------------------------------------------
    private void pasoRelacionUnoAUno(Inscripcion inscripcion) {
        lector.mostrarError("PASO 4 — Relación 1:1 (Uno a Uno)\n\n"
                + "Una inscripción tiene a lo sumo UN certificado. La tabla certificados\n"
                + "tiene id_inscripcion único. La regla (solo sobre APROBADA y sin\n"
                + "duplicados) vive en CertificadoService, no en la vista.");
        try {
            inscripcionService.actualizarResultado(inscripcion, ResultadoInscripcion.APROBADO);
            inscripcion.setResultado(ResultadoInscripcion.APROBADO);
            lector.mostrarError("Resultado de la inscripción actualizado a APROBADO.");
        } catch (DAOException e) {
            lector.mostrarError("Error al actualizar el resultado: " + e.getMessage());
            return;
        }

        String folio = lector.leerTextoNoVacio("Folio del certificado: ");
        LocalDate emision = leerFecha("Fecha de emisión (aaaa-mm-dd): ");
        if (emision == null) {
            lector.mostrarError("Fecha inválida.");
            return;
        }
        try {
            Certificado certificado = certificadoService.emitir(inscripcion, folio, emision);
            lector.mostrarError("Certificado emitido: " + certificado);
        } catch (ReglaNegocioException e) {
            lector.mostrarError("Regla de negocio: " + e.getMessage());
        } catch (DAOException e) {
            lector.mostrarError("Error de persistencia: " + e.getMessage());
        }

        lector.mostrarError("Intentemos emitir un SEGUNDO certificado para la misma\n"
                + "inscripción; el servicio debe rechazarlo.");
        try {
            certificadoService.emitir(inscripcion, folio + "-DUP", emision);
            lector.mostrarError("(Inesperado) se emitió un duplicado.");
        } catch (ReglaNegocioException e) {
            lector.mostrarError("Rechazado correctamente: " + e.getMessage());
        } catch (DAOException e) {
            lector.mostrarError("Error de persistencia: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Paso 5: borrado lógico
    // ------------------------------------------------------------------
    private void pasoBorradoLogico(Curso curso) {
        lector.mostrarError("PASO 5 — Borrado lógico\n\n"
                + "En estas entidades 'borrar' marca activo = false y los listados\n"
                + "omiten las filas inactivas, sin eliminar físicamente la fila.");
        try {
            cursoService.borrarRegistro(curso.getId());
        } catch (DAOException e) {
            lector.mostrarError("Error al borrar: " + e.getMessage());
            return;
        }
        List<Curso> activos = cursoService.recuperarTodosRegistros();
        lector.mostrarError("Cursos activos tras el borrado lógico: " + activos);
    }

    // ------------------------------------------------------------------
    // Auxiliares
    // ------------------------------------------------------------------
    private Optional<Tema> buscarTemaInteractivo() {
        lector.mostrarError("Temas disponibles:\n" + listar(temaService.recuperarTodosRegistros()));
        int id = lector.leerEntero("Id del tema: ");
        return temaService.buscarUnRegistro(id);
    }

    private Optional<Persona> buscarPersonaInteractiva() {
        lector.mostrarError("Personas disponibles:\n" + listar(personaService.recuperarTodosRegistros()));
        int id = lector.leerEntero("Id de la persona: ");
        return personaService.buscarUnRegistro(id);
    }

    private LocalDate leerFecha(String mensaje) {
        String texto = lector.leerTextoNoVacio(mensaje);
        try {
            return LocalDate.parse(texto.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private String listar(List<?> registros) {
        StringBuilder salida = new StringBuilder();
        for (Object registro : registros) {
            salida.append("  ").append(registro).append('\n');
        }
        return salida.toString();
    }
}