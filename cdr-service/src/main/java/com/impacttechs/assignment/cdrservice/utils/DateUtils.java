package com.impacttechs.assignment.cdrservice.utils;

import org.apache.commons.lang3.time.FastDateFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {
    public static final DateTimeFormatter DTF_MMddyyyy = DateTimeFormatter.ofPattern("MMddyyyy");
    public static final DateTimeFormatter DTF_yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter DTF_yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final FastDateFormat DF_yyyyMMddHHmmss = FastDateFormat.getInstance("yyyyMMddHHmmss");
    public static final DateTimeFormatter DTF_MMM_DD = DateTimeFormatter.ofPattern("MMM dd");

    // Private constructor to prevent instantiation
    private DateUtils() {
        throw new UnsupportedOperationException();
    }

    public static Date convertLocalDateToDate(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime convertIsoInstantToLocalDateTime(String dateInISOInstantPattern) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        return LocalDateTime.parse(dateInISOInstantPattern, formatter);
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    public static Date convertLocalDateTimeToDate(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }
}
