package com.moneytransfer.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidatorUtil {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validate(T item) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(item);

        if (constraintViolations.size() > 1) {
         //todo
        }
    }

}
