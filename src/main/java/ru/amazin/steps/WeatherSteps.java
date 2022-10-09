package ru.amazin.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.log4j.Log4j2;
import ru.amazin.data.currentWeather.CurrentWeatherPojo;
import ru.amazin.data.error.ErrorPojo;
import ru.amazin.services.WeatherService;
import ru.amazin.services.WeatherServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static ru.amazin.context.RunContext.RUN_CONTEXT;

@Log4j2
public class WeatherSteps {

    public static WeatherService service = new WeatherServiceImpl();

    @Given("generate the expected data for {string}")
    public void generateTheExpectedDataFor(String cityName) {
        CurrentWeatherPojo expectedWeather = service.generateExpectedWeather(cityName);
        RUN_CONTEXT.put("expectedCurrentWeather", expectedWeather);
    }

    @When("get data about the current weather in {string} from the Weatherstack")
    public void getDataAboutTheCurrentWeather(String cityName) {
        CurrentWeatherPojo currentWeather = service.getCurrentWeather(cityName);
        RUN_CONTEXT.put("actualCurrentWeather", currentWeather);
    }

    @Then("compare the data received from the service with the expected data")
    public void compareReceivedDataWithExpected() {
        CurrentWeatherPojo expectedCurrentWeather =
                RUN_CONTEXT.get("expectedCurrentWeather", CurrentWeatherPojo.class);
        CurrentWeatherPojo actualCurrentWeather =
                RUN_CONTEXT.get("actualCurrentWeather", CurrentWeatherPojo.class);
        service.logTheDifference(expectedCurrentWeather, actualCurrentWeather);

    }

    @When("Weatherstack API called without an access key")
    public void callApiWithoutAccessKey() {
        ErrorPojo errorPojo = service.getCurrentWeatherWithoutAccessKey();
        RUN_CONTEXT.put("errorPojo", errorPojo);
    }

    @When("Weatherstack API called with incorrect path")
    public void callApiWithIncorrectPath(){
        ErrorPojo errorPojo = service.getCurrentWeatherForWrongPathParam();
        RUN_CONTEXT.put("errorPojo", errorPojo);
    }


    @When("Weatherstack API called without query parameter")
    public void callApiWithoutQueryParam() {
        ErrorPojo errorPojo = service.getCurrentWeatherWithoutQueryParam();
        RUN_CONTEXT.put("errorPojo", errorPojo);
    }

    @When("Weatherstack API called for wrong city")
    public void callApiForWrongCity() {
        ErrorPojo errorPojo = service.getCurrentWeatherForWrongCity();
        RUN_CONTEXT.put("errorPojo", errorPojo);
    }

    @Then("received and validated error {int}")
    public void validateError(int errorCode) {
        ErrorPojo errorPojo = RUN_CONTEXT.get("errorPojo", ErrorPojo.class);
        String type = null;
        String info = null;
        switch (errorCode) {
            case 101: {
                type = "missing_access_key";
                info = "You have not supplied an API Access Key. [Required format: access_key=YOUR_ACCESS_KEY]";
                break;
            }
            case 103: {
                type = "invalid_api_function";
                info = "This API Function does not exist.";
                break;
            }
            case 601: {
                type = "missing_query";
                info = "Please specify a valid location identifier using the query parameter.";
                break;
            }
            case 615: {
                type = "request_failed";
                info = "Your API request failed. Please try again or contact support.";
                break;
            }
            default:
                throw new RuntimeException("Invalid error code passed: " + errorCode);
        }
        assertFalse(errorPojo.getSuccess());
        assertEquals(errorPojo.getError().getCode(), errorCode);
        assertEquals(errorPojo.getError().getType(), type);
        assertEquals(errorPojo.getError().getInfo(), info);
    }
}