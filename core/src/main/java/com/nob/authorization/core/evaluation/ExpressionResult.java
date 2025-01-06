package com.nob.authorization.core.evaluation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the result of evaluating an {@code Expression}.
 * This class encapsulates the result type and the cause of indeterminacy (if any).
 * It provides utility methods to determine the evaluation outcome.
 *
 * <p>Result types:
 * <ul>
 *   <li>{@code MATCH} - Indicates that the expression matches.</li>
 *   <li>{@code NO_MATCH} - Indicates that the expression does not match.</li>
 *   <li>{@code INDETERMINATE} - Indicates an error or uncertainty during evaluation.</li>
 * </ul>
 * </p>
 *
 * @author Truong Ngo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpressionResult {

    /**
     * The type of result for the expression evaluation.
     * Possible values are {@link ResultType#MATCH}, {@link ResultType#NO_MATCH},
     * or {@link ResultType#INDETERMINATE}.
     */
    private ResultType resultType;

    /**
     * The cause of indeterminacy, if the result is {@code INDETERMINATE}.
     */
    private IndeterminateCause indeterminateCause;

    /**
     * Constructs an {@code ExpressionResult} with the specified result type.
     *
     * @param resultType The type of result.
     */
    public ExpressionResult(ResultType resultType) {
        this.resultType = resultType;
    }

    /**
     * Checks if the result is {@code INDETERMINATE}.
     *
     * @return {@code true} if the result is {@code INDETERMINATE}, otherwise {@code false}.
     */
    public boolean isIndeterminate() {
        return resultType == ResultType.INDETERMINATE;
    }

    /**
     * Checks if the result is {@code NO_MATCH}.
     *
     * @return {@code true} if the result is {@code NO_MATCH}, otherwise {@code false}.
     */
    public boolean isNotMatch() {
        return resultType == ResultType.NO_MATCH;
    }

    /**
     * Checks if the result is {@code MATCH}.
     *
     * @return {@code true} if the result is {@code MATCH}, otherwise {@code false}.
     */
    public boolean isMatch() {
        return resultType == ResultType.MATCH;
    }

    /**
     * Enumeration of possible result types for expression evaluation.
     */
    public enum ResultType {
        /** Indicates that the expression matches. */
        MATCH,
        /** Indicates that the expression does not match. */
        NO_MATCH,
        /** Indicates an error or uncertainty during evaluation. */
        INDETERMINATE
    }
}