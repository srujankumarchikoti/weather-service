package com.weather.service;

import com.weather.config.WeatherConfigurationProperties;
import com.weather.exception.GameNotFoundException;
import com.weather.exception.InvalidRequestException;
import com.weather.exception.VenueNotFoundException;
import com.weather.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WeatherConfigurationProperties weatherConfigurationProperties;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWeatherForVenue_shouldReturnVenueWeatherResponse_whenVenueExists() {
        // Given
        int venueId = 1;
        VenueResponse venueResponse = new VenueResponse();
        Venue venue = new Venue();
        Location location = new Location();
        Coordinate coordinates = new Coordinate();
        coordinates.setLatitude(40.7128);
        coordinates.setLongitude(-74.0060);
        location.setDefaultCoordinates(coordinates);
        location.setCity("California");
        venue.setLocation(location);
        venue.setName("Dodger Stadium");
        venueResponse.setVenues(Collections.singletonList(venue));

        WeatherResponse weatherResponse = new WeatherResponse();
        Weather weather = new Weather();
        weather.setTemp(38.0);
        weather.setWind_speed(5.0);
        weather.setHumidity(62);
        weather.setPressure(1103);
        weatherResponse.setCurrent(weather);

        when(weatherConfigurationProperties.getMlbBaseUrl()).thenReturn("http://mlb.com");
        when(restTemplate.getForObject(anyString(), eq(VenueResponse.class))).thenReturn(venueResponse);
        when(weatherConfigurationProperties.getWeatherBaseUrl()).thenReturn("http://weather.com");
        when(weatherConfigurationProperties.getApiId()).thenReturn("dummyApiId");
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(weatherResponse);

        // When
        VenueWeatherResponse response = weatherService.getWeatherForVenue(venueId);

        // Then
        assertNotNull(response);
        assertEquals("Dodger Stadium", response.getName());
        assertEquals("California", response.getCity());
        assertEquals(38.0, response.getTemp());
        assertEquals(5.0, response.getWind_speed());
        assertEquals(62, response.getHumidity());
        assertEquals(1103, response.getPressure());
    }

    @Test
    void getWeatherForVenue_shouldThrowVenueNotFoundException_whenVenueDoesNotExist() {
        // Given
        int venueId = 1;
        VenueResponse venueResponse = new VenueResponse();
        venueResponse.setVenues(Collections.emptyList());

        when(weatherConfigurationProperties.getMlbBaseUrl()).thenReturn("http://mlb.com");
        when(restTemplate.getForObject(anyString(), eq(VenueResponse.class))).thenReturn(venueResponse);

        // When & Then
        assertThrows(VenueNotFoundException.class, () -> weatherService.getWeatherForVenue(venueId));
    }

    @Test
    void getWeatherForGame_shouldReturnGameWeatherResponse_whenGameExists() {
        // Given
        int teamId = 17;
        LocalDate gameDate = LocalDate.now().plusDays(1);

        // Create and set up GamesResponse
        GamesResponse gamesResponse = new GamesResponse();
        GameDates gameDates = new GameDates();
        Game game = new Game();

        // Set up Teams and TeamMatchInfo
        Team awayTeam = new Team();
        awayTeam.setName("Away Team");
        Team homeTeam = new Team();
        homeTeam.setName("Home Team");

        TeamMatchInfo awayTeamInfo = new TeamMatchInfo();
        awayTeamInfo.setTeam(awayTeam);
        TeamMatchInfo homeTeamInfo = new TeamMatchInfo();
        homeTeamInfo.setTeam(homeTeam);

        TeamInfo teamInfo = new TeamInfo();
        teamInfo.setAway(awayTeamInfo);
        teamInfo.setHome(homeTeamInfo);

        // Set teams and venue in the game
        game.setTeams(teamInfo);
        Venue gameVenue = new Venue();
        gameVenue.setId(1);
        game.setVenue(gameVenue);

        // Set games in GameDates and dates in GamesResponse
        gameDates.setGames(Collections.singletonList(game));
        gamesResponse.setDates(Collections.singletonList(gameDates));

        // Create and set up VenueWeatherResponse
        VenueWeatherResponse venueWeatherResponse = new VenueWeatherResponse();
        venueWeatherResponse.setName("Dodger Stadium");
        venueWeatherResponse.setCity("California");
        venueWeatherResponse.setTemp(38.0);
        venueWeatherResponse.setWind_speed(5.0);
        venueWeatherResponse.setHumidity(62);
        venueWeatherResponse.setPressure(1103);

        WeatherResponse weatherResponse = new WeatherResponse();
        Weather weather = new Weather();
        weather.setTemp(38.0);
        weather.setWind_speed(5.0);
        weather.setHumidity(62);
        weather.setPressure(1103);
        weatherResponse.setCurrent(weather);

        // Create and set up VenueResponse
        Venue venue = new Venue();
        venue.setId(1);
        Location location = new Location();
        Coordinate coordinate = new Coordinate(40.75753012, -73.84559155);
        location.setDefaultCoordinates(coordinate);
        venue.setLocation(location);
        VenueResponse venueResponse = new VenueResponse();
        venueResponse.setVenues(Collections.singletonList(venue));

        // Mock dependencies
        when(weatherConfigurationProperties.getMlbBaseUrl()).thenReturn("http://mlb.com");
        when(restTemplate.getForObject(anyString(), eq(GamesResponse.class))).thenReturn(gamesResponse);
        when(restTemplate.getForObject(anyString(), eq(VenueResponse.class))).thenReturn(venueResponse);
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(weatherResponse);

        // When
        GameWeatherResponse response = weatherService.getWeatherForGame(teamId, gameDate);

        // Then
        assertNotNull(response);
        assertEquals(gameDate, response.getGameDate());
        assertTrue(response.getTeams().contains("Away Team"));
        assertTrue(response.getTeams().contains("Home Team"));
        assertEquals(38.0, response.getTemp());
        assertEquals(5.0, response.getWind_speed());
        assertEquals(62, response.getHumidity());
        assertEquals(1103, response.getPressure());
    }

    @Test
    void getWeatherForGame_shouldThrowGameNotFoundException_whenGameDoesNotExist() {
        // Given
        int teamId = 1;
        LocalDate gameDate = LocalDate.now().plusDays(1);
        GamesResponse gamesResponse = new GamesResponse();
        gamesResponse.setDates(Collections.emptyList());

        when(weatherConfigurationProperties.getMlbBaseUrl()).thenReturn("http://mlb.com");
        when(restTemplate.getForObject(anyString(), eq(GamesResponse.class))).thenReturn(gamesResponse);

        // When & Then
        assertThrows(GameNotFoundException.class, () -> weatherService.getWeatherForGame(teamId, gameDate));
    }

    @Test
    void getWeatherForGame_shouldThrowInvalidRequestException_whenGameDateIsInvalid() {
        // Given
        int teamId = 1;
        LocalDate invalidGameDate = LocalDate.now().minusDays(1);

        // When & Then
        assertThrows(InvalidRequestException.class, () -> weatherService.getWeatherForGame(teamId, invalidGameDate));
    }
    @Test
    void getVenue_shouldReturnVenueResponse_whenVenueExists() {
        // Given
        int venueId = 1;
        VenueResponse venueResponse = new VenueResponse();
        Venue venue = new Venue();
        Location location = new Location();
        Coordinate coordinates = new Coordinate();
        coordinates.setLatitude(40.7128);
        coordinates.setLongitude(-74.0060);
        location.setDefaultCoordinates(coordinates);
        location.setCity("California");
        venue.setLocation(location);
        venue.setName("Dodger Stadium");
        venueResponse.setVenues(Collections.singletonList(venue));

        when(weatherConfigurationProperties.getMlbBaseUrl()).thenReturn("http://mlb.com");
        when(restTemplate.getForObject(anyString(), eq(VenueResponse.class))).thenReturn(venueResponse);

        // When
        VenueResponse response = weatherService.getVenue(venueId);

        // Then
        assertNotNull(response);
        assertEquals(1, response.getVenues().size());
        assertEquals("Dodger Stadium", response.getVenues().get(0).getName());
    }

    @Test
    void getVenue_shouldThrowVenueNotFoundException_whenVenueDoesNotExist() {
        // Given
        int venueId = 1;
        when(weatherConfigurationProperties.getMlbBaseUrl()).thenReturn("http://mlb.com");
        when(restTemplate.getForObject(anyString(), eq(VenueResponse.class))).thenThrow(new RuntimeException());

        // When & Then
        assertThrows(VenueNotFoundException.class, () -> weatherService.getVenue(venueId));
    }

    @Test
    void getWeatherInfo_shouldReturnWeatherResponse_whenWeatherInfoExists() {
        // Given
        Double latitude = 40.7128;
        Double longitude = -74.0060;
        WeatherResponse weatherResponse = new WeatherResponse();
        Weather weather = new Weather();
        weather.setTemp(25.0);
        weather.setWind_speed(5.0);
        weather.setHumidity(60);
        weather.setPressure(1013);
        weatherResponse.setCurrent(weather);

        when(weatherConfigurationProperties.getWeatherBaseUrl()).thenReturn("http://weather.com");
        when(weatherConfigurationProperties.getApiId()).thenReturn("dummyApiId");
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(weatherResponse);

        // When
        WeatherResponse response = weatherService.getWeatherInfo(latitude, longitude);

        // Then
        assertNotNull(response);
        assertEquals(25.0, response.getCurrent().getTemp());
        assertEquals(5.0, response.getCurrent().getWind_speed());
        assertEquals(60, response.getCurrent().getHumidity());
        assertEquals(1013, response.getCurrent().getPressure());
    }


