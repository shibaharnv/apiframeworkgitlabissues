package cucumber.Options;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import utilities.Email;


import java.io.IOException;

@RunWith(Cucumber.class)
@CucumberOptions(publish = true,features="src/test/java/features",plugin ="json:target/jsonReports/cucumber-report.json",glue= {"stepDefinitions"},tags="@EdgeCase or @Crud")



public class TestRunner {

    @BeforeClass
    public static void setUp(){
        System.out.println("This is set up method.");
    }

    @AfterClass
    public static void tearDown() throws IOException {

        String recipient=System.getProperty("RecipientList");
        System.out.println(recipient);

        Email.emailTrigger(recipient);


    }
}
