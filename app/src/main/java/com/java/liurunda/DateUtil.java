package com.java.liurunda;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {
    public static String getTextFormattedDate(ZonedDateTime dateTime) {
        ZonedDateTime point = dateTime.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime current = ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault());
        if (point.toLocalDate().equals(current.toLocalDate())) {
            return point.toLocalTime().format(DateTimeFormatter.ofPattern("H:mm", Locale.getDefault()));
        } else if (point.toLocalDate().getYear() == current.toLocalDate().getYear()) {
            return point.toLocalDateTime().format(DateTimeFormatter.ofPattern("M/d H:mm", Locale.getDefault()));
        } else {
            return point.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy M/d H:mm", Locale.getDefault()));
        }
    }
}
