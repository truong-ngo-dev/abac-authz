package com.nob.authorization.core.utils;

import java.lang.invoke.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

import static org.springframework.util.StringUtils.capitalize;

public class ReflectionUtils {


    /**
     * Retrieve a field by its name, including fields from superclasses (excluding Object).
     *
     * @param clazz     The class to start searching from.
     * @param fieldName The name of the field to retrieve.
     * @return an {@link Optional} containing the Field, or an empty Optional if the field is not found.
     * @throws IllegalArgumentException if clazz or fieldName is null.
     */
    public static Optional<Field> getField(Class<?> clazz, String fieldName) {
        if (Objects.isNull(clazz) || Objects.isNull(fieldName)) {
            throw new IllegalArgumentException("Class and field name must not be null");
        }
        Class<?> currentClass = clazz;
        while (Objects.nonNull(currentClass) && !currentClass.equals(Object.class)) {
            try {
                return Optional.of(currentClass.getDeclaredField(fieldName));
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return Optional.empty();
    }


    @SuppressWarnings("all")
    public static Function getFieldAccessor(Class<?> clazz, String fieldName) {
        String getterName = clazz == boolean.class || clazz == Boolean.class ? "is" + capitalize(fieldName) : "get" + capitalize(fieldName);
        Method getterMethod;
        try {
            getterMethod = clazz.getMethod(getterName);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("The class (" + clazz + ") has doesn't have the getter method ("
                    + getterName + ").", e);
        }
        Class<?> returnType = getterMethod.getReturnType();

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        CallSite site;
        try {
            site = LambdaMetafactory.metafactory(lookup,
                    "apply",
                    MethodType.methodType(Function.class),
                    MethodType.methodType(Object.class, Object.class),
                    lookup.findVirtual(clazz, getterName, MethodType.methodType(returnType)),
                    MethodType.methodType(returnType, clazz));
        } catch (LambdaConversionException | NoSuchMethodException | IllegalAccessException e) {
            throw new IllegalArgumentException("Lambda creation failed for method (" + getterMethod + ").", e);
        }
        try {
            return (Function) site.getTarget();
        } catch (Throwable e) {
            throw new IllegalArgumentException("Lambda creation failed for method (" + getterMethod + ").", e);
        }
    }


    /**
     * Retrieve the value of a field from an object.
     *
     * @param obj       The object to retrieve the field value from.
     * @param fieldName The name of the field.
     * @return an {@link Optional} containing the value of the field, or an empty Optional if the field is not found or inaccessible.
     * @throws IllegalArgumentException if obj or fieldName is null.
     * @throws UnsupportedOperationException if field is not accessible
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        if (Objects.isNull(obj) || Objects.isNull(fieldName)) {
            throw new IllegalArgumentException("Object and field name must not be null");
        }
        Class<?> clazz = obj.getClass();
        Field field = getField(clazz, fieldName).orElseThrow(() -> new IllegalArgumentException("Field " + fieldName + " not found"));
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException("Unable to access field: " + fieldName);
        }
    }


    /**
     * Retrieves the value from a nested object structure based on the given dot-separated path.
     *
     * <p>The path can contain nested field names, map keys, and indexed access for collections or arrays.
     * For example:
     * <ul>
     *     <li>"field1.field2" retrieves the "field2" field of the "field1" field in the object.</li>
     *     <li>"list[0]" retrieves the first element of a collection or array named "list".</li>
     *     <li>"map.key" retrieves the value associated with the key "key" in a map named "map".</li>
     * </ul>
     *
     * @param obj  The root object from which to start navigation. Must not be {@code null}.
     * @param path The dot-separated path specifying the value to retrieve. Must not be {@code null}.
     * @return The value located at the specified path, or throws an exception if the path cannot be resolved.
     * @throws IllegalArgumentException if the {@code obj} or {@code path} is {@code null},
     *                                  if the path is invalid, or if any part of the resolution fails.
     */
    public static Object getObjectValue(Object obj, String path) {
        if (Objects.isNull(obj) || Objects.isNull(path)) {
            throw new IllegalArgumentException("Object and path must not be null");
        }
        String[] paths = path.split("\\.");
        Object target = obj;
        Object value = null;
        for (int i = 0; i < paths.length; i++) {
            String p = paths[i];
            if (Objects.isNull(target)) throw new IllegalArgumentException("Path '" + p + "' error");
            if (p.matches(".*\\[\\d+]$")) {
                String prefix = p.substring(0, p.length() - 3);
                int index = StringUtils.extractIndex(p);
                if (!prefix.isEmpty()) {
                    Object base = getValueOfNonCollectionObject(target, prefix);
                    if (Objects.isNull(base)) throw new IllegalArgumentException("Path '" + p + "' error");
                    value = getCollectionElement(p, index, base);
                } else {
                    value = getCollectionElement(p, index, target);
                }
            } else {
                value = getValueOfNonCollectionObject(target, p);
            }
            if (i != paths.length - 1) {
                if (value == null) throw new IllegalArgumentException("Path '" + p + "' error");
                else target = value;
            } else {
                target = value;
            }
        }
        return value;
    }


    /**
     * Utility method for {@link #getObjectValue(Object, String)} Retrieves an element from a collection or array based on the specified index.
     *
     * @param path The original path string, used for error reporting.
     * @param index The index of the element to retrieve.
     * @param base The collection or array from which to retrieve the element.
     * @return The element at the specified index.
     * @throws IllegalArgumentException if {@code base} is not a collection or array,
     *                                  or if the index is out of bounds.
     */
    private static Object getCollectionElement(String path, int index, Object base) {
        Object value;
        if (!TypeUtils.isCollection(base.getClass())) throw new IllegalArgumentException("Base object is not collection");
        if (Collection.class.isAssignableFrom(base.getClass())) {
            List<?> list = new ArrayList<>((Collection<?>) base);
            if (index < 0 || index >= list.size()) throw new IllegalArgumentException("Path " + path + " error");
            value = list.get(index);
        } else {
            if (index < 0 || index >= Array.getLength(base)) throw new IllegalArgumentException("Path " + path + " error");
            value = Array.get(base, index);
        }
        return value;
    }


    /**
     * Retrieves the value of a non-collection object field or map entry based on the field name or key.
     *
     * @param obj  The object or map from which to retrieve the value.
     * @param name The field name or key. Must not be {@code null}.
     * @return The value of the specified field or key.
     * @throws IllegalArgumentException if {@code obj} or {@code name} is {@code null},
     *                                  if {@code obj} is a value-based type or collection,
     *                                  or if the field or key does not exist.
     */
    private static Object getValueOfNonCollectionObject(Object obj, String name) {
         if (Objects.isNull(obj) || Objects.isNull(name)) throw new IllegalArgumentException("Object and name must not be null");
         if (TypeUtils.isValueBased(obj.getClass())) throw new IllegalArgumentException("Object is value based type");
         if (TypeUtils.isCollection(obj.getClass())) throw new IllegalArgumentException("Object is collection");
         if (TypeUtils.isMap(obj.getClass())) return ((Map<?, ?>) obj).get(name);
         return getFieldValue(obj, name);
    }

}
