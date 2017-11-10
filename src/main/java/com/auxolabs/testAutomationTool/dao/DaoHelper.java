package com.auxolabs.testAutomationTool.dao;

import com.auxolabs.testAutomationTool.MongoConnector;
import com.auxolabs.testAutomationTool.configuration.TestAutomationToolConfiguration;
import com.auxolabs.testAutomationTool.models.AllTestDetails;
import com.auxolabs.testAutomationTool.models.UploadedDocumentsDetails;
import com.auxolabs.testAutomationTool.models.request.PutPostTestDetailsRequestModel;
import com.auxolabs.testAutomationTool.models.TestDetails;
import com.auxolabs.testAutomationTool.models.TestResult;
import org.bson.types.ObjectId;
import org.bson.Document;

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
    DaoHelper(TestAutomationToolConfiguration configuration){
        this.mongoConnector = new MongoConnector(configuration);
        collectionName1 = "testDetails";
        collectionName2 = "testResults";
        collectionName3 = "uploadedDocuments";
        logger.addHandler(new ConsoleHandler());
        logger.setLevel(Level.FINE);
    }

    TestDetails addTest(PutPostTestDetailsRequestModel putPostTestDetailsRequestModel){

        logger.log(Level.FINE,"Method addTest of DaoHelper is called");
        Document toInsert = new Document("testName", putPostTestDetailsRequestModel.getTestName())
                                            .append("httpMethod", putPostTestDetailsRequestModel.getHttpMethod()).append("url", putPostTestDetailsRequestModel.getUrl())
                                            .append("responseParameters", putPostTestDetailsRequestModel.getResponseParameters()).append("requestParameters", putPostTestDetailsRequestModel.getRequestParameters())
                                            .append("contentType", putPostTestDetailsRequestModel.getContentType()).append("date", System.currentTimeMillis());
        try {
            mongoConnector.insert(collectionName1, toInsert);
            return new TestDetails(toInsert.getObjectId("_id").toString(), putPostTestDetailsRequestModel.getTestName(), putPostTestDetailsRequestModel.getHttpMethod(), putPostTestDetailsRequestModel.getUrl(), putPostTestDetailsRequestModel.getRequestParameters(), putPostTestDetailsRequestModel.getResponseParameters(), putPostTestDetailsRequestModel.getContentType(),toInsert.getLong("date"));
        }catch (Exception e){
            logger.severe("Error at adding document");
        }
        return null;
    }

    TestDetails getDetails(ObjectId id){
        Document query = new Document("_id",id);
        Document details = mongoConnector.getDocument(collectionName1,query);
        if (details == null)
            return null;
        logger.info("Test name is : "+ details.getString("testName"));
        return new TestDetails(details.getObjectId("_id").toString(),details.getString("testName"),details.getString("httpMethod"),
                                                    details.getString("url"), (Map<String,String>) (details.get("requestParameters")),
                                                    (Map<String,String>) (details.get("responseParameters")),
                                                    details.getString("contentType"),details.getLong("date"));
    }

    List<TestResult> getResults(String id){
        List<Document> allDocuments = mongoConnector.getAllDocuments(collectionName2);
        List<TestResult> results = new ArrayList<TestResult>();
        for (Document document : allDocuments){
            if (id.equals(document.getString("testId"))){
                TestResult testResult = new TestResult(document.getObjectId("_id").toString(),document.getString("testId"),document.getDouble("success"),document.getDouble("failure"),document.getDouble("date"));
                results.add(testResult);
            }
        }
        if (results.isEmpty())
            return null;
        return results;
    }

    TestDetails updateTestDetails(ObjectId id,PutPostTestDetailsRequestModel putPostTestDetailsRequestModel){
        TestDetails toUpdateTestDetails = getDetails(id);
        if (toUpdateTestDetails == null)
            return null;
        logger.info("the id obtained is :"+ toUpdateTestDetails.get_id());
        Document query = new Document("_id",id);
        Document updated = new Document();
        updated.put("$set",new Document("testName",putPostTestDetailsRequestModel.getTestName()).append("httpMethod",putPostTestDetailsRequestModel.getHttpMethod())
                .append("url",putPostTestDetailsRequestModel.getUrl()).append("requestParameters",putPostTestDetailsRequestModel.getRequestParameters()).append("responseParameters",putPostTestDetailsRequestModel.getResponseParameters())
                .append("contentType",putPostTestDetailsRequestModel.getContentType()).append("date",toUpdateTestDetails.getDate()));
        try {
            mongoConnector.update(collectionName1,query,updated);
            return new TestDetails(id.toString(),putPostTestDetailsRequestModel.getTestName(), putPostTestDetailsRequestModel.getHttpMethod(), putPostTestDetailsRequestModel.getUrl(), putPostTestDetailsRequestModel.getRequestParameters(), putPostTestDetailsRequestModel.getResponseParameters(), putPostTestDetailsRequestModel.getContentType(),toUpdateTestDetails.getDate());
        }catch (Exception e){
            logger.severe("Couldnt update");
            return null;
        }
    }

    long deleteDetails(ObjectId id){
        TestDetails toDeleteTestDetails = getDetails(id);
        if (toDeleteTestDetails == null)
            return 0;
        Document query = new Document("_id",toDeleteTestDetails.get_id());
        return mongoConnector.delete(collectionName1,query);
    }

    List<AllTestDetails> getAllTestDetails(){
        List<Document> allDocumentDetails = mongoConnector.getAllDocuments(collectionName1);
        List<Document> allDocumentResults = mongoConnector.getAllDocuments(collectionName2);
        List<AllTestDetails> allTestDetails = new ArrayList<AllTestDetails>();
        for (Document documentOfDetails : allDocumentDetails){
            String id = documentOfDetails.getObjectId("_id").toString();
            String testName = documentOfDetails.getString("testName");
            long date = findlatestDate(id,allDocumentResults);
            float successPercentage = findlatestSuccessPercentage(id,date,allDocumentResults);
            allTestDetails.add(new AllTestDetails(id,testName,date,successPercentage));
        }
        return allTestDetails;
    }

    private long findlatestDate(String id, List<Document> allDocumentResults){
        long largest = 0;
        for (Document documentOfResults : allDocumentResults){
            if (documentOfResults.getString("testId").equals(id)) {
                long currentDocumentDate = documentOfResults.getDouble("date").longValue();
                if (largest < currentDocumentDate) {
                    largest = currentDocumentDate;
                }
            }
        }
        return largest;
    }

    private float findlatestSuccessPercentage(String id, long date, List<Document> allDocumentResults){
        for (Document documentOfResults : allDocumentResults){
            String currentId = documentOfResults.getString("testId");
            long currentDate = documentOfResults.getDouble("date").longValue();
            if (id.equals(currentId) && date == currentDate){
                long success = documentOfResults.getDouble("success").longValue();
                long failure = documentOfResults.getDouble("failure").longValue();
                return ((float) success / (success + failure));
            }
        }
        return -1;
    }

    UploadedDocumentsDetails addUploadedDocuments(String id, String locationOfFile){
        ArrayList<String> documents = new ArrayList<String>();
        documents.add(locationOfFile);
        List<Document> allDocumentDetails = mongoConnector.getAllDocuments(collectionName3);
        Document toInsert = new Document("testId",id).append("documentNames",documents);
        try {
            mongoConnector.insert(collectionName3, toInsert);
            return new UploadedDocumentsDetails(toInsert.getObjectId("_id").toString(), toInsert.getString("testId"),(ArrayList<String>) toInsert.get("documentNames"));
        }catch (Exception e){
            logger.severe("Error at adding document");
        }
        return null;
    }
}
