package com.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponse {
    private double lat;
    private double lon;
    private String timezone;
    private int timezone_offset;
    private Weather current;
}
