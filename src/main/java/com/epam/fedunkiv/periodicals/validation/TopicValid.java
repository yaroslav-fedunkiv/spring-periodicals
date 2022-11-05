package com.epam.fedunkiv.periodicals.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TopicValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TopicValid {
    String message() default "Topic doesn't valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
