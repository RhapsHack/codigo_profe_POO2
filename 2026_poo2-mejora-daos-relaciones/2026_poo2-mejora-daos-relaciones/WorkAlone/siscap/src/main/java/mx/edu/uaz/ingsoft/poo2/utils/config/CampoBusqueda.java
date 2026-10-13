package mx.edu.uaz.ingsoft.poo2.utils.config;

/**
 * Configuración de un campo buscable de un catálogo.
 *
 * <p>{@code key} es la llave de la etiqueta en {@code mensajes.properties}
 * (lo que se muestra en el combo de campos). El atributo JPA es el nombre de
 * la propiedad de la entidad (p. ej. {@code lenguajeBase}) que usa la
 * Criteria API en {@code findByCampo}; el tipo indica cómo convertir el texto
 * escrito por el usuario antes de buscar.
 */
public record CampoBusqueda(String key, String atributoJpa, TipoCampoBusqueda tipo) {
}