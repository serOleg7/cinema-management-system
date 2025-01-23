package com.example.cinemasystem.service;

import com.example.cinemasystem.model.Movie;
import com.example.cinemasystem.repository.MovieRepo;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieService {

  private final MovieRepo movieRepo;

  public Movie addMovie(Movie movie) {
    return movieRepo.save(movie);
  }

  @Transactional
  public Movie updateMovie(Movie movieDetails) {
    Movie movie = getMovieById(movieDetails.getId());

    movie.setTitle(movieDetails.getTitle());
    movie.setGenre(movieDetails.getGenre());
    movie.setDuration(movieDetails.getDuration());
    movie.setRating(movieDetails.getRating());
    movie.setReleaseYear(movieDetails.getReleaseYear());

    return movie;
  }

  // TODO: Movie Deletion Process
  // Implement cascade delete for showtimes without tickets when movie is deleted
  // Prevent deletion of movies with showtimes that have sold tickets
  // Provide clear error messages when delete is blocked due to sold tickets
  @Transactional
  public void deleteMovie(Long id) {
    movieRepo.deleteById(id);
  }

  public List<Movie> getAllMovies() {
    return movieRepo.findAll();
  }

  public Movie getMovieById(Long id) {
    return movieRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Movie with id " + id + " not found"));
  }

}

