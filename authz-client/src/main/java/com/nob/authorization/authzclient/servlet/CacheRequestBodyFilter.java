package com.nob.authorization.authzclient.servlet;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * {@code CacheRequestBodyFilter} is a Spring {@link OncePerRequestFilter} that wraps the incoming
 * {@link HttpServletRequest} in a {@link CacheBodyHttpServletRequest}, allowing the request body
 * to be read multiple times during the request lifecycle.
 * <p>
 * This filter is especially useful when the request body needs to be accessed in multiple layers
 * such as logging, authentication, or validation filters before reaching the controller.
 * </p>
 *
 * <p>
 * The filter is annotated with {@code @Order(Ordered.HIGHEST_PRECEDENCE)} to ensure it is executed
 * early in the filter chain, before any other filters that might access the request body.
 * </p>
 *
 * <blockquote><pre>{@code
 * @Component
 * public class CacheRequestBodyFilter extends OncePerRequestFilter {
 *     // wraps request with a cached body for multi-read support
 * }
 * }</pre></blockquote>
 *
 * @author Truong Ngo
 * @version 1.0
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CacheRequestBodyFilter extends OncePerRequestFilter {

    /**
     * Wraps the original {@link HttpServletRequest} with a {@link CacheBodyHttpServletRequest}
     * and continues the filter chain with the wrapped request.
     *
     * @param request the incoming HTTP servlet request
     * @param response the HTTP servlet response
     * @param filterChain the filter chain
     * @throws ServletException if an exception occurs during filtering
     * @throws IOException if an I/O error occurs during reading the request body
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        CacheBodyHttpServletRequest cacheBodyHttpServletRequest = new CacheBodyHttpServletRequest(request);
        filterChain.doFilter(cacheBodyHttpServletRequest, response);
    }
}
