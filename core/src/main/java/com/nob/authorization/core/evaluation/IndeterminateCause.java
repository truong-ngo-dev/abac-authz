package com.nob.authorization.core.evaluation;

import com.nob.authorization.core.domain.Expression;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the cause of an indeterminate result in evaluation processes.
 * This class provides details about the type, description, and additional context of the indeterminate cause.
 * It also supports hierarchical structuring of causes through sub-causes.
 *
 * <p>Key features:
 * <ul>
 *   <li>Encapsulates the type of indeterminate cause, such as syntax or processing errors.</li>
 *   <li>Provides a description and optional content for additional context.</li>
 *   <li>Supports nested sub-causes to build a cause tree for complex scenarios.</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:
 * <blockquote><pre>
 *     IndeterminateCause cause = new IndeterminateCause(IndeterminateCauseType.SYNTAX_ERROR);
 *     cause.buildDefaultDescription("Policy", "12345");
 * </pre></blockquote>
 * </p>
 *
 * <p>Static utility fields:
 * <ul>
 *   <li>{@link #DEFAULT_DESC_FORMAT} - Format for default descriptions.</li>
 * </ul>
 * </p>
 *
 * @see Expression
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndeterminateCause {

    /**
     * Default format for cause descriptions.
     * Includes the type of element, its ID, and the cause code.
     */
    public static final String DEFAULT_DESC_FORMAT = "%s with id %s has %s";

    /**
     * Enumeration of indeterminate cause types.
     */
    public enum IndeterminateCauseType {
        /** Represents an error due to invalid syntax. */
        SYNTAX_ERROR,
        /** Represents an error occurring during processing. */
        PROCESSING_ERROR
    }

    /**
     * The type of the indeterminate cause.
     * Can be one of {@link IndeterminateCauseType#SYNTAX_ERROR} or {@link IndeterminateCauseType#PROCESSING_ERROR}.
     */
    private IndeterminateCauseType code;

    /**
     * A short description of the cause.
     * This description is typically generated using {@link #buildDefaultDescription(String, String)}.
     */
    private String description;

    /**
     * Additional content providing context for the cause.
     * For example, this might be the expression content that caused the indeterminate result.
     */
    private String content;

    /**
     * A list of sub-causes, enabling the creation of a hierarchical tree of causes.
     */
    private List<IndeterminateCause> subIndeterminateCauses;

    /**
     * Constructs an {@code IndeterminateCause} with a specified type.
     *
     * @param code The type of the indeterminate cause.
     */
    public IndeterminateCause(IndeterminateCauseType code) {
        this.code = code;
    }

    /**
     * Constructs an {@code IndeterminateCause} with a specified type and sub-causes.
     *
     * @param code The type of the indeterminate cause.
     * @param subIndeterminateCauses A list of sub-causes.
     */
    public IndeterminateCause(IndeterminateCauseType code, List<IndeterminateCause> subIndeterminateCauses) {
        this.code = code;
        this.subIndeterminateCauses = subIndeterminateCauses;
    }

    /**
     * Builds a default description for the cause using the element type and ID.
     * The description follows the format defined by {@link #DEFAULT_DESC_FORMAT}.
     *
     * @param element The type of the evaluated element (e.g., "Policy" or "Rule").
     * @param id The ID of the evaluated element.
     */
    public void buildDefaultDescription(String element, String id) {
        description = String.format(DEFAULT_DESC_FORMAT, element, id, code.name().toLowerCase());
    }

    /**
     * Builds default content based on an {@code Expression}.
     * The content is typically the expression's string representation.
     *
     * @param expression The expression that caused the indeterminate result.
     */
    public void buildDefaultContent(Expression expression) {
        content = expression.getExpression();
    }
}
