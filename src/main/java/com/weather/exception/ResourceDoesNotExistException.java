package com.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceDoesNotExistException extends Exception {

    public ResourceDoesNotExistException() {
    }

    protected ResourceDoesNotExistException(String businessMessage) {
        super(businessMessage);
    }
}
