package com.auxolabs.testAutomationTool.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class TestAutomationToolConfiguration extends Configuration {

    @JsonProperty
    private String mongoHost;
    @JsonProperty
    private int mongoPort;
    @JsonProperty
    private String mongoDatabase;
    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    public String getMongoHost() {
        return mongoHost;
    }

    public int getMongoPort() {
        return mongoPort;
    }

    public String getMongoDatabase() {
        return mongoDatabase;
    }
}
