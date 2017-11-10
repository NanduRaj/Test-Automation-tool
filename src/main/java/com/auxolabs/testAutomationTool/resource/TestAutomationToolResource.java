package com.auxolabs.testAutomationTool.resource;

import com.auxolabs.testAutomationTool.configuration.TestAutomationToolConfiguration;
import com.auxolabs.testAutomationTool.dao.TestAutomationToolDao;
import com.auxolabs.testAutomationTool.excelReader.ExcelSheetReader;
import com.auxolabs.testAutomationTool.models.AllTestDetails;
import com.auxolabs.testAutomationTool.models.TestDetails;
import com.auxolabs.testAutomationTool.models.UploadedDocumentsDetails;
import com.auxolabs.testAutomationTool.models.response.*;
import com.auxolabs.testAutomationTool.models.TestResult;
import com.auxolabs.testAutomationTool.models.request.PutPostTestDetailsRequestModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;

@Path("/testAutomationTool")
@Api(value = "TestAutomationTool")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestAutomationToolResource {

    private TestAutomationToolDao dao;
    public TestAutomationToolResource(TestAutomationToolConfiguration configuration){
        this.dao = new TestAutomationToolDao(configuration);
    }

    @PUT
    @ApiOperation(value = "create new test",response = TestDetailsResponseModel.class)
    public Response createTest(PutPostTestDetailsRequestModel putPostTestDetailsRequestModel){
        try {
            TestDetails testDetails = dao.addTestDetails(putPostTestDetailsRequestModel);
            if (testDetails != null) {
                TestDetailsResponseModel testDetailsResponseModel = new TestDetailsResponseModel("Success","The record is created",testDetails);
                return Response.ok(testDetailsResponseModel).build();
            }
            else {
                TestDetailsResponseModel testDetailsResponseModel = new TestDetailsResponseModel("Failure","The record was not created",null);
                return Response.ok(testDetailsResponseModel).build();
            }
        }catch (Exception e){
            return Response.serverError().entity(new TestDetailsResponseModel("Failure", "Api error", null)).build();
        }
    }

    @ApiOperation(value = "get test details",response = TestDetailsResponseModel.class)
    @GET
    @Path("/details")
    public Response getTestDetails(@QueryParam("id") ObjectId testId){
        try {
            TestDetails testDetails = dao.getTestDetails(testId);
            if (testDetails != null) {
                TestDetailsResponseModel testDetailsResponseModel = new TestDetailsResponseModel("Success", "The student has been fetched with id "+ testId, testDetails);
                return Response.ok(testDetailsResponseModel).build();
            } else {
                TestDetailsResponseModel testDetailsResponseModel = new TestDetailsResponseModel("Success", "No such record found", null);
                return Response.ok(testDetailsResponseModel).build();
            }
        }catch (Exception e) {
            return Response.serverError().entity(new TestDetailsResponseModel("Failure", "Api error", null)).build();
        }
    }

    @ApiOperation(value = "update a test",response = TestDetailsResponseModel.class)
    @POST
    @Path("/{id}")
    public Response updateTestDetails(@PathParam("id") ObjectId testId, PutPostTestDetailsRequestModel putPostTestDetailsRequestModel) {
        try {
            TestDetails testDetails = dao.updateTestDetails(testId, putPostTestDetailsRequestModel);
            if (testDetails != null) {
                TestDetailsResponseModel testDetailsResponseModel = new TestDetailsResponseModel("Success", "The record is updated", testDetails);
                return Response.ok(testDetailsResponseModel).build();
            } else {
                TestDetailsResponseModel testDetailsResponseModel = new TestDetailsResponseModel("Failure", "No such record found", null);
                return Response.ok(testDetailsResponseModel).build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(new TestDetailsResponseModel("Failure", "Api error", null)).build();
        }
    }

    @ApiOperation(value = "delete a test")
    @DELETE
    @Path("/{id}")
    public Response deleteTest(@PathParam("id") ObjectId testId){
        try {
            long isDeleted = dao.deleteTestDetails(testId);
            if (isDeleted > 0) {
                TestDetailsResponseModel testDetailsResponseModel = new TestDetailsResponseModel("Success", "The record is deleted", null);
                return Response.ok(testDetailsResponseModel).build();
            }
            else {
                TestDetailsResponseModel testDetailsResponseModel = new TestDetailsResponseModel("Failure", "The record not found", null);
                return Response.ok(testDetailsResponseModel).entity("Document not found").build();
            }
        }catch (Exception e){
            return Response.serverError().entity(new TestDetailsResponseModel("Failure", "Api error", null)).build();
        }
    }

    @ApiOperation(value = "get test results")
    @GET
    @Path("/results")
    public Response getTestResults(@QueryParam("id") String testId){
        try {
            List<TestResult> testResult = dao.getTestResults(testId);
            if(testResult != null){
                TestResultsResponseModel testResultsResponseModel = new TestResultsResponseModel("Success","The record is fetched",testResult);
                return Response.ok(testResultsResponseModel).build();
            }
            else{
                TestResultsResponseModel testResultsResponseModel = new TestResultsResponseModel("Failed","The record is not found",null);
                return Response.ok(testResultsResponseModel).build();
            }

        }catch (Exception e){
            e.printStackTrace();
            return Response.serverError().entity(new TestDetailsResponseModel("Failure", "Api error", null)).build();
        }
    }

    @ApiOperation(value = "details of all tests")
    @GET
    public Response getAllTestDetails(){
        try {
            List<AllTestDetails> allTestDetails = dao.getAllTestDetails();
            if (allTestDetails != null){
                AllTestDetailsResponseModel allTestDetailsResponseModel = new AllTestDetailsResponseModel("Success","The test details are fetched",allTestDetails);
                return Response.ok(allTestDetailsResponseModel).build();
            }
            else {
                AllTestDetailsResponseModel allTestDetailsResponseModel = new AllTestDetailsResponseModel("Failure","The test details couldnt be fetched",null);
                return Response.ok(allTestDetailsResponseModel).build();
            }

        }catch (Exception e){
            e.printStackTrace();
            return Response.serverError().entity(new AllTestDetailsResponseModel("Failure","Api error",null)).build();
        }
    }

    @ApiOperation(value = "upload the file")
    @PUT
    @Path("/{id}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @PathParam("id") String id,
            @FormDataParam("file") InputStream inputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetails
            ) throws IOException {
        try {
            String location = "/home/nandu/Documents/TestAutomationToolUploads/"+ fileDetails.getFileName();
            saveFile(inputStream, location);
            String output = "The file is uploaded to " + location;
            UploadedDocumentsDetails uploadedDocumentsDetails = dao.addUploadedDocumentsDetail(id,location);
            ExcelSheetReader excelSheetReader = new ExcelSheetReader();
            System.out.println(excelSheetReader.readFromExcelFile(location));
            if (uploadedDocumentsDetails != null)
                return Response.ok(new UploadedDocumentsModel("Success", "Successfully inserted : "+ output, uploadedDocumentsDetails)).build();
            else
                return Response.ok(new BaseResponse("Failed","couldnt upload")).build();
        }catch (Exception e){
            e.printStackTrace();
            return Response.serverError().entity(new BaseResponse("Failure","Api error")).build();
        }
    }

    private void saveFile(InputStream uploadedFileStream, String location) throws IOException {
        int read;
        byte[] bytes = new byte[1024];
        OutputStream outputStream = new FileOutputStream(new File(location));
        while ((read = uploadedFileStream.read(bytes)) != -1){
            outputStream.write(bytes,0,read);
        }
        outputStream.flush();
        outputStream.close();
    }

}
