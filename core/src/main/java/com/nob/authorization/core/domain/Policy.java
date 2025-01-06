package com.nob.authorization.core.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Represents an XACML Policy, which is a specific type of policy element that defines a set of rules
 * for access control decisions. A Policy is a subclass of {@link AbstractPolicy} and includes a list
 * of {@link Rule} elements that define conditions for allowing or denying access.
 *
 * @author Truong Ngo
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Policy extends AbstractPolicy {

    /**
     * A list of {@link Rule} elements associated with the policy.
     * Each rule defines specific conditions and effects (permit or deny) that determine whether
     * access should be granted or denied under certain circumstances.
     */
    private List<Rule> rules;
}

