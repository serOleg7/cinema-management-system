package com.example.cinemasystem.controller;

import com.example.cinemasystem.model.Movie;
import com.example.cinemasystem.model.Showtime;
import com.example.cinemasystem.service.ShowtimeService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/showtimes")
@AllArgsConstructor
public class ShowtimeController {

  private final ShowtimeService showtimeService;

  @PostMapping
  public ResponseEntity<Showtime> addShowtime(@RequestBody Showtime showtime) {
    return ResponseEntity.ok(showtimeService.addShowtime(showtime));
  }

  @PutMapping
  public ResponseEntity<Showtime> updateShowtime(@RequestBody Showtime showtime) {
    return ResponseEntity.ok(showtimeService.updateShowtime(showtime));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
    showtimeService.deleteShowtime(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/movie/{movieId}")
  public ResponseEntity<List<Showtime>> getShowtimesByMovie(@PathVariable Long movieId) {
    Movie movie = new Movie();
    movie.setId(movieId);
    return ResponseEntity.ok(showtimeService.getShowtimesByMovie(movie));
  }

  @GetMapping("/theater/{theater}")
  public ResponseEntity<List<Showtime>> getShowtimesByTheater(@PathVariable String theater) {
    return ResponseEntity.ok(showtimeService.getShowtimesByTheater(theater));
  }

}

