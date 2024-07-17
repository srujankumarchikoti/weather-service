package com.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameWeatherResponse {
    private List<String> teams;
    private LocalDate gameDate;
    private double temp;
    private double wind_speed;
    private int pressure;
    private int humidity;
}

