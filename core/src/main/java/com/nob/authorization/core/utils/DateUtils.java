package com.nob.authorization.core.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;

/**
 * Utility for date type, all of those class below will be treated as date type:
 * <ul>
 *     <li>{@link Date}</li>
 *     <li>{@link LocalDate}</li>
 *     <li>{@link LocalDateTime}</li>
 *     <li>{@link ZonedDateTime}</li>
 *     <li>{@link Instant}</li>
 * </ul>
 * Provide utility method for converting and manipulating date type.
 * @author Truong Ngo
 * */
public class DateUtils {

    /**
     * Prevent instantiate
     * */
    private DateUtils() {
        throw new UnsupportedOperationException("Cannot be instantiated!");
    }

    /**
     * Parse a given string into date type {@code T} with iso format
     * @param s a string represent date
     * @param targetType type of date
     * @param zoneId the time zone required
     * @return date object of type {@code T}
     * @throws IllegalArgumentException if the string or format or date type is invalid
     * */
    public static <T> T parseDate(String s, Class<T> targetType, ZoneId zoneId)  {
        try {
            if (targetType == LocalDate.class) {
                return targetType.cast(LocalDate.parse(s));
            } else if (targetType == LocalDateTime.class) {
                return targetType.cast(LocalDateTime.parse(s));
            } else if (targetType == Instant.class) {
                LocalDateTime dateTime = LocalDateTime.parse(s);
                return targetType.cast(dateTime.atZone(zoneId).toInstant());
            } else if (targetType == Date.class) {
                LocalDateTime dateTime = LocalDateTime.parse(s);
                Instant instant = dateTime.atZone(zoneId).toInstant();
                return targetType.cast(Date.from(instant));
            } else if (targetType == ZonedDateTime.class) {
                LocalDateTime dateTime = LocalDateTime.parse(s);
                return targetType.cast(dateTime.atZone(zoneId));
            } else {
                throw new IllegalArgumentException("Unsupported target type: " + targetType);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + s, e);
        }
    }

    /**
     * Parse a given string into date type {@code T}
     * @param s a string represent date
     * @param pattern date pattern
     * @param targetType type of date
     * @param zoneId the time zone required
     * @return date object of type {@code T}
     * @throws IllegalArgumentException if the string or format or date type is invalid
     * */
    public static <T> T parseDate(String s, String pattern, Class<T> targetType, ZoneId zoneId)  {
        DateTimeFormatter formatter;
        try {
            formatter = DateTimeFormatter.ofPattern(pattern);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid pattern: " + pattern, e);
        }
        try {
            if (targetType == LocalDate.class) {
                return targetType.cast(LocalDate.parse(s, formatter));
            } else if (targetType == LocalDateTime.class) {
                return targetType.cast(LocalDateTime.parse(s, formatter));
            } else if (targetType == Instant.class) {
                LocalDateTime dateTime = LocalDateTime.parse(s, formatter);
                return targetType.cast(dateTime.atZone(zoneId).toInstant());
            } else if (targetType == Date.class) {
                LocalDateTime dateTime = LocalDateTime.parse(s, formatter);
                Instant instant = dateTime.atZone(zoneId).toInstant();
                return targetType.cast(Date.from(instant));
            } else if (targetType == ZonedDateTime.class) {
                LocalDateTime dateTime = LocalDateTime.parse(s, formatter);
                return targetType.cast(dateTime.atZone(zoneId));
            } else {
                throw new IllegalArgumentException("Unsupported target type: " + targetType);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + s, e);
        }
    }

    /**
     * Parse a given string into date type {@code T} in iso format with UTC+0 zone
     * @param s a string represent date
     * @param targetType type of date
     * @return date object of type {@code T}
     * @throws IllegalArgumentException if the string or format or date type is invalid
     * */
    public static <T> T parseDateWithUTC(String s, Class<T> targetType) {
        ZoneId zoneId = ZoneOffset.UTC;
        return parseDate(s, targetType, zoneId);
    }

    /**
     * Parse a given string into date type {@code T} with UTC+0 zone
     * @param s a string represent date
     * @param pattern date pattern
     * @param targetType type of date
     * @return date object of type {@code T}
     * @throws IllegalArgumentException if the string or format or date type is invalid
     * */
    public static <T> T parseDateWithUTC(String s, String pattern, Class<T> targetType) {
        ZoneId zoneId = ZoneOffset.UTC;
        return parseDate(s, pattern, targetType, zoneId);
    }

    /**
     * Parse a given string into date type {@code T} in iso format with system zone
     * @param s a string represent date
     * @param targetType type of date
     * @return date object of type {@code T}
     * @throws IllegalArgumentException if the string or format or date type is invalid
     * */
    public static <T> T parseDateWithSystemZone(String s, Class<T> targetType) {
        ZoneId zoneId = ZoneId.systemDefault();
        return parseDate(s, targetType, zoneId);
    }

    /**
     * Parse a given string into date type {@code T} with system zone
     * @param s a string represent date
     * @param pattern date pattern
     * @param targetType type of date
     * @return date object of type {@code T}
     * @throws IllegalArgumentException if the string or format or date type is invalid
     * */
    public static <T> T parseDateWithSystemZone(String s, String pattern, Class<T> targetType) {
        ZoneId zoneId = ZoneId.systemDefault();
        return parseDate(s, pattern, targetType, zoneId);
    }

