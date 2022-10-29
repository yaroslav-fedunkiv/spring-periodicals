package com.epam.fedunkiv.periodicals.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatchValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatch {
        String message() default "Password and confirm password don't match";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
}
