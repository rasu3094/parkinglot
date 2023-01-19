package com.example.parkinglot.service;

import com.example.parkinglot.model.ParkingFloor;
import com.example.parkinglot.model.ParkingLot;
import com.example.parkinglot.model.ParkingLotRequest;
import com.example.parkinglot.model.ParkingSpot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ParkingLotService {
    ParkingLot addParking(ParkingLotRequest parkingLot);

    List<ParkingLot> getAllParkingSlots();

    ParkingLot updateParkingLot(int parkingLotId);

    ParkingLot removeParkingLot(int parkingLotId);

    ParkingFloor addParkingFloor(ParkingFloor parkingFloor, int parkingLotId);

    ParkingSpot addParkingSpot(ParkingSpot parkingSpot, int parkingLotId, int parkingFloorId);
}
