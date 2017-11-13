package com.auxolabs.testAutomationTool.dao;

import com.auxolabs.testAutomationTool.MongoConnector;
import com.auxolabs.testAutomationTool.client.Client;
import com.auxolabs.testAutomationTool.configuration.TestAutomationToolConfiguration;
import com.auxolabs.testAutomationTool.excelReader.ExcelSheetReader;
import com.auxolabs.testAutomationTool.models.AllTestDetails;
import com.auxolabs.testAutomationTool.models.UploadedDocumentsDetails;
import com.auxolabs.testAutomationTool.models.request.AddTestResultRequestModel;
import com.auxolabs.testAutomationTool.models.request.PutPostTestDetailsRequestModel;
import com.auxolabs.testAutomationTool.models.TestDetails;
import com.auxolabs.testAutomationTool.models.TestResult;
import com.auxolabs.testAutomationTool.models.response.CurrentTestResultResponseModel;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.bson.types.ObjectId;
import org.bson.Document;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


class DaoHelper {
    private MongoConnector mongoConnector;
    private String collectionName1;
    private String collectionName2;
    private String collectionName3;
    private static final Logger logger = Logger.getLogger(DaoHelper.class.getName());

    DaoHelper(TestAutomationToolConfiguration configuration) {
        this.mongoConnector = new MongoConnector(configuration);
        collectionName1 = "testDetails";
        collectionName2 = "testResults";
        collectionName3 = "uploadedDocuments";
        logger.addHandler(new ConsoleHandler());
        logger.setLevel(Level.FINE);
    }

    /**
     * function to add a new test
     * @param putPostTestDetailsRequestModel, the model class used for the request
     * @return TestDetails, the class model having the details of test
     */
    TestDetails addTest(PutPostTestDetailsRequestModel putPostTestDetailsRequestModel) {

        logger.log(Level.FINE, "Method addTest of DaoHelper is called");
        Document toInsert = new Document("testName", putPostTestDetailsRequestModel.getTestName()).append("httpMethod", putPostTestDetailsRequestModel.getHttpMethod()).append("url", putPostTestDetailsRequestModel.getUrl()).append("responseParameters", putPostTestDetailsRequestModel.getResponseParameters()).append("requestParameters", putPostTestDetailsRequestModel.getRequestParameters()).append("contentType", putPostTestDetailsRequestModel.getContentType()).append("date", System.currentTimeMillis());
        try {
            mongoConnector.insert(collectionName1, toInsert);
            return new TestDetails(toInsert.getObjectId("_id").toString(), putPostTestDetailsRequestModel.getTestName(), putPostTestDetailsRequestModel.getHttpMethod(), putPostTestDetailsRequestModel.getUrl(), putPostTestDetailsRequestModel.getRequestParameters(), putPostTestDetailsRequestModel.getResponseParameters(), putPostTestDetailsRequestModel.getContentType(), toInsert.getLong("date"));
        } catch (Exception e) {
            logger.severe("Error at adding document");
        }
        return null;
    }

    /**
     * function to get details of a given test
     * @param id, the testId
     * @return TestDetails, the class model having the details of test
     */
    TestDetails getDetails(ObjectId id) {
        Document query = new Document("_id", id);
        Document details = mongoConnector.getDocument(collectionName1, query);
        if (details == null) return null;
        logger.info("Test name is : " + details.getString("testName"));
        return new TestDetails(details.getObjectId("_id").toString(), details.getString("testName"), details.getString("httpMethod"), details.getString("url"), (Map<String, String>) (details.get("requestParameters")), (Map<String, String>) (details.get("responseParameters")), details.getString("contentType"), details.getLong("date"));
    }

    /**
     * function to fetch the results of a test
     * @param id the testId
     * @return list of testResult class model
     */
    List<TestResult> getResults(String id) {
        List<Document> allDocuments = mongoConnector.getAllDocuments(collectionName2);
        List<TestResult> results = new ArrayList<TestResult>();
        for (Document document : allDocuments) {
            if (id.equals(document.getString("testId"))) {
                TestResult testResult = new TestResult(document.getObjectId("_id").toString(), document.getString("testId"), document.getLong("success"), document.getLong("failure"), document.getLong("date"));
                results.add(testResult);
            }
        }
        if (results.isEmpty()) return null;
        return results;
    }

