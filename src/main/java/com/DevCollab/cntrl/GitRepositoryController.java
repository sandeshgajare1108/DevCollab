package com.DevCollab.cntrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.DevCollab.entity.GitRepositoryEntity;
import com.DevCollab.entity.UserEntity;
import com.DevCollab.service.GitRepositoryService;
import com.DevCollab.service.UserService;

@RestController
@RequestMapping("/api/github")
@CrossOrigin
public class GitRepositoryController {

    @Autowired
    private GitRepositoryService gitRepositoryService;

    @Autowired
    private UserService userService;


    // =====================================================
    // CONNECT
    // =====================================================

    @PostMapping("/repositories/connect")
    public ResponseEntity<?> connectRepository(
            @RequestBody ConnectRepositoryRequest request,
            Authentication authentication) {

        try {

            if (!isAuthenticated(authentication)) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Authentication required");
            }


            if (request == null ||
                request.getProjectId() == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Project ID is required");
            }


            if (request.getGithubUrl() == null ||
                request.getGithubUrl()
                        .trim()
                        .isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body(
                            "GitHub repository URL is required"
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


            GitRepositoryEntity saved =
                    gitRepositoryService
                            .connectRepository(
                                    request.getProjectId(),
                                    user.getUserId(),
                                    request.getGithubUrl()
                            );


            return ResponseEntity.ok(saved);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                        "Failed to connect GitHub repository"
                    );
        }
    }


    // =====================================================
    // GET CONNECTED REPOSITORY
    // =====================================================

    @GetMapping("/repositories/project/{projectId}")
    public ResponseEntity<?> getRepository(
            @PathVariable Long projectId,
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                        "Authentication required"
                    );
        }


        Optional<GitRepositoryEntity>
                repository =
                gitRepositoryService
                        .getByProjectId(projectId);


        if (!repository.isPresent()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                        "No GitHub repository connected"
                    );
        }


        return ResponseEntity.ok(
                repository.get()
        );
    }


    // =====================================================
    // COMMITS
    // =====================================================

    @GetMapping("/repositories/project/{projectId}/commits")
    public ResponseEntity<?> getCommits(
            @PathVariable Long projectId,
            Authentication authentication) {

        try {

            if (!isAuthenticated(authentication)) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(
                            "Authentication required"
                        );
            }


            return ResponseEntity.ok(
                    gitRepositoryService
                            .getCommits(projectId)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                        "Failed to load GitHub commits"
                    );
        }
    }


    // =====================================================
    // PULL REQUESTS
    // =====================================================

    @GetMapping("/repositories/project/{projectId}/pulls")
    public ResponseEntity<?> getPullRequests(
            @PathVariable Long projectId,
            @RequestParam(
                defaultValue = "open"
            )
            String state,
            Authentication authentication) {

        try {

            if (!isAuthenticated(authentication)) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(
                            "Authentication required"
                        );
            }


            return ResponseEntity.ok(
                    gitRepositoryService
                            .getPullRequests(
                                    projectId,
                                    state
                            )
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                        "Failed to load GitHub pull requests"
                    );
        }
    }


    // =====================================================
    // DISCONNECT
    // =====================================================

    @DeleteMapping(
        "/repositories/project/{projectId}"
    )
    public ResponseEntity<?> disconnectRepository(
            @PathVariable Long projectId,
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                        "Authentication required"
                    );
        }


        boolean deleted =
                gitRepositoryService
                        .deleteByProjectId(
                                projectId
                        );


        if (!deleted) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                        "GitHub repository not connected"
                    );
        }


        return ResponseEntity.ok(
                "GitHub repository disconnected"
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
                userService.getByUserEmail(email);


        if (users == null ||
            users.isEmpty()) {

            return null;
        }


        return users.get(0);
    }


    // =====================================================
    // REQUEST DTO
    // =====================================================

    public static class ConnectRepositoryRequest {

        private Long projectId;

        private String githubUrl;


        public Long getProjectId() {
            return projectId;
        }


        public void setProjectId(
                Long projectId) {

            this.projectId = projectId;
        }


        public String getGithubUrl() {
            return githubUrl;
        }


        public void setGithubUrl(
                String githubUrl) {

            this.githubUrl = githubUrl;
        }
    }
}