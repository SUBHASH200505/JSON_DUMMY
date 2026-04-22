package stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import Utils.*;

import java.util.Map;

import org.testng.Assert;

public class TodosStepDefinition {

    Map<String, String> data;
    Response response;

    String method;
    String endpoint;

    // =========================
    // EXCEL
    // =========================
    @Given("I read todos test data {string}")
    public void readData(String testCaseID) {

        data = ExcelUtil.getData(testCaseID);

        if (data == null || data.isEmpty()) {
            throw new RuntimeException("No data found for " + testCaseID);
        }

        System.out.println("DATA: " + data);
    }

    @And("I validate todos precondition")
    public void validatePrecondition() {
        System.out.println("Precondition OK");
    }

    @When("I perform todos API request")
    public void performRequest() {

        method = data.getOrDefault("method", "GET");
        String endpointPath = data.getOrDefault("endpoint", "/todos");
        String body = data.get("testdata");

        // ✅ FIXED CLASS NAME
        endpoint = ConfiReader.get("base.url") + endpointPath;

        System.out.println("METHOD: " + method);
        System.out.println("ENDPOINT: " + endpoint);
        System.out.println("BODY: " + body);

        // ✅ FIXED send() call
        response = ApiUtil.send(method, endpoint, body, null);

        System.out.println("RESPONSE: " + response.asString());
    }

    @Then("I validate todos expected result")
    public void validateResult() {

        int expected = Integer.parseInt(data.get("expectedstatus"));
        int actual = response.getStatusCode();

        Assert.assertEquals(actual, expected,
                "FAILED → Expected: " + expected + " but got: " + actual);

        System.out.println("✔ Excel Test Passed");
    }

    // =========================
    // INLINE (GET & DELETE)
    // =========================
    @Given("I set todos request {string} {string}")
    public void setRequest(String m, String e) {

        method = m;
        endpoint = ConfiReader.get("base.url") + e;
    }

    @When("I send todos request")
    public void sendRequest() {

        response = ApiUtil.send(method, endpoint, null, null);
    }

    @Then("I validate todos status {string}")
    public void validateStatus(String status) {

        int expected = Integer.parseInt(status);
        int actual = response.getStatusCode();

        Assert.assertEquals(actual, expected);

        System.out.println("✔ Inline Test Passed");
    }
}