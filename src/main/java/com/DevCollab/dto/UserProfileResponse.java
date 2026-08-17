package com.DevCollab.dto;

import java.sql.Timestamp;

public class UserProfileResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String mobile;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String role;


    public UserProfileResponse() {
    }


    public UserProfileResponse(
            Long userId,
            String fullName,
            String email,
            String mobile,
            String status,
            Timestamp createdAt,
            Timestamp updatedAt,
            String role) {

        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
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


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}