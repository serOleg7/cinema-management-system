package com.example.cinemasystem.controller;

import com.example.cinemasystem.dto.TicketBookingRequestDto;
import com.example.cinemasystem.dto.TicketResponseDto;
import com.example.cinemasystem.service.TicketService;
import com.example.user.model.User;
import com.example.user.service.UserDetailsImpl;
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

  @PostMapping
  public ResponseEntity<TicketResponseDto> bookTicket(@RequestBody TicketBookingRequestDto ticketBookingRequestDto) {
    return ResponseEntity.ok(ticketService.bookTicket(ticketBookingRequestDto, getAuthenticatedUser()));
  }

  @GetMapping
  public ResponseEntity<TicketResponseDto> getTicketsByAuthenticatedUser() {
    return ResponseEntity.ok(ticketService.getTickets(getAuthenticatedUser(), null));
  }

  @GetMapping("/showtime/{showtimeId}")
  public ResponseEntity<TicketResponseDto> getTicketsByAuthenticatedUserAndShowtimeId(@PathVariable Long showtimeId) {
    return ResponseEntity.ok(ticketService.getTickets(getAuthenticatedUser(), showtimeId));
  }

  private User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
    return principal.user();
  }

}
