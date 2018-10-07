package com.pacgame.main.service;

import com.pacgame.user.model.CustomUserDetails;
import com.pacgame.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class OwnerChecker {

    public boolean isOwner(Authentication auth, Object o)
    {
        if (o instanceof User) {
            return ((CustomUserDetails) auth.getPrincipal()).getId().equals(o);
        }

        return false;
    }
}
