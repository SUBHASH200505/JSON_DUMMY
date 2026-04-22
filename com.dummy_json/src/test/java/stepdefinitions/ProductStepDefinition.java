package stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;

<<<<<<< HEAD
import utils.*;
=======
import Utils.*;
>>>>>>> product

import java.util.List;
import java.util.Map;

import org.testng.Assert;

public class ProductStepDefinition {

    Map<String, String> data;
    Response response;

    String method;
    String endpoint;

<<<<<<< HEAD
    // =========================
    // EXCEL DRIVEN
    // =========================

    @Given("I read products test data {string}")
    public void readData(String testCaseID) {
=======
    // =====================================
    // EXCEL DRIVEN → POST PRODUCT
    // =====================================

    @Given("I read products test data {string}")
    public void readProductsData(String testCaseID) {
>>>>>>> product

        data = ExcelUtil.getData(testCaseID);

        if (data == null || data.isEmpty()) {
<<<<<<< HEAD
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

=======
            throw new RuntimeException("No data found for: " + testCaseID);
        }

        System.out.println("TEST DATA: " + data);
    }

    @And("I validate products precondition")
    public void validateProductsPrecondition() {
        System.out.println("Products precondition validated");
    }

    @When("I perform products API request")
    public void performProductsApiRequest() {

        method = data.get("method");
        String endpointPath = data.get("endpoint");
        String requestBody = data.get("testdata");

        endpoint = ConfiReader.get("base.url") + endpointPath;

        response = ApiUtil.send(method, endpoint, requestBody, null);

        System.out.println("METHOD: " + method);
        System.out.println("ENDPOINT: " + endpoint);
        System.out.println("BODY: " + requestBody);
>>>>>>> product
        System.out.println("RESPONSE: " + response.asString());
    }

    @Then("I validate products expected result")
<<<<<<< HEAD
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
=======
    public void validateProductsExpectedResult() {

        int expectedStatus = Integer.parseInt(data.get("expectedstatus"));
        int actualStatus = response.getStatusCode();

        Assert.assertEquals(
                actualStatus,
                expectedStatus,
                "Status code mismatch"
        );

        System.out.println("✔ Product Excel Driven Test Passed");
    }

    // =====================================
    // INLINE REQUESTS → GET / DELETE / PUT / PATCH
    // =====================================

    @Given("I set products request {string} {string}")
    public void iSetProductsRequest(String reqMethod, String reqEndpoint) {

        method = reqMethod;
        endpoint = ConfiReader.get("base.url") + reqEndpoint;

        System.out.println("METHOD: " + method);
        System.out.println("ENDPOINT: " + endpoint);
    }

    @When("I send products request")
    public void iSendProductsRequest() {

        response = ApiUtil.send(method, endpoint, null, null);

        System.out.println("RESPONSE: " + response.asString());
    }

    @Then("I validate products status {string}")
    public void iValidateProductsStatus(String expectedStatus) {

        int expected = Integer.parseInt(expectedStatus);
        int actual = response.getStatusCode();

        Assert.assertEquals(
                actual,
                expected,
                "Products status validation failed"
        );

        System.out.println("✔ Status Validation Passed");
    }

    // =====================================
    // RESPONSE STRUCTURE VALIDATION
    // =====================================

    @Then("Response body should contain products array")
    public void responseBodyShouldContainProductsArray() {

        JsonPath json = response.jsonPath();
        List<?> products = json.getList("products");

        Assert.assertNotNull(products, "Products array not found");
        Assert.assertTrue(products.size() > 0, "Products array is empty");

        System.out.println("✔ Products array validated");
    }

    @And("Each product should have {string}, {string}, {string}, {string}")
    public void eachProductShouldHaveFields(
            String field1,
            String field2,
            String field3,
            String field4) {

        JsonPath json = response.jsonPath();

        Assert.assertNotNull(json.get("products[0]." + field1));
        Assert.assertNotNull(json.get("products[0]." + field2));
        Assert.assertNotNull(json.get("products[0]." + field3));
        Assert.assertNotNull(json.get("products[0]." + field4));

        System.out.println("✔ Product fields validated");
    }

    // =====================================
    // PUT REQUEST WITH DATA TABLE
    // =====================================

    @When("I send PUT request with body:")
    public void iSendPutRequestWithBody(io.cucumber.datatable.DataTable table) {

        Map<String, String> requestBody =
                table.asMaps(String.class, String.class).get(0);

        String jsonBody = "";

        // title update
        if (requestBody.containsKey("title")) {

            jsonBody =
                    "{ \"title\": \"" + requestBody.get("title") + "\" }";
        }

        // body/postId/userId (for generic support)
        else if (requestBody.containsKey("body")) {

            jsonBody =
                    "{ "
                            + "\"body\": \"" + requestBody.get("body") + "\", "
                            + "\"postId\": " + requestBody.get("postId") + ", "
                            + "\"userId\": " + requestBody.get("userId")
                            + " }";
        }

        // invalid price validation
        else if (requestBody.containsKey("price")) {

            jsonBody =
                    "{ \"price\": \"" + requestBody.get("price") + "\" }";
        }

        response = ApiUtil.send(
                "PUT",
                endpoint,
                jsonBody,
                null
        );

        System.out.println("REQUEST BODY: " + jsonBody);
        System.out.println("PUT RESPONSE: " + response.asString());
    }

    // =====================================
    // PATCH REQUEST
    // =====================================

    @When("I send PATCH request with body {string}")
    public void iSendPatchRequestWithBody(String updatedText) {

        String requestBody =
                "{ \"title\": \"" + updatedText + "\" }";

        response = ApiUtil.send(
                "PATCH",
                endpoint,
                requestBody,
                null
        );

        System.out.println("PATCH RESPONSE: " + response.asString());
    }

    // =====================================
    // COMMON RESPONSE BODY VALIDATION
    // =====================================

    @And("Response body should contain {string}")
    public void responseBodyShouldContain(String expectedText) {

        String responseBody = response.asString();

        Assert.assertTrue(
                responseBody.contains(expectedText),
                "Response body does not contain: " + expectedText
        );

        System.out.println("✔ Response contains: " + expectedText);
>>>>>>> product
    }
}