package com.moneytransfer.configuration.googlejuice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.moneytransfer.controller.AccountController;
import com.moneytransfer.controller.UserController;
import com.moneytransfer.service.AccountService;
import com.moneytransfer.service.AccountTransactionService;
import com.moneytransfer.service.UserService;
import io.vavr.control.Try;


public class BasicModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountTransactionService.class).toConstructor(
                Try.of(AccountTransactionService.class::getConstructor)
                        .getOrElseThrow(ex -> new RuntimeException(ex.getMessage())))
                .in(Scopes.SINGLETON);

        bind(UserService.class).toConstructor(
                Try.of(UserService.class::getConstructor)
                        .getOrElseThrow(ex -> new RuntimeException(ex.getMessage())))
                .in(Scopes.SINGLETON);

        bind(AccountService.class).toConstructor(
                Try.of( () -> AccountService.class.getConstructor(AccountTransactionService.class, UserService.class) )
                        .getOrElseThrow(ex -> new RuntimeException(ex.getMessage())))
                .in(Scopes.SINGLETON);

        requestStaticInjection(UserController.class);
        requestStaticInjection(AccountController.class);
    }
}
