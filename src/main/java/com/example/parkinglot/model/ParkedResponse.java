package com.example.parkinglot.model;


import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ParkedResponse {

    private Long ticketNumber;
    private int carNumber;
    private Instant timeIn;
    private String slot;

    private String carType;

    private String parkingSlotType;


}
