package com.nob.authorization.core.algorithm;

import com.nob.authorization.core.context.EvaluationContext;
import com.nob.authorization.core.domain.Principle;
import com.nob.authorization.core.evaluation.EvaluationResult;
import com.nob.authorization.core.evaluation.IndeterminateCause;

import java.util.List;

/**
 * This class implements the "Permit Overrides" combining algorithm,
 * which prioritizes PERMIT results over DENY, INDETERMINATE, and NOT_APPLICABLE results.
 * If no PERMIT is found, it evaluates other results based on specific conditions.
 *
 * @param <E> The type of principles being evaluated, extending the {@code Principle} interface.
 * @author Truong Ngo
 */
public class PermitOverridesCombineAlgorithm<E extends Principle> implements CombineAlgorithm<E> {


    /**
     * Evaluates a list of principles against the provided evaluation context.
     * The "Permit Overrides" algorithm operates as follows:
     * <ul>
     *     <li>If any principle evaluates to {@code PERMIT}, the result is {@code PERMIT}.</li>
     *     <li>If there is no PERMIT, the result is determined based on the presence of:
     *         <ul>
     *             <li>{@code INDETERMINATE_DP}: If at least one result is indeterminate with both permit and deny reasons.</li>
     *             <li>{@code INDETERMINATE_P}: If at least one result is indeterminate with permit reasons only,
     *                 and no {@code DENY} or {@code INDETERMINATE_D} exists.</li>
     *             <li>{@code INDETERMINATE_D}: If at least one result is indeterminate with deny reasons only
     *                 and no {@code PERMIT} or {@code INDETERMINATE_P} exists.</li>
     *             <li>{@code DENY}: If at least one result is {@code DENY} and no higher-priority results apply.</li>
     *         </ul>
     *     </li>
     *     <li>If no applicable results are found, the result is {@code NOT_APPLICABLE}.</li>
     * </ul>
     *
     * @param principles The list of principles to evaluate.
     * @param context    The evaluation context providing additional data for evaluation.
     * @return An {@code EvaluationResult} based on the "Permit Overrides" algorithm:
     * <ul>
     *     <li>{@code PERMIT}: If any principle evaluates to PERMIT.</li>
     *     <li>{@code INDETERMINATE_DP}: If conflicting permit and deny indeterminate results exist.</li>
     *     <li>{@code INDETERMINATE_P}: If indeterminate results with permit reasons exist without conflicts.</li>
     *     <li>{@code INDETERMINATE_D}: If indeterminate results with deny reasons exist without conflicts.</li>
     *     <li>{@code DENY}: If any principle evaluates to DENY and no higher-priority results apply.</li>
     *     <li>{@code NOT_APPLICABLE}: If no principles are applicable.</li>
     * </ul>
     */
    @Override
    public EvaluationResult evaluate(List<E> principles, EvaluationContext context) {
        List<EvaluationResult> results = getListEvaluationResult(principles, context);

        boolean atLeastOneInD = false,
                atLeastOneInP = false,
                atLeastOneInDP = false,
                atLeastOneDeny = false;

        for (EvaluationResult result : results) {
            if (result.isDeny()) {
                atLeastOneDeny = true;
            }
            if (result.isPermit()) {
                return new EvaluationResult(EvaluationResult.EvaluationResultType.PERMIT);
            }
            if (result.getResult().equals(EvaluationResult.EvaluationResultType.INDETERMINATE_D)) {
                atLeastOneInD = true;
            }
            if (result.getResult().equals(EvaluationResult.EvaluationResultType.INDETERMINATE_P)) {
                atLeastOneInP = true;
            }
            if (result.getResult().equals(EvaluationResult.EvaluationResultType.INDETERMINATE_DP)) {
                atLeastOneInDP = true;
            }
        }

        IndeterminateCause cause = getIndeterminateEvaluationCause(results);

        if (atLeastOneInDP) {
            return new EvaluationResult(EvaluationResult.EvaluationResultType.INDETERMINATE_DP, cause);
        }
        if (atLeastOneInP && (atLeastOneInD || atLeastOneDeny)) {
            return new EvaluationResult(EvaluationResult.EvaluationResultType.INDETERMINATE_DP, cause);
        }
        if (atLeastOneInP) {
            return new EvaluationResult(EvaluationResult.EvaluationResultType.INDETERMINATE_P, cause);
        }
        if (atLeastOneDeny) {
            return new EvaluationResult(EvaluationResult.EvaluationResultType.DENY);
        }
        if (atLeastOneInD) {
            return new EvaluationResult(EvaluationResult.EvaluationResultType.INDETERMINATE_D, cause);
        }
        return new EvaluationResult(EvaluationResult.EvaluationResultType.NOT_APPLICABLE);
    }
}
