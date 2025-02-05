package com.example.cinemasystem.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetailsDto {

  private Long ticketId;
  private List<String> seatNumbers;
  private Double price;
  private String theater;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String movieName;
  private LocalDateTime purchaseDate;

}

