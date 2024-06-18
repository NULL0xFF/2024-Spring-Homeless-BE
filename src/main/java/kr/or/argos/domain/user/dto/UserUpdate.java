package kr.or.argos.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.regex.Pattern;
import kr.or.argos.global.exception.InvalidRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdate implements UserRequest {

  @NotNull(message = "Enter your username.")
  @NotBlank(message = "Username cannot be blank.")
  private String username;
  @NotNull(message = "Enter your password.")
  @NotBlank(message = "Password cannot be blank.")
  private String password;

  /**
   * Ensures the password meets the policy based on NIST SP 800-63B guidelines.
   * <p>
   * NIST SP 800-63B 가이드라인을 기반으로 비밀번호 정책을 충족하는지 확인합니다.
   *
   * @throws InvalidRequestException if the password does not meet the requirements
   * @see <a href="https://pages.nist.gov/800-63-3/sp800-63b.html">NIST SP 800-63B</a>
   */
  private void validatePassword() {
    Pattern passwordPattern = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{12,}$");
    if (!passwordPattern.matcher(password).matches()) {
      throw new InvalidRequestException(
          "Password must be at least 12 characters long and include an uppercase letter, a lowercase letter, a digit, and a special character.");
    }
  }

  @Override
  public void validate() {
    if (username == null || username.isEmpty()) {
      throw new InvalidRequestException("Username is missing");
    }
    if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Password is missing");
    }
    validatePassword();
  }
}