package com.epam.fedunkiv.periodicals.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistedTitleValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistedTitle {
    String message() default "Such publisher title is already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
