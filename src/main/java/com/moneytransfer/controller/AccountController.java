package com.moneytransfer.controller;

import com.google.inject.Inject;
import com.moneytransfer.model.dto.AccountCreationDTO;
import com.moneytransfer.model.dto.TransferDTO;
import com.moneytransfer.service.AccountService;
import com.moneytransfer.util.ValidatorUtil;
import io.javalin.http.Handler;

public class AccountController {
    private AccountService accountService;

    @Inject
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public Handler transfer = context -> {
        TransferDTO transferDTO = context.bodyAsClass(TransferDTO.class);

        ValidatorUtil.validate(transferDTO);

        context.json(accountService.transfer(transferDTO));
    };

    public Handler create = context -> {
        AccountCreationDTO accountCreationDTO = context.bodyAsClass(AccountCreationDTO.class);

        ValidatorUtil.validate(accountCreationDTO);

        context.json(accountService.create(accountCreationDTO));
    };
}
