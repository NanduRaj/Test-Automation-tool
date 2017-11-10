package com.auxolabs.testAutomationTool.models.response;

import com.auxolabs.testAutomationTool.models.TestDetails;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestDetailsResponseModel extends BaseResponse {
    @JsonProperty
    private TestDetails testDetails;
    TestDetailsResponseModel(){}
    public TestDetailsResponseModel(String status, String message, TestDetails testDetails){
        super(status,message);
        this.testDetails = testDetails;
    }
}
