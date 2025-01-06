package com.nob.authorization.core.algorithm;

import com.nob.authorization.core.context.EvaluationContext;
import com.nob.authorization.core.domain.Principle;
import com.nob.authorization.core.evaluation.EvaluationResult;
import com.nob.authorization.core.evaluation.IndeterminateCause;

import java.util.List;

/**
 * This class implements the "First Applicable" combining algorithm,
 * which evaluates a list of principles sequentially and returns the result
 * of the first applicable principle (PERMIT, DENY, or INDETERMINATE).
 * If no principles are applicable, the result is NOT_APPLICABLE.
 *
 * @param <E> The type of principles being evaluated, extending the {@code Principle} interface.
 * @author Truong Ngo
 */
public class FirstApplicableCombineAlgorithm<E extends Principle> implements CombineAlgorithm<E> {

    /**
     * Evaluates a list of principles against the provided evaluation context.
     *
     * <p>The "First Applicable" algorithm operates as follows:</p>
     * <ul>
     *     <li>Evaluates each principle in sequence.</li>
     *     <li>If a principle evaluates to PERMIT or DENY, that result is returned.</li>
     *     <li>If a principle evaluates to INDETERMINATE, an {@code INDETERMINATE} result is returned,
     *         including an aggregated list of causes from all INDETERMINATE evaluations up to that point.</li>
     *     <li>If no principles are applicable, the result is {@code NOT_APPLICABLE}.</li>
     * </ul>
     *
     * @param principles The list of principles to evaluate.
     * @param context    The evaluation context providing additional data for evaluation.
     * @return An {@code EvaluationResult} representing the first applicable result:
     * <ul>
     *     <li>{@code PERMIT} or {@code DENY}: The result of the first applicable principle.</li>
     *     <li>{@code INDETERMINATE}: If the first applicable principle is indeterminate, including its causes.</li>
     *     <li>{@code NOT_APPLICABLE}: If no principles are applicable.</li>
     * </ul>
     */
    @Override
    public EvaluationResult evaluate(List<E> principles, EvaluationContext context) {
        List<EvaluationResult> results = getListEvaluationResult(principles, context);

        List<IndeterminateCause> indeterminate = results.stream()
                .filter(EvaluationResult::isIndeterminate)
                .map(EvaluationResult::getIndeterminateCause)
                .toList();
        IndeterminateCause cause = new IndeterminateCause(IndeterminateCause.IndeterminateCauseType.PROCESSING_ERROR, indeterminate);

        for (EvaluationResult result : results) {
            if (result.isPermit() || result.isDeny() || result.isIndeterminate()) {
                return result.isIndeterminate() ?
                        new EvaluationResult(EvaluationResult.EvaluationResultType.INDETERMINATE, cause) :
                        new EvaluationResult(result.getResult());
            }
        }
        return new EvaluationResult(EvaluationResult.EvaluationResultType.NOT_APPLICABLE);
    }
}
