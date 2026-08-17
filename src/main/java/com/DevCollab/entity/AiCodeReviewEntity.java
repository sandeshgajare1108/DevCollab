package com.DevCollab.entity;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "ai_code_reviews")
public class AiCodeReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "pull_request_id")
    private Long pullRequestId;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "submitted_by", nullable = false)
    private Long submittedBy;

    @Column(name = "score", nullable = false)
    private Integer score = 0;

    @Column(name = "bug_count", nullable = false)
    private Integer bugCount = 0;

    @Column(name = "security_count", nullable = false)
    private Integer securityCount = 0;

    @Column(name = "quality_count", nullable = false)
    private Integer qualityCount = 0;

    @Column(name = "findings", columnDefinition = "TEXT")
    private String findings;

    @Column(name = "suggestions", columnDefinition = "TEXT")
    private String suggestions;

    @Column(name = "review_status", nullable = false, length = 30)
    private String reviewStatus = "COMPLETED";

    @Column(name = "created_at")
    private Timestamp createdAt;


    public AiCodeReviewEntity() {
    }


    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {

            createdAt =
                    new Timestamp(
                            System.currentTimeMillis()
                    );
        }

        if (reviewStatus == null ||
            reviewStatus.trim().isEmpty()) {

            reviewStatus = "COMPLETED";
        }

        if (score == null) {
            score = 0;
        }

        if (bugCount == null) {
            bugCount = 0;
        }

        if (securityCount == null) {
            securityCount = 0;
        }

        if (qualityCount == null) {
            qualityCount = 0;
        }
    }


    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }


    public Long getPullRequestId() {
        return pullRequestId;
    }

    public void setPullRequestId(Long pullRequestId) {
        this.pullRequestId = pullRequestId;
    }


    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }


    public Long getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(Long submittedBy) {
        this.submittedBy = submittedBy;
    }


    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }


    public Integer getBugCount() {
        return bugCount;
    }

    public void setBugCount(Integer bugCount) {
        this.bugCount = bugCount;
    }


    public Integer getSecurityCount() {
        return securityCount;
    }

    public void setSecurityCount(Integer securityCount) {
        this.securityCount = securityCount;
    }


    public Integer getQualityCount() {
        return qualityCount;
    }

    public void setQualityCount(Integer qualityCount) {
        this.qualityCount = qualityCount;
    }


    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }


    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }


    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}