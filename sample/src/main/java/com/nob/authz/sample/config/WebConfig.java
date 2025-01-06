package com.nob.authz.sample.config;

import com.nob.authorization.authzclient.pep.PepEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final PepEngine pepEngine;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthzInterceptor(pepEngine));
    }
}
