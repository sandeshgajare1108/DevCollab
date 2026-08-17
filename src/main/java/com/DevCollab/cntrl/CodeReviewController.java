
package com.DevCollab.cntrl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.DevCollab.entity.CodeReviewEntity;
import com.DevCollab.entity.UserEntity;
import com.DevCollab.service.CodeReviewService;
import com.DevCollab.service.UserService;

@RestController
@RequestMapping("/api/code-reviews")
@CrossOrigin
public class CodeReviewController {

    @Autowired
    private CodeReviewService codeReviewService;

    @Autowired
    private UserService userService;


    // =====================================================
    // CREATE HUMAN REVIEW
    // =====================================================

    @PostMapping("/pull-request/{pullRequestId}")
    public ResponseEntity<?> createReview(
            @PathVariable Long pullRequestId,
            @RequestBody ReviewRequest request,
            Authentication authentication) {

        try {

            // =================================================
            // AUTHENTICATION
            // =================================================

            if (!isAuthenticated(
                    authentication
            )) {

                return ResponseEntity
                        .status(
                            HttpStatus.UNAUTHORIZED
                        )
                        .body(
                            "Authentication required"
                        );
            }


            // =================================================
            // USER
            // =================================================

            UserEntity user =
                    getLoggedInUser(
                            authentication
                    );


            if (user == null) {

                return ResponseEntity
                        .status(
                            HttpStatus.UNAUTHORIZED
                        )
                        .body(
                            "Logged-in user not found"
                        );
            }


            // =================================================
            // REQUEST VALIDATION
            // =================================================

            if (request == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                            "Review request is required"
                        );
            }


            if (pullRequestId == null ||
                pullRequestId <= 0) {

                return ResponseEntity
                        .badRequest()
                        .body(
                            "Invalid Pull Request ID"
                        );
            }


            if (request.getDecision() == null ||
                request.getDecision()
                       .trim()
                       .isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body(
                            "Review decision is required"
                        );
            }


            // =================================================
            // CREATE REVIEW
            // =================================================

            CodeReviewEntity review =
                    codeReviewService.createReview(

                            pullRequestId,

                            user.getUserId(),

                            request.getReviewComment(),

                            request.getDecision()
                    );


            return ResponseEntity
                    .status(
                        HttpStatus.CREATED
                    )
                    .body(
                        review
                    );


        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(
                        HttpStatus.BAD_REQUEST
                    )
                    .body(
                        e.getMessage()
                    );


        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                        "Failed to create human code review"
                    );
        }
    }


    // =====================================================
    // GET PR REVIEWS
    // =====================================================

    @GetMapping("/pull-request/{pullRequestId}")
    public ResponseEntity<?> getPrReviews(
            @PathVariable Long pullRequestId,
            Authentication authentication) {

        if (!isAuthenticated(
                authentication
        )) {

            return ResponseEntity
                    .status(
                        HttpStatus.UNAUTHORIZED
                    )
                    .body(
                        "Authentication required"
                    );
        }


        return ResponseEntity.ok(
                codeReviewService
                        .getReviewsByPullRequest(
                                pullRequestId
                        )
        );
    }


    // =====================================================
    // GET PROJECT REVIEWS
    // =====================================================

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getProjectReviews(
            @PathVariable Long projectId,
            Authentication authentication) {

        if (!isAuthenticated(
                authentication
        )) {

            return ResponseEntity
                    .status(
                        HttpStatus.UNAUTHORIZED
                    )
                    .body(
                        "Authentication required"
                    );
        }


        return ResponseEntity.ok(
                codeReviewService
                        .getReviewsByProject(
                                projectId
                        )
        );
    }


    // =====================================================
    // GET TASK REVIEWS
    // =====================================================

    @GetMapping("/task/{taskId}")
    public ResponseEntity<?> getTaskReviews(
            @PathVariable Long taskId,
            Authentication authentication) {

        if (!isAuthenticated(
                authentication
        )) {

            return ResponseEntity
                    .status(
                        HttpStatus.UNAUTHORIZED
                    )
                    .body(
                        "Authentication required"
                    );
        }


        return ResponseEntity.ok(
                codeReviewService
                        .getReviewsByTask(
                                taskId
                        )
        );
    }


    // =====================================================
    // GET REVIEW BY ID
    // =====================================================

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReviewById(
            @PathVariable Long reviewId,
            Authentication authentication) {

        if (!isAuthenticated(
                authentication
        )) {

            return ResponseEntity
                    .status(
                        HttpStatus.UNAUTHORIZED
                    )
                    .body(
                        "Authentication required"
                    );
        }


        Optional<CodeReviewEntity> review =
                codeReviewService
                        .getReviewById(
                                reviewId
                        );


        if (!review.isPresent()) {

            return ResponseEntity
                    .status(
                        HttpStatus.NOT_FOUND
                    )
                    .body(
                        "Code review not found"
                    );
        }


        return ResponseEntity.ok(
                review.get()
        );
    }


    // =====================================================
    // AUTHENTICATION
    // =====================================================

    private boolean isAuthenticated(
            Authentication authentication) {

        return authentication != null &&
               authentication.isAuthenticated();
    }


    // =====================================================
    // USER
    // =====================================================

    private UserEntity getLoggedInUser(
            Authentication authentication) {

        if (authentication == null) {

            return null;
        }


        List<UserEntity> users =
                userService.getByUserEmail(
                        authentication.getName()
                );


        if (users == null ||
            users.isEmpty()) {

            return null;
        }


        return users.get(0);
    }


    // =====================================================
    // REQUEST DTO
    // =====================================================

    public static class ReviewRequest {

        private String reviewComment;

        private String decision;


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
    }
}

