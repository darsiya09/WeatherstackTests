package ru.amazin.data.currentWeather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Current {
    private String observation_time;
    private Integer temperature;
    private Integer weather_code;
    private ArrayList<String> weather_icons;
    private ArrayList<String> weather_descriptions;
    private Integer wind_speed;
    private Integer wind_degree;
    private String wind_dir;
    private Integer pressure;
    private Integer precip;
    private Integer humidity;
    private Integer cloudcover;
    private Integer feelslike;
    private Integer uv_index;
    private Integer visibility;
    private String is_day;
}