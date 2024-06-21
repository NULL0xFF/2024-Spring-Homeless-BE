package kr.or.argos.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.or.argos.global.exception.InvalidRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDeletion implements UserRequest {

  @NotNull(message = "Enter the username to delete.")
  @NotBlank(message = "Username cannot be blank.")
  private String username;

  @NotNull(message = "Enter the password to delete.")
  @NotBlank(message = "Password cannot be blank.")
  private String password;

  @Override
  public void validate() {
    if (username == null || username.isEmpty()) {
      throw new InvalidRequestException("Username is missing");
    }
    if (password == null || password.isEmpty()) {
      throw new InvalidRequestException("Password is missing");
    }
  }
}