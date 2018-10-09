package com.pacgame.user.fixtures;

import com.pacgame.user.model.Privilege;
import com.pacgame.user.model.Role;
import com.pacgame.user.model.User;
import com.pacgame.user.model.UserDetails;
import com.pacgame.user.repository.PrivilegeRepository;
import com.pacgame.user.repository.RoleRepository;
import com.pacgame.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

@Component
public class InitialUserData {

    boolean alreadySetup = false;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;


//    @Autowired
//    private PasswordEncoder passwordEncoder;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Transactional
    @PostConstruct
    public void init() {

        if (alreadySetup)
            return;

//        if (event instanceof ContextRefreshedEvent) {
//            ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();
////            applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods().forEach((e, a) -> {
////                System.out.print(e.getName());
////            });
//        }

        List<Integer> countLoop = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14));

        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        Set<Privilege> adminPrivileges = new HashSet<Privilege>();
        Set<Privilege> userPrivileges = new HashSet<Privilege>();
        adminPrivileges.add(readPrivilege);
        adminPrivileges.add(writePrivilege);

        userPrivileges.add(readPrivilege);

        Role superAdminRole = createRoleIfNotFound("ROLE_SUPER_ADMIN", adminPrivileges);
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        Role userRole = createRoleIfNotFound("ROLE_USER", userPrivileges);

        Set<Role> rolesForAdmin = new HashSet<Role>();
        Set<Role> rolesForUser = new HashSet<Role>();

        rolesForAdmin.add(adminRole);

//        System.out.println(userRole);
//        System.out.println(adminRole);
        rolesForUser.add(userRole);
//        rolesForAdmin.add(userRole);

        User sadmin = new User();
        sadmin.setPassword(passwordEncoder().encode("test"));
        sadmin.setUsername("sadm");
        sadmin.addRole(superAdminRole);
        sadmin.addRole(adminRole);
        sadmin.addRole(userRole);
        sadmin.setEnabled(true);

        UserDetails userDetailsSuperAdmin = new UserDetails();
        userDetailsSuperAdmin.setScore(0);
        userDetailsSuperAdmin.setFirstName("super_admin");
        userDetailsSuperAdmin.setLastName("super_admin");

        sadmin.setUserDetails(userDetailsSuperAdmin);
        userRepository.save(sadmin);


        User admin = new User();
        admin.setPassword(passwordEncoder().encode("test"));
        admin.setUsername("adm");
        admin.addRole(adminRole);
        admin.addRole(userRole);
        admin.setEnabled(true);

        UserDetails userDetailsAdmin = new UserDetails();
        userDetailsAdmin.setScore(0);
        userDetailsAdmin.setFirstName("admin");
        userDetailsAdmin.setLastName("admin");

        admin.setUserDetails(userDetailsAdmin);

        userRepository.save(admin);

        User user = new User();
        user.setPassword(passwordEncoder().encode("test"));
        user.setUsername("use");
        user.addRole(userRole);
        user.setEnabled(true);

        UserDetails userDetailsUsr = new UserDetails();
        userDetailsUsr.setScore(0);
        userDetailsUsr.setFirstName("user");
        userDetailsUsr.setLastName("user");

        user.setUserDetails(userDetailsUsr);

        userRepository.save(user);

        for(int i = 0; i < 10; i++) {


            createAdmin(adminRole, i);
            createUser(userRole, i);
        }

        alreadySetup = true;
    }

    @Transactional
    private void createAdmin(Role role, int i)
    {
        User admin = new User();
        admin.setPassword(passwordEncoder().encode("test"));
        admin.setUsername("adm" + i);
        admin.addRole(role);
        admin.setEnabled(true);

        UserDetails userDetailsAdmin = new UserDetails();
        userDetailsAdmin.setScore(0);
        userDetailsAdmin.setFirstName("admin" + i);
        userDetailsAdmin.setLastName("admin" + i);

        admin.setUserDetails(userDetailsAdmin);

        userRepository.save(admin);
    }



    @Transactional
    private void createUser(Role role, int i)
    {
        User user = new User();
        user.setPassword(passwordEncoder().encode("test"));
        user.setUsername("use" + i);
        user.addRole(role);
        user.setEnabled(true);

        UserDetails userDetails = new UserDetails();
        userDetails.setScore(0);
        userDetails.setFirstName("user" + i);
        userDetails.setLastName("user" + i);

        user.setUserDetails(userDetails);

        userRepository.save(user);
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
//            entityManager.persist(privilege);
            privilegeRepository.save(privilege);

        }
        return privilege;
    }

    @Transactional
    private Role createRoleIfNotFound(
            String name, Set<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges( privileges);
            roleRepository.save(role);
        }
        return role;
    }

}
