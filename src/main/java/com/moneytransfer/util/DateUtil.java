package com.moneytransfer.util;

import java.time.LocalDateTime;
import java.util.Optional;

public class DateUtil {
    public static LocalDateTime getDateUpdated(String dateUpdated) {
        return LocalDateTime.parse(dateUpdated);
    }

    public static LocalDateTime getDateCreated(String dateCreated) {
        Optional<String> date = Optional.ofNullable(dateCreated);

        return date.map(LocalDateTime::parse).orElse(LocalDateTime.now());
    }
}
