package com.example.cinemasystem.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ShowtimeDto {

  private Long id;
  private MovieDto movie;
  private String theater;
  private LocalDateTime startTime;
  private LocalDateTime endTime;

}
