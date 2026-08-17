package com.DevCollab.service;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.GitRepositoryRepository;
import com.DevCollab.Repository.PullRequestRepository;
import com.DevCollab.entity.GitRepositoryEntity;
import com.DevCollab.entity.PullRequestEntity;

@Service
public class PullRequestService {

    @Autowired
    private PullRequestRepository pullRequestRepository;


    @Autowired
    private GitRepositoryRepository
            gitRepositoryRepository;


    @Autowired
    private GitHubService gitHubService;


    // =====================================================
    // SYNC GITHUB PULL REQUESTS
    // =====================================================

    public List<PullRequestEntity> syncPullRequests(
            Long projectId,
            Long createdBy) {


        Optional<GitRepositoryEntity>
                repository =
                gitRepositoryRepository
                        .findByProjectId(projectId);


        if (!repository.isPresent()) {

            throw new RuntimeException(
                    "No GitHub repository connected to this project"
            );
        }


        GitRepositoryEntity gitRepository =
                repository.get();


        List<Map<String, Object>> githubPulls =
                gitHubService.getPullRequests(
                        gitRepository.getGithubOwner(),
                        gitRepository.getGithubRepo(),
                        "all"
                );


        List<PullRequestEntity> result =
                new ArrayList<PullRequestEntity>();


        for (
            Map<String, Object> githubPr
                : githubPulls
        ) {


            Object numberObject =
                    githubPr.get("number");


            if (numberObject == null) {
                continue;
            }


            Long githubPrNumber =
                    Long.valueOf(
                        numberObject.toString()
                    );


            Optional<PullRequestEntity>
                    existing =
                    pullRequestRepository
                        .findByProjectIdAndGithubPrNumber(
                            projectId,
                            githubPrNumber
                        );


            PullRequestEntity entity;


            if (existing.isPresent()) {

                entity =
                        existing.get();

            } else {

                entity =
                        new PullRequestEntity();

                entity.setProjectId(
                        projectId
                );

                entity.setCreatedBy(
                        createdBy
                );

                entity.setGithubPrNumber(
                        githubPrNumber
                );

                entity.setCreatedAt(
                        new Timestamp(
                            System.currentTimeMillis()
                        )
                );

                /*
                 * New PR starts as OPEN.
                 */
                entity.setStatus(
                        "OPEN"
                );
            }


            // =================================================
            // TITLE
            // =================================================

            Object title =
                    githubPr.get("title");


            entity.setTitle(
                    title != null
                        ? title.toString()
                        : "Untitled Pull Request"
            );


            // =================================================
            // DESCRIPTION
            // =================================================

            Object body =
                    githubPr.get("body");


            entity.setDescription(
                    body != null
                        ? body.toString()
                        : ""
            );


            // =================================================
            // URL
            // =================================================

            Object htmlUrl =
                    githubPr.get("html_url");


            entity.setPrUrl(
                    htmlUrl != null
                        ? htmlUrl.toString()
                        : null
            );


            // =================================================
            // STATE
            // =================================================

            Object state =
                    githubPr.get("state");


            entity.setGithubState(
                    state != null
                        ? state.toString().toUpperCase()
                        : "UNKNOWN"
            );


            /*
             * Keep DevCollab review status independent.
             * Only update OPEN/CLOSED automatically.
             */
            if (
                "OPEN".equals(
                    entity.getGithubState()
                ) &&
                (
                    entity.getStatus() == null ||
                    "OPEN".equals(
                        entity.getStatus()
                    )
                )
            ) {

                entity.setStatus(
                        "OPEN"
                );
            }


            if (
                "CLOSED".equals(
                    entity.getGithubState()
                )
                &&
                (
                    entity.getStatus() == null ||
                    "OPEN".equals(
                        entity.getStatus()
                    )
                )
            ) {

                entity.setStatus(
                        "CLOSED"
                );
            }


            // =================================================
            // HEAD BRANCH
            // =================================================

            Object headObject =
                    githubPr.get("head");


            if (headObject instanceof Map) {

                Map<?, ?> head =
                        (Map<?, ?>)
                            headObject;


                Object ref =
                        head.get("ref");


                entity.setSourceBranch(
                        ref != null
                            ? ref.toString()
                            : null
                );
            }


            // =================================================
            // BASE BRANCH
            // =================================================

            Object baseObject =
                    githubPr.get("base");


            if (baseObject instanceof Map) {

                Map<?, ?> base =
                        (Map<?, ?>)
                            baseObject;


                Object ref =
                        base.get("ref");


                entity.setTargetBranch(
                        ref != null
                            ? ref.toString()
                            : null
                );
            }


            // =================================================
            // GITHUB CREATED AT
            // =================================================

            entity.setGithubCreatedAt(
                    parseGitHubTimestamp(
                        githubPr.get(
                            "created_at"
                        )
                    )
            );


            // =================================================
            // GITHUB UPDATED AT
            // =================================================

            entity.setGithubUpdatedAt(
                    parseGitHubTimestamp(
                        githubPr.get(
                            "updated_at"
                        )
                    )
            );


            entity.setUpdatedAt(
                    new Timestamp(
                        System.currentTimeMillis()
                    )
            );


            PullRequestEntity saved =
                    pullRequestRepository.save(
                            entity
                    );


            result.add(saved);
        }


        return result;
    }


