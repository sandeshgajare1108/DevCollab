
package com.DevCollab.cntrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.DevCollab.dto.ChatMessageRequest;
import com.DevCollab.entity.ChatMessageEntity;
import com.DevCollab.service.ChatService;

@Controller
public class ChatWebSocketController {

    @Autowired
    private ChatService chatService;


    // =====================================================
    // SEND CHAT MESSAGE
    // =====================================================

    @MessageMapping("/chat.send")
    @SendTo("/topic/chat")
    public ChatMessageEntity sendMessage(
            ChatMessageRequest request,
            Authentication authentication) {


        // =================================================
        // AUTHENTICATION
        // =================================================

        if (authentication == null ||
            !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "Authentication required"
            );
        }


        // =================================================
        // GET USER ID FROM JWT
        // =================================================

        Object details =
                authentication.getDetails();


        if (!(details instanceof Long)) {

            throw new RuntimeException(
                    "User ID not found in authentication"
            );
        }


        Long senderId =
                (Long) details;
        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(
                            authority ->
                                "ROLE_ADMIN".equals(
                                    authority.getAuthority()
                                )
                        );

        if (!isAdmin &&
            !chatService.canAccessProject(
                request.getProjectId(),
                senderId
            )) {

            throw new RuntimeException(
                "You are not a member of this project"
            );
        }


        // =================================================
        // REQUEST VALIDATION
        // =================================================

        if (request == null) {

            throw new RuntimeException(
                    "Chat message is required"
            );
        }


        if (request.getProjectId() == null ||
            request.getProjectId() <= 0) {

            throw new RuntimeException(
                    "Valid project ID is required"
            );
        }


        if (request.getMessage() == null ||
            request.getMessage()
                   .trim()
                   .isEmpty()) {

            throw new RuntimeException(
                    "Message is required"
            );
        }


        System.out.println(
                "======================================"
        );

        System.out.println(
                "CHAT MESSAGE"
        );

        System.out.println(
                "SENDER ID  -> " + senderId
        );

        System.out.println(
                "PROJECT ID -> " +
                request.getProjectId()
        );

        System.out.println(
                "MESSAGE    -> " +
                request.getMessage()
        );

        System.out.println(
                "======================================"
        );


        // =================================================
        // SAVE
        // =================================================

        return chatService.saveMessage(
                request.getProjectId(),
                senderId,
                request.getMessage()
        );
    }
}
