package com.moneytransfer.util;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DateUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String getDateUpdated(String dateUpdated) {
        return parseAndFormat(dateUpdated);
    }

    public static String getDateCreated(String dateCreated) {
        Optional<String> date = Optional.ofNullable(dateCreated);

        return date.map(DateUtil::parseAndFormat).orElse(formatter.format(LocalDateTime.now()));
    }

    private static String parseAndFormat(String date) {
        return formatter.format(LocalDateTime.parse(date));
    }
}
