package com.example.impostergate.model;

public class MockData {

    private String requestName;
    private String responseName;
    private String additionalName;
    private String[] responseBody;

    public MockData() {
    }

    public MockData(String requestName, String responseName, String additionalName, String[] responseBody) {
        this.requestName = requestName;
        this.responseName = responseName;
        this.additionalName = additionalName;
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        return "Request [requestName=" + requestName + ", responseName=" + responseName + ", responseBody=" + responseBody + "]";
    }

    public String getRequestName() {return requestName;}

    public String getResponseName() {return responseName;}

    public String getAdditionalName() {return additionalName;}

    public String[] getResponseBody() {return responseBody;}
}
