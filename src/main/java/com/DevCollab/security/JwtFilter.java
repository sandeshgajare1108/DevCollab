package com.DevCollab.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {


    @Autowired
    private JwtService jwtService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        System.out.println(
                "======================================"
        );

        System.out.println(
                "JWT FILTER -> " +
                request.getMethod() +
                " " +
                request.getRequestURI()
        );


        // =================================================
        // AUTHORIZATION HEADER
        // =================================================

        String authorizationHeader =
                request.getHeader("Authorization");


        System.out.println(
                "Authorization Header -> " +
                authorizationHeader
        );


        // =================================================
        // TOKEN NOT FOUND
        // =================================================

        if (authorizationHeader == null ||
            !authorizationHeader.startsWith("Bearer ")) {


            System.out.println(
                    "JWT FILTER -> TOKEN NOT FOUND"
            );


            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }


        // =================================================
        // EXTRACT TOKEN
        // =================================================

        String token =
                authorizationHeader.substring(7);


        try {


            // =================================================
            // VALIDATE TOKEN
            // =================================================

            boolean valid =
                    jwtService.validateToken(token);


            System.out.println(
                    "JWT VALID -> " + valid
            );


            if (!valid) {

                System.out.println(
                        "JWT FILTER -> INVALID TOKEN"
                );

                filterChain.doFilter(
                        request,
                        response
                );

                return;
            }


            // =================================================
            // EMAIL
            // =================================================

            String email =
                    jwtService.extractEmail(token);


            System.out.println(
                    "JWT EMAIL -> " + email
            );


            // =================================================
            // USER ID
            // =================================================

            Long userId =
                    jwtService.extractUserId(token);


            System.out.println(
                    "JWT USER ID -> " + userId
            );


            // =================================================
            // ROLES
            // =================================================

            List<String> roles =
                    jwtService.extractRoles(token);


            System.out.println(
                    "JWT ROLES -> " + roles
            );


            // =================================================
            // AUTHORITIES
            // =================================================

            List<SimpleGrantedAuthority>
                    authorities =
                    new ArrayList<>();


            if (roles != null) {


                for (String role : roles) {


                    if (role == null ||
                        role.trim().isEmpty()) {

                        continue;
                    }


                    role =
                        role.trim()
                            .toUpperCase();


                    /*
                     * Spring Security:
                     *
                     * hasRole("ADMIN")
                     *
                     * expects:
                     *
                     * ROLE_ADMIN
                     */

                    if (!role.startsWith("ROLE_")) {

                        role =
                            "ROLE_" + role;
                    }


                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority(
                                    role
                            );


                    authorities.add(
                            authority
                    );


                    System.out.println(
                            "GRANTED AUTHORITY -> " +
                            role
                    );
                }
            }


            // =================================================
            // CREATE AUTHENTICATION
            // =================================================

            JwtUserPrincipal principal =
                    new JwtUserPrincipal(
                            userId,
                            email
                    );


            UsernamePasswordAuthenticationToken
                    authentication =

                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            authorities
                    );

            // =================================================
            // SET SECURITY CONTEXT
            // =================================================

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(
                            authentication
                    );


            System.out.println(
                    "JWT AUTHENTICATED -> " +
                    email
            );


            System.out.println(
                    "AUTHORITIES -> " +
                    authorities
            );


        } catch (Exception e) {


            System.out.println(
                    "JWT FILTER ERROR -> " +
                    e.getClass().getName()
            );


            System.out.println(
                    "JWT FILTER ERROR MESSAGE -> " +
                    e.getMessage()
            );


            SecurityContextHolder
                    .clearContext();
        }


        System.out.println(
                "======================================"
        );


        // =================================================
        // CONTINUE REQUEST
        // =================================================

        filterChain.doFilter(
                request,
                response
        );
    }
}