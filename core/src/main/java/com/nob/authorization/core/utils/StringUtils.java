package com.nob.authorization.core.utils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * String utility
 * @author Truong Ngo
 * */
public class StringUtils {

    public static final String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    public static final String ALPHANUMERIC_REGEX = "[a-zA-Z0-9]+";

    public static final String URL_REGEX_PREFIX = "^(?i)(%s):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+[/#?]?.*$";

    public static final String COLLECTION_ACCESS_REGEX = "^+\\[(\\d+)]$";

    public static final String PATH_VARIABLE_REGEX = ".*\\{[^}]+}.*";

    public static final String PATH_VARIABLE_REGEX_W_GROUP = "\\{([^}]+)}";


    /**
     * Check if given string is empty or not after trim()
     * @param s the string
     * */
    public static boolean isTrimEmpty(String s) {
        return Objects.isNull(s) || s.trim().isEmpty();
    }


    /**
     * Join the object's collection into string
     * @param list the collection
     * @param delimiter delimiter between element
     * @param mapper map object of T to string
     * @param <T> generic type of object
     * */
    public static <T> String join(List<T> list, String delimiter, Function<T, String> mapper) {
        if (CollectionUtils.isNullOrEmpty(list)) return null;
        return list.stream().map(mapper).collect(Collectors.joining(delimiter));
    }


    /**
     * Split string into collection of object
     * @param s the string
     * @param delimiter delimiter between element
     * @param mapper map string to object of T
     * @param <T> generic type of object
     * */
    public static <T> List<T> split(String s, String delimiter, Function<String, T> mapper) {
        if (isTrimEmpty(s)) return Collections.emptyList();
        return Stream.of(s.split(delimiter)).map(mapper).collect(Collectors.toList());
    }


    /**
     * Get the non-empty value of string, if the input string is empty return value is null
     * @param s the string
     * */
    public static String nonEmptyValue(String s) {
        return isTrimEmpty(s) ? null : s;
    }


    /**
     * Get the non-null string value
     * @param s the string
     * */
    public static String nvl(String s) {
        return isTrimEmpty(s) ? "" : s;
    }


    /**
     * Check if given string is email
     * @param s given string
     * @return true if string is email
     * */
    public static boolean isEmail(String s) {
        return Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE).matcher(s).matches();
    }


    /**
     * Check if given string is alphanumeric
     * @param s given string
     * @return true if string is alphanumeric
     * */
    public static boolean isAlphanumeric(String s) {
        return s.matches(ALPHANUMERIC_REGEX);
    }


    /**
     * Check if given string is url
     * @param s given string
     * @param schemes allow schemes (http, https, ftp...)
     * @return true if string is url base on allow schemes
     **/
    public static boolean isUrl(String s, List<String> schemes) {
        String scheme = String.join("|", schemes);
        String regex = String.format(URL_REGEX_PREFIX, scheme);
        return s.matches(regex);
    }


    /**
     * Capitalizes the first letter of the given string.
     * <p>
     * This method returns a new string with the first letter of the input string
     * converted to uppercase. The rest of the string remains unchanged. If the
     * input string is {@code null} or empty, it will return the string as-is.
     * </p>
     *
     * @param s The input string to be processed. Can be {@code null} or empty.
     * @return A new string with the first letter capitalized. If the input string
     *         is {@code null} or empty, the method returns the input string as-is.
     */
    public static String capitalizeFirstLetter(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }


    /**
     * Extracts an integer index from the given string based on a predefined regex pattern.
     *
     * @param s The input string to extract the index from.
     * @return The extracted index as an integer if the pattern matches; otherwise, returns -1.
     */
    public static int extractIndex(String s) {
        Pattern pattern = Pattern.compile(COLLECTION_ACCESS_REGEX);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches() ? Integer.parseInt(matcher.group(1)) : -1;
    }


    /**
     * Checks if the actual URL path matches the specified template path.
     * Converts path variables in the template to a regex pattern for matching.
     *
     * @param template The template path containing optional path variables.
     * @param actualPath The actual path to compare against the template.
     * @return {@code true} if the actual path matches the template; {@code false} otherwise.
     */
    public static boolean matchUrlPath(String template, String actualPath) {
        String regex = template.replaceAll(PATH_VARIABLE_REGEX_W_GROUP, "(?<$1>[^/]+)");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(actualPath);
        return matcher.matches();
    }


    /**
     * Determines if the given template contains path variables based on a predefined regex pattern.
     *
     * @param template The template path to check for path variables.
     * @return {@code true} if the template contains path variables; {@code false} otherwise.
     */
    public static boolean hasPathVariable(String template) {
        Pattern pattern = Pattern.compile(PATH_VARIABLE_REGEX);
        Matcher matcher = pattern.matcher(template);
        return matcher.matches();
    }
}
