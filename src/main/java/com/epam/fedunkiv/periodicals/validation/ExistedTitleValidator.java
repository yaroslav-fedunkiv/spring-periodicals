package com.epam.fedunkiv.periodicals.validation;

import com.epam.fedunkiv.periodicals.exceptions.NoSuchPublisherException;
import com.epam.fedunkiv.periodicals.exceptions.NoSuchUserException;
import com.epam.fedunkiv.periodicals.services.PublisherService;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistedTitleValidator implements
        ConstraintValidator<ExistedTitle, String> {
    @Resource
    private PublisherService publisherService;

    @Override
    public void initialize(ExistedTitle constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx){
        try{
            publisherService.getByTitle(s);
            return false;
        } catch (NullPointerException | NoSuchPublisherException e){
            return true;
        }
    }
}
