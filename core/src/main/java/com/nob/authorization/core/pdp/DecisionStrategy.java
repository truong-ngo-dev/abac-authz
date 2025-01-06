package com.nob.authorization.core.pdp;

import com.nob.authorization.core.evaluation.EvaluationResult;

/**
 * Enum representing the different strategies used to decide the authorization outcome based on an {@link EvaluationResult}.
 * Each strategy determines how to handle the decision when the evaluation result is either {@code PERMIT}, {@code DENY},
 * {@code INDETERMINATE}, or {@code NOT_APPLICABLE}.
 * @author Truong Ngo
 */
public enum DecisionStrategy {

    /**
     * The default deny strategy:
     * - If the result is either {@code PERMIT} or {@code DENY}, the outcome is determined by the {@link #permitDenyDecide(EvaluationResult)} method.
     * - If the result is neither {@code PERMIT} nor {@code DENY}, it defaults to {@code DENY}.
     */
    DEFAULT_DENY {
        @Override
        public AuthzDecision.Decision decide(EvaluationResult result) {
            return (result.isPermit() || result.isDeny()) ? permitDenyDecide(result) : AuthzDecision.Decision.DENY;
        }
    },

    /**
     * The default permit strategy:
     * - If the result is either {@code PERMIT} or {@code DENY}, the outcome is determined by the {@link #permitDenyDecide(EvaluationResult)} method.
     * - If the result is neither {@code PERMIT} nor {@code DENY}, it defaults to {@code PERMIT}.
     */
    DEFAULT_PERMIT {
        @Override
        public AuthzDecision.Decision decide(EvaluationResult result) {
            return (result.isPermit() || result.isDeny()) ? permitDenyDecide(result) : AuthzDecision.Decision.PERMIT;
        }
    },

    /**
     * A strategy that handles the {@code NOT_APPLICABLE} and {@code INDETERMINATE} results:
     * - If the result is either {@code PERMIT} or {@code DENY}, the outcome is determined by the {@link #permitDenyDecide(EvaluationResult)} method.
     * - If the result is {@code INDETERMINATE}, it defaults to {@code DENY}.
     * - If the result is {@code NOT_APPLICABLE}, it defaults to {@code PERMIT}.
     */
    NOT_APPLICABLE_PERMIT_INDETERMINATE_DENY {
        @Override
        public AuthzDecision.Decision decide(EvaluationResult result) {
            if (result.isPermit() || result.isDeny()) return permitDenyDecide(result);
            if (result.isIndeterminate()) return AuthzDecision.Decision.DENY;
            return AuthzDecision.Decision.PERMIT;
        }
    };

    /**
     * Abstract method that defines the logic for making a decision based on the evaluation result.
     * Each {@link DecisionStrategy} must implement this method to return the appropriate authorization decision.
     *
     * @param result The {@link EvaluationResult} from the PDP evaluation.
     * @return The decision based on the evaluation result (either {@code PERMIT} or {@code DENY}).
     */
    public abstract AuthzDecision.Decision decide(EvaluationResult result);

    /**
     * Helper method to resolve the decision when the result is either {@code PERMIT} or {@code DENY}.
     *
     * @param result The {@link EvaluationResult} that indicates whether access is allowed or denied.
     * @return The decision ({@code PERMIT} or {@code DENY}) based on the result.
     */
    public AuthzDecision.Decision permitDenyDecide(EvaluationResult result) {
        return result.isPermit() ? AuthzDecision.Decision.PERMIT : AuthzDecision.Decision.DENY;
    }
}
