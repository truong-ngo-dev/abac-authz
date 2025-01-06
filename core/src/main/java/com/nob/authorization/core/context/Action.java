package com.nob.authorization.core.context;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents an action in an Attribute-Based Access Control (ABAC) system.
 * The Action class holds the HTTP request associated with the action and a set of attributes
 * that are used in ABAC policy evaluations to make access control decisions.
 * <p>
 * This class is used to store the relevant attributes for the
 * action being carried out, which are used to determine if access should be granted.
 * @author Truong Ngo
 */
@Data
public class Action {

    /**
     * The HTTP request that triggered the action.
     * */
    private HttpRequest request;

    /**
     * Attributes related to the action for ABAC evaluation.
     * */
    private Map<String, Object> attributes;

    /**
     * Constructs an {@code Action} object based on the provided HTTP request.
     * Initializes the request and creates an empty attributes map to store action-related attributes.
     *
     * @param httpRequest The HTTP request associated with the action.
     */
    public Action(HttpServletRequest httpRequest) {
        request = HttpRequest.parse(httpRequest);
        attributes = new LinkedHashMap<>();
    }

    /**
     * Adds an attribute to the action.
     * In ABAC systems, this method allows adding custom attributes to describe the action,
     * such as the type of action, requester details, or resource-specific attributes.
     *
     * @param name  The name of the attribute (e.g., "actionType", "resourceId").
     * @param value The value of the attribute, which represents specific data for the action.
     */
    public void addAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    /**
     * Retrieves the value of an attribute by its name.
     * This method retrieves the value of a specific attribute
     * related to the action being performed.
     *
     * @param attributeName The name of the attribute to retrieve (e.g., "actionType").
     * @return The value of the attribute, or null if the attribute is not found.
     */
    public Object getAttribute(String attributeName) {
        return attributes.get(attributeName); // Retrieve the value of the attribute.
    }
}
