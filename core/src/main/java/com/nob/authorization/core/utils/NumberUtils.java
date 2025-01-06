package com.nob.authorization.core.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utility for number
 * @author Truong Ngo
 * @version 1.0
 * */
public class NumberUtils {

     /**
     * Decimal number regex
     * */
    public static final String DECIMAL_NUMBER_REGEX = "^[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?$";

    /**
     * Integer converter, ignored invalid string input
     * */
    public static final Function<String, Integer> INTEGER_CONVERTER = Integer::valueOf;

    /**
     * Long converter, ignored invalid string input
     * */
    public static final Function<String, Long> LONG_CONVERTER = Long::valueOf;

    /**
     * Double converter, ignored invalid string input
     * */
    public static final Function<String, Double> DOUBLE_CONVERTER = Double::valueOf;

    /**
     * Float converter, ignored invalid string input
     * */
    public static final Function<String, Float> FLOAT_CONVERTER = Float::valueOf;

    /**
     * Check if string is decimal number
     * @param s string to check
     * @return true if string is represent decimal number otherwise false
     * */
    public static boolean isDecimalNumber(String s) {
        return Objects.nonNull(s) && s.matches(DECIMAL_NUMBER_REGEX);
    }

    /**
     * Check if given string is of {@link T} number type
     * @param s string to check
     * @param converter converter of {@code String} to {@code T} type
     * @return true if the string is convertable to T type, otherwise false
     * */
    public static  <T extends Number> boolean isNumberOf(String s, Function<String, T> converter) {
        if (!isDecimalNumber(s)) {
            return false;
        }
        try {
            converter.apply(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if given string is {@code Integer}
     * @param s string to check
     * @return true if the string is convertable to {@code Integer}, otherwise false
     * */
    public static boolean isInteger(String s) {
        return isNumberOf(s, INTEGER_CONVERTER);
    }

    /**
     * Check if given string is {@code Long}
     * @param s string to check
     * @return true if the string is convertable to {@code Long}, otherwise false
     * */
    public static boolean isLong(String s) {
        return isNumberOf(s, LONG_CONVERTER);
    }

    /**
     * Check if given string is {@code Float}
     * @param s string to check
     * @return true if the string is convertable to {@code Float}, otherwise false
     * */
    public static boolean isFloat(String s) {
        return isNumberOf(s, FLOAT_CONVERTER);
    }

    /**
     * Check if given string is {@code Double}
     * @param s string to check
     * @return true if the string is convertable to {@code Double}, otherwise false
     * */
    public static boolean isDouble(String s) {
        return isNumberOf(s, DOUBLE_CONVERTER);
    }

    /**
     * Create integer base on given string
     * @param s string that need convert to {@code Integer}
     * @return an {@code Integer} if the string is valid and
     * value is not out of integer range otherwise false
     * */
    public Integer createInteger(String s) {
        return isInteger(s) ? INTEGER_CONVERTER.apply(s) : null;
    }

    /**
     * Create integer base on given string
     * @param s string that need convert to {@code Integer}
     * @param defaultVal default value
     * @return an {@code Integer} if the string is valid and
     * value is not out of integer range otherwise default value
     * */
    public Integer createIntegerOrElse(String s, Integer defaultVal) {
        return isInteger(s) ? INTEGER_CONVERTER.apply(s) : defaultVal;
    }

    /**
     * Create integer base on given string
     * @param s string that need convert to {@code Integer}
     * @return an {@code Integer} if the string is valid and
     * value is not out of integer range;
     * @throws X when string is invalid or the value is out of integer range
     * */
    public <X extends Throwable> Integer createIntegerOrElseThrow(String s, Supplier<X> exSupplier) throws X {
        if (isInteger(s)) return INTEGER_CONVERTER.apply(s);
        else throw exSupplier.get();
    }

    /**
     * Create long base on given string
     * @param s string that need convert to {@code Long}
     * @return an {@code Long} if the string is valid and
     * value is not out of long range otherwise false
     * */
    public Long createLong(String s) {
        return isLong(s) ? LONG_CONVERTER.apply(s) : null;
    }

    /**
     * Create long base on given string
     * @param s string that need convert to {@code Long}
     * @param defaultVal default value
     * @return an {@code Long} if the string is valid and
     * value is not out of long range otherwise default value
     * */
    public Long createLongOrElse(String s, Long defaultVal) {
        return isLong(s) ? LONG_CONVERTER.apply(s) : defaultVal;
    }

    /**
     * Create long base on given string
     * @param s string that need convert to {@code Long}
     * @return an {@code Long} if the string is valid and
     * value is not out of long range;
     * @throws X when string is invalid or the value is out of long range
     * */
    public <X extends Throwable> Long createLongOrElseThrow(String s, Supplier<X> exSupplier) throws X {
        if (isLong(s)) return LONG_CONVERTER.apply(s);
        else throw exSupplier.get();
    }

    /**
     * Create double base on given string
     * @param s string that need convert to {@code Double}
     * @return an {@code Double} if the string is valid and
     * value is not out of double range otherwise false
     * */
    public Double createDouble(String s) {
        return isDouble(s) ? DOUBLE_CONVERTER.apply(s) : null;
    }

    /**
     * Create double base on given string
     * @param s string that need convert to {@code Double}
     * @param defaultVal default value
     * @return an {@code Double} if the string is valid and
     * value is not out of double range otherwise default value
     * */
    public Double createDoubleOrElse(String s, Double defaultVal) {
        return isDouble(s) ? DOUBLE_CONVERTER.apply(s) : defaultVal;
    }

    /**
     * Create double base on given string
     * @param s string that need convert to {@code Double}
     * @return an {@code Double} if the string is valid and
     * value is not out of double range;
     * @throws X when string is invalid or the value is out of double range
     * */
    public <X extends Throwable> Double createDoubleOrElseThrow(String s, Supplier<X> exSupplier) throws X {
        if (isDouble(s)) return DOUBLE_CONVERTER.apply(s);
        else throw exSupplier.get();
    }

    /**
     * Create float base on given string
     * @param s string that need convert to {@code Float}
     * @return an {@code Float} if the string is valid and
     * value is not out of float range otherwise false
     * */
    public Float createFloat(String s) {
        return isFloat(s) ? FLOAT_CONVERTER.apply(s) : null;
    }

    /**
     * Create float base on given string
     * @param s string that need convert to {@code Float}
     * @param defaultVal default value
     * @return an {@code Float} if the string is valid and
     * value is not out of float range otherwise default value
     * */
    public Float createFloatOrElse(String s, Float defaultVal) {
        return isFloat(s) ? FLOAT_CONVERTER.apply(s) : defaultVal;
    }

    /**
     * Create float base on given string
     * @param s string that need convert to {@code Float}
     * @return an {@code Float} if the string is valid and
     * value is not out of double range;
     * @throws X when string is invalid or the value is out of float range
     * */
    public <X extends Throwable> Float createFloatOrElseThrow(String s, Supplier<X> exSupplier) throws X {
        if (isFloat(s)) return FLOAT_CONVERTER.apply(s);
        else throw exSupplier.get();
    }

    public static int compare(Number lhs, Number rhs) {
        BigDecimal ld = new BigDecimal(lhs.toString());
        BigDecimal rd = new BigDecimal(rhs.toString());
        return ld.compareTo(rd);
    }

    public static Number toNumber(Class<?> numberType, String s) {
        if (Objects.isNull(numberType)) return null;
        if (numberType == Byte.class) return Byte.valueOf(s);
        if (numberType == Short.class) return Short.valueOf(s);
        if (numberType == Integer.class) return Integer.valueOf(s);
        if (numberType == Long.class) return Long.valueOf(s);
        if (numberType == BigInteger.class) return new BigInteger(s);
        if (numberType == Float.class) return Float.valueOf(s);
        if (numberType == Double.class) return Double.valueOf(s);
        if (numberType == BigDecimal.class) return new BigDecimal(s);
        throw new IllegalArgumentException("Cannot convert " + s + " to " + numberType);
    }
}
