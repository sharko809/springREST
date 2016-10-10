package com.serviceapp.validation.annotation;

import com.serviceapp.validation.UserTransferObjectValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be placed onto <code>UserTransferObject</code> password field. Validates password if it is
 * not empty.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserTransferObjectValidator.class)
public @interface ValidUserTransferObjectPassword {

    String message() default "{user.invalid}";

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}