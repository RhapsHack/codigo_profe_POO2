package mx.edu.uaz.ingsoft.poo2.modelo.convertidores;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;

/**
 * Convierte {@link LocalDate} a texto ISO ({@code yyyy-MM-dd}) y viceversa.
 *
 * <p>SQLite no tiene un tipo de dato de fecha nativo; guardar la fecha como
 * texto ISO la mantiene legible y comparable, y evita depender de cómo el
 * driver traduce los tipos {@code java.time}. Se aplica campo por campo con
 * {@code @Convert}, así no hace falta registrarlo en {@code persistence.xml}.
 */
@Converter
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate fecha) {
        return fecha == null ? null : fecha.toString();
    }

    @Override
    public LocalDate convertToEntityAttribute(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return LocalDate.parse(valor);
    }
}