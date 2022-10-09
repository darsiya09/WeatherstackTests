package ru.amazin.data.currentWeather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentWeatherPojo {
    private Request request;
    private Location location;
    private Current current;
}
