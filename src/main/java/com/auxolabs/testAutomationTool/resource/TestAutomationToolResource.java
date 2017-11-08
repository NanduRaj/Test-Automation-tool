package com.auxolabs.testAutomationTool.resource;

import com.auxolabs.testAutomationTool.configuration.TestAutomationToolConfiguration;
import com.auxolabs.testAutomationTool.dao.TestAutomationToolDao;
import com.auxolabs.testAutomationTool.excelReader.ExcelSheetReader;
import com.auxolabs.testAutomationTool.models.AllTestDetails;
import com.auxolabs.testAutomationTool.models.TestDetails;
import com.auxolabs.testAutomationTool.models.response.BaseResponse;
import com.auxolabs.testAutomationTool.models.response.HomeScreenResponse;
import com.auxolabs.testAutomationTool.models.response.TestDetailsResponse;
import com.auxolabs.testAutomationTool.models.TestResult;
import com.auxolabs.testAutomationTool.models.response.TestResultsResponse;
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

@Path("/test")
@Api(value = "TestAutomationTool")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestAutomationToolResource {

    private TestAutomationToolDao dao;
    public TestAutomationToolResource(TestAutomationToolConfiguration configuration){
        this.dao = new TestAutomationToolDao(configuration);
    }

    @PUT
    @ApiOperation(value = "create new test",response = TestDetailsResponse.class)
    public Response createTest(PutPostTestDetailsRequestModel putPostTestDetailsRequestModel){
        try {
            TestDetails testDetails = dao.addTestDetails(putPostTestDetailsRequestModel);
            if (testDetails != null) {
                TestDetailsResponse testDetailsResponse = new TestDetailsResponse("Success","The record is created",testDetails);
                return Response.ok(testDetailsResponse).build();
            }
            else {
                TestDetailsResponse testDetailsResponse = new TestDetailsResponse("Failure","The record was not created",null);
                return Response.ok(testDetailsResponse).build();
            }
        }catch (Exception e){
            return Response.serverError().entity(new TestDetailsResponse("Failure", "Api error", null)).build();
        }
    }

    @ApiOperation(value = "get test details",response = TestDetailsResponse.class)
    @GET
    @Path("/{id}")
    public Response getTestDetails(@PathParam("id") ObjectId testId){
        try {
            TestDetails testDetails = dao.getTestDetails(testId);
            if (testDetails != null) {
                TestDetailsResponse testDetailsResponse= new TestDetailsResponse("Success", "The student has been fetched with id "+ testId, testDetails);
                return Response.ok(testDetailsResponse).build();
            } else {
                TestDetailsResponse testDetailsResponse = new TestDetailsResponse("Success", "No such record found", null);
                return Response.ok(testDetailsResponse).build();
            }
        }catch (Exception e) {
            return Response.serverError().entity(new TestDetailsResponse("Failure", "Api error", null)).build();
        }
    }

    @ApiOperation(value = "update a test",response = TestDetailsResponse.class)
    @POST
    @Path("/{id}")
    public Response updateTestDetails(@PathParam("id") ObjectId testId, PutPostTestDetailsRequestModel putPostTestDetailsRequestModel) {
        try {
            TestDetails testDetails = dao.updateTestDetails(testId, putPostTestDetailsRequestModel);
            if (testDetails != null) {
                TestDetailsResponse testDetailsResponse = new TestDetailsResponse("Success", "The record is updated", testDetails);
                return Response.ok(testDetailsResponse).build();
            } else {
                TestDetailsResponse testDetailsResponse = new TestDetailsResponse("Failure", "No such record found", null);
                return Response.ok(testDetailsResponse).build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(new TestDetailsResponse("Failure", "Api error", null)).build();
        }
    }

    @ApiOperation(value = "delete a test")
    @DELETE
    @Path("/{id}")
    public Response deleteTest(@PathParam("id") ObjectId testId){
        try {
            long isDeleted = dao.deleteTestDetails(testId);
            if (isDeleted > 0) {
                TestDetailsResponse testDetailsResponse = new TestDetailsResponse("Success", "The record is deleted", null);
                return Response.ok(testDetailsResponse).build();
            }
            else {
                TestDetailsResponse testDetailsResponse = new TestDetailsResponse("Failure", "The record not found", null);
                return Response.ok(testDetailsResponse).entity("Document not found").build();
            }
        }catch (Exception e){
            return Response.serverError().entity(new TestDetailsResponse("Failure", "Api error", null)).build();
        }
    }

    @ApiOperation(value = "get test results")
    @GET
    @Path("/{id}/results")
    public Response getTestResults(@PathParam("id") String testId){
        try {
            List<TestResult> testResult = dao.getTestResults(testId);
            if(testResult != null){
                TestResultsResponse testResultsResponse = new TestResultsResponse("Success","The record is fetched",testResult);
                return Response.ok(testResultsResponse).build();
            }
            else{
                TestResultsResponse testResultsResponse = new TestResultsResponse("Failed","The record is not found",null);
                return Response.ok(testResultsResponse).build();
            }

        }catch (Exception e){
            e.printStackTrace();
            return Response.serverError().entity(new TestDetailsResponse("Failure", "Api error", null)).build();
        }
    }

    @ApiOperation(value = "details of all tests")
    @GET
    public Response getAllTestDetails(){
        try {
            List<AllTestDetails> allTestDetails = dao.getAllTestDetails();
            if (allTestDetails != null){
                HomeScreenResponse homeScreenResponse = new HomeScreenResponse("Success","The test details are fetched",allTestDetails);
                return Response.ok(homeScreenResponse).build();
            }
            else {
                HomeScreenResponse homeScreenResponse = new HomeScreenResponse("Failure","The test details couldnt be fetched",null);
                return Response.ok(homeScreenResponse).build();
            }

        }catch (Exception e){
            e.printStackTrace();
            return Response.serverError().entity(new HomeScreenResponse("Failure","Api error",null)).build();
        }
    }

    @ApiOperation(value = "upload the file")
    @PUT
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream inputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetails
            ) throws IOException {
        try {
            File currentDir = new File("");
            String basepath = currentDir.getCanonicalPath();
            String relativepath = "/src/main/resources/" + fileDetails.getFileName();
            String location = "/home/nandu/Documents/TestAutomationToolUploads"+ fileDetails.getFileName();
            saveFile(inputStream, location);
            String output = "The file is uploaded to " + location;
            ExcelSheetReader excelSheetReader = new ExcelSheetReader();
            System.out.println(excelSheetReader.readFromExcelFile(location));
            return Response.ok(new BaseResponse("Success", output)).build();
        }catch (Exception e){
            e.printStackTrace();
            return Response.serverError().entity(new BaseResponse("Failure","Couldnt upload")).build();
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
