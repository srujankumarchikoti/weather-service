package com.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private Coordinate defaultCoordinates;
    private String address1;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
