package com.DevCollab.cntrl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.DevCollab.entity.UserEntity;
import com.DevCollab.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;


    // =========================================================
    // GET ALL USERS
    // =========================================================

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            Authentication authentication) {

        if (authentication == null ||
            !authentication.isAuthenticated()) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication required");
        }

        /*
         * Only ADMIN should get all users.
         */
        if (!isAdmin(authentication)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Only ADMIN can view all users");
        }

        return ResponseEntity.ok(
                userService.allRecordsUsers()
        );
    }


    // =========================================================
    // GET USER BY ID
    // =========================================================

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(
            @PathVariable Long userId,
            Authentication authentication) {

        if (userId == null || userId <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Invalid user ID");
        }


        if (authentication == null ||
            !authentication.isAuthenticated()) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication required");
        }


        UserEntity loggedInUser =
                getLoggedInUser(authentication);


        if (loggedInUser == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Logged-in user not found");
        }


        /*
         * ADMIN can view any user.
         *
         * Normal user can view only own profile.
         */
        if (!isAdmin(authentication) &&
            !loggedInUser.getUserId().equals(userId)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                        "You cannot view another user's profile"
                    );
        }


        UserEntity user =
                userService.getUserById(userId);


        if (user == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }


        return ResponseEntity.ok(user);
    }


    // =========================================================
    // UPDATE USER PROFILE
    // =========================================================

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestBody UserEntity request,
            Authentication authentication) {

        try {

            if (userId == null || userId <= 0) {

                return ResponseEntity
                        .badRequest()
                        .body("Invalid user ID");
            }


            if (request == null) {

                return ResponseEntity
                        .badRequest()
                        .body("User data is required");
            }


            if (authentication == null ||
                !authentication.isAuthenticated()) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Authentication required");
            }


            UserEntity loggedInUser =
                    getLoggedInUser(authentication);


            if (loggedInUser == null) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Logged-in user not found");
            }


            /*
             * ADMIN can update any user.
             *
             * Normal user can update only own profile.
             */
            if (!isAdmin(authentication) &&
                !loggedInUser.getUserId().equals(userId)) {

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(
                            "You cannot update another user's profile"
                        );
            }


            /*
             * Validate Full Name
             */
            if (request.getFullName() == null ||
                request.getFullName().trim().isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("Full name is required");
            }


            /*
             * Validate Email
             */
            if (request.getEmail() == null ||
                request.getEmail().trim().isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("Email is required");
            }


            UserEntity updatedUser =
                    userService.updateUser(
                            userId,
                            request.getFullName().trim(),
                            request.getEmail().trim(),
                            request.getMobile()
                    );


            if (updatedUser == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }


            return ResponseEntity.ok(updatedUser);

        } catch (RuntimeException e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                        "Failed to update user: "
                        + e.getMessage()
                    );
        }
    }


    // =========================================================
    // GET USERS BY STATUS
    // =========================================================

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getByUserStatus(
            @PathVariable String status,
            Authentication authentication) {

        if (authentication == null ||
            !authentication.isAuthenticated()) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication required");
        }


        /*
         * Only ADMIN should search all users by status.
         */
        if (!isAdmin(authentication)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Only ADMIN can search users by status");
        }


        return ResponseEntity.ok(
                userService.getByUserStatus(status)
        );
    }


    // =========================================================
    // GET USERS BY EMAIL
    // =========================================================

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getByUserEmail(
            @PathVariable String email,
            Authentication authentication) {

        if (authentication == null ||
            !authentication.isAuthenticated()) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication required");
        }


        /*
         * Only ADMIN should search users by email.
         */
        if (!isAdmin(authentication)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Only ADMIN can search users by email");
        }


        return ResponseEntity.ok(
                userService.getByUserEmail(email)
        );
    }


    // =========================================================
    // CHECK EMAIL
    // =========================================================

    @PostMapping("/CheckEmail/{email}")
    public ResponseEntity<?> checkUserEmail(
            @PathVariable String email) {

        return ResponseEntity.ok(
                userService.checkUserEmail(email)
        );
    }


    // =========================================================
    // SEARCH BY NAME
    // =========================================================

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getByUserName(
            @PathVariable String name,
            Authentication authentication) {

        if (authentication == null ||
            !authentication.isAuthenticated()) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication required");
        }


        /*
         * Only ADMIN should search users by name.
         */
        if (!isAdmin(authentication)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Only ADMIN can search users by name");
        }


        return ResponseEntity.ok(
                userService.getByUserName(name)
        );
    }


    // =========================================================
    // CHECK ADMIN ROLE
    // =========================================================

    private boolean isAdmin(
            Authentication authentication) {

        if (authentication == null) {

            return false;
        }


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


    // =========================================================
    // GET LOGGED-IN USER
    // =========================================================

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
                userService.getByUserEmail(email);


        if (users == null ||
            users.isEmpty()) {

            return null;
        }


        return users.get(0);
    }
}