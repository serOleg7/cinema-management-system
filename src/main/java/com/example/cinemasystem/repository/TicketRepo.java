package com.example.cinemasystem.repository;

import com.example.cinemasystem.model.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Long> {

  @Query("""
          SELECT DISTINCT t 
          FROM Ticket t
          JOIN FETCH t.showtime s
          JOIN FETCH s.movie m
          LEFT JOIN FETCH t.seatNumbers
          WHERE t.user.email = :email
            AND (:showtimeId IS NULL OR s.id = :showtimeId)
      """)
  List<Ticket> findTicketsByUserEmailWithOptionalShowtime(String email, Long showtimeId);

}

