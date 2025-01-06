package com.nob.authorization.authzclient.rap;

/**
 * Provides a registry for managing {@link ResourceAccessMetadata} objects,
 * allowing retrieval metadata based on a unique resource key.
 * @author Truong Ngo
 */
public interface ResourceAccessPoint {

    /**
     * Retrieves the {@link ResourceAccessMetadata} associated with the given resource key.
     *
     * @param resourceKey The key representing the resource (combination of HTTP method and path template).
     * @return The {@link ResourceAccessMetadata} object if found; otherwise, {@code null}.
     */
     ResourceAccessMetadata getResourceAccessMetadata(String resourceKey);
}
