package com.example.parkinglot.controller;

import com.example.parkinglot.exception.ParkingLotNotFoundException;
import com.example.parkinglot.model.ParkingFloor;
import com.example.parkinglot.model.ParkingLot;
import com.example.parkinglot.model.ParkingLotRequest;
import com.example.parkinglot.model.ParkingSpot;
import com.example.parkinglot.service.ParkingLotService;
import com.example.parkinglot.util.ErrorCodes;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/v1/parking-lot")
public class ParkingLotController {

    @Autowired
    private ParkingLotService parkingLotService;

    @PostMapping
    public ResponseEntity addParkingLot(@RequestBody @NonNull ParkingLotRequest parkingLotRequest){
        return new ResponseEntity<>(parkingLotService.addParking(parkingLotRequest), HttpStatus.CREATED);
    }
    @PostMapping("/{parkingLotId}")
    public ResponseEntity addParkingFloor(@RequestBody @NonNull ParkingFloor parkingFloor, @PathVariable int parkingLotId){
        return new ResponseEntity<>(parkingLotService.addParkingFloor(parkingFloor, parkingLotId), HttpStatus.OK);
    }

    @PostMapping("/{parkingLotId}/{parkingFloorId}")
    public ResponseEntity addParkingSpot(@RequestBody @NonNull ParkingSpot parkingSpot, @PathVariable int parkingLotId, @PathVariable int parkingFloorId){
        return new ResponseEntity<>(parkingLotService.addParkingSpot(parkingSpot,parkingLotId, parkingFloorId), HttpStatus.CREATED);
    }


    @PutMapping("/{parkingLotId}")
    public ResponseEntity updateParkingLot(@PathVariable int parkingLotId) throws ParkingLotNotFoundException {
        ParkingLot  parkingLot = parkingLotService.updateParkingLot(parkingLotId);
        if(Objects.nonNull(parkingLot)){
            return new ResponseEntity<>(parkingLot, HttpStatus.OK);
        }
        throw new ParkingLotNotFoundException("ParkingLot not found - "+parkingLotId, ErrorCodes.NOT_FOUND);
     }
    @DeleteMapping("/{parkingLotId}")
    public ResponseEntity removeParkingLot(@PathVariable int parkingLotId) throws ParkingLotNotFoundException {
        ParkingLot  parkingLot = parkingLotService.removeParkingLot(parkingLotId);
        if(Objects.nonNull(parkingLot)){
            return new ResponseEntity<>(parkingLot, HttpStatus.OK);
        }
        throw new ParkingLotNotFoundException("ParkingLot not found - "+parkingLotId, ErrorCodes.NOT_FOUND);
     }

    @GetMapping
    public ResponseEntity getParkingLots(){
        return new ResponseEntity<>(parkingLotService.getAllParkingSlots(), HttpStatus.CREATED);
    }
}
