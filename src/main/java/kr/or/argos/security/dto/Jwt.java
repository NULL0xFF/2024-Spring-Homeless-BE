package kr.or.argos.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(type = "string", example = "header.payload.signature")
public class Jwt {

  private final String header;
  private final String payload;
  private final String signature;

  public static Jwt fromString(String token) {
    String[] parts = token.split("\\.");
    if (parts.length != 3) {
      throw new IllegalArgumentException("Invalid token format");
    }
    return Jwt.builder().header(parts[0]).payload(parts[1]).signature(parts[2]).build();
  }

  @Override
  public String toString() {
    return header + "." + payload + "." + signature;
  }
}