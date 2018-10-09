package com.pacgame.main.service;

import com.pacgame.user.model.CustomUserDetails;
import com.pacgame.user.model.User;
import com.pacgame.user.repository.RoleRepository;
import com.pacgame.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private IUserService service;

    @Autowired
    private MessageSource messages;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {

        User user = userRepository.findByUsername(email);
        System.out.println(userRepository.findAll().size());
        System.out.println(userRepository.findAll().size());
        if (user == null) {
            throw new UsernameNotFoundException(
                    "No user found with username: "+ email);
        }

        return new CustomUserDetails(user);
    }


}