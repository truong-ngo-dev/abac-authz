package com.nob.authorization.authzclient.pep;

import com.nob.authorization.authzclient.pip.PipEngine;
import com.nob.authorization.core.context.Action;
import com.nob.authorization.core.context.Environment;
import com.nob.authorization.core.context.Resource;
import com.nob.authorization.core.context.Subject;
import com.nob.authorization.core.domain.AbstractPolicy;
import com.nob.authorization.core.pdp.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * Represents the Policy Enforcement Point (PEP) engine in the context of an Attribute-Based Access Control (ABAC) system.
 * This engine enforces authorization decisions based on the policy and resource information retrieved from
 * the Policy Information Point (PIP) engine and evaluates the decision using the Policy Decision Point (PDP) engine.
 *
 * The PEP engine coordinates the enforcement of authorization decisions by retrieving the necessary details
 * about policies, subjects, resources, and actions, and then passing this data to the PDP engine for evaluation.
 * If the decision is favorable, the request is allowed; otherwise, it is denied.
 *
 * @see PdpEngine
 * @see PipEngine
 * @see AuthzDecision
 * @see AuthzRequest
 * @see AbstractPolicy
 * @see Subject
 * @see Resource
 * @see Action
 * @author Truong Ngo
 */
@Slf4j
public class PepEngine {

    /**
     * PDP engine used to evaluate authorization requests
     * */
    private final PdpEngine pdpEngine;

    /**
     * PIP engine used to retrieve the necessary data for authorization decisions
     * */
    private final PipEngine pipEngine;

    /**
     * The name of the service, typically injected from application configuration.
     */
    @Value("${spring.application.name}")
    private String serviceName;

    /**
     * Constructs a new PEP engine with the specified decision strategy and PIP engine.
     * The PDP engine is initialized with the provided decision strategy configuration.
     *
     * @param decisionStrategy The strategy to be used for policy decision evaluation.
     * @param pipEngine The PIP engine used to retrieve resource and subject information.
     */
    public PepEngine(DecisionStrategy decisionStrategy, PipEngine pipEngine) {
        this.pdpEngine = new PdpEngine(new PdpConfiguration(decisionStrategy));
        this.pipEngine = pipEngine;
    }

    /**
     * Enforces an authorization decision based on the provided HTTP request.
     * The method retrieves the necessary data (e.g., policy, subject, resource, action, environment)
     * and constructs an {@link AuthzRequest} to be evaluated by the PDP engine.
     *
     * @param request The HTTP servlet request to be authorized.
     * @return The authorization decision based on the policy evaluation.
     * @throws Exception if any error occurs during the enforcement process or policy evaluation.
     */
    public AuthzDecision enforce(HttpServletRequest request) throws Exception {
        AbstractPolicy policy = pipEngine.getPolicy(serviceName);
        Action action = new Action(request);
        Subject subject = pipEngine.getSubject(request.getUserPrincipal());
        Environment environment = pipEngine.getEnvironment(serviceName);
        Resource object = pipEngine.getResource(action);
        AuthzRequest authzRequest = new AuthzRequest(subject, object, action, environment, policy);
        return pdpEngine.authorize(authzRequest);
    }
}
