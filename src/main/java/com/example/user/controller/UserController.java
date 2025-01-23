package com.example.user.controller;

import com.example.user.dto.UserLoginDto;
import com.example.user.dto.UserRegistrationDto;
import com.example.user.exception.UnauthorizedException;
import com.example.user.model.User;
import com.example.user.security.JwtTokenProvider;
import com.example.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/register/customer")
  public ResponseEntity<?> registerCustomer(@Valid @RequestBody UserRegistrationDto userDto) {
    try {
      userService.registerCustomer(userDto);
      String token = generateTokenForUser(userDto.getEmail(), userDto.getPassword());
      return ResponseEntity.ok(token);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/register/admin")
  public ResponseEntity<?> registerAdmin(@Valid @RequestBody UserRegistrationDto userDto) {
    try {
      User registeredAdmin = userService.registerAdmin(userDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(registeredAdmin);
    } catch (UnauthorizedException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserLoginDto loginDto) {
    try {
      String token = generateTokenForUser(loginDto.getEmail(), loginDto.getPassword());
      return ResponseEntity.ok(token);
    } catch (BadCredentialsException ex) {
      throw new UsernameNotFoundException("Invalid email or password");
    }
  }

  private String generateTokenForUser(String email, String password) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password)
    );
    return jwtTokenProvider.generateToken(authentication);
  }

}


