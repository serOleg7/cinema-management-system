package com.example.cinemasystem.repository;

import com.example.cinemasystem.model.Showtime;
import com.example.cinemasystem.model.Ticket;
import com.example.user.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Long> {

  List<Ticket> findByUser(User user);

  List<Ticket> findByShowtime(Showtime showtime);

  boolean existsByShowtimeAndSeatNumber(Showtime showtime, String seatNumber);

  @Query("SELECT t FROM Ticket t WHERE t.user.email = :email")
  List<Ticket> findTicketsByUserEmail(String email);

}

