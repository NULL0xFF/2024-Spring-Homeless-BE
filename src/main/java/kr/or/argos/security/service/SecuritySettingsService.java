package kr.or.argos.security.service;

import io.jsonwebtoken.Jwts.SIG;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import kr.or.argos.security.entity.SecuritySettings;
import kr.or.argos.security.repository.SecuritySettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SecuritySettingsService {

  private final SecuritySettingsRepository securitySettingsRepository;

  @Transactional
  protected SecretKey getSecretKey() {
    // @formatter:off
    return securitySettingsRepository
        // Find stored secret key from the database.
        .findByName("secretKey")
        .map(settings ->
            // If the key is found, create a new SecretKeySpec object with the key string.
            (SecretKey) new SecretKeySpec(
                Base64.getDecoder().decode(settings.getValue()),
                "HmacSHA256"
            ))
        .orElseGet(() ->
        {
          // If the key is not found, generate a new key and store it in the database.
          SecretKey newKey = SIG.HS256.key().build();
          SecuritySettings newSettings = SecuritySettings
              .builder()
              .name("secretKey")
              .value(Base64.getEncoder().encodeToString(newKey.getEncoded()))
              .build();
          securitySettingsRepository.save(newSettings);
          return newKey;
        });
    // @formatter:on
  }
}
