package com.weather.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueResponse {
    private String copyright;
    private List<Venue> venues;
}
