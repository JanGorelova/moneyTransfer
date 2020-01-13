package com.moneytransfer.configuration.database;

import com.moneytransfer.exception.MoneyTransferException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DB;

import javax.sql.DataSource;
import java.util.Properties;

@Getter
@Slf4j
public class DatabaseConfiguration {
    private static DataSource dataSource;
    private static String profile;

    public static void initializeDatabase(Properties properties) {
        profile = properties.getProperty("profile");
        dataSource = getHikariDataSource(properties);

        FlywayConfiguration.initializeSchema(dataSource, profile);
    }

    private static HikariDataSource getHikariDataSource(Properties properties) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(properties.getProperty("datasource.url"));
        hikariConfig.setUsername(properties.getProperty("datasource.user"));
        hikariConfig.setPassword(properties.getProperty("datasource.password"));
        hikariConfig.setMaximumPoolSize(100);

        if (profile.contains("mysql"))
            hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");

        return new HikariDataSource(hikariConfig);
    }

    public static DB getConnection() {
        return Try.ofSupplier( () -> Base.open(dataSource) )
                .onFailure(e -> log.error(e.getMessage()))
                .getOrElseThrow(e -> new MoneyTransferException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500));
    }
}
