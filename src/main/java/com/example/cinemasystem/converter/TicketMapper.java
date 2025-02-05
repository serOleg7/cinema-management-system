package com.example.cinemasystem.converter;

import com.example.cinemasystem.dto.TicketBookingRequestDto;
import com.example.cinemasystem.dto.TicketDetailsDto;
import com.example.cinemasystem.dto.TicketResponseDto;
import com.example.cinemasystem.model.Showtime;
import com.example.cinemasystem.model.Ticket;
import com.example.user.model.User;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

  public TicketResponseDto toTicketResponseDto(List<Ticket> tickets, User user) {
    List<TicketDetailsDto> ticketDetails = tickets.stream()
        .map(this::mapTicketDetails)
        .toList();

    return new TicketResponseDto(
        user.getName(),
        user.getEmail(),
        ticketDetails
    );
  }

  private TicketDetailsDto mapTicketDetails(Ticket ticket) {
    return new TicketDetailsDto(
        ticket.getId(),
        ticket.getSeatNumbers(),
        ticket.getTotalPrice(),
        ticket.getShowtime().getTheater(),
        ticket.getShowtime().getStartTime(),
        ticket.getShowtime().getEndTime(),
        ticket.getShowtime().getMovie().getTitle(),
        ticket.getPurchaseDate()
    );
  }

  public Ticket toTicket(TicketBookingRequestDto bookingRequest, User user, Showtime showtime) {
    Ticket ticket = new Ticket();
    ticket.setUser(user);
    ticket.setShowtime(showtime);
    ticket.setSeatNumbers(bookingRequest.getSeatNumbers());
    ticket.setTotalPrice(bookingRequest.getPricePerSeat() * bookingRequest.getSeatNumbers().size());
    return ticket;
  }

}

