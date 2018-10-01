package com.pacgame.user.service;

import com.pacgame.user.exception.EmailExistsException;
import com.pacgame.user.model.CustomUserDetails;
import com.pacgame.user.model.User;
import com.pacgame.user.model.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    User registerNewUserAccount(UserDto accountDto)
            throws EmailExistsException;

    User changePassword(UserDto accountDto);

    User changeRoles(UserDto userDto);

    User updateUser(User user, UserDto accountDto) throws Exception;

    Page<User> findAll(Pageable pageable);

    Page<User> findBySearchTerm(String term, Pageable pageable);

}
