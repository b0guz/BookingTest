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

public class DeleteBookingTest {

    private RequestSpecification request;

    @BeforeEach
    public void setUp() {
        request = new Constructor().newRequest(BASEURL, BOOK);
    }

    @Test
    public void deleteBookingTest() {

        String token = PrepareData.createToken(BASEURL, AUTH);
        String id = PrepareData.createBooking(BASEURL, BOOK);

        Response response = request
                .header("Cookie", "token=" + token)
                .when()
                .delete(id)
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_CREATED, response.statusCode());
        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, PrepareData.returnStatusCodeOfGetBooking(BASEURL, BOOK, id));
    }


    @Test
    public void deleteSameBookingAgainTest() {

        String token = PrepareData.createToken(BASEURL, AUTH);
        String id = PrepareData.deleteBookingById(BASEURL, BOOK);

        Response response = request
                .header("Cookie", "token=" + token)
                .when()
                .delete(id)
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode());
    }

    @Test
    public void deleteBookingNoIdTest() {

        String token = PrepareData.createToken(BASEURL, BOOK);

        Response response = request
                .header("Cookie", "token=" + token)
                .when()
                .delete()
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, response.statusCode());
    }

    @Test
    public void deleteBookingNoTokenTest() {

        String id = PrepareData.createBooking(BASEURL, BOOK);

        Response response = request
                .header("Cookie", "token=")
                .when()
                .delete(id)
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_FORBIDDEN, response.statusCode());
    }

}
