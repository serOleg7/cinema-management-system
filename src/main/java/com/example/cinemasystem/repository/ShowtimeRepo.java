package com.example.cinemasystem.repository;

import com.example.cinemasystem.model.Movie;
import com.example.cinemasystem.model.Showtime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepo extends JpaRepository<Showtime, Long> {

  List<Showtime> findByMovie(Movie movie);

  List<Showtime> findByTheater(String theater);

  List<Showtime> findByTheaterAndStartTimeBetween(String theater, LocalDateTime start, LocalDateTime end);

  @Query("SELECT s FROM Showtime s JOIN FETCH s.movie WHERE s.id = :id")
  Optional<Showtime> findByIdWithMovie(Long id);

}

