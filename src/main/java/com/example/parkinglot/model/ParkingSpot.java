package com.example.parkinglot.model;


import com.example.parkinglot.util.ParkingSlotSize;
import com.example.parkinglot.util.ParkingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "parking_spots", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name",
                "parking_lot_id"
        })
})
public class ParkingSpot extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "registration_number")
    private Vehicle vehicle;

    private boolean isAvailable;

    private ParkingStatus parkingSpotStatus;

    private ParkingSlotSize parkingSpotType;

    @ManyToOne
    @JoinColumn(name = "parking_floor_id", nullable = false)
    private ParkingFloor parkingFloor;

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", nullable = false)
    private ParkingLot parkingLot;

}
