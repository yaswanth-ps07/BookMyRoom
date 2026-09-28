package com.example.bookmyroom.service;

import com.example.bookmyroom.model.Booking;
import com.example.bookmyroom.model.Guest;
import com.example.bookmyroom.model.Room;
import com.example.bookmyroom.repository.BookingRepository;
import com.example.bookmyroom.repository.GuestRepository;
import com.example.bookmyroom.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;

    public DashboardService(RoomRepository roomRepository, GuestRepository guestRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.bookingRepository = bookingRepository;
    }

    public long getTotalRooms() {
        return roomRepository.count();
    }

    public long getAvailableToday() {
        long totalUsableRooms = roomRepository.findAll().stream()
                .filter(room -> !"Maintenance".equalsIgnoreCase(room.getStatus()))
                .count();
        long occupiedToday = getCurrentlyOccupiedRooms();
        return Math.max(0, totalUsableRooms - occupiedToday);
    }

    public long getTotalUsableRooms() {
        return roomRepository.findAll().stream()
                .filter(room -> !"Maintenance".equalsIgnoreCase(room.getStatus()))
                .count();
    }

    public long getCurrentlyOccupiedRooms() {
        LocalDate today = LocalDate.now();
        return bookingRepository.findByStatusAndCheckInDateLessThanEqualAndCheckOutDateAfter("Confirmed", today, today)
                .size();
    }

    public long getMaintenanceRooms() {
        return roomRepository.findAll().stream()
                .filter(room -> "Maintenance".equalsIgnoreCase(room.getStatus()))
                .count();
    }

    public double getOccupancyPercentage() {
        long totalRooms = getTotalRooms();
        if (totalRooms == 0) {
            return 0.0;
        }
        return (getCurrentlyOccupiedRooms() * 100.0) / totalRooms;
    }

    public long getTotalGuests() {
        return guestRepository.count();
    }

    public long getActiveBookings() {
        return bookingRepository.findAll().stream()
                .filter(booking -> "Confirmed".equalsIgnoreCase(booking.getStatus()))
                .count();
    }

    public long getCompletedBookings() {
        return bookingRepository.findAll().stream()
                .filter(booking -> "Completed".equalsIgnoreCase(booking.getStatus()))
                .count();
    }

    public long getCancelledBookings() {
        return bookingRepository.findAll().stream()
                .filter(booking -> "Cancelled".equalsIgnoreCase(booking.getStatus()))
                .count();
    }

    public List<Booking> getRecentBookings() {
        return bookingRepository.findAll().stream()
                .sorted((b1, b2) -> b2.getBookedAt().compareTo(b1.getBookedAt()))
                .limit(5)
                .toList();
    }

    public List<Booking> getUpcomingCheckIns() {
        LocalDate today = LocalDate.now();
        return bookingRepository.findByStatusAndCheckInDateGreaterThanEqualOrderByCheckInDateAsc("Confirmed", today)
                .stream()
                .limit(5)
                .toList();
    }

    public List<Booking> getTodaysCheckIns() {
        return bookingRepository.findByStatusAndCheckInDate("Confirmed", LocalDate.now());
    }

    public List<Booking> getTodaysCheckOuts() {
        return bookingRepository.findByStatusAndCheckOutDate("Confirmed", LocalDate.now());
    }

    public Map<String, Long> getDashboardStats() {
        return Map.of(
                "totalRooms", getTotalRooms(),
                "availableToday", getAvailableToday(),
                "totalGuests", getTotalGuests(),
                "activeBookings", getActiveBookings(),
                "completedBookings", getCompletedBookings(),
                "cancelledBookings", getCancelledBookings()
        );
    }
}
