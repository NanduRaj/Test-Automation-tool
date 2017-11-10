package com.auxolabs.testAutomationTool.models.response;

import com.auxolabs.testAutomationTool.models.UploadedDocumentsDetails;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadedDocumentsModel extends BaseResponse {

    @JsonProperty
    private UploadedDocumentsDetails documentsDetails;

    public UploadedDocumentsModel(){}

    public UploadedDocumentsModel(String status,String message, UploadedDocumentsDetails documentsDetails){
        super(status, message);
        this.documentsDetails = documentsDetails;
    }
}
