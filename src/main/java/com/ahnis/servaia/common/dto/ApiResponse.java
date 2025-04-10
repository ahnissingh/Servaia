package com.ahnis.servaia.common.dto;


import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * A generic record representing an API response.
 * <p>
 * This record encapsulates the HTTP status, a message, a timestamp, and the actual payload data.
 * It is designed to provide a consistent structure for API responses, making it easier to handle
 * success and error cases uniformly.
 * </p>
 *
 * @param status    The HTTP status code of the response.
 * @param message   A descriptive message about the response.
 * @param timestamp The timestamp when the response was generated.
 * @param data      The payload data included in the response.
 *
 * @author Ahnis Singh Aneja
 */
public record ApiResponse<T>(
        HttpStatus status, // HTTP status code
        String message, // Description of the response
        LocalDateTime timestamp, // Timestamp of the response
        T data // Actual payload
) {

    /**
     * Constructs an {@link ApiResponse} with the current timestamp.
     *
     * @param status  The HTTP status code of the response.
     * @param message A descriptive message about the response.
     * @param data    The payload data included in the response.
     */
    public ApiResponse(HttpStatus status, String message, T data) {
        this(status, message, LocalDateTime.now(), data);
    }

    /**
     * Creates a successful {@link ApiResponse} with a status of {@link HttpStatus#OK} and a default message.
     *
     * @param data The payload data included in the response.
     * @return A successful {@link ApiResponse}.
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK, "Success", data);
    }

    /**
     * Creates a successful {@link ApiResponse} with a custom status and message.
     *
     * @param status  The HTTP status code of the response.
     * @param message A descriptive message about the response.
     * @param data    The payload data included in the response.
     * @return A successful {@link ApiResponse}.
     */
    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    /**
     * Creates an error {@link ApiResponse} with no payload data.
     *
     * @param status  The HTTP status code of the response.
     * @param message A descriptive message about the error.
     * @return An error {@link ApiResponse}.
     */
    public static ApiResponse<Void> error(HttpStatus status, String message) {
        return new ApiResponse<>(status, message, null);
    }
}
