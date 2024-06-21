package kr.or.argos.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import kr.or.argos.domain.common.entity.BaseEntity;
import kr.or.argos.security.dto.TokenString;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Token extends BaseEntity {

  @Column(unique = true)
  @NotNull
  private String username;
  @NotNull
  private String header;
  @NotNull
  private String payload;
  @NotNull
  private String signature;

  public static Token createFromTokenString(String username, String auth) {
    String[] parts = auth.split("\\.");
    if (parts.length != 3) {
      throw new IllegalArgumentException("Invalid token format");
    }
    return Token.builder().username(username).header(parts[0]).payload(parts[1]).signature(parts[2])
        .build();
  }

  public void updateFromTokenString(String auth) {
    String[] parts = auth.split("\\.");
    if (parts.length != 3) {
      throw new IllegalArgumentException("Invalid token format");
    }
    header = parts[0];
    payload = parts[1];
    signature = parts[2];
  }

  public TokenString toTokenString() {
    return new TokenString(header, payload, signature);
  }
}