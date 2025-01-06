package com.nob.authorization.core.context;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Represents a Subject with a user ID, roles, and attributes for Attribute-Based Access Control (ABAC).
 * A Subject can have a list of roles and a collection of key-value pairs representing attributes.
 * This class provides methods to manage the attributes.
 * @author Truong Ngo
 */
@Data
public class Subject {

    /**
     * The user ID associated with the subject.
     */
    private String userId;

    /**
     * A list of roles assigned to the subject.
     */
    private List<String> roles;

    /**
     * A map of attributes associated with the subject. The keys are attribute names, and the values are the corresponding attribute values.
     */
    private Map<String, Object> attributes;

    /**
     * Adds a new attribute to the subject.
     * If the attribute already exists, its value will be updated.
     *
     * @param key   The name of the attribute to be added.
     * @param value The value of the attribute to be added.
     */
    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * Retrieves the value of a specific attribute based on the key.
     *
     * @param key The name of the attribute to retrieve.
     * @return The value of the attribute, or {@code null} if the attribute does not exist.
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }
}
