package com.dounine.corgi.demo.validation.jsr303;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by huanghuanlai on 16/6/24.
 */
public class PasswordValidImpl implements ConstraintValidator<PasswordValid,String> {

    private int min;
    private int max;

    @Override
    public void initialize(PasswordValid constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isNotBlank(value)){
            if(value.trim().length()>=min&&value.trim().length()<=max){
                return true;
            }
        }
        return false;
    }
}
