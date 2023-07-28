package com.example.demo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class MyCustomValidator implements ConstraintValidator<MyCustomValidation, String> {
    private static final List<String> VALID_VALUES = Arrays.asList("남성", "여성", "성별을 선택하지 않음");

    @Override
    public void initialize(MyCustomValidation constraintAnnotation) {
        // 초기화 작업
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || !VALID_VALUES.contains(value)) {
            return false;
        }
        return true;
    }
}
