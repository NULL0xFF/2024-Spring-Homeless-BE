package kr.or.argos.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(type = "string", example = "header.payload.signature")
public record TokenString(String header, String payload, String signature) {

  @Override
  public String toString() {
    return header + "." + payload + "." + signature;
  }
}
