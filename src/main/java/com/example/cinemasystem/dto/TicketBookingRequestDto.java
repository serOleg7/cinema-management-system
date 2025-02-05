package com.example.cinemasystem.dto;

import java.util.List;
import lombok.Data;

@Data
public class TicketBookingRequestDto {

  private Long showtimeId;
  private List<String> seatNumbers;
  private Double pricePerSeat;

}
