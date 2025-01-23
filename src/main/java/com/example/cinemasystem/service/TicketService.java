package com.example.cinemasystem.service;

import com.example.cinemasystem.converter.TicketMapper;
import com.example.cinemasystem.dto.TicketResponseDto;
import com.example.cinemasystem.model.Showtime;
import com.example.cinemasystem.model.Ticket;
import com.example.cinemasystem.repository.TicketRepo;
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
  public TicketResponseDto bookTicket(Ticket ticket) {
    Showtime showtime = showtimeService.getShowtimeByIdWithMovie(ticket.getShowtime().getId());
    validateTicket(showtime, ticket.getSeatNumber());
    showtime.setAvailableSeats(showtime.getAvailableSeats() - 1);
    ticket.setShowtime(showtime);
    return ticketMapper.toTicketResponseDto(ticketRepo.save(ticket));
  }

  public List<TicketResponseDto> getTicketsByUserEmail(String email) {
    List<Ticket> tickets = ticketRepo.findTicketsByUserEmail(email);
    return ticketMapper.toTicketResponseDtoList(tickets);
  }

  public List<TicketResponseDto> getTicketsByShowtime(Long showtimeId) {
    Showtime showtime = showtimeService.getShowtimeByIdWithMovie(showtimeId);
    List<Ticket> tickets = ticketRepo.findByShowtime(showtime);
    return ticketMapper.toTicketResponseDtoList(tickets);
  }

  private void validateTicket(Showtime showtime, String seatNumber) {
    if (showtime.getAvailableSeats() <= 0) {
      throw new IllegalStateException("No available seats for this showtime");
    }
    if (ticketRepo.existsByShowtimeAndSeatNumber(showtime, seatNumber)) {
      throw new IllegalStateException("Seat already booked for this showtime");
    }
  }
}
