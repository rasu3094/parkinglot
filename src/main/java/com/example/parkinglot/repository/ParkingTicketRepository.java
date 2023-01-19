package com.example.parkinglot.repository;


import com.example.parkinglot.model.ParkingTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, Integer> {

}
