package com.DevCollab.cntrl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.DevCollab.entity.BugEntity;
import com.DevCollab.entity.UserEntity;
import com.DevCollab.service.BugService;
import com.DevCollab.service.UserService;

@RestController
@RequestMapping("/api/bugs")
@CrossOrigin
public class BugController {

    @Autowired
    private BugService bugService;

    @Autowired
    private UserService userService;


    // =====================================================
    // CREATE BUG
    // =====================================================

    @PostMapping
    public ResponseEntity<?> createBug(
            @RequestBody BugEntity bug,
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

            /*
             * Reporter always comes from JWT.
             * Do not trust frontend reportedBy.
             */

            bug.setReportedBy(
                    user.getUserId()
            );

            BugEntity saved =
                    bugService.createBug(
                            bug
                    );

            return ResponseEntity
                    .status(
                        HttpStatus.CREATED
                    )
                    .body(
                        saved
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
                        "Failed to create bug"
                    );
        }
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ResponseEntity<?> getAllBugs(
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
                bugService.getAllBugs()
        );
    }


    // =====================================================
    // GET BUG
    // =====================================================

    @GetMapping("/{bugId}")
    public ResponseEntity<?> getBugById(
            @PathVariable Long bugId,
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

        Optional<BugEntity> bug =
                bugService.getBugById(
                        bugId
                );

        if (!bug.isPresent()) {

            return ResponseEntity
                    .status(
                        HttpStatus.NOT_FOUND
                    )
                    .body(
                        "Bug not found"
                    );
        }

        return ResponseEntity.ok(
                bug.get()
        );
    }


    // =====================================================
    // GET PROJECT BUGS
    // =====================================================

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getProjectBugs(
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
                bugService.getProjectBugs(
                        projectId
                )
        );
    }


    // =====================================================
    // GET TASK BUGS
    // =====================================================

    @GetMapping("/task/{taskId}")
    public ResponseEntity<?> getTaskBugs(
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
                bugService.getTaskBugs(
                        taskId
                )
        );
    }


    // =====================================================
    // GET PR BUGS
    // =====================================================

    @GetMapping(
        "/pull-request/{pullRequestId}"
    )
    public ResponseEntity<?> getPullRequestBugs(
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
                bugService
                    .getPullRequestBugs(
                        pullRequestId
                    )
        );
    }


    // =====================================================
    // GET ASSIGNED BUGS
    // =====================================================

    @GetMapping(
        "/assigned/{userId}"
    )
    public ResponseEntity<?> getAssignedBugs(
            @PathVariable Long userId,
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
                bugService.getAssignedBugs(
                        userId
                )
        );
    }


    // =====================================================
    // ASSIGN BUG
    // =====================================================

    @PutMapping("/{bugId}/assign")
    public ResponseEntity<?> assignBug(
            @PathVariable Long bugId,
            @RequestParam Long assignedTo,
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

            BugEntity updated =
                    bugService.assignBug(
                            bugId,
                            assignedTo
                    );

            if (updated == null) {

                return ResponseEntity
                        .status(
                            HttpStatus.NOT_FOUND
                        )
                        .body(
                            "Bug not found"
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
    // UPDATE STATUS
    // =====================================================

    @PutMapping("/{bugId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long bugId,
            @RequestParam String status,
            @RequestParam(
                required = false
            )
            String resolution,
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

            BugEntity updated =
                    bugService.updateStatus(
                            bugId,
                            status,
                            resolution
                    );

            if (updated == null) {

                return ResponseEntity
                        .status(
                            HttpStatus.NOT_FOUND
                        )
                        .body(
                            "Bug not found"
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
    // UPDATE BUG
    // =====================================================

    @PutMapping("/{bugId}")
    public ResponseEntity<?> updateBug(
            @PathVariable Long bugId,
            @RequestBody BugEntity request,
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

            BugEntity updated =
                    bugService.updateBug(
                            bugId,
                            request
                    );

            if (updated == null) {

                return ResponseEntity
                        .status(
                            HttpStatus.NOT_FOUND
                        )
                        .body(
                            "Bug not found"
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
    // DELETE BUG
    // =====================================================

    @DeleteMapping("/{bugId}")
    public ResponseEntity<?> deleteBug(
            @PathVariable Long bugId,
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
                bugService.deleteBug(
                        bugId
                );

        if (!deleted) {

            return ResponseEntity
                    .status(
                        HttpStatus.NOT_FOUND
                    )
                    .body(
                        "Bug not found"
                    );
        }

        return ResponseEntity.ok(
                "Bug deleted successfully"
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
}