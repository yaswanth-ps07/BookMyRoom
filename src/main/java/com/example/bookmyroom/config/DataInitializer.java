package com.example.bookmyroom.config;

import com.example.bookmyroom.model.Booking;
import com.example.bookmyroom.model.Guest;
import com.example.bookmyroom.model.Room;
import com.example.bookmyroom.repository.BookingRepository;
import com.example.bookmyroom.repository.GuestRepository;
import com.example.bookmyroom.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;

    public DataInitializer(RoomRepository roomRepository, GuestRepository guestRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(String... args) {
        if (roomRepository.count() == 0) {
            Room room1 = new Room(null, "101", "Single", 1, 1200.0, "Available");
            Room room2 = new Room(null, "102", "Double", 2, 1800.0, "Available");
            Room room3 = new Room(null, "103", "Deluxe", 2, 2500.0, "Available");
            Room room4 = new Room(null, "104", "Family", 4, 3200.0, "Available");
            roomRepository.save(room1);
            roomRepository.save(room2);
            roomRepository.save(room3);
            roomRepository.save(room4);
        }

        if (guestRepository.count() == 0) {
            Guest guest1 = new Guest(null, "Arun Kumar", "arun@example.com", "9876543210", "Coimbatore");
            Guest guest2 = new Guest(null, "Priya S", "priya@example.com", "9876501234", "Chennai");
            guestRepository.save(guest1);
            guestRepository.save(guest2);
        }

        if (bookingRepository.count() == 0) {
            Room room = roomRepository.findAll().get(0);
            Guest guest = guestRepository.findAll().get(0);

            Booking booking = new Booking();
            booking.setRoom(room);
            booking.setGuest(guest);
            booking.setCheckInDate(LocalDate.now().plusDays(3));
            booking.setCheckOutDate(LocalDate.now().plusDays(6));
            booking.setStatus("Confirmed");
            booking.setTotalCost(room.getRatePerNight() * 3);
            booking.setBookedAt(LocalDateTime.now());
            bookingRepository.save(booking);
        }
    }
}
