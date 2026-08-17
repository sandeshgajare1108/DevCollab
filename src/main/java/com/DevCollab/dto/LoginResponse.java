package com.DevCollab.dto;

import java.util.Set;

import com.DevCollab.entity.RoleEntity;

public class LoginResponse {

    private String token;

    private Long userId;

    private String fullName;

    private String email;

    private Set<RoleEntity> roles;


    // Default Constructor
    public LoginResponse() {
    }


    // Parameterized Constructor
    public LoginResponse(
            String token,
            Long userId,
            String fullName,
            String email,
            Set<RoleEntity> roles) {

        this.token = token;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
    }


    // Getters and Setters

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }
}