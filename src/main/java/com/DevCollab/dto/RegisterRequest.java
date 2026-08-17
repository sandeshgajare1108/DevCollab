
package com.DevCollab.dto;

public class RegisterRequest {

    private String fullName;

    private String email;

    private String mobile;

    private String password;


    // =====================================================
    // DEFAULT CONSTRUCTOR
    // =====================================================

    public RegisterRequest() {
    }


    // =====================================================
    // GETTERS
    // =====================================================

    public String getFullName() {
        return fullName;
    }


    public String getEmail() {
        return email;
    }


    public String getMobile() {
        return mobile;
    }


    public String getPassword() {
        return password;
    }


    // =====================================================
    // SETTERS
    // =====================================================

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public void setPassword(String password) {
        this.password = password;
    }
}
