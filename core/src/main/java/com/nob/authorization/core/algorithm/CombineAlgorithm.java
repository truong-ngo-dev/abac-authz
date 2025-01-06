package com.nob.authorization.core.algorithm;

import com.nob.authorization.core.context.EvaluationContext;
import com.nob.authorization.core.domain.*;
import com.nob.authorization.core.evaluation.EvaluationResult;
import com.nob.authorization.core.evaluation.ExpressionResult;
import com.nob.authorization.core.evaluation.IndeterminateCause;
import com.nob.authorization.core.evaluation.PolicyEvaluators;

import java.util.List;

/**
 * Defines a combination algorithm for {@link Principle} elements.
 * This interface is used to evaluate and combine the results of multiple {@link Rule} or {@link AbstractPolicy}
 * elements based on a specified algorithm, such as in the case of policy or rule combination.
 *
 * @param <E> The type of {@link Principle} (e.g., {@link Rule} or {@link AbstractPolicy}).
 */
public interface CombineAlgorithm<E extends Principle> {

    /**
     * Evaluates a list of {@link Principle} elements, which can include {@link Rule} and {@link AbstractPolicy},
     * within the given {@link EvaluationContext}.
     *
     * @param principles The list of principles to evaluate.
     * @param context The evaluation context.
     * @return The combined {@link EvaluationResult} for the provided principles.
     */
    EvaluationResult evaluate(List<E> principles, EvaluationContext context);


    /**
     * Retrieves a list of {@link EvaluationResult} objects by evaluating each principle in the list
     * within the given {@link EvaluationContext}.
     *
     * @param principles The list of principles to evaluate.
     * @param context The evaluation context.
     * @return A list of {@link EvaluationResult} objects for the provided principles.
     */
    default List<EvaluationResult> getListEvaluationResult(List<E> principles, EvaluationContext context) {
        return principles.stream().map(p -> PolicyEvaluators.evaluate(context, p)).toList();
    }


    /**
     * Evaluates the result of a {@link Policy} or {@link PolicySet} when the target evaluation is indeterminate.
     *
     * @param combineResult The combined result of the policies or policy set.
     * @param principle The principle being evaluated.
     * @param target The target expression result.
     * @return The {@link EvaluationResult} reflecting the indeterminate evaluation of the principle.
     */
    static EvaluationResult evaluatePolicyIfIndeterminateTarget(EvaluationResult combineResult, Principle principle, ExpressionResult target) {
        EvaluationResult.EvaluationResultType resultType = switch (combineResult.getResult()) {
            case NOT_APPLICABLE -> EvaluationResult.EvaluationResultType.NOT_APPLICABLE;
            case PERMIT, INDETERMINATE_P -> EvaluationResult.EvaluationResultType.INDETERMINATE_P;
            case DENY, INDETERMINATE_D -> EvaluationResult.EvaluationResultType.INDETERMINATE_D;
            case INDETERMINATE_DP, INDETERMINATE -> EvaluationResult.EvaluationResultType.INDETERMINATE_DP;
        };

        target.getIndeterminateCause().buildDefaultDescription("Target", principle.getTarget().getId());
        IndeterminateCause cause = new IndeterminateCause(IndeterminateCause.IndeterminateCauseType.PROCESSING_ERROR, List.of(target.getIndeterminateCause()));

        return new EvaluationResult(resultType, cause);
    }

    /**
     * Evaluates the result of a {@link Rule} when its target or condition is indeterminate.
     *
     * @param rule The rule being evaluated.
     * @param expressionResult The evaluation result of the target or condition expression.
     * @param element The element being evaluated ("Target" or "Condition").
     * @return The {@link EvaluationResult} reflecting the indeterminate evaluation of the rule.
     */
    static EvaluationResult evaluateRuleIfIndeterminate(Rule rule, ExpressionResult expressionResult, String element) {
        String id = element.equals("Target") ? rule.getTarget().getId() : rule.getCondition().getId();
        expressionResult.getIndeterminateCause().buildDefaultDescription(element, id);
        IndeterminateCause cause = new IndeterminateCause(IndeterminateCause.IndeterminateCauseType.PROCESSING_ERROR);
        cause.buildDefaultDescription("Rule", rule.getId());
        cause.setSubIndeterminateCauses(List.of(expressionResult.getIndeterminateCause()));
        EvaluationResult.EvaluationResultType resultType = rule.getEffect().equals(Rule.Effect.PERMIT) ?
                EvaluationResult.EvaluationResultType.INDETERMINATE_P :
                EvaluationResult.EvaluationResultType.INDETERMINATE_D;
        return new EvaluationResult(resultType, cause);
    }

    /**
     * Retrieves the default indeterminate evaluation cause for a list of evaluation results.
     *
     * @param results A list of {@link EvaluationResult} objects to analyze.
     * @return An {@link IndeterminateCause} representing the combined indeterminate causes of the results.
     */
    default IndeterminateCause getIndeterminateEvaluationCause(List<EvaluationResult> results) {
        List<IndeterminateCause> indeterminate = results.stream()
                .filter(EvaluationResult::isIndeterminate)
                .map(EvaluationResult::getIndeterminateCause)
                .toList();
        return new IndeterminateCause(IndeterminateCause.IndeterminateCauseType.PROCESSING_ERROR, indeterminate);
    }
}
