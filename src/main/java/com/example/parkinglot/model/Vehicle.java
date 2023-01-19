package com.example.parkinglot.model;

import com.example.parkinglot.util.ParkingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @Column(name = "registration_number", nullable = false)
    private int registrationNumber;

    @NonNull
    private String color;

    @NonNull
    private String type;

    private ParkingStatus parkingStatus;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<ParkingSpot> parkingSpots;
}