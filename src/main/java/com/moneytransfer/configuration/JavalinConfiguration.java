package com.moneytransfer.configuration;

import com.google.inject.Inject;
import com.moneytransfer.controller.AccountController;
import com.moneytransfer.exception.MoneyTransferException;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Properties;

public class JavalinConfiguration {
    @Inject
    private static AccountController accountController;

    public static void startJavalin(Properties properties) {
        Javalin app = Javalin.create(JavalinConfig::enableDevLogging);

        configureEndpoints(app);
        configureExceptionHandling(app);

        int port = Integer.parseInt(properties.getProperty("application.port"));
        app.start(port);
    }

    private static void configureEndpoints(Javalin app) {
        app.post("/transfer", accountController.transfer);
    }

    private static void configureExceptionHandling(Javalin app) {
        app.exception(MoneyTransferException.class, (e, context) -> {
            context.status(HttpStatus.NOT_ACCEPTABLE_406);
            context.json(e);
        });

        app.exception(Exception.class, (e, context) -> {
            context.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            context.json(e);
        });
    }
}
