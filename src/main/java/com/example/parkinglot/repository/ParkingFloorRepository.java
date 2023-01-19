package com.example.parkinglot.repository;


import com.example.parkinglot.model.ParkingFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingFloorRepository extends JpaRepository<ParkingFloor, Integer> {
    ParkingFloor findByNumber(int number);
}
