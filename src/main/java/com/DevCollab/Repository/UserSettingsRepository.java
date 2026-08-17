package com.DevCollab.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevCollab.entity.UserSettingsEntity;

@Repository
public interface UserSettingsRepository
        extends JpaRepository<UserSettingsEntity, Long> {

    Optional<UserSettingsEntity> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

}