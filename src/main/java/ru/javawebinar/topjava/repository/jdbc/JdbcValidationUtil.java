package ru.javawebinar.topjava.repository.jdbc;

import javax.validation.*;
import java.util.Set;

public class JdbcValidationUtil<T> {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    protected void validate(T itemForValidation) {
        Set<ConstraintViolation<T>> violations = validator.validate(itemForValidation);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
