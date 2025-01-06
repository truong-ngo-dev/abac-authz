package com.nob.authz.sample.config;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nob.authorization.authzclient.rap.ResourceAccessMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ResourceAccessPoint implements com.nob.authorization.authzclient.rap.ResourceAccessPoint {

    private final Map<String, ResourceAccessMetadata> resourceAccessMetadataMap;

    public ResourceAccessPoint() {
        try {
            Resource resource = new ClassPathResource("rap.json");
            List<ResourceAccessMetadata> accessMetadata = new ObjectMapper().readValue(resource.getInputStream(), new TypeReference<>() {});
            resourceAccessMetadataMap = accessMetadata.stream().collect(Collectors.toMap(m -> String.format("%s_%s", m.getHttpMethod().name(), m.getPathTemplate()), Function.identity()));
        } catch (IOException e) {
            log.error("Cannot read rap.json", e);
            throw new RuntimeException("Cannot read rap.json");
        }
    }

    @Override
    public ResourceAccessMetadata getResourceAccessMetadata(String resourceKey) {
        return resourceAccessMetadataMap.get(resourceKey);
    }
}
