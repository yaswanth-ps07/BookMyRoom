package com.example.bookmyroom.controller;

import com.example.bookmyroom.model.Guest;
import com.example.bookmyroom.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping
    public String viewGuests(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("guests", guestService.searchGuests(keyword));
        model.addAttribute("keyword", keyword);
        return "guests";
    }

    @GetMapping("/add")
    public String showAddGuestForm(Model model) {
        model.addAttribute("guest", new Guest());
        return "guest-form";
    }

    @PostMapping("/save")
    public String saveGuest(@Valid @ModelAttribute("guest") Guest guest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "guest-form";
        }
        guestService.saveGuest(guest);
        return "redirect:/guests";
    }

    @GetMapping("/edit/{id}")
    public String showEditGuestForm(@PathVariable Long id, Model model) {
        model.addAttribute("guest", guestService.getGuestById(id));
        return "guest-form";
    }

    @PostMapping("/update/{id}")
    public String updateGuest(@PathVariable Long id, @Valid @ModelAttribute("guest") Guest guest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "guest-form";
        }
        guestService.updateGuest(id, guest);
        return "redirect:/guests";
    }

    @GetMapping("/delete/{id}")
    public String deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
        return "redirect:/guests";
    }

    @GetMapping("/search")
    public String searchGuests(@RequestParam String keyword, Model model) {
        model.addAttribute("guests", guestService.searchGuests(keyword));
        model.addAttribute("keyword", keyword);
        return "guests";
    }
}
