package com.pacgame.user.service;

import com.pacgame.user.exception.UsernameExistsException;
import com.pacgame.user.model.User;
import com.pacgame.user.model.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    User registerNewUserAccount(UserDto accountDto)
            throws UsernameExistsException;

    User changePassword(UserDto accountDto);

    User changeRoles(UserDto userDto);

    User updateUser(User user, UserDto accountDto) throws Exception;

    Page<User> findAll(Pageable pageable);

    Page<User> findBySearchTerm(String term, Pageable pageable);

}
