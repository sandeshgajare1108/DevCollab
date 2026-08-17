
package com.DevCollab.entity;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(
    name = "team_members",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_project_user",
            columnNames = {"project_id", "user_id"}
        )
    }
)
public class TeamMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_member_id")
    private Long teamMemberId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "role", length = 50)
    private String role = "MEMBER";

    @Column(name = "joined_at")
    private Timestamp joinedAt;


    // ================= GETTERS & SETTERS =================

    public Long getTeamMemberId() {
        return teamMemberId;
    }

    public void setTeamMemberId(Long teamMemberId) {
        this.teamMemberId = teamMemberId;
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


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public Timestamp getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
    }
}
