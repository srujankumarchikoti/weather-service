package com.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Venue {
    private Integer id;
    private String name;
    private String link;
    private Location location;
    private Boolean active;
    private String season;
}
