package com.example.cinemasystem.controller;

import com.example.cinemasystem.model.Movie;
import com.example.cinemasystem.service.MovieService;
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
@RequestMapping("/api/movies")
@AllArgsConstructor
public class MovieController {

  private final MovieService movieService;

  @PostMapping
  public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
    return ResponseEntity.ok(movieService.addMovie(movie));
  }

  @PutMapping
  public ResponseEntity<Movie> updateMovie(@RequestBody Movie movieDetails) {
    return ResponseEntity.ok(movieService.updateMovie(movieDetails));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
    movieService.deleteMovie(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<Movie>> getAllMovies() {
    return ResponseEntity.ok(movieService.getAllMovies());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
    return ResponseEntity.ok(movieService.getMovieById(id));
  }

}

