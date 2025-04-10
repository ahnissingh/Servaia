package com.ahnis.servaia.common.dto;


import java.time.LocalDateTime;

/**
 * A record representing detailed information about an error.
 * <p>
 * This record encapsulates the timestamp of the error, a descriptive message, and additional details
 * about the error. It is typically used to provide more context in error responses.
 * </p>
 *
 * @param timestamp The timestamp when the error occurred.
 * @param message   A descriptive message about the error.
 * @param details   Additional details about the error.
 *
 * @author Ahnis Singh Aneja
 */
public record ErrorDetails(
        LocalDateTime timestamp, // Timestamp of the error
        String message, // Description of the error
        String details // Additional details about the error
) {
}
