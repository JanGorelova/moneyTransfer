package com.moneytransfer.util;

import com.moneytransfer.exception.MoneyTransferException;
import org.eclipse.jetty.http.HttpStatus;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidatorUtil {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validate(T item) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(item);

        if (!constraintViolations.isEmpty()) {
            String message = constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(";"));

            throw new MoneyTransferException(message, HttpStatus.BAD_REQUEST_400);
        }
    }

}
