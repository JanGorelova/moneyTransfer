package com.moneytransfer.configuration;

import org.javalite.activejdbc.Base;

import java.util.Properties;

public class DatabaseConfiguration {
    public static void initializeDatabase(Properties properties) {
        final String password = properties.getProperty("datasource.password");
        final String driver = properties.getProperty("datasource.driver");
        final String user = properties.getProperty("datasource.user");
        final String url = properties.getProperty("datasource.url");

        Base.open(driver, url, user, password);
    }
}
