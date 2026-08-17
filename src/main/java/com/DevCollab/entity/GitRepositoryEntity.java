package com.DevCollab.entity;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(
    name = "git_repositories",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_project_github",
            columnNames = {"project_id"}
        )
    }
)
public class GitRepositoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "git_repository_id")
    private Long gitRepositoryId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "github_url", nullable = false, length = 300)
    private String githubUrl;

    @Column(name = "github_owner", nullable = false, length = 150)
    private String githubOwner;

    @Column(name = "github_repo", nullable = false, length = 150)
    private String githubRepo;

    @Column(name = "default_branch", length = 150)
    private String defaultBranch;

    @Column(name = "connected_by", nullable = false)
    private Long connectedBy;

    @Column(name = "connected_at")
    private Timestamp connectedAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;


    public GitRepositoryEntity() {
    }


    public Long getGitRepositoryId() {
        return gitRepositoryId;
    }

    public void setGitRepositoryId(Long gitRepositoryId) {
        this.gitRepositoryId = gitRepositoryId;
    }


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }


    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }


    public String getGithubOwner() {
        return githubOwner;
    }

    public void setGithubOwner(String githubOwner) {
        this.githubOwner = githubOwner;
    }


    public String getGithubRepo() {
        return githubRepo;
    }

    public void setGithubRepo(String githubRepo) {
        this.githubRepo = githubRepo;
    }


    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }


    public Long getConnectedBy() {
        return connectedBy;
    }

    public void setConnectedBy(Long connectedBy) {
        this.connectedBy = connectedBy;
    }


    public Timestamp getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(Timestamp connectedAt) {
        this.connectedAt = connectedAt;
    }


    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}