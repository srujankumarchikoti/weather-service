package com.weather.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(WeatherConfigurationProperties.PREFIX)
public class WeatherConfigurationProperties {

    public static final String PREFIX = "weather";

    private String mlbBaseUrl;

    private String weatherBaseUrl;

    private String apiId;
}
