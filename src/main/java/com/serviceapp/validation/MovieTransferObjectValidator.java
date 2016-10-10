package com.serviceapp.validation;

import com.serviceapp.validation.annotation.ValidMovieTransferObjectURL;
import org.apache.commons.validator.routines.UrlValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Class for validating <code>MovieTransferObject's</code> URL fields. If user leaves URL field empty during
 * movie creation, validation completes successfully. Otherwise validator checks for URL validity.
 */
public class MovieTransferObjectValidator implements ConstraintValidator<ValidMovieTransferObjectURL, String> {

    private int min;
    private int max;

    /**
     * Initializes this validator with minimal and maximal values (if none - use defaults)
     *
     * @param validMovieTransferObjectURL <code>ValidMovieTransferObjectURL</code> annotation
     * @see ValidMovieTransferObjectURL
     */
    @Override
    public void initialize(ValidMovieTransferObjectURL validMovieTransferObjectURL) {
        this.min = validMovieTransferObjectURL.min();
        this.max = validMovieTransferObjectURL.max();
    }

    /**
     * Validates URL. If URL is <code>null</code> - constraint considered satisfied. Otherwise URL validation is
     * preformed with apache validator and length check
     *
     * @param url                        URL to validate
     * @param constraintValidatorContext context in which the constraint is evaluated
     * @return <code>false</code> if <code>url</code> does not pass the constraint
     */
    @Override
    public boolean isValid(String url, ConstraintValidatorContext constraintValidatorContext) {

        UrlValidator urlValidator = new UrlValidator();
        StringBuilder response = new StringBuilder();

        if (url != null) {
            if (url.isEmpty()) {
                return true;
            } else {
                if (url.length() < min || url.length() > max) {
                    response.append("{movie.url.size}\n");
                } else if (!urlValidator.isValid(url)) {
                    response.append("{movie.url}\n");
                }
            }
        } else {
            return true;
        }

        if (response.toString().isEmpty()) {
            return true;
        } else {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(response.toString())
                    .addConstraintViolation();
        }

        return false;

    }

}