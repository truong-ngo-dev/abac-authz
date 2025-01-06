package com.nob.authorization.core.context;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code Resource} class represents a resource in an ABAC (Attribute-Based Access Control) system.
 * A resource is any entity to which access is being controlled.
 * This class encapsulates the resource's name, data, and a collection of attributes
 * that can be used for access control evaluations.
 * @author Truong Ngo
 */
@Data
public class Resource {

    /**
     * The name of the resource.
     */
    private String name;

    /**
     * Collection of sub-resource name.
     */
    private List<String> subResourceName;

    /**
     * The actual data or content of the resource. This can be any object that represents the resource's data.
     */
    private Object data;

    /**
     * A map of attributes associated with the resource. Attributes are used for defining
     * metadata and other properties of the resource in a key-value format.
     */
    private Map<String, Object> attributes;

    /**
     * Default constructor. Initializes an empty attributes map.
     */
    public Resource() {
        this.attributes = new LinkedHashMap<>();
    }

    /**
     * Constructor to initialize the resource with a name and data.
     *
     * @param name the name of the resource
     * @param data the data or content of the resource
     */
    public Resource(String name, Object data) {
        this.name = name;
        this.data = data;
        this.attributes = new LinkedHashMap<>();
    }

    /**
     * Retrieves the value of an attribute associated with the resource.
     *
     * @param name the name of the attribute to retrieve
     * @return the value of the attribute, or {@code null} if the attribute does not exist
     */
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    /**
     * Sets or updates the value of an attribute for the resource.
     * If the attribute already exists, its value will be updated.
     *
     * @param name  the name of the attribute
     * @param value the value to associate with the attribute
     */
    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }
}
