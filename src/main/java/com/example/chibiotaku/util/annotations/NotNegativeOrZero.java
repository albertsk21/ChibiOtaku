package com.example.chibiotaku.util.annotations;

import com.example.chibiotaku.util.validators.NotNegativeOrZeroValidator;
import com.example.chibiotaku.util.validators.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.METHOD})
@Constraint(validatedBy = NotNegativeOrZeroValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNegativeOrZero {
    String message() default "Invalid field";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
