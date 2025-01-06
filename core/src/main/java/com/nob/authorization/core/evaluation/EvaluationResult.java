package com.nob.authorization.core.evaluation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the result of an evaluation in a decision-making system.
 * This class encapsulates the result type and, if applicable, the cause of indeterminacy.
 * @author Truong Ngo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class EvaluationResult {

    /**
     * The type of the evaluation result (e.g., PERMIT, DENY, NOT_APPLICABLE, or INDETERMINATE types).
     */
    private EvaluationResultType result;

    /**
     * The cause of indeterminacy, if the result is indeterminate.
     */
    private IndeterminateCause indeterminateCause;

    /**
     * Constructs an {@code EvaluationResult} with the specified result type.
     * Intended for results without indeterminate causes (e.g., PERMIT, DENY, NOT_APPLICABLE).
     *
     * @param result The type of the evaluation result.
     */
    public EvaluationResult(EvaluationResultType result) {
        this.result = result;
    }

    /**
     * Checks if the result is one of the indeterminate types.
     *
     * @return {@code true} if the result is indeterminate, otherwise {@code false}.
     */
    public boolean isIndeterminate() {
        return result.isIndeterminate();
    }

    /**
     * Checks if the result is {@code PERMIT}.
     *
     * @return {@code true} if the result is {@code PERMIT}, otherwise {@code false}.
     */
    public boolean isPermit() {
        return result.isPermit();
    }

    /**
     * Checks if the result is {@code DENY}.
     *
     * @return {@code true} if the result is {@code DENY}, otherwise {@code false}.
     */
    public boolean isDeny() {
        return result.isDeny();
    }

    /**
     * Checks if the result is {@code NOT_APPLICABLE}.
     *
     * @return {@code true} if the result is {@code NOT_APPLICABLE}, otherwise {@code false}.
     */
    public boolean isNotApplicable() {
        return result.isNotApplicable();
    }

    /**
     * Enumeration representing the possible types of evaluation results.
     */
    public enum EvaluationResultType {
        PERMIT, DENY, NOT_APPLICABLE, INDETERMINATE,
        INDETERMINATE_D, INDETERMINATE_P, INDETERMINATE_DP;

        /**
         * Checks if the type is one of the indeterminate types.
         *
         * @return {@code true} if the type is indeterminate, otherwise {@code false}.
         */
        public boolean isIndeterminate() {
            return this.equals(INDETERMINATE) || this.equals(INDETERMINATE_D) ||
                   this.equals(INDETERMINATE_P) || this.equals(INDETERMINATE_DP);
        }

        /**
         * Checks if the type is {@code PERMIT}.
         *
         * @return {@code true} if the type is {@code PERMIT}, otherwise {@code false}.
         */
        public boolean isPermit() {
            return this.equals(PERMIT);
        }

        /**
         * Checks if the type is {@code DENY}.
         *
         * @return {@code true} if the type is {@code DENY}, otherwise {@code false}.
         */
        public boolean isDeny() {
            return this.equals(DENY);
        }


        /**
         * Checks if the type is {@code NOT_APPLICABLE}.
         *
         * @return {@code true} if the type is {@code NOT_APPLICABLE}, otherwise {@code false}.
         */
        public boolean isNotApplicable() {
            return this.equals(NOT_APPLICABLE);
        }
    }
}
