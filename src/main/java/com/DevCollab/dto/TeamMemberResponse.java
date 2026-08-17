
package com.DevCollab.dto;

import java.sql.Timestamp;

public class TeamMemberResponse {

    private Long projectMemberId;

    private Long projectId;

    private Long userId;

    private String userName;

    private String email;

    private String memberRole;

    private Timestamp joinedAt;


    public TeamMemberResponse() {
    }


    public TeamMemberResponse(
            Long projectMemberId,
            Long projectId,
            Long userId,
            String userName,
            String email,
            String memberRole,
            Timestamp joinedAt) {

        this.projectMemberId = projectMemberId;
        this.projectId = projectId;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.memberRole = memberRole;
        this.joinedAt = joinedAt;
    }


    public Long getProjectMemberId() {
        return projectMemberId;
    }

    public void setProjectMemberId(Long projectMemberId) {
        this.projectMemberId = projectMemberId;
    }


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(String memberRole) {
        this.memberRole = memberRole;
    }


    public Timestamp getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
    }
}
