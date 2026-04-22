
package stepdefinitions;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;
import Utils.*;

import java.util.Map;
import java.util.List;

import org.testng.Assert;

public class ProductStepDefinition {

    Map<String, String> data;
    Response response;

    String method;
    String endpoint;

    // =========================================
    // EXCEL STEPS
    // =========================================

    @Given("I read products test data {string}")
    public void readProductsData(String testCaseID) {

        data = ExcelUtil.getData(testCaseID);

        if (data == null || data.isEmpty()) {
            throw new RuntimeException("No data found for: " + testCaseID);
        }
    }

    @And("I validate products precondition")
    public void validateProductsPrecondition() {
        System.out.println("Precondition OK");
    }

    @When("I perform products API request")
    public void performProductsRequest() {

        method = data.get("method");
        String endpointPath = data.get("endpoint");
        String body = data.get("testdata");

        endpoint = ConfiReader.get("base.url") + endpointPath;

        response = ApiUtil.send(method, endpoint, body, null);

        System.out.println("RESPONSE: " + response.asString());
    }

    @Then("I validate products expected result")
    public void validateProductsResult() {

        int expected = Integer.parseInt(data.get("expectedstatus"));
        int actual = response.getStatusCode();

        Assert.assertEquals(actual, expected);
    }

    // =========================================
    // INLINE REQUESTS
    // =========================================

    @Given("I set products request {string} {string}")
    public void setProductsRequest(String m, String e) {

        method = m;
        endpoint = ConfiReader.get("base.url") + e;
    }

    @When("I send products request")
    public void sendProductsRequest() {

        response = ApiUtil.send(method, endpoint, null, null);
    }

    @Then("I validate products status {string}")
    public void validateProductsStatus(String status) {

        int expected = Integer.parseInt(status);
        int actual = response.getStatusCode();

        Assert.assertEquals(actual, expected);
    }

    // =========================================
    // RESPONSE STRUCTURE
    // =========================================

    @Then("Response body should contain products array")
    public void validateProductsArray() {

        JsonPath json = response.jsonPath();
        List<?> products = json.getList("products");

        Assert.assertNotNull(products);
        Assert.assertTrue(products.size() > 0);
    }

    @Then("Each product should have {string}, {string}, {string}, {string}")
    public void validateProductFields(String f1, String f2, String f3, String f4) {

        JsonPath json = response.jsonPath();

        Assert.assertNotNull(json.get("products[0]." + f1));
        Assert.assertNotNull(json.get("products[0]." + f2));
        Assert.assertNotNull(json.get("products[0]." + f3));
        Assert.assertNotNull(json.get("products[0]." + f4));
    }

    // =========================================
    // PUT (FIXED JSON + MATCHED STEP)
    // =========================================

    @When("I send products PUT request with body:")
    public void sendProductsPutRequest(DataTable table) {

        Map<String, String> map = table.asMaps().get(0);

        String jsonBody = "{";

        if (map.containsKey("title")) {
            jsonBody += "\"title\":\"" + map.get("title") + "\",";
        }

        if (map.containsKey("price")) {
            jsonBody += "\"price\":" + map.get("price") + ",";
        }

        jsonBody = jsonBody.replaceAll(",$", "") + "}";

        response = ApiUtil.send("PUT", endpoint, jsonBody, null);

        System.out.println("PUT BODY: " + jsonBody);
        System.out.println("PUT RESPONSE: " + response.asString());
    }

    // =========================================
    // PATCH
    // =========================================

    @When("I send products PATCH request with body {string}")
    public void sendProductsPatchRequest(String text) {

        String requestBody = "{ \"title\": \"" + text + "\" }";

        response = ApiUtil.send("PATCH", endpoint, requestBody, null);

        System.out.println("PATCH RESPONSE: " + response.asString());
    }

    // =========================================
    // UNIQUE RESPONSE VALIDATION
    // =========================================

    @Then("Products response should contain {string}")
    public void validateProductsResponseContains(String text) {

        Assert.assertTrue(response.asString().contains(text),
                "Response does not contain: " + text);
    }
}