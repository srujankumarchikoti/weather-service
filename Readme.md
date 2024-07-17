# Weather Microservice

This microservice provides weather information for MLB ballparks and upcoming games.

## Setup

1. Navigate to the project directory:

    ```bash
    cd weather-service
    ```

2. Build the project using Maven:

    ```bash
    mvn clean install
    ```

## Usage

### Endpoints

1. **Get Venue Weather**: Get weather information for a specific venue.

    - **URL**: `/weather/venue/{venueId}`
    - **Method**: GET
    - **Parameters**:
        - `venueId`: ID of the venue
    - **Example**: `/weather/venue/3289`

2. **Get Game Weather**: Get weather information for an upcoming game.

    - **URL**: `/weather/game?teamId=&gameDate=`
    - **Method**: GET
    - **Parameters**:
        - `teamId`: ID of the baseball team
        - `gameDate`: Date of the game (format: yyyy-MM-dd)
    - **Example**: `/weather/game?teamId=121&gameDate=2024-06-06`

### Dependencies

- Java 17
- Spring Boot 3.3.0
- Maven

### Configuration

- API URLs and keys are configured in `application.properties`.

## Development

- This project is built using Spring Boot.
- Tests are written using JUnit and Mockito.



