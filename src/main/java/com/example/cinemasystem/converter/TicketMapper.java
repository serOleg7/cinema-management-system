package com.example.cinemasystem.converter;

import com.example.cinemasystem.dto.MovieDto;
import com.example.cinemasystem.dto.ShowtimeDto;
import com.example.cinemasystem.dto.TicketResponseDto;
import com.example.cinemasystem.dto.UserDto;
import com.example.cinemasystem.model.Ticket;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

  public TicketResponseDto toTicketResponseDto(Ticket ticket) {
    TicketResponseDto dto = new TicketResponseDto();

    dto.setId(ticket.getId());

    // User mapping
    UserDto userDto = new UserDto();
    userDto.setId(ticket.getUser().getId());
    userDto.setName(ticket.getUser().getName());
    userDto.setEmail(ticket.getUser().getEmail());
    dto.setUser(userDto);

    // Showtime mapping
    ShowtimeDto showtimeDto = new ShowtimeDto();
    showtimeDto.setId(ticket.getShowtime().getId());
    showtimeDto.setTheater(ticket.getShowtime().getTheater());
    showtimeDto.setStartTime(ticket.getShowtime().getStartTime());
    showtimeDto.setEndTime(ticket.getShowtime().getEndTime());

    // Movie mapping
    MovieDto movieDto = new MovieDto();
    movieDto.setTitle(ticket.getShowtime().getMovie().getTitle());
    movieDto.setGenre(ticket.getShowtime().getMovie().getGenre());
    showtimeDto.setMovie(movieDto);

    dto.setShowtime(showtimeDto);

    dto.setSeatNumber(ticket.getSeatNumber());
    dto.setPrice(ticket.getPrice());

    return dto;
  }

  public List<TicketResponseDto> toTicketResponseDtoList(List<Ticket> tickets) {
    return tickets.stream()
        .map(this::toTicketResponseDto)
        .toList();
  }

}

