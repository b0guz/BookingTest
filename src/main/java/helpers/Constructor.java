package helpers;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static specs.BookingSpecifications.*;

public class Constructor {

    private RequestSpecification request;

    public RequestSpecification newRequest(String uri, String path) {
        setSpecification(requestSpec(uri, path), responseSpec());
        return request = RestAssured.given();
    }

}
