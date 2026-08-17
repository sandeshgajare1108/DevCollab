
package com.DevCollab.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.CodeReviewRepository;
import com.DevCollab.Repository.ProjectMemberRepository;
import com.DevCollab.Repository.ProjectRepository;
import com.DevCollab.Repository.PullRequestRepository;
import com.DevCollab.entity.CodeReviewEntity;
import com.DevCollab.entity.NotificationEntity;
import com.DevCollab.entity.ProjectEntity;
import com.DevCollab.entity.ProjectMemberEntity;
import com.DevCollab.entity.PullRequestEntity;

@Service
public class CodeReviewService {

    @Autowired
    private CodeReviewRepository codeReviewRepository;

    @Autowired
    private PullRequestRepository pullRequestRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private NotificationService notificationService;


    // =====================================================
    // CREATE HUMAN REVIEW
    // =====================================================

    public CodeReviewEntity createReview(
            Long pullRequestId,
            Long reviewerId,
            String reviewComment,
            String decision) {

        // =================================================
        // VALIDATION
        // =================================================

        if (pullRequestId == null) {

            throw new RuntimeException(
                    "Pull Request ID is required"
            );
        }


        if (reviewerId == null) {

            throw new RuntimeException(
                    "Reviewer ID is required"
            );
        }


        // =================================================
        // GET PULL REQUEST
        // =================================================

        Optional<PullRequestEntity> optionalPr =
                pullRequestRepository.findById(
                        pullRequestId
                );


        if (!optionalPr.isPresent()) {

            throw new RuntimeException(
                    "Pull Request not found"
            );
        }


        PullRequestEntity pr =
                optionalPr.get();


        if (pr.getProjectId() == null) {

            throw new RuntimeException(
                    "Pull Request project is missing"
            );
        }


        // =================================================
        // REVIEW AUTHORIZATION
        // =================================================

        if (!canReview(
                pr.getProjectId(),
                reviewerId
        )) {

            throw new RuntimeException(
                    "You are not authorized to review this Pull Request"
            );
        }


        // =================================================
        // NORMALIZE DECISION
        // =================================================

        String normalizedDecision =
                normalizeDecision(
                        decision
                );


        if (!isValidDecision(
                normalizedDecision
        )) {

            throw new RuntimeException(
                    "Invalid review decision. "
                    + "Use APPROVED or CHANGES_REQUESTED"
            );
        }


        // =================================================
        // CREATE REVIEW
        // =================================================

        CodeReviewEntity review =
                new CodeReviewEntity();


        review.setPullRequestId(
                pr.getPullRequestId()
        );


        review.setProjectId(
                pr.getProjectId()
        );


        review.setTaskId(
                pr.getTaskId()
        );


        review.setReviewerId(
                reviewerId
        );


        review.setReviewComment(
                reviewComment
        );


        review.setDecision(
                normalizedDecision
        );


        Timestamp now =
                new Timestamp(
                        System.currentTimeMillis()
                );


        review.setCreatedAt(now);

        review.setUpdatedAt(now);


        CodeReviewEntity savedReview =
                codeReviewRepository.save(
                        review
                );


        // =================================================
        // UPDATE PR STATUS
        // =================================================

        if ("APPROVED".equals(
                normalizedDecision
        )) {

            pr.setStatus(
                    "APPROVED"
            );

        } else {

            pr.setStatus(
                    "CHANGES_REQUESTED"
            );
        }


        pr.setUpdatedAt(
                new Timestamp(
                        System.currentTimeMillis()
                )
        );


        PullRequestEntity updatedPr =
                pullRequestRepository.save(
                        pr
                );


        // =================================================
        // NOTIFY PR CREATOR
        // =================================================

        createReviewNotification(
                updatedPr,
                normalizedDecision
        );


        return savedReview;
    }


    // =====================================================
    // PR REVIEW NOTIFICATION
    // =====================================================

