package com.nob.authorization.core.context;

import com.nob.authorization.core.utils.ReflectionUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Represents metadata associated with an HTTP request.
 * Encapsulates various details about the HTTP request, such as encoding, protocol,
 * server information, client connection details, and locale settings. This class provides a structured way
 * to access and manage this information.
 * @author Truong Ngo
 */
@Data
public class Metadata {

    /**
     * The character encoding of the request (e.g., UTF-8).
     */
    private String characterEncoding;

    /**
     * The protocol used for the request (e.g., HTTP/1.1).
     */
    private String protocol;

    /**
     * The scheme of the request (e.g., http, https).
     */
    private String scheme;

    /**
     * The name of the server handling the request.
     */
    private String serverName;

    /**
     * The port number on which the server is listening.
     */
    private int serverPort;

    /**
     * The IP address of the client that sent the request.
     */
    private String remoteAddress;

    /**
     * The hostname of the client that sent the request.
     */
    private String remoteHost;

    /**
     * The port number on the client used to send the request.
     */
    private int remotePort;

    /**
     * Indicates whether the request was made over a secure connection (HTTPS).
     */
    private boolean isSecure;

    /**
     * The hostname of the local server handling the request.
     */
    private String localName;

    /**
     * The IP address of the local server handling the request.
     */
    private String localAddress;

    /**
     * The port number on which the local server is listening.
     */
    private int localPort;

    /**
     * The session ID requested by the client, if any.
     */
    private String requestedSessionId;

    /**
     * The primary locale of the request, representing the client's language preference.
     */
    private Locale locale;

    /**
     * A list of all locales preferred by the client, in the order of preference.
     */
    private List<Locale> locales;

    /**
     * Parses an {@code HttpServletRequest} into a {@code Metadata} object.
     * This method extracts and structures various metadata details from the provided {@code HttpServletRequest}.
     *
     * @param request The {@code HttpServletRequest} object to parse.
     * @return A {@code Metadata} instance containing the parsed metadata details.
     */
    public static Metadata parse(HttpServletRequest request) {
        Metadata metadata = new Metadata();
        metadata.setCharacterEncoding(request.getCharacterEncoding());
        metadata.setProtocol(request.getProtocol());
        metadata.setScheme(request.getScheme());
        metadata.setServerName(request.getServerName());
        metadata.setServerPort(request.getServerPort());
        metadata.setRemoteAddress(request.getRemoteAddr());
        metadata.setRemoteHost(request.getRemoteHost());
        metadata.setRemotePort(request.getRemotePort());
        metadata.setSecure(request.isSecure());
        metadata.setLocalName(request.getLocalName());
        metadata.setLocalAddress(request.getLocalAddr());
        metadata.setLocalPort(request.getLocalPort());
        metadata.setRequestedSessionId(request.getRequestedSessionId());
        metadata.setLocale(request.getLocale());
        metadata.setLocales(Collections.list(request.getLocales()));
        return metadata;
    }


    /**
     * Retrieves a metadata value by key.
     *
     * @param key The key for the metadata value.
     * @return The metadata value associated with the key, or {@code null} if not found.
     */
    public Object getValue(String key) {
        return ReflectionUtils.getFieldValue(this, key);
    }
}
