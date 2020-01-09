package com.moneytransfer.configuration.googleaspect;


import com.moneytransfer.exception.MoneyTransferException;
import io.vavr.control.Try;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eclipse.jetty.http.HttpStatus;
import org.javalite.activejdbc.Base;

public class TransactionAspect implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return Try.of( () -> processInTransaction(invocation) )
                .onFailure( e -> Base.rollbackTransaction() )
                .getOrElseThrow( e -> new MoneyTransferException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500, e.getCause()));
    }

    private Object processInTransaction(MethodInvocation methodInvocation) throws Throwable {
        Base.openTransaction();

        Object result = methodInvocation.proceed();

        Base.commitTransaction();

        return result;
    }
}
