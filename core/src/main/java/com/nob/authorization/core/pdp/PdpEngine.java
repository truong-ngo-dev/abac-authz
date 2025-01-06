package com.nob.authorization.core.pdp;

import com.nob.authorization.core.context.EvaluationContext;
import com.nob.authorization.core.domain.AbstractPolicy;
import com.nob.authorization.core.evaluation.EvaluationResult;
import com.nob.authorization.core.evaluation.PolicyEvaluators;

/**
 * Represents a Policy Decision Point (PDP) engine that evaluates authorization requests.
 * The PDP engine evaluates an authorization request based on the associated policy and context,
 * and returns an evaluation result that indicates whether access is allowed or denied.
 *
 * The PDP engine utilizes the configured {@link PdpConfiguration} and the appropriate
 * {@link DecisionStrategy} to make authorization decisions based on evaluation results.
 *
 * <p>This class acts as the central point for making authorization decisions based on policies,
 * user context, and actions, enforcing access control decisions in an authorization system.</p>
 *
 * @author Truong Ngo
 */
public class PdpEngine {

   /**
    * Configuration for the Policy Decision Point engine.
    */
    private final PdpConfiguration configuration;

    /**
     * Constructs a new {@code PdpEngine} with the provided configuration.
     *
     * @param configuration The {@link PdpConfiguration} containing settings for the PDP engine.
     */
    public PdpEngine(PdpConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Evaluates an authorization request and returns the result based on the provided policy and context.
     *
     * The evaluation is performed by the {@link PolicyEvaluators} class, which applies the given policy
     * to the provided evaluation context. The context consists of details such as the subject, object,
     * action, and environment that influence the decision-making process.
     *
     * @param authzRequest The authorization request to be evaluated. This request includes information
     *                     about the subject (e.g., user), object (e.g., resource), action (e.g., access type),
     *                     environment, and policy.
     * @return The result of the evaluation, represented by an {@link EvaluationResult}, which indicates
     *         whether access is allowed (PERMIT), denied (DENY), or requires further processing (Indeterminate).
     *         The result encapsulates the decision outcome and the associated cause (if any).
     */
    public EvaluationResult evaluate(AuthzRequest authzRequest) {
        AbstractPolicy policy = authzRequest.getPolicy();
        EvaluationContext context = authzRequest.getEvaluationContext();
        return PolicyEvaluators.evaluate(context, policy);
    }

    /**
     * Authorizes an authorization request by evaluating it and making a decision based on the configured PDP strategy.
     *
     * This method first evaluates the request and then uses the {@link DecisionStrategy} configured in
     * {@link PdpConfiguration} to determine whether the request is allowed or denied. The evaluation result
     * provides the decision outcome (e.g., PERMIT or DENY). If the result is indeterminate, the cause of indeterminacy
     * will be included in the decision details. If no policy is applicable, a default message is used.
     *
     * @param authzRequest The authorization request to be processed, containing the subject, object, action,
     *                     environment, and policy to be evaluated.
     * @return An {@link AuthzDecision} representing the authorization decision, including the decision outcome
     *         (PERMIT or DENY) and any relevant details (e.g., cause of indeterminacy).
     */
    public AuthzDecision authorize(AuthzRequest authzRequest) {
        EvaluationResult evaluationResult = evaluate(authzRequest);
        AuthzDecision.Decision decision = configuration.getDecisionStrategy().decide(evaluationResult);
        Object details = evaluationResult.isIndeterminate() ?
                evaluationResult.getIndeterminateCause() :
                evaluationResult.isNotApplicable() ? "No policy applicable" : null;
        return new AuthzDecision(decision, details);
    }
}
