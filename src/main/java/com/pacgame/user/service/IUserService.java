package com.pacgame.user.service;

import com.pacgame.user.exception.UsernameExistsException;
import com.pacgame.user.model.User;
import com.pacgame.user.model.UserDetails;
import com.pacgame.user.model.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUserService {

    User createNewUserAccount(User accountUser);

    User changePassword(UserDto accountDto);

    User changeRoles(UserDto userDto);

    User updateUser(User user, UserDto accountDto) throws Exception;

    Page<User> findAll(Pageable pageable);

    Page<User> findBySearchTerm(String term, Pageable pageable);

    User findByIdToEdit(Long id);

    UserDetails updateUserDetails(UserDetails userDetails, UserDetails userDetailsDto) throws IOException;

    String updateImage(UserDetails userDetails, MultipartFile multipartFile) throws IOException;
}
