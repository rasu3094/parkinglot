package com.example.parkinglot.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "parking_tickets")
public class ParkingTicket extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", nullable = false)
    private ParkingLot parkingLot;

    @ManyToOne
    @JoinColumn(name = "parking_floor_id", nullable = false)
    private ParkingFloor parkingFloor;

    @ManyToOne
    @JoinColumn(name = "parking_spot_id", nullable = false)
    private ParkingSpot parkingSpot;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "registration_number")
    private Vehicle vehicle;

    private boolean isExpired;


}
