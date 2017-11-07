package com.auxolabs.testAutomationTool.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResponse {
    @JsonProperty
    private String status;
    @JsonProperty
    private String message;
    public BaseResponse(){}
    public BaseResponse(String status,String message){
        this.status = status;
        this.message = message;
    }

    public String isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
