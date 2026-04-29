package com.java_api.infra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UrlValidator.class)
@Documented
public @interface ValidUrl {
    String message() default "URL not found";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
