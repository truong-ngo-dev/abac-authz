package com.nob.authorization.authzclient.pep;

import com.nob.authorization.authzclient.pip.PipEngine;
import com.nob.authorization.core.context.Action;
import com.nob.authorization.core.context.Resource;
import com.nob.authorization.core.context.Subject;
import com.nob.authorization.core.domain.AbstractPolicy;
import com.nob.authorization.core.pdp.*;
import lombok.extern.slf4j.Slf4j;

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
     * Constructs a new Policy Enforcement Point (PEP) engine with the specified decision strategy.
     * The PDP engine is initialized with the provided decision strategy to evaluate authorization requests.
     *
     * @param decisionStrategy The strategy to be used for evaluating policy decisions.
     */
    public PepEngine(DecisionStrategy decisionStrategy) {
        this.pdpEngine = new PdpEngine(new PdpConfiguration(decisionStrategy));
    }

    /**
     * Enforces an authorization decision based on the provided authorization request.
     * Retrieves all necessary information (e.g., policy, subject, resource, action, environment)
     * to construct an {@link AuthzRequest} and evaluates the decision using the PDP engine.
     *
     * @param authzRequest The authorization request containing the details to be evaluated.
     * @return The result of the authorization evaluation as an {@link AuthzDecision}.
     */
    public AuthzDecision enforce(AuthzRequest authzRequest) {
        log.info("enforce authz request: {}", authzRequest);
        AuthzDecision decision = pdpEngine.authorize(authzRequest);
        log.info("enforce decision: {}", decision.getDecision());
        return decision;
    }
}
