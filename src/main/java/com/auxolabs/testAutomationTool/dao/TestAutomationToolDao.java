package com.auxolabs.testAutomationTool.dao;

import com.auxolabs.testAutomationTool.configuration.TestAutomationToolConfiguration;
import com.auxolabs.testAutomationTool.models.AllTestDetails;
import com.auxolabs.testAutomationTool.models.TestDetails;
import com.auxolabs.testAutomationTool.models.TestResult;
import com.auxolabs.testAutomationTool.models.UploadedDocumentsDetails;
import com.auxolabs.testAutomationTool.models.request.AddTestResultRequestModel;
import com.auxolabs.testAutomationTool.models.request.PutPostTestDetailsRequestModel;
import com.auxolabs.testAutomationTool.models.response.CurrentTestResultResponseModel;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TestAutomationToolDao {
    private static final Logger logger = Logger.getLogger(TestAutomationToolDao.class.getName());
    private DaoHelper helper;
    public TestAutomationToolDao(TestAutomationToolConfiguration configuration){
        this.helper = new DaoHelper(configuration);
        logger.addHandler(new ConsoleHandler());
        logger.setLevel(Level.FINE);
    }

    public TestDetails addTestDetails(PutPostTestDetailsRequestModel putPostTestDetailsRequestModel){
        logger.log(Level.FINE,"Method addTestDetails of TestAutomationToolDao is called\n");
        return helper.addTest(putPostTestDetailsRequestModel);
    }

    public TestDetails getTestDetails(ObjectId id){
        logger.log(Level.FINE,"Method getTestDetails of TestAutomationToolDao is called\n");
        return helper.getDetails(id);
    }

    public List<TestResult> getTestResults(String id){
        return helper.getResults(id);
    }

    public long deleteTestDetails(ObjectId id){
        return helper.deleteDetails(id);
    }

    public TestDetails updateTestDetails(ObjectId id,PutPostTestDetailsRequestModel putPostTestDetailsRequestModel){
        logger.log(Level.FINE,"Method updateTestDetails of TestAutomationToolDao is called\n");
        return helper.updateTestDetails(id,putPostTestDetailsRequestModel);
    }

    public List<AllTestDetails> getAllTestDetails(){
        return helper.getAllTestDetails();
    }

    public UploadedDocumentsDetails addUploadedDocumentsDetail(String id, String locationOfFile){
        return helper.addUploadedDocuments(id,locationOfFile);
    }

    public UploadedDocumentsDetails getUploadedDocumentDetails(String id){
        return helper.getUploadedDocumentDetails(id);
    }

    public CurrentTestResultResponseModel test(TestDetails testDetails, UploadedDocumentsDetails uploadedDocumentsDetails) throws ParseException, UnirestException, IOException {
        return helper.test(testDetails, uploadedDocumentsDetails);
    }

    public void addTestResults(AddTestResultRequestModel addTestResultRequestModel){
        helper.addTestResults(addTestResultRequestModel);
    }
}
