package tests;

import helpers.Constructor;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.PojoBookingIds;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.List;
import java.util.Random;

import static constants.Constants.BASEURL;
import static constants.Constants.BOOK;
import static helpers.PrepareData.generateNBookings;
import static helpers.PrepareData.returnResponseOfGetBooking;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BookingIdsFilterTest {

    private RequestSpecification request;
    static int usersCount = new Random().nextInt(3,10);

    @BeforeAll
    public static void generateBookings() {
        generateNBookings(BASEURL, BOOK, usersCount);
    }

    @BeforeEach
    public void setUp() {
        request = new Constructor().newRequest(BASEURL, BOOK);
    }

    @Test
    public void getBookingIdsByNameTest() {

        File bookingUserData = new File("src/test/resources/bookingUserData.json");
        JsonPath bookingData = new JsonPath(bookingUserData);
        String firstName = bookingData.getString("firstname");

        Response response = request
                .param("firstname", firstName)
                .get()
                .then()
                .body(matchesJsonSchemaInClasspath("bookingIdsSchema.json"))
                .extract().response();

        List<PojoBookingIds> listOfAllBookings = response.body().jsonPath().getList("", PojoBookingIds.class);
        int randomBooking = new Random().nextInt(listOfAllBookings.size());
        String BookId = listOfAllBookings.get(randomBooking).getBookingid().toString();

        Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
        Assertions.assertTrue(usersCount <= listOfAllBookings.size());
        Assertions.assertEquals(firstName, returnResponseOfGetBooking(BASEURL, BOOK, BookId).path("firstname"));
    }

}
