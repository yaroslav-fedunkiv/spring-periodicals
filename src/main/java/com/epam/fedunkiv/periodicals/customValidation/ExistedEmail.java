package com.epam.fedunkiv.periodicals.customValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistedEmailValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistedEmail {
    String message() default "Such email address is already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
