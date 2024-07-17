package com.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueWeatherResponse {

    private String name;
    private String city;
    private double temp;
    private double wind_speed;
    private int pressure;
    private int humidity;
}
