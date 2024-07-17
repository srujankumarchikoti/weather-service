package com.weather.controller;

import com.weather.model.GameWeatherResponse;
import com.weather.model.VenueWeatherResponse;
import com.weather.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    private WeatherController weatherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        weatherController = new WeatherController(weatherService);
    }

    @Test
    void getVenueWeather_shouldReturnVenueWeatherResponse() {
        // Given
        int venueId = 1;
        VenueWeatherResponse expectedResponse = new VenueWeatherResponse();

        // Mock the weather service
        when(weatherService.getWeatherForVenue(venueId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<VenueWeatherResponse> responseEntity = weatherController.getVenueWeather(venueId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void getGameWeather_shouldReturnGameWeatherResponse() {
        // Given
        int teamId = 17;
        String gameDate = "2024-06-07";
        LocalDate parsedDate = LocalDate.parse(gameDate);
        GameWeatherResponse expectedResponse = new GameWeatherResponse();

        // Mock the weather service
        when(weatherService.getWeatherForGame(teamId, parsedDate)).thenReturn(expectedResponse);

        // When
        ResponseEntity<GameWeatherResponse> responseEntity = weatherController.getGameWeather(teamId, gameDate);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
}
