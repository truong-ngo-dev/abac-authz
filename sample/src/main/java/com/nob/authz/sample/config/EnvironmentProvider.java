package com.nob.authz.sample.config;

import com.nob.authorization.core.context.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class EnvironmentProvider implements com.nob.authorization.authzclient.pip.EnvironmentProvider {

    private Environment environment;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private int port;

    public EnvironmentProvider() {;
    }

    @Override
    public Environment getEnvironment(String serviceName) {
        if (environment == null) {
            environment = new Environment();
            environment.setService(new LinkedHashMap<>());
            environment.addServiceEnv("application.name", applicationName);
            environment.addServiceEnv("application.port", port);
        }
        return environment;
    }
}
