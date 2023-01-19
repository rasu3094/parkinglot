package com.example.parkinglot.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLotRequest {

    private int parkingSlotsCount;

    private int parkingFloorsCount;
    private String name;

    private List<ParkingFloorRequest> parkingFloorRequests;

}
