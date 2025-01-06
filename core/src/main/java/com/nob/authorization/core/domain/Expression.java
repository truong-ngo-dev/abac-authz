package com.nob.authorization.core.domain;

import lombok.Data;

import java.util.List;

/**
 * Represents an XACML Expression, which encapsulates elements such as {@code Target}, {@code AnyOf}, {@code AllOf},
 * {@code Match}, {@code Condition}, and {@code Apply} into a single object.
 * This class allows for flexible expression composition and evaluation in access control policies.
 *
 * An {@link Expression} can be a literal value or a composition of sub-expressions combined using logical operators.
 * It supports hierarchical structures for building complex conditions in XACML policies.
 *
 * @author Truong Ngo
 */
@Data
public class Expression {

    /**
     * A unique identifier for the expression.
     */
    private String id;

    /**
     * A description of the expression, providing additional context or details.
     */
    private String description;

    /**
     * The type of the expression, which can either be {@link Type#LITERAL} or {@link Type#COMPOSITION}.
     */
    private Type type;

    /**
     * The raw expression string for literal expressions.
     * This field is typically used when the expression type is {@link Type#LITERAL}.
     */
    private String expression;

    /**
     * A list of sub-expressions for composite expressions.
     * This field is used when the expression type is {@link Type#COMPOSITION}.
     */
    private List<Expression> subExpressions;

    /**
     * The combination type used for combining sub-expressions in a composite expression.
     * Can be {@link CombinationType#AND} or {@link CombinationType#OR}.
     */
    private CombinationType combinationType;

    /**
     * Determines whether this expression is a literal expression.
     *
     * @return {@code true} if the expression type is {@link Type#LITERAL}, otherwise {@code false}.
     */
    public boolean isLiteral() {
        return type == Type.LITERAL;
    }

    /**
     * Defines the possible logical combination types for sub-expressions.
     * <ul>
     *   <li>{@link #AND} - All sub-expressions must evaluate to true for the combination to be true.</li>
     *   <li>{@link #OR} - At least one sub-expression must evaluate to true for the combination to be true.</li>
     * </ul>
     */
    public enum CombinationType {
        AND,
        OR
    }

    /**
     * Defines the types of expressions.
     * <ul>
     *   <li>{@link #LITERAL} - A standalone value or condition.</li>
     *   <li>{@link #COMPOSITION} - A combination of sub-expressions.</li>
     * </ul>
     */
    public enum Type {
        LITERAL,
        COMPOSITION
    }
}
