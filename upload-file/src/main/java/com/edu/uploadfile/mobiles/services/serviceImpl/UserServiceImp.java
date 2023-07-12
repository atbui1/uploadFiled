package com.edu.uploadfile.mobiles.services.serviceImpl;

import com.edu.uploadfile.mobiles.converter.UserConvert;
import com.edu.uploadfile.mobiles.exception.ApiException;
import com.edu.uploadfile.mobiles.exception.ErrorMessage;
import com.edu.uploadfile.mobiles.exception.NotFoundException;
import com.edu.uploadfile.mobiles.models.ERole;
import com.edu.uploadfile.mobiles.models.Role;
import com.edu.uploadfile.mobiles.models.User;
import com.edu.uploadfile.mobiles.payloads.requests.LoginRequest;
import com.edu.uploadfile.mobiles.payloads.requests.SignupRequest;
import com.edu.uploadfile.mobiles.payloads.response.ResponseObject;
import com.edu.uploadfile.mobiles.repositories.ImageRepository;
import com.edu.uploadfile.mobiles.repositories.RoleRepository;
import com.edu.uploadfile.mobiles.repositories.UserRepository;
import com.edu.uploadfile.mobiles.services.iservice.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImp implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserConvert userConvert;

    @Autowired
    ImageServiceIml imageServiceIml;


    @Override
    public List<User> showAllUser() {
     return userRepository.findAll();
    }

    @Override
    public User registerUser(SignupRequest signupRequest) {

        if (signupRequest.getUsername().trim().equals("")) {
            throw new NotFoundException("username null");
        } else if (signupRequest.getEmail().trim().equals("")) {
            throw new NotFoundException("email null");
        } else if (userRepository.existsByUsername(signupRequest.getUsername().trim())) {
            throw new NotFoundException("username is exits");
        } else if (userRepository.existsByEmail(signupRequest.getEmail().trim())){
            throw new NotFoundException("email is exits");
        }

        Set<String> strRole = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRole == null) {
            Optional<Role> role = roleRepository.findByRoleName(ERole.ROLE_USER);
            roles.add(role.get());
        } else {
            strRole.forEach((element) -> {
                switch (element) {
                    case "admin":
                        Optional<Role> roleAdmin = roleRepository.findByRoleName(ERole.ROLE_ADMIN);
                        roles.add(roleAdmin.get());
                        break;
                    case "mod":
                        Optional<Role> roleMod = roleRepository.findByRoleName(ERole.ROLE_MODERATOR);
                        roles.add(roleMod.get());
                        break;
                    default:
                        Optional<Role> roleDefault = roleRepository.findByRoleName(ERole.ROLE_USER);
                        roles.add(roleDefault.get());
                }
            });
        }
        signupRequest.setStatus("active");
        signupRequest.setRoles(roles);
        User user = userConvert.toEntity(signupRequest);

        userRepository.save(user);

        return userRepository.findByUsername(user.getUsername()).get();
    }

    @Override
    public User upsertUser(SignupRequest signupRequest) {
        User newEntity = new  User();

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        addRole(strRoles, roles);
        signupRequest.setRoles(roles);

        if (signupRequest.getId() != null) {
            User oldEntity = userRepository.findById(signupRequest.getId()).get();
            newEntity = userConvert.toEntity(oldEntity, signupRequest);
        } else {
            newEntity = userConvert.toEntity(signupRequest);
        }
        userRepository.save(newEntity);
        return userRepository.findByUsername(newEntity.getUsername()).get();
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("not found id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User loginUser(LoginRequest loginRequest) {
//        try {
            Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());
            if (user.isPresent()) {
                if (user.get().getPassword().equals(loginRequest.getPassword().trim())) {
                    return user.get();
                } else {
                    throw new NotFoundException("password wrong");
                }
            } else {
                throw new  NotFoundException("username wrong");
            }

//        } catch (Exception e) {
//
//            e.printStackTrace();
//            System.out.println("ERROR: " + e);
//        }
//        return null;
    }


    public Object addRole(Set<String> strRole, Set<Role> roles) {

        if (strRole == null) {
            Optional<Role> role = roleRepository.findByRoleName(ERole.ROLE_USER);
            roles.add(role.get());
        } else {
            strRole.forEach((element) -> {
                switch (element) {
                    case "admin":
                        Optional<Role> roleAdmin = roleRepository.findByRoleName(ERole.ROLE_ADMIN);
                        roles.add(roleAdmin.get());
                        break;
                    case "mod":
                        Optional<Role> roleMod = roleRepository.findByRoleName(ERole.ROLE_MODERATOR);
                        roles.add(roleMod.get());
                        break;
                    default:
                        Optional<Role> roleDefault = roleRepository.findByRoleName(ERole.ROLE_USER);
                        roles.add(roleDefault.get());
                }
            });
        }

        return roles;
    }
}
