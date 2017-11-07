package com.auxolabs.testAutomationTool.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PutPostTestDetailsRequestModel {
    @JsonProperty
    private String testName;
    @JsonProperty
    private String httpMethod;
    @JsonProperty
    private String url;
    @JsonProperty
    private Map<String,String> requestParameters;
    @JsonProperty
    private Map<String,String> responseParameters;
    @JsonProperty
    private String contentType;

    public PutPostTestDetailsRequestModel(){}

    public String getTestName() {
        return testName;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getRequestParameters() {
        return requestParameters;
    }

    public Map<String, String> getResponseParameters() {
        return responseParameters;
    }

    public String getContentType() {
        return contentType;
    }
}
