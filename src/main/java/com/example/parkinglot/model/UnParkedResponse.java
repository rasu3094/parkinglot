package com.example.parkinglot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UnParkedResponse {

    private int carNumber;
    @JsonIgnore
    private Instant timeIn;
    @JsonIgnore
    private Instant timeOut;
    private String slot;

    private String carType;

    private String parkingSlotType;
}