    /**
     * function to update a test
     * @param id, id of the test
     * @param putPostTestDetailsRequestModel, the model class used for the request
     * @return TestDetails, the class model having the details of test
     */
    TestDetails updateTestDetails(ObjectId id, PutPostTestDetailsRequestModel putPostTestDetailsRequestModel) {
        TestDetails toUpdateTestDetails = getDetails(id);
        if (toUpdateTestDetails == null) return null;
        logger.info("the id obtained is :" + toUpdateTestDetails.get_id());
        Document query = new Document("_id", id);
        Document updated = new Document();
        updated.put("$set", new Document("testName", putPostTestDetailsRequestModel.getTestName()).append("httpMethod", putPostTestDetailsRequestModel.getHttpMethod()).append("url", putPostTestDetailsRequestModel.getUrl()).append("requestParameters", putPostTestDetailsRequestModel.getRequestParameters()).append("responseParameters", putPostTestDetailsRequestModel.getResponseParameters()).append("contentType", putPostTestDetailsRequestModel.getContentType()).append("date", toUpdateTestDetails.getDate()));
        try {
            mongoConnector.update(collectionName1, query, updated);
            return new TestDetails(id.toString(), putPostTestDetailsRequestModel.getTestName(), putPostTestDetailsRequestModel.getHttpMethod(), putPostTestDetailsRequestModel.getUrl(), putPostTestDetailsRequestModel.getRequestParameters(), putPostTestDetailsRequestModel.getResponseParameters(), putPostTestDetailsRequestModel.getContentType(), toUpdateTestDetails.getDate());
        } catch (Exception e) {
            logger.severe("Couldnt update");
            return null;
        }
    }

    /**
     * function to delete test Details
     * @param id, id of test to be deleted
     * @return number of documents deleted
     */
    long deleteDetails(ObjectId id) {
        TestDetails toDeleteTestDetails = getDetails(id);
        if (toDeleteTestDetails == null) return 0;
        Document query = new Document("_id", toDeleteTestDetails.get_id());
        return mongoConnector.delete(collectionName1, query);
    }

    /**
     * function to get all test Details
     * @return list of model class AllTestDetails
     */
    List<AllTestDetails> getAllTestDetails() {
        List<Document> allDocumentDetails = mongoConnector.getAllDocuments(collectionName1);
        List<Document> allDocumentResults = mongoConnector.getAllDocuments(collectionName2);
        List<AllTestDetails> allTestDetails = new ArrayList<AllTestDetails>();
        for (Document documentOfDetails : allDocumentDetails) {
            String id = documentOfDetails.getObjectId("_id").toString();
            String testName = documentOfDetails.getString("testName");
            long date = findLatestDate(id, allDocumentResults);
            float successPercentage = findlatestSuccessPercentage(id, date, allDocumentResults);
            allTestDetails.add(new AllTestDetails(id, testName, date, successPercentage));
        }
        return allTestDetails;
    }

    /**
     * helper function to get the last date the test being done
     * @param id, the testId
     * @param allDocumentResults, list of all documents in the testResult collection
     * @return date on which the test was last conducted
     */
    private long findLatestDate(String id, List<Document> allDocumentResults) {
        long largest = 0;
        for (Document documentOfResults : allDocumentResults) {
            if (documentOfResults.getString("testId").equals(id)) {
                long currentDocumentDate = documentOfResults.getLong("date");
                if (largest < currentDocumentDate) {
                    largest = currentDocumentDate;
                }
            }
        }
        return largest;
    }

    /**
     * helper function to get the latest success percentage
     * @param id, the testId
     * @param date, the date returned from function findLatestDate
     * @param allDocumentResults, success percentage
     * @return
     */
    private float findlatestSuccessPercentage(String id, long date, List<Document> allDocumentResults) {
        for (Document documentOfResults : allDocumentResults) {
            String currentId = documentOfResults.getString("testId");
            long currentDate = documentOfResults.getLong("date");
            if (id.equals(currentId) && date == currentDate) {
                long success = documentOfResults.getLong("success");
                long failure = documentOfResults.getLong("failure");
                return ((float) success / (success + failure)) * 100;
            }
        }
        return 0;
    }

    /**
     * function to add documents uploaded
     * @param id testId
     * @param locationOfFile path where document is stored
     * @return UploadDocumentDetails , a class model
     */

