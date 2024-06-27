package kr.or.argos.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.or.argos.security.service.TokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

  private final TokenService tokenService;

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

      // Continue the filter chain
      filterChain.doFilter(httpServletRequest, httpServletResponse);
    } catch (ExpiredJwtException e) {
      // If the token is expired, clear the security context and return a 400 status code
      SecurityContextHolder.clearContext();
      httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      httpServletResponse.setContentType("plain/text");
      httpServletResponse.getWriter().write("Expired token");
    } catch (JwtException | IllegalArgumentException e) {
      // If the token is invalid, clear the security context and return a 400 status code
      SecurityContextHolder.clearContext();
      httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      httpServletResponse.setContentType("plain/text");
      httpServletResponse.getWriter().write("Invalid token");
    }
  }
}