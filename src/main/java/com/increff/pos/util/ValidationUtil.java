package com.increff.pos.util;

import com.increff.pos.service.ApiException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidationUtil {

    public static <T> Set<ConstraintViolation<T>> validate(T form) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(form);
    }

    public static <T> void checkValid(T obj) throws ApiException {
        Set<ConstraintViolation<T>> violations = validate(obj);
        if (violations.isEmpty()) {
            return;
        }
        String errorList = "";
        for (ConstraintViolation<T> violation : violations) {
            if(!errorList.isEmpty()){
                errorList += "\n";
            }
            errorList = errorList + violation.getMessage();
        }
        throw new ApiException(errorList);
    }
}
