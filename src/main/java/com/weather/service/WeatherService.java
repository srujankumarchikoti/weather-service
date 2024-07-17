package com.weather.service;

import com.weather.config.WeatherConfigurationProperties;
import com.weather.exception.GameNotFoundException;
import com.weather.exception.VenueNotFoundException;
import com.weather.exception.WeatherInfoNotFoundException;
import com.weather.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import com.weather.exception.InvalidRequestException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeatherConfigurationProperties weatherConfigurationProperties;

    public VenueWeatherResponse getWeatherForVenue(int venueId) {
        VenueResponse venueResponse = getVenue(venueId);
        if (!CollectionUtils.isEmpty(venueResponse.getVenues())) {
            Venue venue = venueResponse
                    .getVenues()
                    .get(0);
            WeatherResponse weatherResponse = getWeatherInfo(
                    venue
                            .getLocation()
                            .getDefaultCoordinates()
                            .getLatitude(),
                    venue
                            .getLocation()
                            .getDefaultCoordinates()
                            .getLongitude());
            VenueWeatherResponse venueWeatherResponse = new VenueWeatherResponse();
            venueWeatherResponse.setName(venue.getName());
            venueWeatherResponse.setCity(venue
                    .getLocation()
                    .getCity());
            venueWeatherResponse.setTemp(weatherResponse
                    .getCurrent()
                    .getTemp());
            venueWeatherResponse.setWind_speed(weatherResponse
                    .getCurrent()
                    .getWind_speed());
            venueWeatherResponse.setHumidity(weatherResponse
                    .getCurrent()
                    .getHumidity());
            venueWeatherResponse.setPressure(weatherResponse
                    .getCurrent()
                    .getPressure());
            return venueWeatherResponse;
        } else {
            throw new VenueNotFoundException("Venue not found for venueId: " + venueId);
        }
    }

    public GameWeatherResponse getWeatherForGame(int teamId, LocalDate gameDate) {
        validateGameDate(gameDate);
        GamesResponse response = getGameInfo(teamId, gameDate);

        if (Objects.nonNull(response) && !CollectionUtils.isEmpty(response.getDates())) {
            List<String> teamNames = new ArrayList<>();
            response.getDates().forEach(date -> {
                date.getGames().forEach(game-> {
                    teamNames.add(game.getTeams().getAway().getTeam().getName());
                    teamNames.add(game.getTeams().getHome().getTeam().getName());
                });
            });

            Integer venueId = response.getDates().get(0).getGames().get(0).getVenue().getId();
            VenueWeatherResponse venueWeatherResponse = getWeatherForVenue(venueId);
            return mapResponse(teamNames, venueWeatherResponse, gameDate);
        } else {
            throw new GameNotFoundException("Game not found for teamId: " + teamId );
        }

    }

    private void validateGameDate(LocalDate gameDate) {
        LocalDate today = LocalDate.now();
        if (gameDate.isBefore(today) || gameDate.isAfter(today.plusDays(7))) {
            throw new InvalidRequestException(
                    "Invalid date: Date is either in the past or more than 7 days in the future.");
        }
    }

    public VenueResponse getVenue(int venueId) {
        StringBuilder venueUrl = new StringBuilder();
        venueUrl.append(weatherConfigurationProperties.getMlbBaseUrl());
        venueUrl.append("/venues/");
        venueUrl.append(venueId);
        venueUrl.append("?hydrate=location");
        try {
            return restTemplate.getForObject(venueUrl.toString(), VenueResponse.class);
        } catch (Exception e) {
            throw new VenueNotFoundException("Venue not found for venueId:" + venueId);
        }
    }

    public WeatherResponse getWeatherInfo(Double latitude, Double longitude) {
        StringBuilder weatherUrl = new StringBuilder();
        weatherUrl.append(weatherConfigurationProperties.getWeatherBaseUrl());
        weatherUrl.append("?lat=");
        weatherUrl.append(latitude);
        weatherUrl.append("&lon=");
        weatherUrl.append(longitude);
        weatherUrl.append("&units=metric&appid=");
        weatherUrl.append(weatherConfigurationProperties.getApiId());

        try {
            return restTemplate.getForObject(weatherUrl.toString(), WeatherResponse.class);
        } catch (Exception e) {
            throw new WeatherInfoNotFoundException("Weather info not found for latitude: " + latitude + " longitude: " + longitude);
        }
    }

    public GamesResponse getGameInfo(int teamId, LocalDate gameDate) {
        StringBuilder gameUrl = new StringBuilder();
        gameUrl.append(weatherConfigurationProperties.getMlbBaseUrl());
        gameUrl.append("/schedule?scheduleTypes=games&sportIds=1&teamIds=");
        gameUrl.append(teamId);
        gameUrl.append("&startDate=");
        gameUrl.append(gameDate);
        gameUrl.append("&endDate=");
        gameUrl.append(gameDate.plusDays(7));
        try {
            return restTemplate.getForObject(gameUrl.toString(), GamesResponse.class);
        } catch (Exception e) {
            throw new GameNotFoundException("Game info not found for given teamId: " + teamId);
        }
    }

    private GameWeatherResponse mapResponse(List<String> teams, VenueWeatherResponse venueWeatherResponse, LocalDate gameDate) {
        GameWeatherResponse weatherResponse = new GameWeatherResponse();
        weatherResponse.setGameDate(gameDate);
        weatherResponse.setTeams(teams.stream()
                .distinct()
                .collect(Collectors.toList()));
        weatherResponse.setTemp(venueWeatherResponse.getTemp());
        weatherResponse.setPressure(venueWeatherResponse.getPressure());
        weatherResponse.setHumidity(venueWeatherResponse.getHumidity());
        weatherResponse.setWind_speed(venueWeatherResponse.getWind_speed());
        return weatherResponse;
    }
}