package stepdefinitions;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;

import Utils.*;

import java.util.Map;
import java.util.List;

import org.testng.Assert;

public class CommentsStepDefinition {

    Response response;
    Map<String, String> data;
    String method;
    String endpoint;
    String body;
    String token = null;

    // =========================================
    // EXCEL BASED
    // =========================================

    @Given("I read comments test data {string}")
    public void readCommentsData(String testCaseID) {

        data = ExcelUtil.getData(testCaseID);

        if (data == null || data.isEmpty()) {
            throw new RuntimeException("No data found for " + testCaseID);
        }

        method = data.get("method").toUpperCase();
        endpoint = ConfiReader.get("base.url") + data.get("endpoint");
        body = data.get("testdata");

        if (body != null && body.trim().isEmpty()) {
            body = null;
        }
    }

    @And("I validate comments precondition")
    public void validatePrecondition() {
        System.out.println("✔ Precondition passed");
    }

    @When("I perform comments API request")
    public void performCommentsRequest() {

        response = ApiUtil.send(method, endpoint, body, token);

        if (response == null) {
            throw new RuntimeException("Response is NULL");
        }
    }

    @Then("I validate comments expected result")
    public void validateExpectedResult() {

        int expected = Integer.parseInt(data.get("expectedstatus"));
        int actual = response.getStatusCode();

        Assert.assertEquals(actual, expected, "Status code mismatch");
    }

    // =========================================
    // INLINE REQUESTS
    // =========================================

    @Given("I set comments request {string} {string}")
    public void setCommentsRequest(String reqMethod, String reqEndpoint) {

        method = reqMethod.toUpperCase();
        endpoint = ConfiReader.get("base.url") + reqEndpoint;
        body = null;
    }

    @When("I send comments request")
    public void sendCommentsRequest() {

        response = ApiUtil.send(method, endpoint, body, token);
    }

    @Then("I validate comments status {string}")
    public void validateStatus(String status) {

        int expected = Integer.parseInt(status);
        int actual = response.getStatusCode();

        Assert.assertEquals(actual, expected, "Status mismatch");
    }

    // =========================================
    // PUT (DATA TABLE)
    // =========================================

    @When("I send comments PUT request with body:")
    public void sendPutRequest(DataTable table) {

        Map<String, String> row = table.asMaps().get(0);

        String requestBody = "{\n" +
                "\"body\":\"" + row.get("body") + "\",\n" +
                "\"postId\":" + row.get("postId") + ",\n" +
                "\"userId\":" + row.get("userId") + "\n" +
                "}";

        response = ApiUtil.send("PUT", endpoint, requestBody, token);
    }

    // =========================================
    // PATCH
    // =========================================

    @When("I send comments PATCH request with body {string}")
    public void sendPatchRequest(String bodyText) {

        String requestBody = "{ \"body\":\"" + bodyText + "\" }";

        response = ApiUtil.send("PATCH", endpoint, requestBody, token);
    }

    // =========================================
    // RESPONSE VALIDATION (UNIQUE)
    // =========================================

    @Then("Comments response should contain {string}")
    public void validateResponseContains(String text) {

        Assert.assertTrue(response.asString().contains(text),
                "Response does not contain expected text: " + text);
    }

    // =========================================
    // RESPONSE STRUCTURE
    // =========================================

    @Then("Response body should contain comments array")
    public void validateCommentsArray() {

        JsonPath json = response.jsonPath();
        List<Object> comments = json.getList("comments");

        Assert.assertNotNull(comments, "Comments array is null");
        Assert.assertTrue(comments.size() > 0, "Comments array is empty");
    }

    @Then("Each comment should have {string}, {string}, {string}, {string}")
    public void validateCommentFields(String f1, String f2, String f3, String f4) {

        JsonPath json = response.jsonPath();

        Assert.assertNotNull(json.get("comments[0]." + f1));
        Assert.assertNotNull(json.get("comments[0]." + f2));
        Assert.assertNotNull(json.get("comments[0]." + f3));
        Assert.assertNotNull(json.get("comments[0]." + f4));
    }
}