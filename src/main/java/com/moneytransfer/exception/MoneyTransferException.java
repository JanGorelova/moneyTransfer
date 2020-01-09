package com.moneytransfer.exception;

public class MoneyTransferException extends RuntimeException {
    private final int status;

    public MoneyTransferException(final String message, final int status) {
        this(message, status, null);
    }

    public MoneyTransferException(final String message, final int status, final Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
