package com.moneytransfer.configuration.googlejuice.aspect;


import com.moneytransfer.configuration.database.DatabaseConfiguration;
import com.moneytransfer.exception.MoneyTransferException;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eclipse.jetty.http.HttpStatus;
import org.javalite.activejdbc.DB;

@Slf4j
public class TransactionAspect implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) {
        DB connection = DatabaseConfiguration.getConnection();

        return Try.of( () -> processInTransaction(invocation, connection) )
                .onFailure( e -> log.error(e.getMessage()) )
                .onFailure( e -> connection.rollbackTransaction() )
                .andFinally(connection::close)
                .getOrElseThrow( e -> new MoneyTransferException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500, e.getCause()));
    }

    private Object processInTransaction(MethodInvocation methodInvocation, DB connection) throws Throwable {
        connection.openTransaction();

        Object result = methodInvocation.proceed();

        connection.commitTransaction();

        return result;
    }
}
