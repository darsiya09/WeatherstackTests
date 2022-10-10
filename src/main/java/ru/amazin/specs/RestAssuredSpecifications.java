package ru.amazin.specs;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import ru.amazin.helpers.PropertiesReader;

public class RestAssuredSpecifications {

    private static final RestAssuredSpecifications instance = new RestAssuredSpecifications();

    private final PropertiesReader propertyReader = PropertiesReader.getInstance();

    public static RestAssuredSpecifications getInstance() {
        return instance;
    }

    private RestAssuredSpecifications() {
    }

    public RequestSpecification getReqSpec() {

        return new RequestSpecBuilder()
                .log(LogDetail.BODY)
                .setBaseUri(propertyReader.getProperty("url"))
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }

    public ResponseSpecification getResSpec(int expectedStatusCode) {
        return new ResponseSpecBuilder()
                .log(LogDetail.BODY)
                .expectStatusCode(expectedStatusCode)
                .build();
    }
}
