package com.nob.authorization.authzclient.servlet;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * {@code CacheBodyServletInputStream} is a wrapper around a cached request body byte array,
 * allowing the body of an HTTP request to be read multiple times.
 * <p>
 * In servlet-based applications, the request input stream can usually be read only once.
 * This class enables re-reading the input stream by storing the body in a {@link ByteArrayInputStream}.
 * It is typically used in filters or interceptors where the request body needs to be logged or processed
 * before it reaches the actual servlet or controller.
 * </p>
 *
 * <blockquote><pre>{@code
 * byte[] cachedBody = request.getInputStream().readAllBytes();
 * ServletInputStream inputStream = new CacheBodyServletInputStream(cachedBody);
 * }</pre></blockquote>
 *
 * @author Truong Ngo
 * @version 1.0
 */
public class CacheBodyServletInputStream extends ServletInputStream {

    private static final Logger log = LoggerFactory.getLogger(CacheBodyServletInputStream.class);

    private final InputStream cachedBodyInputStream;

    /**
     * Constructs a new {@code CacheBodyServletInputStream} with the given cached body bytes.
     *
     * @param cacheBody the byte array representing the cached request body
     */
    public CacheBodyServletInputStream(byte[] cacheBody) {
        this.cachedBodyInputStream = new ByteArrayInputStream(cacheBody);
    }


    /**
     * Indicates whether all the data from the input stream has been read.
     *
     * @return {@code true} if there are no more bytes available to read, {@code false} otherwise
     */
    @Override
    public boolean isFinished() {
        try {
            return cachedBodyInputStream.available() == 0;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }


    /**
     * Indicates whether the input stream is ready to be read.
     * <p>
     * Since the stream is backed by a byte array in memory, it is always ready for reading.
     * </p>
     *
     * @return {@code true}
     */
    @Override
    public boolean isReady() {
        return true;
    }


    /**
     * Not supported in this implementation.
     *
     * @param listener the read listener to be set
     * @throws UnsupportedOperationException always thrown
     */
    @Override
    public void setReadListener(ReadListener listener) {
        throw new UnsupportedOperationException();
    }


    /**
     * Reads the next byte of data from the input stream.
     *
     * @return the next byte of data, or -1 if the end of the stream is reached
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read() throws IOException {
        return cachedBodyInputStream.read();
    }
}
