package kr.or.argos.security.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.or.argos.security.provider.JwtProvider;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;

  public JwtRequestFilter(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest,
      @NonNull HttpServletResponse httpServletResponse, @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String token = jwtProvider.resolveToken(httpServletRequest);
    try {
      if (token != null) {
        jwtProvider.validateToken(token);
        Authentication auth = jwtProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (JwtException | IllegalArgumentException ex) {
      SecurityContextHolder.clearContext();
      httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
      return;
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}