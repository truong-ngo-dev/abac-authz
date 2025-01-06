package com.nob.authorization.core.algorithm;

import com.nob.authorization.core.context.EvaluationContext;
import com.nob.authorization.core.domain.AbstractPolicy;
import com.nob.authorization.core.domain.Principle;
import com.nob.authorization.core.evaluation.EvaluationResult;
import com.nob.authorization.core.evaluation.ExpressionResult;
import com.nob.authorization.core.evaluation.IndeterminateCause;
import com.nob.authorization.core.evaluation.PolicyEvaluators;

import java.util.List;

/**
 * This class implements the "Only One Applicable" combining algorithm,
 * which ensures that exactly one principle is applicable in a given context.
 * If more than one principle is applicable or an indeterminate result is encountered,
 * the evaluation returns an INDETERMINATE result with detailed causes.
 *
 * @param <E> The type of principles being evaluated, extending the {@code Principle} interface.
 * @author Truong Ngo
 */
public class OnlyOneApplicableCombineAlgorithm<E extends Principle> implements CombineAlgorithm<E> {


    /**
     * Evaluates a list of principles against the provided evaluation context.
     * The "Only One Applicable" algorithm operates as follows:
     * <ul>
     *     <li>Evaluates each principle in sequence.</li>
     *     <li>If a principle is indeterminate, returns an {@code INDETERMINATE} result with the associated cause.</li>
     *     <li>If more than one principle matches the context, returns an {@code INDETERMINATE} result
     *         indicating a processing error due to multiple applicable principles.</li>
     *     <li>If exactly one principle matches the context, evaluates and returns the result of that principle.</li>
     *     <li>If no principles are applicable, returns a {@code NOT_APPLICABLE} result.</li>
     * </ul>
     *
     * @param principles The list of principles to evaluate.
     * @param context    The evaluation context providing additional data for evaluation.
     * @return An {@code EvaluationResult} with one of the following:
     * <ul>
     *     <li>{@code PERMIT} or {@code DENY}: The result of the single applicable principle.</li>
     *     <li>{@code INDETERMINATE}: If an indeterminate result or multiple applicable principles are detected.</li>
     *     <li>{@code NOT_APPLICABLE}: If no principles are applicable.</li>
     * </ul>
     */
    @Override
    public EvaluationResult evaluate(List<E> principles, EvaluationContext context) {
        boolean atLeastOne = false;
        E selectedPolicy = null;
        for (E policy : principles) {
            ExpressionResult applicableResult = policy.isApplicable(context);
            if (applicableResult.isIndeterminate()) {
                return buildIndeterminateResult(policy, List.of(applicableResult.getIndeterminateCause()));
            }
            if (applicableResult.isMatch()) {
                if (atLeastOne) {
                    return buildIndeterminateResult(policy, List.of(applicableResult.getIndeterminateCause()));
                } else {
                    atLeastOne = true;
                    selectedPolicy = policy;
                }
            }
        }
        if (atLeastOne) {
            return PolicyEvaluators.evaluate(context, selectedPolicy);
        } else {
            return new EvaluationResult(EvaluationResult.EvaluationResultType.NOT_APPLICABLE);
        }
    }

    private EvaluationResult buildIndeterminateResult(Principle principle, List<IndeterminateCause> subCauses) {
        IndeterminateCause cause = new IndeterminateCause(IndeterminateCause.IndeterminateCauseType.PROCESSING_ERROR, subCauses);
        String element = principle instanceof AbstractPolicy ? "Policy" : "PolicySet";
        cause.buildDefaultDescription(element, principle.getId());
        return new EvaluationResult(EvaluationResult.EvaluationResultType.INDETERMINATE, cause);
    }
}
