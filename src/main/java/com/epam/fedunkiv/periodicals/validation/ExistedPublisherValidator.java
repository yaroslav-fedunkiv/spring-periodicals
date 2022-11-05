package com.epam.fedunkiv.periodicals.validation;

import com.epam.fedunkiv.periodicals.services.PublisherService;
import com.epam.fedunkiv.periodicals.services.UserService;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.NoSuchElementException;

public class ExistedPublisherValidator implements
        ConstraintValidator<ExistedPublisher, String> {
    @Resource
    private PublisherService publisherService;

    @Override
    public void initialize(ExistedPublisher constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx){
        try {
            publisherService.getByTitle(s).get();
            return true;
        } catch (NoSuchElementException e){
            return false;
        }
//        return !publisherService.getByTitle(s).isEmpty();
    }
}
