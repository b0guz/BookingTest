package helpers;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BookingSpecifications.responseSpecCreated;

public class PrepareData {

    private static RequestSpecification request;

    public static String createBookingAndPassId(String uri, String path) {

        request = new Constructor().newRequest(uri, path);

        File bookingUserData = new File("src/test/resources/bookingUserData.json");

        String id = request
                .body(bookingUserData)
                .post()
                .then()
                .extract().body().path("bookingid").toString();

        return id;
    }

    public static void createBooking(String uri, String path) {

        request = new Constructor().newRequest(uri, path);

        File bookingUserData = new File("src/test/resources/bookingUserData.json");

        request
                .body(bookingUserData)
                .post();
    }

    public static String createAndPassToken(String uri, String path) {

        request = new Constructor().newRequest(uri, path);

        File authData = new File("src/test/resources/tokenUserData.json");

        String token = request
                .body(authData)
                .basePath("auth")
                .when()
                .post()
                .then()
                .assertThat().body("token", notNullValue())
                .extract().body().jsonPath().getString("token");

        return token;
    }

    public static Response returnResponseOfGetBooking(String uri, String path, String bookingId) {

        request = new Constructor().newRequest(uri, path);

        Response response = request
                .when()
                .get(bookingId)
                .then()
                .extract().response();

        return response;
    }

    public static String deleteBookingAndPassId(String uri, String path) {

        String token = createAndPassToken(uri, path);
        String id = createBookingAndPassId(uri, path);

        request = new Constructor().newRequest(uri, path);

                request
                .header("Cookie", "token=" + token)
                .response().spec(responseSpecCreated())
                .when()
                .delete(id)
                .then();

        return id;
    }

    public static void generateNBookings(String uri, String path, int testCount) {

        for (int i = 0; i < testCount; i++) {
            createBooking(uri, path);
        }
    }
}
