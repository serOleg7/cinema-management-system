package com.example.user.configuration;

import com.example.user.model.enums.Role;
import com.example.user.security.AuthenticationFailureHandler;
import com.example.user.security.AuthorizationFailureHandler;
import com.example.user.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private static final String[] ADMIN_ENDPOINTS = {"/api/users/register/admin", "/api/movies/**", "/api/showtimes/**"};
  private static final String[] CUSTOMER_ENDPOINTS = {"/api/tickets/**"};
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationFailureHandler authenticationFailureHandler;
  private final AuthorizationFailureHandler authorizationFailureHandler;
  @Value("${app.security.public-endpoints}")
  private String[] publicEndpoints;
  @Value("${spring.profiles.active:Unknown}")
  private String activeProfile;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(publicEndpoints).permitAll()
            .requestMatchers(ADMIN_ENDPOINTS).hasRole(Role.ADMIN.name())
            .requestMatchers(CUSTOMER_ENDPOINTS).hasRole(Role.CUSTOMER.name())
            .requestMatchers(HttpMethod.GET, "/api/movies/**").hasAnyRole(Role.CUSTOMER.name(), Role.ADMIN.name())
            .requestMatchers(HttpMethod.GET, "/api/showtimes/**").hasAnyRole(Role.CUSTOMER.name(), Role.ADMIN.name())
            .requestMatchers("/api/**").authenticated()
            .anyRequest().authenticated())
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(authenticationFailureHandler)
            .accessDeniedHandler(authorizationFailureHandler)
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    if ("dev".equals(activeProfile)) {
      http.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
    }

    return http.build();
  }

}

