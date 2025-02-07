package com.nob.authorization.core.context;

import com.nob.authorization.core.utils.HttpUtils;
import com.nob.authorization.core.utils.ReflectionUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

/**
 * Represents a structured abstraction of an HTTP request.
 * This class provides a detailed representation of an HTTP request, including its metadata,
 * headers, query parameters, path variables, cookies, session attributes, and body content.
 * <p>
 * It is particularly used in {@link Action} element in Attribute-Based Access Control (ABAC) systems.
 * @author Truong Ngo
 */
@Data
@Slf4j
public class HttpRequest {

    /**
     * Metadata about the HTTP request, such as timestamps or client-specific information.
     */
    private Metadata metadata;

    /**
     * The HTTP method of the request (e.g., GET, POST, PUT, DELETE).
     */
    private String method;

    /**
     * The context path of the application handling the request.
     */
    private String contextPath;

    /**
     * The URI (Uniform Resource Identifier Pattern) of the requested resource.
     */
    private String servletPattern;

    /**
     * The URI (Uniform Resource Identifier) of the requested resource.
     */
    private String requestedURI;

    /**
     * The full URL (Uniform Resource Locator) of the request.
     */
    private String requestURL;

    /**
     * The servlet path of the request, identifying the specific servlet handling it.
     */
    private String servletPath;

    /**
     * A map of HTTP headers, where each header name maps to a list of its associated values.
     */
    private Map<String, List<String>> headers;

    /**
     * A map of path variables extracted from the request URI.
     */
    private Map<String, String> pathVariables;

    /**
     * A map of query parameters from the request, where each parameter name maps to a list of its associated values.
     */
    private Map<String, List<String>> queryParams;

    /**
     * The body of the HTTP request, represented as a serializable object.
     */
    private Object requestBody;

    /**
     * A map of cookies associated with the HTTP request.
     * Each cookie name maps to its corresponding value.
     */
    private Map<String, String> cookies;

    /**
     * A map of session attributes associated with the HTTP request.
     * Each session attribute name maps to its corresponding value.
     */
    private Map<String, Object> session;

    /**
     * Parses an {@code HttpServletRequest} into an {@code HttpRequest} object.
     * This method extracts and structures details from the provided {@code HttpServletRequest},
     * such as metadata, headers, path variables, query parameters, cookies, session attributes, and the request body.
     *
     * @param request The {@code HttpServletRequest} object to parse.
     * @return An {@code HttpRequest} instance containing the parsed details.
     */
    public static HttpRequest parse(HttpServletRequest request) {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.metadata = Metadata.parse(request);
        httpRequest.method = request.getMethod();
        httpRequest.contextPath = request.getContextPath();
        httpRequest.servletPattern = HttpUtils.getServletPattern(request);
        httpRequest.requestedURI = request.getRequestURI();
        httpRequest.requestURL = request.getRequestURL().toString();
        httpRequest.servletPath = request.getServletPath();
        httpRequest.headers = HttpUtils.getHeaders(request);
        httpRequest.pathVariables = HttpUtils.getPathVariables(request);
        httpRequest.queryParams = HttpUtils.getQueryParameters(request);
        httpRequest.requestBody = HttpUtils.getSerializableRequestBody(request);
        httpRequest.cookies = HttpUtils.getAllCookies(request);
        httpRequest.session = HttpUtils.getAllSessionAttributes(request);
        return httpRequest;
    }

    /**
     * Retrieves a metadata value by key.
     *
     * @param key The key for the metadata value.
     * @return The metadata value associated with the key, or {@code null} if not found.
     */
    public Object getMetaData(String key) {
        return metadata.getValue(key);
    }

    /**
     * Retrieves a header value by key.
     *
     * @param key The key for the header.
     * @return A list of header values associated with the key, or {@code null} if not found.
     */
    public List<String> getHeader(String key) {
        return headers.get(key);
    }

    /**
     * Retrieves a path variable value by key.
     *
     * @param key The key for the path variable.
     * @return The path variable value associated with the key, or {@code null} if not found.
     */
    public String getPathVariable(String key) {
        return pathVariables.get(key);
    }

    /**
     * Retrieves a query parameter value by key.
     *
     * @param key The key for the query parameter.
     * @return A list of query parameter values associated with the key, or {@code null} if not found.
     */
    public List<String> getQueryParam(String key) {
        return queryParams.get(key);
    }

    /**
     * Retrieves a value from the request body based on a dot-separated path.
     *
     * @param path The dot-separated path specifying the value to retrieve.
     * @return The value located at the specified path, or {@code null} if not found.
     */
    public Object getRequestBody(String path) {
        return ReflectionUtils.getObjectValue(requestBody, path);
    }

    /**
     * Retrieves a cookie value by key.
     *
     * @param key The key for the cookie.
     * @return The cookie value associated with the key, or {@code null} if not found.
     */
    public String getCookie(String key) {
        return cookies.get(key);
    }

    /**
     * Retrieves a session attribute value by key.
     *
     * @param key The key for the session attribute.
     * @return The session attribute value associated with the key, or {@code null} if not found.
     */
    public Object getSession(String key) {
        return session.get(key);
    }

}


