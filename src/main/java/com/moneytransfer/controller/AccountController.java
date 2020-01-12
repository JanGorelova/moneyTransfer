package com.moneytransfer.controller;

import com.google.inject.Inject;
import com.moneytransfer.model.dto.entity.AccountDTO;
import com.moneytransfer.model.dto.entity.AccountTransactionDTO;
import com.moneytransfer.model.dto.request.AccountCreationDTO;
import com.moneytransfer.model.dto.request.AccountDepositDTO;
import com.moneytransfer.model.dto.request.AccountTransferDTO;
import com.moneytransfer.service.AccountService;
import com.moneytransfer.service.AccountTransactionService;
import com.moneytransfer.util.ValidatorUtil;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;

public class AccountController {
    @Inject
    private static AccountTransactionService accountTransactionService;

    @Inject
    private static AccountService accountService;

    @OpenApi(summary = "Create account", path = "/api/accounts/create", method = HttpMethod.POST,
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = AccountCreationDTO.class)}),
            responses = @OpenApiResponse(status = "200", content = {@OpenApiContent(from = AccountDTO.class)}))
    public static void create(Context context) {
        AccountCreationDTO accountCreationDTO = context.bodyAsClass(AccountCreationDTO.class);

        ValidatorUtil.validate(accountCreationDTO);

        context.json(accountService.create(accountCreationDTO));
    }

    @OpenApi(summary = "Deposit funds to account", path = "/api/accounts/deposit", method = HttpMethod.POST,
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = AccountDepositDTO.class)}),
            responses = @OpenApiResponse(status = "200", content = {@OpenApiContent(from = AccountDTO.class)}))
    public static void deposit(Context context) {
        AccountDepositDTO accountDepositDTO = context.bodyAsClass(AccountDepositDTO.class);

        ValidatorUtil.validate(accountDepositDTO);

        context.json(accountService.deposit(accountDepositDTO));
    }

    @OpenApi(summary = "Transfer funds between accounts", path = "/api/accounts/transfer", method = HttpMethod.POST,
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = AccountTransferDTO.class)}),
            responses = @OpenApiResponse(status = "200", content = {@OpenApiContent(from = AccountTransactionDTO.class)}))
    public static void transfer(Context context) {
        AccountTransferDTO transferDTO = context.bodyAsClass(AccountTransferDTO.class);

        ValidatorUtil.validate(transferDTO);

        context.json(accountService.transfer(transferDTO));
    }

    @OpenApi(summary = "Transfer funds between accounts", path = "/api/accounts/transactions", method = HttpMethod.GET,
            responses = @OpenApiResponse(status = "200", content = {@OpenApiContent(from = AccountTransactionDTO.class, isArray = true)}))
    public static void getTransactions(Context context) {
        context.json(accountTransactionService.getAll());
    }
}

