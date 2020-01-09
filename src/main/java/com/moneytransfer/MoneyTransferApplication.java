package com.moneytransfer;

import com.google.inject.Guice;
import com.moneytransfer.configuration.DatabaseConfiguration;
import com.moneytransfer.configuration.JavalinConfiguration;
import com.moneytransfer.configuration.googlejuice.AOPModule;
import com.moneytransfer.configuration.googlejuice.BasicModule;
import io.vavr.control.Try;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MoneyTransferApplication {
    public static void main(String[] args) {
        Properties properties = Try.withResources(
                () -> MoneyTransferApplication.class.getClassLoader().getResourceAsStream("configuration.properties"))
                .of(MoneyTransferApplication::getProperties)
                .getOrElseThrow( ex -> new RuntimeException(ex.getMessage()));

        Guice.createInjector(new BasicModule(), new AOPModule());

        DatabaseConfiguration.initializeDatabase(properties);
        JavalinConfiguration.startJavalin(properties);
    }

    private static Properties getProperties(InputStream inputStream) throws IOException {
        Properties properties = new Properties();

        properties.load(inputStream);

        return properties;
    }
}
