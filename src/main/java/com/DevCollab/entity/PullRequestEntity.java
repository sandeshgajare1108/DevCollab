package com.DevCollab.entity;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(
    name = "pull_requests",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_project_pr",
            columnNames = {
                "project_id",
                "github_pr_number"
            }
        )
    }
)
public class PullRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pull_request_id")
    private Long pullRequestId;


    @Column(name = "project_id", nullable = false)
    private Long projectId;


    @Column(name = "task_id")
    private Long taskId;


    @Column(name = "created_by", nullable = false)
    private Long createdBy;


    @Column(name = "github_pr_number", nullable = false)
    private Long githubPrNumber;


    @Column(name = "title", nullable = false, length = 300)
    private String title;


    @Column(name = "description", columnDefinition = "TEXT")
    private String description;


    @Column(name = "source_branch", length = 200)
    private String sourceBranch;


    @Column(name = "target_branch", length = 200)
    private String targetBranch;


    @Column(name = "pr_url", length = 500)
    private String prUrl;


    @Column(name = "github_state", length = 30)
    private String githubState;


    @Column(name = "status", nullable = false, length = 40)
    private String status = "OPEN";


    @Column(name = "github_created_at")
    private Timestamp githubCreatedAt;


    @Column(name = "github_updated_at")
    private Timestamp githubUpdatedAt;


    @Column(name = "created_at")
    private Timestamp createdAt;


    @Column(name = "updated_at")
    private Timestamp updatedAt;


    public PullRequestEntity() {
    }


    public Long getPullRequestId() {
        return pullRequestId;
    }

    public void setPullRequestId(Long pullRequestId) {
        this.pullRequestId = pullRequestId;
    }


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }


    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }


    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }


    public Long getGithubPrNumber() {
        return githubPrNumber;
    }

    public void setGithubPrNumber(Long githubPrNumber) {
        this.githubPrNumber = githubPrNumber;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getSourceBranch() {
        return sourceBranch;
    }

    public void setSourceBranch(String sourceBranch) {
        this.sourceBranch = sourceBranch;
    }


    public String getTargetBranch() {
        return targetBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }


    public String getPrUrl() {
        return prUrl;
    }

    public void setPrUrl(String prUrl) {
        this.prUrl = prUrl;
    }


    public String getGithubState() {
        return githubState;
    }

    public void setGithubState(String githubState) {
        this.githubState = githubState;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Timestamp getGithubCreatedAt() {
        return githubCreatedAt;
    }

    public void setGithubCreatedAt(Timestamp githubCreatedAt) {
        this.githubCreatedAt = githubCreatedAt;
    }


    public Timestamp getGithubUpdatedAt() {
        return githubUpdatedAt;
    }

    public void setGithubUpdatedAt(Timestamp githubUpdatedAt) {
        this.githubUpdatedAt = githubUpdatedAt;
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
}