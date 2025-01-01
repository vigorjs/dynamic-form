package com.virgo.dynamic_form.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {
    public static LocalDateTime convertStringToLocalDateTime(String timestamp, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(timestamp, formatter);
    }

    public static String convertLocalDateTimeToString(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    // Method untuk mengubah Integer menjadi LocalDateTime
    public static LocalDateTime convertIntegerToLocalDateTime(Integer dateTimeInt) {
        if (dateTimeInt == null) {
            return null;
        }
        String dateTimeString = dateTimeInt.toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    // Method untuk mengubah LocalDateTime menjadi Integer
    public static Integer convertLocalDateTimeToInteger(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return (int) dateTime.toEpochSecond(ZoneOffset.UTC);
    }
    public static Long convertDateToUnix(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return date.getTime();
    }
}
