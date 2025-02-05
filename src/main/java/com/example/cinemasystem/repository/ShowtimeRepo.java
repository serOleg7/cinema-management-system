package com.example.cinemasystem.repository;

import com.example.cinemasystem.model.Movie;
import com.example.cinemasystem.model.Showtime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepo extends JpaRepository<Showtime, Long> {

  List<Showtime> findByMovie(Movie movie);

  List<Showtime> findByTheater(String theater);

  List<Showtime> findByTheaterAndStartTimeBetween(String theater, LocalDateTime start, LocalDateTime end);

  @Query("SELECT s FROM Showtime s JOIN FETCH s.movie WHERE s.id = :showtimeId")
  Optional<Showtime> findByIdWithMovie(Long showtimeId);

  @Modifying
  @Query("""
          UPDATE Showtime s 
          SET s.availableSeats = s.availableSeats - :seatsCount 
          WHERE s.id = :showtimeId 
            AND s.availableSeats >= :seatsCount 
            AND NOT EXISTS (
                SELECT 1 
                FROM Ticket t 
                JOIN t.seatNumbers sn 
                WHERE t.showtime.id = :showtimeId 
                  AND sn IN :seatNumbers
          )
      """)
  int tryReserveSeats(Long showtimeId, int seatsCount, List<String> seatNumbers);

}