@Test
void getGameInfo_shouldReturnGamesResponse_whenGamesExist() {
    // Given
    int teamId = 1;
    LocalDate gameDate = LocalDate.now().plusDays(1);
    GamesResponse gamesResponse = new GamesResponse();
    GameDates gameDates = new GameDates();
    Game game = new Game();

    // Set up Teams and TeamMatchInfo
    Team awayTeam = new Team();
    awayTeam.setName("Away Team");
    Team homeTeam = new Team();
    homeTeam.setName("Home Team");

    TeamMatchInfo awayTeamInfo = new TeamMatchInfo(null, awayTeam, false, 0);
    TeamMatchInfo homeTeamInfo = new TeamMatchInfo(null, homeTeam, false, 0);
    TeamInfo teamInfo = new TeamInfo(awayTeamInfo, homeTeamInfo);

    // Set teams and venue in the game
    game.setTeams(teamInfo);
    Venue gameVenue = new Venue();
    gameVenue.setId(1);
    game.setVenue(gameVenue);

    // Set games in GameDates and dates in GamesResponse
    gameDates.setGames(Collections.singletonList(game));
    gamesResponse.setDates(Collections.singletonList(gameDates));

    // Mock dependencies
    when(weatherConfigurationProperties.getMlbBaseUrl()).thenReturn("http://mlb.com");
    when(restTemplate.getForObject(anyString(), eq(GamesResponse.class))).thenReturn(gamesResponse);

    // When
    GamesResponse response = weatherService.getGameInfo(teamId, gameDate);

    // Then
    assertNotNull(response);
    assertEquals(1, response.getDates().size());
    assertEquals(1, response.getDates().get(0).getGames().size());
    assertEquals("Away Team", response.getDates().get(0).getGames().get(0).getTeams().getAway().getTeam().getName());
    assertEquals("Home Team", response.getDates().get(0).getGames().get(0).getTeams().getHome().getTeam().getName());

    // Verify method invocations
    verify(weatherConfigurationProperties, times(1)).getMlbBaseUrl();
    verify(restTemplate, times(1)).getForObject(anyString(), eq(GamesResponse.class));
}

    @Test
    void getGameInfo_shouldThrowGameNotFoundException_whenGamesDoNotExist() {
        // Given
        int teamId = 1;
        LocalDate gameDate = LocalDate.now().plusDays(1);
        when(weatherConfigurationProperties.getMlbBaseUrl()).thenReturn("http://mlb.com");
        when(restTemplate.getForObject(anyString(), eq(GamesResponse.class))).thenThrow(new RuntimeException());

        // When & Then
        assertThrows(GameNotFoundException.class, () -> weatherService.getGameInfo(teamId, gameDate));
    }
}


