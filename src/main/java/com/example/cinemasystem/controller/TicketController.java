package com.example.cinemasystem.controller;

import com.example.cinemasystem.dto.TicketResponseDto;
import com.example.cinemasystem.model.Ticket;
import com.example.cinemasystem.service.TicketService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {

  private final TicketService ticketService;

  // todo fix user details response
  @PostMapping
  public ResponseEntity<TicketResponseDto> bookTicket(@RequestBody Ticket ticket) {
    return ResponseEntity.ok(ticketService.bookTicket(ticket));
  }

  @GetMapping
  public ResponseEntity<List<TicketResponseDto>> getTicketsByAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    return ResponseEntity.ok(ticketService.getTicketsByUserEmail(email));
  }

  @GetMapping("/showtime/{showtimeId}")
  public ResponseEntity<List<TicketResponseDto>> getTicketsByShowtime(@PathVariable Long showtimeId) {
    return ResponseEntity.ok(ticketService.getTicketsByShowtime(showtimeId));
  }

}
