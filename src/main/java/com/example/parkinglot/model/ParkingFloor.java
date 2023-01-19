package com.example.parkinglot.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "parking_floors")
public class ParkingFloor extends BaseEntity {

    @Column(name = "number", nullable = false, unique = true)
    private int number;

    private String name;

    private boolean isAvailable;

    @JsonIgnore
    @OneToMany(mappedBy = "parkingFloor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ParkingSpot> parkingSpots;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parking_lot_id", nullable = false)
    private ParkingLot parkingLot;

}
