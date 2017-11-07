package com.auxolabs.testAutomationTool.models.response;

import com.auxolabs.testAutomationTool.models.TestDetails;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestDetailsResponse extends BaseResponse {
    @JsonProperty
    private TestDetails testDetails;
    TestDetailsResponse(){}
    public TestDetailsResponse(String status,String message,TestDetails testDetails){
        super(status,message);
        this.testDetails = testDetails;
    }
}
