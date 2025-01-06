package com.nob.authorization.core.pdp;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The {@code PdpConfiguration} class represents the configuration for a Policy Decision Point (PDP).
 * It defines the strategy used to handle decisions when a policy evaluation result is {@code notApplicable}
 * or {@code indeterminate}, allowing flexibility in how such results are processed.
 *
 * <p>This class encapsulates the {@link DecisionStrategy} that will be used to compute a final decision
 * when a policy evaluation does not yield a clear {@code PERMIT} or {@code DENY} result.
 * </p>
 *
 * <p>It provides a way for the application to customize how PDP decisions are handled, including
 * conflict resolution, fallback mechanisms, and logging behavior for exceptional cases.
 * </p>
 * @see DecisionStrategy
 */
@Data
public class PdpConfiguration {

    /**
     * The decision strategy that will be applied when evaluating policy results.
     * It determines how to handle {@code notApplicable} and {@code indeterminate} results.
     */
    private DecisionStrategy decisionStrategy;

    /**
     * Other configuration
     * */
    private Map<String, Object> config;

    /**
     * Constructs a {@code PdpConfiguration} object with the specified decision strategy and empty config.
     *
     * @param decisionStrategy The decision strategy to be used by the PDP.
     * @throws IllegalArgumentException if the provided strategy is null.
     */
    public PdpConfiguration(DecisionStrategy decisionStrategy) {
        if (decisionStrategy == null) {
            throw new IllegalArgumentException("Decision strategy cannot be null.");
        }
        this.decisionStrategy = decisionStrategy;
        this.config = new LinkedHashMap<>();
    }
}
