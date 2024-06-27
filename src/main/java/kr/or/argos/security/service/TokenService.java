package kr.or.argos.security.service;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import kr.or.argos.global.exception.InvalidTokenException;
import kr.or.argos.security.entity.Token;
import kr.or.argos.security.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final UserDetailsService myUserDetails;
  private final SecuritySettingsService securitySettingsService;
  private final TokenRepository tokenRepository;

  private SecretKey key;
  @Value("${security.jwt.token.expire-length:expire-length}")
  private long expireLength;

  @PostConstruct
  private void init() {
    key = securitySettingsService.getSecretKey();
  }

  @Transactional
  public Token issueToken(UserDetails userDetails) {
    // Check if the token already exists
    Token token = tokenRepository.findByUsername(userDetails.getUsername()).orElse(null);

    // If the token does not exist, create a new token
    if (token == null) {
      token = Token.createFromTokenString(userDetails.getUsername(),
          createTokenString(userDetails));
    } else {
      // If the token exists, update the token
      token.updateFromTokenString(createTokenString(userDetails));
    }

    // Save the token
    tokenRepository.save(token);

    return token;
  }

  @Transactional(readOnly = true)
  public void validateToken(String token) {
    // Get the username from the token
    String username = resolveUsername(token);

    // Check if the token exists
    Token storedToken = tokenRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("Token not found"));

    // Check if the token is valid
    if (!storedToken.toTokenString().toString().equals(token)) {
      throw new InvalidTokenException("Invalid token");
    }
  }

  private String createTokenString(UserDetails userDetails) {
    Date now = new Date();
    // @formatter:off
    return Jwts.builder()
        .header().add("typ", "JWT").and()
        .subject(userDetails.getUsername())
        .claim("auth", userDetails.getAuthorities())
        .issuedAt(now)
        .expiration(new Date(now.getTime() + expireLength))
        .signWith(key)
        .compact();
    // @formatter:on
  }

  public String resolveToken(HttpServletRequest req) {
    // Get the authorization header from the request
    String bearerToken = req.getHeader("Authorization");

    // Extract the token from the authorization header
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    } else {
      return null;
    }
  }

  public String resolveUsername(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
  }

  @Transactional(readOnly = true)
  public Authentication getAuthentication(String token) {
    UserDetails userDetails = myUserDetails.loadUserByUsername(resolveUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }
}