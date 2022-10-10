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
    private final RequestSpecBuilder requestSpecBuilder;
    private final ResponseSpecBuilder respSpecBuilder;

    public static RestAssuredSpecifications getInstance() {
        return instance;
    }

    private RestAssuredSpecifications() {
        this.requestSpecBuilder = new RequestSpecBuilder();
        this.respSpecBuilder = new ResponseSpecBuilder();
    }

    public RequestSpecification getReqSpec() {

        return requestSpecBuilder
                .log(LogDetail.BODY)
                .setBaseUri(propertyReader.getProperty("url"))
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }

    public ResponseSpecification getResSpec(int expectedStatusCode) {
        return respSpecBuilder
                .log(LogDetail.BODY)
                .expectStatusCode(expectedStatusCode)
                .build();
    }
}
