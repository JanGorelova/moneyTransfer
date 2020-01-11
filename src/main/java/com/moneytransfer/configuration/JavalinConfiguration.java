package com.moneytransfer.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.moneytransfer.controller.AccountController;
import com.moneytransfer.controller.UserController;
import com.moneytransfer.exception.MoneyTransferException;
import com.moneytransfer.model.dto.entity.ExceptionDTO;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Properties;

import static io.javalin.apibuilder.ApiBuilder.*;

@Slf4j
public class JavalinConfiguration {
    public static void startJavalin(Properties properties) {
        Javalin app = Javalin.create(config -> {
            config.enableDevLogging();
            config.registerPlugin(new OpenApiPlugin(configureSwagger()));
            config.defaultContentType = "application/json";
        });

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
                    post("/transfer", AccountController::transfer);
                    post("/create", AccountController::create);
                    post("/deposit", AccountController::deposit);
                    get("/transactions", AccountController::getTransactions);
                });
                path("/users", () -> {
                    post("/create", UserController::create);
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
        ObjectMapper objectMapper = JavalinJackson.getObjectMapper();

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private static OpenApiOptions configureSwagger() {
        return new OpenApiOptions(new Info().version("1.0").description("Money transfer application"))
                .activateAnnotationScanningFor("com.moneytransfer.controller")
                .path("/swagger-documents")
                .swagger(new SwaggerOptions("/swagger").title("Swagger Documentation"))
                .defaultDocumentation(doc -> {
                    doc.json("500", ExceptionDTO.class);
                    doc.json("406", ExceptionDTO.class);
        });
    }
}
