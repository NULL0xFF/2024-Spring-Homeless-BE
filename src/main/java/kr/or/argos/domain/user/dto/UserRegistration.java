package kr.or.argos.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.regex.Pattern;
import kr.or.argos.domain.user.entity.User;
import kr.or.argos.global.exception.InvalidRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
public class UserRegistration implements UserRequest {

  @NotNull(message = "Enter an username.")
  @NotBlank(message = "Username cannot be blank.")
  private String username;
  @NotNull(message = "Enter a password.")
  @NotBlank(message = "Password cannot be blank.")
  private String password;
  @NotNull(message = "Enter a student ID.")
  private Integer studentId;
  @NotNull(message = "Enter a name.")
  @NotBlank(message = "Name cannot be blank.")
  private String name;

  public User toEntity(PasswordEncoder encoder) {
    validate();
    // @formatter:off
    return User.builder()
        .username(username)
        .password(encoder.encode(password))
        .studentId(studentId)
        .name(name)
        .build();
    // @formatter:on
  }

  private void validateField(String field, String fieldName) {
    if (field == null || field.isEmpty()) {
      throw new InvalidRequestException(fieldName + " is missing");
    }
  }

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

  /**
   * Ensures the student ID is 9 digits.
   */
  private void validateStudentId() {
    Pattern studentIdPattern = Pattern.compile("[1-9][0-9]{8}");
    if (!studentIdPattern.matcher(studentId.toString()).matches()) {
      throw new InvalidRequestException("Student ID must be 9 digits.");
    }
  }

  @Override
  public void validate() {
    // Check if the fields are empty or null
    validateField(username, "Username");
    validateField(password, "Password");
    validateField(name, "Name");
    if (studentId == null) {
      throw new InvalidRequestException("Student ID is missing");
    }    // Check if the password meets the requirements
    validatePassword();
    // Check if the student ID meets the requirements
    validateStudentId();
  }
}