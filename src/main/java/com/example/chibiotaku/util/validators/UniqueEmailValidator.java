package com.example.chibiotaku.util.validators;

import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.repo.UserRepository;
import com.example.chibiotaku.util.annotations.UniqueEmail;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail,String> {
    private UserRepository userRepository;

    public UniqueEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<UserEntity> user = this.userRepository.findUserEntityByEmail(value);
        return user.isEmpty();
    }
}
