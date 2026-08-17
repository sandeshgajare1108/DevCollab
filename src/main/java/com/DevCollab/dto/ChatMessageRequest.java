
package com.DevCollab.dto;

public class ChatMessageRequest {

    private Long projectId;

    private String message;


    public ChatMessageRequest() {
    }


    public Long getProjectId() {
        return projectId;
    }


    public void setProjectId(
            Long projectId) {

        this.projectId =
                projectId;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(
            String message) {

        this.message =
                message;
    }
}
