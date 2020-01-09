package com.moneytransfer.controller;

import com.google.inject.Inject;
import com.moneytransfer.dto.TransferDTO;
import com.moneytransfer.util.ValidatorUtil;
import io.javalin.http.Handler;
import com.moneytransfer.service.AccountService;

public class AccountController {
    private AccountService accountService;

    @Inject
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public Handler transfer = context -> {
        TransferDTO transferDTO = context.bodyAsClass(TransferDTO.class);

//        throw new NullPointerException("lalal");

//        ValidatorUtil.validate(transferDTO);

        accountService.transfer();
    };
}
