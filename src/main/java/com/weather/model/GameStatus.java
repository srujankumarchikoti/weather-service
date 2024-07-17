package com.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStatus {

    public String abstractGameState;
    public String codedGameState;
    public String detailedState;
    public String statusCode;
    public boolean startTimeTBD;
    public String abstractGameCode;
}
