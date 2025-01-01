package com.virgo.dynamic_form.service;

import com.virgo.dynamic_form.dto.request.UserRequestDTO;
import com.virgo.dynamic_form.dto.request.UserUpdateRequestDTO;
import com.virgo.dynamic_form.model.meta.global.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    @Transactional
    UserEntity create(UserRequestDTO request);

    UserEntity getByName(String name);

    UserEntity getByEmail(String email);

    List<UserEntity> getAll();

    UserEntity getById(String userId);

    @Transactional
    UserEntity update(UserUpdateRequestDTO request);

    UserEntity getUserAuthenticated();

    void deleteById(String userId);
}
