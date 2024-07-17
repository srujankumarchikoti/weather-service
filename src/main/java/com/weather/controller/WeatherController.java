package com.weather.controller;

import com.weather.model.GameWeatherResponse;
import com.weather.model.VenueWeatherResponse;
import com.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<VenueWeatherResponse> getVenueWeather(@PathVariable int venueId) {
        VenueWeatherResponse response = weatherService.getWeatherForVenue(venueId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/game")
    public ResponseEntity<GameWeatherResponse> getGameWeather(@RequestParam int teamId, @RequestParam String gameDate) {
        LocalDate date = LocalDate.parse(gameDate);
        GameWeatherResponse response = weatherService.getWeatherForGame(teamId, date);
        return ResponseEntity.ok(response);
    }
}
