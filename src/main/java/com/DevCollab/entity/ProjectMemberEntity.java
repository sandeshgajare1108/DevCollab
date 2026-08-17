package com.DevCollab.entity;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "project_members")
public class ProjectMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_member_id")
    private Long projectMemberId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private MemberRole memberRole;

    @Column(name = "joined_at")
    private Timestamp joinedAt;


    public enum MemberRole {
        OWNER,
        DEVELOPER,
        TESTER,
        DESIGNER,
        MANAGER
    }


    public ProjectMemberEntity() {
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


    public MemberRole getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(MemberRole memberRole) {
        this.memberRole = memberRole;
    }


    public Timestamp getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
    }
}