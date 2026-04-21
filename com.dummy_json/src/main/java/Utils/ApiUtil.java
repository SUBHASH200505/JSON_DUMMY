package Utils;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ApiUtil {

    public static Response send(String method, String endpoint, String body, String token) {

        method = method.trim().toUpperCase();

        RequestSpecification request = given()
                .header("Content-Type", "application/json");

        // Token (optional)
        if (token != null && !token.isEmpty()) {
            request.header("Authorization", "Bearer " + token);
        }

        // Body (optional)
        if (body != null && !body.trim().isEmpty()) {
            request.body(body);
        }

        switch (method) {

            case "GET":
                return request.get(endpoint);

            case "POST":
                return request.post(endpoint);

            case "PUT":
                return request.put(endpoint);

            case "PATCH":
                return request.patch(endpoint);

            case "DELETE":
                return request.delete(endpoint);

            default:
                throw new RuntimeException("Invalid HTTP method: " + method);
        }
    }
}