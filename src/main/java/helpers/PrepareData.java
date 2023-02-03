package helpers;

import io.restassured.specification.RequestSpecification;
import java.io.File;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BookingSpecifications.responseSpecCreated;
import static specs.BookingSpecifications.responseSpec;

public class PrepareData {

    private static RequestSpecification request;

    public static String createBooking(String uri, String path) {

        request = new Constructor().newRequest(uri, path);
        request.response().spec(responseSpec());

        File bookingUserData = new File("src/test/resources/bookingUserData.json");

        String id = request
                .body(bookingUserData)
                .post()
                .then()
                .extract().body().path("bookingid").toString();

        return id;
    }

    public static String createToken(String uri, String path) {

        request = new Constructor().newRequest(uri, path);
        request.response().spec(responseSpec());

        File prepareData = new File("src/test/resources/tokenUserData.json");

        String token = request
                .body(prepareData)
                .basePath("auth")
                .when()
                .post()
                .then()
                .assertThat().body("token", notNullValue())
                .extract().body().jsonPath().getString("token");

        return token;
    }

    public static int returnStatusCodeOfGetBooking(String uri, String path, String bookingId) {

        request = new Constructor().newRequest(uri, path);

        int statusCode = request
                .when()
                .get(bookingId)
                .then()
                .extract().statusCode();

        return statusCode;
    }

    public static String deleteBookingById(String uri, String path) {

        String token = createToken(uri, path);
        String id = createBooking(uri, path);

        request = new Constructor().newRequest(uri, path);

                request
                .header("Cookie", "token=" + token)
                .response().spec(responseSpecCreated())
                .when()
                .delete(id)
                .then();

        return id;
    }
}
