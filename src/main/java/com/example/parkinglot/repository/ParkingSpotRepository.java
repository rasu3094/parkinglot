package com.example.parkinglot.repository;


import com.example.parkinglot.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Integer> {

}
