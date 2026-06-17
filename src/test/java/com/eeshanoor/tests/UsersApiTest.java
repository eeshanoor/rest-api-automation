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
@Feature("Users API")
public class UsersApiTest extends BaseApiTest {

    @Test(groups = {"smoke", "regression"}, description = "GET list of users returns 200 with data array")
    @Story("Get Users")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetUsersList() {
        given().spec(reqResSpec)
            .queryParam("page", 1)
        .when()
            .get("/users")
        .then()
            .spec(responseSpec200)
            .body("page", equalTo(1))
            .body("data", not(empty()))
            .body("data[0].id", notNullValue())
            .body("data[0].email", containsString("@"));
    }

    @Test(groups = {"smoke", "regression"}, description = "GET single user returns correct user data")
    @Story("Get Users")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetSingleUser() {
        given().spec(reqResSpec)
        .when()
            .get("/users/2")
        .then()
            .spec(responseSpec200)
            .body("data.id", equalTo(2))
            .body("data.email", equalTo("janet.weaver@reqres.in"))
            .body("data.first_name", equalTo("Janet"));
    }

    @Test(groups = {"regression"}, description = "GET non-existent user returns 404")
    @Story("Get Users")
    @Severity(SeverityLevel.NORMAL)
    public void testGetUserNotFound() {
        given().spec(reqResSpec)
        .when()
            .get("/users/999")
        .then()
            .statusCode(404);
    }

    @Test(groups = {"smoke", "regression"}, description = "POST create user returns 201 with id and createdAt")
    @Story("Create User")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateUser() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "Eesha Noor");
        body.put("job", "SDET");

        Response response = given().spec(reqResSpec)
            .body(body)
        .when()
            .post("/users")
        .then()
            .spec(responseSpec201)
            .body("name", equalTo("Eesha Noor"))
            .body("job", equalTo("SDET"))
            .body("id", notNullValue())
            .body("createdAt", notNullValue())
            .extract().response();

        String userId = response.jsonPath().getString("id");
        Assert.assertNotNull(userId, "User ID should not be null");
    }

    @Test(groups = {"regression"}, description = "PUT update user returns 200 with updatedAt timestamp")
    @Story("Update User")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateUser() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "Eesha Noor");
        body.put("job", "Senior SDET");

        given().spec(reqResSpec)
            .body(body)
        .when()
            .put("/users/2")
        .then()
            .statusCode(200)
            .body("job", equalTo("Senior SDET"))
            .body("updatedAt", notNullValue());
    }

    @Test(groups = {"regression"}, description = "PATCH partial update returns 200")
    @Story("Update User")
    @Severity(SeverityLevel.NORMAL)
    public void testPatchUser() {
        Map<String, String> body = new HashMap<>();
        body.put("job", "Lead Automation Engineer");

        given().spec(reqResSpec)
            .body(body)
        .when()
            .patch("/users/2")
        .then()
            .statusCode(200)
            .body("job", equalTo("Lead Automation Engineer"));
    }

    @Test(groups = {"smoke", "regression"}, description = "DELETE user returns 204")
    @Story("Delete User")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteUser() {
        given().spec(reqResSpec)
        .when()
            .delete("/users/2")
        .then()
            .statusCode(204);
    }

    @Test(groups = {"regression"}, description = "GET users page 2 returns different data than page 1")
    @Story("Get Users")
    @Severity(SeverityLevel.MINOR)
    public void testGetUsersPage2() {
        Response page1 = given().spec(reqResSpec).queryParam("page", 1).when().get("/users").then().extract().response();
        Response page2 = given().spec(reqResSpec).queryParam("page", 2).when().get("/users").then().extract().response();

        int page1FirstId = page1.jsonPath().getInt("data[0].id");
        int page2FirstId = page2.jsonPath().getInt("data[0].id");
        Assert.assertNotEquals(page1FirstId, page2FirstId, "Pages should return different users");
    }

    @Test(groups = {"regression"}, description = "Response time is under 3 seconds")
    @Story("Performance")
    @Severity(SeverityLevel.NORMAL)
    public void testResponseTime() {
        given().spec(reqResSpec)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .time(lessThan(3000L));
    }
}