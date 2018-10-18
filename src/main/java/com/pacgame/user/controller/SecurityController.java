package com.pacgame.user.controller;

import com.pacgame.main.exception.ApiError;
import com.pacgame.main.exception.ApiSubError;
import com.pacgame.main.exception.ApiValidationError;
import com.pacgame.main.exception.ResourceNotFoundException;
import com.pacgame.main.validation.group.Registration;
import com.pacgame.user.exception.UsernameExistsException;
import com.pacgame.user.model.CustomUserDetails;
import com.pacgame.user.model.User;
import com.pacgame.user.model.UserDto;
import com.pacgame.user.repository.UserRepository;
import com.pacgame.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SecurityController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IUserService service;

    @GetMapping("/user/logged_user")
    public Object getLoggedUser( Authentication authentication) {
//        System.out.println(auth.getPrincipal());
        if (authentication.getPrincipal() == null) {
            throw new ResourceNotFoundException("Resource not found");
        }

        return ((CustomUserDetails)authentication.getPrincipal()).getUser();
    }

    @RequestMapping(path="/register", method = RequestMethod.POST, produces={"application/json"})
    @ResponseBody
    public ResponseEntity registerProcess(
             @Validated(Registration.class) @RequestBody User user, BindingResult bindingResult,
             Errors errors
            ) {

        System.out.println("/register");
//        System.out.println(result.hasErrors());
//        if (result.hasErrors()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
//        }


        if (bindingResult.hasErrors()) {
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Invalid data", bindingResult.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }

        User newUserAccount = service.createNewUserAccount(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUserAccount);

    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    public ResponseEntity<Object> validationError(MethodArgumentNotValidException ex) {
//        BindingResult result = ex.getBindingResult();
//        System.out.println("1111111111111111");
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
