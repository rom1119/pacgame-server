package com.pacgame.user.controller;

import com.pacgame.main.exception.ApiError;
import com.pacgame.main.exception.ResourceNotFoundException;
import com.pacgame.main.service.StorageService;
import com.pacgame.user.model.CustomUserDetails;
import com.pacgame.user.model.User;
import com.pacgame.user.model.UserDetails;
import com.pacgame.user.model.UserDto;
import com.pacgame.user.repository.UserRepository;
import com.pacgame.user.service.IUserService;
import com.pacgame.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.header.Header;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private StorageService storageService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id, Authentication auth) {
        System.out.println(auth.getPrincipal());
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAll()
    {
        List<User> all = userRepository.findAllOrderByScoreDesc();

        return ResponseEntity.ok(all);
    }

    @PutMapping( path = "/{id}")
    public ResponseEntity update(
                            @PathVariable final Long id,
                             @RequestBody @Valid UserDetails userDetails,
//                                 @RequestPart( "imageFile") MultipartFil imageFile,
                            BindingResult bindingResult
                 ) throws Exception {

        if (bindingResult.hasErrors()) {
             return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Invalid data", bindingResult.getFieldErrors()), HttpStatus.BAD_REQUEST);

        }

        User userDb = Optional.ofNullable(userService.findByIdToEdit(id))
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono strony"));
////
        UserDetails user = userService.updateUserDetails(userDb.getUserDetails(), userDetails);
//
        return ResponseEntity.ok(userDb);

    }

}


