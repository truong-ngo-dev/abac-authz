package com.nob.authorization.authzclient.rap;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents the configuration for resource access, providing details about
 * resource extraction and required fetch resources.
 * @author Truong Ngo
 */
@Getter
public class ResourceAccessConfig {

    /**
     * A regex string used to extract the resource name from a path.
     */
    private final String resourceNameExtractor;

    /**
     * A set of path templates that define the resources required to fetch.
     */
    private final Set<String> requiredFetchResources;

    /**
     * A map for storing additional configuration parameters.
     */
    private final Map<String, Object> config;

    /**
     * Constructs a ResourceAccessConfig instance with the specified resource name extractor
     * and required fetch resources. Initializes the configuration map as an empty {@code LinkedHashMap}.
     *
     * @param resourceNameExtractor A string expression or pattern to extract the resource name.
     * @param requiredFetchResources A set of templates defining resources that need to be fetched.
     */
    public ResourceAccessConfig(String resourceNameExtractor, Set<String> requiredFetchResources) {
        this.resourceNameExtractor = resourceNameExtractor;
        this.requiredFetchResources = requiredFetchResources;
        this.config = new LinkedHashMap<>();
    }
}
