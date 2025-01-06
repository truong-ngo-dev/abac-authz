package com.nob.authorization.core.evaluation;

import com.nob.authorization.core.algorithm.CombineAlgorithm;
import com.nob.authorization.core.algorithm.CombineAlgorithmFactory;
import com.nob.authorization.core.context.EvaluationContext;
import com.nob.authorization.core.domain.*;

import java.util.Objects;

/**
 * Provides evaluation logic for policy elements in an attribute-based access control (ABAC) system.
 * This class includes evaluators for {@link Rule}, {@link Policy}, and {@link PolicySet} objects.
 *
 * <p>Key responsibilities:
 * <ul>
 *   <li>Evaluating individual rules and applying their effects.</li>
 *   <li>Evaluating policies and policy sets using specified combination algorithms.</li>
 *   <li>Handling indeterminate results and ensuring proper target inheritance for rules.</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <blockquote><pre>
 *     EvaluationContext context = ...;
 *     AbstractPolicy policy = ...;
 *     EvaluationResult result = PolicyEvaluators.evaluate(context, policy);
 * </pre></blockquote>
 * </p>
 * @see Rule
 * @see AbstractPolicy
 * @see PolicySet
 */
public class PolicyEvaluators {

    /**
     * Evaluates a given policy element (rule or abstract policy) in the provided evaluation context.
     *
     * @param context The evaluation context containing necessary attributes and data.
     * @param policy The policy element to evaluate.
     * @param <E> The type of policy element.
     * @return An {@link EvaluationResult} indicating the result of the evaluation.
     */
    public static <E extends Principle> EvaluationResult evaluate(EvaluationContext context, E policy) {
        return (policy instanceof Rule rule) ?
                ruleEvaluator.evaluate(context, rule) :
                policyEvaluator.evaluate(context, (AbstractPolicy) policy);
    }

    /**
     * Interface for evaluating policy elements.
     *
     * @param <E> The type of the policy element to evaluate.
     */
    public interface PolicyElementEvaluator<E extends Principle> {

        /**
         * Evaluates a given policy element within the provided evaluation context.
         *
         * @param context The {@link EvaluationContext} containing the necessary attributes and data for evaluation.
         * @param policy The policy element to be evaluated.
         * @return An {@link EvaluationResult} representing the result of the evaluation, such as PERMIT, DENY,
         *         NOT_APPLICABLE, or INDETERMINATE.
         */
        EvaluationResult evaluate(EvaluationContext context, E policy);
    }

    /**
     * Evaluates a {@link Rule} in the given evaluation context.
     *
     * <p>Evaluation flow:
     * <ul>
     *   <li>If the rule's target matches and the condition matches, the rule's effect (PERMIT or DENY) is returned.</li>
     *   <li>If the condition does not match, the result is {@code NOT_APPLICABLE}.</li>
     *   <li>If the target or condition is indeterminate, the appropriate indeterminate cause is evaluated and returned.</li>
     * </ul>
     * </p>
     */
    public static PolicyElementEvaluator<Rule> ruleEvaluator = (context, rule) -> {
        ExpressionResult target = rule.getTarget() == null ?
                new ExpressionResult(ExpressionResult.ResultType.MATCH) :
                ExpressionEvaluators.evaluate(context, rule.getTarget());
        ExpressionResult condition = rule.getCondition() == null ?
                new ExpressionResult(ExpressionResult.ResultType.MATCH) :
                ExpressionEvaluators.evaluate(context, rule.getCondition());

        if (target.isMatch()) {
            if (condition.isMatch()) {
                return rule.getEffect().equals(Rule.Effect.PERMIT) ?
                        new EvaluationResult(EvaluationResult.EvaluationResultType.PERMIT) :
                        new EvaluationResult(EvaluationResult.EvaluationResultType.DENY);
            }
            if (condition.isNotMatch()) {
                return new EvaluationResult(EvaluationResult.EvaluationResultType.NOT_APPLICABLE);
            }
            return CombineAlgorithm.evaluateRuleIfIndeterminate(rule, condition, "Condition");
        } else if (target.isNotMatch()) {
            return new EvaluationResult(EvaluationResult.EvaluationResultType.NOT_APPLICABLE);
        } else {
            return CombineAlgorithm.evaluateRuleIfIndeterminate(rule, target, "Target");
        }
    };

    /**
     * Evaluates an {@link AbstractPolicy} in the given evaluation context.
     *
     * <p>Evaluation flow:
     * <ul>
     *   <li>If the policy's target does not match, the result is {@code NOT_APPLICABLE}.</li>
     *   <li>If the target matches, the policy's rules or child policies are evaluated using the appropriate combine algorithm.</li>
     *   <li>If the target is indeterminate, the result is evaluated based on the indeterminate cause.</li>
     * </ul>
     * </p>
     */
    public static PolicyElementEvaluator<AbstractPolicy> policyEvaluator = (context, policy) -> {
        ExpressionResult target = ExpressionEvaluators.evaluate(context, policy.getTarget());
        if (target.isNotMatch()) {
            return new EvaluationResult(EvaluationResult.EvaluationResultType.NOT_APPLICABLE);
        }

        EvaluationResult combineResult;
        String element;
        if (policy instanceof PolicySet) {
            element = "PolicySet";
            CombineAlgorithm<AbstractPolicy> combineAlgorithm = CombineAlgorithmFactory.from(policy.getCombineAlgorithmName(), AbstractPolicy.class);
            combineResult = combineAlgorithm.evaluate(((PolicySet) policy).getPolicies(), context);
        } else {
            element = "Policy";
            inheritTargetIfRuleOmitted((Policy) policy);
            CombineAlgorithm<Rule> combineAlgorithm = CombineAlgorithmFactory.from(policy.getCombineAlgorithmName(), Rule.class);
            combineResult = combineAlgorithm.evaluate(((Policy) policy).getRules(), context);
        }

        if (target.isMatch()) {
            if (combineResult.isIndeterminate()) {
                combineResult.getIndeterminateCause().buildDefaultDescription(element, policy.getId());
            }
            return combineResult;
        }
        return CombineAlgorithm.evaluatePolicyIfIndeterminateTarget(combineResult, policy, target);
    };

    /**
     * Inherits the target of a {@link Policy} for rules that do not specify a target.
     *
     * @param policy The policy whose rules will inherit the target.
     */
    public static void inheritTargetIfRuleOmitted(Policy policy) {
        policy.getRules().forEach(r -> {
            if (Objects.isNull(r.getTarget())) {
                r.setTarget(policy.getTarget());
            }
        });
    }
}
