package com.auxolabs.testAutomationTool.models;

import java.util.ArrayList;

public class UploadedDocumentsDetails {
    private long testId;
    private ArrayList<String> documentNames;

    public UploadedDocumentsDetails() {
        testId = 0;
        documentNames = new ArrayList<String>();
    }

    public UploadedDocumentsDetails(long testId, ArrayList<String> documentNames) {
        this.testId = testId;
        this.documentNames = documentNames;
    }

    public long getTestId() {
        return testId;
    }

    public ArrayList<String> getDocumentNames() {
        return documentNames;
    }

}
