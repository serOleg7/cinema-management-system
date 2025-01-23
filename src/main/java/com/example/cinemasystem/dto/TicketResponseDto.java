package com.example.cinemasystem.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TicketResponseDto {

  private Long id;
  private UserDto user;
  private ShowtimeDto showtime;
  private String seatNumber;
  private Double price;
  private LocalDateTime bookingDate = LocalDateTime.now();
  private String status = "CONFIRMED";

}
