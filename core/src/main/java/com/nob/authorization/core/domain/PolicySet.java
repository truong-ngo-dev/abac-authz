package com.nob.authorization.core.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Represents an XACML PolicySet, which is a container for multiple policies or other policy sets.
 * A PolicySet is a subclass of {@link AbstractPolicy} and is used to group policies and
 * define how their results should be combined using a specified combination algorithm.
 *
 * This class supports hierarchical policy organization and allows for more complex
 * access control decisions by combining multiple policies or policy sets.
 *
 * @author Truong Ngo
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PolicySet extends AbstractPolicy {

    /**
     * A list of {@link AbstractPolicy} elements contained within this policy set.
     * These can include individual {@link Policy} objects or nested {@link PolicySet} objects.
     * The results of these policies are combined according to the combination algorithm defined in {@link AbstractPolicy}.
     */
    private List<AbstractPolicy> policies;
}
