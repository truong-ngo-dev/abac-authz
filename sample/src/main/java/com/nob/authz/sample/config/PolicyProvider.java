package com.nob.authz.sample.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nob.authorization.core.domain.AbstractPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class PolicyProvider implements com.nob.authorization.authzclient.pip.PolicyProvider {

    @Value("classpath:policy.json")
    private Resource resource;

    private AbstractPolicy policy;

    public PolicyProvider() {}

    @Override
    public AbstractPolicy getPolicy(String serviceName) {
        if (policy == null) {
            try {
                InputStream inputStream = resource.getInputStream();
                policy = new ObjectMapper().readValue(inputStream, AbstractPolicy.class);
            } catch (IOException ignored) {}
        }
        return policy;
    }
}
