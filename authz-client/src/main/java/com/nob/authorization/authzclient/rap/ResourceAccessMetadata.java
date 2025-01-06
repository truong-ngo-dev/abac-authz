package com.nob.authorization.authzclient.rap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * Represents metadata for resource access, encapsulating information
 * about the resource type, access path, HTTP method, accessor details,
 * and parameter mappings.
 * @author Truong Ngo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceAccessMetadata {

    /**
     * The type of the resource being accessed.
     */
    private Class<?> resourceType;

    /**
     * The path template associated with the resource.
     */
    private String pathTemplate;

    /**
     * The HTTP method used for accessing the resource.
     */
    private HttpMethod httpMethod;

    /**
     * The class of the accessor responsible for handling the resource access.
     */
    private Class<?> accessor;

    /**
     * The method name in the accessor class used for accessing the resource.
     */
    private String accessorMethod;

    /**
     * A list of mappings between method parameters and their corresponding resource attributes.
     */
    private List<ParameterMapping> parameterMappings;

}
