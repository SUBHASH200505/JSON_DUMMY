package stepdefinitions;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import Utils.*;

import java.util.Map;

public class RecipeStepDefinition {

    String testCaseID;
    Map<String, String> testData;
    Response response;

    String endpoint;

    // =========================================
    // NORMAL / OUTLINE STEPS
    // =========================================

    @Given("I set endpoint {string}")
    public void setEndpoint(String ep) {

        RestAssured.baseURI = "https://dummyjson.com";
        endpoint = ep;
    }

    @When("I send GET request")
    public void sendGetRequest() {

        response = ApiUtil.send("GET", endpoint, "", "");
    }

    @Then("Response status code should be {int}")
    public void validateStatus(int status) {

        int actual = response.getStatusCode();

        if (actual != status) {
            throw new AssertionError(
                    "Expected: " + status + " but got: " + actual);
        }
    }

    // =========================================
    // DATA TABLE POST
    // =========================================

    @When("I send POST request with body:")
    public void sendPostRequest(DataTable table) {

        Map<String, String> map = table.asMaps().get(0);

        String jsonBody = "{"
                + "\"name\":\"" + map.get("name") + "\","
                + "\"ingredients\":\"" + map.get("ingredients") + "\","
                + "\"instructions\":\"" + map.get("instructions") + "\""
                + "}";

        response = ApiUtil.send("POST", endpoint, jsonBody, "");
    }

    // =========================================
    // EXCEL STEPS
    // =========================================

    @Given("I read test data {string}")
    public void read_test_data(String id) {

        RestAssured.baseURI = "https://dummyjson.com";

        testCaseID = id;

        testData = ExcelUtil.getData(testCaseID);

        if (testData == null || testData.isEmpty()) {
            throw new RuntimeException(
                    "Test data NOT found for: " + testCaseID);
        }
    }

    @Given("I validate precondition")
    public void validate_precondition() {

        if (testData == null) {
            throw new RuntimeException(
                    "Precondition Failed for: " + testCaseID);
        }
    }

    @When("I perform API request")
    public void perform_api_request() {

        String method = testData.getOrDefault("method", "GET");
        String ep = testData.get("endpoint");
        String body = testData.getOrDefault("testdata", "");
        String token = testData.getOrDefault("token", "");

        response = ApiUtil.send(method, ep, body, token);
    }

    @Then("I validate expected result")
    public void validate_expected_result() {

        int expected =
                Integer.parseInt(testData.get("expectedstatus"));

        int actual = response.getStatusCode();

        if (actual != expected) {
            throw new AssertionError(
                    "FAILED " + testCaseID +
                            " | Expected: " + expected +
                            " | Actual: " + actual);
        }
    }
}