package com.epam.fedunkiv.periodicals.customValidation;

import com.epam.fedunkiv.periodicals.services.UserService;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistedEmailValidator implements
        ConstraintValidator<ExistedEmail, String> {
    @Resource
    private UserService userService;

    @Override
    public void initialize(ExistedEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx){
        return userService.getByEmail(s).isEmpty();
    }
}
