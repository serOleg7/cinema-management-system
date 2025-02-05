package com.example.cinemasystem.model;

import com.example.user.model.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "showtime_id", nullable = false)
  private Showtime showtime;

  @ElementCollection
  @CollectionTable(name = "ticket_seats", joinColumns = @JoinColumn(name = "ticket_id"))
  @Column(name = "seat_number")
  private List<String> seatNumbers;

  @Column(name = "price", nullable = false)
  private Double totalPrice;

  @Column(name = "purchase_date", nullable = false, updatable = false)
  private LocalDateTime purchaseDate;

  @PrePersist
  protected void onCreate() {
    this.purchaseDate = LocalDateTime.now();
  }

}
