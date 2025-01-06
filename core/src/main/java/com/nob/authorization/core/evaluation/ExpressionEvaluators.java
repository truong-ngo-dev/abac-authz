package com.nob.authorization.core.evaluation;

import com.nob.authorization.core.context.EvaluationContext;
import com.nob.authorization.core.domain.Expression;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;
import java.util.Objects;

/**
 * Utility class for evaluating logical expressions in the context of decision-making systems.
 * It supports evaluating literal and composite expressions using custom evaluators.
 *
 * @author Truong Ngo
 */
public class ExpressionEvaluators {

    /**
     * Evaluates the given expression against the provided evaluation context.
     * Supports literal and composite expressions with AND/OR combination types.
     *
     * @param context    The evaluation context providing additional data for the evaluation.
     * @param expression The expression to evaluate.
     * @return The result of the evaluation as an {@code ExpressionResult}.
     */
    public static ExpressionResult evaluate(EvaluationContext context, Expression expression) {
        if (expression.isLiteral()) {
            return literalEvaluator.evaluate(context, expression);
        } else {
            return switch (expression.getCombinationType()) {
                case AND -> conjunctionEvaluator.evaluate(context, expression.getSubExpressions());
                case OR -> disConjunctionEvaluator.evaluate(context, expression.getSubExpressions());
            };
        }
    }

    // ***** Literal evaluator *****

    /**
     * Functional interface for evaluating literal expressions.
     */
    @FunctionalInterface
    public interface ExpressionEvaluator {

        /**
         * Evaluates an expression within the given context.
         *
         * @param context    The evaluation context.
         * @param expression The expression to evaluate.
         * @return The result of the evaluation as an {@code ExpressionResult}.
         */
        ExpressionResult evaluate(EvaluationContext context, Expression expression);

    }

    /**
     * Evaluator for literal expressions.
     * Parses and evaluates the expression using Spring Expression Language (SpEL).
     */
    public static ExpressionEvaluator literalEvaluator =  (context, expression) -> {
        String expr = expression.getExpression();
        if (Objects.isNull(expr)) {
            IndeterminateCause cause = new IndeterminateCause();
            cause.setCode(IndeterminateCause.IndeterminateCauseType.SYNTAX_ERROR);
            cause.setDescription("Expression is null");
            return new ExpressionResult(ExpressionResult.ResultType.INDETERMINATE, cause);
        }
        ExpressionResult result = new ExpressionResult();
        IndeterminateCause cause = null;
        try {
            ExpressionParser parser = new SpelExpressionParser();
            org.springframework.expression.Expression exp = parser.parseExpression(expr);
            Boolean expValue = exp.getValue(context, Boolean.class);
            ExpressionResult.ResultType resultType = Boolean.TRUE.equals(expValue) ?
                    ExpressionResult.ResultType.MATCH :
                    ExpressionResult.ResultType.NO_MATCH;
            result.setResultType(resultType);
        } catch (ParseException | EvaluationException | IllegalAccessError exception) {
            result.setResultType(ExpressionResult.ResultType.INDETERMINATE);
            cause = new IndeterminateCause(IndeterminateCause.IndeterminateCauseType.SYNTAX_ERROR);
        }
        if (Objects.nonNull(cause)) {
            result.setIndeterminateCause(cause);
        }
        return result;
    };

    // ***** Composite evaluator *****

    /**
     * Functional interface for evaluating composite expressions.
     */
    @FunctionalInterface
    public interface CompositeExpressionEvaluator {

        /**
         * Evaluates a composite expression consisting of multiple sub-expressions.
         *
         * @param context     The evaluation context.
         * @param expressions The list of sub-expressions to evaluate.
         * @return The result of the evaluation as an {@code ExpressionResult}.
         */
        default ExpressionResult evaluate(EvaluationContext context, List<Expression> expressions) {
            if (Objects.isNull(expressions) || expressions.isEmpty()) {
                IndeterminateCause cause = new IndeterminateCause();
                cause.setCode(IndeterminateCause.IndeterminateCauseType.SYNTAX_ERROR);
                cause.setDescription("Sub expression is empty");
                return new ExpressionResult(ExpressionResult.ResultType.INDETERMINATE, cause);
            } else {
                return evaluateInternal(context, expressions);
            }
        }

