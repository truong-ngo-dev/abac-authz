package com.nob.authorization.core.pdp;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The {@code AuthzDecision} class represents an authorization decision made by the Policy Decision Point (PDP).
 * It encapsulates the final decision of whether access should be granted or denied, along with additional context.
 * This class can store the decision, the timestamp of when it was made, and any additional details related to the decision.
 * @author Truong Ngo
 */
@Data
@AllArgsConstructor
public class AuthzDecision {

    /**
     * The authorization decision indicating whether access is permitted or denied.
     */
    private Decision decision;

    /**
     * The timestamp when the decision was made, represented as milliseconds from the Unix epoch.
     */
    private Long timestamp;

    /**
     * Additional details related to the decision, which could be any context or message.
     */
    private Object details;

    /**
     * Enum representing the possible authorization decisions.
     * A {@code PERMIT} decision grants access, while a {@code DENY} decision prevents access.
     */
    public enum Decision {
        /**
         * The decision that grants access.
         */
        PERMIT,

        /**
         * The decision that denies access.
         */
        DENY
    }

    /**
     * Constructs an {@code AuthzDecision} with the specified decision and details.
     * The timestamp is automatically set to the current time in milliseconds.
     *
     * @param decision The authorization decision to set.
     * @param details Additional context or details related to the decision.
     */
    public AuthzDecision(Decision decision, Object details) {
        this.decision = decision;
        this.timestamp = System.currentTimeMillis();
        this.details = details;
    }

    /**
     * Checks if the decision is {@code PERMIT}.
     *
     * @return {@code true} if the decision is {@code PERMIT}, {@code false} otherwise.
     */
    public boolean isPermit() {
        return decision == Decision.PERMIT;
    }

    /**
     * Checks if the decision is {@code DENY}.
     *
     * @return {@code true} if the decision is {@code DENY}, {@code false} otherwise.
     */
    public boolean isDeny() {
        return decision == Decision.DENY;
    }
}
