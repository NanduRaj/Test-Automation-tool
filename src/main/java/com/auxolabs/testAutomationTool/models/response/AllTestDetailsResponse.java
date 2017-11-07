package com.auxolabs.testAutomationTool.models.response;

import com.auxolabs.testAutomationTool.models.AllTestDetails;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class AllTestDetailsResponse extends BaseResponse {
    @JsonProperty
    private List<AllTestDetails> allTestDetailsList;
    public AllTestDetailsResponse(){
        allTestDetailsList = new ArrayList<AllTestDetails>();
    }
    public AllTestDetailsResponse(String status, String message, List<AllTestDetails> allTestDetailsList){
        super(status, message);
        this.allTestDetailsList = allTestDetailsList;
    }
}
