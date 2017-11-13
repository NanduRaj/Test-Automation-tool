package com.auxolabs.testAutomationTool.models.response;

import com.auxolabs.testAutomationTool.models.TestResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AllTestResultsResponseModel extends BaseResponse {
    @JsonProperty
    private List<TestResult> testResult;
    AllTestResultsResponseModel(){}
    public AllTestResultsResponseModel(String status, String message, List<TestResult> testResult){
        super(status,message);
        this.testResult = testResult;
    }
}
