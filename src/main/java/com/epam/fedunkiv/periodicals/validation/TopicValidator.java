package com.epam.fedunkiv.periodicals.validation;

import com.epam.fedunkiv.periodicals.model.Topics;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class TopicValidator implements ConstraintValidator<TopicValid, String> {
    @Override
    public void initialize(TopicValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(String topic, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Topics.valueOf(topic);
            return true;
        } catch (IllegalArgumentException e){
            return false;
        }
    }
}
