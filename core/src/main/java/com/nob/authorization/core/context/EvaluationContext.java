package com.nob.authorization.core.context;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the context for evaluating access control decisions.
 * This context includes information about the subject, object, action, and environment
 * to perform Attribute-Based Access Control (ABAC) evaluation.
 * @author Truong Ngo
 */
@Data
@AllArgsConstructor
public class EvaluationContext {

    /**
     * The subject involved in the evaluation, typically representing a user or an entity.
     */
    public Subject subject;

    /**
     * The object that the subject is trying to access or perform an action on.
     */
    public Resource object;

    /**
     * The action that the subject intends to perform on the object.
     */
    public Action action;

    /**
     * The environment in which the evaluation is taking place, which may contain external factors
     * such as time, location, or other context.
     */
    public Environment environment;

}
