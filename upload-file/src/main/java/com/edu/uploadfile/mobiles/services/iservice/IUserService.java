package com.edu.uploadfile.mobiles.services.iservice;

import com.edu.uploadfile.mobiles.models.User;
import com.edu.uploadfile.mobiles.payloads.requests.LoginRequest;
import com.edu.uploadfile.mobiles.payloads.requests.SignupRequest;

import java.util.List;

public interface IUserService {
    List<User> showAllUser();
    User registerUser(SignupRequest signupRequest);
    User upsertUser(SignupRequest signupRequest);
    void deleteUser(Long id);

    User loginUser(LoginRequest loginRequest);

}
