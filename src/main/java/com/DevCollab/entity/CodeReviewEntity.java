package com.DevCollab.entity;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "code_reviews")
public class CodeReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_review_id")
    private Long codeReviewId;


    @Column(name = "pull_request_id", nullable = false)
    private Long pullRequestId;


    @Column(name = "project_id", nullable = false)
    private Long projectId;


    @Column(name = "task_id")
    private Long taskId;


    @Column(name = "reviewer_id", nullable = false)
    private Long reviewerId;


    @Column(
        name = "review_comment",
        columnDefinition = "TEXT"
    )
    private String reviewComment;


    @Column(
        name = "decision",
        nullable = false,
        length = 40
    )
    private String decision;


    @Column(name = "created_at")
    private Timestamp createdAt;


    @Column(name = "updated_at")
    private Timestamp updatedAt;


    public CodeReviewEntity() {
    }


    public Long getCodeReviewId() {
        return codeReviewId;
    }

    public void setCodeReviewId(
            Long codeReviewId) {
        this.codeReviewId =
                codeReviewId;
    }


    public Long getPullRequestId() {
        return pullRequestId;
    }

    public void setPullRequestId(
            Long pullRequestId) {
        this.pullRequestId =
                pullRequestId;
    }


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(
            Long projectId) {
        this.projectId =
                projectId;
    }


    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(
            Long taskId) {
        this.taskId =
                taskId;
    }


    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(
            Long reviewerId) {
        this.reviewerId =
                reviewerId;
    }


    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(
            String reviewComment) {
        this.reviewComment =
                reviewComment;
    }


    public String getDecision() {
        return decision;
    }

    public void setDecision(
            String decision) {
        this.decision =
                decision;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            Timestamp createdAt) {
        this.createdAt =
                createdAt;
    }


    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(
            Timestamp updatedAt) {
        this.updatedAt =
                updatedAt;
    }
}