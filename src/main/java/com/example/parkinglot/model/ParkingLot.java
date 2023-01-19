package com.example.parkinglot.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "parking_lots")
public class ParkingLot extends BaseEntity {

    @Column(name = "number", nullable = false, unique = true)
    private int number;

    private String name;

    private boolean isAvailable;

    private int slotsCount;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<ParkingFloor> parkingFloors;

}
