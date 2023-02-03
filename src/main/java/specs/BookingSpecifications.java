package specs;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

public class BookingSpecifications {

    public static RequestSpecification requestSpec(String url, String path) {
        return new RequestSpecBuilder()
                .setBaseUri(url)
                .setBasePath(path)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification responseSpec() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification responseSpecCreated() {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_CREATED)
                .build();
    }

    public static void setSpecification(RequestSpecification request, ResponseSpecification response) {
        RestAssured.requestSpecification = request;
    }

}
