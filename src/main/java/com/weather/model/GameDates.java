package com.weather.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDates {

    public String date;
    public int totalItems;
    public int totalEvents;
    public int totalGames;
    public int totalGamesInProgress;
    public List<Game> games;
}
