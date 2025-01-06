package com.nob.authz.sample.config;

import com.nob.authorization.authzclient.pep.PepEngine;
import com.nob.authorization.authzclient.pip.*;
import com.nob.authorization.authzclient.pip.EnvironmentProvider;
import com.nob.authorization.authzclient.pip.PolicyProvider;
import com.nob.authorization.authzclient.pip.SubjectProvider;
import com.nob.authorization.authzclient.rap.ResourceAccessConfig;
import com.nob.authorization.core.pdp.DecisionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class AuthzConfig {

    private final ApplicationContext applicationContext;

    @Bean
    public ResourceProvider resourceProvider() {
        Set<String> requiredFetchResources = Set.of("/api/note/{id}");
        String apiPattern = "^/api/(\\w+)/?.*$";
        ResourceAccessConfig config = new ResourceAccessConfig(apiPattern, requiredFetchResources);
        ResourceAccessPoint resourceAccessPoint = new ResourceAccessPoint();
        return new ResourceProvider(config, resourceAccessPoint, applicationContext);
    }

    @Bean
    public PipEngine pipEngine() {
        PolicyProvider policyProvider = applicationContext.getBean(PolicyProvider.class);
        EnvironmentProvider environmentProvider = applicationContext.getBean(EnvironmentProvider.class);
        SubjectProvider subjectProvider = applicationContext.getBean(SubjectProvider.class);
        return new PipEngine(policyProvider, environmentProvider, subjectProvider, resourceProvider());
    }

    @Bean
    public PepEngine pepEngine() {
        return new PepEngine(DecisionStrategy.NOT_APPLICABLE_PERMIT_INDETERMINATE_DENY, pipEngine());
    }

}
