package com.example.parkinglot.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingFloorRequest {

    private int smallSizeSlotsCount;
    private int mediumSizeSlotsCount;
    private int largeSizeSlotsCount;
    private int extraLargeSizeSlotsCount;

    private int parkingSlotsCount;

    private String name;

    private int number;



}
