package com.nob.authorization.authzclient.pep;

import com.nob.authorization.authzclient.exception.AuthorizationException;
import com.nob.authorization.authzclient.pip.PipEngine;
import com.nob.authorization.core.context.Action;
import com.nob.authorization.core.context.Environment;
import com.nob.authorization.core.context.Resource;
import com.nob.authorization.core.context.Subject;
import com.nob.authorization.core.domain.AbstractPolicy;
import com.nob.authorization.core.pdp.AuthzDecision;
import com.nob.authorization.core.pdp.AuthzRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * An aspect that enforces authorization policies for methods annotated with
 * {@code @PreEnforce} and {@code @PostEnforce} annotations. This aspect uses
 * a combination of the PEP (Policy Enforcement Point) and PIP (Policy Information Point)
 * engines to perform access control checks.
 *
 * <p>Key features of this aspect:</p>
 * <ul>
 *     <li>Executes pre-authorization checks before method execution using {@link #preEnforce(JoinPoint)}.</li>
 *     <li>Executes post-authorization checks after method execution using {@link #postEnforce(JoinPoint, Object)}.</li>
 *     <li>Prepares authorization requests using {@link #prepareAuthzRequest()}.</li>
 * </ul>
 *
 * <p>If an authorization decision denies access, an {@link AuthorizationException} is thrown with relevant details.</p>
 *
 * @see PreEnforce
 * @see PostEnforce
 * @see AuthorizationException
 * @see PepEngine
 * @see PipEngine
 * @see AuthzRequest
 * @see AuthzDecision
 *
 * @author Truong Ngo
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    /**
     * The current HTTP servlet request, used to extract request details for authorization checks.
     */
    private final HttpServletRequest request;

    /**
     * The Policy Enforcement Point (PEP) engine used for evaluating authorization requests.
     */
    private final PepEngine pepEngine;

    /**
     * The Policy Information Point (PIP) engine used for retrieving policies, subjects,
     * environment, and resource information.
     */
    private final PipEngine pipEngine;

    /**
     * The name of the service, injected from the application properties.
     */
    @Value("${spring.application.name}")
    private String serviceName;

    /**
     * Executes a pre-authorization check before the method execution.
     *
     * <p>This method is triggered for methods annotated with {@code @PreEnforce}.
     * It prepares an {@link AuthzRequest} and evaluates it using the PEP engine.
     * If access is denied, an {@link AuthorizationException} is thrown.</p>
     *
     * @param joinPoint The join point representing the method being intercepted.
     * @throws AuthorizationException if the authorization decision denies access.
     */
    @Before(value = "@annotation(com.nob.authorization.authzclient.pep.PreEnforce)")
    public void preEnforce(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        PreEnforce enforcer = method.getAnnotation(PreEnforce.class);
        String[] ignoredPath = enforcer.ignore();
        AuthzRequest authzRequest = prepareAuthzRequest();
        AuthzDecision decision = pepEngine.enforce(authzRequest, ignoredPath);
        if (decision.isDeny())
            throw new AuthorizationException("Forbidden", decision.getDetails(), decision.getTimestamp());
    }

    /**
     * Executes a post-authorization check after the method execution.
     *
     * <p>This method is triggered for methods annotated with {@code @PostEnforce}.
     * It prepares an {@link AuthzRequest}, sets the returned object as data in the request,
     * and evaluates it using the PEP engine. If access is denied, an {@link AuthorizationException} is thrown.</p>
     *
     * @param joinPoint The join point representing the method being intercepted.
     * @param returnObject The object returned by the intercepted method.
     * @throws AuthorizationException if the authorization decision denies access.
     */
    @AfterReturning(value = "@annotation(com.nob.authorization.authzclient.pep.PostEnforce)", returning = "returnObject")
    public void postEnforce(JoinPoint joinPoint, Object returnObject) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        PostEnforce enforcer = method.getAnnotation(PostEnforce.class);
        String[] ignoredPath = enforcer.ignore();
        AuthzRequest authzRequest = prepareAuthzRequest();
        authzRequest.getObject().setData(returnObject);
        AuthzDecision decision = pepEngine.enforce(authzRequest, ignoredPath);
        if (decision.isDeny())
            throw new AuthorizationException("Forbidden", decision.getDetails(), decision.getTimestamp());
    }

    /**
     * Prepares an {@link AuthzRequest} by gathering necessary details from the
     * {@link PipEngine}, including policy, subject, resource, action, and environment information.
     *
     * <p>This method extracts:</p>
     * <ul>
     *     <li>The policy associated with the current service.</li>
     *     <li>The subject (user or entity) from the current request principal.</li>
     *     <li>The environment settings for the current service.</li>
     *     <li>The action and resource details from the current HTTP request.</li>
     * </ul>
     *
     * @return A fully constructed {@link AuthzRequest} containing all necessary data for authorization evaluation.
     */
    public AuthzRequest prepareAuthzRequest() {
        AbstractPolicy policy = pipEngine.getPolicy(serviceName);
        Action action = new Action(request);
        Subject subject = pipEngine.getSubject(request.getUserPrincipal());
        Environment environment = pipEngine.getEnvironment(serviceName);
        Resource resource = new Resource();
        String path = action.getRequest().getRequestedURI();
        String resourceName = pipEngine.getResourceName(path);
        resource.setName(resourceName);
        return new AuthzRequest(subject, resource, action, environment, policy);
    }
}
