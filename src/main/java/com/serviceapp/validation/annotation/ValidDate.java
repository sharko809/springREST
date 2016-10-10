package com.serviceapp.validation.annotation;

import com.serviceapp.validation.CustomDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be placed onto <code>java.sql.Date</code> fields. Checks dates for
 * validity if date is not <code>null</code>. <code>null</code> value is allowed and considered valid
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomDateValidator.class)
public @interface ValidDate {

    String message() default "{date.invalid}";

    String pattern() default "yyyy-MM-dd";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
