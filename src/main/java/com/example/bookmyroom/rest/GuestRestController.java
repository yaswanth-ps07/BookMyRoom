package com.example.bookmyroom.rest;

import com.example.bookmyroom.exception.ResourceNotFoundException;
import com.example.bookmyroom.model.Guest;
import com.example.bookmyroom.service.GuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guests")
@Tag(name = "Guests", description = "Guest management APIs")
public class GuestRestController {

    private final GuestService guestService;

    public GuestRestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @Operation(summary = "Get all guests")
    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    @Operation(summary = "Get guest by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getGuestById(@PathVariable Long id) {
        try {
            Guest guest = guestService.getGuestById(id);
            return ResponseEntity.ok(guest);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Guest not found"));
        }
    }

    @Operation(summary = "Create a new guest")
    @PostMapping
    public ResponseEntity<Guest> createGuest(@Valid @RequestBody Guest guest) {
        guest.setId(null);
        Guest createdGuest = guestService.saveGuest(guest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGuest);
    }

    @Operation(summary = "Update guest")
    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(@PathVariable Long id, @Valid @RequestBody Guest guestDetails) {
        return ResponseEntity.ok(guestService.updateGuest(id, guestDetails));
    }

    @Operation(summary = "Delete guest")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
        return ResponseEntity.noContent().build();
    }
}
