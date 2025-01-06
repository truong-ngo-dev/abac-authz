package com.nob.authorization.core.algorithm;

import com.nob.authorization.core.context.EvaluationContext;
import com.nob.authorization.core.domain.AbstractPolicy;
import com.nob.authorization.core.domain.Principle;
import com.nob.authorization.core.domain.Rule;
import com.nob.authorization.core.evaluation.EvaluationResult;
import com.nob.authorization.core.evaluation.IndeterminateCause;

import java.util.List;

/**
 * This class implements the "Deny Overrides" combining algorithm,
 * which prioritizes DENY results over PERMIT, INDETERMINATE, and NOT_APPLICABLE results.
 * If no DENY is found, it evaluates other results based on specific conditions.
 *
 * @param <E> The type of principles being evaluated, extending the {@code Principle} interface.
 * @author Truong Ngo
 */
public class DenyOverridesCombineAlgorithm<E extends Principle> implements CombineAlgorithm<E> {


    /**
     * Evaluates a list of principles against the provided evaluation context.
     * The "Deny Overrides" algorithm operates as follows:
     * <ul>
     *     <li>If any principle evaluates to {@code DENY}, the result is {@code DENY}.</li>
     *     <li>If no DENY is found, the result is determined based on the presence of:
     *         <ul>
     *             <li>{@code INDETERMINATE_DP}: If at least one result is indeterminate with both permit and deny reasons.</li>
     *             <li>{@code INDETERMINATE_D}: If at least one result is indeterminate with deny reasons only,
     *                 and no {@code PERMIT} or {@code INDETERMINATE_P} exists.</li>
     *             <li>{@code INDETERMINATE_P}: If at least one result is indeterminate with permit reasons only
     *                 and no higher-priority results apply.</li>
     *             <li>{@code PERMIT}: If at least one principle evaluates to PERMIT and no higher-priority results apply.</li>
     *         </ul>
     *     </li>
     *     <li>If no applicable results are found, the result is {@code NOT_APPLICABLE}.</li>
     * </ul>
     *
     * @param principles The list of principles to evaluate.
     * @param context    The evaluation context providing additional data for evaluation.
     * @return An {@code EvaluationResult} based on the "Deny Overrides" algorithm:
     * <ul>
     *     <li>{@code DENY}: If any principle evaluates to DENY.</li>
     *     <li>{@code INDETERMINATE_DP}: If conflicting permit and deny indeterminate results exist.</li>
     *     <li>{@code INDETERMINATE_D}: If indeterminate results with deny reasons exist without conflicts.</li>
     *     <li>{@code INDETERMINATE_P}: If indeterminate results with permit reasons exist without conflicts.</li>
     *     <li>{@code PERMIT}: If any principle evaluates to PERMIT and no higher-priority results apply.</li>
     *     <li>{@code NOT_APPLICABLE}: If no principles are applicable.</li>
     * </ul>
     */
    @Override
    public EvaluationResult evaluate(List<E> principles, EvaluationContext context) {

        List<EvaluationResult> results = getListEvaluationResult(principles, context);

        boolean atLeastOneInD = false;  // Indicates if there's an INDETERMINATE_D result
        boolean atLeastOneInP = false;  // Indicates if there's an INDETERMINATE_P result
        boolean atLeastOneInDP = false; // Indicates if there's an INDETERMINATE_DP result
        boolean atLeastOnePermit = false; // Indicates if there's at least one PERMIT result

        for (EvaluationResult result : results) {
            if (result.isDeny()) {
                return new EvaluationResult(EvaluationResult.EvaluationResultType.DENY); // If a Deny is found, return Deny immediately
            }
            if (result.isPermit()) {
                atLeastOnePermit = true;
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

        IndeterminateCause cause = getIndeterminateEvaluationCause(results); // Determine the indeterminate cause if needed

        // Resolve results based on the combination logic
        if (atLeastOneInDP) {
            return new EvaluationResult(EvaluationResult.EvaluationResultType.INDETERMINATE_DP, cause);
        }
        if (atLeastOneInD && (atLeastOneInP || atLeastOnePermit)) {
            return new EvaluationResult(EvaluationResult.EvaluationResultType.INDETERMINATE_DP, cause);
        }
        if (atLeastOneInD) {
            return new EvaluationResult(EvaluationResult.EvaluationResultType.INDETERMINATE_D, cause);
        }
        if (atLeastOnePermit) {
            return new EvaluationResult(EvaluationResult.EvaluationResultType.PERMIT);
        }
        if (atLeastOneInP) {
            return new EvaluationResult(EvaluationResult.EvaluationResultType.INDETERMINATE_P, cause);
        }

        // If no applicable results exist, return Not Applicable
        return new EvaluationResult(EvaluationResult.EvaluationResultType.NOT_APPLICABLE);
    }
}