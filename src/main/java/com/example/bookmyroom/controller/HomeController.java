package com.example.bookmyroom.controller;

import com.example.bookmyroom.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final DashboardService dashboardService;

    public HomeController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalRooms", dashboardService.getTotalRooms());
        model.addAttribute("availableToday", dashboardService.getAvailableToday());
        model.addAttribute("totalGuests", dashboardService.getTotalGuests());
        model.addAttribute("activeBookings", dashboardService.getActiveBookings());
        model.addAttribute("completedBookings", dashboardService.getCompletedBookings());
        model.addAttribute("cancelledBookings", dashboardService.getCancelledBookings());
        model.addAttribute("recentBookings", dashboardService.getRecentBookings());
        model.addAttribute("upcomingCheckIns", dashboardService.getUpcomingCheckIns());
        model.addAttribute("todaysCheckIns", dashboardService.getTodaysCheckIns());
        model.addAttribute("todaysCheckOuts", dashboardService.getTodaysCheckOuts());
        model.addAttribute("totalUsableRooms", dashboardService.getTotalUsableRooms());
        model.addAttribute("currentlyOccupied", dashboardService.getCurrentlyOccupiedRooms());
        model.addAttribute("maintenanceRooms", dashboardService.getMaintenanceRooms());
        model.addAttribute("occupancyPercentage", dashboardService.getOccupancyPercentage());
        return "index";
    }
}
