package com.moneytransfer.configuration.googlejuice;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.moneytransfer.configuration.googlejuice.aspect.InTransaction;
import com.moneytransfer.configuration.googlejuice.aspect.ReadFromDatabase;
import com.moneytransfer.configuration.googlejuice.aspect.ReadFromDatabaseAspect;
import com.moneytransfer.configuration.googlejuice.aspect.TransactionAspect;

public class AOPModule extends AbstractModule {
    @Override
    protected void configure() {
        bindInterceptor(
                Matchers.any(),
                Matchers.annotatedWith(InTransaction.class),
                new TransactionAspect()
        );
        bindInterceptor(
                Matchers.any(),
                Matchers.annotatedWith(ReadFromDatabase.class),
                new ReadFromDatabaseAspect()
        );
    }
}
