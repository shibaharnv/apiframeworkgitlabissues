package base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Properties;
import java.util.Random;


public class Utils {

    public static RequestSpecification req;

    public RequestSpecification requestSpecificationMethod() throws IOException {

        if(req==null) {

            PrintStream log = new PrintStream(new FileOutputStream("src/test/java/logs/logs.txt"));

            req = new RequestSpecBuilder().setBaseUri(getGlobalValue("baseUrl"))
                    .addHeader("Authorization", "Bearer "+getGlobalValue("oauthtoken")+"")
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .build();
            return req;
        }
        return req;

    }


    public static String getGlobalValue(String key) throws IOException {

        Properties prop = new Properties();
        String path =System.getProperty("user.dir");
        String end="/src/test/resources/global.properties";
        String fullPath =path.concat(end);
        FileInputStream fis =new FileInputStream(fullPath);
        prop.load(fis);
        return prop.getProperty(key);

    }


    public String getJsonPath(Response response,String key)
    {
        String resp=response.asString();
        JsonPath   js = new JsonPath(resp);
        return js.get(key).toString();
    }


    public static String randomStringGeneration() {
        Random robj = new Random();
        String alphabets = "abcdefghijklmnopqrstuvwxyz";
        String newWord = "";
        for (int i = 0; i < 10; i++) {
            char c = alphabets.charAt(robj.nextInt(15));
            newWord = newWord + c;
        }
        return newWord;
    }

}
