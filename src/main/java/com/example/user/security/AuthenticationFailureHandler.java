package com.example.user.security;

import com.example.common.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureHandler implements AuthenticationEntryPoint {

  private static final String FORWARD_REQUEST_URI = "jakarta.servlet.forward.request_uri";
  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {

    String path = (String) request.getAttribute(FORWARD_REQUEST_URI);
    if (path == null) {
      path = request.getRequestURI();
    }

    ApiException apiError = ApiException.withoutStackTrace(
        HttpStatus.UNAUTHORIZED,
        "Full authentication is required to access this resource",
        path
    );

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write(objectMapper.writeValueAsString(apiError));
  }

}

