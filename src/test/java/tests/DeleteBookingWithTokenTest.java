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

public class DeleteBookingWithTokenTest {

    private RequestSpecification request;
    private String token;
    private String id;

    @BeforeEach
    public void setUp() {
        request = new Constructor().newRequest(BASEURL, BOOK);
        token = PrepareData.createAndPassToken(BASEURL, AUTH);
        id = PrepareData.createBookingAndPassId(BASEURL, BOOK);
    }

    @Test
    public void deleteBookingWithTokenTest() {

        Response response = request
                .header("Cookie", "token=" + token)
                .when()
                .delete(id)
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_CREATED, response.statusCode());
        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, PrepareData.returnResponseOfGetBooking(BASEURL, BOOK, id).statusCode());
    }


    @Test
    public void deleteSameBookingAgainTest() {

        String id = PrepareData.deleteBookingAndPassId(BASEURL, BOOK);

        Response response = request
                .header("Cookie", "token=" + token)
                .when()
                .delete(id)
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode());
    }

    @Test
    public void deleteBookingNoIdWithTokenTest() {

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

        Response response = request
                .header("Cookie", "token=")
                .when()
                .delete(id)
                .then()
                .extract().response();

        Assertions.assertEquals(HttpStatus.SC_FORBIDDEN, response.statusCode());
    }

}
