package com.epam.fedunkiv.periodicals.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistedUserValidator.class)
@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistedUser {
    String message() default "Such a user isn't exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
