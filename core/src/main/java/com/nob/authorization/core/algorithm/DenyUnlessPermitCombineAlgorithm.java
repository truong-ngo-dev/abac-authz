package com.nob.authorization.core.algorithm;

import com.nob.authorization.core.context.EvaluationContext;
import com.nob.authorization.core.domain.Principle;
import com.nob.authorization.core.evaluation.EvaluationResult;

import java.util.List;

/**
 * This class implements the "Deny Unless Permit" combining algorithm,
 * which evaluates a list of principles and returns a PERMIT if at least
 * one of the evaluations results in a PERMIT; otherwise, it returns DENY.
 *
 * @param <E> The type of principles being evaluated, extending the {@code Principle} interface.
 * @author Truong Ngo
 */
public class DenyUnlessPermitCombineAlgorithm<E extends Principle> implements CombineAlgorithm<E> {

    /**
     * Evaluates a list of principles against the provided evaluation context.
     * The "Deny Unless Permit" algorithm is used, which means:
     * - If any principle evaluates to PERMIT, the result is PERMIT.
     * - Otherwise, the result is DENY.
     *
     * @param principles The list of principles to evaluate.
     * @param context    The evaluation context providing additional data for evaluation.
     * @return an {@code EvaluationResult} of type PERMIT if any principle evaluates to PERMIT,
     *         otherwise an {@code EvaluationResult} of type DENY.
     */
    @Override
    public EvaluationResult evaluate(List<E> principles, EvaluationContext context) {
        List<EvaluationResult> results = getListEvaluationResult(principles, context);
        return results.stream().anyMatch(EvaluationResult::isPermit) ?
                new EvaluationResult(EvaluationResult.EvaluationResultType.PERMIT) :
                new EvaluationResult(EvaluationResult.EvaluationResultType.DENY);
    }
}
