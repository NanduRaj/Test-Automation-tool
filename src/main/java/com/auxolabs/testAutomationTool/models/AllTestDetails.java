package com.auxolabs.testAutomationTool.models;

public class AllTestDetails {
    @net.vz.mongodb.jackson.ObjectId
    private String id;
    private String testName;
    private long lastTestedDate;
    private float lastTestedSuccessPercentage;
    public AllTestDetails(){}
    public AllTestDetails(String id, String name, long date, float success){
        this.id = id;
        this.testName = name;
        this.lastTestedDate = date;
        this.lastTestedSuccessPercentage = success;
    }

    public String getId() {
        return id;
    }

    public String getTestName() {
        return testName;
    }

    public long getLastTestedDate() {
        return lastTestedDate;
    }

    public float getLastTestedSuccess() {
        return lastTestedSuccessPercentage;
    }
}
