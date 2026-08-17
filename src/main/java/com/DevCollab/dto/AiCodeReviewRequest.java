package com.DevCollab.dto;

public class AiCodeReviewRequest {

    private Long pullRequestId;

    private Long taskId;

    private String code;


    public AiCodeReviewRequest() {
    }


    public Long getPullRequestId() {
        return pullRequestId;
    }

    public void setPullRequestId(Long pullRequestId) {
        this.pullRequestId = pullRequestId;
    }


    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}