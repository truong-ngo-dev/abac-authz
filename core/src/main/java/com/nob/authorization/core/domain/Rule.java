package com.nob.authorization.core.domain;

import lombok.Data;

/**
 * Represents an XACML Rule element, which is a key component in an access control policy.
 * A Rule defines a condition and a target expression that determine whether a subject
 * is permitted or denied access to a resource based on the policy.
 *
 * @author Truong Ngo
 */
@Data
public class Rule implements Principle {

    /**
     * The unique identifier for the rule.
     * This ID is used to reference the rule within a policy.
     */
    private String id;

    /**
     * A description of the rule, providing additional context or explanation for its purpose.
     */
    private String description;

    /**
     * The target expression for the rule, defining the conditions under which the rule applies.
     * If the target expression evaluates to true, the rule is applicable.
     */
    private Expression target;

    /**
     * The condition expression of the rule. This expression defines additional criteria that must be
     * satisfied for the rule to be evaluated as applicable.
     */
    private Expression condition;

    /**
     * The effect of the rule, which determines whether access is permitted or denied if the rule applies.
     * The effect can either be {@link Effect#PERMIT} or {@link Effect#DENY}.
     */
    private Effect effect;

    /**
     * Defines the possible effects of a rule.
     * The two effects are:
     * <ul>
     *   <li>{@link #PERMIT} - Allows the action.</li>
     *   <li>{@link #DENY} - Denies the action.</li>
     * </ul>
     */
    public enum Effect {
        PERMIT,  // Grants permission to perform the action
        DENY     // Denies permission to perform the action
    }
}
