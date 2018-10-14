package com.pacgame.main.exception;

public class ApiValidationError extends ApiSubError {
    private String objectName;
    private String field;
    private Object rejectedValue;
    private String message;

    ApiValidationError(String objectName, String message) {
        this.objectName = objectName;
        this.message = message;
    }

    public ApiValidationError(String objectName, String field, Object rejectedValue, String message) {
        this.objectName = objectName;
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

    public ApiValidationError() {
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}