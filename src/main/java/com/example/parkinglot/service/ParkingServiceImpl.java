package com.example.parkinglot.service;

import com.example.parkinglot.exception.ParkingUnavailableException;
import com.example.parkinglot.model.*;
import com.example.parkinglot.repository.*;
import com.example.parkinglot.util.ErrorCodes;
import com.example.parkinglot.util.ParkingSlotSize;
import com.example.parkinglot.util.ParkingStatus;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ParkingServiceImpl implements ParkingService{

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private ParkingFloorRepository parkingFloorRepository;

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingTicketRepository parkingTicketRepository;

    @Override
    @Transactional
    public ParkedResponse getAvailableSlot(Vehicle vehicle, int parkingLotId) throws ParkingUnavailableException {
        List<ParkingSpot> parkingSpots = parkingSpotRepository.findAll()
                                    .stream()
                                    .filter(ps -> ps.getParkingLot().getNumber() == parkingLotId)
                                    .filter(ParkingSpot::isAvailable)
                                    .collect(Collectors.toList());
        ParkingSpot availableParkingSpot = getAvailableParkingSpot(parkingSpots, vehicle.getType());
        if (Objects.nonNull(availableParkingSpot)) {
            saveVehicleWithParkingStatus(availableParkingSpot, vehicle);
            updateParkingDetails(availableParkingSpot, vehicle);
            Long ticketNumber = addParkingTicketDetails(availableParkingSpot, vehicle).getId();
            return getParkedResponse(availableParkingSpot, vehicle, ticketNumber);
        }
        throw new ParkingUnavailableException("Parking not available", ErrorCodes.NOT_FOUND);
    }

    @Override
    @Transactional
    public UnParkedResponse freeParkingSlot(int parkingTicketNumber) {
        Optional<ParkingTicket> parkingTicket = parkingTicketRepository.findById(parkingTicketNumber);
        if(parkingTicket.isPresent()){
            updateParkingFloorDetails(parkingTicket.get());
            updateParkingSlotDetails(parkingTicket.get());
            updateParkingTicketDetails(parkingTicket.get());
            return getUnParkedResponse(parkingTicket.get());
        }
        throw new RuntimeException("Bad Request");
    }

    private UnParkedResponse getUnParkedResponse(ParkingTicket parkingTicket) {
        return UnParkedResponse.builder()
                .carNumber(parkingTicket.getVehicle().getRegistrationNumber())
                .carType(parkingTicket.getVehicle().getType())
                .parkingSlotType(parkingTicket.getParkingSpot().getParkingSpotType().name())
                .timeIn(parkingTicket.getCreatedOn())
                .timeOut(Instant.now())
                .slot(parkingTicket.getParkingSpot().getName())
                .build();
    }

    private void updateParkingSlotDetails(ParkingTicket parkingTicket) {
        ParkingSpot parkingSpot = parkingTicket.getParkingSpot();
        parkingSpot.setUpdatedOn(Instant.now());
        parkingSpot.setAvailable(true);
        parkingSpot.setVehicle(null);
        parkingSpot.setParkingSpotStatus(ParkingStatus.UNPARKED);
        parkingSpotRepository.save(parkingSpot);
    }

    private void updateParkingFloorDetails(ParkingTicket parkingTicket) {
        ParkingFloor parkingFloor = parkingTicket.getParkingFloor();
        parkingFloor.setUpdatedOn(Instant.now());
        parkingFloor.setAvailable(true);
        parkingFloorRepository.save(parkingFloor);
    }

    private void updateParkingTicketDetails(ParkingTicket parkingTicket) {
        parkingTicket.setUpdatedOn(Instant.now());
        parkingTicket.setExpired(true);
        parkingTicketRepository.save(parkingTicket);
    }

    private ParkingTicket addParkingTicketDetails(ParkingSpot parkingSpot, Vehicle vehicle) {
        ParkingTicket parkingTicket = new ParkingTicket();
        parkingTicket.setParkingLot(parkingSpot.getParkingLot());
        parkingTicket.setParkingFloor(parkingSpot.getParkingFloor());
        parkingTicket.setUpdatedOn(Instant.now());
        parkingTicket.setParkingSpot(parkingSpot);
        parkingTicket.setVehicle(vehicle);
        return parkingTicketRepository.save(parkingTicket);
    }

    private void updateParkingDetails(ParkingSpot parkingSpot, Vehicle vehicle) {
        parkingSpot.setAvailable(false);
        parkingSpot.setVehicle(vehicle);
        parkingSpot.setParkingSpotStatus(ParkingStatus.PARKED);
        parkingSpotRepository.save(parkingSpot);
    }

    private void saveVehicleWithParkingStatus(ParkingSpot availableParkingSpot, Vehicle vehicle) {
        vehicle.setParkingSpots(Arrays.asList(availableParkingSpot));
        vehicle.setParkingStatus(ParkingStatus.PARKED);
        vehicleRepository.save(vehicle);
    }

    private ParkingSpot getAvailableParkingSpot(List<ParkingSpot> parkingSpots, String type) {
        ParkingSlotSize slotSize = ParkingSlotSize.valueOf(type);
        ParkingSpot parkingSpot = null;
        switch (slotSize) {
            case SMALL :
                parkingSpot= parkingSpots.stream()
                            .filter(ps -> ps.getParkingSpotType()==ParkingSlotSize.SMALL )
                            .findFirst().orElse(null);
                if(Objects.nonNull(parkingSpot)) break;

            case MEDIUM :
                parkingSpot= parkingSpots.stream()
                        .filter(ps -> ps.getParkingSpotType()==ParkingSlotSize.MEDIUM )
                        .findFirst().orElse(null);
                if(Objects.nonNull(parkingSpot)) break;

            case LARGE :
                parkingSpot= parkingSpots.stream()
                        .filter(ps -> ps.getParkingSpotType()==ParkingSlotSize.LARGE )
                        .findFirst().orElse(null);
                if(Objects.nonNull(parkingSpot)) break;

            case XLARGE :
                parkingSpot= parkingSpots.stream()
                        .filter(ps -> ps.getParkingSpotType()==ParkingSlotSize.XLARGE )
                        .findFirst().orElse(null);
                if(Objects.nonNull(parkingSpot)) break;
            default :
                log.info("Not Matching slot available");
        }
        return parkingSpot;
    }

    private ParkedResponse getParkedResponse(ParkingSpot parkingSpot, Vehicle vehicle, Long ticketNumber) {
        return ParkedResponse.builder()
                .parkingSlotType(parkingSpot.getParkingSpotType().name())
                .carType(vehicle.getType())
                .carNumber(vehicle.getRegistrationNumber())
                .timeIn(Instant.now())
                .ticketNumber(ticketNumber)
                .slot(parkingSpot.getName())
                .build();
    }
}
