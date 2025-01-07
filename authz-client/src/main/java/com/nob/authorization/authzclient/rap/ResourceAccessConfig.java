package com.nob.authorization.authzclient.rap;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents the configuration for resource access in the context of an access control system.
 * This configuration provides details about resource name extraction, ignored paths,
 * and additional configuration parameters required for resource management.
 *
 * <p>This class is primarily used to define:</p>
 * <ul>
 *     <li>A regex pattern for extracting resource names from paths.</li>
 *     <li>A set of path templates that should be ignored during resource access checks.</li>
 *     <li>Additional configuration parameters for resource access customization.</li>
 * </ul>
 *
 * @author Truong Ngo
 */
@Getter
public class ResourceAccessConfig {

    /**
     * A regex pattern used to extract the resource name from a given path.
     */
    private final String resourceNameExtractor;

    /**
     * A set of path templates that define which paths should be ignored during resource access checks.
     */
    private final Set<String> ignoredPaths;

    /**
     * A map for storing additional configuration parameters for resource access.
     * This map is initialized as an empty {@link LinkedHashMap}.
     */
    private final Map<String, Object> config;

    /**
     * Constructs a new {@code ResourceAccessConfig} instance with the specified resource name extractor
     * and a set of ignored paths. Initializes the {@code config} map as an empty {@link LinkedHashMap}.
     *
     * @param resourceNameExtractor A regex string or pattern used to extract the resource name from a path.
     * @param ignoredPaths A set of path templates that specify paths to be ignored during access checks.
     */
    public ResourceAccessConfig(String resourceNameExtractor, Set<String> ignoredPaths) {
        this.resourceNameExtractor = resourceNameExtractor;
        this.ignoredPaths = ignoredPaths;
        this.config = new LinkedHashMap<>();
    }
}
