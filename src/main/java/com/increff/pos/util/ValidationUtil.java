package com.increff.pos.util;

import com.increff.pos.service.exception.ApiException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

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

    public static <T> List<Map<String, String>> normalizeValidateFormList(List<T> formList) throws ApiException {
        List<Map<String, String>> errorList = new ArrayList<>();

        for (int i = 0; i < formList.size(); i++) {
            T form = formList.get(i);
            try {
                NormalizeUtil.normalize(form);
                ValidationUtil.checkValid(form);
            } catch (Exception e) {
                Map<String, String> row = new HashMap<>();
                row.put("index", Integer.toString(i));
                row.put("error", e.getMessage());
                errorList.add(row);
            }
        }
        return errorList;
    }

}
