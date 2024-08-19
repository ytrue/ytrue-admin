package com.ytrue.infra.core.annotation.validator;


import com.ytrue.infra.core.annotation.EnumValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author ytrue
 * @date 2023-06-13 17:13
 * @description EnumValueValidator
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

    private String[] strValues;
    private int[] intValues;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        strValues = constraintAnnotation.strValues();
        intValues = constraintAnnotation.intValues();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof String) {
            for (String s : strValues) {
                if (s.equals(value)) {
                    return true;
                }
            }
        } else if (value instanceof Integer integer) {
            for (int s : intValues) {
                if (s == integer.intValue()) {
                    return true;
                }
            }
        }
        return false;
    }

}
