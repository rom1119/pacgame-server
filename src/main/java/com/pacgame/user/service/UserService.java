package com.pacgame.user.service;

import com.pacgame.main.service.StorageManager;
import com.pacgame.main.service.StorageService;
import com.pacgame.user.exception.UsernameExistsException;
import com.pacgame.user.model.User;
import com.pacgame.user.model.UserDetails;
import com.pacgame.user.model.UserDto;
import com.pacgame.user.repository.RoleRepository;
import com.pacgame.user.repository.UserDetailsRepository;
import com.pacgame.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StorageService storageService;

//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }


    @Override
    @Transactional
    public User createNewUserAccount(User accountUser)
    {

//        entityManager = emf.createEntityManager();

        User user = new User();
        user.setPassword(passwordEncoder.encode(accountUser.getPassword()));
        user.setUsername(accountUser.getUsername());
        user.setEnabled(true);
        user.addRole(roleRepository.findByName("ROLE_USER"));

        UserDetails userDetails = new UserDetails();
        userDetails.setScore(0);
//        userDetails.setFirstName(accountUser.getUserDetails().getFirstName());
//        userDetails.setLastName(accountUser.getUserDetails().getLastName());;

        user.setUserDetails(userDetails);

//        userDetailsRepository.save(userDetailsAdmin);
        userRepository.save(user);

        return user;
    }

    @Override
    @Transactional
    public User changePassword(UserDto accountDto) {

        User user = userRepository.findById(accountDto.getId()).get();
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));

        userRepository.save(user);

        return user;
    }

    @Override
    @Transactional
    public User updateUser(User userDb, UserDto userDto) throws Exception {

        User user = modelMapper.map(userDto, User.class);
        user.getUserDetails().setFileName(userDb.getUserDetails().getFileName());
        user.setRoles(userDb.getRoles());

        userRepository.save(user);

        return user;
    }

    @Override
    public User changeRoles(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).get();
        user.setRoles(userDto.getRoles());

        userRepository.save(user);

        return user;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findBySearchTerm(String term, Pageable pageable) {
        return userRepository.findAll(term, pageable);

    }

    @Override
    public User findByIdToEdit(Long id) {
        return findOne(id);
    }

    @Override
    @Transactional
    public UserDetails updateUserDetails(UserDetails userDetails, UserDetails userDetailsDto) throws IOException {

        userDetails.setFirstName(userDetailsDto.getFirstName());
        userDetails.setLastName(userDetailsDto.getLastName());
//        userDetails.setFile(userDetailsDto.getFile());

        userDetailsRepository.save(userDetails);

        return userDetails;
    }

    @Override
    @Transactional
    public String updateImage(UserDetails userDetails, MultipartFile multipartFile) throws IOException {

        String filename = null;
        if (!multipartFile.isEmpty()) {
            userDetails.setFile(multipartFile);
            filename = storageService.updateFile(userDetails);

            userDetailsRepository.save(userDetails);

        }

        return filename;
    }


    private List<User> getOneMethod(User user) throws NoSuchElementException
    {
        User toEdit = findOne(user.getId());
        List<User> arr = new ArrayList<>();
        if (toEdit != null) {
            arr.add(toEdit);
        }
        return arr;
    }


    private boolean usernameExist(String email) {
        User user = userRepository.findByUsername(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    private User findOne(Long id) throws NoSuchElementException {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

}
