package com.nob.authorization.authzclient.servlet;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.*;

/**
 * {@code CacheBodyHttpServletRequest} is a custom wrapper for {@link HttpServletRequest}
 * that allows the request body to be read multiple times.
 * <p>
 * In a typical servlet environment, the input stream from a request can only be read once.
 * This wrapper caches the body upon instantiation and provides access to the cached data
 * through overridden {@code getInputStream()} and {@code getReader()} methods.
 * </p>
 * <p>
 * This is useful in scenarios like logging, validation, or authorization where the request
 * body needs to be read in pre-processing filters or interceptors before it is consumed by
 * the main controller or handler.
 * </p>
 *
 * <blockquote><pre>{@code
 * HttpServletRequest cachedRequest = new CacheBodyHttpServletRequest(originalRequest);
 * String body = new BufferedReader(cachedRequest.getReader())
 *         .lines()
 *         .collect(Collectors.joining(System.lineSeparator()));
 * }</pre></blockquote>
 *
 * @author Truong Ngo
 * @version 1.0
 */
public class CacheBodyHttpServletRequest extends HttpServletRequestWrapper {

    private final byte[] cachedBody;


    /**
     * Constructs a new {@code CacheBodyHttpServletRequest} by reading and caching the body of the original request.
     *
     * @param request the original {@link HttpServletRequest}
     * @throws IOException if an I/O error occurs during reading the request body
     */
    public CacheBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
    }


    /**
     * Returns a {@link ServletInputStream} that reads from the cached request body.
     *
     * @return a new {@link CacheBodyServletInputStream} initialized with the cached body
     */
    @Override
    public ServletInputStream getInputStream() {
        return new CacheBodyServletInputStream(this.cachedBody);
    }


    /**
     * Returns a {@link BufferedReader} for reading from the cached request body.
     *
     * @return a new {@link BufferedReader} initialized with the cached body
     */
    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }
}
