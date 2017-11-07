package com.auxolabs.testAutomationTool.models.response;

import com.auxolabs.testAutomationTool.models.AllTestDetails;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HomeScreenResponse extends BaseResponse {
    @JsonProperty
    private List<AllTestDetails> allTestDetails;
    public HomeScreenResponse(){}
    public HomeScreenResponse(String status, String message, List<AllTestDetails> homeScreen){
        super(status, message);
        this.allTestDetails = homeScreen;
    }

}
