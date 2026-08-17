package com.DevCollab.cntrl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.DevCollab.entity.ProjectEntity;
import com.DevCollab.entity.ProjectMemberEntity;
import com.DevCollab.entity.UserEntity;
import com.DevCollab.service.ProjectMemberService;
import com.DevCollab.service.ProjectService;
import com.DevCollab.service.UserService;

@RestController
@RequestMapping("/api/project-members")
@CrossOrigin
public class ProjectMemberController {

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;


    // =====================================================
    // GET ALL MEMBERS
    // =====================================================

    @GetMapping
    public ResponseEntity<?> getAllMembers(
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {
            return unauthorized();
        }

        return ResponseEntity.ok(
                projectMemberService.getAllMembers()
        );
    }


    // =====================================================
    // GET MEMBER BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<?> getMemberById(
            @PathVariable Long id,
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {
            return unauthorized();
        }

        if (id == null || id <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Invalid project member ID");
        }

        Optional<ProjectMemberEntity> member =
                projectMemberService.getMemberById(id);

        if (!member.isPresent()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Project member not found");
        }

        return ResponseEntity.ok(member.get());
    }


    // =====================================================
    // GET MEMBERS BY PROJECT
    // =====================================================

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getMembersByProject(
            @PathVariable Long projectId,
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {
            return unauthorized();
        }

        if (projectId == null || projectId <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Invalid project ID");
        }

        return ResponseEntity.ok(
                projectMemberService
                        .getMembersByProject(projectId)
        );
    }


    // =====================================================
    // GET MEMBERS BY USER
    // =====================================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getMembersByUser(
            @PathVariable Long userId,
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {
            return unauthorized();
        }

        if (userId == null || userId <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Invalid user ID");
        }

        return ResponseEntity.ok(
                projectMemberService
                        .getMembersByUser(userId)
        );
    }


    // =====================================================
    // CREATE MEMBER
    // =====================================================

    @PostMapping
    public ResponseEntity<?> createMember(
            @RequestBody ProjectMemberEntity member,
            Authentication authentication) {

        try {

            if (!isAuthenticated(authentication)) {
                return unauthorized();
            }

            if (member == null ||
                member.getProjectId() == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Project ID is required");
            }

            UserEntity loggedInUser =
                    getLoggedInUser(authentication);

            if (loggedInUser == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Logged-in user not found");
            }

            if (!canManageProject(
                    member.getProjectId(),
                    loggedInUser.getUserId(),
                    authentication)) {

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(
                            "You cannot manage members of this project"
                        );
            }

            ProjectMemberEntity saved =
                    projectMemberService
                            .createMember(member);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(saved);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                        "Failed to create project member"
                    );
        }
    }


    // =====================================================
    // UPDATE MEMBER
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(
            @PathVariable Long id,
            @RequestBody ProjectMemberEntity member,
            Authentication authentication) {

        try {

            if (!isAuthenticated(authentication)) {
                return unauthorized();
            }

            if (id == null || id <= 0) {

                return ResponseEntity
                        .badRequest()
                        .body("Invalid project member ID");
            }

            Optional<ProjectMemberEntity> existing =
                    projectMemberService.getMemberById(id);

            if (!existing.isPresent()) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Project member not found");
            }

            UserEntity loggedInUser =
                    getLoggedInUser(authentication);

            if (loggedInUser == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Logged-in user not found");
            }

            Long projectId =
                    existing.get().getProjectId();

            /*
             * Use existing project's ID for authorization.
             * Do not trust projectId sent by frontend.
             */
            if (!canManageProject(
                    projectId,
                    loggedInUser.getUserId(),
                    authentication)) {

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(
                            "You cannot update this project member"
                        );
            }

            ProjectMemberEntity updated =
                    projectMemberService
                            .updateMember(id, member);

            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                        "Failed to update project member"
                    );
        }
    }


    // =====================================================
    // DELETE MEMBER
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(
            @PathVariable Long id,
            Authentication authentication) {

        try {

            if (!isAuthenticated(authentication)) {
                return unauthorized();
            }

            if (id == null || id <= 0) {

                return ResponseEntity
                        .badRequest()
                        .body(
                            "Invalid project member ID"
                        );
            }

            Optional<ProjectMemberEntity> existing =
                    projectMemberService.getMemberById(id);

            if (!existing.isPresent()) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(
                            "Project member not found"
                        );
            }

            UserEntity loggedInUser =
                    getLoggedInUser(authentication);

            if (loggedInUser == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Logged-in user not found");
            }

            Long projectId =
                    existing.get().getProjectId();

            if (!canManageProject(
                    projectId,
                    loggedInUser.getUserId(),
                    authentication)) {

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(
                            "You cannot delete this project member"
                        );
            }

            boolean deleted =
                    projectMemberService.deleteMember(id);

            if (!deleted) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(
                            "Project member not found"
                        );
            }

            return ResponseEntity.ok(
                    "Project member deleted successfully"
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                        "Failed to delete project member"
                    );
        }
    }


    // =====================================================
    // GET ALL MEMBERS WITH USER DETAILS
    // =====================================================

    @GetMapping("/details")
    public ResponseEntity<?> getAllMembersWithUserDetails(
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {
            return unauthorized();
        }

        try {

            return ResponseEntity.ok(
                    projectMemberService
                            .getAllMembersWithUserDetails()
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                        "Failed to load team member details"
                    );
        }
    }


    // =====================================================
    // GET PROJECT MEMBERS WITH USER DETAILS
    // =====================================================

    @GetMapping("/details/project/{projectId}")
    public ResponseEntity<?> getProjectMembersWithUserDetails(
            @PathVariable Long projectId,
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {
            return unauthorized();
        }

        try {

            if (projectId == null || projectId <= 0) {

                return ResponseEntity
                        .badRequest()
                        .body("Invalid project ID");
            }

            return ResponseEntity.ok(
                    projectMemberService
                            .getProjectMembersWithUserDetails(
                                    projectId
                            )
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                        "Failed to load project team details"
                    );
        }
    }


    // =====================================================
    // CAN MANAGE PROJECT
    // =====================================================

    private boolean canManageProject(
            Long projectId,
            Long userId,
            Authentication authentication) {

        if (isAdmin(authentication)) {
            return true;
        }

        Optional<ProjectEntity> project =
                projectService.getProjectById(projectId);

        if (!project.isPresent()) {
            return false;
        }

        return userId.equals(
                project.get().getOwnerId()
        );
    }


    // =====================================================
    // ADMIN
    // =====================================================

    private boolean isAdmin(
            Authentication authentication) {

        return authentication
                .getAuthorities()
                .stream()
                .anyMatch(
                    authority ->
                        "ROLE_ADMIN".equals(
                            authority.getAuthority()
                        )
                );
    }


    // =====================================================
    // AUTHENTICATED
    // =====================================================

    private boolean isAuthenticated(
            Authentication authentication) {

        return authentication != null &&
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

        List<UserEntity> users =
                userService.getByUserEmail(email);

        if (users == null ||
            users.isEmpty()) {

            return null;
        }

        return users.get(0);
    }


    private ResponseEntity<?> unauthorized() {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Authentication required");
    }
}