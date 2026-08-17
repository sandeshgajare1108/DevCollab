
package com.DevCollab.cntrl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.DevCollab.dto.AiCodeReviewRequest;
import com.DevCollab.entity.AiCodeReviewEntity;
import com.DevCollab.entity.UserEntity;
import com.DevCollab.service.AiCodeReviewService;
import com.DevCollab.service.UserService;

@RestController
@RequestMapping("/api/ai-reviews")
@CrossOrigin
public class AiCodeReviewController {

    @Autowired
    private AiCodeReviewService aiCodeReviewService;


    @Autowired
    private UserService userService;


    // =====================================================
    // MANUAL CODE ANALYSIS
    // =====================================================

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeCode(
            @RequestBody AiCodeReviewRequest request,
            Authentication authentication) {

        try {

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


            AiCodeReviewEntity review =
                    aiCodeReviewService.analyzeCode(
                            request,
                            user.getUserId()
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
                            "AI code review failed"
                    );
        }
    }


    // =====================================================
    // AUTOMATIC GITHUB PR ANALYSIS
    // =====================================================

    @PostMapping(
            "/pull-request/{pullRequestId}"
    )
    public ResponseEntity<?> analyzePullRequest(
            @PathVariable Long pullRequestId,
            Authentication authentication) {

        try {

            // =============================================
            // AUTHENTICATION
            // =============================================

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


            // =============================================
            // LOGGED USER
            // =============================================

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


            // =============================================
            // VALID PR ID
            // =============================================

            if (pullRequestId == null ||
                pullRequestId <= 0) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Invalid Pull Request ID"
                        );
            }


            // =============================================
            // AUTOMATIC REVIEW
            // =============================================

            AiCodeReviewEntity review =
                    aiCodeReviewService
                            .analyzePullRequest(
                                    pullRequestId,
                                    user.getUserId()
                            );


            return ResponseEntity
                    .status(
                            HttpStatus.CREATED
                    )
                    .body(
                            review
                    );


        } catch (RuntimeException e) {

            e.printStackTrace();

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
                            "Automatic GitHub Pull Request AI review failed"
                    );
        }
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


        Optional<AiCodeReviewEntity> review =
                aiCodeReviewService
                        .getReviewById(
                                reviewId
                        );


        if (!review.isPresent()) {

            return ResponseEntity
                    .status(
                            HttpStatus.NOT_FOUND
                    )
                    .body(
                            "AI review not found"
                    );
        }


        return ResponseEntity.ok(
                review.get()
        );
    }


    // =====================================================
    // GET PR REVIEWS
    // =====================================================

    @GetMapping(
            "/pull-request/{pullRequestId}"
    )
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
                aiCodeReviewService
                        .getReviewsByPullRequest(
                                pullRequestId
                        )
        );
    }


    // =====================================================
    // GET TASK REVIEWS
    // =====================================================

    @GetMapping(
            "/task/{taskId}"
    )
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
                aiCodeReviewService
                        .getReviewsByTask(
                                taskId
                        )
        );
    }


    // =====================================================
    // AUTHENTICATION
    // =====================================================

    private boolean isAuthenticated(
            Authentication authentication) {

        return authentication != null
                &&
                authentication.isAuthenticated();
    }


    // =====================================================
    // GET LOGGED USER
    // =====================================================

    private UserEntity getLoggedInUser(
            Authentication authentication) {

        if (authentication == null) {

            return null;
        }


        String email =
                authentication.getName();


        if (email == null ||
            email.trim().isEmpty()) {

            return null;
        }


        List<UserEntity> users =
                userService.getByUserEmail(
                        email
                );


        if (users == null ||
            users.isEmpty()) {

            return null;
        }


        return users.get(0);
    }
}