    // =====================================================
    // GET PROJECT PRs
    // =====================================================

    public List<PullRequestEntity>
    getProjectPullRequests(
            Long projectId) {

        return pullRequestRepository
                .findByProjectIdOrderByCreatedAtDesc(
                        projectId
                );
    }


    // =====================================================
    // GET TASK PRs
    // =====================================================

    public List<PullRequestEntity>
    getTaskPullRequests(
            Long taskId) {

        return pullRequestRepository
                .findByTaskIdOrderByCreatedAtDesc(
                        taskId
                );
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    public Optional<PullRequestEntity>
    getById(
            Long pullRequestId) {

        return pullRequestRepository
                .findById(
                    pullRequestId
                );
    }


    // =====================================================
    // LINK PR TO TASK
    // =====================================================

    public PullRequestEntity linkToTask(
            Long pullRequestId,
            Long taskId) {


        Optional<PullRequestEntity>
                optional =
                pullRequestRepository
                    .findById(
                        pullRequestId
                    );


        if (!optional.isPresent()) {

            return null;
        }


        PullRequestEntity pr =
                optional.get();


        pr.setTaskId(
                taskId
        );


        pr.setUpdatedAt(
                new Timestamp(
                    System.currentTimeMillis()
                )
        );


        return pullRequestRepository.save(
                pr
        );
    }


    // =====================================================
    // UPDATE DEV COLLAB STATUS
    // =====================================================

    public PullRequestEntity updateStatus(
            Long pullRequestId,
            String status) {


        Optional<PullRequestEntity>
                optional =
                pullRequestRepository
                    .findById(
                        pullRequestId
                    );


        if (!optional.isPresent()) {

            return null;
        }


        PullRequestEntity pr =
                optional.get();


        String normalized =
                status
                    .trim()
                    .toUpperCase();


        if (!isValidStatus(
                normalized
        )) {

            throw new RuntimeException(
                    "Invalid Pull Request status"
            );
        }


        pr.setStatus(
                normalized
        );


        pr.setUpdatedAt(
                new Timestamp(
                    System.currentTimeMillis()
                )
        );


        return pullRequestRepository.save(
                pr
        );
    }


    // =====================================================
    // DELETE LOCAL PR RECORD
    // =====================================================

    public boolean delete(
            Long pullRequestId) {


        if (
            !pullRequestRepository
                .existsById(
                    pullRequestId
                )
        ) {

            return false;
        }


        pullRequestRepository.deleteById(
                pullRequestId
        );


        return true;
    }


    // =====================================================
    // VALID STATUS
    // =====================================================

    private boolean isValidStatus(
            String status) {

        switch (status) {

            case "OPEN":
            case "AI_REVIEW":
            case "HUMAN_REVIEW":
            case "CHANGES_REQUESTED":
            case "APPROVED":
            case "MERGED":
            case "CLOSED":

                return true;

            default:

                return false;
        }
    }


    // =====================================================
    // PARSE GITHUB TIME
    // =====================================================

    private Timestamp parseGitHubTimestamp(
            Object value) {

        if (value == null) {

            return null;
        }


        try {

            OffsetDateTime dateTime =
                    OffsetDateTime.parse(
                        value.toString()
                    );


            return Timestamp.from(
                    dateTime.toInstant()
            );

        } catch (Exception e) {

            return null;
        }
    }
}