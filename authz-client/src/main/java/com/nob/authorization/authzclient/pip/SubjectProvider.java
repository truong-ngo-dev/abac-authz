package com.nob.authorization.authzclient.pip;

import com.nob.authorization.core.context.Subject;

import java.security.Principal;

/**
 * Interface for providing a {@link Subject} based on a given {@link Principal}.
 * This interface is responsible for mapping a {@link Principal} to a {@link Subject} that represents
 * the user or entity in the context of an authorization system, such as ABAC (Attribute-Based Access Control).
 *
 * Implementations of this interface will define how a {@link Principal}, which typically represents
 * an authenticated user or entity, is used to create or retrieve the corresponding {@link Subject}.
 *
 * @see Principal
 * @see Subject
 * @author Truong Ngo
 */
public interface SubjectProvider {

    /**
     * Retrieves the {@link Subject} corresponding to the provided {@link Principal}.
     * The {@link Subject} represents the user or entity within the context of the authorization system.
     *
     * @param principal The principal representing the authenticated user or entity.
     * @return The {@link Subject} associated with the given {@link Principal}.
     */
    Subject getSubject(Principal principal);
}
