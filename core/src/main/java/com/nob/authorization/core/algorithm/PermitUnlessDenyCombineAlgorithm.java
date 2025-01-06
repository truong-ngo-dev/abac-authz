package com.nob.authorization.core.algorithm;

import com.nob.authorization.core.context.EvaluationContext;
import com.nob.authorization.core.domain.Principle;
import com.nob.authorization.core.evaluation.EvaluationResult;

import java.util.List;

/**
 * This class implements the "Permit Unless Deny" combining algorithm,
 * which defaults to {@code PERMIT} unless a {@code DENY} result is encountered.
 *
 * @param <E> The type of principles being evaluated, extending the {@code Principle} interface.
 */
public class PermitUnlessDenyCombineAlgorithm<E extends Principle> implements CombineAlgorithm<E> {


    /**
     * Evaluates a list of principles against the provided evaluation context.
     * The "Permit Unless Deny" algorithm operates as follows:
     * <ul>
     *     <li>If any principle evaluates to {@code DENY}, the result is {@code DENY}.</li>
     *     <li>If no {@code DENY} is found, the result is {@code PERMIT}.</li>
     * </ul>
     *
     * @param principles The list of principles to evaluate.
     * @param context    The evaluation context providing additional data for evaluation.
     * @return An {@code EvaluationResult} representing the result of the evaluation:
     * <ul>
     *     <li>{@code DENY}: If any principle evaluates to {@code DENY}.</li>
     *     <li>{@code PERMIT}: If no principles evaluate to {@code DENY}.</li>
     * </ul>
     */
    @Override
    public EvaluationResult evaluate(List<E> principles, EvaluationContext context) {
        List<EvaluationResult> results = getListEvaluationResult(principles, context);
        return results.stream().anyMatch(EvaluationResult::isDeny) ?
                new EvaluationResult(EvaluationResult.EvaluationResultType.DENY) :
                new EvaluationResult(EvaluationResult.EvaluationResultType.PERMIT);
    }
}
