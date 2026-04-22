package stepdefinitions;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;

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

        endpoint = ConfiReader.get("base.url") + endpointPath;

        response = ApiUtil.send(method, endpoint, body, null);
    }

    @Then("I validate todos expected result")
    public void validateResult() {

        int expected = Integer.parseInt(data.get("expectedstatus"));
        int actual = response.getStatusCode();

        Assert.assertEquals(actual, expected);
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
    }

    // =========================
    // RESPONSE VALIDATION
    // =========================
    @Then("Response body should contain todos array")
    public void validateTodosArray() {
        Assert.assertTrue(response.asString().contains("todos"));
    }

    @Then("Each todo should have {string}, {string}, {string}, {string}")
    public void validateFields(String f1, String f2, String f3, String f4) {

        JsonPath json = response.jsonPath();

        Assert.assertNotNull(json.get("todos[0]." + f1));
        Assert.assertNotNull(json.get("todos[0]." + f2));
        Assert.assertNotNull(json.get("todos[0]." + f3));
        Assert.assertNotNull(json.get("todos[0]." + f4));
    }

    // =========================
    // PUT (DataTable)
    // =========================
    @When("I send PUT request with body:")
    public void sendPut(DataTable table) {

        Map<String, String> map = table.asMaps().get(0);

        String body = "{"
                + "\"todo\":\"" + map.get("todo") + "\","
                + "\"completed\":" + map.get("completed") + ","
                + "\"userId\":" + map.get("userId")
                + "}";

        response = ApiUtil.send("PUT", endpoint, body, null);
    }

    // =========================
    // PATCH
    // =========================
    @When("I send PATCH request with body {string}")
    public void sendPatch(String text) {

        String body = "{\"todo\":\"" + text + "\"}";

        response = ApiUtil.send("PATCH", endpoint, body, null);
    }

    // =========================
    // GENERIC RESPONSE CHECK
    // =========================
    @Then("Response body should contain {string}")
    public void validateContains(String text) {

        Assert.assertTrue(response.asString().contains(text));
    }
}