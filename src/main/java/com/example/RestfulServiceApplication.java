package com.example;

import com.example.user.model.User;
import com.example.user.model.enums.Role;
import com.example.user.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.cinemasystem", "com.example.user"})
@EnableJpaRepositories(basePackages = {"com.example.cinemasystem", "com.example.user"})
public class RestfulServiceApplication implements CommandLineRunner {

  @Autowired
  UserRepo userRepo;

  @Autowired
  PasswordEncoder passwordEncoder;

  public static void main(String[] args) {
    SpringApplication.run(RestfulServiceApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    if (!userRepo.existsByEmail("admin@example.com")) {
      User admin = User.builder()
          .email("admin@example.com")
          .password(passwordEncoder.encode("admin"))
          .name("admin")
          .role(Role.ADMIN)
          .build();

      userRepo.save(admin);
    }

  }

}