    /**
     * Parse a given timestamp into date type {@code T} with given zone id
     * @param timestamp timestamp as millisecond
     * @param targetType type of date
     * @return date object of type {@code T}
     * @throws IllegalArgumentException if the string or format or date type is invalid
     * */
    public static <T> T parseDate(Long timestamp, Class<T> targetType, ZoneId zoneId) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        if (targetType == Date.class) return targetType.cast(Date.from(instant));
        if (targetType == Instant.class) return targetType.cast(instant);
        if (targetType == LocalDate.class) return targetType.cast(instant.atZone(zoneId).toLocalDate());
        if (targetType == LocalDateTime.class) return targetType.cast(instant.atZone(zoneId).toLocalDateTime());
        if (targetType == ZonedDateTime.class) return targetType.cast(instant.atZone(zoneId));
        throw new IllegalArgumentException("Unsupported target type: " + targetType);
    }

    /**
     * Parse a given timestamp into date type {@code T} with UTC+0 zone
     * @param timestamp timestamp as millisecond
     * @param targetType type of date
     * @return date object of type {@code T}
     * @throws IllegalArgumentException if the string or format or date type is invalid
     * */
    public static <T> T parseDateWithUTC(Long timestamp, Class<T> targetType) {
        return parseDate(timestamp, targetType, ZoneOffset.UTC);
    }

    /**
     * Parse a given timestamp into date type {@code T} with UTC+0 zone
     * @param timestamp timestamp as millisecond
     * @param targetType type of date
     * @return date object of type {@code T}
     * @throws IllegalArgumentException if the string or format or date type is invalid
     * */
    public static <T> T parseDateWithSystemZone(Long timestamp, Class<T> targetType) {
        return parseDate(timestamp, targetType, ZoneId.systemDefault());
    }

    /**
     * Convert supported type date object to {@code Instant}
     * @param o the date object
     * @param zoneId zone id
     * @return value as {@code Instant}
     * @throws IllegalArgumentException if date type id not one of supported type date
     * */
    public static Instant toInstant(Object o, ZoneId zoneId) {
        if (Objects.isNull(o)) return null;
        if (o instanceof Instant) return (Instant) o;
        if (o instanceof Date) return ((Date) o).toInstant();
        if (o instanceof LocalDate) return ((LocalDate) o).atStartOfDay(zoneId).toInstant();
        if (o instanceof LocalDateTime) return ((LocalDateTime) o).atZone(zoneId).toInstant();
        if (o instanceof ZonedDateTime) return ((ZonedDateTime) o).toInstant();
        throw new IllegalArgumentException("Unsupported target type: " + o.getClass());
    }

    /**
     * Convert supported type date object with UTC zone to {@code Instant}
     * @param o the date object
     * @return value as {@code Instant}
     * @throws IllegalArgumentException if date type id not one of supported type date
     * */
    public static Instant toInstantUTC(Object o) {
        return toInstant(o, ZoneOffset.UTC);
    }

    /**
     * Convert supported type date object with system zone to {@code Instant}
     * @param o the date object
     * @return value as {@code Instant}
     * @throws IllegalArgumentException if date type id not one of supported type date
     * */
    public static Instant toInstantSystem(Object o) {
        return toInstant(o, ZoneId.systemDefault());
    }

    /**
     * Convert {@code Instant} to date object
     * @param instant instant object
     * @param targetType desire date type
     * @param zoneId zone id
     * @return value as desire date type
     * @throws IllegalArgumentException if target type is not one of support date type
     * */
    public static Object fromInstant(Instant instant, ZoneId zoneId, Class<?> targetType) {
        if (Objects.isNull(instant)) return null;
        if (targetType == Date.class) return Date.from(instant);
        if (targetType == Instant.class) return instant;
        if (targetType == LocalDate.class) return instant.atZone(zoneId).toLocalDate();
        if (targetType == LocalDateTime.class) return instant.atZone(zoneId).toLocalDateTime();
        if (targetType == ZonedDateTime.class) return instant.atZone(zoneId);
        throw new IllegalArgumentException("Unsupported target type: " + targetType);
    }

    /**
     * Convert {@code Instant} to date object in UTC+0 zone
     * @param instant instant object
     * @param targetType desire date type
     * @return value as desire date type
     * @throws IllegalArgumentException if target type is not one of support date type
     * */
    public static Object fromInstantUTC(Instant instant, Class<?> targetType) {
        ZoneId zoneId = ZoneOffset.UTC;
        return fromInstant(instant, zoneId, targetType);
    }

    /**
     * Convert {@code Instant} to date object with system zone
     * @param instant instant object
     * @param targetType desire date type
     * @return value as desire date type
     * @throws IllegalArgumentException if target type is not one of support date type
     * */
    public static Object fromInstantSystemZone(Instant instant, Class<?> targetType) {
        ZoneId zoneId = ZoneId.systemDefault();
        return fromInstant(instant, zoneId, targetType);
    }

    /**
     * Compare to date object
     * @param v1 left-hand side date object
     * @param v2 right-hand side date object
     * @return compare result
     * */
    @SuppressWarnings("all")
    public static int compare(Object v1, Object v2) {
        Objects.requireNonNull(v1);
        Objects.requireNonNull(v2);
        Instant date1 = toInstant(v1, ZoneId.systemDefault());
        Instant date2 = toInstant(v2, ZoneId.systemDefault());
        return date1.compareTo(date2);
    }
}
