package com.nob.authorization.authzclient.servlet;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CacheBodyServletInputStream extends ServletInputStream {

    private static final Logger log = LoggerFactory.getLogger(CacheBodyServletInputStream.class);

    private final InputStream cachedBodyInputStream;

    public CacheBodyServletInputStream(byte[] cacheBody) {
        this.cachedBodyInputStream = new ByteArrayInputStream(cacheBody);
    }

    /**
     * Indicates whether InputStream has more data to read or not.
     *
     * @return true when zero bytes available to read
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
     * Indicates whether InputStream is ready for reading or not.
     * Since we've already copied InputStream in a byte array, we'll return true to indicate that it's always available.
     *
     * @return true
     */
    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
        return cachedBodyInputStream.read();
    }
}
