package com.nob.authorization.authzclient.pip;

import com.nob.authorization.core.context.Action;
import com.nob.authorization.core.context.Environment;
import com.nob.authorization.core.context.Resource;
import com.nob.authorization.core.context.Subject;
import com.nob.authorization.core.domain.AbstractPolicy;
import java.security.Principal;

/**
 * Represents the Policy Information Point (PIP) engine in the context of an Attribute-Based Access Control (ABAC) system.
 * This engine integrates various providers for policies, environment, subjects, and resources,
 * and serves as a central point for fetching policy-related information and resources required for authorization decisions.
 *
 * The PIP engine utilizes the provided providers to retrieve necessary details about policies, environment settings,
 * subjects (users or entities), and resources for access control.
 *
 * @param policyProvider The provider for retrieving policies associated with a given service.
 * @param environmentProvider The provider for retrieving environment information for a given service.
 * @param subjectProvider The provider for retrieving the {@link Subject} information based on the {@link Principal}.
 * @param resourceProvider The provider for retrieving resources based on an {@link Action}.
 *
 * @see PolicyProvider
 * @see EnvironmentProvider
 * @see SubjectProvider
 * @see ResourceProvider
 * @see AbstractPolicy
 * @see Subject
 * @see Action
 * @author Truong Ngo
 */
public record PipEngine(PolicyProvider policyProvider, EnvironmentProvider environmentProvider,
                        SubjectProvider subjectProvider, ResourceProvider resourceProvider) {

    /**
     * Retrieves the policy associated with the given service name.
     *
     * @param serviceName The name of the service for which to retrieve the policy.
     * @return The {@link AbstractPolicy} associated with the given service.
     */
    public AbstractPolicy getPolicy(String serviceName) {
        return policyProvider.getPolicy(serviceName);
    }

    /**
     * Retrieves the environment information associated with the given service name.
     *
     * @param serviceName The name of the service for which to retrieve the environment.
     * @return The {@link Environment} associated with the given service.
     */
    public Environment getEnvironment(String serviceName) {
        return environmentProvider.getEnvironment(serviceName);
    }

    /**
     * Retrieves the {@link Subject} for the given {@link Principal}.
     * The {@link Subject} represents the user or entity involved in the authorization request.
     *
     * @param principal The {@link Principal} (e.g., authenticated user or entity).
     * @return The {@link Subject} associated with the given {@link Principal}.
     */
    public Subject getSubject(Principal principal) {
        return subjectProvider.getSubject(principal);
    }

    /**
     * Retrieves the resource for the given {@link Action}.
     * This method extracts resource details and associated metadata, which may include parameters
     * needed for authorization decisions.
     *
     * @param action The {@link Action} representing the HTTP request or authorization action.
     * @return The resource data corresponding to the given action.
     */
    public Resource getResource(Action action) {
        return resourceProvider.getResource(action);
    }
}
