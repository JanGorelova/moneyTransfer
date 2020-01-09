package com.moneytransfer.service;

import com.google.inject.Inject;
import com.moneytransfer.configuration.googleaspect.InTransaction;
import com.moneytransfer.entity.Account;
import com.moneytransfer.entity.AccountTransaction;


public class AccountService {
    private final AccountTransactionService accountTransactionService;
    private final UserService userService;

    @Inject
    public AccountService(AccountTransactionService accountTransactionService, UserService userService) {
        this.accountTransactionService = accountTransactionService;
        this.userService = userService;
    }

    public Account create() {
        return null;
    }

    @InTransaction
    public AccountTransaction transfer() {
        return null;
    }
}
