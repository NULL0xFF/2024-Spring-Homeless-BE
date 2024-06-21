package kr.or.argos.security.provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import kr.or.argos.global.exception.EntryNotFoundException;
import kr.or.argos.security.dto.Jwt;
import kr.or.argos.security.entity.SecuritySettings;
import kr.or.argos.security.repository.SecuritySettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class JwtProvider {

  private final UserDetailsService myUserDetails;
  private final SecuritySettingsRepository securitySettingsRepository;
  private SecretKey key;
  @Value("${security.jwt.token.expire-length:expire-length}")
  private long validityInMilliseconds;

  @PostConstruct
  public void init() {
    // The key is stored in the database.
    // If the key is not found in the database, a new key is generated and stored in the database.
    key = securitySettingsRepository.findByName("secretKey").map(
        settings -> (SecretKey) new SecretKeySpec(Base64.getDecoder().decode(settings.getValue()),
            "HmacSHA256")).orElseGet(() -> {
      SecretKey newKey = SIG.HS256.key().build();
      SecuritySettings newSettings = SecuritySettings.builder().name("secretKey")
          .value(Base64.getEncoder().encodeToString(newKey.getEncoded())).build();
      securitySettingsRepository.save(newSettings);
      return newKey;
    });
  }

  public void refreshKey() {
    key = SIG.HS256.key().build();
    SecuritySettings secretKeySetting = securitySettingsRepository.findByName("secretKey")
        .orElseThrow(() -> new EntryNotFoundException("secretKey setting not found"));
    secretKeySetting.setValue(Base64.getEncoder().encodeToString(key.getEncoded()));
    securitySettingsRepository.save(secretKeySetting);
  }

  public Jwt createToken(UserDetails userDetails) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + validityInMilliseconds);
    return Jwt.fromString(
        // @formatter:off
        Jwts.builder()
            .subject(userDetails.getUsername())
            .claim("auth", userDetails.getAuthorities())
            .issuedAt(now)
            .expiration(expiration)
            .signWith(key)
            .compact()
        // @formatter:on
    );
  }

  @Transactional(readOnly = true)
  public Authentication getAuthentication(String token) {
    UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public void validateToken(String token) {
    Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
  }
}