package tests;

import helpers.Constructor;
import helpers.PrepareData;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static constants.Constants.BASEURL;
import static constants.Constants.BOOK;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class PartialUpdateWithTokenTest {

    private RequestSpecification request;
    private final String firstname = "Mickey";
    private String token;
    private String id;

    @BeforeEach
    public void setUp() {
        request = new Constructor().newRequest(BASEURL, BOOK);
        request.header("Accept", JSON);

        token = PrepareData.createAndPassToken(BASEURL, BOOK);
        id = PrepareData.createBookingAndPassId(BASEURL, BOOK);
    }

    @Test
    public void partialUpdateNameWithTokenTest() {

        String lastname = "Mouse";

        Map<String, Object> booking = new HashMap<>();
        booking.put("firstname", firstname);
        booking.put("lastname", lastname);

        Response response = request
                .header("Cookie", "token=" + token)
                .body(booking)
                .when()
                .patch(id)
                .then()
                .body(matchesJsonSchemaInClasspath("bookingSchema.json"))
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
        Assertions.assertEquals(firstname, response.path("firstname"));
        Assertions.assertEquals(lastname, response.path("lastname"));
    }

    @Test
    public void partialUpdateSumAndDatesWithTokenTest() {

        int totalPrice = 777;
        String checkIn = "2023-04-11";
        String checkOut = "2023-04-21";

        Map<String, Object> booking = new HashMap<>();
        booking.put("totalprice", totalPrice);
        booking.put("bookingdates.checkin", checkIn);
        booking.put("bookingdates.checkout", checkOut);

        Response response = request
                .header("Cookie", "token=" + token)
                .body(booking)
                .when()
                .patch(id)
                .then()
                .body(matchesJsonSchemaInClasspath("bookingSchema.json"))
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
        Assertions.assertEquals(totalPrice, (Integer) response.path("totalprice"));
        Assertions.assertEquals(checkIn, response.path("bookingdates.checkin"));
        Assertions.assertEquals(checkOut, response.path("bookingdates.checkout"));
    }

    @Test
    public void partialUpdateDepositAndAdditionalNotesWithTokenTest() {

        Boolean depositPaid = false;
        String additionalNeeds = "Drink";

        Map<String, Object> booking = new HashMap<>();
        booking.put("depositpaid", depositPaid);
        booking.put("additionalneeds", additionalNeeds);

        Response response = request
                .header("Cookie", "token=" + token)
                .body(booking)
                .when()
                .patch(id)
                .then()
                .body(matchesJsonSchemaInClasspath("bookingSchema.json"))
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
        Assertions.assertEquals(depositPaid, response.path("depositpaid"));
        Assertions.assertEquals(additionalNeeds, response.path("additionalneeds"));
    }

    @Test
    public void partialUpdateIdNotExistWithTokenTest() {

        String id = PrepareData.deleteBookingAndPassId(BASEURL, BOOK);

        Map<String, Object> booking = new HashMap<>();
        booking.put("firstname", firstname);

        Response response = request
                .header("Cookie", "token=" + token)
                .body(booking)
                .when()
                .patch(id)
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode());
    }

    @Test
    public void partialUpdateNoIdWithTokenTest() {

        Map<String, Object> booking = new HashMap<>();
        booking.put("firstname", firstname);

        Response response = request
                .header("Cookie", "token=" + token)
                .body(booking)
                .when()
                .patch()
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, response.statusCode());
    }

    @Test
    public void partialUpdateNoTokenTest() {

        Map<String, Object> booking = new HashMap<>();
        booking.put("firstname", firstname);

        Response response = request
                .header("Cookie", "token=" )
                .body(booking)
                .when()
                .patch(id)
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_FORBIDDEN, response.statusCode());
    }

}
