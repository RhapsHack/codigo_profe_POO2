package mx.edu.uaz.ingsoft.poo2.utils.validadores.entidades;

import mx.edu.uaz.ingsoft.poo2.modelo.entidades.IEntidad;
import mx.edu.uaz.ingsoft.poo2.utils.validadores.ValidadorString;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas del validador de entidad: acumulación de errores de todos los
 * campos y obtención automática de los valores desde la entidad (sin
 * asignación manual previa).
 *
 * <p>Usa una entidad de prueba local (no una entidad real del modelo) para
 * que el binding se pruebe de forma aislada.
 */
public class ValidadorEntidadTest {

    /** Entidad mínima de prueba: dos campos con validación registrada. */
    private static class RegistroPrueba implements IEntidad {
        private Integer id;
        private boolean activo;
        private String nombre;
        private String correo;

        private RegistroPrueba(String nombre, String correo) {
            this.nombre = nombre;
            this.correo = correo;
        }

        @Override
        public Integer getId() { return id; }

        @Override
        public void setId(Integer id) { this.id = id; }

        @Override
        public boolean isActivo() { return activo; }

        @Override
        public void setActivo(boolean activo) { this.activo = activo; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
    }

    /** Registro en una sola operación: campo + validador + getter. */
    private static class RegistroValidador extends ValidadorEntidad<RegistroPrueba> {
        private RegistroValidador() {
            agregarValidador("nombre",
                    new ValidadorString().noVacia().soloAlfabeticas(),
                    RegistroPrueba::getNombre);
            agregarValidador("correo",
                    new ValidadorString().noVacia().cumpleRegExp(".+@.+"),
                    RegistroPrueba::getCorreo);
        }
    }

    @Test
    @DisplayName("Dos campos inválidos: la lista de errores contiene los mensajes de ambos")
    public void dosCamposInvalidosReportaErroresDeAmbos() {
        RegistroValidador validador = new RegistroValidador();
        RegistroPrueba registro = new RegistroPrueba("", "sin-arroba");

        assertFalse(validador.validar(registro));

        List<String> errores = validador.getErrores();
        assertTrue(errores.size() >= 2, "Debe acumular errores de los dos campos: " + errores);
        assertTrue(errores.stream().anyMatch(m -> m.startsWith("Nombre:") && m.contains("vacía")),
                "Falta el error etiquetado del nombre: " + errores);
        assertTrue(errores.stream().anyMatch(m -> m.startsWith("correo:") && m.contains("RegExp")),
                "Falta el error etiquetado del correo: " + errores);
    }

    @Test
    @DisplayName("Cada mensaje lleva la etiqueta visible del campo; sin etiqueta en el bundle se usa la llave cruda")
    public void cadaMensajeSeEtiquetaConElCampo() {
        RegistroValidador validador = new RegistroValidador();
        RegistroPrueba registro = new RegistroPrueba("", "sin-arroba");

        validador.validar(registro);
        List<String> errores = validador.getErrores();

        assertTrue(errores.stream().allMatch(m -> m.contains(": ")),
                "Todos los mensajes deben llevar etiqueta de campo: " + errores);
        assertTrue(errores.stream().anyMatch(m -> m.startsWith("Nombre: ")),
                "La llave 'nombre' existe en el bundle y debe resolverse como etiqueta: " + errores);
        assertTrue(errores.stream().anyMatch(m -> m.startsWith("correo: ")),
                "La llave 'correo' no existe en el bundle y debe usarse cruda: " + errores);
    }

    @Test
    @DisplayName("Cada campo se evalúa contra el valor real de la entidad, sin paso previo de asignación")
    public void leeElValorDeLaEntidadSinPasoPrevioDeAsignacion() {
        RegistroValidador validador = new RegistroValidador();
        RegistroPrueba registro = new RegistroPrueba("", "sin-arroba");

        assertFalse(validador.validar(registro));
        assertFalse(validador.getErrores().isEmpty());

        registro.setNombre("Ana");
        registro.setCorreo("ana@empresa.com");

        assertTrue(validador.validar(registro));
        assertTrue(validador.getErrores().isEmpty());
    }

    @Test
    @DisplayName("La entidad válida reporta cero errores y getErrores() devuelve la lista vacía")
    public void entidadValidaSinErrores() {
        RegistroValidador validador = new RegistroValidador();
        RegistroPrueba registro = new RegistroPrueba("Ana", "ana@empresa.com");

        assertTrue(validador.validar(registro));
        assertEquals(List.of(), validador.getErrores());
    }
}
