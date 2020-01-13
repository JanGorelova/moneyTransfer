package com.moneytransfer.configuration.googlejuice.aspect;


import com.moneytransfer.configuration.database.DatabaseConfiguration;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DB;

@Slf4j
public class ReadFromDatabaseAspect implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) {
        Try<Object> objectTry;

        if (Base.hasConnection())
            objectTry = Try.of(invocation::proceed);
        else {
            DB connection = DatabaseConfiguration.getConnection();

            objectTry = Try.of(invocation::proceed)
                    .onFailure( e -> log.error(e.getMessage()) )
                    .andFinally(connection::close);
        }

        return objectTry.get();
    }
}
