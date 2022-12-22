package com.example.chibiotaku.util.validators;

import com.example.chibiotaku.util.annotations.NotNegativeOrZero;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotNegativeOrZeroValidator implements ConstraintValidator<NotNegativeOrZero, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(value == null){
            return  false;
        }
        return value > 0;
    }
}