    private void createReviewNotification(
            PullRequestEntity pr,
            String decision) {

        if (pr == null) {

            return;
        }


        // =================================================
        // GET PR CREATOR
        // =================================================

        Long developerId =
                pr.getCreatedBy();


        if (developerId == null) {

            System.out.println(
                    "PR NOTIFICATION SKIPPED: "
                    + "createdBy is null"
            );

            return;
        }


        String title;

        String message;

        String type;


        // =================================================
        // APPROVED
        // =================================================

        if ("APPROVED".equals(
                decision
        )) {

            title =
                    "Pull Request Approved";

            message =
                    "Your Pull Request #"
                    + pr.getPullRequestId()
                    + " has been approved.";

            type =
                    "PR_APPROVED";


        // =================================================
        // CHANGES REQUESTED
        // =================================================

        } else {

            title =
                    "Changes Requested";

            message =
                    "Changes have been requested on "
                    + "Pull Request #"
                    + pr.getPullRequestId()
                    + ".";

            type =
                    "PR_CHANGES_REQUESTED";
        }


        try {

            NotificationEntity notification =
                    notificationService.createNotification(

                            developerId,

                            title,

                            message,

                            type
                    );


            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "PR REVIEW NOTIFICATION CREATED"
            );

            System.out.println(
                    "PR ID -> "
                    + pr.getPullRequestId()
            );

            System.out.println(
                    "PR CREATOR / DEVELOPER ID -> "
                    + developerId
            );

            System.out.println(
                    "DECISION -> "
                    + decision
            );

            System.out.println(
                    "NOTIFICATION ID -> "
                    + notification.getNotificationId()
            );

            System.out.println(
                    "TYPE -> "
                    + notification.getType()
            );

            System.out.println(
                    "======================================"
            );


        } catch (Exception e) {

            /*
             * Review creation must not fail just
             * because notification creation failed.
             */

            System.out.println(
                    "PR REVIEW NOTIFICATION ERROR -> "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // GET REVIEWS FOR PR
    // =====================================================

    public List<CodeReviewEntity>
    getReviewsByPullRequest(
            Long pullRequestId) {

        return codeReviewRepository
                .findByPullRequestIdOrderByCreatedAtDesc(
                        pullRequestId
                );
    }


    // =====================================================
    // GET PROJECT REVIEWS
    // =====================================================

    public List<CodeReviewEntity>
    getReviewsByProject(
            Long projectId) {

        return codeReviewRepository
                .findByProjectIdOrderByCreatedAtDesc(
                        projectId
                );
    }


    // =====================================================
    // GET TASK REVIEWS
    // =====================================================

    public List<CodeReviewEntity>
    getReviewsByTask(
            Long taskId) {

        return codeReviewRepository
                .findByTaskIdOrderByCreatedAtDesc(
                        taskId
                );
    }


    // =====================================================
    // GET REVIEW BY ID
    // =====================================================

    public Optional<CodeReviewEntity>
    getReviewById(
            Long reviewId) {

        return codeReviewRepository.findById(
                reviewId
        );
    }


    // =====================================================
    // REVIEW AUTHORIZATION
    // =====================================================

    private boolean canReview(
            Long projectId,
            Long userId) {

        // =================================================
        // PROJECT OWNER
        // =================================================

        Optional<ProjectEntity> optionalProject =
                projectRepository.findById(
                        projectId
                );


        if (optionalProject.isPresent()) {

            ProjectEntity project =
                    optionalProject.get();


            if (project.getOwnerId() != null &&
                project.getOwnerId().equals(
                        userId
                )) {

                return true;
            }
        }


        // =================================================
        // PROJECT MANAGER
        // =================================================

        return projectMemberRepository
                .existsByProjectIdAndUserIdAndMemberRole(
                        projectId,
                        userId,
                        ProjectMemberEntity.MemberRole.MANAGER
                );
    }


    // =====================================================
    // NORMALIZE DECISION
    // =====================================================

    private String normalizeDecision(
            String decision) {

        if (decision == null) {

            return "";
        }


        return decision
                .trim()
                .toUpperCase();
    }


    // =====================================================
    // VALID DECISION
    // =====================================================

    private boolean isValidDecision(
            String decision) {

        return
            "APPROVED".equals(
                decision
            )
            ||
            "CHANGES_REQUESTED".equals(
                decision
            );
    }
}
