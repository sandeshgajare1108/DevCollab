
package com.DevCollab.cntrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevCollab.entity.ChatMessageEntity;
import com.DevCollab.entity.UserEntity;
import com.DevCollab.service.ChatService;
import com.DevCollab.service.UserService;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;


    // =====================================================
    // GET PROJECT CHAT HISTORY
    // =====================================================

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getProjectMessages(
            @PathVariable Long projectId,
            Authentication authentication) {

        // =================================================
        // AUTHENTICATION CHECK
        // =================================================

        if (authentication == null ||
            !authentication.isAuthenticated()) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication required");
        }


        // =================================================
        // PROJECT ID VALIDATION
        // =================================================

        if (projectId == null ||
            projectId <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Invalid project ID");
        }


        try {

            // =============================================
            // GET LOGGED-IN USER USING JWT EMAIL
            // =============================================

            String email =
                    authentication.getName();


            if (email == null ||
                email.trim().isEmpty()) {

                return ResponseEntity
                        .status(
                            HttpStatus.UNAUTHORIZED
                        )
                        .body(
                            "User email not found in authentication"
                        );
            }


            List<UserEntity> users =
                    userService.getByUserEmail(
                            email
                    );


            if (users == null ||
                users.isEmpty()) {

                return ResponseEntity
                        .status(
                            HttpStatus.UNAUTHORIZED
                        )
                        .body(
                            "Logged-in user not found"
                        );
            }


            UserEntity loggedInUser =
                    users.get(0);


            Long userId =
                    loggedInUser.getUserId();


            // =============================================
            // ADMIN CHECK
            // =============================================

            boolean isAdmin =
                    authentication
                            .getAuthorities()
                            .stream()
                            .anyMatch(
                                authority ->
                                    "ROLE_ADMIN"
                                    .equals(
                                        authority
                                            .getAuthority()
                                    )
                            );


            // =============================================
            // PROJECT MEMBERSHIP CHECK
            // =============================================

            if (!isAdmin &&
                !chatService.canAccessProject(
                        projectId,
                        userId
                )) {

                return ResponseEntity
                        .status(
                            HttpStatus.FORBIDDEN
                        )
                        .body(
                            "You are not a member of this project"
                        );
            }


            // =============================================
            // LOAD CHAT HISTORY
            // =============================================

            List<ChatMessageEntity> messages =
                    chatService.getProjectMessages(
                            projectId
                    );


            return ResponseEntity.ok(
                    messages
            );


        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                        "Failed to load chat history"
                    );
        }
    }
}
