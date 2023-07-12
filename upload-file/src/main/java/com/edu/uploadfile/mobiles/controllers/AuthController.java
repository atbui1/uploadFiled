package com.edu.uploadfile.mobiles.controllers;


import com.edu.uploadfile.mobiles.models.ERole;
import com.edu.uploadfile.mobiles.models.Role;
import com.edu.uploadfile.mobiles.models.User;
import com.edu.uploadfile.mobiles.payloads.requests.LoginRequest;
import com.edu.uploadfile.mobiles.payloads.requests.SignupRequest;
import com.edu.uploadfile.mobiles.payloads.response.ResponseObject;
import com.edu.uploadfile.mobiles.repositories.RoleRepository;
import com.edu.uploadfile.mobiles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping(path = "/api/v1")
public class AuthController {

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_LOCK = "lock";
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
//    @Autowired
//    PasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());
            if (user.isPresent()) {
                if (user.get().getPassword().trim().equals(loginRequest.getPassword().trim())) {
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("ok", "login success", user.get())
                    );
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "password wrong", "")
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "username wrong", "")
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "login failed", "")
            );
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseObject> signupAccount(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(
                    new ResponseObject("failed", "username is already exist..!!", "")
            );
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(
                    new ResponseObject("failed", "Email is already exist..!!", "")
                    );
        }
        /** create new account */
        User newUser = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
//                encoder.encode(signupRequest.getPassword()),
                signupRequest.getPassword(),
                STATUS_ACTIVE);
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            System.out.println("nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
            Role role = roleRepository.findByRoleName(ERole.ROLE_USER)
                            .orElseThrow(()->new RuntimeException("ERROR: role not found!!"));
            roles.add(role);
        } else if (strRoles.size() < 1) {
            System.out.println("ddddddddddddddddddddddddddddddddddddddddddddddddd1111111111111111111111111111111111111111");
            Role role = roleRepository.findByRoleName(ERole.ROLE_USER)
                    .orElseThrow(()->new RuntimeException("ERROR: role not found!!"));
            roles.add(role);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role roleAdmin = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(()->new RuntimeException("ERROR: not found role admin"));
                        roles.add(roleAdmin);
                        break;
                    case "mod":
                        Role roleMod = roleRepository.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(()->new RuntimeException("ERROR: not found role mod"));
                        roles.add(roleMod);
                        break;
                    case "user":
                        Role roleUser = roleRepository.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(()-> new RuntimeException("ERROR: not found role user"));
                        roles.add(roleUser);
                        break;
                }
            });
        }

        newUser.setRoles(roles);
        userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "SUCCESSFULLY create new user", userRepository.findByUsername(signupRequest.getUsername()))
        );
    }

    /** new get all user */
    @GetMapping("/users")
    public ResponseEntity<ResponseObject> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.size() < 1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Not found user", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "found user", users)
        );
    }
}
