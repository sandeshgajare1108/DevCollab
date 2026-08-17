
package com.DevCollab.dto;

public class ProfileUpdateRequest {

    private String fullName;
    private String mobile;


    public ProfileUpdateRequest() {
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
