package com.nob.authorization.authzclient.exception;

import lombok.Getter;

/**
 * Represents a custom exception for authorization-related errors in the system.
 * This exception provides additional details about the error, including specific information and
 * a timestamp for when the exception occurred.
 *
 * <p>Use this exception to handle scenarios where an authorization check fails or is invalid.</p>
 *
 * <p>The class includes:</p>
 * <ul>
 *     <li>An optional {@code detail} object to provide more context about the exception.</li>
 *     <li>A {@code timestamp} field to record the time when the exception was created.</li>
 * </ul>
 *
 * @author Truong Ngo
 */
@Getter
public class AuthorizationException extends RuntimeException {

    /**
     * Additional details about the exception.
     * This can include context-specific information or metadata about the error.
     */
    private Object detail;

    /**
     * The timestamp (in milliseconds) indicating when the exception occurred.
     */
    private Long timestamp;

    /**
     * Constructs an {@code AuthorizationException} with the specified message.
     *
     * @param message The detail message for the exception.
     */
    public AuthorizationException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code AuthorizationException} with the specified message, detail, and timestamp.
     *
     * @param message The detail message for the exception.
     * @param detail An object containing additional information about the exception.
     * @param timestamp The time (in milliseconds) when the exception occurred.
     */
    public AuthorizationException(String message, Object detail, Long timestamp) {
        super(message);
        this.detail = detail;
        this.timestamp = timestamp;
    }
}
