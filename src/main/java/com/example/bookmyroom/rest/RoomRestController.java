package com.example.bookmyroom.rest;

import com.example.bookmyroom.exception.ResourceNotFoundException;
import com.example.bookmyroom.model.Room;
import com.example.bookmyroom.repository.RoomRepository;
import com.example.bookmyroom.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
@Tag(name = "Rooms", description = "Room management APIs")
public class RoomRestController {

    private final RoomService roomService;
    private final RoomRepository roomRepository;

    public RoomRestController(RoomService roomService, RoomRepository roomRepository) {
        this.roomService = roomService;
        this.roomRepository = roomRepository;
    }

    @Operation(summary = "Get all rooms")
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @Operation(summary = "Get room by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        try {
            Room room = roomService.getRoomById(id);
            return ResponseEntity.ok(room);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Room not found"));
        }
    }

    @Operation(summary = "Create a new room")
    @PostMapping
    public ResponseEntity<Room> createRoom(@Valid @RequestBody Room room) {
        room.setId(null);
        if (roomRepository.existsByRoomNumberIgnoreCase(room.getRoomNumber())) {
            throw new IllegalArgumentException("Room number already exists.");
        }
        Room createdRoom = roomService.saveRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    @Operation(summary = "Update room")
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @Valid @RequestBody Room roomDetails) {
        return ResponseEntity.ok(roomService.updateRoom(id, roomDetails));
    }

    @Operation(summary = "Delete room")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
