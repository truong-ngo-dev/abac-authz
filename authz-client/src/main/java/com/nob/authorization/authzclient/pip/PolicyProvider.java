package com.nob.authorization.authzclient.pip;

import com.nob.authorization.core.domain.AbstractPolicy;

/**
 * Provides access to the policy associated with a given service in an ABAC (Attribute-Based Access Control) system.
 * Implementations of this interface are responsible for retrieving the policy that governs the access control rules
 * for a specific service based on its attributes.
 * @author Truong Ngo
 */
public interface PolicyProvider {

    /**
     * Retrieves the policy for a given service in the ABAC system.
     * The policy defines the access control rules and conditions under which access to the service is granted or denied,
     * based on the attributes of the user, the resource, and the environment.
     *
     * @param serviceName The name of the service for which the policy is requested.
     * @return The {@link AbstractPolicy} associated with the specified service, which contains the access control rules.
     */
    AbstractPolicy getPolicy(String serviceName);
}
