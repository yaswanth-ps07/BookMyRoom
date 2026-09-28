package com.example.bookmyroom.controller;

import com.example.bookmyroom.model.Booking;
import com.example.bookmyroom.model.Guest;
import com.example.bookmyroom.model.Room;
import com.example.bookmyroom.service.BookingService;
import com.example.bookmyroom.service.GuestService;
import com.example.bookmyroom.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final GuestService guestService;
    private final RoomService roomService;

    public BookingController(BookingService bookingService, GuestService guestService, RoomService roomService) {
        this.bookingService = bookingService;
        this.guestService = guestService;
        this.roomService = roomService;
    }

    @GetMapping
    public String viewBookings(@RequestParam(required = false) String keyword, @RequestParam(required = false) String status, Model model) {
        List<Booking> bookings = bookingService.searchBookings(keyword == null ? "" : keyword);
        if (status != null && !status.isEmpty()) {
            bookings = bookings.stream()
                    .filter(booking -> booking.getStatus().equalsIgnoreCase(status))
                    .toList();
        }
        model.addAttribute("bookings", bookings);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        return "bookings";
    }

    @GetMapping("/add")
    public String showAddBookingForm(Model model) {
        model.addAttribute("booking", new Booking());
        model.addAttribute("guests", guestService.getAllGuests());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "booking-form";
    }

    @PostMapping("/save")
    public String saveBooking(@Valid @ModelAttribute("booking") Booking booking, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("guests", guestService.getAllGuests());
            model.addAttribute("rooms", roomService.getAllRooms());
            return "booking-form";
        }

        try {
            bookingService.createBooking(booking);
            model.addAttribute("successMessage", "Booking confirmed successfully.");
            return "redirect:/bookings";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("guests", guestService.getAllGuests());
            model.addAttribute("rooms", roomService.getAllRooms());
            return "booking-form";
        }
    }

    @GetMapping("/view/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        model.addAttribute("booking", bookingService.getBookingById(id));
        return "booking-view";
    }

    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return "redirect:/bookings";
    }

    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return "redirect:/bookings";
    }

    @GetMapping("/search")
    public String searchBookings(@RequestParam String keyword, @RequestParam(required = false) String status, Model model) {
        List<Booking> bookings = bookingService.searchBookings(keyword);
        if (status != null && !status.isEmpty()) {
            bookings = bookings.stream().filter(booking -> booking.getStatus().equalsIgnoreCase(status)).toList();
        }
        model.addAttribute("bookings", bookings);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        return "bookings";
    }

    @GetMapping("/availability")
    public String showAvailabilityForm(Model model) {
        model.addAttribute("checkIn", LocalDate.now());
        model.addAttribute("checkOut", LocalDate.now().plusDays(2));
        return "availability";
    }

    @PostMapping("/availability")
    public String checkAvailability(@RequestParam LocalDate checkIn,
                                   @RequestParam LocalDate checkOut,
                                   Model model) {
        try {
            List<Room> availableRooms = bookingService.getAvailableRooms(checkIn, checkOut);
            model.addAttribute("availableRooms", availableRooms);
            model.addAttribute("checkIn", checkIn);
            model.addAttribute("checkOut", checkOut);
            return "availability";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "availability";
        }
    }
}
