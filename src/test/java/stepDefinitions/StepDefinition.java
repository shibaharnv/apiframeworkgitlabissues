package stepDefinitions;

import base.ApiResourcesEnum;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import base.Utils;

import java.io.IOException;

import static org.junit.Assert.*;
import static io.restassured.RestAssured.given;



public class StepDefinition extends Utils {

    ResponseSpecification responseSpec;

    RequestSpecification reqObj;
    Response response;

    static String issueIid;


    @Given("Setting up the request specification for gitlab issues api")
    public void setting_up_the_request_specification_for_gitlab_issues_api() throws IOException {

        reqObj = given().spec(requestSpecificationMethod());
    }

    @When("User creates new issue with {string} and {string}")
    public void user_creates_new_issue_with_and(String issueTitle, String description) {

        response = reqObj.queryParam("title",issueTitle)
                .queryParam("description",description).when().post();

    }



    @Then("Validate the {int} from output response")
    public void validate_the_from_output_response(Integer statusCode) {

    if(statusCode == 200 ||statusCode==201)
    {
        responseSpec = new ResponseSpecBuilder().expectStatusCode(statusCode).expectContentType(ContentType.JSON).build();
    }

        if(statusCode != 200 ||statusCode!=201)
        {
            responseSpec = new ResponseSpecBuilder().expectStatusCode(statusCode).build();
        }

        response =response.then().spec(responseSpec).log().all().extract().response();
        assertEquals(response.getStatusCode(), (int) statusCode);
    }


    @Then("Extract the response and store the issue_id")
    public void extract_the_response_and_store_the_issue_id() {

        issueIid = getJsonPath(response,"iid");
        System.out.println("iid " + issueIid);
    }

    @Then("Verify the {string} and {string} in output response")
    public void verify_the_and_in_output_response(String expectedTitle, String expectedDescription) {

        String actualTitle=getJsonPath(response,"title");
        String actualDescription = getJsonPath(response,"description");
        assertEquals(expectedTitle,actualTitle);
        assertEquals(expectedDescription,actualDescription);
        System.out.println("Assertion Passed");

    }

    @When("User update the existing issue {string} and {string}")
    public void user_update_the_existing_issue_and(String title, String description) {

        response = reqObj.queryParam("title",title)
                .queryParam("description",description).when().put(("/"+issueIid+""));

    }


    @When("User deletes the existing gitlab issue")
    public void user_deletes_the_existing_gitlab_issue() {
        response = reqObj
                .when().delete(("/"+issueIid+""));
    }


    @When("User moves an issue to {string}")
    public void user_moves_an_issue_to(String newProjectId) {
        response = reqObj.contentType("multipart/form-data")
                .multiPart("to_project_id", newProjectId)
                .when().post(("/"+issueIid+"/move"));

    }



    @When("User clone the issue with {string} and {string}")
    public void user_clone_the_issue_with_and(String notes, String newProjectId) {
        response = reqObj
                .queryParam("with_notes",notes)
                .queryParam("to_project_id",newProjectId)
                .when().post(("/"+issueIid+"/clone"));


    }


    @When("User {string} an issue in Gitlab with {string} and {string}")
    public void user_an_issue_in_gitlab_with_and(String resource, String newProjectId, String notes) {

        ApiResourcesEnum resourcObj = ApiResourcesEnum.valueOf(resource);
        System.out.println(resourcObj.getresourcemethod());
        String actualResource=resourcObj.getresourcemethod();

        if(resource.equals("subscribe") || resource.equals("unsubscribe"))
        {
            response = reqObj
                    .when().post("/"+issueIid+""+actualResource);
        }

        if(resource.equals("clone")) {
            response = reqObj
                    .queryParam("with_notes", notes)
                    .queryParam("to_project_id", newProjectId)
                    .when().post("/" + issueIid + "" + actualResource);
        }

        if(resource.equals("move"))
        {
            response = reqObj.contentType("multipart/form-data")
                    .multiPart("to_project_id", newProjectId)
                    .when().post(("/"+issueIid+"/move"));
        }

    }





}
