package com.moneytransfer.controller;

import com.google.inject.Inject;
import com.moneytransfer.model.dto.UserCreationDTO;
import com.moneytransfer.service.UserService;
import com.moneytransfer.util.ValidatorUtil;
import io.javalin.http.Handler;

public class UserController {
    private UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public Handler create = context -> {
        UserCreationDTO userCreationDTO = context.bodyAsClass(UserCreationDTO.class);

        ValidatorUtil.validate(userCreationDTO);

        context.json(userService.create(userCreationDTO));
    };
}
