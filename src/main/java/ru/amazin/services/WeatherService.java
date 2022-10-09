package ru.amazin.services;

import ru.amazin.data.currentWeather.CurrentWeatherPojo;
import ru.amazin.data.error.ErrorPojo;

public interface WeatherService {
    CurrentWeatherPojo getCurrentWeather(String cityName);

    CurrentWeatherPojo generateExpectedWeather(String cityName);

    ErrorPojo getCurrentWeatherForWrongCity();

    ErrorPojo getCurrentWeatherWithoutQueryParam();

    ErrorPojo getCurrentWeatherForWrongPathParam();

    ErrorPojo getCurrentWeatherWithoutAccessKey();

    void logTheDifference(CurrentWeatherPojo expected, CurrentWeatherPojo actual);
}
