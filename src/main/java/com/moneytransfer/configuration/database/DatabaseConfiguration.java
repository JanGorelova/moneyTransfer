package com.moneytransfer.configuration.database;

import com.moneytransfer.exception.MoneyTransferException;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DB;

import java.util.Properties;

@Getter

@Slf4j
public class DatabaseConfiguration {
    public static String password;
    public static String driver;
    public static String user;
    public static String url;

    public static void initializeDatabase(Properties properties) {
        password = properties.getProperty("datasource.password");
        driver = properties.getProperty("datasource.driver");
        user = properties.getProperty("datasource.user");
        url = properties.getProperty("datasource.url");

        FlywayConfiguration.initializeSchema(url, user, password);
    }

    public static DB getConnection() {
        return Try.ofSupplier(() -> Base.open(driver, url, user, password))
                .onFailure(e -> log.error(e.getMessage()))
                .getOrElseThrow(e -> new MoneyTransferException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500));
    }
}
