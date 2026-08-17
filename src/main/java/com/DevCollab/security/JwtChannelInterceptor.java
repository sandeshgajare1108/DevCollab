
package com.DevCollab.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtChannelInterceptor
        implements ChannelInterceptor {

    @Autowired
    private JwtService jwtService;


    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message,
                        StompHeaderAccessor.class
                );


        if (accessor == null) {
            return message;
        }


        // =================================================
        // STOMP CONNECT
        // =================================================

        if (StompCommand.CONNECT.equals(
                accessor.getCommand())) {

            String authorization =
                    accessor.getFirstNativeHeader(
                            "Authorization"
                    );


            if (authorization == null ||
                !authorization.startsWith("Bearer ")) {

                throw new IllegalArgumentException(
                        "JWT token is required"
                );
            }


            String token =
                    authorization.substring(7);


            // =================================================
            // VALIDATE JWT
            // =================================================

            if (!jwtService.validateToken(token)) {

                throw new IllegalArgumentException(
                        "Invalid JWT token"
                );
            }


            // =================================================
            // EXTRACT JWT DATA
            // =================================================

            String email =
                    jwtService.extractEmail(token);


            Long userId =
                    jwtService.extractUserId(token);


            List<String> roles =
                    jwtService.extractRoles(token);


            // =================================================
            // AUTHORITIES
            // =================================================

            List<SimpleGrantedAuthority>
                    authorities =
                    new ArrayList<SimpleGrantedAuthority>();


            if (roles != null) {

                for (String role : roles) {

                    if (role == null ||
                        role.trim().isEmpty()) {

                        continue;
                    }


                    role =
                            role.trim()
                                .toUpperCase();


                    if (!role.startsWith("ROLE_")) {

                        role =
                                "ROLE_" + role;
                    }


                    authorities.add(
                            new SimpleGrantedAuthority(
                                    role
                            )
                    );
                }
            }


            // =================================================
            // AUTHENTICATION
            //
            // principal = email
            // details    = userId
            // =================================================

            UsernamePasswordAuthenticationToken
                    authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            authorities
                    );


            authentication.setDetails(
                    userId
            );


            // =================================================
            // SET USER ON STOMP SESSION
            // =================================================

            accessor.setUser(
                    authentication
            );


            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "STOMP JWT AUTHENTICATED"
            );

            System.out.println(
                    "EMAIL    -> " + email
            );

            System.out.println(
                    "USER ID  -> " + userId
            );

            System.out.println(
                    "ROLES    -> " + authorities
            );

            System.out.println(
                    "======================================"
            );
        }


        return message;
    }
}
