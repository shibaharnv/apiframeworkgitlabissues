package stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import Base.TestDataBuild;
import Base.Utils;
import java.io.File;
import java.io.IOException;
import java.util.*;
import static org.junit.Assert.*;
import static io.restassured.RestAssured.given;



public class StepDefinition extends Utils {

    ResponseSpecification responseSpec;

    RequestSpecification reqObj;
    Response response;

    TestDataBuild data = new TestDataBuild();


    static String issueIid;

    static String petID;



    @Given("Setting up the request specification for gitlab issues api")
    public void setting_up_the_request_specification_for_gitlab_issues_api() throws IOException {

        reqObj = given().spec(requestSpecificationMethod());
    }

    @When("User creates new issue with {string} and {string}")
    public void user_creates_new_issue_with_and(String issueTitle, String description) {

       // responseSpec = new ResponseSpecBuilder().expectStatusCode(201).expectContentType(ContentType.JSON).build();
//
//        response = reqObj.queryParam("title",issueTitle)
//                .queryParam("labels",label).when().post().then().spec(responseSpec).log().all().extract().response();


        response = reqObj.queryParam("title",issueTitle)
                .queryParam("description",description).when().post();

    }

    @Then("Validate the {int} from output respose")
    public void validate_the_from_output_respose(Integer statusCode) {
        responseSpec = new ResponseSpecBuilder().expectStatusCode(201).expectContentType(ContentType.JSON).build();
        response =response.then().spec(responseSpec).log().all().extract().response();
        assertEquals(response.getStatusCode(), (int) statusCode);
    }


    @Then("Extract the response and store the issueid")
    public void extract_the_response_and_store_the_issueid() {


        String res = response.asString();
        JsonPath js = new JsonPath(res);
        issueIid = js.get("iid").toString();
        System.out.println("iid " + issueIid);

    }




    @Given("Setting up the request sepecification for create pet with image")
    public void setting_up_the_request_sepecification_for_create_pet_with_image() throws IOException {

        String filePath=System.getProperty("user.dir")+"/src/test/java/files/test1.png";
        System.out.println(filePath);
        File fobj = new File(filePath);
        reqObj = given().spec(requestSpecificationMethod()).multiPart("file",fobj);

    }


    @When("The user hits the endpoint with {string}")
    public void the_user_hits_the_endpoint_with(String httpMethod) {
        responseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();
        response = reqObj.when().post("/123/uploadImage").then().spec(responseSpec).log().all().extract().response();

    }


    @Given("Setting up the request sepecification for create pet with payload")
    public void setting_up_the_request_sepecification_for_create_pet_with_payload() throws IOException {
        reqObj = given().spec(requestSpecificationMethod()).contentType(ContentType.JSON).body(data.createPetPayLoad());
    }


    @When("The user hits the pet payload endpoint with {string}")
    public void the_user_hits_the_pet_payload_endpoint_with(String httpMethod) {
        responseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();

        if(httpMethod.equalsIgnoreCase("POST"))
        {
            response = reqObj.when().post().then().spec(responseSpec).log().all().extract().response();
        }

    }


    @When("User hits the endpoint for {string}")
    public void user_hits_the_endpoint_for(String httpMethod) {


        if(httpMethod.equalsIgnoreCase("GET"))
        {
            responseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();
            response = reqObj.when().get("/"+petID+"").then().spec(responseSpec).log().all().extract().response();
        }

        if(httpMethod.equalsIgnoreCase("DELETE"))
        {
            responseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();
            response = reqObj.when().delete("/"+petID+"").then().spec(responseSpec).log().all().extract().response();
        }

    }



    @Given("Setting up the pet request sepecification")
    public void setting_up_the_pet_request_sepecification() throws IOException {
        reqObj = given().spec(requestSpecificationMethod());
    }



    @When("User creates a pet with below values")
    public void user_creates_a_pet_with_below_values(DataTable dataTable) throws IOException {

        List<List<String>> dataList = dataTable.asLists(String.class);

        System.out.println("datalist "+dataList);

        for (List<String> singleList : dataList) {

            System.out.println(singleList);
            reqObj = reqObj.contentType(ContentType.JSON).body(data.createPetPayLoadList(singleList));
            responseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();
            response = reqObj.when().post().then().spec(responseSpec).log().all().extract().response();

        }
    }


    @When("User updates a pet with below values")
    public void user_updates_a_pet_with_below_values(DataTable dataTable) {
        List<List<String>> dataList = dataTable.asLists(String.class);

        for (List<String> singleList : dataList) {

            System.out.println(singleList);
            reqObj = reqObj.contentType(ContentType.JSON).body(data.createPetUpdatedPayLoadList(singleList,petID));
            responseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();
            response = reqObj.when().put().then().spec(responseSpec).log().all().extract().response();

        }
    }


    @Then("Verify the updated values")
    public void verify_the_updated_values(DataTable dataTable) {

        List<List<String>> dataList = dataTable.asLists(String.class);

        for (List<String> singleList : dataList) {

            System.out.println(singleList);
            String res = response.asString();
            String actualpetID = getJsonPath(response,"id");
            Assert.assertEquals(actualpetID,petID);
            System.out.println("petID " + petID);



            //name and status verification

            String actualName = getJsonPath(response,"name");
            Assert.assertEquals(actualName,data.removeExtraQuotes(singleList.get(0)));
            System.out.println("actualname " + actualName);

            String actualStatus = getJsonPath(response,"status");
            Assert.assertEquals(actualStatus,data.removeExtraQuotes(singleList.get(1)));
            System.out.println("actualStatus " + actualStatus);

            //Photo urls verification

            List actualPhotoList=getJsonPathList(res,"photoUrls");

            String[] photostringArray = singleList.get(2).split(";");
            ArrayList<String> expPhotoList = new ArrayList<String>();
            for(String singleUrl:photostringArray)
            {
                String alString= data.removeExtraQuotes(singleUrl);
                expPhotoList.add(alString);
            }
            Assert.assertTrue(actualPhotoList.equals(expPhotoList));



            JSONObject jsonObject = new JSONObject(res);
            JSONObject getCategory = jsonObject.getJSONObject("category");
            String actualCategoryId=getCategory.get("id").toString();
            String actualCategoryName=getCategory.get("name").toString();
            Assert.assertEquals(actualCategoryId,data.removeExtraQuotes(singleList.get(5)));
            Assert.assertEquals(actualCategoryName,data.removeExtraQuotes(singleList.get(6)));



            JSONArray gettags = jsonObject.getJSONArray("tags");
            JSONObject actualtagsId=gettags.getJSONObject(0);
            String actualtagsIdString=actualtagsId.get("id").toString();
            String actualtagNameString=actualtagsId.get("name").toString();
            System.out.println(actualtagsIdString);
            Assert.assertEquals(actualtagsIdString,data.removeExtraQuotes(singleList.get(3)));
            Assert.assertEquals(actualtagNameString,data.removeExtraQuotes(singleList.get(4)));

        }
    }


    @Then("Validate if the API call got success with status code {int}")
    public void validate_if_the_api_call_got_success_with_status_code(int status_code) {
        assertEquals(response.getStatusCode(), status_code);
    }


    @Then("Store the petId from the response body")
    public void store_the_pet_id_from_the_response_body() {
        String res = response.asString();
        JsonPath js = new JsonPath(res);
        petID = js.get("id").toString();
        System.out.println("petID " + petID);
    }


    @Then("Validate the {int} from response")
    public void validate_the_from_response(Integer statusCode) {

        assertEquals(response.getStatusCode(), (int) statusCode);
        System.out.println("Status code Assertion Passed ");

    }

}