package com.ahnis.servaia.user.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.ZoneId;

public class TimezoneValidator implements ConstraintValidator<ValidTimezone, String> {

    @Override
    public void initialize(ValidTimezone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String timezone, ConstraintValidatorContext context) {
        if (timezone == null) return false;
        try {
         ZoneId.of(timezone);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
