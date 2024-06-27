package kr.or.argos.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.util.Date;
import kr.or.argos.domain.user.entity.User;
import kr.or.argos.domain.user.validation.Password;
import kr.or.argos.domain.user.validation.StudentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
public class UserRegistration implements UserRequest {

  @NotNull(message = "Username must not be null")
  @NotBlank(message = "Username must not be blank")
  private String username;

  @NotNull(message = "Password must not be null")
  @Password
  private String password;

  @NotNull(message = "Email must not be null")
  @Email
  private String email;

  @NotNull(message = "Student ID must not be null")
  @StudentId
  private Integer studentId;

  @NotNull(message = "Surname must not be null")
  private String surname;

  @NotNull(message = "Forename must not be null")
  @NotBlank(message = "Forename must not be blank")
  private String forename;

  @NotNull(message = "Birthday must not be null")
  @PastOrPresent(message = "Birthday must be a date in the past or in the present")
  @Schema(type = "string", format = "date", example = "yyyy-MM-dd")
  private Date birthday;

  public User toEntity(PasswordEncoder encoder) {
    // @formatter:off
    return User.builder()
        .username(username)
        .password(encoder.encode(password))
        .email(email)
        .studentId(studentId)
        .surname(surname)
        .forename(forename)
        .birthday(birthday)
        .build();
    // @formatter:on
  }
}