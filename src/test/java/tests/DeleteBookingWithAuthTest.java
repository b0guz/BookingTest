package tests;

import helpers.Constructor;
import helpers.PrepareData;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static constants.Constants.*;

public class DeleteBookingWithAuthTest {

    private RequestSpecification request;
    private final static String token = TOKEN;
    private String id;

    @BeforeEach
    public void setUp() {
        request = new Constructor().newRequest(BASEURL, BOOK);
        id = PrepareData.createBookingAndPassId(BASEURL, BOOK);
    }

    @Test
    public void deleteBookingWithAuthTest() {

        Response response = request
                .header("Authorization", "Basic " + token)
                .when()
                .delete(id)
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_CREATED, response.statusCode());
        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, PrepareData.returnResponseOfGetBooking(BASEURL, BOOK, id).statusCode());
    }

    @Test
    public void deleteSameBookingAgainWithAuthTest() {

        String id = PrepareData.deleteBookingAndPassId(BASEURL, BOOK);

        Response response = request
                .header("Authorization", "Basic " + token)
                .when()
                .delete(id)
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode());
    }

    @Test
    public void deleteBookingNoIdWithAuthTest() {

        Response response = request
                .header("Authorization", "Basic " + token)
                .when()
                .delete()
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, response.statusCode());
    }

    @Test
    public void deleteBookingNoAuthTest() {

        Response response = request
                .header("Authorization", "Basic ")
                .when()
                .delete(id)
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_FORBIDDEN, response.statusCode());
    }

}
