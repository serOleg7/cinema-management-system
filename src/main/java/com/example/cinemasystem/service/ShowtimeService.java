package com.example.cinemasystem.service;

import com.example.cinemasystem.model.Movie;
import com.example.cinemasystem.model.Showtime;
import com.example.cinemasystem.repository.ShowtimeRepo;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShowtimeService {

  private final ShowtimeRepo showtimeRepo;
  private final MovieService movieService;

  @Transactional
  public Showtime addShowtime(Showtime showtime) {
    Movie movie = movieService.getMovieById(showtime.getMovie().getId());
    showtime.setMovie(movie);
    validateShowtime(showtime);
    return showtimeRepo.save(showtime);
  }

  // TODO: Showtime Update-Delete Process
  // Prevent updating showtimes that have sold tickets
  // Provide clear error messages when updates/delete are blocked due to sold tickets

  @Transactional
  public Showtime updateShowtime(Showtime showtime) {
    Showtime existingShowtime = getShowtimeById(showtime.getId());

    Movie movie = movieService.getMovieById(showtime.getMovie().getId());

    existingShowtime.setMovie(movie);
    existingShowtime.setStartTime(showtime.getStartTime());
    existingShowtime.setEndTime(showtime.getEndTime());
    existingShowtime.setAvailableSeats(showtime.getAvailableSeats());

    validateShowtime(showtime);
    return existingShowtime;
  }

  @Transactional
  public void deleteShowtime(Long id) {
    if (!showtimeRepo.existsById(id)) {
      throw new EntityNotFoundException("Showtime not found");
    }
    showtimeRepo.deleteById(id);
  }

  public List<Showtime> getShowtimesByMovie(Movie movie) {
    return showtimeRepo.findByMovie(movie);
  }

  public List<Showtime> getShowtimesByTheater(String theater) {
    return showtimeRepo.findByTheater(theater);
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public Showtime reserveSeatsAndGetShowtime(Long showtimeId, List<String> seatNumbers) {
    int updatedRows = showtimeRepo.tryReserveSeats(showtimeId, seatNumbers.size(), seatNumbers);
    if (updatedRows == 0) {
      throw new IllegalStateException("Unable to reserve seats. They may be already taken or showtime is full.");
    }
    return getShowtimeById(showtimeId);
  }

  private void validateShowtime(Showtime showtime) {
    List<Showtime> overlappingShowtimes = showtimeRepo.findByTheaterAndStartTimeBetween(
            showtime.getTheater(),
            showtime.getStartTime(),
            showtime.getEndTime()
        ).stream()
        .filter(existingShowtime -> !existingShowtime.getId().equals(showtime.getId()))
        .toList();
    if (!overlappingShowtimes.isEmpty()) {
      throw new IllegalArgumentException("Overlapping showtimes exist for this theater");
    }
  }

  private Showtime getShowtimeById(Long id) {
    return showtimeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Showtime not found"));
  }

}

