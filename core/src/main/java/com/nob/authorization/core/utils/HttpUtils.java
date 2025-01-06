package com.nob.authorization.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class HttpUtils {

    /**
     * Parses a query string into a map of key-value pairs.
     * <p>
     * This method takes a URL-encoded query string, decodes it, and splits it into key-value pairs,
     * handling multiple values for the same key by storing them in a list.
     * </p>
     *
     * @param queryString The query string to parse, typically found in the URL after the '?' character or value of form-urlencoded.
     *                    It can contain key-value pairs separated by '&' and '='. Example: "key1=value1&key2=value2&key1=value3".
     * @return A map where each key corresponds to a query parameter, and the value is a list of strings
     *         representing the associated values for that key. If a parameter has no value, an empty string
     *         is used as the value.
     */
    public static Map<String, List<String>> parseQueryString(String queryString) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        if (Objects.nonNull(queryString)) {
            String decodeString = URLDecoder.decode(queryString, StandardCharsets.UTF_8);
            String[] params = decodeString.split("&");
            for (String param : params) {
                String[] pair = param.split("=", 2);
                String key = pair[0];
                String value = pair.length > 1 ? pair[1] : null;
                result.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            }
        }
        return result;
    }


    /**
     * Reads the request body from an {@link HttpServletRequest}.
     * <p>
     * This method reads the body content of an HTTP request and returns it as a {@link String}.
     * It is typically used for processing POST or PUT requests where the body contains data, such as JSON or form data.
     * </p>
     *
     * @param request The {@link HttpServletRequest} object from which to read the request body.
     * @return A {@link String} containing the entire body of the HTTP request.
     * @throws IllegalStateException If an I/O error occurs while reading the request body.
     */
    public static String parseRequestBody(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("Cannot read request body: {}", e.getMessage());
            throw new IllegalStateException("Cannot read request body: " + e.getMessage());
        }
        return sb.toString();
    }


    /**
     * Parses a JSON string into a generic {@link Object}.
     * <p>
     * This method takes a JSON-formatted {@link String} and converts it into a generic object representation.
     * The resulting object could be a {@link Map}, {@link List}, or any other type, depending on the JSON content.
     * </p>
     *
     * @param body The JSON string to parse.
     * @return An {@link Object} representing the deserialized JSON content.
     * @throws IllegalStateException If the JSON cannot be deserialized due to invalid syntax or other errors.
     */
    public static Object parseJsonBody(String body) {
        try {
            return new ObjectMapper().readValue(body, Object.class);
        } catch (IOException e) {
            log.error("Cannot deserialize JSON request body: {}", e.getMessage());
            throw new IllegalStateException("Cannot deserialize JSON request body: " + e.getMessage());
        }
    }


    /**
     * Retrieves the headers from an HTTP request as a map of header names to their corresponding values.
     * <p>
     * This method iterates over all header names in the HTTP request and collects the values for each header.
     * Multiple values for a header (such as repeated "Cookie" or "Accept-Encoding" headers) are collected
     * in a list.
     * </p>
     *
     * @param request The {@link HttpServletRequest} from which headers are to be retrieved.
     *                This object contains all the header information from the HTTP request.
     * @return A map where the keys are the header names, and the values are lists of strings representing
     *         the corresponding header values. If a header has multiple values, they will appear in the list.
     *         If a header has a single value, the list will contain just one element.
     */
    public static Map<String, List<String>> getHeaders(HttpServletRequest request) {
        Map<String, List<String>> headers = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, Collections.list(request.getHeaders(headerName)));
        }
        return headers;
    }


    /**
     * Retrieves path variables from the HTTP request.
     * <p>
     * This method retrieves the path variables that are typically extracted from the URL template
     * when a request is matched to a handler method in a Spring MVC controller. The path variables
     * are stored as a map with variable names as keys and their corresponding values as the map's values.
     * </p>
     *
     * @param request The {@link HttpServletRequest} from which the path variables are retrieved.
     *                This should be the request object that contains the path variables set by the
     *                framework (e.g., Spring MVC).
     * @return A map of path variables, where the keys are the variable names and the values are the
     *         corresponding values extracted from the URI template. If no path variables exist, this
     *         may return `null` or an empty map, depending on the implementation.
     * @throws ClassCastException if the attribute in the request is not of type {@link Map}.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getPathVariables(HttpServletRequest request) {
        return (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    }


    /**
     * Retrieves the request parameters from an HTTP request as a map of parameter names to their values.
     * <p>
     * This method parses the query string from the given {@link HttpServletRequest} and returns a map
     * where each parameter name is associated with a list of its values.
     * </p>
     *
     * @param request The {@link HttpServletRequest} from which to retrieve the request parameters.
     *                This object contains the query string to be parsed.
     * @return A map where the keys are parameter names and the values are lists of strings representing
     *         the values associated with each parameter. If a parameter has multiple values, all values
     *         are included in the list. If no query string is present or parameters are not found, the
     *         map will be empty.
     */
    public static Map<String, List<String>> getQueryParameters(HttpServletRequest request) {
        return parseQueryString(request.getQueryString());
    }


    /**
     * Retrieves the parts of a multipart HTTP request.
     * <p>
     * This method is used to extract parts (e.g., files or form fields) from a multipart request
     * as a collection of {@link Part} objects. It relies on the {@link HttpServletRequest#getParts()}
     * method and handles exceptions that may occur during processing.
     * </p>
     *
     * @param request The {@link HttpServletRequest} containing the multipart data.
     * @return A collection of {@link Part} objects representing the parts of the request, or {@code null}
     *         if an error occurs during processing.
     */
    public static Collection<Part> getRequestParts(HttpServletRequest request) {
        try {
            return request.getParts();
        } catch (ServletException | IOException e) {
            log.error("Can not parse request parts: {}", e.getMessage());
            return null;
        }
    }


    /**
     * Retrieves metadata for all request parts (file or form data) from a multipart HTTP request.
     * This method processes each part of the request, extracts the metadata (such as file name, size, content type),
     * and returns a list of maps containing the metadata for each part.
     * @param request The HttpServletRequest object containing the multipart data.
     * @return A list of maps where each map contains metadata about a request part.
     *         If there are 2no parts or the parts are null, an empty list is returned.
     * @see #getPartMetadata(Part)
     * @see #getRequestParts(HttpServletRequest)
     */
    public static List<Map<String, Object>> getSerializableRequestParts(HttpServletRequest request) {
        Collection<Part> parts = getRequestParts(request);
        if (Objects.isNull(parts) || parts.isEmpty()) return Collections.emptyList();
        return parts.stream().map(HttpUtils::getPartMetadata).collect(Collectors.toList());
    }


    /**
     * Extracts and parses the body of an HTTP request based on its content type.
     * <p>
     * This method inspects the content-type of an {@link HttpServletRequest} to determine how to handle
     * and parse the request body. It supports:
     * <ul>
     *   <li><code>multipart/form-data</code>: Returns the request parts.</li>
     *   <li><code>application/x-www-form-urlencoded</code>: Parses the body as a query string and returns a {@link Map} of parameters.</li>
     *   <li><code>json</code>: Parses the body into a generic {@link Object}.</li>
     * </ul>
     * If the body is empty or the content type is unsupported, the method returns <code>null</code>.
     * </p>
     *
     * @param request The {@link HttpServletRequest} object from which to extract the body.
     * @return An {@link Object} representing the parsed request body. This could be:
     *         <ul>
     *           <li>A {@link Collection} of {@link Part} objects for multipart requests.</li>
     *           <li>A {@link Map} for form-encoded requests.</li>
     *           <li>A deserialized JSON object for JSON requests.</li>
     *           <li><code>null</code> if the body is empty or unsupported.</li>
     *         </ul>
     * @throws IllegalStateException If an error occurs while reading the request body or parsing it.
     * @see HttpServletRequest#getContentType()
     * @see #parseRequestBody(HttpServletRequest)
     * @see #parseQueryString(String)
     * @see #parseJsonBody(String)
     * @see #getRequestParts(HttpServletRequest)
     */
    public static Object getRequestBody(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (Objects.nonNull(contentType) && contentType.contains("multipart/form-data")) {
            return getRequestParts(request);
        }
        String body = parseRequestBody(request);
        if (body.trim().isEmpty()) {
            return null;
        }
        if (Objects.nonNull(contentType) && contentType.contains("application/x-www-form-urlencoded")) {
            return parseQueryString(body);
        }
        return parseJsonBody(body);
    }


    /**
     * Retrieves the serialized content of the HTTP request body.
     * This method processes the request body based on its content type and returns a corresponding representation.
     * <p>
     * The method handles different content types:
     * <ul>
     *     <li>{@code multipart/form-data}: If the content type is "multipart/form-data", the method processes the request parts (such as files or form data) and returns their metadata.</li>
     *     <li>{@code application/x-www-form-urlencoded}: If the content type is "application/x-www-form-urlencoded", the method parses the query string and returns the parameters as a map.</li>
     *     <li>{@code other}: For other content types, the method attempts to parse the body as JSON and returns it as a Java object.</li>
     * </ul>
     *
     * @param request The {@link HttpServletRequest} object containing the request data.
     * @return The parsed request body content, which can be:
     * <ul>
     *     <li>A list of maps containing metadata for each request part (in case of <code>multipart/form-data</code>).</li>
     *     <li>A map containing parameters for <code>application/x-www-form-urlencoded</code>.</li>
     *     <li>A Java object representing the JSON body content (for other content types).</li>
     *     <li>If the body is empty or cannot be parsed, the method returns null.</li>
     * </ul>
     */
    public static Object getSerializableRequestBody(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (Objects.nonNull(contentType) && contentType.contains("multipart/form-data")) {
            Collection<Part> parts = getRequestParts(request);
            if (Objects.isNull(parts) || parts.isEmpty()) return null;
            return parts.stream().map(HttpUtils::getPartMetadata).toList();
        }
        String body = parseRequestBody(request);
        if (body.trim().isEmpty()) {
            return null;
        }
        if (Objects.nonNull(contentType) && contentType.contains("application/x-www-form-urlencoded")) {
            return parseQueryString(body);
        }
        return parseJsonBody(body);
    }


    /**
     * Retrieves all cookies from the HTTP request.
     *
     * @param request The {@link HttpServletRequest} from which to retrieve cookies.
     * @return A {@link Map} where the keys are cookie names and the values are cookie values.
     *         Returns an empty map if no cookies are present.
     */
    public static Map<String, String> getAllCookies(HttpServletRequest request) {
        Map<String, String> cookiesMap = new LinkedHashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookiesMap.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookiesMap;
    }


    /**
     * Retrieves all session attributes from the HTTP request.
     *
     * @param request The {@link HttpServletRequest} from which to retrieve session attributes.
     * @return A {@link Map} where the keys are attribute names and the values are attribute values.
     *         Returns an empty map if the session does not exist or has no attributes.
     */
    public static Map<String, Object> getAllSessionAttributes(HttpServletRequest request) {
        Map<String, Object> sessionAttributes = new LinkedHashMap<>();
        HttpSession session = request.getSession(false); // Retrieve the session, do not create if not present
        if (session != null) {
            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                sessionAttributes.put(attributeName, session.getAttribute(attributeName));
            }
        }
        return sessionAttributes;
    }


    /**
     * Retrieves metadata information from a file part.
     * This method extracts common file metadata such as file name, size, content type,
     * encoding, content disposition, file extension, and the original file name.
     * <p>
     * The metadata is returned as a map, where the key is the metadata field name and the value
     * is the corresponding metadata value.
     *
     * @param part The Part object representing the uploaded file in a multipart request.
     * @return A Map containing metadata about the uploaded file part.
     *         The map keys and their corresponding values are:
     *         <ul>
     *         <li>{@code fileName}: The submitted file name (e.g., "image.jpg").</li>
     *         <li>{@code size}: The size of the uploaded file in bytes (e.g., 1024).</li>
     *         <li>{@code contentType}: The MIME type of the file (e.g., "image/jpeg", "application/pdf").</li>
     *         <li>{@code encoding}: The encoding of the file (if available).</li>
     *         <li>{@code contentDisposition}: The content disposition header (e.g., "inline", "attachment").</li>
     *         <li>{@code extension}: The file extension (e.g., "jpg", "pdf").</li>
     *         </ul>
     */
    public static Map<String, Object> getPartMetadata(Part part) {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("contentType", part.getContentType());
        metadata.put("headers", part.getHeaderNames().stream().collect(Collectors.toMap(h -> h, part::getHeader)));
        metadata.put("name", part.getName());
        if (Objects.isNull(part.getSubmittedFileName())) {
            try (InputStream is = part.getInputStream()) {
                metadata.put("value", new String(is.readAllBytes(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                log.error("Can not read part: {}", e.getMessage());
                throw new IllegalStateException("Can not read part: " + e.getMessage());
            }
        } else {
            metadata.put("fileName", part.getSubmittedFileName());
            metadata.put("size", part.getSize());
            metadata.put("encoding", part.getHeader("Content-Transfer-Encoding"));
            metadata.put("contentDisposition", part.getHeader("Content-Disposition"));
            metadata.put("extension", getFileExtension(part.getSubmittedFileName()));
        }
        return metadata;
    }


    /**
     * Utility method for {@link #getPartMetadata(Part)}. Extracts the file extension from the given file name.
     * <p>
     * This method looks for the last period (.) in the file name and returns the substring that
     * follows it as the file extension. If no period is found, an empty string is returned.
     *
     * @param fileName The file name from which to extract the extension (e.g., "image.jpg").
     * @return The file extension in lowercase (e.g., "jpg"), or an empty string if no extension is found.
     */
    private static String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            return fileName.substring(index + 1).toLowerCase();
        }
        return "";
    }
}
