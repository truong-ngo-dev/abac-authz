package com.nob.authorization.authzclient.rap;

import lombok.Data;

/**
 * Represents the mapping of a method parameter to its corresponding source
 * in an HTTP request, such as query parameters, path variables, or headers.
 * @author Truong Ngo
 */
@Data
public class ParameterMapping {

    /**
     * The name of the parameter in the method.
     */
    private String parameterName;

    /**
     * The source of the parameter in the HTTP request (e.g., query parameter, path variable).
     */
    private HttpSource source;

    /**
     * The type of the parameter as defined in the method.
     */
    private Class<?> parameterType;

    /**
     * The index of the parameter in the method's parameter list.
     */
    private Integer index;

    /**
     * Enum representing the various sources from which an HTTP parameter can be derived.
     */
    public enum HttpSource {
        /**
         * Parameter derived from query parameters in the URL.
         */
        QUERY_PARAM,

        /**
         * Parameter extracted from the path variable in the URL.
         */
        PATH_VARIABLE,

        /**
         * Parameter obtained from the body of the HTTP request.
         */
        REQUEST_BODY,

        /**
         * Parameter derived from the headers of the HTTP request.
         */
        REQUEST_HEADER,

        /**
         * Parameter retrieved from cookies in the HTTP request.
         */
        COOKIE,

        /**
         * Parameter sourced from the HTTP session.
         */
        SESSION
    }
}
