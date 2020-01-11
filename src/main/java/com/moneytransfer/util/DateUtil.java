package com.moneytransfer.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DateUtil {
    public static LocalDateTime getDateUpdated(String dateUpdated) {
        return LocalDateTime.parse(dateUpdated);
    }

    public static LocalDateTime getDateCreated(String dateCreated) {
        Optional<String> date = Optional.ofNullable(dateCreated);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        return date.map(d -> LocalDateTime.parse(d, dateTimeFormatter)).orElse(LocalDateTime.now());
    }
}
