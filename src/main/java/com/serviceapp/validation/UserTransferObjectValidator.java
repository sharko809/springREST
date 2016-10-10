package com.serviceapp.validation;

import com.serviceapp.validation.annotation.ValidMovieTransferObjectURL;
import com.serviceapp.validation.annotation.ValidUserTransferObjectPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Class for validating <code>UserTransferObject's</code> password field. If user leaves password field empty during
 * account info update, password will stay the same and validation completes successfully. Otherwise validator checks
 * for password length.
 */
public class UserTransferObjectValidator implements ConstraintValidator<ValidUserTransferObjectPassword, String> {

    private int min;
    private int max;

    /**
     * Initializes this validator with minimal and maximal values (if none - use defaults)
     *
     * @param validUserTransferObjectPassword <code>ValidUserTransferObjectPassword</code> annotation
     * @see ValidMovieTransferObjectURL
     */
    @Override
    public void initialize(ValidUserTransferObjectPassword validUserTransferObjectPassword) {
        this.min = validUserTransferObjectPassword.min();
        this.max = validUserTransferObjectPassword.max();
    }

    /**
     * Validates password. If password if <code>null</code> - constraint considered satisfied. Otherwise checks for
     * password length
     *
     * @param password                   password to validate
     * @param constraintValidatorContext context in which the constraint is evaluated
     * @return <code>false</code> if <code>password</code> does not pass the constraint
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {

        return password == null || password.isEmpty() || !(password.length() < min || password.length() > max);

    }

}