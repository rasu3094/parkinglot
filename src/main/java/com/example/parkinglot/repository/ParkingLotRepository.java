package com.example.parkinglot.repository;


import com.example.parkinglot.model.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Integer> {
        ParkingLot findByNumber(int number);
}
