package com.example.bookmyroom.repository;

import com.example.bookmyroom.model.Booking;
import com.example.bookmyroom.model.Guest;
import com.example.bookmyroom.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.room = :room AND b.status <> 'Cancelled' AND b.checkInDate < :checkOut AND b.checkOutDate > :checkIn")
    List<Booking> findOverlappingBookings(@Param("room") Room room,
                                          @Param("checkIn") LocalDate checkIn,
                                          @Param("checkOut") LocalDate checkOut);

    List<Booking> findByStatus(String status);

    List<Booking> findByGuest(Guest guest);

    List<Booking> findByRoom(Room room);

    List<Booking> findByStatusAndCheckInDateLessThanEqualAndCheckOutDateAfter(String status, LocalDate checkInDate, LocalDate checkOutDate);

    List<Booking> findByStatusAndCheckInDate(String status, LocalDate checkInDate);

    List<Booking> findByStatusAndCheckOutDate(String status, LocalDate checkOutDate);

    List<Booking> findByStatusAndCheckInDateGreaterThanEqualOrderByCheckInDateAsc(String status, LocalDate checkInDate);
}
