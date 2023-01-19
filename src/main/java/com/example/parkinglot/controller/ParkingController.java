package com.example.parkinglot.controller;

import com.example.parkinglot.exception.ParkingUnavailableException;
import com.example.parkinglot.model.ParkedResponse;
import com.example.parkinglot.model.Vehicle;
import com.example.parkinglot.service.ParkingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/v1/parking")
@Api(value = "parking", description = "Operations pertaining to parking in Parking lots")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @PostMapping("/{parkingLotId}")
    @ApiOperation(value = "Sample Test case", response = ParkedResponse.class)
    public ResponseEntity getSlot(@RequestBody @NonNull final Vehicle vehicle, @PathVariable int parkingLotId) throws ParkingUnavailableException {
        ParkedResponse parkedResponse = parkingService.getAvailableSlot(vehicle, parkingLotId);
        return new ResponseEntity<>(parkedResponse, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{parkingTicketNumber}")
    public ResponseEntity freeSlot(@PathVariable int parkingTicketNumber) {
        try {
            return new ResponseEntity<>(parkingService.freeParkingSlot(parkingTicketNumber), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
