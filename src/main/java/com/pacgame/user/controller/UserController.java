package com.pacgame.user.controller;

import com.pacgame.main.exception.ResourceNotFoundException;
import com.pacgame.user.model.CustomUserDetails;
import com.pacgame.user.model.User;
import com.pacgame.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

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
}
