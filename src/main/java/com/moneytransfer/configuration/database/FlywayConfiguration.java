package com.moneytransfer.configuration.database;

import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

public class FlywayConfiguration {
    public static void initializeSchema(DataSource dataSource, String profile) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();

        if (profile.contains("test"))
            flyway.clean();

        flyway.repair();

        flyway.migrate();
    }
}
