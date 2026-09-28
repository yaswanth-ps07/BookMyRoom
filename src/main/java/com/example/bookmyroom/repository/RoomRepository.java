package com.example.bookmyroom.repository;

import com.example.bookmyroom.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByRoomNumberContainingIgnoreCaseOrRoomTypeContainingIgnoreCase(String roomNumber, String roomType);

    boolean existsByRoomNumberIgnoreCase(String roomNumber);

    long countByStatusNot(String status);
}
