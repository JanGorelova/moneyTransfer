package com.moneytransfer.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.google.inject.Inject;
import com.moneytransfer.controller.AccountController;
import com.moneytransfer.controller.UserController;
import com.moneytransfer.exception.MoneyTransferException;
import com.moneytransfer.model.dto.entity.ExceptionDTO;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.plugin.json.JavalinJson;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Properties;

import static io.javalin.apibuilder.ApiBuilder.*;

@Slf4j
public class JavalinConfiguration {
    @Inject
    private static AccountController accountController;

    @Inject
    private static UserController userController;

    public static void startJavalin(Properties properties) {
        Javalin app = Javalin.create(JavalinConfig::enableDevLogging);

        configureEndpoints(app);
        configureExceptionHandling(app);
        configureToJsonMapper();

        int port = Integer.parseInt(properties.getProperty("application.port"));
        app.start(port);
    }

    private static void configureEndpoints(Javalin app) {
        app.routes(() -> {
            path("/api", () -> {
                path("/accounts", () -> {
                    post("/transfer", accountController.transfer);
                    post("/create", accountController.create);
                    get("/transactions", accountController.getTransactions);
                });
                path("/users", () -> {
                    post("/create", userController.create);
                });
            });
        });
    }

    private static void configureExceptionHandling(Javalin app) {
        app.exception(MoneyTransferException.class, (e, context) -> {
            context.status(e.getStatus());
            context.json(ExceptionDTO.getExceptionDTO(e));
        });

        app.exception(Exception.class, (e, context) -> {
            context.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            context.json(ExceptionDTO.getExceptionDTO(e));
        });
    }

    private static void configureToJsonMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JSR310Module());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        JavalinJson.setToJsonMapper( object -> getJson(objectMapper, object));
    }

    private static String getJson(ObjectMapper objectMapper, Object object) {
        return Try.of( () -> objectMapper.writeValueAsString(object) )
                .onFailure(ex -> log.error(ex.getMessage()))
                .getOrElseThrow(ex -> new MoneyTransferException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500));
    }
}
