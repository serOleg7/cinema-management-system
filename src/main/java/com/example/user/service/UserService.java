package com.example.user.service;

import com.example.user.dto.UserRegistrationDto;
import com.example.user.model.User;
import com.example.user.model.enums.Role;
import com.example.user.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;

  public User registerCustomer(UserRegistrationDto dto) {
    return registerUser(dto, Role.CUSTOMER);
  }

  public User registerAdmin(UserRegistrationDto dto) {
    return registerUser(dto, Role.ADMIN);
  }

  private User registerUser(UserRegistrationDto dto, Role role) {
    validateUniqueEmail(dto.getEmail());

    User user = User.builder()
        .email(dto.getEmail())
        .password(passwordEncoder.encode(dto.getPassword()))
        .name(dto.getName())
        .role(role)
        .build();

    return userRepo.save(user);
  }

  private void validateUniqueEmail(String email) {
    if (userRepo.existsByEmail(email)) {
      throw new RuntimeException("User with this email already exists");
    }
  }

  public User getUserByEmail(String email) {
    return userRepo.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
  }

}
