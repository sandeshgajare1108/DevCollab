package com.DevCollab.entity;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "user_settings")
public class UserSettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private Long settingId;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "email_notifications")
    private Boolean emailNotifications = true;

    @Column(name = "task_notifications")
    private Boolean taskNotifications = true;

    @Column(name = "project_notifications")
    private Boolean projectNotifications = true;

    @Column(name = "dark_mode")
    private Boolean darkMode = false;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;


    public UserSettingsEntity() {
        super();
    }


    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }


    public Boolean getTaskNotifications() {
        return taskNotifications;
    }

    public void setTaskNotifications(Boolean taskNotifications) {
        this.taskNotifications = taskNotifications;
    }


    public Boolean getProjectNotifications() {
        return projectNotifications;
    }

    public void setProjectNotifications(Boolean projectNotifications) {
        this.projectNotifications = projectNotifications;
    }


    public Boolean getDarkMode() {
        return darkMode;
    }

    public void setDarkMode(Boolean darkMode) {
        this.darkMode = darkMode;
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


    @Override
    public String toString() {

        return "UserSettingsEntity [settingId=" + settingId
                + ", userId=" + userId
                + ", emailNotifications=" + emailNotifications
                + ", taskNotifications=" + taskNotifications
                + ", projectNotifications=" + projectNotifications
                + ", darkMode=" + darkMode
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + "]";
    }
}