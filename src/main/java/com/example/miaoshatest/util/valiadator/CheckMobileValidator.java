package com.example.miaoshatest.util.valiadator;

import com.alibaba.druid.util.StringUtils;
import com.example.miaoshatest.util.PhoneUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CheckMobileValidator implements ConstraintValidator<CheckMobile, String> {

    private boolean require = false;


    @Override
    public void initialize(CheckMobile constraintAnnotation) {
        require = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        Predicate<String> predicate = (String value) -> PhoneUtil.checkPhone(value);
        if (require){
            return predicate.test(s);
        }else {
            predicate = (String value) -> StringUtils.isEmpty(value);
            return predicate.test(s);
        }
    }
}
