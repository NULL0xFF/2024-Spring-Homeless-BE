package kr.or.argos.security.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.or.argos.security.service.TokenService;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtRequestFilter extends OncePerRequestFilter {

  private final TokenService tokenService;

  public JwtRequestFilter(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest,
      @NonNull HttpServletResponse httpServletResponse, @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    // Resolve the token from the request
    String token = tokenService.resolveToken(httpServletRequest);

    try {
      // If the token is not null, validate the token and set the authentication
      if (token != null) {
        tokenService.validateToken(token);
        SecurityContextHolder.getContext().setAuthentication(tokenService.getAuthentication(token));
      }
    } catch (JwtException | IllegalArgumentException ex) {
      // If the token is invalid, clear the context and send an error response
      SecurityContextHolder.clearContext();
      httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
      return;
    }

    // Continue the filter chain
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}