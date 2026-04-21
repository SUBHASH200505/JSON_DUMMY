package Hooks;

import io.cucumber.java.Before;
import io.restassured.RestAssured;
import Utils.*;

public class Hooks {

    @Before
    public void setup() {
        RestAssured.baseURI = ConfiReader.get("base.url");
    }
}