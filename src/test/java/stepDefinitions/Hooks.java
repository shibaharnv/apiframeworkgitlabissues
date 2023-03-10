package stepDefinitions;

import java.io.IOException;

import io.cucumber.java.Before;
import Base.TestDataBuild;

public class Hooks {

    public  static String firstName = "";
    public  static String lastName = "";

    public static String checkInDate="";

    public static String checkOutDate="";


    @Before("@Regression or @EdgeCase")
    public void beforeScenario() throws IOException

    {
        // this code will be executed only when issue id is null

        StepDefinition m =new StepDefinition();

        if(StepDefinition.issueIid==null)
        {
            m.setting_up_the_request_specification_for_gitlab_issues_api();
            m.user_creates_new_issue_with_and("new issue Title","new description");
            m.validate_the_from_output_response(201);
            m.extract_the_response_and_store_the_issue_id();

        }

    }



}