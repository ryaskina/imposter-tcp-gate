package com.example.impostergate.model;

public class ImposterGateResponse {

    private final StatusType status;
    private final String content;

    public ImposterGateResponse(StatusType status, String content) {
        this.status = status;
        this.content = content;
    }

    public StatusType getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }

    public enum StatusType {
        SUCCESS, ERROR
    }

}
