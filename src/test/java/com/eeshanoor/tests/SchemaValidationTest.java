package com.eeshanoor.tests;

import com.eeshanoor.base.BaseApiTest;
import com.eeshanoor.factory.UserFactory;
import com.eeshanoor.utils.SchemaValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Epic("ReqRes API")
@Feature("Schema Validation")
public class SchemaValidationTest extends BaseApiTest {

    @Test(description = "GET single user response matches JSON schema")
    @Severity(SeverityLevel.CRITICAL)
    public void testSingleUserSchema() {
        Response response = given().spec(reqResSpec)
            .when().get("/users/1")
            .then().statusCode(200).extract().response();
        SchemaValidator.validate(response, "user-response.json");
    }

    @Test(description = "GET users list response matches JSON schema")
    @Severity(SeverityLevel.CRITICAL)
    public void testUsersListSchema() {
        Response response = given().spec(reqResSpec)
            .when().get("/users")
            .then().statusCode(200).extract().response();
        SchemaValidator.validate(response, "users-list-response.json");
    }

    @Test(description = "Create user with factory-generated data")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserWithFactory() {
        var user = UserFactory.builder()
            .name("Eesha Noor")
            .job("Senior SDET")
            .build();

        given().spec(reqResSpec)
            .body(user)
            .when().post("/users")
            .then()
            .statusCode(201)
            .extract().response();
    }
}