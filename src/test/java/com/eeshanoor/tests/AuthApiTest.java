package com.eeshanoor.tests;

import com.eeshanoor.base.BaseApiTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Epic("ReqRes API")
@Feature("Authentication API")
public class AuthApiTest extends BaseApiTest {

    @Test(groups = {"smoke", "regression"}, description = "Valid login returns auth token")
    @Story("Login")
    @Severity(SeverityLevel.BLOCKER)
    public void testSuccessfulLogin() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "eve.holt@reqres.in");
        credentials.put("password", "cityslicka");

        Response response = given().spec(reqResSpec)
            .body(credentials)
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .body("token", notNullValue())
            .extract().response();

        String token = response.jsonPath().getString("token");
        Assert.assertFalse(token.isEmpty(), "Token should not be empty");
    }

    @Test(groups = {"smoke", "regression"}, description = "Login without password returns 400 with error")
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginMissingPassword() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "peter@klaven");

        given().spec(reqResSpec)
            .body(credentials)
        .when()
            .post("/login")
        .then()
            .statusCode(400)
            .body("error", equalTo("Missing password"));
    }

    @Test(groups = {"regression"}, description = "Register with valid data returns token and id")
    @Story("Register")
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessfulRegister() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "pistol");

        given().spec(reqResSpec)
            .body(body)
        .when()
            .post("/register")
        .then()
            .statusCode(200)
            .body("id", notNullValue())
            .body("token", notNullValue());
    }

    @Test(groups = {"regression"}, description = "Register without password returns 400")
    @Story("Register")
    @Severity(SeverityLevel.NORMAL)
    public void testRegisterMissingPassword() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "sydney@fife");

        given().spec(reqResSpec)
            .body(body)
        .when()
            .post("/register")
        .then()
            .statusCode(400)
            .body("error", equalTo("Missing password"));
    }
}