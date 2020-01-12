package com.moneytransfer.util;

import io.vavr.control.Try;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class DateUtil {
    public static LocalDateTime getDateUpdated(String dateUpdated) {
        return LocalDateTime.parse(dateUpdated);
    }

    public static LocalDateTime getDateCreated(String dateCreated) {
        Optional<String> date = Optional.ofNullable(dateCreated);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return date.map(d ->
                Try.of(() -> LocalDateTime.ofInstant(format.parse(dateCreated).toInstant(), ZoneId.systemDefault())).get())
                .orElse(LocalDateTime.now());
    }
}
