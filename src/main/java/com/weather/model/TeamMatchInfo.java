package com.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamMatchInfo {

    public LeagueRecord leagueRecord;
    public Team team;
    public boolean splitSquad;
    public int seriesNumber;
}
