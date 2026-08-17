package com.DevCollab.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.UserRepository;
import com.DevCollab.entity.UserEntity;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    // =========================================================
    // GET ALL USERS
    // =========================================================

    public List<UserEntity> allRecordsUsers() {

        return userRepository.findAll();
    }


    // =========================================================
    // GET USER BY ID
    // =========================================================

    public UserEntity getUserById(Long userId) {

        Optional<UserEntity> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isPresent()) {

            return optionalUser.get();
        }

        return null;
    }


    // =========================================================
    // UPDATE PROFILE
    // =========================================================

    public UserEntity updateUser(
            Long userId,
            String fullName,
            String email,
            String mobile) {

        Optional<UserEntity> optionalUser =
                userRepository.findById(userId);

        if (!optionalUser.isPresent()) {

            return null;
        }

        UserEntity user =
                optionalUser.get();


        // Update only profile fields

        user.setFullName(fullName);

        user.setEmail(email);

        user.setMobile(mobile);


        // Updated timestamp

        user.setUpdatedAt(
                new Timestamp(
                        System.currentTimeMillis()
                )
        );


        return userRepository.save(user);
    }


    // =========================================================
    // FIND BY STATUS
    // =========================================================

    public List<UserEntity> getByUserStatus(
            String status) {

        return userRepository.findByStatus(status);
    }


    // =========================================================
    // FIND BY EMAIL
    // =========================================================

    public List<UserEntity> getByUserEmail(
            String email) {

        return userRepository.findByEmail(email);
    }


    // =========================================================
    // CHECK EMAIL
    // =========================================================

    public boolean checkUserEmail(
            String email) {

        return userRepository.existsByEmail(email);
    }


    // =========================================================
    // SEARCH BY NAME
    // =========================================================

    public List<UserEntity> getByUserName(
            String fullName) {

        return userRepository
                .findByFullNameContainingIgnoreCase(fullName);
    }
}