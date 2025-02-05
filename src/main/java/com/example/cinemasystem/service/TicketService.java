package com.example.cinemasystem.service;

import com.example.cinemasystem.converter.TicketMapper;
import com.example.cinemasystem.dto.TicketBookingRequestDto;
import com.example.cinemasystem.dto.TicketResponseDto;
import com.example.cinemasystem.model.Showtime;
import com.example.cinemasystem.model.Ticket;
import com.example.cinemasystem.repository.TicketRepo;
import com.example.user.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepo ticketRepo;
  private final ShowtimeService showtimeService;
  private final TicketMapper ticketMapper;

  @Transactional
  public TicketResponseDto bookTicket(TicketBookingRequestDto bookingRequest, User user) {
    Showtime showtime = showtimeService.reserveSeatsAndGetShowtime(bookingRequest.getShowtimeId(), bookingRequest.getSeatNumbers());
    Ticket ticket = ticketMapper.toTicket(bookingRequest, user, showtime);
    ticketRepo.save(ticket);
    return ticketMapper.toTicketResponseDto(List.of(ticket), user);
  }

  public TicketResponseDto getTickets(User user, Long showtimeId) {
    List<Ticket> tickets = ticketRepo.findTicketsByUserEmailWithOptionalShowtime(user.getEmail(), showtimeId);
    return ticketMapper.toTicketResponseDto(tickets, user);
  }

}
