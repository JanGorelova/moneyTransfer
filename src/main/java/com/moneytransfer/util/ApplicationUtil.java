package com.moneytransfer.util;

import com.google.inject.Guice;
import com.moneytransfer.MoneyTransferApplication;
import com.moneytransfer.configuration.JavalinConfiguration;
import com.moneytransfer.configuration.database.DatabaseConfiguration;
import com.moneytransfer.configuration.googlejuice.AOPModule;
import com.moneytransfer.configuration.googlejuice.BasicModule;
import io.javalin.Javalin;
import io.vavr.control.Try;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationUtil {
    public static Javalin start(String propertiesName) {
        Properties properties = Try.withResources(
                () -> MoneyTransferApplication.class.getClassLoader().getResourceAsStream(propertiesName))
                .of(ApplicationUtil::getProperties)
                .getOrElseThrow( ex -> new RuntimeException(ex.getMessage()));

        Guice.createInjector(new BasicModule(), new AOPModule());

        DatabaseConfiguration.initializeDatabase(properties);
        return JavalinConfiguration.startJavalin(properties);
    }

    private static Properties getProperties(InputStream inputStream) throws IOException {
        Properties properties = new Properties();

        properties.load(inputStream);

        return properties;
    }
}
