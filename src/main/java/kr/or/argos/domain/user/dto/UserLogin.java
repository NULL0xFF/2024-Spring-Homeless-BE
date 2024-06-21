package kr.or.argos.domain.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import kr.or.argos.global.exception.InvalidRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Valid
@Getter
@AllArgsConstructor
public class UserLogin implements UserRequest {

  @NotBlank(message = "Enter your username.")
  private String username;
  @NotBlank(message = "Enter your password.")
  private String password;

  @Override
  public void validate() {
    if (username == null || username.isEmpty()) {
      throw new InvalidRequestException("Username is missing");
    }
    if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Password is missing");
    }
  }
}