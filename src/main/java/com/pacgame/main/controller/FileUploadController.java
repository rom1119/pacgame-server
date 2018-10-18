package com.pacgame.main.controller;

import com.pacgame.main.exception.ApiError;
import com.pacgame.main.exception.ResourceNotFoundException;
import com.pacgame.main.validation.ImageValidator;
import com.pacgame.main.validation.group.FileValidationGroup;
import com.pacgame.user.model.User;
import com.pacgame.user.model.UserDetails;
import com.pacgame.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @Autowired
    private IUserService userService;

//    @InitBinder
//    protected void initBinderFileBucket(WebDataBinder binder) {
//        binder.setValidator(imageValidator);
//        binder.
//    }

    @PutMapping( path = "/user/{id}/file")
    public ResponseEntity updateImage(
            @PathVariable final Long id,
            @ModelAttribute( "imageFile") @Validated(FileValidationGroup.class) UserDetails userDetails,
            BindingResult bindingResult,
            @RequestHeader("Content-type") String c
    ) throws Exception {
        System.out.println(userDetails.getFile().getContentType());
        if (bindingResult.hasErrors()) {
            System.out.println("error");

            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Invalid data", bindingResult.getFieldErrors()), HttpStatus.BAD_REQUEST);

        }
        System.out.println("not error");
        User userDb = Optional.ofNullable(userService.findByIdToEdit(id))
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono strony"));
////
        userService.updateImage(userDb.getUserDetails(), userDetails.getFile());
//
        return ResponseEntity.ok(userDb);

    }
}
