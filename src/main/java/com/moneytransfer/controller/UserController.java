package com.moneytransfer.controller;

import com.google.inject.Inject;
import com.moneytransfer.model.dto.entity.UserDTO;
import com.moneytransfer.model.dto.request.UserCreationDTO;
import com.moneytransfer.service.UserService;
import com.moneytransfer.util.ValidatorUtil;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;

public class UserController {
    @Inject
    private static UserService userService;

    @OpenApi(summary = "Create user", path = "/api/users/create", method = HttpMethod.POST,
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = UserCreationDTO.class)}),
            responses = @OpenApiResponse(status = "200", content = {@OpenApiContent(from = UserDTO.class)}))
    public static void create(Context context)  {
        UserCreationDTO userCreationDTO = context.bodyAsClass(UserCreationDTO.class);

        ValidatorUtil.validate(userCreationDTO);

        context.json(userService.create(userCreationDTO));
    }
}