        /**
         * Internal evaluation logic for composite expressions.
         *
         * @param context     The evaluation context.
         * @param expressions The list of sub-expressions to evaluate.
         * @return The result of the evaluation as an {@code ExpressionResult}.
         */
        ExpressionResult evaluateInternal(EvaluationContext context, List<Expression> expressions);
    }

    /**
     * Evaluator for composite expressions with an AND combination type.
     * <ul>
     *   <li>All sub-expressions must match for the result to be <b>MATCH</b>.</li>
     *   <li>If any sub-expression is <b>NO_MATCH</b>, the result is <b>NO_MATCH</b>.</li>
     *   <li>If any sub-expression is indeterminate, the result is <b>INDETERMINATE</b>.</li>
     * </ul>
     */
    public static CompositeExpressionEvaluator conjunctionEvaluator = (context, expressions) -> {
        boolean atLeastOneIndeterminate = false;
        List<ExpressionResult> expressionResults = expressions.stream().map(r -> ExpressionEvaluators.evaluate(context, r)).toList();
        List<IndeterminateCause> indeterminateCauses = expressionResults.stream()
                .filter(ExpressionResult::isIndeterminate)
                .map(ExpressionResult::getIndeterminateCause)
                .toList();

        for (ExpressionResult r : expressionResults) {
            if (r.isNotMatch()) {
                return new ExpressionResult(ExpressionResult.ResultType.NO_MATCH);
            }
            if (r.isIndeterminate()) {
                atLeastOneIndeterminate = true;
            }
        }

        if (atLeastOneIndeterminate) {
            IndeterminateCause cause = new IndeterminateCause(IndeterminateCause.IndeterminateCauseType.PROCESSING_ERROR, indeterminateCauses);
            return new ExpressionResult(ExpressionResult.ResultType.INDETERMINATE, cause);
        } else {
            return new ExpressionResult(ExpressionResult.ResultType.MATCH);
        }
    };

    /**
     * Evaluator for composite expressions with an OR combination type.
     * <ul>
     *   <li>If any sub-expression matches, the result is <b>MATCH</b>.</li>
     *   <li>If all sub-expressions are <b>NO_MATCH</b>, the result is <b>NO_MATCH</b>.</li>
     *   <li>If any sub-expression is indeterminate, the result is <b>INDETERMINATE</b>.</li>
     * </ul>
     */
    public static CompositeExpressionEvaluator disConjunctionEvaluator = (context, expressions) -> {
        boolean atLeastOneIndeterminate = false;
        List<ExpressionResult> expressionResults = expressions.stream().map(r -> ExpressionEvaluators.evaluate(context, r)).toList();
        List<IndeterminateCause> indeterminateCauses = expressionResults.stream().
                filter(ExpressionResult::isIndeterminate).
                map(ExpressionResult::getIndeterminateCause).
                toList();

        for (ExpressionResult r : expressionResults) {
            if (r.isMatch()) {
                return new ExpressionResult(ExpressionResult.ResultType.MATCH);
            }
            if (r.isIndeterminate()) {
                atLeastOneIndeterminate = true;
            }
        }

        if (atLeastOneIndeterminate) {
            IndeterminateCause cause = new IndeterminateCause(IndeterminateCause.IndeterminateCauseType.PROCESSING_ERROR, indeterminateCauses);
            return new ExpressionResult(ExpressionResult.ResultType.INDETERMINATE, cause);
        } else {
            return new ExpressionResult(ExpressionResult.ResultType.NO_MATCH);
        }
    };
}
