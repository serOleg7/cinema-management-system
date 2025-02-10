package com.example.cinemasystem.service;

import static com.example.cinemasystem.service.ShowtimeService.OVERLAP_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

import com.example.cinemasystem.model.Movie;
import com.example.cinemasystem.model.Showtime;
import com.example.cinemasystem.repository.MovieRepo;
import com.example.cinemasystem.repository.ShowtimeRepo;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ShowtimeServiceIntegrationTest {

  @Autowired
  private ShowtimeService showtimeService;

  @Autowired
  private MovieRepo movieRepo;

  @Autowired
  private ShowtimeRepo showtimeRepo;

  private static Movie movie;
  private Showtime existingShowtime;
  private Showtime showtimeToUpdate;

  @BeforeAll
  static void setup(@Autowired MovieRepo movieRepo) {
    movie = new Movie();
    movie.setTitle("Test Movie");
    movie.setGenre("Action");
    movie.setDuration(120);
    movie.setRating(8.5);
    movie.setReleaseYear(2025);
    movieRepo.save(movie);
  }

  @BeforeEach
  void setupShowtimes() {
    existingShowtime = new Showtime();
    existingShowtime.setMovie(movie);
    existingShowtime.setTheater("Theater A");
    existingShowtime.setStartTime(LocalDateTime.of(2025, 2, 8, 14, 0));
    existingShowtime.setEndTime(LocalDateTime.of(2025, 2, 8, 16, 0));
    showtimeRepo.save(existingShowtime);

    showtimeToUpdate = new Showtime();
    showtimeToUpdate.setMovie(movie);
    showtimeToUpdate.setTheater("Theater A");
    showtimeToUpdate.setStartTime(LocalDateTime.of(2025, 2, 8, 16, 30));
    showtimeToUpdate.setEndTime(LocalDateTime.of(2025, 2, 8, 18, 0));
    showtimeToUpdate.setAvailableSeats(50);
    showtimeRepo.save(showtimeToUpdate);
  }

  @AfterEach
  void cleanup() {
    showtimeRepo.deleteAll();
  }

  @Test
  public void testAddShowtime_NoOverlap_Success() {
    Showtime newShowtime = new Showtime();
    newShowtime.setMovie(movie);
    newShowtime.setTheater("Theater A");
    newShowtime.setStartTime(LocalDateTime.of(2025, 2, 8, 11, 30));
    newShowtime.setEndTime(LocalDateTime.of(2025, 2, 8, 13, 30));

    // Act
    Showtime savedShowtime = showtimeService.addShowtime(newShowtime);

    // Assert
    assertNotNull(savedShowtime.getId());
  }

  @Test
  public void testAddShowtime_StartsBeforeAndEndsInside_ThrowsException() {
    Showtime overlappingShowtime = new Showtime();
    overlappingShowtime.setMovie(movie);
    overlappingShowtime.setTheater("Theater A");
    overlappingShowtime.setStartTime(LocalDateTime.of(2025, 2, 8, 13, 30));
    overlappingShowtime.setEndTime(LocalDateTime.of(2025, 2, 8, 15, 0));

    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> showtimeService.addShowtime(overlappingShowtime));

    assertEquals("Overlapping showtimes exist for this theater", exception.getMessage());
  }

  @Test
  public void testAddShowtime_StartsInsideAndEndsAfter_ThrowsException() {
    Showtime overlappingShowtime = new Showtime();
    overlappingShowtime.setMovie(movie);
    overlappingShowtime.setTheater("Theater A");
    overlappingShowtime.setStartTime(LocalDateTime.of(2025, 2, 8, 15, 30));
    overlappingShowtime.setEndTime(LocalDateTime.of(2025, 2, 8, 17, 0));

    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> showtimeService.addShowtime(overlappingShowtime));

    assertEquals("Overlapping showtimes exist for this theater", exception.getMessage());
  }

  @Test
  public void testAddShowtime_FullyOverlaps_ThrowsException() {
    Showtime overlappingShowtime = new Showtime();
    overlappingShowtime.setMovie(movie);
    overlappingShowtime.setTheater("Theater A");
    overlappingShowtime.setStartTime(LocalDateTime.of(2025, 2, 8, 13, 30));
    overlappingShowtime.setEndTime(LocalDateTime.of(2025, 2, 8, 16, 30));

    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> showtimeService.addShowtime(overlappingShowtime));

    assertEquals(OVERLAP_ERROR_MESSAGE, exception.getMessage());
  }

  @Test
  public void testAddShowtime_ExactMatch_ThrowsException() {
    Showtime overlappingShowtime = new Showtime();
    overlappingShowtime.setMovie(movie);
    overlappingShowtime.setTheater("Theater A");
    overlappingShowtime.setStartTime(LocalDateTime.of(2025, 2, 8, 14, 0));
    overlappingShowtime.setEndTime(LocalDateTime.of(2025, 2, 8, 16, 0));

    // Act & Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> showtimeService.addShowtime(overlappingShowtime));

    assertEquals(OVERLAP_ERROR_MESSAGE, exception.getMessage());
  }


  @Test
  public void testUpdateShowtime_NoOverlap_Success() {
    // Act
    showtimeToUpdate.setStartTime(LocalDateTime.of(2025, 2, 8, 18, 30));
    showtimeToUpdate.setEndTime(LocalDateTime.of(2025, 2, 8, 20, 0));
    Showtime updatedShowtime = showtimeService.updateShowtime(showtimeToUpdate);

    // Assert
    assertNotNull(updatedShowtime.getId());
    assertEquals(LocalDateTime.of(2025, 2, 8, 18, 30), updatedShowtime.getStartTime());
    assertEquals(LocalDateTime.of(2025, 2, 8, 20, 0), updatedShowtime.getEndTime());
  }

  @Test
  public void testUpdateShowtime_StartsBeforeAndEndsInside_ThrowsException() {
    // Act
    showtimeToUpdate.setStartTime(LocalDateTime.of(2025, 2, 8, 13, 30));
    showtimeToUpdate.setEndTime(LocalDateTime.of(2025, 2, 8, 15, 0));

    // Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> showtimeService.updateShowtime(showtimeToUpdate));

    assertEquals(OVERLAP_ERROR_MESSAGE, exception.getMessage());
  }

  @Test
  public void testUpdateShowtime_StartsInsideAndEndsAfter_ThrowsException() {
    // Act
    showtimeToUpdate.setStartTime(LocalDateTime.of(2025, 2, 8, 15, 30));
    showtimeToUpdate.setEndTime(LocalDateTime.of(2025, 2, 8, 17, 0));

    // Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> showtimeService.updateShowtime(showtimeToUpdate));

    assertEquals(OVERLAP_ERROR_MESSAGE, exception.getMessage());
  }

  @Test
  public void testUpdateShowtime_FullyOverlaps_ThrowsException() {
    // Act
    showtimeToUpdate.setStartTime(LocalDateTime.of(2025, 2, 8, 13, 30));
    showtimeToUpdate.setEndTime(LocalDateTime.of(2025, 2, 8, 16, 30));

    // Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> showtimeService.updateShowtime(showtimeToUpdate));

    assertEquals(OVERLAP_ERROR_MESSAGE, exception.getMessage());
  }

  @Test
  public void testUpdateShowtime_ExactMatch_ThrowsException() {
    // Act
    showtimeToUpdate.setStartTime(LocalDateTime.of(2025, 2, 8, 14, 0));
    showtimeToUpdate.setEndTime(LocalDateTime.of(2025, 2, 8, 16, 0));

    // Assert
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> showtimeService.updateShowtime(showtimeToUpdate));

    assertEquals(OVERLAP_ERROR_MESSAGE, exception.getMessage());
  }

}