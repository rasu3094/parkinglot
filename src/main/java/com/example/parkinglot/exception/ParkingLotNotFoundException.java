package com.example.parkinglot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParkingLotNotFoundException extends Exception{

    private String errorCode;
    private String errorMessage;

    public ParkingLotNotFoundException(String errorMessage, String errorCode){
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
