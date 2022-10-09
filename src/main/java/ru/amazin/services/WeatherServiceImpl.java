package ru.amazin.services;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import ru.amazin.data.currentWeather.CurrentWeatherPojo;
import ru.amazin.data.error.ErrorPojo;
import ru.amazin.helpers.Mapper;
import ru.amazin.helpers.PropertiesReader;
import ru.amazin.specs.RestAssuredSpecifications;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@Log4j2
public class WeatherServiceImpl implements WeatherService {

    private final RestAssuredSpecifications specifications = RestAssuredSpecifications.getInstance();
    private final PropertiesReader propertyReader = PropertiesReader.getInstance();

    @Override
    public CurrentWeatherPojo getCurrentWeather(String cityName) {
        Map<String, Object> params = getParams();
        params.put("query", cityName);
        ValidatableResponse response = callCurrentWeatherMethod(params, null);
        return getPojoFromValidateResponse(response, CurrentWeatherPojo.class);
    }

    @Override
    public ErrorPojo getCurrentWeatherForWrongCity() {
        Map<String, Object> params = getParams();
        params.put("query", "LostCity");
        ValidatableResponse response = callCurrentWeatherMethod(params, null);
        return getPojoFromValidateResponse(response, ErrorPojo.class);
    }

    @Override
    public ErrorPojo getCurrentWeatherForWrongPathParam() {
        ValidatableResponse response = callCurrentWeatherMethod(getParams(), "wrong_path_param");
        return getPojoFromValidateResponse(response, ErrorPojo.class);
    }

    @Override
    public ErrorPojo getCurrentWeatherWithoutAccessKey() {
        ValidatableResponse response = callCurrentWeatherMethod(new HashMap<>(), null);
        return getPojoFromValidateResponse(response, ErrorPojo.class);
    }

    @Override
    public ErrorPojo getCurrentWeatherWithoutQueryParam() {
        Map<String, Object> params = getParams();
        params.remove("query");
        ValidatableResponse response = callCurrentWeatherMethod(params, null);
        return getPojoFromValidateResponse(response, ErrorPojo.class);
    }

    @Override
    public CurrentWeatherPojo generateExpectedWeather(String cityName) {
        return Mapper.getPojoFromJson(CurrentWeatherPojo.class, cityName);
    }

    @Override
    public void logTheDifference(CurrentWeatherPojo expected, CurrentWeatherPojo actual) {
        checkCompliance(expected.getRequest(), actual.getRequest());
        checkCompliance(expected.getLocation(), actual.getLocation());
        checkCompliance(expected.getCurrent(), actual.getCurrent());
    }

    private <T> void checkCompliance(T expected, T actual) {
        List<String> objectFieldNames = Arrays.stream(expected.getClass().getDeclaredFields())
                .map(Field::getName).collect(Collectors.toList());
        objectFieldNames.forEach(field -> {
            try {
                Field expectedField = expected.getClass().getDeclaredField(field);
                expectedField.setAccessible(true);
                Field actualField = actual.getClass().getDeclaredField(field);
                actualField.setAccessible(true);
                Object expectedValue = expectedField.get(expected);
                Object actualValue = actualField.get(actual);
                if (!expectedValue.equals(actualValue))
                    log.error("Unexpected value in field \"{}\"; Expected: \"{}\"; Actual: \"{}",
                            field, expectedValue, actualValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private ValidatableResponse callCurrentWeatherMethod(Map<String, Object> params, String pathParam) {
        RequestSpecification spec = given()
                .spec(specifications.getReqSpec())
                .when()
                .queryParams(params);
        if (pathParam == null) spec.pathParam("base", "current");
        else spec.pathParam("base", pathParam);

        return spec.get("/{base}")
                .then()
                .assertThat()
                .spec(specifications.getResSpec(200));
    }

    private <T> T getPojoFromValidateResponse(ValidatableResponse response, Class<T> tClass) {
        T pojo = null;
        try {
            pojo = response.extract().as(tClass);
        } catch (Exception ex) {
            log.error("Current weather request exception" + Arrays.toString(ex.getStackTrace()));
        }
        return pojo;
    }

    private Map<String, Object> getParams() {
        return new HashMap<String, Object>() {{
            put("access_key", propertyReader.getProperty("access_key"));
            put("query", "Moscow");
        }};
    }
}