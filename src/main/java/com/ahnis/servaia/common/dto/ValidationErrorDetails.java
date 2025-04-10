package com.ahnis.servaia.common.dto;


import java.time.LocalDateTime;
import java.util.Map;

/**
 * A record representing detailed information about validation errors.
 * <p>
 * This record encapsulates the timestamp of the validation error, a descriptive message, and a map
 * of field-specific error messages. It is typically used to provide detailed feedback when request
 * validation fails.
 * </p>
 *
 * @param timestamp The timestamp when the validation error occurred.
 * @param message   A descriptive message about the validation error.
 * @param errors    A map of field names to error messages.
 *
 * @author Ahnis Singh Aneja
 */
public record ValidationErrorDetails(
        LocalDateTime timestamp, // Timestamp of the validation error
        String message, // Description of the validation error
        Map<String, String> errors // Map of field-specific error messages
) {
}
