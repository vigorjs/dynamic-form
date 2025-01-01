package com.virgo.dynamic_form.repository;

import com.virgo.dynamic_form.model.meta.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByName(String name);

    Optional<UserEntity> findByEmail(String email);
}
