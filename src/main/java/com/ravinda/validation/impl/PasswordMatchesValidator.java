package com.ravinda.validation.impl;

import com.ravinda.dto.UserDto;
import com.ravinda.validation.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserDto> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(UserDto obj, ConstraintValidatorContext context) {
        return obj.getPassword().equals(obj.getMatchingPassword());
    }
}
