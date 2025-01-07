//package com.nob.authorization.authzclient.pip;
//
//import com.nob.authorization.authzclient.rap.ParameterMapping;
//import com.nob.authorization.authzclient.rap.ResourceAccessMetadata;
//import com.nob.authorization.authzclient.rap.ResourceAccessPoint;
//import com.nob.authorization.authzclient.rap.ResourceAccessConfig;
//import com.nob.authorization.core.context.Action;
//import com.nob.authorization.core.context.HttpRequest;
//import com.nob.authorization.core.context.Resource;
//import com.nob.authorization.core.utils.StringUtils;
//import com.nob.authorization.core.utils.TypeUtils;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ApplicationContext;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.Comparator;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//
///**
// * Provides resources by extracting and processing data from HTTP requests,
// * utilizing the provided resource access configuration and metadata.
// * This class works with the ABAC (Attribute-Based Access Control) system to retrieve
// * and process data based on user and service attributes for resource access decisions.
// *
// * @author Truong Ngo
// */
//@Slf4j
//@RequiredArgsConstructor
//public class ResourceProvider {
//
//    /**
//     * The configuration for resource access, which defines how resources are accessed and processed.
//     */
//    @Getter
//    private final ResourceAccessConfig resourceAccessConfig;
//
//    /**
//     * The provider for retrieving resource access metadata, which defines the method and parameters
//     * required for accessing the resource.
//     */
//    private final ResourceAccessPoint resourceAccessPoint;
//
//    /**
//     * The application context used to retrieve beans for invoking accessor methods.
//     */
//    private final ApplicationContext applicationContext;
//
//    /**
//     * Retrieves a {@link Resource} based on the provided {@link Action}.
//     * This method extracts relevant parameters from the request, invokes the appropriate
//     * accessor method, and returns the resource data.
//     *
//     * @param action The action that triggered the resource request. It contains the request and associated details.
//     * @return The {@link Resource} containing the resource name and the corresponding data.
//     * @throws RuntimeException if any reflection-based operation fails, such as method invocation errors.
//     */
//    public Resource getResource(Action action) {
//        Resource resource = new Resource();
//        String path = action.getRequest().getRequestedURI();
//        String resourceName = getResourceName(path);
//        resource.setName(resourceName);
//
//        if (!isResourceDataNeeded(path, action)) return resource; // If the resource data is not needed, return an empty resource.
//
//        String resourceKey = getResourceAccessMetadataKey(action);
//        ResourceAccessMetadata metadata = resourceAccessPoint.getResourceAccessMetadata(resourceKey);
//
//        Class<?>[] parameterTypes = metadata.getParameterMappings().stream()
//                .sorted(Comparator.comparing(ParameterMapping::getIndex))
//                .map(ParameterMapping::getParameterType)
//                .toArray(Class<?>[]::new);
//
//        Object[] extractedParameterValues = metadata.getParameterMappings().stream()
//                .sorted(Comparator.comparing(ParameterMapping::getIndex))
//                .map(mp -> extractHttpValue(action, mp.getParameterName(), mp.getSource()))
//                .toArray(Object[]::new);
//
//        Object[] parameterValues = new Object[parameterTypes.length];
//        for (int i = 0; i < parameterTypes.length; i++) {
//            Object parameterValue = extractedParameterValues[i];
//            Class<?> parameterType = parameterTypes[i];
//            if (parameterValue.getClass() == parameterType) {
//                parameterValues[i] = parameterValue;
//            } else {
//                parameterValues[i] = TypeUtils.castValueAs(parameterValue.toString(), parameterType);
//            }
//        }
//
//        try {
//            Method method = metadata.getAccessor().getMethod(metadata.getAccessorMethod(), parameterTypes);
//            Object accessor = applicationContext.getBean(metadata.getAccessor());
//            Object data = method.invoke(accessor, parameterValues);
//            resource.setData(data);
//            return resource;
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            log.error(e.getMessage(), e);
//            throw new RuntimeException("Error invoking method for resource access", e);
//        }
//    }
//
//    /**
//     * Extracts the resource name from the given path using the configured resource name extractor.
//     *
//     * @param path The path from which the resource name should be extracted.
//     * @return The extracted resource name, or an empty string if no match is found.
//     */
//    public String getResourceName(String path) {
//        Pattern pattern = Pattern.compile(resourceAccessConfig.getResourceNameExtractor());
//        Matcher matcher = pattern.matcher(path);
//        return matcher.find() ? matcher.group(1) : "";
//    }
//
//    /**
//     * Generates a unique key for resource access metadata using HTTP method and path template.
//     *
//     * @param action The action that triggered the resource request, used to extract the method and path.
//     * @return A unique string key for the resource access metadata, combining the HTTP method and path template.
//     */
//    public String getResourceAccessMetadataKey(Action action) {
//        String path = action.getRequest().getRequestedURI();
//        String pathTemplate = getPathTemplate(path, action);
//        String method = action.getRequest().getMethod();
//        return String.format("%s_%s", method, pathTemplate);
//    }
//
//    /**
//     * Determines whether the resource data associated with the provided path is needed
//     * based on the configured required fetch resources.
//     *
//     * @param path The path to check for resource need.
//     * @param action The action containing the request details.
//     * @return {@code true} if the resource is needed, {@code false} otherwise.
//     */
//    public boolean isResourceDataNeeded(String path, Action action) {
//        return resourceAccessConfig.getIgnoredPaths()
//                .stream().anyMatch(s -> matchPath(path, s, action));
//    }
//
//    /**
//     * Retrieves the path template that matches the provided path and action.
//     *
//     * @param path The path to check for a matching template.
//     * @param action The action containing request details.
//     * @return The matched path template, or {@code null} if no match is found.
//     */
//    @SuppressWarnings("all")
//    public String getPathTemplate(String path, Action action) {
//        return resourceAccessConfig.getIgnoredPaths()
//                .stream().filter(s -> matchPath(path, s, action)).findFirst().orElse(null);
//    }
//
//    /**
//     * Matches a given path template with the actual path from the action,
//     * considering any path variables.
//     *
//     * @param path The actual path.
//     * @param template The path template to match.
//     * @param action The action containing request details.
//     * @return {@code true} if the path matches the template, {@code false} otherwise.
//     */
//    private boolean matchPath(String path, String template, Action action) {
//        boolean match = StringUtils.matchUrlPath(template, path);
//        boolean hasPathVariable = StringUtils.hasPathVariable(template);
//        if (!match) return false;
//        if (hasPathVariable) return !action.getRequest().getPathVariables().isEmpty();
//        else return true;
//    }
//
//    /**
//     * Extracts a parameter value from the HTTP request based on its source (e.g., query param, path variable).
//     *
//     * @param action The action containing the HTTP request.
//     * @param key The key corresponding to the parameter.
//     * @param source The source from which to extract the parameter.
//     * @return The extracted value from the request, based on the source type.
//     */
//    public Object extractHttpValue(Action action, String key, ParameterMapping.HttpSource source) {
//        HttpRequest request = action.getRequest();
//        return switch (source) {
//            case COOKIE -> request.getCookie(key);
//            case SESSION -> request.getSession(key);
//            case QUERY_PARAM -> request.getQueryParam(key);
//            case PATH_VARIABLE -> request.getPathVariable(key);
//            case REQUEST_HEADER -> request.getHeader(key);
//            case REQUEST_BODY -> request.getRequestBody(key);
//        };
//    }
//}
