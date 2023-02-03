package tests;

import helpers.PrepareData;
import helpers.Constructor;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import models.PojoBookingIds;
import java.util.List;

import static constants.Constants.BASEURL;
import static constants.Constants.BOOK;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingIdsTest {

    private RequestSpecification request;

    @BeforeEach
    public void setUp() {
        request = new Constructor().newRequest(BASEURL, BOOK);
    }

    @Test
    public void allBookingIdsNotNullTest() {

        List<PojoBookingIds> listOfAllBookings = request
                .get()
                .then()
                .body(matchesJsonSchemaInClasspath("bookingIdsSchema.json"))
                .extract().body().jsonPath().getList("", PojoBookingIds.class);

        listOfAllBookings.forEach(x-> assertNotNull(x.getBookingid()));

    }

    @Test
    public void findCreatedBookingInAllBookingIdsTest() {

        int id = Integer.parseInt(PrepareData.createBooking(BASEURL, BOOK));

        List<PojoBookingIds> listOfAllBookings = request
                .get()
                .then()
                .body(matchesJsonSchemaInClasspath("bookingIdsSchema.json"))
                .extract().body().jsonPath().getList("", PojoBookingIds.class);

        Assertions.assertTrue(listOfAllBookings.stream().anyMatch(obj ->
                obj.getBookingid().equals(id)));
    }

    @Test
    public void notFoundDeletedBookingInAllBookingIdsTest() {

        String id = PrepareData.deleteBookingById(BASEURL, BOOK);

        List<PojoBookingIds> listOfAllBookings = request
                .get()
                .then()
                .body(matchesJsonSchemaInClasspath("bookingIdsSchema.json"))
                .extract().body().jsonPath().getList("", PojoBookingIds.class);

        Assertions.assertFalse(listOfAllBookings.stream().anyMatch(obj ->
                obj.getBookingid().equals(Integer.parseInt(id))));
    }
}
