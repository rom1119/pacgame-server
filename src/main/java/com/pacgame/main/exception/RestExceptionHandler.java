package com.pacgame.main.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Not correct format send data";
        if (headers.containsKey("Content-Type") && headers.containsValue("application/json")) {
            error = "Json format is not correct";
        }
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }



    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

//    @ExceptionHandler(Exception.class)
//    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
//        System.out.println("asdasdasdasdasd");
//        System.out.println(ex);
//        System.out.println("asdasdasdasdasd");
//        ApiError apiError =
//                new ApiError(HttpStatus.TOO_MANY_REQUESTS);
//        return new ResponseEntity<Object>(
//                apiError, new HttpHeaders(), apiError.getStatus());
//    }




//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler({ MethodArgumentNotValidException.class })
//    @Override
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

//    @ExceptionHandler({ ConstraintViolationException.class })
//    public ResponseEntity<Object> handleConstraintViolation(
//            ConstraintViolationException ex
//           ) {
//        System.out.println("222222222222");
////        ex.getParameter();
//        List<ApiSubError> errors = new ArrayList<>();
//        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
////            errors.add(error.getField() + ": " + error.getDefaultMessage());
//            errors.add(new ApiValidationError("asd", violation.getPropertyPath().toString(), "reject", violation.getMessage()));
//        }
//
//        ApiError apiError =
//                new ApiError(HttpStatus.BAD_REQUEST, "Invalid data object", errors);
//        return new ResponseEntity<Object>(
//                apiError, new HttpHeaders(), apiError.getStatus());
//    }

    //other exception handlers below

}