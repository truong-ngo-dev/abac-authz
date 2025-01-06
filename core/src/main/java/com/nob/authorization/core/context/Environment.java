package com.nob.authorization.core.context;

import lombok.Data;

import java.util.Map;

/**
 * The {@code Environment} class is responsible for managing environment-specific data
 * that can influence access control decisions in an Attribute-Based Access Control (ABAC) system.
 * The class allows you to store and retrieve environmental information under two categories:
 * <ul>
 *     <li>Global Environment: Data that is universally applicable across the system.</li>
 *     <li>Service Environment: Data specific to a particular service or context.</li>
 * </ul>
 * <p>
 * This class provides methods to add and retrieve environment attributes for both global and service-specific contexts.
 * </p>
 * @author Truong Ngo
 */
@Data
public class Environment {

    /** A map holding global environment attributes */
    private Map<String, Object> global;

    /** A map holding service-specific environment attributes */
    private Map<String, Object> service;

    /**
     * Adds a key-value pair to the global environment map.
     * <p>
     * This method associates the provided key with a value that is relevant to the global context.
     * </p>
     *
     * @param key   The key (a {@code String}) identifying the global environment attribute.
     * @param value The value (an {@code Object}) associated with the key.
     */
    public void addGlobalEnv(String key, Object value) {
        global.put(key, value);
    }

    /**
     * Adds a key-value pair to the service-specific environment map.
     * <p>
     * This method associates the provided key with a value that is specific to a particular service or context.
     * </p>
     *
     * @param key   The key (a {@code String}) identifying the service-specific environment attribute.
     * @param value The value (an {@code Object}) associated with the key.
     */
    public void addServiceEnv(String key, Object value) {
        service.put(key, value);
    }

    /**
     * Retrieves the value associated with a given key from the global environment map.
     * <p>
     * If the key does not exist, this method returns {@code null}.
     * </p>
     *
     * @param key The key (a {@code String}) for which the value is requested.
     * @return The value associated with the specified key, or {@code null} if the key does not exist.
     */
    public Object getGlobalEnv(String key) {
        return global.get(key);
    }

    /**
     * Retrieves the value associated with a given key from the service-specific environment map.
     * <p>
     * If the key does not exist, this method returns {@code null}.
     * </p>
     *
     * @param key The key (a {@code String}) for which the value is requested.
     * @return The value associated with the specified key, or {@code null} if the key does not exist.
     */
    public Object getServiceEnv(String key) {
        return service.get(key);
    }
}
