package com.moneytransfer.configuration.database;

import org.flywaydb.core.Flyway;

public class FlywayConfiguration {
    public static void initializeSchema(String url, String user, String password) {
        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .load();

        flyway.repair();

        flyway.migrate();
    }
}
