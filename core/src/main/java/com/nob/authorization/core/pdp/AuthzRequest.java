package com.nob.authorization.core.pdp;

import com.nob.authorization.core.context.*;
import com.nob.authorization.core.domain.AbstractPolicy;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents an authorization request in an Attribute-Based Access Control (ABAC) system.
 * This class encapsulates the subject, object, action, environment, and associated policy
 * to evaluate whether the subject is authorized to perform a given action on an object in a certain environment.
 *
 * @author Truong Ngo
 */
@Data
@AllArgsConstructor
public class AuthzRequest {

    /**
     * The subject making the authorization request (typically a user or an entity).
     */
    private Subject subject;

    /**
     * The object that the subject wants to interact with or access.
     */
    private Resource object;

    /**
     * The action that the subject intends to perform on the object.
     */
    private Action action;

    /**
     * The environment in which the authorization request is being made, which may include factors like time, location, etc.
     */
    private Environment environment;

    /**
     * The policy associated with the authorization request, which determines the rules and conditions for access.
     */
    private AbstractPolicy policy;

    /**
     * Creates an {@link EvaluationContext} from the current authorization request,
     * which combines the subject, object, action, and environment for evaluation against the policy.
     *
     * @return A new {@link EvaluationContext} representing the current request context.
     */
    public EvaluationContext getEvaluationContext() {
        return new EvaluationContext(subject, object, action, environment);
    }
}
