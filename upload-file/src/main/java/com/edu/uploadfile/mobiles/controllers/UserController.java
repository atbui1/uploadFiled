package com.edu.uploadfile.mobiles.controllers;


import com.edu.uploadfile.mobiles.models.User;
import com.edu.uploadfile.mobiles.payloads.requests.LoginRequest;
import com.edu.uploadfile.mobiles.payloads.requests.SignupRequest;
import com.edu.uploadfile.mobiles.payloads.response.ResponseObject;
import com.edu.uploadfile.mobiles.services.serviceImpl.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping(path = "/api/v2")
public class UserController {

    @Autowired
    UserServiceImp userServiceImp;

    @GetMapping("/users")
    ResponseEntity<?> showAllUser() {
        userServiceImp.showAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("success", "Found users", userServiceImp.showAllUser())
        );
    }

    @PostMapping("/user/register")
    ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        User user = userServiceImp.registerUser(signupRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("success", "register users", user)
        );
    }

    @PostMapping("/user/login")
    ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userServiceImp.loginUser(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("success", "login users", user)
        );
    }

    /** update and insert */
    @PostMapping("/user/upsert")
    ResponseEntity<?> upsertUser(@RequestBody SignupRequest signupRequest) {
        User user = userServiceImp.upsertUser(signupRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("success", "upsert users", user)
        );
    }

    /** delete user */
    @DeleteMapping("/user/{id}")
    ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long id) {
        userServiceImp.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("success", "success delete user's id: " + id, "")
        );
    }

}
