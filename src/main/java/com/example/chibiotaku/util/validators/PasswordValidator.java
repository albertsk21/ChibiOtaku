package com.example.chibiotaku.util.validators;

import com.example.chibiotaku.util.annotations.IsPasswordValid;
import com.example.chibiotaku.util.annotations.UniqueEmail;
import org.springframework.security.core.parameters.P;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator  implements ConstraintValidator<IsPasswordValid,String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {



        String containsOneLowerCaseRegex = "[a-z]";
        String containsOneUpperCaseRegex = "[A-Z]";
        String containsOneDigits = "[0-9]";


        Pattern pattern = Pattern.compile(containsOneLowerCaseRegex);

        Matcher matcher = pattern.matcher(value);



        if (!matcher.find()){
            return false;
        }


        pattern = Pattern.compile(containsOneUpperCaseRegex);
        matcher = pattern.matcher(value);

        if (!matcher.find()){
            return false;
        }

        pattern = Pattern.compile(containsOneDigits);
        matcher = pattern.matcher(value);

        if (!matcher.find()){
            return false;
        }
        return value.length() >= 8 && value.length() <= 20;
    }
}
