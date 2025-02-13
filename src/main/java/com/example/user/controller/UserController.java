package com.example.user.controller;

import com.example.user.dto.UserLoginDto;
import com.example.user.dto.UserRegistrationDto;
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
    userService.registerCustomer(userDto);
    String token = generateTokenForUser(userDto.getEmail(), userDto.getPassword());
    return ResponseEntity.ok(token);
  }

  @PostMapping("/register/admin")
  public ResponseEntity<?> registerAdmin(@Valid @RequestBody UserRegistrationDto userDto) {
    User registeredAdmin = userService.registerAdmin(userDto);
    return ResponseEntity.status(HttpStatus.OK).body(registeredAdmin);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserLoginDto loginDto) {
    try {
      String token = generateTokenForUser(loginDto.getEmail(), loginDto.getPassword());
      return ResponseEntity.ok(token);
    } catch (BadCredentialsException ex) {
      throw new BadCredentialsException("Invalid email or password");
    }
  }

  private String generateTokenForUser(String email, String password) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password)
    );
    return jwtTokenProvider.generateToken(authentication);
  }

}


