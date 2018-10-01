package com.pacgame.user.controller;

import com.pacgame.main.exception.ResourceNotFoundException;
import com.pacgame.user.model.User;
import com.pacgame.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("#oauth2.hasScope('read')")
    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAll()
    {
        List<User> all = userRepository.findAll();

        return ResponseEntity.ok(all);
    }
}
