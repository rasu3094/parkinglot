package com.example.parkinglot.service;

import com.example.parkinglot.exception.ParkingUnavailableException;
import com.example.parkinglot.model.ParkedResponse;
import com.example.parkinglot.model.UnParkedResponse;
import com.example.parkinglot.model.Vehicle;
import org.springframework.stereotype.Service;

@Service
public interface ParkingService {
    ParkedResponse getAvailableSlot(Vehicle vehicle, int parkingLotId) throws ParkingUnavailableException;

    UnParkedResponse freeParkingSlot(int parkingTicketNumber);
}
