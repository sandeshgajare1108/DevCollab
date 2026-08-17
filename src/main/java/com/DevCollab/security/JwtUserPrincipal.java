package com.DevCollab.security;

public class JwtUserPrincipal {

    private Long userId;
    private String email;

    public JwtUserPrincipal(
            Long userId,
            String email) {

        this.userId = userId;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return email;
    }
}