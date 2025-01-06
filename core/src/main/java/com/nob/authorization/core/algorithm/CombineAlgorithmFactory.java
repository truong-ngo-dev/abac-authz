package com.nob.authorization.core.algorithm;

import com.nob.authorization.core.domain.AbstractPolicy;
import com.nob.authorization.core.domain.Principle;
import com.nob.authorization.core.domain.Rule;

/**
 * Factory class for instantiating {@link CombineAlgorithm} instances based on a specified {@link CombineAlgorithmName}.
 * This class provides a centralized mechanism for creating different combination algorithms used in
 * evaluating and combining {@link Principle} elements in XACML policies.
 *
 * @author Truong Ngo
 */
public class CombineAlgorithmFactory {

    /**
     * Factory method for creating a {@link CombineAlgorithm} instance based on the provided {@link CombineAlgorithmName}.
     *
     * @param name The name of the combination algorithm to instantiate.
     * @param clazz The class of the {@link Principle} elements to which the algorithm will be applied.
     *              This is typically {@link Rule} or {@link AbstractPolicy}.
     * @param <E> The type of {@link Principle} elements (e.g., {@link Rule} or {@link AbstractPolicy}).
     * @return An instance of the appropriate {@link CombineAlgorithm}.
     * @throws IllegalArgumentException If an invalid combination algorithm is requested.
     */
    public static <E extends Principle> CombineAlgorithm<E> from(CombineAlgorithmName name, Class<E> clazz) {
        return switch (name) {
            case DENY_OVERRIDES -> new DenyOverridesCombineAlgorithm<>();
            case PERMIT_OVERRIDES -> new PermitOverridesCombineAlgorithm<>();
            case DENY_UNLESS_PERMIT -> new DenyUnlessPermitCombineAlgorithm<>();
            case PERMIT_UNLESS_DENY -> new PermitUnlessDenyCombineAlgorithm<>();
            case FIRST_APPLICABLE -> new FirstApplicableCombineAlgorithm<>();
            case ONLY_ONE_APPLICABLE -> getOnlyOneCombineAlg(clazz);
        };
    }

    /**
     * Creates an instance of {@link OnlyOneApplicableCombineAlgorithm} for {@link AbstractPolicy}.
     *
     * @param clazz The class of the {@link Principle} elements to which the algorithm will be applied.
     * @param <E> The type of {@link Principle} elements (must be {@link AbstractPolicy}).
     * @return An instance of {@link OnlyOneApplicableCombineAlgorithm}.
     * @throws IllegalArgumentException If the provided class is not {@link AbstractPolicy}.
     */
    private static <E extends Principle> CombineAlgorithm<E> getOnlyOneCombineAlg(Class<E> clazz) {
        if (clazz == AbstractPolicy.class) {
            return new OnlyOneApplicableCombineAlgorithm<>();
        } else {
            throw new IllegalArgumentException("Only one applicable combine algorithm is not available for Rule.");
        }
    }
}
