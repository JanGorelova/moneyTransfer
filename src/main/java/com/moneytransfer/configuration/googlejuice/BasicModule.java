package com.moneytransfer.configuration.googlejuice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.moneytransfer.configuration.JavalinConfiguration;
import com.moneytransfer.controller.AccountController;
import io.vavr.control.Try;
import com.moneytransfer.service.AccountService;
import com.moneytransfer.service.AccountTransactionService;
import com.moneytransfer.service.UserService;


public class BasicModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountTransactionService.class).toInstance(new AccountTransactionService());
        bind(UserService.class).toInstance(new UserService());
        bind(AccountService.class).toConstructor(
                Try.of( () -> AccountService.class.getConstructor(AccountTransactionService.class, UserService.class) )
                        .getOrElseThrow(ex -> new RuntimeException(ex.getMessage())))
                .in(Scopes.SINGLETON);

        bind(AccountController.class).toConstructor(
                Try.of( () -> AccountController.class.getConstructor(AccountService.class) )
                        .getOrElseThrow(ex -> new RuntimeException(ex.getMessage())))
                .in(Scopes.SINGLETON);

        requestStaticInjection(JavalinConfiguration.class);
    }
}
