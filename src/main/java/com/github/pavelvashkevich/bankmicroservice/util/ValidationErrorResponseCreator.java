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
        }
        return response.toString();
    }

    private String createGlobalErrorsResponse(List<ObjectError> globalErrors) {
        StringBuilder response = new StringBuilder();
        for (ObjectError globalError : globalErrors) {
            response.append(globalError.getDefaultMessage()).append(";");
        }
        return response.toString();
    }
}
