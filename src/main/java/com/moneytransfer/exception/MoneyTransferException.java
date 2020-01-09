package com.moneytransfer.exception;

import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;


public class MoneyTransferException extends RuntimeException {
    private final int status;

    public MoneyTransferException(final String message, final int status) {
        this(message, status, null);
    }

    public MoneyTransferException(final String message, final int status, final Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
