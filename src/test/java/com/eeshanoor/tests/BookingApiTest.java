package com.eeshanoor.tests;

import com.eeshanoor.base.BaseApiTest;
import com.eeshanoor.models.Booking;
import com.eeshanoor.models.Booking.BookingDates;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Epic("Restful Booker API")
@Feature("Booking Management")
public class BookingApiTest extends BaseApiTest {

    private String authToken;
    private int createdBookingId;

    @BeforeClass
    public void authenticate() {
        Map<String, String> creds = new HashMap<>();
        creds.put("username", "admin");
        creds.put("password", "password123");

        authToken = given().spec(bookerSpec)
            .body(creds)
        .when()
            .post("/auth")
        .then()
            .statusCode(200)
            .extract().jsonPath().getString("token");

        Assert.assertNotNull(authToken, "Auth token must be obtained before booking tests");
    }

    @Test(groups = {"smoke", "regression"}, priority = 1, description = "GET all bookings returns list")
    @Story("Get Bookings")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetAllBookings() {
        given().spec(bookerSpec)
        .when()
            .get("/booking")
        .then()
            .statusCode(200)
            .body("bookingid", not(empty()));
    }

    @Test(groups = {"smoke", "regression"}, priority = 2, description = "POST create booking returns booking ID")
    @Story("Create Booking")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateBooking() {
        Booking booking = new Booking(
            "Eesha", "Noor", 150, true,
            new BookingDates("2025-01-01", "2025-01-07"), "Breakfast"
        );

        Response response = given().spec(bookerSpec)
            .body(booking)
        .when()
            .post("/booking")
        .then()
            .statusCode(200)
            .body("bookingid", notNullValue())
            .body("booking.firstname", equalTo("Eesha"))
            .body("booking.lastname", equalTo("Noor"))
            .body("booking.totalprice", equalTo(150))
            .extract().response();

        createdBookingId = response.jsonPath().getInt("bookingid");
        Assert.assertTrue(createdBookingId > 0, "Booking ID should be positive");
    }

    @Test(groups = {"regression"}, priority = 3, description = "GET booking by ID returns correct booking")
    @Story("Get Bookings")
    @Severity(SeverityLevel.NORMAL)
    public void testGetBookingById() {
        given().spec(bookerSpec)
        .when()
            .get("/booking/" + createdBookingId)
        .then()
            .statusCode(200)
            .body("firstname", equalTo("Eesha"))
            .body("lastname", equalTo("Noor"));
    }

    @Test(groups = {"regression"}, priority = 4, description = "PUT update booking with auth token")
    @Story("Update Booking")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateBooking() {
        Booking updated = new Booking(
            "Eesha", "Noor Updated", 200, false,
            new BookingDates("2025-02-01", "2025-02-10"), "Lunch"
        );

        given().spec(bookerSpec)
            .header("Cookie", "token=" + authToken)
            .body(updated)
        .when()
            .put("/booking/" + createdBookingId)
        .then()
            .statusCode(200)
            .body("lastname", equalTo("Noor Updated"))
            .body("totalprice", equalTo(200));
    }

    @Test(groups = {"regression"}, priority = 5, description = "PATCH partial update booking")
    @Story("Update Booking")
    @Severity(SeverityLevel.NORMAL)
    public void testPartialUpdateBooking() {
        Map<String, Object> patch = new HashMap<>();
        patch.put("firstname", "Eesha Patched");
        patch.put("totalprice", 999);

        given().spec(bookerSpec)
            .header("Cookie", "token=" + authToken)
            .body(patch)
        .when()
            .patch("/booking/" + createdBookingId)
        .then()
            .statusCode(200)
            .body("firstname", equalTo("Eesha Patched"))
            .body("totalprice", equalTo(999));
    }

    @Test(groups = {"regression"}, priority = 6, description = "DELETE booking with auth token returns 201")
    @Story("Delete Booking")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteBooking() {
        given().spec(bookerSpec)
            .header("Cookie", "token=" + authToken)
        .when()
            .delete("/booking/" + createdBookingId)
        .then()
            .statusCode(201);
    }

    @Test(groups = {"regression"}, description = "GET deleted booking returns 404")
    @Story("Delete Booking")
    @Severity(SeverityLevel.NORMAL)
    public void testGetDeletedBookingReturns404() {
        given().spec(bookerSpec)
        .when()
            .get("/booking/" + createdBookingId)
        .then()
            .statusCode(404);
    }
}