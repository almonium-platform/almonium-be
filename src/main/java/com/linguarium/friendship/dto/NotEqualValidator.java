package com.linguarium.friendship.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;

public class NotEqualValidator implements ConstraintValidator<NotEqual, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(NotEqual constraintAnnotation) {
        firstFieldName = constraintAnnotation.firstField();
        secondFieldName = constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object firstObj = BeanUtils.getProperty(value, firstFieldName);
            Object secondObj = BeanUtils.getProperty(value, secondFieldName);

            return firstObj == null && secondObj == null || firstObj != null && !firstObj.equals(secondObj);
        } catch (Exception ignore) {
            // ignore
        }
        return true;
    }
}
