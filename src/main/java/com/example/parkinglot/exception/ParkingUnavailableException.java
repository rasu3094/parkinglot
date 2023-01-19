package com.example.parkinglot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ParkingUnavailableException extends Exception{

    private String errorCode;
    private String errorMessage;

    public ParkingUnavailableException(String errorMessage, String errorCode){
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
