package com.github.pavelvashkevich.bankmicroservice.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

@Component
public class ValidationErrorResponseCreator {

    private ValidationErrorResponseCreator() {
    }

    public String createResponse(BindingResult validationResult) {
        if (validationResult.hasFieldErrors())
            return createFieldErrorsResponse(validationResult.getFieldErrors());
        else
            return createGlobalErrorsResponse(validationResult.getGlobalErrors());
    }

    private String createFieldErrorsResponse(List<FieldError> fieldErrors) {
        StringBuilder response = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            response.append(fieldError.getField()).append(" - ").append(fieldError.getDefaultMessage()).append(";");
            response.append(modifyFieldName(fieldError.getField()))
                    .append(" - ").append(fieldError.getDefaultMessage()).append(";");
        }
        return response.toString();
    }

    private String modifyFieldName(String fieldName) {
        String resultFieldName = getTheLastFieldFromFieldName(fieldName);
        return convertFieldNameToSnakeCase(resultFieldName);
    }

    private String getTheLastFieldFromFieldName(String fieldName) {
        String[] splitFieldError = fieldName.split("\\.");
        return splitFieldError.length >= 2 ? splitFieldError[splitFieldError.length - 1] : fieldName;
    }

    private String convertFieldNameToSnakeCase(String fieldName) {
        StringBuilder result = new StringBuilder();
        for(char ch: fieldName.toCharArray()) {
            if(Character.isUpperCase(ch)) {
                result.append("_").append(Character.toLowerCase(ch));
                continue;
            }
            result.append(ch);
        }
        return result.toString();
    }

    private String createGlobalErrorsResponse(List<ObjectError> globalErrors) {
        StringBuilder response = new StringBuilder();
        for (ObjectError globalError : globalErrors) {
            response.append(globalError.getDefaultMessage()).append(";");
        }
        return response.toString();
    }
}
