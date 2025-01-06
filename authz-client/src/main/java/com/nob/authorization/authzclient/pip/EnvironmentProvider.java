package com.nob.authorization.authzclient.pip;

import com.nob.authorization.core.context.Environment;

/**
 * Provides access to the environment context associated with a given service
 * in an ABAC (Attribute-Based Access Control) authorization system. The environment
 * provides contextual information that can be used to evaluate access control policies
 * based on the attributes of the service.
 * @author Truong Ngo
 */
public interface EnvironmentProvider {

    /**
     * Retrieves the environment configuration for a given service in the ABAC system.
     * The environment is used to evaluate access control decisions based on attributes
     * related to the service.
     *
     * @param serviceName The name of the service for which the environment configuration is requested.
     * @return The {@link Environment} object containing the relevant attributes for the service.
     */
    Environment getEnvironment(String serviceName);
}