    UploadedDocumentsDetails addUploadedDocuments(String id, String locationOfFile) {
        ArrayList<String> documents = new ArrayList<String>();
        documents.add(locationOfFile);
        List<Document> allDocumentDetails = mongoConnector.getAllDocuments(collectionName3);
        Document toInsert = new Document("testId", id).append("documentNames", documents);
        try {
            mongoConnector.insert(collectionName3, toInsert);
            return new UploadedDocumentsDetails(toInsert.getObjectId("_id").toString(), toInsert.getString("testId"), (ArrayList<String>) toInsert.get("documentNames"));
        } catch (Exception e) {
            logger.severe("Error at adding document");
        }
        return null;
    }

    /**
     * function to get Uploaded document details from collection uploadedDocuments
     * @param id testId
     * @return UploadedDocumentsDetails class model
     */
    UploadedDocumentsDetails getUploadedDocumentDetails(String id) {
        Document query = new Document("testId", id);
        Document details = mongoConnector.getDocument(collectionName3, query);
        if (details == null) return null;
        return (new UploadedDocumentsDetails(details.getObjectId("_id").toString(), details.getString("testId"), (ArrayList<String>) details.get("documentNames")));
    }

    /**
     * function to test
     * @param testDetails the details of the test from the collection testDetails
     * @param documentsDetails, the details of the uploaded documents from collection uploaded document
     * @return CurrentTestResultResponseModel, a model class
     * @throws ParseException
     * @throws UnirestException
     * @throws IOException
     */
    CurrentTestResultResponseModel test(TestDetails testDetails, UploadedDocumentsDetails documentsDetails) throws ParseException, UnirestException, IOException {

        long success = 0, failure = 0;

        ArrayList<String> documentNames = documentsDetails.getDocumentNames();

        ExcelSheetReader excelSheetReader = new ExcelSheetReader();
        HashMap<String, ArrayList<Object>> excelData = excelSheetReader.readFromExcelFile(documentNames.get(0)); // need to be modified, now doesnt look the condition for more than one documents uploaded

        Map<String, Object> requestparamValue = new HashMap<String, Object>();
        Object requestParameterAsInUploadedDocument = testDetails.getRequestParameters().values().toArray()[0]; // checked only assuming there is only one request parameter(mostly id)
        ArrayList<Object> excelMappingValues = excelData.get(requestParameterAsInUploadedDocument);
        for (Object id : excelMappingValues) {
            int flag = 1;
            String idToTest = id.toString();
            Client client = new Client();
            for (String key : testDetails.getRequestParameters().keySet()) {
                requestparamValue.put(key, idToTest);
            }

            HashMap<String, Object> testUrlResponse = client.getResponse(testDetails.getUrl(), requestparamValue);
            for (String responseParameter : testDetails.getResponseParameters().keySet()) {
                Object value = testUrlResponse.get(responseParameter);
                int index = (excelData.get(requestParameterAsInUploadedDocument).indexOf(idToTest));
                if (!(testUrlResponse.get(responseParameter) == excelData.get(testDetails.getResponseParameters().get(responseParameter)).get(index) ||    // to compare variable types other than string
                        testUrlResponse.get(responseParameter).toString().equals(excelData.get(testDetails.getResponseParameters().get(responseParameter)).get(index).toString()))) { //to compare string types
                    flag = 0;
                    break;
                }
            }
            if (flag == 1) success++;
            else {
                failure++;
                System.out.println("id failed : "+ idToTest);
            }
        }
        float successPercentage = (float) success/(success+failure) * 100;
        return (new CurrentTestResultResponseModel("Success","Tested succesfully",testDetails.get_id(),success,failure,successPercentage,new Date().getTime()));
    }

    /**
     * function to add test Results to the collection testResult
     * @param addTestResultRequestModel, model class
     */
    void addTestResults(AddTestResultRequestModel addTestResultRequestModel){
        Document toInsert = new Document("testId",addTestResultRequestModel.getTestId()).append("success",addTestResultRequestModel.getSuccess())
                                    .append("failure",addTestResultRequestModel.getFailure()).append("date",addTestResultRequestModel.getDate());
        try {
            mongoConnector.insert(collectionName2,toInsert);
        }catch (Exception e){
            e.printStackTrace();
            logger.severe("Test result document wasnt able to be added to database");
        }
    }
}
