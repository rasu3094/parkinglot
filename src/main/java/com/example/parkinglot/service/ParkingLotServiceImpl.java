package com.example.parkinglot.service;

import com.example.parkinglot.model.*;
import com.example.parkinglot.repository.ParkingFloorRepository;
import com.example.parkinglot.repository.ParkingLotRepository;
import com.example.parkinglot.repository.ParkingSpotRepository;
import com.example.parkinglot.util.ParkingSlotSize;
import com.example.parkinglot.util.ParkingStatus;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ParkingLotServiceImpl implements ParkingLotService{
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;
    @Autowired
    private ParkingFloorRepository parkingFloorRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Override
    @Transactional
    public ParkingLot addParking(ParkingLotRequest parkingLotRequest) {
        ParkingLot parkingLot = ParkingLot.builder()
                .slotsCount(parkingLotRequest.getParkingSlotsCount())
                .name(parkingLotRequest.getName())
                .isAvailable(true)
                .build();
        parkingLotRepository.save(parkingLot);
        List<ParkingFloor> parkingFloors= insertParkingFloors(parkingLotRequest, parkingLot);
        parkingLot.setParkingFloors(parkingFloors);
        parkingLot.setUpdatedOn(Instant.now());
        return parkingLotRepository.save(parkingLot);
    }

    private List<ParkingFloor> insertParkingFloors(ParkingLotRequest parkingLotRequest, ParkingLot parkingLot) {
        List<ParkingFloor> parkingFloors = new ArrayList<>();
        for(int i =0; i < parkingLotRequest.getParkingFloorsCount(); i++){
            ParkingFloor parkingFloor = ParkingFloor.builder()
                    .isAvailable(true)
                    .name(parkingLotRequest.getParkingFloorRequests().get(i).getName())
                    .parkingLot(parkingLot)
                    .number(parkingLotRequest.getParkingFloorRequests().get(i).getNumber())
                    .build();
            ParkingFloor pf = parkingFloorRepository.save(parkingFloor);
            insertParkingSpots(parkingLotRequest.getParkingFloorRequests().get(i), pf, parkingLot);
            parkingFloors.add(parkingFloor);
            parkingFloor.setUpdatedOn(Instant.now());
            parkingFloorRepository.save(parkingFloor);
        }
        return parkingFloors;
    }


    private void insertParkingSpots(ParkingFloorRequest parkingLotRequest, ParkingFloor parkingFloor, ParkingLot parkingLot) {
        for(int i =0; i < parkingLotRequest.getSmallSizeSlotsCount(); i++){
            parkingSpotRepository.save(createParkingSpotsBySize(i, ParkingSlotSize.SMALL, parkingFloor, parkingLot));
        }
        for(int i =0; i < parkingLotRequest.getMediumSizeSlotsCount(); i++){
            parkingSpotRepository.save(createParkingSpotsBySize(i, ParkingSlotSize.MEDIUM, parkingFloor, parkingLot));
        }
         for(int i =0; i < parkingLotRequest.getLargeSizeSlotsCount(); i++){
            parkingSpotRepository.save(createParkingSpotsBySize(i, ParkingSlotSize.LARGE, parkingFloor, parkingLot));
        }
         for(int i =0; i < parkingLotRequest.getExtraLargeSizeSlotsCount(); i++){
            parkingSpotRepository.save(createParkingSpotsBySize(i, ParkingSlotSize.XLARGE, parkingFloor, parkingLot));
        }
    }

    private ParkingSpot createParkingSpotsBySize(int number, ParkingSlotSize parkingSlotSize, ParkingFloor parkingFloor, ParkingLot parkingLot) {
        return ParkingSpot.builder()
               .parkingSpotStatus(ParkingStatus.UNPARKED)
               .isAvailable(true)
               .parkingSpotType(parkingSlotSize)
               .parkingFloor(parkingFloor)
               .parkingLot(parkingLot)
               .name(parkingFloor.getName()+"-"+number+"-"+parkingSlotSize.name())
               .build();
    }

    @Override
    public List<ParkingLot> getAllParkingSlots() {
        return parkingLotRepository.findAll();
    }

    @Override
    public ParkingLot updateParkingLot(int parkingLotId) {
        ParkingLot  parkingLot = parkingLotRepository.findByNumber(parkingLotId);
        parkingLot.setUpdatedOn(Instant.now());
        return Objects.nonNull(parkingLot) ? parkingLotRepository.save(parkingLot) : null;
    }

    @Override
    public ParkingLot removeParkingLot(int parkingLotId) {
        ParkingLot  parkingLot = parkingLotRepository.findByNumber(parkingLotId);
        if (Objects.nonNull(parkingLot)) {
            parkingLotRepository.delete(parkingLot);
        }
        return parkingLot;
    }

    @Override
    public ParkingFloor addParkingFloor(ParkingFloor parkingFloor, int parkingLotId) {
        ParkingLot  parkingLot = parkingLotRepository.findByNumber(parkingLotId);
        parkingLot.setUpdatedOn(Instant.now());
        parkingFloor.setUpdatedOn(Instant.now());
        parkingFloor.setParkingLot(parkingLot);
        return Objects.nonNull(parkingLot) ? parkingFloorRepository.save(parkingFloor) : null;
    }

    @Override
    public ParkingSpot addParkingSpot(ParkingSpot parkingSpot, int parkingLotId, int parkingFloorId) {
        ParkingLot  parkingLot = parkingLotRepository.findByNumber(parkingLotId);
        parkingLot.setAvailable(true);
        parkingLot.setUpdatedOn(Instant.now());
        ParkingFloor parkingFloor = parkingFloorRepository.findByNumber(parkingFloorId);
        parkingFloor.setAvailable(true);
        parkingFloor.setUpdatedOn(Instant.now());
        parkingSpot.setParkingLot(parkingLot);
        parkingSpot.setParkingFloor(parkingFloor);
        parkingSpot.setAvailable(true);
        parkingSpot.setUpdatedOn(Instant.now());
        return Objects.nonNull(parkingLot) ? parkingSpotRepository.save(parkingSpot) : null;
    }
}
