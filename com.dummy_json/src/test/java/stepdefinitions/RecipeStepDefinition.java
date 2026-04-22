package stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import Utils.ExcelUtil;
import Utils.ApiUtil;

import java.util.Map;

public class RecipeStepDefinition {

    String testCaseID;
    Map<String, String> testData;
    Response response;

    // ===============================
    // HARD-SET BASE URI HERE
    // ===============================

    @Given("I read test data {string}")
    public void read_test_data(String id) {

        // ✅ Hard-coded base URI
        RestAssured.baseURI = "https://dummyjson.com";

        testCaseID = id;

        testData = ExcelUtil.getData(testCaseID);

        if (testData == null || testData.isEmpty()) {

            throw new RuntimeException(
                    "Test data NOT found for: "
                            + testCaseID);
        }

        System.out.println("\n=================================");
        System.out.println("Running TestCase: " + testCaseID);
        System.out.println("Test Data: " + testData);
    }

    // ===============================
    // PRECONDITION
    // ===============================

    @Given("I validate precondition")
    public void validate_precondition() {

        if (testData == null) {

            throw new RuntimeException(
                    "Precondition Failed for: "
                            + testCaseID);
        }
    }

    // ===============================
    // PERFORM REQUEST
    // ===============================

    @When("I perform API request")
    public void perform_api_request() {

        String method =
                testData.get("method");

        String endpoint =
                testData.get("endpoint");

        String body =
                testData.get("testdata");

        String token =
                testData.get("token");

        // Defaults

        if (method == null || method.isEmpty()) {
            method = "GET";
        }

        if (endpoint == null || endpoint.isEmpty()) {

            throw new RuntimeException(
                    "Endpoint EMPTY for: "
                            + testCaseID);
        }

        if (body == null) {
            body = "";
        }

        if (token == null) {
            token = "";
        }

        System.out.println("METHOD   = " + method);
        System.out.println("ENDPOINT = " + endpoint);
        System.out.println("BODY     = " + body);

        try {

            response =
                    ApiUtil.send(
                            method,
                            endpoint,
                            body,
                            token);

        }

        catch (Exception e) {

            System.out.println(
                    "API Exception for: "
                            + testCaseID);

            e.printStackTrace();

            throw new RuntimeException(
                    "Request Failed for: "
                            + testCaseID);
        }
    }

    // ===============================
    // VALIDATE RESPONSE
    // ===============================

    @Then("I validate expected result")
    public void validate_expected_result() {

        if (response == null) {

            throw new RuntimeException(
                    "Response NULL for: "
                            + testCaseID);
        }

        int expectedStatus =
                Integer.parseInt(
                        testData.get("expectedstatus"));

        int actualStatus =
                response.getStatusCode();

        System.out.println(
                "Expected Status: "
                        + expectedStatus);

        System.out.println(
                "Actual Status: "
                        + actualStatus);

        if (actualStatus != expectedStatus) {

            throw new AssertionError(

                    "FAILED TestCase: "
                            + testCaseID
                            + " | Expected: "
                            + expectedStatus
                            + " | Actual: "
                            + actualStatus);
        }

        System.out.println(
                "PASSED TestCase: "
                        + testCaseID);
    }
}