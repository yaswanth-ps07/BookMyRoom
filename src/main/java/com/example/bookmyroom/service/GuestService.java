package com.example.bookmyroom.service;

import com.example.bookmyroom.exception.ResourceNotFoundException;
import com.example.bookmyroom.model.Guest;
import com.example.bookmyroom.repository.GuestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    public Guest getGuestById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));
    }

    public Guest saveGuest(Guest guest) {
        return guestRepository.save(guest);
    }

    public Guest updateGuest(Long id, Guest guestDetails) {
        Guest guest = getGuestById(id);
        guest.setName(guestDetails.getName());
        guest.setEmail(guestDetails.getEmail());
        guest.setPhone(guestDetails.getPhone());
        guest.setAddress(guestDetails.getAddress());
        return guestRepository.save(guest);
    }

    public void deleteGuest(Long id) {
        Guest guest = getGuestById(id);
        guestRepository.delete(guest);
    }

    public List<Guest> searchGuests(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllGuests();
        }
        return guestRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(keyword, keyword, keyword);
    }
}
