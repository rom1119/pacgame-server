package com.pacgame.user.service;

import com.pacgame.user.exception.EmailExistsException;
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

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
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

    private EntityManager entityManager;

//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }


    @Transactional
    @Override
    public User registerNewUserAccount(UserDto accountDto)
            throws EmailExistsException {

        if (emailExist(accountDto.getEmail())) {
            throw new EmailExistsException(
                    "There is an account with that email adress: "
                            +  accountDto.getEmail());
        }
//        entityManager = emf.createEntityManager();

        User user = new User();
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setUsername(accountDto.getEmail());
        user.setEnabled(true);
        user.addRole(roleRepository.findByName("ROLE_USER"));

        UserDetails userDetailsAdmin = new UserDetails();
        userDetailsAdmin.setScore(0);
        userDetailsAdmin.setFirstName(accountDto.getUserDetails().getFirstName());
        userDetailsAdmin.setLastName(accountDto.getUserDetails().getLastName());;

        userDetailsAdmin.setUser(user);

        userDetailsRepository.save(userDetailsAdmin);
        userRepository.save(user);

        return user;
    }

    @Override
    public User changePassword(UserDto accountDto) {

        User user = userRepository.findById(accountDto.getId()).get();
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));

        userRepository.save(user);

        return user;
    }

    @Override
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


    private List<User> getOneMethod(User user) throws NoSuchElementException
    {
        User toEdit = findOne(user.getId());
        List<User> arr = new ArrayList<>();
        if (toEdit != null) {
            arr.add(toEdit);
        }
        return arr;
    }


    private boolean emailExist(String email) {
        User user = userRepository.findByEmail(email);
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
