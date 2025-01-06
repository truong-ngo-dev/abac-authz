package com.nob.authorization.core.domain;

import com.nob.authorization.core.context.EvaluationContext;
import com.nob.authorization.core.evaluation.ExpressionEvaluators;
import com.nob.authorization.core.evaluation.ExpressionResult;

/**
 * Marker interface for policy elements such as {@code Rule}, {@code Policy}, and {@code PolicySet}.
 * This interface is intended to be implemented by entities that define access control rules
 * and conditions within an authorization system.
 *
 * @author Truong Ngo
 */
public interface Principle {

    /**
     * Gets the unique identifier for this policy element (e.g., Rule, Policy, PolicySet).
     *
     * @return The unique identifier of the policy element.
     */
    String getId();

    /**
     * Gets a description for this policy element (e.g., Rule, Policy, PolicySet).
     *
     * @return The description of the policy element.
     */
    String getDescription();

    /**
     * Gets the target expression that defines the conditions under which the policy element is applicable.
     *
     * @return The target expression associated with the policy element.
     */
    Expression getTarget();

    /**
     * Evaluates if the policy element (such as {@link Rule} or {@link AbstractPolicy}) is applicable
     * based on the provided {@link EvaluationContext}. This method checks the target expression to
     * determine if it applies to the current context.
     *
     * @param context The context in which the policy element's applicability is evaluated.
     * @return The result of the applicability evaluation, indicating whether the target expression is
     *         applicable or not.
     */
    default ExpressionResult isApplicable(EvaluationContext context) {
        Expression target = getTarget();
        if (target == null) {
            return new ExpressionResult(ExpressionResult.ResultType.MATCH);  // If no target expression is defined, the principle is always considered applicable
        }
        ExpressionResult targetApplicable = ExpressionEvaluators.evaluate(context, target); // Evaluate the target expression for applicability in the current context
        if (!targetApplicable.isIndeterminate()) {
            return targetApplicable; // If the target expression evaluation is not indeterminate, return the result
        }
        targetApplicable.getIndeterminateCause().buildDefaultDescription("Target", target.getId()); // If the result is indeterminate, build a default description for the cause
        return targetApplicable;
    }
}
