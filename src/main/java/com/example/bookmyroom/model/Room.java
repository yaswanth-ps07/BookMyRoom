package com.example.bookmyroom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Room number is required.")
    @Column(nullable = false)
    private String roomNumber;

    @NotBlank(message = "Room type is required.")
    @Column(nullable = false)
    private String roomType;

    @NotNull(message = "Capacity is required.")
    @Positive(message = "Capacity must be positive.")
    @Column(nullable = false)
    private Integer capacity;

    @NotNull(message = "Rate per night is required.")
    @Positive(message = "Rate per night must be positive.")
    @Column(nullable = false)
    private Double ratePerNight;

    @Column(nullable = false)
    private String status = "Available";

    public Room() {
    }

    public Room(Long id, String roomNumber, String roomType, Integer capacity, Double ratePerNight, String status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.ratePerNight = ratePerNight;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getRatePerNight() {
        return ratePerNight;
    }

    public void setRatePerNight(Double ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
