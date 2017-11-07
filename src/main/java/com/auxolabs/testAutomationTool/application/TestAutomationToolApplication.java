package com.auxolabs.testAutomationTool.application;

import com.auxolabs.testAutomationTool.configuration.TestAutomationToolConfiguration;
import com.auxolabs.testAutomationTool.resource.TestAutomationToolResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class TestAutomationToolApplication extends Application<TestAutomationToolConfiguration>{
    @Override
    public void initialize(Bootstrap<TestAutomationToolConfiguration> bootstrap){
        bootstrap.addBundle(new SwaggerBundle<TestAutomationToolConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(TestAutomationToolConfiguration sampleConfiguration) {
                return sampleConfiguration.swaggerBundleConfiguration;
            }
        });
            }

    @Override
    public void run(TestAutomationToolConfiguration configuration, Environment environment){
        environment.jersey().register(new TestAutomationToolResource(configuration));
        environment.jersey().register(MultiPartFeature.class);
    }

    public static void main(String[] args) throws Exception {
        new TestAutomationToolApplication().run(args);
    }
}
