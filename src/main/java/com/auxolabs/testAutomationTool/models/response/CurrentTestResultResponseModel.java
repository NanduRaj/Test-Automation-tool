package com.auxolabs.testAutomationTool.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrentTestResultResponseModel extends BaseResponse{
    @JsonProperty
    String testId;
    @JsonProperty
    long success;
    @JsonProperty
    long failure;
    @JsonProperty
    float successPercentage;
    @JsonProperty
    long date;

    public CurrentTestResultResponseModel(){}
    public CurrentTestResultResponseModel(String status,String message,String testId,long success,long failure,float successPercentage,long date){
        super(status, message);
        this.testId = testId;
        this.success = success;
        this.failure = failure;
        this.successPercentage = successPercentage;
        this.date = date;
    }

    public String getTestId() {
        return testId;
    }

    public long getSuccess() {
        return success;
    }

    public long getFailure() {
        return failure;
    }

    public float getSuccessPercentage() {
        return successPercentage;
    }

    public long getDate() {
        return date;
    }
}
