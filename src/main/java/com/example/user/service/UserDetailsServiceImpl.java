package com.example.user.service;

import com.example.user.model.User;
import com.example.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepo userRepo;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepo.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    String[] roles = new String[]{"ROLE_" + user.getRole().name().toUpperCase()};

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPassword(),
        AuthorityUtils.createAuthorityList(roles)
    );
  }

}

