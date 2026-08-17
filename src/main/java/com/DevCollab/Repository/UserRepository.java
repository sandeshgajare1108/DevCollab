package com.DevCollab.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevCollab.entity.UserEntity;

@Repository
public interface UserRepository
        extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findByEmail(String email);

    List<UserEntity> findByStatus(String status);

    boolean existsByEmail(String email);

    List<UserEntity>
    findByFullNameContainingIgnoreCase(String fullName);
    Optional<UserEntity> findByResetToken(String resetToken);
}