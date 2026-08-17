package com.DevCollab.cntrl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.DevCollab.entity.PullRequestEntity;
import com.DevCollab.entity.UserEntity;
import com.DevCollab.service.PullRequestService;
import com.DevCollab.service.UserService;

@RestController
@RequestMapping("/api/pull-requests")
@CrossOrigin
public class PullRequestController {

    @Autowired
    private PullRequestService pullRequestService;


    @Autowired
    private UserService userService;


    // =====================================================
    // SYNC GITHUB PRs
    // =====================================================

    @PostMapping("/sync/project/{projectId}")
    public ResponseEntity<?> syncPullRequests(
            @PathVariable Long projectId,
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


            List<PullRequestEntity> result =
                    pullRequestService
                        .syncPullRequests(
                            projectId,
                            user.getUserId()
                        );


            return ResponseEntity.ok(
                    result
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
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
                        "Failed to sync pull requests"
                    );
        }
    }


    // =====================================================
    // GET PROJECT PRs
    // =====================================================

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getProjectPullRequests(
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
                pullRequestService
                    .getProjectPullRequests(
                        projectId
                    )
        );
    }


    // =====================================================
    // GET TASK PRs
    // =====================================================

    @GetMapping("/task/{taskId}")
    public ResponseEntity<?> getTaskPullRequests(
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
                pullRequestService
                    .getTaskPullRequests(
                        taskId
                    )
        );
    }


    // =====================================================
    // GET PR BY ID
    // =====================================================

    @GetMapping("/{pullRequestId}")
    public ResponseEntity<?> getPullRequest(
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


        Optional<PullRequestEntity>
                pr =
                pullRequestService
                    .getById(
                        pullRequestId
                    );


        if (!pr.isPresent()) {

            return ResponseEntity
                    .status(
                        HttpStatus.NOT_FOUND
                    )
                    .body(
                        "Pull Request not found"
                    );
        }


        return ResponseEntity.ok(
                pr.get()
        );
    }


    // =====================================================
    // LINK PR TO TASK
    // =====================================================

    @PutMapping("/{pullRequestId}/task/{taskId}")
    public ResponseEntity<?> linkTask(
            @PathVariable Long pullRequestId,
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


        PullRequestEntity updated =
                pullRequestService.linkToTask(
                        pullRequestId,
                        taskId
                );


        if (updated == null) {

            return ResponseEntity
                    .status(
                        HttpStatus.NOT_FOUND
                    )
                    .body(
                        "Pull Request not found"
                    );
        }


        return ResponseEntity.ok(
                updated
        );
    }


    // =====================================================
    // UPDATE STATUS
    // =====================================================

    @PutMapping("/{pullRequestId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long pullRequestId,
            @RequestParam String status,
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


            PullRequestEntity updated =
                    pullRequestService.updateStatus(
                            pullRequestId,
                            status
                    );


            if (updated == null) {

                return ResponseEntity
                        .status(
                            HttpStatus.NOT_FOUND
                        )
                        .body(
                            "Pull Request not found"
                        );
            }


            return ResponseEntity.ok(
                    updated
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        e.getMessage()
                    );
        }
    }


    // =====================================================
    // DELETE LOCAL RECORD
    // =====================================================

    @DeleteMapping("/{pullRequestId}")
    public ResponseEntity<?> delete(
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


        boolean deleted =
                pullRequestService.delete(
                    pullRequestId
                );


        if (!deleted) {

            return ResponseEntity
                    .status(
                        HttpStatus.NOT_FOUND
                    )
                    .body(
                        "Pull Request not found"
                    );
        }


        return ResponseEntity.ok(
                "Pull Request record deleted"
        );
    }


    // =====================================================
    // AUTH
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


        String email =
                authentication.getName();


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