package com.auxolabs.testAutomationTool.models;

import java.util.ArrayList;

public class UploadedDocumentsDetails {
    private String _id;
    private String testId;
    private ArrayList<String> documentNames;

    public UploadedDocumentsDetails() {
        documentNames = new ArrayList<String>();
    }

    public UploadedDocumentsDetails(String _id, String testId, ArrayList<String> documentNames) {
        this._id = _id;

        this.testId = testId;
        this.documentNames = documentNames;
    }

    public String get_id() {
        return _id;
    }

    public String getTestId() {
        return testId;
    }

    public ArrayList<String> getDocumentNames() {
        return documentNames;
    }

}
