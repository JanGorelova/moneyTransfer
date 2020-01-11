package com.moneytransfer.configuration.database;

import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

public class FlywayConfiguration {
    public static void initializeSchema(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();

        flyway.clean();

        flyway.repair();

        flyway.migrate();
    }
}
