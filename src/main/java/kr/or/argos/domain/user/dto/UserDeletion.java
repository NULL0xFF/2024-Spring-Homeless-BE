package kr.or.argos.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDeletion implements UserRequest {

  @NotNull(message = "Username must not be null")
  @NotBlank(message = "Username must not be blank")
  private String username;

  @NotNull(message = "Password must not be null")
  @NotBlank(message = "Password must not be blank")
  private String password;
}