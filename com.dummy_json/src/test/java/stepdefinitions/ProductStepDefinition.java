package stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;

import Utils.*;

import java.util.List;
import java.util.Map;

import org.testng.Assert;

public class ProductStepDefinition {

    Map<String, String> data;
    Response response;

    String method;
    String endpoint;

    // =========================
    // EXCEL DRIVEN
    // =========================

    @Given("I read products test data {string}")
    public void readData(String testCaseID) {

        data = ExcelUtil.getData(testCaseID);

        if (data == null || data.isEmpty()) {
            throw new RuntimeException("No data found for " + testCaseID);
        }

        System.out.println("DATA: " + data);
    }

    @And("I validate products precondition")
    public void validatePrecondition() {
        System.out.println("Precondition OK");
    }

    @When("I perform products API request")
    public void performRequest() {

        method = data.getOrDefault("method", "GET").trim();
        String endpointPath = data.getOrDefault("endpoint", "/products").trim();
        String body = data.get("testdata");

        endpoint = ConfiReader.get("base.url") + endpointPath;

        System.out.println("METHOD: " + method);
        System.out.println("ENDPOINT: " + endpoint);
        System.out.println("BODY: " + body);

        // ✅ FIXED HERE
        response = ApiUtil.send(method, endpoint, body, null);

        if (response == null) {
            throw new RuntimeException("API response is null");
        }

        System.out.println("RESPONSE: " + response.asString());
    }

    @Then("I validate products expected result")
    public void validateResult() {

        int expected = Integer.parseInt(data.getOrDefault("expectedstatus", "200"));
        int actual = response.getStatusCode();

        Assert.assertEquals(actual, expected,
                "FAILED → Expected: " + expected + " but got: " + actual);

        JsonPath json = response.jsonPath();

        String key = data.get("expectedkey");
        String value = data.get("expectedvalue");

        // ================= PRODUCT LIST =================
        try {
            List<?> productList = json.getList("products");

            if (productList != null) {

                System.out.println("Validating PRODUCT LIST...");

                if ("present".equalsIgnoreCase(value)) {
                    Assert.assertTrue(productList.size() > 0, "Product list is empty");
                }

                if (key != null && !key.isEmpty()) {
                    Object val = json.get("products[0]." + key);
                    Assert.assertNotNull(val, "Missing key: " + key);
                }

                return;
            }
        } catch (Exception ignored) {}

        // ================= CATEGORY LIST =================
        if (response.asString().trim().startsWith("[")) {

            System.out.println("Validating CATEGORY LIST...");

            List<?> categories = json.getList("$");
            Assert.assertTrue(categories.size() > 0, "Category list is empty");
            return;
        }

        // ================= SINGLE PRODUCT =================
        if (actual == 200 || actual == 201) {

            System.out.println("Validating PRODUCT OBJECT...");

            if (key != null && !key.isEmpty()) {

                Object actualValue = json.get(key);
                Assert.assertNotNull(actualValue, "Key not found: " + key);

                if (value != null && !value.isEmpty()) {

                    if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {

                        Assert.assertEquals(
                                Boolean.parseBoolean(value),
                                actualValue,
                                "Boolean mismatch"
                        );

                    } else {

                        Assert.assertEquals(
                                String.valueOf(actualValue),
                                value,
                                "Value mismatch"
                        );
                    }
                }
            }
        }

        System.out.println("✔ Product Excel Test Passed");
    }

    // =========================
    // INLINE (GET / DELETE)
    // =========================

    @Given("I set products request {string} {string}")
    public void setRequest(String m, String e) {

        method = m;
        endpoint = ConfiReader.get("base.url") + e;
    }

    @When("I send products request")
    public void sendRequest() {

        // ✅ FIXED HERE ALSO
        response = ApiUtil.send(method, endpoint, null, null);
    }

    @Then("I validate products status {string}")
    public void validateStatus(String status) {

        int expected = Integer.parseInt(status);
        int actual = response.getStatusCode();

        Assert.assertEquals(actual, expected);

        System.out.println("✔ Inline Product Test Passed");
    }
}