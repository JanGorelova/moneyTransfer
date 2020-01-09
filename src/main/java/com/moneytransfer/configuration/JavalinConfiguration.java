package com.moneytransfer.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.google.inject.Inject;
import com.moneytransfer.controller.AccountController;
import com.moneytransfer.controller.UserController;
import com.moneytransfer.exception.MoneyTransferException;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.plugin.json.JavalinJson;
import io.javalin.plugin.json.ToJsonMapper;
import io.vavr.control.Try;
import org.eclipse.jetty.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.Properties;

import static io.javalin.apibuilder.ApiBuilder.*;

public class JavalinConfiguration {
    @Inject
    private static AccountController accountController;

    @Inject
    private static UserController userController;

    public static void startJavalin(Properties properties) {
        Javalin app = Javalin.create(JavalinConfig::enableDevLogging);

        configureEndpoints(app);
        configureExceptionHandling(app);

        int port = Integer.parseInt(properties.getProperty("application.port"));
        app.start(port);
    }

    private static void configureEndpoints(Javalin app) {
        app.routes(() -> {
            path("/api", () -> {
                path("/accounts", () -> {
                    post("/transfer", accountController.transfer);
                    post("/create", accountController.create);
                });
                path("/users", () -> {
                    post("/create", userController.create);
                });
            });
        });
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
