package com.ahnis.servaia.user.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimezoneValidator.class) // Link to the validator
public @interface ValidTimezone {
    String message() default "Timezone must be a valid IANA timezone (e.g., Asia/Kolkata, Europe/London,etc)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
