package com.serviceapp.validation.annotation;

import com.serviceapp.validation.MovieTransferObjectValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is to be placed onto <code>MovieTransferObject</code> URL fields. Checks URL's for length and
 * validity if URL is not empty
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MovieTransferObjectValidator.class)
public @interface ValidMovieTransferObjectURL {

    String message() default "{movie.url}";

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}