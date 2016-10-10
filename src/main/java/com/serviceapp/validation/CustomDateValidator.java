package com.serviceapp.validation;

import com.serviceapp.validation.annotation.ValidDate;
import org.apache.commons.validator.routines.DateValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Date;

/**
 * Class for validating <code>java.sql.Date</code> values. If date is empty validation completes successfully.
 * Otherwise validator checks for date validity by "yyyy-MM-dd" pattern.
 */
public class CustomDateValidator implements ConstraintValidator<ValidDate, Date> {

    private String dateFormat;

    /**
     * Initialize this validator retrieving date format specified in annotation (if not specified - uses default)
     *
     * @param constraint <code>ValidDate</code> annotation
     * @see ValidDate
     */
    @Override
    public void initialize(ValidDate constraint) {
        this.dateFormat = constraint.pattern();
    }

    /**
     * Validates date. If date is <code>null</code> - constraint considered satisfied. If not - performs validation
     * according to specified pattern.
     *
     * @param date    date to validate
     * @param context context in which the constraint is evaluated
     * @return <code>false</code> if <code>date</code> does not pass the constraint
     */
    @Override
    public boolean isValid(Date date, ConstraintValidatorContext context) {

        DateValidator dateValidator = new DateValidator();

        if (date == null) {
            return true;
        } else {
            java.util.Date validated = dateValidator.validate(date.toString(), dateFormat);
            return validated != null;
        }

    }
}
