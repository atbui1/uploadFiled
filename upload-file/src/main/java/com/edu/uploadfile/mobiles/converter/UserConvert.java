package com.edu.uploadfile.mobiles.converter;

import com.edu.uploadfile.mobiles.models.User;
import com.edu.uploadfile.mobiles.payloads.requests.SignupRequest;
import org.springframework.stereotype.Component;

@Component
public class UserConvert {

    public User toEntity(SignupRequest dto) {
        User entity = new User();
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setStatus(dto.getStatus());
        entity.setRoles(dto.getRoles());
        entity.setAvatar(dto.getAvatar());

        return entity;
    }

    public User toEntity(User entity, SignupRequest dto) {
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setStatus(dto.getStatus());
        entity.setRoles(dto.getRoles());
        entity.setAvatar(dto.getAvatar());

        return entity;
    }
}
