package com.auxolabs.testAutomationTool.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;

import java.io.Serializable;

public class TestResult implements Serializable {
    @JsonProperty
    @net.vz.mongodb.jackson.ObjectId
    private String _id;
    @JsonProperty
    @net.vz.mongodb.jackson.ObjectId
    private String testId;
    @JsonProperty
    private long success;
    @JsonProperty
    private long failure;
    @JsonProperty
    private long date;

    public TestResult(){}

    public TestResult(String _id, String testId, Long success, Long failure, Long date) {
        this._id = _id;
        this.testId = testId;
        this.success = success;
        this.failure = failure;
        this.date = date;
    }

    public String get_id() {
        return _id;
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
