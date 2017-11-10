package com.auxolabs.testAutomationTool.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TestDetails implements Serializable{
    @JsonProperty
    private String _id;
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
    @JsonProperty
    private long date;

    public TestDetails(){
        requestParameters = new HashMap<String, String>();
        responseParameters = new HashMap<String, String>();
    }

    public TestDetails(String testId, String testName, String httpMethod, String url, Map<String,String> requestParameters,
                       Map<String,String> responseParameters, String contentType, long date){
        this._id = testId;
        this.testName = testName;
        this.httpMethod = httpMethod;
        this.url = url;
        this.requestParameters = requestParameters;
        this.responseParameters = responseParameters;
        this.contentType = contentType;
        this.date = date;
    }

    public String get_id() {
        return _id;
    }

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

    public long getDate() {
        return date;
    }
}
