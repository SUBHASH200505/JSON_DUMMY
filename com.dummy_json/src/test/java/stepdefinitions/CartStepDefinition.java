package stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import Utils.*;

import java.util.Map;

import org.testng.Assert;

public class CartStepDefinition {

    Map<String, String> data;
    Response response;

    String method;
    String endpoint;
    String token; 

    @Given("I read carts test data {string}")
    public void readData(String testCaseID) {

        data = ExcelUtil.getData(testCaseID);

        if (data == null || data.isEmpty()) {
            throw new RuntimeException("No data found for " + testCaseID);
        }

        
        token = data.get("token");

        System.out.println("DATA: " + data);
    }

    @And("I validate carts precondition")
    public void validatePrecondition() {
        System.out.println("Precondition OK");
    }

    @When("I perform carts API request")
    public void performRequest() {

        method = data.getOrDefault("method", "GET");
        String endpointPath = data.getOrDefault("endpoint", "/carts");
        String body = data.get("testdata");

        endpoint = ConfiReader.get("base.url") + endpointPath;

        System.out.println("METHOD: " + method);
        System.out.println("ENDPOINT: " + endpoint);
        System.out.println("BODY: " + body);
        System.out.println("TOKEN: " + token);

       
        response = ApiUtil.send(method, endpoint, body, token);

        System.out.println("RESPONSE: " + response.asString());
    }

    @Then("I validate carts expected result")
    public void validateResult() {

        int expected = Integer.parseInt(data.get("expectedstatus"));
        int actual = response.getStatusCode();

        Assert.assertEquals(actual, expected,
                "FAILED → Expected: " + expected + " but got: " + actual);

        System.out.println("✔ Excel Test Passed");
    }



    @Given("I set carts request {string} {string}")
    public void setRequest(String m, String e) {

        method = m;
        endpoint = ConfiReader.get("base.url") + e;
        token = null; // default
    }

    @When("I send carts request")
    public void sendRequest() {

        response = ApiUtil.send(method, endpoint, null, token);
    }

    @Then("I validate carts status {string}")
    public void validateStatus(String status) {

        int expected = Integer.parseInt(status);
        int actual = response.getStatusCode();

        Assert.assertEquals(actual, expected);

        System.out.println("✔ Inline Test Passed");
    }
}