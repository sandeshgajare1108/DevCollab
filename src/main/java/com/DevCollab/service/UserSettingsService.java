package com.DevCollab.service;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.UserSettingsRepository;
import com.DevCollab.entity.UserSettingsEntity;

@Service
public class UserSettingsService {

    @Autowired
    private UserSettingsRepository repository;


    // =====================================================
    // GET SETTINGS
    // =====================================================

    public UserSettingsEntity getSettings(Long userId) {

        Optional<UserSettingsEntity> optional =
                repository.findByUserId(userId);

        if (optional.isPresent()) {

            return optional.get();

        }

        // Create default settings if not available

        UserSettingsEntity settings =
                new UserSettingsEntity();

        settings.setUserId(userId);

        settings.setEmailNotifications(true);
        settings.setTaskNotifications(true);
        settings.setProjectNotifications(true);
        settings.setDarkMode(false);

        Timestamp now =
                new Timestamp(System.currentTimeMillis());

        settings.setCreatedAt(now);
        settings.setUpdatedAt(now);

        return repository.save(settings);
    }


    // =====================================================
    // UPDATE SETTINGS
    // =====================================================

    public UserSettingsEntity updateSettings(
            Long userId,
            UserSettingsEntity request) {

        UserSettingsEntity settings =
                getSettings(userId);


        if (request.getEmailNotifications() != null) {

            settings.setEmailNotifications(
                    request.getEmailNotifications()
            );
        }


        if (request.getTaskNotifications() != null) {

            settings.setTaskNotifications(
                    request.getTaskNotifications()
            );
        }


        if (request.getProjectNotifications() != null) {

            settings.setProjectNotifications(
                    request.getProjectNotifications()
            );
        }


        if (request.getDarkMode() != null) {

            settings.setDarkMode(
                    request.getDarkMode()
            );
        }


        settings.setUpdatedAt(
                new Timestamp(
                        System.currentTimeMillis()
                )
        );


        return repository.save(settings);
    }


    // =====================================================
    // DELETE SETTINGS
    // =====================================================

    public void deleteSettings(Long userId) {

        Optional<UserSettingsEntity> optional =
                repository.findByUserId(userId);

        if (optional.isPresent()) {

            repository.delete(optional.get());

        }
    }
}