package com.example.user.repository;

import com.example.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
  boolean existsByEmail(String email);

}
