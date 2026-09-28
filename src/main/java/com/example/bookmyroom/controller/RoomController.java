package com.example.bookmyroom.controller;

import com.example.bookmyroom.model.Room;
import com.example.bookmyroom.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String viewRooms(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("rooms", roomService.searchRooms(keyword));
        model.addAttribute("keyword", keyword);
        return "rooms";
    }

    @GetMapping("/add")
    public String showAddRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "room-form";
    }

    @PostMapping("/save")
    public String saveRoom(@Valid @ModelAttribute("room") Room room, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "room-form";
        }
        roomService.saveRoom(room);
        return "redirect:/rooms";
    }

    @GetMapping("/edit/{id}")
    public String showEditRoomForm(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomService.getRoomById(id));
        return "room-form";
    }

    @PostMapping("/update/{id}")
    public String updateRoom(@PathVariable Long id, @Valid @ModelAttribute("room") Room room, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "room-form";
        }
        roomService.updateRoom(id, room);
        return "redirect:/rooms";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/rooms";
    }

    @GetMapping("/search")
    public String searchRooms(@RequestParam String keyword, Model model) {
        model.addAttribute("rooms", roomService.searchRooms(keyword));
        model.addAttribute("keyword", keyword);
        return "rooms";
    }
}
