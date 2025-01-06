package com.nob.authorization.core.domain;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nob.authorization.core.algorithm.CombineAlgorithmName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Abstract base class for policy elements such as {@code Policy} and {@code PolicySet}.
 * This class provides common attributes and functionality shared by both policies and policy sets.
 * It encapsulates information such as the policy ID, description, target expression,
 * combination algorithm, and whether the element is a root element in the policy tree.
 *
 * @author Truong Ngo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.DEDUCTION,
        include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Policy.class, name = "policy"),
        @JsonSubTypes.Type(value = PolicySet.class, name = "policySet")
})
public abstract class AbstractPolicy implements Principle {

    /**
     * The unique identifier for the policy or policy set.
     * This ID is used to reference the policy element within an authorization system.
     */
    private String id;

    /**
     * A description of the policy or policy set.
     * Provides additional context or details about the purpose and scope of the policy.
     */
    private String description;

    /**
     * The target expression of the policy or policy set.
     * Defines the conditions under which the policy applies. If the target expression evaluates to true,
     * the policy becomes applicable.
     */
    private Expression target;

    /**
     * The combination algorithm name used to evaluate the results of multiple policies in a policy set.
     * This defines how the individual policy decisions (such as permit or deny) are combined.
     */
    private CombineAlgorithmName combineAlgorithmName;

    /**
     * Indicates whether this policy or policy set is a root element.
     * A root element is typically the top-most policy or policy set in a policy tree.
     */
    private Boolean isRoot;
}
