package com.nob.authorization.core.algorithm;

import com.nob.authorization.core.domain.AbstractPolicy;
import com.nob.authorization.core.domain.Principle;
import com.nob.authorization.core.domain.Rule;

/**
 * Enum representing the names of different combination algorithms used in XACML policies.
 * These algorithms define how multiple {@link Principle} elements, such as {@link Rule} or {@link AbstractPolicy},
 * are evaluated and combined to produce a final authorization decision.
 *
 * <ul>
 *   <li>{@code DENY_OVERRIDES}: Deny takes precedence over other decisions.</li>
 *   <li>{@code PERMIT_OVERRIDES}: Permit takes precedence over other decisions.</li>
 *   <li>{@code DENY_UNLESS_PERMIT}: Deny by default unless a permit decision is explicitly granted.</li>
 *   <li>{@code PERMIT_UNLESS_DENY}: Permit by default unless a deny decision is explicitly made.</li>
 *   <li>{@code FIRST_APPLICABLE}: The decision of the first applicable rule or policy is used.</li>
 *   <li>{@code ONLY_ONE_APPLICABLE}: Only one applicable rule or policy is allowed; otherwise, an error is raised.</li>
 * </ul>
 *
 * These algorithms are critical for defining how conflicting or multiple rules/policies are resolved.
 *
 * @author Truong Ngo
 */
public enum CombineAlgorithmName {

    /**
     * Deny overrides: Deny decisions take precedence over all other decisions.
     */
    DENY_OVERRIDES,

    /**
     * Permit overrides: Permit decisions take precedence over all other decisions.
     */
    PERMIT_OVERRIDES,

    /**
     * Deny unless permit: Deny is the default decision unless an explicit permit is granted.
     */
    DENY_UNLESS_PERMIT,

    /**
     * Permit unless deny: Permit is the default decision unless an explicit deny is made.
     */
    PERMIT_UNLESS_DENY,

    /**
     * First applicable: The result of the first applicable rule or policy is used.
     */
    FIRST_APPLICABLE,

    /**
     * Only one applicable: Only one rule or policy can apply; if multiple apply, it results in an error.
     */
    ONLY_ONE_APPLICABLE
}
