package com.pacgame.main.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@ControllerAdvice
public class GlobalExceptionHandler {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(ConstraintViolationException.class)
//    public List<String> handleValidationExceptions(ConstraintViolationException ex) {
//        System.out.println("333333333");
//
//        return ex.getConstraintViolations()
//                .stream()
//                .map(ConstraintViolation::getMessage)
//                .collect(Collectors.toList());
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public List<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
                System.out.println("333333333");

        return ex.getBindingResult()
                .getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler({ MethodArgumentNotValidException.class })
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException ex,
//            HttpHeaders headers,
//            HttpStatus status,
//            WebRequest request) {
//        System.out.println("1111111111111111");
//        ex.getParameter();
//        List<ApiSubError> errors = new ArrayList<>();
//        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
////            errors.add(error.getField() + ": " + error.getDefaultMessage());
//            errors.add(new ApiValidationError("asd", error.getField(), "reject", error.getDefaultMessage()));
//        }
////        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
////            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
////        }
//
//        ApiError apiError =
//                new ApiError(HttpStatus.BAD_REQUEST, "Invalid data object", errors);
//        return new ResponseEntity<Object>(
//                apiError, new HttpHeaders(), apiError.getStatus());
//    }

}
