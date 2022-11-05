package com.epam.fedunkiv.periodicals.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistedPublisherValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistedPublisher {
    String message() default "Such a publisher isn't exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
