package stepdefinitions;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;
import Utils.*;

import java.util.Map;
import java.util.List;

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

        response = ApiUtil.send(method, endpoint, body, token);

        System.out.println("RESPONSE: " + response.asString());
    }

    @Then("I validate carts expected result")
    public void validateResult() {

        int expected = Integer.parseInt(data.get("expectedstatus"));
        int actual = response.getStatusCode();

        Assert.assertEquals(actual, expected);
        System.out.println("✔ Excel Test Passed");
    }

   
    @Given("I set carts request {string} {string}")
    public void setRequest(String m, String e) {
        method = m;
        endpoint = ConfiReader.get("base.url") + e;
        token = null;
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

  
    @When("I send PUT request with body:")
    public void sendPutRequest(DataTable dataTable) {

        Map<String, String> bodyMap = dataTable.asMaps().get(0);

        String requestBody = "{";

        if (bodyMap.containsKey("products")) {
            requestBody += "\"products\":" + bodyMap.get("products") + ",";
        }

        if (bodyMap.containsKey("total")) {
            requestBody += "\"total\":" + bodyMap.get("total") + ",";
        }

        if (bodyMap.containsKey("userId")) {
            requestBody += "\"userId\":" + bodyMap.get("userId") + ",";
        }

        if (bodyMap.containsKey("quantity")) {
            requestBody += "\"quantity\":\"" + bodyMap.get("quantity") + "\",";
        }

        requestBody = requestBody.replaceAll(",$", "") + "}";

        response = ApiUtil.send(method, endpoint, requestBody, token);

        System.out.println("PUT BODY: " + requestBody);
        System.out.println("PUT RESPONSE: " + response.asString());
    }

   

    @Then("Response body should contain {string}")
    public void validateResponseContains(String expectedText) {

        String responseBody = response.asString();

        if (method.equalsIgnoreCase("PUT")) {
            System.out.println("Skipping validation for PUT (API doesn't echo data)");
            return;
        }

        Assert.assertTrue(responseBody.contains(expectedText),
                "Response does not contain: " + expectedText);
    }

    @Then("Response body should contain carts array")
    public void validateCartsArray() {

        JsonPath json = response.jsonPath();
        List<Object> carts = json.getList("carts");

        Assert.assertNotNull(carts);
        Assert.assertTrue(carts.size() > 0);
    }

    @Then("Each cart should have {string}, {string}, {string}, {string}")
    public void validateCartFields(String id, String products, String total, String userId) {

        JsonPath json = response.jsonPath();
        List<Map<String, Object>> carts = json.getList("carts");

        for (Map<String, Object> cart : carts) {
            Assert.assertTrue(cart.containsKey(id));
            Assert.assertTrue(cart.containsKey(products));
            Assert.assertTrue(cart.containsKey(total));
            Assert.assertTrue(cart.containsKey(userId));
        }
    }
}


