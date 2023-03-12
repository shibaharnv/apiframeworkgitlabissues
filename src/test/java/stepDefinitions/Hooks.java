package stepDefinitions;

import java.io.IOException;

import io.cucumber.java.Before;

import static base.Utils.randomStringGeneration;

public class Hooks {



    @Before("@Crud or @EdgeCase")
    public void beforeScenario() throws IOException

    {
        // this code will be executed only when issue Iid is null

        StepDefinition m =new StepDefinition();

        if(StepDefinition.issueIid==null)
        {
            m.setting_up_the_request_specification_for_gitlab_issues_api();
            m.user_creates_new_issue_with_and(randomStringGeneration(),randomStringGeneration());
            m.validate_the_from_output_response(201);
            m.extract_the_response_and_store_the_issue_id();

        }

    }



}