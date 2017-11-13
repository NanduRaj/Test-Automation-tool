package com.auxolabs.testAutomationTool.models.request;

public class AddTestResultRequestModel {
    private String testId;
    private long success;
    private long failure;
    private long date;

    public AddTestResultRequestModel() {}

    public AddTestResultRequestModel(String testId, long success, long failure, long date) {
        this.testId = testId;
        this.success = success;
        this.failure = failure;
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

    public long getDate() {
        return date;
    }
}
