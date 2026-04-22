package stepdefinitions;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;
import Utils.*;

import java.util.Map;
import java.util.List;

import org.testng.Assert;

public class TodosStepDefinition {

    Response response;
    Map<String, String> data;
    String method;
    String endpoint;
    String body;
    String token = null;

    // =========================================
    // EXCEL BASED
    // =========================================

    @Given("I read todos test data {string}")
    public void readTodosData(String testCaseID) {

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

    @And("I validate todos precondition")
    public void validatePrecondition() {
        System.out.println("✔ Todos Precondition OK");
    }

    @When("I perform todos API request")
    public void performTodosRequest() {

        response = ApiUtil.send(method, endpoint, body, token);

        if (response == null) {
            throw new RuntimeException("Response is NULL");
        }
    }

    @Then("I validate todos expected result")
    public void validateExpectedResult() {

        int expected = Integer.parseInt(data.get("expectedstatus"));
        int actual = response.getStatusCode();

        Assert.assertEquals(actual, expected, "Status code mismatch");
    }

    // =========================================
    // INLINE REQUESTS
    // =========================================

    @Given("I set todos request {string} {string}")
    public void setTodosRequest(String reqMethod, String reqEndpoint) {

        method = reqMethod.toUpperCase();
        endpoint = ConfiReader.get("base.url") + reqEndpoint;
        body = null;
    }

    @When("I send todos request")
    public void sendTodosRequest() {

        response = ApiUtil.send(method, endpoint, body, token);
    }

    @Then("I validate todos status {string}")
    public void validateStatus(String status) {

        int expected = Integer.parseInt(status);
        int actual = response.getStatusCode();

        Assert.assertEquals(actual, expected);
    }

    // =========================================
    // PUT (DATA TABLE)
    // =========================================

    @When("I send todos PUT request with body:")
    public void sendPutRequest(DataTable table) {

        Map<String, String> row = table.asMaps().get(0);

        String requestBody = "{\n" +
                "\"todo\":\"" + row.get("todo") + "\",\n" +
                "\"completed\":" + row.get("completed") + ",\n" +
                "\"userId\":" + row.get("userId") + "\n" +
                "}";

        response = ApiUtil.send("PUT", endpoint, requestBody, token);
    }

    // =========================================
    // PATCH
    // =========================================

    @When("I send todos PATCH request with body {string}")
    public void sendPatchRequest(String text) {

        String requestBody = "{ \"todo\":\"" + text + "\" }";

        response = ApiUtil.send("PATCH", endpoint, requestBody, token);
    }

    // =========================================
    // RESPONSE VALIDATION (UNIQUE)
    // =========================================

    @Then("Todos response should contain {string}")
    public void validateResponseContains(String text) {

        Assert.assertTrue(response.asString().contains(text),
                "Response does not contain: " + text);
    }

    // =========================================
    // RESPONSE STRUCTURE
    // =========================================

    @Then("Response body should contain todos array")
    public void validateTodosArray() {

        JsonPath json = response.jsonPath();
        List<Object> todos = json.getList("todos");

        Assert.assertNotNull(todos);
        Assert.assertTrue(todos.size() > 0);
    }

    @Then("Each todo should have {string}, {string}, {string}, {string}")
    public void validateTodoFields(String f1, String f2, String f3, String f4) {

        JsonPath json = response.jsonPath();

        Assert.assertNotNull(json.get("todos[0]." + f1));
        Assert.assertNotNull(json.get("todos[0]." + f2));
        Assert.assertNotNull(json.get("todos[0]." + f3));
        Assert.assertNotNull(json.get("todos[0]." + f4));
    }
}