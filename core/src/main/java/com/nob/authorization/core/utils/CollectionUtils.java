package com.nob.authorization.core.utils;

import java.util.Collection;
import java.util.Objects;

/**
 * Collection utility
 * @author Truong Ngo
 * */
public class CollectionUtils {

    /**
     * Check if collection is null or empty
     * @param c the collection
     * */
    public static boolean isNullOrEmpty(Collection<?> c) {
        return Objects.isNull(c) || c.isEmpty();
    }
}
