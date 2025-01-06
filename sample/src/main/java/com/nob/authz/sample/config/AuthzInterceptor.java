package com.nob.authz.sample.config;

import com.nob.authorization.authzclient.pep.PepEngine;
import com.nob.authorization.core.pdp.AuthzDecision;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthzInterceptor implements HandlerInterceptor {

    private final PepEngine pepEngine;

    public AuthzInterceptor(PepEngine pepEngine) {
        this.pepEngine = pepEngine;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        AuthzDecision decision = pepEngine.enforce(request);
        if (decision.isDeny()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\":\"Access Denied\"}");
            return false;
        }
        return true;
    }
}
