package com.example.bookmyroom.service;

import com.example.bookmyroom.exception.BookingConflictException;
import com.example.bookmyroom.exception.ResourceNotFoundException;
import com.example.bookmyroom.model.Booking;
import com.example.bookmyroom.model.Room;
import com.example.bookmyroom.repository.BookingRepository;
import com.example.bookmyroom.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository, RoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    public Booking createBooking(Booking booking) {
        validateDates(booking.getCheckInDate(), booking.getCheckOutDate());

        Room room = booking.getRoom();
        if (room == null || room.getId() == null) {
            throw new IllegalArgumentException("Please select a room.");
        }
        Room existingRoom = roomService.getRoomById(room.getId());
        booking.setRoom(existingRoom);

        if (hasOverlappingBooking(existingRoom, booking.getCheckInDate(), booking.getCheckOutDate(), booking.getId())) {
            throw new BookingConflictException("Room is already booked for the selected date range.");
        }

        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        if (nights <= 0) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }

        double total = existingRoom.getRatePerNight() * nights;
        booking.setTotalCost(total);
        booking.setStatus("Confirmed");
        booking.setBookedAt(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.setStatus("Cancelled");
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        Booking booking = getBookingById(id);
        bookingRepository.delete(booking);
    }

    public List<Booking> searchBookings(String keyword) {
        List<Booking> bookings = getAllBookings();
        if (keyword == null || keyword.trim().isEmpty()) {
            return bookings;
        }
        String searchText = keyword.trim().toLowerCase();
        return bookings.stream()
                .filter(booking -> {
                    String guestName = booking.getGuest() != null && booking.getGuest().getName() != null ? booking.getGuest().getName().toLowerCase() : "";
                    String roomNumber = booking.getRoom() != null && booking.getRoom().getRoomNumber() != null ? booking.getRoom().getRoomNumber().toLowerCase() : "";
                    String status = booking.getStatus() != null ? booking.getStatus().toLowerCase() : "";
                    return guestName.contains(searchText)
                            || roomNumber.contains(searchText)
                            || status.contains(searchText);
                })
                .toList();
    }

    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        validateDates(checkIn, checkOut);
        List<Room> allRooms = roomRepository.findAll();
        return allRooms.stream()
                .filter(room -> !hasOverlappingBooking(room, checkIn, checkOut, null))
                .toList();
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required.");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
    }

    private boolean hasOverlappingBooking(Room room, LocalDate newCheckIn, LocalDate newCheckOut, Long bookingIdToIgnore) {
        List<Booking> bookings = bookingRepository.findOverlappingBookings(room, newCheckIn, newCheckOut);
        return bookings.stream().anyMatch(b -> !"Cancelled".equalsIgnoreCase(b.getStatus()) && !b.getId().equals(bookingIdToIgnore));
    }
}
