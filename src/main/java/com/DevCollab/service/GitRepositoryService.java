
package com.DevCollab.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.GitRepositoryRepository;
import com.DevCollab.entity.GitRepositoryEntity;

@Service
public class GitRepositoryService {

    @Autowired
    private GitRepositoryRepository repository;

    @Autowired
    private GitHubService gitHubService;

    // =====================================================
    // CONNECT REPOSITORY
    // =====================================================

    public GitRepositoryEntity connectRepository(
            Long projectId,
            Long connectedBy,
            String githubUrl) {

        if (projectId == null) {

            throw new RuntimeException(
                    "Project ID is required"
            );
        }

        if (connectedBy == null) {

            throw new RuntimeException(
                    "Connected user is required"
            );
        }

        String[] parts =
                gitHubService.parseRepositoryUrl(
                        githubUrl
                );

        String owner = parts[0];

        String repo = parts[1];

        Map<String, Object> githubRepository =
                gitHubService.getRepository(
                        owner,
                        repo
                );

        if (githubRepository == null) {

            throw new RuntimeException(
                    "GitHub repository not found"
            );
        }

        String defaultBranch =
                gitHubService.getDefaultBranch(
                        githubRepository
                );

        Optional<GitRepositoryEntity> existing =
                repository.findByProjectId(
                        projectId
                );

        GitRepositoryEntity entity;

        if (existing.isPresent()) {

            entity = existing.get();

        } else {

            entity = new GitRepositoryEntity();

            entity.setProjectId(projectId);

            entity.setConnectedAt(
                    new Timestamp(
                            System.currentTimeMillis()
                    )
            );
        }

        entity.setGithubUrl(
                gitHubService.buildRepositoryUrl(
                        owner,
                        repo
                )
        );

        entity.setGithubOwner(owner);

        entity.setGithubRepo(repo);

        entity.setDefaultBranch(
                defaultBranch
        );

        entity.setConnectedBy(
                connectedBy
        );

        entity.setUpdatedAt(
                new Timestamp(
                        System.currentTimeMillis()
                )
        );

        return repository.save(entity);
    }

    // =====================================================
    // GET BY PROJECT
    // =====================================================

    public Optional<GitRepositoryEntity>
    getByProjectId(Long projectId) {

        return repository.findByProjectId(
                projectId
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    public boolean deleteByProjectId(
            Long projectId) {

        Optional<GitRepositoryEntity> existing =
                repository.findByProjectId(
                        projectId
                );

        if (!existing.isPresent()) {

            return false;
        }

        repository.delete(
                existing.get()
        );

        return true;
    }

    // =====================================================
    // COMMITS
    // =====================================================

    public List<Map<String, Object>>
    getCommits(Long projectId) {

        GitRepositoryEntity repo =
                getRepositoryEntity(
                        projectId
                );

        return gitHubService.getCommits(
                repo.getGithubOwner(),
                repo.getGithubRepo()
        );
    }

    // =====================================================
    // PULL REQUESTS
    // =====================================================

    public List<Map<String, Object>>
    getPullRequests(
            Long projectId,
            String state) {

        GitRepositoryEntity repo =
                getRepositoryEntity(
                        projectId
                );

        return gitHubService.getPullRequests(
                repo.getGithubOwner(),
                repo.getGithubRepo(),
                state
        );
    }

    // =====================================================
    // PULL REQUEST DIFF
    // =====================================================

    public String getPullRequestDiff(
            Long projectId,
            Long pullRequestNumber) {

        GitRepositoryEntity repo =
                getRepositoryEntity(
                        projectId
                );

        return gitHubService.getPullRequestDiff(
                repo.getGithubOwner(),
                repo.getGithubRepo(),
                pullRequestNumber
        );
    }

    // =====================================================
    // PULL REQUEST FILES
    // =====================================================

    public List<Map<String, Object>>
    getPullRequestFiles(
            Long projectId,
            Long pullRequestNumber) {

        GitRepositoryEntity repo =
                getRepositoryEntity(
                        projectId
                );

        return gitHubService.getPullRequestFiles(
                repo.getGithubOwner(),
                repo.getGithubRepo(),
                pullRequestNumber
        );
    }

    // =====================================================
    // GET REPOSITORY ENTITY
    // =====================================================

    private GitRepositoryEntity getRepositoryEntity(
            Long projectId) {

        if (projectId == null ||
            projectId <= 0) {

            throw new RuntimeException(
                    "Valid Project ID is required"
            );
        }

        Optional<GitRepositoryEntity> optional =
                repository.findByProjectId(
                        projectId
                );

        if (!optional.isPresent()) {

            throw new RuntimeException(
                    "No GitHub repository connected to this project"
            );
        }

        return optional.get();
    }
}

