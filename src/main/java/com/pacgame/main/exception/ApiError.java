package com.pacgame.main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class ApiError {

    private HttpStatus status;

    private String message;
    private List<ApiSubError> errors;

    private ApiError() {
    }

    ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    ApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
    }


    public ApiError(HttpStatus status, String message, List<FieldError> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = new ArrayList<>();
        errors.stream().forEach(el -> {
            ApiValidationError apiSubError = new ApiValidationError();
            apiSubError.setObjectName(el.getObjectName());
            apiSubError.setMessage(el.getDefaultMessage());
            apiSubError.setRejectedValue(el.getCode());
            apiSubError.setField(el.getField());
            this.errors.add(apiSubError);

        });
    }


    ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public List<ApiSubError> getErrors() {
        return errors;
    }

    public void setErrors(List<ApiSubError> errors) {
        this.errors = errors;
    }
}