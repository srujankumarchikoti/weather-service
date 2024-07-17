package com.weather.model;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    public int gamePk;
    public String gameGuid;
    public String link;
    public String gameType;
    public String season;
    public Date gameDate;
    public String officialDate;
    public GameStatus status;
    public TeamInfo teams;
    public Venue venue;
    public Content content;
    public int gameNumber;
    public boolean publicFacing;
    public String doubleHeader;
    public String gamedayType;
    public String tiebreaker;
    public String calendarEventID;
    public String seasonDisplay;
    public String dayNight;
    public String description;
    public int scheduledInnings;
    public boolean reverseHomeAwayStatus;
    public int inningBreakLength;
    public int gamesInSeries;
    public int seriesGameNumber;
    public String seriesDescription;
    public String recordSource;
    public String ifNecessary;
    public String ifNecessaryDescription;
}
